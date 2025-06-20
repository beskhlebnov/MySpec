//package com.example.myspec.screens
//
//import androidx.compose.runtime.Composable
//import android.widget.Toast
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalSoftwareKeyboardController
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import com.example.myspec.viewmodels.auth.AuthorizationViewModel
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavGraph.Companion.findStartDestination
//import com.example.myspec.constants.AuthSteps
//import com.example.myspec.R
//import com.example.myspec.components.LoadingAnimation
//import com.example.myspec.components.controls.buttons.GradientButton
//import com.example.myspec.components.Logo
//import com.example.myspec.components.controls.fields.MyTextField
//import com.example.myspec.components.controls.fields.PasswordField
//import com.example.myspec.components.text.ThinText
//import com.example.myspec.constants.ResetPasswordSteps
//import com.example.myspec.ui.theme.White
//import com.example.myspec.viewmodels.auth.ResetPasswordViewModel
//
//@Composable
//fun ResetPasswordScreen(
//    navController: NavController,
//    viewModel: ResetPasswordViewModel = viewModel()
//) {
//    val context = LocalContext.current
//    val keyboardController = LocalSoftwareKeyboardController.current
//
//    val loading by viewModel.loading.collectAsState()
//    val step by viewModel.step.collectAsState()
//    val state by viewModel.state.collectAsState()
//    val email by viewModel.email.collectAsState()
//    val password by viewModel.password.collectAsState()
//    val passwordRepeated by viewModel.passwordRepeated.collectAsState()
//    val code by viewModel.code.collectAsState()
//
//    Column(
//        modifier = Modifier.fillMaxWidth(),
//    ) {
//        Logo()
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(3f)
//                .padding(horizontal = 8.dp)
//                .clip(RoundedCornerShape(16.dp))
//                .background(Color.Transparent),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Top
//        ) {
//            Text(
//                text = "Сброс пароля",
//                style = MaterialTheme.typography.titleLarge,
//                color = MaterialTheme.colorScheme.primary
//            )
//
//            when(step) {
//
//                ResetPasswordSteps.START -> StartView(
//                    email = email,
//                    onStartClick = {},
//                    onBackClick = {}
//                )
//
//                ResetPasswordSteps.CODE -> CodeVerificationView(
//                    code = code,
//                    onCodeChanged = viewModel::onCodeFieldValueChanged
//                )
//
//                ResetPasswordSteps.PASSWORD -> PasswordChangeView(
//                    password = password,
//                    passwordRepeated = passwordRepeated,
//                    onPasswordChanged = viewModel::onPasswordFieldValueChanged,
//                    onPasswordRepeatedChanged = viewModel::onPasswordRepeatedFieldValueChanged,
//                    onCancelClick = {},
//                    onSaveClick = {}
//                )
//
//                ResetPasswordSteps.FINAL -> {
//                    navController.navigate("profile") {
//                        popUpTo(navController.graph.findStartDestination().id)
//                        launchSingleTop = true
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//private fun StartView(
//    email: String,
//    onStartClick: () -> Unit,
//    onBackClick: () -> Unit
//) {
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//
//        Text(
//            text = "Для сброса пороля было отправлено письмо на адрес $email",
//            style = MaterialTheme.typography.titleLarge,
//            color = MaterialTheme.colorScheme.primary
//        )
//
//        GradientButton(
//            stringId = R.string.button_continue,
//            onClick = onStartClick
//        )
//
//        GradientButton(
//            stringId = R.string.button_cancel,
//            onClick = onBackClick
//        )
//    }
//}
//
//@Composable
//private fun PasswordChangeView(
//    password: String,
//    passwordRepeated: String,
//    onPasswordChanged: (String) -> Unit,
//    onPasswordRepeatedChanged: (String) -> Unit,
//    onSaveClick: () -> Unit,
//    onCancelClick: () -> Unit,
//) {
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//
//        Text(
//            text = "Введите новый пароль",
//            style = MaterialTheme.typography.titleLarge,
//            color = MaterialTheme.colorScheme.primary
//        )
//
//        PasswordField(
//            value = password,
//            onValueChange = onPasswordChanged
//        )
//
//        PasswordField(
//            value = passwordRepeated,
//            onValueChange = onPasswordRepeatedChanged
//        )
//
//        GradientButton(
//            stringId = R.string.save,
//            onClick = onSaveClick
//        )
//
//        GradientButton(
//            stringId = R.string.button_cancel,
//            onClick = onCancelClick
//        )
//    }
//}
//
//@Composable
//private fun CodeVerificationView(
//    code: String,
//    onCodeChanged: (String) -> Unit
//) {
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        ThinText(
//            stringId = R.string.authorization_code_message,
//            size = 20,
//            color = White
//        )
//
//        MyTextField(
//            value = code,
//            labelStringId = R.string.authorization_code_label,
//            type = KeyboardType.Number,
//            onValueChange = onCodeChanged,
//        )
//    }
//}