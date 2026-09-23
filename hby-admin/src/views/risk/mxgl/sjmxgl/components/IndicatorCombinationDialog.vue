<template>
  <el-dialog
    title="指标组合分析"
    :visible.sync="dialogVisible"
    width="95%"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :modal="true"
    :modal-append-to-body="true"
    :append-to-body="true"
    :lock-scroll="true"
    :z-index="2500"
    custom-class="indicator-combination-dialog risk-mxgl-sjmxgl-page"
    @close="handleUserClose"
    :before-close="handleBeforeClose"
  >
    <div class="combination-container risk-mxgl-sjmxgl-page">
      <!-- 顶部工具栏 -->
      <div class="toolbar">
        <el-button type="primary" icon="el-icon-plus" @click="handleCreateCombination">
          新建组合
        </el-button>
        <el-button icon="el-icon-refresh" @click="loadCombinationList">
          刷新
        </el-button>
      </div>

      <!-- 筛选表单 -->
      <div class="filter-form">
        <el-form :inline="true" size="small">
          <el-form-item label="组合代码">
            <el-input
              v-model="filterForm.combinationCode"
              placeholder="请输入组合代码"
              clearable
              style="width: 180px"
              @keyup.enter.native="handleSearch"
            />
          </el-form-item>
          <el-form-item label="组合名称">
            <el-input
              v-model="filterForm.combinationName"
              placeholder="请输入组合名称"
              clearable
              style="width: 180px"
              @keyup.enter.native="handleSearch"
            />
          </el-form-item>
          <el-form-item label="领域分类">
            <el-select
              v-model="filterForm.category"
              placeholder="请选择领域分类"
              clearable
              style="width: 180px"
            >
              <el-option label="投资穿透" value="INVESTMENT_PENETRATION" />
              <el-option label="产权穿透" value="PROPERTY_PENETRATION" />
              <el-option label="财务穿透" value="FINANCIAL_PENETRATION" />
              <el-option label="金融风险穿透" value="FINANCIAL_RISK_PENETRATION" />
              <el-option label="会计穿透" value="ACCOUNTING_PENETRATION" />
              <el-option label="薪酬分配" value="SALARY_DISTRIBUTION" />
              <el-option label="军品穿透" value="MILITARY_PENETRATION" />
              <el-option label="采购与供应链" value="PROCUREMENT_SUPPLY_CHAIN" />
              <el-option label="境外穿透" value="OVERSEAS_PENETRATION" />
              <el-option label="合同穿透" value="CONTRACT_PENETRATION" />
              <el-option label="行业穿透" value="INDUSTRY_PENETRATION" />
              <el-option label="资金穿透" value="FUND_PENETRATION" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select
              v-model="filterForm.status"
              placeholder="请选择状态"
              clearable
              style="width: 120px"
            >
              <el-option label="草稿" value="DRAFT" />
              <el-option label="启用" value="ACTIVE" />
              <el-option label="禁用" value="INACTIVE" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleSearch">
              查询
            </el-button>
            <el-button icon="el-icon-refresh-left" @click="handleResetFilter">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 主要内容区域 -->
      <div class="main-content">
        <!-- 左侧：组合列表 -->
        <div class="combination-list">
          <div class="list-header">
            <h3>指标组合列表</h3>
            <el-tag type="info">共 {{ pagination.total }} 个组合</el-tag>
          </div>
          
          <div class="list-content">
            <div
              v-for="combination in combinationList"
              :key="combination.combinationId"
              :class="['combination-item', { active: selectedCombination?.combinationId === combination.combinationId }]"
              @click="selectCombination(combination)"
            >
              <div class="item-header">
                <h4>{{ combination.combinationName }}</h4>
                <el-tag :type="getStatusType(combination.status)" size="mini">
                  {{ getStatusText(combination.status) }}
                </el-tag>
              </div>
              <div class="item-content">
                <p class="description">{{ combination.description || '暂无描述' }}</p>
                <div class="item-meta">
                  <span class="meta-item">
                    <i class="el-icon-collection"></i>
                    {{ combination.indicatorCount || 0 }} 个指标
                  </span>
                  <span class="meta-item">
                    <i class="el-icon-time"></i>
                    {{ combination.createTime }}
                  </span>
                </div>
              </div>
              <div class="item-actions">
                <el-button type="text" size="mini" @click.stop="handleEditCombination(combination)">
                  编辑
                </el-button>
                <el-button type="text" size="mini" @click.stop="handleExecuteCombination(combination)">
                  执行
                </el-button>
                <el-button type="text" size="mini" @click.stop="handleViewFlow(combination)">
                  流程图
                </el-button>
                <el-dropdown @command="handleMoreAction" trigger="click" @click.native.stop>
                  <el-button type="text" size="mini">
                    更多<i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item :command="{action: 'copy', data: combination}">复制</el-dropdown-item>
                    <el-dropdown-item :command="{action: 'history', data: combination}">执行历史</el-dropdown-item>
                    <el-dropdown-item :command="{action: 'modelDoc', data: combination}">模型文档</el-dropdown-item>
                    <el-dropdown-item :command="{action: 'delete', data: combination}" divided>删除</el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </div>
            </div>
          </div>

          <!-- 分页组件 -->
          <div class="list-pagination">
            <el-pagination
              small
              @size-change="handleCombinationSizeChange"
              @current-change="handleCombinationPageChange"
              :current-page="pagination.pageNum"
              :page-sizes="[5, 10, 20, 50]"
              :page-size="pagination.pageSize"
              layout="total, sizes, prev, pager, next"
              :total="pagination.total"
            />
          </div>
        </div>

        <!-- 右侧：详情面板 -->
        <div class="detail-panel">
          <div v-if="!selectedCombination" class="empty-state">
            <i class="el-icon-info"></i>
            <p>请选择一个指标组合查看详情</p>
          </div>
          
          <div v-else class="combination-detail">
            <!-- 基本信息 -->
            <div class="detail-section">
              <h3>基本信息</h3>
              <el-descriptions :column="2" border>
                <el-descriptions-item label="组合名称">
                  {{ selectedCombination.combinationName }}
                </el-descriptions-item>
                <el-descriptions-item label="组合编码">
                  {{ selectedCombination.combinationCode }}
                </el-descriptions-item>
                <el-descriptions-item label="领域分类">
                  {{ getCategoryText(selectedCombination.category) }}
                </el-descriptions-item>
                <el-descriptions-item label="执行模式">
                  <el-tag :type="selectedCombination.executionMode === 'SEQUENCE' ? 'primary' : 'success'">
                    {{ selectedCombination.executionMode === 'SEQUENCE' ? '顺序执行' : '并行执行' }}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="状态">
                  <el-tag :type="getStatusType(selectedCombination.status)">
                    {{ getStatusText(selectedCombination.status) }}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="创建时间">
                  {{ selectedCombination.createTime }}
                </el-descriptions-item>
              </el-descriptions>
            </div>

            <!-- 指标配置 -->
            <div class="detail-section">
              <div class="section-header">
                <h3>指标配置</h3>
                <div class="header-actions">
                  <el-button type="success" size="mini" @click="executeIndicatorCombination(selectedCombination)" :loading="indicatorSqlLoading">
                    <i class="el-icon-video-play"></i> 执行整个组合
                  </el-button>
                  <el-button type="primary" size="mini" @click="handleConfigureIndicators">
                    配置指标
                  </el-button>
                </div>
              </div>
              
              <!-- 指标列表 -->
              <div v-if="selectedCombination.indicators && selectedCombination.indicators.length > 0" class="indicators-list">
                <div
                  v-for="(indicator, index) in selectedCombination.indicators"
                  :key="indicator.configId"
                  class="indicator-item clickable-indicator"
                  @click="showIndicatorSqlResult(indicator, $event)"
                  style="cursor: pointer; border: 1px solid #ddd; margin: 8px 0; padding: 12px; border-radius: 6px; transition: all 0.2s ease;"
                >
                  <div class="indicator-header">
                    <span class="order-badge">{{ indicator.executionOrder }}</span>
                    <h4>{{ indicator.indicatorName }}</h4>
                    <el-tag size="mini" :type="indicator.isEnabled ? 'success' : 'info'">
                      {{ indicator.isEnabled ? '启用' : '禁用' }}
                    </el-tag>
                    <el-tooltip content="点击查看SQL执行结果" placement="top">
                      <i class="el-icon-view result-icon" style="margin-left: 8px; color: #409eff;"></i>
                    </el-tooltip>
                  </div>
                  <div class="indicator-content">
                    <p class="description">{{ indicator.description || '暂无描述' }}</p>
                    <div class="indicator-meta">
                      <span class="meta-item">编码: {{ indicator.indicatorCode }}</span>
                      <span v-if="indicator.dependencyConfig && Object.keys(indicator.dependencyConfig).length > 0" class="meta-item">
                        <i class="el-icon-link"></i> 有依赖关系
                      </span>
                      <!-- 显示引用信息 -->
                      <span v-if="hasIndicatorReferences(indicator)" class="meta-item reference-info">
                        <i class="el-icon-connection"></i> 引用前置结果
                      </span>
                    </div>
                  </div>
                  <!-- 操作按钮 -->
                  <div class="indicator-actions" style="margin-top: 8px;">
                    <el-button size="mini" type="primary" @click.stop="showIndicatorSqlResult(indicator, $event)">
                      <i class="el-icon-view"></i> 查看结果
                    </el-button>
                    <el-button size="mini" type="warning" @click.stop="handleEditIndicator(indicator, $event)">
                      <i class="el-icon-edit"></i> 编辑
                    </el-button>
                    <el-button size="mini" type="danger" @click.stop="handleDeleteIndicator(indicator, $event)">
                      <i class="el-icon-delete"></i> 删除
                    </el-button>
                  </div>
                </div>
              </div>
              
              <div v-else class="empty-indicators">
                <el-empty description="暂无指标配置">
                  <el-button type="primary" @click="handleConfigureIndicators">
                    <i class="el-icon-plus"></i> 配置指标
                  </el-button>
                </el-empty>
              </div>
            </div>

            <!-- 执行历史 -->
            <div class="detail-section">
              <div class="section-header">
                <h3>最近执行</h3>
                <el-button type="text" size="mini" @click="handleViewAllHistory">
                  查看全部
                </el-button>
              </div>
              
              <div class="execution-history">
                <div v-if="recentExecutions.length > 0">
                  <div
                    v-for="execution in recentExecutions"
                    :key="execution.executionId"
                    class="execution-item"
                  >
                    <div class="execution-header">
                      <span class="execution-name">{{ execution.executionName }}</span>
                      <el-tag :type="getExecutionStatusType(execution.status)" size="mini">
                        {{ getExecutionStatusText(execution.status) }}
                      </el-tag>
                    </div>
                    <div class="execution-meta">
                      <span>执行时间: {{ execution.startTime }}</span>
                      <span v-if="execution.totalDuration">耗时: {{ formatDuration(execution.totalDuration) }}</span>
                      <span>成功: {{ execution.successCount || 0 }} / 失败: {{ execution.failedCount || 0 }}</span>
                    </div>
                  </div>
                </div>
                <div v-else class="empty-history">
                  <p>暂无执行记录</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 子对话框 -->
    <CombinationConfigDialog
      :visible.sync="configDialogVisible"
      :combination="editingCombination"
      @saved="handleCombinationSaved"
    />

    <IndicatorConfigDialog
      :visible.sync="indicatorConfigDialogVisible"
      :combination="selectedCombination"
      @saved="handleIndicatorsSaved"
    />

    <ExecutionDialog
      :visible.sync="executionDialogVisible"
      :combination="selectedCombination"
      @executed="handleExecuted"
    />

    <FlowViewDialog
      :visible="flowDialogVisible"
      :combination="selectedCombination"
      @close="handleFlowDialogClose"
    />

    <ExecutionHistoryDialog
      :visible.sync="historyDialogVisible"
      :combination="selectedCombination"
    />

    <!-- SQL结果展示对话框 -->
    <el-dialog
      title="SQL执行结果"
      :visible.sync="sqlResultDialogVisible"
      width="80%"
      :close-on-click-modal="false"
      :append-to-body="true"
      :modal="false"
      :z-index="3500"
      class="sql-result-dialog sjmxgl-dialog-scope"
      custom-class="sql-result-dialog-wrapper"
      @opened="handleSqlResultDialogOpened"
      @close="handleSqlResultDialogClose"
    >
      <div class="sql-result-container">
        <!-- 指标信息 -->
        <div v-if="currentIndicatorConfig" class="indicator-info">
          <div class="info-item">
            <label>指标名称:</label>
            <span>{{ currentIndicatorConfig.indicatorName }}</span>
          </div>
          <div class="info-item">
            <label>指标编码:</label>
            <span>{{ currentIndicatorConfig.indicatorCode }}</span>
          </div>
          <div class="info-item">
            <label>执行顺序:</label>
            <span>{{ currentIndicatorConfig.executionOrder }}</span>
          </div>
        </div>

        <!-- 执行结果 -->
        <div class="result-section">
          <!-- 🔥 修复：加载状态不使用遮罩层，只显示提示信息 -->
          <div v-if="indicatorSqlLoading" class="loading-placeholder">
            <div style="padding: 40px; text-align: center;">
              <i class="el-icon-loading" style="font-size: 32px; color: #409EFF;"></i>
              <p style="margin-top: 16px; font-size: 16px; color: #606266;">正在执行SQL查询...</p>
              <p style="margin-top: 8px; font-size: 14px; color: #909399;">查询时间可能较长，请耐心等待</p>
            </div>
          </div>

          <!-- 成功结果 -->
          <div v-else-if="indicatorSqlResult && !indicatorSqlResult.error">
            <!-- 统计信息 -->
            <div class="result-stats">
              <el-row :gutter="20">
                <el-col :span="8">
                  <div class="stat-item">
                    <span class="stat-label">返回记录数:</span>
                    <span class="stat-value">{{ indicatorSqlResult.total || 0 }}</span>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="stat-item">
                    <span class="stat-label">字段数量:</span>
                    <span class="stat-value">{{ indicatorSqlResult.columns?.length || 0 }}</span>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="stat-item">
                    <span class="stat-label">执行时间:</span>
                    <span class="stat-value">{{ indicatorSqlResult.executionTime || 0 }}ms</span>
                  </div>
                </el-col>
              </el-row>
            </div>

            <!-- 数据表格 -->
            <div class="result-table">
              <el-table
                :data="indicatorSqlResult.records"
                border
                stripe
                max-height="400"
                style="width: 100%"
              >
                <el-table-column
                  v-for="column in indicatorSqlResult.columns"
                  :key="column.name"
                  :prop="column.name"
                  :label="column.label || column.name"
                  show-overflow-tooltip
                />
              </el-table>
            </div>
          </div>

          <!-- 错误结果 -->
          <div v-else-if="indicatorSqlResult && indicatorSqlResult.error" class="error-result">
            <el-alert
              title="SQL执行失败"
              type="error"
              :description="indicatorSqlResult.error"
              show-icon
              :closable="false"
            />
          </div>

          <!-- 空结果 -->
          <div v-else class="empty-result">
            <el-empty description="暂无执行结果" />
          </div>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="sqlResultDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="showIndicatorSqlResult(currentIndicatorConfig)">重新执行</el-button>
      </div>
    </el-dialog>
  </el-dialog>
