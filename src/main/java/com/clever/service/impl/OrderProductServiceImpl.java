package com.clever.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.bean.model.OnlineUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import com.clever.mapper.OrderProductMapper;
import com.clever.bean.shopping.OrderProduct;
import com.clever.service.OrderProductService;

import javax.annotation.Resource;

/**
 * 订单商品服务
 *
 * @Author xixi
 * @Date 2024-03-26 17:10:18
 */
@Service
public class OrderProductServiceImpl implements OrderProductService {

    private final static Logger log = LoggerFactory.getLogger(OrderProductServiceImpl.class);

    @Resource
    private OrderProductMapper orderProductMapper;

    /**
     * 分页查询订单商品列表
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param orderId 订单id
     * @param productId 商品id
     * @return Page<OrderProduct>
     */
    @Override
    public Page<OrderProduct> selectPage(Integer pageNumber, Integer pageSize,String orderId,String productId) {
        QueryWrapper<OrderProduct> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(orderId)) {
            queryWrapper.eq("order_id", orderId);
        }
        if (StringUtils.isNotBlank(productId)) {
            queryWrapper.eq("product_id", productId);
        }
        return orderProductMapper.selectPage(new Page<OrderProduct>(pageNumber, pageSize), queryWrapper);
    }
    /**
     * 根据订单商品id获取订单商品
     *
     * @param id 订单商品id
     * @return OrderProduct 订单商品信息
     */
    @Override
    public OrderProduct selectById(String id) {
        return orderProductMapper.selectById(id);
    }
    /**
     * 根据订单id获取列表
     *
     * @param orderId 订单id
     * @return List<OrderProduct> 订单商品列表
     */
    @Override
    public List<OrderProduct> selectListByOrderId(String orderId) {
        return orderProductMapper.selectList(new QueryWrapper<OrderProduct>().eq("order_id", orderId).orderByAsc("id"));
    }
    /**
     * 根据商品id获取列表
     *
     * @param productId 商品id
     * @return List<OrderProduct> 订单商品列表
     */
    @Override
    public List<OrderProduct> selectListByProductId(String productId) {
        return orderProductMapper.selectList(new QueryWrapper<OrderProduct>().eq("product_id", productId).orderByAsc("id"));
    }
    /**
    * 新建订单商品
    *
    * @param orderProduct 订单商品实体信息
    * @param onlineUser   当前登录用户
    * @return OrderProduct 新建后的订单商品信息
    */
    @Override
    public OrderProduct create(OrderProduct orderProduct, OnlineUser onlineUser) {
        orderProductMapper.insert(orderProduct);
        log.info("订单商品, 订单商品信息创建成功: userId={}, orderProductId={}", onlineUser.getId(), orderProduct.getId());
        return orderProduct;
    }

    /**
    * 修改订单商品
    *
    * @param orderProduct 订单商品实体信息
    * @param onlineUser   当前登录用户
    * @return OrderProduct 修改后的订单商品信息
    */
    @Override
    public OrderProduct update(OrderProduct orderProduct, OnlineUser onlineUser) {
        orderProductMapper.updateById(orderProduct);
        log.info("订单商品, 订单商品信息修改成功: userId={}, orderProductId={}", onlineUser.getId(), orderProduct.getId());
        return orderProduct;
    }

    /**
    * 保存订单商品
    *
    * @param orderProduct 订单商品实体信息
    * @param onlineUser 当前登录用户
    * @return OrderProduct 保存后的订单商品信息
    */
    @Override
    public OrderProduct save(OrderProduct orderProduct, OnlineUser onlineUser) {
        if (StringUtils.isNotBlank(orderProduct.getId())) {
           return create(orderProduct, onlineUser);
        }
        return update(orderProduct, onlineUser);
    }

    /**
     * 根据订单商品id删除订单商品信息
     *
     * @param id 订单商品id
     * @param onlineUser 当前登录用户
     */
    @Override
    public void delete(String id, OnlineUser onlineUser) {
        orderProductMapper.deleteById(id);
        log.info("订单商品, 订单商品信息删除成功: userId={}, orderProductId={}", onlineUser.getId(), id);
    }

    /**
     * 根据订单商品id列表删除订单商品信息
     *
     * @param ids        订单商品id列表
     * @param onlineUser 当前登录用户
     */
    @Override
    public void deleteBatchIds(List<String> ids, OnlineUser onlineUser) {
        orderProductMapper.deleteBatchIds(ids);
        log.info("订单商品, 订单商品信息批量删除成功: userId={}, count={}, orderProductIds={}", onlineUser.getId(), ids.size(), ids.toString());
    }
    /**
     * 根据订单id删除
     *
     * @param orderId 订单id
     * @param onlineUser 当前登录用户
     */
    @Override
    public void deleteByOrderId(String orderId, OnlineUser onlineUser) {
        orderProductMapper.delete(new QueryWrapper<OrderProduct>().eq("order_id", orderId));
        log.info("订单商品, 订单商品信息根据orderId删除成功: userId={}, orderId={}", onlineUser.getId(), orderId);
    }
    /**
     * 根据商品id删除
     *
     * @param productId 商品id
     * @param onlineUser 当前登录用户
     */
    @Override
    public void deleteByProductId(String productId, OnlineUser onlineUser) {
        orderProductMapper.delete(new QueryWrapper<OrderProduct>().eq("product_id", productId));
        log.info("订单商品, 订单商品信息根据productId删除成功: userId={}, productId={}", onlineUser.getId(), productId);
    }
}
