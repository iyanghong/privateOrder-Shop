package com.clever.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.bean.model.OnlineUser;
import com.clever.bean.shopping.projo.output.CartProductDetailVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import com.clever.mapper.CartMapper;
import com.clever.bean.shopping.Cart;
import com.clever.service.CartService;

import javax.annotation.Resource;

/**
 * 购物车服务
 *
 * @Author xixi
 * @Date 2024-03-27 11:46:50
 */
@Service
public class CartServiceImpl implements CartService {

    private final static Logger log = LoggerFactory.getLogger(CartServiceImpl.class);

    @Resource
    private CartMapper cartMapper;

    /**
     * 分页查询购物车列表
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param userId     用户id
     * @param productId  商品id
     * @return Page<Cart>
     */
    @Override
    public Page<Cart> selectPage(Integer pageNumber, Integer pageSize, String userId, String productId) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userId)) {
            queryWrapper.eq("user_id", userId);
        }
        if (StringUtils.isNotBlank(productId)) {
            queryWrapper.eq("product_id", productId);
        }
        return cartMapper.selectPage(new Page<Cart>(pageNumber, pageSize), queryWrapper);
    }

    /**
     * 根据购物车id获取购物车
     *
     * @param id 购物车id
     * @return Cart 购物车信息
     */
    @Override
    public Cart selectById(String id) {
        return cartMapper.selectById(id);
    }

    /**
     * 根据用户id获取列表
     *
     * @param userId 用户id
     * @return List<Cart> 购物车列表
     */
    @Override
    public List<Cart> selectListByUserId(String userId) {
        return cartMapper.selectList(new QueryWrapper<Cart>().eq("user_id", userId).orderByAsc("id"));
    }

    /**
     * 根据商品id获取列表
     *
     * @param productId 商品id
     * @return List<Cart> 购物车列表
     */
    @Override
    public List<Cart> selectListByProductId(String productId) {
        return cartMapper.selectList(new QueryWrapper<Cart>().eq("product_id", productId).orderByAsc("id"));
    }

    /**
     * 新建购物车
     *
     * @param productId     商品id
     * @param quantity      商品数量
     * @param selectedParam 商品规格
     * @param onlineUser    当前登录用户
     * @return Cart 新建后的购物车信息
     */
    @Override
    public Cart create(String productId, Integer quantity, String selectedParam, OnlineUser onlineUser) {
        Cart checkCart = cartMapper.selectOne(new QueryWrapper<Cart>().eq("user_id", onlineUser.getId()).eq("product_id", productId).eq("selected_param", selectedParam));
        if (checkCart != null) {
            checkCart.setQuantity(checkCart.getQuantity() + quantity);
            cartMapper.updateById(checkCart);
            log.info("购物车, 购物车信息修改成功: userId={}, cartId={}", onlineUser.getId(), checkCart.getId());
            return checkCart;
        }
        Cart cart = new Cart();
        cart.setUserId(onlineUser.getId());
        cart.setProductId(productId);
        cart.setSelectedParam(selectedParam);
        cart.setQuantity(quantity);
        cartMapper.insert(cart);
        log.info("购物车, 购物车信息创建成功: userId={}, cartId={}", onlineUser.getId(), cart.getId());
        return cart;
    }

    /**
     * 修改购物车
     *
     * @param cart       购物车实体信息
     * @param onlineUser 当前登录用户
     * @return Cart 修改后的购物车信息
     */
    @Override
    public Cart update(Cart cart, OnlineUser onlineUser) {
        cartMapper.updateById(cart);
        log.info("购物车, 购物车信息修改成功: userId={}, cartId={}", onlineUser.getId(), cart.getId());
        return cart;
    }

    /**
     * 根据购物车id删除购物车信息
     *
     * @param id         购物车id
     * @param onlineUser 当前登录用户
     */
    @Override
    public void delete(String id, OnlineUser onlineUser) {
        cartMapper.deleteById(id);
        log.info("购物车, 购物车信息删除成功: userId={}, cartId={}", onlineUser.getId(), id);
    }

    /**
     * 根据购物车id列表删除购物车信息
     *
     * @param ids        购物车id列表
     * @param onlineUser 当前登录用户
     */
    @Override
    public void deleteBatchIds(List<String> ids, OnlineUser onlineUser) {
        cartMapper.deleteBatchIds(ids);
        log.info("购物车, 购物车信息批量删除成功: userId={}, count={}, cartIds={}", onlineUser.getId(), ids.size(), ids.toString());
    }

    /**
     * 根据用户id删除
     *
     * @param userId     用户id
     * @param onlineUser 当前登录用户
     */
    @Override
    public void deleteByUserId(String userId, OnlineUser onlineUser) {
        cartMapper.delete(new QueryWrapper<Cart>().eq("user_id", userId));
        log.info("购物车, 购物车信息根据userId删除成功: userId={}, userId={}", onlineUser.getId(), userId);
    }

    /**
     * 根据商品id删除
     *
     * @param productId  商品id
     * @param onlineUser 当前登录用户
     */
    @Override
    public void deleteByProductId(String productId, OnlineUser onlineUser) {
        cartMapper.delete(new QueryWrapper<Cart>().eq("product_id", productId));
        log.info("购物车, 购物车信息根据productId删除成功: userId={}, productId={}", onlineUser.getId(), productId);
    }

    @Override
    public List<CartProductDetailVO> selectCartProductDetailByUserId(String userId) {
        return cartMapper.selectCartProductDetailByUserId(userId);
    }

    @Override
    public List<CartProductDetailVO> selectCartProductDetailByCartIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        return cartMapper.selectCartProductDetailByCartIds(ids);
    }
}
