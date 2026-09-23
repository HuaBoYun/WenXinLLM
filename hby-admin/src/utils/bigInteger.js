/**
 * 大整数处理工具类
 * 用于解决JavaScript长整数精度丢失问题
 *
 * @author 示例云AI助手
 * @date 2025-12-05
 */

/**
 * 大整数处理工具类
 */
export class BigIntegerUtil {

  /**
   * JavaScript安全整数最大值
   */
  static MAX_SAFE_INTEGER = Number.MAX_SAFE_INTEGER // 9007199254740991

  /**
   * JavaScript安全整数最小值
   */
  static MIN_SAFE_INTEGER = Number.MIN_SAFE_INTEGER // -9007199254740991

  /**
   * 检查数字是否超出JavaScript安全整数范围
   * @param {Number|String} num 要检查的数字
   * @returns {Boolean} 是否超出安全范围
   */
  static isUnsafeInteger(num) {
    if (num === null || num === undefined || num === '') {
      return false
    }

    try {
      const numStr = String(num).trim()
      const numValue = Number(numStr)

      // 检查是否为有效数字
      if (isNaN(numValue)) {
        return false
      }

      // 检查是否超出安全范围
      if (numValue > this.MAX_SAFE_INTEGER || numValue < this.MIN_SAFE_INTEGER) {
        return true
      }

      // 检查字符串转换是否一致（检测精度丢失）
      return numStr !== numValue.toString()
    } catch (error) {
      console.warn('检查大整数安全性时出错:', error)
      return true // 出错时视为不安全
    }
  }

  /**
   * 安全转换大整数为字符串
   * @param {Number|String} num 要转换的数字
   * @returns {String} 字符串形式的数字
   */
  static safeToString(num) {
    if (num === null || num === undefined) {
      return ''
    }

    try {
      return String(num)
    } catch (error) {
      console.warn('转换大整数为字符串时出错:', error)
      return ''
    }
  }

  /**
   * 安全比较两个大整数
   * @param {Number|String} a 第一个数字
   * @param {Number|String} b 第二个数字
   * @returns {Number} 比较结果：-1(a<b), 0(a==b), 1(a>b)
   */
  static safeCompare(a, b) {
    const aStr = this.safeToString(a)
    const bStr = this.safeToString(b)

    // 处理空值
    if (!aStr && !bStr) return 0
    if (!aStr) return -1
    if (!bStr) return 1

    try {
      // 如果都在安全范围内，直接比较
      const aNum = Number(aStr)
      const bNum = Number(bStr)

      if (!isNaN(aNum) && !isNaN(bNum) &&
          !this.isUnsafeInteger(aNum) && !this.isUnsafeInteger(bNum)) {
        return aNum > bNum ? 1 : (aNum < bNum ? -1 : 0)
      }

      // 大整数比较逻辑
      // 移除前导零和负号处理
      const aClean = aStr.replace(/^0+/, '') || '0'
      const bClean = bStr.replace(/^0+/, '') || '0'

      // 比较长度
      if (aClean.length !== bClean.length) {
        return aClean.length > bClean.length ? 1 : -1
      }

      // 逐位比较
      return aClean.localeCompare(bClean)
    } catch (error) {
      console.warn('比较大整数时出错:', error)
      return aStr.localeCompare(bStr)
    }
  }

  /**
   * 格式化显示大整数
   * @param {Number|String} num 要格式化的数字
   * @param {String} suffix 后缀，如'ID'
   * @returns {String} 格式化后的显示文本
   */
  static formatDisplay(num, suffix = '') {
    const numStr = this.safeToString(num)
    if (!numStr) {
      return suffix
    }

    try {
      // 如果超出安全范围，显示截断格式
      if (this.isUnsafeInteger(num)) {
        if (numStr.length > 12) {
          return `${numStr.slice(0, 8)}...${numStr.slice(-4)}${suffix}`
        }
      }

      return numStr + suffix
    } catch (error) {
      console.warn('格式化大整数显示时出错:', error)
      return numStr + suffix
    }
  }

  /**
   * 验证是否为有效的数字字符串
   * @param {String} numStr 数字字符串
   * @returns {Boolean} 是否为有效数字
   */
  static isValidNumberString(numStr) {
    if (numStr === null || numStr === undefined || numStr === '') {
      return false
    }

    const trimmedStr = String(numStr).trim()

    // 检查是否为纯数字（允许负号）
    return /^-?\d+$/.test(trimmedStr)
  }

  /**
   * 从对象中安全获取ID字段
   * 支持多种可能的字段名：budgetId, BUDGET_ID, BUDGETID等
   * @param {Object} obj 源对象
   * @param {Array<String>} possibleFields 可能的字段名数组
   * @returns {String} 字符串形式的ID
   */
  static safeGetId(obj, possibleFields = ['id', 'Id', 'ID']) {
    if (!obj || typeof obj !== 'object') {
      return ''
    }

    for (const field of possibleFields) {
      if (obj.hasOwnProperty(field) && obj[field] != null) {
        return this.safeToString(obj[field])
      }
    }

    return ''
  }

  /**
   * 检查并记录大整数警告
   * @param {Number|String} num 要检查的数字
   * @param {String} context 上下文信息
   */
  static checkAndLogWarning(num, context = '') {
    if (this.isUnsafeInteger(num)) {
      const numStr = this.safeToString(num)
      console.warn(`[大整数精度警告] ${context}: 检测到超出JavaScript安全范围的整数 ${numStr}`)
      return true
    }
    return false
  }

  /**
   * 批量处理对象数组中的ID字段
   * @param {Array<Object>} list 对象数组
   * @param {Array<String>} idFields 需要处理的ID字段名
   * @returns {Array<Object>} 处理后的数组
   */
  static batchProcessIds(list, idFields = ['id', 'Id', 'ID']) {
    if (!Array.isArray(list)) {
      return list
    }

    return list.map(item => {
      if (!item || typeof item !== 'object') {
        return item
      }

      const processedItem = { ...item }

      // 处理每个ID字段
      idFields.forEach(fieldName => {
        if (processedItem.hasOwnProperty(fieldName)) {
          processedItem[fieldName] = this.safeToString(processedItem[fieldName])
        }
      })

      return processedItem
    })
  }
}

/**
 * 默认导出工具类实例
 */
export default BigIntegerUtil