package com.example.schoolmanagement.logic

import com.example.schoolmanagement.database.CourseRepository
import com.example.schoolmanagement.entity.Course
import com.example.schoolmanagement.entity.Professor

class CourseWorker(
    private val courseRepository: CourseRepository,
    private val professorWorker: ProfessorWorker
) {

    fun addCourse(course: Course): Result<Course> {

        return when (val result = courseRepository.addCourse(course)) {

            is Success -> {
                val created = course.copy(code = result.data)
                Success(created)
            }

            is Error -> Error(result.errorMessage)
            is Errors -> result as Errors<Course> // should i care about it

        }

    }

    fun getCourse(courseCode: Int): Result<Course> {
        return when (val result = courseRepository.getCourse(courseCode)) {
            is Success -> {
                val course = result.data
                Success(course)
            }

            is Error -> Error(result.errorMessage)
            is Errors -> result
        }
    }

    fun addProfessor(courseCode: Int, professorId: Int): Result<Unit> {
        return courseRepository.addProfessor(courseCode, professorId)
    }

    // this function can be break apart in two
    fun getProfessors(courseCode: Int): Pair<Success<List<Professor>>, Errors<List<Professor>>> { // ugliest code

        val list = courseRepository.getProfessors(courseCode)

        if (list !is Success) {
            if (list is Error) {
                return Pair(Success(emptyList()), Errors<List<Professor>>(listOf(Error(list.errorMessage))))
            } else if (list is Errors) {
                return Pair(Success(emptyList()), list as Errors<List<Professor>>)
            }
        }

        val profList = mutableListOf<Professor>()
        val errors = mutableListOf<Error<List<Professor>>>()

        list as Success

        for (profId in list.data) {

            when (val prof = professorWorker.getProfessor(profId)) {
                is Success -> profList.add(prof.data)
                is Error -> errors.add(Error("$profId => ${prof.errorMessage}"))

                // for time being we add only first
                is Errors -> errors.addAll(prof.errors.map { error ->
                    Error<List<Professor>>("$profId => ${error.errorMessage}")
                })

            }
        }

        val profs = profList as List<Professor>

        return Pair(Success(profs), Errors(errors))
    }

}