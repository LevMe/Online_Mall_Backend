package com.example.onlinemall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.onlinemall.dto.AddProductRequest;
import com.example.onlinemall.dto.UpdateProductRequest;
import com.example.onlinemall.entity.Product;

import java.util.List;

/**
 * 商品服务接口
 */
public interface ProductService extends IService<Product> {

    IPage<Product> getProductPage(Page<Product> page, Integer categoryId, String keyword);

    Product addProduct(AddProductRequest request);

    Product updateProduct(Long productId, UpdateProductRequest request);

    void deleteProduct(Long productId);

    /**
     * 获取推荐商品列表
     * @param userId 用户ID (可为空)
     * @param pageSize 需要的数量
     * @return 商品列表
     */
    List<Product> getRecommendedProducts(Long userId, Integer pageSize);
}