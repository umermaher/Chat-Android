package com.therevotech.revoschat.ui.auth.signup

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.therevotech.revoschat.R
import com.therevotech.revoschat.models.auth.AuthResult
import com.therevotech.revoschat.ui.MainActivity
import com.therevotech.revoschat.ui.auth.authutils.*
import com.therevotech.revoschat.utils.ShowProgressBar
import com.therevotech.revoschat.utils.isUsernameValid
import com.therevotech.revoschat.utils.showMessageDialog
import com.therevotech.revoschat.utils.validatePassword
import java.util.*

@Composable
fun SignUpScreen(
    name: String?,
    viewModel: SignUpViewModel = hiltViewModel()
){
    val context = LocalContext.current
    val activity = context as Activity
    val state = viewModel.state

    LaunchedEffect(viewModel, activity){
        viewModel.authResults.collect{ result ->
            when(result){
                is AuthResult.Authorized -> {
                    activity.startActivity(Intent(context, MainActivity::class.java))
                    activity.finish()
                }
                is AuthResult.UnAuthorized -> {
                    context.showMessageDialog(
                        title = context.getString(R.string.error),
                        message = context.getString(R.string.unauthorized_msg)
                    )
                }
                is AuthResult.UnKnownError -> {
                    context.showMessageDialog(
                        title = context.getString(R.string.error),
                        message = result.message.toString()
                    )
                }
                is AuthResult.NetworkError -> {
                    context.showMessageDialog(
                        title = context.getString(R.string.error),
                        message = result.message.toString()
                    )
                }
            }
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxWidth(),
        ) {
            TopIconSegment()
        }

        Column(
            modifier = Modifier
                .weight(0.1f)
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(id = R.string.create_account),
                fontFamily = FontFamily.SansSerif,
                color = colorResource(id = R.color.secondary_text_color)
            )
        }

        Column (
            modifier = Modifier
                .weight(0.5f)
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {

                TextInput(InputType.Name, state.signUpUsername) { text ->
                    viewModel.onEvent(SignUpUiEvent.SignUpUsernameChanged(text))
                }
                AuthComponentSpacer()

                TextInput(InputType.Password, state.signUpPassword) { text ->
                    viewModel.onEvent(SignUpUiEvent.SignUpPasswordChanged(text))
                }
                AuthComponentSpacer()

                LoginSignUpBtn(
                    text = stringResource(id = R.string.sign_up)
                ) {
                    val isUsernameValid= viewModel.state.signUpUsername.isUsernameValid(context)
                    if(!isUsernameValid) return@LoginSignUpBtn
                    val isPwdValid = viewModel.state.signUpPassword.validatePassword(context)
                    if(!isPwdValid) return@LoginSignUpBtn

                    viewModel.onEvent(SignUpUiEvent.SignUpEvent)
                }

                AuthComponentSpacer()

                SuggestNavigationButton(
                    text = stringResource(id = R.string.already_have_an_account),
                    btnText =stringResource(id = R.string.login)
                ) {
                    activity.onBackPressed()
                }
            }
        }
    }
    if (state.isLoading) {
        ShowProgressBar()
    }
}

