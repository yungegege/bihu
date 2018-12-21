package com.linghua.wenda.controller;

import com.linghua.wenda.async.EventModel;
import com.linghua.wenda.async.EventProducer;
import com.linghua.wenda.async.EventType;
import com.linghua.wenda.model.Comment;
import com.linghua.wenda.model.EntityType;
import com.linghua.wenda.model.HostHolder;
import com.linghua.wenda.service.CommentService;
import com.linghua.wenda.service.LikeService;
import com.linghua.wenda.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CommentService commentService;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(value = "/like",method = RequestMethod.POST)
    @ResponseBody
    public String like(@RequestParam(value = "commentId")int commentId){
        if (hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }
        Comment comment = commentService.getCommentById(commentId);
        //给自己点赞不用入队列
        if (comment.getUserId()!=hostHolder.getUser().getId()){
            EventModel eventModel = new EventModel();
            eventModel.setType(EventType.LIKE);
            eventModel.setActorId(hostHolder.getUser().getId());
            eventModel.setEntityId(commentId);
            eventModel.setEntityType(EntityType.ENTITY_COMMENT);
            eventModel.setEntityOwnerId(comment.getUserId());
            eventModel.setExt("questionId",String.valueOf(comment.getEntityId()));
            eventModel.setExt("commentId",String.valueOf(comment.getId()));
            eventProducer.fireEvent(eventModel);
        }
        long likeCount = likeService.like(hostHolder.getUser().getId(),commentId,EntityType.ENTITY_COMMENT);

        return WendaUtil.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(value = "/dislike",method = RequestMethod.POST)
    @ResponseBody
    public String dislike(@RequestParam(value = "commentId")int commentId){
        if (hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
        }

        long likeCount = likeService.disLike(hostHolder.getUser().getId(),commentId,EntityType.ENTITY_COMMENT);

        return WendaUtil.getJSONString(0,String.valueOf(likeCount));
    }
}
