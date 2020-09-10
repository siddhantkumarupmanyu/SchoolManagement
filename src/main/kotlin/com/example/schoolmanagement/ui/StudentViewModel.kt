package com.example.schoolmanagement.ui

import com.example.schoolmanagement.entity.Enroll
import com.example.schoolmanagement.entity.Student
import com.example.schoolmanagement.logic.Result
import com.example.schoolmanagement.logic.StudentWorker
import com.example.schoolmanagement.logic.Success

class StudentViewModel(private val studentWorker: StudentWorker) {

    private lateinit var student: Student

    private lateinit var enrollments: List<Enroll>

    fun register(inputStudent: Student): Result<Student> {

        return when (val result = studentWorker.addStudent(inputStudent)) {
            is Success -> {
                setStudent(result.data)
                result
            }
            else -> result
        }

    }


    fun login(studentId: Int): Result<Student> {
        return when (val result = studentWorker.getStudent(studentId)) {
            is Success -> {
                setStudent(result.data)
                result
            }
            else -> result
        }
    }

    private fun setStudent(result: Student) {
        student = result
    }

}