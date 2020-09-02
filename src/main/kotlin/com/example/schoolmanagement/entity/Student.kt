package com.example.schoolmanagement.entity

data class Student(
        override val id: Int = NOT_DEFINED,
        override val firstName: String,
        override val lastName: String,
        override val middleName: String,
        override val phoneNo: String,
        override val address: Address,
        override val dateOfBirth: Date,
        override val age: Int = NOT_DEFINED,

        val international: Boolean


) : Person() {

    var enrollments: List<Enroll> = emptyList() // I don't know if this should be immutable

    val isPartTime: Boolean
        get() = enrollments.size >= 2 // if he is entrolled in two or more courses


    /*val isOnProbation: Boolean
        get() = */

}