package com.therevotech.revoschat.data.repositories.chatservice

import android.content.SharedPreferences
import android.util.Log
import com.therevotech.revoschat.models.message.Message
import com.therevotech.revoschat.utils.Resource
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.HttpMethod
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString

class ChatSocketServiceImpl(
    private val client:HttpClient,
    private val prefs: SharedPreferences
): ChatSocketService {

    private var socket: WebSocketSession?=null

    override suspend fun initSession(username: String): Resource<Unit> {
        return try {
            socket = client.webSocketSession {
                url("${ChatSocketService.EndPoints.ChatSocket.url}?username=$username")
                header("Authorization","Bearer ${prefs.getString("jwt","")}")
            }
            if(socket?.isActive == true) {
                Resource.Success(Unit)
            } else Resource.Error("Couldn't establish a connection.")
        } catch(e: Exception) {
            e.printStackTrace()
            Resource.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    override suspend fun sendMessage(message: String) {
        try {
            socket?.send(Frame.Text(message))
        }catch (e: Exception){
            Log.e("socket error",e.message ?: "Unknown Error!")
        }
    }

    override suspend fun observeMessages(): Flow<Message> {
        return try {
            socket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as? Frame.Text)?.readText()?:""
                    val message = kotlinx.serialization.json.Json.decodeFromString<Message>(json)
                    message
                }?: flow {  }
        }catch (e: Exception){
            Log.e("socket error",e.message ?: "Unknown Error!")
            flow {  }
        }
    }

    override suspend fun closeSession() {
        socket?.close()
    }
}