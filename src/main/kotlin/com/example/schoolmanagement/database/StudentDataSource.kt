package com.example.schoolmanagement.database

import com.example.schoolmanagement.entity.Student
import com.example.schoolmanagement.logic.Error
import com.example.schoolmanagement.logic.NO_ENTRY
import com.example.schoolmanagement.logic.Result
import com.example.schoolmanagement.logic.Success

class StudentDataSource : DataSource<Student>, Existence {

    private val studentMap = mutableMapOf<Int, Student>()
    private var count = -1

    override fun insert(data: Student): Result<Int> {
        count += 1
        studentMap[count] = data.copy(id = count)

        return Success(count)
    }

    override fun get(id: Int): Result<Student> {
        val value = studentMap[id]

        return if (value == null) {
            Error(NO_ENTRY)
        } else {
            Success(value)
        }
    }

    override fun doesExist(id: Int): Boolean {
        return studentMap.containsKey(id)
    }


}