package com.github.better.monitorapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * @author zhaoyu1  2019/5/16
 **/
class PreReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.w("better", "onReceive: " + intent?.extras)
    }

}