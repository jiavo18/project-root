<template>
  <!-- 登录页面：全屏居中布局 -->
  <div class="login-container">
    <div class="login-card">
      <!-- 标题区域 -->
      <div class="login-header">
        <el-icon :size="40" color="#409EFF"><Monitor /></el-icon>
        <h2>用户管理系统</h2>
        <p class="subtitle">AI 辅助全栈开发 Demo</p>
      </div>

      <!-- 登录/注册切换标签 -->
      <el-tabs v-model="activeTab" class="login-tabs">
        <!-- 登录表单 -->
        <el-tab-pane label="登录" name="login">
          <el-form
            ref="loginFormRef"
            :model="loginForm"
            :rules="loginRules"
            label-position="top"
            @keyup.enter="handleLogin"
          >
            <el-form-item label="账号" prop="account">
              <el-input
                v-model="loginForm.account"
                placeholder="请输入用户名或邮箱"
                :prefix-icon="User"
                size="large"
              />
            </el-form-item>

            <el-form-item label="密码" prop="password">
              <el-input
                v-model="loginForm.password"
                type="password"
                placeholder="请输入密码"
                :prefix-icon="Lock"
                size="large"
                show-password
              />
            </el-form-item>

            <el-form-item>
              <el-button
                type="primary"
                size="large"
                :loading="loading"
                @click="handleLogin"
                style="width: 100%"
              >
                {{ loading ? '登录中...' : '登 录' }}
              </el-button>
            </el-form-item>
          </el-form>

          <!-- 演示账号提示 -->
          <el-alert
            title="演示账号"
            type="info"
            :closable="false"
            show-icon
          >
            <template #default>
              <p>管理员：admin / admin123</p>
              <p>普通用户：张三 / admin123（或其他测试用户）</p>
            </template>
          </el-alert>
        </el-tab-pane>

        <!-- 注册表单 -->
        <el-tab-pane label="注册" name="register">
          <el-form
            ref="registerFormRef"
            :model="registerForm"
            :rules="registerRules"
            label-position="top"
            @keyup.enter="handleRegister"
          >
            <el-form-item label="用户名" prop="username">
              <el-input
                v-model="registerForm.username"
                placeholder="3-50个字符"
                :prefix-icon="User"
                size="large"
              />
            </el-form-item>

            <el-form-item label="邮箱" prop="email">
              <el-input
                v-model="registerForm.email"
                placeholder="请输入邮箱"
                :prefix-icon="Message"
                size="large"
              />
            </el-form-item>

            <el-form-item label="密码" prop="password">
              <el-input
                v-model="registerForm.password"
                type="password"
                placeholder="至少6位密码"
                :prefix-icon="Lock"
                size="large"
                show-password
              />
            </el-form-item>

            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input
                v-model="registerForm.confirmPassword"
                type="password"
                placeholder="请再次输入密码"
                :prefix-icon="Lock"
                size="large"
                show-password
              />
            </el-form-item>

            <el-form-item>
              <el-button
                type="success"
                size="large"
                :loading="loading"
                @click="handleRegister"
                style="width: 100%"
              >
                {{ loading ? '注册中...' : '注 册' }}
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>

      <!-- 底部版权信息 -->
      <div class="login-footer">
        <p>Powered by Spring Boot + Vue3 + Claude Code</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Message } from '@element-plus/icons-vue'
import { useUserStore } from '../store'
import authApi from '../api/auth'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

// ---- 状态 ----
const activeTab = ref('login')     // 当前标签页
const loading = ref(false)         // 加载状态
const loginFormRef = ref(null)
const registerFormRef = ref(null)

// ---- 登录表单 ----
const loginForm = reactive({
  account: '',
  password: ''
})

const loginRules = {
  account: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

// ---- 注册表单 ----
const registerForm = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
})

const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 50, message: '用户名长度需要在 3-50 之间', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少 6 位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== registerForm.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

/**
 * 处理登录
 */
async function handleLogin() {
  if (!loginFormRef.value) return

  // 表单校验
  const valid = await loginFormRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const data = await authApi.login({
      account: loginForm.account,
      password: loginForm.password
    })

    // 保存登录信息到 Store
    userStore.setLoginInfo(data)
    ElMessage.success('登录成功！欢迎回来')

    // 登录成功后跳转到目标页面（或默认到看板）
    const redirect = route.query.redirect || '/dashboard'
    router.push(redirect)
  } catch (error) {
    // 错误已在 Axios 拦截器中统一处理，这里做兜底
    console.error('登录失败:', error)
  } finally {
    loading.value = false
  }
}

/**
 * 处理注册
 */
async function handleRegister() {
  if (!registerFormRef.value) return

  // 表单校验
  const valid = await registerFormRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    await authApi.register({
      username: registerForm.username,
      email: registerForm.email,
      password: registerForm.password
    })

    ElMessage.success('注册成功！请登录')
    // 切换到登录标签页
    activeTab.value = 'login'
    // 清空注册表单
    registerFormRef.value.resetFields()
  } catch (error) {
    console.error('注册失败:', error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* 全屏居中布局 */
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

/* 登录卡片 */
.login-card {
  width: 420px;
  padding: 40px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

/* 标题区域 */
.login-header {
  text-align: center;
  margin-bottom: 20px;
}

.login-header h2 {
  margin: 10px 0 4px;
  font-size: 24px;
  color: #303133;
}

.subtitle {
  color: #909399;
  font-size: 13px;
}

/* 标签页 */
.login-tabs {
  margin-top: 10px;
}

/* 底部信息 */
.login-footer {
  text-align: center;
  margin-top: 20px;
  color: #c0c4cc;
  font-size: 12px;
}

/* 演示账号提示框样式微调 */
.login-footer p {
  margin: 0;
}
</style>
