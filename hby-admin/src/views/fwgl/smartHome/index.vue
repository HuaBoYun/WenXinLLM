<template>
  <div class="smart-legal-home" :style="themeVars">
    <!-- ========== 顶部欢迎区 ========== -->
    <div class="welcome-section">
      <div class="welcome-bg"></div>
      <div class="welcome-content">
        <div class="welcome-left">
          <div class="greeting">
            <span class="greeting-icon"><i class="el-icon-sunny" /></span>
            <span class="greeting-text">{{ greetingWord }}，{{ userName }}</span>
          </div>
          <h1 class="welcome-title">智慧法务监管平台</h1>
          <p class="welcome-desc">纠纷管理 · 法律审核 · 法律服务 · 普法培训 · AI法务审查</p>
          <div class="welcome-actions">
            <el-button type="primary" icon="el-icon-warning-outline" round @click="$router.push('/home/jf')">纠纷首页</el-button>
            <el-button icon="el-icon-cpu" round @click="$router.push('/home/index')">AI 法务助手</el-button>
            <el-button icon="el-icon-data-analysis" round @click="$router.push('/home/cwzl')">法务总览</el-button>
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

    <!-- ========== 法务全生命周期链路（拓扑图） ========== -->
    <div class="lifecycle-section">
      <div class="section-header">
        <div class="section-title"><i class="el-icon-share" /> 法务全生命周期链路</div>
        <div class="section-legend">
          <span><i class="dot blue" />主流程</span>
          <span><i class="dot orange" />关注</span>
          <span><i class="dot red" />纠纷/预警</span>
          <span><i class="dot cyan" />法律服务</span>
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
              <marker id="fl-arrow-service" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="7" markerHeight="7" orient="auto">
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

    <!-- ========== 中部：纠纷管理 + 法律审核 + 法律服务 ========== -->
    <el-row :gutter="14" class="content-row">
      <!-- 纠纷管理 -->
      <el-col :span="10">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-warning-outline" /> 纠纷管理</span>
            <span class="card-more" @click="jumpTo('/home/jf')">纠纷首页 →</span>
          </div>
          <el-tabs v-model="disputeActive" class="inner-tabs">
            <el-tab-pane name="register">
              <span slot="label">纠纷登记</span>
              <div class="dispute-flow-mini">
                <div v-for="step in disputeSteps" :key="step.name" class="df-step" @click="jumpTo(step.route)">
                  <div class="df-dot" :style="{background: step.color}"></div>
                  <div class="df-info">
                    <span class="df-name">{{ step.name }}</span>
                    <span class="df-count" v-if="step.count">{{ step.count }}件</span>
                  </div>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane name="stats">
              <span slot="label">统计分析</span>
              <div class="dispute-stats-list">
                <div v-for="ds in disputeStats" :key="ds.label" class="ds-item">
                  <div class="ds-dot" :style="{background: ds.color}"></div>
                  <span class="ds-label">{{ ds.label }}</span>
                  <span class="ds-val">{{ ds.value }}</span>
                  <div class="ds-bar-bg"><div class="ds-bar-fill" :style="{width: ds.percent + '%', background: ds.color}"></div></div>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
          <div class="dispute-bottom">
            <div class="db-item" v-for="db in disputeBottom" :key="db.name" @click="jumpTo(db.route)">
              <i :class="db.icon" :style="{color: db.iconColor}" />
              <span>{{ db.name }}</span>
            </div>
          </div>
        </div>
      </el-col>

      <!-- 法律审核 -->
      <el-col :span="7">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-document-checked" /> 法律审核</span>
            <span class="card-more" @click="jumpTo('/flsh/shtz')">审核台账 →</span>
          </div>
          <div class="audit-list">
            <div v-for="a in auditItems" :key="a.name" class="audit-item" @click="jumpTo(a.route)">
              <div class="ai-icon" :style="{background: a.bgColor}">
                <i :class="a.icon" :style="{color: a.iconColor}" />
              </div>
              <div class="ai-body">
                <div class="ai-name">{{ a.name }}</div>
                <div class="ai-desc">{{ a.desc }}</div>
              </div>
              <el-badge v-if="a.badge" :value="a.badge" class="ai-badge" />
            </div>
          </div>
          <div class="audit-progress">
            <div class="ap-row" v-for="ap in auditProgress" :key="ap.label">
              <span class="ap-label">{{ ap.label }}</span>
              <div class="ap-bar-bg"><div class="ap-bar-fill" :style="{width: ap.percent + '%', background: ap.color}" /></div>
              <span class="ap-val">{{ ap.percent }}%</span>
            </div>
          </div>
        </div>
      </el-col>

      <!-- 法律服务与组织 -->
      <el-col :span="7">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-service" /> 法律服务与组织</span>
            <span class="card-more" @click="jumpTo('/flfw/cnflfw')">法律服务 →</span>
          </div>
          <div class="service-tabs">
            <div v-for="st in serviceTabs" :key="st.key" class="st-tab"
              :class="{active: serviceActive === st.key}" @click="serviceActive = st.key">
              {{ st.name }}
            </div>
          </div>
          <div v-if="serviceActive === 'service'" class="entry-list">
            <div v-for="sv in legalServiceItems" :key="sv.name" class="entry-item" @click="jumpTo(sv.route)">
              <div class="ei-icon" :style="{background: sv.bgColor}">
                <i :class="sv.icon" :style="{color: sv.iconColor}" />
              </div>
              <div class="ei-info">
                <div class="ei-name">{{ sv.name }}</div>
                <div class="ei-desc">{{ sv.desc }}</div>
              </div>
              <i class="el-icon-arrow-right ei-arrow" />
            </div>
          </div>
          <div v-if="serviceActive === 'org'" class="entry-list">
            <div v-for="org in orgItems" :key="org.name" class="entry-item" @click="jumpTo(org.route)">
              <div class="ei-icon" :style="{background: org.bgColor}">
                <i :class="org.icon" :style="{color: org.iconColor}" />
              </div>
              <div class="ei-info">
                <div class="ei-name">{{ org.name }}</div>
                <div class="ei-desc">{{ org.desc }}</div>
              </div>
              <i class="el-icon-arrow-right ei-arrow" />
            </div>
          </div>
          <div v-if="serviceActive === 'assess'" class="entry-list">
            <div v-for="as in assessItems" :key="as.name" class="entry-item" @click="jumpTo(as.route)">
              <div class="ei-icon" :style="{background: as.bgColor}">
                <i :class="as.icon" :style="{color: as.iconColor}" />
              </div>
              <div class="ei-info">
                <div class="ei-name">{{ as.name }}</div>
                <div class="ei-desc">{{ as.desc }}</div>
              </div>
              <i class="el-icon-arrow-right ei-arrow" />
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- ========== 普法培训 + 学法考试 + 计划考核 ========== -->
    <el-row :gutter="14" class="content-row">
      <el-col :span="8">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-reading" /> 普法培训</span>
          </div>
          <div class="train-grid">
            <div v-for="t in trainingItems" :key="t.name" class="train-card" @click="jumpTo(t.route)">
              <div class="tc-icon" :style="{background: t.iconBg}">
                <i :class="t.icon" :style="{color: t.iconColor}" />
              </div>
              <div class="tc-name">{{ t.name }}</div>
              <el-badge v-if="t.badge" :value="t.badge" class="tc-badge" />
            </div>
          </div>
        </div>
      </el-col>

      <el-col :span="8">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-edit-outline" /> 学法考试</span>
          </div>
          <div class="exam-grid">
            <div v-for="ex in examItems" :key="ex.name" class="exam-card" @click="jumpTo(ex.route)">
              <div class="ec-icon" :style="{background: ex.bgColor}">
                <i :class="ex.icon" :style="{color: ex.iconColor}" />
              </div>
              <div class="ec-body">
                <div class="ec-name">{{ ex.name }}</div>
                <div class="ec-desc">{{ ex.desc }}</div>
              </div>
            </div>
          </div>
        </div>
      </el-col>

      <el-col :span="8">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-date" /> 计划考核</span>
          </div>
          <div class="plan-list">
            <div v-for="p in planItems" :key="p.name" class="plan-item" @click="jumpTo(p.route)">
              <div class="pi-icon" :style="{background: p.bgColor}">
                <i :class="p.icon" :style="{color: p.iconColor}" />
              </div>
              <span class="pi-name">{{ p.name }}</span>
              <i class="el-icon-arrow-right pi-arrow" />
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- ========== 功能模块 + 知识产权与日常 ========== -->
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
            <span class="card-title"><i class="el-icon-medal" /> 知识产权与日常管理</span>
          </div>
          <div class="ip-daily-grid">
            <div class="ipd-section">
              <div class="ipd-title">知识产权</div>
              <div v-for="ip in ipItems" :key="ip.name" class="ipd-item" @click="jumpTo(ip.route)">
                <i :class="ip.icon" /><span>{{ ip.name }}</span>
              </div>
            </div>
            <div class="ipd-section">
              <div class="ipd-title">日常管理</div>
              <div v-for="dm in dailyItems" :key="dm.name" class="ipd-item" @click="jumpTo(dm.route)">
                <i :class="dm.icon" /><span>{{ dm.name }}</span>
              </div>
            </div>
            <div class="ipd-section">
              <div class="ipd-title">评分管理</div>
              <div v-for="sc in scoreItems" :key="sc.name" class="ipd-item" @click="jumpTo(sc.route)">
                <i :class="sc.icon" :style="{color: sc.iconColor}" /><span>{{ sc.name }}</span>
              </div>
            </div>
            <div class="ipd-section">
              <div class="ipd-title">公司律师</div>
              <div v-for="ls in lawyerItems" :key="ls.name" class="ipd-item" @click="jumpTo(ls.route)">
                <i :class="ls.icon" /><span>{{ ls.name }}</span>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- ========== AI法务 + 法务总览统计分析 ========== -->
    <el-row :gutter="14" class="content-row">
      <el-col :span="12">
        <div class="card-panel ai-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-cpu" /> AI 法务智能体</span>
            <span class="card-more" @click="jumpTo('/Ai/home')">AI首页 →</span>
          </div>
          <div class="ai-grid">
            <div v-for="ai in aiFeatures" :key="ai.name" class="ai-card" @click="jumpTo(ai.route)">
              <div class="aif-icon" :style="{background: ai.bgColor}">
                <i :class="ai.icon" :style="{color: ai.iconColor}" />
              </div>
              <div class="aif-body">
                <div class="aif-name">{{ ai.name }}</div>
                <div class="aif-desc">{{ ai.desc }}</div>
              </div>
              <i class="el-icon-arrow-right aif-arrow" />
            </div>
          </div>
        </div>
      </el-col>

      <el-col :span="12">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-data-analysis" /> 法务总览统计分析</span>
            <span class="card-more" @click="jumpTo('/home/cwzl')">总览 →</span>
          </div>
          <div class="stats-grid">
            <div v-for="st in statsTemplates" :key="st.name" class="stats-card" @click="jumpTo(st.route)">
              <div class="stc-icon" :style="{background: st.bgColor}">
                <i :class="st.icon" :style="{color: st.iconColor}" />
              </div>
              <div class="stc-name">{{ st.name }}</div>
            </div>
          </div>
          <div class="stats-quick">
            <div v-for="sq in statsQuickLinks" :key="sq.name" class="sq-item" @click="jumpTo(sq.route)">
              <i :class="sq.icon" /><span>{{ sq.name }}</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { investThemeMixin } from '@/views/stateAssets/themeMixin'
