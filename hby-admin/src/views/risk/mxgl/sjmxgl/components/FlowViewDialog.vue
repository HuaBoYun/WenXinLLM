<template>
  <el-dialog
    title="指标执行流程图"
    :visible.sync="dialogVisible"
    width="90%"
    :close-on-click-modal="false"
    :modal="false"
    :modal-append-to-body="false"
    :append-to-body="true"
    class="flow-view-dialog sjmxgl-dialog-scope"
    @close="handleClose"
    :destroy-on-close="true"
    :lock-scroll="false"
    :before-close="handleBeforeClose"
  >
    <div class="flow-container sjmxgl-dialog-scope">
      <!-- 工具栏 -->
      <div class="toolbar">
        <div class="toolbar-left">
          <el-button type="primary" size="mini" @click="generateFlow">
            <i class="el-icon-refresh"></i> 重新生成
          </el-button>
          <el-button size="mini" @click="saveFlow">
            <i class="el-icon-document"></i> 保存流程图
          </el-button>
          <el-button size="mini" @click="exportFlow">
            <i class="el-icon-download"></i> 导出图片
          </el-button>
        </div>
        
        <div class="toolbar-right">
          <el-button-group>
            <el-button size="mini" @click="zoomIn">
              <i class="el-icon-zoom-in"></i>
            </el-button>
            <el-button size="mini" @click="zoomOut">
              <i class="el-icon-zoom-out"></i>
            </el-button>
            <el-button size="mini" @click="resetZoom">
              <i class="el-icon-refresh-left"></i>
            </el-button>
          </el-button-group>
        </div>
      </div>

      <!-- 流程图显示区域 -->
      <div class="flow-display">
        <div v-if="loading" class="loading-state">
          <el-loading text="正在生成流程图..." />
        </div>

        <div v-if="!loading && flowData" class="flow-content">
          <!-- Mermaid 流程图 -->
          <div ref="mermaidContainer" class="mermaid-container">
            <div :id="'mermaid-graph-' + (combination?.combinationId || 'default')" class="mermaid-graph">
            </div>
          </div>
        </div>

        <div v-else-if="!loading && !flowData" class="empty-state">
          <i class="el-icon-picture-outline"></i>
          <p>暂无流程图数据</p>
          <el-button type="primary" @click="generateFlow">生成流程图</el-button>
        </div>
      </div>


    </div>

    <!-- 节点执行结果对话框 -->
    <el-dialog
      title="指标执行结果"
      :visible.sync="nodeResultDialogVisible"
      width="80%"
      :close-on-click-modal="false"
      :append-to-body="true"
      :modal-append-to-body="true"
      :z-index="9500"
      class="node-result-dialog"
      custom-class="node-result-dialog-wrapper"
      @close="handleNodeResultDialogClose"
      :before-close="handleNodeResultDialogClose"
      :destroy-on-close="true"
      :lock-scroll="false"
    >
      <div v-loading="nodeResultLoading" class="node-result-container">
        <div v-if="nodeResultData" class="result-content">
          <!-- 指标基本信息 -->
          <div class="indicator-info">
            <h3>{{ nodeResultData.indicator?.INDICATOR_NAME || '未知指标' }}</h3>
            <div class="info-row">
              <span class="label">指标编码:</span>
              <span class="value">{{ nodeResultData.indicator?.INDICATOR_CODE || '-' }}</span>
            </div>
            <div class="info-row">
              <span class="label">所属组合:</span>
              <span class="value">{{ nodeResultData.indicator?.COMBINATION_NAME || '-' }}</span>
            </div>
            <div class="info-row" v-if="nodeResultData.indicator?.DESCRIPTION">
              <span class="label">描述:</span>
              <span class="value">{{ nodeResultData.indicator.DESCRIPTION }}</span>
            </div>
          </div>

          <!-- 执行统计 -->
          <div v-if="nodeResultData.historyStats" class="execution-stats">
            <h4>执行统计</h4>
            <div class="stats-grid">
              <div class="stat-item">
                <div class="stat-value">{{ nodeResultData.historyStats.TOTAL_COUNT || 0 }}</div>
                <div class="stat-label">总执行次数</div>
              </div>
              <div class="stat-item">
                <div class="stat-value success">{{ nodeResultData.historyStats.SUCCESS_COUNT || 0 }}</div>
                <div class="stat-label">成功次数</div>
              </div>
              <div class="stat-item">
                <div class="stat-value failed">{{ nodeResultData.historyStats.FAILED_COUNT || 0 }}</div>
                <div class="stat-label">失败次数</div>
              </div>
              <div class="stat-item">
                <div class="stat-value">{{ Math.round(nodeResultData.historyStats.AVG_DURATION || 0) }}ms</div>
                <div class="stat-label">平均耗时</div>
              </div>
            </div>
          </div>

          <!-- 执行结果 -->
          <div v-if="nodeResultData.executionResult" class="execution-result">
            <h4>最近执行结果</h4>
            <div class="result-info">
              <div class="info-row">
                <span class="label">执行状态:</span>
                <el-tag :type="getStatusType(nodeResultData.executionResult.STATUS)">
                  {{ getStatusText(nodeResultData.executionResult.STATUS) }}
                </el-tag>
              </div>
              <div class="info-row">
                <span class="label">结果数量:</span>
                <span class="value">{{ nodeResultData.total || 0 }} 条</span>
              </div>
              <div class="info-row">
                <span class="label">执行耗时:</span>
                <span class="value">{{ nodeResultData.executionResult.DURATION || 0 }}ms</span>
              </div>
              <div class="info-row" v-if="nodeResultData.executionResult.ERROR_MESSAGE">
                <span class="label">错误信息:</span>
                <span class="value error">{{ nodeResultData.executionResult.ERROR_MESSAGE }}</span>
              </div>
            </div>
          </div>

          <!-- 结果数据表格 -->
          <div v-if="nodeResultData.resultData && nodeResultData.resultData.length > 0" class="result-data">
            <h4>结果数据
              <span v-if="nodeResultData.isPreview" class="preview-tip">(预览前20条)</span>
            </h4>
            <el-table :data="nodeResultData.resultData" border size="mini" max-height="300">
              <el-table-column
                v-for="(value, key) in nodeResultData.resultData[0]"
                :key="key"
                :prop="key"
                :label="key"
                show-overflow-tooltip
              />
            </el-table>
          </div>

          <div v-else class="no-data">
            <i class="el-icon-document"></i>
            <p>暂无执行结果数据</p>
          </div>
        </div>
      </div>
    </el-dialog>

    <!-- 🔥 新增：指标执行结果对话框 -->
    <IndicatorExecutionResultDialog
      :visible.sync="indicatorResultDialogVisible"
      :indicator-info="selectedIndicatorInfo"
      :combination-id="combination?.combinationId"
      :combination-name="combination?.combinationName"
      @update:visible="handleIndicatorDialogVisibleChange"
    />

    <!-- 🔥 新增：汇聚结果对话框 -->
    <el-dialog
      title="🔄 结果汇聚并行处理"
      :visible.sync="mergeResultDialogVisible"
      width="80%"
      :modal="true"
      :modal-append-to-body="true"
      :close-on-click-modal="true"
      :close-on-press-escape="true"
      :show-close="true"
      class="merge-result-dialog-wrapper"
      :z-index="3600"
      @close="handleMergeResultDialogClose"
      @closed="handleMergeResultDialogClosed"
    >
      <div v-if="mergeResultLoading" class="loading-container">
        <i class="el-icon-loading"></i>
        <span>正在加载汇聚结果...</span>
      </div>
      <div v-else-if="mergeResultData" class="merge-result-content">
        <!-- 汇聚信息概览 -->
        <div class="merge-info-section">
          <el-card class="info-card">
            <div slot="header" class="card-header">
              <span>📊 汇聚信息概览</span>
            </div>
            <div class="info-grid">
              <div class="info-item">
                <label>🎯 组合名称:</label>
                <span>{{ combination.combinationName }}</span>
              </div>
              <div class="info-item">
                <label>⚡ 执行模式:</label>
                <span class="parallel-mode">{{ mergeResultData.executionMode }} (并行执行)</span>
              </div>
              <div class="info-item">
                <label>📈 参与指标:</label>
                <span>{{ mergeResultData.indicatorCount || 0 }} 个</span>
              </div>
              <div class="info-item">
                <label>🔗 交集数据:</label>
                <span class="highlight">{{ mergeResultData.total || 0 }} 条</span>
              </div>
              <div class="info-item">
                <label>📄 当前页:</label>
                <span>第 {{ mergeResultData.pageNum || 1 }} 页 / 共 {{ Math.ceil((mergeResultData.total || 0) / (mergeResultData.pageSize || 20)) }} 页</span>
              </div>
              <div class="info-item">
                <label>📊 显示数量:</label>
                <span>{{ (mergeResultData.mergeData && mergeResultData.mergeData.length) || 0 }} / {{ mergeResultData.pageSize || 20 }} 条/页</span>
              </div>
            </div>
          </el-card>
        </div>

        <!-- 汇聚结果数据表格 -->
        <div class="merge-data-section">
          <el-card class="data-card">
            <div slot="header" class="card-header">
              <span>🔄 汇聚结果数据</span>
              <span class="result-count">(共 {{ mergeResultData.total || 0 }} 条)</span>
              <div class="header-actions">
                <el-button
                  size="mini"
                  type="primary"
                  icon="el-icon-refresh"
                  @click="refreshMergeData"
                  :loading="mergeResultLoading"
                >
                  刷新数据
                </el-button>
              </div>
            </div>
            <div v-if="mergeResultData.mergeData && mergeResultData.mergeData.length > 0" v-loading="mergeResultLoading">
              <el-table
                :data="mergeResultData.mergeData"
                border
                stripe
                height="500"
                class="merge-data-table"
                :show-summary="true"
                :summary-method="getMergeDataSummary"
              >
                <el-table-column
                  v-for="(column, index) in getMergeDataColumns()"
                  :key="index"
                  :prop="column.prop"
                  :label="column.label"
                  :width="column.width"
                  show-overflow-tooltip
                />
              </el-table>

              <!-- 🔥 修复：添加分页组件 -->
              <div class="pagination-container">
                <el-pagination
                  @size-change="handleMergePageSizeChange"
                  @current-change="handleMergePageChange"
                  :current-page="mergeResultData.pageNum || 1"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="mergeResultData.pageSize || 20"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="mergeResultData.total || 0"
                />
              </div>
            </div>
            <div v-else class="no-merge-data">
              <i class="el-icon-info"></i>
              <p>{{ mergeResultData.message || '暂无汇聚结果数据' }}</p>
            </div>
          </el-card>
        </div>
      </div>
      <div v-else class="no-data">
        <i class="el-icon-warning"></i>
        <p>暂无汇聚结果数据</p>
      </div>
    </el-dialog>

      <div v-loading="nodeDetailLoading" class="node-detail-container">
        <div v-if="nodeDetailData" class="detail-content">
          <div class="node-header">
            <span class="node-icon">{{ nodeDetailData.icon }}</span>
            <h3>{{ nodeDetailData.nodeName }}</h3>
            <el-tag size="mini">{{ getNodeTypeText(nodeDetailData.nodeType) }}</el-tag>
          </div>

          <div class="detail-info">
            <div class="info-row" v-if="nodeDetailData.description">
              <span class="label">描述:</span>
              <span class="value">{{ nodeDetailData.description }}</span>
            </div>

            <!-- 指标节点特有信息 -->
            <template v-if="nodeDetailData.nodeType === 'indicator'">
              <div class="info-row" v-if="nodeDetailData.indicatorCode">
                <span class="label">指标编码:</span>
                <span class="value">{{ nodeDetailData.indicatorCode }}</span>
              </div>
              <div class="info-row" v-if="nodeDetailData.dataSourceName">
                <span class="label">数据源:</span>
                <span class="value">{{ nodeDetailData.dataSourceName }}</span>
              </div>
              <div class="info-row" v-if="nodeDetailData.executionOrder">
                <span class="label">执行顺序:</span>
                <span class="value">{{ nodeDetailData.executionOrder }}</span>
              </div>
              <div class="info-row">
                <span class="label">启用状态:</span>
                <el-tag :type="nodeDetailData.isEnabled ? 'success' : 'danger'" size="mini">
                  {{ nodeDetailData.isEnabled ? '已启用' : '已禁用' }}
                </el-tag>
              </div>

              <!-- 最近执行状态 -->
              <div v-if="nodeDetailData.lastExecutionStatus" class="last-execution">
                <h4>最近执行状态</h4>
                <div class="info-row">
                  <span class="label">执行状态:</span>
                  <el-tag :type="getStatusType(nodeDetailData.lastExecutionStatus)" size="mini">
                    {{ getStatusText(nodeDetailData.lastExecutionStatus) }}
                  </el-tag>
                </div>
                <div class="info-row" v-if="nodeDetailData.lastExecutionTime">
                  <span class="label">执行时间:</span>
                  <span class="value">{{ formatDate(nodeDetailData.lastExecutionTime) }}</span>
                </div>
                <div class="info-row" v-if="nodeDetailData.lastDuration">
                  <span class="label">执行耗时:</span>
                  <span class="value">{{ nodeDetailData.lastDuration }}ms</span>
                </div>
                <div class="info-row" v-if="nodeDetailData.lastResultCount">
                  <span class="label">结果数量:</span>
                  <span class="value">{{ nodeDetailData.lastResultCount }} 条</span>
                </div>
              </div>
            </template>
          </div>
        </div>
      </div>
    </el-dialog>

  </el-dialog>
</template>

<script>
import { getCombinationFlow, generateCombinationFlow, saveCombinationFlow, getNodeExecutionResult, getNodeDetail, getExecutionHistory, getExecutionResult } from '@/api/mxgl'
import IndicatorExecutionResultDialog from './IndicatorExecutionResultDialog.vue'

// 🔥 引入对话框层级修复样式
import './dialog-layer-fix.css'

