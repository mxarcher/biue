package com.mxarcher.biue.service.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.mxarcher.biue.models.SignalData;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/29 17:07
 * @Description:
 */
public class ConnectService {
    private static final String TAG = "ConnectService";
    private static ConnectService instance = null;
    private final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.CHINA);
    BluetoothAdapter adapter;
    BluetoothManager manager;
    BluetoothDevice currentDevice;
    private final int TMaxSize = 50;
    private final int FMaxSize = 1024;

    private WeakReference<Context> context;
    private ConnectThread connectThread;

    private ConnectService(Context context) {
        this.context = new WeakReference<>(context);
        manager = context.getSystemService(BluetoothManager.class);
        adapter = manager.getAdapter();
    }

    // https://stackoverflow.com/a/14057777
    // 实现线程安全的单例模式
    public static ConnectService getInstance(Context context) {
        if (instance == null) {
            instance = getSync(context);
        }
        return instance;
    }

    private static synchronized ConnectService getSync(Context context) {
        if (instance == null) {
            instance = new ConnectService(context);
        }
        return instance;
    }


    public synchronized BluetoothAdapter getAdapter() {
        return adapter;
    }

    public synchronized void connect(BluetoothDevice device) {
        this.currentDevice = device;
        connectThread = new ConnectThread(device);
    }

    public boolean start(List<Short> tList, ReentrantReadWriteLock tLock, List<Short> fList, ReentrantReadWriteLock fLock, String dir) {
        if (currentDevice != null) {
            if (connectThread != null) {
                connectThread.cancel();
                connectThread = null;
            }
            connectThread = new ConnectThread(currentDevice);
            String path = dir + "/" + sdf.format(new Date());
            connectThread.setFilename(path);
            connectThread.initTList(tList, tLock);
            connectThread.initFList(fList, fLock);
            connectThread.start();
            return true;
        } else return false;
    }

    public void stop() {
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }
    }


    public class ConnectThread extends Thread {
        ReentrantReadWriteLock TLock;
        ReentrantReadWriteLock FLock;
        private String filename;
        private DataInputStream in;
        private DataInputStream out;
        private BluetoothSocket socket = null;
        private boolean isRunning;
        private List<Short> TList;
        private List<Short> FList;


        public ConnectThread(BluetoothDevice device) {
            if (ActivityCompat.checkSelfPermission(context.get(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            try {
                if (socket == null) {
                    socket = device.createRfcommSocketToServiceRecord(uuid);
                }
                Log.d(TAG, "ConnectThread: " + socket.toString());
                if (in == null) {
                    in = new DataInputStream(socket.getInputStream());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public boolean isSocketEmpty() {
            return socket == null;
        }

        public void initTList(List<Short> shorts, ReentrantReadWriteLock lock) {
            this.TList = shorts;
            this.TLock = lock;
        }

        public void initFList(List<Short> shorts, ReentrantReadWriteLock lock) {
            this.FList = shorts;
            this.FLock = lock;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public void run() {
            isRunning = true;
            if (ActivityCompat.checkSelfPermission(context.get(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            adapter.cancelDiscovery();
            if (socket != null) {
                synchronized (ConnectService.this) {

                    try {
                        socket.connect();
                    } catch (IOException e) {
                        try {
                            socket.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        e.printStackTrace();
                        return;
                    }
                    // 能走到这里说明连接已经成功了
                    boolean prefix_ab = false;
                    boolean prefix_cd = false;
                    byte temp;
                    while (isRunning) {
                        if (socket.isConnected()) {
                            try {
                                if (!prefix_ab) {
                                    if (in.readByte() == (byte) 0xAB) {
                                        prefix_ab = true;
                                    }
                                }

                                if ((temp = in.readByte()) == (byte) 0xCD) {
                                    prefix_cd = true;
                                } else if (temp == (byte) 0xAB) {
                                    prefix_ab = true;
                                    continue;
                                }
                                if (prefix_ab && prefix_cd) {
                                    SignalData signalData = new SignalData();
                                    signalData.x = in.readShort();
                                    signalData.y = in.readShort();
                                    signalData.z = in.readShort();
                                    signalData.ppg = in.readShort();
                                    signalData.temp = in.readShort();
                                    signalData.resistance = in.readShort();
                                    TLock.writeLock().lock();
                                    addToTList(signalData.ppg);
                                    TLock.writeLock().unlock();
                                    FLock.writeLock().lock();
                                    addToFList(signalData.ppg);
                                    FLock.writeLock().unlock();
                                }

                                prefix_ab = false;
                                prefix_cd = false;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else cancel();
                    }
                }
            }
        }

        public void cancel() {
            try {
                isRunning = false;
                socket.close();
            } catch (IOException e) {
                Log.e(TAG, "不要关闭客户端连接");
                e.printStackTrace();
            }
            Thread.interrupted();
        }

        private void addToTList(short x) {
            if (TList.size() == TMaxSize) {
                TList.remove(0);
            }
            TList.add(x);
        }

        private void addToFList(short x) {
            Log.d(TAG, "addToFList: "+FList.size());
            if (FList.size() == FMaxSize) {
                FList.remove(0);
            }
            FList.add(x);
        }
    }
}
