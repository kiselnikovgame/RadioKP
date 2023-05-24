package com.kiselikovgame.radiokp.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.kiselikovgame.radiokp.R
import io.reactivex.Observable
import java.lang.RuntimeException
import java.net.HttpURLConnection
import java.net.URL

fun createRequest(url: String) = Observable.create<String> {

    val urlConn = URL(url).openConnection() as HttpURLConnection
    try {
        urlConn.connect()
        if (urlConn.responseCode != HttpURLConnection.HTTP_OK)
            it.onError(RuntimeException(urlConn.responseMessage))
        else {
            val str = urlConn.inputStream.bufferedReader().readText()
            it.onNext(str)
        }
    } finally {
        urlConn.disconnect()
    }
}

fun createNotificationChannel(context: Context) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        val id = context.getString(R.string.channel_id)
        val name = context.getString(R.string.channel_name)
        val desc = context.getString(R.string.channel_desc)
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(id, name, importance).apply {
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            description = desc
        }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}