import store from '@/store'
import axios from 'axios'
//对象数组去重复
export function deWeight(arr) {
  for (var i = 0; i < arr.length - 1; i++) {
    for (var j = i + 1; j < arr.length; j++) {
      if (arr[i].id == arr[j].id) {
        arr.splice(j, 1);
        //因为数组长度减小1，所以直接 j++ 会漏掉一个元素，所以要 j--
        j--;
      }
    }
  }
  return arr;
}

//对象数组去重复(staffid)
export function deWeight2(arr) {
  for (var i = 0; i < arr.length - 1; i++) {
    for (var j = i + 1; j < arr.length; j++) {
      if (arr[i].staffid == arr[j].staffid) {
        arr.splice(j, 1)
        //因为数组长度减小1，所以直接 j++ 会漏掉一个元素，所以要 j--
        j--
      }
    }
  }
  return arr
}

/**
 * @description 格式化时间
 * @param time
 * @param cFormat
 * @returns {string|null}
 */
export function parseTime(time, cFormat) {
  if (arguments.length === 0) {
    return null
  }
  const format = cFormat || '{y}-{m}-{d} {h}:{i}:{s}'
  let date
  if (typeof time === 'object') {
    date = time
  } else {
    if (typeof time === 'string' && /^[0-9]+$/.test(time)) {
      time = parseInt(time)
    }
    if (typeof time === 'number' && time.toString().length === 10) {
      time = time * 1000
    }
    date = new Date(time)
  }
  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay(),
  }
  return format.replace(/{([ymdhisa])+}/g, (result, key) => {
    let value = formatObj[key]
    if (key === 'a') {
      return ['日', '一', '二', '三', '四', '五', '六'][value]
    }
    if (result.length > 0 && value < 10) {
      value = '0' + value
    }
    return value || 0
  })
}

/**
 * @description 格式化时间
 * @param time
 * @param option
 * @returns {string}
 */
export function formatTime(time, option) {
  if (('' + time).length === 10) {
    time = parseInt(time) * 1000
  } else {
    time = +time
  }
  const d = new Date(time)
  const now = Date.now()

  const diff = (now - d) / 1000

  if (diff < 30) {
    return '刚刚'
  } else if (diff < 3600) {
    // less 1 hour
    return Math.ceil(diff / 60) + '分钟前'
  } else if (diff < 3600 * 24) {
    return Math.ceil(diff / 3600) + '小时前'
  } else if (diff < 3600 * 24 * 2) {
    return '1天前'
  }
  if (option) {
    return parseTime(time, option)
  } else {
    return (
      d.getMonth() +
      1 +
      '月' +
      d.getDate() +
      '日' +
      d.getHours() +
      '时' +
      d.getMinutes() +
      '分'
    )
  }
}

/**
 * @description 格式化日期
 * @param ticellValueme
 * @returns {string}
 * 到秒
 */
export function formatDate(cellValue) {
  if (cellValue == null || cellValue == '') return ''
  var date = new Date(cellValue)
  var year = date.getFullYear()
  var month =
    date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1
  var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate()
  var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours()
  var minutes =
    date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()
  var seconds =
    date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds()
  return (
    year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds
  )
}

/**
 * @description 格式化日期
 * @param ticellValueme
 * @returns {string}
 * 到天
 */
export function formatDay(cellValue) {
  if (cellValue == null || cellValue == '') return ''
  var date = new Date(cellValue)
  var year = date.getFullYear()
  var month =
    date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1
  var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate()
  return year + '-' + month + '-' + day
}

export function formatYear(cellValue) {
  if (cellValue == null || cellValue == '') return ''
  var date = new Date(cellValue)
  var year = date.getFullYear()
  var month =
    date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1
  var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate()
  return year
}

/**
 * @description 将url请求参数转为json格式
 * @param url
 * @returns {{}|any}
 */
