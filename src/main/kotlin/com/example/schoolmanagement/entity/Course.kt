package com.example.schoolmanagement.entity


data class Course(
        val name: String,
        val code: String,
        val currentNoOfStudents: Int,
        val minNoOfStudents: Int,
        val maxNoOfStudents: Int,
        val start: Date,
        val end: Date
) {

    var professors: List<Professor> = emptyList() // list of professors that will teach the course

    var isCanceled: Boolean = false // by default not cancelled
}