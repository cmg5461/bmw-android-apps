package com.cmg5461.jb4u;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cmg5461.jb4u.data.Constants;
import com.cmg5461.jb4u.providers.JB4Connection;
import com.cmg5461.jb4u.service.JB4ConnectionService;


public class JB4UActivity extends ActionBarActivity {

    private JB4Connection jb4Connection;
    private ServiceConnection JB4ServiceConnection;
    private JB4ConnectionService myServiceBinder;

    private TextView console;
    private Button startButton;
    private Button stopButton;
    private Button randomButton;

    private ScrollView scrollView;

    private StringBuilder consoleText;
    private int consoleLines = 0;

    private boolean serviceStarted = false;

    @Override
    protected void onDestroy() {
        if (myServiceBinder != null) {
            myServiceBinder.getJb4Connection().stop();
            unbindService(myConnection);
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jb4_buddy);
        InitializeComponents();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void InitializeComponents() {
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        console = (TextView) findViewById(R.id.console);
        consoleText = new StringBuilder("init");
        console.setText(consoleText.toString());
        console.setEnabled(false);
        console.setTextColor(Color.BLACK);
        consoleLines++;

        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddConsoleLine("service start click");
                if (!serviceStarted) {
                    Intent i = new Intent(JB4UActivity.this, JB4ConnectionService.class);
                    i.putExtra("name", "JB4ConnectionService");
                    bindService(i, myConnection, Context.BIND_AUTO_CREATE);
                    JB4UActivity.this.startService(i);
                    AddConsoleLine("service started");
                    serviceStarted = true;
                }
            }
        });
        stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddConsoleLine("service stahp click");
                if (serviceStarted) {
                    Intent i = new Intent(JB4UActivity.this, JB4ConnectionService.class);
                    JB4UActivity.this.stopService(i);
                    if (myServiceBinder != null && myServiceBinder.getJb4Connection() != null) {
                        myServiceBinder.getJb4Connection().stop();
                        unbindService(myConnection);
                        myServiceBinder = null;
                    }
                    serviceStarted = false;
                    AddConsoleLine("service stahpped");
                }
            }
        });
        randomButton = (Button) findViewById(R.id.randomButton);
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddConsoleLine("random click");
                if (serviceStarted && myServiceBinder != null) {
                    AddConsoleLine("Random: " + myServiceBinder.getJb4Connection().getData());
                }
            }
        });

        AddConsoleLine("init finished");
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
        if (id == R.id.action_settings) {
            return true;
        }

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
        console.setText(consoleText.toString());

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        if (scrollView != null) {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(View.FOCUS_DOWN);
                    //scrollView.scrollTo(0, scrollView.getBottom());
                }
            });
        }
    }

    public ServiceConnection myConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder binder) {
            myServiceBinder = ((JB4ConnectionService.MyBinder) binder).getService();
            Log.d(Constants.TAG, "service connected");
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.d(Constants.TAG, "service disconnected");
            myServiceBinder = null;
        }
    };

    @Override
    protected void onResume() {
        Log.d(Constants.TAG, "onResume");
        if (myServiceBinder == null) {
            doBindService();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(Constants.TAG, "onPause");
        //if (myServiceBinder != null) {
        //unbindService(myConnection);
        //myServiceBinder = null;
        //}
        super.onPause();
    }

    public void doBindService() {
        Intent intent = new Intent(JB4UActivity.this, JB4ConnectionService.class);
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE);
    }
}
