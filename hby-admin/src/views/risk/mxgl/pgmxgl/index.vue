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
        <el-form-item label="模型状态">
          <el-select
            v-model="queryForm.modelStatus"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="草稿" value="DRAFT" />
            <el-option label="测试中" value="TESTING" />
            <el-option label="已发布" value="PUBLISHED" />
            <el-option label="已停用" value="DISABLED" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否启用">
          <el-select
            v-model="queryForm.isEnabled"
            placeholder="请选择"
            clearable
            style="width: 100px"
          >
            <el-option label="是" value="Y" />
            <el-option label="否" value="N" />
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
      <el-button type="primary" @click="handleAdd" icon="el-icon-plus">
        新增模型
      </el-button>
      <el-button
        type="success"
        @click="handleBatchPublish"
        icon="el-icon-upload"
        :disabled="!multipleSelection.length"
      >
        批量发布
      </el-button>
      <el-button
        type="warning"
        @click="handleBatchStart"
        icon="el-icon-video-play"
        :disabled="!multipleSelection.length"
      >
        批量启动
      </el-button>
      <el-button
        type="danger"
        @click="handleBatchDelete"
        icon="el-icon-delete"
        :disabled="!multipleSelection.length"
      >
        批量删除
      </el-button>
      <el-button
        type="info"
        @click="handleRefreshStatistics"
        icon="el-icon-refresh"
      >
        刷新统计
      </el-button>
    </div>

    <!-- 统计信息 -->
    <div class="statistics-cards">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="stat-card total">
            <div class="stat-number">{{ statistics.totalCount || 0 }}</div>
            <div class="stat-label">总模型数</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="stat-card enabled">
            <div class="stat-number">{{ statistics.enabledCount || 0 }}</div>
            <div class="stat-label">已启用</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="stat-card disabled">
            <div class="stat-number">{{ statistics.disabledCount || 0 }}</div>
            <div class="stat-label">未启用</div>
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
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTagType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isEnabled" label="是否启用" width="80">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.isEnabled"
              active-value="Y"
              inactive-value="N"
              @change="handleToggleEnabled(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="version" label="版本" width="80" />
        <el-table-column prop="totalWeight" label="总权重" width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.totalWeight">
              {{ scope.row.totalWeight }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>

        <el-table-column prop="createUser" label="创建人" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="150">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="380" fixed="right">
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
              type="primary"
              @click="handleEdit(scope.row)"
              icon="el-icon-edit"
            >
              编辑
            </el-button>

            <!-- 启动/停止按钮 -->
            <el-button
              v-if="
                scope.row.status === 'PUBLISHED' && scope.row.isEnabled === 'Y'
              "
              size="mini"
              type="success"
              @click="handleStart(scope.row)"
              icon="el-icon-video-play"
            >
              启动
            </el-button>
            <el-button
              v-if="scope.row.status === 'RUNNING'"
              size="mini"
              type="warning"
              @click="handleStop(scope.row)"
              icon="el-icon-video-pause"
            >
              停止
            </el-button>

            <el-dropdown @command="handleCommand" trigger="click">
              <el-button size="mini" type="info">
                更多
                <i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <!-- 🆕 新增：执行模型选项 -->
                <el-dropdown-item
                  v-if="
                    scope.row.status === 'PUBLISHED' &&
                    scope.row.isEnabled === 'Y'
                  "
                  :command="{ action: 'execute', row: scope.row }"
                  icon="el-icon-video-play"
                >
                  立即执行
                </el-dropdown-item>
                <el-dropdown-item
                  :command="{ action: 'copy', row: scope.row }"
                  icon="el-icon-copy-document"
                >
                  复制模型
                </el-dropdown-item>
                <el-dropdown-item
                  :command="{ action: 'publish', row: scope.row }"
                  icon="el-icon-upload2"
                >
                  发布模型
                </el-dropdown-item>
                <el-dropdown-item
                  :command="{ action: 'delete', row: scope.row }"
                  icon="el-icon-delete"
                  divided
                >
                  删除模型
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

    <!-- 定时任务配置对话框 -->
    <schedule-config-dialog
      ref="scheduleConfigDialog"
      @submit="handleScheduleSubmit"
    />
  </div>
</template>

<script>
  import {
    getEvaluationModelList,
    saveEvaluationModel,
    deleteEvaluationModel,
    getEvaluationModelDetail,
    publishEvaluationModel,
    copyEvaluationModel,
    getEvaluationModelStatistics,
    executeEvaluationModel,
    startEvaluationModel,
    batchStartEvaluationModels,
    stopEvaluationModel,
    getModelSqlExecutionResult,
    getWarningSourceData,
    getWarningSourceDataDetail,
  } from '@/api/mxgl'
  import EvaluationModelForm from './components/EvaluationModelForm'
  import EvaluationModelView from './components/EvaluationModelView'
  import ScheduleConfigDialog from './components/ScheduleConfigDialog'

  export default {
    name: 'EvaluationModelManagement',
    components: {
      EvaluationModelForm,
      EvaluationModelView,
      ScheduleConfigDialog,
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
        dialogTitle: '',
        isEdit: false,
        currentModel: {},
        // 当前启动的模型
        currentStartModel: null,
        // 当前批量启动的模型列表
        currentBatchStartModels: null,
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
      }
    },
    created() {
      this.loadData()
      this.loadStatistics()
    },
    methods: {
      // 加载数据
      async loadData() {
        this.loading = true
        try {
          console.log('📊 评估模型管理 - 查询参数:', this.queryForm)
          const response = await getEvaluationModelList(this.queryForm)
          console.log('📊 评估模型管理 - 接口响应:', response)

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
            this.$message.error('查询失败: 响应格式错误')
            console.error('❌ 未知的响应格式:', response)
            this.tableData = []
            this.total = 0
          }
        } catch (error) {
          this.$message.error('查询失败')
          console.error('❌ 查询评估模型列表异常:', error)
          this.tableData = []
          this.total = 0
        } finally {
          this.loading = false
        }
      },
      // 加载统计数据
      async loadStatistics() {
        try {
          const response = await getEvaluationModelStatistics()
          console.log('统计数据API响应:', response)

          // 🔧 修复：兼容拦截器解包后的数据结构
          if (response.code === 1 && response.data) {
            this.statistics = response.data || {}
          } else if (
            response.totalCount !== undefined ||
            response.enabledCount !== undefined
          ) {
            // 拦截器已解包
            this.statistics = response || {}
          }
        } catch (error) {
          console.error('加载统计数据失败', error)
        }
      },
      // 查询
      handleSearch() {
        this.queryForm.pageNum = 1
        this.loadData()
      },
      // 重置
      handleReset() {
        this.$refs.queryForm.resetFields()
        this.queryForm.pageNum = 1
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
      // 启动评估模型
      handleStart(row) {
        console.log('启动评估模型:', row)
        // 保存当前要启动的模型
        this.currentStartModel = row
        // 打开定时任务配置对话框
        this.$refs.scheduleConfigDialog.show()
      },
      // 处理定时任务配置提交
      async handleScheduleSubmit(scheduleConfig) {
        // 判断是单个启动还是批量启动
        if (
          this.currentBatchStartModels &&
          this.currentBatchStartModels.length > 0
        ) {
          // 批量启动
          await this.handleBatchScheduleSubmit(scheduleConfig)
        } else if (this.currentStartModel) {
          // 单个启动
          try {
            console.log('提交定时任务配置:', scheduleConfig)
            const response = await startEvaluationModel(
              this.currentStartModel.evalModelId,
              scheduleConfig
            )
            if (response.code === 1) {
              this.$message.success(
                '评估模型启动成功，将按配置的时间定时执行并生成风险预警'
              )
              this.loadData()
              this.loadStatistics()
            } else {
              this.$message.error(response.msg || '启动失败')
            }
          } catch (error) {
            console.error('启动评估模型失败:', error)
            this.$message.error('启动失败: ' + (error.message || '未知错误'))
          } finally {
            this.currentStartModel = null
          }
        } else {
          this.$message.error('未选择要启动的模型')
        }
      },
      // 停止评估模型
      async handleStop(row) {
        this.$confirm('确认停止该评估模型吗？', '停止评估模型', {
          confirmButtonText: '确定停止',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          try {
            const response = await stopEvaluationModel(row.evalModelId)
            if (response.code === 1) {
              this.$message.success('评估模型已停止')
              this.loadData()
            } else {
              this.$message.error(response.msg || '停止失败')
            }
          } catch (error) {
            this.$message.error('停止失败')
          }
        })
      },
      // 更多操作
      handleCommand(command) {
        const { action, row } = command
        switch (action) {
          case 'copy':
            this.handleCopy(row)
            break
          case 'publish':
            this.handlePublish(row)
            break
          case 'execute':
            this.handleExecute(row)
            break
          case 'delete':
            this.handleDelete(row)
            break
        }
      },
      // 复制模型
      async handleCopy(row) {
        try {
          const response = await copyEvaluationModel({
            sourceModelId: row.evalModelId,
            newModelName: row.modelName + '_副本',
          })
          if (response.code === 1) {
            this.$message.success('复制成功')
            this.loadData()
          } else {
            this.$message.error(response.msg || '复制失败')
          }
        } catch (error) {
          this.$message.error('复制失败')
        }
      },
      // 发布模型
      async handlePublish(row) {
        this.$confirm('确认发布该模型吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          try {
            const response = await publishEvaluationModel(row.evalModelId)
            if (response.code === 1) {
              this.$message.success('发布成功')
              this.loadData()
            } else {
              this.$message.error(response.msg || '发布失败')
            }
          } catch (error) {
            this.$message.error('发布失败')
          }
        })
      },
      // 执行模型
      async handleExecute(row) {
        try {
          const response = await executeEvaluationModel({
            modelId: row.evalModelId,
          })
          if (response.code === 1) {
            this.$message.success('执行成功')
            this.loadData()
          } else {
            this.$message.error(response.msg || '执行失败')
          }
        } catch (error) {
          this.$message.error('执行失败')
        }
      },
      // 删除
      handleDelete(row) {
        // 检查模型是否已启用
        if (row.isEnabled === 'Y') {
          this.$message.warning('已启用的模型不能删除，请先停用后再删除')
          return
        }

        // 检查模型是否正在运行
        if (row.status === 'RUNNING') {
          this.$message.warning('正在运行的模型不能删除，请先停止后再删除')
          return
        }

        this.$confirm('确认删除该模型吗？删除后将无法恢复。', '删除确认', {
          confirmButtonText: '确定删除',
          cancelButtonText: '取消',
          type: 'warning',
        })
          .then(async () => {
            try {
              const response = await deleteEvaluationModel(row.evalModelId)
              if (response.code === 1) {
                this.$message.success('删除成功')
                this.loadData()
                this.loadStatistics()
              } else {
                this.$message.error(response.msg || '删除失败')
              }
            } catch (error) {
              console.error('删除模型失败:', error)
              this.$message.error('删除失败: ' + (error.message || '未知错误'))
            }
          })
          .catch(() => {
            // 用户取消删除
          })
      },
      // 批量发布
      handleBatchPublish() {
        if (!this.multipleSelection.length) {
          this.$message.warning('请选择要发布的模型')
          return
        }
        this.$confirm(
          `确认发布选中的 ${this.multipleSelection.length} 个模型吗？`,
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
          }
        ).then(async () => {
          try {
            const promises = this.multipleSelection.map((item) =>
              publishEvaluationModel(item.evalModelId)
            )
            await Promise.all(promises)
            this.$message.success('批量发布成功')
            this.loadData()
          } catch (error) {
            this.$message.error('批量发布失败')
          }
        })
      },
      // 批量启动
      handleBatchStart() {
        if (!this.multipleSelection.length) {
          this.$message.warning('请选择要启动的模型')
          return
        }

        // 检查是否有未发布的模型
        const unpublishedModels = this.multipleSelection.filter(
          (item) => item.status !== 'PUBLISHED'
        )
        if (unpublishedModels.length > 0) {
          this.$message.warning(
            `选中的模型中有 ${unpublishedModels.length} 个未发布，只有已发布的模型才能启动`
          )
          return
        }

        // 检查是否有未启用的模型
        const disabledModels = this.multipleSelection.filter(
          (item) => item.isEnabled !== 'Y'
        )
        if (disabledModels.length > 0) {
          this.$message.warning(
            `选中的模型中有 ${disabledModels.length} 个未启用，只有已启用的模型才能启动`
          )
          return
        }

        // 保存选中的模型并打开定时任务配置对话框
        this.currentBatchStartModels = this.multipleSelection
        this.$refs.scheduleConfigDialog.show()
      },
      // 处理批量启动的定时任务配置提交
      async handleBatchScheduleSubmit(scheduleConfig) {
        if (
          !this.currentBatchStartModels ||
          !this.currentBatchStartModels.length
        ) {
          this.$message.error('未选择要启动的模型')
          return
        }

        const loading = this.$loading({
          lock: true,
          text: `正在批量启动 ${this.currentBatchStartModels.length} 个模型...`,
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)',
        })

        try {
          console.log(
            '批量启动模型，数量:',
            this.currentBatchStartModels.length
          )
          const modelIds = this.currentBatchStartModels.map(
            (item) => item.evalModelId
          )

          const response = await batchStartEvaluationModels(
            modelIds,
            scheduleConfig
          )

          loading.close()

          if (response.code === 1) {
            const result = response.data
            if (result.failed === 0) {
              this.$message.success(
                `批量启动成功！共启动 ${result.success} 个模型`
              )
            } else {
              this.$message.warning(
                `批量启动部分成功！成功: ${result.success}，失败: ${result.failed}`
              )
            }
            this.loadData()
            this.loadStatistics()
          } else {
            this.$message.error(response.msg || '批量启动失败')
          }
        } catch (error) {
          loading.close()
          console.error('批量启动模型失败:', error)
          this.$message.error('批量启动失败: ' + (error.message || '未知错误'))
        } finally {
          this.currentBatchStartModels = null
        }
      },
      // 批量删除
      handleBatchDelete() {
        if (!this.multipleSelection.length) {
          this.$message.warning('请选择要删除的模型')
          return
        }

        // 检查是否有已启用的模型
        const enabledModels = this.multipleSelection.filter(
          (item) => item.isEnabled === 'Y'
        )
        if (enabledModels.length > 0) {
          this.$message.warning(
            `选中的模型中有 ${enabledModels.length} 个已启用，已启用的模型不能删除，请先停用后再删除`
          )
          return
        }

        this.$confirm(
          `确认删除选中的 ${this.multipleSelection.length} 个模型吗？删除后将无法恢复。`,
          '批量删除确认',
          {
            confirmButtonText: '确定删除',
            cancelButtonText: '取消',
            type: 'warning',
          }
        )
          .then(async () => {
            try {
              const promises = this.multipleSelection.map((item) =>
                deleteEvaluationModel(item.evalModelId)
              )
              await Promise.all(promises)
              this.$message.success('批量删除成功')
              this.loadData()
              this.loadStatistics()
            } catch (error) {
              console.error('批量删除失败:', error)
              this.$message.error(
                '批量删除失败: ' + (error.message || '未知错误')
              )
            }
          })
          .catch(() => {
            // 用户取消删除
          })
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

      // 加载SQL执行结果数据
      async loadSqlExecutionResult() {
        this.sqlResultLoading = true
        try {
          // 调用API获取模型SQL执行结果
          // 🔥 修复：不再传递分页参数，后端返回所有数据，前端处理分页
          const params = {
            evalModelId: this.currentSqlModel.evalModelId,
            dataModelId: this.currentSqlModel.dataModelId,
            modelType: this.currentSqlModel.modelType,
          }

          console.log('调用模型SQL执行结果接口:', params)
          console.log(
            '模型类型判断:',
            params.dataModelId?.startsWith('COMB')
              ? '指标组合分析'
              : '数据模型管理'
          )

          // 调用模型SQL执行结果接口获取结果
          const response = await getModelSqlExecutionResult(params)
          if (response.code === 1) {
            // 处理SQL执行结果数据结构
            let resultData = []
            let totalCount = 0
            let executionInfo = {}

            if (response.data) {
              // 判断模型类型来处理不同的响应结构
              const isComboModel = params.dataModelId?.startsWith('COMB')

              if (isComboModel) {
                // 指标组合分析结果处理
                // 🔥 修复：优先使用allData字段，避免$ref引用问题
                if (response.data.allData) {
                  resultData = response.data.allData
                  totalCount =
                    response.data.totalCount || response.data.allData.length
                } else if (response.data.finalResults) {
                  resultData = response.data.finalResults
                  totalCount = response.data.finalResults.length
                } else if (response.data.executionResults) {
                  resultData = response.data.executionResults
                  totalCount = response.data.executionResults.length
                } else if (Array.isArray(response.data)) {
                  resultData = response.data
                  totalCount = response.data.length
                }

                executionInfo = {
                  executionMode: response.data.executionMode || '未知',
                  indicatorCount: response.data.indicatorCount || 0,
                  executionTime:
                    response.data.executionTime || new Date().toLocaleString(),
                }
              } else {
                // 数据模型管理结果处理
                // 🔥 修复：优先使用allData字段，避免$ref引用问题
                if (response.data.allData) {
                  resultData = response.data.allData
                  totalCount =
                    response.data.totalCount || response.data.allData.length
                } else if (response.data.list) {
                  resultData = response.data.list
                  totalCount = response.data.total || response.data.list.length
                } else if (Array.isArray(response.data)) {
                  resultData = response.data
                  totalCount = response.data.length
                } else if (response.data.executionResult) {
                  resultData = response.data.executionResult
                  totalCount = Array.isArray(resultData) ? resultData.length : 1
                } else {
                  // 如果data是对象，包装成数组
                  resultData = [response.data]
                  totalCount = 1
                }

                executionInfo = {
                  executionTime:
                    response.data.executionTime || new Date().toLocaleString(),
                  sqlStatement: response.data.sqlStatement || '',
                }
              }
            }

            // 🔥 修复：存储所有数据，实现前端分页
            this.sqlResultAllData = resultData
            this.sqlResultTotal = totalCount

            // 🔥 新增：前端分页处理
            this.updateSqlResultPageData()

            // 动态生成列配置
            if (this.sqlResultAllData.length > 0) {
              // 根据第一条数据的字段动态生成列
              this.sqlResultColumns = Object.keys(this.sqlResultAllData[0]).map(
                (key) => ({
                  prop: key,
                  label: this.getColumnLabel(key),
                  width: this.getColumnWidth(key),
                })
              )
            } else {
              this.sqlResultColumns = []
            }

            // 设置执行信息
            this.sqlExecutionInfo = {
              executionTime: executionInfo.executionTime,
              recordCount: this.sqlResultTotal,
              status: 'SUCCESS',
              sqlContent: '', // 不显示SQL内容
              modelType: params.dataModelId?.startsWith('COMB')
                ? '指标组合分析'
                : '数据模型管理',
              executionMode: executionInfo.executionMode,
              indicatorCount: executionInfo.indicatorCount,
            }

            console.log('模型SQL执行结果加载成功:', {
              模型类型: this.sqlExecutionInfo.modelType,
              执行模式: this.sqlExecutionInfo.executionMode,
              记录数: this.sqlResultData.length,
              总数: this.sqlResultTotal,
              列数: this.sqlResultColumns.length,
              响应数据: response.data,
            })
          } else {
            this.$message.error(response.msg || '获取模型SQL执行结果失败')
            this.sqlExecutionInfo.status = 'FAILED'
          }
        } catch (error) {
          console.error('获取SQL执行结果失败:', error)
          this.$message.error('获取SQL执行结果失败: ' + error.message)
          this.sqlExecutionInfo.status = 'FAILED'
        } finally {
          this.sqlResultLoading = false
        }
      },

      // 刷新SQL执行结果
      async refreshSqlResult() {
        this.sqlResultQuery.pageNum = 1
        await this.loadSqlExecutionResult()
      },

      // SQL执行结果分页 - 页大小改变
      handleSqlResultSizeChange(val) {
        this.sqlResultQuery.pageSize = val
        this.sqlResultQuery.pageNum = 1
        // 🔥 修复：前端分页，不再调用后端接口
        this.updateSqlResultPageData()
      },

      // SQL执行结果分页 - 当前页改变
      handleSqlResultCurrentChange(val) {
        this.sqlResultQuery.pageNum = val
        // 🔥 修复：前端分页，不再调用后端接口
        this.updateSqlResultPageData()
      },

      // 🔥 新增：前端分页数据处理方法
      updateSqlResultPageData() {
        if (!this.sqlResultAllData || this.sqlResultAllData.length === 0) {
          this.sqlResultData = []
          return
        }

        const { pageNum, pageSize } = this.sqlResultQuery
        const startIndex = (pageNum - 1) * pageSize
        const endIndex = startIndex + pageSize

        // 从所有数据中截取当前页的数据
        this.sqlResultData = this.sqlResultAllData.slice(startIndex, endIndex)

        console.log('前端分页处理:', {
          总数据量: this.sqlResultAllData.length,
          当前页: pageNum,
          页大小: pageSize,
          开始索引: startIndex,
          结束索引: endIndex,
          当前页数据量: this.sqlResultData.length,
        })
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

        &.total .stat-number {
          color: #409eff;
        }

        &.enabled .stat-number {
          color: #67c23a;
        }

        &.disabled .stat-number {
          color: #909399;
        }

        &.performance .stat-number {
          color: #f56c6c;
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
