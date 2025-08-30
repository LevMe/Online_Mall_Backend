package com.example.onlinemall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.onlinemall.entity.ProductCategory;
import com.example.onlinemall.mapper.ProductCategoryMapper;
import com.example.onlinemall.service.ProductCategoryService;
import org.springframework.stereotype.Service;

/**
 * 商品分类服务实现类
 */
@Service
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {
    // 继承 ServiceImpl 已经自动实现了 IService 中的所有方法
    // 例如 list(), getById(), save() 等，无需我们手动编写
}