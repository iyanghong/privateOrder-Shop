package com.clever.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.clever.bean.shopping.ProductCategory;


/**
 * 商品分类Mapper
 *
 * @Author xixi
 * @Date 2024-03-26 17:10:18
 */
@Mapper
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {

}
