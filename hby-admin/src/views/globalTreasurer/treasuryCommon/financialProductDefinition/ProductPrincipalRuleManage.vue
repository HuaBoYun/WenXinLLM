<template>
  <div class="product-principal-rule-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-wallet"></i>
            产品本金规则管理
          </h2>
          <p class="page-description">管理金融产品本金处理规则，包括本金归还方式、分期规则、提前还款和违约处理</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增规则
          </el-button>
          <el-button type="success" icon="el-icon-calculator" @click="handleCalculate">
            本金计算
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
                <i class="el-icon-wallet"></i>
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
              <div class="card-icon lump-sum-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-info">
                <div class="card-title">一次性还款</div>
                <div class="card-value">{{ lumpSumRules }}</div>
                <div class="card-change positive">到期还本</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon installment-icon">
                <i class="el-icon-data-line"></i>
              </div>
              <div class="card-info">
                <div class="card-title">分期还款</div>
                <div class="card-value">{{ installmentRules }}</div>
                <div class="card-change">分期规则</div>
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
          <el-form-item label="本金处理方式">
            <el-select
              v-model="listQuery.principalHandlingMethod"
              placeholder="请选择本金处理方式"
              clearable
              style="width: 150px;"
            >
              <el-option label="到期一次性返还" value="MATURITY_LUMP_SUM" />
              <el-option label="分期返还" value="INSTALLMENT" />
              <el-option label="随时赎回" value="ANYTIME_REDEMPTION" />
              <el-option label="保本保息" value="PRINCIPAL_GUARANTEED" />
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
      <el-table-column label="规则编码" prop="ruleCode" sortable="custom" align="center" width="150">
        <template slot-scope="{row}">
          <span>{{ row.ruleCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="规则名称" width="200px" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.ruleName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="产品类型" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getProductTypeColor(row.productType)" size="small">
            {{ getProductTypeName(row.productType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="本金处理方式" width="150px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getPrincipalHandlingColor(row.principalHandlingMethod)" size="small">
            {{ getPrincipalHandlingName(row.principalHandlingMethod) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="风险等级" width="100px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getRiskLevelColor(row.riskLevel)" size="small">
            {{ getRiskLevelName(row.riskLevel) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="最小投资金额" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.minInvestmentAmount | currency }}</span>
        </template>
      </el-table-column>
      <el-table-column label="最大投资金额" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.maxInvestmentAmount | currency }}</span>
        </template>
      </el-table-column>
      <el-table-column label="本金保障率" width="100px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.principalGuaranteeRate }}%</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.createTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" width="100">
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
  name: 'ProductPrincipalRuleManage',
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
        name: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      setTimeout(() => {
        this.list = [
          {
            id: 1,
            name: '示例数据',
            description: '这是一个示例数据',
            status: 1
          }
        ]
        this.total = 1
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
