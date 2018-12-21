package com.linghua.wenda.async;

import com.alibaba.fastjson.JSON;
import com.linghua.wenda.util.JedisUtil;
import com.linghua.wenda.util.RedisKeyUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j
@Service
public class EventConsumer implements InitializingBean,ApplicationContextAware {

    private ApplicationContext applicationContext;

    private Map<EventType,List<EventHandler>> config = new HashMap<>();

    @Autowired
    private JedisUtil jedisUtil;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String,EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans!=null){
            for (Map.Entry<String,EventHandler> entry:beans.entrySet()){
                //获取每个EventHandler支持的事件类型
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();

                for (EventType type:eventTypes){
                    if (!config.containsKey(type)){
                        config.put(type,new ArrayList<>());
                    }
                    //在这种类型对于的value上加上这个EventHandler
                    config.get(type).add(entry.getValue());
                }
            }
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    String key = RedisKeyUtil.getEventQueueKey();
                    List<String> events = jedisUtil.brpop(0,key);
                    for (String message:events){
                        //返回的list第一个值为key
                        if (message.equals(key)){
                            continue;
                        }
                        EventModel eventModel = JSON.parseObject(message,EventModel.class);
                        if (!config.containsKey(eventModel.getType())){
                            log.error("不能识别的事件");
                            continue;
                        }
                        for (EventHandler eventHandler:config.get(eventModel.getType())){
                            eventHandler.doHandle(eventModel);
                        }
                    }
                }
            }
        });

        thread.start();
    }

}
