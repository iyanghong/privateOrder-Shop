package com.clever.bean.shopping;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.FieldFill;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.Date;
import java.math.BigDecimal;

/**
 * 商品
 *
 * @Author xixi
 * @Date 2024-03-28 09:00:55
 */
public class Product implements Serializable {

    /**
     * 商品id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 商品名称
     */
    @NotBlank(message = "商品名称不能为空")
    private String name;
    /**
     * 商品描述
     */
    private String description;
    /**
     * 商品价格
     */
    @NotNull(message = "商品价格不能为空")
    private BigDecimal price;
    /**
     * 商品图片地址
     */
    private String imgUrl;
    /**
     * 是否热门：0-否，1-是
     */
    private Integer if_hot;
    /**
     * 商品规格
     */
    private String specification;
    /**
     * 商品库存
     */
    private Integer stock;



    /**
     * 商品状态:0-下架,1-上架
     */
    private Integer status;
    /**
     * 商品分类id
     */
    @NotBlank(message = "商品分类id不能为空")
    private String categoryId;
    /**
     * 创建者id
     */
    @TableField(value = "creator", fill = FieldFill.INSERT)
    private String creator;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 修改时间
     */
    private Date updatedAt;


    /**
     * 商品id
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 商品名称
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 商品描述
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 商品价格
     */
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 商品图片地址
     */
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /**
     * 商品规格
     */
    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    /**
     * 商品库存
     */
    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    /**
     * 商品状态:0-下架,1-上架
     */
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 商品分类id
     */
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 创建者id
     */
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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

    public Integer getIf_hot() {
        return if_hot;
    }

    public void setIf_hot(Integer if_hot) {
        this.if_hot = if_hot;
    }
}