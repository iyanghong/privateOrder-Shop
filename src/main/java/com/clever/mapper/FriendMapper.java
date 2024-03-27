package com.clever.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clever.bean.shopping.projo.output.FriendDetailOutput;
import org.apache.ibatis.annotations.Mapper;
import com.clever.bean.shopping.Friend;

import java.util.List;


/**
 * 好友Mapper
 *
 * @Author xixi
 * @Date 2024-03-27 17:36:54
 */
@Mapper
public interface FriendMapper extends BaseMapper<Friend> {
    List<FriendDetailOutput> selectFriendDetailListByUserId(String userId);
}
