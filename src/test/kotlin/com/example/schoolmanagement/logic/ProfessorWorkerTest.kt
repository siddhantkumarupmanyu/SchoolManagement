package com.example.schoolmanagement.logic

import com.example.schoolmanagement.database.ProfessorRepository
import com.example.schoolmanagement.entity.*
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import java.time.Year

class ProfessorWorkerTest {

    private lateinit var professorWorker: ProfessorWorker
    private lateinit var professor: Professor

    private lateinit var repository: ProfessorRepository
    private lateinit var courseWorker: CourseWorker

    @Before
    fun setUp() {
        repository = Mockito.mock(ProfessorRepository::class.java)
        courseWorker = Mockito.mock(CourseWorker::class.java)
        professorWorker = ProfessorWorker(repository, courseWorker)

        val address = Address(
            "streetAddress",
            "city",
            "state",
            "123456",
            "country"
        )

        val dateOfBirth = Date(1, 0, 1998)

        professor = Professor(
            NOT_DEFINED,
            "firstName",
            "lastName",
            "middleName",
            "9876543210",
            address,
            dateOfBirth,
            50000f
        )
    }

    @Test
    fun addProfessor_GetProfessor() {
        val professorId0 = professor.copy(id = 0)
        professorId0.age = Year.now().value - professor.dateOfBirth.year

        Mockito.`when`(repository.addProfessor(professor)).thenReturn(Success(0))
        Mockito.`when`(repository.getProfessor(0)).thenReturn(Success(professorId0))

        val addProfRes = professorWorker.addProfessor(professor)
        Assert.assertTrue(addProfRes is Success)
        addProfRes as Success
        MatcherAssert.assertThat(addProfRes, Matchers.`is`(Success(professorId0)))
        // cause data class equal takes only constructor parameters
        MatcherAssert.assertThat(addProfRes.data.age, Matchers.`is`(professorId0.age))

        val getProfRes = professorWorker.getProfessor(0)
        Assert.assertTrue(getProfRes is Success)
        getProfRes as Success
        MatcherAssert.assertThat(getProfRes, Matchers.`is`(Success(professorId0)))
        MatcherAssert.assertThat(getProfRes.data.age, Matchers.`is`(professorId0.age))
    }

    @Test
    fun addProfessorError_ByRepository_GetProfessorError() {
        Mockito.`when`(repository.addProfessor(professor)).thenReturn(Error(UNABLE_TO_ADD))
        Mockito.`when`(repository.getProfessor(0)).thenReturn(Error(NO_ENTRY))

        val addProfRes = professorWorker.addProfessor(professor)
        Assert.assertTrue(addProfRes is Error)
        addProfRes as Error
        MatcherAssert.assertThat(addProfRes, Matchers.`is`(Error(UNABLE_TO_ADD)))

        val getProfRes = professorWorker.getProfessor(0)
        Assert.assertTrue(getProfRes is Error)
        getProfRes as Error
        MatcherAssert.assertThat(getProfRes, Matchers.`is`(Error(NO_ENTRY)))
    }

    @Test
    fun addProfessorErrors_ByRepository_GetProfessorErrors() {
        Mockito.`when`(repository.addProfessor(professor)).thenReturn(
            Errors<Int>(listOf(Error(UNABLE_TO_ADD), Error("SecondError")))
        )
        Mockito.`when`(repository.getProfessor(0)).thenReturn(
            Errors<Professor>(listOf(Error(NO_ENTRY), Error("SecondError")))
        )

        val addRes = professorWorker.addProfessor(professor)
        Assert.assertTrue(addRes is Errors)
        addRes as Errors
        MatcherAssert.assertThat(
            addRes,
            Matchers.`is`(Errors<Professor>(listOf(Error(UNABLE_TO_ADD), Error("SecondError"))))
        )

        val getProfRes = professorWorker.getProfessor(0)
        Assert.assertTrue(getProfRes is Errors)
        getProfRes as Errors
        MatcherAssert.assertThat(
            getProfRes,
            Matchers.`is`(Errors<Professor>(listOf(Error(NO_ENTRY), Error("SecondError"))))
        )
    }

    @Test
    fun addProfErrors_ByPersonValidator_GetProfError() {
        val wrongProf = professor.copy(firstName = "yoyo121", phoneNo = "21545sdsd4")

        Mockito.`when`(repository.addProfessor(professor)).thenReturn(Success(0))
        Mockito.`when`(repository.getProfessor(0)).thenReturn(Error(NO_ENTRY))

        val listOfErrors = listOf(
            Error<Professor>(ONLY_ALPHA),
            Error<Professor>(ONLY_DIGIT)
        )

        val addRes = professorWorker.addProfessor(wrongProf)
        Assert.assertTrue(addRes is Errors)
        addRes as Errors
        MatcherAssert.assertThat(addRes, Matchers.`is`(Errors(listOfErrors)))

        val getRes = professorWorker.getProfessor(0)
        Assert.assertTrue(getRes is Error)
        getRes as Error
        MatcherAssert.assertThat(getRes, Matchers.`is`(Error(NO_ENTRY)))
    }

