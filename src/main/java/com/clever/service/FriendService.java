package com.clever.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.bean.model.OnlineUser;

import java.util.List;

import com.clever.bean.shopping.Friend;
import com.clever.bean.shopping.projo.output.FriendDetailOutput;

/**
 * 好友服务接口
 *
 * @Author xixi
 * @Date 2024-03-27 17:36:54
 */
public interface FriendService {

    /**
     * 分页查询列表
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param userId     用户id
     * @param friendId   好友id
     * @return Page<Friend>
     */
    Page<Friend> selectPage(Integer pageNumber, Integer pageSize, String userId, String friendId);

    /**
     * 根据好友id获取好友
     *
     * @param id 好友id
     * @return Friend 好友id信息
     */
    Friend selectById(String id);

    /**
     * 根据用户id获取列表
     *
     * @param userId 用户id
     * @return List<Friend> 好友列表
     */
    List<FriendDetailOutput> selectMyFriend(String userId);

    /**
     * 根据好友id获取列表
     *
     * @param friendId 好友id
     * @return List<Friend> 好友列表
     */
    List<Friend> selectListByFriendId(String friendId);

    /**
     * 新建好友
     *
     * @param friend     好友实体信息
     * @param onlineUser 当前登录用户
     * @return Friend 新建后的好友信息
     */
    Friend create(Friend friend, OnlineUser onlineUser);

    /**
     * 修改好友
     *
     * @param friend     好友实体信息
     * @param onlineUser 当前登录用户
     * @return Friend 修改后的好友信息
     */
    Friend update(Friend friend, OnlineUser onlineUser);

    /**
     * 保存好友
     *
     * @param friend     好友实体信息
     * @param onlineUser 当前登录用户
     * @return Friend 保存后的好友信息
     */
    Friend save(Friend friend, OnlineUser onlineUser);

    /**
     * 根据好友id删除信息
     *
     * @param id         好友id
     * @param onlineUser 当前登录用户
     */
    void delete(String id, OnlineUser onlineUser);

    /**
     * 根据好友id列表删除信息
     *
     * @param ids        好友id列表
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

}
