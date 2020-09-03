package com.example.schoolmanagement.logic

import com.example.schoolmanagement.entity.Person

object PersonValidator {

    fun validatePersonForAddition(person: Person): Errors<Person> {
        val errorList = mutableListOf<Error<Person>>()

        val nameResult = checkName(person.firstName, person.middleName, person.lastName)

        errorList.addAll(nameResult.errors)

        val result = validatePerson(person)

        errorList.addAll(result.errors)

        return Errors(errorList)


    }

    // Let's not validate dob and address for now

    fun validatePerson(person: Person): Errors<Person> {
        val errorList = mutableListOf<Error<Person>>()
        val phoneNoResult = checkPhoneNo(person.phoneNo)

        errorList.addAll(phoneNoResult.errors)

        return Errors(errorList)
    }

    fun checkName(firstName: String, middleName: String, lastName: String): Errors<Person> {
        val errorList = mutableListOf<Error<Person>>()

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

        return Errors(errorList)

    }

    fun checkPhoneNo(phoneNo: String): Errors<Person> {
        val errorList = mutableListOf<Error<Person>>()

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

        return Errors(errorList)

    }

}