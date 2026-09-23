<template>
  <div class="page-container" :style="themeVars">
    <!-- Banner -->
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title"><i class="el-icon-share"></i> 采购风险穿透分析</h2>
        <p class="page-desc">从集团层逐层穿透查看各级企业采购风险，标记关联采购和违规采购</p>
      </div>
      <div class="header-right">
        <span style="margin-right:8px;font-size:13px;opacity:.9">穿透层级：</span>
        <el-radio-group v-model="treeDepth" size="small" @change="renderTree">
          <el-radio-button label="2">2层</el-radio-button>
          <el-radio-button label="3">3层</el-radio-button>
          <el-radio-button label="4">4层</el-radio-button>
          <el-radio-button label="5">5层</el-radio-button>
          <el-radio-button label="all">全量</el-radio-button>
        </el-radio-group>
      </div>
    </div>

    <!-- 主体：左树 + 右侧详情 -->
    <el-row :gutter="16" class="main-row">
      <!-- 左侧穿透树 -->
      <el-col :span="14">
        <el-card shadow="never" class="tree-card">
          <div slot="header" class="card-hd">
            <span><i class="el-icon-share" :style="{ color: ipSecondary, marginRight: '6px' }"></i>采购穿透树</span>
            <div class="legend-row">
              <span class="legend-dot" :style="{ background: ipPrimary }"></span><span>集团层</span>
              <span class="legend-dot" :style="{ background: ipSecondary, marginLeft: '8px' }"></span><span>一级</span>
              <span class="legend-dot" :style="{ background: ipBright, marginLeft: '8px' }"></span><span>二级</span>
              <span class="legend-dot" style="background:#36CFC9;margin-left:8px"></span><span>三级</span>
              <span class="legend-dot" style="background:#73D13D;margin-left:8px"></span><span>四级+</span>
              <span class="legend-dot" style="background:#CF1322;margin-left:8px"></span><span>高风险</span>
            </div>
          </div>
          <div ref="treeChart" class="tree-chart"></div>
          <div class="tree-tip">点击节点查看采购详情 →</div>
        </el-card>
      </el-col>

      <!-- 右侧详情面板 -->
      <el-col :span="10">
        <!-- 空状态 -->
        <el-card v-if="!selectedNode" shadow="never" class="detail-card detail-empty">
          <div class="empty-hint">
            <i class="el-icon-mouse" style="font-size:48px;color:#C0C4CC"></i>
            <p>点击左侧穿透树节点</p>
            <p>查看企业采购详情</p>
          </div>
        </el-card>

        <!-- 详情面板 -->
        <el-card v-else shadow="never" class="detail-card">
          <div slot="header" class="detail-hd">
            <div class="detail-title">
              <i class="el-icon-office-building" style="margin-right:6px"></i>
              {{ selectedNode.label }}
            </div>
            <el-tag :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }[selectedNode.riskLevel]" size="mini">
              {{ { HIGH: '高风险', MEDIUM: '中风险', LOW: '低风险' }[selectedNode.riskLevel] }}
            </el-tag>
          </div>

          <!-- KPI卡 -->
          <div class="kpi-row">
            <div v-for="kpi in selectedNode.kpis" :key="kpi.label" class="kpi-item" :style="{ borderTop: '3px solid ' + kpi.color }">
              <div class="kpi-v" :style="{ color: kpi.color }">{{ kpi.value }}</div>
              <div class="kpi-l">{{ kpi.label }}</div>
            </div>
          </div>

          <!-- 标签页 -->
          <el-tabs v-model="activeTab" size="small">
            <el-tab-pane label="采购明细" name="purchase">
              <el-table :data="selectedNode.purchaseList" size="small" border>
                <el-table-column label="项目名称" prop="name" min-width="100" show-overflow-tooltip />
                <el-table-column label="类型" prop="type" width="60" align="center" />
                <el-table-column label="金额(万)" prop="amount" width="80" align="right" />
                <el-table-column label="方式" prop="method" width="85" align="center" />
                <el-table-column label="状态" prop="status" width="80" align="center">
                  <template slot-scope="{ row }">
                    <el-tag :type="row.status.includes('逾期') || row.status.includes('违规') ? 'danger' : 'success'" size="mini">{{ row.status }}</el-tag>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
            <el-tab-pane label="风险预警" name="warning">
              <div v-if="selectedNode.warnings && selectedNode.warnings.length">
                <div v-for="w in selectedNode.warnings" :key="w.id" class="warn-item" :class="'warn-' + w.level.toLowerCase()">
                  <div class="warn-row1">
                    <el-tag :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'info' }[w.level]" size="mini">
                      {{ { HIGH: '高', MEDIUM: '中', LOW: '低' }[w.level] }}
                    </el-tag>
                    <span class="warn-id">{{ w.id }}</span>
                    <span class="warn-time">{{ w.time }}</span>
                  </div>
                  <div class="warn-tit">{{ w.title }}</div>
                  <div class="warn-desc">{{ w.desc }}</div>
                </div>
              </div>
              <el-empty v-else description="暂无预警" :image-size="60" />
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getProcurementDrillDownTree, getProcurementDrillDownDetail } from '@/api/stateAssets/procurementPenetration'
import { investThemeMixin } from '../../themeMixin'

