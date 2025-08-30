package com.example.onlinemall.controller;

import com.example.onlinemall.common.Result;
import com.example.onlinemall.dto.UserProfileResponse;
import com.example.onlinemall.dto.UserRegisterRequest;
import com.example.onlinemall.entity.User;
import com.example.onlinemall.interceptor.JwtInterceptor;
import com.example.onlinemall.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户功能模块的控制器
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody UserRegisterRequest registerRequest) {
        // ... (原有注册代码保持不变)
        User registeredUser = userService.register(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                registerRequest.getEmail()
        );
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("userId", registeredUser.getId().toString());

        responseData.put("username", registeredUser.getUsername());
        return Result.created(responseData, "注册成功");
    }

    /**
     * 获取当前登录用户的详细信息
     * 此接口受JwtInterceptor保护
     * @return 包含用户详细信息的响应
     */
    @GetMapping("/me")
    public Result<UserProfileResponse> getCurrentUser() {
        // 1. 从ThreadLocal获取当前登录的用户ID (由拦截器设置)
        Long currentUserId = JwtInterceptor.getCurrentUserId();
        if (currentUserId == null) {
            // 正常情况下，如果拦截器配置正确，这里不会是null
            return Result.error(401, "用户未登录");
        }

        // 2. 使用ID从数据库查询用户信息
        User user = userService.getById(currentUserId);
        if (user == null) {
            return Result.error(404, "用户不存在");
        }

        // 3. 将实体对象(User)转换为响应DTO(UserProfileResponse)
        UserProfileResponse response = new UserProfileResponse();
        // 使用Spring的BeanUtils工具类快速拷贝属性
        BeanUtils.copyProperties(user, response);
        response.setUserId(user.getId().toString()); // 确保userId是字符串类型

        // 4. 返回成功响应
        return Result.success(response, "获取成功");
    }
}
