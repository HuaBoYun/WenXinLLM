<template>
  <div class="procurement-home-container" :style="themeVars">
    <!-- Banner -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title"><i class="el-icon-shopping-cart-full"></i> 采购穿透监管平台</h1>
        <p class="page-desc">依托穿透式采购数据图谱与供应商风险监控平台，实现"采购一张图、合规一本账"</p>
      </div>
      <div class="header-right">
        <el-button size="small" type="primary" plain icon="el-icon-refresh" @click="refreshData">刷新数据</el-button>
        <el-button size="small" plain icon="el-icon-document" @click="$router.push('/fxyj/Cgxmtz')">采购台账</el-button>
      </div>
    </div>

    <!-- 6个KPI统计卡 -->
    <el-row :gutter="16" class="kpi-row">
      <el-col :span="4" v-for="kpi in kpiCards" :key="kpi.label">
        <div class="kpi-card" :style="{ borderLeft: '4px solid ' + kpi.color }">
          <div class="kpi-icon" :style="{ color: kpi.color }"><i :class="kpi.icon"></i></div>
          <div class="kpi-body">
            <div class="kpi-val" :style="{ color: kpi.color }">{{ kpi.value }}</div>
            <div class="kpi-label">{{ kpi.label }}</div>
            <div class="kpi-trend" :style="{ color: kpi.trendColor || '#8c8c8c' }">{{ kpi.trend }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 主体：左侧穿透树 + 右侧预警清单 -->
    <el-row :gutter="16" class="main-row">
      <el-col :span="14">
        <el-card shadow="never" class="tree-card">
          <div slot="header" class="card-hd">
            <span><i class="el-icon-share" :style="{color: ipSecondary, marginRight: '6px'}"></i>采购穿透树形图</span>
            <el-radio-group v-model="treeDepth" size="mini" @change="renderTree">
              <el-radio-button label="2">2层</el-radio-button>
              <el-radio-button label="3">3层</el-radio-button>
              <el-radio-button label="all">全量</el-radio-button>
            </el-radio-group>
          </div>
          <div ref="procTreeChart" class="tree-chart"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never" class="warning-card">
          <div slot="header" class="card-hd">
            <span><i class="el-icon-warning" style="color:#CF1322;margin-right:6px"></i>活跃预警清单</span>
            <el-tag size="mini" type="danger">{{ warnings.length }} 条</el-tag>
          </div>
          <div class="warning-list">
            <div v-for="w in warnings" :key="w.id" class="warning-item" :class="'warn-' + w.level.toLowerCase()">
              <div class="warn-header">
                <el-tag :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'info' }[w.level]" size="mini">
                  {{ { HIGH: '高', MEDIUM: '中', LOW: '低' }[w.level] }}
                </el-tag>
                <span class="warn-id">{{ w.id }}</span>
                <span class="warn-time">{{ w.time }}</span>
              </div>
              <div class="warn-title">{{ w.title }}</div>
              <div class="warn-company">{{ w.company }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 8个功能导航卡 -->
    <div class="nav-section">
      <div class="section-title"><i class="el-icon-grid"></i> 功能导航</div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="nav in navCards" :key="nav.title" style="margin-bottom:16px">
          <div class="nav-card" @click="handleNavClick(nav)">
            <div class="nav-icon" :style="{ background: nav.color + '18', color: nav.color }">
              <i :class="nav.icon"></i>
            </div>
            <div class="nav-body">
              <div class="nav-title">{{ nav.title }}</div>
              <div class="nav-desc">{{ nav.desc }}</div>
            </div>
            <i class="el-icon-arrow-right nav-arrow"></i>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getProcurementKPI, getProcurementTree, getProcurementWarnings } from '@/api/stateAssets/procurementPenetration'
import { investThemeMixin } from '../../themeMixin'

function getNodeColor(riskLevel, level, colors) {
  if (riskLevel === 'HIGH') return '#CF1322'
  if (level === 0) return colors.primary
  if (level === 1) return colors.secondary
  return colors.bright
}

// 将后端树数据转换为echarts需要的格式
function transformTreeNode(node) {
  if (!node) return null
  return {
    name: node.nodeName || node.name || '',
    id: node.id,
    value: { riskLevel: node.riskLevel || 'LOW', level: node.nodeLevel != null ? node.nodeLevel : 0, purchaseTotal: node.purchaseTotal, relatedRatio: node.relatedRatio, warnCount: node.warnCount },
    children: (node.children || []).map(c => transformTreeNode(c)),
  }
}

function processNode(node, maxDepth, depth, colors) {
  const val = node.value || {}
  const color = getNodeColor(val.riskLevel, val.level, colors)
  const result = {
    name: node.name,
    id: node.id,
    itemStyle: { color, borderColor: color },
    label: { color: '#fff', backgroundColor: color, padding: [3, 7], borderRadius: 4, fontSize: 12 },
    children: [],
  }
  if (maxDepth === 'all' || depth < parseInt(maxDepth)) {
    result.children = (node.children || []).map(c => processNode(c, maxDepth, depth + 1, colors))
  }
  return result
}

