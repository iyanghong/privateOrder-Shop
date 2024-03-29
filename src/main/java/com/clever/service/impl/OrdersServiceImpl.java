package com.clever.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.bean.model.OnlineUser;
import com.clever.bean.shopping.OrderProduct;
import com.clever.bean.shopping.User;
import com.clever.bean.shopping.projo.output.CartProductDetailVO;
import com.clever.bean.shopping.projo.output.OrderDetailVO;
import com.clever.bean.shopping.projo.output.OrderProductDetailVO;
import com.clever.bean.shopping.projo.output.OrdersDetailVO;
import com.clever.exception.BaseException;
import com.clever.exception.ConstantException;
import com.clever.service.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.clever.mapper.OrdersMapper;
import com.clever.bean.shopping.Orders;
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
    @Resource
    private UserService userService;

    @Resource
    private AddressesService addressesService;

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
    public Page<OrdersDetailVO> selectPage(Integer pageNumber, Integer pageSize, String userId, Integer status) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userId)) {
            queryWrapper.eq("user_id", userId);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }

        Page<Orders> ordersPage = ordersMapper.selectPage(new Page<Orders>(pageNumber, pageSize), queryWrapper);

        Page<OrdersDetailVO> ordersDetailOutputPage = new Page<OrdersDetailVO>();
        ordersDetailOutputPage.setOrders(ordersPage.getOrders());
        ordersDetailOutputPage.setCountId(ordersPage.getCountId());
        ordersDetailOutputPage.setCurrent(ordersPage.getCurrent());
        ordersDetailOutputPage.setPages(ordersPage.getPages());
        ordersDetailOutputPage.setSize(ordersPage.getSize());
        ordersDetailOutputPage.setTotal(ordersPage.getTotal());
        if (ordersPage.getRecords().isEmpty()) {
            return ordersDetailOutputPage;
        }
        ordersDetailOutputPage.setRecords(resolveDetail(ordersPage.getRecords()));
        return ordersDetailOutputPage;
    }

    /**
     * 根据订单id获取订单
     *
     * @param id 订单id
     * @return Orders 订单信息
     */
    @Override
    public OrderDetailVO selectById(String id) {
        Orders orders = ordersMapper.selectById(id);
        if (orders == null) {
            throw new BaseException(ConstantException.DATA_NOT_EXIST.format("订单"));
        }
        OrderDetailVO orderDetailVO = new OrderDetailVO();
        BeanUtil.copyProperties(orders, orderDetailVO);
        List<String> ids = new ArrayList<>();
        ids.add(id);
        List<OrderProductDetailVO> orderProductList = orderProductService.selectDetailListByOrderIds(ids);
        orderDetailVO.setProductList(orderProductList);
        if (StringUtils.isNotBlank(orders.getAddress())) {
            orderDetailVO.setAddressDetail(addressesService.selectById(orders.getAddress()));
        }

        return orderDetailVO;
    }

    /**
     * 根据用户id获取列表
     *
     * @param userId 用户id
     * @return List<Orders> 订单列表
     */
    @Override
    public List<OrdersDetailVO> selectListByUserId(String userId,Integer status) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userId)) {
            queryWrapper.eq("user_id", userId);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        List<Orders> orders = ordersMapper.selectList(queryWrapper);
        return resolveDetail(orders);
    }

    private List<OrdersDetailVO> resolveDetail(List<Orders> orderList) {
        List<String> ids = orderList.stream().map(Orders::getId).collect(Collectors.toList());
        List<OrderProductDetailVO> orderProductList = orderProductService.selectDetailListByOrderIds(ids);
        return orderList.stream().map(orders -> {
            OrdersDetailVO ordersDetailOutput = new OrdersDetailVO();
            BeanUtil.copyProperties(orders, ordersDetailOutput);
            ordersDetailOutput.setOrderProductList(orderProductList.stream().filter(orderProduct -> orderProduct.getOrderId().equals(ordersDetailOutput.getId())).collect(Collectors.toList()));
            return ordersDetailOutput;
        }).collect(Collectors.toList());
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

        List<CartProductDetailVO> cartProductDetailOutputs = cartService.selectCartProductDetailByCartIds(cartIds);

        if (cartProductDetailOutputs == null || cartProductDetailOutputs.isEmpty()) {
            throw new BaseException(ConstantException.DATA_NOT_EXIST.format("购物车"));
        }

        List<CartProductDetailVO> lackStockList = cartProductDetailOutputs.stream().filter(cartProductDetailVO -> cartProductDetailVO.getProductStock() < cartProductDetailVO.getQuantity()).collect(Collectors.toList());
        if (!lackStockList.isEmpty()) {
            log.info("订单, 库存不足: userId={}, lackStockList={}", onlineUser.getId(), lackStockList.get(0).getProductName());
            throw new BaseException(ConstantException.INSUFFICIENT_INVENTORY_GOODS.format(lackStockList.get(0).getProductName()));
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartProductDetailVO cartProductDetailVO : cartProductDetailOutputs) {
            totalPrice = totalPrice.add(cartProductDetailVO.getProductPrice().multiply(new BigDecimal(cartProductDetailVO.getQuantity())));
        }


        Orders orders = new Orders();
        orders.setUserId(onlineUser.getId());
        orders.setTotalPrice(totalPrice);
        ordersMapper.insert(orders);
        // 添加订单商品
        for (CartProductDetailVO cartProductDetailVO : cartProductDetailOutputs) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrderId(orders.getId());
            orderProduct.setProductId(cartProductDetailVO.getProductId());
            orderProduct.setPrice(cartProductDetailVO.getProductPrice());
            orderProduct.setQuantity(cartProductDetailVO.getQuantity());
            orderProduct.setSelectedParam(cartProductDetailVO.getSelectedParam());
            orderProductService.create(orderProduct, onlineUser);
            // 扣除库存
            productService.reductionStock(orderProduct.getProductId(), orderProduct.getQuantity(), onlineUser);
            cartService.delete(cartProductDetailVO.getId(), onlineUser);
        }

        log.info("订单, 订单信息创建成功: userId={}, ordersId={}", onlineUser.getId(), orders.getId());
        return orders;
    }

    /**
     * 订单支付
     *
     * @param oderId     订单号
     * @param onlineUser 当前登录用户
     */
    @Override
    public void pay(String oderId, OnlineUser onlineUser) {
        Orders orders = ordersMapper.selectById(oderId);
        if (orders == null) {
            throw new BaseException(ConstantException.DATA_NOT_EXIST.format("订单"));
        }
        if (orders.getStatus() != 0) {
            throw new BaseException(ConstantException.ORDER_STATUS_ERROR);
        }
        User currentUserData = userService.selectById(onlineUser.getId());
        BigDecimal userBalance = currentUserData.getMoney();
        if (userBalance.compareTo(orders.getTotalPrice()) < 0) {
            log.info("订单, 支付余额不足: userId={}, userBalance={}, totalPrice={}", onlineUser.getId(), userBalance, orders.getTotalPrice());
            throw new BaseException(ConstantException.INSUFFICIENT_BALANCE);
        }
        userService.deduction(orders.getTotalPrice(), onlineUser.getId());
        orders.setStatus(1);
        ordersMapper.updateById(orders);
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

    /**
     * 取消订单
     *
     * @param orderId    订单id
     * @param onlineUser 当前登录用户
     */
    @Transactional
    @Override
    public void cancel(String orderId, OnlineUser onlineUser) {
        OrderDetailVO orderDetailVO = selectById(orderId);
        if (orderDetailVO.getStatus() != 0) {
            throw new BaseException(ConstantException.ORDER_STATUS_ERROR);
        }
        orderDetailVO.getProductList().forEach(orderProductDetailVO -> {
            productService.increaseStock(orderProductDetailVO.getProductId(), orderProductDetailVO.getQuantity(), onlineUser);
        });
        Orders updateOrders = new Orders();
        updateOrders.setId(orderId);
        updateOrders.setStatus(4);
        ordersMapper.updateById(updateOrders);
        log.info("订单, 订单取消成功: userId={}, orderId={}", onlineUser.getId(), orderId);
    }

    /**
     * 退款
     *
     * @param orderId    订单id
     * @param onlineUser 当前登录用户
     */
    @Override
    public void refund(String orderId, OnlineUser onlineUser) {
        OrderDetailVO orderDetailVO = selectById(orderId);
        if (orderDetailVO.getStatus() != 1 && orderDetailVO.getStatus() != 2) {
            throw new BaseException(ConstantException.ORDER_STATUS_ERROR);
        }
        orderDetailVO.getProductList().forEach(orderProductDetailVO -> {
            productService.increaseStock(orderProductDetailVO.getProductId(), orderProductDetailVO.getQuantity(), onlineUser);
        });
        Orders updateOrders = new Orders();
        updateOrders.setId(orderId);
        updateOrders.setStatus(5);
        ordersMapper.updateById(updateOrders);
        userService.refund(orderDetailVO.getTotalPrice(), orderDetailVO.getUserId());
        log.info("订单, 订单退款成功: userId={}, orderId={}", onlineUser.getId(), orderId);
    }
}
