package io.github.mamedovilkin.finexetf.util

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import java.io.File

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun getCacheSize(cacheDir: File): Long {
    var totalSize = 0L

    fun calculateSize(directory: File) {
        val files = directory.listFiles() ?: return
        for (file in files) {
            if (file.isFile) {
                totalSize += file.length()
            } else if (file.isDirectory) {
                calculateSize(file)
            }
        }
    }

    calculateSize(cacheDir)
    return totalSize
}

fun formatSize(size: Long): String {
    val kb = size / 1024
    val mb = kb / 1024
    return if (mb > 0) "$mb MB" else "$kb KB"
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    return connectivityManager!!.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
}