export default {
  name: 'ProcurementHome',
  mixins: [investThemeMixin],
  data() {
    return {
      kpiCards: [],
      treeData: null,
      treeDepth: 'all',
      warnings: [],
      treeChart: null,
      navCardsConfig: [
        { title: '采购驾驶舱', desc: '集团采购综合大屏', icon: 'el-icon-data-analysis', colorKey: 'secondary', path: '/fxyj/Cgjkjsc' },
        { title: '采购台账', desc: '采购记录台账查询', icon: 'el-icon-tickets', colorKey: 'bright', path: '/fxyj/Cgxmtz' },
        { title: '供应商档案', desc: '供应商资质与风险管理', icon: 'el-icon-s-custom', color: '#52C41A', path: '/fxyj/Gysda' },
        { title: '关联交易监控', desc: '关联方采购识别与预警', icon: 'el-icon-connection', color: '#722ED1', path: '/fxyj/Gljyjk' },
        { title: '招投标合规', desc: '招标方式合规性分析', icon: 'el-icon-document', color: '#FAAD14', path: '/fxyj/Ztbhg' },
        { title: '合同履约追踪', desc: '合同执行全程监控', icon: 'el-icon-document-checked', color: '#FA8C16', path: '/fxyj/Htlyzz' },
        { title: '采购风险穿透', desc: '多层穿透下钻分析', icon: 'el-icon-share', color: '#CF1322', path: '/fxyj/Cgfxct' },
        { title: '招标过程监控', desc: '围标串标风险识别', icon: 'el-icon-s-order', color: '#13C2C2', path: '/fxyj/Zbgcjk' },
      ],
    }
  },
  computed: {
    navCards() {
      const colorMap = { primary: this.ipPrimary, secondary: this.ipSecondary, bright: this.ipBright }
      return this.navCardsConfig.map(item => ({
        ...item,
        color: item.colorKey ? colorMap[item.colorKey] : item.color,
      }))
    },
  },
  created() {
    this.fetchData()
  },
  mounted() {
    this.initTree()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    if (this.treeChart) this.treeChart.dispose()
  },
  methods: {
    async fetchData() {
      try {
        const [kpiRes, treeRes, warnRes] = await Promise.all([
          getProcurementKPI(),
          getProcurementTree(),
          getProcurementWarnings(),
        ])
        // KPI
        if (kpiRes && kpiRes.result === 200 && kpiRes.data) {
          const d = kpiRes.data
          const val = (v) => v != null && v !== '' ? v : '--'
          this.kpiCards = [
            { label: '集团采购总额(亿元)', value: val(d.totalPurchaseAmount), icon: 'el-icon-shopping-cart-2', color: this.ipSecondary, trend: d.totalPurchaseAmountTrend || '', trendColor: '#CF1322' },
            { label: '注册供应商数', value: val(d.supplierCount), icon: 'el-icon-s-custom', color: this.ipBright, trend: d.supplierCountTrend || '', trendColor: '#CF1322' },
            { label: '关联交易占比', value: val(d.relatedTransactionRatio), icon: 'el-icon-connection', color: '#722ED1', trend: d.relatedTransactionRatioTrend || '', trendColor: '#CF1322' },
            { label: '招投标合规率', value: val(d.biddingComplianceRate), icon: 'el-icon-circle-check', color: '#52C41A', trend: d.biddingComplianceRateTrend || '', trendColor: '#389E0D' },
            { label: '活跃预警', value: val(d.activeAlerts), icon: 'el-icon-warning', color: '#CF1322', trend: d.activeAlertsTrend || '', trendColor: '#CF1322' },
            { label: '项目数量', value: val(d.projectCount), icon: 'el-icon-document-checked', color: '#FA8C16', trend: d.projectCountTrend || '', trendColor: '#FA8C16' },
          ]
        }
        // Tree - API返回数组，取第一个作为根节点
        if (treeRes && treeRes.result === 200 && treeRes.data) {
          const treeArr = Array.isArray(treeRes.data) ? treeRes.data : [treeRes.data]
          if (treeArr.length > 0) {
            this.treeData = transformTreeNode(treeArr[0])
            this.$nextTick(() => { this.renderTree() })
          }
        }
        // Warnings
        if (warnRes && warnRes.result === 200 && warnRes.data) {
          this.warnings = (warnRes.data || []).map(w => ({
            id: w.warningNo || w.id,
            level: w.warningLevel || 'MEDIUM',
            title: w.title,
            time: w.warningTime,
            company: w.companyName,
          }))
        }
      } catch (e) {
        console.error('采购首页数据加载失败', e)
        this.$message.error('数据加载失败，请稍后重试')
      }
    },
    refreshData() {
      this.fetchData()
      this.$message.success('数据已刷新')
    },
    handleResize() { if (this.treeChart) this.treeChart.resize() },
    initTree() {
      if (!this.$refs.procTreeChart) return
      this.treeChart = echarts.init(this.$refs.procTreeChart)
    },
    renderTree() {
      if (!this.treeChart) this.initTree()
      if (!this.treeChart || !this.treeData) return
      const colors = { primary: this.ipPrimary, secondary: this.ipSecondary, bright: this.ipBright }
      const treeNode = processNode(this.treeData, this.treeDepth, 0, colors)
      this.treeChart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: (params) => `<b>${params.name}</b>`,
        },
        series: [{
          type: 'tree', data: [treeNode], orient: 'LR',
          left: '5%', right: '12%', top: '8%', bottom: '8%',
          symbol: 'circle', symbolSize: 10, roam: true,
          initialTreeDepth: this.treeDepth === 'all' ? 10 : parseInt(this.treeDepth),
          lineStyle: { color: '#C8D8F0', width: 1.5, curveness: 0.5 },
          expandAndCollapse: true, animationDuration: 550, animationDurationUpdate: 750,
        }],
      })
    },
    handleNavClick(nav) { this.$router.push(nav.path) },
  },
}
</script>

