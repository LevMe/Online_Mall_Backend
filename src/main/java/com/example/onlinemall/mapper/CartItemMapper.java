package com.example.onlinemall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.onlinemall.entity.CartItem;
import com.example.onlinemall.vo.CartItemDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 购物车项数据访问层接口
 */
@Mapper
public interface CartItemMapper extends BaseMapper<CartItem> {

    /**
     * 根据用户ID，通过JOIN查询获取购物车中所有商品的详细信息
     *
     * @param userId 用户ID
     * @return 包含商品详情的购物车项列表
     */
    @Select("SELECT " +
            "ci.id AS cartItemId, " +
            "ci.product_id AS productId, " +
            "ci.quantity, " +
            "p.name, " +
            "p.price, " +
            "p.main_image_url AS imageUrl " +
            "FROM cart_items ci " +
            "JOIN products p ON ci.product_id = p.id " +
            "WHERE ci.user_id = #{userId}")
    List<CartItemDetailVO> findCartItemDetailsByUserId(Long userId);
}
