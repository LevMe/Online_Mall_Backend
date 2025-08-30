package com.example.onlinemall.interceptor;

import com.example.onlinemall.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 管理员权限校验拦截器
 */
@Component
public class AdminInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Autowired
    public AdminInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 从请求头获取 'Authorization'
        String authHeader = request.getHeader("Authorization");

        // 2. 检查Header是否存在 (JwtInterceptor应该已经检查过了，这里作为双重保险)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.getWriter().write("Unauthorized: Missing Authorization header");
            return false;
        }

        // 3. 提取Token字符串
        String token = authHeader.substring(7);

        try {
            // 4. 从Token中解析出角色信息
            String role = jwtUtil.getRoleFromToken(token);

            // 5. 校验角色是否为 "ADMIN"
            if ("ADMIN".equals(role)) {
                // 如果是管理员，放行
                return true;
            } else {
                // 如果不是管理员，返回 403 Forbidden
                response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
                response.getWriter().write("Forbidden: Access denied. Admin role required.");
                return false;
            }
        } catch (Exception e) {
            // Token验证失败 (例如过期、签名错误等)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.getWriter().write("Unauthorized: " + e.getMessage());
            return false;
        }
    }
}