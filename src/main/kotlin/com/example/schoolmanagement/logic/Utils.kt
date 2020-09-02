package com.example.schoolmanagement.logic

object Utils {

    fun trimStrings(vararg strings: String): Array<String> {
        val trimmedList = mutableListOf<String>()

        for (string in strings) {
            trimmedList.add(string.trim())
        }
        return trimmedList.toTypedArray()
    }

    fun onlyAlpha(vararg strings: String): Boolean {
        if (strings.isEmpty()) { // this condition should never be true
            return false
        }
        for (string in strings) {
            if (string.isNotEmpty() && !string.matches("[a-zA-Z]+".toRegex())) {
                return false
            }
        }
        return true
    }

    fun onlyDigits(vararg strings: String): Boolean {
        if (strings.isEmpty()) { // this condition should never be true
            return false
        }
        for (string in strings) {
            if (string.isNotEmpty() && !string.matches("[0-9]+".toRegex())) {
                return false
            }
        }
        return true
    }

    fun emptyString(vararg strings: String): Boolean {
        if (strings.isEmpty()) {
            return true
        }
        for (string in strings) {
            if (string.isEmpty()) {
                return true
            }
        }
        return false
    }

}