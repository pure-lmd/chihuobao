<template>
  <div class="shop-detail-container">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-wrapper">
      <el-skeleton :rows="10" animated />
    </div>

    <template v-else-if="shopInfo">
      <!-- 商家头部信息 -->
      <section class="shop-header">
        <div class="shop-cover">
          <img :src="getShopImage(shopInfo.coverUrl)" :alt="shopInfo.name" @error="handleImageError">
          <div class="cover-overlay"></div>
        </div>
        <div class="shop-header-content">
          <div class="container">
            <div class="shop-basic-info">
              <h1 class="shop-name">{{ shopInfo.name }}</h1>
              <div class="shop-meta">
                <div class="rating-badge">
                  <el-icon color="#F59E0B"><StarFilled /></el-icon>
                  <span class="rating-score">{{ shopInfo.rating?.toFixed(1) || '4.5' }}</span>
                  <span class="rating-text">分</span>
                </div>
                <span class="meta-divider">|</span>
                <span class="monthly-sales">月售 {{ monthlySales }}+</span>
                <span class="meta-divider">|</span>
                <span class="delivery-time">
                  <el-icon><Timer /></el-icon>
                  约30分钟
                </span>
              </div>
              <div class="shop-notice" v-if="shopInfo.description">
                <el-icon><InfoFilled /></el-icon>
                <span>{{ shopInfo.description }}</span>
              </div>
              <div class="shop-delivery-info">
                <el-tag size="small" effect="plain">
                  <el-icon><Wallet /></el-icon>
                  起送 ¥{{ shopInfo.minPrice || 0 }}
                </el-tag>
                <el-tag size="small" effect="plain">
                  <el-icon><Bicycle /></el-icon>
                  配送 ¥{{ shopInfo.deliveryFee || 0 }}
                </el-tag>
                <el-tag size="small" :type="shopInfo.status === 1 ? 'success' : 'info'">
                  {{ shopInfo.status === 1 ? '营业中' : '休息中' }}
                </el-tag>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 主体内容 -->
      <section class="shop-main">
        <div class="container">
          <div class="main-layout">
            <!-- 左侧商品列表 -->
            <div class="product-section">
              <!-- 分类标签 -->
              <div class="category-tabs">
                <el-tabs v-model="activeCategory" type="border-card">
                  <el-tab-pane label="全部商品" name="all">
                    <div class="products-grid">
                      <div
                        v-for="product in productList"
                        :key="product.id"
                        class="product-card"
                        :class="{ 'out-of-stock': product.stock === 0 || product.status === 0 }"
                      >
                        <div class="product-image">
                          <img :src="getProductImage(product.imageUrl)" :alt="product.name" @error="handleImageError">
                          <div class="stock-badge" v-if="product.status === 0">已下架</div>
                          <div class="stock-badge" v-else-if="product.stock === 0">已售罄</div>
                        </div>
                        <div class="product-info">
                          <h3 class="product-name">{{ product.name }}</h3>
                          <p class="product-desc" v-if="product.description">{{ product.description }}</p>
                          <div class="product-footer">
                            <div class="product-price">
                              <span class="price-symbol">¥</span>
                              <span class="price-value">{{ product.price?.toFixed(2) || '0.00' }}</span>
                            </div>
                            <div class="product-actions">
                              <el-button
                                v-if="product.status === 0"
                                type="info"
                                size="small"
                                disabled
                              >
                                已下架
                              </el-button>
                              <el-button
                                v-else-if="getCartQuantity(product.id) === 0"
                                type="primary"
                                size="small"
                                :disabled="product.stock === 0"
                                @click="addToCart(product)"
                              >
                                加入购物车
                              </el-button>
                              <el-input-number
                                v-else
                                v-model="cartItems[product.id].quantity"
                                :min="0"
                                :max="product.stock"
                                size="small"
                                @change="(val) => handleQuantityChange(product, val)"
                              />
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </el-tab-pane>
                  <el-tab-pane
                    v-for="category in productCategories"
                    :key="category.id"
                    :label="category.name"
                    :name="category.id.toString()"
                  >
                    <div class="products-grid">
                      <div
                        v-for="product in getProductsByCategory(category.id)"
                        :key="product.id"
                        class="product-card"
                        :class="{ 'out-of-stock': product.stock === 0 || product.status === 0 }"
                      >
                        <div class="product-image">
                          <img :src="getProductImage(product.imageUrl)" :alt="product.name" @error="handleImageError">
                          <div class="stock-badge" v-if="product.status === 0">已下架</div>
                          <div class="stock-badge" v-else-if="product.stock === 0">已售罄</div>
                        </div>
                        <div class="product-info">
                          <h3 class="product-name">{{ product.name }}</h3>
                          <p class="product-desc" v-if="product.description">{{ product.description }}</p>
                          <div class="product-footer">
                            <div class="product-price">
                              <span class="price-symbol">¥</span>
                              <span class="price-value">{{ product.price?.toFixed(2) || '0.00' }}</span>
                            </div>
                            <div class="product-actions">
                              <el-button
                                v-if="product.status === 0"
                                type="info"
                                size="small"
                                disabled
                              >
                                已下架
                              </el-button>
                              <el-button
                                v-else-if="getCartQuantity(product.id) === 0"
                                type="primary"
                                size="small"
                                :disabled="product.stock === 0"
                                @click="addToCart(product)"
                              >
                                加入购物车
                              </el-button>
                              <el-input-number
                                v-else
                                v-model="cartItems[product.id].quantity"
                                :min="0"
                                :max="product.stock"
                                size="small"
                                @change="(val) => handleQuantityChange(product, val)"
                              />
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </el-tab-pane>
                </el-tabs>
              </div>
            </div>

            <!-- 右侧购物车 -->
            <div class="cart-section">
              <div class="cart-card" :class="{ 'is-sticky': cartTotal > 0 }">
                <div class="cart-header">
                  <h3>
                    <el-icon><ShoppingCart /></el-icon>
                    购物车
                    <el-tag type="primary" size="small" v-if="cartItemCount > 0">{{ cartItemCount }}</el-tag>
                  </h3>
                  <el-button
                    v-if="cartItemCount > 0"
                    type="danger"
                    link
                    size="small"
                    @click="clearCart"
                  >
                    清空
                  </el-button>
                </div>

                <div class="cart-content">
                  <div v-if="cartItemCount === 0" class="cart-empty">
                    <el-icon :size="48" color="#ddd"><ShoppingCart /></el-icon>
                    <p>购物车是空的</p>
                    <p class="cart-tip">快去选购心仪的商品吧</p>
                  </div>
                  <div v-else class="cart-items">
                    <div
                      v-for="item in cartList"
                      :key="item.productId"
                      class="cart-item"
                    >
                      <div class="item-info">
                        <span class="item-name">{{ item.name }}</span>
                        <span class="item-price">¥{{ (item.price * item.quantity).toFixed(2) }}</span>
                      </div>
                      <div class="item-quantity">
                        <el-input-number
                          v-model="item.quantity"
                          :min="0"
                          :max="item.stock"
                          size="small"
                          @change="(val) => updateCartItem(item.productId, val)"
                        />
                      </div>
                    </div>
                  </div>
                </div>

                <div class="cart-footer" v-if="cartItemCount > 0">
                  <div class="cart-total">
                    <span class="total-label">合计：</span>
                    <span class="total-price">
                      <span class="price-symbol">¥</span>
                      <span class="price-value">{{ cartTotal.toFixed(2) }}</span>
                    </span>
                  </div>
                  <el-button
                    type="primary"
                    size="large"
                    class="checkout-btn"
                    :disabled="cartTotal < (shopInfo.minPrice || 0)"
                    @click="goToCheckout"
                  >
                    <template v-if="cartTotal < (shopInfo.minPrice || 0)">
                      差 ¥{{ ((shopInfo.minPrice || 0) - cartTotal).toFixed(2) }} 起送
                    </template>
                    <template v-else>
                      去结算
                    </template>
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </template>

    <!-- 错误状态 -->
    <div v-else class="error-wrapper">
      <el-empty description="商家不存在或已下线">
        <el-button type="primary" @click="goBack">返回商家列表</el-button>
      </el-empty>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  StarFilled,
  Timer,
  InfoFilled,
  Wallet,
  Bicycle,
  ShoppingCart
} from '@element-plus/icons-vue'
import http from '../utils/http.js'

