<template>
  <!-- 侧边栏导航 -->
  <el-aside :width="isCollapse ? '64px' : '220px'" class="sidebar">
    <div class="logo" @click="goHome">
      <el-icon :size="24"><Monitor /></el-icon>
      <span v-show="!isCollapse" class="logo-text">用户管理系统</span>
    </div>

    <el-menu
      :default-active="activeMenu"
      :collapse="isCollapse"
      :collapse-transition="false"
      background-color="#304156"
      text-color="#bfcbd9"
      active-text-color="#409EFF"
      router
    >
      <!-- 数据看板菜单 -->
      <el-menu-item index="/dashboard">
        <el-icon><DataAnalysis /></el-icon>
        <span>数据看板</span>
      </el-menu-item>

      <!-- 用户管理菜单 -->
      <el-menu-item index="/users">
        <el-icon><UserFilled /></el-icon>
        <span>用户管理</span>
      </el-menu-item>
    </el-menu>
  </el-aside>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

// 是否折叠侧边栏（可以通过按钮切换）
const isCollapse = computed(() => false)

// 当前激活的菜单项
const activeMenu = computed(() => route.path)

// 点击 Logo 返回首页
function goHome() {
  router.push('/')
}
</script>

<style scoped>
.sidebar {
  background-color: #304156;
  overflow-y: auto;
  transition: width 0.3s;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 60px;
  color: #fff;
  cursor: pointer;
  gap: 8px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.logo-text {
  font-size: 16px;
  font-weight: 600;
  white-space: nowrap;
}

/* Element Plus 菜单覆盖样式 */
.el-menu {
  border-right: none;
}

.el-menu-item:hover {
  background-color: #263445 !important;
}
</style>
