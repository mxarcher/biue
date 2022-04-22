package com.mxarcher.biue.fragments;

import static android.net.InetAddresses.isNumericAddress;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.mxarcher.biue.R;
import com.mxarcher.biue.models.ConfigViewModel;

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
        // 如果ViewModel里没有配置shp,则表明没有初始化，进行初始化
        if (viewModel.isEmpty()) {
            SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
            viewModel.set(sharedPreferences);
        }
        EditTextPreference usernamePreference = findPreference(getResources().getString(R.string.key_operator_name));
        EditTextPreference ipPreference = findPreference(getResources().getString(R.string.key_cloud_ip));
        EditTextPreference portPreference = findPreference(getResources().getString(R.string.key_cloud_port));
        // 检验填写内容是否有效
        if (usernamePreference != null) {
            usernamePreference.setOnPreferenceChangeListener((preference, newValue) -> {
                Log.d(TAG, "onUsernamePreferenceChange: " + newValue.toString());
                if (newValue.toString().length() > 10) {
                    Toast.makeText(requireContext(), "请输入小于50字的用户名", Toast.LENGTH_SHORT).show();
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
                } else return true;
            });
        }
        if (portPreference != null) {
            // 设置仅允许输入数字
            portPreference.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED));
            portPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                Log.d(TAG, "onPortPreferenceChange: " + newValue.toString());
                int x = Integer.parseInt(newValue.toString());
                if (x <= 0 || x > 65535) {
                    Toast.makeText(requireContext(), "请输入合法的端口号", Toast.LENGTH_SHORT).show();
                    return false;
                } else return true;
            });
        }

    }
}