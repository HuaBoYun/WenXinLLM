<template>
  <el-dialog :title="isEdit ? '编辑采购' : '新建采购'" :visible.sync="dialogVisible" width="700px" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="采购名称" prop="procurementName">
            <el-input v-model="form.procurementName" placeholder="请输入采购名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="采购编号" prop="procurementNo">
            <el-input v-model="form.procurementNo" placeholder="请输入采购编号" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="供应商" prop="supplierId">
            <el-select v-model="form.supplierId" placeholder="请选择供应商" style="width:100%" @change="onSupplierChange">
              <el-option v-for="item in supplierOptions" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="采购类型" prop="procurementType">
            <el-select v-model="form.procurementType" placeholder="请选择" style="width:100%">
              <el-option label="设备采购" value="设备采购" />
              <el-option label="服务采购" value="服务采购" />
              <el-option label="原材料采购" value="原材料采购" />
              <el-option label="办公用品采购" value="办公用品采购" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数量" prop="quantity">
            <el-input-number v-model="form.quantity" :min="1" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单价" prop="unitPrice">
            <el-input-number v-model="form.unitPrice" :min="0" :precision="2" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="申请人" prop="applicant">
            <el-input v-model="form.applicant" placeholder="请输入申请人" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预计到货" prop="expectedDate">
            <el-date-picker v-model="form.expectedDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注">
            <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <span slot="footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">确 定</el-button>
    </span>
  </el-dialog>
</template>
<script>
import request from '@/utils/request'
export default {
  name: 'ProcurementEditDialog',
  props: { visible: { type: Boolean, default: false }, data: { type: Object, default: null }, isEdit: { type: Boolean, default: false } },
  data() {
    return {
      dialogVisible: this.visible, submitting: false, supplierOptions: [],
      form: { procurementName: '', procurementNo: '', supplierId: '', supplierName: '', procurementType: '', quantity: 1, unitPrice: 0, applicant: '', expectedDate: '', remark: '' },
      rules: {
        procurementName: [{ required: true, message: '请输入采购名称', trigger: 'blur' }],
        procurementType: [{ required: true, message: '请选择采购类型', trigger: 'change' }],
        quantity: [{ required: true, message: '请输入数量', trigger: 'blur' }]
      }
    }
  },
  watch: {
    visible: {
      immediate: true,
      handler(val) { this.dialogVisible = val; if (val) this.loadSupplierOptions() }
    },
    data: { immediate: true, handler(val) { if (val && this.isEdit) this.form = { ...val } } }
  },
  methods: {
    handleClose() { this.$emit('close') },
    onSupplierChange(id) {
      const s = this.supplierOptions.find(i => i.id === id)
      if (s) this.form.supplierName = s.name
    },
    async loadSupplierOptions() {
      try {
        const res = await request({ url: '/monitor/v1/enterprise/supply/options', method: 'get' })
        if (res && res.data) this.supplierOptions = res.data
      } catch (e) { console.error(e) }
    },
    handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return
        this.submitting = true
        try {
          const url = this.isEdit ? '/monitor/v1/enterprise/supply/procurement/update' : '/monitor/v1/enterprise/supply/procurement/add'
          const res = await request({ url, method: 'post', data: this.form, headers: { 'Content-Type': 'application/json;charset=UTF-8' } })
          if (res && res.result === 200) { this.$message.success(this.isEdit ? '编辑成功' : '新增成功'); this.$emit('success') }
          else this.$message.error(res.msg || '操作失败')
        } catch (e) { this.$message.error('操作失败') } finally { this.submitting = false }
      })
    }
  }
}
</script>