function getColor(riskLevel, level, vm) {
  if (riskLevel === 'HIGH') return '#CF1322'
  if (level === 0) return (vm && vm.ipPrimary) || '#003A6C'
  if (level === 1) return (vm && vm.ipSecondary) || '#0050A0'
  if (level === 2) return (vm && vm.ipBright) || '#1677FF'
  if (level === 3) return '#36CFC9'
  return '#73D13D'
}

// 将后端树数据转换为echarts需要的格式
function transformTreeNode(node) {
  if (!node) return null
  return {
    name: node.nodeName || node.name || '',
    id: node.id,
    value: { riskLevel: node.riskLevel || 'LOW', level: node.nodeLevel != null ? node.nodeLevel : 0 },
    children: (node.children || []).map(c => transformTreeNode(c)),
  }
}

function processNode(node, maxDepth, depth, vm) {
  const val = node.value || {}
  const color = getColor(val.riskLevel, val.level, vm)
  const result = {
    name: node.name, id: node.id,
    itemStyle: { color, borderColor: color },
    label: { color: '#fff', backgroundColor: color, padding: [3, 8], borderRadius: 4, fontSize: 12 },
    children: [],
  }
  if (maxDepth === 'all' || depth < parseInt(maxDepth)) {
    result.children = (node.children || []).map(c => processNode(c, maxDepth, depth + 1, vm))
  }
  return result
}

export default {
  name: 'ProcurementDrillDown',
  mixins: [investThemeMixin],
  data() {
    return {
      treeDepth: 'all',
      treeChart: null,
      treeData: null,
      selectedNode: null,
      activeTab: 'purchase',
    }
  },
  mounted() {
    this.$nextTick(() => this.initTree())
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    if (this.treeChart) this.treeChart.dispose()
  },
  methods: {
    handleResize() { if (this.treeChart) this.treeChart.resize() },
    async initTree() {
      this.treeChart = echarts.init(this.$refs.treeChart)
      await this.fetchTreeData()
      this.renderTree()
      this.treeChart.on('click', (params) => {
        if (params.data && params.data.id) {
          this.fetchNodeDetail(params.data.id)
        }
      })
    },
    async fetchTreeData() {
      try {
        const res = await getProcurementDrillDownTree({ depth: this.treeDepth })
        if (res && res.result === 200 && res.data) {
          const treeArr = Array.isArray(res.data) ? res.data : [res.data]
          if (treeArr.length > 0) {
            this.treeData = transformTreeNode(treeArr[0])
            return
          }
        }
      } catch (e) {
        console.warn('获取穿透树数据失败', e)
      }
      this.treeData = null
    },
    async fetchNodeDetail(nodeId) {
      try {
        const res = await getProcurementDrillDownDetail(nodeId)
        if (res && res.result === 200 && res.data) {
          const d = res.data
          const node = d.node || {}
          const records = d.purchaseRecords || []
          this.selectedNode = {
            label: node.nodeName || nodeId,
            riskLevel: node.riskLevel || 'LOW',
            kpis: [
              { label: '采购总额(万元)', value: node.purchaseTotal || '0', color: this.ipSecondary },
              { label: '关联交易比', value: (node.relatedRatio || 0) + '%', color: (node.relatedRatio || 0) > 30 ? '#CF1322' : '#52C41A' },
              { label: '预警数', value: (node.warnCount || 0) + '条', color: (node.warnCount || 0) > 0 ? '#CF1322' : '#52C41A' },
            ],
            purchaseList: records.map(r => ({
              name: r.purchaseNo || '',
              type: r.purchaseType || '',
              amount: r.contractAmount || 0,
              method: r.biddingMethod || '',
              status: r.complianceStatus || '',
            })),
            warnings: d.warnings || [],
          }
          this.activeTab = 'purchase'
          return
        }
      } catch (e) {
        console.warn('获取节点详情失败', e)
      }
      this.$message.warning('暂无该节点的采购数据')
    },
    renderTree() {
      if (!this.treeChart || !this.treeData) return
      const node = processNode(this.treeData, this.treeDepth, 0, this)
      this.treeChart.clear()
      this.treeChart.setOption({
        tooltip: { trigger: 'item', formatter: (p) => p.name },
        series: [{
          type: 'tree', data: [node],
          orient: 'LR', left: '5%', right: '12%', top: '5%', bottom: '5%',
          symbol: 'circle', symbolSize: 10, roam: true,
          initialTreeDepth: this.treeDepth === 'all' ? 10 : parseInt(this.treeDepth),
          lineStyle: { color: '#C8D8F0', width: 1.5, curveness: 0.5 },
          expandAndCollapse: true,
          animationDuration: 550, animationDurationUpdate: 750,
        }],
      })
    },
  },
}
</script>

