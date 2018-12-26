package com.linghua.wenda.service;


import com.linghua.wenda.model.User;

import java.util.Map;

public interface UserService {

    Map<String,String> register(String username,String password,String email);

    Map<String,String> login(String username,String password);

    void updatePassword(User user);

    User getUserById(int userId);

    User getUserByName(String name);

    void logout(String ticket);

    void updateStatus(User user);
}
