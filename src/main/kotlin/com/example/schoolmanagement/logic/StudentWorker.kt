package com.example.schoolmanagement.logic

import com.example.schoolmanagement.entity.Enroll
import com.example.schoolmanagement.entity.Student
import java.time.Year

class StudentWorker {

    fun addStudent(student: Student): Result<Student> {

        val validationResult = PersonValidator.validatePersonForAddition(student)
        if (validationResult.errors.isNotEmpty()) {
            return validationResult
        }

        // call save to repository method which returns the id if insertion is successful
        // TODO complete this method
        val errorList = mutableListOf<Error<Student>>()

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
                val student = result.data
                student.age = getAge(student.dateOfBirth.year)
                return Success(student)
            }

            is Error -> return result

            is Errors -> return result

        }

    }

    fun enroll(enroll: Enroll): Result<Enroll> {
        // todo check if student id and course id in enroll is valid
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