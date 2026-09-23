<template>
  <div class="app-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>资金计划调整管理</h2>
      <p>管理资金计划的调整申请，包括审批流程、影响分析和风险评估</p>
    </div>

    <!-- 查询条件 -->
    <div class="filter-container">
      <el-select
        v-model="listQuery.adjustmentType"
        placeholder="调整类型"
        clearable
        style="width: 150px"
        class="filter-item"
      >
        <el-option
          v-for="item in adjustmentTypeOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-select
        v-model="listQuery.approvalStatus"
        placeholder="审批状态"
        clearable
        style="width: 150px"
        class="filter-item"
      >
        <el-option
          v-for="item in approvalStatusOptions"
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
        新建调整
      </el-button>
      <el-button
        v-waves
        class="filter-item"
        type="success"
        icon="el-icon-data-analysis"
        @click="showImpactAnalysis"
      >
        影响分析
      </el-button>
      <el-button
        v-waves
        class="filter-item"
        type="warning"
        icon="el-icon-document-checked"
        @click="showPendingApproval"
      >
        待审批
      </el-button>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.totalCount || 0 }}</div>
            <div class="statistics-label">调整总数</div>
          </div>
          <i class="el-icon-edit-outline statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.pendingCount || 0 }}</div>
            <div class="statistics-label">待审批</div>
          </div>
          <i class="el-icon-time statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ summaryInfo.approvedCount || 0 }}</div>
            <div class="statistics-label">已审批</div>
          </div>
          <i class="el-icon-circle-check statistics-icon"></i>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-value">{{ formatAmount(summaryInfo.totalAdjustmentAmount) }}</div>
            <div class="statistics-label">调整总金额</div>
          </div>
          <i class="el-icon-money statistics-icon"></i>
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
        label="调整编号"
        prop="adjustmentNo"
        width="140"
        align="center"
      >
        <template slot-scope="{row}">
          <el-link type="primary" @click="showDetail(row)">
            {{ row.adjustmentNo }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column
        label="调整类型"
        prop="adjustmentType"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag :type="adjustmentTypeTagMap[row.adjustmentType]">
            {{ adjustmentTypeMap[row.adjustmentType] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="原始金额"
        prop="originalAmount"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ formatAmount(row.originalAmount) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="调整后金额"
        prop="adjustedAmount"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span>{{ formatAmount(row.adjustedAmount) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="调整金额"
        prop="adjustmentAmount"
        width="120"
        align="center"
      >
        <template slot-scope="{row}">
          <span :class="getAmountClass(row.adjustmentAmount)">
            {{ formatAmount(row.adjustmentAmount) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        label="审批状态"
        prop="approvalStatus"
        width="100"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag :type="approvalStatusTagMap[row.approvalStatus]">
            {{ approvalStatusMap[row.approvalStatus] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        label="调整原因"
        prop="adjustmentReason"
        width="200"
        align="center"
        show-overflow-tooltip
      >
        <template slot-scope="{row}">
          <span>{{ row.adjustmentReason || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="影响评估"
        prop="impactAssessment"
        width="150"
        align="center"
        show-overflow-tooltip
      >
        <template slot-scope="{row}">
          <span>{{ row.impactAssessment || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="风险等级"
        prop="riskEvaluation"
        width="100"
        align="center"
      >
        <template slot-scope="{row}">
          <el-tag v-if="row.riskEvaluation" :type="riskTagMap[row.riskEvaluation]" size="mini">
            {{ riskMap[row.riskEvaluation] }}
          </el-tag>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column
        label="申请时间"
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
        label="审批时间"
        prop="approveTime"
        width="160"
        align="center"
      >
        <template slot-scope="{row}">
          <span v-if="row.approveTime">{{ row.approveTime | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
          <span v-else class="text-muted">-</span>
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
            v-if="row.approvalStatus === 'PENDING'"
            type="success"
            size="mini"
            @click="handleApprove(row)"
          >
            审批
          </el-button>
          <el-button
            v-if="row.approvalStatus === 'PENDING'"
            type="danger"
            size="mini"
            @click="handleReject(row)"
          >
            拒绝
          </el-button>
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
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
              <el-dropdown-item command="copy">复制调整</el-dropdown-item>
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

    <!-- 审批对话框 -->
    <el-dialog
      title="调整审批"
      :visible.sync="approvalDialogVisible"
      width="500px"
      @close="resetApprovalForm"
    >
      <el-form
        ref="approvalForm"
        :model="approvalForm"
        :rules="approvalRules"
        label-width="100px"
      >
        <el-form-item label="调整编号">
          <el-input v-model="approvalForm.adjustmentNo" disabled />
        </el-form-item>
        <el-form-item label="调整类型">
          <el-input v-model="approvalForm.adjustmentTypeText" disabled />
        </el-form-item>
        <el-form-item label="调整金额">
          <el-input v-model="approvalForm.adjustmentAmount" disabled>
            <template slot="append">元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="审批结果" prop="approvalResult">
          <el-radio-group v-model="approvalForm.approvalResult">
            <el-radio label="APPROVED">同意</el-radio>
            <el-radio label="REJECTED">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审批意见">
          <el-input
            v-model="approvalForm.approveComment"
            type="textarea"
            :rows="3"
            placeholder="请输入审批意见"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="approvalDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmApproval">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getFundPlanAdjustmentPage, createFundPlanAdjustment, updateFundPlanAdjustment, deleteFundPlanAdjustment,
         getFundPlanAdjustmentSummary, approveFundPlanAdjustment, rejectFundPlanAdjustment } from '@/api/globalTreasurer/zjjh'
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import { parseTime } from '@/utils'

export default {
  name: 'FundPlanAdjustment',
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
        adjustmentType: undefined,
        approvalStatus: undefined,
        startDate: undefined,
        endDate: undefined,
        sort: '-createTime'
      },
      dateRange: [],
      summaryInfo: {},
      approvalDialogVisible: false,
      approvalForm: {
        adjustmentId: null,
        adjustmentNo: '',
        adjustmentTypeText: '',
        adjustmentAmount: '',
        approvalResult: '',
        approveComment: ''
      },
      approvalRules: {
        approvalResult: [
          { required: true, message: '请选择审批结果', trigger: 'change' }
        ]
      },
      adjustmentTypeOptions: [
        { label: '金额调整', value: 'AMOUNT' },
        { label: '时间调整', value: 'DATE' },
        { label: '项目调整', value: 'PROJECT' },
        { label: '紧急调整', value: 'EMERGENCY' },
        { label: '取消调整', value: 'CANCEL' }
      ],
      approvalStatusOptions: [
        { label: '待审批', value: 'PENDING' },
        { label: '已审批', value: 'APPROVED' },
        { label: '已拒绝', value: 'REJECTED' }
      ],
      adjustmentTypeMap: {
        'AMOUNT': '金额调整',
        'DATE': '时间调整',
        'PROJECT': '项目调整',
        'EMERGENCY': '紧急调整',
        'CANCEL': '取消调整'
      },
      adjustmentTypeTagMap: {
        'AMOUNT': 'primary',
        'DATE': 'success',
        'PROJECT': 'info',
        'EMERGENCY': 'danger',
        'CANCEL': 'warning'
      },
      approvalStatusMap: {
        'PENDING': '待审批',
        'APPROVED': '已审批',
        'REJECTED': '已拒绝'
      },
      approvalStatusTagMap: {
        'PENDING': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger'
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
      getFundPlanAdjustmentPage(this.listQuery).then(response => {
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
      getFundPlanAdjustmentSummary(this.listQuery).then(response => {
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
      if (prop === 'createTime') {
        this.sortByCreateTime(order)
      }
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
      this.$router.push('/globalTreasurer/zjjh/adjustment/create')
    },
    handleUpdate(row) {
      this.$router.push(`/globalTreasurer/zjjh/adjustment/edit/${row.adjustmentId}`)
    },
    handleApprove(row) {
      this.approvalForm = {
        adjustmentId: row.adjustmentId,
        adjustmentNo: row.adjustmentNo,
        adjustmentTypeText: this.adjustmentTypeMap[row.adjustmentType],
        adjustmentAmount: row.adjustmentAmount,
        approvalResult: 'APPROVED',
        approveComment: ''
      }
      this.approvalDialogVisible = true
    },
    handleReject(row) {
      this.approvalForm = {
        adjustmentId: row.adjustmentId,
        adjustmentNo: row.adjustmentNo,
        adjustmentTypeText: this.adjustmentTypeMap[row.adjustmentType],
        adjustmentAmount: row.adjustmentAmount,
        approvalResult: 'REJECTED',
        approveComment: ''
      }
      this.approvalDialogVisible = true
    },
    confirmApproval() {
      this.$refs.approvalForm.validate(valid => {
        if (valid) {
          const approvalData = {
            adjustmentId: this.approvalForm.adjustmentId,
            approveComment: this.approvalForm.approveComment
          }

          const apiCall = this.approvalForm.approvalResult === 'APPROVED'
            ? approveFundPlanAdjustment(approvalData)
            : rejectFundPlanAdjustment(approvalData)

          apiCall.then(response => {
            if (response.code === 200) {
              this.$message.success(this.approvalForm.approvalResult === 'APPROVED' ? '审批成功' : '拒绝成功')
              this.approvalDialogVisible = false
              this.getList()
              this.getSummaryInfo()
            } else {
              this.$message.error(response.message || '操作失败')
            }
          })
        }
      })
    },
    resetApprovalForm() {
      this.approvalForm = {
        adjustmentId: null,
        adjustmentNo: '',
        adjustmentTypeText: '',
        adjustmentAmount: '',
        approvalResult: '',
        approveComment: ''
      }
      if (this.$refs.approvalForm) {
        this.$refs.approvalForm.resetFields()
      }
    },
    handleCommand(command, row) {
      switch (command) {
        case 'detail':
          this.showDetail(row)
          break
        case 'copy':
          this.copyAdjustment(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },
    showDetail(row) {
      this.$router.push(`/globalTreasurer/zjjh/adjustment/view/${row.adjustmentId}`)
    },
    copyAdjustment(row) {
      const newAdjustment = { ...row }
      delete newAdjustment.adjustmentId
      newAdjustment.adjustmentNo = `${row.adjustmentNo}_COPY`
      this.$router.push({
        path: '/globalTreasurer/zjjh/adjustment/create',
        query: { copyData: JSON.stringify(newAdjustment) }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除该调整记录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteFundPlanAdjustment(row.adjustmentId).then(response => {
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
    showImpactAnalysis() {
      this.$router.push('/globalTreasurer/zjjh/adjustment/impact-analysis')
    },
    showPendingApproval() {
      this.listQuery.approvalStatus = 'PENDING'
      this.handleFilter()
    },
    getAmountClass(amount) {
      if (amount > 0) return 'text-success'
      if (amount < 0) return 'text-danger'
      return ''
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
