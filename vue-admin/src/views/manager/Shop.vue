<template>
  <div class="page-container">
    <!-- 搜索和操作栏 -->
    <div class="header-section">
      <div class="search-section">
        <el-form :inline="true" :model="queryParams" class="search-form">
                    <el-form-item label="店铺名称">
            <el-input
              v-model="queryParams.name"
              placeholder="请输入店铺名称"
              style="width: 200px"
              clearable
            />
          </el-form-item>
          <el-form-item label="店铺地址">
            <el-input
              v-model="queryParams.address"
              placeholder="请输入店铺地址"
              style="width: 200px"
              clearable
            />
          </el-form-item>
          <el-form-item label="联系电话">
            <el-input
              v-model="queryParams.phone"
              placeholder="请输入联系电话"
              style="width: 200px"
              clearable
            />
          </el-form-item>
          <el-form-item label="店铺简介">
            <el-input
              v-model="queryParams.description"
              placeholder="请输入店铺简介"
              style="width: 200px"
              clearable
            />
          </el-form-item>
          <el-form-item label="起送价">
            <el-input
              v-model="queryParams.minPrice"
              placeholder="请输入起送价"
              style="width: 200px"
              clearable
            />
          </el-form-item>
          <el-form-item label="配送费">
            <el-input
              v-model="queryParams.deliveryFee"
              placeholder="请输入配送费"
              style="width: 200px"
              clearable
            />
          </el-form-item>
          <el-form-item label="综合评分">
            <el-input
              v-model="queryParams.rating"
              placeholder="请输入综合评分"
              style="width: 200px"
              clearable
            />
          </el-form-item>
          <el-form-item label="营业状态">
            <el-input
              v-model="queryParams.status"
              placeholder="请输入营业状态: 0-休息, 1-营业"
              style="width: 200px"
              clearable
            />
          </el-form-item>
          <el-form-item label="店主">
            <el-input
              v-model="queryParams.ownerId"
              placeholder="请输入店主ID，关联user表"
              style="width: 200px"
              clearable
            />
          </el-form-item>
          <el-form-item label="创建时间">
            <el-date-picker
              v-model="queryParams.createTime"
              type="datetime"
              placeholder="请选择创建时间"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              style="width: 200px"
              clearable
            />
          </el-form-item>
          <el-form-item label="修改时间">
            <el-date-picker
              v-model="queryParams.updateTime"
              type="datetime"
              placeholder="请选择修改时间"
              format="YYYY-MM-DD HH:mm:ss"
              value-format="YYYY-MM-DD HH:mm:ss"
              style="width: 200px"
              clearable
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">
              查询
            </el-button>
            <el-button @click="resetSearch">
              重置
            </el-button>
            <el-button type="success" @click="handleAdd">
              新增
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>

    <!-- 表格 -->
    <div class="table-container">
      <el-table
        :data="tableData"
        style="width: 100%"
        v-loading="loading"
        :header-cell-style="{ background: '#f5f7fa', color: '#606266' }"
      >
        <el-table-column label="ID" prop="id" min-width="120" />
        <el-table-column label="店铺名称" prop="name" min-width="120" />
        <el-table-column label="店铺地址" prop="address" min-width="120" />
        <el-table-column label="联系电话" prop="phone" min-width="120" />
        <el-table-column label="店铺封面" width="120" align="center">
          <template #default="scope">
            <ImagePreview
              :image-url="scope.row.coverUrl"
              :width="60"
              :height="60"
              :alt="封面"
            />
          </template>
        </el-table-column>
        <el-table-column label="店铺简介" prop="description" min-width="120" />
        <el-table-column label="起送价" prop="minPrice" min-width="120" />
        <el-table-column label="配送费" prop="deliveryFee" min-width="120" />
        <el-table-column label="综合评分" prop="rating" min-width="120" />
        <el-table-column label="营业状态" prop="status" min-width="120" />
        <el-table-column label="店主" prop="ownerId" min-width="120" />
        <el-table-column label="创建时间" prop="createTime" min-width="120" />
        <el-table-column label="修改时间" prop="updateTime" min-width="120" />
        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              type="danger"
              size="small"
              @click="handleDelete(scope.row.id)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.pageNum"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="80px"
      >
        <el-form-item label="店铺名称" prop="name">
          <el-input
            v-model="formData.name"
            placeholder="请输入店铺名称"
            clearable
          />
        </el-form-item>
        <el-form-item label="店铺地址" prop="address">
          <el-input
            v-model="formData.address"
            placeholder="请输入店铺地址"
            clearable
          />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input
            v-model="formData.phone"
            placeholder="请输入联系电话"
            clearable
          />
        </el-form-item>
        <el-form-item label="封面" prop="coverUrl">
          <ImageUpload
            v-model="formData.coverUrl"
            :upload-url="uploadUrl"
            :width="120"
            :height="120"
          />
        </el-form-item>
        <el-form-item label="店铺简介" prop="description">
          <el-input
            v-model="formData.description"
            placeholder="请输入店铺简介"
            clearable
          />
        </el-form-item>
        <el-form-item label="起送价" prop="minPrice">
          <el-input
            v-model="formData.minPrice"
            placeholder="请输入起送价"
            clearable
          />
        </el-form-item>
        <el-form-item label="配送费" prop="deliveryFee">
          <el-input
            v-model="formData.deliveryFee"
            placeholder="请输入配送费"
            clearable
          />
        </el-form-item>
        <el-form-item label="综合评分" prop="rating">
          <el-input
            v-model="formData.rating"
            placeholder="请输入综合评分"
            clearable
          />
        </el-form-item>
        <el-form-item label="营业状态" prop="status">
          <el-input
            v-model="formData.status"
            placeholder="请输入营业状态: 0-休息, 1-营业"
            clearable
          />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select
            v-model="formData.categoryId"
            placeholder="请选择分类"
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="cat in shopCategoryList"
              :key="cat.id"
              :label="cat.name"
              :value="cat.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="店主" prop="ownerId">
          <el-select
            v-model="formData.ownerId"
            placeholder="请选择店主"
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="user in userList"
              :key="user.id"
              :label="user.username"
              :value="user.id"
            />
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import http from '@/utils/http'
import ImageUpload from '@/components/ImageUpload.vue'
import ImagePreview from '@/components/ImagePreview.vue'

