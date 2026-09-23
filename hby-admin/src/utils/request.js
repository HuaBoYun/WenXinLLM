import Vue from 'vue'
import axios from 'axios'
import {
  baseURL,
  contentType,
  debounce,
  messageName,
  requestTimeout,
  statusName,
  successCode,
  /* tokenName, */
} from '@/config'
import store from '@/store'
import qs from 'qs'
import router from '@/router'
import { isArray } from '@/utils/validate'
import { needErrorLog, addErrorLog } from '@/vab/plugins/errorLog'
import { refreshToken } from '@/api/refreshToken'
import {
  mockOrganizationStructurePage,
  mockFeatureStats,
  mockRecentActivities,
  mockNotifications,
  mockExecutionPage,
  mockOrganizations,
  mockAccounts,
  mockVarianceAnalysis,
  mockUsers,
  mockIntegrationStatus,
  mockIntegrationStats
} from '@/mock/budget'

let loadingInstance

let refreshToking = false

let requests = []

// 操作正常Code数组
const codeVerificationArray = isArray(successCode)
  ? [...successCode]
  : [...[successCode]]

const CODE_MESSAGE = {
  200: '服务器成功返回请求数据',
  201: '新建或修改数据成功',
  202: '一个请求已经进入后台排队(异步任务)',
  204: '删除数据成功',
  400: '发出信息有误',
  401: '用户没有权限(令牌失效、用户名、密码错误、登录过期)',
  402: '令牌过期',
  403: '用户得到授权，但是访问是被禁止的',
  404: '访问资源不存在',
  406: '请求格式不可得',
  410: '请求资源被永久删除，且不会被看到',
  500: '服务器发生错误',
  502: '网关错误',
  503: '服务不可用，服务器暂时过载或维护',
  504: '网关超时',
}

/**
 * axios请求拦截器配置
 * @param config
 * @returns {any}
 */
const requestConf = (config) => {
  const token = store.getters['user/token']

  // 只有在请求头中没有token时才添加默认token
  if (!config.headers['token']) {
    // 规范写法 不可随意自定义
    if (token) config.headers['Authorization'] = `Bearer ${token}`
    if (token) config.headers['token'] = token
  }

  // ========== 计费系统请求头（必须在 if 外面，确保每个请求都带上） ==========
  if (token) {
    config.headers['X-Billing-Token'] = token
  }
  const pageRoute = window.__currentPageRoute || ''
  if (pageRoute) {
    config.headers['X-Page-Route'] = pageRoute
  }
  const pageRightId = window.__currentPageRightId || ''
  if (pageRightId) {
    config.headers['X-Page-Right-Id'] = pageRightId
  }
  const pageModule = localStorage.getItem('model') || ''
  if (pageModule) {
    config.headers['X-Page-Module'] = pageModule
  }
  const pageModuleName = window.__currentPageModuleName || ''
  if (pageModuleName) {
    config.headers['X-Page-Module-Name'] = encodeURIComponent(pageModuleName)
  }
  const pageName = window.__currentPageName || ''
  if (pageName) {
    config.headers['X-Page-Name'] = encodeURIComponent(pageName)
  }
  const subModuleName = window.__currentSubModuleName || ''
  if (subModuleName) {
    config.headers['X-Page-Sub-Module-Name'] = encodeURIComponent(subModuleName)
  }

  // 修复生产环境中 /contract 请求缺少 /api 前缀的问题
  if (process.env.NODE_ENV === 'production' && config.url && config.url.startsWith('/contract')) {
    // 在生产环境中，确保 /contract 请求使用完整的 baseURL
    // 如果 baseURL 已经包含 /api，则不需要额外处理
    // 如果 baseURL 不包含 /api，则需要手动添加
    const { baseURL: configBaseURL } = require('@/config')
    if (configBaseURL && !config.url.startsWith(configBaseURL)) {
      // 确保 baseURL 以 /api 结尾
      const normalizedBaseURL = configBaseURL.endsWith('/api') ? configBaseURL : `${configBaseURL}/api`
      config.baseURL = normalizedBaseURL
    }
  }

  // Dify 代理服务请求：/dify 开头的请求统一指向 huabao.example.com
  if (config.url && config.url.startsWith('/console/dify')) {
    config.baseURL = 'https://www.huabao.example.com'
  }

  // 穿透式监管模块后端使用@RequestBody，需要JSON格式
  if (config.url && config.url.includes('/monitor/v1/supervision/')) {
    config.headers['Content-Type'] = 'application/json;charset=UTF-8'
  }

  // 产品成本管理模块后端使用@RequestBody ProductCostQueryParam，需要JSON格式
  if (config.url && config.url.includes('/ma/productcost/')) {
    config.headers['Content-Type'] = 'application/json;charset=UTF-8'
  }

  if (
    config.data &&
    config.headers['Content-Type'] ===
    'application/x-www-form-urlencoded;charset=UTF-8'
  )
    config.data = qs.stringify(config.data)
  if (debounce.some((item) => config.url.includes(item)))
    loadingInstance = Vue.prototype.$baseLoading()
  return config
}

