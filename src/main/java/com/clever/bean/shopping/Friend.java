package com.clever.bean.shopping;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import javax.validation.constraints.NotBlank;

import java.util.Date;

/**
 * 好友
 *
 * @Author xixi
 * @Date 2024-03-27 17:36:54
 */
public class Friend implements Serializable {

    /**
     * 好友id
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
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 修改时间
     */
    private Date updatedAt;


    /**
     * 好友id
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
     * 备注
     */
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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