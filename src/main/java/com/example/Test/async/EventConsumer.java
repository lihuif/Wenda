package com.example.Test.async;

import com.alibaba.fastjson.JSON;
import com.example.Test.util.JedisAdapter;
import com.example.Test.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 把事件取出来
 * 找到关联的Handler
 * 调用Handler来进行处理
 */
//事件消费者

@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware{
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    private Map<EventType, List<EventHandler>> config = new HashMap<EventType, List<EventHandler>>();
    private ApplicationContext applicationContext;

    @Autowired
    JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception { //初始化
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);//找出所有实现eventhandler的类
        if (beans!=null){ //如果存在
            for (Map.Entry<String, EventHandler>entry : beans.entrySet()){ //遍历这些类
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes(); //关注了哪几个事件类型
                for (EventType type: eventTypes){ //遍历事件类型
                    if (!config.containsKey(type)){
                        logger.info(type.toString());
                        config.put(type, new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue()); //添加事件类型对应的handler
                }//for
            }//for
        }//if
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    String key = RedisKeyUtil.getEventQueueKey();
                    List<String> events = jedisAdapter.brpop(0,key); //没有元素在队列时，一直阻塞
                    for (String message: events){ //队列里存储的是eventmodel
                        if (message.equals(key)){
                            continue;
                        }
                        EventModel eventModel = JSON.parseObject(message, EventModel.class);//对EventProducer推入队列的对象进行反序列化
                        if (!config.containsKey(eventModel.getType())){ //如果这个model对应的事件类型不存在
                            logger.error("不能识别的事件" + eventModel.getType());
                            continue;
                        }
                        for (EventHandler handler: config.get(eventModel.getType())){
                            handler.doHandle(eventModel);
                        }
                    }//for
                }
            }
        });
        thread.start();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
