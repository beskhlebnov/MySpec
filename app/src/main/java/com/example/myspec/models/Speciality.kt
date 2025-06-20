package com.example.myspec.models

data class Speciality(
    val code: String,
    val name: String,
)


fun List<Speciality>.toCodeNameStrings(): List<String> = this.map { "${it.code} ${it.name}" }