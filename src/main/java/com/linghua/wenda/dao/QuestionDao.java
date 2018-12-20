package com.linghua.wenda.dao;

import com.linghua.wenda.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionDao {

    String TABLE_NAME = " question ";
    String INSERT_FILEDS = " title,content,created_date,user_id,comment_count ";
    String SELECT_FILEDS = " id," + INSERT_FILEDS;


    @Insert({"insert into", TABLE_NAME, "(", INSERT_FILEDS, ") values(#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    int addQuestion(Question question);

    @Select({"select", SELECT_FILEDS, "from", TABLE_NAME, "where id=#{id}"})
    Question selectById(int id);

    @Update({"update", TABLE_NAME, "set comment_count=#{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("commentCount") int commentCount, @Param("id") int id);


    List<Question> selectLatestQuestions(@Param("userId") int userId,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

}
