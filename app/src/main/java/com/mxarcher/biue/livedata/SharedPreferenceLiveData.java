package com.mxarcher.biue.livedata;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;

/**
 * 参考链接: https://stackoverflow.com/questions/50649014/livedata-with-shared-preferences
 *
 * @Description: 由于配置文件只需要读就行，因此继承LiveData而不是MutableLiveData
 */
public abstract class SharedPreferenceLiveData<T> extends LiveData<T> {
    SharedPreferences sharedPrefs;
    String key;
    private final SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = (sharedPreferences, key) -> {
        if (SharedPreferenceLiveData.this.key.equals(key)) {
            setValue(getValueFromPreferences(key));
        }
    };

    public SharedPreferenceLiveData(SharedPreferences prefs, String key) {
        this.sharedPrefs = prefs;
        this.key = key;
    }

    public abstract T getValueFromPreferences(String key);

    @Override
    protected void onActive() {
        super.onActive();
        setValue(getValueFromPreferences(key));
        sharedPrefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }


    @Override
    protected void onInactive() {
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
        super.onInactive();
    }

}

