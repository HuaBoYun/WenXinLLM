<template>
  <div class="container" v-loading="loading">
    <div class="main-content" :class="{ 'with-preview': showHtmlPreview }" :style="showHtmlPreview ? { marginRight: previewPanelWidth + 'px' } : {}">
      <div class="agent-list">
        <div class="scorll-content">
          <div class="text-content">
            <div class="page-title">
              <div class="menu-item">
                <i :class="`menu-icon el-icon-edit`"></i>
                <div class="menu-title">{{ menuInfo.label || 'AI 写作' }}</div>
              </div>
            </div>
            <div class="intro">搜索世界万物，一键成文</div>
          </div>
          <div class="relative-cards">
            <div class="card-item" :class="{ 'card-item-actived': item.id === curCard.id }" v-for="item in allFillterList" :key="item.id" @click="onCardClick(item)">
              <div class="card-icon"><img :src="iconList[Number(item.agentPicture)] || defaultAvatar" alt="" ></div>
              <div class="card-title">{{ item.agentName }}</div>
              <div class="card-desc">{{ item.describe }}</div>
            </div>
          </div>

          <!-- AI 对话展示区域 -->
          <div class="chat-content" ref="chatContent">
            <div class="line" v-for="(item, index) in dialogue" :key="index">
              <div class="chat-item question" v-if="item.role === 'user'">
                {{ item.content }}
              </div>
              <div class="chat-item answer" v-if="item.role === 'assistant'">
                <div
                  class="assistant-avatar"
                  style="margin-bottom: 10px; display: flex; align-items: center"
                >
                  <img
                    src="@/assets/deepseek.svg"
                    style="width: 30px; height: 30px"
                    alt=""
                  />
                  问心AI：
                  <div
                    v-if="item.think"
                    style="
                      padding: 4px 14px;
                      background: rgb(237 237 237);
                      margin-left: 10px;
                      border-radius: 10px;
                      font-size: 14px;
                    "
                  >
                    <i class="icon el-icon-check"></i>
                    已深度思考
                  </div>
                </div>
                <div
                  v-if="item.think && item.thinkContent"
                  style="
                    border-left: 2px solid rgba(0, 0, 0, 0.1);
                    padding-left: 8px;
                    margin-bottom: 30px;
                    color: #8b8b8b;
                    font-size: 14px;
                    margin-left: 40px;
                  "
                >
                  {{ item.thinkContent }}
                </div>
                <div style="margin-left: 40px">
                  <!-- 渲染普通文本内容（流式实时显示） -->
                  <div v-show="item.textContent !== undefined" style="white-space: pre-wrap;">{{ item.textContent }}</div>

                  <!-- 渲染HTML5文档卡片（与文本内容共存） -->
                  <div v-if="item.htmlContent" class="html-doc-card" @click="openHtmlPreview(item.htmlContent, item.htmlTitle, index)" style="margin-top: 12px;">
                    <div class="card-header">
                      <i class="el-icon-document"></i>
                      <span>{{ item.htmlTitle || '文档' }}</span>
                    </div>
                    <div class="card-body">
                      <i class="el-icon-view"></i>
                      <span>点击预览生成的文档</span>
                    </div>
                  </div>
                </div>

                <!-- 多轮思考：显示额外的思考内容 -->
                <template v-if="item.extraBlocks && item.extraBlocks.length > 0">
                  <template v-for="(block, blockIndex) in item.extraBlocks">
                    <!-- 额外的思考内容 -->
                    <div
                      v-if="block.type === 'thinking' && block.content"
                      :key="'think-' + blockIndex"
                      style="
                        border-left: 2px solid rgba(0, 0, 0, 0.1);
                        padding-left: 8px;
                        margin-top: 20px;
                        margin-bottom: 10px;
                        color: #8b8b8b;
                        font-size: 14px;
                        margin-left: 40px;
                      "
                    >
                      <div class="think-header" style="display: flex; align-items: center; margin-bottom: 8px;">
                        <i class="icon el-icon-loading" v-if="block.isThinking"></i>
                        <i class="icon el-icon-check" v-else></i>
                        {{ block.isThinking ? '正在思考...' : '继续思考' }}
                      </div>
                      {{ block.content }}
                    </div>
                  </template>
                </template>
              </div>
              <div
                class="answer-action"
                v-if="index === dialogue.length - 1 && item.role === 'assistant'"
              >
                <div class="action-btn">
                  <i class="icon el-icon-phone-outline"></i>
                  <i class="icon el-icon-document-copy"></i>
                  <i class="icon el-icon-refresh"></i>
                  <i class="icon el-icon-share">
                    <span style="margin-left: 4px; font-size: 12px">分享</span>
                  </i>
                  <i class="icon el-icon-more"></i>
                </div>
                <div class="realtive-msg">
                  <div
                    class="msg-item"
                    v-for="item in realtiveQuestion"
                    :key="item.msg"
                  >
                    {{ item.msg }}
                    <i class="icon el-icon-right"></i>
                  </div>
                </div>
              </div>
            </div>

            <Loading v-if="isLoading" loadintText="思考中" />
          </div>
        </div>

        <div class="search-input">
          <div class="input-content">
            <el-input
              class="input-style"
              v-model="searchKey"
              clearable
              type="textarea"
              :rows="1"
              autosize
              placeholder="输入你要撰写的主题"
              @keydown.enter.native="onAgent"
            />
            <div class="btn-group">
              <div class="switch-group">
                <div @click="think = !think" class="deep" :style="`${think?'color:#4D6BFE;background:#DBEAFE;border-color:rgba(0, 122, 255, 0.15);':''}`">
                  深度思考
                  <i v-if="!think" class="btn-icon el-icon-turn-off"></i>
                  <i v-else class="btn-icon el-icon-open"></i>
                </div>
                <div @click="search = !search" class="deep" :style="`${search?'color:#4D6BFE;background:#DBEAFE;border-color:rgba(0, 122, 255, 0.15);':''}`">
                  联网
                  <i v-if="!search" class="btn-icon el-icon-turn-off"></i>
                  <i v-else class="btn-icon el-icon-open"></i>
                </div>
                <div @click="editor = !editor" class="deep" :style="`${editor?'color:#4D6BFE;background:#DBEAFE;border-color:rgba(0, 122, 255, 0.15);':''}`">
                  文档编辑器
                  <i v-if="!editor" class="btn-icon el-icon-turn-off"></i>
                  <i v-else class="btn-icon el-icon-open"></i>
                </div>
              </div>

              <div class="icons">
                <voice class="icon" @getMessage="getMessage" />
                <i
                  v-if="!stopLoading"
                  class="icon el-icon-video-pause send"
                  @click="stopStreaming"
                ></i>
                <i v-else class="icon el-icon-top send" @click="onAgent"></i>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- HTML5 预览侧边栏 -->
    <transition name="slide">
      <div v-if="showHtmlPreview" class="html-preview-panel" :style="{ width: previewPanelWidth + 'px' }">
        <div class="preview-header">
          <span class="preview-title">
            <i :class="isHtmlGenerating ? 'el-icon-loading' : 'el-icon-document'"></i>
            {{ currentHtmlTitle || '文档预览' }}
            <span v-if="isHtmlGenerating" class="generating-badge">生成中</span>
          </span>
          <div class="preview-actions">
            <el-button
              type="success"
              size="small"
              icon="el-icon-download"
              @click="downloadAsWord"
              :loading="downloadingWord"
              :disabled="isHtmlGenerating"
            >
              Word
            </el-button>
            <el-button
              type="warning"
              size="small"
              icon="el-icon-download"
              @click="downloadAsPpt"
              :loading="downloadingPpt"
              :disabled="isHtmlGenerating"
            >
              PPT
            </el-button>
            <el-button
              type="info"
              size="small"
              icon="el-icon-download"
              @click="downloadAsExcel"
              :loading="downloadingExcel"
              :disabled="isHtmlGenerating"
            >
              Excel
            </el-button>
            <el-button
              v-if="isModelDocument"
              type="primary"
              size="small"
              icon="el-icon-s-data"
              @click="generateModel"
              :loading="generatingModel"
              :disabled="isHtmlGenerating"
            >
              生成模型
            </el-button>
            <i class="el-icon-close preview-close" @click="closeHtmlPreview"></i>
          </div>
        </div>
        <div class="preview-content">
          <iframe
            ref="htmlPreviewFrame"
            :srcdoc="currentHtmlContent"
            frameborder="0"
            sandbox="allow-scripts allow-same-origin"
          ></iframe>
        </div>
        <!-- 拖拽调整宽度的手柄 -->
        <div
          class="resize-handle"
          @mousedown="startResize"
        ></div>
      </div>
    </transition>
  </div>
