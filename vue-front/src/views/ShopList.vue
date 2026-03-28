<template>
  <div class="shop-list-container">
    <!-- 页面头部 -->
    <section class="page-header">
      <div class="container">
        <h1 class="page-title">发现优质商家</h1>
        <p class="page-subtitle">数千家精选餐厅，满足您的味蕾需求</p>
      </div>
    </section>

    <!-- 筛选和搜索区域 -->
    <section class="filter-section">
      <div class="container">
        <div class="filter-bar">
          <!-- 分类筛选 -->
          <div class="filter-group">
            <span class="filter-label">分类：</span>
            <el-radio-group v-model="filterParams.category" size="default" @change="handleFilterChange">
              <el-radio-button label="">全部</el-radio-button>
              <el-radio-button v-for="cat in categories" :key="cat.id" :label="cat.name">
                {{ cat.name }}
              </el-radio-button>
            </el-radio-group>
          </div>

          <!-- 排序方式 -->
          <div class="filter-group">
            <span class="filter-label">排序：</span>
            <el-select v-model="filterParams.sort" placeholder="默认排序" @change="handleFilterChange">
              <el-option label="综合排序" value="" />
              <el-option label="评分最高" value="rating_desc" />
              <el-option label="起送价最低" value="minPrice_asc" />
              <el-option label="配送费最低" value="deliveryFee_asc" />
            </el-select>
          </div>

          <!-- 营业状态 -->
          <div class="filter-group">
            <el-checkbox v-model="filterParams.onlyOpen" @change="handleFilterChange">
              仅看营业中
            </el-checkbox>
          </div>

          <!-- 搜索框 -->
          <div class="filter-search">
            <el-input
              v-model="filterParams.keyword"
              placeholder="搜索商家名称..."
              clearable
              @keyup.enter="handleFilterChange"
              @clear="handleFilterChange"
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
              <template #append>
                <el-button @click="handleFilterChange">
                  <el-icon><Search /></el-icon>
                </el-button>
              </template>
            </el-input>
          </div>
        </div>
      </div>
    </section>

    <!-- 商家列表 -->
    <section class="shops-section">
      <div class="container">
        <!-- 加载状态 -->
        <div v-if="loading" class="loading-wrapper">
          <el-skeleton :rows="6" animated />
        </div>

        <!-- 商家网格 -->
        <div v-else-if="shopList.length > 0" class="shops-grid">
          <el-card
            v-for="shop in shopList"
            :key="shop.id"
            class="shop-card"
            shadow="hover"
            @click="goToShop(shop.id)"
          >
            <div class="shop-image-wrapper">
              <img
                :src="getShopImage(shop.coverUrl)"
                :alt="shop.name"
                class="shop-image"
                @error="handleImageError"
              >
              <div class="shop-status" :class="{ 'is-open': shop.status === 1 }">
                {{ shop.status === 1 ? '营业中' : '休息中' }}
              </div>
            </div>
            <div class="shop-info">
              <h3 class="shop-name">{{ shop.name }}</h3>
              <div class="shop-meta">
                <div class="rating">
                  <el-icon color="#F59E0B"><StarFilled /></el-icon>
                  <span class="rating-score">{{ shop.rating?.toFixed(1) || '4.5' }}</span>
                  <span class="rating-count">月售 {{ getMonthlySales(shop.id) }}+</span>
                </div>
              </div>
              <div class="shop-delivery">
                <span class="delivery-item">
                  <el-icon><Timer /></el-icon>
                  30分钟
                </span>
                <span class="delivery-item">
                  <el-icon><Wallet /></el-icon>
                  起送 ¥{{ shop.minPrice || 0 }}
                </span>
                <span class="delivery-item">
                  <el-icon><Bicycle /></el-icon>
                  配送 ¥{{ shop.deliveryFee || 0 }}
                </span>
              </div>
              <div class="shop-tags" v-if="shop.description">
                <el-tag size="small" effect="plain" type="info">
                  {{ truncateText(shop.description, 20) }}
                </el-tag>
              </div>
            </div>
          </el-card>
        </div>

        <!-- 空状态 -->
        <div v-else class="empty-wrapper">
          <el-empty description="暂无商家数据">
            <el-button type="primary" @click="resetFilter">重置筛选</el-button>
          </el-empty>
        </div>

        <!-- 分页 -->
        <div v-if="shopList.length > 0" class="pagination-wrapper">
          <el-pagination
            v-model:current-page="pagination.pageNum"
            v-model:page-size="pagination.pageSize"
            :page-sizes="[12, 24, 36, 48]"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handlePageChange"
          />
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  Search,
  StarFilled,
  Timer,
  Wallet,
  Bicycle
} from '@element-plus/icons-vue'
import http from '../utils/http.js'

