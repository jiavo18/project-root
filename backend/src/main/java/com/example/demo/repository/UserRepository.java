package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 用户数据访问层 (JPA Repository)
 * 继承 JpaRepository 后自动获得基本的 CRUD 方法
 * 这里定义了一些自定义查询方法
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查找用户
     */
    Optional<User> findByEmail(String email);

    /**
     * 检查用户名是否已存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否已存在
     */
    boolean existsByEmail(String email);

    /**
     * 根据用户名或邮箱查找用户（用于登录）
     */
    Optional<User> findByUsernameOrEmail(String username, String email);

    /**
     * 统计在指定时间之后创建的用户数量
     */
    long countByCreatedAtAfter(LocalDateTime dateTime);

    /**
     * 统计近7天每天的注册人数
     * 使用 JPQL 查询，按日期分组统计
     * 返回结果示例：[["2026-06-11", 5], ["2026-06-12", 3], ...]
     */
    @Query(value = "SELECT DATE(u.createdAt) as date, COUNT(u) as count " +
                   "FROM User u " +
                   "WHERE u.createdAt >= :startDate " +
                   "GROUP BY DATE(u.createdAt) " +
                   "ORDER BY DATE(u.createdAt) ASC")
    List<Object[]> countDailyRegistrations(@Param("startDate") LocalDateTime startDate);
}
