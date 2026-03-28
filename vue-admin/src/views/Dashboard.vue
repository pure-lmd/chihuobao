<template>
  <div class="dashboard">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="12" :sm="12" :md="6" v-for="item in statsCards" :key="item.title">
        <el-card class="stats-card" shadow="hover">
          <div class="stats-content">
            <div class="stats-icon" :style="{ background: item.bgColor }">
              <el-icon size="28" :color="item.color">
                <component :is="item.icon" />
              </el-icon>
            </div>
            <div class="stats-info">
              <p class="stats-title">{{ item.title }}</p>
              <p class="stats-value" :style="{ color: item.color }">{{ item.value }}</p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第一行图表 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :xs="24" :sm="24" :md="14">
        <el-card class="chart-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span class="card-header-title">近7日订单趋势</span>
              <el-tag type="warning" size="small">实时</el-tag>
            </div>
          </template>
          <LineChart :options="orderTrendOptions" />
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="10">
        <el-card class="chart-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span class="card-header-title">订单状态分布</span>
            </div>
          </template>
          <PieChart :options="orderStatusOptions" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 第二行图表 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :xs="24" :sm="24" :md="12">
        <el-card class="chart-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span class="card-header-title">热销菜品TOP10</span>
              <el-tag type="danger" size="small">畅销</el-tag>
            </div>
          </template>
          <BarChart :options="hotProductsOptions" />
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12">
        <el-card class="chart-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span class="card-header-title">菜品分类销量占比</span>
            </div>
          </template>
          <PieChart :options="categorySalesOptions" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 第三行图表 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :xs="24" :sm="24" :md="12">
        <el-card class="chart-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span class="card-header-title">月度收入趋势</span>
              <el-tag type="success" size="small">收入</el-tag>
            </div>
          </template>
          <AreaChart :options="monthlyRevenueOptions" />
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12">
        <el-card class="chart-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span class="card-header-title">用户增长趋势</span>
              <el-tag type="primary" size="small">用户</el-tag>
            </div>
          </template>
          <LineChart :options="userGrowthOptions" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 底部欢迎信息 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="24">
        <el-card class="welcome-card" shadow="hover">
          <div class="welcome-content">
            <div class="welcome-icon">
              <el-icon size="48" color="#ff6b35"><ShoppingCart /></el-icon>
            </div>
            <div class="welcome-text">
              <h3>欢迎回来，{{ userStore.userInfo.name || userStore.userInfo.username }}！</h3>
              <p>您的角色是：{{ getRoleText(userStore.userInfo.role) }}</p>
              <p class="system-status">
                <span class="status-dot"></span>
                系统运行正常，所有功能可正常使用
              </p>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '../stores/user'
import http from '../utils/http'
import {
  User, Shop, ShoppingCart, Money, DataAnalysis, TrendCharts
} from '@element-plus/icons-vue'
import LineChart from '../components/charts/LineChart.vue'
import BarChart from '../components/charts/BarChart.vue'
import PieChart from '../components/charts/PieChart.vue'
import AreaChart from '../components/charts/AreaChart.vue'

const userStore = useUserStore()

const getRoleText = (role) => {
  const roleMap = {
    'SUPER_ADMIN': '超级管理员',
    'ADMIN': '管理员',
    'USER': '普通用户'
  }
  return roleMap[role] || role
}

// 格式化数字
const formatNumber = (num) => {
  if (num === null || num === undefined) return '0'
  return num.toLocaleString()
}

// 统计卡片数据
const statsCards = ref([
  { title: '用户总数', value: '0', icon: 'User', color: '#ff6b35', bgColor: '#fff5f0' },
  { title: '商店数量', value: '0', icon: 'Shop', color: '#409EFF', bgColor: '#ecf5ff' },
  { title: '订单总数', value: '0', icon: 'ShoppingCart', color: '#67C23A', bgColor: '#f0f9eb' },
  { title: '收入总额', value: '¥0', icon: 'Money', color: '#F56C6C', bgColor: '#fef0f0' }
])

// 图表数据
const orderTrendData = ref({ dates: [], orderCounts: [], amounts: [] })
const orderStatusData = ref([])
const hotProductsData = ref({ names: [], sales: [] })
const categorySalesData = ref([])
const monthlyRevenueData = ref({ months: [], revenues: [] })
const userGrowthData = ref({ months: [], newUsers: [] })

