package com.mxarcher.biue.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.preference.PreferenceFragmentCompat;

import com.mxarcher.biue.R;
import com.mxarcher.biue.models.ConfigViewModel;

public class SettingsFragment extends PreferenceFragmentCompat {
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
        if(viewModel.isEmpty()){
            SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
            viewModel.set(sharedPreferences);
        }
        // 设置填写内容的事件
    }
}