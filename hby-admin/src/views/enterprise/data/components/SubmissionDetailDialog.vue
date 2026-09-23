<template>
  <el-dialog
    title="报送详情"
    :visible.sync="dialogVisible"
    width="700px"
    @close="handleClose"
  >
    <el-descriptions :column="2" border>
      <el-descriptions-item label="企业名称">
        {{ submissionData.enterpriseName || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="报送状态">
        <el-tag :type="getStatusType(submissionData.status)">
          {{ submissionData.status || '-' }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="数据类型">
        {{ submissionData.dataType || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="数据类别">
        {{ submissionData.dataCategory || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="报告期间">
        {{ submissionData.reportPeriod || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="报告年度">
        {{ submissionData.reportYear || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="录入人">
        {{ submissionData.submitter || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="提交时间">
        {{ submissionData.submitTime || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="质量评分">
        <span v-if="submissionData.qualityScore">
          <el-tag :type="submissionData.qualityScore >= 90 ? 'success' : submissionData.qualityScore >= 70 ? 'warning' : 'danger'">
            {{ submissionData.qualityScore }}分
          </el-tag>
        </span>
        <span v-else style="color:#909399">未评分</span>
      </el-descriptions-item>
      <el-descriptions-item label="创建时间">
        {{ submissionData.createTime || '-' }}
      </el-descriptions-item>
    </el-descriptions>

    <div v-if="submissionData.remark" style="margin-top: 20px;">
      <el-divider content-position="left">备注</el-divider>
      <p style="color:#606266;">{{ submissionData.remark }}</p>
    </div>

    <el-divider content-position="left">报送进度</el-divider>
    <el-steps :active="currentStep" finish-status="success" align-center>
      <el-step title="数据录入" :description="submissionData.createTime || ''"></el-step>
      <el-step title="待报送" description="草稿状态"></el-step>
      <el-step title="已报送" :description="submissionData.submitTime || ''"></el-step>
      <el-step title="审核完成" :description="submissionData.auditTime || ''"></el-step>
    </el-steps>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'SubmissionDetailDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    submissionData: {
      type: Object,
      default: () => ({})
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    },
    currentStep() {
      const status = this.submissionData.status
      if (status === '已审核') return 4
      if (status === '已提交') return 3
      if (status === '草稿') return 1
      if (status === '已退回') return 2
      return 0
    }
  },
  methods: {
    getStatusType(status) {
      const map = { '草稿': 'info', '已提交': 'primary', '已审核': 'success', '已退回': 'danger' }
      return map[status] || 'info'
    },
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>
