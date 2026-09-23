<template>
  <el-dialog title="风险处理" :visible.sync="dialogVisible" width="500px" @close="handleClose">
    <el-descriptions :column="1" border v-if="data" style="margin-bottom:20px">
      <el-descriptions-item label="风险名称">{{ data.riskName }}</el-descriptions-item>
      <el-descriptions-item label="风险等级">{{ data.riskLevel }}</el-descriptions-item>
      <el-descriptions-item label="供应商">{{ data.supplierName }}</el-descriptions-item>
    </el-descriptions>
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="处理状态" prop="status">
        <el-select v-model="form.status" style="width:100%">
          <el-option label="处理中" value="处理中" />
          <el-option label="已解决" value="已解决" />
          <el-option label="监控中" value="监控中" />
        </el-select>
      </el-form-item>
      <el-form-item label="处理结果" prop="handleResult">
        <el-input v-model="form.handleResult" type="textarea" :rows="3" placeholder="请输入处理结果" />
      </el-form-item>
    </el-form>
    <span slot="footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">确认处理</el-button>
    </span>
  </el-dialog>
</template>
<script>
import request from '@/utils/request'
export default {
  name: 'RiskHandleDialog',
  props: { visible: { type: Boolean, default: false }, data: { type: Object, default: null } },
  data() {
    return {
      dialogVisible: this.visible, submitting: false,
      form: { status: '已解决', handleResult: '' },
      rules: { handleResult: [{ required: true, message: '请输入处理结果', trigger: 'blur' }] }
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
          const res = await request({ url: '/monitor/v1/enterprise/supply/risk/handle', method: 'post', data: { id: this.data.id, ...this.form }, headers: { 'Content-Type': 'application/json;charset=UTF-8' } })
          if (res && res.result === 200) { this.$message.success('处理成功'); this.$emit('success') }
          else this.$message.error(res.msg || '处理失败')
        } catch (e) { this.$message.error('处理失败') } finally { this.submitting = false }
      })
    }
  }
}
</script>
