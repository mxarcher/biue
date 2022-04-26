package com.mxarcher.biue.models;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/25 22:35
 * @Description:
 */
public interface AdapterCallback<T> {

    public void onEdit(T t);

    public void onDelete(T t);

}
