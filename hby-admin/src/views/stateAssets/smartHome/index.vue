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
            <el-button type="primary" icon="el-icon-s-data" round @click="jumpTo('/stateAssetsConfig/dashboard')">监管驾驶舱</el-button>
            <el-button icon="el-icon-view" round @click="jumpTo('/stateAssetsConfig/gzctzl')">国资穿透总览</el-button>
            <el-button icon="el-icon-warning" round @click="jumpTo('/stateAssetsConfig/gzfxct')">国资风险穿透</el-button>
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

    <!-- ========== 各层独立全景拓扑图区块 ========== -->
    <div v-for="group in cardGroups" :key="group.id"
         class="layer-section"
         :style="{'border-left': '4px solid '+group.color}">
      <!-- 层标题 -->
      <div class="layer-section-header" :style="{background: group.color+'30'}">
        <div class="layer-section-title">
          <span class="layer-icon-wrap" :style="{background: group.color}">
            <i :class="group.icon" style="color:#fff" />
          </span>
          <span class="layer-name" :style="{color: group.color}">{{ group.name }}</span>
          <span class="layer-card-count">共{{ group.cards.length }}个领域</span>
        </div>
        <div class="section-legend">
          <span><i class="dot dot-main" />主流程</span>
          <span><i class="dot dot-sub" />支撑节点</span>
          <span><i class="dot dot-warn" />风险/预警</span>
          <span><i class="dot dot-finance" />财务/数据</span>
        </div>
      </div>
      <!-- 该层内各领域卡片（每张占满一行，独立拓扑图） -->
      <div class="layer-cards">
        <div v-for="card in group.cards" :key="card.id"
             class="domain-card"
             :style="{'border-left': '3px solid '+card.color}">
          <!-- 领域标题栏 -->
          <div class="card-header-bar">
            <span class="card-color-dot" :style="{background: card.color}"></span>
            <i :class="card.icon" :style="{color: card.color}" />
            <span class="card-title-text">{{ card.name }}</span>
            <span class="card-count">{{ card.nodes.length }}个页面</span>
          </div>
          <!-- 该领域独立 SVG 拓扑图 -->
          <div class="card-canvas-wrap">
            <div class="card-canvas"
                 :style="{ width: calcCardW(card)+'px', height: calcCardH(card)+'px' }">
              <svg class="card-svg" :width="calcCardW(card)" :height="calcCardH(card)">
                <defs>
                  <marker :id="'ca-arr-main-'+card.id" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="6" markerHeight="6" orient="auto">
                    <path d="M0,0 L10,5 L0,10 z" fill="var(--ip-primary,#1890FF)" />
                  </marker>
                  <marker :id="'ca-arr-sub-'+card.id" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="5" markerHeight="5" orient="auto">
                    <path d="M0,0 L10,5 L0,10 z" fill="#bbb" />
                  </marker>
                  <marker :id="'ca-arr-warn-'+card.id" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="5" markerHeight="5" orient="auto">
                    <path d="M0,0 L10,5 L0,10 z" fill="#FF4D4F" />
                  </marker>
                  <marker :id="'ca-arr-finance-'+card.id" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="5" markerHeight="5" orient="auto">
                    <path d="M0,0 L10,5 L0,10 z" fill="#13C2C2" />
                  </marker>
                </defs>
                <path v-for="(e, idx) in calcEdges(card)" :key="'e'+idx"
                      :d="e.d" :class="['card-edge','edge-'+e.kind]"
                      :marker-end="e.marker" fill="none" />
              </svg>
              <div v-for="n in card.nodes" :key="n.id"
                   class="flow-pos-node"
                   :class="['fpn-'+n.kind, n.alert ? 'fpn-alert' : '']"
                   :style="calcNodePos(card, n)"
                   @click="jumpTo(n.route)">
                <div class="fpn-icon-wrap"><i :class="n.icon" /></div>
                <div class="fpn-label">{{ n.name }}</div>
              </div>
            </div>
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
            <span class="card-more" @click="jumpTo('/stateAssetsConfig/gzfxct')">更多 →</span>
          </div>
          <el-tabs v-model="todoActive" class="inner-tabs">
            <el-tab-pane name="risk">
              <span slot="label">风险预警 <el-badge :value="riskList.length" type="danger" class="tab-badge" /></span>
              <div class="todo-list">
                <div v-for="item in riskList" :key="item.id" class="todo-item">
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
                <div v-for="item in pendingList" :key="item.id" class="todo-item">
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
                <div v-for="item in abnormalList" :key="item.id" class="todo-item">
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
            <span class="card-more" @click="jumpTo('/stateAssetsConfig/gzctzl')">详情 →</span>
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
            <span class="card-more" @click="jumpTo('/report/index')">更多 →</span>
          </div>
          <div class="report-list">
            <div v-for="r in reportList" :key="r.id" class="report-item">
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
            <span class="card-more" @click="jumpTo('/stateAssetsConfig/dashboard')">全部 →</span>
          </div>
          <div class="contract-progress-list">
            <div v-for="p in keyProgresses" :key="p.id" class="cpl-row">
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
import { getSmartHomeOverview } from '@/api/stateAssets/smartHome'

