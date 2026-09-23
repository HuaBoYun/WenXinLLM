<template>
  <div class="container" v-loading="loading">
    <!-- 历史记录侧边栏 -->
    <transition name="slide-left">
      <div v-if="showHistoryPanel" class="history-panel">
        <div class="history-header">
          <span class="history-title">
            <i class="el-icon-time"></i>
            历史记录
          </span>
          <div class="history-actions">
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              @click="createNewChat"
              circle
            ></el-button>
            <el-button
              type="text"
              icon="el-icon-close"
              @click="showHistoryPanel = false"
              class="close-btn"
            ></el-button>
          </div>
        </div>
        <div class="history-list">
          <div
            v-for="(item, index) in historyList"
            :key="item.id"
            class="history-item"
            :class="{ 'active': currentHistoryId === item.id }"
            @click="loadHistory(item)"
          >
            <div class="history-item-header">
              <i class="el-icon-chat-dot-round"></i>
              <span class="history-item-title">{{ item.title }}</span>
            </div>
            <div class="history-item-info">
              <span class="history-time">{{ formatHistoryTime(item.time) }}</span>
            </div>
            <div class="history-item-footer">
              <el-button
                type="text"
                icon="el-icon-delete"
                size="mini"
                @click.stop="deleteHistory(item.id)"
                class="delete-btn"
              ></el-button>
            </div>
          </div>
          <div v-if="historyList.length === 0" class="history-empty">
            <i class="el-icon-folder-opened"></i>
            <p>暂无历史记录</p>
          </div>
        </div>
      </div>
    </transition>

    <!-- 历史记录切换按钮 -->
    <div class="history-toggle" @click="showHistoryPanel = !showHistoryPanel">
      <i :class="showHistoryPanel ? 'el-icon-s-fold' : 'el-icon-s-unfold'"></i>
    </div>

    <div class="main-content" :class="{ 'with-history': showHistoryPanel }">
      <div class="agent-list">
        <div class="scorll-content">
          <div class="text-content">
            <div class="page-title">
              <div class="menu-item">
                <i :class="`menu-icon el-icon-cpu`"></i>
                <div class="menu-title">{{ menuInfo.label || 'AI 编程' }}</div>
              </div>
            </div>
            <div class="intro">问心智能体（老黄牛），AI 编程实时交付</div>
          </div>

          <!-- 连接状态 -->
          <div class="connection-status" :class="{ connected: isConnected, connecting: isConnecting, 'bridge-mode': useBridgeMode }">
            <i :class="isConnected ? 'el-icon-check' : (isConnecting ? 'el-icon-loading' : 'el-icon-close')"></i>
            <span>{{ connectionStatusText }}</span>
            <el-button
              v-if="!isConnected && !isConnecting"
              type="primary"
              size="mini"
              @click="useBridgeMode ? initBridge() : reconnect()"
              style="margin-left: 10px"
            >
              重新连接
            </el-button>
          </div>

          <!-- AI 对话展示区域 -->
          <div class="chat-content" ref="chatContent">
            <div class="line" v-for="(item, index) in dialogue" :key="index">
              <div class="chat-item question" v-if="item.role === 'user'">
                <div class="markdown-body user-markdown" v-html="renderMarkdown(item.content)"></div>
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
                  问心AI
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
                  <div
                    v-if="item.isLoading && !item.textContent"
                    style="
                      padding: 4px 14px;
                      background: #ECF5FF;
                      margin-left: 10px;
                      border-radius: 10px;
                      font-size: 14px;
                      color: #409EFF;
                    "
                  >
                    <i class="icon el-icon-loading"></i>
                    处理中
                  </div>
                </div>
                <div style="margin-left: 40px">
                  <!-- 文本 + 工具指示器内联交错渲染 -->
                  <template v-if="item.textContent || getToolSegments(item).length > 0">
                    <template v-for="(seg, segIdx) in buildInterleaved(item)">
                      <div
                        v-if="seg.type === 'text'"
                        :key="'txt-' + segIdx"
                        class="markdown-body"
                        :class="{ 'is-error': item.isError }"
                        v-html="renderMarkdown(seg.content)"
                      ></div>
                      <div
                        v-else-if="seg.type === 'tool'"
                        :key="'tl-' + segIdx"
                        class="tool-inline-indicator"
                        :class="{ done: seg.done, active: seg.active }"
                      >
                        <span class="tool-status-icon">
                          <i v-if="seg.done" class="el-icon-check"></i>
                          <i v-else-if="seg.active" class="el-icon-loading"></i>
                        </span>
                        <span class="tool-label">{{ seg.text }}</span>
                      </div>
                    </template>
                  </template>
                  <!-- 页面跳转测试卡片 -->
                  <div
                    v-if="item.navigationPages && item.navigationPages.length > 0 && item.isComplete"
                    class="navigation-card"
                  >
                    <div class="nav-card-header">
                      <i class="el-icon-monitor"></i>
                      <span>前往测试页面</span>
                    </div>
                    <div class="nav-card-list">
                      <div
                        v-for="(page, pIdx) in item.navigationPages"
                        :key="pIdx"
                        class="nav-card-item"
                        @click="openPageInNewTab(page)"
                      >
                        <i class="el-icon-link"></i>
                        <span class="nav-page-name">{{ page.name }}</span>
                        <i class="el-icon-top-right nav-open-icon"></i>
                      </div>
                    </div>
                  </div>
                </div>
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
              placeholder="输入你的编程问题"
              @keydown.enter.native="onSend"
              :disabled="!isConnected"
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
                <div
                  @click="showBusinessReviewDialog"
                  class="deep review-btn"
                >
                  业务梳理
                  <i class="btn-icon el-icon-document"></i>
                </div>
                <div
                  @click="showBlueprintDialog"
                  class="deep blueprint-btn"
                >
                  业务蓝图
                  <i class="btn-icon el-icon-files"></i>
                </div>
                <div
                  @click="showTemplateDialog"
                  class="deep template-btn"
                >
                  开发模板
                  <i class="btn-icon el-icon-edit"></i>
                </div>
              </div>

              <div class="icons">
                <i
                  v-if="!stopLoading"
                  class="icon el-icon-video-pause send"
                  @click="stopStreaming"
                ></i>
                <i v-else class="icon el-icon-top send" @click="onSend"></i>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 页面选择对话框 -->
    <el-dialog
      title="选择页面"
      :visible.sync="pageSelectorVisible"
      width="500px"
      :close-on-click-modal="false"
      :modal-append-to-body="true"
      :append-to-body="true"
      custom-class="page-selector-dialog"
    >
      <div class="page-selector-content">
        <el-select
          v-model="selectedModule"
          placeholder="请选择模块"
          style="width: 100%; margin-bottom: 16px; text-align: left;"
          @change="fetchPageList"
          popper-append-to-body
        >
          <el-option
            v-for="item in moduleList"
            :key="item.uniqueIdentification"
            :label="item.projectName"
            :value="item.uniqueIdentification"
          />
        </el-select>

        <el-input
          v-model="pageSearchKey"
          placeholder="搜索页面名称"
          prefix-icon="el-icon-search"
          style="margin-bottom: 12px;"
          clearable
          @input="filterPageList"
        />

        <div class="page-tree" v-loading="pageListLoading">
          <el-tree
            ref="pageTree"
            :data="filteredPageList"
            :props="treeProps"
            :filter-node-method="filterNode"
            node-key="id"
            :expand-on-click-node="false"
            :default-expand-all="true"
            show-checkbox
            @check="handlePageCheck"
          >
            <span slot-scope="{ node, data }" class="custom-tree-node">
              <div class="tree-node-content">
                <vab-icon
                  v-if="data.type !== 2 && data.icon"
                  :icon="data.icon"
                  :is-custom-svg="true"
                  class="tree-icon"
                  style="width: 14px; height: 14px;"
                />
                <span :class="{ 'disabled-node': data.type === 0 }">{{ node.label }}</span>
                <span v-if="data.type === 0" class="type-label">(目录)</span>
              </div>
            </span>
          </el-tree>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <span v-if="selectedPages.length > 0" style="float: left; line-height: 36px; color: #409EFF; font-size: 13px;">
          已选 {{ selectedPages.length }} 个页面
        </span>
        <el-button @click="pageSelectorVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmPageSelection" :disabled="selectedPages.length === 0">确定</el-button>
      </div>
    </el-dialog>

    <!-- 开发模板对话框 -->
    <el-dialog
      title="🚀 全栈开发任务模板"
      :visible.sync="templateDialogVisible"
      width="900px"
      :close-on-click-modal="false"
      :modal-append-to-body="true"
      :append-to-body="true"
      custom-class="template-dialog"
    >
      <div class="template-layout">
        <!-- 左侧：模板列表 -->
        <div class="template-list-panel">
          <div class="template-list-header">
            <span class="template-list-title">模板列表</span>
          </div>
          <div class="template-list-body" v-loading="templateListLoading">
            <div
              v-for="item in templateList"
              :key="item.id"
              class="template-list-item"
              :class="{ 'is-active': activeTemplateId === item.id }"
              @click="selectTemplate(item)"
            >
              <div class="tpl-item-info">
                <span class="tpl-item-title">{{ item.title }}</span>
                <span class="tpl-item-tag" v-if="item.isSystem === 1">系统</span>
              </div>
              <el-button
                v-if="item.isSystem !== 1"
                type="text"
                size="mini"
                icon="el-icon-delete"
                class="tpl-delete-btn"
                @click.stop="handleDeleteTemplate(item)"
              ></el-button>
            </div>
            <div v-if="templateList.length === 0 && !templateListLoading" class="tpl-empty">
              <i class="el-icon-folder-opened"></i>
              <p>暂无模板</p>
            </div>
          </div>
        </div>

        <!-- 右侧：模板内容 -->
        <div class="template-content">
          <div class="template-tip">
            <i class="el-icon-info"></i>
            <span>请根据实际需求修改下方<span class="highlight-text">核心输入</span>部分的内容</span>
          </div>

          <!-- 选择页面按钮 -->
          <div class="template-page-selector-bar">
            <el-button
              type="primary"
              size="small"
              icon="el-icon-s-operation"
              @click="showTemplatePageSelector"
              plain
            >
              选择相关页面
            </el-button>
            <span v-if="templateSelectedPages.length > 0" class="selected-pages-info">
              <i class="el-icon-check"></i>
              已选择 {{ templateSelectedPages.length }} 个页面
            </span>
          </div>

          <!-- 模板内容区域 -->
          <div class="template-sections-wrapper">
            <!-- 上半部分：只读渲染 -->
            <div class="template-section template-readonly">
              <div class="markdown-render" v-html="renderTemplateMarkdown(templateTopContent)"></div>
            </div>

            <!-- 核心输入部分：可编辑 -->
            <div class="template-section template-editable">
              <div class="editable-header">
                <span class="editable-badge">✏️ 可编辑区域</span>
                <span class="editable-title">核心输入 (Input):</span>
              </div>
              <div class="input-wrapper">
                <span class="input-prefix">* 需求信息：</span>
                <el-input
                  v-model="templateInputContent"
                  type="textarea"
                  :rows="3"
                  placeholder="按格式修改， 例如：这个页面请帮我新增一个查询条件： 登记人"
                  class="template-input-textarea"
                />
                <span class="input-suffix">*</span>
              </div>
            </div>

            <!-- 下半部分：只读渲染 -->
            <div class="template-section template-readonly">
              <div class="markdown-render" v-html="renderTemplateMarkdown(templateBottomContent)"></div>
            </div>
          </div>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="templateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="sendTemplate" :disabled="!templateInputContent.trim()">
          <i class="el-icon-s-promotion"></i>
          发送到 AI
        </el-button>
      </div>
    </el-dialog>

    <!-- 模板页面选择对话框 -->
    <el-dialog
      title="选择相关页面"
      :visible.sync="templatePageSelectorVisible"
      width="500px"
      :close-on-click-modal="false"
      :modal-append-to-body="true"
      :append-to-body="true"
      custom-class="page-selector-dialog"
    >
      <div class="page-selector-content">
        <el-select
          v-model="selectedModule"
          placeholder="请选择模块"
          style="width: 100%; margin-bottom: 16px; text-align: left;"
          @change="fetchPageList"
          popper-append-to-body
        >
          <el-option
            v-for="item in moduleList"
            :key="item.uniqueIdentification"
            :label="item.projectName"
            :value="item.uniqueIdentification"
          />
        </el-select>

        <el-input
          v-model="templatePageSearchKey"
          placeholder="搜索页面名称"
          prefix-icon="el-icon-search"
          style="margin-bottom: 12px;"
          clearable
          @input="filterPageList"
        />

        <div class="page-tree" v-loading="pageListLoading">
          <el-tree
            ref="templatePageTree"
            :data="filteredPageList"
            :props="treeProps"
            :filter-node-method="filterNode"
            node-key="id"
            :expand-on-click-node="false"
            :default-expand-all="true"
            show-checkbox
            @check="handleTemplatePageCheck"
          >
            <span slot-scope="{ node, data }" class="custom-tree-node">
              <div class="tree-node-content">
                <vab-icon
                  v-if="data.type !== 2 && data.icon"
                  :icon="data.icon"
                  :is-custom-svg="true"
                  class="tree-icon"
                  style="width: 14px; height: 14px;"
                />
                <span :class="{ 'disabled-node': data.type === 0 }">{{ node.label }}</span>
                <span v-if="data.type === 0" class="type-label">(目录)</span>
              </div>
            </span>
          </el-tree>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <span v-if="templateSelectedPages.length > 0" style="float: left; line-height: 36px; color: #409EFF; font-size: 13px;">
          已选 {{ templateSelectedPages.length }} 个页面
        </span>
        <el-button @click="templatePageSelectorVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmTemplatePageSelection" :disabled="templateSelectedPages.length === 0">确定</el-button>
      </div>
    </el-dialog>

    <!-- 业务蓝图对话框 -->
    <BlueprintDialog
      :visible.sync="blueprintDialogVisible"
      :selectedPages="selectedPages"
      :initialRequirements="blueprintInitialRequirements"
      @confirm="handleBlueprintConfirm"
    />

    <!-- 业务梳理对话框 -->
    <BusinessReviewDialog
      :visible.sync="businessReviewVisible"
      @send-to-blueprint="handleSendToBlueprint"
    />
  </div>
