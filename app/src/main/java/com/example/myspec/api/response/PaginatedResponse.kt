package com.example.myspec.api.response

import com.example.myspec.models.Program

data class PaginatedResponse<T>(
    val data: List<T>,
    val total: Int,
    val chunkSize: Int,
    val chunk: Int,
    val hasMore: Boolean
)

data class SpecialtyResponse(
    val response: PaginatedResponse<Program>
)