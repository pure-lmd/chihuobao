<template>
  <el-header class="portal-header">
    <div class="header-content">
      <div class="logo" @click="goHome">
        <el-icon size="28" color="#DC2626"><Food /></el-icon>
        <h1>吃货宝</h1>
      </div>
      <div class="nav-menu">
        <el-menu
          :default-active="activeMenu"
          class="header-menu"
          mode="horizontal"
          background-color="#ffffff"
          text-color="#333"
          active-text-color="#DC2626"
          router
        >
          <el-menu-item index="/home">
            <el-icon><House /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/shops">
            <el-icon><Shop /></el-icon>
            <span>商家</span>
          </el-menu-item>
          <el-menu-item index="/orders">
            <el-icon><Document /></el-icon>
            <span>订单</span>
          </el-menu-item>
        </el-menu>
      </div>
      <div class="user-info">
        <!-- 未登录状态 -->
        <div v-if="!userStore.isLogin" class="login-actions">
          <el-button type="primary" @click="goLogin">
            <el-icon><User /></el-icon>
            <span>登录</span>
          </el-button>
        </div>
        <!-- 已登录状态 -->
        <div v-else class="user-profile">
          <el-dropdown @command="handleCommand">
            <span class="user-dropdown">
              <el-avatar :size="32" :src="getAvatarUrl(userStore?.userInfo?.avatar)" />
              <span class="username">{{ userStore?.userInfo?.name || userStore?.userInfo?.username || '用户' }}</span>
              <el-icon class="el-icon--right"><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>
                  个人信息
                </el-dropdown-item>
                <el-dropdown-item command="logout" divided>
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
  </el-header>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import { House, Document, User, ArrowDown, SwitchButton, Food, Shop } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
// 处理菜单选择
const activeMenu = computed(() => {
  return route.path
})

// 跳转到登录页面
const goLogin = () => {
  router.push('/login')
}

// 跳转到首页
const goHome = () => {
  router.push('/home')
}

// 处理头像URL
const getAvatarUrl = (avatar) => {
  if (!avatar) return '/files/avatar.png'
  if (avatar.startsWith('http')) return avatar
  return avatar
}

// 处理用户下拉菜单命令
const handleCommand = async (command) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'logout':
      try {
        await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        userStore.logout()
        ElMessage.success('退出登录成功')
        router.push('/home')
      } catch {
        // 用户取消退出
      }
      break
  }
}
</script>

<style scoped>
.portal-header {
  background: #fff;
  border-bottom: 1px solid #e6e6e6;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
}

.header-content {
  display: flex;
  align-items: center;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.logo {
  margin-right: 40px;
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.logo h1 {
  color: #DC2626;
  font-size: 22px;
  font-weight: 700;
  margin: 0;
}

.nav-menu {
  flex: 1;
  display: flex;
  align-items: center;
}

.header-menu {
  border-bottom: none;
  flex: 1;
}

:deep(.el-menu--horizontal) {
  border-bottom: none;
}

:deep(.el-menu--horizontal .el-menu-item) {
  border-bottom: none;
}

:deep(.el-menu--horizontal .el-menu-item.is-active) {
  border-bottom: 2px solid #DC2626;
}

:deep(.el-menu--horizontal .el-menu-item:hover) {
  background-color: rgba(220, 38, 38, 0.1);
}

.user-info {
  margin-left: 20px;
}

.login-actions {
  display: flex;
  align-items: center;
}

.user-profile {
  display: flex;
  align-items: center;
}

.user-dropdown {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 6px;
  transition: background-color 0.3s;
}

.user-dropdown:hover {
  background-color: rgba(220, 38, 38, 0.1);
}

.username {
  margin-left: 8px;
  margin-right: 4px;
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

:deep(.el-dropdown-menu__item) {
  padding: 8px 16px;
}

:deep(.el-dropdown-menu__item .el-icon) {
  margin-right: 8px;
}
</style>
