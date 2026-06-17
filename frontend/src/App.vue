<template>
  <!-- 应用根组件：根据路由切换显示登录页或主页面 -->
  <router-view />
</template>

<script setup>
/**
 * App.vue — 应用根组件
 * 仅作为路由的出口容器，实际页面内容由 vue-router 根据 URL 动态渲染
 *
 * AI 辅助声明：
 * 本项目代码约 80% 由 Claude Code 和 GitHub Copilot 辅助生成，
 * 我负责需求分析、架构设计、代码调试和功能整合。
 */
</script>

<style>
/* 全局基础样式 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body, #app {
  height: 100%;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB',
               'Microsoft YaHei', Arial, sans-serif;
}
</style>
