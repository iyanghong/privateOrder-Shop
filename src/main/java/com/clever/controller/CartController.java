package com.clever.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.util.SpringUtil;
import com.clever.annotation.Auth;
import com.clever.annotation.AuthGroup;
import com.clever.bean.model.OnlineUser;
import com.clever.bean.model.Result;

import java.util.List;

import com.clever.bean.shopping.Cart;
import com.clever.service.CartService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 购物车接口
 *
 * @Author xixi
 * @Date 2024-03-27 11:46:50
 */
@RestController
@Validated
@RequestMapping("/cart")
@AuthGroup(value = "clever-shopping.cart", name = "购物车模块", description = "购物车模块权限组")
public class CartController {

    @Resource
    private CartService cartService;


    /**
     * 分页查询购物车列表
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param userId     用户id
     * @param productId  商品id
     * @return 当前页数据
     */
    @GetMapping("/page/{pageNumber}/{pageSize}")
    @Auth(value = "clever-shopping.cart.page", name = "购物车分页", description = "购物车分页接口")
    public Result<Page<Cart>> selectPage(@PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize, String userId, String productId) {
        return new Result<>(cartService.selectPage(pageNumber, pageSize, userId, productId), "分页数据查询成功");
    }

    /**
     * 根据用户id获取列表
     *
     * @param userId 用户id
     * @return List<Cart> 购物车列表
     */
    @GetMapping("/listByUserId/{userId}")
    @Auth(value = "clever-shopping.cart.listByUserId", name = "根据用户id获取购物车列表", description = "根据用户id获取购物车列表接口")
    public Result<List<Cart>> selectListByUserId(@PathVariable("userId") String userId) {
        return new Result<>(cartService.selectListByUserId(userId), "查询成功");
    }

    /**
     * 根据商品id获取列表
     *
     * @param productId 商品id
     * @return List<Cart> 购物车列表
     */
    @GetMapping("/listByProductId/{productId}")
    @Auth(value = "clever-shopping.cart.listByProductId", name = "根据商品id获取购物车列表", description = "根据商品id获取购物车列表接口")
    public Result<List<Cart>> selectListByProductId(@PathVariable("productId") String productId) {
        return new Result<>(cartService.selectListByProductId(productId), "查询成功");
    }

    /**
     * 根据购物车id获取购物车信息
     *
     * @param id 购物车id
     * @return 购物车信息
     */
    @GetMapping("/{id}")
    @Auth(value = "clever-system.cart.selectById", name = "根据购物车id获取购物车信息", description = "根据购物车id获取购物车信息接口")
    public Result<Cart> selectById(@PathVariable("id") String id) {
        return new Result<>(cartService.selectById(id), "查询成功");
    }

    /**
     * 创建购物车信息
     *
     * @param cart 购物车实体信息
     * @return 创建后的购物车信息
     */
    @PostMapping("")
    @Auth(value = "clever-shopping.cart.create", name = "创建购物车", description = "创建购物车信息接口")
    public Result<Cart> create(@Validated Cart cart) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        return new Result<>(cartService.create(cart, onlineUser), "创建成功");
    }

    /**
     * 修改购物车信息
     *
     * @param cart 购物车实体信息
     * @return 修改后的购物车信息
     */
    @PatchMapping("/{id}")
    @Auth(value = "clever-shopping.cart.update", name = "修改购物车", description = "修改购物车信息接口")
    public Result<Cart> update(@Validated Cart cart, @PathVariable("id") String id) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        cart.setId(id);
        return new Result<>(cartService.update(cart, onlineUser), "修改成功");
    }

    /**
     * 保存购物车信息
     *
     * @param cart 购物车实体信息
     * @return 保存后的购物车信息
     */
    @PostMapping("/save")
    @Auth(value = "clever-shopping.cart.save", name = "保存购物车", description = "保存购物车信息接口")
    public Result<Cart> save(@Validated Cart cart) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        return new Result<>(cartService.save(cart, onlineUser), "保存成功");
    }

    /**
     * 根据购物车id删除购物车信息
     *
     * @param id 购物车id
     */
    @DeleteMapping("/{id}")
    @Auth(value = "clever-shopping.cart.delete", name = "删除购物车", description = "删除购物车信息接口")
    public Result<String> delete(@PathVariable("id") String id) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        cartService.delete(id, onlineUser);
        return Result.ofSuccess("删除成功");
    }
}
