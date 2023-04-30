package com.therevotech.revoschat.ui.auth.authutils

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.therevotech.revoschat.ui.auth.login.LoginScreen
import com.therevotech.revoschat.ui.auth.signup.SignUpScreen

@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.LoginScreen.route ){
        composable(route = Screen.LoginScreen.route){
            LoginScreen(navController = navController)
        }
        composable(
            route = Screen.SignupScreen.route + "/{name}",
            arguments = listOf(
                navArgument("name"){
                    type = NavType.StringType
                    defaultValue = "umer"
                    nullable = true
                }
            )
        ){ entry ->
            SignUpScreen(name = entry.arguments?.getString("name"))
        }
    }
}