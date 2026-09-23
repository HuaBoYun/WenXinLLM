<template>
  <el-dialog
    title="资金流向路径分析"
    :visible.sync="dialogVisible"
    width="80%"
    :before-close="handleClose"
  >
    <div v-loading="loading" class="path-dialog-container">
      <!-- 流向信息 -->
      <el-card class="mb-20">
        <div slot="header">
          <span>流向信息</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="info-item">
              <div class="info-label">源企业</div>
              <div class="info-value">{{ flowData.fromCompany || '-' }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <div class="info-label">目标企业</div>
              <div class="info-value">{{ flowData.toCompany || '-' }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <div class="info-label">流转金额</div>
              <div class="info-value">{{ flowData.amount || 0 }}万元</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <div class="info-label">路径层级</div>
              <div class="info-value">{{ pathDetails.length || 0 }}层</div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 路径图表 -->
      <el-card class="mb-20">
        <div slot="header">
          <span>流向路径图</span>
        </div>
        <div ref="pathChart" style="height: 400px;"></div>
      </el-card>

      <!-- 路径详情表格 -->
      <el-card>
        <div slot="header">
          <span>路径详情</span>
        </div>
        <el-table :data="pathDetails" stripe border>
          <el-table-column prop="stepNo" label="步骤" width="80" align="center" />
          <el-table-column prop="fromEntity" label="来源账户" min-width="150" />
          <el-table-column prop="toEntity" label="目标账户" min-width="150" />
          <el-table-column prop="transferAmount" label="转移金额" width="120" align="center" />
          <el-table-column prop="transferDate" label="转移日期" width="150" align="center" />
          <el-table-column prop="transferType" label="转移类型" width="120" align="center" />
          <el-table-column prop="status" label="状态" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="scope.row.status === 'COMPLETED' ? 'success' : 'warning'">
                {{ scope.row.status === 'COMPLETED' ? '已完成' : '进行中' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts'
import { analyzeFundFlowPath } from '@/api/stateAssets/fundFlow'

export default {
  name: 'FundFlowPathDialog',
  props: {
    visible: { type: Boolean, default: false },
    data: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      loading: false,
      flowData: {},
      pathDetails: [],
      pathChart: null
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
        this.$nextTick(() => {
          this.initChart()
          this.loadPathData()
        })
      } else {
        // 关闭时销毁图表实例，避免重复初始化警告
        if (this.pathChart) {
          this.pathChart.dispose()
          this.pathChart = null
        }
      }
    }
  },
  beforeDestroy() {
    if (this.pathChart) {
      this.pathChart.dispose()
      this.pathChart = null
    }
  },
  methods: {
    initChart() {
      if (this.$refs.pathChart) {
        // 如果已有实例先销毁
        if (this.pathChart) {
          this.pathChart.dispose()
          this.pathChart = null
        }
        this.pathChart = echarts.init(this.$refs.pathChart)
      }
    },
    loadPathData() {
      this.loading = true
      this.flowData = this.data || {}
      const id = this.data.id || this.data.fundFlowId || this.data.partyId
      if (!id) {
        this.loading = false
        this.$message.warning('缺少资金流向ID')
        return
      }
      analyzeFundFlowPath({ fundFlowId: id })
        .then((response) => {
          if (response && response.data) {
            const d = response.data
            this.pathDetails = d.pathDetails || []
            this.drawPathChart(d.nodes || [], d.links || [])
          } else if (response && response.result && response.result !== 200) {
            this.pathDetails = []
            this.$message.warning(response.msg || '暂无路径数据')
          } else {
            this.pathDetails = []
            this.$message.warning('暂无路径数据')
          }
          this.loading = false
        })
        .catch((err) => {
          this.pathDetails = []
          this.loading = false
          if (err && err.msg) {
            this.$message.error(err.msg)
          } else {
            this.$message.error('路径数据加载失败')
          }
        })
    },
    drawPathChart(nodes, links) {
      if (!this.pathChart) return
      // 过滤掉自环链接（source === target），sankey图不支持
      const validLinks = links.filter(l => l.source !== l.target)
      if (nodes.length === 0 || validLinks.length === 0) {
        // 数据不足以绘制sankey图，使用力导向图替代
        this.drawForceGraph(nodes, links)
        return
      }
      try {
        const option = {
          title: { text: '资金流向路径', left: 'center' },
          tooltip: { trigger: 'item', formatter: '{b}: {c}万元' },
          series: [{
            type: 'sankey',
            data: nodes,
            links: validLinks,
            emphasis: { focus: 'adjacency' },
            lineStyle: { color: 'gradient', curveness: 0.5 },
            label: { fontSize: 12 }
          }]
        }
        this.pathChart.setOption(option, true)
      } catch (e) {
        console.warn('Sankey图渲染失败，切换为力导向图:', e)
        this.drawForceGraph(nodes, links)
      }
    },
    drawForceGraph(nodes, links) {
      if (!this.pathChart) return
      // 力导向图可以处理自环和任意拓扑
      const graphNodes = nodes.map(n => ({
        name: n.name,
        symbolSize: 50,
        label: { show: true, fontSize: 12 }
      }))
      const graphLinks = links.map(l => ({
        source: l.source,
        target: l.target,
        value: l.value,
        lineStyle: { width: 2 }
      }))
      try {
        const option = {
          title: { text: '资金流向路径', left: 'center' },
          tooltip: { trigger: 'item', formatter: function(params) {
            if (params.dataType === 'edge') {
              return params.data.source + ' → ' + params.data.target + ': ' + params.data.value + '万元'
            }
            return params.name
          }},
          series: [{
            type: 'graph',
            layout: 'force',
            data: graphNodes,
            links: graphLinks,
            roam: true,
            force: { repulsion: 200, edgeLength: 150 },
            edgeSymbol: ['none', 'arrow'],
            edgeSymbolSize: 8,
            lineStyle: { color: '#409EFF', curveness: 0.3 },
            emphasis: { focus: 'adjacency' }
          }]
        }
        this.pathChart.setOption(option, true)
      } catch (e) {
        console.error('图表渲染失败:', e)
      }
    },
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.path-dialog-container {
  padding: 10px;
}
.mb-20 {
  margin-bottom: 20px;
}
.info-item {
  text-align: center;
}
.info-label {
  color: #909399;
  font-size: 12px;
  margin-bottom: 8px;
}
.info-value {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}
.dialog-footer {
  text-align: right;
}
</style>