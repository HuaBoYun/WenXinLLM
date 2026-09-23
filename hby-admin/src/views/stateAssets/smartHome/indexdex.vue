<template>
  <div class="state-assets-home" :style="themeVars">
    <!-- ========== 顶部欢迎区 ========== -->
    <div class="welcome-section">
      <div class="welcome-bg"></div>
      <div class="welcome-content">
        <div class="welcome-left">
          <div class="greeting">
            <span class="greeting-icon"><i class="el-icon-sunny" /></span>
            <span class="greeting-text">{{ greetingWord }}，{{ userName }}</span>
          </div>
          <h1 class="welcome-title">国资穿透式监管平台</h1>
          <p class="welcome-desc">投资 · 金融 · 采购 · 军品 · 境外 · 行业 · 合同 · 会计 · 财务 · 资金 · 薪酬 · 产权 穿透全覆盖</p>
          <div class="welcome-actions">
            <el-button type="primary" icon="el-icon-s-data" round @click="jumpTo('/stateAssets/dashboard/index')">监管驾驶舱</el-button>
            <el-button icon="el-icon-view" round @click="jumpTo('/home/screen/gzctzl')">国资穿透总览</el-button>
            <el-button icon="el-icon-warning" round @click="jumpTo('/home/screen/gzfxct')">国资风险穿透</el-button>
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
          <div class="kpi-trend" v-if="k.trend !== undefined" :class="k.trend > 0 ? 'trend-up' : 'trend-down'">
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

    <!-- ========== 国资穿透六层全景拓扑图 ========== -->
    <div class="lifecycle-section">
      <div class="section-header">
        <div class="section-title"><i class="el-icon-share" /> 国资穿透全景拓扑图</div>
        <div class="section-legend">
          <span><i class="dot dot-main" />主流程</span>
          <span><i class="dot dot-sub" />支撑节点</span>
          <span><i class="dot dot-warn" />风险/预警</span>
          <span><i class="dot dot-finance" />财务/数据</span>
        </div>
      </div>
      <div class="lf-canvas-wrap">
        <div class="lf-canvas" :style="{ width: lfCanvasW + 'px', height: lfCanvasH + 'px' }">
          <!-- 泳道背景 -->
          <div v-for="layer in lfLayers" :key="'layer-'+layer.id"
               class="lf-lane"
               :class="'lane-'+layer.id"
               :style="{ top: layer.laneTop + 'px', height: layer.laneH + 'px', width: lfCanvasW + 'px' }">
            <div class="lf-lane-label" :style="{ background: layer.color, color: '#fff' }">
              <i :class="layer.icon" />
              <span>{{ layer.name }}</span>
            </div>
          </div>
          <!-- SVG 连线 -->
          <svg class="lf-svg" :width="lfCanvasW" :height="lfCanvasH">
            <defs>
              <marker id="lf-arr-main" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="6" markerHeight="6" orient="auto">
                <path d="M0,0 L10,5 L0,10 z" fill="var(--ip-primary,#1890FF)" />
              </marker>
              <marker id="lf-arr-sub" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="5" markerHeight="5" orient="auto">
                <path d="M0,0 L10,5 L0,10 z" fill="#bbb" />
              </marker>
              <marker id="lf-arr-warn" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="5" markerHeight="5" orient="auto">
                <path d="M0,0 L10,5 L0,10 z" fill="#FF4D4F" />
              </marker>
              <marker id="lf-arr-finance" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="5" markerHeight="5" orient="auto">
                <path d="M0,0 L10,5 L0,10 z" fill="#13C2C2" />
              </marker>
            </defs>
            <path v-for="(e, idx) in lfEdgePaths" :key="'lfe'+idx"
                  :d="e.d" :class="['lf-edge','edge-'+e.kind]" :marker-end="e.marker" fill="none" />
          </svg>
          <!-- 节点 -->
          <div v-for="n in lfAllNodes" :key="n.id"
               class="flow-pos-node"
               :class="['fpn-'+n.kind, n.alert ? 'fpn-alert' : '']"
               :style="lfNodePos(n)"
               @click="jumpTo(n.route)">
            <div class="fpn-icon-wrap"><i :class="n.icon" /></div>
            <div class="fpn-label">{{ n.name }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- ========== 中部：风险预警 + 穿透分布 + 监管快报 ========== -->
    <el-row :gutter="14" class="content-row">
      <!-- 风险预警中心 -->
      <el-col :span="10">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-bell" /> 风险预警中心</span>
            <span class="card-more" @click="jumpTo('/home/screen/gzfxct')">更多 →</span>
          </div>
          <el-tabs v-model="todoActive" class="inner-tabs">
            <el-tab-pane name="risk">
              <span slot="label">风险预警 <el-badge :value="riskList.length" type="danger" class="tab-badge" /></span>
              <div class="todo-list">
                <div v-for="item in riskList" :key="item.id" class="todo-item" @click="jumpTo(item.route)">
                  <div class="todo-left">
                    <div class="todo-dot" :class="item.level === 'HIGH' ? 'dot-red' : 'dot-orange'"></div>
                    <div class="todo-info">
                      <div class="todo-name">{{ item.title }}</div>
                      <div class="todo-meta">{{ item.domain }} · {{ item.desc }}</div>
                    </div>
                  </div>
                  <div class="todo-right-col">
                    <el-tag :type="item.level === 'HIGH' ? 'danger' : item.level === 'MEDIUM' ? 'warning' : 'info'" size="mini">{{ item.levelLabel }}</el-tag>
                    <span class="todo-time">{{ item.time }}</span>
                  </div>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane name="pending">
              <span slot="label">待处理 <el-badge :value="pendingList.length" class="tab-badge" /></span>
              <div class="todo-list">
                <div v-for="item in pendingList" :key="item.id" class="todo-item" @click="jumpTo(item.route)">
                  <div class="todo-left">
                    <div class="todo-dot" :class="item.urgent ? 'dot-red' : 'dot-orange'"></div>
                    <div class="todo-info">
                      <div class="todo-name">{{ item.title }}</div>
                      <div class="todo-meta">{{ item.domain }} · {{ item.desc }}</div>
                    </div>
                  </div>
                  <el-tag :type="item.urgent ? 'danger' : 'warning'" size="mini" effect="dark">{{ item.statusLabel }}</el-tag>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane name="abnormal">
              <span slot="label">异常检测 <el-badge :value="abnormalList.length" type="danger" class="tab-badge" /></span>
              <div class="todo-list">
                <div v-for="item in abnormalList" :key="item.id" class="todo-item" @click="jumpTo(item.route)">
                  <div class="todo-left">
                    <div class="todo-dot dot-red"></div>
                    <div class="todo-info">
                      <div class="todo-name">{{ item.title }}</div>
                      <div class="todo-meta">{{ item.domain }} · {{ item.desc }}</div>
                    </div>
                  </div>
                  <el-tag type="danger" size="mini">{{ item.typeLabel }}</el-tag>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-col>

      <!-- 穿透领域分布 -->
      <el-col :span="7">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-pie-chart" /> 穿透领域监管分布</span>
            <span class="card-more" @click="jumpTo('/home/screen/gzctzl')">详情 →</span>
          </div>
          <div class="type-chart-area">
            <div v-for="t in domainStats" :key="t.type" class="type-row">
              <div class="type-head">
                <span class="type-dot" :style="{background: t.color}"></span>
                <span class="type-name">{{ t.type }}</span>
                <span class="type-count">{{ t.count }}项</span>
              </div>
              <div class="type-bar-bg">
                <div class="type-bar-fill" :style="{width: t.ratio + '%', background: t.color}"></div>
              </div>
            </div>
          </div>
          <div class="type-summary">
            <span>覆盖监管指标共 <b>{{ totalItems }}</b> 项</span>
          </div>
        </div>
      </el-col>

      <!-- 监管快报 -->
      <el-col :span="7">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-document" /> 监管快报</span>
            <span class="card-more" @click="jumpTo('/monitor/monitorReport/index')">更多 →</span>
          </div>
          <div class="report-list">
            <div v-for="r in reportList" :key="r.id" class="report-item" @click="jumpTo(r.route)">
              <div class="report-left">
                <div class="report-tag" :style="{background: r.tagBg, color: r.tagColor}">{{ r.tag }}</div>
                <div class="report-info">
                  <div class="report-title">{{ r.title }}</div>
                  <div class="report-meta">{{ r.time }} · {{ r.author }}</div>
                </div>
              </div>
              <i class="el-icon-arrow-right report-arrow" />
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- ========== 重点监管进度 + 功能模块 ========== -->
    <el-row :gutter="14" class="content-row">
      <el-col :span="12">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-data-line" /> 重点穿透监管进度</span>
            <span class="card-more" @click="jumpTo('/stateAssets/dashboard/index')">全部 →</span>
          </div>
          <div class="contract-progress-list">
            <div v-for="p in keyProgresses" :key="p.id" class="cpl-row" @click="jumpTo(p.route)">
              <div class="cpl-row-top">
                <span class="cpl-row-name">{{ p.name }}</span>
                <div class="cpl-row-tags">
                  <el-tag v-if="p.abnormal > 0" type="danger" size="mini" effect="dark">异常{{ p.abnormal }}项</el-tag>
                  <el-tag :type="p.rate >= 80 ? 'success' : p.rate >= 50 ? 'warning' : 'danger'" size="mini">完成{{ p.rate }}%</el-tag>
                </div>
              </div>
              <div class="cpl-row-bar">
                <div class="cpl-bar-bg">
                  <div class="cpl-bar-fill" :style="{width: p.rate + '%', background: p.rate >= 80 ? '#52C41A' : p.rate >= 50 ? '#FA8C16' : '#FF4D4F'}"></div>
                </div>
              </div>
              <div class="cpl-row-info">
                <span><i class="el-icon-office-building" /> {{ p.scope }}</span>
                <span><i class="el-icon-s-check" /> 覆盖{{ p.coverage }}家</span>
                <span><i class="el-icon-warning" /> 风险{{ p.riskCount }}项</span>
              </div>
            </div>
          </div>
        </div>
      </el-col>

      <el-col :span="12">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-grid" /> 全部功能模块</span>
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
    </el-row>

    <!-- ========== 智能分析 + 监管配置 ========== -->
    <el-row :gutter="14" class="content-row">
      <el-col :span="12">
        <div class="card-panel ai-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-cpu" /> 智能分析与决策支持</span>
            <span class="card-more" @click="jumpTo('/intelligent/dataAnalysis/index')">进入 →</span>
          </div>
          <div class="ai-grid">
            <div v-for="ai in aiFeatures" :key="ai.name" class="ai-card" @click="jumpTo(ai.route)">
              <div class="ai-icon" :style="{background: ai.bgColor}">
                <i :class="ai.icon" :style="{color: ai.iconColor}" />
              </div>
              <div class="ai-body">
                <div class="ai-name">{{ ai.name }}</div>
                <div class="ai-desc">{{ ai.desc }}</div>
              </div>
              <i class="el-icon-arrow-right ai-arrow" />
            </div>
          </div>
        </div>
      </el-col>

      <el-col :span="12">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-setting" /> 监管配置与数据协同</span>
          </div>
          <div class="lib-grid">
            <div v-for="lib in configItems" :key="lib.name" class="lib-card" @click="jumpTo(lib.route)">
              <div class="lib-icon-wrap" :style="{background: lib.bgColor}">
                <i :class="lib.icon" :style="{color: lib.iconColor}" />
              </div>
              <div class="lib-body">
                <div class="lib-name">{{ lib.name }}</div>
                <div class="lib-desc">{{ lib.desc }}</div>
              </div>
              <i class="el-icon-arrow-right lib-arrow" />
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { investThemeMixin } from '@/views/stateAssets/themeMixin'

export default {
  name: 'StateAssetsSmartHome',
  mixins: [investThemeMixin],
  data() {
    return {
      userName: '',
      todoActive: 'risk',
      // ===== 6层平铺拓扑图全局配置 =====
      // 节点布局：colW/rowH=单格大小, nodeW/nodeH=节点大小, laneLabel=泳道标签宽度
      lfCfg: { colW: 150, rowH: 100, nodeW: 130, nodeH: 72, padX: 16, padY: 16, laneLabelW: 72, laneGap: 14 },
      // 6层定义（静态，laneTop/laneH 由 computed 计算）
      lfLayers_def: [
        { id: 'industry', name: '行业领域',     icon: 'el-icon-s-grid',        color: '#13C2C2', rowStart: 0, rows: 2 },
        { id: 'manage',   name: '经营领域',     icon: 'el-icon-data-analysis', color: '#1890FF', rowStart: 2, rows: 3 },
        { id: 'carrier',  name: '载体',         icon: 'el-icon-document',      color: '#EB2F96', rowStart: 5, rows: 3 },
        { id: 'mgmt',     name: '管理领域',     icon: 'el-icon-setting',       color: '#722ED1', rowStart: 8, rows: 3 },
        { id: 'data',     name: '内外部数据融合', icon: 'el-icon-connection',    color: '#FA8C16', rowStart: 11, rows: 2 },
        { id: 'warn',     name: '预警处置',     icon: 'el-icon-warning',       color: '#FF4D4F', rowStart: 13, rows: 2 },
      ],
      // 所有节点：每个节点用 gCol/gRow 在全局网格中定位
      // gRow = 对应层的 rowStart + 层内偏移行
      // gCol = 全局列索引（整个画布共用列网格）
      lfNodes: [
        // ==================== 层 0：行业领域 ====================
        // 行业穿透：12页  gRow:0-1
        { id:'ind_home',  name:'行业穿透首页',  gCol:0,  gRow:0, icon:'el-icon-s-home',         kind:'main', route:'/stateAssets/industryPenetration/home/index' },
        { id:'ind_lay',   name:'行业布局台账',  gCol:1,  gRow:0, icon:'el-icon-tickets',        kind:'main', route:'/stateAssets/industryPenetration/industryLayout' },
        { id:'ind_dash',  name:'监控驾驶舡',    gCol:2,  gRow:0, icon:'el-icon-s-platform',     kind:'main', route:'/stateAssets/industryPenetration/dashboard' },
        { id:'ind_eng',   name:'能源行业',      gCol:0,  gRow:1, icon:'el-icon-lightning',      kind:'main', route:'/stateAssets/industryPenetration/energy' },
        { id:'ind_fin',   name:'金融行业',      gCol:1,  gRow:1, icon:'el-icon-bank-card',      kind:'main', route:'/stateAssets/industryPenetration/financial' },
        { id:'ind_mfg',   name:'制造行业',      gCol:2,  gRow:1, icon:'el-icon-s-tools',        kind:'main', route:'/stateAssets/industryPenetration/manufacturing' },
        { id:'ind_infra', name:'基础设施',      gCol:3,  gRow:1, icon:'el-icon-office-building', kind:'main', route:'/stateAssets/industryPenetration/infrastructure' },
        { id:'ind_pub',   name:'公共服务',      gCol:4,  gRow:1, icon:'el-icon-s-custom',       kind:'main', route:'/stateAssets/industryPenetration/publicService' },
        { id:'ind_comp',  name:'竞争力分析',    gCol:5,  gRow:1, icon:'el-icon-data-analysis',  kind:'sub',  route:'/stateAssets/industryPenetration/competitiveness' },
        { id:'ind_syn',   name:'产业协同分析',  gCol:6,  gRow:1, icon:'el-icon-share',          kind:'sub',  route:'/stateAssets/industryPenetration/synergy' },
        { id:'ind_drill', name:'行业穿透分析',  gCol:7,  gRow:1, icon:'el-icon-connection',     kind:'main', route:'/stateAssets/industryPenetration/drillDown' },
        { id:'ind_warn',  name:'风险预警管理',  gCol:8,  gRow:1, icon:'el-icon-warning',        kind:'warn', route:'/stateAssets/industryPenetration/riskWarning', alert:true },
        // ==================== 层 1：经营领域 ====================
        // 投资穿透 (gRow:2-4, gCol:0-3)
        { id:'i_home',    name:'投资穿透首页',  gCol:0,  gRow:2, icon:'el-icon-s-home',         kind:'main', route:'/stateAssets/investPenetration/home/index' },
        { id:'i_proj',    name:'投资项目台账',  gCol:1,  gRow:2, icon:'el-icon-tickets',        kind:'main', route:'/stateAssets/investPenetration/project/index' },
        { id:'i_dash',    name:'投资驾驶舡',    gCol:2,  gRow:2, icon:'el-icon-s-platform',     kind:'main', route:'/stateAssets/investPenetration/dashboard/index' },
        { id:'i_comp',    name:'决策合规追踪',  gCol:3,  gRow:2, icon:'el-icon-s-check',        kind:'main', route:'/stateAssets/investPenetration/compliance/index' },
        { id:'i_prog',    name:'投资进度监控',  gCol:0,  gRow:3, icon:'el-icon-timer',          kind:'sub',  route:'/stateAssets/investPenetration/progressMonitor/index' },
        { id:'i_post',    name:'投后评价管理',  gCol:1,  gRow:3, icon:'el-icon-s-data',         kind:'sub',  route:'/stateAssets/investPenetration/postEval/index' },
        { id:'i_nonmain', name:'非主业投资分析', gCol:2, gRow:3, icon:'el-icon-warning-outline', kind:'sub',  route:'/stateAssets/investPenetration/nonMainBiz/index' },
        { id:'i_drill',   name:'投资穿透分析',  gCol:3,  gRow:3, icon:'el-icon-data-analysis',  kind:'main', route:'/stateAssets/investPenetration/drillDown/index' },
        { id:'i_warn',    name:'投资风险预警',  gCol:3,  gRow:4, icon:'el-icon-warning',        kind:'warn', route:'/stateAssets/investPenetration/riskWarning/index', alert:true },
        // 金融穿透 (gRow:2-4, gCol:5-12)
        { id:'eq_home',   name:'金融穿透首页',  gCol:5,  gRow:2, icon:'el-icon-s-home',         kind:'main', route:'/stateAssets/equityPenetration/home/index' },
        { id:'eq_owner',  name:'实际控制人',    gCol:6,  gRow:2, icon:'el-icon-user',           kind:'main', route:'/stateAssets/equityPenetration/beneficialOwner/index' },
        { id:'eq_chain',  name:'控制链分析',    gCol:7,  gRow:2, icon:'el-icon-share',          kind:'main', route:'/stateAssets/equityPenetration/controlChain/index' },
        { id:'eq_struct', name:'股权结构',      gCol:8,  gRow:2, icon:'el-icon-connection',     kind:'main', route:'/stateAssets/equityPenetration/equityStructure/index' },
        { id:'eq_holder', name:'股东穿透分析',  gCol:9,  gRow:2, icon:'el-icon-s-custom',       kind:'sub',  route:'/stateAssets/equityPenetration/shareholderAnalysis/index' },
        { id:'eq_change', name:'股权变动',      gCol:10, gRow:2, icon:'el-icon-refresh',        kind:'sub',  route:'/stateAssets/equityPenetration/equityChanges/index' },
        { id:'eq_cca',    name:'控制链统计',    gCol:11, gRow:2, icon:'el-icon-data-line',      kind:'sub',  route:'/stateAssets/equityPenetration/controlChainAnalysis/index' },
        { id:'eq_invest', name:'投资项目',      gCol:12, gRow:2, icon:'el-icon-folder-add',     kind:'sub',  route:'/stateAssets/equityPenetration/investmentDecision/index' },
        { id:'eq_fin',    name:'融资记录台账',  gCol:5,  gRow:3, icon:'el-icon-bank-card',      kind:'finance', route:'/stateAssets/financialRiskPenetration/financing/index' },
        { id:'eq_guar',   name:'担保记录台账',  gCol:6,  gRow:3, icon:'el-icon-s-order',        kind:'finance', route:'/stateAssets/financialRiskPenetration/guarantee/index' },
        { id:'eq_loan',   name:'委托贷款监控',  gCol:7,  gRow:3, icon:'el-icon-money',          kind:'finance', route:'/stateAssets/financialRiskPenetration/entrustedLoan/index' },
        { id:'eq_deriv',  name:'衍生品监控',    gCol:8,  gRow:3, icon:'el-icon-s-finance',      kind:'finance', route:'/stateAssets/financialRiskPenetration/derivatives/index' },
        { id:'eq_liq',    name:'流动性风险',    gCol:9,  gRow:3, icon:'el-icon-warning',        kind:'warn',    route:'/stateAssets/financialRiskPenetration/liquidity/index', alert:true },
        { id:'eq_drill',  name:'金融风险穿透分析', gCol:10, gRow:3, icon:'el-icon-data-analysis',  kind:'warn',    route:'/stateAssets/financialRiskPenetration/drillDown/index' },
        { id:'eq_dash',   name:'金融风险驾驶舡', gCol:11, gRow:3, icon:'el-icon-s-platform',     kind:'main',    route:'/stateAssets/financialRiskPenetration/dashboard/index' },
        // 采购穿透 (gRow:2-4, gCol: 14-20)
        { id:'pr_home',   name:'采购穿透首页',  gCol:14, gRow:2, icon:'el-icon-s-home',         kind:'main', route:'/stateAssets/procurementPenetration/home/index' },
        { id:'pr_proj',   name:'采购项目台账',  gCol:15, gRow:2, icon:'el-icon-tickets',        kind:'main', route:'/stateAssets/procurementPenetration/project/index' },
        { id:'pr_rec',    name:'采购台账',      gCol:16, gRow:2, icon:'el-icon-document',       kind:'main', route:'/stateAssets/procurementPenetration/purchaseRecord' },
        { id:'pr_sup',    name:'供应商台账',      gCol:14, gRow:3, icon:'el-icon-s-cooperation',  kind:'sub',  route:'/stateAssets/procurementPenetration/supplier/index' },
        { id:'pr_sprof',  name:'供应商档案',      gCol:15, gRow:3, icon:'el-icon-folder',         kind:'sub',  route:'/stateAssets/procurementPenetration/supplierProfile' },
        { id:'pr_bid',    name:'招标过程监控',  gCol:16, gRow:3, icon:'el-icon-view',           kind:'main', route:'/stateAssets/procurementPenetration/biddingMonitor/index' },
        { id:'pr_bidc',   name:'招投标合规',      gCol:17, gRow:3, icon:'el-icon-s-check',        kind:'sub',  route:'/stateAssets/procurementPenetration/biddingCompliance' },
        { id:'pr_price',  name:'采购价格对标',  gCol:18, gRow:3, icon:'el-icon-data-line',      kind:'sub',  route:'/stateAssets/procurementPenetration/priceBenchmark/index' },
        { id:'pr_rel',    name:'关联交易监控',  gCol:14, gRow:4, icon:'el-icon-connection',     kind:'finance', route:'/stateAssets/procurementPenetration/relatedTransaction' },
        { id:'pr_fake',   name:'虚假贸易核查',  gCol:15, gRow:4, icon:'el-icon-warning-outline', kind:'warn', route:'/stateAssets/procurementPenetration/fakeTrade/index' },
        { id:'pr_risk',   name:'供应链风险',      gCol:16, gRow:4, icon:'el-icon-warning',        kind:'warn', route:'/stateAssets/procurementPenetration/supplyChainRisk/index' },
        { id:'pr_drill',  name:'采购风险穿透',  gCol:17, gRow:4, icon:'el-icon-data-analysis',  kind:'main', route:'/stateAssets/procurementPenetration/drillDown' },
        { id:'pr_dash',   name:'采购驾驶舡',    gCol:18, gRow:4, icon:'el-icon-s-platform',     kind:'main', route:'/stateAssets/procurementPenetration/dashboard/index' },
        // 军品穿透 (gRow:2-4, gCol:20-25)
        { id:'ml_home',   name:'军品穿透首页',  gCol:20, gRow:2, icon:'el-icon-s-home',         kind:'main', route:'/stateAssets/militaryPenetration/home/index' },
        { id:'ml_task',   name:'军品任务台账',  gCol:21, gRow:2, icon:'el-icon-s-flag',         kind:'main', route:'/stateAssets/militaryPenetration/task/index' },
        { id:'ml_taskr',  name:'任务台账',      gCol:22, gRow:2, icon:'el-icon-tickets',        kind:'main', route:'/stateAssets/militaryPenetration/taskRecord/index' },
        { id:'ml_qual',   name:'军品资质管理',  gCol:20, gRow:3, icon:'el-icon-s-check',        kind:'main', route:'/stateAssets/militaryPenetration/qualification/index' },
        { id:'ml_qualp',  name:'资质档案',      gCol:21, gRow:3, icon:'el-icon-folder',         kind:'sub',  route:'/stateAssets/militaryPenetration/qualificationProfile/index' },
        { id:'ml_sec',    name:'军品保密管理',  gCol:22, gRow:3, icon:'el-icon-lock',           kind:'warn', route:'/stateAssets/militaryPenetration/securityMgmt/index' },
        { id:'ml_qual2',  name:'军品质量管理',  gCol:20, gRow:4, icon:'el-icon-s-order',        kind:'main', route:'/stateAssets/militaryPenetration/qualityMgmt/index' },
        { id:'ml_sub',    name:'分包合规',      gCol:21, gRow:4, icon:'el-icon-document-checked', kind:'sub',  route:'/stateAssets/militaryPenetration/subcontractCompliance/index' },
        { id:'ml_sc',     name:'供应链安全',      gCol:22, gRow:4, icon:'el-icon-connection',     kind:'sub',  route:'/stateAssets/militaryPenetration/supplyChainSecurity/index' },
        { id:'ml_exec',   name:'合同履约追踪',  gCol:23, gRow:4, icon:'el-icon-finished',       kind:'finance', route:'/stateAssets/militaryPenetration/contractExecution/index' },
        { id:'ml_drill',  name:'军品风险穿透',  gCol:24, gRow:4, icon:'el-icon-data-analysis',  kind:'warn', route:'/stateAssets/militaryPenetration/drillDown/index' },
        { id:'ml_dash',   name:'军品驾驶舡',    gCol:25, gRow:4, icon:'el-icon-s-platform',     kind:'main', route:'/stateAssets/militaryPenetration/dashboard/index' },
        // 境外穿透 (gRow:2-4, gCol:27-34)
        { id:'ov_home',   name:'境外穿透首页',  gCol:27, gRow:2, icon:'el-icon-s-home',         kind:'main', route:'/stateAssets/overseasPenetration/home/index' },
        { id:'ov_unit',   name:'境外单位台账',  gCol:28, gRow:2, icon:'el-icon-office-building', kind:'main', route:'/stateAssets/overseasPenetration/unit/index' },
        { id:'ov_edash',  name:'企业管理驾驶舡', gCol:29, gRow:2, icon:'el-icon-s-data',          kind:'sub',  route:'/enterprise/dashboard/index' },
        { id:'ov_lead',   name:'负责人信息',      gCol:30, gRow:2, icon:'el-icon-user',           kind:'sub',  route:'/leader/management/index' },
        { id:'ov_eval',   name:'负责人考核',      gCol:31, gRow:2, icon:'el-icon-s-check',        kind:'sub',  route:'/leader/evaluation/index' },
        { id:'ov_fin',    name:'财务报表',      gCol:27, gRow:3, icon:'el-icon-money',          kind:'finance', route:'/enterprise/financial/index' },
        { id:'ov_hr',     name:'人力资源',      gCol:28, gRow:3, icon:'el-icon-user',           kind:'finance', route:'/enterprise/hr/index' },
        { id:'ov_op',     name:'生产经营',      gCol:29, gRow:3, icon:'el-icon-s-order',        kind:'finance', route:'/enterprise/operation/index' },
        { id:'ov_innov',  name:'技术创新',      gCol:30, gRow:3, icon:'el-icon-magic-stick',    kind:'sub',  route:'/enterprise/innovation/index' },
        { id:'ov_strat',  name:'战略管理',      gCol:31, gRow:3, icon:'el-icon-s-grid',         kind:'sub',  route:'/enterprise/strategy/index' },
        { id:'ov_invest', name:'境外投资管理',  gCol:27, gRow:4, icon:'el-icon-data-analysis',  kind:'main', route:'/stateAssets/overseasPenetration/investMgmt/index' },
        { id:'ov_crisk',  name:'国别风险地图',  gCol:28, gRow:4, icon:'el-icon-map-location',   kind:'warn', route:'/stateAssets/overseasPenetration/countryRiskMap/index', alert:true },
        { id:'ov_forex',  name:'外汇风险分析',  gCol:29, gRow:4, icon:'el-icon-s-finance',      kind:'warn', route:'/stateAssets/overseasPenetration/forexAnalysis/index' },
        { id:'ov_comp',   name:'境外合规管理',  gCol:30, gRow:4, icon:'el-icon-document-checked', kind:'sub', route:'/stateAssets/overseasPenetration/complianceMgmt/index' },
        { id:'ov_opanal', name:'境外经营分析',  gCol:31, gRow:4, icon:'el-icon-data-line',      kind:'main', route:'/stateAssets/overseasPenetration/operationAnalysis/index' },
        { id:'ov_safe',   name:'人员安全管理',  gCol:32, gRow:4, icon:'el-icon-lock',           kind:'warn', route:'/stateAssets/overseasPenetration/personnelSafety/index' },
        { id:'ov_emg',    name:'应急指挥中心',  gCol:33, gRow:4, icon:'el-icon-phone',          kind:'warn', route:'/stateAssets/overseasPenetration/emergencyCommand/index' },
        { id:'ov_dash',   name:'境外驾驶舡',    gCol:34, gRow:4, icon:'el-icon-s-platform',     kind:'main', route:'/stateAssets/overseasPenetration/dashboard/index' },
        // ==================== 层 2：载体 ====================
        // 合同穿透 (gRow:5-7, gCol:0-9)
        { id:'ct_home',   name:'合同穿透首页',  gCol:0,  gRow:5, icon:'el-icon-s-home',         kind:'main', route:'/stateAssets/contractPenetration/home/index' },
        { id:'ct_rec',    name:'合同记录台账',  gCol:1,  gRow:5, icon:'el-icon-tickets',        kind:'main', route:'/stateAssets/contractPenetration/record/index' },
        { id:'ct_list',   name:'合同台账管理',  gCol:2,  gRow:5, icon:'el-icon-document',       kind:'main', route:'/stateAssets/contractPenetration/contractList' },
        { id:'ct_dash',   name:'合同驾驶舡',    gCol:3,  gRow:5, icon:'el-icon-s-platform',     kind:'main', route:'/stateAssets/contractPenetration/dashboard/index' },
        { id:'ct_appr',   name:'合同审批追踪',  gCol:0,  gRow:6, icon:'el-icon-s-check',        kind:'main', route:'/stateAssets/contractPenetration/approvalTrack/index' },
        { id:'ct_comp',   name:'审批合规追踪',  gCol:1,  gRow:6, icon:'el-icon-document-checked', kind:'sub',  route:'/stateAssets/contractPenetration/compliance' },
        { id:'ct_lc',     name:'合同生命周期',  gCol:2,  gRow:6, icon:'el-icon-time',           kind:'sub',  route:'/stateAssets/contractPenetration/lifecycle/index' },
        { id:'ct_perf',   name:'合同履行监控',  gCol:3,  gRow:6, icon:'el-icon-s-order',        kind:'main', route:'/stateAssets/contractPenetration/performanceMonitor/index' },
        { id:'ct_exec',   name:'履行监控',      gCol:4,  gRow:6, icon:'el-icon-finished',       kind:'main', route:'/stateAssets/contractPenetration/execution' },
        { id:'ct_credit', name:'对方信用分析',  gCol:5,  gRow:6, icon:'el-icon-medal',          kind:'finance', route:'/stateAssets/contractPenetration/creditAnalysis/index' },
        { id:'ct_counter',name:'对方信用监控',  gCol:6,  gRow:6, icon:'el-icon-user',           kind:'finance', route:'/stateAssets/contractPenetration/counterparty' },
        { id:'ct_warn',   name:'风险预警管理',  gCol:7,  gRow:6, icon:'el-icon-warning',        kind:'warn', route:'/stateAssets/contractPenetration/riskWarning', alert:true },
        { id:'ct_disp',   name:'合同纠纷台账',  gCol:0,  gRow:7, icon:'el-icon-warning-outline', kind:'warn', route:'/stateAssets/contractPenetration/dispute/index' },
        { id:'ct_case',   name:'法律纠纷案件',  gCol:1,  gRow:7, icon:'el-icon-s-flag',         kind:'warn', route:'/stateAssets/contractPenetration/caseManagement/index' },
        { id:'ct_smart',  name:'合同智能审查',  gCol:2,  gRow:7, icon:'el-icon-cpu',            kind:'sub',  route:'/stateAssets/contractPenetration/smartReview/index' },
        { id:'ct_drill',  name:'合同穿透分析',  gCol:3,  gRow:7, icon:'el-icon-data-analysis',  kind:'main', route:'/stateAssets/contractPenetration/drillDown' },
        // 资金穿透 (gRow:5-7, gCol:11-18)
        { id:'fu_home',   name:'资金穿透首页',  gCol:11, gRow:5, icon:'el-icon-s-home',         kind:'main', route:'/stateAssets/fundPenetration/home/index' },
        { id:'fu_skdata', name:'司库数据',      gCol:12, gRow:5, icon:'el-icon-bank-card',      kind:'finance', route:'/finance/tableQuery' },
        { id:'fu_cwdata', name:'财务数据',      gCol:13, gRow:5, icon:'el-icon-money',          kind:'finance', route:'/finance/tableQuery' },
        { id:'fu_htdata', name:'合同数据',      gCol:14, gRow:5, icon:'el-icon-document',       kind:'finance', route:'/finance/tableQuery' },
        { id:'fu_fpdata', name:'发票数据',      gCol:15, gRow:5, icon:'el-icon-document-checked', kind:'finance', route:'/finance/tableQuery' },
        { id:'fu_hrdata', name:'人员数据',      gCol:16, gRow:5, icon:'el-icon-user',           kind:'finance', route:'/finance/tableQuery' },
        { id:'fu_sjdata', name:'司法数据穿透',  gCol:11, gRow:6, icon:'el-icon-s-flag',         kind:'sub',  route:'/workbench/industryData/telescope' },
        { id:'fu_portrait',name:'资金穿透画像', gCol:12, gRow:6, icon:'el-icon-s-custom',       kind:'main', route:'/risk/home/enterpriseProfile' },
        { id:'fu_dp',     name:'数据穿透',      gCol:13, gRow:6, icon:'el-icon-connection',     kind:'main', route:'/home/screen/dataPortrait/index' },
        { id:'fu_dsrc',   name:'数据源管理',    gCol:14, gRow:6, icon:'el-icon-folder',         kind:'sub',  route:'/risk/base/sjygl' },
        { id:'fu_model',  name:'数据模型管理',  gCol:11, gRow:7, icon:'el-icon-s-grid',         kind:'main', route:'/risk/mxgl/sjmxgl' },
        { id:'fu_pgmodel',name:'评估模型',      gCol:12, gRow:7, icon:'el-icon-s-opportunity',  kind:'main', route:'/risk/mxgl/pgmxgl' },
        { id:'fu_warn',   name:'资金风险预警',  gCol:13, gRow:7, icon:'el-icon-warning',        kind:'warn', route:'/risk/mxgl/fxyj', alert:true },
        { id:'fu_wdata',  name:'预警数据',      gCol:14, gRow:7, icon:'el-icon-data-line',      kind:'warn', route:'/risk/mxgl/fxyjgl' },
        // ==================== 层 3：管理领域 ====================
        // 会计穿透 (gRow:8-10, gCol:0-8)
        { id:'ac_home',   name:'会计穿透首页',  gCol:0,  gRow:8, icon:'el-icon-s-home',         kind:'main', route:'/stateAssets/accountingPenetration/home/index' },
        { id:'ac_bookd',  name:'账簿数据',      gCol:1,  gRow:8, icon:'el-icon-document',       kind:'finance', route:'/cwsc/jcpz/companyData/ZNFXaccountData' },
        { id:'ac_stmtd',  name:'报表数据',      gCol:2,  gRow:8, icon:'el-icon-data-board',     kind:'finance', route:'/cwsc/jcpz/companyData/statement' },
        { id:'ac_subj',   name:'科目表',       gCol:3,  gRow:8, icon:'el-icon-tickets',        kind:'finance', route:'/cwsc/jcpz/companyData/sub/subject' },
        { id:'ac_bal',    name:'余额表',       gCol:4,  gRow:8, icon:'el-icon-s-data',         kind:'finance', route:'/cwsc/jcpz/companyData/sub/balance' },
        { id:'ac_dash',   name:'会计驾驶舡',    gCol:5,  gRow:8, icon:'el-icon-s-platform',     kind:'main', route:'/stateAssets/accountingPenetration/dashboard/index' },
        { id:'ac_vouch',  name:'凭证穿透',      gCol:0,  gRow:9, icon:'el-icon-document-checked', kind:'main', route:'/stateAssets/accountingPenetration/voucherPenetration/index' },
        { id:'ac_vlib',   name:'凭证库',       gCol:1,  gRow:9, icon:'el-icon-files',          kind:'sub',  route:'/cwsc/jcpz/companyData/sub/voucherLib' },
        { id:'ac_book',   name:'账簿穿透查询',  gCol:2,  gRow:9, icon:'el-icon-notebook-1',     kind:'main', route:'/stateAssets/accountingPenetration/bookPenetration/index' },
        { id:'ac_gcate',  name:'总分类账',      gCol:3,  gRow:9, icon:'el-icon-reading',        kind:'sub',  route:'/cwsc/jcpz/companyData/sub/accountCate' },
        { id:'ac_detail', name:'明细账',       gCol:4,  gRow:9, icon:'el-icon-document',       kind:'sub',  route:'/cwsc/jcpz/companyData/sub/accountDetail' },
        { id:'ac_diary',  name:'日记账',       gCol:5,  gRow:9, icon:'el-icon-date',           kind:'sub',  route:'/cwsc/jcpz/companyData/sub/accountDiary' },
        { id:'ac_assist', name:'辅助账',       gCol:6,  gRow:9, icon:'el-icon-s-cooperation',  kind:'sub',  route:'/cwsc/jcpz/companyData/sub/accountAssist' },
        { id:'ac_rept',   name:'财务报表穿透',  gCol:0,  gRow:10, icon:'el-icon-pie-chart',      kind:'main', route:'/stateAssets/accountingPenetration/reportPenetration/index' },
        { id:'ac_budget', name:'预算执行监管',  gCol:1,  gRow:10, icon:'el-icon-coin',           kind:'main', route:'/stateAssets/accountingPenetration/budgetMonitor/index' },
        { id:'ac_twog',   name:'“两金”压降监控',  gCol:2,  gRow:10, icon:'el-icon-s-finance',      kind:'warn', route:'/stateAssets/accountingPenetration/twoGoldMonitor/index' },
        { id:'ac_fraud',  name:'财务造假识别',  gCol:3,  gRow:10, icon:'el-icon-warning',        kind:'warn', route:'/stateAssets/accountingPenetration/fraudDetection/index', alert:true },
        { id:'ac_glass',  name:'放大镜',       gCol:4,  gRow:10, icon:'el-icon-search',         kind:'sub',  route:'/workbench/auditTools/magnifyingGlass' },
        // 财务穿透 (gRow:8-10, gCol:10-17)
        { id:'fi_home',   name:'财务穿透首页',  gCol:10, gRow:8, icon:'el-icon-s-home',         kind:'main', route:'/stateAssets/financialPenetration/home/index' },
        { id:'fi_stmt',   name:'财务报表台账',  gCol:11, gRow:8, icon:'el-icon-document',       kind:'main', route:'/stateAssets/financePenetration/report/index' },
        { id:'fi_cons',   name:'合并财务分析',  gCol:12, gRow:8, icon:'el-icon-data-analysis',  kind:'main', route:'/stateAssets/financialPenetration/consolidatedAnalysis/index' },
        { id:'fi_fund',   name:'资金流向分析',  gCol:13, gRow:8, icon:'el-icon-coin',           kind:'main', route:'/stateAssets/financialPenetration/fundFlow/index' },
        { id:'fi_dash',   name:'财务驾驶舡',    gCol:14, gRow:8, icon:'el-icon-s-platform',     kind:'main', route:'/stateAssets/financePenetration/dashboard/index' },
        { id:'fi_bench',  name:'财务指标对标',  gCol:10, gRow:9, icon:'el-icon-data-line',      kind:'sub',  route:'/stateAssets/financePenetration/benchmark/index' },
        { id:'fi_rel',    name:'关联交易分析',  gCol:11, gRow:9, icon:'el-icon-connection',     kind:'finance', route:'/stateAssets/financialPenetration/relatedTransaction/index' },
        { id:'fi_relp',   name:'关联交易台账',  gCol:12, gRow:9, icon:'el-icon-tickets',        kind:'finance', route:'/stateAssets/financePenetration/relatedTransaction/index' },
        { id:'fi_exp',    name:'费用管控',      gCol:13, gRow:9, icon:'el-icon-wallet',         kind:'sub',  route:'/stateAssets/financialPenetration/expenseMonitor/index' },
        { id:'fi_comp',   name:'财务合规',      gCol:10, gRow:10, icon:'el-icon-document-checked', kind:'sub',  route:'/stateAssets/financialPenetration/financialCompliance/index' },
        { id:'fi_peval',  name:'财务绩效评价',  gCol:11, gRow:10, icon:'el-icon-s-check',        kind:'sub',  route:'/stateAssets/financialPenetration/financialPerformance/index' },
        { id:'fi_risk',   name:'财务风险统计',  gCol:12, gRow:10, icon:'el-icon-warning-outline', kind:'warn', route:'/stateAssets/financialPenetration/financialRisk/index' },
        { id:'fi_anomaly',name:'异常检测',      gCol:13, gRow:10, icon:'el-icon-warning',        kind:'warn', route:'/stateAssets/financePenetration/anomaly/index', alert:true },
        { id:'fi_drill',  name:'财务穿透分析',  gCol:14, gRow:10, icon:'el-icon-data-analysis',  kind:'main', route:'/stateAssets/financialPenetration/drillDown/index' },
        // 薄酬穿透 (gRow:8-10, gCol:19-23)
        { id:'sa_home',   name:'薄酬穿透首页',  gCol:19, gRow:8, icon:'el-icon-s-home',         kind:'main', route:'/stateAssets/salaryPenetration/home/index' },
        { id:'sa_total',  name:'薄酬总额台账',  gCol:20, gRow:8, icon:'el-icon-tickets',        kind:'main', route:'/stateAssets/salaryPenetration/total/index' },
        { id:'sa_totlist',name:'工资总额管理',  gCol:21, gRow:8, icon:'el-icon-document',       kind:'main', route:'/stateAssets/salaryPenetration/totalList' },
        { id:'sa_dash',   name:'薄酬驾驶舡',    gCol:22, gRow:8, icon:'el-icon-s-platform',     kind:'main', route:'/stateAssets/salaryPenetration/dashboard/index' },
        { id:'sa_exec',   name:'高管薄酬分析',  gCol:19, gRow:9, icon:'el-icon-user',           kind:'main', route:'/stateAssets/salaryPenetration/executivePay/index' },
        { id:'sa_link',   name:'效益联动分析',  gCol:20, gRow:9, icon:'el-icon-data-line',      kind:'main', route:'/stateAssets/salaryPenetration/performanceLink/index' },
        { id:'sa_incentive',name:'中长期激励',     gCol:21, gRow:9, icon:'el-icon-s-opportunity',  kind:'sub',  route:'/stateAssets/salaryPenetration/incentivePlan/index' },
        { id:'sa_labor',  name:'人工成本分析',  gCol:19, gRow:10, icon:'el-icon-coin',           kind:'finance', route:'/stateAssets/salaryPenetration/laborCost/index' },
        { id:'sa_comply', name:'薄酬合规检查',  gCol:20, gRow:10, icon:'el-icon-document-checked', kind:'sub',  route:'/stateAssets/salaryPenetration/complianceCheck' },
        { id:'sa_warn',   name:'薄酬风险预警',  gCol:21, gRow:10, icon:'el-icon-warning',        kind:'warn', route:'/stateAssets/salaryPenetration/riskWarning', alert:true },
        { id:'sa_drill',  name:'薄酬穿透分析',  gCol:22, gRow:10, icon:'el-icon-data-analysis',  kind:'main', route:'/stateAssets/salaryPenetration/drillDown' },
        // 产权穿透 (gRow:8-10, gCol:25-33)
        { id:'pp_home',   name:'产权穿透首页',  gCol:25, gRow:8, icon:'el-icon-s-home',         kind:'main', route:'/stateAssets/propertyPenetration/home' },
        { id:'pp_conc',   name:'资产集中度',    gCol:26, gRow:8, icon:'el-icon-s-grid',         kind:'sub',  route:'/stateAssets/assetPenetration/assetConcentration/index' },
        { id:'pp_flow',   name:'资产流向追踪',  gCol:27, gRow:8, icon:'el-icon-connection',     kind:'main', route:'/stateAssets/assetPenetration/assetFlow/index' },
        { id:'pp_cross',  name:'交叉持股分析',  gCol:28, gRow:8, icon:'el-icon-share',          kind:'sub',  route:'/stateAssets/assetPenetration/crossHolding/index' },
        { id:'pp_alloc',  name:'资产配置统计',  gCol:29, gRow:8, icon:'el-icon-data-line',      kind:'sub',  route:'/stateAssets/assetPenetration/assetAllocation/index' },
        { id:'pp_dash',   name:'产权驾驶舡',    gCol:30, gRow:8, icon:'el-icon-s-platform',     kind:'main', route:'/stateAssets/propertyPenetration/dashboard/index' },
        { id:'pp_eqchart',name:'股权穿透图',      gCol:25, gRow:9, icon:'el-icon-s-custom',       kind:'main', route:'/stateAssets/propertyPenetration/equityChart/index' },
        { id:'pp_eqstruct',name:'股权结构分析', gCol:26, gRow:9, icon:'el-icon-connection',     kind:'main', route:'/stateAssets/equityPenetration/equityStructure/index' },
        { id:'pp_chain',  name:'控制链分析',    gCol:27, gRow:9, icon:'el-icon-share',          kind:'main', route:'/stateAssets/equityPenetration/controlChain/index' },
        { id:'pp_owner',  name:'受益所有人',    gCol:28, gRow:9, icon:'el-icon-user',           kind:'sub',  route:'/stateAssets/equityPenetration/beneficialOwner/index' },
        { id:'pp_holder', name:'股东分析',      gCol:29, gRow:9, icon:'el-icon-s-check',        kind:'sub',  route:'/stateAssets/equityPenetration/shareholderAnalysis/index' },
        { id:'pp_reg',    name:'产权登记台账',  gCol:25, gRow:10, icon:'el-icon-tickets',        kind:'main', route:'/stateAssets/propertyPenetration/right/index' },
        { id:'pp_reglist',name:'产权登记',       gCol:26, gRow:10, icon:'el-icon-document',       kind:'main', route:'/stateAssets/propertyPenetration/registryList' },
        { id:'pp_change', name:'股权变动记录',  gCol:27, gRow:10, icon:'el-icon-refresh',        kind:'sub',  route:'/stateAssets/equityPenetration/equityChanges/index' },
        { id:'pp_trans',  name:'产权交易台账',  gCol:28, gRow:10, icon:'el-icon-sort',           kind:'main', route:'/stateAssets/propertyPenetration/transaction/index' },
        { id:'pp_review', name:'产权交易审查',  gCol:29, gRow:10, icon:'el-icon-document-checked', kind:'sub',  route:'/stateAssets/propertyPenetration/tradeReview' },
        { id:'pp_share',  name:'参股企业经营',  gCol:30, gRow:10, icon:'el-icon-s-cooperation',  kind:'finance', route:'/stateAssets/propertyPenetration/shareholding/index' },
        // ==================== 层 4：内外部数据融合 ====================
        // (gRow:11-12)
        { id:'df_portrait',name:'资金穿透画像', gCol:0,  gRow:11, icon:'el-icon-s-custom',       kind:'main', route:'/risk/home/enterpriseProfile' },
        { id:'df_dp',     name:'数据穿透',      gCol:1,  gRow:11, icon:'el-icon-connection',     kind:'main', route:'/home/screen/dataPortrait/index' },
        { id:'df_sk',     name:'司库数据',      gCol:2,  gRow:11, icon:'el-icon-bank-card',      kind:'finance', route:'/finance/tableQuery' },
        { id:'df_cw',     name:'财务数据',      gCol:3,  gRow:11, icon:'el-icon-money',          kind:'finance', route:'/finance/tableQuery' },
        { id:'df_ht',     name:'合同数据',      gCol:4,  gRow:11, icon:'el-icon-document',       kind:'finance', route:'/finance/tableQuery' },
        { id:'df_fp',     name:'发票数据',      gCol:5,  gRow:11, icon:'el-icon-document-checked', kind:'finance', route:'/finance/tableQuery' },
        { id:'df_hr',     name:'人员数据',      gCol:6,  gRow:11, icon:'el-icon-user',           kind:'finance', route:'/finance/tableQuery' },
        { id:'df_sj',     name:'司法数据穿透',  gCol:7,  gRow:11, icon:'el-icon-s-flag',         kind:'sub',  route:'/workbench/industryData/telescope' },
        { id:'df_model',  name:'数据模型管理',  gCol:0,  gRow:12, icon:'el-icon-s-grid',         kind:'main', route:'/risk/mxgl/sjmxgl' },
        { id:'df_pgm',    name:'评估模型',      gCol:1,  gRow:12, icon:'el-icon-s-opportunity',  kind:'main', route:'/risk/mxgl/pgmxgl' },
        { id:'df_dsrc',   name:'数据源管理',    gCol:2,  gRow:12, icon:'el-icon-folder',         kind:'sub',  route:'/risk/base/sjygl' },
        // ==================== 层 5：预警处置 ====================
        // (gRow:13-14)
        { id:'wa_risk',   name:'风险预警',      gCol:0,  gRow:13, icon:'el-icon-warning',        kind:'warn', route:'/risk/mxgl/fxyj', alert:true },
        { id:'wa_rdata',  name:'风险预警数据',  gCol:1,  gRow:13, icon:'el-icon-data-line',      kind:'warn', route:'/risk/mxgl/fxyjgl' },
        { id:'wa_issues', name:'问题汇总',      gCol:2,  gRow:13, icon:'el-icon-tickets',        kind:'warn', route:'/audit/collect/auditIssuesCollect' },
        { id:'wa_dash',   name:'监管驾驶舡',    gCol:3,  gRow:13, icon:'el-icon-s-platform',     kind:'main', route:'/stateAssets/dashboard/index' },
        { id:'wa_report', name:'监管报告',      gCol:4,  gRow:13, icon:'el-icon-document',       kind:'main', route:'/monitor/monitorReport/index' },
        { id:'wa_screen', name:'国资穿透总览',  gCol:5,  gRow:13, icon:'el-icon-view',           kind:'main', route:'/home/screen/gzctzl' },
        { id:'wa_collect',name:'问题投密丮案',  gCol:0,  gRow:14, icon:'el-icon-edit',           kind:'sub',  route:'/stateAssets/collaboration/supervisionInstruction/index' },
        { id:'wa_collab', name:'监管指令协同',  gCol:1,  gRow:14, icon:'el-icon-s-promotion',    kind:'sub',  route:'/stateAssets/collaboration/supervisionInstruction/index' },
        { id:'wa_rcollab',name:'风险协同预警',  gCol:2,  gRow:14, icon:'el-icon-bell',           kind:'warn', route:'/stateAssets/collaboration/riskCollaboration/index' },
        { id:'wa_intel',  name:'智能风险识别',  gCol:3,  gRow:14, icon:'el-icon-cpu',            kind:'main', route:'/intelligent/riskIdentification/index' },
        { id:'wa_ai',     name:'智能决策支持',  gCol:4,  gRow:14, icon:'el-icon-s-opportunity',  kind:'main', route:'/intelligent/decisionSupport/index' },
      ],
      // 连线定义
      lfEdges: [
        // 行业内部
        ['ind_home','ind_lay','main'],['ind_lay','ind_dash','main'],
        ['ind_home','ind_eng','main'],['ind_eng','ind_fin','main'],['ind_fin','ind_mfg','main'],['ind_mfg','ind_infra','main'],['ind_infra','ind_pub','main'],
        ['ind_eng','ind_comp','sub'],['ind_mfg','ind_syn','sub'],['ind_pub','ind_drill','main'],['ind_drill','ind_warn','warn'],
        // 投资内部
        ['i_home','i_proj','main'],['i_proj','i_dash','main'],['i_proj','i_comp','main'],
        ['i_proj','i_prog','sub'],['i_proj','i_post','sub'],['i_comp','i_drill','main'],['i_drill','i_nonmain','sub'],['i_drill','i_warn','warn'],
        // 金融内部
        ['eq_home','eq_owner','main'],['eq_owner','eq_chain','main'],['eq_chain','eq_struct','main'],
        ['eq_struct','eq_holder','sub'],['eq_struct','eq_cca','sub'],['eq_struct','eq_change','sub'],['eq_struct','eq_invest','sub'],
        ['eq_home','eq_fin','finance'],['eq_fin','eq_guar','finance'],['eq_guar','eq_loan','finance'],
        ['eq_loan','eq_deriv','finance'],['eq_deriv','eq_liq','warn'],['eq_liq','eq_drill','warn'],['eq_drill','eq_dash','main'],
        // 采购内部
        ['pr_home','pr_proj','main'],['pr_proj','pr_rec','main'],['pr_rec','pr_bid','main'],
        ['pr_proj','pr_sup','sub'],['pr_sup','pr_sprof','sub'],
        ['pr_bid','pr_bidc','sub'],['pr_bid','pr_price','sub'],
        ['pr_proj','pr_rel','finance'],['pr_rec','pr_fake','warn'],['pr_fake','pr_risk','warn'],['pr_risk','pr_drill','main'],['pr_drill','pr_dash','main'],
        // 军品内部
        ['ml_home','ml_task','main'],['ml_task','ml_taskr','main'],
        ['ml_task','ml_qual','main'],['ml_qual','ml_qualp','sub'],['ml_task','ml_sec','warn'],
        ['ml_taskr','ml_qual2','main'],['ml_qual2','ml_sub','sub'],['ml_qual2','ml_sc','sub'],
        ['ml_sub','ml_exec','finance'],['ml_sec','ml_drill','warn'],['ml_drill','ml_dash','main'],
        // 境外内部
        ['ov_home','ov_unit','main'],['ov_unit','ov_edash','sub'],['ov_unit','ov_lead','sub'],
        ['ov_lead','ov_eval','sub'],
        ['ov_edash','ov_fin','finance'],['ov_edash','ov_hr','finance'],['ov_edash','ov_op','finance'],
        ['ov_edash','ov_innov','sub'],['ov_edash','ov_strat','sub'],
        ['ov_unit','ov_invest','main'],['ov_invest','ov_crisk','warn'],['ov_crisk','ov_forex','warn'],
        ['ov_unit','ov_comp','sub'],['ov_fin','ov_opanal','main'],['ov_opanal','ov_dash','main'],
        ['ov_crisk','ov_safe','warn'],['ov_safe','ov_emg','warn'],
        // 合同内部
        ['ct_home','ct_rec','main'],['ct_rec','ct_list','main'],['ct_list','ct_dash','main'],
        ['ct_list','ct_appr','main'],['ct_appr','ct_comp','sub'],['ct_appr','ct_lc','sub'],
        ['ct_list','ct_perf','main'],['ct_perf','ct_exec','main'],['ct_exec','ct_credit','finance'],['ct_credit','ct_counter','finance'],
        ['ct_exec','ct_warn','warn'],['ct_perf','ct_disp','warn'],['ct_disp','ct_case','warn'],
        ['ct_case','ct_smart','sub'],['ct_drill','ct_dash','main'],['ct_warn','ct_drill','main'],
        // 资金内部
        ['fu_home','fu_skdata','finance'],['fu_skdata','fu_cwdata','finance'],['fu_cwdata','fu_htdata','finance'],
        ['fu_htdata','fu_fpdata','finance'],['fu_fpdata','fu_hrdata','finance'],
        ['fu_home','fu_sjdata','sub'],['fu_sjdata','fu_portrait','main'],['fu_skdata','fu_dp','main'],
        ['fu_dp','fu_dsrc','sub'],
        ['fu_portrait','fu_model','main'],['fu_dp','fu_model','main'],
        ['fu_model','fu_pgmodel','main'],['fu_pgmodel','fu_warn','warn'],['fu_warn','fu_wdata','warn'],
        // 会计内部
        ['ac_home','ac_bookd','finance'],['ac_bookd','ac_stmtd','finance'],['ac_stmtd','ac_subj','finance'],
        ['ac_subj','ac_bal','finance'],['ac_bal','ac_dash','main'],
        ['ac_bookd','ac_vouch','main'],['ac_vouch','ac_vlib','sub'],['ac_vouch','ac_book','main'],
        ['ac_book','ac_gcate','sub'],['ac_book','ac_detail','sub'],['ac_detail','ac_diary','sub'],['ac_diary','ac_assist','sub'],
        ['ac_book','ac_rept','main'],['ac_rept','ac_budget','main'],['ac_budget','ac_twog','warn'],
        ['ac_rept','ac_fraud','warn'],['ac_fraud','ac_glass','sub'],
        // 财务内部
        ['fi_home','fi_stmt','main'],['fi_stmt','fi_cons','main'],['fi_cons','fi_fund','main'],['fi_fund','fi_dash','main'],
        ['fi_cons','fi_bench','sub'],['fi_fund','fi_rel','finance'],['fi_rel','fi_relp','finance'],
        ['fi_stmt','fi_exp','sub'],
        ['fi_home','fi_comp','sub'],['fi_home','fi_peval','sub'],
        ['fi_rel','fi_risk','warn'],['fi_risk','fi_anomaly','warn'],['fi_anomaly','fi_drill','main'],['fi_drill','fi_dash','main'],
        // 薄酬内部
        ['sa_home','sa_total','main'],['sa_total','sa_totlist','main'],['sa_totlist','sa_dash','main'],
        ['sa_total','sa_exec','main'],['sa_exec','sa_link','main'],['sa_link','sa_incentive','sub'],
        ['sa_total','sa_labor','finance'],['sa_exec','sa_comply','sub'],
        ['sa_comply','sa_warn','warn'],['sa_link','sa_warn','warn'],['sa_warn','sa_drill','main'],['sa_drill','sa_dash','main'],
        // 产权内部
        ['pp_home','pp_conc','sub'],['pp_conc','pp_flow','main'],['pp_flow','pp_cross','sub'],['pp_flow','pp_alloc','sub'],['pp_flow','pp_dash','main'],
        ['pp_home','pp_eqchart','main'],['pp_eqchart','pp_eqstruct','main'],['pp_eqstruct','pp_chain','main'],
        ['pp_chain','pp_owner','sub'],['pp_owner','pp_holder','sub'],
        ['pp_home','pp_reg','main'],['pp_reg','pp_reglist','main'],['pp_eqstruct','pp_change','sub'],
        ['pp_reg','pp_trans','main'],['pp_trans','pp_review','sub'],
        ['pp_chain','pp_share','finance'],
        // 数据融合内部
        ['df_portrait','df_dp','main'],['df_sk','df_cw','finance'],['df_cw','df_ht','finance'],
        ['df_ht','df_fp','finance'],['df_fp','df_hr','finance'],['df_dp','df_sj','sub'],
        ['df_portrait','df_model','main'],['df_dp','df_model','main'],
        ['df_model','df_pgm','main'],['df_model','df_dsrc','sub'],
        // 预警处置内部
        ['wa_risk','wa_rdata','warn'],['wa_rdata','wa_issues','warn'],['wa_issues','wa_dash','main'],
        ['wa_dash','wa_report','main'],['wa_dash','wa_screen','main'],
        ['wa_issues','wa_collect','sub'],['wa_collect','wa_collab','sub'],['wa_collab','wa_rcollab','warn'],
        ['wa_rcollab','wa_intel','main'],['wa_intel','wa_ai','main'],
        // 跨层连线：行业→经营
        ['ind_warn','i_home','warn'],['ind_warn','eq_home','warn'],['ind_warn','pr_home','warn'],['ind_warn','ml_home','warn'],['ind_warn','ov_home','warn'],
        // 跨层连线：经营→载体
        ['i_warn','ct_home','main'],['i_warn','fu_home','main'],
        ['eq_dash','ct_home','main'],
        // 跨层连线：载体→管理
        ['ct_drill','ac_home','main'],['ct_drill','fi_home','main'],
        ['fu_warn','ac_home','main'],
        // 跨层连线：管理→数据融合
        ['ac_fraud','df_portrait','warn'],['fi_anomaly','df_dp','warn'],['sa_warn','df_portrait','warn'],['pp_share','df_sk','finance'],
        // 跨层连线：数据融合→预警处置
        ['df_model','wa_risk','warn'],['df_pgm','wa_risk','warn'],
      ],
      welcomeStats: [
        { label: '监管企业总数', value: '1,286家', color: '#1890FF' },
        { label: '覆盖穿透领域', value: '12个', color: '#52C41A' },
        { label: '风险预警', value: '38条', color: '#FF4D4F' },
        { label: '待处理事项', value: '15个', color: '#FA8C16' },
        { label: '本期报告', value: '6份', color: '#722ED1' },
      ],
      kpiCards: [
        { label: '监管企业总数', value: '1,286', unit: '家', icon: 'el-icon-office-building', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', barBg: 'rgba(24,144,255,0.1)', barColor: '#1890FF', barWidth: 78, trend: 3.2, theme: '' },
        { label: '穿透覆盖率', value: '96.5', unit: '%', icon: 'el-icon-connection', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', barBg: 'rgba(82,196,26,0.1)', barColor: '#52C41A', barWidth: 96, trend: 1.8, theme: '' },
        { label: '风险预警总数', value: '38', unit: '条', icon: 'el-icon-warning', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', barBg: 'rgba(255,77,79,0.1)', barColor: '#FF4D4F', barWidth: 38, trend: 2.5, theme: 'kpi-danger' },
        { label: '异常识别项', value: '127', unit: '项', icon: 'el-icon-s-opportunity', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', barBg: 'rgba(250,140,22,0.1)', barColor: '#FA8C16', barWidth: 52, trend: -1.2, theme: 'kpi-warning' },
        { label: '本期监管报告', value: '6', unit: '份', icon: 'el-icon-document', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', barBg: 'rgba(114,46,209,0.1)', barColor: '#722ED1', barWidth: 60, trend: 0, theme: '' },
        { label: '数据协同完成率', value: '88.3', unit: '%', icon: 'el-icon-refresh', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', barBg: 'rgba(19,194,194,0.1)', barColor: '#13C2C2', barWidth: 88, trend: 4.1, theme: '' },
      ],
      domains: [
        { id: 'invest', name: '投资穿透', desc: '项目台账/决策合规/投后评价', icon: 'el-icon-data-analysis', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', kind: 'core', count: 7, route: '/stateAssets/investPenetration/home/index' },
        { id: 'equity', name: '金融穿透', desc: '股权结构/控制链/股东分析', icon: 'el-icon-share', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', kind: 'core', count: 14, route: '/stateAssets/equityPenetration/home/index' },
        { id: 'procurement', name: '采购穿透', desc: '项目台账/供应商/招投标', icon: 'el-icon-goods', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', kind: 'core', count: 12, route: '/stateAssets/procurementPenetration/home/index' },
        { id: 'military', name: '军品穿透', desc: '任务台账/资质/质量/保密', icon: 'el-icon-s-flag', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', kind: 'core', count: 9, route: '/stateAssets/militaryPenetration/home/index' },
        { id: 'overseas', name: '境外穿透', desc: '境外单位/投资/国别风险', icon: 'el-icon-location', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', kind: 'core', count: 13, route: '/stateAssets/overseasPenetration/home/index' },
        { id: 'industry', name: '行业穿透', desc: '能源/金融/制造/基础设施', icon: 'el-icon-s-grid', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', kind: 'core', count: 10, route: '/stateAssets/industryPenetration/home/index' },
        { id: 'contract', name: '合同穿透', desc: '台账/审批/履行/纠纷', icon: 'el-icon-document-checked', iconBg: 'rgba(235,47,150,0.1)', iconColor: '#EB2F96', kind: 'core', count: 11, route: '/stateAssets/contractPenetration/home/index' },
        { id: 'accounting', name: '会计穿透', desc: '凭证/账簿/报表/预算', icon: 'el-icon-notebook-2', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', kind: 'core', count: 8, route: '/stateAssets/accountingPenetration/home/index' },
        { id: 'financial', name: '财务穿透', desc: '合并分析/资金流/绩效', icon: 'el-icon-money', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', kind: 'core', count: 12, route: '/stateAssets/financialPenetration/home/index' },
        { id: 'fund', name: '资金穿透', desc: '资金画像/数据穿透/风控', icon: 'el-icon-coin', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', kind: 'core', count: 6, route: '/stateAssets/fundPenetration/home/index' },
        { id: 'salary', name: '薪酬穿透', desc: '总额台账/高管/激励/合规', icon: 'el-icon-user', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', kind: 'core', count: 8, route: '/stateAssets/salaryPenetration/home/index' },
        { id: 'property', name: '产权穿透', desc: '产权登记/股权变动/交易', icon: 'el-icon-s-home', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', kind: 'core', count: 14, route: '/stateAssets/propertyPenetration/home' },
      ],
      riskList: [
        { id: 1, title: '某子公司投资项目超概算预警', domain: '投资穿透', desc: '超预算达32%', level: 'HIGH', levelLabel: '高', time: '05-27', route: '/stateAssets/investPenetration/riskWarning/index' },
        { id: 2, title: '金融风险-流动性比率异常', domain: '金融穿透', desc: '流动比率低于1.0', level: 'HIGH', levelLabel: '高', time: '05-27', route: '/stateAssets/financialRiskPenetration/liquidity/index' },
        { id: 3, title: '采购大额合同未招标', domain: '采购穿透', desc: '3份合同违规直采', level: 'MEDIUM', levelLabel: '中', time: '05-26', route: '/stateAssets/procurementPenetration/biddingMonitor/index' },
        { id: 4, title: '境外单位财务数据逾期未报', domain: '境外穿透', desc: '逾期45天', level: 'MEDIUM', levelLabel: '中', time: '05-26', route: '/stateAssets/overseasPenetration/operationAnalysis/index' },
        { id: 5, title: '薪酬总额超预算预警', domain: '薪酬穿透', desc: '超出预算18.6%', level: 'MEDIUM', levelLabel: '中', time: '05-25', route: '/stateAssets/salaryPenetration/dashboard/index' },
      ],
      pendingList: [
        { id: 1, title: '产权登记台账核查', domain: '产权穿透', desc: '12家企业待核查', urgent: true, statusLabel: '紧急', route: '/stateAssets/propertyPenetration/right/index' },
        { id: 2, title: '军品资质到期续审', domain: '军品穿透', desc: '5个资质即将到期', urgent: true, statusLabel: '紧急', route: '/stateAssets/militaryPenetration/qualification/index' },
        { id: 3, title: '合同审批追踪复核', domain: '合同穿透', desc: '8份合同待复核', urgent: false, statusLabel: '待办', route: '/stateAssets/contractPenetration/approvalTrack/index' },
        { id: 4, title: '会计凭证穿透抽查', domain: '会计穿透', desc: '本月抽查计划', urgent: false, statusLabel: '待办', route: '/stateAssets/accountingPenetration/voucherPenetration/index' },
      ],
      abnormalList: [
        { id: 1, title: '关联方非公允交易异常', domain: '财务穿透', desc: '发现3笔可疑交易', typeLabel: '财务异常', route: '/stateAssets/financialPenetration/relatedTransaction/index' },
        { id: 2, title: '供应商虚假贸易核查', domain: '采购穿透', desc: '2家供应商存疑', typeLabel: '采购异常', route: '/stateAssets/procurementPenetration/fakeTrade/index' },
        { id: 3, title: '高管薪酬偏离度超标', domain: '薪酬穿透', desc: '偏离行业均值42%', typeLabel: '薪酬异常', route: '/stateAssets/salaryPenetration/executivePay/index' },
      ],
      domainStats: [
        { type: '投资/金融穿透', count: 21, ratio: 18.3, color: '#1890FF' },
        { type: '采购/军品穿透', count: 21, ratio: 18.3, color: '#52C41A' },
        { type: '境外/行业穿透', count: 23, ratio: 20.0, color: '#FA8C16' },
        { type: '合同/会计穿透', count: 19, ratio: 16.5, color: '#722ED1' },
        { type: '财务/资金穿透', count: 18, ratio: 15.7, color: '#13C2C2' },
        { type: '薪酬/产权穿透', count: 13, ratio: 11.3, color: '#EB2F96' },
      ],
      reportList: [
        { id: 1, tag: '投资', title: '2026年Q1投资项目穿透分析报告', time: '05-27', author: '监管处', tagBg: 'rgba(24,144,255,0.1)', tagColor: '#1890FF', route: '/monitor/monitorReport/index' },
        { id: 2, tag: '金融', title: '金融风险综合穿透评估报告', time: '05-26', author: '风控部', tagBg: 'rgba(82,196,26,0.1)', tagColor: '#52C41A', route: '/monitor/monitorReport/index' },
        { id: 3, tag: '采购', title: '采购供应链风险穿透报告', time: '05-25', author: '审计部', tagBg: 'rgba(250,140,22,0.1)', tagColor: '#FA8C16', route: '/monitor/monitorReport/index' },
        { id: 4, tag: '薪酬', title: '高管薪酬效益联动分析报告', time: '05-24', author: '人事部', tagBg: 'rgba(114,46,209,0.1)', tagColor: '#722ED1', route: '/monitor/monitorReport/index' },
        { id: 5, tag: '境外', title: '境外机构合规经营专项报告', time: '05-23', author: '合规部', tagBg: 'rgba(255,77,79,0.1)', tagColor: '#FF4D4F', route: '/monitor/monitorReport/index' },
        { id: 6, tag: '产权', title: '产权登记与交易合规专项报告', time: '05-22', author: '法务部', tagBg: 'rgba(19,194,194,0.1)', tagColor: '#13C2C2', route: '/monitor/monitorReport/index' },
      ],
      keyProgresses: [
        { id: 1, name: '投资穿透监管', rate: 88, abnormal: 3, scope: '投资领域', coverage: 156, riskCount: 8, route: '/stateAssets/investPenetration/dashboard/index' },
        { id: 2, name: '采购供应链穿透', rate: 75, abnormal: 5, scope: '采购领域', coverage: 238, riskCount: 12, route: '/stateAssets/procurementPenetration/dashboard/index' },
        { id: 3, name: '境外单位穿透监管', rate: 62, abnormal: 7, scope: '境外领域', coverage: 89, riskCount: 15, route: '/stateAssets/overseasPenetration/dashboard/index' },
        { id: 4, name: '薪酬合规穿透', rate: 91, abnormal: 2, scope: '薪酬领域', coverage: 312, riskCount: 5, route: '/stateAssets/salaryPenetration/dashboard/index' },
        { id: 5, name: '产权登记穿透核查', rate: 55, abnormal: 9, scope: '产权领域', coverage: 445, riskCount: 18, route: '/stateAssets/propertyPenetration/dashboard/index' },
      ],
      modules: [
        { name: '投资穿透', icon: 'el-icon-data-analysis', desc: '项目/决策/投后', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', route: '/stateAssets/investPenetration/home/index' },
        { name: '金融穿透', icon: 'el-icon-share', desc: '股权/控制链/风险', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', route: '/stateAssets/equityPenetration/home/index' },
        { name: '采购穿透', icon: 'el-icon-goods', desc: '台账/供应商/招标', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', route: '/stateAssets/procurementPenetration/home/index' },
        { name: '军品穿透', icon: 'el-icon-s-flag', desc: '任务/资质/质量', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', route: '/stateAssets/militaryPenetration/home/index' },
        { name: '境外穿透', icon: 'el-icon-location', desc: '境外/投资/风险', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', route: '/stateAssets/overseasPenetration/home/index' },
        { name: '行业穿透', icon: 'el-icon-s-grid', desc: '能源/金融/制造', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', route: '/stateAssets/industryPenetration/home/index' },
        { name: '合同穿透', icon: 'el-icon-document-checked', desc: '台账/履行/纠纷', iconBg: 'rgba(235,47,150,0.1)', iconColor: '#EB2F96', route: '/stateAssets/contractPenetration/home/index' },
        { name: '会计穿透', icon: 'el-icon-notebook-2', desc: '凭证/账簿/报表', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', route: '/stateAssets/accountingPenetration/home/index' },
        { name: '财务穿透', icon: 'el-icon-money', desc: '合并/资金流/绩效', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', route: '/stateAssets/financialPenetration/home/index' },
        { name: '资金穿透', icon: 'el-icon-coin', desc: '画像/数据/风控', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', route: '/stateAssets/fundPenetration/home/index' },
        { name: '薪酬穿透', icon: 'el-icon-user', desc: '总额/高管/激励', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', route: '/stateAssets/salaryPenetration/home/index' },
        { name: '产权穿透', icon: 'el-icon-s-home', desc: '登记/变动/交易', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', route: '/stateAssets/propertyPenetration/home' },
        { name: '监管报告', icon: 'el-icon-tickets', desc: '编制/分发/质控', iconBg: 'rgba(235,47,150,0.1)', iconColor: '#EB2F96', route: '/monitor/monitorReport/index' },
        { name: '监管驾驶舱', icon: 'el-icon-s-platform', desc: '综合监管总览', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', route: '/stateAssets/dashboard/index' },
        { name: '智能风控总览', icon: 'el-icon-s-cooperation', desc: '大数据风控', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', route: '/home/screen/dsjzl' },
        { name: '企业信息', icon: 'el-icon-office-building', desc: '企业基本信息库', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', route: '/stateAssets/enterprise/index' },
      ],
      aiFeatures: [
        { name: '智能数据分析', icon: 'el-icon-data-line', desc: '多维度数据穿透智能分析', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/intelligent/dataAnalysis/index' },
        { name: '智能决策分析', icon: 'el-icon-s-opportunity', desc: 'AI辅助监管决策支持', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/intelligent/decisionSupport/index' },
        { name: '智能风险识别', icon: 'el-icon-warning-outline', desc: '自动识别异常与风险模式', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/intelligent/riskIdentification/index' },
        { name: '国有资本布局', icon: 'el-icon-connection', desc: '国有资本战略布局分析', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/advanced/capitalLayout/index' },
        { name: '决策支持分析', icon: 'el-icon-s-marketing', desc: '多因素综合决策模型', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/advanced/decisionAnalysis/index' },
        { name: '信用风险穿透', icon: 'el-icon-medal', desc: '企业信用综合评估分析', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/home/screen/xyfx' },
      ],
      configItems: [
        { name: '监管驾驶舱', icon: 'el-icon-s-platform', desc: '国资综合监管总览', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/stateAssets/dashboard/index' },
        { name: '国资穿透总览', icon: 'el-icon-view', desc: '全域穿透可视化大屏', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/home/screen/gzctzl' },
        { name: '国资财经总览', icon: 'el-icon-money', desc: '财经运行综合总览', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/home/screen/czyxzl' },
        { name: '数据报送任务', icon: 'el-icon-upload2', desc: '数据报送与采集管理', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/stateAssets/dataCollection/index' },
        { name: '数据协同', icon: 'el-icon-refresh', desc: '跨系统数据协同管理', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/stateAssets/collaboration/dataCollaboration/index' },
        { name: '风险协同预警', icon: 'el-icon-bell', desc: '多方协同风险预警', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/stateAssets/collaboration/riskCollaboration/index' },
        { name: '监管指令协同', icon: 'el-icon-s-promotion', desc: '监管指令下达与跟踪', bgColor: 'rgba(235,47,150,0.08)', iconColor: '#EB2F96', route: '/stateAssets/collaboration/supervisionInstruction/index' },
        { name: '知识管理', icon: 'el-icon-reading', desc: '监管知识库管理', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/advanced/knowledgeManagement/index' },
        { name: '政策管理', icon: 'el-icon-document', desc: '监管政策法规管理', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/advanced/policyManagement/index' },
        { name: '监控报告编制', icon: 'el-icon-edit', desc: '监管报告编制工具', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/monitor/monitorReport/index' },
        { name: '自定义报告', icon: 'el-icon-s-tools', desc: '自定义报告模板配置', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/monitor/monitorReport/customer' },
        { name: '报告模板', icon: 'el-icon-files', desc: '标准报告模板库', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/report/template/index' },
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
    totalItems() {
      return this.domainStats.reduce((sum, t) => sum + t.count, 0)
    },
    // ===== 6层平铺拓扑图计算属性 =====
    // 全局节点Map
    lfNodeMap() {
      const m = {}
      this.lfNodes.forEach(n => { m[n.id] = n })
      return m
    },
    // 所有节点（供模板遍历）
    lfAllNodes() {
      return this.lfNodes
    },
    // 每层的 laneTop / laneH（基于 rowStart/rows）
    lfLayers() {
      const { rowH, padY, nodeH, laneGap } = this.lfCfg
      return this.lfLayers_def.map(layer => {
        const laneTop = padY + layer.rowStart * rowH - laneGap / 2
        const laneH = layer.rows * rowH + laneGap
        return { ...layer, laneTop, laneH }
      })
    },
    // 画布总宽度
    lfCanvasW() {
      const { colW, padX, laneLabelW } = this.lfCfg
      const maxCol = Math.max(...this.lfNodes.map(n => n.gCol))
      return Math.ceil(laneLabelW + (maxCol + 1) * colW + padX * 2)
    },
    // 画布总高度
    lfCanvasH() {
      const { rowH, padY, nodeH } = this.lfCfg
      const maxRow = Math.max(...this.lfNodes.map(n => n.gRow))
      return Math.ceil((maxRow + 1) * rowH + padY * 2 + nodeH)
    },
    // 所有连线路径
    lfEdgePaths() {
      const { colW, rowH, nodeW, nodeH, padX, padY, laneLabelW } = this.lfCfg
      const nodeMap = this.lfNodeMap
      const markerMap = {
        main:    'url(#lf-arr-main)',
        sub:     'url(#lf-arr-sub)',
        warn:    'url(#lf-arr-warn)',
        finance: 'url(#lf-arr-finance)',
      }
      return this.lfEdges.map(([from, to, kind]) => {
        const a = nodeMap[from], b = nodeMap[to]
        if (!a || !b) return { d: '', kind: '', marker: '' }
        const ax = laneLabelW + padX + (a.gCol + 0.5) * colW
        const ay = padY + a.gRow * rowH + nodeH / 2
        const bx = laneLabelW + padX + (b.gCol + 0.5) * colW
        const by = padY + b.gRow * rowH + nodeH / 2
        const halfW = nodeW / 2 + 2
        const halfH = nodeH / 2 + 2
        let d
        if (a.gRow === b.gRow) {
          const dir = bx > ax ? 1 : -1
          d = `M${ax + dir * halfW},${ay} L${bx - dir * halfW},${by}`
        } else {
          const dirY = by > ay ? 1 : -1
          const dirX = bx > ax ? 1 : -1
          const sx = ax, sy = ay + dirY * halfH
          const tx = bx - dirX * halfW, ty = by
          d = `M${sx},${sy} L${sx},${ty} L${tx},${ty}`
        }
        return { d, kind, marker: markerMap[kind] || markerMap.main }
      })
    },
  },
  watch: {
    ipBright: {
      immediate: true,
      handler() { this._patchPrimaryColors() },
    },
  },
  created() {
    try {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      this.userName = userInfo ? userInfo.realname || userInfo.username || '' : ''
    } catch (e) {
      this.userName = ''
    }
    this._patchPrimaryColors()
  },
  methods: {
    jumpTo(route) {
      if (route) this.$router.push(route)
    },
    // ===== 6层平铺拓扑图节点定位 =====
    lfNodePos(n) {
      const { colW, rowH, nodeW, nodeH, padX, padY, laneLabelW } = this.lfCfg
      return {
        left: (laneLabelW + padX + n.gCol * colW + (colW - nodeW) / 2) + 'px',
        top:  (padY + n.gRow * rowH) + 'px',
        width: nodeW + 'px',
        height: nodeH + 'px',
      }
    },
    _patchPrimaryColors() {
      const bright = this.ipBright || '#1890FF'
      const rgb = this.ipPrimaryRgb || '24,144,255'
      const brightBg = `rgba(${rgb},0.1)`
      const brightBgLight = `rgba(${rgb},0.08)`
      if (this.welcomeStats && this.welcomeStats[0]) this.welcomeStats[0].color = bright
      if (this.kpiCards && this.kpiCards[0]) {
        this.kpiCards[0].iconBg = brightBg
        this.kpiCards[0].iconColor = bright
        this.kpiCards[0].barBg = brightBg
        this.kpiCards[0].barColor = bright
      }
      if (this.domains && this.domains[0]) {
        this.domains[0].iconBg = brightBg
        this.domains[0].iconColor = bright
      }
      if (this.modules && this.modules[0]) {
        this.modules[0].iconBg = brightBg
        this.modules[0].iconColor = bright
      }
      if (this.aiFeatures && this.aiFeatures[0]) {
        this.aiFeatures[0].bgColor = brightBgLight
        this.aiFeatures[0].iconColor = bright
      }
      if (this.configItems && this.configItems[0]) {
        this.configItems[0].bgColor = brightBgLight
        this.configItems[0].iconColor = bright
      }
    },
  },
}
</script>

<style lang="scss" scoped>
.state-assets-home {
  padding: 16px;
  background: #f0f2f5;
  min-height: 100vh;
}

/* ========== 欢迎区 ========== */
.welcome-section {
  position: relative;
  border-radius: 12px;
  overflow: hidden;
  margin-bottom: 16px;
}
.welcome-bg {
  position: absolute;
  top: 0; left: 0; right: 0; bottom: 0;
  background: linear-gradient(135deg, var(--ip-primary) 0%, var(--ip-secondary) 50%, var(--ip-bright) 100%);
  z-index: 0;
}
.welcome-content {
  position: relative;
  z-index: 1;
  padding: 28px 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #fff;
}
.welcome-left { flex: 1; }
.greeting {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 8px;
  .greeting-icon { font-size: 20px; color: #FAAD14; }
  .greeting-text { font-size: 14px; color: rgba(255,255,255,0.7); }
}
.welcome-title {
  margin: 0 0 8px;
  font-size: 26px;
  font-weight: 700;
  letter-spacing: 2px;
  background: linear-gradient(90deg, #fff, var(--ip-light-bg, #B2D4FF));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}
.welcome-desc {
  font-size: 13px;
  color: rgba(255,255,255,0.6);
  margin-bottom: 18px;
}
.welcome-actions {
  display: flex;
  gap: 10px;
}
.welcome-stats {
  display: flex;
  gap: 24px;
}
.ws-item {
  text-align: center;
  .ws-value { font-size: 20px; font-weight: 700; }
  .ws-label { font-size: 12px; color: rgba(255,255,255,0.6); margin-top: 4px; }
}

/* ========== KPI ========== */
.kpi-section {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 14px;
  margin-bottom: 16px;
}
.kpi-card {
  background: #fff;
  border-radius: 10px;
  padding: 16px;
  box-shadow: 0 1px 6px rgba(0,0,0,.06);
  transition: all 0.2s;
  &:hover { transform: translateY(-2px); box-shadow: 0 6px 16px rgba(0,0,0,.1); }
  &.kpi-warning { border-top: 3px solid #FA8C16; }
  &.kpi-danger { border-top: 3px solid #FF4D4F; }
  .kpi-top {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 8px;
  }
  .kpi-icon-wrap {
    width: 36px; height: 36px;
    border-radius: 8px;
    display: flex; align-items: center; justify-content: center;
    i { font-size: 18px; }
  }
  .kpi-trend {
    font-size: 12px;
    &.trend-up { color: #52C41A; }
    &.trend-down { color: #FF4D4F; }
  }
  .kpi-value { font-size: 24px; font-weight: 700; color: var(--ip-primary); }
  .kpi-unit { font-size: 13px; font-weight: 400; margin-left: 2px; color: #999; }
  .kpi-label { font-size: 12px; color: #888; margin: 4px 0 8px; }
  .kpi-bar {
    height: 4px;
    border-radius: 2px;
    overflow: hidden;
  }
  .kpi-bar-fill {
    height: 100%;
    border-radius: 2px;
    transition: width 0.6s ease;
  }
}

/* ========== 6层平铺拓扑图 ========== */
.lifecycle-section {
  background: #fff;
  border-radius: 10px;
  padding: 18px 20px;
  margin-bottom: 16px;
  box-shadow: 0 1px 6px rgba(0,0,0,.06);
}
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
}
.section-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--ip-primary);
  i { margin-right: 6px; }
}
.section-legend {
  font-size: 12px;
  color: #888;
  span { margin-left: 14px; }
  .dot {
    display: inline-block;
    width: 8px; height: 8px;
    border-radius: 50%;
    margin-right: 4px;
    vertical-align: middle;
    &.dot-main    { background: var(--ip-primary, #1890FF); }
    &.dot-sub     { background: #bbb; }
    &.dot-warn    { background: #FF4D4F; }
    &.dot-finance { background: #13C2C2; }
  }
}
/* 画布外层容器 */
.lf-canvas-wrap {
  width: 100%;
  overflow-x: auto;
  overflow-y: hidden;
  padding: 6px;
  background:
    linear-gradient(90deg, rgba(24,144,255,0.03) 1px, transparent 1px) 0 0 / 20px 20px,
    linear-gradient(0deg, rgba(24,144,255,0.03) 1px, transparent 1px) 0 0 / 20px 20px,
    #fafbff;
  border-radius: 8px;
  border: 1px solid #f0f2f5;
}
/* 画布（绝对定位容器） */
.lf-canvas {
  position: relative;
  margin: 0 auto;
}
/* SVG 连线层 */
.lf-svg {
  position: absolute;
  top: 0; left: 0;
  pointer-events: none;
  z-index: 1;
}
/* 泳道背景块 */
.lf-lane {
  position: absolute;
  left: 0;
  border-radius: 6px;
  z-index: 0;
  overflow: hidden;
  &.lane-industry { background: rgba(19,194,194,0.05); border: 1px solid rgba(19,194,194,0.2); }
  &.lane-manage   { background: rgba(24,144,255,0.05); border: 1px solid rgba(24,144,255,0.2); }
  &.lane-carrier  { background: rgba(235,47,150,0.05); border: 1px solid rgba(235,47,150,0.2); }
  &.lane-mgmt     { background: rgba(114,46,209,0.05); border: 1px solid rgba(114,46,209,0.2); }
  &.lane-data     { background: rgba(250,140,22,0.05); border: 1px solid rgba(250,140,22,0.2); }
  &.lane-warn     { background: rgba(255,77,79,0.05);  border: 1px solid rgba(255,77,79,0.2); }
}
/* 泳道左侧标签 */
.lf-lane-label {
  position: absolute;
  left: 0; top: 0; bottom: 0;
  width: 64px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 4px;
  font-size: 13px;
  font-weight: 700;
  letter-spacing: 2px;
  writing-mode: vertical-rl;
  text-orientation: mixed;
  padding: 6px 0;
  border-radius: 6px 0 0 6px;
  opacity: 0.92;
  i { font-size: 14px; writing-mode: horizontal-tb; margin-bottom: 4px; }
  span { writing-mode: vertical-rl; }
}
/* 边线样式 */
.lf-edge {
  fill: none;
  &.edge-main    { stroke: var(--ip-primary, #1890FF); stroke-width: 2; }
  &.edge-sub     { stroke: #bbb; stroke-width: 1.4; stroke-dasharray: 4 3; }
  &.edge-warn    { stroke: #FF4D4F; stroke-width: 1.8; }
  &.edge-finance { stroke: #13C2C2; stroke-dasharray: 5 3; stroke-width: 1.6; }
}
/* 节点样式 */
.flow-pos-node {
  position: absolute;
  z-index: 2;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 10px 6px 8px;
  background: #fff;
  border: 1.5px solid #e8e8e8;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s;
  box-shadow: 0 1px 4px rgba(0,0,0,.04);
  &:hover {
    transform: translateY(-2px) scale(1.04);
    box-shadow: 0 6px 18px rgba(0,0,0,.12);
    z-index: 3;
  }
  .fpn-icon-wrap {
    width: 34px; height: 34px;
    border-radius: 8px;
    display: flex; align-items: center; justify-content: center;
    margin-bottom: 5px;
    i { font-size: 18px; }
  }
  .fpn-label {
    font-size: 12px;
    font-weight: 600;
    color: #333;
    white-space: nowrap;
    line-height: 1.3;
    margin-bottom: 2px;
  }
  .fpn-sub-items {
    display: flex;
    flex-wrap: wrap;
    gap: 2px;
    justify-content: center;
    padding: 0 2px;
  }
  .fpn-sub-tag {
    font-size: 9px;
    padding: 1px 4px;
    border-radius: 3px;
    background: #f5f5f5;
    color: #888;
    cursor: pointer;
    white-space: nowrap;
    transition: all 0.15s;
    &:hover { background: var(--ip-primary, #1890FF); color: #fff; }
  }
  .fpn-dot {
    position: absolute;
    top: -3px; right: -3px;
    width: 9px; height: 9px;
    border-radius: 50%;
    background: #FF4D4F;
    animation: pulse 1.5s infinite;
  }
  /* 不同 kind 配色 */
  &.fpn-main {
    border-color: var(--ip-primary, #1890FF);
    border-width: 2px;
    .fpn-icon-wrap { background: rgba(24,144,255,0.12); }
    .fpn-icon-wrap i { color: var(--ip-primary, #1890FF); }
    .fpn-label { color: var(--ip-primary, #1890FF); }
  }
  &.fpn-sub {
    border-color: #d9d9d9;
    background: #fafafa;
    .fpn-icon-wrap { background: rgba(0,0,0,0.05); }
    .fpn-icon-wrap i { color: #888; }
    .fpn-label { color: #666; }
  }
  &.fpn-warn {
    border-color: #FF4D4F;
    background: #fff1f0;
    .fpn-icon-wrap { background: rgba(255,77,79,0.15); }
    .fpn-icon-wrap i { color: #FF4D4F; }
    .fpn-label { color: #CF1322; }
  }
  &.fpn-finance {
    border-color: #13C2C2;
    background: #e6fffb;
    .fpn-icon-wrap { background: rgba(19,194,194,0.15); }
    .fpn-icon-wrap i { color: #13C2C2; }
    .fpn-label { color: #08979C; }
  }
  /* 兼容旧属性名 */
  &.fpn-blue {
    border-color: var(--ip-primary, #1890FF);
    border-width: 2px;
    .fpn-icon-wrap { background: rgba(24,144,255,0.12); }
    .fpn-icon-wrap i { color: var(--ip-primary, #1890FF); }
    .fpn-label { color: var(--ip-primary, #1890FF); }
  }
  &.fpn-green {
    border-color: #52C41A;
    border-width: 2px;
    background: #f6ffed;
    .fpn-icon-wrap { background: rgba(82,196,26,0.15); }
    .fpn-icon-wrap i { color: #52C41A; }
    .fpn-label { color: #389E0D; }
    .fpn-sub-tag { background: rgba(82,196,26,0.08); color: #389E0D; }
  }
  &.fpn-orange {
    border-color: #FA8C16;
    border-width: 2px;
    background: #fffbe6;
    .fpn-icon-wrap { background: rgba(250,140,22,0.15); }
    .fpn-icon-wrap i { color: #FA8C16; }
    .fpn-label { color: #D46B08; }
    .fpn-sub-tag { background: rgba(250,140,22,0.08); color: #D46B08; }
  }
  &.fpn-purple {
    border-color: #722ED1;
    background: #f9f0ff;
    .fpn-icon-wrap { background: rgba(114,46,209,0.15); }
    .fpn-icon-wrap i { color: #722ED1; }
    .fpn-label { color: #531DAB; }
    .fpn-sub-tag { background: rgba(114,46,209,0.08); color: #531DAB; }
  }
  &.fpn-cyan {
    border-color: #13C2C2;
    background: #e6fffb;
    .fpn-icon-wrap { background: rgba(19,194,194,0.15); }
    .fpn-icon-wrap i { color: #13C2C2; }
    .fpn-label { color: #08979C; }
    .fpn-sub-tag { background: rgba(19,194,194,0.08); color: #08979C; }
  }
  &.fpn-red {
    border-color: #FF4D4F;
    background: #fff1f0;
    .fpn-icon-wrap { background: rgba(255,77,79,0.15); }
    .fpn-icon-wrap i { color: #FF4D4F; }
    .fpn-label { color: #CF1322; }
    .fpn-sub-tag { background: rgba(255,77,79,0.08); color: #CF1322; }
  }
  &.fpn-alert { animation: nodeAlert 2s infinite; }
}
@keyframes pulse {
  0% { box-shadow: 0 0 0 0 rgba(255,77,79,0.5); }
  70% { box-shadow: 0 0 0 6px rgba(255,77,79,0); }
  100% { box-shadow: 0 0 0 0 rgba(255,77,79,0); }
}
@keyframes nodeAlert {
  0%, 100% { box-shadow: 0 1px 4px rgba(0,0,0,.04); }
  50% { box-shadow: 0 0 0 4px rgba(255,77,79,0.15), 0 1px 4px rgba(0,0,0,.04); }
}

/* ========== 通用面板 ========== */
.content-row { margin-bottom: 16px; }
.card-panel {
  background: #fff;
  border-radius: 10px;
  padding: 16px 18px;
  box-shadow: 0 1px 6px rgba(0,0,0,.06);
  height: 100%;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
}
.card-title {
  font-size: 15px;
  font-weight: 600;
  color: var(--ip-primary);
  i { margin-right: 6px; }
}
.card-more {
  font-size: 12px;
  color: var(--ip-bright);
  cursor: pointer;
  &:hover { opacity: 0.8; }
}

/* ========== 待办/风险 Tabs ========== */
.inner-tabs {
  ::v-deep .el-tabs__header { margin-bottom: 8px; }
  ::v-deep .el-tabs__item { font-size: 13px; padding: 0 12px; }
}
.tab-badge {
  ::v-deep .el-badge__content { top: -2px; }
}
.todo-list {}
.todo-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.15s;
  &:hover { background: #f5f7fa; }
  & + .todo-item { border-top: 1px solid #f5f5f5; }
}
.todo-left {
  display: flex;
  align-items: center;
  gap: 10px;
  flex: 1;
  min-width: 0;
}
.todo-dot {
  width: 8px; height: 8px;
  border-radius: 50%;
  flex-shrink: 0;
  &.dot-red { background: #FF4D4F; }
  &.dot-orange { background: #FA8C16; }
}
.todo-info { flex: 1; min-width: 0; }
.todo-name {
  font-size: 13px;
  color: #333;
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.todo-meta { font-size: 11px; color: #999; margin-top: 2px; }
.todo-right-col {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 4px;
}
.todo-time { font-size: 11px; color: #bbb; }

/* ========== 穿透分布 ========== */
.type-chart-area { padding: 0 4px; }
.type-row { margin-bottom: 14px; }
.type-head {
  display: flex;
  align-items: center;
  margin-bottom: 6px;
  .type-dot {
    width: 8px; height: 8px;
    border-radius: 2px;
    margin-right: 6px;
    flex-shrink: 0;
  }
  .type-name { font-size: 12px; color: #555; flex: 1; }
  .type-count { font-size: 12px; color: #999; }
}
.type-bar-bg {
  height: 8px;
  background: #f5f5f5;
  border-radius: 4px;
  overflow: hidden;
}
.type-bar-fill {
  height: 100%;
  border-radius: 4px;
  transition: width 0.6s ease;
}
.type-summary {
  margin-top: 12px;
  padding-top: 10px;
  border-top: 1px solid #f0f0f0;
  font-size: 12px;
  color: #888;
  b { color: var(--ip-primary); font-size: 14px; }
}

/* ========== 监管快报 ========== */
.report-list {}
.report-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.15s;
  &:hover { background: #f5f7fa; }
  & + .report-item { border-top: 1px solid #f5f5f5; }
}
.report-left {
  display: flex;
  align-items: center;
  gap: 10px;
  flex: 1;
  min-width: 0;
}
.report-tag {
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 11px;
  font-weight: 600;
  flex-shrink: 0;
}
.report-info { flex: 1; min-width: 0; }
.report-title {
  font-size: 13px;
  color: #333;
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.report-meta { font-size: 11px; color: #bbb; margin-top: 2px; }
.report-arrow { font-size: 12px; color: #d9d9d9; flex-shrink: 0; }

/* ========== 监管进度 ========== */
.contract-progress-list {}
.cpl-row {
  padding: 10px 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.15s;
  &:hover { background: #f5f7fa; }
  & + .cpl-row { border-top: 1px solid #f5f5f5; }
}
.cpl-row-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
  .cpl-row-name { font-size: 13px; font-weight: 500; color: #333; }
  .cpl-row-tags { display: flex; gap: 4px; }
}
.cpl-row-bar { margin-bottom: 6px; }
.cpl-bar-bg {
  height: 6px;
  background: #f5f5f5;
  border-radius: 3px;
  overflow: hidden;
}
.cpl-bar-fill {
  height: 100%;
  border-radius: 3px;
}
.cpl-row-info {
  display: flex;
  justify-content: space-between;
  font-size: 11px;
  color: #999;
  i { margin-right: 2px; }
}

/* ========== 模块网格 ========== */
.module-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
}
.module-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 14px 6px;
  border-radius: 10px;
  border: 1px solid #f0f0f0;
  cursor: pointer;
  transition: all 0.2s;
  text-align: center;
  &:hover {
    border-color: var(--ip-primary);
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(var(--ip-primary-rgb,24,144,255),0.1);
  }
  .mc-icon {
    width: 36px; height: 36px;
    border-radius: 8px;
    display: flex; align-items: center; justify-content: center;
    margin-bottom: 6px;
    i { font-size: 18px; }
  }
  .mc-name { font-size: 12px; font-weight: 600; color: #333; }
  .mc-desc { font-size: 10px; color: #bbb; margin-top: 2px; }
}

/* ========== AI ========== */
.ai-panel {
  background: linear-gradient(180deg, var(--ip-light-bg, #f0f5ff) 0%, #fff 100%);
}
.ai-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
}
.ai-card {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px;
  border-radius: 10px;
  border: 1px solid #e8e8e8;
  cursor: pointer;
  transition: all 0.2s;
  &:hover {
    border-color: var(--ip-bright);
    box-shadow: 0 4px 12px rgba(var(--ip-primary-rgb,24,144,255),0.12);
    transform: translateY(-2px);
  }
  .ai-icon {
    width: 38px; height: 38px;
    border-radius: 10px;
    display: flex; align-items: center; justify-content: center;
    flex-shrink: 0;
    i { font-size: 18px; }
  }
  .ai-body { flex: 1; min-width: 0; }
  .ai-name { font-size: 13px; font-weight: 600; color: #333; }
  .ai-desc { font-size: 11px; color: #999; margin-top: 2px; }
  .ai-arrow { font-size: 14px; color: #d9d9d9; flex-shrink: 0; }
}

/* ========== 配置文库 ========== */
.lib-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 8px;
}
.lib-card {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 12px;
  border-radius: 8px;
  border: 1px solid #f0f0f0;
  cursor: pointer;
  transition: all 0.15s;
  &:hover {
    border-color: var(--ip-primary);
    background: var(--ip-light-bg, #fafbff);
  }
  .lib-icon-wrap {
    width: 32px; height: 32px;
    border-radius: 8px;
    display: flex; align-items: center; justify-content: center;
    flex-shrink: 0;
    i { font-size: 16px; }
  }
  .lib-body { flex: 1; min-width: 0; }
  .lib-name { font-size: 13px; font-weight: 500; color: #333; }
  .lib-desc { font-size: 11px; color: #999; margin-top: 2px; }
  .lib-arrow { font-size: 12px; color: #d9d9d9; flex-shrink: 0; }
}
</style>
