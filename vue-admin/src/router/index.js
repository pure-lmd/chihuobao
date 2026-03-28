import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { requiresAuth: false, title: '登录' }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue'),
    meta: { requiresAuth: false, title: '注册' }
  },
  {
    path: '/manager',
    name: 'Manager',
    component: () => import('../views/Manager.vue'),
    meta: { requiresAuth: true },
    redirect: '/manager/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: { requiresAuth: true, title: '仪表盘' }
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('../views/User.vue'),
        meta: { requiresAuth: true, title: '用户列表' }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('../views/Profile.vue'),
        meta: { requiresAuth: true, title: '个人资料' }
      },
      {
        path: 'orders',
        name: 'Orders',
        component: () => import('../views/manager/Orders.vue'),
        meta: { requiresAuth: true, title: '订单列表' }
      },
      {
        path: 'orders/detail/:id',
        name: 'OrderDetail',
        component: () => import('../views/manager/OrderDetail.vue'),
        meta: { requiresAuth: true, title: '订单详情' }
      },
      {
        path: 'shop',
        name: 'Shop',
        component: () => import('../views/manager/Shop.vue'),
        meta: { requiresAuth: true, title: '商店管理' }
      },
      {
        path: 'product',
        name: 'Product',
        component: () => import('../views/manager/Product.vue'),
        meta: { requiresAuth: true, title: '菜品管理' }
      },
      {
        path: 'category',
        name: 'Category',
        component: () => import('../views/manager/Category.vue'),
        meta: { requiresAuth: true, title: '分类管理' }
      }
    ]
  },
  {
    path: '/404',
    name: 'NotFound',
    component: () => import('../views/NotFound.vue'),
    meta: { requiresAuth: false, title: '页面未找到' }
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/404'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()

  if (to.meta.requiresAuth && !userStore.isLogin) {
    // 需要登录但未登录，跳转到登录页
    next('/login')
  } else if (to.path === '/login' && userStore.isLogin) {
    // 已登录访问登录页，跳转到管理页
    next('/manager')
  } else {
    next()
  }
})

export default router
