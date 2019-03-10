package com.linghua.wenda.model;

import org.springframework.stereotype.Component;

/**
 * Created by nowcoder on 2016/7/3.
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<User>();
    private static ThreadLocal<Integer> ids = new ThreadLocal<>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();
    }

    public int getId(){
        return ids.get();
    }

    public void setId(int id) {
        ids.set(id);
    }

    public void clearId(){
        ids.remove();
    }

}
