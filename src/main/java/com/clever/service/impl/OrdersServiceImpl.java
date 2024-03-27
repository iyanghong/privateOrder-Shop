package com.clever.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.bean.model.OnlineUser;
import com.clever.bean.shopping.OrderProduct;
import com.clever.bean.shopping.projo.output.CartProductDetailOutput;
import com.clever.bean.shopping.projo.output.OrdersDetailOutput;
import com.clever.exception.BaseException;
import com.clever.exception.ConstantException;
import com.clever.service.CartService;
import com.clever.service.OrderProductService;
import com.clever.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.clever.mapper.OrdersMapper;
import com.clever.bean.shopping.Orders;
import com.clever.service.OrdersService;
import org.springframework.transaction.annotation.Transactional;

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
    @Resource
    private CartService cartService;
    @Resource
    private OrderProductService orderProductService;
    @Resource
    private ProductService productService;

    /**
     * 分页查询订单列表
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param userId     用户id
     * @param status     订单状态 0:未支付 1:已支付 2:已发货 3:已收货 4:已评价 5:已取消
     * @return Page<Orders>
     */
    @Override
    public Page<OrdersDetailOutput> selectPage(Integer pageNumber, Integer pageSize, String userId, Integer status) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userId)) {
            queryWrapper.eq("user_id", userId);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }

        Page<Orders> ordersPage = ordersMapper.selectPage(new Page<Orders>(pageNumber, pageSize), queryWrapper);

        Page<OrdersDetailOutput> ordersDetailOutputPage = new Page<OrdersDetailOutput>();
        ordersDetailOutputPage.setOrders(ordersPage.getOrders());
        ordersDetailOutputPage.setCountId(ordersPage.getCountId());
        ordersDetailOutputPage.setCurrent(ordersPage.getCurrent());
        ordersDetailOutputPage.setPages(ordersPage.getPages());
        ordersDetailOutputPage.setSize(ordersPage.getSize());
        ordersDetailOutputPage.setTotal(ordersPage.getTotal());
        if (ordersPage.getRecords().isEmpty()) {
            return ordersDetailOutputPage;
        }
        List<String> ids = ordersPage.getRecords().stream().map(Orders::getId).collect(Collectors.toList());
        List<OrderProduct> orderProductList = orderProductService.selectListByOrderIds(ids);

        ordersDetailOutputPage.setRecords(ordersPage.getOrders().stream().map(orders -> {
            OrdersDetailOutput ordersDetailOutput = new OrdersDetailOutput();
            BeanUtil.copyProperties(orders, ordersDetailOutput);
            ordersDetailOutput.setOrderProductList(orderProductList.stream().filter(orderProduct -> orderProduct.getOrderId().equals(ordersDetailOutput.getId())).collect(Collectors.toList()));
            return ordersDetailOutput;
        }).collect(Collectors.toList()));

        return ordersDetailOutputPage;
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
     * @param cartIds    购物车列表
     * @param onlineUser 当前登录用户
     * @return Orders 新建后的订单信息
     */
    @Transactional
    @Override
    public Orders create(List<String> cartIds, OnlineUser onlineUser) {
        if (cartIds == null || cartIds.isEmpty()) {
            throw new BaseException(ConstantException.DATA_NOT_EXIST.format("购物车商品"));
        }

        List<CartProductDetailOutput> cartProductDetailOutputs = cartService.selectCartProductDetailByCartIds(cartIds);

        if (cartProductDetailOutputs == null || cartProductDetailOutputs.isEmpty()) {
            throw new BaseException(ConstantException.DATA_NOT_EXIST.format("购物车"));
        }

        List<CartProductDetailOutput> lackStockList = cartProductDetailOutputs.stream().filter(cartProductDetailOutput -> cartProductDetailOutput.getProductStock() < cartProductDetailOutput.getQuantity()).collect(Collectors.toList());
        if (!lackStockList.isEmpty()) {
            log.info("订单, 库存不足: userId={}, lackStockList={}", onlineUser.getId(), lackStockList.get(0).getProductName());
            throw new BaseException(ConstantException.INSUFFICIENT_INVENTORY_GOODS.format(lackStockList.get(0).getProductName()));
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartProductDetailOutput cartProductDetailOutput : cartProductDetailOutputs) {
            totalPrice = totalPrice.add(cartProductDetailOutput.getProductPrice().multiply(new BigDecimal(cartProductDetailOutput.getQuantity())));
        }
        BigDecimal userBalance = onlineUser.getMoney();
        if (userBalance.compareTo(totalPrice) < 0) {
            log.info("订单, 余额不足: userId={}, userBalance={}, totalPrice={}", onlineUser.getId(), userBalance, totalPrice);
            throw new BaseException(ConstantException.INSUFFICIENT_BALANCE);
        }


        Orders orders = new Orders();
        orders.setUserId(onlineUser.getId());
        orders.setTotalPrice(totalPrice);
        ordersMapper.insert(orders);
        // 添加订单商品
        for (CartProductDetailOutput cartProductDetailOutput : cartProductDetailOutputs) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrderId(orders.getId());
            orderProduct.setProductId(cartProductDetailOutput.getProductId());
            orderProduct.setPrice(cartProductDetailOutput.getProductPrice());
            orderProduct.setQuantity(cartProductDetailOutput.getQuantity());
            orderProduct.setSelectedParam(cartProductDetailOutput.getSelectedParam());
            orderProductService.create(orderProduct, onlineUser);
            // 扣除库存
            productService.reductionStock(orderProduct.getProductId(), orderProduct.getQuantity(), onlineUser);
        }
        log.info("订单, 订单信息创建成功: userId={}, ordersId={}", onlineUser.getId(), orders.getId());
        return orders;
    }

    /**
     * 修改订单
     *
     * @param orders     订单实体信息
     * @param onlineUser 当前登录用户
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
     * @param orders     订单实体信息
     * @param onlineUser 当前登录用户
     * @return Orders 保存后的订单信息
     */
    @Override
    public Orders save(Orders orders, OnlineUser onlineUser) {
        if (StringUtils.isNotBlank(orders.getId())) {
            return create((List<String>) orders, onlineUser);
        }
        return update(orders, onlineUser);
    }

    /**
     * 根据订单id删除订单信息
     *
     * @param id         订单id
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
     * @param userId     用户id
     * @param onlineUser 当前登录用户
     */
    @Override
    public void deleteByUserId(String userId, OnlineUser onlineUser) {
        ordersMapper.delete(new QueryWrapper<Orders>().eq("user_id", userId));
        log.info("订单, 订单信息根据userId删除成功: userId={}, userId={}", onlineUser.getId(), userId);
    }
}