</template>

<script>
  import voice from '@/views/index/components/AI/voice.vue'
  import Loading from '@/components/Loading.vue'
  import BlueprintDialog from '@/views/index/components/AI/BlueprintDialog.vue'
    import BusinessReviewDialog from '@/views/index/components/AI/BusinessReviewDialog.vue'
  import { getOpenClawClient } from '@/api/ai/openclaw'
  import { createAIBridgeClient } from '@/api/ai/aiBridge'
  import { getAuthList } from '@/api/setting/auths'
  import { getModuleList } from '@/api/setting/system'
  import { saveChatHistory, getChatHistoryList, getChatHistoryDetail, deleteChatHistory, updateChatHistory } from '@/api/ai/chatHistory'
  import { getTemplateList, deleteTemplate } from '@/api/ai/businessReview'
  import marked from 'marked'
  import DOMPurify from 'dompurify'

  export default {
    props: {
      menuInfo: {
        type: Object,
        default: () => ({})
      }
    },
    components: { voice, Loading, BlueprintDialog, BusinessReviewDialog },
    data() {
      return {
        loading: false,
        searchKey: '',
        think: false,
        search: false,
        dialogue: [], // 对话列表
        isLoading: false, // 正在回复
        stopLoading: true,

        // WebSocket 连接状态
        openclawClient: null,
        isConnected: false,
        isConnecting: false,

        // 当前正在生成的消息索引
        currentMessageIndex: -1,
        // 当前对话的 runId，用于多轮对话追踪
        currentRunId: null,
        // 事件监听器是否已注册，防止重复注册
        eventHandlersRegistered: false,

        // 页面选择器相关
        pageSelectorVisible: false,
        pageListLoading: false,
        pageSearchKey: '',
        pageList: [],
        filteredPageList: [],
        moduleList: [],
        selectedModule: 'xtsz',
        selectedPages: [],
        treeProps: {
          children: 'children',
          label: 'name'
        },

        // IDE Bridge 模式相关
        useBridgeMode: false, // 是否使用 IDE 同步模式
        bridgeClient: null,   // Bridge 客户端实例

        // 开发模板对话框
        templateDialogVisible: false,
        templateContent: '',
        templateTopContent: '',      // 模板上半部分（只读）
        templateInputContent: '',    // 核心输入部分（可编辑）
        templateBottomContent: '',   // 模板下半部分（只读）
        templatePageSelectorVisible: false,
        templatePageSearchKey: '',
        templateSelectedPages: [],
        // 模板列表
        templateList: [],            // 从后端加载的模板列表
        templateListLoading: false,  // 模板列表加载状态
        activeTemplateId: null,      // 当前选中的模板 ID

        // 业务蓝图对话框
        blueprintDialogVisible: false,
        blueprintInitialRequirements: [],

        // 业务梳理对话框
        businessReviewVisible: false,

        // 历史记录相关
        showHistoryPanel: false, // 是否显示历史记录面板
        historyList: [], // 历史记录列表
        currentHistoryId: null, // 当前选中的历史记录ID
        isSavingHistory: false, // 是否正在保存历史记录
        sessionId: 'session_' + Date.now(), // 会话ID

        // Markdown 渲染防抖定时器
        saveDebounceTimer: null,

        // 标记是否在恢复模式
        isRestoringFromHistory: false,

        // 标记用户是否在等待回复（防止 messageComplete 后的延迟 chatStarted 误触发）
        awaitingResponse: false,

        // 页面跳转卡片：暂存当前轮次用户选择的页面
        pendingNavigationPages: []
      }
    },
    computed: {
      connectionStatusText() {
        if (this.useBridgeMode) {
          if (this.isConnecting) return 'IDE 同步连接中...'
          if (this.isConnected) return 'IDE 同步已连接'
          return 'IDE 同步未连接'
        }
        if (this.isConnecting) return '连接中...'
        if (this.isConnected) return '已连接'
        return '未连接'
      }
    },
    created() {
      // 配置 marked：启用表格、代码高亮、换行等
      marked.setOptions({
        gfm: true,        // GitHub Flavored Markdown（支持表格、删除线等）
        breaks: true,     // 换行符转 <br>
        headerIds: false,
        sanitize: false   // 不用 marked 内置清理，用 DOMPurify
      })

      this.initOpenClaw()
      this.fetchModuleList()
      // 从后端加载历史记录列表
      this.loadHistoryFromBackend()
      // 自动恢复最新的未完成对话（页面刷新后）
      this.restoreLatestSession()
    },
    beforeDestroy() {
      // 清理防抖定时器
      if (this.saveDebounceTimer) {
        clearTimeout(this.saveDebounceTimer)
        this.saveDebounceTimer = null
      }
      // 组件销毁时断开连接
      if (this.openclawClient) {
        this.openclawClient.disconnect()
      }
      if (this.bridgeClient) {
        this.bridgeClient.disconnect()
      }
    },
    methods: {
      /**
       * 初始化 OpenClaw 客户端
       */
      initOpenClaw() {
        console.log('[AIProgramming] 初始化 OpenClaw 客户端')

        // 创建客户端实例
        this.openclawClient = getOpenClawClient({
          gatewayUrl: 'ws://127.0.0.1:8381',
          token: 'qwertyuiopasdfghjkl123',
          sessionKey: 'agent:main:main',
          client: 'webchat-ui',
          clientVersion: '1.0.0',
          mode: 'webchat',
          scopes: ['operator.read', 'operator.write']
        })

        // 注册事件监听
        this.registerEventHandlers()

        // 尝试连接
        this.connectToGateway()
      },

      /**
       * 注册事件处理器
       */
      registerEventHandlers() {
        // 防止重复注册事件监听器
        if (this.eventHandlersRegistered) {
          console.log('[AIProgramming] 事件监听器已注册，跳过')
          return
        }
        this.eventHandlersRegistered = true

        // 连接成功
        this.openclawClient.on('connected', () => {
          console.log('[AIProgramming] 连接成功')
          this.isConnected = true
          this.isConnecting = false

          // 清理可能的残留状态
          this.isLoading = false
          this.stopLoading = true
          this.currentRunId = null
          this.currentMessageIndex = -1

          // 获取历史消息
          this.openclawClient.getHistory(50).catch(err => {
            console.error('[AIProgramming] 获取历史失败:', err)
          })

          // 如果不是从恢复模式，则清空对话
          if (!this.isRestoringFromHistory) {
            this.dialogue = []
          }
          this.isRestoringFromHistory = false
        })

        // 连接断开
        this.openclawClient.on('disconnected', () => {
          console.log('[AIProgramming] 连接断开')
          this.isConnected = false
          this.isConnecting = false

          // 清理所有相关状态，防止状态不一致
          this.isLoading = false
          this.stopLoading = true
          this.currentRunId = null
          this.currentMessageIndex = -1

          // 不重置对话历史记录，因为重连后会重新获取
          // this.dialogue = []
        })

        // 注意：不再监听 'message'（chat/delta）事件
        // agentAssistantEvent 已经是主流式通道，chat/delta 是网关对同一事件的二次广播，
        // 两者同时处理会导致文本重复累加

        // 消息完成
        this.openclawClient.on('messageComplete', (payload) => {
          console.log('[AIProgramming] 消息完成:', payload)

          // 使用当前消息索引
          const idx = this.currentMessageIndex

          if (idx >= 0) {
            const target = this.dialogue[idx]
            if (!target) {
              this.saveHistory()
              return
            }

            // 优先使用 final 消息的完整文本
            let finalText = target.textContent || ''
            if (payload.message) {
              let msgText = ''
              if (Array.isArray(payload.message.content)) {
                const block = payload.message.content.find(b => b.type === 'text')
                msgText = block ? block.text : ''
              } else if (typeof payload.message.content === 'string') {
                msgText = payload.message.content
              } else if (payload.message.content && payload.message.content.text) {
                msgText = payload.message.content.text
              }
              if (msgText && msgText.length >= finalText.length) {
                finalText = msgText
              }
            }

            // 如果没有任何文本内容，说明是中间轮次的空 final，不标记完成，不清除状态
            if (!finalText) {
              console.log('[AIProgramming] 收到空 messageComplete，忽略（中间轮次）')
              return
            }

            // 有实际文本内容，才真正结束
            this.isLoading = false
            this.stopLoading = true
            this.awaitingResponse = false

            // 标记所有工具段落为完成（不再动文本段落）
            const finalSegments = (target.segments || []).map(s => {
              if (s.type === 'tool' && (s.active || !s.done)) {
                return { ...s, done: true, active: false }
              }
              return s
            })

            this.$set(this.dialogue, idx, {
              ...target,
              textContent: finalText,
              content: finalText,
              isComplete: true,
              segments: finalSegments,
              navigationPages: this.pendingNavigationPages.length > 0
                ? [...this.pendingNavigationPages]
                : (target.navigationPages || [])
            })

            // 清理暂存的导航页面
            this.pendingNavigationPages = []
            this.currentRunId = null
          }

          // 保存历史记录
          this.saveHistory()
        })

        // 聊天开始 — 仅在用户主动发送消息后才响应，防止 messageComplete 后延迟的 lifecycle start 误触发
        this.openclawClient.on('chatStarted', (payload) => {
          if (!this.awaitingResponse) {
            console.log('[AIProgramming] 忽略非预期的 chatStarted 事件')
            return
          }
          this.isLoading = true
          this.stopLoading = false
          // 保存当前对话的 runId，确保多轮对话不错乱
          if (payload && payload.runId) {
            this.currentRunId = payload.runId
          }
        })

        // 聊天中止
        this.openclawClient.on('chatAborted', (payload) => {
          console.log('[AIProgramming] 聊天中止:', payload)
          this.isLoading = false
          this.stopLoading = true
          this.awaitingResponse = false
        })

        // 聊天错误（含限流）
        this.openclawClient.on('chatError', (payload) => {
          console.error('[AIProgramming] 聊天错误:', payload)
          this.isLoading = false
          this.stopLoading = true
          this.awaitingResponse = false

          const errMsg = payload.errorMessage || ''
          const isRateLimit = errMsg.includes('rate limit') || errMsg.includes('429') || errMsg.includes('访问量过大') || errMsg.includes('稍后再试')

          const tipText = isRateLimit
            ? '⚠️ 当前请求量过大，模型被限流了，请稍等片刻再试～'
            : ('请求失败：' + (errMsg || '未知错误'))

          if (this.currentMessageIndex >= 0) {
            const target = this.dialogue[this.currentMessageIndex]
            this.$set(this.dialogue, this.currentMessageIndex, {
              ...target,
              textContent: tipText,
              isError: true
            })
          } else {
            this.$message.warning(tipText)
          }
        })

        // 输入状态
        this.openclawClient.on('typing', (isTyping) => {
          console.log('[AIProgramming] 输入状态', isTyping)
        })

        // 错误
        this.openclawClient.on('error', (error) => {
          console.error('[AIProgramming] 错误:', error)
          this.$message.error('连接错误: ' + (error.message || '未知错误'))
        })

        // 历史消息 — 页面刷新后从网关恢复对话
        this.openclawClient.on('history', (dialogue) => {
          console.log('[AIProgramming] 历史消息:', Array.isArray(dialogue) ? dialogue.length : typeof dialogue, '条')
          if (!Array.isArray(dialogue) || dialogue.length === 0) return

          // 只在当前对话为空时才加载（避免覆盖已有对话）
          if (this.dialogue.length === 0) {
            this.dialogue = dialogue
            // 检查最后一条消息是否未完成，若是则恢复索引以继续接收增量
            const lastMsg = this.dialogue[this.dialogue.length - 1]
            if (lastMsg && lastMsg.role === 'assistant' && lastMsg.textContent && !lastMsg.isComplete) {
              this.currentMessageIndex = this.dialogue.length - 1
              console.log('[AI编程] 自动恢复未完成的助手消息')
            }
            this.$nextTick(() => this.scrollToBottom())
          }
        })

        // 工具使用事件 — 实时显示当前正在执行的操作
        this.openclawClient.on('agentToolEvent', (payload) => {
          this.handleToolEvent(payload)
        })

        // Agent 流式文本事件 — 实时渲染 AI 回复（主要的流式通道）
        this.openclawClient.on('agentAssistantEvent', (payload) => {
          if (!payload || !payload.data) return

          const text = payload.data.text || ''
          if (!text) return

          // 构造兼容 handleAssistantMessage 的 message 格式
          const message = { content: text }
          this.handleAssistantMessage(message, payload.runId)
        })
      },

      /**
       * 连接到 Gateway
       */
      async connectToGateway() {
        this.isConnecting = true
        try {
          console.log('[AIProgramming] 开始连接到 Gateway...')
          await this.openclawClient.connect()
          console.log('[AIProgramming] Gateway 连接成功')
        } catch (error) {
          console.error('[AIProgramming] 连接失败:', error)
          this.$message.error('连接失败: ' + error.message)
          this.isConnecting = false
        }
      },

      /**
       * 重新连接
       */
      reconnect() {
        console.log('[AIProgramming] 手动重连')
        // 断开现有连接
        if (this.openclawClient) {
          this.openclawClient.disconnect()
        }

        // 重置事件监听器注册标志
        this.eventHandlersRegistered = false
        // 清除事件监听器
        if (this.openclawClient) {
          this.openclawClient.eventHandlers = {}
        }

        // 重新创建客户端并连接
        this.openclawClient = new (require('@/api/ai/openclaw').OpenClawClient)({
          gatewayUrl: 'ws://127.0.0.1:8381',
          token: 'qwertyuiopasdfghjkl123',
          sessionKey: 'agent:main:main',
          client: 'webchat-ui',
          clientVersion: '1.0.0',
          mode: 'webchat',
          scopes: ['operator.read', 'operator.write']
        })
        this.registerEventHandlers()
        this.connectToGateway()
      },

      /**
       * 发送消息（Enter 发送，Shift+Enter 换行）
       */
      onSend(e) {
        // 如果是键盘事件，处理 Shift+Enter 换行
        if (e && e.type === 'keydown') {
          if (e.shiftKey) return // Shift+Enter：允许默认换行
          e.preventDefault()     // Enter：阻止换行
        }
        if (!this.searchKey.trim()) return this.$message.error('请输入内容！')
        if (!this.isConnected) return this.$message.warning('请等待连接...')
        if (this.isLoading) {
          console.warn('[AIProgramming] 正在处理中，忽略发送请求')
          return
        }

        const userMessage = this.searchKey.trim()

        // Bridge 模式：发送到 Bridge Server
        if (this.useBridgeMode) {
          this.dialogue.push({ role: 'user', content: userMessage })
          const thisMessageIndex = this.dialogue.length
          this.currentMessageIndex = thisMessageIndex
          this.dialogue.push({
            role: 'assistant',
            content: '',
            textContent: '等待 IDE 回复中...',
            think: false,
            segments: []
          })
          this.isLoading = true
          this.stopLoading = false
          this.$nextTick(() => this.scrollToBottom())

          try {
            this.bridgeClient.sendMessage(userMessage)
          } catch (error) {
            console.error('[AIProgramming] Bridge 发送失败', error)
            this.$message.error('发送失败: ' + error.message)
            this.isLoading = false
            this.stopLoading = true
          }
          this.searchKey = ''
          return
        }

        // OpenClaw 模式
        try {
          // 保存当前选中页面快照，用于回复完成后渲染跳转卡片
          const pages = Array.isArray(this.selectedPages) ? this.selectedPages : []
          this.pendingNavigationPages = pages
            .filter(p => p && p.type === 1)
            .map(p => ({ name: p.name, fullPath: p.fullPath || p.path || '', component: p.component || '' }))

          // 添加用户消息
          this.dialogue.push({
            role: 'user',
            content: userMessage
          })

          // 添加 AI 回复占位
          const thisMessageIndex = this.dialogue.length
          this.currentMessageIndex = thisMessageIndex
          this.currentRunId = null

          this.dialogue.push({
            role: 'assistant',
            content: '',
            textContent: '',
            think: this.think,
            navigationPages: [],
            segments: [] // 交错段落：[{ type: 'tool', ... }, { type: 'text', content: '...' }]
          })

          this.isLoading = true
          this.stopLoading = false
          this.awaitingResponse = true
          this.$nextTick(() => this.scrollToBottom())

          // 发送消息到 Gateway
          this.openclawClient.sendMessage(userMessage)
        } catch (error) {
          console.error('[AIProgramming] 发送流程出错:', error)
          this.$message.error('发送失败: ' + (error.message || error))
          this.isLoading = false
          this.stopLoading = true
          this.currentRunId = null
        }

        this.searchKey = ''
      },

      /**
       * 获取对话项中的工具步骤段落
       */
      getToolSegments(item) {
        if (!item || !item.segments) return []
        return item.segments.filter(s => s.type === 'tool')
      },

      /**
       * 构建交错的文本 + 工具指示器序列
       * 根据工具段落的 textLenBefore 切分 textContent，实现工具指示器内联嵌入
       */
      buildInterleaved(item) {
        const tools = this.getToolSegments(item)
        const text = item.textContent || ''

        // 没有工具：整个文本作为一段
        if (tools.length === 0) return [{ type: 'text', content: text }]

        // 按 textLenBefore 排序（保证插入顺序）
        const sorted = [...tools].sort((a, b) => (a.textLenBefore || 0) - (b.textLenBefore || 0))

        const result = []
        let lastPos = 0

        for (let i = 0; i < sorted.length; i++) {
          const tool = sorted[i]
          const pos = tool.textLenBefore || 0

          // 切分：上一工具结束位置到当前工具开始位置的文本
          if (pos > lastPos) {
            const chunk = text.substring(lastPos, pos)
            if (chunk.trim()) {
              result.push({ type: 'text', content: chunk })
            }
          }

          result.push({ type: 'tool', ...tool })
          lastPos = Math.max(lastPos, pos)
        }

        // 剩余文本（最后一个工具之后的内容）
        if (lastPos < text.length) {
          const remaining = text.substring(lastPos)
          if (remaining.trim()) {
            result.push({ type: 'text', content: remaining })
          }
        }

        // 如果结果为空，回退到整个文本
        return result.length > 0 ? result : [{ type: 'text', content: text }]
      },

      /**
       * 渲染 Markdown 文本为安全的 HTML
       */
      renderMarkdown(text) {
        if (!text) return ''
        try {
          const raw = marked(text)
          return DOMPurify.sanitize(raw, {
            ADD_TAGS: ['table', 'thead', 'tbody', 'tr', 'th', 'td', 'pre', 'code', 'blockquote', 'hr', 'br', 'img'],
            ADD_ATTR: ['class', 'colspan', 'rowspan', 'align', 'valign', 'scope']
          })
        } catch (e) {
          // 如果渲染失败，回退到纯文本
          return text
        }
      },

      /**
       * 在新标签页中打开指定页面，用于测试修改后的功能
       */
      openPageInNewTab(page) {
        if (!page || !page.fullPath) {
          this.$message.warning('无法获取页面路径')
          return
        }
        // 构建完整 URL：hash 路由模式
        const base = window.location.origin + window.location.pathname
        const url = base + '#' + (page.fullPath.startsWith('/') ? page.fullPath : '/' + page.fullPath)
        window.open(url, '_blank')
      },

      /**
       * 从文本中提取工具步骤
       */
      extractToolSteps(text) {
        const steps = []
        if (!text) return steps

        const patterns = [
          { regex: /(?:让我|我来|我先)(?:来)?(?:查看|读取|打开|分析一下?)\s*([^\n，,。！？。!?]+)/g, icon: '📖', action: '读取' },
          { regex: /(?:修改|更新|编辑|写入|替换|添加|新增|创建)\s*([^\n，,。！？。!?]+)/g, icon: '✏️', action: '修改' },
          { regex: /(?:查找|搜索|检索|定位|查询)\s*([^\n，,。！？。!?]+)/g, icon: '🔍', action: '查找' },
          { regex: /(?:检查|检查|确认|验证|验证)\s*([^\n，,。！？。!?]+)/g, icon: '🔎', action: '检查' },
          { regex: /(?:运行|执行|调用|编译)\s*([^\n，,。！？。!?]+)/g, icon: '⚡', action: '运行' },
        ]

        const lines = text.split('\n')
        const seen = new Set()

        for (const line of lines) {
          for (const pattern of patterns) {
            const matches = [...line.matchAll(pattern.regex)]
            for (const match of matches) {
              const detail = match[1].trim()
              if (!detail || detail.length > 100 || seen.has(detail)) continue
              seen.add(detail)
              steps.push({
                icon: pattern.icon,
                action: pattern.action,
                text: `${pattern.icon} ${pattern.action} ${detail}`,
                done: false,
                active: false
              })
            }
          }
        }

        // 标记：最后一个为 active
        if (steps.length > 0) {
          steps.forEach((s, i) => {
            if (i < steps.length - 1) s.done = true
            else s.active = true
          })
        }

        return steps
      },

      /**
       * 自动恢复最新的未完成会话（页面刷新后）
       */
      async restoreLatestSession() {
        // 等待历史记录列表加载完成
        await this.$nextTick()

        // 如果已经有对话内容，说明网关历史已经恢复了，不需要额外处理
        if (this.dialogue.length > 0) return

        // 尝试从最新的历史记录中恢复（列表不含dialogue，直接加载最新一条）
        if (this.historyList.length > 0) {
          const latest = this.historyList[0]
          await this.loadHistory(latest)
          // 加载后检查最后一条消息是否未完成
          const lastMsg = this.dialogue[this.dialogue.length - 1]
          if (lastMsg && lastMsg.role === 'assistant' && lastMsg.textContent && !lastMsg.isComplete) {
            console.log('[AI编程] 检测到未完成的会话，自动恢复:', latest.title)
            this.$message.info('已自动恢复上次未完成的对话')
          }
        }
      },

      /**
       * 处理工具使用事件
       */
      handleToolEvent(payload) {
        console.log('[AIProgramming] 工具事件原始数据:', JSON.stringify(payload).substring(0, 800))
        const data = payload.data || payload
        const phase = data.phase || ''
        const toolCallId = data.toolCallId || ''

        // 从更多可能的字段中提取工具名
        let toolName = data.name || data.toolName || data.tool || data.type || ''
        // OpenClaw 的工具参数在 data.args 中
        const toolArgs = data.args || data.input || data.parameters || {}

        // start 阶段：显示新步骤；result 阶段：标记完成
        if (phase === 'result') {
          // 标记对应 toolCallId 的步骤为完成
          if (toolCallId) {
            const idx = this.currentMessageIndex
            if (idx >= 0 && this.dialogue[idx]) {
              const target_item = this.dialogue[idx]
              const segments = (target_item.segments || []).map(s => {
                if (s.type === 'tool' && s.toolCallId === toolCallId) {
                  return { ...s, done: true, active: false }
                }
                return s
              })
              this.$set(this.dialogue, idx, { ...target_item, segments })
            }
          }
          return
        }
        if (phase && phase !== 'start') return

        // 从 tool args 中提取有意义的目标信息
        let target = ''
        if (toolArgs.file_path) target = toolArgs.file_path
        else if (toolArgs.path) target = toolArgs.path
        else if (toolArgs.file) target = toolArgs.file
        else if (toolArgs.command) target = this.truncateText(toolArgs.command, 80)
        else if (toolArgs.pattern) target = toolArgs.pattern
        else if (toolArgs.query) target = this.truncateText(toolArgs.query, 60)
        else if (toolArgs.url) target = toolArgs.url

        if (!toolName && !target) return

        // 缩短文件路径，只保留最后2-3层目录
        if (target && target.includes('/')) {
          const parts = target.split('/')
          target = parts.length > 3 ? '.../' + parts.slice(-3).join('/') : target
        } else if (target && target.includes('\\')) {
          const parts = target.split('\\')
          target = parts.length > 3 ? '...\\' + parts.slice(-3).join('\\') : target
        }

        // 如果工具名是 exec，尝试从命令或路径推断实际操作类型
        let effectiveName = toolName
        if (toolName === 'exec' || toolName === 'execute') {
          const cmd = (toolArgs.command || '').toLowerCase()
          const tgt = target.toLowerCase()
          if (cmd.includes('cat ') || cmd.includes('type ') || cmd.includes('get-content') ||
              tgt.match(/\.(vue|js|ts|jsx|tsx|json|css|scss|html|md|py|java|go|xml|sql)$/)) {
            effectiveName = 'Read'
          } else if (cmd.includes('echo ') || cmd.includes('>') || cmd.includes('set-content') ||
                     cmd.includes('out-file') || cmd.includes('write') || cmd.includes('edit')) {
            effectiveName = 'Edit'
          } else if (cmd.includes('ls') || cmd.includes('dir') || cmd.includes('get-childitem') || cmd.includes('find .')) {
            effectiveName = 'Glob'
          } else if (cmd.includes('grep') || cmd.includes('select-string') || cmd.includes('findstr')) {
            effectiveName = 'Grep'
          } else {
            effectiveName = 'Bash'
          }
        }

        // 工具名 → 中文显示名映射
        const chineseNameMap = {
          'Read': '读取', 'read': '读取', 'ReadFile': '读取', 'read_file': '读取', 'View': '读取', 'view': '读取',
          'Edit': '修改', 'edit': '修改', 'EditFile': '修改', 'Write': '新增', 'WriteFile': '新增',
          'write': '新增', 'write_file': '新增', 'save': '新增', 'Save': '新增',
          'MultiEdit': '批量修改', 'multiedit': '批量修改',
          'Bash': '执行', 'bash': '执行', 'execute': '执行', 'exec': '执行',
          'Glob': '查询文件', 'GlobTool': '查询文件', 'glob': '查询文件',
          'Grep': '搜索', 'GrepTool': '搜索', 'grep': '搜索',
          'TodoRead': '读取任务', 'TodoWrite': '更新任务',
          'WebFetch': '抓取网页', 'WebSearch': '搜索网页',
        }

        // 构建步骤文本
        const iconMap = {
          'Read': '📖', 'read': '📖', 'ReadFile': '📖', 'read_file': '📖', 'View': '📖', 'view': '📖',
          'Edit': '✏️', 'edit': '✏️', 'EditFile': '✏️', 'Write': '✏️', 'WriteFile': '✏️',
          'write': '✏️', 'write_file': '✏️', 'save': '✏️', 'Save': '✏️',
          'MultiEdit': '✏️', 'multiedit': '✏️',
          'Bash': '⚡', 'bash': '⚡', 'execute': '⚡', 'exec': '⚡',
          'Glob': '🔍', 'GlobTool': '🔍', 'glob': '🔍',
          'Grep': '🔍', 'GrepTool': '🔍', 'grep': '🔍',
          'TodoRead': '📋', 'TodoWrite': '📋',
          'WebFetch': '🌐', 'WebSearch': '🔎',
        }
        const icon = iconMap[effectiveName] || iconMap[toolName] || '🔧'
        const displayName = chineseNameMap[effectiveName] || chineseNameMap[toolName] || effectiveName
        const displayText = `${icon} ${displayName}${target ? ': ' + target : ''}`

        const idx = this.currentMessageIndex
        if (idx < 0 || !this.dialogue[idx]) return

        const target_item = this.dialogue[idx]

        // 更新 segments：标记上一个工具段落为完成，追加新的工具段落
        const segments = (target_item.segments || []).map(s => {
          if (s.type === 'tool' && s.active) return { ...s, done: true, active: false }
          return s
        })
        segments.push({
          type: 'tool',
          text: displayText,
          icon: icon,
          toolName: effectiveName,
          toolCallId: toolCallId,
          target: target,
          textLenBefore: (target_item.textContent || '').length, // 记录插入点
          done: false,
          active: true
        })

        this.$set(this.dialogue, idx, {
          ...target_item,
          segments: segments
        })
        this.scrollToBottom()
        this.debounceSaveHistory()
      },

      /**
       * 截断文本，超过 maxLen 的部分用 ... 代替
       */
      truncateText(text, maxLen) {
        if (!text || text.length <= maxLen) return text || ''
        return text.substring(0, maxLen) + '...'
      },

      /**
       * 处理助手消息（来自 agentAssistantEvent，text 始终是完整累积文本）
       */
      handleAssistantMessage(message, runId) {
        if (!message) return

        let fullText = ''

        if (Array.isArray(message.content)) {
          const textBlock = message.content.find(item => item.type === 'text')
          fullText = textBlock ? textBlock.text : ''
        } else if (typeof message.content === 'string') {
          fullText = message.content
        } else if (message.content && message.content.text) {
          fullText = message.content.text
        }

        if (!fullText) return

        const idx = this.currentMessageIndex
        if (idx < 0) return

        const target = this.dialogue[idx]
        if (!target) return

        // 完整累积文本，直接替换；只保留更长的版本防止乱序
        const prev = target.textContent || ''
        if (fullText.length < prev.length) return

        // 只更新 textContent，不动 segments（工具步骤由 handleToolEvent 管理）
        this.$set(this.dialogue, idx, {
          ...target,
          textContent: fullText
        })
        this.scrollToBottom()
        this.debounceSaveHistory()
      },

      /**
       * 停止流式输出
       */
      stopStreaming() {
        if (this.openclawClient && this.isLoading) {
          try {
            this.openclawClient.abortChat()
          } catch (error) {
            console.error('[AIProgramming] 中止失败:', error)
          }
        }
        this.isLoading = false
        this.stopLoading = true
      },

      /**
       * 滚动到底部
       */
      scrollToBottom() {
        this.$nextTick(() => {
          const container = this.$refs.chatContent
          if (container) {
            container.scrollTop = container.scrollHeight
          }
        })
      },

      /**
       * 获取语音消息
       */
      getMessage(rectxt) {
        this.searchKey = rectxt
      },

      /**
       * 获取模块列表
       */
      async fetchModuleList() {
        try {
          const res = await getModuleList({})
          this.moduleList = res.data || []
        } catch (error) {
          console.error('[AIProgramming] 获取模块列表失败:', error)
        }
      },



      /**
       * 获取页面列表
       */
      async fetchPageList() {
        if (!this.selectedModule) return

        this.pageListLoading = true
        try {
          const { data } = await getAuthList({
            moduletype: this.selectedModule,
            judge: 1
          })
          this.pageList = this.buildPageTree(data.rightList || [])
          this.filterPageList()
        } catch (error) {
          console.error('[AIProgramming] 获取页面列表失败:', error)
          this.$message.error('获取页面列表失败')
        } finally {
          this.pageListLoading = false
        }
      },

      /**
       * 构建页面树：移除操作节点（type=2），并计算每个节点的完整路由路径 fullPath
       */
      buildPageTree(nodes, parentPath = '') {
        if (!nodes) return []

        return nodes
          .filter(node => node.type !== 2)
          .map(node => {
            // 计算完整路由路径
            let fullPath = ''
            if (node.path) {
              fullPath = node.path.startsWith('/')
                ? node.path
                : (parentPath ? parentPath + '/' + node.path : '/' + node.path)
            } else {
              fullPath = parentPath
            }
            return {
              ...node,
              fullPath: fullPath,
              children: this.buildPageTree(node.children, fullPath)
            }
          })
      },

      /**
       * 过滤页面列表
       */
      filterPageList() {
        if (!this.pageSearchKey) {
          this.filteredPageList = this.pageList
        } else {
          this.filteredPageList = this.filterTree(this.pageList, this.pageSearchKey)
        }
      },

      /**
       * 树形过滤方法
       */
      filterTree(tree, keyword) {
        if (!tree) return []

        const filteredTree = []
        for (const node of tree) {
          // 检查当前节点是否匹配
          const match = node.name.toLowerCase().includes(keyword.toLowerCase())

          // 递归过滤子节点
          const filteredChildren = this.filterTree(node.children, keyword)

          // 如果当前节点匹配或有匹配的子节点，则保留
          if (match || filteredChildren.length > 0) {
            filteredTree.push({
              ...node,
              children: filteredChildren
            })
          }
        }
        return filteredTree
      },

      /**
       * 树节点过滤方法（el-tree使用）
       */
      filterNode(value, data) {
        if (!value) return true
        return data.name.toLowerCase().includes(value.toLowerCase())
      },

      /**
       * 处理页面 checkbox 勾选
       */
      handlePageCheck(data, { checkedNodes }) {
        // 只保留 type === 1 的页面节点，过滤掉目录节点
        this.selectedPages = checkedNodes.filter(node => node.type === 1)
      },

      // ==================== 业务蓝图相关方法 ====================

      /**
       * 显示业务蓝图对话框
       */
      showBlueprintDialog() {
        this.blueprintDialogVisible = true
      },

      /**
       * 接收业务梳理组件发来的需求数据，关闭梳理、打开蓝图
       */
      handleSendToBlueprint(requirements) {
        this.blueprintInitialRequirements = requirements || []
        this.businessReviewVisible = false
        this.$nextTick(() => {
          this.blueprintDialogVisible = true
        })
      },

      /**
       * 显示业务梳理对话框
       */
      showBusinessReviewDialog() {
        this.businessReviewVisible = true
      },

      /**
       * 处理业务蓝图确认（接收组装好的内容并发送到AI）
       */
      handleBlueprintConfirm(content) {
        if (!content) return
        if (!this.isConnected) {
          return this.$message.warning('请等待连接...')
        }
        this.searchKey = content
        this.$nextTick(() => {
          this.onSend()
        })
      },

      // ==================== 开发模板相关方法 ====================

      /**
       * 显示开发模板对话框
       */
      showTemplateDialog() {
        // 重置模板页面选择
        this.templateSelectedPages = []
        // 初始化模板各部分内容（默认模板）
        this.initTemplateContent()
        this.activeTemplateId = null
        // 加载模板列表
        this.loadTemplateList()
        this.templateDialogVisible = true
      },

      /**
       * 从后端加载模板列表
       */
      async loadTemplateList() {
        this.templateListLoading = true
        try {
          const res = await getTemplateList()
          if (res && (res.code === 200 || res.code === 1 || res.code === 0)) {
            this.templateList = res.data || []
          } else {
            this.templateList = []
          }
        } catch (e) {
          console.error('[AIProgramming] 加载模板列表失败:', e)
          this.templateList = []
        } finally {
          this.templateListLoading = false
        }
      },

      /**
       * 选择模板（点击左侧列表项）
       */
      selectTemplate(item) {
        this.activeTemplateId = item.id
        // 用模板标题替换角色背景部分，保持约束和工作流程不变
        this.templateTopContent = `**角色 (Role):**
你是一位资深的示例云项目全栈开发工程师，精通 Spring Boot、Vue2、Element-UI 及达梦数据库技术栈。

**背景 (Background):**
我需要在示例云项目中修改部分功能模块。你将负责从需求分析到前后端开发的全部实现工作。

**当前模板：${item.title}**`
        this.templateInputContent = ''
      },

      /**
       * 删除模板
       */
      async handleDeleteTemplate(item) {
        try {
          await this.$confirm(`确定要删除模板「${item.title}」吗？`, '删除确认', {
            confirmButtonText: '确定删除',
            cancelButtonText: '取消',
            type: 'warning'
          })
        } catch {
          return // 用户取消
        }
        try {
          const res = await deleteTemplate(item.id)
          if (res && (res.code === 200 || res.code === 1 || res.code === 0)) {
            this.$message.success('模板已删除')
            // 如果删除的是当前选中的模板，重置内容
            if (this.activeTemplateId === item.id) {
              this.activeTemplateId = null
              this.initTemplateContent()
            }
            // 刷新列表
            this.loadTemplateList()
          } else {
            this.$message.error('删除失败：' + (res && res.message ? res.message : '未知错误'))
          }
        } catch (e) {
          console.error('[AIProgramming] 删除模板失败:', e)
          this.$message.error('删除失败，请稍后重试')
        }
      },

      /**
       * 工具下拉菜单命令处理
       */
      handleToolCommand(command) {
        if (command === 'businessReview') {
          this.showBusinessReviewDialog()
        } else if (command === 'blueprint') {
          this.showBlueprintDialog()
        } else if (command === 'template') {
          this.showTemplateDialog()
        }
      },

      /**
       * 初始化模板内容（分段）
       */
      initTemplateContent() {
        // 上半部分：角色和背景
        this.templateTopContent = `**角色 (Role):**
你是一位资深的示例云项目全栈开发工程师，精通 Spring Boot、Vue2、Element-UI 及达梦数据库技术栈。

**背景 (Background):**
我需要在示例云项目中修改部分功能模块。你将负责从需求分析到前后端开发的全部实现工作。`

        // 核心输入部分（可编辑）- 初始为空，使用 placeholder 显示示例
        this.templateInputContent = ''

        // 下半部分：开发约束和工作流程
        this.templateBottomContent = `**开发约束 (Constraints):**

1. **强制规范**：开始开发前，你**必须**首先调用示例云开发助手mcp，以全面理解并遵循项目的技术架构与开发规范
2. **技术栈**：严格遵守 Spring Boot + Vue2 + 达梦数据库的技术架构。
3. **代码质量**：代码必须包含完整的注释、异常处理和必要的权限控制逻辑。
4. **交付物**：你需要提供可直接用于项目的功能代码，包括但不限于数据库设计、后端API和前端页面组件。
5. **文件地址**： 你要修改的文件，前端地址是： D:\\aicode\\hbyun-admin\\hby-admin，后端地址是： D:\\aicode\\springbootHbfkKnowledgeSharing

**工作流程 (Workflow):**
请严格按照以下步骤执行：

1. **准备阶段**: 调用上述所有 \`tool_get_hbyun_...\` 工具函数，并确认你已理解所有规范和文档。
2. **分析与规划**:
    * 深入分析 \`<需求文档>\` 的内容。
    * 结合获取到的项目规范，制定一份详细的开发计划，包括：
        * 数据库表结构设计（DDL语句）。
        * 后端API接口设计（URL、请求方法、参数、返回值）。
        * 前端组件拆分计划。
3. **后端开发**:
    * 依次完成 \`Entity\`, \`Mapper\`, \`Service\`, \`Controller\` 层的代码编写。
4. **前端开发**:
    * 依次完成 \`api定义\`, \`路由配置\`, \`页面组件\` 的代码编写。
5. **重启服务**:
    * 如果涉及到后端服务的修改，就需要重启后端服务才能进行测试，重启命令可以参考合同服务的启动命令：
    \`\`\`powershell
    cd D:\\aicode\\springbootHbfkKnowledgeSharing\\springboot-hbyunContract
    $env:JAVA_HOME = "D:\\develop\\software\\jdk"
    $env:MAVEN_HOME = "D:\\develop\\software\\apache-maven-3.6.3"
    $env:PATH = "$env:JAVA_HOME\\bin;$env:MAVEN_HOME\\bin;$env:PATH"
    mvn spring-boot:run -DskipTests
    \`\`\`
    * 注意必须服务启动完成后，才能进行下一步验证
6. **测试验证**:
    * 需要确保前端接口与后端接口对接完全成功
    * 提供关键业务逻辑的单元测试方案或代码。

请从**第一步（准备阶段）**开始执行。并持续完成全部。如果有需要我确认的信息，请直接向我询问`
      },

      /**
       * 获取完整模板内容（用于发送）
       */
      getFullTemplateContent() {
        // 组装页面信息（如果有选择的话）
        let pageInfoBlock = ''
        if (this.templateSelectedPages.length > 0) {
          const pagesInfo = this.templateSelectedPages.map((page, index) => {
            return `  - 页面${index + 1}：${page.name}
    - 页面路径：${page.component || page.path}
    - 权限标识：${page.perms || '无'}`
          }).join('\n')
          pageInfoBlock = `\n**相关页面信息 (共${this.templateSelectedPages.length}个):**
${pagesInfo}\n`
        }

        // 组装完整内容，前缀和后缀固定
        return `${this.templateTopContent}
${pageInfoBlock}
**核心输入 (Input):**

* 需求信息：${this.templateInputContent}*

${this.templateBottomContent}`
      },

      /**
       * 渲染模板 Markdown 内容
       */
      renderTemplateMarkdown(content) {
        if (!content) return ''
        try {
          const rawHtml = marked(content)
          return DOMPurify.sanitize(rawHtml)
        } catch (e) {
          console.error('Markdown 渲染失败:', e)
          return content
        }
      },

      /**
       * 显示模板页面选择器
       */
      showTemplatePageSelector() {
        this.templatePageSelectorVisible = true
        this.templatePageSearchKey = ''
        if (!this.selectedModule && this.moduleList.length > 0) {
          this.selectedModule = this.moduleList[0].uniqueIdentification
        }
        this.fetchPageList()
      },

      /**
       * 处理模板页面选择
       */
      handleTemplatePageCheck(data, { checkedNodes }) {
        this.templateSelectedPages = checkedNodes.filter(node => node.type === 1)
      },

      /**
       * 确认模板页面选择
       */
      confirmTemplatePageSelection() {
        if (this.templateSelectedPages.length === 0) {
          return this.$message.warning('请至少选择一个页面')
        }

        this.templatePageSelectorVisible = false
        const names = this.templateSelectedPages.map(p => p.name).join('、')
        this.$message.success(`已添加 ${this.templateSelectedPages.length} 个页面: ${names}`)
      },

      /**
       * 发送模板内容到 AI
       */
      sendTemplate() {
        if (!this.templateInputContent.trim()) {
          return this.$message.warning('请填写核心输入内容')
        }
        if (!this.isConnected) {
          return this.$message.warning('请等待连接...')
        }

        // 将模板选择的页面同步到主页面选择器，确保 onSend 能正确生成导航卡片
        if (this.templateSelectedPages.length > 0) {
          this.selectedPages = [...this.templateSelectedPages]
        }

        // 组装完整模板内容并发送
        this.searchKey = this.getFullTemplateContent()
        this.templateDialogVisible = false

        // 触发发送
        this.$nextTick(() => {
          this.onSend()
        })
      },

      /**
       * 防抖保存历史记录，用于流式输出期间自动保存
       */
      debounceSaveHistory() {
        if (this.saveDebounceTimer) {
          clearTimeout(this.saveDebounceTimer)
        }
        this.saveDebounceTimer = setTimeout(() => {
          if (this.dialogue.length > 0 && this.currentHistoryId) {
            this.saveHistory()
          }
        }, 3000) // 每 3 秒最多保存一次
      },

      // ==================== 历史记录相关方法 ====================

      /**
       * 保存历史记录到后端
       */
      async saveHistory() {
        if (this.isSavingHistory || this.dialogue.length === 0) return

        this.isSavingHistory = true

        try {
          // 获取用户信息
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          const userId = userInfo.staffid

          if (!userId) {
            console.warn('无法获取用户ID，跳过保存历史记录')
            return
          }

          console.log('[AI编程] 成功获取用户ID:', userId)

          // 获取用户的第一条消息作为标题
          const firstUserMessage = this.dialogue.find(item => item.role === 'user')
          const title = firstUserMessage ?
            (firstUserMessage.content.length > 30 ?
              firstUserMessage.content.substring(0, 30) + '...' :
              firstUserMessage.content) :
            '新对话'

          const historyData = {
            userId: userId,
            sessionId: this.sessionId,
            title: title,
            dialogue: JSON.stringify(this.dialogue),
            hasDocument: false,
            type: 'programming' // 添加类型标识
          }

          // 如果是更新现有历史记录
          if (this.currentHistoryId) {
            historyData.id = this.currentHistoryId
            const res = await updateChatHistory(historyData)
            if (res.code === 200) {
              console.log('[AI编程] 历史记录已更新', title)
              await this.loadHistoryFromBackend()
            }
          } else {
            // 新增历史记录
            const res = await saveChatHistory(historyData)
            if (res.code === 200 && res.data) {
              this.currentHistoryId = res.data.id
              console.log('[AI编程] 历史记录已保存', title)
              await this.loadHistoryFromBackend()
            }
          }
        } catch (error) {
          console.error('[AI编程] 保存历史记录失败:', error)
        } finally {
          this.isSavingHistory = false
        }
      },

      /**
       * 从后端加载历史记录列表（只加载元数据，不含dialogue）
       */
      async loadHistoryFromBackend() {
        try {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          const userId = userInfo.staffid

          if (!userId) {
            console.warn('[AI编程] 无法获取用户ID')
            this.historyList = []
            return
          }

          const res = await getChatHistoryList(userId, 'programming')

          if (res.code === 200 && res.data) {
            // 兼容分页格式（res.data.list）和旧的数组格式（res.data 直接是数组）
            const list = Array.isArray(res.data) ? res.data : (res.data.list || [])
            this.historyList = list.map(item => ({
              id: item.id,
              title: item.title,
              time: item.updateTime || item.createTime,
              hasDocument: item.hasDocument,
              sessionId: item.sessionId
            }))
            console.log('[AI编程] 从后端加载历史记录', this.historyList.length, '条')
          }
        } catch (error) {
          console.error('[AI编程] 加载历史记录失败:', error)
          this.historyList = []
        }
      },

      /**
       * 加载历史记录（点击时请求详情接口获取完整dialogue）
       */
      async loadHistory(item) {
        try {
          this.currentHistoryId = item.id

          // 从后端获取完整的对话详情
          const res = await getChatHistoryDetail(item.id)
          if (res.code === 200 && res.data) {
            this.dialogue = JSON.parse(res.data.dialogue || '[]')
            this.sessionId = res.data.sessionId || 'session_' + Date.now()
          } else {
            this.dialogue = []
            this.sessionId = 'session_' + Date.now()
            this.$message.error('加载历史记录失败')
            return
          }

          // 检查最后一条消息是否是未完成的（有 textContent 但不是最终状态）
          const lastMsg = this.dialogue[this.dialogue.length - 1]
          if (lastMsg && lastMsg.role === 'assistant' && lastMsg.textContent && !lastMsg.isComplete) {
            // 恢复未完成消息的索引，以便继续接收后续增量
            this.currentMessageIndex = this.dialogue.length - 1
            console.log('[AI编程] 恢复未完成的助手消息，索引:', this.currentMessageIndex)
          }

          // 滚动到底部
          this.$nextTick(() => {
            this.scrollToBottom()
          })

          console.log('[AI编程] 已加载历史记录', item.title)
          this.$message.success('已加载历史记录')
        } catch (error) {
          console.error('[AI编程] 加载历史记录失败:', error)
          this.$message.error('加载历史记录失败')
        }
      },

      /**
       * 创建新对话
       */
      createNewChat() {
        this.currentHistoryId = null
        this.dialogue = []
        this.sessionId = 'session_' + Date.now()
        console.log('[AI编程] 创建新对话')
        this.$message.success('已创建新对话')
      },

      /**
       * 删除历史记录（从后端删除）
       */
      async deleteHistory(id) {
        this.$confirm('确定要删除这条历史记录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          try {
            const res = await deleteChatHistory(id)

            if (res.code === 200) {
              // 从本地列表中移除
              const index = this.historyList.findIndex(item => item.id === id)
              if (index !== -1) {
                this.historyList.splice(index, 1)
              }

              // 如果删除的是当前查看的历史记录，清空对话
              if (this.currentHistoryId === id) {
                this.createNewChat()
              }

              this.$message.success('删除成功')
            }
          } catch (error) {
            console.error('[AI编程] 删除历史记录失败:', error)
            this.$message.error('删除失败')
          }
        }).catch(() => {
          // 取消删除
        })
      },

      /**
       * 格式化历史记录时间
       */
      formatHistoryTime(timeStr) {
        if (!timeStr) return ''

        try {
          // 如果是时间戳数字
          if (typeof timeStr === 'number') {
            const date = new Date(timeStr)
            const month = String(date.getMonth() + 1).padStart(2, '0')
            const day = String(date.getDate()).padStart(2, '0')
            const hours = String(date.getHours()).padStart(2, '0')
            const minutes = String(date.getMinutes()).padStart(2, '0')
            return `${month}-${day} ${hours}:${minutes}`
          }

          // 提取日期和时间部分
          const parts = String(timeStr).split(' ')
          if (parts.length >= 2) {
            const datePart = parts[0] // 2026-04-01
            const timePart = parts[1].substring(0, 5) // 09:10

            // 简化显示：只显示月-日 时:分
            const dateParts = datePart.split('-')
            if (dateParts.length === 3) {
              return `${dateParts[1]}-${dateParts[2]} ${timePart}`
            }
          }
          return timeStr
        } catch (error) {
          return timeStr
        }
      },

      // ==================== IDE Bridge 模式方法 ====================

      /**
       * 切换 IDE 同步模式
       */
      toggleBridgeMode() {
        this.useBridgeMode = !this.useBridgeMode
        if (this.useBridgeMode) {
          // 断开 OpenClaw
          if (this.openclawClient) {
            this.openclawClient.disconnect()
          }
          this.isConnected = false
          this.isConnecting = false
          // 连接 Bridge
          this.initBridge()
        } else {
          // 断开 Bridge
          if (this.bridgeClient) {
            this.bridgeClient.disconnect()
            this.bridgeClient = null
          }
          this.isConnected = false
          this.isConnecting = false
          // 重连 OpenClaw
          this.eventHandlersRegistered = false
          this.initOpenClaw()
        }
      },

      /**
       * 初始化 Bridge 客户端
       */
      initBridge() {
        console.log('[AIProgramming] 初始化 IDE Bridge 客户端')
        this.isConnecting = true

        this.bridgeClient = createAIBridgeClient({
          url: 'ws://127.0.0.1:8382'
        })

        // 注册事件
        this.bridgeClient.on('connected', () => {
          console.log('[AIProgramming] Bridge 连接成功')
          this.isConnected = true
          this.isConnecting = false
          this.$message.success('IDE 同步已连接')
        })

        this.bridgeClient.on('disconnected', () => {
          console.log('[AIProgramming] Bridge 断开')
          this.isConnected = false
          this.isConnecting = false
          this.isLoading = false
          this.stopLoading = true
        })

        this.bridgeClient.on('history', (dialogue) => {
          if (!Array.isArray(dialogue) || dialogue.length === 0) return
          console.log('[AIProgramming] Bridge 历史消息:', dialogue.length, '条')
          this.dialogue = dialogue
          this.$nextTick(() => this.scrollToBottom())
        })

        this.bridgeClient.on('messageComplete', (message) => {
          console.log('[AIProgramming] Bridge 收到完整回复')
          this.isLoading = false
          this.stopLoading = true

          const idx = this.currentMessageIndex
          if (idx >= 0 && this.dialogue[idx]) {
            this.$set(this.dialogue, idx, {
              ...this.dialogue[idx],
              content: message.content,
              textContent: message.content
            })
          } else {
            // 没有占位消息，直接追加
            this.dialogue.push({
              role: 'assistant',
              content: message.content,
              textContent: message.content
            })
          }
          this.$nextTick(() => this.scrollToBottom())
          this.saveHistory()
        })

        this.bridgeClient.on('message', (message) => {
          // 流式部分回复
          const idx = this.currentMessageIndex
          if (idx >= 0 && this.dialogue[idx]) {
            this.$set(this.dialogue, idx, {
              ...this.dialogue[idx],
              textContent: message.content || message.textContent || ''
            })
            this.scrollToBottom()
          }
        })

        this.bridgeClient.on('error', (error) => {
          console.error('[AIProgramming] Bridge 错误:', error)
          this.isConnecting = false
          this.$message.error('IDE 同步连接失败，请确认 Bridge Server 已启动')
        })

        // 连接
        this.bridgeClient.connect().catch(err => {
          console.error('[AIProgramming] Bridge 连接失败:', err)
          this.isConnecting = false
        })
      }
    }
  }
