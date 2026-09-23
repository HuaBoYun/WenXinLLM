<template>
  <el-dialog
    title="关联交易网络图"
    :visible.sync="dialogVisible"
    width="75%"
    :before-close="handleClose"
  >
    <div class="network-toolbar">
      <el-radio-group v-model="layoutType" size="small">
        <el-radio-button label="force">力导向布局</el-radio-button>
        <el-radio-button label="circular">环形布局</el-radio-button>
      </el-radio-group>
      <el-button size="small" type="primary" @click="refreshNetwork" :loading="loading">
        <i class="el-icon-refresh"></i> 刷新
      </el-button>
    </div>
    <div ref="networkChart" style="height:450px;"></div>

    <el-divider>关联交易统计</el-divider>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-value" style="color:#1677FF">{{ stats.nodeCount }}</div>
          <div class="stat-label">关联企业数</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-value" style="color:#FA8C16">{{ stats.linkCount }}</div>
          <div class="stat-label">关联交易笔数</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-value" style="color:#52C41A">{{ stats.totalAmount }}万</div>
          <div class="stat-label">交易总额</div>
        </el-card>
      </el-col>
    </el-row>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'RelatedTransactionNetworkDialog',
  props: {
    visible: { type: Boolean, default: false },
    transactionData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      loading: false,
      layoutType: 'force',
      stats: { nodeCount: 0, linkCount: 0, totalAmount: 0 }
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.$nextTick(() => this.refreshNetwork())
      }
    }
  },
  methods: {
    refreshNetwork() {
      this.loading = true
      const networkData = this.transactionData.networkData || {}
      const nodes = networkData.nodes || []
      const links = networkData.links || []
      this.stats = {
        nodeCount: networkData.nodeCount || nodes.length,
        linkCount: networkData.linkCount || links.length,
        totalAmount: networkData.totalAmount || 0
      }
      this.renderChart(nodes, links)
      this.loading = false
    },
    renderChart(nodes, links) {
      const echarts = window.echarts || this.$echarts
      if (!echarts || !this.$refs.networkChart) return
      const chart = echarts.init(this.$refs.networkChart)
      const categories = [{ name: '企业' }, { name: '关联方' }]
      const chartNodes = nodes.map(n => ({
        name: n.name,
        symbolSize: n.symbolSize || 25,
        category: n.category || 0
      }))
      const chartLinks = links.map(l => ({
        source: l.source,
        target: l.target,
        value: l.value || 0
      }))
      const isCircular = this.layoutType === 'circular'
      chart.setOption({
        tooltip: {
          formatter: function(params) {
            if (params.dataType === 'edge') return `${params.data.source} → ${params.data.target}<br/>交易额: ${params.data.value}万`
            return `${params.name}`
          }
        },
        legend: { data: categories.map(c => c.name), bottom: 0 },
        series: [{
          type: 'graph',
          layout: isCircular ? 'circular' : 'force',
          data: chartNodes,
          links: chartLinks,
          categories: categories,
          roam: true,
          label: { show: true, position: 'right', fontSize: 11 },
          force: { repulsion: 300, edgeLength: [80, 200] },
          circular: { rotateLabel: true },
          lineStyle: { color: 'source', curveness: 0.3, width: 2 },
          emphasis: { focus: 'adjacency', lineStyle: { width: 4 } }
        }]
      })
    },
    handleClose() { this.dialogVisible = false }
  }
}
</script>

<style scoped>
.dialog-footer { text-align: right; }
.network-toolbar { display: flex; align-items: center; justify-content: space-between; margin-bottom: 12px; }
.stat-card { text-align: center; }
.stat-value { font-size: 22px; font-weight: 700; }
.stat-label { font-size: 12px; color: #888; margin-top: 4px; }
</style>
