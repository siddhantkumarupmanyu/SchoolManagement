package com.example.schoolmanagement.database

import com.example.schoolmanagement.logic.Result

interface DataSource<T> {
    fun insert(data: T) : Result<Int>
    fun get(id: Int): Result<T>
}

interface Existence{
    fun doesExist(id: Int) : Boolean
}