package com.example.Test.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.example.Test.async.EventHandler;
import com.example.Test.async.EventModel;
import com.example.Test.async.EventType;
import com.example.Test.model.EntityType;
import com.example.Test.model.Feed;
import com.example.Test.model.Question;
import com.example.Test.model.User;
import com.example.Test.service.FeedService;
import com.example.Test.service.FollowService;
import com.example.Test.service.QuestionService;
import com.example.Test.service.UserService;
import com.example.Test.util.JedisAdapter;
import com.example.Test.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FeedHandler implements EventHandler{
    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    @Autowired
    FeedService feedService;

    @Autowired
    JedisAdapter jedisAdapter;

    @Autowired
    QuestionService questionService;

    /**
     * 提取评论内容
     * @param model
     * @return
     */
    public String buildFeedData(EventModel model){
        Map<String,String> map = new HashMap<String, String>();
        //触发的用户
        User actor = userService.getUser(model.getActorId());
        if (actor == null){ //异常
            return null;
        }
        map.put("userId", String.valueOf(actor.getId()));
        map.put("userHead", actor.getHeadUrl());
        map.put("userName", actor.getName());

        if (model.getType()==EventType.COMMENT || //如果发生了一个评论
                model.getType()==EventType.FOLLOW && model.getEntityType()== EntityType.ENTITY_QUESTION){ //或者关注了一个问题
            Question question = questionService.getById(model.getEntityId());
            if (question == null){
                return null;
            }
            map.put("questionId", String.valueOf(question.getId()));
            map.put("questionTitle", question.getTitle());
            return JSONObject.toJSONString(map);
        }
        return null;
    }

    @Override
    public void doHandle(EventModel model) {
        //构造一个新鲜事
        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setUserId(model.getActorId());//触发者
        feed.setType(model.getType().getValue());//新鲜事类型
        feed.setData(buildFeedData(model));
        if (feed.getData()==null){
            //不支持的feed
            return; //不处理
        }
        feedService.addFeed(feed); //写入数据库

        //获得所有粉丝
        List<Integer> followers = followService.getFollowers(EntityType.ENTITY_USER,  model.getActorId(), Integer.MAX_VALUE);
        //系统队列
        followers.add(0);
        for (int follower : followers) { //对于所有的粉丝
            String timelineKey = RedisKeyUtil.getTimelineKey(follower);
            jedisAdapter.lpush(timelineKey, String.valueOf(feed.getId()));
            // 限制最长长度，如果timelineKey的长度过大，就删除后面的新鲜事
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.COMMENT, EventType.FOLLOW});
    }
}
