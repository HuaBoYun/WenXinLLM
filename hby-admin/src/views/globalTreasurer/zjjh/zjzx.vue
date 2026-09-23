<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>资金计划执行管理</h2>
      <p>监控和管理资金计划的执行情况，包括执行进度、效率分析和风险控制</p>
    </div>

    <!-- 查询条件 -->
    <div class="filter-container">
      <el-select
        v-model="listQuery.executionType"
        placeholder="执行类型"
        clearable
        style="width: 150px"
        class="filter-item"
      >
        <el-option
          v-for="item in executionTypeOptions"
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
      <el-select
        v-model="listQuery.riskLevel"
        placeholder="风险等级"
        clearable
        style="width: 120px"
        class="filter-item"
      >
        <el-option
          v-for="item in riskLevelOptions"
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
        新建执行
      </el-button>
      <el-button
        v-waves
        class="filter-item"
        type="success"
        icon="el-icon-data-analysis"
        @click="showEfficiencyAnalysis"
      >
        效率分析
      </el-button>
      <el-button
        v-waves
        class="filter-item"
        type="warning"
        icon="el-icon-warning"
        @click="showDelayedExecutions"
      >
        延期执行
      </el-button>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.totalCount || 0 }}</div>
            <div class="statistics-label">执行总数</div>
          </div>
          <i class="el-icon-s-data statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.inProgressCount || 0 }}</div>
            <div class="statistics-label">进行中</div>
          </div>
          <i class="el-icon-loading statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.avgExecutionRate || 0 }}%</div>
            <div class="statistics-label">平均执行率</div>
          </div>
          <i class="el-icon-data-line statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.avgExecutionEfficiency || 0 }}%</div>
            <div class="statistics-label">平均效率</div>
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
        label="执行编号"
        prop="executionNo"
        width="140"
        align="center"
      >
        <template slot-scope="{row}">
          <el-link type="primary" @click="showDetail(row)">
            {{ row.executionNo }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column
        label="执行类型"
        prop="executionType"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag :type="executionTypeTagMap[row.executionType]">
            {{ executionTypeMap[row.executionType] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="执行状态"
        prop="executionStatus"
        width="100"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag :type="executionStatusTagMap[row.executionStatus]">
            {{ executionStatusMap[row.executionStatus] }}
          </el-tag>
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
        label="执行金额"
        prop="executedAmount"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ formatAmount(row.executedAmount) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="执行率"
        prop="executionRate"
        width="100"
        align="center"
        sortable="custom"
      >
        <template slot-scope="{row}">
          <span :class="getExecutionRateClass(row.executionRate)">
            {{ row.executionRate || 0 }}%
          </span>
        </template>
      </el-table-column>
      <el-table-column
        label="计划日期"
        prop="plannedDate"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ row.plannedDate | parseTime('{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="实际日期"
        prop="actualDate"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span v-if="row.actualDate">{{ row.actualDate | parseTime('{y}-{m}-{d}') }}</span>
          <span v-else class="text-muted">-</span>
        </template>
      </el-table-column>
      <el-table-column
        label="延期天数"
        prop="delayDays"
        width="100"
        align="center"
      >
        <template slot-scope="{row}">
          <span :class="getDelayDaysClass(row.delayDays)">
            {{ row.delayDays || 0 }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        label="执行效率"
        prop="executionEfficiency"
        width="100"
        align="center"
        sortable="custom"
      >
        <template slot-scope="{row}">
          <span :class="getEfficiencyClass(row.executionEfficiency)">
            {{ row.executionEfficiency || 0 }}%
          </span>
        </template>
      </el-table-column>
      <el-table-column
        label="风险等级"
        prop="riskLevel"
        width="100"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag v-if="row.riskLevel" :type="riskTagMap[row.riskLevel]" size="mini">
            {{ riskMap[row.riskLevel] }}
          </el-tag>
          <span v-else>-</span>
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
          <el-button
            v-if="row.executionStatus === 'PENDING'"
            type="success"
            size="mini"
            @click="handleStart(row)"
          >
            开始
          </el-button>
          <el-button
            v-if="row.executionStatus === 'IN_PROGRESS'"
            type="primary"
            size="mini"
            @click="handleUpdate(row)"
          >
            更新
          </el-button>
          <el-button
            v-if="row.executionStatus === 'IN_PROGRESS'"
            type="warning"
            size="mini"
            @click="handleComplete(row)"
          >
            完成
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
              <el-dropdown-item command="pause">暂停执行</el-dropdown-item>
              <el-dropdown-item command="cancel">取消执行</el-dropdown-item>
              <el-dropdown-item command="delete">删除</el-dropdown-item>
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

    <!-- 执行更新对话框 -->
    <el-dialog
      title="更新执行"
      :visible.sync="updateDialogVisible"
      width="500px"
      @close="resetUpdateForm"
    >
      <el-form
        ref="updateForm"
        :model="updateForm"
        :rules="updateRules"
        label-width="100px"
      >
        <el-form-item label="执行编号">
          <el-input v-model="updateForm.executionNo" disabled />
        </el-form-item>
        <el-form-item label="计划金额">
          <el-input v-model="updateForm.plannedAmount" disabled>
            <template slot="append">元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="执行金额" prop="executedAmount">
          <el-input
            v-model="updateForm.executedAmount"
            type="number"
            placeholder="请输入执行金额"
          >
            <template slot="append">元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="实际日期" prop="actualDate">
          <el-date-picker
            v-model="updateForm.actualDate"
            type="date"
            placeholder="选择实际日期"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="执行说明">
          <el-input
            v-model="updateForm.executionNotes"
            type="textarea"
            :rows="3"
            placeholder="请输入执行说明"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmUpdate">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getFundPlanExecutionPage, createFundPlanExecution, updateFundPlanExecution, deleteFundPlanExecution,
         getFundPlanExecutionSummary, startFundPlanExecution, completeFundPlanExecution,
         pauseFundPlanExecution, cancelFundPlanExecution } from '@/api/globalTreasurer/zjjh'
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import { parseTime } from '@/utils'

export default {
  name: 'FundPlanExecution',
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
        executionType: undefined,
        executionStatus: undefined,
        riskLevel: undefined,
        startDate: undefined,
        endDate: undefined,
        sort: '-createTime'
      },
      dateRange: [],
      summaryInfo: {},
      updateDialogVisible: false,
      updateForm: {
        executionId: null,
        executionNo: '',
        plannedAmount: '',
        executedAmount: '',
        actualDate: null,
        executionNotes: ''
      },
      updateRules: {
        executedAmount: [
          { required: true, message: '请输入执行金额', trigger: 'blur' },
          { type: 'number', message: '执行金额必须为数字', trigger: 'blur' }
        ],
        actualDate: [
          { required: true, message: '请选择实际日期', trigger: 'change' }
        ]
      },
      executionTypeOptions: [
        { label: '收入执行', value: 'INCOME' },
        { label: '支出执行', value: 'EXPENSE' },
        { label: '投资执行', value: 'INVESTMENT' },
        { label: '融资执行', value: 'FINANCING' },
        { label: '转账执行', value: 'TRANSFER' }
      ],
      executionStatusOptions: [
        { label: '待执行', value: 'PENDING' },
        { label: '执行中', value: 'IN_PROGRESS' },
        { label: '已完成', value: 'COMPLETED' },
        { label: '已暂停', value: 'PAUSED' },
        { label: '已取消', value: 'CANCELLED' }
      ],
      riskLevelOptions: [
        { label: '高风险', value: 'HIGH' },
        { label: '中风险', value: 'MEDIUM' },
        { label: '低风险', value: 'LOW' }
      ],
      executionTypeMap: {
        'INCOME': '收入执行',
        'EXPENSE': '支出执行',
        'INVESTMENT': '投资执行',
        'FINANCING': '融资执行',
        'TRANSFER': '转账执行'
      },
      executionTypeTagMap: {
        'INCOME': 'success',
        'EXPENSE': 'danger',
        'INVESTMENT': 'warning',
        'FINANCING': 'primary',
        'TRANSFER': 'info'
      },
      executionStatusMap: {
        'PENDING': '待执行',
        'IN_PROGRESS': '执行中',
        'COMPLETED': '已完成',
        'PAUSED': '已暂停',
        'CANCELLED': '已取消'
      },
      executionStatusTagMap: {
        'PENDING': 'info',
        'IN_PROGRESS': 'warning',
        'COMPLETED': 'success',
        'PAUSED': '',
        'CANCELLED': 'danger'
      },
      riskMap: {
        'HIGH': '高风险',
        'MEDIUM': '中风险',
        'LOW': '低风险'
      },
      riskTagMap: {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'success'
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
      getFundPlanExecutionPage(this.listQuery).then(response => {
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
      getFundPlanExecutionSummary(this.listQuery).then(response => {
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
      if (prop === 'executionRate') {
        this.sortByExecutionRate(order)
      } else if (prop === 'executionEfficiency') {
        this.sortByExecutionEfficiency(order)
      } else if (prop === 'createTime') {
        this.sortByCreateTime(order)
      }
    },
    sortByExecutionRate(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+executionRate'
      } else {
        this.listQuery.sort = '-executionRate'
      }
      this.handleFilter()
    },
    sortByExecutionEfficiency(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+executionEfficiency'
      } else {
        this.listQuery.sort = '-executionEfficiency'
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
      this.$router.push('/globalTreasurer/zjjh/execution/create')
    },
    handleStart(row) {
      this.$confirm('确认开始执行该计划?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        startFundPlanExecution(row.executionId).then(response => {
          if (response.code === 200) {
            this.$message.success('开始执行成功')
            this.getList()
            this.getSummaryInfo()
          } else {
            this.$message.error(response.message || '开始执行失败')
          }
        })
      })
    },
    handleUpdate(row) {
      this.updateForm = {
        executionId: row.executionId,
        executionNo: row.executionNo,
        plannedAmount: row.plannedAmount,
        executedAmount: row.executedAmount || '',
        actualDate: row.actualDate || new Date(),
        executionNotes: row.executionNotes || ''
      }
      this.updateDialogVisible = true
    },
    confirmUpdate() {
      this.$refs.updateForm.validate(valid => {
        if (valid) {
          const updateData = {
            executionId: this.updateForm.executionId,
            executedAmount: parseFloat(this.updateForm.executedAmount),
            actualDate: this.updateForm.actualDate,
            executionNotes: this.updateForm.executionNotes
          }

          updateFundPlanExecution(updateData).then(response => {
            if (response.code === 200) {
              this.$message.success('更新成功')
              this.updateDialogVisible = false
              this.getList()
              this.getSummaryInfo()
            } else {
              this.$message.error(response.message || '更新失败')
            }
          })
        }
      })
    },
    resetUpdateForm() {
      this.updateForm = {
        executionId: null,
        executionNo: '',
        plannedAmount: '',
        executedAmount: '',
        actualDate: null,
        executionNotes: ''
      }
      if (this.$refs.updateForm) {
        this.$refs.updateForm.resetFields()
      }
    },
    handleComplete(row) {
      this.$confirm('确认完成该执行?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        completeFundPlanExecution(row.executionId).then(response => {
          if (response.code === 200) {
            this.$message.success('完成执行成功')
            this.getList()
            this.getSummaryInfo()
          } else {
            this.$message.error(response.message || '完成执行失败')
          }
        })
      })
    },
    handleCommand(command, row) {
      switch (command) {
        case 'detail':
          this.showDetail(row)
          break
        case 'pause':
          this.pauseExecution(row)
          break
        case 'cancel':
          this.cancelExecution(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },
    showDetail(row) {
      this.$router.push(`/globalTreasurer/zjjh/execution/view/${row.executionId}`)
    },
    pauseExecution(row) {
      this.$confirm('确认暂停该执行?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        pauseFundPlanExecution(row.executionId).then(response => {
          if (response.code === 200) {
            this.$message.success('暂停成功')
            this.getList()
            this.getSummaryInfo()
          } else {
            this.$message.error(response.message || '暂停失败')
          }
        })
      })
    },
    cancelExecution(row) {
      this.$confirm('确认取消该执行?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        cancelFundPlanExecution(row.executionId).then(response => {
          if (response.code === 200) {
            this.$message.success('取消成功')
            this.getList()
            this.getSummaryInfo()
          } else {
            this.$message.error(response.message || '取消失败')
          }
        })
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除该执行记录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteFundPlanExecution(row.executionId).then(response => {
          if (response.code === 200) {
            this.$message.success('删除成功')
            this.getList()
            this.getSummaryInfo()
          } else {
            this.$message.error(response.message || '删除失败')
          }
        })
      })
    },
    showEfficiencyAnalysis() {
      this.$router.push('/globalTreasurer/zjjh/execution/efficiency-analysis')
    },
    showDelayedExecutions() {
      this.listQuery.delayDays = 1 // 显示延期1天以上的执行
      this.handleFilter()
    },
    getExecutionRateClass(rate) {
      if (rate >= 100) return 'text-success'
      if (rate >= 80) return 'text-warning'
      if (rate >= 60) return 'text-info'
      return 'text-danger'
    },
    getDelayDaysClass(days) {
      if (days > 7) return 'text-danger'
      if (days > 3) return 'text-warning'
      if (days > 0) return 'text-info'
      return 'text-success'
    },
    getEfficiencyClass(efficiency) {
      if (efficiency >= 90) return 'text-success'
      if (efficiency >= 80) return 'text-warning'
      if (efficiency >= 70) return 'text-info'
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

.text-info {
  color: #409EFF !important;
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

  .text-info {
    color: #409EFF;
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
