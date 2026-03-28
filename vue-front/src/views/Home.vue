<template>
  <div class="home-container">
    <!-- Hero 区域 -->
    <section class="hero-section">
      <div class="hero-container">
        <div class="hero-content">
          <h1 class="hero-title">
            发现身边<span class="highlight">美味</span>
          </h1>
          <p class="hero-subtitle">
            数千家优质餐厅，30分钟极速送达<br>
            新用户首单立减 <span class="discount">¥20</span>
          </p>
          <div class="hero-cta">
            <el-button type="primary" size="large" class="cta-primary" @click="scrollToShops">
              <el-icon><Food /></el-icon>
              立即点餐
            </el-button>
            <el-button size="large" class="cta-secondary" @click="showPromotions">
              <el-icon><Ticket /></el-icon>
              查看优惠
            </el-button>
          </div>
          <div class="hero-stats">
            <div class="stat-item">
              <span class="stat-number">5000+</span>
              <span class="stat-label">合作餐厅</span>
            </div>
            <div class="stat-item">
              <span class="stat-number">30min</span>
              <span class="stat-label">平均送达</span>
            </div>
            <div class="stat-item">
              <span class="stat-number">4.9</span>
              <span class="stat-label">用户评分</span>
            </div>
          </div>
        </div>
        <div class="hero-image">
          <div class="hero-card">
            <img src="https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=600&h=400&fit=crop" alt="美食展示" class="hero-img">
            <div class="floating-badge delivery">
              <el-icon><Timer /></el-icon>
              <span>30分钟达</span>
            </div>
            <div class="floating-badge discount">
              <el-icon><Discount /></el-icon>
              <span>首单减20</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 分类导航 -->
    <section class="categories-section">
      <div class="container">
        <h2 class="section-title">美食分类</h2>
        <div class="categories-grid">
          <div
            v-for="category in categories"
            :key="category.id"
            class="category-card"
            @click="filterByCategory(category.name)"
          >
            <div class="category-icon" :style="{ backgroundColor: category.color }">
              <el-icon size="32" color="white">
                <component :is="category.icon" />
              </el-icon>
            </div>
            <span class="category-name">{{ category.name }}</span>
          </div>
        </div>
      </div>
    </section>

    <!-- 推荐餐厅 -->
    <section class="shops-section" ref="shopsSection">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">推荐餐厅</h2>
          <el-button type="text" @click="goToAllShops">
            查看全部 <el-icon><ArrowRight /></el-icon>
          </el-button>
        </div>

        <!-- 加载状态 -->
        <div v-if="loading" class="shops-grid">
          <el-card v-for="n in 6" :key="n" class="shop-card" shadow="hover">
            <el-skeleton animated>
              <template #template>
                <el-skeleton-item variant="image" style="width: 100%; height: 180px;" />
                <div style="padding: 16px;">
                  <el-skeleton-item variant="h3" style="width: 60%; margin-bottom: 10px;" />
                  <el-skeleton-item variant="text" style="width: 80%; margin-bottom: 8px;" />
                  <el-skeleton-item variant="text" style="width: 40%;" />
                </div>
              </template>
            </el-skeleton>
          </el-card>
        </div>

        <div v-else class="shops-grid">
          <el-card
            v-for="shop in featuredShops"
            :key="shop.id"
            class="shop-card"
            shadow="hover"
            @click="goToShop(shop.id)"
          >
            <div class="shop-image-wrapper">
              <img :src="shop.image" :alt="shop.name" class="shop-image">
              <div class="shop-badge" v-if="shop.isNew">新店</div>
              <div class="shop-badge hot" v-if="shop.isHot">热销</div>
            </div>
            <div class="shop-info">
              <h3 class="shop-name">{{ shop.name }}</h3>
              <div class="shop-meta">
                <div class="rating">
                  <el-icon color="#F59E0B"><StarFilled /></el-icon>
                  <span>{{ shop.rating }}</span>
                </div>
                <span class="delivery-time">{{ shop.deliveryTime }}分钟</span>
                <span class="delivery-fee">配送¥{{ shop.deliveryFee }}</span>
              </div>
              <div class="shop-tags">
                <el-tag v-for="tag in shop.tags" :key="tag" size="small" effect="plain">
                  {{ tag }}
                </el-tag>
              </div>
            </div>
          </el-card>
        </div>
      </div>
    </section>

    <!-- 特色服务 -->
    <section class="features-section">
      <div class="container">
        <h2 class="section-title">为什么选择我们</h2>
        <div class="features-grid">
          <div v-for="feature in features" :key="feature.id" class="feature-item">
            <div class="feature-icon-wrapper" :style="{ backgroundColor: feature.bgColor }">
              <el-icon size="40" :color="feature.iconColor">
                <component :is="feature.icon" />
              </el-icon>
            </div>
            <h3 class="feature-title">{{ feature.title }}</h3>
            <p class="feature-desc">{{ feature.description }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 优惠活动 -->
    <section class="promotions-section">
      <div class="container">
        <h2 class="section-title">限时优惠</h2>
        <el-carousel :interval="5000" type="card" height="200px">
          <el-carousel-item v-for="promo in promotions" :key="promo.id">
            <div class="promo-card" :style="{ background: promo.gradient }">
              <div class="promo-content">
                <h3>{{ promo.title }}</h3>
                <p>{{ promo.description }}</p>
                <el-button type="warning" size="small" round @click="claimPromo(promo)">
                  立即领取
                </el-button>
              </div>
              <div class="promo-icon">
                <el-icon size="80" color="white" :opacity="0.3">
                  <component :is="promo.icon" />
                </el-icon>
              </div>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>
    </section>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="container">
        <div class="footer-content">
          <div class="footer-brand">
            <div class="footer-logo">
              <el-icon size="40" color="#DC2626"><Food /></el-icon>
              <span>吃货宝</span>
            </div>
            <p>让美食触手可及，让生活更加美好</p>
          </div>
          <div class="footer-links">
            <div class="link-group">
              <h4>关于我们</h4>
              <a href="#">公司介绍</a>
              <a href="#">加入我们</a>
              <a href="#">联系方式</a>
            </div>
            <div class="link-group">
              <h4>帮助中心</h4>
              <a href="#">常见问题</a>
              <a href="#">配送说明</a>
              <a href="#">退款政策</a>
            </div>
            <div class="link-group">
              <h4>商家服务</h4>
              <a href="#">商家入驻</a>
              <a href="#">商家后台</a>
              <a href="#">合作咨询</a>
            </div>
          </div>
        </div>
        <div class="footer-bottom">
          <p>&copy; 2026 吃货宝外卖. 保留所有权利.</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  Food,
  Ticket,
  Timer,
  Discount,
  ArrowRight,
  StarFilled,
  Bowl,
  Coffee,
  IceCream,
  Chicken,
  Burger,
  Dish,
  Grid,
  ShoppingCart,
  Wallet,
  Headset,
  Medal
} from '@element-plus/icons-vue'
import http from '../utils/http.js'

