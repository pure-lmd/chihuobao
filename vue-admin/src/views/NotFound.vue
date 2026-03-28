<template>
  <div class="not-found-container">
    <div class="not-found-content">
      <el-card class="error-card">
        <div class="error-content">
          <div class="error-icon">
            <el-icon size="80" color="#F56C6C"><Warning /></el-icon>
          </div>
          <div class="error-code">404</div>
          <div class="error-message">
            <h2>页面未找到</h2>
            <p>抱歉，您访问的页面不存在或已被删除。</p>
          </div>
          <div class="actions">
            <el-button type="primary" size="large" @click="goHome">
              <el-icon><HomeFilled /></el-icon>
              返回首页
            </el-button>
            <el-button size="large" @click="goBack">
              <el-icon><Back /></el-icon>
              返回上一页
            </el-button>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { Warning, HomeFilled, Back } from '@element-plus/icons-vue'

const router = useRouter()

const goHome = () => {
  router.push('/manager/dashboard')
}

const goBack = () => {
  router.go(-1)
}
</script>

<style scoped>
.not-found-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f0f2f5;
  padding: 20px;
}

.not-found-content {
  width: 100%;
  display: flex;
  justify-content: center;
}

.error-card {
  max-width: 600px;
  width: 100%;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.error-content {
  text-align: center;
  padding: 40px 20px;
}

.error-icon {
  margin-bottom: 20px;
}

.error-code {
  font-size: 100px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 20px;
  line-height: 1;
}

.error-message h2 {
  color: #333;
  margin-bottom: 15px;
  font-size: 24px;
  font-weight: 600;
}

.error-message p {
  color: #666;
  font-size: 16px;
  line-height: 1.6;
  margin-bottom: 40px;
}

.actions {
  display: flex;
  gap: 15px;
  justify-content: center;
  flex-wrap: wrap;
}

.actions .el-button {
  padding: 12px 24px;
  font-size: 16px;
}

.actions .el-button .el-icon {
  margin-right: 5px;
}

@media (max-width: 768px) {
  .error-card {
    margin: 0 10px;
  }
  
  .error-content {
    padding: 30px 15px;
  }
  
  .error-code {
    font-size: 80px;
  }
  
  .error-message h2 {
    font-size: 20px;
  }
  
  .actions {
    flex-direction: column;
    align-items: center;
  }
  
  .actions .el-button {
    width: 100%;
    max-width: 200px;
  }
}
</style> 