<template>
  <div class="admin-layout">
    <!-- 侧边栏 -->
    <Sidebar />

    <!-- 主要内容区域 -->
    <div class="admin-main">
      <Header>
        <template #left>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/manager' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentPageTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </template>
      </Header>

      <div class="main-content">
        <div class="content-body">
          <!-- 路由视图 -->
          <router-view />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useUserStore } from '../stores/user'
import { useRoute } from 'vue-router'
import Sidebar from '../components/Sidebar.vue'
import Header from '../components/Header.vue'

const userStore = useUserStore()
const route = useRoute()

// 当前页面标题
const currentPageTitle = computed(() => {
  return route.meta.title || '仪表盘'
})

// 组件挂载时初始化用户状态
onMounted(() => {
  userStore.initUserState()
})
</script>

<style scoped>
.admin-layout {
  display: flex;
  height: 100vh;
  background: var(--bg-secondary);
  overflow: hidden;
}

.admin-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background: var(--bg-secondary);
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.content-body {
  flex: 1;
  padding: var(--spacing-lg);
  overflow-y: auto;
  background: var(--bg-secondary);
}
</style>
