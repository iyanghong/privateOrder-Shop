package com.clever.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.clever.bean.shopping.Orders;


/**
 * 订单Mapper
 *
 * @Author xixi
 * @Date 2024-03-26 17:10:18
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

}
