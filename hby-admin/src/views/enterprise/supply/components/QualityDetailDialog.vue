<template>
  <el-dialog title="质检详情" :visible.sync="dialogVisible" width="700px" @close="handleClose">
    <el-descriptions :column="2" border v-if="data">
      <el-descriptions-item label="检验编号">{{ data.inspectionNo }}</el-descriptions-item>
      <el-descriptions-item label="物料名称">{{ data.materialName }}</el-descriptions-item>
      <el-descriptions-item label="供应商">{{ data.supplierName }}</el-descriptions-item>
      <el-descriptions-item label="检验类型">{{ data.inspectionType }}</el-descriptions-item>
      <el-descriptions-item label="批次号">{{ data.batchNo }}</el-descriptions-item>
      <el-descriptions-item label="检验数量">{{ data.quantity }}</el-descriptions-item>
      <el-descriptions-item label="合格数量">{{ data.qualifiedQuantity }}</el-descriptions-item>
      <el-descriptions-item label="合格率">{{ data.qualificationRate }}%</el-descriptions-item>
      <el-descriptions-item label="检验员">{{ data.inspector }}</el-descriptions-item>
      <el-descriptions-item label="检验日期">{{ data.inspectionDate }}</el-descriptions-item>
      <el-descriptions-item label="检验结果">
        <el-tag :type="{'合格':'success','不合格':'danger','待复检':'warning'}[data.result]||'info'">{{ data.result }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="备注">{{ data.remark || '-' }}</el-descriptions-item>
    </el-descriptions>
    <span slot="footer"><el-button @click="handleClose">关 闭</el-button></span>
  </el-dialog>
</template>
<script>
export default {
  name: 'QualityDetailDialog',
  props: { visible: { type: Boolean, default: false }, data: { type: Object, default: null } },
  data() { return { dialogVisible: this.visible } },
  watch: { visible(val) { this.dialogVisible = val } },
  methods: { handleClose() { this.$emit('close') } }
}
</script>
