<template>
  <div class="example-container">
    <div class="container">
      <!-- 页面标题 -->
      <div class="page-header">
        <h1>示例页面</h1>
        <p>这是一个示例页面，展示各种组件和功能</p>
      </div>

      <!-- 表单示例 -->
      <div class="example-section">
        <h2>表单示例</h2>
        <el-card>
          <el-form :model="form" label-width="120px">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="姓名">
                  <el-input v-model="form.name" placeholder="请输入姓名" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="邮箱">
                  <el-input v-model="form.email" placeholder="请输入邮箱" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="性别">
                  <el-select v-model="form.gender" placeholder="请选择性别">
                    <el-option label="男" value="male" />
                    <el-option label="女" value="female" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="生日">
                  <el-date-picker
                    v-model="form.birthday"
                    type="date"
                    placeholder="请选择生日"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="简介">
              <el-input
                v-model="form.description"
                type="textarea"
                :rows="4"
                placeholder="请输入简介"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm">提交</el-button>
              <el-button @click="resetForm">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>

      <!-- 数据展示示例 -->
      <div class="example-section">
        <h2>数据展示示例</h2>
        <el-card>
          <el-table :data="tableData" style="width: 100%">
            <el-table-column prop="id" label="ID" width="80" />
            <el-table-column prop="name" label="姓名" width="120" />
            <el-table-column prop="email" label="邮箱" width="200" />
            <el-table-column prop="department" label="部门" width="150" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="scope">
                <el-tag :type="scope.row.status === '在职' ? 'success' : 'danger'">
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template #default="scope">
                <el-button type="primary" link @click="editUser(scope.row)">
                  编辑
                </el-button>
                <el-button type="danger" link @click="deleteUser(scope.row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>

      <!-- 统计卡片示例 -->
      <div class="example-section">
        <h2>统计卡片示例</h2>
        <el-row :gutter="20">
          <el-col :span="6" v-for="stat in statsData" :key="stat.title">
            <el-card class="stat-card">
              <div class="stat-content">
                <div class="stat-icon" :style="{ backgroundColor: stat.color }">
                  <el-icon size="30" color="white">
                    <component :is="stat.icon" />
                  </el-icon>
                </div>
                <div class="stat-info">
                  <h3>{{ stat.value }}</h3>
                  <p>{{ stat.title }}</p>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

// 表单数据
const form = ref({
  name: '',
  email: '',
  gender: '',
  birthday: '',
  description: ''
})

// 表格数据
const tableData = ref([
  {
    id: 1,
    name: '张三',
    email: 'zhangsan@example.com',
    department: '技术部',
    status: '在职'
  },
  {
    id: 2,
    name: '李四',
    email: 'lisi@example.com',
    department: '销售部',
    status: '在职'
  },
  {
    id: 3,
    name: '王五',
    email: 'wangwu@example.com',
    department: '市场部',
    status: '离职'
  },
  {
    id: 4,
    name: '赵六',
    email: 'zhaoliu@example.com',
    department: '人事部',
    status: '在职'
  }
])

// 统计数据
const statsData = ref([
  {
    title: '总用户数',
    value: '1,234',
    icon: 'User',
    color: '#409EFF'
  },
  {
    title: '今日访问',
    value: '567',
    icon: 'View',
    color: '#67C23A'
  },
  {
    title: '本月订单',
    value: '89',
    icon: 'Document',
    color: '#E6A23C'
  },
  {
    title: '总收入',
    value: '¥12,345',
    icon: 'Money',
    color: '#F56C6C'
  }
])

// 提交表单
const submitForm = () => {
  ElMessage.success('表单提交成功！')
}

// 重置表单
const resetForm = () => {
  form.value = {
    name: '',
    email: '',
    gender: '',
    birthday: '',
    description: ''
  }
  ElMessage.info('表单已重置')
}

// 编辑用户
const editUser = (user) => {
  ElMessage.info(`编辑用户：${user.name}`)
}

// 删除用户
const deleteUser = (user) => {
  ElMessage.warning(`删除用户：${user.name}`)
}
</script>

<style scoped>
.example-container {
  min-height: 100vh;
  padding: 40px 0;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.page-header {
  text-align: center;
  margin-bottom: 50px;
}

.page-header h1 {
  font-size: 32px;
  color: #333;
  margin-bottom: 15px;
  font-weight: 600;
}

.page-header p {
  font-size: 16px;
  color: #666;
}

.example-section {
  margin-bottom: 40px;
}

.example-section h2 {
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
  font-weight: 600;
}

.stat-card {
  cursor: pointer;
  transition: transform 0.3s;
}

.stat-card:hover {
  transform: translateY(-2px);
}

.stat-content {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
}

.stat-info h3 {
  font-size: 24px;
  color: #333;
  margin-bottom: 5px;
  font-weight: 600;
}

.stat-info p {
  color: #666;
  font-size: 14px;
}
</style> 