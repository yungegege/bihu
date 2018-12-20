package com.linghua.wenda.service.impl;

import com.linghua.wenda.service.LikeService;
import com.linghua.wenda.util.JedisUtil;
import com.linghua.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    private JedisUtil jedisUtil;

    @Override
    public long getLikeCount(int entityId, int entityType) {
        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);
        return jedisUtil.scard(likeKey);
    }

    @Override
    public int getLikeStatus(int userId, int entityId, int entityType) {
        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);
        if (jedisUtil.sismember(likeKey,String.valueOf(userId))){
            return 1;
        }
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId,entityType);
        return jedisUtil.sismember(disLikeKey,String.valueOf(userId))?-1:0;
    }

    public long like(int userId, int entityId, int entityType){
        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);
        jedisUtil.sadd(likeKey,String.valueOf(userId));

        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId,entityType);
        jedisUtil.srem(disLikeKey,String.valueOf(userId));

        return jedisUtil.scard(likeKey);
    }

    public long disLike(int userId,int entityId,int entityType){
        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);
        jedisUtil.srem(likeKey,String.valueOf(userId));

        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId,entityType);
        jedisUtil.sadd(disLikeKey,String.valueOf(userId));

        return jedisUtil.scard(likeKey);
    }

}
