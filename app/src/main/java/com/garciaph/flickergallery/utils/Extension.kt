package com.garciaph.flickergallery.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun Application.hasNetworkConnection(): Boolean =
    with(getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager) {
        val activeNetwork: NetworkInfo? = activeNetworkInfo
        activeNetwork != null && activeNetwork.isConnected
    }