export default {
  name: 'FlowViewDialog',
  components: {
    IndicatorExecutionResultDialog
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    combination: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      dialogVisible: false,
      loading: false,
      flowData: null,
      zoomLevel: 1,

      // 🔥 新增：子对话框关闭标志
      isChildDialogClosing: false,

      // 节点结果对话框
      nodeResultDialogVisible: false,
      nodeResultData: null,
      nodeResultLoading: false,

      // 🔥 新增：指标执行结果对话框相关
      indicatorResultDialogVisible: false,
      selectedIndicatorInfo: null,

      // 🔥 新增：汇聚结果对话框相关
      mergeResultDialogVisible: false,
      selectedMergeInfo: null,
      mergeResultData: null,
      mergeResultLoading: false,

      // 节点详情对话框
      nodeDetailDialogVisible: false,
      nodeDetailData: null,
      nodeDetailLoading: false,
      selectedNodeInfo: null
    }
  },
  computed: {
    mermaidLoaded() {
      return !!(typeof window !== 'undefined' && window.mermaid)
    }
  },
  watch: {
    visible: {
      handler(val) {
        console.log('🔍 FlowViewDialog visible changed:', val)
        console.log('🔍 Current combination:', this.combination)
        this.dialogVisible = val
        if (val) {
          console.log('🔍 Dialog opening, loading flow data...')
          this.loadFlowData()
        }
      },
      immediate: true
    },
    dialogVisible(val) {
      console.log('🔍 FlowViewDialog dialogVisible changed:', val)

      if (val) {
        this.$emit('update:visible', true)
      } else {
        // 🔥 最简单的修复：不发送关闭事件给父组件
        // 让父组件自己管理状态，避免误关闭
        console.log('🔒 FlowViewDialog 关闭，不影响父对话框')

        // 只清理自己的状态
        this.nodeResultDialogVisible = false
        this.nodeDetailDialogVisible = false
        this.indicatorResultDialogVisible = false
        this.mergeResultDialogVisible = false
        this.selectedIndicatorInfo = null
        this.selectedMergeInfo = null
      }
    },

    // 🔥 新增：监听指标执行结果对话框状态
    indicatorResultDialogVisible(val) {
      console.log('🔍 IndicatorResultDialog visible changed:', val)
      if (val) {
        // 🔥 确保指标对话框在最上层
        this.$nextTick(() => {
          this.ensureIndicatorDialogOnTop()
        })
      } else {
        // 🔥 设置子对话框关闭标志
        this.isChildDialogClosing = true
      }
      // 指标执行结果对话框的状态变化不应该影响主对话框
      // 这里只做日志记录，不做任何可能影响父组件的操作
    },

    nodeResultDialogVisible(val) {
      if (val) {
        this.$nextTick(() => {
          this.ensureDialogOnTop('node-result-dialog-wrapper')
        })
      } else {
        // 🔥 设置子对话框关闭标志
        this.isChildDialogClosing = true
      }
    },

    nodeDetailDialogVisible(val) {
      if (val) {
        this.$nextTick(() => {
          this.ensureDialogOnTop('node-detail-dialog-wrapper')
        })
      } else {
        // 🔥 设置子对话框关闭标志
        this.isChildDialogClosing = true
      }
    },

    // 🔥 修复：监听汇聚结果对话框状态
    mergeResultDialogVisible(val) {
      console.log('🔄 汇聚结果对话框状态变化:', val)
      if (val) {
        this.$nextTick(() => {
          this.ensureDialogOnTop('merge-result-dialog-wrapper')
        })
      } else {
        // 🔥 设置子对话框关闭标志
        this.isChildDialogClosing = true
        // 🔥 修复：对话框关闭时，只清理当前组件相关的遮罩
        setTimeout(() => {
          this.clearCurrentDialogMasks()
          this.ensureCurrentDialogInteractable()
        }, 100)
      }
    }
  },
  mounted() {
    // 🔥 修复：不在mounted时加载Mermaid，避免不必要的提示
    // 只在真正需要显示流程图时才加载
    // this.loadMermaid()

    // 验证方法是否存在
    console.log('FlowViewDialog mounted, handleClose exists:', typeof this.handleClose === 'function')

    // 🔥 修复：清理可能残留的遮罩层
    this.clearCurrentDialogMasks()
  },
  beforeDestroy() {
    console.log('🔥 FlowViewDialog beforeDestroy called')
    // 🔥 修复：销毁时只清理当前组件相关的遮罩和状态
    this.clearCurrentDialogMasks()
    this.resetDialogStates()
    this.ensureCurrentDialogInteractable()
  },
  methods: {
    // 动态加载 Mermaid
    async loadMermaid() {
      if (window.mermaid) {
        console.log('✅ Mermaid 已加载')
        return
      }

      try {
        console.log('📦 开始加载 Mermaid...')

        // 动态导入 mermaid
        const mermaid = await import('mermaid')
        window.mermaid = mermaid.default || mermaid

        // 初始化 Mermaid
        window.mermaid.initialize({
          startOnLoad: false,
          theme: 'default',
          securityLevel: 'loose',
          flowchart: {
            useMaxWidth: true,
            htmlLabels: true,
            curve: 'basis'
          },
          themeVariables: {
            fontFamily: 'Arial, sans-serif',
            fontSize: '14px'
          }
        })

        console.log('✅ Mermaid初始化完成，版本:', window.mermaid.version || 'unknown')
      } catch (error) {
        console.error('❌ 加载 Mermaid 失败:', error)
        this.$message.error('流程图组件加载失败: ' + error.message)
        throw error
      }
    },

    // 加载流程图数据
    async loadFlowData() {
      console.log('📊 流程图对话框 - 开始加载流程图数据')

      if (!this.combination || !this.combination.combinationId) {
        console.error('❌ 组合信息不完整，无法加载流程图')
        console.log('combination:', this.combination)
        this.showErrorMessage('组合信息不完整')
        return
      }

      // 🔥 在真正需要显示流程图时才加载Mermaid
      await this.loadMermaid()

      try {
        this.loading = true
        console.log('📊 流程图对话框 - 开始调用API，组合ID:', this.combination.combinationId)
        console.log('📊 流程图对话框 - API路径:', `/riskcontrol/model/combination/flow/${this.combination.combinationId}`)

        const response = await getCombinationFlow(this.combination.combinationId)
        console.log('📊 流程图对话框 - API响应:', response)
        console.log('📊 流程图对话框 - 响应状态码:', response?.code)
        console.log('📊 流程图对话框 - 响应数据:', response?.data)

        // 兼容两种响应格式
        let flowData = null
        if (response && response.code === 1 && response.data) {
          // 标准格式
          flowData = response.data
          console.log('✅ 流程图数据加载成功(标准格式)')
        } else if (response && response.mermaidDefinition) {
          // 直接格式（mock数据）
          flowData = response
          console.log('✅ 流程图数据加载成功(直接格式)')
        } else {
          console.warn('⚠️ 没有有效的流程图数据，尝试自动生成')
          console.log('响应详情:', {
            hasResponse: !!response,
            code: response?.code,
            hasData: !!response?.data,
            dataContent: response?.data
          })
        }

        if (flowData) {
          this.flowData = flowData
          console.log('✅ 成功设置flowData:', this.flowData)
          console.log('📊 mermaidDefinition存在:', !!this.flowData.mermaidDefinition)

          if (this.flowData.mermaidDefinition) {
            console.log('📝 Mermaid定义内容预览:', this.flowData.mermaidDefinition.substring(0, 200) + '...')
          }

          // 等待DOM更新后再渲染
          await this.$nextTick()
          console.log('🔄 DOM已更新，准备渲染...')

          // 再次等待确保DOM完全渲染
          setTimeout(async () => {
            console.log('🎨 开始渲染流程图...')
            await this.renderMermaidGraph()
          }, 200) // 增加等待时间到200ms
        } else {

          // 如果没有流程图数据，自动生成
          await this.generateFlow()
        }
      } catch (error) {
        console.error('❌ 加载流程图失败:', error)
        console.log('错误详情:', {
          message: error.message,
          stack: error.stack,
          response: error.response
        })

        this.$message.error(`加载流程图失败: ${error.message}`)
        this.showErrorMessage(`API调用失败: ${error.message}`)
      } finally {
        this.loading = false
        console.log('🏁 流程图数据加载完成')
      }
    },

    // 生成流程图
    async generateFlow() {
      console.log('=== 开始生成流程图 ===')

      if (!this.combination || !this.combination.combinationId) {
        console.error('❌ 组合信息不完整，无法生成流程图')
        this.$message.error('组合信息不完整')
        this.showErrorMessage('组合信息不完整')
        return
      }

      try {
        this.loading = true
        console.log('🔧 调用生成API，组合ID:', this.combination.combinationId)

        const response = await generateCombinationFlow(this.combination.combinationId)
        console.log('🔧 生成API响应:', response)

        if (response && response.code === 1 && response.data) {
          this.flowData = response.data
          console.log('✅ 生成流程图成功，设置flowData:', this.flowData)
          console.log('📊 生成的mermaidDefinition存在:', !!this.flowData.mermaidDefinition)

          // 等待DOM更新后再渲染
          await this.$nextTick()
          console.log('🔄 DOM已更新，准备渲染生成的流程图...')

          // 再次等待确保DOM完全渲染
          setTimeout(async () => {
            console.log('🎨 开始渲染生成的流程图...')
            await this.renderMermaidGraph()
          }, 200)

          this.$message.success('流程图生成成功')
        } else {
          console.error('❌ 生成流程图失败:', response)
          const errorMsg = response?.msg || '生成流程图失败'
          this.$message.error(errorMsg)
          this.showErrorMessage(errorMsg)
        }
      } catch (error) {
        console.error('❌ 生成流程图异常:', error)
        const errorMsg = `生成失败: ${error.message}`
        this.$message.error(errorMsg)
        this.showErrorMessage(errorMsg)
      } finally {
        this.loading = false
        console.log('🏁 流程图生成完成')
      }
    },

    // 渲染 Mermaid 图表
    async renderMermaidGraph() {
      console.log('=== 开始渲染流程图 ===')
      console.log('flowData:', this.flowData)
      console.log('mermaidDefinition存在:', !!this.flowData?.mermaidDefinition)

      if (!this.flowData || !this.flowData.mermaidDefinition) {
        console.error('❌ 没有流程图数据或Mermaid定义')
        this.showErrorMessage('没有流程图数据')
        return
      }

      if (!window.mermaid) {
        console.error('❌ Mermaid 未加载')
        this.showErrorMessage('流程图组件未加载')
        return
      }

      // 等待容器出现，最多等待5秒
      let container = null
      let attempts = 0
      const maxAttempts = 50 // 50次 * 100ms = 5秒
      const containerId = `mermaid-graph-${this.combination?.combinationId || 'default'}`

      console.log('🔍 开始查找容器:', containerId)

      while (!container && attempts < maxAttempts) {
        container = document.getElementById(containerId)
        if (!container) {
          console.log(`⏳ 第${attempts + 1}次尝试查找${containerId}容器...`)
          await new Promise(resolve => setTimeout(resolve, 100))
          attempts++
        }
      }

      if (!container) {
        console.warn(`⚠️ 通过ID找不到${containerId}容器，尝试其他方式`)

        // 尝试使用ref查找
        if (this.$refs.mermaidContainer) {
          const refContainer = this.$refs.mermaidContainer.querySelector('.mermaid-graph')
          if (refContainer) {
            container = refContainer
            console.log('✅ 通过ref找到容器')
          }
        }

        // 最后的尝试：强制创建容器
        if (!container && this.$refs.mermaidContainer) {
          console.log('🔧 强制创建容器')
          container = document.createElement('div')
          container.id = containerId
          container.className = 'mermaid-graph'
          container.style.width = '100%'
          container.style.height = '400px'
          container.style.minHeight = '400px'
          this.$refs.mermaidContainer.appendChild(container)
          console.log('✅ 强制创建容器成功')
        }

        if (!container) {
          console.error('❌ 所有方式都无法找到或创建容器')
          this.showErrorMessage('无法创建流程图容器')
          return
        }
      }

      console.log('✅ 找到容器:', containerId, container)

      // 清空容器并设置基本样式
      container.innerHTML = ''
      container.style.width = '100%'
      container.style.height = 'auto'
      container.style.minHeight = '400px'

      try {
        console.log('🎨 开始渲染Mermaid图表')
        console.log('Mermaid定义内容:', this.flowData.mermaidDefinition)

        // 初始化Mermaid配置
        window.mermaid.initialize({
          startOnLoad: false,
          theme: 'default',
          securityLevel: 'loose',
          flowchart: {
            useMaxWidth: true,
            htmlLabels: true
          }
        })

        // 兼容不同版本的Mermaid API
        if (typeof window.mermaid.render === 'function') {
          console.log('📝 使用新版本Mermaid API')
          try {
            // 新版本API (v9+)
            const renderResult = await window.mermaid.render('mermaid-svg-' + Date.now(), this.flowData.mermaidDefinition)

            if (renderResult && renderResult.svg) {
              container.innerHTML = renderResult.svg
              console.log('✅ Mermaid渲染成功 (新版本API)')
            } else if (typeof renderResult === 'string') {
              container.innerHTML = renderResult
              console.log('✅ Mermaid渲染成功 (新版本API - 字符串返回)')
            } else {
              throw new Error('渲染结果格式不正确')
            }
          } catch (renderError) {
            console.warn('⚠️ 新版本API失败，尝试旧版本API:', renderError)
            // 回退到旧版本API
            await this.renderWithLegacyAPI(container)
          }
        } else {
          console.log('📝 使用传统Mermaid API')
          await this.renderWithLegacyAPI(container)
        }

        // 流程图渲染完成，添加节点点击事件
        this.addNodeClickEvents(container)

        console.log('🎉 流程图渲染完成')
      } catch (error) {
        console.error('❌ 渲染流程图失败:', error)
        this.showErrorMessage(`渲染失败: ${error.message}`)
      }
    },

    // 使用传统API渲染
    async renderWithLegacyAPI(container) {
      return new Promise((resolve, reject) => {
        try {
          if (typeof window.mermaid.render === 'function') {
            // 旧版本回调API
            window.mermaid.render('mermaid-svg-legacy', this.flowData.mermaidDefinition, (svgCode) => {
              container.innerHTML = svgCode
              console.log('✅ Mermaid渲染成功 (旧版本API)')
              resolve()
            })
          } else {
            // 直接使用mermaid.init方法
            const mermaidDiv = document.createElement('div')
            mermaidDiv.className = 'mermaid'
            mermaidDiv.textContent = this.flowData.mermaidDefinition
            container.appendChild(mermaidDiv)

            window.mermaid.init(undefined, mermaidDiv)
            console.log('✅ Mermaid渲染成功 (init方法)')
            resolve()
          }
        } catch (error) {
          console.error('❌ 传统API渲染失败:', error)
          reject(error)
        }
      })
    },

    // 显示错误信息
    showErrorMessage(message) {
      const containerId = `mermaid-graph-${this.combination?.combinationId || 'default'}`
      const container = document.getElementById(containerId) ||
                       (this.$refs.mermaidContainer && this.$refs.mermaidContainer.querySelector('.mermaid-graph'))

      if (container) {
        container.innerHTML = `
          <div style="text-align: center; padding: 40px; color: #909399; border: 2px dashed #ddd; border-radius: 8px; background: #fafafa;">
            <i class="el-icon-warning" style="font-size: 48px; margin-bottom: 16px; color: #f56c6c;"></i>
            <div style="font-size: 16px; margin-bottom: 8px; color: #606266;">流程图显示异常</div>
            <div style="font-size: 12px; color: #909399;">${message}</div>
            <div style="margin-top: 16px;">
              <button onclick="location.reload()" style="padding: 8px 16px; background: #409eff; color: white; border: none; border-radius: 4px; cursor: pointer;">
                刷新页面
              </button>
            </div>
          </div>
        `
      }
    },



    // 获取容器ID
    getContainerId() {
      return `mermaid-graph-${this.combination?.combinationId || 'default'}`
    },



    // 保存流程图
    async saveFlow() {
      if (!this.flowData) {
        this.$message.warning('暂无流程图数据')
        return
      }
      
      try {
        const response = await saveCombinationFlow({
          combinationId: this.combination.combinationId,
          flowConfig: this.flowData.flowConfig,
          mermaidDefinition: this.flowData.mermaidDefinition
        })
        
        if (response.code === 1) {
          this.$message.success('流程图保存成功')
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        console.error('保存流程图失败:', error)
        this.$message.error('保存流程图失败')
      }
    },

    // 导出流程图
    exportFlow() {
      const containerId = this.getContainerId()
      const svg = document.querySelector(`#${containerId} svg`)
      if (!svg) {
        this.$message.warning('暂无流程图可导出')
        return
      }
      
      // 创建 Canvas
      const canvas = document.createElement('canvas')
      const ctx = canvas.getContext('2d')
      
      // 获取 SVG 尺寸
      const svgRect = svg.getBoundingClientRect()
      canvas.width = svgRect.width
      canvas.height = svgRect.height
      
      // 将 SVG 转换为图片
      const svgData = new XMLSerializer().serializeToString(svg)
      const img = new Image()
      
      img.onload = () => {
        ctx.drawImage(img, 0, 0)
        
        // 下载图片
        const link = document.createElement('a')
        link.download = `${this.combination.combinationName}-流程图.png`
        link.href = canvas.toDataURL('image/png')
        link.click()
        
        this.$message.success('导出成功')
      }
      
      img.src = 'data:image/svg+xml;base64,' + btoa(unescape(encodeURIComponent(svgData)))
    },

    // 缩放控制
    zoomIn() {
      this.zoomLevel = Math.min(this.zoomLevel + 0.1, 2)
      this.applyZoom()
    },

    zoomOut() {
      this.zoomLevel = Math.max(this.zoomLevel - 0.1, 0.5)
      this.applyZoom()
    },

    resetZoom() {
      this.zoomLevel = 1
      this.applyZoom()
    },

    applyZoom() {
      const container = this.$refs.mermaidContainer
      if (container) {
        container.style.transform = `scale(${this.zoomLevel})`
      }
    },

    // 🔥 最简单的修复：关闭前处理
    handleBeforeClose(done) {
      console.log('🔒 FlowViewDialog 准备关闭')
      // 通知父组件关闭
      this.$emit('close')
      done()
    },

    // 对话框关闭处理
    handleClose(event) {
      console.log('🔒 FlowViewDialog handleClose called')

      // 清理状态
      this.flowData = null
      this.zoomLevel = 1
      this.loading = false
      this.nodeResultDialogVisible = false
      this.nodeDetailDialogVisible = false
      this.indicatorResultDialogVisible = false
      this.selectedIndicatorInfo = null
      this.nodeResultData = null
      this.nodeDetailData = null

      console.log('🔒 FlowViewDialog state cleared')
    },

    // 🔥 强制清理所有对话框
    forceCleanupAllDialogs() {
      console.log('🧹 强制清理所有对话框')

      try {
        // 1. 移除所有Element UI对话框包装器
        const wrappers = document.querySelectorAll('.el-dialog__wrapper')
        wrappers.forEach((wrapper, index) => {
          console.log(`移除对话框包装器 ${index + 1}`)
          wrapper.style.display = 'none'
          if (wrapper.parentNode) {
            wrapper.parentNode.removeChild(wrapper)
          }
        })

        // 2. 移除所有遮罩层
        const modals = document.querySelectorAll('.v-modal')
        modals.forEach((modal, index) => {
          console.log(`移除遮罩层 ${index + 1}`)
          if (modal.parentNode) {
            modal.parentNode.removeChild(modal)
          }
        })

        // 3. 移除特定的节点对话框
        const nodeDialogs = document.querySelectorAll('.node-result-dialog-wrapper, .node-detail-dialog-wrapper')
        nodeDialogs.forEach((dialog, index) => {
          console.log(`移除节点对话框 ${index + 1}`)
          if (dialog.parentNode) {
            dialog.parentNode.removeChild(dialog)
          }
        })

        // 4. 清理body样式
        document.body.classList.remove('el-popup-parent--hidden')
        document.body.style.removeProperty('overflow')
        document.body.style.removeProperty('padding-right')

        console.log('✅ 对话框清理完成')

      } catch (error) {
        console.error('❌ 清理对话框时出错:', error)
      }
    },

    // 🔥 新增：处理指标对话框可见性变化
    handleIndicatorDialogVisibleChange(visible) {
      console.log('🔍 指标对话框可见性变化:', visible)
      this.indicatorResultDialogVisible = visible

      if (visible) {
        // 确保指标对话框在最上层
        this.$nextTick(() => {
          this.ensureIndicatorDialogOnTop()
        })
      }
    },

    // 🔥 新增：确保指标对话框在最上层
    ensureIndicatorDialogOnTop() {
      try {
        console.log('🔝 确保指标对话框在最上层')

        // 等待DOM更新
        setTimeout(() => {
          // 查找指标对话框包装器
          const indicatorWrapper = document.querySelector('.indicator-result-dialog-wrapper')
          if (indicatorWrapper) {
            // 设置极高的z-index
            indicatorWrapper.style.setProperty('z-index', '9600', 'important')

            const dialog = indicatorWrapper.querySelector('.el-dialog')
            if (dialog) {
              dialog.style.setProperty('z-index', '9601', 'important')
            }

            // 设置遮罩层
            const modal = indicatorWrapper.querySelector('.v-modal') ||
                         document.querySelector('.v-modal:last-child')
            if (modal) {
              modal.style.setProperty('z-index', '9599', 'important')
            }

            console.log('✅ 指标对话框层级设置完成')
          } else {
            console.warn('⚠️ 未找到指标对话框包装器，尝试其他方式')

            // 备用方案：查找包含指标对话框的包装器
            const allWrappers = document.querySelectorAll('.el-dialog__wrapper')
            allWrappers.forEach((wrapper) => {
              const indicatorDialog = wrapper.querySelector('.indicator-result-dialog')
              if (indicatorDialog) {
                wrapper.style.setProperty('z-index', '9600', 'important')
                indicatorDialog.style.setProperty('z-index', '9601', 'important')
                console.log('✅ 使用备用方案设置指标对话框层级')
              }
            })
          }
        }, 100)
      } catch (error) {
        console.error('❌ 设置指标对话框层级失败:', error)
      }
    },

    // 🔥 新增：清理遮罩层的简单方法
    clearModalMasks() {
      try {
        console.log('🧹 清理遮罩层')

        // 查找所有遮罩层
        const modals = document.querySelectorAll('.v-modal')
        console.log(`🔍 找到 ${modals.length} 个遮罩层`)

        modals.forEach((modal, index) => {
          // 检查遮罩层是否属于流程图对话框
          const wrapper = modal.parentElement
          if (wrapper && wrapper.querySelector('.flow-view-dialog')) {
            console.log(`🗑️ 移除流程图对话框的遮罩层 ${index + 1}`)
            modal.style.display = 'none'
            // 或者直接移除
            // modal.remove()
          }
        })

        // 确保body可以滚动
        document.body.style.overflow = ''
        document.body.classList.remove('el-popup-parent--hidden')

        console.log('✅ 遮罩层清理完成')
      } catch (error) {
        console.error('❌ 清理遮罩层失败:', error)
      }
    },

    // 🔥 新增：重置对话框状态
    resetDialogStates() {
      console.log('🔄 重置对话框状态')
      this.isChildDialogClosing = false
      this.nodeResultDialogVisible = false
      this.nodeDetailDialogVisible = false
      this.indicatorResultDialogVisible = false
      this.mergeResultDialogVisible = false
      this.selectedIndicatorInfo = null
      this.selectedMergeInfo = null
      this.nodeResultData = null
      this.nodeDetailData = null
      this.mergeResultData = null
    },





    // 添加节点点击事件
    addNodeClickEvents(container) {
      try {
        console.log('🖱️ 开始添加节点点击事件')
        console.log('🖱️ 容器:', container)

        // 等待SVG渲染完成，增加等待时间
        setTimeout(() => {
          const svg = container.querySelector('svg')
          console.log('🖱️ SVG元素:', svg)

          if (!svg) {
            console.warn('⚠️ 未找到SVG元素，尝试其他方式查找')
            // 尝试在整个容器中查找SVG
            const allSvgs = container.querySelectorAll('svg')
            console.log('🔍 找到的所有SVG:', allSvgs)
            return
          }

          // 查找所有可能的节点元素
          const nodeSelectors = [
            'g.node',           // 标准节点
            'g[class*="node"]', // 包含node的class
            'g[id*="flowchart"]', // flowchart相关的g元素
            'rect',             // 矩形节点
            'circle',           // 圆形节点
            'ellipse',          // 椭圆节点
            'polygon'           // 多边形节点
          ]

          let allNodes = []
          nodeSelectors.forEach(selector => {
            const nodes = svg.querySelectorAll(selector)
            console.log(`🔍 选择器 ${selector} 找到 ${nodes.length} 个元素`)
            allNodes = [...allNodes, ...Array.from(nodes)]
          })

          // 去重
          allNodes = [...new Set(allNodes)]
          console.log(`🔍 总共找到 ${allNodes.length} 个可能的节点元素`)

          if (allNodes.length === 0) {
            console.warn('⚠️ 未找到任何节点元素，打印SVG内容:')
            console.log(svg.innerHTML.substring(0, 500) + '...')

            // 尝试为整个SVG添加点击事件作为测试
            svg.style.cursor = 'pointer'
            svg.addEventListener('click', (event) => {
              console.log('🖱️ SVG被点击了!', event.target)
              this.$message.info('SVG被点击了，但未找到具体节点')
            })
            return
          }

          allNodes.forEach((node, index) => {
            // 获取节点ID
            const nodeId = this.extractNodeIdFromElement(node)
            console.log(`🏷️ 节点 ${index + 1}: ${nodeId}, 元素:`, node)

            // 为所有节点添加点击事件，不管是否有ID
            node.style.cursor = 'pointer'
            node.addEventListener('click', (event) => {
              event.stopPropagation()
              console.log('🖱️ 节点被点击!', {
                nodeId,
                element: node,
                tagName: node.tagName,
                className: node.className,
                id: node.id
              })

              if (nodeId) {
                this.handleNodeClick(nodeId, node)
              } else {
                console.warn('⚠️ 节点没有有效的ID，无法处理点击事件')
                this.$message.warning('无法识别该节点，请点击其他节点')
              }
            })

            // 添加悬停效果
            node.addEventListener('mouseenter', () => {
              node.style.opacity = '0.8'
              console.log('🖱️ 鼠标进入节点:', nodeId || 'unknown')
            })

            node.addEventListener('mouseleave', () => {
              node.style.opacity = '1'
            })
          })

          console.log('✅ 节点点击事件添加完成')
        }, 1000) // 增加等待时间到1秒
      } catch (error) {
        console.error('❌ 添加节点点击事件失败:', error)
      }
    },

    // 从DOM元素中提取节点ID
    extractNodeIdFromElement(nodeElement) {
      try {
        console.log('🔍 提取节点ID，元素:', nodeElement)

        // 1. 尝试从id属性获取
        if (nodeElement.id) {
          console.log('📋 从ID属性获取:', nodeElement.id)
          let nodeId = nodeElement.id
          // 清理常见的前缀
          nodeId = nodeId.replace(/^flowchart-/, '')
          nodeId = nodeId.replace(/^mermaid-/, '')
          nodeId = nodeId.replace(/-\d+$/, '') // 移除末尾的数字
          console.log('📋 清理后的ID:', nodeId)
          return nodeId
        }

        // 2. 尝试从class获取
        if (nodeElement.className) {
          const classList = Array.from(nodeElement.classList || [])
          console.log('📋 类名列表:', classList)

          for (const className of classList) {
            if (className.startsWith('node-')) {
              const nodeId = className.replace('node-', '')
              console.log('📋 从类名获取ID:', nodeId)
              return nodeId
            }
          }
        }

        // 3. 尝试从父元素获取
        if (nodeElement.parentElement && nodeElement.parentElement.id) {
          console.log('📋 从父元素ID获取:', nodeElement.parentElement.id)
          let parentId = nodeElement.parentElement.id
          parentId = parentId.replace(/^flowchart-/, '').replace(/^mermaid-/, '')
          if (parentId !== nodeElement.parentElement.id) {
            return parentId
          }
        }

        // 4. 尝试从文本内容推断
        const textElement = nodeElement.querySelector('text, tspan, title') || nodeElement
        if (textElement) {
          const text = textElement.textContent?.trim() || ''
          console.log('📋 节点文本内容:', text)

          // 匹配指标节点模式
          if (text.includes('📊') || text.includes('指标')) {
            // 从文本中提取可能的ID
            const matches = text.match(/IND_\w+|[A-Z0-9]{8,}/g)
            if (matches && matches.length > 0) {
              console.log('📋 从文本匹配到ID:', matches[0])
              return matches[0]
            }

            // 如果没有匹配到具体ID，生成一个基于文本的ID
            const simpleText = text.replace(/[^\w\u4e00-\u9fa5]/g, '')
            if (simpleText.length > 0) {
              const generatedId = 'IND_' + simpleText.substring(0, 10)
              console.log('📋 生成的指标ID:', generatedId)
              return generatedId
            }
          }

          // 特殊节点识别
          if (text.includes('开始') || text.includes('START') || text.includes('🚀')) {
            console.log('📋 识别为开始节点')
            return 'START'
          }
          if (text.includes('结束') || text.includes('END') || text.includes('✅') || text.includes('完成')) {
            console.log('📋 识别为结束节点')
            return 'END'
          }
          if (text.includes('汇聚') || text.includes('MERGE') || text.includes('🔄') || text.includes('合并')) {
            console.log('📋 识别为汇聚节点')
            return 'MERGE'
          }

          // 如果文本包含中文，尝试生成一个ID
          if (/[\u4e00-\u9fa5]/.test(text)) {
            const cleanText = text.replace(/[^\w\u4e00-\u9fa5]/g, '').substring(0, 8)
            const generatedId = 'NODE_' + cleanText
            console.log('📋 基于中文生成ID:', generatedId)
            return generatedId
          }
        }

        // 5. 最后尝试：基于元素位置生成ID
        const rect = nodeElement.getBoundingClientRect()
        const positionId = `NODE_${Math.round(rect.x)}_${Math.round(rect.y)}`
        console.log('📋 基于位置生成ID:', positionId)
        return positionId

      } catch (error) {
        console.error('❌ 提取节点ID失败:', error)
        return 'UNKNOWN_NODE'
      }
    },

    // 处理节点点击
    async handleNodeClick(nodeId, nodeElement) {
      try {
        console.log('🖱️ 节点被点击:', {
          nodeId,
          element: nodeElement,
          combination: this.combination,
          textContent: nodeElement.textContent,
          className: nodeElement.className,
          tagName: nodeElement.tagName
        })

        // 检查是否有组合信息
        if (!this.combination || !this.combination.combinationId) {
          this.$message.warning('无法获取组合信息，请重新打开流程图')
          return
        }

        // 🔥 新增：判断节点类型
        const nodeType = this.identifyNodeType(nodeId, nodeElement)
        console.log('🏷️ 节点类型:', nodeType)

        // 🔥 调试：提前测试指标信息提取
        const testIndicatorInfo = this.extractIndicatorInfo(nodeElement)
        console.log('🔍 测试提取的指标信息:', testIndicatorInfo)

        if (nodeType === 'merge') {
          // 🔥 汇聚节点：显示所有并行指标的汇聚结果
          console.log('🔄 处理汇聚节点')
          await this.handleMergeNodeClick(nodeId, nodeElement)
          return
        } else if (nodeType === 'control') {
          // 控制节点：显示节点详情
          console.log('🎛️ 处理控制节点')
          await this.handleControlNodeClick(nodeId, nodeElement)
          return
        } else if (nodeType === 'indicator') {
          // 指标节点：显示指标执行结果
          console.log('📊 处理指标节点')
          await this.handleIndicatorNodeClick(nodeId, nodeElement)
          return
        } else {
          console.warn('⚠️ 未识别的节点类型:', nodeType)
          // 🔥 如果节点类型未识别，但有指标信息，仍然尝试作为指标节点处理
          if (testIndicatorInfo) {
            console.log('🔄 节点类型未识别，但有指标信息，尝试作为指标节点处理')
            await this.handleIndicatorNodeClick(nodeId, nodeElement)
            return
          }
          this.$message.warning('无法识别节点类型，请检查节点配置')
          return
        }
      } catch (error) {
        console.error('❌ 处理节点点击失败:', error)
        this.$message.error('处理节点点击失败: ' + error.message)
      }
    },

    // 🔥 新增：识别节点类型
    identifyNodeType(nodeId, nodeElement) {
      try {
        const textContent = nodeElement.textContent || ''
        console.log('🔍 识别节点类型，文本内容:', textContent)

        // 1. 汇聚节点识别
        if (nodeId === 'MERGE' || nodeId === 'merge' ||
            textContent.includes('🔄') || textContent.includes('结果汇聚') ||
            textContent.includes('并行处理')) {
          return 'merge'
        }

        // 2. 控制节点识别
        if (nodeId === 'START' || nodeId === 'start' ||
            nodeId === 'END' || nodeId === 'end' ||
            textContent.includes('🚀') || textContent.includes('✅') ||
            textContent.includes('开始') || textContent.includes('结束')) {
          return 'control'
        }

        // 3. 指标节点识别
        if (textContent.includes('📊') || textContent.includes('指标') ||
            textContent.includes('🏷️ 编码:') ||
            this.parseIndicatorCodeFromText(textContent)) {
          return 'indicator'
        }

        // 默认返回未知类型
        return 'unknown'
      } catch (error) {
        console.error('❌ 识别节点类型失败:', error)
        return 'unknown'
      }
    },

    // 🔥 新增：处理汇聚节点点击
    async handleMergeNodeClick(nodeId, nodeElement) {
      try {
        console.log('🔄 处理汇聚节点点击:', nodeId)

        // 获取当前组合的执行模式
        const executionMode = this.combination.executionMode
        if (executionMode !== 'PARALLEL') {
          this.$message.warning('当前组合不是并行执行模式，无法显示汇聚结果')
          return
        }

        // 设置汇聚信息并显示对话框
        this.selectedMergeInfo = {
          nodeId,
          nodeName: '🔄 结果汇聚并行处理',
          description: '汇聚所有并行执行指标的结果，显示相同数据的交集',
          combinationId: this.combination.combinationId,
          executionMode: executionMode
        }

        this.mergeResultDialogVisible = true

        // 🔥 立即加载汇聚结果数据
        await this.loadMergeResultData()

        this.$message.success('汇聚结果加载完成')
      } catch (error) {
        console.error('❌ 处理汇聚节点点击失败:', error)
        this.$message.error('处理汇聚节点点击失败: ' + error.message)
      }
    },

    // 🔥 新增：处理控制节点点击
    async handleControlNodeClick(nodeId, nodeElement) {
      try {
        console.log('🎛️ 处理控制节点点击:', nodeId)

        // 显示节点详情
        this.selectedNodeInfo = {
          nodeId,
          nodeName: this.getControlNodeName(nodeId),
          description: this.getControlNodeDescription(nodeId),
          nodeType: 'control'
        }

        this.nodeDetailDialogVisible = true
      } catch (error) {
        console.error('❌ 处理控制节点点击失败:', error)
        this.$message.error('处理控制节点点击失败: ' + error.message)
      }
    },

    // 🔥 新增：处理指标节点点击
    async handleIndicatorNodeClick(nodeId, nodeElement) {
      try {
        console.log('📊 处理指标节点点击:', nodeId)

        // 🔥 提取指标信息
        const indicatorInfo = this.extractIndicatorInfo(nodeElement)
        console.log('📊 提取的指标信息:', indicatorInfo)

        if (!indicatorInfo) {
          this.$message.warning('无法从节点中提取指标信息')
          return
        }

        // 🔥 核心修复：使用指标编码调用后端API
        console.log('📡 开始调用后端API获取指标执行结果...')

        // 🔥 关键修复：从指标信息中获取真正的指标编码
        const indicatorCode = indicatorInfo.indicatorCode
        if (!indicatorCode) {
          console.error('❌ 无法获取指标编码:', indicatorInfo)
          this.$message.error('无法获取指标编码，请检查节点配置')
          return
        }

        console.log('🏷️ 使用指标编码调用API:', indicatorCode)

        // 显示加载状态
        this.$message.info('正在获取指标执行结果...')

        try {
          // 🔥 使用指标编码而不是节点ID调用API
          const response = await getNodeExecutionResult(indicatorCode, {
            combinationId: this.combination.combinationId,
            pageNum: 1,
            pageSize: 20
          })

          console.log('📡 API响应:', response)

          if (response && response.code === 1) {
            // API调用成功，使用真实数据
            console.log('✅ 成功获取指标执行结果:', response.data)

            // 设置选中的指标信息（包含API返回的数据）
            this.selectedIndicatorInfo = {
              ...indicatorInfo,
              apiData: response.data  // 将API数据附加到指标信息中
            }

            // 显示指标执行结果对话框
            this.indicatorResultDialogVisible = true
            console.log('🔥 设置 indicatorResultDialogVisible = true')

            this.$message.success('指标执行结果获取成功')
            console.log('✅ 指标执行结果对话框已打开（使用真实API数据）')
          } else {
            // API调用失败，但仍然显示对话框（使用静态数据）
            console.warn('⚠️ API调用失败，使用静态数据:', response?.msg)

            this.selectedIndicatorInfo = indicatorInfo
            this.indicatorResultDialogVisible = true
            console.log('🔥 设置 indicatorResultDialogVisible = true (静态数据)')

            this.$message.warning(`API调用失败: ${response?.msg || '未知错误'}，显示静态数据`)
          }
        } catch (apiError) {
          // API调用异常，但仍然显示对话框（使用静态数据）
          console.error('❌ API调用异常:', apiError)

          this.selectedIndicatorInfo = indicatorInfo
          this.indicatorResultDialogVisible = true
          console.log('🔥 设置 indicatorResultDialogVisible = true (异常处理)')

          this.$message.warning(`API调用异常: ${apiError.message}，显示静态数据`)
        }

        // 🔥 强制更新组件状态并检查对话框状态
        this.$forceUpdate()
        this.$nextTick(() => {
          console.log('🔄 强制更新完成，当前对话框状态:', {
            indicatorResultDialogVisible: this.indicatorResultDialogVisible,
            selectedIndicatorInfo: this.selectedIndicatorInfo
          })
        })

      } catch (error) {
        console.error('❌ 处理节点点击失败:', error)
        this.$message.error(`获取节点信息失败: ${error.message}`)
      }
    },

    // 判断是否为指标节点
    isIndicatorNode(nodeId) {
      if (!nodeId) return false

      console.log('🔍 判断节点类型:', nodeId)

      // 排除特殊节点
      const specialNodes = ['START', 'END', 'MERGE', 'start', 'end', 'merge']
      if (specialNodes.includes(nodeId)) {
        console.log('🔍 特殊节点，非指标节点')
        return false
      }

      // 🔥 修复：更宽松的指标节点识别逻辑，包含IND1这种格式
      const isIndicator = nodeId.startsWith('IND') ||  // 修复：IND开头（不只是IND_）
                         nodeId.startsWith('INDICATOR') ||
                         nodeId.includes('indicator') ||
                         nodeId.match(/^[0-9a-fA-F-]{8,}$/) ||
                         nodeId.match(/^CONFIG_\d+$/) ||
                         nodeId.match(/^IND\d+$/) ||  // 新增：IND+数字格式
                         nodeId.match(/^METRIC_/) ||  // 新增：METRIC_开头
                         nodeId.match(/^MEASURE_/)    // 新增：MEASURE_开头

      console.log('🔍 节点类型判断结果:', { nodeId, isIndicator })
      console.log('🔍 详细匹配结果:', {
        startsWithIND: nodeId.startsWith('IND'),
        startsWithINDICATOR: nodeId.startsWith('INDICATOR'),
        includesIndicator: nodeId.includes('indicator'),
        uuidFormat: nodeId.match(/^[0-9a-fA-F-]{8,}$/),
        configFormat: nodeId.match(/^CONFIG_\d+$/),
        indNumberFormat: nodeId.match(/^IND\d+$/),
        metricFormat: nodeId.match(/^METRIC_/),
        measureFormat: nodeId.match(/^MEASURE_/)
      })
      return isIndicator
    },

    // 🔥 新方法：基于指标信息显示执行结果
    async showIndicatorExecutionResult(nodeElement) {
      try {
        console.log('📊 开始显示指标执行结果:', nodeElement)

        // 先显示对话框
        this.nodeResultDialogVisible = true
        this.nodeResultLoading = true
        this.nodeResultData = null

        // 强制更新确保对话框显示
        this.$forceUpdate()

        // 等待DOM更新后强制显示
        this.$nextTick(() => {
          setTimeout(() => {
            this.forceShowElementUIDialog()
          }, 100)
        })

        // 🔥 核心改进：从节点元素中提取指标信息
        const indicatorInfo = this.extractIndicatorInfo(nodeElement)
        console.log('📊 提取的指标信息:', indicatorInfo)

        if (!indicatorInfo) {
          throw new Error('无法从节点中提取指标信息')
        }

        // 🔥 查询该指标的成功执行结果
        const successResult = await this.getIndicatorSuccessExecutionResult(indicatorInfo)
        console.log('📊 指标成功执行结果:', successResult)

        if (successResult && successResult.result) {
          // 🔥 使用成功执行结果构建显示数据
          this.nodeResultData = this.buildIndicatorResultData(
            indicatorInfo,
            successResult.execution,
            successResult.result,
            successResult.allExecutions
          )
          console.log('✅ 指标执行结果构建成功:', this.nodeResultData)
        } else {
          throw new Error('该指标没有成功的执行结果')
        }

      } catch (error) {
        console.error('❌ 获取指标执行结果失败:', error)
        this.$message.error(`获取指标执行结果失败: ${error.message}`)

        // 失败时关闭对话框
        this.nodeResultDialogVisible = false
        this.nodeResultData = null
      } finally {
        this.nodeResultLoading = false
        console.log('📊 指标执行结果获取流程完成')
      }
    },

    // 🔥 从节点元素中提取指标信息
    extractIndicatorInfo(nodeElement) {
      try {
        // 方法1：从文本内容中提取
        const textContent = nodeElement.textContent || ''
        console.log('🔍 节点文本内容:', textContent)

        // 🔥 方法2：从文本中解析真正的指标编码
        const realIndicatorCode = this.parseIndicatorCodeFromText(textContent)
        console.log('🔍 解析的指标编码:', realIndicatorCode)

        // 方法3：从组合配置中查找匹配的指标
        if (this.combination && this.combination.indicators) {
          const indicators = this.combination.indicators

          // 🔥 优先通过解析的编码匹配
          let matchedIndicator = null

          if (realIndicatorCode) {
            matchedIndicator = indicators.find(indicator => {
              const indicatorCode = indicator.indicatorCode || indicator.code || ''
              return indicatorCode === realIndicatorCode
            })
          }

          // 如果编码匹配失败，尝试通过名称匹配
          if (!matchedIndicator) {
            matchedIndicator = indicators.find(indicator => {
              const indicatorName = indicator.indicatorName || indicator.name || ''
              const indicatorCode = indicator.indicatorCode || indicator.code || ''

              return textContent.includes(indicatorName) ||
                     textContent.includes(indicatorCode) ||
                     indicatorName.includes(textContent.trim())
            })
          }

          if (matchedIndicator) {
            console.log('✅ 找到匹配的指标:', matchedIndicator)
            return {
              indicatorId: matchedIndicator.indicatorId || matchedIndicator.id,
              indicatorName: matchedIndicator.indicatorName || matchedIndicator.name,
              indicatorCode: matchedIndicator.indicatorCode || matchedIndicator.code,
              combinationId: this.combination.combinationId
            }
          }
        }

        // 🔥 方法4：如果有解析的编码，使用解析的编码
        if (realIndicatorCode) {
          return {
            indicatorId: null,
            indicatorName: realIndicatorCode, // 使用编码作为名称
            indicatorCode: realIndicatorCode,
            combinationId: this.combination?.combinationId
          }
        }

        // 方法5：如果没有找到，使用文本内容作为指标名称
        if (textContent.trim()) {
          return {
            indicatorId: null,
            indicatorName: textContent.trim(),
            indicatorCode: textContent.trim(),
            combinationId: this.combination?.combinationId
          }
        }

        return null
      } catch (error) {
        console.error('❌ 提取指标信息失败:', error)
        return null
      }
    },

    // 🔥 新增：从文本中解析真正的指标编码
    parseIndicatorCodeFromText(text) {
      try {
        console.log('🔍 解析指标编码，输入文本:', text)

        // 🔥 模式1：🏷️ 编码: XXXX 格式（支持大小写字母、数字、下划线）
        const pattern1 = /🏷️\s*编码:\s*([a-zA-Z0-9_]+)/
        const match1 = text.match(pattern1)
        if (match1) {
          console.log('✅ 模式1匹配成功:', match1[1])
          return match1[1]
        }

        // 🔥 模式2：编码: XXXX 格式（支持大小写字母、数字、下划线）
        const pattern2 = /编码:\s*([a-zA-Z0-9_]+)/
        const match2 = text.match(pattern2)
        if (match2) {
          console.log('✅ 模式2匹配成功:', match2[1])
          return match2[1]
        }

        // 模式3：CODE: XXXX 格式
        const pattern3 = /CODE:\s*([a-zA-Z0-9_]+)/i
        const match3 = text.match(pattern3)
        if (match3) {
          console.log('✅ 模式3匹配成功:', match3[1])
          return match3[1]
        }

        // 🔥 模式4：匹配常见的指标编码格式（更宽松的匹配，支持大小写）
        const pattern4 = /([a-zA-Z][a-zA-Z0-9_]{2,})/g
        const matches4 = text.match(pattern4)
        if (matches4 && matches4.length > 0) {
          // 选择最长的匹配作为指标编码
          const longestMatch = matches4.reduce((a, b) => a.length > b.length ? a : b)
          console.log('✅ 模式4匹配成功:', longestMatch)
          return longestMatch
        }

        // 🔥 模式5：直接的字母编码（如果文本很短且全是字母数字下划线）
        if (text.length <= 15 && /^[a-zA-Z0-9_]+$/.test(text.trim())) {
          console.log('✅ 模式5匹配成功:', text.trim())
          return text.trim()
        }

        // 🔥 模式6：从多行文本中提取编码（处理换行符，支持大小写）
        const lines = text.split(/[\n\r]+/).map(line => line.trim()).filter(line => line)
        for (const line of lines) {
          if (/^[a-zA-Z][a-zA-Z0-9_]{2,}$/.test(line)) {
            console.log('✅ 模式6匹配成功:', line)
            return line
          }
        }

        console.log('❌ 未能解析出指标编码')
        return null
      } catch (error) {
        console.error('❌ 解析指标编码失败:', error)
        return null
      }
    },

    // 🔥 新增：获取控制节点名称
    getControlNodeName(nodeId) {
      const nodeNames = {
        'START': '🚀 开始节点',
        'start': '🚀 开始节点',
        'END': '✅ 结束节点',
        'end': '✅ 结束节点',
        'MERGE': '🔄 结果汇聚节点',
        'merge': '🔄 结果汇聚节点'
      }
      return nodeNames[nodeId] || '🎛️ 控制节点'
    },

    // 🔥 新增：获取控制节点描述
    getControlNodeDescription(nodeId) {
      const descriptions = {
        'START': '流程开始，准备执行指标分析',
        'start': '流程开始，准备执行指标分析',
        'END': '流程结束，所有指标执行完成',
        'end': '流程结束，所有指标执行完成',
        'MERGE': '汇聚所有指标执行结果，进行交集分析',
        'merge': '汇聚所有指标执行结果，进行交集分析'
      }
      return descriptions[nodeId] || '流程控制节点'
    },

    // 🔥 修复：处理汇聚结果对话框关闭 - 只清理自己的状态
    handleMergeResultDialogClose() {
      console.log('🔄 汇聚结果对话框关闭')
      this.mergeResultDialogVisible = false
      this.selectedMergeInfo = null
      this.mergeResultData = null
      this.mergeResultLoading = false

      // 🔥 修复：只清理当前组件相关的遮罩层，不影响其他组件
      this.$nextTick(() => {
        this.clearCurrentDialogMasks()
        // 不调用 resetDialogStates()，避免影响其他对话框
      })
    },

    // 🔥 修复：处理汇聚结果对话框完全关闭后 - 避免影响其他组件
    handleMergeResultDialogClosed() {
      console.log('🔄 汇聚结果对话框完全关闭后处理')

      // 延迟执行清理，确保对话框动画完成
      setTimeout(() => {
        // 🔥 修复：只清理当前对话框相关的遮罩，不影响其他组件
        this.clearCurrentDialogMasks()

        // 🔥 修复：不强制重新渲染父组件，避免影响其他组件状态
        // this.$forceUpdate() // 注释掉，避免影响父组件

        // 🔥 修复：只确保当前对话框区域可交互，不影响全局
        this.ensureCurrentDialogInteractable()
      }, 300) // 等待对话框关闭动画完成
    },

    // 🔥 新增：确保当前对话框区域可交互（修复版）
    ensureCurrentDialogInteractable() {
      console.log('🔓 确保当前对话框区域可交互...')

      try {
        // 🔥 修复：只处理当前组件相关的样式，不影响全局
        const currentDialog = this.$el
        if (currentDialog) {
          currentDialog.style.pointerEvents = ''
        }

        // 🔥 修复：只检查当前组件相关的遮罩层
        const currentDialogMasks = this.$el ? this.$el.querySelectorAll('.v-modal') : []
        if (currentDialogMasks.length > 0) {
          console.log('🔍 发现当前对话框残留遮罩:', currentDialogMasks.length)
          currentDialogMasks.forEach(mask => mask.remove())
        }

        // 🔥 修复：检查是否还有全局遮罩层，但不强制清理
        const remainingMasks = document.querySelectorAll('.v-modal')
        if (remainingMasks.length > 0) {
          console.warn('⚠️ 仍有遮罩层残留，强制清理:', remainingMasks.length)
          remainingMasks.forEach(mask => mask.remove())
        }

        console.log('✅ 页面交互性恢复完成')
      } catch (error) {
        console.error('❌ 恢复页面交互性失败:', error)
      }
    },

    // 🔥 新增：获取汇聚数据表格列
    getMergeDataColumns() {
      if (!this.mergeResultData || !this.mergeResultData.mergeData || this.mergeResultData.mergeData.length === 0) {
        return []
      }

      // 从第一行数据中提取列信息
      const firstRow = this.mergeResultData.mergeData[0]
      const columns = []

      Object.keys(firstRow).forEach((key, index) => {
        columns.push({
          prop: key,
          label: key,
          width: index === 0 ? '150px' : 'auto'
        })
      })

      return columns
    },

    // 🔥 新增：获取汇聚数据汇总信息
    getMergeDataSummary(param) {
      const { columns, data } = param
      const sums = []

      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = `共 ${this.mergeResultData?.total || 0} 条汇聚数据`
          return
        }

        // 对于数值列，计算汇总
        const values = data.map(item => Number(item[column.property]))
        if (!values.every(value => isNaN(value))) {
          sums[index] = values.reduce((prev, curr) => {
            const value = Number(curr)
            if (!isNaN(value)) {
              return prev + curr
            } else {
              return prev
            }
          }, 0)
          sums[index] += ' (合计)'
        } else {
          sums[index] = ''
        }
      })

      return sums
    },

    // 🔥 新增：处理汇聚结果分页大小变化
    handleMergePageSizeChange(pageSize) {
      console.log('🔄 汇聚结果分页大小变化:', pageSize)
      this.loadMergeResultData(1, pageSize)
    },

    // 🔥 新增：处理汇聚结果页码变化
    handleMergePageChange(pageNum) {
      console.log('🔄 汇聚结果页码变化:', pageNum)
      const pageSize = this.mergeResultData?.pageSize || 20
      this.loadMergeResultData(pageNum, pageSize)
    },

    // 🔥 新增：加载汇聚结果数据
    async loadMergeResultData(pageNum = 1, pageSize = 20) {
      try {
        if (!this.selectedMergeInfo) {
          return
        }

        console.log('📊 流程图对话框 - 加载汇聚结果数据')
        this.mergeResultLoading = true

        // 调用后端API获取汇聚结果
        const response = await getNodeExecutionResult('🔄 结果汇聚并行处理', {
          combinationId: this.selectedMergeInfo.combinationId,
          executionId: '', // 获取最新执行结果
          pageNum,
          pageSize,
          token: this.$store.getters.token
        })

        console.log('📊 流程图对话框 - 汇聚结果API响应:', response)

        // 兼容两种响应格式
        let mergeData = null
        if (response && response.code === 1) {
          // 标准格式
          mergeData = response.data
          console.log('✅ 汇聚结果数据加载成功(标准格式)')
        } else if (response && response.mergeData) {
          // 直接格式（mock数据）
          mergeData = response
          console.log('✅ 汇聚结果数据加载成功(直接格式)')
        } else {
          console.error('❌ 汇聚结果数据加载失败:', response)
          this.$message.error(response.msg || '加载汇聚结果失败')
          this.mergeResultLoading = false
          return
        }

        if (mergeData) {
          this.mergeResultData = mergeData

          // 🔥 确保分页信息正确设置
          if (!this.mergeResultData.pageNum) {
            this.mergeResultData.pageNum = pageNum
          }
          if (!this.mergeResultData.pageSize) {
            this.mergeResultData.pageSize = pageSize
          }

          console.log('✅ 成功获取汇聚结果:', this.mergeResultData)
          console.log(`📊 数据统计: 当前页 ${this.mergeResultData.pageNum}/${Math.ceil(this.mergeResultData.total / this.mergeResultData.pageSize)}, 每页 ${this.mergeResultData.pageSize} 条, 总计 ${this.mergeResultData.total} 条`)

          // 🔥 验证数据完整性
          if (this.mergeResultData.mergeData && this.mergeResultData.mergeData.length > 0) {
            console.log(`📋 当前页显示 ${this.mergeResultData.mergeData.length} 条数据`)
          } else {
            console.warn('⚠️ 当前页没有数据')
          }
        } else {
          console.warn('⚠️ 获取汇聚结果失败:', response?.msg)
          this.$message.warning(`获取汇聚结果失败: ${response?.msg || '未知错误'}`)

          // 设置默认数据
          this.mergeResultData = {
            nodeType: 'merge',
            message: response?.msg || '获取汇聚结果失败',
            mergeData: [],
            total: 0,
            pageNum: pageNum,
            pageSize: pageSize
          }
        }
      } catch (error) {
        console.error('❌ 加载汇聚结果数据失败:', error)
        this.$message.error(`加载汇聚结果失败: ${error.message}`)

        // 设置错误数据
        this.mergeResultData = {
          nodeType: 'merge',
          message: `加载失败: ${error.message}`,
          mergeData: [],
          total: 0
        }
      } finally {
        this.mergeResultLoading = false
      }
    },

    // 🔥 新增：刷新汇聚数据
    refreshMergeData() {
      console.log('🔄 刷新汇聚数据...')
      if (this.mergeResultData) {
        const currentPageNum = this.mergeResultData.pageNum || 1
        const currentPageSize = this.mergeResultData.pageSize || 20
        this.loadMergeResultData(currentPageNum, currentPageSize)
      } else {
        this.loadMergeResultData(1, 20)
      }
    },

    // 🔥 修复：只清理当前组件相关的遮罩层
    clearCurrentDialogMasks() {
      console.log('🧹 开始清理当前组件相关的遮罩层...')

      try {
        // 🔥 修复：只清理当前组件内的遮罩层
        if (this.$el) {
          const currentDialogMasks = this.$el.querySelectorAll('.v-modal')
          currentDialogMasks.forEach((modal, index) => {
            console.log(`🗑️ 移除当前组件遮罩层 ${index + 1}:`, modal)
            modal.remove()
          })
        }

        // 🔥 修复：只清理特定的汇聚结果对话框遮罩
        const mergeDialogMasks = document.querySelectorAll('.merge-result-dialog-wrapper .v-modal')
        mergeDialogMasks.forEach((mask, index) => {
          console.log(`🗑️ 移除汇聚结果对话框遮罩 ${index + 1}:`, mask)
          mask.remove()
        })

        // 🔥 修复：只清理当前组件的节点结果对话框遮罩
        const nodeResultMasks = document.querySelectorAll('.node-result-dialog-wrapper .v-modal')
        nodeResultMasks.forEach((mask, index) => {
          console.log(`🗑️ 移除节点结果对话框遮罩 ${index + 1}:`, mask)
          mask.remove()
        })

        console.log('✅ 当前组件遮罩层清理完成')
      } catch (error) {
        console.error('❌ 清理当前组件遮罩层失败:', error)
      }
    },

    // 🔥 保留原方法作为备用（但不在正常流程中使用）
    clearAllModalMasks() {
      console.log('🧹 开始清理所有遮罩层...')

      try {
        // 方法1: 移除所有Element-UI遮罩层
        const modalElements = document.querySelectorAll('.v-modal')
        modalElements.forEach((modal, index) => {
          console.log(`🗑️ 移除遮罩层 ${index + 1}:`, modal)
          modal.remove()
        })

        // 方法2: 移除所有对话框包装器的遮罩
        const dialogWrappers = document.querySelectorAll('.el-dialog__wrapper')
        dialogWrappers.forEach((wrapper, index) => {
          const modal = wrapper.querySelector('.v-modal')
          if (modal) {
            console.log(`🗑️ 移除对话框包装器遮罩 ${index + 1}:`, modal)
            modal.remove()
          }
        })

        // 方法3: 强制移除特定类名的遮罩
        const specificMasks = document.querySelectorAll('.merge-result-dialog-wrapper .v-modal, .node-result-dialog-wrapper .v-modal, .node-detail-dialog-wrapper .v-modal')
        specificMasks.forEach((mask, index) => {
          console.log(`🗑️ 移除特定遮罩 ${index + 1}:`, mask)
          mask.remove()
        })

        // 🔥 新增：方法4: 移除所有高z-index的遮罩层
        const highZIndexMasks = document.querySelectorAll('[class*="v-modal"], [style*="z-index"]')
        highZIndexMasks.forEach((element, index) => {
          if (element.classList.contains('v-modal') ||
              (element.style.zIndex && parseInt(element.style.zIndex) > 2000)) {
            console.log(`🗑️ 移除高z-index遮罩 ${index + 1}:`, element)
            element.remove()
          }
        })

        // 方法4: 重置body的overflow样式
        document.body.style.overflow = ''
        document.body.style.paddingRight = ''

        // 方法5: 移除body上的modal-open类
        document.body.classList.remove('el-popup-parent--hidden')

        console.log('✅ 遮罩层清理完成')
      } catch (error) {
        console.error('❌ 清理遮罩层失败:', error)
      }
    },

    // 🔥 新增：重置对话框状态
    resetDialogStates() {
      console.log('🔄 重置所有对话框状态...')

      try {
        // 重置所有对话框的可见性
        this.mergeResultDialogVisible = false
        this.indicatorResultDialogVisible = false
        this.nodeResultDialogVisible = false
        this.nodeDetailDialogVisible = false

        // 清理所有对话框数据
        this.selectedMergeInfo = null
        this.mergeResultData = null
        this.selectedIndicatorInfo = null
        this.nodeResultData = null
        this.selectedNodeInfo = null

        // 重置加载状态
        this.mergeResultLoading = false
        this.nodeResultLoading = false
        this.nodeDetailLoading = false

        console.log('✅ 对话框状态重置完成')
      } catch (error) {
        console.error('❌ 重置对话框状态失败:', error)
      }
    },

    // 🔥 获取指标的成功执行结果
    async getIndicatorSuccessExecutionResult(indicatorInfo) {
      try {
        console.log('📞 查询指标成功执行结果:', indicatorInfo)

        // 🔥 第一步：查询执行成功的历史记录
        const historyResponse = await getExecutionHistory({
          combinationId: indicatorInfo.combinationId,
          status: 'SUCCESS',  // 🔥 只查询成功的执行
          pageNum: 1,
          pageSize: 10
        })

        console.log('📥 成功执行历史响应:', historyResponse)

        if (historyResponse && historyResponse.code === 1) {
          const successExecutions = historyResponse.data?.list || historyResponse.data || []

          if (successExecutions.length === 0) {
            throw new Error('没有找到成功的执行记录')
          }

          // 🔥 第二步：获取最新成功执行的详细结果
          const latestSuccessExecution = successExecutions[0]
          console.log('🎯 最新成功执行:', latestSuccessExecution)

          // 🔥 第三步：查询该执行中指标的具体结果
          const executionId = latestSuccessExecution.EXECUTION_ID || latestSuccessExecution.executionId

          if (!executionId) {
            throw new Error('执行记录中没有执行ID')
          }

          // 🔥 调用执行结果API获取指标的具体执行结果
          const resultResponse = await getExecutionResult(executionId)
          console.log('📊 执行结果响应:', resultResponse)

          if (resultResponse && resultResponse.code === 1) {
            const executionResult = resultResponse.data

            // 🔥 从执行结果中提取当前指标的数据
            const indicatorResult = this.extractIndicatorFromExecutionResult(
              executionResult,
              indicatorInfo
            )

            return {
              execution: latestSuccessExecution,
              result: indicatorResult,
              allExecutions: successExecutions
            }
          } else {
            throw new Error(`获取执行结果失败: ${resultResponse?.msg}`)
          }
        } else {
          throw new Error(`获取执行历史失败: ${historyResponse?.msg}`)
        }
      } catch (error) {
        console.error('❌ 查询指标成功执行结果失败:', error)
        throw error
      }
    },

    // 🔥 从执行结果中提取指标数据
    extractIndicatorFromExecutionResult(executionResult, indicatorInfo) {
      try {
        console.log('🔍 从执行结果中提取指标数据:', { executionResult, indicatorInfo })

        // 方法1：如果执行结果中有指标列表
        if (executionResult.indicators && Array.isArray(executionResult.indicators)) {
          const matchedIndicator = executionResult.indicators.find(indicator => {
            return indicator.indicatorId === indicatorInfo.indicatorId ||
                   indicator.indicatorName === indicatorInfo.indicatorName ||
                   indicator.indicatorCode === indicatorInfo.indicatorCode
          })

          if (matchedIndicator) {
            console.log('✅ 在执行结果中找到匹配的指标:', matchedIndicator)
            return matchedIndicator
          }
        }

        // 方法2：如果执行结果中有结果数据
        if (executionResult.resultData && Array.isArray(executionResult.resultData)) {
          return {
            indicatorId: indicatorInfo.indicatorId,
            indicatorName: indicatorInfo.indicatorName,
            indicatorCode: indicatorInfo.indicatorCode,
            resultData: executionResult.resultData,
            total: executionResult.total || executionResult.resultData.length,
            executionTime: executionResult.executionTime,
            duration: executionResult.duration
          }
        }

        // 方法3：使用整个执行结果作为指标结果
        return {
          indicatorId: indicatorInfo.indicatorId,
          indicatorName: indicatorInfo.indicatorName,
          indicatorCode: indicatorInfo.indicatorCode,
          resultData: executionResult.data || [],
          total: executionResult.total || 0,
          executionTime: executionResult.executionTime,
          duration: executionResult.duration
        }
      } catch (error) {
        console.error('❌ 提取指标数据失败:', error)
        return null
      }
    },

    // 🔥 构建指标结果数据
    buildIndicatorResultData(indicatorInfo, successExecution, indicatorResult, allExecutions) {
      return {
        // 指标基本信息
        indicator: {
          INDICATOR_NAME: indicatorInfo.indicatorName,
          INDICATOR_CODE: indicatorInfo.indicatorCode,
          COMBINATION_NAME: this.combination?.combinationName || '未知组合',
          DESCRIPTION: `${indicatorInfo.indicatorName}的执行结果`
        },

        // 🔥 最近成功执行结果（来自真实的执行记录）
        executionResult: {
          STATUS: 'SUCCESS',
          DURATION: successExecution.DURATION || successExecution.duration || 0,
          EXECUTION_TIME: successExecution.EXECUTION_TIME || successExecution.executionTime,
          RESULT_COUNT: indicatorResult.total || indicatorResult.resultData?.length || 0,
          ERROR_MESSAGE: null
        },

        // 🔥 历史统计信息（基于成功执行的历史）
        historyStats: {
          TOTAL_COUNT: allExecutions.length,
          SUCCESS_COUNT: allExecutions.length, // 都是成功的
          FAILED_COUNT: 0,
          AVG_DURATION: allExecutions.length > 0 ?
            Math.round(allExecutions.reduce((sum, ex) =>
              sum + (ex.DURATION || ex.duration || 0), 0
            ) / allExecutions.length) : 0
        },

        // 🔥 真实的指标执行结果数据
        total: indicatorResult.total || indicatorResult.resultData?.length || 0,
        resultData: indicatorResult.resultData || [],

        // 🔥 额外的指标信息
        indicatorExecutionInfo: {
          executionId: successExecution.EXECUTION_ID || successExecution.executionId,
          executionTime: indicatorResult.executionTime,
          duration: indicatorResult.duration
        },

        // 标记为真实数据
        isRealData: true
      }
    },

    // 🔥 调试方法：直接显示节点结果（仅用于测试）
    directShowNodeResult(nodeId) {
      console.warn('⚠️ 调用了调试方法 directShowNodeResult，这应该只在测试时使用')
      console.log('🔥 直接显示节点结果:', nodeId)

      // 直接调用正常的显示方法，而不是使用假数据
      this.showNodeExecutionResult(nodeId)
    },

    // 🔥 处理API响应数据，获取执行历史中第一个成功的数据
    processApiResponseData(apiData, nodeId) {
      console.log('🔄 处理API响应数据:', apiData)

      try {
        // 基础指标信息
        const indicator = apiData.indicator || {}

        // 查找执行历史中第一个成功的记录
        const executionHistory = apiData.executionHistory || []
        const firstSuccessExecution = executionHistory.find(item =>
          item.STATUS === 'SUCCESS' || item.status === 'SUCCESS' ||
          item.STATUS === 1 || item.status === 1
        )

        console.log('🔍 执行历史:', executionHistory)
        console.log('✅ 第一个成功执行:', firstSuccessExecution)

        // 构建返回数据
        const processedData = {
          // 指标基本信息
          indicator: {
            INDICATOR_NAME: indicator.INDICATOR_NAME || indicator.indicatorName || `指标 ${nodeId}`,
            INDICATOR_CODE: indicator.INDICATOR_CODE || indicator.indicatorCode || nodeId,
            COMBINATION_NAME: indicator.COMBINATION_NAME || indicator.combinationName || this.combination?.combinationName || '未知组合',
            DESCRIPTION: indicator.DESCRIPTION || indicator.description || '暂无描述'
          },

          // 最近执行结果（使用第一个成功的执行记录）
          executionResult: firstSuccessExecution ? {
            STATUS: firstSuccessExecution.STATUS || firstSuccessExecution.status,
            DURATION: firstSuccessExecution.DURATION || firstSuccessExecution.duration || 0,
            ERROR_MESSAGE: firstSuccessExecution.ERROR_MESSAGE || firstSuccessExecution.errorMessage,
            EXECUTION_TIME: firstSuccessExecution.EXECUTION_TIME || firstSuccessExecution.executionTime,
            RESULT_COUNT: firstSuccessExecution.RESULT_COUNT || firstSuccessExecution.resultCount || 0
          } : null,

          // 历史统计信息
          historyStats: apiData.historyStats || {
            TOTAL_COUNT: executionHistory.length,
            SUCCESS_COUNT: executionHistory.filter(item =>
              item.STATUS === 'SUCCESS' || item.status === 'SUCCESS' ||
              item.STATUS === 1 || item.status === 1
            ).length,
            FAILED_COUNT: executionHistory.filter(item =>
              item.STATUS === 'FAILED' || item.status === 'FAILED' ||
              item.STATUS === 0 || item.status === 0
            ).length,
            AVG_DURATION: executionHistory.length > 0 ?
              Math.round(executionHistory.reduce((sum, item) =>
                sum + (item.DURATION || item.duration || 0), 0
              ) / executionHistory.length) : 0
          },

          // 结果数据总数
          total: apiData.total || (firstSuccessExecution ? firstSuccessExecution.RESULT_COUNT || firstSuccessExecution.resultCount || 0 : 0),

          // 具体结果数据（使用第一个成功执行的结果数据）
          resultData: firstSuccessExecution ?
            (firstSuccessExecution.RESULT_DATA || firstSuccessExecution.resultData || []) : [],

          // 是否为预览数据
          isPreview: (apiData.resultData && apiData.resultData.length > 20) || false
        }

        console.log('✅ 处理后的数据:', processedData)
        return processedData

      } catch (error) {
        console.error('❌ 处理API数据失败:', error)

        // 返回基础数据结构，避免组件报错
        return {
          indicator: {
            INDICATOR_NAME: `指标 ${nodeId}`,
            INDICATOR_CODE: nodeId,
            COMBINATION_NAME: this.combination?.combinationName || '未知组合',
            DESCRIPTION: '数据处理失败'
          },
          executionResult: null,
          historyStats: {
            TOTAL_COUNT: 0,
            SUCCESS_COUNT: 0,
            FAILED_COUNT: 0,
            AVG_DURATION: 0
          },
          total: 0,
          resultData: [],
          isPreview: false
        }
      }
    },

    // 获取测试数据（仅用于调试）
    getTestNodeResultData(nodeId) {
      console.warn('⚠️ 使用测试数据，这应该只在调试时出现')
      return {
        indicator: {
          INDICATOR_NAME: `测试指标 ${nodeId}`,
          INDICATOR_CODE: nodeId,
          COMBINATION_NAME: '测试组合',
          DESCRIPTION: '这是测试数据，请检查API调用'
        },
        executionResult: {
          STATUS: 'SUCCESS',
          DURATION: 1000
        },
        historyStats: {
          TOTAL_COUNT: 10,
          SUCCESS_COUNT: 8,
          FAILED_COUNT: 2,
          AVG_DURATION: 1200
        },
        total: 15,
        resultData: [
          { id: 1, name: '测试数据1', value: 100, status: '正常' },
          { id: 2, name: '测试数据2', value: 200, status: '异常' }
        ],
        isPreview: true
      }
    },

    // 强制显示Element UI对话框
    forceShowElementUIDialog() {
      const wrappers = document.querySelectorAll('.el-dialog__wrapper')
      console.log('找到Element UI包装器:', wrappers.length)

      wrappers.forEach((wrapper, index) => {
        console.log(`强制显示包装器 ${index + 1}`)
        wrapper.style.setProperty('z-index', '999999', 'important')
        wrapper.style.setProperty('display', 'flex', 'important')
        wrapper.style.setProperty('position', 'fixed', 'important')
        wrapper.style.setProperty('top', '0', 'important')
        wrapper.style.setProperty('left', '0', 'important')
        wrapper.style.setProperty('width', '100%', 'important')
        wrapper.style.setProperty('height', '100%', 'important')
        wrapper.style.setProperty('background', 'rgba(0,0,0,0.5)', 'important')
        wrapper.style.setProperty('align-items', 'center', 'important')
        wrapper.style.setProperty('justify-content', 'center', 'important')

        const dialog = wrapper.querySelector('.el-dialog')
        if (dialog) {
          dialog.style.setProperty('background', 'white', 'important')
          dialog.style.setProperty('border-radius', '8px', 'important')
          dialog.style.setProperty('box-shadow', '0 4px 12px rgba(0,0,0,0.3)', 'important')
          dialog.style.setProperty('position', 'relative', 'important')
          dialog.style.setProperty('z-index', '1000000', 'important')
        }
      })
    },

    // 显示节点详情
    async showNodeDetail(nodeId) {
      try {
        console.log('ℹ️ 开始显示节点详情:', nodeId)

        // 先显示对话框
        this.nodeDetailLoading = true
        this.nodeDetailDialogVisible = true

        console.log('ℹ️ 对话框状态设置完成:', {
          nodeDetailLoading: this.nodeDetailLoading,
          nodeDetailDialogVisible: this.nodeDetailDialogVisible
        })

        // 确保对话框在最上层
        this.$nextTick(() => {
          console.log('ℹ️ 确保对话框在最上层')
          this.ensureDialogOnTop('node-detail-dialog-wrapper')
        })

        console.log('ℹ️ 准备调用API获取节点详情:', nodeId)

        const params = {
          combinationId: this.combination?.combinationId
        }

        console.log('ℹ️ API调用参数:', params)

        const response = await getNodeDetail(nodeId, params)
        console.log('ℹ️ API响应:', response)

        if (response && response.code === 1) {
          this.nodeDetailData = response.data
          console.log('✅ 节点详情获取成功:', this.nodeDetailData)
        } else {
          const errorMsg = response?.msg || '获取节点详情失败'
          console.error('❌ API返回失败:', response)
          throw new Error(errorMsg)
        }
      } catch (error) {
        console.error('❌ 获取节点详情失败:', error)
        this.$message.error(`获取节点详情失败: ${error.message}`)
        this.nodeDetailDialogVisible = false
        this.nodeDetailData = null
      } finally {
        this.nodeDetailLoading = false
        console.log('ℹ️ 节点详情获取流程完成')
      }
    },

    // 获取最新的执行ID
    getLatestExecutionId() {
      // 这里可以从组合数据中获取最新的执行ID
      // 暂时返回null，让后端返回最近的执行结果
      return null
    },

    // 处理节点结果对话框关闭
    handleNodeResultDialogClose() {
      console.log('📊 节点结果对话框关闭')
      this.nodeResultDialogVisible = false
      this.nodeResultData = null
      this.nodeResultLoading = false

      // 🔥 修复：清理遮罩层，解决黑色遮罩层问题
      this.$nextTick(() => {
        // 立即清理一次
        this.clearCurrentDialogMasks()
        this.ensureCurrentDialogInteractable()

        // 延迟再清理一次，确保对话框关闭动画完成
        setTimeout(() => {
          this.clearCurrentDialogMasks()
          this.ensureCurrentDialogInteractable()

          // 强制更新组件状态
          this.$forceUpdate()

          console.log('✅ 节点结果对话框关闭完成，遮罩层已清理')
        }, 300)
      })
    },

    // 处理节点详情对话框关闭
    handleNodeDetailDialogClose() {
      console.log('ℹ️ 节点详情对话框关闭')

      // 🔥 强化关闭逻辑
      this.nodeDetailDialogVisible = false
      this.nodeDetailData = null
      this.nodeDetailLoading = false

      // 强制更新
      this.$forceUpdate()

      // 🔥 立即清理DOM中的节点详情对话框
      this.$nextTick(() => {
        const detailWrappers = document.querySelectorAll('.node-detail-dialog-wrapper')
        detailWrappers.forEach((wrapper, index) => {
          console.log(`强制移除节点详情对话框 ${index + 1}`)
          wrapper.style.display = 'none'
          if (wrapper.parentNode) {
            wrapper.parentNode.removeChild(wrapper)
          }
        })

        // 清理可能的遮罩层
        const modals = document.querySelectorAll('.v-modal')
        modals.forEach(modal => {
          if (modal.style.zIndex === '9399' || modal.style.zIndex === '9400') {
            if (modal.parentNode) {
              modal.parentNode.removeChild(modal)
            }
          }
        })
      })

      console.log('✅ 节点详情对话框关闭完成')
    },

    // 调试方法：强制显示节点结果对话框
    debugShowNodeResult(nodeId = 'test-node') {
      console.log('🐛 调试：强制显示节点结果对话框')
      this.nodeResultData = {
        indicator: {
          INDICATOR_NAME: '测试指标',
          INDICATOR_CODE: 'TEST_001',
          COMBINATION_NAME: '测试组合'
        },
        executionResult: {
          STATUS: 'SUCCESS',
          DURATION: 1000
        },
        total: 10,
        resultData: [
          { id: 1, name: '测试数据1', value: 100 },
          { id: 2, name: '测试数据2', value: 200 }
        ]
      }
      this.nodeResultLoading = false
      this.nodeResultDialogVisible = true

      this.$nextTick(() => {
        console.log('🐛 调试：对话框状态已更新')
        console.log('🐛 nodeResultDialogVisible:', this.nodeResultDialogVisible)
        console.log('🐛 nodeResultData:', this.nodeResultData)

        // 强制设置对话框层级
        this.forceShowDialog()
      })
    },

    // 强制显示对话框
    forceShowDialog() {
      console.log('💪 强制显示对话框')

      setTimeout(() => {
        // 查找所有可能的对话框包装器
        const wrappers = document.querySelectorAll('.el-dialog__wrapper')
        console.log('找到对话框包装器数量:', wrappers.length)

        wrappers.forEach((wrapper, index) => {
          const zIndex = 9500 + index
          wrapper.style.setProperty('z-index', zIndex.toString(), 'important')
          wrapper.style.setProperty('display', 'flex', 'important')
          wrapper.style.setProperty('position', 'fixed', 'important')
          wrapper.style.setProperty('top', '0', 'important')
          wrapper.style.setProperty('left', '0', 'important')
          wrapper.style.setProperty('width', '100%', 'important')
          wrapper.style.setProperty('height', '100%', 'important')
          wrapper.style.setProperty('align-items', 'center', 'important')
          wrapper.style.setProperty('justify-content', 'center', 'important')

          const dialog = wrapper.querySelector('.el-dialog')
          if (dialog) {
            dialog.style.setProperty('z-index', (zIndex + 1).toString(), 'important')
            dialog.style.setProperty('position', 'relative', 'important')
            dialog.style.setProperty('background', 'white', 'important')
            dialog.style.setProperty('border-radius', '4px', 'important')
            dialog.style.setProperty('box-shadow', '0 1px 3px rgba(0,0,0,.3)', 'important')
          }

          console.log(`💪 强制设置对话框${index + 1} z-index:`, zIndex)
        })

        // 移除可能阻挡的遮罩
        const modals = document.querySelectorAll('.v-modal')
        modals.forEach(modal => {
          modal.style.setProperty('z-index', '9499', 'important')
        })

      }, 100)
    },

    // 🔥 终极强制显示方法
    ultimateForceShow() {
      console.log('🔥 执行终极强制显示')

      // 方法1: 直接在body中创建对话框
      this.createDirectDialog()

      // 方法2: 强制修改现有对话框
      setTimeout(() => {
        this.forceModifyExistingDialog()
      }, 50)

      // 方法3: 移除所有可能的遮挡
      setTimeout(() => {
        this.removeAllBlockers()
      }, 100)
    },

    // 创建直接对话框（绕过Vue组件系统）
    createDirectDialog() {
      console.log('🔥 创建直接对话框')

      // 移除已存在的直接对话框
      const existing = document.getElementById('ultimate-node-result-dialog')
      if (existing) {
        existing.remove()
      }

      const dialogHtml = `
        <div id="ultimate-node-result-dialog" style="
          position: fixed !important;
          top: 0 !important;
          left: 0 !important;
          width: 100vw !important;
          height: 100vh !important;
          z-index: 999999 !important;
          background: rgba(0,0,0,0.5) !important;
          display: flex !important;
          align-items: center !important;
          justify-content: center !important;
          pointer-events: all !important;
        ">
          <div style="
            width: 80% !important;
            max-width: 1000px !important;
            max-height: 80vh !important;
            background: white !important;
            border-radius: 8px !important;
            box-shadow: 0 4px 12px rgba(0,0,0,0.3) !important;
            overflow: hidden !important;
            position: relative !important;
            z-index: 1000000 !important;
          ">
            <div style="
              padding: 20px !important;
              border-bottom: 1px solid #eee !important;
              background: #f8f9fa !important;
              display: flex !important;
              justify-content: space-between !important;
              align-items: center !important;
            ">
              <h3 style="margin: 0 !important; color: #333 !important;">🔥 终极测试对话框</h3>
              <button onclick="document.getElementById('ultimate-node-result-dialog').remove()" style="
                background: #f56c6c !important;
                color: white !important;
                border: none !important;
                padding: 8px 12px !important;
                border-radius: 4px !important;
                cursor: pointer !important;
              ">关闭</button>
            </div>
            <div style="
              padding: 20px !important;
              max-height: 60vh !important;
              overflow-y: auto !important;
            ">
              <div style="margin-bottom: 20px !important;">
                <h4 style="color: #67c23a !important;">🎉 恭喜！如果你能看到这个对话框，说明显示机制是正常的！</h4>
                <p>这个对话框使用了最高的z-index (999999) 和强制CSS属性。</p>
              </div>

              <div style="margin-bottom: 20px !important; padding: 15px !important; background: #f0f9ff !important; border-radius: 4px !important;">
                <h5 style="margin-top: 0 !important; color: #409eff !important;">诊断信息：</h5>
                <p><strong>当前时间:</strong> ${new Date().toLocaleString()}</p>
                <p><strong>对话框状态:</strong> nodeResultDialogVisible = ${this.nodeResultDialogVisible}</p>
                <p><strong>数据状态:</strong> nodeResultData = ${!!this.nodeResultData}</p>
                <p><strong>加载状态:</strong> nodeResultLoading = ${this.nodeResultLoading}</p>
              </div>

              <div style="margin-bottom: 20px !important;">
                <button onclick="window.debugNodeClick && window.debugNodeClick.checkDialogDOM()" style="
                  background: #409eff !important;
                  color: white !important;
                  border: none !important;
                  padding: 10px 16px !important;
                  border-radius: 4px !important;
                  cursor: pointer !important;
                  margin-right: 10px !important;
                ">检查DOM状态</button>

                <button onclick="console.log('Vue组件实例:', this)" style="
                  background: #67c23a !important;
                  color: white !important;
                  border: none !important;
                  padding: 10px 16px !important;
                  border-radius: 4px !important;
                  cursor: pointer !important;
                ">查看组件状态</button>
              </div>

              <div style="background: #fff2e8 !important; padding: 15px !important; border-radius: 4px !important;">
                <h5 style="margin-top: 0 !important; color: #e6a23c !important;">下一步调试：</h5>
                <ol>
                  <li>如果能看到这个对话框，说明浏览器渲染正常</li>
                  <li>检查Vue组件的nodeResultDialogVisible状态</li>
                  <li>检查Element UI的对话框是否被正确创建</li>
                  <li>查看是否有CSS规则覆盖了对话框样式</li>
                </ol>
              </div>
            </div>
          </div>
        </div>
      `

      document.body.insertAdjacentHTML('beforeend', dialogHtml)
      console.log('🔥 直接对话框已创建')
    },

    // 强制修改现有对话框
    forceModifyExistingDialog() {
      console.log('🔥 强制修改现有对话框')

      // 查找所有可能的对话框
      const selectors = [
        '.node-result-dialog-wrapper',
        '.el-dialog__wrapper',
        '[class*="node-result"]',
        '[class*="dialog"]'
      ]

      selectors.forEach(selector => {
        const elements = document.querySelectorAll(selector)
        elements.forEach((el, index) => {
          console.log(`🔥 修改元素 ${selector}[${index}]:`, el)

          // 强制设置样式
          el.style.setProperty('position', 'fixed', 'important')
          el.style.setProperty('top', '0', 'important')
          el.style.setProperty('left', '0', 'important')
          el.style.setProperty('width', '100vw', 'important')
          el.style.setProperty('height', '100vh', 'important')
          el.style.setProperty('z-index', '999998', 'important')
          el.style.setProperty('display', 'flex', 'important')
          el.style.setProperty('align-items', 'center', 'important')
          el.style.setProperty('justify-content', 'center', 'important')
          el.style.setProperty('background', 'rgba(0,0,0,0.5)', 'important')
          el.style.setProperty('pointer-events', 'all', 'important')

          // 修改内部对话框
          const dialog = el.querySelector('.el-dialog')
          if (dialog) {
            dialog.style.setProperty('background', 'white', 'important')
            dialog.style.setProperty('border-radius', '8px', 'important')
            dialog.style.setProperty('box-shadow', '0 4px 12px rgba(0,0,0,0.3)', 'important')
            dialog.style.setProperty('z-index', '999999', 'important')
            dialog.style.setProperty('position', 'relative', 'important')
          }
        })
      })
    },

    // 移除所有可能的遮挡元素
    removeAllBlockers() {
      console.log('🔥 移除所有可能的遮挡元素')

      // 移除可能遮挡的元素
      const blockerSelectors = [
        '.v-modal[style*="z-index: 999"]',
        '.el-loading-mask',
        '.el-overlay',
        '[style*="z-index"][style*="999"]'
      ]

      blockerSelectors.forEach(selector => {
        const elements = document.querySelectorAll(selector)
        elements.forEach(el => {
          console.log('🔥 移除遮挡元素:', el)
          el.style.setProperty('z-index', '1', 'important')
          el.style.setProperty('display', 'none', 'important')
        })
      })

      // 重置body样式
      document.body.style.removeProperty('overflow')
      document.body.style.removeProperty('padding-right')
    },

    // 创建终极测试对话框
    createUltimateTestDialog() {
      console.log('🔥 创建终极测试对话框')

      // 立即创建一个绝对可见的对话框
      setTimeout(() => {
        this.createDirectDialog()
      }, 10)
    },



    // 获取状态类型
    getStatusType(status) {
      const statusMap = {
        'SUCCESS': 'success',
        'FAILED': 'danger',
        'RUNNING': 'warning',
        'PENDING': 'info'
      }
      return statusMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const statusMap = {
        'SUCCESS': '成功',
        'FAILED': '失败',
        'RUNNING': '运行中',
        'PENDING': '等待中'
      }
      return statusMap[status] || status
    },

    // 获取节点类型文本
    getNodeTypeText(nodeType) {
      const typeMap = {
        'start': '开始节点',
        'end': '结束节点',
        'merge': '汇聚节点',
        'indicator': '指标节点',
        'unknown': '未知节点'
      }
      return typeMap[nodeType] || nodeType
    },

    // 格式化日期
    formatDate(date) {
      if (!date) return '-'
      try {
        return new Date(date).toLocaleString('zh-CN')
      } catch (error) {
        return date.toString()
      }
    },



    // 确保对话框在最上层
    ensureDialogOnTop(dialogClass) {
      try {
        console.log('🔝 确保对话框在最上层:', dialogClass)

        // 多次尝试，确保DOM渲染完成
        let attempts = 0
        const maxAttempts = 10

        const trySetZIndex = () => {
          attempts++

          // 查找对话框元素 - 多种方式查找
          let dialogWrapper = document.querySelector(`.${dialogClass}`) ||
                             document.querySelector(`[class*="${dialogClass}"]`) ||
                             document.querySelector('.el-dialog__wrapper:last-child')

          if (dialogWrapper) {
            console.log('✅ 找到对话框包装器:', dialogWrapper)

            // 获取当前最高的z-index
            const currentMaxZIndex = Math.max(
              ...Array.from(document.querySelectorAll('*')).map(el =>
                parseInt(getComputedStyle(el).zIndex) || 0
              )
            )
            const baseZIndex = Math.max(currentMaxZIndex + 100, 9000)

            console.log('🔝 当前最高z-index:', currentMaxZIndex, '设置基础z-index:', baseZIndex)

            // 设置包装器的z-index
            dialogWrapper.style.setProperty('z-index', baseZIndex.toString(), 'important')

            // 查找对话框本体
            const dialog = dialogWrapper.querySelector('.el-dialog')
            if (dialog) {
              dialog.style.setProperty('z-index', (baseZIndex + 1).toString(), 'important')
              console.log('✅ 设置对话框z-index成功:', baseZIndex + 1)
            }

            // 查找遮罩层 - 多种方式查找
            let modal = dialogWrapper.querySelector('.v-modal') ||
                       dialogWrapper.previousElementSibling ||
                       document.querySelector('.v-modal:last-child')

            if (modal && modal.classList.contains('v-modal')) {
              modal.style.setProperty('z-index', (baseZIndex - 1).toString(), 'important')
              console.log('✅ 设置遮罩层z-index成功:', baseZIndex - 1)
            }

            // 确保对话框包装器在body的最后
            if (dialogWrapper.parentNode === document.body) {
              document.body.appendChild(dialogWrapper)
              console.log('✅ 移动对话框到body最后')
            }

            return true // 成功
          } else if (attempts < maxAttempts) {
            console.log(`⏳ 第${attempts}次尝试未找到对话框，继续尝试...`)
            setTimeout(trySetZIndex, 50)
            return false
          } else {
            console.warn('⚠️ 达到最大尝试次数，未找到对话框包装器:', dialogClass)

            // 最后的备用方案：设置所有对话框的z-index
            const allWrappers = document.querySelectorAll('.el-dialog__wrapper')
            if (allWrappers.length > 0) {
              const baseZIndex = 9000
              allWrappers.forEach((wrapper, index) => {
                const zIndex = baseZIndex + (index * 10)
                wrapper.style.setProperty('z-index', zIndex.toString(), 'important')
                const dialog = wrapper.querySelector('.el-dialog')
                if (dialog) {
                  dialog.style.setProperty('z-index', (zIndex + 1).toString(), 'important')
                }
                console.log(`✅ 设置对话框${index + 1} z-index:`, zIndex)
              })
              console.log('✅ 使用备用方案设置所有对话框')
            }
            return true
          }
        }

        // 立即尝试一次
        trySetZIndex()

      } catch (error) {
        console.error('❌ 设置对话框层级失败:', error)
      }
    },

    // 清理遮罩层
    clearModalMask() {
      try {
        console.log('🧹 Starting modal mask cleanup...')

        // 移除所有可能的遮罩层
        const selectors = [
          '.v-modal',
          '.el-overlay',
          '.el-dialog__wrapper .el-overlay',
          '.flow-view-dialog + .v-modal'
        ]

        selectors.forEach(selector => {
          const elements = document.querySelectorAll(selector)
          elements.forEach(element => {
            if (element && element.parentNode) {
              console.log(`🗑️ Removing element: ${selector}`)
              element.parentNode.removeChild(element)
            }
          })
        })

        // 移除body上的相关类
        const bodyClasses = [
          'el-popup-parent--hidden',
          'el-dialog-open',
          'modal-open'
        ]

        bodyClasses.forEach(className => {
          if (document.body.classList.contains(className)) {
            console.log(`🗑️ Removing body class: ${className}`)
            document.body.classList.remove(className)
          }
        })

        // 恢复body的样式
        const bodyStyles = ['overflow', 'paddingRight', 'marginRight']
        bodyStyles.forEach(style => {
          if (document.body.style[style]) {
            console.log(`🔄 Resetting body style: ${style}`)
            document.body.style[style] = ''
          }
        })

        // 强制重新计算布局
        document.body.offsetHeight

        console.log('✅ Modal mask cleared successfully')
      } catch (error) {
        console.error('❌ Error clearing modal mask:', error)
      }
    }
  }
}
</script>

