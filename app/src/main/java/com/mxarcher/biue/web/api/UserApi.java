package com.mxarcher.biue.web.api;

import com.mxarcher.biue.models.User;
import com.mxarcher.biue.web.ReqBody;
import com.mxarcher.biue.web.RespBody;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/23 11:32
 * @Description:
 */
public interface UserApi {
    String remote = "/api/v1/user";

    @GET(remote)
    Observable<RespBody<User>> GetUser();

    @POST(remote)
    Observable<RespBody<Boolean>> AddUser(@Body ReqBody<User> body);

    @PUT(remote)
    Observable<RespBody<Boolean>> UpdateUser(@Body ReqBody<User> body);

    @HTTP(method = "DELETE", path = remote, hasBody = true)
    Observable<Response<Void>> DeleteUser(@Body ReqBody<User> body);
}
