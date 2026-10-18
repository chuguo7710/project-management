import axios from 'axios'
import type { AxiosInstance, InternalAxiosRequestConfig, AxiosResponse } from 'axios'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'
import router from '@/router/index'

const request: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  timeout: 5000,
})

request.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // 从本地存储中获取token并上传
    const store = useUserStore()
    if (store.token) {
      config.headers.Authorization = `Bearer ${store.token}` // 将token添加到请求头中
    }
    return config
  },
  (error) => {
    // 请求配置错误
    return Promise.reject(error)
  },
)

request.interceptors.response.use(
  (response: AxiosResponse) => {
    // 2xx状态码
    console.log('200')
    if (response.data.code === 0) {
      ElMessage.error(response.data.msg)
    }
    return response
  },
  (error) => {
    // 非2xx状态码
    if (error.response) {
      const status = error.response.status
      switch (status) {
        case 401: // 登录过期
          console.log('401')
          ElMessage.error(error.response.data)
          const store = useUserStore()
          store.logout()
          router.push({ name: 'login' })
          break
        case 403: // 没有权限
          console.log('403')
          ElMessage.error('无权限访问')
          break
        case 404: // 请求的资源不存在
          console.log('404')
          ElMessage.error('404,请求的资源不存在')
          break
        case 405: // 请求方法不允许
          console.log('405')
          ElMessage.error('请求方法不匹配，请联系管理员')
          break
        case 500:
          console.log('500')
          ElMessage.error('服务器异常')
          break
        case 502: // 服务器异常
          console.log('502')
          ElMessage.error('服务器接收不到请求')
          break
      }
    } else if (error.request) {
      // 请求已发出，但没有收到响应
      ElMessage.error('服务器未响应')
    } else {
      // 其他错误
      ElMessage.error(error.response?.data?.msg || '网络异常')
    }
    return Promise.reject(error)
  },
)

export default request
