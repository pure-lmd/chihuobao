<template>
  <div class="page-container">
    <!-- 搜索和操作栏 -->
    <div class="header-section">
      <div class="search-section">
        <el-form :inline="true" :model="queryParams" class="search-form">
          <el-form-item label="订单编号">
            <el-input
              v-model="queryParams.orderNo"
              placeholder="请输入订单编号"
              style="width: 200px"
              clearable
            />
          </el-form-item>
          <el-form-item label="订单状态">
            <el-select
              v-model="queryParams.status"
              placeholder="请选择状态"
              style="width: 150px"
              clearable
            >
              <el-option label="待支付" :value="1" />
              <el-option label="已支付" :value="2" />
              <el-option label="已发货" :value="3" />
              <el-option label="已完成" :value="4" />
              <el-option label="已取消" :value="5" />
            </el-select>
          </el-form-item>
          <el-form-item label="收货人">
            <el-input
              v-model="queryParams.receiverName"
              placeholder="收货人姓名/电话"
              style="width: 150px"
              clearable
            />
          </el-form-item>
          <el-form-item label="下单时间">
            <el-date-picker
              v-model="queryParams.createTime"
              type="date"
              placeholder="请选择日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"
              style="width: 150px"
              clearable
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">查询</el-button>
            <el-button @click="resetSearch">重置</el-button>
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
        <el-table-column label="订单编号" prop="orderNo" min-width="120" />
        <el-table-column label="用户ID" prop="userId" min-width="120" />
        <el-table-column label="店铺ID" prop="shopId" min-width="120" />
        <el-table-column label="订单总金额" prop="totalAmount" min-width="120" />
        <el-table-column label="实付金额" prop="payAmount" min-width="120" />
        <el-table-column label="运费" prop="deliveryFee" min-width="120" />
        <el-table-column label="订单备注" prop="remark" min-width="120" />
        <el-table-column label="订单状态" prop="status" min-width="120">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusLabel(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="收货人姓名" prop="receiverName" min-width="120" />
        <el-table-column label="收货人电话" prop="receiverPhone" min-width="120" />
        <el-table-column label="收货地址" prop="receiverAddress" min-width="120" />
        <el-table-column label="支付时间" prop="payTime" min-width="120" />
        <el-table-column label="发货时间" prop="deliveryTime" min-width="120" />
        <el-table-column label="完成时间" prop="finishTime" min-width="120" />
        <el-table-column label="创建时间" prop="createTime" min-width="120" />
        <el-table-column label="修改时间" prop="updateTime" min-width="120" />
        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              @click="handleDetail(scope.row)"
            >
              详情
            </el-button>
            <el-button
              v-if="scope.row.status === 2"
              type="success"
              size="small"
              @click="handleDeliver(scope.row)"
            >
              出餐
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

    <!-- 订单详情对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单编号" :span="2">{{ formData.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="用户ID">{{ formData.userId }}</el-descriptions-item>
        <el-descriptions-item label="店铺ID">{{ formData.shopId }}</el-descriptions-item>
        <el-descriptions-item label="订单总金额">¥{{ formData.totalAmount }}</el-descriptions-item>
        <el-descriptions-item label="实付金额">¥{{ formData.payAmount }}</el-descriptions-item>
        <el-descriptions-item label="运费">¥{{ formData.deliveryFee }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">{{ formData.status }}</el-descriptions-item>
        <el-descriptions-item label="订单备注" :span="2">{{ formData.remark || '无' }}</el-descriptions-item>
        <el-descriptions-item label="收货人姓名">{{ formData.receiverName }}</el-descriptions-item>
        <el-descriptions-item label="收货人电话">{{ formData.receiverPhone }}</el-descriptions-item>
        <el-descriptions-item label="收货地址" :span="2">{{ formData.receiverAddress }}</el-descriptions-item>
        <el-descriptions-item label="支付时间">{{ formData.payTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="发货时间">{{ formData.deliveryTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="完成时间">{{ formData.finishTime || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import http from '@/utils/http'

// 文件上传地址
const uploadUrl = '/api/files/upload'

// 响应式数据
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)

// 查询参数
const queryParams = reactive({
  orderNo: '',
  status: '',
  receiverName: '',
  createTime: ''
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
const formData = reactive({})

// 状态映射
const statusMap = {
  1: { label: '待支付', type: 'warning' },
  2: { label: '已支付', type: 'primary' },
  3: { label: '已发货', type: 'success' },
  4: { label: '已完成', type: 'info' },
  5: { label: '已取消', type: 'danger' }
}

// 获取状态标签
const getStatusLabel = (status) => {
  return statusMap[status]?.label || status
}

// 获取状态标签类型
const getStatusType = (status) => {
  return statusMap[status]?.type || ''
}

// 获取列表数据
const getList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      ...queryParams
    }

    const response = await http.get('/orders/selectOrdersPage', { params })

    if (response.code === 200) {
      const pageInfo = response.data
      tableData.value = pageInfo.list || []
      pagination.total = pageInfo.total || 0
    } else {
      ElMessage.error(response.msg || '获取数据失败')
    }
  } catch (error) {
    console.error('获取Orders列表失败:', error)
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
  queryParams.orderNo = ''
  queryParams.status = ''
  queryParams.receiverName = ''
  queryParams.createTime = ''
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

// 查看详情
const handleDetail = (row) => {
  dialogTitle.value = '订单详情'
  dialogVisible.value = true
  // 填充表单数据（只读）
  Object.assign(formData, {
    id: row.id,
    orderNo: row.orderNo,
    userId: row.userId,
    shopId: row.shopId,
    totalAmount: row.totalAmount,
    payAmount: row.payAmount,
    deliveryFee: row.deliveryFee,
    remark: row.remark,
    status: row.status,
    receiverName: row.receiverName,
    receiverPhone: row.receiverPhone,
    receiverAddress: row.receiverAddress,
    payTime: row.payTime,
    deliveryTime: row.deliveryTime,
    finishTime: row.finishTime,
  })
}

// 关闭对话框
const handleDialogClose = () => {
  dialogVisible.value = false
}

// 出餐
const handleDeliver = async (row) => {
  try {
    await ElMessageBox.confirm('确认对该订单进行出餐？', '提示', {
      confirmButtonText: '确认',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const now = new Date()
    const deliveryTime = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}-${String(now.getDate()).padStart(2, '0')} ${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}:${String(now.getSeconds()).padStart(2, '0')}`
    const updateData = {
      id: row.id,
      status: 3,  // 已发货
      deliveryTime
    }

    const response = await http.put('/orders/updateOrdersById', updateData)

    if (response.code === 200) {
      ElMessage.success('出餐成功')
      getList()
    } else {
      ElMessage.error(response.msg || '发货失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('发货失败:', error)
      ElMessage.error('发货失败')
    }
  }
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
