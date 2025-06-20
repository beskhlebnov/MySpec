package com.example.myspec.viewmodels.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myspec.models.RefreshToken
import com.example.myspec.api.RetrofitInstance
import com.example.myspec.api.body.FavoriteBody
import com.example.myspec.api.body.ProgramBody
import com.example.myspec.keystore.TokenManager.getRefreshToken
import com.example.myspec.keystore.TokenManager.saveRefreshToken
import com.example.myspec.models.Program
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ListViewModel(application: Application) : AndroidViewModel(application) {
    private val _specialties = MutableStateFlow<List<Program>>(emptyList())
    val specialties: StateFlow<List<Program>> = _specialties

    private val _isLoading = MutableStateFlow(false)
    private val isLoading: StateFlow<Boolean> = _isLoading

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _hasMore = MutableStateFlow(true)
    val hasMore: StateFlow<Boolean> = _hasMore

    private var currentChunk = 0
    private val chunkSize = 30
    private var totalItems = 0
    private var autoLoadJob: Job? = null


    fun toggleFavorite(programId: Int) {
        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext
                val token = getRefreshToken(context)
                if (!token.isNullOrBlank()) {
                    val response = RetrofitInstance.api.toggleFavorite(
                        FavoriteBody(RefreshToken(token), programId)
                    )
                    if (response.refreshToken.isNotBlank()) {
                        saveRefreshToken(context, response.refreshToken)
                    }
                }
            } catch (e: HttpException) {
                when (e.code()) {
                    404 -> {
                        Log.e(e.code().toString(), e.message())
                    }

                    401 -> {
                        Log.e(e.code().toString(), e.message())
                    }
                }
            } catch (e: Exception) {
                Log.e("toggleFavorite", e.toString())
            }
        }
    }

    fun loadInitialData(isFavorite: Boolean) {
        autoLoadJob?.cancel()
        autoLoadJob = viewModelScope.launch {
            _isLoading.value = false
            _error.value = null
            currentChunk = 0

            try {
                val context = getApplication<Application>().applicationContext
                val storedToken = getRefreshToken(context)

                if (!storedToken.isNullOrEmpty()) {
                    while (_hasMore.value && !isLoading.value) {
                        val response = RetrofitInstance.api.getPrograms(
                            ProgramBody(
                                token = RefreshToken(storedToken),
                                isFavorites = isFavorite
                            ),
                            chunk = currentChunk,
                            chunkSize = chunkSize
                        )
                        totalItems = response.total
                        _specialties.value += response.data
                        _hasMore.value = response.hasMore
                        _loading.value = false
                        currentChunk++
                        delay(300)
                    }
                }
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки: ${e.message}"
                Log.e("ListViewModel", "Error loading data", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        autoLoadJob?.cancel()
    }
}