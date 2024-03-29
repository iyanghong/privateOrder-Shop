package com.clever.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clever.bean.shopping.projo.output.UserSearchVO;
import org.apache.ibatis.annotations.Mapper;
import com.clever.bean.shopping.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 用户Mapper
 *
 * @Author xixi
 * @Date 2024-03-26 17:10:18
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    List<UserSearchVO> selectFriendSearch(@Param("userId") String userId, @Param("keyword") String keyword);
}
