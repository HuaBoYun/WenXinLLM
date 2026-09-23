import { getControlByRightId, validateIndicator } from '@/api/indicatorControl'
import { Message, MessageBox } from 'element-ui'
import { successCode } from '@/config'

/** 判断响应码是否属于成功码（兼容 number / string） */
const _successCodes = Array.isArray(successCode) ? successCode : [successCode]
function isSuccess(code) {
  return _successCodes.indexOf(code) !== -1
}

/**
 * 检查是否需要指标控制
 * @param {String} rightId - 当前页面的 rightId
 * @param {String} operationType - 操作类型 CREATE/UPDATE/DELETE
 * @param {Object} formData - 表单数据
 * @returns {Promise<{needControl: boolean, canProceed: boolean, controlType?: string}>}
 */
export async function checkIndicatorControl(rightId, operationType, formData) {
  try {
    if (!rightId) {
      return { needControl: false, canProceed: true }
    }

    console.log(`[IndicatorControl] checkIndicatorControl: rightId=${rightId}, opType=${operationType}`)

    const configRes = await getControlByRightId(rightId, operationType)
    console.log('[IndicatorControl] configRes:', JSON.stringify(configRes))

    // 兼容两种返回格式：
    // 1. { code:1, data: { needControl, configs } }  — 标准 JsonBean
    // 2. { needControl, configs }                     — 直接返回 data 层
    const configData = configRes.data || configRes
    if (!configData || !configData.needControl) {
      console.log('[IndicatorControl] 该页面无控制配置，放行')
      return { needControl: false, canProceed: true }
    }

    const configs = configData.configs || []
    console.log(`[IndicatorControl] 命中 ${configs.length} 条控制配置`)

    for (const config of configs) {
      const params = buildParams(formData, config.fieldMapping)

      const validateRes = await validateIndicator({
        modelId: config.modelId,
        params: params
      })
      console.log('[IndicatorControl] validateRes:', JSON.stringify(validateRes))

      // 判断校验是否通过：
      // 1. 响应码不在成功码列表中 → 模型执行失败，fail-open 放行
      // 2. 响应码成功 → 用模型返回数据与阈值比较决定是否通过
      const resData = validateRes.data || validateRes
      const codeOk = isSuccess(validateRes.code)

      if (!codeOk || !resData.success) {
        // 模型执行本身失败，放行（fail-open）
        console.warn('[IndicatorControl] 模型执行失败，放行')
        continue
      }

      // 从模型返回的 data 数组中提取校验值，与阈值比较
      const evalResult = evaluateModelResult(resData, config.thresholdValue)
      console.log(`[IndicatorControl] 模型结果判定: mode=${evalResult.mode}, pass=${evalResult.pass}, value=${evalResult.value}, threshold=${evalResult.threshold}`)

      if (!evalResult.pass) {
        // 构建详细的提示信息
        // 优先使用 SQL 结果行中的 MESSAGE 列，其次用后端顶层 message
        let serverMsg = resData.message
        if (Array.isArray(resData.data) && resData.data.length > 0) {
          const firstRow = resData.data[0]
          const rowMsg = firstRow.MESSAGE || firstRow.message || firstRow.Message
          if (rowMsg) serverMsg = rowMsg
        }
        const detailMsg = buildControlMessage(serverMsg, evalResult, config)
        if (config.controlLevel === 'RIGID') {
          await MessageBox.alert(detailMsg, '操作被拦截', {
            dangerouslyUseHTMLString: true,
            type: 'error',
            confirmButtonText: '我知道了'
          }).catch(() => {})
          return { needControl: true, canProceed: false, controlType: 'RIGID' }
        } else {
          const confirmed = await MessageBox.confirm(detailMsg,
            '风险提示',
            {
              dangerouslyUseHTMLString: true,
              confirmButtonText: '确认继续',
              cancelButtonText: '取消操作',
              type: 'warning'
            }
          ).catch(() => false)
          return { needControl: true, canProceed: !!confirmed, controlType: 'FLEXIBLE' }
        }
      }
    }

    return { needControl: true, canProceed: true }
  } catch (error) {
    console.error('[IndicatorControl] 检查失败，放行:', error)
    return { needControl: false, canProceed: true, error: error.message }
  }
}

/**
 * 构建传给指标模型的参数
 *
 * fieldMapping 配置示例（JSON 字符串或对象）：
 *   { "COMPANY_ID": "companyId", "AMOUNT": "totalAmount" }
 *   key = 模型 SQL 中的 ${参数名}
 *   value = 表单字段名
 *
 * 如果 fieldMapping 为空，则直接把表单数据全量传给模型，
 * 后端会用 key 匹配 ${参数名} 进行替换。
 */
function buildParams(formData, fieldMapping) {
  if (!formData || typeof formData !== 'object') return {}

  // 无映射配置 → 全量传递
  if (!fieldMapping || (typeof fieldMapping === 'string' && fieldMapping.trim() === '')) {
    const params = {}
    Object.keys(formData).forEach(key => {
      const val = formData[key]
      // 跳过复杂对象和数组，只传基本类型
      // null/undefined 转为空串，确保后端能匹配到 ${参数名} 进行替换
      if (val === null || val === undefined) {
        params[key] = ''
      } else if (typeof val !== 'object') {
        params[key] = val
      }
    })
    console.log('[IndicatorControl] buildParams(全量): keys=', Object.keys(params))
    return params
  }

  // 有映射配置 → 按映射提取
  const mapping = typeof fieldMapping === 'string' ? JSON.parse(fieldMapping) : fieldMapping
  const params = {}
  Object.keys(mapping).forEach(modelParamName => {
    const formFieldName = mapping[modelParamName]
    const val = formData[formFieldName]
    // null/undefined 转为空串
    if (val === null || val === undefined) {
      params[modelParamName] = ''
    } else if (typeof val !== 'object') {
      params[modelParamName] = val
    }
  })
  console.log('[IndicatorControl] buildParams(映射): mapping=', mapping, 'result=', params)
  return params
}

