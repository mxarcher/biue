package com.mxarcher.biue.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mxarcher.biue.adapters.bluetooth.DevicesAdapter;
import com.mxarcher.biue.databinding.BluetoothFragmentBinding;
import com.mxarcher.biue.service.bluetooth.ConnectService;
import com.mxarcher.biue.viewmodels.BlueToothViewModel;
import com.tbruyelle.rxpermissions3.RxPermissions;

public class BluetoothFragment extends Fragment {
    private static final String TAG = "BlueToothFragment";
    private final String[] permissions = new String[]{
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };
    BluetoothFragmentBinding binding;
    DevicesAdapter pairedDevicesAdapter;
    DevicesAdapter unpairedDevicesAdapter;
    RxPermissions rxPermissions;
    BluetoothAdapter bluetoothAdapter;
    ConnectService service;
    // 注意连接设备前需要关闭扫描
    private BlueToothViewModel blueToothViewModel;
    private final BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.d(TAG, "found: " + device.getAddress());
                    blueToothViewModel.addToUnpairedDeviceList(device);
                    break;
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (rxPermissions == null) {
            rxPermissions = new RxPermissions(this);
        }
        blueToothViewModel = new ViewModelProvider(requireActivity()).get(BlueToothViewModel.class);
        binding = BluetoothFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initBlueTooth();
        Log.d(TAG, "onViewCreated:  init finished");

        if (blueToothViewModel.callBack == null) {
            blueToothViewModel.callBack = permission -> rxPermissions.request(permission)
                    .subscribe(granted -> {
                    });
        }
        if (pairedDevicesAdapter == null) {
            pairedDevicesAdapter = new DevicesAdapter(new DevicesAdapter.Callback() {
                @Override
                public void onRequestPermission(String permission) {
                    rxPermissions.request(permissions).subscribe();
                }

                // TODO：待优化代码，减少权限检查次数
                @Override
                public void onItemClickListener(BluetoothDevice device) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setMessage("请选择要对已绑定设备进行操作");
                    builder.setPositiveButton("连接设备", (dialogInterface, i) -> {
                        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            rxPermissions.request(Manifest.permission.BLUETOOTH_CONNECT).subscribe();
//                            return;
                        }
                        if (service != null) {
                            service.connect(device);
                        } else Log.d(TAG, "-------------------- service is null");
                    });
                    builder.setNeutralButton("撤销配对", (dialogInterface, i) -> {
                        blueToothViewModel.removeBond(device);
                    });
                    builder.setNegativeButton("取消", null);
                    builder.create().show();
                }
            });
        }
        if (unpairedDevicesAdapter == null) {
            unpairedDevicesAdapter = new DevicesAdapter(new DevicesAdapter.Callback() {
                @Override
                public void onRequestPermission(String permission) {
                    rxPermissions.request(permissions).subscribe();
                }

                @Override
                public void onItemClickListener(BluetoothDevice device) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setMessage("请选择要对未绑定设备进行操作");
                    builder.setPositiveButton("绑定设备", (dialogInterface, i) -> {
                        Log.d(TAG, "onItemClickListener: createBond");
                        blueToothViewModel.createBond(device);
                    });
                    builder.setNegativeButton("取消", null);
                    builder.create().show();
                }
            });
        }
        binding.bluetoothPairedList.setAdapter(pairedDevicesAdapter);
        binding.bluetoothUnpairedList.setAdapter(unpairedDevicesAdapter);
        blueToothViewModel.getPairedLiveData().observe(getViewLifecycleOwner(), bluetoothDevices -> {
            pairedDevicesAdapter.setDeviceList(bluetoothDevices);
            Log.d(TAG, "onViewCreated: getPairedLiveData" + bluetoothDevices.toString());
        });
        blueToothViewModel.getUnPairedLiveData().observe(getViewLifecycleOwner(), bluetoothDevices -> {
            unpairedDevicesAdapter.setDeviceList(bluetoothDevices);
            Log.d(TAG, "onViewCreated: getUnPairedLiveData" + bluetoothDevices.toString());
        });

        findDevice();
    }

    private void initBlueTooth() {
        // 蓝牙服务注册
        if (service == null) {
            service = ConnectService.getInstance(requireContext());
        }
        if (bluetoothAdapter == null) {
            bluetoothAdapter = service.getAdapter();
        }


        // 请求所有权限
        rxPermissions.request(permissions)
                .subscribe(granted -> {
                });
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        requireActivity().registerReceiver(bluetoothReceiver, intentFilter);
    }


    private boolean isBlueToothEnabled() {
        if (isSupportBlueTooth()) {
            return bluetoothAdapter.isEnabled();
        } else return false;
    }

    private void turnOnBlueTooth() {
        if (!isBlueToothEnabled()) {
            ActivityResultLauncher<Intent> intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result != null) {
                    Log.d(TAG, "turnOnBlueTooth: " + result.getResultCode());
                }
            });
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            intentActivityResultLauncher.launch(enableIntent);
        }
    }

    private void turnOffBlueTooth() {
        if (isBlueToothEnabled()) {
            Activity activity = requireActivity();
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                rxPermissions.request(Manifest.permission.BLUETOOTH_CONNECT)
                        .subscribe(granted -> {

                        });
            }
            bluetoothAdapter.disable();
        }
    }

    /**
     * 启用蓝牙可见
     */
    private void enableVisibility() {
        if (isSupportBlueTooth()) {
            ActivityResultLauncher<Intent> intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            });
            Intent visibilityIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            visibilityIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            intentActivityResultLauncher.launch(visibilityIntent);
        }
    }

    private void findDevice() {
        if (bluetoothAdapter != null) {
            Activity activity = requireActivity();
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "findDevice: ");
                rxPermissions.request(Manifest.permission.BLUETOOTH_SCAN)
                        .subscribe(granted -> {
                        });
                return;
            }
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
            bluetoothAdapter.startDiscovery();
            Log.d(TAG, "findDevice:end ");
        }
    }


    private void addToUnpairedDevice(Intent intent) {
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        blueToothViewModel.addToUnpairedDeviceList(device);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().unregisterReceiver(bluetoothReceiver);
    }

    private void showMsg(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public boolean isSupportBlueTooth() {
        if (bluetoothAdapter == null) {
            showMsg("该设备不支持蓝牙");
            return false;
        } else {
            return true;
        }
    }

}


/*
startActivityForResult替代方案
public class JumpPage extends AppCompatActivity {
    public void goPage() {
//            startActivityForResult(intent, newWordActivityRequestCode)
        ActivityResultLauncher<Intent> intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                //此处是跳转的result回调方法
                if (result.getData() != null && result.getResultCode() == Activity.RESULT_OK) {
                    result.getData().getStringExtra(NewWordActivity.EXTRA_REPLY);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.empty_not_saved, Toast.LENGTH_LONG).show();
                }
            }
        });
        Intent intent = new Intent(JumpPage.this, NewWordActivity.class);
        intentActivityResultLauncher.launch(intent);
    }
*/