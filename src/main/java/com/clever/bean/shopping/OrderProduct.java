package com.clever.bean.shopping;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.Date;
import java.math.BigDecimal;

/**
 * 订单商品
 *
 * @Author xixi
 * @Date 2024-03-28 09:00:55
 */
public class OrderProduct implements Serializable {

    /**
     * 订单商品id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 订单id
     */
    @NotBlank(message = "订单id不能为空")
    private String orderId;
    /**
     * 商品id
     */
    @NotBlank(message = "商品id不能为空")
    private String productId;
    /**
     * 商品数量
     */
    @NotNull(message = "商品数量不能为空")
    private Integer quantity;
    /**
     * 已选规格
     */
    private String selectedParam;
    /**
     * 商品价格
     */
    @NotNull(message = "商品价格不能为空")
    private BigDecimal price;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 修改时间
     */
    private Date updatedAt;


    /**
     * 订单商品id
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 订单id
     */
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * 商品id
     */
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * 商品数量
     */
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * 已选规格
     */
    public String getSelectedParam() {
        return selectedParam;
    }

    public void setSelectedParam(String selectedParam) {
        this.selectedParam = selectedParam;
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