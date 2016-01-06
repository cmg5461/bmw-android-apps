package com.cmg5461.jbPro.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.cmg5461.jbPro.data.Constants;
import com.cmg5461.jbPro.log.JB4SettingPoint;
import com.cmg5461.jbPro.log.LogPoint;
import com.cmg5461.jbPro.providers.JB4Connection;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JB4ConnectionService extends Service {
    private final IBinder mBinder = new MyBinder();
    private final ExecutorService exs = Executors.newFixedThreadPool(1);
    private JB4Connection jb4Connection;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Constants.TAG, "SERVICE onCreate");
        jb4Connection = JB4Connection.getSingleton();
        jb4Connection.setService(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Constants.TAG, "SERVICE onDestroy");
        disconnect();
    }

    public void disconnect() {
        Log.d(Constants.TAG, "jb4 disconnected from service");
        exs.submit(new Runnable() {
            @Override
            public void run() {
                jb4Connection.Disconnect();
            }
        });
    }

    public void connect() {
        Log.d(Constants.TAG, "jb4 connected from service");
        exs.submit(new Runnable() {
            @Override
            public void run() {
                jb4Connection.Connect();
            }
        });
    }

    public void logSplit() {
        Log.d(Constants.TAG, "jb4 log split");
        exs.submit(new Runnable() {
            @Override
            public void run() {
                jb4Connection.logSplit();
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Constants.TAG, "service start command received");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(Constants.TAG, "service bound");
        return mBinder;
    }

    public boolean isConnected() {
        return jb4Connection.isLogging();
    }

    public LogPoint getPoint() {
        return jb4Connection.getLogPoint();
    }

    public JB4SettingPoint getSettingPoint() {
        return jb4Connection.getSettingPoint();
    }

    public int getLogIndex() {
        return jb4Connection.getLogIndex();
    }

    public class MyBinder extends Binder {
        public JB4ConnectionService getService() {
            return JB4ConnectionService.this;
        }
    }
}