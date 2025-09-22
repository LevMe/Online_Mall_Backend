package com.example.onlinemall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.onlinemall.dto.CreateUserRequest;
import com.example.onlinemall.dto.UpdateUserRequest;
import com.example.onlinemall.dto.UserLoginResponse;
import com.example.onlinemall.entity.User;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册功能
     *
     * @param username 用户名
     * @param password 密码
     * @param email    邮箱
     * @return 注册成功后的用户信息（不含密码）
     */
    User register(String username, String password, String email);

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 包含Token和用户信息的响应对象
     */
    UserLoginResponse login(String username, String password);

    /**
     * 分页获取用户列表，支持按关键词搜索
     * @param page 分页对象
     * @param keyword 搜索关键词 (可选)
     * @return 用户分页数据
     */
    Page<User> getUsers(Page<User> page, String keyword);

    User createUser(CreateUserRequest request);

    User updateUser(Long userId, UpdateUserRequest request);

    void deleteUser(Long userId);

}