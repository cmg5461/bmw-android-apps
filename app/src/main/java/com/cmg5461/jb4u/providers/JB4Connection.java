package com.cmg5461.jb4u.providers;

import android.app.Service;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.cmg5461.jb4u.data.Constants;
import com.cmg5461.jb4u.data.JB4Buffer;
import com.cmg5461.jb4u.data.JB4Command;
import com.cmg5461.jb4u.log.LogPoint;
import com.cmg5461.jb4u.log.JB4SettingPoint;
import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_Device;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Chris on 11/4/2015.
 */
public class JB4Connection {

    private static JB4Connection singleton = null;

    public static JB4Connection getSingleton() {
        if (singleton == null) {
            singleton = new JB4Connection();
        }
        return singleton;
    }

    private Service mService;
    private Context ctx;

    private boolean logging = false;

    // drives and drivers
    private FT_Device ftdev;
    public D2xxManager ftD2xx;

    // connection parameters
    private final int baudRate = 9600;
    private final byte dataBits = D2xxManager.FT_DATA_BITS_8;
    private final byte stopBits = D2xxManager.FT_STOP_BITS_1;
    private final byte parity = D2xxManager.FT_PARITY_NONE;
    private final byte bitMode = D2xxManager.FT_BITMODE_RESET;
    private final byte flowControl = D2xxManager.FT_FLOW_NONE;

    // communication constants
    private final int readInterval = 25; // ms
    private final int heartbeatInterval = 1000; // ms
    private final int commandDelayInterval = 100; // ms
    private final int testInterval = 25;

    //private StringBuilder sb = new StringBuilder();

    // buffer vars
    private LogPoint logPoint = new LogPoint(true);
    private LogPoint lastLogPoint = new LogPoint(true);
    private JB4SettingPoint settingPoint = new JB4SettingPoint();
    private JB4Buffer buffer = new JB4Buffer(logPoint, settingPoint);
    private int maxPoints = 5000;
    private LogPoint[] storedPoints = new LogPoint[maxPoints];
    private int storedPointIdx = 0;
    private byte[] rxBuffer = new byte[512];

    // timers
    private final ScheduledExecutorService heartbeatScheduler;
    private final ScheduledExecutorService readScheduler;
    private final ScheduledExecutorService testScheduler;
    private final Runnable heartbeatRunnable;
    private final Runnable readRunnable;
    private final Runnable testRunnable;
    private ScheduledFuture heartbeatLoopFuture = null;
    private ScheduledFuture readLoopFuture = null;
    private ScheduledFuture testLoopFuture = null;
    private Handler mHandler = null;

    private Toast mToast = null;

