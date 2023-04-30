package com.therevotech.revoschat.ui.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.therevotech.revoschat.R
import com.therevotech.revoschat.utils.SecretKeys
import com.therevotech.revoschat.utils.showToast
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChatScreen(
    navController: NavController,
    username: String,
    viewModel: ChatViewModel = hiltViewModel(),
){
    val context = LocalContext.current
    LaunchedEffect(key1 = true ){
        viewModel.toastEvent.collectLatest{ msg ->
            context.showToast(msg)
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner){
        val observer = LifecycleEventObserver{_, event ->
            if(event == Lifecycle.Event.ON_START){
                viewModel.connectToChat(username)
            }
            else if(event == Lifecycle.Event.ON_STOP){
                viewModel.disconnect()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val state = viewModel.state.value

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row (
            modifier = Modifier.fillMaxWidth()
        ) {

            Image(
                modifier = Modifier.size(45.dp),
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = null,
                colorFilter = ColorFilter.tint(color = Color.DarkGray),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier =
                Modifier
                    .weight(1f)
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center,
            ){
                Text(
                    text = stringResource(id = R.string.chat),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.avenirltstd_roman))
                )
            }
            IconButton(
                onClick = {
                    navController.navigate("users_screen")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "List",
                    tint = Color.DarkGray
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            reverseLayout = true
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
            items(state.messages) { msg ->

                val isOwnMessage = msg.username == username
                Box(
                    contentAlignment = if (isOwnMessage) {
                        Alignment.CenterEnd
                    } else Alignment.CenterStart,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .width(230.dp)
                            .background(
                                color = if (isOwnMessage) colorResource(id = R.color.theme_color) else Color.DarkGray,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(10.dp)
                    ) {
                        Text(
                            text = msg.username,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = msg.text,
                            color = Color.White
                        )
                        Text(
                            text = viewModel.getTime(msg.timestamp),
                            fontSize = 12.sp,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
        ){
            TextField(
                value = viewModel.messageText.value,
                onValueChange = viewModel::onMessageChange,
                placeholder = {
                    Text(text = stringResource(id = R.string.message), color = Color.Gray)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = colorResource(id = R.color.text_field_bg),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
            IconButton(onClick = viewModel::sendMessage) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = colorResource(id = R.color.theme_color)
                )
            }
        }
    }
}