<template>
  <div class="drill-down-page" :style="themeVars" v-loading="loading" element-loading-text="数据加载中...">
    <!-- Banner -->
    <div class="page-banner">
      <div class="banner-content">
        <div class="banner-title"><i class="el-icon-zoom-in" style="margin-right:8px;"/>行业穿透分析</div>
        <div class="banner-sub">集团 → 行业群 → 子行业 → 企业，四层穿透，精准定位风险</div>
      </div>
      <div class="banner-right">
        <div class="depth-badge">当前层级：<strong>{{ currentDepthLabel }}</strong></div>
      </div>
    </div>

    <!-- 面包屑导航 -->
    <el-card shadow="never" class="breadcrumb-card">
      <div class="breadcrumb-row">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item
            v-for="(b, i) in breadcrumbs" :key="i"
            :class="{ 'active-crumb': i === breadcrumbs.length - 1 }"
            @click.native="jumpToBreadcrumb(i)">
            <span class="crumb-text" :style="i < breadcrumbs.length - 1 ? {cursor:'pointer', color: ipBright} : {color:'#303133', fontWeight:'600'}">
              <i :class="b.icon" style="margin-right:4px;"/>{{ b.label }}
            </span>
          </el-breadcrumb-item>
        </el-breadcrumb>
        <el-button v-if="breadcrumbs.length > 1" size="small" type="text" icon="el-icon-back" @click="goBack" style="margin-left:16px;">返回上级</el-button>
      </div>
      <!-- 层级步骤指示器 -->
      <el-steps :active="currentDepth" finish-status="success" size="small" style="margin-top:12px;">
        <el-step title="集团层" description="国有资本总体视图" icon="el-icon-office-building"/>
        <el-step title="行业群层" description="五大行业群分析" icon="el-icon-s-grid"/>
        <el-step title="子行业层" description="细分子行业穿透" icon="el-icon-connection"/>
        <el-step title="企业层" description="单体企业详情" icon="el-icon-s-custom"/>
      </el-steps>
    </el-card>

    <!-- 第0层：集团层 -->
    <template v-if="currentDepth === 0">
      <el-row :gutter="16" style="margin-bottom:16px;">
        <el-col :span="6" v-for="c in groupStats" :key="c.label">
          <el-card class="stat-card" shadow="never" :style="{'border-left': '4px solid ' + c.color}">
            <div class="stat-inner">
              <div>
                <div class="stat-num" :style="{color: c.color}">{{ c.value }}</div>
                <div class="stat-lbl">{{ c.label }}</div>
              </div>
              <i :class="c.icon" :style="{fontSize:'26px', color: c.color, opacity: 0.3}"/>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="16">
        <el-col :span="8" v-for="g in industryGroups" :key="g.name">
          <el-card class="group-card" shadow="hover" @click.native="drillToGroup(g)">
            <div class="group-header" :style="{background: g.gradient}">
              <i :class="g.icon" style="font-size:28px; color:#fff;"/>
              <div class="group-name">{{ g.name }}</div>
              <div class="group-drill-hint"><i class="el-icon-zoom-in"/> 点击穿透</div>
            </div>
            <div class="group-body">
              <div class="group-metric">
                <span class="gm-val">{{ g.companies }}</span><span class="gm-lbl">纳管企业</span>
              </div>
              <div class="group-metric">
                <span class="gm-val">{{ g.revenue }}</span><span class="gm-lbl">营收(亿)</span>
              </div>
              <div class="group-metric">
                <span class="gm-val">{{ g.riskCount }}</span><span class="gm-lbl">风险预警</span>
              </div>
            </div>
            <div class="group-footer">
              <el-tag :type="g.riskLevel === 'HIGH' ? 'danger' : g.riskLevel === 'MEDIUM' ? 'warning' : 'success'" size="mini">
                {{ g.riskLevel === 'HIGH' ? '高风险' : g.riskLevel === 'MEDIUM' ? '中风险' : '低风险' }}
              </el-tag>
              <span style="font-size:12px; color:#8c8c8c; margin-left:8px;">{{ g.subCount }}个子行业</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </template>

    <!-- 第1层：行业群层 -->
    <template v-if="currentDepth === 1 && currentGroup">
      <el-row :gutter="16" style="margin-bottom:16px;">
        <el-col :span="6" v-for="c in groupDetail.stats" :key="c.label">
          <el-card class="stat-card" shadow="never" :style="{'border-left': '4px solid ' + ipSecondary}">
            <div class="stat-inner">
              <div><div class="stat-num" :style="{color: ipSecondary}">{{ c.value }}</div><div class="stat-lbl">{{ c.label }}</div></div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-card shadow="never" style="margin-bottom:16px;">
        <div slot="header" style="font-weight:600;">{{ currentGroup.name }} — 子行业列表</div>
        <el-table :data="groupDetail.subIndustries" border @row-click="drillToSubIndustry">
          <el-table-column prop="name" label="子行业名称" min-width="140"/>
          <el-table-column prop="companyCount" label="企业数" width="90" align="center"/>
          <el-table-column prop="revenue" label="营收(亿)" width="100" align="center"/>
          <el-table-column prop="profit" label="利润(亿)" width="100" align="center">
            <template slot-scope="{row}"><span :style="{color: row.profit < 0 ? '#F5222D' : '#52C41A'}">{{ row.profit }}</span></template>
          </el-table-column>
          <el-table-column prop="profitRate" label="利润率" width="90" align="center">
            <template slot-scope="{row}"><span :style="{color: row.profitRate < 0 ? '#F5222D' : '#303133'}">{{ row.profitRate }}%</span></template>
          </el-table-column>
          <el-table-column prop="riskScore" label="风险评分" width="100" align="center">
            <template slot-scope="{row}">
              <el-progress :percentage="Math.min(row.riskScore, 100)" :color="row.riskScore >= 70 ? '#F5222D' : row.riskScore >= 50 ? '#FA8C16' : '#52C41A'" :stroke-width="8" :show-text="false" style="width:60px;display:inline-block;"/>
              <span style="margin-left:6px;">{{ row.riskScore }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100" align="center">
            <template slot-scope="{row}">
              <el-button size="mini" type="primary" plain @click.stop="drillToSubIndustry(row)">穿透<i class="el-icon-zoom-in"/></el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </template>

    <!-- 第2层：子行业层 -->
    <template v-if="currentDepth === 2 && currentSub">
      <el-row :gutter="16" style="margin-bottom:16px;">
        <el-col :span="6" v-for="c in currentSub.stats" :key="c.label">
          <el-card class="stat-card" shadow="never" :style="{'border-left': '4px solid ' + ipSecondary}">
            <div class="stat-inner">
              <div><div class="stat-num" :style="{color: ipSecondary}">{{ c.value }}</div><div class="stat-lbl">{{ c.label }}</div></div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-card shadow="never">
        <div slot="header" style="font-weight:600;">{{ currentSub.name }} — 企业穿透列表</div>
        <el-table :data="currentSub.companies" border :row-class-name="companyRowClass" @row-click="drillToCompany">
          <el-table-column prop="code" label="企业代码" width="130"/>
          <el-table-column prop="name" label="企业名称" min-width="160"/>
          <el-table-column prop="type" label="企业类型" width="100" align="center">
            <template slot-scope="{row}">
              <el-tag :type="row.isMainBiz ? 'primary' : 'info'" size="mini">{{ row.isMainBiz ? '主业' : '非主业' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="revenue" label="营收(亿)" width="100" align="center"/>
          <el-table-column prop="profit" label="净利润(亿)" width="110" align="center">
            <template slot-scope="{row}"><span :style="{color: row.profit < 0 ? '#F5222D' : '#52C41A'}">{{ row.profit }}</span></template>
          </el-table-column>
          <el-table-column prop="assets" label="资产规模(亿)" width="120" align="center"/>
          <el-table-column prop="riskLevel" label="风险等级" width="100" align="center">
            <template slot-scope="{row}">
              <el-tag :type="row.riskLevel === 'HIGH' ? 'danger' : row.riskLevel === 'MEDIUM' ? 'warning' : 'success'" size="mini">
                {{ row.riskLevel === 'HIGH' ? '高风险' : row.riskLevel === 'MEDIUM' ? '中风险' : '低风险' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100" align="center">
            <template slot-scope="{row}">
              <el-button size="mini" type="primary" plain @click.stop="drillToCompany(row)">详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </template>

    <!-- 第3层：企业详情层 -->
    <template v-if="currentDepth === 3 && currentCompany">
      <el-row :gutter="16">
        <el-col :span="14">
          <el-card shadow="never" style="margin-bottom:16px;">
            <div slot="header" style="font-weight:600;"><i class="el-icon-office-building" :style="{marginRight:'6px', color: ipBright}"/>{{ currentCompany.name }}</div>
            <el-descriptions :column="2" border size="small">
              <el-descriptions-item label="企业代码">{{ currentCompany.code }}</el-descriptions-item>
              <el-descriptions-item label="所属行业">{{ currentSub.name }}</el-descriptions-item>
              <el-descriptions-item label="企业类型">
                <el-tag :type="currentCompany.isMainBiz ? 'primary' : 'info'" size="small">{{ currentCompany.isMainBiz ? '主业' : '非主业' }}</el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="风险等级">
                <el-tag :type="currentCompany.riskLevel === 'HIGH' ? 'danger' : currentCompany.riskLevel === 'MEDIUM' ? 'warning' : 'success'" size="small">
                  {{ currentCompany.riskLevel === 'HIGH' ? '高风险' : currentCompany.riskLevel === 'MEDIUM' ? '中风险' : '低风险' }}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="营收规模(亿)">{{ currentCompany.revenue }}</el-descriptions-item>
              <el-descriptions-item label="净利润(亿)"><span :style="{color: currentCompany.profit < 0 ? '#F5222D' : '#52C41A'}">{{ currentCompany.profit }}</span></el-descriptions-item>
              <el-descriptions-item label="资产规模(亿)">{{ currentCompany.assets }}</el-descriptions-item>
              <el-descriptions-item label="资产负债率">{{ currentCompany.debtRatio }}%</el-descriptions-item>
            </el-descriptions>
          </el-card>
          <el-card shadow="never">
            <div slot="header" style="font-weight:600;">风险指标详情</div>
            <el-table :data="currentCompany.riskIndicators" border size="small">
              <el-table-column prop="name" label="指标名称" min-width="140"/>
              <el-table-column prop="value" label="当前值" width="100" align="center"/>
              <el-table-column prop="threshold" label="预警阈值" width="100" align="center"/>
              <el-table-column prop="status" label="状态" width="90" align="center">
                <template slot-scope="{row}">
                  <el-tag :type="row.status === 'WARN' ? 'danger' : 'success'" size="mini">{{ row.status === 'WARN' ? '预警' : '正常' }}</el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
        <el-col :span="10">
          <el-card shadow="never">
            <div slot="header" style="font-weight:600;">穿透路径</div>
            <div class="drill-path">
              <div class="path-node" v-for="(b, i) in breadcrumbs" :key="i">
                <div class="path-node-icon" :style="{background: i === breadcrumbs.length -1 ? ipBright : '#f0f0f0'}">
                  <i :class="b.icon" :style="{color: i === breadcrumbs.length -1 ? '#fff' : '#8c8c8c'}"/>
                </div>
                <div class="path-node-label">{{ b.label }}</div>
                <div class="path-arrow" v-if="i < breadcrumbs.length - 1"><i class="el-icon-arrow-down" style="color:#bfbfbf;"/></div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </template>
  </div>
</template>

<script>
import { getDrillOverview, getDrillGroup, getDrillSubIndustry, getDrillCompany } from '@/api/stateAssets/industryPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'IndustryDrillDown',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      currentDepth: 0,
      currentGroup: null,
      currentSub: null,
      currentCompany: null,
      breadcrumbs: [{ label: '国有资本总览', icon: 'el-icon-office-building' }],
      // Level 0 数据
      groupStats: [],
      industryGroups: [],
      // Level 1 数据
      groupDetail: { stats: [], subIndustries: [] },
      // Level 2 数据
      subDetail: { stats: [], companies: [] },
      // Level 3 数据
      companyDetail: null,
    }
  },
  computed: {
    currentDepthLabel() {
      return ['集团层', '行业群层', '子行业层', '企业层'][this.currentDepth]
    },
  },
  created() { this.loadOverview() },
  methods: {
    /** Level 0: 加载集团层概览 */
    async loadOverview() {
      this.loading = true
      try {
        const res = await getDrillOverview()
        if (res && res.result === 200 && res.data) {
          this.groupStats = res.data.stats || []
          this.industryGroups = res.data.groups || []
        }
      } catch (e) { this.$message.error('加载概览数据失败') }
      this.loading = false
    },
    /** Level 1: 穿透到行业群 */
    async drillToGroup(g) {
      this.loading = true
      this.currentGroup = g
      this.currentDepth = 1
      this.breadcrumbs = [
        { label: '国有资本总览', icon: 'el-icon-office-building' },
        { label: g.name, icon: g.icon },
      ]
      try {
        const res = await getDrillGroup({ industryCode: g.code })
        if (res && res.result === 200 && res.data) {
          this.groupDetail = res.data
          this.currentGroup = { ...g, stats: res.data.stats || [] }
        }
      } catch (e) { this.$message.error('加载行业群数据失败') }
      this.loading = false
    },
    /** Level 2: 穿透到子行业 */
    async drillToSubIndustry(row) {
      this.loading = true
      this.currentSub = row
      this.currentDepth = 2
      this.breadcrumbs = [
        { label: '国有资本总览', icon: 'el-icon-office-building' },
        { label: this.currentGroup.name, icon: this.currentGroup.icon },
        { label: row.name, icon: 'el-icon-connection' },
      ]
      try {
        const res = await getDrillSubIndustry({ industryCode: this.currentGroup.code, subIndustry: row.name })
        if (res && res.result === 200 && res.data) {
          this.subDetail = res.data
          // 映射字段名以匹配模板
          const companies = (res.data.companies || []).map(c => ({
            code: c.companyId,
            name: c.companyName,
            isMainBiz: c.isMainBiz,
            revenue: c.revenue,
            profit: c.profit,
            assets: c.assets,
            riskLevel: c.riskLevel,
            companyId: c.companyId,
          }))
          this.currentSub = { ...row, stats: res.data.stats || [], companies }
        }
      } catch (e) { this.$message.error('加载子行业数据失败') }
      this.loading = false
    },
    /** Level 3: 穿透到企业详情 */
    async drillToCompany(row) {
      this.loading = true
      this.currentDepth = 3
      this.breadcrumbs = [
        { label: '国有资本总览', icon: 'el-icon-office-building' },
        { label: this.currentGroup.name, icon: this.currentGroup.icon },
        { label: this.currentSub.name, icon: 'el-icon-connection' },
        { label: row.name, icon: 'el-icon-s-custom' },
      ]
      try {
        const companyId = row.companyId || row.code
        const res = await getDrillCompany(companyId)
        if (res && res.result === 200 && res.data) {
          const d = res.data
          const base = d.baseInfo || {}
          const monitor = d.monitorData || {}
          // 扁平化映射给模板使用
          this.currentCompany = {
            code: base.companyId,
            name: base.companyName,
            isMainBiz: base.isMainBiz,
            riskLevel: base.riskLevel,
            revenue: base.revenue,
            profit: base.revenue && base.netMargin ? (base.revenue * base.netMargin / 100).toFixed(2) : 0,
            assets: base.assets,
            debtRatio: monitor.debtRatio || 65,
            riskIndicators: d.riskIndicators || [],
            warnings: d.warnings || [],
            stats: d.stats || [],
          }
        } else {
          this.currentCompany = row
        }
      } catch (e) {
        this.currentCompany = row
        this.$message.error('加载企业详情失败')
      }
      this.loading = false
    },
    /** 面包屑跳转 */
    jumpToBreadcrumb(idx) {
      if (idx === this.currentDepth) return
      if (idx === 0) {
        this.currentDepth = 0
        this.currentGroup = null
        this.currentSub = null
        this.currentCompany = null
        this.breadcrumbs = [{ label: '国有资本总览', icon: 'el-icon-office-building' }]
        this.loadOverview()
      } else if (idx === 1 && this.currentGroup) {
        this.currentDepth = 1
        this.currentSub = null
        this.currentCompany = null
        this.breadcrumbs = this.breadcrumbs.slice(0, 2)
      } else if (idx === 2 && this.currentSub) {
        this.currentDepth = 2
        this.currentCompany = null
        this.breadcrumbs = this.breadcrumbs.slice(0, 3)
      }
    },
    /** 返回上级 */
    goBack() {
      if (this.currentDepth > 0) this.jumpToBreadcrumb(this.currentDepth - 1)
    },
    /** 企业行高亮 */
    companyRowClass({ row }) {
      if (row.riskLevel === 'HIGH') return 'row-company-high'
      if (row.riskLevel === 'MEDIUM') return 'row-company-medium'
      return ''
    },
  },
}
</script>

<style lang="scss" scoped>
.drill-down-page { padding: 16px; background: #f5f7fa; min-height: 100%; }
.page-banner {
  background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 50%, var(--ip-bright, #1677FF) 100%);
  border-radius: 8px; padding: 20px 32px; display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px;
  .banner-title { font-size: 22px; font-weight: 700; color: #fff; }
  .banner-sub { font-size: 13px; color: rgba(255,255,255,0.8); margin-top: 4px; }
  .depth-badge { background: rgba(255,255,255,0.15); border-radius: 20px; padding: 6px 16px; color: #fff; font-size: 13px; }
}
.breadcrumb-card { margin-bottom: 16px; }
.breadcrumb-row { display: flex; align-items: center; }
.stat-card { border-radius: 6px; margin-bottom: 0; }
.stat-inner { display: flex; align-items: center; justify-content: space-between; padding: 4px 0; }
.stat-num { font-size: 28px; font-weight: 700; }
.stat-lbl { font-size: 12px; color: #8c8c8c; margin-top: 4px; }
.group-card {
  border-radius: 8px; overflow: hidden; cursor: pointer; transition: all 0.25s; margin-bottom: 16px;
  &:hover { transform: translateY(-3px); box-shadow: 0 8px 24px rgba(0,0,0,0.12); }
  .group-header { padding: 20px; display: flex; flex-direction: column; align-items: flex-start; gap: 6px; }
  .group-name { font-size: 16px; font-weight: 700; color: #fff; }
  .group-drill-hint { font-size: 12px; color: rgba(255,255,255,0.7); }
  .group-body { display: flex; justify-content: space-around; padding: 16px 8px; border-bottom: 1px solid #f0f0f0; }
  .group-metric { text-align: center; }
  .gm-val { font-size: 20px; font-weight: 700; color: #303133; display: block; }
  .gm-lbl { font-size: 12px; color: #8c8c8c; }
  .group-footer { padding: 10px 16px; display: flex; align-items: center; }
}
.drill-path { padding: 8px 0; }
.path-node { display: flex; flex-direction: column; align-items: center; padding: 8px 0; }
.path-node-icon { width: 36px; height: 36px; border-radius: 50%; display: flex; align-items: center; justify-content: center; margin-bottom: 4px; }
.path-node-label { font-size: 13px; font-weight: 600; }
.path-arrow { margin: 4px 0; }
::v-deep .row-company-high td { background: #FFF1F0 !important; }
::v-deep .row-company-medium td { background: #FFFBE6 !important; }
</style>
