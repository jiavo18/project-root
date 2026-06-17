import apiClient from './index'

/**
 * 用户管理相关 API
 */
export default {
  /**
   * 获取用户列表（分页）
   * @param {number} page - 页码（从0开始）
   * @param {number} size - 每页数量
   * @returns {Promise} 分页用户数据
   */
  getUsers(page = 0, size = 10) {
    return apiClient.get('/users', { params: { page, size } })
  },

  /**
   * 获取单个用户信息
   * @param {number} id - 用户 ID
   */
  getUserById(id) {
    return apiClient.get(`/users/${id}`)
  },

  /**
   * 更新用户信息
   * @param {number} id - 用户 ID
   * @param {Object} data - { email, role }
   */
  updateUser(id, data) {
    return apiClient.put(`/users/${id}`, data)
  },

  /**
   * 删除用户（仅管理员）
   * @param {number} id - 用户 ID
   */
  deleteUser(id) {
    return apiClient.delete(`/users/${id}`)
  }
}
