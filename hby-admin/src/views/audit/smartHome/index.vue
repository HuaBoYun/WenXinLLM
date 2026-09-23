<template>
  <div class="smart-audit-home" :style="themeVars">
    <!-- ========== 顶部欢迎区 ========== -->
    <div class="welcome-section">
      <div class="welcome-bg"></div>
      <div class="welcome-content">
        <div class="welcome-left">
          <div class="greeting">
            <span class="greeting-icon"><i class="el-icon-sunny" /></span>
            <span class="greeting-text">{{ greetingWord }}，{{ userName }}</span>
          </div>
          <h1 class="welcome-title">智能审计监管平台</h1>
          <p class="welcome-desc">计划管理 · 项目实施 · 整改跟踪 · 审计档案 · AI智能审查 · 风险预警</p>
          <div class="welcome-actions">
            <el-button type="primary" icon="el-icon-s-order" round @click="$router.push('/project/planIndex')">计划管理</el-button>
            <el-button icon="el-icon-cpu" round @click="$router.push('/Ai/home')">AI 审计助手</el-button>
            <el-button icon="el-icon-data-analysis" round @click="$router.push('/home/homesjfx')">审计驾驶舱</el-button>
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

    <!-- ========== 审计全生命周期链路（拓扑图） ========== -->
    <div class="lifecycle-section">
      <div class="section-header">
        <div class="section-title"><i class="el-icon-share" /> 审计全生命周期链路</div>
        <div class="section-legend">
          <span><i class="dot blue" />主流程</span>
          <span><i class="dot orange" />关注</span>
          <span><i class="dot red" />预警/整改</span>
          <span><i class="dot cyan" />AI/模型</span>
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
              <marker id="fl-arrow-ai" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="7" markerHeight="7" orient="auto">
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

    <!-- ========== 中部：审计实施 + 审计分析 ========== -->
    <el-row :gutter="14" class="content-row">
      <!-- 审计实施 -->
      <el-col :span="14">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-s-operation" /> 审计实施</span>
            <span class="card-more" @click="jumpTo('/implement/task')">我的任务 →</span>
          </div>
          <el-tabs v-model="implActive" class="inner-tabs">
            <el-tab-pane name="task">
              <span slot="label">任务与底稿</span>
              <div class="impl-list">
                <div v-for="item in implTaskItems" :key="item.name" class="impl-entry" @click="jumpTo(item.route)">
                  <div class="ie-icon" :style="{background: item.bgColor}">
                    <i :class="item.icon" :style="{color: item.iconColor}" />
                  </div>
                  <div class="ie-body">
                    <div class="ie-name">{{ item.name }}</div>
                    <div class="ie-desc">{{ item.desc }}</div>
                  </div>
                  <el-badge v-if="item.badge" :value="item.badge" class="ie-badge" />
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane name="result">
              <span slot="label">审计发现</span>
              <div class="impl-list">
                <div v-for="item in implResultItems" :key="item.name" class="impl-entry" @click="jumpTo(item.route)">
                  <div class="ie-icon" :style="{background: item.bgColor}">
                    <i :class="item.icon" :style="{color: item.iconColor}" />
                  </div>
                  <div class="ie-body">
                    <div class="ie-name">{{ item.name }}</div>
                    <div class="ie-desc">{{ item.desc }}</div>
                  </div>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-col>

      <!-- 审计分析 -->
      <el-col :span="10">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-data-analysis" /> 审计分析</span>
            <span class="card-more" @click="jumpTo('/question/project')">项目分析 →</span>
          </div>
          <div class="analysis-grid">
            <div v-for="a in analysisItems" :key="a.name" class="analysis-card" @click="jumpTo(a.route)">
              <div class="alc-icon" :style="{background: a.bgColor}">
                <i :class="a.icon" :style="{color: a.iconColor}" />
              </div>
              <div class="alc-body">
                <div class="alc-name">{{ a.name }}</div>
                <div class="alc-desc">{{ a.desc }}</div>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- ========== 审计报告 + 审计档案 + AI审计 ========== -->
    <el-row :gutter="14" class="content-row">
      <el-col :span="8">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-document" /> 审计报告与成果</span>
            <span class="card-more" @click="jumpTo('/implement/index')">结果文书 →</span>
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

      <el-col :span="8">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-folder-opened" /> 审计档案</span>
            <span class="card-more" @click="jumpTo('/auditRecord/list')">档案列表 →</span>
          </div>
          <div class="archive-grid">
            <div v-for="a in archiveItems" :key="a.name" class="archive-card" @click="jumpTo(a.route)">
              <div class="ac-icon" :style="{background: a.bgColor}">
                <i :class="a.icon" :style="{color: a.iconColor}" />
              </div>
              <div class="ac-name">{{ a.name }}</div>
              <div class="ac-desc">{{ a.desc }}</div>
            </div>
          </div>
        </div>
      </el-col>

      <el-col :span="8">
        <div class="card-panel ai-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-cpu" /> AI 审计智能体</span>
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
    </el-row>

    <!-- ========== 功能模块 + 统计分析与预警 ========== -->
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
            <span class="card-title"><i class="el-icon-warning-outline" /> 预警管理与统计</span>
          </div>
          <div class="warn-grid">
            <div class="wg-section">
              <div class="wg-title">风险预警</div>
              <div v-for="w in warningItems" :key="w.name" class="wg-item" @click="jumpTo(w.route)">
                <i :class="w.icon" :style="{color: w.iconColor}" /><span>{{ w.name }}</span>
              </div>
            </div>
            <div class="wg-section">
              <div class="wg-title">审计统计</div>
              <div v-for="s in statItems" :key="s.name" class="wg-item" @click="jumpTo(s.route)">
                <i :class="s.icon" :style="{color: s.iconColor}" /><span>{{ s.name }}</span>
              </div>
            </div>
            <div class="wg-section">
              <div class="wg-title">统计分析模版</div>
              <div v-for="t in statTemplates" :key="t.name" class="wg-item" @click="jumpTo(t.route)">
                <i :class="t.icon" :style="{color: t.iconColor}" /><span>{{ t.name }}</span>
              </div>
            </div>
            <div class="wg-section">
              <div class="wg-title">基础配置</div>
              <div v-for="b in baseConfigItems" :key="b.name" class="wg-item" @click="jumpTo(b.route)">
                <i :class="b.icon" /><span>{{ b.name }}</span>
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
import {
  getAuditHomeKpiData,
  getAuditLifecycleData,
  getAuditRectifyProgress,
  getAuditRiskWarningList,
} from '@/api/audit/smartHome'

