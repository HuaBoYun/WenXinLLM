<template>
  <div class="trial-balance-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-scale-to-original"></i>
          试算平衡表
        </h1>
        <p class="page-description">检查账簿记录的借贷平衡关系，确保账务处理的准确性</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-refresh" @click="generateTrialBalance">
          生成试算平衡表
        </el-button>
        <el-button type="success" icon="el-icon-download" @click="exportTrialBalance">
          导出报表
        </el-button>
        <el-button type="warning" icon="el-icon-printer" @click="printTrialBalance">
          打印报表
        </el-button>
      </div>
    </div>

    <!-- 查询条件 -->
    <div class="search-container">
      <el-form :model="searchForm" :inline="true" label-width="80px">
        <el-form-item label="会计期间" required>
          <el-date-picker
            v-model="searchForm.accountingPeriod"
            type="month"
            placeholder="选择会计期间"
            format="yyyy-MM"
            value-format="yyyy-MM">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="科目级次">
          <el-select v-model="searchForm.subjectLevel" placeholder="请选择科目级次">
            <el-option label="全部级次" :value="0"></el-option>
            <el-option label="一级科目" :value="1"></el-option>
            <el-option label="二级科目" :value="2"></el-option>
            <el-option label="三级科目" :value="3"></el-option>
            <el-option label="明细科目" :value="4"></el-option>
          </el-select>
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
        <el-form-item label="显示选项">
          <el-checkbox-group v-model="searchForm.displayOptions">
            <el-checkbox label="showZeroBalance">显示零余额科目</el-checkbox>
            <el-checkbox label="showOnlyImbalance">仅显示不平衡科目</el-checkbox>
            <el-checkbox label="includeAuxiliary">包含辅助核算</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchTrialBalance">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 平衡状态统计 -->
    <div class="balance-stats">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card balanced">
            <div class="stat-icon">
              <i class="el-icon-success"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.balancedCount }}</div>
              <div class="stat-label">平衡科目</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card imbalanced">
            <div class="stat-icon">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ stats.imbalancedCount }}</div>
              <div class="stat-label">不平衡科目</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card total-debit">
            <div class="stat-icon">
              <i class="el-icon-top"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalDebit) }}</div>
              <div class="stat-label">借方合计</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card total-credit">
            <div class="stat-icon">
              <i class="el-icon-bottom"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatAmount(stats.totalCredit) }}</div>
              <div class="stat-label">贷方合计</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 整体平衡状态 -->
    <div class="overall-balance">
      <el-alert
        :title="overallBalanceTitle"
        :type="overallBalanceType"
        :description="overallBalanceDescription"
        show-icon
        :closable="false">
      </el-alert>
    </div>

    <!-- 试算平衡表 -->
    <div class="trial-balance-table">
      <el-table
        v-loading="loading"
        :data="trialBalanceList"
        stripe
        border
        style="width: 100%"
        :summary-method="getSummaries"
        show-summary
        :span-method="objectSpanMethod">
        <el-table-column prop="subjectCode" label="科目编码" width="120" fixed="left"></el-table-column>
        <el-table-column prop="subjectName" label="科目名称" min-width="200" fixed="left"></el-table-column>
        <el-table-column prop="subjectCategory" label="科目类别" width="100">
          <template slot-scope="scope">
            <el-tag :type="getSubjectCategoryTag(scope.row.subjectCategory)" size="mini">
              {{ getSubjectCategoryName(scope.row.subjectCategory) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="期初余额" align="center">
          <el-table-column prop="openingDebit" label="借方" width="120" align="right">
            <template slot-scope="scope">
              <span v-if="scope.row.openingDebit > 0" class="debit-amount">
                {{ formatAmount(scope.row.openingDebit) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="openingCredit" label="贷方" width="120" align="right">
            <template slot-scope="scope">
              <span v-if="scope.row.openingCredit > 0" class="credit-amount">
                {{ formatAmount(scope.row.openingCredit) }}
              </span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="本期发生额" align="center">
          <el-table-column prop="currentDebit" label="借方" width="120" align="right">
            <template slot-scope="scope">
              <span v-if="scope.row.currentDebit > 0" class="debit-amount">
                {{ formatAmount(scope.row.currentDebit) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="currentCredit" label="贷方" width="120" align="right">
            <template slot-scope="scope">
              <span v-if="scope.row.currentCredit > 0" class="credit-amount">
                {{ formatAmount(scope.row.currentCredit) }}
              </span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="期末余额" align="center">
          <el-table-column prop="endingDebit" label="借方" width="120" align="right">
            <template slot-scope="scope">
              <span v-if="scope.row.endingDebit > 0" class="debit-amount">
                {{ formatAmount(scope.row.endingDebit) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="endingCredit" label="贷方" width="120" align="right">
            <template slot-scope="scope">
              <span v-if="scope.row.endingCredit > 0" class="credit-amount">
                {{ formatAmount(scope.row.endingCredit) }}
              </span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column prop="balanceStatus" label="平衡状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isBalanced ? 'success' : 'danger'" size="mini">
              {{ scope.row.isBalanced ? '平衡' : '不平衡' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="viewSubjectDetail(scope.row)">查看明细</el-button>
            <el-button size="mini" type="text" @click="adjustBalance(scope.row)" v-if="!scope.row.isBalanced">
              调整余额
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 不平衡分析对话框 -->
    <ImbalanceAnalysisDialog
      :visible.sync="analysisDialogVisible"
      :analysis-data="imbalanceAnalysis"
    />
  </div>
</template>

<script>
import {
  generateTrialBalance as apiGenerateTrialBalance,
  checkBalance,
  exportTrialBalance as apiExportTrialBalance,
  getAvailablePeriods,
  getTrialBalanceConfig,
  buildTrialBalanceParam
} from '@/api/financialSharing/trialBalance'
import ImbalanceAnalysisDialog from './components/ImbalanceAnalysisDialog'

export default {
  name: 'TrialBalanceIndex',
  components: {
    ImbalanceAnalysisDialog
  },
  data() {
    return {
      loading: false,
      trialBalanceList: [],
      analysisDialogVisible: false,
      imbalanceAnalysis: {},
      searchForm: {
        bookId: 1, // 默认账簿ID，应该从用户信息或配置中获取
        tenantId: 1, // 默认租户ID，应该从用户信息中获取
        accountingPeriod: '',
        subjectLevel: 0,
        subjectType: 0,
        subjectCategory: '', // 科目类别
        subjectIds: [],
        excludeSubjectIds: [],
        includeUnposted: false,
        includeZeroBalance: true,
        includeDetailSubjects: true,
        includeAuxiliary: false,
        tolerance: 0.01,
        displayOptions: ['showZeroBalance'] // 显示选项，默认显示零余额科目
      },
      stats: {
        balancedCount: 0,
        imbalancedCount: 0,
        totalDebit: 0,
        totalCredit: 0
      },
      currentTrialBalanceResult: null
    }
  },
  computed: {
    overallBalanceTitle() {
      const diff = Math.abs(this.stats.totalDebit - this.stats.totalCredit)
      if (diff < 0.01) {
        return '试算平衡检查：平衡'
      } else {
        return `试算平衡检查：不平衡（差额：${this.formatAmount(diff)}）`
      }
    },
    overallBalanceType() {
      const diff = Math.abs(this.stats.totalDebit - this.stats.totalCredit)
      return diff < 0.01 ? 'success' : 'error'
    },
    overallBalanceDescription() {
      const diff = Math.abs(this.stats.totalDebit - this.stats.totalCredit)
      if (diff < 0.01) {
        return '所有科目的借贷方发生额和余额均保持平衡，账务处理正确。'
      } else {
        return `存在借贷不平衡情况，请检查相关科目的账务处理。不平衡科目数量：${this.stats.imbalancedCount}个`
      }
    }
  },
  mounted() {
    this.initDefaultPeriod()
    this.loadAvailablePeriods()
    this.loadTrialBalance()
  },
  methods: {
    initDefaultPeriod() {
      const now = new Date()
      this.searchForm.accountingPeriod = now.getFullYear() + '-' + String(now.getMonth() + 1).padStart(2, '0')
    },
    async loadAvailablePeriods() {
      try {
        const response = await getAvailablePeriods({
          bookId: this.searchForm.bookId,
          tenantId: this.searchForm.tenantId
        })
        if (response.code === 1) {
          // 可以在这里设置可选期间列表
          console.log('可用期间：', response.data)
        }
      } catch (error) {
        console.error('加载可用期间失败：', error)
      }
    },
    async loadTrialBalance() {
      this.loading = true
      try {
        // 构建API参数
        const apiParam = this.buildApiParam()
        const response = await apiGenerateTrialBalance(apiParam)
        if (response.code === 1) {
          this.currentTrialBalanceResult = response.data
          this.trialBalanceList = this.formatTrialBalanceData(response.data)
          this.updateStatsFromResponse(response.data)
        } else {
          this.$message.error(response.msg || '加载试算平衡表失败')
          // 数据加载失败时的空状态降级（不再使用模拟数据）
          this.trialBalanceList = []
          this.updateStats()
        }
      } catch (error) {
        console.error('加载试算平衡表失败：', error)
        this.$message.error('加载试算平衡表失败：' + (error.message || '未知错误'))
        // 数据加载失败时的空状态降级（不再使用模拟数据）
        this.trialBalanceList = []
        this.updateStats()
      } finally {
        this.loading = false
      }
    },
    buildApiParam() {
      // 将搜索表单转换为API参数
      return buildTrialBalanceParam({
        bookId: this.searchForm.bookId,
        tenantId: this.searchForm.tenantId,
        accountingPeriod: this.searchForm.accountingPeriod,
        subjectLevel: this.searchForm.subjectLevel === 'all' ? 0 : parseInt(this.searchForm.subjectLevel),
        subjectType: this.getSubjectTypeValue(this.searchForm.subjectCategory),
        includeZeroBalance: this.searchForm.displayOptions.includes('showZeroBalance'),
        includeAuxiliary: this.searchForm.displayOptions.includes('includeAuxiliary'),
        tolerance: this.searchForm.tolerance
      })
    },
    getSubjectTypeValue(category) {
      const typeMap = {
        'assets': 1,
        'liabilities': 2,
        'equity': 3,
        'cost': 5,
        'profit_loss': 4
      }
      return typeMap[category] || 0
    },
    formatTrialBalanceData(data) {
      if (!data || !data.subjectBalances) {
        return []
      }

      return data.subjectBalances.map(item => ({
        subjectCode: item.subjectCode || '',
        subjectName: item.subjectName || '',
        subjectCategory: this.getSubjectCategoryByType(item.subjectType),
        openingDebit: item.balanceDirection === 1 ? item.openingBalance || 0 : 0,
        openingCredit: item.balanceDirection === 2 ? item.openingBalance || 0 : 0,
        currentDebit: item.debitAmount || 0,
        currentCredit: item.creditAmount || 0,
        endingDebit: item.balanceDirection === 1 ? item.closingBalance || 0 : 0,
        endingCredit: item.balanceDirection === 2 ? item.closingBalance || 0 : 0,
        isBalanced: item.isNormal !== false,
        isCategory: false,
        subjectId: item.subjectId
      }))
    },
    getSubjectCategoryByType(type) {
      const categoryMap = {
        1: 'assets',
        2: 'liabilities',
        3: 'equity',
        4: 'profit_loss',
        5: 'cost'
      }
      return categoryMap[type] || 'assets'
    },
    updateStatsFromResponse(data) {
      if (!data) {
        this.updateStats()
        return
      }

      let balancedCount = 0
      let imbalancedCount = 0

      if (data.subjectBalances) {
        data.subjectBalances.forEach(item => {
          if (item.isNormal !== false) {
            balancedCount++
          } else {
            imbalancedCount++
          }
        })
      }

      this.stats = {
        balancedCount,
        imbalancedCount,
        totalDebit: data.totalDebit || 0,
        totalCredit: data.totalCredit || 0
      }
    },
    generateMockTrialBalanceData() {
      // 暂未对接 API，先以空状态展示
      return []
    },
    updateStats() {
      let balancedCount = 0
      let imbalancedCount = 0
      let totalDebit = 0
      let totalCredit = 0
      
      this.trialBalanceList.forEach(item => {
        if (!item.isCategory) {
          if (item.isBalanced) {
            balancedCount++
          } else {
            imbalancedCount++
          }
          totalDebit += item.endingDebit
          totalCredit += item.endingCredit
        }
      })
      
      this.stats = {
        balancedCount,
        imbalancedCount,
        totalDebit,
        totalCredit
      }
    },
    searchTrialBalance() {
      this.loadTrialBalance()
    },
    resetSearch() {
      this.searchForm = {
        ...this.searchForm,
        accountingPeriod: '',
        subjectLevel: 0,
        subjectCategory: '',
        displayOptions: ['showZeroBalance']
      }
      this.initDefaultPeriod()
      this.searchTrialBalance()
    },
    async generateTrialBalance() {
      this.loading = true
      try {
        const apiParam = this.buildApiParam()
        const response = await apiGenerateTrialBalance(apiParam)
        if (response.code === 1) {
          this.currentTrialBalanceResult = response.data
          this.trialBalanceList = this.formatTrialBalanceData(response.data)
          this.updateStatsFromResponse(response.data)

          if (response.data.isBalanced) {
            this.$message.success('试算平衡表生成成功：' + (response.msg || '试算平衡'))
          } else {
            this.$message.warning('试算平衡表生成完成：' + (response.msg || '试算不平衡'))
          }
        } else {
          this.$message.error(response.msg || '生成试算平衡表失败')
        }
      } catch (error) {
        console.error('生成试算平衡表失败：', error)
        this.$message.error('生成试算平衡表失败：' + (error.message || '未知错误'))
      } finally {
        this.loading = false
      }
    },
    async exportTrialBalance() {
      if (!this.searchForm.accountingPeriod) {
        this.$message.warning('请先选择会计期间')
        return
      }

      this.loading = true
      try {
        const apiParam = this.buildApiParam()
        const response = await apiExportTrialBalance(apiParam)

        // 处理文件下载
        const blob = new Blob([response], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `试算平衡表_${this.searchForm.accountingPeriod}.xlsx`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)

        this.$message.success('导出成功')
      } catch (error) {
        console.error('导出失败：', error)
        this.$message.error('导出失败：' + (error.message || '未知错误'))
      } finally {
        this.loading = false
      }
    },
    printTrialBalance() {
      window.print()
    },
    viewSubjectDetail(row) {
      this.$router.push({
        path: '/management/financial/generalLedger/detailLedger',
        query: {
          subjectCode: row.subjectCode,
          period: this.searchForm.accountingPeriod
        }
      })
    },
    adjustBalance(row) {
      const content = `<p><b>科目编码：</b>${row.subjectCode || '-'}</p><p><b>科目名称：</b>${row.subjectName || '-'}</p><p><b>借方余额：</b>${row.debitBalance || 0}</p><p><b>贷方余额：</b>${row.creditBalance || 0}</p><p><b>差额：</b>${(row.debitBalance || 0) - (row.creditBalance || 0)}</p>`
      this.$alert(content, '余额调整', { dangerouslyUseHTMLString: true })
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
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      if (row.isCategory) {
        if (columnIndex === 0) {
          return [1, 2]
        } else if (columnIndex === 1) {
          return [0, 0]
        }
      }
    },
    getSummaries(param) {
      const { columns, data } = param
      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '总计'
          return
        }
        if (index === 1 || index === 2) {
          sums[index] = ''
          return
        }
        
        const values = data.filter(item => !item.isCategory).map(item => Number(item[column.property]))
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
.trial-balance-container {
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
        color: #e6a23c;
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

.balance-stats {
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
    }

    &.balanced .stat-icon {
      background: linear-gradient(135deg, #67c23a 0%, #5daf34 100%);
    }

    &.imbalanced .stat-icon {
      background: linear-gradient(135deg, #f56c6c 0%, #f04864 100%);
    }

    &.total-debit .stat-icon {
      background: linear-gradient(135deg, #409eff 0%, #36a3f7 100%);
    }

    &.total-credit .stat-icon {
      background: linear-gradient(135deg, #e6a23c 0%, #d48806 100%);
    }

    .stat-content {
      .stat-value {
        font-size: 20px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .stat-label {
        font-size: 12px;
        color: #909399;
      }
    }
  }
}

.overall-balance {
  margin-bottom: 24px;
}

.trial-balance-table {
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

    // 类别汇总行样式
    ::v-deep .el-table__row {
      &:has(.is-category) {
        background-color: #f8f9fa;
        font-weight: 600;
      }
    }
  }
}
</style>
