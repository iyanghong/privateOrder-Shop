package com.clever.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.bean.model.OnlineUser;
import com.clever.exception.BaseException;
import com.clever.exception.ConstantException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import com.clever.mapper.ProductMapper;
import com.clever.bean.shopping.Product;
import com.clever.service.ProductService;

import javax.annotation.Resource;

/**
 * 商品服务
 *
 * @Author xixi
 * @Date 2024-03-27 11:46:50
 */
@Service
public class ProductServiceImpl implements ProductService {

    private final static Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Resource
    private ProductMapper productMapper;

    /**
     * 分页查询商品列表
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param name       商品名称
     * @param status     商品状态:0-下架,1-上架
     * @param categoryId 商品分类id
     * @return Page<Product>
     */
    @Override
    public Page<Product> selectPage(Integer pageNumber, Integer pageSize, String name, Integer status, String categoryId) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.eq("name", name);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        if (StringUtils.isNotBlank(categoryId)) {
            queryWrapper.eq("category_id", categoryId);
        }
        return productMapper.selectPage(new Page<Product>(pageNumber, pageSize), queryWrapper);
    }

    /**
     * 根据商品id获取商品
     *
     * @param id 商品id
     * @return Product 商品信息
     */
    @Override
    public Product selectById(String id) {
        return productMapper.selectById(id);
    }

    /**
     * 根据商品分类id获取列表
     *
     * @param categoryId 商品分类id
     * @return List<Product> 商品列表
     */
    @Override
    public List<Product> selectListByCategoryId(String categoryId) {
        return productMapper.selectList(new QueryWrapper<Product>().eq("category_id", categoryId).orderByAsc("id"));
    }

    /**
     * 根据创建者id获取列表
     *
     * @param creator 创建者id
     * @return List<Product> 商品列表
     */
    @Override
    public List<Product> selectListByCreator(String creator) {
        return productMapper.selectList(new QueryWrapper<Product>().eq("creator", creator).orderByAsc("id"));
    }

    /**
     * 新建商品
     *
     * @param product    商品实体信息
     * @param onlineUser 当前登录用户
     * @return Product 新建后的商品信息
     */
    @Override
    public Product create(Product product, OnlineUser onlineUser) {
        productMapper.insert(product);
        log.info("商品, 商品信息创建成功: userId={}, productId={}", onlineUser.getId(), product.getId());
        return product;
    }

    /**
     * 修改商品
     *
     * @param product    商品实体信息
     * @param onlineUser 当前登录用户
     * @return Product 修改后的商品信息
     */
    @Override
    public Product update(Product product, OnlineUser onlineUser) {
        productMapper.updateById(product);
        log.info("商品, 商品信息修改成功: userId={}, productId={}", onlineUser.getId(), product.getId());
        return product;
    }

    /**
     * 保存商品
     *
     * @param product    商品实体信息
     * @param onlineUser 当前登录用户
     * @return Product 保存后的商品信息
     */
    @Override
    public Product save(Product product, OnlineUser onlineUser) {
        if (StringUtils.isNotBlank(product.getId())) {
            return create(product, onlineUser);
        }
        return update(product, onlineUser);
    }

    /**
     * 根据商品id删除商品信息
     *
     * @param id         商品id
     * @param onlineUser 当前登录用户
     */
    @Override
    public void delete(String id, OnlineUser onlineUser) {
        productMapper.deleteById(id);
        log.info("商品, 商品信息删除成功: userId={}, productId={}", onlineUser.getId(), id);
    }

    /**
     * 根据商品id列表删除商品信息
     *
     * @param ids        商品id列表
     * @param onlineUser 当前登录用户
     */
    @Override
    public void deleteBatchIds(List<String> ids, OnlineUser onlineUser) {
        productMapper.deleteBatchIds(ids);
        log.info("商品, 商品信息批量删除成功: userId={}, count={}, productIds={}", onlineUser.getId(), ids.size(), ids.toString());
    }

    /**
     * 根据商品分类id删除
     *
     * @param categoryId 商品分类id
     * @param onlineUser 当前登录用户
     */
    @Override
    public void deleteByCategoryId(String categoryId, OnlineUser onlineUser) {
        productMapper.delete(new QueryWrapper<Product>().eq("category_id", categoryId));
        log.info("商品, 商品信息根据categoryId删除成功: userId={}, categoryId={}", onlineUser.getId(), categoryId);
    }

    /**
     * 根据创建者id删除
     *
     * @param creator    创建者id
     * @param onlineUser 当前登录用户
     */
    @Override
    public void deleteByCreator(String creator, OnlineUser onlineUser) {
        productMapper.delete(new QueryWrapper<Product>().eq("creator", creator));
        log.info("商品, 商品信息根据creator删除成功: userId={}, creator={}", onlineUser.getId(), creator);
    }

    /**
     * 商品库存减少
     *
     * @param productId  商品id
     * @param num        商品数量
     * @param onlineUser 当前用户
     */
    @Override
    public void reductionStock(String productId, Integer num, OnlineUser onlineUser) {
        Product product = selectById(productId);
        if (product != null) {
            if (product.getStock() < num) {
                throw new BaseException(ConstantException.INSUFFICIENT_INVENTORY_GOODS.format(product.getName()));
            }
            product.setStock(product.getStock() - num);
            update(product, onlineUser);
        }
    }

    /**
     * 商品库存增加
     *
     * @param productId  商品id
     * @param num        商品数量
     * @param onlineUser 当前用户
     */
    @Override
    public void increaseStock(String productId, Integer num, OnlineUser onlineUser) {
        Product product = selectById(productId);
        if (product != null) {
            product.setStock(product.getStock() + num);
            update(product, onlineUser);
        }
    }
}