<style lang="scss" scoped>
.page-container { padding: 16px; background: #F0F2F5; min-height: calc(100vh - 84px); }
.page-header {
  display: flex; align-items: center; justify-content: space-between;
  padding: 18px 24px; margin-bottom: 16px;
  background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 60%, var(--ip-bright, #1677FF) 100%);
  border-radius: 8px; color: #fff;
  .page-title { margin: 0 0 4px 0; font-size: 18px; font-weight: 700; i { margin-right: 8px; } }
  .page-desc { margin: 0; font-size: 13px; opacity: .85; }
  .header-right { display: flex; align-items: center; flex-shrink: 0; }
}
.main-row { }
.tree-card { height: 560px; }
.tree-chart { height: 460px; }
.tree-tip { font-size: 12px; color: #8C8C8C; text-align: center; margin-top: -4px; }
.card-hd {
  display: flex; align-items: center; justify-content: space-between;
  font-size: 14px; font-weight: 600; color: #303133;
}
.legend-row {
  display: flex; align-items: center; font-size: 12px; color: #666;
  .legend-dot { width: 10px; height: 10px; border-radius: 50%; display: inline-block; margin-right: 4px; }
}
.detail-card { height: 560px; overflow-y: auto; }
.detail-empty {
  display: flex; align-items: center; justify-content: center;
  .empty-hint { text-align: center; color: #C0C4CC; p { margin: 6px 0; font-size: 14px; } }
}
.detail-hd {
  display: flex; align-items: center; justify-content: space-between;
  .detail-title { font-size: 14px; font-weight: 700; color: #262626; }
}
.kpi-row {
  display: flex; gap: 8px; margin-bottom: 12px;
  .kpi-item {
    flex: 1; padding: 8px 10px; background: #F8FAFE; border-radius: 4px; text-align: center;
    .kpi-v { font-size: 18px; font-weight: 700; }
    .kpi-l { font-size: 11px; color: #8C8C8C; margin-top: 2px; }
  }
}
.warn-item {
  padding: 10px 12px; border-radius: 6px; margin-bottom: 8px; border-left: 3px solid #d9d9d9; background: #fafafa;
  &.warn-high { border-left-color: #CF1322; background: #FFF1F0; }
  &.warn-medium { border-left-color: #FA8C16; background: #FFF7E6; }
  &.warn-low { border-left-color: var(--ip-bright, #1677FF); background: var(--ip-light-bg, #EBF1FF); }
  .warn-row1 { display: flex; align-items: center; gap: 6px; margin-bottom: 4px; }
  .warn-id, .warn-time { font-size: 12px; color: #8C8C8C; }
  .warn-time { margin-left: auto; }
  .warn-tit { font-size: 13px; color: #262626; font-weight: 500; margin-bottom: 2px; }
  .warn-desc { font-size: 12px; color: #666; }
}
::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); font-weight: 600; }
::v-deep .el-card { border-radius: 6px; }
::v-deep .el-card__header { padding: 12px 16px; border-bottom: 1px solid #F0F2F5; }
</style>
