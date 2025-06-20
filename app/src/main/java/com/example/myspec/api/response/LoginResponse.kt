package com.example.myspec.api.response

data class LoginResponse (val refreshToken: String, val isUserExists: Boolean, val state: Int)