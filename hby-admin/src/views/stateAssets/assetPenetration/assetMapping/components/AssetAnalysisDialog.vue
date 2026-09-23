<template>
  <el-dialog
    title="资产分析"
    :visible.sync="dialogVisible"
    width="80%"
    :before-close="handleClose"
  >
    <el-tabs v-model="activeTab">
      <el-tab-pane label="资产概览" name="overview">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-statistic title="总资产" :value="analysisData.totalAssets" suffix="万元"></el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="固定资产" :value="analysisData.fixedAssets" suffix="万元"></el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="流动资产" :value="analysisData.currentAssets" suffix="万元"></el-statistic>
          </el-col>
          <el-col :span="6">
            <el-statistic title="无形资产" :value="analysisData.intangibleAssets" suffix="万元"></el-statistic>
          </el-col>
        </el-row>
      </el-tab-pane>
      
      <el-tab-pane label="分布分析" name="distribution">
        <div id="distributionChart" style="width: 100%; height: 400px;"></div>
      </el-tab-pane>
      
      <el-tab-pane label="趋势分析" name="trend">
        <div id="trendChart" style="width: 100%; height: 400px;"></div>
      </el-tab-pane>
    </el-tabs>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="exportAnalysis">导出分析</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'AssetAnalysisDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    analysisData: {
      type: Object,
      default: () => ({
        totalAssets: 50000,
        fixedAssets: 30000,
        currentAssets: 15000,
        intangibleAssets: 5000
      })
    }
  },
  data() {
    return {
      activeTab: 'overview'
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
    exportAnalysis() {
      this.$message.success('导出资产分析报告')
    }
  }
}
</script>
