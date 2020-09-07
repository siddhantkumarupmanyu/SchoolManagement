package com.example.schoolmanagement.logic

import com.example.schoolmanagement.database.StudentRepository
import com.example.schoolmanagement.entity.Date
import com.example.schoolmanagement.entity.Enroll
import com.example.schoolmanagement.entity.Student
import java.time.LocalDate
import java.time.Year

class StudentWorker(private val studentRepository: StudentRepository) {

    fun addStudent(student: Student): Result<Student> {

        val validationResult = PersonValidator.validatePersonForAddition(student)
        if (validationResult.errors.isNotEmpty()) {
            return validationResult
        }

        return when (val studentResult = studentRepository.addStudent(student)) {

            is Success -> {
                val studentCreated = student.copy(id = studentResult.data)
                studentCreated.age = getAge(studentCreated.dateOfBirth.year)
                Success(studentCreated)
            }

            is Error -> Error(studentResult.errorMessage)
            is Errors -> studentResult as Errors<Student> // should i care about it

        }

    }

    fun getStudent(id: Int): Result<Student> {
        // add calculate age of the student
        return when (val studentResult = studentRepository.getStudent(id)) {
            is Success -> {
                val student = studentResult.data
                student.age = getAge(student.dateOfBirth.year)
                Success(student)
            }

            is Error -> Error(studentResult.errorMessage)
            is Errors -> studentResult as Errors<Student> // should i care about it
        }
    }

    fun enroll(enroll: Enroll): Result<Enroll> {
        if (studentRepository.enrollDoesExist(enroll.studentId, enroll.courseCode)) {
            return Error("Already Enrolled")
        }
        val localDate = LocalDate.now()
        val currentDate = Date(localDate.dayOfMonth, localDate.monthValue - 1, localDate.year)
        val enrollWithDate = enroll.copy(date = currentDate)

        return when (val result = studentRepository.insertEnroll(enrollWithDate)) {
            is Success -> Success(enrollWithDate)
            is Error -> Error(result.errorMessage)
            is Errors -> result as Errors<Enroll> // should i care about it
        }

    }

    fun getEnrollments(studentId: Int): Result<List<Enroll>> {
        return studentRepository.getEnrollments(studentId)
    }


    private fun getAge(year: Int): Int {
        val currentYear = Year.now().value
        return currentYear - year
    }

}