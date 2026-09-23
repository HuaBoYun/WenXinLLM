<template>
  <el-dialog
    title="背书详情"
    :visible.sync="dialogVisible"
    width="700px"
    :before-close="handleClose"
  >
    <div v-if="endorseData.endorseId">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="背书编号">{{ endorseData.endorseNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="票据号">{{ endorseData.billNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="背书人">{{ endorseData.endorserName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="被背书人">{{ endorseData.endorseeName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="背书金额">
          <span class="amount-text">{{ formatAmount(endorseData.endorseAmount) }}</span> 元
        </el-descriptions-item>
        <el-descriptions-item label="背书日期">{{ endorseData.endorseDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="背书类型">
          {{ endorseData.endorseType === 1 ? '转让' : endorseData.endorseType === 2 ? '质押' : '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(endorseData.status)">
            {{ endorseData.statusName || getStatusText(endorseData.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="被背书人开户行" :span="2">
          {{ endorseData.endorseeBank || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="被背书人账号" :span="2">
          {{ endorseData.endorseeAccount || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="背书说明" :span="2">
          {{ endorseData.description || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">
          {{ endorseData.remarks || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="审批人" v-if="endorseData.approveBy">
          {{ endorseData.approveBy }}
        </el-descriptions-item>
        <el-descriptions-item label="审批时间" v-if="endorseData.approveTime">
          {{ formatDateTime(endorseData.approveTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="审批意见" :span="2" v-if="endorseData.approveComments">
          {{ endorseData.approveComments }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ formatDateTime(endorseData.createTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="创建人">
          {{ endorseData.createBy || '-' }}
        </el-descriptions-item>
      </el-descriptions>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'EndorseDetailDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    endorseData: {
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
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
      this.$emit('close')
    },
    formatAmount(amount) {
      if (!amount && amount !== 0) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      const date = new Date(dateTime)
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      })
    },
    getStatusType(status) {
      const statusMap = {
        pending: 'info',
        approved: 'success',
        rejected: 'danger',
        cancelled: 'warning'
      }
      return statusMap[status] || 'info'
    },
    getStatusText(status) {
      const statusMap = {
        pending: '待审批',
        approved: '已审批',
        rejected: '已拒绝',
        cancelled: '已撤销'
      }
      return statusMap[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.dialog-footer {
  text-align: right;
}

.amount-text {
  color: #f56c6c;
  font-weight: bold;
  font-size: 16px;
}
</style>
