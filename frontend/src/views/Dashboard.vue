<template>
  <!-- 数据看板页面 -->
  <div class="dashboard">
    <h3 class="page-title">数据看板</h3>

    <!-- 统计卡片区域 -->
    <el-row :gutter="20" class="stats-cards">
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #ecf5ff">
              <el-icon :size="32" color="#409EFF"><UserFilled /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">用户总数</div>
              <div class="stat-value">{{ stats.totalUsers }}</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #f0f9eb">
              <el-icon :size="32" color="#67C23A"><Plus /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">今日新增</div>
              <div class="stat-value">{{ stats.todayNewUsers }}</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #fef0f0">
              <el-icon :size="32" color="#F56C6C"><TrendCharts /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">本周新增</div>
              <div class="stat-value">{{ weeklyTotal }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-card shadow="hover" class="chart-card">
      <template #header>
        <span>近7天注册趋势</span>
      </template>
      <v-chart :option="chartOption" style="height: 360px" autoresize />
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import VChart from 'vue-echarts'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart, LineChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components'
import statsApi from '../api/stats'

// 注册 ECharts 所需的组件
use([
  CanvasRenderer, BarChart, LineChart,
  TitleComponent, TooltipComponent, LegendComponent, GridComponent
])

// ---- 数据状态 ----
const stats = ref({
  totalUsers: 0,
  todayNewUsers: 0,
  weeklyTrend: []
})

// ---- 计算属性 ----
/** 本周新增总数 */
const weeklyTotal = computed(() => {
  return stats.value.weeklyTrend.reduce((sum, item) => sum + item.count, 0)
})

/** ECharts 图表配置 */
const chartOption = computed(() => {
  const dates = stats.value.weeklyTrend.map(item => item.date)
  const counts = stats.value.weeklyTrend.map(item => item.count)

  return {
    // 提示框
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' }
    },
    // 图例
    legend: {
      data: ['注册人数']
    },
    // X 轴：日期
    xAxis: {
      type: 'category',
      data: dates,
      axisLabel: {
        formatter: (value) => value.substring(5)  // 只显示 MM-DD
      }
    },
    // Y 轴：数量
    yAxis: {
      type: 'value',
      minInterval: 1,
      name: '人数'
    },
    // 数据系列：同时展示柱状图和折线图
    series: [
      {
        name: '注册人数',
        type: 'bar',
        data: counts,
        itemStyle: {
          color: '#409EFF',
          borderRadius: [4, 4, 0, 0]
        },
        barMaxWidth: 40
      },
      {
        name: '注册人数',
        type: 'line',
        data: counts,
        smooth: true,
        lineStyle: { color: '#67C23A', width: 2 },
        itemStyle: { color: '#67C23A' },
        symbol: 'circle',
        symbolSize: 8
      }
    ],
    grid: {
      top: 30,
      left: 50,
      right: 20,
      bottom: 30
    }
  }
})

// ---- 方法 ----

/**
 * 加载数据看板统计数据
 */
async function loadStats() {
  try {
    const data = await statsApi.getDashboardStats()
    stats.value = data
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

// 页面挂载时加载数据
onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.dashboard {
  max-width: 1200px;
}

.page-title {
  margin-bottom: 20px;
  font-size: 20px;
  color: #303133;
}

.stats-cards {
  margin-bottom: 20px;
}

.stat-card {
  cursor: default;
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 60px;
  height: 60px;
  border-radius: 12px;
}

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 4px;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
}

.chart-card {
  margin-top: 0;
}
</style>
