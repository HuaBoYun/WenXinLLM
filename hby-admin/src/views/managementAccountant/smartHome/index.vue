<template>
  <div class="smart-magt-home" :style="themeVars">
    <!-- ========== 顶部欢迎区 ========== -->
    <div class="welcome-section">
      <div class="welcome-bg"></div>
      <div class="welcome-content">
        <div class="welcome-left">
          <div class="greeting">
            <span class="greeting-icon"><i class="el-icon-sunny" /></span>
            <span class="greeting-text">{{ greetingWord }}，{{ userName }}</span>
          </div>
          <h1 class="welcome-title">管理会计智慧服务平台</h1>
          <p class="welcome-desc">全面预算 · 管理账套 · 绩效管理 · 共享服务 · 税务档案 · 价值管理 · 标杆管控</p>
          <div class="welcome-actions">
            <el-button type="primary" icon="el-icon-s-data" round @click="jumpTo('/home/cwzl')">管理会计总览</el-button>
            <el-button icon="el-icon-coin" round @click="jumpTo('/qmys/budgetPreparation')">预算编制</el-button>
            <el-button icon="el-icon-cpu" round @click="jumpTo('/contract/AI/AIAgent')">管理智能体</el-button>
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
          <div class="kpi-trend" v-if="k.trend !== undefined" :class="k.trend >= 0 ? 'trend-up' : 'trend-down'">
            <i :class="k.trend >= 0 ? 'el-icon-top' : 'el-icon-bottom'" />
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

    <!-- ========== 管理会计全链路流程图（拓扑图） ========== -->
    <div class="lifecycle-section">
      <div class="section-header">
        <div class="section-title"><i class="el-icon-share" /> 管理会计全链路流程</div>
        <div class="section-legend">
          <span><i class="dot blue" />主流程</span>
          <span><i class="dot orange" />关注</span>
          <span><i class="dot cyan" />服务并行</span>
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

    <!-- ========== 中部：预算管理 + 统计分析 + 管理账套 ========== -->
    <el-row :gutter="14" class="content-row">
      <!-- 预算管理 -->
      <el-col :span="10">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-coin" /> 全面预算管理</span>
            <span class="card-more" @click="jumpTo('/qmys/budgetPreparation')">预算编制 →</span>
          </div>
          <el-tabs v-model="budgetActive" class="inner-tabs">
            <el-tab-pane name="prepare">
              <span slot="label">预算编制</span>
              <div class="feature-list">
                <div v-for="item in budgetPrepareItems" :key="item.name" class="feature-entry" @click="jumpTo(item.route)">
                  <div class="fe-icon" :style="{background: item.bgColor}"><i :class="item.icon" :style="{color: item.iconColor}" /></div>
                  <div class="fe-body"><span class="fe-name">{{ item.name }}</span><span class="fe-desc">{{ item.desc }}</span></div>
                  <i class="el-icon-arrow-right fe-arrow" />
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane name="control">
              <span slot="label">预算控制</span>
              <div class="feature-list">
                <div v-for="item in budgetControlItems" :key="item.name" class="feature-entry" @click="jumpTo(item.route)">
                  <div class="fe-icon" :style="{background: item.bgColor}"><i :class="item.icon" :style="{color: item.iconColor}" /></div>
                  <div class="fe-body"><span class="fe-name">{{ item.name }}</span><span class="fe-desc">{{ item.desc }}</span></div>
                  <i class="el-icon-arrow-right fe-arrow" />
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane name="analysis">
              <span slot="label">预算分析</span>
              <div class="feature-list">
                <div v-for="item in budgetAnalysisItems" :key="item.name" class="feature-entry" @click="jumpTo(item.route)">
                  <div class="fe-icon" :style="{background: item.bgColor}"><i :class="item.icon" :style="{color: item.iconColor}" /></div>
                  <div class="fe-body"><span class="fe-name">{{ item.name }}</span><span class="fe-desc">{{ item.desc }}</span></div>
                  <i class="el-icon-arrow-right fe-arrow" />
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-col>

      <!-- 统计分析 -->
      <el-col :span="7">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-data-analysis" /> 统计分析</span>
            <span class="card-more" @click="jumpTo('/home/cwzl')">总览 →</span>
          </div>
          <div class="stats-entry-list">
            <div v-for="s in statsItems" :key="s.name" class="stats-entry" @click="jumpTo(s.route)">
              <div class="se-icon" :style="{background: s.bgColor}"><i :class="s.icon" :style="{color: s.iconColor}" /></div>
              <div class="se-body"><div class="se-name">{{ s.name }}</div><div class="se-desc">{{ s.desc }}</div></div>
              <i class="el-icon-arrow-right se-arrow" />
            </div>
          </div>
        </div>
      </el-col>

      <!-- 管理账套 -->
      <el-col :span="7">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-s-finance" /> 管理账套</span>
            <span class="card-more" @click="jumpTo('/managementAccountant/glzt/companyData/statement')">报表数据 →</span>
          </div>
          <el-tabs v-model="ledgerActive" class="inner-tabs-sm">
            <el-tab-pane v-for="tab in ledgerTabs" :key="tab.key" :name="tab.key">
              <span slot="label">{{ tab.label }}</span>
              <div class="ledger-list">
                <div v-for="item in tab.items" :key="item.name" class="ledger-item" @click="jumpTo(item.route)">
                  <i :class="item.icon" :style="{color: item.iconColor}" /><span>{{ item.name }}</span>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-col>
    </el-row>

    <!-- ========== 绩效 + 共享服务 + 税务 + 档案 ========== -->
    <el-row :gutter="14" class="content-row">
      <!-- 绩效管理 -->
      <el-col :span="6">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-trophy" /> 绩效管理</span>
            <span class="card-more" @click="jumpTo('/managementAccountant/pm')">进入 →</span>
          </div>
          <div class="sub-list">
            <div v-for="item in pmItems" :key="item.name" class="sub-entry" @click="jumpTo(item.route)">
              <div class="sub-icon" :style="{background: item.bgColor}"><i :class="item.icon" :style="{color: item.iconColor}" /></div>
              <div class="sub-body"><div class="sub-name">{{ item.name }}</div><div class="sub-desc">{{ item.desc }}</div></div>
            </div>
          </div>
        </div>
      </el-col>

      <!-- 共享服务 -->
      <el-col :span="6">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-share" /> 共享服务</span>
            <span class="card-more" @click="jumpTo('/managementAccountant/ss/servicePortal/ServicePortalDashboard')">进入 →</span>
          </div>
          <div class="sub-list">
            <div v-for="item in ssItems" :key="item.name" class="sub-entry" @click="jumpTo(item.route)">
              <div class="sub-icon" :style="{background: item.bgColor}"><i :class="item.icon" :style="{color: item.iconColor}" /></div>
              <div class="sub-body"><div class="sub-name">{{ item.name }}</div><div class="sub-desc">{{ item.desc }}</div></div>
            </div>
          </div>
        </div>
      </el-col>

      <!-- 税务服务 -->
      <el-col :span="6">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-s-ticket" /> 税务服务</span>
            <span class="card-more" @click="jumpTo('/managementAccountant/ts/taxDeclaration')">进入 →</span>
          </div>
          <div class="sub-list">
            <div v-for="item in tsItems" :key="item.name" class="sub-entry" @click="jumpTo(item.route)">
              <div class="sub-icon" :style="{background: item.bgColor}"><i :class="item.icon" :style="{color: item.iconColor}" /></div>
              <div class="sub-body"><div class="sub-name">{{ item.name }}</div><div class="sub-desc">{{ item.desc }}</div></div>
            </div>
          </div>
        </div>
      </el-col>

      <!-- 档案服务 -->
      <el-col :span="6">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-folder-opened" /> 档案服务</span>
            <span class="card-more" @click="jumpTo('/managementAccountant/as/archiveSearch')">进入 →</span>
          </div>
          <div class="sub-list">
            <div v-for="item in asItems" :key="item.name" class="sub-entry" @click="jumpTo(item.route)">
              <div class="sub-icon" :style="{background: item.bgColor}"><i :class="item.icon" :style="{color: item.iconColor}" /></div>
              <div class="sub-body"><div class="sub-name">{{ item.name }}</div><div class="sub-desc">{{ item.desc }}</div></div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- ========== 功能模块全景 + AI智能体 ========== -->
    <el-row :gutter="14" class="content-row">
      <el-col :span="16">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-grid" /> 功能模块全景</span>
          </div>
          <div class="module-grid">
            <div v-for="m in modules" :key="m.name" class="module-card" @click="jumpTo(m.route)">
              <div class="mc-icon" :style="{background: m.iconBg}"><i :class="m.icon" :style="{color: m.iconColor}" /></div>
              <div class="mc-name">{{ m.name }}</div>
              <div class="mc-desc">{{ m.desc }}</div>
            </div>
          </div>
        </div>
      </el-col>

      <el-col :span="8">
        <div class="card-panel ai-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-cpu" /> 管理智能体 & 快捷入口</span>
            <span class="card-more" @click="jumpTo('/contract/AI/AIAgent')">进入 →</span>
          </div>
          <div class="ai-grid">
            <div v-for="ai in aiFeatures" :key="ai.name" class="ai-card" @click="jumpTo(ai.route)">
              <div class="aif-icon" :style="{background: ai.bgColor}"><i :class="ai.icon" :style="{color: ai.iconColor}" /></div>
              <div class="aif-body">
                <div class="aif-name">{{ ai.name }}</div>
                <div class="aif-desc">{{ ai.desc }}</div>
              </div>
              <i class="el-icon-arrow-right aif-arrow" />
            </div>
          </div>
          <div class="quick-divider">快捷入口</div>
          <div class="quick-row">
            <div v-for="q in quickItems" :key="q.name" class="quick-btn" @click="jumpTo(q.route)">
              <i :class="q.icon" :style="{color: q.iconColor}" />
              <span>{{ q.name }}</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- ========== 系统集成 + 高级功能 ========== -->
    <el-row :gutter="14" class="content-row">
      <el-col :span="12">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-connection" /> 系统集成</span>
            <span class="card-more" @click="jumpTo('/qmys/systemIntegration')">集成首页 →</span>
          </div>
          <div class="intg-grid">
            <div v-for="item in intgItems" :key="item.name" class="intg-card" @click="jumpTo(item.route)">
              <div class="ic-icon" :style="{background: item.bgColor}"><i :class="item.icon" :style="{color: item.iconColor}" /></div>
              <div class="ic-name">{{ item.name }}</div>
            </div>
          </div>
        </div>
      </el-col>

      <el-col :span="12">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-magic-stick" /> 高级功能</span>
            <span class="card-more" @click="jumpTo('/qmys/advancedFeatures')">高级功能 →</span>
          </div>
          <div class="intg-grid">
            <div v-for="item in advItems" :key="item.name" class="intg-card" @click="jumpTo(item.route)">
              <div class="ic-icon" :style="{background: item.bgColor}"><i :class="item.icon" :style="{color: item.iconColor}" /></div>
              <div class="ic-name">{{ item.name }}</div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { investThemeMixin } from '@/views/stateAssets/themeMixin'