export default {
  name: 'StateAssetsSmartHome',
  mixins: [investThemeMixin],
  data() {
    return {
      userName: '',
      todoActive: 'risk',
      // ===== 6层平铺拓扑图全局配置 =====
      // 节点布局：colW/rowH=单格大小, nodeW/nodeH=节点大小, laneLabel=泳道标签宽度
      // ===== 各领域全景卡片配置 =====
      cardCfg: { colW: 130, rowH: 86, nodeW: 110, nodeH: 64, padX: 12, padY: 12 },
      // 12个领域卡片 + 数据融合 + 预警处置（每个领域独立的 nodes/edges，使用领域内部 col/row 坐标）
      domainCards: [
        // ==================== 行业领域 ====================
        {
          id: 'industry', name: '行业穿透', color: '#13C2C2', icon: 'el-icon-s-grid',
          route: '/industry/HyctIndex',
          nodes: [
            { id:'ind_home',  name:'行业穿透首页',  col:0, row:0, icon:'el-icon-s-home',          kind:'main',    route:'/industry/HyctIndex' },
            { id:'ind_lay',   name:'行业布局台账',  col:1, row:0, icon:'el-icon-tickets',         kind:'main',    route:'/industry/Hybjtz' },
            { id:'ind_dash',  name:'监控驾驶舱',    col:2, row:0, icon:'el-icon-s-platform',      kind:'main',    route:'/industry/HYjkjsc' },
            { id:'ind_eng',   name:'能源行业',      col:0, row:1, icon:'el-icon-lightning',       kind:'main',    route:'/industry/Nyhyjg' },
            { id:'ind_fin',   name:'金融行业',      col:1, row:1, icon:'el-icon-bank-card',       kind:'main',    route:'/industry/Jrhyjg' },
            { id:'ind_mfg',   name:'制造行业',      col:2, row:1, icon:'el-icon-s-tools',         kind:'main',    route:'/industry/Zzhyjg' },
            { id:'ind_infra', name:'基础设施',      col:3, row:1, icon:'el-icon-office-building', kind:'main',    route:'/industry/Jcssjg' },
            { id:'ind_pub',   name:'公共服务',      col:4, row:1, icon:'el-icon-s-custom',        kind:'main',    route:'/industry/Ggfwjg' },
            { id:'ind_comp',  name:'竞争力分析',    col:5, row:1, icon:'el-icon-data-analysis',   kind:'sub',     route:'/industry/Jzlfx' },
            { id:'ind_syn',   name:'产业协同分析',  col:6, row:1, icon:'el-icon-share',           kind:'sub',     route:'/industry/Cyxtfx' },
            { id:'ind_drill', name:'行业穿透分析',  col:7, row:1, icon:'el-icon-connection',      kind:'main',    route:'/industry/Hyctfx' },
            { id:'ind_warn',  name:'风险预警管理',  col:8, row:1, icon:'el-icon-warning',         kind:'warn',    route:'/industry/Fxyjgl', alert:true },
          ],
          edges: [
            ['ind_home','ind_lay','main'],['ind_lay','ind_dash','main'],
            ['ind_home','ind_eng','main'],['ind_eng','ind_fin','main'],['ind_fin','ind_mfg','main'],
            ['ind_mfg','ind_infra','main'],['ind_infra','ind_pub','main'],
            ['ind_eng','ind_comp','sub'],['ind_mfg','ind_syn','sub'],
            ['ind_pub','ind_drill','main'],['ind_drill','ind_warn','warn'],
          ],
        },
        // ==================== 经营领域：投资穿透 ====================
        {
          id: 'invest', name: '投资穿透', color: '#1890FF', icon: 'el-icon-data-analysis',
          route: '/modelMonitor/TzctIndex',
          nodes: [
            { id:'i_home',    name:'投资穿透首页',  col:0, row:0, icon:'el-icon-s-home',          kind:'main', route:'/modelMonitor/TzctIndex' },
            { id:'i_proj',    name:'投资项目台账',  col:1, row:0, icon:'el-icon-tickets',         kind:'main', route:'/modelMonitor/Tzxmtz' },
            { id:'i_dash',    name:'投资驾驶舱',    col:2, row:0, icon:'el-icon-s-platform',      kind:'main', route:'/modelMonitor/Tzjkjsc' },
            { id:'i_comp',    name:'决策合规追踪',  col:3, row:0, icon:'el-icon-s-check',         kind:'main', route:'/modelMonitor/Tzjchgzz' },
            { id:'i_prog',    name:'投资进度监控',  col:0, row:1, icon:'el-icon-timer',           kind:'sub',  route:'/modelMonitor/Tzjdjk' },
            { id:'i_post',    name:'投后评价管理',  col:1, row:1, icon:'el-icon-s-data',          kind:'sub',  route:'/modelMonitor/Thpjgl' },
            { id:'i_nonmain', name:'非主业投资分析',col:2, row:1, icon:'el-icon-warning-outline', kind:'sub',  route:'/modelMonitor/Fzytzfx' },
            { id:'i_drill',   name:'投资穿透分析',  col:3, row:1, icon:'el-icon-data-analysis',   kind:'main', route:'/modelMonitor/Tzctfx' },
            { id:'i_warn',    name:'投资风险预警',  col:3, row:2, icon:'el-icon-warning',         kind:'warn', route:'/modelMonitor/Fxyjgl', alert:true },
          ],
          edges: [
            ['i_home','i_proj','main'],['i_proj','i_dash','main'],['i_proj','i_comp','main'],
            ['i_proj','i_prog','sub'],['i_proj','i_post','sub'],
            ['i_comp','i_drill','main'],['i_drill','i_nonmain','sub'],['i_drill','i_warn','warn'],
          ],
        },
        // ==================== 经营领域：金融穿透 ====================
        {
          id: 'equity', name: '金融穿透', color: '#52C41A', icon: 'el-icon-share',
          route: '/equityPenetration/JrctIndex',
          nodes: [
            { id:'eq_home',   name:'金融穿透首页',  col:0, row:0, icon:'el-icon-s-home',          kind:'main',    route:'/equityPenetration/JrctIndex' },
            { id:'eq_owner',  name:'实际控制人',    col:1, row:0, icon:'el-icon-user',            kind:'main',    route:'/equityPenetration/beneficialOwner' },
            { id:'eq_chain',  name:'控制链分析',    col:2, row:0, icon:'el-icon-share',           kind:'main',    route:'/equityPenetration/controlChain' },
            { id:'eq_struct', name:'股权结构',      col:3, row:0, icon:'el-icon-connection',      kind:'main',    route:'/equityPenetration/equityStructure' },
            { id:'eq_holder', name:'股东穿透分析',  col:4, row:0, icon:'el-icon-s-custom',        kind:'sub',     route:'/equityPenetration/shareholderAnalysis' },
            { id:'eq_change', name:'股权变动',      col:5, row:0, icon:'el-icon-refresh',         kind:'sub',     route:'/equityPenetration/equityChanges' },
            { id:'eq_cca',    name:'控制链统计',    col:6, row:0, icon:'el-icon-data-line',       kind:'sub',     route:'/equityPenetration/controlChainAnalysis' },
            { id:'eq_invest', name:'投资项目',      col:7, row:0, icon:'el-icon-folder-add',      kind:'sub',     route:'/equityPenetration/investmentDecision' },
            { id:'eq_fin',    name:'融资记录台账',  col:0, row:1, icon:'el-icon-bank-card',       kind:'finance', route:'/equityPenetration/Rzjltz' },
            { id:'eq_guar',   name:'担保记录台账',  col:1, row:1, icon:'el-icon-s-order',         kind:'finance', route:'/equityPenetration/Dbjltz' },
            { id:'eq_loan',   name:'委托贷款监控',  col:2, row:1, icon:'el-icon-money',           kind:'finance', route:'/equityPenetration/Wtdkjk' },
            { id:'eq_deriv',  name:'衍生品监控',    col:3, row:1, icon:'el-icon-s-finance',       kind:'finance', route:'/equityPenetration/Yspywjk' },
            { id:'eq_liq',    name:'流动性风险',    col:4, row:1, icon:'el-icon-warning',         kind:'warn',    route:'/equityPenetration/Ldxfxfx', alert:true },
            { id:'eq_drill',  name:'金融风险穿透',  col:5, row:1, icon:'el-icon-data-analysis',   kind:'warn',    route:'/equityPenetration/Jrfxctfx' },
            { id:'eq_dash',   name:'金融风险驾驶舱',col:6, row:1, icon:'el-icon-s-platform',      kind:'main',    route:'/equityPenetration/Jrfxjsc' },
          ],
          edges: [
            ['eq_home','eq_owner','main'],['eq_owner','eq_chain','main'],['eq_chain','eq_struct','main'],
            ['eq_struct','eq_holder','sub'],['eq_struct','eq_cca','sub'],['eq_struct','eq_change','sub'],['eq_struct','eq_invest','sub'],
            ['eq_home','eq_fin','finance'],['eq_fin','eq_guar','finance'],['eq_guar','eq_loan','finance'],
            ['eq_loan','eq_deriv','finance'],['eq_deriv','eq_liq','warn'],['eq_liq','eq_drill','warn'],['eq_drill','eq_dash','main'],
          ],
        },
        // ==================== 经营领域：采购穿透 ====================
        {
          id: 'procurement', name: '采购穿透', color: '#FA8C16', icon: 'el-icon-goods',
          route: '/fxyj/CgctIndex',
          nodes: [
            { id:'pr_home',  name:'采购穿透首页',  col:0, row:0, icon:'el-icon-s-home',           kind:'main',    route:'/fxyj/CgctIndex' },
            { id:'pr_proj',  name:'采购项目台账',  col:1, row:0, icon:'el-icon-tickets',          kind:'main',    route:'/fxyj/Cgxmtz' },
            { id:'pr_rec',   name:'采购台账',      col:2, row:0, icon:'el-icon-document',         kind:'main',    route:'/fxyj/Cgtz' },
            { id:'pr_sup',   name:'供应商台账',    col:0, row:1, icon:'el-icon-s-cooperation',    kind:'sub',     route:'/fxyj/Gtstz' },
            { id:'pr_sprof', name:'供应商档案',    col:1, row:1, icon:'el-icon-folder',           kind:'sub',     route:'/fxyj/Gysda' },
            { id:'pr_bid',   name:'招标过程监控',  col:2, row:1, icon:'el-icon-view',             kind:'main',    route:'/fxyj/Zbgcjk' },
            { id:'pr_bidc',  name:'招投标合规',    col:3, row:1, icon:'el-icon-s-check',          kind:'sub',     route:'/fxyj/Ztbhg' },
            { id:'pr_price', name:'采购价格对标',  col:4, row:1, icon:'el-icon-data-line',        kind:'sub',     route:'/fxyj/Cgjgdb' },
            { id:'pr_rel',   name:'关联交易监控',  col:0, row:2, icon:'el-icon-connection',       kind:'finance', route:'/fxyj/Gljyjk' },
            { id:'pr_fake',  name:'虚假贸易核查',  col:1, row:2, icon:'el-icon-warning-outline',  kind:'warn',    route:'/fxyj/Xjmyhc' },
            { id:'pr_risk',  name:'供应链风险',    col:2, row:2, icon:'el-icon-warning',          kind:'warn',    route:'/fxyj/Gylfxfx' },
            { id:'pr_drill', name:'采购风险穿透',  col:3, row:2, icon:'el-icon-data-analysis',    kind:'main',    route:'/fxyj/Cgfxct' },
            { id:'pr_dash',  name:'采购驾驶舱',    col:4, row:2, icon:'el-icon-s-platform',       kind:'main',    route:'/fxyj/Cgjkjsc' },
          ],
          edges: [
            ['pr_home','pr_proj','main'],['pr_proj','pr_rec','main'],['pr_rec','pr_bid','main'],
            ['pr_proj','pr_sup','sub'],['pr_sup','pr_sprof','sub'],
            ['pr_bid','pr_bidc','sub'],['pr_bid','pr_price','sub'],
            ['pr_proj','pr_rel','finance'],['pr_rec','pr_fake','warn'],['pr_fake','pr_risk','warn'],
            ['pr_risk','pr_drill','main'],['pr_drill','pr_dash','main'],
          ],
        },
        // ==================== 经营领域：军品穿透 ====================
        {
          id: 'military', name: '军品穿透', color: '#FF4D4F', icon: 'el-icon-s-flag',
          route: '/riskPenetration/JpctIndex',
          nodes: [
            { id:'ml_home',  name:'军品穿透首页',  col:0, row:0, icon:'el-icon-s-home',            kind:'main',    route:'/riskPenetration/JpctIndex' },
            { id:'ml_task',  name:'军品任务台账',  col:1, row:0, icon:'el-icon-s-flag',            kind:'main',    route:'/riskPenetration/Jprwtz' },
            { id:'ml_taskr', name:'任务台账',      col:2, row:0, icon:'el-icon-tickets',           kind:'main',    route:'/riskPenetration/taskRecord' },
            { id:'ml_qual',  name:'军品资质管理',  col:0, row:1, icon:'el-icon-s-check',           kind:'main',    route:'/riskPenetration/Jpzzgl' },
            { id:'ml_qualp', name:'资质档案',      col:1, row:1, icon:'el-icon-folder',            kind:'sub',     route:'/riskPenetration/qualificationProfile' },
            { id:'ml_sec',   name:'军品保密管理',  col:2, row:1, icon:'el-icon-lock',              kind:'warn',    route:'/riskPenetration/Jpbmgl' },
            { id:'ml_qual2', name:'军品质量管理',  col:0, row:2, icon:'el-icon-s-order',           kind:'main',    route:'/riskPenetration/Jpzlgl' },
            { id:'ml_sub',   name:'分包合规',      col:1, row:2, icon:'el-icon-document-checked',  kind:'sub',     route:'/riskPenetration/subcontractCompliance' },
            { id:'ml_sc',    name:'供应链安全',    col:2, row:2, icon:'el-icon-connection',        kind:'sub',     route:'/riskPenetration/supplyChainSecurity' },
            { id:'ml_exec',  name:'合同履约追踪',  col:3, row:2, icon:'el-icon-finished',          kind:'finance', route:'/riskPenetration/contractExecution' },
            { id:'ml_drill', name:'军品风险穿透',  col:4, row:2, icon:'el-icon-data-analysis',     kind:'warn',    route:'/riskPenetration/drillDown' },
            { id:'ml_dash',  name:'军品驾驶舱',    col:5, row:2, icon:'el-icon-s-platform',        kind:'main',    route:'/riskPenetration/Jpjkjsc' },
          ],
          edges: [
            ['ml_home','ml_task','main'],['ml_task','ml_taskr','main'],
            ['ml_task','ml_qual','main'],['ml_qual','ml_qualp','sub'],['ml_task','ml_sec','warn'],
            ['ml_taskr','ml_qual2','main'],['ml_qual2','ml_sub','sub'],['ml_qual2','ml_sc','sub'],
            ['ml_sub','ml_exec','finance'],['ml_sec','ml_drill','warn'],['ml_drill','ml_dash','main'],
          ],
        },
        // ==================== 经营领域：境外穿透 ====================
        {
          id: 'overseas', name: '境外穿透', color: '#722ED1', icon: 'el-icon-location',
          route: '/enterprise/JwctIndex',
          nodes: [
            { id:'ov_home',   name:'境外穿透首页',  col:0, row:0, icon:'el-icon-s-home',            kind:'main',    route:'/enterprise/JwctIndex' },
            { id:'ov_unit',   name:'境外单位台账',  col:1, row:0, icon:'el-icon-office-building',   kind:'main',    route:'/enterprise/Jwdwtz' },
            { id:'ov_edash',  name:'企业管理驾驶舱',col:2, row:0, icon:'el-icon-s-data',            kind:'sub',     route:'/enterprise/qydashboard' },
            { id:'ov_lead',   name:'负责人信息',    col:3, row:0, icon:'el-icon-user',              kind:'sub',     route:'/enterprise/management' },
            { id:'ov_eval',   name:'负责人考核',    col:4, row:0, icon:'el-icon-s-check',           kind:'sub',     route:'/enterprise/evaluation' },
            { id:'ov_fin',    name:'财务报表',      col:0, row:1, icon:'el-icon-money',             kind:'finance', route:'/enterprise/qyfinancial' },
            { id:'ov_hr',     name:'人力资源',      col:1, row:1, icon:'el-icon-user',              kind:'finance', route:'/enterprise/qyhr' },
            { id:'ov_op',     name:'生产经营',      col:2, row:1, icon:'el-icon-s-order',           kind:'finance', route:'/enterprise/operation' },
            { id:'ov_innov',  name:'技术创新',      col:3, row:1, icon:'el-icon-magic-stick',       kind:'sub',     route:'/enterprise/innovation' },
            { id:'ov_strat',  name:'战略管理',      col:4, row:1, icon:'el-icon-s-grid',            kind:'sub',     route:'/enterprise/qystrategy' },
            { id:'ov_invest', name:'境外投资管理',  col:0, row:2, icon:'el-icon-data-analysis',     kind:'main',    route:'/enterprise/Jwtzgl' },
            { id:'ov_crisk',  name:'国别风险地图',  col:1, row:2, icon:'el-icon-map-location',      kind:'warn',    route:'/enterprise/Gbfxdt', alert:true },
            { id:'ov_forex',  name:'外汇风险分析',  col:2, row:2, icon:'el-icon-s-finance',         kind:'warn',    route:'/enterprise/Whfxfx' },
            { id:'ov_comp',   name:'境外合规管理',  col:3, row:2, icon:'el-icon-document-checked',  kind:'sub',     route:'/enterprise/Jwhggl' },
            { id:'ov_opanal', name:'境外经营分析',  col:4, row:2, icon:'el-icon-data-line',         kind:'main',    route:'/enterprise/Jwjyfx' },
            { id:'ov_safe',   name:'人员安全管理',  col:5, row:2, icon:'el-icon-lock',              kind:'warn',    route:'/enterprise/Jwryaqgl' },
            { id:'ov_emg',    name:'应急指挥中心',  col:6, row:2, icon:'el-icon-phone',             kind:'warn',    route:'/enterprise/Jwyjzhzx' },
            { id:'ov_dash',   name:'境外驾驶舱',    col:7, row:2, icon:'el-icon-s-platform',        kind:'main',    route:'/enterprise/Jwjkjsc' },
          ],
          edges: [
            ['ov_home','ov_unit','main'],['ov_unit','ov_edash','sub'],['ov_unit','ov_lead','sub'],
            ['ov_lead','ov_eval','sub'],
            ['ov_edash','ov_fin','finance'],['ov_edash','ov_hr','finance'],['ov_edash','ov_op','finance'],
            ['ov_edash','ov_innov','sub'],['ov_edash','ov_strat','sub'],
            ['ov_unit','ov_invest','main'],['ov_invest','ov_crisk','warn'],['ov_crisk','ov_forex','warn'],
            ['ov_unit','ov_comp','sub'],['ov_fin','ov_opanal','main'],['ov_opanal','ov_dash','main'],
            ['ov_crisk','ov_safe','warn'],['ov_safe','ov_emg','warn'],
          ],
        },
        // ==================== 载体：合同穿透 ====================
        {
          id: 'contract', name: '合同穿透', color: '#EB2F96', icon: 'el-icon-document-checked',
          route: '/monitorExecute/HtctIndex',
          nodes: [
            { id:'ct_home',   name:'合同穿透首页',  col:0, row:0, icon:'el-icon-s-home',            kind:'main',    route:'/monitorExecute/HtctIndex' },
            { id:'ct_rec',    name:'合同记录台账',  col:1, row:0, icon:'el-icon-tickets',           kind:'main',    route:'/monitorExecute/Htjltz' },
            { id:'ct_list',   name:'合同台账管理',  col:2, row:0, icon:'el-icon-document',          kind:'main',    route:'/monitorExecute/Httzgl' },
            { id:'ct_dash',   name:'合同驾驶舱',    col:3, row:0, icon:'el-icon-s-platform',        kind:'main',    route:'/monitorExecute/Htjkjsc' },
            { id:'ct_appr',   name:'合同审批追踪',  col:0, row:1, icon:'el-icon-s-check',           kind:'main',    route:'/monitorExecute/Htspzz' },
            { id:'ct_comp',   name:'审批合规追踪',  col:1, row:1, icon:'el-icon-document-checked',  kind:'sub',     route:'/monitorExecute/Htsphgzz' },
            { id:'ct_lc',     name:'合同生命周期',  col:2, row:1, icon:'el-icon-time',              kind:'sub',     route:'/monitorExecute/Htsmzq' },
            { id:'ct_perf',   name:'合同履行监控',  col:3, row:1, icon:'el-icon-s-order',           kind:'main',    route:'/monitorExecute/Htlxjk' },
            { id:'ct_exec',   name:'履行监控',      col:4, row:1, icon:'el-icon-finished',          kind:'main',    route:'/monitorExecute/Htlxjk1' },
            { id:'ct_credit', name:'对方信用分析',  col:5, row:1, icon:'el-icon-medal',             kind:'finance', route:'/monitorExecute/Dfxyfx' },
            { id:'ct_counter',name:'对方信用监控',  col:6, row:1, icon:'el-icon-user',              kind:'finance', route:'/monitorExecute/Dfxyjk' },
            { id:'ct_warn',   name:'风险预警管理',  col:7, row:1, icon:'el-icon-warning',           kind:'warn',    route:'/monitorExecute/Fxyjgl', alert:true },
            { id:'ct_disp',   name:'合同纠纷台账',  col:0, row:2, icon:'el-icon-warning-outline',   kind:'warn',    route:'/monitorExecute/Htjftz' },
            { id:'ct_case',   name:'法律纠纷案件',  col:1, row:2, icon:'el-icon-s-flag',            kind:'warn',    route:'/monitorExecute/Fljfajgl' },
            { id:'ct_smart',  name:'合同智能审查',  col:2, row:2, icon:'el-icon-cpu',               kind:'sub',     route:'/monitorExecute/Htznsc' },
            { id:'ct_drill',  name:'合同穿透分析',  col:3, row:2, icon:'el-icon-data-analysis',     kind:'main',    route:'/monitorExecute/Htctfx' },
          ],
          edges: [
            ['ct_home','ct_rec','main'],['ct_rec','ct_list','main'],['ct_list','ct_dash','main'],
            ['ct_list','ct_appr','main'],['ct_appr','ct_comp','sub'],['ct_appr','ct_lc','sub'],
            ['ct_list','ct_perf','main'],['ct_perf','ct_exec','main'],
            ['ct_exec','ct_credit','finance'],['ct_credit','ct_counter','finance'],
            ['ct_exec','ct_warn','warn'],['ct_perf','ct_disp','warn'],['ct_disp','ct_case','warn'],
            ['ct_case','ct_smart','sub'],['ct_warn','ct_drill','main'],
          ],
        },
        // ==================== 载体：资金穿透 ====================
        {
          id: 'fund', name: '资金穿透', color: '#08979C', icon: 'el-icon-coin',
          route: '/ruleMonitor/ZjctIndex',
          nodes: [
            { id:'fu_home',    name:'资金穿透首页', col:0, row:0, icon:'el-icon-s-home',           kind:'main',    route:'/ruleMonitor/ZjctIndex' },
            { id:'fu_skdata',  name:'司库数据',     col:1, row:0, icon:'el-icon-bank-card',        kind:'finance', route:'/ruleMonitor/TableQuerySK' },
            { id:'fu_cwdata',  name:'财务数据',     col:2, row:0, icon:'el-icon-money',            kind:'finance', route:'/ruleMonitor/TableQueryCW' },
            { id:'fu_htdata',  name:'合同数据',     col:3, row:0, icon:'el-icon-document',         kind:'finance', route:'/ruleMonitor/TableQueryHT' },
            { id:'fu_fpdata',  name:'发票数据',     col:4, row:0, icon:'el-icon-document-checked', kind:'finance', route:'/ruleMonitor/TableQueryFP' },
            { id:'fu_hrdata',  name:'人员数据',     col:5, row:0, icon:'el-icon-user',             kind:'finance', route:'/ruleMonitor/TableQueryRY' },
            { id:'fu_sjdata',  name:'司法数据穿透', col:0, row:1, icon:'el-icon-s-flag',           kind:'sub',     route:'/ruleMonitor/Telescope' },
            { id:'fu_portrait',name:'资金穿透画像', col:1, row:1, icon:'el-icon-s-custom',         kind:'main',    route:'/ruleMonitor/GZQYHX' },
            { id:'fu_dp',      name:'数据穿透',     col:2, row:1, icon:'el-icon-connection',       kind:'main',    route:'/ruleMonitor/HBSJCT' },
            { id:'fu_dsrc',    name:'数据源管理',   col:3, row:1, icon:'el-icon-folder',           kind:'sub',     route:'/ruleMonitor/BaseMXSjygl' },
            { id:'fu_model',   name:'数据模型管理', col:0, row:2, icon:'el-icon-s-grid',           kind:'main',    route:'/ruleMonitor/MXsjmxgl' },
            { id:'fu_pgmodel', name:'评估模型',     col:1, row:2, icon:'el-icon-s-opportunity',    kind:'main',    route:'/ruleMonitor/PGmxgl' },
            { id:'fu_warn',    name:'资金风险预警', col:2, row:2, icon:'el-icon-warning',          kind:'warn',    route:'/ruleMonitor/FXYJ', alert:true },
            { id:'fu_wdata',   name:'预警数据',     col:3, row:2, icon:'el-icon-data-line',        kind:'warn',    route:'/ruleMonitor/MXfxyjgl' },
          ],
          edges: [
            ['fu_home','fu_skdata','finance'],['fu_skdata','fu_cwdata','finance'],['fu_cwdata','fu_htdata','finance'],
            ['fu_htdata','fu_fpdata','finance'],['fu_fpdata','fu_hrdata','finance'],
            ['fu_home','fu_sjdata','sub'],['fu_sjdata','fu_portrait','main'],['fu_skdata','fu_dp','main'],
            ['fu_dp','fu_dsrc','sub'],
            ['fu_portrait','fu_model','main'],['fu_dp','fu_model','main'],
            ['fu_model','fu_pgmodel','main'],['fu_pgmodel','fu_warn','warn'],['fu_warn','fu_wdata','warn'],
          ],
        },
        // ==================== 内外部数据融合 ====================
        {
          id: 'dataFusion', name: '内外部数据融合', color: '#D46B08', icon: 'el-icon-connection',
          route: '/ruleMonitor/GZQYHX',
          nodes: [
            { id:'df_portrait',name:'资金穿透画像', col:0, row:0, icon:'el-icon-s-custom',          kind:'main',    route:'/ruleMonitor/GZQYHX' },
            { id:'df_dp',      name:'数据穿透',     col:1, row:0, icon:'el-icon-connection',        kind:'main',    route:'/ruleMonitor/HBSJCT' },
            { id:'df_sk',      name:'司库数据',     col:2, row:0, icon:'el-icon-bank-card',         kind:'finance', route:'/ruleMonitor/TableQuerySK' },
            { id:'df_cw',      name:'财务数据',     col:3, row:0, icon:'el-icon-money',             kind:'finance', route:'/ruleMonitor/TableQueryCW' },
            { id:'df_ht',      name:'合同数据',     col:4, row:0, icon:'el-icon-document',          kind:'finance', route:'/ruleMonitor/TableQueryHT' },
            { id:'df_fp',      name:'发票数据',     col:5, row:0, icon:'el-icon-document-checked',  kind:'finance', route:'/ruleMonitor/TableQueryFP' },
            { id:'df_hr',      name:'人员数据',     col:6, row:0, icon:'el-icon-user',              kind:'finance', route:'/ruleMonitor/TableQueryRY' },
            { id:'df_sj',      name:'司法数据穿透', col:7, row:0, icon:'el-icon-s-flag',            kind:'sub',     route:'/ruleMonitor/Telescope' },
            { id:'df_model',   name:'数据模型管理', col:0, row:1, icon:'el-icon-s-grid',            kind:'main',    route:'/ruleMonitor/MXsjmxgl' },
            { id:'df_pgm',     name:'评估模型',     col:1, row:1, icon:'el-icon-s-opportunity',     kind:'main',    route:'/ruleMonitor/PGmxgl' },
            { id:'df_dsrc',    name:'数据源管理',   col:2, row:1, icon:'el-icon-folder',            kind:'sub',     route:'/ruleMonitor/BaseMXSjygl' },
          ],
          edges: [
            ['df_portrait','df_dp','main'],['df_sk','df_cw','finance'],['df_cw','df_ht','finance'],
            ['df_ht','df_fp','finance'],['df_fp','df_hr','finance'],['df_dp','df_sj','sub'],
            ['df_portrait','df_model','main'],['df_dp','df_model','main'],
            ['df_model','df_pgm','main'],['df_model','df_dsrc','sub'],
          ],
        },
        // ==================== 预警处置 ====================
        {
          id: 'warning', name: '预警处置', color: '#A8071A', icon: 'el-icon-warning',
          route: '/ruleMonitor/FXYJ',
          nodes: [
            { id:'wa_risk',    name:'风险预警',       col:0, row:0, icon:'el-icon-warning',         kind:'warn',    route:'/ruleMonitor/FXYJ', alert:true },
            { id:'wa_rdata',   name:'风险预警数据',   col:1, row:0, icon:'el-icon-data-line',       kind:'warn',    route:'/ruleMonitor/MXfxyjgl' },
            { id:'wa_issues',  name:'问题汇总',       col:2, row:0, icon:'el-icon-tickets',         kind:'warn',    route:'/ruleMonitor/AUditIssuesCollect' },
            { id:'wa_dash',    name:'监管驾驶舱',     col:3, row:0, icon:'el-icon-s-platform',      kind:'main',    route:'/stateAssetsConfig/dashboard' },
            { id:'wa_report',  name:'监管报告',       col:4, row:0, icon:'el-icon-document',        kind:'main',    route:'/report/index' },
            { id:'wa_screen',  name:'国资穿透总览',   col:5, row:0, icon:'el-icon-view',            kind:'main',    route:'/stateAssetsConfig/gzctzl' },
            { id:'wa_collab',  name:'监督指令协同',   col:0, row:1, icon:'el-icon-s-promotion',     kind:'sub',     route:'/stateAssetsConfig/hby-admin/src/views/stateAssets/collaboration/supervisionInstruction/index.vue' },
            { id:'wa_rcollab', name:'风险协同预警',   col:1, row:1, icon:'el-icon-bell',            kind:'warn',    route:'/stateAssetsConfig/riskCollaboration' },
            { id:'wa_intel',   name:'智能风险识别',   col:2, row:1, icon:'el-icon-cpu',             kind:'main',    route:'/stateAssetsConfig/riskIdentification' },
            { id:'wa_ai',      name:'智能决策支持',   col:3, row:1, icon:'el-icon-s-opportunity',   kind:'main',    route:'/stateAssetsConfig/decisionSupport' },
          ],
          edges: [
            ['wa_risk','wa_rdata','warn'],['wa_rdata','wa_issues','warn'],['wa_issues','wa_dash','main'],
            ['wa_dash','wa_report','main'],['wa_dash','wa_screen','main'],
            ['wa_issues','wa_collab','sub'],['wa_collab','wa_rcollab','warn'],
            ['wa_rcollab','wa_intel','main'],['wa_intel','wa_ai','main'],
          ],
        },
        // ==================== 管理领域：会计穿透 ====================
        {
          id: 'accounting', name: '会计穿透', color: '#096DD9', icon: 'el-icon-notebook-2',
          route: '/compliance/KjctSy',
          nodes: [
            { id:'acc_home',     name:'会计穿透首页',   col:0, row:0, icon:'el-icon-s-home',         kind:'main',    route:'/compliance/KjctSy' },
            { id:'acc_book',     name:'账簿数据',       col:1, row:0, icon:'el-icon-notebook-2',     kind:'main',    route:'/compliance/ZNFXaccountData' },
            { id:'acc_stmt',     name:'报表数据',       col:2, row:0, icon:'el-icon-tickets',        kind:'main',    route:'/compliance/statement' },
            { id:'acc_subj',     name:'科目表',         col:3, row:0, icon:'el-icon-menu',           kind:'main',    route:'/compliance/subject' },
            { id:'acc_bal',      name:'余额表',         col:4, row:0, icon:'el-icon-coin',           kind:'main',    route:'/compliance/balance' },
            { id:'acc_dash',     name:'会计驾驶舱',     col:5, row:0, icon:'el-icon-s-platform',     kind:'main',    route:'/compliance/Jkctjkjsc' },
            { id:'acc_voucher',  name:'凭证穿透',       col:0, row:1, icon:'el-icon-document',       kind:'main',    route:'/compliance/Kjpzct' },
            { id:'acc_vlib',     name:'凭证库',         col:1, row:1, icon:'el-icon-folder',         kind:'main',    route:'/compliance/voucherLib' },
            { id:'acc_qbook',    name:'账簿穿透查询',   col:2, row:1, icon:'el-icon-search',         kind:'main',    route:'/compliance/Zbctcx' },
            { id:'acc_cate',     name:'总分类账',       col:3, row:1, icon:'el-icon-notebook-1',     kind:'main',    route:'/compliance/accountCate' },
            { id:'acc_detail',   name:'明细账',         col:4, row:1, icon:'el-icon-detail',         kind:'main',    route:'/compliance/accountDetail' },
            { id:'acc_diary',    name:'日记帐',         col:5, row:1, icon:'el-icon-date',           kind:'main',    route:'/compliance/accountDiary' },
            { id:'acc_assist',   name:'辅助帐',         col:6, row:1, icon:'el-icon-s-grid',         kind:'main',    route:'/compliance/accountAssist' },
            { id:'acc_finrpt',   name:'财务报表穿透',   col:0, row:2, icon:'el-icon-data-analysis',  kind:'main',    route:'/compliance/Cwbbct' },
            { id:'acc_budget',   name:'预算执行监控',   col:1, row:2, icon:'el-icon-s-data',         kind:'main',    route:'/compliance/Yszxjk' },
            { id:'acc_twoasset', name:'两金压降监控',   col:2, row:2, icon:'el-icon-s-finance',      kind:'main',    route:'/compliance/Ljysjk' },
            { id:'acc_fraud',    name:'财务造假识别',   col:3, row:2, icon:'el-icon-warning',        kind:'warn',    route:'/compliance/Cwzjsb', alert:true },
            { id:'acc_glass',    name:'放大镜',         col:4, row:2, icon:'el-icon-zoom-in',        kind:'main',    route:'/compliance/magnifyingGlass' },
          ],
          edges: [
            ['acc_home','acc_book','main'],['acc_book','acc_stmt','main'],['acc_stmt','acc_subj','main'],
            ['acc_subj','acc_bal','main'],['acc_bal','acc_dash','main'],
            ['acc_home','acc_voucher','main'],['acc_voucher','acc_vlib','main'],['acc_vlib','acc_qbook','main'],
            ['acc_qbook','acc_cate','main'],['acc_cate','acc_detail','main'],['acc_detail','acc_diary','main'],
            ['acc_diary','acc_assist','main'],['acc_assist','acc_finrpt','main'],
            ['acc_finrpt','acc_budget','main'],['acc_budget','acc_twoasset','main'],
            ['acc_twoasset','acc_fraud','warn'],['acc_fraud','acc_glass','main'],
          ],
        },
        // ==================== 管理领域：财务穿透 ====================
        {
          id: 'financial', name: '财务穿透', color: '#389E0D', icon: 'el-icon-money',
          route: '/financialPenetration/home',
          nodes: [
            { id:'fin_home',     name:'财务穿透首页',   col:0, row:0, icon:'el-icon-s-home',         kind:'main',    route:'/financialPenetration/home' },
            { id:'fin_ledger',   name:'财务报表台账',   col:1, row:0, icon:'el-icon-tickets',        kind:'main',    route:'/financialPenetration/Cwbbtz' },
            { id:'fin_consolid', name:'合并财务分析',   col:2, row:0, icon:'el-icon-connection',     kind:'main',    route:'/financialPenetration/consolidatedAnalysis' },
            { id:'fin_fundflow', name:'资金流向分析',   col:3, row:0, icon:'el-icon-money',          kind:'main',    route:'/financialPenetration/fundFlow' },
            { id:'fin_dash',     name:'财务驾驶舱',     col:4, row:0, icon:'el-icon-s-platform',     kind:'main',    route:'/financialPenetration/Cwjkjsc' },
            { id:'fin_bench',    name:'财务指标对标',   col:5, row:0, icon:'el-icon-data-line',      kind:'main',    route:'/financialPenetration/benchmark' },
            { id:'fin_related',  name:'关联交易分析',   col:6, row:0, icon:'el-icon-share',          kind:'main',    route:'/financialPenetration/relatedTransaction' },
            { id:'fin_rledger',  name:'关联交易台账',   col:7, row:0, icon:'el-icon-notebook-2',     kind:'main',    route:'/financialPenetration/Gljytz' },
            { id:'fin_expense',  name:'费用管控',       col:0, row:1, icon:'el-icon-s-finance',      kind:'main',    route:'/financialPenetration/expenseMonitor' },
            { id:'fin_compli',   name:'财务合规',       col:1, row:1, icon:'el-icon-s-check',        kind:'main',    route:'/financialPenetration/financialCompliance' },
            { id:'fin_perf',     name:'财务绩效评价',   col:2, row:1, icon:'el-icon-s-data',         kind:'main',    route:'/financialPenetration/financialPerformance' },
            { id:'fin_risk',     name:'财务风险统计',   col:3, row:1, icon:'el-icon-warning',        kind:'warn',    route:'/financialPenetration/financialRisk', alert:true },
            { id:'fin_anomaly',  name:'异常检测',       col:4, row:1, icon:'el-icon-s-opportunity',  kind:'warn',    route:'/financialPenetration/anomalyDetect', alert:true },
            { id:'fin_drill',    name:'财务穿透分析',   col:5, row:1, icon:'el-icon-search',         kind:'main',    route:'/financialPenetration/drillDown' },
          ],
          edges: [
            ['fin_home','fin_ledger','main'],['fin_ledger','fin_consolid','main'],['fin_consolid','fin_fundflow','main'],
            ['fin_fundflow','fin_dash','main'],['fin_dash','fin_bench','main'],['fin_bench','fin_related','main'],
            ['fin_related','fin_rledger','main'],
            ['fin_home','fin_expense','main'],['fin_expense','fin_compli','main'],['fin_compli','fin_perf','main'],
            ['fin_perf','fin_risk','warn'],['fin_risk','fin_anomaly','warn'],['fin_anomaly','fin_drill','main'],
          ],
        },
        // ==================== 管理领域：薪酬穿透 ====================
        {
          id: 'salary', name: '薪酬穿透', color: '#531DAB', icon: 'el-icon-user',
          route: '/formMonitor/XcctIndex',
          nodes: [
            { id:'sal_home',     name:'薪酬穿透首页',   col:0, row:0, icon:'el-icon-s-home',         kind:'main',    route:'/formMonitor/XcctIndex' },
            { id:'sal_ledger',   name:'薪酬总额台账',   col:1, row:0, icon:'el-icon-tickets',        kind:'main',    route:'/formMonitor/Xczetz' },
            { id:'sal_mgmt',     name:'工资总额管理',   col:2, row:0, icon:'el-icon-s-management',   kind:'main',    route:'/formMonitor/salaryPenetration' },
            { id:'sal_dash',     name:'薪酬驾驶舱',     col:3, row:0, icon:'el-icon-s-platform',     kind:'main',    route:'/formMonitor/Xcjkjsc' },
            { id:'sal_exec',     name:'高管薪酬分配',   col:4, row:0, icon:'el-icon-user',           kind:'main',    route:'/formMonitor/Ggxcfx' },
            { id:'sal_benefit',  name:'效益联动分配',   col:5, row:0, icon:'el-icon-s-data',         kind:'main',    route:'/formMonitor/Xyldfx' },
            { id:'sal_incent',   name:'中长期激励',     col:6, row:0, icon:'el-icon-medal',          kind:'main',    route:'/formMonitor/Zcqjljh' },
            { id:'sal_cost',     name:'人工成本分析',   col:0, row:1, icon:'el-icon-coin',           kind:'main',    route:'/formMonitor/Rgcbfx' },
            { id:'sal_check',    name:'薪酬合规检查',   col:1, row:1, icon:'el-icon-s-check',        kind:'main',    route:'/formMonitor/Xchgswpc' },
            { id:'sal_warn',     name:'薪酬风险预警',   col:2, row:1, icon:'el-icon-warning',        kind:'warn',    route:'/formMonitor/salaryPenetrationriskWarning', alert:true },
            { id:'sal_drill',    name:'薪酬穿透分析',   col:3, row:1, icon:'el-icon-search',         kind:'main',    route:'/formMonitor/salaryPenetrationdrillDown' },
          ],
          edges: [
            ['sal_home','sal_ledger','main'],['sal_ledger','sal_mgmt','main'],['sal_mgmt','sal_dash','main'],
            ['sal_dash','sal_exec','main'],['sal_exec','sal_benefit','main'],['sal_benefit','sal_incent','main'],
            ['sal_home','sal_cost','main'],['sal_cost','sal_check','main'],['sal_check','sal_warn','warn'],['sal_warn','sal_drill','main'],
          ],
        },
        // ==================== 管理领域：产权穿透 ====================
        {
          id: 'property', name: '产权穿透', color: '#36CFC9', icon: 'el-icon-s-home',
          route: '/assetPenetration/CqctIndex',
          nodes: [
            { id:'prop_home',     name:'产权穿透首页',   col:0, row:0, icon:'el-icon-s-home',         kind:'main',    route:'/assetPenetration/CqctIndex' },
            { id:'prop_concent',  name:'资产集中度',     col:1, row:0, icon:'el-icon-s-grid',         kind:'main',    route:'/assetPenetration/assetConcentration' },
            { id:'prop_flow',     name:'资产流向追踪',   col:2, row:0, icon:'el-icon-location',       kind:'main',    route:'/assetPenetration/assetFlow' },
            { id:'prop_cross',    name:'交叉持股分析',   col:3, row:0, icon:'el-icon-share',          kind:'main',    route:'/assetPenetration/crossHolding' },
            { id:'prop_alloc',    name:'资产配置统计',   col:4, row:0, icon:'el-icon-data-line',      kind:'main',    route:'/assetPenetration/assetAllocation' },
            { id:'prop_dash',     name:'产权驾驶舱',     col:5, row:0, icon:'el-icon-s-platform',     kind:'main',    route:'/assetPenetration/Cqjkjsc' },
            { id:'prop_chart',    name:'股权穿透图',     col:6, row:0, icon:'el-icon-connection',     kind:'main',    route:'/assetPenetration/Cqctt' },
            { id:'prop_equity',   name:'股权结构分析',   col:0, row:1, icon:'el-icon-share',          kind:'main',    route:'/assetPenetration/Gqjgfx' },
            { id:'prop_chain',    name:'控制链分析',     col:1, row:1, icon:'el-icon-link',           kind:'main',    route:'/assetPenetration/Kzlfx' },
            { id:'prop_owner',    name:'受益所有人',     col:2, row:1, icon:'el-icon-user',           kind:'main',    route:'/assetPenetration/Sysyr' },
            { id:'prop_share',    name:'股东分析',       col:3, row:1, icon:'el-icon-s-custom',       kind:'main',    route:'/assetPenetration/Gdfx' },
            { id:'prop_registry', name:'产权登记台账',   col:4, row:1, icon:'el-icon-tickets',        kind:'main',    route:'/assetPenetration/propertyPenetrationregistryList' },
            { id:'prop_register', name:'产权登记',       col:5, row:1, icon:'el-icon-document',       kind:'main',    route:'/assetPenetration/Cqdjtz' },
            { id:'prop_change',   name:'股权变动纪录',   col:6, row:1, icon:'el-icon-refresh',        kind:'main',    route:'/assetPenetration/Gqbdjl' },
            { id:'prop_trade',    name:'产权交易台账',   col:0, row:2, icon:'el-icon-notebook-2',     kind:'main',    route:'/assetPenetration/Cqjytz' },
            { id:'prop_review',   name:'产权交易审查',   col:1, row:2, icon:'el-icon-s-check',        kind:'main',    route:'/assetPenetration/propertyPenetrationtradeReview' },
            { id:'prop_operate',  name:'参股企业经营',   col:2, row:2, icon:'el-icon-office-building', kind:'main',    route:'/assetPenetration/Cgqyfx' },
          ],
          edges: [
            ['prop_home','prop_concent','main'],['prop_concent','prop_flow','main'],['prop_flow','prop_cross','main'],
            ['prop_cross','prop_alloc','main'],['prop_alloc','prop_dash','main'],['prop_dash','prop_chart','main'],
            ['prop_home','prop_equity','main'],['prop_equity','prop_chain','main'],['prop_chain','prop_owner','main'],
            ['prop_owner','prop_share','main'],['prop_share','prop_registry','main'],['prop_registry','prop_register','main'],
            ['prop_register','prop_change','main'],['prop_change','prop_trade','main'],
            ['prop_trade','prop_review','main'],['prop_review','prop_operate','main'],
          ],
        },
      ],
      welcomeStats: [
        { label: '监管企业总数', value: '--', color: '#1890FF' },
        { label: '覆盖穿透领域', value: '12个', color: '#52C41A' },
        { label: '风险预警', value: '0条', color: '#FF4D4F' },
        { label: '待处理事项', value: '0个', color: '#FA8C16' },
        { label: '本期报告', value: '0份', color: '#722ED1' },
        { label: '异常检测', value: '0项', color: '#EB2F96' },
      ],
      kpiCards: [],
      domains: [
        { id: 'invest', name: '投资穿透', desc: '项目台账/决策合规/投后评价', icon: 'el-icon-data-analysis', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', kind: 'core', count: 7, route: '/modelMonitor/TzctIndex' },
        { id: 'equity', name: '金融穿透', desc: '股权结构/控制链/股东分析', icon: 'el-icon-share', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', kind: 'core', count: 14, route: '/equityPenetration/JrctIndex' },
        { id: 'procurement', name: '采购穿透', desc: '项目台账/供应商/招投标', icon: 'el-icon-goods', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', kind: 'core', count: 12, route: '/fxyj/CgctIndex' },
        { id: 'military', name: '军品穿透', desc: '任务台账/资质/质量/保密', icon: 'el-icon-s-flag', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', kind: 'core', count: 9, route: '/riskPenetration/JpctIndex' },
        { id: 'overseas', name: '境外穿透', desc: '境外单位/投资/国别风险', icon: 'el-icon-location', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', kind: 'core', count: 13, route: '/enterprise/JwctIndex' },
        { id: 'industry', name: '行业穿透', desc: '能源/金融/制造/基础设施', icon: 'el-icon-s-grid', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', kind: 'core', count: 10, route: '/industry/HyctIndex' },
        { id: 'contract', name: '合同穿透', desc: '台账/审批/履行/纠纷', icon: 'el-icon-document-checked', iconBg: 'rgba(235,47,150,0.1)', iconColor: '#EB2F96', kind: 'core', count: 11, route: '/monitorExecute/HtctIndex' },
        { id: 'accounting', name: '会计穿透', desc: '凭证/账簿/报表/预算', icon: 'el-icon-notebook-2', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', kind: 'core', count: 23, route: '/compliance/KjctSy' },
        { id: 'financial', name: '财务穿透', desc: '合并分析/资金流/绩效', icon: 'el-icon-money', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', kind: 'core', count: 14, route: '/financialPenetration/home' },
        { id: 'fund', name: '资金穿透', desc: '资金画像/数据穿透/风控', icon: 'el-icon-coin', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', kind: 'core', count: 6, route: '/ruleMonitor/ZjctIndex' },
        { id: 'salary', name: '薪酬穿透', desc: '总额台账/高管/激励/合规', icon: 'el-icon-user', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', kind: 'core', count: 10, route: '/formMonitor/XcctIndex' },
        { id: 'property', name: '产权穿透', desc: '产权登记/股权变动/交易', icon: 'el-icon-s-home', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', kind: 'core', count: 17, route: '/assetPenetration/CqctIndex' },
      ],
      riskList: [],
      pendingList: [],
      abnormalList: [],
      domainStats: [],
      reportList: [],
      keyProgresses: [],
      modules: [
        { name: '投资穿透', icon: 'el-icon-data-analysis', desc: '项目/决策/投后', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', route: '/modelMonitor/TzctIndex' },
        { name: '金融穿透', icon: 'el-icon-share', desc: '股权/控制链/风险', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', route: '/equityPenetration/JrctIndex' },
        { name: '采购穿透', icon: 'el-icon-goods', desc: '台账/供应商/招标', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', route: '/fxyj/CgctIndex' },
        { name: '军品穿透', icon: 'el-icon-s-flag', desc: '任务/资质/质量', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', route: '/riskPenetration/JpctIndex' },
        { name: '境外穿透', icon: 'el-icon-location', desc: '境外/投资/风险', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', route: '/enterprise/JwctIndex' },
        { name: '行业穿透', icon: 'el-icon-s-grid', desc: '能源/金融/制造', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', route: '/industry/HyctIndex' },
        { name: '合同穿透', icon: 'el-icon-document-checked', desc: '台账/履行/纠纷', iconBg: 'rgba(235,47,150,0.1)', iconColor: '#EB2F96', route: '/monitorExecute/HtctIndex' },
        { name: '会计穿透', icon: 'el-icon-notebook-2', desc: '凭证/账簿/报表', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', route: '/compliance/KjctSy' },
        { name: '财务穿透', icon: 'el-icon-money', desc: '合并/资金流/绩效', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', route: '/financialPenetration/home' },
        { name: '资金穿透', icon: 'el-icon-coin', desc: '画像/数据/风控', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', route: '/ruleMonitor/ZjctIndex' },
        { name: '薪酬穿透', icon: 'el-icon-user', desc: '总额/高管/激励', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', route: '/formMonitor/XcctIndex' },
        { name: '产权穿透', icon: 'el-icon-s-home', desc: '登记/变动/交易', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', route: '/assetPenetration/CqctIndex' },
        { name: '监管报告', icon: 'el-icon-tickets', desc: '编制/分发/质控', iconBg: 'rgba(235,47,150,0.1)', iconColor: '#EB2F96', route: '/report/index' },
        { name: '监管驾驶舱', icon: 'el-icon-s-platform', desc: '综合监管总览', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', route: '/stateAssetsConfig/dashboard' },
        { name: '智能风控总览', icon: 'el-icon-s-cooperation', desc: '大数据风控', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', route: '/stateAssetsConfig/dsjzldp' },
        { name: '企业信息', icon: 'el-icon-office-building', desc: '企业基本信息库', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', route: '/stateAssetsConfig/enterprise' },
      ],
      aiFeatures: [
        { name: '智能数据分析', icon: 'el-icon-data-line', desc: '多维度数据穿透智能分析', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/stateAssetsConfig/dataAnalysis' },
        { name: '智能决策分析', icon: 'el-icon-s-opportunity', desc: 'AI辅助监管决策支持', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/stateAssetsConfig/decisionSupport' },
        { name: '智能风险识别', icon: 'el-icon-warning-outline', desc: '自动识别异常与风险模式', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/stateAssetsConfig/riskIdentification' },
        { name: '国有资本布局', icon: 'el-icon-connection', desc: '国有资本战略布局分析', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/stateAssetsConfig/capitalLayout' },
        { name: '决策支持分析', icon: 'el-icon-s-marketing', desc: '多因素综合决策模型', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/stateAssetsConfig/decisionAnalysis' },
        { name: '信用风险穿透', icon: 'el-icon-medal', desc: '企业信用综合评估分析', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/stateAssetsConfig/xyfxdp' },
      ],
      configItems: [
        { name: '监管驾驶舱', icon: 'el-icon-s-platform', desc: '国资综合监管总览', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/stateAssetsConfig/dashboard' },
        { name: '国资穿透总览', icon: 'el-icon-view', desc: '全域穿透可视化大屏', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/stateAssetsConfig/gzctzl' },
        { name: '国资财经总览', icon: 'el-icon-money', desc: '财经运行综合总览', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/stateAssetsConfig/czyxzl' },
        { name: '数据报送任务', icon: 'el-icon-upload2', desc: '数据报送与采集管理', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/stateAssetsConfig/dataCollection' },
        { name: '数据协同', icon: 'el-icon-refresh', desc: '跨系统数据协同管理', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/stateAssetsConfig/dataCollaboration' },
        { name: '风险协同预警', icon: 'el-icon-bell', desc: '多方协同风险预警', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/stateAssetsConfig/riskCollaboration' },
        { name: '监管指令协同', icon: 'el-icon-s-promotion', desc: '监管指令下达与跟踪', bgColor: 'rgba(235,47,150,0.08)', iconColor: '#EB2F96', route: '/stateAssetsConfig/hby-admin/src/views/stateAssets/collaboration/supervisionInstruction/index.vue' },
        { name: '知识管理', icon: 'el-icon-reading', desc: '监管知识库管理', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/stateAssetsConfig/knowledgeManagement' },
        { name: '政策管理', icon: 'el-icon-document', desc: '监管政策法规管理', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/stateAssetsConfig/policyManagement' },
        { name: '监控报告编制', icon: 'el-icon-edit', desc: '监管报告编制工具', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/report/index' },
        { name: '自定义报告', icon: 'el-icon-s-tools', desc: '自定义报告模板配置', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/report/customer' },
        { name: '报告模板', icon: 'el-icon-files', desc: '标准报告模板库', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route:'/report/template' },
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
    // ===== 各领域全景卡片分组 =====
    cardGroups() {
      const groupDefs = [
        { id:'industry', name:'行业领域', icon:'el-icon-s-grid',        color:'#13C2C2', opacity:'BB', ids:['industry'] },
        { id:'manage',   name:'经营领域', icon:'el-icon-data-analysis', color:'#1890FF', opacity:'55', ids:['invest','equity','procurement','military','overseas'] },
        { id:'carrier',  name:'监管载体',     icon:'el-icon-document',      color:'#EB2F96', opacity:'BB', ids:['contract','fund'] },
        { id:'mgmt',     name:'管理领域', icon:'el-icon-setting',       color:'#722ED1', opacity:'55', ids:['accounting','financial','salary','property'] },
        { id:'data',     name:'内外数据融合', icon:'el-icon-connection',    color:'#FA8C16', opacity:'BB', ids:['dataFusion'] },
        { id:'warn',     name:'风险处置', icon:'el-icon-warning',       color:'#FF4D4F', opacity:'55', ids:['warning'] },
      ]
      const cardMap = {}
      this.domainCards.forEach(c => { cardMap[c.id] = c })
      return groupDefs.map(g => ({
        ...g,
        cards: g.ids.map(id => cardMap[id]).filter(Boolean),
      }))
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
    this.fetchHomeData()
  },
  methods: {
    /** 从后端获取首页数据 */
    async fetchHomeData() {
      try {
        const res = await getSmartHomeOverview()
        const data = res.data || res
        // KPI卡片
        if (data.kpiCards && data.kpiCards.length) {
          this.kpiCards = data.kpiCards.map(item => ({
            label: item.label,
            value: item.value,
            unit: item.unit,
            icon: item.icon,
            iconBg: item.iconBg,
            iconColor: item.iconColor,
            barBg: item.barBg,
            barColor: item.barColor,
            barWidth: item.barWidth,
            trend: item.trend,
            theme: item.theme || '',
          }))
        }
        // 风险预警
        if (data.riskList && data.riskList.length) {
          this.riskList = data.riskList.map(item => ({
            id: item.id,
            title: item.title,
            domain: item.domain,
            desc: item.description || item.desc,
            level: item.level,
            levelLabel: item.levelLabel,
            time: item.timeStr || item.time,
            route: item.route,
          }))
        }
        // 待处理
        if (data.pendingList && data.pendingList.length) {
          this.pendingList = data.pendingList.map(item => ({
            id: item.id,
            title: item.title,
            domain: item.domain,
            desc: item.description || item.desc,
            urgent: item.urgent === 1 || item.urgent === true,
            statusLabel: item.statusLabel,
            route: item.route,
          }))
        }
        // 异常检测
        if (data.abnormalList && data.abnormalList.length) {
          this.abnormalList = data.abnormalList.map(item => ({
            id: item.id,
            title: item.title,
            domain: item.domain,
            desc: item.description || item.desc,
            typeLabel: item.typeLabel,
            route: item.route,
          }))
        }
        // 领域分布统计
        if (data.domainStats && data.domainStats.length) {
          this.domainStats = data.domainStats.map(item => ({
            type: item.typeName || item.type,
            count: item.countNum || item.count,
            ratio: item.ratio,
            color: item.color,
          }))
        }
        // 监管快报
        if (data.reportList && data.reportList.length) {
          this.reportList = data.reportList.map(item => ({
            id: item.id,
            tag: item.tag,
            title: item.title,
            time: item.timeStr || item.time,
            author: item.author,
            tagBg: item.tagBg,
            tagColor: item.tagColor,
            route: item.route,
          }))
        }
        // 重点监管进度
        if (data.keyProgresses && data.keyProgresses.length) {
          this.keyProgresses = data.keyProgresses.map(item => ({
            id: item.id,
            name: item.name,
            rate: item.rate,
            abnormal: item.abnormalCount || item.abnormal,
            scope: item.scope,
            coverage: item.coverage,
            riskCount: item.riskCount,
            route: item.route,
          }))
        }
        // 欢迎区统计（从KPI和列表数据动态计算，始终保持6项显示）
        const kpi = (data.kpiCards && data.kpiCards.length) ? data.kpiCards : []
        this.welcomeStats = [
          { label: '监管企业总数', value: (kpi[0] ? kpi[0].value + (kpi[0].unit || '') : '--'), color: '#1890FF' },
          { label: '覆盖穿透领域', value: '12个', color: '#52C41A' },
          { label: '风险预警', value: ((data.riskList || []).length || 0) + '条', color: '#FF4D4F' },
          { label: '待处理事项', value: ((data.pendingList || []).length || 0) + '个', color: '#FA8C16' },
          { label: '本期报告', value: ((data.reportList || []).length || 0) + '份', color: '#722ED1' },
          { label: '异常检测', value: ((data.abnormalList || []).length || 0) + '项', color: '#EB2F96' },
        ]
        this._patchPrimaryColors()
      } catch (e) {
        console.warn('[SmartHome] 获取首页数据失败', e)
      }
    },
    jumpTo(route) {
      if (route) this.$router.push(route)
    },
    // ===== 各领域全景卡片内部拓扑图方法 =====
    // 计算卡片画布宽度
    calcCardW(card) {
      const { colW, padX } = this.cardCfg
      if (!card.nodes.length) return 300
      const maxCol = Math.max(...card.nodes.map(n => n.col))
      return Math.ceil((maxCol + 1) * colW + padX * 2)
    },
    // 计算卡片画布高度
    calcCardH(card) {
      const { rowH, padY, nodeH } = this.cardCfg
      if (!card.nodes.length) return 100
      const maxRow = Math.max(...card.nodes.map(n => n.row))
      return Math.ceil((maxRow + 1) * rowH + padY * 2 + nodeH)
    },
    // 卡片内节点定位
    calcNodePos(card, n) {
      const { colW, rowH, nodeW, nodeH, padX, padY } = this.cardCfg
      return {
        left: (padX + n.col * colW + (colW - nodeW) / 2) + 'px',
        top:  (padY + n.row * rowH) + 'px',
        width: nodeW + 'px',
        height: nodeH + 'px',
      }
    },
    // 卡片内连线路径
    calcEdges(card) {
      const { colW, rowH, nodeW, nodeH, padX, padY } = this.cardCfg
      const nodeMap = {}
      card.nodes.forEach(n => { nodeMap[n.id] = n })
      const id = card.id
      const markerMap = {
        main:    `url(#ca-arr-main-${id})`,
        sub:     `url(#ca-arr-sub-${id})`,
        warn:    `url(#ca-arr-warn-${id})`,
        finance: `url(#ca-arr-finance-${id})`,
      }
      return card.edges.map(([from, to, kind]) => {
        const a = nodeMap[from], b = nodeMap[to]
        if (!a || !b) return { d: '', kind: '', marker: '' }
        const ax = padX + (a.col + 0.5) * colW
        const ay = padY + a.row * rowH + nodeH / 2
        const bx = padX + (b.col + 0.5) * colW
        const by = padY + b.row * rowH + nodeH / 2
        const halfW = nodeW / 2 + 2
        const halfH = nodeH / 2 + 2
        let d
        if (a.row === b.row) {
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

/* ========== 各层独立区块拓扑图 ========== */
/* 层区块：每个大层级一个独立卡片 */
.layer-section {
  background: #fff;
  border-radius: 10px;
  margin-bottom: 16px;
  box-shadow: 0 1px 6px rgba(0,0,0,.06);
  overflow: hidden;
  border-left-width: 5px;
}
/* 层区块标题栏 */
.layer-section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 18px 10px;
  border-bottom: 2px solid rgba(0,0,0,.15);
}
.layer-section-title {
  display: flex;
  align-items: center;
  gap: 10px;
  .layer-icon-wrap {
    width: 30px; height: 30px;
    border-radius: 8px;
    display: flex; align-items: center; justify-content: center;
    flex-shrink: 0;
    i { font-size: 15px; }
  }
  .layer-name {
    font-size: 15px;
    font-weight: 700;
    letter-spacing: 1px;
  }
  .layer-card-count {
    font-size: 11px;
    color: #999;
    background: #f5f5f5;
    padding: 2px 8px;
    border-radius: 10px;
  }
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
/* 层内各领域卡片列表 */
.layer-cards {
  padding: 12px 16px 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}
/* 单张领域卡片（独占一行） */
.domain-card {
  width: 100%;
  background: #fafbff;
  border-radius: 8px;
  border: 1px solid #eef0f5;
  overflow: hidden;
  transition: box-shadow 0.2s;
  &:hover {
    box-shadow: 0 3px 12px rgba(0,0,0,.08);
  }
}
/* 卡片标题栏 */
.card-header-bar {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 14px;
  background: #fff;
  border-bottom: 1px solid #f0f2f5;
  .card-color-dot {
    width: 10px; height: 10px;
    border-radius: 50%;
    flex-shrink: 0;
  }
  i { font-size: 14px; }
  .card-title-text {
    font-size: 13px;
    font-weight: 700;
    color: #222;
    flex: 1;
  }
  .card-count {
    font-size: 11px;
    color: #999;
    background: #f5f5f5;
    padding: 2px 7px;
    border-radius: 10px;
  }
}
/* 卡片画布区域 */
.card-canvas-wrap {
  overflow-x: auto;
  overflow-y: hidden;
  padding: 10px 10px 12px;
  background:
    linear-gradient(90deg, rgba(24,144,255,0.025) 1px, transparent 1px) 0 0 / 16px 16px,
    linear-gradient(0deg, rgba(24,144,255,0.025) 1px, transparent 1px) 0 0 / 16px 16px,
    #fafbff;
}
.card-canvas {
  position: relative;
  margin: 0 auto;
}
.card-svg {
  position: absolute;
  top: 0; left: 0;
  pointer-events: none;
  z-index: 1;
}
/* 连线样式 */
.card-edge {
  fill: none;
  &.edge-main    { stroke: var(--ip-primary, #1890FF); stroke-width: 1.8; }
  &.edge-sub     { stroke: #bbb; stroke-width: 1.3; stroke-dasharray: 4 3; }
  &.edge-warn    { stroke: #FF4D4F; stroke-width: 1.6; }
  &.edge-finance { stroke: #13C2C2; stroke-dasharray: 5 3; stroke-width: 1.4; }
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
