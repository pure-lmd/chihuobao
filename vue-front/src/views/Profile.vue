                                                                                                                                                                                                                                                                                          <template>
  <div class="profile-container">
    <div class="container">
      <!-- 页面头部 -->
      <div class="page-header">
        <h1 class="page-title">个人信息</h1>
        <p class="page-subtitle">管理您的账户信息和偏好设置</p>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading-wrapper">
        <el-skeleton :rows="10" animated />
      </div>

      <div v-else class="profile-content">
        <!-- 左侧：头像卡片 -->
        <div class="profile-sidebar">
          <div class="avatar-card">
            <div class="avatar-wrapper">
              <el-avatar
                :size="120"
                :src="getAvatarUrl(form.avatar)"
                class="profile-avatar"
              />
              <div class="avatar-overlay" @click="triggerAvatarUpload">
                <el-icon><Camera /></el-icon>
                <span>更换头像</span>
              </div>
            </div>
            <input
              ref="avatarInput"
              type="file"
              accept="image/*"
              style="display: none"
              @change="handleAvatarChange"
            />
            <h3 class="user-name">{{ form.name || form.username || '用户' }}</h3>
            <p class="user-role">{{ form.role === 'ADMIN' ? '管理员' : '普通用户' }}</p>
            <div class="user-stats">
              <div class="stat-item">
                <span class="stat-value">{{ orderCount }}</span>
                <span class="stat-label">订单</span>
              </div>
              <div class="stat-divider"></div>
              <div class="stat-item">
                <span class="stat-value">{{ userDays }}</span>
                <span class="stat-label">天数</span>
              </div>
            </div>
          </div>

          <!-- 快捷菜单 -->
          <div class="quick-menu">
            <div
              v-for="item in menuItems"
              :key="item.key"
              class="menu-item"
              :class="{ active: activeTab === item.key }"
              @click="activeTab = item.key"
            >
              <el-icon><component :is="item.icon" /></el-icon>
              <span>{{ item.label }}</span>
            </div>
          </div>
        </div>

        <!-- 右侧：内容区域 -->
        <div class="profile-main">
          <!-- 基本信息 -->
          <div v-show="activeTab === 'basic'" class="content-section">
            <div class="section-header">
              <h2 class="section-title">
                <el-icon><User /></el-icon>
                基本信息
              </h2>
              <el-button
                v-if="!isEditing"
                type="primary"
                @click="startEditing"
              >
                <el-icon><Edit /></el-icon>
                编辑资料
              </el-button>
              <div v-else class="edit-actions">
                <el-button @click="cancelEditing">取消</el-button>
                <el-button
                  type="primary"
                  :loading="saving"
                  @click="saveProfile"
                >
                  保存
                </el-button>
              </div>
            </div>

            <el-form
              ref="formRef"
              :model="form"
              :rules="rules"
              label-position="top"
              class="profile-form"
              :disabled="!isEditing"
            >
              <div class="form-row">
                <el-form-item label="用户名" prop="username">
                  <el-input
                    v-model="form.username"
                    placeholder="请输入用户名"
                    :prefix-icon="User"
                    disabled
                  />
                </el-form-item>
                <el-form-item label="姓名" prop="name">
                  <el-input
                    v-model="form.name"
                    placeholder="请输入姓名"
                    :prefix-icon="Avatar"
                    maxlength="20"
                    show-word-limit
                  />
                </el-form-item>
              </div>

              <el-form-item label="头像链接" prop="avatar">
                <el-input
                  v-model="form.avatar"
                  placeholder="请输入头像图片链接"
                  :prefix-icon="Picture"
                />
              </el-form-item>
            </el-form>
          </div>

          <!-- 账号安全 -->
          <div v-show="activeTab === 'security'" class="content-section">
            <div class="section-header">
              <h2 class="section-title">
                <el-icon><Lock /></el-icon>
                账号安全
              </h2>
            </div>

            <div class="security-list">
              <div class="security-item">
                <div class="security-info">
                  <el-icon class="security-icon" :size="24"><Key /></el-icon>
                  <div class="security-detail">
                    <h4>登录密码</h4>
                    <p>定期更换密码可以保护您的账号安全</p>
                  </div>
                </div>
                <el-button @click="showPasswordDialog = true">
                  修改密码
                </el-button>
              </div>

              <div class="security-item">
                <div class="security-info">
                  <el-icon class="security-icon" :size="24"><UserFilled /></el-icon>
                  <div class="security-detail">
                    <h4>用户ID</h4>
                    <p class="user-id">{{ form.id }}</p>
                  </div>
                </div>
                <el-tag type="info">不可修改</el-tag>
              </div>

              <div class="security-item">
                <div class="security-info">
                  <el-icon class="security-icon" :size="24"><CircleCheck /></el-icon>
                  <div class="security-detail">
                    <h4>账号状态</h4>
                    <p>您的账号状态正常</p>
                  </div>
                </div>
                <el-tag type="success">正常</el-tag>
              </div>
            </div>
          </div>

          <!-- 我的订单概览 -->
          <div v-show="activeTab === 'orders'" class="content-section">
            <div class="section-header">
              <h2 class="section-title">
                <el-icon><Document /></el-icon>
                订单概览
              </h2>
              <el-button type="primary" @click="router.push('/orders')">
                查看全部
              </el-button>
            </div>

            <div class="order-stats">
              <div
                v-for="stat in orderStats"
                :key="stat.status"
                class="order-stat-card"
                @click="goToOrders(stat.status)"
              >
                <el-icon :size="32" :color="stat.color">
                  <component :is="stat.icon" />
                </el-icon>
                <span class="stat-count">{{ stat.count }}</span>
                <span class="stat-name">{{ stat.name }}</span>
              </div>
            </div>

            <div v-if="recentOrders.length > 0" class="recent-orders">
              <h3 class="subsection-title">最近订单</h3>
              <div class="order-list">
                <div
                  v-for="order in recentOrders"
                  :key="order.id"
                  class="order-item"
                  @click="viewOrderDetail(order)"
                >
                  <div class="order-info">
                    <span class="order-no">{{ order.orderNo }}</span>
                    <span class="order-shop">{{ order.shopName || '未知商家' }}</span>
                  </div>
                  <div class="order-meta">
                    <span class="order-amount">¥{{ order.payAmount?.toFixed(2) }}</span>
                    <el-tag :type="getStatusType(order.status)" size="small">
                      {{ getStatusText(order.status) }}
                    </el-tag>
                  </div>
                </div>
              </div>
            </div>

            <el-empty v-else description="暂无订单">
              <el-button type="primary" @click="router.push('/shops')">
                去逛逛
              </el-button>
            </el-empty>
          </div>
        </div>
      </div>
    </div>

    <!-- 修改密码对话框 -->
    <el-dialog
      v-model="showPasswordDialog"
      title="修改密码"
      width="400px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="passwordFormRef"
        :model="passwordForm"
        :rules="passwordRules"
        label-position="top"
      >
        <el-form-item label="当前密码" prop="oldPassword">
          <el-input
            v-model="passwordForm.oldPassword"
            type="password"
            placeholder="请输入当前密码"
            show-password
          />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="passwordForm.newPassword"
            type="password"
            placeholder="请输入新密码（6-20位）"
            show-password
          />
        </el-form-item>
        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input
            v-model="passwordForm.confirmPassword"
            type="password"
            placeholder="请再次输入新密码"
            show-password
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showPasswordDialog = false">取消</el-button>
        <el-button
          type="primary"
          :loading="changingPassword"
          @click="changePassword"
        >
          确认修改
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  User, Edit, Camera, Lock, Key, CircleCheck,
  UserFilled, Document, Picture, Avatar,
  Wallet, Box, Check, Clock, Close
} from '@element-plus/icons-vue'
import http from '../utils/http.js'
import { useUserStore } from '../stores/user.js'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)
const passwordFormRef = ref(null)
const avatarInput = ref(null)

