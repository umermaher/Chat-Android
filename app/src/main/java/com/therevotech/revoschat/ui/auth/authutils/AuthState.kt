package com.therevotech.revoschat.ui.auth.authutils

data class LoginState(
    val isLoading: Boolean = false,
    val signInUsername: String = "",
    val signInPassword: String = "",
)

data class SignUpState(
    val isLoading: Boolean = false,
    val signUpUsername: String = "",
    val signUpPassword: String = "",
)