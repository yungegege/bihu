package com.linghua.wenda.service;

import com.linghua.wenda.model.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageService {

    int addMessage(Message message);

    List<Message> getConversationDetail(String conversationId, int offset, int limit);

    List<Message> getConversationList(int userId, int offset, int limit);

    int getConversationUnreadCount(int userId, String conversationId);

    int readMessage(String conversationId, int fromId);
}
