package com.clever.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.bean.model.OnlineUser;

import java.util.List;

import com.clever.bean.shopping.Cart;
import com.clever.bean.shopping.projo.output.CartProductDetailVO;
import org.apache.ibatis.annotations.Param;

/**
 * 购物车服务接口
 *
 * @Author xixi
 * @Date 2024-03-27 11:46:50
 */
public interface CartService {

    /**
     * 分页查询列表
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param userId     用户id
     * @return Page<Cart>
     */
    Page<CartProductDetailVO> selectPage(Integer pageNumber, Integer pageSize, String userId);

    /**
     * 根据购物车id获取购物车
     *
     * @param id 购物车id
     * @return Cart 购物车id信息
     */
    Cart selectById(String id);

    /**
     * 根据用户id获取列表
     *
     * @param userId 用户id
     * @return List<Cart> 购物车列表
     */
    List<CartProductDetailVO> selectListByUserId(String userId);

    /**
     * 根据商品id获取列表
     *
     * @param productId 商品id
     * @return List<Cart> 购物车列表
     */
    List<Cart> selectListByProductId(String productId);

    /**
     * 新建购物车
     *
     * @param productId     商品id
     * @param quantity      商品数量
     * @param selectedParam 商品规格
     * @param onlineUser    当前登录用户
     * @return Cart 新建后的购物车信息
     */
    Cart create(String productId, Integer quantity, String selectedParam, OnlineUser onlineUser);

    /**
     * 修改购物车
     *
     * @param cart       购物车实体信息
     * @param onlineUser 当前登录用户
     * @return Cart 修改后的购物车信息
     */
    Cart update(Cart cart, OnlineUser onlineUser);


    /**
     * 根据购物车id删除信息
     *
     * @param id         购物车id
     * @param onlineUser 当前登录用户
     */
    void delete(String id, OnlineUser onlineUser);

    /**
     * 根据购物车id列表删除信息
     *
     * @param ids        购物车id列表
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
     * 根据商品id删除
     *
     * @param productId  商品id
     * @param onlineUser 当前登录用户
     */
    void deleteByProductId(String productId, OnlineUser onlineUser);

    /**
     * 获取用户购物车列表与详情
     *
     * @param userId 用户id
     * @return List<CartProductDetailOutput>
     */
    List<CartProductDetailVO> selectCartProductDetailByUserId(String userId);

    /**
     * 获取购物车商品详情
     *
     * @param ids 购物车id列表
     * @return List<CartProductDetailOutput>
     */
    List<CartProductDetailVO> selectCartProductDetailByCartIds(List<String> ids);
}