// 文件上传地址
const uploadUrl = '/api/files/upload'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)

// 查询参数
const queryParams = reactive({
  name: '',
  address: '',
  phone: '',
  description: '',
  minPrice: '',
  deliveryFee: '',
  rating: '',
  status: '',
  ownerId: '',
  createTime: '',
  updateTime: ''
})

// 分页参数
const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

// 表格数据
const tableData = ref([])

// 表单数据
const formData = reactive({
  name: '',
  address: '',
  phone: '',
  coverUrl: '',
  description: '',
  minPrice: '',
  deliveryFee: '',
  rating: '',
  status: '',
  categoryId: '',
  ownerId: '',
})

// 用户列表（用于店主下拉框）
const userList = ref([])

// 商家分类列表
const shopCategoryList = ref([])

// 获取商家分类列表
const getShopCategoryList = async () => {
  try {
    const res = await http.get('/shopCategory/selectAll')
    if (res.code === 200) {
      shopCategoryList.value = res.data || []
    }
  } catch (error) {
    console.error('获取分类失败:', error)
  }
}

// 获取用户列表
const getUserList = async () => {
  try {
    const response = await http.get('/user/selectUserPage', {
      params: { pageNum: 1, pageSize: 100 }
    })
    if (response.code === 200) {
      userList.value = response.data.list || []
    }
  } catch (error) {
    console.error('获取用户列表失败:', error)
  }
}

