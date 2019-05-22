package com.github.better.monitorapp.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log

/**
 * @author zhaoyu1  2019/5/17
 **/
class TestService : Service() {

    private val messagerHandler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
        }
    }

    private val message = Messenger(messagerHandler)

    override fun onCreate() {
        super.onCreate()
        Log.e("better", "TestService onCreate()...")
    }

    override fun onBind(intent: Intent?): IBinder {
        return message.binder
    }
}