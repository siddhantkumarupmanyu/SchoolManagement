package com.example.schoolmanagement.database

import com.example.schoolmanagement.entity.Course
import com.example.schoolmanagement.entity.Date
import com.example.schoolmanagement.entity.NOT_DEFINED
import com.example.schoolmanagement.logic.Error
import com.example.schoolmanagement.logic.NO_ENTRY
import com.example.schoolmanagement.logic.Result
import com.example.schoolmanagement.logic.Success
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CourseDataSourceTest {

    private lateinit var courseDataSource: CourseDataSource
    private lateinit var course: Course

    @Before
    fun setUp() {
        courseDataSource = CourseDataSource()

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
    fun insertCourse_Then_GetCourse() {

        val codeResult = courseDataSource.insert(course)
        Assert.assertThat(codeResult, Matchers.`is`(Success(0) as Result<Int>))

        val getCourse = courseDataSource.get(0)
        Assert.assertThat(getCourse, Matchers.`is`(Matchers.not(Success(course) as Result<Course>)))

        val studentWithId = course.copy(code = 0)

        Assert.assertThat(getCourse, Matchers.`is`(Success(studentWithId) as Result<Course>))

    }

    @Test
    fun insertCourses_Then_GetCourses() {
        Assert.assertThat(courseDataSource.insert(course), Matchers.`is`(Success(0) as Result<Int>))
        Assert.assertThat(courseDataSource.insert(course), Matchers.`is`(Success(1) as Result<Int>))
        Assert.assertThat(courseDataSource.insert(course), Matchers.`is`(Success(2) as Result<Int>))
        Assert.assertThat(courseDataSource.insert(course), Matchers.`is`(Success(3) as Result<Int>))

        Assert.assertThat(
            courseDataSource.get(0),
            Matchers.`is`(Success(course.copy(code = 0)) as Result<Course>)
        )
        Assert.assertThat(
            courseDataSource.get(1),
            Matchers.`is`(Success(course.copy(code = 1)) as Result<Course>)
        )
        Assert.assertThat(
            courseDataSource.get(2),
            Matchers.`is`(Success(course.copy(code = 2)) as Result<Course>)
        )
        Assert.assertThat(
            courseDataSource.get(3),
            Matchers.`is`(Success(course.copy(code = 3)) as Result<Course>)
        )

    }

    @Test
    fun getCourseError() {
        Assert.assertThat(courseDataSource.get(0), Matchers.`is`(Error(NO_ENTRY)))

        Assert.assertThat(courseDataSource.insert(course), Matchers.`is`(Success(0) as Result<Int>))
        Assert.assertThat(courseDataSource.get(10), Matchers.`is`(Error(NO_ENTRY)))
    }

    @Test
    fun checkDoesExist() {
        Assert.assertThat(courseDataSource.doesExist(0), Matchers.`is`(false))

        courseDataSource.insert(course)
        Assert.assertThat(courseDataSource.doesExist(0), Matchers.`is`(true))

        Assert.assertThat(courseDataSource.doesExist(10), Matchers.`is`(false))
        courseDataSource.insert(course)
        Assert.assertThat(courseDataSource.doesExist(10), Matchers.`is`(false))
        Assert.assertThat(courseDataSource.doesExist(1), Matchers.`is`(true))
    }

    @Test
    fun addCourse_addProfessor_getCourses() {
        val idResult = courseDataSource.insert(course)
        Assert.assertTrue(idResult is Success)
        idResult as Success
        MatcherAssert.assertThat(idResult, Matchers.`is`(Success(0)))

        val emptyGetRes = courseDataSource.getProfessors(idResult.data)
        Assert.assertTrue(emptyGetRes is Success)
        emptyGetRes as Success
        MatcherAssert.assertThat(emptyGetRes, Matchers.`is`(Success(emptyList<Int>())))

        val addProfRes = courseDataSource.addProfessor(idResult.data, 0)
        Assert.assertTrue(addProfRes is Success)

        val getProfRes = courseDataSource.getProfessors(idResult.data)
        Assert.assertTrue(getProfRes is Success)
        getProfRes as Success
        MatcherAssert.assertThat(getProfRes, Matchers.`is`(Success(listOf(0))))

        courseDataSource.addProfessor(idResult.data, 1)
        courseDataSource.addProfessor(idResult.data, 2)
        courseDataSource.addProfessor(idResult.data, 3)
        courseDataSource.addProfessor(idResult.data, 10)

        val getProfsRes = courseDataSource.getProfessors(idResult.data)
        Assert.assertTrue(addProfRes is Success)
        getProfsRes as Success
        MatcherAssert.assertThat(getProfsRes, Matchers.`is`(Success(listOf(0, 1, 2, 3, 10))))
    }

    @Test
    fun addCourses_addProfessors_getCourses() {
        courseDataSource.insert(course)
        courseDataSource.insert(course)
        courseDataSource.insert(course)
        courseDataSource.insert(course)
        courseDataSource.insert(course)

        for (i in 0..4) {
            val emptyGetRes = courseDataSource.getProfessors(i)
            Assert.assertTrue(emptyGetRes is Success)
            emptyGetRes as Success
            MatcherAssert.assertThat(emptyGetRes, Matchers.`is`(Success(emptyList<Int>())))
        }

        courseDataSource.addProfessor(0, 10)
        courseDataSource.addProfessor(0, 20)
        courseDataSource.addProfessor(0, 30)

        courseDataSource.addProfessor(1, 11)
        courseDataSource.addProfessor(1, 21)

        courseDataSource.addProfessor(2, 12)
        courseDataSource.addProfessor(2, 22)
        courseDataSource.addProfessor(2, 32)

        courseDataSource.addProfessor(3, 10)

        val course0 = courseDataSource.getProfessors(0)
        Assert.assertTrue(course0 is Success)
        course0 as Success
        MatcherAssert.assertThat(course0.data, Matchers.`is`(listOf(10, 20, 30)))

        val course1 = courseDataSource.getProfessors(1)
        Assert.assertTrue(course1 is Success)
        course1 as Success
        MatcherAssert.assertThat(course1.data, Matchers.`is`(listOf(11, 21)))

        val course2 = courseDataSource.getProfessors(2)
        Assert.assertTrue(course2 is Success)
        course2 as Success
        MatcherAssert.assertThat(course2.data, Matchers.`is`(listOf(12, 22, 32)))

        val course3 = courseDataSource.getProfessors(3)
        Assert.assertTrue(course3 is Success)
        course3 as Success
        MatcherAssert.assertThat(course3.data, Matchers.`is`(listOf(10)))

        val course4 = courseDataSource.getProfessors(4)
        Assert.assertTrue(course4 is Success)
        course4 as Success
        MatcherAssert.assertThat(course4.data, Matchers.`is`(listOf()))

    }

    @Test
    fun addProf_addCourses_removeCourse(){
        courseDataSource.insert(course)

        courseDataSource.addProfessor(0, 10)
        courseDataSource.addProfessor(0, 20)
        courseDataSource.addProfessor(0, 30)

        val res = courseDataSource.getProfessors(0)
        Assert.assertTrue(res is Success)
        res as Success
        MatcherAssert.assertThat(res.data, Matchers.`is`(listOf(10, 20, 30)))

        courseDataSource.removeLastProfessor(0)

        val res1 = courseDataSource.getProfessors(0)
        Assert.assertTrue(res1 is Success)
        res1 as Success
        MatcherAssert.assertThat(res1.data, Matchers.`is`(listOf(10, 20)))

    }

}