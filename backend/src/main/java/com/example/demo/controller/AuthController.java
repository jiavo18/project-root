package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器
 * 处理用户注册和登录的 HTTP 请求
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 用户注册
     * POST /api/auth/register
     *
     * 请求体示例：
     * {
     *   "username": "testuser",
     *   "password": "123456",
     *   "email": "test@example.com"
     * }
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            Map<String, String> result = authService.register(request);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 用户登录
     * POST /api/auth/login
     *
     * 请求体示例：
     * {
     *   "account": "admin",      // 可以是用户名或邮箱
     *   "password": "admin123"
     * }
     *
     * 成功响应：
     * {
     *   "token": "eyJhbGciOi...",
     *   "username": "admin",
     *   "email": "admin@example.com",
     *   "role": "ROLE_ADMIN"
     * }
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            Map<String, Object> result = authService.login(request);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
