<template>
  <div class="page-container">
    <!-- 搜索和操作栏 -->
    <div class="header-section">
      <div class="search-section">
        <el-form :inline="true" :model="queryParams" class="search-form">
                    <el-form-item label="店铺">
            <el-select
              v-model="queryParams.shopId"
              placeholder="请选择店铺"
              style="width: 200px"
              clearable
            >
              <el-option
                v-for="shop in shopList"
                :key="shop.id"
                :label="shop.name"
                :value="shop.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="分类">
            <el-select
              v-model="queryParams.categoryId"
              placeholder="请选择分类"
              style="width: 200px"
              clearable
            >
              <el-option
                v-for="category in categoryList"
                :key="category.id"
                :label="category.name"
                :value="category.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="商品名称">
            <el-input
              v-model="queryParams.name"
              placeholder="请输入商品名称"
              style="width: 200px"
              clearable
            />
          </el-form-item>
          <el-form-item label="价格">
            <el-input
              v-model="queryParams.price"
              placeholder="请输入价格"
              style="width: 200px"
              clearable
            />
          </el-form-item>
          <el-form-item label="库存">
            <el-input
              v-model="queryParams.stock"
              placeholder="请输入库存"
              style="width: 200px"
              clearable
            />
          </el-form-item>
          <el-form-item label="商品描述">
            <el-input
              v-model="queryParams.description"
              placeholder="请输入商品描述"
              style="width: 200px"
              clearable
            />
          </el-form-item>
          <el-form-item label="累计销量">
            <el-input
              v-model="queryParams.sales"
              placeholder="请输入累计销量"
              style="width: 200px"
              clearable
            />
          </el-form-item>
          <el-form-item label="售卖状态">
            <el-select
              v-model="queryParams.status"
              placeholder="请选择售卖状态"
              style="width: 200px"
              clearable
            >
              <el-option label="上架" :value="1" />
              <el-option label="下架" :value="0" />
            </el-select>
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
        <el-table-column label="店铺" prop="shopName" min-width="120" />
        <el-table-column label="分类" prop="categoryName" min-width="120" />
        <el-table-column label="商品名称" prop="name" min-width="120" />
        <el-table-column label="价格" prop="price" min-width="120" />
        <el-table-column label="库存" prop="stock" min-width="120" />
        <el-table-column label="商品描述" prop="description" min-width="120" />
        <el-table-column label="商品图片" width="120" align="center">
          <template #default="scope">
            <ImagePreview
              :image-url="scope.row.imageUrl"
              :width="60"
              :height="60"
              :alt="商品图片"
            />
          </template>
        </el-table-column>
        <el-table-column label="累计销量" prop="sales" min-width="120" />
        <el-table-column label="售卖状态" min-width="120">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
              {{ scope.row.status === 1 ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
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
        <el-form-item label="店铺" prop="shopId">
          <el-select
            v-model="formData.shopId"
            placeholder="请选择店铺"
            style="width: 100%"
            clearable
          >
            <el-option
              v-for="shop in shopList"
              :key="shop.id"
              :label="shop.name"
              :value="shop.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select
            v-model="formData.categoryId"
            placeholder="请选择分类"
            style="width: 100%"
            clearable
          >
            <el-option
              v-for="category in categoryList"
              :key="category.id"
              :label="category.name"
              :value="category.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="商品名称" prop="name">
          <el-input
            v-model="formData.name"
            placeholder="请输入商品名称"
            clearable
          />
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input
            v-model="formData.price"
            placeholder="请输入价格"
            clearable
          />
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-input
            v-model="formData.stock"
            placeholder="请输入库存"
            clearable
          />
        </el-form-item>
        <el-form-item label="商品描述" prop="description">
          <el-input
            v-model="formData.description"
            placeholder="请输入商品描述"
            clearable
          />
        </el-form-item>
        <el-form-item label="商品图片" prop="imageUrl">
          <ImageUpload
            v-model="formData.imageUrl"
            :upload-url="uploadUrl"
            :width="120"
            :height="120"
          />
        </el-form-item>
        <el-form-item label="累计销量" prop="sales">
          <el-input
            v-model="formData.sales"
            placeholder="请输入累计销量"
            clearable
          />
        </el-form-item>
        <el-form-item label="售卖状态" prop="status">
          <el-select v-model="formData.status" placeholder="请选择售卖状态" style="width: 100%">
            <el-option label="上架" :value="1" />
            <el-option label="下架" :value="0" />
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
  shopId: '',
  categoryId: '',
  name: '',
  price: '',
  stock: '',
  description: '',
  sales: '',
  status: '',
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

// 店铺列表
const shopList = ref([])
// 分类列表
const categoryList = ref([])

// 表单数据
const formData = reactive({
  shopId: '',
  categoryId: '',
  name: '',
  price: '',
  stock: '',
  description: '',
  imageUrl: '',
  sales: '',
  status: '',
})

// 表单验证规则
const formRules = reactive({
  shopId: [
    { required: true, message: '请输入店铺ID', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请输入分类ID', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入商品名称', trigger: 'blur' }
  ],
  price: [
    { required: true, message: '请输入价格', trigger: 'blur' }
  ],
  stock: [
  ],
  description: [
  ],
  imageUrl: [
  ],
  sales: [
  ],
  status: [
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

    const response = await http.get('/product/selectProductPage', { params })

    if (response.code === 200) {
      const pageInfo = response.data
      tableData.value = pageInfo.list || []
      pagination.total = pageInfo.total || 0
    } else {
      ElMessage.error(response.msg || '获取数据失败')
    }
  } catch (error) {
    console.error('获取Product列表失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
  }
}

// 获取店铺列表
const getShopList = async () => {
  try {
    const response = await http.get('/shop/selectShopPage', { params: { pageSize: 1000 } })
    if (response.code === 200) {
      shopList.value = response.data.list || []
    }
  } catch (error) {
    console.error('获取店铺列表失败:', error)
  }
}

// 获取分类列表
const getCategoryList = async () => {
  try {
    const response = await http.get('/category/selectCategoryPage', { params: { pageSize: 1000 } })
    if (response.code === 200) {
      categoryList.value = response.data.list || []
    }
  } catch (error) {
    console.error('获取分类列表失败:', error)
  }
}

// 搜索
const handleSearch = () => {
  pagination.pageNum = 1
  getList()
}

// 重置搜索
const resetSearch = () => {
  queryParams.shopId = ''
  queryParams.categoryId = ''
  queryParams.name = ''
  queryParams.price = ''
  queryParams.stock = ''
  queryParams.description = ''
  queryParams.sales = ''
  queryParams.status = ''
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
  dialogTitle.value = '新增Product'
  dialogVisible.value = true
  resetFormData()
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑Product'
  dialogVisible.value = true

  // 填充表单数据
  Object.assign(formData, {
    id: row.id,
    shopId: row.shopId,
    categoryId: row.categoryId,
    name: row.name,
    price: row.price,
    stock: row.stock,
    description: row.description,
    imageUrl: row.imageUrl,
    sales: row.sales,
    status: row.status,
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

    const response = await http.delete(`/product/deleteProductById/${id}`)

    if (response.code === 200) {
      ElMessage.success('删除成功')
      getList()
    } else {
      ElMessage.error(response.msg || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除Product失败:', error)
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
      response = await http.put('/product/updateProductById', formData)
    } else {
      // 新增
      response = await http.post('/product/addProduct', formData)
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
    shopId: '',
    categoryId: '',
    name: '',
    price: '',
    stock: '',
    description: '',
    imageUrl: '',
    sales: '',
    status: '',
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
  getShopList()
  getCategoryList()
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
