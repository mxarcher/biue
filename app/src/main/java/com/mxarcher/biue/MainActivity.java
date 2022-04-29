package com.mxarcher.biue;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mxarcher.biue.viewmodels.ConfigViewModel;
import com.mxarcher.biue.service.web.ServiceGenerator;

public class MainActivity extends AppCompatActivity {
    ConfigViewModel configViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getWindow().setFormat(PixelFormat.TRANSPARENT);

        // 初始化各个viewModel的实例 以及servicegenerator

        configViewModel = new ViewModelProvider(this).get(ConfigViewModel.class);
        String config_file_name = getResources().getString(R.string.config_file_name);
        SharedPreferences sharedPreferences = getSharedPreferences(config_file_name, Context.MODE_PRIVATE);
        configViewModel.setSharedPreferences(sharedPreferences);
        String ip_key = getString(R.string.key_cloud_ip);
        String port_key = getString(R.string.key_cloud_port);
        // 由于userViewModel调用了serviceGenerator的函数，但此时serviceGenerator还没有初始化，因此不能通过configViewModel获取数据，只能通过sharedPreference获取
        // 也因此userViewModel需要在serviceGenerator后面进行初始化
        String url = "http://" + sharedPreferences.getString(ip_key, "127.0.0.1") + ":" + sharedPreferences.getString(port_key, "8080");
        ServiceGenerator.setBaseUrl(url);




/*
         不建议直接使用this,可能会导致内存泄漏
         getApplicationContext 指向全局的，只要应用程序存在，就会一致存在的数据
         可以拥有获得全局资源的能力
*/


        // 设置默认界面为蓝牙所在界面
//        if( savedInstanceState == null){
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.blueToothFragment, new BlueToothFragment())
//                    .commit();
//        }

//        ActionBar actionBar = getSupportActionBar();
//        if(actionBar!=null){
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }

        // 修改左上角名称
        // 另一个传之前设置的host
        // url: https://developer.android.com/guide/navigation/navigation-getting-started#navigate
        // 根据navigation中设置的名称显示
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        NavController controller;
        if (navHostFragment != null) {
            controller = navHostFragment.getNavController();
            AppBarConfiguration appBarConfiguration =
                    new AppBarConfiguration.Builder(bottomNavigationView.getMenu()).build();
            NavigationUI.setupActionBarWithNavController(this, controller, appBarConfiguration);
            NavigationUI.setupWithNavController(bottomNavigationView, controller);
        }
    }
}