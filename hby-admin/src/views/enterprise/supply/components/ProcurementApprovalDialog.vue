<template>
  <el-dialog title="采购审批" :visible.sync="dialogVisible" width="500px" @close="handleClose">
    <el-descriptions :column="1" border v-if="data" style="margin-bottom:20px">
      <el-descriptions-item label="采购单号">{{ data.procurementNo }}</el-descriptions-item>
      <el-descriptions-item label="采购名称">{{ data.procurementName }}</el-descriptions-item>
      <el-descriptions-item label="供应商">{{ data.supplierName }}</el-descriptions-item>
      <el-descriptions-item label="总金额">¥{{ data.totalAmount }}</el-descriptions-item>
    </el-descriptions>
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="审批人" prop="approver">
        <el-input v-model="form.approver" placeholder="请输入审批人" />
      </el-form-item>
      <el-form-item label="审批结果" prop="status">
        <el-radio-group v-model="form.status">
          <el-radio label="已审批">通过</el-radio>
          <el-radio label="已取消">驳回</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <span slot="footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">确认审批</el-button>
    </span>
  </el-dialog>
</template>
<script>
import request from '@/utils/request'
export default {
  name: 'ProcurementApprovalDialog',
  props: { visible: { type: Boolean, default: false }, data: { type: Object, default: null } },
  data() {
    return {
      dialogVisible: this.visible, submitting: false,
      form: { approver: '', status: '已审批' },
      rules: { approver: [{ required: true, message: '请输入审批人', trigger: 'blur' }] }
    }
  },
  watch: { visible(val) { this.dialogVisible = val } },
  methods: {
    handleClose() { this.$emit('close') },
    async handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return
        this.submitting = true
        try {
          const res = await request({ url: '/monitor/v1/enterprise/supply/procurement/approve', method: 'post', data: { id: this.data.id, ...this.form }, headers: { 'Content-Type': 'application/json;charset=UTF-8' } })
          if (res && res.result === 200) { this.$message.success('审批成功'); this.$emit('success') }
          else this.$message.error(res.msg || '审批失败')
        } catch (e) { this.$message.error('审批失败') } finally { this.submitting = false }
      })
    }
  }
}
</script>
