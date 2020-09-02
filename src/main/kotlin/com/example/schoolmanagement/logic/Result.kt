package com.example.schoolmanagement.logic

sealed class Result<T>

data class Success<T>(
        val data: T
) : Result<T>()

data class Error<T>(
        val errorMessage: String
) : Result<T>()

data class Errors<T>(
        val errors: List<Error<T>>
) : Result<T>()