const router = useRouter()
const route = useRoute()

// 加载状态
const loading = ref(false)

// 商家列表数据
const shopList = ref([])

// 分页参数
const pagination = reactive({
  pageNum: 1,
  pageSize: 12,
  total: 0
})

// 筛选参数
const filterParams = reactive({
  category: '',
  sort: '',
  onlyOpen: false,
  keyword: ''
})

// 分类数据
const categories = ref([])

// 获取分类列表
const fetchCategories = async () => {
  try {
    const res = await http.get('/shopCategory/selectAll')
    if (res.code === 200) {
      categories.value = res.data || []
    }
  } catch (error) {
    console.error('获取分类失败:', error)
  }
}

// 获取商家列表
const fetchShopList = async () => {
  loading.value = true
  try {
    const params = {
      pageNum: pagination.pageNum,
      pageSize: pagination.pageSize,
      name: filterParams.keyword || undefined
    }

    // 如果选择了仅看营业中
    if (filterParams.onlyOpen) {
      params.status = 1
    }
    
    // 分类筛选
    if (filterParams.category) {
      const cat = categories.value.find(c => c.name === filterParams.category)
      if (cat) params.categoryId = cat.id
    }

    const res = await http.get('/shop/selectShopPage', { params })
    if (res.code === 200) {
      let list = res.data.list || []
      // 前端排序
      if (filterParams.sort === 'rating_desc') {
        list.sort((a, b) => (b.rating || 0) - (a.rating || 0))
      } else if (filterParams.sort === 'minPrice_asc') {
        list.sort((a, b) => (a.minPrice || 0) - (b.minPrice || 0))
      } else if (filterParams.sort === 'deliveryFee_asc') {
        list.sort((a, b) => (a.deliveryFee || 0) - (b.deliveryFee || 0))
      }
      shopList.value = list
      pagination.total = res.data.total || 0
    } else {
      ElMessage.error(res.msg || '获取商家列表失败')
    }
  } catch (error) {
    console.error('获取商家列表失败:', error)
    ElMessage.error('获取商家列表失败')
    // 使用模拟数据作为后备
    useMockData()
  } finally {
    loading.value = false
  }
}

// 模拟数据（当API不可用时使用）
const useMockData = () => {
  shopList.value = [
    {
      id: 1,
      name: '麦当劳',
      coverUrl: 'https://images.unsplash.com/photo-1571091718767-18b5b1457add?w=400&h=300&fit=crop',
      rating: 4.8,
      minPrice: 20,
      deliveryFee: 0,
      status: 1,
      description: '汉堡薯条，快乐时光'
    },
    {
      id: 2,
      name: '必胜客',
      coverUrl: 'https://images.unsplash.com/photo-1604382354936-07c5d9983bd3?w=400&h=300&fit=crop',
      rating: 4.6,
      minPrice: 30,
      deliveryFee: 5,
      status: 1,
      description: '披萨专家，意式美味'
    },
    {
      id: 3,
      name: '喜茶',
      coverUrl: 'https://images.unsplash.com/photo-1541658016709-82535e94bc69?w=400&h=300&fit=crop',
      rating: 4.9,
      minPrice: 15,
      deliveryFee: 3,
      status: 1,
      description: '灵感之茶，中国制造'
    },
    {
      id: 4,
      name: '海底捞',
      coverUrl: 'https://images.unsplash.com/photo-1555126634-323283e090fa?w=400&h=300&fit=crop',
      rating: 4.7,
      minPrice: 50,
      deliveryFee: 8,
      status: 0,
      description: '服务至上，顾客至上'
    },
    {
      id: 5,
      name: '星巴克',
      coverUrl: 'https://images.unsplash.com/photo-1509042239860-f550ce710b93?w=400&h=300&fit=crop',
      rating: 4.5,
      minPrice: 25,
      deliveryFee: 2,
      status: 1,
      description: '激发并孕育人文精神'
    },
    {
      id: 6,
      name: '肯德基',
      coverUrl: 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?w=400&h=300&fit=crop',
      rating: 4.4,
      minPrice: 25,
      deliveryFee: 4,
      status: 1,
      description: ' finger lickin good'
    }
  ]
  pagination.total = shopList.value.length
}

