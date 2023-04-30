package com.therevotech.revoschat.utils

object SecretKeys {
    init {
        System.loadLibrary("revoschat")
    }
    val keys:Keys = RevosKeys()
    val mapsKey: String = keys.getMapsApiKey()
}

interface Keys{
    fun getMapsApiKey(): String
    fun encrypt(text: CharSequence): String
}

class RevosKeys: Keys {
    external override fun getMapsApiKey(): String
    external override fun encrypt(text: CharSequence): String
}