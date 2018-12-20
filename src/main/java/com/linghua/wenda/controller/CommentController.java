package com.linghua.wenda.controller;

import com.linghua.wenda.model.Comment;
import com.linghua.wenda.model.EntityType;
import com.linghua.wenda.model.HostHolder;
import com.linghua.wenda.model.Question;
import com.linghua.wenda.service.CommentService;
import com.linghua.wenda.service.QuestionService;
import com.linghua.wenda.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    public String addComment(@RequestParam("questionId") int questionId, @RequestParam("content") String content) {
        try {
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setStatus(0);
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setEntityId(questionId);
            comment.setCreatedDate(new Date());
            if (hostHolder.getUser()==null){
                comment.setUserId(WendaUtil.ANONYMOUS_USERID);
//                return "redirect:/reglogin";
            }else {
                comment.setUserId(hostHolder.getUser().getId());
            }
            commentService.addComment(comment);
            questionService.updateCommentCount(questionService.selectById(questionId).getCommentCount()+1,questionId);
        } catch (Exception e) {
           logger.error("添加评论失败"+e.getMessage());
        }
        return "redirect:/question/"+questionId;


    }
}
