package com.cmg5461.jb4u.ui.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cmg5461.jb4u.R;
import com.cmg5461.jb4u.data.Constants;
import com.cmg5461.jb4u.service.JB4ConnectionService;
import com.cmg5461.jb4u.ui.views.GaugeView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class JB4UActivity extends Activity {

    private JB4ConnectionService myService;
    public ServiceConnection myConnection;

    //private TextView console;
    private ImageButton connectButton;
    private TextView rpm;
    private TextView boost;

    //private ScrollView scrollView;

    private StringBuilder consoleText;
    private int consoleLines = 0;

    private boolean connected = false;

    private final ScheduledExecutorService updateScheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture updateFuture;

    private int loopDelay = 100;

    //private StringBuilder sbText = new StringBuilder();
    //private Formatter sbFormat = new Formatter(sbText, Locale.US);

    private GaugeView rpm_gauge;

    private NotificationManager mNM;
    private final int notificationID = 8675309;
    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI();
                    }
                });
            } catch (Throwable t) {
                Log.e(Constants.TAG, "updateRunnable ERROR", t);
            }
        }
    };

    @Override
    protected void onDestroy() {
        Log.d(Constants.TAG, "ACTIVITY onDestroy");
        if (myService != null) {
            myService.disconnect();
        }
        stopService();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Constants.TAG, "ACTIVITY onCreate");
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        showNotification();
        // connection
        myConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder binder) {
                myService = ((JB4ConnectionService.MyBinder) binder).getService();
                Log.d(Constants.TAG, "service connected");
            }

            public void onServiceDisconnected(ComponentName className) {
                myService = null;
                Log.d(Constants.TAG, "service disconnected");
            }
        };
        startService();
        setContentView(R.layout.activity_home);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initializeComponents();
    }

    private void startService() {
        JB4UActivity.this.startService(new Intent(JB4UActivity.this, JB4ConnectionService.class));
    }

    private void stopService() {
        JB4UActivity.this.stopService(new Intent(JB4UActivity.this, JB4ConnectionService.class));
    }

    @Override
    protected void onStart() {
        Log.d(Constants.TAG, "ACTIVITY onStart");
        doBindService();
        if (connected && updateFuture != null && updateFuture.isCancelled()) startUpdater();
        super.onStart();
    }

    private void startUpdater() {
        stopUpdater();
        updateFuture = updateScheduler.scheduleAtFixedRate(updateRunnable, 0L, loopDelay, TimeUnit.MILLISECONDS);
    }

    @Override
    protected void onStop() {
        Log.d(Constants.TAG, "ACTIVITY onStop");
        if (updateFuture != null && !updateFuture.isCancelled()) updateFuture.cancel(true);
        if (myConnection != null) unbindService(myConnection);
        super.onStop();
    }

    private void initializeComponents() {
        boost = (TextView) findViewById(R.id.textBoost);
        rpm = (TextView) findViewById(R.id.textRPM);
        rpm_gauge = (GaugeView) findViewById(R.id.gaugeRPM);

        //scrollView = (ScrollView) findViewById(R.id.scrollView);
        //console = (TextView) findViewById(R.id.console);
        consoleText = new StringBuilder("init");
        //console.setText(consoleText.toString());
        //console.setEnabled(false);
        //console.setTextColor(Color.BLACK);
        consoleLines++;

        connectButton = (ImageButton) findViewById(R.id.imageButton_toggleConnect);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddConsoleLine("service start click");
                if (!connected) {
                    if (myService != null) {
                        connected = true;
                        myService.connect();
                        startUpdater();
                        connectButton.setBackgroundResource(R.drawable.disconnect);
                        AddConsoleLine("service started");
                    }
                } else {
                    if (myService != null) {
                        connected = false;
                        myService.disconnect();
                        stopUpdater();
                        connectButton.setBackgroundResource(R.drawable.connect);
                        AddConsoleLine("service stahpped");
                    }
                }
            }
        });
        AddConsoleLine("init finished");
    }

    public void stopUpdater() {
        if (updateFuture != null && !updateFuture.isCancelled())
            updateFuture.cancel(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_jb4_buddy, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) return true;

        return super.onOptionsItemSelected(item);
    }

    public void AddConsoleLine(String text) {
        consoleText.append('\n');
        consoleText.append(text);
        consoleLines++;

        if (consoleLines > 50) {
            consoleText.replace(0, consoleText.indexOf("\n") + 1, "");
            consoleLines--;
        }
        Log.d(Constants.TAG, "Console: " + text);
        //console.setText(consoleText.toString());

        //scrollView = (ScrollView) findViewById(R.id.scrollView);
        //if (scrollView != null) {
        //    scrollView.postDelayed(new Runnable() {
        //        @Override
        //        public void run() {
        //            scrollView.fullScroll(View.FOCUS_DOWN);
        //        }
        //    }, 25);
        //}
    }

    @Override
    protected void onResume() {
        Log.d(Constants.TAG, "ACTIVITY onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(Constants.TAG, "ACTIVITY onPause");
        super.onPause();
    }

    public void doBindService() {
        bindService(new Intent(JB4UActivity.this, JB4ConnectionService.class), myConnection, Context.BIND_AUTO_CREATE);
    }

    private void updateUI() {
        if (connected) {
            //sbFormat.format("%15.2f Psi", myService.getPoint().boost);
            boost.setText(String.format("%15.2f PSI", myService.getPoint().boost));
            //sbText.setLength(0);
            //sbFormat.format("%15d rpm", myService.getPoint().rpm);
            rpm.setText(String.format("%15s RPM", myService.getPoint().rpm));
            rpm_gauge.setTargetValue(myService.getPoint().rpm);
            //sbText.setLength(0);
        }
    }

    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = "Running!";

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, JB4UActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        Notification.Builder nb = new Notification.Builder(this)
                .setSmallIcon(R.drawable.notification_template_icon_bg)  // the status icon
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle("JB4U")  // the label of the entry
                .setContentText(text)  // the contents of the entry
                .setContentIntent(contentIntent); // The intent to send when the entry is clicked

        // Send the notification.
        Notification notification;
        if (Build.VERSION.SDK_INT < 16) {
            notification = nb.getNotification();
            notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
            //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        } else {
            notification = nb.build();
            notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
        }
        mNM.notify(notificationID, notification);
    }

    @Override
    public void onBackPressed() {
        if (connected) moveTaskToBack(true);
        else {
            super.onBackPressed();
            mNM.cancel(notificationID);
        }
    }


}
