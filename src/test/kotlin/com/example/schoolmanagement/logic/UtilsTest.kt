package com.example.schoolmanagement.logic

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UtilsTest {

    @Test
    fun onlyDigits(){
        // singleElement
        assertTrue(Utils.onlyDigits("0123456798"))
        assertTrue(Utils.onlyDigits("265"))
        assertFalse(Utils.onlyDigits("str6ing"))
        assertFalse(Utils.onlyDigits("&*&*6"))
        assertFalse(Utils.onlyDigits("str(ing"))
        assertFalse(Utils.onlyDigits("str`ing"))

        // Array
        var mutiElem = arrayOf("12345", "564", "56554")
        assertTrue(Utils.onlyDigits(*mutiElem))

        mutiElem = arrayOf("", "", "")
        assertTrue(Utils.onlyDigits(*mutiElem))

        mutiElem = arrayOf("564", "", "564545")
        assertTrue(Utils.onlyDigits(*mutiElem))

        mutiElem = arrayOf("4544", "", "abcede34")
        assertFalse(Utils.onlyDigits(*mutiElem))

        mutiElem = emptyArray()
        assertFalse(Utils.onlyDigits(*mutiElem))
    }

    @Test
    fun onlyAlpha() {
        // singleElement
        assertTrue(Utils.onlyAlpha("string"))
        assertFalse(Utils.onlyAlpha("str6ing"))
        assertFalse(Utils.onlyAlpha("str*ing"))
        assertFalse(Utils.onlyAlpha("str(ing"))
        assertFalse(Utils.onlyAlpha("str`ing"))

        // Array
        var mutiElem = arrayOf("string", "stringui", "stringop")
        assertTrue(Utils.onlyAlpha(*mutiElem))

        mutiElem = arrayOf("", "", "")
        assertTrue(Utils.onlyAlpha(*mutiElem))

        mutiElem = arrayOf("string", "stringu()", "stringop56")
        assertFalse(Utils.onlyAlpha(*mutiElem))

        mutiElem = emptyArray()
        assertFalse(Utils.onlyAlpha(*mutiElem))
    }

    @Test
    fun emptyString() {
        var array = arrayOf("string", "string2", "string3")
        assertFalse(Utils.emptyString(*array))

        array = arrayOf("", "", "")
        assertTrue(Utils.emptyString(*array))

        array = arrayOf("string", "", "string3")
        assertTrue(Utils.emptyString(*array))

        array = arrayOf("", "string2", "")
        assertTrue(Utils.emptyString(*array))

        array = emptyArray()
        assertTrue(Utils.emptyString(*array))

    }

}