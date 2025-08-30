package com.example.onlinemall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.onlinemall.entity.ProductCategory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品分类数据访问层接口
 * 继承 BaseMapper 后，自动拥有对 ProductCategory 表的 CRUD 能力
 */
@Mapper
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {
    // 同样，暂时不需要自定义任何方法
}
