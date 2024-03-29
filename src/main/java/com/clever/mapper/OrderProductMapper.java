package com.clever.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clever.bean.shopping.projo.output.OrderProductDetailVO;
import org.apache.ibatis.annotations.Mapper;
import com.clever.bean.shopping.OrderProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 订单商品Mapper
 *
 * @Author xixi
 * @Date 2024-03-27 11:46:50
 */
@Mapper
public interface OrderProductMapper extends BaseMapper<OrderProduct> {

    List<OrderProductDetailVO> selectDetailListByOrderIds(@Param("orderIds") List<String> orderIds);
}
