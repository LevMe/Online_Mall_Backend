package com.example.onlinemall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.onlinemall.entity.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品数据访问层接口
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    // 暂时不需要自定义SQL，Mybatis-Plus的QueryWrapper足够应对当前的需求
}
