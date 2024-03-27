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
public class OrdersDetailOutput extends Orders implements Serializable {
    /**
     * 订单商品列表
     */
    private List<OrderProduct> orderProductList;

    public List<OrderProduct> getOrderProductList() {
        return orderProductList;
    }

    public void setOrderProductList(List<OrderProduct> orderProductList) {
        this.orderProductList = orderProductList;
    }
}
