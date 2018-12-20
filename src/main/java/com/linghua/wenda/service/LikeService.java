package com.linghua.wenda.service;

public interface LikeService {

    public long getLikeCount(int entityId, int entityType);

    public int getLikeStatus(int userId, int entityId, int entityType);

    public long like(int userId, int entityId, int entityType);

    public long disLike(int userId, int entityId, int entityType);
}
