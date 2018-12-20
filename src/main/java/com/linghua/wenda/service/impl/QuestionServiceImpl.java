package com.linghua.wenda.service.impl;

import com.linghua.wenda.dao.QuestionDao;
import com.linghua.wenda.model.Question;
import com.linghua.wenda.service.QuestionService;
import com.linghua.wenda.util.SensitiveUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    QuestionDao questionDao;

    @Autowired
    SensitiveUtil sensitiveUtil;

    @Override
    public int addQuestion(Question question) {
        //过滤html
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        //过滤敏感词
        question.setTitle(sensitiveUtil.filter(question.getTitle()));
        question.setContent(sensitiveUtil.filter(question.getContent()));
        return questionDao.addQuestion(question);
    }

    @Override
    public List<Question> getLatestQuestions(int userId, int offset, int limit) {
        return questionDao.selectLatestQuestions(userId, offset, limit);
    }


    @Override
    public Question selectById(int id) {
        return questionDao.selectById(id);
    }

    @Override
    public int updateCommentCount(int count, int id) {
        return questionDao.updateCommentCount(count, id);
    }
}
