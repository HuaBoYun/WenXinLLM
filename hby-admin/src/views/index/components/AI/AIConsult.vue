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
        <div class="history-list" @scroll="handleHistoryScroll">
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
              <span v-if="item.hasDocument" class="history-doc-badge">
                <i class="el-icon-document"></i>
                文档
              </span>
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
          <div v-if="historyLoading" class="history-loading">
            <i class="el-icon-loading"></i>
            <span>加载中...</span>
          </div>
          <div v-if="!historyHasMore && historyList.length > 0" class="history-no-more">
            <span>没有更多了</span>
          </div>
          <div v-if="historyList.length === 0 && !historyLoading" class="history-empty">
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

    <div class="main-content" :class="{ 'with-preview': showHtmlPreview || showDocPanel, 'with-history': showHistoryPanel }" :style="showHtmlPreview ? { marginRight: previewPanelWidth + 'px' } : (showDocPanel ? { marginRight: docPanelWidth + 'px' } : {})">
      <div class="agent-list">
        <div class="scorll-content">
          <div class="text-content">
            <div class="page-title">
              <div class="menu-item">
                <i :class="`menu-icon el-icon-edit`"></i>
                <div class="menu-title">{{ menuInfo.label || 'AI 咨询' }}</div>
              </div>
            </div>
            <div class="intro">AI咨询实时解决方案</div>
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
                    v-if="item.thinkContent"
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
                  v-if="item.thinkContent"
                  style="
                    border-left: 2px solid rgba(0, 0, 0, 0.1);
                    padding-left: 8px;
                    margin-bottom: 30px;
                    color: #8b8b8b;
                    font-size: 14px;
                    margin-left: 40px;
                    white-space: pre-wrap;
                  "
                >
                  {{ item.thinkContent }}
                </div>
                <div style="margin-left: 40px">
                  <!-- 文档模式：对话区显示文档卡片，正文在右侧面板渲染 -->
                  <div v-if="item.isDoc" class="html-doc-card" @click="openDocPanel(item.docContent, item.docTitle, index)">
                    <div class="card-header">
                      <i class="el-icon-document"></i>
                      <span>{{ item.docTitle || '文档' }}</span>
                    </div>
                    <div class="card-body">
                      <i class="el-icon-view"></i>
                      <span>点击查看文档</span>
                    </div>
                  </div>

                  <!-- 渲染普通文本内容（Markdown，非文档模式） -->
                  <div v-else-if="item.textContent" class="markdown-body" v-html="renderMarkdown(item.textContent)"></div>

                  <!-- 渲染HTML5文档卡片 -->
                  <div v-if="item.htmlContent" class="html-doc-card" @click="openHtmlPreview(item.htmlContent, item.htmlTitle, index)">
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
            <!-- 已上传文件展示 -->
            <div v-if="fileObject && fileObject.name" class="uploaded-file">
              <i class="el-icon-document"></i>
              <span class="uploaded-file-name">{{ fileObject.name }}</span>
              <i v-if="fileParsing" class="el-icon-loading"></i>
              <i v-else class="el-icon-close uploaded-file-del" @click="removeFile"></i>
            </div>
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
                <div @click="openPromptDialog" class="deep">
                  设置提示词
                  <i class="btn-icon el-icon-setting"></i>
                </div>
              </div>

              <div class="icons">
                <i
                  class="icon el-icon-paperclip"
                  :class="{ 'is-disabled': fileParsing }"
                  title="上传文件（md/txt/doc/docx/xls/xlsx/pdf）"
                  @click="triggerFileUpload"
                ></i>
                <input
                  v-show="false"
                  ref="fileInput"
                  type="file"
                  accept=".md,.txt,.doc,.docx,.xls,.xlsx,.pdf"
                  @change="onFileChange"
                />
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

    <!-- Markdown 文档面板（用户明确提“文档”时弹出，渲染 md 并支持下载 Word） -->
    <transition name="slide">
      <div v-if="showDocPanel" class="html-preview-panel" :style="{ width: docPanelWidth + 'px' }">
        <div class="preview-header">
          <span class="preview-title">
            <i class="el-icon-document"></i>
            {{ docTitle || '文档' }}
          </span>
          <div class="preview-actions">
            <el-button
              type="success"
              size="small"
              icon="el-icon-download"
              @click="downloadDocAsWord"
              :loading="downloadingDocWord"
              :disabled="!docContent"
            >
              下载 Word
            </el-button>
            <i class="el-icon-close preview-close" @click="closeDocPanel"></i>
          </div>
        </div>
        <div class="preview-content doc-preview-content">
          <div class="markdown-body" v-html="renderMarkdown(docContent)"></div>
        </div>
        <!-- 拖拽调整宽度的手柄 -->
        <div
          class="resize-handle"
          @mousedown="startDocResize"
        ></div>
      </div>
    </transition>
    <el-dialog
      title="设置提示词"
      :visible.sync="promptDialogVisible"
      width="640px"
      append-to-body
      custom-class="prompt-setting-dialog"
    >
      <div class="prompt-dialog-tip">
        自定义提示词将决定回答的风格与专业方向，保存后对你后续的咨询生效。
      </div>
      <el-input
        type="textarea"
        :rows="14"
        v-model="customSystemPrompt"
        placeholder="请输入自定义提示词，例如：你是一名资深财务顾问……"
      ></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="restoreDefaultPrompt">恢复默认</el-button>
        <el-button @click="promptDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="savingPrompt" @click="savePrompt">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import voice from '@/views/index/components/AI/voice.vue'
  import mapData from '@/views/index/components/AI/mock/dataMap.js'
  import fullscreenSvg from '@/views/index/components/AI/icon/fullscreen.svg'
  import marked from 'marked'
  import DOMPurify from 'dompurify'
  import { getAgentList } from '@/api/ai/index.js'
  import { consultStream } from '@/api/ai/starlight'
  import { saveChatHistory, getChatHistoryList, getChatHistoryDetail, deleteChatHistory, updateChatHistory } from '@/api/ai/chatHistory'
  import { getPromptConfig, savePromptConfig } from '@/api/ai/promptConfig'
  import { parseFile } from '@/api/ai/fileParse'
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
    name: 'AIConsult',
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
        currentHtmlIndex: -1, // 当前正在生成HTML的对话索引
        // Markdown 文档面板相关（用户明确提“文档”时，右侧渲染 md 并支持下载 Word）
        docMode: false, // 本轮对话是否为“文档模式”
        showDocPanel: false, // 是否显示 markdown 文档面板
        docContent: '', // 当前文档的 markdown 原文
        docTitle: '文档', // 当前文档标题
        docPanelWidth: 1120, // 文档面板宽度
        docCurrentIndex: -1, // 当前文档对应的对话索引
        downloadingDocWord: false, // 文档面板是否正在下载 Word
        // 历史记录相关
        showHistoryPanel: false, // 是否显示历史记录面板
        historyList: [], // 历史记录列表
        currentHistoryId: null, // 当前选中的历史记录ID
        isSavingHistory: false, // 是否正在保存历史记录
        // 分页相关
        historyPageNum: 1, // 当前页码
        historyPageSize: 20, // 每页条数
        historyHasMore: true, // 是否还有更多数据
        historyLoading: false, // 是否正在加载更多
        // 提示词设置相关
        promptDialogVisible: false, // 提示词设置弹窗是否显示
        customSystemPrompt: '', // 用户自定义系统提示词（已保存/编辑中的内容）
        savingPrompt: false, // 是否正在保存提示词
        // 文件上传相关（上传文档→后端抽文本→拼进消息供模型分析）
        fileObject: { name: '', content: '' }, // 当前已解析的文件 { name, content }
        fileParsing: false, // 是否正在解析上传的文件
        // 默认咨询提示词（与后端 AiConsultController.DEFAULT_CONSULT_SYSTEM_PROMPT 保持一致，用于“恢复默认”）
        defaultSystemPrompt: '你是星光问心AI大模型，是示例云的财经领域专家顾问。\n' +
          '你的职责是为用户提供专业、准确、易懂的财经与业务咨询服务，覆盖以下方向：\n' +
          '1. 财务管理：财务分析、预算管理、成本控制、财务报表解读、财务指标分析\n' +
          '2. 国资监管与合规：穿透式监管、内控合规、风险防范、政策法规解读\n' +
          '3. 经营决策：经营分析、投资评估、商业模式、行业趋势研判\n' +
          '4. 通用咨询：对用户提出的财经、管理、业务问题进行专业解答与建议\n\n' +
          '回答要求：\n' +
          '- 结论先行，条理清晰，必要时分点或用表格呈现\n' +
          '- 基于事实与专业逻辑作答，涉及时效性信息时可联网搜索核实\n' +
          '- 语言通俗专业，避免空话套话，给出可落地的建议'
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
      // 配置 marked：启用表格、换行等（GitHub Flavored Markdown），不用内置清理，交给 DOMPurify
      // 自定义 code 渲染：mermaid 代码块转为占位容器，稍后由 mermaid.js 渲染为 SVG 流程图
      const mermaidRenderer = new marked.Renderer()
      const defaultCodeRenderer = mermaidRenderer.code.bind(mermaidRenderer)
      mermaidRenderer.code = function(code, infostring, escaped) {
        const lang = (infostring || '').trim().split(/\s+/)[0].toLowerCase()
        if (lang === 'mermaid') {
          return '<div class="mermaid-block" data-code="' + encodeURIComponent(code) + '"></div>'
        }
        return defaultCodeRenderer(code, infostring, escaped)
      }
      marked.setOptions({
        gfm: true,
        breaks: true,
        headerIds: false,
        sanitize: false,
        renderer: mermaidRenderer
      })

      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      this.userName = userInfo.realname
      console.log('relativeList', this.relativeList)
      this.sessionId = 'session_' + Date.now()
      
      // 从后端加载历史记录
      this.loadHistoryFromBackend()

      // 加载用户已保存的自定义提示词
      this.loadPromptConfig()

      this.fetchData()
    },
    updated() {
      // DOM 更新后（含流式追加、面板渲染、历史加载），防抖渲染 mermaid 流程图
      this.scheduleMermaidRender()
    },
    beforeDestroy() {
      if (this._mermaidTimer) {
        clearTimeout(this._mermaidTimer)
        this._mermaidTimer = null
      }
    },
    methods: {
      /**
       * 渲染 Markdown 文本为安全的 HTML
       */
      renderMarkdown(text) {
        if (!text) return ''
        try {
          const raw = marked(text)
          return DOMPurify.sanitize(raw, {
            ADD_TAGS: ['table', 'thead', 'tbody', 'tr', 'th', 'td', 'pre', 'code', 'blockquote', 'hr', 'br', 'img', 'div'],
            ADD_ATTR: ['class', 'colspan', 'rowspan', 'align', 'valign', 'scope', 'data-code']
          })
        } catch (e) {
          console.error('Markdown 渲染失败:', e)
          return text
        }
      },
      /**
       * 防抖调度 mermaid 渲染：流式追加时频繁触发，延迟到内容稳定再渲染，避免渲染半截的图定义
       */
      scheduleMermaidRender() {
        if (this._mermaidTimer) clearTimeout(this._mermaidTimer)
        this._mermaidTimer = setTimeout(() => {
          this.renderMermaidBlocks()
        }, 350)
      },
      /**
       * 懒加载并初始化 mermaid（复用全局 window.mermaid，与项目其他流程图组件保持一致）
       */
      async ensureMermaid() {
        if (window.mermaid) return window.mermaid
        const mod = await import('mermaid')
        const mermaid = mod.default || mod
        mermaid.initialize({
          startOnLoad: false,
          theme: 'default',
          securityLevel: 'loose',
          flowchart: { useMaxWidth: true, htmlLabels: true, curve: 'basis' },
          themeVariables: { fontFamily: 'Arial, sans-serif', fontSize: '14px' }
        })
        window.mermaid = mermaid
        return mermaid
      },
      /**
       * 扫描页面内 .mermaid-block 占位容器，将其 data-code 渲染为 SVG 流程图。
       * 已渲染过的（data-rendered）跳过；定义不完整导致渲染失败的保留占位，等下次内容更新重试。
       */
      async renderMermaidBlocks() {
        const blocks = this.$el ? this.$el.querySelectorAll('.mermaid-block[data-code]') : []
        if (!blocks || blocks.length === 0) return
        let mermaid
        try {
          mermaid = await this.ensureMermaid()
        } catch (e) {
          console.error('加载 mermaid 失败:', e)
          return
        }
        for (const el of blocks) {
          const code = decodeURIComponent(el.getAttribute('data-code') || '')
          if (!code.trim()) continue
          // 已渲染且代码未变化则跳过
          if (el.getAttribute('data-rendered') === '1' && el.getAttribute('data-rendered-code') === code) {
            continue
          }
          const id = 'mermaid-svg-' + Date.now() + '-' + Math.floor(Math.random() * 100000)
          try {
            const result = await mermaid.render(id, code)
            const svg = (result && result.svg) ? result.svg : (typeof result === 'string' ? result : '')
            if (svg) {
              el.innerHTML = svg
              el.setAttribute('data-rendered', '1')
              el.setAttribute('data-rendered-code', code)
            }
          } catch (err) {
            // 流式中定义可能不完整，渲染失败属正常，保留占位等下次重试
            // mermaid 渲染失败会向 body 注入临时残留节点，清理掉
            const orphan = document.getElementById('d' + id) || document.getElementById(id)
            if (orphan && orphan.parentNode) orphan.parentNode.removeChild(orphan)
          }
        }
      },
      /**
       * 将单个 mermaid 定义渲染为 PNG（2x 高清），供 Word 导出使用。
       * 调用前应已将 mermaid 全局配置切到 htmlLabels:false（标签用 SVG <text>，转 PNG 文字不丢）。
       * @returns {Promise<{dataUrl:string, width:number, height:number}>}
       */
      async mermaidToPng(code, mermaid, scale = 2) {
        const id = 'mermaid-export-' + Date.now() + '-' + Math.floor(Math.random() * 100000)
        let svg = ''
        try {
          const result = await mermaid.render(id, code)
          svg = (result && result.svg) ? result.svg : (typeof result === 'string' ? result : '')
        } catch (e) {
          const orphan = document.getElementById('d' + id) || document.getElementById(id)
          if (orphan && orphan.parentNode) orphan.parentNode.removeChild(orphan)
          throw e
        }
        if (!svg) throw new Error('mermaid 渲染结果为空')
        // 解析 SVG 取真实尺寸，并固定 width/height（去掉 useMaxWidth 的百分比，保证 Image 加载尺寸确定）
        const parser = new DOMParser()
        const svgDoc = parser.parseFromString(svg, 'image/svg+xml')
        const svgEl = svgDoc.documentElement
        let w = 0
        let h = 0
        const viewBox = svgEl.getAttribute('viewBox')
        if (viewBox) {
          const parts = viewBox.split(/[\s,]+/).map(Number)
          w = parts[2]
          h = parts[3]
        }
        if (!w || !h) {
          w = parseFloat(svgEl.getAttribute('width')) || 800
          h = parseFloat(svgEl.getAttribute('height')) || 600
        }
        svgEl.setAttribute('width', w)
        svgEl.setAttribute('height', h)
        const fixedSvg = new XMLSerializer().serializeToString(svgEl)
        const svgDataUrl = 'data:image/svg+xml;base64,' + btoa(unescape(encodeURIComponent(fixedSvg)))
        return await new Promise((resolve, reject) => {
          const img = new Image()
          img.onload = () => {
            const canvas = document.createElement('canvas')
            canvas.width = Math.ceil(w * scale)
            canvas.height = Math.ceil(h * scale)
            const ctx = canvas.getContext('2d')
            // 白底，避免透明区域在 Word 中显示为黑色
            ctx.fillStyle = '#ffffff'
            ctx.fillRect(0, 0, canvas.width, canvas.height)
            ctx.drawImage(img, 0, 0, canvas.width, canvas.height)
            resolve({ dataUrl: canvas.toDataURL('image/png'), width: w, height: h })
          }
          img.onerror = () => reject(new Error('SVG 转 PNG 失败'))
          img.src = svgDataUrl
        })
      },
      /**
       * 触发隐藏的文件选择框
       */
      triggerFileUpload() {
        if (this.fileParsing) return
        this.$refs.fileInput.click()
      },
      /**
       * 选中文件后：上传到后端解析为文本
       */
      async onFileChange(event) {
        const files = event.target.files
        if (!files || !files.length) return
        const file = files[0]
        this.fileParsing = true
        this.fileObject = { name: file.name, content: '' }
        try {
          const res = await parseFile(file)
          if (res && Number(res.code) === 200 && res.data) {
            this.fileObject = {
              name: res.data.fileName || file.name,
              content: res.data.content || ''
            }
            this.$message.success('文件解析成功，发送时将作为分析内容')
          } else {
            this.fileObject = { name: '', content: '' }
            this.$message.error((res && res.msg) || '文件解析失败')
          }
        } catch (e) {
          console.error('文件解析异常:', e)
          this.fileObject = { name: '', content: '' }
          this.$message.error('文件解析失败，请重试')
        } finally {
          this.fileParsing = false
          // 清空 input，保证同名文件可再次选择
          if (this.$refs.fileInput) this.$refs.fileInput.value = ''
        }
      },
      /**
       * 移除已上传的文件
       */
      removeFile() {
        this.fileObject = { name: '', content: '' }
        if (this.$refs.fileInput) this.$refs.fileInput.value = ''
      },
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
      /**
       * 加载用户已保存的自定义提示词
       */
      async loadPromptConfig() {
        try {
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          const userId = userInfo && userInfo.staffid
          if (!userId) return
          const res = await getPromptConfig(userId, 'consult')
          if (res && res.code === 200 && res.data && res.data.promptContent) {
            this.customSystemPrompt = res.data.promptContent
          }
        } catch (error) {
          console.error('加载提示词配置失败', error)
        }
      },
      /**
       * 打开提示词设置弹窗
       */
      openPromptDialog() {
        // 未设置过则用默认提示词作为初始内容，方便用户在默认基础上修改
        if (!this.customSystemPrompt) {
          this.customSystemPrompt = this.defaultSystemPrompt
        }
        this.promptDialogVisible = true
      },
      /**
       * 恢复默认提示词（仅填充输入框，需点击保存才会写库）
       */
      restoreDefaultPrompt() {
        this.customSystemPrompt = this.defaultSystemPrompt
      },
      /**
       * 保存自定义提示词到数据库
       */
      async savePrompt() {
        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        const userId = userInfo && userInfo.staffid
        if (!userId) {
          this.$message.error('无法获取用户信息，保存失败')
          return
        }
        if (!this.customSystemPrompt || !this.customSystemPrompt.trim()) {
          this.$message.warning('提示词内容不能为空')
          return
        }
        this.savingPrompt = true
        try {
          const res = await savePromptConfig({
            userId: userId,
            promptType: 'consult',
            promptContent: this.customSystemPrompt
          })
          if (res && res.code === 200) {
            this.$message.success('提示词保存成功')
            this.promptDialogVisible = false
          } else {
            this.$message.error((res && res.msg) || '提示词保存失败')
          }
        } catch (error) {
          console.error('保存提示词失败', error)
          this.$message.error('提示词保存失败')
        } finally {
          this.savingPrompt = false
        }
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
        if (this.fileParsing) return this.$message.warning('文件正在解析中，请稍候…')

        // 获取当前北京时间
        const beijingTime = this.getBeijingTime()

        // 构建包含时间上下文的消息
        const userMessage = this.searchKey
        // 文档模式判定：用户明确提到“文档”时，AI 正文渲染到右侧文档面板并支持下载 Word
        this.docMode = /文档/.test(userMessage)
        // 若有已解析的上传文件，把文件内容作为分析材料拼进消息
        let fileContext = ''
        if (this.fileObject && this.fileObject.content) {
          fileContext = `\n\n以下是用户上传的文件【${this.fileObject.name}】的内容，请基于该内容进行分析：\n"""\n${this.fileObject.content}\n"""`
        }
        // 文档模式：要求模型用标记包裹文档正文，过程性说明（开场白、搜索提示等）放标记外
        let docInstruction = ''
        if (this.docMode) {
          docInstruction = '\n\n【输出要求】请把最终的文档正文用 <<<DOC_START>>> 和 <<<DOC_END>>> 两个标记完整包裹起来。' +
            '标记内只放规范的 Markdown 文档正文（标题、正文、表格等），' +
            '不要包含任何开场白、过程说明、联网搜索提示或结束语。所有非正文的说明性文字请放在标记之外。'
        }
        const messageWithTimeContext = `当前北京时间：${beijingTime.formatted}\n\n用户问题：${userMessage}${fileContext}${docInstruction}`

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
          isDoc: this.docMode, // 文档模式标记
          docContent: '', // 文档模式下的 markdown 正文
          docTitle: '文档', // 文档标题
          extraBlocks: [] // 多轮思考内容
        })

        this.isStreaming = true
        this.isLoading = true
        this.stopLoading = false

        const currentIndex = this.dialogue.length - 1

        // 文档模式：发送后立即弹出右侧面板（显示生成中），随流式逐步填充
        if (this.docMode) {
          this.docContent = ''
          this.docTitle = '正在生成文档…'
          this.docCurrentIndex = currentIndex
          this.showDocPanel = true
        }

        // 滚动到底部
        this.$nextTick(() => {
          this.scrollToBottom()
        })

        // 调用流式接口（AI 咨询专用，发送包含时间上下文的消息）
        this.abortController = consultStream(
          {
            message: messageWithTimeContext,
            sessionId: this.sessionId,
            enableThinking: this.think,
            enableWebSearch: this.search,
            stream: true,
            // 用户自定义提示词（有值才带，否则由后端使用默认咨询提示词）
            systemPrompt: (this.customSystemPrompt && this.customSystemPrompt.trim()) ? this.customSystemPrompt : undefined,
            // 额外的时间信息，供后端使用
            currentTime: beijingTime.fullTime,
            timestamp: beijingTime.timestamp,
            timezone: 'Asia/Shanghai'
          },
          {
            onThinking: (data) => {
              if (data.type === 'start') {
                // 思考开始
                const currentContent = this.dialogue[currentIndex].content.trim()
                if (currentContent) {
                  // 已有第一轮内容，创建新的思考块
                  this.dialogue[currentIndex].extraBlocks.push({
                    type: 'thinking',
                    content: '',
                    isThinking: true
                  })
                }
              } else if (data.type === 'content') {
                // 思考内容
                const extraBlocks = this.dialogue[currentIndex].extraBlocks
                const lastThinkingBlock = extraBlocks.filter(b => b.type === 'thinking').pop()
                if (lastThinkingBlock && lastThinkingBlock.isThinking) {
                  // 追加到额外思考块
                  lastThinkingBlock.content += data.content
                } else {
                  // 追加到第一轮思考
                  this.dialogue[currentIndex].thinkContent += data.content
                }
                this.scrollToBottom()
              } else if (data.type === 'end') {
                // 思考结束
                const extraBlocks = this.dialogue[currentIndex].extraBlocks
                const lastThinkingBlock = extraBlocks.filter(b => b.type === 'thinking').pop()
                if (lastThinkingBlock) {
                  lastThinkingBlock.isThinking = false
                }
              }
            },
            onMessage: (content) => {
              // 所有文本内容都追加到主 content
              this.dialogue[currentIndex].content += content
              if (this.dialogue[currentIndex].isDoc) {
                // 文档模式：只提取 <<<DOC_START>>>...<<<DOC_END>>> 标记内的正文，过滤开场白/搜索提示
                const body = this.extractDocBody(this.dialogue[currentIndex].content)
                this.dialogue[currentIndex].docContent = body
                const docTitle = this.extractDocTitle(body)
                this.dialogue[currentIndex].docTitle = docTitle
                // 首次有正文时自动打开面板
                if (!this.showDocPanel || this.docCurrentIndex !== currentIndex) {
                  this.showDocPanel = true
                  this.docCurrentIndex = currentIndex
                }
                this.docContent = body
                this.docTitle = docTitle
              } else {
                // 实时解析 HTML5 内容
                this.parseHtmlContentRealtime(currentIndex)
              }
              this.isLoading = false
              this.scrollToBottom()
            },
            onToolCall: (tool) => {
              // 可以在这里处理工具调用
            },
            onProgress: (message) => {
              // 文档模式下，进度/搜索提示不能进入文档正文，仅忽略（不拼进 content）
              if (this.dialogue[currentIndex].isDoc) {
                this.scrollToBottom()
                return
              }
              // 显示SQL执行进度 - 追加到主 content
              this.dialogue[currentIndex].content += '\n' + message
              this.scrollToBottom()
            },
            onError: (error) => {
              this.isLoading = false
              this.dialogue[currentIndex].content = `请求失败: ${error}`
              this.isStreaming = false
              this.stopLoading = true
              this.isHtmlGenerating = false
              this.$message.error(`请求失败: ${error}`)
              // 保存历史记录（即使出错）
              this.saveHistory()
            },
            onComplete: () => {
              this.isLoading = false
              this.isStreaming = false
              this.stopLoading = true
              this.isHtmlGenerating = false
              // 文档模式：流式结束后确保面板内容为最终完整正文
              if (this.dialogue[currentIndex].isDoc) {
                let body = this.extractDocBody(this.dialogue[currentIndex].content)
                // 兜底：模型未按标记输出时，用全文并剥离 🔍 搜索/进度提示行
                if (!body) {
                  body = this.stripProcessText(this.dialogue[currentIndex].content)
                }
                this.dialogue[currentIndex].docContent = body
                this.dialogue[currentIndex].docTitle = this.extractDocTitle(body)
                this.docCurrentIndex = currentIndex
                this.docContent = body
                this.docTitle = this.dialogue[currentIndex].docTitle || '文档'
                this.showDocPanel = true
              }
              // 流式输出完成后，如果有新的HTML文档，自动打开预览
              if (this.dialogue[currentIndex].htmlContent && this.lastHtmlIndex !== currentIndex) {
                this.lastHtmlIndex = currentIndex
                this.openHtmlPreview(
                  this.dialogue[currentIndex].htmlContent,
                  this.dialogue[currentIndex].htmlTitle,
                  currentIndex
                )
              }
              // 保存历史记录
              this.saveHistory()
            }
          }
        )

        this.searchKey = ''
        // 文件内容已随本条消息发出，清空避免下一条重复携带
        this.fileObject = { name: '', content: '' }
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
          
          console.log('✅ 成功获取用户ID:', userId)
          
          // 获取用户的第一条消息作为标题
          const firstUserMessage = this.dialogue.find(item => item.role === 'user')
          const title = firstUserMessage ? 
            (firstUserMessage.content.length > 30 ? 
              firstUserMessage.content.substring(0, 30) + '...' : 
              firstUserMessage.content) : 
            '新对话'

          // 检查是否有文档
          const hasDocument = this.dialogue.some(item => item.role === 'assistant' && item.htmlContent)

          const historyData = {
            userId: userId,
            sessionId: this.sessionId,
            title: title,
            dialogue: JSON.stringify(this.dialogue),
            hasDocument: hasDocument,
            type: 'consult'
          }
          
          // 如果是更新现有历史记录
          if (this.currentHistoryId) {
            historyData.id = this.currentHistoryId
            const res = await updateChatHistory(historyData)
            if (res.code === 200) {
              console.log('历史记录已更新:', title)
              await this.loadHistoryFromBackend()
            }
          } else {
            // 新增历史记录
            const res = await saveChatHistory(historyData)
            if (res.code === 200 && res.data) {
              this.currentHistoryId = res.data.id
              console.log('历史记录已保存:', title)
              await this.loadHistoryFromBackend()
            }
          }
        } catch (error) {
          console.error('保存历史记录失败:', error)
        } finally {
          this.isSavingHistory = false
        }
      },
      /**
       * 从后端加载历史记录列表（分页，支持滚动加载）
       * @param {Boolean} append 是否追加模式（滚动加载更多时为true）
       */
      async loadHistoryFromBackend(append = false) {
        if (this.historyLoading) return
        if (append && !this.historyHasMore) return

        try {
          this.historyLoading = true
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          const userId = userInfo.staffid

          if (!userId) {
            console.warn('无法获取用户ID')
            this.historyList = []
            return
          }

          // 如果不是追加模式，重置分页
          if (!append) {
            this.historyPageNum = 1
            this.historyHasMore = true
          }

          const res = await getChatHistoryList(userId, 'consult', this.historyPageNum, this.historyPageSize)

          if (res.code === 200 && res.data) {
            const pageData = res.data
            const newItems = (pageData.list || []).map(item => ({
              id: item.id,
              title: item.title,
              time: item.updateTime || item.createTime,
              hasDocument: item.hasDocument,
              sessionId: item.sessionId
            }))

            if (append) {
              // 追加模式：滚动加载更多
              this.historyList = [...this.historyList, ...newItems]
            } else {
              // 刷新模式：替换列表
              this.historyList = newItems
            }

            // 更新分页状态
            this.historyHasMore = pageData.hasMore || false
            this.historyPageNum++
            console.log('加载历史记录:', this.historyList.length, '条, hasMore:', this.historyHasMore)
          }
        } catch (error) {
          console.error('加载历史记录失败:', error)
          if (!append) {
            this.historyList = []
          }
        } finally {
          this.historyLoading = false
        }
      },
      /**
       * 历史记录列表滚动事件处理（滚动到底部时加载更多）
       */
      handleHistoryScroll(event) {
        const el = event.target
        // 距离底部小于50px时触发加载
        if (el.scrollHeight - el.scrollTop - el.clientHeight < 50) {
          this.loadHistoryFromBackend(true)
        }
      },
      /**
       * 加载历史记录（点击时请求详情接口获取完整dialogue）
       */
      async loadHistory(item) {
        try {
          this.currentHistoryId = item.id
          this.lastHtmlIndex = -1
          this.closeHtmlPreview()

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

          // 滚动到底部
          this.$nextTick(() => {
            this.scrollToBottom()
          })

          console.log('已加载历史记录:', item.title)
        } catch (error) {
          console.error('加载历史记录失败:', error)
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
        this.lastHtmlIndex = -1
        this.closeHtmlPreview()
        console.log('创建新对话')
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
            console.error('删除历史记录失败:', error)
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
      /**
       * 实时解析 HTML5 内容（支持流式渲染）
       * 格式: <<<HTML5_START:文档标题>>>...html内容...<<<HTML5_END>>>
       */
      parseHtmlContentRealtime(index) {
        const content = this.dialogue[index].content
        const startTag = '<<<HTML5_START'
        const endTag = '<<<HTML5_END>>>'

        console.log('[HTML解析] 内容长度:', content.length, '内容预览:', content.substring(0, 50))

        const startIndex = content.indexOf(startTag)
        const endIndex = content.indexOf(endTag)

        if (startIndex !== -1) {
          console.log('[HTML解析] 找到开始标记，位置:', startIndex)
        }

        if (startIndex !== -1 && endIndex !== -1 && endIndex > startIndex) {
          // HTML 完整接收完成
          console.log('[HTML解析] HTML完整接收完成')

          // 提取标题
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

          // 提取普通文本内容
          const textBefore = content.substring(0, startIndex).trim()
          const textAfter = content.substring(endIndex + endTag.length).trim()
          const textContent = (textBefore + (textBefore && textAfter ? '\n\n' : '') + textAfter).trim()

          this.dialogue[index].htmlContent = htmlContent
          this.dialogue[index].htmlTitle = htmlTitle
          this.dialogue[index].textContent = textContent

          // 检查是否是模型文档
          this.isModelDocument = htmlTitle.includes('模型') || htmlTitle.includes('数据模型') || htmlTitle.includes('表结构')

          // 更新预览面板
          if (this.showHtmlPreview && this.currentHtmlIndex === index) {
            this.currentHtmlContent = htmlContent
            this.currentHtmlTitle = htmlTitle
            this.isHtmlGenerating = false
          }
        } else if (startIndex !== -1) {
          // 只找到开始标记，正在接收中
          console.log('[HTML解析] 正在接收HTML内容...')

          // 提取标题
          const titleEndIndex = content.indexOf('>>>', startIndex)
          let htmlTitle = '文档'
          if (titleEndIndex !== -1) {
            const titlePart = content.substring(startIndex + startTag.length, titleEndIndex)
            if (titlePart.startsWith(':')) {
              htmlTitle = titlePart.substring(1).trim()
            }
          }

          // 提取已接收的 HTML 内容
          const htmlStartIndex = titleEndIndex !== -1 ? titleEndIndex + 3 : startIndex + startTag.length
          const partialHtml = content.substring(htmlStartIndex).trim()

          // 提取普通文本内容
          const textContent = content.substring(0, startIndex).trim()
          this.dialogue[index].textContent = textContent
          this.dialogue[index].htmlTitle = htmlTitle

          // 如果预览面板未打开，立即打开
          if (!this.showHtmlPreview && partialHtml.length > 0) {
            this.showHtmlPreview = true
            this.currentHtmlIndex = index
            this.currentHtmlTitle = htmlTitle
            this.isHtmlGenerating = true
            this.isModelDocument = htmlTitle.includes('模型') || htmlTitle.includes('数据模型') || htmlTitle.includes('表结构')
          }

          // 实时更新预览内容
          if (this.showHtmlPreview && this.currentHtmlIndex === index) {
            this.currentHtmlContent = this.wrapPartialHtml(partialHtml)
            this.currentHtmlTitle = htmlTitle
          }
        } else {
          // 没有 HTML 标记，全部作为普通文本
          this.dialogue[index].textContent = content
          this.dialogue[index].htmlContent = ''
          this.dialogue[index].htmlTitle = ''
        }
      },
      /**
       * 包装正在生成的 HTML 内容，添加视觉效果
       */
      wrapPartialHtml(partialHtml) {
        // 如果已经有完整的 HTML 结构，直接返回
        if (partialHtml.includes('</html>')) {
          return partialHtml
        }

        // 添加生成中的视觉效果
        return `
          <!DOCTYPE html>
          <html>
          <head>
            <meta charset="UTF-8">
            <style>
              body { font-family: 'Microsoft YaHei', sans-serif; }
              .generating-indicator {
                position: fixed;
                top: 10px;
                right: 10px;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                padding: 8px 16px;
                border-radius: 20px;
                font-size: 12px;
                animation: pulse 1.5s ease-in-out infinite;
                z-index: 9999;
              }
              @keyframes pulse {
                0%, 100% { opacity: 1; }
                50% { opacity: 0.6; }
              }
              .cursor-blink {
                display: inline-block;
                width: 2px;
                height: 1em;
                background: #333;
                animation: blink 1s step-end infinite;
                vertical-align: text-bottom;
                margin-left: 2px;
              }
              @keyframes blink {
                0%, 100% { opacity: 1; }
                50% { opacity: 0; }
              }
            </style>
          </head>
          <body>
            <div class="generating-indicator">正在生成文档...</div>
            ${partialHtml}
            <span class="cursor-blink"></span>
          </body>
          </html>
        `
      },
      /**
       * 打开 HTML 预览
       */
      openHtmlPreview(htmlContent, htmlTitle, index) {
        this.currentHtmlContent = htmlContent
        this.currentHtmlTitle = htmlTitle || '文档预览'
        this.showHtmlPreview = true

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
      },
      /**
       * 生成模型（调用后端接口）
       */
      async generateModel() {
        if (this.generatingModel) return

        this.generatingModel = true
        try {
          // ========== 新逻辑：发送消息给 AI ==========
          // 自动发送一条消息给 AI
          this.searchKey = '好的，我认可你生成的文档内容，请按照文档帮我生成对应的SQL，并帮我插入对应的数据库表'

          // 关闭预览面板
          this.closeHtmlPreview()

          // 延迟一下再发送,让用户看到消息内容
          await new Promise(resolve => setTimeout(resolve, 300))

          // 调用发送消息方法
          this.onAgent()
          // ========== 新逻辑结束 ==========
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
          const code = match[1].replace(/<[^>]+>/g, '').trim()
          if (code.toUpperCase().includes('CREATE TABLE') ||
              code.toUpperCase().includes('INSERT INTO') ||
              code.toUpperCase().includes('ALTER TABLE')) {
            sqlStatements.push(code)
          }
        }

        return sqlStatements
      },
      /**
       * 执行 SQL 语句（调用后端接口）
       */
      async executeSqlStatements(sqlStatements) {
        // TODO: 替换为实际的 API 调用
        // 这里需要调用后端的 SQL 执行接口
        return new Promise((resolve) => {
          setTimeout(() => {
            resolve({
              success: true,
              message: '执行成功'
            })
          }, 1000)
        })
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
       * 提取文档正文：只取 <<<DOC_START>>>...<<<DOC_END>>> 标记之间的内容。
       * 兼容流式：标记未出现返回空；只有起始标记返回其后内容；都有返回中间。
       */
      extractDocBody(content) {
        if (!content) return ''
        const startTag = '<<<DOC_START>>>'
        const endTag = '<<<DOC_END>>>'
        const startIdx = content.indexOf(startTag)
        if (startIdx === -1) {
          // 还没出现起始标记（流式开场白阶段）→ 不显示
          return ''
        }
        const bodyStart = startIdx + startTag.length
        const endIdx = content.indexOf(endTag, bodyStart)
        if (endIdx === -1) {
          // 正在接收正文
          return content.substring(bodyStart).trim()
        }
        return content.substring(bodyStart, endIdx).trim()
      },
      /**
       * 兜底剥离过程性文本：去掉 🔍 联网搜索提示、进度行等非正文内容。
       * 仅在模型未按标记输出时作为降级方案使用。
       */
      stripProcessText(content) {
        if (!content) return ''
        const lines = content.split('\n')
        const kept = lines.filter(line => {
          const t = line.trim()
          if (!t) return true
          // 过滤联网搜索/进度类提示行
          if (t.startsWith('🔍')) return false
          if (t.indexOf('正在联网搜索') !== -1) return false
          if (t.indexOf('正在搜索') !== -1) return false
          return true
        })
        return kept.join('\n').trim()
      },
      /**
       * 从 markdown 内容提取文档标题（取第一个 # 标题，否则用首行/默认）
       */
      extractDocTitle(md) {
        if (!md) return '文档'
        const lines = md.split('\n')
        for (const line of lines) {
          const m = line.match(/^#{1,6}\s+(.+)$/)
          if (m && m[1].trim()) {
            return m[1].trim()
          }
        }
        // 没有标题则取第一行非空文本（截断）
        for (const line of lines) {
          const t = line.trim()
          if (t) {
            return t.length > 20 ? t.substring(0, 20) + '…' : t
          }
        }
        return '文档'
      },
      /**
       * 打开 Markdown 文档面板（点击对话区文档卡片时调用）
       */
      openDocPanel(docContent, docTitle, index) {
        this.docContent = docContent || ''
        this.docTitle = docTitle || '文档'
        this.docCurrentIndex = index != null ? index : -1
        this.showDocPanel = true
      },
      /**
       * 关闭 Markdown 文档面板
       */
      closeDocPanel() {
        this.showDocPanel = false
      },
      /**
       * 开始调整文档面板宽度
       */
      startDocResize(e) {
        this.isResizing = true
        this.startX = e.clientX
        this.startWidth = this.docPanelWidth
        document.addEventListener('mousemove', this.handleDocResize)
        document.addEventListener('mouseup', this.stopDocResize)
        e.preventDefault()
      },
      handleDocResize(e) {
        if (!this.isResizing) return
        const deltaX = this.startX - e.clientX
        const newWidth = this.startWidth + deltaX
        if (newWidth >= 400 && newWidth <= 1200) {
          this.docPanelWidth = newWidth
        }
      },
      stopDocResize() {
        this.isResizing = false
        document.removeEventListener('mousemove', this.handleDocResize)
        document.removeEventListener('mouseup', this.stopDocResize)
      },
      /**
       * 文档面板：将 markdown 渲染为 HTML 后下载为 Word
       */
      async downloadDocAsWord() {
        if (!this.docContent) {
          this.$message.warning('没有可下载的文档内容')
          return
        }
        this.downloadingDocWord = true
        try {
          const htmlDocx = await import('html-docx-js/dist/html-docx')
          // markdown 渲染为 HTML（复用页面统一渲染逻辑）
          let bodyHtml = this.renderMarkdown(this.docContent)
          // mermaid 占位容器：渲染为 PNG 图片嵌入 Word（html-docx-js 不支持 SVG）
          try {
            const parser = new DOMParser()
            const parsed = parser.parseFromString(bodyHtml, 'text/html')
            const blocks = Array.from(parsed.querySelectorAll('.mermaid-block'))
            if (blocks.length > 0) {
              // Word 正文可用宽度约 600px（A4 去页边距），按此上限等比缩放
              const MAX_DOC_WIDTH = 600
              let mermaid = null
              let switched = false
              try {
                mermaid = await this.ensureMermaid()
                // 临时切到 htmlLabels:false，使标签为 SVG <text>，转 PNG 时中文不丢失
                mermaid.initialize({
                  startOnLoad: false,
                  theme: 'default',
                  securityLevel: 'loose',
                  flowchart: { useMaxWidth: false, htmlLabels: false, curve: 'basis' },
                  themeVariables: { fontFamily: 'Microsoft YaHei, Arial, sans-serif', fontSize: '14px' }
                })
                switched = true
                for (const el of blocks) {
                  const code = decodeURIComponent(el.getAttribute('data-code') || '')
                  if (!code.trim()) {
                    el.parentNode.removeChild(el)
                    continue
                  }
                  try {
                    const png = await this.mermaidToPng(code, mermaid, 2)
                    const img = parsed.createElement('img')
                    img.setAttribute('src', png.dataUrl)
                    const displayW = Math.min(png.width, MAX_DOC_WIDTH)
                    const displayH = Math.round(png.height * (displayW / png.width))
                    img.setAttribute('width', displayW)
                    img.setAttribute('height', displayH)
                    el.parentNode.replaceChild(img, el)
                  } catch (convErr) {
                    // 转换失败降级为代码文本，保证不空白
                    const pre = parsed.createElement('pre')
                    pre.textContent = code
                    el.parentNode.replaceChild(pre, el)
                  }
                }
              } finally {
                // 恢复页面渲染用的 htmlLabels:true 配置，避免影响后续在线渲染
                if (switched && mermaid) {
                  mermaid.initialize({
                    startOnLoad: false,
                    theme: 'default',
                    securityLevel: 'loose',
                    flowchart: { useMaxWidth: true, htmlLabels: true, curve: 'basis' },
                    themeVariables: { fontFamily: 'Arial, sans-serif', fontSize: '14px' }
                  })
                }
              }
            }
            bodyHtml = parsed.body.innerHTML
          } catch (e) {
            // 解析失败时保持原样，不阻断导出
          }
          const htmlContent = `
            <!DOCTYPE html>
            <html>
              <head>
                <meta charset="UTF-8">
                <title>${this.docTitle || '文档'}</title>
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
                  blockquote { border-left: 4px solid #ddd; padding-left: 12px; color: #666; margin: 10px 0; }
                </style>
              </head>
              <body>${bodyHtml}</body>
            </html>
          `
          const converted = htmlDocx.asBlob(htmlContent)
          const url = URL.createObjectURL(converted)
          const link = document.createElement('a')
          link.href = url
          link.download = `${this.docTitle || '文档'}.docx`
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          URL.revokeObjectURL(url)
          this.$message.success('Word文档下载成功')
        } catch (error) {
          console.error('下载Word失败:', error)
          this.$message.error('下载Word文档失败: ' + error.message)
        } finally {
          this.downloadingDocWord = false
        }
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
  /* 文件上传：回形针图标与已选文件标签 */
  .icons .el-icon-paperclip {
    cursor: pointer;
  }
  .icons .el-icon-paperclip.is-disabled {
    cursor: not-allowed;
    opacity: 0.5;
  }
  .uploaded-file {
    display: inline-flex;
    align-items: center;
    gap: 6px;
    margin: 8px 0 4px;
    padding: 4px 10px;
    background: #f0f3f6;
    border: 1px solid #d6e0ea;
    border-radius: 6px;
    font-size: 13px;
    color: #4D6BFE;
    max-width: 100%;
  }
  .uploaded-file-name {
    max-width: 240px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .uploaded-file-del {
    cursor: pointer;
    color: #909399;
  }
  .uploaded-file-del:hover {
    color: #f56c6c;
  }

  /* ==================== Markdown 渲染样式（>>> 穿透 scoped，作用于 v-html 内容） ==================== */
  .markdown-body {
    font-size: 15px;
    line-height: 1.75;
    color: #333;
    word-break: break-word;
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
    line-height: 1.4;
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
    background: #f2f4f7;
    color: #c7254e;
    font-size: 0.9em;
  }
  /* 代码块 — 深色背景 */
  .markdown-body >>> pre {
    margin: 14px 0;
    padding: 16px;
    border-radius: 8px;
    background: #2d2d2d;
    overflow-x: auto;
  }
  .markdown-body >>> pre code {
    padding: 0;
    margin: 0;
    border-radius: 0;
    background: transparent;
    color: #f8f8f2;
  }
  /* 引用块 */
  .markdown-body >>> blockquote {
    margin: 12px 0;
    padding: 10px 16px;
    border-left: 4px solid #409EFF;
    background: #f6f8fa;
    color: #606266;
  }
  .markdown-body >>> blockquote p:last-child {
    margin-bottom: 0;
  }
  /* 表格 */
  .markdown-body >>> table {
    margin: 14px 0;
    border-collapse: collapse;
    width: 100%;
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
  /* mermaid 流程图容器 */
  .markdown-body >>> .mermaid-block {
    margin: 16px 0;
    padding: 12px;
    background: #fff;
    border: 1px solid #e8e8e8;
    border-radius: 8px;
    text-align: center;
    overflow-x: auto;
  }
  .markdown-body >>> .mermaid-block svg {
    max-width: 100%;
    height: auto;
  }
  /* 未渲染（流式中）的占位容器不显示边框 */
  .markdown-body >>> .mermaid-block:empty {
    padding: 0;
    border: none;
  }
  .page-title {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  .prompt-setting-btn {
    color: #75a4ff;
    font-size: 14px;
  }
  .prompt-dialog-tip {
    margin-bottom: 12px;
    color: #909399;
    font-size: 13px;
    line-height: 1.6;
  }

  .container {
    /* height: 100vh; */
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

  .history-doc-badge {
    font-size: 12px;
    color: #52c41a;
    display: flex;
    align-items: center;
    gap: 4px;
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

  .history-loading,
  .history-no-more {
    text-align: center;
    padding: 12px 0;
    color: #999;
    font-size: 12px;
  }

  .history-loading i {
    margin-right: 4px;
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

  /* 文档面板内容区：markdown 渲染，可滚动 */
  .doc-preview-content {
    overflow-y: auto;
    padding: 24px 32px;
    background: #fff;
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

  /* 生成中徽章样式 */
  .generating-badge {
    display: inline-block;
    margin-left: 10px;
    padding: 2px 10px;
    font-size: 12px;
    font-weight: normal;
    color: #fff;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 10px;
    animation: pulse 1.5s ease-in-out infinite;
  }

  @keyframes pulse {
    0%, 100% { opacity: 1; }
    50% { opacity: 0.6; }
  }
</style>
