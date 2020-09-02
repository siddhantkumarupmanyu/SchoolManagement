package com.example.schoolmanagement.entity

data class Professor(
        override var id: Int = NOT_DEFINED,
        override val firstName: String,
        override val lastName: String,
        override val middleName: String,
        override val phoneNo: String,
        override val address: Address,
        override val dateOfBirth: Date,

        val salary: Float
) : Person(){

    var courses: List<Course> = emptyList()

}