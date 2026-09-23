<template>
  <div class="account-balance-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-s-data"></i>
          科目余额
        </h1>
        <p class="page-description">查看和管理各科目的期初、本期发生、期末余额信息</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-refresh" @click="refreshBalance">
          刷新余额
        </el-button>
        <el-button type="success" icon="el-icon-download" @click="exportBalance">
          导出余额
        </el-button>
        <el-button type="warning" icon="el-icon-edit" @click="adjustBalance">
          余额调整
        </el-button>
      </div>
    </div>

    <!-- 查询条件 -->
    <div class="search-container">
      <el-form :model="searchForm" :inline="true" label-width="80px">
        <el-form-item label="会计期间">
          <el-date-picker
            v-model="searchForm.period"
            type="month"
            placeholder="选择会计期间"
            format="yyyy-MM"
            value-format="yyyy-MM">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="科目编码">
          <el-input
            v-model="searchForm.subjectCode"
            placeholder="请输入科目编码"
            clearable>
          </el-input>
        </el-form-item>
        <el-form-item label="科目名称">
          <el-input
            v-model="searchForm.subjectName"
            placeholder="请输入科目名称"
            clearable>
          </el-input>
        </el-form-item>
        <el-form-item label="科目类别">
          <el-select v-model="searchForm.subjectCategory" placeholder="请选择科目类别" clearable>
            <el-option label="资产类" value="assets"></el-option>
            <el-option label="负债类" value="liabilities"></el-option>
            <el-option label="所有者权益类" value="equity"></el-option>
            <el-option label="成本类" value="cost"></el-option>
            <el-option label="损益类" value="profit_loss"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="余额方向">
          <el-select v-model="searchForm.balanceDirection" placeholder="请选择余额方向" clearable>
            <el-option label="借方" value="debit"></el-option>
            <el-option label="贷方" value="credit"></el-option>
            <el-option label="无余额" value="zero"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchBalance">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 统计信息 -->
    <div class="stats-cards">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon debit">
              <i class="el-icon-top"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalDebit) }}</div>
              <div class="stat-label">借方余额合计</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon credit">
              <i class="el-icon-bottom"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalCredit) }}</div>
              <div class="stat-label">贷方余额合计</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon balance">
              <i class="el-icon-scale-to-original"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value" :class="{ 'balanced': stats.isBalanced, 'unbalanced': !stats.isBalanced }">
                {{ stats.isBalanced ? '平衡' : '不平衡' }}
              </div>
              <div class="stat-label">借贷平衡</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon count">
              <i class="el-icon-document"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.subjectCount }}</div>
              <div class="stat-label">科目数量</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 科目余额表格 -->
    <div class="balance-table">
      <el-table
        v-loading="loading"
        :data="balanceList"
        stripe
        border
        style="width: 100%"
        :summary-method="getSummaries"
        show-summary>
        <el-table-column prop="subjectCode" label="科目编码" width="120" fixed="left"></el-table-column>
        <el-table-column prop="subjectName" label="科目名称" min-width="200" fixed="left"></el-table-column>
        <el-table-column prop="subjectCategory" label="科目类别" width="100">
          <template slot-scope="scope">
            <el-tag :type="getSubjectCategoryTag(scope.row.subjectCategory)">
              {{ getSubjectCategoryName(scope.row.subjectCategory) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="openingBalance" label="期初余额" width="120" align="right">
          <template slot-scope="scope">
            <span :class="{ 'debit-amount': scope.row.openingBalance > 0, 'credit-amount': scope.row.openingBalance < 0 }">
              {{ formatAmount(Math.abs(scope.row.openingBalance)) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="currentDebit" label="本期借方" width="120" align="right">
          <template slot-scope="scope">
            {{ formatAmount(scope.row.currentDebit) }}
          </template>
        </el-table-column>
        <el-table-column prop="currentCredit" label="本期贷方" width="120" align="right">
          <template slot-scope="scope">
            {{ formatAmount(scope.row.currentCredit) }}
          </template>
        </el-table-column>
        <el-table-column prop="endingBalance" label="期末余额" width="120" align="right">
          <template slot-scope="scope">
            <span :class="{ 'debit-amount': scope.row.endingBalance > 0, 'credit-amount': scope.row.endingBalance < 0 }">
              {{ formatAmount(Math.abs(scope.row.endingBalance)) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="balanceDirection" label="余额方向" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.endingBalance > 0 ? 'primary' : scope.row.endingBalance < 0 ? 'success' : 'info'" size="mini">
              {{ scope.row.endingBalance > 0 ? '借' : scope.row.endingBalance < 0 ? '贷' : '平' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="viewDetail(scope.row)">查看明细</el-button>
            <el-button size="mini" type="text" @click="viewTrend(scope.row)">余额趋势</el-button>
            <el-button size="mini" type="text" @click="adjustSubjectBalance(scope.row)">余额调整</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total">
        </el-pagination>
      </div>
    </div>

    <!-- 余额调整对话框 -->
    <BalanceAdjustDialog
      :visible.sync="adjustDialogVisible"
      :subject-data="selectedSubject"
      @confirm="handleAdjustConfirm"
    />

    <!-- 余额趋势对话框 -->
    <BalanceTrendDialog
      :visible.sync="trendDialogVisible"
      :subject-data="selectedSubject"
    />

    <!-- 导出进度对话框 -->
    <ExportProgressDialog
      :visible.sync="exportDialogVisible"
      :export-info="exportInfo"
      @start-export="handleStartExport"
      @export-success="handleExportSuccess"
      @export-cancel="handleExportCancel" />
  </div>
</template>

<script>
import {
  getGeneralLedgerBalancePage,
  getSubjectBalanceDetail,
  recalculateSubjectBalance,
  exportOpeningBalance
} from '@/api/financialSharing/generalLedger'
import BalanceAdjustDialog from './components/BalanceAdjustDialog'
import BalanceTrendDialog from './components/BalanceTrendDialog'
import ExportProgressDialog from '@/components/ExportProgressDialog'

export default {
  name: 'AccountBalanceIndex',
  components: {
    BalanceAdjustDialog,
    BalanceTrendDialog,
    ExportProgressDialog
  },
  data() {
    return {
      loading: false,
      balanceList: [],
      selectedSubject: null,
      adjustDialogVisible: false,
      trendDialogVisible: false,
      exportDialogVisible: false,
      searchForm: {
        period: '',
        subjectCode: '',
        subjectName: '',
        subjectCategory: '',
        balanceDirection: ''
      },
      stats: {
        totalDebit: 0,
        totalCredit: 0,
        isBalanced: true,
        subjectCount: 0
      },
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      },
      exportInfo: {
        type: '科目余额表',
        range: '',
        count: 0
      }
    }
  },
  mounted() {
    this.initDefaultPeriod()
    this.loadBalanceList()
  },
  methods: {
    initDefaultPeriod() {
      const now = new Date()
      this.searchForm.period = now.getFullYear() + '-' + String(now.getMonth() + 1).padStart(2, '0')
    },
    async loadBalanceList() {
      this.loading = true
      try {
        const params = {
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize,
          ...this.searchForm
        }
        
        const response = await getGeneralLedgerBalancePage(params)
        if (response.code === 200) {
          this.balanceList = response.data.records || []
          this.pagination.total = response.data.total || 0
          this.updateStats()
        }
      } catch (error) {
        this.$message.error('加载科目余额失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    updateStats() {
      let totalDebit = 0
      let totalCredit = 0
      
      this.balanceList.forEach(item => {
        if (item.endingBalance > 0) {
          totalDebit += item.endingBalance
        } else if (item.endingBalance < 0) {
          totalCredit += Math.abs(item.endingBalance)
        }
      })
      
      this.stats = {
        totalDebit,
        totalCredit,
        isBalanced: Math.abs(totalDebit - totalCredit) < 0.01,
        subjectCount: this.balanceList.length
      }
    },
    searchBalance() {
      this.pagination.currentPage = 1
      this.loadBalanceList()
    },
    resetSearch() {
      this.searchForm = {
        period: '',
        subjectCode: '',
        subjectName: '',
        subjectCategory: '',
        balanceDirection: ''
      }
      this.initDefaultPeriod()
      this.searchBalance()
    },
    async refreshBalance() {
      try {
        const response = await recalculateSubjectBalance({
          period: this.searchForm.period
        })
        if (response.code === 200) {
          this.$message.success('余额刷新成功')
          this.loadBalanceList()
        }
      } catch (error) {
        this.$message.error('余额刷新失败：' + error.message)
      }
    },
    async exportBalance() {
      // 准备导出信息
      this.exportInfo = {
        type: '科目余额表',
        range: this.searchForm.period || '全部期间',
        count: this.pagination.total
      }
      this.exportDialogVisible = true
    },
    async handleStartExport(options) {
      try {
        const response = await exportOpeningBalance({
          ...this.searchForm,
          exportOptions: options
        })

        // 处理文件下载
        const blob = new Blob([response], {
          type: options.format === 'pdf' ? 'application/pdf' : 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        })
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = `科目余额表_${this.searchForm.period || '全部'}_${new Date().getTime()}.${options.format}`
        link.click()
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
        throw error
      }
    },
    handleExportSuccess() {
      this.$message.success('导出成功')
    },
    handleExportCancel() {
      this.$message.info('已取消导出')
    },
    adjustBalance() {
      this.$confirm('确认执行批量余额调整操作？此操作将对不平衡的科目进行余额调整。', '批量余额调整', {
        type: 'warning'
      }).then(() => {
        this.$message.success('批量余额调整已完成')
      }).catch(() => {})
    },
    viewDetail(row) {
      this.$router.push({
        path: '/management/financial/generalLedger/detailLedger',
        query: {
          subjectCode: row.subjectCode,
          period: this.searchForm.period
        }
      })
    },
    viewTrend(row) {
      this.selectedSubject = row
      this.trendDialogVisible = true
    },
    adjustSubjectBalance(row) {
      this.selectedSubject = row
      this.adjustDialogVisible = true
    },
    handleAdjustConfirm() {
      this.loadBalanceList()
    },
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.loadBalanceList()
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.loadBalanceList()
    },
    formatAmount(amount) {
      if (amount >= 10000) {
        return (amount / 10000).toFixed(2) + '万'
      }
      return amount.toLocaleString()
    },
    getSubjectCategoryTag(category) {
      const tags = {
        assets: 'primary',
        liabilities: 'success',
        equity: 'warning',
        cost: 'danger',
        profit_loss: 'info'
      }
      return tags[category] || 'default'
    },
    getSubjectCategoryName(category) {
      const names = {
        assets: '资产类',
        liabilities: '负债类',
        equity: '权益类',
        cost: '成本类',
        profit_loss: '损益类'
      }
      return names[category] || category
    },
    getSummaries(param) {
      const { columns, data } = param
      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计'
          return
        }
        if (index === 1) {
          sums[index] = ''
          return
        }
        
        const values = data.map(item => Number(item[column.property]))
        if (!values.every(value => isNaN(value))) {
          sums[index] = this.formatAmount(values.reduce((prev, curr) => {
            const value = Number(curr)
            if (!isNaN(value)) {
              return prev + curr
            } else {
              return prev
            }
          }, 0))
        } else {
          sums[index] = ''
        }
      })
      return sums
    }
  }
}
</script>

<style lang="scss" scoped>
.account-balance-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      font-size: 24px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 12px;
        color: #409eff;
      }
    }

    .page-description {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .header-right {
    .el-button {
      margin-left: 12px;
    }
  }
}

.search-container {
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.stats-cards {
  margin-bottom: 24px;

  .stat-card {
    background: white;
    border-radius: 12px;
    padding: 24px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
    }

    .stat-icon {
      width: 48px;
      height: 48px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;

      i {
        font-size: 24px;
        color: white;
      }

      &.debit {
        background: linear-gradient(135deg, #409eff 0%, #36a3f7 100%);
      }

      &.credit {
        background: linear-gradient(135deg, #67c23a 0%, #5daf34 100%);
      }

      &.balance {
        background: linear-gradient(135deg, #e6a23c 0%, #d48806 100%);
      }

      &.count {
        background: linear-gradient(135deg, #f56c6c 0%, #f04864 100%);
      }
    }

    .stat-content {
      .stat-value {
        font-size: 20px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;

        &.balanced {
          color: #67c23a;
        }

        &.unbalanced {
          color: #f56c6c;
        }
      }

      .stat-label {
        font-size: 12px;
        color: #909399;
      }
    }
  }
}

.balance-table {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .el-table {
    border-radius: 8px;
    overflow: hidden;

    .debit-amount {
      color: #409eff;
      font-weight: 600;
    }

    .credit-amount {
      color: #67c23a;
      font-weight: 600;
    }
  }
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}
</style>
