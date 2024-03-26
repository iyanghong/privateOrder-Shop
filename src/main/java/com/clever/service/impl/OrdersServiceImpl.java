package com.clever.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.bean.model.OnlineUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import com.clever.mapper.OrdersMapper;
import com.clever.bean.shopping.Orders;
import com.clever.service.OrdersService;

import javax.annotation.Resource;

/**
 * 订单服务
 *
 * @Author xixi
 * @Date 2024-03-26 17:10:18
 */
@Service
public class OrdersServiceImpl implements OrdersService {

    private final static Logger log = LoggerFactory.getLogger(OrdersServiceImpl.class);

    @Resource
    private OrdersMapper ordersMapper;

    /**
     * 分页查询订单列表
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param userId 用户id
     * @param status 订单状态 0:未支付 1:已支付 2:已发货 3:已收货 4:已评价 5:已取消
     * @return Page<Orders>
     */
    @Override
    public Page<Orders> selectPage(Integer pageNumber, Integer pageSize,String userId,Integer status) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userId)) {
            queryWrapper.eq("user_id", userId);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        return ordersMapper.selectPage(new Page<Orders>(pageNumber, pageSize), queryWrapper);
    }
    /**
     * 根据订单id获取订单
     *
     * @param id 订单id
     * @return Orders 订单信息
     */
    @Override
    public Orders selectById(String id) {
        return ordersMapper.selectById(id);
    }
    /**
     * 根据用户id获取列表
     *
     * @param userId 用户id
     * @return List<Orders> 订单列表
     */
    @Override
    public List<Orders> selectListByUserId(String userId) {
        return ordersMapper.selectList(new QueryWrapper<Orders>().eq("user_id", userId).orderByAsc("id"));
    }
    /**
    * 新建订单
    *
    * @param orders 订单实体信息
    * @param onlineUser   当前登录用户
    * @return Orders 新建后的订单信息
    */
    @Override
    public Orders create(Orders orders, OnlineUser onlineUser) {
        ordersMapper.insert(orders);
        log.info("订单, 订单信息创建成功: userId={}, ordersId={}", onlineUser.getId(), orders.getId());
        return orders;
    }

    /**
    * 修改订单
    *
    * @param orders 订单实体信息
    * @param onlineUser   当前登录用户
    * @return Orders 修改后的订单信息
    */
    @Override
    public Orders update(Orders orders, OnlineUser onlineUser) {
        ordersMapper.updateById(orders);
        log.info("订单, 订单信息修改成功: userId={}, ordersId={}", onlineUser.getId(), orders.getId());
        return orders;
    }

    /**
    * 保存订单
    *
    * @param orders 订单实体信息
    * @param onlineUser 当前登录用户
    * @return Orders 保存后的订单信息
    */
    @Override
    public Orders save(Orders orders, OnlineUser onlineUser) {
        if (StringUtils.isNotBlank(orders.getId())) {
           return create(orders, onlineUser);
        }
        return update(orders, onlineUser);
    }

    /**
     * 根据订单id删除订单信息
     *
     * @param id 订单id
     * @param onlineUser 当前登录用户
     */
    @Override
    public void delete(String id, OnlineUser onlineUser) {
        ordersMapper.deleteById(id);
        log.info("订单, 订单信息删除成功: userId={}, ordersId={}", onlineUser.getId(), id);
    }

    /**
     * 根据订单id列表删除订单信息
     *
     * @param ids        订单id列表
     * @param onlineUser 当前登录用户
     */
    @Override
    public void deleteBatchIds(List<String> ids, OnlineUser onlineUser) {
        ordersMapper.deleteBatchIds(ids);
        log.info("订单, 订单信息批量删除成功: userId={}, count={}, ordersIds={}", onlineUser.getId(), ids.size(), ids.toString());
    }
    /**
     * 根据用户id删除
     *
     * @param userId 用户id
     * @param onlineUser 当前登录用户
     */
    @Override
    public void deleteByUserId(String userId, OnlineUser onlineUser) {
        ordersMapper.delete(new QueryWrapper<Orders>().eq("user_id", userId));
        log.info("订单, 订单信息根据userId删除成功: userId={}, userId={}", onlineUser.getId(), userId);
    }
}
