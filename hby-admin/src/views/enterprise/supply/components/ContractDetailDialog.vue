<template>
  <el-dialog title="合同详情" :visible.sync="dialogVisible" width="700px" @close="handleClose">
    <el-descriptions :column="2" border v-if="data">
      <el-descriptions-item label="合同编号">{{ data.contractNo }}</el-descriptions-item>
      <el-descriptions-item label="合同名称">{{ data.contractName }}</el-descriptions-item>
      <el-descriptions-item label="供应商">{{ data.supplierName }}</el-descriptions-item>
      <el-descriptions-item label="合同类型">{{ data.contractType }}</el-descriptions-item>
      <el-descriptions-item label="合同金额">¥{{ data.contractAmount }}</el-descriptions-item>
      <el-descriptions-item label="执行进度">{{ data.executionProgress }}%</el-descriptions-item>
      <el-descriptions-item label="签署日期">{{ data.signDate || '未签署' }}</el-descriptions-item>
      <el-descriptions-item label="开始日期">{{ data.startDate }}</el-descriptions-item>
      <el-descriptions-item label="结束日期">{{ data.endDate }}</el-descriptions-item>
      <el-descriptions-item label="负责人">{{ data.responsiblePerson }}</el-descriptions-item>
      <el-descriptions-item label="状态">
        <el-tag :type="getStatusType(data.status)">{{ data.status }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="备注" :span="2">{{ data.remark || '-' }}</el-descriptions-item>
    </el-descriptions>
    <span slot="footer"><el-button @click="handleClose">关 闭</el-button></span>
  </el-dialog>
</template>
<script>
export default {
  name: 'ContractDetailDialog',
  props: { visible: { type: Boolean, default: false }, data: { type: Object, default: null } },
  data() { return { dialogVisible: this.visible } },
  watch: { visible(val) { this.dialogVisible = val } },
  methods: {
    handleClose() { this.$emit('close') },
    getStatusType(s) { return { '待签署': 'warning', '执行中': 'primary', '已完成': 'success', '已到期': 'info', '已终止': 'danger' }[s] || 'info' }
  }
}
</script>