const router = useRouter()
const route = useRoute()

// 加载状态
const loading = ref(false)

// 商家信息
const shopInfo = ref(null)

// 商品分类
const productCategories = ref([])

// 商品列表
const productList = ref([])

// 当前选中的分类
const activeCategory = ref('all')

// 月售数量（模拟）
const monthlySales = ref(0)

// 购物车数据
const cartItems = reactive({})

// 计算购物车列表
const cartList = computed(() => {
  return Object.values(cartItems).filter(item => item.quantity > 0)
})

// 计算购物车商品总数
const cartItemCount = computed(() => {
  return cartList.value.reduce((sum, item) => sum + item.quantity, 0)
})

// 计算购物车总价
const cartTotal = computed(() => {
  return cartList.value.reduce((sum, item) => sum + (item.price * item.quantity), 0)
})

// 获取商家详情
const fetchShopDetail = async () => {
  const shopId = route.params.id
  if (!shopId) {
    ElMessage.error('商家ID不存在')
    return
  }

  loading.value = true
  try {
    const res = await http.get(`/shop/selectShopById/${shopId}`)
    if (res.code === 200 && res.data) {
      shopInfo.value = res.data
      monthlySales.value = 100 + (parseInt(shopId) * 50)
      // 获取商品列表
      await fetchProducts(shopId)
    } else {
      ElMessage.error(res.msg || '获取商家详情失败')
    }
  } catch (error) {
    console.error('获取商家详情失败:', error)
    ElMessage.error('获取商家详情失败')
    // 使用模拟数据
    useMockData(shopId)
  } finally {
    loading.value = false
  }
}

