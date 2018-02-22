package com.example.Test.service;

import com.example.Test.dao.FeedDAO;
import com.example.Test.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedService {
    @Autowired
    FeedDAO feedDAO;

    /**
     * 拉模式下获取数据
     * @param maxId 增量查询所需
     * @param userIds
     * @param count
     * @return
     */
    public List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count){
        return feedDAO.selectUserFeeds(maxId, userIds, count);
    }

    /**
     * 增加新鲜事
     * @param feed
     * @return
     */
    public boolean addFeed(Feed feed){
        feedDAO.addFeed(feed);
        return feed.getId()>0;
    }

    /**
     * 推模式
     */
    public Feed getById(int id){
        return feedDAO.getFeedById(id);
    }
}
