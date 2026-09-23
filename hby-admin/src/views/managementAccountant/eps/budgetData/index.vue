<template>
  <div class="budget-data-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h2>预算数据管理</h2>
        <p>管理和维护预算数据，支持矩阵视图、批量操作、数据计算等功能</p>
      </div>
      <div class="header-right">
        <el-button-group>
          <el-button 
            :type="viewMode === 'list' ? 'primary' : 'default'"
            @click="viewMode = 'list'"
            icon="el-icon-menu">
            列表视图
          </el-button>
          <el-button 
            :type="viewMode === 'matrix' ? 'primary' : 'default'"
            @click="viewMode = 'matrix'"
            icon="el-icon-s-grid">
            矩阵视图
          </el-button>
        </el-button-group>
      </div>
    </div>

    <!-- 查询条件 -->
    <div class="search-container">
      <el-form :model="searchForm" inline size="small">
        <el-form-item label="预算版本">
          <el-select v-model="searchForm.versionId" placeholder="请选择版本" clearable>
            <el-option
              v-for="version in versionOptions"
              :key="version.versionId"
              :label="version.versionName"
              :value="version.versionId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预算科目">
          <el-select v-model="searchForm.subjectId" placeholder="请选择科目" clearable>
            <el-option
              v-for="subject in subjectOptions"
              :key="subject.subjectId"
              :label="subject.subjectName"
              :value="subject.subjectId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="组织">
          <el-select v-model="searchForm.organizationId" placeholder="请选择组织" clearable>
            <el-option
              v-for="org in organizationOptions"
              :key="org.organizationId"
              :label="org.organizationName"
              :value="org.organizationId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="预算期间">
          <el-select v-model="searchForm.budgetPeriod" placeholder="请选择期间" clearable>
            <el-option
              v-for="period in periodOptions"
              :key="period.value"
              :label="period.label"
              :value="period.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数据状态">
          <el-select v-model="searchForm.dataStatus" placeholder="请选择状态" clearable>
            <el-option label="草稿" value="DRAFT"></el-option>
            <el-option label="已提交" value="SUBMITTED"></el-option>
            <el-option label="已审批" value="APPROVED"></el-option>
            <el-option label="已拒绝" value="REJECTED"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch" icon="el-icon-search">查询</el-button>
          <el-button @click="handleReset" icon="el-icon-refresh">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作工具栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <el-button type="primary" @click="handleCreate" icon="el-icon-plus">新增数据</el-button>
        <el-button type="success" @click="handleBatchImport" icon="el-icon-upload2">批量导入</el-button>
        <el-button type="info" @click="handleBatchExport" icon="el-icon-download">批量导出</el-button>
        <el-dropdown @command="handleBatchOperation" v-if="selectedRows.length > 0">
          <el-button type="warning">
            批量操作<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="lock">锁定数据</el-dropdown-item>
            <el-dropdown-item command="unlock">解锁数据</el-dropdown-item>
            <el-dropdown-item command="submit">提交审批</el-dropdown-item>
            <el-dropdown-item command="approve">批量审批</el-dropdown-item>
            <el-dropdown-item command="reject">批量拒绝</el-dropdown-item>
            <el-dropdown-item command="delete" divided>删除数据</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
      <div class="toolbar-right">
        <el-button @click="handleCalculate" icon="el-icon-s-operation">数据计算</el-button>
        <el-button @click="handleValidate" icon="el-icon-circle-check">数据验证</el-button>
        <el-button @click="handleRefresh" icon="el-icon-refresh">刷新</el-button>
      </div>
    </div>

    <!-- 列表视图 -->
    <div v-if="viewMode === 'list'" class="list-view">
      <el-table
        :data="tableData"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
        height="500">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="dataId" label="数据ID" width="80"></el-table-column>
        <el-table-column prop="versionName" label="预算版本" width="120"></el-table-column>
        <el-table-column prop="subjectName" label="预算科目" width="150"></el-table-column>
        <el-table-column prop="organizationName" label="组织" width="120"></el-table-column>
        <el-table-column prop="budgetPeriod" label="预算期间" width="100"></el-table-column>
        <el-table-column prop="budgetAmount" label="预算金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.budgetAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="actualAmount" label="实际金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount-text">{{ formatAmount(scope.row.actualAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="varianceAmount" label="差异金额" width="120" align="right">
          <template slot-scope="scope">
            <span :class="getVarianceClass(scope.row.varianceAmount)">
              {{ formatAmount(scope.row.varianceAmount) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="dataStatus" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.dataStatus)" size="small">
              {{ getStatusText(scope.row.dataStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isLocked" label="锁定" width="80">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isLocked" type="danger" size="mini">已锁定</el-tag>
            <el-tag v-else type="success" size="mini">未锁定</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdTime" label="创建时间" width="150">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.createdTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)" icon="el-icon-view">查看</el-button>
            <el-button 
              size="mini" 
              type="primary" 
              @click="handleEdit(scope.row)"
              :disabled="scope.row.isLocked"
              icon="el-icon-edit">
              编辑
            </el-button>
            <el-dropdown @command="(command) => handleRowOperation(command, scope.row)">
              <el-button size="mini" type="text">
                更多<i class="el-icon-arrow-down"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="copy">复制</el-dropdown-item>
                <el-dropdown-item command="history">历史</el-dropdown-item>
                <el-dropdown-item v-if="!scope.row.isLocked" command="lock">锁定</el-dropdown-item>
                <el-dropdown-item v-if="scope.row.isLocked" command="unlock">解锁</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.current"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total">
        </el-pagination>
      </div>
    </div>

    <!-- 矩阵视图 -->
    <div v-if="viewMode === 'matrix'" class="matrix-view">
      <div class="matrix-toolbar">
        <el-button @click="loadMatrixData" icon="el-icon-refresh">刷新矩阵</el-button>
        <el-button @click="exportMatrix" icon="el-icon-download">导出矩阵</el-button>
      </div>
      <div class="matrix-container" v-loading="matrixLoading">
        <div v-if="matrixData && matrixData.length > 0" class="matrix-table">
          <!-- 矩阵表格将在这里渲染 -->
          <p class="matrix-placeholder">矩阵视图开发中...</p>
        </div>
        <div v-else class="matrix-empty">
          <el-empty description="暂无矩阵数据"></el-empty>
        </div>
      </div>
    </div>

    <!-- 数据表单对话框 -->
    <BudgetDataForm
      :visible.sync="formVisible"
      :form-data="currentFormData"
      :form-mode="formMode"
      @submit="handleFormSubmit"
      @cancel="handleFormCancel">
    </BudgetDataForm>

    <!-- 批量导入对话框 -->
    <BudgetDataImport
      :visible.sync="importVisible"
      @success="handleImportSuccess">
    </BudgetDataImport>

    <!-- 数据计算对话框 -->
    <BudgetDataCalculate
      :visible.sync="calculateVisible"
      :selected-data="selectedRows"
      @success="handleCalculateSuccess">
    </BudgetDataCalculate>

    <!-- 数据验证对话框 -->
    <BudgetDataValidate
      :visible.sync="validateVisible"
      :selected-data="selectedRows"
      @success="handleValidateSuccess">
    </BudgetDataValidate>
  </div>
</template>

<script>
import { 
  queryBudgetDataPage, 
  deleteBudgetData, 
  batchDeleteBudgetData,
  getBudgetDataMatrix,
  lockBudgetData,
  unlockBudgetData,
  batchOperateBudgetData,
  budgetDataUtils
} from '@/api/managementAccountant/eps/budgetData'
import BudgetDataForm from './components/BudgetDataForm'
import BudgetDataImport from './components/BudgetDataImport'
import BudgetDataCalculate from './components/BudgetDataCalculate'
import BudgetDataValidate from './components/BudgetDataValidate'

export default {
  name: 'BudgetData',
  components: {
    BudgetDataForm,
    BudgetDataImport,
    BudgetDataCalculate,
    BudgetDataValidate
  },
  data() {
    return {
      // 视图模式
      viewMode: 'list', // list | matrix
      
      // 加载状态
      loading: false,
      matrixLoading: false,
      
      // 搜索表单
      searchForm: {
        versionId: null,
        subjectId: null,
        organizationId: null,
        budgetPeriod: null,
        dataStatus: null
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
      
      // 矩阵数据
      matrixData: [],
      
      // 选项数据
      versionOptions: [],
      subjectOptions: [],
      organizationOptions: [],
      periodOptions: [
        { label: '2024年1月', value: '2024-01' },
        { label: '2024年2月', value: '2024-02' },
        { label: '2024年3月', value: '2024-03' },
        { label: '2024年4月', value: '2024-04' },
        { label: '2024年5月', value: '2024-05' },
        { label: '2024年6月', value: '2024-06' },
        { label: '2024年7月', value: '2024-07' },
        { label: '2024年8月', value: '2024-08' },
        { label: '2024年9月', value: '2024-09' },
        { label: '2024年10月', value: '2024-10' },
        { label: '2024年11月', value: '2024-11' },
        { label: '2024年12月', value: '2024-12' }
      ],
      
      // 对话框状态
      formVisible: false,
      importVisible: false,
      calculateVisible: false,
      validateVisible: false,
      
      // 表单数据
      currentFormData: {},
      formMode: 'create' // create | edit | view
    }
  },
  created() {
    this.loadData()
    this.loadOptions()
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const params = {
          current: this.pagination.current,
          size: this.pagination.size,
          ...this.searchForm
        }
        const response = await queryBudgetDataPage(params)
        if (response.success) {
          this.tableData = response.data.records || []
          this.pagination.total = response.data.total || 0
        }
      } catch (error) {
        this.$message.error('加载数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    
    // 加载选项数据
    loadOptions() {
      // TODO: 加载版本、科目、组织选项
      this.versionOptions = []
      this.subjectOptions = []
      this.organizationOptions = []
    },
    
    // 加载矩阵数据
    async loadMatrixData() {
      this.matrixLoading = true
      try {
        const params = {
          versionId: this.searchForm.versionId,
          organizationId: this.searchForm.organizationId,
          budgetPeriod: this.searchForm.budgetPeriod
        }
        const response = await getBudgetDataMatrix(params)
        if (response.success) {
          this.matrixData = response.data.matrixData || []
        }
      } catch (error) {
        this.$message.error('加载矩阵数据失败：' + error.message)
      } finally {
        this.matrixLoading = false
      }
    },
    
    // 搜索
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },
    
    // 重置
    handleReset() {
      this.searchForm = {
        versionId: null,
        subjectId: null,
        organizationId: null,
        budgetPeriod: null,
        dataStatus: null
      }
      this.handleSearch()
    },
    
    // 刷新
    handleRefresh() {
      this.loadData()
    },
    
    // 新增
    handleCreate() {
      this.currentFormData = {}
      this.formMode = 'create'
      this.formVisible = true
    },
    
    // 查看
    handleView(row) {
      this.currentFormData = { ...row }
      this.formMode = 'view'
      this.formVisible = true
    },
    
    // 编辑
    handleEdit(row) {
      this.currentFormData = { ...row }
      this.formMode = 'edit'
      this.formVisible = true
    },
    
    // 表格选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    
    // 分页大小变化
    handleSizeChange(size) {
      this.pagination.size = size
      this.loadData()
    },
    
    // 当前页变化
    handleCurrentChange(current) {
      this.pagination.current = current
      this.loadData()
    },
    
    // 表单提交
    handleFormSubmit() {
      this.formVisible = false
      this.loadData()
      this.$message.success('操作成功')
    },
    
    // 表单取消
    handleFormCancel() {
      this.formVisible = false
    },
    
    // 批量导入
    handleBatchImport() {
      this.importVisible = true
    },
    
    // 导入成功
    handleImportSuccess() {
      this.importVisible = false
      this.loadData()
      this.$message.success('导入成功')
    },
    
    // 批量导出
    handleBatchExport() {
      this.$message.info('导出功能开发中...')
    },
    
    // 数据计算
    handleCalculate() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请先选择要计算的数据')
        return
      }
      this.calculateVisible = true
    },
    
    // 计算成功
    handleCalculateSuccess() {
      this.calculateVisible = false
      this.loadData()
      this.$message.success('计算完成')
    },
    
    // 数据验证
    handleValidate() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请先选择要验证的数据')
        return
      }
      this.validateVisible = true
    },
    
    // 验证成功
    handleValidateSuccess() {
      this.validateVisible = false
      this.$message.success('验证完成')
    },
    
    // 批量操作
    async handleBatchOperation(command) {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请先选择要操作的数据')
        return
      }
      
      const dataIds = this.selectedRows.map(row => row.dataId)
      
      try {
        let result
        switch (command) {
          case 'lock':
            result = await batchOperateBudgetData({
              operation: 'LOCK',
              dataIds
            })
            break
          case 'unlock':
            result = await batchOperateBudgetData({
              operation: 'UNLOCK',
              dataIds
            })
            break
          case 'submit':
            result = await batchOperateBudgetData({
              operation: 'SUBMIT',
              dataIds
            })
            break
          case 'approve':
            result = await batchOperateBudgetData({
              operation: 'APPROVE',
              dataIds
            })
            break
          case 'reject':
            result = await batchOperateBudgetData({
              operation: 'REJECT',
              dataIds
            })
            break
          case 'delete':
            await this.$confirm('确认删除选中的数据吗？', '提示', {
              type: 'warning'
            })
            result = await batchDeleteBudgetData(dataIds)
            break
        }
        
        if (result && result.success) {
          this.$message.success('操作成功')
          this.loadData()
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('操作失败：' + error.message)
        }
      }
    },
    
    // 行操作
    async handleRowOperation(command, row) {
      try {
        switch (command) {
          case 'copy':
            this.currentFormData = { ...row, dataId: null }
            this.formMode = 'create'
            this.formVisible = true
            break
          case 'history':
            this.$message.info('历史记录功能开发中...')
            break
          case 'lock':
            await lockBudgetData(row.dataId)
            this.$message.success('锁定成功')
            this.loadData()
            break
          case 'unlock':
            await unlockBudgetData(row.dataId)
            this.$message.success('解锁成功')
            this.loadData()
            break
          case 'delete':
            await this.$confirm('确认删除该数据吗？', '提示', {
              type: 'warning'
            })
            await deleteBudgetData(row.dataId)
            this.$message.success('删除成功')
            this.loadData()
            break
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('操作失败：' + error.message)
        }
      }
    },
    
    // 导出矩阵
    exportMatrix() {
      this.$message.info('矩阵导出功能开发中...')
    },
    
    // 格式化金额
    formatAmount(amount) {
      return budgetDataUtils.formatAmount(amount)
    },
    
    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return ''
      return new Date(dateTime).toLocaleString('zh-CN')
    },
    
    // 获取差异样式类
    getVarianceClass(variance) {
      if (!variance) return 'amount-text'
      return variance > 0 ? 'amount-positive' : 'amount-negative'
    },
    
    // 获取状态类型
    getStatusType(status) {
      const typeMap = {
        'DRAFT': 'info',
        'SUBMITTED': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger'
      }
      return typeMap[status] || 'info'
    },
    
    // 获取状态文本
    getStatusText(status) {
      return budgetDataUtils.getStatusText(status)
    }
  }
}
</script>

<style scoped>
.budget-data-container {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e4e7ed;
}

.header-left h2 {
  margin: 0 0 8px 0;
  color: #303133;
}

.header-left p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.search-container {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.list-view {
  background: white;
  border-radius: 4px;
}

.matrix-view {
  background: white;
  border-radius: 4px;
  padding: 20px;
}

.matrix-toolbar {
  margin-bottom: 20px;
}

.matrix-container {
  min-height: 400px;
}

.matrix-placeholder {
  text-align: center;
  color: #909399;
  font-size: 16px;
  padding: 100px 0;
}

.pagination-container {
  padding: 20px;
  text-align: right;
}

.amount-text {
  color: #303133;
}

.amount-positive {
  color: #67c23a;
}

.amount-negative {
  color: #f56c6c;
}
</style>