// 加载统计数据
const loadStatistics = async () => {
  try {
    // 加载统计卡片
    const summaryRes = await http.get('/statistics/summary')
    if (summaryRes.code === 200) {
      const data = summaryRes.data
      statsCards.value = [
        { title: '用户总数', value: formatNumber(data.userCount), icon: 'User', color: '#ff6b35', bgColor: '#fff5f0' },
        { title: '商店数量', value: formatNumber(data.shopCount), icon: 'Shop', color: '#409EFF', bgColor: '#ecf5ff' },
        { title: '订单总数', value: formatNumber(data.orderCount), icon: 'ShoppingCart', color: '#67C23A', bgColor: '#f0f9eb' },
        { title: '收入总额', value: '¥' + formatNumber(data.totalRevenue), icon: 'Money', color: '#F56C6C', bgColor: '#fef0f0' }
      ]
    }

    // 加载近7日订单趋势
    const trendRes = await http.get('/statistics/order-trend')
    if (trendRes.code === 200) {
      orderTrendData.value = trendRes.data
    }

    // 加载订单状态分布
    const statusRes = await http.get('/statistics/order-status')
    if (statusRes.code === 200) {
      const statusMap = { 1: '待付款', 2: '待接单', 3: '配送中', 4: '已完成', 5: '已取消' }
      orderStatusData.value = statusRes.data.map(item => ({
        name: statusMap[item.name] || item.name,
        value: item.value
      }))
    }

    // 加载热销菜品
    const hotRes = await http.get('/statistics/hot-products')
    if (hotRes.code === 200) {
      const products = hotRes.data
      hotProductsData.value = {
        names: products.map(p => p.name),
        sales: products.map(p => p.sales)
      }
    }

    // 加载分类销量
    const categoryRes = await http.get('/statistics/category-sales')
    if (categoryRes.code === 200) {
      categorySalesData.value = categoryRes.data
    }

    // 加载月度收入
    const monthlyRes = await http.get('/statistics/monthly-revenue')
    if (monthlyRes.code === 200) {
      monthlyRevenueData.value = monthlyRes.data
    }

    // 加载用户增长
    const userRes = await http.get('/statistics/user-growth')
    if (userRes.code === 200) {
      const users = userRes.data
      userGrowthData.value = {
        months: users.map(u => u.month),
        newUsers: users.map(u => u.count)
      }
    }
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

onMounted(() => {
  loadStatistics()
})

// 近7日订单趋势 - 双轴折线图
const orderTrendOptions = computed(() => ({
  tooltip: {
    trigger: 'axis',
    axisPointer: { type: 'cross' }
  },
  legend: {
    data: ['订单数', '订单金额'],
    bottom: 0
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '15%',
    top: '10%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: orderTrendData.value.dates.length ? orderTrendData.value.dates : []
  },
  yAxis: [
    {
      type: 'value',
      name: '订单数',
      position: 'left',
      axisLine: { show: true, lineStyle: { color: '#5470C6' } }
    },
    {
      type: 'value',
      name: '金额(元)',
      position: 'right',
      axisLine: { show: true, lineStyle: { color: '#91CC75' } },
      splitLine: { show: false }
    }
  ],
  series: [
    {
      name: '订单数',
      type: 'line',
      smooth: true,
      data: orderTrendData.value.orderCounts.length ? orderTrendData.value.orderCounts : [],
      itemStyle: { color: '#5470C6' },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0, y: 0, x2: 0, y2: 1,
          colorStops: [
            { offset: 0, color: 'rgba(84, 112, 198, 0.3)' },
            { offset: 1, color: 'rgba(84, 112, 198, 0.05)' }
          ]
        }
      }
    },
    {
      name: '订单金额',
      type: 'line',
      smooth: true,
      yAxisIndex: 1,
      data: orderTrendData.value.amounts.length ? orderTrendData.value.amounts : [],
      itemStyle: { color: '#91CC75' }
    }
  ]
}))

