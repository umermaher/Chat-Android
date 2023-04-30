package com.therevotech.revoschat.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.therevotech.revoschat.ui.MainActivity
import com.therevotech.revoschat.ui.auth.authutils.Navigation
import com.therevotech.revoschat.ui.ui.theme.RevosChatTheme
import com.therevotech.revoschat.utils.SecretKeys
import com.therevotech.revoschat.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {
    private val viewModel: AuthViewModel by viewModels()

    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition{
                viewModel.isLoading.value
            }
            setOnExitAnimationListener{ ssViewProvider ->
                if(viewModel.isLoggedIn.value){
                    startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                    finish()
                }else
                    ssViewProvider.remove()
            }
        }
        setContent {
            RevosChatTheme {
                Navigation()
            }
        }
    }
}
