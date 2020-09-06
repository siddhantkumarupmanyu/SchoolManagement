package com.example.schoolmanagement.logic

import com.example.schoolmanagement.database.StudentRepository
import com.example.schoolmanagement.entity.Enroll
import com.example.schoolmanagement.entity.Student
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
        // todo check if student id and course id in enroll is valid that is its not enrolled before
        // add date before saving
        TODO()
    }

    fun getEnrollments(studentId: Int): Result<List<Enroll>> {

        // call repository and return list of enrollments
        TODO()

    }


    private fun getAge(year: Int): Int {
        val currentYear = Year.now().value
        return currentYear - year
    }

}