package com.therevotech.revoschat.ui

//import android.location.LocationListener;

import android.Manifest
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.tasks.Task
import com.therevotech.revoschat.R
import com.therevotech.revoschat.models.auth.AuthResult
import com.therevotech.revoschat.ui.auth.AuthActivity
import com.therevotech.revoschat.ui.chat.ChatScreen
import com.therevotech.revoschat.ui.ui.theme.RevosChatTheme
import com.therevotech.revoschat.ui.users.UsersScreen
import com.therevotech.revoschat.utils.SecretKeys
import com.therevotech.revoschat.utils.ShowProgressBar
import com.therevotech.revoschat.utils.showMessageDialog
import com.therevotech.revoschat.utils.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    var mLocationRequest: LocationRequest? = null

    companion object{
        const val REQUEST_CHECK_SETTINGS = 99
        const val MY_PERMISSIONS_REQUEST_LOCATION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RevosChatTheme {
                val state = viewModel.state
                LaunchedEffect(viewModel, this){
                    viewModel.authResults.collect { result ->
                        when(result){
                            is AuthResult.Authorized -> {}
                            is AuthResult.UnAuthorized -> {
                                showMessageDialog(
                                    title = getString(R.string.error),
                                    message = getString(R.string.session_expired_msg)
                                ){
                                    logout()
                                }
                            }
                            is AuthResult.UnKnownError -> {
                                showMessageDialog(
                                    title = getString(R.string.error),
                                    message = result.message.toString()
                                ){
                                    logout()
                                }
                            }
                            is AuthResult.NetworkError -> {
                                showMessageDialog(
                                    title = getString(R.string.error),
                                    message = result.message.toString()
                                )
                            }
                        }
                    }
                }
                if (state.isLoading) {
                    ShowProgressBar()
                }else {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "chat_screen"
                    ){
                        composable("chat_screen"){
                            ChatScreen(navController = navController,username = viewModel.getUsername())
                        }
                        composable("users_screen"){
                            UsersScreen(viewModel.getUsername())
                        }
                    }
                }
            }
        }
    }

    private fun logout() {
        viewModel.logout()
        startActivity(Intent(this@MainActivity,AuthActivity::class.java))
        finish()
    }

    private fun gpsDialog() {
//        val builder: LocationSettingsRequest.Builder =
//            LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest)
//        builder.setAlwaysShow(true)
//        val responseTask: Task<LocationSettingsResponse> = LocationServices
//            .getSettingsClient(applicationContext).checkLocationSettings(builder.build())
//
//        responseTask.addOnCompleteListener {task ->
//            try {
//                val response: LocationSettingsResponse = task.getResult(ApiException::class.java)
//            } catch (e: ApiException) {
//                if (e.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
//                    val resolvableApiException = e as ResolvableApiException
//                    try {
//                        resolvableApiException.startResolutionForResult(
//                            this@MainActivity,
//                            REQUEST_CHECK_SETTINGS
//                        )
//                    } catch (sendIntentException: SendIntentException) {
//                        Log.i("tag", "PendingIntent unable to execute request.")
//                    }
//                }
//                if (e.statusCode == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE) {
//                    Toast.makeText(this@MainActivity, "Settings not available", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            }
//        }
    }

}