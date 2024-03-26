package com.clever.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.bean.model.OnlineUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import com.clever.mapper.FriendRequestMapper;
import com.clever.bean.shopping.FriendRequest;
import com.clever.service.FriendRequestService;

import javax.annotation.Resource;

/**
 * 好友申请服务
 *
 * @Author xixi
 * @Date 2024-03-26 17:10:18
 */
@Service
public class FriendRequestServiceImpl implements FriendRequestService {

    private final static Logger log = LoggerFactory.getLogger(FriendRequestServiceImpl.class);

    @Resource
    private FriendRequestMapper friendRequestMapper;

    /**
     * 分页查询好友申请列表
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param userId 用户id
     * @param friendId 好友id
     * @param status 好友申请状态 0:未处理 1:已同意 2:已拒绝
     * @return Page<FriendRequest>
     */
    @Override
    public Page<FriendRequest> selectPage(Integer pageNumber, Integer pageSize,String userId,String friendId,Integer status) {
        QueryWrapper<FriendRequest> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userId)) {
            queryWrapper.eq("user_id", userId);
        }
        if (StringUtils.isNotBlank(friendId)) {
            queryWrapper.eq("friend_id", friendId);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        return friendRequestMapper.selectPage(new Page<FriendRequest>(pageNumber, pageSize), queryWrapper);
    }
    /**
     * 根据好友申请id获取好友申请
     *
     * @param id 好友申请id
     * @return FriendRequest 好友申请信息
     */
    @Override
    public FriendRequest selectById(String id) {
        return friendRequestMapper.selectById(id);
    }
    /**
     * 根据用户id获取列表
     *
     * @param userId 用户id
     * @return List<FriendRequest> 好友申请列表
     */
    @Override
    public List<FriendRequest> selectListByUserId(String userId) {
        return friendRequestMapper.selectList(new QueryWrapper<FriendRequest>().eq("user_id", userId).orderByAsc("id"));
    }
    /**
     * 根据好友id获取列表
     *
     * @param friendId 好友id
     * @return List<FriendRequest> 好友申请列表
     */
    @Override
    public List<FriendRequest> selectListByFriendId(String friendId) {
        return friendRequestMapper.selectList(new QueryWrapper<FriendRequest>().eq("friend_id", friendId).orderByAsc("id"));
    }
    /**
    * 新建好友申请
    *
    * @param friendRequest 好友申请实体信息
    * @param onlineUser   当前登录用户
    * @return FriendRequest 新建后的好友申请信息
    */
    @Override
    public FriendRequest create(FriendRequest friendRequest, OnlineUser onlineUser) {
        friendRequestMapper.insert(friendRequest);
        log.info("好友申请, 好友申请信息创建成功: userId={}, friendRequestId={}", onlineUser.getId(), friendRequest.getId());
        return friendRequest;
    }

    /**
    * 修改好友申请
    *
    * @param friendRequest 好友申请实体信息
    * @param onlineUser   当前登录用户
    * @return FriendRequest 修改后的好友申请信息
    */
    @Override
    public FriendRequest update(FriendRequest friendRequest, OnlineUser onlineUser) {
        friendRequestMapper.updateById(friendRequest);
        log.info("好友申请, 好友申请信息修改成功: userId={}, friendRequestId={}", onlineUser.getId(), friendRequest.getId());
        return friendRequest;
    }

    /**
    * 保存好友申请
    *
    * @param friendRequest 好友申请实体信息
    * @param onlineUser 当前登录用户
    * @return FriendRequest 保存后的好友申请信息
    */
    @Override
    public FriendRequest save(FriendRequest friendRequest, OnlineUser onlineUser) {
        if (StringUtils.isNotBlank(friendRequest.getId())) {
           return create(friendRequest, onlineUser);
        }
        return update(friendRequest, onlineUser);
    }

    /**
     * 根据好友申请id删除好友申请信息
     *
     * @param id 好友申请id
     * @param onlineUser 当前登录用户
     */
    @Override
    public void delete(String id, OnlineUser onlineUser) {
        friendRequestMapper.deleteById(id);
        log.info("好友申请, 好友申请信息删除成功: userId={}, friendRequestId={}", onlineUser.getId(), id);
    }

    /**
     * 根据好友申请id列表删除好友申请信息
     *
     * @param ids        好友申请id列表
     * @param onlineUser 当前登录用户
     */
    @Override
    public void deleteBatchIds(List<String> ids, OnlineUser onlineUser) {
        friendRequestMapper.deleteBatchIds(ids);
        log.info("好友申请, 好友申请信息批量删除成功: userId={}, count={}, friendRequestIds={}", onlineUser.getId(), ids.size(), ids.toString());
    }
    /**
     * 根据用户id删除
     *
     * @param userId 用户id
     * @param onlineUser 当前登录用户
     */
    @Override
    public void deleteByUserId(String userId, OnlineUser onlineUser) {
        friendRequestMapper.delete(new QueryWrapper<FriendRequest>().eq("user_id", userId));
        log.info("好友申请, 好友申请信息根据userId删除成功: userId={}, userId={}", onlineUser.getId(), userId);
    }
    /**
     * 根据好友id删除
     *
     * @param friendId 好友id
     * @param onlineUser 当前登录用户
     */
    @Override
    public void deleteByFriendId(String friendId, OnlineUser onlineUser) {
        friendRequestMapper.delete(new QueryWrapper<FriendRequest>().eq("friend_id", friendId));
        log.info("好友申请, 好友申请信息根据friendId删除成功: userId={}, friendId={}", onlineUser.getId(), friendId);
    }
}
