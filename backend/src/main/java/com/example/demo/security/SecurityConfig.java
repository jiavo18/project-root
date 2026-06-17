package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Spring Security 核心配置类
 * 配置认证规则、CORS 跨域、密码加密器
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * 安全过滤器链配置
     * - 禁用 CSRF（前后端分离项目，使用 JWT 不需要 CSRF 保护）
     * - 设置为无状态会话（不创建 HttpSession）
     * - 配置 URL 访问权限
     * - 注册 JWT 过滤器
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 禁用 CSRF（跨站请求伪造防护），因为使用 JWT 无状态认证
            .csrf(csrf -> csrf.disable())

            // 配置 CORS 跨域
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // 无状态会话：不使用 Session，每次请求独立认证
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // URL 访问权限配置
            .authorizeHttpRequests(auth -> auth
                // 公开接口：注册、登录、H2 控制台
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()

                // 数据看板接口：登录用户均可访问
                .requestMatchers("/api/stats/**").authenticated()

                // 用户管理接口：仅管理员可以删除用户，查看和修改所有登录用户均可
                .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/api/users/**").authenticated()

                // 其他所有请求需要认证
                .anyRequest().authenticated()
            )

            // H2 控制台需要允许 frame（H2 使用 iframe）
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))

            // 将 JWT 过滤器添加到 UsernamePasswordAuthenticationFilter 之前
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS 跨域配置
     * 允许前端 (Vue3, 默认端口 5173) 跨域访问后端接口
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 允许的前端源地址
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:3000"));
        // 允许的 HTTP 方法
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 允许的请求头
        configuration.setAllowedHeaders(List.of("*"));
        // 允许携带认证信息（如 Authorization 头）
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * 密码加密器
     * 使用 BCrypt 算法加密用户密码
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
