package com.mxarcher.biue.livedata;

import android.content.SharedPreferences;

import java.util.Map;

public class ConfigLiveData extends SharedPreferenceLiveData<Map<String, ?>> {

    public ConfigLiveData(SharedPreferences prefs, String key) {
        super(prefs, key);
        setValue(getValueFromPreferences(key));
    }

    @Override
    public Map<String, ?> getValueFromPreferences(String key) {
        return sharedPrefs.getAll();
    }
}
