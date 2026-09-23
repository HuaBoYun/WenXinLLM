<template>
  <div class="drill-page" :style="themeVars">
    <!-- Banner -->
    <div class="drill-banner">
      <div class="banner-left">
        <i class="el-icon-s-operation"></i>
        <span>金融风险穿透分析</span>
        <span class="banner-sub">从集团层逐层穿透，识别各级企业金融风险敞口</span>
      </div>
      <div class="banner-right">
        <el-radio-group v-model="treeDepth" size="small" @change="loadTreeData">
          <el-radio-button label="2">2层穿透</el-radio-button>
          <el-radio-button label="3">3层穿透</el-radio-button>
          <el-radio-button label="all">全量穿透</el-radio-button>
        </el-radio-group>
      </div>
    </div>

    <!-- 主体：左树 + 右详情 -->
    <el-row :gutter="16" style="margin-top:16px">
      <!-- 左侧穿透树 -->
      <el-col :span="10">
        <el-card shadow="never" class="tree-card">
          <div slot="header" class="card-header">
            <span><i class="el-icon-share" :style="{ color: ipSecondary, marginRight: '6px' }"></i>金融风险穿透树</span>
            <div class="legend">
              <span class="legend-dot" :style="{ background: ipPrimary }"></span>集团
              <span class="legend-dot" :style="{ background: ipBright, marginLeft: '8px' }"></span>子企业
              <span class="legend-dot" style="background:#FF4D4F;margin-left:8px"></span>高风险
              <span class="legend-dot" :style="{ background: '#FA8C16', marginLeft: '8px' }"></span>质押担保
            </div>
          </div>
          <div ref="treeChart" style="height:520px;width:100%"></div>
          <div class="tree-tip">点击节点查看详细金融风险信息</div>
        </el-card>
      </el-col>

      <!-- 右侧详情 -->
      <el-col :span="14">
        <!-- 未选中企业时的提示 -->
        <el-card v-if="!selectedNode.id" shadow="never" class="empty-card">
          <div class="empty-hint">
            <i class="el-icon-arrow-left"></i>
            <p>请在左侧穿透树中点击企业节点</p>
            <p style="font-size:13px;color:#bfbfbf;margin-top:4px">查看该企业的金融风险详情</p>
          </div>
        </el-card>

        <!-- 选中后展示详情 -->
        <template v-else>
          <!-- 企业头部信息 -->
          <el-card shadow="never" class="detail-header-card mb-12">
            <div class="detail-header">
              <div class="detail-header-left">
                <div class="detail-company-name">{{ selectedNode.label }}</div>
                <div class="detail-meta">
                  <el-tag size="small" :type="riskTagType(selectedNode.riskLevel)">
                    {{ selectedNode.riskLevel === 'HIGH' ? '高风险' : selectedNode.riskLevel === 'MEDIUM' ? '中风险' : '低风险' }}
                  </el-tag>
                  <span class="meta-item"><i class="el-icon-office-building"></i> {{ selectedNode.level }}级子企业</span>
                  <span class="meta-item"><i class="el-icon-time"></i> 更新：2025-12-31</span>
                </div>
              </div>
              <div class="detail-header-right">
                <el-button size="small" type="primary" icon="el-icon-document" plain @click="handleGenerateReport">生成报告</el-button>
              </div>
            </div>
          </el-card>

          <!-- KPI 统计卡 -->
          <el-row :gutter="12" class="mb-12">
            <el-col :span="8" v-for="kpi in selectedKpis" :key="kpi.label">
              <div class="kpi-card" :style="{ borderLeft: '3px solid ' + kpi.color }">
                <div class="kpi-val" :style="{ color: kpi.color }">{{ kpi.value }}</div>
                <div class="kpi-label">{{ kpi.label }}</div>
              </div>
            </el-col>
          </el-row>

          <!-- 标签页：融资明细 / 担保明细 / 预警清单 -->
          <el-card shadow="never">
            <el-tabs v-model="activeTab">
              <!-- 融资明细 -->
              <el-tab-pane label="融资明细" name="financing">
                <el-table :data="selectedNode.financingList" border size="small" :row-class-name="finRowClass">
                  <el-table-column label="融资名称" prop="name" min-width="140" show-overflow-tooltip />
                  <el-table-column label="类型" prop="type" width="90" align="center">
                    <template slot-scope="s">
                      <el-tag size="mini" type="primary">{{ s.row.type }}</el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column label="金额(万元)" prop="amount" width="110" align="right">
                    <template slot-scope="s">
                      <span :style="{ fontWeight: '600', color: ipSecondary }">{{ s.row.amount }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="到期日" prop="dueDate" width="100" align="center" />
                  <el-table-column label="状态" prop="status" width="80" align="center">
                    <template slot-scope="s">
                      <el-tag size="mini" :type="s.row.status === '逾期' ? 'danger' : s.row.status === '正常' ? 'success' : 'warning'">{{ s.row.status }}</el-tag>
                    </template>
                  </el-table-column>
                </el-table>
              </el-tab-pane>

              <!-- 担保明细 -->
              <el-tab-pane label="担保明细" name="guarantee">
                <el-table :data="selectedNode.guaranteeList" border size="small">
                  <el-table-column label="担保项目" prop="name" min-width="140" show-overflow-tooltip />
                  <el-table-column label="担保类型" prop="type" width="100" align="center">
                    <template slot-scope="s">
                      <el-tag size="mini" :type="s.row.type === '互保' ? 'warning' : 'info'">{{ s.row.type }}</el-tag>
                    </template>
                  </el-table-column>
                  <el-table-column label="金额(万元)" prop="amount" width="110" align="right">
                    <template slot-scope="s">
                      <span style="font-weight:600">{{ s.row.amount }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="被担保方" prop="counterpart" min-width="120" show-overflow-tooltip />
                  <el-table-column label="到期日" prop="dueDate" width="100" align="center" />
                </el-table>
              </el-tab-pane>

              <!-- 预警清单 -->
              <el-tab-pane label="预警清单" name="warnings">
                <div v-if="selectedNode.warnings && selectedNode.warnings.length">
                  <div v-for="w in selectedNode.warnings" :key="w.id" class="warn-item" :class="'warn-' + w.level.toLowerCase()">
                    <div class="warn-header">
                      <el-tag size="mini" :type="w.level === 'HIGH' ? 'danger' : w.level === 'MEDIUM' ? 'warning' : 'info'">{{ w.level === 'HIGH' ? '高危' : w.level === 'MEDIUM' ? '中危' : '低危' }}</el-tag>
                      <span class="warn-title">{{ w.title }}</span>
                      <span class="warn-time">{{ w.time }}</span>
                    </div>
                    <div class="warn-desc">{{ w.desc }}</div>
                  </div>
                </div>
                <el-empty v-else description="暂无预警信息" :image-size="60" />
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </template>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getFinancialRiskDrillDownTree, getFinancialRiskDrillDownDetail } from '@/api/stateAssets/financialRiskPenetration'
import { investThemeMixin } from '../../themeMixin'



export default {
  name: 'FinancialRiskDrillDown',
  mixins: [investThemeMixin],
  data() {
    return {
      treeDepth: '3',
      treeChart: null,
      selectedNode: {},
      activeTab: 'financing',
      treeData: null,
      loading: false,
    }
  },
  computed: {
    selectedKpis() { return this.selectedNode.kpis || [] },
  },
  mounted() {
    this.$nextTick(() => { this.loadTreeData() })
  },
  beforeDestroy() {
    if (this.treeChart) { this.treeChart.dispose(); this.treeChart = null }
    window.removeEventListener('resize', this.resizeHandler)
  },
  methods: {
    getNodeColor(riskLevel, level) {
      if (riskLevel === 'HIGH') return '#FF4D4F'
      if (level === 0) return this.ipPrimary
      if (level === 1) return this.ipSecondary
      return this.ipBright
    },
    processNode(node, maxDepth, currentDepth) {
      const rl = node.riskLevel || (node.value && node.value.riskLevel) || 'LOW'
      const lv = node.level || (node.value && node.value.level) || currentDepth
      const color = this.getNodeColor(rl, lv)
      const processed = {
        name: node.name,
        id: node.id || node.key,
        itemStyle: { color: color, borderColor: color },
        label: { color: '#fff', backgroundColor: color, padding: [4, 8], borderRadius: 4, fontSize: 12 },
        lineStyle: { color: '#C8D8F0' },
        emphasis: { itemStyle: { borderWidth: 3, borderColor: this.ipAccent, shadowBlur: 6, shadowColor: 'rgba(250,173,20,0.5)' } },
        children: [],
      }
      if (maxDepth === 'all' || currentDepth < parseInt(maxDepth)) {
        processed.children = (node.children || []).map(c => this.processNode(c, maxDepth, currentDepth + 1))
      }
      return processed
    },
    loadTreeData() {
      this.loading = true
      getFinancialRiskDrillDownTree({ depth: this.treeDepth }).then(res => {
        if (res.data) {
          this.treeData = res.data
          this.initTree()
        }
      }).catch((err) => {
        console.error('加载穿透树失败', err)
        this.$message.error('加载穿透树失败')
      }).finally(() => { this.loading = false })
    },
    initTree() {
      const el = this.$refs.treeChart
      if (!el) return
      if (!this.treeChart) {
        this.treeChart = echarts.init(el)
        this.resizeHandler = () => this.treeChart && this.treeChart.resize()
        window.addEventListener('resize', this.resizeHandler)
        this.treeChart.on('click', (params) => {
          if (params.data && params.data.id) {
            this.loadNodeDetail(params.data.id)
          }
        })
      }
      this.renderTree()
    },
    loadNodeDetail(nodeId) {
      getFinancialRiskDrillDownDetail(nodeId).then(res => {
        if (res.data) {
          this.selectedNode = res.data
          this.activeTab = 'financing'
        }
      }).catch((err) => {
        console.error('加载节点详情失败', err)
        this.$message.error('加载节点详情失败')
      })
    },
    renderTree() {
      if (!this.treeChart || !this.treeData) return
      const treeNode = this.processNode(this.treeData, this.treeDepth, 0)
      this.treeChart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: (params) => {
            const name = params.data.name || params.name
            return `<b>${name}</b><br/>点击查看详细信息`
          },
          backgroundColor: '#fff', borderColor: '#e0e7ff', textStyle: { color: '#303133', fontSize: 13 },
        },
        series: [{
          type: 'tree', data: [treeNode], orient: 'LR',
          left: '5%', right: '10%', top: '5%', bottom: '5%',
          symbol: 'circle', symbolSize: 12, roam: true,
          initialTreeDepth: this.treeDepth === 'all' ? 10 : parseInt(this.treeDepth),
          lineStyle: { color: '#C8D8F0', width: 1.5, curveness: 0.5 },
          label: { position: 'right', fontSize: 12, color: '#303133' },
          leaves: { label: { position: 'right', fontSize: 12 } },
          emphasis: { focus: 'ancestor' },
          expandAndCollapse: true, animationDuration: 550, animationDurationUpdate: 750,
        }],
      }, true) // notMerge=true 强制完全重绘
    },
    handleGenerateReport() {
      if (!this.selectedNode || !this.selectedNode.id) {
        this.$message.warning('请先选择一个企业节点')
        return
      }
      const loading = this.$loading({ lock: true, text: '正在生成报告...', background: 'rgba(0, 0, 0, 0.7)' })
      const node = this.selectedNode
      // 基于当前选中节点数据生成报告
      setTimeout(() => {
        loading.close()
        const kpis = node.kpis || []
        const financingList = node.financingList || []
        const guaranteeList = node.guaranteeList || []

        let reportContent = `【${node.name || ''}】金融风险穿透分析报告\n`
        reportContent += `━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n`
        reportContent += `生成时间：${new Date().toLocaleString()}\n`
        reportContent += `风险等级：${node.riskLevel === 'HIGH' ? '高风险' : node.riskLevel === 'MEDIUM' ? '中风险' : '低风险'}\n`
        reportContent += `企业层级：${node.level || '-'}级子企业\n\n`

        reportContent += `【关键指标】\n`
        kpis.forEach(k => { reportContent += `  ${k.label}：${k.value}${k.unit || ''}\n` })

        reportContent += `\n【融资记录】共${financingList.length}笔\n`
        financingList.forEach((f, i) => { reportContent += `  ${i + 1}. ${f.lender || '-'} | ${f.amount || '-'}万元 | ${f.rate || '-'}% | ${f.status || '-'}\n` })

        reportContent += `\n【担保记录】共${guaranteeList.length}笔\n`
        guaranteeList.forEach((g, i) => { reportContent += `  ${i + 1}. ${g.guaranteedName || '-'} | ${g.amount || '-'}万元 | ${g.status || '-'}\n` })

        // 下载为文本文件
        const blob = new Blob([reportContent], { type: 'text/plain;charset=utf-8' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `${node.name || '企业'}_金融风险穿透报告_${new Date().getTime()}.txt`
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('报告生成成功')
      }, 800)
    },
    riskTagType(level) { return level === 'HIGH' ? 'danger' : level === 'MEDIUM' ? 'warning' : 'success' },
    finRowClass({ row }) {
      if (row.status === '逾期') return 'overdue-row'
      if (row.status === '即将到期') return 'near-row'
      return ''
    },
  },
}
</script>


