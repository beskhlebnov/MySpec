package com.example.myspec.api.body

import com.example.myspec.models.RefreshToken

data class FavoriteBody(val token: RefreshToken, val programId: Int)
