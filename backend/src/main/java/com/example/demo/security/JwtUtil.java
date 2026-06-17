package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

/**
 * JWT 工具类
 * 负责生成 Token、解析 Token、验证 Token 有效性
 */
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long expiration;

    /**
     * 构造函数：从配置文件读取 JWT 密钥和过期时间
     *
     * @param secret     Base64 编码的密钥字符串
     * @param expiration Token 过期时间（毫秒）
     */
    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration}") long expiration) {
        // 解码 Base64 密钥，生成 HMAC-SHA 签名所需的 Key
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.expiration = expiration;
    }

    /**
     * 生成 JWT Token
     *
     * @param username 用户名
     * @param role     用户角色（ROLE_USER / ROLE_ADMIN）
     * @return JWT Token 字符串
     */
    public String generateToken(String username, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(username)              // 主题：用户名
                .claim("role", role)            // 自定义声明：用户角色
                .issuedAt(now)                  // 签发时间
                .expiration(expiryDate)         // 过期时间
                .signWith(secretKey)            // 签名
                .compact();
    }

    /**
     * 从 Token 中解析出用户名
     *
     * @param token JWT Token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * 从 Token 中解析出用户角色
     *
     * @param token JWT Token
     * @return 角色字符串
     */
    public String getRoleFromToken(String token) {
        return parseClaims(token).get("role", String.class);
    }

    /**
     * 验证 Token 是否有效
     *
     * @param token JWT Token
     * @return true=有效, false=无效
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Token 过期、签名错误、格式错误等都会抛出异常
            return false;
        }
    }

    /**
     * 解析 Token 的 Claims（声明信息）
     */
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
