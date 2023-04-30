package com.therevotech.revoschat.ui.users.map

import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapProperties
import com.therevotech.revoschat.models.members.Location

data class MapState (
    val properties:  MapProperties = MapProperties(),
    val isMapFallOut: Boolean = false,
)

data class MapCameraState(
    val cameraPositionState: CameraPositionState = CameraPositionState()
)

data class LocationState (
    val myLocation: Location ?= null,
    val othersLocation: List<Location> = emptyList()
)