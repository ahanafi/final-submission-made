package com.ahanafi.id.myfavoritemovieapp.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.ahanafi.id.myfavoritemovieapp.R

fun setNotification(
    context: Context, title: String?, message: String?,
    channelId: Int, ChannelName: String?
) {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val arrayLongVibration = longArrayOf(5000, 5000, 5000, 5000, 5000)
    val notificationBuilder =  NotificationCompat.Builder(context, channelId.toString())
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle(title)
        .setContentText(message)
        .setAutoCancel(true)
        .setColor(ContextCompat.getColor(context, android.R.color.transparent))
        .setVibrate(arrayLongVibration)
        .setSound(alarmSound)

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
        val channel = NotificationChannel(
            channelId.toString(), ChannelName, NotificationManager.IMPORTANCE_DEFAULT
        )
        with(channel){
            enableVibration(true)
            setShowBadge(true)
            canShowBadge()
            vibrationPattern = arrayLongVibration
            lockscreenVisibility = 1
        }


        notificationBuilder.setChannelId(channelId.toString())
        notificationManager.createNotificationChannel(channel)
    }

    val notification = notificationBuilder.build()
    notificationManager.notify(channelId, notification)

    Log.d("INFO -> CHANNEL $ChannelName", "EXECUTED!")
    Log.d("INFO -> CHANNEL ID : ", channelId.toString())
}