/**
 * 刷新刷新令牌
 * @param config 过期请求配置
 * @returns {any} 返回结果
 */
const tryRefreshToken = async (config) => {
  if (!refreshToking) {
    refreshToking = true
    try {
      const {
        data: { token },
      } = await refreshToken()
      if (token) {
        store.dispatch('user/setToken', token).then(() => { })
        // 已经刷新了token，将所有队列中的请求进行重试
        requests.forEach((cb) => cb(token))
        requests = []
        return instance(requestConf(config))
      }
    } catch (error) {
      console.error('refreshToken error =>', error)
      router.push({ path: '/login', replace: true }).then(() => { })
    } finally {
      refreshToking = false
    }
  } else {
    return new Promise((resolve) => {
      // 将resolve放进队列，用一个函数形式来保存，等token刷新后直接执行
      requests.push(() => {
        resolve(instance(requestConf(config)))
      })
    })
  }
}

/**
 * axios响应拦截器
 * @param config 请求配置
 * @param data response数据
 * @param status HTTP status
 * @param statusText HTTP status text
 * @returns {Promise<*|*>}
 */
const handleData = async ({ config, data, status, statusText, headers }) => {
  // Blob 响应类型直接返回，不做解析（用于文件下载）
  if (config.responseType === 'blob') {
    if (loadingInstance) loadingInstance.close()
    return { data, headers, status }
  }

  // Dify 接口返回标准 HTTP 状态码，无业务 code 字段，2xx 直接透传
  if (config.url && config.url.includes('/dify/') && status >= 200 && status < 300) {
    return data
  }

  if (Number(data.code) === 0) {
    Vue.prototype.$baseMessage(data.msg || data.data, 'error', 'vab-hey-message-error')
    if (data.msg === "用户已失效") {
      store
        .dispatch('user/resetAll')
        .then(() =>
          router.push({ path: '/login', replace: true }).then(() => { })
        )
    }
  }
  if (loadingInstance) loadingInstance.close()
  // 若data.code存在，覆盖默认code
  let code = data && data[statusName] ? data[statusName] : status
  // 若code属于操作正常code，则status修改为200
  if (codeVerificationArray.indexOf(data[statusName]) + 1) code = 200
  switch (code) {
    case 200:
      // 业务层级错误处理，以下是假定restful有一套统一输出格式(指不管成功与否都有相应的数据格式)情况下进行处理
      // 例如响应内容：
      // 错误内容：{ code: 1, msg: '非法参数' }
      // 正确内容：{ code: 200, data: {  }, msg: '操作正常' }
      return data
    // if (config.isShowAllData) {
    //   return {
    //     headers,
    //     data
    //   }
    // } else {
    //   // 处理大整数问题 - 在返回数据前检查并处理ID字段
    //   // 如果data字段存在且包含rows或total,直接返回data字段的内容
    //   // if (data && data.data && (data.data.rows || data.data.total)) {
    //   //   return fixBigIntegerFields(data.data)
    //   // }
    //   // return fixBigIntegerFields(data)
    // }
    case 401:
      store
        .dispatch('user/resetAll')
        .then(() =>
          router.push({ path: '/login', replace: true }).then(() => { })
        )
      break
    case 402:
      return await tryRefreshToken(config)
    case 403:
      router.push({ path: '/403' }).then(() => { })
      break
    case 408:
      return data
      break
  }
  // 异常处理
  // 若data.msg存在，覆盖默认提醒消息
  const errMsg = `${config.url} 后端接口 ${code} 异常：${data && data[messageName]
    ? data[messageName]
    : CODE_MESSAGE[code]
      ? CODE_MESSAGE[code]
      : statusText
    }`
  // 仅当调用方显式传 __silent500ToEmpty__: true 时，对该次请求的 500 静默处理
  // 由调用方在 .catch 中负责把 500 兜底为业务空数据；这里只阻止全局错误弹窗
  const isSilent500 = Number(code) === 500 && config && config.__silent500ToEmpty__
  // 是否显示高亮错误(与errorHandler钩子触发逻辑一致)
  if (needErrorLog() && !isSilent500) {
    Vue.prototype.$baseMessage(errMsg, 'error', 'vab-hey-message-error')
    addErrorLog({ message: errMsg, stack: data })
  }
  return Promise.reject(data)
}

/**
 * @description axios初始化
 */
const instance = axios.create({
  baseURL,
  timeout: requestTimeout,
  headers: {
    'Content-Type': contentType,
  },
})

/**
 * @description axios请求拦截器
 */
instance.interceptors.request.use(requestConf, (error) => {
  return Promise.reject(error)
})

/**
 * @description axios响应拦截器
 */
