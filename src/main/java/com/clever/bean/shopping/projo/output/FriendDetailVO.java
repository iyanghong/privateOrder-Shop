package com.clever.bean.shopping.projo.output;

import com.clever.bean.shopping.Friend;

/**
 * @Author xixi
 * @Date 2024-03-27 17:39
 **/
public class FriendDetailVO extends Friend {
    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别:0-未知,1-男,2-女
     */
    private Integer gender;
    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户类型 0:普通用户 1:商家
     */
    private Integer type;

    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
