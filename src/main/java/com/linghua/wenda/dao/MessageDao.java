package com.linghua.wenda.dao;

import com.linghua.wenda.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageDao {

    String TABLE_NAME = " message ";
    String INSERT_FILEDS = " from_id,to_id,content,created_date,has_read,conversation_id ";
    String SELECT_FILDS = " id," + INSERT_FILEDS;

    @Insert({"insert into", TABLE_NAME, "(", INSERT_FILEDS, ") values(#{fromId},#{toId},#{content},#{createdDate},#{hasRead},#{conversationId})"})
    int addMessage(Message message);

    @Select({"select", SELECT_FILDS, "from", TABLE_NAME, "where conversation_id=#{conversationId} order by created_date desc limit #{offset},#{limit}"})
    List<Message> selectConversationDetail(@Param("conversationId") String conversationId, @Param("offset") int offset, @Param("limit") int limit);

    @Select({"select "+INSERT_FILEDS+" ,count(id) as id from (select * from", TABLE_NAME, "where from_id=#{userId} or to_id=#{userId} order by created_date desc) tt " +
            "group by conversation_id order by created_date desc limit #{offset},#{limit}"})
    List<Message> getConversationList(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit);

    @Select({"select count(id) from", TABLE_NAME, "where conversation_id=#{conversationId}"})
    int getCountByConversationId(String conversationId);

    @Select({"select count(id) from ", TABLE_NAME, " where has_read=0 and to_id=#{userId} and conversation_id=#{conversationId}"})
    int getConversationUnreadCount(@Param("userId") int userId, @Param("conversationId") String conversationId);


    @Update({"update", TABLE_NAME, "set has_read=1 where conversation_id=#{conversationId} and from_id=#{fromId}"})
    int updateMessageStatus(@Param("conversationId") String conversationId, @Param("fromId") int fromId);


    /**
     * SELECT
     * conversation_id,
     * MAX(created_date) d
     * FROM
     * (
     * SELECT
     * *
     * FROM
     * message
     * WHERE
     * from_id = 1
     * OR to_id = 1
     * ) t
     * GROUP BY
     * conversation_id
     * ORDER BY
     * d DESC;
     */
    void mobailaoge();
}
