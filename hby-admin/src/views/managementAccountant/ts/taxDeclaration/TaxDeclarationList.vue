<template>
  <div class="tax-declaration-list">
    <!-- 搜索筛选区域 -->
    <div class="search-section">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="申报编号">
          <el-input
            v-model="searchForm.declarationCode"
            placeholder="请输入申报编号"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        
        <el-form-item label="申报名称">
          <el-input
            v-model="searchForm.declarationName"
            placeholder="请输入申报名称"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        
        <el-form-item label="税种">
          <el-select
            v-model="searchForm.taxType"
            placeholder="请选择税种"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="(label, value) in TAX_TYPE_LABELS"
              :key="value"
              :label="label"
              :value="value"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="申报类型">
          <el-select
            v-model="searchForm.declarationType"
            placeholder="请选择申报类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="(label, value) in DECLARATION_TYPE_LABELS"
              :key="value"
              :label="label"
              :value="value"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="申报状态">
          <el-select
            v-model="searchForm.declarationStatus"
            placeholder="请选择申报状态"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="(label, value) in DECLARATION_STATUS_LABELS"
              :key="value"
              :label="label"
              :value="value"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="纳税人名称">
          <el-input
            v-model="searchForm.taxpayerName"
            placeholder="请输入纳税人名称"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        
        <el-form-item label="申报期间">
          <el-input
            v-model="searchForm.declarationPeriod"
            placeholder="如：2024-01"
            clearable
            style="width: 120px"
          />
        </el-form-item>
        
        <el-form-item label="创建时间">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 300px"
            value-format="yyyy-MM-dd HH:mm:ss"
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
          @click="$emit('create-declaration')"
        >
          新建申报
        </el-button>
        
        <el-button
          type="success"
          icon="el-icon-upload2"
          size="small"
          :disabled="selectedRows.length === 0"
          @click="handleBatchSubmit"
        >
          批量提交
        </el-button>
        
        <el-button
          type="warning"
          icon="el-icon-edit"
          size="small"
          :disabled="selectedRows.length === 0"
          @click="handleBatchUpdateStatus"
        >
          批量更新状态
        </el-button>
        
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="small"
          :disabled="selectedRows.length === 0"
          @click="handleBatchDelete"
        >
          批量删除
        </el-button>
      </div>
      
      <div class="toolbar-right">
        <el-button
          type="info"
          icon="el-icon-download"
          size="small"
          :disabled="selectedRows.length === 0"
          @click="handleExport"
        >
          导出数据
        </el-button>
        
        <el-button
          icon="el-icon-refresh"
          size="small"
          @click="loadData"
        >
          刷新
        </el-button>
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
        @row-click="handleRowClick"
      >
        <el-table-column type="selection" width="50" align="center" />
        
        <el-table-column prop="declarationCode" label="申报编号" width="140" fixed="left">
          <template slot-scope="scope">
            <el-link type="primary" @click="handleViewDetail(scope.row)">
              {{ scope.row.declarationCode }}
            </el-link>
          </template>
        </el-table-column>
        
        <el-table-column prop="declarationName" label="申报名称" width="200" show-overflow-tooltip />
        
        <el-table-column prop="taxType" label="税种" width="100" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" type="info">
              {{ getTaxTypeLabel(scope.row.taxType) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="declarationType" label="申报类型" width="100" align="center">
          <template slot-scope="scope">
            {{ getDeclarationTypeLabel(scope.row.declarationType) }}
          </template>
        </el-table-column>
        
        <el-table-column prop="declarationStatus" label="申报状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag
              size="mini"
              :type="getStatusTagType(scope.row.declarationStatus)"
            >
              {{ getDeclarationStatusLabel(scope.row.declarationStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="taxpayerName" label="纳税人名称" width="180" show-overflow-tooltip />
        
        <el-table-column prop="declarationPeriod" label="申报期间" width="100" align="center" />
        
        <el-table-column prop="taxAmount" label="申报税额" width="120" align="right">
          <template slot-scope="scope">
            <span v-if="scope.row.taxAmount">
              ¥{{ formatTaxAmount(scope.row.taxAmount) }}
            </span>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="deadline" label="申报截止日期" width="150" align="center">
          <template slot-scope="scope">
            <span
              v-if="scope.row.deadline"
              :class="{
                'text-danger': isDeclarationOverdue(scope.row.deadline, scope.row.declarationStatus),
                'text-warning': isDeclarationUpcoming(scope.row.deadline)
              }"
            >
              {{ formatDate(scope.row.deadline) }}
            </span>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="submitTime" label="提交时间" width="150" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.submitTime">
              {{ formatDate(scope.row.submitTime) }}
            </span>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        
        <el-table-column prop="priority" label="优先级" width="80" align="center">
          <template slot-scope="scope">
            <el-tag
              size="mini"
              :type="getPriorityTagType(scope.row.priority)"
            >
              {{ getPriorityLabel(scope.row.priority) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="createdTime" label="创建时间" width="150" align="center">
          <template slot-scope="scope">
            {{ formatDate(scope.row.createdTime) }}
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="200" fixed="right" align="center">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="mini"
              icon="el-icon-view"
              @click="handleViewDetail(scope.row)"
            >
              详情
            </el-button>
            
            <el-button
              v-if="canEdit(scope.row.declarationStatus)"
              type="text"
              size="mini"
              icon="el-icon-edit"
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            
            <el-button
              v-if="canSubmit(scope.row.declarationStatus)"
              type="text"
              size="mini"
              icon="el-icon-upload2"
              @click="handleSubmit(scope.row)"
            >
              提交
            </el-button>
            
            <el-button
              v-if="canWithdraw(scope.row.declarationStatus)"
              type="text"
              size="mini"
              icon="el-icon-download"
              @click="handleWithdraw(scope.row)"
            >
              撤回
            </el-button>
            
            <el-dropdown
              trigger="click"
              @command="(command) => handleMoreAction(command, scope.row)"
            >
              <el-button type="text" size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="calculate">计算税额</el-dropdown-item>
                <el-dropdown-item command="validate">验证数据</el-dropdown-item>
                <el-dropdown-item command="progress">查看进度</el-dropdown-item>
                <el-dropdown-item command="history">查看历史</el-dropdown-item>
                <el-dropdown-item command="logs">查看日志</el-dropdown-item>
                <el-dropdown-item command="reminder">发送提醒</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页组件 -->
    <div class="pagination-section">
      <el-pagination
        :current-page="pagination.current"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.size"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 批量操作对话框 -->
    <el-dialog
      title="批量更新状态"
      :visible.sync="showBatchStatusDialog"
      width="400px"
    >
      <el-form label-width="80px">
        <el-form-item label="新状态">
          <el-select v-model="batchStatus" placeholder="请选择状态" style="width: 100%">
            <el-option
              v-for="(label, value) in DECLARATION_STATUS_LABELS"
              :key="value"
              :label="label"
              :value="value"
            />
          </el-select>
        </el-form-item>
      </el-form>
      
      <div slot="footer" class="dialog-footer">
        <el-button @click="showBatchStatusDialog = false">取消</el-button>
        <el-button type="primary" @click="confirmBatchUpdateStatus">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getDeclarationPage,
  submitDeclaration,
  withdrawDeclaration,
  calculateTaxAmount,
  validateDeclarationData,
  sendDeclarationReminder,
  deleteDeclaration,
  batchSubmitDeclarations,
  batchUpdateStatus,
  batchDeleteDeclarations,
  batchExportDeclarations,
  TAX_TYPE_LABELS,
  DECLARATION_TYPE_LABELS,
  DECLARATION_STATUS_LABELS,
  PRIORITY_LABELS,
  getTaxTypeLabel,
  getDeclarationTypeLabel,
  getDeclarationStatusLabel,
  getPriorityLabel,
  formatTaxAmount,
  isDeclarationOverdue,
  isDeclarationUpcoming,
  getDeclarationStatusColor
} from '@/api/managementAccountant/ts/taxDeclaration'

export default {
  name: 'TaxDeclarationList',
  data() {
    return {
      // 常量
      TAX_TYPE_LABELS,
      DECLARATION_TYPE_LABELS,
      DECLARATION_STATUS_LABELS,
      PRIORITY_LABELS,
      
      // 表格数据
      tableData: [],
      loading: false,
      selectedRows: [],
      
      // 搜索表单
      searchForm: {
        tenantId: this.$store.getters.tenantId,
        declarationCode: '',
        declarationName: '',
        taxType: '',
        declarationType: '',
        declarationStatus: '',
        taxpayerName: '',
        declarationPeriod: '',
        dateRange: null
      },
      
      // 分页信息
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      
      // 批量操作
      showBatchStatusDialog: false,
      batchStatus: ''
    }
  },
  created() {
    this.loadData()
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
        
        // 处理日期范围
        if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
          params.startDate = this.searchForm.dateRange[0]
          params.endDate = this.searchForm.dateRange[1]
        }
        delete params.dateRange
        
        const response = await getDeclarationPage(params)
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
    
    // 搜索
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },
    
    // 重置搜索
    handleReset() {
      this.searchForm = {
        tenantId: this.$store.getters.tenantId,
        declarationCode: '',
        declarationName: '',
        taxType: '',
        declarationType: '',
        declarationStatus: '',
        taxpayerName: '',
        declarationPeriod: '',
        dateRange: null
      }
      this.pagination.current = 1
      this.loadData()
    },
    
    // 分页大小改变
    handleSizeChange(size) {
      this.pagination.size = size
      this.pagination.current = 1
      this.loadData()
    },
    
    // 当前页改变
    handleCurrentChange(current) {
      this.pagination.current = current
      this.loadData()
    },
    
    // 选择改变
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    
    // 行点击
    handleRowClick(row) {
      this.$refs.dataTable.toggleRowSelection(row)
    },
    
    // 查看详情
    handleViewDetail(row) {
      this.$emit('view-detail', row)
    },
    
    // 编辑
    handleEdit(row) {
      this.$emit('edit-declaration', row)
    },
    
    // 提交申报
    async handleSubmit(row) {
      try {
        await this.$confirm('确认提交该申报吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await submitDeclaration(row.tenantId, row.declarationId)
        if (response.success) {
          this.$message.success('提交申报成功')
          this.loadData()
          this.$emit('refresh-overview')
        } else {
          this.$message.error(response.message || '提交申报失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('提交申报失败:', error)
          this.$message.error('提交申报失败')
        }
      }
    },
    
    // 撤回申报
    async handleWithdraw(row) {
      try {
        await this.$confirm('确认撤回该申报吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await withdrawDeclaration(row.tenantId, row.declarationId)
        if (response.success) {
          this.$message.success('撤回申报成功')
          this.loadData()
          this.$emit('refresh-overview')
        } else {
          this.$message.error(response.message || '撤回申报失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('撤回申报失败:', error)
          this.$message.error('撤回申报失败')
        }
      }
    },
    
    // 更多操作
    async handleMoreAction(command, row) {
      switch (command) {
        case 'calculate':
          await this.handleCalculateTax(row)
          break
        case 'validate':
          await this.handleValidateData(row)
          break
        case 'progress':
          this.handleViewProgress(row)
          break
        case 'history':
          this.handleViewHistory(row)
          break
        case 'logs':
          this.handleViewLogs(row)
          break
        case 'reminder':
          await this.handleSendReminder(row)
          break
        case 'delete':
          await this.handleDelete(row)
          break
      }
    },
    
    // 计算税额
    async handleCalculateTax(row) {
      try {
        const response = await calculateTaxAmount(row.tenantId, row.declarationId)
        if (response.success) {
          this.$message.success('税额计算完成')
          this.loadData()
        } else {
          this.$message.error(response.message || '税额计算失败')
        }
      } catch (error) {
        console.error('税额计算失败:', error)
        this.$message.error('税额计算失败')
      }
    },
    
    // 验证数据
    async handleValidateData(row) {
      try {
        const response = await validateDeclarationData(row.tenantId, row.declarationId)
        if (response.success) {
          const result = response.data
          if (result.valid) {
            this.$message.success('数据验证通过')
          } else {
            this.$message.warning(`数据验证失败：${result.message}`)
          }
        } else {
          this.$message.error(response.message || '数据验证失败')
        }
      } catch (error) {
        console.error('数据验证失败:', error)
        this.$message.error('数据验证失败')
      }
    },
    
    // 查看进度
    handleViewProgress(row) {
      // 实现查看进度逻辑
      this.$message.info('查看进度功能开发中')
    },
    
    // 查看历史
    handleViewHistory(row) {
      // 实现查看历史逻辑
      this.$message.info('查看历史功能开发中')
    },
    
    // 查看日志
    handleViewLogs(row) {
      // 实现查看日志逻辑
      this.$message.info('查看日志功能开发中')
    },
    
    // 发送提醒
    async handleSendReminder(row) {
      try {
        const response = await sendDeclarationReminder(row.tenantId, row.declarationId)
        if (response.success) {
          this.$message.success('提醒发送成功')
        } else {
          this.$message.error(response.message || '提醒发送失败')
        }
      } catch (error) {
        console.error('提醒发送失败:', error)
        this.$message.error('提醒发送失败')
      }
    },
    
    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该申报吗？删除后无法恢复！', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'error'
        })
        
        const response = await deleteDeclaration(row.tenantId, row.declarationId)
        if (response.success) {
          this.$message.success('删除成功')
          this.loadData()
          this.$emit('refresh-overview')
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
    
    // 批量提交
    async handleBatchSubmit() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要提交的申报')
        return
      }
      
      try {
        await this.$confirm(`确认提交选中的${this.selectedRows.length}个申报吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const declarationIds = this.selectedRows.map(row => row.declarationId)
        const response = await batchSubmitDeclarations(this.$store.getters.tenantId, declarationIds)
        
        if (response.success) {
          const result = response.data
          this.$message.success(`批量提交完成，成功：${result.successCount}，失败：${result.failCount}`)
          this.loadData()
          this.$emit('refresh-overview')
        } else {
          this.$message.error(response.message || '批量提交失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('批量提交失败:', error)
          this.$message.error('批量提交失败')
        }
      }
    },
    
    // 批量更新状态
    handleBatchUpdateStatus() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要更新的申报')
        return
      }
      this.showBatchStatusDialog = true
    },
    
    // 确认批量更新状态
    async confirmBatchUpdateStatus() {
      if (!this.batchStatus) {
        this.$message.warning('请选择状态')
        return
      }
      
      try {
        const declarationIds = this.selectedRows.map(row => row.declarationId)
        const response = await batchUpdateStatus(this.$store.getters.tenantId, declarationIds, this.batchStatus)
        
        if (response.success) {
          const result = response.data
          this.$message.success(`批量更新完成，成功：${result.successCount}，失败：${result.failCount}`)
          this.showBatchStatusDialog = false
          this.batchStatus = ''
          this.loadData()
          this.$emit('refresh-overview')
        } else {
          this.$message.error(response.message || '批量更新失败')
        }
      } catch (error) {
        console.error('批量更新失败:', error)
        this.$message.error('批量更新失败')
      }
    },
    
    // 批量删除
    async handleBatchDelete() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要删除的申报')
        return
      }
      
      try {
        await this.$confirm(`确认删除选中的${this.selectedRows.length}个申报吗？删除后无法恢复！`, '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'error'
        })
        
        const declarationIds = this.selectedRows.map(row => row.declarationId)
        const response = await batchDeleteDeclarations(this.$store.getters.tenantId, declarationIds)
        
        if (response.success) {
          const result = response.data
          this.$message.success(`批量删除完成，成功：${result.successCount}，失败：${result.failCount}`)
          this.loadData()
          this.$emit('refresh-overview')
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
    
    // 导出数据
    async handleExport() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要导出的申报')
        return
      }
      
      try {
        const declarationIds = this.selectedRows.map(row => row.declarationId)
        const response = await batchExportDeclarations(this.$store.getters.tenantId, declarationIds)
        
        if (response.success) {
          // 这里可以实现具体的导出逻辑，比如下载Excel文件
          this.$message.success('导出成功')
        } else {
          this.$message.error(response.message || '导出失败')
        }
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      }
    },
    
    // 状态判断方法
    canEdit(status) {
      return ['DRAFT', 'FILL_FAILED'].includes(status)
    },
    
    canSubmit(status) {
      return ['DRAFT', 'FILLED', 'SUBMIT_FAILED'].includes(status)
    },
    
    canWithdraw(status) {
      return ['SUBMITTED', 'UNDER_REVIEW'].includes(status)
    },
    
    // 获取状态标签类型
    getStatusTagType(status) {
      const typeMap = {
        'DRAFT': '',
        'FILLING': 'warning',
        'FILLED': 'info',
        'SUBMITTING': 'warning',
        'SUBMITTED': 'info',
        'UNDER_REVIEW': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'COMPLETED': 'success',
        'WITHDRAWN': '',
        'FILL_FAILED': 'danger',
        'SUBMIT_FAILED': 'danger'
      }
      return typeMap[status] || ''
    },
    
    // 获取优先级标签类型
    getPriorityTagType(priority) {
      const typeMap = {
        'LOW': 'info',
        'NORMAL': '',
        'HIGH': 'warning',
        'URGENT': 'danger'
      }
      return typeMap[priority] || ''
    },
    
    // 工具方法
    getTaxTypeLabel,
    getDeclarationTypeLabel,
    getDeclarationStatusLabel,
    getPriorityLabel,
    formatTaxAmount,
    isDeclarationOverdue,
    isDeclarationUpcoming,
    
    formatDate(date) {
      if (!date) return '-'
      return new Date(date).toLocaleString('zh-CN')
    }
  }
}
</script>

<style lang="scss" scoped>
.tax-declaration-list {
  .search-section {
    padding: 20px;
    background: #f8f9fa;
    border-radius: 4px;
    margin-bottom: 16px;
  }
  
  .toolbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 0;
    border-bottom: 1px solid #EBEEF5;
    margin-bottom: 16px;
  }
  
  .table-section {
    margin-bottom: 16px;
  }
  
  .pagination-section {
    text-align: right;
    padding: 16px 0;
  }
  
  .text-muted {
    color: #909399;
  }
  
  .text-danger {
    color: #F56C6C;
  }
  
  .text-warning {
    color: #E6A23C;
  }
}
</style>
