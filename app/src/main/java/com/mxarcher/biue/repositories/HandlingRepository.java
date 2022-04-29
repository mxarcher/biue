package com.mxarcher.biue.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.mxarcher.biue.models.Handling;
import com.mxarcher.biue.service.web.ReqBody;
import com.mxarcher.biue.service.web.RespBody;
import com.mxarcher.biue.service.web.ServiceGenerator;
import com.mxarcher.biue.service.web.api.HandlingApi;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Response;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/25 7:49
 * @Description:
 */
public class HandlingRepository {
    private static final String TAG = "HandlingRepository";
    private static final HandlingRepository instance = new HandlingRepository();
    private static HandlingApi api;
    private final MutableLiveData<List<Handling>> handlingList = new MutableLiveData<>();

    public HandlingRepository() {
        api = ServiceGenerator.getHandleApiInstance();
    }

    public static HandlingRepository getInstance() {
        return instance;
    }

    public MutableLiveData<List<Handling>> getHandlingList() {
        Log.d(TAG, "getHandlingList: " + api);
        Observable<RespBody<Handling>> respsList = api.GetHandling();
        respsList.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RespBody<Handling>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull RespBody<Handling> handlingRespBody) {
                        Log.d(TAG, "onNext: " + handlingRespBody.getData().getBody());
                        Log.d(TAG, "onNext: " + handlingRespBody.getCode());
                        handlingList.postValue(handlingRespBody.getData().getBody());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        Log.d(TAG, "onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });
        return handlingList;
    }

    public void addHandling(ReqBody<Handling> reqBody) {
        Observable<RespBody<Boolean>> respBody = api.AddHandling(reqBody);
        respBody.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RespBody<Boolean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull RespBody<Boolean> booleanRespBody) {
                        getHandlingList();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");
                    }
                });
    }

    public void updateHandling(ReqBody<Handling> reqBody) {
        Observable<RespBody<Boolean>> x = api.UpdateHandling(reqBody);
        x.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RespBody<Boolean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull RespBody<Boolean> booleanRespBody) {
                        getHandlingList();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void deleteHandling(ReqBody<Handling> reqBody) {
        Observable<Response<Void>> x = api.DeleteHandling(reqBody);
        x.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Response<Void> voidResponse) {
                        Log.d(TAG, "onDeleteNext: ");
                        getHandlingList();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        Log.d(TAG, "onDeleteError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onDeleteComplete: ");
                    }
                });
    }
}
