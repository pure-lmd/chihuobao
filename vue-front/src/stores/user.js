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
        // 检查是否为普通用户角色
        if (res.data.role !== 'USER') {
          return { success: false, message: '管理员请使用管理端登录' }
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
      try {
        const parsedUserInfo = JSON.parse(savedUserInfo)
        // 确保解析的数据包含必要的字段
        userInfo.value = {
          id: parsedUserInfo.id || null,
          username: parsedUserInfo.username || '',
          name: parsedUserInfo.name || '',
          avatar: parsedUserInfo.avatar || '',
          role: parsedUserInfo.role || ''
        }
        isLogin.value = true
      } catch (error) {
        // 如果解析失败，清除损坏的数据
        localStorage.removeItem('userInfo')
        localStorage.removeItem('isLogin')
        console.error('Failed to parse user info from localStorage:', error)
      }
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