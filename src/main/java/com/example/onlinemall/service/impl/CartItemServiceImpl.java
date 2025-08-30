package com.example.onlinemall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.onlinemall.dto.ViewCartResponse;
import com.example.onlinemall.entity.CartItem;
import com.example.onlinemall.mapper.CartItemMapper;
import com.example.onlinemall.service.CartItemService;
import com.example.onlinemall.vo.CartItemDetailVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * 购物车项服务实现类
 */
@Service
public class CartItemServiceImpl extends ServiceImpl<CartItemMapper, CartItem> implements CartItemService {

    // ... (addItemToCart 和 viewCart 方法保持不变)
    @Override
    public void addItemToCart(Long userId, Long productId, Integer quantity) {
        QueryWrapper<CartItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("product_id", productId);
        CartItem existingItem = this.baseMapper.selectOne(queryWrapper);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            this.baseMapper.updateById(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setId(UUID.randomUUID().toString());
            newItem.setUserId(userId);
            newItem.setProductId(productId);
            newItem.setQuantity(quantity);
            this.baseMapper.insert(newItem);
        }
    }

    @Override
    public ViewCartResponse viewCart(Long userId) {
        List<CartItemDetailVO> items = this.baseMapper.findCartItemDetailsByUserId(userId);
        BigDecimal totalPrice = items.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new ViewCartResponse(totalPrice, items);
    }

    @Override
    public void updateItemQuantity(Long userId, String cartItemId, Integer quantity) {
        // 1. 根据 cartItemId 查询购物车项
        CartItem cartItem = this.baseMapper.selectById(cartItemId);

        // 2. 校验：必须保证记录存在，且属于当前用户
        if (cartItem == null || !cartItem.getUserId().equals(userId)) {
            throw new RuntimeException("购物车项不存在或无权操作");
        }
        // 校验数量必须大于0
        if (quantity <= 0) {
            throw new RuntimeException("商品数量必须大于0");
        }

        // 3. 更新数量并保存
        cartItem.setQuantity(quantity);
        this.baseMapper.updateById(cartItem);
    }

    @Override
    public void deleteItemsFromCart(Long userId, List<String> cartItemIds) {
        if (cartItemIds == null || cartItemIds.isEmpty()) {
            return; // 如果列表为空，则不执行任何操作
        }
        // 关键的安全校验：使用QueryWrapper确保只能删除属于当前用户的记录
        QueryWrapper<CartItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.in("id", cartItemIds);

        this.baseMapper.delete(queryWrapper);
    }
}