<template>
  <el-dialog :title="isEdit ? '编辑风险' : '新增风险'" :visible.sync="dialogVisible" width="700px" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row :gutter="20">
        <el-col :span="12"><el-form-item label="风险名称" prop="riskName"><el-input v-model="form.riskName" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="风险编号"><el-input v-model="form.riskNo" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="供应商">
          <el-select v-model="form.supplierId" style="width:100%" @change="onSupplierChange"><el-option v-for="item in supplierOptions" :key="item.id" :label="item.name" :value="item.id" /></el-select>
        </el-form-item></el-col>
        <el-col :span="12"><el-form-item label="风险类型" prop="riskType">
          <el-select v-model="form.riskType" style="width:100%"><el-option label="质量风险" value="质量风险" /><el-option label="交付风险" value="交付风险" /><el-option label="价格风险" value="价格风险" /><el-option label="合规风险" value="合规风险" /></el-select>
        </el-form-item></el-col>
        <el-col :span="12"><el-form-item label="风险等级" prop="riskLevel">
          <el-select v-model="form.riskLevel" style="width:100%"><el-option label="低风险" value="低风险" /><el-option label="中风险" value="中风险" /><el-option label="高风险" value="高风险" /><el-option label="极高风险" value="极高风险" /></el-select>
        </el-form-item></el-col>
        <el-col :span="12"><el-form-item label="风险评分"><el-input-number v-model="form.riskScore" :min="0" :max="100" style="width:100%" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="负责人"><el-input v-model="form.responsiblePerson" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="识别日期"><el-date-picker v-model="form.identifyDate" type="date" value-format="yyyy-MM-dd" style="width:100%" /></el-form-item></el-col>
        <el-col :span="24"><el-form-item label="风险描述"><el-input v-model="form.riskDescription" type="textarea" :rows="2" /></el-form-item></el-col>
        <el-col :span="24"><el-form-item label="管控措施"><el-input v-model="form.controlMeasure" type="textarea" :rows="2" /></el-form-item></el-col>
      </el-row>
    </el-form>
    <span slot="footer"><el-button @click="handleClose">取 消</el-button><el-button type="primary" :loading="submitting" @click="handleSubmit">确 定</el-button></span>
  </el-dialog>
</template>
<script>
import request from '@/utils/request'
export default {
  name: 'RiskEditDialog',
  props: { visible: { type: Boolean, default: false }, data: { type: Object, default: null }, isEdit: { type: Boolean, default: false } },
  data() {
    return {
      dialogVisible: this.visible, submitting: false, supplierOptions: [],
      form: { riskName: '', riskNo: '', supplierId: '', supplierName: '', riskType: '', riskLevel: '', riskScore: 50, responsiblePerson: '', identifyDate: '', riskDescription: '', controlMeasure: '' },
      rules: { riskName: [{ required: true, message: '请输入风险名称', trigger: 'blur' }], riskType: [{ required: true, message: '请选择风险类型', trigger: 'change' }] }
    }
  },
  watch: {
    visible: { immediate: true, handler(val) { this.dialogVisible = val; if (val) this.loadSupplierOptions() } },
    data: { immediate: true, handler(val) { if (val && this.isEdit) this.form = { ...val } } }
  },
  methods: {
    handleClose() { this.$emit('close') },
    onSupplierChange(id) { const s = this.supplierOptions.find(i => i.id === id); if (s) this.form.supplierName = s.name },
    async loadSupplierOptions() { try { const res = await request({ url: '/monitor/v1/enterprise/supply/options', method: 'get' }); if (res && res.data) this.supplierOptions = res.data } catch (e) { console.error(e) } },
    handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return
        this.submitting = true
        try {
          const url = this.isEdit ? '/monitor/v1/enterprise/supply/risk/update' : '/monitor/v1/enterprise/supply/risk/add'
          const res = await request({ url, method: 'post', data: this.form, headers: { 'Content-Type': 'application/json;charset=UTF-8' } })
          if (res && res.result === 200) { this.$message.success(this.isEdit ? '编辑成功' : '新增成功'); this.$emit('success') }
          else this.$message.error(res.msg || '操作失败')
        } catch (e) { this.$message.error('操作失败') } finally { this.submitting = false }
      })
    }
  }
}
</script>
