package com.clever.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clever.bean.shopping.projo.output.FriendRequestDetailOutput;
import org.apache.ibatis.annotations.Mapper;
import com.clever.bean.shopping.FriendRequest;

import java.util.List;


/**
 * 好友申请Mapper
 *
 * @Author xixi
 * @Date 2024-03-27 17:36:54
 */
@Mapper
public interface FriendRequestMapper extends BaseMapper<FriendRequest> {
    List<FriendRequestDetailOutput> selectFriendRequestDetailListByUserId(String userId);
}
