package com.example.onlinemall.interceptor;

import com.example.onlinemall.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 认证拦截器
 * 用于在请求到达Controller前验证Token
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    // 创建一个ThreadLocal来存储用户ID，确保线程安全
    private static final ThreadLocal<Long> userThreadLocal = new ThreadLocal<>();

    @Autowired
    public JwtInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 在Controller方法执行前进行处理
     * @return boolean - true表示放行，false表示拦截
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 从请求头获取 'Authorization'
        String authHeader = request.getHeader("Authorization");

        // 2. 检查Header是否存在且格式是否正确 (Bearer <token>)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.getWriter().write("Unauthorized: Missing or invalid Authorization header");
            return false;
        }

        // 3. 提取Token字符串
        String token = authHeader.substring(7); // "Bearer " 后面是token

        try {
            // 4. 解析和验证Token
            Claims claims = jwtUtil.validateToken(token);
            // 【重要修正】直接将 "userId" claim 解析为 Long 类型
            Long userId = claims.get("userId", Long.class);
            if (userId == null) {
                // 如果解析出的userId是null，说明Token有问题
                throw new RuntimeException("Invalid token: Missing userId claim");
            }

            // 5. 将用户ID存入ThreadLocal，方便后续Controller使用
            userThreadLocal.set(userId);

            // 6. 放行
            return true;
        } catch (Exception e) {
            // Token验证失败 (过期、签名错误等)
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.getWriter().write("Unauthorized: " + e.getMessage());
            return false;
        }
    }

    /**
     * 在请求处理完成后（视图渲染前）调用，用于清理资源
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清理ThreadLocal，防止内存泄漏
        userThreadLocal.remove();
    }

    /**
     * 提供一个静态方法，方便在其他地方获取当前登录的用户ID
     * @return 当前线程中的用户ID，如果未登录则返回null
     */
    public static Long getCurrentUserId() {
        return userThreadLocal.get();
    }
}