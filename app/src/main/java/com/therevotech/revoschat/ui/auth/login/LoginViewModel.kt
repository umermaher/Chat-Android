package com.therevotech.revoschat.ui.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therevotech.revoschat.models.auth.AuthResult
import com.therevotech.revoschat.data.repositories.auth.AuthRepository
import com.therevotech.revoschat.ui.auth.authutils.LoginState
import com.therevotech.revoschat.ui.auth.authutils.LoginUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    var state by mutableStateOf(LoginState())

    private val resultChannel = Channel<AuthResult<Unit>>()
    val authResults = resultChannel.receiveAsFlow()

    fun onEvent(event: LoginUiEvent){
        when(event){
            is LoginUiEvent.SignInUsernameChanged -> {
                state = state.copy(signInUsername = event.value)
            }
            is LoginUiEvent.SignInPasswordChanged -> {
                state = state.copy(signInPassword = event.value)
            }
            is LoginUiEvent.SignInEvent -> {
                signIn()
            }
        }
    }

    private fun signIn(){
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            val result = repository.signIn(
                username = state.signInUsername,
                password = state.signInPassword
            )
            resultChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }
}