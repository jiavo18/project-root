import axios from 'axios'
import { useUserStore } from '../store'
import router from '../router'
import { ElMessage } from 'element-plus'

/**
 * Axios 实例 — 全局 HTTP 请求配置
 *
 * 功能：
 * 1. 统一请求基础路径
 * 2. 请求拦截器：自动携带 JWT Token
 * 3. 响应拦截器：统一处理错误（如 Token 过期自动跳转登录页）
 */

// 创建 Axios 实例
const apiClient = axios.create({
  baseURL: '/api',           // 所有请求以 /api 开头
  timeout: 10000,            // 请求超时时间 10 秒
  headers: {
    'Content-Type': 'application/json'
  }
})

/**
 * 请求拦截器
 * 在每次请求发出前，自动从 Pinia Store 中取出 Token 并添加到 Authorization 头
 */
apiClient.interceptors.request.use(
  config => {
    const userStore = useUserStore()
    if (userStore.token) {
      // 设置 Bearer Token
      config.headers.Authorization = `Bearer ${userStore.token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

/**
 * 响应拦截器
 * 统一处理错误响应
 */
apiClient.interceptors.response.use(
  response => {
    // 成功响应：直接返回数据
    return response.data
  },
  error => {
    // 错误响应处理
    if (error.response) {
      const { status, data } = error.response

      switch (status) {
        case 401:
          // 未认证：Token 过期或无效 → 清除登录状态并跳转到登录页
          ElMessage.error('登录已过期，请重新登录')
          const userStore = useUserStore()
          userStore.logout()
          router.push('/login')
          break
        case 403:
          // 无权限访问
          ElMessage.error('权限不足，您没有操作权限')
          break
        case 400:
          // 请求参数错误
          ElMessage.error(data?.error || '请求参数有误')
          break
        default:
          ElMessage.error(data?.error || '服务器异常，请稍后重试')
      }
    } else {
      // 网络错误
      ElMessage.error('网络连接失败，请检查网络')
    }

    return Promise.reject(error)
  }
)

export default apiClient
