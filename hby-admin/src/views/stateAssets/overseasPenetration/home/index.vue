<template>
  <div class="overseas-penetration-container" :style="themeVars">
    <!-- Banner区域 -->
    <div class="page-banner">
      <div class="banner-left">
        <h1 class="page-title"><i class="el-icon-place"></i> 境外穿透</h1>
        <p class="page-subtitle">境外投资·经营·风险·外汇·合规 全链条穿透监管</p>
      </div>
      <div class="banner-right">
        <el-button type="primary" plain icon="el-icon-map-location" @click="goTo('countryRiskMap')">风险地图</el-button>
        <el-button type="primary" plain icon="el-icon-odometer" @click="goTo('dashboard')">监控驾驶舱</el-button>
      </div>
    </div>

    <!-- 穿透链路图 -->
    <div class="penetration-chain">
      <div class="chain-node" v-for="(node, idx) in chainNodes" :key="node.key" @click="goTo(node.key)">
        <div class="node-icon"><i :class="node.icon"></i></div>
        <div class="node-label">{{ node.label }}</div>
        <div class="node-count">{{ node.count }}</div>
        <div class="chain-arrow" v-if="idx < chainNodes.length - 1"><i class="el-icon-right"></i></div>
      </div>
    </div>

    <!-- 全景KPI -->
    <div class="stats-overview">
      <el-row :gutter="16">
        <el-col :span="4" v-for="(item, idx) in kpiCards" :key="idx">
          <div class="stat-card">
            <div class="stat-icon" :class="'icon-' + (idx + 1)"><i :class="item.icon"></i></div>
            <div class="stat-content">
              <div class="stat-value">{{ item.value }}</div>
              <div class="stat-label">{{ item.label }}</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能模块导航 -->
    <div class="function-modules">
      <el-row :gutter="16" v-for="(row, rowIdx) in moduleRows" :key="'row-' + rowIdx" :style="rowIdx > 0 ? 'margin-top: 16px' : ''">
        <el-col :span="8" v-for="(mod, idx) in row" :key="mod.key">
          <div class="module-card" @click="goTo(mod.key)">
            <div class="card-header">
              <div class="card-icon"><i :class="mod.icon"></i></div>
              <div class="card-title">
                <h3>{{ mod.title }}</h3>
                <span class="card-subtitle">{{ mod.desc }}</span>
              </div>
            </div>
            <div class="card-features">
              <span class="feature-tag" v-for="f in mod.features" :key="f">{{ f }}</span>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 快速操作面板 -->
    <div class="quick-actions-panel">
      <div class="panel-header"><h3><i class="el-icon-lightning"></i> 快速操作</h3></div>
      <el-row :gutter="12">
        <el-col :span="4" v-for="action in quickActions" :key="action.key">
          <div class="quick-action-item" @click="goTo(action.key)">
            <div class="action-icon"><i :class="action.icon"></i></div>
            <div class="action-content">
              <div class="action-title">{{ action.title }}</div>
              <div class="action-desc">{{ action.desc }}</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { getOverseasKPI } from '@/api/stateAssets/overseasPenetration'
