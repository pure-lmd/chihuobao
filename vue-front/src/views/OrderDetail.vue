<template>
  <div class="order-detail-container">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-wrapper">
      <el-skeleton :rows="10" animated />
    </div>

    <template v-else-if="order">
      <div class="container">
        <!-- 头部 -->
        <div class="page-header">
          <el-button link @click="goBack">
            <el-icon><ArrowLeft /></el-icon>
            返回订单列表
          </el-button>
          <h1 class="page-title">订单详情</h1>
        </div>

        <!-- 订单状态卡片 -->
        <el-card class="status-card" shadow="never">
          <div class="status-content">
            <div class="status-icon" :class="getStatusClass(order.status)">
              <el-icon :size="32">
                <component :is="getStatusIcon(order.status)" />
              </el-icon>
            </div>
            <div class="status-info">
              <h2 class="status-text">{{ getStatusText(order.status) }}</h2>
              <p class="status-desc">{{ getStatusDesc(order.status) }}</p>
            </div>
            <div class="status-actions">
              <template v-if="order.status === 1">
                <el-button type="primary" size="large" @click="payOrder">
                  立即支付
                </el-button>
                <el-button size="large" @click="cancelOrder">
                  取消订单
                </el-button>
              </template>
              <template v-else-if="order.status === 3">
                <el-button type="primary" size="large" @click="confirmReceive">
                  确认收货
                </el-button>
              </template>
              <template v-else-if="order.status === 4 || order.status === 5">
                <el-button type="primary" size="large" @click="reorder">
                  再次购买
                </el-button>
              </template>
            </div>
          </div>
        </el-card>

        <div class="detail-layout">
          <!-- 左侧：订单信息 -->
          <div class="detail-main">
            <!-- 商家信息 -->
            <el-card class="section-card" shadow="never">
              <template #header>
                <div class="card-header">
                  <span class="header-title">
                    <el-icon><Shop /></el-icon>
                    商家信息
                  </span>
                </div>
              </template>
              <div class="shop-info">
                <div class="shop-name">{{ order.shopName || '未知商家' }}</div>
                <div class="shop-meta">
                  <span>订单号：{{ order.orderNo }}</span>
                  <span>下单时间：{{ formatTime(order.createTime) }}</span>
                </div>
              </div>
            </el-card>

            <!-- 订单商品 -->
            <el-card class="section-card" shadow="never">
              <template #header>
                <div class="card-header">
                  <span class="header-title">
                    <el-icon><ShoppingBag /></el-icon>
                    商品信息
                  </span>
                  <span class="header-extra">共 {{ totalItems }} 件</span>
                </div>
              </template>
              <div class="product-list">
                <div
                  v-for="item in order.orderDetails"
                  :key="item.id"
                  class="product-item"
                >
                  <div class="product-image">
                    <img
                      :src="getProductImage(item.productImage)"
                      :alt="item.productName"
                      @error="handleImageError"
                    />
                  </div>
                  <div class="product-info">
                    <div class="product-name">{{ item.productName }}</div>
                    <div class="product-meta">
                      <span class="product-price">
                        ¥{{ item.price?.toFixed(2) }}
                      </span>
                      <span class="product-quantity">
                        x{{ item.quantity }}
                      </span>
                    </div>
                  </div>
                  <div class="product-total">
                    <span class="label">小计：</span>
                    <span class="amount">¥{{ item.totalFee?.toFixed(2) }}</span>
                  </div>
                </div>
              </div>
            </el-card>

            <!-- 收货信息 -->
            <el-card class="section-card" shadow="never">
              <template #header>
                <div class="card-header">
                  <span class="header-title">
                    <el-icon><Location /></el-icon>
                    收货信息
                  </span>
                </div>
              </template>
              <div class="address-info">
                <div class="address-row">
                  <span class="label">收货人：</span>
                  <span class="value">{{ order.receiverName }}</span>
                </div>
                <div class="address-row">
                  <span class="label">联系电话：</span>
                  <span class="value">{{ order.receiverPhone }}</span>
                </div>
                <div class="address-row">
                  <span class="label">收货地址：</span>
                  <span class="value">{{ order.receiverAddress }}</span>
                </div>
              </div>
            </el-card>

            <!-- 订单备注 -->
            <el-card v-if="order.remark" class="section-card" shadow="never">
              <template #header>
                <div class="card-header">
                  <span class="header-title">
                    <el-icon><Document /></el-icon>
                    订单备注
                  </span>
                </div>
              </template>
              <div class="remark-content">{{ order.remark }}</div>
            </el-card>
          </div>

          <!-- 右侧：结算信息 -->
          <div class="detail-sidebar">
            <div class="summary-card" :class="{ 'is-sticky': true }">
              <el-card shadow="never">
                <template #header>
                  <div class="card-header">
                    <span class="header-title">订单金额</span>
                  </div>
                </template>

                <div class="summary-content">
                  <div class="summary-row">
                    <span class="label">商品总价</span>
                    <span class="value">¥{{ order.totalAmount?.toFixed(2) }}</span>
                  </div>
                  <div class="summary-row">
                    <span class="label">配送费</span>
                    <span class="value">¥{{ order.deliveryFee?.toFixed(2) || '0.00' }}</span>
                  </div>
                  <div v-if="discount > 0" class="summary-row">
                    <span class="label">优惠金额</span>
                    <span class="value discount">-¥{{ discount.toFixed(2) }}</span>
                  </div>
                  <el-divider />
                  <div class="summary-row total">
                    <span class="label">实付金额</span>
                    <span class="value total-price">
                      <span class="symbol">¥</span>
                      <span class="amount">{{ order.payAmount?.toFixed(2) }}</span>
                    </span>
                  </div>
                </div>

                <!-- 时间线 -->
                <div v-if="showTimeline" class="order-timeline">
                  <el-divider />
                  <div class="timeline-title">订单状态</div>
                  <el-timeline>
                    <el-timeline-item
                      v-if="order.createTime"
                      type="primary"
                      :icon="CircleCheck"
                    >
                      <div class="timeline-content">
                        <div class="timeline-label">订单创建</div>
                        <div class="timeline-time">{{ formatTime(order.createTime) }}</div>
                      </div>
                    </el-timeline-item>
                    <el-timeline-item
                      v-if="order.payTime"
                      type="success"
                      :icon="CircleCheck"
                    >
                      <div class="timeline-content">
                        <div class="timeline-label">支付成功</div>
                        <div class="timeline-time">{{ formatTime(order.payTime) }}</div>
                      </div>
                    </el-timeline-item>
                    <el-timeline-item
                      v-if="order.deliveryTime"
                      type="warning"
                      :icon="CircleCheck"
                    >
                      <div class="timeline-content">
                        <div class="timeline-label">开始配送</div>
                        <div class="timeline-time">{{ formatTime(order.deliveryTime) }}</div>
                      </div>
                    </el-timeline-item>
                    <el-timeline-item
                      v-if="order.finishTime"
                      type="info"
                      :icon="CircleCheck"
                    >
                      <div class="timeline-content">
                        <div class="timeline-label">订单完成</div>
                        <div class="timeline-time">{{ formatTime(order.finishTime) }}</div>
                      </div>
                    </el-timeline-item>
                  </el-timeline>
                </div>
              </el-card>
            </div>
          </div>
        </div>
      </div>
    </template>

    <!-- 错误状态 -->
    <div v-else class="error-wrapper">
      <el-empty description="订单不存在">
        <el-button type="primary" @click="goBack">返回订单列表</el-button>
      </el-empty>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  ArrowLeft,
  Shop,
  ShoppingBag,
  Location,
  Document,
  Clock,
  Check,
  Bicycle,
  CircleCheck,
  CircleClose
} from '@element-plus/icons-vue'
import http from '../utils/http.js'
import { useUserStore } from '../stores/user.js'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