<style scoped>
.flow-view-dialog {
  .flow-container {
    height: 80vh;
    display: flex;
    flex-direction: column;
  }

  .toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 0;
    border-bottom: 1px solid #ebeef5;
    margin-bottom: 16px;

    .toolbar-left,
    .toolbar-right {
      display: flex;
      gap: 8px;
    }
  }

  .flow-display {
    flex: 1;
    display: flex;
    min-height: 0;
    width: 100%;

    .loading-state {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .flow-content {
      flex: 1;
      display: flex;
      width: 100%;

      .mermaid-container {
        flex: 1;
        width: 100%;
        border: 1px solid #ebeef5;
        border-radius: 4px;
        overflow: auto;
        background-color: #fff;
        position: relative;
        transform-origin: center center;
        transition: transform 0.3s ease;
        min-height: 600px;

        .mermaid-graph {
          width: 100%;
          height: 100%;
          display: flex;
          align-items: center;
          justify-content: center;
          padding: 20px;
          min-height: 600px;
        }
      }
    }

    .empty-state {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      color: #909399;

      i {
        font-size: 48px;
        margin-bottom: 16px;
      }
    }
  }


}

/* 节点结果对话框样式 */
.node-result-dialog-wrapper {
  z-index: 3000 !important;
}

.node-result-dialog-wrapper .el-dialog {
  z-index: 3001 !important;
}

