package com.cmg5461.jb4u.providers;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.cmg5461.jb4u.data.Constants;
import com.cmg5461.jb4u.data.JB4Buffer;
import com.cmg5461.jb4u.data.JB4Command;
import com.cmg5461.jb4u.log.DetailLogPoint;
import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_Device;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Chris on 11/4/2015.
 */
public class JB4Connection implements Runnable {

    private Service mService;
    private Context ctx;

    private static final String ACTION_USB_PERMISSION = "com.cmg5461.jb4u.USB_PERMISSION";

    private UsbDevice mUsbDevice;
    private UsbManager mUsbManager;
    private UsbSerialDriver mUsbDriver;
    private PendingIntent mPermissionIntent;

    private boolean logging = false;
    private boolean run = false;
    private boolean mPermissionRequestPending = false;

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
    private final int readDataInterval = 25; // ms
    private final int logHeartbeatInterval = 1000; // ms
    private final int commandDelayInterval = 100; // ms

    // buffer vars
    private DetailLogPoint logPoint = new DetailLogPoint(true);
    private JB4Buffer buffer = new JB4Buffer(logPoint);
    private int maxPoints = 10000;
    private DetailLogPoint[] storedPoints = new DetailLogPoint[maxPoints];
    private int storedPointIdx = 0;

    public JB4Connection(Service mService) {
        this.mService = mService;
        this.ctx = mService.getApplicationContext();
        for (int i = 0; i < maxPoints; i++) {
            storedPoints[i] = new DetailLogPoint();
        }
    }

