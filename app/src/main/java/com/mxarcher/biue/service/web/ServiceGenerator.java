package com.mxarcher.biue.service.web;

import android.util.Log;

import com.mxarcher.biue.service.web.api.CollectionApi;
import com.mxarcher.biue.service.web.api.HandlingApi;
import com.mxarcher.biue.service.web.api.LogApi;
import com.mxarcher.biue.service.web.api.UploadApi;
import com.mxarcher.biue.service.web.api.UserApi;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/23 12:49
 * @Description:
 */
// 需要先设置url在获取api实例
public class ServiceGenerator {
    private static final String TAG = "ServiceGenerator";
    private static String baseUrl;
    //retrofit 必须要提供baseUrl 不然会直接报错
    private static Retrofit retrofit;

    public static UserApi getUserApiInstance() {
        return retrofit.create(UserApi.class);
    }

    public static void setBaseUrl(String baseUrl) {
        Log.d(TAG, "setBaseUrl: "+baseUrl);
        ServiceGenerator.baseUrl = baseUrl;
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    public static CollectionApi getCollectionApiInstance() {
        return retrofit.create(CollectionApi.class);
    }

    public static HandlingApi getHandleApiInstance() {
        return retrofit.create(HandlingApi.class);
    }

    public static LogApi getLogApiInstance() {
        return retrofit.create(LogApi.class);
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static UploadApi getUploadApiInstance() {
        return retrofit.create(UploadApi.class);
    }
}
