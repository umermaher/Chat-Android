package com.therevotech.revoschat.ui.auth.authutils

sealed class Screen(val route:String){
    object LoginScreen : Screen("login_screen")
    object SignupScreen : Screen("signup_screen")

    fun withArgs(vararg args: String): String{
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
