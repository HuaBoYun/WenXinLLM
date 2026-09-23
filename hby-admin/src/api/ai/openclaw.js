/**
 * OpenClaw Gateway WebSocket 客户端
 * 用于与 XingGuang AI 编程助手进行实时对话
 *
 * 协议版本: v2
 * 签名算法: Ed25519
 */

// Ed25519 签名：使用 Web Crypto API 原生实现，无需第三方库

/**
 * 设备身份信息
 */
class DeviceIdentity {
  constructor(deviceId, publicKey, privateKey) {
    this.deviceId = deviceId
    this.publicKey = publicKey
    this.privateKey = privateKey
  }
}

/**
 * OpenClaw WebSocket 客户端类
 */
export class OpenClawClient {
  /**
   * 构造函数
   * @param {Object} config 配置对象
   * @param {String} config.gatewayUrl Gateway 地址，默认 ws://127.0.0.1:8381
   * @param {String} config.token 认证 Token
   * @param {String} config.client 客户端名称
   * @param {String} config.mode 客户端模式
   * @param {Array} config.scopes 权限范围
   */
  constructor(config = {}) {
    this.gatewayUrl = config.gatewayUrl || 'ws://127.0.0.1:8381'
    this.token = config.token || ''
    this.sessionKey = config.sessionKey || 'agent:main:main'
    this.client = config.client || 'webchat-ui'
    this.clientVersion = config.clientVersion || '1.0.0'
    this.mode = config.mode || 'webchat'
    this.scopes = config.scopes || ['operator.read', 'operator.write']

    // 连接状态
    this.isConnected = false
    this.ws = null
    this.reconnectAttempts = 0
    this.maxReconnectAttempts = 5

    // 消息处理
    this.requestIdCounter = 0
    this.pendingRequests = new Map()
    this.eventHandlers = {}

    // 设备身份
    this.deviceIdentity = null
    this.connectNonce = null

    // 使用设备身份认证，webchat 模式下支持本地自动配对
    this.skipDeviceAuth = false
    // 消息队列（连接前缓存）
    this.messageQueue = []
  }

  /**
   * 连接到 Gateway
   * @returns {Promise<void>}
   */
  connect() {
    return new Promise((resolve, reject) => {
      try {
        // 构建连接 URL
        const url = `${this.gatewayUrl}${this.token ? '?token=' + this.token : ''}`
        console.log('[OpenClaw] 连接到:', url)

        this.ws = new WebSocket(url)

        // 连接打开
        this.ws.onopen = async () => {
          console.log('[OpenClaw] WebSocket 连接成功')
          this.isConnected = true
          this.reconnectAttempts = 0

          // 加载设备身份
          try {
            await this.loadDeviceIdentity()
            console.log('[OpenClaw] 设备身份已加载:', {
              deviceId: this.deviceIdentity.deviceId
            })
          } catch (error) {
            console.error('[OpenClaw] 加载设备身份失败:', error)
            reject(error)
            return
          }

          // 等待连接挑战或直接发送连接请求
          // 注意：connect.challenge 事件会触发 sendConnect
          // 如果没有收到挑战，750ms 后自动发送连接请求
        }

        // 收到消息
        this.ws.onmessage = (event) => {
          this.handleMessage(event.data)
        }

        // 连接错误
        this.ws.onerror = (error) => {
          console.error('[OpenClaw] WebSocket 错误:', error)
          this.isConnected = false
          this.emit('error', error)
        }

        // 连接关闭
        this.ws.onclose = (event) => {
          console.log('[OpenClaw] 连接关闭, code:', event.code, 'reason:', event.reason)
          this.isConnected = false
          this.connectNonce = null
          // 注意：不在 onclose 中重置 skipDeviceAuth，保留给重连使用
          this.emit('disconnected')

          // 自动重连
          if (this.reconnectAttempts < this.maxReconnectAttempts) {
            this.reconnectAttempts++
            console.log(`[OpenClaw] 3秒后尝试重连 (${this.reconnectAttempts}/${this.maxReconnectAttempts})`)
            setTimeout(() => this.connect(), 3000)
          } else {
            console.error('[OpenClaw] 已达到最大重连次数，停止重连')
            this.emit('maxReconnectAttemptsReached')
          }
        }
      } catch (error) {
        console.error('[OpenClaw] 连接创建失败:', error)
        reject(error)
      }
    })
  }

