package com.therevotech.revoschat.models.message

@kotlinx.serialization.Serializable
data class MessagesResponse(
    val messages:List<Message>
)