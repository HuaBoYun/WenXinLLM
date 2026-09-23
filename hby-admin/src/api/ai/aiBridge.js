/**
 * AI Bridge 客户端 - 用于 AIProgramming.vue 连接本地 Bridge Server
 * 
 * 提供与 OpenClaw 客户端类似的事件驱动 API，方便页面无缝切换
 */

export class AIBridgeClient {
  constructor(config = {}) {
    this.url = config.url || 'ws://127.0.0.1:8382'
    this.ws = null
    this.isConnected = false
    this.reconnectAttempts = 0
    this.maxReconnectAttempts = 10
    this.reconnectTimer = null
    this.eventHandlers = {}
  }

  /**
   * 连接到 Bridge Server
   */
  connect() {
    return new Promise((resolve, reject) => {
      try {
        console.log('[AIBridge] 连接到:', this.url)
        this.ws = new WebSocket(this.url)

        this.ws.onopen = () => {
          console.log('[AIBridge] 连接成功')
          this.isConnected = true
          this.reconnectAttempts = 0
          this.emit('connecting')
        }

        this.ws.onmessage = (event) => {
          this.handleMessage(event.data)
        }

        this.ws.onerror = (error) => {
          console.error('[AIBridge] 连接错误:', error)
          this.emit('error', error)
          reject(error)
        }

        this.ws.onclose = (event) => {
          console.log('[AIBridge] 连接关闭:', event.code)
          this.isConnected = false
          this.emit('disconnected')
          this.tryReconnect()
        }
      } catch (error) {
        console.error('[AIBridge] 创建连接失败:', error)
        reject(error)
      }
    })
  }

  /**
   * 处理收到的消息
   */
  handleMessage(raw) {
    try {
      const data = JSON.parse(raw)

      switch (data.type) {
        case 'connected':
          console.log('[AIBridge] 会话ID:', data.sessionId)
          this.emit('connected', data)
          break

        case 'history':
          console.log('[AIBridge] 收到历史消息:', data.dialogue?.length, '条')
          this.emit('history', data.dialogue || [])
          break

        case 'userMessage':
          // 其他标签页发送的消息（多端同步）
          this.emit('userMessage', data.message)
          break

        case 'assistantMessage':
          // IDE 的回复
          console.log('[AIBridge] 收到 IDE 回复, 完成:', data.isComplete)
          if (data.isComplete) {
            this.emit('messageComplete', data.message)
          } else {
            this.emit('message', data.message)
          }
          break

      case 'toolEvent':
        // IDE 工具调用事件
        this.emit('toolEvent', data.tool)
        break

        case 'historyCleared':
          this.emit('historyCleared')
          break

        case 'ping':
          // 心跳，忽略
          break

        default:
          console.log('[AIBridge] 未知消息类型:', data.type)
      }
    } catch (e) {
      console.error('[AIBridge] 解析消息失败:', e)
    }
  }

  /**
   * 发送聊天消息
   */
  sendMessage(content) {
    if (!this.isConnected) {
      throw new Error('未连接到 Bridge Server')
    }
    this.ws.send(JSON.stringify({
      type: 'chat',
      content: content,
      timestamp: Date.now()
    }))
  }

  /**
   * 清空历史
   */
  clearHistory() {
    if (this.isConnected) {
      this.ws.send(JSON.stringify({ type: 'clearHistory' }))
    }
  }

  /**
   * 断开连接
   */
  disconnect() {
    if (this.reconnectTimer) {
      clearTimeout(this.reconnectTimer)
      this.reconnectTimer = null
    }
    this.reconnectAttempts = this.maxReconnectAttempts // 阻止重连
    if (this.ws) {
      this.ws.close()
      this.ws = null
    }
    this.isConnected = false
  }

  /**
   * 自动重连
   */
  tryReconnect() {
    if (this.reconnectAttempts >= this.maxReconnectAttempts) return
    this.reconnectAttempts++
    const delay = Math.min(1000 * this.reconnectAttempts, 5000)
    console.log(`[AIBridge] ${delay}ms 后重连 (${this.reconnectAttempts}/${this.maxReconnectAttempts})`)
    this.reconnectTimer = setTimeout(() => this.connect().catch(() => {}), delay)
  }

  // ============ 事件系统 ============
  on(event, handler) {
    if (!this.eventHandlers[event]) this.eventHandlers[event] = []
    this.eventHandlers[event].push(handler)
  }

  off(event, handler) {
    if (!this.eventHandlers[event]) return
    this.eventHandlers[event] = this.eventHandlers[event].filter(h => h !== handler)
  }

  emit(event, ...args) {
    if (this.eventHandlers[event]) {
      this.eventHandlers[event].forEach(h => {
        try { h(...args) } catch (e) { console.error('[AIBridge] 事件处理器错误:', e) }
      })
    }
  }
}

/**
 * 创建 AIBridge 客户端实例
 */
export function createAIBridgeClient(config) {
  return new AIBridgeClient(config)
}

