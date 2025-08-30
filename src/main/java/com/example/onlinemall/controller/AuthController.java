package com.example.onlinemall.controller;

import com.example.onlinemall.common.Result;
import com.example.onlinemall.dto.UserLoginRequest;
import com.example.onlinemall.dto.UserLoginResponse;
import com.example.onlinemall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制器
 * 负责处理登录等认证相关的请求
 */
@RestController
@RequestMapping("/auth") // 路径前缀为 /api/v1/auth
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户登录接口
     *
     * @param loginRequest 包含用户名和密码的请求体
     * @return 包含Token和用户信息的响应
     */
    @PostMapping("/login")
    public Result<UserLoginResponse> login(@RequestBody UserLoginRequest loginRequest) {
        // 调用Service层执行登录逻辑
        UserLoginResponse loginResponse = userService.login(loginRequest.getUsername(), loginRequest.getPassword());

        // 使用Result类封装成功的响应
        return Result.success(loginResponse, "登录成功");
    }
}
