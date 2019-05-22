package com.github.better.monitorapp

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.util.Log
import com.github.better.monitorapp.extension.getProcessName
import com.github.better.monitorapp.extension.isMainProcess
import com.github.better.monitorapp.extension.showToast
import com.github.better.monitorapp.applifemonitor.MainProcessMonitor
import com.github.better.monitorapp.applifemonitor.OtherProcessMonitor
import com.github.better.monitorapp.net.MantoListenNetState
import com.github.better.monitorapp.receiver.PreReceiver
import com.github.better.monitorapp.service.TestService
import com.github.better.monitorapp.tools.MyExceptionHandler
import java.util.*

/**
 * @author zhaoyu1  2019/5/15
 **/
class App : Application() {

    private val TAG = "bettermonitor"
    private var netState: MantoListenNetState? = null
    private val handler = Handler()

    companion object {
        var instance: Application? = null
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("better", "app onCreate")
        App.instance = this
        initExceptionRecord()
        initMonitor()
        initNetChange()
        initService()
    }

    private fun initMonitor() {
        // 进程不一样，但是包名是相同的
        // Log.e(TAG, "packageName: ${applicationContext.packageName}")

        // 分进程初始化
        if (applicationContext.isMainProcess()) {
            val intent = Intent()
            intent.setClass(this, PreReceiver::class.java)
            sendBroadcast(intent)

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

    /**
     * 记录异常信息
     */
    private fun initExceptionRecord() {
        Thread.setDefaultUncaughtExceptionHandler(MyExceptionHandler(applicationContext))
    }

    private fun initService() {
        instance?.bindService(Intent(instance, TestService::class.java), object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {

            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                Log.e("better", "TestService Connected!!")
            }

        }, Context.BIND_AUTO_CREATE)
    }

    /**
     * 多进程下，每个进程注册一个
     */
    private fun initNetChange() {
        netState = MantoListenNetState()
        netState?.registerReceiver(instance)

        handler.postDelayed({
            netState?.registerReceiver(instance)
        }, Random().nextInt(2000).toLong())
    }
}


// =======================