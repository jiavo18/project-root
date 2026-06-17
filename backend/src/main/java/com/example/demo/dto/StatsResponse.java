package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 数据统计响应 DTO
 * 包含用户总数、今日新增，以及近7天的注册趋势数据
 */
@Data
@Builder
public class StatsResponse {

    /**
     * 用户总数
     */
    private long totalUsers;

    /**
     * 今日新增用户数
     */
    private long todayNewUsers;

    /**
     * 近7天注册趋势
     * 每一项是一个 Map，包含 date (日期) 和 count (新增数量)
     * 示例：[{"date": "2026-06-11", "count": 5}, {"date": "2026-06-12", "count": 3}, ...]
     */
    private List<Map<String, Object>> weeklyTrend;
}
