<template>
  <div class="invest-penetration-home" :style="themeVars">
    <!-- 顶部 Banner -->
    <div class="page-banner">
      <div class="banner-left">
        <div class="banner-icon"><i class="el-icon-bank-card" /></div>
        <div class="banner-text">
          <h2>投资穿透监管</h2>
          <p>覆盖股权/债权/混合型投资全生命周期，穿透核查决策合规性与回报真实性</p>
        </div>
      </div>
      <div class="banner-tags">
        <el-tag v-if="stats.overdueMilestones > 0" type="danger" effect="dark">
          <i class="el-icon-warning" /> {{ stats.overdueMilestones }} 个里程碑逾期
        </el-tag>
        <el-tag v-if="stats.highRiskCount > 0" type="danger" effect="dark">
          <i class="el-icon-bell" /> {{ stats.highRiskCount }} 条高级预警
        </el-tag>
        <el-tag v-if="stats.nonMainBizAlert" type="warning" effect="dark">
          <i class="el-icon-warning-outline" /> 非主业占比预警
        </el-tag>
        <el-tag type="success" effect="plain">
          <i class="el-icon-check" /> 数据更新：{{ stats.updateTime }}
        </el-tag>
      </div>
    </div>

    <!-- KPI 概览条 -->
    <el-row :gutter="16" class="kpi-row">
      <el-col :span="4" v-for="k in kpiList" :key="k.label">
        <div class="kpi-card" :class="k.alertClass">
          <div class="kpi-label">{{ k.label }}</div>
          <div class="kpi-value">{{ k.value }}<span class="kpi-unit">{{ k.unit }}</span></div>
          <div class="kpi-sub">{{ k.sub }}</div>
        </div>
      </el-col>
    </el-row>

    <!-- 投资穿透链路可视化 -->
    <div class="section-title"><i class="el-icon-share" /> 投资穿透链路</div>
    <div class="chain-wrapper">
      <div class="chain-flow">
        <div v-for="(node, idx) in chainNodes" :key="node.key" class="chain-item">
          <div class="chain-node" :class="node.status" @click="jumpTo(node.route)">
            <div class="cn-icon"><i :class="node.icon" /></div>
            <div class="cn-name">{{ node.name }}</div>
            <div class="cn-count" v-if="node.count !== undefined">{{ node.count }}</div>
            <el-badge v-if="node.alert" :value="node.alert" type="danger" class="cn-badge" />
          </div>
          <div v-if="idx < chainNodes.length - 1" class="chain-arrow"><i class="el-icon-right" /></div>
        </div>
      </div>
      <div class="chain-legend">
        <span><i class="dot green" />正常</span>
        <span><i class="dot orange" />关注</span>
        <span><i class="dot red" />预警</span>
      </div>
    </div>

    <!-- 中部两列 -->
    <el-row :gutter="16" class="mid-row">
      <!-- 近期预警 -->
      <el-col :span="14">
        <div class="panel">
          <div class="panel-header">
            <span><i class="el-icon-bell" /> 风险预警（近30天）</span>
            <el-button type="text" size="mini" @click="$router.push('/modelMonitor/Fxyjgl')">查看全部</el-button>
          </div>
          <el-table :data="recentWarnings" size="small" height="280">
            <el-table-column label="预警编号" prop="warningCode" width="130" />
            <el-table-column label="预警类型" prop="warningType" width="110" />
            <el-table-column label="触发企业" prop="company" width="110" />
            <el-table-column label="项目名称" prop="projectName" show-overflow-tooltip />
            <el-table-column label="级别" width="70">
              <template slot-scope="{row}">
                <el-tag :type="row.level === 'HIGH' ? 'danger' : row.level === 'MEDIUM' ? 'warning' : 'info'" size="mini">{{ row.levelLabel }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="触发时间" prop="triggerTime" width="110" />
            <el-table-column label="状态" width="70">
              <template slot-scope="{row}">
                <el-tag :type="row.handleStatus === 'PENDING' ? 'danger' : 'success'" size="mini">{{ row.handleStatus === 'PENDING' ? '待处理' : '已处理' }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>

      <!-- 各企业非主业占比 -->
      <el-col :span="10">
        <div class="panel">
          <div class="panel-header">
            <span><i class="el-icon-pie-chart" /> 非主业投资占比</span>
            <el-button type="text" size="mini" @click="$router.push('/modelMonitor/Fzytzfx')">详情</el-button>
          </div>
          <div class="non-main-list">
            <div v-for="n in nonMainBizStats" :key="n.company" class="nm-item">
              <span class="nm-company">{{ n.company }}</span>
              <el-progress
                :percentage="n.ratio"
                :color="n.ratio > 20 ? '#FF4D4F' : n.ratio > 10 ? '#FA8C16' : '#52C41A'"
                :stroke-width="12"
                class="nm-progress"
              />
              <span class="nm-ratio" :style="{color: n.ratio > 20 ? '#FF4D4F' : n.ratio > 10 ? '#FA8C16' : '#52C41A'}">
                {{ n.ratio }}%
              </span>
            </div>
          </div>
          <div class="nm-alert-tip">
            <i class="el-icon-warning" style="color:#FA8C16" /> 非主业占比 &gt; 20% 触发橙色预警
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 下部两列 -->
    <el-row :gutter="16" class="btm-row">
      <!-- 投资进度 -->
      <el-col :span="12">
        <div class="panel">
          <div class="panel-header">
            <span><i class="el-icon-s-order" /> 重点投资项目进度</span>
            <el-button type="text" size="mini" @click="$router.push('/modelMonitor/Tzjdjk')">查看全部</el-button>
          </div>
          <div class="progress-list">
            <div v-for="p in keyProjects" :key="p.projectId" class="progress-item">
              <div class="pi-top">
                <span class="pi-name">{{ p.projectName }}</span>
                <el-tag :type="p.progressRate >= 80 ? 'success' : p.progressRate >= 60 ? 'warning' : 'danger'" size="mini">{{ p.progressRate }}%</el-tag>
              </div>
              <el-progress :percentage="p.progressRate"
                :color="p.progressRate >= 80 ? '#52C41A' : p.progressRate >= 60 ? '#FA8C16' : '#FF4D4F'"
                :stroke-width="8" />
              <div class="pi-bottom">
                <span>{{ p.company }}</span>
                <span>计划完成：{{ p.planDate }}</span>
                <span :style="{color: p.delay > 0 ? '#FF4D4F' : '#52C41A'}">{{ p.delay > 0 ? `逾期 ${p.delay} 天` : '按计划' }}</span>
              </div>
            </div>
          </div>
        </div>
      </el-col>

      <!-- 功能模块入口 -->
      <el-col :span="12">
        <div class="panel">
          <div class="panel-header"><span><i class="el-icon-menu" /> 功能模块</span></div>
          <div class="module-grid">
            <div v-for="m in modules" :key="m.name" class="module-item" @click="$router.push(m.route)">
              <i :class="m.icon" class="module-icon" />
              <div class="module-name">{{ m.name }}</div>
              <div class="module-desc">{{ m.desc }}</div>
              <el-badge v-if="m.badgeCount" :value="m.badgeCount" type="danger" class="module-badge" />
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getInvestDashboard, getInvestWarningList, getNonMainBizStats } from '@/api/stateAssets/investPenetration'
import { investThemeMixin } from '../themeMixin'

const WARNING_TYPE_MAP = {
  UNAUTHORIZED: '越权投资',
  RETURN_DEVIATION: '收益偏离',
  PROGRESS_DELAY: '进度滞后',
  NON_MAIN_EXCEED: '非主业超限',
  COMPLIANCE: '决策程序',
  EXIT_OVERDUE: '清退逾期',
}

export default {
  name: 'InvestPenetrationHome',
  mixins: [investThemeMixin],
  data() {
    return {
      stats: { overdueMilestones: 0, highRiskCount: 0, nonMainBizAlert: false, updateTime: '-' },
      kpiList: [],
      chainNodes: [
        { key: 'home', name: '穿透首页', icon: 'el-icon-s-home', status: 'normal', route: '/modelMonitor/TzctIndex' },
        { key: 'project', name: '项目台账', icon: 'el-icon-document', status: 'normal', count: 0, route: '/modelMonitor/Tzxmtz' },
        { key: 'progress', name: '进度监控', icon: 'el-icon-s-order', status: 'normal', alert: 0, route: '/modelMonitor/Tzjdjk' },
        { key: 'drill', name: '穿透分析', icon: 'el-icon-share', status: 'normal', route: '/modelMonitor/Tzctfx' },
        { key: 'compliance', name: '合规追踪', icon: 'el-icon-s-check', status: 'normal', alert: 0, route: '/modelMonitor/Tzjchgzz' },
        { key: 'postEval', name: '投后评价', icon: 'el-icon-data-analysis', status: 'normal', route: '/modelMonitor/Thpjgl' },
        { key: 'nonMain', name: '非主业分析', icon: 'el-icon-pie-chart', status: 'normal', route: '/modelMonitor/Fzytzfx' },
        { key: 'risk', name: '风险预警', icon: 'el-icon-bell', status: 'normal', alert: 0, route: '/modelMonitor/Fxyjgl' },
        { key: 'dashboard', name: '监控驾驶舱', icon: 'el-icon-odometer', status: 'normal', route: '/modelMonitor/Tzjkjsc' },
      ],
      recentWarnings: [],
      nonMainBizStats: [],
      keyProjects: [],
      modules: [
        { name: '项目台账', icon: 'el-icon-document', desc: '投资项目全量管理', route: '/modelMonitor/Tzxmtz', badgeCount: null },
        { name: '穿透分析', icon: 'el-icon-share', desc: '股权结构多层穿透', route: '/modelMonitor/Tzctfx', badgeCount: null },
        { name: '合规追踪', icon: 'el-icon-s-check', desc: '决策程序合规核查', route: '/modelMonitor/Tzjchgzz', badgeCount: null },
        { name: '投后评价', icon: 'el-icon-data-analysis', desc: 'KPI达成率跟踪', route: '/modelMonitor/Thpjgl', badgeCount: null },
        { name: '非主业分析', icon: 'el-icon-pie-chart', desc: '非主业占比预警', route: '/modelMonitor/Fzytzfx', badgeCount: null },
        { name: '风险预警', icon: 'el-icon-bell', desc: '10类预警规则引擎', route: '/modelMonitor/Fxyjgl', badgeCount: null },
        { name: '进度监控', icon: 'el-icon-s-order', desc: '里程碑完成追踪', route: '/modelMonitor/Tzjdjk', badgeCount: null },
        { name: '监控驾驶舱', icon: 'el-icon-odometer', desc: '综合可视化看板', route: '/modelMonitor/Tzjkjsc', badgeCount: null },
      ],
    }
  },
  created() {
    this.loadDashboard()
    this.loadWarnings()
    this.loadNonMainBiz()
  },
  methods: {
    async loadDashboard() {
      try {
        const res = await getInvestDashboard()
        if (res.result === 200 && res.data) {
          const kpi = res.data.kpi || {}
          this.stats = {
            overdueMilestones: 0,
            highRiskCount: kpi.activeWarnings || 0,
            nonMainBizAlert: (kpi.nonMainBizRatio || 0) > 15,
            updateTime: new Date().toLocaleString(),
          }
          this.kpiList = [
            { label: '在管投资项目', value: kpi.totalProjects || 0, unit: '个', sub: '', alertClass: '' },
            { label: '投资总额', value: ((kpi.totalInvestAmount || 0) / 10000).toFixed(1), unit: '亿元', sub: '', alertClass: '' },
            { label: '综合收益率', value: kpi.avgReturnRate || 0, unit: '%', sub: '目标8.0%', alertClass: (kpi.avgReturnRate || 0) < 8 ? 'kpi-warn' : '' },
            { label: '非主业占比', value: kpi.nonMainBizRatio || 0, unit: '%', sub: '预警线 20%', alertClass: (kpi.nonMainBizRatio || 0) > 15 ? 'kpi-warn' : '' },
            { label: '未解决预警', value: kpi.activeWarnings || 0, unit: '条', sub: '', alertClass: (kpi.activeWarnings || 0) > 5 ? 'kpi-danger' : '' },
            { label: '投后评价达标', value: kpi.postEvalPassRate || 0, unit: '%', sub: '目标 ≥ 80%', alertClass: (kpi.postEvalPassRate || 0) < 80 ? 'kpi-warn' : '' },
          ]
          // 动态更新链路节点
          this.$set(this.chainNodes[1], 'count', kpi.totalProjects || 0)
          this.$set(this.chainNodes[7], 'alert', kpi.activeWarnings || 0)
          if ((kpi.activeWarnings || 0) > 0) {
            this.$set(this.chainNodes[7], 'status', 'alert')
          }
          // 重点项目进度
          this.keyProjects = (res.data.projectProgress || []).map(p => ({
            projectId: p.name,
            projectName: p.name,
            company: '',
            progressRate: 50,
            planDate: '-',
            delay: 0,
          }))
        }
      } catch (e) {
        console.error('[loadDashboard]', e)
      }
    },
    async loadWarnings() {
      try {
        const res = await getInvestWarningList({ pageNumber: 1, pageSize: 5 })
        if (res.result === 200 && res.data) {
          this.recentWarnings = (res.data.tlist || []).map(w => ({
            warningCode: w.warningCode,
            warningType: WARNING_TYPE_MAP[w.warningType] || w.warningType,
            company: w.companyName,
            projectName: w.projectName,
            level: w.level,
            levelLabel: { HIGH: '高', MEDIUM: '中', LOW: '低' }[w.level] || w.level,
            triggerTime: w.triggerTime ? w.triggerTime.substring(0, 10) : '',
            handleStatus: w.status,
          }))
        }
      } catch (e) {
        console.error('[loadWarnings]', e)
      }
    },
    async loadNonMainBiz() {
      try {
        const res = await getNonMainBizStats()
        if (res.result === 200 && res.data) {
          this.nonMainBizStats = (res.data.companyStats || []).map(c => ({
            company: c.company,
            ratio: Number(c.ratio) || 0,
          }))
        }
      } catch (e) {
        console.error('[loadNonMainBiz]', e)
      }
    },
    jumpTo(route) {
      if (route) this.$router.push(route)
    },
  },
}
</script>

<style lang="scss" scoped>
.invest-penetration-home {
  padding: 16px;
  background: #f0f2f5;
  min-height: 100vh;
}

/* Banner */
.page-banner {
  background: linear-gradient(135deg, var(--ip-primary, #1A3A6B) 0%, var(--ip-secondary, #2A5298) 100%);
  border-radius: 8px;
  padding: 20px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  color: #fff;
  .banner-left {
    display: flex;
    align-items: center;
    gap: 16px;
  }
  .banner-icon {
    width: 48px;
    height: 48px;
    background: rgba(250,173,20,0.2);
    border: 2px solid var(--ip-accent, #FAAD14);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    i { font-size: 24px; color: var(--ip-accent, #FAAD14); }
  }
  .banner-text h2 { margin: 0; font-size: 20px; color: #fff; }
  .banner-text p { margin: 4px 0 0; font-size: 13px; color: rgba(255,255,255,0.75); }
  .banner-tags { display: flex; gap: 8px; flex-wrap: wrap; }
}

/* KPI */
.kpi-row { margin-bottom: 16px; }
.kpi-card {
  background: #fff;
  border-radius: 8px;
  padding: 14px 16px;
  border-left: 4px solid var(--ip-primary, #1A3A6B);
  box-shadow: 0 1px 6px rgba(0,0,0,.06);
  &.kpi-warn { border-left-color: #FA8C16; }
  &.kpi-danger { border-left-color: #FF4D4F; }
  .kpi-label { font-size: 12px; color: #888; margin-bottom: 4px; }
  .kpi-value { font-size: 22px; font-weight: 700; color: var(--ip-primary, #1A3A6B); }
  .kpi-unit { font-size: 13px; font-weight: 400; margin-left: 2px; }
  .kpi-sub { font-size: 11px; color: #aaa; margin-top: 2px; }
}

/* 穿透链路 */
.section-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--ip-primary, #1A3A6B);
  margin-bottom: 10px;
  i { margin-right: 6px; }
}
.chain-wrapper {
  background: #fff;
  border-radius: 8px;
  padding: 16px 20px;
  margin-bottom: 16px;
  box-shadow: 0 1px 6px rgba(0,0,0,.06);
}
.chain-flow {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 4px;
}
.chain-item {
  display: flex;
  align-items: center;
  gap: 4px;
}
.chain-node {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 80px;
  padding: 10px 4px;
  border-radius: 8px;
  border: 2px solid #d9d9d9;
  cursor: pointer;
  transition: all 0.2s;
  background: #fafafa;
  &:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(0,0,0,.12); }
  &.normal { border-color: #52C41A; }
  &.warn { border-color: #FA8C16; }
  &.alert { border-color: #FF4D4F; }
  .cn-icon i { font-size: 20px; color: var(--ip-primary, #1A3A6B); }
  .cn-name { font-size: 11px; margin-top: 4px; color: #333; }
  .cn-count { font-size: 16px; font-weight: 700; color: var(--ip-primary, #1A3A6B); }
  .cn-badge { position: absolute; top: -6px; right: -6px; }
}
.chain-arrow { color: var(--ip-accent, #FAAD14); font-size: 18px; }
.chain-legend {
  margin-top: 12px;
  font-size: 12px;
  color: #888;
  span { margin-right: 16px; }
  .dot {
    display: inline-block;
    width: 10px;
    height: 10px;
    border-radius: 50%;
    margin-right: 4px;
    vertical-align: middle;
    &.green { background: #52C41A; }
    &.orange { background: #FA8C16; }
    &.red { background: #FF4D4F; }
  }
}

/* Panel */
.panel {
  background: #fff;
  border-radius: 8px;
  padding: 12px 16px;
  box-shadow: 0 1px 6px rgba(0,0,0,.06);
  height: 100%;
  .panel-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 14px;
    font-weight: 600;
    color: var(--ip-primary, #1A3A6B);
    margin-bottom: 10px;
    padding-bottom: 8px;
    border-bottom: 2px solid var(--ip-accent, #FAAD14);
  }
}
.mid-row, .btm-row { margin-bottom: 16px; }

/* 非主业 */
.non-main-list { padding: 0 4px; }
.nm-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  gap: 8px;
  .nm-company { width: 70px; font-size: 12px; color: #555; flex-shrink: 0; }
  .nm-progress { flex: 1; }
  .nm-ratio { width: 44px; text-align: right; font-size: 13px; font-weight: 600; flex-shrink: 0; }
}
.nm-alert-tip { font-size: 11px; color: #888; margin-top: 8px; }

/* 进度列表 */
.progress-list { padding: 0 4px; }
.progress-item {
  margin-bottom: 14px;
  .pi-top {
    display: flex;
    justify-content: space-between;
    margin-bottom: 4px;
    .pi-name { font-size: 13px; font-weight: 500; color: #333; }
  }
  .pi-bottom {
    display: flex;
    justify-content: space-between;
    font-size: 11px;
    color: #888;
    margin-top: 4px;
  }
}

/* 模块网格 */
.module-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
}
.module-item {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 12px 6px;
  border-radius: 8px;
  border: 1px solid #e8e8e8;
  cursor: pointer;
  transition: all 0.2s;
  text-align: center;
  &:hover {
    background: #{'rgba(var(--ip-primary-rgb, 26,58,107), 0.05)'};
    border-color: var(--ip-primary, #1A3A6B);
    transform: translateY(-2px);
    box-shadow: 0 4px 12px #{'rgba(var(--ip-primary-rgb, 26,58,107), 0.12)'};
  }
  .module-icon { font-size: 22px; color: var(--ip-primary, #1A3A6B); margin-bottom: 4px; }
  .module-name { font-size: 12px; font-weight: 600; color: #333; }
  .module-desc { font-size: 10px; color: #aaa; margin-top: 2px; }
  .module-badge { position: absolute; top: -4px; right: -4px; }
}
</style>
