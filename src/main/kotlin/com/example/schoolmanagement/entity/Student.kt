package com.example.schoolmanagement.entity

data class Student(
    override val id: Int = NOT_DEFINED,
    override val firstName: String,
    override val lastName: String,
    override val middleName: String,
    override val phoneNo: String,
    override val address: Address,
    override val dateOfBirth: Date,

    val international: Boolean


) : Person() {

    override var age: Int = NOT_DEFINED

    // i think this list should not be a part of student
    // var enrollments: List<Enroll> = emptyList() // I don't know if this should be immutable

    // since its dependent on enrollments its can be removed from here
    /*val isPartTime: Boolean
        get() = enrollments.size >= 2 // if he is entrolled in two or more courses*/


    /*val isOnProbation: Boolean
        get() = */

}