// 获取商家图片
const getShopImage = (coverUrl) => {
  if (!coverUrl) {
    return 'https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=400&h=300&fit=crop'
  }
  if (coverUrl.startsWith('http')) {
    return coverUrl
  }
  // 使用相对路径，让 vite 代理处理
  return coverUrl.startsWith('/') ? coverUrl : '/' + coverUrl
}

// 图片加载失败处理
const handleImageError = (e) => {
  e.target.src = 'https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=400&h=300&fit=crop'
}

// 获取月售数量（模拟）
const getMonthlySales = (shopId) => {
  // 基于shopId生成一个固定的月售数量
  const base = 100 + (shopId * 50)
  return Math.floor(base / 10) * 10
}

// 文本截断
const truncateText = (text, length) => {
  if (!text) return ''
  return text.length > length ? text.substring(0, length) + '...' : text
}

// 筛选变化处理
const handleFilterChange = () => {
  pagination.pageNum = 1
  fetchShopList()
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.pageSize = size
  fetchShopList()
}

// 页码变化
const handlePageChange = (page) => {
  pagination.pageNum = page
  fetchShopList()
}

// 重置筛选
const resetFilter = () => {
  filterParams.category = ''
  filterParams.sort = ''
  filterParams.onlyOpen = false
  filterParams.keyword = ''
  pagination.pageNum = 1
  fetchShopList()
}

// 跳转到商家详情
const goToShop = (shopId) => {
  router.push(`/shop/${shopId}`)
}

// 监听路由参数变化
watch(() => route.query, (newQuery) => {
  if (newQuery.category) {
    filterParams.category = newQuery.category
  }
  if (newQuery.keyword) {
    filterParams.keyword = newQuery.keyword
  }
  fetchShopList()
}, { immediate: true })

onMounted(() => {
  fetchCategories()
  fetchShopList()
})
</script>

<style scoped>
.shop-list-container {
  min-height: 100vh;
  background-color: #FEF2F2;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* 页面头部 */
.page-header {
  background: linear-gradient(135deg, #DC2626 0%, #EF4444 100%);
  padding: 40px 0;
  color: white;
  text-align: center;
}

.page-title {
  font-size: 36px;
  font-weight: 700;
  margin-bottom: 12px;
}

.page-subtitle {
  font-size: 16px;
  opacity: 0.9;
}

/* 筛选区域 */
.filter-section {
  background: white;
  padding: 20px 0;
  border-bottom: 1px solid #e5e7eb;
  position: sticky;
  top: 60px;
  z-index: 100;
}

.filter-bar {
  display: flex;
  align-items: center;
  gap: 24px;
  flex-wrap: wrap;
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 12px;
}

.filter-label {
  font-size: 14px;
  color: #6b7280;
  white-space: nowrap;
}

.filter-search {
  margin-left: auto;
  width: 280px;
}

/* 商家列表区域 */
.shops-section {
  padding: 40px 0;
}

.loading-wrapper {
  padding: 40px 0;
}

.shops-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.shop-card {
  border-radius: 16px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
  border: none;
}

.shop-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.12);
}

.shop-image-wrapper {
  position: relative;
  height: 160px;
  overflow: hidden;
}

.shop-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.shop-card:hover .shop-image {
  transform: scale(1.05);
}

.shop-status {
  position: absolute;
  top: 12px;
  right: 12px;
  padding: 4px 12px;
  background: #9ca3af;
  color: white;
  font-size: 12px;
  font-weight: 600;
  border-radius: 20px;
}

.shop-status.is-open {
  background: #10b981;
}

.shop-info {
  padding: 16px;
}

.shop-name {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.shop-meta {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.rating {
  display: flex;
  align-items: center;
  gap: 4px;
}

.rating-score {
  font-size: 14px;
  font-weight: 600;
  color: #f59e0b;
}

.rating-count {
  font-size: 12px;
  color: #6b7280;
  margin-left: 8px;
}

.shop-delivery {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
  font-size: 12px;
  color: #6b7280;
}

.delivery-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.shop-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

/* 空状态 */
.empty-wrapper {
  padding: 80px 0;
}

/* 分页 */
.pagination-wrapper {
  display: flex;
  justify-content: center;
  padding: 40px 0;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .shops-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 992px) {
  .filter-bar {
    flex-direction: column;
    align-items: flex-start;
  }

  .filter-search {
    margin-left: 0;
    width: 100%;
  }

  .shops-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 576px) {
  .page-title {
    font-size: 28px;
  }

  .shops-grid {
    grid-template-columns: 1fr;
  }

  .filter-group {
    flex-wrap: wrap;
  }
}
</style>
