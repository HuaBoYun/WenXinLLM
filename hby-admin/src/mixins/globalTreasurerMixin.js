/**
 * 全球司库系统通用混入
 * 提供错误处理和模拟数据功能
 */

import MockDataNotice from '@/components/GlobalTreasurer/MockDataNotice'

export default {
  components: {
    MockDataNotice
  },
  data() {
    return {
      // 是否使用模拟数据
      useMockData: false,
      // 网络错误重试次数
      retryCount: 0,
      maxRetryCount: 2
    }
  },

  methods: {
    /**
     * 通用API调用方法，包含错误处理和模拟数据回退
     * @param {Function} apiCall - API调用函数
     * @param {Function} mockDataGenerator - 模拟数据生成函数
     * @param {String} errorMessage - 错误提示信息
     * @returns {Promise} API响应或模拟数据
     */
    async callApiWithFallback(apiCall, mockDataGenerator, errorMessage = '获取数据失败') {
      try {
        const response = await apiCall()
        const successCodes = [200, 0, '200', '0', '1', 1, 2]
        
        if (successCodes.includes(response.code)) {
          this.retryCount = 0 // 重置重试次数
          return response
        } else {
          throw new Error(response.message || '服务器返回错误')
        }
      } catch (error) {
        console.warn(`API调用失败: ${error.message}`)
        
        // 如果是网络错误且未达到最大重试次数，则使用模拟数据
        if (this.retryCount < this.maxRetryCount && this.isNetworkError(error)) {
          this.retryCount++
          console.log(`使用模拟数据 (重试 ${this.retryCount}/${this.maxRetryCount})`)
        }
        
        // 使用模拟数据
        if (mockDataGenerator && typeof mockDataGenerator === 'function') {
          this.useMockData = true
          const mockResponse = mockDataGenerator()
          
          // 显示友好的提示信息（只在第一次显示时提醒）
          if (!this.useMockData) {
            this.$message.warning('网络连接异常，已切换到模拟数据模式')
          }
          
          return {
            code: 200,
            message: '成功',
            data: mockResponse
          }
        } else {
          this.$message.error(errorMessage)
          throw error
        }
      }
    },

    /**
     * 判断是否为网络错误
     * @param {Error} error - 错误对象
     * @returns {Boolean} 是否为网络错误
     */
    isNetworkError(error) {
      if (!error) return false

      const message = error.message || ''
      const errorMsg = String(error).toLowerCase()

      return message.includes('Network Error') ||
             message.includes('timeout') ||
             message.includes('ECONNREFUSED') ||
             errorMsg.includes('network') ||
             errorMsg.includes('timeout') ||
             error.code === 'NETWORK_ERROR'
    },

    /**
     * 格式化金额显示
     * @param {Number} amount - 金额
     * @returns {String} 格式化后的金额
     */
    formatAmount(amount) {
      if (!amount && amount !== 0) return '0.00'
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },

    /**
     * 格式化日期显示
     * @param {String|Date} date - 日期
     * @returns {String} 格式化后的日期
     */
    formatDate(date) {
      if (!date) return '-'
      const d = new Date(date)
      return d.toLocaleDateString('zh-CN')
    },

    /**
     * 格式化日期时间显示
     * @param {String|Date} datetime - 日期时间
     * @returns {String} 格式化后的日期时间
     */
    formatDateTime(datetime) {
      if (!datetime) return '-'
      const d = new Date(datetime)
      return d.toLocaleString('zh-CN')
    },

    /**
     * 生成随机ID
     * @returns {String} 随机ID
     */
    generateId() {
      return Date.now().toString(36) + Math.random().toString(36).substr(2)
    },

    /**
     * 生成随机金额
     * @param {Number} min - 最小值
     * @param {Number} max - 最大值
     * @returns {Number} 随机金额
     */
    generateRandomAmount(min = 1000, max = 1000000) {
      return Math.floor(Math.random() * (max - min + 1)) + min
    },

    /**
     * 生成随机日期
     * @param {Number} daysAgo - 几天前
     * @returns {String} 随机日期
     */
    generateRandomDate(daysAgo = 30) {
      const date = new Date()
      date.setDate(date.getDate() - Math.floor(Math.random() * daysAgo))
      return date.toISOString().split('T')[0]
    },

    /**
     * 生成随机状态
     * @param {Array} statuses - 状态数组
     * @returns {String} 随机状态
     */
    generateRandomStatus(statuses = ['PENDING', 'APPROVED', 'COMPLETED']) {
      return statuses[Math.floor(Math.random() * statuses.length)]
    }
  },

  created() {
    // 检查是否强制使用模拟数据（开发环境）
    if (process.env.NODE_ENV === 'development' && process.env.VUE_APP_USE_MOCK === 'true') {
      this.useMockData = true
    }
  }
}
