<template>
  <el-dialog
    title="融资监控详情"
    :visible.sync="dialogVisible"
    width="700px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div v-loading="loading" class="monitoring-detail">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="监控ID">{{ detail.monitoringId || '-' }}</el-descriptions-item>
        <el-descriptions-item label="预警类型">
          <el-tag :type="getAlertTypeTag(detail.alertType)">{{ getAlertTypeText(detail.alertType) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="预警级别">
          <el-tag :type="getAlertLevelTag(detail.alertLevel)">{{ getAlertLevelText(detail.alertLevel) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="预警状态">
          <el-tag :type="getAlertStatusTag(detail.alertStatus)">{{ getAlertStatusText(detail.alertStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="预警日期">{{ detail.alertDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="公司名称">{{ detail.companyName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="预警内容" :span="2">{{ detail.alertMessage || '-' }}</el-descriptions-item>
        <el-descriptions-item label="处理人">{{ detail.handlerName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="处理日期">{{ detail.handleDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="处理意见" :span="2">{{ detail.handleOpinion || '-' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detail.remark || '-' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detail.createdTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detail.updatedTime || '-' }}</el-descriptions-item>
      </el-descriptions>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
      <el-button v-if="detail.alertStatus === 'PENDING'" type="primary" @click="handleProcess">处理预警</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getFinancingMonitoringDetail } from '@/api/globalTreasurer/rzgl'

export default {
  name: 'MonitoringDetailDialog',
  props: {
    visible: { type: Boolean, default: false },
    monitoringId: { type: [String, Number], default: null }
  },
  data() {
    return {
      dialogVisible: false,
      loading: false,
      detail: {}
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val && this.monitoringId) {
        this.fetchDetail()
      }
    }
  },
  methods: {
    fetchDetail() {
      this.loading = true
      getFinancingMonitoringDetail(this.monitoringId).then(response => {
        if (response && [1, 200, '1', '200'].includes(response.code)) {
          this.detail = response.data || {}
        } else {
          this.$message.error(response?.msg || '获取详情失败')
        }
      }).catch(error => {
        console.error('获取监控详情失败:', error)
        this.$message.error('获取详情失败')
      }).finally(() => {
        this.loading = false
      })
    },
    handleClose() {
      this.dialogVisible = false
      this.detail = {}
      this.$emit('update:visible', false)
    },
    handleProcess() {
      this.$emit('process', this.detail)
    },
    getAlertTypeText(type) {
      const map = { 'RISK': '风险预警', 'COMPLIANCE': '合规预警', 'PAYMENT': '还款预警', 'MATURITY': '到期预警' }
      return map[type] || type || '-'
    },
    getAlertTypeTag(type) {
      const map = { 'RISK': 'danger', 'COMPLIANCE': 'warning', 'PAYMENT': 'info', 'MATURITY': '' }
      return map[type] || ''
    },
    getAlertLevelText(level) {
      const map = { 'HIGH': '高风险', 'MEDIUM': '中风险', 'LOW': '低风险' }
      return map[level] || level || '-'
    },
    getAlertLevelTag(level) {
      const map = { 'HIGH': 'danger', 'MEDIUM': 'warning', 'LOW': 'success' }
      return map[level] || ''
    },
    getAlertStatusText(status) {
      const map = { 'PENDING': '待处理', 'HANDLED': '已处理', 'CLOSED': '已关闭' }
      return map[status] || status || '-'
    },
    getAlertStatusTag(status) {
      const map = { 'PENDING': 'warning', 'HANDLED': 'success', 'CLOSED': 'info' }
      return map[status] || ''
    }
  }
}
</script>

<style lang="scss" scoped>
.monitoring-detail {
  padding: 10px 0;
  ::v-deep .el-descriptions-item__label {
    width: 100px;
    font-weight: bold;
  }
}
</style>

