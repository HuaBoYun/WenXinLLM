<template>
  <div class="smart-contract-home" :style="themeVars">
    <!-- ========== 顶部欢迎区 ========== -->
    <div class="welcome-section">
      <div class="welcome-bg"></div>
      <div class="welcome-content">
        <div class="welcome-left">
          <div class="greeting">
            <span class="greeting-icon"><i class="el-icon-sunny" /></span>
            <span class="greeting-text">{{ greetingWord }}，{{ userName }}</span>
          </div>
          <h1 class="welcome-title">智慧合同监管平台</h1>
          <p class="welcome-desc">全生命周期合同管理 · AI智能审查 · 风险实时预警 · 财务法务闭环</p>
          <div class="welcome-actions">
            <el-button type="primary" icon="el-icon-plus" round @click="$router.push('/contractManage/create')">新建合同</el-button>
            <el-button icon="el-icon-cpu" round @click="$router.push('/Ai/home')">AI 合同助手</el-button>
            <el-button icon="el-icon-search" round @click="$router.push('/Account/index')">合同查询</el-button>
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

    <!-- ========== 合同业务全链路流程图（拓扑图） ========== -->
    <div class="lifecycle-section">
      <div class="section-header">
        <div class="section-title"><i class="el-icon-share" /> 合同业务全链路流程</div>
        <div class="section-legend">
          <span><i class="dot blue" />主流程</span>
          <span><i class="dot orange" />关注</span>
          <span><i class="dot red" />法务/预警</span>
          <span><i class="dot cyan" />财务并行</span>
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
              <marker id="fl-arrow-legal" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="7" markerHeight="7" orient="auto">
                <path d="M0,0 L10,5 L0,10 z" fill="#FF4D4F" />
              </marker>
              <marker id="fl-arrow-finance" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="7" markerHeight="7" orient="auto">
                <path d="M0,0 L10,5 L0,10 z" fill="#13C2C2" />
              </marker>
              <marker id="fl-arrow-warn" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="7" markerHeight="7" orient="auto">
                <path d="M0,0 L10,5 L0,10 z" fill="#FA8C16" />
              </marker>
            </defs>
            <path v-for="(e, idx) in edgePaths" :key="'e'+idx" :d="e.d" :class="['fl-edge', e.cls]" :marker-end="e.marker" fill="none" />
          </svg>
          <div v-for="n in flowNodes" :key="n.id"
               class="flow-pos-node"
               :class="['fpn-'+n.kind, n.alert ? 'fpn-alert' : '']"
               :style="nodePos(n)"
               @click="jumpTo(n.route)">
            <div class="fpn-icon-wrap"><i :class="n.icon" /></div>
            <div class="fpn-label">{{ n.name }}</div>
            <div class="fpn-dot" v-if="n.alert"></div>
            <div class="fpn-badge" v-if="n.badge">{{ n.badge }}</div>
          </div>
        </div>
      </div>
    </div>
    <!-- ========== 中部：待办预警 + 合同类型 + 相对方 ========== -->
    <el-row :gutter="14" class="content-row">
      <!-- 待办 & 预警 -->
      <el-col :span="10">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-bell" /> 待办与预警中心</span>
            <span class="card-more" @click="$router.push('/home/infoCenter')">更多 →</span>
          </div>
          <el-tabs v-model="todoActive" class="inner-tabs">
            <el-tab-pane name="pending">
              <span slot="label">待审批 <el-badge :value="pendingList.length" class="tab-badge" /></span>
              <div class="todo-list">
                <div v-for="item in pendingList" :key="item.id" class="todo-item" @click="jumpTo('/contractManage/create')">
                  <div class="todo-left">
                    <div class="todo-dot" :class="item.status === 'urgent' ? 'dot-red' : 'dot-orange'"></div>
                    <div class="todo-info">
                      <div class="todo-name">{{ item.contractName }}</div>
                      <div class="todo-meta">{{ item.contractType }} · {{ item.initiator }} · {{ item.amount }}万</div>
                    </div>
                  </div>
                  <el-tag :type="item.status === 'urgent' ? 'danger' : 'warning'" size="mini" effect="dark">{{ item.statusLabel }}</el-tag>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane name="risk">
              <span slot="label">风险预警 <el-badge :value="riskList.length" type="danger" class="tab-badge" /></span>
              <div class="todo-list">
                <div v-for="item in riskList" :key="item.id" class="todo-item" @click="jumpTo('/opposite/warning')">
                  <div class="todo-left">
                    <div class="todo-dot" :class="item.level === 'HIGH' ? 'dot-red' : 'dot-orange'"></div>
                    <div class="todo-info">
                      <div class="todo-name">{{ item.contractName }}</div>
                      <div class="todo-meta">{{ item.riskType }} · {{ item.counterpart }}</div>
                    </div>
                  </div>
                  <div class="todo-right-col">
                    <el-tag :type="item.level === 'HIGH' ? 'danger' : item.level === 'MEDIUM' ? 'warning' : 'info'" size="mini">{{ item.levelLabel }}</el-tag>
                    <span class="todo-time">{{ item.triggerTime }}</span>
                  </div>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane name="expiring">
              <span slot="label">即将到期 <el-badge :value="expiringList.length" class="tab-badge" /></span>
              <div class="todo-list">
                <div v-for="item in expiringList" :key="item.id" class="todo-item" @click="jumpTo('/execute/track')">
                  <div class="todo-left">
                    <div class="todo-dot" :class="item.remainDays <= 7 ? 'dot-red' : 'dot-orange'"></div>
                    <div class="todo-info">
                      <div class="todo-name">{{ item.contractName }}</div>
                      <div class="todo-meta">{{ item.counterpart }} · 到期日 {{ item.expireDate }}</div>
                    </div>
                  </div>
                  <el-tag :type="item.remainDays <= 7 ? 'danger' : 'warning'" size="mini" effect="dark">剩余{{ item.remainDays }}天</el-tag>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-col>

      <!-- 合同类型分布 -->
      <el-col :span="7">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-pie-chart" /> 合同类型分布</span>
            <span class="card-more" @click="jumpTo('/Account/index')">详情 →</span>
          </div>
          <div class="type-chart-area">
            <div v-for="t in contractTypeStats" :key="t.type" class="type-row">
              <div class="type-head">
                <span class="type-dot" :style="{background: t.color}"></span>
                <span class="type-name">{{ t.type }}</span>
                <span class="type-count">{{ t.count }}份</span>
              </div>
              <div class="type-bar-bg">
                <div class="type-bar-fill" :style="{width: t.ratio + '%', background: t.color}"></div>
              </div>
            </div>
          </div>
          <div class="type-summary">
            <span>本年共签订 <b>{{ totalContracts }}</b> 份合同</span>
          </div>
        </div>
      </el-col>

      <!-- 相对方风险 -->
      <el-col :span="7">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-user" /> 相对方风险监控</span>
            <span class="card-more" @click="jumpTo('/opposite/warning')">详情 →</span>
          </div>
          <div class="cp-list">
            <div v-for="c in counterpartRiskStats" :key="c.name" class="cp-card">
              <div class="cp-head">
                <span class="cp-name">{{ c.name }}</span>
                <el-tag :type="c.riskLevel === 'HIGH' ? 'danger' : c.riskLevel === 'MEDIUM' ? 'warning' : 'success'" size="mini" effect="dark">{{ c.riskLabel }}</el-tag>
              </div>
              <div class="cp-metrics">
                <div class="cp-metric">
                  <span class="cp-metric-val">{{ c.contractCount }}</span>
                  <span class="cp-metric-lbl">合同数</span>
                </div>
                <div class="cp-metric">
                  <span class="cp-metric-val" :style="{color: c.overdueCount > 0 ? '#FF4D4F' : '#52C41A'}">{{ c.overdueCount }}</span>
                  <span class="cp-metric-lbl">逾期</span>
                </div>
                <div class="cp-metric">
                  <span class="cp-metric-val" :style="{color: c.blacklist ? '#FF4D4F' : '#52C41A'}">{{ c.blacklist ? '是' : '否' }}</span>
                  <span class="cp-metric-lbl">黑名单</span>
                </div>
                <div class="cp-metric">
                  <span class="cp-metric-val" :style="{color: c.healthScore >= 80 ? '#52C41A' : c.healthScore >= 60 ? '#FA8C16' : '#FF4D4F'}">{{ c.healthScore }}</span>
                  <span class="cp-metric-lbl">健康分</span>
                </div>
              </div>
              <div class="cp-health-bar">
                <div class="cp-health-fill" :style="{width: c.healthScore + '%', background: c.healthScore >= 80 ? '#52C41A' : c.healthScore >= 60 ? '#FA8C16' : '#FF4D4F'}"></div>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- ========== 重点合同履行 + 功能模块 ========== -->
    <el-row :gutter="14" class="content-row">
      <el-col :span="12">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-data-line" /> 重点合同履行进度</span>
            <span class="card-more" @click="jumpTo('/execute/track')">全部 →</span>
          </div>
          <div class="contract-progress-list">
            <div v-for="p in keyContracts" :key="p.contractId" class="cpl-row" @click="jumpTo('/execute/mine')">
              <div class="cpl-row-top">
                <span class="cpl-row-name">{{ p.contractName }}</span>
                <div class="cpl-row-tags">
                  <el-tag v-if="p.delay > 0" type="danger" size="mini" effect="dark">逾期{{ p.delay }}天</el-tag>
                  <el-tag :type="p.progressRate >= 80 ? 'success' : p.progressRate >= 50 ? 'warning' : 'danger'" size="mini">履行{{ p.progressRate }}%</el-tag>
                </div>
              </div>
              <div class="cpl-row-bar">
                <div class="cpl-bar-bg">
                  <div class="cpl-bar-fill" :style="{width: p.progressRate + '%', background: p.progressRate >= 80 ? '#52C41A' : p.progressRate >= 50 ? '#FA8C16' : '#FF4D4F'}"></div>
                </div>
              </div>
              <div class="cpl-row-info">
                <span><i class="el-icon-user" /> {{ p.counterpart }}</span>
                <span><i class="el-icon-coin" /> {{ p.amount }}万</span>
                <span><i class="el-icon-money" /> 回款{{ p.paymentRate }}%</span>
              </div>
            </div>
          </div>
        </div>
      </el-col>

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
    </el-row>

    <!-- ========== AI 智能体 + 合同文库 ========== -->
    <el-row :gutter="14" class="content-row">
      <el-col :span="12">
        <div class="card-panel ai-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-cpu" /> AI 合同智能体</span>
            <span class="card-more" @click="jumpTo('/Ai/home')">进入AI →</span>
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
            <span class="card-title"><i class="el-icon-reading" /> 合同文库与配置</span>
          </div>
          <div class="lib-grid">
            <div v-for="lib in libraryItems" :key="lib.name" class="lib-card" @click="jumpTo(lib.route)">
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
import {
  getSmartHomeData,
  getPendingApprovalList,
  getRiskWarningList,
  getExpiringContractList,
  getCounterpartRiskList,
  getKeyContractProgress,
} from '@/api/contract/smartHome'

