<template>
  <div class="profile-container">
    <!-- 个人信息卡片 -->
    <div class="profile-card">
      <div class="profile-header">
        <div class="avatar-section">
          <el-avatar :size="80" :src="getAvatarUrl(userInfo.avatar)" icon="User" />
          <el-button
            type="primary"
            size="small"
            @click="handleAvatarUpload"
            style="margin-top: 10px"
          >
            更换头像
          </el-button>
        </div>

        <div class="basic-info">
          <h2>{{ userInfo.name || userInfo.username }}</h2>
          <p class="role-tag">{{ getRoleText(userInfo.role) }}</p>
          <p class="username">用户名：{{ userInfo.username }}</p>
        </div>
      </div>

      <el-divider />

      <div class="profile-form">
        <el-form
          ref="formRef"
          :model="formData"
          :rules="formRules"
          label-width="100px"
          size="large"
        >
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="用户名" prop="username">
                <el-input
                  v-model="formData.username"
                  disabled
                  placeholder="用户名不可修改"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="姓名" prop="name">
                <el-input
                  v-model="formData.name"
                  placeholder="请输入姓名"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="角色">
                <el-input
                  :value="getRoleText(formData.role)"
                  disabled
                  placeholder="角色不可修改"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="头像链接" prop="avatar">
                <el-input
                  v-model="formData.avatar"
                  placeholder="请输入头像链接"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item>
            <el-button
              type="primary"
              @click="handleSave"
              :loading="saveLoading"
              size="large"
            >
              保存修改
            </el-button>
            <el-button
              @click="handleReset"
              size="large"
            >
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>

    <!-- 修改密码卡片 -->
    <div class="password-card">
      <div class="card-header">
        <h3>修改密码</h3>
      </div>

      <el-form
        ref="passwordFormRef"
        :model="passwordForm"
        :rules="passwordRules"
        label-width="100px"
        size="large"
      >
        <el-form-item label="原密码" prop="oldPassword">
          <el-input
            v-model="passwordForm.oldPassword"
            type="password"
            placeholder="请输入原密码"
            show-password
          />
        </el-form-item>

        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="passwordForm.newPassword"
            type="password"
            placeholder="请输入新密码"
            show-password
          />
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="passwordForm.confirmPassword"
            type="password"
            placeholder="请确认新密码"
            show-password
          />
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            @click="handlePasswordChange"
            :loading="passwordLoading"
            size="large"
          >
            修改密码
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 文件上传对话框 -->
    <el-dialog
      v-model="uploadDialogVisible"
      title="上传头像"
      width="400px"
    >
      <el-upload
        class="avatar-uploader"
        :action="uploadUrl"
        :show-file-list="false"
        :on-success="handleUploadSuccess"
        :before-upload="beforeUpload"
        accept="image/*"
      >
        <img v-if="uploadImageUrl" :src="getAvatarUrl(uploadImageUrl)" class="avatar-preview" />
        <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
      </el-upload>

      <template #footer>
        <el-button @click="uploadDialogVisible = false">取消</el-button>
        <el-button
          type="primary"
          @click="handleConfirmUpload"
          :disabled="!uploadImageUrl"
        >
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../stores/user'
import http from '../utils/http.js'

const userStore = useUserStore()

// 用户信息
const userInfo = ref({})

// 表单数据
const formData = reactive({
  id: null,
  username: '',
  name: '',
  role: '',
  avatar: ''
})

// 密码表单
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 表单引用
const formRef = ref()
const passwordFormRef = ref()

// 加载状态
const saveLoading = ref(false)
const passwordLoading = ref(false)

// 上传相关
const uploadDialogVisible = ref(false)
const uploadImageUrl = ref('')
const uploadUrl = '/api/files/upload'
const fileBaseUrl = ''

// 获取头像完整URL
const getAvatarUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) return url
  return fileBaseUrl + url
}

// 表单验证规则
const formRules = {
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ]
}

// 密码验证规则
const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入原密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' }
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

// 获取角色文本
const getRoleText = (role) => {
  const roleMap = {
    'SUPER_ADMIN': '超级管理员',
    'ADMIN': '管理员',
    'USER': '普通用户'
  }
  return roleMap[role] || role
}

