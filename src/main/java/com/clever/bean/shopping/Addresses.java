package com.clever.bean.shopping;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.Date;

/**
 * 收货地址
 *
 * @Author xixi
 * @Date 2024-03-27 11:46:50
 */
public class Addresses implements Serializable {

    /**
     * 收货地址id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 用户id
     */
    @NotBlank(message = "用户id不能为空")
    private String userId;
    /**
     * 收货人姓名
     */
    @NotBlank(message = "收货人姓名不能为空")
    private String name;
    /**
     * 收货人电话
     */
    @NotBlank(message = "收货人电话不能为空")
    private String phone;
    /**
     * 收货地址
     */
    @NotBlank(message = "收货地址不能为空")
    private String address;
    /**
     * 详细地址
     */
    private String addressDetail;
    /**
     * 是否默认地址:0-否,1-是
     */
    @NotNull(message = "是否默认地址不能为空")
    private Integer isDefault;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 修改时间
     */
    private Date updatedAt;


    /**
     * 收货地址id
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
     * 收货人姓名
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 收货人电话
     */
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 收货地址
     */
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 详细地址
     */
    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    /**
     * 是否默认地址:0-否,1-是
     */
    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
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