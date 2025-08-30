package com.example.onlinemall.dto;

import com.example.onlinemall.vo.CartItemDetailVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 查看购物车接口的响应数据传输对象 (DTO)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewCartResponse {
    private BigDecimal totalPrice;
    private List<CartItemDetailVO> items;
}