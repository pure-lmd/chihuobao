<template>
  <div class="page-container">
    <!-- 搜索和操作栏 -->
    <div class="header-section">
      <div class="search-section">
        <el-form :inline="true" :model="queryParams" class="search-form">
                    <el-form-item label="订单ID">
            <el-input
              v-model="queryParams.orderId"
              placeholder="请输入订单ID"
              style="width: 200px"
              clearable
            />
          </el-form-item>
          <el-form-item label="商品ID">
            <el-input
              v-model="queryParams.productId"
              placeholder="请输入商品ID"
              style="width: 200px"
              clearable
            />
          </el-form-item>
          <el-form-item label="商品名称">
            <el-input
              v-model="queryParams.productName"
              placeholder="请输入商品名称"
              style="width: 200px"
              clearable
            />
          </el-form-item>
          <el-form-item label="单价">
            <el-input
              v-model="queryParams.price"
              placeholder="请输入单价"
              style="width: 200px"
              clearable
            />
          </el-form-item>
          <el-form-item label="购买数量">
            <el-input
              v-model="queryParams.quantity"
              placeholder="请输入购买数量"
              style="width: 200px"
              clearable
            />
          </el-form-item>
          <el-form-item label="总金额">
            <el-input
              v-model="queryParams.totalFee"
              placeholder="请输入总金额"
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
        <el-table-column label="订单ID" prop="orderId" min-width="120" />
        <el-table-column label="商品ID" prop="productId" min-width="120" />
        <el-table-column label="商品名称" prop="productName" min-width="120" />
        <el-table-column label="商品图片" width="120" align="center">
          <template #default="scope">
            <ImagePreview
              :image-url="scope.row.productImage"
              :width="60"
              :height="60"
              :alt="商品图片"
            />
          </template>
        </el-table-column>
        <el-table-column label="单价" prop="price" min-width="120" />
        <el-table-column label="购买数量" prop="quantity" min-width="120" />
        <el-table-column label="总金额" prop="totalFee" min-width="120" />
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
        <el-form-item label="订单ID" prop="orderId">
          <el-input
            v-model="formData.orderId"
            placeholder="请输入订单ID"
            clearable
          />
        </el-form-item>
        <el-form-item label="商品ID" prop="productId">
          <el-input
            v-model="formData.productId"
            placeholder="请输入商品ID"
            clearable
          />
        </el-form-item>
        <el-form-item label="商品名称" prop="productName">
          <el-input
            v-model="formData.productName"
            placeholder="请输入商品名称"
            clearable
          />
        </el-form-item>
        <el-form-item label="商品图片" prop="productImage">
          <ImageUpload
            v-model="formData.productImage"
            :upload-url="uploadUrl"
            :width="120"
            :height="120"
          />
        </el-form-item>
        <el-form-item label="单价" prop="price">
          <el-input
            v-model="formData.price"
            placeholder="请输入单价"
            clearable
          />
        </el-form-item>
        <el-form-item label="购买数量" prop="quantity">
          <el-input
            v-model="formData.quantity"
            placeholder="请输入购买数量"
            clearable
          />
        </el-form-item>
        <el-form-item label="总金额" prop="totalFee">
          <el-input
            v-model="formData.totalFee"
            placeholder="请输入总金额"
            clearable
          />
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
  orderId: '',
  productId: '',
  productName: '',
  price: '',
  quantity: '',
  totalFee: '',
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
  orderId: '',
  productId: '',
  productName: '',
  productImage: '',
  price: '',
  quantity: '',
  totalFee: '',
})

// 表单验证规则
const formRules = reactive({
  orderId: [
    { required: true, message: '请输入订单ID', trigger: 'blur' }
  ],
  productId: [
    { required: true, message: '请输入商品ID', trigger: 'blur' }
  ],
  productName: [
    { required: true, message: '请输入商品名称', trigger: 'blur' }
  ],
  productImage: [
  ],
  price: [
    { required: true, message: '请输入单价', trigger: 'blur' }
  ],
  quantity: [
    { required: true, message: '请输入购买数量', trigger: 'blur' }
  ],
  totalFee: [
    { required: true, message: '请输入总金额', trigger: 'blur' }
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

    const response = await http.get('/orderDetail/selectOrderDetailPage', { params })

    if (response.code === 200) {
      const pageInfo = response.data
      tableData.value = pageInfo.list || []
      pagination.total = pageInfo.total || 0
    } else {
      ElMessage.error(response.msg || '获取数据失败')
    }
  } catch (error) {
    console.error('获取OrderDetail列表失败:', error)
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
  queryParams.orderId = ''
  queryParams.productId = ''
  queryParams.productName = ''
  queryParams.price = ''
  queryParams.quantity = ''
  queryParams.totalFee = ''
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
  dialogTitle.value = '新增OrderDetail'
  dialogVisible.value = true
  resetFormData()
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑OrderDetail'
  dialogVisible.value = true

  // 填充表单数据
  Object.assign(formData, {
    id: row.id,
    orderId: row.orderId,
    productId: row.productId,
    productName: row.productName,
    productImage: row.productImage,
    price: row.price,
    quantity: row.quantity,
    totalFee: row.totalFee,
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

    const response = await http.delete(`/orderDetail/deleteOrderDetailById/${id}`)

    if (response.code === 200) {
      ElMessage.success('删除成功')
      getList()
    } else {
      ElMessage.error(response.msg || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除OrderDetail失败:', error)
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
      response = await http.put('/orderDetail/updateOrderDetailById', formData)
    } else {
      // 新增
      response = await http.post('/orderDetail/addOrderDetail', formData)
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
    orderId: '',
    productId: '',
    productName: '',
    productImage: '',
    price: '',
    quantity: '',
    totalFee: '',
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
