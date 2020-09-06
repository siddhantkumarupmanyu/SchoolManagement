package com.example.schoolmanagement.database

import com.example.schoolmanagement.entity.Enroll
import com.example.schoolmanagement.entity.Student
import com.example.schoolmanagement.logic.Result

class StudentRepository(
    private val studentDataSource: StudentDataSource,
    private val enrollmentDataSource: EnrollmentDataSource
) { // takes studentDatasource and EnrollmentDataSource

    fun addStudent(student: Student): Result<Int> {
        return studentDataSource.insert(student)
    }

    fun getStudent(id: Int): Result<Student> {
        return studentDataSource.get(id)
    }

    fun studentDoesExist(id: Int): Boolean {
        return studentDataSource.doesExist(id)
    }

    fun insertEnroll(enroll: Enroll): Result<Unit> {
        return enrollmentDataSource.insert(enroll)
    }

    fun getEnrollments(studentId: Int): Result<List<Enroll>> {
        return enrollmentDataSource.get { stdId, _ -> stdId == studentId }
    }

    fun enrollDoesExist(studentId: Int, courseCode: Int): Boolean {
        return enrollmentDataSource.doesExist(studentId, courseCode)
    }

}