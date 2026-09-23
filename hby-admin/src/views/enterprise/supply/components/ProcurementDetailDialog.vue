<template>
  <el-dialog title="采购详情" :visible.sync="dialogVisible" width="700px" @close="handleClose">
    <el-descriptions :column="2" border v-if="data">
      <el-descriptions-item label="采购单号">{{ data.procurementNo }}</el-descriptions-item>
      <el-descriptions-item label="采购名称">{{ data.procurementName }}</el-descriptions-item>
      <el-descriptions-item label="供应商">{{ data.supplierName }}</el-descriptions-item>
      <el-descriptions-item label="采购类型">{{ data.procurementType }}</el-descriptions-item>
      <el-descriptions-item label="数量">{{ data.quantity }}</el-descriptions-item>
      <el-descriptions-item label="单价">¥{{ data.unitPrice }}</el-descriptions-item>
      <el-descriptions-item label="总金额">¥{{ data.totalAmount }}</el-descriptions-item>
      <el-descriptions-item label="申请人">{{ data.applicant }}</el-descriptions-item>
      <el-descriptions-item label="审批人">{{ data.approver || '待审批' }}</el-descriptions-item>
      <el-descriptions-item label="申请日期">{{ data.applyDate }}</el-descriptions-item>
      <el-descriptions-item label="预计到货">{{ data.expectedDate }}</el-descriptions-item>
      <el-descriptions-item label="实际到货">{{ data.actualDate || '-' }}</el-descriptions-item>
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
  name: 'ProcurementDetailDialog',
  props: { visible: { type: Boolean, default: false }, data: { type: Object, default: null } },
  data() { return { dialogVisible: this.visible } },
  watch: { visible(val) { this.dialogVisible = val } },
  methods: {
    handleClose() { this.$emit('close') },
    getStatusType(s) { return { '待审批': 'warning', '已审批': 'primary', '采购中': 'info', '已完成': 'success', '已取消': 'danger' }[s] || 'info' }
  }
}
</script>
