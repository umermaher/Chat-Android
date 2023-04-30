package com.therevotech.revoschat.utils

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.therevotech.revoschat.R


fun String.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches();
}

fun String.isUsernameValid(context: Context): Boolean {
    if(TextUtils.isEmpty(this)){
        context.showToast(context.getString(R.string.fields_empty_msg))
        return false
    }
    if(length<6){
        context.showToast(context.getString(R.string.invalid_username))
        return false
    }
    return true
}

fun String.validatePassword(context: Context): Boolean{
    val value=this
    val pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*+=?-]).{8,15}$"
    var haveNotUpperCase = true
    value.forEach {
        if(it.isUpperCase())
            haveNotUpperCase = false
    }
    return if(haveNotUpperCase){
        context.showToast(context.getString(R.string.cap_in_pwd_msg))
        return false
    }
    else if(!value.matches(pattern.toRegex())){
        context.showToast(context.getString(R.string.pwd_weak_msg))
        false
    }else{
        true
    }
}