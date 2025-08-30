package com.example.onlinemall.controller;

import com.example.onlinemall.common.Result;
import com.example.onlinemall.dto.AddItemToCartRequest;
import com.example.onlinemall.dto.DeleteCartItemsRequest;
import com.example.onlinemall.dto.UpdateCartItemRequest;
import com.example.onlinemall.dto.ViewCartResponse;
import com.example.onlinemall.interceptor.JwtInterceptor;
import com.example.onlinemall.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 购物车控制器
 */
@RestController
@RequestMapping("/cart") // 路径前缀 /api/v1/cart
public class CartController {

    private final CartItemService cartItemService;

    @Autowired
    public CartController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping("/items")
    public Result<?> addItem(@RequestBody AddItemToCartRequest request) {
        Long userId = JwtInterceptor.getCurrentUserId();
        cartItemService.addItemToCart(userId, request.getProductId(), request.getQuantity());
        return Result.created(null, "添加成功");
    }

    @GetMapping
    public Result<ViewCartResponse> viewCart() {
        Long userId = JwtInterceptor.getCurrentUserId();
        ViewCartResponse cartResponse = cartItemService.viewCart(userId);
        return Result.success(cartResponse, "获取成功");
    }

    /**
     * 更新购物车商品数量
     * @param cartItemId 购物车项ID，从URL路径中获取
     * @param request 包含新数量的请求体
     * @return 操作结果
     */
    @PutMapping("/items/{cartItemId}")
    public Result<?> updateItemQuantity(
            @PathVariable String cartItemId,
            @RequestBody UpdateCartItemRequest request) {
        Long userId = JwtInterceptor.getCurrentUserId();
        cartItemService.updateItemQuantity(userId, cartItemId, request.getQuantity());
        return Result.success(null, "更新成功");
    }

    /**
     * 从购物车删除商品
     * @param request 包含待删除的 cartItemIds 列表的请求体
     * @return 操作结果
     */
    @DeleteMapping("/items")
    public Result<?> deleteItems(@RequestBody DeleteCartItemsRequest request) {
        Long userId = JwtInterceptor.getCurrentUserId();
        cartItemService.deleteItemsFromCart(userId, request.getCartItemIds());
        return Result.success(null, "删除成功");
    }
}