</script>

<style scoped>
  /* 复用 AIWrite.vue 的样式 */
  .container {
    overflow: hidden;
    display: flex;
    position: relative;
  }

  /* 历史记录面板样式 */
  .history-panel {
    width: 280px;
    background: #fff;
    border-right: 1px solid #e8e8e8;
    display: flex;
    flex-direction: column;
    position: absolute;
    left: 0;
    top: 0;
    bottom: 0;
    z-index: 100;
    box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
  }

  .history-header {
    padding: 16px;
    border-bottom: 1px solid #e8e8e8;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .history-title {
    font-size: 16px;
    font-weight: 600;
    color: #333;
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .history-actions {
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .close-btn {
    padding: 0;
    color: #999;
  }

  .close-btn:hover {
    color: #333;
  }

  .history-list {
    flex: 1;
    overflow-y: auto;
    padding: 8px;
  }

  .history-item {
    padding: 12px;
    border-radius: 8px;
    cursor: pointer;
    margin-bottom: 8px;
    background: #f9f9f9;
    transition: all 0.2s;
  }

  .history-item:hover {
    background: #f0f0f0;
  }

  .history-item.active {
    background: #e6f7ff;
    border: 1px solid #91d5ff;
  }

  .history-item-header {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 8px;
  }

  .history-item-header i {
    color: #1890ff;
  }

  .history-item-title {
    font-size: 14px;
    font-weight: 500;
    color: #333;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    flex: 1;
  }

  .history-item-info {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 4px;
  }

  .history-time {
    font-size: 12px;
    color: #999;
  }

  .history-item-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .history-item-count {
    font-size: 12px;
    color: #999;
  }

  .delete-btn {
    padding: 0;
    color: #999;
  }

  .delete-btn:hover {
    color: #ff4d4f;
  }

  .history-empty {
    text-align: center;
    padding: 40px 20px;
    color: #999;
  }

  .history-empty i {
    font-size: 48px;
    margin-bottom: 16px;
    display: block;
  }

  .history-empty p {
    margin: 0;
  }

  /* 历史记录切换按钮 */
  .history-toggle {
    position: absolute;
    left: 0;
    top: 50%;
    transform: translateY(-50%);
    width: 24px;
    height: 60px;
    background: #fff;
    border: 1px solid #e8e8e8;
    border-left: none;
    border-radius: 0 8px 8px 0;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    z-index: 99;
    box-shadow: 2px 0 8px rgba(0, 0, 0, 0.05);
  }

  .history-toggle:hover {
    background: #f5f5f5;
  }

  .history-toggle i {
    font-size: 16px;
    color: #666;
  }

  /* 侧边栏动画 */
  .slide-left-enter-active,
  .slide-left-leave-active {
    transition: all 0.3s ease;
  }

  .slide-left-enter,
  .slide-left-leave-to {
    transform: translateX(-100%);
  }

  .main-content {
    flex: 1;
    display: flex;
    transition: all 0.3s ease;
  }

  .main-content.with-history {
    margin-left: 280px;
  }

  .agent-list {
    width: 810px;
    max-width: 100%;
    margin: 0 auto;
    height: 100%;
    padding-bottom: 150px;
  }

  .scorll-content {
    overflow-y: auto;
    height: 100%;
    scrollbar-width: none;
    -ms-overflow-style: none;
  }

  /* 连接状态样式 */
  .connection-status {
    display: flex;
    align-items: center;
    padding: 10px 16px;
    margin: 20px 0;
    border-radius: 8px;
    background: #f5f5f5;
    color: #666;
    font-size: 14px;
  }

  .connection-status.connected {
    background: #e6f7ff;
    color: #1890ff;
  }

  .connection-status.bridge-mode.connected {
    background: #f0f9ff;
    color: #67C23A;
    border: 1px solid #E1F3D8;
  }

  .connection-status.connecting {
    background: #fff7e6;
    color: #fa8c16;
  }

  .connection-status i {
    margin-right: 8px;
    font-size: 16px;
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
    word-wrap: break-word;
    overflow-wrap: break-word;
    background: rgba(0, 0, 0, 0.04);
    color: rgba(0, 0, 0, 0.85);
    max-width: 80%;
    width: fit-content;
    line-height: 24px;
  }

  /* 用户消息中的 Markdown 样式微调 */
  .question .user-markdown {
    font-size: 15px;
  }
  .question .user-markdown >>> p:last-child {
    margin-bottom: 0;
  }
  .question .user-markdown >>> pre {
    margin: 8px 0;
  }

  /* AI 回答样式 */
  .answer {
    justify-self: flex-start;
    color: rgba(0, 0, 0, 0.85);
    font-size: 16px;
    line-height: 24px;
  }

  .answer-action {
    min-height: 40px;
  }

  .action-btn .icon {
    font-size: 16px;
    margin: 20px 20px 20px 0;
    cursor: pointer;
  }

  /* 页面跳转测试卡片 */
  .navigation-card {
    margin-top: 12px;
    border: 1px solid #e4e7ed;
    border-radius: 8px;
    background: #f8faff;
    overflow: hidden;
  }

  .nav-card-header {
    padding: 10px 14px;
    font-size: 13px;
    font-weight: 600;
    color: #409EFF;
    background: #ecf5ff;
    border-bottom: 1px solid #d9ecff;
    display: flex;
    align-items: center;
    gap: 6px;
  }

  .nav-card-list {
    padding: 6px 0;
  }

  .nav-card-item {
    display: flex;
    align-items: center;
    padding: 8px 14px;
    cursor: pointer;
    transition: background 0.2s;
    gap: 8px;
    font-size: 13px;
    color: #303133;
  }

  .nav-card-item:hover {
    background: #ecf5ff;
    color: #409EFF;
  }

  .nav-card-item .el-icon-link {
    color: #909399;
    font-size: 14px;
  }

  .nav-card-item:hover .el-icon-link {
    color: #409EFF;
  }

  .nav-page-name {
    flex: 1;
  }

  .nav-open-icon {
    font-size: 12px;
    color: #c0c4cc;
    opacity: 0;
    transition: opacity 0.2s;
  }

  .nav-card-item:hover .nav-open-icon {
    opacity: 1;
    color: #409EFF;
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
    text-align: left !important;
  }

  .input-style >>> textarea:focus {
    outline: none;
  }

  .input-style >>> .textarea_ai {
    display: none;
  }

  .btn-group {
    display: flex;
    justify-content: space-between;
    margin-left: 14px;
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
    background: #DBEAFE;
    border-color: rgba(0, 122, 255, 0.15);
  }

  .btn-icon {
    width: 18px;
    height: 18px;
    font-size: 18px;
    margin-right: 0;
    margin-left: 4px;
  }

  .page-selector {
    color: #666;
  }

  .page-selector:hover {
    background: #DBEAFE;
    border-color: rgba(0, 122, 255, 0.15);
  }

  /* 页面选择对话框样式 */
  .page-selector-content {
    max-height: 500px;
    overflow-y: auto;
  }

  .page-tree {
    max-height: 400px;
    overflow-y: auto;
    border: 1px solid #EBEEF5;
    border-radius: 4px;
    padding: 8px;
  }

  .custom-tree-node {
    flex: 1;
    display: inline-flex;
    align-items: center;
    padding-right: 8px;
    overflow: hidden;
    width: auto;
  }

  .tree-node-content {
    display: inline-flex;
    align-items: center;
    gap: 6px;
    width: auto;
  }

  .tree-icon {
    width: 16px;
    height: 16px;
    flex-shrink: 0;
    display: inline-block;
  }

  .disabled-node {
    color: #999;
    font-weight: normal;
  }

  .type-label {
    color: #999;
    font-size: 12px;
    margin-left: 4px;
    white-space: nowrap;
  }

  .dialog-footer {
    text-align: right;
  }

  /* 自定义对话框样式，确保正常显示 */
  .page-selector-dialog {
    text-align: left !important;
    margin-top: 5vh !important;
  }

  .page-selector-dialog .el-dialog__body {
    text-align: left !important;
  }

  .page-selector-dialog .el-select {
    text-align: left !important;
  }

  .page-selector-dialog >>> .el-input__inner {
    text-align: left !important;
  }

  .page-selector-dialog >>> .el-select-dropdown {
    text-align: left !important;
  }

  .page-selector-dialog >>> .el-select-dropdown__item {
    text-align: left !important;
  }

  .page-selector-dialog >>> .el-tree-node__label {
    text-align: left !important;
  }

  .page-selector-dialog >>> .el-tree-node__content {
    align-items: center !important;
  }

  .page-selector-dialog >>> .el-tree-node__expand-icon {
    margin-right: 8px !important;
  }

  .page-selector-dialog >>> .el-tree-node {
    text-align: left !important;
  }

  .page-selector-dialog >>> .el-tree-node__content {
    display: flex !important;
    align-items: center !important;
    justify-content: flex-start !important;
  }

  /* ==================== Markdown 渲染样式（>>> 穿透 scoped，作用于 v-html 内容） ==================== */
  .markdown-body {
    font-size: 15px;
    line-height: 1.75;
    color: #333;
    word-wrap: break-word;
    overflow-wrap: break-word;
  }

  .markdown-body.is-error {
    color: #e6a23c;
  }

  .markdown-body >>> h1,
  .markdown-body >>> h2,
  .markdown-body >>> h3,
  .markdown-body >>> h4,
  .markdown-body >>> h5,
  .markdown-body >>> h6 {
    margin-top: 20px;
    margin-bottom: 10px;
    font-weight: 600;
    line-height: 1.35;
    color: #1a1a1a;
  }

  .markdown-body >>> h1 { font-size: 1.5em; border-bottom: 2px solid #409EFF; padding-bottom: 8px; }
  .markdown-body >>> h2 { font-size: 1.3em; border-bottom: 1px solid #e8e8e8; padding-bottom: 6px; }
  .markdown-body >>> h3 { font-size: 1.15em; color: #303133; }
  .markdown-body >>> h4 { font-size: 1.05em; color: #606266; }

  .markdown-body >>> p {
    margin-bottom: 12px;
    line-height: 1.8;
  }

  .markdown-body >>> ul,
  .markdown-body >>> ol {
    margin-bottom: 12px;
    padding-left: 24px;
  }

  .markdown-body >>> li {
    margin-bottom: 4px;
    line-height: 1.7;
  }

  .markdown-body >>> a {
    color: #1890ff;
    text-decoration: none;
  }

  .markdown-body >>> a:hover {
    text-decoration: underline;
  }

  /* 行内代码 */
  .markdown-body >>> code {
    padding: 2px 6px;
    margin: 0 2px;
    border-radius: 4px;
    background: #f0f2f5;
    font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
    font-size: 0.88em;
    color: #c7254e;
    border: 1px solid #e8e8e8;
  }

  /* 代码块 — 深色背景 */
  .markdown-body >>> pre {
    margin: 14px 0;
    padding: 16px;
    border-radius: 8px;
    background: #1e1e2e;
    overflow-x: auto;
    line-height: 1.6;
    border: 1px solid #2d2d3f;
  }

  .markdown-body >>> pre code {
    padding: 0;
    margin: 0;
    border-radius: 0;
    background: transparent;
    color: #cdd6f4;
    font-size: 13px;
    border: none;
  }

  /* 引用块 */
  .markdown-body >>> blockquote {
    margin: 12px 0;
    padding: 10px 16px;
    border-left: 4px solid #409EFF;
    background: #f0f5ff;
    color: #555;
    border-radius: 0 6px 6px 0;
  }

  .markdown-body >>> blockquote p:last-child {
    margin-bottom: 0;
  }

  /* 表格 */
  .markdown-body >>> table {
    margin: 14px 0;
    border-collapse: collapse;
    width: 100%;
    overflow: auto;
    display: table;
    border: 1px solid #d0d7de;
    border-radius: 6px;
    font-size: 14px;
  }

  .markdown-body >>> th,
  .markdown-body >>> td {
    padding: 8px 14px;
    border: 1px solid #d0d7de;
    text-align: left;
  }

  .markdown-body >>> th {
    background: #f0f3f6;
    font-weight: 600;
    color: #1a1a1a;
  }

  .markdown-body >>> tr:nth-child(even) {
    background: #f8f9fb;
  }

  .markdown-body >>> tr:hover {
    background: #eef2f7;
  }

  .markdown-body >>> hr {
    margin: 16px 0;
    border: none;
    border-top: 1px solid #eee;
  }

  .markdown-body >>> img {
    max-width: 100%;
    border-radius: 4px;
  }

  .markdown-body >>> strong {
    font-weight: 600;
    color: #1a1a1a;
  }

  .markdown-body >>> em {
    color: #606266;
  }

  /* ==================== 工具指示器（内联嵌入） ==================== */
  .tool-inline-indicator {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 6px 14px;
    margin: 6px 0;
    border-radius: 6px;
    background: #f0f4f8;
    font-size: 13px;
    color: #666;
    transition: all 0.3s ease;
  }

  .tool-inline-indicator.done {
    background: #f0f9eb;
    color: #67C23A;
  }

  .tool-inline-indicator.active {
    background: #ecf5ff;
    color: #409EFF;
  }

  .tool-inline-indicator .tool-status-icon {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 20px;
    height: 20px;
    flex-shrink: 0;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.6);
    font-size: 12px;
  }

  .tool-inline-indicator.active .tool-status-icon .el-icon-loading {
    animation: rotating 2s linear infinite;
  }

  .tool-inline-indicator .tool-label {
    font-family: 'Consolas', 'Monaco', 'Courier New', monospace;
    font-size: 12px;
    word-break: break-all;
  }

  @keyframes rotating {
    from { transform: rotate(0deg); }
    to { transform: rotate(360deg); }
  }

  /* ==================== 业务蓝图按钮样式 ==================== */
  .blueprint-btn {
    color: #1a73e8;
    border-color: rgba(26, 115, 232, 0.3);
  }

  /* ==================== 业务梳理按钮样式 ==================== */
  .review-btn {
    color: #10b981;
    border-color: rgba(16, 185, 129, 0.3);
  }
  .review-btn:hover {
    background: rgba(16, 185, 129, 0.08);
  }

  .blueprint-btn:hover {
    color: #1a73e8;
    background: #E8F0FE;
    border-color: rgba(26, 115, 232, 0.5);
  }

  /* ==================== 开发模板对话框样式 ==================== */
  .template-btn {
    color: #E6A23C;
    border-color: rgba(230, 162, 60, 0.3);
  }

  .template-btn:hover {
    color: #E6A23C;
    background: #FDF6EC;
    border-color: rgba(230, 162, 60, 0.5);
  }

  .template-dialog {
    text-align: left !important;
  }

  .template-dialog .el-dialog__header {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    padding: 15px 20px;
    border-radius: 4px 4px 0 0;
  }

  .template-dialog .el-dialog__title {
    color: #fff;
    font-size: 16px;
    font-weight: 600;
  }

  .template-dialog .el-dialog__headerbtn .el-dialog__close {
    color: #fff;
  }

  .template-content {
    padding: 0;
  }

  .template-tip {
    display: flex;
    align-items: flex-start;
    padding: 12px 16px;
    background: #FDF6EC;
    border: 1px solid #FAECD8;
    border-radius: 4px;
    margin-bottom: 16px;
    font-size: 14px;
    color: #E6A23C;
  }

  .template-tip i {
    margin-right: 8px;
    font-size: 16px;
    margin-top: 2px;
  }

  .template-page-selector-bar {
    display: flex;
    align-items: center;
    margin-bottom: 16px;
    padding: 12px 16px;
    background: #F5F7FA;
    border-radius: 4px;
  }

  .template-page-selector-bar .el-button {
    margin-right: 12px;
  }

  .selected-pages-info {
    color: #67C23A;
    font-size: 13px;
    display: flex;
    align-items: center;
  }

  .selected-pages-info i {
    margin-right: 4px;
  }

  /* 模板分段区域样式 */
  .template-sections-wrapper {
    max-height: 500px;
    overflow-y: auto;
    border: 1px solid #EBEEF5;
    border-radius: 4px;
    background: #FAFAFA;
  }

  .template-section {
    padding: 16px;
  }

  .template-readonly {
    background: #F5F7FA;
    border-bottom: 1px solid #EBEEF5;
  }

  .template-readonly:last-child {
    border-bottom: none;
  }

  .template-editable {
    background: linear-gradient(135deg, #FFF8E1 0%, #FFECB3 100%);
    border: 2px dashed #FF9800;
    margin: 0;
  }

  .editable-header {
    display: flex;
    align-items: center;
    margin-bottom: 12px;
  }

  .editable-badge {
    display: inline-block;
    padding: 4px 10px;
    background: linear-gradient(135deg, #FF9800 0%, #F57C00 100%);
    color: #fff;
    font-size: 12px;
    font-weight: 600;
    border-radius: 4px;
    margin-right: 12px;
  }

  .editable-title {
    font-size: 16px;
    font-weight: 700;
    color: #E65100;
  }

  .input-wrapper {
    display: flex;
    align-items: flex-start;
    background: #fff;
    border: 1px solid #FFB74D;
    border-radius: 4px;
    padding: 8px 12px;
  }

  .input-prefix {
    color: #E65100;
    font-weight: 600;
    font-size: 14px;
    white-space: nowrap;
    padding-top: 4px;
    user-select: none;
  }

  .input-suffix {
    color: #E65100;
    font-weight: 600;
    font-size: 14px;
    white-space: nowrap;
    padding-top: 4px;
    user-select: none;
  }

  .template-input-textarea {
    flex: 1;
    margin: 0 4px;
  }

  .template-input-textarea >>> .el-textarea__inner {
    font-size: 14px;
    line-height: 1.8;
    padding: 4px 8px;
    border-radius: 4px;
    background: #FFFDE7;
    border: none;
    resize: none;
  }

  .template-input-textarea >>> .el-textarea__inner:focus {
    background: #FFF8E1;
    box-shadow: none;
  }

  .template-input-textarea >>> .el-textarea__inner::placeholder {
    color: #BDBDBD;
    font-style: italic;
  }

  /* Markdown 渲染样式 */
  .markdown-render {
    font-size: 14px;
    line-height: 1.7;
    color: #333;
  }

  .markdown-render >>> h1,
  .markdown-render >>> h2,
  .markdown-render >>> h3,
  .markdown-render >>> h4 {
    margin-top: 0;
    margin-bottom: 12px;
    font-weight: 600;
    color: #1a1a1a;
  }

  .markdown-render >>> strong {
    color: #409EFF;
  }

  .markdown-render >>> p {
    margin: 0 0 10px 0;
  }

  .markdown-render >>> ul,
  .markdown-render >>> ol {
    margin: 0 0 10px 0;
    padding-left: 20px;
  }

  .markdown-render >>> li {
    margin-bottom: 4px;
  }

  .markdown-render >>> code {
    background: #f0f0f0;
    padding: 2px 6px;
    border-radius: 3px;
    font-family: 'Consolas', 'Monaco', monospace;
    font-size: 13px;
  }

  .markdown-render >>> pre {
    background: #2d2d2d;
    color: #f8f8f2;
    padding: 12px;
    border-radius: 4px;
    overflow-x: auto;
    margin: 10px 0;
  }

  .markdown-render >>> pre code {
    background: transparent;
    padding: 0;
    color: inherit;
  }

  .highlight-text {
    color: #FF5722;
    font-weight: 600;
    font-size: 14px;
  }

  .template-dialog .dialog-footer {
    padding: 15px 20px;
    border-top: 1px solid #EBEEF5;
  }

  .template-dialog .dialog-footer .el-button--primary {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
  }

  .template-dialog .dialog-footer .el-button--primary:hover {
    background: linear-gradient(135deg, #5a6fd6 0%, #6a4190 100%);
  }

  /* ==================== 工具下拉菜单按钮 ==================== */
  .tools-btn {
    position: relative;
    color: #606266;
    border-color: rgba(96, 98, 102, 0.3);
  }

  .tools-btn:hover {
    color: #409EFF;
    background: #ECF5FF;
    border-color: rgba(64, 158, 255, 0.3);
  }

  .tools-btn--active {
    color: #409EFF;
    background: #ECF5FF;
    border-color: rgba(64, 158, 255, 0.3);
  }

  .tools-badge {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    min-width: 16px;
    height: 16px;
    padding: 0 4px;
    background: #409EFF;
    color: #fff;
    font-size: 11px;
    border-radius: 8px;
    margin: 0 4px;
    line-height: 1;
  }

  /* ==================== 模板对话框左右布局 ==================== */
  .template-layout {
    display: flex;
    gap: 0;
    min-height: 420px;
  }

  /* 左侧模板列表面板 */
  .template-list-panel {
    width: 200px;
    flex-shrink: 0;
    border-right: 1px solid #EBEEF5;
    display: flex;
    flex-direction: column;
    background: #FAFAFA;
    border-radius: 4px 0 0 4px;
  }

  .template-list-header {
    padding: 12px 14px;
    border-bottom: 1px solid #EBEEF5;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .template-list-title {
    font-size: 13px;
    font-weight: 600;
    color: #303133;
  }

  .template-list-body {
    flex: 1;
    overflow-y: auto;
    padding: 6px 0;
  }

  .template-list-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 8px 12px;
    cursor: pointer;
    transition: background 0.15s;
    border-radius: 4px;
    margin: 2px 6px;
  }

  .template-list-item:hover {
    background: #ECF5FF;
  }

  .template-list-item.is-active {
    background: #E6F0FF;
    border-left: 3px solid #409EFF;
    padding-left: 9px;
  }

  .tpl-item-info {
    display: flex;
    align-items: center;
    gap: 6px;
    flex: 1;
    min-width: 0;
  }

  .tpl-item-title {
    font-size: 13px;
    color: #303133;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .template-list-item.is-active .tpl-item-title {
    color: #409EFF;
    font-weight: 500;
  }

  .tpl-item-tag {
    flex-shrink: 0;
    font-size: 11px;
    padding: 1px 5px;
    background: #F0F2F5;
    color: #909399;
    border-radius: 3px;
    border: 1px solid #DCDFE6;
  }

  .tpl-delete-btn {
    flex-shrink: 0;
    padding: 2px 4px;
    color: #C0C4CC;
    opacity: 0;
    transition: opacity 0.15s, color 0.15s;
  }

  .template-list-item:hover .tpl-delete-btn {
    opacity: 1;
  }

  .tpl-delete-btn:hover {
    color: #F56C6C !important;
  }

  .tpl-empty {
    text-align: center;
    padding: 30px 10px;
    color: #C0C4CC;
    font-size: 13px;
  }

  .tpl-empty i {
    font-size: 28px;
    display: block;
    margin-bottom: 8px;
  }

  /* 右侧内容区适配 */
  .template-layout .template-content {
    flex: 1;
    min-width: 0;
    padding: 0 0 0 16px;
  }
</style>
