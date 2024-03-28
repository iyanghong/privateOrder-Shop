package com.clever.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.bean.shopping.projo.input.UserRegisterInput;
import com.clever.util.SpringUtil;
import com.clever.annotation.Auth;
import com.clever.annotation.AuthGroup;
import com.clever.bean.model.OnlineUser;
import com.clever.bean.model.Result;

import java.math.BigDecimal;
import java.util.List;

import com.clever.bean.shopping.User;
import com.clever.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用户接口
 *
 * @Author xixi
 * @Date 2024-03-26 17:10:18
 */
@RestController
@Validated
@RequestMapping("/user")
@AuthGroup(value = "clever-shopping.user", name = "用户模块", description = "用户模块权限组")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 充值
     * @param amount 金额
     * @return Result<String>
     */
    @PostMapping("/recharge")
    public Result<String> recharge(@NotNull(message = "请输入充值金额") @Min(value = 1,message = "充值金额不能小于0") BigDecimal amount){
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        userService.recharge(amount, onlineUser);
        return Result.ofSuccess("充值成功");
    }
    @PostMapping("/login")
    public Result<OnlineUser> login(@NotBlank(message = "账户不能为空") String account, @NotBlank(message = "密码不能为空") String password) {
        return new Result<>(userService.login(account, password), "登录成功");
    }

    @PostMapping("/register")
    public Result<User> register(@Validated UserRegisterInput userRegisterInput) {
        return new Result<>(userService.register(userRegisterInput), "注册成功");
    }

    /**
     * 获取当前登录用户信息
     * @return Result<OnlineUser>
     */
    @GetMapping("/getCurrentUser")
    public Result<OnlineUser> getCurrentUser(){
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        User user = userService.selectById(onlineUser.getId());
        return new Result<>(new OnlineUser(user,onlineUser.getToken()));
    }

    /**
     * 分页查询用户列表
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param username   用户名
     * @param nickname   昵称
     * @param gender     性别:0-未知,1-男,2-女
     * @param type       用户类型 0:普通用户 1:商家
     * @param phone      手机号
     * @param email      邮箱
     * @return 当前页数据
     */
    @GetMapping("/page/{pageNumber}/{pageSize}")
    @Auth(value = "clever-shopping.user.page", name = "用户分页", description = "用户分页接口")
    public Result<Page<User>> selectPage(@PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize, String username, String nickname, Integer gender, Integer type, String phone, String email) {
        return new Result<>(userService.selectPage(pageNumber, pageSize, username, nickname, gender, type, phone, email), "分页数据查询成功");
    }

    /**
     * 根据用户名邀请码获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping("/username/{username}")
    @Auth(value = "clever-system.platform.selectByUsername", name = "根据用户id获取用户信息", description = "根据用户id获取用户信息接口")
    public Result<User> selectByUsername(@PathVariable("username") String username) {
        return new Result<>(userService.selectByUsername(username), "查询成功");
    }

    /**
     * 根据手机号邀请码获取用户信息
     *
     * @param phone 手机号
     * @return 用户信息
     */
    @GetMapping("/phone/{phone}")
    @Auth(value = "clever-system.platform.selectByPhone", name = "根据用户id获取用户信息", description = "根据用户id获取用户信息接口")
    public Result<User> selectByPhone(@PathVariable("phone") String phone) {
        return new Result<>(userService.selectByPhone(phone), "查询成功");
    }

    /**
     * 根据邮箱邀请码获取用户信息
     *
     * @param email 邮箱
     * @return 用户信息
     */
    @GetMapping("/email/{email}")
    @Auth(value = "clever-system.platform.selectByEmail", name = "根据用户id获取用户信息", description = "根据用户id获取用户信息接口")
    public Result<User> selectByEmail(@PathVariable("email") String email) {
        return new Result<>(userService.selectByEmail(email), "查询成功");
    }

    /**
     * 根据用户id获取用户信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    @GetMapping("/{id}")
    @Auth(value = "clever-system.user.selectById", name = "根据用户id获取用户信息", description = "根据用户id获取用户信息接口")
    public Result<User> selectById(@PathVariable("id") String id) {
        return new Result<>(userService.selectById(id), "查询成功");
    }

    /**
     * 创建用户信息
     *
     * @param user 用户实体信息
     * @return 创建后的用户信息
     */
    @PostMapping("")
    @Auth(value = "clever-shopping.user.create", name = "创建用户", description = "创建用户信息接口")
    public Result<User> create(@Validated User user) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        return new Result<>(userService.create(user, onlineUser), "创建成功");
    }

    /**
     * 修改用户信息
     *
     * @param user 用户实体信息
     * @return 修改后的用户信息
     */
    @PatchMapping("/{id}")
    @Auth(value = "clever-shopping.user.update", name = "修改用户", description = "修改用户信息接口")
    public Result<User> update(@Validated User user, @PathVariable("id") String id) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        user.setId(id);
        return new Result<>(userService.update(user, onlineUser), "修改成功");
    }

    /**
     * 保存用户信息
     *
     * @param user 用户实体信息
     * @return 保存后的用户信息
     */
    @PostMapping("/save")
    @Auth(value = "clever-shopping.user.save", name = "保存用户", description = "保存用户信息接口")
    public Result<User> save(@Validated User user) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        return new Result<>(userService.save(user, onlineUser), "保存成功");
    }

    /**
     * 根据用户id删除用户信息
     *
     * @param id 用户id
     */
    @DeleteMapping("/{id}")
    @Auth(value = "clever-shopping.user.delete", name = "删除用户", description = "删除用户信息接口")
    public Result<String> delete(@PathVariable("id") String id) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        userService.delete(id, onlineUser);
        return Result.ofSuccess("删除成功");
    }
}
