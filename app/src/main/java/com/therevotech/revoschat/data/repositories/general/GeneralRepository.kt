package com.therevotech.revoschat.data.repositories.general

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.therevotech.revoschat.models.members.Location
import com.therevotech.revoschat.models.members.Member
import com.therevotech.revoschat.utils.Resource

interface GeneralRepository {
    suspend fun getMembers(): Resource<List<Member>>
    suspend fun updateLocation(lng: Location)
    suspend fun updateAvatar(path: String)
    fun getFalloutMapStyleOption(): MapStyleOptions
    fun getFusedLocationProviderClient(): FusedLocationProviderClient
}