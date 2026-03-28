<template>
  <div class="portal-container">
    <!-- 顶部导航栏 -->
    <Header />

    <!-- 主体内容 -->
    <el-main class="portal-content">
      <!-- 路由视图 -->
      <router-view />
    </el-main>
  </div>
</template>

<script setup>
import Header from '../components/Header.vue'
</script>

<style scoped>
.portal-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.portal-content {
  flex: 1;
  margin-top: 60px; /* 为固定顶部菜单预留空间 */
  padding: 0;
  background: #f0f2f5;
}
</style> 