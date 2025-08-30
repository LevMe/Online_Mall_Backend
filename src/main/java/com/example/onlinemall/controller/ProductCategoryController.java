package com.example.onlinemall.controller;

import com.example.onlinemall.common.Result;
import com.example.onlinemall.entity.ProductCategory;
import com.example.onlinemall.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品分类控制器
 */
@RestController
@RequestMapping("/categories") // 路径前缀为 /api/v1/categories
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @Autowired
    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    /**
     * 获取所有商品分类列表
     * @return 商品分类列表
     */
    @GetMapping
    public Result<List<ProductCategory>> getCategoryList() {
        // 调用 Service 层的 list() 方法，该方法继承自 IService，用于查询所有记录
        List<ProductCategory> categoryList = productCategoryService.list();
        // 返回成功响应
        return Result.success(categoryList, "获取成功");
    }
}
