<template>
  <div class="login-container">
    <!-- 动态粒子背景 -->
    <div class="particles">
      <div v-for="i in 50" :key="i" class="particle"></div>
    </div>
    
    <div class="login-box">
      <h2 class="login-title">管理员登录</h2>
      
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="用户名"
            clearable
          >
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="密码"
            show-password
            clearable
            @keyup.enter="handleLogin"
          >
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            class="login-btn"
            :loading="loading"
            @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form-item>
      </el-form>

      <div class="form-footer">
        <el-link type="primary" :underline="false" @click.prevent="goRegister">
          立即注册
        </el-link>
      </div>
    </div>
    
    <div class="footer-text">
      © 2026 吃货宝后台管理系统 | Design by Team9
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const loginFormRef = ref()
const loading = ref(false)
const rememberMe = ref(false)

const STORAGE_KEYS = {
  remember: 'admin-remember-username',
  username: 'admin-last-username'
}

const loginForm = reactive({
  username: '',
  password: ''
})

const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 3, max: 20, message: '密码长度在 3 到 20 个字符', trigger: 'blur' }
  ]
}

onMounted(() => {
  const remembered = localStorage.getItem(STORAGE_KEYS.remember) === 'true'
  rememberMe.value = remembered
  if (remembered) {
    loginForm.username = localStorage.getItem(STORAGE_KEYS.username) || ''
  }
})

watch(rememberMe, (value) => {
  if (!value) {
    localStorage.removeItem(STORAGE_KEYS.remember)
    localStorage.removeItem(STORAGE_KEYS.username)
  } else {
    localStorage.setItem(STORAGE_KEYS.remember, 'true')
    if (loginForm.username) {
      localStorage.setItem(STORAGE_KEYS.username, loginForm.username)
    }
  }
})

watch(() => loginForm.username, (value) => {
  if (rememberMe.value) {
    localStorage.setItem(STORAGE_KEYS.username, value)
  }
})

const handleLogin = async () => {
  if (!loginFormRef.value) return

  try {
    const valid = await loginFormRef.value.validate()
    if (!valid) return

    loading.value = true
    const result = await userStore.login(loginForm)

    if (result.success) {
      ElMessage.success(result.message)
      if (rememberMe.value) {
        localStorage.setItem(STORAGE_KEYS.remember, 'true')
        localStorage.setItem(STORAGE_KEYS.username, loginForm.username)
      }
      router.push('/manager')
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
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%);
  background-size: 400% 400%;
  animation: gradientBG 15s ease infinite;
  position: relative;
  overflow: hidden;
}

@keyframes gradientBG {
  0% { background-position: 0% 50%; }
  50% { background-position: 100% 50%; }
  100% { background-position: 0% 50%; }
}

/* 粒子效果 */
.particles {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: 0;
}

.particle {
  position: absolute;
  width: 4px;
  height: 4px;
  background: rgba(255, 255, 255, 0.5);
  border-radius: 50%;
  animation: float 15s infinite;
}

.particle:nth-child(odd) {
  background: rgba(100, 200, 255, 0.6);
}

@keyframes float {
  0%, 100% {
    transform: translateY(100vh) rotate(0deg);
    opacity: 0;
  }
  10% {
    opacity: 1;
  }
  90% {
    opacity: 1;
  }
  100% {
    transform: translateY(-100vh) rotate(720deg);
    opacity: 0;
  }
}

/* 随机分布粒子 */
.particle:nth-child(1) { left: 5%; animation-delay: 0s; animation-duration: 12s; }
.particle:nth-child(2) { left: 10%; animation-delay: 1s; animation-duration: 14s; }
.particle:nth-child(3) { left: 15%; animation-delay: 2s; animation-duration: 11s; }
.particle:nth-child(4) { left: 20%; animation-delay: 0.5s; animation-duration: 16s; }
.particle:nth-child(5) { left: 25%; animation-delay: 3s; animation-duration: 13s; }
.particle:nth-child(6) { left: 30%; animation-delay: 1.5s; animation-duration: 15s; }
.particle:nth-child(7) { left: 35%; animation-delay: 4s; animation-duration: 12s; }
.particle:nth-child(8) { left: 40%; animation-delay: 2.5s; animation-duration: 14s; }
.particle:nth-child(9) { left: 45%; animation-delay: 0.8s; animation-duration: 17s; }
.particle:nth-child(10) { left: 50%; animation-delay: 3.5s; animation-duration: 11s; }
.particle:nth-child(11) { left: 55%; animation-delay: 1.2s; animation-duration: 13s; }
.particle:nth-child(12) { left: 60%; animation-delay: 4.5s; animation-duration: 16s; }
.particle:nth-child(13) { left: 65%; animation-delay: 2.2s; animation-duration: 12s; }
.particle:nth-child(14) { left: 70%; animation-delay: 0.3s; animation-duration: 15s; }
.particle:nth-child(15) { left: 75%; animation-delay: 3.8s; animation-duration: 14s; }
.particle:nth-child(16) { left: 80%; animation-delay: 1.8s; animation-duration: 11s; }
.particle:nth-child(17) { left: 85%; animation-delay: 4.2s; animation-duration: 17s; }
.particle:nth-child(18) { left: 90%; animation-delay: 2.8s; animation-duration: 13s; }
.particle:nth-child(19) { left: 95%; animation-delay: 0.6s; animation-duration: 16s; }
.particle:nth-child(20) { left: 3%; animation-delay: 3.2s; animation-duration: 12s; }
.particle:nth-child(n+21) { left: calc(2% * var(--i, 1)); animation-delay: calc(0.2s * var(--i, 1)); }

.login-box {
  width: 380px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 25px 45px rgba(0, 0, 0, 0.2);
  z-index: 1;
}

.login-title {
  text-align: center;
  color: #fff;
  font-size: 28px;
  font-weight: 600;
  margin-bottom: 40px;
  letter-spacing: 2px;
}

.login-form {
  width: 100%;
}

:deep(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.1) !important;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 8px;
  box-shadow: none !important;
  padding: 12px 15px;
}

:deep(.el-input__wrapper:hover) {
  border-color: rgba(100, 200, 255, 0.5);
}

:deep(.el-input__wrapper.is-focus) {
  border-color: #64c8ff !important;
  box-shadow: 0 0 15px rgba(100, 200, 255, 0.3) !important;
}

:deep(.el-input__inner) {
  color: #fff !important;
  font-size: 14px;
}

:deep(.el-input__inner::placeholder) {
  color: rgba(255, 255, 255, 0.6);
}

:deep(.el-input__prefix .el-icon) {
  color: rgba(255, 255, 255, 0.7);
  font-size: 18px;
}

:deep(.el-form-item) {
  margin-bottom: 25px;
}

:deep(.el-form-item__error) {
  color: #ff6b6b;
}

.login-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 8px;
  border: none;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  transition: all 0.3s ease;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 30px rgba(102, 126, 234, 0.4);
  background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
}

.form-footer {
  text-align: center;
  margin-top: 20px;
}

:deep(.el-link) {
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
}

:deep(.el-link:hover) {
  color: #64c8ff;
}

.footer-text {
  position: absolute;
  bottom: 20px;
  color: rgba(255, 255, 255, 0.5);
  font-size: 12px;
  z-index: 1;
}
</style>
