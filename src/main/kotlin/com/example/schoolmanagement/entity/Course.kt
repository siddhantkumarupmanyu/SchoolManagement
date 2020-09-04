package com.example.schoolmanagement.entity


data class Course(
    val name: String,
    val code: String,
    val minNoOfStudents: Int,
    val maxNoOfStudents: Int,
    val start: Date,
    val end: Date,
    val isCanceled: Boolean = false // by default not cancelled
) {

    // var currentNoOfStudents: Int can be retrieved from enrollments

    // i do not know if this should be immutable
    var professors: List<Professor> = emptyList() // list of professors that will teach the course
}