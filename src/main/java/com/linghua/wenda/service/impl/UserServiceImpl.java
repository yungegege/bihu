package com.linghua.wenda.service.impl;

import com.linghua.wenda.dao.LoginTicketDao;
import com.linghua.wenda.dao.UserDao;
import com.linghua.wenda.model.LoginTicket;
import com.linghua.wenda.model.User;
import com.linghua.wenda.service.UserService;
import com.linghua.wenda.util.WendaUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    LoginTicketDao loginTicketDao;

    @Override
    public Map<String,String> register(String username,String password) {
        Map<String,String> map = new HashMap<>();
        if (StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }
        User user = userDao.selectByName(username);
        if (null!=user){
            map.put("msg","用户已经被注册");
            return map;
        }
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setPassword(WendaUtil.MD5(password+user.getSalt()));
        userDao.addUser(user);

        user = userDao.selectByName(username);
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public Map<String,String> login(String username,String password){
        Map<String,String> map = new HashMap<>();
        if (StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }
        User user = userDao.selectByName(username);
        if (null==user){
            map.put("msg","用户不存在");
            return map;
        }
        if (!user.getPassword().equals(WendaUtil.MD5(password+user.getSalt()))){
            map.put("msg","密码错误");
            return map;
        }
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;

    }

    @Override
    public User getUserById(int userId){
        return userDao.selectById(userId);
    }

    @Override
    public void updatePassword(User user) {
        userDao.updatePassword(user);
    }

    public String addLoginTicket(int userId){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime()+2*1000*24*3600);
        loginTicket.setExpired(date);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDao.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    @Override
    public void logout(String ticket) {
        loginTicketDao.updateStatus(ticket,1);
    }

    @Override
    public User getUserByName(String name) {
        return userDao.selectByName(name);
    }
}
