package com.clever.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.bean.model.OnlineUser;

import java.util.List;

import com.clever.bean.shopping.ProductCategory;

/**
 * 商品分类服务接口
 *
 * @Author xixi
 * @Date 2024-03-26 17:10:18
 */
public interface ProductCategoryService {

    /**
     * 分页查询列表
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param name       商品分类名称
     * @param parentId   父级分类id
     * @return Page<ProductCategory>
     */
    Page<ProductCategory> selectPage(Integer pageNumber, Integer pageSize, String name, String parentId);

    /**
     * 根据商品分类id获取商品分类
     *
     * @param id 商品分类id
     * @return ProductCategory 商品分类id信息
     */
    ProductCategory selectById(String id);

    /**
     * 根据父级分类id获取列表
     *
     * @param parentId 父级分类id
     * @return List<ProductCategory> 商品分类列表
     */
    List<ProductCategory> selectListByParentId(String parentId);

    /**
     * 根据创建者id获取列表
     *
     * @param creator 创建者id
     * @return List<ProductCategory> 商品分类列表
     */
    List<ProductCategory> selectListByCreator(String creator);

    /**
     * 新建商品分类
     *
     * @param productCategory 商品分类实体信息
     * @param onlineUser      当前登录用户
     * @return ProductCategory 新建后的商品分类信息
     */
    ProductCategory create(ProductCategory productCategory, OnlineUser onlineUser);

    /**
     * 修改商品分类
     *
     * @param productCategory 商品分类实体信息
     * @param onlineUser      当前登录用户
     * @return ProductCategory 修改后的商品分类信息
     */
    ProductCategory update(ProductCategory productCategory, OnlineUser onlineUser);

    /**
     * 保存商品分类
     *
     * @param productCategory 商品分类实体信息
     * @param onlineUser      当前登录用户
     * @return ProductCategory 保存后的商品分类信息
     */
    ProductCategory save(ProductCategory productCategory, OnlineUser onlineUser);

    /**
     * 根据商品分类id删除信息
     *
     * @param id         商品分类id
     * @param onlineUser 当前登录用户
     */
    void delete(String id, OnlineUser onlineUser);

    /**
     * 根据商品分类id列表删除信息
     *
     * @param ids        商品分类id列表
     * @param onlineUser 当前登录用户
     */
    void deleteBatchIds(List<String> ids, OnlineUser onlineUser);

    /**
     * 根据父级分类id删除
     *
     * @param parentId   父级分类id
     * @param onlineUser 当前登录用户
     */
    void deleteByParentId(String parentId, OnlineUser onlineUser);

    /**
     * 根据创建者id删除
     *
     * @param creator    创建者id
     * @param onlineUser 当前登录用户
     */
    void deleteByCreator(String creator, OnlineUser onlineUser);

}