    public void Connect() {
        mUsbManager = (UsbManager) mService.getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> drivers = UsbSerialProber.getDefaultProber().findAllDrivers(mUsbManager);
        if (drivers.isEmpty()) return;
        mUsbDriver = drivers.get(0);
        mUsbDevice = mUsbDriver.getDevice();
        mPermissionIntent = PendingIntent.getBroadcast(mService, 0, new Intent(ACTION_USB_PERMISSION), 0);
        mUsbManager.requestPermission(mUsbDevice, mPermissionIntent);
        while (!mUsbManager.hasPermission(mUsbDriver.getDevice())) {
            synchronized (mUsbReceiver) {
                if (!mPermissionRequestPending) {
                    mUsbManager.requestPermission(mUsbDevice, mPermissionIntent);
                    mPermissionRequestPending = true;
                }
            }
        }
        try {
            ftD2xx = D2xxManager.getInstance(mService);
        } catch (D2xxManager.D2xxException e) {
            e.printStackTrace();
        }
        if (!ftD2xx.setVIDPID(1027, 24577)) {

        }
        int nDev = ftD2xx.createDeviceInfoList(ctx);
        if (nDev < 1) return;
        for (int i = 0; i < nDev; i++) {
            ftdev = ftD2xx.openByIndex(ctx, i);
            if (ftdev != null) break;
        }
        if (ftdev == null) {
            return;
        }
        if (!ftdev.isOpen()) {
            return;
        }
        // ftdev is open!
        ftdev.setBitMode((byte) 0, bitMode);
        ftdev.setBaudRate(baudRate);
        ftdev.setDataCharacteristics(dataBits, stopBits, parity);
        ftdev.setFlowControl(flowControl, (byte) 0, (byte) 0);
        try {
            sendBytes(JB4Command.INITIALIZE_CONNECTION1);
            Thread.sleep(500);
            int s1 = readByte(JB4Command.HEARTBEAT_TICK);
            int s2 = readByte(JB4Command.HEARTBEAT_TICK);
            if ((s1 != 72 && s1 != 59) || (s2 != 72 && s2 != 59)) {
                Log.d(Constants.TAG, "FAILED TO CONNECT TO JB4 - FUCK");
                return;
            }
            purgeRX();
            buffer.updateTimestamp();
            sendBytes(JB4Command.INITIALIZE_CONNECTION2);
            parseData();
            sendBytes(JB4Command.INITIALIZE_CONNECTION3);
            parseData();
            logging = true;
            Log.d(Constants.TAG, "Connected!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void Disconnect() {
        if (ftdev == null || !ftdev.isOpen()) return;
        if (storedPointIdx > 0) {
            saveCsvFile(storedPointIdx);
            storedPointIdx = 0;
        }
        logging = false;
        run = false;
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ftdev.close();
        ftdev = null;
    }

    @Override
    public void run() {
        Connect();
        run = true;
        Thread logHeartBeatThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (run) {
                    try {
                        if (logging && ftdev != null && ftdev.isOpen()) {
                            sendBytes(JB4Command.HEARTBEAT_TICK);
                        }
                        Thread.sleep(logHeartbeatInterval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        logHeartBeatThread.start();
        while (run) {
            try {
                if (logging && ftdev != null && ftdev.isOpen()) {
                    parseData();
                }
                buffer.updateTimestamp();
                Thread.sleep(readDataInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Disconnect();
    }

    public void stop() {
        run = false;
    }

    public int getData() {
        Random r = new Random();
        r.setSeed(System.currentTimeMillis());
        return r.nextInt(100000);
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
            Constants.LogD("DATA TX: " + new String(b));
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
        int r = 0;

        try {
            if (!cmd.equals("")) {
                purgeRX();
                sendBytes(cmd);
            }
            int c = 0;
            while (r == 0 && c++ < 40) {
                r = ftdev.getQueueStatus();
                Thread.sleep(20);
            }
            if (r == 0) return 88;
            byte[] bytes = new byte[1];
            ftdev.read(bytes, 1);
            Constants.LogD("DATA RX: " + new String(bytes));
            return bytes[0];
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 88;
    }

    public String readString(String cmd) {
        int r = 0;
        try {
            if (!cmd.equals("")) {
                purgeRX();
                sendBytes(cmd);
            }
            int c = 0;
            while (r == 0 && c++ < 40) {
                r = ftdev.getQueueStatus();
                Thread.sleep(20);
            }
            if (r == 0) return "";
            byte[] bytes = new byte[r];
            ftdev.read(bytes, r);
            Constants.LogD("DATA RX: " + new String(bytes));
            return new String(bytes);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void parseData() {
        if (ftdev == null || !ftdev.isOpen()) return;
        int len = ftdev.getQueueStatus();
        if (len > 0) {
            byte[] bytes = new byte[len];
            ftdev.read(bytes, len);
            Constants.LogD("DATA RX: " + Arrays.toString(bytes));
            buffer.AddBytes(bytes);
            buffer.ParseBuffer();
            if (logPoint.Rpm < 1 || logPoint.Rpm != storedPoints[storedPointIdx].Rpm || logPoint.Boost != storedPoints[storedPointIdx].Boost) {
                DetailLogPoint.Copy(logPoint, storedPoints[storedPointIdx++]);
                if (storedPointIdx == maxPoints) {
                    saveCsvFile(storedPointIdx);
                    storedPointIdx = 0;
                }
            }
        }
    }

    private void Toast(String s) {
        Toast.makeText(mService.getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        Constants.LogD(s);
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                    } else Constants.LogD("permission denied for device: " + device);
                }
            } else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
                UsbAccessory mDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (mDevice != null && mDevice.equals(device)) {
                    //closeAccessory();
                }
            }
        }
    };

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
        final DetailLogPoint[] tempPoints = new DetailLogPoint[rows];
        System.arraycopy(storedPoints, 0, tempPoints, 0, rows);
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();
                sb.append(DetailLogPoint.getCsvHeader());
                for (int i = 0; i < rows; i++) {
                    sb.append(DetailLogPoint.getCsvString(tempPoints[i]));
                }
                File folder = new File(Environment.getExternalStorageDirectory() + "/Logs");
                boolean var = false;
                if (!folder.exists()) var = folder.mkdir();
                String filename = folder.toString() + "/JB4U_LOG_" + new SimpleDateFormat("yyyy-MM-dd-HH_mm_ss", Locale.US).format(new Date()) + ".csv";
                try {
                    FileWriter fw = new FileWriter(filename);
                    fw.write(sb.toString());
                    fw.flush();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public DetailLogPoint getLogPoint() {
        return logPoint;
    }
}
