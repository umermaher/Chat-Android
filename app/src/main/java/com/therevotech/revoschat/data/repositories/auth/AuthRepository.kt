package com.therevotech.revoschat.data.repositories.auth

import com.therevotech.revoschat.models.auth.AuthResult

interface AuthRepository {
    suspend fun signUp(username:String, password: String): AuthResult<Unit>
    suspend fun signIn(username:String, password: String): AuthResult<Unit>
    suspend fun authenticate(): AuthResult<Unit>
    suspend fun isLoggedIn(): Boolean
    fun logout()
    fun getUsername(): String
}