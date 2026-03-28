<template>
  <div class="not-found-container">
    <div class="not-found-content">
      <div class="error-icon">
        <el-icon size="120" color="#e6e6e6">
          <WarningFilled />
        </el-icon>
      </div>
      <h1>404</h1>
      <h2>页面未找到</h2>
      <p>抱歉，您访问的页面不存在</p>
      <div class="actions">
        <el-button type="primary" size="large" @click="goHome">
          返回首页
        </el-button>
        <el-button size="large" @click="goBack">
          返回上一页
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goHome = () => {
  router.push('/home')
}

const goBack = () => {
  router.go(-1)
}
</script>

<style scoped>
.not-found-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0f2f5;
}

.not-found-content {
  text-align: center;
  padding: 40px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.error-icon {
  margin-bottom: 30px;
}

.not-found-content h1 {
  font-size: 72px;
  color: #409EFF;
  margin-bottom: 20px;
  font-weight: 600;
}

.not-found-content h2 {
  font-size: 24px;
  color: #333;
  margin-bottom: 15px;
  font-weight: 600;
}

.not-found-content p {
  font-size: 16px;
  color: #666;
  margin-bottom: 30px;
}

.actions {
  display: flex;
  gap: 15px;
  justify-content: center;
}
</style> 