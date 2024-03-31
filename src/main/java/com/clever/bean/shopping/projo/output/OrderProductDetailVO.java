package com.clever.bean.shopping.projo.output;

import com.clever.bean.shopping.OrderProduct;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author xixi
 * @Date 2024-03-28 10:25
 **/
public class OrderProductDetailVO extends OrderProduct {
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品图片
     */
    private String productImage;
    /**
     * 商品价格
     */
    private BigDecimal productPrice;
    /**
     * 商品库存
     */
    private Integer productStock;

    /**
     * 商品状态 0:下架 1:上架
     */
    private Integer productStatus;
    /**
     * 商品描述
     */
    private String productDescription;
    /**
     * 商品规格
     */
    private String productParam;
    /**
     * 商品创建时间
     */
    private Date productCreateTime;

    public Date getProductCreateTime() {
        return productCreateTime;
    }

    public void setProductCreateTime(Date productCreateTime) {
        this.productCreateTime = productCreateTime;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProductStock() {
        return productStock;
    }

    public void setProductStock(Integer productStock) {
        this.productStock = productStock;
    }

    public Integer getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(Integer productStatus) {
        this.productStatus = productStatus;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductParam() {
        return productParam;
    }

    public void setProductParam(String productParam) {
        this.productParam = productParam;
    }


}
