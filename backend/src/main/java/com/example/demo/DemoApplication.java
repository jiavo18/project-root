package com.example.demo;

import com.example.demo.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 用户管理系统 + 数据看板 — Spring Boot 启动类
 *
 * 技术栈：Java 17 + Spring Boot 3.2 + Spring Security + JWT + JPA + H2
 *
 * AI 辅助声明：
 * 本项目代码约 80% 由 Claude Code 和 GitHub Copilot 辅助生成，
 * 我（开发者）负责需求分析、架构设计、代码调试和功能整合。
 */
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        System.out.println("========================================");
        System.out.println("  用户管理系统启动成功！");
        System.out.println("  API 地址: http://localhost:8080");
        System.out.println("  H2 控制台: http://localhost:8080/h2-console");
        System.out.println("  管理员账号: admin / admin123");
        System.out.println("========================================");
    }

    /**
     * 应用启动后自动初始化演示数据
     * 如果数据库中没有用户，会自动创建管理员和测试用户
     */
    @Bean
    public CommandLineRunner initDemoData(UserService userService) {
        return args -> userService.initDemoData();
    }
}
