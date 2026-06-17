import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'        // Element Plus 全局样式
import * as ElementPlusIconsVue from '@element-plus/icons-vue'  // 所有图标

import App from './App.vue'
import router from './router'

// 创建 Vue 应用实例
const app = createApp(App)

// 注册 Pinia 状态管理
app.use(createPinia())

// 注册 Vue Router 路由
app.use(router)

// 注册 Element Plus UI 组件库
app.use(ElementPlus)

// 全局注册所有 Element Plus 图标组件
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 挂载到页面
app.mount('#app')