.node-result-dialog {
  .node-result-container {
    .result-content {
      .indicator-info {
        background: #f8f9fa;
        padding: 16px;
        border-radius: 8px;
        margin-bottom: 20px;

        h3 {
          margin: 0 0 12px 0;
          color: #303133;
          font-size: 18px;
        }

        .info-row {
          display: flex;
          margin-bottom: 8px;

          .label {
            width: 100px;
            color: #606266;
            font-weight: 500;
          }

          .value {
            flex: 1;
            color: #303133;

            &.error {
              color: #f56c6c;
            }
          }
        }
      }

      .execution-stats {
        margin-bottom: 20px;

        h4 {
          margin: 0 0 12px 0;
          color: #303133;
        }

        .stats-grid {
          display: grid;
          grid-template-columns: repeat(4, 1fr);
          gap: 16px;

          .stat-item {
            text-align: center;
            padding: 16px;
            background: #fff;
            border: 1px solid #ebeef5;
            border-radius: 8px;

            .stat-value {
              font-size: 24px;
              font-weight: bold;
              margin-bottom: 4px;

              &.success {
                color: #67c23a;
              }

              &.failed {
                color: #f56c6c;
              }
            }

            .stat-label {
              font-size: 12px;
              color: #909399;
            }
          }
        }
      }

      .execution-result {
        margin-bottom: 20px;

        h4 {
          margin: 0 0 12px 0;
          color: #303133;
        }

        .result-info {
          background: #f8f9fa;
          padding: 16px;
          border-radius: 8px;

          .info-row {
            display: flex;
            margin-bottom: 8px;

            .label {
              width: 100px;
              color: #606266;
              font-weight: 500;
            }

            .value {
              flex: 1;
              color: #303133;

              &.error {
                color: #f56c6c;
              }
            }
          }
        }
      }

      .result-data {
        h4 {
          margin: 0 0 12px 0;
          color: #303133;

          .preview-tip {
            font-size: 12px;
            color: #909399;
            font-weight: normal;
          }
        }
      }

      .no-data {
        text-align: center;
        padding: 40px;
        color: #909399;

        i {
          font-size: 48px;
          margin-bottom: 16px;
        }
      }
    }
  }
}

