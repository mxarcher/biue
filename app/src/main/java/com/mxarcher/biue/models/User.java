package com.mxarcher.biue.models;

import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/23 10:19
 * @Description:
 */

@Entity
public class User {
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
    @SerializedName("sex")
    private boolean sex;
    @SerializedName("height")
    private float height;
    @SerializedName("weight")
    private float weight;
    @SerializedName("comments")
    private String comments;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", updated_at='" + updated_at + '\'' +
                ", created_at='" + created_at + '\'' +
                ", deleted_at='" + deleted_at + '\'' +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", height=" + height +
                ", weight=" + weight +
                ", comments='" + comments + '\'' +
                '}';
    }
}
