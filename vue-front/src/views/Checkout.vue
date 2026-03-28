<template>
  <div class="checkout-container">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-wrapper">
      <el-skeleton :rows="10" animated />
    </div>

    <template v-else-if="orderData">
      <div class="container">
        <div class="checkout-header">
          <el-button link @click="goBack">
            <el-icon><ArrowLeft /></el-icon>
            返回购物车
          </el-button>
          <h1 class="checkout-title">确认订单</h1>
        </div>

        <div class="checkout-layout">
          <!-- 左侧：订单信息 -->
          <div class="checkout-main">
            <!-- 商家信息卡片 -->
            <el-card class="section-card" shadow="never">
              <div class="shop-info">
                <el-icon :size="24" color="#2563EB"><Shop /></el-icon>
                <div class="shop-detail">
                  <h3>{{ orderData.shopName }}</h3>
                  <p class="shop-meta">预计30分钟送达</p>
                </div>
              </div>
            </el-card>

            <!-- 收货地址 -->
            <el-card class="section-card" shadow="never">
              <template #header>
                <div class="card-header">
                  <span class="header-title">
                    <el-icon><Location /></el-icon>
                    收货地址
                  </span>
                </div>
              </template>
              <el-form :model="form" :rules="rules" ref="formRef" label-position="top">
                <div class="address-form">
                  <el-form-item label="收货人姓名" prop="receiverName">
                    <el-input
                      v-model="form.receiverName"
                      placeholder="请输入收货人姓名"
                      size="large"
                      :prefix-icon="User"
                    />
                  </el-form-item>
                  <el-form-item label="联系电话" prop="receiverPhone">
                    <el-input
                      v-model="form.receiverPhone"
                      placeholder="请输入联系电话"
                      size="large"
                      :prefix-icon="Phone"
                    />
                  </el-form-item>
                </div>
                <el-form-item label="详细地址" prop="receiverAddress">
                  <el-input
                    v-model="form.receiverAddress"
                    type="textarea"
                    :rows="3"
                    placeholder="请输入详细地址（街道、门牌号等）"
                    size="large"
                  />
                </el-form-item>
              </el-form>
            </el-card>

            <!-- 订单商品 -->
            <el-card class="section-card" shadow="never">
              <template #header>
                <div class="card-header">
                  <span class="header-title">
                    <el-icon><ShoppingBag /></el-icon>
                    订单商品
                  </span>
                  <span class="header-extra">共 {{ totalItems }} 件</span>
                </div>
              </template>
              <div class="order-items">
                <div
                  v-for="item in orderData.items"
                  :key="item.productId"
                  class="order-item"
                >
                  <div class="item-main">
                    <span class="item-name">{{ item.name }}</span>
                    <span class="item-quantity">x{{ item.quantity }}</span>
                  </div>
                  <div class="item-price">
                    <span class="price-symbol">¥</span>
                    <span class="price-value">{{ (item.price * item.quantity).toFixed(2) }}</span>
                  </div>
                </div>
              </div>
            </el-card>

            <!-- 订单备注 -->
            <el-card class="section-card" shadow="never">
              <template #header>
                <div class="card-header">
                  <span class="header-title">
                    <el-icon><Document /></el-icon>
                    订单备注
                  </span>
                </div>
              </template>
              <el-input
                v-model="form.remark"
                type="textarea"
                :rows="3"
                placeholder="请输入订单备注（如：少辣、不要葱等）"
                maxlength="200"
                show-word-limit
              />
            </el-card>
          </div>

          <!-- 右侧：结算信息 -->
          <div class="checkout-sidebar">
            <div class="summary-card" :class="{ 'is-sticky': true }">
              <el-card shadow="never">
                <template #header>
                  <div class="card-header">
                    <span class="header-title">结算明细</span>
                  </div>
                </template>

                <div class="summary-content">
                  <div class="summary-row">
                    <span class="label">商品总价</span>
                    <span class="value">
                      <span class="price-symbol">¥</span>
                      <span class="price-value">{{ orderData.total.toFixed(2) }}</span>
                    </span>
                  </div>
                  <div class="summary-row">
                    <span class="label">配送费</span>
                    <span class="value">
                      <span class="price-symbol">¥</span>
                      <span class="price-value">{{ deliveryFee.toFixed(2) }}</span>
                    </span>
                  </div>
                  <div class="summary-row" v-if="discount > 0">
                    <span class="label">优惠金额</span>
                    <span class="value discount">
                      <span class="price-symbol">-¥</span>
                      <span class="price-value">{{ discount.toFixed(2) }}</span>
                    </span>
                  </div>
                  <el-divider />
                  <div class="summary-row total">
                    <span class="label">实付金额</span>
                    <span class="value total-price">
                      <span class="price-symbol">¥</span>
                      <span class="price-value">{{ payAmount.toFixed(2) }}</span>
                    </span>
                  </div>
                </div>

                <div class="summary-footer">
                  <el-button
                    type="primary"
                    size="large"
                    class="submit-btn"
                    :loading="submitting"
                    @click="submitOrder"
                  >
                    <template v-if="submitting">
                      提交中...
                    </template>
                    <template v-else>
                      提交订单
                    </template>
                  </el-button>
                  <p class="submit-tip">
                    <el-icon><InfoFilled /></el-icon>
                    点击提交订单即表示您同意我们的服务条款
                  </p>
                </div>
              </el-card>
            </div>
          </div>
        </div>
      </div>
    </template>

    <!-- 错误状态 -->
    <div v-else class="error-wrapper">
      <el-empty description="订单信息不存在或已过期">
        <el-button type="primary" @click="goToShops">去选购商品</el-button>
      </el-empty>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  ArrowLeft,
  Shop,
  Location,
  User,
  Phone,
  ShoppingBag,
  Document,
  InfoFilled
} from '@element-plus/icons-vue'
import http from '../utils/http.js'
import { useUserStore } from '../stores/user.js'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)

