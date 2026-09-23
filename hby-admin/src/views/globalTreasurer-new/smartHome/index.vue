<template>
  <div class="smart-treasurer-home" :style="themeVars">
    <!-- ========== 顶部欢迎区 ========== -->
    <div class="welcome-section">
      <div class="welcome-bg"></div>
      <div class="welcome-content">
        <div class="welcome-left">
          <div class="greeting">
            <span class="greeting-icon"><i class="el-icon-sunny" /></span>
            <span class="greeting-text">{{ greetingWord }}，{{ userName }}</span>
          </div>
          <h1 class="welcome-title">全球司库管理平台</h1>
          <p class="welcome-desc">资金归集 · 资金计划 · 现金管理 · 融资管理 · 投资管理 · 风险管控 · 决策支持</p>
          <div class="welcome-actions">
            <el-button type="primary" icon="el-icon-data-board" round @click="$router.push('/czzjgj/fundConcentrationindex')">资金归集</el-button>
            <el-button icon="el-icon-s-finance" round @click="$router.push('/czzjjh/fundPlanningzjjh')">资金计划</el-button>
            <el-button icon="el-icon-data-analysis" round @click="$router.push('/zjdd/decisionSupport')">决策支持</el-button>
          </div>
        </div>
        <div class="welcome-stats">
          <div class="ws-item" v-for="s in welcomeStats" :key="s.label">
            <div class="ws-value" :style="{color: s.color}">{{ s.value }}</div>
            <div class="ws-label">{{ s.label }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- ========== KPI 数据卡片 ========== -->
    <div class="kpi-section">
      <div class="kpi-card" v-for="k in kpiCards" :key="k.label" :class="k.theme">
        <div class="kpi-top">
          <div class="kpi-icon-wrap" :style="{background: k.iconBg}">
            <i :class="k.icon" :style="{color: k.iconColor}" />
          </div>
          <div class="kpi-trend" v-if="k.trend" :class="k.trend > 0 ? 'trend-up' : 'trend-down'">
            <i :class="k.trend > 0 ? 'el-icon-top' : 'el-icon-bottom'" />
            <span>{{ Math.abs(k.trend) }}%</span>
          </div>
        </div>
        <div class="kpi-value">{{ k.value }}<span class="kpi-unit">{{ k.unit }}</span></div>
        <div class="kpi-label">{{ k.label }}</div>
        <div class="kpi-bar" :style="{background: k.barBg}">
          <div class="kpi-bar-fill" :style="{width: k.barWidth + '%', background: k.barColor}" />
        </div>
      </div>
    </div>

    <!-- ========== 司库资金全链路流程图（拓扑图） ========== -->
    <div class="lifecycle-section">
      <div class="section-header">
        <div class="section-title"><i class="el-icon-share" /> 司库资金全链路流程</div>
        <div class="section-legend">
          <span><i class="dot blue" />主流程</span>
          <span><i class="dot green" />子模块</span>
          <span><i class="dot orange" />执行层</span>
          <span><i class="dot gray" />基础支撑</span>
        </div>
      </div>
      <div class="flow-canvas-wrap">
        <div class="flow-canvas" :style="{ width: canvasW + 'px', height: canvasH + 'px' }">
          <svg class="flow-svg" :width="canvasW" :height="canvasH">
            <defs>
              <marker id="fl-arrow" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="7" markerHeight="7" orient="auto">
                <path d="M0,0 L10,5 L0,10 z" fill="#bbb" />
              </marker>
              <marker id="fl-arrow-main" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="8" markerHeight="8" orient="auto">
                <path d="M0,0 L10,5 L0,10 z" fill="#1890FF" />
              </marker>
              <marker id="fl-arrow-sub" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="7" markerHeight="7" orient="auto">
                <path d="M0,0 L10,5 L0,10 z" fill="#52C41A" />
              </marker>
              <marker id="fl-arrow-exec" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="7" markerHeight="7" orient="auto">
                <path d="M0,0 L10,5 L0,10 z" fill="#FA8C16" />
              </marker>
            </defs>
            <path v-for="(e, idx) in edgePaths" :key="'e'+idx" :d="e.d" :class="['fl-edge', e.cls]" :marker-end="e.marker" fill="none" />
          </svg>
          <div v-for="n in flowNodes" :key="n.id"
               class="flow-pos-node"
               :class="['fpn-'+n.kind]"
               :style="nodePos(n)"
               @click="jumpTo(n.route)">
            <div class="fpn-icon-wrap"><i :class="n.icon" /></div>
            <div class="fpn-label">{{ n.name }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- ========== 中部：资金归集 + 现金管理 + 资金计划 ========== -->
    <el-row :gutter="14" class="content-row">
      <!-- 资金归集 -->
      <el-col :span="10">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-coin" /> 资金归集</span>
            <span class="card-more" @click="jumpTo('/czzjgj/fundConcentrationindex')">资金池管理 →</span>
          </div>
          <el-tabs v-model="concentActive" class="inner-tabs">
            <el-tab-pane name="pool">
              <span slot="label">资金池</span>
              <div class="concent-list">
                <div v-for="item in concentPoolItems" :key="item.name" class="concent-entry" @click="jumpTo(item.route)">
                  <div class="ce-icon" :style="{background: item.bgColor}">
                    <i :class="item.icon" :style="{color: item.iconColor}" />
                  </div>
                  <div class="ce-body">
                    <div class="ce-name">{{ item.name }}</div>
                    <div class="ce-desc">{{ item.desc }}</div>
                  </div>
                  <i class="el-icon-arrow-right ce-arrow" />
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane name="stat">
              <span slot="label">归集统计</span>
              <div class="concent-list">
                <div v-for="item in concentStatItems" :key="item.name" class="concent-entry" @click="jumpTo(item.route)">
                  <div class="ce-icon" :style="{background: item.bgColor}">
                    <i :class="item.icon" :style="{color: item.iconColor}" />
                  </div>
                  <div class="ce-body">
                    <div class="ce-name">{{ item.name }}</div>
                    <div class="ce-desc">{{ item.desc }}</div>
                  </div>
                  <i class="el-icon-arrow-right ce-arrow" />
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-col>

      <!-- 现金管理 -->
      <el-col :span="7">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-wallet" /> 现金管理</span>
            <span class="card-more" @click="jumpTo('/b2cfw/cashManagementzjdb')">资金调拨 →</span>
          </div>
          <div class="cash-grid">
            <div v-for="c in cashItems" :key="c.name" class="cash-card" @click="jumpTo(c.route)">
              <div class="cc-icon" :style="{background: c.bgColor}">
                <i :class="c.icon" :style="{color: c.iconColor}" />
              </div>
              <div class="cc-body">
                <div class="cc-name">{{ c.name }}</div>
                <div class="cc-desc">{{ c.desc }}</div>
              </div>
              <el-badge v-if="c.badge" :value="c.badge" class="cc-badge" />
            </div>
          </div>
          <div class="cash-flow">
            <div class="cf-row" v-for="cf in cashFlowStats" :key="cf.label">
              <span class="cf-label">{{ cf.label }}</span>
              <div class="cf-bar-bg"><div class="cf-bar-fill" :style="{width: cf.percent + '%', background: cf.color}" /></div>
              <span class="cf-val">{{ cf.value }}</span>
            </div>
          </div>
        </div>
      </el-col>

      <!-- 资金计划 -->
      <el-col :span="7">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-date" /> 资金计划</span>
            <span class="card-more" @click="jumpTo('/czzjjh/fundPlanningzjjh')">资金计划 →</span>
          </div>
          <div class="plan-list">
            <div v-for="p in planItems" :key="p.name" class="plan-entry" @click="jumpTo(p.route)">
              <div class="pe-icon" :style="{background: p.bgColor}">
                <i :class="p.icon" :style="{color: p.iconColor}" />
              </div>
              <div class="pe-info">
                <span class="pe-name">{{ p.name }}</span>
                <span class="pe-desc">{{ p.desc }}</span>
              </div>
              <i class="el-icon-arrow-right pe-arrow" />
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- ========== 融资管理 + 投资管理 + 票据管理 ========== -->
    <el-row :gutter="14" class="content-row">
      <el-col :span="8">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-s-finance" /> 融资管理</span>
            <span class="card-more" @click="jumpTo('/xyzgl/financingManagementindex')">融资计划 →</span>
          </div>
          <div class="finance-list">
            <div v-for="f in financeItems" :key="f.name" class="finance-entry" @click="jumpTo(f.route)">
              <div class="fe-dot" :style="{background: f.color}"></div>
              <span class="fe-name">{{ f.name }}</span>
              <span class="fe-count" v-if="f.count" :style="{color: f.color}">{{ f.count }}</span>
            </div>
          </div>
          <div class="finance-progress">
            <div class="fp-row" v-for="fp in financeProgress" :key="fp.label">
              <span class="fp-label">{{ fp.label }}</span>
              <div class="fp-bar-bg"><div class="fp-bar-fill" :style="{width: fp.percent + '%', background: fp.color}" /></div>
              <span class="fp-val">{{ fp.percent }}%</span>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-data-line" /> 投资管理</span>
            <span class="card-more" @click="jumpTo('/zjjh/investmentManagementtzjh')">投资计划 →</span>
          </div>
          <div class="invest-grid">
            <div v-for="inv in investItems" :key="inv.name" class="invest-card" @click="jumpTo(inv.route)">
              <div class="iv-icon" :style="{background: inv.bgColor}">
                <i :class="inv.icon" :style="{color: inv.iconColor}" />
              </div>
              <div class="iv-body">
                <div class="iv-name">{{ inv.name }}</div>
                <div class="iv-desc">{{ inv.desc }}</div>
              </div>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-tickets" /> 票据管理</span>
            <span class="card-more" @click="jumpTo('/syhp/billManagementindex')">票据管理 →</span>
          </div>
          <div class="bill-grid">
            <div v-for="b in billItems" :key="b.name" class="bill-card" @click="jumpTo(b.route)">
              <div class="bl-icon" :style="{background: b.bgColor}">
                <i :class="b.icon" :style="{color: b.iconColor}" />
              </div>
              <div class="bl-name">{{ b.name }}</div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- ========== 功能模块 + 风险管理 + 结算 ========== -->
    <el-row :gutter="14" class="content-row">
      <el-col :span="12">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-grid" /> 功能模块</span>
          </div>
          <div class="module-grid">
            <div v-for="m in modules" :key="m.name" class="module-card" @click="jumpTo(m.route)">
              <div class="mc-icon" :style="{background: m.iconBg}">
                <i :class="m.icon" :style="{color: m.iconColor}" />
              </div>
              <div class="mc-name">{{ m.name }}</div>
              <div class="mc-desc">{{ m.desc }}</div>
            </div>
          </div>
        </div>
      </el-col>

      <el-col :span="12">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-warning-outline" /> 风险管控与结算</span>
          </div>
          <div class="risk-grid">
            <div class="rk-section">
              <div class="rk-title">风险管理</div>
              <div v-for="r in riskItems" :key="r.name" class="rk-item" @click="jumpTo(r.route)">
                <i :class="r.icon" :style="{color: r.iconColor}" /><span>{{ r.name }}</span>
              </div>
            </div>
            <div class="rk-section">
              <div class="rk-title">结算平台</div>
              <div v-for="s in settleItems" :key="s.name" class="rk-item" @click="jumpTo(s.route)">
                <i :class="s.icon" /><span>{{ s.name }}</span>
              </div>
            </div>
            <div class="rk-section">
              <div class="rk-title">账户管理</div>
              <div v-for="a in accountItems" :key="a.name" class="rk-item" @click="jumpTo(a.route)">
                <i :class="a.icon" /><span>{{ a.name }}</span>
              </div>
            </div>
            <div class="rk-section">
              <div class="rk-title">基础配置</div>
              <div v-for="b in baseConfigItems" :key="b.name" class="rk-item" @click="jumpTo(b.route)">
                <i :class="b.icon" /><span>{{ b.name }}</span>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- ========== 决策支持 + 金融衍生品 + 监管报送 ========== -->
    <el-row :gutter="14" class="content-row">
      <el-col :span="8">
        <div class="card-panel ai-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-data-analysis" /> 决策支持中心</span>
            <span class="card-more" @click="jumpTo('/zjdd/decisionSupport')">进入 →</span>
          </div>
          <div class="decision-grid">
            <div v-for="d in decisionItems" :key="d.name" class="decision-card" @click="jumpTo(d.route)">
              <div class="dc-icon" :style="{background: d.bgColor}">
                <i :class="d.icon" :style="{color: d.iconColor}" />
              </div>
              <div class="dc-body">
                <div class="dc-name">{{ d.name }}</div>
                <div class="dc-desc">{{ d.desc }}</div>
              </div>
              <i class="el-icon-arrow-right dc-arrow" />
            </div>
          </div>
        </div>
      </el-col>

      <el-col :span="8">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-s-mark" /> 金融衍生品</span>
            <span class="card-more" @click="jumpTo('/yspx/derivativesyspx')">衍生品交易 →</span>
          </div>
          <div class="deriv-grid">
            <div v-for="dv in derivItems" :key="dv.name" class="deriv-card" @click="jumpTo(dv.route)">
              <div class="dv-icon" :style="{background: dv.bgColor}">
                <i :class="dv.icon" :style="{color: dv.iconColor}" />
              </div>
              <div class="dv-body">
                <div class="dv-name">{{ dv.name }}</div>
                <div class="dv-desc">{{ dv.desc }}</div>
              </div>
            </div>
          </div>
        </div>
      </el-col>

      <el-col :span="8">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-document" /> 监管报送</span>
          </div>
          <div class="report-grid">
            <div v-for="r in reportItems" :key="r.name" class="report-card" @click="jumpTo(r.route)">
              <div class="rp-icon" :style="{background: r.bgColor}">
                <i :class="r.icon" :style="{color: r.iconColor}" />
              </div>
              <div class="rp-body">
                <div class="rp-name">{{ r.name }}</div>
                <div class="rp-desc">{{ r.desc }}</div>
              </div>
            </div>
          </div>
          <div class="report-extras">
            <div v-for="e in reportExtras" :key="e.name" class="re-item" @click="jumpTo(e.route)">
              <i :class="e.icon" /><span>{{ e.name }}</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { investThemeMixin } from '@/views/stateAssets/themeMixin'
import {
  getTreasurerSmartHomeData,
  getCashFlowStats,
  getFinanceProgress,
} from '@/api/globalTreasurer-new/smartHome'

export default {
  name: 'SmartTreasurerHome',
  mixins: [investThemeMixin],
  data() {
    return {
      userName: '',
      concentActive: 'pool',
      loading: false,
      welcomeStats: [
        { label: '资金归集', value: '-', color: '#1890FF' },
        { label: '融资余额', value: '-', color: '#722ED1' },
        { label: '投资规模', value: '-', color: '#52C41A' },
        { label: '票据规模', value: '-', color: '#FA8C16' },
        { label: '风险指标', value: '-', color: '#FF4D4F' },
      ],
      kpiCards: [
        { label: '资金归集率', value: '-', unit: '%', icon: 'el-icon-coin', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', barBg: 'rgba(24,144,255,0.1)', barColor: '#1890FF', barWidth: 0, trend: 0, theme: '' },
        { label: '融资余额', value: '-', unit: '亿', icon: 'el-icon-s-finance', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', barBg: 'rgba(114,46,209,0.1)', barColor: '#722ED1', barWidth: 0, trend: 0, theme: '' },
        { label: '投资收益率', value: '-', unit: '%', icon: 'el-icon-data-line', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', barBg: 'rgba(82,196,26,0.1)', barColor: '#52C41A', barWidth: 0, trend: 0, theme: '' },
        { label: '票据到期额', value: '-', unit: '亿', icon: 'el-icon-tickets', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', barBg: 'rgba(250,140,22,0.1)', barColor: '#FA8C16', barWidth: 0, trend: 0, theme: 'kpi-warning' },
        { label: '风险评分', value: '-', unit: '分', icon: 'el-icon-warning-outline', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', barBg: 'rgba(19,194,194,0.1)', barColor: '#13C2C2', barWidth: 0, trend: 0, theme: '' },
        { label: '结算完成率', value: '-', unit: '%', icon: 'el-icon-circle-check', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', barBg: 'rgba(255,77,79,0.1)', barColor: '#FF4D4F', barWidth: 0, trend: 0, theme: '' },
      ],
      // ===== 拓扑流程图数据 =====
      flowConfig: { colW: 132, rowH: 96, nodeW: 96, nodeH: 60, padX: 20, padY: 16 },
      flowNodes: [
        // 主干流程（row 2）
        { id: 'plan',     name: '资金计划', col: 0, row: 2, icon: 'el-icon-date',            kind: 'main', route: '/czzjjh/fundPlanningzjjh' },
        { id: 'concent',  name: '资金归集', col: 1, row: 2, icon: 'el-icon-coin',            kind: 'main', route: '/czzjgj/fundConcentrationindex' },
        { id: 'cash',     name: '现金管理', col: 2, row: 2, icon: 'el-icon-wallet',          kind: 'main', route: '/b2cfw/cashManagementzjdb' },
        { id: 'settle',   name: '结算平台', col: 3, row: 2, icon: 'el-icon-s-order',         kind: 'main', route: '/jspt/settlementPlatformindex' },
        { id: 'finance',  name: '融资管理', col: 4, row: 2, icon: 'el-icon-s-finance',       kind: 'main', route: '/xyzgl/financingManagementindex' },
        { id: 'invest',   name: '投资管理', col: 5, row: 2, icon: 'el-icon-data-line',       kind: 'main', route: '/zjjh/investmentManagementtzjh' },
        { id: 'risk',     name: '风险管控', col: 6, row: 2, icon: 'el-icon-warning-outline', kind: 'warn', route: '/qqfxgl/riskManagementfxjk' },
        { id: 'decision', name: '决策支持', col: 7, row: 2, icon: 'el-icon-data-analysis',   kind: 'main', route: '/zjdd/decisionSupport' },
        // 子模块层（row 1）
        { id: 'zjyc',  name: '资金预测',   col: 0.5, row: 1, icon: 'el-icon-data-line',       kind: 'sub', route: '/czzjjh/fundPlanningzjyc' },
        { id: 'gjjh',  name: '归集计划',   col: 1.5, row: 1, icon: 'el-icon-date',            kind: 'sub', route: '/czzjgj/fundConcentrationgjjhgl' },
        { id: 'xjlyc', name: '现金流预测', col: 2.5, row: 1, icon: 'el-icon-data-line',       kind: 'sub', route: '/b2cfw/cashManagementxjlyc' },
        { id: 'pjgl',  name: '票据管理',   col: 3.5, row: 1, icon: 'el-icon-tickets',         kind: 'sub', route: '/syhp/billManagementindex' },
        { id: 'rzjh',  name: '融资计划',   col: 4.5, row: 1, icon: 'el-icon-s-finance',       kind: 'sub', route: '/xyzgl/financingManagementrzjhgl' },
        { id: 'yhlc',  name: '银行理财',   col: 5.5, row: 1, icon: 'el-icon-wallet',          kind: 'sub', route: '/zjjh/investmentManagementyhlc' },
        { id: 'fxpg',  name: '风险评估',   col: 6.5, row: 1, icon: 'el-icon-data-analysis',   kind: 'sub', route: '/qqfxgl/riskManagementfxpg' },
        // 规划层（row 0）
        { id: 'zjmb',  name: '资金目标',   col: 0,   row: 0, icon: 'el-icon-s-flag',          kind: 'config', route: '/czzjjh/fundPlanningzjmb' },
        { id: 'gjcl',  name: '归集策略',   col: 1.5, row: 0, icon: 'el-icon-setting',         kind: 'config', route: '/czzjgj/fundConcentrationgjclpz' },
        { id: 'tzcp',  name: '投资产品',   col: 5,   row: 0, icon: 'el-icon-s-order',         kind: 'config', route: '/zjjh/investmentManagementtzcp' },
        { id: 'jgbs',  name: '监管报送',   col: 7,   row: 0, icon: 'el-icon-document',        kind: 'config', route: '/jgbg/regulatoryReportingbbsj' },
        // 执行层（row 3）
        { id: 'zjzx',  name: '资金执行',   col: 0.5, row: 3, icon: 'el-icon-circle-check',    kind: 'exec', route: '/czzjjh/fundPlanningzjzx' },
        { id: 'gjjk',  name: '归集监控',   col: 1.5, row: 3, icon: 'el-icon-view',            kind: 'exec', route: '/czzjgj/fundConcentrationgjzxjk' },
        { id: 'fkgl',  name: '付款管理',   col: 2.5, row: 3, icon: 'el-icon-bottom',          kind: 'exec', route: '/b2cfw/cashManagementfkgl' },
        { id: 'jsjk',  name: '结算监控',   col: 3.5, row: 3, icon: 'el-icon-view',            kind: 'exec', route: '/jspt/settlementPlatformjsjk' },
        { id: 'rzhk',  name: '融资还款',   col: 4.5, row: 3, icon: 'el-icon-money',           kind: 'exec', route: '/xyzgl/financingManagementrzhkgl' },
        { id: 'tzjk',  name: '投资监控',   col: 5.5, row: 3, icon: 'el-icon-view',            kind: 'exec', route: '/zjjh/investmentManagementtzjk' },
        { id: 'fxsb',  name: '风险识别',   col: 6.5, row: 3, icon: 'el-icon-search',          kind: 'exec', route: '/qqfxgl/riskManagementfxsb' },
        // 基础支撑（row 4）
        { id: 'jcpz',  name: '基础配置', col: 0, row: 4, icon: 'el-icon-setting',       kind: 'config', route: '/xjgl/basicConfigBusinessSystemRegister' },
        { id: 'sjgz',  name: '数据规则', col: 1, row: 4, icon: 'el-icon-s-order',       kind: 'config', route: '/xjgl/BusinessRuleManage' },
        { id: 'jrcp',  name: '金融产品', col: 2, row: 4, icon: 'el-icon-coin',          kind: 'config', route: '/cmr/financialProductDefinitionFinancialProductManage' },
        { id: 'yqzl',  name: '银企直连', col: 3, row: 4, icon: 'el-icon-connection',    kind: 'config', route: '/lsfw/partnerDirectConnectionBankDirectConnectionManage' },
        { id: 'zhgl',  name: '账户管理', col: 4, row: 4, icon: 'el-icon-office-building', kind: 'config', route: '/czzhgl/accountManagementindex' },
        { id: 'ysp',   name: '衍生品',   col: 5, row: 4, icon: 'el-icon-s-mark',        kind: 'config', route: '/yspx/derivativesyspx' },
        { id: 'skgl',  name: '收款管理', col: 6, row: 4, icon: 'el-icon-top',           kind: 'config', route: '/b2cfw/cashManagementskgl' },
        { id: 'yhdz',  name: '银行对账', col: 7, row: 4, icon: 'el-icon-document-checked', kind: 'config', route: '/b2cfw/cashManagementyhdz' },
      ],
      flowEdges: [
        // 主干流程
        ['plan', 'concent', 'main'], ['concent', 'cash', 'main'], ['cash', 'settle', 'main'],
        ['settle', 'finance', 'main'], ['finance', 'invest', 'main'], ['invest', 'risk', 'main'],
        ['risk', 'decision', 'main'],
        // 子模块 → 主干
        ['zjyc', 'plan', 'sub'], ['gjjh', 'concent', 'sub'], ['xjlyc', 'cash', 'sub'],
        ['pjgl', 'settle', 'sub'], ['rzjh', 'finance', 'sub'], ['yhlc', 'invest', 'sub'],
        ['fxpg', 'risk', 'sub'],
        // 规划层 → 子模块
        ['zjmb', 'zjyc', 'config'], ['gjcl', 'gjjh', 'config'],
        ['tzcp', 'yhlc', 'config'], ['jgbs', 'decision', 'config'],
        // 主干 → 执行层
        ['plan', 'zjzx', 'exec'], ['concent', 'gjjk', 'exec'], ['cash', 'fkgl', 'exec'],
        ['settle', 'jsjk', 'exec'], ['finance', 'rzhk', 'exec'], ['invest', 'tzjk', 'exec'],
        ['risk', 'fxsb', 'exec'],
        // 主干 → 基础支撑
        ['settle', 'yqzl', 'config'], ['settle', 'zhgl', 'config'],
        ['cash', 'skgl', 'config'], ['cash', 'yhdz', 'config'],
      ],
      concentPoolItems: [
        { name: '资金池管理', desc: '资金池创建与配置', icon: 'el-icon-coin', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/czzjgj/fundConcentrationindex' },
        { name: '归集计划管理', desc: '资金归集计划编制', icon: 'el-icon-date', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/czzjgj/fundConcentrationgjjhgl' },
        { name: '归集执行监控', desc: '归集执行状态监控', icon: 'el-icon-view', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/czzjgj/fundConcentrationgjzxjk' },
        { name: '归集异常处理', desc: '归集异常扣款处理', icon: 'el-icon-s-order', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/czzjgj/fundConcentrationgjyccl' },
      ],
      concentStatItems: [
        { name: '归集策略配置', desc: '归集规则策略设定', icon: 'el-icon-setting', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/czzjgj/fundConcentrationgjclpz' },
        { name: '归集结果分析', desc: '归集数据统计分析', icon: 'el-icon-data-analysis', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/czzjgj/fundConcentrationgjjgfx' },
        { name: '归集报表统计', desc: '归集业务报表汇总', icon: 'el-icon-document', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/czzjgj/fundConcentrationgjbbtj' },
      ],
      cashItems: [
        { name: '资金调拨', desc: '跨账户资金调拨', icon: 'el-icon-sort', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', badge: '3', route: '/b2cfw/cashManagementzjdb' },
        { name: '付款管理', desc: '对外付款审批', icon: 'el-icon-bottom', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/b2cfw/cashManagementfkgl' },
        { name: '收款管理', desc: '收款确认管理', icon: 'el-icon-top', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/b2cfw/cashManagementskgl' },
        { name: '现金流量预测', desc: '现金流预测分析', icon: 'el-icon-data-line', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/b2cfw/cashManagementxjlyc' },
        { name: '银行对账', desc: '银企对账管理', icon: 'el-icon-document-checked', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/b2cfw/cashManagementyhdz' },
      ],
      cashFlowStats: [
        { label: '流入', value: '-', percent: 0, color: '#52C41A' },
        { label: '流出', value: '-', percent: 0, color: '#FF4D4F' },
        { label: '净流量', value: '-', percent: 0, color: '#1890FF' },
      ],
      planItems: [
        { name: '资金计划', desc: '月度/季度资金计划', icon: 'el-icon-date', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/czzjjh/fundPlanningzjjh' },
        { name: '计划明细', desc: '资金计划明细管理', icon: 'el-icon-s-order', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/czzjjh/fundPlanningzjjhmx' },
        { name: '资金预测', desc: '资金需求预测', icon: 'el-icon-data-line', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/czzjjh/fundPlanningzjyc' },
        { name: '资金目标', desc: '资金管理目标设定', icon: 'el-icon-s-flag', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/czzjjh/fundPlanningzjmb' },
        { name: '资金分析', desc: '资金运行分析报告', icon: 'el-icon-data-analysis', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/czzjjh/fundPlanningzjfx' },
        { name: '资金调整', desc: '计划调整审批', icon: 'el-icon-edit', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/czzjjh/fundPlanningzjtz' },
        { name: '资金执行', desc: '计划执行跟踪', icon: 'el-icon-circle-check', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/czzjjh/fundPlanningzjzx' },
      ],
      financeItems: [
        { name: '融资计划管理', color: '#1890FF', count: '8', route: '/xyzgl/financingManagementrzjhgl' },
        { name: '融资租赁管理', color: '#722ED1', route: '/xyzgl/financingManagementrzzlgl' },
        { name: '融资还款管理', color: '#52C41A', route: '/xyzgl/financingManagementrzhkgl' },
        { name: '融资监控管理', color: '#FA8C16', route: '/xyzgl/financingManagementrzjkgl' },
        { name: '银行贷款管理', color: '#13C2C2', route: '/xyzgl/financingManagementyhdkgl' },
        { name: '债券发行管理', color: '#FF4D4F', route: '/xyzgl/financingManagementzqfxgl' },
      ],
      financeProgress: [
        { label: '融资计划完成率', percent: 0, color: '#1890FF' },
        { label: '还款执行率', percent: 0, color: '#52C41A' },
        { label: '融资成本控制率', percent: 0, color: '#722ED1' },
      ],
      investItems: [
        { name: '投资计划', desc: '投资计划编制审批', icon: 'el-icon-date', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/zjjh/investmentManagementtzjh' },
        { name: '银行理财', desc: '银行理财产品管理', icon: 'el-icon-wallet', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/zjjh/investmentManagementyhlc' },
        { name: '债券投资', desc: '债券投资组合管理', icon: 'el-icon-data-line', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/zjjh/investmentManagementzqtz' },
        { name: '投资产品', desc: '投资产品配置管理', icon: 'el-icon-s-order', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/zjjh/investmentManagementtzcp' },
        { name: '投资监控', desc: '投资收益风险监控', icon: 'el-icon-view', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/zjjh/investmentManagementtzjk' },
      ],
      billItems: [
        { name: '票据管理', icon: 'el-icon-tickets', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/syhp/billManagementindex' },
        { name: '票据登记', icon: 'el-icon-edit', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/syhp/billManagementpzdjgl' },
        { name: '票据到期', icon: 'el-icon-time', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/syhp/billManagementpzdqgl' },
        { name: '票据池', icon: 'el-icon-circle-check', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/syhp/billManagementpzc' },
        { name: '票据贴现', icon: 'el-icon-coin', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/syhp/billManagementpztxgl' },
        { name: '票据风险', icon: 'el-icon-data-analysis', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/syhp/billManagementpzfxgl' },
        { name: '票据背书', icon: 'el-icon-document', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/syhp/billManagementpzbsgl' },
        { name: '票据查询统计', icon: 'el-icon-search', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/syhp/billManagementpzcxtj' },
        { name: '电子票据管理', icon: 'el-icon-paperclip', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/syhp/billManagementdzpzgl' },
      ],
      modules: [
        { name: '资金归集', icon: 'el-icon-coin', desc: '资金池/归集/下发', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', route: '/czzjgj/fundConcentrationindex' },
        { name: '资金计划', icon: 'el-icon-date', desc: '计划/预测/目标', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', route: '/czzjjh/fundPlanningzjjh' },
        { name: '现金管理', icon: 'el-icon-wallet', desc: '调拨/收付/对账', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', route: '/b2cfw/cashManagementzjdb' },
        { name: '融资管理', icon: 'el-icon-s-finance', desc: '计划/租赁/还款', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', route: '/xyzgl/financingManagementindex' },
        { name: '投资管理', icon: 'el-icon-data-line', desc: '理财/债券/监控', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', route: '/zjjh/investmentManagementtzjh' },
        { name: '票据管理', icon: 'el-icon-tickets', desc: '登记/承兑/贴现', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', route: '/syhp/billManagementindex' },
        { name: '风险管理', icon: 'el-icon-warning-outline', desc: '监控/评估/识别', iconBg: 'rgba(235,47,150,0.1)', iconColor: '#EB2F96', route: '/qqfxgl/riskManagementfxjk' },
        { name: '结算平台', icon: 'el-icon-s-order', desc: '结算/策略/监控', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', route: '/jspt/settlementPlatformindex' },
        { name: '账户管理', icon: 'el-icon-office-building', desc: '开户/销户/变更', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', route: '/czzhgl/accountManagementindex' },
        { name: '金融衍生品', icon: 'el-icon-s-mark', desc: '期货/期权/远期', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', route: '/yspx/derivativesyspx' },
        { name: '决策支持', icon: 'el-icon-data-analysis', desc: '分析/决策/看板', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', route: '/zjdd/decisionSupport' },
        { name: '监管报送', icon: 'el-icon-document', desc: '报表/监管报送', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', route: '/jgbg/regulatoryReportingbbsj' },
      ],
      riskItems: [
        { name: '风险监控', icon: 'el-icon-warning-outline', iconColor: '#FF4D4F', route: '/qqfxgl/riskManagementfxjk' },
        { name: '风险评估', icon: 'el-icon-data-analysis', iconColor: '#1890FF', route: '/qqfxgl/riskManagementfxpg' },
        { name: '风险识别', icon: 'el-icon-search', iconColor: '#722ED1', route: '/qqfxgl/riskManagementfxsb' },
        { name: '风险管理', icon: 'el-icon-s-order', iconColor: '#52C41A', route: '/qqfxgl/riskManagementfxgl' },
      ],
      settleItems: [
        { name: '结算监控', icon: 'el-icon-view', route: '/jspt/settlementPlatformjsjk' },
        { name: '结算策略管理', icon: 'el-icon-setting', route: '/jspt/settlementPlatformjsclgl' },
        { name: '对接数据管理', icon: 'el-icon-s-operation', route: '/jspt/settlementPlatformdjssjgl' },
        { name: '异常流程配置', icon: 'el-icon-connection', route: '/jspt/settlementPlatformyqlpz' },
      ],
      accountItems: [
        { name: '账户开户', icon: 'el-icon-plus', route: '/czzhgl/accountManagementAccountOpeningManage' },
        { name: '账户销户', icon: 'el-icon-close', route: '/czzhgl/accountManagementAccountClosingManage' },
        { name: '账户变更', icon: 'el-icon-edit', route: '/czzhgl/accountManagementAccountChangeManage' },
        { name: '账户冻结', icon: 'el-icon-lock', route: '/czzhgl/accountManagementAccountFreezeManage' },
        { name: '直连授权', icon: 'el-icon-connection', route: '/czzhgl/accountManagementDirectConnectAuthManage' },
        { name: 'UKey管理', icon: 'el-icon-key', route: '/czzhgl/accountManagementUKeyManage' },
      ],
      baseConfigItems: [
        { name: '基础配置', icon: 'el-icon-setting', route: '/xjgl/basicConfigBusinessSystemRegister' },
        { name: '数据规则管理', icon: 'el-icon-s-order', route: '/xjgl/BusinessRuleManage' },
        { name: '金融产品定义', icon: 'el-icon-coin', route: '/cmr/financialProductDefinitionFinancialProductManage' },
        { name: '银企直连', icon: 'el-icon-connection', route: '/lsfw/partnerDirectConnectionBankDirectConnectionManage' },
      ],
      decisionItems: [
        { name: '决策支持中心', desc: '司库决策分析看板', icon: 'el-icon-data-analysis', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/zjdd/decisionSupport' },
        { name: '流动性分析', desc: '资金流动性监控', icon: 'el-icon-data-line', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/czzjjh/fundPlanningzjfx' },
      ],
      derivItems: [
        { name: '远期交易', desc: '远期合约交易管理', icon: 'el-icon-time', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/yspx/derivativesyqjy' },
        { name: '期货交易', desc: '期货合约交易管理', icon: 'el-icon-s-mark', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/yspx/qhjy' },
        { name: '期权交易', desc: '期权合约交易管理', icon: 'el-icon-s-operation', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/yspx/derivativesqqjy' },
        { name: '掉期交易', desc: '掉期合约交易管理', icon: 'el-icon-sort', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/yspx/dqjy' },
        { name: '衍生品概览', desc: '衍生品交易总览', icon: 'el-icon-circle-check', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/yspx/derivativesyspx' },
        { name: '衍生品监控', desc: '交易监控与风控', icon: 'el-icon-view', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/yspx/derivativesyspxjk' },
      ],
      reportItems: [
        { name: '监管报送', desc: '监管数据报送管理', icon: 'el-icon-document', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/jgbg/regulatoryReportingbbsj' },
        { name: '报表数据', desc: '报表数据采集管理', icon: 'el-icon-s-order', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/jgbg/regulatoryReportingjgbs' },
      ],
      reportExtras: [
        { name: '账户信息', icon: 'el-icon-office-building', route: '/czzhgl/accountManagementzhxx' },
        { name: '账户限额', icon: 'el-icon-s-order', route: '/czzhgl/accountManagementAccountLimitManage' },
        { name: '账户对账', icon: 'el-icon-document-checked', route: '/czzhgl/accountManagementAccountCheckManage' },
        { name: '消息中心', icon: 'el-icon-bell', route: '/home/xxzx' },
      ],
    }
  },
  computed: {
    greetingWord() {
      const h = new Date().getHours()
      if (h < 6) return '凌晨好'
      if (h < 12) return '上午好'
      if (h < 14) return '中午好'
      if (h < 18) return '下午好'
      return '晚上好'
    },
    nodeMap() {
      const m = {}
      this.flowNodes.forEach(n => { m[n.id] = n })
      return m
    },
    canvasW() {
      const maxCol = Math.max(...this.flowNodes.map(n => n.col))
      return Math.ceil((maxCol + 1) * this.flowConfig.colW + this.flowConfig.padX * 2)
    },
    canvasH() {
      const maxRow = Math.max(...this.flowNodes.map(n => n.row))
      return Math.ceil((maxRow + 1) * this.flowConfig.rowH + this.flowConfig.padY * 2)
    },
    edgePaths() {
      const { colW, rowH, nodeW, nodeH, padX, padY } = this.flowConfig
      const center = (n) => ({
        x: padX + (n.col + 0.5) * colW,
        y: padY + (n.row + 0.5) * rowH,
      })
      const markerMap = {
        main: 'url(#fl-arrow-main)',
        sub: 'url(#fl-arrow-sub)',
        exec: 'url(#fl-arrow-exec)',
        config: 'url(#fl-arrow)',
      }
      return this.flowEdges.map(([from, to, kind]) => {
        const a = this.nodeMap[from], b = this.nodeMap[to]
        if (!a || !b) return { d: '', cls: '', marker: '' }
        const ac = center(a), bc = center(b)
        let sx, sy, tx, ty, d
        if (a.row === b.row) {
          const dir = bc.x > ac.x ? 1 : -1
          sx = ac.x + dir * (nodeW / 2 + 2); sy = ac.y
          tx = bc.x - dir * (nodeW / 2 + 2); ty = bc.y
          d = `M${sx},${sy} L${tx},${ty}`
        } else if (a.col === b.col) {
          const dir = bc.y > ac.y ? 1 : -1
          sx = ac.x; sy = ac.y + dir * (nodeH / 2 + 2)
          tx = bc.x; ty = bc.y - dir * (nodeH / 2 + 2)
          d = `M${sx},${sy} L${tx},${ty}`
        } else {
          const dirY = bc.y > ac.y ? 1 : -1
          const dirX = bc.x > ac.x ? 1 : -1
          sx = ac.x; sy = ac.y + dirY * (nodeH / 2 + 2)
          tx = bc.x - dirX * (nodeW / 2 + 2); ty = bc.y
          d = `M${sx},${sy} L${sx},${ty} L${tx},${ty}`
        }
        return { d, cls: 'edge-' + kind, marker: markerMap[kind] || markerMap.config }
      })
    },
  },
  watch: {
    ipBright: { immediate: true, handler() { this._patchPrimaryColors() } },
  },
  created() {
    try {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      this.userName = userInfo ? userInfo.realname || userInfo.username || '' : ''
    } catch (e) { this.userName = '' }
    this._patchPrimaryColors()
    this.fetchAllData()
  },
  methods: {
    jumpTo(route) { if (route) this.$router.push(route) },
    nodePos(n) {
      const { colW, rowH, nodeW, nodeH, padX, padY } = this.flowConfig
      const left = padX + n.col * colW + (colW - nodeW) / 2
      const top = padY + n.row * rowH + (rowH - nodeH) / 2
      return { left: left + 'px', top: top + 'px', width: nodeW + 'px', height: nodeH + 'px' }
    },
    /** 加载首页所有数据 */
    async fetchAllData() {
      this.loading = true
      try {
        await Promise.all([
          this.fetchOverview(),
          this.fetchCashFlow(),
          this.fetchFinanceProgress(),
        ])
      } catch (e) {
        console.error('首页数据加载异常', e)
      } finally {
        this.loading = false
      }
    },
    /** 获取首页聚合数据 */
    async fetchOverview() {
      try {
        const res = await getTreasurerSmartHomeData({})
        if (res && res.codes === '1') {
          const kpi = res.kpi || {}
          const welcome = res.welcome || {}
          // 更新欢迎区
          this.$set(this.welcomeStats, 0, { ...this.welcomeStats[0], value: this.formatBillion(welcome.fundConcentration) })
          this.$set(this.welcomeStats, 1, { ...this.welcomeStats[1], value: this.formatBillion(welcome.financingBalance) })
          this.$set(this.welcomeStats, 2, { ...this.welcomeStats[2], value: this.formatBillion(welcome.investScale) })
          this.$set(this.welcomeStats, 3, { ...this.welcomeStats[3], value: this.formatBillion(welcome.billScale) })
          this.$set(this.welcomeStats, 4, { ...this.welcomeStats[4], value: welcome.riskScore != null ? String(welcome.riskScore) : '-' })
          // 更新KPI
          this.$set(this.kpiCards, 0, { ...this.kpiCards[0], value: this.fmtKpi(kpi.fundConcentrationRate), barWidth: Math.min(kpi.fundConcentrationRate || 0, 100) })
          this.$set(this.kpiCards, 1, { ...this.kpiCards[1], value: this.fmtKpi(kpi.financingBalance), barWidth: Math.min((kpi.financingBalance || 0) * 3, 100) })
          this.$set(this.kpiCards, 2, { ...this.kpiCards[2], value: this.fmtKpi(kpi.investReturnRate), barWidth: Math.min((kpi.investReturnRate || 0) * 10, 100) })
          this.$set(this.kpiCards, 3, { ...this.kpiCards[3], value: this.fmtKpi(kpi.billMaturityAmount), barWidth: Math.min((kpi.billMaturityAmount || 0) * 5, 100) })
          this.$set(this.kpiCards, 4, { ...this.kpiCards[4], value: this.fmtKpi(kpi.riskScore), barWidth: Math.min(kpi.riskScore || 0, 100) })
          this.$set(this.kpiCards, 5, { ...this.kpiCards[5], value: this.fmtKpi(kpi.settlementRate), barWidth: Math.min(kpi.settlementRate || 0, 100) })
        }
      } catch (e) {
        console.error('获取首页聚合数据失败', e)
      }
    },
    /** 获取现金流统计 */
    async fetchCashFlow() {
      try {
        const res = await getCashFlowStats({})
        if (res && res.codes === '1') {
          const inflow = res.cashInflow
          const outflow = res.cashOutflow
          const net = res.netCashflow
          if (inflow == null && outflow == null) return // 无数据不更新
          const inVal = Number(inflow) || 0
          const outVal = Number(outflow) || 0
          const netVal = Number(net) || 0
          const maxVal = Math.max(inVal, outVal, 1)
          this.$set(this.cashFlowStats, 0, { ...this.cashFlowStats[0], value: inVal + '亿', percent: Math.round(inVal * 100 / maxVal) })
          this.$set(this.cashFlowStats, 1, { ...this.cashFlowStats[1], value: outVal + '亿', percent: Math.round(outVal * 100 / maxVal) })
          this.$set(this.cashFlowStats, 2, { ...this.cashFlowStats[2], value: (netVal >= 0 ? '+' : '') + netVal + '亿', percent: Math.round(Math.abs(netVal) * 100 / maxVal) })
        }
      } catch (e) {
        console.error('获取现金流统计失败', e)
      }
    },
    /** 获取融资进度 */
    async fetchFinanceProgress() {
      try {
        const res = await getFinanceProgress({})
        if (res && res.codes === '1') {
          if (res.planRate != null) this.$set(this.financeProgress, 0, { ...this.financeProgress[0], percent: Number(res.planRate) || 0 })
          if (res.repayRate != null) this.$set(this.financeProgress, 1, { ...this.financeProgress[1], percent: Number(res.repayRate) || 0 })
          if (res.costRate != null) this.$set(this.financeProgress, 2, { ...this.financeProgress[2], percent: Number(res.costRate) || 0 })
        }
      } catch (e) {
        console.error('获取融资进度失败', e)
      }
    },
    /** 格式化KPI值，null显示- */
    fmtKpi(val) {
      if (val == null) return '-'
      return String(val)
    },
    /** 格式化为亿元显示 */
    formatBillion(val) {
      if (val == null) return '-'
      const num = Number(val)
      if (num === 0) return '0'
      return num.toFixed(1) + '亿'
    },
    _patchPrimaryColors() {
      const bright = this.ipBright || '#1890FF'
      const rgb = this.ipPrimaryRgb || '24,144,255'
      const brightBg = `rgba(${rgb},0.1)`
      const brightBg2 = `rgba(${rgb},0.08)`
      if (this.welcomeStats && this.welcomeStats[0]) this.welcomeStats[0].color = bright
      if (this.kpiCards && this.kpiCards[0]) { this.kpiCards[0].iconBg = brightBg; this.kpiCards[0].iconColor = bright; this.kpiCards[0].barBg = brightBg; this.kpiCards[0].barColor = bright }
      if (this.modules && this.modules[0]) { this.modules[0].iconBg = brightBg; this.modules[0].iconColor = bright }
      if (this.decisionItems && this.decisionItems[0]) { this.decisionItems[0].bgColor = brightBg2; this.decisionItems[0].iconColor = bright }
    },
  },
}
</script>

<style lang="scss" scoped>
.smart-treasurer-home { padding: 16px; background: #f0f2f5; min-height: 100vh; }
.welcome-section { position: relative; border-radius: 12px; overflow: hidden; margin-bottom: 16px; }
.welcome-bg { position: absolute; top: 0; left: 0; right: 0; bottom: 0; background: linear-gradient(135deg, var(--ip-primary) 0%, var(--ip-secondary) 50%, var(--ip-bright) 100%); z-index: 0; }
.welcome-content { position: relative; z-index: 1; padding: 28px 32px; display: flex; justify-content: space-between; align-items: center; color: #fff; }
.welcome-left { flex: 1; }
.greeting { display: flex; align-items: center; gap: 6px; margin-bottom: 8px; .greeting-icon { font-size: 20px; color: #FAAD14; } .greeting-text { font-size: 14px; color: rgba(255,255,255,0.7); } }
.welcome-title { margin: 0 0 8px; font-size: 26px; font-weight: 700; letter-spacing: 2px; background: linear-gradient(90deg, #fff, var(--ip-light-bg, #B2D4FF)); -webkit-background-clip: text; -webkit-text-fill-color: transparent; }
.welcome-desc { font-size: 14px; color: rgba(255,255,255,0.6); margin-bottom: 18px; }
.welcome-actions { display: flex; gap: 10px; }
.welcome-stats { display: flex; gap: 24px; }
.ws-item { text-align: center; .ws-value { font-size: 22px; font-weight: 700; } .ws-label { font-size: 12px; color: rgba(255,255,255,0.6); margin-top: 4px; } }

.kpi-section { display: grid; grid-template-columns: repeat(6, 1fr); gap: 14px; margin-bottom: 16px; }
.kpi-card { background: #fff; border-radius: 10px; padding: 16px; box-shadow: 0 1px 6px rgba(0,0,0,.06); transition: all 0.2s;
  &:hover { transform: translateY(-2px); box-shadow: 0 6px 16px rgba(0,0,0,.1); }
  &.kpi-warning { border-top: 3px solid #FA8C16; } &.kpi-danger { border-top: 3px solid #FF4D4F; }
  .kpi-top { display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px; }
  .kpi-icon-wrap { width: 36px; height: 36px; border-radius: 8px; display: flex; align-items: center; justify-content: center; i { font-size: 18px; } }
  .kpi-trend { font-size: 12px; &.trend-up { color: #52C41A; } &.trend-down { color: #FF4D4F; } }
  .kpi-value { font-size: 24px; font-weight: 700; color: var(--ip-primary); } .kpi-unit { font-size: 13px; font-weight: 400; margin-left: 2px; color: #999; }
  .kpi-label { font-size: 12px; color: #888; margin: 4px 0 8px; } .kpi-bar { height: 4px; border-radius: 2px; overflow: hidden; }
  .kpi-bar-fill { height: 100%; border-radius: 2px; transition: width 0.6s ease; }
}

.lifecycle-section { background: #fff; border-radius: 10px; padding: 18px 20px; margin-bottom: 16px; box-shadow: 0 1px 6px rgba(0,0,0,.06); }
.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px; }
.section-title { font-size: 14px; font-weight: 600; color: var(--ip-primary); i { margin-right: 6px; } }
.section-legend { font-size: 12px; color: #888; span { margin-left: 14px; } .dot { display: inline-block; width: 8px; height: 8px; border-radius: 50%; margin-right: 4px; vertical-align: middle; &.blue { background: var(--ip-primary, #1890FF); } &.green { background: #52C41A; } &.orange { background: #FA8C16; } &.red { background: #FF4D4F; } &.gray { background: #bbb; } } }

.flow-canvas-wrap {
  width: 100%;
  overflow-x: auto;
  overflow-y: hidden;
  padding: 6px;
  background:
    linear-gradient(90deg, rgba(24,144,255,0.04) 1px, transparent 1px) 0 0 / 24px 24px,
    linear-gradient(0deg, rgba(24,144,255,0.04) 1px, transparent 1px) 0 0 / 24px 24px,
    #fafbff;
  border-radius: 8px;
  border: 1px solid #f0f2f5;
}
.flow-canvas { position: relative; margin: 0 auto; }
.flow-svg { position: absolute; top: 0; left: 0; pointer-events: none; z-index: 1; }

.fl-edge {
  stroke-width: 1.6;
  fill: none;
  &.edge-main { stroke: var(--ip-primary, #1890FF); stroke-width: 2.2; }
  &.edge-sub { stroke: #52C41A; stroke-dasharray: 4 3; }
  &.edge-exec { stroke: #FA8C16; stroke-dasharray: 4 3; }
  &.edge-config { stroke: #b8b8b8; stroke-dasharray: 4 3; }
}

.flow-pos-node {
  position: absolute;
  z-index: 2;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #fff;
  border: 1.5px solid #e8e8e8;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.2s;
  box-shadow: 0 1px 4px rgba(0,0,0,.04);
  &:hover { transform: translateY(-2px) scale(1.05); box-shadow: 0 6px 18px rgba(0,0,0,.12); z-index: 3; }
  .fpn-icon-wrap { width: 26px; height: 26px; border-radius: 6px; display: flex; align-items: center; justify-content: center; margin-bottom: 2px; i { font-size: 14px; } }
  .fpn-label { font-size: 11px; font-weight: 600; color: #333; white-space: nowrap; line-height: 1.2; }
  &.fpn-main {
    border-color: var(--ip-primary, #1890FF); border-width: 2px; background: #fff;
    .fpn-icon-wrap { background: rgba(var(--ip-primary-rgb,24,144,255),0.12); }
    .fpn-icon-wrap i { color: var(--ip-primary, #1890FF); }
    .fpn-label { color: var(--ip-primary, #1890FF); }
  }
  &.fpn-sub {
    border-color: #52C41A; background: #f6ffed;
    .fpn-icon-wrap { background: rgba(82,196,26,0.12); }
    .fpn-icon-wrap i { color: #52C41A; }
    .fpn-label { color: #389e0d; }
  }
  &.fpn-warn {
    border-color: #FA8C16; border-width: 2px; background: #FFFBE6;
    .fpn-icon-wrap { background: rgba(250,140,22,0.15); }
    .fpn-icon-wrap i { color: #FA8C16; }
    .fpn-label { color: #FA8C16; }
  }
  &.fpn-exec {
    border-color: #FA8C16; background: #fff7e6;
    .fpn-icon-wrap { background: rgba(250,140,22,0.12); }
    .fpn-icon-wrap i { color: #FA8C16; }
    .fpn-label { color: #d46b08; }
  }
  &.fpn-config {
    border-color: #e8e8e8; background: #fafafa; border-style: dashed;
    .fpn-icon-wrap { background: #f0f0f0; }
    .fpn-icon-wrap i { color: #888; }
    .fpn-label { color: #666; font-weight: 500; }
  }
}

@keyframes pulse { 0% { box-shadow: 0 0 0 0 rgba(255,77,79,0.5); } 70% { box-shadow: 0 0 0 6px rgba(255,77,79,0); } 100% { box-shadow: 0 0 0 0 rgba(255,77,79,0); } }

.content-row { margin-bottom: 16px; }
.card-panel { background: #fff; border-radius: 10px; padding: 16px 18px; box-shadow: 0 1px 6px rgba(0,0,0,.06); height: 100%; }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; padding-bottom: 10px; border-bottom: 1px solid #f0f0f0; }
.card-title { font-size: 15px; font-weight: 600; color: var(--ip-primary); i { margin-right: 6px; } }
.card-more { font-size: 12px; color: var(--ip-bright); cursor: pointer; &:hover { opacity: 0.8; } }

.inner-tabs { ::v-deep .el-tabs__header { margin-bottom: 8px; } ::v-deep .el-tabs__item { font-size: 13px; padding: 0 12px; } }
.concent-list { display: flex; flex-direction: column; gap: 6px; }
.concent-entry { display: flex; align-items: center; gap: 10px; padding: 8px 10px; border-radius: 8px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.15s;
  &:hover { border-color: var(--ip-primary); background: var(--ip-light-bg, #fafbff); }
  .ce-icon { width: 30px; height: 30px; border-radius: 6px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 14px; } }
  .ce-body { flex: 1; .ce-name { font-size: 13px; font-weight: 500; color: #333; } .ce-desc { font-size: 11px; color: #999; } }
  .ce-arrow { font-size: 12px; color: #d9d9d9; flex-shrink: 0; }
}

.cash-grid { display: flex; flex-direction: column; gap: 6px; margin-bottom: 12px; }
.cash-card { display: flex; align-items: center; gap: 10px; padding: 8px 10px; border-radius: 8px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.15s;
  &:hover { border-color: var(--ip-primary); background: var(--ip-light-bg, #fafbff); }
  .cc-icon { width: 30px; height: 30px; border-radius: 6px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 14px; } }
  .cc-body { flex: 1; .cc-name { font-size: 13px; font-weight: 500; color: #333; } .cc-desc { font-size: 11px; color: #999; } }
  .cc-badge { ::v-deep .el-badge__content { top: 4px; } }
}
.cash-flow { margin-top: 4px; }
.cf-row { display: flex; align-items: center; gap: 8px; margin-bottom: 6px; .cf-label { font-size: 12px; color: #888; width: 50px; flex-shrink: 0; } .cf-bar-bg { flex: 1; height: 6px; background: #f5f5f5; border-radius: 3px; overflow: hidden; } .cf-bar-fill { height: 100%; border-radius: 3px; } .cf-val { font-size: 12px; font-weight: 600; color: #333; width: 56px; text-align: right; } }

.plan-list { display: flex; flex-direction: column; gap: 6px; }
.plan-entry { display: flex; align-items: center; gap: 10px; padding: 7px 10px; border-radius: 8px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.15s;
  &:hover { border-color: var(--ip-primary); background: var(--ip-light-bg, #fafbff); }
  .pe-icon { width: 28px; height: 28px; border-radius: 6px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 13px; } }
  .pe-info { flex: 1; .pe-name { font-size: 13px; font-weight: 500; color: #333; } .pe-desc { font-size: 11px; color: #999; } }
  .pe-arrow { font-size: 12px; color: #d9d9d9; flex-shrink: 0; }
}

.finance-list { display: flex; flex-direction: column; gap: 6px; margin-bottom: 12px; }
.finance-entry { display: flex; align-items: center; gap: 8px; padding: 6px 10px; border-radius: 8px; cursor: pointer; transition: background 0.15s;
  &:hover { background: #f5f7fa; }
  .fe-dot { width: 8px; height: 8px; border-radius: 50%; flex-shrink: 0; } .fe-name { flex: 1; font-size: 13px; color: #333; font-weight: 500; } .fe-count { font-size: 14px; font-weight: 700; }
}
.finance-progress { margin-top: 4px; }
.fp-row { display: flex; align-items: center; gap: 8px; margin-bottom: 6px; .fp-label { font-size: 12px; color: #888; width: 100px; flex-shrink: 0; } .fp-bar-bg { flex: 1; height: 6px; background: #f5f5f5; border-radius: 3px; overflow: hidden; } .fp-bar-fill { height: 100%; border-radius: 3px; } .fp-val { font-size: 12px; font-weight: 600; color: #333; width: 40px; text-align: right; } }

.invest-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 8px; }
.invest-card { display: flex; align-items: center; gap: 8px; padding: 10px; border-radius: 8px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.15s;
  &:hover { border-color: var(--ip-primary); }
  .iv-icon { width: 28px; height: 28px; border-radius: 6px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 13px; } }
  .iv-body { .iv-name { font-size: 12px; font-weight: 500; color: #333; } .iv-desc { font-size: 10px; color: #999; } }
}

.bill-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 8px; }
.bill-card { display: flex; flex-direction: column; align-items: center; padding: 12px 6px; border-radius: 8px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.2s; text-align: center;
  &:hover { border-color: var(--ip-primary); transform: translateY(-2px); box-shadow: 0 4px 12px rgba(var(--ip-primary-rgb),0.1); }
  .bl-icon { width: 32px; height: 32px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-bottom: 4px; i { font-size: 16px; } }
  .bl-name { font-size: 11px; font-weight: 500; color: #333; }
}

.module-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px; }
.module-card { display: flex; flex-direction: column; align-items: center; justify-content: center; padding: 14px 6px; border-radius: 10px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.2s; text-align: center;
  &:hover { border-color: var(--ip-primary); transform: translateY(-2px); box-shadow: 0 6px 16px rgba(var(--ip-primary-rgb),0.1); }
  .mc-icon { width: 36px; height: 36px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-bottom: 6px; i { font-size: 18px; } }
  .mc-name { font-size: 12px; font-weight: 600; color: #333; } .mc-desc { font-size: 10px; color: #bbb; margin-top: 2px; }
}

.risk-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 10px; }
.rk-section { padding: 10px; border-radius: 8px; border: 1px solid #f0f0f0; }
.rk-title { font-size: 12px; font-weight: 600; color: var(--ip-primary); margin-bottom: 8px; padding-bottom: 4px; border-bottom: 1px solid #f0f0f0; }
.rk-item { display: flex; align-items: center; gap: 6px; padding: 5px 4px; font-size: 12px; cursor: pointer; transition: color 0.15s; &:hover { color: var(--ip-primary); } i { font-size: 12px; color: #888; } span { color: #333; } }

.ai-panel { background: linear-gradient(180deg, var(--ip-light-bg, #f0f5ff) 0%, #fff 100%); }
.decision-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 10px; }
.decision-card { display: flex; align-items: center; gap: 10px; padding: 12px; border-radius: 10px; border: 1px solid #e8e8e8; cursor: pointer; transition: all 0.2s;
  &:hover { border-color: var(--ip-bright); box-shadow: 0 4px 12px rgba(var(--ip-primary-rgb),0.12); transform: translateY(-2px); }
  .dc-icon { width: 38px; height: 38px; border-radius: 10px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 18px; } }
  .dc-body { flex: 1; min-width: 0; .dc-name { font-size: 13px; font-weight: 600; color: #333; } .dc-desc { font-size: 11px; color: #999; margin-top: 2px; } }
  .dc-arrow { font-size: 14px; color: #d9d9d9; flex-shrink: 0; }
}

.deriv-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 8px; }
.deriv-card { display: flex; align-items: center; gap: 8px; padding: 10px; border-radius: 8px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.15s;
  &:hover { border-color: var(--ip-primary); }
  .dv-icon { width: 28px; height: 28px; border-radius: 6px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 13px; } }
  .dv-body { .dv-name { font-size: 12px; font-weight: 500; color: #333; } .dv-desc { font-size: 10px; color: #999; } }
}

.report-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 10px; margin-bottom: 12px; }
.report-card { display: flex; align-items: center; gap: 8px; padding: 12px; border-radius: 10px; border: 1px solid #e8e8e8; cursor: pointer; transition: all 0.2s;
  &:hover { border-color: var(--ip-bright); box-shadow: 0 4px 12px rgba(var(--ip-primary-rgb),0.12); }
  .rp-icon { width: 32px; height: 32px; border-radius: 8px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 16px; } }
  .rp-body { .rp-name { font-size: 13px; font-weight: 600; color: #333; } .rp-desc { font-size: 11px; color: #999; margin-top: 2px; } }
}
.report-extras { display: flex; gap: 8px; flex-wrap: wrap; }
.re-item { display: flex; align-items: center; gap: 4px; padding: 6px 10px; border-radius: 8px; background: #fafafa; cursor: pointer; transition: all 0.15s; font-size: 12px; &:hover { background: var(--ip-light-bg, #f0f5ff); } i { font-size: 12px; color: var(--ip-primary); } span { color: #333; } }
</style>