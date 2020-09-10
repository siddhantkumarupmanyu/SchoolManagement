package com.example.schoolmanagement.ui

import com.example.schoolmanagement.entity.Address
import com.example.schoolmanagement.entity.Date
import com.example.schoolmanagement.entity.Student
import com.example.schoolmanagement.logic.Error
import com.example.schoolmanagement.logic.Errors
import com.example.schoolmanagement.logic.Success

class StudentScreen(private val studentViewModel: StudentViewModel) {

    // todo add readline() null cases


    fun main() {

        println("1. Login")
        println("2. Register")
        val input = readLine()

        val res = when (input) {
            "1" -> login()
            "2" -> register()
            else -> {
                println(NOT_VALID)
                false
            }
        }

        if (!res) {
            TODO("loop above screen")
        } else {
            showStudent()
        }

    }

    private fun showStudent() {
        TODO("Not yet implemented")
    }

    private fun login(): Boolean {
        print("Student UserID:- ")
        val userId = readLine()?.toIntOrNull()
        if (userId == null) {
            TODO() // enter valid userID case
        }

        val result = studentViewModel.login(userId)

        if (result is Error) {
            println(result.errorMessage)
        } else if (result is Errors) {
            result.errors.forEach {
                println(it.errorMessage)
            }
        }

        return result is Success
    }

    private fun register(): Boolean {

        val student = getStudentFromUser()

        val result = studentViewModel.register(student)

        if (result is Error) {
            println(result.errorMessage)
        } else if (result is Errors) {
            result.errors.forEach {
                println(it.errorMessage)
            }
        }

        return result is Success

    }

    private fun getStudentFromUser(): Student {
        print("FirstName:- ")
        val firstName = readLine()
        print("LastName:- ")
        val lastName = readLine()
        print("MiddleName:- ")
        val middleName = readLine()
        print("phoneName:- ")
        val phoneNo = readLine()


        val address = getAddress()

        val dateOfBirth = getDateOfBirth()

        // very ugly code
        print("International(Y/N):- ")
        val rawInternational = readLine().takeIf {
            val s = it!!.toLowerCase()
            s == "y" || s == "n"
        }

        if (rawInternational.isNullOrBlank()) {
            TODO()
        }

        val international = rawInternational.toLowerCase() == "y"
        // need to do something with this ugly code

        return Student(
            firstName = firstName!!,
            lastName = lastName!!,
            middleName = middleName!!,
            phoneNo = phoneNo!!,
            address = address,
            dateOfBirth = dateOfBirth,
            international = international
        )
    }

    private fun getDateOfBirth(): Date {
        print("Date(dd/mm/yyyy):- ")
        val rawDate = readLine()

        val (rawd, rawm, rawy) = rawDate!!.split('/')

        val d = rawd.toIntOrNull()
        val m = rawm.toIntOrNull()
        val y = rawy.toIntOrNull()

        if (d == null || m == null || y == null) {
            TODO("Implement")
            // maybe i could re ask for input
        }

        if (d !in (1..31) || m !in (0..11)) {
            TODO()
        }

        return Date(d, m, y)
    }

    private fun getAddress(): Address {
        print("Street Address:- ")
        val streetAddress = readLine()
        print("City:- ")
        val city = readLine()
        print("State:- ")
        val state = readLine()
        print("Postal Code:- ")
        val postalCode = readLine()
        print("Country:- ")
        val country = readLine()

        return Address(
            streetAddress!!,
            city!!,
            state!!,
            postalCode!!,
            country!!
        )
    }

}