package com.linghua.wenda.service;

import com.linghua.wenda.WendaApplicationTests;
import com.linghua.wenda.model.EntityType;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LikeServiceTest extends WendaApplicationTests {
    @Autowired
    LikeService likeService;

    @Test
    public void testLike(){
        likeService.like(1,1,EntityType.ENTITY_COMMENT);
        Assert.assertEquals(1,likeService.getLikeStatus(1,1,EntityType.ENTITY_COMMENT));
        likeService.like(1,1,EntityType.ENTITY_COMMENT);
        Assert.assertEquals(0,likeService.getLikeStatus(1,1,EntityType.ENTITY_COMMENT));


    }
}
