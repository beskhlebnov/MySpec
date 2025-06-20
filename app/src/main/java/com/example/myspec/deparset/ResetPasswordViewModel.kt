//package com.example.myspec.viewmodels.auth
//
//import android.app.Application
//import android.util.Log
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.myspec.constants.AuthSteps
//import com.example.myspec.R
//import com.example.myspec.api.ApiUtils
//import com.example.myspec.api.RetrofitInstance
//import com.example.myspec.models.User
//import com.example.myspec.api.body.VerifyBody
//import com.example.myspec.constants.ResetPasswordSteps
//import com.example.myspec.keystore.TokenManager
//import kotlinx.coroutines.flow.MutableSharedFlow
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asSharedFlow
//import kotlinx.coroutines.launch
//import retrofit2.HttpException
//
//class ResetPasswordViewModel(application: Application): AndroidViewModel(application) {
//    private val _loading = MutableStateFlow(false)
//    val loading: StateFlow<Boolean> = _loading
//
//    private val _step = MutableStateFlow(ResetPasswordSteps.START)
//    val step: StateFlow<ResetPasswordSteps> = _step
//
//    private val _state = MutableStateFlow(0)
//    val state: StateFlow<Int> = _state
//
//    private val _email = MutableStateFlow("")
//    val email: StateFlow<String> = _email
//
//    private val _password = MutableStateFlow("")
//    val password: StateFlow<String> = _password
//
//    private val _passwordRepeated = MutableStateFlow("")
//    val passwordRepeated: StateFlow<String> = _passwordRepeated
//
//    private val _refreshToken = MutableStateFlow<String?>(null)
//    private val refreshToken: StateFlow<String?> = _refreshToken
//
//    sealed class ToastEvent {
//        data class ResourceMessage(val resId: Int) : ToastEvent()
//        data class StringMessage(val text: String) : ToastEvent()
//    }
//
//    private val _toastEvent = MutableSharedFlow<ToastEvent>()
//    val toastEvent = _toastEvent.asSharedFlow()
//
//    private val _code = MutableStateFlow("")
//    val code: StateFlow<String> = _code
//
//    fun onPasswordFieldValueChanged(newValue: String) {
//        _password.value = newValue
//    }
//
//    fun onPasswordRepeatedFieldValueChanged(newValue: String) {
//        _passwordRepeated.value = newValue
//    }
//
//    fun onCodeFieldValueChanged(newValue: String) {
//        _code.value = newValue
//        if(_code.value.length == 6) {
//            verify()
//        }
//    }
//
//    fun onLoginButtonClick() {
//        if(email.value.isBlank()) {
//            showToastMessage(R.string.authorization_email_error)
//            return
//        }
//        if(password.value.isBlank()) {
//            showToastMessage(R.string.authorization_password_error)
//            return
//        }
//        sendCode()
//    }
//
//    fun onRegisterButtonClick() {
//        if(email.value.isBlank()) {
//            showToastMessage(R.string.authorization_email_error)
//            return
//        }
//        if(password.value.isBlank()) {
//            showToastMessage(R.string.authorization_password_error)
//            return
//        }
//        if(password.value != passwordRepeated.value) {
//            showToastMessage(R.string.authorization_passwords_match_error)
//            return
//        }
//        register()
//    }
//
//    private fun sendCode() {
//        _loading.value = true
//        viewModelScope.launch {
//            try {
//                val loginResponse = RetrofitInstance.api.sendCode(User(email.value, password.value))
//                val context = getApplication<Application>().applicationContext
//                TokenManager.saveRefreshToken(context, loginResponse.refreshToken)
//                _state.value = loginResponse.state
//                _step.value = ResetPasswordSteps.FINAL
//            } catch (e: HttpException) {
//                when (e.code()) {
//                    404 -> {
//                        showToastMessage(R.string.user_not_found)
//                        _step.value = ResetPasswordSteps.PASSWORD
//                    }
//                    401 -> showToastMessage(R.string.invalid_credentials)
//                    else -> ApiUtils.handleHttpException(e) { error ->
//                        showToastServerMessage(error)
//                    }
//                }
//            } catch (e: Exception) {
//                Log.e("LOGIN_ERROR", "Error during login", e)
//                showToastMessage(R.string.network_error)
//            } finally {
//                _loading.value = false
//            }
//        }
//    }
//
//    private fun register() {
//        _loading.value = true
//        viewModelScope.launch {
//            try {
//                val registerResponse = RetrofitInstance.api.register(User(email.value, password.value))
//                _refreshToken.value = registerResponse.refreshToken
//                _step.value = AuthSteps.CODE
//            } catch (e: HttpException) {
//                when (e.code()) {
//                    400 -> {Log.e("REG","USER_ALREADY_EXISTS"); showToastMessage(R.string.user_already_exists)}
//                    else -> ApiUtils.handleHttpException(e) { error ->
//                        showToastServerMessage(error)
//                    }
//                }
//            } catch (e: Exception) {
//                Log.e("REGISTER_ERROR", "Error during registration", e)
//                showToastMessage(R.string.network_error)
//            } finally {
//                _loading.value = false
//            }
//        }
//    }
//
//    private fun verify() {
//        _loading.value = true
//        viewModelScope.launch {
//            try {
//                val verifyResponse = RetrofitInstance.api.verify(VerifyBody(email.value, code.value))
//                if(verifyResponse.isVerified) {
//                    val context = getApplication<Application>().applicationContext
//                    TokenManager.saveRefreshToken(context, refreshToken.value!!)
//                    _step.value = AuthSteps.FINAL
//                }
//            } catch (e: HttpException) {
//                ApiUtils.handleHttpException(e) { error ->
//                    showToastServerMessage(error)
//                }
//            } catch (e: Exception) {
//                Log.e("VERIFY_ERROR", "Error during verification", e)
//                showToastMessage(R.string.network_error)
//            } finally {
//                _loading.value = false
//            }
//        }
//    }
//
//    private suspend fun emitToastEvent(event: ToastEvent) {
//        _toastEvent.emit(event)
//    }
//
//    private fun showToastMessage(message: Int) {
//        viewModelScope.launch {
//            emitToastEvent(ToastEvent.ResourceMessage(message))
//        }
//    }
//
//    private fun showToastServerMessage(message: String) {
//        viewModelScope.launch {
//            emitToastEvent(ToastEvent.StringMessage(message))
//        }
//    }
//}