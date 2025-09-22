package com.example.onlinemall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.onlinemall.common.Result;
import com.example.onlinemall.dto.CreateUserRequest;
import com.example.onlinemall.dto.PageResponse;
import com.example.onlinemall.dto.UpdateUserRequest;
import com.example.onlinemall.entity.User;
import com.example.onlinemall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public Result<PageResponse<User>> getUsers(
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            // 添加 keyword 请求参数，非必需
            @RequestParam(required = false) String keyword) {
        Page<User> page = new Page<>(pageNo, pageSize);
        // 调用更新后的 service 方法
        Page<User> userPage = userService.getUsers(page, keyword);
        PageResponse<User> pageResponse = new PageResponse<>(
                userPage.getTotal(),
                userPage.getCurrent(),
                userPage.getSize(),
                userPage.getRecords()
        );
        return Result.success(pageResponse, "获取用户列表成功");
    }

    @PostMapping
    public Result<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        User user = userService.createUser(createUserRequest);
        return Result.created(user, "创建用户成功");
    }

    @PutMapping("/{userId}")
    public Result<User> updateUser(@PathVariable Long userId, @RequestBody UpdateUserRequest updateUserRequest) {
        User updatedUser = userService.updateUser(userId, updateUserRequest);
        return Result.success(updatedUser, "更新用户成功");
    }

    @DeleteMapping("/{userId}")
    public Result<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return Result.success(null, "删除用户成功");
    }
}