package com.example.schoolmanagement.database

import com.example.schoolmanagement.entity.Address
import com.example.schoolmanagement.entity.Date
import com.example.schoolmanagement.entity.NOT_DEFINED
import com.example.schoolmanagement.entity.Student
import com.example.schoolmanagement.logic.Error
import com.example.schoolmanagement.logic.NO_ENTRY
import com.example.schoolmanagement.logic.Result
import com.example.schoolmanagement.logic.Success
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

class StudentDataSourceTest {

    private lateinit var studentDataSource: StudentDataSource
    private lateinit var student: Student

    @Before
    fun setUp() {
        studentDataSource = StudentDataSource()

        val address = Address(
                "streetAddress",
                "city",
                "state",
                "123456",
                "country"
        )

        val dateOfBirth = Date(1, 0, 1998)

        student = Student(
                NOT_DEFINED,
                "firstName0",
                "lastName",
                "middleName",
                "9876543210",
                address,
                dateOfBirth,
                false
        )
    }

    @Test
    fun insertStudent_Then_GetStudent() {

        val idResult = studentDataSource.insert(student)
        assertThat(idResult, `is`(Success(0) as Result<Int>))

        val getStudent = studentDataSource.get(0)
        assertThat(getStudent, `is`(not(Success(student) as Result<Student>)))

        val studentWithId = student.copy(id = 0)

        assertThat(getStudent, `is`(Success(studentWithId) as Result<Student>))

    }

    @Test
    fun insertStudents_Then_GetStudents() {
        assertThat(studentDataSource.insert(student), `is`(Success(0) as Result<Int>))
        assertThat(studentDataSource.insert(student), `is`(Success(1) as Result<Int>))
        assertThat(studentDataSource.insert(student), `is`(Success(2) as Result<Int>))
        assertThat(studentDataSource.insert(student), `is`(Success(3) as Result<Int>))

        assertThat(studentDataSource.get(0), `is`(Success(student.copy(id = 0)) as Result<Student>))
        assertThat(studentDataSource.get(1), `is`(Success(student.copy(id = 1)) as Result<Student>))
        assertThat(studentDataSource.get(2), `is`(Success(student.copy(id = 2)) as Result<Student>))
        assertThat(studentDataSource.get(3), `is`(Success(student.copy(id = 3)) as Result<Student>))

    }

    @Test
    fun getStudentError() {
        assertThat(studentDataSource.get(0), `is`(Error(NO_ENTRY)))

        assertThat(studentDataSource.insert(student), `is`(Success(0) as Result<Int>))
        assertThat(studentDataSource.get(10), `is`(Error(NO_ENTRY)))
    }

    @Test
    fun checkDoesExist() {
        assertThat(studentDataSource.doesExist(0), `is`(false))

        studentDataSource.insert(student)
        assertThat(studentDataSource.doesExist(0), `is`(true))

        assertThat(studentDataSource.doesExist(10), `is`(false))
        studentDataSource.insert(student)
        assertThat(studentDataSource.doesExist(10), `is`(false))
        assertThat(studentDataSource.doesExist(1), `is`(true))

    }


}