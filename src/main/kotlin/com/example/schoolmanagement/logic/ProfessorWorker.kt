package com.example.schoolmanagement.logic

import com.example.schoolmanagement.database.ProfessorRepository
import com.example.schoolmanagement.entity.Course
import com.example.schoolmanagement.entity.Professor
import java.time.Year

class ProfessorWorker(private val professorRepository: ProfessorRepository) {

    fun addProfessor(professor: Professor): Result<Professor> {

        val validationResult = PersonValidator.validatePersonForAddition(professor)
        if (validationResult.errors.isNotEmpty()) {
            return validationResult
        }

        return when (val result = professorRepository.addProfessor(professor)) {

            is Success -> {
                val created = professor.copy(id = result.data)
                created.age = getAge(created.dateOfBirth.year)
                Success(created)
            }

            is Error -> Error(result.errorMessage)
            is Errors -> result as Errors<Professor> // should i care about it

        }

    }

    fun getProfessor(id: Int): Result<Professor> {
        // add calculate age of the student
        return when (val result = professorRepository.getProfessor(id)) {
            is Success -> {
                val professor = result.data
                professor.age = getAge(professor.dateOfBirth.year)
                Success(professor)
            }

            is Error -> Error(result.errorMessage)
            is Errors -> result as Errors<Professor> // should i care about it
        }
    }

    // since professor cannot add itself to the course
    // fun addCourse(id: Int, courseCode: Int): Result<Unit> {}

    fun getCourses(id: Int): Result<List<Course>> {
        TODO()
    }

    private fun getAge(year: Int): Int {
        val currentYear = Year.now().value
        return currentYear - year
    }

}