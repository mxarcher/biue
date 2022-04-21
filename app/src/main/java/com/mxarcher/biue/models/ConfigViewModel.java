package com.mxarcher.biue.models;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.mxarcher.biue.R;

/**
 * @author MXArcher Lee
 * @date 2022/4/20
 */


// Subclasses must have a constructor which accepts Application as the only parameter.
// 只要类是继承自AndroidViewModel，在类里就可以直接访问getApplication
public class ConfigViewModel extends AndroidViewModel {
    String key = getApplication().getResources().getString(R.string.username_key);
    String config = getApplication().getResources().getString(R.string.config_file_name);
    private MutableLiveData<String> username = new MutableLiveData<>();

    public ConfigViewModel(@NonNull Application application) {
        super(application);
    }


    public String getUsername() {
        if (username.getValue() == null) {
            SharedPreferences shp = getApplication().getSharedPreferences(config, Context.MODE_PRIVATE);
            String name = shp.getString(key, null);
            username.setValue(name);
        }
        return username.getValue();
    }

    public void setUsername(String username) {
        SharedPreferences.Editor editor = getApplication().getSharedPreferences(config, Context.MODE_PRIVATE).edit();
        editor.putString(key, username);
        editor.apply();
        this.username.setValue(username);
    }

}
