package com.clever.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.bean.model.OnlineUser;
import com.clever.bean.shopping.projo.output.ProductCategoryTreeVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.clever.mapper.ProductCategoryMapper;
import com.clever.bean.shopping.ProductCategory;
import com.clever.service.ProductCategoryService;

import javax.annotation.Resource;

/**
 * 商品分类服务
 *
 * @Author xixi
 * @Date 2024-03-26 17:10:18
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final static Logger log = LoggerFactory.getLogger(ProductCategoryServiceImpl.class);

    @Resource
    private ProductCategoryMapper productCategoryMapper;

    /**
     * 分页查询商品分类列表
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param name       商品分类名称
     * @param parentId   父级分类id
     * @return Page<ProductCategory>
     */
    @Override
    public Page<ProductCategory> selectPage(Integer pageNumber, Integer pageSize, String name, String parentId) {
        QueryWrapper<ProductCategory> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.eq("name", name);
        }
        if (StringUtils.isNotBlank(parentId)) {
            queryWrapper.eq("parent_id", parentId);
        }
        return productCategoryMapper.selectPage(new Page<ProductCategory>(pageNumber, pageSize), queryWrapper);
    }

    /**
     * 根据商品分类id获取商品分类
     *
     * @param id 商品分类id
     * @return ProductCategory 商品分类信息
     */
    @Override
    public ProductCategory selectById(String id) {
        return productCategoryMapper.selectById(id);
    }

    /**
     * 根据父级分类id获取列表
     *
     * @param parentId 父级分类id
     * @return List<ProductCategory> 商品分类列表
     */
    @Override
    public List<ProductCategory> selectListByParentId(String parentId) {
        return productCategoryMapper.selectList(new QueryWrapper<ProductCategory>().eq("parent_id", parentId).orderByAsc("id"));
    }

    /**
     * 根据创建者id获取列表
     *
     * @param creator 创建者id
     * @return List<ProductCategory> 商品分类列表
     */
    @Override
    public List<ProductCategory> selectListByCreator(String creator) {
        return productCategoryMapper.selectList(new QueryWrapper<ProductCategory>().eq("creator", creator).orderByAsc("id"));
    }

    /**
     * 新建商品分类
     *
     * @param productCategory 商品分类实体信息
     * @param onlineUser      当前登录用户
     * @return ProductCategory 新建后的商品分类信息
     */
    @Override
    public ProductCategory create(ProductCategory productCategory, OnlineUser onlineUser) {
        if (StringUtils.isBlank(productCategory.getParentId())){
            productCategory.setParentId("-1");
        }
        productCategoryMapper.insert(productCategory);
        log.info("商品分类, 商品分类信息创建成功: userId={}, productCategoryId={}", onlineUser.getId(), productCategory.getId());
        return productCategory;
    }

    /**
     * 修改商品分类
     *
     * @param productCategory 商品分类实体信息
     * @param onlineUser      当前登录用户
     * @return ProductCategory 修改后的商品分类信息
     */
    @Override
    public ProductCategory update(ProductCategory productCategory, OnlineUser onlineUser) {
        productCategoryMapper.updateById(productCategory);
        log.info("商品分类, 商品分类信息修改成功: userId={}, productCategoryId={}", onlineUser.getId(), productCategory.getId());
        return productCategory;
    }

    /**
     * 保存商品分类
     *
     * @param productCategory 商品分类实体信息
     * @param onlineUser      当前登录用户
     * @return ProductCategory 保存后的商品分类信息
     */
    @Override
    public ProductCategory save(ProductCategory productCategory, OnlineUser onlineUser) {
        if (StringUtils.isNotBlank(productCategory.getId())) {
            return create(productCategory, onlineUser);
        }
        return update(productCategory, onlineUser);
    }

    /**
     * 根据商品分类id删除商品分类信息
     *
     * @param id         商品分类id
     * @param onlineUser 当前登录用户
     */
    @Override
    public void delete(String id, OnlineUser onlineUser) {
        productCategoryMapper.deleteById(id);
        log.info("商品分类, 商品分类信息删除成功: userId={}, productCategoryId={}", onlineUser.getId(), id);
    }

    /**
     * 根据商品分类id列表删除商品分类信息
     *
     * @param ids        商品分类id列表
     * @param onlineUser 当前登录用户
     */
    @Override
    public void deleteBatchIds(List<String> ids, OnlineUser onlineUser) {
        productCategoryMapper.deleteBatchIds(ids);
        log.info("商品分类, 商品分类信息批量删除成功: userId={}, count={}, productCategoryIds={}", onlineUser.getId(), ids.size(), ids.toString());
    }

    /**
     * 根据父级分类id删除
     *
     * @param parentId   父级分类id
     * @param onlineUser 当前登录用户
     */
    @Override
    public void deleteByParentId(String parentId, OnlineUser onlineUser) {
        productCategoryMapper.delete(new QueryWrapper<ProductCategory>().eq("parent_id", parentId));
        log.info("商品分类, 商品分类信息根据parentId删除成功: userId={}, parentId={}", onlineUser.getId(), parentId);
    }

    /**
     * 根据创建者id删除
     *
     * @param creator    创建者id
     * @param onlineUser 当前登录用户
     */
    @Override
    public void deleteByCreator(String creator, OnlineUser onlineUser) {
        productCategoryMapper.delete(new QueryWrapper<ProductCategory>().eq("creator", creator));
        log.info("商品分类, 商品分类信息根据creator删除成功: userId={}, creator={}", onlineUser.getId(), creator);
    }

    /**
     * 查询树形列表
     *
     * @return List<ProductCategoryTreeVO> 商品分类树形列表
     */
    @Override
    public List<ProductCategoryTreeVO> selectTreeList() {
        return selectTreeListRecursion("-1");
    }

    /**
     * 查询树形列表 递归主函数
     * @param parentId 父级分类id
     * @return List<ProductCategoryTreeVO> 商品分类树形列表
     */
    private List<ProductCategoryTreeVO> selectTreeListRecursion(String parentId) {
        List<ProductCategory> productCategoryList = productCategoryMapper.selectList(new QueryWrapper<ProductCategory>().eq("parent_id", parentId).orderByAsc("sort_no"));
        List<ProductCategoryTreeVO> list = productCategoryList.stream().map(productCategory -> {
            ProductCategoryTreeVO productCategoryTreeVO = new ProductCategoryTreeVO();
            BeanUtil.copyProperties(productCategory, productCategoryTreeVO);
            productCategoryTreeVO.setChildren(selectTreeListRecursion(productCategoryTreeVO.getId()));
            return productCategoryTreeVO;
        }).collect(Collectors.toList());
        return list;
    }
}
