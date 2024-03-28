package com.clever.bean.shopping.projo.output;

import cn.hutool.db.sql.Order;
import com.clever.bean.shopping.Addresses;
import com.clever.bean.shopping.Orders;

import java.util.List;

/**
 * @Author xixi
 * @Date 2024-03-28 10:35
 **/
public class OrderDetailVO extends Orders {
    /**
     * 商品列表
     */
    private List<OrderProductDetailVO> productList;

    /**
     * 收货地址
     */
    private Addresses addressDetail;

    public List<OrderProductDetailVO> getProductList() {
        return productList;
    }

    public void setProductList(List<OrderProductDetailVO> productList) {
        this.productList = productList;
    }

    public Addresses getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(Addresses addressDetail) {
        this.addressDetail = addressDetail;
    }
}
