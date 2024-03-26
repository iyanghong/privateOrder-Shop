package com.clever.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.bean.model.OnlineUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import com.clever.mapper.AddressesMapper;
import com.clever.bean.shopping.Addresses;
import com.clever.service.AddressesService;

import javax.annotation.Resource;

/**
 * 收货地址服务
 *
 * @Author xixi
 * @Date 2024-03-26 17:10:18
 */
@Service
public class AddressesServiceImpl implements AddressesService {

    private final static Logger log = LoggerFactory.getLogger(AddressesServiceImpl.class);

    @Resource
    private AddressesMapper addressesMapper;

    /**
     * 分页查询收货地址列表
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param userId 用户id
     * @param name 收货人姓名
     * @param phone 收货人电话
     * @return Page<Addresses>
     */
    @Override
    public Page<Addresses> selectPage(Integer pageNumber, Integer pageSize,String userId,String name,String phone) {
        QueryWrapper<Addresses> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userId)) {
            queryWrapper.eq("user_id", userId);
        }
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.eq("name", name);
        }
        if (StringUtils.isNotBlank(phone)) {
            queryWrapper.eq("phone", phone);
        }
        return addressesMapper.selectPage(new Page<Addresses>(pageNumber, pageSize), queryWrapper);
    }
    /**
     * 根据收货地址id获取收货地址
     *
     * @param id 收货地址id
     * @return Addresses 收货地址信息
     */
    @Override
    public Addresses selectById(String id) {
        return addressesMapper.selectById(id);
    }
    /**
     * 根据用户id获取列表
     *
     * @param userId 用户id
     * @return List<Addresses> 收货地址列表
     */
    @Override
    public List<Addresses> selectListByUserId(String userId) {
        return addressesMapper.selectList(new QueryWrapper<Addresses>().eq("user_id", userId).orderByAsc("id"));
    }
    /**
    * 新建收货地址
    *
    * @param addresses 收货地址实体信息
    * @param onlineUser   当前登录用户
    * @return Addresses 新建后的收货地址信息
    */
    @Override
    public Addresses create(Addresses addresses, OnlineUser onlineUser) {
        addressesMapper.insert(addresses);
        log.info("收货地址, 收货地址信息创建成功: userId={}, addressesId={}", onlineUser.getId(), addresses.getId());
        return addresses;
    }

    /**
    * 修改收货地址
    *
    * @param addresses 收货地址实体信息
    * @param onlineUser   当前登录用户
    * @return Addresses 修改后的收货地址信息
    */
    @Override
    public Addresses update(Addresses addresses, OnlineUser onlineUser) {
        addressesMapper.updateById(addresses);
        log.info("收货地址, 收货地址信息修改成功: userId={}, addressesId={}", onlineUser.getId(), addresses.getId());
        return addresses;
    }

    /**
    * 保存收货地址
    *
    * @param addresses 收货地址实体信息
    * @param onlineUser 当前登录用户
    * @return Addresses 保存后的收货地址信息
    */
    @Override
    public Addresses save(Addresses addresses, OnlineUser onlineUser) {
        if (StringUtils.isNotBlank(addresses.getId())) {
           return create(addresses, onlineUser);
        }
        return update(addresses, onlineUser);
    }

    /**
     * 根据收货地址id删除收货地址信息
     *
     * @param id 收货地址id
     * @param onlineUser 当前登录用户
     */
    @Override
    public void delete(String id, OnlineUser onlineUser) {
        addressesMapper.deleteById(id);
        log.info("收货地址, 收货地址信息删除成功: userId={}, addressesId={}", onlineUser.getId(), id);
    }

    /**
     * 根据收货地址id列表删除收货地址信息
     *
     * @param ids        收货地址id列表
     * @param onlineUser 当前登录用户
     */
    @Override
    public void deleteBatchIds(List<String> ids, OnlineUser onlineUser) {
        addressesMapper.deleteBatchIds(ids);
        log.info("收货地址, 收货地址信息批量删除成功: userId={}, count={}, addressesIds={}", onlineUser.getId(), ids.size(), ids.toString());
    }
    /**
     * 根据用户id删除
     *
     * @param userId 用户id
     * @param onlineUser 当前登录用户
     */
    @Override
    public void deleteByUserId(String userId, OnlineUser onlineUser) {
        addressesMapper.delete(new QueryWrapper<Addresses>().eq("user_id", userId));
        log.info("收货地址, 收货地址信息根据userId删除成功: userId={}, userId={}", onlineUser.getId(), userId);
    }
}