import { getSmartHomeDashboard } from '@/api/fwgl/smartHome'
import {
  getcaseInformationList,
  getnegotiatedSettlementInfoList,
  getlitigationSettlement,
  getArbitratSettlementInfoList,
  legalExecumgrList,
} from '@/api/fwgl/legal'

export default {
  name: 'SmartLegalHome',
  mixins: [investThemeMixin],
  data() {
    return {
      userName: '',
      disputeActive: 'register',
      serviceActive: 'service',
      welcomeStats: [
        { label: '纠纷总量', value: '--', color: '#FF4D4F' },
        { label: '审核完成率', value: '--', color: '#52C41A' },
        { label: '法律服务', value: '--', color: '#1890FF' },
        { label: '普法覆盖', value: '--', color: '#FA8C16' },
        { label: '知识产权', value: '--', color: '#722ED1' },
      ],
      kpiCards: [
        { label: '纠纷总量', value: '0', unit: '件', icon: 'el-icon-warning', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', barBg: 'rgba(255,77,79,0.1)', barColor: '#FF4D4F', barWidth: 0, trend: 0, theme: 'kpi-danger' },
        { label: '审核完成率', value: '0', unit: '%', icon: 'el-icon-circle-check', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', barBg: 'rgba(82,196,26,0.1)', barColor: '#52C41A', barWidth: 0, trend: 0, theme: '' },
        { label: '法律服务项', value: '0', unit: '项', icon: 'el-icon-service', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', barBg: 'rgba(24,144,255,0.1)', barColor: '#1890FF', barWidth: 0, trend: 0, theme: '' },
        { label: '普法覆盖率', value: '0', unit: '%', icon: 'el-icon-reading', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', barBg: 'rgba(250,140,22,0.1)', barColor: '#FA8C16', barWidth: 0, trend: 0, theme: 'kpi-warning' },
        { label: '知识产权', value: '0', unit: '件', icon: 'el-icon-medal', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', barBg: 'rgba(114,46,209,0.1)', barColor: '#722ED1', barWidth: 0, trend: 0, theme: '' },
        { label: '制度审核', value: '0', unit: '项', icon: 'el-icon-document-checked', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', barBg: 'rgba(19,194,194,0.1)', barColor: '#13C2C2', barWidth: 0, trend: 0, theme: '' },
      ],
      // ===== 流程图拓扑数据 =====
      flowConfig: { colW: 132, rowH: 96, nodeW: 96, nodeH: 60, padX: 20, padY: 16 },
      flowNodes: [
        // 主流程-纠纷管理（row 2）
        { id: 'dispute', name: '纠纷登记', col: 0, row: 2, icon: 'el-icon-edit', kind: 'legal', route: '/legal/dispute' },
        { id: 'consult', name: '协商过程', col: 1, row: 2, icon: 'el-icon-chat-dot-round', kind: 'legal', route: '/legal/consult' },
        { id: 'execute', name: '执行管理', col: 3, row: 2, icon: 'el-icon-s-operation', kind: 'legal', route: '/legal/manger' },
        { id: 'account', name: '纠纷台账', col: 4, row: 2, icon: 'el-icon-notebook-2', kind: 'legal', route: '/legal/account' },
        { id: 'classic', name: '典型案例库', col: 5, row: 2, icon: 'el-icon-collection', kind: 'legal', route: '/legal/classic' },
        { id: 'analysis', name: '统计分析', col: 6, row: 2, icon: 'el-icon-data-analysis', kind: 'legal', route: '/legal/analysis' },
        // 纠纷分支（row 3）
        { id: 'lawsuit', name: '诉讼过程', col: 1.5, row: 3, icon: 'el-icon-s-flag', kind: 'legal', route: '/legal/lawsuit' },
        { id: 'arbit',   name: '仲裁过程', col: 2.5, row: 3, icon: 'el-icon-medal', kind: 'legal', route: '/legal/arbitration' },
        // 法律审核（row 1）
        { id: 'zdsh', name: '制度审核', col: 0, row: 1, icon: 'el-icon-document-checked', kind: 'main', route: '/flsh/zdsh' },
        { id: 'jysh', name: '经营事项审核', col: 1, row: 1, icon: 'el-icon-s-operation', kind: 'main', route: '/flsh/jysxsh' },
        { id: 'shtz', name: '审核台账', col: 2, row: 1, icon: 'el-icon-notebook-2', kind: 'main', route: '/flsh/shtz' },
        // 法律服务（row 4）
        { id: 'cnfw', name: '常年法律服务', col: 0, row: 4, icon: 'el-icon-service', kind: 'service', route: '/flfw/cnflfw' },
        { id: 'zxfw', name: '专项法律服务', col: 1, row: 4, icon: 'el-icon-collection', kind: 'service', route: '/flfw/zxflfw' },
        { id: 'fwkh', name: '考核台账', col: 2, row: 4, icon: 'el-icon-s-order', kind: 'service', route: '/flfw/khtz' },
        // 组织信息（row 0）
        { id: 'flgw', name: '总法律顾问', col: 4, row: 0, icon: 'el-icon-s-custom', kind: 'sub', route: '/zzxx/flgw' },
        { id: 'fwry', name: '法务人员', col: 5, row: 0, icon: 'el-icon-user', kind: 'sub', route: '/zzxx/fwry' },
        { id: 'fwjg', name: '法务机构', col: 6, row: 0, icon: 'el-icon-office-building', kind: 'sub', route: '/zzxx/fwjgfzr' },
        // 普法培训（row 5）
        { id: 'pfjh', name: '普法计划', col: 3, row: 4, icon: 'el-icon-date', kind: 'warn', route: '/pfpx/pfjh' },
        { id: 'hdgl', name: '活动管理', col: 4, row: 4, icon: 'el-icon-s-flag', kind: 'warn', route: '/pfpx/hdgl' },
        { id: 'ktgl', name: '课题管理', col: 5, row: 4, icon: 'el-icon-notebook-1', kind: 'warn', route: '/pfpx/ktgl' },
        { id: 'zsk',  name: '知识库', col: 6, row: 4, icon: 'el-icon-reading', kind: 'warn', route: '/pfpx/xfks' },
        // 支撑层（row 5）
        { id: 'jhkh', name: '计划考核', col: 0, row: 5, icon: 'el-icon-date', kind: 'config', route: '/jhkh/ghgl' },
        { id: 'xfks', name: '学法考试', col: 1, row: 5, icon: 'el-icon-edit-outline', kind: 'config', route: '/xfks/zxks' },
        { id: 'pfgl', name: '评分管理', col: 2, row: 5, icon: 'el-icon-star-off', kind: 'config', route: '/pfgll/pfxgl' },
        { id: 'zscq', name: '知识产权', col: 3, row: 5, icon: 'el-icon-medal', kind: 'config', route: '/zscqgl/djgl' },
        { id: 'rcgl', name: '日常管理', col: 4, row: 5, icon: 'el-icon-s-order', kind: 'config', route: '/rcgl/hygl' },
        { id: 'gsls', name: '公司律师', col: 5, row: 5, icon: 'el-icon-s-claim', kind: 'config', route: '/gsls/zysq' },
      ],
      flowEdges: [
        // 纠纷主流程
        ['dispute','consult','legal'], ['consult','execute','legal'], ['execute','account','legal'],
        ['account','classic','legal'], ['classic','analysis','legal'],
        // 纠纷分支（协商 → 诉讼/仲裁）
        ['consult','lawsuit','legal-dash'], ['consult','arbit','legal-dash'],
        ['lawsuit','execute','legal-dash'], ['arbit','execute','legal-dash'],
        // 法律审核主链
        ['zdsh','jysh','main'], ['jysh','shtz','main'],
        // 法律服务链
        ['cnfw','zxfw','service'], ['zxfw','fwkh','service'],
        // 普法培训链
        ['pfjh','hdgl','warn'], ['hdgl','ktgl','warn'], ['ktgl','zsk','warn'],
        // 审核 → 纠纷（跨层关联）
        ['shtz','dispute','sub'],
        // 组织信息横向
        ['flgw','fwry','sub'], ['fwry','fwjg','sub'],
      ],
      disputeSteps: [
        { name: '纠纷登记', color: '#FF4D4F', count: '0', route: '/legal/dispute' },
        { name: '协商过程', color: '#FA8C16', count: '0', route: '/legal/consult' },
        { name: '诉讼过程', color: '#722ED1', count: '0', route: '/legal/lawsuit' },
        { name: '仲裁过程', color: '#1890FF', count: '0', route: '/legal/arbitration' },
        { name: '执行管理', color: '#52C41A', count: '0', route: '/legal/manger' },
      ],
      disputeStats: [
        { label: '协商中', value: '0件', color: '#FA8C16', percent: 0 },
        { label: '诉讼中', value: '0件', color: '#722ED1', percent: 0 },
        { label: '仲裁中', value: '0件', color: '#1890FF', percent: 0 },
        { label: '已结案', value: '0件', color: '#52C41A', percent: 0 },
      ],
      disputeBottom: [
        { name: '纠纷台账', icon: 'el-icon-document', iconColor: '#1890FF', route: '/legal/account' },
        { name: '典型案例库', icon: 'el-icon-collection', iconColor: '#722ED1', route: '/legal/classic' },
        { name: '统计分析', icon: 'el-icon-data-analysis', iconColor: '#52C41A', route: '/legal/analysis' },
      ],
      auditItems: [
        { name: '制度审核', desc: '规章制度法律审查', icon: 'el-icon-document-checked', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/flsh/zdsh' },
        { name: '经营事项审核', desc: '经营决策法律审核', icon: 'el-icon-s-operation', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/flsh/jysxsh' },
        { name: '审核台账', desc: '审核记录汇总台账', icon: 'el-icon-notebook-2', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/flsh/shtz' },
      ],
      auditProgress: [
        { label: '制度审核完成率', percent: 0, color: '#722ED1' },
        { label: '经营审核完成率', percent: 0, color: '#1890FF' },
        { label: '审核合规率', percent: 0, color: '#52C41A' },
      ],
      serviceTabs: [
        { name: '法律服务', key: 'service' },
        { name: '组织信息', key: 'org' },
        { name: '考核台账', key: 'assess' },
      ],
      legalServiceItems: [
        { name: '常年法律服务', desc: '长期法律顾问服务', icon: 'el-icon-service', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/flfw/cnflfw' },
        { name: '专项法律服务库', desc: '专项法律服务资源', icon: 'el-icon-collection', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/flfw/zxflfw' },
        { name: '考核台账', desc: '法律服务考核记录', icon: 'el-icon-s-order', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/flfw/khtz' },
      ],
      orgItems: [
        { name: '总法律顾问', desc: '集团总法律顾问管理', icon: 'el-icon-s-custom', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/zzxx/flgw' },
        { name: '法务人员', desc: '法务人员信息管理', icon: 'el-icon-user', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/zzxx/fwry' },
        { name: '法务机构及负责人', desc: '法务机构设置管理', icon: 'el-icon-office-building', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/zzxx/fwjgfzr' },
        { name: '公司律师', desc: '公司律师信息管理', icon: 'el-icon-s-claim', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/zzxx/gsls' },
      ],
      assessItems: [
        { name: '考核台账', desc: '考核记录汇总台账', icon: 'el-icon-s-order', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/jhkh/khtz' },
        { name: '规划管理', desc: '法务规划管理', icon: 'el-icon-s-data', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/jhkh/ghgl' },
        { name: '年度计划', desc: '年度工作计划', icon: 'el-icon-date', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/jhkh/ndjh' },
        { name: '年度考核', desc: '年度考核评价', icon: 'el-icon-star-off', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/jhkh/ndkh' },
      ],
      trainingItems: [
        { name: '普法计划', icon: 'el-icon-date', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', route: '/pfpx/pfjh' },
        { name: '活动管理', icon: 'el-icon-s-flag', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', route: '/pfpx/hdgl' },
        { name: '课题管理', icon: 'el-icon-notebook-1', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', route: '/pfpx/ktgl' },
        { name: '知识库', icon: 'el-icon-reading', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', route: '/pfpx/xfks' },
        { name: '领导学法', icon: 'el-icon-star-on', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', route: '/pfpx/ldxf' },
      ],
      examItems: [
        { name: '在线考试', desc: '法务在线考试', icon: 'el-icon-edit-outline', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/xfks/zxks' },
        { name: '我的成绩', desc: '个人考试成绩', icon: 'el-icon-s-mark', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/xfks/wdcj' },
        { name: '题库管理', desc: '题库资源管理', icon: 'el-icon-folder-opened', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/xfks/tkgl' },
        { name: '试题管理', desc: '试题编辑管理', icon: 'el-icon-edit', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/xfks/stgl' },
        { name: '考试管理', desc: '考试安排管理', icon: 'el-icon-s-order', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/xfks/ksgl' },
      ],
      planItems: [
        { name: '规划管理', icon: 'el-icon-s-data', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/jhkh/ghgl' },
        { name: '年度计划', icon: 'el-icon-date', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/jhkh/ndjh' },
        { name: '年度考核', icon: 'el-icon-star-off', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/jhkh/ndkh' },
        { name: '考核台账', icon: 'el-icon-s-order', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/jhkh/khtz' },
      ],
      modules: [
        { name: '纠纷管理', icon: 'el-icon-warning-outline', desc: '登记/协商/诉讼/仲裁', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', route: '/home/jf' },
        { name: '法律审核', icon: 'el-icon-document-checked', desc: '制度/经营/台账', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', route: '/flsh/shtz' },
        { name: '法律服务', icon: 'el-icon-service', desc: '常年/专项/考核', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', route: '/flfw/cnflfw' },
        { name: '组织信息', icon: 'el-icon-s-custom', desc: '顾问/人员/机构/律师', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', route: '/zzxx/flgw' },
        { name: '公司律师', icon: 'el-icon-s-claim', desc: '申请/台账/活动/考核', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', route: '/gsls/zysq' },
        { name: '普法培训', icon: 'el-icon-reading', desc: '计划/活动/课题/知识', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', route: '/pfpx/pfjh' },
        { name: '学法考试', icon: 'el-icon-edit-outline', desc: '考试/成绩/题库/管理', iconBg: 'rgba(235,47,150,0.1)', iconColor: '#EB2F96', route: '/xfks/zxks' },
        { name: '评分管理', icon: 'el-icon-star-off', desc: '评分项/重点/评分表', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', route: '/pfgll/pfxgl' },
        { name: '计划考核', icon: 'el-icon-date', desc: '规划/计划/考核/台账', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', route: '/jhkh/ghgl' },
        { name: '知识产权', icon: 'el-icon-medal', desc: '登记/台账管理', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', route: '/zscqgl/djgl' },
        { name: '日常管理', icon: 'el-icon-s-order', desc: '会议/文件/学习/通知', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', route: '/rcgl/hygl' },
        { name: '消息中心', icon: 'el-icon-bell', desc: '系统消息与通知', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', route: '/home/infoCenter' },
      ],
      ipItems: [
        { name: '登记管理', icon: 'el-icon-document-add', route: '/zscqgl/djgl' },
        { name: '台账管理', icon: 'el-icon-notebook-2', route: '/zscqgl/tzgl' },
      ],
      dailyItems: [
        { name: '会议管理', icon: 'el-icon-date', route: '/rcgl/hygl' },
        { name: '其他文件报送', icon: 'el-icon-document', route: '/rcgl/qtwjbs' },
        { name: '学习园地', icon: 'el-icon-reading', route: '/rcgl/Xxyd' },
        { name: '通知公告', icon: 'el-icon-bell', route: '/rcgl/Tzgg' },
      ],
      scoreItems: [
        { name: '评分项管理', icon: 'el-icon-s-grid', iconColor: '#FF4D4F', route: '/pfgll/pfxgl' },
        { name: '评分重点管理', icon: 'el-icon-star-off', iconColor: '#1890FF', route: '/pfgll/pfzdgl' },
        { name: '评分表管理', icon: 'el-icon-notebook-2', iconColor: '#52C41A', route: '/pfgll/pfbgl' },
      ],
      lawyerItems: [
        { name: '执业申请', icon: 'el-icon-document-add', route: '/gsls/zysq' },
        { name: '人员台账', icon: 'el-icon-user', route: '/gsls/rytz' },
        { name: '执业活动', icon: 'el-icon-s-operation', route: '/gsls/zyhd' },
        { name: '执业考核', icon: 'el-icon-star-off', route: '/gsls/zykh' },
      ],
      aiFeatures: [
        { name: 'AI法务首页', desc: 'AI法务智能入口', icon: 'el-icon-cpu', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/Ai/home' },
        { name: '法律大模型', desc: '法律AI大语言模型', icon: 'el-icon-magic-stick', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/Wxmx/index' },
        { name: '法务智能体', desc: '法务智能代理助手', icon: 'el-icon-connection', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/Ai/AIAgent' },
        { name: '法务总览分析', desc: '综合统计智能分析', icon: 'el-icon-data-analysis', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/home/cwzl' },
      ],
      statsTemplates: [
        { name: '法务总览', icon: 'el-icon-data-analysis', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/home/cwzl' },
        { name: '模版一·利润', icon: 'el-icon-data-line', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/home/cwzl' },
        { name: '模版二·收入', icon: 'el-icon-coin', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/home/cwzl' },
        { name: '模版三·成本', icon: 'el-icon-s-order', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/home/cwzl' },
        { name: '模版四·资产', icon: 'el-icon-s-finance', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/home/cwzl' },
        { name: '模版五·应收', icon: 'el-icon-s-claim', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/home/cwzl' },
      ],
      statsQuickLinks: [
        { name: '典型案例库', icon: 'el-icon-collection', route: '/legal/classic' },
        { name: '统计分析', icon: 'el-icon-data-analysis', route: '/legal/analysis' },
        { name: '消息中心', icon: 'el-icon-bell', route: '/home/infoCenter' },
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
        service: 'url(#fl-arrow-service)',
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
    this.fetchDashboardData()
  },
  methods: {
    /** 流程图节点定位 */
    nodePos(n) {
      const { colW, rowH, nodeW, nodeH, padX, padY } = this.flowConfig
      const left = padX + n.col * colW + (colW - nodeW) / 2
      const top = padY + n.row * rowH + (rowH - nodeH) / 2
      return { left: left + 'px', top: top + 'px', width: nodeW + 'px', height: nodeH + 'px' }
    },
    /** 获取首页仪表盘数据 */
    async fetchDashboardData() {
      // 并行请求：法务服务数据 + 纠纷数据（合同服务）
      const [dashRes, disputeData] = await Promise.all([
        this.fetchLegalDashboard(),
        this.fetchDisputeCounts(),
      ])
      if (dashRes) this.applyDashboardData(dashRes)
      if (disputeData) this.applyDisputeData(disputeData)
    },
    /** 法务服务仪表盘数据 */
    async fetchLegalDashboard() {
      try {
        const res = await getSmartHomeDashboard({})
        if (res && res.code === 200 && res.data) return res.data
      } catch (e) {
        console.error('获取法务仪表盘数据失败', e)
      }
      return null
    },
    /** 从合同服务获取纠纷各模块总数 */
    async fetchDisputeCounts() {
      const param = { pageNumber: 1, pageSize: 1 }
      try {
        const [dispute, consult, lawsuit, arbit, exec] = await Promise.all([
          getcaseInformationList(param).catch(() => null),
          getnegotiatedSettlementInfoList(param).catch(() => null),
          getlitigationSettlement(param).catch(() => null),
          getArbitratSettlementInfoList(param).catch(() => null),
          legalExecumgrList(param).catch(() => null),
        ])
        return {
          disputeCount: dispute && dispute.date ? dispute.date.totalRecord || 0 : 0,
          consultCount: consult && consult.date ? consult.date.totalRecord || 0 : 0,
          lawsuitCount: lawsuit && lawsuit.date ? lawsuit.date.totalRecord || 0 : 0,
          arbitCount: arbit && arbit.date ? arbit.date.totalRecord || 0 : 0,
          execCount: exec && exec.date ? exec.date.totalRecord || 0 : 0,
        }
      } catch (e) {
        console.error('获取纠纷数据失败', e)
        return null
      }
    },
    /** 将法务服务数据映射到页面 */
    applyDashboardData(data) {
      // 顶部统计（纠纷总量由 applyDisputeData 单独处理）
      this.welcomeStats[1].value = data.auditCompletionRate + '%'
      this.welcomeStats[2].value = data.legalServiceCount + '项'
      this.welcomeStats[3].value = data.popularizeLawRate + '%'
      this.welcomeStats[4].value = data.intellectualPropertyCount + '件'

      // KPI卡片
      this.kpiCards[1].value = String(data.auditCompletionRate)
      this.kpiCards[1].barWidth = data.auditCompletionRate
      this.kpiCards[1].trend = data.auditTrend || 0

      this.kpiCards[2].value = String(data.legalServiceCount)
      this.kpiCards[2].barWidth = Math.min(data.legalServiceCount * 2, 100)
      this.kpiCards[2].trend = data.serviceTrend || 0

      this.kpiCards[3].value = String(data.popularizeLawRate)
      this.kpiCards[3].barWidth = data.popularizeLawRate
      this.kpiCards[3].trend = data.popularizeTrend || 0

      this.kpiCards[4].value = String(data.intellectualPropertyCount)
      this.kpiCards[4].barWidth = Math.min(data.intellectualPropertyCount * 2, 100)
      this.kpiCards[4].trend = data.ipTrend || 0

      this.kpiCards[5].value = String(data.institutionAuditCount)
      this.kpiCards[5].barWidth = Math.min(data.institutionAuditCount, 100)
      this.kpiCards[5].trend = data.institutionTrend || 0

      // 审核进度
      this.auditProgress[0].percent = Math.round(data.institutionAuditRate || 0)
      this.auditProgress[1].percent = Math.round(data.operateAuditRate || 0)
      this.auditProgress[2].percent = Math.round(data.auditComplianceRate || 0)
    },
    /** 将纠纷数据映射到页面 */
    applyDisputeData(data) {
      const total = data.disputeCount + data.consultCount + data.lawsuitCount + data.arbitCount + data.execCount

      // 顶部统计 & KPI
      this.welcomeStats[0].value = total + '件'
      this.kpiCards[0].value = String(total)
      this.kpiCards[0].barWidth = Math.min(total, 100)

      // 纠纷各阶段
      this.disputeSteps[0].count = String(data.disputeCount)
      this.disputeSteps[1].count = String(data.consultCount)
      this.disputeSteps[2].count = String(data.lawsuitCount)
      this.disputeSteps[3].count = String(data.arbitCount)
      this.disputeSteps[4].count = String(data.execCount)

      // 纠纷统计
      const divisor = total || 1
      this.disputeStats[0].value = data.consultCount + '件'
      this.disputeStats[0].percent = Math.round(data.consultCount / divisor * 100)
      this.disputeStats[1].value = data.lawsuitCount + '件'
      this.disputeStats[1].percent = Math.round(data.lawsuitCount / divisor * 100)
      this.disputeStats[2].value = data.arbitCount + '件'
      this.disputeStats[2].percent = Math.round(data.arbitCount / divisor * 100)
      this.disputeStats[3].value = data.execCount + '件'
      this.disputeStats[3].percent = Math.round(data.execCount / divisor * 100)
    },
    jumpTo(route) {
      if (route) this.$router.push(route)
    },
    _patchPrimaryColors() {
      const bright = this.ipBright || '#1890FF'
      const rgb = this.ipPrimaryRgb || '24,144,255'
      const brightBg = `rgba(${rgb},0.1)`
      const brightBg2 = `rgba(${rgb},0.08)`
      if (this.welcomeStats && this.welcomeStats[2]) this.welcomeStats[2].color = bright
      if (this.kpiCards && this.kpiCards[2]) {
        this.kpiCards[2].iconBg = brightBg
        this.kpiCards[2].iconColor = bright
        this.kpiCards[2].barBg = brightBg
        this.kpiCards[2].barColor = bright
      }
      if (this.modules && this.modules[3]) {
        this.modules[3].iconBg = brightBg
        this.modules[3].iconColor = bright
      }
      if (this.aiFeatures && this.aiFeatures[0]) {
        this.aiFeatures[0].bgColor = brightBg2
        this.aiFeatures[0].iconColor = bright
      }
      if (this.statsTemplates && this.statsTemplates[0]) {
        this.statsTemplates[0].bgColor = brightBg2
        this.statsTemplates[0].iconColor = bright
      }
    },
  },
}
</script>

<style lang="scss" scoped>
.smart-legal-home {
  padding: 16px;
  background: #f0f2f5;
  min-height: 100vh;
}

/* ========== 欢迎区 ========== */
.welcome-section { position: relative; border-radius: 12px; overflow: hidden; margin-bottom: 16px; }
.welcome-bg {
  position: absolute; top: 0; left: 0; right: 0; bottom: 0;
  background: linear-gradient(135deg, var(--ip-primary) 0%, var(--ip-secondary) 50%, var(--ip-bright) 100%);
  z-index: 0;
}
.welcome-content { position: relative; z-index: 1; padding: 28px 32px; display: flex; justify-content: space-between; align-items: center; color: #fff; }
.welcome-left { flex: 1; }
.greeting { display: flex; align-items: center; gap: 6px; margin-bottom: 8px;
  .greeting-icon { font-size: 20px; color: #FAAD14; }
  .greeting-text { font-size: 14px; color: rgba(255,255,255,0.7); }
}
.welcome-title {
  margin: 0 0 8px; font-size: 26px; font-weight: 700; letter-spacing: 2px;
  background: linear-gradient(90deg, #fff, var(--ip-light-bg, #B2D4FF));
  -webkit-background-clip: text; -webkit-text-fill-color: transparent;
}
.welcome-desc { font-size: 14px; color: rgba(255,255,255,0.6); margin-bottom: 18px; }
.welcome-actions { display: flex; gap: 10px; }
.welcome-stats { display: flex; gap: 24px; }
.ws-item { text-align: center;
  .ws-value { font-size: 22px; font-weight: 700; }
  .ws-label { font-size: 12px; color: rgba(255,255,255,0.6); margin-top: 4px; }
}

/* ========== KPI ========== */
.kpi-section { display: grid; grid-template-columns: repeat(6, 1fr); gap: 14px; margin-bottom: 16px; }
.kpi-card {
  background: #fff; border-radius: 10px; padding: 16px; box-shadow: 0 1px 6px rgba(0,0,0,.06); transition: all 0.2s;
  &:hover { transform: translateY(-2px); box-shadow: 0 6px 16px rgba(0,0,0,.1); }
  &.kpi-warning { border-top: 3px solid #FA8C16; }
  &.kpi-danger { border-top: 3px solid #FF4D4F; }
  .kpi-top { display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px; }
  .kpi-icon-wrap { width: 36px; height: 36px; border-radius: 8px; display: flex; align-items: center; justify-content: center; i { font-size: 18px; } }
  .kpi-trend { font-size: 12px; &.trend-up { color: #52C41A; } &.trend-down { color: #FF4D4F; } }
  .kpi-value { font-size: 24px; font-weight: 700; color: var(--ip-primary); }
  .kpi-unit { font-size: 13px; font-weight: 400; margin-left: 2px; color: #999; }
  .kpi-label { font-size: 12px; color: #888; margin: 4px 0 8px; }
  .kpi-bar { height: 4px; border-radius: 2px; overflow: hidden; }
  .kpi-bar-fill { height: 100%; border-radius: 2px; transition: width 0.6s ease; }
}

/* ========== 全链路流程图（拓扑图） ========== */
.lifecycle-section { background: #fff; border-radius: 10px; padding: 18px 20px; margin-bottom: 16px; box-shadow: 0 1px 6px rgba(0,0,0,.06); }
.section-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px; }
.section-title { font-size: 14px; font-weight: 600; color: var(--ip-primary); i { margin-right: 6px; } }
.section-legend { font-size: 12px; color: #888; span { margin-left: 14px; }
  .dot { display: inline-block; width: 8px; height: 8px; border-radius: 50%; margin-right: 4px; vertical-align: middle;
    &.blue { background: var(--ip-primary, #1890FF); } &.green { background: #52C41A; }
    &.orange { background: #FA8C16; } &.red { background: #FF4D4F; }
    &.cyan { background: #13C2C2; } &.gray { background: #bbb; }
  }
}
.flow-canvas-wrap {
  width: 100%; overflow-x: auto; overflow-y: hidden; padding: 6px;
  background:
    linear-gradient(90deg, rgba(24,144,255,0.04) 1px, transparent 1px) 0 0 / 24px 24px,
    linear-gradient(0deg, rgba(24,144,255,0.04) 1px, transparent 1px) 0 0 / 24px 24px,
    #fafbff;
  border-radius: 8px; border: 1px solid #f0f2f5;
}
.flow-canvas { position: relative; margin: 0 auto; }
.flow-svg { position: absolute; top: 0; left: 0; pointer-events: none; z-index: 1; }
.fl-edge {
  stroke-width: 1.6; fill: none;
  &.edge-main { stroke: var(--ip-primary, #1890FF); stroke-width: 2.2; }
  &.edge-sub { stroke: #b8b8b8; stroke-dasharray: 4 3; }
  &.edge-service { stroke: #13C2C2; stroke-dasharray: 4 3; }
  &.edge-legal { stroke: #FF4D4F; stroke-width: 1.8; }
  &.edge-legal-dash { stroke: #FF7875; stroke-dasharray: 5 3; }
  &.edge-warn { stroke: #FA8C16; stroke-dasharray: 4 3; }
}
.flow-pos-node {
  position: absolute; z-index: 2; display: flex; flex-direction: column; align-items: center; justify-content: center;
  background: #fff; border: 1.5px solid #e8e8e8; border-radius: 10px; cursor: pointer; transition: all 0.2s;
  box-shadow: 0 1px 4px rgba(0,0,0,.04);
  &:hover { transform: translateY(-2px) scale(1.05); box-shadow: 0 6px 18px rgba(0,0,0,.12); z-index: 3; }
  .fpn-icon-wrap { width: 26px; height: 26px; border-radius: 6px; display: flex; align-items: center; justify-content: center; margin-bottom: 2px; i { font-size: 14px; } }
  .fpn-label { font-size: 11px; font-weight: 600; color: #333; white-space: nowrap; line-height: 1.2; }
  .fpn-dot { position: absolute; top: -3px; right: -3px; width: 9px; height: 9px; border-radius: 50%; background: #FF4D4F; animation: pulse 1.5s infinite; }
  .fpn-badge { position: absolute; top: -7px; right: -7px; min-width: 18px; height: 16px; padding: 0 4px; border-radius: 8px; background: #FF4D4F; color: #fff; font-size: 10px; font-weight: 700; line-height: 16px; text-align: center; box-shadow: 0 1px 3px rgba(255,77,79,.4); }
  &.fpn-main { border-color: var(--ip-primary, #1890FF); border-width: 2px; .fpn-icon-wrap { background: rgba(var(--ip-primary-rgb,24,144,255),0.12); } .fpn-icon-wrap i { color: var(--ip-primary, #1890FF); } .fpn-label { color: var(--ip-primary, #1890FF); } }
  &.fpn-sub { border-color: #d9d9d9; .fpn-icon-wrap { background: #f0f5ff; } .fpn-icon-wrap i { color: #5b8def; } }
  &.fpn-warn { border-color: #FA8C16; border-width: 2px; background: #FFFBE6; .fpn-icon-wrap { background: rgba(250,140,22,0.15); } .fpn-icon-wrap i { color: #FA8C16; } .fpn-label { color: #FA8C16; } }
  &.fpn-service { border-color: #13C2C2; background: #f0fafa; .fpn-icon-wrap { background: rgba(19,194,194,0.15); } .fpn-icon-wrap i { color: #13C2C2; } .fpn-label { color: #08979C; } }
  &.fpn-legal { border-color: #FF7875; background: #FFF1F0; .fpn-icon-wrap { background: rgba(255,77,79,0.12); } .fpn-icon-wrap i { color: #FF4D4F; } .fpn-label { color: #CF1322; } }
  &.fpn-config { border-color: #e8e8e8; background: #fafafa; border-style: dashed; .fpn-icon-wrap { background: #f0f0f0; } .fpn-icon-wrap i { color: #888; } .fpn-label { color: #666; font-weight: 500; } }
  &.fpn-alert { animation: nodeAlert 2s infinite; }
}
@keyframes pulse { 0% { box-shadow: 0 0 0 0 rgba(255,77,79,0.5); } 70% { box-shadow: 0 0 0 6px rgba(255,77,79,0); } 100% { box-shadow: 0 0 0 0 rgba(255,77,79,0); } }
@keyframes nodeAlert { 0%, 100% { box-shadow: 0 1px 4px rgba(0,0,0,.04); } 50% { box-shadow: 0 0 0 4px rgba(255,77,79,0.15), 0 1px 4px rgba(0,0,0,.04); } }

/* ========== 通用面板 ========== */
.content-row { margin-bottom: 16px; }
.card-panel { background: #fff; border-radius: 10px; padding: 16px 18px; box-shadow: 0 1px 6px rgba(0,0,0,.06); height: 100%; }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; padding-bottom: 10px; border-bottom: 1px solid #f0f0f0; }
.card-title { font-size: 15px; font-weight: 600; color: var(--ip-primary); i { margin-right: 6px; } }
.card-more { font-size: 12px; color: var(--ip-bright); cursor: pointer; &:hover { opacity: 0.8; } }

/* ========== 纠纷管理 ========== */
.inner-tabs { ::v-deep .el-tabs__header { margin-bottom: 8px; } ::v-deep .el-tabs__item { font-size: 13px; padding: 0 12px; } }
.dispute-flow-mini { display: flex; flex-direction: column; gap: 6px; }
.df-step {
  display: flex; align-items: center; gap: 10px; padding: 8px 12px; border-radius: 8px;
  cursor: pointer; transition: all 0.15s;
  &:hover { background: #f5f7fa; }
  .df-dot { width: 10px; height: 10px; border-radius: 50%; flex-shrink: 0; }
  .df-info { flex: 1; display: flex; justify-content: space-between; align-items: center; }
  .df-name { font-size: 13px; color: #333; font-weight: 500; }
  .df-count { font-size: 14px; font-weight: 700; color: var(--ip-primary); }
}
.dispute-stats-list { display: flex; flex-direction: column; gap: 8px; }
.ds-item {
  display: flex; align-items: center; gap: 8px;
  .ds-dot { width: 8px; height: 8px; border-radius: 50%; flex-shrink: 0; }
  .ds-label { font-size: 12px; color: #888; width: 56px; }
  .ds-val { font-size: 13px; font-weight: 600; color: #333; width: 50px; }
  .ds-bar-bg { flex: 1; height: 6px; background: #f5f5f5; border-radius: 3px; overflow: hidden; }
  .ds-bar-fill { height: 100%; border-radius: 3px; transition: width 0.4s; }
}
.dispute-bottom {
  display: flex; gap: 12px; margin-top: 12px; padding-top: 10px; border-top: 1px solid #f0f0f0;
  .db-item {
    display: flex; align-items: center; gap: 4px; font-size: 12px; color: #666; cursor: pointer;
    &:hover { color: var(--ip-primary); }
    i { font-size: 14px; }
  }
}

/* ========== 法律审核 ========== */
.audit-list { display: flex; flex-direction: column; gap: 8px; margin-bottom: 12px; }
.audit-item {
  display: flex; align-items: center; gap: 10px; padding: 10px 12px; border-radius: 8px;
  border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.15s;
  &:hover { border-color: var(--ip-primary); background: var(--ip-light-bg, #fafbff); }
  .ai-icon { width: 32px; height: 32px; border-radius: 8px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 14px; } }
  .ai-body { flex: 1; .ai-name { font-size: 13px; font-weight: 500; color: #333; } .ai-desc { font-size: 11px; color: #999; } }
  .ai-badge { ::v-deep .el-badge__content { top: 4px; } }
}
.audit-progress { margin-top: 4px; }
.ap-row { display: flex; align-items: center; gap: 8px; margin-bottom: 6px;
  .ap-label { font-size: 12px; color: #888; width: 100px; flex-shrink: 0; }
  .ap-bar-bg { flex: 1; height: 6px; background: #f5f5f5; border-radius: 3px; overflow: hidden; }
  .ap-bar-fill { height: 100%; border-radius: 3px; }
  .ap-val { font-size: 12px; font-weight: 600; color: #333; width: 40px; text-align: right; }
}

/* ========== 法律服务Tab ========== */
.service-tabs { display: flex; gap: 8px; margin-bottom: 12px; }
.st-tab {
  padding: 6px 14px; border-radius: 20px; font-size: 13px; cursor: pointer;
  background: #f5f5f5; color: #888; transition: all 0.2s;
  &.active { background: var(--ip-primary); color: #fff; }
  &:hover { opacity: 0.9; }
}
.entry-list { display: flex; flex-direction: column; gap: 6px; }
.entry-item {
  display: flex; align-items: center; gap: 10px; padding: 8px 10px; border-radius: 8px;
  border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.15s;
  &:hover { border-color: var(--ip-primary); background: var(--ip-light-bg, #fafbff); }
  .ei-icon { width: 30px; height: 30px; border-radius: 6px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 14px; } }
  .ei-info { flex: 1; .ei-name { font-size: 13px; font-weight: 500; color: #333; } .ei-desc { font-size: 11px; color: #999; } }
  .ei-arrow { font-size: 12px; color: #d9d9d9; flex-shrink: 0; }
}

/* ========== 普法培训 ========== */
.train-grid { display: grid; grid-template-columns: repeat(5, 1fr); gap: 10px; }
.train-card {
  display: flex; flex-direction: column; align-items: center; justify-content: center;
  padding: 14px 6px; border-radius: 10px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.2s; text-align: center;
  &:hover { border-color: var(--ip-primary); transform: translateY(-2px); box-shadow: 0 6px 16px rgba(var(--ip-primary-rgb),0.1); }
  position: relative;
  .tc-icon { width: 36px; height: 36px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-bottom: 6px; i { font-size: 18px; } }
  .tc-name { font-size: 12px; font-weight: 600; color: #333; }
  .tc-badge { position: absolute; top: 4px; right: 4px; }
}

/* ========== 学法考试 ========== */
.exam-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 8px; }
.exam-card {
  display: flex; align-items: center; gap: 8px; padding: 10px; border-radius: 8px;
  border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.15s;
  &:hover { border-color: #13C2C2; }
  .ec-icon { width: 28px; height: 28px; border-radius: 6px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 13px; } }
  .ec-body { .ec-name { font-size: 12px; font-weight: 500; color: #333; } .ec-desc { font-size: 10px; color: #999; } }
}

/* ========== 计划考核 ========== */
.plan-list { display: flex; flex-direction: column; gap: 6px; }
.plan-item {
  display: flex; align-items: center; gap: 10px; padding: 10px 14px; border-radius: 8px;
  border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.15s;
  &:hover { border-color: var(--ip-primary); background: var(--ip-light-bg, #fafbff); }
  .pi-icon { width: 30px; height: 30px; border-radius: 6px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 14px; } }
  .pi-name { flex: 1; font-size: 13px; font-weight: 500; color: #333; }
  .pi-arrow { font-size: 12px; color: #d9d9d9; }
}

/* ========== 功能模块 ========== */
.module-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px; }
.module-card {
  display: flex; flex-direction: column; align-items: center; justify-content: center;
  padding: 14px 6px; border-radius: 10px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.2s; text-align: center;
  &:hover { border-color: var(--ip-primary); transform: translateY(-2px); box-shadow: 0 6px 16px rgba(var(--ip-primary-rgb),0.1); }
  .mc-icon { width: 36px; height: 36px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-bottom: 6px; i { font-size: 18px; } }
  .mc-name { font-size: 12px; font-weight: 600; color: #333; }
  .mc-desc { font-size: 10px; color: #bbb; margin-top: 2px; }
}

/* ========== 知识产权与日常 ========== */
.ip-daily-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 10px; }
.ipd-section { padding: 10px; border-radius: 8px; border: 1px solid #f0f0f0; }
.ipd-title { font-size: 12px; font-weight: 600; color: var(--ip-primary); margin-bottom: 8px; padding-bottom: 4px; border-bottom: 1px solid #f0f0f0; }
.ipd-item {
  display: flex; align-items: center; gap: 6px; padding: 6px 4px; font-size: 12px; cursor: pointer; transition: color 0.15s;
  &:hover { color: var(--ip-primary); }
  i { font-size: 12px; color: #888; }
  span { color: #333; }
}

/* ========== AI法务 ========== */
.ai-panel { background: linear-gradient(180deg, var(--ip-light-bg, #f0f5ff) 0%, #fff 100%); }
.ai-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 10px; }
.ai-card {
  display: flex; align-items: center; gap: 10px; padding: 12px; border-radius: 10px;
  border: 1px solid #e8e8e8; cursor: pointer; transition: all 0.2s;
  &:hover { border-color: var(--ip-bright); box-shadow: 0 4px 12px rgba(var(--ip-primary-rgb),0.12); transform: translateY(-2px); }
  .aif-icon { width: 38px; height: 38px; border-radius: 10px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 18px; } }
  .aif-body { flex: 1; min-width: 0; }
  .aif-name { font-size: 13px; font-weight: 600; color: #333; }
  .aif-desc { font-size: 11px; color: #999; margin-top: 2px; }
  .aif-arrow { font-size: 14px; color: #d9d9d9; flex-shrink: 0; }
}

/* ========== 统计分析 ========== */
.stats-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 10px; margin-bottom: 12px; }
.stats-card {
  display: flex; flex-direction: column; align-items: center; padding: 14px 8px; border-radius: 10px;
  border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.2s;
  &:hover { border-color: var(--ip-primary); transform: translateY(-2px); }
  .stc-icon { width: 32px; height: 32px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-bottom: 6px; i { font-size: 14px; } }
  .stc-name { font-size: 12px; color: #333; font-weight: 500; text-align: center; }
}
.stats-quick { display: flex; gap: 8px; }
.sq-item {
  display: flex; align-items: center; gap: 4px; padding: 6px 10px; border-radius: 8px;
  background: #fafafa; cursor: pointer; transition: all 0.15s; font-size: 12px;
  &:hover { background: var(--ip-light-bg, #f0f5ff); }
  i { font-size: 12px; color: var(--ip-primary); }
}
</style>