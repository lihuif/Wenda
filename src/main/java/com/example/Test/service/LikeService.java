package com.example.Test.service;

import com.example.Test.util.JedisAdapter;
import com.example.Test.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;

    public long getLikeCount(int entityType, int entityId){ //获得赞的数量
        String likekey = RedisKeyUtil.getLikeKey(entityType, entityId);
        return jedisAdapter.scard(likekey);
    }

    public int getLikeStatus(int userId, int entityType, int entityId){ //用户踩、赞之后的加亮状态
        String likekey = RedisKeyUtil.getLikeKey(entityType, entityId);
        if (jedisAdapter.sismember(likekey, String.valueOf(userId))){
            return 1;
        }
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        return jedisAdapter.sismember(disLikeKey, String.valueOf(userId))? -1:0;
    }

    public long like (int userId, int entityType, int entityId){   //点赞操作
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.sadd(likeKey, String.valueOf(userId));

        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);//将踩清空
        jedisAdapter.srem(disLikeKey, String.valueOf(userId));

        return jedisAdapter.scard(likeKey);
    }

    public long disLike (int userId, int entityType, int entityId){
        String dislikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdapter.sadd(dislikeKey, String.valueOf(userId));

        String LikeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.srem(LikeKey, String.valueOf(userId));

        return jedisAdapter.scard(dislikeKey);
    }
}
