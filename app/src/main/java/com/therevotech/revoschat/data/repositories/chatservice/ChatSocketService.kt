package com.therevotech.revoschat.data.repositories.chatservice

import com.therevotech.revoschat.models.message.Message
import com.therevotech.revoschat.utils.CHAT_SOCKET
import com.therevotech.revoschat.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ChatSocketService {

    suspend fun initSession(username:String): Resource<Unit>
    suspend fun sendMessage(message:String)
    suspend fun observeMessages(): Flow<Message>
    suspend fun closeSession()

    companion object{
        const val BASE_URL = "ws://192.168.1.63:8080/"
    }
    sealed class EndPoints(val url: String){
        object ChatSocket: EndPoints(BASE_URL + CHAT_SOCKET)
    }
}