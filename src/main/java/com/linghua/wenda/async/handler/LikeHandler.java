package com.linghua.wenda.async.handler;

import com.linghua.wenda.async.EventHandler;
import com.linghua.wenda.async.EventModel;
import com.linghua.wenda.async.EventType;
import com.linghua.wenda.model.Comment;
import com.linghua.wenda.model.Message;
import com.linghua.wenda.model.User;
import com.linghua.wenda.service.CommentService;
import com.linghua.wenda.service.MessageService;
import com.linghua.wenda.service.UserService;
import com.linghua.wenda.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class LikeHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(WendaUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUserById(model.getActorId());
        Comment comment = commentService.getCommentById(Integer.parseInt(model.getExt("commentId")));
        message.setContent("您的评论"+comment.getContent().replaceAll("&quot;"," ").substring(0,10)+"...收到来自用户 "+user.getName()+" 的赞。http://127.0.0.1:8080/question/"+model.getExt("questionId"));
        message.setConversationId(Math.min(message.getFromId(),message.getToId())+"_"+Math.max(message.getFromId(),message.getToId()));
        messageService.addMessage(message);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
