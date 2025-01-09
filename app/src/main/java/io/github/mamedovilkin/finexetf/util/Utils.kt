package io.github.mamedovilkin.finexetf.util

import android.content.Context
import android.net.ConnectivityManager
import android.view.View

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    return connectivityManager!!.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
}