/* 节点详情对话框样式 */
.node-detail-dialog-wrapper {
  z-index: 3000 !important;
}

.node-detail-dialog-wrapper .el-dialog {
  z-index: 3001 !important;
}

.node-detail-dialog {
  .node-detail-container {
    .detail-content {
      .node-header {
        display: flex;
        align-items: center;
        margin-bottom: 20px;
        padding-bottom: 16px;
        border-bottom: 1px solid #ebeef5;

        .node-icon {
          font-size: 32px;
          margin-right: 12px;
        }

        h3 {
          flex: 1;
          margin: 0;
          color: #303133;
        }
      }

      .detail-info {
        .info-row {
          display: flex;
          margin-bottom: 12px;

          .label {
            width: 120px;
            color: #606266;
            font-weight: 500;
          }

          .value {
            flex: 1;
            color: #303133;
          }
        }

        .last-execution {
          margin-top: 20px;
          padding-top: 16px;
          border-top: 1px solid #ebeef5;

          h4 {
            margin: 0 0 12px 0;
            color: #303133;
            font-size: 14px;
          }
        }
      }
    }
  }
}
</style>

<style>
/* 全局样式 - 确保节点对话框在最上层 */
.node-result-dialog-wrapper,
.node-detail-dialog-wrapper {
  z-index: 9500 !important;
}

