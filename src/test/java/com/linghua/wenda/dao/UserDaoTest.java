package com.linghua.wenda.dao;


import com.linghua.wenda.WendaApplicationTests;
import com.linghua.wenda.model.User;
import com.linghua.wenda.util.WendaUtil;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.UUID;

public class UserDaoTest extends WendaApplicationTests {

    @Autowired
    UserDao userDao;

    @Test
    public void testAddUser() {
        Random random = new Random();
        String name = getRandomName();
        User user = userDao.selectByName(name);
        if (user == null) {
            user = new User();
            user.setName(name);
            user.setEmail("1250429552@qq.com");
            user.setStatus(1);
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setSalt(UUID.randomUUID().toString().substring(0, 5));
            user.setPassword(WendaUtil.MD5("199781" + user.getSalt()));
            int a = userDao.addUser(user);
        }

    }

    @Test
    public void testSelectById() {
        User user = userDao.selectById(1);
        System.out.println(user);
    }

    @Test
    public void testSelectByName() {
        User user = userDao.selectByName("lyf1");
        System.out.println(user);
    }


    public String getRandomName() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        int a = random.nextInt(7) + 1;
        for (int i = 0; i < a; i++) {
            sb.append(getRandomChar());
        }
        return sb.toString();

    }


    //随机生成常见汉字
    public String getRandomChar() {
        String str = "";
        int highCode;
        int lowCode;

        Random random = new Random();

        highCode = (176 + Math.abs(random.nextInt(39))); //B0 + 0~39(16~55) 一级汉字所占区
        lowCode = (161 + Math.abs(random.nextInt(93))); //A1 + 0~93 每区有94个汉字

        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(highCode)).byteValue();
        b[1] = (Integer.valueOf(lowCode)).byteValue();

        try {
            str = new String(b, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

}
