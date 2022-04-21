package com.mxarcher.biue.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

import com.mxarcher.biue.R;

public class SettingsFragment extends  PreferenceFragmentCompat{


    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        String config_file_name = getResources().getString(R.string.config_file_name);
        getPreferenceManager().setSharedPreferencesName(config_file_name);

        // 放在后面可以正常读取配置
        setPreferencesFromResource(R.xml.perference,rootKey);
    }
}