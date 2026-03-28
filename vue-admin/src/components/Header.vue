<template>
  <el-header class="admin-header">
    <div class="header-left">
      <slot name="left" />
    </div>
    <div class="header-right">
      <el-dropdown @command="handleCommand">
        <div class="user-info">
          <el-avatar :size="32" :src="getAvatarUrl(userStore.userInfo.avatar)" icon="User" />
          <span class="username">{{ userStore.userInfo.name || userStore.userInfo.username }}</span>
          <el-icon class="arrow-down"><ArrowDown /></el-icon>
        </div>
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
  </el-header>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import { User, ArrowDown, SwitchButton } from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

// 处理头像URL
const getAvatarUrl = (avatar) => {
  if (!avatar) return ''
  if (avatar.startsWith('http')) return avatar
  return avatar
}

// 处理下拉菜单命令
const handleCommand = async (command) => {
  switch (command) {
    case 'profile':
      router.push('/manager/profile')
      break
    case 'logout':
      try {
        await ElMessageBox.confirm('确定要退出登录吗？', '退出确认', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        userStore.logout()
        ElMessage.success('已退出登录')
        router.push('/login')
      } catch {
        // 用户取消退出
      }
      break
  }
}
</script>

<style scoped>
.admin-header {
  background: #fff;
  border-bottom: 1px solid var(--border-color);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 var(--spacing-lg);
  height: 60px;
  box-shadow: var(--shadow-sm);
}

.header-left {
  display: flex;
  align-items: center;
  height: 100%;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: var(--border-radius-md);
  transition: all 0.2s ease;
}

.user-info:hover {
  background-color: #fff5f2;
  color: #ff6b35;
}

.username {
  margin: 0 8px;
  color: var(--text-primary);
  font-weight: 500;
}

.arrow-down {
  color: var(--text-placeholder);
  transition: transform 0.2s ease;
}

.user-info:hover .arrow-down {
  color: #ff6b35;
  transform: rotate(180deg);
}
</style>
