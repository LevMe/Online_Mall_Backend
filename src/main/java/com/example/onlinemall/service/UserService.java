package com.example.onlinemall.service;

import com.baomidou.mybatisplus.extension.service.IService;
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

}
