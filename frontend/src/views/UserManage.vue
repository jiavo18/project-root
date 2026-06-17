<template>
  <!-- 用户管理页面 -->
  <div class="user-manage">
    <h3 class="page-title">用户管理</h3>

    <!-- 搜索和操作区域 -->
    <div class="toolbar">
      <el-button type="primary" @click="handleRefresh">
        <el-icon><Refresh /></el-icon>
        刷新列表
      </el-button>
    </div>

    <!-- 用户表格 -->
    <el-card shadow="hover">
      <el-table
        :data="tableData"
        v-loading="tableLoading"
        stripe
        style="width: 100%"
        :header-cell-style="{ background: '#f5f7fa', color: '#303133' }"
      >
        <el-table-column prop="id" label="ID" width="60" align="center" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="email" label="邮箱" min-width="200" />
        <el-table-column prop="role" label="角色" width="120" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.role === 'ROLE_ADMIN'" type="danger" effect="dark" size="small">
              管理员
            </el-tag>
            <el-tag v-else type="info" effect="dark" size="small">
              普通用户
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.enabled" type="success" size="small">正常</el-tag>
            <el-tag v-else type="danger" size="small">禁用</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="注册时间" width="180" align="center">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              link
              @click="handleEdit(row)"
            >
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <!-- 删除按钮：仅管理员可见 -->
            <el-button
              v-if="userStore.isAdmin"
              type="danger"
              size="small"
              link
              @click="handleDelete(row)"
            >
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[5, 10, 20, 50]"
          :total="totalItems"
          layout="total, sizes, prev, pager, next, jumper"
          background
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </el-card>

    <!-- 编辑用户对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="编辑用户信息"
      width="450px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="editFormRef"
        :model="editForm"
        :rules="editRules"
        label-width="80px"
      >
        <el-form-item label="用户名">
          <el-input v-model="editForm.username" disabled />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="editForm.email" placeholder="请输入新邮箱" />
        </el-form-item>

        <el-form-item label="角色" prop="role">
          <el-select v-model="editForm.role" style="width: 100%">
            <el-option label="普通用户" value="ROLE_USER" />
            <el-option label="管理员" value="ROLE_ADMIN" />
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saveLoading" @click="handleSave">
          保存
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '../store'
import userApi from '../api/user'

const userStore = useUserStore()

// ---- 表格数据 ----
const tableData = ref([])
const tableLoading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const totalItems = ref(0)

// ---- 编辑对话框 ----
const dialogVisible = ref(false)
const saveLoading = ref(false)
const editFormRef = ref(null)
const editForm = ref({
  id: null,
  username: '',
  email: '',
  role: ''
})

const editRules = {
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }]
}

// ---- 方法 ----

/**
 * 加载用户列表（分页）
 */
async function loadUsers() {
  tableLoading.value = true
  try {
    const data = await userApi.getUsers(currentPage.value - 1, pageSize.value)
    tableData.value = data.content
    totalItems.value = data.totalElements
  } catch (error) {
    console.error('加载用户列表失败:', error)
  } finally {
    tableLoading.value = false
  }
}

/**
 * 刷新列表
 */
function handleRefresh() {
  currentPage.value = 1
  loadUsers()
  ElMessage.success('列表已刷新')
}

/**
 * 页码变化
 */
function handlePageChange(page) {
  currentPage.value = page
  loadUsers()
}

/**
 * 每页数量变化
 */
function handleSizeChange(size) {
  pageSize.value = size
  currentPage.value = 1
  loadUsers()
}

/**
 * 格式化日期
 */
function formatDate(dateStr) {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

/**
 * 打开编辑对话框
 */
function handleEdit(row) {
  editForm.value = {
    id: row.id,
    username: row.username,
    email: row.email,
    role: row.role
  }
  dialogVisible.value = true
}

/**
 * 保存编辑
 */
async function handleSave() {
  if (!editFormRef.value) return

  const valid = await editFormRef.value.validate().catch(() => false)
  if (!valid) return

  saveLoading.value = true
  try {
    await userApi.updateUser(editForm.value.id, {
      email: editForm.value.email,
      role: editForm.value.role
    })
    ElMessage.success('用户信息已更新')
    dialogVisible.value = false
    loadUsers()  // 刷新列表
  } catch (error) {
    console.error('更新用户失败:', error)
  } finally {
    saveLoading.value = false
  }
}

/**
 * 删除用户（仅管理员）
 */
function handleDelete(row) {
  ElMessageBox.confirm(
    `确定要删除用户「${row.username}」吗？此操作不可恢复。`,
    '删除确认',
    {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'warning',
      confirmButtonClass: 'el-button--danger'
    }
  ).then(async () => {
    try {
      await userApi.deleteUser(row.id)
      ElMessage.success(`用户「${row.username}」已删除`)
      loadUsers()  // 刷新列表
    } catch (error) {
      console.error('删除用户失败:', error)
    }
  }).catch(() => {
    // 用户取消删除
  })
}

// 页面挂载时加载数据
onMounted(() => {
  loadUsers()
})
</script>

<style scoped>
.user-manage {
  max-width: 1200px;
}

.page-title {
  margin-bottom: 20px;
  font-size: 20px;
  color: #303133;
}

.toolbar {
  margin-bottom: 16px;
  display: flex;
  justify-content: flex-start;
}

.pagination {
  margin-top: 16px;
  display: flex;
  justify-content: flex-end;
}
</style>
