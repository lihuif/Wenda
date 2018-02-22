package com.example.Test.async.handler;

import com.example.Test.async.EventHandler;
import com.example.Test.async.EventModel;
import com.example.Test.async.EventType;
import com.example.Test.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LoginExceptionHandler implements EventHandler{

    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        // xxxx判断发现这个用户登陆异常
        if (model.getExt("username") == "xx"){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("username", model.getExt("username"));
            mailSender.sendWithHTMLTemplate(model.getExt("email"), "登陆IP异常",  map);
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