.node-result-dialog-wrapper .el-dialog,
.node-detail-dialog-wrapper .el-dialog {
  z-index: 9501 !important;
}

.node-result-dialog-wrapper .el-dialog__wrapper,
.node-detail-dialog-wrapper .el-dialog__wrapper {
  z-index: 9500 !important;
}

/* 强制显示对话框 */
.node-result-dialog-wrapper.el-dialog__wrapper,
.node-detail-dialog-wrapper.el-dialog__wrapper {
  display: flex !important;
  align-items: center;
  justify-content: center;
  position: fixed !important;
  top: 0 !important;
  left: 0 !important;
  width: 100% !important;
  height: 100% !important;
  z-index: 9500 !important;
}

/* 确保对话框内容可见 */
.node-result-dialog-wrapper .el-dialog,
.node-detail-dialog-wrapper .el-dialog {
  position: relative !important;
  margin: 0 auto !important;
  background: white !important;
  border-radius: 4px !important;
  box-shadow: 0 1px 3px rgba(0,0,0,.3) !important;
  z-index: 9501 !important;
}

.node-result-dialog-wrapper .el-dialog,
.node-detail-dialog-wrapper .el-dialog {
  z-index: 3001 !important;
}

.node-result-dialog-wrapper .v-modal,
.node-detail-dialog-wrapper .v-modal {
  z-index: 2999 !important;
}

