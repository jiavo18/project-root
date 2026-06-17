import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

/**
 * Vite 构建配置
 *
 * 代理配置说明：
 * 开发时将 /api 开头的请求代理到后端 http://localhost:8080
 * 这样前端请求 /api/auth/login 实际会转发到 http://localhost:8080/api/auth/login
 * 避免了跨域问题（生产环境下由 Nginx 或后端 CORS 处理）
 */
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,                    // 前端开发服务器端口
    proxy: {
      '/api': {
        target: 'http://localhost:8080',  // 后端地址
        changeOrigin: true,
      }
    }
  }
})
