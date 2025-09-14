package com.example.onlinemall.common;

/**
 * 自定义业务异常类
 * 用于封装业务逻辑层面的错误信息
 */
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        // 默认使用 400 Bad Request 状态码
        this(400, message);
    }

    public int getCode() {
        return code;
    }
}
