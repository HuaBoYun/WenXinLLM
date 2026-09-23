<template>
  <el-dialog title="供应商详情" :visible.sync="dialogVisible" width="700px" @close="handleClose">
    <el-descriptions :column="2" border v-if="supplierData">
      <el-descriptions-item label="供应商编码">{{ supplierData.supplierCode }}</el-descriptions-item>
      <el-descriptions-item label="供应商名称">{{ supplierData.supplierName }}</el-descriptions-item>
      <el-descriptions-item label="供应商类型">{{ getTypeText(supplierData.supplierType) }}</el-descriptions-item>
      <el-descriptions-item label="评级">
        <el-tag :type="getRatingTagType(supplierData.rating)" size="small">{{ supplierData.rating }}级</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="联系人">{{ supplierData.contactPerson }}</el-descriptions-item>
      <el-descriptions-item label="联系电话">{{ supplierData.contactPhone }}</el-descriptions-item>
      <el-descriptions-item label="地址" :span="2">{{ supplierData.address }}</el-descriptions-item>
      <el-descriptions-item label="合作年限">{{ supplierData.cooperationYears }}年</el-descriptions-item>
      <el-descriptions-item label="合作金额">{{ supplierData.totalAmount }}万元</el-descriptions-item>
      <el-descriptions-item label="质量评分">{{ supplierData.qualityScore }}</el-descriptions-item>
      <el-descriptions-item label="交付评分">{{ supplierData.deliveryScore }}</el-descriptions-item>
      <el-descriptions-item label="价格评分">{{ supplierData.priceScore }}</el-descriptions-item>
      <el-descriptions-item label="综合评分">{{ supplierData.overallScore }}</el-descriptions-item>
      <el-descriptions-item label="风险等级">{{ supplierData.riskLevel }}</el-descriptions-item>
      <el-descriptions-item label="合作状态">
        <el-tag :type="getStatusTagType(supplierData.status)" size="small">{{ getStatusText(supplierData.status) }}</el-tag>
      </el-descriptions-item>
    </el-descriptions>
    <span slot="footer">
      <el-button @click="handleClose">关 闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'SupplierDetailDialog',
  props: {
    visible: { type: Boolean, default: false },
    supplierData: { type: Object, default: null }
  },
  data() {
    return { dialogVisible: this.visible }
  },
  watch: {
    visible(val) { this.dialogVisible = val }
  },
  methods: {
    handleClose() { this.$emit('close') },
    getTypeText(type) {
      const map = { material: '原材料', equipment: '设备', service: '服务', technology: '技术' }
      return map[type] || type || '未知'
    },
    getRatingTagType(rating) {
      const map = { A: 'success', B: 'primary', C: 'warning', D: 'danger' }
      return map[rating] || 'info'
    },
    getStatusTagType(status) {
      const map = { active: 'success', suspended: 'warning', terminated: 'danger' }
      return map[status] || 'info'
    },
    getStatusText(status) {
      const map = { active: '合作中', suspended: '暂停合作', terminated: '终止合作' }
      return map[status] || status || '未知'
    }
  }
}
</script>
