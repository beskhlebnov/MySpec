package com.example.myspec.screens.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myspec.viewmodels.auth.AuthorizationViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.myspec.constants.AuthSteps
import com.example.myspec.R
import com.example.myspec.components.LoadingAnimation
import com.example.myspec.components.controls.buttons.GradientButton
import com.example.myspec.components.Logo
import com.example.myspec.components.controls.fields.MyTextField
import com.example.myspec.components.controls.fields.PasswordField
import com.example.myspec.components.text.ThinText
import com.example.myspec.ui.theme.White

@Composable
fun AuthorizationScreen(navController: NavController, viewModel: AuthorizationViewModel = viewModel()) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val loading by viewModel.loading.collectAsState()
    val step by viewModel.step.collectAsState()
    val state by viewModel.state.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val passwordRepeated by viewModel.passwordRepeated.collectAsState()
    val code by viewModel.code.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.toastEvent.collect { event ->
            when(event) {
                is AuthorizationViewModel.ToastEvent.ResourceMessage ->
                    Toast.makeText(context, context.getString(event.resId), Toast.LENGTH_SHORT).show()
                is AuthorizationViewModel.ToastEvent.StringMessage ->
                    Toast.makeText(context, event.text, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Logo()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f)
                .padding(horizontal = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Transparent),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            ThinText(
                stringId = if (step == AuthSteps.REGISTER || step == AuthSteps.CODE) R.string.registration_title
                    else R.string.authorization_title
                ,
                color = White
            )
            when(step) {
                AuthSteps.LOGIN -> {
                    LoginView(
                        email = email,
                        password = password,
                        onEmailChanged = viewModel::onEmailFieldValueChanged,
                        onPasswordChanged = viewModel::onPasswordFieldValueChanged,
                        onLoginClick = {
                            keyboardController?.hide()
                            viewModel.onLoginButtonClick()
                        },
                        onSwitchToRegister = viewModel::switchToRegister
                    )
                }

                AuthSteps.REGISTER -> {
                    RegisterView(
                        email = email,
                        password = password,
                        passwordRepeated = passwordRepeated,
                        onEmailChanged = viewModel::onEmailFieldValueChanged,
                        onPasswordChanged = viewModel::onPasswordFieldValueChanged,
                        onPasswordRepeatedChanged = viewModel::onPasswordRepeatedFieldValueChanged,
                        onRegisterClick = {
                            keyboardController?.hide()
                            viewModel.onRegisterButtonClick()
                        },
                        onSwitchToLogin = viewModel::switchToLogin
                    )
                }

                AuthSteps.CODE -> {
                    CodeVerificationView(
                        code = code,
                        onCodeChanged = viewModel::onCodeFieldValueChanged
                    )
                }

                AuthSteps.FINAL -> {
                    navController.navigate(if (state == 0) "home" else "list") {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                }
                AuthSteps.PASSWORD_REPEAT -> TODO()
            }

            if(loading) {
                LoadingAnimation()
            }
        }
    }
}

@Composable
private fun LoginView(
    email: String,
    password: String,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClick: () -> Unit,
    onSwitchToRegister: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        MyTextField(
            value = email,
            labelStringId = R.string.authorization_email_label,
            type = KeyboardType.Email,
            onValueChange = onEmailChanged
        )

        PasswordField(
            value = password,
            onValueChange = onPasswordChanged
        )

        GradientButton(
            stringId = R.string.login,
            onClick = onLoginClick
        )

        GradientButton(
            stringId = R.string.register,
            onClick = onSwitchToRegister
        )
    }
}

@Composable
private fun RegisterView(
    email: String,
    password: String,
    passwordRepeated: String,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onPasswordRepeatedChanged: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onSwitchToLogin: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        MyTextField(
            value = email,
            labelStringId = R.string.authorization_email_label,
            type = KeyboardType.Email,
            onValueChange = onEmailChanged
        )

        PasswordField(
            value = password,
            //labelStringId = R.string.authorization_password_label,
            onValueChange = onPasswordChanged
        )

        PasswordField(
            value = passwordRepeated,
            //labelStringId = R.string.authorization_repeat_password_label,
            onValueChange = onPasswordRepeatedChanged
        )

        GradientButton(
            stringId = R.string.register,
            onClick = onRegisterClick
        )

        GradientButton(
            stringId = R.string.back_to_login,
            //modifier = Modifier.padding(top = 8.dp),
            onClick = onSwitchToLogin
        )
    }
}

@Composable
private fun CodeVerificationView(
    code: String,
    onCodeChanged: (String) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        ThinText(
            stringId = R.string.authorization_code_message,
            size = 20,
            color = White
        )
        ThinText(
            stringId = R.string.authorization_code_forlabel,
            size = 15,
            color = White
        )

        MyTextField(
            value = code,
            labelStringId = R.string.authorization_code_label,
            type = KeyboardType.Number,
            onValueChange = onCodeChanged,
        )
    }
}