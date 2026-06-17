package com.example.demo.service;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务
 * 处理用户注册和登录的业务逻辑
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 用户注册
     *
     * @param request 包含用户名、密码、邮箱的注册请求
     * @return 成功消息
     * @throws RuntimeException 如果用户名或邮箱已存在
     */
    public Map<String, String> register(RegisterRequest request) {
        // 1. 检查用户名是否已被占用
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已被注册: " + request.getUsername());
        }

        // 2. 检查邮箱是否已被占用
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("邮箱已被注册: " + request.getEmail());
        }

        // 3. 创建用户对象，密码使用 BCrypt 加密存储
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))  // 加密密码
                .email(request.getEmail())
                .role("ROLE_USER")      // 默认注册为普通用户
                .enabled(true)          // 默认启用
                .build();

        // 4. 保存到数据库
        userRepository.save(user);

        // 5. 返回成功消息
        Map<String, String> result = new HashMap<>();
        result.put("message", "注册成功！欢迎 " + user.getUsername());
        return result;
    }

    /**
     * 用户登录
     *
     * @param request 包含账号（用户名/邮箱）和密码的登录请求
     * @return JWT Token 和用户基本信息
     * @throws RuntimeException 如果账号不存在或密码错误
     */
    public Map<String, Object> login(LoginRequest request) {
        // 1. 根据用户名或邮箱查找用户
        User user = userRepository.findByUsernameOrEmail(
                        request.getAccount(), request.getAccount())
                .orElseThrow(() -> new RuntimeException("账号不存在: " + request.getAccount()));

        // 2. 检查账户是否被禁用
        if (!user.getEnabled()) {
            throw new RuntimeException("账户已被禁用，请联系管理员");
        }

        // 3. 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 4. 生成 JWT Token
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        // 5. 构建返回数据（包含 Token 和用户信息）
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("username", user.getUsername());
        result.put("email", user.getEmail());
        result.put("role", user.getRole());
        return result;
    }
}
