<template>
  <div class="register-container">
    <!-- 动态粒子背景 -->
    <div class="particles">
      <div v-for="i in 50" :key="i" class="particle"></div>
    </div>
    
    <div class="register-box">
      <h2 class="register-title">注册账号</h2>
      
      <el-form
        ref="registerFormRef"
        :model="registerForm"
        :rules="registerRules"
        class="register-form"
      >
        <el-form-item prop="username">
          <el-input
            v-model="registerForm.username"
            placeholder="用户名"
            clearable
          >
            <template #prefix>
              <el-icon><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="email">
          <el-input
            v-model="registerForm.email"
            placeholder="邮箱"
            clearable
          >
            <template #prefix>
              <el-icon><Message /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="密码"
            show-password
            clearable
          >
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="确认密码"
            show-password
            clearable
            @keyup.enter="handleRegister"
          >
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            class="register-btn"
            :loading="loading"
            @click="handleRegister"
          >
            {{ loading ? '注册中...' : '注 册' }}
          </el-button>
        </el-form-item>
      </el-form>

      <div class="form-footer">
        <span class="footer-text">已有账号？</span>
        <el-link type="primary" :underline="false" @click.prevent="goLogin">
          返回登录
        </el-link>
      </div>
    </div>
    
    <div class="copyright-text">
      © 2026 吃货宝后台管理系统 | Design by Team9
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Message } from '@element-plus/icons-vue'
import http from '../utils/http'

const router = useRouter()
const registerFormRef = ref()
const loading = ref(false)

const registerForm = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请再次输入密码'))
  } else if (value !== registerForm.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度需在 2-20 个字符之间', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: ['blur', 'change'] }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度需在 6-20 个字符之间', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const handleRegister = async () => {
  if (!registerFormRef.value) return

  try {
    const valid = await registerFormRef.value.validate()
    if (!valid) return

    loading.value = true
    const response = await http.post('/register', {
      username: registerForm.username,
      email: registerForm.email,
      password: registerForm.password
    })

    if (response?.code === 200) {
      ElMessage.success(response?.msg || '注册成功，请登录')
      router.push('/login')
    } else {
      ElMessage.error(response?.msg || '注册失败，请重试')
    }
  } catch (error) {
    ElMessage.error(error.response?.data?.msg || '服务异常，请稍后再试')
  } finally {
    loading.value = false
  }
}

const goLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.register-container {
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
  10% { opacity: 1; }
  90% { opacity: 1; }
  100% {
    transform: translateY(-100vh) rotate(720deg);
    opacity: 0;
  }
}

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

.register-box {
  width: 380px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 0 25px 45px rgba(0, 0, 0, 0.2);
  z-index: 1;
}

.register-title {
  text-align: center;
  color: #fff;
  font-size: 28px;
  font-weight: 600;
  margin-bottom: 35px;
  letter-spacing: 2px;
}

.register-form {
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
  margin-bottom: 22px;
}

:deep(.el-form-item__error) {
  color: #ff6b6b;
}

.register-btn {
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

.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 10px 30px rgba(102, 126, 234, 0.4);
  background: linear-gradient(135deg, #764ba2 0%, #667eea 100%);
}

.form-footer {
  text-align: center;
  margin-top: 20px;
}

.footer-text {
  color: rgba(255, 255, 255, 0.6);
  font-size: 14px;
}

:deep(.el-link) {
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
}

:deep(.el-link:hover) {
  color: #64c8ff;
}

.copyright-text {
  position: absolute;
  bottom: 20px;
  color: rgba(255, 255, 255, 0.5);
  font-size: 12px;
  z-index: 1;
}
</style>
