package com.therevotech.revoschat.models.auth

sealed class AuthResult<T>(val data:T ?= null, val message:String?=null){
    class Authorized<T>(data:T?=null): AuthResult<T>(data)
    class UnAuthorized<T>: AuthResult<T>()
    class UnKnownError<T>(message:String): AuthResult<T>(message = message)
    class NetworkError<T>(message:String): AuthResult<T>(message = message)
}
