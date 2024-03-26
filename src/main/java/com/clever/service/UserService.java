package com.clever.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.bean.model.OnlineUser;

import java.math.BigDecimal;
import java.util.List;

import com.clever.bean.shopping.User;
import com.clever.bean.shopping.input.UserRegisterInput;

/**
 * 用户服务接口
 *
 * @Author xixi
 * @Date 2024-03-26 17:10:18
 */
public interface UserService {

    /**
     * 分页查询列表
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param username 用户名
     * @param nickname 昵称
     * @param gender 性别:0-未知,1-男,2-女
     * @param type 用户类型 0:普通用户 1:商家
     * @param phone 手机号
     * @param email 邮箱
     * @return Page<User>
     */
    Page<User> selectPage(Integer pageNumber, Integer pageSize,String username,String nickname,Integer gender,Integer type,String phone,String email);

    /**
     * 根据用户id获取用户
     *
     * @param id 用户id
     * @return User 用户id信息
     */
    User selectById(String id);

    /**
     * 根据用户名获取信息
     *
     * @param username 用户名
     * @return User 用户信息
     */
    User selectByUsername(String username);

    /**
     * 根据手机号获取信息
     *
     * @param phone 手机号
     * @return User 用户信息
     */
    User selectByPhone(String phone);

    /**
     * 根据邮箱获取信息
     *
     * @param email 邮箱
     * @return User 用户信息
     */
    User selectByEmail(String email);

    /**
     * 新建用户
     *
     * @param user 用户实体信息
     * @param onlineUser   当前登录用户
     * @return User 新建后的用户信息
     */
    User create(User user, OnlineUser onlineUser);

    /**
    * 修改用户
    *
    * @param user 用户实体信息
    * @param onlineUser   当前登录用户
    * @return User 修改后的用户信息
    */
    User update(User user, OnlineUser onlineUser);

    /**
    * 保存用户
    *
    * @param user 用户实体信息
    * @param onlineUser 当前登录用户
    * @return User 保存后的用户信息
    */
    User save(User user, OnlineUser onlineUser);

    /**
     * 根据用户id删除信息
     *
     * @param id 用户id
     * @param onlineUser 当前登录用户
     */
    void delete(String id, OnlineUser onlineUser);

    /**
     * 根据用户id列表删除信息
     *
     * @param ids        用户id列表
     * @param onlineUser 当前登录用户
     */
    void deleteBatchIds(List<String> ids, OnlineUser onlineUser);

    /**
     * 用户登录
     * @param userRegisterInput 注册信息
     * @return User
     */
    User register(UserRegisterInput userRegisterInput);

    /**
     * 用户登录
     *
     * @param account    账户(可为邮箱或者系统账号)
     * @param password   密码
     * @return user
     */
    OnlineUser login(String account, String password);

    /**
     * 登录退出
     *
     * @param onlineUser user
     */
    void logout(OnlineUser onlineUser);
    /**
     * 根据token获取登录用户信息
     * @param token token
     * @return OnlineUser
     */
    OnlineUser getOnlineUserByToken(String token);

    /**
     * 充值金额
     * @param amount 金额
     * @param onlineUser 当前登录用户
     */
    void recharge(BigDecimal amount, OnlineUser onlineUser);
}