// 加载状态
const loading = ref(true)
const saving = ref(false)
const changingPassword = ref(false)

// 编辑状态
const isEditing = ref(false)
const activeTab = ref('basic')

// 密码对话框
const showPasswordDialog = ref(false)

// 菜单项
const menuItems = [
  { key: 'basic', label: '基本信息', icon: 'User' },
  { key: 'security', label: '账号安全', icon: 'Lock' },
  { key: 'orders', label: '订单概览', icon: 'Document' }
]

// 表单数据
const form = reactive({
  id: null,
  username: '',
  name: '',
  avatar: '',
  role: ''
})

// 原始数据（用于取消编辑）
const originalForm = reactive({})

// 密码表单
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 订单统计
const orderCount = ref(0)
const userDays = ref(1)
const recentOrders = ref([])

// 订单状态统计
const orderStats = ref([
  { status: 1, name: '待付款', count: 0, icon: 'Wallet', color: '#f59e0b' },
  { status: 2, name: '待接单', count: 0, icon: 'Clock', color: '#3b82f6' },
  { status: 3, name: '配送中', count: 0, icon: 'Box', color: '#10b981' },
  { status: 4, name: '已完成', count: 0, icon: 'Check', color: '#6b7280' }
])

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度在2-20个字符之间', trigger: 'blur' }
  ]
}

