import apiClient from './index'

/**
 * 数据统计相关 API
 */
export default {
  /**
   * 获取数据看板统计数据
   * @returns {Promise} { totalUsers, todayNewUsers, weeklyTrend }
   */
  getDashboardStats() {
    return apiClient.get('/stats/dashboard')
  }
}
