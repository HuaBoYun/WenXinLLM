<template>
  <el-dialog title="风险报告" :visible.sync="dialogVisible" width="900px" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="报告名称" prop="reportTitle"><el-input v-model="form.reportTitle" placeholder="请输入报告名称"></el-input></el-form-item>
      <el-form-item label="报告类型" prop="reportType">
        <el-select v-model="form.reportType" placeholder="请选择报告类型">
          <el-option label="风险评估报告" value="assessment"></el-option><el-option label="风险监控报告" value="monitoring"></el-option>
          <el-option label="风险事件报告" value="incident"></el-option><el-option label="综合风险报告" value="comprehensive"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="报告周期">
        <el-select v-model="form.period" placeholder="请选择报告周期">
          <el-option label="日报" value="daily"></el-option><el-option label="周报" value="weekly"></el-option>
          <el-option label="月报" value="monthly"></el-option><el-option label="季报" value="quarterly"></el-option>
          <el-option label="年报" value="yearly"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="报告摘要">
        <el-input v-model="form.summary" type="textarea" :rows="4" placeholder="请输入报告摘要"></el-input>
      </el-form-item>
      <el-form-item label="报告内容">
        <el-input v-model="form.reportContent" type="textarea" :rows="6" placeholder="请输入报告内容"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading">保存</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { generateRiskReport, updateRiskReport, extractData } from '@/api/enterprise/risk'

export default {
  name: 'RiskReportDialog',
  props: {
    visible: { type: Boolean, default: false },
    enterpriseId: { type: String, default: '' }
  },
  data() {
    return {
      loading: false,
      form: { reportTitle: '', reportType: 'comprehensive', period: 'monthly', summary: '', reportContent: '' },
      rules: {
        reportTitle: [{ required: true, message: '请输入报告名称', trigger: 'blur' }],
        reportType: [{ required: true, message: '请选择报告类型', trigger: 'change' }]
      }
    }
  },
  computed: {
    dialogVisible: { get() { return this.visible }, set(val) { this.$emit('update:visible', val) } }
  },
  watch: { visible(val) { if (val) this.initForm() } },
  methods: {
    initForm() {
      this.form = { reportTitle: '', reportType: 'comprehensive', period: 'monthly', summary: '', reportContent: '' }
    },
    handleClose() { this.dialogVisible = false; this.$refs.form.resetFields() },
    handleConfirm() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return
        this.loading = true
        try {
          const data = { ...this.form, enterpriseId: this.enterpriseId, enterpriseName: this.$parent.selectedEnterpriseName || '', creator: '系统用户' }
          const res = await generateRiskReport(data)
          const reportData = extractData(res)
          if (reportData && reportData.status === 'draft') {
            // 自动将报告状态更新为pending
            await updateRiskReport({ ...reportData, status: 'pending' })
          }
          this.$message.success('报告生成成功')
          this.handleClose(); this.$emit('refresh')
        } catch (e) { this.$message.error('操作失败：' + e.message) }
        finally { this.loading = false }
      })
    }
  }
}
</script>
