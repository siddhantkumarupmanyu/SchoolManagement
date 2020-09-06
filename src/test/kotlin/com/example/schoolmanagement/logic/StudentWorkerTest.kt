package com.example.schoolmanagement.logic

import com.example.schoolmanagement.database.StudentRepository
import com.example.schoolmanagement.entity.Address
import com.example.schoolmanagement.entity.Date
import com.example.schoolmanagement.entity.NOT_DEFINED
import com.example.schoolmanagement.entity.Student
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.time.Year

class StudentWorkerTest {

    private lateinit var studentWorker: StudentWorker
    private lateinit var student: Student

    private lateinit var repository: StudentRepository

    @Before
    fun setUp() {
        repository = mock(StudentRepository::class.java)
        studentWorker = StudentWorker(repository)

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
            "firstName",
            "lastName",
            "middleName",
            "9876543210",
            address,
            dateOfBirth,
            false
        )
    }

    @Test
    fun addStudent_GetStudent() {
        val studentId0 = student.copy(id = 0)
        studentId0.age = Year.now().value - student.dateOfBirth.year

        `when`(repository.addStudent(student)).thenReturn(Success(0))
        `when`(repository.getStudent(0)).thenReturn(Success(studentId0))

        val addStudentRes = studentWorker.addStudent(student)
        assertTrue(addStudentRes is Success)
        addStudentRes as Success
        assertThat(addStudentRes, `is`(Success(studentId0)))
        // cause data class equal takes only constructor parameters
        assertThat(addStudentRes.data.age, `is`(studentId0.age))

        val getStudentRes = studentWorker.getStudent(0)
        assertTrue(getStudentRes is Success)
        getStudentRes as Success
        assertThat(getStudentRes, `is`(Success(studentId0)))
        assertThat(getStudentRes.data.age, `is`(studentId0.age))
    }

    @Test
    fun addStudentError_ByRepository_GetStudentError() {
        `when`(repository.addStudent(student)).thenReturn(Error(UNABLE_TO_ADD))
        `when`(repository.getStudent(0)).thenReturn(Error(NO_ENTRY))

        val addStudentRes = studentWorker.addStudent(student)
        assertTrue(addStudentRes is Error)
        addStudentRes as Error
        assertThat(addStudentRes, `is`(Error(UNABLE_TO_ADD)))

        val getStudentRes = studentWorker.getStudent(0)
        assertTrue(getStudentRes is Error)
        getStudentRes as Error
        assertThat(getStudentRes, `is`(Error(NO_ENTRY)))
    }

    @Test
    fun addStudentErrors_ByRepository_GetStudentErrors() {
        `when`(repository.addStudent(student)).thenReturn(
            Errors<Int>(listOf(Error(UNABLE_TO_ADD), Error("SecondError")))
        )
        `when`(repository.getStudent(0)).thenReturn(
            Errors<Student>(listOf(Error(NO_ENTRY), Error("SecondError")))
        )

        val addStudentRes = studentWorker.addStudent(student)
        assertTrue(addStudentRes is Errors)
        addStudentRes as Errors
        assertThat(addStudentRes, `is`(Errors<Student>(listOf(Error(UNABLE_TO_ADD), Error("SecondError")))))

        val getStudentRes = studentWorker.getStudent(0)
        assertTrue(getStudentRes is Errors)
        getStudentRes as Errors
        assertThat(getStudentRes, `is`(Errors<Student>(listOf(Error(NO_ENTRY), Error("SecondError")))))
    }

    @Test
    fun addStudentErrors_ByPersonValidator_GetStudentsError() {
        val wrongStudent = student.copy(firstName = "yoyo121", phoneNo = "21545sdsd4")

        `when`(repository.addStudent(student)).thenReturn(Success(0))
        `when`(repository.getStudent(0)).thenReturn(Error(NO_ENTRY))

        val listOfErrors = listOf(
            Error<Student>(ONLY_ALPHA),
            Error<Student>(ONLY_DIGIT)
        )

        val addStudentRes = studentWorker.addStudent(wrongStudent)
        assertTrue(addStudentRes is Errors)
        addStudentRes as Errors
        assertThat(addStudentRes, `is`(Errors(listOfErrors)))

        val getStudentRes = studentWorker.getStudent(0)
        assertTrue(getStudentRes is Error)
        getStudentRes as Error
        assertThat(getStudentRes, `is`(Error(NO_ENTRY)))
    }

}