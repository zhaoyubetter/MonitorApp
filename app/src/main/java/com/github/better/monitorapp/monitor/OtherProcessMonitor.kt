package com.github.better.monitorapp.monitor

import android.app.Activity
import android.app.Application
import android.content.Intent

// === 其他进程监听
internal class OtherProcessMonitor private constructor() : ActivityLifeCycleMonitors() {

    companion object {
        fun getInstance(app: Application): OtherProcessMonitor {
            return lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
                OtherProcessMonitor()
            }.value.apply {
                app.registerActivityLifecycleCallbacks(this)
            }
        }
    }

    override fun onActivityResumed(p0: Activity?) {
        p0?.apply {
            sendBroadcast(Intent(MainProcessMonitor.ACTION_BROAD_CAST_MONITOR).apply {
                putExtra(MainProcessMonitor.CALLBACK_TAG, MainProcessMonitor.RESUME)
                putExtra(MainProcessMonitor.STR_FOREGROUND, false)
                putExtra(MainProcessMonitor.STR_PAUSE, false)
            })
        }
    }

    override fun onActivityPaused(p0: Activity?) {
        p0?.apply {
            sendBroadcast(Intent(MainProcessMonitor.ACTION_BROAD_CAST_MONITOR).apply {
                putExtra(MainProcessMonitor.CALLBACK_TAG, MainProcessMonitor.PAUSE)
                putExtra(MainProcessMonitor.STR_FOREGROUND, true)
                putExtra(MainProcessMonitor.STR_PAUSE, true)
            })
        }
    }


    override fun onActivityStopped(p0: Activity?) {
        p0?.apply {
            sendBroadcast(Intent(MainProcessMonitor.ACTION_BROAD_CAST_MONITOR).apply {
                putExtra(MainProcessMonitor.CALLBACK_TAG, MainProcessMonitor.STOP)
                putExtra(MainProcessMonitor.STR_FOREGROUND, true)
                putExtra(MainProcessMonitor.STR_PAUSE, true)
            })
        }
    }
}