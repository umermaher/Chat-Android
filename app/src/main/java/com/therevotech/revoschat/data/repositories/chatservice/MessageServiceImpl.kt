package com.therevotech.revoschat.data.repositories.chatservice

import android.content.SharedPreferences
import com.therevotech.revoschat.models.message.Message
import com.therevotech.revoschat.models.message.MessagesResponse
import io.ktor.client.*
import io.ktor.client.request.*

class MessageServiceImpl(
    private val client: HttpClient,
    private val prefs: SharedPreferences
): MessageService {
    override suspend fun getAllMessages(): MessagesResponse {
        return try {
            client.get<MessagesResponse>(MessageService.EndPoints.GetAllMessages.url){
                header("Authorization","Bearer ${prefs.getString("jwt","")}")
            }
        }catch (e: Exception){
            MessagesResponse(emptyList<Message>())
        }
    }
}