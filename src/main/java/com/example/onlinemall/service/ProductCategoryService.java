package com.example.onlinemall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.onlinemall.entity.ProductCategory;

/**
 * 商品分类服务接口
 */
public interface ProductCategoryService extends IService<ProductCategory> {
    // 目前没有特殊的业务方法，所以接口是空的
    // 继承 IService 已经获得了丰富的 CRUD 方法
}