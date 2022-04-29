package com.mxarcher.biue.service.web.api;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/26 22:51
 * @Description:
 */
public interface UploadApi {
    @Multipart
    @POST("/api/v1/upload")
    Observable<ResponseBody> uploadFile(@Part MultipartBody.Part file);

}
