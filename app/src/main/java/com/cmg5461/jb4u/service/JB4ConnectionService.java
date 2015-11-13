package com.cmg5461.jb4u.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.cmg5461.jb4u.data.Constants;
import com.cmg5461.jb4u.providers.JB4Connection;

public class JB4ConnectionService extends Service {
    private JB4Connection jb4Connection;
    private final IBinder mBinder = new MyBinder();

    @Override
    public void onCreate() {
        Log.d(Constants.TAG, "service created");
        if (jb4Connection == null) {
            jb4Connection = JB4Connection.getSingleton();
            jb4Connection.setService(this);
            Log.d(Constants.TAG, "jb4connection initialized");
        }
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d(Constants.TAG, "service destroy");
        if (jb4Connection != null) {
            jb4Connection.Disconnect();
            jb4Connection = null;
        }
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Constants.TAG, "service started");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(Constants.TAG, "service bound");
        return mBinder;
    }

    public JB4Connection getJb4Connection() {
        return jb4Connection;
    }

    public class MyBinder extends Binder {
        public JB4ConnectionService getService() {
            return JB4ConnectionService.this;
        }
    }
}