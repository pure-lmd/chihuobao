<template>
  <el-aside width="250px" class="admin-sidebar">
    <div class="sidebar-header">
      <div class="logo-wrapper">
        <img :src="logo" alt="后台管理系统" class="sidebar-logo" />
      </div>
      <div class="title-wrapper">
        <span class="sidebar-title">吃货宝后台管理系统</span>
        <span class="sidebar-subtitle">CHIHUOBAO ADMIN</span>
      </div>
    </div>
    <el-menu
      :default-active="activeMenu"
      :default-openeds="openMenus"
      class="sidebar-menu"
      :collapse="isCollapse"
      :collapse-transition="false"
      background-color="#1e293b"
      text-color="#94a3b8"
      active-text-color="#ff6b35"
      router
    >
      <el-menu-item index="/manager/dashboard">
        <div class="icon-wrapper"><el-icon><House /></el-icon></div>
        <template #title>仪表盘</template>
      </el-menu-item>
      <el-menu-item index="/manager/user">
        <div class="icon-wrapper"><el-icon><Avatar /></el-icon></div>
        <template #title>用户列表</template>
      </el-menu-item>
      <el-sub-menu index="/manager/takeout">
        <template #title>
          <div class="icon-wrapper"><el-icon><Shop /></el-icon></div>
          <span class="menu-group-label">外卖管理</span>
        </template>
        <el-menu-item index="/manager/shop">
          <el-icon><Shop /></el-icon>
          <template #title>商店管理</template>
        </el-menu-item>
        <el-menu-item index="/manager/product">
          <el-icon><Food /></el-icon>
          <template #title>菜品管理</template>
        </el-menu-item>
        <el-menu-item index="/manager/category">
          <el-icon><Grid /></el-icon>
          <template #title>分类管理</template>
        </el-menu-item>
        <el-menu-item index="/manager/orders">
          <el-icon><ShoppingCart /></el-icon>
          <template #title>订单管理</template>
        </el-menu-item>
      </el-sub-menu>
    </el-menu>
  </el-aside>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRoute } from 'vue-router'
import { House, Avatar, Shop, ShoppingCart, Food, Grid } from '@element-plus/icons-vue'
import logo from '../assets/imgs/logo.svg'

const route = useRoute()

const isCollapse = ref(false)

// 处理菜单选择
const activeMenu = computed(() => {
  return route.path
})

const openMenus = computed(() => {
  const segments = route.path.split('/').filter(Boolean)
  if (segments.length >= 3) {
    return [`/${segments.slice(0, 3).join('/')}`]
  }
  return []
})

// 暴露activeMenu供父组件使用
defineExpose({
  activeMenu
})
</script>

<style scoped>
.admin-sidebar {
  background: #1e293b;
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden;
  box-shadow: 2px 0 8px rgba(0, 21, 41, 0.15);
}

.sidebar-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 20px;
  color: #fff;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
  background: linear-gradient(180deg, #1a2332 0%, #162032 100%);
  position: relative;
}

.sidebar-header::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 20px;
  right: 20px;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(255, 107, 53, 0.3), transparent);
}

.logo-wrapper {
  position: relative;
}

.sidebar-logo {
  width: 40px;
  height: 40px;
  object-fit: contain;
  background: #fff;
  border-radius: 10px;
  padding: 6px;
  box-shadow: 0 2px 8px rgba(255, 107, 53, 0.2);
}

.title-wrapper {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.sidebar-title {
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #fff 0%, #e2e8f0 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  line-height: 1.2;
}

.sidebar-subtitle {
  font-size: 11px;
  color: #64748b;
  letter-spacing: 1px;
  text-transform: uppercase;
}

.sidebar-menu {
  flex: 1;
  border-right: none;
  overflow-y: auto;
  background: transparent;
  padding: 12px 8px;
}

.icon-wrapper {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.06);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  transition: all 0.3s ease;
}

:deep(.el-menu-item) {
  border-radius: var(--border-radius-md);
  margin-bottom: 4px;
  height: 44px;
  line-height: 44px;
  transition: all 0.2s ease;
  position: relative;
}

:deep(.el-menu-item:hover) {
  background-color: rgba(255, 107, 53, 0.08) !important;
  color: #ff6b35 !important;
}

:deep(.el-menu-item:hover) .icon-wrapper {
  background: rgba(255, 107, 53, 0.15);
  transform: scale(1.05);
}

:deep(.el-menu-item.is-active) {
  background-color: rgba(255, 107, 53, 0.12) !important;
  color: #ff6b35 !important;
  font-weight: 500;
}

:deep(.el-menu-item.is-active) .icon-wrapper {
  background: #ff6b35;
  color: #fff;
}

:deep(.el-menu-item.is-active)::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 3px;
  height: 20px;
  background: #ff6b35;
  border-radius: 0 2px 2px 0;
}

:deep(.el-sub-menu .el-menu-item) {
  margin-bottom: 4px;
  height: 40px;
  line-height: 40px;
  background-color: #162032 !important;
  border-radius: var(--border-radius-md);
}

:deep(.el-sub-menu__title) {
  border-radius: var(--border-radius-md);
  margin-bottom: 4px;
  transition: all 0.2s ease;
  position: relative;
}

:deep(.el-sub-menu__title:hover) {
  background-color: rgba(255, 107, 53, 0.08) !important;
  color: #ff6b35 !important;
}

:deep(.el-sub-menu__title:hover) .icon-wrapper {
  background: rgba(255, 107, 53, 0.15);
  transform: scale(1.05);
}

.menu-group-label {
  font-size: 14px;
  color: inherit;
  font-weight: 400;
}

/* Scrollbar styles */
.admin-sidebar::-webkit-scrollbar {
  width: 6px;
}

.admin-sidebar::-webkit-scrollbar-track {
  background: transparent;
}

.admin-sidebar::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.15);
  border-radius: 3px;
}

.admin-sidebar::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 255, 255, 0.25);
}

.sidebar-menu::-webkit-scrollbar {
  width: 6px;
}

.sidebar-menu::-webkit-scrollbar-track {
  background: transparent;
}

.sidebar-menu::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 3px;
}

.sidebar-menu::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 255, 255, 0.2);
}
</style>
