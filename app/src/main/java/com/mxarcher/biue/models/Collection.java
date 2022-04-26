package com.mxarcher.biue.models;

import com.google.gson.annotations.SerializedName;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/25 7:49
 * @Description:
 */
public class Collection {
    @SerializedName("id")
    private int id;
    @SerializedName("updated_at")
    private String updated_at;
    @SerializedName("created_at")
    private String created_at;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @SerializedName("deleted_at")
    private String deleted_at;
    @SerializedName("user_id")
    private int uid;
    @SerializedName("name")
    private String name;
    @SerializedName("program")
    private String program;
    @SerializedName("dataset")
    private String dataset;
    @SerializedName("location")
    private String location;
    @SerializedName("path")
    private String path;
    @SerializedName("comments")
    private String comments;

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

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "id=" + id +
                ", updated_at='" + updated_at + '\'' +
                ", created_at='" + created_at + '\'' +
                ", deleted_at='" + deleted_at + '\'' +
                ", name='" + name + '\'' +
                ", program='" + program + '\'' +
                ", dataset='" + dataset + '\'' +
                ", location='" + location + '\'' +
                ", path='" + path + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }
}
