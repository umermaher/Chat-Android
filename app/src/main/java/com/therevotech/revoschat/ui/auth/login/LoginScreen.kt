package com.therevotech.revoschat.ui.auth.login

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import androidx.navigation.NavController
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
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val state = viewModel.state
    LaunchedEffect(viewModel, context){
        viewModel.authResults.collect { result ->
            when(result){
                is AuthResult.Authorized -> {
                    context.startActivity(Intent(context,MainActivity::class.java))
                    (context as Activity).finish()
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
                text = stringResource(id = R.string.sign_in_to_continue),
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

                TextInput(InputType.Name, state.signInUsername) { value ->
                    viewModel.onEvent(LoginUiEvent.SignInUsernameChanged(value))
                }

                AuthComponentSpacer()

                TextInput(InputType.Password, state.signInPassword) { value ->
                    viewModel.onEvent(LoginUiEvent.SignInPasswordChanged(value))
                }

                AuthComponentSpacer()

                LoginSignUpBtn(
                    text = stringResource(id = R.string.sign_in)
                ) {
                    val isUsernameValid= viewModel.state.signInUsername.isUsernameValid(context)
                    if(!isUsernameValid) return@LoginSignUpBtn
                    val isPwdValid = viewModel.state.signInPassword.validatePassword(context)
                    if(!isPwdValid) return@LoginSignUpBtn

                    viewModel.onEvent(LoginUiEvent.SignInEvent)
                }

                AuthComponentSpacer()

                SuggestNavigationButton(
                    text = stringResource(id = R.string.dont_have_an_account),
                    btnText = stringResource(id = R.string.register)
                ) {
                    navController.navigate(
                        Screen.SignupScreen.withArgs("toSignup")
                    )
                }
            }
        }
    }
    if (state.isLoading) {
        ShowProgressBar()
    }
}