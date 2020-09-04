package com.example.schoolmanagement.database

import com.example.schoolmanagement.entity.Enroll
import com.example.schoolmanagement.logic.Result
import com.example.schoolmanagement.logic.Success

const val ENROLL_SUCCESS = 0

class EnrollmentDataSource { // IDK but it does not follow DataSource Structure as there is not Id

    private val enrollmentMap = mutableMapOf<EnrollKey, Enroll>()

    fun insert(data: Enroll): Result<Unit> {
        val enrollKey = EnrollKey(data.studentId, data.courseCode)

        enrollmentMap[enrollKey] = data

        return Success(ENROLL_SUCCESS)
    }

    fun get(
        breakAfterFirstMatch: Boolean = false,
        matching: (studentId: Int, courseCode: Int) -> Boolean
    ): Result<List<Enroll>> { // since this is never used we will implement it later
        val data = mutableListOf<Enroll>()

        /*for ((key, value) in enrollmentMap) {
            if (matching(key.studentId, key.courseCode)) {
                data.add(value)
                if (breakAfterFirstMatch){ // i can do this but its checking this condition on each iteration
                    break
                }
            }
        }*/

        // this way we only check condition once
        if (breakAfterFirstMatch) {
            for ((key, value) in enrollmentMap) {
                if (matching(key.studentId, key.courseCode)) {
                    data.add(value) // anyway this condition is rare
                    break
                }
            }
        } else {
            for ((key, value) in enrollmentMap) {
                if (matching(key.studentId, key.courseCode)) {
                    data.add(value)
                }
            }
        }

        return Success(data)
    }

    // can extracted to interface like exsistence but since its used only here no need
    fun doesExist(studentId: Int, courseId: Int): Boolean {
        val enrollKey = EnrollKey(studentId, courseId)
        return enrollmentMap.containsKey(enrollKey)
    }

    private data class EnrollKey(val studentId: Int, val courseCode: Int)

}