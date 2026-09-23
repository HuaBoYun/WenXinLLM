<template>
  <el-dialog
    title="应收汇总"
    :visible.sync="dialogVisible"
    width="900px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div v-loading="loading" class="summary-container">
      <!-- 客户信息 -->
      <div class="customer-info">
        <div class="info-item">
          <span class="label">客户编码：</span>
          <span class="value">{{ customerData.customerCode }}</span>
        </div>
        <div class="info-item">
          <span class="label">客户名称：</span>
          <span class="value">{{ customerData.customerName }}</span>
        </div>
      </div>

      <!-- 汇总统计 -->
      <div class="summary-stats">
        <el-row :gutter="16">
          <el-col :span="6">
            <div class="stat-card total">
              <div class="stat-label">应收总额</div>
              <div class="stat-value">¥ {{ formatAmount(summaryData.totalReceivable) }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card received">
              <div class="stat-label">已收金额</div>
              <div class="stat-value">¥ {{ formatAmount(summaryData.totalReceived) }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card unpaid">
              <div class="stat-label">未收金额</div>
              <div class="stat-value">¥ {{ formatAmount(summaryData.totalUnpaid) }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card overdue">
              <div class="stat-label">逾期金额</div>
              <div class="stat-value">¥ {{ formatAmount(summaryData.totalOverdue) }}</div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 应收单列表 -->
      <div class="receivable-list">
        <div class="section-title">
          <i class="el-icon-document"></i>
          应收单明细
        </div>
        <el-table :data="receivableList" border size="small" max-height="300">
          <el-table-column prop="receivableNo" label="应收单号" width="150" />
          <el-table-column prop="businessType" label="业务类型" width="100">
            <template slot-scope="scope">
              {{ getBusinessTypeName(scope.row.businessType) }}
            </template>
          </el-table-column>
          <el-table-column prop="receivableAmount" label="应收金额" width="120" align="right">
            <template slot-scope="scope">
              <span>¥ {{ formatAmount(scope.row.receivableAmount) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="receivedAmount" label="已收金额" width="120" align="right">
            <template slot-scope="scope">
              <span class="amount-received">¥ {{ formatAmount(scope.row.receivedAmount) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="unpaidAmount" label="未收金额" width="120" align="right">
            <template slot-scope="scope">
              <span class="amount-unpaid">¥ {{ formatAmount(scope.row.unpaidAmount) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="receivableDate" label="应收日期" width="110" />
          <el-table-column prop="dueDate" label="到期日期" width="110" />
          <el-table-column prop="status" label="状态" width="90">
            <template slot-scope="scope">
              <el-tag :type="getStatusType(scope.row.status)" size="mini">
                {{ getStatusName(scope.row.status) }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 账龄分析 -->
      <div class="aging-analysis">
        <div class="section-title">
          <i class="el-icon-data-analysis"></i>
          账龄分析
        </div>
        <el-table :data="agingData" border size="small">
          <el-table-column prop="period" label="账龄区间" />
          <el-table-column prop="amount" label="金额" align="right">
            <template slot-scope="scope">
              <span>¥ {{ formatAmount(scope.row.amount) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="percentage" label="占比" align="right">
            <template slot-scope="scope">
              <span>{{ scope.row.percentage }}%</span>
            </template>
          </el-table-column>
          <el-table-column prop="count" label="笔数" align="right" />
        </el-table>
      </div>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
      <el-button type="primary" icon="el-icon-printer" @click="handleExport">导出报表</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getCustomerReceivableSummary } from '@/api/financialSharing/receivables'

export default {
  name: 'ReceivableSummaryDialog',
  data() {
    return {
      dialogVisible: false,
      loading: false,
      customerData: {},
      summaryData: {
        totalReceivable: 0,
        totalReceived: 0,
        totalUnpaid: 0,
        totalOverdue: 0
      },
      receivableList: [],
      agingData: []
    }
  },
  methods: {
    open(row) {
      this.dialogVisible = true
      this.customerData = { ...row }
      this.loadSummaryData()
    },
    handleClose() {
      this.dialogVisible = false
      this.customerData = {}
      this.summaryData = { totalReceivable: 0, totalReceived: 0, totalUnpaid: 0, totalOverdue: 0 }
      this.receivableList = []
      this.agingData = []
    },
    async loadSummaryData() {
      this.loading = true
      try {
        const res = await getCustomerReceivableSummary({ customerId: this.customerData.customerId })
        if (res.code === 1 || res.code === 200) {
          this.summaryData = res.data.summary || this.summaryData
          this.receivableList = res.data.receivableList || []
          this.agingData = res.data.agingData || []
        }
      } catch (e) {
        // 接口失败时降级为空状态
        this.loadMockData()
      } finally {
        this.loading = false
      }
    },
    // 数据加载失败时的空状态降级（不再使用模拟数据）
    loadMockData() {
      this.summaryData = {
        totalReceivable: 0,
        totalReceived: 0,
        totalUnpaid: 0,
        totalOverdue: 0
      }
      this.receivableList = []
      this.agingData = []
    },
    formatAmount(amount) {
      if (!amount && amount !== 0) return '0.00'
      return Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
    },
    getBusinessTypeName(type) {
      const map = { 1: '销售', 2: '服务', 3: '其他' }
      return map[type] || '未知'
    },
    getStatusType(status) {
      const map = { 1: 'info', 2: 'warning', 3: 'success', 4: 'danger' }
      return map[status] || 'info'
    },
    getStatusName(status) {
      const map = { 1: '待收款', 2: '部分收款', 3: '已收款', 4: '已逾期' }
      return map[status] || '未知'
    },
    handleExport() {
      try {
        const data = this.receivableList || []
        if (data.length === 0) {
          this.$message.warning('暂无数据可导出')
          return
        }
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `${this.customerData.customerName || '客户'}_应收汇总.json`
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.summary-container {
  .customer-info {
    display: flex;
    gap: 40px;
    padding: 12px 16px;
    background: #f5f7fa;
    border-radius: 8px;
    margin-bottom: 20px;

    .info-item {
      .label {
        color: #909399;
        font-size: 13px;
      }
      .value {
        color: #303133;
        font-weight: 500;
      }
    }
  }

  .summary-stats {
    margin-bottom: 20px;

    .stat-card {
      padding: 16px;
      border-radius: 8px;
      text-align: center;

      .stat-label {
        font-size: 13px;
        color: #606266;
        margin-bottom: 8px;
      }
      .stat-value {
        font-size: 18px;
        font-weight: 600;
      }

      &.total {
        background: #ecf5ff;
        .stat-value { color: #409eff; }
      }
      &.received {
        background: #f0f9eb;
        .stat-value { color: #67c23a; }
      }
      &.unpaid {
        background: #fdf6ec;
        .stat-value { color: #e6a23c; }
      }
      &.overdue {
        background: #fef0f0;
        .stat-value { color: #f56c6c; }
      }
    }
  }

  .receivable-list,
  .aging-analysis {
    margin-bottom: 20px;

    .section-title {
      font-size: 14px;
      font-weight: 600;
      color: #303133;
      margin-bottom: 12px;
      display: flex;
      align-items: center;

      i {
        margin-right: 8px;
        color: #409eff;
      }
    }
  }

  .amount-received {
    color: #67c23a;
  }
  .amount-unpaid {
    color: #e6a23c;
  }
}
</style>
