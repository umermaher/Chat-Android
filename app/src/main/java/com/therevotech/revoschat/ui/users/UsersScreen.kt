package com.therevotech.revoschat.ui.users

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.PowerInput
import androidx.compose.material.icons.filled.PowerOff
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.SettingsPower
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.therevotech.revoschat.R
import com.therevotech.revoschat.ui.auth.AuthActivity
import com.therevotech.revoschat.ui.users.map.MapEvent
import com.therevotech.revoschat.utils.getBitmapDescriptor
import com.therevotech.revoschat.utils.showConfirmDialog
import com.therevotech.revoschat.utils.showMessageDialog
import com.therevotech.revoschat.utils.showToast
import kotlinx.coroutines.flow.collectLatest

@Composable
fun UsersScreen (
    username: String,
    viewModel: UsersViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    LaunchedEffect(true){
        viewModel.toastEvent.collectLatest{ msg ->
            context.showToast(msg)
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner){
        val observer = LifecycleEventObserver{_, event ->
            if(event == Lifecycle.Event.ON_RESUME)
                viewModel.startLocationUpdates()
            if(event == Lifecycle.Event.ON_PAUSE)
                viewModel.removeLocationUpdates()
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val permissions = viewModel.getLocationPermission()

    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->

        if(permissionsMap.values.isEmpty()) return@rememberLauncherForActivityResult

        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }

        if (areGranted) {
            viewModel.startLocationUpdates()
            context.showToast(context.getString(R.string.permission_granted))
        } else {
            context.showToast(context.getString(R.string.permission_granted))
        }
    }

    if(permissions.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }) {
        // Get the location
        viewModel.startLocationUpdates()
    } else {
        SideEffect {
            launcherMultiplePermissions.launch(permissions)
        }
    }

    val state = viewModel.usersState.value

    Column (
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
                .padding(16.dp)
        ){
            items(state.users){ user ->
                Row (
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {}, modifier = Modifier.size(45.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.default_avatar),
                            contentDescription = null,
                        )
                    }
                    Text(
                        text = user.username + if(username == user.username) " (You)" else "",
                        modifier = Modifier
                            .weight(1f)
                            .padding(26.dp)
                    )
                    if(username == user.username)
                        IconButton(onClick = {

                            context.showConfirmDialog(
                                title = context.getString(R.string.hey) + " $username",
                                message = context.getString(R.string.logout_confirm_msg)
                            ){
                                viewModel.logout()
                                context.startActivity(Intent(context, AuthActivity::class.java))
                                (context as Activity).finish()
                            }

                        }, modifier = Modifier.size(45.dp)) {
                            Icon(imageVector = Icons.Default.PowerSettingsNew, contentDescription = "Logout")
                        }
                }
                Divider(
                    modifier = Modifier.height(0.5.dp),
                    color = Color.Gray
                )
            }
        }

        Box(
            modifier = Modifier.weight(0.5f)
        ){
            val scaffoldState = rememberScaffoldState()
            val uiSettings = remember {
                MapUiSettings(zoomControlsEnabled = false)
            }
            val cameraState = viewModel.mapCameraState.value.cameraPositionState

            Scaffold (
                scaffoldState = scaffoldState,
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            viewModel.onEvent(MapEvent.ToggleFallOutMap)
                        },
                        backgroundColor = colorResource(id = R.color.theme_color)
                    ) {
                        Icon(
                            imageVector =
                            if(viewModel.mapState.isMapFallOut) Icons.Default.ToggleOff
                            else Icons.Default.ToggleOn,
                            contentDescription = "Toggle Map fallout"
                        )
                    }
                },
                modifier = Modifier.fillMaxSize()
            ) {

                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    properties = viewModel.mapState.properties,
                    uiSettings = uiSettings,
                    cameraPositionState = cameraState
                ){
                    state.users.forEach {  user ->
                        user.location?.let {
                            Marker(
                                position = LatLng(
                                    it.lat,
                                    it.lng
                                ),
                                title =
                                if(username == user.username) context.getString(R.string.you)
                                else user.username,
                                snippet = user.location.toString(),
                                icon =
                                if(username == user.username) getBitmapDescriptor(context, R.drawable.default_avatar)
                                else null
                            )
                        }
                    }
                }
            }
        }
    }
}