package com.linghua.wenda.async;

public enum  EventType {
    LIKE(0),
    COMMENT(1),
    REGISTER(2),
    LOGIN(3),
    MAIL(4),
    FOLLOW(5),
    UNFOLLOW(6),
    ADD_QUESTION(7);

    private int value;
    EventType(int value) { this.value = value; }
    public int getValue() { return value; }
}
