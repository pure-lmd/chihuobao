import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建axios实例
const http = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080', // 从环境变量获取后端接口地址
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
http.interceptors.request.use(
  config => {
    // 在发送请求之前做些什么
    return config
  },
  error => {
    // 对请求错误做些什么
    return Promise.reject(error)
  }
)

// 响应拦截器
http.interceptors.response.use(
  response => {
    // 直接返回响应数据，调用方可以直接使用 response.code, response.data, response.msg
    return response.data
  },
  error => {
    // 对响应错误做点什么
    if (error.response && error.response.data && error.response.data.msg) {
      ElMessage.error(error.response.data.msg)
    } else {
      ElMessage.error('网络请求失败')
    }
    return Promise.reject(error)
  }
)

export default http