// 订单状态分布 - 饼图
const statusColors = ['#909399', '#409EFF', '#E6A23C', '#67C23A', '#F56C6C']
const orderStatusOptions = computed(() => ({
  tooltip: {
    trigger: 'item',
    formatter: '{b}: {c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    right: '5%',
    top: 'center'
  },
  series: [
    {
      type: 'pie',
      radius: ['45%', '70%'],
      center: ['40%', '50%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 8,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: { show: false },
      emphasis: {
        label: {
          show: true,
          fontSize: 14,
          fontWeight: 'bold'
        }
      },
      data: orderStatusData.value.length ? orderStatusData.value.map((item, index) => ({
        value: item.value,
        name: item.name,
        itemStyle: { color: statusColors[index % statusColors.length] }
      })) : []
    }
  ]
}))

// 热销菜品TOP10 - 横向柱状图
const hotProductsOptions = computed(() => ({
  tooltip: {
    trigger: 'axis',
    axisPointer: { type: 'shadow' }
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    top: '5%',
    containLabel: true
  },
  xAxis: {
    type: 'value',
    splitLine: { show: false }
  },
  yAxis: {
    type: 'category',
    data: hotProductsData.value.names.length ? hotProductsData.value.names : [],
    inverse: true
  },
  series: [
    {
      type: 'bar',
      data: hotProductsData.value.sales.length ? hotProductsData.value.sales : [],
      itemStyle: {
        color: {
          type: 'linear',
          x: 0, y: 0, x2: 1, y2: 0,
          colorStops: [
            { offset: 0, color: '#ff6b35' },
            { offset: 1, color: '#ffa35c' }
          ]
        },
        borderRadius: [0, 4, 4, 0]
      },
      barWidth: '60%'
    }
  ]
}))

// 菜品分类销量占比 - 环形图
const categoryColors = ['#ff6b35', '#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399', '#B88230', '#00CE94']
const categorySalesOptions = computed(() => ({
  tooltip: {
    trigger: 'item',
    formatter: '{b}: {c} ({d}%)'
  },
  legend: {
    orient: 'horizontal',
    bottom: '5%'
  },
  series: [
    {
      type: 'pie',
      radius: ['40%', '65%'],
      center: ['50%', '45%'],
      roseType: 'radius',
      itemStyle: {
        borderRadius: 5,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: true,
        formatter: '{b}\n{d}%'
      },
      data: categorySalesData.value.length ? categorySalesData.value.map((item, index) => ({
        value: item.value,
        name: item.name,
        itemStyle: { color: categoryColors[index % categoryColors.length] }
      })) : []
    }
  ]
}))

// 月度收入趋势 - 面积图
const monthlyRevenueOptions = computed(() => ({
  tooltip: {
    trigger: 'axis',
    axisPointer: { type: 'cross' }
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    top: '10%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: monthlyRevenueData.value.months.length ? monthlyRevenueData.value.months : []
  },
  yAxis: {
    type: 'value',
    name: '收入(元)'
  },
  series: [
    {
      name: '收入',
      type: 'line',
      smooth: true,
      symbol: 'circle',
      symbolSize: 8,
      data: monthlyRevenueData.value.revenues.length ? monthlyRevenueData.value.revenues : [],
      itemStyle: { color: '#67C23A' },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0, y: 0, x2: 0, y2: 1,
          colorStops: [
            { offset: 0, color: 'rgba(103, 194, 58, 0.5)' },
            { offset: 1, color: 'rgba(103, 194, 58, 0.05)' }
          ]
        }
      }
    }
  ]
}))

// 用户增长趋势 - 折线图
const userGrowthOptions = computed(() => ({
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    data: ['新增用户'],
    bottom: 0
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '15%',
    top: '10%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: userGrowthData.value.months.length ? userGrowthData.value.months : []
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '新增用户',
      type: 'line',
      smooth: true,
      data: userGrowthData.value.newUsers.length ? userGrowthData.value.newUsers : [],
      itemStyle: { color: '#ff6b35' },
      lineStyle: { width: 3 }
    }
  ]
}))
</script>

<style scoped>
.dashboard {
  padding: 0;
}

.stats-row {
  margin-bottom: 20px;
}

.stats-card {
  height: 110px;
  border-radius: 12px;
  transition: all 0.3s ease;
  border: none;
}

.stats-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.stats-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.stats-icon {
  width: 56px;
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  margin-right: 16px;
  flex-shrink: 0;
}

.stats-title {
  margin: 0 0 8px 0;
  font-size: 14px;
  color: #909399;
}

.stats-value {
  margin: 0;
  font-size: 26px;
  font-weight: 600;
}

.chart-row {
  margin-bottom: 20px;
}

.chart-card {
  border-radius: 12px;
  border: none;
  transition: all 0.3s ease;
}

.chart-card:hover {
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.card-header-title {
  font-weight: 600;
  font-size: 16px;
  color: #303133;
}

.welcome-card {
  border-radius: 12px;
  border: none;
  background: linear-gradient(135deg, #fff9f5 0%, #fff 100%);
}

.welcome-content {
  display: flex;
  align-items: center;
  padding: 20px;
}

.welcome-icon {
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff5f0;
  border-radius: 50%;
  margin-right: 24px;
  flex-shrink: 0;
}

.welcome-text h3 {
  margin: 0 0 8px 0;
  font-size: 20px;
  color: #303133;
}

.welcome-text p {
  margin: 4px 0;
  color: #606266;
  font-size: 14px;
}

.system-status {
  display: flex;
  align-items: center;
  margin-top: 12px !important;
}

.status-dot {
  width: 8px;
  height: 8px;
  background: #67C23A;
  border-radius: 50%;
  margin-right: 8px;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.5; }
}

@media (max-width: 768px) {
  .welcome-content {
    flex-direction: column;
    text-align: center;
  }

  .welcome-icon {
    margin-right: 0;
    margin-bottom: 16px;
  }

  .stats-value {
    font-size: 20px;
  }
}
</style>
