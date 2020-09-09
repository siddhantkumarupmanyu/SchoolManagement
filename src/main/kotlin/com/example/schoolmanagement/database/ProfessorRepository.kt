package com.example.schoolmanagement.database

import com.example.schoolmanagement.entity.Professor
import com.example.schoolmanagement.logic.Result

class ProfessorRepository(
    private val professorDataSource: ProfessorDataSource
) {

    fun addProfessor(professor: Professor): Result<Int> {
        return professorDataSource.insert(professor)
    }

    fun getProfessor(id: Int): Result<Professor> {
        return professorDataSource.get(id)
    }

    fun professorDoesExist(id: Int): Boolean {
        return professorDataSource.doesExist(id)
    }

    /*fun addCourse(id: Int, courseCode: Int): Result<Unit> {
        return professorDataSource.addCourse(id, courseCode)
    }*/

    fun getCourses(id: Int): Result<List<Int>> {
        return professorDataSource.getCourses(id)
    }

    /*fun removeLastCourse(id: Int): Result<Unit>{
        return professorDataSource.removeLastCourse(id)
    }*/

}