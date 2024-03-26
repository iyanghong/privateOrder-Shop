package com.clever.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.util.SpringUtil;
import com.clever.annotation.Auth;
import com.clever.annotation.AuthGroup;
import com.clever.bean.model.OnlineUser;
import com.clever.bean.model.Result;

import java.util.List;

import com.clever.bean.shopping.Addresses;
import com.clever.service.AddressesService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 收货地址接口
 *
 * @Author xixi
 * @Date 2024-03-26 17:10:18
 */
@RestController
@Validated
@RequestMapping("/addresses")
@AuthGroup(value = "clever-shopping.addresses", name = "收货地址模块", description = "收货地址模块权限组")
public class AddressesController {

    @Resource
    private AddressesService addressesService;


    /**
     * 分页查询收货地址列表
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param userId 用户id
     * @param name 收货人姓名
     * @param phone 收货人电话
     * @return 当前页数据
     */
    @GetMapping("/page/{pageNumber}/{pageSize}")
    @Auth(value = "clever-shopping.addresses.page", name = "收货地址分页", description = "收货地址分页接口")
    public Result<Page<Addresses>> selectPage(@PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize,String userId,String name,String phone) {
        return new Result<>(addressesService.selectPage(pageNumber, pageSize, userId, name, phone), "分页数据查询成功");
    }
    /**
    * 根据用户id获取列表
    *
    * @param userId 用户id
    * @return List<Addresses> 收货地址列表
    */
    @GetMapping("/listByUserId/{userId}")
    @Auth(value = "clever-shopping.addresses.listByUserId", name = "根据用户id获取收货地址列表", description = "根据用户id获取收货地址列表接口")
    public Result<List<Addresses>> selectListByUserId(@PathVariable("userId") String userId) {
        return new Result<>(addressesService.selectListByUserId(userId), "查询成功");
    }

    /**
    * 根据收货地址id获取收货地址信息
    *
    * @param id 收货地址id
    * @return 收货地址信息
    */
    @GetMapping("/{id}")
    @Auth(value = "clever-system.addresses.selectById", name = "根据收货地址id获取收货地址信息", description = "根据收货地址id获取收货地址信息接口")
    public Result<Addresses> selectById(@PathVariable("id") String id) {
    return new Result<>(addressesService.selectById(id), "查询成功");
    }
    /**
    * 创建收货地址信息
    *
    * @param addresses 收货地址实体信息
    * @return 创建后的收货地址信息
    */
    @PostMapping("")
    @Auth(value = "clever-shopping.addresses.create", name = "创建收货地址", description = "创建收货地址信息接口")
    public Result<Addresses> create(@Validated Addresses addresses) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        return new Result<>(addressesService.create(addresses, onlineUser), "创建成功");
    }
    /**
    * 修改收货地址信息
    *
    * @param addresses 收货地址实体信息
    * @return 修改后的收货地址信息
    */
    @PatchMapping("/{id}")
    @Auth(value = "clever-shopping.addresses.update", name = "修改收货地址", description = "修改收货地址信息接口")
    public Result<Addresses> update(@Validated Addresses addresses, @PathVariable("id") String id) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        addresses.setId(id);
        return new Result<>(addressesService.update(addresses, onlineUser), "修改成功");
    }

    /**
     * 保存收货地址信息
     *
     * @param addresses 收货地址实体信息
     * @return 保存后的收货地址信息
     */
    @PostMapping("/save")
    @Auth(value = "clever-shopping.addresses.save", name = "保存收货地址", description = "保存收货地址信息接口")
    public Result<Addresses> save(@Validated Addresses addresses) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        return new Result<>(addressesService.save(addresses, onlineUser), "保存成功");
    }

    /**
     * 根据收货地址id删除收货地址信息
     *
     * @param id 收货地址id
     */
    @DeleteMapping("/{id}")
    @Auth(value = "clever-shopping.addresses.delete", name = "删除收货地址", description = "删除收货地址信息接口")
    public Result<String> delete(@PathVariable("id") String id) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        addressesService.delete(id, onlineUser);
        return Result.ofSuccess("删除成功");
    }
}
