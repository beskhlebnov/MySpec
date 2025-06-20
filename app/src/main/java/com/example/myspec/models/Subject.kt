package com.example.myspec.models

data class Subject (val subject: String, var ball: Int? = null){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Subject) return false
        return subject == other.subject
    }

    override fun hashCode(): Int = subject.hashCode()
}