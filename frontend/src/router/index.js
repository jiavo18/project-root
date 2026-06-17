import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../store'

/**
 * Vue Router 路由配置
 * - 定义前端页面路由映射
 * - 通过导航守卫检查登录状态
 */

const routes = [
  {
    path: '/login',
    name: 'Login',
    // 登录页不包含侧边栏，独立布局
    component: () => import('../views/Login.vue'),
    meta: { title: '登录', requiresAuth: false }
  },
  {
    path: '/',
    // 主布局（包含侧边栏和顶部导航）
    component: () => import('../components/Layout.vue'),
    // 主页面内的子路由
    children: [
      {
        path: '',
        redirect: '/dashboard'  // 默认跳转到数据看板
      },
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: { title: '数据看板', requiresAuth: true }
      },
      {
        path: 'users',
        name: 'UserManage',
        component: () => import('../views/UserManage.vue'),
        meta: { title: '用户管理', requiresAuth: true }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),  // HTML5 History 模式
  routes
})

/**
 * 全局导航守卫：检查登录状态
 * 访问需要登录的页面时，如果未登录则跳转到登录页
 */
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - 用户管理系统` : '用户管理系统'

  const userStore = useUserStore()

  // 如果目标页面需要登录认证
  if (to.matched.some(record => record.meta.requiresAuth !== false)) {
    if (!userStore.token) {
      // 未登录 → 跳转到登录页，并记录原本想访问的页面
      next({ path: '/login', query: { redirect: to.fullPath } })
    } else {
      next()
    }
  } else {
    // 目标页面不需要认证（如登录页）
    if (userStore.token && to.path === '/login') {
      // 已登录用户访问登录页 → 重定向到主页
      next({ path: '/' })
    } else {
      next()
    }
  }
})

export default router
