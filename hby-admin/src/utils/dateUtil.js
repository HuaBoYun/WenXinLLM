/**
 * 日期时间工具类
 * 提供日期时间格式化、解析等常用功能
 */

/**
 * 格式化日期时间
 * @param {Date|String|Number} date 日期对象、日期字符串或时间戳
 * @param {String} format 格式化模板，默认 'YYYY-MM-DD HH:mm:ss'
 * @returns {String} 格式化后的日期时间字符串
 */
export function formatDateTime(date, format = 'YYYY-MM-DD HH:mm:ss') {
  if (!date) return ''
  
  // 转换为Date对象
  let dateObj
  if (date instanceof Date) {
    dateObj = date
  } else if (typeof date === 'string') {
    dateObj = new Date(date)
  } else if (typeof date === 'number') {
    dateObj = new Date(date)
  } else {
    return ''
  }
  
  // 检查日期是否有效
  if (isNaN(dateObj.getTime())) {
    return ''
  }
  
  const year = dateObj.getFullYear()
  const month = String(dateObj.getMonth() + 1).padStart(2, '0')
  const day = String(dateObj.getDate()).padStart(2, '0')
  const hours = String(dateObj.getHours()).padStart(2, '0')
  const minutes = String(dateObj.getMinutes()).padStart(2, '0')
  const seconds = String(dateObj.getSeconds()).padStart(2, '0')
  
  return format
    .replace('YYYY', year)
    .replace('MM', month)
    .replace('DD', day)
    .replace('HH', hours)
    .replace('mm', minutes)
    .replace('ss', seconds)
}

/**
 * 格式化日期（不含时间）
 * @param {Date|String|Number} date 日期
 * @returns {String} 格式化后的日期字符串 YYYY-MM-DD
 */
export function formatDate(date) {
  return formatDateTime(date, 'YYYY-MM-DD')
}

/**
 * 格式化时间（不含日期）
 * @param {Date|String|Number} date 日期时间
 * @returns {String} 格式化后的时间字符串 HH:mm:ss
 */
export function formatTime(date) {
  return formatDateTime(date, 'HH:mm:ss')
}

/**
 * 解析日期字符串为Date对象
 * @param {String} dateStr 日期字符串
 * @returns {Date|null} Date对象或null
 */
export function parseDate(dateStr) {
  if (!dateStr) return null
  const date = new Date(dateStr)
  return isNaN(date.getTime()) ? null : date
}

/**
 * 获取当前日期时间字符串
 * @param {String} format 格式化模板
 * @returns {String} 当前日期时间字符串
 */
export function now(format = 'YYYY-MM-DD HH:mm:ss') {
  return formatDateTime(new Date(), format)
}

/**
 * 获取今天的日期字符串
 * @returns {String} 今天的日期 YYYY-MM-DD
 */
export function today() {
  return formatDate(new Date())
}

/**
 * 计算两个日期之间的天数差
 * @param {Date|String} date1 日期1
 * @param {Date|String} date2 日期2
 * @returns {Number} 天数差
 */
export function daysBetween(date1, date2) {
  const d1 = parseDate(date1)
  const d2 = parseDate(date2)
  if (!d1 || !d2) return 0
  
  const diffTime = Math.abs(d2.getTime() - d1.getTime())
  return Math.ceil(diffTime / (1000 * 60 * 60 * 24))
}

/**
 * 添加天数
 * @param {Date|String} date 日期
 * @param {Number} days 要添加的天数（可以为负数）
 * @returns {Date} 新的日期对象
 */
export function addDays(date, days) {
  const d = parseDate(date) || new Date()
  d.setDate(d.getDate() + days)
  return d
}

/**
 * 添加月份
 * @param {Date|String} date 日期
 * @param {Number} months 要添加的月份（可以为负数）
 * @returns {Date} 新的日期对象
 */
export function addMonths(date, months) {
  const d = parseDate(date) || new Date()
  d.setMonth(d.getMonth() + months)
  return d
}

/**
 * 获取月份的第一天
 * @param {Date|String} date 日期
 * @returns {Date} 月份第一天的日期对象
 */
export function getMonthStart(date) {
  const d = parseDate(date) || new Date()
  return new Date(d.getFullYear(), d.getMonth(), 1)
}

/**
 * 获取月份的最后一天
 * @param {Date|String} date 日期
 * @returns {Date} 月份最后一天的日期对象
 */
export function getMonthEnd(date) {
  const d = parseDate(date) || new Date()
  return new Date(d.getFullYear(), d.getMonth() + 1, 0)
}

