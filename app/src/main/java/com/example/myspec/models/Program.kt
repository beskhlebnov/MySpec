package com.example.myspec.models

data class Program(
    val id: Int,
    var isFavorite: Boolean,
    val code: String,
    val name: String,
    val university: String,
    val universityFull: String,
    val predict_ball: Int,
    val pref_ball: Int,
    val exams: List<Exam>,
    val admission_chance: Int,
) {

    fun toggleFavorite(){ isFavorite = !isFavorite }

    fun getExamsStr(): String {
        var result = ""
        exams.forEach { exam ->
            result = result.plus(" ${exam.subject}")
        }
        return result
    }
}


data class Exam(
    val type: String,
    val subject: String,
    val min: String
)
