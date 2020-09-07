package com.example.schoolmanagement.entity

const val UNDEFINED_GRADE = -1f
val UNDEFINED_DATE = Date(-1, -1, -1)

data class Enroll(
    val studentId: Int,
    val courseCode: Int,
    val date: Date = UNDEFINED_DATE,
    val grade: Float = UNDEFINED_GRADE
)