// 获取用户信息
const getUserInfo = async () => {
  try {
    const response = await http.get(`/user/selectUserById/${userStore.userInfo.id}`)

    if (response.code === 200) {
      userInfo.value = response.data

      // 填充表单数据
      Object.assign(formData, {
        id: userInfo.value.id,
        username: userInfo.value.username,
        name: userInfo.value.name,
        role: userInfo.value.role,
        avatar: userInfo.value.avatar
      })
    } else {
      ElMessage.error(response.msg || '获取用户信息失败')
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    ElMessage.error('获取用户信息失败')
  }
}

// 保存修改
const handleSave = async () => {
  try {
    await formRef.value.validate()

    saveLoading.value = true

    const response = await http.put('/user/updateUserById', formData)

    if (response.code === 200) {
      ElMessage.success('保存成功')

      // 更新用户store中的信息
      userStore.updateUserInfo({
        name: formData.name,
        avatar: formData.avatar
      })

      // 重新获取用户信息
      await getUserInfo()
    } else {
      ElMessage.error(response.msg || '保存失败')
    }
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error('保存失败')
  } finally {
    saveLoading.value = false
  }
}

// 重置表单
const handleReset = () => {
  Object.assign(formData, {
    id: userInfo.value.id,
    username: userInfo.value.username,
    name: userInfo.value.name,
    role: userInfo.value.role,
    avatar: userInfo.value.avatar
  })
}

// 修改密码
const handlePasswordChange = async () => {
  try {
    await passwordFormRef.value.validate()

    passwordLoading.value = true

    const response = await http.put('/updatePassword', {
      username: userInfo.value.username,
      password: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword,
      role: userInfo.value.role
    })

    if (response.code === 200) {
      ElMessage.success('密码修改成功')

      // 重置密码表单
      Object.assign(passwordForm, {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      })

      passwordFormRef.value.resetFields()
    } else {
      ElMessage.error(response.msg || '密码修改失败')
    }
  } catch (error) {
    console.error('密码修改失败:', error)
    ElMessage.error('密码修改失败')
  } finally {
    passwordLoading.value = false
  }
}

// 处理头像上传
const handleAvatarUpload = () => {
  uploadDialogVisible.value = true
  uploadImageUrl.value = ''
}

// 上传前验证
const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }
  return true
}

// 上传成功回调
const handleUploadSuccess = (response) => {
  if (response.code === 200 || response.code === '200') {
    uploadImageUrl.value = response.data
    ElMessage.success('图片上传成功')
  } else {
    ElMessage.error(response.msg || '图片上传失败')
  }
}

// 确认上传
const handleConfirmUpload = () => {
  if (uploadImageUrl.value) {
    formData.avatar = uploadImageUrl.value
    uploadDialogVisible.value = false
    ElMessage.success('头像已更新，请点击保存修改')
  }
}

// 组件挂载时获取用户信息
onMounted(() => {
  getUserInfo()
})
</script>

<style scoped>
.profile-container {
  padding: 0;
  min-height: 100%;
}

.profile-card,
.password-card {
  background: #fff;
  border-radius: var(--border-radius-lg);
  box-shadow: var(--shadow-sm);
  margin-bottom: var(--spacing-lg);
  padding: var(--spacing-xl);
}

.profile-header {
  display: flex;
  align-items: center;
  margin-bottom: var(--spacing-lg);
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-right: var(--spacing-xl);
}

.basic-info h2 {
  margin: 0 0 var(--spacing-sm) 0;
  color: var(--text-primary);
  font-size: var(--font-size-xl);
}

.role-tag {
  display: inline-block;
  background: #ff6b35;
  color: white;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: var(--font-size-xs);
  margin: 0 0 var(--spacing-sm) 0;
}

.username {
  color: var(--text-secondary);
  margin: 0;
  font-size: var(--font-size-base);
}

.card-header h3 {
  margin: 0 0 var(--spacing-lg) 0;
  color: var(--text-primary);
  font-size: var(--font-size-lg);
}

.profile-form {
  max-width: 600px;
}

.avatar-uploader {
  display: flex;
  justify-content: center;
}

.avatar-uploader :deep(.el-upload) {
  border: 1px dashed #d9d9d9;
  border-radius: var(--border-radius-md);
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: all 0.3s;
  width: 120px;
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-uploader :deep(.el-upload:hover) {
  border-color: #ff6b35;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
}

.avatar-preview {
  width: 120px;
  height: 120px;
  object-fit: cover;
}

:deep(.el-form-item__label) {
  font-weight: 500;
}
</style>
