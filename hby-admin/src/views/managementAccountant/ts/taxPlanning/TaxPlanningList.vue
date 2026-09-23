<template>
  <div class="tax-planning-list">
    <!-- 搜索筛选区域 -->
    <div class="search-section">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="筹划编号">
          <el-input
            v-model="searchForm.planningCode"
            placeholder="请输入筹划编号"
            style="width: 180px"
            clearable
          />
        </el-form-item>
        
        <el-form-item label="筹划名称">
          <el-input
            v-model="searchForm.planningName"
            placeholder="请输入筹划名称"
            style="width: 180px"
            clearable
          />
        </el-form-item>
        
        <el-form-item label="筹划类型">
          <el-select
            v-model="searchForm.planningType"
            placeholder="请选择筹划类型"
            style="width: 150px"
            clearable
          >
            <el-option
              v-for="(label, value) in planningTypeOptions"
              :key="value"
              :label="label"
              :value="value"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="筹划状态">
          <el-select
            v-model="searchForm.planningStatus"
            placeholder="请选择筹划状态"
            style="width: 120px"
            clearable
          >
            <el-option
              v-for="(label, value) in planningStatusOptions"
              :key="value"
              :label="label"
              :value="value"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="执行状态">
          <el-select
            v-model="searchForm.executionStatus"
            placeholder="请选择执行状态"
            style="width: 120px"
            clearable
          >
            <el-option
              v-for="(label, value) in executionStatusOptions"
              :key="value"
              :label="label"
              :value="value"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="风险等级">
          <el-select
            v-model="searchForm.riskLevel"
            placeholder="请选择风险等级"
            style="width: 120px"
            clearable
          >
            <el-option
              v-for="(label, value) in riskLevelOptions"
              :key="value"
              :label="label"
              :value="value"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="责任人">
          <el-input
            v-model="searchForm.responsiblePerson"
            placeholder="请输入责任人"
            style="width: 120px"
            clearable
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">
            搜索
          </el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作工具栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="small"
          @click="$emit('create-planning')"
        >
          新建筹划
        </el-button>
        
        <el-button
          type="success"
          icon="el-icon-download"
          size="small"
          :disabled="!selectedRows.length"
          @click="handleBatchExport"
        >
          批量导出
        </el-button>
        
        <el-button
          type="warning"
          icon="el-icon-folder"
          size="small"
          :disabled="!selectedRows.length"
          @click="handleBatchArchive"
        >
          批量归档
        </el-button>
        
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="small"
          :disabled="!selectedRows.length"
          @click="handleBatchDelete"
        >
          批量删除
        </el-button>
      </div>
      
      <div class="toolbar-right">
        <el-tooltip content="刷新" placement="top">
          <el-button icon="el-icon-refresh" size="small" @click="refreshData" />
        </el-tooltip>
        
        <el-tooltip content="列设置" placement="top">
          <el-button icon="el-icon-setting" size="small" @click="columnSettingVisible = true" />
        </el-tooltip>
      </div>
    </div>

    <!-- 数据表格 -->
    <div class="table-section">
      <el-table
        ref="dataTable"
        v-loading="loading"
        :data="tableData"
        stripe
        border
        height="600"
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <el-table-column type="selection" width="50" fixed="left" />
        
        <el-table-column
          prop="planningCode"
          label="筹划编号"
          width="140"
          fixed="left"
          show-overflow-tooltip
        />
        
        <el-table-column
          prop="planningName"
          label="筹划名称"
          width="200"
          show-overflow-tooltip
        />
        
        <el-table-column
          prop="planningType"
          label="筹划类型"
          width="120"
          :formatter="formatPlanningType"
        />
        
        <el-table-column
          prop="planningStatus"
          label="筹划状态"
          width="100"
        >
          <template slot-scope="scope">
            <el-tag :type="getPlanningStatusTagType(scope.row.planningStatus)" size="small">
              {{ getPlanningStatusLabel(scope.row.planningStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column
          prop="executionStatus"
          label="执行状态"
          width="100"
        >
          <template slot-scope="scope">
            <el-tag :type="getExecutionStatusTagType(scope.row.executionStatus)" size="small">
              {{ getExecutionStatusLabel(scope.row.executionStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column
          prop="taxType"
          label="税种"
          width="120"
          :formatter="formatTaxType"
        />
        
        <el-table-column
          prop="taxSavingAmount"
          label="节税金额(万元)"
          width="130"
          align="right"
          sortable="custom"
        >
          <template slot-scope="scope">
            <span class="amount-text success">
              {{ formatAmount(scope.row.taxSavingAmount) }}
            </span>
          </template>
        </el-table-column>
        
        <el-table-column
          prop="netBenefit"
          label="净收益(万元)"
          width="120"
          align="right"
          sortable="custom"
        >
          <template slot-scope="scope">
            <span class="amount-text" :class="getBenefitClass(scope.row.netBenefit)">
              {{ formatAmount(scope.row.netBenefit) }}
            </span>
          </template>
        </el-table-column>
        
        <el-table-column
          prop="roi"
          label="投资回报率"
          width="100"
          align="right"
          sortable="custom"
        >
          <template slot-scope="scope">
            <span class="percentage-text" :class="getRoiClass(scope.row.roi)">
              {{ formatPercentage(scope.row.roi) }}
            </span>
          </template>
        </el-table-column>
        
        <el-table-column
          prop="riskLevel"
          label="风险等级"
          width="100"
        >
          <template slot-scope="scope">
            <el-tag :type="getRiskLevelTagType(scope.row.riskLevel)" size="small">
              {{ getRiskLevelLabel(scope.row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column
          prop="priority"
          label="优先级"
          width="80"
        >
          <template slot-scope="scope">
            <el-tag :type="getPriorityTagType(scope.row.priority)" size="small">
              {{ getPriorityLabel(scope.row.priority) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column
          prop="executionProgress"
          label="执行进度"
          width="120"
        >
          <template slot-scope="scope">
            <el-progress
              :percentage="Number(scope.row.executionProgress || 0)"
              :stroke-width="6"
              :show-text="false"
            />
            <span class="progress-text">{{ Number(scope.row.executionProgress || 0) }}%</span>
          </template>
        </el-table-column>
        
        <el-table-column
          prop="responsiblePerson"
          label="责任人"
          width="100"
          show-overflow-tooltip
        />
        
        <el-table-column
          prop="startTime"
          label="开始时间"
          width="110"
          :formatter="formatDate"
        />
        
        <el-table-column
          prop="endTime"
          label="结束时间"
          width="110"
          :formatter="formatDate"
        />
        
        <el-table-column
          prop="createdTime"
          label="创建时间"
          width="110"
          :formatter="formatDate"
        />
        
        <el-table-column
          label="操作"
          width="200"
          fixed="right"
        >
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              icon="el-icon-view"
              @click="handleViewDetail(scope.row)"
            >
              详情
            </el-button>
            
            <el-button
              type="text"
              size="small"
              icon="el-icon-edit"
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            
            <el-dropdown @command="handleCommand($event, scope.row)">
              <el-button type="text" size="small">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="copy">复制</el-dropdown-item>
                <el-dropdown-item command="calculate">计算效益</el-dropdown-item>
                <el-dropdown-item command="assess">风险评估</el-dropdown-item>
                <el-dropdown-item command="report">生成报告</el-dropdown-item>
                <el-dropdown-item command="archive">归档</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination-section">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.current"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.size"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
      />
    </div>
  </div>
</template>

<script>
import {
  getPlanningPage,
  batchExportPlannings,
  batchArchivePlannings,
  batchDeletePlannings,
  copyPlanning,
  calculatePlanningBenefit,
  assessPlanningRisk,
  generatePlanningReport,
  getPlanningTypeLabel,
  getPlanningStatusLabel,
  getExecutionStatusLabel,
  getRiskLevelLabel,
  getPriorityLabel,
  getTaxTypeLabel,
  formatAmount,
  formatPercentage,
  PLANNING_TYPES,
  PLANNING_STATUS,
  EXECUTION_STATUS,
  RISK_LEVELS,
  PRIORITIES,
  TAX_TYPES
} from '@/api/managementAccountant/ts/taxPlanning'

export default {
  name: 'TaxPlanningList',
  data() {
    return {
      loading: false,
      
      // 搜索表单
      searchForm: {
        planningCode: '',
        planningName: '',
        planningType: '',
        planningStatus: '',
        executionStatus: '',
        riskLevel: '',
        responsiblePerson: ''
      },
      
      // 表格数据
      tableData: [],
      selectedRows: [],
      
      // 分页
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      
      // 排序
      sortField: '',
      sortOrder: '',
      
      // 选项数据
      planningTypeOptions: {},
      planningStatusOptions: {},
      executionStatusOptions: {},
      riskLevelOptions: {},
      priorityOptions: {},
      taxTypeOptions: {},
      
      // 列设置
      columnSettingVisible: false
    }
  },
  created() {
    this.initOptions()
    this.loadData()
  },
  methods: {
    // 初始化选项数据
    initOptions() {
      // 筹划类型选项
      this.planningTypeOptions = {
        [PLANNING_TYPES.TAX_REDUCTION]: '减税筹划',
        [PLANNING_TYPES.TAX_DEFERRAL]: '延税筹划',
        [PLANNING_TYPES.TAX_EXEMPTION]: '免税筹划',
        [PLANNING_TYPES.TAX_CREDIT]: '税收抵免',
        [PLANNING_TYPES.STRUCTURE_OPTIMIZATION]: '结构优化',
        [PLANNING_TYPES.BUSINESS_RESTRUCTURING]: '业务重组',
        [PLANNING_TYPES.INVESTMENT_PLANNING]: '投资筹划',
        [PLANNING_TYPES.MERGER_ACQUISITION]: '并购筹划',
        [PLANNING_TYPES.INTERNATIONAL_PLANNING]: '国际筹划',
        [PLANNING_TYPES.OTHER]: '其他'
      }
      
      // 筹划状态选项
      this.planningStatusOptions = {
        [PLANNING_STATUS.DRAFT]: '草稿',
        [PLANNING_STATUS.UNDER_REVIEW]: '审核中',
        [PLANNING_STATUS.APPROVED]: '已审批',
        [PLANNING_STATUS.REJECTED]: '已拒绝',
        [PLANNING_STATUS.SUSPENDED]: '已暂停',
        [PLANNING_STATUS.CANCELLED]: '已取消',
        [PLANNING_STATUS.ARCHIVED]: '已归档'
      }
      
      // 执行状态选项
      this.executionStatusOptions = {
        [EXECUTION_STATUS.NOT_STARTED]: '未开始',
        [EXECUTION_STATUS.PREPARING]: '准备中',
        [EXECUTION_STATUS.EXECUTING]: '执行中',
        [EXECUTION_STATUS.PAUSED]: '已暂停',
        [EXECUTION_STATUS.COMPLETED]: '已完成',
        [EXECUTION_STATUS.FAILED]: '执行失败',
        [EXECUTION_STATUS.CANCELLED]: '已取消'
      }
      
      // 风险等级选项
      this.riskLevelOptions = {
        [RISK_LEVELS.LOW]: '低风险',
        [RISK_LEVELS.MEDIUM]: '中风险',
        [RISK_LEVELS.HIGH]: '高风险',
        [RISK_LEVELS.CRITICAL]: '极高风险'
      }
      
      // 优先级选项
      this.priorityOptions = {
        [PRIORITIES.LOW]: '低',
        [PRIORITIES.NORMAL]: '普通',
        [PRIORITIES.HIGH]: '高',
        [PRIORITIES.URGENT]: '紧急'
      }
      
      // 税种选项
      this.taxTypeOptions = {
        [TAX_TYPES.VAT]: '增值税',
        [TAX_TYPES.CORPORATE_INCOME_TAX]: '企业所得税',
        [TAX_TYPES.INDIVIDUAL_INCOME_TAX]: '个人所得税',
        [TAX_TYPES.BUSINESS_TAX]: '营业税',
        [TAX_TYPES.CONSUMPTION_TAX]: '消费税',
        [TAX_TYPES.STAMP_TAX]: '印花税',
        [TAX_TYPES.PROPERTY_TAX]: '房产税',
        [TAX_TYPES.LAND_USE_TAX]: '土地使用税',
        [TAX_TYPES.VEHICLE_TAX]: '车船税',
        [TAX_TYPES.OTHER]: '其他'
      }
    },
    
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const params = {
          current: this.pagination.current,
          size: this.pagination.size,
          ...this.searchForm
        }
        
        if (this.sortField) {
          params.sortField = this.sortField
          params.sortOrder = this.sortOrder
        }
        
        const response = await getPlanningPage(this.$store.getters.tenantId, params)
        if (response.success) {
          this.tableData = response.data.records || []
          this.pagination.total = response.data.total || 0
        } else {
          this.$message.error(response.message || '加载数据失败')
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    
    // 刷新数据
    refreshData() {
      this.loadData()
    },
    
    // 搜索
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },
    
    // 重置搜索
    handleReset() {
      this.searchForm = {
        planningCode: '',
        planningName: '',
        planningType: '',
        planningStatus: '',
        executionStatus: '',
        riskLevel: '',
        responsiblePerson: ''
      }
      this.pagination.current = 1
      this.loadData()
    },
    
    // 选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    
    // 排序变化
    handleSortChange({ column, prop, order }) {
      this.sortField = prop
      this.sortOrder = order === 'ascending' ? 'asc' : 'desc'
      this.loadData()
    },
    
    // 分页大小变化
    handleSizeChange(size) {
      this.pagination.size = size
      this.pagination.current = 1
      this.loadData()
    },
    
    // 当前页变化
    handleCurrentChange(current) {
      this.pagination.current = current
      this.loadData()
    },
    
    // 查看详情
    handleViewDetail(row) {
      this.$emit('view-detail', row)
    },
    
    // 编辑
    handleEdit(row) {
      this.$emit('edit-planning', row)
    },
    
    // 更多操作
    async handleCommand(command, row) {
      switch (command) {
        case 'copy':
          await this.handleCopy(row)
          break
        case 'calculate':
          await this.handleCalculateBenefit(row)
          break
        case 'assess':
          await this.handleAssessRisk(row)
          break
        case 'report':
          await this.handleGenerateReport(row)
          break
        case 'archive':
          await this.handleArchive(row)
          break
        case 'delete':
          await this.handleDelete(row)
          break
      }
    },
    
    // 复制筹划
    async handleCopy(row) {
      try {
        const { value: newName } = await this.$prompt('请输入新筹划名称', '复制筹划', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValue: row.planningName + '_副本'
        })
        
        const response = await copyPlanning(this.$store.getters.tenantId, row.planningId, newName)
        if (response.success) {
          this.$message.success('复制成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '复制失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('复制筹划失败:', error)
          this.$message.error('复制失败')
        }
      }
    },
    
    // 计算效益
    async handleCalculateBenefit(row) {
      try {
        const response = await calculatePlanningBenefit(this.$store.getters.tenantId, row.planningId)
        if (response.success) {
          const data = response.data
          this.$alert(
            `节税金额: ${formatAmount(data.taxSaving)}万元\n净收益: ${formatAmount(data.netBenefit)}万元\n投资回报率: ${formatPercentage(data.roi)}`,
            '效益计算结果',
            { type: 'success' }
          )
        } else {
          this.$message.error(response.message || '计算失败')
        }
      } catch (error) {
        console.error('计算效益失败:', error)
        this.$message.error('计算失败')
      }
    },
    
    // 风险评估
    async handleAssessRisk(row) {
      try {
        const response = await assessPlanningRisk(this.$store.getters.tenantId, row.planningId)
        if (response.success) {
          const data = response.data
          this.$alert(
            `风险等级: ${getRiskLevelLabel(data.riskLevel)}\n风险评分: ${data.riskScore}分\n风险因素: ${data.riskDescription}`,
            '风险评估结果',
            { type: 'warning' }
          )
        } else {
          this.$message.error(response.message || '评估失败')
        }
      } catch (error) {
        console.error('风险评估失败:', error)
        this.$message.error('评估失败')
      }
    },
    
    // 生成报告
    async handleGenerateReport(row) {
      try {
        const response = await generatePlanningReport(this.$store.getters.tenantId, row.planningId)
        if (response.success) {
          this.$message.success('报告生成成功')
          // 这里可以添加下载报告的逻辑
        } else {
          this.$message.error(response.message || '生成失败')
        }
      } catch (error) {
        console.error('生成报告失败:', error)
        this.$message.error('生成失败')
      }
    },
    
    // 归档
    async handleArchive(row) {
      try {
        await this.$confirm('确认归档该筹划吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await batchArchivePlannings(this.$store.getters.tenantId, [row.planningId])
        if (response.success) {
          this.$message.success('归档成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '归档失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('归档失败:', error)
          this.$message.error('归档失败')
        }
      }
    },
    
    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该筹划吗？删除后不可恢复！', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await batchDeletePlannings(this.$store.getters.tenantId, [row.planningId])
        if (response.success) {
          this.$message.success('删除成功')
          this.loadData()
          this.$emit('refresh')
        } else {
          this.$message.error(response.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      }
    },
    
    // 批量导出
    async handleBatchExport() {
      try {
        const planningIds = this.selectedRows.map(row => row.planningId)
        const response = await batchExportPlannings(this.$store.getters.tenantId, planningIds)
        if (response.success) {
          this.$message.success('导出成功')
          // 这里可以添加下载文件的逻辑
        } else {
          this.$message.error(response.message || '导出失败')
        }
      } catch (error) {
        console.error('批量导出失败:', error)
        this.$message.error('导出失败')
      }
    },
    
    // 批量归档
    async handleBatchArchive() {
      try {
        await this.$confirm(`确认归档选中的 ${this.selectedRows.length} 个筹划吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const planningIds = this.selectedRows.map(row => row.planningId)
        const response = await batchArchivePlannings(this.$store.getters.tenantId, planningIds)
        if (response.success) {
          this.$message.success('批量归档成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '批量归档失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量归档失败:', error)
          this.$message.error('批量归档失败')
        }
      }
    },
    
    // 批量删除
    async handleBatchDelete() {
      try {
        await this.$confirm(`确认删除选中的 ${this.selectedRows.length} 个筹划吗？删除后不可恢复！`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const planningIds = this.selectedRows.map(row => row.planningId)
        const response = await batchDeletePlannings(this.$store.getters.tenantId, planningIds)
        if (response.success) {
          this.$message.success('批量删除成功')
          this.loadData()
          this.$emit('refresh')
        } else {
          this.$message.error(response.message || '批量删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量删除失败:', error)
          this.$message.error('批量删除失败')
        }
      }
    },
    
    // 格式化方法
    formatPlanningType(row) {
      return getPlanningTypeLabel(row.planningType)
    },
    
    formatTaxType(row) {
      return getTaxTypeLabel(row.taxType)
    },
    
    formatDate(row, column, cellValue) {
      if (!cellValue) return '-'
      return new Date(cellValue).toLocaleDateString('zh-CN')
    },
    
    // 标签类型方法
    getPlanningStatusTagType(status) {
      const typeMap = {
        'DRAFT': '',
        'UNDER_REVIEW': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'SUSPENDED': 'info',
        'CANCELLED': '',
        'ARCHIVED': 'info'
      }
      return typeMap[status] || ''
    },
    
    getExecutionStatusTagType(status) {
      const typeMap = {
        'NOT_STARTED': '',
        'PREPARING': 'warning',
        'EXECUTING': 'primary',
        'PAUSED': 'warning',
        'COMPLETED': 'success',
        'FAILED': 'danger',
        'CANCELLED': ''
      }
      return typeMap[status] || ''
    },
    
    getRiskLevelTagType(level) {
      const typeMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'danger'
      }
      return typeMap[level] || ''
    },
    
    getPriorityTagType(priority) {
      const typeMap = {
        'LOW': 'info',
        'NORMAL': '',
        'HIGH': 'warning',
        'URGENT': 'danger'
      }
      return typeMap[priority] || ''
    },
    
    getBenefitClass(benefit) {
      if (!benefit) return ''
      return Number(benefit) >= 0 ? 'success' : 'danger'
    },
    
    getRoiClass(roi) {
      if (!roi) return ''
      const value = Number(roi)
      if (value >= 0.2) return 'success'
      if (value >= 0.1) return 'warning'
      return 'danger'
    },
    
    // 工具方法
    getPlanningTypeLabel,
    getPlanningStatusLabel,
    getExecutionStatusLabel,
    getRiskLevelLabel,
    getPriorityLabel,
    formatAmount,
    formatPercentage
  }
}
</script>

<style lang="scss" scoped>
.tax-planning-list {
  .search-section {
    background: white;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 16px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  
  .toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 20px;
    background: white;
    border-radius: 8px;
    margin-bottom: 16px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    
    .toolbar-left {
      .el-button {
        margin-right: 12px;
      }
    }
    
    .toolbar-right {
      .el-button {
        margin-left: 8px;
      }
    }
  }
  
  .table-section {
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    
    .amount-text {
      font-weight: 600;
      
      &.success {
        color: #67C23A;
      }
      
      &.danger {
        color: #F56C6C;
      }
    }
    
    .percentage-text {
      font-weight: 600;
      
      &.success {
        color: #67C23A;
      }
      
      &.warning {
        color: #E6A23C;
      }
      
      &.danger {
        color: #F56C6C;
      }
    }
    
    .progress-text {
      font-size: 12px;
      color: #909399;
      margin-left: 8px;
    }
  }
  
  .pagination-section {
    display: flex;
    justify-content: center;
    padding: 20px;
    background: white;
    border-radius: 8px;
    margin-top: 16px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
}
</style>
