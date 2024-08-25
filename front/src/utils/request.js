import router from '@/router'
import axios from 'axios'
import { getToken, setToken, removeToken } from './auth'
import ElementUI from 'element-ui'

const service = axios.create({
  timeout: 10000 // 请求超时设置为 10 秒
})

// axios拦截器，默认增加请求头token
service.interceptors.request.use(
  config => {
    const token = getToken()
    if (token) {
      config.headers.Authorization = token // 如果存在 token，则在请求头中添加 Authorization
    }

    config.headers['X-Real-IP'] = '0.0.0.0' // 如果无法获取 IP，设置为默认值

    return config
  },
  err => {
    return Promise.reject(err) // 请求错误时，返回一个拒绝的 Promise
  }
)

// 防止同时间弹出多个错误提示
let hasErrorMessage = false

const showErrorMessage = (response) => {
  if (!hasErrorMessage && response.data.message && response.data.message !== '用户未登录，请先登录') {
    ElementUI.Message.error(response.data.message) // 使用 ElementUI 弹出错误提示
    hasErrorMessage = true
    setTimeout(() => {
      hasErrorMessage = false // 在 1 秒后允许再次弹出错误提示
    }, 1000)
  }
}

// 更新token
const updateToken = (response) => {
  const oldToken = getToken() // 获取当前存储的旧 token
  const newToken = response.headers.authorization // 从响应头中获取新的 token
  if (newToken && oldToken !== newToken) {
    setToken(newToken) // 如果新 token 存在且与旧 token 不相同，则更新 token
  }
  // 检查是否需要移除token
  if (response.headers['remove-token']) {
    removeToken() // 如果响应头中包含 'remove-token'，则移除当前存储的 token
  }
}

// axios响应拦截器

service.interceptors.response.use(
  function(res) {
    updateToken(res)
    if (res.data && res.data.status && (res.data.status < 200 || res.data.status >= 300)) {
      showErrorMessage(res)
      return Promise.reject(res)
    } else {
      return res
    }
  },
  function(error) {
    if (![500, 400, 404].includes(error.response.status)) {
      updateToken(error.response)
      showErrorMessage(error.response)
    }
    // 处理 401 Unauthorized 错误
    if (error.response.status === 401) {
      // UserUtils.resetUserStore() // 注释的部分可能是重置用户状态
      setTimeout(() => {
        const path = router.app.$route.path
        if (path === '/login') {
          location.reload()
        } else {
          router.push({ path: '/login' })
        }
      }, 1000)
    }
    return Promise.reject(error)
  }
)

export default service
