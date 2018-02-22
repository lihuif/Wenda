package com.example.Test.async.handler;

import com.example.Test.async.EventHandler;
import com.example.Test.async.EventModel;
import com.example.Test.async.EventType;
import com.example.Test.model.Message;
import com.example.Test.model.User;
import com.example.Test.service.MessageService;
import com.example.Test.service.UserService;
import com.example.Test.util.TestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class LikeHandler implements EventHandler{
    @Autowired
    MessageService messageService; //要发一个站内信

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) { //处理函数
        Message message = new Message();
        message.setFromId(TestUtil.SYSTEM_USERID); //发送方：管理员
        message.setToId(model.getEntityOwnerId()); //接收方：被点赞客户的ID
        message.setCreatedDate(new Date()); //创建时间
        User user = userService.getUser(model.getActorId()); //触发者用户，即点赞的那个人
        message.setContent("用户" + user.getName() + "赞了你的评论，http://127.0.0.1:8080/question/" + model.getExt("questionId"));
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() { //只关注like的事件
        return Arrays.asList(EventType.LIKE);
    }
}
