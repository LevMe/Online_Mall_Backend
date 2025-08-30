package com.example.onlinemall.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 商品实体类，映射 'products' 表
 */
@Data
@TableName(value = "products", autoResultMap = true) // autoResultMap = true 是让TypeHandler生效的关键
public class Product {

    /**
     * 商品ID, 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 所属分类ID
     */
    private Integer categoryId;

    /**
     * 商品主图URL
     */
    private String mainImageUrl;

    /**
     * 商品图片URL列表 (JSON数组)
     * 使用 JacksonTypeHandler 来自动处理 JSON 字符串与 List<String> 之间的转换
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> imageUrls;

    /**
     * 商品规格参数 (JSON对象)
     * 使用 JacksonTypeHandler 来自动处理 JSON 字符串与 Map<String, Object> 之间的转换
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> specs;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
