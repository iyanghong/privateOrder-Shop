package com.clever.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clever.bean.shopping.projo.output.CartProductDetailOutput;
import org.apache.ibatis.annotations.Mapper;
import com.clever.bean.shopping.Cart;

import java.util.List;


/**
 * 购物车Mapper
 *
 * @Author xixi
 * @Date 2024-03-27 11:46:50
 */
@Mapper
public interface CartMapper extends BaseMapper<Cart> {

    /**
     * 获取用户购物车列表与详情
     *
     * @param userId 用户id
     * @return List<CartProductDetailOutput>
     */
    List<CartProductDetailOutput> selectCartProductDetailByUserId(String userId);

    /**
     * 获取购物车商品详情
     *
     * @param ids 购物车id列表
     * @return List<CartProductDetailOutput>
     */
    List<CartProductDetailOutput> selectCartProductDetailByCartIds(List<String> ids);
}