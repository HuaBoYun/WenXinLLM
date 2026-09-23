<template>
  <div class="app-container">
    <!-- 面包屑导航 -->
    <el-breadcrumb class="breadcrumb-container" separator="/">
      <el-breadcrumb-item>
        <router-link to="/globalTreasurer/zjjh">资金计划</router-link>
      </el-breadcrumb-item>
      <el-breadcrumb-item>计划明细</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 计划信息卡片 -->
    <el-card class="plan-info-card" shadow="never">
      <div slot="header" class="clearfix">
        <span>计划信息</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="info-item">
            <span class="label">计划编号：</span>
            <span class="value">{{ planInfo.planNo }}</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="info-item">
            <span class="label">计划名称：</span>
            <span class="value">{{ planInfo.planName }}</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="info-item">
            <span class="label">计划类型：</span>
            <el-tag :type="planTypeTagMap[planInfo.planType]">
              {{ planTypeMap[planInfo.planType] }}
            </el-tag>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="info-item">
            <span class="label">计划状态：</span>
            <el-tag :type="planStatusTagMap[planInfo.planStatus]">
              {{ planStatusMap[planInfo.planStatus] }}
            </el-tag>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top: 15px;">
        <el-col :span="6">
          <div class="info-item">
            <span class="label">计划期间：</span>
            <span class="value">{{ planInfo.startDate }} 至 {{ planInfo.endDate }}</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="info-item">
            <span class="label">总收入：</span>
            <span class="value text-success">{{ formatAmount(planInfo.totalIncome) }}</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="info-item">
            <span class="label">总支出：</span>
            <span class="value text-danger">{{ formatAmount(planInfo.totalExpense) }}</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="info-item">
            <span class="label">净现金流：</span>
            <span :class="planInfo.netCashFlow >= 0 ? 'value text-success' : 'value text-danger'">
              {{ formatAmount(planInfo.netCashFlow) }}
            </span>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 查询条件 -->
    <div class="filter-container">
      <el-select
        v-model="listQuery.businessType"
        placeholder="业务类型"
        clearable
        style="width: 150px"
        class="filter-item"
      >
        <el-option
          v-for="item in businessTypeOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-select
        v-model="listQuery.executionStatus"
        placeholder="执行状态"
        clearable
        style="width: 150px"
        class="filter-item"
      >
        <el-option
          v-for="item in executionStatusOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-date-picker
        v-model="dateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        class="filter-item"
        style="width: 240px"
        @change="handleDateRangeChange"
      />
      <el-button
        v-waves
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >
        搜索
      </el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-edit"
        @click="handleCreate"
      >
        添加明细
      </el-button>
      <el-button
        v-waves
        class="filter-item"
        type="success"
        icon="el-icon-pie-chart"
        @click="showAnalysis"
      >
        分析报告
      </el-button>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.totalCount || 0 }}</div>
            <div class="statistics-label">明细总数</div>
          </div>
          <i class="el-icon-document statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ formatAmount(summaryInfo.totalPlannedAmount) }}</div>
            <div class="statistics-label">计划金额</div>
          </div>
          <i class="el-icon-money statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ formatAmount(summaryInfo.totalActualAmount) }}</div>
            <div class="statistics-label">实际金额</div>
          </div>
          <i class="el-icon-coin statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.avgExecutionRate || 0 }}%</div>
            <div class="statistics-label">平均执行率</div>
          </div>
          <i class="el-icon-data-analysis statistics-icon"></i>
        </el-card>
      </el-col>
    </el-row>

    <!-- 表格 -->
    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange"
    >
      <el-table-column
        label="业务类型"
        prop="businessType"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag :type="businessTypeTagMap[row.businessType]">
            {{ businessTypeMap[row.businessType] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="业务项目"
        prop="businessItem"
        width="200"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ row.businessItem }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="计划日期"
        prop="plannedDate"
        width="120"
        align="center"
        sortable="custom"
      >
        <template slot-scope="{row}">
          <span>{{ row.plannedDate }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="计划金额"
        prop="plannedAmount"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ formatAmount(row.plannedAmount) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="实际日期"
        prop="actualDate"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ row.actualDate || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="实际金额"
        prop="actualAmount"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ row.actualAmount ? formatAmount(row.actualAmount) : '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="差异金额"
        prop="varianceAmount"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span v-if="row.varianceAmount !== null" 
                :class="row.varianceAmount >= 0 ? 'text-success' : 'text-danger'">
            {{ formatAmount(row.varianceAmount) }}
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column
        label="差异率"
        prop="varianceRate"
        width="100"
        align="center"
      >
        <template slot-scope="{row}">
          <span v-if="row.varianceRate !== null" 
                :class="Math.abs(row.varianceRate) > 10 ? 'text-danger' : 'text-success'">
            {{ row.varianceRate }}%
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column
        label="执行状态"
        prop="executionStatus"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag :type="executionStatusTagMap[row.executionStatus]">
            {{ executionStatusMap[row.executionStatus] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="执行率"
        prop="executionRate"
        width="100"
        align="center"
      >
        <template slot-scope="{row}">
          <span v-if="row.executionRate !== null">{{ row.executionRate }}%</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column
        label="币种"
        prop="currencyCode"
        width="80"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ row.currencyCode || 'CNY' }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        width="200"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button
            v-if="row.executionStatus === 'PENDING'"
            size="mini"
            type="success"
            @click="handleExecute(row)"
          >
            执行
          </el-button>
          <el-button
            v-if="['PENDING', 'PROCESSING'].includes(row.executionStatus)"
            size="mini"
            type="danger"
            @click="handleDelete(row, $index)"
          >
            删除
          </el-button>
          <el-dropdown
            trigger="click"
            @command="(command) => handleCommand(command, row)"
          >
            <el-button size="mini">
              更多<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="detail">查看详情</el-dropdown-item>
              <el-dropdown-item command="copy">复制明细</el-dropdown-item>
              <el-dropdown-item
                v-if="row.executionStatus === 'EXECUTED'"
                command="adjust"
              >
                调整明细
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { getFundPlanDetailPage, createFundPlanDetail, updateFundPlanDetail, deleteFundPlanDetail,
         getFundPlanDetailSummary, getFundPlanDetailVarianceAnalysis, getFundPlanDetailExecutionAnalysis } from '@/api/globalTreasurer/zjjh'
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'

export default {
  name: 'FundPlanDetail',
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        planId: null,
        businessType: undefined,
        executionStatus: undefined,
        startDate: undefined,
        endDate: undefined,
        sort: '+plannedDate'
      },
      dateRange: [],
      planInfo: {},
      summaryInfo: {},
      businessTypeOptions: [
        { label: '收入', value: 'INCOME' },
        { label: '支出', value: 'EXPENSE' },
        { label: '投资', value: 'INVESTMENT' },
        { label: '融资', value: 'FINANCING' },
        { label: '其他', value: 'OTHER' }
      ],
      executionStatusOptions: [
        { label: '待执行', value: 'PENDING' },
        { label: '执行中', value: 'PROCESSING' },
        { label: '已执行', value: 'EXECUTED' },
        { label: '已取消', value: 'CANCELLED' }
      ],
      businessTypeMap: {
        'INCOME': '收入',
        'EXPENSE': '支出',
        'INVESTMENT': '投资',
        'FINANCING': '融资',
        'OTHER': '其他'
      },
      businessTypeTagMap: {
        'INCOME': 'success',
        'EXPENSE': 'danger',
        'INVESTMENT': 'warning',
        'FINANCING': 'info',
        'OTHER': ''
      },
      executionStatusMap: {
        'PENDING': '待执行',
        'PROCESSING': '执行中',
        'EXECUTED': '已执行',
        'CANCELLED': '已取消'
      },
      executionStatusTagMap: {
        'PENDING': 'warning',
        'PROCESSING': 'info',
        'EXECUTED': 'success',
        'CANCELLED': 'danger'
      },
      planTypeMap: {
        'ANNUAL': '年度计划',
        'QUARTERLY': '季度计划',
        'MONTHLY': '月度计划',
        'WEEKLY': '周计划',
        'DAILY': '日计划'
      },
      planTypeTagMap: {
        'ANNUAL': 'danger',
        'QUARTERLY': 'warning',
        'MONTHLY': 'info',
        'WEEKLY': 'success',
        'DAILY': ''
      },
      planStatusMap: {
        'DRAFT': '草稿',
        'SUBMITTED': '已提交',
        'APPROVED': '已审批',
        'EXECUTING': '执行中',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      },
      planStatusTagMap: {
        'DRAFT': '',
        'SUBMITTED': 'warning',
        'APPROVED': 'info',
        'EXECUTING': 'success',
        'COMPLETED': 'success',
        'CANCELLED': 'danger'
      }
    }
  },
  created() {
    this.listQuery.planId = this.$route.params.planId
    this.getPlanInfo()
    this.getList()
    this.getSummaryInfo()
  },
  methods: {
    getList() {
      this.listLoading = true
      getFundPlanDetailPage(this.listQuery).then(response => {
        if (response.code === 200) {
          this.list = response.data.records
          this.total = response.data.total
        } else {
          this.$message.error(response.message || '查询失败')
        }
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
      })
    },
    getPlanInfo() {
      // 获取计划基本信息
      // 这里应该调用获取计划详情的API
      this.planInfo = {
        planNo: 'FP202501001',
        planName: '2025年第一季度资金计划',
        planType: 'QUARTERLY',
        planStatus: 'EXECUTING',
        startDate: '2025-01-01',
        endDate: '2025-03-31',
        totalIncome: 10000000,
        totalExpense: 8000000,
        netCashFlow: 2000000
      }
    },
    getSummaryInfo() {
      getFundPlanDetailSummary({ planId: this.listQuery.planId }).then(response => {
        if (response.code === 200) {
          this.summaryInfo = response.data
        }
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleDateRangeChange(val) {
      if (val) {
        this.listQuery.startDate = val[0]
        this.listQuery.endDate = val[1]
      } else {
        this.listQuery.startDate = undefined
        this.listQuery.endDate = undefined
      }
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'plannedDate') {
        this.sortByPlannedDate(order)
      }
    },
    sortByPlannedDate(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+plannedDate'
      } else {
        this.listQuery.sort = '-plannedDate'
      }
      this.handleFilter()
    },
    handleCreate() {
      this.$router.push(`/globalTreasurer/zjjh/detail/create?planId=${this.listQuery.planId}`)
    },
    handleUpdate(row) {
      this.$router.push(`/globalTreasurer/zjjh/detail/edit/${row.detailId}`)
    },
    handleExecute(row) {
      this.$confirm('确认执行该明细项目?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 调用执行API
        this.$message.success('执行成功')
        this.getList()
      })
    },
    handleDelete(row, index) {
      this.$confirm('确认删除该明细?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteFundPlanDetail(row.detailId).then(response => {
          if (response.code === 200) {
            this.$message.success('删除成功')
            this.list.splice(index, 1)
            this.total--
            this.getSummaryInfo()
          } else {
            this.$message.error(response.message || '删除失败')
          }
        })
      })
    },
    handleCommand(command, row) {
      switch (command) {
        case 'detail':
          this.showDetail(row)
          break
        case 'copy':
          this.copyDetail(row)
          break
        case 'adjust':
          this.adjustDetail(row)
          break
      }
    },
    showDetail(row) {
      this.$router.push(`/globalTreasurer/zjjh/detail/view/${row.detailId}`)
    },
    copyDetail(row) {
      const newDetail = { ...row }
      delete newDetail.detailId
      newDetail.businessItem = `${row.businessItem}(复制)`
      this.$router.push({
        path: `/globalTreasurer/zjjh/detail/create`,
        query: { planId: this.listQuery.planId, copyData: JSON.stringify(newDetail) }
      })
    },
    adjustDetail(row) {
      this.$router.push(`/globalTreasurer/zjjh/detail/adjust/${row.detailId}`)
    },
    showAnalysis() {
      this.$router.push(`/globalTreasurer/zjjh/analysis/${this.listQuery.planId}`)
    },
    formatAmount(amount) {
      if (amount === null || amount === undefined) return '-'
      return new Intl.NumberFormat('zh-CN', {
        style: 'currency',
        currency: 'CNY',
        minimumFractionDigits: 2
      }).format(amount)
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.breadcrumb-container {
  margin-bottom: 20px;
}

.plan-info-card {
  margin-bottom: 20px;

  .info-item {
    display: flex;
    align-items: center;
    margin-bottom: 8px;

    .label {
      font-weight: 500;
      color: #606266;
      min-width: 80px;
    }

    .value {
      color: #303133;

      &.text-success {
        color: #67C23A;
        font-weight: 500;
      }

      &.text-danger {
        color: #F56C6C;
        font-weight: 500;
      }
    }
  }
}

.filter-container {
  padding: 10px 0;
  margin-bottom: 20px;

  .filter-item {
    display: inline-block;
    vertical-align: middle;
    margin-bottom: 10px;
    margin-right: 10px;
  }
}

.statistics-row {
  margin-bottom: 20px;
}

.statistics-card {
  position: relative;
  overflow: hidden;

  .statistics-content {
    padding: 20px;

    .statistics-value {
      font-size: 28px;
      font-weight: bold;
      color: #303133;
      line-height: 1;
      margin-bottom: 8px;
    }

    .statistics-label {
      font-size: 14px;
      color: #909399;
    }
  }

  .statistics-icon {
    position: absolute;
    right: 20px;
    top: 50%;
    transform: translateY(-50%);
    font-size: 40px;
    color: #E4E7ED;
  }
}

.text-success {
  color: #67C23A !important;
}

.text-danger {
  color: #F56C6C !important;
}

.text-warning {
  color: #E6A23C !important;
}

.text-info {
  color: #909399 !important;
}

::v-deep .el-table {
  .text-success {
    color: #67C23A;
  }

  .text-danger {
    color: #F56C6C;
  }

  .text-warning {
    color: #E6A23C;
  }
}
</style>
