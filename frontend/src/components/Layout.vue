<template>
  <!-- 主页面布局：侧边栏 + 顶部导航 + 内容区域 -->
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <Sidebar />

    <el-container>
      <!-- 顶部导航栏 -->
      <Header />

      <!-- 主内容区域 -->
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import Sidebar from './Sidebar.vue'
import Header from './Header.vue'
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.main-content {
  background-color: #f5f7fa;
  min-height: calc(100vh - 60px);
  padding: 20px;
}
</style>
