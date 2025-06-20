package com.example.myspec.api.response

import com.example.myspec.models.Bonus
import com.example.myspec.models.Subject

data class ProfileGetResponse(val email: String, val state: Int, val bonus: Bonus, val subjects: Set<Subject>, val specialities: Set<String>, val refreshToken: String)