// 密码表单验证规则
const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入当前密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在6-20位之间', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 状态映射
const statusMap = {
  1: { text: '待付款', type: 'warning' },
  2: { text: '待接单', type: 'primary' },
  3: { text: '配送中', type: 'success' },
  4: { text: '已完成', type: 'info' },
  5: { text: '已取消', type: 'danger' }
}

// 获取状态文本
const getStatusText = (status) => {
  return statusMap[status]?.text || '未知状态'
}

// 获取状态标签类型
const getStatusType = (status) => {
  return statusMap[status]?.type || 'info'
}

// 获取头像URL
const getAvatarUrl = (avatar) => {
  if (!avatar) return '/files/avatar.png'
  if (avatar.startsWith('http')) return avatar
  return avatar
}

// 获取用户信息
const fetchUserInfo = async () => {
  if (!userStore.isLogin) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  loading.value = true
  try {
    const res = await http.get(`/user/selectUserById/${userStore.userInfo.id}`)
    if (res.code === 200) {
      const userData = res.data
      Object.assign(form, userData)
      Object.assign(originalForm, userData)

      // 计算用户注册天数（模拟）
      userDays.value = Math.floor(Math.random() * 365) + 1
    } else {
      ElMessage.error(res.msg || '获取用户信息失败')
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    ElMessage.error('获取用户信息失败')
  } finally {
    loading.value = false
  }
}

// 获取订单统计
const fetchOrderStats = async () => {
  try {
    const res = await http.get('/orders/selectOrdersPage', {
      params: {
        userId: userStore.userInfo.id,
        pageNum: 1,
        pageSize: 100
      }
    })

    if (res.code === 200) {
      const orders = res.data?.list || []
      orderCount.value = res.data?.total || 0

      // 统计各状态订单数量
      orderStats.value.forEach(stat => {
        stat.count = orders.filter(o => o.status === stat.status).length
      })

      // 获取最近3个订单
      recentOrders.value = orders.slice(0, 3)
    }
  } catch (error) {
    console.error('获取订单统计失败:', error)
  }
}

// 开始编辑
const startEditing = () => {
  isEditing.value = true
}

// 取消编辑
const cancelEditing = () => {
  Object.assign(form, originalForm)
  isEditing.value = false
  formRef.value?.clearValidate()
}

// 保存资料
const saveProfile = async () => {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  saving.value = true
  try {
    const res = await http.put('/user/updateUserById', {
      id: form.id,
      name: form.name,
      avatar: form.avatar
    })

    if (res.code === 200) {
      ElMessage.success('保存成功')
      Object.assign(originalForm, form)
      isEditing.value = false

      // 更新store中的用户信息
      userStore.updateUserInfo({
        name: form.name,
        avatar: form.avatar
      })
    } else {
      ElMessage.error(res.msg || '保存失败')
    }
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error('保存失败')
  } finally {
    saving.value = false
  }
}

// 触发头像上传
const triggerAvatarUpload = () => {
  if (!isEditing.value && activeTab.value === 'basic') {
    ElMessage.info('请先点击"编辑资料"按钮')
    return
  }
  avatarInput.value?.click()
}

// 处理头像选择
const handleAvatarChange = async (event) => {
  const file = event.target.files[0]
  if (!file) return

  // 验证文件类型
  if (!file.type.startsWith('image/')) {
    ElMessage.error('请选择图片文件')
    return
  }

  // 验证文件大小（最大2MB）
  if (file.size > 2 * 1024 * 1024) {
    ElMessage.error('图片大小不能超过2MB')
    return
  }

  // 上传到服务器
  const formData = new FormData()
  formData.append('file', file)
  try {
    const res = await http.post('/files/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    if (res.code === 200) {
      form.avatar = res.data
      // 自动保存头像到数据库
      const saveRes = await http.put('/user/updateUserById', {
        id: form.id,
        name: form.name,
        avatar: form.avatar
      })
      if (saveRes.code === 200) {
        ElMessage.success('头像更新成功')
        userStore.updateUserInfo({ avatar: form.avatar })
      } else {
        ElMessage.error(saveRes.msg || '保存失败')
      }
    } else {
      ElMessage.error(res.msg || '上传失败')
    }
  } catch (error) {
    ElMessage.error('上传失败')
  }

  // 清空input，允许重复选择同一文件
  event.target.value = ''
}

// 修改密码
const changePassword = async () => {
  const valid = await passwordFormRef.value?.validate().catch(() => false)
  if (!valid) return

  changingPassword.value = true
  try {
    const res = await http.put('/user/updatePassword', {
      username: form.username,
      password: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword
    })

    if (res.code === 200) {
      ElMessage.success('密码修改成功')
      showPasswordDialog.value = false
      passwordForm.oldPassword = ''
      passwordForm.newPassword = ''
      passwordForm.confirmPassword = ''
    } else {
      ElMessage.error(res.msg || '密码修改失败')
    }
  } catch (error) {
    console.error('密码修改失败:', error)
    ElMessage.error('密码修改失败')
  } finally {
    changingPassword.value = false
  }
}

// 跳转到订单页面
const goToOrders = (status) => {
  router.push({ path: '/orders', query: { status } })
}

// 查看订单详情
const viewOrderDetail = (order) => {
  router.push(`/order/${order.id}`)
}

onMounted(() => {
  fetchUserInfo()
  fetchOrderStats()
})
</script>

<style scoped>
.profile-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4e8ec 100%);
  padding: 24px 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 页面头部 */
