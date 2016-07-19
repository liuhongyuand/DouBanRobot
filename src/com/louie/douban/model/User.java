package com.louie.douban.model;

import java.util.Map;

/**
 * Users
 * Created by liuhongyu.louie on 2016/7/19.
 */
public class User {

    private String username;
    private String password;
    private String remember;
    private String cookie;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemember() {
        return remember;
    }

    public void setRemember(String remember) {
        this.remember = remember;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
