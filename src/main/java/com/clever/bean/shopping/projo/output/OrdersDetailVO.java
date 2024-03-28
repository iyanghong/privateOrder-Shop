package com.clever.bean.shopping.projo.output;

import com.clever.bean.shopping.OrderProduct;
import com.clever.bean.shopping.Orders;

import java.io.Serializable;
import java.util.List;

/**
 * 订单详情列表
 *
 * @Author xixi
 * @Date 2024-03-27 16:14
 **/
public class OrdersDetailVO extends Orders implements Serializable {
    /**
     * 订单商品列表
     */
    private List<OrderProductDetailVO> orderProductList;

    public List<OrderProductDetailVO> getOrderProductList() {
        return orderProductList;
    }

    public void setOrderProductList(List<OrderProductDetailVO> orderProductList) {
        this.orderProductList = orderProductList;
    }
}
