package com.mxarcher.biue.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.mxarcher.biue.models.User;
import com.mxarcher.biue.web.ReqBody;
import com.mxarcher.biue.web.RespBody;
import com.mxarcher.biue.web.ServiceGenerator;
import com.mxarcher.biue.web.api.UserApi;

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
 * @Date: 2022/4/23 14:35
 * @Description:
 */

public class UserRepository {
    private static final String TAG = "UserRepository";

    private static final UserRepository instance = new UserRepository();
    private static UserApi api;
    // LiveData不建议放到static中
    private MutableLiveData<List<User>> userList = new MutableLiveData<>();


    public UserRepository() {
        Log.d(TAG, "UserRepository: enter");
        api = ServiceGenerator.getUserApiInstance();
    }

    public static UserRepository getInstance() {
        return instance;
    }

    public MutableLiveData<List<User>> getUserList() {
        Log.d(TAG, "getUserList: " + api);
        Observable<RespBody<User>> respsList = api.GetUser();
        respsList.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RespBody<User>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        // 初始化工作
                    }

                    @Override
                    public void onNext(@NonNull RespBody<User> userRespBody) {
                        // 请求成功后的操作
                        Log.d(TAG, "onNext: " + userRespBody.getData().getBody().toString());
                        Log.d(TAG, "onNext: " + userRespBody.getCode());
                        userList.postValue(userRespBody.getData().getBody());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        Log.d(TAG, "onError: 请求失败 " + e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: 请求成功");
                    }
                });

        return userList;
    }

    public void addUser(ReqBody<User> reqBody) {
        Observable<RespBody<Boolean>> respBody = api.AddUser(reqBody);
        respBody.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RespBody<Boolean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull RespBody<Boolean> booleanRespBody) {
                        Log.d(TAG, "onNext: ");
                        //添加完成后更新用户信息
                        getUserList();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "add onError: ");

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "add onComplete: ");

                    }
                });
    }
    public void updateUser(ReqBody<User> reqBody){
        Observable<RespBody<Boolean>> x = api.UpdateUser(reqBody);
        x.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RespBody<Boolean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull RespBody<Boolean> booleanRespBody) {
                        getUserList();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void deleteUser(ReqBody<User> reqBody) {
        Observable<Response<Void>> x = api.DeleteUser(reqBody);
        x.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Response<Void> voidResponse) {
                        Log.d(TAG, "onDeleteNext: ");
                        getUserList();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        Log.d(TAG, "onDeleteError: ");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onDeleteComplete: ");
                    }
                });
    }
}
