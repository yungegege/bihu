package com.linghua.wenda.dao;

import com.linghua.wenda.WendaApplicationTests;
import com.linghua.wenda.model.Message;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MessageDaoTest extends WendaApplicationTests {
    @Autowired
    MessageDao messageDao;

    @Test
    public void testSelectConversationDetail(){
        List<Message> messageList = messageDao.selectConversationDetail("1", 0, 10);
        for (Message message :
                messageList) {
            System.out.println(message);
        }

    }

    @Test
    public void testUpdateMessageStatus(){
        messageDao.updateMessageStatus("1",17);
    }
}
