<template>
  <div>
    <!-- 🔥 执行指标组合对话框 - 确保在最上层显示 -->
    <el-dialog
    title="执行指标组合"
    :visible.sync="dialogVisible"
    width="900px"
    :close-on-click-modal="false"
    :modal-append-to-body="false"
    :append-to-body="true"
    :z-index="3500"
    class="execution-dialog sjmxgl-dialog-scope"
    custom-class="execution-dialog-wrapper"
    @close="handleClose"
    @opened="handleDialogOpened"
  >
    <div class="execution-container">
      <!-- 执行配置 -->
      <div class="execution-config">
        <h3>执行配置</h3>
        <el-form :model="executionForm" label-width="120px" size="mini">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="执行名称">
                <el-input v-model="executionForm.executionName" placeholder="请输入执行名称" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="执行模式">
                <el-select v-model="executionForm.executionMode" style="width: 100%;" disabled>
                  <el-option label="顺序执行" value="SEQUENCE" />
                  <el-option label="并行执行" value="PARALLEL" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-form-item label="参数配置">
            <div class="parameter-config">
              <div
                v-for="(param, key) in combinationParameters"
                :key="key"
                class="parameter-item"
              >
                <label>{{ key }}:</label>
                <el-input
                  v-model="executionForm.parameters[key]"
                  :placeholder="param.description || '请输入参数值'"
                  size="mini"
                />
                <span class="param-type">{{ param.type }}</span>
              </div>
              
              <div v-if="Object.keys(combinationParameters).length === 0" class="no-parameters">
                暂无参数配置
              </div>
            </div>
          </el-form-item>
          
          <el-form-item label="执行说明">
            <el-input
              v-model="executionForm.description"
              type="textarea"
              :rows="2"
              placeholder="请输入执行说明（可选）"
            />
          </el-form-item>
        </el-form>
      </div>

      <!-- 指标预览 -->
      <div class="indicators-preview">
        <h3>指标预览 ({{ enabledIndicators.length }}个)</h3>
        <div v-if="enabledIndicators.length > 0" class="preview-list">
          <div
            v-for="indicator in enabledIndicators"
            :key="indicator.configId"
            class="preview-item"
          >
            <div class="item-header">
              <span class="order-badge">{{ indicator.executionOrder }}</span>
              <span class="indicator-name">{{ indicator.indicatorName }}</span>
              <el-tag size="mini" type="success">启用</el-tag>
            </div>
            <div class="item-meta">
              <span>编码: {{ indicator.indicatorCode }}</span>
              <span v-if="indicator.dependsOn && indicator.dependsOn.length > 0">
                依赖: {{ indicator.dependsOn.join(', ') }}
              </span>
            </div>
          </div>
        </div>
        <div v-else class="empty-indicators">
          <el-empty description="暂无启用的指标">
            <span style="color: #999; font-size: 12px;">
              组合数据: {{ combination ? '已加载' : '未加载' }} |
              总指标数: {{ combination?.indicators?.length || 0 }} |
              启用数: {{ enabledIndicators.length }}
            </span>
          </el-empty>
        </div>
      </div>

      <!-- 执行状态 - 简化显示条件，只要有executionId就显示 -->
      <div v-if="executionId || isExecuting || hasExecuted" class="execution-status">
        <h3>执行状态</h3>

        <!-- 执行进度条 - 始终显示，执行结束后保持最终状态 -->
        <div class="progress-section">
          <div class="status-header">
            <el-progress
              :percentage="finalExecutionProgress"
              :status="getProgressStatus()"
              :stroke-width="20"
            />
            <div class="status-info">
              <span class="status-text">{{ getExecutionStatusText() }}</span>
              <span v-if="executionResult" class="completion-time">
                完成时间: {{ executionResult.endTime || '未知' }}
              </span>
            </div>
            <div v-if="isExecuting" class="status-actions">
              <el-button type="danger" size="mini" @click="cancelExecution">
                取消执行
              </el-button>
              <el-button
                type="info"
                size="mini"
                @click="toggleExecutionLogs"
                :icon="showExecutionLogs ? 'el-icon-view' : 'el-icon-document'"
              >
                {{ showExecutionLogs ? '隐藏日志' : '显示日志' }}
              </el-button>
            </div>
            <div v-if="!isExecuting && executionResult" class="result-actions">
              <el-button type="primary" size="mini" @click="showExecutionResult">
                查看执行结果
              </el-button>
              <el-button size="mini" @click="downloadExecutionReport">
                下载报告
              </el-button>
              <el-button
                type="info"
                size="mini"
                @click="toggleExecutionLogs"
                :icon="showExecutionLogs ? 'el-icon-view' : 'el-icon-document'"
              >
                {{ showExecutionLogs ? '隐藏日志' : '显示日志' }}
              </el-button>
            </div>
          </div>
        </div>

        <!-- 执行日志 -->
        <div v-if="executionLogs.length > 0 && showExecutionLogs" class="execution-log">
          <div class="log-header">
            <h4><i class="el-icon-document"></i> 执行日志</h4>
            <div class="log-stats">
              <el-tag size="mini" type="info">{{ executionLogs.length }} 条记录</el-tag>
              <el-button type="text" size="mini" @click="clearLogs">
                <i class="el-icon-delete"></i> 清空
              </el-button>
            </div>
          </div>
          <div class="log-container">
            <div
              v-for="log in executionLogs"
              :key="log.timestamp"
              :class="['log-item', log.level.toLowerCase()]"
            >
              <div class="log-icon">
                <i :class="getLogIcon(log.level)"></i>
              </div>
              <div class="log-content">
                <div class="log-message">{{ log.message }}</div>
                <div class="log-meta">
                  <span class="timestamp">{{ formatTime(log.timestamp) }}</span>
                  <span class="level">{{ log.level }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 执行结果 - 修复显示条件 -->
        <div v-if="executionResult && hasExecuted" class="execution-result">
          <div class="result-summary">
            <el-descriptions :column="4" border>
              <el-descriptions-item label="执行状态">
                <el-tag :type="getStatusType(executionResult.status)">
                  {{ getStatusText(executionResult.status) }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="总耗时">
                {{ formatDuration(executionResult.totalDuration) }}
              </el-descriptions-item>
              <el-descriptions-item label="成功数量">
                {{ executionResult.successCount || 0 }}
              </el-descriptions-item>
              <el-descriptions-item label="失败数量">
                {{ executionResult.failedCount || 0 }}
              </el-descriptions-item>
            </el-descriptions>
          </div>
          
          <div class="result-details">
            <el-tabs v-model="activeResultTab">
              <el-tab-pane label="指标结果" name="indicators">
                <div class="indicator-results">
                  <div
                    v-for="(result, index) in indicatorResults"
                    :key="`result-${index}-${result.resultId || result.configId}`"
                    class="result-item"
                  >
                    <div class="result-header">
                      <h4>{{ result.indicatorName }}</h4>
                      <el-tag :type="getStatusType(result.status)" size="mini">
                        {{ getStatusText(result.status) }}
                      </el-tag>
                      <span class="duration">{{ formatDuration(result.executionDuration) }}</span>
                    </div>
                    
                    <div class="result-content">
                      <div class="result-stats">
                        <span>记录数: {{ result.recordCount || 0 }}</span>
                        <span v-if="result.errorMessage">错误: {{ result.errorMessage }}</span>
                      </div>
                      
                      <div class="result-actions">
                        <el-button type="text" size="mini" @click="viewResultDetail(result)">
                          查看详情
                        </el-button>
                        <el-button type="text" size="mini" @click="downloadResult(result)">
                          下载结果
                        </el-button>
                      </div>
                    </div>
                  </div>
                </div>
              </el-tab-pane>
              
              <el-tab-pane label="交集分析" name="intersection">
                <div class="intersection-analysis">
                  <div class="analysis-config">
                    <el-button type="primary" size="mini" @click="executeIntersectionAnalysis">
                      执行交集分析
                    </el-button>
                    <el-button type="info" size="mini" @click="showIntersectionDebugInfo" style="margin-left: 8px;">
                      交集信息
                    </el-button>
                    <el-select
                      v-model="selectedIndicatorsForIntersection"
                      multiple
                      placeholder="选择要分析的指标"
                      style="width: 300px; margin-left: 12px;"
                      :popper-append-to-body="true"
                      popper-class="intersection-select-dropdown execution-dialog-dropdown"
                      :z-index="3600"
                      @visible-change="handleIntersectionSelectVisibleChange"
                      @focus="ensureIntersectionSelectDropdownVisible"
                    >
                      <el-option
                        v-for="(result, index) in successfulResults"
                        :key="`option-${index}-${result.resultId || result.configId}`"
                        :label="result.indicatorName"
                        :value="result.resultId"
                      />
                    </el-select>
                  </div>
                  
                  <div v-if="intersectionResult" class="intersection-result">
                    <h4>交集分析结果</h4>
                    <el-descriptions :column="2" border>
                      <el-descriptions-item label="交集记录数">
                        {{ intersectionResult.resultCount || 0 }}
                      </el-descriptions-item>
                      <el-descriptions-item label="分析时间">
                        {{ intersectionResult.analysisTime }}
                      </el-descriptions-item>
                      <el-descriptions-item label="分析ID">
                        {{ intersectionResult.analysisId }}
                      </el-descriptions-item>
                      <el-descriptions-item label="分析类型">
                        {{ intersectionResult.intersectionType }}
                      </el-descriptions-item>
                    </el-descriptions>

                    <div class="intersection-actions" style="margin-top: 12px;">
                      <el-button type="primary" size="mini" @click="viewIntersectionDetail">
                        查看交集详情
                      </el-button>
                      <el-button type="primary" size="mini" plain @click="showIntersectionDetailDialog">
                        详情对话框
                      </el-button>
                      <el-button type="primary" size="mini" @click="exportIntersectionToExcelLocal">
                        <i class="el-icon-download"></i> 导出Excel
                      </el-button>
                      <el-button type="info" size="mini" @click="showIntersectionDebugInfo">
                        交集信息
                      </el-button>
                    </div>
                  </div>
                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
        </div>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button
        v-if="!isExecuting && !executionResult"
        type="primary"
        :loading="starting"
        @click="startExecution"
      >
        开始执行
      </el-button>
      <el-button
        v-if="executionResult"
        type="success"
        @click="saveExecutionResult"
      >
        保存结果
      </el-button>
    </div>

    <!-- 结果详情对话框 -->
    <el-dialog
      title="指标结果详情"
      :visible.sync="resultDetailVisible"
      width="95%"
      top="5vh"
      append-to-body
      :close-on-click-modal="false"
      class="result-detail-dialog"
    >
      <div v-if="currentResultDetail" class="result-detail-container">
        <!-- 数据统计信息栏 -->
        <div class="result-stats-bar">
          <div class="stats-info">
            <el-tag type="info" size="small">
              <i class="el-icon-document"></i>
              {{ currentResultDetail.records?.length || 0 }} 条记录
            </el-tag>
            <el-tag type="success" size="small">
              <i class="el-icon-menu"></i>
              {{ currentResultDetail.columns?.length || 0 }} 个字段
            </el-tag>
            <el-tag type="warning" size="small">
              <i class="el-icon-collection"></i>
              总计 {{ currentResultDetail.total || 0 }} 条
            </el-tag>
          </div>
          <div class="stats-actions">
            <el-button type="text" size="small" @click="exportResultData">
              <i class="el-icon-download"></i> 导出数据
            </el-button>
            <el-button type="text" size="small" @click="refreshResultData">
              <i class="el-icon-refresh"></i> 刷新
            </el-button>
          </div>
        </div>

        <!-- 表格容器 -->
        <div class="table-container">
          <el-table
            :data="currentResultDetail.records"
            border
            stripe
            height="100%"
            v-loading="false"
            :header-cell-style="{ background: '#f5f7fa', color: '#606266', fontWeight: 'bold' }"
            :cell-style="{ padding: '8px 0' }"
            style="width: 100%;"
          >
            <el-table-column
              v-for="column in currentResultDetail.columns"
              :key="column.field"
              :prop="column.field"
              :label="column.label"
              :min-width="getColumnWidth(column)"
              show-overflow-tooltip
              :sortable="true"
            >
              <template slot-scope="scope">
                <span :class="getColumnClass(column.field, scope.row[column.field])">
                  {{ formatCellValue(column.field, scope.row[column.field]) }}
                </span>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 分页容器 -->
        <div class="pagination-container">
          <el-pagination
            v-if="currentResultDetail.total > 0"
            :current-page="resultDetailPage"
            :page-size="20"
            :total="currentResultDetail.total"
            layout="total, sizes, prev, pager, next, jumper"
            :page-sizes="[10, 20, 50, 100]"
            @current-change="handleResultDetailPageChange"
            @size-change="handleResultDetailSizeChange"
            background
          />
        </div>
      </div>

      <!-- 无数据提示 -->
      <div v-else class="no-data-container">
        <div class="no-data-content">
          <i class="el-icon-document" style="font-size: 64px; color: #ddd; margin-bottom: 16px;"></i>
          <h3 style="color: #999; margin: 0 0 8px 0;">暂无结果详情数据</h3>
          <p style="color: #ccc; margin: 0;">请确保指标执行成功并生成了结果数据</p>
        </div>
      </div>
    </el-dialog>

    </el-dialog>

    <!-- 🔥 修复：将交集详情对话框移到执行对话框外部，避免层级冲突 -->
    <IntersectionDetailDialog
      :visible.sync="intersectionDetailDialogVisible"
      :intersection-result="intersectionResult"
      @close="handleIntersectionDetailDialogClose"
    />
  </div>
</template>

<style scoped>
/* 执行状态区域样式优化 */
.execution-status {
  margin-top: 20px;
  padding: 16px;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  border-radius: 8px;
  border: 1px solid #dee2e6;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.execution-status h3 {
  margin: 0 0 16px 0;
  color: #495057;
  font-size: 16px;
  font-weight: 600;
  display: flex;
  align-items: center;
}

.execution-status h3::before {
  content: '⚡';
  margin-right: 8px;
  font-size: 18px;
}

/* 进度区域样式 */
.progress-section {
  background: white;
  padding: 16px;
  border-radius: 6px;
  border: 1px solid #e9ecef;
  margin-bottom: 16px;
}

.status-header {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.status-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 8px;
}

.status-text {
  font-weight: 600;
  color: #495057;
  font-size: 14px;
}

.completion-time {
  color: #6c757d;
  font-size: 12px;
}

/* 操作按钮样式 */
.status-actions,
.result-actions {
  display: flex;
  gap: 8px;
  margin-top: 12px;
  flex-wrap: wrap;
}

/* 执行日志样式优化 */
.execution-log {
  background: white;
  border-radius: 6px;
  border: 1px solid #e9ecef;
  overflow: hidden;
  animation: slideDown 0.3s ease-out;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.log-header {
  background: #f8f9fa;
  padding: 12px 16px;
  border-bottom: 1px solid #e9ecef;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.log-header h4 {
  margin: 0;
  color: #495057;
  font-size: 14px;
  font-weight: 600;
}

.log-stats {
  display: flex;
  gap: 8px;
  align-items: center;
}

.log-container {
  max-height: 300px;
  overflow-y: auto;
  padding: 8px;
}

.log-item {
  display: flex;
  align-items: flex-start;
  padding: 8px 12px;
  margin-bottom: 4px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.log-item:hover {
  background-color: #f8f9fa;
}

.log-item.success {
  border-left: 3px solid #28a745;
  background-color: #f8fff9;
}

.log-item.info {
  border-left: 3px solid #17a2b8;
  background-color: #f0fdff;
}

.log-item.warn {
  border-left: 3px solid #ffc107;
  background-color: #fffdf0;
}

.log-item.error {
  border-left: 3px solid #dc3545;
  background-color: #fff5f5;
}

.log-icon {
  margin-right: 8px;
  margin-top: 2px;
}

.log-content {
  flex: 1;
}

.log-message {
  color: #495057;
  font-size: 13px;
  line-height: 1.4;
  margin-bottom: 4px;
}

.log-meta {
  display: flex;
  gap: 12px;
  font-size: 11px;
  color: #6c757d;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .status-actions,
  .result-actions {
    flex-direction: column;
  }

  .status-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;
  }
}
</style>

<script>
import {
  executeCombination,
  getExecutionStatus,
  getExecutionResult,
  cancelExecution,
  getIndicatorResultDetail,
  executeIntersectionAnalysis,
  exportAnalysisResult
} from '@/api/mxgl'
import IntersectionDetailDialog from './IntersectionDetailDialog.vue'

export default {
  name: 'ExecutionDialog',
  components: {
    IntersectionDetailDialog
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
      
      // 执行表单
      executionForm: {
        executionName: '',
        executionMode: 'SEQUENCE',
        parameters: {},
        description: ''
      },
      
      // 执行状态
      isExecuting: false,
      hasExecuted: false, // 是否已经执行过
      starting: false,
      executionId: '',
      executionProgress: 0,
      executionStatus: '',
      executionLogs: [],
      showExecutionLogs: false, // 控制执行日志的显示/隐藏

      // 轮询状态
      isPolling: false,
      pollingStartTime: null,
      
      // 执行结果
      executionResult: null,
      indicatorResults: [],
      activeResultTab: 'indicators',
      
      // 交集分析
      selectedIndicatorsForIntersection: [],
      intersectionResult: null,
      intersectionDetailDialogVisible: false,
      
      // 结果详情
      resultDetailVisible: false,
      currentResultDetail: null,
      currentResultId: null,  // 保存当前查看详情的resultId
      resultDetailPage: 1,
      
      // 轮询定时器
      statusTimer: null,
      maxPollingTimer: null
    }
  },
  computed: {
    combinationParameters() {
      if (!this.combination || !this.combination.parameterConfig) {
        return {}
      }
      
      try {
        return typeof this.combination.parameterConfig === 'string'
          ? JSON.parse(this.combination.parameterConfig)
          : this.combination.parameterConfig
      } catch (error) {
        console.error('解析参数配置失败:', error)
        return {}
      }
    },
    
    enabledIndicators() {
      if (!this.combination || !this.combination.indicators) {
        return []
      }

      return this.combination.indicators
        .filter(indicator => indicator.isEnabled)
        .sort((a, b) => a.executionOrder - b.executionOrder)
    },

    // 最终显示的执行进度
    finalExecutionProgress() {
      if (this.isExecuting) {
        return Math.max(this.executionProgress, 0) // 确保进度不为负数
      } else if (this.hasExecuted && this.executionResult) {
        // 执行结束后根据状态显示进度
        const status = this.executionResult.status
        if (status === 'SUCCESS') return 100
        if (status === 'PARTIAL_SUCCESS') return Math.max(this.executionProgress || 80, 50) // 部分成功至少显示50%
        if (status === 'FAILED') return Math.max(this.executionProgress || 0, 0)
        if (status === 'CANCELLED') return Math.max(this.executionProgress || 0, 0)
      } else if (this.executionId && !this.isExecuting) {
        return 0 // 有执行ID但未执行时显示0%
      }
      return Math.max(this.executionProgress, 0)
    },

    successfulResults() {
      // 🔥 增强：过滤成功的结果并确保有有效的resultId
      const results = this.indicatorResults.filter(result => {
        const isSuccess = result.status === 'SUCCESS'
        const hasValidResultId = result.resultId && typeof result.resultId === 'string' && result.resultId.trim().length > 0

        if (isSuccess && !hasValidResultId) {
          console.warn('⚠️ 成功的指标结果缺少有效的resultId:', result)
        }

        return isSuccess && hasValidResultId
      })

      console.log('📊 成功的指标结果:', results.map(r => ({
        indicatorName: r.indicatorName,
        resultId: r.resultId,
        status: r.status
      })))

      return results
    }
  },
  watch: {
    visible: {
      handler(val) {
        this.dialogVisible = val
        if (val) {
          this.initExecutionForm()
        }
      },
      immediate: true
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
      if (!val) {
        this.clearStatusTimer()
      } else {
        // 🔥 新增：对话框打开时确保在最上层
        this.$nextTick(() => {
          this.ensureDialogOnTop()
          this.ensureIntersectionSelectDropdownVisible()
        })
      }
    },

    // 🔥 新增：监听交集分析选择的指标变化
    selectedIndicatorsForIntersection: {
      handler(val) {
        console.log('🔽 交集分析选择的指标变化:', val)
        if (val && val.length > 0) {
          this.$nextTick(() => {
            this.ensureIntersectionSelectDropdownVisible()
          })
        }
      },
      deep: true
    },
    combination: {
      handler(newCombination) {
        console.log('组合数据变化:', newCombination)
        if (newCombination) {
          this.initExecutionForm()
          console.log('指标数量:', newCombination.indicators?.length || 0)
        }
      },
      immediate: true,
      deep: true
    }
  },
  methods: {
    // 初始化执行表单
    initExecutionForm() {
      if (this.combination) {
        this.executionForm = {
          executionName: `${this.combination.combinationName}-${this.formatTime(new Date())}`,
          executionMode: this.combination.executionMode || 'SEQUENCE',
          parameters: this.initParameters(),
          description: ''
        }
      }
      
      // 重置状态
      this.isExecuting = false
      this.executionResult = null
      this.indicatorResults = []
      this.intersectionResult = null
      this.executionLogs = []
      this.executionProgress = 0
      this.showExecutionLogs = false // 重置日志显示状态为隐藏
    },

    // 初始化参数
    initParameters() {
      const params = {}
      Object.keys(this.combinationParameters).forEach(key => {
        const param = this.combinationParameters[key]
        params[key] = param.defaultValue || ''
      })
      return params
    },

    // 开始执行
    async startExecution() {
      try {
        this.starting = true
        
        const response = await executeCombination({
          combinationId: this.combination.combinationId,
          executionName: this.executionForm.executionName,
          executionMode: this.executionForm.executionMode,
          parameters: JSON.stringify(this.executionForm.parameters),
          description: this.executionForm.description
        })
        
        if (response.code === 1) {
          this.executionId = response.data.executionId
          this.isExecuting = true
          this.hasExecuted = false // 重置执行状态
          this.executionProgress = 0
          this.executionStatus = 'RUNNING'
          this.executionLogs = []
          this.executionResult = null // 清空之前的结果
          this.showExecutionLogs = false // 默认隐藏日志，用户需要点击按钮显示

          this.addLog('INFO', '开始执行指标组合分析...')
          this.startStatusPolling()

          this.$message.success('执行已开始')
          console.log('执行状态已设置:', {
            isExecuting: this.isExecuting,
            hasExecuted: this.hasExecuted,
            executionId: this.executionId
          })
        } else {
          this.$message.error(response.msg || '启动执行失败')
        }
      } catch (error) {
        console.error('启动执行失败:', error)
        this.$message.error('启动执行失败')
      } finally {
        this.starting = false
      }
    },

    // 开始状态轮询 - 使用递归调用而非定时器
    startStatusPolling() {
      // 先清除可能存在的定时器
      this.clearStatusTimer()

      // 设置轮询开始时间，用于超时检查
      this.pollingStartTime = Date.now()
      this.isPolling = true

      this.addLog('INFO', '开始智能状态轮询...')

      // 立即开始第一次检查
      this.performStatusCheck()
    },

    // 执行状态检查 - 递归调用实现智能轮询
    async performStatusCheck() {
      // 检查是否应该停止轮询
      if (!this.isPolling || !this.isExecuting) {
        console.log('轮询已停止或执行已结束')
        return
      }

      // 检查超时（10分钟）
      const elapsed = Date.now() - this.pollingStartTime
      if (elapsed > 10 * 60 * 1000) {
        this.addLog('WARN', '轮询超时（10分钟），自动停止')
        this.stopPolling()
        return
      }

      try {
        console.log(`执行状态检查: ${this.executionId}`)
        const response = await getExecutionStatus(this.executionId)

        if (response.code === 1) {
          const status = response.data
          console.log('🔄 状态接口响应:', status)
          console.log('🔄 状态接口完整数据:', JSON.stringify(status, null, 2))

          // 检查状态接口是否包含indicators数据
          if (status.indicators && status.indicators.length > 0) {
            console.log('🔄 状态接口包含indicators数据:', status.indicators.length, '个指标')
            console.log('🔄 状态接口indicators详情:', JSON.stringify(status.indicators, null, 2))
          } else {
            console.log('🔄 状态接口不包含indicators数据')
          }

          // 更新状态
          this.executionStatus = status.status
          this.executionProgress = status.progress || 0

          // 添加状态日志
          this.addLog('INFO', `执行进度: ${status.progress || 0}% - ${status.currentIndicator || '准备中'}`)

          // 处理服务器返回的日志
          if (status.logs && status.logs.length > 0) {
            status.logs.forEach(log => {
              if (!this.executionLogs.find(l => l.timestamp === log.timestamp)) {
                this.executionLogs.push(log)
              }
            })
          }

          // 根据状态决定是否继续轮询
          const shouldContinue = this.shouldContinuePolling(status)

          if (shouldContinue) {
            // 继续轮询，延迟2秒后再次检查
            console.log(`状态为 ${status.status}，2秒后继续轮询`)
            setTimeout(() => {
              this.performStatusCheck()
            }, 2000)
          } else {
            // 停止轮询
            console.log(`状态为 ${status.status}，停止轮询`)
            this.stopPolling()
          }
        } else {
          // 接口返回错误，继续轮询
          console.log('状态查询失败，继续轮询:', response.msg)
          this.addLog('ERROR', `状态查询失败: ${response.msg || '未知错误'}，继续轮询`)

          // 延迟5秒后重试
          setTimeout(() => {
            this.performStatusCheck()
          }, 5000)
        }
      } catch (error) {
        console.error('状态查询异常:', error)
        this.addLog('ERROR', `状态查询异常: ${error.message}`)

        // 检查是否是网络错误或跨域错误
        if (error.message.includes('strict-origin-when-cross-origin') ||
            error.message.includes('Network Error') ||
            error.message.includes('CORS')) {
          this.addLog('WARN', '检测到网络/跨域错误，停止轮询')
          this.stopPolling()
          return
        }

        // 其他错误，延迟5秒后重试
        setTimeout(() => {
          this.performStatusCheck()
        }, 5000)
      }
    },

    // 判断是否应该继续轮询
    shouldContinuePolling(status) {
      const runningStatuses = ['RUNNING', 'PENDING', 'STARTING', 'PROCESSING']
      const completedStatuses = ['SUCCESS', 'FAILED', 'CANCELLED', 'PARTIAL_SUCCESS', 'COMPLETED']

      // 如果是运行中状态，继续轮询
      if (runningStatuses.includes(status.status)) {
        console.log(`执行状态为 ${status.status}，继续轮询`)
        return true
      }

      // 如果是完成状态，尝试获取结果后停止轮询
      if (completedStatuses.includes(status.status)) {
        console.log(`检测到完成状态: ${status.status}，准备停止轮询`)
        this.handleExecutionCompleted(status)
        return false
      }

      // 未知状态，继续轮询但记录警告
      console.log(`未知执行状态: ${status.status}，继续轮询`)
      this.addLog('WARN', `未知执行状态: ${status.status}，继续轮询`)
      return true
    },

    // 处理执行完成
    async handleExecutionCompleted(status) {
      this.addLog('INFO', `检测到完成状态: ${status.status}，开始处理执行结果...`)

      try {
        // 检查状态接口是否已包含结果数据
        if (status.indicators && status.indicators.length > 0) {
          console.log('🔍 状态接口包含结果数据，检查数据结构')
          console.log('🔍 状态接口indicators数据:', JSON.stringify(status.indicators, null, 2))

          // 检查状态接口的数据是否包含resultId
          const hasResultId = status.indicators.some(indicator =>
            indicator.resultId || indicator.RESULT_ID || indicator.result_id
          )

          if (hasResultId) {
            console.log('✅ 状态接口数据包含resultId，直接使用')
            this.isExecuting = false
            this.hasExecuted = true

            const resultData = {
              ...status,
              indicatorResults: status.indicators || [],
              indicators: status.indicators || []
            }
            this.processExecutionResult({ code: 1, data: resultData })
            this.addLog('SUCCESS', '执行结果加载完成')
            return
          } else {
            console.log('⚠️ 状态接口数据缺少resultId，强制调用执行结果接口')
            this.addLog('WARN', '状态接口数据不完整，获取详细执行结果')
          }
        }

        // 状态接口没有结果数据，尝试获取执行结果
        console.log('状态接口无结果数据，尝试获取执行结果')
        const resultResponse = await getExecutionResult(this.executionId)

        if (resultResponse.code === 1 && resultResponse.data) {
          console.log('执行结果获取成功')
          this.isExecuting = false
          this.hasExecuted = true

          this.processExecutionResult(resultResponse)
          this.addLog('SUCCESS', '执行结果加载完成')
        } else {
          console.log('执行结果获取失败:', resultResponse.msg)
          this.addLog('WARN', `执行结果获取失败: ${resultResponse.msg || '未知原因'}`)

          // 即使获取结果失败，也要停止执行状态
          this.isExecuting = false
          this.hasExecuted = true
        }
      } catch (error) {
        console.error('处理执行完成失败:', error)
        this.addLog('ERROR', `处理执行完成失败: ${error.message}`)

        // 发生异常也要停止执行状态
        this.isExecuting = false
        this.hasExecuted = true
      }
    },

    // 停止轮询
    stopPolling() {
      this.isPolling = false
      this.addLog('INFO', '状态轮询已停止')
      console.log('轮询已停止')
    },



    // 处理执行结果数据
    processExecutionResult(resultResponse) {
      try {
        console.log('🔍 开始处理执行结果:', resultResponse)
        console.log('🔍 完整响应数据结构:', JSON.stringify(resultResponse, null, 2))

        this.executionResult = resultResponse.data

        // 获取指标结果数据，支持多种数据格式
        let indicators = resultResponse.data.indicators ||
                        resultResponse.data.indicatorResults ||
                        resultResponse.data.indicatorList || []

        console.log('🔍 提取的指标数组:', indicators)
        console.log('🔍 指标数组长度:', indicators.length)
        console.log('🔍 指标数组详细结构:', JSON.stringify(indicators, null, 2))

        // 检查每个指标的原始字段
        indicators.forEach((indicator, index) => {
          console.log(`🔍 指标 ${index + 1} 原始数据:`, indicator)
          console.log(`🔍 指标 ${index + 1} 所有字段:`, Object.keys(indicator))
          console.log(`🔍 指标 ${index + 1} resultId相关字段检查:`, {
            resultId: indicator.resultId,
            RESULT_ID: indicator.RESULT_ID,
            result_id: indicator.result_id,
            executionResultId: indicator.executionResultId,
            id: indicator.id
          })
        })

        // 确保指标数据格式正确
        this.indicatorResults = indicators.map((indicator, index) => {
          // 详细检查所有可能的ID字段
          console.log(`指标 ${index + 1} 原始数据所有字段:`, Object.keys(indicator))
          console.log(`指标 ${index + 1} 原始数据:`, indicator)

          // 尝试获取真正的resultId
          let actualResultId = indicator.resultId ||
                              indicator.executionResultId ||
                              indicator.id ||
                              indicator.RESULT_ID ||           // 大写字段名
                              indicator.result_id ||           // 下划线字段名
                              null;

          // 如果没有找到resultId，生成一个临时的ID
          if (!actualResultId || actualResultId === 'unknown') {
            // 使用配置ID + 执行ID + 时间戳 + 索引的组合作为临时resultId，确保唯一性
            const configId = indicator.configId || indicator.CONFIG_ID || 'unknown';
            const executionId = this.executionId || 'unknown';
            const timestamp = Date.now();
            const randomSuffix = Math.random().toString(36).substr(2, 9);
            actualResultId = `TEMP_${configId}_${executionId}_${index}_${timestamp}_${randomSuffix}`;
            console.warn(`指标 ${index + 1} 缺少resultId，生成临时ID:`, actualResultId);
          }

          const processedIndicator = {
            resultId: actualResultId,
            configId: indicator.configId || indicator.CONFIG_ID || 'unknown',
            indicatorName: indicator.indicatorName || indicator.INDICATOR_NAME || '未知指标',
            indicatorCode: indicator.indicatorCode || indicator.configId || 'UNKNOWN',
            status: indicator.status || indicator.STATUS || 'SUCCESS',
            recordCount: indicator.resultCount || indicator.recordCount || indicator.RESULT_COUNT || 0,
            executionDuration: indicator.duration || indicator.executionDuration || indicator.DURATION || 0,
            errorMessage: indicator.errorMessage || indicator.ERROR_MESSAGE || null
          }

          console.log(`指标 ${index + 1} 数据映射详情:`, {
            原始字段检查: {
              resultId: indicator.resultId,
              executionResultId: indicator.executionResultId,
              id: indicator.id,
              RESULT_ID: indicator.RESULT_ID,
              result_id: indicator.result_id,
              executionId: indicator.executionId,
              configId: indicator.configId
            },
            最终映射结果: {
              resultId: processedIndicator.resultId,
              configId: processedIndicator.configId,
              indicatorName: processedIndicator.indicatorName
            }
          })

          return processedIndicator
        })

        // 确保执行结果有基本信息
        if (!this.executionResult.status) {
          this.executionResult.status = 'SUCCESS'
        }
        if (!this.executionResult.successCount) {
          this.executionResult.successCount = this.indicatorResults.filter(r => r.status === 'SUCCESS').length
        }
        if (!this.executionResult.failedCount) {
          this.executionResult.failedCount = this.indicatorResults.filter(r => r.status === 'FAILED').length
        }

        console.log('处理后的执行结果:', this.executionResult)
        console.log('处理后的指标结果:', this.indicatorResults)

        // 强制触发视图更新
        this.$nextTick(() => {
          console.log('执行结果显示状态:', {
            hasExecutionResult: !!this.executionResult,
            hasExecuted: this.hasExecuted,
            isExecuting: this.isExecuting,
            indicatorCount: this.indicatorResults.length
          })
        })

      } catch (error) {
        console.error('处理执行结果失败:', error)
        this.addLog('ERROR', '处理执行结果失败: ' + error.message)
      }
    },

    // 加载执行结果
    async loadExecutionResult() {
      try {
        console.log('🚀 开始加载执行结果')
        console.log('🚀 当前executionId:', this.executionId)
        console.log('🚀 executionId类型:', typeof this.executionId)
        console.log('🚀 executionId长度:', this.executionId ? this.executionId.length : 'null')

        if (!this.executionId) {
          console.error('❌ executionId为空，无法加载执行结果')
          this.addLog('ERROR', 'executionId为空，无法加载执行结果')
          return
        }

        const response = await getExecutionResult(this.executionId)
        console.log('🚀 执行结果API响应:', response)
        console.log('🚀 响应数据结构:', JSON.stringify(response, null, 2))

        if (response.code === 1) {
          // 使用统一的结果处理方法
          this.processExecutionResult(response)

          this.addLog('INFO', `执行完成，状态: ${this.getStatusText(this.executionResult.status)}`)
        } else {
          console.error('❌ 获取执行结果失败:', response.msg)
          this.addLog('ERROR', `获取执行结果失败: ${response.msg}`)
        }
      } catch (error) {
        console.error('❌ 加载执行结果失败:', error)
        this.addLog('ERROR', '加载执行结果失败')
      }
    },

    // 取消执行
    async cancelExecution() {
      try {
        await this.$confirm('确定要取消当前执行吗？', '确认取消', {
          type: 'warning'
        })
        
        const response = await cancelExecution(this.executionId)
        if (response.code === 1) {
          this.addLog('WARN', '执行已被用户取消')
          this.$message.success('执行已取消')
        } else {
          this.$message.error(response.msg || '取消失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('取消执行失败:', error)
          this.$message.error('取消执行失败')
        }
      }
    },

    // 查看结果详情
    async viewResultDetail(result) {
      try {
        console.log('查看结果详情:', result)
        console.log('传递的resultId:', result.resultId)
        console.log('配置ID:', result.configId)

        // 检查resultId是否有效
        if (!result.resultId || result.resultId === 'unknown') {
          this.$message.error('无法获取结果详情：缺少有效的结果ID')
          console.error('resultId无效:', result.resultId)
          this.showResultDetailError('缺少有效的结果ID')
          return
        }

        // 检查是否是临时生成的ID（以TEMP_开头）
        if (result.resultId.startsWith('TEMP_')) {
          console.warn('检测到临时生成的resultId，尝试使用配置ID查询:', result.configId)

          // 尝试使用配置ID查询
          if (result.configId && result.configId !== 'unknown') {
            try {
              const response = await getIndicatorResultDetail(result.configId, {
                pageNum: this.resultDetailPage,
                pageSize: 20
              })

              if (response.code === 1) {
                this.currentResultDetail = response.data
                this.resultDetailVisible = true
                this.$message.success('使用配置ID成功获取结果详情')
                return
              }
            } catch (error) {
              console.warn('使用配置ID查询失败:', error)
            }
          }

          // 如果配置ID也失败，显示错误信息
          this.$message.error('无法获取结果详情：后端数据缺少有效的结果ID')
          this.showResultDetailError('后端数据缺少有效的结果ID，请检查执行结果是否完整')
          return
        }

        const response = await getIndicatorResultDetail(result.resultId, {
          pageNum: this.resultDetailPage,
          pageSize: 20
        })

        console.log('🎯 结果详情响应:', response)
        console.log('🎯 响应数据结构:', JSON.stringify(response.data, null, 2))

        if (response.code === 1) {
          // 保存当前查看的resultId，用于分页
          this.currentResultId = result.resultId
          console.log('🎯 保存resultId用于分页:', this.currentResultId)

          // 处理后端返回的数据结构，转换为前端期望的格式
          const backendData = response.data

          this.currentResultDetail = {
            // 数据记录：后端用data字段，前端期望records字段
            records: backendData.data || backendData.records || [],

            // 列定义：处理列信息，添加必要的字段
            columns: (backendData.columns || []).map(col => ({
              field: col.name || col.field,
              label: col.description || col.label || col.name,
              width: col.width || 120
            })),

            // 总数：从pagination中获取或直接获取
            total: backendData.pagination?.total || backendData.total || 0
          }

          console.log('🎯 处理后的数据结构:', {
            recordsCount: this.currentResultDetail.records.length,
            columnsCount: this.currentResultDetail.columns.length,
            total: this.currentResultDetail.total
          })

          this.resultDetailVisible = true
          this.$message.success('结果详情加载成功')
        } else {
          console.log('❌ API调用失败:', response.msg)
          this.$message.error(`查询失败: ${response.msg}`)
          this.showResultDetailError(response.msg)
        }
      } catch (error) {
        console.error('加载结果详情失败:', error)
        this.$message.error(`加载结果详情失败: ${error.message}`)
        this.showResultDetailError(error.message)
      }
    },

    // 显示结果详情错误信息
    showResultDetailError(errorMessage) {
      this.currentResultDetail = {
        columns: [
          { field: 'error', label: '错误信息', width: 600 },
          { field: 'suggestion', label: '建议解决方案', width: 400 }
        ],
        records: [
          {
            error: `查询失败: ${errorMessage}`,
            suggestion: '1. 检查指标是否执行成功\n2. 确认后端数据库中有执行结果记录\n3. 联系管理员检查系统日志'
          }
        ],
        total: 0
      }
      this.resultDetailVisible = true
    },

    // 尝试使用配置ID查询结果详情（备用方案）
    async tryQueryByConfigId(configId) {
      try {
        console.log('尝试使用配置ID查询结果详情:', configId)

        const response = await getIndicatorResultDetail(configId, {
          pageNum: this.resultDetailPage,
          pageSize: 20
        })

        if (response.code === 1) {
          // 使用相同的数据处理逻辑
          const backendData = response.data

          this.currentResultDetail = {
            records: backendData.data || backendData.records || [],
            columns: (backendData.columns || []).map(col => ({
              field: col.name || col.field,
              label: col.description || col.label || col.name,
              width: col.width || 120
            })),
            total: backendData.pagination?.total || backendData.total || 0
          }

          this.resultDetailVisible = true
          this.$message.success('使用配置ID成功获取结果详情')
          return true
        } else {
          console.warn('配置ID查询也失败:', response.msg)
          return false
        }
      } catch (error) {
        console.error('配置ID查询出错:', error)
        return false
      }
    },

    // 备用方法：显示模拟数据（暂时保留）
    showMockResultDetail() {
      this.currentResultDetail = {
        columns: [
          { field: 'id', label: 'ID', width: 80 },
          { field: 'companyName', label: '企业名称', width: 200 },
          { field: 'riskScore', label: '风险评分', width: 120 },
          { field: 'riskLevel', label: '风险等级', width: 120 },
          { field: 'updateTime', label: '更新时间', width: 160 }
        ],
        records: [
          {
            id: 1,
            companyName: '测试企业A',
            riskScore: 85.6,
            riskLevel: '高风险',
            updateTime: '2025-01-21 10:30:00'
          },
          {
            id: 2,
            companyName: '测试企业B',
            riskScore: 62.3,
            riskLevel: '中风险',
            updateTime: '2025-01-21 10:25:00'
          }
        ],
        total: 150
      }
      this.resultDetailVisible = true
    },

    // 结果详情分页
    async handleResultDetailPageChange(page) {
      console.log('🔄 分页切换到第', page, '页')
      console.log('🔄 当前保存的resultId:', this.currentResultId)

      this.resultDetailPage = page

      if (this.currentResultId) {
        // 使用保存的resultId重新查询
        await this.viewResultDetail({ resultId: this.currentResultId })
      } else {
        console.error('❌ 分页失败：缺少resultId')
        this.$message.error('分页失败：缺少结果ID')
      }
    },

    // 结果详情页面大小变化
    async handleResultDetailSizeChange(size) {
      console.log('🔄 页面大小切换到', size, '条/页')
      this.resultDetailPage = 1  // 重置到第一页

      if (this.currentResultId) {
        const response = await getIndicatorResultDetail(this.currentResultId, {
          pageNum: 1,
          pageSize: size
        })

        if (response.code === 1) {
          const backendData = response.data
          this.currentResultDetail = {
            records: backendData.data || backendData.records || [],
            columns: this.currentResultDetail.columns, // 保持列定义不变
            total: backendData.pagination?.total || backendData.total || 0
          }
        }
      }
    },

    // 获取列宽度
    getColumnWidth(column) {
      const fieldName = column.field.toLowerCase()

      // 根据字段类型设置合适的宽度
      if (fieldName.includes('id')) return 120
      if (fieldName.includes('name')) return 200
      if (fieldName.includes('amount') || fieldName.includes('money')) return 150
      if (fieldName.includes('date') || fieldName.includes('time')) return 140
      if (fieldName.includes('type') || fieldName.includes('status')) return 100

      return 120 // 默认宽度
    },

    // 获取单元格样式类
    getColumnClass(field, value) {
      const fieldName = field.toLowerCase()

      if (fieldName.includes('amount') || fieldName.includes('money')) {
        return 'cell-amount'
      }
      if (fieldName.includes('date') || fieldName.includes('time')) {
        return 'cell-date'
      }
      if (fieldName.includes('status')) {
        return 'cell-status'
      }

      return 'cell-default'
    },

    // 格式化单元格值
    formatCellValue(field, value) {
      if (!value && value !== 0) return '-'

      const fieldName = field.toLowerCase()

      // 金额格式化
      if (fieldName.includes('amount') || fieldName.includes('money')) {
        const num = parseFloat(value)
        if (!isNaN(num)) {
          return num.toLocaleString('zh-CN', {
            style: 'currency',
            currency: 'CNY',
            minimumFractionDigits: 2
          })
        }
      }

      // 日期格式化
      if (fieldName.includes('date') || fieldName.includes('time')) {
        if (value.length === 10 && value.includes('-')) {
          return value // 已经是YYYY-MM-DD格式
        }
      }

      return value
    },

    // 导出结果数据
    exportResultData() {
      this.$message.info('导出功能开发中...')
    },

    // 刷新结果数据
    async refreshResultData() {
      if (this.currentResultId) {
        await this.viewResultDetail({ resultId: this.currentResultId })
        this.$message.success('数据刷新成功')
      }
    },

    // 下载结果
    async downloadResult(result) {
      try {
        console.log('🔽 开始导出结果:', result)
        console.log('🔽 导出参数:', {
          resultId: result.resultId,
          format: 'EXCEL'
        })

        const response = await exportAnalysisResult({
          resultId: result.resultId,
          format: 'EXCEL',
          fileName: result.indicatorName || '指标结果'
        })

        console.log('🔽 导出响应:', response)

        if (response.code === 1) {
          // 处理文件下载
          console.log('🔽 开始处理文件下载:', response.data)

          const downloadUrl = response.data.downloadUrl
          const fileName = response.data.fileName || `${result.indicatorName}-结果.xlsx`

          // 构建完整的下载URL
          // 由于我们的后端是riskcontrol模块，需要使用正确的baseURL
          let fullDownloadUrl
          if (downloadUrl.startsWith('http')) {
            fullDownloadUrl = downloadUrl
          } else {
            // 使用当前页面的协议和主机，加上后端路径
            const protocol = window.location.protocol
            const host = window.location.host
            fullDownloadUrl = `${protocol}//${host}/vab-mock-server${downloadUrl}`
          }

          console.log('🔽 下载URL:', fullDownloadUrl)
          console.log('🔽 文件名:', fileName)

          // 创建下载链接
          const link = document.createElement('a')
          link.href = fullDownloadUrl
          link.download = fileName
          link.target = '_blank'  // 在新窗口打开，避免页面跳转

          // 添加错误处理
          link.onerror = (error) => {
            console.error('🔽 下载链接错误:', error)
            this.$message.error('下载失败，请检查文件是否存在')
          }

          // 添加到DOM并点击
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)

          this.$message.success('导出成功，文件开始下载')

          // 额外提供手动下载链接
          console.log('🔽 如果自动下载失败，请手动访问:', fullDownloadUrl)

          // 同时尝试window.open方式下载
          setTimeout(() => {
            console.log('🔽 尝试window.open下载')
            window.open(fullDownloadUrl, '_blank')
          }, 1000)
        } else {
          this.$message.error(response.msg || '导出失败')
        }
      } catch (error) {
        console.error('导出结果失败:', error)
        this.$message.error('导出结果失败: ' + (error.message || '未知错误'))
      }
    },

    // 执行交集分析
    async executeIntersectionAnalysis() {
      console.log('🔄 开始执行交集分析...')

      // 🔥 数据完整性验证
      const validation = this.validateIntersectionAnalysisData()
      if (!validation.valid) {
        this.$message.error(validation.message)
        return
      }

      // 🔥 增强参数验证
      if (this.selectedIndicatorsForIntersection.length < 2) {
        this.$message.warning('请至少选择2个指标进行交集分析')
        return
      }

      // 🔥 验证executionId
      if (!this.executionId) {
        this.$message.error('执行ID不存在，请重新执行指标组合')
        return
      }

      // 🔥 验证resultIds格式和有效性
      const validResultIds = this.selectedIndicatorsForIntersection.filter(id => {
        return id && typeof id === 'string' && id.trim().length > 0
      })

      if (validResultIds.length < 2) {
        this.$message.error('选择的指标结果ID无效，请重新选择')
        return
      }

      // 🔥 验证选择的指标是否在成功结果中
      const availableResultIds = this.successfulResults.map(result => result.resultId).filter(id => id)
      const invalidIds = validResultIds.filter(id => !availableResultIds.includes(id))

      if (invalidIds.length > 0) {
        console.error('❌ 无效的结果ID:', invalidIds)
        console.log('📋 可用的结果ID:', availableResultIds)
        this.$message.error(`选择的指标结果无效: ${invalidIds.join(', ')}`)
        return
      }

      console.log('📊 交集分析参数验证通过:', {
        executionId: this.executionId,
        selectedResultIds: validResultIds,
        availableResultIds: availableResultIds,
        successfulResults: this.successfulResults.length
      })

      try {
        // 🔥 构建请求参数
        const requestParams = {
          executionId: this.executionId,
          resultIds: validResultIds,
          analysisType: 'INTERSECTION'
        }

        console.log('📤 发送交集分析请求:', requestParams)

        const response = await executeIntersectionAnalysis(requestParams)

        console.log('📥 交集分析响应:', response)

        if (response.code === 1) {
          this.intersectionResult = response.data
          this.$message.success('交集分析完成')
          console.log('✅ 交集分析成功:', this.intersectionResult)
        } else {
          console.error('❌ 交集分析业务失败:', response.msg)
          this.$message.error(response.msg || '交集分析失败')
        }
      } catch (error) {
        console.error('❌ 交集分析请求异常:', error)

        // 🔥 详细错误信息处理
        let errorMessage = '交集分析失败'
        if (error.response) {
          // 服务器响应错误
          errorMessage = `服务器错误 (${error.response.status}): ${error.response.data?.msg || error.response.statusText}`
          console.error('服务器响应错误:', error.response.data)
        } else if (error.request) {
          // 网络请求错误
          errorMessage = '网络请求失败，请检查网络连接'
          console.error('网络请求错误:', error.request)
        } else {
          // 其他错误
          errorMessage = error.message || '未知错误'
          console.error('其他错误:', error.message)
        }

        this.$message.error(errorMessage)
      }
    },

    // 🔥 修复：查看交集详情
    viewIntersectionDetail() {
      console.log('🔍 查看交集详情被点击')
      console.log('📊 intersectionResult:', this.intersectionResult)

      if (!this.intersectionResult) {
        console.warn('❌ intersectionResult 为空')
        this.$message.warning('没有交集分析结果')
        return
      }

      if (!this.intersectionResult.intersectionData) {
        console.warn('❌ intersectionData 为空')
        console.log('📋 可用字段:', Object.keys(this.intersectionResult))
        this.$message.warning('没有交集数据可查看')
        return
      }

      // 构建交集详情表格数据
      const intersectionData = this.intersectionResult.intersectionData
      console.log('📋 交集数据:', intersectionData)
      const tableData = intersectionData.map((item, index) => {
        return {
          序号: index + 1,
          交集键: item.intersectionKey,
          指标1名称: item.indicator1Name,
          指标2名称: item.indicator2Name,
          指标1数据: JSON.stringify(item.indicator1Data, null, 2),
          指标2数据: JSON.stringify(item.indicator2Data, null, 2)
        }
      })

      // 🔥 修复：使用更简单的方式显示详情
      try {
        console.log('🚀 开始显示交集详情对话框')

        // 构建HTML表格内容
        let tableHtml = `
          <div style="max-height: 400px; overflow: auto;">
            <table border="1" cellpadding="5" cellspacing="0" style="width: 100%; border-collapse: collapse; font-size: 12px;">
              <thead>
                <tr style="background-color: #f5f7fa;">
                  <th style="padding: 8px; text-align: center;">序号</th>
                  <th style="padding: 8px; text-align: center;">交集键</th>
                  <th style="padding: 8px; text-align: center;">指标1名称</th>
                  <th style="padding: 8px; text-align: center;">指标2名称</th>
                  <th style="padding: 8px; text-align: center;">指标1数据</th>
                  <th style="padding: 8px; text-align: center;">指标2数据</th>
                </tr>
              </thead>
              <tbody>
        `

        // 只显示前20条记录，避免页面卡顿
        const displayData = tableData.slice(0, 20)
        displayData.forEach(row => {
          tableHtml += `
            <tr>
              <td style="padding: 4px; text-align: center;">${row.序号}</td>
              <td style="padding: 4px; text-align: center;">${row.交集键 || '-'}</td>
              <td style="padding: 4px; max-width: 150px; word-break: break-all;">${row.指标1名称 || '-'}</td>
              <td style="padding: 4px; max-width: 150px; word-break: break-all;">${row.指标2名称 || '-'}</td>
              <td style="padding: 4px; max-width: 200px; word-break: break-all; font-family: monospace;">${(row.指标1数据 || '{}').substring(0, 100)}${row.指标1数据 && row.指标1数据.length > 100 ? '...' : ''}</td>
              <td style="padding: 4px; max-width: 200px; word-break: break-all; font-family: monospace;">${(row.指标2数据 || '{}').substring(0, 100)}${row.指标2数据 && row.指标2数据.length > 100 ? '...' : ''}</td>
            </tr>
          `
        })

        tableHtml += `
              </tbody>
            </table>
        `

        if (intersectionData.length > 20) {
          tableHtml += `
            <p style="text-align: center; color: #909399; margin-top: 10px; font-size: 12px;">
              注：仅显示前20条记录，总共${intersectionData.length}条记录
            </p>
          `
        }

        tableHtml += `</div>`

        console.log('📋 准备显示MessageBox')

        // 使用MessageBox显示
        this.$msgbox({
          title: `交集分析详情 (共${intersectionData.length}条记录)`,
          dangerouslyUseHTMLString: true,
          message: tableHtml,
          showCancelButton: true,
          confirmButtonText: '导出完整数据',
          cancelButtonText: '关闭',
          customClass: 'intersection-detail-msgbox',
          beforeClose: (action, instance, done) => {
            console.log('📤 MessageBox关闭，action:', action)
            done()
          }
        }).then(() => {
          console.log('✅ 用户点击导出完整数据')
          this.exportIntersectionResult()
        }).catch(() => {
          console.log('❌ 用户点击关闭')
        })

        console.log('✅ MessageBox显示完成')

      } catch (error) {
        console.error('❌ 显示交集详情失败:', error)

        // 🔥 备用方案：使用简单的alert显示基本信息
        const summary = `
交集分析详情摘要：
- 总记录数：${intersectionData.length}
- 分析ID：${this.intersectionResult.analysisId || '未知'}
- 分析时间：${this.intersectionResult.analysisTime || '未知'}
- 前5条交集键：${tableData.slice(0, 5).map(item => item.交集键).join(', ')}

详细数据请使用"导出Excel"功能查看完整Excel文件。
        `

        this.$alert(summary, '交集分析详情', {
          confirmButtonText: '确定',
          type: 'info'
        })
      }
    },

    // 🔥 新增：显示交集详情对话框
    showIntersectionDetailDialog() {
      console.log('🔍 显示交集详情对话框被点击')
      console.log('📊 intersectionResult:', this.intersectionResult)

      if (!this.intersectionResult) {
        this.$message.warning('没有交集分析结果')
        return
      }

      if (!this.intersectionResult.intersectionData || this.intersectionResult.intersectionData.length === 0) {
        this.$message.warning('没有交集数据可查看')
        return
      }

      console.log('✅ 打开交集详情对话框')
      this.intersectionDetailDialogVisible = true
    },

    // 🔥 新增：交集详情对话框关闭处理
    handleIntersectionDetailDialogClose() {
      console.log('🔒 交集详情对话框已关闭')
      this.intersectionDetailDialogVisible = false
    },

    // 🔥 新增：本地导出交集分析Excel功能
    async exportIntersectionToExcelLocal() {
      try {
        console.log('📊 开始本地导出交集分析Excel')

        if (!this.intersectionResult || !this.intersectionResult.intersectionData || this.intersectionResult.intersectionData.length === 0) {
          this.$message.warning('暂无交集分析数据可导出')
          return
        }

        // 🔥 动态导入xlsx库
        const XLSX = await import('xlsx')
        const FileSaver = await import('file-saver')

        // 🔥 处理交集分析数据，转换为表格格式
        const exportData = this.processIntersectionDataForExport()
        const columns = this.generateDynamicColumnsForExport()

        console.log('📋 导出数据量:', exportData.length)
        console.log('📋 导出列数:', columns.length)

        // 🔥 创建Excel工作簿
        const workbook = this.createIntersectionExcelWorkbook(XLSX, exportData, columns)

        // 🔥 生成文件名
        const fileName = `交集分析结果_${this.intersectionResult.analysisId}_${new Date().getTime()}.xlsx`

        // 🔥 下载文件
        this.downloadIntersectionExcelFile(XLSX, FileSaver, workbook, fileName)

        this.$message.success('Excel导出成功！')

      } catch (error) {
        console.error('❌ 本地导出交集分析Excel失败:', error)
        this.$message.error('导出失败: ' + (error.message || '未知错误'))
      }
    },

    // 🔥 处理交集分析数据为导出格式
    processIntersectionDataForExport() {
      return this.intersectionResult.intersectionData.map((row, index) => {
        const processedRow = {
          序号: index + 1,
          交集键: row.intersectionKey || row.交集键 || `交集${index + 1}`
        }

        // 🔥 扁平化指标1数据
        if (row.indicator1Data && typeof row.indicator1Data === 'object' && !row.indicator1Data.$ref) {
          Object.keys(row.indicator1Data).forEach(field => {
            processedRow[`指标1_${field}`] = row.indicator1Data[field]
          })
        }

        // 🔥 扁平化指标2数据
        if (row.indicator2Data && typeof row.indicator2Data === 'object' && !row.indicator2Data.$ref) {
          Object.keys(row.indicator2Data).forEach(field => {
            processedRow[`指标2_${field}`] = row.indicator2Data[field]
          })
        }

        // 🔥 处理更多指标数据（如果有的话）
        Object.keys(row).forEach(key => {
          if (key.startsWith('indicator') && key.endsWith('Data') && key !== 'indicator1Data' && key !== 'indicator2Data') {
            const indicatorNum = key.match(/indicator(\d+)Data/)?.[1]
            if (indicatorNum && row[key] && typeof row[key] === 'object' && !row[key].$ref) {
              Object.keys(row[key]).forEach(field => {
                processedRow[`指标${indicatorNum}_${field}`] = row[key][field]
              })
            }
          }
        })

        return processedRow
      })
    },

    // 🔥 生成动态列配置
    generateDynamicColumnsForExport() {
      const allFields = new Set()

      // 🔥 收集所有指标数据中的字段
      this.intersectionResult.intersectionData.forEach(row => {
        // 收集指标1数据的字段
        if (row.indicator1Data && typeof row.indicator1Data === 'object' && !row.indicator1Data.$ref) {
          Object.keys(row.indicator1Data).forEach(field => {
            allFields.add(`指标1_${field}`)
          })
        }

        // 收集指标2数据的字段
        if (row.indicator2Data && typeof row.indicator2Data === 'object' && !row.indicator2Data.$ref) {
          Object.keys(row.indicator2Data).forEach(field => {
            allFields.add(`指标2_${field}`)
          })
        }

        // 收集其他指标数据的字段
        Object.keys(row).forEach(key => {
          if (key.startsWith('indicator') && key.endsWith('Data') && key !== 'indicator1Data' && key !== 'indicator2Data') {
            const indicatorNum = key.match(/indicator(\d+)Data/)?.[1]
            if (indicatorNum && row[key] && typeof row[key] === 'object' && !row[key].$ref) {
              Object.keys(row[key]).forEach(field => {
                allFields.add(`指标${indicatorNum}_${field}`)
              })
            }
          }
        })
      })

      // 🔥 生成列配置
      return Array.from(allFields).sort().map(fieldKey => {
        const [indicatorPrefix, fieldName] = fieldKey.split('_')
        return {
          prop: fieldKey,
          label: `${indicatorPrefix}(${fieldName})`,
          width: this.getExportColumnWidth(fieldName)
        }
      })
    },

    // 🔥 获取导出列宽度
    getExportColumnWidth(fieldName) {
      // 特殊字段的固定宽度
      const fixedWidthMap = {
        '交集键': 100,
        'intersectionKey': 100,
        '序号': 60
      }

      if (fixedWidthMap[fieldName]) {
        return fixedWidthMap[fieldName]
      }

      // 根据字段名称智能判断宽度
      const fieldLower = fieldName.toLowerCase()

      if (fieldLower.includes('id') || fieldLower.includes('code')) {
        return 120
      } else if (fieldLower.includes('name') || fieldLower.includes('title')) {
        return 180
      } else if (fieldLower.includes('time') || fieldLower.includes('date')) {
        return 160
      } else if (fieldLower.includes('amount') || fieldLower.includes('money') || fieldLower.includes('price')) {
        return 120
      } else {
        return 120 // 默认宽度
      }
    },

    // 🔥 创建交集分析Excel工作簿
    createIntersectionExcelWorkbook(XLSX, data, columns) {
      // 🔥 准备表头
      const headers = ['序号', '交集键', ...columns.map(col => col.label)]

      // 🔥 准备数据行
      const rows = data.map(row => {
        return [
          row.序号,
          row.交集键 || '',
          ...columns.map(col => {
            const value = row[col.prop]
            if (value === null || value === undefined) {
              return ''
            }
            return this.formatExcelValue(value)
          })
        ]
      })

      // 🔥 合并表头和数据
      const worksheetData = [headers, ...rows]

      // 🔥 创建工作表
      const worksheet = XLSX.utils.aoa_to_sheet(worksheetData)

      // 🔥 设置列宽
      const colWidths = headers.map((header, index) => {
        if (index === 0) return { wch: 8 }   // 序号列
        if (index === 1) return { wch: 15 }  // 交集键列
        return { wch: 20 } // 其他列
      })
      worksheet['!cols'] = colWidths

      // 🔥 创建工作簿
      const workbook = XLSX.utils.book_new()
      XLSX.utils.book_append_sheet(workbook, worksheet, '交集分析结果')

      return workbook
    },

    // 🔥 格式化Excel值
    formatExcelValue(value) {
      if (typeof value === 'object') {
        if (Array.isArray(value)) {
          return `[数组:${value.length}项]`
        } else {
          return `[对象:${Object.keys(value).length}个字段]`
        }
      }
      return String(value)
    },

    // 🔥 下载交集分析Excel文件
    downloadIntersectionExcelFile(XLSX, FileSaver, workbook, fileName) {
      // 🔥 生成Excel文件
      const excelBuffer = XLSX.write(workbook, {
        bookType: 'xlsx',
        type: 'array'
      })

      // 🔥 创建Blob对象
      const blob = new Blob([excelBuffer], {
        type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
      })

      // 🔥 使用FileSaver下载
      FileSaver.saveAs(blob, fileName)

      console.log('✅ 交集分析Excel文件下载完成:', fileName)
    },

    // 🔥 新增：验证交集分析数据完整性
    validateIntersectionAnalysisData() {
      console.log('🔍 验证交集分析数据完整性...')

      // 检查执行结果
      if (!this.executionResult) {
        console.error('❌ 执行结果不存在')
        return { valid: false, message: '请先执行指标组合' }
      }

      // 检查指标结果
      if (!this.indicatorResults || this.indicatorResults.length === 0) {
        console.error('❌ 指标结果为空')
        return { valid: false, message: '没有可用的指标结果' }
      }

      // 检查成功的结果
      const successResults = this.successfulResults
      if (successResults.length < 2) {
        console.error('❌ 成功的指标结果少于2个:', successResults.length)
        return { valid: false, message: `成功的指标结果只有${successResults.length}个，至少需要2个才能进行交集分析` }
      }

      // 检查resultId的有效性
      const invalidResults = successResults.filter(result => !result.resultId || result.resultId.includes('TEMP_'))
      if (invalidResults.length > 0) {
        console.warn('⚠️ 发现临时或无效的resultId:', invalidResults.map(r => r.resultId))
        return {
          valid: false,
          message: `部分指标结果ID无效，可能是执行过程中出现问题。请重新执行指标组合。无效ID: ${invalidResults.map(r => r.resultId).join(', ')}`
        }
      }

      console.log('✅ 交集分析数据验证通过')
      return { valid: true, message: '数据验证通过' }
    },

    // 🔥 新增：显示交集分析信息
    showIntersectionDebugInfo() {
      const debugInfo = {
        executionId: this.executionId,
        executionResult: !!this.executionResult,
        indicatorResultsCount: this.indicatorResults.length,
        successfulResultsCount: this.successfulResults.length,
        selectedCount: this.selectedIndicatorsForIntersection.length,
        indicatorResults: this.indicatorResults.map(r => ({
          resultId: r.resultId,
          indicatorName: r.indicatorName,
          status: r.status,
          recordCount: r.recordCount
        })),
        successfulResults: this.successfulResults.map(r => ({
          resultId: r.resultId,
          indicatorName: r.indicatorName,
          status: r.status
        })),
        selectedIndicators: this.selectedIndicatorsForIntersection
      }

      console.log('🔍 交集分析信息:', debugInfo)

      this.$alert(`
        <div style="text-align: left; font-family: monospace; font-size: 12px;">
          <p><strong>执行ID:</strong> ${debugInfo.executionId || '无'}</p>
          <p><strong>执行结果:</strong> ${debugInfo.executionResult ? '存在' : '不存在'}</p>
          <p><strong>指标结果总数:</strong> ${debugInfo.indicatorResultsCount}</p>
          <p><strong>成功结果数:</strong> ${debugInfo.successfulResultsCount}</p>
          <p><strong>已选择指标数:</strong> ${debugInfo.selectedCount}</p>
          <hr>
          <p><strong>成功的指标结果:</strong></p>
          ${debugInfo.successfulResults.map(r =>
            `<div>• ${r.indicatorName} (${r.resultId})</div>`
          ).join('')}
          <hr>
          <p><strong>已选择的指标ID:</strong></p>
          ${debugInfo.selectedIndicators.map(id => `<div>• ${id}</div>`).join('')}
        </div>
      `, '交集分析信息', {
        dangerouslyUseHTMLString: true,
        customClass: 'debug-info-dialog'
      })
    },

    // 🔥 新增：交集分析下拉框可见性变化处理
    handleIntersectionSelectVisibleChange(visible) {
      console.log('🔽 交集分析下拉框可见性变化:', visible)
      if (visible) {
        this.$nextTick(() => {
          this.ensureIntersectionSelectDropdownVisible()
        })
      }
    },

    // 🔥 新增：确保交集分析下拉框可见
    ensureIntersectionSelectDropdownVisible() {
      try {
        console.log('🔽 确保交集分析下拉框可见')

        // 查找交集分析下拉框的弹出层
        const dropdowns = document.querySelectorAll('.intersection-select-dropdown, .execution-dialog-dropdown')
        dropdowns.forEach((dropdown, index) => {
          if (dropdown) {
            dropdown.style.zIndex = '3600'
            dropdown.style.position = 'fixed'
            console.log(`✅ 设置下拉框 ${index + 1} z-index: 3600`)
          }
        })

        // 查找所有Element-UI下拉框
        const allDropdowns = document.querySelectorAll('.el-select-dropdown')
        allDropdowns.forEach((dropdown, index) => {
          const currentZIndex = parseInt(window.getComputedStyle(dropdown).zIndex) || 0
          if (currentZIndex < 3600) {
            dropdown.style.zIndex = '3600'
            dropdown.style.position = 'fixed'
            console.log(`✅ 修复下拉框 ${index + 1} z-index: 3600`)
          }
        })

        // 特别处理执行对话框内的下拉框
        const executionDialog = document.querySelector('.execution-dialog-wrapper')
        if (executionDialog) {
          const selectDropdowns = executionDialog.querySelectorAll('.el-select-dropdown')
          selectDropdowns.forEach((dropdown, index) => {
            dropdown.style.zIndex = '3600'
            dropdown.style.position = 'fixed'
            console.log(`✅ 修复执行对话框内下拉框 ${index + 1} z-index: 3600`)
          })
        }

      } catch (error) {
        console.error('❌ 确保交集分析下拉框可见失败:', error)
      }
    },

    // 🔥 新增：全局点击处理，确保下拉框层级
    handleGlobalClick(event) {
      // 检查是否点击了下拉框相关元素
      const target = event.target
      if (target && (
        target.closest('.el-select') ||
        target.closest('.el-select-dropdown') ||
        target.classList.contains('el-select-dropdown__item')
      )) {
        // 延迟确保下拉框可见
        setTimeout(() => {
          this.ensureIntersectionSelectDropdownVisible()
        }, 50)
      }
    },

    // 保存执行结果
    saveExecutionResult() {
      this.$emit('executed', this.executionResult)
      this.handleClose()
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
    },

    // 🔥 新增：对话框打开时的处理
    handleDialogOpened() {
      console.log('🎯 执行对话框已打开，确保在最上层')
      this.ensureDialogOnTop()
      this.fixDialogWidth()
    },

    // 🔥 新增：修复对话框宽度问题
    fixDialogWidth() {
      this.$nextTick(() => {
        setTimeout(() => {
          const dialogWrapper = document.querySelector('.execution-dialog-wrapper')
          if (dialogWrapper) {
            // 移除可能影响宽度的样式
            dialogWrapper.style.removeProperty('width')
            dialogWrapper.style.removeProperty('position')
            dialogWrapper.style.removeProperty('top')
            dialogWrapper.style.removeProperty('left')
            dialogWrapper.style.removeProperty('right')
            dialogWrapper.style.removeProperty('bottom')

            // 只保留z-index
            dialogWrapper.style.zIndex = '3500'

            console.log('✅ 修复执行对话框宽度，移除position和width样式')

            // 检查对话框内部的el-dialog元素
            const dialog = dialogWrapper.querySelector('.el-dialog')
            if (dialog) {
              dialog.style.removeProperty('width')
              dialog.style.removeProperty('position')
              console.log('✅ 修复内部对话框样式')
            }
          }
        }, 200)
      })
    },

    // 🔥 新增：确保对话框在最上层
    ensureDialogOnTop() {
      try {
        console.log('🔝 确保执行对话框在最上层')

        // 方法1: 通过类名查找对话框元素
        const dialogElement = document.querySelector('.execution-dialog-wrapper')
        if (dialogElement) {
          // 只设置z-index，不改变宽度和位置
          dialogElement.style.setProperty('z-index', '3500', 'important')
          console.log('✅ 通过类名设置执行对话框z-index: 3500')
        }

        // 方法2: 通过标题查找对话框元素
        const dialogByTitle = document.querySelector('.el-dialog__header .el-dialog__title')
        if (dialogByTitle && dialogByTitle.textContent.includes('执行指标组合')) {
          const dialog = dialogByTitle.closest('.el-dialog__wrapper')
          if (dialog) {
            dialog.style.setProperty('z-index', '3500', 'important')
            console.log('✅ 通过标题设置执行对话框z-index: 3500')
          }
        }

        // 方法3: 查找所有对话框，确保执行对话框在最上层
        const allDialogs = document.querySelectorAll('.el-dialog__wrapper')
        let maxZIndex = 3400
        allDialogs.forEach(dialog => {
          const currentZIndex = parseInt(window.getComputedStyle(dialog).zIndex) || 0
          if (currentZIndex > maxZIndex) {
            maxZIndex = currentZIndex
          }
        })

        // 为执行对话框设置更高的z-index
        const executionDialog = document.querySelector('.execution-dialog-wrapper') ||
                               document.querySelector('.execution-dialog')?.closest('.el-dialog__wrapper')
        if (executionDialog) {
          const newZIndex = Math.max(maxZIndex + 10, 3500)
          executionDialog.style.setProperty('z-index', newZIndex.toString(), 'important')
          console.log(`✅ 动态设置执行对话框z-index: ${newZIndex}`)
        }

        // 方法4: 强制设置CSS类
        this.$nextTick(() => {
          const dialogWrapper = this.$el?.closest('.el-dialog__wrapper')
          if (dialogWrapper) {
            dialogWrapper.classList.add('execution-dialog-top-layer')
            dialogWrapper.style.setProperty('z-index', '3500', 'important')
            console.log('✅ 添加顶层CSS类并设置z-index: 3500')
          }
        })

      } catch (error) {
        console.error('❌ 设置执行对话框层级失败:', error)
      }
    },

    // 清除状态定时器 - 兼容新的轮询逻辑
    clearStatusTimer() {
      // 停止新的轮询逻辑
      this.stopPolling()

      // 兼容旧的定时器逻辑（如果存在）
      if (this.statusTimer) {
        clearInterval(this.statusTimer)
        this.statusTimer = null
      }

      if (this.maxPollingTimer) {
        clearTimeout(this.maxPollingTimer)
        this.maxPollingTimer = null
      }
    },

    // 添加日志
    addLog(level, message) {
      this.executionLogs.push({
        timestamp: new Date().getTime(),
        level,
        message
      })

      // 自动滚动到最新日志
      this.$nextTick(() => {
        const logContainer = this.$el.querySelector('.log-container')
        if (logContainer) {
          logContainer.scrollTop = logContainer.scrollHeight
        }
      })
    },

    // 清空日志
    clearLogs() {
      this.executionLogs = []
    },

    // 切换执行日志显示/隐藏
    toggleExecutionLogs() {
      this.showExecutionLogs = !this.showExecutionLogs

      // 如果显示日志，自动滚动到日志区域
      if (this.showExecutionLogs) {
        this.$nextTick(() => {
          const logElement = this.$el.querySelector('.execution-log')
          if (logElement) {
            logElement.scrollIntoView({ behavior: 'smooth', block: 'nearest' })
          }
        })
      }
    },

    // 获取日志图标
    getLogIcon(level) {
      const icons = {
        'SUCCESS': 'el-icon-success',
        'INFO': 'el-icon-info',
        'WARN': 'el-icon-warning',
        'ERROR': 'el-icon-error'
      }
      return icons[level] || 'el-icon-info'
    },

    // 工具方法
    getStatusType(status) {
      const typeMap = {
        'SUCCESS': 'success',
        'RUNNING': 'primary',
        'FAILED': 'danger',
        'CANCELLED': 'info'
      }
      return typeMap[status] || 'info'
    },

    getStatusText(status) {
      const textMap = {
        'SUCCESS': '成功',
        'RUNNING': '运行中',
        'FAILED': '失败',
        'CANCELLED': '已取消'
      }
      return textMap[status] || status
    },

    formatTime(timestamp) {
      const date = new Date(timestamp)
      return date.toLocaleString()
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

    // 获取进度条状态
    getProgressStatus() {
      if (this.isExecuting) {
        return null // 执行中显示默认蓝色
      } else if (this.executionResult) {
        const status = this.executionResult.status
        if (status === 'SUCCESS') return 'success'
        if (status === 'PARTIAL_SUCCESS') return 'warning'
        if (status === 'FAILED') return 'exception'
        if (status === 'CANCELLED') return 'warning'
      }
      return null
    },

    // 获取执行状态文本
    getExecutionStatusText() {
      if (this.isExecuting) {
        const currentIndicator = this.executionStatus === 'RUNNING' ? '正在执行指标...' : '准备中...'
        return `执行中 ${this.executionProgress}% - ${currentIndicator}`
      } else if (this.executionResult) {
        const status = this.executionResult.status
        const textMap = {
          'SUCCESS': '✅ 执行成功',
          'PARTIAL_SUCCESS': '⚠️ 部分成功',
          'FAILED': '❌ 执行失败',
          'CANCELLED': '🚫 已取消'
        }
        return textMap[status] || status
      } else if (this.executionId) {
        return '🔄 准备执行...'
      }
      return '⏳ 等待开始'
    },

    // 显示执行结果详情
    showExecutionResult() {
      if (!this.executionResult) {
        this.$message.warning('暂无执行结果')
        return
      }

      // 切换到结果标签页
      this.activeResultTab = 'indicators'

      // 滚动到结果区域
      this.$nextTick(() => {
        const resultElement = this.$el.querySelector('.execution-result')
        if (resultElement) {
          resultElement.scrollIntoView({ behavior: 'smooth' })
        }
      })
    },

    // 下载执行报告
    downloadExecutionReport() {
      if (!this.executionResult) {
        this.$message.warning('暂无执行结果')
        return
      }

      try {
        const reportData = {
          executionId: this.executionId,
          combinationName: this.combination?.combinationName || '未知组合',
          executionTime: this.executionResult.endTime || new Date().toLocaleString(),
          status: this.executionResult.status,
          totalDuration: this.executionResult.totalDuration,
          indicators: this.indicatorResults
        }

        const blob = new Blob([JSON.stringify(reportData, null, 2)], { type: 'application/json' })
        const url = URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = `执行报告_${this.executionId}_${new Date().getTime()}.json`
        document.body.appendChild(a)
        a.click()
        document.body.removeChild(a)
        URL.revokeObjectURL(url)

        this.$message.success('报告下载成功')
      } catch (error) {
        console.error('下载报告失败:', error)
        this.$message.error('下载报告失败')
      }
    }
  },

  mounted() {
    // 🔥 新增：组件挂载时确保层级正确
    console.log('🎯 ExecutionDialog mounted')
    if (this.dialogVisible) {
      this.$nextTick(() => {
        this.ensureDialogOnTop()
        this.ensureIntersectionSelectDropdownVisible()
      })
    }

    // 🔥 新增：监听全局下拉框事件
    document.addEventListener('click', this.handleGlobalClick)
  },

  beforeDestroy() {
    this.clearStatusTimer()
    // 🔥 新增：移除全局事件监听
    document.removeEventListener('click', this.handleGlobalClick)
  }
}
</script>

<style scoped>
.execution-dialog {
  .execution-container {
    max-height: 70vh;
    overflow-y: auto;
  }

  .execution-config,
  .indicators-preview,
  .execution-status {
    margin-bottom: 24px;
    padding-bottom: 16px;
    border-bottom: 1px solid #ebeef5;

    &:last-child {
      border-bottom: none;
      margin-bottom: 0;
    }

    h3 {
      margin: 0 0 16px 0;
      font-size: 16px;
      font-weight: 600;
      color: #303133;
    }
  }

  .parameter-config {
    .parameter-item {
      display: flex;
      align-items: center;
      gap: 12px;
      margin-bottom: 8px;

      label {
        width: 120px;
        font-weight: 600;
        color: #606266;
      }

      .el-input {
        flex: 1;
      }

      .param-type {
        font-size: 12px;
        color: #909399;
        width: 60px;
      }
    }

    .no-parameters {
      color: #909399;
      text-align: center;
      padding: 20px;
    }
  }

  .preview-list {
    .preview-item {
      padding: 12px;
      border: 1px solid #ebeef5;
      border-radius: 4px;
      margin-bottom: 8px;

      .item-header {
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

        .indicator-name {
          flex: 1;
          font-weight: 600;
        }
      }

      .item-meta {
        display: flex;
        gap: 16px;
        font-size: 12px;
        color: #909399;
      }
    }
  }

  .executing {
    .status-header {
      display: flex;
      align-items: center;
      gap: 16px;
      margin-bottom: 16px;

      .el-progress {
        flex: 1;
      }
    }

    .execution-log {
      margin-top: 16px;
      border: 1px solid #e4e7ed;
      border-radius: 8px;
      background: #ffffff;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);

      .log-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 12px 16px;
        border-bottom: 1px solid #f0f2f5;
        background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
        border-radius: 8px 8px 0 0;

        h4 {
          margin: 0;
          font-size: 14px;
          font-weight: 600;
          color: #303133;
          display: flex;
          align-items: center;
          gap: 8px;

          i {
            color: #409eff;
          }
        }

        .log-stats {
          display: flex;
          align-items: center;
          gap: 8px;
        }
      }

      .log-container {
        max-height: 240px;
        overflow-y: auto;
        padding: 8px;
        background-color: #fafbfc;

        &::-webkit-scrollbar {
          width: 6px;
        }

        &::-webkit-scrollbar-track {
          background: #f1f1f1;
          border-radius: 3px;
        }

        &::-webkit-scrollbar-thumb {
          background: #c1c1c1;
          border-radius: 3px;

          &:hover {
            background: #a8a8a8;
          }
        }

        .log-item {
          display: flex;
          align-items: flex-start;
          gap: 12px;
          margin-bottom: 8px;
          padding: 8px 12px;
          border-radius: 6px;
          font-size: 13px;
          line-height: 1.5;
          transition: all 0.2s ease;

          &:hover {
            background-color: rgba(64, 158, 255, 0.05);
          }

          &.success {
            background-color: rgba(103, 194, 58, 0.08);
            border-left: 3px solid #67c23a;

            .log-icon i {
              color: #67c23a;
            }
          }

          &.info {
            background-color: rgba(64, 158, 255, 0.08);
            border-left: 3px solid #409eff;

            .log-icon i {
              color: #409eff;
            }
          }

          &.warn {
            background-color: rgba(230, 162, 60, 0.08);
            border-left: 3px solid #e6a23c;

            .log-icon i {
              color: #e6a23c;
            }
          }

          &.error {
            background-color: rgba(245, 108, 108, 0.08);
            border-left: 3px solid #f56c6c;

            .log-icon i {
              color: #f56c6c;
            }
          }

          .log-icon {
            width: 20px;
            height: 20px;
            display: flex;
            align-items: center;
            justify-content: center;
            flex-shrink: 0;
            margin-top: 2px;

            i {
              font-size: 14px;
            }
          }

          .log-content {
            flex: 1;
            min-width: 0;

            .log-message {
              color: #303133;
              font-weight: 500;
              margin-bottom: 4px;
              word-break: break-word;
            }

            .log-meta {
              display: flex;
              gap: 12px;
              font-size: 11px;
              color: #909399;

              .timestamp {
                font-family: 'Courier New', monospace;
              }

              .level {
                font-weight: 600;
                text-transform: uppercase;
              }
            }
          }
        }
      }
    }
  }

  .execution-result {
    .result-summary {
      margin-bottom: 16px;
    }

    .indicator-results {
      .result-item {
        padding: 12px;
        border: 1px solid #ebeef5;
        border-radius: 4px;
        margin-bottom: 8px;

        .result-header {
          display: flex;
          align-items: center;
          gap: 12px;
          margin-bottom: 8px;

          h4 {
            margin: 0;
            flex: 1;
            font-size: 14px;
            font-weight: 600;
          }

          .duration {
            font-size: 12px;
            color: #909399;
          }
        }

        .result-content {
          display: flex;
          justify-content: space-between;
          align-items: center;

          .result-stats {
            display: flex;
            gap: 16px;
            font-size: 12px;
            color: #606266;
          }

          .result-actions {
            display: flex;
            gap: 8px;
          }
        }
      }
    }

    .intersection-analysis {
      .analysis-config {
        display: flex;
        align-items: center;
        margin-bottom: 16px;
      }

      .intersection-result {
        padding: 16px;
        border: 1px solid #ebeef5;
        border-radius: 4px;
        background-color: #f5f7fa;

        h4 {
          margin: 0 0 12px 0;
          font-size: 14px;
          font-weight: 600;
        }

        .intersection-actions {
          margin-top: 12px;
          display: flex;
          gap: 8px;
        }
      }
    }
  }

  .dialog-footer {
    text-align: right;
  }

  /* 结果详情对话框样式 */
  .result-detail-dialog {
    .el-dialog {
      margin: 0 !important;
      height: 90vh;
      overflow: hidden;

      .el-dialog__body {
        padding: 0;
        height: calc(90vh - 120px);
        overflow: hidden;
      }
    }
  }

  .result-detail-container {
    height: 100%;
    display: flex;
    flex-direction: column;

    .result-stats-bar {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 12px 20px;
      background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
      border-bottom: 1px solid #e4e7ed;

      .stats-info {
        display: flex;
        gap: 12px;

        .el-tag {
          padding: 4px 8px;

          i {
            margin-right: 4px;
          }
        }
      }

      .stats-actions {
        display: flex;
        gap: 8px;

        .el-button {
          padding: 4px 8px;
          font-size: 12px;

          i {
            margin-right: 4px;
          }
        }
      }
    }

    .table-container {
      flex: 1;
      padding: 0 20px;
      overflow: hidden;

      .el-table {
        height: 100% !important;

        .el-table__body-wrapper {
          height: calc(100% - 48px) !important;
          overflow-y: auto;
        }
      }

      /* 单元格样式 */
      .cell-amount {
        color: #e6a23c;
        font-weight: 600;
        text-align: right;
      }

      .cell-date {
        color: #909399;
        font-family: 'Courier New', monospace;
      }

      .cell-status {
        font-weight: 500;
      }

      .cell-default {
        color: #606266;
      }
    }

    .pagination-container {
      padding: 16px 20px;
      background: #fafafa;
      border-top: 1px solid #e4e7ed;
      display: flex;
      justify-content: center;
    }
  }

  .no-data-container {
    height: 400px;
    display: flex;
    align-items: center;
    justify-content: center;

    .no-data-content {
      text-align: center;
    }
  }
}

/* 🔥 新增：交集信息对话框样式 - 全局样式 */
.debug-info-dialog {
  .el-message-box__content {
    max-height: 400px;
    overflow-y: auto;
  }

  .el-message-box__message {
    line-height: 1.4;
  }
}
</style>
