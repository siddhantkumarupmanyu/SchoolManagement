package com.example.schoolmanagement.logic

import com.example.schoolmanagement.entity.Student
import java.time.Year

class StudentWorker {

    fun addStudent(student: Student): Result<String> {
        val errorList = mutableListOf<Error<String>>()

        val validationResult = PersonValidator.validatePersonForAddition(student)
        if (validationResult is Errors) {
            errorList.addAll(validationResult.errors)
        }

        // call save to repository method

        return if (errorList.isEmpty()) {
            Success(SUCCESS)
        } else {
            Errors(errorList)
        }
    }

    fun getStudent(id: Int): Result<Student> {
        val result = Temp.getTempStudent() // assume this student is coming from repository

        when (result) {
            is Success -> {
                val dob = result.data.dateOfBirth
                val student = result.data.copy(age = getAge(dob.year))
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