package com.example.myspec.api.body

import com.example.myspec.models.RefreshToken

data class ProgramBody (val token: RefreshToken, val isFavorites: Boolean)