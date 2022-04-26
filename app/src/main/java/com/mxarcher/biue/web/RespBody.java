package com.mxarcher.biue.web;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/23 12:20
 * @Description:
 */
public class RespBody<T> {
    @SerializedName("code")
    Integer code;
    @SerializedName("data")
    data<T> data;

    public RespBody.data<T> getData() {
        return data;
    }

    public Integer getCode() {
        return code;
    }

    public static class data<U> {
        @SerializedName("count")
        Integer count;
        @SerializedName("body")
        List<U> body;

        public List<U> getBody() {
            return body;
        }

        public Integer getCount() {
            return count;
        }
    }
}

