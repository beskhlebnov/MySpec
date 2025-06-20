package com.example.myspec.api.body

import com.example.myspec.models.RefreshToken
import com.example.myspec.models.Subject

data class SubjectBody(val token: RefreshToken, val subjects: Set<Subject>)