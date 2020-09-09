package com.example.schoolmanagement.database

import com.example.schoolmanagement.entity.Professor
import com.example.schoolmanagement.logic.*

class ProfessorDataSource : DataSource<Professor>, Existence {

    private val professorMap = mutableMapOf<Int, Professor>()
    private var count = -1

    private val coursesMap = mutableMapOf<Int, List<Int>>()

    override fun insert(data: Professor): Result<Int> {
        count += 1
        professorMap[count] = data.copy(id = count)

        coursesMap[count] = emptyList()

        return Success(count)
    }

    override fun get(id: Int): Result<Professor> {
        val value = professorMap[id]

        return if (value == null) {
            Error(NO_ENTRY)
        } else {
            Success(value)
        }
    }

    override fun doesExist(id: Int): Boolean {
        return professorMap.containsKey(id)
    }

    fun addCourse(id: Int, courseCode: Int): Result<Unit> {
        val currentList = coursesMap[id]!!
        val updatedList = currentList + courseCode

        coursesMap[id] = updatedList

        return Success(SUCCESS)
    }

    fun getCourses(id: Int): Result<List<Int>> {
        val courses = coursesMap[id]!!

        return Success(courses)
    }

}