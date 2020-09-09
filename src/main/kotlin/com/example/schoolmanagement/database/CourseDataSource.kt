package com.example.schoolmanagement.database

import com.example.schoolmanagement.entity.Course
import com.example.schoolmanagement.logic.*

class CourseDataSource : DataSource<Course>, Existence {

    private val courseMap = mutableMapOf<Int, Course>()
    private var count = -1

    private val professorsMap = mutableMapOf<Int, List<Int>>()

    override fun insert(data: Course): Result<Int> {
        count += 1
        courseMap[count] = data.copy(code = count)

        professorsMap[count] = emptyList()

        return Success(count)
    }

    override fun get(id: Int): Result<Course> { // id  is course code
        val value = courseMap[id]

        return if (value == null) {
            Error(NO_ENTRY)
        } else {
            Success(value)
        }
    }

    override fun doesExist(id: Int): Boolean {
        return courseMap.containsKey(id)
    }

    fun addProfessor(courseCode: Int, profID: Int): Result<Unit> {
        val currentList = professorsMap[courseCode]!!
        val updatedList = currentList + profID

        professorsMap[courseCode] = updatedList

        return Success(SUCCESS)
    }

    fun getProfessors(courseCode: Int): Result<List<Int>> {
        val courses = professorsMap[courseCode]!!

        return Success(courses)
    }

    fun removeLastProfessor(courseCode: Int): Result<Unit> {
        val currentList = professorsMap[courseCode]!!
        val removedList = currentList.dropLast(1)

        professorsMap[courseCode] = removedList

        return Success(SUCCESS)
    }

}