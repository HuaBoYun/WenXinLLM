<template>
  <el-dialog
    title="监控历史记录"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div class="history-header">
      <el-descriptions :column="3" border size="small">
        <el-descriptions-item label="监控ID">{{ monitoringData.monitoringId || '-' }}</el-descriptions-item>
        <el-descriptions-item label="公司名称">{{ monitoringData.companyName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="当前状态">
          <el-tag :type="getStatusTag(monitoringData.alertStatus)" size="small">
            {{ getStatusText(monitoringData.alertStatus) }}
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </div>

    <el-divider content-position="left">历史记录</el-divider>

    <el-timeline v-loading="loading">
      <el-timeline-item
        v-for="(item, index) in historyList"
        :key="index"
        :timestamp="item.operateTime"
        :type="getTimelineType(item.operateType)"
        placement="top"
      >
        <el-card shadow="hover" class="history-card">
          <div class="history-item">
            <div class="item-header">
              <span class="operate-type">{{ getOperateTypeText(item.operateType) }}</span>
              <span class="operator">操作人：{{ item.operatorName || '-' }}</span>
            </div>
            <div class="item-content">
              <p v-if="item.beforeStatus">状态变更：{{ getStatusText(item.beforeStatus) }} → {{ getStatusText(item.afterStatus) }}</p>
              <p v-if="item.remark">备注：{{ item.remark }}</p>
            </div>
          </div>
        </el-card>
      </el-timeline-item>
      <el-timeline-item v-if="historyList.length === 0 && !loading" timestamp="" type="info">
        <span class="no-data">暂无历史记录</span>
      </el-timeline-item>
    </el-timeline>

    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'MonitoringHistoryDialog',
  props: {
    visible: { type: Boolean, default: false },
    monitoringData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      dialogVisible: false,
      loading: false,
      historyList: []
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) this.fetchHistory()
    }
  },
  methods: {
    fetchHistory() {
      this.loading = true
      // 模拟获取历史记录
      setTimeout(() => {
        this.historyList = [
          { operateTime: '2026-02-06 10:30:00', operateType: 'CREATE', operatorName: '系统', beforeStatus: '', afterStatus: 'PENDING', remark: '系统自动创建预警' },
          { operateTime: '2026-02-05 14:20:00', operateType: 'UPDATE', operatorName: '张三', beforeStatus: 'PENDING', afterStatus: 'HANDLED', remark: '已联系相关部门处理' },
          { operateTime: '2026-02-04 09:15:00', operateType: 'ALERT', operatorName: '系统', beforeStatus: 'LOW', afterStatus: 'MEDIUM', remark: '风险等级自动升级' }
        ]
        this.loading = false
      }, 500)
    },
    handleClose() {
      this.dialogVisible = false
      this.historyList = []
      this.$emit('update:visible', false)
    },
    getStatusText(status) {
      const map = { 'PENDING': '待处理', 'HANDLED': '已处理', 'CLOSED': '已关闭', 'HIGH': '高风险', 'MEDIUM': '中风险', 'LOW': '低风险' }
      return map[status] || status || '-'
    },
    getStatusTag(status) {
      const map = { 'PENDING': 'warning', 'HANDLED': 'success', 'CLOSED': 'info' }
      return map[status] || ''
    },
    getOperateTypeText(type) {
      const map = { 'CREATE': '创建记录', 'UPDATE': '更新状态', 'ALERT': '预警变更', 'PROCESS': '处理预警', 'CLOSE': '关闭预警' }
      return map[type] || type || '-'
    },
    getTimelineType(type) {
      const map = { 'CREATE': 'primary', 'UPDATE': 'success', 'ALERT': 'warning', 'PROCESS': 'success', 'CLOSE': 'info' }
      return map[type] || ''
    }
  }
}
</script>

<style lang="scss" scoped>
.history-header { margin-bottom: 20px; }
.history-card { margin-bottom: 10px; }
.history-item {
  .item-header {
    display: flex; justify-content: space-between; margin-bottom: 8px;
    .operate-type { font-weight: bold; color: #303133; }
    .operator { color: #909399; font-size: 12px; }
  }
  .item-content p { margin: 4px 0; color: #606266; font-size: 13px; }
}
.no-data { color: #909399; }
</style>

