<template>
  <div class="property-home">
    <!-- Banner -->
    <div class="page-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-content">
        <div class="banner-left">
          <h2 class="banner-title">产权穿透监管</h2>
          <p class="banner-sub">全级次·全链条·全生命周期产权监管体系</p>
          <el-button type="primary" size="small" icon="el-icon-full-screen" @click="openScreen" style="margin-top:10px">大屏展示</el-button>
        </div>
        <!-- 穿透链路 -->
        <div class="penetration-chain">
          <div v-for="(node, idx) in chainNodes" :key="idx" class="chain-item" @click="$router.push(node.path)">
            <div class="chain-node">{{ node.name }}</div>
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

    <!-- 合规进度条 -->
    <el-row :gutter="16" class="compliance-row">
      <el-col :span="8" v-for="item in complianceList" :key="item.key">
        <el-card class="compliance-card" shadow="never">
          <div class="comp-label">{{ item.label }}</div>
          <el-progress :percentage="item.value" :color="item.color" :stroke-width="16" :format="(p) => p + '%'"></el-progress>
          <div class="comp-sub">{{ item.desc }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 模块导航卡 -->
    <el-row :gutter="16" class="module-row">
      <el-col :span="6" v-for="mod in modules" :key="mod.path">
        <el-card class="module-card" shadow="hover" @click.native="$router.push(mod.path)">
          <div class="mod-icon" :style="{ background: mod.bg }">
            <i :class="mod.icon" style="font-size:24px;color:#fff;"></i>
          </div>
          <div class="mod-name">{{ mod.name }}</div>
          <div class="mod-count">{{ mod.countLabel }}: <b :style="{ color: mod.countColor }">{{ mod.count }}</b></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16" style="margin-top:16px;">
      <!-- 异常企业榜 -->
      <el-col :span="14">
        <el-card class="table-card" shadow="never">
          <div slot="header" class="card-header">
            <span>近期产权异常企业</span>
            <el-tag size="mini" type="danger">{{ abnormalList.length }} 条</el-tag>
          </div>
          <el-table :data="abnormalList" size="small" :row-class-name="abnormalRowClass">
            <el-table-column label="企业名称" prop="companyName" min-width="160"></el-table-column>
            <el-table-column label="问题类型" prop="issueType" width="130">
              <template slot-scope="{row}">
                <el-tag :type="issueTagType(row.issueLevel)" size="mini">{{ issueTypeLabel(row.issueType) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="严重程度" prop="issueLevel" width="90">
              <template slot-scope="{row}">
                <span :style="{ color: issueLevelColor(row.issueLevel), fontWeight: '700' }">{{ row.issueLevelLabel }}</span>
              </template>
            </el-table-column>
            <el-table-column label="处置状态" prop="status" width="90">
              <template slot-scope="{row}">
                <el-tag :type="row.status === 'HANDLED' ? 'success' : row.status === 'PENDING' ? 'danger' : 'warning'" size="mini">{{ row.statusLabel }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="发现时间" prop="foundDate" width="110"></el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <!-- 预警统计 -->
      <el-col :span="10">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>产权预警分布</span>
          </div>
          <div ref="warningChart" style="height:200px;"></div>
          <div class="warning-list">
            <div v-for="w in recentWarnings" :key="w.id" class="warning-item">
              <el-tag :type="w.level === 'HIGH' ? 'danger' : w.level === 'MEDIUM' ? 'warning' : ''" size="mini">{{ w.levelLabel }}</el-tag>
              <span class="w-company">{{ w.companyName }}</span>
              <span class="w-desc">{{ w.desc }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <div v-if="screenVisible" ref="screenContainer" class="fullscreen-container">
      <PropertyScreen @close="closeScreen" />
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import { getPropertyDashboard, getPropertyWarningList } from '@/api/stateAssets/propertyRight'

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
  name: 'PropertyPenetrationHome',
  components: { PropertyScreen: () => import('../screen/index.vue') },
  data() {
    return {
      screenVisible: false,
      chainNodes: [
        { name: '产权登记', path: '/assetPenetration/Cqdjtz' },
        { name: '股权结构', path: '/assetPenetration/Gqjgfx' },
        { name: '产权变动', path: '/assetPenetration/Cqbddj' },
        { name: '交易合规', path: '/assetPenetration/propertyPenetrationtradeReview' },
        { name: '参股监控', path: '/assetPenetration/Gqjgfx' },
      ],
      kpiList: [],
      complianceList: [],
      modules: [
        { name: '产权登记台账', path: '/assetPenetration/Cqdjtz', icon: 'el-icon-document', bg: '#1677FF', countLabel: '登记企业', count: 0, countColor: '#1677FF' },
        { name: '股权穿透图', path: '/assetPenetration/Cqctt', icon: 'el-icon-share', bg: '#722ED1', countLabel: '最深层级', count: '-', countColor: '#722ED1' },
        { name: '产权变动登记', path: '/assetPenetration/Cqbddj', icon: 'el-icon-refresh', bg: '#FA8C16', countLabel: '本年变动', count: 0, countColor: '#FA8C16' },
        { name: '产权交易台账', path: '/assetPenetration/Cqjytz', icon: 'el-icon-money', bg: '#13C2C2', countLabel: '交易笔数', count: 0, countColor: '#13C2C2' },
        { name: '产权交易审查', path: '/assetPenetration/propertyPenetrationtradeReview', icon: 'el-icon-s-order', bg: '#CF1322', countLabel: '合规问题', count: 0, countColor: '#CF1322' },
        { name: '参股企业分析', path: '/assetPenetration/Cgqyfx', icon: 'el-icon-pie-chart', bg: '#237804', countLabel: '参股企业', count: 0, countColor: '#237804' },
        { name: '三表比对看板', path: '/assetPenetration/Sbdbkb', icon: 'el-icon-s-grid', bg: '#EB2F96', countLabel: '差异企业', count: 0, countColor: '#EB2F96' },
        { name: '监控驾驶舱', path: '/assetPenetration/Cqjkjsc', icon: 'el-icon-data-analysis', bg: '#0050A0', countLabel: '活跃预警', count: 0, countColor: '#F5222D' },
      ],
      abnormalList: [],
      recentWarnings: [],
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      try {
        const res = await getPropertyDashboard()
        const d = res.data || {}
        this.kpiList = this.buildKpiList(d)
        this.complianceList = d.complianceList || []
        if (d.complianceList && d.complianceList.length === 0) {
          this.complianceList = [
            { key: 'exchange', label: '进场交易率', value: d.exchangeTradeRate || 0, color: '#1677FF', desc: '应进场交易已进场比例' },
            { key: 'appraisal', label: '评估覆盖率', value: d.appraisalRate || 0, color: '#52C41A', desc: '产权交易资产评估完成比例' },
            { key: 'compliance', label: '整体合规率', value: d.complianceRate || 0, color: '#722ED1', desc: '产权管理综合合规评分' },
          ]
        }
        this.modules[0].count = d.registeredCount || 0
        this.modules[1].count = d.maxEquityLevel ? d.maxEquityLevel + '级' : '-'
        this.modules[2].count = d.yearlyChangeCount || 0
        this.modules[3].count = d.yearlyTransCount || 0
        this.modules[4].count = d.complianceIssueCount || 0
        this.modules[5].count = d.shareholdingCount || 0
        this.modules[6].count = d.inconsistentCount || 0
        this.modules[7].count = d.activeWarnings || 0
        this.abnormalList = d.abnormalList || []
      } catch {
        this.kpiList = this.buildKpiList({})
      }
      try {
        const warnRes = await getPropertyWarningList({ pageSize: 5 })
        const rawWarnings = (warnRes.data && (warnRes.data.tlist || warnRes.data.list)) || []
        this.recentWarnings = rawWarnings.map(w => ({
          id: w.warningId,
          level: w.warningLevel || w.level,
          levelLabel: (w.warningLevel || w.level) === 'HIGH' ? '高危' : (w.warningLevel || w.level) === 'MEDIUM' ? '中危' : '低危',
          companyName: w.companyName,
          desc: w.warningContent,
          status: w.status,
        }))
      } catch {
        this.recentWarnings = []
      }
      this.$nextTick(() => this.initWarningChart())
    },
    buildKpiList(d) {
      return [
        { key: 'registered', label: '已登记企业数', value: d.registeredCount || 0, unit: '家', color: '#1677FF' },
        { key: 'level', label: '最深股权层级', value: d.maxEquityLevel || 0, unit: '级', color: (d.maxEquityLevel || 0) > 5 ? '#F5222D' : '#52C41A' },
        { key: 'trans', label: '年度交易数', value: d.yearlyTransCount || 0, unit: '笔', color: '#FA8C16' },
        { key: 'exchange', label: '进场交易率', value: d.exchangeTradeRate || 0, unit: '%', color: (d.exchangeTradeRate || 0) < 80 ? '#F5222D' : '#52C41A' },
        { key: 'shareholding', label: '参股企业数', value: d.shareholdingCount || 0, unit: '家', color: '#722ED1' },
        { key: 'warnings', label: '活跃预警数', value: d.activeWarnings || 0, unit: '条', color: '#F5222D' },
      ]
    },
    initWarningChart() {
      if (!this.$refs.warningChart) return
      const chart = echarts.init(this.$refs.warningChart)
      const high = this.recentWarnings.filter(w => w.level === 'HIGH').length
      const medium = this.recentWarnings.filter(w => w.level === 'MEDIUM').length
      const low = this.recentWarnings.filter(w => w.level === 'LOW').length
      const chartData = high > 0 || medium > 0 || low > 0
        ? [
            { value: high, name: '高危', itemStyle: { color: '#F5222D' } },
            { value: medium, name: '中危', itemStyle: { color: '#FA8C16' } },
            { value: low, name: '低危', itemStyle: { color: '#FAAD14' } },
          ]
        : [
            { value: 0, name: '暂无预警', itemStyle: { color: '#D9D9D9' } },
          ]
      chart.setOption({
        tooltip: { trigger: 'item' },
        legend: { bottom: 0, textStyle: { fontSize: 11 } },
        series: [{
          type: 'pie', radius: ['40%', '65%'], center: ['50%', '45%'],
          data: chartData,
          label: { formatter: '{b}: {c}条' },
        }]
      })
    },
    abnormalRowClass({ row }) {
      if (row.issueLevel === 'HIGH') return 'row-danger'
      if (row.issueLevel === 'MEDIUM') return 'row-warning'
      return ''
    },
    issueTagType(level) {
      return level === 'HIGH' ? 'danger' : level === 'MEDIUM' ? 'warning' : ''
    },
    issueLevelColor(level) {
      return level === 'HIGH' ? '#F5222D' : level === 'MEDIUM' ? '#FA8C16' : '#FAAD14'
    },
    issueTypeLabel(type) {
      const map = {
        'LOSS': '连续亏损',
        'RATIO_CHANGE': '比例异动',
        'COMPLIANCE': '合规问题',
        'LEVEL_EXCEED': '层级超限',
        'TRANSFER': '产权转让',
        'ABNORMAL': '经营异常',
      }
      return map[type] || type || '其他'
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

<style lang="scss" scoped>
.fullscreen-container { position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; z-index: 9999; }

.property-home { padding: 16px; background: #F5F7FA; min-height: 100vh; }
.page-banner {
  border-radius: 8px; padding: 24px 32px; margin-bottom: 16px; color: #fff;
}
.banner-content { display: flex; align-items: center; justify-content: space-between; }
.banner-title { font-size: 24px; font-weight: 700; margin: 0 0 4px; }
.banner-sub { font-size: 13px; opacity: 0.85; margin: 0; }
.penetration-chain { display: flex; align-items: center; flex-wrap: nowrap; }
.chain-item { display: inline-flex; align-items: center; flex-shrink: 0; }
.chain-node { background: rgba(255,255,255,0.15); padding: 6px 14px; border-radius: 20px; font-size: 13px; white-space: nowrap; cursor: pointer; transition: background .2s; }
.chain-node:hover { background: rgba(255,255,255,0.35); }
.chain-arrow { margin: 0 6px; font-size: 16px; opacity: 0.7; color: #fff; }
.kpi-row { margin-bottom: 16px; }
.kpi-card { text-align: center; padding: 4px; }
.kpi-value { font-size: 26px; font-weight: 700; }
.kpi-label { font-size: 12px; color: #888; margin-top: 4px; }
.compliance-row { margin-bottom: 16px; }
.compliance-card { padding: 4px; }
.comp-label { font-size: 13px; color: #333; font-weight: 600; margin-bottom: 8px; }
.comp-sub { font-size: 11px; color: #aaa; margin-top: 4px; }
.module-row { margin-bottom: 16px; }
.module-card { text-align: center; cursor: pointer; padding: 8px; transition: transform .2s; }
.module-card:hover { transform: translateY(-3px); }
.mod-icon { width: 50px; height: 50px; border-radius: 50%; display: flex; align-items: center; justify-content: center; margin: 0 auto 8px; }
.mod-name { font-size: 13px; font-weight: 600; color: #333; }
.mod-count { font-size: 12px; color: #888; margin-top: 4px; }
.card-header { display: flex; align-items: center; justify-content: space-between; font-size: 14px; font-weight: 600; }
.warning-list { margin-top: 8px; }
.warning-item { display: flex; align-items: center; gap: 8px; padding: 6px 0; border-bottom: 1px solid #f0f0f0; font-size: 12px; }
.w-company { color: #333; font-weight: 600; min-width: 80px; }
.w-desc { color: #888; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
::v-deep .row-danger td { background: #FFF1F0 !important; }
::v-deep .row-warning td { background: #FFF7E6 !important; }
</style>
