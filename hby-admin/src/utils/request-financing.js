/**
 * 授信管理专用API请求工具
 * 直接指向后端服务，不走 mock 服务器
 * @author 示例云开发团队
 * @since 2026-02-04
 */
import axios from 'axios'
import store from '@/store'
import { Message } from 'element-ui'

const service = axios.create({
  baseURL: 'http://127.0.0.1:8088', // 直接指向全球司库服务
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json;charset=UTF-8'
  }
})

// request拦截器
service.interceptors.request.use(
  config => {
    // 添加token
    const token = store.getters['user/token']
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
      config.headers['token'] = token
    }
    return config
  },
  error => {
    console.error('请求错误：', error)
    return Promise.reject(error)
  }
)

// response拦截器
service.interceptors.response.use(
  response => {
    const res = response.data

    // 如果响应的状态码不是1，说明接口异常
    if (res.code !== 1 && res.code !== '1') {
      Message({
        message: res.msg || '请求失败',
        type: 'error',
        duration: 5 * 1000
      })
      return Promise.reject(new Error(res.msg || '请求失败'))
    } else {
      return res
    }
  },
  error => {
    console.error('响应错误：', error)
    Message({
      message: error.message || '网络错误',
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
