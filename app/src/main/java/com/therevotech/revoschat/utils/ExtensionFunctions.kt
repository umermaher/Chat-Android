package com.therevotech.revoschat.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.ContactsContract
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showMessageDialog(title: String, message: String, listener: () -> Unit = {}) {
    MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("ok"){ dialog, _ ->
            dialog.dismiss()
            listener()
        }
        .show()
}

fun Context.showConfirmDialog(title: String, message: String, listener: () -> Unit = {}) {
    MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("yes"){ dialog, _ ->
            dialog.dismiss()
            listener()
        }
        .setNegativeButton("no"){ dialog, _ ->
            dialog.dismiss()
        }
        .show()
}

fun Application.hasInternetConnection():Boolean{
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
        val activeNetwork= connectivityManager.activeNetwork ?: return false
        val networkCapabilities=connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when{
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }else{
        connectivityManager.activeNetworkInfo?.run {
            return when(type){
                ConnectivityManager.TYPE_WIFI -> true
                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> true
                ConnectivityManager.TYPE_ETHERNET -> true
                else -> true
            }
        }
    }
    return false
}