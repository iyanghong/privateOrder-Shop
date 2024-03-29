package com.clever.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.bean.model.OnlineUser;

import java.util.List;

import com.clever.bean.shopping.Product;

/**
 * 商品服务接口
 *
 * @Author xixi
 * @Date 2024-03-27 11:46:50
 */
public interface ProductService {

    /**
     * 分页查询列表
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param name       商品名称
     * @param status     商品状态:0-下架,1-上架
     * @param categoryId 商品分类id
     * @return Page<Product>
     */
    Page<Product> selectPage(Integer pageNumber, Integer pageSize, String name, Integer status, String categoryId);

    /**
     * 根据商品id获取商品
     *
     * @param id 商品id
     * @return Product 商品id信息
     */
    Product selectById(String id);

    /**
     * 根据商品分类id获取列表
     *
     * @param categoryId 商品分类id
     * @return List<Product> 商品列表
     */
    List<Product> selectListByCategoryId(String categoryId,String name,String status,Integer ifHot);
    List<Product> selectHotList();

    /**
     * 根据创建者id获取列表
     *
     * @param creator 创建者id
     * @return List<Product> 商品列表
     */
    List<Product> selectListByCreator(String creator);

    /**
     * 新建商品
     *
     * @param product    商品实体信息
     * @param onlineUser 当前登录用户
     * @return Product 新建后的商品信息
     */
    Product create(Product product, OnlineUser onlineUser);

    /**
     * 修改商品
     *
     * @param product    商品实体信息
     * @param onlineUser 当前登录用户
     * @return Product 修改后的商品信息
     */
    Product update(Product product, OnlineUser onlineUser);

    /**
     * 保存商品
     *
     * @param product    商品实体信息
     * @param onlineUser 当前登录用户
     * @return Product 保存后的商品信息
     */
    Product save(Product product, OnlineUser onlineUser);

    /**
     * 根据商品id删除信息
     *
     * @param id         商品id
     * @param onlineUser 当前登录用户
     */
    void delete(String id, OnlineUser onlineUser);

    /**
     * 根据商品id列表删除信息
     *
     * @param ids        商品id列表
     * @param onlineUser 当前登录用户
     */
    void deleteBatchIds(List<String> ids, OnlineUser onlineUser);

    /**
     * 根据商品分类id删除
     *
     * @param categoryId 商品分类id
     * @param onlineUser 当前登录用户
     */
    void deleteByCategoryId(String categoryId, OnlineUser onlineUser);

    /**
     * 根据创建者id删除
     *
     * @param creator    创建者id
     * @param onlineUser 当前登录用户
     */
    void deleteByCreator(String creator, OnlineUser onlineUser);


    /**
     * 商品库存减少
     *
     * @param productId  商品id
     * @param num        商品数量
     * @param onlineUser 当前用户
     */
    void reductionStock(String productId, Integer num, OnlineUser onlineUser);

    /**
     * 商品库存增加
     *
     * @param productId  商品id
     * @param num        商品数量
     * @param onlineUser 当前用户
     */
    void increaseStock(String productId, Integer num, OnlineUser onlineUser);
}
