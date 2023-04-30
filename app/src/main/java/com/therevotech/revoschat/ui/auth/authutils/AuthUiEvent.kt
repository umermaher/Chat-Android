package com.therevotech.revoschat.ui.auth.authutils

sealed class LoginUiEvent{
    data class SignInUsernameChanged(val value:String): LoginUiEvent()
    data class SignInPasswordChanged(val value:String): LoginUiEvent()
    object SignInEvent: LoginUiEvent()
}

sealed class SignUpUiEvent{
    data class SignUpUsernameChanged(val value: String): SignUpUiEvent()
    data class SignUpPasswordChanged(val value: String): SignUpUiEvent()
    object SignUpEvent: SignUpUiEvent()
}


