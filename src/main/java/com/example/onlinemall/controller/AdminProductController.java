package com.example.onlinemall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.onlinemall.common.Result;
import com.example.onlinemall.dto.AddProductRequest;
import com.example.onlinemall.dto.PageResponse;
import com.example.onlinemall.dto.UpdateProductRequest;
import com.example.onlinemall.entity.Product;
import com.example.onlinemall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员商品管理控制器
 */
@RestController
@RequestMapping("/admin/products") // 路径前缀 /api/v1/admin/products
public class AdminProductController {

    private final ProductService productService;

    @Autowired
    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Result<PageResponse<Product>> getProducts(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<Product> page = new Page<>(pageNo, pageSize);
        Page<Product> productPage = productService.page(page); // 使用MyBatis-Plus自带的分页查询
        PageResponse<Product> pageResponse = new PageResponse<>(
                productPage.getTotal(),
                productPage.getCurrent(),
                productPage.getSize(),
                productPage.getRecords()
        );
        return Result.success(pageResponse, "获取商品列表成功");
    }

    @PostMapping
    public Result<Product> addProduct(@RequestBody AddProductRequest request) {
        Product newProduct = productService.addProduct(request);
        return Result.created(newProduct, "商品添加成功");
    }

    @PutMapping("/{productId}")
    public Result<Product> updateProduct(
            @PathVariable Long productId,
            @RequestBody UpdateProductRequest request) {
        Product updatedProduct = productService.updateProduct(productId, request);
        return Result.success(updatedProduct, "商品更新成功");
    }

    @DeleteMapping("/{productId}")
    public Result<?> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return Result.success(null, "商品删除成功");
    }
}