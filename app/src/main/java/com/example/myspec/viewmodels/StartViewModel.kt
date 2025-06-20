package com.example.myspec.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myspec.models.RefreshToken
import com.example.myspec.api.RetrofitInstance
import com.example.myspec.keystore.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class StartViewModel (application: Application): AndroidViewModel(application) {
    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    private val _refreshed = MutableStateFlow(false)
    val refreshed: StateFlow<Boolean> = _refreshed

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _state = MutableStateFlow(0)
    val state: StateFlow<Int> = _state

    fun refresh() {
        viewModelScope.launch {
            try {
                _loading.value = true
                val context = getApplication<Application>().applicationContext
                val storedToken = TokenManager.getRefreshToken(context)
                if(storedToken != null && storedToken != ""){
                    val response = RetrofitInstance.api.refresh(RefreshToken(storedToken))
                    if (response.refreshToken != "") {
                        TokenManager.saveRefreshToken(context, response.refreshToken)
                        _state.value = response.state
                        _refreshed.value = true
                    }
                }
            } catch (e: HttpException) {
                if (e.code() != 401 && e.code() != 400){
                    _error.value = e.message
                }
                e.printStackTrace()
            }  finally {
                _loading.value = false
            }
        }
    }
}