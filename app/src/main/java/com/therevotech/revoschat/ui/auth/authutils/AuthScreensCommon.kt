package com.therevotech.revoschat.ui.auth.authutils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import com.therevotech.revoschat.R
import com.therevotech.revoschat.utils.isUsernameValid
import com.therevotech.revoschat.utils.validatePassword
import java.util.*

@Composable
fun TopIconSegment(){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = colorResource(R.color.theme_color),
                shape = RoundedCornerShape(0.dp, 0.dp, 0.dp, 70.dp)
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            colorFilter = ColorFilter.tint(color = Color.White)
        )
    }
}

@Composable
fun TextInput(
    inputType: InputType,
    value: String,
    onValueChange: (String) -> Unit
){

    TextField(
        value = value,
        onValueChange = { text ->
            onValueChange(text)
        },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(
                imageVector = inputType.icon,
                contentDescription = null,
                tint = colorResource(id = R.color.secondary_text_color)
            )
        },
        label = {
            Text(
                text = inputType.label,
                color = colorResource(id = R.color.secondary_text_color)
            )
        },
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = colorResource(id = R.color.text_field_bg),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        keyboardOptions = inputType.keyboardOptions,
        visualTransformation = inputType.visualTransformation,
    )
}

@Composable
fun LoginSignUpBtn(
    text: String,
    onClick: () -> Unit
){
    Button(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = {
            onClick()
        },
        shape = RoundedCornerShape(10.dp, 10.dp, 10.dp, 10.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.theme_color))
    ) {
        Text(
            text = text.toUpperCase(Locale.ROOT),
            color = Color.White,
            modifier = Modifier
                .padding(vertical = 8.dp),
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun SuggestNavigationButton(
    text: String,
    btnText: String,
    onClick: () -> Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontFamily = FontFamily.SansSerif,
            color = colorResource(id = R.color.secondary_text_color)
        )
        TextButton(onClick = {
            onClick.invoke()
        }) {
            Text(
                text = btnText.uppercase(Locale.ROOT),
                color = colorResource(id = R.color.theme_color),
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun AuthComponentSpacer() = Spacer(modifier = Modifier.height(20.dp))