import { investThemeMixin } from '../../themeMixin'
export default {
  name: 'OverseasPenetrationHome',
  mixins: [investThemeMixin],
  data() {
    return {
      chainNodes: [
        { key: 'investMgmt', label: '投资决策', icon: 'el-icon-money', count: '-' },
        { key: 'operationAnalysis', label: '经营监控', icon: 'el-icon-data-analysis', count: '-' },
        { key: 'countryRiskMap', label: '国别风险', icon: 'el-icon-map-location', count: '-' },
        { key: 'forexAnalysis', label: '外汇分析', icon: 'el-icon-data-line', count: '-' },
        { key: 'complianceMgmt', label: '合规安全', icon: 'el-icon-document-checked', count: '-' }
      ],
      kpiCards: [
        { label: '境外单位数', value: '-', icon: 'el-icon-office-building' },
        { label: '投资总额(亿美元)', value: '-', icon: 'el-icon-coin' },
        { label: '境外人员数', value: '-', icon: 'el-icon-user' },
        { label: '高风险国家', value: '-', icon: 'el-icon-warning' },
        { label: '外汇敞口(亿$)', value: '-', icon: 'el-icon-data-line' },
        { label: '合规预警', value: '-', icon: 'el-icon-bell' }
      ],
      modules: [
        { key: 'unit', title: '境外单位台账', desc: '管理境外单位基本信息与运营状态', icon: 'el-icon-office-building',
          features: ['单位登记', '信息维护', '组织架构', '经营状态'] },
        { key: 'investMgmt', title: '境外投资管理', desc: '管理境外投资项目与资金全流程', icon: 'el-icon-money',
          features: ['项目管理', '审批追踪', '收益分析', '风险评估'] },
        { key: 'operationAnalysis', title: '境外经营分析', desc: '分析境外企业经营状况与盈亏', icon: 'el-icon-data-analysis',
          features: ['营收利润', '盈亏对比', '经营趋势', '亏损预警'] },
        { key: 'countryRiskMap', title: '国别风险地图', desc: '可视化展示全球国别风险分布', icon: 'el-icon-map-location',
          features: ['风险地图', '风险评级', '政治风险', '经济风险'] },
        { key: 'forexAnalysis', title: '外汇风险分析', desc: '分析外汇敞口与汇率波动风险', icon: 'el-icon-data-line',
          features: ['敞口监控', '套保分析', '汇率趋势', '损益统计'] },
        { key: 'complianceMgmt', title: '境外合规管理', desc: '管理境外经营合规与违规事项', icon: 'el-icon-document-checked',
          features: ['合规检查', '违规处理', '反腐败', '出口管制'] },
        { key: 'personnelSafety', title: '人员安全管理', desc: '境外派驻人员安全状态管理', icon: 'el-icon-user',
          features: ['人员台账', '安全状态', '一键呼救', '应急响应'] },
        { key: 'emergencyCommand', title: '应急指挥中心', desc: '境外应急事件管理与处置跟踪', icon: 'el-icon-phone-outline',
          features: ['事件管理', '应急预案', '资源调度', '处置跟踪'] },
        { key: 'dashboard', title: '境外监控驾驶舱', desc: '实时监控境外整体经营与风险态势', icon: 'el-icon-odometer',
          features: ['经营分析', '风险态势', '预警汇总', '综合报告'] }
      ],
      quickActions: [
        { key: 'unit', title: '境外台账', desc: '查看境外单位', icon: 'el-icon-office-building' },
        { key: 'investMgmt', title: '投资管理', desc: '境外投资项目', icon: 'el-icon-money' },
        { key: 'countryRiskMap', title: '风险地图', desc: '国别风险分布', icon: 'el-icon-map-location' },
        { key: 'operationAnalysis', title: '经营分析', desc: '境外经营分析', icon: 'el-icon-data-analysis' },
        { key: 'personnelSafety', title: '人员安全', desc: '境外人员管理', icon: 'el-icon-user' },
        { key: 'dashboard', title: '监控驾驶舱', desc: '境外态势总览', icon: 'el-icon-odometer' }
      ]
    }
  },
  mounted() {
    this.loadData()
  },
  computed: {
    moduleRows() {
      const rows = []
      for (let i = 0; i < this.modules.length; i += 3) { rows.push(this.modules.slice(i, i + 3)) }
      return rows
    }
  },
  methods: {
    async loadData() {
      try {
        const res = await getOverseasKPI()
        if (res && res.result === 200 && res.data) {
          const d = res.data
          if (d.unitCount != null) this.kpiCards[0].value = d.unitCount
          if (d.investTotal != null) this.kpiCards[1].value = d.investTotal
          if (d.personnelCount != null) this.kpiCards[2].value = d.personnelCount
          if (d.highRiskCountries != null) this.kpiCards[3].value = d.highRiskCountries
          if (d.forexExposure != null) this.kpiCards[4].value = d.forexExposure
          if (d.complianceWarning != null) this.kpiCards[5].value = d.complianceWarning
          if (d.investCount != null) this.chainNodes[0].count = d.investCount + '项'
          if (d.unitCount != null) this.chainNodes[1].count = d.unitCount + '家'
          if (d.countryCount != null) this.chainNodes[2].count = d.countryCount + '国'
          if (d.currencyCount != null) this.chainNodes[3].count = d.currencyCount + '币种'
          if (d.complianceCount != null) this.chainNodes[4].count = d.complianceCount + '条'
        }
      } catch (e) {
        console.warn('境外KPI数据加载失败', e)
      }
    },
    goTo(key) {
      const routeMap = {
        unit: '/enterprise/Jwdwtz',
        investMgmt: '/enterprise/Jwtzgl',
        operationAnalysis: '/enterprise/Jwjyfx',
        countryRiskMap: '/enterprise/Gbfxdt',
        forexAnalysis: '/enterprise/Whfxfx',
        complianceMgmt: '/enterprise/Jwhggl',
        personnelSafety: '/enterprise/Jwryaqgl',
        emergencyCommand: '/enterprise/Jwyjzhzx',
        dashboard: '/enterprise/Jwjkjsc'
      }
      const path = routeMap[key]
      if (path) { this.$router.push(path).catch(() => {}) }
      else { this.$message.info(`${key}功能模块加载中...`) }
    }
  }
}
</script>

<style lang="scss" scoped>
.overseas-penetration-container {
  padding: 16px;
  background: #f0f2f5;
  min-height: calc(100vh - 84px);
}

