package com.clever.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.bean.model.OnlineUser;

import java.util.List;

import com.clever.bean.shopping.Addresses;

/**
 * 收货地址服务接口
 *
 * @Author xixi
 * @Date 2024-03-26 17:10:18
 */
public interface AddressesService {

    /**
     * 分页查询列表
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param userId 用户id
     * @param name 收货人姓名
     * @param phone 收货人电话
     * @return Page<Addresses>
     */
    Page<Addresses> selectPage(Integer pageNumber, Integer pageSize,String userId,String name,String phone);

    /**
     * 根据收货地址id获取收货地址
     *
     * @param id 收货地址id
     * @return Addresses 收货地址id信息
     */
    Addresses selectById(String id);

    /**
     * 根据用户id获取列表
     *
     * @param userId 用户id
     * @return List<Addresses> 收货地址列表
     */
    List<Addresses> selectListByUserId(String userId);

    /**
     * 新建收货地址
     *
     * @param addresses 收货地址实体信息
     * @param onlineUser   当前登录用户
     * @return Addresses 新建后的收货地址信息
     */
    Addresses create(Addresses addresses, OnlineUser onlineUser);

    /**
    * 修改收货地址
    *
    * @param addresses 收货地址实体信息
    * @param onlineUser   当前登录用户
    * @return Addresses 修改后的收货地址信息
    */
    Addresses update(Addresses addresses, OnlineUser onlineUser);

    /**
    * 保存收货地址
    *
    * @param addresses 收货地址实体信息
    * @param onlineUser 当前登录用户
    * @return Addresses 保存后的收货地址信息
    */
    Addresses save(Addresses addresses, OnlineUser onlineUser);

    /**
     * 根据收货地址id删除信息
     *
     * @param id 收货地址id
     * @param onlineUser 当前登录用户
     */
    void delete(String id, OnlineUser onlineUser);

    /**
     * 根据收货地址id列表删除信息
     *
     * @param ids        收货地址id列表
     * @param onlineUser 当前登录用户
     */
    void deleteBatchIds(List<String> ids, OnlineUser onlineUser);
    /**
     * 根据用户id删除
     *
     * @param userId 用户id
     * @param onlineUser 当前登录用户
     */
    void deleteByUserId(String userId, OnlineUser onlineUser);

}
