package com.example.assigntodo.API

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.assigntodo.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class NotificationService : FirebaseMessagingService(){

    val chanelID = "Assigntodo"

    override fun onMessageReceived(message: RemoteMessage) {

        val manager = getSystemService(NOTIFICATION_SERVICE)
        createNotificationChannel(manager as NotificationManager)

        val notification = NotificationCompat.Builder(this,chanelID)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["body"])
            .setSmallIcon(R.drawable.img_1)
            .setAutoCancel(false)
            .setContentIntent(null)
            .build()

        manager.notify(Random.nextInt(),notification)

    }


    private fun createNotificationChannel(manager: NotificationManager) {

        val channel = NotificationChannel(chanelID , "assigntodo",NotificationManager.IMPORTANCE_HIGH )
            .apply {
                description = "New Todo"
                enableLights(true)
            }

        manager.createNotificationChannel(channel)
    }


}