// 加载状态
const loading = ref(false)
const submitting = ref(false)

// 订单数据（从localStorage获取）
const orderData = ref(null)

// 配送费
const deliveryFee = ref(0)

// 优惠金额
const discount = ref(0)

// 表单数据
const form = reactive({
  receiverName: '',
  receiverPhone: '',
  receiverAddress: '',
  remark: ''
})

// 表单验证规则
const rules = {
  receiverName: [
    { required: true, message: '请输入收货人姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度在2-20个字符之间', trigger: 'blur' }
  ],
  receiverPhone: [
    { required: true, message: '请输入联系电话', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  receiverAddress: [
    { required: true, message: '请输入详细地址', trigger: 'blur' },
    { min: 5, max: 200, message: '地址长度在5-200个字符之间', trigger: 'blur' }
  ]
}

// 计算商品总数
const totalItems = computed(() => {
  if (!orderData.value?.items) return 0
  return orderData.value.items.reduce((sum, item) => sum + item.quantity, 0)
})

// 计算实付金额
const payAmount = computed(() => {
  if (!orderData.value) return 0
  return orderData.value.total + deliveryFee.value - discount.value
})

// 加载订单数据
const loadOrderData = () => {
  const savedOrder = localStorage.getItem('currentOrder')
  if (savedOrder) {
    try {
      orderData.value = JSON.parse(savedOrder)
    } catch (e) {
      console.error('解析订单数据失败:', e)
      orderData.value = null
    }
  }
}

// 预填充用户信息
const prefillUserInfo = () => {
  if (userStore.userInfo) {
    form.receiverName = userStore.userInfo.name || ''
    // 如果用户有其他信息可以在这里填充
  }
}

// 返回购物车
const goBack = () => {
  if (orderData.value?.shopId) {
    router.push(`/shop/${orderData.value.shopId}`)
  } else {
    router.push('/shops')
  }
}

// 去选购商品
const goToShops = () => {
  router.push('/shops')
}

// 生成订单编号
const generateOrderNo = () => {
  const timestamp = Date.now().toString(36).toUpperCase()
  const random = Math.random().toString(36).substring(2, 6).toUpperCase()
  return `ORD${timestamp}${random}`
}

// 提交订单
const submitOrder = async () => {
  if (!userStore.isLogin) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  if (!orderData.value) {
    ElMessage.error('订单信息不存在')
    return
  }

  // 表单验证
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) {
    ElMessage.error('请填写完整的收货信息')
    return
  }

  submitting.value = true

  try {
    // 构建订单数据
    const orderPayload = {
      orderNo: generateOrderNo(),
      userId: userStore.userInfo.id,
      shopId: orderData.value.shopId,
      totalAmount: orderData.value.total,
      payAmount: payAmount.value,
      deliveryFee: deliveryFee.value,
      remark: form.remark,
      status: 1, // 待付款
      receiverName: form.receiverName,
      receiverPhone: form.receiverPhone,
      receiverAddress: form.receiverAddress,
      // 订单明细
      orderDetails: orderData.value.items.map(item => ({
        productId: item.productId,
        productName: item.name,
        price: item.price,
        quantity: item.quantity,
        totalFee: item.price * item.quantity
      }))
    }

    // 调用创建订单API
    const res = await http.post('/orders/addOrders', orderPayload)

    if (res.code === 200) {
      ElMessage.success('订单创建成功')
      // 清除购物车数据
      localStorage.removeItem('currentOrder')
      // 跳转到订单成功页面或订单列表
      router.push('/orders')
    } else {
      ElMessage.error(res.msg || '订单创建失败')
    }
  } catch (error) {
    console.error('创建订单失败:', error)
    ElMessage.error('订单创建失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  // 检查登录状态
  if (!userStore.isLogin) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }

  loadOrderData()
  prefillUserInfo()
})
</script>

<style scoped>
.checkout-container {
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
.checkout-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 24px;
}

.checkout-title {
  font-size: 24px;
  font-weight: 600;
  color: #1f2937;
  margin: 0;
}

/* 布局 */
.checkout-layout {
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
  align-items: center;
  gap: 16px;
}

.shop-detail h3 {
  margin: 0 0 4px 0;
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
}

.shop-meta {
  margin: 0;
  font-size: 14px;
  color: #6b7280;
}

/* 地址表单 */
.address-form {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

/* 订单商品 */
.order-items {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.order-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 0;
  border-bottom: 1px solid #f3f4f6;
}

.order-item:last-child {
  border-bottom: none;
}

.item-main {
  display: flex;
  align-items: center;
  gap: 12px;
  flex: 1;
}

.item-name {
  font-size: 14px;
  color: #1f2937;
}

.item-quantity {
  font-size: 14px;
  color: #6b7280;
}

.item-price {
  color: #ef4444;
  font-weight: 600;
}

.price-symbol {
  font-size: 12px;
}

.price-value {
  font-size: 16px;
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

.summary-row.total .value.total-price .price-value {
  font-size: 24px;
  font-weight: 700;
}

/* 提交按钮 */
.summary-footer {
  margin-top: 20px;
}

.submit-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #DC2626 0%, #EF4444 100%);
  border: none;
  border-radius: 8px;
}

.submit-btn:hover {
  background: linear-gradient(135deg, #B91C1C 0%, #DC2626 100%);
}

.submit-tip {
  margin: 12px 0 0 0;
  font-size: 12px;
  color: #9ca3af;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
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
  .checkout-layout {
    grid-template-columns: 1fr;
  }

  .summary-card.is-sticky {
    position: static;
  }

  .address-form {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 576px) {
  .container {
    padding: 12px;
  }

  .checkout-title {
    font-size: 20px;
  }

  .section-card :deep(.el-card__body) {
    padding: 16px;
  }

  .item-main {
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;
  }
}
</style>
