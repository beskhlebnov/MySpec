package com.example.myspec.api

import com.example.myspec.api.body.FavoriteBody
import com.example.myspec.api.body.ProgramBody
import com.example.myspec.api.body.SaveBonusBody
import com.example.myspec.api.body.SubjectBody
import com.example.myspec.api.response.SubjectResponse
import com.example.myspec.api.body.VerifyBody
import com.example.myspec.api.response.LoginResponse
import com.example.myspec.api.response.RegistrationResponse
import com.example.myspec.api.body.SpecialityBody
import com.example.myspec.api.response.PaginatedResponse
import com.example.myspec.api.response.ProfileGetResponse
import com.example.myspec.api.response.RefreshResponse
import com.example.myspec.api.response.SpecialityGetResponse
import com.example.myspec.api.response.SpecialitySetResponse
import com.example.myspec.api.response.VerifiedResponse
import com.example.myspec.models.Program
import com.example.myspec.models.RefreshToken
import com.example.myspec.models.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("/login")
    suspend fun login(@Body user: User): LoginResponse

    @POST("/verify")
    suspend fun verify(@Body body: VerifyBody): VerifiedResponse

    @POST("/refresh")
    suspend fun refresh(@Body token: RefreshToken): RefreshResponse

    @POST("/register")
    suspend fun register(@Body body: User): RegistrationResponse

    @POST("/subject")
    suspend fun setSubject(@Body body: SubjectBody): SubjectResponse

    @GET("/speciality")
    suspend fun getSpeciality(): SpecialityGetResponse

    @POST("/speciality")
    suspend fun setSpeciality(@Body body: SpecialityBody): SpecialitySetResponse

    @POST("/save_bonus")
    suspend fun saveBonus(@Body body: SaveBonusBody): RefreshToken

    @POST("/spo")
    suspend fun setSpo(@Body body: RefreshToken): RefreshToken

    @POST("/profile")
    suspend fun getProfile(@Body body: RefreshToken): ProfileGetResponse

    @POST("/loadprograms")
    suspend fun getPrograms(
        @Body body: ProgramBody,
        @Query("chunk") chunk: Int,
        @Query("chunk_size") chunkSize: Int = 30
    ): PaginatedResponse<Program>

    @POST("/toggle_favorite")
    suspend fun toggleFavorite(@Body body: FavoriteBody): RefreshToken


}