import { getSmartHomeData } from '@/api/managementAccountant/smartHome'

export default {
  name: 'SmartManagementAccountantHome',
  mixins: [investThemeMixin],
  data() {
    return {
      userName: '',
      loading: false,
      budgetActive: 'prepare',
      ledgerActive: 'glzt',
      welcomeStats: [
        { label: '预算执行率', value: '-', color: '#52C41A' },
        { label: '预算编制完成', value: '-', color: '#1890FF' },
        { label: '绩效评分', value: '-', color: '#722ED1' },
        { label: '税务申报', value: '-', color: '#FA8C16' },
        { label: '账套数量', value: '-', color: '#13C2C2' },
      ],
      kpiCards: [
        { label: '预算执行率', value: '-', unit: '%', icon: 'el-icon-coin', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', barBg: 'rgba(24,144,255,0.1)', barColor: '#1890FF', barWidth: 0, trend: 0 },
        { label: '预算偏差率', value: '-', unit: '%', icon: 'el-icon-s-data', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', barBg: 'rgba(250,140,22,0.1)', barColor: '#FA8C16', barWidth: 0, trend: 0, theme: 'kpi-warning' },
        { label: '成本节约率', value: '-', unit: '%', icon: 'el-icon-s-finance', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', barBg: 'rgba(82,196,26,0.1)', barColor: '#52C41A', barWidth: 0, trend: 0 },
        { label: '绩效达标率', value: '-', unit: '%', icon: 'el-icon-trophy', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', barBg: 'rgba(114,46,209,0.1)', barColor: '#722ED1', barWidth: 0, trend: 0 },
        { label: '税务合规率', value: '-', unit: '%', icon: 'el-icon-s-ticket', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', barBg: 'rgba(19,194,194,0.1)', barColor: '#13C2C2', barWidth: 0, trend: 0 },
        { label: '报表完成率', value: '-', unit: '%', icon: 'el-icon-document', iconBg: 'rgba(235,47,150,0.1)', iconColor: '#EB2F96', barBg: 'rgba(235,47,150,0.1)', barColor: '#EB2F96', barWidth: 0, trend: 0 },
      ],
      // ===== 流程图拓扑数据 =====
      flowConfig: { colW: 130, rowH: 92, nodeW: 92, nodeH: 56, padX: 20, padY: 16 },
      flowNodes: [
        // 主干（row 2）：预算设置 → 预算编制 → 预算控制 → 预算分析 → 管理账套 → 统计分析 → 绩效管理
        { id: 'setup', name: '预算设置', col: 0, row: 2, icon: 'el-icon-setting', kind: 'main', route: '/qmys/budgetSystem' },
        { id: 'prepare', name: '预算编制', col: 1.5, row: 2, icon: 'el-icon-edit', kind: 'main', route: '/qmys/budgetPreparation' },
        { id: 'control', name: '预算控制', col: 3, row: 2, icon: 'el-icon-s-check', kind: 'warn', route: '/qmys/budgetControl', alert: false, badge: '' },
        { id: 'analysis', name: '预算分析', col: 4.5, row: 2, icon: 'el-icon-data-analysis', kind: 'main', route: '/qmys/budgetAnalysis' },
        { id: 'ledger', name: '管理账套', col: 6, row: 2, icon: 'el-icon-s-finance', kind: 'main', route: '/managementAccountant/glzt/companyData/statement' },
        { id: 'stats', name: '统计分析', col: 7.5, row: 2, icon: 'el-icon-s-marketing', kind: 'main', route: '/home/cwzl' },
        { id: 'perf', name: '绩效管理', col: 9, row: 2, icon: 'el-icon-trophy', kind: 'main', route: '/managementAccountant/pm' },

        // 上层第一排（row 0）：系统配置支撑
        { id: 'org', name: '组织结构', col: 0, row: 0, icon: 'el-icon-office-building', kind: 'config', route: '/qmys/OrganizationStructure' },
        { id: 'dim', name: '维度配置', col: 1.5, row: 0, icon: 'el-icon-s-grid', kind: 'config', route: '/qmys/DimensionConfiguration' },
        { id: 'model', name: '预算模型', col: 3, row: 0, icon: 'el-icon-cpu', kind: 'config', route: '/qmys/BudgetModel' },
        { id: 'indicator', name: '指标管理', col: 4.5, row: 0, icon: 'el-icon-s-data', kind: 'config', route: '/qmys/IndicatorManagement' },

        // 上层第二排（row 1）：编制支撑
        { id: 'template', name: '预算模版', col: 2, row: 1, icon: 'el-icon-document-copy', kind: 'sub', route: '/qmys/budgetTemplate' },
        { id: 'approval', name: '审批流程', col: 3.5, row: 1, icon: 'el-icon-s-claim', kind: 'sub', route: '/qmys/approvalFlow' },
        { id: 'dataEntry', name: '预算数据', col: 5, row: 1, icon: 'el-icon-edit-outline', kind: 'sub', route: '/qmys/budgetDataEntry' },
        { id: 'variance', name: '差异分析', col: 6.5, row: 1, icon: 'el-icon-s-marketing', kind: 'sub', route: '/qmys/VarianceAnalysis' },
        { id: 'rolling', name: '滚动预测', col: 8, row: 1, icon: 'el-icon-refresh', kind: 'sub', route: '/qmys/RollingForecast' },

        // 下层（row 3）：控制分支 & 服务
        { id: 'execution', name: '预算执行', col: 2, row: 3, icon: 'el-icon-data-line', kind: 'finance', route: '/qmys/BudgetExecution' },
        { id: 'warning', name: '预算预警', col: 3.5, row: 3, icon: 'el-icon-warning-outline', kind: 'finance', route: '/qmys/BudgetWarning' },
        { id: 'freeze', name: '预算冻结', col: 5, row: 3, icon: 'el-icon-lock', kind: 'finance', route: '/qmys/BudgetFreeze' },
        { id: 'share', name: '共享服务', col: 6.5, row: 3, icon: 'el-icon-share', kind: 'finance', route: '/managementAccountant/ss/servicePortal/ServicePortalDashboard' },
        { id: 'intg', name: '系统集成', col: 8, row: 3, icon: 'el-icon-connection', kind: 'finance', route: '/qmys/systemIntegration' },

        // 底层（row 4）：支撑服务
        { id: 'tax', name: '税务服务', col: 0.5, row: 4, icon: 'el-icon-s-ticket', kind: 'config', route: '/managementAccountant/ts/taxDeclaration' },
        { id: 'archive', name: '档案服务', col: 2, row: 4, icon: 'el-icon-folder-opened', kind: 'config', route: '/managementAccountant/as/archiveSearch' },
        { id: 'adv', name: '高级功能', col: 4, row: 4, icon: 'el-icon-magic-stick', kind: 'config', route: '/qmys/advancedFeatures' },
        { id: 'msg', name: '消息中心', col: 6, row: 4, icon: 'el-icon-bell', kind: 'config', route: '/home/xxzx' },
        { id: 'ai', name: '管理智能体', col: 8, row: 4, icon: 'el-icon-cpu', kind: 'config', route: '/contract/AI/AIAgent' },
      ],
      flowEdges: [
        // 主干流程（实线）
        ['setup', 'prepare', 'main'], ['prepare', 'control', 'main'], ['control', 'analysis', 'main'],
        ['analysis', 'ledger', 'main'], ['ledger', 'stats', 'main'], ['stats', 'perf', 'main'],
        // 上层配置 → 主干（虚线）
        ['org', 'setup', 'sub'], ['dim', 'setup', 'sub'],
        ['model', 'prepare', 'sub'], ['indicator', 'prepare', 'sub'],
        // 编制支撑 → 主干
        ['template', 'prepare', 'sub'], ['approval', 'prepare', 'sub'], ['dataEntry', 'analysis', 'sub'],
        // 分析分支
        ['variance', 'analysis', 'sub'], ['rolling', 'analysis', 'sub'],
        // 控制 → 下层
        ['control', 'execution', 'finance'], ['control', 'warning', 'finance'], ['control', 'freeze', 'finance'],
        // 管理账套 → 共享服务
        ['ledger', 'share', 'finance'],
        // 分析 → 系统集成
        ['analysis', 'intg', 'finance'],
      ],
      budgetPrepareItems: [
        { name: '预算编制首页', desc: '编制总览与启动', icon: 'el-icon-s-home', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/qmys/budgetPreparation' },
        { name: '预算数据录入', desc: '预算数据填报录入', icon: 'el-icon-edit', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/qmys/budgetDataEntry' },
        { name: '预算模版', desc: '预算编制模版管理', icon: 'el-icon-document-copy', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/qmys/budgetTemplate' },
        { name: '预算分配', desc: '指标分解与分配', icon: 'el-icon-share', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/qmys/BudgetAllocation' },
        { name: '预算调整', desc: '预算调整与修正', icon: 'el-icon-refresh', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/qmys/budgetAdjustment' },
        { name: '审批流程', desc: '预算审批流程管理', icon: 'el-icon-s-check', bgColor: 'rgba(235,47,150,0.08)', iconColor: '#EB2F96', route: '/qmys/approvalFlow' },
      ],
      budgetControlItems: [
        { name: '预算控制首页', desc: '控制总览监控大屏', icon: 'el-icon-monitor', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/qmys/budgetControl' },
        { name: '预算执行监控', desc: '执行进度实时监控', icon: 'el-icon-data-line', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/qmys/BudgetExecution' },
        { name: '预算预警', desc: '超支预警自动提醒', icon: 'el-icon-warning-outline', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/qmys/BudgetWarning' },
        { name: '预算控制', desc: '授权控制与约束', icon: 'el-icon-lock', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/qmys/BudgetControl' },
        { name: '预算冻结', desc: '预算冻结与释放', icon: 'el-icon-turn-off', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/qmys/BudgetFreeze' },
        { name: '预算监控大屏', desc: '大屏可视化展示', icon: 'el-icon-full-screen', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/qmys/BudgetMonitor' },
      ],
      budgetAnalysisItems: [
        { name: '预算分析首页', desc: '分析总览仪表板', icon: 'el-icon-data-analysis', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/qmys/budgetAnalysis' },
        { name: '差异分析', desc: '预实差异深度分析', icon: 'el-icon-s-marketing', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/qmys/VarianceAnalysis' },
        { name: '趋势分析', desc: '多维度趋势分析', icon: 'el-icon-data-line', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/qmys/TrendAnalysis' },
        { name: '滚动预测', desc: '动态滚动预测', icon: 'el-icon-refresh', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/qmys/RollingForecast' },
        { name: '场景分析', desc: '多场景模拟分析', icon: 'el-icon-s-opportunity', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/qmys/ScenarioAnalysis' },
        { name: '分析报告', desc: '综合分析报告输出', icon: 'el-icon-document', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/qmys/AnalysisReport' },
      ],
      statsItems: [
        { name: '管理会计总览', desc: '财务综合总览', icon: 'el-icon-s-home', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/home/cwzl' },
        { name: '模版一·利润', desc: '利润综合分析', icon: 'el-icon-data-line', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/home/lrfx' },
        { name: '模版二·收入', desc: '收入结构分析', icon: 'el-icon-coin', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/home/srfx' },
        { name: '模版三·成本', desc: '成本费用分析', icon: 'el-icon-s-order', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/home/cbfyfx' },
        { name: '模版四·资产', desc: '资产负债分析', icon: 'el-icon-s-finance', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/glkj/cwztfx/zcfzfx' },
        { name: '模版五·应收', desc: '应收应付分析', icon: 'el-icon-s-claim', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/glkj/cwztfx/ysyfzkfx' },
        { name: '消息中心', desc: '通知与系统消息', icon: 'el-icon-bell', bgColor: 'rgba(235,47,150,0.08)', iconColor: '#EB2F96', route: '/home/xxzx' },
      ],
      ledgerTabs: [
        {
          key: 'glzt', label: '管理账套',
          items: [
            { name: '报表数据', icon: 'el-icon-document', iconColor: '#1890FF', route: '/managementAccountant/glzt/companyData/statement' },
            { name: '账簿数据', icon: 'el-icon-notebook-2', iconColor: '#52C41A', route: '/managementAccountant/glzt/companyData/ZNFXaccountData' },
            { name: '科目表', icon: 'el-icon-s-order', iconColor: '#FA8C16', route: '/managementAccountant/glzt/companyData/sub/subject' },
            { name: '余额表', icon: 'el-icon-s-data', iconColor: '#722ED1', route: '/managementAccountant/glzt/companyData/sub/balance' },
            { name: '总分类账', icon: 'el-icon-reading', iconColor: '#13C2C2', route: '/managementAccountant/glzt/companyData/sub/accountCate' },
            { name: '明细账', icon: 'el-icon-document-copy', iconColor: '#EB2F96', route: '/managementAccountant/glzt/companyData/sub/accountDetail' },
            { name: '凭证库', icon: 'el-icon-collection', iconColor: '#FF4D4F', route: '/managementAccountant/glzt/companyData/sub/voucherLib' },
          ]
        },
        {
          key: 'jgzt', label: '监管账套',
          items: [
            { name: '报表数据', icon: 'el-icon-document', iconColor: '#1890FF', route: '/managementAccountant/jgzt/companyData/statement' },
            { name: '账簿数据', icon: 'el-icon-notebook-2', iconColor: '#52C41A', route: '/managementAccountant/jgzt/companyData/ZNFXaccountData' },
            { name: '科目表', icon: 'el-icon-s-order', iconColor: '#FA8C16', route: '/managementAccountant/jgzt/companyData/sub/subject' },
            { name: '余额表', icon: 'el-icon-s-data', iconColor: '#722ED1', route: '/managementAccountant/jgzt/companyData/sub/balance' },
            { name: '总分类账', icon: 'el-icon-reading', iconColor: '#13C2C2', route: '/managementAccountant/jgzt/companyData/sub/accountCate' },
            { name: '凭证库', icon: 'el-icon-collection', iconColor: '#FF4D4F', route: '/managementAccountant/jgzt/companyData/sub/voucherLib' },
            { name: '放大镜', icon: 'el-icon-search', iconColor: '#1890FF', route: '/workbench/auditTools/magnifyingGlass' },
          ]
        },
        {
          key: 'zbzt', label: '资本账套',
          items: [
            { name: '报表数据', icon: 'el-icon-document', iconColor: '#1890FF', route: '/managementAccountant/zbzt/companyData/statement' },
            { name: '账簿数据', icon: 'el-icon-notebook-2', iconColor: '#52C41A', route: '/managementAccountant/zbzt/companyData/ZNFXaccountData' },
            { name: '科目表', icon: 'el-icon-s-order', iconColor: '#FA8C16', route: '/managementAccountant/zbzt/companyData/sub/subject' },
            { name: '余额表', icon: 'el-icon-s-data', iconColor: '#722ED1', route: '/managementAccountant/zbzt/companyData/sub/balance' },
            { name: '总分类账', icon: 'el-icon-reading', iconColor: '#13C2C2', route: '/managementAccountant/zbzt/companyData/sub/accountCate' },
            { name: '凭证库', icon: 'el-icon-collection', iconColor: '#FF4D4F', route: '/managementAccountant/zbzt/companyData/sub/voucherLib' },
            { name: '放大镜', icon: 'el-icon-search', iconColor: '#1890FF', route: '/workbench/auditTools/magnifyingGlass' },
          ]
        },
        {
          key: 'gkzt', label: '管控账套',
          items: [
            { name: '报表数据', icon: 'el-icon-document', iconColor: '#1890FF', route: '/managementAccountant/gkzt/companyData/statement' },
            { name: '账簿数据', icon: 'el-icon-notebook-2', iconColor: '#52C41A', route: '/managementAccountant/gkzt/companyData/ZNFXaccountData' },
            { name: '科目表', icon: 'el-icon-s-order', iconColor: '#FA8C16', route: '/managementAccountant/gkzt/companyData/sub/subject' },
            { name: '余额表', icon: 'el-icon-s-data', iconColor: '#722ED1', route: '/managementAccountant/gkzt/companyData/sub/balance' },
            { name: '总分类账', icon: 'el-icon-reading', iconColor: '#13C2C2', route: '/managementAccountant/gkzt/companyData/sub/accountCate' },
            { name: '凭证库', icon: 'el-icon-collection', iconColor: '#FF4D4F', route: '/managementAccountant/gkzt/companyData/sub/voucherLib' },
            { name: '放大镜', icon: 'el-icon-search', iconColor: '#1890FF', route: '/workbench/auditTools/magnifyingGlass' },
          ]
        },
      ],
      pmItems: [
        { name: '绩效管理首页', desc: '绩效总览', icon: 'el-icon-s-home', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/managementAccountant/pm' },
        { name: '考核方案', desc: '考核方案配置', icon: 'el-icon-s-check', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/managementAccountant/pm/assessmentPlan' },
        { name: '目标管理', desc: '绩效目标分解', icon: 'el-icon-aim', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/managementAccountant/pm/targetManagement' },
        { name: '绩效面谈', desc: '绩效面谈记录', icon: 'el-icon-chat-dot-round', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/managementAccountant/pm/performanceInterview' },
        { name: '激励管理', desc: '激励方案管理', icon: 'el-icon-trophy', bgColor: 'rgba(235,47,150,0.08)', iconColor: '#EB2F96', route: '/managementAccountant/pm/incentiveManagement' },
        { name: '360度评估', desc: '多维度评估', icon: 'el-icon-refresh', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/managementAccountant/pm/assessment360' },
      ],
      ssItems: [
        { name: '服务门户', desc: '共享服务入口', icon: 'el-icon-s-grid', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/managementAccountant/ss/servicePortal/ServicePortalDashboard' },
        { name: '智能审核', desc: '凭证智能审核', icon: 'el-icon-cpu', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/managementAccountant/ss/intelligentAudit/IntelligentAuditDashboard' },
        { name: '成本分摊', desc: '成本分摊计算', icon: 'el-icon-s-finance', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/managementAccountant/ss/costAllocation' },
        { name: '数字员工', desc: 'RPA数字员工', icon: 'el-icon-user-solid', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/managementAccountant/ss/digitalEmployee' },
        { name: '质量管控', desc: '数据质量管控', icon: 'el-icon-s-check', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/managementAccountant/ss/qualityControl' },
        { name: '作业调度', desc: '自动化作业调度', icon: 'el-icon-time', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/managementAccountant/ss/jobSchedule' },
      ],
      tsItems: [
        { name: '税务申报', desc: '税务申报管理', icon: 'el-icon-document-checked', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/managementAccountant/ts/taxDeclaration' },
        { name: '发票管理', desc: '发票开具与管理', icon: 'el-icon-s-ticket', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/managementAccountant/ts/invoiceManagement' },
        { name: '税务筹划', desc: '税务优化筹划', icon: 'el-icon-s-opportunity', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/managementAccountant/ts/taxPlanning' },
        { name: '合规检查', desc: '税务合规检查', icon: 'el-icon-s-check', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/managementAccountant/ts/taxCompliance' },
        { name: '税务风险', desc: '税务风险识别', icon: 'el-icon-warning-outline', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/managementAccountant/ts/taxRisk' },
        { name: '税务分析', desc: '税务数据分析', icon: 'el-icon-data-analysis', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/managementAccountant/ts/taxAnalytics' },
      ],
      asItems: [
        { name: '档案检索', desc: '全文检索查询', icon: 'el-icon-search', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/managementAccountant/as/archiveSearch' },
        { name: '智能分类', desc: 'AI智能分类归档', icon: 'el-icon-cpu', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/managementAccountant/as/intelligentClassification' },
        { name: '文档版本', desc: '版本管理与追踪', icon: 'el-icon-document-copy', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/managementAccountant/as/documentVersion' },
        { name: '档案权限', desc: '访问权限管理', icon: 'el-icon-lock', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/managementAccountant/as/archivePermission' },
      ],
      modules: [
        { name: '全面预算', icon: 'el-icon-coin', desc: '预算编制/控制/分析', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', route: '/qmys/budgetPreparation' },
        { name: '预算设置', icon: 'el-icon-setting', desc: '预算体系配置', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', route: '/qmys/budgetSystem' },
        { name: '预算分析', icon: 'el-icon-data-analysis', desc: '差异/趋势/预测', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', route: '/qmys/budgetAnalysis' },
        { name: '管理账套', icon: 'el-icon-s-finance', desc: '管理/监管/资本', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', route: '/managementAccountant/glzt/companyData/statement' },
        { name: '绩效管理', icon: 'el-icon-trophy', desc: '考核/目标/激励', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', route: '/managementAccountant/pm' },
        { name: '共享服务', icon: 'el-icon-share', desc: '智能审核/成本分摊', iconBg: 'rgba(235,47,150,0.1)', iconColor: '#EB2F96', route: '/managementAccountant/ss/servicePortal/ServicePortalDashboard' },
        { name: '税务服务', icon: 'el-icon-s-ticket', desc: '申报/发票/筹划', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', route: '/managementAccountant/ts/taxDeclaration' },
        { name: '档案服务', icon: 'el-icon-folder-opened', desc: '检索/分类/版本', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', route: '/managementAccountant/as/archiveSearch' },
        { name: '统计分析', icon: 'el-icon-s-marketing', desc: '五大分析模版', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', route: '/home/cwzl' },
        { name: 'EPS预算体系', icon: 'el-icon-s-grid', desc: 'EPS预算标准管理', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', route: '/managementAccountant/eps/budgetSystem' },
        { name: '系统集成', icon: 'el-icon-connection', desc: 'ERP/BI/云平台', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', route: '/qmys/systemIntegration' },
        { name: '高级功能', icon: 'el-icon-magic-stick', desc: '智能推荐/多币种', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', route: '/qmys/advancedFeatures' },
        { name: '接口管理', icon: 'el-icon-set-up', desc: '接口配置与管理', iconBg: 'rgba(235,47,150,0.1)', iconColor: '#EB2F96', route: '/managementAccountant/intg/apiManagement' },
        { name: '移动应用', icon: 'el-icon-mobile-phone', desc: '移动端配置管理', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', route: '/managementAccountant/mobile/appConfig' },
        { name: '消息中心', icon: 'el-icon-bell', desc: '通知与消息管理', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', route: '/home/xxzx' },
        { name: '管理智能体', icon: 'el-icon-cpu', desc: 'AI智能管理助手', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', route: '/contract/AI/AIAgent' },
      ],
      aiFeatures: [
        { name: '管理智能体', desc: 'AI驱动管理分析', icon: 'el-icon-cpu', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/contract/AI/AIAgent' },
        { name: '智能审核', desc: '凭证智能审核引擎', icon: 'el-icon-s-check', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/managementAccountant/ss/intelligentAudit/IntelligentAuditDashboard' },
        { name: '智能推荐', desc: '预算智能推荐引擎', icon: 'el-icon-s-opportunity', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/qmys/IntelligentRecommendation' },
        { name: '预算预警', desc: '超支风险智能预警', icon: 'el-icon-warning-outline', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/qmys/BudgetWarning' },
      ],
      quickItems: [
        { name: '公司账簿', icon: 'el-icon-notebook-2', iconColor: '#1890FF', route: '/cwsc/jcpz/gszb' },
        { name: '预算监控', icon: 'el-icon-full-screen', iconColor: '#52C41A', route: '/qmys/BudgetMonitor' },
        { name: '税务审计', icon: 'el-icon-s-check', iconColor: '#FA8C16', route: '/managementAccountant/ts/taxAudit' },
        { name: '穿透查询', icon: 'el-icon-zoom-in', iconColor: '#722ED1', route: '/qmys/DrillThroughQuery' },
        { name: '版本对比', icon: 'el-icon-document-copy', iconColor: '#13C2C2', route: '/qmys/VersionComparison' },
        { name: '消息中心', icon: 'el-icon-bell', iconColor: '#EB2F96', route: '/home/xxzx' },
      ],
      intgItems: [
        { name: 'ERP集成', icon: 'el-icon-connection', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/qmys/ErpIntegration' },
        { name: 'API集成', icon: 'el-icon-set-up', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/qmys/ApiIntegration' },
        { name: 'BI集成', icon: 'el-icon-data-analysis', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/qmys/BiIntegration' },
        { name: '云平台', icon: 'el-icon-upload2', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/qmys/CloudIntegration' },
        { name: '数据库集成', icon: 'el-icon-coin', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/qmys/DatabaseIntegration' },
        { name: '数据映射', icon: 'el-icon-sort', bgColor: 'rgba(235,47,150,0.08)', iconColor: '#EB2F96', route: '/qmys/DataMapping' },
        { name: '集成配置', icon: 'el-icon-setting', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/qmys/IntegrationConfig' },
        { name: '集成监控', icon: 'el-icon-monitor', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/qmys/IntegrationMonitor' },
        { name: 'WEB服务', icon: 'el-icon-connection', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/qmys/WebServiceIntegration' },
        { name: '消息队列', icon: 'el-icon-s-grid', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/qmys/MessageQueueIntegration' },
      ],
      advItems: [
        { name: '智能推荐', icon: 'el-icon-s-opportunity', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/qmys/IntelligentRecommendation' },
        { name: '预算优化', icon: 'el-icon-magic-stick', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/qmys/BudgetOptimization' },
        { name: '批量计算', icon: 'el-icon-s-data', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/qmys/BatchCalculation' },
        { name: '穿透查询', icon: 'el-icon-zoom-in', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/qmys/DrillThroughQuery' },
        { name: '公式追踪', icon: 'el-icon-s-marketing', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/qmys/FormulaTrace' },
        { name: '数据挖掘', icon: 'el-icon-search', bgColor: 'rgba(235,47,150,0.08)', iconColor: '#EB2F96', route: '/qmys/DataMining' },
        { name: '多币种管理', icon: 'el-icon-coin', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/qmys/CurrencyManagement' },
        { name: '协同编制', icon: 'el-icon-s-custom', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/qmys/CollaborativeBudgeting' },
        { name: '版本对比', icon: 'el-icon-document-copy', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/qmys/VersionComparison' },
        { name: '高级报表', icon: 'el-icon-document', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/qmys/AdvancedReports' },
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
        sub: 'url(#fl-arrow)',
        finance: 'url(#fl-arrow-finance)',
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
          const dirY = bc.y > ac.y ? 1 : -1
          const dirX = bc.x > ac.x ? 1 : -1
          sx = ac.x; sy = ac.y + dirY * (nodeH / 2 + 2)
          tx = bc.x - dirX * (nodeW / 2 + 2); ty = bc.y
          d = `M${sx},${sy} L${sx},${ty} L${tx},${ty}`
        }
        return { d, cls: 'edge-' + kind, marker: markerMap[kind] || markerMap.sub }
      })
    },
  },
  watch: {
    ipBright: { immediate: true, handler() { this._patchPrimaryColors() } },
  },
  created() {
    try {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      this.userName = userInfo ? (userInfo.realname || userInfo.username || '') : ''
    } catch (e) { this.userName = '' }
    this._patchPrimaryColors()
    this.fetchSmartHomeData()
  },
  methods: {
    jumpTo(route) { if (route) this.$router.push(route) },
    nodePos(n) {
      const { colW, rowH, nodeW, nodeH, padX, padY } = this.flowConfig
      const left = padX + n.col * colW + (colW - nodeW) / 2
      const top = padY + n.row * rowH + (rowH - nodeH) / 2
      return { left: left + 'px', top: top + 'px', width: nodeW + 'px', height: nodeH + 'px' }
    },
    /** 获取首页汇总数据 */
    async fetchSmartHomeData() {
      this.loading = true
      try {
        const res = await getSmartHomeData()
        if (res && res.code === 1 && res.data) {
          const { welcomeStats, kpiCards, pendingCounts } = res.data
          // 更新欢迎区
          if (welcomeStats) {
            this.$set(this.welcomeStats, 0, { ...this.welcomeStats[0], value: welcomeStats.budgetExecutionRate || '-' })
            this.$set(this.welcomeStats, 1, { ...this.welcomeStats[1], value: welcomeStats.budgetPreparationCount || '-' })
            this.$set(this.welcomeStats, 2, { ...this.welcomeStats[2], value: welcomeStats.performanceScore || '-' })
            this.$set(this.welcomeStats, 3, { ...this.welcomeStats[3], value: welcomeStats.taxPendingCount || '-' })
            this.$set(this.welcomeStats, 4, { ...this.welcomeStats[4], value: welcomeStats.ledgerCount || '-' })
          }
          // 更新KPI卡片
          if (kpiCards && kpiCards.length) {
            kpiCards.forEach((item, idx) => {
              if (this.kpiCards[idx]) {
                this.$set(this.kpiCards, idx, {
                  ...this.kpiCards[idx],
                  value: item.value || '-',
                  barWidth: item.barWidth || 0,
                  trend: item.trend || 0,
                })
              }
            })
          }
          // 更新链路节点徽标
          if (pendingCounts) {
            const controlNode = this.flowNodes.find(n => n.id === 'control')
            if (controlNode && pendingCounts.budgetControl > 0) {
              controlNode.badge = String(pendingCounts.budgetControl)
              controlNode.alert = true
            }
          }
        }
      } catch (e) {
        console.error('获取管理会计首页数据失败', e)
      } finally {
        this.loading = false
      }
    },
    _patchPrimaryColors() {
      const bright = this.ipBright || '#1890FF'
      const rgb = this.ipPrimaryRgb || '24,144,255'
      const brightBg = `rgba(${rgb},0.1)`
      const brightBg2 = `rgba(${rgb},0.08)`
      if (this.kpiCards && this.kpiCards[0]) { this.kpiCards[0].iconBg = brightBg; this.kpiCards[0].iconColor = bright; this.kpiCards[0].barBg = brightBg; this.kpiCards[0].barColor = bright }
      if (this.modules && this.modules[0]) { this.modules[0].iconBg = brightBg; this.modules[0].iconColor = bright }
      if (this.aiFeatures && this.aiFeatures[0]) { this.aiFeatures[0].bgColor = brightBg2; this.aiFeatures[0].iconColor = bright }
    },
  },
}
</script>

<style lang="scss" scoped>
.smart-magt-home { padding: 16px; background: #f0f2f5; min-height: 100vh; }

/* 欢迎区 */
.welcome-section { position: relative; border-radius: 12px; overflow: hidden; margin-bottom: 16px; }
.welcome-bg { position: absolute; inset: 0; background: linear-gradient(135deg, var(--ip-primary) 0%, var(--ip-secondary) 50%, var(--ip-bright) 100%); z-index: 0; }
.welcome-content { position: relative; z-index: 1; padding: 28px 32px; display: flex; justify-content: space-between; align-items: center; color: #fff; }
.welcome-left { flex: 1; }
.greeting { display: flex; align-items: center; gap: 6px; margin-bottom: 8px; .greeting-icon { font-size: 20px; color: #FAAD14; } .greeting-text { font-size: 14px; color: rgba(255,255,255,0.7); } }
.welcome-title { margin: 0 0 8px; font-size: 26px; font-weight: 700; letter-spacing: 2px; background: linear-gradient(90deg, #fff, var(--ip-light-bg, #B2D4FF)); -webkit-background-clip: text; -webkit-text-fill-color: transparent; }
.welcome-desc { font-size: 14px; color: rgba(255,255,255,0.6); margin-bottom: 18px; }
.welcome-actions { display: flex; gap: 10px; }
.welcome-stats { display: flex; gap: 24px; }
.ws-item { text-align: center; .ws-value { font-size: 18px; font-weight: 700; } .ws-label { font-size: 11px; color: rgba(255,255,255,0.6); margin-top: 4px; } }

/* KPI */
.kpi-section { display: grid; grid-template-columns: repeat(6, 1fr); gap: 14px; margin-bottom: 16px; }
.kpi-card { background: #fff; border-radius: 10px; padding: 16px; box-shadow: 0 1px 6px rgba(0,0,0,.06); transition: all 0.2s;
  &:hover { transform: translateY(-2px); box-shadow: 0 6px 16px rgba(0,0,0,.1); }
  &.kpi-warning { border-top: 3px solid #FA8C16; } &.kpi-danger { border-top: 3px solid #FF4D4F; }
  .kpi-top { display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px; }
  .kpi-icon-wrap { width: 36px; height: 36px; border-radius: 8px; display: flex; align-items: center; justify-content: center; i { font-size: 18px; } }
  .kpi-trend { font-size: 12px; &.trend-up { color: #52C41A; } &.trend-down { color: #FF4D4F; } }
  .kpi-value { font-size: 24px; font-weight: 700; color: var(--ip-primary); } .kpi-unit { font-size: 13px; font-weight: 400; margin-left: 2px; color: #999; }
  .kpi-label { font-size: 12px; color: #888; margin: 4px 0 8px; } .kpi-bar { height: 4px; border-radius: 2px; overflow: hidden; } .kpi-bar-fill { height: 100%; border-radius: 2px; transition: width 0.6s ease; }
}

/* 全链路流程图（拓扑图） */
.lifecycle-section { background: #fff; border-radius: 10px; padding: 18px 20px; margin-bottom: 16px; box-shadow: 0 1px 6px rgba(0,0,0,.06); }
.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px; }
.section-title { font-size: 14px; font-weight: 600; color: var(--ip-primary); i { margin-right: 6px; } }
.section-legend { font-size: 12px; color: #888; span { margin-left: 14px; } .dot { display: inline-block; width: 8px; height: 8px; border-radius: 50%; margin-right: 4px; vertical-align: middle; &.blue { background: var(--ip-primary, #1890FF); } &.green { background: #52C41A; } &.orange { background: #FA8C16; } &.cyan { background: #13C2C2; } &.gray { background: #bbb; } } }

.flow-canvas-wrap {
  width: 100%; overflow-x: auto; overflow-y: hidden; padding: 6px;
  background: linear-gradient(90deg, rgba(24,144,255,0.04) 1px, transparent 1px) 0 0 / 24px 24px, linear-gradient(0deg, rgba(24,144,255,0.04) 1px, transparent 1px) 0 0 / 24px 24px, #fafbff;
  border-radius: 8px; border: 1px solid #f0f2f5;
}
.flow-canvas { position: relative; margin: 0 auto; }
.flow-svg { position: absolute; top: 0; left: 0; pointer-events: none; z-index: 1; }

/* 边线样式 */
.fl-edge { stroke-width: 1.6; fill: none;
  &.edge-main { stroke: var(--ip-primary, #1890FF); stroke-width: 2.2; }
  &.edge-sub { stroke: #b8b8b8; stroke-dasharray: 4 3; }
  &.edge-finance { stroke: #13C2C2; stroke-dasharray: 4 3; }
  &.edge-warn { stroke: #FA8C16; stroke-dasharray: 4 3; }
}

/* 节点样式 */
.flow-pos-node {
  position: absolute; z-index: 2; display: flex; flex-direction: column; align-items: center; justify-content: center;
  background: #fff; border: 1.5px solid #e8e8e8; border-radius: 10px; cursor: pointer; transition: all 0.2s; box-shadow: 0 1px 4px rgba(0,0,0,.04);
  &:hover { transform: translateY(-2px) scale(1.05); box-shadow: 0 6px 18px rgba(0,0,0,.12); z-index: 3; }
  .fpn-icon-wrap { width: 26px; height: 26px; border-radius: 6px; display: flex; align-items: center; justify-content: center; margin-bottom: 2px; i { font-size: 14px; } }
  .fpn-label { font-size: 11px; font-weight: 600; color: #333; white-space: nowrap; line-height: 1.2; }
  .fpn-dot { position: absolute; top: -3px; right: -3px; width: 9px; height: 9px; border-radius: 50%; background: #FF4D4F; animation: pulse 1.5s infinite; }
  .fpn-badge { position: absolute; top: -7px; right: -7px; min-width: 18px; height: 16px; padding: 0 4px; border-radius: 8px; background: #FF4D4F; color: #fff; font-size: 10px; font-weight: 700; line-height: 16px; text-align: center; box-shadow: 0 1px 3px rgba(255,77,79,.4); }
  &.fpn-main { border-color: var(--ip-primary, #1890FF); border-width: 2px; .fpn-icon-wrap { background: rgba(var(--ip-primary-rgb,24,144,255),0.12); } .fpn-icon-wrap i { color: var(--ip-primary, #1890FF); } .fpn-label { color: var(--ip-primary, #1890FF); } }
  &.fpn-sub { border-color: #d9d9d9; .fpn-icon-wrap { background: #f0f5ff; } .fpn-icon-wrap i { color: #5b8def; } }
  &.fpn-warn { border-color: #FA8C16; border-width: 2px; background: #FFFBE6; .fpn-icon-wrap { background: rgba(250,140,22,0.15); } .fpn-icon-wrap i { color: #FA8C16; } .fpn-label { color: #FA8C16; } }
  &.fpn-finance { border-color: #13C2C2; background: #f0fafa; .fpn-icon-wrap { background: rgba(19,194,194,0.15); } .fpn-icon-wrap i { color: #13C2C2; } .fpn-label { color: #08979C; } }
  &.fpn-config { border-color: #e8e8e8; background: #fafafa; border-style: dashed; .fpn-icon-wrap { background: #f0f0f0; } .fpn-icon-wrap i { color: #888; } .fpn-label { color: #666; font-weight: 500; } }
  &.fpn-alert { animation: nodeAlert 2s infinite; }
}
@keyframes pulse { 0% { box-shadow: 0 0 0 0 rgba(255,77,79,0.5); } 70% { box-shadow: 0 0 0 6px rgba(255,77,79,0); } 100% { box-shadow: 0 0 0 0 rgba(255,77,79,0); } }
@keyframes nodeAlert { 0%, 100% { box-shadow: 0 1px 4px rgba(0,0,0,.04); } 50% { box-shadow: 0 0 0 4px rgba(255,77,79,0.15), 0 1px 4px rgba(0,0,0,.04); } }

/* 通用卡片 */
.content-row { margin-bottom: 16px; }
.card-panel { background: #fff; border-radius: 10px; padding: 16px 18px; box-shadow: 0 1px 6px rgba(0,0,0,.06); height: 100%; }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; padding-bottom: 10px; border-bottom: 1px solid #f0f0f0; }
.card-title { font-size: 15px; font-weight: 600; color: var(--ip-primary); i { margin-right: 6px; } }
.card-more { font-size: 12px; color: var(--ip-bright); cursor: pointer; &:hover { opacity: 0.8; } }

/* tabs */
.inner-tabs { ::v-deep .el-tabs__header { margin-bottom: 8px; } ::v-deep .el-tabs__item { font-size: 13px; padding: 0 12px; } }
.inner-tabs-sm { ::v-deep .el-tabs__header { margin-bottom: 6px; } ::v-deep .el-tabs__item { font-size: 12px; padding: 0 8px; } }

/* feature列表 */
.feature-list { display: flex; flex-direction: column; gap: 5px; }
.feature-entry { display: flex; align-items: center; gap: 8px; padding: 7px 10px; border-radius: 8px; cursor: pointer; transition: background 0.15s;
  &:hover { background: #f5f7fa; }
  .fe-icon { width: 28px; height: 28px; border-radius: 6px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 13px; } }
  .fe-body { flex: 1; .fe-name { font-size: 13px; color: #333; font-weight: 500; display: block; } .fe-desc { font-size: 11px; color: #999; } }
  .fe-arrow { font-size: 12px; color: #d9d9d9; flex-shrink: 0; }
}

/* 统计分析入口 */
.stats-entry-list { display: flex; flex-direction: column; gap: 5px; }
.stats-entry { display: flex; align-items: center; gap: 8px; padding: 7px 10px; border-radius: 8px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.15s;
  &:hover { border-color: var(--ip-primary); background: var(--ip-light-bg, #fafbff); }
  .se-icon { width: 28px; height: 28px; border-radius: 6px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 13px; } }
  .se-body { flex: 1; .se-name { font-size: 13px; font-weight: 500; color: #333; } .se-desc { font-size: 11px; color: #999; } }
  .se-arrow { font-size: 12px; color: #d9d9d9; flex-shrink: 0; }
}

/* 账套 */
.ledger-list { display: grid; grid-template-columns: repeat(2, 1fr); gap: 4px; }
.ledger-item { display: flex; align-items: center; gap: 5px; padding: 5px 6px; font-size: 12px; cursor: pointer; border-radius: 6px; transition: background 0.15s;
  &:hover { background: #f0f5ff; color: var(--ip-primary); } i { font-size: 12px; } span { color: #333; }
}

/* 子模块 */
.sub-list { display: flex; flex-direction: column; gap: 5px; }
.sub-entry { display: flex; align-items: center; gap: 8px; padding: 7px 10px; border-radius: 8px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.15s;
  &:hover { border-color: var(--ip-primary); background: var(--ip-light-bg, #fafbff); }
  .sub-icon { width: 28px; height: 28px; border-radius: 6px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 13px; } }
  .sub-body { flex: 1; .sub-name { font-size: 13px; font-weight: 500; color: #333; } .sub-desc { font-size: 11px; color: #999; } }
}

/* 模块全景 */
.module-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px; }
.module-card { display: flex; flex-direction: column; align-items: center; padding: 14px 6px; border-radius: 10px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.2s; text-align: center;
  &:hover { border-color: var(--ip-primary); transform: translateY(-2px); box-shadow: 0 6px 16px rgba(var(--ip-primary-rgb),0.1); }
  .mc-icon { width: 36px; height: 36px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-bottom: 6px; i { font-size: 18px; } }
  .mc-name { font-size: 12px; font-weight: 600; color: #333; } .mc-desc { font-size: 10px; color: #bbb; margin-top: 2px; }
}

/* AI */
.ai-panel { background: linear-gradient(180deg, var(--ip-light-bg, #f0f5ff) 0%, #fff 100%); }
.ai-grid { display: flex; flex-direction: column; gap: 7px; margin-bottom: 10px; }
.ai-card { display: flex; align-items: center; gap: 10px; padding: 10px 12px; border-radius: 10px; border: 1px solid #e8e8e8; cursor: pointer; transition: all 0.2s;
  &:hover { border-color: var(--ip-bright); box-shadow: 0 4px 12px rgba(var(--ip-primary-rgb),0.12); transform: translateY(-1px); }
  .aif-icon { width: 34px; height: 34px; border-radius: 8px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 16px; } }
  .aif-body { flex: 1; .aif-name { font-size: 13px; font-weight: 600; color: #333; } .aif-desc { font-size: 11px; color: #999; } }
  .aif-arrow { font-size: 14px; color: #d9d9d9; }
}
.quick-divider { font-size: 12px; color: #bbb; margin: 8px 0 6px; padding-top: 8px; border-top: 1px solid #f0f0f0; }
.quick-row { display: grid; grid-template-columns: repeat(3, 1fr); gap: 6px; }
.quick-btn { display: flex; flex-direction: column; align-items: center; padding: 8px 4px; border-radius: 8px; border: 1px solid #f0f0f0; cursor: pointer; font-size: 11px; transition: all 0.15s;
  &:hover { border-color: var(--ip-primary); background: var(--ip-light-bg, #f0f5ff); }
  i { font-size: 16px; margin-bottom: 3px; } span { color: #555; }
}

/* 集成/高级功能 */
.intg-grid { display: grid; grid-template-columns: repeat(5, 1fr); gap: 8px; }
.intg-card { display: flex; flex-direction: column; align-items: center; padding: 12px 6px; border-radius: 8px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.2s;
  &:hover { border-color: var(--ip-primary); transform: translateY(-2px); }
  .ic-icon { width: 32px; height: 32px; border-radius: 7px; display: flex; align-items: center; justify-content: center; margin-bottom: 5px; i { font-size: 14px; } }
  .ic-name { font-size: 11px; color: #333; font-weight: 500; text-align: center; }
}
</style>
