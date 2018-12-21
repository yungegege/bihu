package com.linghua.wenda.async.handler;

import com.linghua.wenda.async.EventHandler;
import com.linghua.wenda.async.EventModel;
import com.linghua.wenda.async.EventType;
import com.linghua.wenda.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoginExceptionHandler implements EventHandler {

    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        Map<String,Object> map = new HashMap<>();
        map.put("username",model.getExt("username"));
        mailSender.sendWithHTMLTemplate(model.getExt("email"),"登录IP异常","mails/login_exception.html",map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
