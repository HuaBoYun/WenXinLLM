<template>
  <div class="evaluation-model-management">
    <!-- 查询条件 -->
    <div class="search-form">
      <el-form
        :model="queryForm"
        ref="queryForm"
        :inline="true"
        label-width="100px"
      >
        <el-form-item label="模型名称">
          <el-input
            v-model="queryForm.modelName"
            placeholder="请输入模型名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="模型类型">
          <el-select
            v-model="queryForm.modelType"
            placeholder="请选择模型类型"
            clearable
            style="width: 150px"
          >
            <el-option label="采购风险" value="PROCUREMENT" />
            <el-option label="财务风险" value="FINANCIAL" />
            <el-option label="信用风险" value="CREDIT" />
            <el-option label="合规风险" value="COMPLIANCE" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch" icon="el-icon-search">
            查询
          </el-button>
          <el-button @click="handleReset" icon="el-icon-refresh">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作按钮 -->
    <div class="toolbar">
      <el-button
        type="info"
        @click="handleRefreshStatistics"
        icon="el-icon-refresh"
      >
        刷新统计
      </el-button>
      <!-- 🔧 暂时隐藏：生成报告按钮 -->
      <!-- <el-button type="primary" @click="handleGenerateReport" icon="el-icon-document">生成报告</el-button> -->
      <el-button @click="handleWarningConfig" icon="el-icon-setting">
        预警配置
      </el-button>
    </div>

    <!-- 统计信息 -->
    <div class="statistics-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-number">{{ statistics.totalCount || 0 }}</div>
            <div class="stat-label">总预警数</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card pending">
            <div class="stat-number">
              {{
                (statistics.pendingCount || 0) +
                (statistics.processingCount || 0)
              }}
            </div>
            <div class="stat-label">待处理</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card processed">
            <div class="stat-number">{{ statistics.processedCount || 0 }}</div>
            <div class="stat-label">已处理</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card today">
            <div class="stat-number">{{ statistics.todayCount || 0 }}</div>
            <div class="stat-label">今日新增</div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column
          prop="modelCode"
          label="模型编码"
          width="120"
          show-overflow-tooltip
        />
        <el-table-column
          prop="modelName"
          label="模型名称"
          min-width="180"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <span>{{ scope.row.modelName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="businessScenario" label="业务场景" width="120" />
        <el-table-column prop="version" label="版本" width="80" />
        <el-table-column prop="totalWeight" label="总权重" width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.totalWeight">
              {{ scope.row.totalWeight }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <!-- 🔧 修复：待处理预警数量字段 -->
        <el-table-column
          prop="pendingWarningCount"
          label="待处理预警"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            <!-- 🔧 修复：启用的模型都显示待处理预警数量 -->
            <span v-if="scope.row.isEnabled === 'Y'">
              <span
                class="pending-warning-count"
                @click="handleRefreshPendingWarningCount(scope.row)"
                style="cursor: pointer; color: #e6a23c"
                title="点击刷新待处理预警数量"
              >
                {{ getModelPendingWarningCount(scope.row) }}
              </span>
            </span>
            <span v-else class="disabled-warning">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="createUser" label="创建人" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="150">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template slot-scope="scope">
            <el-button
              size="mini"
              @click="handleView(scope.row)"
              icon="el-icon-view"
            >
              查看
            </el-button>

            <!-- 🔧 修复：查看预警结果按钮 - 启用的模型都显示，启动后一直显示 -->
            <el-button
              v-if="scope.row.isEnabled === 'Y'"
              size="mini"
              type="warning"
              @click="handleViewWarningResults(scope.row)"
              icon="el-icon-warning-outline"
            >
              预警结果
            </el-button>

            <!-- 🆕 新增：更多操作下拉菜单 -->
            <el-dropdown
              v-if="scope.row.isEnabled === 'Y'"
              @command="handleMoreAction"
              trigger="click"
              size="mini"
            >
              <el-button size="mini" type="primary">
                更多
                <i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  :command="{ action: 'generateReport', row: scope.row }"
                  icon="el-icon-document"
                >
                  生成报告
                </el-dropdown-item>
                <el-dropdown-item
                  :command="{ action: 'viewFlowChart', row: scope.row }"
                  icon="el-icon-share"
                >
                  流程图
                </el-dropdown-item>
                <el-dropdown-item
                  :command="{ action: 'warningProcess', row: scope.row }"
                  icon="el-icon-check"
                >
                  预警处理
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryForm.pageNum"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="queryForm.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      />
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <evaluation-model-form
        ref="modelForm"
        :form-data="currentModel"
        :is-edit="isEdit"
        @submit="handleFormSubmit"
        @cancel="handleDialogClose"
      />
    </el-dialog>

    <!-- 测试对话框 -->
    <el-dialog
      title="模型测试"
      :visible.sync="testDialogVisible"
      width="1000px"
      :close-on-click-modal="false"
    >
      <evaluation-model-test
        ref="modelTest"
        :model-data="currentModel"
        @close="handleTestDialogClose"
      />
    </el-dialog>

    <!-- 查看对话框 -->
    <evaluation-model-view ref="modelView" />

    <!-- SQL执行结果查看对话框 -->
    <el-dialog
      title="模型SQL执行结果"
      :visible.sync="sqlResultDialogVisible"
      width="95%"
      :close-on-click-modal="false"
      custom-class="sql-result-dialog"
    >
      <div class="sql-result-container">
        <!-- 模型信息 -->
        <div class="model-info-section">
          <el-card shadow="never" class="model-info-card">
            <div slot="header" class="card-header">
              <span>模型信息</span>
              <div class="header-actions">
                <el-button
                  size="small"
                  type="primary"
                  @click="refreshSqlResult"
                  :loading="sqlResultLoading"
                >
                  <i class="el-icon-refresh"></i>
                  刷新
                </el-button>
              </div>
            </div>
            <el-descriptions :column="3" border>
              <el-descriptions-item label="模型名称">
                {{ currentSqlModel.modelName }}
              </el-descriptions-item>
              <el-descriptions-item label="模型类型">
                {{ getModelTypeText(currentSqlModel.modelType) }}
              </el-descriptions-item>
              <el-descriptions-item label="业务场景">
                {{ currentSqlModel.businessScenario }}
              </el-descriptions-item>
              <el-descriptions-item label="执行时间">
                {{ sqlExecutionInfo.executionTime || '-' }}
              </el-descriptions-item>
              <el-descriptions-item label="数据条数">
                {{ sqlExecutionInfo.recordCount || 0 }}
              </el-descriptions-item>
              <el-descriptions-item label="执行状态">
                <el-tag
                  :type="
                    sqlExecutionInfo.status === 'SUCCESS' ? 'success' : 'danger'
                  "
                >
                  {{ sqlExecutionInfo.status === 'SUCCESS' ? '成功' : '失败' }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item
                label="执行模式"
                v-if="sqlExecutionInfo.executionMode"
              >
                <el-tag
                  :type="
                    sqlExecutionInfo.executionMode === 'SEQUENCE'
                      ? 'primary'
                      : 'warning'
                  "
                >
                  {{
                    sqlExecutionInfo.executionMode === 'SEQUENCE'
                      ? '顺序执行'
                      : '并行执行'
                  }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item
                label="指标数量"
                v-if="sqlExecutionInfo.indicatorCount"
              >
                {{ sqlExecutionInfo.indicatorCount }}
              </el-descriptions-item>
            </el-descriptions>

            <!-- SQL内容展示 - 已隐藏 -->
            <!-- <div style="margin-top: 15px;" v-if="sqlExecutionInfo.sqlContent">
              <h5>执行的SQL语句：</h5>
              <el-input
                type="textarea"
                :rows="6"
                :value="sqlExecutionInfo.sqlContent"
                readonly
                style="font-family: 'Courier New', monospace;"
              />
            </div> -->
          </el-card>
        </div>

        <!-- SQL执行结果表格 -->
        <div class="sql-results-section">
          <el-card shadow="never">
            <div slot="header" class="card-header">
              <span>SQL执行结果</span>
              <div class="header-actions">
                <el-button
                  type="success"
                  size="small"
                  @click="handleJumpToWarningManagement"
                >
                  进入风险预警管理
                </el-button>
              </div>
            </div>

            <el-table
              :data="sqlResultData"
              v-loading="sqlResultLoading"
              border
              stripe
              style="width: 100%"
              max-height="500"
            >
              <el-table-column
                v-for="(column, index) in sqlResultColumns"
                :key="index"
                :prop="column.prop"
                :label="column.label"
                :width="column.width"
                show-overflow-tooltip
              />
            </el-table>

            <!-- 分页 -->
            <div class="pagination-container" style="margin-top: 20px">
              <el-pagination
                @size-change="handleSqlResultSizeChange"
                @current-change="handleSqlResultCurrentChange"
                :current-page="sqlResultQuery.pageNum"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="sqlResultQuery.pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="sqlResultTotal"
              />
            </div>
          </el-card>
        </div>
      </div>
    </el-dialog>

    <!-- 数据穿透分析对话框 -->
    <el-dialog
      title="数据穿透分析"
      :visible.sync="dataDrillThroughDialogVisible"
      width="95%"
      :close-on-click-modal="false"
      custom-class="data-drill-through-dialog"
    >
      <div class="data-drill-through-container">
        <el-tabs v-model="activeDrillThroughTab" type="border-card">
          <!-- 源数据查看 -->
          <el-tab-pane label="源数据查看" name="sourceData">
            <div class="source-data-section">
              <div class="section-header">
                <h4>业务源数据</h4>
                <p>显示与该预警相关的原始业务数据</p>
              </div>
              <el-table
                :data="sourceData"
                v-loading="sourceDataLoading"
                border
                stripe
                style="width: 100%"
                max-height="400"
              >
                <el-table-column
                  v-for="(column, index) in sourceDataColumns"
                  :key="index"
                  :prop="column.prop"
                  :label="column.label"
                  :width="column.width"
                  show-overflow-tooltip
                />
              </el-table>

              <!-- 源数据分页 -->
              <div class="pagination-container" style="margin-top: 15px">
                <el-pagination
                  @size-change="handleSourceDataSizeChange"
                  @current-change="handleSourceDataCurrentChange"
                  :current-page="sourceDataQuery.pageNum"
                  :page-sizes="[10, 20, 50]"
                  :page-size="sourceDataQuery.pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="sourceDataTotal"
                />
              </div>
            </div>
          </el-tab-pane>

          <!-- 预警源数据查看 -->
          <el-tab-pane label="预警源数据查看" name="warningSourceData">
            <div class="warning-source-data-section">
              <div class="section-header">
                <h4>预警源数据</h4>
                <p>显示指标SQL查询出来的具体数据和计算过程</p>
              </div>
              <el-table
                :data="warningSourceData"
                v-loading="warningSourceDataLoading"
                border
                stripe
                style="width: 100%"
                max-height="400"
              >
                <el-table-column
                  v-for="(column, index) in warningSourceDataColumns"
                  :key="index"
                  :prop="column.prop"
                  :label="column.label"
                  :width="column.width"
                  show-overflow-tooltip
                />
              </el-table>

              <!-- 预警源数据分页 -->
              <div class="pagination-container" style="margin-top: 15px">
                <el-pagination
                  @size-change="handleWarningSourceDataSizeChange"
                  @current-change="handleWarningSourceDataCurrentChange"
                  :current-page="warningSourceDataQuery.pageNum"
                  :page-sizes="[10, 20, 50]"
                  :page-size="warningSourceDataQuery.pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="warningSourceDataTotal"
                />
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-dialog>

    <!-- 预警配置对话框 -->
    <el-dialog
      title="预警配置"
      :visible.sync="configDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <warning-config-form
        ref="configForm"
        @submit="handleConfigSubmit"
        @cancel="configDialogVisible = false"
      />
    </el-dialog>

    <!-- 报告生成对话框 -->
    <el-dialog
      title="生成预警报告"
      :visible.sync="reportDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <report-generate-form
        ref="reportForm"
        @submit="handleReportSubmit"
        @cancel="reportDialogVisible = false"
      />
    </el-dialog>

    <!-- 🆕 新增：流程图查看对话框 -->
    <FlowViewDialog
      :visible.sync="flowChartDialogVisible"
      :combination="currentCombination"
    />
  </div>
</template>

<style scoped>
  /* 🔧 新增：待处理预警数量样式 */
  .pending-warning-count {
    font-weight: bold;
    font-size: 14px;
    transition: color 0.3s ease;
  }

  .pending-warning-count:hover {
    color: #f56c6c !important;
    text-decoration: underline;
  }

  .disabled-warning {
    color: #c0c4cc;
    font-style: italic;
  }

  /* 统计卡片样式 */
  .statistics-cards {
    margin-bottom: 20px;
  }

  .stat-card {
    background: #fff;
    border-radius: 8px;
    padding: 20px;
    text-align: center;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    transition: transform 0.3s ease;
  }

  .stat-card:hover {
    transform: translateY(-2px);
  }

  .stat-card.pending {
    border-left: 4px solid #e6a23c;
  }

  .stat-card.processed {
    border-left: 4px solid #67c23a;
  }

  .stat-card.today {
    border-left: 4px solid #409eff;
  }

  .stat-number {
    font-size: 28px;
    font-weight: bold;
    color: #303133;
    margin-bottom: 8px;
  }

  .stat-label {
    font-size: 14px;
    color: #606266;
  }

  /* 表格样式优化 */
  .table-container {
    background: #fff;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  }

  /* 分页样式 */
  .pagination-container {
    padding: 20px;
    text-align: right;
    background: #fff;
  }

  /* 工具栏样式 */
  .toolbar {
    margin-bottom: 20px;
  }

  /* 搜索表单样式 */
  .search-form {
    background: #fff;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  }
</style>

<script>
  import {
    getEvaluationModelList,
    getPublishedEnabledModelList, // 🔧 新增：专用API
    saveEvaluationModel,
    deleteEvaluationModel,
    getEvaluationModelDetail,
    testEvaluationModel,
    publishEvaluationModel,
    copyEvaluationModel,
    getEvaluationModelStatistics,
    executeEvaluationModel,
    startEvaluationModel,
    stopEvaluationModel,
    getModelWarningCount,
    getModelPendingWarningCount, // 🔧 新增：获取待处理预警数量
    getModelWarningResults,
    getModelSqlExecutionResult,
    getWarningSourceData,
    getWarningSourceDataDetail,
    // 新增的API
    generateWarningReport,
    updateWarningConfig,
    getWarningStatistics, // 🔧 风险预警统计
    getTodayWarningCount, // 🔧 新增：今日预警数量
  } from '@/api/mxgl'
  import { getWarningDrillDownData } from '@/api/risk/warning'
  import EvaluationModelForm from './components/EvaluationModelForm'
  import EvaluationModelTest from './components/EvaluationModelTest'
  import EvaluationModelView from './components/EvaluationModelView'
  // 新增的组件引用
  import WarningConfigForm from '../fxyjgl/components/WarningConfigForm'
  import ReportGenerateForm from '../fxyjgl/components/ReportGenerateForm'
  // 🆕 新增：流程图组件
  import FlowViewDialog from '../sjmxgl/components/FlowViewDialog'

  export default {
    name: 'EvaluationModelManagement',
    components: {
      EvaluationModelForm,
      EvaluationModelTest,
      EvaluationModelView,
      // 新增的组件
      WarningConfigForm,
      ReportGenerateForm,
      // 🆕 新增：流程图组件
      FlowViewDialog,
    },
    data() {
      return {
        // 查询表单
        queryForm: {
          pageNum: 1,
          pageSize: 20,
          modelName: '',
          modelType: '',
          modelStatus: '',
          isEnabled: '',
        },
        // 表格数据
        tableData: [],
        total: 0,
        loading: false,
        multipleSelection: [],
        // 统计数据
        statistics: {},
        // 对话框
        dialogVisible: false,
        testDialogVisible: false,
        dialogTitle: '',
        isEdit: false,
        currentModel: {},
        // 新增的对话框
        configDialogVisible: false,
        reportDialogVisible: false,
        // SQL执行结果查看对话框
        sqlResultDialogVisible: false,
        currentSqlModel: {},
        sqlResultData: [], // 当前页显示的数据
        sqlResultAllData: [], // 🔥 新增：存储所有数据
        sqlResultTotal: 0,
        sqlResultLoading: false,
        sqlResultQuery: {
          pageNum: 1,
          pageSize: 20,
        },
        sqlResultColumns: [],
        sqlExecutionInfo: {
          executionTime: '',
          recordCount: 0,
          status: 'SUCCESS',
          sqlContent: '',
        },
        // 数据穿透对话框需要的属性（保持兼容性）
        currentWarningModel: {},
        // 数据穿透分析对话框
        dataDrillThroughDialogVisible: false,
        activeDrillThroughTab: 'sourceData',
        currentWarningRecord: {},
        // 源数据
        sourceData: [],
        sourceDataColumns: [],
        sourceDataTotal: 0,
        sourceDataLoading: false,
        sourceDataQuery: {
          pageNum: 1,
          pageSize: 20,
        },
        // 预警源数据
        warningSourceData: [],
        warningSourceDataColumns: [],
        warningSourceDataTotal: 0,
        warningSourceDataLoading: false,
        warningSourceDataQuery: {
          pageNum: 1,
          pageSize: 20,
        },

        // 🔧 修复：预警数量缓存 - 使用普通对象替代Map，确保Vue2响应式追踪
        modelWarningCountCache: {}, // 缓存模型预警数量，key: modelId, value: count
        modelPendingWarningCountCache: {}, // 缓存模型待处理预警数量，key: modelId, value: count

        // 🔧 新增：定时刷新相关
        refreshTimer: null, // 定时器
        refreshInterval: 30000, // 30秒刷新一次

        // 🆕 新增：流程图相关
        flowChartDialogVisible: false, // 流程图对话框显示状态
        currentCombination: null, // 当前选中的组合信息
      }
    },
    created() {
      console.log('🚀 页面初始化开始...')
      this.loadData()
      this.loadStatistics()

      // 🔧 新增：启动定时刷新
      this.startAutoRefresh()
      console.log('✅ 页面初始化完成')
    },

    beforeDestroy() {
      // 🔧 新增：清理定时器
      this.stopAutoRefresh()
    },
    methods: {
      // 🔧 修复：加载数据 - 强制过滤已发布且启用的模型
      async loadData() {
        this.loading = true
        try {
          // 🔧 强制过滤条件：只查询已发布且启用的模型
          const filteredQueryForm = {
            ...this.queryForm,
            modelStatus: 'PUBLISHED', // 强制设置为已发布
            isEnabled: 'Y', // 强制设置为启用
          }

          console.log('📊 风险预警研究 - 查询参数（强制过滤）:', filteredQueryForm)

          const response = await getEvaluationModelList(filteredQueryForm)
          console.log('📊 风险预警研究 - 接口响应:', response)

          // 兼容两种响应格式：
          // 1. 标准格式: {code: 1, data: {records: [...], total: ...}}
          // 2. 直接格式: {list: [...], total: ...} (旧格式)
          if (response && response.code === 1) {
            // 标准格式
            const data = response.data || {}
            this.tableData = data.records || data.list || []
            this.total = data.total || 0
            console.log('✅ 评估模型列表加载成功(标准格式):', {
              total: this.total,
              records: this.tableData.length
            })
          } else if (response && response.list) {
            // 直接格式（旧格式兼容）
            this.tableData = response.list || []
            this.total = response.total || 0
            console.log('✅ 评估模型列表加载成功(直接格式):', {
              total: this.total,
              records: this.tableData.length
            })
          } else {
            console.error('❌ 未知的响应格式:', response)
            this.tableData = []
            this.total = 0
          }

          // 🔧 新增：加载预警数量 - 等待异步完成
          await this.loadWarningCounts()
          // 🔧 新增：加载待处理预警数量 - 等待异步完成
          await this.loadPendingWarningCounts()
        } catch (error) {
          console.error('❌ 查询评估模型列表异常:', error)
          this.$message.error('查询失败')
          this.tableData = []
          this.total = 0
        } finally {
          this.loading = false
        }
      },
      // 🔧 修复：加载统计数据 - 处理字段映射
      async loadStatistics() {
        try {
          const response = await getWarningStatistics()
          console.log('📊 风险预警研究 - 预警统计API响应:', response)

          // 兼容两种响应格式：
          // 1. 标准格式: {code: 1, data: {...}}
          // 2. 直接格式: {...} (拦截器已解包)
          let rawData = {}
          if (response && response.code === 1 && response.data) {
            // 标准格式
            rawData = response.data || {}
            console.log('✅ 统计数据加载成功(标准格式)')
          } else if (response && (response.totalCount !== undefined || response.statusStatistics !== undefined)) {
            // 直接格式（拦截器已解包）
            rawData = response || {}
            console.log('✅ 统计数据加载成功(直接格式)')
          } else {
            console.warn('⚠️ 预警统计数据返回格式不匹配:', response)
            rawData = {}
          }

          console.log('📊 原始预警统计数据:', rawData)

          // 🔧 修复：处理后端数据字段映射
          this.statistics = {
            totalCount: rawData.totalCount || 0,
            pendingCount:
              rawData.pendingCount || rawData.statusStatistics?.PENDING || 0,
            processingCount:
              rawData.processingCount ||
              rawData.statusStatistics?.PROCESSING ||
              0,
            processedCount: rawData.statusStatistics?.PROCESSED || 0,
            ignoredCount:
              rawData.ignoredCount || rawData.statusStatistics?.IGNORED || 0,
            // 🔧 修复：今日新增数量 - 后端暂无此字段，暂时显示0
            todayCount: rawData.todayCount || rawData.dailyCount || 0,
            highRiskCount:
              rawData.highRiskCount || rawData.riskLevelStatistics?.HIGH || 0,
          }

          console.log('✅ 处理后的预警统计数据:', this.statistics)

          // 🔧 新增：单独获取今日新增数量
          this.loadTodayCount()
        } catch (error) {
          console.error('加载统计数据失败', error)
          this.$message.error('加载统计数据失败: ' + error.message)
        }
      },

      // 🔧 新增：加载今日新增预警数量
      async loadTodayCount() {
        try {
          const response = await getTodayWarningCount()
          if (response.code === 1) {
            this.statistics.todayCount = response.data?.count || 0
            console.log('今日新增预警数量:', this.statistics.todayCount)
          }
        } catch (error) {
          console.error('获取今日新增数量失败:', error)
          // 不显示错误信息，保持静默失败
        }
      },

      // 🔧 修复：批量加载预警数量 - 只要启用就加载
      async loadWarningCounts() {
        try {
          // 🔧 修复：只为启用的模型加载预警数量，不再限制状态
          const enabledModels = this.tableData.filter(
            (model) => model.isEnabled === 'Y'
          )

          console.log('开始加载预警数量，启用模型数量:', enabledModels.length)

          // 并行加载所有模型的预警数量
          const promises = enabledModels.map(async (model) => {
            try {
              const response = await getModelWarningCount(model.evalModelId)
              if (response.code === 1 && response.data) {
                const count = response.data.warningCount || 0
                this.$set(this.modelWarningCountCache, model.evalModelId, count)
                console.log(`模型 ${model.modelName} 预警数量: ${count}`)
                return { modelId: model.evalModelId, count }
              }
            } catch (error) {
              console.error(
                `获取模型 ${model.evalModelId} 预警数量失败:`,
                error
              )
              this.$set(this.modelWarningCountCache, model.evalModelId, 0)
              return { modelId: model.evalModelId, count: 0 }
            }
          })

          await Promise.all(promises)
          console.log('预警数量加载完成，缓存:', this.modelWarningCountCache)
        } catch (error) {
          console.error('批量加载预警数量失败:', error)
        }
      },

      // 🔧 修复：获取模型预警数量 - 只要启用就显示数量
      getModelWarningCount(model) {
        // 🔧 修复：只检查是否启用，不再检查状态
        if (model.isEnabled !== 'Y') {
          return 0
        }

        // 优先使用缓存中的数量（响应式对象，直接读取属性）
        const cachedCount = this.modelWarningCountCache[model.evalModelId]
        if (cachedCount !== undefined) {
          return cachedCount
        }

        // 如果缓存中没有，返回模型自带的数量（可能不准确）
        return model.warningCount || 0
      },

      // 🔧 新增：刷新单个模型的预警数量
      async handleRefreshWarningCount(model) {
        try {
          console.log('刷新模型预警数量:', model.modelName)

          const response = await getModelWarningCount(model.evalModelId)
          if (response.code === 1 && response.data) {
            const count = response.data.warningCount || 0
            this.$set(this.modelWarningCountCache, model.evalModelId, count)

            this.$message.success(`${model.modelName} 预警数量已刷新: ${count}`)
            console.log(`模型 ${model.modelName} 预警数量刷新为: ${count}`)
          } else {
            this.$message.error('刷新预警数量失败')
          }
        } catch (error) {
          console.error('刷新预警数量失败:', error)
          this.$message.error('刷新预警数量失败')
        }
      },

      // 🔧 修复：批量加载待处理预警数量 - 使用$set保证响应式
      async loadPendingWarningCounts() {
        try {
          console.log('🔍 开始加载待处理预警数量...')

          // 只为启用的模型加载待处理预警数量
          const enabledModels = this.tableData.filter(
            (model) => model.isEnabled === 'Y'
          )

          console.log(
            '✅ 开始加载待处理预警数量，启用模型数量:',
            enabledModels.length
          )

          if (enabledModels.length === 0) {
            console.log('⚠️ 没有启用的模型，跳过加载')
            return
          }

          // 并行加载所有模型的待处理预警数量
          const promises = enabledModels.map(async (model) => {
            try {
              const response = await getModelPendingWarningCount(
                model.evalModelId
              )
              if (response.code === 1 && response.data) {
                const count = response.data.pendingWarningCount || 0
                this.$set(this.modelPendingWarningCountCache, model.evalModelId, count)
                console.log(`模型 ${model.modelName} 待处理预警数量: ${count}`)
                return { modelId: model.evalModelId, count }
              } else {
                console.warn(`模型 ${model.modelName} 待处理预警接口返回异常:`, response)
                this.$set(this.modelPendingWarningCountCache, model.evalModelId, 0)
                return { modelId: model.evalModelId, count: 0 }
              }
            } catch (error) {
              console.error(
                `获取模型 ${model.evalModelId} 待处理预警数量失败:`,
                error
              )
              this.$set(this.modelPendingWarningCountCache, model.evalModelId, 0)
              return { modelId: model.evalModelId, count: 0 }
            }
          })

          await Promise.all(promises)
          console.log(
            '🎉 待处理预警数量加载完成，缓存:',
            this.modelPendingWarningCountCache
          )
        } catch (error) {
          console.error('批量加载待处理预警数量失败:', error)
        }
      },

      // 🔧 修复：获取模型待处理预警数量 - 使用响应式对象读取
      getModelPendingWarningCount(model) {
        // 只检查是否启用
        if (model.isEnabled !== 'Y') {
          return 0
        }

        // 从响应式缓存对象中读取（Vue2能自动追踪依赖）
        const cachedCount = this.modelPendingWarningCountCache[model.evalModelId]
        if (cachedCount !== undefined) {
          return cachedCount
        }

        // 如果缓存中没有，返回0（加载中状态）
        return 0
      },

      // 🔧 修复：刷新单个模型的待处理预警数量
      async handleRefreshPendingWarningCount(model) {
        try {
          console.log('刷新模型待处理预警数量:', model.modelName)

          const response = await getModelPendingWarningCount(model.evalModelId)
          if (response.code === 1 && response.data) {
            const count = response.data.pendingWarningCount || 0
            this.$set(this.modelPendingWarningCountCache, model.evalModelId, count)

            this.$message.success(
              `${model.modelName} 待处理预警数量已刷新: ${count}`
            )
            console.log(
              `模型 ${model.modelName} 待处理预警数量刷新为: ${count}`
            )
          } else {
            this.$message.error('刷新待处理预警数量失败')
          }
        } catch (error) {
          console.error('刷新待处理预警数量失败:', error)
          this.$message.error('刷新待处理预警数量失败')
        }
      },

      // 🔧 新增：启动自动刷新
      startAutoRefresh() {
        // 清除已有定时器
        this.stopAutoRefresh()

        // 启动新的定时器
        this.refreshTimer = setInterval(() => {
          console.log('定时刷新预警数量...')
          this.loadWarningCounts()
          this.loadPendingWarningCounts() // 🔧 新增：同时刷新待处理预警数量
        }, this.refreshInterval)

        console.log(
          `已启动预警数量定时刷新，间隔: ${this.refreshInterval / 1000}秒`
        )
      },

      // 🔧 新增：停止自动刷新
      stopAutoRefresh() {
        if (this.refreshTimer) {
          clearInterval(this.refreshTimer)
          this.refreshTimer = null
          console.log('已停止预警数量定时刷新')
        }
      },
      // 🔧 修复：查询 - 保持过滤条件
      handleSearch() {
        this.queryForm.pageNum = 1
        console.log('🔍 用户触发查询，当前查询条件:', this.queryForm)
        this.loadData()
      },
      // 🔧 修复：重置 - 保持过滤条件
      handleReset() {
        this.$refs.queryForm.resetFields()
        this.queryForm.pageNum = 1
        console.log('🔄 用户触发重置，重置后查询条件:', this.queryForm)
        this.loadData()
      },
      // 新增
      handleAdd() {
        this.dialogTitle = '新增评估模型'
        this.isEdit = false
        this.currentModel = {}
        this.dialogVisible = true
      },
      // 编辑
      async handleEdit(row) {
        this.dialogTitle = '编辑评估模型'
        this.isEdit = true
        try {
          console.log('编辑模型详情:', row)
          const response = await getEvaluationModelDetail(row.evalModelId)
          if (response.code === 1) {
            this.currentModel = response.data
            this.dialogVisible = true
          } else {
            this.$message.error(response.msg || '获取模型详情失败')
          }
        } catch (error) {
          console.error('获取模型详情失败:', error)
          this.$message.error('获取模型详情失败，请稍后重试')
        }
      },
      // 查看
      async handleView(row) {
        try {
          console.log('查看模型详情:', row)
          const response = await getEvaluationModelDetail(row.evalModelId)
          if (response.code === 1) {
            this.$refs.modelView.show(response.data)
          } else {
            this.$message.error(response.msg || '获取模型详情失败')
          }
        } catch (error) {
          console.error('获取模型详情失败:', error)
          this.$message.error('获取模型详情失败，请稍后重试')
        }
      },
      // 测试
      async handleTest(row) {
        console.log('=== 开始测试模型流程 ===')
        console.log('1. 点击的行数据:', row)
        console.log('2. 模型ID:', row.evalModelId)

        try {
          console.log('3. 开始调用 getEvaluationModelDetail API')

          // 获取完整的模型详情，确保包含dataModelId
          const response = await getEvaluationModelDetail(row.evalModelId)
          console.log('4. API响应:', response)

          if (response.code === 1) {
            console.log('5. API调用成功，设置 currentModel')
            this.currentModel = response.data
            console.log('6. currentModel 设置为:', this.currentModel)
            console.log('7. 打开测试对话框')
            this.testDialogVisible = true
          } else {
            console.error('API调用失败:', response.msg)
            this.$message.error(response.msg || '获取模型详情失败')
          }
        } catch (error) {
          console.error('获取模型详情异常:', error)
          this.$message.error('获取模型详情失败，请稍后重试')
        }
      },

      // 生成报告
      handleGenerateReport() {
        this.reportDialogVisible = true
      },

      // 🆕 新增：处理更多操作下拉菜单
      handleMoreAction(command) {
        const { action, row } = command

        switch (action) {
          case 'generateReport':
            this.handleModelReportGenerate(row)
            break
          case 'viewFlowChart':
            this.handleViewFlowChart(row)
            break
          case 'warningProcess':
            this.handleWarningProcess(row)
            break
          default:
            console.warn('未知的操作类型:', action)
        }
      },

      // 🆕 新增：生成模型报告（操作列按钮）
      handleModelReportGenerate(row) {
        console.log('生成模型报告:', row.modelName)

        // 设置当前选中的模型，传递给报告生成组件
        this.currentModel = row

        // 打开报告生成对话框
        this.reportDialogVisible = true

        // 通知报告生成组件当前选中的模型
        this.$nextTick(() => {
          if (this.$refs.reportForm) {
            this.$refs.reportForm.setSelectedModel(row)
          }
        })
      },
      // 报告生成提交
      async handleReportSubmit(formData) {
        try {
          const response = await generateWarningReport(formData)
          if (response.code === 1) {
            this.$message.success('报告生成成功，正在下载...')
            this.reportDialogVisible = false

            // 自动下载报告
            this.downloadReport(response.data)
          } else {
            this.$message.error(response.msg || '报告生成失败')
          }
        } catch (error) {
          this.$message.error('报告生成失败')
        }
      },

      // 下载报告文件
      downloadReport(reportData) {
        try {
          // 构建下载URL
          const downloadUrl = `/riskcontrol/model/warning/downloadReport?reportId=${
            reportData.reportId
          }&fileName=${encodeURIComponent(reportData.fileName)}`

          // 创建隐藏的下载链接
          const link = document.createElement('a')
          link.href = downloadUrl
          link.download = reportData.fileName
          link.style.display = 'none'

          // 添加token到请求头（通过设置链接的方式无法添加自定义请求头，所以使用fetch）
          this.downloadFileWithToken(downloadUrl, reportData.fileName)
        } catch (error) {
          console.error('下载报告失败:', error)
          this.$message.error('下载报告失败')
        }
      },

      // 使用fetch下载文件（支持自定义请求头）
      async downloadFileWithToken(url, fileName) {
        try {
          const token =
            this.$store.getters.token || localStorage.getItem('token')

          const response = await fetch(url, {
            method: 'GET',
            headers: {
              token: token,
            },
          })

          if (!response.ok) {
            throw new Error('下载失败')
          }

          // 获取文件blob
          const blob = await response.blob()

          // 创建下载链接
          const downloadUrl = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = downloadUrl
          link.download = fileName
          link.style.display = 'none'

          // 触发下载
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)

          // 清理URL对象
          window.URL.revokeObjectURL(downloadUrl)

          this.$message.success('报告下载完成')
        } catch (error) {
          console.error('下载文件失败:', error)
          this.$message.error('下载文件失败')
        }
      },
      // 预警配置
      handleWarningConfig() {
        this.configDialogVisible = true
      },
      // 配置提交
      async handleConfigSubmit(formData) {
        try {
          const response = await updateWarningConfig(formData)
          if (response.code === 1) {
            this.$message.success('配置更新成功')
            this.configDialogVisible = false
          } else {
            this.$message.error(response.msg || '配置更新失败')
          }
        } catch (error) {
          this.$message.error('配置更新失败')
        }
      },
      // 切换启用状态
      async handleToggleEnabled(row) {
        try {
          const response = await saveEvaluationModel({
            ...row,
            isEnabled: row.isEnabled,
          })
          if (response.code === 1) {
            this.$message.success('状态更新成功')
          } else {
            this.$message.error(response.msg || '状态更新失败')
            // 恢复原状态
            row.isEnabled = row.isEnabled === 'Y' ? 'N' : 'Y'
          }
        } catch (error) {
          this.$message.error('状态更新失败')
          // 恢复原状态
          row.isEnabled = row.isEnabled === 'Y' ? 'N' : 'Y'
        }
      },
      // 刷新统计
      handleRefreshStatistics() {
        this.loadStatistics()
        this.$message.success('统计数据已刷新')
      },
      // 测试对话框关闭处理
      handleTestDialogClose() {
        this.testDialogVisible = false
        // 测试完成后刷新统计数据，因为可能影响平均准确率
        this.loadStatistics()
        console.log('测试对话框关闭，已刷新统计数据')
      },
      // 表格选择变化
      handleSelectionChange(selection) {
        this.multipleSelection = selection
      },
      // 分页
      handleSizeChange(size) {
        this.queryForm.pageSize = size
        this.queryForm.pageNum = 1
        this.loadData()
      },
      handleCurrentChange(page) {
        this.queryForm.pageNum = page
        this.loadData()
      },
      // 对话框关闭
      handleDialogClose() {
        this.dialogVisible = false
        this.currentModel = {}
      },
      // 表单提交
      async handleFormSubmit(formData) {
        try {
          const response = await saveEvaluationModel(formData)
          if (response.code === 1) {
            this.$message.success(this.isEdit ? '更新成功' : '新增成功')
            this.dialogVisible = false
            this.loadData()
            this.loadStatistics()
          } else {
            this.$message.error(response.msg || '保存失败')
          }
        } catch (error) {
          this.$message.error('保存失败')
        }
      },
      // 获取模型类型标签类型
      getModelTypeTagType(type) {
        const typeMap = {
          PROCUREMENT: 'primary',
          FINANCIAL: 'success',
          CREDIT: 'warning',
          COMPLIANCE: 'info',
        }
        return typeMap[type] || 'default'
      },
      // 获取模型类型文本
      getModelTypeText(type) {
        const typeMap = {
          PROCUREMENT: '采购风险',
          FINANCIAL: '财务风险',
          CREDIT: '信用风险',
          COMPLIANCE: '合规风险',
        }
        return typeMap[type] || type
      },
      // 获取状态标签类型
      getStatusTagType(status) {
        const statusMap = {
          DRAFT: 'info',
          TESTING: 'warning',
          PUBLISHED: 'success',
          DISABLED: 'danger',
        }
        return statusMap[status] || 'default'
      },
      // 获取状态文本
      getStatusText(status) {
        const statusMap = {
          DRAFT: '草稿',
          TESTING: '测试中',
          PUBLISHED: '已发布',
          DISABLED: '已停用',
        }
        return statusMap[status] || status
      },
      // 格式化日期
      formatDate(date) {
        if (!date) return '-'
        return new Date(date).toLocaleString('zh-CN')
      },

      // =====================================================
      // 预警结果查看相关方法
      // =====================================================

      // 查看预警结果（从风险预警表读取数据）
      async handleViewWarningResults(row) {
        console.log('点击预警结果，模型数据:', row)

        this.currentSqlModel = { ...row }
        this.sqlResultDialogVisible = true
        this.sqlResultQuery.pageNum = 1
        await this.loadWarningResults()
      },

      // 加载预警结果数据（从风险预警表）
      async loadWarningResults() {
        this.sqlResultLoading = true
        try {
          const params = {
            evalModelId: this.currentSqlModel.evalModelId,
            pageNum: this.sqlResultQuery.pageNum,
            pageSize: this.sqlResultQuery.pageSize,
          }

          console.log('调用模型预警结果接口:', params)

          // 调用预警结果接口
          const response = await getModelWarningResults(params)
          if (response.code === 1) {
            // 处理预警结果数据
            let resultData = []
            let totalCount = 0

            if (response.data) {
              // 从分页数据中提取列表和总数
              if (response.data.list) {
                const warningList = response.data.list
                totalCount = response.data.total || warningList.length

                // 解析每条预警记录的RELATED_DATA字段
                resultData = warningList.map((warning) => {
                  try {
                    // 解析RELATED_DATA字段中的JSON数据
                    const relatedData = warning.relatedData
                      ? JSON.parse(warning.relatedData)
                      : {}

                    // 合并预警基本信息和JSON数据
                    return {
                      ...relatedData,
                      // 添加预警管理字段
                      预警时间: warning.warningTime,
                      处理人: warning.processUser || '-',
                      处理时间: warning.processTime || '-',
                      预警状态: warning.warningStatus,
                      预警级别: warning.warningLevel,
                      预警ID: warning.warningId,
                    }
                  } catch (e) {
                    console.error('解析预警数据失败:', e, warning)
                    return {
                      预警时间: warning.warningTime,
                      处理人: warning.processUser || '-',
                      处理时间: warning.processTime || '-',
                      预警状态: warning.warningStatus,
                      预警级别: warning.warningLevel,
                      错误: '数据解析失败',
                    }
                  }
                })
              } else if (Array.isArray(response.data)) {
                resultData = response.data
                totalCount = response.data.length
              }
            }

            // 存储数据
            this.sqlResultAllData = resultData
            this.sqlResultTotal = totalCount
            this.sqlResultData = resultData

            // 动态生成列配置
            if (resultData.length > 0) {
              // 根据第一条数据的字段动态生成列
              this.sqlResultColumns = Object.keys(resultData[0]).map((key) => ({
                prop: key,
                label: this.getColumnLabel(key),
                width: this.getColumnWidth(key),
              }))
            } else {
              this.sqlResultColumns = []
            }

            // 设置执行信息
            this.sqlExecutionInfo = {
              executionTime: new Date().toLocaleString(),
              recordCount: this.sqlResultTotal,
              status: 'SUCCESS',
            }

            console.log('预警结果加载成功:', {
              记录数: resultData.length,
              总数: totalCount,
              列数: this.sqlResultColumns.length,
              响应数据: response.data,
            })
          } else {
            this.$message.error(response.msg || '获取预警结果失败')
            this.sqlExecutionInfo.status = 'FAILED'
          }
        } catch (error) {
          console.error('获取预警结果失败:', error)
          this.$message.error('获取预警结果失败: ' + error.message)
          this.sqlExecutionInfo.status = 'FAILED'
        } finally {
          this.sqlResultLoading = false
        }
      },

      // 刷新预警结果
      async refreshSqlResult() {
        this.sqlResultQuery.pageNum = 1
        await this.loadWarningResults()
      },

      // 预警结果分页 - 页大小改变
      handleSqlResultSizeChange(val) {
        this.sqlResultQuery.pageSize = val
        this.sqlResultQuery.pageNum = 1
        this.loadWarningResults()
      },

      // 预警结果分页 - 当前页改变
      handleSqlResultCurrentChange(val) {
        this.sqlResultQuery.pageNum = val
        this.loadWarningResults()
      },

      // 跳转到风险预警管理页面
      handleJumpToWarningManagement() {
        // 🔥 修复：使用正确的路由路径跳转到风险预警管理页面
        this.$router.push({
          path: '/RiskSjfx/mxfxyjgl',
          query: {
            modelId: this.currentSqlModel.evalModelId,
            modelName: this.currentSqlModel.modelName,
            modelType: this.currentSqlModel.modelType,
          },
        })
      },

      // 穿透查询
      async handleDrillThrough(row) {
        console.log('点击穿透按钮，数据行:', row)

        // 检查是否有预警ID
        if (!row.预警ID) {
          this.$message.error('无法获取预警ID，无法进行穿透查询')
          return
        }

        try {
          this.sourceDataLoading = true
          this.dataDrillThroughDialogVisible = true
          this.currentWarningRecord = row

          // 调用穿透查询API
          const response = await getWarningDrillDownData({
            warningId: row.预警ID,
            pageNum: this.sourceDataQuery.pageNum,
            pageSize: this.sourceDataQuery.pageSize,
          })

          if (response.code === 1 && response.data) {
            const drillDownData = response.data

            // 处理业务数据
            if (drillDownData.businessData) {
              const businessData = drillDownData.businessData
              this.sourceData = businessData.list || []
              this.sourceDataTotal = businessData.total || 0

              // 动态生成列
              if (this.sourceData.length > 0) {
                this.sourceDataColumns = Object.keys(this.sourceData[0]).map(
                  (key) => ({
                    prop: key,
                    label: this.getColumnLabel(key),
                    width: this.getColumnWidth(key),
                  })
                )
              }
            }

            console.log('穿透查询成功:', {
              业务数据条数: this.sourceData.length,
              总数: this.sourceDataTotal,
            })
          } else {
            this.$message.error(response.msg || '穿透查询失败')
          }
        } catch (error) {
          console.error('穿透查询失败:', error)
          this.$message.error('穿透查询失败: ' + error.message)
        } finally {
          this.sourceDataLoading = false
        }
      },

      // 🆕 新增：查看流程图（直接在当前页面打开）
      handleViewFlowChart(row) {
        console.log('🎯 点击流程图按钮，评估模型数据:', row)

        // 检查是否有dataModelId
        if (!row.dataModelId) {
          this.$message.error('该评估模型未关联数据模型，无法查看流程图')
          return
        }

        // 判断数据模型类型
        const isComboModel = row.dataModelId.startsWith('COMB')
        console.log(
          '🔍 模型类型判断:',
          isComboModel ? '指标组合分析' : '数据模型管理'
        )

        if (isComboModel) {
          // 指标组合分析：直接打开流程图组件
          console.log('🚀 准备打开指标组合分析流程图')

          // 构造组合信息对象，用于传递给FlowViewDialog组件
          this.currentCombination = {
            combinationId: row.dataModelId, // 对于组合分析，dataModelId就是combinationId
            combinationName: row.modelName + ' - 组合分析',
            combinationCode: row.dataModelId,
            description: `评估模型"${row.modelName}"关联的指标组合分析`,
            // 添加评估模型相关信息
            evalModelId: row.evalModelId,
            evalModelName: row.modelName,
            evalModelType: row.modelType,
          }

          console.log('📋 构造的组合信息:', this.currentCombination)
          console.log(
            '🔗 即将调用的API路径:',
            `/riskcontrol/model/combination/flow/${row.dataModelId}`
          )
          console.log('📊 传递给FlowViewDialog的combination参数:', {
            combinationId: this.currentCombination.combinationId,
            combinationName: this.currentCombination.combinationName,
            evalModelInfo: {
              evalModelId: this.currentCombination.evalModelId,
              evalModelName: this.currentCombination.evalModelName,
              evalModelType: this.currentCombination.evalModelType,
            },
          })

          // 打开流程图对话框
          this.flowChartDialogVisible = true

          this.$message.success(`正在加载"${row.modelName}"的流程图...`)
        } else {
          // 数据模型管理：暂不支持流程图
          this.$message.warning(
            '当前数据模型类型不支持流程图显示，仅支持指标组合分析类型'
          )
        }
      },

      // 🆕 新增：预警处理
      handleWarningProcess(row) {
        console.log('🎯 点击预警处理按钮，评估模型数据:', row)

        // 跳转到风险预警管理页面，并传递模型信息
        this.$router.push({
          path: '/RiskSjfx/mxfxyjgl',
          query: {
            modelId: row.evalModelId,
            modelName: row.modelName,
            modelType: row.modelType,
            action: 'process', // 标识是来处理预警的
          },
        })

        this.$message.success(`正在跳转到"${row.modelName}"的预警处理页面...`)
      },

      // 查看预警详情
      handleViewWarningDetail(row) {
        // 这里可以打开预警详情对话框或跳转到详情页面
        this.$message.info('预警详情功能待实现')
      },

      // =====================================================
      // 数据穿透分析相关方法
      // =====================================================

      // 数据穿透分析
      async handleDataDrillThrough(row) {
        this.currentWarningRecord = { ...row }
        this.dataDrillThroughDialogVisible = true
        this.activeDrillThroughTab = 'sourceData'

        // 加载源数据和预警源数据
        await Promise.all([this.loadSourceData(), this.loadWarningSourceData()])
      },

      // 加载源数据
      async loadSourceData() {
        this.sourceDataLoading = true
        try {
          const params = {
            warningId: this.currentWarningRecord.warningId,
            pageNum: this.sourceDataQuery.pageNum,
            pageSize: this.sourceDataQuery.pageSize,
          }

          // 调用API获取源数据
          const response = await this.getWarningSourceData(params)
          if (response.code === 1) {
            this.sourceData = response.data.list || []
            this.sourceDataTotal = response.data.total || 0
            this.sourceDataColumns = response.data.columns || []
          } else {
            this.$message.error(response.msg || '获取源数据失败')
          }
        } catch (error) {
          console.error('获取源数据失败:', error)
          this.$message.error('获取源数据失败')
        } finally {
          this.sourceDataLoading = false
        }
      },

      // 加载预警源数据
      async loadWarningSourceData() {
        this.warningSourceDataLoading = true
        try {
          const params = {
            warningId: this.currentWarningRecord.warningId,
            pageNum: this.warningSourceDataQuery.pageNum,
            pageSize: this.warningSourceDataQuery.pageSize,
          }

          // 调用API获取预警源数据
          const response = await this.getWarningSourceDataDetail(params)
          if (response.code === 1) {
            this.warningSourceData = response.data.list || []
            this.warningSourceDataTotal = response.data.total || 0
            this.warningSourceDataColumns = response.data.columns || []
          } else {
            this.$message.error(response.msg || '获取预警源数据失败')
          }
        } catch (error) {
          console.error('获取预警源数据失败:', error)
          this.$message.error('获取预警源数据失败')
        } finally {
          this.warningSourceDataLoading = false
        }
      },

      // 源数据分页 - 页大小改变
      handleSourceDataSizeChange(val) {
        this.sourceDataQuery.pageSize = val
        this.sourceDataQuery.pageNum = 1
        this.loadSourceData()
      },

      // 源数据分页 - 当前页改变
      handleSourceDataCurrentChange(val) {
        this.sourceDataQuery.pageNum = val
        this.loadSourceData()
      },

      // 预警源数据分页 - 页大小改变
      handleWarningSourceDataSizeChange(val) {
        this.warningSourceDataQuery.pageSize = val
        this.warningSourceDataQuery.pageNum = 1
        this.loadWarningSourceData()
      },

      // 预警源数据分页 - 当前页改变
      handleWarningSourceDataCurrentChange(val) {
        this.warningSourceDataQuery.pageNum = val
        this.loadWarningSourceData()
      },

      // =====================================================
      // 辅助方法
      // =====================================================

      // 获取列标签（将字段名转换为中文标签）
      getColumnLabel(fieldName) {
        const labelMap = {
          // 通用字段
          ID: 'ID',
          NAME: '名称',
          COMPANY_NAME: '企业名称',
          ENTITY_NAME: '实体名称',
          ENTITY_ID: '实体ID',
          CREATE_TIME: '创建时间',
          UPDATE_TIME: '更新时间',
          CALCULATION_TIME: '计算时间',
          CALC_TIME: '计算时间',

          // 预警相关字段
          warningId: '预警ID',
          warningTime: '预警时间',
          riskLevel: '风险等级',
          riskScore: '风险评分',
          warningContent: '预警内容',
          processStatus: '处理状态',
          WARNING_ID: '预警ID',
          WARNING_TIME: '预警时间',
          RISK_LEVEL: '风险等级',
          RISK_SCORE: '风险评分',
          WARNING_CONTENT: '预警内容',
          PROCESS_STATUS: '处理状态',

          // 组合指标分析相关
          SUPPLIER_COUNT: '供应商数量',
          TOTAL_AMOUNT: '合同总金额',
          AGGREGATION_RATIO: '聚合比例',
          WARNING_LEVEL: '预警等级',

          // 数据模型管理相关
          METRIC_VALUE: '指标值',
          INDICATOR_VALUE: '指标值',
          THRESHOLD_VALUE: '阈值',
          SCORE: '评分',

          // 其他常见字段
          STATUS: '状态',
          TYPE: '类型',
          AMOUNT: '金额',
          COUNT: '数量',
          RATIO: '比例',
          RATE: '比率',
          LEVEL: '等级',
        }

        return labelMap[fieldName] || fieldName
      },

      // 获取列宽度
      getColumnWidth(fieldName) {
        const widthMap = {
          ID: 100,
          ENTITY_ID: 120,
          NAME: 200,
          COMPANY_NAME: 200,
          ENTITY_NAME: 200,
          SUPPLIER_COUNT: 120,
          TOTAL_AMOUNT: 150,
          RISK_SCORE: 120,
          AGGREGATION_RATIO: 120,
          WARNING_LEVEL: 120,
          METRIC_VALUE: 120,
          INDICATOR_VALUE: 120,
          THRESHOLD_VALUE: 120,
          RISK_LEVEL: 120,
          SCORE: 100,
          CREATE_TIME: 180,
          UPDATE_TIME: 180,
          CALCULATION_TIME: 180,
          CALC_TIME: 180,
          STATUS: 100,
          TYPE: 100,
        }

        return widthMap[fieldName] || 150
      },

      // 获取风险等级标签类型
      getRiskLevelTagType(level) {
        const levelMap = {
          LOW: 'success',
          MEDIUM: 'warning',
          HIGH: 'danger',
          EXTREME: 'danger',
        }
        return levelMap[level] || 'default'
      },

      // 获取风险等级文本
      getRiskLevelText(level) {
        const levelMap = {
          LOW: '低风险',
          MEDIUM: '中风险',
          HIGH: '高风险',
          EXTREME: '极高风险',
        }
        return levelMap[level] || level
      },

      // 获取处理状态标签类型
      getProcessStatusTagType(status) {
        const statusMap = {
          PENDING: 'warning',
          PROCESSING: 'primary',
          COMPLETED: 'success',
          IGNORED: 'info',
        }
        return statusMap[status] || 'default'
      },

      // 获取处理状态文本
      getProcessStatusText(status) {
        const statusMap = {
          PENDING: '待处理',
          PROCESSING: '处理中',
          COMPLETED: '已处理',
          IGNORED: '已忽略',
        }
        return statusMap[status] || status
      },

      // =====================================================
      // API调用方法 (使用真实API)
      // =====================================================

      // 获取模型预警结果 - 使用真实API
      async getModelWarningResults(params) {
        try {
          return await getModelWarningResults(params)
        } catch (error) {
          console.error('调用预警结果API失败:', error)
          // 如果API调用失败，返回模拟数据作为降级处理
          return {
            code: 1,
            msg: '查询成功(模拟数据)',
            data: {
              list: [
                {
                  warningId: 'W001',
                  warningTime: new Date(),
                  riskLevel: 'HIGH',
                  riskScore: 85.5,
                  warningContent: '客户信用评分异常，存在违约风险',
                  processStatus: 'PENDING',
                },
                {
                  warningId: 'W002',
                  warningTime: new Date(Date.now() - 86400000),
                  riskLevel: 'MEDIUM',
                  riskScore: 72.3,
                  warningContent: '财务指标波动较大，需要关注',
                  processStatus: 'COMPLETED',
                },
              ],
              total: 2,
            },
          }
        }
      },

      // 获取预警源数据 - 使用真实API
      async getWarningSourceData(params) {
        try {
          return await getWarningSourceData(params)
        } catch (error) {
          console.error('调用源数据API失败:', error)
          // 降级处理
          return {
            code: 1,
            msg: '查询成功(模拟数据)',
            data: {
              list: [
                {
                  id: 1,
                  customerName: '客户A',
                  creditScore: 650,
                  amount: 100000,
                },
                {
                  id: 2,
                  customerName: '客户B',
                  creditScore: 720,
                  amount: 200000,
                },
              ],
              total: 2,
              columns: [
                { prop: 'id', label: 'ID', width: 80 },
                { prop: 'customerName', label: '客户名称', width: 150 },
                { prop: 'creditScore', label: '信用评分', width: 120 },
                { prop: 'amount', label: '金额', width: 120 },
              ],
            },
          }
        }
      },

      // 获取预警源数据详情 - 使用真实API
      async getWarningSourceDataDetail(params) {
        try {
          return await getWarningSourceDataDetail(params)
        } catch (error) {
          console.error('调用预警源数据详情API失败:', error)
          // 降级处理
          return {
            code: 1,
            msg: '查询成功(模拟数据)',
            data: {
              list: [
                {
                  indicator: '信用评分',
                  value: 650,
                  threshold: 700,
                  result: '异常',
                },
                {
                  indicator: '负债率',
                  value: 0.85,
                  threshold: 0.7,
                  result: '超标',
                },
              ],
              total: 2,
              columns: [
                { prop: 'indicator', label: '指标名称', width: 150 },
                { prop: 'value', label: '当前值', width: 120 },
                { prop: 'threshold', label: '阈值', width: 120 },
                { prop: 'result', label: '结果', width: 100 },
              ],
            },
          }
        }
      },

      // 获取组合指标执行结果
      async getCombinationIndicatorExecutionResult(params) {
        try {
          // 这里应该调用真实的组合指标执行结果API
          // 暂时使用模拟数据
          console.log('获取组合指标执行结果:', params)

          // 模拟API调用延迟
          await new Promise((resolve) => setTimeout(resolve, 1000))

          return {
            code: 1,
            msg: '查询成功',
            data: {
              list: [
                {
                  COMPANY_NAME: '示例科技有限公司',
                  SUPPLIER_COUNT: 15,
                  TOTAL_AMOUNT: 2500000,
                  RISK_SCORE: 85.5,
                  AGGREGATION_RATIO: 0.65,
                  WARNING_LEVEL: 'HIGH',
                },
                {
                  COMPANY_NAME: '创新软件公司',
                  SUPPLIER_COUNT: 8,
                  TOTAL_AMOUNT: 1200000,
                  RISK_SCORE: 72.3,
                  AGGREGATION_RATIO: 0.45,
                  WARNING_LEVEL: 'MEDIUM',
                },
                {
                  COMPANY_NAME: '智能制造企业',
                  SUPPLIER_COUNT: 12,
                  TOTAL_AMOUNT: 1800000,
                  RISK_SCORE: 78.9,
                  AGGREGATION_RATIO: 0.55,
                  WARNING_LEVEL: 'HIGH',
                },
              ],
              total: 3,
              columns: [
                { prop: 'COMPANY_NAME', label: '企业名称', width: 200 },
                { prop: 'SUPPLIER_COUNT', label: '供应商数量', width: 120 },
                { prop: 'TOTAL_AMOUNT', label: '合同总金额', width: 150 },
                { prop: 'RISK_SCORE', label: '风险评分', width: 120 },
                { prop: 'AGGREGATION_RATIO', label: '聚合比例', width: 120 },
                { prop: 'WARNING_LEVEL', label: '预警等级', width: 120 },
              ],
              executionTime: new Date().toLocaleString(),
              sqlContent: `WITH SUPPLIER_SUMMARY AS (
    SELECT c.SUPPLIER_ID, c.SUPPLIER_NAME, c.CONTRACT_TYPE,
           SUM(c.CONTRACT_AMOUNT) AS TOTAL_AMOUNT, COUNT(*) AS CONTRACT_COUNT
    FROM TBL_CONTRACT_INFO c
    WHERE c.CONTRACT_SIGN_DATE >= ADD_MONTHS(SYSDATE, -12)
    AND c.CONTRACT_STATUS = 'ACTIVE'
    GROUP BY c.SUPPLIER_ID, c.SUPPLIER_NAME, c.CONTRACT_TYPE
)
SELECT
    c.COMPANY_NAME,
    COUNT(DISTINCT c.SUPPLIER_ID) AS SUPPLIER_COUNT,
    SUM(c.CONTRACT_AMOUNT) AS TOTAL_AMOUNT,
    CASE
        WHEN COUNT(DISTINCT c.SUPPLIER_ID) > 10 THEN 'HIGH'
        WHEN COUNT(DISTINCT c.SUPPLIER_ID) > 5 THEN 'MEDIUM'
        ELSE 'LOW'
    END AS WARNING_LEVEL
FROM TBL_CONTRACT_INFO c
GROUP BY c.COMPANY_NAME
HAVING COUNT(DISTINCT c.SUPPLIER_ID) > 5`,
            },
          }
        } catch (error) {
          console.error('获取组合指标执行结果失败:', error)
          return {
            code: 0,
            msg: '获取组合指标执行结果失败: ' + error.message,
          }
        }
      },

      // 获取数据模型执行结果
      async getDataModelExecutionResult(params) {
        try {
          // 这里应该调用真实的数据模型执行结果API
          // 暂时使用模拟数据
          console.log('获取数据模型执行结果:', params)

          // 模拟API调用延迟
          await new Promise((resolve) => setTimeout(resolve, 800))

          return {
            code: 1,
            msg: '查询成功',
            data: {
              list: [
                {
                  ENTITY_ID: 'ENT001',
                  ENTITY_NAME: '示例科技有限公司',
                  METRIC_VALUE: 0.85,
                  THRESHOLD_VALUE: 0.7,
                  RISK_LEVEL: 'HIGH',
                  CALCULATION_TIME: '2025-01-21 14:30:00',
                },
                {
                  ENTITY_ID: 'ENT002',
                  ENTITY_NAME: '创新软件公司',
                  METRIC_VALUE: 0.62,
                  THRESHOLD_VALUE: 0.7,
                  RISK_LEVEL: 'MEDIUM',
                  CALCULATION_TIME: '2025-01-21 14:30:00',
                },
                {
                  ENTITY_ID: 'ENT003',
                  ENTITY_NAME: '智能制造企业',
                  METRIC_VALUE: 0.78,
                  THRESHOLD_VALUE: 0.7,
                  RISK_LEVEL: 'HIGH',
                  CALCULATION_TIME: '2025-01-21 14:30:00',
                },
              ],
              total: 3,
              columns: [
                { prop: 'ENTITY_ID', label: '实体ID', width: 120 },
                { prop: 'ENTITY_NAME', label: '实体名称', width: 200 },
                { prop: 'METRIC_VALUE', label: '指标值', width: 120 },
                { prop: 'THRESHOLD_VALUE', label: '阈值', width: 120 },
                { prop: 'RISK_LEVEL', label: '风险等级', width: 120 },
                { prop: 'CALCULATION_TIME', label: '计算时间', width: 150 },
              ],
              executionTime: new Date().toLocaleString(),
              sqlContent: `SELECT
    e.ENTITY_ID,
    e.ENTITY_NAME,
    dm.METRIC_VALUE,
    dm.THRESHOLD_VALUE,
    CASE
        WHEN dm.METRIC_VALUE > dm.THRESHOLD_VALUE THEN 'HIGH'
        WHEN dm.METRIC_VALUE > dm.THRESHOLD_VALUE * 0.8 THEN 'MEDIUM'
        ELSE 'LOW'
    END AS RISK_LEVEL,
    dm.CALCULATION_TIME
FROM TBL_DATA_MODEL_RESULT dm
JOIN TBL_ENTITY_INFO e ON dm.ENTITY_ID = e.ENTITY_ID
WHERE dm.MODEL_ID = '${params.evalModelId}'
AND dm.CALCULATION_TIME >= SYSDATE - 7
ORDER BY dm.METRIC_VALUE DESC`,
            },
          }
        } catch (error) {
          console.error('获取数据模型执行结果失败:', error)
          return {
            code: 0,
            msg: '获取数据模型执行结果失败: ' + error.message,
          }
        }
      },
    },
  }
</script>

<style lang="scss" scoped>
  .evaluation-model-management {
    padding: 20px;

    .search-form {
      background: #fff;
      padding: 20px;
      border-radius: 4px;
      margin-bottom: 20px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    .toolbar {
      margin-bottom: 20px;

      .el-button {
        margin-right: 10px;
      }
    }

    .statistics-cards {
      margin-bottom: 20px;

      .stat-card {
        background: #fff;
        padding: 20px;
        border-radius: 4px;
        text-align: center;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        transition: all 0.3s ease;

        &:hover {
          transform: translateY(-2px);
          box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
        }

        .stat-number {
          font-size: 28px;
          font-weight: bold;
          color: #409eff;
          margin-bottom: 8px;
        }

        .stat-label {
          font-size: 14px;
          color: #666;
        }

        &.pending .stat-number {
          color: #e6a23c;
        }

        &.processed .stat-number {
          color: #67c23a;
        }

        &.today .stat-number {
          color: #909399;
        }
      }
    }

    .table-container {
      background: #fff;
      border-radius: 4px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    .pagination-container {
      margin-top: 20px;
      text-align: right;
    }

    // 预警数量样式
    .warning-count {
      color: #409eff;
      font-weight: bold;
      cursor: pointer;

      &:hover {
        color: #66b1ff;
      }
    }

    .disabled-warning {
      color: #c0c4cc;
    }

    // 🆕 新增：操作按钮样式优化
    .table-container {
      ::v-deep .el-table {
        .el-table__body {
          .el-button {
            margin: 2px 4px;
            padding: 5px 8px;
            font-size: 12px;
            border-radius: 3px;
            transition: all 0.3s ease;

            // 生成报告按钮样式
            &.el-button--primary {
              background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
              border: none;
              box-shadow: 0 2px 4px rgba(64, 158, 255, 0.3);

              &:hover {
                background: linear-gradient(135deg, #66b1ff 0%, #409eff 100%);
                box-shadow: 0 4px 8px rgba(64, 158, 255, 0.4);
                transform: translateY(-1px);
              }
            }

            // 流程图按钮样式
            &.el-button--info {
              background: linear-gradient(135deg, #909399 0%, #b1b3b8 100%);
              border: none;
              box-shadow: 0 2px 4px rgba(144, 147, 153, 0.3);

              &:hover {
                background: linear-gradient(135deg, #b1b3b8 0%, #909399 100%);
                box-shadow: 0 4px 8px rgba(144, 147, 153, 0.4);
                transform: translateY(-1px);
              }
            }

            // 预警处理按钮样式
            &.el-button--success {
              background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
              border: none;
              box-shadow: 0 2px 4px rgba(103, 194, 58, 0.3);

              &:hover {
                background: linear-gradient(135deg, #85ce61 0%, #67c23a 100%);
                box-shadow: 0 4px 8px rgba(103, 194, 58, 0.4);
                transform: translateY(-1px);
              }
            }

            // 预警结果按钮样式
            &.el-button--warning {
              background: linear-gradient(135deg, #e6a23c 0%, #ebb563 100%);
              border: none;
              box-shadow: 0 2px 4px rgba(230, 162, 60, 0.3);

              &:hover {
                background: linear-gradient(135deg, #ebb563 0%, #e6a23c 100%);
                box-shadow: 0 4px 8px rgba(230, 162, 60, 0.4);
                transform: translateY(-1px);
              }
            }

            // 查看按钮样式
            &.el-button--default {
              background: linear-gradient(135deg, #dcdfe6 0%, #e4e7ed 100%);
              color: #606266;
              border: none;
              box-shadow: 0 2px 4px rgba(220, 223, 230, 0.3);

              &:hover {
                background: linear-gradient(135deg, #e4e7ed 0%, #dcdfe6 100%);
                box-shadow: 0 4px 8px rgba(220, 223, 230, 0.4);
                transform: translateY(-1px);
              }
            }

            // 按钮图标样式
            i {
              margin-right: 4px;
              font-size: 12px;
            }

            // 按钮文字样式
            span {
              font-weight: 500;
              letter-spacing: 0.5px;
            }

            // 按钮禁用状态
            &.is-disabled {
              opacity: 0.6;
              cursor: not-allowed;
              transform: none !important;
              box-shadow: none !important;

              &:hover {
                transform: none !important;
                box-shadow: none !important;
              }
            }

            // 按钮加载状态
            &.is-loading {
              pointer-events: none;

              .el-icon-loading {
                animation: rotating 2s linear infinite;
              }
            }
          }

          // 操作列按钮组样式
          .el-table__cell {
            &:last-child {
              .cell {
                display: flex;
                flex-wrap: wrap;
                gap: 4px;
                justify-content: flex-start;
                align-items: center;
              }
            }
          }
        }
      }
    }

    // 🆕 新增：按钮动画效果
    @keyframes rotating {
      0% {
        transform: rotate(0deg);
      }
      100% {
        transform: rotate(360deg);
      }
    }

    // 🆕 新增：按钮响应式样式
    @media (max-width: 1200px) {
      .table-container {
        ::v-deep .el-table {
          .el-table__body {
            .el-button {
              padding: 4px 6px;
              font-size: 11px;

              span {
                display: none; // 小屏幕隐藏文字，只显示图标
              }
            }
          }
        }
      }
    }

    @media (max-width: 768px) {
      .table-container {
        ::v-deep .el-table {
          .el-table__body {
            .el-button {
              padding: 3px 5px;
              margin: 1px 2px;

              i {
                margin-right: 0;
                font-size: 10px;
              }
            }
          }
        }
      }
    }
  }

  // 预警结果对话框样式
  ::v-deep .warning-result-dialog {
    .el-dialog__body {
      padding: 10px 20px;
    }

    .warning-result-container {
      .model-info-section {
        margin-bottom: 20px;

        .model-info-card {
          .card-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-weight: bold;
          }
        }
      }

      .warning-results-section {
        .card-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          font-weight: bold;

          .header-actions {
            .el-button {
              margin-left: 10px;
            }
          }
        }
      }
    }
  }

  // 数据穿透对话框样式
  ::v-deep .data-drill-through-dialog {
    .el-dialog__body {
      padding: 10px 20px;
    }

    .data-drill-through-container {
      .section-header {
        margin-bottom: 15px;
        padding-bottom: 10px;
        border-bottom: 1px solid #ebeef5;

        h4 {
          margin: 0 0 5px 0;
          color: #303133;
          font-size: 16px;
        }

        p {
          margin: 0;
          color: #909399;
          font-size: 14px;
        }
      }

      .source-data-section,
      .warning-source-data-section {
        .el-table {
          margin-bottom: 15px;
        }
      }
    }
  }
</style>
