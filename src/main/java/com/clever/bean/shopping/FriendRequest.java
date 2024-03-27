package com.clever.bean.shopping;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.Date;

/**
 * 好友申请
 *
 * @Author xixi
 * @Date 2024-03-27 17:36:54
 */
public class FriendRequest implements Serializable {

    /**
     * 好友申请id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 用户id
     */
    @NotBlank(message = "用户id不能为空")
    private String userId;
    /**
     * 好友id
     */
    @NotBlank(message = "好友id不能为空")
    private String friendId;
    /**
     * 好友申请消息
     */
    private String message;
    /**
     * 好友申请状态:0-未处理,1-已同意,2-已拒绝
     */
    @NotNull(message = "好友申请状态不能为空")
    private Integer status;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 修改时间
     */
    private Date updatedAt;


    /**
     * 好友申请id
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 用户id
     */
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 好友id
     */
    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    /**
     * 好友申请消息
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 好友申请状态:0-未处理,1-已同意,2-已拒绝
     */
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 创建时间
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 修改时间
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}