/**
 * 构建控制拦截的详细提示信息（HTML 格式）
 * @param {String} serverMsg - 后端返回的 message
 * @param {Object} evalResult - { pass, value, threshold }
 * @param {Object} config - 控制配置项
 * @returns {String} HTML 格式的提示信息
 */
function buildControlMessage(serverMsg, evalResult, config) {
  const isRigid = config.controlLevel === 'RIGID'
  const lines = []

  // 主提示语
  if (serverMsg) {
    lines.push(`<p style="margin:0 0 8px;font-size:14px;">${serverMsg}</p>`)
  } else {
    lines.push(`<p style="margin:0 0 8px;font-size:14px;">${isRigid ? '指标校验未通过，当前操作已被拦截。' : '当前操作存在风险，请确认是否继续。'}</p>`)
  }

  // 指标值 & 阈值明细
  if (evalResult.value !== null && evalResult.threshold !== null) {
    lines.push('<div style="background:#f5f7fa;border-radius:4px;padding:8px 12px;margin:8px 0;font-size:13px;line-height:1.8;">')
    lines.push(`<div>当前指标值：<b style="color:#E6A23C;">${evalResult.value}</b></div>`)
    lines.push(`<div>控制阈值：<b>${evalResult.threshold}</b></div>`)
    lines.push('</div>')
  }

  // 控制级别标签
  if (isRigid) {
    lines.push('<p style="margin:8px 0 0;color:#F56C6C;font-size:12px;">控制级别：刚性控制（不可跳过）</p>')
  } else {
    lines.push('<p style="margin:8px 0 0;color:#E6A23C;font-size:12px;">控制级别：柔性控制（可确认后继续）</p>')
  }

  return lines.join('')
}

/**
 * 解析模型执行结果，判断是否通过
 *
 * 两种判定模式：
 *
 * 模式一：阈值比较（thresholdValue 非空）
 *   后端返回格式：{ success: true, data: [{ CONTROL: 0 }], message: "..." }
 *   指标值 < 阈值 → 通过；指标值 >= 阈值 → 不通过
 *
 * 模式二：模型标志判定（thresholdValue 为空）
 *   后端返回格式：{ success: true, is_control: true/false, message: "拦截原因" }
 *   is_control = true  → 不通过（触发控制）
 *   is_control = false → 通过（放行）
 *
 * @returns {{ pass: boolean, value: number|null, threshold: number|null, mode: string }}
 */
function evaluateModelResult(resData, thresholdValue) {
  // 1. 后端已经给出明确的 pass 判定
  if (resData.pass !== undefined && resData.pass !== null) {
    return { pass: !!resData.pass, value: null, threshold: null, mode: 'pass_field' }
  }

  const threshold = parseFloat(thresholdValue)
  const hasThreshold = thresholdValue !== null && thresholdValue !== undefined
    && String(thresholdValue).trim() !== '' && !isNaN(threshold)

  // 2. 阈值为空 → 使用模型返回的 is_control 标志判定
  if (!hasThreshold) {
    // is_control 可能在 resData 顶层，也可能在 data 数组首行
    const isControl = resolveIsControl(resData)
    if (isControl !== null) {
      console.log(`[IndicatorControl] 无阈值，使用 is_control 判定: is_control=${isControl}`)
      return { pass: !isControl, value: null, threshold: null, mode: 'is_control' }
    }
    // is_control 也不存在，默认放行
    console.log('[IndicatorControl] 无阈值且无 is_control 字段，默认放行')
    return { pass: true, value: null, threshold: null, mode: 'default' }
  }

  // 3. 有阈值 → 从 data 数组提取指标值进行比较
  const rows = resData.data
  if (!Array.isArray(rows) || rows.length === 0) {
    console.warn('[IndicatorControl] 模型返回数据为空，默认放行')
    return { pass: true, value: null, threshold, mode: 'threshold' }
  }

  const firstRow = rows[0]
  const numericValue = Object.values(firstRow).find(v => typeof v === 'number')

  if (numericValue === undefined) {
    console.warn('[IndicatorControl] 模型返回数据中无数值字段，默认放行')
    return { pass: true, value: null, threshold, mode: 'threshold' }
  }

  console.log(`[IndicatorControl] 指标值=${numericValue}, 阈值=${threshold}`)
  // 指标值 < 阈值 → 通过（未超标）；指标值 >= 阈值 → 不通过（触发控制）
  return { pass: numericValue < threshold, value: numericValue, threshold, mode: 'threshold' }
}

/**
 * 从模型返回数据中解析 is_control 字段
 * 优先取 resData 顶层，其次取 data[0] 中的字段
 * @returns {boolean|null} true=需要控制, false=不控制, null=字段不存在
 */
function resolveIsControl(resData) {
  // 顶层 is_control
  if (resData.is_control !== undefined && resData.is_control !== null) {
    return !!resData.is_control
  }
  // data 数组首行
  const rows = resData.data
  if (Array.isArray(rows) && rows.length > 0) {
    const first = rows[0]
    // 兼容 is_control / IS_CONTROL / isControl
    const val = first.is_control !== undefined ? first.is_control
      : first.IS_CONTROL !== undefined ? first.IS_CONTROL
      : first.isControl !== undefined ? first.isControl
      : null
    if (val !== null && val !== undefined) {
      // 兼容 boolean / number / string
      if (typeof val === 'boolean') return val
      if (typeof val === 'number') return val !== 0
      if (typeof val === 'string') return val.toLowerCase() === 'true' || val === '1'
    }
  }
  return null
}

