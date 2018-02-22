package com.example.Test.model;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

/**
 * 新鲜事
 */
public class Feed {
    private int id;
    private int type; //不同的类型
    private int userId; //产生新鲜事的人
    private Date createdDate; //新鲜事产生时间
    //JSON
    private String data;//新鲜事数据
    private JSONObject dataJSON = null;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        dataJSON = JSONObject.parseObject(data);
    }
    public String get(String key) {
        return dataJSON == null ? null : dataJSON.getString(key);
    }
}
