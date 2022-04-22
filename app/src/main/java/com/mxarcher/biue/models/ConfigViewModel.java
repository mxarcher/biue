package com.mxarcher.biue.models;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.mxarcher.biue.R;
import com.mxarcher.biue.livedata.ConfigLiveData;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/20
 */


// Subclasses must have a constructor which accepts Application as the only parameter.
// 只要类是继承自AndroidViewModel，在类里就可以直接访问getApplication
public class ConfigViewModel extends AndroidViewModel {
    final String config_file_name = getApplication().getResources().getString(R.string.config_file_name);
    SharedPreferences sharedPreferences;
    private ConfigLiveData configLiveData;

    public ConfigViewModel(@NonNull Application application) {
        super(application);
    }

    public String get(String key) {
        if (configLiveData == null) return null;
        if(configLiveData.getValue()==null) return null;
        return (String) configLiveData.getValue().getOrDefault(key,null);
    }

    public void set(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        if(configLiveData == null){
            configLiveData = new ConfigLiveData(sharedPreferences,config_file_name);
        }
    }
    public boolean isEmpty(){
        return configLiveData == null;
    }

    public ConfigLiveData getConfigLiveData() {
        return configLiveData;
    }
}
