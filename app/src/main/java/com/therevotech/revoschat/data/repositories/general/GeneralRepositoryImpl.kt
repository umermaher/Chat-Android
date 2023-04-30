package com.therevotech.revoschat.data.repositories.general

import android.app.Application
import android.content.SharedPreferences
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.therevotech.revoschat.R
import com.therevotech.revoschat.apis.RcApi
import com.therevotech.revoschat.models.members.Location
import com.therevotech.revoschat.models.members.Member
import com.therevotech.revoschat.utils.Resource
import com.therevotech.revoschat.utils.hasInternetConnection

class GeneralRepositoryImpl(
    private val api: RcApi,
    private val prefs: SharedPreferences,
    private val app: Application,
    private val fusedLocationProviderClient: FusedLocationProviderClient
): GeneralRepository {
    override suspend fun getMembers(): Resource<List<Member>> {
        return try{
            if(!app.hasInternetConnection())
                return Resource.Error(
                    app.getString(R.string.no_internet_connection)
                )

            val token = prefs.getString("jwt", null)?: return Resource.Error(app.getString(R.string.unauthorized_msg))
            val result = api.getMembers("Bearer $token")
            Resource.Success(result.members)
        }catch (e: Exception){
            Resource.Error(app.getString(R.string.something_went_wrong))
        }
    }

    override suspend fun updateLocation(loc: Location) {
        try{
            val token = prefs.getString("jwt", null)?: return
            api.updateLocation(
                "Bearer $token",
                Location(loc.lat,loc.lng)
            )
        }catch (_: Exception){}
    }

    override suspend fun updateAvatar(path: String) {
        TODO("Not yet implemented")
    }

    override fun getFalloutMapStyleOption(): MapStyleOptions =
        MapStyleOptions.loadRawResourceStyle(app.applicationContext, R.raw.fall_out_map)

    override fun getFusedLocationProviderClient()
    : FusedLocationProviderClient = fusedLocationProviderClient

}