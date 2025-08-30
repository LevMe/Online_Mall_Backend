package com.example.onlinemall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.onlinemall.dto.AddProductRequest;
import com.example.onlinemall.dto.UpdateProductRequest;
import com.example.onlinemall.entity.Product;

/**
 * 商品服务接口
 */
public interface ProductService extends IService<Product> {

    IPage<Product> getProductPage(Page<Product> page, Integer categoryId, String keyword);

    Product addProduct(AddProductRequest request);

    /**
     * 更新一个已存在的商品
     * @param productId 要更新的商品ID
     * @param request 包含新商品信息的DTO
     * @return 更新成功后的商品实体
     */
    Product updateProduct(Long productId, UpdateProductRequest request);

    /**
     * 删除一个商品
     * @param productId 要删除的商品ID
     */
    void deleteProduct(Long productId);
}