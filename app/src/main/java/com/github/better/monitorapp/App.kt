package com.github.better.monitorapp

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.github.better.monitorapp.monitor.MainProcessMonitor
import com.github.better.monitorapp.monitor.OtherProcessMonitor

/**
 * @author zhaoyu1  2019/5/15
 **/
class App : Application() {

    private val TAG = "bettermonitor"

    override fun onCreate() {
        super.onCreate()
        initMonitor()
    }

    private fun initMonitor() {
        // 分进程初始化
        if (applicationContext.isMainProcess()) {
            Log.e("bettermonitor", "main: " + applicationContext.getProcessName())
            MainProcessMonitor.getInstance(this).apply {
                listener = { isForeground ->
                    if (isForeground) {
                        Log.e(TAG, "前台")
                        showToast("app 运行在：foreground")
                    } else {
                        Log.e(TAG, "后台")
                        showToast("app 运行在：background")
                    }
                }
            }
        } else {    // 其他进程
            Log.e(TAG, "other: " + applicationContext.getProcessName())
            OtherProcessMonitor.getInstance(this)
        }
    }
}


// =======================

// 获取进程名
fun Context.getProcessName(): String? {
    val pid = android.os.Process.myPid()
    val activityManager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    return activityManager.runningAppProcesses.firstOrNull { it.pid == pid }?.processName ?: null
}

// 是否主进程
fun Context.isMainProcess(): Boolean {
    return "com.github.better.monitorapp" == this.getProcessName()
}

fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}