package com.therevotech.revoschat.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therevotech.revoschat.data.repositories.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository): ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading get() = _isLoading.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn get() = _isLoggedIn.asStateFlow()

    init {
        viewModelScope.launch {
            delay(2000)
            _isLoggedIn.value = repository.isLoggedIn()
            _isLoading.value = false
        }
    }
}