// 加载状态
const loading = ref(false)

// 订单数据
const order = ref(null)

// 优惠金额（如果有）
const discount = ref(0)

// 状态映射
const statusMap = {
  1: { text: '待付款', desc: '请在30分钟内完成支付，超时订单将自动取消', icon: Clock, type: 'warning' },
  2: { text: '待接单', desc: '商家正在准备您的订单，请耐心等待', icon: Check, type: 'primary' },
  3: { text: '配送中', desc: '订单正在配送中，请保持电话畅通', icon: Bicycle, type: 'success' },
  4: { text: '已完成', desc: '订单已完成，感谢您的光临', icon: CircleCheck, type: 'info' },
  5: { text: '已取消', desc: '订单已取消', icon: CircleClose, type: 'danger' }
}

// 计算商品总数
const totalItems = computed(() => {
  if (!order.value?.orderDetails) return 0
  return order.value.orderDetails.reduce((sum, item) => sum + item.quantity, 0)
})

// 是否显示时间线
const showTimeline = computed(() => {
  return order.value?.payTime || order.value?.deliveryTime || order.value?.finishTime
})

// 获取状态文本
const getStatusText = (status) => {
  return statusMap[status]?.text || '未知状态'
}

// 获取状态描述
const getStatusDesc = (status) => {
  return statusMap[status]?.desc || ''
}

