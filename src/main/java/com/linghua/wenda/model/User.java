package com.linghua.wenda.model;
import lombok.Data;

@Data
public class User {
    private int id;
    private String name;
    private String password;
    private String salt;
    private String headUrl;
    private String email;
    private int status;

}
