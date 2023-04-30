package com.therevotech.revoschat.ui.chat

import android.annotation.SuppressLint
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therevotech.revoschat.data.repositories.chatservice.ChatSocketService
import com.therevotech.revoschat.data.repositories.chatservice.MessageService
import com.therevotech.revoschat.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageService: MessageService,
    private val chatSocketService: ChatSocketService,
    private val savedStateHandler: SavedStateHandle
): ViewModel() {

    private val _messageText = mutableStateOf("")
    val messageText: State<String> = _messageText

    private val _state = mutableStateOf(ChatState())
    val state: State<ChatState> = _state

    private val  _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    fun connectToChat(username: String) {
        getAllMessages()
            viewModelScope.launch {
                val result = chatSocketService.initSession(username)
                when(result){
                    is Resource.Success -> {
                        chatSocketService.observeMessages()
                            .onEach { msg ->
                                val newList = state.value.messages.toMutableList().apply {
                                    add(0, msg)
                                }
                                _state.value = state.value.copy(
                                    messages = newList,
                                )
                            }.launchIn(viewModelScope)
                    }
                    is Resource.Error -> {
                        _toastEvent.emit(result.message ?: "Unknown error")
                    }
                }
        }
    }

    fun onMessageChange(msg:String){
        _messageText.value = msg
    }

    fun disconnect() {
        viewModelScope.launch {
            chatSocketService.closeSession()
        }
    }

    private fun getAllMessages(){
        viewModelScope.launch {
            _state.value = state.value.copy(isLoading = true)
            val result = messageService.getAllMessages()
            _state.value = state.value.copy(
                messages = result.messages,
                isLoading = false
            )
        }
    }

    fun sendMessage(){
        viewModelScope.launch {
            if(_messageText.value.isNotBlank()){
                chatSocketService.sendMessage(_messageText.value)
                delay(1000L)
                _messageText.value = ""
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }

    @SuppressLint("SimpleDateFormat")
    fun getTime(timestamp: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp

        val sdf = SimpleDateFormat("dd/mm/yyyy hh:mm aa")
        return sdf.format(calendar.time)
    }
}