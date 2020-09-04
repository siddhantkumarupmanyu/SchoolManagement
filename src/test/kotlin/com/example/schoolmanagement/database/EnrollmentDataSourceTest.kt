package com.example.schoolmanagement.database

import com.example.schoolmanagement.entity.Date
import com.example.schoolmanagement.entity.Enroll
import com.example.schoolmanagement.logic.Result
import com.example.schoolmanagement.logic.Success
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Before
import org.junit.Test

class EnrollmentDataSourceTest {

    private lateinit var enrollmentDataSource: EnrollmentDataSource
    private lateinit var enroll: Enroll

    @Before
    fun setUp() {
        enrollmentDataSource = EnrollmentDataSource()

        val date = Date(1, 0, 1998)

        enroll = Enroll(
            0,
            0,
            date,
            5f
        )
    }

    @Test
    fun insertEnroll_Then_GetEnroll() {
        val enrollResult = enrollmentDataSource.insert(enroll)
        assertThat(enrollResult, `is`(Success(ENROLL_SUCCESS) as Result<*>))

        val enrollListResult = Success(listOf(enroll))

        val getEnrollList = enrollmentDataSource.get { studentId, courseCode ->
            studentId == enroll.studentId && courseCode == enroll.courseCode
        }
        assertThat(getEnrollList, `is`(enrollListResult as Result<List<Enroll>>))
    }

    @Test
    fun noEnroll() {
        assertThat(
            enrollmentDataSource.get { _, _ -> true },
            `is`(Success(emptyList<Enroll>()) as Result<List<Enroll>>)
        )

        enrollmentDataSource.insert(
            enroll.copy(studentId = 0, courseCode = 1)
        )
        enrollmentDataSource.insert(
            enroll.copy(studentId = 1, courseCode = 1)
        )
        enrollmentDataSource.insert(
            enroll.copy(studentId = 2, courseCode = 3)
        )

        assertThat(
            enrollmentDataSource.get { studentId, _ -> studentId == 10 },
            `is`(Success(emptyList<Enroll>()) as Result<List<Enroll>>)
        )

        assertThat(
            enrollmentDataSource.get { _, courseId -> courseId == 10 },
            `is`(Success(emptyList<Enroll>()) as Result<List<Enroll>>)
        )

        assertThat(
            enrollmentDataSource.get { studentId, courseId -> studentId == 10 && courseId == 10 },
            `is`(Success(emptyList<Enroll>()) as Result<List<Enroll>>)
        )

    }

    @Test
    fun checkDoesExist() {
        assertThat(enrollmentDataSource.doesExist(0, 0), `is`(false))

        enrollmentDataSource.insert(
            enroll.copy(studentId = 0, courseCode = 0)
        )
        assertThat(enrollmentDataSource.doesExist(0, 0), `is`(true))
    }

    @Test
    fun insertEnrolls_Then_GetEnrollsByStudentId() {

        val studentId0 = listOf(
            enroll,
            enroll.copy(studentId = 0, courseCode = 1),
            enroll.copy(studentId = 0, courseCode = 4)
        )

        val studentId1 = listOf(
            enroll.copy(studentId = 1, courseCode = 0),
            enroll.copy(studentId = 1, courseCode = 2)
        )

        val studentId2 = listOf(
            enroll.copy(studentId = 2, courseCode = 1),
            enroll.copy(studentId = 2, courseCode = 3)
        )

        val studentId3 = listOf(
            enroll.copy(studentId = 3, courseCode = 2),
            enroll.copy(studentId = 3, courseCode = 4)
        )

        val studentId4 = listOf(
            enroll.copy(studentId = 4, courseCode = 3),
            enroll.copy(studentId = 4, courseCode = 1)
        )

        val totalList = (studentId0 + studentId1 + studentId2 + studentId3 + studentId4).distinct()

        for (enroll in totalList) {
            enrollmentDataSource.insert(enroll)
        }

        val student0Result = enrollmentDataSource.get { studentId, courseCode ->
            studentId == 0
        }
        assertThat(student0Result, `is`(Success(studentId0) as Result<List<Enroll>>))

        val student1Result = enrollmentDataSource.get { studentId, courseCode ->
            studentId == 1
        }
        assertThat(student1Result, `is`(Success(studentId1) as Result<List<Enroll>>))

        val student2Result = enrollmentDataSource.get { studentId, courseCode ->
            studentId == 2
        }
        assertThat(student2Result, `is`(Success(studentId2) as Result<List<Enroll>>))

        val student3Result = enrollmentDataSource.get { studentId, courseCode ->
            studentId == 3
        }
        assertThat(student3Result, `is`(Success(studentId3) as Result<List<Enroll>>))

        val student4Result = enrollmentDataSource.get { studentId, courseCode ->
            studentId == 4
        }
        assertThat(student4Result, `is`(Success(studentId4) as Result<List<Enroll>>))

    }

    @Test
    fun insertEnrolls_Then_GetEnrollsByCourseCode() {

        val courseCode0 = listOf(
            enroll,
            enroll.copy(courseCode = 0, studentId = 1),
            enroll.copy(courseCode = 0, studentId = 4)
        )

        val courseCode1 = listOf(
            enroll.copy(courseCode = 1, studentId = 0),
            enroll.copy(courseCode = 1, studentId = 2)
        )

        val courseCode2 = listOf(
            enroll.copy(courseCode = 2, studentId = 1),
            enroll.copy(courseCode = 2, studentId = 3)
        )

        val courseCode3 = listOf(
            enroll.copy(courseCode = 3, studentId = 2),
            enroll.copy(courseCode = 3, studentId = 4)
        )

        val courseCode4 = listOf(
            enroll.copy(courseCode = 4, studentId = 3),
            enroll.copy(courseCode = 4, studentId = 1)
        )

        val totalList = (courseCode0 + courseCode1 + courseCode2 + courseCode3 + courseCode4).distinct()

        for (enroll in totalList) {
            enrollmentDataSource.insert(enroll)
        }

        val course0Result = enrollmentDataSource.get { studentId, courseCode ->
            courseCode == 0
        }
        assertThat(course0Result, `is`(Success(courseCode0) as Result<List<Enroll>>))

        val course1Result = enrollmentDataSource.get { studentId, courseCode ->
            courseCode == 1
        }
        assertThat(course1Result, `is`(Success(courseCode1) as Result<List<Enroll>>))

        val course2Result = enrollmentDataSource.get { studentId, courseCode ->
            courseCode == 2
        }
        assertThat(course2Result, `is`(Success(courseCode2) as Result<List<Enroll>>))

        val course3Result = enrollmentDataSource.get { studentId, courseCode ->
            courseCode == 3
        }
        assertThat(course3Result, `is`(Success(courseCode3) as Result<List<Enroll>>))

        val course4Result = enrollmentDataSource.get { studentId, courseCode ->
            courseCode == 4
        }
        assertThat(course4Result, `is`(Success(courseCode4) as Result<List<Enroll>>))

    }


}