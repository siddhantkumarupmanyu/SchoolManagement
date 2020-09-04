package com.example.schoolmanagement.entity

const val UNDEFINED_GRADE = -1f

data class Enroll(
        val studentId: Int,
        val courseCode: Int,
        val date: Date,
        val grade: Float = UNDEFINED_GRADE
)