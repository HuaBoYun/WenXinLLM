<template>
  <el-dialog title="风险评估" :visible.sync="dialogVisible" width="800px" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="风险名称" prop="assessmentName"><el-input v-model="form.assessmentName" placeholder="请输入评估名称"></el-input></el-form-item>
      <el-form-item label="风险类型" prop="assessmentType">
        <el-select v-model="form.assessmentType" placeholder="请选择">
          <el-option label="财务风险" value="financial"></el-option><el-option label="运营风险" value="operational"></el-option>
          <el-option label="市场风险" value="market"></el-option><el-option label="技术风险" value="technical"></el-option>
          <el-option label="合规风险" value="compliance"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="关联风险">
        <el-select v-model="form.riskIdentificationId" placeholder="请选择关联的风险识别记录" clearable filterable style="width:100%">
          <el-option v-for="risk in riskOptions" :key="risk.riskIdentificationId" :label="risk.riskName" :value="risk.riskIdentificationId"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="评估方法">
        <el-select v-model="form.assessmentMethod" placeholder="请选择">
          <el-option label="定性分析" value="qualitative"></el-option><el-option label="定量分析" value="quantitative"></el-option>
          <el-option label="综合分析" value="comprehensive"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="整体风险等级">
        <el-select v-model="form.overallRiskLevel" placeholder="请选择">
          <el-option label="低风险" value="low"></el-option><el-option label="中风险" value="medium"></el-option>
          <el-option label="高风险" value="high"></el-option><el-option label="极高风险" value="very_high"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="风险评分"><el-input-number v-model="form.overallRiskScore" :min="0" :max="100" :precision="1"></el-input-number></el-form-item>
      <el-form-item label="评估人"><el-input v-model="form.assessor" placeholder="请输入评估人"></el-input></el-form-item>
      <el-form-item label="评估日期">
        <el-date-picker v-model="form.assessmentDate" type="date" placeholder="选择评估日期" format="yyyy-MM-dd" value-format="yyyy-MM-dd"></el-date-picker>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { addRiskAssessment, updateRiskAssessment, getRiskIdentificationList, extractData } from '@/api/enterprise/risk'

export default {
  name: 'RiskAssessmentDialog',
  props: {
    visible: { type: Boolean, default: false },
    enterpriseId: { type: String, default: '' },
    riskData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      loading: false,
      riskOptions: [],
      form: {
        assessmentName: '', assessmentType: '', assessmentMethod: 'comprehensive',
        overallRiskLevel: 'medium', overallRiskScore: 50, assessor: '', assessmentDate: '',
        riskIdentificationId: ''
      },
      rules: {
        assessmentName: [{ required: true, message: '请输入评估名称', trigger: 'blur' }],
        assessmentType: [{ required: true, message: '请选择风险类型', trigger: 'change' }]
      }
    }
  },
  computed: {
    dialogVisible: { get() { return this.visible }, set(val) { this.$emit('update:visible', val) } }
  },
  watch: { visible(val) { if (val) { this.initForm(); this.loadRiskOptions() } } },
  methods: {
    initForm() {
      if (this.riskData && this.riskData.riskAssessmentId) {
        this.form = { ...this.riskData }
      } else {
        this.form = { assessmentName: '', assessmentType: '', assessmentMethod: 'comprehensive', overallRiskLevel: 'medium', overallRiskScore: 50, assessor: '', assessmentDate: '', riskIdentificationId: '' }
      }
    },
    async loadRiskOptions() {
      if (!this.enterpriseId) return
      try {
        const res = await getRiskIdentificationList({ enterpriseId: this.enterpriseId, pageNumber: 1, pageSize: 100 })
        const data = extractData(res)
        this.riskOptions = (data && (data.tlist || data.records)) || []
      } catch (e) { console.error('加载风险选项失败:', e) }
    },
    handleClose() { this.dialogVisible = false; this.$refs.form.resetFields() },
    handleConfirm() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return
        this.loading = true
        try {
          const isEdit = !!(this.riskData && this.riskData.riskAssessmentId)
          const data = { ...this.form, enterpriseId: this.enterpriseId, enterpriseName: this.$parent.selectedEnterpriseName || '', assessmentStatus: isEdit ? this.form.assessmentStatus : 'draft' }
          if (isEdit) { await updateRiskAssessment(data) } else { await addRiskAssessment(data) }
          this.$message.success(isEdit ? '更新成功' : '新增成功')
          this.handleClose(); this.$emit('refresh')
        } catch (e) { this.$message.error('操作失败：' + e.message) }
        finally { this.loading = false }
      })
    }
  }
}
</script>
