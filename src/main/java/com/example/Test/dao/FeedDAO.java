package com.example.Test.dao;

import com.example.Test.model.Feed;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FeedDAO {
    String TABLE_NAME = " feed ";
    String INSERT_FIELDS = " user_Id, data, created_date, type ";
    String SELECT_FIELDS = " id " + INSERT_FIELDS;

    /**
     * 插入新鲜事
     * @param feed
     * @return
     */
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
    ") values (#{userId}, #{data}, #{createdDate}, #{type})"})
    int addFeed(Feed feed);

    /**
     * 根据ID选择新鲜事，推模式
     * @param id
     * @return
     */
    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME,
    " where id = #{id}"})
    Feed getFeedById(int id);

    /**
     * 拉更新
     * @param maxId
     * @param userIds
     * @param count
     * @return
     */
    List<Feed> selectUserFeeds(@Param("maxId") int maxId, //增量更新
                               @Param("userIds") List<Integer> userIds, //关注的用户的列表
                               @Param("count") int count);// 分页
}
