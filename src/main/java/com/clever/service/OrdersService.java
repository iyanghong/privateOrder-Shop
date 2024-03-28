package com.clever.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.bean.model.OnlineUser;

import java.util.List;

import com.clever.bean.shopping.Orders;
import com.clever.bean.shopping.projo.output.OrderDetailVO;
import com.clever.bean.shopping.projo.output.OrdersDetailVO;

/**
 * 订单服务接口
 *
 * @Author xixi
 * @Date 2024-03-26 17:10:18
 */
public interface OrdersService {

    /**
     * 分页查询列表
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param userId     用户id
     * @param status     订单状态 0:未支付 1:已支付 2:已发货 3:已收货 4:已评价 5:已取消
     * @return Page<Orders>
     */
    Page<OrdersDetailVO> selectPage(Integer pageNumber, Integer pageSize, String userId, Integer status);

    /**
     * 根据订单id获取订单
     *
     * @param id 订单id
     * @return Orders 订单id信息
     */
    OrderDetailVO selectById(String id);

    /**
     * 根据用户id获取列表
     *
     * @param userId 用户id
     * @return List<Orders> 订单列表
     */
    List<Orders> selectListByUserId(String userId);

    /**
     * 新建订单
     *
     * @param cartIds    购物车列表
     * @param onlineUser 当前登录用户
     * @return Orders 新建后的订单信息
     */
    Orders create(List<String> cartIds, OnlineUser onlineUser);
    /**
     * 订单支付
     * @param oderId 订单号
     * @param onlineUser 当前登录用户
     */
    void pay(String oderId, OnlineUser onlineUser);
    /**
     * 修改订单
     *
     * @param orders     订单实体信息
     * @param onlineUser 当前登录用户
     * @return Orders 修改后的订单信息
     */
    Orders update(Orders orders, OnlineUser onlineUser);

    /**
     * 保存订单
     *
     * @param orders     订单实体信息
     * @param onlineUser 当前登录用户
     * @return Orders 保存后的订单信息
     */
    Orders save(Orders orders, OnlineUser onlineUser);

    /**
     * 根据订单id删除信息
     *
     * @param id         订单id
     * @param onlineUser 当前登录用户
     */
    void delete(String id, OnlineUser onlineUser);

    /**
     * 根据订单id列表删除信息
     *
     * @param ids        订单id列表
     * @param onlineUser 当前登录用户
     */
    void deleteBatchIds(List<String> ids, OnlineUser onlineUser);

    /**
     * 根据用户id删除
     *
     * @param userId     用户id
     * @param onlineUser 当前登录用户
     */
    void deleteByUserId(String userId, OnlineUser onlineUser);

    /**
     * 取消订单
     *
     * @param orderId    订单id
     * @param onlineUser 当前登录用户
     */
    void cancel(String orderId, OnlineUser onlineUser);
    /**
     * 退款
     *
     * @param orderId    订单id
     * @param onlineUser 当前登录用户
     */
    void refund(String orderId, OnlineUser onlineUser);
}
