package com.linghua.wenda.service;

import com.linghua.wenda.model.Question;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface QuestionService {

    int addQuestion(Question question);

    List<Question> getLatestQuestions(int userId, int offset, int limit);

    Question selectById(int id);

    int updateCommentCount(int count, int id);
}
