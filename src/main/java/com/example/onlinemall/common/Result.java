package com.example.onlinemall.common;

import lombok.Data;

/**
 * 通用API响应体封装类
 *
 * @param <T> data字段的泛型类型
 */
@Data
public class Result<T> {

    /**
     * 业务状态码, 200-成功, 其他-失败
     */
    private Integer code;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    // 私有化构造函数，防止外部直接创建实例
    private Result() {
    }

    /**
     * 成功响应的静态工厂方法 (带数据)
     *
     * @param data 返回的数据
     * @return Result 实例
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 成功响应的静态工厂方法 (带数据和自定义消息)
     *
     * @param data    返回的数据
     * @param message 自定义消息
     * @return Result 实例
     */
    public static <T> Result<T> success(T data, String message) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 成功响应的静态工厂方法 (针对PRD中201 Created的场景)
     *
     * @param data    返回的数据
     * @param message 自定义消息
     * @return Result 实例
     */
    public static <T> Result<T> created(T data, String message) {
        Result<T> result = new Result<>();
        result.setCode(201); // 符合PRD中的 201 Created
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 失败响应的静态工厂方法
     *
     * @param code    错误码
     * @param message 错误信息
     * @return Result 实例
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    /**
     * 成功响应的静态工厂方法 (针对PRD中202 Accepted的场景)
     * @param message 自定义消息
     * @return Result 实例
     */
    public static <T> Result<T> accepted(String message) {
        Result<T> result = new Result<>();
        result.setCode(202);
        result.setMessage(message);
        result.setData(null);
        return result;
    }
}
