package com.therevotech.revoschat.data.repositories.auth

import android.app.Application
import android.content.SharedPreferences
import androidx.core.content.edit
import com.therevotech.revoschat.R
import com.therevotech.revoschat.apis.RcApi
import com.therevotech.revoschat.models.auth.AuthRequest
import com.therevotech.revoschat.models.auth.AuthResult
import com.therevotech.revoschat.utils.IS_LOGGED_IN
import com.therevotech.revoschat.utils.USERNAME
import com.therevotech.revoschat.utils.hasInternetConnection
import retrofit2.HttpException

class AuthRepositoryImpl(
    private val application: Application,
    private val api:RcApi,
    private val prefs: SharedPreferences
): AuthRepository {
    override suspend fun signUp(username: String, password: String): AuthResult<Unit> {
        if(!application.hasInternetConnection())
            return AuthResult.NetworkError(
                application.getString(R.string.no_internet_connection)
            )
        return try{
            api.signUp(
                request = AuthRequest(
                    username = username,
                    password = password
                )
            )
            signIn(username, password)
        }catch (e: HttpException){
            if(e.code() == 401){
                AuthResult.UnAuthorized()
            }else if(e.code() == 422){
                AuthResult.UnKnownError(application.getString(R.string.user_with_same_name_msg))
            }else
                AuthResult.UnKnownError(application.getString(R.string.something_went_wrong))
        }catch (e: Exception){
            AuthResult.UnKnownError(application.getString(R.string.something_went_wrong))
        }
    }

    override suspend fun signIn(username: String, password: String): AuthResult<Unit> {
        if(!application.hasInternetConnection())
            return AuthResult.NetworkError(
                application.getString(R.string.no_internet_connection)
            )
        return try{
            val response = api.signIn(
                request = AuthRequest(
                    username = username,
                    password = password
                )
            )
            prefs.edit {
                putString("jwt",response.token)
                putString(USERNAME,username)
                putBoolean(IS_LOGGED_IN,true)
            }
            AuthResult.Authorized()
        }catch (e: HttpException){
            if(e.code() == 401){
                AuthResult.UnAuthorized()
            }else
                AuthResult.UnKnownError(application.getString(R.string.something_went_wrong))
        }catch (e: Exception){
            AuthResult.UnKnownError(application.getString(R.string.something_went_wrong))
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        if(!application.hasInternetConnection())
            return AuthResult.NetworkError(
                application.getString(R.string.no_internet_connection)
            )
        return try{
            val token = prefs.getString("jwt",null) ?: return AuthResult.UnAuthorized()
            api.authenticate("Bearer $token")
            AuthResult.Authorized()
        }catch (e: HttpException){
            if(e.code() == 401){
                AuthResult.UnAuthorized()
            } else
                AuthResult.UnKnownError(application.getString(R.string.something_went_wrong))
        }catch (e: Exception){
            AuthResult.UnKnownError(application.getString(R.string.something_went_wrong))
        }
    }

    override suspend fun isLoggedIn() = prefs.getBoolean(IS_LOGGED_IN,false)

    override fun logout() = prefs.edit {
        clear()
    }

    override fun getUsername(): String = prefs.getString(USERNAME,"user")!!
}