</template>

<script>
  import voice from '@/views/index/components/AI/voice.vue'
  import mapData from '@/views/index/components/AI/mock/dataMap.js'
  import fullscreenSvg from '@/views/index/components/AI/icon/fullscreen.svg'
  import { getAgentList } from '@/api/ai/index.js'
  import { chatStream, executeSql, executeBatchSql } from '@/api/ai/starlight'
  import Loading from '@/components/Loading.vue'
  import defaultAvatar from '@/views/index/components/AI/icon/default-avatar.png'
  import a0 from '@/views/index/components/AI/icon/a0.png'
  import a1 from '@/views/index/components/AI/icon/a1.png'
  import a2 from '@/views/index/components/AI/icon/a2.png'
  import a3 from '@/views/index/components/AI/icon/a3.png'
  import a4 from '@/views/index/components/AI/icon/a4.png'
  import a5 from '@/views/index/components/AI/icon/a5.png'
  import a6 from '@/views/index/components/AI/icon/a6.png'
  import a7 from '@/views/index/components/AI/icon/a7.png'
  import a8 from '@/views/index/components/AI/icon/a8.png'
  import a9 from '@/views/index/components/AI/icon/a9.png'
  import a10 from '@/views/index/components/AI/icon/a10.png'
  import a11 from '@/views/index/components/AI/icon/a11.png'
  import a12 from '@/views/index/components/AI/icon/a12.png'
  import a13 from '@/views/index/components/AI/icon/a13.png'
  import a14 from '@/views/index/components/AI/icon/a14.png'
  import a15 from '@/views/index/components/AI/icon/a15.png'
  import a16 from '@/views/index/components/AI/icon/a16.png'
  import a17 from '@/views/index/components/AI/icon/a17.png'
  import a18 from '@/views/index/components/AI/icon/a18.png'
  import a19 from '@/views/index/components/AI/icon/a19.png'
  import a20 from '@/views/index/components/AI/icon/a20.png'
  import a21 from '@/views/index/components/AI/icon/a21.png'
  import a22 from '@/views/index/components/AI/icon/a22.png'
  import a23 from '@/views/index/components/AI/icon/a23.png'
  import a24 from '@/views/index/components/AI/icon/a24.png'
  import a25 from '@/views/index/components/AI/icon/a25.png'
  import a26 from '@/views/index/components/AI/icon/a26.png'
  import a27 from '@/views/index/components/AI/icon/a27.png'
  import a28 from '@/views/index/components/AI/icon/a28.png'
  import a29 from '@/views/index/components/AI/icon/a29.png'
  import a30 from '@/views/index/components/AI/icon/a30.png'
  import a31 from '@/views/index/components/AI/icon/a31.png'
  import a32 from '@/views/index/components/AI/icon/a32.png'
  import a33 from '@/views/index/components/AI/icon/a33.png'
  import a34 from '@/views/index/components/AI/icon/a34.png'
  import a35 from '@/views/index/components/AI/icon/a35.png'
  import a36 from '@/views/index/components/AI/icon/a36.png'
  import a37 from '@/views/index/components/AI/icon/a37.png'
  import a38 from '@/views/index/components/AI/icon/a38.png'

  export default {
    props: {
      menuInfo: {
        type: Object,
        default: () => ({})
      }
    },
    components: { voice, Loading },
    data() {
      return {
        defaultAvatar,
        loading: false,
        searchKey: '',
        source: '参考资料',
        userName: '',
        relativeKey: '全部',
        value1: '',
        value2: '',
        think: false,
        search: false,
        editor: false,
        relativeList: mapData.smartModelCategory,
        allList: [],
        dialogVisible: false,
        curCard: {},
        review: true,
        iframeUrl: '',
        fullscreenSvg,
        iconList: [a0,a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25,a26,a27,a28,a29,a30,a31,a32,a33,a34,a35,a36,a37,a38],
        // AI 对话相关
        sessionId: null,
        isStreaming: false,
        abortController: null,
        dialogue: [], // 对话列表
        realtiveQuestion: [
          { msg: 'What can you do?' },
          { msg: 'Can you tell me a joke?' },
          { msg: 'Who created you?' },
        ],
        isLoading: false, //正在回复
        stopLoading: true,
        // HTML5 预览相关
        showHtmlPreview: false,
        currentHtmlContent: '',
        currentHtmlTitle: '',
        previewPanelWidth: 1120,
        isResizing: false,
        startX: 0,
        startWidth: 0,
        isModelDocument: false, // 是否是模型文档
        generatingModel: false, // 是否正在生成模型
        lastHtmlIndex: -1, // 记录最后一个HTML文档的索引
        downloadingWord: false, // 是否正在下载Word
        downloadingPpt: false, // 是否正在下载PPT
        downloadingExcel: false, // 是否正在下载Excel
        isHtmlGenerating: false, // 是否正在生成HTML文档
        currentHtmlIndex: -1 // 当前正在生成HTML的对话索引
      }
    },
    watch: {
      'menuInfo.label': {
        deep: true,
        handler(val) {
          this.curCard = {}
          this.searchKey = ''
          this.iframeUrl = ''
          this.fetchData()
        },
      }
    },
    computed: {
      allFillterList() {
        return this.allList.filter(x => this.relativeKey === '全部' || x.agentType === this.relativeKey)
      }
    },
    created() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      this.userName = userInfo.realname
      console.log('relativeList', this.relativeList)
      this.sessionId = 'session_' + Date.now()
      this.fetchData()
    },
    methods: {
      /**
       * 获取北京时间（东八区）
       * @returns {String} 格式化的北京时间字符串
       */
      getBeijingTime() {
        const now = new Date()
        // 获取UTC时间戳
        const utcTime = now.getTime() + (now.getTimezoneOffset() * 60000)
        // 转换为北京时间（UTC+8）
        const beijingTime = new Date(utcTime + (8 * 60 * 60 * 1000))

        const year = beijingTime.getFullYear()
        const month = String(beijingTime.getMonth() + 1).padStart(2, '0')
        const day = String(beijingTime.getDate()).padStart(2, '0')
        const hours = String(beijingTime.getHours()).padStart(2, '0')
        const minutes = String(beijingTime.getMinutes()).padStart(2, '0')
        const seconds = String(beijingTime.getSeconds()).padStart(2, '0')

        const weekDays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
        const weekDay = weekDays[beijingTime.getDay()]

        return {
          fullTime: `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`,
          date: `${year}-${month}-${day}`,
          time: `${hours}:${minutes}:${seconds}`,
          weekDay: weekDay,
          timestamp: beijingTime.getTime(),
          formatted: `${year}年${month}月${day}日 ${weekDay} ${hours}:${minutes}:${seconds}`
        }
      },
      async fetchData() {
        this.loading = true
        // const model = localStorage.getItem('model') || ''
        getAgentList({pageSize: 99999, /* moduleRoute: model */}).then(res => {
          console.log('res', res)
          if (res && res.data && res.data.tlist.length) {
            this.allList = (res.data.tlist || []).filter(x => x.agentType === this.menuInfo.label)
          }
        }).finally(() => {
          this.loading = false
        })
      },
      onTypeClick(item) {
        this.relativeKey = item.label
      },
      onCardClick(item) {
        console.log('item', item)
        this.curCard = item
        this.searchKey = item.describe
        this.iframeUrl = item.jumpAddress
      },
      onAgent() {
        if (!this.searchKey.trim()) return this.$message.error('请输入内容！')
        if (this.isStreaming) return

        // 获取当前北京时间
        const beijingTime = this.getBeijingTime()

        // 构建包含时间上下文的消息
        const userMessage = this.searchKey
        const messageWithTimeContext = `当前北京时间：${beijingTime.formatted}\n\n用户问题：${userMessage}`

        // 添加用户问题到对话列表（显示原始问题）
        this.dialogue.push({
          role: 'user',
          content: this.searchKey
        })

        // 添加一个空的 AI 回复对象
        this.dialogue.push({
          role: 'assistant',
          content: '',
          textContent: '',
          htmlContent: '',
          htmlTitle: '',
          thinkContent: '',
          think: this.think,
          extraBlocks: [] // 用于存储多轮思考和内容
        })

        this.isStreaming = true
        this.isLoading = true
        this.stopLoading = false

        const currentIndex = this.dialogue.length - 1

        // 滚动到底部
        this.$nextTick(() => {
          this.scrollToBottom()
        })

        // 调用流式接口（发送包含时间上下文的消息）
        this.abortController = chatStream(
          {
            message: messageWithTimeContext,
            sessionId: this.sessionId,
            enableThinking: this.think,
            enableWebSearch: this.search,
            stream: true,
            // 额外的时间信息，供后端使用
            currentTime: beijingTime.fullTime,
            timestamp: beijingTime.timestamp,
            timezone: 'Asia/Shanghai'
          },
          {
            onThinking: (data) => {
              const dialogueItem = this.dialogue[currentIndex]

              if (data.type === 'start') {
                // 新一轮思考开始
                // 如果已经有第一轮正式内容（content不为空），说明是多轮思考
                if (dialogueItem.content.trim()) {
                  // 添加新的思考块
                  dialogueItem.extraBlocks.push({
                    type: 'thinking',
                    content: '',
                    isThinking: true
                  })
                }
              } else if (data.type === 'content') {
                // 判断是追加到第一轮思考还是额外的思考块
                // 检查是否有额外的思考块
                const lastThinkingBlock = dialogueItem.extraBlocks.filter(b => b.type === 'thinking').pop()

                if (lastThinkingBlock && lastThinkingBlock.isThinking) {
                  // 追加到额外的思考块
                  lastThinkingBlock.content += data.content
                } else {
                  // 第一轮思考
                  dialogueItem.thinkContent += data.content
                }
                this.scrollToBottom()
              } else if (data.type === 'end') {
                // 思考结束，更新状态
                const lastThinkingBlock = dialogueItem.extraBlocks.filter(b => b.type === 'thinking').pop()
                if (lastThinkingBlock) {
                  lastThinkingBlock.isThinking = false
                }
              }
            },
            onMessage: (content) => {
              const dialogueItem = this.dialogue[currentIndex]

              // 始终将内容追加到主 content，以便 parseHtmlContentRealtime 能正确解析 HTML
              // 这样可以确保 HTML 标记能被正确识别和处理
              dialogueItem.content += content

              // 实时解析 HTML5 标记，分离普通文本和HTML内容
              this.parseHtmlContentRealtime(currentIndex)

              this.isLoading = false
              this.scrollToBottom()
            },
            onToolCall: (tool) => {
              // 可以在这里处理工具调用
            },
            onProgress: (message) => {
              const dialogueItem = this.dialogue[currentIndex]

              // 进度消息追加到主 content
              dialogueItem.content += '\n' + message

              // 实时解析，确保进度消息被正确显示
              this.parseHtmlContentRealtime(currentIndex)

              this.scrollToBottom()
            },
            onError: (error) => {
              this.isLoading = false
              this.dialogue[currentIndex].content = `请求失败: ${error}`
              this.isStreaming = false
              this.stopLoading = true
              this.$message.error(`请求失败: ${error}`)
            },
            onComplete: () => {
              this.isLoading = false
              this.isStreaming = false
              this.stopLoading = true

              // 流式输出完成后，最终解析一次 HTML 内容（确保完整解析）
              this.parseHtmlContent(currentIndex)

              // 如果有新的HTML文档，自动打开预览
              if (this.dialogue[currentIndex].htmlContent && this.lastHtmlIndex !== currentIndex) {
                this.lastHtmlIndex = currentIndex
                this.openHtmlPreview(
                  this.dialogue[currentIndex].htmlContent,
                  this.dialogue[currentIndex].htmlTitle,
                  currentIndex
                )
              }
            }
          }
        )

        this.searchKey = ''
      },
      scrollToBottom() {
        this.$nextTick(() => {
          const container = this.$refs.chatContent
          if (container) {
            container.scrollTop = container.scrollHeight
          }
        })
      },
      stopStreaming() {
        if (this.abortController) {
          this.abortController.abort()
          this.abortController = null
        }
        this.isStreaming = false
        this.isLoading = false
        this.stopLoading = true
      },
      getMessage(rectxt) {
        this.searchKey = rectxt
      },
      /**
       * 实时解析 HTML5 内容（支持流式增量解析）
       * 在流式接收过程中，实时分离普通文本和 HTML 内容
       * 当检测到HTML开始标记时，立即打开预览面板并实时渲染
       */
      parseHtmlContentRealtime(index) {
        const content = this.dialogue[index].content
        const startTag = '<<<HTML5_START'
        const endTag = '<<<HTML5_END>>>'

        console.log('[HTML解析] 内容长度:', content.length, '内容预览:', content.substring(0, 200))

        // 如果还没有找到完整的 HTML 标记，保持全部作为普通文本
        const startIndex = content.indexOf(startTag)
        if (startIndex === -1) {
          this.dialogue[index].textContent = content
          this.dialogue[index].htmlContent = ''
          this.dialogue[index].htmlTitle = ''
          return
        }

        console.log('[HTML解析] 找到开始标记，位置:', startIndex)

        // 提取 HTML 标记之前的普通文本
        const textBefore = content.substring(0, startIndex).trim()

        // 提取标题（如果有）
        const titleEndIndex = content.indexOf('>>>', startIndex)
        let htmlTitle = '文档'
        if (titleEndIndex !== -1) {
          const titlePart = content.substring(startIndex + startTag.length, titleEndIndex)
          if (titlePart.startsWith(':')) {
            htmlTitle = titlePart.substring(1).trim()
          }
        }

        const endIndex = content.indexOf(endTag)

        // 检测到HTML开始但还没结束 - 实时预览模式
        if (endIndex === -1) {
          this.dialogue[index].textContent = textBefore

          // 如果找到了标题结束标记，提取正在生成的HTML内容
          if (titleEndIndex !== -1) {
            const htmlStartIndex = titleEndIndex + 3
            const partialHtmlContent = content.substring(htmlStartIndex).trim()

            // 更新对话数据
            this.dialogue[index].htmlContent = partialHtmlContent
            this.dialogue[index].htmlTitle = htmlTitle

            // 如果还没打开预览面板，打开它
            if (!this.showHtmlPreview || this.currentHtmlIndex !== index) {
              this.currentHtmlIndex = index
              this.isHtmlGenerating = true
              this.showHtmlPreview = true
              this.currentHtmlTitle = htmlTitle + ' (生成中...)'
              this.isModelDocument = htmlTitle.includes('模型') || htmlTitle.includes('数据模型') || htmlTitle.includes('表结构')
            }

            // 实时更新预览内容 - 添加生成中的样式
            this.currentHtmlContent = this.wrapPartialHtml(partialHtmlContent)
          }
          return
        }

        // 找到了完整的HTML内容
        const htmlStartIndex = titleEndIndex + 3
        const htmlContent = content.substring(htmlStartIndex, endIndex).trim()

        // 提取 HTML 标记之后的普通文本
        const textAfter = content.substring(endIndex + endTag.length).trim()
        const textContent = (textBefore + (textBefore && textAfter ? '\n\n' : '') + textAfter).trim()

        // 更新对话框数据
        this.dialogue[index].textContent = textContent
        this.dialogue[index].htmlContent = htmlContent
        this.dialogue[index].htmlTitle = htmlTitle

        // 更新预览面板 - 完成状态
        if (this.showHtmlPreview && this.currentHtmlIndex === index) {
          this.isHtmlGenerating = false
          this.currentHtmlTitle = htmlTitle
          this.currentHtmlContent = htmlContent
        }

        // 检查是否是模型文档
        this.isModelDocument = htmlTitle.includes('模型') || htmlTitle.includes('数据模型') || htmlTitle.includes('表结构')
      },

      /**
       * 包装部分HTML内容，添加生成中的视觉效果
       */
      wrapPartialHtml(partialContent) {
        return `
          <!DOCTYPE html>
          <html>
          <head>
            <meta charset="UTF-8">
            <style>
              body {
                font-family: 'Microsoft YaHei', Arial, sans-serif;
                padding: 20px;
                line-height: 1.6;
              }
              .generating-indicator {
                position: fixed;
                top: 10px;
                right: 10px;
                background: linear-gradient(90deg, #4D6BFE, #7B8CFF);
                color: white;
                padding: 8px 16px;
                border-radius: 20px;
                font-size: 14px;
                display: flex;
                align-items: center;
                gap: 8px;
                box-shadow: 0 2px 8px rgba(77, 107, 254, 0.3);
                z-index: 1000;
              }
              .generating-indicator::before {
                content: '';
                width: 8px;
                height: 8px;
                background: white;
                border-radius: 50%;
                animation: pulse 1s infinite;
              }
              @keyframes pulse {
                0%, 100% { opacity: 1; transform: scale(1); }
                50% { opacity: 0.5; transform: scale(0.8); }
              }
              .cursor-blink {
                display: inline-block;
                width: 2px;
                height: 1em;
                background: #4D6BFE;
                animation: blink 0.8s infinite;
                vertical-align: text-bottom;
                margin-left: 2px;
              }
              @keyframes blink {
                0%, 50% { opacity: 1; }
                51%, 100% { opacity: 0; }
              }
            </style>
          </head>
          <body>
            <div class="generating-indicator">正在生成文档...</div>
            ${partialContent}<span class="cursor-blink"></span>
          </body>
          </html>
        `
      },

      /**
       * 最终解析 HTML5 内容（在流式完成后调用）
       * 格式: <<<HTML5_START:文档标题>>>...html内容...<<<HTML5_END>>>
       */
      parseHtmlContent(index) {
        const content = this.dialogue[index].content
        const startTag = '<<<HTML5_START'
        const endTag = '<<<HTML5_END>>>'

        const startIndex = content.indexOf(startTag)
        const endIndex = content.indexOf(endTag)

        if (startIndex !== -1 && endIndex !== -1 && endIndex > startIndex) {
          // 提取标题（如果有）
          const titleEndIndex = content.indexOf('>>>', startIndex)
          let htmlTitle = '文档'
          if (titleEndIndex !== -1 && titleEndIndex < endIndex) {
            const titlePart = content.substring(startIndex + startTag.length, titleEndIndex)
            if (titlePart.startsWith(':')) {
              htmlTitle = titlePart.substring(1).trim()
            }
          }

          // 提取 HTML 内容
          const htmlStartIndex = content.indexOf('>>>', startIndex) + 3
          const htmlContent = content.substring(htmlStartIndex, endIndex).trim()

          // 提取普通文本内容(HTML 标记之前和之后的内容)
          const textBefore = content.substring(0, startIndex).trim()
          const textAfter = content.substring(endIndex + endTag.length).trim()
          const textContent = (textBefore + (textBefore && textAfter ? '\n\n' : '') + textAfter).trim()

          this.dialogue[index].htmlContent = htmlContent
          this.dialogue[index].htmlTitle = htmlTitle
          this.dialogue[index].textContent = textContent

          // 检查是否是模型文档（标题包含"模型"关键字）
          this.isModelDocument = htmlTitle.includes('模型') || htmlTitle.includes('数据模型') || htmlTitle.includes('表结构')
        } else if (startIndex !== -1) {
          // 只找到开始标记,还在接收中
          const textContent = content.substring(0, startIndex).trim()
          this.dialogue[index].textContent = textContent
        } else {
          // 没有 HTML 标记,全部作为普通文本
          this.dialogue[index].textContent = content
          this.dialogue[index].htmlContent = ''
          this.dialogue[index].htmlTitle = ''
        }
      },
      /**
       * 打开 HTML 预览
       */
      openHtmlPreview(htmlContent, htmlTitle, index) {
        this.currentHtmlContent = htmlContent
        this.currentHtmlTitle = htmlTitle || '文档预览'
        this.showHtmlPreview = true
        this.isHtmlGenerating = false // 手动打开时，文档已完成
        this.currentHtmlIndex = index !== undefined ? index : -1

        // 检查是否是模型文档
        this.isModelDocument = (htmlTitle && (htmlTitle.includes('模型') || htmlTitle.includes('数据模型') || htmlTitle.includes('表结构')))
      },
      /**
       * 关闭 HTML 预览
       */
      closeHtmlPreview() {
        this.showHtmlPreview = false
        this.currentHtmlContent = ''
        this.currentHtmlTitle = ''
        this.isModelDocument = false
        this.isHtmlGenerating = false
        this.currentHtmlIndex = -1
      },
      /**
       * 生成模型（调用后端接口）
       */
      async generateModel() {
        if (this.generatingModel) return

        this.generatingModel = true
        try {
          // 从 HTML 内容中提取 SQL 语句
          const sqlStatements = this.extractSqlFromHtml(this.currentHtmlContent)

          if (!sqlStatements || sqlStatements.length === 0) {
            this.$message.warning('未找到有效的 SQL 语句')
            return
          }

          // 调用后端接口批量执行 SQL
          const response = await this.executeSqlStatements(sqlStatements)

          if (response.success) {
            this.$message.success('模型生成成功！')
            this.closeHtmlPreview()
          } else {
            this.$message.error('模型生成失败：' + response.message)
          }
        } catch (error) {
          console.error('生成模型失败:', error)
          this.$message.error('生成模型失败：' + error.message)
        } finally {
          this.generatingModel = false
        }
      },
      /**
       * 从 HTML 内容中提取 SQL 语句
       */
      extractSqlFromHtml(htmlContent) {
        const sqlStatements = []

        // 使用正则表达式提取 SQL 语句
        // 匹配 <pre> 或 <code> 标签中的 SQL 语句
        const codeBlockRegex = /<(?:pre|code)[^>]*>([\s\S]*?)<\/(?:pre|code)>/gi
        let match

        while ((match = codeBlockRegex.exec(htmlContent)) !== null) {
          // 移除内部HTML标签，只保留纯文本
          const code = match[1].replace(/<[^>]+>/g, '').trim()

          // 检查是否是有效的SQL语句
          const upperCode = code.toUpperCase()
          if (upperCode.includes('CREATE TABLE') ||
              upperCode.includes('INSERT INTO') ||
              upperCode.includes('ALTER TABLE') ||
              upperCode.includes('UPDATE ') ||
              upperCode.includes('DELETE FROM')) {
            // 确保SQL语句以分号结尾（批量执行需要）
            let sql = code
            if (!sql.endsWith(';')) {
              sql += ';'
            }
            sqlStatements.push(sql)
          }
        }

        return sqlStatements
      },
      /**
       * 执行 SQL 语句（调用后端接口）
       */
      async executeSqlStatements(sqlStatements) {
        try {
          // 调用批量执行SQL接口
          const response = await executeBatchSql(sqlStatements, 'dm')

          if (response.success) {
            return {
              success: true,
              message: response.message || '执行成功'
            }
          } else {
            return {
              success: false,
              message: response.message || '执行失败'
            }
          }
        } catch (error) {
          console.error('执行SQL失败:', error)
          return {
            success: false,
            message: error.message || '执行失败'
          }
        }
      },
      /**
       * 开始调整预览面板宽度
       */
      startResize(e) {
        this.isResizing = true
        this.startX = e.clientX
        this.startWidth = this.previewPanelWidth

        document.addEventListener('mousemove', this.handleResize)
        document.addEventListener('mouseup', this.stopResize)
        e.preventDefault()
      },
      /**
       * 处理调整宽度
       */
      handleResize(e) {
        if (!this.isResizing) return

        const deltaX = this.startX - e.clientX
        const newWidth = this.startWidth + deltaX

        // 限制最小和最大宽度
        if (newWidth >= 400 && newWidth <= 1200) {
          this.previewPanelWidth = newWidth
        }
      },
      /**
       * 停止调整宽度
       */
      stopResize() {
        this.isResizing = false
        document.removeEventListener('mousemove', this.handleResize)
        document.removeEventListener('mouseup', this.stopResize)
      },
      /**
       * 下载为Word文档
       */
      async downloadAsWord() {
        if (!this.currentHtmlContent) {
          this.$message.warning('没有可下载的文档内容')
          return
        }

        this.downloadingWord = true
        try {
          // 动态导入html-docx-js库
          const htmlDocx = await import('html-docx-js/dist/html-docx')

          // 准备HTML内容
          let htmlContent = this.currentHtmlContent

          // 如果HTML不包含完整的文档结构，添加基本结构
          if (!htmlContent.includes('<!DOCTYPE') && !htmlContent.includes('<html')) {
            htmlContent = `
              <!DOCTYPE html>
              <html>
                <head>
                  <meta charset="UTF-8">
                  <title>${this.currentHtmlTitle || '文档'}</title>
                  <style>
                    body { font-family: 'Microsoft YaHei', Arial, sans-serif; line-height: 1.6; }
                    h1 { color: #333; font-size: 24px; margin-bottom: 20px; }
                    h2 { color: #555; font-size: 20px; margin-top: 20px; margin-bottom: 15px; }
                    h3 { color: #666; font-size: 18px; margin-top: 15px; margin-bottom: 10px; }
                    p { margin-bottom: 10px; }
                    table { border-collapse: collapse; width: 100%; margin: 20px 0; }
                    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                    th { background-color: #f2f2f2; font-weight: bold; }
                    code { background-color: #f4f4f4; padding: 2px 6px; border-radius: 3px; }
                    pre { background-color: #f4f4f4; padding: 10px; border-radius: 5px; overflow-x: auto; }
                  </style>
                </head>
                <body>
                  ${htmlContent}
                </body>
              </html>
            `
          }

          // 转换HTML为Word文档
          const converted = htmlDocx.asBlob(htmlContent)

          // 创建下载链接
          const url = URL.createObjectURL(converted)
          const link = document.createElement('a')
          link.href = url
          link.download = `${this.currentHtmlTitle || '文档'}.docx`
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          URL.revokeObjectURL(url)

          this.$message.success('Word文档下载成功')
        } catch (error) {
          console.error('下载Word失败:', error)
          this.$message.error('下载Word文档失败: ' + error.message)
        } finally {
          this.downloadingWord = false
        }
      },
      /**
       * 下载为PPT文档
       */
      async downloadAsPpt() {
        if (!this.currentHtmlContent) {
          this.$message.warning('没有可下载的文档内容')
          return
        }

        this.downloadingPpt = true
        try {
          // 动态导入pptxgenjs库
          const PptxGenJS = (await import('pptxgenjs')).default

          const pptx = new PptxGenJS()

          // 解析HTML内容
          const parser = new DOMParser()
          const doc = parser.parseFromString(this.currentHtmlContent, 'text/html')

          // 检查是否有 class="slide" 的结构（AI 生成的 PPT 格式）
          const slides = doc.querySelectorAll('.slide')

          if (slides.length > 0) {
            // 使用 slide 结构生成 PPT
            this.convertSlidesToPpt(pptx, slides)
          } else {
            // 使用传统的标题结构生成 PPT
            this.convertHtmlToPpt(pptx, doc)
          }

          // 保存PPT
          await pptx.writeFile({ fileName: `${this.currentHtmlTitle || '文档'}.pptx` })
          this.$message.success('PPT文档下载成功')
        } catch (error) {
          console.error('下载PPT失败:', error)
          this.$message.error('下载PPT文档失败: ' + error.message)
        } finally {
          this.downloadingPpt = false
        }
      },
      /**
       * 下载为Excel文档
       */
      async downloadAsExcel() {
        if (!this.currentHtmlContent) {
          this.$message.warning('没有可下载的文档内容')
          return
        }

        this.downloadingExcel = true
        try {
          // 动态导入xlsx库
          const XLSX = await import('xlsx')

          // 解析HTML内容
          const parser = new DOMParser()
          const doc = parser.parseFromString(this.currentHtmlContent, 'text/html')

          // 创建工作簿
          const workbook = XLSX.utils.book_new()

          // 查找所有表格
          const tables = doc.querySelectorAll('table')

          if (tables.length > 0) {
            // 如果有表格，每个表格创建一个工作表
            tables.forEach((table, index) => {
              const tableData = this.parseTableToArray(table)
              const worksheet = XLSX.utils.aoa_to_sheet(tableData)

              // 设置列宽
              const colWidths = []
              if (tableData.length > 0) {
                for (let i = 0; i < tableData[0].length; i++) {
                  const maxLength = Math.max(
                    ...tableData.map(row => (row[i] ? String(row[i]).length : 0))
                  )
                  colWidths.push({ wch: Math.min(maxLength + 2, 50) })
                }
              }
              worksheet['!cols'] = colWidths

              XLSX.utils.book_append_sheet(workbook, worksheet, `表格${index + 1}`)
            })
          } else {
            // 如果没有表格，将文本内容转换为表格
            const textContent = []
            const elements = doc.body.children

            // 添加标题行
            textContent.push(['内容'])

            for (let i = 0; i < elements.length; i++) {
              const element = elements[i]
              const text = element.textContent.trim()
              if (text) {
                textContent.push([text])
              }
            }

            const worksheet = XLSX.utils.aoa_to_sheet(textContent)
            worksheet['!cols'] = [{ wch: 100 }]
            XLSX.utils.book_append_sheet(workbook, worksheet, '内容')
          }

          // 保存Excel文件
          XLSX.writeFile(workbook, `${this.currentHtmlTitle || '文档'}.xlsx`)
          this.$message.success('Excel文档下载成功')
        } catch (error) {
          console.error('下载Excel失败:', error)
          this.$message.error('下载Excel文档失败: ' + error.message)
        } finally {
          this.downloadingExcel = false
        }
      },
      /**
       * 将表格元素转换为二维数组
       */
      parseTableToArray(tableElement) {
        const rows = tableElement.querySelectorAll('tr')
        const tableData = []

        rows.forEach(row => {
          const cells = row.querySelectorAll('th, td')
          const rowData = []
          cells.forEach(cell => {
            rowData.push(cell.textContent.trim())
          })
          if (rowData.length > 0) {
            tableData.push(rowData)
          }
        })

        return tableData
      },
      /**
       * 将内容添加到PPT幻灯片
       */
      addContentToSlide(slide, content) {
        let yPos = 1.5

        content.forEach(item => {
          if (item.text && yPos < 6) {
            slide.addText(item.text, {
              x: 0.5,
              y: yPos,
              w: 9,
              h: 0.5,
              fontSize: 14,
              color: '333333',
              breakLine: true
            })
            yPos += 0.6
          }
        })
      },
      /**
       * 将 slide 结构转换为 PPT（AI 生成的格式）- 完全重写版
       */
      convertSlidesToPpt(pptx, slides) {
        slides.forEach((slideElement, index) => {
          const slide = pptx.addSlide()

          // 检查是否是封面页或结束页
          const isCover = slideElement.classList.contains('cover-slide')
          const isEnd = slideElement.classList.contains('end-slide')

          if (isCover || isEnd) {
            // ========== 封面页/结束页 ==========
            slide.background = { color: '1890FF' }

            const h1 = slideElement.querySelector('h1')
            if (h1) {
              slide.addText(h1.textContent.trim(), {
                x: 0.5,
                y: 2.5,
                w: 9,
                h: 1,
                fontSize: 48,
                bold: true,
                color: 'FFFFFF',
                align: 'center',
                fontFace: 'Microsoft YaHei'
              })
            }

            const subtitle = slideElement.querySelector('.subtitle')
            if (subtitle) {
              slide.addText(subtitle.textContent.trim(), {
                x: 0.5,
                y: 3.7,
                w: 9,
                h: 0.5,
                fontSize: 20,
                color: 'FFFFFF',
                align: 'center',
                fontFace: 'Microsoft YaHei'
              })
            }

            const company = slideElement.querySelector('.company')
            if (company) {
              slide.addText(company.textContent.trim(), {
                x: 0.5,
                y: 4.5,
                w: 9,
                h: 0.4,
                fontSize: 18,
                color: 'FFFFFF',
                align: 'center',
                fontFace: 'Microsoft YaHei'
              })
            }

            const date = slideElement.querySelector('.date')
            if (date) {
              slide.addText(date.textContent.trim(), {
                x: 0.5,
                y: 5.1,
                w: 9,
                h: 0.4,
                fontSize: 16,
                color: 'FFFFFF',
                align: 'center',
                fontFace: 'Microsoft YaHei'
              })
            }
          } else {
            // ========== 普通内容页 ==========
            slide.background = { color: 'F8FBFF' }

            // 标题
            const h2 = slideElement.querySelector('h2')
            if (h2) {
              slide.addText(h2.textContent.trim(), {
                x: 0.5,
                y: 0.4,
                w: 9,
                h: 0.6,
                fontSize: 28,
                bold: true,
                color: '1890FF',
                fontFace: 'Microsoft YaHei'
              })
            }

            // 处理 feature-list（列表布局）
            const featureList = slideElement.querySelector('.feature-list')
            if (featureList) {
              this.convertFeatureList(pptx, slide, featureList)
            }

            // 处理 content-grid（网格布局）
            const contentGrid = slideElement.querySelector('.content-grid')
            if (contentGrid) {
              this.convertContentGrid(pptx, slide, contentGrid)
            }

            // 处理 architecture（架构图）
            const architecture = slideElement.querySelector('.architecture')
            if (architecture) {
              this.convertArchitecture(pptx, slide, architecture)
            }

            // 处理 process-flow（流程图）
            const processFlow = slideElement.querySelector('.process-flow')
            if (processFlow) {
              this.convertProcessFlow(pptx, slide, processFlow)
            }

            // 处理表格
            const table = slideElement.querySelector('table')
            if (table) {
              this.convertTable(pptx, slide, table)
            }

            // 处理 highlight-box（高亮框）
            const highlightBox = slideElement.querySelector('.highlight-box')
            if (highlightBox) {
              this.convertHighlightBox(pptx, slide, highlightBox)
            }
          }
        })
      },

      /**
       * 转换 feature-list（列表布局）
       */
      convertFeatureList(pptx, slide, featureList) {
        const items = featureList.querySelectorAll('li')
        let yPos = 1.3
        const maxItems = Math.min(items.length, 6) // 最多6个

        for (let i = 0; i < maxItems; i++) {
          const item = items[i]
          const icon = item.querySelector('.icon')
          const text = item.querySelector('.text')
          const h4 = text ? text.querySelector('h4') : null
          const p = text ? text.querySelector('p') : null

          // 图标圆圈
          if (icon) {
            slide.addShape(pptx.ShapeType.ellipse, {
              x: 0.6,
              y: yPos,
              w: 0.5,
              h: 0.5,
              fill: { color: '1890FF' }
            })

            slide.addText(icon.textContent.trim(), {
              x: 0.6,
              y: yPos,
              w: 0.5,
              h: 0.5,
              fontSize: 18,
              bold: true,
              color: 'FFFFFF',
              align: 'center',
              valign: 'middle',
              fontFace: 'Arial'
            })
          }

          // 标题
          if (h4) {
            slide.addText(h4.textContent.trim(), {
              x: 1.3,
              y: yPos,
              w: 8.2,
              h: 0.35,
              fontSize: 18,
              bold: true,
              color: '262626',
              fontFace: 'Microsoft YaHei'
            })
          }

          // 描述
          if (p) {
            slide.addText(p.textContent.trim(), {
              x: 1.3,
              y: yPos + 0.35,
              w: 8.2,
              h: 0.4,
              fontSize: 14,
              color: '595959',
              fontFace: 'Microsoft YaHei'
            })
          }

          yPos += 0.95
        }
      },

      /**
       * 转换 content-grid（网格布局）
       */
      convertContentGrid(pptx, slide, contentGrid) {
        const items = contentGrid.querySelectorAll('.content-item')
        const itemCount = Math.min(items.length, 4) // 最多4个
        const cols = 2
        const rows = Math.ceil(itemCount / cols)

        const cardWidth = 4.2
        const cardHeight = 2.5
        const gapX = 0.6
        const gapY = 0.4
        const startX = 0.6
        const startY = 1.5

        for (let i = 0; i < itemCount; i++) {
          const item = items[i]
          const row = Math.floor(i / cols)
          const col = i % cols

          const x = startX + col * (cardWidth + gapX)
          const y = startY + row * (cardHeight + gapY)

          // 卡片背景
          slide.addShape(pptx.ShapeType.rect, {
            x: x,
            y: y,
            w: cardWidth,
            h: cardHeight,
            fill: { color: 'F8FBFF' }
          })

          // 顶部蓝色条
          slide.addShape(pptx.ShapeType.rect, {
            x: x,
            y: y,
            w: cardWidth,
            h: 0.15,
            fill: { color: '1890FF' }
          })

          // 标题
          const h3 = item.querySelector('h3')
          if (h3) {
            slide.addText(h3.textContent.trim(), {
              x: x + 0.2,
              y: y + 0.3,
              w: cardWidth - 0.4,
              h: 0.4,
              fontSize: 16,
              bold: true,
              color: '1890FF',
              fontFace: 'Microsoft YaHei'
            })
          }

          // 列表项
          const ul = item.querySelector('ul')
          if (ul) {
            const lis = ul.querySelectorAll('li')
            let liY = y + 0.8

            lis.forEach((li, index) => {
              if (index < 4 && liY < y + cardHeight - 0.2) { // 最多4项
                slide.addText('• ' + li.textContent.trim(), {
                  x: x + 0.3,
                  y: liY,
                  w: cardWidth - 0.6,
                  h: 0.35,
                  fontSize: 12,
                  color: '595959',
                  fontFace: 'Microsoft YaHei'
                })
                liY += 0.4
              }
            })
          }
        }
      },

      /**
       * 转换 architecture（架构图）
       */
      convertArchitecture(pptx, slide, architecture) {
        const layers = architecture.querySelectorAll('.layer')
        let yPos = 2
        const layerHeight = 0.7
        const layerGap = 0.3

        layers.forEach((layer, index) => {
          const y = yPos + index * (layerHeight + layerGap)

          // 根据 class 设置颜色
          let color = '1890FF'
          if (layer.classList.contains('layer-1')) color = '1890FF'
          else if (layer.classList.contains('layer-2')) color = '40A9FF'
          else if (layer.classList.contains('layer-3')) color = '69C0FF'
          else if (layer.classList.contains('layer-4')) color = '91D5FF'

          slide.addShape(pptx.ShapeType.rect, {
            x: 1.5,
            y: y,
            w: 7,
            h: layerHeight,
            fill: { color: color }
          })

          slide.addText(layer.textContent.trim(), {
            x: 1.5,
            y: y,
            w: 7,
            h: layerHeight,
            fontSize: 16,
            bold: true,
            color: 'FFFFFF',
            align: 'center',
            valign: 'middle',
            fontFace: 'Microsoft YaHei'
          })
        })
      },

      /**
       * 转换 process-flow（流程图）
       */
      convertProcessFlow(pptx, slide, processFlow) {
        const steps = processFlow.querySelectorAll('.process-step')
        const stepCount = Math.min(steps.length, 5) // 最多5个
        const stepWidth = 8.5 / stepCount
        const startX = 0.75
        const y = 2.5

        for (let i = 0; i < stepCount; i++) {
          const step = steps[i]
          const x = startX + i * stepWidth

          // 步骤背景（渐变蓝色）
          slide.addShape(pptx.ShapeType.rect, {
            x: x,
            y: y,
            w: stepWidth - 0.3,
            h: 2,
            fill: { color: '1890FF' }
          })

          // 步骤编号
          const stepNumber = step.querySelector('.step-number')
          if (stepNumber) {
            slide.addText(stepNumber.textContent.trim(), {
              x: x,
              y: y + 0.3,
              w: stepWidth - 0.3,
              h: 0.6,
              fontSize: 32,
              bold: true,
              color: 'FFFFFF',
              align: 'center',
              fontFace: 'Arial'
            })
          }

          // 步骤标题
          const stepTitle = step.querySelector('.step-title')
          if (stepTitle) {
            slide.addText(stepTitle.textContent.trim(), {
              x: x,
              y: y + 1.1,
              w: stepWidth - 0.3,
              h: 0.7,
              fontSize: 14,
              bold: true,
              color: 'FFFFFF',
              align: 'center',
              fontFace: 'Microsoft YaHei'
            })
          }

          // 箭头（除了最后一个）
          if (i < stepCount - 1) {
            slide.addText('→', {
              x: x + stepWidth - 0.3,
              y: y + 0.8,
              w: 0.3,
              h: 0.4,
              fontSize: 24,
              color: '1890FF',
              align: 'center'
            })
          }
        }
      },

      /**
       * 转换表格
       */
      convertTable(pptx, slide, table) {
        const tableData = this.parseTableToArray(table)
        if (tableData.length > 0) {
          slide.addTable(tableData, {
            x: 0.6,
            y: 1.5,
            w: 8.8,
            fontSize: 12,
            fontFace: 'Microsoft YaHei',
            border: { pt: 1, color: 'CCCCCC' },
            fill: { color: 'FFFFFF' },
            color: '333333',
            align: 'left',
            valign: 'middle'
          })
        }
      },

      /**
       * 转换 highlight-box（高亮框）
       */
      convertHighlightBox(pptx, slide, highlightBox) {
        const h3 = highlightBox.querySelector('h3')
        const p = highlightBox.querySelector('p')

        const y = 5.5

        // 背景框
        slide.addShape(pptx.ShapeType.rect, {
          x: 0.6,
          y: y,
          w: 8.8,
          h: 1.3,
          fill: { color: 'F8FBFF' }
        })

        // 标题
        if (h3) {
          slide.addText(h3.textContent.trim(), {
            x: 0.8,
            y: y + 0.15,
            w: 8.4,
            h: 0.4,
            fontSize: 16,
            bold: true,
            color: '1890FF',
            fontFace: 'Microsoft YaHei'
          })
        }

        // 内容
        if (p) {
          slide.addText(p.textContent.trim(), {
            x: 0.8,
            y: y + 0.6,
            w: 8.4,
            h: 0.6,
            fontSize: 13,
            color: '595959',
            fontFace: 'Microsoft YaHei'
          })
        }
      },

      /**
       * 传统 HTML 转 PPT（基于标题结构）
       */
      convertHtmlToPpt(pptx, doc) {
        // 添加标题页
        const titleSlide = pptx.addSlide()
        titleSlide.background = { color: '4472C4' }
        titleSlide.addText(this.currentHtmlTitle || '文档', {
          x: 0.5,
          y: 2.5,
          w: 9,
          h: 1.5,
          fontSize: 44,
          bold: true,
          color: 'FFFFFF',
          align: 'center',
          valign: 'middle'
        })

        // 处理内容
        const elements = doc.body.children
        let currentSlide = null
        let slideContent = []

        for (let i = 0; i < elements.length; i++) {
          const element = elements[i]
          const tagName = element.tagName.toLowerCase()

          if (tagName === 'h1' || tagName === 'h2') {
            // 如果有累积的内容，先创建幻灯片
            if (slideContent.length > 0 && currentSlide) {
              this.addContentToSlide(currentSlide, slideContent)
              slideContent = []
            }

            // 创建新幻灯片
            currentSlide = pptx.addSlide()
            currentSlide.addText(element.textContent, {
              x: 0.5,
              y: 0.5,
              w: 9,
              h: 0.8,
              fontSize: tagName === 'h1' ? 32 : 28,
              bold: true,
              color: '4472C4'
            })
          } else if (tagName === 'table') {
            // 处理表格
            if (!currentSlide) {
              currentSlide = pptx.addSlide()
            }

            const tableData = this.parseTableToArray(element)
            if (tableData.length > 0) {
              currentSlide.addTable(tableData, {
                x: 0.5,
                y: 1.5,
                w: 9,
                fontSize: 12,
                border: { pt: 1, color: 'CFCFCF' },
                fill: { color: 'F7F7F7' }
              })
            }

            // 表格后创建新幻灯片
            if (slideContent.length > 0) {
              this.addContentToSlide(currentSlide, slideContent)
              slideContent = []
            }
            currentSlide = null
          } else if (tagName === 'p' || tagName === 'ul' || tagName === 'ol' || tagName === 'pre') {
            if (!currentSlide) {
              currentSlide = pptx.addSlide()
            }
            slideContent.push({
              type: tagName,
              text: element.textContent.trim()
            })
          }
        }

        // 添加最后的内容
        if (slideContent.length > 0 && currentSlide) {
          this.addContentToSlide(currentSlide, slideContent)
        }
      }
    }
  }
