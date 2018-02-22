package com.example.Test.service;

import com.example.Test.util.JedisAdapter;
import com.example.Test.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class FollowService {
    //引入resids
    @Autowired
    JedisAdapter jedisAdapter;

    //关注
    public boolean follow(int userId, int entityType, int entityId ){ //发起者是人，被关注的不一定是人
        Date date = new Date();
        //1、把被关注的对象放入用户的关注列表里面
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);
        tx.zadd(followerKey, date.getTime(), String.valueOf(userId));
        //2、把用户放在实体的粉丝列表里面
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        tx.zadd(followeeKey, date.getTime(), String.valueOf(entityId));

        List<Object> ret = jedisAdapter.exec(tx, jedis);//执行事务
        return ret.size()==2 && (Long)ret.get(0)>0 && (Long)ret.get(1)>0;
    }

    //取消关注
    public boolean unfollow(int userId, int entityType, int entityId ){
        Date date = new Date();
        //1、把被关注的对象从用户的关注列表里面移除
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        Jedis jedis = jedisAdapter.getJedis();
        Transaction tx = jedisAdapter.multi(jedis);
        tx.zrem(followerKey, String.valueOf(userId));
        //2、把用户放从实体的粉丝列表里面移除
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        tx.zrem(followeeKey, String.valueOf(entityId));

        List<Object> ret = jedisAdapter.exec(tx, jedis);//执行事务
        return ret.size()==2 && (Long)ret.get(0)>0 && (Long)ret.get(1)>0;
    }

    private List<Integer> getIdsFromSet(Set<String> idset){
        List<Integer> ids = new ArrayList<>();
        for (String str:idset){
            ids.add(Integer.parseInt(str));
        }
        return ids;
    }

    //获取一个实体所有的粉丝
    public List<Integer> getFollowers (int entityType, int entityId, int count){
        String followerkey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerkey, 0, count));
    }

    //获取一个实体部分的粉丝
    public List<Integer> getFollowers (int entityType, int entityId, int offset,  int count){
        String followerkey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerkey, offset, count));
    }

    //获取一个用户所有的关注列表
    public List<Integer> getFollowees (int userId, int entityType, int count){
        String followeekey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followeekey, 0, count));
    }

    //获取一个用户部分的关注列表
    public List<Integer> getFollowees (int userId, int entityType, int offset, int count){
        String followeekey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followeekey, offset, count));
    }

    //一个实体有多少的粉丝(关注者)
    public long getFollowerCount(int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return jedisAdapter.zcard(followerKey);
    }

    //一个用户关注列表的实体数量
    public long getFolloweeCount(int userId, int entityType) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return jedisAdapter.zcard(followeeKey);
    }

    /**
     *  判断用户是否关注了某个实体
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    public boolean isFollower (int userId, int entityType, int entityId){ //查一个实体的粉丝里是否有某用户
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return jedisAdapter.zscore(followerKey, String.valueOf(userId))!=null;
    }
}
