package com.example.schoolmanagement.entity

data class Student(
        override var id: Int = NOT_DEFINED,
        override val firstName: String,
        override val lastName: String,
        override val middleName: String,
        override val phoneNo: String,
        override val address: Address,
        override val dateOfBirth: Date,

        val international: Boolean


) : Person() {

    var courses: List<Course> = emptyList()

    val isPartTime: Boolean
        get() = courses.size >= 2 // if he is entrolled in two or more courses


    /*val isOnProbation: Boolean
        get() = */

}