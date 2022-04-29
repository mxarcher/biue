package com.mxarcher.biue.viewmodels;

import android.Manifest;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BlueToothViewModel extends AndroidViewModel {
    private static final String TAG = "BlueToothViewModel";
    private final MutableLiveData<List<BluetoothDevice>> pairedLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<BluetoothDevice>> unPairedLiveData = new MutableLiveData<>();
    private final BluetoothAdapter adapter;
    private final BluetoothManager manager;
    public CallBack callBack;

    public BlueToothViewModel(@NonNull Application application) {
        super(application);
        manager = application.getSystemService(BluetoothManager.class);
        adapter = manager.getAdapter();
    }


    public MutableLiveData<List<BluetoothDevice>> getPairedLiveData() {
        getBluetoothDeviceList();
        return pairedLiveData;
    }

    // 通过子线程获取数据
    // 获取已经绑定的设备时需要启动蓝牙才能发现设备
    synchronized public void getBluetoothDeviceList() {
        new Thread(() -> {
            if (adapter != null) {
                if (getApplication().checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    callBack.onRequestPermission(Manifest.permission.BLUETOOTH_CONNECT);
                }
                Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
                Log.d(TAG, "getBluetoothDeviceList: getBonedDevices :" + pairedDevices.size());
                if (pairedDevices.size() > 0) {
                    ArrayList<BluetoothDevice> list = new ArrayList<>(pairedDevices);
                    pairedLiveData.postValue(list);
                    Log.d(TAG, "getBluetoothDeviceList: " + list.toString());
                }
            }
        }).start();
    }


    public MutableLiveData<List<BluetoothDevice>> getUnPairedLiveData() {
        return unPairedLiveData;
    }

    public List<BluetoothDevice> getUnPairedDeviceList() {
        return unPairedLiveData.getValue();
    }

    public void addToUnpairedDeviceList(BluetoothDevice device) {
        List<BluetoothDevice> deviceList = unPairedLiveData.getValue();
        List<BluetoothDevice> pairedList = pairedLiveData.getValue();
        if (deviceList == null) {
            deviceList = new ArrayList<>();
        }
        if (pairedList != null && pairedList.contains(device)) {
            return;
        }
        if (!deviceList.contains(device)) {
            if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                callBack.onRequestPermission(Manifest.permission.BLUETOOTH_CONNECT);
            }
            String x = device.getName();
            if (x != null) {
                deviceList.add(device);
                unPairedLiveData.postValue(deviceList);
            }
        }
    }

    private void addToPairedDeviceList(BluetoothDevice device) {
        List<BluetoothDevice> deviceList = pairedLiveData.getValue();
        if (deviceList != null && (!deviceList.contains(device))) {
            Log.d(TAG, "addToPairedDeviceList: ");
            deviceList.add(device);
            pairedLiveData.postValue(deviceList);
        }
    }

    public void removeFromUnpairedDeviceList(BluetoothDevice device) {
        List<BluetoothDevice> deviceList = unPairedLiveData.getValue();
        if (deviceList != null) {
            deviceList.remove(device);
            addToPairedDeviceList(device);
        }
    }


    public void createBond(BluetoothDevice device) {
        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            callBack.onRequestPermission(Manifest.permission.BLUETOOTH_CONNECT);
        }
        device.createBond();
        removeFromUnpairedDeviceList(device);
        addToPairedDeviceList(device);
    }

    public void removeBond(BluetoothDevice device) {
        try {
            Method method = BluetoothDevice.class.getMethod("removeBond");
            method.invoke(device);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
        List<BluetoothDevice> deviceList = pairedLiveData.getValue();
        if (deviceList != null) {
            deviceList.remove(device);
            pairedLiveData.postValue(deviceList);
        }
        addToUnpairedDeviceList(device);
    }

    private void showMsg(String msg) {
        Toast.makeText(getApplication().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public interface CallBack {
        void onRequestPermission(String permission);
    }
}