  /**
   * 加载或创建设备身份
   * @returns {Promise<DeviceIdentity>}
   */
  async loadDeviceIdentity() {
    const STORAGE_KEY = 'openclaw-device-identity-v1'
    const storage = localStorage.getItem(STORAGE_KEY)

    if (storage) {
      try {
        const parsed = JSON.parse(storage)
        if (parsed.version === 1 &&
            parsed.deviceId &&
            parsed.publicKey &&
            parsed.privateKey) {
          this.deviceIdentity = new DeviceIdentity(
            parsed.deviceId,
            parsed.publicKey,
            parsed.privateKey
          )
          console.log('[OpenClaw] 使用已存储的设备身份')
          return
        }
      } catch (error) {
        console.warn('[OpenClaw] 解析存储的设备身份失败，重新生成:', error)
      }
    }

    // 生成新的设备身份
    await this.generateDeviceIdentity()
  }

  /**
   * 生成新的设备身份
   * @returns {Promise<DeviceIdentity>}
   */
  async generateDeviceIdentity() {
    console.log('[OpenClaw] 使用 Web Crypto API 生成 Ed25519 密钥对...')

    const keyPair = await crypto.subtle.generateKey(
      { name: 'Ed25519' },
      true,
      ['sign', 'verify']
    )

    const publicKeyRaw = new Uint8Array(await crypto.subtle.exportKey('raw', keyPair.publicKey))
    const privateKeyPkcs8 = new Uint8Array(await crypto.subtle.exportKey('pkcs8', keyPair.privateKey))

    const deviceId = await this.fingerprintPublicKey(publicKeyRaw)
    const publicKeyBase64 = this.base64UrlEncode(publicKeyRaw)
    const privateKeyBase64 = this.base64UrlEncode(privateKeyPkcs8)

    const stored = {
      version: 1,
      deviceId,
      publicKey: publicKeyBase64,
      privateKey: privateKeyBase64,
      createdAtMs: Date.now()
    }
    localStorage.setItem('openclaw-device-identity-v1', JSON.stringify(stored))

    this.deviceIdentity = new DeviceIdentity(deviceId, publicKeyBase64, privateKeyBase64)
    console.log('[OpenClaw] 新的设备身份已生成:', { deviceId })
  }

  /**
   * 基于公钥生成设备 ID
   * @param {Uint8Array} publicKey 公钥
   * @returns {Promise<String>}
   */
  async fingerprintPublicKey(publicKey) {
    const hash = await crypto.subtle.digest('SHA-256', publicKey.slice().buffer)
    return Array.from(new Uint8Array(hash))
      .map(b => b.toString(16).padStart(2, '0'))
      .join('')
  }

