package com.example.Test.async;

import java.util.List;

//对事件类型的处理方法
public interface EventHandler {
    void doHandle(EventModel model);//处理方法
    List<EventType> getSupportEventTypes(); //这个handler关注了哪几个事件类型
}
