<template>
  <el-dialog
    title="预算详情"
    :visible.sync="dialogVisible"
    width="900px"
    :before-close="handleClose"
    append-to-body
  >
    <div v-loading="loading" class="budget-detail-container">
      <!-- 基本信息 -->
      <div class="detail-section">
        <h3 class="section-title">基本信息</h3>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-item">
              <label>预算编号：</label>
              <span>{{ budgetDetail.budgetCode }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>预算名称：</label>
              <span>{{ budgetDetail.budgetName }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>预算类型：</label>
              <el-tag :type="getBudgetTypeTag(budgetDetail.budgetType)">
                {{ getBudgetTypeText(budgetDetail.budgetType) }}
              </el-tag>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-item">
              <label>预算金额：</label>
              <span class="amount-text">{{ formatAmount(budgetDetail.budgetAmount) }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>预算状态：</label>
              <el-tag :type="getBudgetStatusTag(budgetDetail.budgetStatus)">
                {{ getBudgetStatusText(budgetDetail.budgetStatus) }}
              </el-tag>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>预算期间：</label>
              <span>{{ budgetDetail.startDate }} 至 {{ budgetDetail.endDate }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="detail-item">
              <label>负责部门：</label>
              <span>{{ budgetDetail.responsibleDept }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>负责人：</label>
              <span>{{ budgetDetail.responsiblePerson }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>创建时间：</label>
              <span>{{ budgetDetail.createTime }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <div class="detail-item">
              <label>预算描述：</label>
              <p class="description-text">{{ budgetDetail.budgetDescription || '暂无描述' }}</p>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 预算明细 -->
      <div class="detail-section">
        <h3 class="section-title">预算明细</h3>
        <el-table :data="budgetDetail.budgetItems || []" border style="width: 100%">
          <el-table-column label="序号" type="index" width="60" align="center" />
          <el-table-column label="科目编码" prop="subjectCode" width="120" />
          <el-table-column label="科目名称" prop="subjectName" width="150" />
          <el-table-column label="预算金额" prop="amount" width="120" align="right">
            <template slot-scope="scope">
              <span class="amount-text">{{ formatAmount(scope.row.amount) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="已执行金额" prop="executedAmount" width="120" align="right">
            <template slot-scope="scope">
              <span class="amount-text">{{ formatAmount(scope.row.executedAmount) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="剩余金额" width="120" align="right">
            <template slot-scope="scope">
              <span class="amount-text remaining">
                {{ formatAmount((scope.row.amount || 0) - (scope.row.executedAmount || 0)) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="执行率" width="100" align="center">
            <template slot-scope="scope">
              <el-progress
                :percentage="getExecutionRate(scope.row)"
                :color="getProgressColor(getExecutionRate(scope.row))"
                :stroke-width="8"
              />
            </template>
          </el-table-column>
          <el-table-column label="备注" prop="remark" min-width="150" show-overflow-tooltip />
        </el-table>
        
        <!-- 汇总信息 -->
        <div class="summary-info">
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="summary-item">
                <label>预算总额：</label>
                <span class="amount-text">{{ formatAmount(getTotalBudget()) }}</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="summary-item">
                <label>已执行总额：</label>
                <span class="amount-text">{{ formatAmount(getTotalExecuted()) }}</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="summary-item">
                <label>剩余总额：</label>
                <span class="amount-text remaining">{{ formatAmount(getTotalRemaining()) }}</span>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="summary-item">
                <label>总执行率：</label>
                <span class="rate-text">{{ getTotalExecutionRate() }}%</span>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>

      <!-- 审批记录 -->
      <div class="detail-section" v-if="budgetDetail.approvalRecords && budgetDetail.approvalRecords.length > 0">
        <h3 class="section-title">审批记录</h3>
        <el-timeline>
          <el-timeline-item
            v-for="(record, index) in budgetDetail.approvalRecords"
            :key="index"
            :timestamp="record.approvalTime"
            placement="top"
          >
            <el-card>
              <div class="approval-record">
                <div class="record-header">
                  <span class="approver">{{ record.approver }}</span>
                  <el-tag :type="record.result === '1' ? 'success' : 'danger'" size="small">
                    {{ record.result === '1' ? '通过' : '驳回' }}
                  </el-tag>
                </div>
                <div class="record-content" v-if="record.comment">
                  <p>{{ record.comment }}</p>
                </div>
              </div>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
      <el-button type="primary" @click="handlePrint">打 印</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getBudgetPreparationById } from '@/api/financialSharing/costEstimation'

export default {
  name: 'BudgetDetailDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    budgetId: {
      type: [String, Number],
      default: null
    }
  },
  data() {
    return {
      loading: false,
      budgetDetail: {}
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  watch: {
    visible(val) {
      if (val && this.budgetId) {
        this.loadBudgetDetail()
      }
    }
  },
  methods: {
    loadBudgetDetail() {
      this.loading = true
      getBudgetPreparationById(this.budgetId).then(response => {
        this.budgetDetail = response.data || {}
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    getBudgetTypeTag(type) {
      const tags = { '1': 'primary', '2': 'success', '3': 'warning', '4': 'info' }
      return tags[type] || 'info'
    },
    getBudgetTypeText(type) {
      const texts = { '1': '年度预算', '2': '季度预算', '3': '月度预算', '4': '项目预算' }
      return texts[type] || '未知'
    },
    getBudgetStatusTag(status) {
      const tags = { '0': 'info', '1': 'warning', '2': 'success', '3': 'danger', '4': 'primary' }
      return tags[status] || 'info'
    },
    getBudgetStatusText(status) {
      const texts = { '0': '草稿', '1': '待审批', '2': '已审批', '3': '已驳回', '4': '已执行' }
      return texts[status] || '未知'
    },
    formatAmount(amount) {
      if (!amount) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    getExecutionRate(item) {
      if (!item.amount || item.amount === 0) return 0
      return Math.round((item.executedAmount || 0) / item.amount * 100)
    },
    getProgressColor(rate) {
      if (rate >= 90) return '#f56c6c'
      if (rate >= 70) return '#e6a23c'
      return '#67c23a'
    },
    getTotalBudget() {
      return (this.budgetDetail.budgetItems || []).reduce((sum, item) => sum + (item.amount || 0), 0)
    },
    getTotalExecuted() {
      return (this.budgetDetail.budgetItems || []).reduce((sum, item) => sum + (item.executedAmount || 0), 0)
    },
    getTotalRemaining() {
      return this.getTotalBudget() - this.getTotalExecuted()
    },
    getTotalExecutionRate() {
      const total = this.getTotalBudget()
      if (total === 0) return 0
      return Math.round(this.getTotalExecuted() / total * 100)
    },
    handlePrint() {
      this.$nextTick(() => {
        window.print()
      })
    },
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-detail-container {
  .detail-section {
    margin-bottom: 30px;
    
    .section-title {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
      margin-bottom: 15px;
      padding-bottom: 8px;
      border-bottom: 2px solid #409EFF;
    }
    
    .detail-item {
      margin-bottom: 15px;
      
      label {
        font-weight: 600;
        color: #606266;
        margin-right: 8px;
      }
      
      .description-text {
        margin: 8px 0 0 0;
        color: #606266;
        line-height: 1.6;
      }
    }
    
    .summary-info {
      margin-top: 20px;
      padding: 15px;
      background: #f5f7fa;
      border-radius: 4px;
      
      .summary-item {
        text-align: center;
        
        label {
          display: block;
          font-size: 12px;
          color: #909399;
          margin-bottom: 5px;
        }
      }
    }
  }
  
  .amount-text {
    font-weight: 600;
    color: #E6A23C;
    
    &.remaining {
      color: #67C23A;
    }
  }
  
  .rate-text {
    font-weight: 600;
    color: #409EFF;
  }
  
  .approval-record {
    .record-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 8px;
      
      .approver {
        font-weight: 600;
        color: #303133;
      }
    }
    
    .record-content {
      p {
        margin: 0;
        color: #606266;
        line-height: 1.6;
      }
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>
