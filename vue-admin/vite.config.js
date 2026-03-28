import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 15173,
    proxy: {
      // 配置一个代理，将以 /api 开头的请求代理到目标服务器
      '/api': {
        target: 'http://localhost:8081', // 目标服务器地址
        changeOrigin: true, // 是否改变请求源
        rewrite: (path) => path.replace(/^\/api/, ''), // 重写请求路径
      },
      '/files': {
        target: 'http://localhost:8081',
        changeOrigin: true,
      },
    },
  },
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  }
})
