<template>
  <div class="login-container">
    <div class="login-card">
      <h2 class="title">登录</h2>
      
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
      >
        <div class="form-label">账号</div>
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入账号"
            clearable
          >
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <div class="form-label">密码</div>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            show-password
            clearable
            @keyup.enter="handleLogin"
          >
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <div class="register-link">
          <el-link type="default" :underline="false" @click="goRegister">立即注册</el-link>
        </div>

        <el-form-item>
          <el-button
            class="login-btn"
            :loading="loading"
            @click="handleLogin"
          >
            登 录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const loginFormRef = ref()
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const loginRules = {
  username: [
    { required: true, message: '请输入账号', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  try {
    const valid = await loginFormRef.value.validate()
    if (!valid) return
    loading.value = true
    const result = await userStore.login(loginForm)
    if (result.success) {
      ElMessage.success(result.message)
      router.push('/')
    } else {
      ElMessage.error(result.message)
    }
  } catch (error) {
    ElMessage.error('登录失败，请重试')
  } finally {
    loading.value = false
  }
}

const goRegister = () => {
  router.push('/register')
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%);
}

.login-card {
  width: 420px;
  padding: 50px 40px;
  background: #fff;
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
}

.title {
  text-align: center;
  font-size: 28px;
  font-weight: 500;
  color: #333;
  margin-bottom: 40px;
}

.form-label {
  font-size: 14px;
  color: #333;
  margin-bottom: 8px;
  font-weight: 500;
}

.login-form :deep(.el-input__wrapper) {
  box-shadow: none;
  border-bottom: 1px solid #e0e0e0;
  border-radius: 0;
  padding: 8px 0;
}

.login-form :deep(.el-input__wrapper:hover),
.login-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: none;
  border-bottom-color: #667eea;
}

.login-form :deep(.el-form-item) {
  margin-bottom: 25px;
}

.register-link {
  text-align: right;
  margin-bottom: 30px;
}

.register-link :deep(.el-link) {
  color: #666;
  font-size: 13px;
}

.login-btn {
  width: 100%;
  height: 50px;
  font-size: 16px;
  font-weight: 500;
  color: #fff;
  border: none;
  border-radius: 25px;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 50%, #f093fb 100%);
  cursor: pointer;
  transition: opacity 0.3s;
}

.login-btn:hover {
  opacity: 0.9;
}
</style>
