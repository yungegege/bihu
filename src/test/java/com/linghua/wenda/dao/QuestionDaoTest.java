package com.linghua.wenda.dao;

import com.linghua.wenda.WendaApplicationTests;
import com.linghua.wenda.model.Question;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class QuestionDaoTest extends WendaApplicationTests {

    @Autowired
    QuestionDao questionDao;

    @Test
    public void testAddQuestion(){
        for (int i = 0; i < 23; i++) {
            Question question = new Question();
            question.setTitle("title"+i);
            question.setContent("content-"+i+"-jhveiewkcvwbeubcewbkcbweivckewvi");
            question.setCommentCount(i);
            Date data = new Date();
            data.setTime(data.getTime()+i*5000*3600);
            question.setCreatedDate(data);
            question.setUserId(i+1);
            int a = questionDao.addQuestion(question);
            System.out.println(a);
        }

    }

    @Test
    public void testSelectLatestQuestions(){
        List<Question> list = questionDao.selectLatestQuestions(0,1,10);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }
}
