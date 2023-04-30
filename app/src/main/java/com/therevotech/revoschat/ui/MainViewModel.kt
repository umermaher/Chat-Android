package com.therevotech.revoschat.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therevotech.revoschat.models.auth.AuthResult
import com.therevotech.revoschat.data.repositories.auth.AuthRepository
import com.therevotech.revoschat.ui.auth.authutils.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel(){
    var state by mutableStateOf(LoginState())

    private val resultChannel = Channel<AuthResult<Unit>>()
    val authResults = resultChannel.receiveAsFlow()

    init {
        authenticate()
    }

    private fun authenticate(){
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            val result = repository.authenticate()
            resultChannel.send(result)

            state = state.copy(isLoading = false)
        }
    }

    fun logout() = repository.logout()

    fun getUsername(): String = repository.getUsername()
}