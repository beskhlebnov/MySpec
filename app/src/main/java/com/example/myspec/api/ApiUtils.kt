package com.example.myspec.api

import android.util.Log
import org.json.JSONObject
import retrofit2.HttpException

class ApiUtils {
    companion object{
        private const val ON_EXCEPTION_ERROR_TEXT = "Неизвестная ошибка"

        fun handleHttpException(e: HttpException, cb: (String) -> Unit){
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try{
                JSONObject(errorBody!!).getString("message")
            } catch (ex: Exception){
                e.printStackTrace()
                ON_EXCEPTION_ERROR_TEXT
            }
            cb(errorMessage)
        }
    }
}