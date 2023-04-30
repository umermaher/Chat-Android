package com.therevotech.revoschat.apis

import com.therevotech.revoschat.models.auth.AuthRequest
import com.therevotech.revoschat.models.auth.TokenResponse
import com.therevotech.revoschat.models.members.GetMembersResponse
import com.therevotech.revoschat.models.members.Location
import com.therevotech.revoschat.utils.AUTHENTICATE
import com.therevotech.revoschat.utils.LOCATION
import com.therevotech.revoschat.utils.MEMBERS
import com.therevotech.revoschat.utils.SIGN_IN
import com.therevotech.revoschat.utils.SIGN_UP
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface RcApi {

    @POST(SIGN_UP)
    suspend fun signUp(
        @Body request: AuthRequest
    )

    @POST(SIGN_IN)
    suspend fun signIn(
        @Body request: AuthRequest
    ): TokenResponse

    @GET(AUTHENTICATE)
    suspend fun authenticate(
        @Header("Authorization") token: String
    )

    @GET(MEMBERS)
    suspend fun getMembers(
        @Header("Authorization") token: String
    ): GetMembersResponse

    @PUT(LOCATION)
    suspend fun updateLocation(
        @Header("Authorization") token: String,
        @Body request: Location
    )
}