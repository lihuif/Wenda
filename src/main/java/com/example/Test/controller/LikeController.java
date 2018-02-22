package com.example.Test.controller;

import com.example.Test.async.EventModel;
import com.example.Test.async.EventProducer;
import com.example.Test.async.EventType;
import com.example.Test.model.Comment;
import com.example.Test.model.EntityType;
import com.example.Test.model.HostHolder;
import com.example.Test.service.CommentService;
import com.example.Test.service.LikeService;
import com.example.Test.service.QuestionService;
import com.example.Test.service.UserService;
import com.example.Test.util.TestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {

    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @Autowired
    LikeService likeService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(value = "/like", method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null){
            return TestUtil.getJSONString(999); //999表示没登录
        }
        Comment comment = commentService.getCommentById(commentId);

        eventProducer.fireEvent(new EventModel(EventType.LIKE)          //触发事件
                .setActorId(hostHolder.getUser().getId()).setEntityId(commentId)
                .setEntityType(EntityType.ENTITY_COMMENT).setEntityOwnerId(comment.getUserId())
                .setExt("questionId", String.valueOf(comment.getEntityId())));

        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId); //点赞数获取
        return TestUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null) {
            return TestUtil.getJSONString(999);
        }

        long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return TestUtil.getJSONString(0, String.valueOf(likeCount));
    }
}
