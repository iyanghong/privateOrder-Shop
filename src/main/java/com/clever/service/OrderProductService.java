package com.clever.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.bean.model.OnlineUser;

import java.util.List;

import com.clever.bean.shopping.OrderProduct;
import com.clever.bean.shopping.projo.output.OrderProductDetailVO;

/**
 * 订单商品服务接口
 *
 * @Author xixi
 * @Date 2024-03-27 11:46:50
 */
public interface OrderProductService {

    /**
     * 分页查询列表
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param orderId    订单id
     * @param productId  商品id
     * @return Page<OrderProduct>
     */
    Page<OrderProduct> selectPage(Integer pageNumber, Integer pageSize, String orderId, String productId);

    /**
     * 根据订单商品id获取订单商品
     *
     * @param id 订单商品id
     * @return OrderProduct 订单商品id信息
     */
    OrderProduct selectById(String id);

    /**
     * 根据订单id获取列表
     *
     * @param orderId 订单id
     * @return List<OrderProduct> 订单商品列表
     */
    List<OrderProduct> selectListByOrderId(String orderId);

    /**
     * 根据订单id列表获取列表
     *
     * @param orderIds 订单id列表
     * @return List<OrderProduct> 订单商品列表
     */
    List<OrderProductDetailVO> selectDetailListByOrderIds(List<String> orderIds);

    /**
     * 根据商品id获取列表
     *
     * @param productId 商品id
     * @return List<OrderProduct> 订单商品列表
     */
    List<OrderProduct> selectListByProductId(String productId);

    /**
     * 新建订单商品
     *
     * @param orderProduct 订单商品实体信息
     * @param onlineUser   当前登录用户
     * @return OrderProduct 新建后的订单商品信息
     */
    OrderProduct create(OrderProduct orderProduct, OnlineUser onlineUser);

    /**
     * 修改订单商品
     *
     * @param orderProduct 订单商品实体信息
     * @param onlineUser   当前登录用户
     * @return OrderProduct 修改后的订单商品信息
     */
    OrderProduct update(OrderProduct orderProduct, OnlineUser onlineUser);

    /**
     * 保存订单商品
     *
     * @param orderProduct 订单商品实体信息
     * @param onlineUser   当前登录用户
     * @return OrderProduct 保存后的订单商品信息
     */
    OrderProduct save(OrderProduct orderProduct, OnlineUser onlineUser);

    /**
     * 根据订单商品id删除信息
     *
     * @param id         订单商品id
     * @param onlineUser 当前登录用户
     */
    void delete(String id, OnlineUser onlineUser);

    /**
     * 根据订单商品id列表删除信息
     *
     * @param ids        订单商品id列表
     * @param onlineUser 当前登录用户
     */
    void deleteBatchIds(List<String> ids, OnlineUser onlineUser);

    /**
     * 根据订单id删除
     *
     * @param orderId    订单id
     * @param onlineUser 当前登录用户
     */
    void deleteByOrderId(String orderId, OnlineUser onlineUser);

    /**
     * 根据商品id删除
     *
     * @param productId  商品id
     * @param onlineUser 当前登录用户
     */
    void deleteByProductId(String productId, OnlineUser onlineUser);

}
