<template>
  <div class="forecast-detail-container">
    <el-card shadow="never" class="info-card">
      <div slot="header" class="card-header">
        <span>预测任务信息</span>
        <div>
          <el-button v-if="forecast.status === 'DRAFT'" type="primary" size="small" icon="el-icon-s-promotion" @click="handleSubmit">提交</el-button>
          <el-button v-if="forecast.status === 'SUBMITTED'" type="success" size="small" icon="el-icon-check" @click="handleApprove(true)">通过</el-button>
          <el-button v-if="forecast.status === 'SUBMITTED'" type="danger" size="small" icon="el-icon-close" @click="handleApprove(false)">驳回</el-button>
          <el-button v-if="forecast.status === 'SUBMITTED'" type="warning" size="small" icon="el-icon-refresh-left" @click="handleWithdraw">撤销</el-button>
          <el-button size="small" icon="el-icon-back" @click="handleBack">返回</el-button>
        </div>
      </div>

      <el-descriptions :column="3" border>
        <el-descriptions-item label="预测编号">{{ forecast.forecastNo }}</el-descriptions-item>
        <el-descriptions-item label="预测名称">{{ forecast.forecastName }}</el-descriptions-item>
        <el-descriptions-item label="预算模型">{{ forecast.modelName }}</el-descriptions-item>
        <el-descriptions-item label="预测类型">
          <el-tag v-if="forecast.forecastType === 'MONTHLY'" type="primary" size="small">月度预测</el-tag>
          <el-tag v-else type="success" size="small">季度预测</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="开始期间">{{ forecast.startPeriod }}</el-descriptions-item>
        <el-descriptions-item label="结束期间">{{ forecast.endPeriod }}</el-descriptions-item>
        <el-descriptions-item label="版本">{{ forecast.version }}</el-descriptions-item>
        <el-descriptions-item label="基准类型">
          <span v-if="forecast.baseType === 'HISTORY'">历史数据</span>
          <span v-else-if="forecast.baseType === 'BUDGET'">预算数据</span>
          <span v-else>实际数据</span>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="forecast.status === 'DRAFT'" type="info" size="small">草稿</el-tag>
          <el-tag v-else-if="forecast.status === 'SUBMITTED'" type="warning" size="small">已提交</el-tag>
          <el-tag v-else-if="forecast.status === 'APPROVED'" type="success" size="small">已审批</el-tag>
          <el-tag v-else type="danger" size="small">已驳回</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="预测说明" :span="3">{{ forecast.forecastDesc || '-' }}</el-descriptions-item>
        <el-descriptions-item label="提交人">{{ forecast.submitUser || '-' }}</el-descriptions-item>
        <el-descriptions-item label="提交时间">{{ forecast.submitTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审批人">{{ forecast.approveUser || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审批时间">{{ forecast.approveTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审批意见" :span="2">{{ forecast.approveOpinion || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card shadow="never" class="data-card">
      <div slot="header">
        <span>预测数据明细</span>
      </div>

      <el-table
        v-loading="loading"
        :data="forecast.dataList"
        border
        stripe
        show-summary
        :summary-method="getSummaries"
      >
        <el-table-column prop="period" label="期间" width="100" align="center" />
        <el-table-column prop="subjectCode" label="科目编码" width="120" show-overflow-tooltip />
        <el-table-column prop="subjectName" label="科目名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="organizationCode" label="组织编码" width="120" show-overflow-tooltip />
        <el-table-column prop="organizationName" label="组织名称" min-width="150" show-overflow-tooltip />
        <el-table-column prop="baseValue" label="基准值" width="120" align="right">
          <template slot-scope="scope">
            {{ formatNumber(scope.row.baseValue) }}
          </template>
        </el-table-column>
        <el-table-column prop="forecastValue" label="预测值" width="120" align="right">
          <template slot-scope="scope">
            {{ formatNumber(scope.row.forecastValue) }}
          </template>
        </el-table-column>
        <el-table-column prop="varianceValue" label="差异值" width="120" align="right">
          <template slot-scope="scope">
            <span :class="scope.row.varianceValue >= 0 ? 'positive' : 'negative'">
              {{ formatNumber(scope.row.varianceValue) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="varianceRate" label="差异率(%)" width="120" align="right">
          <template slot-scope="scope">
            <span :class="scope.row.varianceRate >= 0 ? 'positive' : 'negative'">
              {{ formatNumber(scope.row.varianceRate) }}%
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="actualValue" label="实际值" width="120" align="right">
          <template slot-scope="scope">
            {{ formatNumber(scope.row.actualValue) }}
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
      </el-table>
    </el-card>

    <!-- 审批对话框 -->
    <el-dialog
      :title="approveDialogTitle"
      :visible.sync="approveDialogVisible"
      width="500px"
    >
      <el-form :model="approveForm" label-width="100px">
        <el-form-item label="审批意见">
          <el-input
            v-model="approveForm.opinion"
            type="textarea"
            :rows="4"
            placeholder="请输入审批意见"
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="approveDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmApprove">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getForecastWithData,
  submitForecast,
  approveForecast,
  withdrawForecast
} from '@/api/financialSharing/budgetPlanning/rollingForecast'

export default {
  name: 'RollingForecastDetail',
  data() {
    return {
      loading: false,
      forecast: {
        dataList: []
      },
      approveDialogVisible: false,
      approveDialogTitle: '',
      approveForm: {
        forecastId: '',
        approved: true,
        opinion: ''
      }
    }
  },
  created() {
    const forecastId = this.$route.query.forecastId
    if (forecastId) {
      this.loadData(forecastId)
    }
  },
  methods: {
    async loadData(forecastId) {
      this.loading = true
      try {
        const res = await getForecastWithData({ forecastId })
        if (res.code === 1) {
          this.forecast = res.data || { dataList: [] }
        } else {
          this.$message.error(res.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败: ' + error.message)
      } finally {
        this.loading = false
      }
    },
    formatNumber(value) {
      if (value === null || value === undefined) {
        return '-'
      }
      return Number(value).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    getSummaries(param) {
      const { columns, data } = param
      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计'
          return
        }
        const values = data.map(item => Number(item[column.property]))
        if (['baseValue', 'forecastValue', 'varianceValue', 'actualValue'].includes(column.property)) {
          if (!values.every(value => isNaN(value))) {
            sums[index] = values.reduce((prev, curr) => {
              const value = Number(curr)
              if (!isNaN(value)) {
                return prev + curr
              } else {
                return prev
              }
            }, 0)
            sums[index] = this.formatNumber(sums[index])
          } else {
            sums[index] = '-'
          }
        } else {
          sums[index] = ''
        }
      })
      return sums
    },
    handleSubmit() {
      this.$confirm('确认提交该预测任务吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        try {
          const res = await submitForecast({ forecastId: this.forecast.forecastId })
          if (res.code === 1) {
            this.$message.success('提交成功')
            this.loadData(this.forecast.forecastId)
          } else {
            this.$message.error(res.msg || '提交失败')
          }
        } catch (error) {
          this.$message.error('提交失败: ' + error.message)
        }
      }).catch(() => {})
    },
    handleApprove(approved) {
      this.approveForm.forecastId = this.forecast.forecastId
      this.approveForm.approved = approved
      this.approveForm.opinion = ''
      this.approveDialogTitle = approved ? '审批通过' : '审批驳回'
      this.approveDialogVisible = true
    },
    async confirmApprove() {
      try {
        const res = await approveForecast(this.approveForm)
        if (res.code === 1) {
          this.$message.success('审批成功')
          this.approveDialogVisible = false
          this.loadData(this.forecast.forecastId)
        } else {
          this.$message.error(res.msg || '审批失败')
        }
      } catch (error) {
        this.$message.error('审批失败: ' + error.message)
      }
    },
    handleWithdraw() {
      this.$confirm('确认撤销该预测任务吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        try {
          const res = await withdrawForecast({ forecastId: this.forecast.forecastId })
          if (res.code === 1) {
            this.$message.success('撤销成功')
            this.loadData(this.forecast.forecastId)
          } else {
            this.$message.error(res.msg || '撤销失败')
          }
        } catch (error) {
          this.$message.error('撤销失败: ' + error.message)
        }
      }).catch(() => {})
    },
    handleBack() {
      this.$router.back()
    }
  }
}
</script>

<style lang="scss" scoped>
.forecast-detail-container {
  padding: 20px;

  .info-card {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }

  .data-card {
    .el-table {
      .positive {
        color: #67C23A;
      }

      .negative {
        color: #F56C6C;
      }
    }
  }
}
</style>

