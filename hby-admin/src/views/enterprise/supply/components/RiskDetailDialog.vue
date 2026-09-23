<template>
  <el-dialog title="风险详情" :visible.sync="dialogVisible" width="700px" @close="handleClose">
    <el-descriptions :column="2" border v-if="data">
      <el-descriptions-item label="风险编号">{{ data.riskNo }}</el-descriptions-item>
      <el-descriptions-item label="风险名称">{{ data.riskName }}</el-descriptions-item>
      <el-descriptions-item label="供应商">{{ data.supplierName }}</el-descriptions-item>
      <el-descriptions-item label="风险类型">{{ data.riskType }}</el-descriptions-item>
      <el-descriptions-item label="风险等级">
        <el-tag :type="{'低风险':'success','中风险':'warning','高风险':'danger','极高风险':'danger'}[data.riskLevel]||'info'">{{ data.riskLevel }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="风险评分">{{ data.riskScore }}</el-descriptions-item>
      <el-descriptions-item label="风险描述" :span="2">{{ data.riskDescription }}</el-descriptions-item>
      <el-descriptions-item label="管控措施" :span="2">{{ data.controlMeasure }}</el-descriptions-item>
      <el-descriptions-item label="负责人">{{ data.responsiblePerson }}</el-descriptions-item>
      <el-descriptions-item label="识别日期">{{ data.identifyDate }}</el-descriptions-item>
      <el-descriptions-item label="状态">
        <el-tag :type="{'已识别':'info','处理中':'warning','已解决':'success','监控中':'primary'}[data.status]||'info'">{{ data.status }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="处理结果">{{ data.handleResult || '-' }}</el-descriptions-item>
    </el-descriptions>
    <span slot="footer"><el-button @click="handleClose">关 闭</el-button></span>
  </el-dialog>
</template>
<script>
export default {
  name: 'RiskDetailDialog',
  props: { visible: { type: Boolean, default: false }, data: { type: Object, default: null } },
  data() { return { dialogVisible: this.visible } },
  watch: { visible(val) { this.dialogVisible = val } },
  methods: { handleClose() { this.$emit('close') } }
}
</script>
