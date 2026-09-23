<template>
  <div class="transaction-type-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-s-finance"></i>
            交易类型管理
          </h2>
          <p class="page-description">管理金融交易类型定义，包括交易分类、交易规则和业务流程配置</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增交易类型
          </el-button>
          <el-button type="success" icon="el-icon-sort" @click="handleSort">
            排序管理
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出配置
          </el-button>
        </div>
      </div>
    </div>

    <!-- 交易类型统计卡片 -->
    <div class="transaction-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-s-finance"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总交易类型</div>
                <div class="card-value">{{ totalTransactionTypes }}</div>
                <div class="card-change">已配置类型</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon active-icon">
                <i class="el-icon-success"></i>
              </div>
              <div class="card-info">
                <div class="card-title">启用类型</div>
                <div class="card-value">{{ activeTransactionTypes }}</div>
                <div class="card-change positive">正常使用</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon category-icon">
                <i class="el-icon-menu"></i>
              </div>
              <div class="card-info">
                <div class="card-title">交易分类</div>
                <div class="card-value">{{ transactionCategories }}</div>
                <div class="card-change">分类数量</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon usage-icon">
                <i class="el-icon-data-line"></i>
              </div>
              <div class="card-info">
                <div class="card-title">今日交易</div>
                <div class="card-value">{{ todayTransactions }}</div>
                <div class="card-change">交易笔数</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="交易类型编码">
            <el-input
              v-model="listQuery.transactionTypeCode"
              placeholder="请输入交易类型编码"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="交易类型名称">
            <el-input
              v-model="listQuery.transactionTypeName"
              placeholder="请输入交易类型名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="交易分类">
            <el-select
              v-model="listQuery.transactionCategory"
              placeholder="请选择交易分类"
              clearable
              style="width: 150px;"
            >
              <el-option label="投资申购" value="INVESTMENT_PURCHASE" />
              <el-option label="投资赎回" value="INVESTMENT_REDEMPTION" />
              <el-option label="资金划转" value="FUND_TRANSFER" />
              <el-option label="利息收入" value="INTEREST_INCOME" />
              <el-option label="本金回收" value="PRINCIPAL_RECOVERY" />
              <el-option label="费用支出" value="FEE_EXPENSE" />
            </el-select>
          </el-form-item>
          <el-form-item label="交易方向">
            <el-select
              v-model="listQuery.transactionDirection"
              placeholder="请选择交易方向"
              clearable
              style="width: 120px;"
            >
              <el-option label="收入" value="INCOME" />
              <el-option label="支出" value="EXPENSE" />
              <el-option label="双向" value="BOTH" />
            </el-select>
          </el-form-item>
          <el-form-item label="风险等级">
            <el-select
              v-model="listQuery.riskLevel"
              placeholder="请选择风险等级"
              clearable
              style="width: 120px;"
            >
              <el-option label="低风险" value="LOW" />
              <el-option label="中风险" value="MEDIUM" />
              <el-option label="高风险" value="HIGH" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button icon="el-icon-refresh" @click="handleReset">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="交易类型编码" prop="transactionTypeCode" sortable="custom" align="center" min-width="140">
        <template slot-scope="{row}">
          <span>{{ row.transactionTypeCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="交易类型名称" min-width="150" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.transactionTypeName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="交易分类" min-width="120" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getTransactionCategoryColor(row.transactionCategory)" size="small">
            {{ getTransactionCategoryName(row.transactionCategory) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="交易方向" min-width="90" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getTransactionDirectionColor(row.transactionDirection)" size="small">
            {{ getTransactionDirectionName(row.transactionDirection) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="风险等级" min-width="90" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getRiskLevelColor(row.riskLevel)" size="small">
            {{ getRiskLevelName(row.riskLevel) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="会计科目" min-width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.accountingSubject }}</span>
        </template>
      </el-table-column>
      <el-table-column label="审批要求" min-width="90" align="center">
        <template slot-scope="{row}">
          <el-tag :type="row.requiresApproval ? 'warning' : 'success'" size="small">
            {{ row.requiresApproval ? '需审批' : '无需审批' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="限额控制" min-width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.limitAmount | currency }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" min-width="80" align="center">
        <template slot-scope="{row}">
          <el-tag :type="row.isEnabled === 1 ? 'success' : 'danger'">
            {{ row.isEnabled === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button v-if="row.status!='deleted'" size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'

export default {
  name: 'TransactionTypeManage',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: 'success',
        0: 'info'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        transactionTypeCode: undefined,
        transactionTypeName: undefined,
        transactionCategory: undefined,
        transactionDirection: undefined,
        riskLevel: undefined
      },
      totalTypes: 0,
      enabledTypes: 0,
      incomeTypes: 0,
      expenseTypes: 0
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        // 尝试调用真实API
        const response = await this.getTransactionTypeList(this.listQuery)
        if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          this.list = response.data?.tlist || response.data || []
          this.total = response.data?.totalRecord || response.totalRecord || this.list.length
          this.updateStatistics()
        } else {
          throw new Error('API返回状态异常')
        }
      } catch (error) {
        // API调用失败时使用模拟数据
        console.warn('交易类型管理API调用失败，使用模拟数据:', error)
        const { generateTransactionTypeData, handleApiError } = await import('@/utils/mockData')
        const mockResponse = handleApiError(error, generateTransactionTypeData, 6)
        this.list = mockResponse.data.tlist
        this.total = mockResponse.data.totalRecord
        this.updateStatistics()

        // 显示友好提示
        this.$message({
          message: '当前显示模拟数据，请检查网络连接或联系管理员',
          type: 'warning',
          duration: 3000
        })
      } finally {
        this.listLoading = false
      }
    },
    async getTransactionTypeList(params) {
      // 模拟API调用
      throw new Error('API未实现')
    },
    updateStatistics() {
      this.totalTypes = this.list.length
      this.enabledTypes = this.list.filter(item => item.isEnabled === 1).length
      this.incomeTypes = this.list.filter(item => item.transactionDirection === 'IN').length
      this.expenseTypes = this.list.filter(item => item.transactionDirection === 'OUT').length
    },
    getTransactionCategoryName(category) {
      const categoryMap = {
        'INVESTMENT_PURCHASE': '投资申购',
        'INVESTMENT_REDEMPTION': '投资赎回',
        'FUND_TRANSFER': '资金划转',
        'INTEREST_INCOME': '利息收入',
        'PRINCIPAL_RECOVERY': '本金回收',
        'FEE_EXPENSE': '费用支出'
      }
      return categoryMap[category] || category
    },
    getTransactionCategoryColor(category) {
      const colorMap = {
        'INVESTMENT_PURCHASE': 'primary',
        'INVESTMENT_REDEMPTION': 'warning',
        'FUND_TRANSFER': 'info',
        'INTEREST_INCOME': 'success',
        'PRINCIPAL_RECOVERY': 'success',
        'FEE_EXPENSE': 'danger'
      }
      return colorMap[category] || 'default'
    },
    getTransactionDirectionName(direction) {
      const directionMap = {
        'INCOME': '收入',
        'EXPENSE': '支出',
        'BOTH': '双向'
      }
      return directionMap[direction] || direction
    },
    getTransactionDirectionColor(direction) {
      const colorMap = {
        'INCOME': 'success',
        'EXPENSE': 'danger',
        'BOTH': 'info'
      }
      return colorMap[direction] || 'default'
    },
    getRiskLevelName(level) {
      const levelMap = {
        'LOW': '低风险',
        'MEDIUM': '中风险',
        'HIGH': '高风险'
      }
      return levelMap[level] || level
    },
    getRiskLevelColor(level) {
      const colorMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger'
      }
      return colorMap[level] || 'default'
    },
    getList() {
      this.listLoading = true
      setTimeout(() => {
        this.list = [
          {
            transactionTypeId: 1,
            transactionTypeCode: 'TXN_INVEST_PURCHASE',
            transactionTypeName: '投资申购交易',
            transactionCategory: 'INVESTMENT_PURCHASE',
            transactionDirection: 'EXPENSE',
            riskLevel: 'MEDIUM',
            accountingSubject: '1503',
            requiresApproval: true,
            limitAmount: 10000000.00,
            isEnabled: 1,
            createTime: '2024-01-01 10:00:00',
            description: '投资产品申购交易类型'
          },
          {
            transactionTypeId: 2,
            transactionTypeCode: 'TXN_INVEST_REDEMPTION',
            transactionTypeName: '投资赎回交易',
            transactionCategory: 'INVESTMENT_REDEMPTION',
            transactionDirection: 'INCOME',
            riskLevel: 'LOW',
            accountingSubject: '1101',
            requiresApproval: false,
            limitAmount: 50000000.00,
            isEnabled: 1,
            createTime: '2024-01-15 14:30:00',
            description: '投资产品赎回交易类型'
          },
          {
            transactionTypeId: 3,
            transactionTypeCode: 'TXN_INTEREST_INCOME',
            transactionTypeName: '利息收入交易',
            transactionCategory: 'INTEREST_INCOME',
            transactionDirection: 'INCOME',
            riskLevel: 'LOW',
            accountingSubject: '6051',
            requiresApproval: false,
            limitAmount: 0.00,
            isEnabled: 1,
            createTime: '2024-02-01 09:00:00',
            description: '投资产品利息收入交易类型'
          }
        ]
        this.total = 3
        this.listLoading = false
      }, 1000)
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleCreate() {
      this.$message.info('创建功能待实现')
    },
    handleUpdate(row) {
      this.$message.info('编辑功能待实现')
    },
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.list.splice(index, 1)
        this.$notify({
          title: '成功',
          message: '删除成功',
          type: 'success',
          duration: 2000
        })
      })
    }
  }
}
</script>
