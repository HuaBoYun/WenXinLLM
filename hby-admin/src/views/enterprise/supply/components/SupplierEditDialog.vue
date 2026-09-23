<template>
  <el-dialog :title="isEdit ? '编辑供应商' : '新增供应商'" :visible.sync="dialogVisible" width="700px" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="供应商名称" prop="supplierName">
            <el-input v-model="form.supplierName" placeholder="请输入供应商名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="供应商编码" prop="supplierCode">
            <el-input v-model="form.supplierCode" placeholder="请输入供应商编码" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="供应商类型" prop="supplierType">
            <el-select v-model="form.supplierType" placeholder="请选择类型" style="width:100%">
              <el-option label="原材料供应商" value="material" />
              <el-option label="设备供应商" value="equipment" />
              <el-option label="服务供应商" value="service" />
              <el-option label="技术供应商" value="technology" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合作状态" prop="status">
            <el-select v-model="form.status" placeholder="请选择状态" style="width:100%">
              <el-option label="合作中" value="active" />
              <el-option label="暂停合作" value="suspended" />
              <el-option label="终止合作" value="terminated" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系人" prop="contactPerson">
            <el-input v-model="form.contactPerson" placeholder="请输入联系人" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="contactPhone">
            <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="地址" prop="address">
            <el-input v-model="form.address" placeholder="请输入地址" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合作年限">
            <el-input-number v-model="form.cooperationYears" :min="0" :max="100" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="供应金额(万)">
            <el-input-number v-model="form.supplyAmount" :min="0" :precision="2" style="width:100%" />
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
  name: 'SupplierEditDialog',
  props: {
    visible: { type: Boolean, default: false },
    supplierData: { type: Object, default: null },
    isEdit: { type: Boolean, default: false }
  },
  data() {
    return {
      dialogVisible: this.visible,
      submitting: false,
      form: { supplierName: '', supplierCode: '', supplierType: '', status: 'active', contactPerson: '', contactPhone: '', address: '', cooperationYears: 0, supplyAmount: 0 },
      rules: {
        supplierName: [{ required: true, message: '请输入供应商名称', trigger: 'blur' }],
        supplierType: [{ required: true, message: '请选择供应商类型', trigger: 'change' }],
        status: [{ required: true, message: '请选择合作状态', trigger: 'change' }]
      }
    }
  },
  watch: {
    visible(val) { this.dialogVisible = val },
    supplierData: {
      immediate: true,
      handler(val) {
        if (val && this.isEdit) {
          this.form = { ...val }
        }
      }
    }
  },
  methods: {
    handleClose() { this.$emit('close') },
    handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return
        this.submitting = true
        try {
          const url = this.isEdit ? '/monitor/v1/enterprise/supply/update' : '/monitor/v1/enterprise/supply/add'
          const res = await request({ url, method: 'post', data: this.form, headers: { 'Content-Type': 'application/json;charset=UTF-8' } })
          if (res && res.result === 200) {
            this.$message.success(this.isEdit ? '编辑成功' : '新增成功')
            this.$emit('success')
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        } catch (e) {
          this.$message.error('操作失败')
        } finally {
          this.submitting = false
        }
      })
    }
  }
}
</script>