// 获取状态图标
const getStatusIcon = (status) => {
  return statusMap[status]?.icon || Clock
}

// 获取状态样式类
const getStatusClass = (status) => {
  return statusMap[status]?.type || ''
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

// 获取商品图片
const getProductImage = (imageUrl) => {
  if (!imageUrl) {
    return 'https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=100&h=100&fit=crop'
  }
  if (imageUrl.startsWith('http')) {
    return imageUrl
  }
  return imageUrl
}

// 图片加载失败处理
const handleImageError = (e) => {
  e.target.src = 'https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=100&h=100&fit=crop'
}

// 获取订单详情
const fetchOrderDetail = async () => {
  const orderId = route.params.id
  if (!orderId) {
    ElMessage.error('订单ID不存在')
    return
  }

  loading.value = true
  try {
    const res = await http.get(`/orders/selectOrdersById/${orderId}`)
    if (res.code === 200 && res.data) {
      order.value = res.data
      // 计算优惠金额
      discount.value = (order.value.totalAmount || 0) + (order.value.deliveryFee || 0) - (order.value.payAmount || 0)
    } else {
      ElMessage.error(res.msg || '获取订单详情失败')
    }
  } catch (error) {
    console.error('获取订单详情失败:', error)
    ElMessage.error('获取订单详情失败')
  } finally {
    loading.value = false
  }
}

// 返回订单列表
const goBack = () => {
  router.push('/orders')
}

// 支付订单
const payOrder = async () => {
  try {
    await ElMessageBox.confirm(
      `确认支付订单，金额 ¥${order.value.payAmount?.toFixed(2)}？`,
      '确认支付',
      {
        confirmButtonText: '确认支付',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const res = await http.put('/orders/updateOrdersById', {
      id: order.value.id,
      status: 2, // 待接单
      payTime: new Date().toISOString()
    })

    if (res.code === 200) {
      ElMessage.success('支付成功')
      fetchOrderDetail()
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
const cancelOrder = async () => {
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
      id: order.value.id,
      status: 5 // 已取消
    })

    if (res.code === 200) {
      ElMessage.success('订单已取消')
      fetchOrderDetail()
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
const confirmReceive = async () => {
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
      id: order.value.id,
      status: 4, // 已完成
      finishTime: new Date().toISOString()
    })

    if (res.code === 200) {
      ElMessage.success('确认收货成功')
      fetchOrderDetail()
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

// 再次购买
const reorder = () => {
  router.push(`/shop/${order.value.shopId}`)
}

onMounted(() => {
  // 检查登录状态
  if (!userStore.isLogin) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  fetchOrderDetail()
})
</script>

<style scoped>
.order-detail-container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 40px;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.loading-wrapper {
  padding: 40px;
}

/* 头部 */
.page-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 24px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #1f2937;
  margin: 0;
}

/* 状态卡片 */
.status-card {
  margin-bottom: 24px;
  border-radius: 12px;
}

.status-card :deep(.el-card__body) {
  padding: 24px;
}

.status-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.status-icon {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.status-icon.warning {
  background: #fef3c7;
  color: #f59e0b;
}

.status-icon.primary {
  background: #dbeafe;
  color: #3b82f6;
}

.status-icon.success {
  background: #d1fae5;
  color: #10b981;
}

.status-icon.info {
  background: #f3f4f6;
  color: #6b7280;
}

.status-icon.danger {
  background: #fee2e2;
  color: #ef4444;
}

.status-info {
  flex: 1;
}

.status-text {
  font-size: 20px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 8px 0;
}

.status-desc {
  font-size: 14px;
  color: #6b7280;
  margin: 0;
}

.status-actions {
  display: flex;
  gap: 12px;
}

/* 布局 */
.detail-layout {
  display: grid;
  grid-template-columns: 1fr 380px;
  gap: 24px;
}

/* 卡片样式 */
.section-card {
  margin-bottom: 16px;
  border-radius: 12px;
}

.section-card :deep(.el-card__header) {
  padding: 16px 20px;
  border-bottom: 1px solid #e5e7eb;
}

.section-card :deep(.el-card__body) {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
}

.header-extra {
  font-size: 14px;
  color: #6b7280;
}

/* 商家信息 */
.shop-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.shop-name {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
}

.shop-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  font-size: 14px;
  color: #6b7280;
}

/* 商品列表 */
.product-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.product-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 12px 0;
  border-bottom: 1px solid #f3f4f6;
}

.product-item:last-child {
  border-bottom: none;
}

.product-image {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  overflow: hidden;
  flex-shrink: 0;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.product-name {
  font-size: 14px;
  font-weight: 500;
  color: #1f2937;
}

.product-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 14px;
}

.product-price {
  color: #ef4444;
}

.product-quantity {
  color: #6b7280;
}

.product-total {
  text-align: right;
}

.product-total .label {
  font-size: 14px;
  color: #6b7280;
}

.product-total .amount {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
}

/* 收货地址 */
.address-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.address-row {
  display: flex;
  font-size: 14px;
}

.address-row .label {
  width: 80px;
  color: #6b7280;
  flex-shrink: 0;
}

.address-row .value {
  color: #1f2937;
  flex: 1;
}

/* 备注 */
.remark-content {
  font-size: 14px;
  color: #1f2937;
  line-height: 1.6;
}

/* 侧边栏结算 */
.summary-card :deep(.el-card__header) {
  padding: 16px 20px;
  border-bottom: 1px solid #e5e7eb;
}

.summary-card :deep(.el-card__body) {
  padding: 20px;
}

.summary-content {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.summary-row .label {
  font-size: 14px;
  color: #6b7280;
}

.summary-row .value {
  font-size: 14px;
  color: #1f2937;
}

.summary-row .value.discount {
  color: #10b981;
}

.summary-row.total {
  margin-top: 4px;
}

.summary-row.total .label {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
}

.summary-row.total .value.total-price {
  color: #ef4444;
}

.summary-row.total .value.total-price .symbol {
  font-size: 14px;
}

.summary-row.total .value.total-price .amount {
  font-size: 24px;
  font-weight: 700;
}

/* 时间线 */
.order-timeline {
  margin-top: 20px;
}

.timeline-title {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 16px;
}

.timeline-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.timeline-label {
  font-size: 14px;
  color: #1f2937;
}

.timeline-time {
  font-size: 12px;
  color: #9ca3af;
}

/* 粘性定位 */
.is-sticky {
  position: sticky;
  top: 20px;
}

/* 错误状态 */
.error-wrapper {
  padding: 80px 0;
}

/* 响应式设计 */
@media (max-width: 992px) {
  .detail-layout {
    grid-template-columns: 1fr;
  }

  .summary-card.is-sticky {
    position: static;
  }

  .status-content {
    flex-direction: column;
    text-align: center;
  }

  .status-actions {
    width: 100%;
    justify-content: center;
  }
}

@media (max-width: 576px) {
  .container {
    padding: 12px;
  }

  .page-title {
    font-size: 20px;
  }

  .product-item {
    flex-wrap: wrap;
  }

  .product-total {
    width: 100%;
    text-align: left;
    margin-top: 8px;
  }

  .shop-meta {
    flex-direction: column;
    gap: 4px;
  }
}
</style>
