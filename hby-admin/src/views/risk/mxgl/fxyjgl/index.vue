<template>
  <div class="risk-warning-management">
    <!-- 🔥 临时隐藏：模型过滤提示 -->
    <!--
    <div v-if="queryForm.evalModelId" class="model-filter-tip">
      <el-alert
        :title="`当前显示模型${$route.query.modelName || queryForm.evalModelId}的预警数据`"
        type="info"
        :closable="false"
        show-icon
        style="margin-bottom: 20px"
      >
        <template slot="default">
          <span>模型ID: {{ queryForm.evalModelId }}</span>
          <el-button
            type="text"
            size="small"
            @click="clearModelFilter"
            style="margin-left: 10px"
          >
            显示所有模型
          </el-button>
        </template>
      </el-alert>
    </div>
    -->

    <!-- 查询条件 -->
    <div class="search-form">
      <el-form
        :model="queryForm"
        ref="queryForm"
        :inline="true"
        label-width="100px"
      >
        <el-form-item label="预警编码">
          <el-input
            v-model="queryForm.warningCode"
            placeholder="请输入预警编码"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="预警类型">
          <el-select
            v-model="queryForm.warningType"
            placeholder="请选择预警类型"
            clearable
            style="width: 180px"
          >
            <!-- 风险类型 -->
            <el-option label="财务风险" value="FINANCIAL_RISK" />
            <el-option label="采购风险" value="PROCUREMENT_RISK" />
            <el-option label="信用风险" value="CREDIT_RISK" />
            <el-option label="合规风险" value="COMPLIANCE_RISK" />
            <!-- 执行类型 -->
            <el-option label="组合执行" value="COMBINATION_EXECUTION" />
            <el-option label="模型执行" value="DATA_MODEL_EXECUTION" />
            <el-option label="自动生成" value="AUTO_GENERATED" />
            <el-option label="手动创建" value="MANUAL_CREATED" />
            <!-- 预警机制类型 -->
            <el-option label="阈值预警" value="THRESHOLD" />
            <el-option label="趋势预警" value="TREND" />
            <el-option label="异常预警" value="ANOMALY" />
          </el-select>
        </el-form-item>
        <el-form-item label="预警级别">
          <el-select
            v-model="queryForm.warningLevel"
            placeholder="请选择预警级别"
            clearable
            style="width: 120px"
          >
            <el-option label="高风险" value="HIGH" />
            <el-option label="中风险" value="MEDIUM" />
            <el-option label="低风险" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item label="预警状态">
          <el-select
            v-model="queryForm.warningStatus"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="待处理" value="PENDING" />
            <el-option label="处理中" value="PROCESSING" />
            <el-option label="已处理" value="PROCESSED" />
            <el-option label="已忽略" value="IGNORED" />
          </el-select>
        </el-form-item>
        <el-form-item label="企业名称">
          <el-input
            v-model="queryForm.companyName"
            placeholder="请输入企业名称"
            clearable
            style="width: 180px"
          />
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
        type="success"
        @click="handleBatchProcess"
        icon="el-icon-check"
        :disabled="!multipleSelection.length"
      >
        批量处理
      </el-button>
      <el-button
        type="warning"
        @click="handleBatchIgnore"
        icon="el-icon-close"
        :disabled="!multipleSelection.length"
      >
        批量忽略
      </el-button>
      <el-button
        type="info"
        @click="handleRefreshStatistics"
        icon="el-icon-refresh"
      >
        刷新统计
      </el-button>
      <el-button
        type="primary"
        @click="handleGenerateReport"
        icon="el-icon-document"
      >
        生成报告
      </el-button>
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
          prop="warningCode"
          label="预警编码"
          width="140"
          show-overflow-tooltip
        />
        <el-table-column prop="warningType" label="预警类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getWarningTypeTagType(scope.row.warningType)">
              {{ getWarningTypeText(scope.row.warningType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="warningLevel" label="预警级别" width="100">
          <template slot-scope="scope">
            <el-tag :type="getWarningLevelTagType(scope.row.warningLevel)">
              {{ getWarningLevelText(scope.row.warningLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="warningStatus" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getWarningStatusTagType(scope.row.warningStatus)">
              {{ getWarningStatusText(scope.row.warningStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="companyName"
          label="企业名称"
          min-width="150"
          show-overflow-tooltip
        />
        <el-table-column prop="warningValue" label="预警值" width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.warningValue">
              {{ scope.row.warningValue }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="thresholdValue" label="阈值" width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.thresholdValue">
              {{ scope.row.thresholdValue }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="warningDescription"
          label="预警描述"
          min-width="200"
          show-overflow-tooltip
        />
        <el-table-column prop="warningTime" label="预警时间" width="150">
          <template slot-scope="scope">
            {{ formatDate(scope.row.warningTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="processUser" label="处理人" width="100" />
        <el-table-column prop="processTime" label="处理时间" width="150">
          <template slot-scope="scope">
            <span v-if="scope.row.processTime">
              {{ formatDate(scope.row.processTime) }}
            </span>
            <span v-else>-</span>
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
            <el-button
              size="mini"
              type="success"
              @click="handleDrillDown(scope.row)"
              icon="el-icon-data-analysis"
              title="数据穿透分析"
            >
              穿透
            </el-button>
            <el-dropdown @command="handleCommand" trigger="click">
              <el-button size="mini" type="primary">
                处理
                <i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  :command="{ action: 'confirm', row: scope.row }"
                  icon="el-icon-check"
                >
                  确认处理
                </el-dropdown-item>
                <el-dropdown-item
                  :command="{ action: 'ignore', row: scope.row }"
                  icon="el-icon-close"
                >
                  忽略预警
                </el-dropdown-item>
                <el-dropdown-item
                  :command="{ action: 'false_positive', row: scope.row }"
                  icon="el-icon-warning"
                  divided
                >
                  标记误报
                </el-dropdown-item>
                <el-dropdown-item
                  :command="{ action: 'risk_handle', row: scope.row }"
                  icon="el-icon-s-operation"
                  divided
                >
                  风险处理
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

    <!-- 预警详情对话框 -->
    <el-dialog
      title="预警详情"
      :visible.sync="detailDialogVisible"
      width="1000px"
      :close-on-click-modal="false"
    >
      <risk-warning-detail
        ref="warningDetail"
        :warning-data="currentWarning"
        @close="detailDialogVisible = false"
      />
    </el-dialog>

    <!-- 批量处理对话框 -->
    <el-dialog
      title="批量处理预警"
      :visible.sync="batchProcessDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <batch-process-form
        ref="batchProcessForm"
        :selected-warnings="multipleSelection"
        @submit="handleBatchProcessSubmit"
        @cancel="batchProcessDialogVisible = false"
      />
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

    <!-- 数据穿透分析对话框 -->
    <warning-drill-down-dialog
      :visible.sync="drillDownDialogVisible"
      :warning-id="currentDrillDownWarningId"
    />

    <!-- 风险类型选择弹窗 -->
    <risk-type-select-dialog ref="riskTypeSelectDialog" @confirm="onRiskTypeSelected" />

    <!-- 风险处理（新增风险）弹窗 -->
    <risk-edit v-if="riskEditVisible" ref="riskEditRef" :treeId="riskEditTreeId" @fetch-data="onRiskCreateSuccess" />
  </div>
</template>

<script>
  import {
    getRiskWarningList,
    getRiskWarningDetail,
    processRiskWarning,
    batchProcessRiskWarning,
    getWarningStatistics,
    generateWarningReport,
    getWarningConfig,
    updateWarningConfig,
    getTodayWarningCount, // 🔧 新增：今日预警数量
  } from '@/api/mxgl'
  import RiskWarningDetail from './components/RiskWarningDetail'
  import BatchProcessForm from './components/BatchProcessForm'
  import WarningConfigForm from './components/WarningConfigForm'
  import ReportGenerateForm from './components/ReportGenerateForm'
  import WarningDrillDownDialog from '@/components/risk/WarningDrillDownDialog'
  import RiskEdit from '@/views/risk/identify/creation/components/RiskEdit.vue'
  import RiskTypeSelectDialog from './components/RiskTypeSelectDialog'
  import request from '@/utils/request'
  import { customUpload } from '@/utils/Uploader'
  import store from '@/store'
  const { baseURL } = require('@/config')

  export default {
    name: 'RiskWarningManagement',
    components: {
      RiskWarningDetail,
      BatchProcessForm,
      WarningConfigForm,
      ReportGenerateForm,
      WarningDrillDownDialog,
      RiskEdit,
      RiskTypeSelectDialog,
    },
    data() {
      return {
        // 查询表单
        queryForm: {
          pageNum: 1,
          pageSize: 20,
          warningCode: '',
          warningType: '',
          warningLevel: '',
          warningStatus: '',
          companyName: '',
          evalModelId: '', // 🔥 新增：评估模型ID过滤条件
        },
        // 表格数据
        tableData: [],
        total: 0,
        loading: false,
        multipleSelection: [],
        // 统计数据
        statistics: {},
        // 对话框
        detailDialogVisible: false,
        batchProcessDialogVisible: false,
        configDialogVisible: false,
        reportDialogVisible: false,
        drillDownDialogVisible: false,
        currentWarning: {},
        currentDrillDownWarningId: '',
        // 风险处理
        riskEditTreeId: '',
        riskEditVisible: false,
        pendingWarningIds: [], // 风险创建成功后需要更新状态的预警ID
        // 风险类型选择 - 暂存上下文
        riskProcessMode: '', // 'single' | 'batch'
        riskProcessRow: null, // 单条处理时暂存的行数据
      }
    },
    created() {
      // 🔥 新增：检查从评估模型页面传递的参数
      this.initializeFromRoute()
      this.loadData()
      this.loadStatistics()
    },
    methods: {
      // 🔥 新增：从路由参数初始化页面
      initializeFromRoute() {
        const { modelId, modelName, modelType } = this.$route.query

        console.log('🔍 路由参数检查:', {
          '$route.query': this.$route.query,
          modelId,
          modelName,
          modelType,
        })

        if (modelId) {
          // 设置模型ID作为过滤条件
          this.queryForm.evalModelId = modelId

          // 显示当前过滤的模型信息
          this.$message.success(
            `已过滤显示模型"${modelName || modelId}"的预警数据`
          )

          console.log('✅ 风险预警管理页面接收到参数:', {
            modelId,
            modelName,
            modelType,
            queryForm: this.queryForm,
          })
        } else {
          console.log('❌ 没有接收到modelId参数')
        }
      },

      // 加载数据
      async loadData() {
        this.loading = true

        // 🔍 调试：打印查询参数
        console.log('🚀 发送查询请求，参数:', this.queryForm)

        try {
          const response = await getRiskWarningList(this.queryForm)
          // 🔍 调试：打印响应结果
          console.log('📥 接收到响应:', response)
          console.log('📥 response.code:', response.code)
          console.log('📥 response.data:', response.data)

          // ✅ 修复：从 response.data 中获取分页数据
          if (response.code === 1 && response.data) {
            this.tableData = response.data.list || []
            this.total = response.data.total || 0
            console.log('✅ 风险预警列表加载成功:', {
              列表长度: this.tableData.length,
              总记录数: this.total
            })
          } else {
            console.warn('⚠️ 风险预警响应格式异常:', response)
            this.tableData = []
            this.total = 0
          }
        } catch (error) {
          console.error('❌ 加载风险预警列表异常:', error)
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
          console.log('预警统计API响应:', response)

          // 🔧 修复：兼容拦截器解包后的数据结构
          let rawData = {}
          if (response.code === 1 && response.data) {
            rawData = response.data || {}
          } else if (
            response.totalCount !== undefined ||
            response.statusStatistics !== undefined
          ) {
            // 拦截器已解包
            rawData = response || {}
          }

          console.log('原始预警统计数据:', rawData)

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

          console.log('处理后的预警统计数据:', this.statistics)

          // 🔧 新增：单独获取今日新增数量
          this.loadTodayCount()
        } catch (error) {
          console.error('加载统计数据失败', error)
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

      // 🔥 新增：清除模型过滤
      clearModelFilter() {
        this.queryForm.evalModelId = ''
        this.$message.success('已清除模型过滤，显示所有模型的预警数据')
        this.handleSearch()
      },

      // 查询
      handleSearch() {
        this.queryForm.pageNum = 1
        this.loadData()
      },
      // 重置
      handleReset() {
        // 🔥 修复：重置时保留模型ID过滤（如果是从模型页面跳转过来的）
        const originalModelId = this.queryForm.evalModelId
        this.$refs.queryForm.resetFields()

        // 如果原来有模型ID过滤，重置后恢复
        if (originalModelId && this.$route.query.modelId) {
          this.queryForm.evalModelId = originalModelId
        }

        this.queryForm.pageNum = 1
        this.loadData()
      },
      // 查看详情
      async handleView(row) {
        try {
          const response = await getRiskWarningDetail(row.warningId)
          if (response.code === 1) {
            this.currentWarning = response.data
            this.detailDialogVisible = true
          } else {
            this.$message.error(response.msg || '获取预警详情失败')
          }
        } catch (error) {
          this.$message.error('获取预警详情失败')
        }
      },
      // 处理操作
      handleCommand(command) {
        const { action, row } = command
        switch (action) {
          case 'confirm':
            this.handleProcessWarning(row, 'CONFIRM', '确认处理')
            break
          case 'ignore':
            this.handleProcessWarning(row, 'IGNORE', '忽略预警')
            break
          case 'false_positive':
            this.handleProcessWarning(row, 'FALSE_POSITIVE', '标记误报')
            break
          case 'risk_handle':
            this.handleRiskProcess(row)
            break
        }
      },
      // 风险处理 - 先让用户选择风险类型，再生成附件并打开新增弹窗
      handleRiskProcess(row) {
        console.log('风险处理:', row)
        this.riskProcessMode = 'single'
        this.riskProcessRow = row
        this.$refs.riskTypeSelectDialog.open()
      },
      // 风险创建成功后的回调 - 更新预警状态为已处理
      onRiskCreateSuccess() {
        if (!this.pendingWarningIds || this.pendingWarningIds.length === 0) {
          return
        }
        if (this.pendingWarningIds.length === 1) {
          // 单条处理
          processRiskWarning({
            warningId: this.pendingWarningIds[0],
            processAction: 'CONFIRM',
            processNote: '风险处理',
          }).then(() => {
            this.loadData()
            this.loadStatistics()
            this.$message.success('预警已标记为已处理')
          })
        } else {
          // 批量处理
          request({
            url: '/riskcontrol/model/warning/batchProcess',
            method: 'post',
            headers: { 'Content-Type': 'application/json;charset=UTF-8' },
            data: JSON.stringify({
              warningIds: this.pendingWarningIds,
              status: 'PROCESSED',
              handleRemark: '批量风险处理',
            }),
          }).then(() => {
            this.loadData()
            this.loadStatistics()
            this.$message.success('预警已批量标记为已处理')
          })
        }
        this.pendingWarningIds = []
      },
      // 处理预警
      async handleProcessWarning(row, action, actionText) {
        this.$confirm(`确认${actionText}吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          try {
            const response = await processRiskWarning({
              warningId: row.warningId,
              processAction: action,
              processNote: actionText,
            })
            if (response.code === 1) {
              this.$message.success(`${actionText}成功`)
              this.loadData()
              this.loadStatistics()
            } else {
              this.$message.error(response.msg || `${actionText}失败`)
            }
          } catch (error) {
            this.$message.error(`${actionText}失败`)
          }
        })
      },
      // 数据穿透分析
      handleDrillDown(row) {
        console.log('打开数据穿透分析:', row)
        this.currentDrillDownWarningId = row.warningId
        this.drillDownDialogVisible = true
      },
      // 批量处理 - 先让用户选择风险类型，再生成附件并打开风险新增弹窗
      handleBatchProcess() {
        if (!this.multipleSelection.length) {
          this.$message.warning('请选择要处理的预警')
          return
        }
        this.riskProcessMode = 'batch'
        this.riskProcessRow = null
        this.$refs.riskTypeSelectDialog.open()
      },
      /**
       * 用户在风险类型选择弹窗中确认后的回调
       * @param {Object} selectedTypeData - 包含 value, riskcatname, riskcatnametwo, fatherriskcatid, fatherriskcatname
       */
      async onRiskTypeSelected(selectedTypeData) {
        if (this.riskProcessMode === 'single') {
          await this.doRiskProcess(this.riskProcessRow, selectedTypeData)
        } else if (this.riskProcessMode === 'batch') {
          await this.doBatchRiskProcess(selectedTypeData)
        }
      },
      // 单条风险处理 - 生成Excel附件并打开新增弹窗（使用用户选择的风险类型）
      async doRiskProcess(row, typeData) {
        const msgInstance = this.$message({
          message: '正在生成附件...',
          type: 'info',
          duration: 0,
          iconClass: 'el-icon-loading',
        })

        try {
          // 1. 动态导入 XLSX，将预警行数据生成 Excel File 对象
          const XLSX = await import('xlsx')
          const header = [
            '预警编码', '预警类型', '预警级别', '状态', '企业名称',
            '预警值', '阈值', '预警描述', '预警时间', '处理人', '处理时间',
          ]
          const rowData = [
            row.warningCode || '',
            this.getWarningTypeText(row.warningType),
            this.getWarningLevelText(row.warningLevel),
            this.getWarningStatusText(row.warningStatus),
            row.companyName || '',
            row.warningValue || '',
            row.thresholdValue || '',
            row.warningDescription || '',
            row.warningTime ? this.formatDate(row.warningTime) : '',
            row.processUser || '',
            row.processTime ? this.formatDate(row.processTime) : '',
          ]
          const wsData = [header, rowData]
          const ws = XLSX.utils.aoa_to_sheet(wsData)
          ws['!cols'] = header.map(() => ({ wch: 18 }))
          const wb = XLSX.utils.book_new()
          XLSX.utils.book_append_sheet(wb, ws, '预警数据')
          const wbout = XLSX.write(wb, { bookType: 'xlsx', type: 'array' })
          const blob = new Blob([wbout], {
            type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
          })
          const fileName = `预警数据_${row.warningCode || 'unknown'}_${Date.now()}.xlsx`
          const file = new File([blob], fileName, { type: blob.type })

          // 2. 使用 customUpload 加密上传到外网文件服务器（与 creation 页面一致）
          const uploadedFiles = await this.uploadFileEncrypted(file)

          // 3. 打开 RiskEdit 新增弹窗，使用用户选择的风险类型
          this.riskEditVisible = true
          await this.$nextTick()

          const resultData = {
            value: typeData.value,
            riskcatname: typeData.riskcatname || '',
            riskcatnametwo: typeData.riskcatnametwo || '',
            fatherriskcatid: typeData.fatherriskcatid || '',
            fatherriskcatname: typeData.fatherriskcatname || '',
            riskname: row.warningCode || '',
            riskdes: row.warningDescription || '',
          }
          await this.$refs['riskEditRef'].showEdit(resultData, 'add')

          // 5. 将上传成功的附件挂到 RiskEdit 的附件列表
          this.$nextTick(() => {
            const editRef = this.$refs['riskEditRef']
            editRef.tableDataFile = [...editRef.tableDataFile, ...uploadedFiles]
            uploadedFiles.forEach((item) => {
              if (item && item.attid) {
                editRef.fileIdList.push(item.attid)
              }
            })
          })

          this.$message.success('附件已生成')

          // 6. 记录待更新状态的预警ID
          this.pendingWarningIds = [row.warningId]
        } catch (error) {
          console.error('风险处理失败:', error)
          this.$message.error('风险处理失败: ' + (error.message || '未知错误'))
        } finally {
          msgInstance.close()
        }
      },
      // 批量风险处理 - 生成Excel附件并打开新增弹窗（使用用户选择的风险类型）
      async doBatchRiskProcess(typeData) {
        const msgInstance = this.$message({
          message: '正在生成附件...',
          type: 'info',
          duration: 0,
          iconClass: 'el-icon-loading',
        })

        try {
          // 1. 生成包含所有选中行数据的 Excel
          const XLSX = await import('xlsx')
          const header = [
            '预警编码', '预警类型', '预警级别', '状态', '企业名称',
            '预警值', '阈值', '预警描述', '预警时间', '处理人', '处理时间',
          ]
          const rows = this.multipleSelection.map((row) => [
            row.warningCode || '',
            this.getWarningTypeText(row.warningType),
            this.getWarningLevelText(row.warningLevel),
            this.getWarningStatusText(row.warningStatus),
            row.companyName || '',
            row.warningValue || '',
            row.thresholdValue || '',
            row.warningDescription || '',
            row.warningTime ? this.formatDate(row.warningTime) : '',
            row.processUser || '',
            row.processTime ? this.formatDate(row.processTime) : '',
          ])
          const wsData = [header, ...rows]
          const ws = XLSX.utils.aoa_to_sheet(wsData)
          ws['!cols'] = header.map(() => ({ wch: 18 }))
          const wb = XLSX.utils.book_new()
          XLSX.utils.book_append_sheet(wb, ws, '预警数据')
          const wbout = XLSX.write(wb, { bookType: 'xlsx', type: 'array' })
          const blob = new Blob([wbout], {
            type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
          })
          const fileName = `批量预警数据_${this.multipleSelection.length}条_${Date.now()}.xlsx`
          const file = new File([blob], fileName, { type: blob.type })

          // 2. 使用 customUpload 加密上传到外网文件服务器（与 creation 页面一致）
          const uploadedFiles = await this.uploadFileEncrypted(file)

          // 3. 打开 RiskEdit 新增弹窗，使用用户选择的风险类型
          this.riskEditVisible = true
          await this.$nextTick()

          const resultData = {
            value: typeData.value,
            riskcatname: typeData.riskcatname || '',
            riskcatnametwo: typeData.riskcatnametwo || '',
            fatherriskcatid: typeData.fatherriskcatid || '',
            fatherriskcatname: typeData.fatherriskcatname || '',
            riskname: this.multipleSelection[0].warningCode || '',
            riskdes: `批量处理${this.multipleSelection.length}条预警`,
          }
          await this.$refs['riskEditRef'].showEdit(resultData, 'add')

          // 5. 挂载附件
          this.$nextTick(() => {
            const editRef = this.$refs['riskEditRef']
            editRef.tableDataFile = [...editRef.tableDataFile, ...uploadedFiles]
            uploadedFiles.forEach((item) => {
              if (item && item.attid) {
                editRef.fileIdList.push(item.attid)
              }
            })
          })

          this.$message.success(`附件已生成（${this.multipleSelection.length}条数据）`)

          // 6. 记录待更新状态的预警ID
          this.pendingWarningIds = this.multipleSelection.map(
            (item) => item.warningId
          )
        } catch (error) {
          console.error('批量风险处理失败:', error)
          this.$message.error('批量处理失败: ' + (error.message || '未知错误'))
        } finally {
          msgInstance.close()
        }
      },
      /**
       * 加密上传文件到外网文件服务器（与 creation 页面 RiskEdit 中的上传逻辑一致）
       * @param {File} file - 要上传的文件
       * @returns {Promise<Array>} 上传成功后的文件信息数组
       */
      uploadFileEncrypted(file) {
        return new Promise((resolve, reject) => {
          const uploadApi = 'https://www.wenxin.example.com/api/file/file/upload'
          const headers = { token: store.getters['user/token'] }

          customUpload({
            baseApi: baseURL,
            api: uploadApi,
            key: window.key,
            iv: window.iv,
            headers: headers,
            fileList: [file],
            formData: {},
            onProgress: () => {},
            onSuccess: (response) => {
              const code = Number(response.code)
              const data = response.data || response.result
              const msg = response.msg || response.message || ''

              if (code !== 200 || !data) {
                reject(new Error(msg || '附件上传失败'))
                return
              }
              const uploadedFiles = Array.isArray(data) ? data : [data]
              resolve(uploadedFiles)
            },
            onError: (error) => {
              reject(error)
            },
          })
        })
      },
      // 批量忽略
      handleBatchIgnore() {
        if (!this.multipleSelection.length) {
          this.$message.warning('请选择要忽略的预警')
          return
        }
        this.$confirm(
          `确认忽略选中的 ${this.multipleSelection.length} 个预警吗？`,
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          }
        ).then(async () => {
          try {
            const warningIds = this.multipleSelection.map(
              (item) => item.warningId
            )
            const response = await batchProcessRiskWarning({
              warningIds,
              processAction: 'IGNORE',
              processNote: '批量忽略',
            })
            if (response.code === 1) {
              this.$message.success('批量忽略成功')
              this.loadData()
              this.loadStatistics()
            } else {
              this.$message.error(response.msg || '批量忽略失败')
            }
          } catch (error) {
            this.$message.error('批量忽略失败')
          }
        })
      },
      // 批量处理提交
      async handleBatchProcessSubmit(formData) {
        try {
          const warningIds = this.multipleSelection.map(
            (item) => item.warningId
          )
          const response = await batchProcessRiskWarning({
            warningIds,
            ...formData,
          })
          if (response.code === 1) {
            this.$message.success('批量处理成功')
            this.batchProcessDialogVisible = false
            this.loadData()
            this.loadStatistics()
          } else {
            this.$message.error(response.msg || '批量处理失败')
          }
        } catch (error) {
          this.$message.error('批量处理失败')
        }
      },
      // 刷新统计
      handleRefreshStatistics() {
        this.loadStatistics()
        this.$message.success('统计数据已刷新')
      },
      // 生成报告
      handleGenerateReport() {
        this.reportDialogVisible = true
      },
      // 报告生成提交
      async handleReportSubmit(formData) {
        try {
          const response = await generateWarningReport(formData)
          if (response.code === 1) {
            this.$message.success('报告生成成功')
            this.reportDialogVisible = false
            // 这里可以添加下载报告的逻辑
          } else {
            this.$message.error(response.msg || '报告生成失败')
          }
        } catch (error) {
          this.$message.error('报告生成失败')
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
      // 获取预警类型标签类型
      getWarningTypeTagType(type) {
        const typeMap = {
          // 风险类型
          FINANCIAL_RISK: 'success',
          PROCUREMENT_RISK: 'primary',
          CREDIT_RISK: 'warning',
          COMPLIANCE_RISK: 'info',
          // 执行类型
          COMBINATION_EXECUTION: 'primary',
          DATA_MODEL_EXECUTION: 'success',
          AUTO_GENERATED: 'warning',
          MANUAL_CREATED: 'info',
          // 预警机制类型
          THRESHOLD: 'danger',
          TREND: 'warning',
          ANOMALY: 'danger',
        }
        return typeMap[type] || 'default'
      },
      // 获取预警类型文本
      getWarningTypeText(type) {
        const typeMap = {
          // 风险类型
          FINANCIAL_RISK: '财务风险',
          PROCUREMENT_RISK: '采购风险',
          CREDIT_RISK: '信用风险',
          COMPLIANCE_RISK: '合规风险',
          // 执行类型
          COMBINATION_EXECUTION: '组合执行',
          DATA_MODEL_EXECUTION: '模型执行',
          AUTO_GENERATED: '自动生成',
          MANUAL_CREATED: '手动创建',
          // 预警机制类型
          THRESHOLD: '阈值预警',
          TREND: '趋势预警',
          ANOMALY: '异常预警',
        }
        return typeMap[type] || type
      },
      // 获取预警级别标签类型
      getWarningLevelTagType(level) {
        const levelMap = {
          HIGH: 'danger',
          MEDIUM: 'warning',
          LOW: 'success',
        }
        return levelMap[level] || 'default'
      },
      // 获取预警级别文本
      getWarningLevelText(level) {
        const levelMap = {
          HIGH: '高风险',
          MEDIUM: '中风险',
          LOW: '低风险',
        }
        return levelMap[level] || level
      },
      // 获取预警状态标签类型
      getWarningStatusTagType(status) {
        const statusMap = {
          PENDING: 'warning',
          PROCESSING: 'primary',
          PROCESSED: 'success',
          IGNORED: 'info',
        }
        return statusMap[status] || 'default'
      },
      // 获取预警状态文本
      getWarningStatusText(status) {
        const statusMap = {
          PENDING: '待处理',
          PROCESSING: '处理中',
          PROCESSED: '已处理',
          IGNORED: '已忽略',
        }
        return statusMap[status] || status
      },
      // 格式化日期
      formatDate(date) {
        if (!date) return '-'
        return new Date(date).toLocaleString('zh-CN')
      },
    },
  }
</script>

<style lang="scss" scoped>
  .risk-warning-management {
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
  }
</style>
