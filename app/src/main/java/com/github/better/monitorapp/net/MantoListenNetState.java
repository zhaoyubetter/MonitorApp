package com.github.better.monitorapp.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import com.github.better.monitorapp.extension.ContextsKt;


public final class MantoListenNetState {
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            doNetwork(context);
        }
    };

    private void doNetwork(Context context) {
        Log.e("better ==> 网络改变", ContextsKt.getProcessName(context));
    }

    public void registerReceiver(Context context) {
        final IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(mReceiver, mFilter);
    }

    public void unRegisterReceiver(Context context) {
        try {
            context.unregisterReceiver(mReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}