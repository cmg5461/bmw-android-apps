package com.cmg5461.jb4u.ui.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cmg5461.jb4u.R;
import com.cmg5461.jb4u.data.Constants;
import com.cmg5461.jb4u.log.JB4SettingPoint;
import com.cmg5461.jb4u.log.LogPoint;
import com.cmg5461.jb4u.service.JB4ConnectionService;
import com.cmg5461.jb4u.ui.views.GraphView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class JB4UActivity extends Activity {

    private JB4ConnectionService myService;
    public ServiceConnection myConnection;

    private LogPoint displayDLPoint = new LogPoint();
    private JB4SettingPoint displaySettingPoint = new JB4SettingPoint();
    //private TextView console;
    private ImageButton connectButton;
    private Button btnLogSplit;
    private TextView rpm;
    private TextView boost;
    private TextView target;
    private TextView iat;
    private TextView afr;
    private TextView afr2;
    private TextView trims;
    private TextView fp_l;
    private TextView fp_h;
    private TextView ign_1;
    private TextView ign_2;
    private TextView ign_3;
    private TextView ign_4;
    private TextView ign_5;
    private TextView ign_6;
    private TextView waterTemp;
    private TextView oilTemp;

    private TextView ff;
    private TextView fol;
    private TextView avg_ign;
    private TextView map;
    private TextView logLen;

    private GraphView graph_ign;

    //private ScrollView scrollView;

    private final ScheduledExecutorService updateScheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture updateFuture;

    private int loopDelay = 100;
    private int logIndex = 0;

    //private StringBuilder sbText = new StringBuilder();
    //private Formatter sbFormat = new Formatter(sbText, Locale.US);

    //private GaugeView rpm_gauge;

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
    private boolean buttonConnected = false;

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
        if (myService != null && myService.isConnected() && updateFuture != null && updateFuture.isCancelled())
            startUpdater();
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
        //rpm_gauge = (GaugeView) findViewById(R.id.gaugeRPM);
        target = (TextView) findViewById(R.id.textTarget);
        iat = (TextView) findViewById(R.id.textIAT);
        afr = (TextView) findViewById(R.id.textAFR1);
        afr2 = (TextView) findViewById(R.id.textAFR2);
        trims = (TextView) findViewById(R.id.textTrims);
        fp_l = (TextView) findViewById(R.id.textFPL);
        fp_h = (TextView) findViewById(R.id.textFPH);
        ign_1 = (TextView) findViewById(R.id.textIgn1);
        ign_2 = (TextView) findViewById(R.id.textIgn2);
        ign_3 = (TextView) findViewById(R.id.textIgn3);
        ign_4 = (TextView) findViewById(R.id.textIgn4);
        ign_5 = (TextView) findViewById(R.id.textIgn5);
        ign_6 = (TextView) findViewById(R.id.textIgn6);
        waterTemp = (TextView) findViewById(R.id.textWaterTemp);
        oilTemp = (TextView) findViewById(R.id.textOilTemp);
        ff = (TextView) findViewById(R.id.textFF);
        fol = (TextView) findViewById(R.id.textFOL);
        avg_ign = (TextView) findViewById(R.id.textAvgIgn);
        map = (TextView) findViewById(R.id.textMap);
        logLen = (TextView) findViewById(R.id.textLogLen);

        graph_ign = (GraphView) findViewById(R.id.graph_ign);
        graph_ign.mign1.name = "IGN 1";
        graph_ign.mign1.paint.setColor(Color.GREEN);
        graph_ign.mign2.name = "IGN 2";
        graph_ign.mign2.paint.setColor(Color.RED);
        graph_ign.mign3.name = "IGN 3";
        graph_ign.mign3.paint.setColor(Color.BLUE);
        graph_ign.mign4.name = "IGN 4";
        graph_ign.mign4.paint.setColor(Color.YELLOW);
        graph_ign.mign5.name = "IGN 5";
        graph_ign.mign5.paint.setColor(Color.MAGENTA);
        graph_ign.mign6.name = "IGN 6";
        graph_ign.mign6.paint.setColor(Color.CYAN);

        connectButton = (ImageButton) findViewById(R.id.imageButton_toggleConnect);
        connectButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (myService == null) return;
                        if (!myService.isConnected()) {
                            myService.connect();
                            startUpdater();
                        } else {
                            myService.disconnect();
                            stopUpdater();
                            connectButton.setBackgroundResource(R.drawable.connect);
                        }
                    }
                }
        );

        btnLogSplit = (Button) findViewById(R.id.logSplit);
        btnLogSplit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (myService == null || !myService.isConnected()) return;
                        myService.logSplit();
                    }
                }
        );
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
        if (myService == null) return;
        if (myService.isConnected() && !buttonConnected) {
            connectButton.setBackgroundResource(R.drawable.disconnect);
            buttonConnected = true;
        } else if (!myService.isConnected() && buttonConnected) {
            connectButton.setBackgroundResource(R.drawable.connect);
            buttonConnected = false;
        }
        long now = System.currentTimeMillis();
        if (myService.isConnected()) {
            //sbFormat.format("%15.2f Psi", myService.getPoint().boost);
            LogPoint lp = myService.getPoint();

            if (lp.rpm != displayDLPoint.rpm) {
                displayDLPoint.rpm = lp.rpm;
                rpm.setText(String.format("%5d RPM", lp.rpm));
                //rpm_gauge.setTargetValue(lp.rpm);
            }

            if (lp.boost != displayDLPoint.boost) {
                displayDLPoint.boost = lp.boost;
                boost.setText(String.format("%5.1f PSI", lp.boost));
            }

            if (lp.target != displayDLPoint.target) {
                displayDLPoint.target = lp.target;
                target.setText(String.format("%5.1f PSI", lp.target));
            }

            if (lp.iat != displayDLPoint.iat) {
                displayDLPoint.iat = lp.iat;
                iat.setText(String.format("%4d \u2109", lp.iat));
            }

            if (lp.afr != displayDLPoint.afr) {
                displayDLPoint.afr = lp.afr;
                afr.setText(String.format("%5.1f", lp.afr));
            }

            if (lp.afr2 != displayDLPoint.afr2) {
                displayDLPoint.afr2 = lp.afr2;
                afr2.setText(String.format("%5.1f", lp.afr2));
            }

            if (lp.trims != displayDLPoint.trims) {
                displayDLPoint.trims = lp.trims;
                trims.setText(String.format("%3d", lp.trims));
            }

            if (lp.fp_l != displayDLPoint.fp_l) {
                displayDLPoint.fp_l = lp.fp_l;
                fp_l.setText(String.format("%3d PSI", lp.fp_l));
            }

            if (lp.fp_h != displayDLPoint.fp_h) {
                displayDLPoint.fp_h = lp.fp_h;
                fp_h.setText(String.format("%3d", lp.fp_h));
            }

            if (lp.ign_1 != displayDLPoint.ign_1) {
                displayDLPoint.ign_1 = lp.ign_1;
                ign_1.setText(String.format("%4.1f", lp.ign_1));
                graph_ign.mign1.addValue(now, (float) lp.ign_1);
                graph_ign.mign1.purge(now - 10000);
            }

            if (lp.ign_2 != displayDLPoint.ign_2) {
                displayDLPoint.ign_2 = lp.ign_2;
                ign_2.setText(String.format("%4.1f", lp.ign_2));
                graph_ign.mign2.addValue(now, (float) lp.ign_2);
                graph_ign.mign2.purge(now - 10000);
            }

            if (lp.ign_3 != displayDLPoint.ign_3) {
                displayDLPoint.ign_3 = lp.ign_3;
                ign_3.setText(String.format("%4.1f", lp.ign_3));
                graph_ign.mign3.addValue(now, (float) lp.ign_3);
                graph_ign.mign3.purge(now - 10000);
            }

            if (lp.ign_4 != displayDLPoint.ign_4) {
                displayDLPoint.ign_4 = lp.ign_4;
                ign_4.setText(String.format("%4.1f", lp.ign_4));
                graph_ign.mign4.addValue(now, (float) lp.ign_4);
                graph_ign.mign4.purge(now - 10000);
            }

            if (lp.ign_5 != displayDLPoint.ign_5) {
                displayDLPoint.ign_5 = lp.ign_5;
                ign_5.setText(String.format("%4.1f", lp.ign_5));
                graph_ign.mign5.addValue(now, (float) lp.ign_5);
                graph_ign.mign5.purge(now - 10000);
            }

            if (lp.ign_6 != displayDLPoint.ign_6) {
                displayDLPoint.ign_6 = lp.ign_6;
                ign_6.setText(String.format("%4.1f", lp.ign_6));
                graph_ign.mign6.addValue(now, (float) lp.ign_6);
                graph_ign.mign6.purge(now - 10000);
            }

            if (lp.waterTemp != displayDLPoint.waterTemp) {
                displayDLPoint.waterTemp = lp.waterTemp;
                waterTemp.setText(String.format("%4d", lp.waterTemp));
            }

            if (lp.oilTemp != displayDLPoint.oilTemp) {
                displayDLPoint.oilTemp = lp.oilTemp;
                oilTemp.setText(String.format("%4d", lp.oilTemp));
            }

            // settings

            JB4SettingPoint sp = myService.getSettingPoint();
            if (lp.ff != displayDLPoint.ff) {
                displaySettingPoint.FF = sp.FF;
                ff.setText(String.format("%4d", sp.FF));
            }

            if (sp.fuel_ol != displaySettingPoint.fuel_ol) {
                displaySettingPoint.fuel_ol = sp.fuel_ol;
                fol.setText(String.format("%4d", sp.fuel_ol));
            }

            if (lp.avg_ign != displayDLPoint.avg_ign) {
                displayDLPoint.avg_ign = lp.avg_ign;
                avg_ign.setText(String.format("%4.1f", lp.avg_ign));
            }

            if (lp.map != displayDLPoint.map) {
                displayDLPoint.map = lp.map;
                map.setText(String.format("%2d", lp.map));
            }

            if (logIndex != myService.getLogIndex()) {
                logIndex = myService.getLogIndex();
                logLen.setText(String.format("%5d/5000", logIndex));
            }
            graph_ign.invalidate();
        } else {
            /*Random r = new Random();
            graph_ign.mign1.addValue(System.currentTimeMillis(), r.nextFloat() * 10);
            graph_ign.mign1.purge(now - 10000);
            graph_ign.mign2.addValue(System.currentTimeMillis(), r.nextFloat() * 10);
            graph_ign.mign2.purge(now - 10000);
            graph_ign.mign3.addValue(System.currentTimeMillis(), r.nextFloat() * 10);
            graph_ign.mign3.purge(now - 10000);
            graph_ign.mign4.addValue(System.currentTimeMillis(), r.nextFloat() * 10);
            graph_ign.mign4.purge(now - 10000);
            graph_ign.mign5.addValue(System.currentTimeMillis(), r.nextFloat() * 10);
            graph_ign.mign5.purge(now - 10000);
            graph_ign.mign6.addValue(System.currentTimeMillis(), r.nextFloat() * 10);
            graph_ign.mign6.purge(now - 10000);*/
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
        if (myService != null && myService.isConnected()) moveTaskToBack(true);
        else {
            super.onBackPressed();
            mNM.cancel(notificationID);
        }
    }


}
