<template>
  <el-dialog title="控制链图谱" :visible.sync="dialogVisible" width="850px" :before-close="handleClose" @opened="fetchAndRender">
    <div v-loading="loading" element-loading-text="加载图谱数据中...">
      <div v-if="chainData && chainData.enterpriseName">
        <el-alert :title="`${chainData.enterpriseName} — 控制链路径图谱`" type="info" show-icon :closable="false" style="margin-bottom:16px" />
        <div ref="graphChart" style="height:420px;border:1px solid #ebeef5;border-radius:4px"></div>
        <el-divider content-position="left">图谱说明</el-divider>
        <el-descriptions :column="3" size="small" border>
          <el-descriptions-item label="控制类型">{{ { DIRECT:'直接控制', INDIRECT:'间接控制', MIXED:'混合控制', PROXY:'代理控制' }[chainData.controlType] }}</el-descriptions-item>
          <el-descriptions-item label="控制强度">{{ chainData.controlStrength }}%</el-descriptions-item>
          <el-descriptions-item label="链路长度">{{ chainData.chainLength }}级</el-descriptions-item>
          <el-descriptions-item label="节点数量">{{ graphInfo.totalNodes || chainData.nodeCount }}个</el-descriptions-item>
          <el-descriptions-item label="环路检测">
            <el-tag :type="chainData.hasLoop ? 'danger' : 'success'" size="small">{{ chainData.hasLoop ? '存在环路' : '无环路' }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="风险等级">
            <el-tag :type="{ LOW:'success', MEDIUM:'warning', HIGH:'danger' }[chainData.riskLevel]" size="small">{{ chainData.riskLevel }}</el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </div>
    <div slot="footer"><el-button @click="handleClose">关闭</el-button></div>
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts'
import { buildControlChainMap } from '@/api/stateAssets/controlChain'

/** category 到颜色的映射 */
const CATEGORY_COLOR_MAP = {
  controller: '#1a3a5c',
  enterprise: '#67c23a',
  both: '#409eff',
  intermediate: '#909399'
}

/** 所有 category 列表，用于 ECharts legend 和 series.categories */
const CATEGORIES = [
  { name: 'controller', itemStyle: { color: CATEGORY_COLOR_MAP.controller } },
  { name: 'enterprise', itemStyle: { color: CATEGORY_COLOR_MAP.enterprise } },
  { name: 'both', itemStyle: { color: CATEGORY_COLOR_MAP.both } },
  { name: 'intermediate', itemStyle: { color: CATEGORY_COLOR_MAP.intermediate } }
]

export default {
  name: 'ControlChainMapDialog',
  props: {
    visible: { type: Boolean, default: false },
    chainData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      chart: null,
      loading: false,
      graphInfo: { totalNodes: 0, totalLinks: 0 }
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
      if (!val && this.chart) {
        this.chart.dispose()
        this.chart = null
      }
    }
  },
  methods: {
    handleClose() { this.dialogVisible = false },

    /**
     * 请求后端图谱数据并渲染 ECharts
     */
    async fetchAndRender() {
      if (!this.chainData || !this.chainData.chainId) return
      this.loading = true
      try {
        const res = await buildControlChainMap({ chainId: this.chainData.chainId })
        if (res.result === 200 && res.data) {
          this.graphInfo = { totalNodes: res.data.totalNodes, totalLinks: res.data.totalLinks }
          this.$nextTick(() => { this.renderChart(res.data.nodes, res.data.links) })
        } else {
          this.$message.error((res && res.msg) || '获取图谱数据失败')
        }
      } catch (e) {
        console.error('[ControlChainMapDialog] fetchAndRender error:', e)
        this.$message.error('获取控制链图谱数据失败，请稍后重试')
      } finally {
        this.loading = false
      }
    },

    /**
     * 将后端返回的 nodes/links 转换为 ECharts 配置并渲染
     */
    renderChart(rawNodes, rawLinks) {
      const el = this.$refs.graphChart
      if (!el) return
      if (this.chart) this.chart.dispose()
      this.chart = echarts.init(el)

      // 转换节点：将 category 字符串映射为 categories 数组索引，并着色
      const nodes = (rawNodes || []).map(node => {
        const catIndex = CATEGORIES.findIndex(c => c.name === node.category)
        const color = CATEGORY_COLOR_MAP[node.category] || '#909399'
        return {
          name: node.name,
          symbolSize: node.symbolSize || 40,
          category: catIndex >= 0 ? catIndex : 3,
          itemStyle: { color }
        }
      })

      // 转换连线
      const links = (rawLinks || []).map(link => {
        const lineStyle = link.controlType === 'INDIRECT' ? { type: 'dashed' } : {}
        return {
          source: link.source,
          target: link.target,
          label: { show: true, formatter: link.label || '' },
          lineStyle
        }
      })

      this.chart.setOption({
        tooltip: {},
        legend: { data: CATEGORIES.map(c => c.name), top: 10 },
        series: [{
          type: 'graph',
          layout: 'force',
          roam: true,
          draggable: true,
          label: { show: true, fontSize: 12 },
          edgeLabel: { show: true, fontSize: 10, formatter: p => (p.data.label && p.data.label.formatter) || '' },
          categories: CATEGORIES,
          data: nodes,
          links: links,
          force: { repulsion: 300, edgeLength: 150 },
          lineStyle: { width: 2, curveness: 0.1 }
        }]
      })
    }
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose()
      this.chart = null
    }
  }
}
</script>