package com.example.schoolmanagement.logic

import com.example.schoolmanagement.entity.Address
import com.example.schoolmanagement.entity.Date
import com.example.schoolmanagement.entity.NOT_DEFINED
import com.example.schoolmanagement.entity.Student

object Temp {

    fun getTempStudent(): Result<Student> {
        val address = Address(
                "streetAddress",
                "city",
                "state",
                "123456",
                "country"
        )

        val dateOfBirth = Date(1, 0, 1998)

        return Success(Student(
                NOT_DEFINED,
                "firstName",
                "lastName",
                "middleName",
                "9876543210",
                address,
                dateOfBirth,
                false
        ))
    }

}