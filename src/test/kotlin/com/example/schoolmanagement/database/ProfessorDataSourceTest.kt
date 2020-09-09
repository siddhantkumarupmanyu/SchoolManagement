package com.example.schoolmanagement.database

import com.example.schoolmanagement.entity.Address
import com.example.schoolmanagement.entity.Date
import com.example.schoolmanagement.entity.NOT_DEFINED
import com.example.schoolmanagement.entity.Professor
import com.example.schoolmanagement.logic.Error
import com.example.schoolmanagement.logic.NO_ENTRY
import com.example.schoolmanagement.logic.Result
import com.example.schoolmanagement.logic.Success
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.`is`
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class ProfessorDataSourceTest {

    private lateinit var professorDataSource: ProfessorDataSource
    private lateinit var professor: Professor

    @Before
    fun setUp() {
        professorDataSource = ProfessorDataSource()

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
            "firstName0",
            "lastName",
            "middleName",
            "9876543210",
            address,
            dateOfBirth,
            50000f
        )
    }

    @Test
    fun insertProfessor_Then_GetProfessor() {

        val idResult = professorDataSource.insert(professor)
        Assert.assertThat(idResult, Matchers.`is`(Success(0) as Result<Int>))

        val getStudent = professorDataSource.get(0)
        Assert.assertThat(getStudent, Matchers.`is`(Matchers.not(Success(professor) as Result<Professor>)))

        val studentWithId = professor.copy(id = 0)

        Assert.assertThat(getStudent, Matchers.`is`(Success(studentWithId) as Result<Professor>))

    }

    @Test
    fun insertProfessors_Then_GetProfessors() {
        Assert.assertThat(professorDataSource.insert(professor), Matchers.`is`(Success(0) as Result<Int>))
        Assert.assertThat(professorDataSource.insert(professor), Matchers.`is`(Success(1) as Result<Int>))
        Assert.assertThat(professorDataSource.insert(professor), Matchers.`is`(Success(2) as Result<Int>))
        Assert.assertThat(professorDataSource.insert(professor), Matchers.`is`(Success(3) as Result<Int>))

        Assert.assertThat(
            professorDataSource.get(0),
            Matchers.`is`(Success(professor.copy(id = 0)) as Result<Professor>)
        )
        Assert.assertThat(
            professorDataSource.get(1),
            Matchers.`is`(Success(professor.copy(id = 1)) as Result<Professor>)
        )
        Assert.assertThat(
            professorDataSource.get(2),
            Matchers.`is`(Success(professor.copy(id = 2)) as Result<Professor>)
        )
        Assert.assertThat(
            professorDataSource.get(3),
            Matchers.`is`(Success(professor.copy(id = 3)) as Result<Professor>)
        )

    }

    @Test
    fun getProfessorError() {
        Assert.assertThat(professorDataSource.get(0), Matchers.`is`(Error(NO_ENTRY)))

        Assert.assertThat(professorDataSource.insert(professor), Matchers.`is`(Success(0) as Result<Int>))
        Assert.assertThat(professorDataSource.get(10), Matchers.`is`(Error(NO_ENTRY)))
    }

    @Test
    fun checkDoesExist() {
        Assert.assertThat(professorDataSource.doesExist(0), Matchers.`is`(false))

        professorDataSource.insert(professor)
        Assert.assertThat(professorDataSource.doesExist(0), Matchers.`is`(true))

        Assert.assertThat(professorDataSource.doesExist(10), Matchers.`is`(false))
        professorDataSource.insert(professor)
        Assert.assertThat(professorDataSource.doesExist(10), Matchers.`is`(false))
        Assert.assertThat(professorDataSource.doesExist(1), Matchers.`is`(true))
    }

    @Test
    fun addProfessor_addCourses_getCourses() {
        val idResult = professorDataSource.insert(professor)
        assertTrue(idResult is Success)
        idResult as Success
        assertThat(idResult, `is`(Success(0)))

        val emptyGetRes = professorDataSource.getCourses(idResult.data)
        assertTrue(emptyGetRes is Success)
        emptyGetRes as Success
        assertThat(emptyGetRes, `is`(Success(emptyList<Int>())))

        val addCourseRes = professorDataSource.addCourse(idResult.data, 0)
        assertTrue(addCourseRes is Success)

        val getCourseRes = professorDataSource.getCourses(idResult.data)
        assertTrue(getCourseRes is Success)
        getCourseRes as Success
        assertThat(getCourseRes, `is`(Success(listOf(0))))

        professorDataSource.addCourse(idResult.data, 1)
        professorDataSource.addCourse(idResult.data, 2)
        professorDataSource.addCourse(idResult.data, 3)
        professorDataSource.addCourse(idResult.data, 10)

        val getCoursesRes = professorDataSource.getCourses(idResult.data)
        assertTrue(addCourseRes is Success)
        getCoursesRes as Success
        assertThat(getCoursesRes, `is`(Success(listOf(0, 1, 2, 3, 10))))
    }

    @Test
    fun addProfessors_addCourses_getCourses() {
        professorDataSource.insert(professor)
        professorDataSource.insert(professor)
        professorDataSource.insert(professor)
        professorDataSource.insert(professor)
        professorDataSource.insert(professor)

        for (i in 0..4) {
            val emptyGetRes = professorDataSource.getCourses(i)
            assertTrue(emptyGetRes is Success)
            emptyGetRes as Success
            assertThat(emptyGetRes, `is`(Success(emptyList<Int>())))
        }

        professorDataSource.addCourse(0, 10)
        professorDataSource.addCourse(0, 20)
        professorDataSource.addCourse(0, 30)

        professorDataSource.addCourse(1, 11)
        professorDataSource.addCourse(1, 21)

        professorDataSource.addCourse(2, 12)
        professorDataSource.addCourse(2, 22)
        professorDataSource.addCourse(2, 32)

        professorDataSource.addCourse(3, 10)

        val prof0 = professorDataSource.getCourses(0)
        assertTrue(prof0 is Success)
        prof0 as Success
        assertThat(prof0.data, `is`(listOf(10, 20, 30)))

        val prof1 = professorDataSource.getCourses(1)
        assertTrue(prof1 is Success)
        prof1 as Success
        assertThat(prof1.data, `is`(listOf(11, 21)))

        val prof2 = professorDataSource.getCourses(2)
        assertTrue(prof2 is Success)
        prof2 as Success
        assertThat(prof2.data, `is`(listOf(12, 22, 32)))

        val prof3 = professorDataSource.getCourses(3)
        assertTrue(prof3 is Success)
        prof3 as Success
        assertThat(prof3.data, `is`(listOf(10)))

        val prof4 = professorDataSource.getCourses(4)
        assertTrue(prof4 is Success)
        prof4 as Success
        assertThat(prof4.data, `is`(listOf()))

    }

    @Test
    fun addProf_addCourses_removeCourse(){
        professorDataSource.insert(professor)

        professorDataSource.addCourse(0, 10)
        professorDataSource.addCourse(0, 20)
        professorDataSource.addCourse(0, 30)

        val res = professorDataSource.getCourses(0)
        assertTrue(res is Success)
        res as Success
        assertThat(res.data, `is`(listOf(10, 20, 30)))

        professorDataSource.removeLastCourse(0)

        val res1 = professorDataSource.getCourses(0)
        assertTrue(res1 is Success)
        res1 as Success
        assertThat(res1.data, `is`(listOf(10, 20)))

    }

}