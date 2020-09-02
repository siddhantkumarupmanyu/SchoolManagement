package com.example.schoolmanagement.logic

import com.example.schoolmanagement.entity.Student
import java.time.Year

class StudentWorker {

    fun addStudent(student: Student): Result<String> {
        PersonValidator.validatePersonForAddition(student)

        return Success(SUCCESS)
    }

    fun getStudent(id: Int): Result<Student> {
        val result = Temp.getTempStudent() // assume this student is coming from repository

        when (result) {
            is Success -> {
                val student = result.data
                student.age = getAge(student.dateOfBirth.year)
                return Success(student)
            }

            is Error -> return result

            is Errors -> return result

        }

    }

    private fun getAge(year: Int): Int {
        val currentYear = Year.now().value
        return currentYear - year
    }

}