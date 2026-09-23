<template>
  <el-dialog
    title="集中度图表分析"
    :visible.sync="dialogVisible"
    width="80%"
    :before-close="handleClose"
  >
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <div slot="header">
            <span>资产分布饼图</span>
          </div>
          <div id="assetPieChart" style="height: 300px; text-align: center; line-height: 300px; color: #999;">
            资产分布饼图区域
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header">
            <span>集中度趋势图</span>
          </div>
          <div id="concentrationTrendChart" style="height: 300px; text-align: center; line-height: 300px; color: #999;">
            集中度趋势图区域
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card>
          <div slot="header">
            <span>集中度热力图</span>
          </div>
          <div id="concentrationHeatmap" style="height: 400px; text-align: center; line-height: 400px; color: #999;">
            集中度热力图区域
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="exportChart">导出图表</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'ConcentrationChartDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    chartData: {
      type: Object,
      default: () => ({})
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
      this.$message.success('导出图表成功')
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
