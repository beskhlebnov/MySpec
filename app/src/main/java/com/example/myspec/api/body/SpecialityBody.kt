package com.example.myspec.api.body

import com.example.myspec.models.RefreshToken

data class SpecialityBody(val token: RefreshToken, val specialties: List<String>)