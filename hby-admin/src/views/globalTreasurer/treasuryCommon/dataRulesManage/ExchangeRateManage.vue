<template>
  <div class="exchange-rate-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-money"></i>
            汇率管理
          </h2>
          <p class="page-description">管理外汇汇率信息，包括实时汇率、历史数据、汇率预警和趋势分析</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-refresh" @click="handleSyncRates">
            同步汇率
          </el-button>
          <el-button type="success" icon="el-icon-data-line" @click="handleTrendAnalysis">
            趋势分析
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 汇率统计卡片 -->
    <div class="rate-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-info">
                <div class="card-title">支持币种</div>
                <div class="card-value">{{ totalCurrencies }}</div>
                <div class="card-change">种货币</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon usd-icon">
                <i class="el-icon-coin"></i>
              </div>
              <div class="card-info">
                <div class="card-title">美元汇率</div>
                <div class="card-value">{{ usdRate }}</div>
                <div class="card-change positive">CNY/USD</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon eur-icon">
                <i class="el-icon-wallet"></i>
              </div>
              <div class="card-info">
                <div class="card-title">欧元汇率</div>
                <div class="card-value">{{ eurRate }}</div>
                <div class="card-change">CNY/EUR</div>
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
                <div class="card-change">分钟前</div>
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
          <el-form-item label="源币种">
            <el-select
              v-model="listQuery.fromCurrency"
              placeholder="请选择源币种"
              clearable
              style="width: 120px;"
            >
              <el-option label="人民币" value="CNY" />
              <el-option label="美元" value="USD" />
              <el-option label="欧元" value="EUR" />
              <el-option label="日元" value="JPY" />
              <el-option label="英镑" value="GBP" />
              <el-option label="港币" value="HKD" />
            </el-select>
          </el-form-item>
          <el-form-item label="目标币种">
            <el-select
              v-model="listQuery.toCurrency"
              placeholder="请选择目标币种"
              clearable
              style="width: 120px;"
            >
              <el-option label="人民币" value="CNY" />
              <el-option label="美元" value="USD" />
              <el-option label="欧元" value="EUR" />
              <el-option label="日元" value="JPY" />
              <el-option label="英镑" value="GBP" />
              <el-option label="港币" value="HKD" />
            </el-select>
          </el-form-item>
          <el-form-item label="汇率来源">
            <el-select
              v-model="listQuery.rateSource"
              placeholder="请选择汇率来源"
              clearable
              style="width: 120px;"
            >
              <el-option label="央行" value="PBOC" />
              <el-option label="外汇管理局" value="SAFE" />
              <el-option label="银行" value="BANK" />
              <el-option label="第三方" value="THIRD_PARTY" />
            </el-select>
          </el-form-item>
          <el-form-item label="汇率日期">
            <el-date-picker
              v-model="listQuery.rateDate"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 240px;"
            />
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
      <el-table-column label="源币种" prop="fromCurrency" align="center" width="100">
        <template slot-scope="{row}">
          <span>{{ row.fromCurrency }}</span>
        </template>
      </el-table-column>
      <el-table-column label="目标币种" prop="toCurrency" align="center" width="100">
        <template slot-scope="{row}">
          <span>{{ row.toCurrency }}</span>
        </template>
      </el-table-column>
      <el-table-column label="汇率日期" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.rateDate | parseTime('{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="中间价" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.middleRate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="买入价" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.buyRate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="卖出价" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.sellRate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="现钞买入价" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.cashBuyRate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="现钞卖出价" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.cashSellRate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="汇率来源" width="100px" align="center">
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
import { getExchangeRateList, deleteExchangeRate } from '@/api/globalTreasurer/czgg'

export default {
  name: 'ExchangeRateManage',
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
        fromCurrency: undefined,
        toCurrency: undefined,
        rateSource: undefined,
        rateDate: undefined
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
    /**
     * 获取汇率数据列表
     */
    async fetchData() {
      this.listLoading = true
      try {
        const response = await getExchangeRateList(this.listQuery)

        // 使用与配置文件一致的成功状态码
        const successCodes = [200, 0, '200', '0', '1', 1, 2]
        if (successCodes.includes(response.code)) {
          // 支持多种数据结构格式
          if (response.data && response.data.tlist !== undefined) {
            // PageInfo格式：{ code: 200, data: { tlist: [], totalRecord: 0 } }
            this.list = response.data.tlist || []
            this.total = response.data.totalRecord || 0
          } else if (response.data && response.data.list !== undefined) {
            // 标准格式：{ code: 200, data: { list: [], total: 0 } }
            this.list = response.data.list || []
            this.total = response.data.total || 0
          } else if (response.data && Array.isArray(response.data)) {
            // 数组格式：{ code: 200, data: [] }
            this.list = response.data || []
            this.total = response.data.length || 0
          } else if (response.list !== undefined) {
            // 直接格式：{ code: 200, list: [], total: 0 }
            this.list = response.list || []
            this.total = response.total || 0
          } else {
            // 兜底处理
            this.list = []
            this.total = 0
          }
        } else {
          this.$message.error(response.message || '获取数据失败')
          this.list = []
          this.total = 0
        }
      } catch (error) {
        console.error('获取汇率数据列表失败:', error)
        // API异常时使用模拟数据
        this.list = [
          {
            rateId: 1,
            fromCurrency: 'USD',
            toCurrency: 'CNY',
            rateDate: '2024-09-25',
            middleRate: 7.1234,
            buyRate: 7.1100,
            sellRate: 7.1368,
            cashBuyRate: 7.0950,
            cashSellRate: 7.1518,
            rateSource: 'PBOC',
            isActive: 1
          },
          {
            rateId: 2,
            fromCurrency: 'EUR',
            toCurrency: 'CNY',
            rateDate: '2024-09-25',
            middleRate: 7.9456,
            buyRate: 7.9200,
            sellRate: 7.9712,
            cashBuyRate: 7.8950,
            cashSellRate: 7.9962,
            rateSource: 'PBOC',
            isActive: 1
          },
          {
            rateId: 3,
            fromCurrency: 'JPY',
            toCurrency: 'CNY',
            rateDate: '2024-09-25',
            middleRate: 0.0478,
            buyRate: 0.0476,
            sellRate: 0.0480,
            cashBuyRate: 0.0474,
            cashSellRate: 0.0482,
            rateSource: 'PBOC',
            isActive: 1
          }
        ]
        this.total = 3
        this.$message.warning('使用模拟数据，请检查网络连接')
      } finally {
        this.listLoading = false
      }
    },

    getList() {
      this.fetchData()
    },

    handleFilter() {
      this.listQuery.page = 1
      this.fetchData()
    },

    handleCreate() {
      this.$message.info('创建功能待实现')
    },

    handleUpdate(row) {
      this.$message.info('编辑功能待实现')
    },

    async handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteExchangeRate({ id: row.id })
          const successCodes = [200, 0, '200', '0', '1', 1, 2]
          if (successCodes.includes(response.code)) {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            // 重新获取数据
            this.fetchData()
          } else {
            this.$message.error(response.message || '删除失败')
          }
        } catch (error) {
          console.error('删除汇率数据失败:', error)
          this.$message.error('删除失败，请检查网络连接')
        }
      })
    }
  }
}
</script>
