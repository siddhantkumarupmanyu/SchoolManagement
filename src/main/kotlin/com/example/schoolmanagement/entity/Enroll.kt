package com.example.schoolmanagement.entity

const val UNDEFINED_GRADE = -1f

data class Enroll(
        val student: Student,
        val course: Course,
        val date: Date
) {
    var grade: Float = UNDEFINED_GRADE
}