instance.interceptors.response.use(
  (response) => {
    // Blob 响应类型直接返回，不做任何处理（用于文件下载）
    if (response.config.responseType === 'blob') {
      if (loadingInstance) loadingInstance.close()
      return response
    }

    // 先尝试使用Mock数据拦截
    const mockData = tryMockData(response)
    if (mockData) {
      return mockData
    }
    return handleData(response)
  },
  (error) => {
    const { response, config } = error

    // Blob 响应类型的错误直接抛出，不做Mock处理
    if (config?.responseType === 'blob') {
      if (loadingInstance) loadingInstance.close()
      return Promise.reject(error)
    }

    // 请求失败时尝试返回Mock数据
    const mockData = tryMockDataOnError(config, error)
    if (mockData) {
      console.warn('[Mock数据] 接口请求失败，使用Mock数据:', config?.url)
      return mockData
    }

    if (response === undefined) {
      Vue.prototype.$baseMessage(
        '未可知错误，可能是因为后端不支持跨域CORS、接口地址不存在等问题引起',
        'error',
        'vab-hey-message-error'
      )
      return {}
    } else return handleData(response)
  }
)

/**
 * 尝试从响应中获取Mock数据
 * @param {Object} response axios响应对象
 * @returns {Object|null} Mock数据或null
 */
function tryMockData(response) {
  // 如果响应状态码是404或500，尝试返回Mock数据
  if (response && (response.status === 404 || response.status === 500)) {
    const url = response.config?.url
    if (url) {
      return getMockDataByUrl(url, response.config)
    }
  }
  return null
}

/**
 * 请求失败时尝试获取Mock数据
 * @param {Object} config axios请求配置
 * @param {Object} error 错误对象
 * @returns {Object|null} Mock数据或null
 */
function tryMockDataOnError(config, error) {
  const url = config?.url
  if (!url) return null

  // 对预算模块的接口返回Mock数据
  if (url.includes('/accountant/budget/') || url.includes('/accountant/integration/')) {
    return getMockDataByUrl(url, config)
  }

  return null
}

/**
 * 根据URL返回对应的Mock数据
 * @param {String} url 请求URL
 * @param {Object} config 请求配置
 * @returns {Object|null} Mock数据或null
 */
function getMockDataByUrl(url, config) {
  try {
    // 组织体系管理
    if (url.includes('/organization-structure/page')) {
      return mockOrganizationStructurePage(config?.data)
    }

    // 高级功能 - 统计数据
    if (url.includes('/advanced-features/stats') || url.includes('/accountant/advanced/stats')) {
      return mockFeatureStats()
    }

    // 高级功能 - 最近活动
    if (url.includes('/advanced-features/activities') || url.includes('/accountant/advanced/activities')) {
      return mockRecentActivities()
    }

    // 高级功能 - 系统通知
    if (url.includes('/advanced-features/notifications') || url.includes('/accountant/advanced/notifications')) {
      return mockNotifications()
    }

    // 执行监控 - 分页查询
    if (url.includes('/execution/page')) {
      return mockExecutionPage(config?.data)
    }

    // 执行监控 - 组织列表
    if (url.includes('/execution/organizations')) {
      return mockOrganizations()
    }

    // 执行监控 - 科目列表
    if (url.includes('/execution/accounts')) {
      return mockAccounts()
    }

    // 差异分析 - 分析数据
    if (url.includes('/analysis/variance')) {
      return mockVarianceAnalysis(config?.data)
    }

    // 差异分析 - 组织选项
    if (url.includes('/analysis/organizations')) {
      return mockOrganizations()
    }

    // 差异分析 - 科目选项
    if (url.includes('/analysis/budget-accounts')) {
      return mockAccounts()
    }

    // 差异分析 - 用户选项
    if (url.includes('/analysis/users')) {
      return mockUsers()
    }

    // 集成监控 - 状态概览
    if (url.includes('/integration/status/overview') || url.includes('/integration/status')) {
      return mockIntegrationStatus(config?.params)
    }

    // 集成监控 - 统计数据
    if (url.includes('/integration/stats')) {
      return mockIntegrationStats()
    }

    return null
  } catch (error) {
    console.error('[Mock数据] 获取Mock数据失败:', error)
    return null
  }
}

// 修复大整数字段函数
function fixBigIntegerFields(data) {
  if (!data || typeof data !== 'object') return data

  // 处理大整数ID字段
  if (data.code === 1 && data.data) {
    // 处理分页数据
    if (data.data.tlist && Array.isArray(data.data.tlist)) {
      data.data.tlist = data.data.tlist.map(item => processItem(item))
    }
    // 处理单个对象
    if (typeof data.data === 'object' && !Array.isArray(data.data) && data.data.tlist === undefined) {
      data.data = processItem(data.data)
    }
  }

  return data
}

// 处理单个对象中的大整数字段
function processItem(item) {
  if (!item || typeof item !== 'object') return item

  const bigIntFields = ['budgetId', 'centerId', 'BUDGETID', 'CENTERID', 'budget_id', 'center_id']
  const processedItem = { ...item }

  bigIntFields.forEach(field => {
    if (processedItem[field] !== undefined && processedItem[field] !== null) {
      // 记录原始值和类型
      const originalValue = processedItem[field]
      const originalType = typeof originalValue

      // 强制转为字符串
      processedItem[field] = String(originalValue)

      // 检测是否超出JavaScript安全范围
      if (originalType === 'number' && originalValue > Number.MAX_SAFE_INTEGER) {
        console.warn(`检测到大整数超出JavaScript安全范围: ${field} = ${originalValue}`)
      }
    }
  })

  return processedItem
}

export default instance
