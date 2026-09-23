<template>
  <div class="interest-rate-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-money"></i>
            利率管理
          </h2>
          <p class="page-description">管理各类金融产品利率配置，包括基准利率、浮动利率和历史利率数据</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增利率
          </el-button>
          <el-button type="success" icon="el-icon-refresh" @click="handleSyncRates">
            同步利率
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 利率概览卡片 -->
    <div class="rate-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon benchmark-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-info">
                <div class="card-title">基准利率</div>
                <div class="card-value">{{ benchmarkRate }}%</div>
                <div class="card-change">央行基准</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon deposit-icon">
                <i class="el-icon-coin"></i>
              </div>
              <div class="card-info">
                <div class="card-title">存款利率</div>
                <div class="card-value">{{ depositRate }}%</div>
                <div class="card-change positive">年化收益</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon loan-icon">
                <i class="el-icon-wallet"></i>
              </div>
              <div class="card-info">
                <div class="card-title">贷款利率</div>
                <div class="card-value">{{ loanRate }}%</div>
                <div class="card-change negative">年化成本</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon update-icon">
                <i class="el-icon-time"></i>
              </div>
              <div class="card-info">
                <div class="card-title">最后更新</div>
                <div class="card-value">{{ lastUpdateTime }}</div>
                <div class="card-change">利率数据</div>
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
          <el-form-item label="利率类型">
            <el-select
              v-model="listQuery.rateType"
              placeholder="请选择利率类型"
              clearable
              style="width: 120px;"
            >
              <el-option label="存款" value="DEPOSIT" />
              <el-option label="贷款" value="LOAN" />
              <el-option label="同业" value="INTERBANK" />
              <el-option label="债券" value="BOND" />
            </el-select>
          </el-form-item>
          <el-form-item label="币种">
            <el-select
              v-model="listQuery.currencyCode"
              placeholder="请选择币种"
              clearable
              style="width: 100px;"
            >
              <el-option label="人民币" value="CNY" />
              <el-option label="美元" value="USD" />
              <el-option label="欧元" value="EUR" />
              <el-option label="日元" value="JPY" />
              <el-option label="英镑" value="GBP" />
              <el-option label="港币" value="HKD" />
            </el-select>
          </el-form-item>
          <el-form-item label="期限">
            <el-input
              v-model="listQuery.term"
              placeholder="请输入期限"
              style="width: 120px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="期限单位">
            <el-select
              v-model="listQuery.termUnit"
              placeholder="请选择期限单位"
              clearable
              style="width: 100px;"
            >
              <el-option label="天" value="DAY" />
              <el-option label="月" value="MONTH" />
              <el-option label="年" value="YEAR" />
            </el-select>
          </el-form-item>
          <el-form-item label="利率来源">
            <el-select
              v-model="listQuery.rateSource"
              placeholder="请选择利率来源"
              clearable
              style="width: 120px;"
            >
              <el-option label="央行" value="PBOC" />
              <el-option label="银行" value="BANK" />
              <el-option label="市场" value="MARKET" />
              <el-option label="第三方" value="THIRD_PARTY" />
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
      <el-table-column label="利率类型" prop="rateType" align="center" width="100">
        <template slot-scope="{row}">
          <el-tag :type="getRateTypeColor(row.rateType)">
            {{ getRateTypeName(row.rateType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="币种" prop="currencyCode" align="center" width="80">
        <template slot-scope="{row}">
          <span>{{ row.currencyCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="期限" width="100px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.term }}{{ getTermUnitName(row.termUnit) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="利率" width="120px" align="center">
        <template slot-scope="{row}">
          <span class="rate-value">{{ row.interestRate }}%</span>
        </template>
      </el-table-column>
      <el-table-column label="利率日期" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.rateDate | parseTime('{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="利率来源" width="100px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.rateSource }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" width="80">
        <template slot-scope="{row}">
          <el-tag :type="row.isActive === 1 ? 'success' : 'danger'">
            {{ row.isActive === 1 ? '启用' : '停用' }}
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
  name: 'InterestRateManage',
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
        rateType: undefined,
        currencyCode: undefined,
        term: undefined,
        termUnit: undefined,
        rateSource: undefined
      },
      totalRates: 0,
      todayRates: 0,
      activeRates: 0,
      updateCount: 0
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getRateTypeName(type) {
      const typeMap = {
        'DEPOSIT': '存款',
        'LOAN': '贷款',
        'INTERBANK': '同业',
        'BOND': '债券'
      }
      return typeMap[type] || type
    },
    getRateTypeColor(type) {
      const colorMap = {
        'DEPOSIT': 'success',
        'LOAN': 'warning',
        'INTERBANK': 'info',
        'BOND': 'primary'
      }
      return colorMap[type] || 'default'
    },
    getTermUnitName(unit) {
      const unitMap = {
        'DAY': '天',
        'MONTH': '月',
        'YEAR': '年'
      }
      return unitMap[unit] || unit
    },
    getList() {
      this.listLoading = true
      setTimeout(() => {
        this.list = [
          {
            rateId: 1,
            rateType: 'DEPOSIT',
            currencyCode: 'CNY',
            term: '3',
            termUnit: 'MONTH',
            interestRate: 2.75,
            rateDate: '2024-09-25',
            rateSource: 'PBOC',
            isActive: 1
          },
          {
            rateId: 2,
            rateType: 'LOAN',
            currencyCode: 'CNY',
            term: '1',
            termUnit: 'YEAR',
            interestRate: 4.35,
            rateDate: '2024-09-25',
            rateSource: 'PBOC',
            isActive: 1
          },
          {
            rateId: 3,
            rateType: 'INTERBANK',
            currencyCode: 'CNY',
            term: '7',
            termUnit: 'DAY',
            interestRate: 2.15,
            rateDate: '2024-09-25',
            rateSource: 'MARKET',
            isActive: 1
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
