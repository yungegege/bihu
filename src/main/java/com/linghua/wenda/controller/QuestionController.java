package com.linghua.wenda.controller;

import com.linghua.wenda.model.*;
import com.linghua.wenda.service.CommentService;
import com.linghua.wenda.service.LikeService;
import com.linghua.wenda.service.QuestionService;
import com.linghua.wenda.service.UserService;
import com.linghua.wenda.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;

    @RequestMapping(value = "/question/{qid}", method = {RequestMethod.GET})
    public String addQuestion(Model model, @PathVariable("qid")int qid){

        Question question = questionService.selectById(qid);
        model.addAttribute("question",question);
        List<Comment> commentList = commentService.getByEntity(EntityType.ENTITY_QUESTION, qid);
        List<ViewObject> comments = new ArrayList<>();
        for(Comment comment:commentList){
            ViewObject vo = new ViewObject();
            vo.set("comment",comment);
            vo.set("user",userService.getUserById(comment.getUserId()));
            vo.set("likeCount",likeService.getLikeCount(comment.getId(),EntityType.ENTITY_COMMENT));
            if (hostHolder.getUser()==null){
                vo.set("liked",0);
            }else {
                vo.set("liked",likeService.getLikeStatus(hostHolder.getUser().getId(),comment.getId(),EntityType.ENTITY_COMMENT));

            }
            comments.add(vo);
        }

        model.addAttribute("vos",comments);
        if (hostHolder.getUser()!=null){
            model.addAttribute("user",hostHolder.getUser().getName());

        }
        return "detail";

    }

    @RequestMapping(value = "/question/add", method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title")String title,@RequestParam("content")String content){
        try{
            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);
            question.setCreatedDate(new Date());
            if (hostHolder.getUser()==null){
//                question.setUserId(WendaUtil.ANONYMOUS_USERID);
                return WendaUtil.getJSONString(999);
            }else {
                question.setUserId(hostHolder.getUser().getId());
            }
            if (questionService.addQuestion(question)>0){
                return WendaUtil.getJSONString(0);
            }
        }catch (Exception e){
            logger.error("增加提问失败"+e.getMessage());
        }
        return WendaUtil.getJSONString(1,"增加提问失败");
    }


}