</template>

<script>
import {
  getCombinationList,
  getCombinationDetail,
  deleteCombination,
  copyCombination,
  getExecutionHistory,
  executeSQL,
  executeIndicatorWithTempTable,
  removeIndicatorFromCombination,
  getModelDoc
} from '@/api/mxgl'
import { getChatHistoryDetail } from '@/api/ai/chatHistory'

// 导入子组件（这些组件需要单独创建）
import CombinationConfigDialog from './CombinationConfigDialog'
import IndicatorConfigDialog from './IndicatorConfigDialog'
import ExecutionDialog from './ExecutionDialog'
import FlowViewDialog from './FlowViewDialog'
import ExecutionHistoryDialog from './ExecutionHistoryDialog'
// 引入z-index层级管理样式
import './dialog-z-index.css'

export default {
  name: 'IndicatorCombinationDialog',
  components: {
    CombinationConfigDialog,
    IndicatorConfigDialog,
    ExecutionDialog,
    FlowViewDialog,
    ExecutionHistoryDialog
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      // 对话框状态
      dialogVisible: false,

      // 🔥 新增：子对话框关闭标志，防止误关闭主对话框
      isChildDialogClosing: false,

      // 筛选表单
      filterForm: {
        combinationCode: '',
        combinationName: '',
        category: '',
        status: ''
      },

      // 分页参数
      pagination: {
        pageNum: 1,
        pageSize: 5,
        total: 0
      },

      // 数据
      combinationList: [],
      selectedCombination: null,
      recentExecutions: [],
      
      // 编辑状态
      editingCombination: null,
      editingIndicator: null, // 当前编辑的指标
      
      // 子对话框状态
      configDialogVisible: false,
      indicatorConfigDialogVisible: false,
      executionDialogVisible: false,
      flowDialogVisible: false,
      historyDialogVisible: false,

      // SQL结果展示相关
      sqlResultDialogVisible: false,
      currentIndicatorConfig: null,
      indicatorSqlResult: null,
      indicatorSqlLoading: false,

      // 指标执行结果缓存
      indicatorExecutionResults: new Map(), // 存储每个指标的执行结果
      executionOrder: [], // 记录执行顺序

      // 加载状态
      loading: false
    }
  },
  watch: {
    visible: {
      handler(val) {
        console.log('🔍 IndicatorCombinationDialog visible prop changed:', val)
        this.dialogVisible = val
        if (val) {
          this.loadCombinationList()
        } else {
          // 只有当主对话框真正关闭时才清理状态
          // 延迟执行，避免子对话框状态变化触发
          setTimeout(() => {
            if (!this.dialogVisible) {
              this.handleDialogClose()
            }
          }, 200)
        }
      },
      immediate: true
    },
    dialogVisible(val) {
      console.log('🔍 IndicatorCombinationDialog dialogVisible changed:', val)

      // 🔥 修复：只有在非子对话框关闭的情况下才发送事件给父组件
      if (!val) {
        // 检查是否是子对话框关闭导致的
        if (!this.isChildDialogClosing) {
          // 延迟发送关闭事件，确保是用户主动关闭
          this.$nextTick(() => {
            this.$emit('update:visible', false)
          })
        } else {
          // 重置子对话框关闭标志
          this.isChildDialogClosing = false
        }
      } else {
        this.$emit('update:visible', true)
      }
    },

    // 🔥 新增：监听SQL结果对话框状态
    sqlResultDialogVisible(val) {
      console.log('🔄 SQL结果对话框状态变化:', val)
      if (val) {
        this.$nextTick(() => {
          this.ensureSqlResultDialogOnTop()
        })
      } else {
        // 对话框关闭时，清理遮罩层
        setTimeout(() => {
          this.clearSqlResultDialogMasks()
        }, 100)
      }
    }
  },
  methods: {
    // 加载组合列表
    async loadCombinationList(isAutoSelect = true) {
      try {
        this.loading = true
        console.log('📊 指标组合 - 查询参数:', {
          pageNum: this.pagination.pageNum,
          pageSize: this.pagination.pageSize,
          combinationCode: this.filterForm.combinationCode,
          combinationName: this.filterForm.combinationName,
          category: this.filterForm.category,
          status: this.filterForm.status
        })

        const response = await getCombinationList({
          pageNum: this.pagination.pageNum,
          pageSize: this.pagination.pageSize,
          combinationCode: this.filterForm.combinationCode,
          combinationName: this.filterForm.combinationName,
          category: this.filterForm.category,
          status: this.filterForm.status
        })

        console.log('📊 指标组合 - 接口响应:', response)

        // 兼容两种响应格式
        if (response && response.code === 1) {
          this.combinationList = response.data.records || []
          this.pagination.total = response.data.total || 0
          console.log('✅ 指标组合列表加载成功(标准格式):', this.combinationList.length, '个组合, 总计:', this.pagination.total)

          // 自动选择第一个指标组合（仅在初次加载时）
          if (this.combinationList.length > 0 && !this.selectedCombination && isAutoSelect) {
            // 添加小延迟，让用户看到列表加载完成
            setTimeout(async () => {
              await this.selectCombination(this.combinationList[0])
              // 只在非筛选状态下显示自动选择提示
              const hasFilter = this.filterForm.combinationCode || this.filterForm.combinationName ||
                                this.filterForm.category || this.filterForm.status
              if (!hasFilter) {
                this.$message.success(`已自动选择组合: ${this.combinationList[0].combinationName}`)
              }

              // 添加高亮动画效果
              this.$nextTick(() => {
                const firstItem = document.querySelector('.combination-item.active')
                if (firstItem) {
                  firstItem.classList.add('auto-selected')
                  setTimeout(() => {
                    firstItem.classList.remove('auto-selected')
                  }, 2000)
                }
              })
            }, 300)
          }
        } else if (response && response.records) {
          // 直接格式（mock数据）
          this.combinationList = response.records || []
          this.pagination.total = response.total || this.combinationList.length
          console.log('✅ 指标组合列表加载成功(直接格式):', this.combinationList.length, '个组合, 总计:', this.pagination.total)

          // 自动选择第一个指标组合（仅在初次加载时）
          if (this.combinationList.length > 0 && !this.selectedCombination && isAutoSelect) {
            setTimeout(async () => {
              await this.selectCombination(this.combinationList[0])
              const hasFilter = this.filterForm.combinationCode || this.filterForm.combinationName ||
                                this.filterForm.category || this.filterForm.status
              if (!hasFilter) {
                this.$message.success(`已自动选择组合: ${this.combinationList[0].combinationName}`)
              }
            }, 300)
          }
        } else {
          this.$message.error(response.msg || '加载组合列表失败')
          console.error('❌ 指标组合列表加载失败:', response)
        }
      } catch (error) {
        console.error('❌ 加载组合列表失败:', error)
        this.$message.error('加载组合列表失败')
      } finally {
        this.loading = false
      }
    },

    // 搜索
    handleSearch() {
      console.log('🔍 执行筛选查询:', this.filterForm)
      this.pagination.pageNum = 1 // 搜索时重置到第一页
      this.loadCombinationList(false) // 搜索时不自动选择
    },

    // 重置筛选
    handleResetFilter() {
      this.filterForm = {
        combinationCode: '',
        combinationName: '',
        category: '',
        status: ''
      }
      this.pagination.pageNum = 1 // 重置时回到第一页
      console.log('🔄 重置筛选条件')
      this.loadCombinationList(true) // 重置后重新加载并自动选择
    },

    // 分页 - 页码变化
    handleCombinationPageChange(pageNum) {
      this.pagination.pageNum = pageNum
      this.loadCombinationList(false)
    },

    // 分页 - 每页条数变化
    handleCombinationSizeChange(pageSize) {
      this.pagination.pageSize = pageSize
      this.pagination.pageNum = 1
      this.loadCombinationList(false)
    },

    // 选择组合
    async selectCombination(combination) {
      try {
        this.selectedCombination = combination

        console.log('📊 指标组合 - 选择组合:', combination)

        // 加载详细信息
        const detailResponse = await getCombinationDetail(combination.combinationId)
        console.log('📊 指标组合 - 组合详情响应:', detailResponse)

        // 兼容两种响应格式
        if (detailResponse && detailResponse.code === 1) {
          this.selectedCombination = detailResponse.data
          console.log('✅ 组合详情加载成功(标准格式)')
        } else if (detailResponse && detailResponse.combinationId) {
          // 直接格式（mock数据）
          this.selectedCombination = detailResponse
          console.log('✅ 组合详情加载成功(直接格式)')
        } else {
          this.$message.error(detailResponse.msg || '加载组合详情失败')
          console.error('❌ 组合详情加载失败:', detailResponse)
          return
        }

        console.log('组合详情数据:', this.selectedCombination)
        console.log('指标数据:', this.selectedCombination.indicators)

        // 确保指标数据正确加载 - 修复判断逻辑
        if (this.selectedCombination.indicators && Array.isArray(this.selectedCombination.indicators)) {
          console.log(`✅ 成功加载 ${this.selectedCombination.indicators.length} 个指标`)
          // 确保每个指标都有必要的字段
          this.selectedCombination.indicators = this.selectedCombination.indicators.map(indicator => ({
            ...indicator,
            // 确保必要字段存在
            configId: indicator.configId || 'unknown',
            indicatorName: indicator.indicatorName || '未知指标',
            indicatorCode: indicator.indicatorCode || 'UNKNOWN',
            isEnabled: indicator.isEnabled !== undefined ? indicator.isEnabled : true,
            executionOrder: indicator.executionOrder || 0,
            description: indicator.description || '暂无描述'
          }))
        } else {
          console.warn('⚠️ 组合详情中没有有效的指标数据')
          this.$message.warning('该组合暂无配置指标')
          this.selectedCombination.indicators = []
        }

        console.log('最终选择的组合数据:', this.selectedCombination)
        console.log('处理后的指标数量:', this.selectedCombination.indicators?.length || 0)
      } catch (error) {
        console.error('❌ 选择组合失败:', error)
        this.$message.error('加载组合详情失败')
      }

      // 加载最近执行记录
      try {
        console.log('📊 指标组合 - 加载执行历史')
        const historyResponse = await getExecutionHistory({
          pageNum: 1,
          pageSize: 5,
          combinationId: combination.combinationId
        })
        console.log('📊 指标组合 - 执行历史响应:', historyResponse)

        // 兼容两种响应格式
        if (historyResponse && historyResponse.code === 1) {
          this.recentExecutions = historyResponse.data.records || []
          console.log('✅ 执行历史加载成功(标准格式):', this.recentExecutions.length, '条记录')
        } else if (historyResponse && historyResponse.records) {
          // 直接格式（mock数据）
          this.recentExecutions = historyResponse.records || []
          console.log('✅ 执行历史加载成功(直接格式):', this.recentExecutions.length, '条记录')
        } else {
          console.log('⚠️ 执行历史为空或加载失败')
        }
      } catch (error) {
        console.error('❌ 加载执行历史失败:', error)
        // 不影响主流程，只记录错误
      }
    },

    // 新建组合
    handleCreateCombination() {
      this.editingCombination = null
      this.configDialogVisible = true
    },

    // 编辑组合
    handleEditCombination(combination) {
      this.editingCombination = combination
      this.configDialogVisible = true
    },

    // 配置指标
    handleConfigureIndicators() {
      this.indicatorConfigDialogVisible = true
    },

    // 执行组合
    async handleExecuteCombination(combination) {
      // 先选择组合，确保加载完整的指标信息
      await this.selectCombination(combination)
      // 然后打开执行对话框
      this.executionDialogVisible = true
    },

    // 查看流程图
    handleViewFlow(combination) {
      console.log('🎯 handleViewFlow called with combination:', combination)
      console.log('🎯 Current selectedCombination:', this.selectedCombination)
      console.log('🎯 Current flowDialogVisible:', this.flowDialogVisible)

      this.selectedCombination = combination
      this.flowDialogVisible = true

      console.log('🎯 After setting - selectedCombination:', this.selectedCombination)
      console.log('🎯 After setting - flowDialogVisible:', this.flowDialogVisible)

      // 添加一个确认消息
      this.$message.success(`正在打开流程图: ${combination?.combinationName || '未知组合'}`)
    },

    // 🔥 最简单的修复：处理流程图对话框关闭
    handleFlowDialogClose() {
      console.log('🔒 流程图对话框关闭，手动设置状态')
      this.flowDialogVisible = false
    },

    // 查看全部历史
    handleViewAllHistory() {
      this.historyDialogVisible = true
    },

    // 🆕 新增：显示特定组合的流程图（供父组件调用）
    async showSpecificCombinationFlowChart(combinationId, modelInfo) {
      try {
        console.log('🎯 显示特定组合的流程图:', { combinationId, modelInfo })

        // 1. 加载组合列表（如果还没有加载）
        if (!this.combinationList || this.combinationList.length === 0) {
          console.log('📋 先加载组合列表')
          await this.loadCombinationList()
        }

        // 2. 查找目标组合
        const targetCombination = this.combinationList.find(combo =>
          combo.combinationId === combinationId ||
          combo.combinationCode === combinationId
        )

        if (targetCombination) {
          console.log('✅ 找到目标组合:', targetCombination)

          // 3. 选择该组合
          this.selectedCombination = targetCombination

          // 4. 等待组件更新
          await this.$nextTick()

          // 5. 直接打开流程图
          setTimeout(() => {
            console.log('🚀 打开流程图对话框')
            this.handleViewFlow(targetCombination)
          }, 300)

        } else {
          console.warn('⚠️ 未找到目标组合:', combinationId)
          this.$message.warning(`未找到组合ID为 ${combinationId} 的指标组合`)

          // 显示提示信息
          this.$message.info(`来自评估模型"${modelInfo?.modelName || modelInfo?.modelId}"的流程图请求`)
        }

      } catch (error) {
        console.error('❌ 显示特定组合流程图失败:', error)
        this.$message.error('显示流程图失败: ' + error.message)
      }
    },

    // 更多操作
    async handleMoreAction(command) {
      const { action, data } = command

      switch (action) {
        case 'copy':
          await this.handleCopyCombination(data)
          break
        case 'history':
          this.selectedCombination = data
          this.historyDialogVisible = true
          break
        case 'modelDoc':
          this.handleModelDoc(data)
          break
        case 'delete':
          await this.handleDeleteCombination(data)
          break
      }
    },

    // 模型文档
    async handleModelDoc(combination) {
      const loadingMsg = this.$message({ message: '正在加载模型文档...', type: 'success', duration: 0 })

      try {
        const docRes = await getModelDoc(combination.combinationId)
        if (docRes.code !== 1 || !docRes.data || !docRes.data.hasDoc) {
          this.$message.warning('暂无关联文档')
          return
        }

        const { docId, docName } = docRes.data
        const detailRes = await getChatHistoryDetail(docId)

        if (detailRes.code === 200 && detailRes.data) {
          let dialogue = []
          try {
            dialogue = JSON.parse(detailRes.data.dialogue || '[]')
          } catch (e) {
            dialogue = []
          }

          let htmlContent = ''
          let htmlTitle = ''

          for (let i = dialogue.length - 1; i >= 0; i--) {
            const msg = dialogue[i]
            if (msg.role === 'assistant') {
              if (msg.htmlContent) {
                htmlContent = msg.htmlContent
                htmlTitle = msg.htmlTitle || ''
                break
              }
              if (msg.content) {
                const parsed = this.parseHtmlFromContent(msg.content)
                if (parsed.html) {
                  htmlContent = parsed.html
                  htmlTitle = parsed.title
                  break
                }
              }
            }
          }

          if (!htmlContent) {
            this.$message.warning('暂无关联文档')
            return
          }

          // 在新页面中展示文档
          const title = htmlTitle || docName || detailRes.data.title || '模型文档'
          const newWindow = window.open('', '_blank')
          newWindow.document.write(htmlContent)
          newWindow.document.title = title
          newWindow.document.close()
        } else {
          this.$message.warning('文档内容获取失败')
        }
      } catch (error) {
        console.error('获取模型文档失败:', error)
        this.$message.error('获取模型文档失败')
      } finally {
        loadingMsg.close()
      }
    },

    // 从content中解析HTML文档
    parseHtmlFromContent(content) {
      if (!content) return { html: '', title: '' }
      const startRegex = /<<<HTML5_START:(.+?)>>>/
      const endMarker = '<<<HTML5_END>>>'
      const startMatch = content.match(startRegex)
      if (!startMatch) return { html: '', title: '' }
      const title = startMatch[1]
      const startIdx = content.indexOf(startMatch[0]) + startMatch[0].length
      const endIdx = content.indexOf(endMarker)
      if (endIdx === -1) return { html: '', title: '' }
      return { html: content.substring(startIdx, endIdx).trim(), title }
    },

    // 复制组合
    async handleCopyCombination(combination) {
      try {
        const newName = `${combination.combinationName}-副本`
        const newCode = `${combination.combinationCode}-COPY`
        
        const response = await copyCombination(combination.combinationId, {
          newCombinationName: newName,
          newCombinationCode: newCode
        })
        
        if (response.code === 1) {
          this.$message.success('组合复制成功')
          this.loadCombinationList()
        } else {
          this.$message.error(response.msg || '复制失败')
        }
      } catch (error) {
        console.error('复制组合失败:', error)
        this.$message.error('复制组合失败')
      }
    },

    // 删除组合
    async handleDeleteCombination(combination) {
      try {
        await this.$confirm(`确定要删除组合"${combination.combinationName}"吗？`, '确认删除', {
          type: 'warning'
        })
        
        const response = await deleteCombination(combination.combinationId)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadCombinationList()
          if (this.selectedCombination?.combinationId === combination.combinationId) {
            this.selectedCombination = null
          }
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除组合失败:', error)
          this.$message.error('删除组合失败')
        }
      }
    },

    // 组合保存回调
    handleCombinationSaved() {
      this.loadCombinationList(false) // 保存后刷新时不自动选择
    },

    // 指标保存回调
    handleIndicatorsSaved() {
      if (this.selectedCombination) {
        this.selectCombination(this.selectedCombination)
      }
    },

    // 执行完成回调
    handleExecuted() {
      if (this.selectedCombination) {
        this.selectCombination(this.selectedCombination)
      }
    },

    // 工具方法
    getStatusType(status) {
      const typeMap = {
        'ACTIVE': 'success',
        'DRAFT': 'warning',
        'INACTIVE': 'info'
      }
      return typeMap[status] || 'info'
    },

    getStatusText(status) {
      const textMap = {
        'ACTIVE': '启用',
        'DRAFT': '草稿',
        'INACTIVE': '禁用'
      }
      return textMap[status] || status
    },

    getCategoryText(category) {
      const textMap = {
        'INVESTMENT_PENETRATION': '投资穿透',
        'PROPERTY_PENETRATION': '产权穿透',
        'FINANCIAL_PENETRATION': '财务穿透',
        'FINANCIAL_RISK_PENETRATION': '金融风险穿透',
        'ACCOUNTING_PENETRATION': '会计穿透',
        'SALARY_DISTRIBUTION': '薪酬分配',
        'MILITARY_PENETRATION': '军品穿透',
        'PROCUREMENT_SUPPLY_CHAIN': '采购与供应链',
        'OVERSEAS_PENETRATION': '境外穿透',
        'CONTRACT_PENETRATION': '合同穿透',
        'INDUSTRY_PENETRATION': '行业穿透',
        'FUND_PENETRATION': '资金穿透'
      }
      return textMap[category] || category
    },

    getExecutionStatusType(status) {
      const typeMap = {
        'SUCCESS': 'success',
        'RUNNING': 'primary',
        'FAILED': 'danger',
        'CANCELLED': 'info'
      }
      return typeMap[status] || 'info'
    },

    getExecutionStatusText(status) {
      const textMap = {
        'SUCCESS': '成功',
        'RUNNING': '运行中',
        'FAILED': '失败',
        'CANCELLED': '已取消'
      }
      return textMap[status] || status
    },

    formatDuration(duration) {
      if (!duration) return '-'
      const seconds = Math.floor(duration / 1000)
      const minutes = Math.floor(seconds / 60)
      const hours = Math.floor(minutes / 60)

      if (hours > 0) {
        return `${hours}小时${minutes % 60}分钟`
      } else if (minutes > 0) {
        return `${minutes}分钟${seconds % 60}秒`
      } else {
        return `${seconds}秒`
      }
    },

    // 对话框关闭时的清理方法
    handleDialogClose() {
      console.log('🔒 IndicatorCombinationDialog handleDialogClose called')

      // 清理选中状态，确保下次打开时能重新自动选择第一个
      this.selectedCombination = null
      this.recentExecutions = []
      this.filterForm = {
        combinationCode: '',
        combinationName: '',
        category: '',
        status: ''
      }
      this.editingCombination = null

      // 温和地关闭子对话框，避免强制关闭
      // 只有当主对话框真正关闭时才关闭子对话框
      setTimeout(() => {
        if (!this.dialogVisible) {
          this.configDialogVisible = false
          this.indicatorConfigDialogVisible = false
          this.executionDialogVisible = false
          this.flowDialogVisible = false
          this.historyDialogVisible = false
          console.log('🔒 所有子对话框已关闭')

          // 🔥 最终清理遮罩层
          this.clearResidualMasks()
        }
      }, 100)
    },

    // 🔥 新增：清理残留的遮罩层
    clearResidualMasks() {
      try {
        console.log('🧹 清理残留遮罩层')

        // 查找所有遮罩层
        const modals = document.querySelectorAll('.v-modal')
        console.log(`🔍 找到 ${modals.length} 个遮罩层`)

        // 移除多余的遮罩层（保留最后一个，如果有的话）
        if (modals.length > 1) {
          for (let i = 0; i < modals.length - 1; i++) {
            console.log(`🗑️ 移除多余遮罩层 ${i + 1}`)
            modals[i].style.display = 'none'
            modals[i].style.opacity = '0'
            modals[i].style.visibility = 'hidden'
          }
        }

        // 如果没有对话框打开，移除所有遮罩层
        const openDialogs = document.querySelectorAll('.el-dialog__wrapper[style*="display: block"], .el-dialog__wrapper:not([style*="display: none"])')
        if (openDialogs.length === 0) {
          modals.forEach((modal, index) => {
            console.log(`🗑️ 移除所有遮罩层 ${index + 1}`)
            modal.style.display = 'none'
            modal.style.opacity = '0'
            modal.style.visibility = 'hidden'
          })
        }

        // 确保body可以正常滚动
        if (openDialogs.length === 0) {
          document.body.style.overflow = ''
          document.body.style.paddingRight = ''
          document.body.classList.remove('el-popup-parent--hidden')
        }

        console.log('✅ 残留遮罩层清理完成')
      } catch (error) {
        console.error('❌ 清理残留遮罩层失败:', error)
      }
    },

    // 🔥 新增：处理用户主动关闭
    handleUserClose() {
      console.log('🔒 用户主动关闭指标组合分析对话框')
      // 通知父组件用户主动关闭
      this.$parent.userClosedDialog = true
      this.handleDialogClose()
    },

    // 🔥 新增：关闭前处理
    handleBeforeClose(done) {
      console.log('🔒 指标组合分析对话框准备关闭')
      // 设置用户主动关闭标志
      if (this.$parent) {
        this.$parent.userClosedDialog = true
      }
      done()
    },

    // 添加测试指标数据
    addTestIndicators() {
      if (this.selectedCombination) {
        this.selectedCombination.indicators = [
          {
            configId: 'config_test_001',
            indicatorName: '测试指标名称001',
            indicatorCode: 'TEST001',
            isEnabled: true,
            executionOrder: 1,
            sqlContent: 'SELECT ei.ENTERPRISE_NAME AS "企业名称", ROUND(fid.INDICATOR_VALUE, 2) AS "净资产收益率%" FROM TBL_FINANCIAL_INDICATOR_DATA fid JOIN TBL_FINANCIAL_INDICATOR_DEFINITION def ON fid.INDICATOR_ID = def.INDICATOR_ID JOIN TBL_ENTERPRISE_INFO ei ON fid.ENTERPRISE_ID = ei.ENTERPRISE_ID WHERE def.INDICATOR_CODE = "ROE"',
            dataSourceId: '5504842711104fe39f8c3dd36866295c',
            parameterMapping: { 'year': '2024', 'quarter': 'Q4' },
            description: '衡量企业盈利能力的重要指标'
          },
          {
            configId: 'config_test_002',
            indicatorName: '资产负债率',
            indicatorCode: 'DEBT_RATIO',
            isEnabled: true,
            executionOrder: 2,
            sqlContent: 'SELECT ei.ENTERPRISE_NAME AS "企业名称", ROUND(fid.INDICATOR_VALUE, 2) AS "资产负债率%" FROM TBL_FINANCIAL_INDICATOR_DATA fid JOIN TBL_FINANCIAL_INDICATOR_DEFINITION def ON fid.INDICATOR_ID = def.INDICATOR_ID JOIN TBL_ENTERPRISE_INFO ei ON fid.ENTERPRISE_ID = ei.ENTERPRISE_ID WHERE def.INDICATOR_CODE = "DEBT_RATIO"',
            dataSourceId: '5504842711104fe39f8c3dd36866295c',
            parameterMapping: { 'year': '2024' },
            description: '反映企业财务风险的关键指标'
          },
          {
            configId: 'config_test_003',
            indicatorName: '流动比率',
            indicatorCode: 'CURRENT_RATIO',
            isEnabled: false,
            executionOrder: 3,
            sqlContent: 'SELECT ei.ENTERPRISE_NAME AS "企业名称", ROUND(fid.INDICATOR_VALUE, 2) AS "流动比率" FROM TBL_FINANCIAL_INDICATOR_DATA fid JOIN TBL_FINANCIAL_INDICATOR_DEFINITION def ON fid.INDICATOR_ID = def.INDICATOR_ID JOIN TBL_ENTERPRISE_INFO ei ON fid.ENTERPRISE_ID = ei.ENTERPRISE_ID WHERE def.INDICATOR_CODE = "CURRENT_RATIO"',
            dataSourceId: '5504842711104fe39f8c3dd36866295c',
            parameterMapping: {},
            description: '衡量企业短期偿债能力'
          }
        ]
        console.log('已添加测试指标数据:', this.selectedCombination.indicators)
      }
    },

    // 显示指标SQL执行结果
    async showIndicatorSqlResult(indicator, event) {
      console.log('🎯 showIndicatorSqlResult called with indicator:', indicator)

      // 先显示一个测试消息，确认点击事件触发
      this.$message.success('点击事件触发成功！指标：' + (indicator?.indicatorName || '未知'))

      // 详细打印指标数据结构，帮助调试
      console.log('指标完整数据结构:', JSON.stringify(indicator, null, 2))
      console.log('指标字段检查:', {
        indicatorName: indicator?.indicatorName,
        indicatorCode: indicator?.indicatorCode,
        dataSourceId: indicator?.dataSourceId,
        datasourceId: indicator?.datasourceId,
        dataSource: indicator?.dataSource,
        sqlContent: indicator?.sqlContent,
        parameterMapping: indicator?.parameterMapping,
        allKeys: Object.keys(indicator || {})
      })

      // 阻止事件冒泡
      if (event) {
        event.stopPropagation()
      }

      if (!indicator) {
        this.$message.warning('指标配置信息不存在')
        return
      }

      // 显示对话框
      this.currentIndicatorConfig = indicator
      this.sqlResultDialogVisible = true
      this.indicatorSqlLoading = true
      this.indicatorSqlResult = null

      try {
        // 检查SQL内容和数据源
        if (!indicator.sqlContent) {
          this.$message.warning('该指标没有SQL内容')
          this.indicatorSqlLoading = false
          return
        }

        // 检查数据源ID（实际数据中可能没有这个字段，我们使用默认数据源）
        const dataSourceId = indicator.dataSourceId || indicator.datasourceId || indicator.dataSource || '5504842711104fe39f8c3dd36866295c'

        console.log('指标数据源检查:', {
          dataSourceId: indicator.dataSourceId,
          datasourceId: indicator.datasourceId,
          dataSource: indicator.dataSource,
          使用的数据源ID: dataSourceId,
          indicator: indicator
        })

        // 处理参数替换（优先使用组合级别的参数配置）
        let parameterMapping = {}

        // 1. 尝试从组合级别获取参数配置
        if (this.selectedCombination && this.selectedCombination.parameterConfig) {
          try {
            if (typeof this.selectedCombination.parameterConfig === 'string') {
              const combinationParams = JSON.parse(this.selectedCombination.parameterConfig)
              // 提取默认值作为参数值
              Object.keys(combinationParams).forEach(key => {
                const paramConfig = combinationParams[key]
                if (paramConfig && paramConfig.defaultValue) {
                  parameterMapping[key] = paramConfig.defaultValue
                }
              })
            } else if (typeof this.selectedCombination.parameterConfig === 'object') {
              parameterMapping = this.selectedCombination.parameterConfig
            }
          } catch (e) {
            console.warn('解析组合参数配置失败:', e)
          }
        }

        // 2. 如果组合级别没有参数，尝试从指标级别获取
        if (Object.keys(parameterMapping).length === 0) {
          parameterMapping = indicator.parameterMapping || indicator.parameterConfig || {}
        }

        let processedSqlContent = indicator.sqlContent

        console.log('参数映射处理:', {
          组合参数配置: this.selectedCombination?.parameterConfig,
          指标参数映射: indicator.parameterMapping,
          指标参数配置: indicator.parameterConfig,
          最终使用的参数: parameterMapping
        })

        // 3. 处理指标引用替换
        processedSqlContent = this.processIndicatorReferences(processedSqlContent, indicator)

        // 4. 处理普通参数替换
        Object.keys(parameterMapping).forEach(paramName => {
          const paramValue = parameterMapping[paramName]
          if (paramValue !== null && paramValue !== undefined && paramValue !== '') {
            const regex = new RegExp(`\\$\\{${paramName}\\}`, 'g')
            processedSqlContent = processedSqlContent.replace(regex, paramValue)
            console.log(`参数替换: \${${paramName}} → ${paramValue}`)
          }
        })

        // 调用后端API执行SQL
        console.log('执行SQL:', {
          指标名称: indicator.indicatorName,
          指标编码: indicator.indicatorCode,
          数据源ID: dataSourceId,
          原始SQL: indicator.sqlContent.substring(0, 200) + '...',
          处理后SQL: processedSqlContent.substring(0, 200) + '...',
          参数映射: parameterMapping
        })

        // 调用新的指标执行接口，会创建临时表供后续引用
        const response = await executeIndicatorWithTempTable({
          configId: indicator.configId,
          dataSourceId: dataSourceId,
          sqlContent: processedSqlContent,
          parameters: parameterMapping
        })

        if (response.code === 1) {
          // 处理成功响应
          const result = response.data

          // 新接口返回的数据结构：{ records: [...], rowCount: 1000, executionTime: 87, tempTableName: "..." }
          const records = result.records || []

          // 自动生成列信息（基于第一条记录的字段）
          let columns = []
          if (records.length > 0) {
            columns = Object.keys(records[0]).map(key => ({
              name: key,
              label: key,
              type: typeof records[0][key] === 'number' ? 'number' : 'string'
            }))
          }

          this.indicatorSqlResult = {
            records: records,
            columns: columns,
            total: result.rowCount || records.length,
            executionTime: result.executionTime || 0,
            tempTableName: result.tempTableName // 临时表名，供后续引用
          }

          console.log('指标执行成功:', {
            记录数: this.indicatorSqlResult.total,
            实际记录数: this.indicatorSqlResult.records.length,
            执行时间: this.indicatorSqlResult.executionTime + 'ms',
            列数: this.indicatorSqlResult.columns.length,
            列信息: this.indicatorSqlResult.columns.map(col => col.name),
            临时表名: this.indicatorSqlResult.tempTableName
          })

          // 存储指标执行结果，供后续指标引用
          this.storeIndicatorResult(indicator, this.indicatorSqlResult)
        } else {
          // 处理错误响应
          throw new Error(response.msg || 'SQL执行失败')
        }

        // 🔥 修复：关闭 loading 状态（不再需要清理遮罩层，因为没有使用 v-loading）
        this.indicatorSqlLoading = false

      } catch (error) {
        console.error('SQL执行失败:', error)
        this.$message.error('SQL执行失败: ' + error.message)
        this.indicatorSqlResult = {
          error: error.message,
          records: [],
          total: 0,
          columns: []
        }
        // 🔥 修复：关闭 loading 状态（不再需要清理遮罩层，因为没有使用 v-loading）
        this.indicatorSqlLoading = false
      }
    },

    // 🔥 新增：SQL结果对话框打开时的处理
    handleSqlResultDialogOpened() {
      console.log('🎯 SQL结果对话框已打开，确保在最上层')
      this.ensureSqlResultDialogOnTop()
    },

    // 🔥 新增：SQL结果对话框关闭时的处理
    handleSqlResultDialogClose() {
      console.log('🔄 SQL结果对话框关闭')
      this.sqlResultDialogVisible = false
      this.currentIndicatorConfig = null
      this.indicatorSqlResult = null
      this.indicatorSqlLoading = false

      // 🔥 修复：清理可能残留的遮罩层
      this.$nextTick(() => {
        this.clearSqlResultDialogMasks()
      })
    },

    // 🔥 新增：确保SQL结果对话框在最上层
    ensureSqlResultDialogOnTop() {
      try {
        console.log('🔝 确保SQL结果对话框在最上层')

        this.$nextTick(() => {
          // 方法1: 通过类名查找对话框元素
          const dialogElement = document.querySelector('.sql-result-dialog-wrapper')
          if (dialogElement) {
            dialogElement.style.zIndex = '3500'
            console.log('✅ 通过类名设置SQL结果对话框z-index: 3500')
          }

          // 方法2: 通过标题查找对话框元素
          const dialogByTitle = document.querySelector('.el-dialog__header .el-dialog__title')
          if (dialogByTitle && dialogByTitle.textContent.includes('SQL执行结果')) {
            const dialog = dialogByTitle.closest('.el-dialog__wrapper')
            if (dialog) {
              dialog.style.zIndex = '3500'
              console.log('✅ 通过标题设置SQL结果对话框z-index: 3500')
            }
          }

          // 方法3: 查找所有对话框，确保SQL结果对话框在最上层
          const allDialogs = document.querySelectorAll('.el-dialog__wrapper')
          let maxZIndex = 3400
          allDialogs.forEach(dialog => {
            const currentZIndex = parseInt(window.getComputedStyle(dialog).zIndex) || 0
            if (currentZIndex > maxZIndex) {
              maxZIndex = currentZIndex
            }
          })

          // 为SQL结果对话框设置更高的z-index
          const sqlResultDialog = document.querySelector('.sql-result-dialog-wrapper') ||
                                 document.querySelector('.sql-result-dialog')?.closest('.el-dialog__wrapper')
          if (sqlResultDialog) {
            sqlResultDialog.style.zIndex = (maxZIndex + 10).toString()
            console.log(`✅ 动态设置SQL结果对话框z-index: ${maxZIndex + 10}`)
          }

          // 方法4: 强制设置CSS类
          const dialogWrapper = document.querySelector('.sql-result-dialog')?.closest('.el-dialog__wrapper')
          if (dialogWrapper) {
            dialogWrapper.classList.add('sql-result-dialog-top-layer')
            dialogWrapper.style.zIndex = '3500'
            console.log('✅ 添加顶层CSS类并设置z-index')
          }
        })

      } catch (error) {
        console.error('❌ 设置SQL结果对话框层级失败:', error)
      }
    },

    // 🔥 新增：清理 v-loading 指令创建的遮罩层
    clearLoadingMask() {
      try {
        console.log('🧹 开始清理 v-loading 遮罩层...')

        // 查找 SQL 结果对话框内的 loading 遮罩层
        const sqlResultContainer = document.querySelector('.sql-result-dialog .sql-result-container')
        if (sqlResultContainer) {
          // 移除 v-loading 创建的遮罩层
          const loadingMask = sqlResultContainer.querySelector('.el-loading-mask')
          if (loadingMask) {
            console.log('🗑️ 移除 v-loading 遮罩层:', loadingMask)
            loadingMask.remove()
          }

          // 移除 loading 相关的类名
          sqlResultContainer.classList.remove('el-loading-parent--relative')
          sqlResultContainer.classList.remove('el-loading-parent--hidden')
        }

        // 查找 result-section 元素的 loading 遮罩层
        const resultSection = document.querySelector('.sql-result-dialog .result-section')
        if (resultSection) {
          const loadingMask = resultSection.querySelector('.el-loading-mask')
          if (loadingMask) {
            console.log('🗑️ 移除 result-section 的 v-loading 遮罩层:', loadingMask)
            loadingMask.remove()
          }

          resultSection.classList.remove('el-loading-parent--relative')
          resultSection.classList.remove('el-loading-parent--hidden')
        }

        // 清理所有可能残留的 loading 遮罩层
        const allLoadingMasks = document.querySelectorAll('.el-loading-mask')
        allLoadingMasks.forEach((mask, index) => {
          // 检查遮罩层是否在 SQL 结果对话框内
          const isInSqlDialog = mask.closest('.sql-result-dialog')
          if (isInSqlDialog) {
            console.log(`🗑️ 移除残留的 loading 遮罩层 ${index + 1}:`, mask)
            mask.remove()
          }
        })

        console.log('✅ v-loading 遮罩层清理完成')
      } catch (error) {
        console.error('❌ 清理 v-loading 遮罩层失败:', error)
      }
    },

    // 🔥 新增：清理SQL结果对话框的遮罩层
    clearSqlResultDialogMasks() {
      try {
        console.log('🧹 开始清理SQL结果对话框遮罩层...')

        // 先清理 v-loading 遮罩层
        this.clearLoadingMask()

        // 清理SQL结果对话框相关的遮罩层
        const sqlDialogMasks = document.querySelectorAll('.sql-result-dialog-wrapper .v-modal')
        sqlDialogMasks.forEach((mask, index) => {
          console.log(`🗑️ 移除SQL结果对话框遮罩 ${index + 1}:`, mask)
          mask.remove()
        })

        // 清理可能残留的遮罩层
        const allMasks = document.querySelectorAll('.v-modal')
        allMasks.forEach((mask, index) => {
          // 检查遮罩层是否属于已关闭的对话框
          const parentDialog = mask.closest('.el-dialog__wrapper')
          if (!parentDialog || parentDialog.style.display === 'none') {
            console.log(`🗑️ 移除残留遮罩层 ${index + 1}:`, mask)
            mask.remove()
          }
        })

        // 确保页面可交互
        document.body.style.pointerEvents = ''
        document.body.style.overflow = ''
        document.body.classList.remove('el-popup-parent--hidden')

        console.log('✅ SQL结果对话框遮罩层清理完成')
      } catch (error) {
        console.error('❌ 清理SQL结果对话框遮罩层失败:', error)
      }
    },

    // 处理指标引用替换
    processIndicatorReferences(sqlContent, currentIndicator) {
      let processedSql = sqlContent

      // 获取当前指标在组合中的位置
      const indicators = this.selectedCombination?.indicators || []
      const currentIndex = indicators.findIndex(ind => ind.configId === currentIndicator.configId)

      console.log('处理指标引用:', {
        当前指标: currentIndicator.indicatorName,
        当前位置: currentIndex,
        总指标数: indicators.length,
        执行结果缓存: Array.from(this.indicatorExecutionResults.keys())
      })

      // 1. 处理 ${PREV_RESULT} - 引用上一个指标的结果
      if (processedSql.includes('${PREV_RESULT}')) {
        if (currentIndex > 0) {
          const prevIndicator = indicators[currentIndex - 1]
          const prevTableName = this.generateValidTableName(prevIndicator.configId)
          processedSql = processedSql.replace(/\$\{PREV_RESULT\}/g, prevTableName)
          console.log(`引用替换: \${PREV_RESULT} → ${prevTableName}`)
        } else {
          console.warn('当前是第一个指标，无法引用上一个结果')
          this.$message.warning('当前是第一个指标，无法引用上一个指标的结果')
        }
      }

      // 2. 处理 ${STEP_N_RESULT} - 引用指定步骤的结果
      const stepPattern = /\$\{STEP_(\d+)_RESULT\}/g
      let stepMatch
      while ((stepMatch = stepPattern.exec(processedSql)) !== null) {
        const stepNumber = parseInt(stepMatch[1]) - 1 // 转换为0基索引
        if (stepNumber >= 0 && stepNumber < currentIndex && stepNumber < indicators.length) {
          const targetIndicator = indicators[stepNumber]
          const targetTableName = this.generateValidTableName(targetIndicator.configId)
          processedSql = processedSql.replace(stepMatch[0], targetTableName)
          console.log(`引用替换: ${stepMatch[0]} → ${targetTableName}`)
        } else {
          console.warn(`无效的步骤引用: ${stepMatch[0]}`)
          this.$message.warning(`无效的步骤引用: ${stepMatch[0]}`)
        }
      }

      // 3. 处理 ${INDICATOR_CODE_RESULT} - 引用指定编码的指标结果
      const codePattern = /\$\{([A-Z0-9_]+)_RESULT\}/g
      let codeMatch
      while ((codeMatch = codePattern.exec(processedSql)) !== null) {
        const targetCode = codeMatch[1]
        const targetIndicator = indicators.find(ind =>
          ind.indicatorCode === targetCode &&
          indicators.indexOf(ind) < currentIndex
        )
        if (targetIndicator) {
          const targetTableName = this.generateValidTableName(targetIndicator.configId)
          processedSql = processedSql.replace(codeMatch[0], targetTableName)
          console.log(`引用替换: ${codeMatch[0]} → ${targetTableName}`)
        } else {
          console.warn(`找不到指标编码: ${targetCode}`)
          this.$message.warning(`找不到可引用的指标编码: ${targetCode}`)
        }
      }

      // 4. 处理聚合函数引用 ${PREV_RESULT.字段名.聚合函数}
      const aggPattern = /\$\{(PREV_RESULT|STEP_\d+_RESULT|[A-Z0-9_]+_RESULT)\.([A-Z0-9_\u4e00-\u9fa5%]+)\.(SUM|AVG|MAX|MIN|COUNT)\}/g
      let aggMatch
      while ((aggMatch = aggPattern.exec(processedSql)) !== null) {
        const [fullMatch, tableRef, fieldName, aggFunc] = aggMatch

        // 获取引用的表名
        let tableName = tableRef
        if (tableRef === 'PREV_RESULT' && currentIndex > 0) {
          const prevIndicator = indicators[currentIndex - 1]
          tableName = this.generateValidTableName(prevIndicator.configId)
        } else if (tableRef.startsWith('STEP_')) {
          const stepMatch = tableRef.match(/STEP_(\d+)_RESULT/)
          if (stepMatch) {
            const stepNumber = parseInt(stepMatch[1]) - 1
            if (stepNumber >= 0 && stepNumber < currentIndex) {
              const targetIndicator = indicators[stepNumber]
              tableName = this.generateValidTableName(targetIndicator.configId)
            }
          }
        }

        // 生成聚合查询
        const aggregateValue = this.calculateAggregateFromCache(tableName, fieldName, aggFunc)
        processedSql = processedSql.replace(fullMatch, aggregateValue)
        console.log(`聚合引用替换: ${fullMatch} → ${aggregateValue}`)
      }

      return processedSql
    },

    // 从缓存中计算聚合值
    calculateAggregateFromCache(tableName, fieldName, aggFunc) {
      const cachedResult = this.indicatorExecutionResults.get(tableName)
      if (!cachedResult || !cachedResult.result || !cachedResult.result.records) {
        console.warn(`找不到缓存结果: ${tableName}`)
        return '0' // 返回默认值
      }

      const records = cachedResult.result.records
      const values = records.map(record => {
        const value = record[fieldName]
        return parseFloat(value) || 0
      }).filter(v => !isNaN(v))

      if (values.length === 0) {
        console.warn(`字段"${fieldName}"没有有效数值`)
        return '0'
      }

      let result = 0
      switch (aggFunc) {
        case 'SUM':
          result = values.reduce((sum, val) => sum + val, 0)
          break
        case 'AVG':
          result = values.reduce((sum, val) => sum + val, 0) / values.length
          break
        case 'MAX':
          result = Math.max(...values)
          break
        case 'MIN':
          result = Math.min(...values)
          break
        case 'COUNT':
          result = values.length
          break
        default:
          result = 0
      }

      console.log(`聚合计算: ${tableName}.${fieldName}.${aggFunc} = ${result}`)
      return result.toString()
    },

    // 生成有效的数据库表名（移除特殊字符）
    generateValidTableName(configId) {
      // 移除小数点、连字符等特殊字符，只保留字母数字和下划线
      const cleanId = configId.replace(/[^a-zA-Z0-9_]/g, '_')
      return `TEMP_RESULT_${cleanId}`
    },

    // 存储指标执行结果
    storeIndicatorResult(indicator, result) {
      const tableName = this.generateValidTableName(indicator.configId)
      this.indicatorExecutionResults.set(tableName, {
        indicator: indicator,
        result: result,
        timestamp: new Date(),
        tableName: tableName
      })

      // 记录执行顺序
      if (!this.executionOrder.includes(indicator.configId)) {
        this.executionOrder.push(indicator.configId)
      }

      console.log('存储指标结果:', {
        指标名称: indicator.indicatorName,
        表名: tableName,
        记录数: result.records?.length || 0,
        执行顺序: this.executionOrder
      })
    },

    // 执行整个组合的指标（按顺序执行）
    async executeIndicatorCombination(combination) {
      if (!combination || !combination.indicators || combination.indicators.length === 0) {
        this.$message.warning('该组合没有配置指标')
        return
      }

      // 清空之前的执行结果
      this.indicatorExecutionResults.clear()
      this.executionOrder = []

      // 按执行顺序排序指标
      const sortedIndicators = [...combination.indicators].sort((a, b) =>
        (a.executionOrder || 0) - (b.executionOrder || 0)
      )

      console.log('开始执行指标组合:', {
        组合名称: combination.combinationName,
        指标数量: sortedIndicators.length,
        执行顺序: sortedIndicators.map(ind => `${ind.executionOrder}: ${ind.indicatorName}`)
      })

      this.$message.info(`开始执行组合"${combination.combinationName}"，共${sortedIndicators.length}个指标`)

      // 依次执行每个指标
      for (let i = 0; i < sortedIndicators.length; i++) {
        const indicator = sortedIndicators[i]

        try {
          console.log(`执行第${i + 1}个指标: ${indicator.indicatorName}`)

          // 执行单个指标
          await this.executeSingleIndicator(indicator, i + 1, sortedIndicators.length)

          // 短暂延迟，避免过快执行
          await new Promise(resolve => setTimeout(resolve, 500))

        } catch (error) {
          console.error(`指标"${indicator.indicatorName}"执行失败:`, error)
          this.$message.error(`指标"${indicator.indicatorName}"执行失败: ${error.message}`)
          break // 停止执行后续指标
        }
      }

      this.$message.success(`组合执行完成！共执行${this.executionOrder.length}个指标`)
    },

    // 执行单个指标
    async executeSingleIndicator(indicator, stepNumber, totalSteps) {
      return new Promise((resolve, reject) => {
        // 处理参数替换
        const parameterMapping = indicator.parameterMapping || indicator.parameterConfig || {}
        let processedSqlContent = indicator.sqlContent

        // 处理指标引用替换
        processedSqlContent = this.processIndicatorReferences(processedSqlContent, indicator)

        // 处理普通参数替换
        Object.keys(parameterMapping).forEach(paramName => {
          const paramValue = parameterMapping[paramName]
          if (paramValue !== null && paramValue !== undefined && paramValue !== '') {
            const regex = new RegExp(`\\$\\{${paramName}\\}`, 'g')
            processedSqlContent = processedSqlContent.replace(regex, paramValue)
          }
        })

        console.log(`执行指标 ${stepNumber}/${totalSteps}:`, {
          指标名称: indicator.indicatorName,
          指标编码: indicator.indicatorCode,
          处理后SQL: processedSqlContent.substring(0, 200) + '...'
        })

        // 模拟SQL执行
        setTimeout(() => {
          // 生成基于指标的测试数据
          const result = this.generateIndicatorTestData(indicator, stepNumber)

          // 存储指标执行结果
          this.storeIndicatorResult(indicator, result)

          console.log(`指标"${indicator.indicatorName}"执行完成，生成${result.records.length}条记录`)

          resolve(result)
        }, 1000 + Math.random() * 1000) // 随机执行时间
      })
    },

    // 生成指标测试数据
    generateIndicatorTestData(indicator, stepNumber) {
      const baseData = [
        { 企业名称: '示例云科技有限公司', 行业类型: '软件和信息技术服务业', 企业规模: '大型企业' },
        { 企业名称: '测试科技股份有限公司', 行业类型: '软件和信息技术服务业', 企业规模: '中型企业' },
        { 企业名称: '示例企业集团有限公司', 行业类型: '制造业', 企业规模: '大型企业' },
        { 企业名称: '创新科技有限公司', 行业类型: '软件和信息技术服务业', 企业规模: '小型企业' }
      ]

      // 根据指标类型生成不同的数据
      const records = baseData.map((base, index) => {
        const record = { ...base }

        // 添加指标特定字段
        record[`${indicator.indicatorName}_值`] = (Math.random() * 100).toFixed(2)
        record[`${indicator.indicatorName}_排名`] = index + 1
        record['统计日期'] = '2024-12-31'
        record['步骤序号'] = stepNumber
        record['指标编码'] = indicator.indicatorCode

        // 如果是后续指标，可以引用前面的结果
        if (stepNumber > 1) {
          record['引用前置结果'] = `基于第${stepNumber - 1}步结果计算`
          record['组合计算值'] = (Math.random() * 50 + stepNumber * 10).toFixed(2)
        }

        return record
      })

      // 生成列定义
      const columns = Object.keys(records[0] || {}).map(key => ({
        name: key,
        label: key
      }))

      return {
        records: records,
        columns: columns,
        total: records.length,
        executionTime: Math.floor(Math.random() * 500 + 100),
        stepNumber: stepNumber,
        indicatorName: indicator.indicatorName
      }
    },

    // 检查指标是否包含引用
    hasIndicatorReferences(indicator) {
      if (!indicator.sqlContent) return false

      const referencePatterns = [
        /\$\{PREV_RESULT\}/,
        /\$\{STEP_\d+_RESULT\}/,
        /\$\{[A-Z0-9_]+_RESULT\}/,
        /\$\{(PREV_RESULT|STEP_\d+_RESULT|[A-Z0-9_]+_RESULT)\.[A-Z0-9_\u4e00-\u9fa5%]+\.(SUM|AVG|MAX|MIN|COUNT)\}/
      ]

      return referencePatterns.some(pattern => pattern.test(indicator.sqlContent))
    },

    // 编辑指标
    handleEditIndicator(indicator, event) {
      console.log('编辑指标:', indicator)

      if (event) {
        event.stopPropagation()
      }

      // 设置编辑模式和当前指标数据
      this.editingIndicator = { ...indicator }
      this.indicatorConfigDialogVisible = true

      this.$message.info(`正在编辑指标: ${indicator.indicatorName}`)
    },

    // 删除指标
    handleDeleteIndicator(indicator, event) {
      console.log('删除指标:', indicator)

      if (event) {
        event.stopPropagation()
      }

      this.$confirm(
        `确定要删除指标"${indicator.indicatorName}"吗？此操作不可恢复。`,
        '删除指标',
        {
          confirmButtonText: '确定删除',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(() => {
        this.performDeleteIndicator(indicator)
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    },

    // 执行删除指标
    async performDeleteIndicator(indicator) {
      try {
        if (!this.selectedCombination || !this.selectedCombination.indicators) {
          this.$message.error('无法找到指标列表')
          return
        }

        // 从当前组合的指标列表中移除
        const indicatorIndex = this.selectedCombination.indicators.findIndex(
          ind => ind.configId === indicator.configId
        )

        if (indicatorIndex !== -1) {
          this.selectedCombination.indicators.splice(indicatorIndex, 1)

          // 重新排序执行顺序
          this.selectedCombination.indicators.forEach((ind, index) => {
            ind.executionOrder = index + 1
          })

          // 清除相关的执行结果缓存
          const tableName = this.generateValidTableName(indicator.configId)
          if (this.indicatorExecutionResults.has(tableName)) {
            this.indicatorExecutionResults.delete(tableName)
          }

          // 从执行顺序中移除
          const orderIndex = this.executionOrder.indexOf(indicator.configId)
          if (orderIndex !== -1) {
            this.executionOrder.splice(orderIndex, 1)
          }

          this.$message.success(`指标"${indicator.indicatorName}"已删除`)

          // 🔥 调用后端API删除指标
          try {
            console.log('调用后端删除接口:', indicator.configId)
            const response = await removeIndicatorFromCombination(indicator.configId)
            if (response.code !== 1) {
              throw new Error(response.msg || '删除失败')
            }
            console.log('后端删除成功')
          } catch (apiError) {
            console.error('后端删除失败:', apiError)
            this.$message.error('后端删除失败: ' + apiError.message)
            // 如果后端删除失败，恢复前端状态
            this.selectedCombination.indicators.splice(indicatorIndex, 0, indicator)
            // 重新排序执行顺序
            this.selectedCombination.indicators.forEach((ind, index) => {
              ind.executionOrder = index + 1
            })
            return
          }

        } else {
          this.$message.error('未找到要删除的指标')
        }
      } catch (error) {
        console.error('删除指标失败:', error)
        this.$message.error('删除指标失败: ' + error.message)
      }
    }
  },

  mounted() {
    // 🔥 新增：组件挂载时确保层级正确
    console.log('🎯 IndicatorCombinationDialog mounted')
    if (this.sqlResultDialogVisible) {
      this.$nextTick(() => {
        this.ensureSqlResultDialogOnTop()
      })
    }
  },

  beforeDestroy() {
    // 🔥 新增：组件销毁时清理遮罩层
    console.log('🔥 IndicatorCombinationDialog beforeDestroy called')
    this.clearSqlResultDialogMasks()
  }
}
</script>

<style scoped>
/* 全局样式，适配custom-class */
::v-deep .indicator-combination-dialog {
  .combination-container {
    height: 80vh;
    display: flex;
    flex-direction: column;
  }

  .toolbar {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 20px;
    padding-bottom: 12px;
    border-bottom: 1px solid #ebeef5;

    .search-box {
      margin-left: auto;
      width: 300px;
    }
  }
}

/* 局部样式，直接应用到组件内容 */
.combination-container {
  height: 80vh;
  display: flex;
  flex-direction: column;
}

.toolbar {
  display: flex;
  align-items: center;
  gap: 12px;
  padding-bottom: 12px;
  border-bottom: 1px solid #ebeef5;
}

.filter-form {
  margin-top: 16px;
  margin-bottom: 16px;
  padding: 16px;
  background-color: #f5f7fa;
  border-radius: 4px;

  ::v-deep .el-form-item {
    margin-bottom: 0;
  }

  ::v-deep .el-form-item__label {
    font-weight: 500;
    color: #606266;
  }
}

.main-content {
  flex: 1;
  display: flex;
  gap: 20px;
  min-height: 0;
}

.combination-list {
  width: 400px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  display: flex;
  flex-direction: column;

  .list-header {
    padding: 16px;
    border-bottom: 1px solid #ebeef5;
    display: flex;
    align-items: center;
    justify-content: space-between;

    h3 {
      margin: 0;
      font-size: 16px;
      font-weight: 600;
    }
  }

  .list-content {
    flex: 1;
    overflow-y: auto;
  }

  .list-pagination {
    padding: 8px 12px;
    border-top: 1px solid #ebeef5;
    display: flex;
    justify-content: center;
  }

  .combination-item {
    padding: 16px;
    border-bottom: 1px solid #f5f7fa;
    cursor: pointer;
    transition: all 0.3s;

    &:hover {
      background-color: #f5f7fa;
    }

    &.active {
      background-color: #ecf5ff;
      border-left: 3px solid #409eff;
    }

    &.auto-selected {
      animation: autoSelectHighlight 2s ease-in-out;
    }

    .item-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 8px;

      h4 {
        margin: 0;
        font-size: 14px;
        font-weight: 600;
        color: #303133;
      }
    }

    .item-content {
      .description {
        margin: 0 0 8px 0;
        font-size: 12px;
        color: #606266;
        line-height: 1.4;
      }

      .item-meta {
        display: flex;
        gap: 12px;

        .meta-item {
          font-size: 12px;
          color: #909399;
          display: flex;
          align-items: center;
          gap: 4px;
        }
      }
    }

    .item-actions {
      margin-top: 12px;
      display: flex;
      gap: 8px;
    }
  }
}

.detail-panel {
  flex: 1;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  overflow-y: auto;

  .empty-state {
    height: 100%;
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

  .combination-detail {
    padding: 20px;

    .detail-section {
      margin-bottom: 32px;

      &:last-child {
        margin-bottom: 0;
      }

      h3 {
        margin: 0 0 16px 0;
        font-size: 16px;
        font-weight: 600;
        color: #303133;
      }

      .section-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 16px;

        .header-actions {
          display: flex;
          gap: 8px;
        }

        h3 {
          margin: 0;
        }
      }
    }

    .indicators-list {
      .indicator-item {
        padding: 16px;
        border: 1px solid #ebeef5;
        border-radius: 4px;
        margin-bottom: 12px;
        transition: all 0.2s ease;

        &:last-child {
          margin-bottom: 0;
        }

        &.clickable-indicator {
          cursor: pointer;

          &:hover {
            border-color: #409eff;
            background-color: #f0f9ff;
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
          }
        }

        .indicator-header {
          display: flex;
          align-items: center;
          gap: 12px;
          margin-bottom: 8px;

          .order-badge {
            width: 24px;
            height: 24px;
            border-radius: 50%;
            background-color: #409eff;
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 12px;
            font-weight: 600;
          }

          h4 {
            margin: 0;
            flex: 1;
            font-size: 14px;
            font-weight: 600;
          }
        }

        .indicator-content {
          .description {
            margin: 0 0 8px 0;
            font-size: 12px;
            color: #606266;
            line-height: 1.4;
          }

          .indicator-meta {
            display: flex;
            gap: 16px;

            .meta-item {
              font-size: 12px;
              color: #909399;
              display: flex;
              align-items: center;
              gap: 4px;

              &.reference-info {
                color: #e6a23c;
                font-weight: 500;
              }
            }
          }
        }
      }
    }

    .empty-indicators {
      text-align: center;
      padding: 40px;
      color: #909399;

      i {
        font-size: 32px;
        margin-bottom: 12px;
      }
    }

    .execution-history {
      .execution-item {
        padding: 12px;
        border: 1px solid #ebeef5;
        border-radius: 4px;
        margin-bottom: 8px;

        &:last-child {
          margin-bottom: 0;
        }

        .execution-header {
          display: flex;
          align-items: center;
          justify-content: space-between;
          margin-bottom: 8px;

          .execution-name {
            font-size: 14px;
            font-weight: 600;
            color: #303133;
          }
        }

        .execution-meta {
          display: flex;
          gap: 16px;
          font-size: 12px;
          color: #909399;
        }
      }

      .empty-history {
        text-align: center;
        padding: 20px;
        color: #909399;
      }
    }
  }
}

/* 自动选择高亮动画 */
@keyframes autoSelectHighlight {
  0% {
    box-shadow: 0 0 0 0 rgba(64, 158, 255, 0.4);
  }
  50% {
    box-shadow: 0 0 0 10px rgba(64, 158, 255, 0.1);
  }
  100% {
    box-shadow: 0 0 0 0 rgba(64, 158, 255, 0);
  }
}

/* SQL结果对话框样式 */
::v-deep .sql-result-dialog {
  .sql-result-container {
    .indicator-info {
      background: #f8f9fa;
      padding: 16px;
      border-radius: 4px;
      margin-bottom: 20px;

      .info-item {
        margin-bottom: 8px;

        label {
          font-weight: 600;
          color: #606266;
          margin-right: 8px;
        }

        span {
          color: #303133;
        }
      }
    }

    .result-section {
      .result-stats {
        margin-bottom: 20px;
        padding: 16px;
        background: #f0f9ff;
        border-radius: 4px;
        border: 1px solid #e1f5fe;

        .stat-item {
          text-align: center;

          .stat-label {
            display: block;
            font-size: 12px;
            color: #909399;
            margin-bottom: 4px;
          }

          .stat-value {
            font-size: 18px;
            font-weight: 600;
            color: #409eff;
          }
        }
      }

      .loading-placeholder {
        text-align: center;
        padding: 40px;
        min-height: 100px;
      }

      .empty-result {
        padding: 40px;
        text-align: center;
      }

      .error-result {
        padding: 20px;
      }
    }
  }
}

</style>
