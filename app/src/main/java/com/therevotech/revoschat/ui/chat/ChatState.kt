package com.therevotech.revoschat.ui.chat

import com.therevotech.revoschat.models.message.Message

data class ChatState (
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean = false
)