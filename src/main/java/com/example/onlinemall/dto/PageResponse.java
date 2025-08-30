package com.example.onlinemall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 通用的分页响应数据传输对象 (DTO)
 *
 * @param <T> 列表项的类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Long page;

    /**
     * 每页数量
     */
    private Long pageSize;

    /**
     * 当前页的数据列表
     */
    private List<T> items;
}