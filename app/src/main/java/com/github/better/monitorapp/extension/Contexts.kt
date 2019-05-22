package com.github.better.monitorapp.extension

import android.app.ActivityManager
import android.content.Context
import android.widget.Toast

/**
 * @author zhaoyu1  2019/5/16
 **/
// 获取进程名
fun Context.getProcessName(): String? {
    val pid = android.os.Process.myPid()
    val activityManager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    return activityManager.runningAppProcesses.firstOrNull { it.pid == pid }?.processName ?: null
}

// 是否主进程
fun Context.isMainProcess(): Boolean {
    return this.packageName == this.getProcessName()
}

fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}