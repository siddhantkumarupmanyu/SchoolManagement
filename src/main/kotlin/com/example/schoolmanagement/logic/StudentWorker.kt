package com.example.schoolmanagement.logic

import com.example.schoolmanagement.entity.Student
import java.time.Year

class StudentWorker {

    fun addStudent(student: Student): Result<Student> {
        val errorList = mutableListOf<Error<Student>>()

        val validationResult = PersonValidator.validatePersonForAddition(student)
        if (validationResult.errors.isNotEmpty()) {
            return validationResult
        }

        // call save to repository method which returns the id if insertion is successful

        return if (errorList.isEmpty()) {
            Success(SUCCESS)
        } else {
            Errors(errorList)
        }
    }

    fun getStudent(id: Int): Result<Student> {
        // val student = Temp.getTempStudent() // assume this student is coming from repository
        // val result = Success(student)
        val result = Temp.getResult()

        // just testing in out
        // val res = addStudent(Temp.getTempStudent())
        // val dd = (res as Errors).errors

        when (result) {
            is Success -> {
                val dob = result.data.dateOfBirth
                val studentCp = result.data.copy(age = getAge(dob.year))
                return Success(studentCp)
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