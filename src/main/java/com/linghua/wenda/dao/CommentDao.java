package com.linghua.wenda.dao;

import com.linghua.wenda.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentDao {

    String TABLE_NAME = " comment ";
    String INSERT_FILEDS = " content,user_id,entity_id,entity_type,created_date,status ";
    String SELECT_FILDS = " id," + INSERT_FILEDS;

    @Insert({"insert into", TABLE_NAME, "(", INSERT_FILEDS, ") values(#{content},#{userId},#{entityId},#{entityType},#{createdDate},#{status})"})
    int addComment(Comment comment);

    @Select({"select", SELECT_FILDS, "from", TABLE_NAME, "where id=#{id}"})
    Comment selectCommentById(int id);

    @Select({"select", SELECT_FILDS, "from", TABLE_NAME, "where entity_type=#{entityType} and entity_id=#{entityId} order by created_date desc"})
    List<Comment> selectByEntity(@Param("entityType") int entityType, @Param("entityId") int entityId);

    @Select({"select", "count(id)", "from", TABLE_NAME, "where entity_type=#{entityType} and entity_id=#{entityId}"})
    int getCommmentCount(@Param("entityType") int entityType, @Param("entityId") int entityId);

    @Update({"update", TABLE_NAME, "set status=#{status} where id=#{id}"})
    int updateStatus(@Param("id") int id, @Param("status") int status);

    @Select({"select count(id) from", TABLE_NAME, "where user_id=#{userId}"})
    int getUserCommentCount(int userId);

}
