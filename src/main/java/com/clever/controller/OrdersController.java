package com.clever.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.bean.shopping.projo.output.OrdersDetailOutput;
import com.clever.util.SpringUtil;
import com.clever.annotation.Auth;
import com.clever.annotation.AuthGroup;
import com.clever.bean.model.OnlineUser;
import com.clever.bean.model.Result;

import java.util.List;

import com.clever.bean.shopping.Orders;
import com.clever.service.OrdersService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 订单接口
 *
 * @Author xixi
 * @Date 2024-03-26 17:10:18
 */
@RestController
@Validated
@RequestMapping("/orders")
@AuthGroup(value = "clever-shopping.orders", name = "订单模块", description = "订单模块权限组")
public class OrdersController {

    @Resource
    private OrdersService ordersService;


    /**
     * 分页查询订单列表
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param userId     用户id
     * @param status     订单状态 0:未支付 1:已支付 2:已发货 3:已收货 4:已评价 5:已取消
     * @return 当前页数据
     */
    @GetMapping("/page/{pageNumber}/{pageSize}")
    @Auth(value = "clever-shopping.orders.page", name = "订单分页", description = "订单分页接口")
    public Result<Page<OrdersDetailOutput>> selectPage(@PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize, String userId, Integer status) {
        return new Result<>(ordersService.selectPage(pageNumber, pageSize, userId, status), "分页数据查询成功");
    }

    /**
     * 根据用户id获取列表
     *
     * @param userId 用户id
     * @return List<Orders> 订单列表
     */
    @GetMapping("/listByUserId/{userId}")
    @Auth(value = "clever-shopping.orders.listByUserId", name = "根据用户id获取订单列表", description = "根据用户id获取订单列表接口")
    public Result<List<Orders>> selectListByUserId(@PathVariable("userId") String userId) {
        return new Result<>(ordersService.selectListByUserId(userId), "查询成功");
    }

    /**
     * 根据订单id获取订单信息
     *
     * @param id 订单id
     * @return 订单信息
     */
    @GetMapping("/{id}")
    @Auth(value = "clever-system.orders.selectById", name = "根据订单id获取订单信息", description = "根据订单id获取订单信息接口")
    public Result<Orders> selectById(@PathVariable("id") String id) {
        return new Result<>(ordersService.selectById(id), "查询成功");
    }

    /**
     * 创建订单信息
     *
     * @param cartIds 购物车列表
     * @return 创建后的订单信息
     */
    @PostMapping("")
    @Auth(value = "clever-shopping.orders.create", name = "创建订单", description = "创建订单信息接口")
    public Result<Orders> create(@Validated List<String> cartIds) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        return new Result<>(ordersService.create(cartIds, onlineUser), "下单成功");
    }

    /**
     * 修改订单信息
     *
     * @param orders 订单实体信息
     * @return 修改后的订单信息
     */
    @PatchMapping("/{id}")
    @Auth(value = "clever-shopping.orders.update", name = "修改订单", description = "修改订单信息接口")
    public Result<Orders> update(@Validated Orders orders, @PathVariable("id") String id) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        orders.setId(id);
        return new Result<>(ordersService.update(orders, onlineUser), "修改成功");
    }

    /**
     * 保存订单信息
     *
     * @param orders 订单实体信息
     * @return 保存后的订单信息
     */
    @PostMapping("/save")
    @Auth(value = "clever-shopping.orders.save", name = "保存订单", description = "保存订单信息接口")
    public Result<Orders> save(@Validated Orders orders) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        return new Result<>(ordersService.save(orders, onlineUser), "保存成功");
    }

    /**
     * 根据订单id删除订单信息
     *
     * @param id 订单id
     */
    @DeleteMapping("/{id}")
    @Auth(value = "clever-shopping.orders.delete", name = "删除订单", description = "删除订单信息接口")
    public Result<String> delete(@PathVariable("id") String id) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        ordersService.delete(id, onlineUser);
        return Result.ofSuccess("删除成功");
    }
}
