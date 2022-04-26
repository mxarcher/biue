package com.mxarcher.biue.views;

import static android.net.InetAddresses.isNumericAddress;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.mxarcher.biue.R;
import com.mxarcher.biue.viewmodels.ConfigViewModel;
import com.mxarcher.biue.web.ServiceGenerator;

public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String TAG = "SettingsFragment";
    ConfigViewModel viewModel;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // 修改配置文件名，便于其他fragment访问ViewModel
        getPreferenceManager().setSharedPreferencesName(getResources().getString(R.string.config_file_name));
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ConfigViewModel.class);

        EditTextPreference usernamePreference = findPreference(getResources().getString(R.string.key_operator_name));
        EditTextPreference ipPreference = findPreference(getResources().getString(R.string.key_cloud_ip));
        EditTextPreference portPreference = findPreference(getResources().getString(R.string.key_cloud_port));
        // 检验填写内容是否有效
        if (usernamePreference != null) {
            usernamePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                Log.d(TAG, "onUsernamePreferenceChange: " + newValue.toString());
                if (newValue.toString().length() > 20) {
                    Toast.makeText(requireContext(), "请输入小于20字的用户名", Toast.LENGTH_SHORT).show();
                    return false;
                } else return true;

            });
        }
        if (ipPreference != null) {
            ipPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                Log.d(TAG, "onIPPreferenceChange: " + newValue.toString());
                if (!isNumericAddress(newValue.toString())) {
                    Toast.makeText(requireContext(), "请输入合法的ip地址", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    String port_key = getString(R.string.key_cloud_port);
                    String x = viewModel.get(port_key);
                    x = x == null ? "8080" : x;
                    String url = "http://" + newValue + ":" + x;
                    ServiceGenerator.setBaseUrl(url);
                    return true;
                }
            });
        }
        if (portPreference != null) {
            // 设置仅允许输入数字
            portPreference.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED));
            portPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                Log.d(TAG, "onPortPreferenceChange: " + newValue);
                int v = Integer.parseInt(newValue.toString());
                if (v <= 0 || v > 65535) {
                    Toast.makeText(requireContext(), "请输入合法的端口号", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    String ip_key = getString(R.string.key_cloud_ip);
                    String x = viewModel.get(ip_key);
                    x = x == null ? "127.0.0.1" : x;
                    String url = "http://" + x + ":" + newValue;
                    ServiceGenerator.setBaseUrl(url);
                    Log.d(TAG, "onViewCreated: " + ServiceGenerator.getBaseUrl());
                    return true;
                }
            });
        }

    }
}