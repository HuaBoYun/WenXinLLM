<template>
  <el-dialog title="风险控制措施" :visible.sync="dialogVisible" width="800px" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="120px">
      <el-form-item label="措施名称" prop="measureName"><el-input v-model="form.measureName" placeholder="请输入措施名称"></el-input></el-form-item>
      <el-form-item label="措施类型" prop="measureType">
        <el-select v-model="form.measureType" placeholder="请选择">
          <el-option label="预防性控制" value="preventive"></el-option><el-option label="检查性控制" value="detective"></el-option>
          <el-option label="纠正性控制" value="corrective"></el-option><el-option label="补偿性控制" value="compensating"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="关联风险">
        <el-select v-model="form.riskIdentificationId" placeholder="请选择关联的风险" clearable filterable style="width:100%">
          <el-option v-for="risk in riskOptions" :key="risk.riskIdentificationId" :label="risk.riskName" :value="risk.riskIdentificationId"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="目标风险类型">
        <el-select v-model="form.targetRiskType" placeholder="请选择">
          <el-option label="财务风险" value="financial"></el-option><el-option label="运营风险" value="operational"></el-option>
          <el-option label="市场风险" value="market"></el-option><el-option label="技术风险" value="technical"></el-option>
          <el-option label="合规风险" value="compliance"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="控制策略">
        <el-radio-group v-model="form.measureCategory">
          <el-radio label="avoid">风险规避</el-radio><el-radio label="reduce">风险减轻</el-radio>
          <el-radio label="transfer">风险转移</el-radio><el-radio label="accept">风险接受</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="责任人" prop="responsiblePerson"><el-input v-model="form.responsiblePerson" placeholder="请输入责任人"></el-input></el-form-item>
      <el-form-item label="实施期限">
        <el-date-picker v-model="form.plannedCompletionDate" type="date" placeholder="选择实施期限" format="yyyy-MM-dd" value-format="yyyy-MM-dd"></el-date-picker>
      </el-form-item>
      <el-form-item label="措施描述" prop="measureDescription">
        <el-input v-model="form.measureDescription" type="textarea" :rows="4" placeholder="请详细描述控制措施"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleConfirm" :loading="loading">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { addRiskControl, updateRiskControl, getRiskIdentificationList, extractData } from '@/api/enterprise/risk'

export default {
  name: 'RiskControlDialog',
  props: {
    visible: { type: Boolean, default: false },
    enterpriseId: { type: String, default: '' },
    riskData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      loading: false,
      riskOptions: [],
      form: { measureName: '', measureType: '', targetRiskType: '', measureCategory: 'reduce', responsiblePerson: '', plannedCompletionDate: '', measureDescription: '', riskIdentificationId: '' },
      rules: {
        measureName: [{ required: true, message: '请输入措施名称', trigger: 'blur' }],
        measureType: [{ required: true, message: '请选择措施类型', trigger: 'change' }],
        responsiblePerson: [{ required: true, message: '请输入责任人', trigger: 'blur' }]
      }
    }
  },
  computed: {
    dialogVisible: { get() { return this.visible }, set(val) { this.$emit('update:visible', val) } }
  },
  watch: { visible(val) { if (val) { this.initForm(); this.loadRiskOptions() } } },
  methods: {
    initForm() {
      if (this.riskData && this.riskData.controlMeasureId) {
        this.form = { ...this.riskData }
      } else {
        this.form = { measureName: '', measureType: '', targetRiskType: '', measureCategory: 'reduce', responsiblePerson: '', plannedCompletionDate: '', measureDescription: '', riskIdentificationId: '' }
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
          const isEdit = !!(this.riskData && this.riskData.controlMeasureId)
          const data = { ...this.form, enterpriseId: this.enterpriseId, enterpriseName: this.$parent.selectedEnterpriseName || '', implementationStatus: isEdit ? this.form.implementationStatus : 'planned' }
          if (isEdit) { await updateRiskControl(data) } else { await addRiskControl(data) }
          this.$message.success(isEdit ? '更新成功' : '新增成功')
          this.handleClose(); this.$emit('refresh')
        } catch (e) { this.$message.error('操作失败：' + e.message) }
        finally { this.loading = false }
      })
    }
  }
}
</script>
