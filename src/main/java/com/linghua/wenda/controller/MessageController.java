package com.linghua.wenda.controller;

import com.linghua.wenda.model.HostHolder;
import com.linghua.wenda.model.Message;
import com.linghua.wenda.model.User;
import com.linghua.wenda.model.ViewObject;
import com.linghua.wenda.service.MessageService;
import com.linghua.wenda.service.UserService;
import com.linghua.wenda.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = "/msg/list", method = RequestMethod.GET)
    public String getConversationList(Model model) {
        if (null == hostHolder.getUser()) {
            return "redirect:/reglogin";
        }
        int userId = hostHolder.getUser().getId();
        List<Message> conversationList = messageService.getConversationList(userId, 0, 10);

        List<ViewObject> conversations = new ArrayList<>();
        for (Message message : conversationList) {
            ViewObject vo = new ViewObject();
            vo.set("message",message);
            //显示对方(发或者收都可能)的信息
            int targetId = message.getFromId()==userId?message.getToId():message.getFromId();

            vo.set("user",userService.getUserById(targetId));
            int n = messageService.getConversationUnreadCount(userId,message.getConversationId());
            if (n>0){
                vo.set("unread",n);
            }

            conversations.add(vo);
        }
        model.addAttribute("vos",conversations);
        return "letter";
    }

    @RequestMapping(value = "/msg/detail", method = RequestMethod.GET)
    public String getConversationDetail(Model model, @RequestParam("conversationId") String conversationId) {
        try {
            List<Message> messageList = messageService.getConversationDetail(conversationId, 0, 10);
            List<ViewObject> vos = new ArrayList<>();
            for (Message message : messageList) {
                ViewObject vo = new ViewObject();
                vo.set("message", message);
                vo.set("user", userService.getUserById(message.getFromId()));
                vos.add(vo);

                //读过消息后把message的status改为1
                messageService.readMessage(conversationId,message.getFromId());

            }
            model.addAttribute("vos", vos);

        } catch (Exception e) {
            logger.error("获取详情失败" + e.getMessage());
        }
        return "letterDetail";
    }


    @RequestMapping(value = "/msg/addMessage", method = RequestMethod.POST)
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName, @RequestParam("content") String content) {
        try {

            if (null == hostHolder.getUser()) {
                return WendaUtil.getJSONString(999, "未登录");
            }
            User user = userService.getUserByName(toName);
            if (user == null) {
                return WendaUtil.getJSONString(1, "用户不存在");
            }

            Message message = new Message();
            message.setContent(content);
            message.setCreatedDate(new Date());
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            message.setConversationId(Math.min(message.getFromId(),message.getToId()) + "_" + Math.max(message.getFromId(),message.getToId()));

            messageService.addMessage(message);
            return WendaUtil.getJSONString(0);
        } catch (Exception e) {
            logger.error("发消息失败" + e.getMessage());
            return WendaUtil.getJSONString(1, "发送失败");
        }
    }
}
