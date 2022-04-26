package com.mxarcher.biue.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.mxarcher.biue.models.Collection;
import com.mxarcher.biue.web.ReqBody;
import com.mxarcher.biue.web.RespBody;
import com.mxarcher.biue.web.ServiceGenerator;
import com.mxarcher.biue.web.api.CollectionApi;

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
public class CollectionRepository {

    private static final String TAG = "CollectionRepository";
    private static final CollectionRepository instance = new CollectionRepository();
    private static CollectionApi api;
    private final MutableLiveData<List<Collection>> collectionList = new MutableLiveData<>();

    public CollectionRepository() {
        Log.d(TAG, "CollectionRepository: enter");
        api = ServiceGenerator.getCollectionApiInstance();
    }

    // 通过getInstance可以保证实例唯一
    public static CollectionRepository getInstance() {
        return instance;
    }

    public MutableLiveData<List<Collection>> getCollectionList() {
        Log.d(TAG, "getCollectionList: " + api);
        Observable<RespBody<Collection>> respsList = api.GetCollection();
        respsList.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RespBody<Collection>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull RespBody<Collection> collectionRespBody) {
                        Log.d(TAG, "onNext: " + collectionRespBody.getData().getBody());
                        Log.d(TAG, "onNext: " + collectionRespBody.getCode());
                        collectionList.postValue(collectionRespBody.getData().getBody());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        collectionList.postValue(null);
                        Log.d(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: ");

                    }
                });
        return collectionList;
    }

    public void addCollection(ReqBody<Collection> reqBody) {
        Observable<RespBody<Boolean>> respBody = api.AddCollection(reqBody);
        respBody.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RespBody<Boolean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull RespBody<Boolean> booleanRespBody) {
                        Log.d(TAG, "onNext: ");
                        getCollectionList();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: ");
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void deleteCollection(ReqBody<Collection> reqBody) {

        Observable<Response<Void>> respBody = api.DeleteCollection(reqBody);
        respBody.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<Void>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Response<Void> voidResponse) {
                        getCollectionList();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
