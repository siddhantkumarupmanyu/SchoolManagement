package com.example.schoolmanagement.entity

const val NOT_DEFINED = -1

abstract class Person {
    abstract var id: Int
    abstract val firstName: String
    abstract val lastName: String
    abstract val middleName: String
    abstract val phoneNo: String
    abstract val address: Address
    abstract val dateOfBirth: Date

    var age: Int = NOT_DEFINED
}