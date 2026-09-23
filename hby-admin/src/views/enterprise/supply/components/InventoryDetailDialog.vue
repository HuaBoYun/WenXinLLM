<template>
  <el-dialog title="库存详情" :visible.sync="dialogVisible" width="700px" @close="handleClose">
    <el-descriptions :column="2" border v-if="data">
      <el-descriptions-item label="物料编码">{{ data.materialCode }}</el-descriptions-item>
      <el-descriptions-item label="物料名称">{{ data.materialName }}</el-descriptions-item>
      <el-descriptions-item label="分类">{{ data.category }}</el-descriptions-item>
      <el-descriptions-item label="规格型号">{{ data.specification }}</el-descriptions-item>
      <el-descriptions-item label="单位">{{ data.unit }}</el-descriptions-item>
      <el-descriptions-item label="当前库存">{{ data.currentStock }}</el-descriptions-item>
      <el-descriptions-item label="最低库存">{{ data.minStock }}</el-descriptions-item>
      <el-descriptions-item label="最高库存">{{ data.maxStock }}</el-descriptions-item>
      <el-descriptions-item label="单价">¥{{ data.unitPrice }}</el-descriptions-item>
      <el-descriptions-item label="总价值">¥{{ data.totalValue }}</el-descriptions-item>
      <el-descriptions-item label="仓库">{{ data.warehouse }}</el-descriptions-item>
      <el-descriptions-item label="供应商">{{ data.supplierName }}</el-descriptions-item>
      <el-descriptions-item label="库存状态">
        <el-tag :type="{'正常':'success','偏低':'warning','过高':'info','缺货':'danger'}[data.stockStatus]||'info'">{{ data.stockStatus }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="最后更新">{{ data.lastUpdateDate }}</el-descriptions-item>
    </el-descriptions>
    <span slot="footer"><el-button @click="handleClose">关 闭</el-button></span>
  </el-dialog>
</template>
<script>
export default {
  name: 'InventoryDetailDialog',
  props: { visible: { type: Boolean, default: false }, data: { type: Object, default: null } },
  data() { return { dialogVisible: this.visible } },
  watch: { visible(val) { this.dialogVisible = val } },
  methods: { handleClose() { this.$emit('close') } }
}
</script>