const router = useRouter()
const shopsSection = ref(null)
const loading = ref(false)

// 美食分类
const categories = ref([
  { id: 1, name: '中式快餐', icon: 'Bowl', color: '#DC2626' },
  { id: 2, name: '西式快餐', icon: 'Burger', color: '#F59E0B' },
  { id: 3, name: '披萨意面', icon: 'Dish', color: '#EF4444' },
  { id: 4, name: '日韩料理', icon: 'Grid', color: '#8B5CF6' },
  { id: 5, name: '甜品饮品', icon: 'IceCream', color: '#EC4899' },
  { id: 6, name: '咖啡茶饮', icon: 'Coffee', color: '#10B981' },
  { id: 7, name: '烧烤炸串', icon: 'Chicken', color: '#F97316' },
  { id: 8, name: '全部美食', icon: 'Food', color: '#6B7280' }
])

// 推荐餐厅
const featuredShops = ref([])

// 获取店铺图片URL
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

// 生成模拟的配送时间（基于店铺ID生成固定值）
const getDeliveryTime = (shopId) => {
  // 使用店铺ID生成20-45之间的固定配送时间
  return 20 + (shopId % 6) * 5
}

// 生成店铺标签（基于店铺名称）
const getShopTags = (shopName) => {
  const tagMap = {
    '麦当劳': ['汉堡', '薯条', '快餐'],
    '肯德基': ['炸鸡', '汉堡', '快餐'],
    '必胜客': ['披萨', '意面', '西餐'],
    '喜茶': ['奶茶', '果茶', '甜品'],
    '海底捞': ['火锅', '川菜', '正餐'],
    '星巴克': ['咖啡', '蛋糕', '下午茶']
  }
  return tagMap[shopName] || ['美食', '外卖', '热门']
}

