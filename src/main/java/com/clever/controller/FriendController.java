package com.clever.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.bean.shopping.projo.output.FriendDetailVO;
import com.clever.bean.shopping.projo.output.FriendRequestDetailVO;
import com.clever.util.SpringUtil;
import com.clever.annotation.Auth;
import com.clever.annotation.AuthGroup;
import com.clever.bean.model.OnlineUser;
import com.clever.bean.model.Result;

import java.util.List;

import com.clever.bean.shopping.Friend;
import com.clever.service.FriendService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 好友接口
 *
 * @Author xixi
 * @Date 2024-03-27 17:36:54
 */
@RestController
@Validated
@RequestMapping("/friend")
@AuthGroup(value = "clever-shopping.friend", name = "好友模块", description = "好友模块权限组")
public class FriendController {

    @Resource
    private FriendService friendService;

    @GetMapping("/my")
    public Result<List<FriendDetailVO>> my() {
        return new Result<>(friendService.selectFriendDetailListByUserId(SpringUtil.getOnlineUser().getId()), "查询成功");
    }
    /**
     * 分页查询好友列表
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param userId     用户id
     * @param friendId   好友id
     * @return 当前页数据
     */
    @GetMapping("/page/{pageNumber}/{pageSize}")
    @Auth(value = "clever-shopping.friend.page", name = "好友分页", description = "好友分页接口")
    public Result<Page<Friend>> selectPage(@PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize, String userId, String friendId) {
        return new Result<>(friendService.selectPage(pageNumber, pageSize, userId, friendId), "分页数据查询成功");
    }

    /**
     * 查询我的好友
     *
     * @return List<Friend> 好友列表
     */
    @GetMapping("/selectMyFriend")
    @Auth(value = "clever-shopping.friend.selectMyFriend", name = "根据用户id获取好友列表", description = "根据用户id获取好友列表接口")
    public Result<List<FriendDetailVO>> selectMyFriend() {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        return new Result<>(friendService.selectMyFriend(onlineUser.getId()), "查询成功");
    }

    /**
     * 根据好友id获取列表
     *
     * @param friendId 好友id
     * @return List<Friend> 好友列表
     */
    @GetMapping("/listByFriendId/{friendId}")
    @Auth(value = "clever-shopping.friend.listByFriendId", name = "根据好友id获取好友列表", description = "根据好友id获取好友列表接口")
    public Result<List<Friend>> selectListByFriendId(@PathVariable("friendId") String friendId) {
        return new Result<>(friendService.selectListByFriendId(friendId), "查询成功");
    }

    /**
     * 根据好友id获取好友信息
     *
     * @param id 好友id
     * @return 好友信息
     */
    @GetMapping("/{id}")
    @Auth(value = "clever-system.friend.selectById", name = "根据好友id获取好友信息", description = "根据好友id获取好友信息接口")
    public Result<Friend> selectById(@PathVariable("id") String id) {
        return new Result<>(friendService.selectById(id), "查询成功");
    }

    /**
     * 创建好友信息
     *
     * @param friend 好友实体信息
     * @return 创建后的好友信息
     */
    @PostMapping("")
    @Auth(value = "clever-shopping.friend.create", name = "创建好友", description = "创建好友信息接口")
    public Result<Friend> create(@Validated Friend friend) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        return new Result<>(friendService.create(friend, onlineUser), "创建成功");
    }

    /**
     * 修改好友信息
     *
     * @param friend 好友实体信息
     * @return 修改后的好友信息
     */
    @PatchMapping("/{id}")
    @Auth(value = "clever-shopping.friend.update", name = "修改好友", description = "修改好友信息接口")
    public Result<Friend> update(@Validated Friend friend, @PathVariable("id") String id) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        friend.setId(id);
        return new Result<>(friendService.update(friend, onlineUser), "修改成功");
    }

    /**
     * 保存好友信息
     *
     * @param friend 好友实体信息
     * @return 保存后的好友信息
     */
    @PostMapping("/save")
    @Auth(value = "clever-shopping.friend.save", name = "保存好友", description = "保存好友信息接口")
    public Result<Friend> save(@Validated Friend friend) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        return new Result<>(friendService.save(friend, onlineUser), "保存成功");
    }

    /**
     * 根据好友id删除好友信息
     *
     * @param id 好友id
     */
    @DeleteMapping("/{id}")
    @Auth(value = "clever-shopping.friend.delete", name = "删除好友", description = "删除好友信息接口")
    public Result<String> delete(@PathVariable("id") String id) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        friendService.delete(id, onlineUser);
        return Result.ofSuccess("删除成功");
    }
}
