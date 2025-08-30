package com.example.onlinemall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.onlinemall.dto.AddProductRequest;
import com.example.onlinemall.dto.UpdateProductRequest;
import com.example.onlinemall.entity.Product;
import com.example.onlinemall.mapper.ProductMapper;
import com.example.onlinemall.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 商品服务实现类
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Override
    public IPage<Product> getProductPage(Page<Product> page, Integer categoryId, String keyword) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        if (categoryId != null) {
            queryWrapper.eq("category_id", categoryId);
        }
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like("name", keyword);
        }
        queryWrapper.orderByDesc("created_at");
        return this.baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Product addProduct(AddProductRequest request) {
        Product newProduct = new Product();
        BeanUtils.copyProperties(request, newProduct);
        this.baseMapper.insert(newProduct);
        return newProduct;
    }

    @Override
    public Product updateProduct(Long productId, UpdateProductRequest request) {
        // 1. 检查商品是否存在
        Product existingProduct = this.baseMapper.selectById(productId);
        if (existingProduct == null) {
            throw new RuntimeException("商品不存在");
        }

        // 2. 将请求DTO中的属性拷贝到查询出的实体对象上
        BeanUtils.copyProperties(request, existingProduct);

        // 3. 执行更新操作
        this.baseMapper.updateById(existingProduct);
        return existingProduct;
    }

    @Override
    public void deleteProduct(Long productId) {
        // 检查商品是否存在，如果不存在，Mybatis-Plus的deleteById不会报错，但为了逻辑严谨可以加一个检查
        if (this.baseMapper.selectById(productId) == null) {
            throw new RuntimeException("要删除的商品不存在");
        }
        this.baseMapper.deleteById(productId);
    }
}