/**
 * 星光问心AI对话接口
 */
import store from '@/store'

// AI服务基础URL
// 说明：流式接口用原生 fetch（处理 SSE），不走 axios 的 baseURL，因此需手动带上代理前缀。
// 与历史记录接口（axios request，baseURL=/vab-mock-server）保持同一代理通道：
//   dev: /vab-mock-server/bigmodel/** -> vue.config.js 代理转发到网关 http://127.0.0.1:9000
//   网关再将 /bigmodel/** 转发到 exampleBigModelService 服务
const AI_BASE_URL = '/api/bigmodel'

/**
 * 获取请求头（包含token）
 */
function getHeaders() {
  const headers = {
    'Content-Type': 'application/json',
  }
  const token = store.getters['user/token']
  if (token) {
    headers['Authorization'] = `Bearer ${token}`
    headers['token'] = token
  }
  return headers
}

/**
 * 流式对话内部通用实现 - 使用 fetch + EventSource 处理 SSE
 * @param {String} endpoint 接口路径（不同业务模块使用不同的端点，实现请求方法隔离）
 * @param {Object} data 请求参数
 * @param {Object} callbacks 回调集合
 * @returns {AbortController} 用于取消请求
 */
function createChatStream(endpoint, data, { onMessage, onThinking, onToolCall, onProgress, onError, onComplete }) {
  const controller = new AbortController()

  // 自动拼接当前日期到消息头部
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  const weekdays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
  const weekday = weekdays[now.getDay()]
  const dateStr = `${year}年${month}月${day}日（${weekday}）`
  const datePrefix = `【重要时间背景】当前真实日期为：${dateStr}，请以此日期为准生成内容，所有分析数据、时间轴、趋势图等均以${year}年为当前年份，不得使用2024年或更早数据作为当前数据。\n\n`
  const enrichedData = {
    ...data,
    message: data.message ? datePrefix + data.message : data.message
  }

  console.log('[SSE] 发起请求:', `${AI_BASE_URL}${endpoint}`)
  console.log('[SSE] 请求数据:', enrichedData)

  fetch(`${AI_BASE_URL}${endpoint}`, {
    method: 'POST',
    headers: getHeaders(),
    body: JSON.stringify(enrichedData),
    signal: controller.signal,
  })
    .then(response => {
      console.log('[SSE] 收到响应:', response.status, response.ok, response.headers.get('content-type'))

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`)
      }

      const reader = response.body.getReader()
      const decoder = new TextDecoder()
      let buffer = ''

      console.log('[SSE] 开始读取流...')

      function processStream() {
        reader.read().then(({ done, value }) => {
          if (done) {
            if (onComplete) onComplete()
            return
          }

          buffer += decoder.decode(value, { stream: true })
          const lines = buffer.split('\n')
          buffer = lines.pop() || ''

          for (const line of lines) {
            if (line.startsWith('data:')) {
              const dataStr = line.slice(5).trim()
              if (!dataStr) continue

              try {
                // 处理双重 data: 前缀的情况
                let jsonStr = dataStr
                if (jsonStr.startsWith('data:')) {
                  jsonStr = jsonStr.slice(5).trim()
                }

                let event
                try {
                  event = JSON.parse(jsonStr)
                } catch (parseErr) {
                  // JSON 解析失败：尝试用正则提取错误信息
                  const errorMatch = jsonStr.match(/"error"\s*:\s*"([^"]+)"/)
                  const msgMatch = jsonStr.match(/"message"\s*:\s*"([^"]+)"/) ||
                    jsonStr.match(/message["\s:]+([^"\}]+)/)
                  const errMsg = errorMatch ? errorMatch[1] :
                    msgMatch ? msgMatch[1] :
                      jsonStr.substring(0, 200)
                  console.warn('[SSE] JSON解析失败，提取错误信息:', errMsg)
                  if (onError) onError(errMsg)
                  continue
                }

                if (event.type === 'thinking_start') {
                  if (onThinking) onThinking({ type: 'start' })
                } else if (event.type === 'thinking') {
                  if (onThinking) onThinking({ type: 'content', content: event.content })
                } else if (event.type === 'thinking_end') {
                  if (onThinking) onThinking({ type: 'end' })
                } else if (event.type === 'text') {
                  console.log('[SSE] 收到文本事件:', event.content)
                  if (onMessage) onMessage(event.content)
                } else if (event.type === 'tool_call') {
                  // 工具调用事件，包含详细信息
                  console.log('[SSE] 收到工具调用事件:', event.tool, event.message)
                  if (onToolCall) onToolCall(event.tool, event.message, event.params)
                  // 同时将工具调用消息显示在对话中
                  if (event.message && onProgress) {
                    onProgress(event.message)
                  }
                } else if (event.type === 'progress') {
                  console.log('[SSE] 收到进度事件:', event.message)
                  // 处理进度消息
                  if (onProgress) onProgress(event.message)
                } else if (event.type === 'done') {
                  // 流结束
                  console.log('[SSE] 流结束')
                } else if (event.type === 'error') {
                  // 显式 error 类型事件
                  const errDetail = event.error
                  const errMsg = typeof errDetail === 'object'
                    ? (errDetail.message || JSON.stringify(errDetail))
                    : String(errDetail)
                  console.warn('[SSE] 收到错误事件:', errMsg)
                  if (onError) onError(errMsg)
                } else if (event.error) {
                  // 其他含 error 字段的事件
                  const errMsg = typeof event.error === 'object'
                    ? (event.error.message || JSON.stringify(event.error))
                    : String(event.error)
                  if (onError) onError(errMsg)
                }
              } catch (e) {
                console.warn('Parse SSE event error:', e, dataStr)
              }
            }
          }

          processStream()
        }).catch(err => {
          if (err.name !== 'AbortError') {
            if (onError) onError(err.message)
          }
        })
      }

      processStream()
    })
    .catch(err => {
      if (err.name !== 'AbortError') {
        if (onError) onError(err.message)
      }
    })

  return controller
}

/**
 * 流式对话【AI 建模】- 请求 /v1/ai/chat/stream
 * @param {Object} data 请求参数
 * @param {Object} callbacks 回调集合 { onMessage, onThinking, onToolCall, onProgress, onError, onComplete }
 * @returns {AbortController} 用于取消请求
 */
export function chatStream(data, callbacks) {
  return createChatStream('/v1/ai/chat/stream', data, callbacks)
}

/**
 * 流式对话【AI 咨询】- 请求 /v1/ai/consult/stream
 * 独立于 AI 建模的请求方法，使用专属端点，互不影响
 * @param {Object} data 请求参数
 * @param {Object} callbacks 回调集合 { onMessage, onThinking, onToolCall, onProgress, onError, onComplete }
 * @returns {AbortController} 用于取消请求
 */
export function consultStream(data, callbacks) {
  return createChatStream('/v1/ai/consult/stream', data, callbacks)
}

/**
 * 清除会话
 * @param {String} sessionId 会话ID
 */
export function clearSession(sessionId) {
  return fetch(`${AI_BASE_URL}/v1/ai/session/${sessionId}`, {
    method: 'DELETE',
    headers: getHeaders(),
  }).then(res => res.json())
}

/**
 * 简单问答（非流式）
 * @param {String} question 问题
 * @param {Boolean} thinking 是否开启深度思考
 * @param {Boolean} search 是否开启联网搜索
 */
export function askQuestion(question, thinking = false, search = true) {
  return fetch(`${AI_BASE_URL}/v1/ai/ask?question=${encodeURIComponent(question)}&thinking=${thinking}&search=${search}`, {
    headers: getHeaders(),
  }).then(res => res.json())
}

/**
 * 执行SQL语句（用于生成数据库模型）
 * @param {Object} data SQL执行参数
 * @param {String} data.sql SQL语句
 * @param {String} data.dbType 数据库类型 (dm/mysql)
 * @param {String} data.sqlType SQL类型 (DDL/DML/QUERY)
 * @returns {Promise<SqlExecuteResponse>}
 */
export function executeSql(data) {
  return fetch(`${AI_BASE_URL}/v1/sql/execute`, {
    method: 'POST',
    headers: getHeaders(),
    body: JSON.stringify(data),
  }).then(res => res.json())
}

/**
 * 批量执行SQL语句
 * @param {Array} sqlStatements SQL语句数组
 * @param {String} dbType 数据库类型 (dm/mysql)
 * @returns {Promise<SqlExecuteResponse>}
 */
export function executeBatchSql(sqlStatements, dbType = 'dm') {
  return fetch(`${AI_BASE_URL}/v1/sql/batch`, {
    method: 'POST',
    headers: getHeaders(),
    body: JSON.stringify({
      sqls: sqlStatements.join(';'),
      dbType: dbType
    }),
  }).then(res => res.json())
}

