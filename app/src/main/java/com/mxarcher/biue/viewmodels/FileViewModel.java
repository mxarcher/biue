package com.mxarcher.biue.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.mxarcher.biue.service.web.ServiceGenerator;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/26 15:28
 * @Description:
 */


// TODO: 通过room保存文件是否已经上传了
// 参考 https://www.bilibili.com/video/BV1ct411K7tp
public class FileViewModel extends AndroidViewModel {
    private static final String TAG = "FileViewModel";
    private static File dir;
    private final MutableLiveData<List<String>> liveData = new MutableLiveData<>();

    public FileViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<String>> getLiveData() {
        if (dir == null) {
            dir = getApplication().getFilesDir();
        }
        // 更新数据
        getFileInfoList();
        return liveData;
    }

    public void getFileInfoList() {
        new Thread(() -> {
            List<String> fileInfoList = Arrays.asList(getApplication().fileList());
            liveData.postValue(fileInfoList);
        }).start();
    }

    public boolean deleteFile(String filename) {
        boolean ret = getApplication().deleteFile(filename);
        if (ret) {
            getFileInfoList();
        }
        return ret;
    }

    /*
        private void writeFile() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss", Locale.CHINA);
            String x = sdf.format(new Date());
            try {
                FileOutputStream write = new FileOutputStream(new File(dir, x));
                write.write(x.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    */
    private String createFileNameByDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss", Locale.CHINA);
        return sdf.format(new Date());
    }

    private void upLoad(String filename) {
        File file = new File(filename);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        Observable<ResponseBody> x = ServiceGenerator.getUploadApiInstance().uploadFile(body);
        x.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull ResponseBody responseBody) {
                        Log.d(TAG, "onNext: ");
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
