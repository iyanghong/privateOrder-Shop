package com.clever.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.util.SpringUtil;
import com.clever.annotation.Auth;
import com.clever.annotation.AuthGroup;
import com.clever.bean.model.OnlineUser;
import com.clever.bean.model.Result;

import java.util.List;

import com.clever.bean.shopping.Product;
import com.clever.service.ProductService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 商品接口
 *
 * @Author xixi
 * @Date 2024-03-26 17:10:18
 */
@RestController
@Validated
@RequestMapping("/product")
@AuthGroup(value = "clever-shopping.product", name = "商品模块", description = "商品模块权限组")
public class ProductController {

    @Resource
    private ProductService productService;


    /**
     * 分页查询商品列表
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param name 商品名称
     * @param categoryId 商品分类id
     * @return 当前页数据
     */
    @GetMapping("/page/{pageNumber}/{pageSize}")
    @Auth(value = "clever-shopping.product.page", name = "商品分页", description = "商品分页接口")
    public Result<Page<Product>> selectPage(@PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize,String name,String categoryId) {
        return new Result<>(productService.selectPage(pageNumber, pageSize, name, categoryId), "分页数据查询成功");
    }
    /**
    * 根据商品分类id获取列表
    *
    * @param categoryId 商品分类id
    * @return List<Product> 商品列表
    */
    @GetMapping("/listByCategoryId/{categoryId}")
    @Auth(value = "clever-shopping.product.listByCategoryId", name = "根据商品分类id获取商品列表", description = "根据商品分类id获取商品列表接口")
    public Result<List<Product>> selectListByCategoryId(@PathVariable("categoryId") String categoryId) {
        return new Result<>(productService.selectListByCategoryId(categoryId), "查询成功");
    }
    /**
    * 根据创建者id获取列表
    *
    * @param creator 创建者id
    * @return List<Product> 商品列表
    */
    @GetMapping("/listByCreator/{creator}")
    @Auth(value = "clever-shopping.product.listByCreator", name = "根据创建者id获取商品列表", description = "根据创建者id获取商品列表接口")
    public Result<List<Product>> selectListByCreator(@PathVariable("creator") String creator) {
        return new Result<>(productService.selectListByCreator(creator), "查询成功");
    }

    /**
    * 根据商品id获取商品信息
    *
    * @param id 商品id
    * @return 商品信息
    */
    @GetMapping("/{id}")
    @Auth(value = "clever-system.product.selectById", name = "根据商品id获取商品信息", description = "根据商品id获取商品信息接口")
    public Result<Product> selectById(@PathVariable("id") String id) {
    return new Result<>(productService.selectById(id), "查询成功");
    }
    /**
    * 创建商品信息
    *
    * @param product 商品实体信息
    * @return 创建后的商品信息
    */
    @PostMapping("")
    @Auth(value = "clever-shopping.product.create", name = "创建商品", description = "创建商品信息接口")
    public Result<Product> create(@Validated Product product) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        return new Result<>(productService.create(product, onlineUser), "创建成功");
    }
    /**
    * 修改商品信息
    *
    * @param product 商品实体信息
    * @return 修改后的商品信息
    */
    @PatchMapping("/{id}")
    @Auth(value = "clever-shopping.product.update", name = "修改商品", description = "修改商品信息接口")
    public Result<Product> update(@Validated Product product, @PathVariable("id") String id) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        product.setId(id);
        return new Result<>(productService.update(product, onlineUser), "修改成功");
    }

    /**
     * 保存商品信息
     *
     * @param product 商品实体信息
     * @return 保存后的商品信息
     */
    @PostMapping("/save")
    @Auth(value = "clever-shopping.product.save", name = "保存商品", description = "保存商品信息接口")
    public Result<Product> save(@Validated Product product) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        return new Result<>(productService.save(product, onlineUser), "保存成功");
    }

    /**
     * 根据商品id删除商品信息
     *
     * @param id 商品id
     */
    @DeleteMapping("/{id}")
    @Auth(value = "clever-shopping.product.delete", name = "删除商品", description = "删除商品信息接口")
    public Result<String> delete(@PathVariable("id") String id) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        productService.delete(id, onlineUser);
        return Result.ofSuccess("删除成功");
    }
}
