package com.linghua.wenda.dao;


import com.linghua.wenda.WendaApplicationTests;
import com.linghua.wenda.model.User;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

public class UserDaoTest extends WendaApplicationTests {

    @Autowired
    UserDao userDao;

    @Test
    public void testAddUser(){
        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            User user = new User();
            user.setName("lyf"+i);
            user.setPassword("");
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",random.nextInt(1000)));
            user.setSalt("");
            int a = userDao.addUser(user);
            System.out.println(a);
        }

    }

    @Test
    public void testSelectById(){
        User user = userDao.selectById(1);
        System.out.println(user);
    }

    @Test
    public void testSelectByName(){
        User user = userDao.selectByName("lyf1");
        System.out.println(user);
    }

}
