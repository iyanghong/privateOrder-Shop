package com.clever.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.bean.model.OnlineUser;

import java.util.List;

import com.clever.bean.shopping.FriendRequest;
import com.clever.bean.shopping.projo.output.FriendRequestDetailVO;

/**
 * 好友申请服务接口
 *
 * @Author xixi
 * @Date 2024-03-27 17:36:54
 */
public interface FriendRequestService {

    /**
     * 发送好友申请
     *
     * @param userId  用户id
     * @param message 申请消息
     */
    void request(String userId, String message);

    /**
     * 同意好友申请
     *
     * @param id 好友申请id
     */
    void agree(String id);
    /**
     * 拒绝好友申请
     *
     * @param id 好友申请id
     */
    void refuse(String id);
    /**
     * 分页查询列表
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param userId     用户id
     * @param friendId   好友id
     * @param status     好友申请状态:0-未处理,1-已同意,2-已拒绝
     * @return Page<FriendRequest>
     */
    Page<FriendRequest> selectPage(Integer pageNumber, Integer pageSize, String userId, String friendId, Integer status);

    /**
     * 根据好友申请id获取好友申请
     *
     * @param id 好友申请id
     * @return FriendRequest 好友申请id信息
     */
    FriendRequest selectById(String id);

    /**
     * 根据用户id获取列表
     *
     * @param userId 用户id
     * @return List<FriendRequest> 好友申请列表
     */
    List<FriendRequest> selectListByUserId(String userId);

    /**
     * 根据好友id获取列表
     *
     * @param friendId 好友id
     * @return List<FriendRequest> 好友申请列表
     */
    List<FriendRequest> selectListByFriendId(String friendId);

    /**
     * 新建好友申请
     *
     * @param friendRequest 好友申请实体信息
     * @param onlineUser    当前登录用户
     * @return FriendRequest 新建后的好友申请信息
     */
    FriendRequest create(FriendRequest friendRequest, OnlineUser onlineUser);

    /**
     * 修改好友申请
     *
     * @param friendRequest 好友申请实体信息
     * @param onlineUser    当前登录用户
     * @return FriendRequest 修改后的好友申请信息
     */
    FriendRequest update(FriendRequest friendRequest, OnlineUser onlineUser);

    /**
     * 保存好友申请
     *
     * @param friendRequest 好友申请实体信息
     * @param onlineUser    当前登录用户
     * @return FriendRequest 保存后的好友申请信息
     */
    FriendRequest save(FriendRequest friendRequest, OnlineUser onlineUser);

    /**
     * 根据好友申请id删除信息
     *
     * @param id         好友申请id
     * @param onlineUser 当前登录用户
     */
    void delete(String id, OnlineUser onlineUser);

    /**
     * 根据好友申请id列表删除信息
     *
     * @param ids        好友申请id列表
     * @param onlineUser 当前登录用户
     */
    void deleteBatchIds(List<String> ids, OnlineUser onlineUser);

    /**
     * 根据用户id删除
     *
     * @param userId     用户id
     * @param onlineUser 当前登录用户
     */
    void deleteByUserId(String userId, OnlineUser onlineUser);

    /**
     * 根据好友id删除
     *
     * @param friendId   好友id
     * @param onlineUser 当前登录用户
     */
    void deleteByFriendId(String friendId, OnlineUser onlineUser);
    List<FriendRequestDetailVO> selectFriendRequestDetailListByUserId(String userId);
}
