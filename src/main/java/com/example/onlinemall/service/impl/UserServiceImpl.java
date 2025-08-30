package com.example.onlinemall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.onlinemall.dto.UserLoginResponse;
import com.example.onlinemall.entity.User;
import com.example.onlinemall.mapper.UserMapper;
import com.example.onlinemall.service.UserService;
import com.example.onlinemall.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final JwtUtil jwtUtil;

    // 使用构造函数注入 JwtUtil
    @Autowired
    public UserServiceImpl(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public User register(String username, String password, String email) {
        // ... (原有的注册代码保持不变)
        QueryWrapper<User> usernameWrapper = new QueryWrapper<>();
        usernameWrapper.eq("username", username);
        if (this.baseMapper.selectCount(usernameWrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }
        QueryWrapper<User> emailWrapper = new QueryWrapper<>();
        emailWrapper.eq("email", email);
        if (this.baseMapper.selectCount(emailWrapper) > 0) {
            throw new RuntimeException("该邮箱已被注册");
        }
        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(encryptedPassword);
        newUser.setEmail(email);
        this.baseMapper.insert(newUser);
        newUser.setPassword(null);
        return newUser;
    }

    @Override
    public UserLoginResponse login(String username, String password) {
        // 1. 根据用户名查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = this.baseMapper.selectOne(queryWrapper);

        // 2. 检查用户是否存在
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 3. 验证密码
        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!encryptedPassword.equals(user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 4. 登录成功，生成JWT Token，并传入角色信息
        String token = jwtUtil.generateToken(
                user.getId().toString(),
                user.getUsername(),
                user.getRole() // <-- 传入角色
        );

        // 5. 构建并返回 UserLoginResponse 对象
        UserLoginResponse.UserInfo userInfo = new UserLoginResponse.UserInfo(
                user.getId().toString(),
                user.getUsername(),
                user.getAvatarUrl()
        );

        return new UserLoginResponse(token, userInfo);
    }
}
