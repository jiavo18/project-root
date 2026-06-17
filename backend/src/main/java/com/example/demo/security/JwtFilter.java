package com.example.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * JWT 认证过滤器
 * 每个 HTTP 请求都会经过此过滤器，检查是否携带有效的 JWT Token
 * 如果 Token 有效，将用户信息设置到 Spring Security 的上下文中
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. 从请求头中提取 JWT Token
        String token = extractToken(request);

        // 2. 如果 Token 存在且有效，设置认证信息
        if (token != null && jwtUtil.validateToken(token)) {
            String username = jwtUtil.getUsernameFromToken(token);
            String role = jwtUtil.getRoleFromToken(token);

            // 创建认证令牌：包含用户名、角色权限
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,  // 密码不需要，因为已经通过 JWT 验证
                            List.of(new SimpleGrantedAuthority(role))
                    );

            // 将认证信息存入 SecurityContext，后续的 Controller 可以直接获取
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 3. 继续执行过滤器链
        filterChain.doFilter(request, response);
    }

    /**
     * 从 HTTP 请求头中提取 Bearer Token
     * 格式：Authorization: Bearer <token>
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);  // 去掉 "Bearer " 前缀
        }
        return null;
    }
}
