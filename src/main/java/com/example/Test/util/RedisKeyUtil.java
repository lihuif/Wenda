package com.example.Test.util;

/**
 * 生成Redis的key的方法
 */
public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE"; //赞
    private static String BIZ_DISLIKE = "DISLIKE";//踩
    private static String BIZ_EVENTQUEUE = "EVENTQUEUE";//消息队列
    //粉丝
    private static String BIZ_FOLLOWER = "FOLLOWER";
    //关注对象
    private static String BIZ_FOLLOWEE = "FOLLOWEE";
    private static String BIZ_TIMELINE = "TIMELINE";

    public static String getLikeKey(int entityType, int entityID){
        return BIZ_LIKE + SPLIT +String.valueOf(entityType) + SPLIT + String.valueOf(entityID);
    }

    public static String getDisLikeKey(int entityType, int entityID){
        return BIZ_DISLIKE + SPLIT +String.valueOf(entityType) + SPLIT + String.valueOf(entityID);
    }

    public static String getEventQueueKey(){
        return BIZ_EVENTQUEUE;
    }

    public static String getFollowerKey(int entityType, int entityId){ //粉丝，每个实体都有一条自己的队列
        return BIZ_FOLLOWER + SPLIT + String.valueOf(entityType) +String.valueOf(entityId);//唯一一个实体的粉丝
    }

    public static String getFolloweeKey(int userId, int entityType){ //关注列表，某一个用户关注某一个实体的key。例如，某一用户关注的所有问题
        return BIZ_FOLLOWEE + SPLIT + String.valueOf(userId) +String.valueOf(entityType);//唯一一个实体的粉丝
    }

    public static String getTimelineKey(int userId) {
        return BIZ_TIMELINE + SPLIT + String.valueOf(userId);
    }

}