export function paramObj(url) {
  const search = url.split('?')[1]
  if (!search) {
    return {}
  }
  return JSON.parse(
    '{"' +
    decodeURIComponent(search)
      .replace(/"/g, '\\"')
      .replace(/&/g, '","')
      .replace(/=/g, '":"')
      .replace(/\+/g, ' ') +
    '"}'
  )
}

/**
 * @description 父子关系的数组转换成树形结构数据
 * @param data
 * @returns {*}
 */
export function translateDataToTree(data) {
  const parent = data.filter(
    (value) => value.parentId === 'undefined' || value.parentId === null
  )
  const children = data.filter(
    (value) => value.parentId !== 'undefined' && value.parentId !== null
  )
  const translator = (parent, children) => {
    parent.forEach((parent) => {
      children.forEach((current, index) => {
        if (current.parentId === parent.id) {
          const temp = JSON.parse(JSON.stringify(children))
          temp.splice(index, 1)
          translator([current], temp)
          typeof parent.children !== 'undefined'
            ? parent.children.push(current)
            : (parent.children = [current])
        }
      })
    })
  }
  translator(parent, children)
  return parent
}

/**
 * @description 树形结构数据转换成父子关系的数组
 * @param data
 * @returns {[]}
 */
export function translateTreeToData(data) {
  const result = []
  data.forEach((item) => {
    const loop = (data) => {
      result.push({
        id: data.id,
        name: data.name,
        parentId: data.parentId,
      })
      const child = data.children
      if (child) {
        for (let i = 0; i < child.length; i++) {
          loop(child[i])
        }
      }
    }
    loop(item)
  })
  return result
}

/**
 * @description 10位时间戳转换
 * @param time
 * @returns {string}
 */
export function tenBitTimestamp(time) {
  const date = new Date(time * 1000)
  const y = date.getFullYear()
  let m = date.getMonth() + 1
  m = m < 10 ? '' + m : m
  let d = date.getDate()
  d = d < 10 ? '' + d : d
  let h = date.getHours()
  h = h < 10 ? '0' + h : h
  let minute = date.getMinutes()
  let second = date.getSeconds()
  minute = minute < 10 ? '0' + minute : minute
  second = second < 10 ? '0' + second : second
  return y + '年' + m + '月' + d + '日 ' + h + ':' + minute + ':' + second //组合
}

/**
 * @description 13位时间戳转换
 * @param time
 * @returns {string}
 */
export function thirteenBitTimestamp(time) {
  const date = new Date(time / 1)
  const y = date.getFullYear()
  let m = date.getMonth() + 1
  m = m < 10 ? '' + m : m
  let d = date.getDate()
  d = d < 10 ? '' + d : d
  let h = date.getHours()
  h = h < 10 ? '0' + h : h
  let minute = date.getMinutes()
  let second = date.getSeconds()
  minute = minute < 10 ? '0' + minute : minute
  second = second < 10 ? '0' + second : second
  return y + '年' + m + '月' + d + '日 ' + h + ':' + minute + ':' + second //组合
}
/**
 * @description 13位时间戳转换
 * @param time
 * @returns {string}
 */
export function thirteenBitTimestampForYear(time) {
  const date = new Date(time / 1)
  const y = date.getFullYear()
  let m = date.getMonth() + 1
  m = m < 10 ? '' + m : m
  let d = date.getDate()
  d = d < 10 ? '' + d : d
  let h = date.getHours()
  h = h < 10 ? '0' + h : h
  let minute = date.getMinutes()
  let second = date.getSeconds()
  minute = minute < 10 ? '0' + minute : minute
  second = second < 10 ? '0' + second : second
  return y + '年'
}

/**
 * @description 获取随机id
 * @param length
 * @returns {string}
 */
export function uuid(length = 32) {
  const num = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890'
  let str = ''
  for (let i = 0; i < length; i++) {
    str += num.charAt(Math.floor(Math.random() * num.length))
  }
  return str
}

/**
 * @description m到n的随机数
 * @param m
 * @param n
 * @returns {number}
 */
export function random(m, n) {
  return Math.floor(Math.random() * (m - n) + n)
}

/**
 * @description addEventListener
 * @type {function(...[*]=)}
 */
export const on = (function () {
  return function (element, event, handler, useCapture = false) {
    if (element && event && handler) {
      element.addEventListener(event, handler, useCapture)
    }
  }
})()

/**
 * @description removeEventListener
 * @type {function(...[*]=)}
 */
export const off = (function () {
  return function (element, event, handler, useCapture = false) {
    if (element && event) {
      element.removeEventListener(event, handler, useCapture)
    }
  }
})()

/**
 * @description 数组打乱
 * @param array
 * @returns {*}
 */
export function shuffle(array) {
  let m = array.length,
    t,
    i
  while (m) {
    i = Math.floor(Math.random() * m--)
    t = array[m]
    array[m] = array[i]
    array[i] = t
  }
  return array
}

/**
 * @description 数字转中文数码
 *
 * @param {Number|String}   num     数字[正整数]
 * @param {String}          type    文本类型，lower|upper，默认upper
 *
 * @example number2text(100000000) => "壹亿元整"
 */
export const number2text = (number, type = 'upper') => {
  // 配置
  const confs = {
    lower: {
      num: ['零', '一', '二', '三', '四', '五', '六', '七', '八', '九'],
      unit: ['', '十', '百', '千', '万'],
      level: ['', '万', '亿'],
    },
    upper: {
      num: ['零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'],
      unit: ['', '拾', '佰', '仟'],
      level: ['', '万', '亿'],
    },
    decimal: {
      unit: ['分', '角'],
    },
    maxNumber: 999999999999.99,
  }

  // 过滤不合法参数
  if (Number(number) > confs.maxNumber) {
    console.error(
      `The maxNumber is ${confs.maxNumber}. ${number} is bigger than it!`
    )
    return false
  }

  const conf = confs[type]
  const numbers = String(Number(number).toFixed(2)).split('.')
  const integer = numbers[0].split('')
  const decimal = Number(numbers[1]) === 0 ? [] : numbers[1].split('')

  // 四位分级
  const levels = integer.reverse().reduce((pre, item, idx) => {
    let level = pre[0] && pre[0].length < 4 ? pre[0] : []
    let value =
      item === '0' ? conf.num[item] : conf.num[item] + conf.unit[idx % 4]
    level.unshift(value)

    if (level.length === 1) {
      pre.unshift(level)
    } else {
      pre[0] = level
    }

    return pre
  }, [])

  // 整数部分
  const _integer = levels.reduce((pre, item, idx) => {
    let _level = conf.level[levels.length - idx - 1]
    let _item = item.join('').replace(/(零)\1+/g, '$1') // 连续多个零字的部分设置为单个零字

    // 如果这一级只有一个零字，则去掉这级
    if (_item === '零') {
      _item = ''
      _level = ''

      // 否则如果末尾为零字，则去掉这个零字
    } else if (_item[_item.length - 1] === '零') {
      _item = _item.slice(0, _item.length - 1)
    }

    return pre + _item + _level ? pre + _item + _level : '零'
  }, '')

  // 小数部分
  let _decimal = decimal
    .map((item, idx) => {
      const unit = confs.decimal.unit
      const _unit = item !== '0' ? unit[unit.length - idx - 1] : ''

      return `${conf.num[item]}${_unit}`
    })
    .join('')

  // 如果是整数，则补个整字
  return `${_integer}元` + (_decimal || '整')
}

export function loadFile(name) {
  // name为文件所在位置
  const xhr = new XMLHttpRequest()
  const okStatus = document.location.protocol === 'file:' ? 0 : 200
  xhr.open('GET', name, false)
  xhr.overrideMimeType('text/html;charset=utf-8') // 默认为utf-8
  xhr.send(null)
  return xhr.status === okStatus ? xhr.responseText : null
}

/**
 * 判断权限
 * @param key
 * @returns {boolean}
 */
export function hasAuth(key) {
  const authsKeys = store.getters['routes/authsKeys']
  return authsKeys.indexOf(key) !== -1
}

// 判断密级获取返回值
export function hasMJ(key) {
  const levelKeys = store.getters['routes/levelKeys']
  return levelKeys.filter(res => res.name === key)
}
// 判断密级获取返回值
export function couldMJ(key) {
  const info = localStorage.getItem('userInfo')
  const userInfo = JSON.parse(info)
  return userInfo.currentOrg.useSecrect === 1
}
//日期格式化
export function UTCformat(utc) {
  if (!utc) {
    return
  }
  let date = new Date(utc),
    year = date.getFullYear(),
    month =
      date.getMonth() + 1 > 9
        ? date.getMonth() + 1
        : '0' + parseInt(date.getMonth() + 1),
    day = date.getDate() > 9 ? date.getDate() : '0' + date.getDate()
  let res = year + '-' + month + '-' + day
  return res
}

//防抖实现
export function debounce(fn, delay) {
  let timer = null
  return function () {  //借助闭包
    let th = this;
    let args = arguments;
    if (timer) {
      clearTimeout(timer)
    }
    timer = setTimeout(() => {
      fn.apply(th, args)
    }, delay)
  }
}

export function polling(type, url, delay) {
  return new Promise((resolve, reject) => {
    axios({
      url: url,
      method: type,
      headers: { 'Content-Type': 'application/json' },
    }).then(res => {
      if (res.data.status != 'SUCCESS' || res.data.result.code != 0) {
        setTimeout(() => {
          resolve(polling(type, url, delay));
        }, delay)
      } else {
        resolve(res);
      }
    })
  })
}

/**
 * @description 格式化金额
 * @param {Number|String} money 金额
 * @param {Number} decimals 小数位数，默认2位
 * @param {String} decimalPoint 小数点符号，默认'.'
 * @param {String} thousandsSep 千分位分隔符，默认','
 * @returns {String} 格式化后的金额
 */
export function formatMoney(money, decimals = 2, decimalPoint = '.', thousandsSep = ',') {
  if (money === null || money === undefined || money === '') {
    return '0.00'
  }

  const num = parseFloat(money)
  if (isNaN(num)) {
    return '0.00'
  }

  const fixedNum = num.toFixed(decimals)
  const parts = fixedNum.split('.')

  // 添加千分位分隔符
  parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, thousandsSep)

  return parts.join(decimalPoint)
}

/**
 * @description 格式化货币（别名函数）
 * @param {Number|String} amount 金额
 * @param {String} currency 货币代码，可选
 * @returns {String} 格式化后的货币
 */
export function formatCurrency(amount, currency = '') {
  if (amount === null || amount === undefined || amount === '') {
    return currency ? `${currency} 0.00` : '0.00'
  }

  const num = parseFloat(amount)
  if (isNaN(num)) {
    return currency ? `${currency} 0.00` : '0.00'
  }

  // 格式化为千分位
  const formattedAmount = num.toLocaleString('zh-CN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  })

  return currency ? `${currency} ${formattedAmount}` : formattedAmount
}