<style lang="scss" scoped>
.drill-page {
  padding: 16px;
  background: #f0f2f5;
  min-height: calc(100vh - 84px);
}

.drill-banner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 24px;
  background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 60%, var(--ip-bright, #1677FF) 100%);
  border-radius: 8px;
  color: #fff;

  .banner-left {
    display: flex;
    align-items: center;
    font-size: 17px;
    font-weight: 600;

    i {
      font-size: 22px;
      margin-right: 10px;
    }

    .banner-sub {
      font-size: 13px;
      font-weight: 400;
      opacity: 0.8;
      margin-left: 16px;
    }
  }
}

.mb-12 { margin-bottom: 12px; }

.tree-card {
  min-height: 580px;

  .card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-weight: 600;
    color: #303133;

    .legend {
      display: flex;
      align-items: center;
      font-size: 12px;
      color: #606266;
      font-weight: 400;

      .legend-dot {
        display: inline-block;
        width: 10px;
        height: 10px;
        border-radius: 50%;
      }
    }
  }

  .tree-tip {
    text-align: center;
    font-size: 12px;
    color: #bfbfbf;
    margin-top: 4px;
  }
}

.empty-card {
  min-height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;

  .empty-hint {
    text-align: center;
    color: #bfbfbf;
    font-size: 15px;

    i {
      font-size: 36px;
      display: block;
      margin-bottom: 8px;
    }
  }
}