export default {
  name: 'SmartContractHome',
  mixins: [investThemeMixin],
  data() {
    return {
      userName: '',
      todoActive: 'pending',
      loading: false,
      // ===== 流程图拓扑数据 =====
      flowConfig: { colW: 132, rowH: 96, nodeW: 96, nodeH: 60, padX: 20, padY: 16 },
      flowNodes: [
        // 主干（row 3）
        { id: 'opposite', name: '相对方维护', col: 0, row: 3, icon: 'el-icon-user', kind: 'main', route: '/opposite/maintain' },
        { id: 'project',  name: '项目获取',   col: 1, row: 3, icon: 'el-icon-folder-add', kind: 'main', route: '/project/index' },
        { id: 'bidding',  name: '投标管理',   col: 2, row: 3, icon: 'el-icon-trophy', kind: 'main', route: '/toubiao/Biddocuments' },
        { id: 'prepare',  name: '项目准备',   col: 3, row: 3, icon: 'el-icon-set-up', kind: 'main', route: '/ProjectRidy/Projectch' },
        { id: 'create',   name: '合同订立',   col: 4, row: 3, icon: 'el-icon-edit-outline', kind: 'main', route: '/contractManage/create', badge: '' },
        { id: 'seal',     name: '合同用印',   col: 5, row: 3, icon: 'el-icon-stamp', kind: 'warn', route: '/contractManage/seal' },
        { id: 'execute',  name: '合同履行',   col: 6, row: 3, icon: 'el-icon-s-order', kind: 'main', route: '/execute/mine' },
        { id: 'archive',  name: '合同归档',   col: 7, row: 3, icon: 'el-icon-folder', kind: 'main', route: '/contractManage/package' },
        { id: 'account',  name: '合同台账',   col: 8, row: 3, icon: 'el-icon-tickets', kind: 'main', route: '/Account/index' },

        // 上方第一层分支（row 2）
        { id: 'category', name: '合同类型',   col: 3.5, row: 2, icon: 'el-icon-collection-tag', kind: 'sub', route: '/contractManage/category' },
        { id: 'template', name: '合同范本',   col: 4.5, row: 2, icon: 'el-icon-document-copy', kind: 'sub', route: '/contractManage/template' },
        { id: 'change',   name: '合同变更',   col: 5.5, row: 2, icon: 'el-icon-refresh-right', kind: 'sub', route: '/contractManage/change' },
        { id: 'transfer', name: '合同移交',   col: 6.5, row: 2, icon: 'el-icon-sort', kind: 'sub', route: '/contractManage/transfercontract' },
        { id: 'stat',     name: '统计分析',   col: 7.5, row: 2, icon: 'el-icon-data-line', kind: 'sub', route: '/Account/delivery' },
        { id: 'hthz',     name: '合同汇总',   col: 8.5, row: 2, icon: 'el-icon-pie-chart', kind: 'sub', route: '/Account/hthz' },

        // 上方第二层（row 1）
        { id: 'mgmt', name: '项目经营', col: 2.5, row: 1, icon: 'el-icon-office-building', kind: 'sub', route: '/Projectjy/Settlement' },
        { id: 'debt', name: '债权管理', col: 3.5, row: 1, icon: 'el-icon-document-checked', kind: 'sub', route: '/Rights/Ledger' },

        // 财务并行（row 4）
        { id: 'recv', name: '收款管理', col: 4.5, row: 4, icon: 'el-icon-coin', kind: 'finance', route: '/financing/receiving' },
        { id: 'pay',  name: '付款管理', col: 5.5, row: 4, icon: 'el-icon-money', kind: 'finance', route: '/financing/payment' },
        { id: 'bill', name: '发票管理', col: 6.5, row: 4, icon: 'el-icon-document', kind: 'finance', route: '/financing/bill' },
        { id: 'bank', name: '银行账户', col: 7.5, row: 4, icon: 'el-icon-bank-card', kind: 'finance', route: '/financing/bank' },

        // 法务主干（row 5）
        { id: 'dispute', name: '纠纷登记', col: 2, row: 5, icon: 'el-icon-warning', kind: 'legal', route: '/legal/dispute', alert: true },
        { id: 'consult', name: '协商过程', col: 3, row: 5, icon: 'el-icon-chat-dot-round', kind: 'legal', route: '/legal/consult' },
        { id: 'close',   name: '纠纷结案', col: 5, row: 5, icon: 'el-icon-finished', kind: 'legal', route: '/legal/close' },
        { id: 'freeze',  name: '账户冻结', col: 6, row: 5, icon: 'el-icon-lock', kind: 'legal', route: '/legal/freeze' },
        { id: 'lacc',    name: '法务台账', col: 7, row: 5, icon: 'el-icon-notebook-2', kind: 'legal', route: '/legal/account' },

        // 法务分支（row 6）
        { id: 'lawsuit', name: '诉讼过程', col: 3.5, row: 6, icon: 'el-icon-s-flag', kind: 'legal', route: '/legal/lawsuit' },
        { id: 'arbit',   name: '仲裁过程', col: 4.5, row: 6, icon: 'el-icon-medal', kind: 'legal', route: '/legal/arbitration' },

        // 配置/文库（row 7）
        { id: 'htzd', name: '合同制度',   col: 0, row: 7, icon: 'el-icon-notebook-2', kind: 'config', route: '/system/htzd' },
        { id: 'ysk',  name: '合同要素库', col: 1, row: 7, icon: 'el-icon-collection-tag', kind: 'config', route: '/htfbk/htysk' },
        { id: 'fxqd', name: '风险清单',   col: 2, row: 7, icon: 'el-icon-warning-outline', kind: 'config', route: '/htfbk/htfxqd' },
        { id: 'fbk',  name: '合同范本库', col: 3, row: 7, icon: 'el-icon-files', kind: 'config', route: '/htfbk/home' },
        { id: 'mbk',  name: '合同模板库', col: 4, row: 7, icon: 'el-icon-document-copy', kind: 'config', route: '/htfbk/xgmbk' },
        { id: 'flfg', name: '法律法规',   col: 5, row: 7, icon: 'el-icon-reading', kind: 'config', route: '/htfbk/flfg' },
        { id: 'flal', name: '法律案例',   col: 6, row: 7, icon: 'el-icon-notebook-1', kind: 'config', route: '/htfbk/flal' },
        { id: 'flsw', name: '法律实务',   col: 7, row: 7, icon: 'el-icon-s-operation', kind: 'config', route: '/htfbk/flsw' },
      ],
      flowEdges: [
        // 主干（实线主流程）
        ['opposite','project','main'], ['project','bidding','main'], ['bidding','prepare','main'],
        ['prepare','create','main'], ['create','seal','main'], ['seal','execute','main'],
        ['execute','archive','main'], ['archive','account','main'],
        // 顶部分支（虚线）
        ['category','create','sub'], ['template','create','sub'],
        ['change','execute','sub'], ['transfer','execute','sub'],
        ['stat','account','sub'], ['hthz','account','sub'],
        ['mgmt','prepare','sub'], ['debt','prepare','sub'],
        // 财务（合同履行 → 财务，并横向链）
        ['execute','recv','finance'],
        ['recv','pay','finance'], ['pay','bill','finance'], ['bill','bank','finance'],
        // 法务（合同履行 → 纠纷登记，主链）
        ['execute','dispute','legal'],
        ['dispute','consult','legal'],
        ['consult','close','legal'],            // 协商一致
        ['consult','lawsuit','legal-dash'],     // 协商不一致 → 诉讼
        ['consult','arbit','legal-dash'],       // 协商不一致 → 仲裁
        ['lawsuit','close','legal-dash'],
        ['arbit','close','legal-dash'],
        ['close','freeze','legal'], ['freeze','lacc','legal'],
      ],
      welcomeStats: [
        { label: '本年签订合同', value: '-', color: '#1890FF' },
        { label: '合同总额', value: '-', color: '#52C41A' },
        { label: '待审批', value: '-', color: '#FA8C16' },
        { label: '风险预警', value: '-', color: '#FF4D4F' },
        { label: '逾期合同', value: '-', color: '#FF4D4F' },
      ],
      kpiCards: [
        { label: '合同总额', value: '-', unit: '万元', icon: 'el-icon-money', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', barBg: 'rgba(24,144,255,0.1)', barColor: '#1890FF', barWidth: 0, trend: 0, theme: '' },
        { label: '合同数量', value: '-', unit: '份', icon: 'el-icon-document', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', barBg: 'rgba(82,196,26,0.1)', barColor: '#52C41A', barWidth: 0, trend: 0, theme: '' },
        { label: '待审批合同', value: '-', unit: '个', icon: 'el-icon-s-claim', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', barBg: 'rgba(250,140,22,0.1)', barColor: '#FA8C16', barWidth: 0, trend: 0, theme: 'kpi-warning' },
        { label: '履行中合同', value: '-', unit: '份', icon: 'el-icon-s-order', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', barBg: 'rgba(114,46,209,0.1)', barColor: '#722ED1', barWidth: 0, trend: 0, theme: '' },
        { label: '逾期合同', value: '-', unit: '份', icon: 'el-icon-warning', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', barBg: 'rgba(255,77,79,0.1)', barColor: '#FF4D4F', barWidth: 0, trend: 0, theme: 'kpi-danger' },
        { label: '回款率', value: '-', unit: '%', icon: 'el-icon-coin', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', barBg: 'rgba(19,194,194,0.1)', barColor: '#13C2C2', barWidth: 0, trend: 0, theme: 'kpi-warning' },
      ],
      pendingList: [],
      riskList: [],
      expiringList: [],
      contractTypeStats: [],
      counterpartRiskStats: [],
      keyContracts: [],
      modules: [
        { name: '管控分析', icon: 'el-icon-data-analysis', desc: '高管/回款/财务/交付', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', route: '/system/manage' },
        { name: '相对方', icon: 'el-icon-user', desc: '维护/预警/黑名单/监控', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', route: '/opposite/maintain' },
        { name: '项目获取', icon: 'el-icon-folder-add', desc: '项目登记与跟踪', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', route: '/project/index' },
        { name: '合同订立', icon: 'el-icon-edit-outline', desc: '类型/范本/用印/变更', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', route: '/contractManage/create' },
        { name: '合同履行', icon: 'el-icon-s-order', desc: '我的/跟踪/落实', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', route: '/execute/mine' },
        { name: '合同台账', icon: 'el-icon-tickets', desc: '台账/汇总/移交', iconBg: 'rgba(235,47,150,0.1)', iconColor: '#EB2F96', route: '/Account/index' },
        { name: '财务管理', icon: 'el-icon-wallet', desc: '收付款/发票/银行', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', route: '/financing/receiving' },
        { name: '法务管理', icon: 'el-icon-s-custom', desc: '纠纷/诉讼/仲裁/保全', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', route: '/legal/dispute' },
        { name: '项目经营', icon: 'el-icon-office-building', desc: '经营/归档/考核/变更', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', route: '/Projectjy/Settlement' },
        { name: '债权管理', icon: 'el-icon-document-checked', desc: '债权/标书/交底', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', route: '/Rights/Ledger' },
        { name: '合同配置', icon: 'el-icon-setting', desc: '制度/统计分析', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', route: '/system/htzd' },
        { name: '合同移交', icon: 'el-icon-sort', desc: '合同移交管理', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', route: '/contractManage/transfercontract' },
      ],
      aiFeatures: [
        { name: 'AI 合同生成', icon: 'el-icon-magic-stick', desc: '智能生成合同文本，支持多类型', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/Ai/home' },
        { name: 'AI 风险评估', icon: 'el-icon-warning-outline', desc: '自动识别合同风险条款与漏洞', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/project/ProjectRisk' },
        { name: '合同智能体', icon: 'el-icon-cpu', desc: 'AI对话式合同审查与合规分析', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/Ai/AIAgent' },
        { name: '合同总览', icon: 'el-icon-data-board', desc: '财务状态综合分析与可视化', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/home/cwzl' },
      ],
      libraryItems: [
        { name: '合同范本库', icon: 'el-icon-files', desc: '标准化合同模板下载', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/htfbk/home' },
        { name: '合同模板库', icon: 'el-icon-document-copy', desc: '可编辑合同模板', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/htfbk/xgmbk' },
        { name: '合同要素库', icon: 'el-icon-collection-tag', desc: '合同要素标准化管理', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/htfbk/htysk' },
        { name: '合同风险清单', icon: 'el-icon-warning', desc: '常见合同风险点清单', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/htfbk/htfxqd' },
        { name: '法律法规', icon: 'el-icon-reading', desc: '合同相关法律法规库', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/htfbk/flfg' },
        { name: '法律案例', icon: 'el-icon-notebook-1', desc: '合同纠纷判例参考', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/htfbk/flal' },
        { name: '法律实务', icon: 'el-icon-s-operation', desc: '法律实务操作指南', bgColor: 'rgba(235,47,150,0.08)', iconColor: '#EB2F96', route: '/htfbk/flsw' },
        { name: '合同制度', icon: 'el-icon-notebook-2', desc: '合同管理制度文件', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/system/htzd' },
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
    totalContracts() {
      return this.contractTypeStats.reduce((sum, t) => sum + t.count, 0)
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
        sub: 'url(#fl-arrow)',
        finance: 'url(#fl-arrow-finance)',
        legal: 'url(#fl-arrow-legal)',
        'legal-dash': 'url(#fl-arrow-legal)',
        warn: 'url(#fl-arrow-warn)',
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
          // 折线：先垂直离开起点，再水平至目标列，最终垂直进入终点（或反之）
          const dirY = bc.y > ac.y ? 1 : -1
          const dirX = bc.x > ac.x ? 1 : -1
          sx = ac.x; sy = ac.y + dirY * (nodeH / 2 + 2)
          tx = bc.x - dirX * (nodeW / 2 + 2); ty = bc.y
          // 垂直 → 水平
          d = `M${sx},${sy} L${sx},${ty} L${tx},${ty}`
        }
        return { d, cls: 'edge-' + kind, marker: markerMap[kind] || markerMap.sub }
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
    this.fetchAllData()
  },
  methods: {
    jumpTo(route) {
      if (route) this.$router.push(route)
    },
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
          this.fetchSmartHomeData(),
          this.fetchPendingList(),
          this.fetchRiskWarning(),
          this.fetchExpiringList(),
          this.fetchCounterpartRisk(),
          this.fetchKeyContracts(),
        ])
      } catch (e) {
        console.error('首页数据加载异常', e)
      } finally {
        this.loading = false
      }
    },
    /** 获取KPI和统计数据 */
    async fetchSmartHomeData() {
      try {
        const year = new Date().getFullYear()
        const res = await getSmartHomeData({ year })
        if (res && res.codes === '1') {
          const kpi = res.kpi || {}
          const welcome = res.welcome || {}
          // 更新欢迎区统计
          this.$set(this.welcomeStats, 0, { ...this.welcomeStats[0], value: (welcome.contractCount || 0) + '份' })
          this.$set(this.welcomeStats, 1, { ...this.welcomeStats[1], value: this.formatAmount(kpi.totalAmount) })
          this.$set(this.welcomeStats, 2, { ...this.welcomeStats[2], value: (kpi.pendingCount || 0) + '个' })
          this.$set(this.welcomeStats, 3, { ...this.welcomeStats[3], value: (this.riskList.length || 0) + '条' })
          this.$set(this.welcomeStats, 4, { ...this.welcomeStats[4], value: (kpi.breachCount || 0) + '份' })
          // 更新KPI卡片
          const totalAmountWan = kpi.totalAmount ? Math.round(kpi.totalAmount / 10000) : 0
          this.$set(this.kpiCards, 0, { ...this.kpiCards[0], value: this.formatNumber(totalAmountWan), barWidth: Math.min(totalAmountWan / 200, 100) })
          this.$set(this.kpiCards, 1, { ...this.kpiCards[1], value: String(kpi.contractCount || 0), barWidth: Math.min((kpi.contractCount || 0) / 5, 100) })
          this.$set(this.kpiCards, 2, { ...this.kpiCards[2], value: String(kpi.pendingCount || 0), barWidth: Math.min((kpi.pendingCount || 0) * 5, 100) })
          this.$set(this.kpiCards, 3, { ...this.kpiCards[3], value: String(kpi.executingCount || 0), barWidth: Math.min((kpi.executingCount || 0) / 3, 100) })
          this.$set(this.kpiCards, 4, { ...this.kpiCards[4], value: String(kpi.breachCount || 0), barWidth: Math.min((kpi.breachCount || 0) * 10, 100) })
          this.$set(this.kpiCards, 5, { ...this.kpiCards[5], value: String(kpi.paymentRate || 0), barWidth: kpi.paymentRate || 0 })
          // 更新流程图中"合同订立"节点的badge
          const createNode = this.flowNodes.find(n => n.id === 'create')
          if (createNode) {
            createNode.badge = kpi.pendingCount > 0 ? String(kpi.pendingCount) : ''
          }
          // 更新合同类型分布
          const typeColors = ['#1890FF', '#52C41A', '#FA8C16', '#722ED1', '#13C2C2', '#EB2F96']
          const typeList = res.contractTypeStats || []
          const totalCount = typeList.reduce((sum, t) => sum + (Number(t.value || t.VALUE) || 0), 0)
          this.contractTypeStats = typeList.map((t, i) => ({
            type: t.name || t.NAME || '未知',
            count: Number(t.value || t.VALUE) || 0,
            ratio: totalCount > 0 ? Math.round((Number(t.value || t.VALUE) || 0) * 1000 / totalCount) / 10 : 0,
            color: typeColors[i % typeColors.length],
          }))
        }
      } catch (e) {
        console.error('获取首页统计数据失败', e)
      }
    },
    /** 获取待审批列表 */
    async fetchPendingList() {
      try {
        const res = await getPendingApprovalList({ pageNumber: 1, pageSize: 5, category: 'contract' })
        if (res && res.data && res.data.list) {
          this.pendingList = (res.data.list || []).slice(0, 5).map((item, idx) => ({
            id: item.id || idx,
            contractName: item.fullName || item.title || '未命名合同',
            contractType: item.flowCategory || '合同',
            amount: item.amount || '-',
            initiator: item.creatorUser || item.userName || '-',
            status: item.flowUrgent === 1 ? 'urgent' : 'normal',
            statusLabel: item.flowUrgent === 1 ? '紧急' : '待审',
          }))
        }
      } catch (e) {
        console.error('获取待审批列表失败', e)
      }
    },
    /** 获取风险预警列表 */
    async fetchRiskWarning() {
      try {
        const res = await getRiskWarningList({ pageNumber: 1, pageSize: 5 })
        if (res && res.codes === '1' && res.riskList) {
          this.riskList = (res.riskList || []).map((item, idx) => ({
            id: item.CONTRACTID || idx,
            riskType: item.RISK_TYPE || '风险预警',
            contractName: item.CONTRACTNAME || '未命名合同',
            counterpart: item.COUNTERPART || '-',
            level: item.RISK_LEVEL || 'LOW',
            levelLabel: item.RISK_LEVEL === 'HIGH' ? '高' : item.RISK_LEVEL === 'MEDIUM' ? '中' : '低',
            triggerTime: item.TRIGGER_TIME || '-',
          }))
          // 更新欢迎区风险预警数
          if (this.welcomeStats[3]) {
            this.$set(this.welcomeStats, 3, { ...this.welcomeStats[3], value: this.riskList.length + '条' })
          }
        }
      } catch (e) {
        console.error('获取风险预警失败', e)
      }
    },
    /** 获取即将到期合同 */
    async fetchExpiringList() {
      try {
        const res = await getExpiringContractList({ days: 30 })
        if (res && res.codes === '1' && res.expiringList) {
          this.expiringList = (res.expiringList || []).map((item, idx) => ({
            id: item.CONTRACTID || idx,
            contractName: item.CONTRACTNAME || '未命名合同',
            counterpart: item.COUNTERPART || '-',
            expireDate: item.EXPIRE_DATE || '-',
            remainDays: Math.round(Number(item.REMAIN_DAYS) || 0),
          }))
        }
      } catch (e) {
        console.error('获取即将到期合同失败', e)
      }
    },
    /** 获取相对方风险监控 */
    async fetchCounterpartRisk() {
      try {
        const res = await getCounterpartRiskList({})
        if (res && res.codes === '1' && res.counterpartRiskList) {
          this.counterpartRiskStats = (res.counterpartRiskList || []).map(item => ({
            name: item.NAME || '-',
            contractCount: Number(item.CONTRACT_COUNT) || 0,
            overdueCount: Number(item.OVERDUE_COUNT) || 0,
            blacklist: Number(item.BLACKLIST) === 1,
            healthScore: Number(item.HEALTH_SCORE) || 0,
            riskLevel: item.RISK_LEVEL || 'LOW',
            riskLabel: item.RISK_LEVEL === 'HIGH' ? '高风险' : item.RISK_LEVEL === 'MEDIUM' ? '中风险' : '低风险',
          }))
        }
      } catch (e) {
        console.error('获取相对方风险失败', e)
      }
    },
    /** 获取重点合同履行进度 */
    async fetchKeyContracts() {
      try {
        const res = await getKeyContractProgress({ pageSize: 5 })
        if (res && res.codes === '1' && res.keyContractList) {
          this.keyContracts = (res.keyContractList || []).map(item => ({
            contractId: String(item.CONTRACTID || ''),
            contractName: item.CONTRACTNAME || '未命名合同',
            counterpart: item.COUNTERPART || '-',
            amount: item.AMOUNT ? Math.round(Number(item.AMOUNT) / 10000) : 0,
            progressRate: Number(item.PROGRESS_RATE) || 0,
            paymentRate: Number(item.PAYMENT_RATE) || 0,
            delay: Number(item.DELAY_DAYS) || 0,
          }))
        }
      } catch (e) {
        console.error('获取重点合同进度失败', e)
      }
    },
    /** 格式化金额（万元转亿/万显示） */
    formatAmount(amount) {
      if (!amount) return '0'
      const num = Number(amount)
      if (num >= 100000000) return (num / 100000000).toFixed(2) + '亿'
      if (num >= 10000) return (num / 10000).toFixed(0) + '万'
      return String(num)
    },
    /** 格式化数字（加千分位） */
    formatNumber(num) {
      if (!num && num !== 0) return '-'
      return String(num).replace(/\B(?=(\d{3})+(?!\d))/g, ',')
    },
    /** 根据当前主题色动态更新 data 中的品牌色 */
    _patchPrimaryColors() {
      const bright = this.ipBright || '#1890FF'
      const rgb = this.ipPrimaryRgb || '24,144,255'
      const brightBg = `rgba(${rgb},0.1)`
      const brightBgLight = `rgba(${rgb},0.08)`
      // welcomeStats - 第一项"本年签订合同"用主色
      if (this.welcomeStats && this.welcomeStats[0]) this.welcomeStats[0].color = bright
      // kpiCards - 第一项"合同总额"用主色
      if (this.kpiCards && this.kpiCards[0]) {
        this.kpiCards[0].iconBg = brightBg
        this.kpiCards[0].iconColor = bright
        this.kpiCards[0].barBg = brightBg
        this.kpiCards[0].barColor = bright
      }
      // modules - 第0项"管控分析"和第6项"财务管理"用主色
      ;[0, 6].forEach(i => {
        if (this.modules && this.modules[i]) {
          this.modules[i].iconBg = brightBg
          this.modules[i].iconColor = bright
        }
      })
      // aiFeatures - 第0项"AI合同生成"用主色
      if (this.aiFeatures && this.aiFeatures[0]) {
        this.aiFeatures[0].bgColor = brightBgLight
        this.aiFeatures[0].iconColor = bright
      }
      // libraryItems - 第0项"合同范本库"用主色
      if (this.libraryItems && this.libraryItems[0]) {
        this.libraryItems[0].bgColor = brightBgLight
        this.libraryItems[0].iconColor = bright
      }
      // contractTypeStats - 第0项"采购合同"用主色
      if (this.contractTypeStats && this.contractTypeStats[0]) {
        this.contractTypeStats[0].color = bright
      }
    },
  },
}
</script>

<style lang="scss" scoped>
.smart-contract-home {
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
  font-size: 14px;
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
  .ws-value { font-size: 22px; font-weight: 700; }
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

/* ========== 全链路流程图（拓扑图） ========== */
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
    &.blue { background: var(--ip-primary, #1890FF); }
    &.green { background: #52C41A; }
    &.orange { background: #FA8C16; }
    &.red { background: #FF4D4F; }
    &.cyan { background: #13C2C2; }
    &.gray { background: #bbb; }
  }
}

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
.flow-canvas {
  position: relative;
  margin: 0 auto;
}
.flow-svg {
  position: absolute;
  top: 0; left: 0;
  pointer-events: none;
  z-index: 1;
}

/* 边线样式 */
.fl-edge {
  stroke-width: 1.6;
  fill: none;
  &.edge-main {
    stroke: var(--ip-primary, #1890FF);
    stroke-width: 2.2;
  }
  &.edge-sub {
    stroke: #b8b8b8;
    stroke-dasharray: 4 3;
  }
  &.edge-finance {
    stroke: #13C2C2;
    stroke-dasharray: 4 3;
  }
  &.edge-legal {
    stroke: #FF4D4F;
    stroke-width: 1.8;
  }
  &.edge-legal-dash {
    stroke: #FF7875;
    stroke-dasharray: 5 3;
  }
  &.edge-warn {
    stroke: #FA8C16;
    stroke-dasharray: 4 3;
  }
}

/* 节点样式 */
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
  &:hover {
    transform: translateY(-2px) scale(1.05);
    box-shadow: 0 6px 18px rgba(0,0,0,.12);
    z-index: 3;
  }
  .fpn-icon-wrap {
    width: 26px; height: 26px;
    border-radius: 6px;
    display: flex; align-items: center; justify-content: center;
    margin-bottom: 2px;
    i { font-size: 14px; }
  }
  .fpn-label {
    font-size: 11px;
    font-weight: 600;
    color: #333;
    white-space: nowrap;
    line-height: 1.2;
  }
  .fpn-dot {
    position: absolute;
    top: -3px; right: -3px;
    width: 9px; height: 9px;
    border-radius: 50%;
    background: #FF4D4F;
    animation: pulse 1.5s infinite;
  }
  .fpn-badge {
    position: absolute;
    top: -7px; right: -7px;
    min-width: 18px; height: 16px;
    padding: 0 4px;
    border-radius: 8px;
    background: #FF4D4F;
    color: #fff;
    font-size: 10px;
    font-weight: 700;
    line-height: 16px;
    text-align: center;
    box-shadow: 0 1px 3px rgba(255,77,79,.4);
  }
  /* 不同 kind 配色 */
  &.fpn-main {
    border-color: var(--ip-primary, #1890FF);
    border-width: 2px;
    background: #fff;
    .fpn-icon-wrap { background: rgba(var(--ip-primary-rgb,24,144,255),0.12); }
    .fpn-icon-wrap i { color: var(--ip-primary, #1890FF); }
    .fpn-label { color: var(--ip-primary, #1890FF); }
  }
  &.fpn-sub {
    border-color: #d9d9d9;
    background: #fff;
    .fpn-icon-wrap { background: #f0f5ff; }
    .fpn-icon-wrap i { color: #5b8def; }
  }
  &.fpn-warn {
    border-color: #FA8C16;
    border-width: 2px;
    background: #FFFBE6;
    .fpn-icon-wrap { background: rgba(250,140,22,0.15); }
    .fpn-icon-wrap i { color: #FA8C16; }
    .fpn-label { color: #FA8C16; }
  }
  &.fpn-finance {
    border-color: #13C2C2;
    background: #f0fafa;
    .fpn-icon-wrap { background: rgba(19,194,194,0.15); }
    .fpn-icon-wrap i { color: #13C2C2; }
    .fpn-label { color: #08979C; }
  }
  &.fpn-legal {
    border-color: #FF7875;
    background: #FFF1F0;
    .fpn-icon-wrap { background: rgba(255,77,79,0.12); }
    .fpn-icon-wrap i { color: #FF4D4F; }
    .fpn-label { color: #CF1322; }
  }
  &.fpn-config {
    border-color: #e8e8e8;
    background: #fafafa;
    border-style: dashed;
    .fpn-icon-wrap { background: #f0f0f0; }
    .fpn-icon-wrap i { color: #888; }
    .fpn-label { color: #666; font-weight: 500; }
  }
  &.fpn-alert {
    animation: nodeAlert 2s infinite;
  }
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

/* ========== 待办 ========== */
.inner-tabs {
  ::v-deep .el-tabs__header { margin-bottom: 8px; }
  ::v-deep .el-tabs__item { font-size: 13px; padding: 0 12px; }
}
.tab-badge {
  ::v-deep .el-badge__content { top: -2px; }
}
.todo-list { }
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
.todo-info {
  flex: 1;
  min-width: 0;
}
.todo-name {
  font-size: 13px;
  color: #333;
  font-weight: 500;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.todo-meta {
  font-size: 11px;
  color: #999;
  margin-top: 2px;
}
.todo-right-col {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 4px;
}
.todo-time {
  font-size: 11px;
  color: #bbb;
}

/* ========== 合同类型 ========== */
.type-chart-area {
  padding: 0 4px;
  max-height: 220px;
  overflow-y: auto;
  &::-webkit-scrollbar {
    width: 4px;
  }
  &::-webkit-scrollbar-thumb {
    background: #d9d9d9;
    border-radius: 2px;
  }
  &::-webkit-scrollbar-track {
    background: transparent;
  }
}
.type-row {
  margin-bottom: 14px;
}
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

/* ========== 相对方 ========== */
.cp-list { }
.cp-card {
  padding: 10px 12px;
  border-radius: 8px;
  border: 1px solid #f0f0f0;
  margin-bottom: 10px;
  transition: all 0.15s;
  &:hover { border-color: #d9d9d9; box-shadow: 0 2px 8px rgba(0,0,0,.06); }
}
.cp-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
  .cp-name { font-size: 13px; font-weight: 500; color: #333; }
}
.cp-metrics {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}
.cp-metric {
  text-align: center;
  .cp-metric-val { display: block; font-size: 14px; font-weight: 600; color: #333; }
  .cp-metric-lbl { display: block; font-size: 10px; color: #999; margin-top: 2px; }
}
.cp-health-bar {
  height: 3px;
  background: #f5f5f5;
  border-radius: 2px;
  overflow: hidden;
}
.cp-health-fill {
  height: 100%;
  border-radius: 2px;
}

/* ========== 履行进度 ========== */
.contract-progress-list { }
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
    box-shadow: 0 6px 16px rgba(var(--ip-primary-rgb),0.1);
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
    box-shadow: 0 4px 12px rgba(var(--ip-primary-rgb),0.12);
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

/* ========== 文库 ========== */
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