package com.clever.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clever.bean.model.OnlineUser;
import com.clever.bean.shopping.projo.output.FriendDetailOutput;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import com.clever.mapper.FriendMapper;
import com.clever.bean.shopping.Friend;
import com.clever.service.FriendService;

import javax.annotation.Resource;

/**
 * 好友服务
 *
 * @Author xixi
 * @Date 2024-03-27 17:36:54
 */
@Service
public class FriendServiceImpl implements FriendService {

    private final static Logger log = LoggerFactory.getLogger(FriendServiceImpl.class);

    @Resource
    private FriendMapper friendMapper;

    /**
     * 分页查询好友列表
     *
     * @param pageNumber 页码
     * @param pageSize   每页记录数
     * @param userId     用户id
     * @param friendId   好友id
     * @return Page<Friend>
     */
    @Override
    public Page<Friend> selectPage(Integer pageNumber, Integer pageSize, String userId, String friendId) {
        QueryWrapper<Friend> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userId)) {
            queryWrapper.eq("user_id", userId);
        }
        if (StringUtils.isNotBlank(friendId)) {
            queryWrapper.eq("friend_id", friendId);
        }
        return friendMapper.selectPage(new Page<Friend>(pageNumber, pageSize), queryWrapper);
    }

    /**
     * 根据好友id获取好友
     *
     * @param id 好友id
     * @return Friend 好友信息
     */
    @Override
    public Friend selectById(String id) {
        return friendMapper.selectById(id);
    }

    /**
     * 根据用户id获取列表
     *
     * @param userId 用户id
     * @return List<Friend> 好友列表
     */
    @Override
    public List<FriendDetailOutput> selectMyFriend(String userId) {
        return friendMapper.selectFriendDetailListByUserId(userId);
    }

    /**
     * 根据好友id获取列表
     *
     * @param friendId 好友id
     * @return List<Friend> 好友列表
     */
    @Override
    public List<Friend> selectListByFriendId(String friendId) {
        return friendMapper.selectList(new QueryWrapper<Friend>().eq("friend_id", friendId).orderByAsc("id"));
    }

    /**
     * 新建好友
     *
     * @param friend     好友实体信息
     * @param onlineUser 当前登录用户
     * @return Friend 新建后的好友信息
     */
    @Override
    public Friend create(Friend friend, OnlineUser onlineUser) {
        friendMapper.insert(friend);
        log.info("好友, 好友信息创建成功: userId={}, friendId={}", onlineUser.getId(), friend.getId());
        return friend;
    }

    /**
     * 修改好友
     *
     * @param friend     好友实体信息
     * @param onlineUser 当前登录用户
     * @return Friend 修改后的好友信息
     */
    @Override
    public Friend update(Friend friend, OnlineUser onlineUser) {
        friendMapper.updateById(friend);
        log.info("好友, 好友信息修改成功: userId={}, friendId={}", onlineUser.getId(), friend.getId());
        return friend;
    }

    /**
     * 保存好友
     *
     * @param friend     好友实体信息
     * @param onlineUser 当前登录用户
     * @return Friend 保存后的好友信息
     */
    @Override
    public Friend save(Friend friend, OnlineUser onlineUser) {
        if (StringUtils.isNotBlank(friend.getId())) {
            return create(friend, onlineUser);
        }
        return update(friend, onlineUser);
    }

    /**
     * 根据好友id删除好友信息
     *
     * @param id         好友id
     * @param onlineUser 当前登录用户
     */
    @Override
    public void delete(String id, OnlineUser onlineUser) {
        friendMapper.deleteById(id);
        log.info("好友, 好友信息删除成功: userId={}, friendId={}", onlineUser.getId(), id);
    }

    /**
     * 根据好友id列表删除好友信息
     *
     * @param ids        好友id列表
     * @param onlineUser 当前登录用户
     */
    @Override
    public void deleteBatchIds(List<String> ids, OnlineUser onlineUser) {
        friendMapper.deleteBatchIds(ids);
        log.info("好友, 好友信息批量删除成功: userId={}, count={}, friendIds={}", onlineUser.getId(), ids.size(), ids.toString());
    }

    /**
     * 根据用户id删除
     *
     * @param userId     用户id
     * @param onlineUser 当前登录用户
     */
    @Override
    public void deleteByUserId(String userId, OnlineUser onlineUser) {
        friendMapper.delete(new QueryWrapper<Friend>().eq("user_id", userId));
        log.info("好友, 好友信息根据userId删除成功: userId={}, userId={}", onlineUser.getId(), userId);
    }

    /**
     * 根据好友id删除
     *
     * @param friendId   好友id
     * @param onlineUser 当前登录用户
     */
    @Override
    public void deleteByFriendId(String friendId, OnlineUser onlineUser) {
        friendMapper.delete(new QueryWrapper<Friend>().eq("friend_id", friendId));
        log.info("好友, 好友信息根据friendId删除成功: userId={}, friendId={}", onlineUser.getId(), friendId);
    }
}
