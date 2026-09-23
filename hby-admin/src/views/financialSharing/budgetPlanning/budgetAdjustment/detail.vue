<template>
  <div class="adjustment-detail-container">
    <el-card shadow="never">
      <div slot="header" class="card-header">
        <span>调整单详情</span>
        <el-button type="text" icon="el-icon-back" @click="handleBack">返回</el-button>
      </div>

      <!-- 基本信息 -->
      <el-descriptions :column="2" border>
        <el-descriptions-item label="调整单号">
          {{ adjustmentInfo.adjustmentNo || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="预算模型">
          {{ adjustmentInfo.modelName || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="预算期间">
          {{ adjustmentInfo.period || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="预算版本">
          {{ adjustmentInfo.version || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="调整类型">
          <el-tag v-if="adjustmentInfo.adjustmentType === 'FULL'" type="primary" size="small">整版调整</el-tag>
          <el-tag v-else type="success" size="small">零星调整</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="adjustmentInfo.status === 'DRAFT'" type="info" size="small">草稿</el-tag>
          <el-tag v-else-if="adjustmentInfo.status === 'SUBMITTED'" type="warning" size="small">已提交</el-tag>
          <el-tag v-else-if="adjustmentInfo.status === 'APPROVED'" type="success" size="small">已审批</el-tag>
          <el-tag v-else-if="adjustmentInfo.status === 'REJECTED'" type="danger" size="small">已驳回</el-tag>
          <el-tag v-else type="primary" size="small">已执行</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="调整原因" :span="2">
          {{ adjustmentInfo.adjustmentReason || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="调整说明" :span="2">
          {{ adjustmentInfo.adjustmentDesc || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="创建人">
          {{ adjustmentInfo.createUser || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ adjustmentInfo.createTime || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="提交人">
          {{ adjustmentInfo.submitUser || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="提交时间">
          {{ adjustmentInfo.submitTime || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="审批人">
          {{ adjustmentInfo.approveUser || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="审批时间">
          {{ adjustmentInfo.approveTime || '-' }}
        </el-descriptions-item>
        <el-descriptions-item v-if="adjustmentInfo.approveOpinion" label="审批意见" :span="2">
          {{ adjustmentInfo.approveOpinion }}
        </el-descriptions-item>
        <el-descriptions-item v-if="adjustmentInfo.executeUser" label="执行人">
          {{ adjustmentInfo.executeUser }}
        </el-descriptions-item>
        <el-descriptions-item v-if="adjustmentInfo.executeTime" label="执行时间">
          {{ adjustmentInfo.executeTime }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 调整明细 -->
    <el-card shadow="never" style="margin-top: 20px">
      <div slot="header" class="card-header">
        <span>调整明细</span>
        <div>
          <el-button
            v-if="adjustmentInfo.status === 'DRAFT'"
            type="primary"
            size="small"
            icon="el-icon-edit"
            @click="handleEdit"
          >
            编辑
          </el-button>
          <el-button
            v-if="adjustmentInfo.status === 'DRAFT'"
            type="success"
            size="small"
            icon="el-icon-s-promotion"
            @click="handleSubmit"
          >
            提交
          </el-button>
          <el-button
            v-if="adjustmentInfo.status === 'SUBMITTED'"
            type="success"
            size="small"
            icon="el-icon-check"
            @click="handleApprove(true)"
          >
            通过
          </el-button>
          <el-button
            v-if="adjustmentInfo.status === 'SUBMITTED'"
            type="danger"
            size="small"
            icon="el-icon-close"
            @click="handleApprove(false)"
          >
            驳回
          </el-button>
          <el-button
            v-if="adjustmentInfo.status === 'SUBMITTED'"
            type="warning"
            size="small"
            icon="el-icon-refresh-left"
            @click="handleWithdraw"
          >
            撤销
          </el-button>
          <el-button
            v-if="adjustmentInfo.status === 'APPROVED'"
            type="primary"
            size="small"
            icon="el-icon-s-operation"
            @click="handleExecute"
          >
            执行
          </el-button>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="detailList"
        border
        stripe
        show-summary
        :summary-method="getSummaries"
      >
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="subjectCode" label="科目编码" width="120" show-overflow-tooltip />
        <el-table-column prop="subjectName" label="科目名称" width="150" show-overflow-tooltip />
        <el-table-column prop="organizationCode" label="组织编码" width="120" show-overflow-tooltip />
        <el-table-column prop="organizationName" label="组织名称" width="150" show-overflow-tooltip />
        <el-table-column prop="dimension1Code" label="维度1编码" width="120" show-overflow-tooltip />
        <el-table-column prop="dimension1Name" label="维度1名称" width="150" show-overflow-tooltip />
        <el-table-column prop="originalValue" label="原值" width="150" align="right">
          <template slot-scope="scope">
            {{ formatNumber(scope.row.originalValue) }}
          </template>
        </el-table-column>
        <el-table-column prop="adjustmentValue" label="调整值" width="150" align="right">
          <template slot-scope="scope">
            <span :style="{ color: scope.row.adjustmentValue >= 0 ? '#67C23A' : '#F56C6C' }">
              {{ formatNumber(scope.row.adjustmentValue) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="adjustedValue" label="调整后值" width="150" align="right">
          <template slot-scope="scope">
            {{ formatNumber(scope.row.adjustedValue) }}
          </template>
        </el-table-column>
        <el-table-column prop="adjustmentReason" label="调整原因" min-width="200" show-overflow-tooltip />
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
  getAdjustmentWithDetails,
  submitAdjustment,
  approveAdjustment,
  executeAdjustment,
  withdrawAdjustment
} from '@/api/financialSharing/budgetPlanning/budgetAdjustment'

export default {
  name: 'AdjustmentDetail',
  data() {
    return {
      loading: false,
      adjustmentId: '',
      adjustmentInfo: {},
      detailList: [],
      approveDialogVisible: false,
      approveDialogTitle: '',
      approveForm: {
        adjustmentId: '',
        approved: true,
        opinion: ''
      }
    }
  },
  created() {
    this.adjustmentId = this.$route.query.adjustmentId
    if (this.adjustmentId) {
      this.loadAdjustmentData()
    } else {
      this.$message.error('缺少调整单ID参数')
      this.$router.back()
    }
  },
  methods: {
    async loadAdjustmentData() {
      this.loading = true
      try {
        const res = await getAdjustmentWithDetails({ adjustmentId: this.adjustmentId })
        if (res.code === 1) {
          this.adjustmentInfo = res.data
          this.detailList = res.data.detailList || []
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
        if (['originalValue', 'adjustmentValue', 'adjustedValue'].includes(column.property)) {
          const values = data.map(item => Number(item[column.property]))
          if (!values.every(value => isNaN(value))) {
            sums[index] = this.formatNumber(
              values.reduce((prev, curr) => {
                const value = Number(curr)
                if (!isNaN(value)) {
                  return prev + value
                } else {
                  return prev
                }
              }, 0)
            )
          } else {
            sums[index] = '-'
          }
        } else {
          sums[index] = ''
        }
      })
      return sums
    },
    handleEdit() {
      this.$router.push({
        path: '/financialSharing/budgetPlanning/budgetAdjustment/create',
        query: { adjustmentId: this.adjustmentId }
      })
    },
    handleSubmit() {
      this.$confirm('确认提交该调整单吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await submitAdjustment({ adjustmentId: this.adjustmentId })
          if (res.code === 1) {
            this.$message.success('提交成功')
            this.loadAdjustmentData()
          } else {
            this.$message.error(res.msg || '提交失败')
          }
        } catch (error) {
          this.$message.error('提交失败: ' + error.message)
        }
      }).catch(() => {})
    },
    handleApprove(approved) {
      this.approveForm.adjustmentId = this.adjustmentId
      this.approveForm.approved = approved
      this.approveForm.opinion = ''
      this.approveDialogTitle = approved ? '审批通过' : '审批驳回'
      this.approveDialogVisible = true
    },
    async confirmApprove() {
      try {
        const res = await approveAdjustment(this.approveForm)
        if (res.code === 1) {
          this.$message.success('审批成功')
          this.approveDialogVisible = false
          this.loadAdjustmentData()
        } else {
          this.$message.error(res.msg || '审批失败')
        }
      } catch (error) {
        this.$message.error('审批失败: ' + error.message)
      }
    },
    handleWithdraw() {
      this.$confirm('确认撤销该调整单吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await withdrawAdjustment({ adjustmentId: this.adjustmentId })
          if (res.code === 1) {
            this.$message.success('撤销成功')
            this.loadAdjustmentData()
          } else {
            this.$message.error(res.msg || '撤销失败')
          }
        } catch (error) {
          this.$message.error('撤销失败: ' + error.message)
        }
      }).catch(() => {})
    },
    handleExecute() {
      this.$confirm('确认执行该调整单吗?执行后将更新预算数据。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await executeAdjustment({ adjustmentId: this.adjustmentId })
          if (res.code === 1) {
            this.$message.success('执行成功')
            this.loadAdjustmentData()
          } else {
            this.$message.error(res.msg || '执行失败')
          }
        } catch (error) {
          this.$message.error('执行失败: ' + error.message)
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
.adjustment-detail-container {
  padding: 20px;

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
</style>

