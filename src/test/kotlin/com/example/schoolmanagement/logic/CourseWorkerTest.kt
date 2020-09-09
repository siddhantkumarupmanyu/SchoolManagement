package com.example.schoolmanagement.logic

import com.example.schoolmanagement.database.CourseRepository
import com.example.schoolmanagement.entity.*
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.`when`


class CourseWorkerTest {

    private lateinit var courseWorker: CourseWorker
    private lateinit var course: Course

    private lateinit var repository: CourseRepository
    private lateinit var professorWorker: ProfessorWorker

    @Before
    fun setUp() {
        repository = Mockito.mock(CourseRepository::class.java)
        professorWorker = Mockito.mock(ProfessorWorker::class.java)
        courseWorker = CourseWorker(repository, professorWorker)


        val startDate = Date(9, 8, 2020)
        val endDate = Date(9, 9, 2020)

        course = Course(
            NOT_DEFINED,
            "name",
            10,
            100,
            startDate,
            endDate
        )
    }

    @Test
    fun addCourse_GetCourse() {
        val courseId0 = course.copy(code = 0)

        Mockito.`when`(repository.addCourse(course)).thenReturn(Success(0))
        Mockito.`when`(repository.getCourse(0)).thenReturn(Success(courseId0))

        val addCourseRes = courseWorker.addCourse(course)
        Assert.assertTrue(addCourseRes is Success)
        addCourseRes as Success
        MatcherAssert.assertThat(addCourseRes, Matchers.`is`(Success(courseId0)))

        val getCourseRes = courseWorker.getCourse(0)
        Assert.assertTrue(getCourseRes is Success)
        getCourseRes as Success
        MatcherAssert.assertThat(getCourseRes, Matchers.`is`(Success(courseId0)))
    }

    @Test
    fun addCourseError_ByRepository_GetCourseError() {
        Mockito.`when`(repository.addCourse(course)).thenReturn(Error(UNABLE_TO_ADD))
        Mockito.`when`(repository.getCourse(0)).thenReturn(Error(NO_ENTRY))

        val addCourseRes = courseWorker.addCourse(course)
        Assert.assertTrue(addCourseRes is Error)
        addCourseRes as Error
        MatcherAssert.assertThat(addCourseRes, Matchers.`is`(Error(UNABLE_TO_ADD)))

        val getCourseRes = courseWorker.getCourse(0)
        Assert.assertTrue(getCourseRes is Error)
        getCourseRes as Error
        MatcherAssert.assertThat(getCourseRes, Matchers.`is`(Error(NO_ENTRY)))
    }

    @Test
    fun addCourseErrors_ByRepository_GetCourseErrors() {
        Mockito.`when`(repository.addCourse(course)).thenReturn(
            Errors<Int>(listOf(Error(UNABLE_TO_ADD), Error("SecondError")))
        )
        Mockito.`when`(repository.getCourse(0)).thenReturn(
            Errors<Course>(listOf(Error(NO_ENTRY), Error("SecondError")))
        )

        val addRes = courseWorker.addCourse(course)
        Assert.assertTrue(addRes is Errors)
        addRes as Errors
        MatcherAssert.assertThat(
            addRes,
            Matchers.`is`(Errors<Course>(listOf(Error(UNABLE_TO_ADD), Error("SecondError"))))
        )

        val getStudentRes = courseWorker.getCourse(0)
        Assert.assertTrue(getStudentRes is Errors)
        getStudentRes as Errors
        MatcherAssert.assertThat(
            getStudentRes,
            Matchers.`is`(Errors<Course>(listOf(Error(NO_ENTRY), Error("SecondError"))))
        )
    }


    @Test
    fun getProfessors_Error_FromRepo() {
        `when`(repository.getProfessors(ArgumentMatchers.anyInt())).thenReturn(Error("Some Error"))

        val (_, error) = courseWorker.getProfessors(0)
        assertThat(error.errors, `is`(listOf(Error<List<Professor>>("Some Error"))))

        val errorList = listOf(Error<List<Int>>("Some Error"), Error<List<Int>>("Second Error"))
        `when`(repository.getProfessors(ArgumentMatchers.anyInt())).thenReturn(Errors<List<Int>>(errorList))

        val (_, errors) = courseWorker.getProfessors(0)
        assertThat(
            errors.errors,
            `is`(listOf(Error<List<Professor>>("Some Error"), Error<List<Professor>>("Second Error")))
        )

    }

