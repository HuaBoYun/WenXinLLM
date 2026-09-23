<template>
  <div class="asset-disposal-container">
    <div class="page-header">
      <h2>资产处置管理</h2>
      <p>管理固定资产的报废、出售、转让等处置业务</p>
    </div>
    
    <!-- 处置统计卡片 -->
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon scrap">
              <i class="el-icon-delete"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.scrapCount }}</div>
              <div class="stat-label">本月报废</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon sale">
              <i class="el-icon-sell"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.saleAmount) }}</div>
              <div class="stat-label">出售收入</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon loss">
              <i class="el-icon-warning-outline"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.disposalLoss) }}</div>
              <div class="stat-label">处置损失</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon pending">
              <i class="el-icon-time"></i>
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
        <el-form-item label="处置单号" prop="disposalNumber">
          <el-input
            v-model="searchForm.disposalNumber"
            placeholder="请输入处置单号"
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
        <el-form-item label="处置类型" prop="disposalType">
          <el-select
            v-model="searchForm.disposalType"
            placeholder="请选择处置类型"
            clearable
            style="width: 150px"
          >
            <el-option label="报废" value="SCRAP" />
            <el-option label="出售" value="SALE" />
            <el-option label="转让" value="TRANSFER" />
            <el-option label="捐赠" value="DONATION" />
            <el-option label="毁损" value="DAMAGE" />
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
        <el-form-item label="处置日期" prop="disposalDateRange">
          <el-date-picker
            v-model="searchForm.disposalDateRange"
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
      <el-button type="primary" @click="handleAdd">新增处置</el-button>
      <el-button type="success" @click="handleBatchApprove" :disabled="!multipleSelection.length">
        批量审批
      </el-button>
      <el-button type="warning" @click="handleExport">导出</el-button>
      <el-button type="info" @click="handleGenerateReport">生成报告</el-button>
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
        <el-table-column prop="disposalNumber" label="处置单号" width="140" />
        <el-table-column prop="assetCode" label="资产编码" width="120" />
        <el-table-column prop="assetName" label="资产名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="disposalType" label="处置类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getDisposalTypeTagType(scope.row.disposalType)" size="small">
              {{ getDisposalTypeText(scope.row.disposalType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="originalValue" label="资产原值" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount">{{ formatAmount(scope.row.originalValue) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="netBookValue" label="账面净值" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount">{{ formatAmount(scope.row.netBookValue) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="disposalAmount" label="处置收入" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount income">{{ formatAmount(scope.row.disposalAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="disposalGainLoss" label="处置损益" width="120" align="right">
          <template slot-scope="scope">
            <span class="amount" :class="{ 'gain': scope.row.disposalGainLoss > 0, 'loss': scope.row.disposalGainLoss < 0 }">
              {{ formatAmount(scope.row.disposalGainLoss) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="disposalDate" label="处置日期" width="120" />
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

    <!-- 处置详情对话框 -->
    <el-dialog
      title="资产处置详情"
      :visible.sync="detailDialogVisible"
      width="900px"
      :close-on-click-modal="false"
    >
      <div v-if="currentDisposal" class="disposal-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="处置单号">{{ currentDisposal.disposalNumber }}</el-descriptions-item>
          <el-descriptions-item label="处置类型">
            <el-tag :type="getDisposalTypeTagType(currentDisposal.disposalType)" size="small">
              {{ getDisposalTypeText(currentDisposal.disposalType) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="资产编码">{{ currentDisposal.assetCode }}</el-descriptions-item>
          <el-descriptions-item label="资产名称">{{ currentDisposal.assetName }}</el-descriptions-item>
          <el-descriptions-item label="资产原值">
            <span class="amount">{{ formatAmount(currentDisposal.originalValue) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="累计折旧">
            <span class="amount">{{ formatAmount(currentDisposal.accumulatedDepreciation) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="账面净值">
            <span class="amount">{{ formatAmount(currentDisposal.netBookValue) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="处置收入">
            <span class="amount income">{{ formatAmount(currentDisposal.disposalAmount) }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="处置损益">
            <span class="amount" :class="{ 'gain': currentDisposal.disposalGainLoss > 0, 'loss': currentDisposal.disposalGainLoss < 0 }">
              {{ formatAmount(currentDisposal.disposalGainLoss) }}
            </span>
          </el-descriptions-item>
          <el-descriptions-item label="处置日期">{{ currentDisposal.disposalDate }}</el-descriptions-item>
          <el-descriptions-item label="处置原因" :span="2">{{ currentDisposal.disposalReason }}</el-descriptions-item>
          <el-descriptions-item label="接收方">{{ currentDisposal.receiver || '无' }}</el-descriptions-item>
          <el-descriptions-item label="操作人">{{ currentDisposal.operator }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusTagType(currentDisposal.status)" size="small">
              {{ getStatusText(currentDisposal.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ currentDisposal.createTime }}</el-descriptions-item>
        </el-descriptions>

        <!-- 审批历史 -->
        <div v-if="currentDisposal.approvalHistory && currentDisposal.approvalHistory.length" class="approval-history">
          <h4>审批历史</h4>
          <el-timeline>
            <el-timeline-item
              v-for="(item, index) in currentDisposal.approvalHistory"
              :key="index"
              :timestamp="item.approvalTime"
              :type="item.result === 'APPROVED' ? 'success' : 'danger'"
            >
              <div class="approval-item">
                <div class="approver">{{ item.approver }}</div>
                <div class="result">
                  <el-tag :type="item.result === 'APPROVED' ? 'success' : 'danger'" size="mini">
                    {{ item.result === 'APPROVED' ? '同意' : '拒绝' }}
                  </el-tag>
                </div>
                <div class="comment" v-if="item.comment">{{ item.comment }}</div>
              </div>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 资产处置表单对话框 -->
    <asset-disposal-form-dialog
      :visible.sync="formDialogVisible"
      :mode="dialogMode"
      :disposal-data="currentDisposalData"
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
import AssetDisposalFormDialog from './components/AssetDisposalFormDialog.vue'
import ApprovalDialog from '../components/ApprovalDialog.vue'
import BatchApprovalDialog from '../components/BatchApprovalDialog.vue'

export default {
  name: 'AssetDisposal',
  components: {
    AssetDisposalFormDialog,
    ApprovalDialog,
    BatchApprovalDialog
  },
  data() {
    return {
      loading: false,
      tableData: [],
      multipleSelection: [],
      detailDialogVisible: false,
      currentDisposal: null,
      formDialogVisible: false,
      dialogMode: 'add',
      currentDisposalData: {},
      approvalDialogVisible: false,
      currentApprovalData: {},
      batchApprovalDialogVisible: false,
      searchForm: {
        disposalNumber: '',
        assetCode: '',
        disposalType: '',
        status: '',
        disposalDateRange: []
      },
      pagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      stats: {
        scrapCount: 0,
        saleAmount: 0,
        disposalLoss: 0,
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
          scrapCount: 0,
          saleAmount: 0,
          disposalLoss: 0,
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
      this.currentDisposalData = {}
      this.formDialogVisible = true
    },

    handleView(row) {
      this.currentDisposal = row
      this.detailDialogVisible = true
    },

    handleEdit(row) {
      this.dialogMode = 'edit'
      this.currentDisposalData = { ...row }
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
        id: row.disposalId,
        number: row.disposalNumber,
        assetCode: row.assetCode,
        assetName: row.assetName,
        type: row.disposalType,
        amount: row.disposalAmount
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
      this.$confirm('确认执行该资产处置吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          // TODO: 调用API执行处置
          await new Promise(resolve => setTimeout(resolve, 1000))
          this.$message.success('执行成功')
          this.loadData()
        } catch (error) {
          this.$message.error('执行失败：' + error.message)
        }
      }).catch(() => {})
    },

    handleDelete(row) {
      this.$confirm('确认删除该资产处置吗？', '提示', {
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
        link.download = '资产处置数据导出.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败')
      }
    },

    handleGenerateReport() {
      try {
        const data = this.tableData || []
        if (data.length === 0) {
          this.$message.warning('暂无数据可生成报告')
          return
        }
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '资产处置报告.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('报告生成成功')
      } catch (error) {
        this.$message.error('报告生成失败')
      }
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

    getDisposalTypeTagType(type) {
      const typeMap = {
        'SCRAP': 'danger',
        'SALE': 'success',
        'TRANSFER': 'info',
        'DONATION': 'warning',
        'DAMAGE': 'danger'
      }
      return typeMap[type] || 'default'
    },

    getDisposalTypeText(type) {
      const textMap = {
        'SCRAP': '报废',
        'SALE': '出售',
        'TRANSFER': '转让',
        'DONATION': '捐赠',
        'DAMAGE': '毁损'
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
.asset-disposal-container {
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

      &.scrap {
        background: linear-gradient(135deg, #f56c6c 0%, #e85a4f 100%);
      }

      &.sale {
        background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
      }

      &.loss {
        background: linear-gradient(135deg, #e6a23c 0%, #f7ba2a 100%);
      }

      &.pending {
        background: linear-gradient(135deg, #909399 0%, #b3b6bb 100%);
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
  
  &.income {
    color: #67c23a;
  }
  
  &.gain {
    color: #67c23a;
  }
  
  &.loss {
    color: #f56c6c;
  }
}

.danger-text {
  color: #f56c6c;
}

.disposal-detail {
  .el-descriptions {
    margin-bottom: 20px;
  }

  .approval-history {
    h4 {
      margin: 20px 0 15px 0;
      color: #303133;
    }

    .approval-item {
      .approver {
        font-weight: 600;
        margin-bottom: 5px;
      }

      .result {
        margin-bottom: 5px;
      }

      .comment {
        color: #606266;
        font-size: 14px;
      }
    }
  }
}
</style>
