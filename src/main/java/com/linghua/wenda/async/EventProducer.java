package com.linghua.wenda.async;

import com.alibaba.fastjson.JSONObject;
import com.linghua.wenda.util.JedisUtil;
import com.linghua.wenda.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventProducer {

    @Autowired
    private JedisUtil jedisUtil;

    public boolean fireEvent(EventModel eventModel){
        try {
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisUtil.lpush(key,json);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
