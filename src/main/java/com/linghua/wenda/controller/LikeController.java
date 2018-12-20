package com.linghua.wenda.controller;

import com.linghua.wenda.model.EntityType;
import com.linghua.wenda.model.HostHolder;
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

    @RequestMapping(value = "/like",method = RequestMethod.POST)
    @ResponseBody
    public String like(@RequestParam(value = "commentId")int commentId){
        if (hostHolder.getUser()==null){
            return WendaUtil.getJSONString(999);
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
