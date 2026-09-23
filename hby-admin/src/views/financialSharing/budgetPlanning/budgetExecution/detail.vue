<template>
  <div class="app-container">
    <el-page-header @back="goBack" content="预算执行详情" />

    <el-card class="detail-card" v-loading="loading">
      <!-- 基本信息 -->
      <div class="section-title">基本信息</div>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="预算模型">{{ detail.modelName }}</el-descriptions-item>
        <el-descriptions-item label="组织">{{ detail.orgName }}</el-descriptions-item>
        <el-descriptions-item label="科目编码">{{ detail.subjectCode }}</el-descriptions-item>
        <el-descriptions-item label="科目名称">{{ detail.subjectName }}</el-descriptions-item>
        <el-descriptions-item label="期间">{{ detail.period }}</el-descriptions-item>
        <el-descriptions-item label="预算年度">{{ detail.budgetYear }}</el-descriptions-item>
        <el-descriptions-item label="版本号">{{ detail.versionNo }}</el-descriptions-item>
        <el-descriptions-item label="维度1">{{ detail.dimension1Name || '-' }}</el-descriptions-item>
        <el-descriptions-item label="维度2">{{ detail.dimension2Name || '-' }}</el-descriptions-item>
      </el-descriptions>

      <!-- 预算执行情况 -->
      <div class="section-title">预算执行情况</div>
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="info-box">
            <div class="info-label">预算金额</div>
            <div class="info-value">{{ formatMoney(detail.budgetAmount) }}</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-box">
            <div class="info-label">调整后预算</div>
            <div class="info-value">{{ formatMoney(detail.adjustedAmount) }}</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-box">
            <div class="info-label">实际发生额</div>
            <div class="info-value text-primary">{{ formatMoney(detail.actualAmount) }}</div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="8">
          <div class="info-box">
            <div class="info-label">承诺金额</div>
            <div class="info-value">{{ formatMoney(detail.committedAmount) }}</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-box">
            <div class="info-label">占用金额</div>
            <div class="info-value">{{ formatMoney(detail.occupiedAmount) }}</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-box">
            <div class="info-label">可用金额</div>
            <div class="info-value" :class="detail.availableAmount < 0 ? 'text-danger' : 'text-success'">
              {{ formatMoney(detail.availableAmount) }}
            </div>
          </div>
        </el-col>
      </el-row>

      <!-- 差异分析 -->
      <div class="section-title">差异分析</div>
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="info-box">
            <div class="info-label">差异金额</div>
            <div class="info-value" :class="detail.varianceAmount < 0 ? 'text-danger' : ''">
              {{ formatMoney(detail.varianceAmount) }}
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-box">
            <div class="info-label">执行率</div>
            <div class="info-value" :class="getExecutionRateClass(detail.executionRate)">
              {{ formatPercent(detail.executionRate) }}
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="info-box">
            <div class="info-label">差异率</div>
            <div class="info-value">{{ formatPercent(detail.varianceRate) }}</div>
          </div>
        </el-col>
      </el-row>

      <!-- 状态信息 -->
      <div class="section-title">状态信息</div>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(detail.status)" size="small">
            {{ getStatusText(detail.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="预警级别">
          <el-tag v-if="detail.warningLevel" :type="getWarningLevelType(detail.warningLevel)" size="small">
            {{ getWarningLevelText(detail.warningLevel) }}
          </el-tag>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="最后更新时间">{{ detail.lastUpdateTime }}</el-descriptions-item>
      </el-descriptions>

      <!-- 操作按钮 -->
      <div class="button-group">
        <el-button @click="goBack">返回</el-button>
        <el-button type="primary" @click="handleRefresh">刷新数据</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getExecutionById, refreshExecutionData } from '@/api/financialSharing/budgetExecution'

export default {
  name: 'BudgetExecutionDetail',
  data() {
    return {
      loading: false,
      executionId: null,
      detail: {}
    }
  },
  created() {
    this.executionId = this.$route.query.executionId
    if (this.executionId) {
      this.getDetail()
    }
  },
  methods: {
    /** 获取详情 */
    getDetail() {
      this.loading = true
      getExecutionById(this.executionId).then(response => {
        if (response.code === 1) {
          this.detail = response.data || {}
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    /** 刷新数据 */
    handleRefresh() {
      this.$confirm('确认刷新预算执行数据吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const loading = this.$loading({
          lock: true,
          text: '正在刷新数据...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })
        refreshExecutionData({
          modelId: this.detail.modelId,
          orgId: this.detail.orgId,
          subjectCode: this.detail.subjectCode,
          period: this.detail.period
        }).then(response => {
          loading.close()
          if (response.code === 1) {
            this.$message.success('刷新成功')
            this.getDetail()
          } else {
            this.$message.error(response.msg || '刷新失败')
          }
        }).catch(() => {
          loading.close()
        })
      })
    },
    /** 返回 */
    goBack() {
      this.$router.back()
    },
    /** 格式化金额 */
    formatMoney(value) {
      if (value == null) return '0.00'
      return parseFloat(value).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
    },
    /** 格式化百分比 */
    formatPercent(value) {
      if (value == null) return '0.00%'
      return parseFloat(value).toFixed(2) + '%'
    },
    /** 获取执行率样式 */
    getExecutionRateClass(rate) {
      if (rate > 100) return 'text-danger'
      if (rate > 90) return 'text-warning'
      return 'text-success'
    },
    /** 获取状态类型 */
    getStatusType(status) {
      const statusMap = {
        'NORMAL': 'success',
        'WARNING': 'warning',
        'EXCEEDED': 'danger'
      }
      return statusMap[status] || 'info'
    },
    /** 获取状态文本 */
    getStatusText(status) {
      const statusMap = {
        'NORMAL': '正常',
        'WARNING': '预警',
        'EXCEEDED': '超支'
      }
      return statusMap[status] || status
    },
    /** 获取预警级别类型 */
    getWarningLevelType(level) {
      const levelMap = {
        'LOW': 'info',
        'MEDIUM': 'warning',
        'HIGH': 'danger'
      }
      return levelMap[level] || 'info'
    },
    /** 获取预警级别文本 */
    getWarningLevelText(level) {
      const levelMap = {
        'LOW': '低',
        'MEDIUM': '中',
        'HIGH': '高'
      }
      return levelMap[level] || level
    }
  }
}
</script>

<style scoped>
.detail-card {
  margin-top: 20px;
}

.section-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
  margin: 20px 0 15px 0;
  padding-left: 10px;
  border-left: 3px solid #409EFF;
}

.info-box {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
  text-align: center;
}

.info-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}

.info-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.text-primary {
  color: #409EFF;
}

.text-success {
  color: #67C23A;
}

.text-warning {
  color: #E6A23C;
}

.text-danger {
  color: #F56C6C;
}

.button-group {
  margin-top: 30px;
  text-align: center;
}
</style>

