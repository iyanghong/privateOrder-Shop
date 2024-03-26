package com.clever.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.util.SpringUtil;
import com.clever.annotation.Auth;
import com.clever.annotation.AuthGroup;
import com.clever.bean.model.OnlineUser;
import com.clever.bean.model.Result;

import java.util.List;

import com.clever.bean.shopping.OrderProduct;
import com.clever.service.OrderProductService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 订单商品接口
 *
 * @Author xixi
 * @Date 2024-03-26 17:10:18
 */
@RestController
@Validated
@RequestMapping("/orderProduct")
@AuthGroup(value = "clever-shopping.orderProduct", name = "订单商品模块", description = "订单商品模块权限组")
public class OrderProductController {

    @Resource
    private OrderProductService orderProductService;


    /**
     * 分页查询订单商品列表
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param orderId 订单id
     * @param productId 商品id
     * @return 当前页数据
     */
    @GetMapping("/page/{pageNumber}/{pageSize}")
    @Auth(value = "clever-shopping.orderProduct.page", name = "订单商品分页", description = "订单商品分页接口")
    public Result<Page<OrderProduct>> selectPage(@PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize,String orderId,String productId) {
        return new Result<>(orderProductService.selectPage(pageNumber, pageSize, orderId, productId), "分页数据查询成功");
    }
    /**
    * 根据订单id获取列表
    *
    * @param orderId 订单id
    * @return List<OrderProduct> 订单商品列表
    */
    @GetMapping("/listByOrderId/{orderId}")
    @Auth(value = "clever-shopping.orderProduct.listByOrderId", name = "根据订单id获取订单商品列表", description = "根据订单id获取订单商品列表接口")
    public Result<List<OrderProduct>> selectListByOrderId(@PathVariable("orderId") String orderId) {
        return new Result<>(orderProductService.selectListByOrderId(orderId), "查询成功");
    }
    /**
    * 根据商品id获取列表
    *
    * @param productId 商品id
    * @return List<OrderProduct> 订单商品列表
    */
    @GetMapping("/listByProductId/{productId}")
    @Auth(value = "clever-shopping.orderProduct.listByProductId", name = "根据商品id获取订单商品列表", description = "根据商品id获取订单商品列表接口")
    public Result<List<OrderProduct>> selectListByProductId(@PathVariable("productId") String productId) {
        return new Result<>(orderProductService.selectListByProductId(productId), "查询成功");
    }

    /**
    * 根据订单商品id获取订单商品信息
    *
    * @param id 订单商品id
    * @return 订单商品信息
    */
    @GetMapping("/{id}")
    @Auth(value = "clever-system.orderProduct.selectById", name = "根据订单商品id获取订单商品信息", description = "根据订单商品id获取订单商品信息接口")
    public Result<OrderProduct> selectById(@PathVariable("id") String id) {
    return new Result<>(orderProductService.selectById(id), "查询成功");
    }
    /**
    * 创建订单商品信息
    *
    * @param orderProduct 订单商品实体信息
    * @return 创建后的订单商品信息
    */
    @PostMapping("")
    @Auth(value = "clever-shopping.orderProduct.create", name = "创建订单商品", description = "创建订单商品信息接口")
    public Result<OrderProduct> create(@Validated OrderProduct orderProduct) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        return new Result<>(orderProductService.create(orderProduct, onlineUser), "创建成功");
    }
    /**
    * 修改订单商品信息
    *
    * @param orderProduct 订单商品实体信息
    * @return 修改后的订单商品信息
    */
    @PatchMapping("/{id}")
    @Auth(value = "clever-shopping.orderProduct.update", name = "修改订单商品", description = "修改订单商品信息接口")
    public Result<OrderProduct> update(@Validated OrderProduct orderProduct, @PathVariable("id") String id) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        orderProduct.setId(id);
        return new Result<>(orderProductService.update(orderProduct, onlineUser), "修改成功");
    }

    /**
     * 保存订单商品信息
     *
     * @param orderProduct 订单商品实体信息
     * @return 保存后的订单商品信息
     */
    @PostMapping("/save")
    @Auth(value = "clever-shopping.orderProduct.save", name = "保存订单商品", description = "保存订单商品信息接口")
    public Result<OrderProduct> save(@Validated OrderProduct orderProduct) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        return new Result<>(orderProductService.save(orderProduct, onlineUser), "保存成功");
    }

    /**
     * 根据订单商品id删除订单商品信息
     *
     * @param id 订单商品id
     */
    @DeleteMapping("/{id}")
    @Auth(value = "clever-shopping.orderProduct.delete", name = "删除订单商品", description = "删除订单商品信息接口")
    public Result<String> delete(@PathVariable("id") String id) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        orderProductService.delete(id, onlineUser);
        return Result.ofSuccess("删除成功");
    }
}
