package com.example.onlinemall.config;

import com.example.onlinemall.interceptor.AdminInterceptor;
import com.example.onlinemall.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置类
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;
    private final AdminInterceptor adminInterceptor;

    @Autowired
    public WebConfig(JwtInterceptor jwtInterceptor, AdminInterceptor adminInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
        this.adminInterceptor = adminInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册普通用户认证拦截器
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns(
                        "/users/me",
                        "/cart/**",
                        "/behaviors/track",
                        "/orders/**"
                )
                .excludePathPatterns(
                        "/users/register",
                        "/auth/login",
                        "/products/**", // 商品列表和详情通常是公开的
                        "/categories"
                );

        // 注册管理员权限校验拦截器
        // 它会拦截所有 /admin/ 开头的路径
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns("/admin/**");
    }
}