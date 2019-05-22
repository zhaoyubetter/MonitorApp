package com.github.better.monitorapp.tools;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.github.better.monitorapp.extension.ContextsKt;
import com.github.better.monitorapp.extension.FilesKt;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 用于捕获未处理的异常信息
 * TODO: 跨进程，读写有并发问题
 *
 * @author zhaoyu1
 */
public class MyExceptionHandler implements
        java.lang.Thread.UncaughtExceptionHandler {
    private static final String TAG = "exceptionHandler";
    private Context context;

    public MyExceptionHandler(Context context) {
        this.context = context;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        final StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        Log.e(TAG, stackTrace.toString());
        saveErrorLog(stackTrace.toString());
    }

    private void saveErrorLog(String stackInfo) {
        StringBuilder reportText = new StringBuilder();
        reportText.append("Model:").append(Build.MODEL).append("\n");
        reportText.append("Device:").append(Build.DEVICE).append("\n");
        reportText.append("Product:").append(Build.PRODUCT).append("\n");
        reportText.append("Manufacturer:").append(Build.MANUFACTURER).append("\n");
        // 系统版本
        reportText.append("Version:").append(Build.VERSION.RELEASE).append("\n");
        reportText.append("ProcessName:").append(ContextsKt.getProcessName(context)).append("\n");
        reportText.append(stackInfo).append("\n");
        FilesKt.appendContext(new File(context.getExternalFilesDir(null), "bugs.txt"), reportText.toString());
    }
}
