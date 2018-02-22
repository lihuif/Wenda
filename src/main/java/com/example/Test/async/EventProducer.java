package com.example.Test.async;

import com.alibaba.fastjson.JSONObject;
import com.example.Test.util.JedisAdapter;
import com.example.Test.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 把事件推到队列中
 */
@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent (EventModel eventModel){ //将eventmodel序列化之后，插入到redis中
        try {
            String json = JSONObject.toJSONString(eventModel); //序列化
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key,json);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