/* 确保遮罩层在正确位置 */
.el-dialog__wrapper.node-result-dialog-wrapper,
.el-dialog__wrapper.node-detail-dialog-wrapper {
  z-index: 3000 !important;
}

.el-dialog__wrapper.node-result-dialog-wrapper .v-modal,
.el-dialog__wrapper.node-detail-dialog-wrapper .v-modal {
  z-index: 2999 !important;
}

/* 🔥 新增：汇聚结果对话框样式 */
.merge-result-dialog-wrapper {
  z-index: 3600 !important;
}

.merge-result-dialog-wrapper .el-dialog {
  z-index: 3601 !important;
}

.merge-result-content .merge-info-section .info-card .info-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
  margin-bottom: 16px;
}

.merge-result-content .merge-info-section .info-card .info-grid .info-item {
  display: flex;
  align-items: center;
}

.merge-result-content .merge-info-section .info-card .info-grid .info-item label {
  font-weight: 500;
  color: #606266;
  margin-right: 8px;
  min-width: 100px;
}

.merge-result-content .merge-info-section .info-card .info-grid .info-item span.parallel-mode {
  color: #67c23a;
  font-weight: 600;
}

.merge-result-content .merge-info-section .info-card .info-grid .info-item span.highlight {
  color: #e6a23c;
  font-weight: 600;
  font-size: 16px;
}

