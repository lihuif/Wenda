package com.example.Test.async;

public enum EventType {
    LIKE(0), //点赞事件
    COMMENT(1),//评论事件
    LOGIN(2),//登录事件
    MAIL(3),//邮件事件
    FOLLOW(4),//关注事件
    UNFOLLOW(5);//取消关注事件

    private int value;
    EventType(int value){this.value = value;}
    public int getValue(){return value;}
}