    @Test
    fun getProfessors_Error_FromProfessorWorker() {
        `when`(repository.getProfessors(ArgumentMatchers.anyInt())).thenReturn(Success(listOf(0, 1, 2)))
        `when`(professorWorker.getProfessor(ArgumentMatchers.anyInt())).thenReturn(Error("Some Error"))

        val (_, error1) = courseWorker.getProfessors(0)

        assertThat(
            error1.errors,
            `is`(
                listOf(
                    // Error("profId => error it got")
                    Error<List<Professor>>("0 => Some Error"),
                    Error<List<Professor>>("1 => Some Error"),
                    Error<List<Professor>>("2 => Some Error")
                )
            )
        )


        `when`(professorWorker.getProfessor(ArgumentMatchers.anyInt())).thenReturn(
            Errors(listOf(Error("Some Error"), Error("2nd Error")))
        )

        val (_, error2) = courseWorker.getProfessors(0)
        assertThat(
            error2.errors,
            `is`(
                listOf(
                    // Error("profId => error it got")
                    Error<List<Professor>>("0 => Some Error"),
                    Error<List<Professor>>("0 => 2nd Error"),
                    Error<List<Professor>>("1 => Some Error"),
                    Error<List<Professor>>("1 => 2nd Error"),
                    Error<List<Professor>>("2 => Some Error"),
                    Error<List<Professor>>("2 => 2nd Error")
                )
            )
        )


        `when`(professorWorker.getProfessor(0)).thenReturn(
            Errors(listOf(Error("Some Error"), Error("2nd Error")))
        )
        `when`(professorWorker.getProfessor(1)).thenReturn(Error("Some Error"))
        `when`(professorWorker.getProfessor(2)).thenReturn(Error("Some Error"))

        val (_, error3) = courseWorker.getProfessors(0)
        assertThat(
            error3.errors,
            `is`(
                listOf(
                    // Error("profId => error it got")
                    Error<List<Professor>>("0 => Some Error"),
                    Error<List<Professor>>("0 => 2nd Error"),
                    Error<List<Professor>>("1 => Some Error"),
                    Error<List<Professor>>("2 => Some Error")
                )
            )
        )

    }

    @Test
    fun getProfessors_Mixed_FromProfessorWorker() {
        `when`(repository.getProfessors(ArgumentMatchers.anyInt())).thenReturn(Success(listOf(0, 1, 2, 3)))

        val address = Address(
            "streetAddress",
            "city",
            "state",
            "123456",
            "country"
        )

        val dateOfBirth = Date(1, 0, 1998)

        val prof = Professor(
            NOT_DEFINED,
            "firstName",
            "lastName",
            "middleName",
            "9876543210",
            address,
            dateOfBirth,
            50000f
        )

        `when`(professorWorker.getProfessor(0)).thenReturn(
            Errors(listOf(Error("Some Error"), Error("2nd Error")))
        )
        `when`(professorWorker.getProfessor(1)).thenReturn(Success(prof))
        `when`(professorWorker.getProfessor(2)).thenReturn(Error("Some Error"))
        `when`(professorWorker.getProfessor(3)).thenReturn(Success(prof.copy(id = 3)))

        val (success, error) = courseWorker.getProfessors(0)
        assertThat(
            error.errors,
            `is`(
                listOf(
                    // Error("profId => error it got")
                    Error<List<Professor>>("0 => Some Error"),
                    Error<List<Professor>>("0 => 2nd Error"),
                    Error<List<Professor>>("2 => Some Error")
                )
            )
        )

        assertThat(success.data, `is`(listOf(prof, prof.copy(id = 3))))
    }

    private fun <T> anyObj(type: Class<T>): T = Mockito.any<T>(type)

}