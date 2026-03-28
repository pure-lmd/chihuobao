import { defineStore } from 'pinia'
import { ref } from 'vue'
import http from '../utils/http.js'

export const useUserStore = defineStore('user', () => {
  // 用户信息
  const userInfo = ref({
    id: null,
    username: '',
    name: '',
    avatar: '',
    role: ''
  })

  // 登录状态
  const isLogin = ref(false)

  // 登录方法
  const login = async (loginForm) => {
    try {
      const res = await http.post('/login', {
        username: loginForm.username,
        password: loginForm.password
      })

      if (res.code === 200 && res.data) {
        // 检查是否为管理员角色
        if (res.data.role !== 'ADMIN' && res.data.role !== 'SUPER_ADMIN') {
          return { success: false, message: '该账号无管理员权限' }
        }
        // 保存用户信息
        userInfo.value = res.data
        isLogin.value = true

        // 保存到localStorage
        localStorage.setItem('userInfo', JSON.stringify(res.data))
        localStorage.setItem('isLogin', 'true')

        return { success: true, message: '登录成功' }
      } else {
        return { success: false, message: res.msg || '用户名或密码错误' }
      }
    } catch (error) {
      return { success: false, message: error.response?.data?.msg || '登录失败' }
    }
  }

  // 登出方法
  const logout = () => {
    userInfo.value = {
      id: null,
      username: '',
      name: '',
      avatar: '',
      role: ''
    }
    isLogin.value = false

    // 清除localStorage
    localStorage.removeItem('userInfo')
    localStorage.removeItem('isLogin')
  }

  // 初始化用户状态（从localStorage恢复）
  const initUserState = () => {
    const savedUserInfo = localStorage.getItem('userInfo')
    const savedIsLogin = localStorage.getItem('isLogin')

    if (savedUserInfo && savedIsLogin === 'true') {
      userInfo.value = JSON.parse(savedUserInfo)
      isLogin.value = true
    }
  }

  // 更新用户信息
  const updateUserInfo = (newInfo) => {
    userInfo.value = { ...userInfo.value, ...newInfo }

    // 更新localStorage
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
  }

  return {
    userInfo,
    isLogin,
    login,
    logout,
    initUserState,
    updateUserInfo
  }
})
