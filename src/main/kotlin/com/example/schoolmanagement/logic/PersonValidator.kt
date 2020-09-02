package com.example.schoolmanagement.logic

import com.example.schoolmanagement.entity.Person

object PersonValidator {

    fun validatePersonForAddition(person: Person): Result<String> {
        val errorList = mutableListOf<Error<String>>()

        val nameResult = checkName(person.firstName, person.middleName, person.lastName)

        if (nameResult is Errors) {
            errorList.addAll(nameResult.errors)
        }

        val result = validatePerson(person)

        if (result is Errors) {
            errorList.addAll(result.errors)
        }

        return if (errorList.isEmpty()) {
            Success(SUCCESS)
        } else {
            Errors(errorList)
        }


    }

    // Let's not validate dob and address for now

    fun validatePerson(person: Person): Result<String> {
        val errorList = mutableListOf<Error<String>>()
        val phoneNoResult = checkPhoneNo(person.phoneNo)

        if (phoneNoResult is Errors) {
            errorList.addAll(phoneNoResult.errors)
        }

        return if (errorList.isEmpty()) {
            Success(SUCCESS)
        } else {
            Errors(errorList)
        }
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