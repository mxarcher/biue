package com.mxarcher.biue;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
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
            NavigationUI.setupWithNavController(bottomNavigationView,controller);
        }
    }

}