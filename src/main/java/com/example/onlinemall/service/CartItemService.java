package com.example.onlinemall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.onlinemall.dto.ViewCartResponse;
import com.example.onlinemall.entity.CartItem;
import java.util.List;

/**
 * 购物车项服务接口
 */
public interface CartItemService extends IService<CartItem> {

    void addItemToCart(Long userId, Long productId, Integer quantity);

    ViewCartResponse viewCart(Long userId);

    /**
     * 更新购物车中某个商品的数量
     * @param userId 当前用户ID
     * @param cartItemId 购物车项ID
     * @param quantity 新的数量
     */
    void updateItemQuantity(Long userId, String cartItemId, Integer quantity);

    /**
     * 从购物车中删除一个或多个商品
     * @param userId 当前用户ID
     * @param cartItemIds 待删除的购物车项ID列表
     */
    void deleteItemsFromCart(Long userId, List<String> cartItemIds);
}