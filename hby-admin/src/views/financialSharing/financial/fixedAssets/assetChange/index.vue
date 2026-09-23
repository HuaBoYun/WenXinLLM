<template>
  <div class="asset-change-container">
    <div class="page-header">
      <h2>资产变动管理</h2>
      <p>管理固定资产的购置、转移、改良、维修等各类变动业务</p>
    </div>
    
    <!-- 变动统计卡片 -->
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon purchase">
              <i class="el-icon-plus"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.purchaseCount }}</div>
              <div class="stat-label">本月购置</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon transfer">
              <i class="el-icon-sort"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.transferCount }}</div>
              <div class="stat-label">本月调拨</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon improvement">
              <i class="el-icon-upload2"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.improvementCount }}</div>
              <div class="stat-label">本月改良</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon pending">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.pendingCount }}</div>
              <div class="stat-label">待审批</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="变动单号" prop="changeNumber">
          <el-input
            v-model="searchForm.changeNumber"
            placeholder="请输入变动单号"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="资产编码" prop="assetCode">
          <el-input
            v-model="searchForm.assetCode"
            placeholder="请输入资产编码"
            clearable
            style="width: 180px"
          />
        </el-form-item>
        <el-form-item label="变动类型" prop="changeType">
          <el-select
            v-model="searchForm.changeType"
            placeholder="请选择变动类型"
            clearable
            style="width: 150px"
          >
            <el-option label="购置" value="PURCHASE" />
            <el-option label="调拨" value="TRANSFER" />
            <el-option label="改良" value="IMPROVEMENT" />
            <el-option label="维修" value="MAINTENANCE" />
            <el-option label="升级" value="UPGRADE" />
          </el-select>
        </el-form-item>
        <el-form-item label="审批状态" prop="status">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="待审批" value="PENDING" />
            <el-option label="已审批" value="APPROVED" />
            <el-option label="已拒绝" value="REJECTED" />
            <el-option label="已完成" value="COMPLETED" />
          </el-select>
        </el-form-item>
        <el-form-item label="变动日期" prop="changeDateRange">
          <el-date-picker
            v-model="searchForm.changeDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 工具栏 -->
    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">新增变动</el-button>
      <el-button type="success" @click="handleBatchApprove" :disabled="!multipleSelection.length">
        批量审批
      </el-button>
      <el-button type="warning" @click="handleExport">导出</el-button>
      <el-button type="info" @click="handleImport">导入</el-button>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="changeNumber" label="变动单号" width="140" />
        <el-table-column prop="assetCode" label="资产编码" width="120" />
        <el-table-column prop="assetName" label="资产名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="changeType" label="变动类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getChangeTypeTagType(scope.row.changeType)" size="small">
              {{ getChangeTypeText(scope.row.changeType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="changeAmount" label="变动金额" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount" :class="{ 'positive': scope.row.changeAmount > 0, 'negative': scope.row.changeAmount < 0 }">
              {{ formatAmount(scope.row.changeAmount) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="changeDate" label="变动日期" width="120" />
        <el-table-column prop="fromDepartment" label="原部门" width="120" show-overflow-tooltip />
        <el-table-column prop="toDepartment" label="目标部门" width="120" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="90">
          <template slot-scope="scope">
            <el-tag :type="getStatusTagType(scope.row.status)" size="small">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operator" label="操作人" width="100" />
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">
              查看
            </el-button>
            <el-button
              v-if="scope.row.status === 'PENDING'"
              type="text"
              size="small"
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              v-if="scope.row.status === 'PENDING'"
              type="text"
              size="small"
              @click="handleApprove(scope.row)"
            >
              审批
            </el-button>
            <el-button
              v-if="scope.row.status === 'APPROVED'"
              type="text"
              size="small"
              @click="handleExecute(scope.row)"
            >
              执行
            </el-button>
            <el-button
              v-if="scope.row.status === 'PENDING'"
              type="text"
              size="small"
              class="danger-text"
              @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        />
      </div>
    </div>

    <!-- 变动详情对话框 -->
    <el-dialog
      title="资产变动详情"
      :visible.sync="detailDialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <div v-if="currentChange" class="change-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="变动单号">{{ currentChange.changeNumber }}</el-descriptions-item>
          <el-descriptions-item label="变动类型">
            <el-tag :type="getChangeTypeTagType(currentChange.changeType)" size="small">
              {{ getChangeTypeText(currentChange.changeType) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="资产编码">{{ currentChange.assetCode }}</el-descriptions-item>
          <el-descriptions-item label="资产名称">{{ currentChange.assetName }}</el-descriptions-item>
          <el-descriptions-item label="变动金额">
            <span class="amount" :class="{ 'positive': currentChange.changeAmount > 0, 'negative': currentChange.changeAmount < 0 }">
              {{ formatAmount(currentChange.changeAmount) }}
            </span>
          </el-descriptions-item>
          <el-descriptions-item label="变动日期">{{ currentChange.changeDate }}</el-descriptions-item>
          <el-descriptions-item label="原部门">{{ currentChange.fromDepartment }}</el-descriptions-item>
          <el-descriptions-item label="目标部门">{{ currentChange.toDepartment }}</el-descriptions-item>
          <el-descriptions-item label="变动原因" :span="2">{{ currentChange.changeReason }}</el-descriptions-item>
          <el-descriptions-item label="操作人">{{ currentChange.operator }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusTagType(currentChange.status)" size="small">
              {{ getStatusText(currentChange.status) }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 资产变动表单对话框 -->
    <asset-change-form-dialog
      :visible.sync="formDialogVisible"
      :mode="dialogMode"
      :change-data="currentChangeData"
      @submit="handleFormSubmit"
    />

    <!-- 审批对话框 -->
    <approval-dialog
      :visible.sync="approvalDialogVisible"
      :approval-data="currentApprovalData"
      @submit="handleApprovalSubmit"
    />

    <!-- 批量审批对话框 -->
    <batch-approval-dialog
      :visible.sync="batchApprovalDialogVisible"
      :selected-items="multipleSelection"
      @submit="handleBatchApprovalSubmit"
    />
  </div>
</template>

<script>
import AssetChangeFormDialog from './components/AssetChangeFormDialog.vue'
import ApprovalDialog from '../components/ApprovalDialog.vue'
import BatchApprovalDialog from '../components/BatchApprovalDialog.vue'

export default {
  name: 'AssetChange',
  components: {
    AssetChangeFormDialog,
    ApprovalDialog,
    BatchApprovalDialog
  },
  data() {
    return {
      loading: false,
      tableData: [],
      multipleSelection: [],
      detailDialogVisible: false,
      currentChange: null,
      formDialogVisible: false,
      dialogMode: 'add',
      currentChangeData: {},
      approvalDialogVisible: false,
      currentApprovalData: {},
      batchApprovalDialogVisible: false,
      searchForm: {
        changeNumber: '',
        assetCode: '',
        changeType: '',
        status: '',
        changeDateRange: []
      },
      pagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      stats: {
        purchaseCount: 0,
        transferCount: 0,
        improvementCount: 0,
        pendingCount: 0
      }
    }
  },
  mounted() {
    this.loadData()
    this.loadStats()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        // 暂未对接 API，先以空状态展示
        this.tableData = []
        this.pagination.total = 0
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    async loadStats() {
      try {
        // 暂未对接 API，先以空状态展示
        this.stats = {
          purchaseCount: 0,
          transferCount: 0,
          improvementCount: 0,
          pendingCount: 0
        }
      } catch (error) {
        console.error('加载统计数据失败：', error)
      }
    },

    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },

    handleReset() {
      this.$refs.searchForm.resetFields()
      this.handleSearch()
    },

    handleAdd() {
      this.dialogMode = 'add'
      this.currentChangeData = {}
      this.formDialogVisible = true
    },

    handleView(row) {
      this.currentChange = row
      this.detailDialogVisible = true
    },

    handleEdit(row) {
      this.dialogMode = 'edit'
      this.currentChangeData = { ...row }
      this.formDialogVisible = true
    },

    async handleFormSubmit(formData) {
      try {
        // TODO: 调用API提交表单
        await new Promise(resolve => setTimeout(resolve, 1000))
        this.$message.success(this.dialogMode === 'add' ? '新增成功' : '编辑成功')
        this.formDialogVisible = false
        this.loadData()
      } catch (error) {
        this.$message.error('操作失败：' + error.message)
      }
    },

    handleApprove(row) {
      this.currentApprovalData = {
        id: row.id,
        number: row.changeNumber,
        assetCode: row.assetCode,
        assetName: row.assetName,
        type: row.changeType,
        amount: row.changeAmount
      }
      this.approvalDialogVisible = true
    },

    async handleApprovalSubmit(formData) {
      try {
        // TODO: 调用API提交审批
        await new Promise(resolve => setTimeout(resolve, 1000))
        this.$message.success('审批成功')
        this.approvalDialogVisible = false
        this.loadData()
      } catch (error) {
        this.$message.error('审批失败：' + error.message)
      }
    },

    handleExecute(row) {
      this.$confirm('确认执行该资产变动吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          // TODO: 调用API执行变动
          await new Promise(resolve => setTimeout(resolve, 1000))
          this.$message.success('执行成功')
          this.loadData()
        } catch (error) {
          this.$message.error('执行失败：' + error.message)
        }
      }).catch(() => {})
    },

    handleDelete(row) {
      this.$confirm('确认删除该资产变动吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          // TODO: 调用API删除
          await new Promise(resolve => setTimeout(resolve, 1000))
          this.$message.success('删除成功')
          this.loadData()
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      }).catch(() => {})
    },

    handleBatchApprove() {
      if (!this.multipleSelection.length) {
        this.$message.warning('请选择要审批的记录')
        return
      }
      this.batchApprovalDialogVisible = true
    },

    async handleBatchApprovalSubmit(formData) {
      try {
        // TODO: 调用API批量审批
        await new Promise(resolve => setTimeout(resolve, 1000))
        this.$message.success('批量审批成功')
        this.batchApprovalDialogVisible = false
        this.loadData()
      } catch (error) {
        this.$message.error('批量审批失败：' + error.message)
      }
    },

    handleExport() {
      try {
        const data = this.tableData || []
        if (data.length === 0) {
          this.$message.warning('暂无数据可导出')
          return
        }
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '资产变动数据导出.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败')
      }
    },

    handleImport() {
      const input = document.createElement('input')
      input.type = 'file'
      input.accept = '.xlsx,.xls,.csv'
      input.onchange = async (e) => {
        const file = e.target.files[0]
        if (!file) return
        this.$message.success(`文件 ${file.name} 导入处理中...`)
        this.loadData()
      }
      input.click()
    },

    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.loadData()
    },

    handleCurrentChange(page) {
      this.pagination.currentPage = page
      this.loadData()
    },

    formatAmount(amount) {
      return amount ? '¥' + Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2 }) : '¥0.00'
    },

    getChangeTypeTagType(type) {
      const typeMap = {
        'PURCHASE': 'success',
        'TRANSFER': 'info',
        'IMPROVEMENT': 'warning',
        'MAINTENANCE': 'primary',
        'UPGRADE': 'danger'
      }
      return typeMap[type] || 'default'
    },

    getChangeTypeText(type) {
      const textMap = {
        'PURCHASE': '购置',
        'TRANSFER': '调拨',
        'IMPROVEMENT': '改良',
        'MAINTENANCE': '维修',
        'UPGRADE': '升级'
      }
      return textMap[type] || type
    },

    getStatusTagType(status) {
      const typeMap = {
        'PENDING': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'COMPLETED': 'info'
      }
      return typeMap[status] || 'default'
    },

    getStatusText(status) {
      const textMap = {
        'PENDING': '待审批',
        'APPROVED': '已审批',
        'REJECTED': '已拒绝',
        'COMPLETED': '已完成'
      }
      return textMap[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.asset-change-container {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
  
  h2 {
    margin: 0 0 8px 0;
    color: #303133;
  }
  
  p {
    margin: 0;
    color: #606266;
    font-size: 14px;
  }
}

.stats-cards {
  margin-bottom: 20px;

  .stat-card {
    background: white;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    .stat-icon {
      width: 50px;
      height: 50px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 15px;

      i {
        font-size: 24px;
        color: white;
      }

      &.purchase {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.transfer {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.improvement {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }

      &.pending {
        background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
      }
    }

    .stat-content {
      .stat-value {
        font-size: 24px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

.search-container {
  background: white;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.toolbar {
  margin-bottom: 20px;
}

.table-container {
  background: white;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.amount {
  font-weight: 600;
  
  &.positive {
    color: #67c23a;
  }
  
  &.negative {
    color: #f56c6c;
  }
}

.danger-text {
  color: #f56c6c;
}

.change-detail {
  .el-descriptions {
    margin-bottom: 20px;
  }
}
</style>