</script>

<style scoped>
  .container {
    /* height: 100vh; */
    overflow: hidden;
    display: flex;
    position: relative;
  }

  .main-content {
    flex: 1;
    display: flex;
    transition: all 0.3s ease;
  }

  .main-content.with-preview {
    /* margin-right 通过内联样式动态设置 */
  }

  .agent-list {
    width: 810px;
    max-width: 100%;
    margin: 0 auto;
    height: 100%;
    padding-bottom: 150px;
    position: relative;
    transition: all 0.3s ease;
  }

  .main-content.with-preview .agent-list {
    margin: 0;
    margin-left: 40px;
    width: calc(100% - 80px);
    max-width: 810px;
  }

  .scorll-content {
    overflow-y: auto;
    height: 100%;
    /* 隐藏 Firefox 浏览器的滚动条 */
    scrollbar-width: none;
    /* 隐藏 IE 和旧版 Edge 浏览器的滚动条 */
    -ms-overflow-style: none;
  }

  /* 对话内容区域 */
  .chat-content {
    margin-top: 40px;
    padding-bottom: 20px;
  }

  .line {
    margin-bottom: 40px;
    padding-right: 10px;
  }

  .chat-item {
    white-space: pre-wrap;
  }

  /* 用户问题样式 */
  .question {
    justify-self: flex-end;
    padding: 9px 16px;
    border-radius: 12px;
    font-size: 16px;
    white-space: normal;
    word-wrap: normal;
    background: rgba(0, 0, 0, 0.04);
    color: rgba(0, 0, 0, 0.85);
    max-width: 450px;
    width: fit-content;
    line-height: 24px;
  }

  /* AI 回答样式 */
  .answer {
    justify-self: flex-start;
    color: rgba(0, 0, 0, 0.85);
    font-size: 16px;
    line-height: 24px;
    cursor: pointer;
  }

  .answer-action {
    min-height: 40px;
  }

  .action-btn .icon {
    font-size: 16px;
    margin: 20px 20px 20px 0;
    cursor: pointer;
  }

  .realtive-msg .msg-item {
    padding: 9px 16px;
    border-radius: 12px;
    font-size: 14px;
    white-space: normal;
    word-wrap: normal;
    background: rgba(0, 0, 0, 0.04);
    color: rgba(0, 0, 0, 0.85);
    max-width: 450px;
    width: fit-content;
    margin-bottom: 12px;
    cursor: pointer;
  }

  .assistant-avatar {
    margin-bottom: 10px;
    display: flex;
    align-items: center;
    font-weight: 500;
  }
  .text-content {
    margin-top: 50px;
  }
  .search-input {
    width: 810px;
    text-align: center;
    position: absolute;
    bottom: 0;
    padding-bottom: 20px;
  }

  .menu-item {
    display: flex;
    align-items: center;
    justify-content: flex-start;
    font-weight: 700;
  }

  .menu-icon {
    width: 24px;
    height: 24px;
    line-height: 24px;
    text-align: center;
  }

  .intro {
    font-size: 24px;
    margin-top: 12px;
    margin-bottom: 24px;
    color: #000;
    text-align: left;
  }

  .input-content {
    display: flex;
    border: 1px solid rgba(0, 0, 0, 0.1);
    box-shadow: 0 6px 10px 0 rgba(42, 60, 79, 0.1);
    border-radius: 20px;
    display: flex;
    gap: 8px;
    justify-content: space-between;
    padding: 12px 14px 12px 2px;
    flex-direction: column;
    background: #fff;
  }

  .input-style >>> textarea {
    border: none;
    outline: none;
    padding: 14px;
    margin: 0;
    background-image: none;
    background-color: transparent;
    width: 100%;
    resize: none;
    font-size: 16px;
  }

  .input-style >>> textarea:focus {
    outline: none;
  }

  .input-style >>> .textarea_ai {
    display: none;
  }

  .select-style {
    /* width: 100px; */
    display: flex;
  }

  .btn-item {
    display: flex;
    align-items: center;
  }

  .btn-item .btn-title {
    margin: 0 4px;
  }

  .btn-item >>> .el-switch__core {
    width: 30px !important;
    height: 16px !important;
  }

  .btn-item >>> .el-switch__core::after {
    width: 12px;
    height: 12px;
    /* margin-left: -13px !important;
    left: 15px !important; */
  }

  .btn-group {
    display: flex;
    justify-content: space-between;
    margin-left: 14px;
    /* margin-top: 0px; */
  }

  .switch-group {
    display: flex;
    align-items: center;
  }

  .icons .icon {
    font-size: 20px;
    font-weight: 700;
    cursor: pointer;
    margin-right: 10px;
  }

  .send {
    width: 32px;
    height: 32px;
    line-height: 32px;
    border-radius: 50%;
    background-color: rgba(0, 0, 0, 0.15);
    color: #fff;
    margin-left: 12px;
  }

  .relative-list {
    margin-top: 30px;
    display: flex;
    flex-wrap: wrap;
  }

  .relative-list .list-item {
    margin-right: 4px;
    margin-bottom: 4px;
    font-size: 14px;
    border-radius: 8px;
    padding: 7px 16px;
    cursor: pointer;
    border: 1px solid rgba(0,0,0,.08);
  }
  
  .relative-list .list-item:hover {
    background: rgba(0,0,0,.06);
  }

  .relative-list .list-item-actived:hover {
    background: #232629;
  }

  .relative-list .list-item-actived {
    background: #232629;
    color: #fff;
  }

  .relative-list .icon {
    font-size: 14px;
    font-weight: 700;
    margin-right: 4px;
  }
  .relative-list .btn-title {
    font-size: 14px;
  }

  .relative-cards {
    margin-top: 20px;
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 10px;
    margin-bottom: 20px;
    /* padding-bottom: 180px; */
  }

  .card-item {
    border: 1px solid rgba(0,0,0,.08);
    width: 190px;
    height: 130px;
    padding-top: 16px;
    padding-left: 16px;
    padding-right: 16px;
    cursor: pointer;
    border-radius: 16px;
  }

  .card-item:hover {
    box-shadow: 0 6px 10px 0 rgba(42, 60, 79, .1);
  }
  .card-item-actived {
    border-color: #0057ff;
  }

  .card-icon {
    width: 24px;
    height: 24px;
  }

  .card-icon img {
    width: 100%;
    height: 100%;
  }
  .card-title {
    font-size: 16px;
    margin-top: 12px;
    margin-bottom: 4px;
    font-weight: 700;
  }
  .card-desc {
    overflow: hidden;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
    align-items: flex-end;
    display: flex;
    height: 36px;
    line-height: 18px;
    margin-bottom: 16px;
    font-size: 13px;
    text-overflow: ellipsis;
    color: rgba(0,0,0,.3);
  }

  .btn-item {
    margin-right: 10px;
    font-size: 14px;
    border-radius: 8px;
    padding: 10px 12px;
    cursor: pointer;
    border: 1px solid rgba(0,0,0,.08);
  }

  .btn-item .btn-icon {
    font-size: 14px;
    font-weight: 700;
  }

  .btn-item:hover {
    background: rgba(0,0,0,.06);
  }

  .deep {
    display: flex;
    align-items: center;
    margin-right: 10px;
    padding: 5px 8px;
    border: 1px solid rgba(0, 0, 0, 0.5);
    border-radius: 14px;
    cursor: pointer;
    font-size: 14px;
  }

  .deep:hover {
    background:#DBEAFE;
    border-color:rgba(0, 122, 255, 0.15);
  }

  .btn-icon {
    width: 18px;
    height: 18px;
    font-size: 18px;
    margin-right: 0;
    margin-left: 4px;
  }

  .right-content {
    flex: 1;
    /* border: 1px solid #000; */
    border-radius: 16px;
    background: #fff;
    box-shadow: rgba(0, 8, 24, 0.12) 1px 3px 28.8px;
    padding: 16px;
    overflow: hidden;
  }
  
  .right-content .top-nav {
    display: flex;
    align-items: center;
    width: 100%;
    height: 50px;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
    margin-bottom: 10px;
  }

  .right-content .title {
    flex: 1;
  }

  .right-content .icon {
    width: 24px;
    height: 24px;
    font-size: 24px;
    cursor: pointer;
    margin-right: 6px;
  }

  .right-content .btn {
    margin-right: 12px;
  }

  .right-content .file-content {
    max-width: 800px;
    margin: 0px auto;
    padding: 24px 40px 50px 40px;
    background-color: #fff;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
    height: 88%;
    overflow: auto;
  }

  .right-content .footer {
    height: 50px;
    border-top: 1px solid rgba(0, 0, 0, 0.1);
    margin-top: 10px;
    font-size: 20px;
    display: flex;
    align-items: center;
  }

  .right-content .page {
    margin: 0 16px;
  }

  /* HTML5 文档卡片样式 */
  .html-doc-card {
    margin-top: 12px;
    padding: 16px;
    border: 1px solid #1890ff;
    border-radius: 8px;
    background: linear-gradient(135deg, #e6f7ff 0%, #bae7ff 100%);
    color: #1890ff;
    cursor: pointer;
    transition: all 0.3s ease;
    max-width: 400px;
  }

  .html-doc-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(24, 144, 255, 0.3);
    border-color: #40a9ff;
  }

  .html-doc-card .card-header {
    display: flex;
    align-items: center;
    font-size: 16px;
    font-weight: 600;
    margin-bottom: 8px;
    color: #1890ff;
  }

  .html-doc-card .card-header i {
    margin-right: 8px;
    font-size: 20px;
  }

  .html-doc-card .card-body {
    display: flex;
    align-items: center;
    font-size: 14px;
    color: #40a9ff;
  }

  .html-doc-card .card-body i {
    margin-right: 6px;
    font-size: 16px;
  }

  /* HTML5 预览面板样式 */
  .html-preview-panel {
    position: fixed;
    right: 0;
    top: 50px;
    /* width 通过内联样式动态设置 */
    height: calc(100vh - 50px);
    background: #fff;
    box-shadow: -2px 0 8px rgba(0, 0, 0, 0.1);
    z-index: 1000;
    display: flex;
    flex-direction: column;
  }

  .preview-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 20px;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
    background: #fafafa;
  }

  .preview-title {
    font-size: 16px;
    font-weight: 600;
    display: flex;
    align-items: center;
    color: #1890ff;
  }

  .preview-title i {
    margin-right: 8px;
    font-size: 18px;
  }

  .preview-title .generating-badge {
    margin-left: 10px;
    font-size: 12px;
    font-weight: normal;
    background: linear-gradient(90deg, #4D6BFE, #7B8CFF);
    color: white;
    padding: 2px 10px;
    border-radius: 10px;
    animation: badge-pulse 1.5s infinite;
  }

  @keyframes badge-pulse {
    0%, 100% { opacity: 1; }
    50% { opacity: 0.7; }
  }

  .preview-actions {
    display: flex;
    align-items: center;
    gap: 12px;
  }

  .preview-close {
    font-size: 20px;
    cursor: pointer;
    color: #666;
    transition: color 0.2s;
  }

  .preview-close:hover {
    color: #333;
  }

  .preview-content {
    flex: 1;
    overflow: hidden;
    position: relative;
  }

  .preview-content iframe {
    width: 100%;
    height: 100%;
    border: none;
  }

  .resize-handle {
    position: absolute;
    left: 0;
    top: 0;
    width: 4px;
    height: 100%;
    background: transparent;
    cursor: ew-resize;
    z-index: 10;
  }

  .resize-handle:hover {
    background: rgba(0, 122, 255, 0.3);
  }

  /* 滑入滑出动画 */
  .slide-enter-active, .slide-leave-active {
    transition: transform 0.3s ease;
  }

  .slide-enter, .slide-leave-to {
    transform: translateX(100%);
  }
</style>
