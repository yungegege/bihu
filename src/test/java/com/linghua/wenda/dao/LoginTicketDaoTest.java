package com.linghua.wenda.dao;

import com.linghua.wenda.WendaApplicationTests;
import com.linghua.wenda.model.LoginTicket;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class LoginTicketDaoTest extends WendaApplicationTests {

    @Autowired
    LoginTicketDao loginTicketDao;

    @Test
    public void testAddTicket(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(1);
        loginTicket.setTicket("1111");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date());
        loginTicketDao.addTicket(loginTicket);
    }

}