// 获取推荐餐厅列表
const fetchFeaturedShops = async () => {
  loading.value = true
  try {
    const res = await http.get('/shop/selectShopAll')
    if (res.code === 200 && res.data) {
      // 处理店铺数据，转换为前端展示格式
      featuredShops.value = res.data.slice(0, 6).map((shop, index) => ({
        id: shop.id,
        name: shop.name,
        image: getShopImage(shop.coverUrl),
        rating: shop.rating || 4.5,
        deliveryTime: getDeliveryTime(shop.id),
        deliveryFee: shop.deliveryFee || 0,
        tags: getShopTags(shop.name),
        isHot: index < 3, // 前3个标记为热销
        isNew: shop.id > 10 // ID大于10的标记为新店（模拟逻辑）
      }))
    } else {
      ElMessage.error(res.msg || '获取店铺列表失败')
      useMockShops()
    }
  } catch (error) {
    console.error('获取店铺列表失败:', error)
    ElMessage.error('获取店铺列表失败')
    useMockShops()
  } finally {
    loading.value = false
  }
}

// 使用模拟数据（当API失败时）
const useMockShops = () => {
  featuredShops.value = [
    {
      id: 1,
      name: '麦当劳',
      image: 'https://images.unsplash.com/photo-1571091718767-18b5b1457add?w=400&h=300&fit=crop',
      rating: 4.8,
      deliveryTime: 25,
      deliveryFee: 0,
      tags: ['汉堡', '薯条', '可乐'],
      isHot: true,
      isNew: false
    },
    {
      id: 2,
      name: '必胜客',
      image: 'https://images.unsplash.com/photo-1604382354936-07c5d9983bd3?w=400&h=300&fit=crop',
      rating: 4.6,
      deliveryTime: 35,
      deliveryFee: 5,
      tags: ['披萨', '意面', '鸡翅'],
      isHot: true,
      isNew: false
    },
    {
      id: 3,
      name: '喜茶',
      image: 'https://images.unsplash.com/photo-1541658016709-82535e94bc69?w=400&h=300&fit=crop',
      rating: 4.9,
      deliveryTime: 20,
      deliveryFee: 3,
      tags: ['奶茶', '果茶', '芝士'],
      isHot: false,
      isNew: true
    },
    {
      id: 4,
      name: '海底捞',
      image: 'https://images.unsplash.com/photo-1555126634-323283e090fa?w=400&h=300&fit=crop',
      rating: 4.7,
      deliveryTime: 40,
      deliveryFee: 8,
      tags: ['火锅', '冒菜', '小吃'],
      isHot: true,
      isNew: false
    },
    {
      id: 5,
      name: '星巴克',
      image: 'https://images.unsplash.com/photo-1509042239860-f550ce710b93?w=400&h=300&fit=crop',
      rating: 4.5,
      deliveryTime: 15,
      deliveryFee: 2,
      tags: ['咖啡', '星冰乐', '蛋糕'],
      isHot: false,
      isNew: false
    },
    {
      id: 6,
      name: '肯德基',
      image: 'https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?w=400&h=300&fit=crop',
      rating: 4.4,
      deliveryTime: 30,
      deliveryFee: 4,
      tags: ['炸鸡', '汉堡', '蛋挞'],
      isHot: false,
      isNew: true
    }
  ]
}

// 特色服务
const features = ref([
  {
    id: 1,
    title: '极速配送',
    description: '平均30分钟送达，超时赔付',
    icon: 'Timer',
    bgColor: '#FEF3C7',
    iconColor: '#F59E0B'
  },
  {
    id: 2,
    title: '品质保证',
    description: '严选优质商家，食品安全有保障',
    icon: 'Medal',
    bgColor: '#DBEAFE',
    iconColor: '#3B82F6'
  },
  {
    id: 3,
    title: '优惠多多',
    description: '新用户专享红包，满减活动不断',
    icon: 'Wallet',
    bgColor: '#D1FAE5',
    iconColor: '#10B981'
  },
  {
    id: 4,
    title: '贴心服务',
    description: '7×24小时客服，随时为您解决问题',
    icon: 'Headset',
    bgColor: '#FCE7F3',
    iconColor: '#EC4899'
  }
])

// 优惠活动
const promotions = ref([
  {
    id: 1,
    title: '新用户专享',
    description: '首单立减20元，满30可用',
    icon: 'Ticket',
    gradient: 'linear-gradient(135deg, #DC2626 0%, #EF4444 100%)'
  },
  {
    id: 2,
    title: '满减优惠',
    description: '满50减15，满100减40',
    icon: 'Discount',
    gradient: 'linear-gradient(135deg, #F59E0B 0%, #FBBF24 100%)'
  },
  {
    id: 3,
    title: '免配送费',
    description: '指定商家下单免配送费',
    icon: 'ShoppingCart',
    gradient: 'linear-gradient(135deg, #10B981 0%, #34D399 100%)'
  }
])