  /**
   * 发送连接请求
   * @returns {Promise<void>}
   */
  async sendConnect() {
    if (!this.deviceIdentity && !this.skipDeviceAuth) {
      console.error('[OpenClaw] 设备身份未加载')
      return
    }

    if (!this.connectNonce) {
      console.error('[OpenClaw] 连接 nonce 未设置')
      return
    }

    const requestId = this.generateId()

    // 构建连接参数（方案B: 不带 device 字段，依赖 token + 本地连接）
    const params = {
      minProtocol: 3,
      maxProtocol: 3,
      client: {
        id: this.client,
        version: this.clientVersion,
        platform: 'web',
        mode: this.mode
      },
      role: 'operator',
      scopes: this.scopes,
      caps: ['tool-events'],
      userAgent: navigator.userAgent,
      locale: navigator.language
    }

    // 仅在需要设备认证时构建签名和 device 对象
    if (!this.skipDeviceAuth && this.deviceIdentity) {
      const signedAtMs = Date.now()
      const payload = this.buildDeviceAuthPayload({
        deviceId: this.deviceIdentity.deviceId,
        clientId: this.client,
        clientMode: this.mode,
        role: 'operator',
        scopes: this.scopes,
        signedAtMs,
        token: this.token || null,
        nonce: this.connectNonce
      })
      console.log('[OpenClaw] 签名 payload:', payload)
      const signature = await this.signDevicePayload(
        this.deviceIdentity.privateKey,
        payload
      )
      params.device = {
        id: this.deviceIdentity.deviceId,
        publicKey: this.deviceIdentity.publicKey,
        signature: signature,
        signedAt: signedAtMs,
        nonce: this.connectNonce
      }
    }

    // 添加认证信息
    if (this.token) {
      params.auth = { token: this.token }
    }

    console.log('[OpenClaw] 发送连接请求，设备对象:', params.device ? Object.keys(params.device) : 'skipped')

    const request = {
      type: 'req',
      id: requestId,
      method: 'connect',
      params
    }

    // 注册响应处理
    this.pendingRequests.set(requestId, { resolve: () => {}, reject: () => {}, timeout: setTimeout(() => {
      this.pendingRequests.delete(requestId)
      console.warn('[OpenClaw] 连接请求超时')
    }, 10000) })

    // 发送请求
    this.sendRaw(request)
  }

  /**
   * 构建设备认证 payload
   * @param {Object} params 参数
   * @returns {String}
   */
  buildDeviceAuthPayload(params) {
    const scopes = params.scopes.join(',')
    const token = params.token !== null && params.token !== undefined ? params.token : ''
    return [
      'v2',
      params.deviceId,
      params.clientId,
      params.clientMode,
      params.role,
      scopes,
      String(params.signedAtMs),
      token,
      params.nonce
    ].join('|')
  }

  /**
   * 使用私钥签名数据
   * @param {String} privateKeyBase64 Base64Url 编码的私钥
   * @param {String} payload 要签名的数据
   * @returns {Promise<String>}
   */
  async signDevicePayload(privateKeyBase64, payload) {
    const pkcs8Bytes = this.base64UrlDecode(privateKeyBase64)
    const privateKey = await crypto.subtle.importKey(
      'pkcs8',
      pkcs8Bytes,
      { name: 'Ed25519' },
      false,
      ['sign']
    )
    const data = new TextEncoder().encode(payload)
    const sig = new Uint8Array(await crypto.subtle.sign({ name: 'Ed25519' }, privateKey, data))
    return this.base64UrlEncode(sig)
  }

  /**
   * Base64Url 编码
   * @param {Uint8Array} bytes 字节数组
   * @returns {String}
   */
  base64UrlEncode(bytes) {
    let binary = ''
    for (const byte of bytes) {
      binary += String.fromCharCode(byte)
    }
    return btoa(binary).replaceAll('+', '-').replaceAll('/', '_').replace(/=+$/g, '')
  }

  /**
   * Base64Url 解码
   * @param {String} input Base64Url 字符串
   * @returns {Uint8Array}
   */
  base64UrlDecode(input) {
    const normalized = input.replaceAll('-', '+').replaceAll('_', '/')
    const padded = normalized + '='.repeat((4 - (normalized.length % 4)) % 4)
    const binary = atob(padded)
    const out = new Uint8Array(binary.length)
    for (let i = 0; i < binary.length; i++) {
      out[i] = binary.charCodeAt(i)
    }
    return out
  }

  /**
   * 处理收到的消息
   * @param {String} data 消息数据
   */
  handleMessage(data) {
    try {
      const message = JSON.parse(data)

      if (message.type === 'event') {
        // 处理事件
        this.handleEvent(message)
      } else if (message.type === 'res') {
        // 处理响应
        this.handleResponse(message)
      }
    } catch (error) {
      console.error('[OpenClaw] 消息解析失败:', error, data)
    }
  }

