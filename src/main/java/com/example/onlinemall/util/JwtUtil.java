package com.example.onlinemall.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT (JSON Web Token) 工具类
 * 负责生成、解析和验证Token
 */
@Component
public class JwtUtil {

    // Token 的密钥 (Secret Key)，必须足够复杂以保证安全。在生产环境中，应从配置文件中读取。
    // 这里为了演示，我们直接硬编码。
    private static final String SECRET_KEY = "c2VjcmV0X2tleV9mb3Jfand0X2Zvcl9vbmxpbmVfbWFsbF9wcm9qZWN0XzEyMzQ1Ng==";

    // 将密钥字符串转换为Key对象
    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Token 的过期时间 (毫秒)，这里设置为7天
    private static final long EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000;

    /**
     * 根据用户信息生成Token
     *
     * @param userId   用户ID
     * @param username 用户名
     * @return 生成的JWT Token字符串
     */
    public String generateToken(String userId, String username) {
        // 创建一个Map来存放载荷 (Payload) 中的自定义声明
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);

        // 获取当前时间
        Date now = new Date();
        // 计算过期时间
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

        // 使用 Jwts 构建器来创建Token
        return Jwts.builder()
                .setClaims(claims) // 设置自定义声明
                .setSubject(username) // 设置主题，通常是用户名
                .setIssuedAt(now) // 设置签发时间
                .setExpiration(expirationDate) // 设置过期时间
                .signWith(key, SignatureAlgorithm.HS256) // 使用HS256算法和密钥进行签名
                .compact(); // 构建并返回字符串形式的Token
    }

    /**
     * 从Token中解析出载荷 (Claims)
     *
     * @param token JWT Token字符串
     * @return Claims对象，包含了所有声明信息
     */
    public Claims parseToken(String token) {
        // Jwts.parserBuilder() 会自动验证签名和过期时间
        // 如果验证失败，会抛出相应的异常 (如 SignatureException, ExpiredJwtException)
        return Jwts.parserBuilder()
                .setSigningKey(key) // 设置用于验证的密钥
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从Token中获取用户ID
     *
     * @param token JWT Token字符串
     * @return 用户ID
     */
    public String getUserIdFromToken(String token) {
        return parseToken(token).get("userId", String.class);
    }

    /**
     * 从Token中获取用户名
     *
     * @param token JWT Token字符串
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        return parseToken(token).getSubject();
    }

    /**
     * 验证Token是否已过期
     *
     * @param token JWT Token字符串
     * @return 如果未过期返回false，否则返回true
     */
    public boolean isTokenExpired(String token) {
        try {
            return parseToken(token).getExpiration().before(new Date());
        } catch (Exception e) {
            // 如果解析时就因为过期而抛出异常，也视为已过期
            return true;
        }
    }

    /**
     * 根据用户信息生成Token
     *
     * @param userId   用户ID
     * @param username 用户名
     * @param role     用户角色
     * @return 生成的JWT Token字符串
     */
    public String generateToken(String userId, String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("role", role); // <-- 新增角色信息

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从Token中获取角色
     *
     * @param token JWT Token字符串
     * @return 用户角色
     */
    public String getRoleFromToken(String token) {
        return parseToken(token).get("role", String.class);
    }
}