// 获取商品列表
const fetchProducts = async (shopId) => {
  try {
    // 这里调用商品API，假设有根据商家ID查询商品的接口
    const res = await http.get('/product/selectProductAll', { params: { shopId } })
    if (res.code === 200) {
      productList.value = res.data || []
      // 提取分类
      extractCategories(res.data || [])
    }
  } catch (error) {
    console.error('获取商品列表失败:', error)
    useMockProducts(shopId)
  }
}

// 提取商品分类
const extractCategories = (products) => {
  const categoryMap = new Map()
  products.forEach(product => {
    if (product.categoryId && !categoryMap.has(product.categoryId)) {
      categoryMap.set(product.categoryId, {
        id: product.categoryId,
        name: product.categoryName || `分类${product.categoryId}`
      })
    }
  })
  productCategories.value = Array.from(categoryMap.values())
}

// 根据分类获取商品
const getProductsByCategory = (categoryId) => {
  return productList.value.filter(p => p.categoryId === categoryId)
}

// 获取购物车中商品数量
const getCartQuantity = (productId) => {
  return cartItems[productId]?.quantity || 0
}

// 添加到购物车
const addToCart = (product) => {
  if (!cartItems[product.id]) {
    cartItems[product.id] = {
      productId: product.id,
      name: product.name,
      price: product.price,
      stock: product.stock,
      quantity: 0
    }
  }
  cartItems[product.id].quantity++
  ElMessage.success(`已将 ${product.name} 加入购物车`)
}

// 处理数量变化
const handleQuantityChange = (product, val) => {
  if (val === 0) {
    delete cartItems[product.id]
  }
}

// 更新购物车商品数量
const updateCartItem = (productId, quantity) => {
  if (quantity === 0) {
    delete cartItems[productId]
  }
}

// 清空购物车
const clearCart = () => {
  ElMessageBox.confirm('确定要清空购物车吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    Object.keys(cartItems).forEach(key => delete cartItems[key])
    ElMessage.success('购物车已清空')
  }).catch(() => {})
}

// 去结算
const goToCheckout = () => {
  if (cartTotal.value < (shopInfo.value.minPrice || 0)) {
    ElMessage.warning('未达到起送价')
    return
  }
  // 保存购物车数据到本地存储
  const orderData = {
    shopId: shopInfo.value.id,
    shopName: shopInfo.value.name,
    items: cartList.value,
    total: cartTotal.value
  }
  localStorage.setItem('currentOrder', JSON.stringify(orderData))
  router.push('/checkout')
}

