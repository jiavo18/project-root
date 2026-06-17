import apiClient from './index'

/**
 * 认证相关 API
 */
export default {
  /**
   * 用户注册
   * @param {Object} data - { username, password, email }
   * @returns {Promise} 注册结果
   */
  register(data) {
    return apiClient.post('/auth/register', data)
  },

  /**
   * 用户登录
   * @param {Object} data - { account, password }
   * @returns {Promise} { token, username, email, role }
   */
  login(data) {
    return apiClient.post('/auth/login', data)
  }
}