.detail-header-card {
  .detail-header {
    display: flex;
    align-items: center;
    justify-content: space-between;

    .detail-company-name {
      font-size: 18px;
      font-weight: 700;
      color: var(--ip-primary, #003A6C);
    }

    .detail-meta {
      display: flex;
      align-items: center;
      gap: 12px;
      margin-top: 6px;
      font-size: 13px;
      color: #606266;

      .meta-item {
        i { margin-right: 4px; }
      }
    }
  }
}

.kpi-card {
  background: #fff;
  border-radius: 6px;
  padding: 12px 16px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);

  .kpi-val {
    font-size: 20px;
    font-weight: 700;
    line-height: 1.2;
  }

  .kpi-label {
    font-size: 12px;
    color: #909399;
    margin-top: 4px;
  }
}

.warn-item {
  padding: 10px 14px;
  border-radius: 6px;
  margin-bottom: 10px;
  border-left: 4px solid #ccc;

  &.warn-high {
    background: #FFF1F0;
    border-color: #FF4D4F;
  }

  &.warn-medium {
    background: #FFF7E6;
    border-color: #FA8C16;
  }

  &.warn-low {
    background: #F6FFED;
    border-color: #52C41A;
  }

  .warn-header {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 4px;

    .warn-title {
      font-weight: 600;
      color: #303133;
      flex: 1;
    }

    .warn-time {
      font-size: 12px;
      color: #909399;
    }
  }

  .warn-desc {
    font-size: 13px;
    color: #606266;
    line-height: 1.6;
  }
}

::v-deep .el-table th {
  background: var(--ip-light-bg, #EBF1FF) !important;
  color: var(--ip-secondary, #0050A0);
  font-weight: 600;
}

::v-deep .el-table .overdue-row {
  background: #FFF1F0 !important;
}

::v-deep .el-table .near-row {
  background: #FFF7E6 !important;
}

::v-deep .el-card {
  border-radius: 6px;
}

::v-deep .el-tabs__header {
  margin-bottom: 12px;
}
</style>
