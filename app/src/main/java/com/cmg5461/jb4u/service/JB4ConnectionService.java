package com.cmg5461.jb4u.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.cmg5461.jb4u.data.Constants;
import com.cmg5461.jb4u.log.DetailLogPoint;
import com.cmg5461.jb4u.providers.JB4Connection;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JB4ConnectionService extends Service {
    private JB4Connection jb4Connection;
    private final IBinder mBinder = new MyBinder();

    private final ExecutorService exs = Executors.newFixedThreadPool(1);

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

    public class MyBinder extends Binder {
        public JB4ConnectionService getService() {
            return JB4ConnectionService.this;
        }
    }

    public DetailLogPoint getPoint() {
        return jb4Connection.getLogPoint();
    }
}