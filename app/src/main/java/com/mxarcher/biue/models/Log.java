package com.mxarcher.biue.models;

import com.google.gson.annotations.SerializedName;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/25 8:01
 * @Description:
 */
public class Log {
    @SerializedName("id")
    private int id;
    @SerializedName("updated_at")
    private String updated_at;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("deleted_at")
    private String deleted_at;
    @SerializedName("name")
    private String name;
    @SerializedName("operation")
    private String operation;
    @SerializedName("table")
    private String table;
    @SerializedName("tid")
    private int tid;

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", updated_at='" + updated_at + '\'' +
                ", created_at='" + created_at + '\'' +
                ", deleted_at='" + deleted_at + '\'' +
                ", name='" + name + '\'' +
                ", operation='" + operation + '\'' +
                ", table='" + table + '\'' +
                ", tid=" + tid +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }
}
