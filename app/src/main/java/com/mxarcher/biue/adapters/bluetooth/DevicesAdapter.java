package com.mxarcher.biue.adapters.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mxarcher.biue.databinding.BluetoothDeviceItemBinding;

import java.util.List;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/27 19:11
 * @Description:
 */
public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.ViewHolder> {
    private static final String TAG = "PairedDevicesAdapter";
    List<BluetoothDevice> deviceList;
    Callback callback;

    public DevicesAdapter(Callback callback) {
        this.callback = callback;
    }

    public void setDeviceList(List<BluetoothDevice> deviceList) {
        this.deviceList = deviceList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BluetoothDeviceItemBinding binding = BluetoothDeviceItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (ActivityCompat.checkSelfPermission(holder.binding.getRoot().getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            callback.onRequestPermission(Manifest.permission.BLUETOOTH_CONNECT);
        }
        BluetoothDevice device = deviceList.get(position);
        holder.binding.bluetoothDeviceName.setText(device.getName());
        holder.binding.bluetoothDeviceMac.setText(device.getAddress());
        holder.itemView.setOnClickListener(view -> {
            callback.onItemClickListener(device);
        });
    }

    @Override
    public int getItemCount() {
        return deviceList == null ? 0 : deviceList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface Callback {
        void onRequestPermission(String permission);

        void onItemClickListener(BluetoothDevice device);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        BluetoothDeviceItemBinding binding;

        public ViewHolder(@NonNull BluetoothDeviceItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