  /**
   * 处理事件消息
   * @param {Object} data 事件数据
   */
  handleEvent(data) {
    if (!data.event) return

    const { event, payload } = data
    console.log('[OpenClaw] 收到事件:', event, payload ? payload : '')

    switch (event) {
      case 'chat':
        // 服务端发送 chat 事件，通过 payload.state 区分类型
        if (payload.state === 'delta') {
          this.emit('message', payload.message, payload)
        } else if (payload.state === 'final') {
          this.emit('messageComplete', payload)
        } else if (payload.state === 'aborted') {
          this.emit('chatAborted', payload)
        } else if (payload.state === 'error') {
          this.emit('chatError', payload)
          this.emit('chatAborted', payload)
        }
        break
      case 'agent':
        // agent 事件：tool / assistant / lifecycle
        if (payload.stream === 'lifecycle' && payload.data?.phase === 'start') {
          this.emit('chatStarted', payload)
        } else if (payload.stream === 'tool') {
          this.emit('agentToolEvent', payload)
        } else if (payload.stream === 'assistant') {
          this.emit('agentAssistantEvent', payload)
        }
        break
      case 'typing.start':
        this.emit('typing', true)
        break
      case 'typing.end':
        this.emit('typing', false)
        break
      case 'connect.challenge':
        console.log('[OpenClaw] 收到连接挑战')
        const nonce = payload?.nonce
        if (nonce && typeof nonce === 'string') {
          this.connectNonce = nonce
          console.log('[OpenClaw] 设置连接 nonce:', nonce)
          // 收到挑战后发送连接请求
          setTimeout(() => this.sendConnect(), 100)
        }
        break
      case 'connect':
        break
      case 'health':
      case 'tick':
        // 心跳事件，静默忽略
        break
      default:
        console.log('[OpenClaw] 未知事件:', event)
    }
  }

  /**
   * 处理响应消息
   * @param {Object} data 响应数据
   */
  handleResponse(data) {
    const requestId = data.id
    const pendingRequest = this.pendingRequests.get(requestId)

    if (!pendingRequest) {
      console.warn('[OpenClaw] 未找到待处理请求:', requestId)
      return
    }

    clearTimeout(pendingRequest.timeout)
    this.pendingRequests.delete(requestId)

    if (data.ok) {
      // 处理历史消息
      if (data.payload && data.payload.messages) {
        data.payload.messages.forEach(msg => {
          this.emit('history', msg)
        })
      }

      // 连接成功
      if (data.payload?.protocol || data.payload?.server) {
        console.log('[OpenClaw] 连接握手成功')
        this.emit('connected')
        // 发送队列中的消息
        this.flushMessageQueue()
      }

      pendingRequest.resolve(data.payload)
    } else {
      const errorMsg = typeof data.error === 'string'
        ? data.error
        : (data.error?.message || JSON.stringify(data.error))
      console.error('[OpenClaw] 请求失败:', errorMsg)

      // 检查是否需要重新配对设备身份
      if (errorMsg.includes('pairing required')) {
        // 不清除设备身份！保持稳定的 deviceId，让服务端 pending 条目持续存在
        // 用户需要在 XingGuang CLI 中执行 approve 操作来批准此设备
        const deviceId = this.deviceIdentity?.deviceId || '未知'
        console.warn(`[OpenClaw] 需要设备配对审批，当前设备ID: ${deviceId.substring(0, 16)}...`)
        console.warn('[OpenClaw] 请在 XingGuang 终端执行: xingguang devices approve --latest')
        this.emit('pairingRequired', { deviceId })
      }

      // 检查是否是设备身份不匹配错误
      if (errorMsg.includes('device identity mismatch')) {
        console.error('[OpenClaw] 设备身份不匹配，清除并重新生成')
        localStorage.removeItem('openclaw-device-identity-v1')
        this.deviceIdentity = null
      }

      pendingRequest.reject(new Error(errorMsg || '请求失败'))
    }
  }

