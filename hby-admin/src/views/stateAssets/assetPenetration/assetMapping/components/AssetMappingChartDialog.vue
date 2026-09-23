<template>
  <el-dialog
    title="资产映射图表"
    :visible.sync="dialogVisible"
    width="80%"
    :before-close="handleClose"
  >
    <div id="mappingChart" style="width: 100%; height: 500px;"></div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="exportChart">导出图表</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'AssetMappingChartDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
    },
    exportChart() {
      this.$message.success('导出映射图表')
    }
  }
}
</script>