// 返回商家列表
const goBack = () => {
  router.push('/shops')
}

// 获取商家图片
const getShopImage = (coverUrl) => {
  if (!coverUrl) {
    return 'https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=800&h=400&fit=crop'
  }
  if (coverUrl.startsWith('http')) {
    return coverUrl
  }
  // 使用相对路径，让 vite 代理处理
  return coverUrl.startsWith('/') ? coverUrl : '/' + coverUrl
}

// 获取商品图片
const getProductImage = (imageUrl) => {
  if (!imageUrl) {
    return 'https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=300&h=200&fit=crop'
  }
  if (imageUrl.startsWith('http')) {
    return imageUrl
  }
  return imageUrl
}

// 图片加载失败处理
const handleImageError = (e) => {
  e.target.src = 'https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=300&h=200&fit=crop'
}

// 模拟数据
const useMockData = (shopId) => {
  shopInfo.value = {
    id: shopId,
    name: '麦当劳',
    coverUrl: 'https://images.unsplash.com/photo-1571091718767-18b5b1457add?w=800&h=400&fit=crop',
    rating: 4.8,
    minPrice: 20,
    deliveryFee: 0,
    status: 1,
    description: '汉堡薯条，快乐时光。麦当劳致力于为您提供美味的快餐体验。'
  }
  monthlySales.value = 999
  useMockProducts(shopId)
}

// 模拟商品数据
const useMockProducts = (shopId) => {
  productCategories.value = [
    { id: 1, name: '汉堡' },
    { id: 2, name: '小食' },
    { id: 3, name: '饮品' }
  ]
  productList.value = [
    {
      id: 1,
      name: '巨无霸汉堡',
      description: '双层牛肉饼，特制酱料',
      price: 25.00,
      stock: 100,
      categoryId: 1,
      categoryName: '汉堡',
      imageUrl: 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=300&h=200&fit=crop'
    },
    {
      id: 2,
      name: '麦辣鸡腿堡',
      description: '酥脆香辣，口感十足',
      price: 22.00,
      stock: 80,
      categoryId: 1,
      categoryName: '汉堡',
      imageUrl: 'https://images.unsplash.com/photo-1513185158878-8d8c2a2a3da3?w=300&h=200&fit=crop'
    },
    {
      id: 3,
      name: '薯条（中）',
      description: '金黄酥脆，经典美味',
      price: 12.00,
      stock: 200,
      categoryId: 2,
      categoryName: '小食',
      imageUrl: 'https://images.unsplash.com/photo-1630384060421-cb20d0e0649d?w=300&h=200&fit=crop'
    },
    {
      id: 4,
      name: '麦乐鸡（5块）',
      description: '外酥里嫩，搭配蘸酱',
      price: 15.00,
      stock: 150,
      categoryId: 2,
      categoryName: '小食',
      imageUrl: 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?w=300&h=200&fit=crop'
    },
    {
      id: 5,
      name: '可乐（中）',
      description: '冰爽畅饮',
      price: 10.00,
      stock: 300,
      categoryId: 3,
      categoryName: '饮品',
      imageUrl: 'https://images.unsplash.com/photo-1622483767028-3f66f32aef97?w=300&h=200&fit=crop'
    },
    {
      id: 6,
      name: '雪碧（中）',
      description: '清爽柠檬味',
      price: 10.00,
      stock: 0,
      categoryId: 3,
      categoryName: '饮品',
      imageUrl: 'https://images.unsplash.com/photo-1625772299848-391b6a87d7b3?w=300&h=200&fit=crop'
    }
  ]
}

onMounted(() => {
  fetchShopDetail()
})
</script>

<style scoped>
.shop-detail-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.loading-wrapper {
  padding: 40px;
}

/* 商家头部 */
.shop-header {
  position: relative;
  background: white;
}

.shop-cover {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.shop-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.cover-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 100px;
  background: linear-gradient(transparent, rgba(0,0,0,0.5));
}

.shop-header-content {
  padding: 20px 0;
  background: white;
  border-bottom: 1px solid #e5e7eb;
}

