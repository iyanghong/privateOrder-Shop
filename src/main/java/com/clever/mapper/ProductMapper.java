package com.clever.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.clever.bean.shopping.Product;


/**
 * 商品Mapper
 *
 * @Author xixi
 * @Date 2024-03-27 11:46:50
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

}
