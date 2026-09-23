<template>
  <el-dialog
    title="审核详情"
    :visible.sync="dialogVisible"
    width="800px"
    @close="handleClose"
  >
    <el-descriptions :column="2" border>
      <el-descriptions-item label="审核编号">
        {{ auditData.auditId || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="审核类型">
        <el-tag :type="getAuditTypeTag(auditData.auditType)">
          {{ getAuditTypeText(auditData.auditType) }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="审核状态">
        <el-tag :type="getStatusType(auditData.auditStatus)">
          {{ auditData.auditStatus || '-' }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="创建时间">
        {{ auditData.createTime || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="审核人">
        {{ auditData.auditor || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="审核时间">
        {{ auditData.auditTime || '-' }}
      </el-descriptions-item>
    </el-descriptions>
    
    <el-divider content-position="left">审核内容</el-divider>
    <el-descriptions :column="1" border>
      <el-descriptions-item label="数据项目">
        {{ auditData.dataItem || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="审核内容">
        <div v-html="auditData.auditContent || '-'"></div>
      </el-descriptions-item>
      <el-descriptions-item label="审核意见">
        <div v-html="auditData.auditComment || '-'"></div>
      </el-descriptions-item>
    </el-descriptions>
    
    <el-divider content-position="left">审核历史</el-divider>
    <el-timeline>
      <el-timeline-item
        v-for="(item, index) in auditHistory"
        :key="index"
        :timestamp="item.timestamp"
        :type="getTimelineType(item.result)"
      >
        <el-card>
          <h4>{{ item.auditor }} - {{ item.action }}</h4>
          <p>{{ item.comment }}</p>
          <el-tag size="small" :type="getResultType(item.result)">
            {{ item.result }}
          </el-tag>
        </el-card>
      </el-timeline-item>
    </el-timeline>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handlePrint">打印</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'AuditDetailDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    auditData: {
      type: Object,
      default: () => ({})
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
    },
    auditHistory() {
      return this.auditData.auditHistory || [
        {
          timestamp: '2024-01-15 10:30:00',
          auditor: '张三',
          action: '初审',
          result: '通过',
          comment: '数据完整，符合要求'
        },
        {
          timestamp: '2024-01-14 14:20:00',
          auditor: '李四',
          action: '提交审核',
          result: '待审核',
          comment: '提交初审'
        }
      ]
    }
  },
  methods: {
    getAuditTypeTag(type) {
      const typeMap = {
        'initial': 'info',
        'review': 'warning',
        'final': 'success'
      }
      return typeMap[type] || 'info'
    },
    getAuditTypeText(type) {
      const textMap = {
        'initial': '初审',
        'review': '复审',
        'final': '终审'
      }
      return textMap[type] || type
    },
    getStatusType(status) {
      const statusMap = {
        '待审核': 'warning',
        '审核中': 'primary',
        '已通过': 'success',
        '已驳回': 'danger',
        '已退回': 'info'
      }
      return statusMap[status] || 'info'
    },
    getTimelineType(result) {
      const typeMap = {
        '通过': 'success',
        '驳回': 'danger',
        '退回': 'warning',
        '待审核': 'info'
      }
      return typeMap[result] || 'info'
    },
    getResultType(result) {
      const typeMap = {
        '通过': 'success',
        '驳回': 'danger',
        '退回': 'warning',
        '待审核': 'info'
      }
      return typeMap[result] || 'info'
    },
    handleClose() {
      this.dialogVisible = false
    },
    handlePrint() {
      window.print()
    }
  }
}
</script>
