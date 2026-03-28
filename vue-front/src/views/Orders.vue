<template>
  <div class="orders-container">
    <div class="container">
      <!-- 页面头部 -->
      <div class="page-header">
        <h1 class="page-title">我的订单</h1>
        <div class="filter-tabs">
          <el-radio-group v-model="activeStatus" size="large" @change="handleStatusChange">
            <el-radio-button label="">全部</el-radio-button>
            <el-radio-button :label="1">待付款</el-radio-button>
            <el-radio-button :label="2">待接单</el-radio-button>
            <el-radio-button :label="3">配送中</el-radio-button>
            <el-radio-button :label="4">已完成</el-radio-button>
            <el-radio-button :label="5">已取消</el-radio-button>
          </el-radio-group>
        </div>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading-wrapper">
        <el-skeleton :rows="6" animated v-for="n in 3" :key="n" style="margin-bottom: 20px" />
      </div>

      <!-- 订单列表 -->
      <div v-else-if="orders.length > 0" class="orders-list">
        <div
          v-for="order in orders"
          :key="order.id"
          class="order-card"
          @click="viewOrderDetail(order)"
        >
          <div class="order-header">
            <div class="order-info">
              <span class="order-no">订单号：{{ order.orderNo }}</span>
              <span class="order-time">{{ formatTime(order.createTime) }}</span>
            </div>
            <el-tag :type="getStatusType(order.status)" size="small" effect="light">
              {{ getStatusText(order.status) }}
            </el-tag>
          </div>

          <div class="order-body">
            <div class="shop-name">
              <el-icon><Shop /></el-icon>
              {{ order.shopName || '未知商家' }}
            </div>
            <div class="order-summary">
              <span class="item-count">共 {{ order.itemCount || 1 }} 件商品</span>
              <div class="order-amount">
                <span class="label">实付金额：</span>
                <span class="amount">
                  <span class="symbol">¥</span>
                  <span class="value">{{ order.payAmount?.toFixed(2) || '0.00' }}</span>
                </span>
              </div>
            </div>
          </div>

          <div class="order-footer">
            <div class="order-actions">
              <!-- 待付款 -->
              <template v-if="order.status === 1">
                <el-button
                  type="primary"
                  size="small"
                  @click.stop="payOrder(order)"
                >
                  立即支付
                </el-button>
                <el-button
                  size="small"
                  @click.stop="cancelOrder(order)"
                >
                  取消订单
                </el-button>
              </template>

              <!-- 待接单 -->
              <template v-else-if="order.status === 2">
                <el-button
                  size="small"
                  @click.stop="cancelOrder(order)"
                >
                  取消订单
                </el-button>
              </template>

              <!-- 配送中 -->
              <template v-else-if="order.status === 3">
                <el-button
                  type="primary"
                  size="small"
                  @click.stop="confirmReceive(order)"
                >
                  确认收货
                </el-button>
              </template>

              <!-- 已完成 -->
              <template v-else-if="order.status === 4">
                <el-button
                  size="small"
                  @click.stop="reorder(order)"
                >
                  再来一单
                </el-button>
              </template>

              <!-- 已取消 -->
              <template v-else-if="order.status === 5">
                <el-button
                  size="small"
                  @click.stop="reorder(order)"
                >
                  重新下单
                </el-button>
              </template>

              <el-button
                link
                size="small"
                @click.stop="viewOrderDetail(order)"
              >
                查看详情
              </el-button>
            </div>
          </div>
        </div>

        <!-- 分页 -->
        <div class="pagination-wrapper">
          <el-pagination
            v-model:current-page="pageNum"
            v-model:page-size="pageSize"
            :page-sizes="[10, 20, 50]"
            :total="total"
            layout="total, sizes, prev, pager, next"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>

      <!-- 空状态 -->
      <div v-else class="empty-wrapper">
        <el-empty description="暂无订单">
          <template #description>
            <p>{{ activeStatus ? '该状态下暂无订单' : '您还没有下过订单' }}</p>
          </template>
          <el-button type="primary" @click="goToShops">去逛逛</el-button>
        </el-empty>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Shop } from '@element-plus/icons-vue'
import http from '../utils/http.js'
import { useUserStore } from '../stores/user.js'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

// 加载状态
const loading = ref(false)

// 订单列表
const orders = ref([])

// 分页参数
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 当前选中的状态筛选
const activeStatus = ref('')

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

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 获取订单列表
const fetchOrders = async () => {
  if (!userStore.isLogin) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  loading.value = true
  try {
    const params = {
      userId: userStore.userInfo.id,
      pageNum: pageNum.value,
      pageSize: pageSize.value
    }

    // 如果有状态筛选，添加状态参数
    if (activeStatus.value !== '') {
      params.status = activeStatus.value
    }

    const res = await http.get('/orders/selectOrdersPage', { params })

    if (res.code === 200) {
      orders.value = res.data?.list || []
      total.value = res.data?.total || 0
    } else {
      ElMessage.error(res.msg || '获取订单列表失败')
    }
  } catch (error) {
    console.error('获取订单列表失败:', error)
    ElMessage.error('获取订单列表失败')
  } finally {
    loading.value = false
  }
}

