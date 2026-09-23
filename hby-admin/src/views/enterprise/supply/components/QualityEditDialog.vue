<template>
  <el-dialog :title="isEdit ? '编辑质检' : '新建质检'" :visible.sync="dialogVisible" width="700px" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row :gutter="20">
        <el-col :span="12"><el-form-item label="检验编号" prop="inspectionNo"><el-input v-model="form.inspectionNo" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="物料名称" prop="materialName"><el-input v-model="form.materialName" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="供应商">
          <el-select v-model="form.supplierId" style="width:100%" @change="onSupplierChange"><el-option v-for="item in supplierOptions" :key="item.id" :label="item.name" :value="item.id" /></el-select>
        </el-form-item></el-col>
        <el-col :span="12"><el-form-item label="检验类型" prop="inspectionType">
          <el-select v-model="form.inspectionType" style="width:100%"><el-option label="来料检验" value="来料检验" /><el-option label="过程检验" value="过程检验" /><el-option label="成品检验" value="成品检验" /><el-option label="出厂检验" value="出厂检验" /></el-select>
        </el-form-item></el-col>
        <el-col :span="12"><el-form-item label="批次号"><el-input v-model="form.batchNo" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="检验数量" prop="quantity"><el-input-number v-model="form.quantity" :min="1" style="width:100%" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="合格数量"><el-input-number v-model="form.qualifiedQuantity" :min="0" style="width:100%" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="检验员"><el-input v-model="form.inspector" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="检验日期"><el-date-picker v-model="form.inspectionDate" type="date" value-format="yyyy-MM-dd" style="width:100%" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="检验结果">
          <el-select v-model="form.result" style="width:100%"><el-option label="合格" value="合格" /><el-option label="不合格" value="不合格" /><el-option label="待复检" value="待复检" /></el-select>
        </el-form-item></el-col>
        <el-col :span="24"><el-form-item label="备注"><el-input v-model="form.remark" type="textarea" :rows="2" /></el-form-item></el-col>
      </el-row>
    </el-form>
    <span slot="footer"><el-button @click="handleClose">取 消</el-button><el-button type="primary" :loading="submitting" @click="handleSubmit">确 定</el-button></span>
  </el-dialog>
</template>
<script>
import request from '@/utils/request'
export default {
  name: 'QualityEditDialog',
  props: { visible: { type: Boolean, default: false }, data: { type: Object, default: null }, isEdit: { type: Boolean, default: false } },
  data() {
    return {
      dialogVisible: this.visible, submitting: false, supplierOptions: [],
      form: { inspectionNo: '', materialName: '', supplierId: '', supplierName: '', inspectionType: '', batchNo: '', quantity: 1, qualifiedQuantity: 0, inspector: '', inspectionDate: '', result: '', remark: '' },
      rules: { materialName: [{ required: true, message: '请输入物料名称', trigger: 'blur' }], inspectionType: [{ required: true, message: '请选择检验类型', trigger: 'change' }] }
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
          const url = this.isEdit ? '/monitor/v1/enterprise/supply/quality/update' : '/monitor/v1/enterprise/supply/quality/add'
          const res = await request({ url, method: 'post', data: this.form, headers: { 'Content-Type': 'application/json;charset=UTF-8' } })
          if (res && res.result === 200) { this.$message.success(this.isEdit ? '编辑成功' : '新增成功'); this.$emit('success') }
          else this.$message.error(res.msg || '操作失败')
        } catch (e) { this.$message.error('操作失败') } finally { this.submitting = false }
      })
    }
  }
}
</script>