.merge-result-content .merge-info-section .info-card .indicator-list {
  padding-top: 12px;
  border-top: 1px solid #ebeef5;
}

.merge-result-content .merge-info-section .info-card .indicator-list label {
  display: block;
  font-weight: 500;
  color: #606266;
  margin-bottom: 8px;
}

.merge-result-content .merge-info-section .info-card .indicator-list .indicator-tag {
  margin-right: 8px;
  margin-bottom: 4px;
}

.merge-result-content .merge-data-section .data-card .card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
  color: #303133;
}

.merge-result-content .merge-data-section .data-card .card-header .result-count {
  color: #909399;
  font-size: 14px;
  font-weight: normal;
  margin-left: 8px;
}

.merge-result-content .merge-data-section .data-card .card-header .header-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.merge-result-content .merge-data-section .data-card .pagination-container {
  display: flex;
  justify-content: center;
  padding: 20px 0;
  border-top: 1px solid #ebeef5;
  margin-top: 16px;
}

.merge-result-content .merge-data-section .data-card .no-merge-data {
  text-align: center;
  padding: 40px 20px;
  color: #909399;
}

.merge-result-content .merge-data-section .data-card .no-merge-data i {
  font-size: 48px;
  margin-bottom: 16px;
  display: block;
}

.merge-result-content .merge-data-section .data-card .no-merge-data p {
  margin: 0;
  font-size: 16px;
}

/* 🔥 新增：防止遮罩层问题的样式 */
.merge-result-dialog-wrapper .v-modal {
  z-index: 3599 !important;
}

.merge-result-dialog-wrapper .el-dialog {
  z-index: 3601 !important;
}

/* 确保对话框关闭时遮罩层被正确移除 */
.v-modal[style*="display: none"] {
  display: none !important;
  pointer-events: none !important;
}

/* 防止遮罩层阻止页面交互 */
body.el-popup-parent--hidden {
  overflow: hidden !important;
}

/* 当没有对话框时，确保body可以正常滚动 */
body:not(.el-popup-parent--hidden) {
  overflow: auto !important;
  pointer-events: auto !important;
}
</style>
