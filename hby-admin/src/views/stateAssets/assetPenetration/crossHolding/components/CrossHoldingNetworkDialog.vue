<template>
  <el-dialog
    title="交叉持股网络"
    :visible.sync="dialogVisible"
    width="70%"
    :before-close="handleClose"
  >
    <div style="text-align: center; padding: 50px; color: #999;">
      <i class="el-icon-data-analysis" style="font-size: 48px; margin-bottom: 20px;"></i>
      <p>交叉持股网络功能</p>
      <p>此功能正在开发中...</p>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'CrossHoldingNetworkDialog',
  props: {
    visible: { type: Boolean, default: false },
    data: { type: Object, default: () => ({}) }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    }
  },
  methods: {
    handleClose() { this.dialogVisible = false }
  }
}
</script>

<style scoped>
.dialog-footer { text-align: right; }
</style>