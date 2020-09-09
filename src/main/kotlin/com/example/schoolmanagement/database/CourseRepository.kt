package com.example.schoolmanagement.database

import com.example.schoolmanagement.entity.Course
import com.example.schoolmanagement.logic.Result
import com.example.schoolmanagement.logic.SUCCESS
import com.example.schoolmanagement.logic.Success

class CourseRepository(
    private val courseDataSource: CourseDataSource,
    private val professorDataSource: ProfessorDataSource
) {

    fun addCourse(course: Course): Result<Int> {
        return courseDataSource.insert(course)
    }

    fun getCourse(courseCode: Int): Result<Course> {
        return courseDataSource.get(courseCode)
    }

    fun courseDoesExist(id: Int): Boolean {
        return courseDataSource.doesExist(id)
    }

    fun addProfessor(courseCode: Int, profId: Int): Result<Unit> {
        val courseRes = courseDataSource.addProfessor(courseCode, profId)

        if (courseRes !is Success) {
            return courseRes
        }

        val profRes = professorDataSource.addCourse(profId, courseCode)

        if (profRes !is Success) {
            removeLastProfessor(courseCode)
            return profRes
        }

        return Success(SUCCESS)
    }

    fun getProfessors(courseCode: Int): Result<List<Int>> {
        return courseDataSource.getProfessors(courseCode)
    }

    fun removeLastProfessor(courseCode: Int): Result<Unit> {
        return courseDataSource.removeLastProfessor(courseCode)
    }

}