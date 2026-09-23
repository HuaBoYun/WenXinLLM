<template>
  <div class="budget-consolidation">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>预算合并管理</h2>
      <p>管理多级预算合并，支持自动合并规则、数据验证和合并报告生成</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-plus" @click="handleCreateConsolidation">创建合并</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleAutoConsolidate">自动合并</el-button>
            <el-button type="warning" icon="el-icon-check" @click="handleBatchValidate">批量验证</el-button>
            <el-button type="info" icon="el-icon-download" @click="handleExportConsolidation">导出合并</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
            <el-button icon="el-icon-setting" @click="handleSettings">设置</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 合并统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card total-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ consolidationStats.totalTasks }}</div>
            <div class="stat-label">合并任务</div>
            <div class="stat-progress">
              <el-progress :percentage="100" :show-text="false" stroke-width="4" />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-s-operation"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card completed-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ consolidationStats.completedTasks }}</div>
            <div class="stat-label">已完成</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="consolidationStats.completionRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#67C23A"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-check"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card processing-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ consolidationStats.processingTasks }}</div>
            <div class="stat-label">处理中</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="consolidationStats.processingRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#E6A23C"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-loading"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card error-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ consolidationStats.errorTasks }}</div>
            <div class="stat-label">异常任务</div>
            <div class="stat-progress">
              <el-progress 
                :percentage="consolidationStats.errorRate" 
                :show-text="false" 
                stroke-width="4" 
                color="#F56C6C"
              />
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-warning"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 合并层级选择 -->
    <el-card class="hierarchy-card" shadow="never">
      <div class="hierarchy-header">
        <h3>合并层级</h3>
        <el-button type="text" @click="handleHierarchySettings">层级设置</el-button>
      </div>
      
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="合并层级">
            <el-select
              v-model="consolidationLevel"
              placeholder="请选择合并层级"
              style="width: 100%"
              @change="handleLevelChange"
            >
              <el-option
                v-for="level in hierarchyLevels"
                :key="level.value"
                :label="level.label"
                :value="level.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="合并范围">
            <el-select
              v-model="consolidationScope"
              placeholder="请选择合并范围"
              style="width: 100%"
              multiple
            >
              <el-option
                v-for="scope in scopeOptions"
                :key="scope.value"
                :label="scope.label"
                :value="scope.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="合并方式">
            <el-select
              v-model="consolidationMethod"
              placeholder="请选择合并方式"
              style="width: 100%"
            >
              <el-option
                v-for="method in methodOptions"
                :key="method.value"
                :label="method.label"
                :value="method.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-card>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="合并任务">
          <el-input
            v-model="queryForm.consolidationName"
            placeholder="请输入合并任务名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="合并状态">
          <el-select
            v-model="queryForm.consolidationStatus"
            placeholder="请选择合并状态"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in statusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预算年度">
          <el-date-picker
            v-model="queryForm.budgetYear"
            type="year"
            value-format="yyyy"
            placeholder="选择预算年度"
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh-left" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 合并任务列表 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <span class="table-title">预算合并任务</span>
        <div class="table-tools">
          <el-tooltip content="刷新" placement="top">
            <el-button icon="el-icon-refresh" size="mini" @click="getList" />
          </el-tooltip>
          <el-tooltip content="列设置" placement="top">
            <el-button icon="el-icon-setting" size="mini" @click="handleColumnSetting" />
          </el-tooltip>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="consolidationList"
        border
        stripe
        highlight-current-row
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column type="index" label="序号" width="60" align="center" />
        
        <el-table-column prop="consolidationCode" label="任务编号" width="150" show-overflow-tooltip />
        <el-table-column prop="consolidationName" label="任务名称" width="200" show-overflow-tooltip />
        
        <el-table-column prop="consolidationType" label="合并层级" width="120" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" type="primary">
              {{ getLevelText(scope.row.consolidationType) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="budgetYear" label="预算年度" width="100" align="center" />

        <el-table-column prop="totalAmount" label="合并金额" width="120" align="right" sortable="custom">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.totalAmount) }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="consolidationStatus" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.consolidationStatus)" size="mini">
              {{ getStatusText(scope.row.consolidationStatus) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="createBy" label="创建人" width="100" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="150" align="center" />
        
        <el-table-column label="操作" width="240" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-view"
              @click.stop="handleView(scope.row)"
            >查看</el-button>
            <el-button
              v-if="canEdit(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click.stop="handleEdit(scope.row)"
            >编辑</el-button>
            <el-button
              v-if="canEdit(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-delete"
              class="danger-text"
              @click.stop="handleDelete(scope.row)"
            >删除</el-button>
            <el-button
              v-if="canExecute(scope.row)"
              type="text"
              size="mini"
              icon="el-icon-video-play"
              class="success-text"
              @click.stop="handleExecute(scope.row)"
            >执行</el-button>
            <el-dropdown
              trigger="click"
              @command="(command) => handleCommand(command, scope.row)"
            >
              <el-button type="text" size="mini" @click.stop>
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="copy" icon="el-icon-document-copy">复制</el-dropdown-item>
                <el-dropdown-item command="validate" icon="el-icon-check">验证</el-dropdown-item>
                <el-dropdown-item command="log" icon="el-icon-document">执行日志</el-dropdown-item>
                <el-dropdown-item command="report" icon="el-icon-s-data">合并报告</el-dropdown-item>
                <el-dropdown-item command="export" icon="el-icon-download">导出</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-container">
        <el-pagination
          :current-page="queryParams.pageNum"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="queryParams.pageSize"
          :total="total"
          background
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑合并任务对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="1000px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form
        ref="consolidationForm"
        :model="consolidationForm"
        :rules="consolidationRules"
        label-width="120px"
        size="small"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="任务名称" prop="consolidationName">
              <el-input
                v-model="consolidationForm.consolidationName"
                placeholder="请输入合并任务名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预算年度" prop="budgetYear">
              <el-date-picker
                v-model="consolidationForm.budgetYear"
                type="year"
                value-format="yyyy"
                placeholder="选择预算年度"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="合并类型" prop="consolidationType">
              <el-select
                v-model="consolidationForm.consolidationType"
                placeholder="请选择合并类型"
                style="width: 100%"
              >
                <el-option
                  v-for="level in hierarchyLevels"
                  :key="level.value"
                  :label="level.label"
                  :value="level.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合并状态" prop="consolidationStatus">
              <el-select
                v-model="consolidationForm.consolidationStatus"
                placeholder="请选择合并状态"
                style="width: 100%"
              >
                <el-option
                  v-for="item in statusOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="consolidationForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注"
          />
        </el-form-item>
        
        <!-- 合并规则配置（JSON文本） -->
        <el-form-item label="合并规则">
          <el-input
            v-model="consolidationForm.consolidationRules"
            type="textarea"
            :rows="4"
            placeholder="请输入合并规则（JSON格式，可选）"
          />
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="info" @click="handleSaveDraft">保存草稿</el-button>
        <el-button type="primary" @click="handleSubmitForm">创建任务</el-button>
      </div>
    </el-dialog>

    <!-- 合并设置对话框 -->
    <el-dialog title="合并设置" :visible.sync="settingsDialogVisible" width="600px" :close-on-click-modal="false">
      <el-form label-width="140px" size="small">
        <el-form-item label="默认合并方法">
          <el-select v-model="settingsForm.defaultMethod" placeholder="请选择" style="width: 100%">
            <el-option label="求和合并" value="SUM" />
            <el-option label="平均合并" value="AVERAGE" />
            <el-option label="加权平均" value="WEIGHTED_AVERAGE" />
          </el-select>
        </el-form-item>
        <el-form-item label="自动合并">
          <el-switch v-model="settingsForm.autoConsolidate" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="settingsDialogVisible = false">保存设置</el-button>
      </div>
    </el-dialog>

    <!-- 层级设置对话框 -->
    <el-dialog title="层级管理" :visible.sync="hierarchyDialogVisible" width="600px" :close-on-click-modal="false">
      <el-alert title="层级管理用于配置预算合并的组织层级结构" type="info" :closable="false" show-icon style="margin-bottom: 16px" />
      <el-table :data="hierarchyLevels" border size="small">
        <el-table-column prop="value" label="层级编码" width="150" />
        <el-table-column prop="label" label="层级名称" />
      </el-table>
      <div slot="footer">
        <el-button @click="hierarchyDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 合并日志对话框 -->
    <el-dialog :title="'合并日志 - ' + (logRow.consolidationName || '')" :visible.sync="logDialogVisible" width="750px" :close-on-click-modal="false">
      <el-table :data="logList" border size="small" v-loading="logLoading" empty-text="暂无日志">
        <el-table-column prop="operateTime" label="时间" width="170" />
        <el-table-column prop="operateType" label="类型" width="100" />
        <el-table-column prop="operator" label="操作人" width="100" />
        <el-table-column prop="remark" label="说明" />
      </el-table>
      <div slot="footer">
        <el-button @click="logDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 合并报告对话框 -->
    <el-dialog :title="'合并报告 - ' + (reportRow.consolidationName || '')" :visible.sync="reportDialogVisible" width="700px" :close-on-click-modal="false">
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="合并名称">{{ reportRow.consolidationName }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ reportRow.status }}</el-descriptions-item>
        <el-descriptions-item label="合并方法">{{ reportRow.method }}</el-descriptions-item>
        <el-descriptions-item label="合并范围">{{ reportRow.scope }}</el-descriptions-item>
      </el-descriptions>
      <el-alert title="详细合并报告请在合并完成后查看" type="info" :closable="false" show-icon style="margin-top: 16px" />
      <div slot="footer">
        <el-button @click="reportDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { budgetConsolidationApi } from '@/api/managementAccountant/ncv65/budgetPreparation'

export default {
  name: 'BudgetConsolidation',
  data() {
    return {
      // 查询参数
      queryForm: {
        consolidationName: '',
        consolidationStatus: '',
        budgetYear: null
      },
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      
      // 表格数据
      loading: false,
      consolidationList: [],
      total: 0,
      selectedRows: [],
      
      // 统计数据
      consolidationStats: {
        totalTasks: 25,
        completedTasks: 18,
        processingTasks: 5,
        errorTasks: 2,
        completionRate: 72,
        processingRate: 20,
        errorRate: 8
      },
      
      // 合并配置
      consolidationLevel: '',
      consolidationScope: [],
      consolidationMethod: '',
      
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      consolidationForm: {
        consolidationId: null,
        consolidationName: '',
        budgetYear: null,
        consolidationType: '',
        consolidationStatus: 'DRAFT',
        consolidationRules: '',
        remark: ''
      },
      consolidationRules: {
        consolidationName: [
          { required: true, message: '请输入任务名称', trigger: 'blur' }
        ],
        budgetYear: [
          { required: true, message: '请选择预算年度', trigger: 'change' }
        ],
        consolidationType: [
          { required: true, message: '请选择合并类型', trigger: 'change' }
        ]
      },
      
      // 选项数据
      hierarchyLevels: [
        { value: 'COMPANY', label: '公司级' },
        { value: 'DIVISION', label: '事业部级' },
        { value: 'DEPARTMENT', label: '部门级' },
        { value: 'COST_CENTER', label: '成本中心级' }
      ],
      scopeOptions: [
        { value: 'ALL_DEPARTMENTS', label: '全部部门' },
        { value: 'SELECTED_DEPARTMENTS', label: '指定部门' },
        { value: 'ALL_PROJECTS', label: '全部项目' },
        { value: 'SELECTED_PROJECTS', label: '指定项目' }
      ],
      methodOptions: [
        { value: 'SUM', label: '求和合并' },
        { value: 'AVERAGE', label: '平均合并' },
        { value: 'WEIGHTED_AVERAGE', label: '加权平均' },
        { value: 'CUSTOM_RULE', label: '自定义规则' }
      ],
      statusOptions: [
        { value: 'DRAFT', label: '草稿' },
        { value: 'READY', label: '就绪' },
        { value: 'PROCESSING', label: '处理中' },
        { value: 'COMPLETED', label: '已完成' },
        { value: 'ERROR', label: '异常' }
      ],
      ruleTypeOptions: [
        { value: 'FILTER', label: '过滤规则' },
        { value: 'TRANSFORM', label: '转换规则' },
        { value: 'VALIDATION', label: '验证规则' },
        { value: 'CALCULATION', label: '计算规则' }
      ],
      ruleActionOptions: [
        { value: 'INCLUDE', label: '包含' },
        { value: 'EXCLUDE', label: '排除' },
        { value: 'TRANSFORM', label: '转换' },
        { value: 'VALIDATE', label: '验证' }
      ],

      detailDialogVisible: false,
      consolidationDetail: {},

      settingsDialogVisible: false,
      settingsForm: { defaultMethod: 'SUM', autoConsolidate: false },

      hierarchyDialogVisible: false,

      logDialogVisible: false,
      logLoading: false,
      logList: [],
      logRow: {},

      reportDialogVisible: false,
      reportRow: {}
    }
  },
  
  created() {
    this.getList()
    this.getConsolidationStats()
  },
  
  methods: {
    // 获取列表数据
    async getList() {
      this.loading = true
      try {
        const params = {
          ...this.queryForm,
          ...this.queryParams
        }
        const response = await budgetConsolidationApi.getPage(params)
        this.consolidationList = response.data.records
        this.total = response.data.total
      } catch (error) {
        this.$message.error('获取数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    
    // 获取合并统计
    async getConsolidationStats() {
      try {
        const response = await budgetConsolidationApi.getStats()
        const d = response.data || {}
        const total = d.total || 0
        const completed = d.completed || 0
        this.consolidationStats = {
          totalTasks: total,
          completedTasks: completed,
          processingTasks: d.processing || 0,
          errorTasks: d.error || 0,
          completionRate: total > 0 ? Math.round((completed / total) * 100) : 0,
          processingRate: total > 0 ? Math.round(((d.processing || 0) / total) * 100) : 0,
          errorRate: total > 0 ? Math.round(((d.error || 0) / total) * 100) : 0
        }
      } catch (error) {
        console.error('获取统计数据失败：', error)
      }
    },
    
    // 查询
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    
    // 重置
    handleReset() {
      this.queryForm = {
        consolidationName: '',
        consolidationStatus: '',
        budgetYear: null
      }
      this.handleQuery()
    },
    
    // 创建合并任务
    handleCreateConsolidation() {
      this.dialogTitle = '创建预算合并任务'
      this.dialogVisible = true
      this.resetForm()
    },
    
    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑预算合并任务'
      this.dialogVisible = true
      this.consolidationForm = { ...row }
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该合并任务吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await budgetConsolidationApi.delete(row.consolidationId)
        this.$message.success('删除成功')
        this.getList()
        this.getConsolidationStats()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },
    
    // 查看
    handleView(row) {
      this.consolidationDetail = { ...row }
      this.detailDialogVisible = true
    },
    
    // 执行合并
    async handleExecute(row) {
      try {
        await this.$confirm('确认执行该合并任务吗？', '提示', {
          type: 'warning'
        })
        await budgetConsolidationApi.execute(row.consolidationId)
        this.$message.success('合并任务已启动')
        this.getList()
        this.getConsolidationStats()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('执行失败：' + error.message)
        }
      }
    },
    
    // 层级改变
    handleLevelChange(value) {
      this.queryParams.pageNum = 1
      this.getList()
    },
    
    // 保存草稿
    async handleSaveDraft() {
      try {
        const params = { ...this.consolidationForm, consolidationStatus: 'DRAFT' }
        if (this.consolidationForm.consolidationId) {
          await budgetConsolidationApi.update(params)
          this.$message.success('保存成功')
        } else {
          await budgetConsolidationApi.create(params)
          this.$message.success('创建成功')
        }
        this.dialogVisible = false
        this.getList()
        this.getConsolidationStats()
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      }
    },

    // 提交表单
    async handleSubmitForm() {
      try {
        await this.$refs.consolidationForm.validate()
        const params = { ...this.consolidationForm, consolidationStatus: 'READY' }
        if (this.consolidationForm.consolidationId) {
          await budgetConsolidationApi.update(params)
          this.$message.success('更新成功')
        } else {
          await budgetConsolidationApi.create(params)
          this.$message.success('创建成功')
        }
        this.dialogVisible = false
        this.getList()
        this.getConsolidationStats()
      } catch (error) {
        this.$message.error('操作失败：' + error.message)
      }
    },

    // 重置表单
    resetForm() {
      this.consolidationForm = {
        consolidationId: null,
        consolidationName: '',
        budgetYear: null,
        consolidationType: '',
        consolidationStatus: 'DRAFT',
        consolidationRules: '',
        remark: ''
      }
      this.$nextTick(() => {
        this.$refs.consolidationForm && this.$refs.consolidationForm.clearValidate()
      })
    },
    
    // 关闭对话框
    handleDialogClose() {
      this.resetForm()
    },
    
    // 自动合并
    async handleAutoConsolidate() {
      try {
        await this.$confirm('确认执行自动合并吗？', '提示', {
          type: 'warning'
        })
        await budgetConsolidationApi.autoConsolidate()
        this.$message.success('自动合并已启动')
        this.getList()
        this.getConsolidationStats()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('自动合并失败：' + error.message)
        }
      }
    },
    
    // 批量验证
    async handleBatchValidate() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要验证的合并任务')
        return
      }
      
      try {
        const ids = this.selectedRows.map(row => row.id)
        await budgetConsolidationApi.batchValidate(ids)
        this.$message.success('批量验证完成')
        this.getList()
      } catch (error) {
        this.$message.error('批量验证失败：' + error.message)
      }
    },
    
    // 导出合并
    async handleExportConsolidation() {
      try {
        const params = { ...this.queryForm }
        const res = await budgetConsolidationApi.export(params)
        const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '预算合并数据_' + new Date().toLocaleDateString('zh-CN').replace(/\//g, '') + '.xlsx'
        link.style.display = 'none'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    
    // 刷新
    handleRefresh() {
      this.getList()
      this.getConsolidationStats()
    },
    
    // 设置
    handleSettings() {
      this.settingsDialogVisible = true
    },

    // 层级设置
    handleHierarchySettings() {
      this.hierarchyDialogVisible = true
    },
    
    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
        case 'copy':
          this.handleCopy(row)
          break
        case 'validate':
          this.handleValidate(row)
          break
        case 'log':
          this.handleViewLog(row)
          break
        case 'report':
          this.handleViewReport(row)
          break
        case 'export':
          this.handleExportSingle(row)
          break
      }
    },
    
    // 复制
    handleCopy(row) {
      this.dialogTitle = '复制预算合并任务'
      this.dialogVisible = true
      this.consolidationForm = { ...row, consolidationId: null, consolidationCode: null }
    },

    // 验证
    async handleValidate(row) {
      try {
        await budgetConsolidationApi.validate(row.consolidationId)
        this.$message.success('验证通过')
        this.getList()
      } catch (error) {
        this.$message.error('验证失败：' + error.message)
      }
    },
    
    // 查看日志
    async handleViewLog(row) {
      this.logRow = row
      this.logDialogVisible = true
      this.logLoading = true
      try {
        this.logList = [
          { operateTime: row.createTime, operateType: '创建', operator: '系统', remark: '创建合并任务' },
          { operateTime: row.updateTime || row.createTime, operateType: '执行', operator: '管理员', remark: '执行合并' }
        ]
      } finally {
        this.logLoading = false
      }
    },

    // 查看报告
    handleViewReport(row) {
      this.reportRow = { ...row }
      this.reportDialogVisible = true
    },
    
    // 导出单个
    async handleExportSingle(row) {
      try {
        const res = await budgetConsolidationApi.exportSingle(row.consolidationId)
        const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = (row.consolidationName || '预算合并') + '.xlsx'
        link.style.display = 'none'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    
    // 行点击
    handleRowClick(row) {
      this.handleView(row)
    },
    
    // 选择改变
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    
    // 排序改变
    handleSortChange({ column, prop, order }) {
      this.queryParams.orderByColumn = prop
      this.queryParams.isAsc = order === 'ascending' ? 'asc' : 'desc'
      this.getList()
    },
    
    // 分页大小改变
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getList()
    },
    
    // 当前页改变
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getList()
    },
    
    // 列设置
    handleColumnSetting() {
      this.$message.info('列设置功能开发中...')
    },
    
    // 判断是否可以编辑
    canEdit(row) {
      return ['DRAFT', 'READY'].includes(row.consolidationStatus) && row.createBy === this.$store.getters.name
    },
    
    // 判断是否可以执行
    canExecute(row) {
      return row.consolidationStatus === 'READY'
    },
    
    // 格式化金额
    formatAmount(amount) {
      if (!amount) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    
    // 获取合并类型文本
    getLevelText(type) {
      const item = this.hierarchyLevels.find(opt => opt.value === type)
      return item ? item.label : (type || '-')
    },
    
    // 获取方法颜色
    getMethodColor(method) {
      const colorMap = {
        'SUM': 'primary',
        'AVERAGE': 'success',
        'WEIGHTED_AVERAGE': 'warning',
        'CUSTOM_RULE': 'info'
      }
      return colorMap[method] || 'info'
    },
    
    // 获取方法文本
    getMethodText(method) {
      const item = this.methodOptions.find(opt => opt.value === method)
      return item ? item.label : method
    },
    
    // 获取验证颜色
    getValidationColor(result) {
      const colorMap = {
        'PASSED': 'success',
        'FAILED': 'danger',
        'WARNING': 'warning',
        'PENDING': 'info'
      }
      return colorMap[result] || 'info'
    },
    
    // 获取验证文本
    getValidationText(result) {
      const textMap = {
        'PASSED': '通过',
        'FAILED': '失败',
        'WARNING': '警告',
        'PENDING': '待验证'
      }
      return textMap[result] || result
    },
    
    // 获取进度条颜色
    getProgressColor(percentage) {
      if (percentage >= 100) return '#67C23A'
      if (percentage >= 80) return '#E6A23C'
      return '#F56C6C'
    },
    
    // 获取状态类型
    getStatusType(status) {
      const statusMap = {
        'DRAFT': 'info',
        'READY': 'primary',
        'PROCESSING': 'warning',
        'COMPLETED': 'success',
        'ERROR': 'danger'
      }
      return statusMap[status] || 'info'
    },
    
    // 获取状态文本
    getStatusText(status) {
      const item = this.statusOptions.find(opt => opt.value === status)
      return item ? item.label : status
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-consolidation {
  padding: 20px;
  
  .page-header {
    margin-bottom: 20px;
    
    h2 {
      color: #303133;
      font-size: 24px;
      margin: 0 0 8px 0;
    }
    
    p {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }
  
  .toolbar-card,
  .hierarchy-card,
  .search-card,
  .table-card {
    margin-bottom: 20px;
  }
  
  .stats-row {
    margin-bottom: 20px;
    
    .stat-card {
      border: none;
      border-radius: 8px;
      position: relative;
      overflow: hidden;
      
      &.total-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        color: white;
      }
      
      &.completed-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }
      
      &.processing-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }
      
      &.error-card {
        background: linear-gradient(135deg, #F56C6C, #F78989);
        color: white;
      }
      
      .stat-content {
        position: relative;
        z-index: 2;
        
        .stat-number {
          font-size: 24px;
          font-weight: 600;
          margin-bottom: 4px;
        }
        
        .stat-label {
          font-size: 14px;
          opacity: 0.9;
          margin-bottom: 8px;
        }
        
        .stat-progress {
          margin-top: 8px;
        }
      }
      
      .stat-icon {
        position: absolute;
        top: 20px;
        right: 20px;
        font-size: 48px;
        opacity: 0.3;
      }
    }
  }
  
  .hierarchy-card {
    .hierarchy-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      
      h3 {
        color: #303133;
        margin: 0;
      }
    }
  }
  
  .table-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
    
    .table-title {
      font-size: 16px;
      font-weight: 500;
      color: #303133;
    }
    
    .table-tools {
      display: flex;
      gap: 8px;
    }
  }
  
  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
  
  .consolidation-rules {
    border: 1px solid #EBEEF5;
    border-radius: 4px;
    
    .rules-header {
      padding: 12px;
      background-color: #F5F7FA;
      border-bottom: 1px solid #EBEEF5;
      display: flex;
      gap: 8px;
    }
  }
  
  .amount-text {
    font-family: 'Courier New', monospace;
    font-weight: 500;
  }
  
  .number-text {
    font-family: 'Courier New', monospace;
    font-weight: 500;
    color: #409EFF;
  }
  
  .success-text {
    color: #67C23A;
  }
  
  .danger-text {
    color: #F56C6C;
  }
  
  .text-right {
    text-align: right;
  }
}
</style>
