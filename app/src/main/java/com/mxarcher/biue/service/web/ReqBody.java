package com.mxarcher.biue.service.web;
import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class ReqBody<T> {
    @SerializedName("operator")
    String operator;
    @SerializedName("model")
    T model;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    @NonNull
    @Override
    public String toString() {
        return "ReqBody{" +
                "operator='" + operator + '\'' +
                ", model=" + model +
                '}';
    }
}