// 状态筛选变化
const handleStatusChange = () => {
  pageNum.value = 1
  fetchOrders()
}

// 分页大小变化
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchOrders()
}

// 页码变化
const handleCurrentChange = (val) => {
  pageNum.value = val
  fetchOrders()
}

// 查看订单详情
const viewOrderDetail = (order) => {
  router.push(`/order/${order.id}`)
}

// 支付订单
const payOrder = async (order) => {
  try {
    await ElMessageBox.confirm(
      `确认支付订单 ${order.orderNo}，金额 ¥${order.payAmount?.toFixed(2)}？`,
      '确认支付',
      {
        confirmButtonText: '确认支付',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    // 调用支付API（这里模拟支付成功）
    const res = await http.put('/orders/updateOrdersById', {
      id: order.id,
      status: 2, // 待接单
      payTime: new Date().toISOString()
    })

    if (res.code === 200) {
      ElMessage.success('支付成功')
      fetchOrders()
    } else {
      ElMessage.error(res.msg || '支付失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('支付失败:', error)
      ElMessage.error('支付失败')
    }
  }
}

// 取消订单
const cancelOrder = async (order) => {
  try {
    await ElMessageBox.confirm(
      '确定要取消该订单吗？',
      '取消订单',
      {
        confirmButtonText: '确定',
        cancelButtonText: '再想想',
        type: 'warning'
      }
    )

    const res = await http.put('/orders/updateOrdersById', {
      id: order.id,
      status: 5 // 已取消
    })

    if (res.code === 200) {
      ElMessage.success('订单已取消')
      fetchOrders()
    } else {
      ElMessage.error(res.msg || '取消失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消订单失败:', error)
      ElMessage.error('取消失败')
    }
  }
}

// 确认收货
const confirmReceive = async (order) => {
  try {
    await ElMessageBox.confirm(
      '确认已收到商品？',
      '确认收货',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const res = await http.put('/orders/updateOrdersById', {
      id: order.id,
      status: 4, // 已完成
      finishTime: new Date().toISOString()
    })

    if (res.code === 200) {
      ElMessage.success('确认收货成功')
      fetchOrders()
    } else {
      ElMessage.error(res.msg || '操作失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('确认收货失败:', error)
      ElMessage.error('操作失败')
    }
  }
}

// 再来一单/重新下单
const reorder = (order) => {
  router.push(`/shop/${order.shopId}`)
}

// 去逛逛
const goToShops = () => {
  router.push('/shops')
}

onMounted(() => {
  // 从路由参数读取状态
  const status = route.query.status
  if (status) {
    activeStatus.value = Number(status)
  }
  fetchOrders()
})
</script>

<style scoped>
.orders-container {
  min-height: 100vh;
  background-color: #f5f5f5;
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
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 16px 0;
}

.filter-tabs {
  display: flex;
  gap: 8px;
}

/* 加载状态 */
.loading-wrapper {
  padding: 20px 0;
}

/* 订单列表 */
.orders-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* 订单卡片 */
.order-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  cursor: pointer;
  transition: box-shadow 0.2s;
}

.order-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

/* 订单头部 */
.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  background: #f9fafb;
  border-bottom: 1px solid #e5e7eb;
}

.order-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.order-no {
  font-size: 14px;
  color: #6b7280;
  font-family: monospace;
}

.order-time {
  font-size: 14px;
  color: #9ca3af;
}

/* 订单主体 */
.order-body {
  padding: 20px;
}

.shop-name {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 12px;
}

.order-summary {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.item-count {
  font-size: 14px;
  color: #6b7280;
}

.order-amount {
  display: flex;
  align-items: center;
  gap: 4px;
}

.order-amount .label {
  font-size: 14px;
  color: #6b7280;
}

.order-amount .amount {
  color: #ef4444;
  font-weight: 600;
}

.order-amount .symbol {
  font-size: 12px;
}

.order-amount .value {
  font-size: 18px;
}

/* 订单底部 */
.order-footer {
  padding: 12px 20px;
  border-top: 1px solid #f3f4f6;
  display: flex;
  justify-content: flex-end;
}

.order-actions {
  display: flex;
  gap: 8px;
}

/* 分页 */
.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 32px;
  padding: 20px 0;
}

/* 空状态 */
.empty-wrapper {
  padding: 80px 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .container {
    padding: 0 12px;
  }

  .page-title {
    font-size: 20px;
  }

  .filter-tabs {
    overflow-x: auto;
    padding-bottom: 8px;
  }

  .filter-tabs :deep(.el-radio-group) {
    flex-wrap: nowrap;
  }

  .order-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .order-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;
  }

  .order-summary {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .order-actions {
    flex-wrap: wrap;
  }
}
</style>