/* Banner */
.page-banner {
  display: flex; justify-content: space-between; align-items: center;
  padding: 20px 24px; margin-bottom: 16px;
  background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 50%, var(--ip-bright, #1677FF) 100%);
  border-radius: 6px; color: #fff;
  .page-title { font-size: 22px; font-weight: 600; margin: 0 0 6px; display: flex; align-items: center;
    i { margin-right: 10px; font-size: 26px; } }
  .page-subtitle { font-size: 13px; opacity: 0.85; margin: 0; }
  .banner-right { .el-button { margin-left: 10px; } }
}

/* 穿透链路图 */
.penetration-chain {
  display: flex; align-items: center; justify-content: center;
  background: #fff; border-radius: 6px; padding: 18px 24px; margin-bottom: 16px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);
  .chain-node {
    display: flex; flex-direction: column; align-items: center; cursor: pointer;
    position: relative; padding: 0 20px;
    &:hover .node-icon { transform: scale(1.1); box-shadow: 0 4px 12px rgba(22,119,255,0.3); }
    .node-icon {
      width: 50px; height: 50px; border-radius: 50%; display: flex; align-items: center; justify-content: center;
      background: linear-gradient(135deg, var(--ip-primary, #003A6C), var(--ip-bright, #1677FF)); color: #fff; font-size: 22px;
      transition: all 0.3s; margin-bottom: 8px;
    }
    .node-label { font-size: 13px; font-weight: 600; color: #303133; margin-bottom: 2px; }
    .node-count { font-size: 12px; color: #909399; }
    .chain-arrow {
      position: absolute; right: -14px; top: 22px; font-size: 18px; color: #C0C4CC;
    }
  }
}

/* KPI统计 */
.stats-overview {
  margin-bottom: 16px;
  .stat-card {
    background: #fff; border-radius: 6px; padding: 16px; display: flex; align-items: center;
    box-shadow: 0 1px 4px rgba(0,0,0,0.08); transition: all 0.3s;
    &:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(0,0,0,0.12); }
    .stat-icon {
      width: 46px; height: 46px; border-radius: 10px; display: flex; align-items: center; justify-content: center; margin-right: 12px;
      i { font-size: 22px; color: #fff; }
      &.icon-1 { background: linear-gradient(135deg, var(--ip-primary, #003A6C), var(--ip-bright, #1677FF)); }
      &.icon-2 { background: linear-gradient(135deg, #FA8C16, #FAAD14); }
      &.icon-3 { background: linear-gradient(135deg, #52C41A, #73D13D); }
      &.icon-4 { background: linear-gradient(135deg, #F5222D, #FF4D4F); }
      &.icon-5 { background: linear-gradient(135deg, #722ED1, #9254DE); }
      &.icon-6 { background: linear-gradient(135deg, #EB2F96, #F759AB); }
    }
    .stat-content {
      .stat-value { font-size: 22px; font-weight: 600; color: #303133; line-height: 1.2; }
      .stat-label { font-size: 12px; color: #909399; margin-top: 2px; }
    }
  }
}

/* 功能模块卡片 */
.function-modules {
  margin-bottom: 16px;
  .module-card {
    background: #fff; border-radius: 6px; padding: 18px; cursor: pointer;
    box-shadow: 0 1px 4px rgba(0,0,0,0.08); transition: all 0.3s;
    border-left: 3px solid var(--ip-bright, #1677FF);
    &:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(0,0,0,0.12); }
    .card-header { display: flex; align-items: flex-start; margin-bottom: 12px; }
    .card-icon {
      width: 40px; height: 40px; border-radius: 8px; display: flex; align-items: center; justify-content: center;
      background: linear-gradient(135deg, var(--ip-primary, #003A6C), var(--ip-bright, #1677FF)); color: #fff; font-size: 20px;
      margin-right: 12px; flex-shrink: 0;
    }
    .card-title {
      h3 { font-size: 15px; font-weight: 600; color: #303133; margin: 0 0 4px; }
      .card-subtitle { font-size: 12px; color: #909399; }
    }
    .card-features {
      .feature-tag {
        display: inline-block; font-size: 12px; color: var(--ip-bright, #1677FF); background: var(--ip-light-bg, #EBF1FF);
        padding: 2px 8px; border-radius: 3px; margin: 2px 4px 2px 0;
      }
    }
  }
}

/* 快速操作 */
.quick-actions-panel {
  background: #fff; border-radius: 6px; padding: 18px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);
  .panel-header { margin-bottom: 14px;
    h3 { font-size: 16px; font-weight: 600; color: #303133; margin: 0; display: flex; align-items: center;
      i { margin-right: 8px; color: #FA8C16; } }
  }
  .quick-action-item {
    background: #f8f9fa; border-radius: 6px; padding: 12px; cursor: pointer; transition: all 0.3s;
    display: flex; align-items: center;
    &:hover { background: var(--ip-light-bg, #EBF1FF); transform: translateY(-2px); }
    .action-icon {
      width: 36px; height: 36px; border-radius: 8px; background: linear-gradient(135deg, var(--ip-primary, #003A6C), var(--ip-bright, #1677FF));
      display: flex; align-items: center; justify-content: center; margin-right: 10px;
      i { font-size: 18px; color: #fff; }
    }
    .action-content {
      .action-title { font-size: 13px; font-weight: 600; color: #303133; }
      .action-desc { font-size: 11px; color: #909399; }
    }
  }
}
</style>
