package com.linghua.wenda.async;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class EventModel {

    private EventType type;

    private int actorId;

    private  int entityId;

    private int entityType;

    private int entityOwnerId;

    private Map<String,String> exts = new HashMap<>();

    public void setExt(String key,String value){
        exts.put(key,value);
    }

    public String getExt(String key){
        return exts.get(key);
    }
}
