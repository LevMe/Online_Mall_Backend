package com.example.onlinemall.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 捕获所有Controller层抛出的异常，并返回统一的JSON响应格式
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 引入日志记录器，用于在服务器端记录详细的错误信息
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理所有未被特定方法处理的运行时异常 (RuntimeException)
     * @param e 捕获到的异常对象
     * @return 封装好的通用错误响应
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 通常业务逻辑错误返回 400 状态码
    public Result<?> handleRuntimeException(RuntimeException e) {
        // 在服务器的控制台打印详细的错误日志，方便排查问题
        log.error("运行时异常: {}", e.getMessage(), e);

        // e.getMessage() 会获取到我们抛出异常时的字符串，例如 "用户名已存在"
        // 将这个友好的错误信息返回给前端
        return Result.error(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    /**
     * 处理其他所有未捕获的异常 (Exception)
     * 这是一个兜底的异常处理器
     * @param e 捕获到的异常对象
     * @return 封装好的通用服务器内部错误响应
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 未知错误返回 500 状态码
    public Result<?> handleException(Exception e) {
        log.error("服务器内部错误: {}", e.getMessage(), e);

        // 对于未知的服务器内部错误，返回一个统一的、模糊的提示，避免暴露过多细节
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器内部错误，请联系管理员");
    }
}
