package com.linghua.wenda.service.impl;

import com.linghua.wenda.dao.MessageDao;
import com.linghua.wenda.model.Message;
import com.linghua.wenda.service.MessageService;
import com.linghua.wenda.util.SensitiveUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageDao messageDao;

    @Autowired
    SensitiveUtil sensitiveUtil;

    @Override
    public int addMessage(Message message) {
        message.setContent(sensitiveUtil.filter(message.getContent()));
        return messageDao.addMessage(message) > 0 ? message.getId() : 0;
    }

    @Override
    public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
        return messageDao.selectConversationDetail(conversationId, offset, limit);
    }

    @Override
    public List<Message> getConversationList(int userId, int offset, int limit) {
        return messageDao.getConversationList(userId,offset,limit);
    }

    @Override
    public int getConversationUnreadCount(int userId, String conversationId) {
        return messageDao.getConversationUnreadCount(userId,conversationId);
    }


    @Override
    public int readMessage(String conversationId, int fromId) {
        return messageDao.updateMessageStatus(conversationId,fromId);
    }
}
