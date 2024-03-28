package com.clever.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.bean.shopping.projo.output.FriendRequestDetailVO;
import com.clever.util.SpringUtil;
import com.clever.annotation.Auth;
import com.clever.annotation.AuthGroup;
import com.clever.bean.model.OnlineUser;
import com.clever.bean.model.Result;

import java.util.List;

import com.clever.bean.shopping.FriendRequest;
import com.clever.service.FriendRequestService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;

/**
 * 好友申请接口
 *
 * @Author xixi
 * @Date 2024-03-27 17:36:54
 */
@RestController
@Validated
@RequestMapping("/friendRequest")
@AuthGroup(value = "clever-shopping.friendRequest", name = "好友申请模块", description = "好友申请模块权限组")
public class FriendRequestController {

    @Resource
    private FriendRequestService friendRequestService;


    @GetMapping("/my")
    public Result<List<FriendRequestDetailVO>> my() {
        return new Result<>(friendRequestService.selectFriendRequestDetailListByUserId(SpringUtil.getOnlineUser().getId()), "查询成功");
    }

    /**
     * 发送好友申请
     *
     * @param userId  用户id
     * @param message 申请消息
     */
    @PostMapping("/request")
    public Result<String> request(@NotBlank(message = "请输入用户id") String userId, String message) {
        friendRequestService.request(userId, message);
        return Result.ofSuccess("发送成功");
    }

    /**
     * 同意好友申请
     *
     * @param id 好友申请id
     */
    @PostMapping("/agree/{id}")
    public Result<String> agree(@NotBlank(message = "id") @PathVariable("id") String id) {
        friendRequestService.agree(id);
        return Result.ofSuccess("同意成功");
    }

    /**
     * 拒绝好友申请
     *
     * @param id 好友申请id
     */
    @PostMapping("/refuse/{id}")
    public Result<String> refuse(@NotBlank(message = "id") @PathVariable("id") String id) {
        friendRequestService.refuse(id);
        return Result.ofSuccess("拒绝请求成功");
    }


    /**
     * 分页查询好友申请列表
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param userId     用户id
     * @param friendId   好友id
     * @param status     好友申请状态:0-未处理,1-已同意,2-已拒绝
     * @return 当前页数据
     */
    @GetMapping("/page/{pageNumber}/{pageSize}")
    @Auth(value = "clever-shopping.friendRequest.page", name = "好友申请分页", description = "好友申请分页接口")
    public Result<Page<FriendRequest>> selectPage(@PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize, String userId, String friendId, Integer status) {
        return new Result<>(friendRequestService.selectPage(pageNumber, pageSize, userId, friendId, status), "分页数据查询成功");
    }

    /**
     * 根据用户id获取列表
     *
     * @param userId 用户id
     * @return List<FriendRequest> 好友申请列表
     */
    @GetMapping("/listByUserId/{userId}")
    @Auth(value = "clever-shopping.friendRequest.listByUserId", name = "根据用户id获取好友申请列表", description = "根据用户id获取好友申请列表接口")
    public Result<List<FriendRequest>> selectListByUserId(@PathVariable("userId") String userId) {
        return new Result<>(friendRequestService.selectListByUserId(userId), "查询成功");
    }

    /**
     * 根据好友id获取列表
     *
     * @param friendId 好友id
     * @return List<FriendRequest> 好友申请列表
     */
    @GetMapping("/listByFriendId/{friendId}")
    @Auth(value = "clever-shopping.friendRequest.listByFriendId", name = "根据好友id获取好友申请列表", description = "根据好友id获取好友申请列表接口")
    public Result<List<FriendRequest>> selectListByFriendId(@PathVariable("friendId") String friendId) {
        return new Result<>(friendRequestService.selectListByFriendId(friendId), "查询成功");
    }

    /**
     * 根据好友申请id获取好友申请信息
     *
     * @param id 好友申请id
     * @return 好友申请信息
     */
    @GetMapping("/{id}")
    @Auth(value = "clever-system.friendRequest.selectById", name = "根据好友申请id获取好友申请信息", description = "根据好友申请id获取好友申请信息接口")
    public Result<FriendRequestDetailVO> selectById(@PathVariable("id") String id) {
        return new Result<>(friendRequestService.selectFriendRequestDetail(id), "查询成功");
    }

    /**
     * 创建好友申请信息
     *
     * @param friendRequest 好友申请实体信息
     * @return 创建后的好友申请信息
     */
    @PostMapping("")
    @Auth(value = "clever-shopping.friendRequest.create", name = "创建好友申请", description = "创建好友申请信息接口")
    public Result<FriendRequest> create(@Validated FriendRequest friendRequest) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        return new Result<>(friendRequestService.create(friendRequest, onlineUser), "创建成功");
    }

    /**
     * 修改好友申请信息
     *
     * @param friendRequest 好友申请实体信息
     * @return 修改后的好友申请信息
     */
    @PatchMapping("/{id}")
    @Auth(value = "clever-shopping.friendRequest.update", name = "修改好友申请", description = "修改好友申请信息接口")
    public Result<FriendRequest> update(@Validated FriendRequest friendRequest, @PathVariable("id") String id) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        friendRequest.setId(id);
        return new Result<>(friendRequestService.update(friendRequest, onlineUser), "修改成功");
    }

    /**
     * 保存好友申请信息
     *
     * @param friendRequest 好友申请实体信息
     * @return 保存后的好友申请信息
     */
    @PostMapping("/save")
    @Auth(value = "clever-shopping.friendRequest.save", name = "保存好友申请", description = "保存好友申请信息接口")
    public Result<FriendRequest> save(@Validated FriendRequest friendRequest) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        return new Result<>(friendRequestService.save(friendRequest, onlineUser), "保存成功");
    }

    /**
     * 根据好友申请id删除好友申请信息
     *
     * @param id 好友申请id
     */
    @DeleteMapping("/{id}")
    @Auth(value = "clever-shopping.friendRequest.delete", name = "删除好友申请", description = "删除好友申请信息接口")
    public Result<String> delete(@PathVariable("id") String id) {
        OnlineUser onlineUser = SpringUtil.getOnlineUser();
        friendRequestService.delete(id, onlineUser);
        return Result.ofSuccess("删除成功");
    }
}
