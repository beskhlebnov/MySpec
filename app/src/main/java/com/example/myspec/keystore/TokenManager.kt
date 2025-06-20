package com.example.myspec.keystore

import android.content.Context
import android.util.Base64
import android.widget.Toast


object TokenManager {
    private const val PREF_NAME = "secure_prefs"
    private const val TOKEN_KEY = "refresh_token"
    private const val IV_KEY = "iv_key"

    fun saveRefreshToken(context: Context, token: String){
        val (iv, encryptedToken) = KeystoreManager.encrypt(token)
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putString(TOKEN_KEY, Base64.encodeToString(encryptedToken, Base64.DEFAULT))
            .putString(IV_KEY, Base64.encodeToString(iv, Base64.DEFAULT))
            .apply()
    }


    fun getRefreshToken(context: Context): String?{
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val encryptedToken = sharedPreferences.getString(TOKEN_KEY, null)
        val iv = sharedPreferences.getString(IV_KEY, null)
        return if(encryptedToken != null && iv != null) {
            try {
                KeystoreManager.decrypt(
                    Base64.decode(iv, Base64.DEFAULT),
                    Base64.decode(encryptedToken, Base64.DEFAULT))
            } catch (e: Exception){
                e.printStackTrace()
                null
            }
        } else null
    }


    fun removeRefreshToken(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .remove(TOKEN_KEY)
            .remove(IV_KEY)
            .apply()
    }

}