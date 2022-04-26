package com.mxarcher.biue.models;

import com.google.gson.annotations.SerializedName;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/25 7:49
 * @Description:
 */
public class Handling {
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
    @SerializedName("algorithm")
    private String algorithm;
    @SerializedName("results")
    private String results;
    @SerializedName("path")
    private String path;
    @SerializedName("comments")
    private String comments;

    @Override
    public String toString() {
        return "Handling{" +
                "id=" + id +
                ", updated_at='" + updated_at + '\'' +
                ", created_at='" + created_at + '\'' +
                ", deleted_at='" + deleted_at + '\'' +
                ", name='" + name + '\'' +
                ", algorithm='" + algorithm + '\'' +
                ", results='" + results + '\'' +
                ", path='" + path + '\'' +
                ", comments='" + comments + '\'' +
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

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
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
}
