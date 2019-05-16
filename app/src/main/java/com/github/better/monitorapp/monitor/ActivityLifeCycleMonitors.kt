package com.github.better.monitorapp.monitor

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import java.util.concurrent.atomic.AtomicInteger

/**
 * 參考了網絡的寫法，具体地址没找到了。O(∩_∩)O~，找到了再補充
 * @author zhaoyu1  2019/5/15
 **/
internal class MainProcessMonitor private constructor() : ActivityLifeCycleMonitors() {

    private val TAG = "bettermonitor"

    // 暂停的activity个数
    private val pausedCount = AtomicInteger(0)
    // 监听
    var listener: (isForeground: Boolean) -> Unit = {}
    private var run: Runnable? = null
    private val handler = Handler()

    private var foreground = true
    private var paused = true

    companion object {
        const val ACTION_BROAD_CAST_MONITOR = "action_com.github.better.monitorapp.monitor"
        const val CALLBACK_TAG = "callBackTag"
        const val PAUSE = 1
        const val RESUME = 2
        const val STOP = 3

        const val STR_PAUSE = "pause"
        const val STR_FOREGROUND = "foreground"


        fun getInstance(app: Application): MainProcessMonitor {
            return lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
                MainProcessMonitor()
            }.value.apply {
                app.registerActivityLifecycleCallbacks(this)
                registerBroadCast(app)
            }
        }
    }

    // 广播用于跨进程
    private fun registerBroadCast(app: Application) {
        app.applicationContext.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                paused = intent.getBooleanExtra(STR_PAUSE, true)
                // foreground 需要稍后赋值
                val tmpForeground = intent.getBooleanExtra(STR_FOREGROUND, true)
                val callbackTag = intent.getIntExtra(CALLBACK_TAG, PAUSE)
                when (callbackTag) {
                    PAUSE -> pause(tmpForeground)
                    RESUME -> resume(tmpForeground)
                    STOP -> stop(tmpForeground)
                }
            }

        }, IntentFilter(ACTION_BROAD_CAST_MONITOR))
    }

    private inline fun pause(tmpForeground: Boolean) {
        pausedCount.addAndGet(1)


        // 之前有，先移調
        run?.let {
            handler.removeCallbacks(it)
        }

        foreground = tmpForeground

        // 重新提交
        val run = Runnable {
            if (foreground && paused) {
                listener.invoke(false)
            }
        }

        handler.postDelayed(run, 1000)
    }

    private inline fun resume(tmpForeground: Boolean) {
        val count = pausedCount.get()
        // 減1
        if (count > 0) {
            pausedCount.decrementAndGet()
        }

        val wasBackground = !tmpForeground
        foreground = tmpForeground

        // 移除run
        run?.let {
            handler.removeCallbacks(it)
        }

        if (wasBackground && count == 0) {
            listener.invoke(true)
        }
    }

    private inline fun stop(tmpForeground: Boolean) {
        if (pausedCount.get() > 0) {
            pausedCount.decrementAndGet()
        }
    }

    ///////////////////////////////
    override fun onActivityResumed(p0: Activity?) {
        p0?.apply {
            sendBroadcast(Intent(ACTION_BROAD_CAST_MONITOR).apply {
                putExtra(CALLBACK_TAG, RESUME)
                putExtra(STR_FOREGROUND, false)
                putExtra(STR_PAUSE, false)
            })
        }
    }

    override fun onActivityPaused(p0: Activity?) {
        p0?.apply {
            sendBroadcast(Intent(ACTION_BROAD_CAST_MONITOR).apply {
                putExtra(CALLBACK_TAG, PAUSE)
                putExtra(STR_FOREGROUND, true)
                putExtra(STR_PAUSE, true)
            })
        }
    }


    override fun onActivityStopped(p0: Activity?) {
        p0?.apply {
            sendBroadcast(Intent(ACTION_BROAD_CAST_MONITOR).apply {
                putExtra(CALLBACK_TAG, STOP)
                putExtra(STR_FOREGROUND, true)
                putExtra(STR_PAUSE, true)
            })
        }
    }
}


///////////////////////////////////////////////////////////////////////////////
open class ActivityLifeCycleMonitors : Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(p0: Activity?) {
    }

    override fun onActivityResumed(p0: Activity?) {
    }

    override fun onActivityStarted(p0: Activity?) {
    }

    override fun onActivityDestroyed(p0: Activity?) {
    }

    override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {
    }

    override fun onActivityStopped(p0: Activity?) {
    }

    override fun onActivityCreated(p0: Activity?, p1: Bundle?) {
    }
}