<style lang="scss" scoped>
.procurement-home-container {
  padding: 16px;
  background: #F0F2F5;
  min-height: calc(100vh - 84px);
}

/* Banner */
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 28px;
  margin-bottom: 16px;
  background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 60%, var(--ip-bright, #1677FF) 100%);
  border-radius: 8px;
  color: #fff;
  .page-title {
    margin: 0 0 6px 0;
    font-size: 20px;
    font-weight: 700;
    i { margin-right: 8px; }
  }
  .page-desc {
    margin: 0;
    font-size: 13px;
    opacity: 0.85;
  }
  .header-right {
    display: flex;
    gap: 8px;
    flex-shrink: 0;
  }
}

/* KPI卡 */
.kpi-row { margin-bottom: 16px; }
.kpi-card {
  background: #fff;
  border-radius: 6px;
  padding: 14px 16px;
  display: flex;
  align-items: flex-start;
  gap: 12px;
  box-shadow: 0 1px 4px rgba(0,0,0,.07);
  .kpi-icon {
    font-size: 24px;
    margin-top: 2px;
  }
  .kpi-val {
    font-size: 22px;
    font-weight: 700;
    line-height: 1.2;
  }
  .kpi-label {
    font-size: 12px;
    color: #8C8C8C;
    margin: 2px 0;
  }
  .kpi-trend {
    font-size: 12px;
    font-weight: 600;
  }
}

/* 主体区 */
.main-row { margin-bottom: 16px; }
.tree-card, .warning-card { height: 380px; }
.tree-chart { height: 310px; }

.card-hd {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

/* 预警列表 */
.warning-list { overflow-y: auto; max-height: 300px; }
.warning-item {
  padding: 10px 12px;
  border-radius: 6px;
  margin-bottom: 8px;
  border-left: 3px solid #d9d9d9;
  background: #fafafa;
  &.warn-high { border-left-color: #CF1322; background: #FFF1F0; }
  &.warn-medium { border-left-color: #FA8C16; background: #FFF7E6; }
  &.warn-low { border-left-color: var(--ip-bright, #1677FF); background: var(--ip-light-bg, #EBF1FF); }
  .warn-header {
    display: flex;
    align-items: center;
    gap: 6px;
    margin-bottom: 4px;
  }
  .warn-id { font-size: 12px; color: #8C8C8C; }
  .warn-time { font-size: 12px; color: #8C8C8C; margin-left: auto; }
  .warn-title { font-size: 13px; color: #262626; font-weight: 500; margin-bottom: 2px; }
  .warn-company { font-size: 12px; color: #8C8C8C; }
}

/* 导航区 */
.nav-section { margin-top: 4px; }
.section-title {
  font-size: 15px;
  font-weight: 700;
  color: #262626;
  margin-bottom: 14px;
  i { margin-right: 6px; color: var(--ip-secondary, #0050A0); }
}
.nav-card {
  display: flex;
  align-items: center;
  gap: 12px;
  background: #fff;
  border-radius: 8px;
  padding: 14px 16px;
  cursor: pointer;
  box-shadow: 0 1px 4px rgba(0,0,0,.07);
  transition: all .2s;
  &:hover {
    box-shadow: 0 4px 14px rgba(0,80,160,.15);
    transform: translateY(-2px);
  }
  .nav-icon {
    width: 40px;
    height: 40px;
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 20px;
    flex-shrink: 0;
  }
  .nav-body {
    flex: 1;
    min-width: 0;
    .nav-title { font-size: 14px; font-weight: 600; color: #262626; }
    .nav-desc { font-size: 12px; color: #8C8C8C; margin-top: 2px; }
  }
  .nav-arrow { color: #C0C4CC; font-size: 14px; }
}

::v-deep .el-card { border-radius: 6px; }
::v-deep .el-card__header { padding: 12px 16px; border-bottom: 1px solid #F0F2F5; }
</style>
