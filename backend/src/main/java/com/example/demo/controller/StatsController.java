package com.example.demo.controller;

import com.example.demo.dto.StatsResponse;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据统计控制器
 * 提供数据看板所需的统计接口
 * 所有接口都需要登录后才能访问
 */
@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final UserService userService;

    public StatsController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取数据看板统计数据
     * GET /api/stats/dashboard
     *
     * 返回数据包含：
     * - totalUsers: 用户总数
     * - todayNewUsers: 今日新增用户数
     * - weeklyTrend: 近7天注册趋势（日期 + 数量）
     */
    @GetMapping("/dashboard")
    public ResponseEntity<StatsResponse> getDashboardStats() {
        StatsResponse stats = userService.getStats();
        return ResponseEntity.ok(stats);
    }
}
