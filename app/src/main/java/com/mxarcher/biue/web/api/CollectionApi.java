package com.mxarcher.biue.web.api;

import com.mxarcher.biue.models.Collection;
import com.mxarcher.biue.web.ReqBody;
import com.mxarcher.biue.web.RespBody;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/23 11:48
 * @Description:
 */
public interface CollectionApi {
    String remote = "/api/v1/collection";

    @GET(remote)
    Observable<RespBody<Collection>> GetCollection();

    @POST(remote)
    Observable<RespBody<Boolean>> AddCollection(@Body ReqBody<Collection> body);

    @PUT(remote)
    Observable<RespBody<Boolean>> UpdateCollection(@Body ReqBody<Collection> body);

    @HTTP(method = "DELETE", path = remote, hasBody = true)
    Observable<Response<Void>> DeleteCollection(@Body ReqBody<Collection> body);
}
