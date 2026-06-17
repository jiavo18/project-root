import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

/**
 * 用户状态管理 (Pinia Store)
 *
 * 管理用户登录状态、Token、用户信息
 * Token 持久化存储在 localStorage 中，刷新页面不会丢失登录状态
 */
export const useUserStore = defineStore('user', () => {
  // ---- 状态 ----
  const token = ref(localStorage.getItem('token') || '')  // JWT Token
  const username = ref(localStorage.getItem('username') || '')  // 用户名
  const email = ref(localStorage.getItem('email') || '')       // 邮箱
  const role = ref(localStorage.getItem('role') || '')          // 角色

  // ---- 计算属性 ----
  /** 是否已登录 */
  const isLoggedIn = computed(() => !!token.value)

  /** 是否是管理员 */
  const isAdmin = computed(() => role.value === 'ROLE_ADMIN')

  // ---- 方法 ----

  /**
   * 保存登录信息
   * 将 Token 和用户信息存入 Pinia 状态和 localStorage
   */
  function setLoginInfo(data) {
    token.value = data.token
    username.value = data.username
    email.value = data.email
    role.value = data.role

    // 持久化到 localStorage（刷新页面不丢失）
    localStorage.setItem('token', data.token)
    localStorage.setItem('username', data.username)
    localStorage.setItem('email', data.email)
    localStorage.setItem('role', data.role)
  }

  /**
   * 退出登录
   * 清除 Pinia 状态和 localStorage 中的用户数据
   */
  function logout() {
    token.value = ''
    username.value = ''
    email.value = ''
    role.value = ''

    localStorage.removeItem('token')
    localStorage.removeItem('username')
    localStorage.removeItem('email')
    localStorage.removeItem('role')
  }

  return {
    token,
    username,
    email,
    role,
    isLoggedIn,
    isAdmin,
    setLoginInfo,
    logout
  }
})
