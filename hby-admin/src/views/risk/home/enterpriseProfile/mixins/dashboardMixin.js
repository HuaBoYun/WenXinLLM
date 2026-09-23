/**
 * 企业画像大屏通用混入
 * @author AI Agent
 * @since 2025-01-21
 */
import * as enterpriseProfileApi from '@/api/risk/enterpriseProfile'

export default {
  data() {
    return {
      // API接口
      $api: {
        enterpriseProfile: enterpriseProfileApi
      }
    }
  },
  methods: {
    /**
     * 格式化数字
     * @param {Number} num 数字
     * @param {Number} decimals 小数位数
     * @returns {String} 格式化后的字符串
     */
    formatNumber(num, decimals = 2) {
      if (num === null || num === undefined || isNaN(num)) {
        return '0'
      }
      return Number(num).toLocaleString('zh-CN', {
        minimumFractionDigits: decimals,
        maximumFractionDigits: decimals
      })
    },

    /**
     * 格式化百分比
     * @param {Number} num 数字
     * @param {Number} decimals 小数位数
     * @returns {String} 格式化后的百分比字符串
     */
    formatPercent(num, decimals = 2) {
      if (num === null || num === undefined || isNaN(num)) {
        return '0%'
      }
      return (Number(num)).toFixed(decimals) + '%'
    },

    /**
     * 格式化日期
     * @param {String|Date} date 日期
     * @param {String} format 格式
     * @returns {String} 格式化后的日期字符串
     */
    formatDate(date, format = 'YYYY-MM-DD HH:mm:ss') {
      if (!date) return ''
      
      const d = new Date(date)
      if (isNaN(d.getTime())) return ''
      
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      const hours = String(d.getHours()).padStart(2, '0')
      const minutes = String(d.getMinutes()).padStart(2, '0')
      const seconds = String(d.getSeconds()).padStart(2, '0')
      
      return format
        .replace('YYYY', year)
        .replace('MM', month)
        .replace('DD', day)
        .replace('HH', hours)
        .replace('mm', minutes)
        .replace('ss', seconds)
    },

    /**
     * 获取状态颜色
     * @param {String} status 状态
     * @returns {String} 颜色值
     */
    getStatusColor(status) {
      const colorMap = {
        'ACTIVE': '#67C23A',
        'INACTIVE': '#F56C6C',
        'PENDING': '#E6A23C',
        'COMPLETED': '#409EFF',
        'HIGH': '#F56C6C',
        'MEDIUM': '#E6A23C',
        'LOW': '#67C23A',
        'UP': '#67C23A',
        'DOWN': '#F56C6C',
        'STABLE': '#909399'
      }
      return colorMap[status] || '#909399'
    },

    /**
     * 获取状态文本
     * @param {String} status 状态
     * @returns {String} 状态文本
     */
    getStatusText(status) {
      const textMap = {
        'ACTIVE': '正常',
        'INACTIVE': '停用',
        'PENDING': '待处理',
        'COMPLETED': '已完成',
        'HIGH': '高',
        'MEDIUM': '中',
        'LOW': '低',
        'UP': '上升',
        'DOWN': '下降',
        'STABLE': '稳定'
      }
      return textMap[status] || status
    },

    /**
     * 防抖函数
     * @param {Function} func 要防抖的函数
     * @param {Number} delay 延迟时间
     * @returns {Function} 防抖后的函数
     */
    debounce(func, delay = 300) {
      let timeoutId
      return function (...args) {
        clearTimeout(timeoutId)
        timeoutId = setTimeout(() => func.apply(this, args), delay)
      }
    },

    /**
     * 节流函数
     * @param {Function} func 要节流的函数
     * @param {Number} delay 延迟时间
     * @returns {Function} 节流后的函数
     */
    throttle(func, delay = 300) {
      let lastCall = 0
      return function (...args) {
        const now = Date.now()
        if (now - lastCall >= delay) {
          lastCall = now
          return func.apply(this, args)
        }
      }
    },

    /**
     * 深拷贝对象
     * @param {Object} obj 要拷贝的对象
     * @returns {Object} 拷贝后的对象
     */
    deepClone(obj) {
      if (obj === null || typeof obj !== 'object') {
        return obj
      }
      
      if (obj instanceof Date) {
        return new Date(obj.getTime())
      }
      
      if (obj instanceof Array) {
        return obj.map(item => this.deepClone(item))
      }
      
      if (typeof obj === 'object') {
        const clonedObj = {}
        for (const key in obj) {
          if (obj.hasOwnProperty(key)) {
            clonedObj[key] = this.deepClone(obj[key])
          }
        }
        return clonedObj
      }
    },

    /**
     * 生成随机ID
     * @param {Number} length ID长度
     * @returns {String} 随机ID
     */
    generateId(length = 8) {
      const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789'
      let result = ''
      for (let i = 0; i < length; i++) {
        result += chars.charAt(Math.floor(Math.random() * chars.length))
      }
      return result
    },

    /**
     * 处理API错误
     * @param {Error} error 错误对象
     * @param {String} defaultMessage 默认错误消息
     */
    handleApiError(error, defaultMessage = '操作失败') {
      console.error('API Error:', error)
      
      let message = defaultMessage
      if (error.response && error.response.data && error.response.data.msg) {
        message = error.response.data.msg
      } else if (error.message) {
        message = error.message
      }
      
      this.$message.error(message)
    },

    /**
     * 显示加载提示
     * @param {String} text 提示文本
     * @returns {Object} loading实例
     */
    showLoading(text = '加载中...') {
      return this.$loading({
        lock: true,
        text: text,
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
    },

    /**
     * 确认对话框
     * @param {String} message 确认消息
     * @param {String} title 标题
     * @returns {Promise} Promise对象
     */
    confirmDialog(message, title = '确认') {
      return this.$confirm(message, title, {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
    }
  }
}
