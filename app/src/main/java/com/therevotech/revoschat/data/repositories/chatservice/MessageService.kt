package com.therevotech.revoschat.data.repositories.chatservice

import com.therevotech.revoschat.models.message.MessagesResponse
import com.therevotech.revoschat.utils.BASE_URL
import com.therevotech.revoschat.utils.MESSAGES

interface MessageService {
    suspend fun getAllMessages(): MessagesResponse

    sealed class EndPoints(val url: String){
        object GetAllMessages: EndPoints(BASE_URL + MESSAGES)
    }
}