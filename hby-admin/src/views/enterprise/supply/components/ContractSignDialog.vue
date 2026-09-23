<template>
  <el-dialog title="合同签署" :visible.sync="dialogVisible" width="500px" @close="handleClose">
    <el-descriptions :column="1" border v-if="data" style="margin-bottom:20px">
      <el-descriptions-item label="合同编号">{{ data.contractNo }}</el-descriptions-item>
      <el-descriptions-item label="合同名称">{{ data.contractName }}</el-descriptions-item>
      <el-descriptions-item label="供应商">{{ data.supplierName }}</el-descriptions-item>
      <el-descriptions-item label="合同金额">¥{{ data.contractAmount }}</el-descriptions-item>
    </el-descriptions>
    <el-alert title="确认签署后，合同将进入执行状态" type="info" :closable="false" style="margin-bottom:20px" />
    <span slot="footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="submitting" @click="handleSubmit">确认签署</el-button>
    </span>
  </el-dialog>
</template>
<script>
import request from '@/utils/request'
export default {
  name: 'ContractSignDialog',
  props: { visible: { type: Boolean, default: false }, data: { type: Object, default: null } },
  data() { return { dialogVisible: this.visible, submitting: false } },
  watch: { visible(val) { this.dialogVisible = val } },
  methods: {
    handleClose() { this.$emit('close') },
    async handleSubmit() {
      this.submitting = true
      try {
        const res = await request({ url: '/monitor/v1/enterprise/supply/contract/sign', method: 'post', data: { id: this.data.id }, headers: { 'Content-Type': 'application/json;charset=UTF-8' } })
        if (res && res.result === 200) { this.$message.success('签署成功'); this.$emit('success') }
        else this.$message.error(res.msg || '签署失败')
      } catch (e) { this.$message.error('签署失败') } finally { this.submitting = false }
    }
  }
}
</script>