    private JB4Connection() {
        mHandler = new Handler(Looper.getMainLooper());
        heartbeatScheduler = Executors.newScheduledThreadPool(1);
        readScheduler = Executors.newScheduledThreadPool(1);
        testScheduler = Executors.newScheduledThreadPool(1);
        heartbeatRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (logging && ftdev != null && ftdev.isOpen()) {
                        sendBytes(JB4Command.HEARTBEAT_TICK);
                    }
                } catch (Throwable t) {
                    Log.e(Constants.TAG, "heartbeatRunnable ERROR", t);
                }
            }
        };
        readRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (logging && ftdev != null && ftdev.isOpen()) {
                        parseData();
                    }
                } catch (Throwable t) {
                    Log.e(Constants.TAG, "readRunnable ERROR", t);
                }
            }
        };
        testRunnable = new Runnable() {
            @Override
            public void run() {
                if (logging) {
                    logPoint.rpm++;
                    logPoint.boost += 0.01;
                    //long now = System.currentTimeMillis();
                    //Log.d(Constants.TAG, (now - lastTime) + "ms elapsed");
                    //lastTime = now;
                }
            }
        };
        for (int i = 0; i < rxBuffer.length; i++) {
            rxBuffer[i] = -1;
        }
        for (int i = 0; i < maxPoints; i++) {
            storedPoints[i] = new LogPoint();
        }
        settingPoint.jb4interface = "JB4U";
        settingPoint.motor = "N54 E Series";
    }

    public void Connect() {
        Disconnect();
        Toast("Starting connection!");
        try {
            ftD2xx = D2xxManager.getInstance(mService);
        } catch (D2xxManager.D2xxException e) {
            e.printStackTrace();
            Toast("Error getting FTDI instance");
        }
        ftD2xx.setVIDPID(1027, 24577);
        int nDev = ftD2xx.createDeviceInfoList(ctx);
        if (nDev < 1) {
            logging = true;
            testLoopFuture = testScheduler.scheduleAtFixedRate(testRunnable, 0L, testInterval, TimeUnit.MILLISECONDS);
            Toast("No device found!");
            return;
        }
        for (int i = 0; i < nDev; i++) {
            ftdev = ftD2xx.openByIndex(ctx, i);
            if (ftdev != null) break;
        }
        if (ftdev == null || !ftdev.isOpen()) {
            Toast("Error opening JB4 Connection!");
            return;
        }

        // ftdev is open!
        ftdev.setBitMode((byte) 0, bitMode);
        ftdev.setBaudRate(baudRate);
        ftdev.setDataCharacteristics(dataBits, stopBits, parity);
        ftdev.setFlowControl(flowControl, (byte) 0, (byte) 0);
        // ftdev.setLatencyTimer((byte) 2);
        ftdev.restartInTask();
        try {
            sendBytes(JB4Command.INITIALIZE_CONNECTION1);
            Thread.sleep(500);
            int s1 = readByte(JB4Command.HEARTBEAT_TICK);
            int s2 = readByte(JB4Command.HEARTBEAT_TICK);
            if ((s1 != 72 && s1 != 59) || (s2 != 72 && s2 != 59)) {
                Toast("JB4 did not accept the connection initialization!");
                return;
            }
            purgeRX();
            buffer.updateTimestamp();
            sendBytes(JB4Command.INITIALIZE_CONNECTION2);
            parseData();
            buffer.updateTimestamp();
            sendBytes(JB4Command.INITIALIZE_CONNECTION3);
            parseData();
            if (ftdev != null && ftdev.isOpen()) {
                Toast("Connected!");
                logging = true;
                heartbeatLoopFuture = heartbeatScheduler.scheduleAtFixedRate(heartbeatRunnable, 0L, heartbeatInterval, TimeUnit.MILLISECONDS);
                readLoopFuture = readScheduler.scheduleAtFixedRate(readRunnable, 0L, readInterval, TimeUnit.MILLISECONDS);
            } else {
                Toast("Connection failed :(");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void Disconnect() {
        if (heartbeatLoopFuture != null && !heartbeatLoopFuture.isCancelled())
            heartbeatLoopFuture.cancel(true);
        if (readLoopFuture != null && !readLoopFuture.isCancelled()) readLoopFuture.cancel(true);
        if (testLoopFuture != null && !testLoopFuture.isCancelled()) testLoopFuture.cancel(true);
        if (storedPointIdx > 0) {
            saveCsvFile(storedPointIdx);
            storedPointIdx = 0;
        }
        logging = false;
        if (ftdev == null || !ftdev.isOpen()) return;
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ftdev.close();
        ftdev = null;
    }

    public void sendBytes(JB4Command cmd) {
        sendBytes(cmd.getValue());
    }

    public void sendBytes(String cmd) {
        if (ftdev == null || !ftdev.isOpen()) return;
        byte[] bytes = cmd.getBytes();
        byte[] b = new byte[1];
        for (byte by : bytes) {
            b[0] = by;
            //Constants.LogD("DATA TX: " + new String(b));
            ftdev.write(b, 1);
            try {
                Thread.sleep(commandDelayInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int readByte(JB4Command cmd) {
        return readByte(cmd.getValue());
    }

    public int readByte(String cmd) {
        if (ftdev == null || !ftdev.isOpen()) return -1;
        try {
            if (!cmd.equals("")) {
                purgeRX();
                sendBytes(cmd);
            }
            int c = 0;
            int r = ftdev.getQueueStatus();
            while (r == 0 && c++ < 40) {
                r = ftdev.getQueueStatus();
                Thread.sleep(20);
            }
            if (r == 0) return 88;
            byte[] bytes = new byte[1];
            ftdev.read(bytes, 1);
            //Constants.LogD("DATA RX: " + new String(bytes));
            return bytes[0];
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 88;
    }

    public String readString(String cmd) {
        try {
            if (!cmd.equals("")) {
                purgeRX();
                sendBytes(cmd);
            }
            int c = 0;
            int r = ftdev.getQueueStatus();
            while (r == 0 && c++ < 40) {
                r = ftdev.getQueueStatus();
                Thread.sleep(20);
            }
            if (r == 0) return "";
            byte[] bytes = new byte[r];
            ftdev.read(bytes, r);
            //Constants.LogD("DATA RX: " + new String(bytes));
            return new String(bytes);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void parseData() {
        if (ftdev == null || !ftdev.isOpen()) return;
        int r = ftdev.getQueueStatus();
        if (r > 0) {
            ftdev.read(rxBuffer, r);
            byte[] rx = Arrays.copyOfRange(rxBuffer, 0, r);
            //sb.append(System.currentTimeMillis());
            //for (int i = 0; i < r; i++) sb.append(',').append(rx[i]);
            //sb.append("\r\n");
            //Constants.LogD("DATA RX: " + Arrays.toString(rx));
            buffer.AddBytes(rx);
            buffer.ParseBuffer();
            if (System.currentTimeMillis() - lastLogPoint.timestamp > 99 || logPoint.rpm < 1 || logPoint.rpm != lastLogPoint.rpm || logPoint.boost != lastLogPoint.boost) {
                LogPoint.Copy(logPoint, lastLogPoint);
                buffer.updateTimestamp();
                LogPoint.Copy(logPoint, storedPoints[storedPointIdx++]);
                if (storedPointIdx == maxPoints) {
                    saveCsvFile(storedPointIdx);
                    storedPointIdx = 0;
                }
            }
        }
    }

    public void Toast(final String s) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mToast != null) mToast.cancel();
                mToast = Toast.makeText(mService.getApplicationContext(), s, Toast.LENGTH_SHORT);
                mToast.show();
            }
        }, 50);

        Constants.LogD(s);
    }

    private void purgeRX() {
        ftdev.purge(D2xxManager.FT_PURGE_RX);
    }

    private void purgeTX() {
        ftdev.purge(D2xxManager.FT_PURGE_TX);
    }

    private void purgeRXTX() {
        ftdev.purge(D2xxManager.FT_PURGE_RX);
        ftdev.purge(D2xxManager.FT_PURGE_TX);
    }

    private void saveCsvFile(final int rows) {
        final LogPoint[] tempPoints = new LogPoint[rows];
        final JB4SettingPoint settings = JB4SettingPoint.clone(settingPoint);
        for (int i = 0; i < rows; i++) tempPoints[i] = LogPoint.clone(storedPoints[i]);
        new Thread(new Runnable() {
            @Override
            public void run() {
                long startTime = tempPoints[0].timestamp;
                File folder = new File(Environment.getExternalStorageDirectory() + "/Logs");
                boolean var;
                if (!folder.exists()) var = folder.mkdir();
                String filename = folder.toString() + "/JB4U_LOG_" + new SimpleDateFormat("yyyy-MM-dd-HH_mm_ss", Locale.US).format(new Date()) + ".csv";
                String jb4logName = folder.toString() + "/JB4U_JB4Interface_" + new SimpleDateFormat("yyyy-MM-dd-HH_mm_ss", Locale.US).format(new Date()) + ".csv";

                try {
                    FileOutputStream fos = new FileOutputStream(jb4logName);
                    fos.write(JB4SettingPoint.getJB4Header(settings).getBytes());
                    for (int i = 0; i < rows; i++)
                        fos.write(LogPoint.getJB4LogPointData(tempPoints[i], startTime).getBytes());
                    fos.flush();
                    fos.close();
                    Toast("JB4 save successful.");
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast("JB4 save unsuccessful.");
                }

                try {
                    FileOutputStream fos = new FileOutputStream(filename);
                    fos.write(LogPoint.getCsvHeader().getBytes());
                    for (int i = 0; i < rows; i++)
                        fos.write(LogPoint.getCsvString(tempPoints[i]).getBytes());
                    fos.flush();
                    fos.close();
                    Toast("Save successful.");
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast("Save unsuccessful.");
                }
            }
        }).start();
    }

    public LogPoint getLogPoint() {
        return logPoint;
    }

    public JB4SettingPoint getSettingPoint() {
        return settingPoint;
    }

    public int getLogIndex() {
        return storedPointIdx;
    }

    public void setService(Service mService) {
        this.mService = mService;
        this.ctx = mService.getApplicationContext();
    }

    public boolean isLogging() {
        return logging;
    }
}