// 表单验证规则
const formRules = reactive({
  name: [
    { required: true, message: '请输入店铺名称', trigger: 'blur' }
  ],
  address: [
  ],
  phone: [
  ],
  coverUrl: [
  ],
  description: [
  ],
  minPrice: [
  ],
  deliveryFee: [
  ],
  rating: [
  ],
  status: [
    { required: true, message: '请输入营业状态', trigger: 'blur' }
  ],
  ownerId: [
    { required: true, message: '请输入店主', trigger: 'blur' }
  ],
})

// 获取列表数据
const getList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...queryParams
    }

    const response = await http.get('/shop/selectShopPage', { params })

    if (response.code === 200) {
      const pageInfo = response.data
      tableData.value = pageInfo.list || []
      pagination.total = pageInfo.total || 0
    } else {
      ElMessage.error(response.msg || '获取数据失败')
    }
  } catch (error) {
    console.error('获取Shop列表失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.pageNum = 1
  getList()
}

// 重置搜索
const resetSearch = () => {
  queryParams.name = ''
  queryParams.address = ''
  queryParams.phone = ''
  queryParams.description = ''
  queryParams.minPrice = ''
  queryParams.deliveryFee = ''
  queryParams.rating = ''
  queryParams.status = ''
  queryParams.ownerId = ''
  queryParams.createTime = ''
  queryParams.updateTime = ''
  pagination.pageNum = 1
  getList()
}

// 分页大小改变
const handleSizeChange = (size) => {
  pagination.pageSize = size
  pagination.pageNum = 1
  getList()
}

// 页码改变
const handleCurrentChange = (page) => {
  pagination.pageNum = page
  getList()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增Shop'
  dialogVisible.value = true
  resetFormData()
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑Shop'
  dialogVisible.value = true

  // 填充表单数据
  Object.assign(formData, {
    id: row.id,
    name: row.name,
    address: row.address,
    phone: row.phone,
    coverUrl: row.coverUrl,
    description: row.description,
    minPrice: row.minPrice,
    deliveryFee: row.deliveryFee,
    rating: row.rating,
    status: row.status,
    ownerId: row.ownerId,
  })
}

// 删除
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这条记录吗？',
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const response = await http.delete(`/shop/deleteShopById/${id}`)

    if (response.code === 200) {
      ElMessage.success('删除成功')
      getList()
    } else {
      ElMessage.error(response.msg || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除Shop失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value.validate()

    submitLoading.value = true

    const isEdit = !!formData.id
    let response
    if (isEdit) {
      // 编辑
      response = await http.put('/shop/updateShopById', formData)
    } else {
      // 新增
      response = await http.post('/shop/addShop', formData)
    }

    if (response.code === 200) {
      ElMessage.success(isEdit ? '更新成功' : '新增成功')
      dialogVisible.value = false
      getList()
    } else {
      ElMessage.error(response.msg || '操作失败')
    }
  } catch (error) {
    console.error('提交表单失败:', error)
    ElMessage.error('操作失败')
  } finally {
    submitLoading.value = false
  }
}

// 重置表单数据
const resetFormData = () => {
  Object.assign(formData, {
    name: '',
    address: '',
    phone: '',
    coverUrl: '',
    description: '',
    minPrice: '',
    deliveryFee: '',
    rating: '',
    status: '',
    categoryId: '',
    ownerId: '',
  })

  if (formRef.value) {
    formRef.value.resetFields()
  }
}

// 关闭对话框
const handleDialogClose = () => {
  resetFormData()
}



// 页面加载时获取数据
onMounted(() => {
  getList()
  getUserList()
  getShopCategoryList()
})
</script>

<style scoped>
.page-container {
  padding: 0;
  min-height: 100%;
}

.search-form .el-form-item {
  margin-bottom: var(--spacing-sm);
  margin-right: var(--spacing-md);
}
</style>
