<template>
  <div class="fund-home">
    <!-- Banner -->
    <div class="page-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-content">
        <div class="banner-left">
          <h2 class="banner-title">资金穿透监管</h2>
          <p class="banner-sub">画像洞察·数据融合·模型驱动·预警闭环全链路资金监管体系</p>
          <el-button type="primary" size="small" icon="el-icon-full-screen" @click="openScreen" style="margin-top:10px">大屏展示</el-button>
        </div>
        <!-- 穿透链路 -->
        <div class="penetration-chain">
          <div v-for="(node, idx) in chainNodes" :key="idx" class="chain-item">
            <div class="chain-node">{{ node }}</div>
            <i v-if="idx < chainNodes.length - 1" class="el-icon-arrow-right chain-arrow"></i>
          </div>
        </div>
      </div>
    </div>

    <!-- KPI统计行 -->
    <el-row :gutter="16" class="kpi-row">
      <el-col :span="4" v-for="kpi in kpiList" :key="kpi.key">
        <el-card class="kpi-card" shadow="hover">
          <div class="kpi-value" :style="{ color: kpi.color }">{{ kpi.value }}{{ kpi.unit }}</div>
          <div class="kpi-label">{{ kpi.label }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 流程说明条 -->
    <el-row :gutter="16" class="flow-row">
      <el-col :span="24">
        <el-card class="flow-card" shadow="never">
          <div class="flow-content">
            <div v-for="(step, idx) in flowSteps" :key="idx" class="flow-step">
              <div class="flow-step-icon" :style="{ background: step.color }">
                <i :class="step.icon"></i>
              </div>
              <div class="flow-step-info">
                <div class="flow-step-title">{{ step.title }}</div>
                <div class="flow-step-desc">{{ step.desc }}</div>
              </div>
              <i v-if="idx < flowSteps.length - 1" class="el-icon-arrow-right flow-arrow"></i>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 模块导航卡（两行，第一行5个，第二行5个） -->
    <el-row :gutter="16" class="module-row">
      <el-col :span="5" v-for="mod in modulesRow1" :key="mod.path">
        <el-card class="module-card" shadow="hover" @click.native="handleNav(mod)">
          <div class="mod-icon" :style="{ background: mod.bg }">
            <i :class="mod.icon" style="font-size:22px;color:#fff;"></i>
          </div>
          <div class="mod-name">{{ mod.name }}</div>
          <div class="mod-tag">{{ mod.tag }}</div>
          <div class="mod-count">{{ mod.countLabel }}: <b :style="{ color: mod.countColor }">{{ mod.count }}</b></div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="16" class="module-row">
      <el-col :span="5" v-for="mod in modulesRow2" :key="mod.path">
        <el-card class="module-card" shadow="hover" @click.native="handleNav(mod)">
          <div class="mod-icon" :style="{ background: mod.bg }">
            <i :class="mod.icon" style="font-size:22px;color:#fff;"></i>
          </div>
          <div class="mod-name">{{ mod.name }}</div>
          <div class="mod-tag">{{ mod.tag }}</div>
          <div class="mod-count">{{ mod.countLabel }}: <b :style="{ color: mod.countColor }">{{ mod.count }}</b></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top:16px;">
      <!-- 近期风险预警列表 -->
      <el-col :span="14">
        <el-card class="table-card" shadow="never">
          <div slot="header" class="card-header">
            <span>近期资金风险预警</span>
            <el-tag size="mini" type="danger">{{ warningList.length }} 条</el-tag>
          </div>
          <el-table :data="warningList" size="small" :row-class-name="warningRowClass">
            <el-table-column label="企业名称" prop="companyName" min-width="140"></el-table-column>
            <el-table-column label="预警类型" prop="warnType" width="130">
              <template slot-scope="{row}">
                <el-tag :type="levelTagType(row.level)" size="mini">{{ row.warnType }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="风险等级" prop="level" width="90">
              <template slot-scope="{row}">
                <span :style="{ color: levelColor(row.level), fontWeight: '700' }">{{ row.levelLabel }}</span>
              </template>
            </el-table-column>
            <el-table-column label="数据来源" prop="source" width="90">
              <template slot-scope="{row}">
                <el-tag type="info" size="mini">{{ row.source }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="预警时间" prop="warnDate" width="110"></el-table-column>
            <el-table-column label="状态" prop="status" width="80">
              <template slot-scope="{row}">
                <el-tag :type="row.status === 'HANDLED' ? 'success' : row.status === 'PENDING' ? 'danger' : 'warning'" size="mini">{{ row.statusLabel }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <!-- 预警分布图 + 问题汇总 -->
      <el-col :span="10">
        <el-card class="chart-card" shadow="never" style="margin-bottom:16px;">
          <div slot="header" class="card-header">
            <span>资金风险预警分布</span>
          </div>
          <div ref="warningChart" style="height:180px;"></div>
        </el-card>
        <el-card class="issue-card" shadow="never">
          <div slot="header" class="card-header">
            <span>问题汇总概览</span>
            <el-tag size="mini" type="warning">{{ issueStats.total }} 项</el-tag>
          </div>
          <div class="issue-stats">
            <div class="issue-stat-item" v-for="stat in issueStatList" :key="stat.key">
              <div class="issue-stat-bar" :style="{ width: (stat.count / issueStats.total * 100) + '%', background: stat.color }"></div>
              <span class="issue-stat-label">{{ stat.label }}</span>
              <b class="issue-stat-count" :style="{ color: stat.color }">{{ stat.count }}</b>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <div v-if="screenVisible" ref="screenContainer" class="fullscreen-container">
      <FundScreen @close="closeScreen" />
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
const MOCK_KPI = {
  monitorCount: 156,
  modelCount: 34,
  dataSourceCount: 12,
  activeWarnings: 19,
  issueCount: 67,
  dataFusionRate: 88.5
}

const MOCK_WARNINGS = [
  { companyName: '示例能源科技有限公司', warnType: '资金异常流出', level: 'HIGH', levelLabel: '高危', source: '司库数据', warnDate: '2026-04-28', status: 'PENDING', statusLabel: '待处置' },
  { companyName: '云鑫数字科技股份公司', warnType: '关联交易超限', level: 'HIGH', levelLabel: '高危', source: '财务数据', warnDate: '2026-04-26', status: 'HANDLING', statusLabel: '处置中' },
  { companyName: '示例港口运营有限公司', warnType: '融资结构失衡', level: 'MEDIUM', levelLabel: '中危', source: '合同数据', warnDate: '2026-04-25', status: 'PENDING', statusLabel: '待处置' },
  { companyName: '国华冷链物流股份公司', warnType: '流动性风险', level: 'MEDIUM', levelLabel: '中危', source: '司法数据', warnDate: '2026-04-22', status: 'HANDLING', statusLabel: '处置中' },
  { companyName: '示例西部矿业发展有限公司', warnType: '资金占用异常', level: 'LOW', levelLabel: '低危', source: '人员数据', warnDate: '2026-04-20', status: 'HANDLED', statusLabel: '已处置' },
  { companyName: '某城市燃气集团', warnType: '票据合规问题', level: 'MEDIUM', levelLabel: '中危', source: '发票数据', warnDate: '2026-04-18', status: 'PENDING', statusLabel: '待处置' },
]

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
  name: 'FundPenetrationHome',
  components: { FundScreen: () => import('../screen/index.vue') },
  data() {
    return {
      screenVisible: false,
      chainNodes: ['资金画像', '数据融合', '内部数据', '外部数据', '模型构建', '风险预警', '问题处置'],
      kpiList: [],
      flowSteps: [
        { title: '资金穿透画像', desc: '监管企业总体资金情况', icon: 'el-icon-user', color: '#1677FF' },
        { title: '数据穿透', desc: '多源数据融合展示', icon: 'el-icon-connection', color: '#722ED1' },
        { title: '5类内部数据', desc: '司库/财务/合同/发票/人员', icon: 'el-icon-files', color: '#0950A8' },
        { title: '司法数据穿透', desc: '外部司法数据查询', icon: 'el-icon-reading', color: '#CF1322' },
        { title: '数据模型管理', desc: '搭建监管模型', icon: 'el-icon-s-data', color: '#237804' },
        { title: '评估模型', desc: '启动监管模型', icon: 'el-icon-set-up', color: '#FA8C16' },
        { title: '风险预警', desc: '预警数据双视图', icon: 'el-icon-warning', color: '#F5222D' },
        { title: '问题汇总', desc: '汇总处置数据', icon: 'el-icon-document-copy', color: '#08979C' },
        { title: '数据源管理', desc: '模型数据源配置', icon: 'el-icon-setting', color: '#531DAB' },
      ],
      modulesRow1: [
        { name: '资金穿透画像', tag: '总体监管', path: '/risk/home/enterpriseProfile', icon: 'el-icon-user', bg: '#1677FF', countLabel: '监管企业', count: 156, countColor: '#1677FF' },
        { name: '数据穿透', tag: '数据融合', path: '/home/screen/dataPortrait', icon: 'el-icon-connection', bg: '#722ED1', countLabel: '融合数据源', count: 12, countColor: '#722ED1' },
        { name: '司库数据', tag: '内部数据', path: '/finance/tableQuery', icon: 'el-icon-coin', bg: '#0950A8', countLabel: '资金记录', count: '8.2万', countColor: '#0950A8' },
        { name: '财务数据', tag: '内部数据', path: '/finance/tableQuery', icon: 'el-icon-s-finance', bg: '#237804', countLabel: '财务记录', count: '4.6万', countColor: '#237804' },
        { name: '合同数据', tag: '内部数据', path: '/finance/tableQuery', icon: 'el-icon-document', bg: '#FA8C16', countLabel: '合同记录', count: '2.3万', countColor: '#FA8C16' },
      ],
      modulesRow2: [
        { name: '发票数据', tag: '内部数据', path: '/finance/tableQuery', icon: 'el-icon-tickets', bg: '#08979C', countLabel: '发票记录', count: '15.8万', countColor: '#08979C' },
        { name: '人员数据', tag: '内部数据', path: '/finance/tableQuery', icon: 'el-icon-user-solid', bg: '#531DAB', countLabel: '人员记录', count: '3.2万', countColor: '#531DAB' },
        { name: '司法数据穿透', tag: '外部数据', path: '/workbench/industryData/telescope', icon: 'el-icon-reading', bg: '#CF1322', countLabel: '司法案件', count: 248, countColor: '#CF1322' },
        { name: '数据模型管理', tag: '模型构建', path: '/risk/mxgl/sjmxgl', icon: 'el-icon-s-data', bg: '#006D75', countLabel: '数据模型', count: 34, countColor: '#006D75' },
        { name: '评估模型', tag: '模型启动', path: '/risk/mxgl/pgmxgl', icon: 'el-icon-set-up', bg: '#AD4E00', countLabel: '评估模型', count: 18, countColor: '#AD4E00' },
      ],
      warningList: MOCK_WARNINGS,
      issueStats: { total: 67 },
      issueStatList: [
        { key: 'unhandled', label: '待处置', count: 23, color: '#F5222D' },
        { key: 'handling', label: '处置中', count: 31, color: '#FA8C16' },
        { key: 'handled', label: '已处置', count: 13, color: '#52C41A' },
      ],
    }
  },
  mounted() {
    this.initKPI()
    this.$nextTick(() => this.initWarningChart())
  },
  methods: {
    initKPI() {
      const d = MOCK_KPI
      this.kpiList = [
        { key: 'monitorCount', label: '监管企业数', value: d.monitorCount, unit: '家', color: '#1677FF' },
        { key: 'modelCount', label: '数据模型数', value: d.modelCount, unit: '个', color: '#722ED1' },
        { key: 'dataSourceCount', label: '数据源数量', value: d.dataSourceCount, unit: '个', color: '#237804' },
        { key: 'activeWarnings', label: '活跃预警数', value: d.activeWarnings, unit: '条', color: '#F5222D' },
        { key: 'issueCount', label: '问题汇总数', value: d.issueCount, unit: '项', color: '#FA8C16' },
        { key: 'dataFusionRate', label: '数据融合率', value: d.dataFusionRate, unit: '%', color: d.dataFusionRate >= 85 ? '#52C41A' : '#F5222D' },
      ]
    },
    initWarningChart() {
      const echarts = window.echarts || this.$echarts
      if (!echarts || !this.$refs.warningChart) return
      const chart = echarts.init(this.$refs.warningChart)
      chart.setOption({
        tooltip: { trigger: 'item' },
        legend: { bottom: 0, textStyle: { fontSize: 11 } },
        series: [{
          type: 'pie',
          radius: ['38%', '62%'],
          center: ['50%', '43%'],
          data: [
            { value: 7, name: '高危', itemStyle: { color: '#F5222D' } },
            { value: 8, name: '中危', itemStyle: { color: '#FA8C16' } },
            { value: 4, name: '低危', itemStyle: { color: '#FAAD14' } },
          ],
          label: { formatter: '{b}: {c}条' },
        }]
      })
    },
    handleNav(mod) {
      if (mod.path) {
        this.$router.push(mod.path).catch(() => {})
      }
    },
    warningRowClass({ row }) {
      if (row.level === 'HIGH') return 'row-danger'
      if (row.level === 'MEDIUM') return 'row-warning'
      return ''
    },
    levelTagType(level) {
      return level === 'HIGH' ? 'danger' : level === 'MEDIUM' ? 'warning' : ''
    },
    levelColor(level) {
      return level === 'HIGH' ? '#F5222D' : level === 'MEDIUM' ? '#FA8C16' : '#FAAD14'
    },
    openScreen() {
      this.screenVisible = true
      this.$nextTick(() => {
        const el = this.$refs.screenContainer
        if (el && el.requestFullscreen) el.requestFullscreen()
        else if (el && el.webkitRequestFullscreen) el.webkitRequestFullscreen()
      })
    },
    closeScreen() {
      this.screenVisible = false
      if (document.exitFullscreen) document.exitFullscreen()
      else if (document.webkitExitFullscreen) document.webkitExitFullscreen()
    },
  }
}
</script>

<style scoped>
.fullscreen-container { position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; z-index: 9999; }
.fund-home { padding: 16px; background: #F5F7FA; min-height: 100vh; }

/* Banner */
.page-banner {
  border-radius: 8px; padding: 24px 32px; margin-bottom: 16px; color: #fff;
}
.banner-content { display: flex; align-items: center; justify-content: space-between; }
.banner-title { font-size: 24px; font-weight: 700; margin: 0 0 4px; }
.banner-sub { font-size: 13px; opacity: 0.85; margin: 0; }
.penetration-chain { display: flex; align-items: center; flex-wrap: wrap; gap: 4px; }
.chain-item { display: flex; align-items: center; }
.chain-node { background: rgba(255,255,255,0.15); padding: 5px 12px; border-radius: 20px; font-size: 12px; white-space: nowrap; }
.chain-arrow { margin: 0 4px; font-size: 14px; opacity: 0.7; }

/* KPI */
.kpi-row { margin-bottom: 16px; }
.kpi-card { text-align: center; padding: 4px; }
.kpi-value { font-size: 26px; font-weight: 700; }
.kpi-label { font-size: 12px; color: #888; margin-top: 4px; }

/* 流程说明条 */
.flow-row { margin-bottom: 16px; }
.flow-card { padding: 0; }
.flow-content { display: flex; align-items: center; padding: 12px 16px; overflow-x: auto; }
.flow-step { display: flex; align-items: center; flex-shrink: 0; }
.flow-step-icon {
  width: 36px; height: 36px; border-radius: 50%;
  display: flex; align-items: center; justify-content: center; margin-right: 8px; flex-shrink: 0;
}
.flow-step-icon i { font-size: 16px; color: #fff; }
.flow-step-info { margin-right: 4px; }
.flow-step-title { font-size: 12px; font-weight: 600; color: #333; white-space: nowrap; }
.flow-step-desc { font-size: 11px; color: #aaa; white-space: nowrap; }
.flow-arrow { margin: 0 10px; font-size: 16px; color: #bbb; flex-shrink: 0; }

/* 模块导航卡 */
.module-row { margin-bottom: 12px; }
.module-card { text-align: center; cursor: pointer; padding: 8px; transition: transform .2s; }
.module-card:hover { transform: translateY(-3px); }
.mod-icon { width: 46px; height: 46px; border-radius: 50%; display: flex; align-items: center; justify-content: center; margin: 0 auto 8px; }
.mod-name { font-size: 13px; font-weight: 600; color: #333; }
.mod-tag { font-size: 11px; color: #1677FF; background: #E6F4FF; border-radius: 3px; padding: 1px 6px; display: inline-block; margin: 3px 0; }
.mod-count { font-size: 12px; color: #888; margin-top: 2px; }

/* 表格卡片 */
.card-header { display: flex; align-items: center; justify-content: space-between; font-size: 14px; font-weight: 600; }
.table-card { }
.chart-card { }

/* 问题汇总 */
.issue-stats { padding: 4px 0; }
.issue-stat-item { display: flex; align-items: center; margin-bottom: 12px; }
.issue-stat-bar { height: 6px; border-radius: 3px; min-width: 4px; max-width: 60%; transition: width .5s; margin-right: 10px; }
.issue-stat-label { font-size: 12px; color: #555; min-width: 48px; }
.issue-stat-count { font-size: 14px; font-weight: 700; margin-left: auto; }

::v-deep .row-danger td { background: #FFF1F0 !important; }
::v-deep .row-warning td { background: #FFF7E6 !important; }
</style>
