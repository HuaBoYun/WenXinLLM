<template>
  <div class="smart-rectify-home" :style="themeVars">
    <!-- ========== 顶部欢迎区 ========== -->
    <div class="welcome-section">
      <div class="welcome-bg"></div>
      <div class="welcome-content">
        <div class="welcome-left">
          <div class="greeting">
            <span class="greeting-icon"><i class="el-icon-sunny" /></span>
            <span class="greeting-text">{{ greetingWord }}，{{ userName }}</span>
          </div>
          <h1 class="welcome-title">整改追责监管平台</h1>
          <p class="welcome-desc">问题汇总 · 整改分派 · 整改跟踪 · 违规追责 · 闭环管理 · 智能监督</p>
          <div class="welcome-actions">
            <el-button type="primary" icon="el-icon-warning-outline" round @click="$router.push('/prball/auditIssuesCollect')">问题汇总</el-button>
            <el-button icon="el-icon-circle-check" round @click="$router.push('/rectify/assign')">整改分派</el-button>
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

    <!-- ========== 整改追责生命周期链路图（拓扑图） ========== -->
    <div class="lifecycle-section">
      <div class="section-header">
        <div class="section-title"><i class="el-icon-share" /> 整改追责生命周期链路</div>
        <div class="section-legend">
          <span><i class="dot blue" />主流程</span>
          <span><i class="dot orange" />关注</span>
          <span><i class="dot red" />违规追责</span>
          <span><i class="dot purple" />台账归档</span>
          <span><i class="dot gray" />基础支撑</span>
        </div>
      </div>
      <div class="flow-canvas-wrap">
        <div class="flow-canvas" :style="{ width: rcCanvasW + 'px', height: rcCanvasH + 'px' }">
          <svg class="flow-svg" :width="rcCanvasW" :height="rcCanvasH">
            <defs>
              <marker id="rc-arrow" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="7" markerHeight="7" orient="auto">
                <path d="M0,0 L10,5 L0,10 z" fill="#bbb" />
              </marker>
              <marker id="rc-arrow-main" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="8" markerHeight="8" orient="auto">
                <path d="M0,0 L10,5 L0,10 z" fill="#1890FF" />
              </marker>
              <marker id="rc-arrow-legal" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="7" markerHeight="7" orient="auto">
                <path d="M0,0 L10,5 L0,10 z" fill="#FF4D4F" />
              </marker>
              <marker id="rc-arrow-archive" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="7" markerHeight="7" orient="auto">
                <path d="M0,0 L10,5 L0,10 z" fill="#722ED1" />
              </marker>
              <marker id="rc-arrow-warn" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="7" markerHeight="7" orient="auto">
                <path d="M0,0 L10,5 L0,10 z" fill="#FA8C16" />
              </marker>
            </defs>
            <path v-for="(e, idx) in rcEdgePaths" :key="'re'+idx" :d="e.d" :class="['rc-edge', e.cls]" :marker-end="e.marker" fill="none" />
          </svg>
          <div v-for="n in rcFlowNodes" :key="n.id"
               class="flow-pos-node"
               :class="['fpn-'+n.kind, n.alert ? 'fpn-alert' : '']"
               :style="rcNodePos(n)"
               @click="jumpTo(n.route)">
            <div class="fpn-icon-wrap"><i :class="n.icon" /></div>
            <div class="fpn-label">{{ n.name }}</div>
            <div class="fpn-dot" v-if="n.alert"></div>
            <div class="fpn-badge" v-if="n.badge">{{ n.badge }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- ========== 中部：整改跟踪 + 违规追责 + 问题汇总 ========== -->
    <el-row :gutter="14" class="content-row">
      <!-- 整改跟踪 -->
      <el-col :span="10">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-circle-check" /> 整改跟踪管理</span>
            <span class="card-more" @click="jumpTo('/rectify/standingbook')">整改台账 →</span>
          </div>
          <el-tabs v-model="rectifyActive" class="inner-tabs">
            <el-tab-pane name="track">
              <span slot="label">整改进度</span>
              <div class="rectify-list">
                <div v-for="item in rectifyTrackItems" :key="item.name" class="rectify-entry" @click="jumpTo(item.route)">
                  <div class="re-dot" :style="{background: item.color}"></div>
                  <div class="re-body">
                    <span class="re-name">{{ item.name }}</span>
                    <span class="re-desc">{{ item.desc }}</span>
                  </div>
                  <span class="re-count" v-if="item.count" :style="{color: item.color}">{{ item.count }}项</span>
                  <i class="el-icon-arrow-right re-arrow" />
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane name="history">
              <span slot="label">历史整改</span>
              <div class="rectify-list">
                <div v-for="item in rectifyHistItems" :key="item.name" class="rectify-entry" @click="jumpTo(item.route)">
                  <div class="re-icon" :style="{background: item.bgColor}">
                    <i :class="item.icon" :style="{color: item.iconColor}" />
                  </div>
                  <div class="re-body">
                    <span class="re-name">{{ item.name }}</span>
                    <span class="re-desc">{{ item.desc }}</span>
                  </div>
                  <i class="el-icon-arrow-right re-arrow" />
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
          <div class="rectify-progress">
            <div class="rp-row" v-for="rp in rectifyProgress" :key="rp.label">
              <span class="rp-label">{{ rp.label }}</span>
              <div class="rp-bar-bg"><div class="rp-bar-fill" :style="{width: rp.percent + '%', background: rp.color}" /></div>
              <span class="rp-val">{{ rp.percent }}%</span>
            </div>
          </div>
        </div>
      </el-col>

      <!-- 违规追责 -->
      <el-col :span="7">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-s-claim" /> 违规追责</span>
            <span class="card-more" @click="jumpTo('/wgzz/zz')">追责受理 →</span>
          </div>
          <div class="wgzz-list">
            <div v-for="item in wgzzItems" :key="item.name" class="wgzz-entry" @click="jumpTo(item.route)">
              <div class="wz-icon" :style="{background: item.bgColor}">
                <i :class="item.icon" :style="{color: item.iconColor}" />
              </div>
              <div class="wz-body">
                <div class="wz-name">{{ item.name }}</div>
                <div class="wz-desc">{{ item.desc }}</div>
              </div>
              <el-badge v-if="item.badge" :value="item.badge" class="wz-badge" />
            </div>
          </div>
          <div class="wgzz-stat">
            <div class="ws-stat-row" v-for="ws in wgzzStats" :key="ws.label">
              <span class="wss-label">{{ ws.label }}</span>
              <div class="wss-bar-bg"><div class="wss-bar-fill" :style="{width: ws.percent + '%', background: ws.color}" /></div>
              <span class="wss-val">{{ ws.value }}</span>
            </div>
          </div>
        </div>
      </el-col>

      <!-- 问题汇总 -->
      <el-col :span="7">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-warning-outline" /> 问题汇总</span>
            <span class="card-more" @click="jumpTo('/prball/auditIssuesCollect')">审计汇总 →</span>
          </div>
          <div class="collect-list">
            <div v-for="item in collectItems" :key="item.name" class="collect-entry" @click="jumpTo(item.route)">
              <div class="cl-icon" :style="{background: item.bgColor}">
                <i :class="item.icon" :style="{color: item.iconColor}" />
              </div>
              <div class="cl-body">
                <div class="cl-name">{{ item.name }}</div>
                <div class="cl-desc">{{ item.desc }}</div>
              </div>
              <el-tag v-if="item.tag" :type="item.tagType" size="mini" class="cl-tag">{{ item.tag }}</el-tag>
              <i class="el-icon-arrow-right cl-arrow" />
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- ========== 实时报告 + 统计分析 ========== -->
    <el-row :gutter="14" class="content-row">
      <el-col :span="12">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-data-line" /> 实时报告与线索</span>
            <span class="card-more" @click="jumpTo('/rectify/report')">整改报告 →</span>
          </div>
          <div class="report-list">
            <div v-for="r in reportItems" :key="r.name" class="report-entry" @click="jumpTo(r.route)">
              <div class="rt-icon" :style="{background: r.bgColor}">
                <i :class="r.icon" :style="{color: r.iconColor}" />
              </div>
              <div class="rt-body">
                <div class="rt-name">{{ r.name }}</div>
                <div class="rt-desc">{{ r.desc }}</div>
              </div>
              <i class="el-icon-arrow-right rt-arrow" />
            </div>
          </div>
        </div>
      </el-col>

      <el-col :span="12">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-data-analysis" /> 统计分析</span>
            <span class="card-more" @click="jumpTo('/home/index')">总览 →</span>
          </div>
          <div class="stats-grid">
            <div v-for="st in statsTemplates" :key="st.name" class="stats-card" @click="jumpTo(st.route)">
              <div class="stc-icon" :style="{background: st.bgColor}">
                <i :class="st.icon" :style="{color: st.iconColor}" />
              </div>
              <div class="stc-name">{{ st.name }}</div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- ========== 功能模块 + 整改台账与配置 ========== -->
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
            <span class="card-title"><i class="el-icon-s-order" /> 台账管理与整改报告</span>
          </div>
          <div class="ledger-grid">
            <div class="lg-section">
              <div class="lg-title">整改台账</div>
              <div v-for="l in ledgerItems" :key="l.name" class="lg-item" @click="jumpTo(l.route)">
                <i :class="l.icon" :style="{color: l.iconColor}" /><span>{{ l.name }}</span>
              </div>
            </div>
            <div class="lg-section">
              <div class="lg-title">整改报告</div>
              <div v-for="r in rectifyReportItems" :key="r.name" class="lg-item" @click="jumpTo(r.route)">
                <i :class="r.icon" :style="{color: r.iconColor}" /><span>{{ r.name }}</span>
              </div>
            </div>
            <div class="lg-section">
              <div class="lg-title">违规追责流程</div>
              <div v-for="w in wgzzFlowItems" :key="w.name" class="lg-item" @click="jumpTo(w.route)">
                <i :class="w.icon" :style="{color: w.iconColor}" /><span>{{ w.name }}</span>
              </div>
            </div>
            <div class="lg-section">
              <div class="lg-title">快捷入口</div>
              <div v-for="q in quickItems" :key="q.name" class="lg-item" @click="jumpTo(q.route)">
                <i :class="q.icon" :style="{color: q.iconColor}" /><span>{{ q.name }}</span>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { investThemeMixin } from '@/views/stateAssets/themeMixin'
import { getRectifyHomeOverview } from '@/api/audit/smartHome'

export default {
  name: 'SmartRectifyHome',
  mixins: [investThemeMixin],
  data() {
    return {
      userName: '',
      rectifyActive: 'track',
      // ===== 整改追责生命周期拓扑图配置 =====
      rcFlowConfig: { colW: 128, rowH: 92, nodeW: 92, nodeH: 58, padX: 20, padY: 16 },
      rcFlowNodes: [
        // ===== 主干（row 2）：整改追踪主流程 =====
        { id: 'assign',    name: '整改分派',   col: 0, row: 2, icon: 'el-icon-s-order',           kind: 'main',    route: '/rectify/assign' },
        { id: 'scheme',    name: '整改通知',   col: 1, row: 2, icon: 'el-icon-document',           kind: 'main',    route: '/rectify/scheme' },
        { id: 'practicable', name: '整改落实', col: 2, row: 2, icon: 'el-icon-s-check',            kind: 'warn',    route: '/rectify/practicable', alert: true },
        { id: 'track',     name: '整改跟踪',   col: 3, row: 2, icon: 'el-icon-view',               kind: 'main',    route: '/rectify/track' },
        { id: 'valuation', name: '整改评价',   col: 4, row: 2, icon: 'el-icon-star-off',           kind: 'main',    route: '/rectify/valuation' },
        { id: 'query',     name: '整改查询',   col: 5, row: 2, icon: 'el-icon-search',             kind: 'sub',     route: '/rectify/query' },
        { id: 'unregister', name: '未销号问题', col: 6, row: 2, icon: 'el-icon-close',             kind: 'warn',    route: '/rectify/unregisteredProblem', alert: true },

        // ===== 问题汇总层（row 1）：上方分支 =====
        { id: 'auditCollect',   name: '审计问题汇总', col: 0.5, row: 1, icon: 'el-icon-s-data',          kind: 'sub',  route: '/prball/auditIssuesCollect' },
        { id: 'monitorCollect', name: '内控问题汇总', col: 1.5, row: 1, icon: 'el-icon-warning-outline',  kind: 'sub',  route: '/prball/monitorlssuesCollect' },
        { id: 'zgqd',           name: '整改清单',     col: 2.5, row: 1, icon: 'el-icon-s-check',          kind: 'sub',  route: '/prball/zgqd' },

        // ===== 违规追责链（row 3）：下方分支 =====
        { id: 'wgzz',   name: '违规追责受理', col: 2, row: 3, icon: 'el-icon-s-claim',          kind: 'legal', route: '/wgzz/zz', alert: true },
        { id: 'shbg',   name: '初步核实',     col: 3, row: 3, icon: 'el-icon-document-checked', kind: 'legal', route: '/wgzz/shbg' },
        { id: 'wghs',   name: '违规核查',     col: 4, row: 3, icon: 'el-icon-search',           kind: 'legal', route: '/wgzz/wghs' },
        { id: 'wghc',   name: '移送函',       col: 5, row: 3, icon: 'el-icon-position',         kind: 'legal', route: '/wgzz/wghc' },
        { id: 'tzgltz', name: '问题台账',     col: 6, row: 3, icon: 'el-icon-s-order',          kind: 'legal', route: '/wgzz/tzgltz' },

        // ===== 台账归档层（row 0）：顶部支撑 =====
        { id: 'standingbook', name: '整改台账',       col: 3,   row: 0, icon: 'el-icon-notebook-2',   kind: 'archive', route: '/rectify/standingbook' },
        { id: 'zglstz',       name: '整改落实台账',   col: 4,   row: 0, icon: 'el-icon-s-order',       kind: 'archive', route: '/rectify/zglstz' },
        { id: 'wqzgqdhz',     name: '往期整改汇总',   col: 5,   row: 0, icon: 'el-icon-collection',    kind: 'archive', route: '/rectify/wqzgqdhz' },
        { id: 'hxzg',         name: '后续整改',       col: 6,   row: 0, icon: 'el-icon-refresh',       kind: 'archive', route: '/rectify/zgzzhxzg' },
        { id: 'report',       name: '整改报告',       col: 7,   row: 0, icon: 'el-icon-document',      kind: 'archive', route: '/rectify/report' },

        // ===== 统计分析层（row 4）：底部支撑 =====
        { id: 'stat0',  name: '统计总览',   col: 2,   row: 4, icon: 'el-icon-data-analysis', kind: 'config', route: '/home/index' },
        { id: 'stat1',  name: '利润分析',   col: 3,   row: 4, icon: 'el-icon-data-line',     kind: 'config', route: '/home/lrfx' },
        { id: 'stat2',  name: '收入分析',   col: 4,   row: 4, icon: 'el-icon-coin',          kind: 'config', route: '/home/srfx' },
        { id: 'stat3',  name: '成本分析',   col: 5,   row: 4, icon: 'el-icon-s-order',       kind: 'config', route: '/home/cbfyfx' },
        { id: 'stat4',  name: '资产分析',   col: 6,   row: 4, icon: 'el-icon-s-finance',     kind: 'config', route: '/home/zcfzfx' },
        { id: 'stat5',  name: '应收分析',   col: 7,   row: 4, icon: 'el-icon-s-claim',       kind: 'config', route: '/home/ysyfzkfx' },
        { id: 'xxzx',   name: '消息中心',   col: 7.5, row: 2, icon: 'el-icon-bell',          kind: 'config', route: '/home/xxzx' },
      ],
      rcFlowEdges: [
        // 主干流程（蓝色实线）
        ['assign',    'scheme',      'main'],
        ['scheme',    'practicable', 'main'],
        ['practicable','track',      'main'],
        ['track',     'valuation',   'main'],
        ['valuation', 'query',       'main'],
        ['query',     'unregister',  'main'],
        // 问题汇总 → 主干（虚线 sub）
        ['auditCollect',   'assign',  'sub'],
        ['monitorCollect', 'scheme',  'sub'],
        ['zgqd',           'practicable', 'sub'],
        // 整改落实 → 违规追责链（红色）
        ['practicable', 'wgzz',   'legal'],
        ['wgzz',  'shbg',   'legal'],
        ['shbg',  'wghs',   'legal'],
        ['wghs',  'wghc',   'legal'],
        ['wghc',  'tzgltz', 'legal'],
        // 整改跟踪 → 台账归档（紫色虚线）
        ['track',     'standingbook', 'archive-dash'],
        ['valuation', 'zglstz',       'archive-dash'],
        ['query',     'wqzgqdhz',     'archive-dash'],
        ['unregister','hxzg',         'archive-dash'],
        ['unregister','report',       'archive-dash'],
        // 整改评价 → 统计分析（灰虚线）
        ['valuation', 'stat0', 'sub'],
        ['stat0', 'stat1', 'sub'],
        ['stat1', 'stat2', 'sub'],
        ['stat2', 'stat3', 'sub'],
        ['stat3', 'stat4', 'sub'],
        ['stat4', 'stat5', 'sub'],
      ],
      welcomeStats: [
        { label: '问题总量', value: '--', color: '#FF4D4F' },
        { label: '整改完成率', value: '--', color: '#52C41A' },
        { label: '违规追责', value: '--', color: '#722ED1' },
        { label: '未销号问题', value: '--', color: '#FA8C16' },
        { label: '整改台账', value: '--', color: '#1890FF' },
      ],
      kpiCards: [
        { label: '问题总量', value: '--', unit: '项', icon: 'el-icon-warning-outline', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', barBg: 'rgba(255,77,79,0.1)', barColor: '#FF4D4F', barWidth: 0, trend: null, theme: 'kpi-danger' },
        { label: '整改完成率', value: '--', unit: '%', icon: 'el-icon-circle-check', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', barBg: 'rgba(82,196,26,0.1)', barColor: '#52C41A', barWidth: 0, trend: null, theme: '' },
        { label: '整改落实率', value: '--', unit: '%', icon: 'el-icon-s-check', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', barBg: 'rgba(24,144,255,0.1)', barColor: '#1890FF', barWidth: 0, trend: null, theme: '' },
        { label: '未销号问题', value: '--', unit: '项', icon: 'el-icon-close', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', barBg: 'rgba(250,140,22,0.1)', barColor: '#FA8C16', barWidth: 0, trend: null, theme: 'kpi-warning' },
        { label: '违规追责件', value: '--', unit: '件', icon: 'el-icon-s-claim', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', barBg: 'rgba(114,46,209,0.1)', barColor: '#722ED1', barWidth: 0, trend: null, theme: 'kpi-danger' },
        { label: '整改评价分', value: '--', unit: '分', icon: 'el-icon-star-off', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', barBg: 'rgba(19,194,194,0.1)', barColor: '#13C2C2', barWidth: 0, trend: null, theme: '' },
      ],
      quickItems: [
        { name: '整改报告', icon: 'el-icon-data-line', iconColor: '#1890FF', route: '/rectify/report' },
        { name: '消息中心', icon: 'el-icon-bell', iconColor: '#52C41A', route: '/home/xxzx' },
      ],
      rectifyTrackItems: [
        { name: '整改分派', desc: '问题整改分派下达', color: '#1890FF', count: '', route: '/rectify/assign' },
        { name: '整改落实', desc: '整改措施落实情况', color: '#52C41A', count: '', route: '/rectify/practicable' },
        { name: '整改跟踪', desc: '整改进度跟踪监督', color: '#FA8C16', count: '', route: '/rectify/track' },
        { name: '整改评价', desc: '整改效果综合评价', color: '#722ED1', route: '/rectify/valuation' },
        { name: '整改查询', desc: '整改状态综合查询', color: '#13C2C2', route: '/rectify/query' },
        { name: '未销号问题', desc: '尚未完成销号问题', color: '#FF4D4F', count: '', route: '/rectify/unregisteredProblem' },
      ],
      rectifyHistItems: [
        { name: '整改台账', desc: '整改全过程台账', icon: 'el-icon-s-order', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/rectify/standingbook' },
        { name: '往期整改清单汇总', desc: '历年整改清单汇总', icon: 'el-icon-collection', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/rectify/wqzgqdhz' },
        { name: '整改落实台账', desc: '整改落实专项台账', icon: 'el-icon-notebook-2', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/rectify/zglstz' },
        { name: '后续整改', desc: '整改跟踪后续管理', icon: 'el-icon-refresh', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/rectify/zgzzhxzg' },
        { name: '整改清单', desc: '整改事项清单管理', icon: 'el-icon-s-check', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/prball/zgqd' },
      ],
      rectifyProgress: [
        { label: '整改完成率', percent: 0, color: '#52C41A' },
        { label: '整改落实率', percent: 0, color: '#1890FF' },
        { label: '问题销号率', percent: 0, color: '#FA8C16' },
      ],
      wgzzItems: [
        { name: '违规追责受理', desc: '违规问题追责立案', icon: 'el-icon-s-claim', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', badge: '', route: '/wgzz/zz' },
        { name: '问题台账', desc: '违规问题管理台账', icon: 'el-icon-s-order', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/wgzz/tzgltz' },
        { name: '违规核查', desc: '违规行为核查调查', icon: 'el-icon-search', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/wgzz/wghs' },
        { name: '移送函', desc: '违规问题移送处理', icon: 'el-icon-position', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/wgzz/wghc' },
        { name: '初步核实', desc: '违规问题初步核实', icon: 'el-icon-document-checked', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/wgzz/shbg' },
      ],
      wgzzStats: [
        { label: '立案件数', value: '--', percent: 0, color: '#FF4D4F' },
        { label: '核查完成', value: '--', percent: 0, color: '#1890FF' },
        { label: '处置完成', value: '--', percent: 0, color: '#52C41A' },
      ],
      collectItems: [
        { name: '审计问题汇总', desc: '审计发现问题汇总', icon: 'el-icon-s-data', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', tag: '审计', tagType: 'danger', route: '/prball/auditIssuesCollect' },
        { name: '内控问题汇总', desc: '内控监督问题汇总', icon: 'el-icon-warning-outline', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', tag: '内控', tagType: 'warning', route: '/prball/monitorlssuesCollect' },
        { name: '整改清单', desc: '问题整改清单管理', icon: 'el-icon-s-check', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/prball/zgqd' },
        { name: '整改台账', desc: '整改全过程台账', icon: 'el-icon-notebook-2', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/rectify/standingbook' },
        { name: '整改报告', desc: '整改情况综合报告', icon: 'el-icon-document', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/rectify/report' },
      ],
      reportItems: [
        { name: '整改报告', desc: '整改情况综合报告', icon: 'el-icon-document', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/rectify/report' },
        { name: '整改台账', desc: '整改全量台账查阅', icon: 'el-icon-s-order', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/rectify/standingbook' },
        { name: '初步核实', desc: '违规问题初步核实', icon: 'el-icon-document-checked', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/wgzz/shbg' },
        { name: '消息中心', desc: '系统消息与通知', icon: 'el-icon-bell', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/home/xxzx' },
      ],
      statsTemplates: [
        { name: '统计总览', icon: 'el-icon-data-analysis', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/home/index' },
        { name: '模版一·利润', icon: 'el-icon-data-line', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/home/lrfx' },
        { name: '模版二·收入', icon: 'el-icon-coin', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/home/srfx' },
        { name: '模版三·成本', icon: 'el-icon-s-order', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/home/cbfyfx' },
        { name: '模版四·资产', icon: 'el-icon-s-finance', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/home/zcfzfx' },
        { name: '模版五·应收', icon: 'el-icon-s-claim', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/home/ysyfzkfx' },
      ],
      modules: [
        { name: '问题汇总', icon: 'el-icon-warning-outline', desc: '审计/内控问题汇总', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', route: '/prball/auditIssuesCollect' },
        { name: '整改分派', icon: 'el-icon-s-order', desc: '问题整改分派下达', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', route: '/rectify/assign' },
        { name: '整改通知', icon: 'el-icon-document', desc: '整改通知书管理', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', route: '/rectify/scheme' },
        { name: '整改落实', icon: 'el-icon-s-check', desc: '整改措施落实跟踪', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', route: '/rectify/practicable' },
        { name: '整改跟踪', icon: 'el-icon-view', desc: '整改进度跟踪监督', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', route: '/rectify/track' },
        { name: '整改评价', icon: 'el-icon-star-off', desc: '整改效果综合评价', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', route: '/rectify/valuation' },
        { name: '违规追责', icon: 'el-icon-s-claim', desc: '违规核查/移送/追责', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', route: '/wgzz/zz' },
        { name: '整改查询', icon: 'el-icon-search', desc: '整改状态综合查询', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', route: '/rectify/query' },
        { name: '未销号问题', icon: 'el-icon-close', desc: '未完成销号问题', iconBg: 'rgba(235,47,150,0.1)', iconColor: '#EB2F96', route: '/rectify/unregisteredProblem' },
        { name: '整改清单', icon: 'el-icon-s-check', desc: '整改清单管理', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', route: '/prball/zgqd' },
        { name: '统计分析', icon: 'el-icon-data-analysis', desc: '整改数据统计分析', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', route: '/home/index' },
        { name: '消息中心', icon: 'el-icon-bell', desc: '系统消息与通知', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', route: '/home/xxzx' },
      ],
      ledgerItems: [
        { name: '整改台账', icon: 'el-icon-notebook-2', iconColor: '#1890FF', route: '/rectify/standingbook' },
        { name: '整改落实台账', icon: 'el-icon-s-order', iconColor: '#52C41A', route: '/rectify/zglstz' },
        { name: '往期整改清单汇总', icon: 'el-icon-collection', iconColor: '#722ED1', route: '/rectify/wqzgqdhz' },
        { name: '后续整改', icon: 'el-icon-refresh', iconColor: '#FA8C16', route: '/rectify/zgzzhxzg' },
      ],
      rectifyReportItems: [
        { name: '整改报告', icon: 'el-icon-document', iconColor: '#1890FF', route: '/rectify/report' },
        { name: '整改清单', icon: 'el-icon-s-check', iconColor: '#52C41A', route: '/prball/zgqd' },
        { name: '审计问题汇总', icon: 'el-icon-data-analysis', iconColor: '#FF4D4F', route: '/prball/auditIssuesCollect' },
        { name: '内控问题汇总', icon: 'el-icon-warning-outline', iconColor: '#722ED1', route: '/prball/monitorlssuesCollect' },
      ],
      wgzzFlowItems: [
        { name: '违规追责受理', icon: 'el-icon-s-claim', iconColor: '#FF4D4F', route: '/wgzz/zz' },
        { name: '初步核实', icon: 'el-icon-document-checked', iconColor: '#1890FF', route: '/wgzz/shbg' },
        { name: '违规核查', icon: 'el-icon-search', iconColor: '#722ED1', route: '/wgzz/wghs' },
        { name: '移送函', icon: 'el-icon-position', iconColor: '#FA8C16', route: '/wgzz/wghc' },
        { name: '问题台账', icon: 'el-icon-s-order', iconColor: '#52C41A', route: '/wgzz/tzgltz' },
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
    rcNodeMap() {
      const m = {}
      this.rcFlowNodes.forEach(n => { m[n.id] = n })
      return m
    },
    rcCanvasW() {
      const maxCol = Math.max(...this.rcFlowNodes.map(n => n.col))
      return Math.ceil((maxCol + 1) * this.rcFlowConfig.colW + this.rcFlowConfig.padX * 2)
    },
    rcCanvasH() {
      const maxRow = Math.max(...this.rcFlowNodes.map(n => n.row))
      return Math.ceil((maxRow + 1) * this.rcFlowConfig.rowH + this.rcFlowConfig.padY * 2)
    },
    rcEdgePaths() {
      const { colW, rowH, nodeW, nodeH, padX, padY } = this.rcFlowConfig
      const center = (n) => ({
        x: padX + (n.col + 0.5) * colW,
        y: padY + (n.row + 0.5) * rowH,
      })
      const markerMap = {
        main: 'url(#rc-arrow-main)',
        sub: 'url(#rc-arrow)',
        legal: 'url(#rc-arrow-legal)',
        'legal-dash': 'url(#rc-arrow-legal)',
        archive: 'url(#rc-arrow-archive)',
        'archive-dash': 'url(#rc-arrow-archive)',
        warn: 'url(#rc-arrow-warn)',
      }
      return this.rcFlowEdges.map(([from, to, kind]) => {
        const a = this.rcNodeMap[from], b = this.rcNodeMap[to]
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
        return { d, cls: 'rc-edge-' + kind, marker: markerMap[kind] || markerMap.sub }
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
    this.loadOverviewData()
  },
  methods: {
    jumpTo(route) { if (route) this.$router.push(route) },
    /** 加载整改追责首页聚合数据 */
    async loadOverviewData() {
      try {
        const res = await getRectifyHomeOverview({})
        if (res && res.code === 1 && res.data) {
          const data = res.data
          this._applyKpiData(data.kpi)
          this._applyRectifyTrack(data.rectifyTrack)
          this._applyProgress(data.progress)
          this._applyWgzz(data.wgzz)
        }
      } catch (e) {
        console.error('加载整改追责首页数据失败', e)
      }
    },
    /** 应用 KPI 数据到 welcomeStats 和 kpiCards */
    _applyKpiData(kpi) {
      if (!kpi) return
      const { issuesTotal, rectifyRate, implementRate, unregistered, wgzzAccepted, evaluationScore, ledgerCount } = kpi
      // welcomeStats
      this.$set(this.welcomeStats, 0, { ...this.welcomeStats[0], value: issuesTotal + '项' })
      this.$set(this.welcomeStats, 1, { ...this.welcomeStats[1], value: rectifyRate + '%' })
      this.$set(this.welcomeStats, 2, { ...this.welcomeStats[2], value: wgzzAccepted + '件' })
      this.$set(this.welcomeStats, 3, { ...this.welcomeStats[3], value: unregistered + '项' })
      this.$set(this.welcomeStats, 4, { ...this.welcomeStats[4], value: ledgerCount + '条' })
      // kpiCards
      this.$set(this.kpiCards, 0, { ...this.kpiCards[0], value: String(issuesTotal), barWidth: 100 })
      this.$set(this.kpiCards, 1, { ...this.kpiCards[1], value: String(rectifyRate), barWidth: rectifyRate })
      this.$set(this.kpiCards, 2, { ...this.kpiCards[2], value: String(implementRate), barWidth: implementRate })
      this.$set(this.kpiCards, 3, { ...this.kpiCards[3], value: String(unregistered), barWidth: issuesTotal > 0 ? Math.round(unregistered * 100 / issuesTotal) : 0 })
      this.$set(this.kpiCards, 4, { ...this.kpiCards[4], value: String(wgzzAccepted), barWidth: issuesTotal > 0 ? Math.round(wgzzAccepted * 100 / issuesTotal) : 0 })
      this.$set(this.kpiCards, 5, { ...this.kpiCards[5], value: String(evaluationScore), barWidth: evaluationScore })
    },
    /** 应用整改跟踪数量 */
    _applyRectifyTrack(track) {
      if (!track) return
      const { assignCount, practicableCount, trackingCount, unregisteredCount } = track
      this.$set(this.rectifyTrackItems, 0, { ...this.rectifyTrackItems[0], count: String(assignCount) })
      this.$set(this.rectifyTrackItems, 1, { ...this.rectifyTrackItems[1], count: String(practicableCount) })
      this.$set(this.rectifyTrackItems, 2, { ...this.rectifyTrackItems[2], count: String(trackingCount) })
      this.$set(this.rectifyTrackItems, 5, { ...this.rectifyTrackItems[5], count: String(unregisteredCount) })
    },
    /** 应用整改进度百分比 */
    _applyProgress(progress) {
      if (!progress) return
      this.$set(this.rectifyProgress, 0, { ...this.rectifyProgress[0], percent: progress.rectifyRate || 0 })
      this.$set(this.rectifyProgress, 1, { ...this.rectifyProgress[1], percent: progress.implementRate || 0 })
      this.$set(this.rectifyProgress, 2, { ...this.rectifyProgress[2], percent: progress.cancelRate || 0 })
    },
    /** 应用违规追责统计 */
    _applyWgzz(wgzz) {
      if (!wgzz) return
      const { accepted, verified, disposed, pending } = wgzz
      // badge
      if (pending > 0) {
        this.$set(this.wgzzItems, 0, { ...this.wgzzItems[0], badge: String(pending) })
      }
      // stats
      const total = accepted || 1
      this.$set(this.wgzzStats, 0, { ...this.wgzzStats[0], value: accepted + '件', percent: Math.round(accepted * 100 / total) })
      this.$set(this.wgzzStats, 1, { ...this.wgzzStats[1], value: verified + '件', percent: total > 0 ? Math.round(verified * 100 / total) : 0 })
      this.$set(this.wgzzStats, 2, { ...this.wgzzStats[2], value: disposed + '件', percent: total > 0 ? Math.round(disposed * 100 / total) : 0 })
    },
    rcNodePos(n) {
      const { colW, rowH, nodeW, nodeH, padX, padY } = this.rcFlowConfig
      const left = padX + n.col * colW + (colW - nodeW) / 2
      const top = padY + n.row * rowH + (rowH - nodeH) / 2
      return { left: left + 'px', top: top + 'px', width: nodeW + 'px', height: nodeH + 'px' }
    },
    _patchPrimaryColors() {
      const bright = this.ipBright || '#1890FF'
      const rgb = this.ipPrimaryRgb || '24,144,255'
      const brightBg = `rgba(${rgb},0.1)`
      const brightBg2 = `rgba(${rgb},0.08)`
      if (this.welcomeStats && this.welcomeStats[4]) this.welcomeStats[4].color = bright
      if (this.kpiCards && this.kpiCards[2]) { this.kpiCards[2].iconBg = brightBg; this.kpiCards[2].iconColor = bright; this.kpiCards[2].barBg = brightBg; this.kpiCards[2].barColor = bright }
      if (this.modules && this.modules[1]) { this.modules[1].iconBg = brightBg; this.modules[1].iconColor = bright }
      if (this.statsTemplates && this.statsTemplates[0]) { this.statsTemplates[0].bgColor = brightBg2; this.statsTemplates[0].iconColor = bright }
    },
  },
}
</script>

<style lang="scss" scoped>
.smart-rectify-home { padding: 16px; background: #f0f2f5; min-height: 100vh; }

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
.section-legend {
  font-size: 12px; color: #888;
  span { margin-left: 14px; }
  .dot {
    display: inline-block; width: 8px; height: 8px; border-radius: 50%; margin-right: 4px; vertical-align: middle;
    &.blue { background: var(--ip-primary, #1890FF); }
    &.green { background: #52C41A; }
    &.orange { background: #FA8C16; }
    &.red { background: #FF4D4F; }
    &.purple { background: #722ED1; }
    &.gray { background: #bbb; }
  }
}

/* ========== 拓扑图画布 ========== */
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

/* 整改追责专属边线样式 */
.rc-edge {
  stroke-width: 1.6;
  fill: none;
  &.rc-edge-main {
    stroke: var(--ip-primary, #1890FF);
    stroke-width: 2.2;
  }
  &.rc-edge-sub {
    stroke: #b8b8b8;
    stroke-dasharray: 4 3;
  }
  &.rc-edge-legal {
    stroke: #FF4D4F;
    stroke-width: 1.8;
  }
  &.rc-edge-legal-dash {
    stroke: #FF7875;
    stroke-dasharray: 5 3;
  }
  &.rc-edge-archive {
    stroke: #722ED1;
    stroke-width: 1.6;
  }
  &.rc-edge-archive-dash {
    stroke: #9254DE;
    stroke-dasharray: 4 3;
  }
  &.rc-edge-warn {
    stroke: #FA8C16;
    stroke-dasharray: 4 3;
  }
}

/* 节点公共样式（复用合同页方案） */
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
  .fpn-dot { position: absolute; top: -3px; right: -3px; width: 9px; height: 9px; border-radius: 50%; background: #FF4D4F; animation: pulse 1.5s infinite; }
  .fpn-badge { position: absolute; top: -7px; right: -7px; min-width: 18px; height: 16px; padding: 0 4px; border-radius: 8px; background: #FF4D4F; color: #fff; font-size: 10px; font-weight: 700; line-height: 16px; text-align: center; box-shadow: 0 1px 3px rgba(255,77,79,.4); }

  &.fpn-main {
    border-color: var(--ip-primary, #1890FF); border-width: 2px;
    .fpn-icon-wrap { background: rgba(24,144,255,0.12); }
    .fpn-icon-wrap i { color: var(--ip-primary, #1890FF); }
    .fpn-label { color: var(--ip-primary, #1890FF); }
  }
  &.fpn-sub {
    border-color: #d9d9d9;
    .fpn-icon-wrap { background: #f0f5ff; }
    .fpn-icon-wrap i { color: #5b8def; }
  }
  &.fpn-warn {
    border-color: #FA8C16; border-width: 2px; background: #FFFBE6;
    .fpn-icon-wrap { background: rgba(250,140,22,0.15); }
    .fpn-icon-wrap i { color: #FA8C16; }
    .fpn-label { color: #FA8C16; }
  }
  &.fpn-legal {
    border-color: #FF7875; background: #FFF1F0;
    .fpn-icon-wrap { background: rgba(255,77,79,0.12); }
    .fpn-icon-wrap i { color: #FF4D4F; }
    .fpn-label { color: #CF1322; }
  }
  &.fpn-archive {
    border-color: #9254DE; background: #f9f0ff;
    .fpn-icon-wrap { background: rgba(114,46,209,0.12); }
    .fpn-icon-wrap i { color: #722ED1; }
    .fpn-label { color: #531DAB; }
  }
  &.fpn-config {
    border-color: #e8e8e8; background: #fafafa; border-style: dashed;
    .fpn-icon-wrap { background: #f0f0f0; }
    .fpn-icon-wrap i { color: #888; }
    .fpn-label { color: #666; font-weight: 500; }
  }
  &.fpn-alert { animation: nodeAlert 2s infinite; }
}

@keyframes pulse { 0% { box-shadow: 0 0 0 0 rgba(255,77,79,0.5); } 70% { box-shadow: 0 0 0 6px rgba(255,77,79,0); } 100% { box-shadow: 0 0 0 0 rgba(255,77,79,0); } }
@keyframes nodeAlert { 0%, 100% { box-shadow: 0 1px 4px rgba(0,0,0,.04); } 50% { box-shadow: 0 0 0 4px rgba(255,77,79,0.15), 0 1px 4px rgba(0,0,0,.04); } }

.content-row { margin-bottom: 16px; }
.card-panel { background: #fff; border-radius: 10px; padding: 16px 18px; box-shadow: 0 1px 6px rgba(0,0,0,.06); height: 100%; }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; padding-bottom: 10px; border-bottom: 1px solid #f0f0f0; }
.card-title { font-size: 15px; font-weight: 600; color: var(--ip-primary); i { margin-right: 6px; } }
.card-more { font-size: 12px; color: var(--ip-bright); cursor: pointer; &:hover { opacity: 0.8; } }

.inner-tabs { ::v-deep .el-tabs__header { margin-bottom: 8px; } ::v-deep .el-tabs__item { font-size: 13px; padding: 0 12px; } }
.rectify-list { display: flex; flex-direction: column; gap: 6px; }
.rectify-entry { display: flex; align-items: center; gap: 8px; padding: 8px 10px; border-radius: 8px; cursor: pointer; transition: background 0.15s;
  &:hover { background: #f5f7fa; }
  .re-dot { width: 8px; height: 8px; border-radius: 50%; flex-shrink: 0; }
  .re-icon { width: 28px; height: 28px; border-radius: 6px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 13px; } }
  .re-body { flex: 1; .re-name { font-size: 13px; color: #333; font-weight: 500; display: block; } .re-desc { font-size: 11px; color: #999; } }
  .re-count { font-size: 14px; font-weight: 700; flex-shrink: 0; }
  .re-arrow { font-size: 12px; color: #d9d9d9; flex-shrink: 0; }
}
.rectify-progress { margin-top: 10px; padding-top: 10px; border-top: 1px solid #f0f0f0; }
.rp-row { display: flex; align-items: center; gap: 8px; margin-bottom: 6px;
  .rp-label { font-size: 12px; color: #888; width: 80px; flex-shrink: 0; } .rp-bar-bg { flex: 1; height: 6px; background: #f5f5f5; border-radius: 3px; overflow: hidden; } .rp-bar-fill { height: 100%; border-radius: 3px; } .rp-val { font-size: 12px; font-weight: 600; color: #333; width: 40px; text-align: right; }
}

.wgzz-list { display: flex; flex-direction: column; gap: 6px; margin-bottom: 12px; }
.wgzz-entry { display: flex; align-items: center; gap: 10px; padding: 8px 10px; border-radius: 8px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.15s;
  &:hover { border-color: var(--ip-primary); background: var(--ip-light-bg, #fafbff); }
  .wz-icon { width: 30px; height: 30px; border-radius: 6px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 14px; } }
  .wz-body { flex: 1; .wz-name { font-size: 13px; font-weight: 500; color: #333; } .wz-desc { font-size: 11px; color: #999; } }
  .wz-badge { ::v-deep .el-badge__content { top: 4px; } }
}
.wgzz-stat { }
.ws-stat-row { display: flex; align-items: center; gap: 8px; margin-bottom: 6px;
  .wss-label { font-size: 12px; color: #888; width: 60px; flex-shrink: 0; } .wss-bar-bg { flex: 1; height: 6px; background: #f5f5f5; border-radius: 3px; overflow: hidden; } .wss-bar-fill { height: 100%; border-radius: 3px; } .wss-val { font-size: 12px; font-weight: 600; color: #333; width: 40px; text-align: right; }
}

.collect-list { display: flex; flex-direction: column; gap: 6px; }
.collect-entry { display: flex; align-items: center; gap: 10px; padding: 8px 10px; border-radius: 8px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.15s;
  &:hover { border-color: var(--ip-primary); background: var(--ip-light-bg, #fafbff); }
  .cl-icon { width: 30px; height: 30px; border-radius: 6px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 14px; } }
  .cl-body { flex: 1; .cl-name { font-size: 13px; font-weight: 500; color: #333; } .cl-desc { font-size: 11px; color: #999; } }
  .cl-tag { flex-shrink: 0; }
  .cl-arrow { font-size: 12px; color: #d9d9d9; flex-shrink: 0; }
}

.report-list { display: flex; flex-direction: column; gap: 6px; }
.report-entry { display: flex; align-items: center; gap: 10px; padding: 8px 10px; border-radius: 8px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.15s;
  &:hover { border-color: var(--ip-primary); background: var(--ip-light-bg, #fafbff); }
  .rt-icon { width: 30px; height: 30px; border-radius: 6px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 14px; } }
  .rt-body { flex: 1; .rt-name { font-size: 13px; font-weight: 500; color: #333; } .rt-desc { font-size: 11px; color: #999; } }
  .rt-arrow { font-size: 12px; color: #d9d9d9; flex-shrink: 0; }
}

.stats-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 10px; }
.stats-card { display: flex; flex-direction: column; align-items: center; padding: 14px 8px; border-radius: 10px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.2s;
  &:hover { border-color: var(--ip-primary); transform: translateY(-2px); }
  .stc-icon { width: 32px; height: 32px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-bottom: 6px; i { font-size: 14px; } }
  .stc-name { font-size: 12px; color: #333; font-weight: 500; text-align: center; }
}


.module-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px; }
.module-card { display: flex; flex-direction: column; align-items: center; justify-content: center; padding: 14px 6px; border-radius: 10px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.2s; text-align: center;
  &:hover { border-color: var(--ip-primary); transform: translateY(-2px); box-shadow: 0 6px 16px rgba(var(--ip-primary-rgb),0.1); }
  .mc-icon { width: 36px; height: 36px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-bottom: 6px; i { font-size: 18px; } }
  .mc-name { font-size: 12px; font-weight: 600; color: #333; } .mc-desc { font-size: 10px; color: #bbb; margin-top: 2px; }
}

.ledger-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 10px; }
.lg-section { padding: 10px; border-radius: 8px; border: 1px solid #f0f0f0; }
.lg-title { font-size: 12px; font-weight: 600; color: var(--ip-primary); margin-bottom: 8px; padding-bottom: 4px; border-bottom: 1px solid #f0f0f0; }
.lg-item { display: flex; align-items: center; gap: 6px; padding: 5px 4px; font-size: 12px; cursor: pointer; transition: color 0.15s; &:hover { color: var(--ip-primary); } i { font-size: 12px; color: #888; } span { color: #333; } }
</style>