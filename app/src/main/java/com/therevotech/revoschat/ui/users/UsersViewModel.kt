package com.therevotech.revoschat.ui.users

import android.annotation.SuppressLint
import android.os.Looper
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.therevotech.revoschat.data.repositories.auth.AuthRepository
import com.therevotech.revoschat.data.repositories.general.GeneralRepository
import com.therevotech.revoschat.models.members.Location
import com.therevotech.revoschat.ui.users.map.MapCameraState
import com.therevotech.revoschat.ui.users.map.MapEvent
import com.therevotech.revoschat.ui.users.map.MapState
import com.therevotech.revoschat.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val generalRepository: GeneralRepository,
    private val authRepository: AuthRepository
): ViewModel() {

    private val _usersState = mutableStateOf(UsersState())
    val usersState: State<UsersState> = _usersState

    var mapState by mutableStateOf(MapState())

    private val _mapCameraState = mutableStateOf(MapCameraState())
    val mapCameraState: State<MapCameraState> = _mapCameraState

    private val  _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    init {
        getUsers()
    }

    private fun getUsers(){
        viewModelScope.launch {
            val result = generalRepository.getMembers()
            when(result){
                is Resource.Success -> {
                    _usersState.value = _usersState.value.copy(users = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _toastEvent.emit(result.message ?: "Unknown error")
                }
            }
        }
    }

    fun onEvent(event: MapEvent){
        when(event){
            is MapEvent.ToggleFallOutMap -> {
                mapState = mapState.copy(
                    properties = mapState.properties.copy(
                        mapStyleOptions =
                        if(mapState.isMapFallOut) null
                        else generalRepository.getFalloutMapStyleOption(),
                        isMyLocationEnabled = !firstTimeZoom
                    ),
                    isMapFallOut = !mapState.isMapFallOut
                )
            }
        }
    }

    var firstTimeZoom = true
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            for (location in p0.locations) {
                // Update UI with location data
                viewModelScope.launch {
                    val loc = Location(location.latitude, location.longitude)
                    Log.e("Location","${loc.lat}, ${loc.lng}")
                    if(firstTimeZoom) {
                        //camera zoom stuff
                        _mapCameraState.value = _mapCameraState.value.copy(
                            cameraPositionState = CameraPositionState(
                                position = CameraPosition.fromLatLngZoom(
                                    LatLng(loc.lat, loc.lng), 14.5f
                                )
                            )
                        )
                        firstTimeZoom = false
                    }

                    generalRepository.updateLocation(loc)
                    getUsers()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 15000
            fastestInterval = 10000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        generalRepository
            .getFusedLocationProviderClient()
            .requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
    }

    fun removeLocationUpdates(){
        generalRepository
            .getFusedLocationProviderClient()
            .removeLocationUpdates(locationCallback)
    }

    fun getLocationPermission() = arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}