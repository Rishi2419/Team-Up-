package com.example.assigntodo.API

import com.example.assigntodo.Notification
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {

    @Headers(
        "Content-Type:application/json",
        "Authorization:key=AAAAWp8ybEk:APA91bFNRtwAkyiedY43uZQQxNYdO5N7W23OWHuKnZjNUIhu5znnV_H9gClM_5tKrzLTlfmq5NJS2KS-zgfC_PnzkvwB6uD6c74BF8IesCzqQU3HiWaNRpn0aKK7QkbpHS2pLzUsGMXx"


    )

    @POST("fcm/send")
    fun sendNotification(@Body notification: Notification) : retrofit2.Call<Notification>
}

