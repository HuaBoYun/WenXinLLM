<template>
  <div class="military-page" :style="themeVars">
    <div class="page-header"><h2><i class="el-icon-share"></i> 军品风险穿透分析</h2><p>从集团层逐层穿透查看各级军工企业风险</p></div>
    <div class="depth-bar">
      <span>穿透层级：</span>
      <el-radio-group v-model="treeDepth" size="mini" @change="onDepthChange">
        <el-radio-button label="2">2层</el-radio-button>
        <el-radio-button label="3">3层</el-radio-button>
        <el-radio-button label="all">全量</el-radio-button>
      </el-radio-group>
      <span class="legend-bar" style="margin-left:20px">
        <span class="legend-dot" :style="{background: ipPrimary}"></span><span>集团层</span>
        <span class="legend-dot" :style="{background: ipSecondary}"></span><span>一级子企业</span>
        <span class="legend-dot" :style="{background: ipBright}"></span><span>二级子企业</span>
        <span class="legend-dot" style="background:#36A0FA"></span><span>三级子企业</span>
        <span class="legend-dot" style="background:#CF1322"></span><span>高风险</span>
      </span>
    </div>
    <div class="drill-content">
      <div class="drill-tree">
        <div v-if="treeError" style="display:flex;flex-direction:column;align-items:center;justify-content:center;height:580px;color:#C0C4CC">
          <i class="el-icon-warning-outline" style="font-size:40px"></i>
          <p style="margin-top:12px;font-size:14px">暂无穿透树数据，请先执行SQL测试数据初始化</p>
        </div>
        <div ref="treeChart" class="tree-chart" v-show="!treeError"></div>
      </div>
      <div class="drill-detail" v-if="selectedNode">
        <h3>{{ selectedNode.label }}</h3>
        <el-tag :type="selectedNode.riskLevel==='HIGH'?'danger':selectedNode.riskLevel==='MEDIUM'?'warning':'success'" size="small" style="margin-bottom:12px">
          {{ selectedNode.riskLevel==='HIGH'?'高风险':selectedNode.riskLevel==='MEDIUM'?'中风险':'低风险' }}
        </el-tag>
        <el-tabs v-model="activeTab">
          <el-tab-pane label="KPI概览" name="kpi">
            <div class="kpi-grid">
              <div v-for="k in selectedNode.kpis" :key="k.label" class="detail-kpi">
                <div class="dk-val" :style="{color:k.color}">{{ k.value }}</div>
                <div class="dk-label">{{ k.label }}</div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="任务明细" name="purchase">
            <el-table :data="selectedNode.taskList" border size="mini" style="width:100%">
              <el-table-column label="任务名称" prop="name" min-width="120" show-overflow-tooltip />
              <el-table-column label="类型" prop="type" width="60" align="center" />
              <el-table-column label="金额(万)" prop="amount" width="90" align="right" />
              <el-table-column label="状态" prop="status" width="90" align="center" />
              <el-table-column label="进度" prop="progress" width="70" align="center">
                <template slot-scope="{row}"><span>{{ row.progress }}%</span></template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane label="风险预警" name="warning">
            <div v-if="selectedNode.warnings && selectedNode.warnings.length === 0" style="text-align:center;color:#C0C4CC;padding:20px 0">
              <i class="el-icon-check" style="font-size:24px"></i><p style="margin-top:8px;font-size:13px">暂无风险预警</p>
            </div>
            <div v-for="w in selectedNode.warnings" :key="w.id" class="detail-warn" :class="'dw-'+(w.level||'').toLowerCase()">
              <el-tag :type="w.level==='HIGH'?'danger':w.level==='MEDIUM'?'warning':'info'" size="mini">{{ w.level==='HIGH'?'高':w.level==='MEDIUM'?'中':'低' }}</el-tag>
              <div class="dw-body">
                <div class="dw-title">{{ w.title }}</div>
                <div class="dw-desc">{{ w.desc }}</div>
                <div class="dw-time">{{ w.time }}</div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
      <div class="drill-detail drill-empty" v-else>
        <i class="el-icon-info" style="font-size:36px;color:#C0C4CC"></i>
        <p>点击左侧树节点查看企业军品详情</p>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getMilitaryDrillDownTree, getMilitaryDrillDownDetail } from '@/api/stateAssets/militaryPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'MilitaryDrillDown',
  mixins: [investThemeMixin],
  data() {
    return { treeDepth: '3', treeChart: null, selectedNode: null, activeTab: 'kpi', treeError: false, nodeMap: {} }
  },
  mounted() { this.$nextTick(() => this.loadTree()); window.addEventListener('resize', this.handleResize) },
  beforeDestroy() { window.removeEventListener('resize', this.handleResize); if (this.treeChart) { this.treeChart.dispose(); this.treeChart = null } },
  methods: {
    handleResize() { if (this.treeChart) this.treeChart.resize() },
    onDepthChange() {
      this.selectedNode = null
      this.loadTree()
    },
    filterTreeDepth(node, maxDepth, curDepth) {
      if (!node) return null
      const result = Object.assign({}, node)
      if (maxDepth !== 'all' && curDepth >= parseInt(maxDepth) - 1) {
        result.children = []
      } else {
        result.children = (node.children || []).map(c => this.filterTreeDepth(c, maxDepth, curDepth + 1)).filter(Boolean)
      }
      return result
    },
    async loadTree() {
      this.treeError = false
      let treeRoot = null
      try {
        const res = await getMilitaryDrillDownTree({ depth: this.treeDepth })
        if (res && res.result === 200 && res.data) {
          treeRoot = Array.isArray(res.data)
            ? { companyId: 'root', name: '集团总部', riskLevel: 'LOW', taskCount: 0, children: res.data }
            : res.data
        }
      } catch (e) {
        this.treeError = true
      }
      if (!treeRoot) {
        this.treeError = true
        return
      }
      const filteredRoot = this.filterTreeDepth(treeRoot, this.treeDepth, 0)
      this.renderTree(filteredRoot)
    },
    renderTree(root) {
      if (!root) return
      if (this.treeChart) { this.treeChart.off('click'); this.treeChart.dispose() }
      this.treeChart = echarts.init(this.$refs.treeChart)
      const levelColors = [this.ipPrimary, this.ipSecondary, this.ipBright, '#36A0FA']
      this.nodeMap = {}
      const buildNode = (item, level) => {
        const isHighRisk = item.riskLevel === 'HIGH'
        const color = isHighRisk ? '#CF1322' : (levelColors[level] || '#36A0FA')
        const nodeId = item.companyId || item.id || item.name
        this.nodeMap[nodeId] = item
        return {
          name: item.name || item.companyName || '未知',
          id: nodeId,
          value: item.taskCount || 0,
          itemStyle: { color },
          label: {
            color: '#fff',
            backgroundColor: color,
            padding: [3, 7],
            borderRadius: 4,
            fontSize: 12
          },
          children: (item.children || []).map(c => buildNode(c, level + 1))
        }
      }
      const treeNode = buildNode(root, 0)
      this.treeChart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: p => {
            if (!p.data) return ''
            const raw = this.nodeMap[p.data.id] || {}
            return `<b>${p.data.name}</b><br/>` +
              `任务数: ${raw.taskCount || 0}<br/>` +
              `风险等级: ${{ HIGH: '高风险', MEDIUM: '中风险', LOW: '低风险' }[raw.riskLevel] || '低风险'}`
          }
        },
        series: [{
          type: 'tree',
          data: [treeNode],
          top: '5%', left: '12%', bottom: '5%', right: '18%',
          symbolSize: 10,
          orient: 'LR',
          label: { position: 'left', verticalAlign: 'middle', align: 'right', fontSize: 11 },
          leaves: { label: { position: 'right', align: 'left' } },
          lineStyle: { color: '#C0C4CC', width: 1.5 },
          expandAndCollapse: true,
          initialTreeDepth: this.treeDepth === 'all' ? -1 : parseInt(this.treeDepth),
          animationDuration: 550
        }]
      })
      this.treeChart.on('click', async (params) => {
        if (params.data && params.data.id) {
          const nodeId = params.data.id
          const nodeName = params.data.name
          const rawNode = this.nodeMap[nodeId] || null
          await this.loadNodeDetail(nodeId, nodeName, rawNode)
        }
      })
    },
    async loadNodeDetail(nodeId, nodeName, rawNode) {
      let detail = null
      try {
        const res = await getMilitaryDrillDownDetail(nodeId)
        if (res && res.result === 200 && res.data) detail = res.data
      } catch (e) { /* ignore */ }
      // 接口失败时用节点原始数据做最简展示
      if (!detail) {
        detail = rawNode ? {
          riskLevel: rawNode.riskLevel || 'LOW',
          taskCount: rawNode.taskCount || 0,
          completedCount: rawNode.completedCount || 0,
          onTimeRate: rawNode.onTimeRate || '—',
          alertCount: rawNode.alertCount || 0,
          contractAmount: '—',
          tasks: [],
          alerts: []
        } : { riskLevel: 'LOW', taskCount: 0, completedCount: 0, onTimeRate: '—', alertCount: 0, contractAmount: '—', tasks: [], alerts: [] }
      }
      this.selectedNode = {
        label: nodeName,
        riskLevel: detail.riskLevel || 'LOW',
        kpis: [
          { label: '任务总数', value: String(detail.taskCount || 0), color: this.ipSecondary },
          { label: '已完成', value: String(detail.completedCount || 0), color: '#52C41A' },
          { label: '按时完成率', value: detail.onTimeRate || '—', color: '#FA8C16' },
          { label: '预警数', value: String(detail.alertCount || (detail.alerts || []).length || 0), color: '#CF1322' },
          { label: '合同金额(元)', value: detail.contractAmount || '—', color: '#722ED1' },
          { label: '风险等级', value: { HIGH: '高', MEDIUM: '中', LOW: '低' }[detail.riskLevel] || '低', color: detail.riskLevel === 'HIGH' ? '#CF1322' : detail.riskLevel === 'MEDIUM' ? '#FA8C16' : '#52C41A' }
        ],
        taskList: (detail.tasks || []).map(t => ({
          name: t.taskName || t.name,
          type: t.taskType || t.type,
          amount: t.amount || '—',
          status: t.status,
          progress: t.progressRate != null ? Number(t.progressRate) : (t.progress || 0)
        })),
        warnings: (detail.alerts || []).map(a => ({
          id: a.alertId || a.id,
          level: a.level,
          title: a.alertContent || a.title,
          desc: a.desc || a.alertContent || '',
          time: a.createTime || a.time
        }))
      }
      this.activeTab = 'kpi'
    }
  }
}
</script>
<style lang="scss" scoped>
.military-page { padding:16px; background:#f0f2f5; min-height:calc(100vh - 84px); }
.page-header { display:flex; align-items:center; justify-content:space-between; margin-bottom:12px; padding:18px 24px; background:linear-gradient(135deg,var(--ip-primary, #003A6C) 0%,var(--ip-secondary, #0050A0) 60%,var(--ip-bright, #1677FF) 100%); border-radius:6px; color:#fff;
  h2{font-size:20px;margin:0 0 4px;i{margin-right:8px;}}p{font-size:13px;opacity:0.85;margin:0;} }
.depth-bar { display:flex; align-items:center; gap:8px; margin-bottom:12px; padding:10px 16px; background:#fff; border-radius:6px; font-size:13px; color:#606266;
  .legend-bar { display:flex; align-items:center; gap:10px; font-size:12px; color:#8C8C8C; }
  .legend-dot { width:10px; height:10px; border-radius:2px; display:inline-block; margin-right:3px; } }
.drill-content { display:flex; gap:16px; }
.drill-tree { flex:1; background:#fff; border-radius:6px; padding:8px; }
.tree-chart { height:580px; width:100%; }
.drill-detail { width:420px; background:#fff; border-radius:6px; padding:16px; overflow-y:auto; max-height:540px;
  h3 { font-size:16px; margin:0 0 8px; color:#303133; }
  .kpi-grid { display:grid; grid-template-columns:1fr 1fr 1fr; gap:10px; margin-top:8px; }
  .detail-kpi { text-align:center; padding:10px 6px; background:#F5F7FA; border-radius:6px;
    .dk-val { font-size:20px; font-weight:700; line-height:1.2; }
    .dk-label { font-size:11px; color:#8C8C8C; margin-top:4px; } }
  .detail-warn { display:flex; gap:8px; padding:10px; border-radius:6px; margin-bottom:8px; &.dw-high { background:#FFF1F0; } &.dw-medium { background:#FFF7E6; }
    .dw-body { flex:1; .dw-title { font-size:13px; font-weight:600; color:#303133; } .dw-desc { font-size:12px; color:#606266; margin-top:4px; line-height:1.5; } .dw-time { font-size:11px; color:#BFBFBF; margin-top:4px; } } }
  &.drill-empty { display:flex; flex-direction:column; align-items:center; justify-content:center; color:#C0C4CC; p { margin-top:12px; font-size:14px; } } }
</style>
