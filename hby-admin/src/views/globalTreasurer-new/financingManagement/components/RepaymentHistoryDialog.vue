<template>
  <el-dialog title="还款历史记录" :visible.sync="dialogVisible" width="900px" :close-on-click-modal="false" @close="handleClose">
    <div v-loading="loading">
      <el-timeline v-if="historyList.length > 0">
        <el-timeline-item v-for="(item, index) in historyList" :key="index" :timestamp="item.operateTime" :type="getTimelineType(item.operateType)" placement="top">
          <el-card shadow="hover">
            <div class="history-item">
              <div class="history-header">
                <span class="operate-type">{{ getOperateTypeText(item.operateType) }}</span>
                <el-tag :type="getStatusTagType(item.status)" size="mini">{{ getStatusText(item.status) }}</el-tag>
              </div>
              <div class="history-content">
                <el-row :gutter="20">
                  <el-col :span="8"><span class="label">操作金额：</span>{{ formatCurrency(item.amount) }}</el-col>
                  <el-col :span="8"><span class="label">操作人：</span>{{ item.operatorName || '-' }}</el-col>
                  <el-col :span="8"><span class="label">操作时间：</span>{{ item.operateTime || '-' }}</el-col>
                </el-row>
                <div v-if="item.remark" class="remark"><span class="label">备注：</span>{{ item.remark }}</div>
              </div>
            </div>
          </el-card>
        </el-timeline-item>
      </el-timeline>
      <el-empty v-else description="暂无还款历史记录" />
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getRepaymentHistory } from '@/api/globalTreasurer/rzgl'

export default {
  name: 'RepaymentHistoryDialog',
  props: {
    visible: { type: Boolean, default: false },
    repaymentId: { type: [String, Number], default: null }
  },
  data() {
    return { dialogVisible: false, loading: false, historyList: [] }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val && this.repaymentId) this.fetchHistory()
    }
  },
  methods: {
    async fetchHistory() {
      this.loading = true
      try {
        const res = await getRepaymentHistory({ repaymentId: this.repaymentId })
        if (res && res.code === 1) {
          this.historyList = res.data || []
        } else {
          this.$message.error(res?.msg || '获取历史记录失败')
        }
      } catch (e) {
        this.$message.error('获取历史记录失败')
      } finally {
        this.loading = false
      }
    },
    handleClose() {
      this.dialogVisible = false
      this.$emit('update:visible', false)
    },
    getTimelineType(type) {
      return { 'CREATE': 'primary', 'REPAY': 'success', 'PARTIAL': 'warning', 'EXTENSION': 'info', 'OVERDUE': 'danger' }[type] || 'primary'
    },
    getOperateTypeText(type) {
      return { 'CREATE': '创建还款计划', 'REPAY': '执行还款', 'PARTIAL': '部分还款', 'EXTENSION': '展期申请', 'OVERDUE': '逾期标记' }[type] || type || '-'
    },
    getStatusTagType(status) {
      return { 'SUCCESS': 'success', 'PENDING': 'warning', 'FAILED': 'danger' }[status] || 'info'
    },
    getStatusText(status) {
      return { 'SUCCESS': '成功', 'PENDING': '处理中', 'FAILED': '失败' }[status] || status || '-'
    },
    formatCurrency(amount) {
      if (amount === undefined || amount === null) return '¥0.00'
      return new Intl.NumberFormat('zh-CN', { style: 'currency', currency: 'CNY', minimumFractionDigits: 2 }).format(amount)
    }
  }
}
</script>

<style lang="scss" scoped>
.history-item {
  .history-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;
    .operate-type { font-weight: bold; font-size: 14px; }
  }
  .history-content { font-size: 13px; color: #606266;
    .label { color: #909399; }
    .remark { margin-top: 8px; }
  }
}
.dialog-footer { text-align: right; }
</style>

