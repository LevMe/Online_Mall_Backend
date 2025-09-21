package com.example.onlinemall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.onlinemall.common.Result;
import com.example.onlinemall.dto.PageResponse;
import com.example.onlinemall.dto.ProductDetailResponse;
import com.example.onlinemall.dto.ProductListItemResponse;
import com.example.onlinemall.entity.Product;
import com.example.onlinemall.util.JwtUtil;
import com.example.onlinemall.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品控制器
 */
@RestController
@RequestMapping("/products") // 路径前缀 /api/v1/products
public class ProductController {

    private final ProductService productService;
    private final JwtUtil jwtUtil; // 依赖注入JwtUtil

    @Autowired
    public ProductController(ProductService productService, JwtUtil jwtUtil) {
        this.productService = productService;
        this.jwtUtil = jwtUtil; // 初始化
    }

    @GetMapping
    public Result<PageResponse<ProductListItemResponse>> getProductList(
            @RequestParam(required = false) String recommendationType,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "8") Integer pageSize
    ) {
        Page<Product> page = new Page<>(pageNo, pageSize);
        IPage<Product> productPage = productService.getProductPage(page, categoryId, keyword);
        List<ProductListItemResponse> itemResponses = productPage.getRecords().stream().map(product -> {
            ProductListItemResponse responseItem = new ProductListItemResponse();
            BeanUtils.copyProperties(product, responseItem);
            return responseItem;
        }).collect(Collectors.toList());
        PageResponse<ProductListItemResponse> pageResponse = new PageResponse<>(
                productPage.getTotal(),
                productPage.getCurrent(),
                productPage.getSize(),
                itemResponses
        );
        return Result.success(pageResponse, "获取成功");
    }

    /**
     * 根据商品ID获取单个商品的详细信息
     * @param productId 商品ID，从URL路径中获取
     * @return 商品的详细信息
     */
    @GetMapping("/{productId}")
    public Result<ProductDetailResponse> getProductDetails(@PathVariable Long productId) {
        // 1. 调用Service层根据ID查询商品
        Product product = productService.getById(productId);

        // 2. 检查商品是否存在
        if (product == null) {
            // 如果商品不存在，返回一个符合规范的404错误响应
            return Result.error(404, "商品不存在");
        }

        // 3. 将实体对象(Product)转换为响应DTO(ProductDetailResponse)
        ProductDetailResponse response = new ProductDetailResponse();
        BeanUtils.copyProperties(product, response);

        // 4. 返回成功响应
        return Result.success(response, "获取成功");
    }

    /**
     * 获取推荐商品列表
     * @param token 用户认证Token (可选)
     * @param pageSize 返回的商品数量
     * @return 推荐商品列表
     */
    @GetMapping("/recommendations")
    public Result<List<ProductListItemResponse>> getRecommendations(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam(defaultValue = "15") Integer pageSize
    ) {
        Long userId = null;
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            try {
                // 3. 使用注入的 jwtUtil 实例安全地验证和解析Token
                jwtUtil.validateToken(jwt); // 首先验证，如果失败会抛出异常
                userId = jwtUtil.getUserIdFromToken(jwt); // 验证成功后提取userId
            } catch (Exception e) {
                // Token 无效（过期、签名错误等），忽略异常，userId 保持为 null
                // 这样后续会自然地走公共推荐逻辑
            }
        }

        List<Product> products = productService.getRecommendedProducts(userId, pageSize);

        List<ProductListItemResponse> responseItems = products.stream().map(product -> {
            ProductListItemResponse responseItem = new ProductListItemResponse();
            BeanUtils.copyProperties(product, responseItem);
            return responseItem;
        }).collect(Collectors.toList());

        return Result.success(responseItems, "获取推荐商品成功");
    }
}
