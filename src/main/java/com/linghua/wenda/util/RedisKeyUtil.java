package com.linghua.wenda.util;

public class RedisKeyUtil {

    private static String SPILT = "_";
    private static String LIKE = "like";
    private static String DISLIKE = "disLike";
    private static String EVENTQUEUE = "eventqueue";
    private static String ACTIVEKEY = "active";

    public static String getLikeKey(int entityId,int entityType){
        return LIKE+SPILT+entityId+SPILT+entityType;
    }

    public static String getDisLikeKey(int entityId,int entityType){
        return DISLIKE+SPILT+entityId+SPILT+entityType;
    }
    public static String getEventQueueKey(){
        return EVENTQUEUE;
    }

    public static String getActiveKey(String username){
        return ACTIVEKEY+SPILT+username;
    }
}
