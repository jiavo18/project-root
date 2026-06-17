package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户实体类
 * 映射到数据库的 users 表
 *
 * 使用 Lombok 注解简化 Getter/Setter/构造器代码
 * - @Data: 自动生成 getter、setter、toString、equals、hashCode
 * - @Builder: 提供建造者模式，方便构建对象
 * - @NoArgsConstructor: 无参构造器（JPA 要求）
 * - @AllArgsConstructor: 全参构造器
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * 主键，自增 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名，唯一且不可为空
     */
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    /**
     * 密码（加密存储）
     */
    @Column(nullable = false)
    private String password;

    /**
     * 邮箱，唯一且不可为空
     */
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /**
     * 用户角色：ROLE_USER (普通用户) 或 ROLE_ADMIN (管理员)
     * 默认值为 ROLE_USER
     */
    @Column(nullable = false, length = 20)
    @Builder.Default
    private String role = "ROLE_USER";

    /**
     * 账户是否启用（用于禁用账户）
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean enabled = true;

    /**
     * 注册时间（自动填充）
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间（每次修改自动更新）
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 在实体持久化之前自动设置创建时间和更新时间
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 在实体更新之前自动刷新更新时间
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