// 方法
const scrollToShops = () => {
  shopsSection.value?.scrollIntoView({ behavior: 'smooth' })
}

const showPromotions = () => {
  ElMessage.info('优惠活动页面开发中...')
}

const filterByCategory = (categoryName) => {
  ElMessage.info(`筛选分类: ${categoryName}`)
  // router.push(`/category/${encodeURIComponent(categoryName)}`)
}

const goToShop = (shopId) => {
  router.push(`/shop/${shopId}`)
}

const goToAllShops = () => {
  router.push('/shops')
}

const claimPromo = (promo) => {
  ElMessage.success(`已领取: ${promo.title}`)
}

// 页面加载时获取数据
onMounted(() => {
  fetchFeaturedShops()
})
</script>

<style scoped>
/* 基础样式 */
.home-container {
  min-height: 100vh;
  background-color: #FEF2F2;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* Hero 区域 */
.hero-section {
  background: linear-gradient(135deg, #FEF2F2 0%, #FED7AA 50%, #FEF2F2 100%);
  padding: 60px 0;
}

.hero-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
  align-items: center;
}

.hero-title {
  font-size: 56px;
  font-weight: 800;
  color: #450A0A;
  line-height: 1.2;
  margin-bottom: 20px;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

.hero-title .highlight {
  color: #DC2626;
  position: relative;
}

.hero-title .highlight::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 12px;
  background: linear-gradient(120deg, #FEF08A 0%, #FDE047 100%);
  z-index: -1;
  border-radius: 4px;
}

.hero-subtitle {
  font-size: 20px;
  color: #7F1D1D;
  line-height: 1.8;
  margin-bottom: 32px;
}

.hero-subtitle .discount {
  color: #DC2626;
  font-weight: 700;
  font-size: 28px;
}

.hero-cta {
  display: flex;
  gap: 16px;
  margin-bottom: 40px;
}

.cta-primary {
  background: linear-gradient(135deg, #DC2626 0%, #EF4444 100%);
  border: none;
  padding: 16px 32px;
  font-size: 18px;
  border-radius: 12px;
  box-shadow: 0 4px 14px rgba(220, 38, 38, 0.4);
  transition: transform 0.2s, box-shadow 0.2s;
}

.cta-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(220, 38, 38, 0.5);
}

.cta-secondary {
  padding: 16px 32px;
  font-size: 18px;
  border-radius: 12px;
  border-color: #DC2626;
  color: #DC2626;
}

.cta-secondary:hover {
  background: #FEF2F2;
  border-color: #DC2626;
  color: #DC2626;
}

.hero-stats {
  display: flex;
  gap: 40px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.stat-number {
  font-size: 32px;
  font-weight: 800;
  color: #DC2626;
}

.stat-label {
  font-size: 14px;
  color: #7F1D1D;
}

/* Hero 图片区域 */
.hero-image {
  display: flex;
  justify-content: center;
  align-items: center;
}

.hero-card {
  position: relative;
  border-radius: 24px;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(220, 38, 38, 0.2);
}

.hero-img {
  width: 100%;
  max-width: 500px;
  height: auto;
  display: block;
}

.floating-badge {
  position: absolute;
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 16px;
  border-radius: 50px;
  font-size: 14px;
  font-weight: 600;
  color: white;
  animation: float 3s ease-in-out infinite;
}

.floating-badge.delivery {
  top: 20px;
  right: -20px;
  background: linear-gradient(135deg, #10B981 0%, #34D399 100%);
}

.floating-badge.discount {
  bottom: 30px;
  left: -30px;
  background: linear-gradient(135deg, #F59E0B 0%, #FBBF24 100%);
  animation-delay: 1.5s;
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

/* 分类区域 */
.categories-section {
  padding: 60px 0;
  background: white;
}

.section-title {
  font-size: 32px;
  font-weight: 700;
  color: #450A0A;
  text-align: center;
  margin-bottom: 40px;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

.categories-grid {
  display: grid;
  grid-template-columns: repeat(8, 1fr);
  gap: 16px;
}

.category-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 20px 10px;
  border-radius: 16px;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

.category-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

.category-icon {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.2s;
}

.category-card:hover .category-icon {
  transform: scale(1.1);
}

.category-name {
  font-size: 14px;
  color: #450A0A;
  font-weight: 500;
}

/* 餐厅区域 */
.shops-section {
  padding: 60px 0;
  background: #FEF2F2;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.section-header .section-title {
  margin-bottom: 0;
  text-align: left;
}

.shops-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
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
  height: 180px;
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

.shop-badge {
  position: absolute;
  top: 12px;
  left: 12px;
  padding: 4px 12px;
  background: #10B981;
  color: white;
  font-size: 12px;
  font-weight: 600;
  border-radius: 20px;
}

.shop-badge.hot {
  background: #DC2626;
  left: auto;
  right: 12px;
}

.shop-info {
  padding: 16px;
}

.shop-name {
  font-size: 18px;
  font-weight: 600;
  color: #450A0A;
  margin-bottom: 10px;
}

.shop-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
  font-size: 13px;
  color: #7F1D1D;
}

.rating {
  display: flex;
  align-items: center;
  gap: 4px;
  font-weight: 600;
  color: #F59E0B;
}

.shop-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.shop-tags .el-tag {
  border-radius: 12px;
}

/* 特色服务区域 */
.features-section {
  padding: 60px 0;
  background: white;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 32px;
}

.feature-item {
  text-align: center;
  padding: 32px 20px;
  border-radius: 20px;
  transition: transform 0.2s, box-shadow 0.2s;
}

.feature-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.08);
}

.feature-icon-wrapper {
  width: 80px;
  height: 80px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
}

.feature-title {
  font-size: 18px;
  font-weight: 600;
  color: #450A0A;
  margin-bottom: 8px;
}

.feature-desc {
  font-size: 14px;
  color: #7F1D1D;
  line-height: 1.6;
}

/* 优惠活动区域 */
.promotions-section {
  padding: 60px 0;
  background: #FEF2F2;
}

.promo-card {
  height: 100%;
  border-radius: 16px;
  padding: 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  overflow: hidden;
}

.promo-content {
  color: white;
  z-index: 1;
}

.promo-content h3 {
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 8px;
}

.promo-content p {
  font-size: 14px;
  opacity: 0.9;
  margin-bottom: 16px;
}

.promo-icon {
  position: absolute;
  right: 20px;
  top: 50%;
  transform: translateY(-50%);
}

/* 页脚 */
.footer {
  background: #450A0A;
  color: white;
  padding: 60px 0 20px;
}

.footer-content {
  display: grid;
  grid-template-columns: 1fr 2fr;
  gap: 60px;
  margin-bottom: 40px;
}

.footer-brand {
  max-width: 300px;
}

.footer-logo {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.footer-logo span {
  font-size: 24px;
  font-weight: 700;
}

.footer-brand p {
  color: #FCA5A5;
  line-height: 1.6;
}

.footer-links {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 40px;
}

.link-group h4 {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 16px;
  color: white;
}

.link-group a {
  display: block;
  color: #FCA5A5;
  text-decoration: none;
  margin-bottom: 10px;
  transition: color 0.2s;
}

.link-group a:hover {
  color: white;
}

.footer-bottom {
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  padding-top: 20px;
  text-align: center;
}

.footer-bottom p {
  color: #FCA5A5;
  font-size: 14px;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .hero-container {
    grid-template-columns: 1fr;
    text-align: center;
  }

  .hero-title {
    font-size: 42px;
  }

  .hero-cta {
    justify-content: center;
  }

  .hero-stats {
    justify-content: center;
  }

  .hero-image {
    order: -1;
  }

  .hero-card {
    max-width: 400px;
  }

  .categories-grid {
    grid-template-columns: repeat(4, 1fr);
  }

  .shops-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .features-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .nav-container {
    flex-wrap: wrap;
  }

  .nav-search {
    order: 3;
    max-width: 100%;
    width: 100%;
  }

  .hero-title {
    font-size: 36px;
  }

  .hero-subtitle {
    font-size: 16px;
  }

  .hero-cta {
    flex-direction: column;
  }

  .hero-stats {
    gap: 24px;
  }

  .stat-number {
    font-size: 24px;
  }

  .categories-grid {
    grid-template-columns: repeat(4, 1fr);
    gap: 12px;
  }

  .category-icon {
    width: 48px;
    height: 48px;
  }

  .shops-grid {
    grid-template-columns: 1fr;
  }

  .features-grid {
    grid-template-columns: 1fr;
  }

  .footer-content {
    grid-template-columns: 1fr;
    gap: 40px;
  }

  .footer-links {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 480px) {
  .hero-title {
    font-size: 28px;
  }

  .categories-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .category-card {
    padding: 16px 8px;
  }

  .footer-links {
    grid-template-columns: 1fr;
  }
}
</style>
