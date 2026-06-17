<template>
  <!-- 顶部导航栏 -->
  <el-header class="header">
    <div class="header-left">
      <span class="page-title">{{ route.meta.title }}</span>
    </div>

    <div class="header-right">
      <!-- 用户信息下拉菜单 -->
      <el-dropdown trigger="click">
        <span class="user-info">
          <el-icon :size="20"><UserFilled /></el-icon>
          <span class="username">{{ userStore.username }}</span>
          <el-tag v-if="userStore.isAdmin" type="danger" size="small" effect="dark">
            管理员
          </el-tag>
          <el-tag v-else type="info" size="small" effect="dark">
            普通用户
          </el-tag>
        </span>

        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item disabled>
              邮箱：{{ userStore.email }}
            </el-dropdown-item>
            <el-dropdown-item divided @click="handleLogout">
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </el-header>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../store'
import { ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

/**
 * 退出登录
 * 弹出确认对话框，确认后清除登录状态并跳转到登录页
 */
function handleLogout() {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    userStore.logout()
    router.push('/login')
  }).catch(() => {
    // 用户取消退出
  })
}
</script>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  padding: 0 20px;
  height: 60px;
}

.header-left {
  display: flex;
  align-items: center;
}

.page-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 12px;
  border-radius: 8px;
  transition: background-color 0.2s;
}

.user-info:hover {
  background-color: #f5f7fa;
}

.username {
  font-size: 14px;
  color: #303133;
}
</style>
