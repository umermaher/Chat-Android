package com.therevotech.revoschat.models.message

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val text: String,
    val attachment:String = "",
    val username: String,
    val avatar: String = "",
    val timestamp: Long,
    val id: String
)