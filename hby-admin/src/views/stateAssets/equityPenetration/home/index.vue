<template>
  <div class="fin-home-container" :style="themeVars">
    <!-- Banner -->
    <div class="fin-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-left">
        <div class="banner-icon"><i class="el-icon-connection"></i></div>
        <div class="banner-text">
          <div class="banner-title">金融穿透监管平台</div>
          <div class="banner-desc">股权一张图 · 风险一本账 · 金融穿透全覆盖</div>
        </div>
      </div>
      <div class="banner-right">
        <div class="banner-kpi" v-for="k in bannerKpi" :key="k.label">
          <div class="bk-value">{{ k.value }}<span class="bk-unit">{{ k.unit }}</span></div>
          <div class="bk-label">{{ k.label }}</div>
        </div>
      </div>
    </div>

    <!-- KPI 统计卡（6个） -->
    <el-row :gutter="16" class="mb-16">
      <el-col :span="4" v-for="kpi in kpiCards" :key="kpi.label">
        <el-card shadow="hover" class="kpi-card">
          <div class="kpi-inner">
            <div class="kpi-icon" :style="{ background: kpi.color + '18', color: kpi.color }">
              <i :class="kpi.icon"></i>
            </div>
            <div class="kpi-info">
              <div class="kpi-value" :style="{ color: kpi.color }">{{ kpi.value }}<span v-if="kpi.unit" class="kpi-unit">{{ kpi.unit }}</span></div>
              <div class="kpi-label">{{ kpi.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 中部主体：穿透树 + 功能导航 -->
    <el-row :gutter="16" class="mb-16">
      <!-- 左：ECharts 股权穿透树 -->
      <el-col :span="14">
        <el-card shadow="never" class="tree-card">
          <div slot="header" class="card-header">
            <span class="card-title"><i class="el-icon-share"></i> 股权穿透链路图</span>
            <div class="card-actions">
              <el-radio-group v-model="treeDepth" size="mini" @change="renderTree">
                <el-radio-button label="2">2层</el-radio-button>
                <el-radio-button label="3">3层</el-radio-button>
                <el-radio-button label="all">全量</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div class="tree-legend">
            <span class="legend-item"><i class="legend-dot" :style="{background: ipPrimary}"></i>集团根节点</span>
            <span class="legend-item"><i class="legend-dot" :style="{background: ipSecondary}"></i>一级子企业</span>
            <span class="legend-item"><i class="legend-dot" :style="{background: ipBright}"></i>二级子企业</span>
            <span class="legend-item"><i class="legend-dot" style="background:#FF4D4F"></i>高风险</span>
            <span class="legend-item"><i class="legend-dot" style="background:#FA8C16"></i>质押</span>
          </div>
          <div ref="treeChart" style="height:380px"></div>
        </el-card>
      </el-col>

      <!-- 右：功能模块导航卡 -->
      <el-col :span="10">
        <el-card shadow="never" style="height:100%">
          <div slot="header" class="card-header">
            <span class="card-title"><i class="el-icon-menu"></i> 功能模块导航</span>
          </div>
          <el-row :gutter="12">
            <el-col :span="12" v-for="mod in navModules" :key="mod.route" class="mb-12">
              <div class="nav-card" :style="{ borderLeftColor: mod.color }" @click="$router.push(mod.route)">
                <div class="nav-icon" :style="{ background: mod.color + '18', color: mod.color }">
                  <i :class="mod.icon"></i>
                </div>
                <div class="nav-info">
                  <div class="nav-title">{{ mod.title }}</div>
                  <div class="nav-desc">{{ mod.desc }}</div>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <!-- 底部：活跃预警区 -->
    <el-card shadow="never">
      <div slot="header" class="card-header">
        <span class="card-title"><i class="el-icon-bell"></i> 活跃预警清单</span>
        <el-tag type="danger" size="mini">{{ activeWarnings.length }} 条活跃</el-tag>
      </div>
      <el-row :gutter="12">
        <el-col :span="8" v-for="w in activeWarnings" :key="w.id">
          <div class="warning-card" :class="'warning-' + w.level.toLowerCase()">
            <div class="warning-head">
              <el-tag :type="w.level === 'HIGH' ? 'danger' : w.level === 'MEDIUM' ? 'warning' : 'info'" size="mini">
                {{ { HIGH: '高', MEDIUM: '中', LOW: '低' }[w.level] }}
              </el-tag>
              <span class="warning-code">{{ w.code }}</span>
              <span class="warning-time">{{ w.time.substring(5, 16) }}</span>
            </div>
            <div class="warning-name">{{ w.name }}</div>
            <div class="warning-company"><i class="el-icon-office-building"></i> {{ w.company }}</div>
            <div class="warning-meta">当前值：{{ w.value }} &nbsp;|&nbsp; 阈值：{{ w.threshold }}</div>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import { getBeneficialOwnerStatistics } from '@/api/stateAssets/beneficialOwner'
import { getFinancialRiskDashboard } from '@/api/stateAssets/financialRiskPenetration'
import { getEquityPenetrationTree } from '@/api/stateAssets/equityStructure'
import { investThemeMixin } from '../../themeMixin'

export default {
  computed: {
    ...mapGetters({ theme: 'settings/theme' }),
    themeColor() {
      const map = { red: '#e50113', green: '#41b584', ocean: '#1890ff', white: '#1890ff', default: '#1890ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#1890ff'
    },
    themeColorLight() {
      const map = { red: '#fff1f0', green: '#f6ffed', ocean: '#e6f7ff', white: '#e6f7ff', default: '#e6f7ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#e6f7ff'
    },
  },
  name: 'FinancialPenetrationHome',
  mixins: [investThemeMixin],
  data() {
    return {
      treeDepth: 'all',
      treeChart: null,
      bannerKpi: [
        { value: 0, unit: '家', label: '受监管企业数' },
        { value: '0', unit: '亿', label: '融资总额' },
        { value: '0', unit: '亿', label: '担保总额' },
        { value: 0, unit: '条', label: '活跃预警' }
      ],
      kpiCards: [],
      navModules: [],
      activeWarnings: [],
      treeData: null
    }
  },
  created() {
    this.kpiCards = [
      { label: '受监管企业数', value: 0, unit: '家', icon: 'el-icon-office-building', color: this.ipSecondary },
      { label: '融资总额', value: '0', unit: '亿元', icon: 'el-icon-bank-card', color: this.ipBright },
      { label: '对外担保总额', value: '0', unit: '亿元', icon: 'el-icon-s-check', color: '#FA8C16' },
      { label: '高风险企业', value: 0, unit: '家', icon: 'el-icon-warning', color: '#CF1322' },
      { label: '股权质押企业', value: 0, unit: '家', icon: 'el-icon-lock', color: '#FAAD14' },
      { label: '活跃预警数', value: 0, unit: '条', icon: 'el-icon-bell', color: '#FF7A45' }
    ]
    this.navModules = [
      { title: '股东穿透分析', desc: '穿透识别隐藏股东', icon: 'el-icon-user', color: this.ipSecondary, route: '/equityPenetration/shareholderAnalysis' },
      { title: '股权结构分析', desc: '股权比例与集中度', icon: 'el-icon-share', color: this.ipBright, route: '/equityPenetration/equityStructure' },
      { title: '控制链分析', desc: '控制链路穿透追踪', icon: 'el-icon-connection', color: this.ipPrimary, route: '/equityPenetration/controlChainAnalysis' },
      { title: '股权变动监控', desc: '质押转让变动预警', icon: 'el-icon-refresh', color: '#FA8C16', route: '/equityPenetration/equityChanges' },
      { title: '实际控制人', desc: '识别最终控制主体', icon: 'el-icon-user-solid', color: '#722ED1', route: '/equityPenetration/beneficialOwner' },
      { title: '融资记录台账', desc: '融资规模与成本', icon: 'el-icon-coin', color: '#13C2C2', route: '/equityPenetration/Rzjltz' },
      { title: '担保记录台账', desc: '担保链路风险识别', icon: 'el-icon-document-checked', color: '#EB2F96', route: '/equityPenetration/Dbjltz' },
      { title: '风险驾驶舱', desc: '金融风险综合大屏', icon: 'el-icon-odometer', color: '#CF1322', route: '/equityPenetration/Jrfxjsc' }
    ]
  },
  mounted() {
    this.fetchDashboardData()
    this.$nextTick(() => this.renderTree())
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    if (this.treeChart) this.treeChart.dispose()
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    // 获取首页仪表盘数据
    async fetchDashboardData() {
      try {
        const [statsRes, dashboardRes, treeRes] = await Promise.all([
          getBeneficialOwnerStatistics(),
          getFinancialRiskDashboard(),
          getEquityPenetrationTree({ depth: 3 })
        ])

        // 处理统计数据
        if (statsRes.result === 200 && statsRes.data) {
          const stats = statsRes.data
          this.bannerKpi = [
            { value: stats.totalEnterprises || 0, unit: '家', label: '受监管企业数' },
            { value: stats.totalFinancing || '0', unit: '亿', label: '融资总额' },
            { value: stats.totalGuarantee || '0', unit: '亿', label: '担保总额' },
            { value: stats.activeWarnings || 0, unit: '条', label: '活跃预警' }
          ]
          this.kpiCards = [
            { label: '受监管企业数', value: stats.totalEnterprises || 0, unit: '家', icon: 'el-icon-office-building', color: this.ipSecondary },
            { label: '融资总额', value: stats.totalFinancing || '0', unit: '亿元', icon: 'el-icon-bank-card', color: this.ipBright },
            { label: '对外担保总额', value: stats.totalGuarantee || '0', unit: '亿元', icon: 'el-icon-s-check', color: '#FA8C16' },
            { label: '高风险企业', value: stats.highRiskEnterprises || 0, unit: '家', icon: 'el-icon-warning', color: '#CF1322' },
            { label: '股权质押企业', value: stats.pledgedEnterprises || 0, unit: '家', icon: 'el-icon-lock', color: '#FAAD14' },
            { label: '活跃预警数', value: stats.activeWarnings || 0, unit: '条', icon: 'el-icon-bell', color: '#FF7A45' }
          ]
        }

        // 处理仪表盘数据（预警）
        if (dashboardRes.result === 200 && dashboardRes.data) {
          const dashboard = dashboardRes.data
          this.activeWarnings = dashboard.activeWarnings || []
        }

        // 处理股权穿透树数据
        if (treeRes.result === 200 && treeRes.data) {
          this.treeData = treeRes.data
        }
      } catch (error) {
        console.error('获取首页数据失败:', error)
      }
      this.$nextTick(() => this.renderTree())
    },

    handleResize() {
      if (this.treeChart) this.treeChart.resize()
    },

    renderTree() {
      if (this.treeChart) this.treeChart.dispose()
      if (!this.$refs.treeChart) return
      this.treeChart = echarts.init(this.$refs.treeChart)

      // 无数据时显示空状态
      if (!this.treeData || !this.treeData.name) {
        this.treeChart.setOption({
          graphic: [{
            type: 'text',
            left: 'center',
            top: 'middle',
            style: { text: '暂无股权穿透数据', fontSize: 14, fill: '#909399' }
          }]
        })
        return
      }

      const nodeColorMap = { 0: this.ipPrimary, 1: this.ipSecondary, 2: this.ipBright, 3: '#40A9FF' }
      const riskColorMap = { high: '#FF4D4F', pledge: '#FA8C16', joint: '#52C41A' }

      const allTree = this.treeData

      // 按层级截取树
      const trimTree = (node, maxDepth, currentDepth) => {
        if (currentDepth >= maxDepth) return { ...node, children: [] }
        return {
          ...node,
          children: (node.children || []).map(c => trimTree(c, maxDepth, currentDepth + 1))
        }
      }

      let treeData = allTree
      if (this.treeDepth === '2') {
        treeData = trimTree(allTree, 2, 0)
      } else if (this.treeDepth === '3') {
        treeData = trimTree(allTree, 3, 0)
      }

      const processNode = (node) => {
        let color = nodeColorMap[node.level] || '#40A9FF'
        if (node.risk === 'high') color = riskColorMap.high
        if (node.risk === 'pledge') color = riskColorMap.pledge
        if (node.risk === 'joint') color = riskColorMap.joint
        return {
          ...node,
          symbol: 'circle',
          symbolSize: node.level === 0 ? 28 : node.level === 1 ? 22 : node.level === 2 ? 16 : 12,
          itemStyle: { color },
          label: { position: 'right', fontSize: 11, color: '#303133' },
          children: (node.children || []).map(c => processNode(c))
        }
      }

      this.treeChart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: (params) => params.name.replace(/\n/g, '<br/>')
        },
        series: [{
          type: 'tree',
          data: [processNode(treeData)],
          top: '5%', left: '10%', bottom: '5%', right: '20%',
          symbolSize: 20,
          orient: 'LR',
          expandAndCollapse: false,
          label: { position: 'right', fontSize: 11, lineHeight: 16 },
          leaves: { label: { position: 'right' } },
          emphasis: { focus: 'descendant' },
          animationDurationUpdate: 500
        }]
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.fin-home-container { padding: 16px; background: #F0F2F5; min-height: calc(100vh - 84px); }
.mb-16 { margin-bottom: 16px; }
.mb-12 { margin-bottom: 12px; }

// Banner
.fin-banner {
  border-radius: 8px; padding: 20px 28px; margin-bottom: 16px;
  display: flex; align-items: center; justify-content: space-between;
  .banner-left { display: flex; align-items: center; }
  .banner-icon {
    width: 52px; height: 52px; background: rgba(255,255,255,0.18); border-radius: 12px;
    display: flex; align-items: center; justify-content: center; margin-right: 16px;
    i { font-size: 28px; color: #fff; }
  }
  .banner-title { font-size: 20px; font-weight: 700; color: #fff; margin-bottom: 4px; }
  .banner-desc { font-size: 13px; color: rgba(255,255,255,0.8); }
  .banner-right { display: flex; gap: 32px; }
  .banner-kpi { text-align: center;
    .bk-value { font-size: 22px; font-weight: 700; color: var(--ip-accent, #FAAD14); line-height: 1;
      .bk-unit { font-size: 13px; font-weight: 400; color: rgba(255,255,255,0.7); margin-left: 2px; }
    }
    .bk-label { font-size: 12px; color: rgba(255,255,255,0.75); margin-top: 4px; }
  }
}

// KPI卡
.kpi-card { border-radius: 6px; }
.kpi-inner { display: flex; align-items: center; }
.kpi-icon {
  width: 44px; height: 44px; border-radius: 10px; display: flex; align-items: center;
  justify-content: center; margin-right: 12px; flex-shrink: 0;
  i { font-size: 22px; }
}
.kpi-value { font-size: 22px; font-weight: 700; color: #303133; line-height: 1;
  .kpi-unit { font-size: 12px; font-weight: 400; color: #909399; margin-left: 2px; }
}
.kpi-label { font-size: 12px; color: #909399; margin-top: 4px; }

// 穿透树卡
.tree-card { }
.card-header { display: flex; align-items: center; justify-content: space-between; }
.card-title { font-size: 14px; font-weight: 600; color: #303133;
  i { margin-right: 6px; color: var(--ip-secondary, #0050A0); }
}
.tree-legend { display: flex; gap: 16px; margin-bottom: 8px; flex-wrap: wrap; }
.legend-item { font-size: 12px; color: #606266; display: flex; align-items: center; gap: 4px; }
.legend-dot { display: inline-block; width: 10px; height: 10px; border-radius: 50%; }

// 导航卡
.nav-card {
  display: flex; align-items: center; padding: 10px 12px; background: #fff;
  border: 1px solid #E8EDF5; border-left: 3px solid; border-radius: 6px;
  cursor: pointer; transition: all 0.25s; height: 64px;
  &:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(0,0,0,0.08); }
}
.nav-icon {
  width: 36px; height: 36px; border-radius: 8px; display: flex;
  align-items: center; justify-content: center; margin-right: 10px; flex-shrink: 0;
  i { font-size: 18px; }
}
.nav-title { font-size: 13px; font-weight: 600; color: #303133; }
.nav-desc { font-size: 12px; color: #909399; margin-top: 2px; }

// 预警卡
.warning-card {
  padding: 12px 14px; border-radius: 6px; border: 1px solid; margin-bottom: 0;
  &.warning-high { background: #FFF1F0; border-color: #FFCCC7; }
  &.warning-medium { background: #FFF7E6; border-color: #FFD591; }
  &.warning-low { background: #F6FFED; border-color: #B7EB8F; }
}
.warning-head { display: flex; align-items: center; gap: 6px; margin-bottom: 6px; }
.warning-code { font-size: 11px; color: #909399; }
.warning-time { font-size: 11px; color: #C0C4CC; margin-left: auto; }
.warning-name { font-size: 13px; font-weight: 600; color: #303133; margin-bottom: 4px; }
.warning-company { font-size: 12px; color: #606266; margin-bottom: 3px;
  i { margin-right: 3px; }
}
.warning-meta { font-size: 11px; color: #909399; }

::v-deep .el-card { border-radius: 6px; }
::v-deep .el-card__header { padding: 12px 16px; }
</style>