  /**
   * 发送聊天消息
   * @param {String} content 消息内容
   * @returns {String} 请求ID
   */
  sendMessage(content) {
    return this.request('chat.send', {
      sessionKey: this.sessionKey,
      message: content,
      idempotencyKey: `msg_${Date.now()}_${Math.random().toString(36).slice(2, 10)}`
    })
  }

  /**
   * 获取聊天历史
   * @param {Number} limit 历史记录数量，默认 50
   * @returns {Promise<Object>}
   */
  async getHistory(limit = 50) {
    return this.request('chat.history', { sessionKey: this.sessionKey, limit })
  }

  /**
   * 中止当前对话
   * @returns {String} 请求ID
   */
  abortChat() {
    return this.request('chat.abort')
  }

  /**
   * 发送通用请求
   * @param {String} method 方法名
   * @param {Object} params 参数
   * @returns {Promise<Object>}
   */
  request(method, params = {}) {
    return new Promise((resolve, reject) => {
      const requestId = this.generateId()

      const request = {
        type: 'req',
        id: requestId,
        method: method,
        params: params
      }

      // 注册响应处理
      this.pendingRequests.set(requestId, { resolve, reject, timeout: setTimeout(() => {
        this.pendingRequests.delete(requestId)
        reject(new Error('请求超时'))
      }, 30000) })

      // 发送请求
      this.sendRaw(request)
    })
  }

  /**
   * 发送原始消息
   * @param {Object} message 消息对象
   */
  sendRaw(message) {
    if (!this.isConnected || !this.ws) {
      console.warn('[OpenClaw] 未连接，消息加入队列')
      this.messageQueue.push(message)
      return
    }

    try {
      this.ws.send(JSON.stringify(message))
    } catch (error) {
      console.error('[OpenClaw] 发送消息失败:', error)
    }
  }

  /**
   * 刷新消息队列
   */
  flushMessageQueue() {
    while (this.messageQueue.length > 0) {
      const message = this.messageQueue.shift()
      this.sendRaw(message)
    }
  }

  /**
   * 断开连接
   */
  disconnect() {
    if (this.ws) {
      this.ws.close()
      this.ws = null
      this.isConnected = false
    }

    // 清除所有待处理请求
    this.pendingRequests.forEach(req => {
      clearTimeout(req.timeout)
    })
    this.pendingRequests.clear()

    this.messageQueue = []
    this.connectNonce = null
    this.skipDeviceAuth = false
  }

  /**
   * 生成唯一ID
   * @returns {String}
   */
  generateId() {
    return `${++this.requestIdCounter}_${Date.now()}`
  }

  /**
   * 事件订阅
   * @param {String} event 事件名称
   * @param {Function} handler 处理函数
   */
  on(event, handler) {
    if (!this.eventHandlers[event]) {
      this.eventHandlers[event] = []
    }
    this.eventHandlers[event].push(handler)
  }

  /**
   * 取消事件订阅
   * @param {String} event 事件名称
   * @param {Function} handler 处理函数
   */
  off(event, handler) {
    if (!this.eventHandlers[event]) return
    this.eventHandlers[event] = this.eventHandlers[event].filter(h => h !== handler)
  }

  /**
   * 触发事件
   * @param {String} event 事件名称
   * @param {Object} data 事件数据
   */
  emit(event, ...args) {
    if (this.eventHandlers[event]) {
      this.eventHandlers[event].forEach(handler => {
        try {
          handler(...args)
        } catch (error) {
          console.error('[OpenClaw] 事件处理器错误:', error)
        }
      })
    }
  }
}

/**
 * 创建全局 OpenClaw 客户端实例
 * @param {Object} config 配置对象
 * @returns {OpenClawClient}
 */
export function createOpenClawClient(config) {
  return new OpenClawClient(config)
}

/**
 * 全局客户端实例（单例模式）
 */
let globalClient = null

/**
 * 获取全局 OpenClaw 客户端实例
 * @param {Object} config 配置对象（仅第一次创建时使用）
 * @returns {OpenClawClient}
 */
export function getOpenClawClient(config) {
  if (!globalClient) {
    globalClient = new OpenClawClient(config)
  }
  return globalClient
}