.page-header {
  margin-bottom: 24px;
  text-align: center;
}

.page-title {
  font-size: 28px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 8px 0;
}

.page-subtitle {
  font-size: 14px;
  color: #6b7280;
  margin: 0;
}

/* 加载状态 */
.loading-wrapper {
  background: white;
  border-radius: 16px;
  padding: 40px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

/* 内容布局 */
.profile-content {
  display: grid;
  grid-template-columns: 300px 1fr;
  gap: 24px;
}

/* 侧边栏 */
.profile-sidebar {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* 头像卡片 */
.avatar-card {
  background: white;
  border-radius: 16px;
  padding: 32px 24px;
  text-align: center;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.avatar-wrapper {
  position: relative;
  display: inline-block;
  margin-bottom: 16px;
}

.profile-avatar {
  border: 4px solid #f3f4f6;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.avatar-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: white;
  opacity: 0;
  transition: opacity 0.3s;
  cursor: pointer;
}

.avatar-overlay:hover {
  opacity: 1;
}

.avatar-overlay .el-icon {
  font-size: 24px;
  margin-bottom: 4px;
}

.avatar-overlay span {
  font-size: 12px;
}

.user-name {
  font-size: 20px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 4px 0;
}

.user-role {
  font-size: 14px;
  color: #6b7280;
  margin: 0 0 20px 0;
}

.user-stats {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 24px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #DC2626;
}

.stat-label {
  font-size: 12px;
  color: #6b7280;
}

.stat-divider {
  width: 1px;
  height: 40px;
  background: #e5e7eb;
}

/* 快捷菜单 */
.quick-menu {
  background: white;
  border-radius: 16px;
  padding: 8px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 16px;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.2s;
  color: #4b5563;
}

.menu-item:hover {
  background: #f3f4f6;
  color: #1f2937;
}

.menu-item.active {
  background: #DC2626;
  color: white;
}

.menu-item .el-icon {
  font-size: 18px;
}

.menu-item span {
  font-size: 14px;
  font-weight: 500;
}

/* 主内容区 */
.profile-main {
  background: white;
  border-radius: 16px;
  padding: 32px;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
  min-height: 500px;
}

.content-section {
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e5e7eb;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
  margin: 0;
}

.section-title .el-icon {
  color: #DC2626;
}

.edit-actions {
  display: flex;
  gap: 12px;
}

/* 表单样式 */
.profile-form {
  max-width: 600px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
  color: #374151;
  padding-bottom: 6px;
}

/* 安全设置 */
.security-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.security-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background: #f9fafb;
  border-radius: 12px;
  border: 1px solid #e5e7eb;
}

.security-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.security-icon {
  color: #DC2626;
  background: white;
  padding: 10px;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.security-detail h4 {
  font-size: 15px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 4px 0;
}

.security-detail p {
  font-size: 13px;
  color: #6b7280;
  margin: 0;
}

.user-id {
  font-family: monospace;
  color: #9ca3af;
}

/* 订单统计 */
.order-stats {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  margin-bottom: 32px;
}

.order-stat-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 24px 16px;
  background: #f9fafb;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s;
  border: 2px solid transparent;
}

.order-stat-card:hover {
  background: white;
  border-color: #DC2626;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(220, 38, 38, 0.15);
}

.stat-count {
  font-size: 24px;
  font-weight: 700;
  color: #1f2937;
}

.stat-name {
  font-size: 13px;
  color: #6b7280;
}

/* 最近订单 */
.subsection-title {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 16px 0;
}

.order-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.order-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  background: #f9fafb;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.2s;
}

.order-item:hover {
  background: #f3f4f6;
}

.order-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.order-no {
  font-size: 13px;
  color: #9ca3af;
  font-family: monospace;
}

.order-shop {
  font-size: 14px;
  font-weight: 500;
  color: #1f2937;
}

.order-meta {
  display: flex;
  align-items: center;
  gap: 12px;
}

.order-amount {
  font-size: 16px;
  font-weight: 600;
  color: #DC2626;
}

/* 响应式设计 */
@media (max-width: 992px) {
  .profile-content {
    grid-template-columns: 1fr;
  }

  .profile-sidebar {
    flex-direction: row;
    flex-wrap: wrap;
  }

  .avatar-card {
    flex: 1;
    min-width: 280px;
  }

  .quick-menu {
    flex: 1;
    min-width: 200px;
  }

  .order-stats {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .container {
    padding: 0 12px;
  }

  .page-title {
    font-size: 24px;
  }

  .profile-main {
    padding: 20px;
  }

  .form-row {
    grid-template-columns: 1fr;
  }

  .section-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .security-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .order-stats {
    grid-template-columns: repeat(2, 1fr);
  }

  .order-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .order-meta {
    width: 100%;
    justify-content: space-between;
  }
}

@media (max-width: 576px) {
  .order-stats {
    grid-template-columns: 1fr 1fr;
    gap: 12px;
  }

  .order-stat-card {
    padding: 16px 12px;
  }

  .user-stats {
    gap: 16px;
  }
}
</style>