.shop-basic-info {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.shop-name {
  font-size: 28px;
  font-weight: 700;
  color: #1f2937;
  margin: 0;
}

.shop-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  color: #6b7280;
  font-size: 14px;
}

.rating-badge {
  display: flex;
  align-items: center;
  gap: 4px;
}

.rating-score {
  font-size: 18px;
  font-weight: 700;
  color: #f59e0b;
}

.rating-text {
  font-size: 14px;
  color: #6b7280;
}

.meta-divider {
  color: #d1d5db;
}

.delivery-time {
  display: flex;
  align-items: center;
  gap: 4px;
}

.shop-notice {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 16px;
  background: #fef3c7;
  border-radius: 8px;
  color: #92400e;
  font-size: 14px;
}

.shop-delivery-info {
  display: flex;
  gap: 12px;
}

/* 主体内容 */
.shop-main {
  padding: 20px 0;
}

.main-layout {
  display: grid;
  grid-template-columns: 1fr 320px;
  gap: 20px;
}

/* 商品区域 */
.product-section {
  background: white;
  border-radius: 12px;
  overflow: hidden;
}

.category-tabs :deep(.el-tabs__header) {
  margin-bottom: 0;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  padding: 16px;
}

.product-card {
  display: flex;
  gap: 12px;
  padding: 12px;
  border-radius: 12px;
  border: 1px solid #e5e7eb;
  transition: box-shadow 0.2s;
}

.product-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.product-card.out-of-stock {
  opacity: 0.6;
}

.product-image {
  position: relative;
  width: 100px;
  height: 100px;
  border-radius: 8px;
  overflow: hidden;
  flex-shrink: 0;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.stock-badge {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0.6);
  color: white;
  font-size: 14px;
  font-weight: 600;
}

.product-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.product-name {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 4px 0;
}

.product-desc {
  font-size: 12px;
  color: #6b7280;
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.product-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 8px;
}

.product-price {
  color: #ef4444;
}

.price-symbol {
  font-size: 12px;
}

.price-value {
  font-size: 20px;
  font-weight: 700;
}

/* 购物车区域 */
.cart-section {
  position: relative;
}

.cart-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.cart-card.is-sticky {
  position: sticky;
  top: 80px;
}

.cart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #e5e7eb;
}

.cart-header h3 {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 0;
  font-size: 16px;
  font-weight: 600;
}

.cart-content {
  padding: 16px;
  min-height: 200px;
  max-height: 400px;
  overflow-y: auto;
}

.cart-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 200px;
  color: #9ca3af;
}

.cart-empty p {
  margin: 8px 0 0 0;
}

.cart-tip {
  font-size: 12px;
}

.cart-items {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.cart-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 12px;
  border-bottom: 1px solid #f3f4f6;
}

.cart-item:last-child {
  border-bottom: none;
  padding-bottom: 0;
}

.item-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.item-name {
  font-size: 14px;
  color: #1f2937;
}

.item-price {
  font-size: 14px;
  color: #ef4444;
  font-weight: 600;
}

.cart-footer {
  padding: 16px;
  border-top: 1px solid #e5e7eb;
  background: #f9fafb;
}

.cart-total {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.total-label {
  font-size: 14px;
  color: #6b7280;
}

.total-price {
  color: #ef4444;
}

.total-price .price-value {
  font-size: 24px;
  font-weight: 700;
}

.checkout-btn {
  width: 100%;
  background: linear-gradient(135deg, #DC2626 0%, #EF4444 100%);
  border: none;
}

.checkout-btn:disabled {
  background: #9ca3af;
}

/* 错误状态 */
.error-wrapper {
  padding: 80px 0;
}

/* 响应式设计 */
@media (max-width: 992px) {
  .main-layout {
    grid-template-columns: 1fr;
  }

  .cart-section {
    order: -1;
  }

  .cart-card.is-sticky {
    position: static;
  }

  .products-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 576px) {
  .shop-name {
    font-size: 22px;
  }

  .shop-meta {
    flex-wrap: wrap;
  }

  .product-card {
    flex-direction: column;
  }

  .product-image {
    width: 100%;
    height: 150px;
  }
}
</style>