export default {
  name: 'SmartAuditHome',
  mixins: [investThemeMixin],
  data() {
    return {
      userName: '',
      implActive: 'task',
      welcomeStats: [
        { label: '实施项目', value: '-', color: '#1890FF' },
        { label: '整改完成率', value: '-', color: '#52C41A' },
        { label: '审计发现', value: '-', color: '#FA8C16' },
        { label: '审计报告', value: '-', color: '#722ED1' },
        { label: '风险预警', value: '-', color: '#FF4D4F' },
      ],
      kpiCards: [
        { label: '实施项目数', value: '-', unit: '个', icon: 'el-icon-s-order', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', barBg: 'rgba(24,144,255,0.1)', barColor: '#1890FF', barWidth: 0, trend: 0, theme: '' },
        { label: '整改完成率', value: '-', unit: '%', icon: 'el-icon-circle-check', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', barBg: 'rgba(82,196,26,0.1)', barColor: '#52C41A', barWidth: 0, trend: 0, theme: '' },
        { label: '审计发现', value: '-', unit: '项', icon: 'el-icon-warning-outline', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', barBg: 'rgba(250,140,22,0.1)', barColor: '#FA8C16', barWidth: 0, trend: 0, theme: 'kpi-warning' },
        { label: '审计报告', value: '-', unit: '份', icon: 'el-icon-document', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', barBg: 'rgba(114,46,209,0.1)', barColor: '#722ED1', barWidth: 0, trend: 0, theme: '' },
        { label: '未销号问题', value: '-', unit: '项', icon: 'el-icon-close', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', barBg: 'rgba(255,77,79,0.1)', barColor: '#FF4D4F', barWidth: 0, trend: 0, theme: 'kpi-danger' },
        { label: '底稿完成率', value: '-', unit: '%', icon: 'el-icon-notebook-2', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', barBg: 'rgba(19,194,194,0.1)', barColor: '#13C2C2', barWidth: 0, trend: 0, theme: '' },
      ],
      // ===== 流程图拓扑数据 =====
      flowConfig: { colW: 126, rowH: 90, nodeW: 92, nodeH: 58, padX: 16, padY: 14 },
      flowNodes: [
        // 主干流程（row 2）
        { id: 'plan', name: '计划管理', col: 0, row: 2, icon: 'el-icon-date', kind: 'main', route: '/project/planIndex' },
        { id: 'project', name: '项目管理', col: 1, row: 2, icon: 'el-icon-s-order', kind: 'main', route: '/project/index' },
        { id: 'prepare', name: '审计准备', col: 2, row: 2, icon: 'el-icon-s-flag', kind: 'warn', route: '/implement/notice' },
        { id: 'implement', name: '审计实施', col: 3, row: 2, icon: 'el-icon-s-operation', kind: 'main', route: '/implement/task', badge: '' },
        { id: 'discover', name: '审计发现', col: 4, row: 2, icon: 'el-icon-warning-outline', kind: 'main', route: '/implement/discover' },
        { id: 'report', name: '审计报告', col: 5, row: 2, icon: 'el-icon-document', kind: 'main', route: '/implement/index' },
        { id: 'archive', name: '成果归档', col: 6, row: 2, icon: 'el-icon-folder-opened', kind: 'main', route: '/implement/package' },

        // 审计准备/实施分支（row 1）
        { id: 'notice', name: '审计通知', col: 1.5, row: 1, icon: 'el-icon-message', kind: 'sub', route: '/implement/notice' },
        { id: 'guide', name: '任务清单', col: 2.5, row: 1, icon: 'el-icon-s-claim', kind: 'sub', route: '/implement/guide' },
        { id: 'myDraft', name: '我的底稿', col: 3.5, row: 1, icon: 'el-icon-notebook-2', kind: 'sub', route: '/implement/myDraft' },
        { id: 'draftMgr', name: '底稿管理', col: 4.5, row: 1, icon: 'el-icon-document', kind: 'sub', route: '/implement/implement/draftManage' },
        { id: 'doubtful', name: '疑点管理', col: 5.5, row: 1, icon: 'el-icon-question', kind: 'sub', route: '/implement/doubtful' },
        { id: 'evidence', name: '审计取证单', col: 6.5, row: 1, icon: 'el-icon-camera', kind: 'sub', route: '/implement/evidence' },

        // AI/模型层（row 0）
        { id: 'aiHome', name: 'AI审计', col: 2, row: 0, icon: 'el-icon-cpu', kind: 'ai', route: '/Ai/home' },
        { id: 'aiCompare', name: '智能对比', col: 3, row: 0, icon: 'el-icon-sort', kind: 'ai', route: '/tool/compare' },
        { id: 'aiReview', name: '智能审查', col: 4, row: 0, icon: 'el-icon-search', kind: 'ai', route: '/tool/review' },
        { id: 'model', name: '审计模型库', col: 5, row: 0, icon: 'el-icon-s-data', kind: 'ai', route: '/auditSjfx/sjmxk' },

        // 成果/档案/分析层（row 3）
        { id: 'archList', name: '档案列表', col: 0, row: 3, icon: 'el-icon-folder', kind: 'config', route: '/auditRecord/list' },
        { id: 'archRead', name: '档案借阅', col: 1, row: 3, icon: 'el-icon-reading', kind: 'config', route: '/auditRecord/read' },
        { id: 'analyse', name: '审计分析', col: 3, row: 3, icon: 'el-icon-data-analysis', kind: 'config', route: '/question/project' },
        { id: 'cockpit', name: '审计驾驶舱', col: 5, row: 3, icon: 'el-icon-data-board', kind: 'config', route: '/home/homesjfx' },
        { id: 'overview', name: '审计总览', col: 6, row: 3, icon: 'el-icon-pie-chart', kind: 'config', route: '/home/cwzl' },
      ],
      flowEdges: [
        // 主干（实线主流程）
        ['plan', 'project', 'main'], ['project', 'prepare', 'main'], ['prepare', 'implement', 'main'],
        ['implement', 'discover', 'main'], ['discover', 'report', 'main'],
        ['report', 'archive', 'main'],
        // 准备/实施分支（虚线）
        ['notice', 'prepare', 'sub'], ['guide', 'implement', 'sub'],
        ['myDraft', 'implement', 'sub'], ['draftMgr', 'discover', 'sub'],
        ['doubtful', 'discover', 'sub'], ['evidence', 'report', 'sub'],
        // AI层 → 主干
        ['aiHome', 'implement', 'ai'], ['aiCompare', 'implement', 'ai'],
        ['aiReview', 'discover', 'ai'], ['model', 'discover', 'ai'],
        // 成果档案
        ['archive', 'archList', 'sub'], ['archive', 'archRead', 'sub'],
        ['archive', 'analyse', 'sub'], ['archive', 'cockpit', 'sub'], ['archive', 'overview', 'sub'],
      ],
      implTaskItems: [
        { name: '我的任务', desc: '我的审计任务列表', icon: 'el-icon-user', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', badge: '', route: '/implement/task' },
        { name: '工作小结', desc: '审计工作过程记录', icon: 'el-icon-edit-outline', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/implement/log' },
        { name: '我的底稿', desc: '个人底稿管理', icon: 'el-icon-notebook-2', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/implement/myDraft' },
        { name: '底稿管理', desc: '审计底稿编制审核', icon: 'el-icon-document', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/implement/implement/draftManage' },
        { name: '疑点管理', desc: '审计疑点跟踪管理', icon: 'el-icon-question', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/implement/doubtful' },
        { name: '审计取证单', desc: '审计取证凭证管理', icon: 'el-icon-camera', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/implement/evidence' },
      ],
      implResultItems: [
        { name: '审计发现', desc: '审计问题发现记录', icon: 'el-icon-warning-outline', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/implement/discover' },
        { name: '底稿汇总', desc: '底稿数据汇总统计', icon: 'el-icon-data-analysis', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/question/draft' },
        { name: '工作小结', desc: '审计工作过程总结', icon: 'el-icon-edit-outline', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/implement/log' },
        { name: '审计取证单', desc: '审计取证凭证', icon: 'el-icon-camera', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/implement/evidence' },
      ],
      analysisItems: [
        { name: '项目情况分析', desc: '项目执行状态分析', icon: 'el-icon-data-analysis', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/question/project' },
        { name: '审计问题分析', desc: '审计问题分类统计', icon: 'el-icon-warning-outline', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/question/audit' },
        { name: '整改问题分析', desc: '整改进度深度分析', icon: 'el-icon-circle-check', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/question/corrective' },
        { name: '审计业务情况表', desc: '审计业务综合统计', icon: 'el-icon-s-order', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/question/sjywqkb' },
        { name: '审计情况统计表', desc: '审计情况汇总统计', icon: 'el-icon-data-line', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/question/auditqkcnt' },
        { name: '审计人员情况报表', desc: '人员工作量统计', icon: 'el-icon-user', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/question/auditusrrep' },
      ],
      reportItems: [
        { name: '审计结果文书', desc: '审计结果正式文书', icon: 'el-icon-document', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/implement/index' },
        { name: '审计驾驶舱', desc: '审计数据综合看板', icon: 'el-icon-data-board', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/home/homesjfx' },
        { name: '审计总览', desc: '审计业务总览统计', icon: 'el-icon-data-analysis', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/home/cwzl' },
        { name: '计划执行情况表', desc: '计划执行要点完成', icon: 'el-icon-s-order', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/question/auditplanfinal' },
        { name: '中介机构评价表', desc: '中介机构审计评价', icon: 'el-icon-s-custom', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/question/octapr' },
        { name: '年度问题及整改', desc: '本年度问题整改情况', icon: 'el-icon-circle-check', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/question/yearquefind' },
      ],
      archiveItems: [
        { name: '档案列表', desc: '审计档案管理', icon: 'el-icon-folder', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/auditRecord/list' },
        { name: '档案借阅', desc: '审计档案借阅申请', icon: 'el-icon-reading', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/auditRecord/read' },
        { name: '借阅日志', desc: '档案借阅记录查询', icon: 'el-icon-notebook-2', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/auditRecord/log' },
        { name: '项目查看', desc: '审计项目资料查看', icon: 'el-icon-view', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/implement/look' },
        { name: '项目归档', desc: '审计项目成果归档', icon: 'el-icon-folder-opened', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/implement/package' },
      ],
      aiFeatures: [
        { name: 'AI审计首页', desc: 'AI审计智能入口', icon: 'el-icon-cpu', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/Ai/home' },
        { name: '智能对比', desc: '审计数据智能比对', icon: 'el-icon-sort', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/tool/compare' },
        { name: '智能审查', desc: 'AI自动审查分析', icon: 'el-icon-search', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/tool/review' },
        { name: '审计经验库', desc: '审计经验知识积累', icon: 'el-icon-collection', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/configure/sjjyk' },
        { name: '审计模型库', desc: '审计分析模型管理', icon: 'el-icon-s-data', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/auditSjfx/sjmxk' },
        { name: '审计指引模板库', desc: '审计指引模板资源', icon: 'el-icon-guide', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/configure/experience' },
      ],
      modules: [
        { name: '计划管理', icon: 'el-icon-date', desc: '计划/查看', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', route: '/project/planIndex' },
        { name: '项目管理', icon: 'el-icon-s-order', desc: '方案/任务', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', route: '/project/index' },
        { name: '审计实施', icon: 'el-icon-s-operation', desc: '底稿/发现', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', route: '/implement/task' },
        { name: '审计报告', icon: 'el-icon-document', desc: '文书/驾驶舱', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', route: '/implement/index' },
        { name: '审计档案', icon: 'el-icon-folder-opened', desc: '归档/借阅', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', route: '/auditRecord/list' },
        { name: 'AI审计', icon: 'el-icon-cpu', desc: '智能/审查/对比', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', route: '/Ai/home' },
        { name: '审计分析', icon: 'el-icon-data-analysis', desc: '项目/问题/整改', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', route: '/question/project' },
        { name: '审计模型', icon: 'el-icon-s-data', desc: '模型/指引/经验', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', route: '/auditSjfx/sjmxk' },
        { name: '组织管理', icon: 'el-icon-office-building', desc: '机构/人员/评价', iconBg: 'rgba(235,47,150,0.1)', iconColor: '#EB2F96', route: '/configure/structuregl' },
        { name: '数据源', icon: 'el-icon-download', desc: '数据源/Excel', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', route: '/auditSjfx/sjygl' },
        { name: '消息中心', icon: 'el-icon-bell', desc: '系统消息通知', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', route: '/home/xxzx' },
      ],
      warningItems: [
        { name: '风险预警管理', icon: 'el-icon-warning-outline', iconColor: '#FF4D4F', route: '/auditSjfx/fxyjgl' },
        { name: '表达式管理', icon: 'el-icon-s-order', iconColor: '#1890FF', route: '/auditSjfx/bdsgl' },
        { name: '评估模型管理', icon: 'el-icon-data-analysis', iconColor: '#722ED1', route: '/auditSjfx/pgmxgl' },
        { name: '数据模型管理', icon: 'el-icon-s-data', iconColor: '#52C41A', route: '/auditSjfx/sjmxgl' },
      ],
      statItems: [
        { name: '上年度整改情况', icon: 'el-icon-circle-check', iconColor: '#52C41A', route: '/question/lastyearfind' },
        { name: '审计人员情况报表', icon: 'el-icon-user', iconColor: '#1890FF', route: '/question/auditusrrep' },
        { name: '审计计划执行情况', icon: 'el-icon-date', iconColor: '#722ED1', route: '/question/auditplanfinal' },
        { name: '中介机构评价表', icon: 'el-icon-s-custom', iconColor: '#FA8C16', route: '/question/octapr' },
      ],
      statTemplates: [
        { name: '模版一·利润', icon: 'el-icon-data-line', iconColor: '#52C41A', route: '/home/lrfx' },
        { name: '模版二·收入', icon: 'el-icon-coin', iconColor: '#FA8C16', route: '/home/srfx' },
        { name: '模版三·成本', icon: 'el-icon-s-order', iconColor: '#FF4D4F', route: '/home/cbyyfx' },
        { name: '模版四·资产', icon: 'el-icon-s-finance', iconColor: '#722ED1', route: '/home/zcfzfx' },
        { name: '模版五·应收', icon: 'el-icon-s-claim', iconColor: '#13C2C2', route: '/home/ysyffx' },
      ],
      baseConfigItems: [
        { name: '审计类型维护', icon: 'el-icon-setting', route: '/configure/category' },
        { name: '统计类型维护', icon: 'el-icon-s-data', route: '/configure/statistics' },
        { name: '审计问题类型', icon: 'el-icon-warning-outline', route: '/configure/auditQuestions' },
        { name: '审计文书模板库', icon: 'el-icon-document', route: '/configure/sjmbk' },
        { name: '拟实施审计指引', icon: 'el-icon-guide', route: '/configure/audit' },
        { name: '审计对象库', icon: 'el-icon-s-order', route: '/configure/auditobj' },
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
        ai: 'url(#fl-arrow-ai)',
        legal: 'url(#fl-arrow-legal)',
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
      try {
        await Promise.all([
          this.fetchKpiData(),
          this.fetchLifecycleData(),
          this.fetchRectifyProgress(),
        ])
      } catch (e) {
        console.error('审计首页数据加载异常', e)
      }
    },
    /** 获取KPI数据 */
    async fetchKpiData() {
      try {
        const res = await getAuditHomeKpiData({})
        if (res && res.code === 1 && res.data) {
          const d = res.data
          // 更新欢迎区统计
          this.$set(this.welcomeStats, 0, { ...this.welcomeStats[0], value: (d.projectCount || 0) + '个' })
          this.$set(this.welcomeStats, 1, { ...this.welcomeStats[1], value: (d.rectifyRate || 0) + '%' })
          this.$set(this.welcomeStats, 2, { ...this.welcomeStats[2], value: (d.discoveryCount || 0) + '项' })
          this.$set(this.welcomeStats, 3, { ...this.welcomeStats[3], value: (d.reportCount || 0) + '份' })
          this.$set(this.welcomeStats, 4, { ...this.welcomeStats[4], value: (d.riskCount || 0) + '条' })
          // 更新KPI卡片
          this.$set(this.kpiCards, 0, { ...this.kpiCards[0], value: String(d.projectCount || 0), barWidth: Math.min((d.projectCount || 0) * 2, 100) })
          this.$set(this.kpiCards, 1, { ...this.kpiCards[1], value: String(d.rectifyRate || 0), barWidth: d.rectifyRate || 0 })
          this.$set(this.kpiCards, 2, { ...this.kpiCards[2], value: String(d.discoveryCount || 0), barWidth: Math.min((d.discoveryCount || 0) / 3, 100) })
          this.$set(this.kpiCards, 3, { ...this.kpiCards[3], value: String(d.reportCount || 0), barWidth: Math.min((d.reportCount || 0) * 2, 100) })
          this.$set(this.kpiCards, 4, { ...this.kpiCards[4], value: String(d.unsolvedCount || 0), barWidth: Math.min((d.unsolvedCount || 0) * 3, 100) })
          this.$set(this.kpiCards, 5, { ...this.kpiCards[5], value: String(d.sheetRate || 0), barWidth: d.sheetRate || 0 })
        }
      } catch (e) {
        console.error('获取KPI数据失败', e)
      }
    },
    /** 获取生命周期节点数据 */
    async fetchLifecycleData() {
      try {
        const res = await getAuditLifecycleData({})
        if (res && res.code === 1 && res.data) {
          const d = res.data
          // 更新流程图节点badge
          const implementNode = this.flowNodes.find(n => n.id === 'implement')
          if (implementNode) implementNode.badge = d.implementCount > 0 ? String(d.implementCount) : ''
          const rectifyNode = this.flowNodes.find(n => n.id === 'rectify')
          if (rectifyNode) {
            rectifyNode.badge = d.rectifyCount > 0 ? String(d.rectifyCount) : ''
            rectifyNode.alert = d.rectifyCount > 0
          }
        }
      } catch (e) {
        console.error('获取生命周期数据失败', e)
      }
    },
    /** 获取整改进度数据 */
    async fetchRectifyProgress() {
      try {
        const res = await getAuditRectifyProgress({})
        if (res && res.code === 1 && res.data) {
          const d = res.data
          // 更新整改各环节数量
          if (this.rectifyItems[0]) this.$set(this.rectifyItems[0], 'count', d.assignCount > 0 ? String(d.assignCount) : '')
          if (this.rectifyItems[1]) this.$set(this.rectifyItems[1], 'count', d.schemeCount > 0 ? String(d.schemeCount) : '')
          if (this.rectifyItems[2]) this.$set(this.rectifyItems[2], 'count', d.practiceCount > 0 ? String(d.practiceCount) : '')
          if (this.rectifyItems[3]) this.$set(this.rectifyItems[3], 'count', d.trackCount > 0 ? String(d.trackCount) : '')
          if (this.rectifyItems[7]) this.$set(this.rectifyItems[7], 'count', d.unregisteredCount > 0 ? String(d.unregisteredCount) : '')
          // 更新进度条
          this.$set(this.rectifyProgress, 0, { ...this.rectifyProgress[0], percent: d.practiceRate || 0 })
          this.$set(this.rectifyProgress, 1, { ...this.rectifyProgress[1], percent: d.trackRate || 0 })
          this.$set(this.rectifyProgress, 2, { ...this.rectifyProgress[2], percent: d.cancelRate || 0 })
        }
      } catch (e) {
        console.error('获取整改进度失败', e)
      }
    },
    _patchPrimaryColors() {
      const bright = this.ipBright || '#1890FF'
      const rgb = this.ipPrimaryRgb || '24,144,255'
      const brightBg = `rgba(${rgb},0.1)`
      const brightBg2 = `rgba(${rgb},0.08)`
      if (this.welcomeStats && this.welcomeStats[0]) this.welcomeStats[0].color = bright
      if (this.kpiCards && this.kpiCards[0]) {
        this.kpiCards[0].iconBg = brightBg
        this.kpiCards[0].iconColor = bright
        this.kpiCards[0].barBg = brightBg
        this.kpiCards[0].barColor = bright
      }
      if (this.modules && this.modules[0]) {
        this.modules[0].iconBg = brightBg
        this.modules[0].iconColor = bright
      }
      if (this.aiFeatures && this.aiFeatures[0]) {
        this.aiFeatures[0].bgColor = brightBg2
        this.aiFeatures[0].iconColor = bright
      }
    },
  },
}
</script>

<style lang="scss" scoped>
.smart-audit-home { padding: 16px; background: #f0f2f5; min-height: 100vh; }

/* ========== 欢迎区 ========== */
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

/* ========== KPI ========== */
.kpi-section { display: grid; grid-template-columns: repeat(6, 1fr); gap: 14px; margin-bottom: 16px; }
.kpi-card { background: #fff; border-radius: 10px; padding: 16px; box-shadow: 0 1px 6px rgba(0,0,0,.06); transition: all 0.2s;
  &:hover { transform: translateY(-2px); box-shadow: 0 6px 16px rgba(0,0,0,.1); }
  &.kpi-warning { border-top: 3px solid #FA8C16; } &.kpi-danger { border-top: 3px solid #FF4D4F; }
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
.section-legend { font-size: 12px; color: #888; span { margin-left: 14px; } .dot { display: inline-block; width: 8px; height: 8px; border-radius: 50%; margin-right: 4px; vertical-align: middle; &.blue { background: var(--ip-primary, #1890FF); } &.green { background: #52C41A; } &.orange { background: #FA8C16; } &.red { background: #FF4D4F; } &.cyan { background: #13C2C2; } &.gray { background: #bbb; } } }

.flow-canvas-wrap {
  width: 100%; overflow-x: auto; overflow-y: hidden; padding: 6px;
  background: linear-gradient(90deg, rgba(24,144,255,0.04) 1px, transparent 1px) 0 0 / 24px 24px, linear-gradient(0deg, rgba(24,144,255,0.04) 1px, transparent 1px) 0 0 / 24px 24px, #fafbff;
  border-radius: 8px; border: 1px solid #f0f2f5;
}
.flow-canvas { position: relative; margin: 0 auto; }
.flow-svg { position: absolute; top: 0; left: 0; pointer-events: none; z-index: 1; }

/* 边线样式 */
.fl-edge {
  stroke-width: 1.6; fill: none;
  &.edge-main { stroke: var(--ip-primary, #1890FF); stroke-width: 2.2; }
  &.edge-sub { stroke: #b8b8b8; stroke-dasharray: 4 3; }
  &.edge-ai { stroke: #13C2C2; stroke-dasharray: 4 3; }
  &.edge-legal { stroke: #FF4D4F; stroke-width: 1.8; }
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
  &.fpn-ai { border-color: #13C2C2; background: #f0fafa; .fpn-icon-wrap { background: rgba(19,194,194,0.15); } .fpn-icon-wrap i { color: #13C2C2; } .fpn-label { color: #08979C; } }
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

/* ========== 审计实施Tab ========== */
.inner-tabs { ::v-deep .el-tabs__header { margin-bottom: 8px; } ::v-deep .el-tabs__item { font-size: 13px; padding: 0 12px; } }
.impl-list { display: flex; flex-direction: column; gap: 6px; }
.impl-entry { display: flex; align-items: center; gap: 10px; padding: 8px 10px; border-radius: 8px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.15s;
  &:hover { border-color: var(--ip-primary); background: var(--ip-light-bg, #fafbff); }
  .ie-icon { width: 30px; height: 30px; border-radius: 6px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 14px; } }
  .ie-body { flex: 1; .ie-name { font-size: 13px; font-weight: 500; color: #333; } .ie-desc { font-size: 11px; color: #999; } }
  .ie-badge { ::v-deep .el-badge__content { top: 4px; } }
}

/* ========== 整改跟踪 ========== */
.rectify-list { display: flex; flex-direction: column; gap: 6px; margin-bottom: 12px; }
.rect-entry { display: flex; align-items: center; gap: 8px; padding: 6px 10px; border-radius: 8px; cursor: pointer; transition: background 0.15s;
  &:hover { background: #f5f7fa; }
  .re-dot { width: 8px; height: 8px; border-radius: 50%; flex-shrink: 0; }
  .re-name { flex: 1; font-size: 13px; color: #333; font-weight: 500; }
  .re-count { font-size: 14px; font-weight: 700; }
}
.rectify-progress { margin-top: 4px; }
.rp-row { display: flex; align-items: center; gap: 8px; margin-bottom: 6px;
  .rp-label { font-size: 12px; color: #888; width: 80px; flex-shrink: 0; }
  .rp-bar-bg { flex: 1; height: 6px; background: #f5f5f5; border-radius: 3px; overflow: hidden; }
  .rp-bar-fill { height: 100%; border-radius: 3px; }
  .rp-val { font-size: 12px; font-weight: 600; color: #333; width: 40px; text-align: right; }
}

/* ========== 审计分析 ========== */
.analysis-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 8px; }
.analysis-card { display: flex; align-items: center; gap: 8px; padding: 10px; border-radius: 8px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.15s;
  &:hover { border-color: var(--ip-primary); }
  .alc-icon { width: 28px; height: 28px; border-radius: 6px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 13px; } }
  .alc-body { .alc-name { font-size: 12px; font-weight: 500; color: #333; } .alc-desc { font-size: 10px; color: #999; } }
}

/* ========== 审计报告 ========== */
.report-list { display: flex; flex-direction: column; gap: 6px; }
.report-entry { display: flex; align-items: center; gap: 10px; padding: 8px 10px; border-radius: 8px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.15s;
  &:hover { border-color: var(--ip-primary); background: var(--ip-light-bg, #fafbff); }
  .rt-icon { width: 30px; height: 30px; border-radius: 6px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 14px; } }
  .rt-body { flex: 1; .rt-name { font-size: 13px; font-weight: 500; color: #333; } .rt-desc { font-size: 11px; color: #999; } }
  .rt-arrow { font-size: 12px; color: #d9d9d9; flex-shrink: 0; }
}

/* ========== 审计档案 ========== */
.archive-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 10px; }
.archive-card { display: flex; flex-direction: column; align-items: center; padding: 14px 6px; border-radius: 10px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.2s; text-align: center;
  &:hover { border-color: var(--ip-primary); transform: translateY(-2px); box-shadow: 0 6px 16px rgba(var(--ip-primary-rgb),0.1); }
  .ac-icon { width: 36px; height: 36px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-bottom: 6px; i { font-size: 18px; } }
  .ac-name { font-size: 12px; font-weight: 600; color: #333; }
  .ac-desc { font-size: 10px; color: #999; margin-top: 2px; }
}

/* ========== AI审计 ========== */
.ai-panel { background: linear-gradient(180deg, var(--ip-light-bg, #f0f5ff) 0%, #fff 100%); }
.ai-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 10px; }
.ai-card { display: flex; align-items: center; gap: 10px; padding: 12px; border-radius: 10px; border: 1px solid #e8e8e8; cursor: pointer; transition: all 0.2s;
  &:hover { border-color: var(--ip-bright); box-shadow: 0 4px 12px rgba(var(--ip-primary-rgb),0.12); transform: translateY(-2px); }
  .aif-icon { width: 38px; height: 38px; border-radius: 10px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 18px; } }
  .aif-body { flex: 1; min-width: 0; .aif-name { font-size: 13px; font-weight: 600; color: #333; } .aif-desc { font-size: 11px; color: #999; margin-top: 2px; } }
  .aif-arrow { font-size: 14px; color: #d9d9d9; flex-shrink: 0; }
}

/* ========== 功能模块 ========== */
.module-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px; }
.module-card { display: flex; flex-direction: column; align-items: center; justify-content: center; padding: 14px 6px; border-radius: 10px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.2s; text-align: center;
  &:hover { border-color: var(--ip-primary); transform: translateY(-2px); box-shadow: 0 6px 16px rgba(var(--ip-primary-rgb),0.1); }
  .mc-icon { width: 36px; height: 36px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-bottom: 6px; i { font-size: 18px; } }
  .mc-name { font-size: 12px; font-weight: 600; color: #333; }
  .mc-desc { font-size: 10px; color: #bbb; margin-top: 2px; }
}

/* ========== 预警与统计 ========== */
.warn-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 10px; }
.wg-section { padding: 10px; border-radius: 8px; border: 1px solid #f0f0f0; }
.wg-title { font-size: 12px; font-weight: 600; color: var(--ip-primary); margin-bottom: 8px; padding-bottom: 4px; border-bottom: 1px solid #f0f0f0; }
.wg-item { display: flex; align-items: center; gap: 6px; padding: 5px 4px; font-size: 12px; cursor: pointer; transition: color 0.15s;
  &:hover { color: var(--ip-primary); }
  i { font-size: 12px; color: #888; } span { color: #333; }
}
</style>