package com.example.Test.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.Test.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public class JedisAdapter implements InitializingBean{

    private JedisPool pool;
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    public static void print(int index, Object obj){
        System.out.println(String.format("%d, %s", index, obj.toString()));
    }

    public static void main (String[] args){
        Jedis jedis = new Jedis("redis://localhost:6379/9");
        jedis.set("Hello", "world");
        print(1,jedis.get("Hello"));
/*        jedis.flushDB(); //删除数据

        jedis.set("Hello", "world"); //设置k-v
        jedis.rename("Hello", "newhello");
        print(1,jedis.get("newhello"));
        jedis.setex("hello2",15, "world");*/ //超时时间

        /*jedis.set("pv", "100");
        jedis.incr("pv");
        print(2,jedis.get("pv"));
        jedis.incrBy("pv", 5);
        print(2,jedis.get("pv"));
        jedis.decrBy("pv", 5);
        print(2,jedis.get("pv"));
        print(3, jedis.keys("*"));*/

        /*String listName = "list";
        jedis.del(listName);
        for (int i=0; i<10; ++i){
            jedis.lpush(listName, "a"+ String.valueOf(i));
        }
        print(4, jedis.lrange(listName,0,12));
        print(4, jedis.lrange(listName,0,3));
        print(5, jedis.llen(listName));
        print(6, jedis.lpop(listName));
        print(7, jedis.llen(listName));
        print(8, jedis.lrange(listName,2,6));
        print(9, jedis.lindex(listName,3));
        print(10, jedis.linsert(listName, BinaryClient.LIST_POSITION.AFTER,"a4", "bb"));
        print(10, jedis.linsert(listName, BinaryClient.LIST_POSITION.BEFORE,"a4", "xx"));
        print(4, jedis.lrange(listName,0,12));*/

        //hash
        /*String userKey = "userxx";
        jedis.hset(userKey, "name", "Jim");
        jedis.hset(userKey, "age", "12");
        jedis.hset(userKey, "phone", "1985467214");
        print(12,jedis.hget(userKey,"name"));
        print(13,jedis.hgetAll(userKey));
        jedis.hdel(userKey,"phone");
        print(14, jedis.hgetAll(userKey));
        print(15, jedis.hexists(userKey, "email"));
        print(16, jedis.hexists(userKey,"name"));
        print(17,jedis.hkeys(userKey));
        print(18,jedis.hvals(userKey));
        jedis.hsetnx(userKey, "school", "zlu");
        jedis.hsetnx(userKey, "name", "yxy");
        print(19,jedis.hgetAll(userKey));*/

        //set
        /*String likeKey1 = "commentLike1";
        String likeKey2 = "commentLike2";

        for (int i=0; i<10; i++){
            jedis.sadd(likeKey1,String.valueOf(i));
            jedis.sadd(likeKey2,String.valueOf(i*i));
        }
        print(20, jedis.smembers(likeKey1));
        print(21, jedis.smembers(likeKey2));
        print(22, jedis.sunion(likeKey1,likeKey2)); // 求并集
        print(23, jedis.sdiff(likeKey1,likeKey2)); // 差集
        print(24, jedis.sinter(likeKey1,likeKey2));//交集
        print(25, jedis.sismember(likeKey1,"12"));
        print(26, jedis.sismember(likeKey2, "16"));
        jedis.srem(likeKey1,"5");
        print(27, jedis.smembers(likeKey1));
        jedis.smove(likeKey2, likeKey1, "25"); //将25从likeKey2移到likeKey1
        print(28, jedis.smembers(likeKey1));
        print(29, jedis.smembers(likeKey2));
        print(29, jedis.scard(likeKey1));*/

        //优先队列
        /*String rankKey = "rankKey";
        String setKey = "zent";
        jedis.zadd(rankKey, 15, "jim");
        jedis.zadd(rankKey, 60, "Ben");
        jedis.zadd(rankKey, 90, "Lee");
        jedis.zadd(rankKey, 75, "Lucy");
        jedis.zadd(rankKey, 80, "Mei");
        print(30, jedis.zcard(rankKey));
        print(31, jedis.zcount(rankKey, 60, 100));
        print(32, jedis.zscore(rankKey, "Lucy"));
        jedis.zincrby(rankKey, 2, "Lucy");
        print(33, jedis.zscore(rankKey, "Lucy"));
        jedis.zincrby(rankKey, 2, "Luc");
        print(34, jedis.zscore(rankKey, "Luc"));
        print(35, jedis.zrange(rankKey,0,100));
        print(36, jedis.zrange(rankKey, 0 , 10));
        print(36, jedis.zrange(rankKey, 1,3));
        print(36, jedis.zrevrange(rankKey, 1,3));
        for (Tuple tuple: jedis.zrangeByScoreWithScores(rankKey, 60, 100)){
            print(37, tuple.getElement() + ":" + String.valueOf(tuple.getScore()));
        }
        print(38, jedis.zrank(rankKey, "Ben"));
        print(39, jedis.zrevrank(rankKey, "Ben"));

        jedis.zadd(setKey, 1, "a");
        jedis.zadd(setKey, 1, "b");
        jedis.zadd(setKey, 1, "c");
        jedis.zadd(setKey, 1, "d");
        jedis.zadd(setKey, 1, "e");

        print(40, jedis.zlexcount(setKey, "-", "+"));
        print(41, jedis.zlexcount(setKey, "[b", "[d")); //b<= <=d
        print(42, jedis.zlexcount(setKey, "(b", "[d"));
        jedis.zrem(setKey, "b");
        print(43, jedis.zrange(setKey, 0,10));
        jedis.zremrangeByLex(setKey, "(c","+");
        print(44, jedis.zrange(setKey, 0 ,2));*/

        /*JedisPool pool = new JedisPool("redis://localhost:6379/9");
        jedis.set("pv", "100");
        print(2,jedis.get("pv"));
        for (int i=0; i<100; i++){
            Jedis j = pool.getResource();
            print(45, j.get("pv"));
            j.close();//归还连接池的连接
        }*/

        User user = new User();
        user.setName("xx");
        user.setPassword("ppp");
        user.setHeadUrl("a.png");
        user.setSalt("salt");
        user.setId(1);
        jedis.set("user1", JSONObject.toJSONString(user)); //序列化
        print(46, jedis.get("user1"));

        String value = jedis.get("user1");
        User user2 = JSON.parseObject(value, User.class);  //反序列化
        print(47, user2);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://localhost:6379/10");
    }

    public long sadd(String key, String value){ //添加到集合
        Jedis jedis = pool.getResource();
        try {
            return jedis.sadd(key, value);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public long srem(String key, String value){//从集合中移除
        Jedis jedis = pool.getResource();
        try {
            return jedis.srem(key, value);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public long scard(String key){
        Jedis jedis = pool.getResource();
        try {
            return jedis.scard(key);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return 0;
    }

    public boolean sismember(String key, String value){//判断对象是否存在
        Jedis jedis = pool.getResource();
        try {
            return jedis.sismember(key, value);
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
        return false;
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public Jedis getJedis(){ //获取jedis对象
        return pool.getResource();
    }

    //开启一个事务
    public Transaction multi(Jedis jedis){
        try {
            return jedis.multi();//标记一个事务块的开启
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        }
        return null;
    }

    //执行一个事务
    public List<Object> exec (Transaction tx, Jedis jedis){
        try {
            return tx.exec(); //事务的执行
        }catch (Exception e){
            logger.error("发生异常" + e.getMessage());
        }finally {
            try {
                tx.close();
            } catch (IOException e) {
                logger.error("发生异常" + e.getMessage());
            }
            if (jedis!=null)
                jedis.close();
        }
        return null;
    }

    //ZADD命令添加所有指定的成员指定的分数存放在键的有序集合
    public long zadd (String key, double score, String value){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zadd(key, score, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public long zrem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrem(key, value);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    //返回有序集中，指定区间内的成员
    public Set<String> zrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrange(key, start, end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
    //返回有序集中，指定区间内的成员(逆序)
    public Set<String> zrevrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    //ZCARD命令返回在指定的键存储在集合中的元素的数量
    public long zcard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    //Redis Zscore 命令返回有序集中，成员的分数值。 如果成员元素不是有序集 key 的成员，或 key 不存在，返回 nil
    public Double zscore(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zscore(key, member);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public List<String> lrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lrange(key, start, end);
        } catch (Exception e) {
            logger.error("发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

}