    @Test
    fun getCourses_Error_FromRepo() {
        Mockito.`when`(repository.getCourses(ArgumentMatchers.anyInt())).thenReturn(Error("Some Error"))

        val (_, error) = professorWorker.getCourses(0)
        MatcherAssert.assertThat(error.errors, Matchers.`is`(listOf(Error<List<Course>>("Some Error"))))

        val errorList = listOf(Error<List<Int>>("Some Error"), Error<List<Int>>("Second Error"))
        Mockito.`when`(repository.getCourses(ArgumentMatchers.anyInt())).thenReturn(Errors<List<Int>>(errorList))

        val (_, errors) = professorWorker.getCourses(0)
        MatcherAssert.assertThat(
            errors.errors,
            Matchers.`is`(listOf(Error<List<Course>>("Some Error"), Error<List<Course>>("Second Error")))
        )

    }

    @Test
    fun getProfessors_Error_FromProfessorWorker() {
        Mockito.`when`(repository.getCourses(ArgumentMatchers.anyInt())).thenReturn(Success(listOf(0, 1, 2)))
        Mockito.`when`(courseWorker.getCourse(ArgumentMatchers.anyInt())).thenReturn(Error("Some Error"))

        val (_, error1) = professorWorker.getCourses(0)

        MatcherAssert.assertThat(
            error1.errors,
            Matchers.`is`(
                listOf(
                    // Error("profId => error it got")
                    Error<List<Course>>("0 => Some Error"),
                    Error<List<Course>>("1 => Some Error"),
                    Error<List<Course>>("2 => Some Error")
                )
            )
        )


        Mockito.`when`(courseWorker.getCourse(ArgumentMatchers.anyInt())).thenReturn(
            Errors(listOf(Error("Some Error"), Error("2nd Error")))
        )

        val (_, error2) = professorWorker.getCourses(0)
        MatcherAssert.assertThat(
            error2.errors,
            Matchers.`is`(
                listOf(
                    // Error("profId => error it got")
                    Error<List<Course>>("0 => Some Error"),
                    Error<List<Course>>("0 => 2nd Error"),
                    Error<List<Course>>("1 => Some Error"),
                    Error<List<Course>>("1 => 2nd Error"),
                    Error<List<Course>>("2 => Some Error"),
                    Error<List<Course>>("2 => 2nd Error")
                )
            )
        )


        Mockito.`when`(courseWorker.getCourse(0)).thenReturn(
            Errors(listOf(Error("Some Error"), Error("2nd Error")))
        )
        Mockito.`when`(courseWorker.getCourse(1)).thenReturn(Error("Some Error"))
        Mockito.`when`(courseWorker.getCourse(2)).thenReturn(Error("Some Error"))

        val (_, error3) = professorWorker.getCourses(0)
        MatcherAssert.assertThat(
            error3.errors,
            Matchers.`is`(
                listOf(
                    // Error("profId => error it got")
                    Error<List<Course>>("0 => Some Error"),
                    Error<List<Course>>("0 => 2nd Error"),
                    Error<List<Course>>("1 => Some Error"),
                    Error<List<Course>>("2 => Some Error")
                )
            )
        )

    }


    @Test
    fun getProfessors_Mixed_FromProfessorWorker() {
        Mockito.`when`(repository.getCourses(ArgumentMatchers.anyInt())).thenReturn(Success(listOf(0, 1, 2, 3)))

        val startDate = Date(9, 8, 2020)
        val endDate = Date(9, 9, 2020)

        val course = Course(
            NOT_DEFINED,
            "name",
            10,
            100,
            startDate,
            endDate
        )

        Mockito.`when`(courseWorker.getCourse(0)).thenReturn(
            Errors(listOf(Error("Some Error"), Error("2nd Error")))
        )
        Mockito.`when`(courseWorker.getCourse(1)).thenReturn(Success(course))
        Mockito.`when`(courseWorker.getCourse(2)).thenReturn(Error("Some Error"))
        Mockito.`when`(courseWorker.getCourse(3)).thenReturn(Success(course.copy(code = 3)))

        val (success, error) = professorWorker.getCourses(0)
        MatcherAssert.assertThat(
            error.errors,
            Matchers.`is`(
                listOf(
                    // Error("profId => error it got")
                    Error<List<Course>>("0 => Some Error"),
                    Error<List<Course>>("0 => 2nd Error"),
                    Error<List<Course>>("2 => Some Error")
                )
            )
        )

        MatcherAssert.assertThat(success.data, Matchers.`is`(listOf(course, course.copy(code = 3))))
    }

    private fun <T> anyObj(type: Class<T>): T = Mockito.any<T>(type)

}