package com.example.schoolmanagement.logic

import com.example.schoolmanagement.entity.Person

object PersonValidator {

    fun validatePersonForAddition(person: Person) {
        TODO("Not yet implemented")
    }

    fun validatePerson(person: Person): Result<String> {
        TODO("Not yet implemented")
    }


    fun checkName(firstName: String, middleName: String, lastName: String): Result<String> {
        val errorList = mutableListOf<Error<String>>()

        val trimmedNames = Utils.trimStrings(firstName, middleName, lastName)

        if (trimmedNames.all { it.isEmpty() }) {
            errorList.add(Error(ALL_EMPTY))
            return Errors(errorList)
        }

        val (tFName, tMidName, tLastName) = trimmedNames

        if (tFName.isEmpty()) {
            errorList.add(Error(FIRSTNAME_EMPTY))
        }

        if (!Utils.onlyAlpha(tFName, tMidName, tLastName)) {
            errorList.add(Error(ONLY_ALPHA))
        }

        return if (errorList.isEmpty()) {
            Success(SUCCESS)
        } else {
            Errors(errorList)
        }
    }

    fun checkPhoneNo(phoneNo: String): Result<String> {
        val errorList = mutableListOf<Error<String>>()

        val (tPhoneNo) = Utils.trimStrings(phoneNo)
        
        if (tPhoneNo.isEmpty()) {
            errorList.add(Error(ALL_EMPTY))
            return Errors(errorList)
        }
        if (!Utils.onlyDigits(tPhoneNo)) {
            errorList.add(Error(ONLY_DIGIT))
        }

        if (tPhoneNo.length != PHONE_NO_LENGTH) {
            errorList.add(Error(PHONE_NO_LENGTH_MISMATCHED))
        }

        return if (errorList.isEmpty()) {
            Success(SUCCESS)
        } else {
            Errors(errorList)
        }
    }

}