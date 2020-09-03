package com.example.schoolmanagement.logic

import com.example.schoolmanagement.entity.Person
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.Test

class PersonValidatorTest {

    @Test
    fun checkPhoneNo() {
        var errors = Errors<Person>(listOf(Error(ALL_EMPTY)))
        assertThat(PersonValidator.checkPhoneNo("      "), `is`(errors))

        errors = Errors<Person>(listOf(Error(ONLY_DIGIT)))
        assertThat(PersonValidator.checkPhoneNo("012dsf1212"), `is`(errors))

        errors = Errors<Person>(listOf(Error(PHONE_NO_LENGTH_MISMATCHED)))
        assertThat(PersonValidator.checkPhoneNo("0123154"), `is`(errors))

        errors = Errors<Person>(listOf(Error(ONLY_DIGIT), Error(PHONE_NO_LENGTH_MISMATCHED)))
        assertThat(PersonValidator.checkPhoneNo("012t4r678"), `is`(errors))

        val success = Errors<Person>(emptyList())
        assertThat(PersonValidator.checkPhoneNo("0213456906"), `is`(success))
    }

    @Test
    fun checkName() {
        var errors = Errors<Person>(listOf(Error(FIRSTNAME_EMPTY)))
        assertThat(PersonValidator.checkName("", "middleName", "lstName"), `is`(errors))

        errors = Errors<Person>(listOf(Error(ONLY_ALPHA)))
        assertThat(PersonValidator.checkName("asdasd", "kjasdkas2", "sad1`"), `is`(errors))

        errors = Errors<Person>(listOf(Error(ONLY_ALPHA)))
        assertThat(PersonValidator.checkName("asdasd", "middle", "last`"), `is`(errors))

        errors = Errors<Person>(listOf(Error(ALL_EMPTY)))
        assertThat(PersonValidator.checkName("   ", "  ", "   "), `is`(errors))

        errors = Errors<Person>(listOf(Error(FIRSTNAME_EMPTY), Error(ONLY_ALPHA)))
        assertThat(PersonValidator.checkName("  ", "middle", "last`"), `is`(errors))

        val success = Errors<Person>(emptyList())
        assertThat(PersonValidator.checkName("asdasd", "kjasdkas", "sadgdg"), `is`(success))
    }
}