package com.clever.bean.shopping.projo.output;

import com.clever.bean.shopping.ProductCategory;

import java.util.List;

/**
 * @Author xixi
 * @Date 2024-03-28 08:46
 **/
public class ProductCategoryTreeVO extends ProductCategory {

    /**
     * 子分类
     */
    private List<ProductCategoryTreeVO> children;

    public List<ProductCategoryTreeVO> getChildren() {
        return children;
    }

    public void setChildren(List<ProductCategoryTreeVO> children) {
        this.children = children;
    }
}
