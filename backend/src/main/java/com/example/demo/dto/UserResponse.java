package com.example.demo.dto;

import com.example.demo.model.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户信息响应 DTO
 * 返回给前端的用户数据（不包含密码等敏感信息）
 */
@Data
@Builder
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private String role;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 将 User 实体转换为 UserResponse DTO
     * 这样就不会把密码等敏感字段暴露给前端
     */
    public static UserResponse fromEntity(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .enabled(user.getEnabled())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
