package com.clever.bean.model;

import com.clever.bean.shopping.User;

import java.io.Serializable;

/**
 * @Author xixi
 * @Date 2024-03-26 17:06
 **/
public class OnlineUser extends User implements Serializable {
    /**
     * token
     */
    private String token;

    public OnlineUser(User user, String token) {
        this.setId(user.getId());
        this.setUsername(user.getUsername());
        this.setPassword(user.getPassword());
        this.setNickname(user.getNickname());
        this.setGender(user.getGender());
        this.setAvatar(user.getAvatar());
        this.setType(user.getType());
        this.setPhone(user.getPhone());
        this.setEmail(user.getEmail());
        this.setMoney(user.getMoney());
        this.setLastLoginTime(user.getLastLoginTime());
        this.setLoginIp(user.getLoginIp());
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
