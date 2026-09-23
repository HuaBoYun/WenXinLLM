<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>资金预测管理</h2>
      <p>基于历史数据和多种预测模型，为资金计划提供科学的预测支持</p>
    </div>

    <!-- 查询条件 -->
    <div class="filter-container">
      <el-select
        v-model="listQuery.forecastType"
        placeholder="预测类型"
        clearable
        style="width: 150px"
        class="filter-item"
      >
        <el-option
          v-for="item in forecastTypeOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-select
        v-model="listQuery.forecastMethod"
        placeholder="预测方法"
        clearable
        style="width: 150px"
        class="filter-item"
      >
        <el-option
          v-for="item in forecastMethodOptions"
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
        新建预测
      </el-button>
      <el-button
        v-waves
        class="filter-item"
        type="success"
        icon="el-icon-data-analysis"
        @click="showAccuracyAnalysis"
      >
        准确率分析
      </el-button>
      <el-button
        v-waves
        class="filter-item"
        type="warning"
        icon="el-icon-trend-charts"
        @click="showTrendAnalysis"
      >
        趋势分析
      </el-button>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.totalCount || 0 }}</div>
            <div class="statistics-label">预测总数</div>
          </div>
          <i class="el-icon-data-line statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.verifiedCount || 0 }}</div>
            <div class="statistics-label">已验证预测</div>
          </div>
          <i class="el-icon-circle-check statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.avgAccuracy || 0 }}%</div>
            <div class="statistics-label">平均准确率</div>
          </div>
          <i class="el-icon-data-analysis statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.highAccuracyCount || 0 }}</div>
            <div class="statistics-label">高准确率预测</div>
          </div>
          <i class="el-icon-trophy statistics-icon"></i>
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
        label="预测编号"
        prop="forecastNo"
        width="140"
        align="center"
      >
        <template slot-scope="{row}">
          <el-link type="primary" @click="showDetail(row)">
            {{ row.forecastNo }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column
        label="预测类型"
        prop="forecastType"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag :type="forecastTypeTagMap[row.forecastType]">
            {{ forecastTypeMap[row.forecastType] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="预测方法"
        prop="forecastMethod"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag :type="forecastMethodTagMap[row.forecastMethod]">
            {{ forecastMethodMap[row.forecastMethod] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="预测日期"
        prop="forecastDate"
        width="120"
        align="center"
        sortable="custom"
      >
        <template slot-scope="{row}">
          <span>{{ row.forecastDate }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="预测金额"
        prop="forecastAmount"
        width="140"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ formatAmount(row.forecastAmount) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="实际金额"
        prop="actualAmount"
        width="140"
        align="center"
      >
        <template slot-scope="{row}">
          <span v-if="row.actualAmount">{{ formatAmount(row.actualAmount) }}</span>
          <span v-else class="text-muted">待验证</span>
        </template>
      </el-table-column>
      <el-table-column
        label="预测准确率"
        prop="forecastAccuracy"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span v-if="row.forecastAccuracy !== null" 
                :class="getAccuracyClass(row.forecastAccuracy)">
            {{ row.forecastAccuracy }}%
          </span>
          <span v-else class="text-muted">-</span>
        </template>
      </el-table-column>
      <el-table-column
        label="置信区间"
        prop="confidenceInterval"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span v-if="row.confidenceInterval">{{ row.confidenceInterval }}%</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column
        label="预测周期"
        prop="forecastPeriod"
        width="100"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ row.forecastPeriod || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="数据源"
        prop="dataSource"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag size="mini" :type="dataSourceTagMap[row.dataSource]">
            {{ dataSourceMap[row.dataSource] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="创建时间"
        prop="createTime"
        width="160"
        align="center"
        sortable="custom"
      >
        <template slot-scope="{row}">
          <span>{{ row.createTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
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
            v-if="!row.actualAmount"
            size="mini"
            type="success"
            @click="handleVerify(row)"
          >
            验证
          </el-button>
          <el-button
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
              <el-dropdown-item command="copy">复制预测</el-dropdown-item>
              <el-dropdown-item command="export">导出数据</el-dropdown-item>
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

    <!-- 预测验证对话框 -->
    <el-dialog
      title="预测验证"
      :visible.sync="verifyDialogVisible"
      width="500px"
      @close="resetVerifyForm"
    >
      <el-form
        ref="verifyForm"
        :model="verifyForm"
        :rules="verifyRules"
        label-width="100px"
      >
        <el-form-item label="预测编号">
          <el-input v-model="verifyForm.forecastNo" disabled />
        </el-form-item>
        <el-form-item label="预测金额">
          <el-input v-model="verifyForm.forecastAmount" disabled>
            <template slot="append">元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="实际金额" prop="actualAmount">
          <el-input
            v-model="verifyForm.actualAmount"
            type="number"
            placeholder="请输入实际金额"
          >
            <template slot="append">元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="验证说明">
          <el-input
            v-model="verifyForm.verifyNotes"
            type="textarea"
            :rows="3"
            placeholder="请输入验证说明"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="verifyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmVerify">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getFundForecastPage, createFundForecast, updateFundForecast, deleteFundForecast,
         getFundForecastSummary, getFundForecastAccuracyAnalysis, getFundForecastTrendAnalysis } from '@/api/globalTreasurer/zjjh'
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import { parseTime } from '@/utils'

export default {
  name: 'FundForecast',
  components: { Pagination },
  directives: { waves },
  filters: {
    parseTime
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
        forecastType: undefined,
        forecastMethod: undefined,
        startDate: undefined,
        endDate: undefined,
        sort: '-forecastDate'
      },
      dateRange: [],
      summaryInfo: {},
      verifyDialogVisible: false,
      verifyForm: {
        forecastId: null,
        forecastNo: '',
        forecastAmount: '',
        actualAmount: '',
        verifyNotes: ''
      },
      verifyRules: {
        actualAmount: [
          { required: true, message: '请输入实际金额', trigger: 'blur' },
          { type: 'number', message: '实际金额必须为数字', trigger: 'blur' }
        ]
      },
      forecastTypeOptions: [
        { label: '收入预测', value: 'INCOME' },
        { label: '支出预测', value: 'EXPENSE' },
        { label: '现金流预测', value: 'CASHFLOW' },
        { label: '投资预测', value: 'INVESTMENT' },
        { label: '融资预测', value: 'FINANCING' }
      ],
      forecastMethodOptions: [
        { label: '历史数据法', value: 'HISTORICAL' },
        { label: '回归分析法', value: 'REGRESSION' },
        { label: '季节性分析', value: 'SEASONAL' },
        { label: '移动平均法', value: 'MOVING_AVERAGE' },
        { label: '指数平滑法', value: 'EXPONENTIAL' },
        { label: '人工预测', value: 'MANUAL' }
      ],
      forecastTypeMap: {
        'INCOME': '收入预测',
        'EXPENSE': '支出预测',
        'CASHFLOW': '现金流预测',
        'INVESTMENT': '投资预测',
        'FINANCING': '融资预测'
      },
      forecastTypeTagMap: {
        'INCOME': 'success',
        'EXPENSE': 'danger',
        'CASHFLOW': 'info',
        'INVESTMENT': 'warning',
        'FINANCING': 'primary'
      },
      forecastMethodMap: {
        'HISTORICAL': '历史数据法',
        'REGRESSION': '回归分析法',
        'SEASONAL': '季节性分析',
        'MOVING_AVERAGE': '移动平均法',
        'EXPONENTIAL': '指数平滑法',
        'MANUAL': '人工预测'
      },
      forecastMethodTagMap: {
        'HISTORICAL': 'info',
        'REGRESSION': 'success',
        'SEASONAL': 'warning',
        'MOVING_AVERAGE': 'primary',
        'EXPONENTIAL': 'danger',
        'MANUAL': ''
      },
      dataSourceMap: {
        'SYSTEM': '系统数据',
        'MANUAL': '手工录入',
        'IMPORT': '导入数据',
        'API': '接口数据'
      },
      dataSourceTagMap: {
        'SYSTEM': 'success',
        'MANUAL': 'warning',
        'IMPORT': 'info',
        'API': 'primary'
      }
    }
  },
  created() {
    this.getList()
    this.getSummaryInfo()
  },
  methods: {
    getList() {
      this.listLoading = true
      getFundForecastPage(this.listQuery).then(response => {
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
    getSummaryInfo() {
      getFundForecastSummary(this.listQuery).then(response => {
        if (response.code === 200) {
          this.summaryInfo = response.data
        }
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
      this.getSummaryInfo()
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
      if (prop === 'forecastDate') {
        this.sortByForecastDate(order)
      } else if (prop === 'createTime') {
        this.sortByCreateTime(order)
      }
    },
    sortByForecastDate(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+forecastDate'
      } else {
        this.listQuery.sort = '-forecastDate'
      }
      this.handleFilter()
    },
    sortByCreateTime(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+createTime'
      } else {
        this.listQuery.sort = '-createTime'
      }
      this.handleFilter()
    },
    handleCreate() {
      this.$router.push('/globalTreasurer/zjjh/forecast/create')
    },
    handleUpdate(row) {
      this.$router.push(`/globalTreasurer/zjjh/forecast/edit/${row.forecastId}`)
    },
    handleVerify(row) {
      this.verifyForm = {
        forecastId: row.forecastId,
        forecastNo: row.forecastNo,
        forecastAmount: row.forecastAmount,
        actualAmount: '',
        verifyNotes: ''
      }
      this.verifyDialogVisible = true
    },
    confirmVerify() {
      this.$refs.verifyForm.validate(valid => {
        if (valid) {
          // 调用验证API
          const verifyData = {
            forecastId: this.verifyForm.forecastId,
            actualAmount: parseFloat(this.verifyForm.actualAmount),
            verifyNotes: this.verifyForm.verifyNotes
          }

          updateFundForecast(verifyData).then(response => {
            if (response.code === 200) {
              this.$message.success('验证成功')
              this.verifyDialogVisible = false
              this.getList()
              this.getSummaryInfo()
            } else {
              this.$message.error(response.message || '验证失败')
            }
          })
        }
      })
    },
    resetVerifyForm() {
      this.verifyForm = {
        forecastId: null,
        forecastNo: '',
        forecastAmount: '',
        actualAmount: '',
        verifyNotes: ''
      }
      if (this.$refs.verifyForm) {
        this.$refs.verifyForm.resetFields()
      }
    },
    handleDelete(row, index) {
      this.$confirm('确认删除该预测记录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteFundForecast(row.forecastId).then(response => {
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
          this.copyForecast(row)
          break
        case 'export':
          this.exportForecast(row)
          break
      }
    },
    showDetail(row) {
      this.$router.push(`/globalTreasurer/zjjh/forecast/view/${row.forecastId}`)
    },
    copyForecast(row) {
      const newForecast = { ...row }
      delete newForecast.forecastId
      newForecast.forecastNo = `${row.forecastNo}_COPY`
      this.$router.push({
        path: '/globalTreasurer/zjjh/forecast/create',
        query: { copyData: JSON.stringify(newForecast) }
      })
    },
    exportForecast(row) {
      // 导出单个预测数据
      this.$message.info('导出功能开发中...')
    },
    showAccuracyAnalysis() {
      this.$router.push('/globalTreasurer/zjjh/forecast/accuracy-analysis')
    },
    showTrendAnalysis() {
      this.$router.push('/globalTreasurer/zjjh/forecast/trend-analysis')
    },
    getAccuracyClass(accuracy) {
      if (accuracy >= 90) return 'text-success'
      if (accuracy >= 80) return 'text-warning'
      return 'text-danger'
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

.page-header {
  margin-bottom: 20px;

  h2 {
    margin: 0 0 8px 0;
    color: #303133;
    font-size: 24px;
    font-weight: 500;
  }

  p {
    margin: 0;
    color: #909399;
    font-size: 14px;
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
  font-weight: 500;
}

.text-warning {
  color: #E6A23C !important;
  font-weight: 500;
}

.text-danger {
  color: #F56C6C !important;
  font-weight: 500;
}

.text-muted {
  color: #C0C4CC !important;
}

::v-deep .el-table {
  .text-success {
    color: #67C23A;
    font-weight: 500;
  }

  .text-warning {
    color: #E6A23C;
    font-weight: 500;
  }

  .text-danger {
    color: #F56C6C;
    font-weight: 500;
  }

  .text-muted {
    color: #C0C4CC;
  }
}

.dialog-footer {
  text-align: right;
}
</style>
