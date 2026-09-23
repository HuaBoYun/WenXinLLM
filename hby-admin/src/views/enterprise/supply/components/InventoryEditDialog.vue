<template>
  <el-dialog :title="isEdit ? '编辑物料' : '新增物料'" :visible.sync="dialogVisible" width="700px" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row :gutter="20">
        <el-col :span="12"><el-form-item label="物料名称" prop="materialName"><el-input v-model="form.materialName" placeholder="请输入" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="物料编码" prop="materialCode"><el-input v-model="form.materialCode" placeholder="请输入" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="分类" prop="category">
          <el-select v-model="form.category" style="width:100%"><el-option label="原材料" value="原材料" /><el-option label="半成品" value="半成品" /><el-option label="成品" value="成品" /><el-option label="备品备件" value="备品备件" /></el-select>
        </el-form-item></el-col>
        <el-col :span="12"><el-form-item label="规格型号"><el-input v-model="form.specification" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="单位">
          <el-select v-model="form.unit" style="width:100%"><el-option label="吨" value="吨" /><el-option label="件" value="件" /><el-option label="套" value="套" /><el-option label="箱" value="箱" /></el-select>
        </el-form-item></el-col>
        <el-col :span="12"><el-form-item label="单价"><el-input-number v-model="form.unitPrice" :min="0" :precision="2" style="width:100%" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="当前库存"><el-input-number v-model="form.currentStock" :min="0" style="width:100%" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="最低库存"><el-input-number v-model="form.minStock" :min="0" style="width:100%" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="最高库存"><el-input-number v-model="form.maxStock" :min="0" style="width:100%" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="仓库"><el-input v-model="form.warehouse" placeholder="请输入仓库" /></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="供应商">
          <el-select v-model="form.supplierId" style="width:100%" @change="onSupplierChange">
            <el-option v-for="item in supplierOptions" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item></el-col>
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
  name: 'InventoryEditDialog',
  props: { visible: { type: Boolean, default: false }, data: { type: Object, default: null }, isEdit: { type: Boolean, default: false } },
  data() {
    return {
      dialogVisible: this.visible, submitting: false, supplierOptions: [],
      form: { materialName: '', materialCode: '', category: '', specification: '', unit: '件', unitPrice: 0, currentStock: 0, minStock: 0, maxStock: 0, warehouse: '', supplierId: '', supplierName: '' },
      rules: { materialName: [{ required: true, message: '请输入物料名称', trigger: 'blur' }], category: [{ required: true, message: '请选择分类', trigger: 'change' }] }
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
          const url = this.isEdit ? '/monitor/v1/enterprise/supply/inventory/update' : '/monitor/v1/enterprise/supply/inventory/add'
          const res = await request({ url, method: 'post', data: this.form, headers: { 'Content-Type': 'application/json;charset=UTF-8' } })
          if (res && res.result === 200) { this.$message.success(this.isEdit ? '编辑成功' : '新增成功'); this.$emit('success') }
          else this.$message.error(res.msg || '操作失败')
        } catch (e) { this.$message.error('操作失败') } finally { this.submitting = false }
      })
    }
  }
}
</script>
