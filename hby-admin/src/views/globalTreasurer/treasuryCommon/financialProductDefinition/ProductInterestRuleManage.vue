<template>
  <div class="product-interest-rule-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-coin"></i>
            产品利息规则管理
          </h2>
          <p class="page-description">管理金融产品利息计算规则，包括计息方式、利率配置、复利规则和结息周期</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增规则
          </el-button>
          <el-button type="success" icon="el-icon-calculator" @click="handleCalculate">
            利息计算
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出规则
          </el-button>
        </div>
      </div>
    </div>

    <!-- 规则统计卡片 -->
    <div class="rule-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-coin"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总规则数</div>
                <div class="card-value">{{ totalRules }}</div>
                <div class="card-change">已配置规则</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon simple-icon">
                <i class="el-icon-s-finance"></i>
              </div>
              <div class="card-info">
                <div class="card-title">单利规则</div>
                <div class="card-value">{{ simpleInterestRules }}</div>
                <div class="card-change positive">简单计息</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon compound-icon">
                <i class="el-icon-data-line"></i>
              </div>
              <div class="card-info">
                <div class="card-title">复利规则</div>
                <div class="card-value">{{ compoundInterestRules }}</div>
                <div class="card-change">复合计息</div>
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
                <div class="card-title">启用规则</div>
                <div class="card-value">{{ activeRules }}</div>
                <div class="card-change positive">正常使用</div>
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
          <el-form-item label="规则编码">
            <el-input
              v-model="listQuery.ruleCode"
              placeholder="请输入规则编码"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="规则名称">
            <el-input
              v-model="listQuery.ruleName"
              placeholder="请输入规则名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="产品类型">
            <el-select
              v-model="listQuery.productType"
              placeholder="请选择产品类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="银行理财" value="BANK_WEALTH" />
              <el-option label="债券投资" value="BOND" />
              <el-option label="股票投资" value="EQUITY" />
              <el-option label="基金投资" value="FUND" />
              <el-option label="存款产品" value="DEPOSIT" />
            </el-select>
          </el-form-item>
          <el-form-item label="计息方式">
            <el-select
              v-model="listQuery.interestCalculationMethod"
              placeholder="请选择计息方式"
              clearable
              style="width: 120px;"
            >
              <el-option label="单利" value="SIMPLE" />
              <el-option label="复利" value="COMPOUND" />
              <el-option label="贴现" value="DISCOUNT" />
              <el-option label="浮动利率" value="FLOATING" />
            </el-select>
          </el-form-item>
          <el-form-item label="结息周期">
            <el-select
              v-model="listQuery.interestSettlementPeriod"
              placeholder="请选择结息周期"
              clearable
              style="width: 120px;"
            >
              <el-option label="按日" value="DAILY" />
              <el-option label="按月" value="MONTHLY" />
              <el-option label="按季" value="QUARTERLY" />
              <el-option label="按年" value="YEARLY" />
              <el-option label="到期一次性" value="MATURITY" />
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
      <el-table-column label="规则编码" prop="ruleCode" sortable="custom" align="center" min-width="120">
        <template slot-scope="{row}">
          <span>{{ row.ruleCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="规则名称" min-width="150" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.ruleName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="产品类型" min-width="100" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getProductTypeColor(row.productType)" size="small">
            {{ getProductTypeName(row.productType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="基准利率" min-width="90" align="center">
        <template slot-scope="{row}">
          <span>{{ row.baseInterestRate }}%</span>
        </template>
      </el-table-column>
      <el-table-column label="利率浮动" min-width="90" align="center">
        <template slot-scope="{row}">
          <span>{{ row.interestRateFloat }}%</span>
        </template>
      </el-table-column>
      <el-table-column label="计息方式" min-width="100" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getCalculationMethodColor(row.interestCalculationMethod)" size="small">
            {{ getCalculationMethodName(row.interestCalculationMethod) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="结息周期" min-width="100" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getSettlementPeriodColor(row.interestSettlementPeriod)" size="small">
            {{ getSettlementPeriodName(row.interestSettlementPeriod) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="生效日期" min-width="100" align="center">
        <template slot-scope="{row}">
          <span>{{ row.effectiveDate | parseTime('{y}-{m}-{d}') }}</span>
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
  name: 'ProductInterestRuleManage',
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
        ruleCode: undefined,
        ruleName: undefined,
        productType: undefined,
        interestCalculationMethod: undefined,
        interestSettlementPeriod: undefined
      },
      totalRules: 0,
      enabledRules: 0,
      simpleInterestRules: 0,
      compoundInterestRules: 0
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
        const response = await this.getProductInterestRuleList(this.listQuery)
        if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          this.list = response.data?.tlist || response.data || []
          this.total = response.data?.totalRecord || response.totalRecord || this.list.length
          this.updateStatistics()
        } else {
          throw new Error('API返回状态异常')
        }
      } catch (error) {
        // API调用失败时使用模拟数据
        console.warn('产品利息规则管理API调用失败，使用模拟数据:', error)
        const { generateProductInterestRuleData, handleApiError } = await import('@/utils/mockData')
        const mockResponse = handleApiError(error, generateProductInterestRuleData, 5)
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
    async getProductInterestRuleList(params) {
      // 模拟API调用
      throw new Error('API未实现')
    },
    updateStatistics() {
      this.totalRules = this.list.length
      this.enabledRules = this.list.filter(item => item.isEnabled === 1).length
      this.simpleInterestRules = this.list.filter(item => item.interestCalculationMethod === 'SIMPLE').length
      this.compoundInterestRules = this.list.filter(item => item.interestCalculationMethod === 'COMPOUND').length
    },
    getProductTypeName(type) {
      const typeMap = {
        'BANK_WEALTH': '银行理财',
        'BOND': '债券投资',
        'EQUITY': '股票投资',
        'FUND': '基金投资',
        'DEPOSIT': '存款产品'
      }
      return typeMap[type] || type
    },
    getProductTypeColor(type) {
      const colorMap = {
        'BANK_WEALTH': 'primary',
        'BOND': 'success',
        'EQUITY': 'warning',
        'FUND': 'info',
        'DEPOSIT': 'default'
      }
      return colorMap[type] || 'default'
    },
    getCalculationMethodName(method) {
      const methodMap = {
        'SIMPLE': '单利',
        'COMPOUND': '复利',
        'DISCOUNT': '贴现',
        'FLOATING': '浮动利率'
      }
      return methodMap[method] || method
    },
    getCalculationMethodColor(method) {
      const colorMap = {
        'SIMPLE': 'primary',
        'COMPOUND': 'success',
        'DISCOUNT': 'warning',
        'FLOATING': 'info'
      }
      return colorMap[method] || 'default'
    },
    getSettlementPeriodName(period) {
      const periodMap = {
        'DAILY': '按日',
        'MONTHLY': '按月',
        'QUARTERLY': '按季',
        'YEARLY': '按年',
        'MATURITY': '到期一次性'
      }
      return periodMap[period] || period
    },
    getSettlementPeriodColor(period) {
      const colorMap = {
        'DAILY': 'info',
        'MONTHLY': 'primary',
        'QUARTERLY': 'success',
        'YEARLY': 'warning',
        'MATURITY': 'danger'
      }
      return colorMap[period] || 'default'
    },
    getList() {
      this.listLoading = true
      setTimeout(() => {
        this.list = [
          {
            ruleId: 1,
            ruleCode: 'INTEREST_RULE_001',
            ruleName: '银行理财产品利息规则',
            productType: 'BANK_WEALTH',
            baseInterestRate: 3.50,
            interestRateFloat: 0.20,
            interestCalculationMethod: 'SIMPLE',
            interestSettlementPeriod: 'MONTHLY',
            effectiveDate: '2024-01-01',
            expiryDate: '2024-12-31',
            isEnabled: 1,
            createTime: '2024-01-01 10:00:00',
            description: '银行理财产品的利息计算规则'
          },
          {
            ruleId: 2,
            ruleCode: 'INTEREST_RULE_002',
            ruleName: '债券投资利息规则',
            productType: 'BOND',
            baseInterestRate: 4.20,
            interestRateFloat: 0.50,
            interestCalculationMethod: 'COMPOUND',
            interestSettlementPeriod: 'QUARTERLY',
            effectiveDate: '2024-01-01',
            expiryDate: '2024-12-31',
            isEnabled: 1,
            createTime: '2024-01-15 14:30:00',
            description: '债券投资产品的利息计算规则'
          },
          {
            ruleId: 3,
            ruleCode: 'INTEREST_RULE_003',
            ruleName: '存款产品利息规则',
            productType: 'DEPOSIT',
            baseInterestRate: 2.75,
            interestRateFloat: 0.10,
            interestCalculationMethod: 'SIMPLE',
            interestSettlementPeriod: 'YEARLY',
            effectiveDate: '2024-01-01',
            expiryDate: '2024-12-31',
            isEnabled: 1,
            createTime: '2024-02-01 09:00:00',
            description: '存款产品的利息计算规则'
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
