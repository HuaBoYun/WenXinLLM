<template>
  <div class="detail-ledger-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-document"></i>
          明细账查询
        </h1>
        <p class="page-description">查看科目的详细发生额和余额明细记录</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-search" @click="queryDetail">
          查询明细
        </el-button>
        <el-button type="success" icon="el-icon-download" @click="exportDetail">
          导出明细
        </el-button>
        <el-button type="warning" icon="el-icon-printer" @click="printDetail">
          打印明细
        </el-button>
      </div>
    </div>

    <!-- 查询条件 -->
    <div class="search-container">
      <el-form :model="searchForm" :inline="true" label-width="80px">
        <el-form-item label="科目选择" required>
          <el-cascader
            v-model="searchForm.subjectPath"
            :options="subjectOptions"
            :props="{ value: 'code', label: 'name', children: 'children' }"
            placeholder="请选择科目"
            clearable
            filterable
            style="width: 300px">
          </el-cascader>
        </el-form-item>
        <el-form-item label="查询期间" required>
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="凭证字号">
          <el-input
            v-model="searchForm.voucherNo"
            placeholder="请输入凭证字号"
            clearable>
          </el-input>
        </el-form-item>
        <el-form-item label="摘要">
          <el-input
            v-model="searchForm.summary"
            placeholder="请输入摘要关键字"
            clearable>
          </el-input>
        </el-form-item>
        <el-form-item label="金额范围">
          <el-input
            v-model="searchForm.minAmount"
            placeholder="最小金额"
            type="number"
            style="width: 120px">
          </el-input>
          <span style="margin: 0 8px;">-</span>
          <el-input
            v-model="searchForm.maxAmount"
            placeholder="最大金额"
            type="number"
            style="width: 120px">
          </el-input>
        </el-form-item>
        <el-form-item label="辅助核算">
          <el-select v-model="searchForm.auxiliaryType" placeholder="辅助类型" clearable>
            <el-option label="客户" value="customer"></el-option>
            <el-option label="供应商" value="supplier"></el-option>
            <el-option label="部门" value="department"></el-option>
            <el-option label="项目" value="project"></el-option>
            <el-option label="存货" value="inventory"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchDetail">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 科目信息 -->
    <div class="subject-info" v-if="currentSubject">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="info-item">
            <label>科目编码：</label>
            <span>{{ currentSubject.code }}</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="info-item">
            <label>科目名称：</label>
            <span>{{ currentSubject.name }}</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="info-item">
            <label>期初余额：</label>
            <span :class="{ 'debit-amount': currentSubject.openingBalance > 0, 'credit-amount': currentSubject.openingBalance < 0 }">
              {{ formatAmount(Math.abs(currentSubject.openingBalance)) }}
              {{ currentSubject.openingBalance > 0 ? '(借)' : currentSubject.openingBalance < 0 ? '(贷)' : '' }}
            </span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="info-item">
            <label>期末余额：</label>
            <span :class="{ 'debit-amount': currentSubject.endingBalance > 0, 'credit-amount': currentSubject.endingBalance < 0 }">
              {{ formatAmount(Math.abs(currentSubject.endingBalance)) }}
              {{ currentSubject.endingBalance > 0 ? '(借)' : currentSubject.endingBalance < 0 ? '(贷)' : '' }}
            </span>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 明细账表格 -->
    <div class="detail-table">
      <el-table
        v-loading="loading"
        :data="detailList"
        stripe
        border
        style="width: 100%"
        :summary-method="getSummaries"
        show-summary>
        <el-table-column prop="voucherDate" label="凭证日期" width="100" align="center"></el-table-column>
        <el-table-column prop="voucherNo" label="凭证字号" width="120" align="center"></el-table-column>
        <el-table-column prop="summary" label="摘要" min-width="200"></el-table-column>
        <el-table-column prop="debitAmount" label="借方金额" width="120" align="right">
          <template slot-scope="scope">
            <span v-if="scope.row.debitAmount > 0" class="debit-amount">
              {{ formatAmount(scope.row.debitAmount) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="creditAmount" label="贷方金额" width="120" align="right">
          <template slot-scope="scope">
            <span v-if="scope.row.creditAmount > 0" class="credit-amount">
              {{ formatAmount(scope.row.creditAmount) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="direction" label="方向" width="60" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.debitAmount > 0 ? 'primary' : 'success'" size="mini">
              {{ scope.row.debitAmount > 0 ? '借' : '贷' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="balance" label="余额" width="120" align="right">
          <template slot-scope="scope">
            <span :class="{ 'debit-amount': scope.row.balance > 0, 'credit-amount': scope.row.balance < 0 }">
              {{ formatAmount(Math.abs(scope.row.balance)) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="balanceDirection" label="余额方向" width="80" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.balance > 0 ? 'primary' : scope.row.balance < 0 ? 'success' : 'info'" size="mini">
              {{ scope.row.balance > 0 ? '借' : scope.row.balance < 0 ? '贷' : '平' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="auxiliaryInfo" label="辅助信息" width="150">
          <template slot-scope="scope">
            <div v-if="scope.row.auxiliaryInfo">
              <div v-for="(aux, index) in scope.row.auxiliaryInfo" :key="index" class="auxiliary-item">
                <el-tag size="mini" type="info">{{ aux.type }}：{{ aux.name }}</el-tag>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="viewVoucher(scope.row)">查看凭证</el-button>
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

    <!-- 凭证查看对话框 -->
    <VoucherViewDialog
      :visible.sync="voucherDialogVisible"
      :voucher-data="selectedVoucher"
    />
  </div>
</template>

<script>
import {
  getDetailLedgerPage,
  getSubjectDetailLedger,
  exportDetailLedger
} from '@/api/financialSharing/generalLedger'
import VoucherViewDialog from './components/VoucherViewDialog'

export default {
  name: 'DetailLedgerIndex',
  components: {
    VoucherViewDialog
  },
  data() {
    return {
      loading: false,
      detailList: [],
      currentSubject: null,
      selectedVoucher: null,
      voucherDialogVisible: false,
      searchForm: {
        subjectPath: [],
        dateRange: [],
        voucherNo: '',
        summary: '',
        minAmount: '',
        maxAmount: '',
        auxiliaryType: ''
      },
      subjectOptions: [],
      pagination: {
        currentPage: 1,
        pageSize: 20,
        total: 0
      }
    }
  },
  mounted() {
    this.initDefaultDateRange()
    this.loadSubjectOptions()
    this.checkRouteParams()
  },
  methods: {
    initDefaultDateRange() {
      const now = new Date()
      const startOfMonth = new Date(now.getFullYear(), now.getMonth(), 1)
      const endOfMonth = new Date(now.getFullYear(), now.getMonth() + 1, 0)
      
      this.searchForm.dateRange = [
        startOfMonth.toISOString().split('T')[0],
        endOfMonth.toISOString().split('T')[0]
      ]
    },
    checkRouteParams() {
      const { subjectCode, period } = this.$route.query
      if (subjectCode) {
        this.searchForm.subjectPath = [subjectCode]
        if (period) {
          const year = period.split('-')[0]
          const month = period.split('-')[1]
          const startDate = new Date(year, month - 1, 1)
          const endDate = new Date(year, month, 0)
          this.searchForm.dateRange = [
            startDate.toISOString().split('T')[0],
            endDate.toISOString().split('T')[0]
          ]
        }
        this.searchDetail()
      }
    },
    loadSubjectOptions() {
      // 暂未对接 API，先以空状态展示
      this.subjectOptions = []
    },
    async searchDetail() {
      if (!this.searchForm.subjectPath || this.searchForm.subjectPath.length === 0) {
        this.$message.warning('请选择科目')
        return
      }
      
      if (!this.searchForm.dateRange || this.searchForm.dateRange.length !== 2) {
        this.$message.warning('请选择查询期间')
        return
      }

      this.loading = true
      try {
        const params = {
          pageNumber: this.pagination.currentPage,
          pageSize: this.pagination.pageSize,
          subjectCode: this.searchForm.subjectPath[this.searchForm.subjectPath.length - 1],
          startDate: this.searchForm.dateRange[0],
          endDate: this.searchForm.dateRange[1],
          voucherNo: this.searchForm.voucherNo,
          summary: this.searchForm.summary,
          minAmount: this.searchForm.minAmount,
          maxAmount: this.searchForm.maxAmount,
          auxiliaryType: this.searchForm.auxiliaryType
        }
        
        const response = await getDetailLedgerPage(params)
        if (response.code === 200) {
          this.detailList = response.data && response.data.records ? response.data.records : []
          this.pagination.total = response.data && response.data.total ? response.data.total : 0
          this.updateCurrentSubject()
        }
      } catch (error) {
        this.$message.error('查询明细账失败：' + error.message)
        // 数据加载失败时的空状态降级（不再使用模拟数据）
        this.detailList = []
        this.pagination.total = 0
        this.updateCurrentSubject()
      } finally {
        this.loading = false
      }
    },
    generateMockDetailData() {
      // 暂未对接 API，先以空状态展示
      return []
    },
    updateCurrentSubject() {
      const subjectCode = this.searchForm.subjectPath[this.searchForm.subjectPath.length - 1]
      const subject = this.findSubjectByCode(subjectCode)
      
      if (subject) {
        this.currentSubject = {
          code: subject.code,
          name: subject.name,
          openingBalance: 100000,
          endingBalance: this.detailList.length > 0 ? this.detailList[this.detailList.length - 1].balance : 100000
        }
      }
    },
    findSubjectByCode(code) {
      const findInTree = (nodes) => {
        for (const node of nodes) {
          if (node.code === code) {
            return node
          }
          if (node.children && node.children.length > 0) {
            const found = findInTree(node.children)
            if (found) return found
          }
        }
        return null
      }
      
      return findInTree(this.subjectOptions)
    },
    resetSearch() {
      this.searchForm = {
        subjectPath: [],
        dateRange: [],
        voucherNo: '',
        summary: '',
        minAmount: '',
        maxAmount: '',
        auxiliaryType: ''
      }
      this.initDefaultDateRange()
      this.detailList = []
      this.currentSubject = null
    },
    queryDetail() {
      this.pagination.currentPage = 1
      this.searchDetail()
    },
    async exportDetail() {
      try {
        const response = await exportDetailLedger(this.searchForm)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    printDetail() {
      window.print()
    },
    viewVoucher(row) {
      this.selectedVoucher = row
      this.voucherDialogVisible = true
    },
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.searchDetail()
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.searchDetail()
    },
    formatAmount(amount) {
      if (amount >= 10000) {
        return (amount / 10000).toFixed(2) + '万'
      }
      return amount.toLocaleString()
    },
    getSummaries(param) {
      const { columns, data } = param
      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计'
          return
        }
        if (index === 1 || index === 2) {
          sums[index] = ''
          return
        }
        
        if (column.property === 'debitAmount' || column.property === 'creditAmount') {
          const values = data.map(item => Number(item[column.property]))
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
.detail-ledger-container {
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
        color: #67c23a;
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

.subject-info {
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .info-item {
    font-size: 14px;
    margin-bottom: 8px;

    label {
      color: #606266;
      margin-right: 8px;
      font-weight: 600;
    }

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

.detail-table {
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

    .auxiliary-item {
      margin-bottom: 4px;

      &:last-child {
        margin-bottom: 0;
      }
    }
  }
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}
</style>
