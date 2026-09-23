<template>
  <el-dialog :title="isEdit ? '编辑合同' : '新建合同'" :visible.sync="dialogVisible" width="700px" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="合同名称" prop="contractName">
            <el-input v-model="form.contractName" placeholder="请输入合同名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同编号" prop="contractNo">
            <el-input v-model="form.contractNo" placeholder="请输入合同编号" />
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
          <el-form-item label="合同类型" prop="contractType">
            <el-select v-model="form.contractType" placeholder="请选择" style="width:100%">
              <el-option label="采购合同" value="采购合同" />
              <el-option label="服务合同" value="服务合同" />
              <el-option label="框架协议" value="框架协议" />
              <el-option label="补充协议" value="补充协议" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合同金额" prop="contractAmount">
            <el-input-number v-model="form.contractAmount" :min="0" :precision="2" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="负责人" prop="responsiblePerson">
            <el-input v-model="form.responsiblePerson" placeholder="请输入负责人" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开始日期" prop="startDate">
            <el-date-picker v-model="form.startDate" type="date" value-format="yyyy-MM-dd" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="结束日期" prop="endDate">
            <el-date-picker v-model="form.endDate" type="date" value-format="yyyy-MM-dd" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" :rows="2" /></el-form-item>
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
  name: 'ContractEditDialog',
  props: { visible: { type: Boolean, default: false }, data: { type: Object, default: null }, isEdit: { type: Boolean, default: false } },
  data() {
    return {
      dialogVisible: this.visible, submitting: false, supplierOptions: [],
      form: { contractName: '', contractNo: '', supplierId: '', supplierName: '', contractType: '', contractAmount: 0, responsiblePerson: '', startDate: '', endDate: '', remark: '' },
      rules: {
        contractName: [{ required: true, message: '请输入合同名称', trigger: 'blur' }],
        contractType: [{ required: true, message: '请选择合同类型', trigger: 'change' }]
      }
    }
  },
  watch: {
    visible: { immediate: true, handler(val) { this.dialogVisible = val; if (val) this.loadSupplierOptions() } },
    data: { immediate: true, handler(val) { if (val && this.isEdit) this.form = { ...val } } }
  },
  methods: {
    handleClose() { this.$emit('close') },
    onSupplierChange(id) { const s = this.supplierOptions.find(i => i.id === id); if (s) this.form.supplierName = s.name },
    async loadSupplierOptions() {
      try { const res = await request({ url: '/monitor/v1/enterprise/supply/options', method: 'get' }); if (res && res.data) this.supplierOptions = res.data } catch (e) { console.error(e) }
    },
    handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return
        this.submitting = true
        try {
          const url = this.isEdit ? '/monitor/v1/enterprise/supply/contract/update' : '/monitor/v1/enterprise/supply/contract/add'
          const res = await request({ url, method: 'post', data: this.form, headers: { 'Content-Type': 'application/json;charset=UTF-8' } })
          if (res && res.result === 200) { this.$message.success(this.isEdit ? '编辑成功' : '新增成功'); this.$emit('success') }
          else this.$message.error(res.msg || '操作失败')
        } catch (e) { this.$message.error('操作失败') } finally { this.submitting = false }
      })
    }
  }
}
</script>
