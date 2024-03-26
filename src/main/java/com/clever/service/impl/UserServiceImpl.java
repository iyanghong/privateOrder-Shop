package com.clever.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.bean.model.OnlineUser;
import com.clever.bean.shopping.input.UserRegisterInput;
import com.clever.constant.CacheConstant;
import com.clever.exception.BaseException;
import com.clever.exception.ConstantException;
import com.clever.service.RedisService;
import com.clever.util.RegularUtil;
import com.clever.util.SpringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.clever.mapper.UserMapper;
import com.clever.bean.shopping.User;
import com.clever.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务
 *
 * @Author xixi
 * @Date 2024-03-26 17:10:18
 */
@Service
public class UserServiceImpl implements UserService {

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisService redis;

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
     * @return Page<User>
     */
    @Override
    public Page<User> selectPage(Integer pageNumber, Integer pageSize, String username, String nickname, Integer gender, Integer type, String phone, String email) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.eq("username", username);
        }
        if (StringUtils.isNotBlank(nickname)) {
            queryWrapper.eq("nickname", nickname);
        }
        if (gender != null) {
            queryWrapper.eq("gender", gender);
        }
        if (type != null) {
            queryWrapper.eq("type", type);
        }
        if (StringUtils.isNotBlank(phone)) {
            queryWrapper.eq("phone", phone);
        }
        if (StringUtils.isNotBlank(email)) {
            queryWrapper.eq("email", email);
        }
        return userMapper.selectPage(new Page<User>(pageNumber, pageSize), queryWrapper);
    }

    /**
     * 根据用户id获取用户
     *
     * @param id 用户id
     * @return User 用户信息
     */
    @Override
    public User selectById(String id) {
        return userMapper.selectById(id);
    }

    /**
     * 根据用户名获取信息
     *
     * @param username 用户名
     * @return User 用户信息
     */
    @Override
    public User selectByUsername(String username) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
    }

    /**
     * 根据手机号获取信息
     *
     * @param phone 手机号
     * @return User 用户信息
     */
    @Override
    public User selectByPhone(String phone) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("phone", phone));
    }

    /**
     * 根据邮箱获取信息
     *
     * @param email 邮箱
     * @return User 用户信息
     */
    @Override
    public User selectByEmail(String email) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
    }

    /**
     * 新建用户
     *
     * @param user       用户实体信息
     * @param onlineUser 当前登录用户
     * @return User 新建后的用户信息
     */
    @Override
    public User create(User user, OnlineUser onlineUser) {
        userMapper.insert(user);
        log.info("用户, 用户信息创建成功: userId={}, userId={}", onlineUser.getId(), user.getId());
        return user;
    }

    /**
     * 修改用户
     *
     * @param user       用户实体信息
     * @param onlineUser 当前登录用户
     * @return User 修改后的用户信息
     */
    @Override
    public User update(User user, OnlineUser onlineUser) {
        userMapper.updateById(user);
        log.info("用户, 用户信息修改成功: userId={}, userId={}", onlineUser.getId(), user.getId());
        return user;
    }

    /**
     * 保存用户
     *
     * @param user       用户实体信息
     * @param onlineUser 当前登录用户
     * @return User 保存后的用户信息
     */
    @Override
    public User save(User user, OnlineUser onlineUser) {
        if (StringUtils.isNotBlank(user.getId())) {
            return create(user, onlineUser);
        }
        return update(user, onlineUser);
    }

    /**
     * 根据用户id删除用户信息
     *
     * @param id         用户id
     * @param onlineUser 当前登录用户
     */
    @Override
    public void delete(String id, OnlineUser onlineUser) {
        userMapper.deleteById(id);
        log.info("用户, 用户信息删除成功: userId={}, userId={}", onlineUser.getId(), id);
    }

    /**
     * 根据用户id列表删除用户信息
     *
     * @param ids        用户id列表
     * @param onlineUser 当前登录用户
     */
    @Override
    public void deleteBatchIds(List<String> ids, OnlineUser onlineUser) {
        userMapper.deleteBatchIds(ids);
        log.info("用户, 用户信息批量删除成功: userId={}, count={}, userIds={}", onlineUser.getId(), ids.size(), ids.toString());
    }

    /**
     * 用户登录
     *
     * @param userRegisterInput 注册信息
     * @return User
     */
    @Override
    public User register(UserRegisterInput userRegisterInput) {
        Integer checkUsernameCount = userMapper.selectCount(new QueryWrapper<User>().eq("username", userRegisterInput.getUsername()));
        if (checkUsernameCount > 0) {
            throw new BaseException(ConstantException.ACCOUNT_IS_EXISTED);
        }
        if (StringUtils.isNotBlank(userRegisterInput.getPhone()) && userMapper.selectCount(new QueryWrapper<User>().eq("phone", userRegisterInput.getPhone())) > 0) {
            throw new BaseException(ConstantException.DATA_IS_EXIST.format("手机号"));
        }
        if (StringUtils.isNotBlank(userRegisterInput.getEmail()) && userMapper.selectCount(new QueryWrapper<User>().eq("email", userRegisterInput.getEmail())) > 0) {
            throw new BaseException(ConstantException.DATA_IS_EXIST.format("电子邮箱号码"));
        }

        User user = new User();
        user.setUsername(userRegisterInput.getUsername());
        user.setPassword(userRegisterInput.getPassword());
        user.setNickname(userRegisterInput.getNickname());
        user.setGender(userRegisterInput.getGender());
        user.setAvatar(userRegisterInput.getAvatar());
        user.setType(userRegisterInput.getType());
        user.setPhone(userRegisterInput.getPhone());
        user.setEmail(userRegisterInput.getEmail());
        userMapper.insert(user);
        log.info("用户注册, 用户注册成功: account={}", user.getUsername());
        return user;
    }

    /**
     * 用户登录
     *
     * @param account  账户(可为邮箱或者系统账号)
     * @param password 密码
     * @return user
     */
    @Override
    public OnlineUser login(String account, String password) {
        Date now = new Date();
        HttpServletRequest request = SpringUtil.getRequest();
        String ip = request.getRemoteAddr();
        log.info("用户登录, 用户开始登陆: account={}, ip={}", account, ip);

        // 判断用户使用什么登录
        String loginField = "username";
        if (RegularUtil.isMatching(RegularUtil.REGULAR_EMAIL, account)) {
            loginField = "email"; // 使用邮箱登录
        } else if (RegularUtil.isMatching(RegularUtil.REGULAR_PHONE, account)) {
            loginField = "phone"; // 使用手机登录
        }
        User user = userMapper.selectOne(new QueryWrapper<User>().eq(loginField, account));
        //账号不存在
        if (user == null) {
            log.error("用户登录, 用户不存在: account={}, ip={}", account, ip);
            throw new BaseException(ConstantException.USER_ACCOUNT_NOT_FOUND);
        }
        // 生成token
        String token = SpringUtil.getUuid();
        // 更新用户登录时间
        user.setLastLoginTime(now);
        // 更新用户登录ip
        user.setLoginIp(ip);
        // 创建在线用户
        OnlineUser onlineUser = new OnlineUser(user, token);
        // 设置在线用户
        redis.setString(CacheConstant.getOnlineKeyName(token), onlineUser, 3, TimeUnit.HOURS);
        // 更新用户信息
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setLastLoginTime(now);
        updateUser.setLoginIp(ip);
        userMapper.updateById(updateUser);
        log.info("用户登录, 用户登录成功: account={}, ip={}", account, ip);
        return onlineUser;
    }

    /**
     * 用户退出登录
     *
     * @param onlineUser 当前登录用户
     */
    @Override
    public void logout(OnlineUser onlineUser) {
        redis.delKeys(CacheConstant.getOnlineKeyName(onlineUser.getToken()));
        log.info("用户退出登录, 用户退出登录成功: userId={}", onlineUser.getId());
    }

    /**
     * 根据token获取登录用户信息
     *
     * @param token token
     * @return OnlineUser
     */
    @Override
    public OnlineUser getOnlineUserByToken(String token) {
        OnlineUser onlineUser = redis.getString(CacheConstant.getOnlineKeyName(token));
        if (onlineUser == null) {
            throw new BaseException(ConstantException.USER_NO_ONLINE);
        }
        return onlineUser;
    }

    /**
     * 充值金额
     * @param amount 金额
     * @param onlineUser 当前登录用户
     */
    @Override
    public void recharge(BigDecimal amount, OnlineUser onlineUser) {
        if (amount.compareTo(new BigDecimal("0.00")) < 0) {
            throw new BaseException(ConstantException.RECHARGE_AMOUNT_CANNOT_BE_LESS_THAN_0);
        }
        User updateUser = new User();
        updateUser.setId(onlineUser.getId());
        updateUser.setMoney(onlineUser.getMoney().add(amount));
        userMapper.updateById(updateUser);
        log.info("充值，充值成功，account = {},amount = {}", onlineUser.getUsername(), amount);
    }
}
