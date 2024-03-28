package com.clever.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.bean.shopping.projo.output.ProductCategoryTreeVO;
import com.clever.util.SpringUtil;
import com.clever.annotation.Auth;
import com.clever.annotation.AuthGroup;
import com.clever.bean.model.OnlineUser;
import com.clever.bean.model.Result;

import java.util.List;

import com.clever.bean.shopping.ProductCategory;
import com.clever.service.ProductCategoryService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 商品分类接口
 *
 * @Author xixi
 * @Date 2024-03-26 17:10:18
 */
@RestController
@Validated
@RequestMapping("/productCategory")
@AuthGroup(value = "clever-shopping.productCategory", name = "商品分类模块", description = "商品分类模块权限组")
public class ProductCategoryController {

    @Resource
    private ProductCategoryService productCategoryService;

    @GetMapping("/tree")
    @Auth(value = "clever-shopping.productCategory.tree", name = "商品分类树型数据", description = "商品分类树型数据接口")
    public Result<List<ProductCategoryTreeVO>> selectTreeList(){
        return new Result<>(productCategoryService.selectTreeList(),"查询成功");
    }

    /**
     * 分页查询商品分类列表
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param name       商品分类名称
     * @param parentId   父级分类id
     * @return 当前页数据
     */
    @GetMapping("/page/{pageNumber}/{pageSize}")
    @Auth(value = "clever-shopping.productCategory.page", name = "商品分类分页", description = "商品分类分页接口")
    public Result<Page<ProductCategory>> selectPage(@PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize, String name, String parentId) {
        return new Result<>(productCategoryService.selectPage(pageNumber, pageSize, name, parentId), "分页数据查询成功");
    }

    /**
     * 根据父级分类id获取列表
     *
     * @param parentId 父级分类id
     * @return List<ProductCategory> 商品分类列表
     */
    @GetMapping("/listByParentId/{parentId}")
    @Auth(value = "clever-shopping.productCategory.listByParentId", name = "根据父级分类id获取商品分类列表", description = "根据父级分类id获取商品分类列表接口")
    public Result<List<ProductCategory>> selectListByParentId(@PathVariable("parentId") String parentId) {
        return new Result<>(productCategoryService.selectListByParentId(parentId), "查询成功");
    }

    /**
     * 根据创建者id获取列表
     *
     * @param creator 创建者id
     * @return List<ProductCategory> 商品分类列表
     */
    @GetMapping("/listByCreator/{creator}")
    @Auth(value = "clever-shopping.productCategory.listByCreator", name = "根据创建者id获取商品分类列表", description = "根据创建者id获取商品分类列表接口")
    public Result<List<ProductCategory>> selectListByCreator(@PathVariable("creator") String creator) {
        return new Result<>(productCategoryService.selectListByCreator(creator), "查询成功");
    }

    /**
     * 根据商品分类id获取商品分类信息
     *
     * @param id 商品分类id
     * @return 商品分类信息
     */
    @GetMapping("/{id}")
    @Auth(value = "clever-system.productCategory.selectById", name = "根据商品分类id获取商品分类信息", description = "根据商品分类id获取商品分类信息接口")
    public Result<ProductCategory> selectById(@PathVariable("id") String id) {
        return new Result<>(productCategoryService.selectById(id), "查询成功");
    }

    /**
     * 创建商品分类信息
     *
     * @param productCategory 商品分类实体信息
     * @return 创建后的商品分类信息
     */
    @PostMapping("")
    @Auth(value = "clever-shopping.productCategory.create", name = "创建商品分类", description = "创建商品分类信息接口")
    public Result<ProductCategory> create(@Validated ProductCategory productCategory) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        return new Result<>(productCategoryService.create(productCategory, onlineUser), "创建成功");
    }

    /**
     * 修改商品分类信息
     *
     * @param productCategory 商品分类实体信息
     * @return 修改后的商品分类信息
     */
    @PatchMapping("/{id}")
    @Auth(value = "clever-shopping.productCategory.update", name = "修改商品分类", description = "修改商品分类信息接口")
    public Result<ProductCategory> update(@Validated ProductCategory productCategory, @PathVariable("id") String id) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        productCategory.setId(id);
        return new Result<>(productCategoryService.update(productCategory, onlineUser), "修改成功");
    }

    /**
     * 保存商品分类信息
     *
     * @param productCategory 商品分类实体信息
     * @return 保存后的商品分类信息
     */
    @PostMapping("/save")
    @Auth(value = "clever-shopping.productCategory.save", name = "保存商品分类", description = "保存商品分类信息接口")
    public Result<ProductCategory> save(@Validated ProductCategory productCategory) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        return new Result<>(productCategoryService.save(productCategory, onlineUser), "保存成功");
    }

    /**
     * 根据商品分类id删除商品分类信息
     *
     * @param id 商品分类id
     */
    @DeleteMapping("/{id}")
    @Auth(value = "clever-shopping.productCategory.delete", name = "删除商品分类", description = "删除商品分类信息接口")
    public Result<String> delete(@PathVariable("id") String id) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        productCategoryService.delete(id, onlineUser);
        return Result.ofSuccess("删除成功");
    }
}
