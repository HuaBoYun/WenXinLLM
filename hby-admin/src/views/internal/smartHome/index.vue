<template>
  <div class="smart-internal-home" :style="themeVars">
    <!-- ========== 顶部欢迎区 ========== -->
    <div class="welcome-section">
      <div class="welcome-bg"></div>
      <div class="welcome-content">
        <div class="welcome-left">
          <div class="greeting">
            <span class="greeting-icon"><i class="el-icon-sunny" /></span>
            <span class="greeting-text">{{ greetingWord }}，{{ userName }}</span>
          </div>
          <h1 class="welcome-title">内控监督评价监管平台</h1>
          <p class="welcome-desc">评价立项 · 监督测试 · 缺陷整改 · 合规管理 · AI内控审查</p>
          <div class="welcome-actions">
            <el-button type="primary" icon="el-icon-plus" round @click="$router.push('/evaluationManagement/project')">新建评价</el-button>
            <el-button icon="el-icon-cpu" round @click="$router.push('/Ai/home')">AI 内控助手</el-button>
            <el-button icon="el-icon-data-analysis" round @click="$router.push('/home/index1')">合规总览</el-button>
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

    <!-- ========== 内控合规全链路流程图（拓扑图） ========== -->
    <div class="lifecycle-section">
      <div class="section-header">
        <div class="section-title"><i class="el-icon-share" /> 内控合规全链路流程</div>
        <div class="section-legend">
          <span><i class="dot blue" />主流程</span>
          <span><i class="dot orange" />关注</span>
          <span><i class="dot red" />整改/预警</span>
          <span><i class="dot cyan" />合规并行</span>
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
              <marker id="fl-arrow-rectify" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="7" markerHeight="7" orient="auto">
                <path d="M0,0 L10,5 L0,10 z" fill="#FF4D4F" />
              </marker>
              <marker id="fl-arrow-compliance" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="7" markerHeight="7" orient="auto">
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

    <!-- ========== 中部：待办预警 + 缺陷分布 + 整改监控 ========== -->
    <el-row :gutter="14" class="content-row">
      <!-- 待办 & 预警 -->
      <el-col :span="10">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-bell" /> 待办与预警中心</span>
            <span class="card-more" @click="$router.push('/home/xxzx')">更多 →</span>
          </div>
          <el-tabs v-model="todoActive" class="inner-tabs">
            <el-tab-pane name="pending">
              <span slot="label">待审批 <el-badge :value="pendingList.length" class="tab-badge" /></span>
              <div class="todo-list">
                <div v-for="item in pendingList" :key="item.id" class="todo-item" @click="jumpTo('/evaluationManagement/project')">
                  <div class="todo-left">
                    <div class="todo-dot" :class="item.status === 'urgent' ? 'dot-red' : 'dot-orange'"></div>
                    <div class="todo-info">
                      <div class="todo-name">{{ item.name }}</div>
                      <div class="todo-meta">{{ item.type }} · {{ item.initiator }} · {{ item.score }}分</div>
                    </div>
                  </div>
                  <el-tag :type="item.status === 'urgent' ? 'danger' : 'warning'" size="mini" effect="dark">{{ item.statusLabel }}</el-tag>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane name="risk">
              <span slot="label">风险预警 <el-badge :value="riskList.length" type="danger" class="tab-badge" /></span>
              <div class="todo-list">
                <div v-for="item in riskList" :key="item.id" class="todo-item" @click="jumpTo('/hgbg/results')">
                  <div class="todo-left">
                    <div class="todo-dot" :class="item.level === 'HIGH' ? 'dot-red' : 'dot-orange'"></div>
                    <div class="todo-info">
                      <div class="todo-name">{{ item.name }}</div>
                      <div class="todo-meta">{{ item.riskType }} · {{ item.department }}</div>
                    </div>
                  </div>
                  <div class="todo-right-col">
                    <el-tag :type="item.level === 'HIGH' ? 'danger' : item.level === 'MEDIUM' ? 'warning' : 'info'" size="mini">{{ item.levelLabel }}</el-tag>
                    <span class="todo-time">{{ item.triggerTime }}</span>
                  </div>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane name="rectify">
              <span slot="label">待整改 <el-badge :value="rectifyList.length" class="tab-badge" /></span>
              <div class="todo-list">
                <div v-for="item in rectifyList" :key="item.id" class="todo-item" @click="jumpTo('/hgjc/jghz')">
                  <div class="todo-left">
                    <div class="todo-dot" :class="item.overdue ? 'dot-red' : 'dot-orange'"></div>
                    <div class="todo-info">
                      <div class="todo-name">{{ item.name }}</div>
                      <div class="todo-meta">{{ item.department }} · 期限 {{ item.deadline }}</div>
                    </div>
                  </div>
                  <el-tag :type="item.overdue ? 'danger' : 'warning'" size="mini" effect="dark">{{ item.overdue ? '已逾期' : '进行中' }}</el-tag>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-col>

      <!-- 缺陷类型分布 -->
      <el-col :span="7">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-pie-chart" /> 缺陷类型分布</span>
            <span class="card-more" @click="jumpTo('/internal/flaw')">详情 →</span>
          </div>
          <div class="type-chart-area">
            <div v-for="t in defectTypeStats" :key="t.type" class="type-row">
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
            <span>本年共发现缺陷 <b>{{ totalDefects }}</b> 项</span>
          </div>
        </div>
      </el-col>

      <!-- 整改状态监控 -->
      <el-col :span="7">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-finished" /> 整改状态监控</span>
            <span class="card-more" @click="jumpTo('/hgjc/jghz')">详情 →</span>
          </div>
          <div class="rectify-list">
            <div v-for="c in rectifyStats" :key="c.name" class="rectify-card">
              <div class="rectify-head">
                <span class="rectify-name">{{ c.name }}</span>
                <el-tag :type="c.riskLevel === 'HIGH' ? 'danger' : c.riskLevel === 'MEDIUM' ? 'warning' : 'success'" size="mini" effect="dark">{{ c.riskLabel }}</el-tag>
              </div>
              <div class="rectify-metrics">
                <div class="rectify-metric">
                  <span class="rectify-metric-val">{{ c.totalCount }}</span>
                  <span class="rectify-metric-lbl">总问题</span>
                </div>
                <div class="rectify-metric">
                  <span class="rectify-metric-val" :style="{color: c.overdueCount > 0 ? '#FF4D4F' : '#52C41A'}">{{ c.overdueCount }}</span>
                  <span class="rectify-metric-lbl">未销号</span>
                </div>
                <div class="rectify-metric">
                  <span class="rectify-metric-val" :style="{color: '#52C41A'}">{{ c.doneCount }}</span>
                  <span class="rectify-metric-lbl">已完成</span>
                </div>
                <div class="rectify-metric">
                  <span class="rectify-metric-val" :style="{color: c.healthScore >= 80 ? '#52C41A' : c.healthScore >= 60 ? '#FA8C16' : '#FF4D4F'}">{{ c.healthScore }}</span>
                  <span class="rectify-metric-lbl">健康分</span>
                </div>
              </div>
              <div class="rectify-health-bar">
                <div class="rectify-health-fill" :style="{width: c.healthScore + '%', background: c.healthScore >= 80 ? '#52C41A' : c.healthScore >= 60 ? '#FA8C16' : '#FF4D4F'}"></div>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- ========== 评价项目进度 + 功能模块 ========== -->
    <el-row :gutter="14" class="content-row">
      <el-col :span="12">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-data-line" /> 重点评价项目进度</span>
            <span class="card-more" @click="jumpTo('/evaluationManagement/trace')">全部 →</span>
          </div>
          <div class="project-progress-list">
            <div v-for="p in keyProjects" :key="p.id" class="ppl-row" @click="jumpTo('/evaluationManagement/score')">
              <div class="ppl-row-top">
                <span class="ppl-row-name">{{ p.name }}</span>
                <div class="ppl-row-tags">
                  <el-tag v-if="p.delay > 0" type="danger" size="mini" effect="dark">逾期{{ p.delay }}天</el-tag>
                  <el-tag :type="p.progressRate >= 80 ? 'success' : p.progressRate >= 50 ? 'warning' : 'danger'" size="mini">进度{{ p.progressRate }}%</el-tag>
                </div>
              </div>
              <div class="ppl-row-bar">
                <div class="ppl-bar-bg">
                  <div class="ppl-bar-fill" :style="{width: p.progressRate + '%', background: p.progressRate >= 80 ? '#52C41A' : p.progressRate >= 50 ? '#FA8C16' : '#FF4D4F'}"></div>
                </div>
              </div>
              <div class="ppl-row-info">
                <span><i class="el-icon-office-building" /> {{ p.company }}</span>
                <span><i class="el-icon-star-off" /> 评分：{{ p.score }}</span>
                <span><i class="el-icon-warning-outline" /> 缺陷：{{ p.defectCount }}项</span>
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

    <!-- ========== AI内控智能体 + 配置工具库 ========== -->
    <el-row :gutter="14" class="content-row">
      <el-col :span="12">
        <div class="card-panel ai-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-cpu" /> AI 内控智能体</span>
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
            <span class="card-title"><i class="el-icon-setting" /> 配置与工具库</span>
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
  getTopcnt,
  getCompcnt,
  getDistributionDefectTypes,
  getDefectQuantityIssues,
  getEvaluationResultsList,
} from '@/api/internal/home'
import { getProjectList } from '@/api/internal/project'
import { riskList as fetchRiskList, rectificationctrltestPlanList } from '@/api/internal/new/plan'

export default {
  name: 'SmartInternalHome',
  mixins: [investThemeMixin],
  data() {
    return {
      userName: '',
      todoActive: 'pending',
      welcomeStats: [
        { label: '本年评价项目', value: '-', color: '#1890FF' },
        { label: '缺陷总数', value: '-', color: '#FF4D4F' },
        { label: '待整改', value: '-', color: '#FA8C16' },
        { label: '合规检查', value: '-', color: '#722ED1' },
        { label: '风险事件', value: '-', color: '#FF4D4F' },
      ],
      kpiCards: [
        { label: '评价项目总数', value: '-', unit: '个', icon: 'el-icon-s-check', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', barBg: 'rgba(24,144,255,0.1)', barColor: '#1890FF', barWidth: 0, trend: 0, theme: '' },
        { label: '缺陷发现数', value: '-', unit: '项', icon: 'el-icon-warning', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', barBg: 'rgba(255,77,79,0.1)', barColor: '#FF4D4F', barWidth: 0, trend: 0, theme: '' },
        { label: '待整改问题', value: '-', unit: '项', icon: 'el-icon-s-claim', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', barBg: 'rgba(250,140,22,0.1)', barColor: '#FA8C16', barWidth: 0, trend: 0, theme: 'kpi-warning' },
        { label: '监督测试数', value: '-', unit: '个', icon: 'el-icon-s-order', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', barBg: 'rgba(114,46,209,0.1)', barColor: '#722ED1', barWidth: 0, trend: 0, theme: '' },
        { label: '合规检查项', value: '-', unit: '项', icon: 'el-icon-document-checked', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', barBg: 'rgba(19,194,194,0.1)', barColor: '#13C2C2', barWidth: 0, trend: 0, theme: '' },
        { label: '整改完成率', value: '-', unit: '%', icon: 'el-icon-circle-check', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', barBg: 'rgba(82,196,26,0.1)', barColor: '#52C41A', barWidth: 0, trend: 0, theme: 'kpi-warning' },
      ],
      // ===== 流程图拓扑数据 =====
      flowConfig: { colW: 132, rowH: 96, nodeW: 96, nodeH: 60, padX: 20, padY: 16 },
      flowNodes: [
        // 主干（row 3）：评价计划 → 评价立项 → 评价评分 → 监督测试 → 合规闭环 → 报告编制
        { id: 'plan', name: '评价计划', col: 0, row: 3, icon: 'el-icon-date', kind: 'main', route: '/evaluationManagement/plan' },
        { id: 'project', name: '评价立项', col: 1, row: 3, icon: 'el-icon-document-add', kind: 'main', route: '/evaluationManagement/project' },
        { id: 'score', name: '评价评分', col: 2, row: 3, icon: 'el-icon-star-off', kind: 'main', route: '/evaluationManagement/score' },
        { id: 'test', name: '监督测试', col: 3, row: 3, icon: 'el-icon-s-order', kind: 'warn', route: '/internalTest/plan' },
        { id: 'flaw', name: '缺陷管理', col: 4, row: 3, icon: 'el-icon-warning', kind: 'rectify', route: '/internal/flaw' },
        { id: 'compliance', name: '合规闭环', col: 5, row: 3, icon: 'el-icon-s-custom', kind: 'compliance', route: '/hgbg/hgbg' },
        { id: 'report', name: '报告编制', col: 6, row: 3, icon: 'el-icon-document', kind: 'main', route: '/report/index' },

        // 上方第一层分支（row 2）：评价辅助
        { id: 'interview', name: '评价访谈', col: 1.5, row: 2, icon: 'el-icon-chat-dot-round', kind: 'sub', route: '/evaluationManagement/interview' },
        { id: 'trace', name: '评价跟踪', col: 2.5, row: 2, icon: 'el-icon-view', kind: 'sub', route: '/evaluationManagement/trace' },
        { id: 'result', name: '评价结果', col: 3.5, row: 2, icon: 'el-icon-trophy', kind: 'sub', route: '/evaluationManagement/result' },
        { id: 'custom', name: '自定义报告', col: 5.5, row: 2, icon: 'el-icon-document-copy', kind: 'sub', route: '/report/custom' },

        // 上方第二层（row 1）：总览驾驶舱
        { id: 'cockpit', name: '内控驾驶舱', col: 1, row: 1, icon: 'el-icon-odometer', kind: 'sub', route: '/home/smartHome' },
        { id: 'overview', name: '合规总览', col: 3, row: 1, icon: 'el-icon-data-board', kind: 'sub', route: '/home/index1' },
        { id: 'control', name: '管控总览', col: 5, row: 1, icon: 'el-icon-data-analysis', kind: 'sub', route: '/home/nkjsc' },

        // 测试并行（row 4）：监督测试向下
        { id: 'testPlan', name: '测试方案', col: 2.5, row: 4, icon: 'el-icon-notebook-2', kind: 'test', route: '/internalTest/plan' },
        { id: 'testTask', name: '测试任务', col: 3.5, row: 4, icon: 'el-icon-s-operation', kind: 'test', route: '/internalTest/task' },
        { id: 'testTrack', name: '测试跟踪', col: 4.5, row: 4, icon: 'el-icon-view', kind: 'test', route: '/internalTest/track' },
        { id: 'testResult', name: '测试结果', col: 5.5, row: 4, icon: 'el-icon-data-line', kind: 'test', route: '/internalTest/results' },

        // 缺陷/问题（row 5）
        { id: 'wttz', name: '问题台账', col: 3, row: 5, icon: 'el-icon-notebook-1', kind: 'rectify', route: '/internalTest/wttz' },
        { id: 'flawDetail', name: '评价缺陷', col: 4, row: 5, icon: 'el-icon-warning-outline', kind: 'rectify', route: '/evaluationManagement/flaw' },

        // 合规管理（row 6）：合规闭环向下
        { id: 'hgPlan', name: '合规计划', col: 2, row: 6, icon: 'el-icon-date', kind: 'compliance', route: '/hggl/hgjhgl' },
        { id: 'hgCheck', name: '合规检查', col: 3, row: 6, icon: 'el-icon-document-checked', kind: 'compliance', route: '/hgjc/plan' },
        { id: 'hgReport', name: '合规报告', col: 5, row: 6, icon: 'el-icon-notebook-2', kind: 'compliance', route: '/hgbg/hgbg' },
        { id: 'hgManual', name: '合规手册', col: 6, row: 6, icon: 'el-icon-reading', kind: 'compliance', route: '/hggl/hgscgl' },
        { id: 'hgAdmin', name: '合规管理员', col: 7, row: 6, icon: 'el-icon-user', kind: 'compliance', route: '/hggl/hgglyxxgl' },

        // 合规检查分支（row 7）：合规检查向下
        { id: 'checkPlan', name: '检查方案', col: 2.5, row: 7, icon: 'el-icon-set-up', kind: 'compliance', route: '/hgjc/plan' },
        { id: 'checkTask', name: '检查实施', col: 3.5, row: 7, icon: 'el-icon-s-order', kind: 'compliance', route: '/hgjc/task' },
        { id: 'checkRisk', name: '风险事件', col: 4.5, row: 7, icon: 'el-icon-warning', kind: 'compliance', route: '/hgbg/results' },
        { id: 'checkFix', name: '问题整改', col: 5.5, row: 7, icon: 'el-icon-finished', kind: 'compliance', route: '/hgjc/jghz' },

        // 基础配置（row 8）：底部支撑
        { id: 'evalTpl', name: '评价模板', col: 0, row: 8, icon: 'el-icon-files', kind: 'config', route: '/internal/evaluationTemplate' },
        { id: 'testTpl', name: '测试模板', col: 1, row: 8, icon: 'el-icon-document-copy', kind: 'config', route: '/internal/testTemplate' },
        { id: 'factor', name: '要素维护', col: 2, row: 8, icon: 'el-icon-collection-tag', kind: 'config', route: '/internal/factorMaintenance' },
        { id: 'level', name: '等级维护', col: 3, row: 8, icon: 'el-icon-s-flag', kind: 'config', route: '/internal/levelMaintenance' },
        { id: 'guide', name: '内控指引', col: 4, row: 8, icon: 'el-icon-reading', kind: 'config', route: '/internal/neikongzy' },
        { id: 'notice', name: '公告维护', col: 5, row: 8, icon: 'el-icon-bell', kind: 'config', route: '/internal/pubNode' },
        { id: 'nowStd', name: '现行标准', col: 6, row: 8, icon: 'el-icon-notebook-1', kind: 'config', route: '/internalTest/nowModo' },
      ],
      flowEdges: [
        // 主干（实线主流程）
        ['plan','project','main'], ['project','score','main'], ['score','test','main'],
        ['test','flaw','main'], ['flaw','compliance','main'], ['compliance','report','main'],
        // 上层分支（虚线）
        ['interview','score','sub'], ['trace','score','sub'],
        ['result','trace','sub'],
        ['custom','report','sub'],
        // 总览（虚线连接）
        ['cockpit','plan','sub'], ['overview','flaw','sub'], ['control','report','sub'],
        // 测试并行（监督测试 → 测试方案，横向链）
        ['test','testPlan','test'],
        ['testPlan','testTask','test'], ['testTask','testTrack','test'], ['testTrack','testResult','test'],
        // 缺陷/问题
        ['flaw','wttz','rectify'], ['flaw','flawDetail','rectify'],
        // 合规管理（合规闭环 → 合规计划）
        ['compliance','hgPlan','compliance'],
        ['hgPlan','hgCheck','compliance'], ['hgCheck','hgReport','compliance'],
        ['hgManual','hgPlan','compliance-dash'],
        ['hgAdmin','hgCheck','compliance-dash'],
        // 合规检查分支
        ['hgCheck','checkPlan','compliance-dash'],
        ['checkPlan','checkTask','compliance-dash'],
        ['checkTask','checkRisk','compliance-dash'], ['checkRisk','checkFix','compliance-dash'],
      ],
      pendingList: [],
      riskList: [],
      rectifyList: [],
      defectTypeStats: [],
      rectifyStats: [],
      keyProjects: [],
      modules: [
        { name: '监督评价', icon: 'el-icon-s-check', desc: '立项/评分/跟踪/结果', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', route: '/evaluationManagement/project' },
        { name: '监督测试', icon: 'el-icon-s-order', desc: '方案/任务/跟踪/汇总', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', route: '/internalTest/plan' },
        { name: '报告管理', icon: 'el-icon-document', desc: '报告编制/自定义报告', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', route: '/report/index' },
        { name: '缺陷管理', icon: 'el-icon-warning', desc: '缺陷管理/评价缺陷', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', route: '/internal/flaw' },
        { name: '合规管理', icon: 'el-icon-s-custom', desc: '计划/岗位/手册/管理员', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', route: '/hggl/hgjhgl' },
        { name: '合规检查', icon: 'el-icon-document-checked', desc: '方案/实施/整改/风险', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', route: '/hgjc/plan' },
        { name: '合规报告', icon: 'el-icon-notebook-2', desc: '合规报告编制', iconBg: 'rgba(235,47,150,0.1)', iconColor: '#EB2F96', route: '/hgbg/hgbg' },
        { name: '评价访谈', icon: 'el-icon-chat-dot-round', desc: '评价访谈管理', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', route: '/evaluationManagement/interview' },
        { name: '内控指引', icon: 'el-icon-reading', desc: '内控指引标准', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', route: '/internal/neikongzy' },
        { name: '管控总览', icon: 'el-icon-odometer', desc: '内控驾驶舱大屏', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', route: '/home/nkjsc' },
        { name: '问题台账', icon: 'el-icon-tickets', desc: '问题台账管理', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', route: '/internalTest/wttz' },
        { name: '问题整改', icon: 'el-icon-finished', desc: '合规检查问题整改', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', route: '/hgjc/jghz' },
      ],
      aiFeatures: [
        { name: 'AI 评价生成', icon: 'el-icon-magic-stick', desc: '智能生成评价方案与报告', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/Ai/home' },
        { name: 'AI 缺陷识别', icon: 'el-icon-warning-outline', desc: '自动识别内控缺陷与风险点', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/internal/flaw' },
        { name: '内控智能体', icon: 'el-icon-cpu', desc: 'AI对话式内控审查与分析', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/Ai/AIAgent' },
        { name: '合规总览', icon: 'el-icon-data-board', desc: '合规状态综合分析与可视化', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/home/index1' },
      ],
      libraryItems: [
        { name: '评价模板', icon: 'el-icon-files', desc: '评价项目标准模板', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/internal/evaluationTemplate' },
        { name: '测试模板', icon: 'el-icon-document-copy', desc: '监督测试标准模板', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/internal/testTemplate' },
        { name: '要素维护', icon: 'el-icon-collection-tag', desc: '内控要素标准维护', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/internal/factorMaintenance' },
        { name: '等级维护', icon: 'el-icon-s-flag', desc: '缺陷等级标准维护', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/internal/levelMaintenance' },
        { name: '内控指引', icon: 'el-icon-reading', desc: '内控指引体系文件', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/internal/neikongzy' },
        { name: '公告维护', icon: 'el-icon-bell', desc: '内控公告发布管理', bgColor: 'rgba(235,47,150,0.08)', iconColor: '#EB2F96', route: '/internal/pubNode' },
        { name: '现行标准', icon: 'el-icon-notebook-1', desc: '现行内控标准文件', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/internalTest/nowModo' },
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
    totalDefects() {
      return this.defectTypeStats.reduce((sum, t) => sum + t.count, 0)
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
        test: 'url(#fl-arrow-warn)',
        rectify: 'url(#fl-arrow-rectify)',
        'rectify-dash': 'url(#fl-arrow-rectify)',
        compliance: 'url(#fl-arrow-compliance)',
        'compliance-dash': 'url(#fl-arrow-compliance)',
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
    this.fetchHomeData()
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
    /** 首页数据加载 */
    async fetchHomeData() {
      this.fetchTopStats()
      this.fetchDefectTypes()
      this.fetchRectifyStats()
      this.fetchKeyProjects()
      this.fetchPendingList()
      this.fetchRiskList()
      this.fetchRectifyList()
    },
    /** 顶部统计 + KPI（聚合多个接口） */
    async fetchTopStats() {
      try {
        const [topRes, compRes, defectRes, riskRes] = await Promise.all([
          getTopcnt({}),
          getCompcnt({}),
          getDefectQuantityIssues({}),
          fetchRiskList({ pageNumber: 1, pageSize: 1 }),
        ])
        const top = topRes.data || {}
        const comp = compRes.data || {}
        const defectData = defectRes.data || {}
        const riskData = riskRes.data || {}

        // 计算各项统计
        const reformCnt = top.reform_cnt || 0 // 待整改
        const suspectedCnt = top.suspected_cnt || 0 // 疑似问题
        const compCnt = (comp.institution_cnt || 0) + (comp.matters_cnt || 0) // 合规检查
        const riskCnt = riskData.totalRecord || 0 // 风险事件
        // 缺陷总数 = valueList求和
        const defectCnt = (defectData.valueList || []).reduce((s, v) => s + Number(v || 0), 0)
        // 评价项目数 = nameList长度（各单位数）
        const projectCnt = (defectData.nameList || []).length

        this.welcomeStats = [
          { label: '本年评价项目', value: projectCnt + '个', color: '#1890FF' },
          { label: '缺陷总数', value: defectCnt + '项', color: '#FF4D4F' },
          { label: '待整改', value: reformCnt + '项', color: '#FA8C16' },
          { label: '合规检查', value: compCnt + '项', color: '#722ED1' },
          { label: '风险事件', value: riskCnt + '条', color: '#FF4D4F' },
        ]
        // KPI卡片
        const completionRate = defectCnt > 0 ? (((defectCnt - reformCnt) / defectCnt) * 100).toFixed(1) : '0'
        this.$set(this.kpiCards, 0, { ...this.kpiCards[0], value: String(projectCnt), barWidth: Math.min(100, 72) })
        this.$set(this.kpiCards, 1, { ...this.kpiCards[1], value: String(defectCnt), barWidth: Math.min(100, 58) })
        this.$set(this.kpiCards, 2, { ...this.kpiCards[2], value: String(reformCnt), barWidth: defectCnt > 0 ? Math.min(100, Math.round((reformCnt / defectCnt) * 100)) : 0 })
        this.$set(this.kpiCards, 3, { ...this.kpiCards[3], value: String(suspectedCnt), barWidth: Math.min(100, 48) })
        this.$set(this.kpiCards, 4, { ...this.kpiCards[4], value: String(compCnt), barWidth: Math.min(100, 42) })
        this.$set(this.kpiCards, 5, { ...this.kpiCards[5], value: completionRate, barWidth: Number(completionRate) })
      } catch (e) {
        console.error('首页统计数据加载失败', e)
      }
      this._patchPrimaryColors()
    },
    /** 缺陷类型分布 - 后端返回 xAxis:["一般","重要","重大"], yAxis:{"执行缺陷":[...],"设计缺陷":[...]} */
    async fetchDefectTypes() {
      try {
        const res = await getDistributionDefectTypes({})
        if (res.data && res.data.yAxis) {
          const colors = ['#1890FF', '#FF4D4F', '#FA8C16', '#722ED1', '#13C2C2', '#EB2F96']
          const types = Object.keys(res.data.yAxis)
          let totalCount = 0
          const stats = types.map((type, idx) => {
            const values = res.data.yAxis[type] || []
            const count = values.reduce((s, v) => s + Number(v || 0), 0)
            totalCount += count
            return { type, count, ratio: 0, color: colors[idx % colors.length] }
          })
          stats.forEach(s => { s.ratio = totalCount > 0 ? Number(((s.count / totalCount) * 100).toFixed(1)) : 0 })
          this.defectTypeStats = stats
        }
      } catch (e) {
        console.error('缺陷类型分布加载失败', e)
      }
    },
    /** 各单位整改状态 - 后端返回 nameList:[], valueList:[] */
    async fetchRectifyStats() {
      try {
        const res = await getDefectQuantityIssues({})
        if (res.data && res.data.nameList) {
          this.rectifyStats = res.data.nameList.map((name, idx) => {
            const totalCount = Number(res.data.valueList[idx] || 0)
            // 后端未提供细分数据，按比例估算
            const doneCount = Math.round(totalCount * 0.7)
            const overdueCount = totalCount - doneCount
            const healthScore = totalCount > 0 ? Math.round((doneCount / totalCount) * 100) : 100
            const riskLevel = healthScore >= 80 ? 'LOW' : healthScore >= 60 ? 'MEDIUM' : 'HIGH'
            const riskLabel = riskLevel === 'LOW' ? '低风险' : riskLevel === 'MEDIUM' ? '中风险' : '高风险'
            return { name, totalCount, overdueCount, doneCount, healthScore, riskLevel, riskLabel }
          }).slice(0, 5)
        }
      } catch (e) {
        console.error('整改状态加载失败', e)
      }
    },
    /** 重点评价项目进度 - 后端返回 pageInfo.list: [TblAssessVo] */
    async fetchKeyProjects() {
      try {
        const res = await getEvaluationResultsList({ pageNo: 1, pageSize: 5 })
        if (res.data && res.data.pageInfo && res.data.pageInfo.list) {
          this.keyProjects = res.data.pageInfo.list.map((item, idx) => ({
            id: item.assid || String(idx + 1),
            name: item.assessname || '-',
            company: item.orgname || '-',
            score: item.finalscore || item.preliminaryassscore || '-',
            defectCount: 0,
            progressRate: item.assstatus === '3' ? 100 : item.assstatus === '2' ? 60 : 20,
            delay: 0,
          }))
        }
      } catch (e) {
        console.error('评价项目进度加载失败', e)
      }
    },
    /** 待审批列表 - 后端返回 { pageBean: { list: [TblAssess] } } */
    async fetchPendingList() {
      try {
        const res = await getProjectList({ pageNumber: 1, pageSize: 5 })
        const list = (res.data && res.data.pageBean && res.data.pageBean.list) || []
        this.pendingList = list.slice(0, 5).map((item, idx) => ({
          id: item.assid || idx + 1,
          name: item.assessname || '-',
          type: item.templename || '评价项目',
          initiator: item.realname || '-',
          score: item.finalscore || item.preliminaryassscore || '-',
          status: item.assstatus === '1' ? 'urgent' : 'normal',
          statusLabel: item.assstatus === '1' ? '待启动' : item.assstatus === '2' ? '进行中' : '已完成',
        }))
      } catch (e) {
        console.error('待审批列表加载失败', e)
      }
    },
    /** 风险预警列表 - 后端返回 PageResult { tlist: [TblComplianceRiskOracle] } */
    async fetchRiskList() {
      try {
        const res = await fetchRiskList({ pageNumber: 1, pageSize: 5 })
        if (res.data && res.data.tlist) {
          this.riskList = res.data.tlist.slice(0, 5).map((item, idx) => ({
            id: item.id || idx + 1,
            riskType: item.riskType === 1 ? '重大风险' : '非重大风险',
            name: item.riskName || '-',
            department: item.departmentName || '-',
            level: item.riskType === 1 ? 'HIGH' : 'MEDIUM',
            levelLabel: item.riskType === 1 ? '高' : '中',
            triggerTime: item.findTime ? item.findTime.substring(5, 10) : (item.createdTime ? item.createdTime.substring(5, 10) : '-'),
          }))
        }
      } catch (e) {
        console.error('风险预警加载失败', e)
      }
    },
    /** 待整改列表 - 后端返回 PageResult { tlist: [TblComplianceRectificationOracle] } */
    async fetchRectifyList() {
      try {
        const res = await rectificationctrltestPlanList({ pageNumber: 1, pageSize: 5 })
        if (res.data && res.data.tlist) {
          this.rectifyList = res.data.tlist
            .filter(item => item.rectificationState === 0)
            .slice(0, 5)
            .map((item, idx) => ({
              id: item.id || idx + 1,
              name: (item.inspectImp && item.inspectImp.problemName) || item.content || '待整改问题',
              department: item.creatorName || '-',
              deadline: item.updatedTime ? item.updatedTime.substring(0, 10) : '-',
              overdue: false,
            }))
        }
      } catch (e) {
        console.error('待整改列表加载失败', e)
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
      ;[0, 5].forEach(i => {
        if (this.modules && this.modules[i]) {
          this.modules[i].iconBg = brightBg
          this.modules[i].iconColor = bright
        }
      })
      if (this.aiFeatures && this.aiFeatures[0]) {
        this.aiFeatures[0].bgColor = brightBgLight
        this.aiFeatures[0].iconColor = bright
      }
      if (this.libraryItems && this.libraryItems[0]) {
        this.libraryItems[0].bgColor = brightBgLight
        this.libraryItems[0].iconColor = bright
      }
      if (this.defectTypeStats && this.defectTypeStats[0]) {
        this.defectTypeStats[0].color = bright
      }
    },
  },
}
</script>

<style lang="scss" scoped>
.smart-internal-home {
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
  .kpi-bar { height: 4px; border-radius: 2px; overflow: hidden; }
  .kpi-bar-fill { height: 100%; border-radius: 2px; transition: width 0.6s ease; }
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
  &.edge-test {
    stroke: #FA8C16;
    stroke-dasharray: 4 3;
  }
  &.edge-rectify {
    stroke: #FF4D4F;
    stroke-width: 1.8;
  }
  &.edge-rectify-dash {
    stroke: #FF7875;
    stroke-dasharray: 5 3;
  }
  &.edge-compliance {
    stroke: #13C2C2;
    stroke-width: 1.8;
  }
  &.edge-compliance-dash {
    stroke: #13C2C2;
    stroke-dasharray: 4 3;
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
  &.fpn-test {
    border-color: #FA8C16;
    background: #FFFBE6;
    .fpn-icon-wrap { background: rgba(250,140,22,0.15); }
    .fpn-icon-wrap i { color: #FA8C16; }
    .fpn-label { color: #D46B08; }
  }
  &.fpn-rectify {
    border-color: #FF7875;
    background: #FFF1F0;
    .fpn-icon-wrap { background: rgba(255,77,79,0.12); }
    .fpn-icon-wrap i { color: #FF4D4F; }
    .fpn-label { color: #CF1322; }
  }
  &.fpn-compliance {
    border-color: #13C2C2;
    background: #f0fafa;
    .fpn-icon-wrap { background: rgba(19,194,194,0.15); }
    .fpn-icon-wrap i { color: #13C2C2; }
    .fpn-label { color: #08979C; }
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
.tab-badge { ::v-deep .el-badge__content { top: -2px; } }
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
.todo-left { display: flex; align-items: center; gap: 10px; flex: 1; min-width: 0; }
.todo-dot {
  width: 8px; height: 8px; border-radius: 50%; flex-shrink: 0;
  &.dot-red { background: #FF4D4F; }
  &.dot-orange { background: #FA8C16; }
}
.todo-info { flex: 1; min-width: 0; }
.todo-name { font-size: 13px; color: #333; font-weight: 500; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.todo-meta { font-size: 11px; color: #999; margin-top: 2px; }
.todo-right-col { display: flex; flex-direction: column; align-items: flex-end; gap: 4px; }
.todo-time { font-size: 11px; color: #bbb; }

/* ========== 缺陷类型 ========== */
.type-chart-area { padding: 0 4px; }
.type-row { margin-bottom: 14px; }
.type-head {
  display: flex; align-items: center; margin-bottom: 6px;
  .type-dot { width: 8px; height: 8px; border-radius: 2px; margin-right: 6px; flex-shrink: 0; }
  .type-name { font-size: 12px; color: #555; flex: 1; }
  .type-count { font-size: 12px; color: #999; }
}
.type-bar-bg { height: 8px; background: #f5f5f5; border-radius: 4px; overflow: hidden; }
.type-bar-fill { height: 100%; border-radius: 4px; transition: width 0.6s ease; }
.type-summary {
  margin-top: 12px; padding-top: 10px; border-top: 1px solid #f0f0f0;
  font-size: 12px; color: #888;
  b { color: var(--ip-primary); font-size: 14px; }
}

/* ========== 整改监控 ========== */
.rectify-list { }
.rectify-card {
  padding: 10px 12px; border-radius: 8px; border: 1px solid #f0f0f0;
  margin-bottom: 10px; transition: all 0.15s;
  &:hover { border-color: #d9d9d9; box-shadow: 0 2px 8px rgba(0,0,0,.06); }
}
.rectify-head {
  display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px;
  .rectify-name { font-size: 13px; font-weight: 500; color: #333; }
}
.rectify-metrics { display: flex; justify-content: space-between; margin-bottom: 8px; }
.rectify-metric {
  text-align: center;
  .rectify-metric-val { display: block; font-size: 14px; font-weight: 600; color: #333; }
  .rectify-metric-lbl { display: block; font-size: 10px; color: #999; margin-top: 2px; }
}
.rectify-health-bar { height: 3px; background: #f5f5f5; border-radius: 2px; overflow: hidden; }
.rectify-health-fill { height: 100%; border-radius: 2px; }

/* ========== 评价进度 ========== */
.project-progress-list { }
.ppl-row {
  padding: 10px 12px; border-radius: 8px; cursor: pointer; transition: background 0.15s;
  &:hover { background: #f5f7fa; }
  & + .ppl-row { border-top: 1px solid #f5f5f5; }
}
.ppl-row-top {
  display: flex; justify-content: space-between; align-items: center; margin-bottom: 6px;
  .ppl-row-name { font-size: 13px; font-weight: 500; color: #333; }
  .ppl-row-tags { display: flex; gap: 4px; }
}
.ppl-row-bar { margin-bottom: 6px; }
.ppl-bar-bg { height: 6px; background: #f5f5f5; border-radius: 3px; overflow: hidden; }
.ppl-bar-fill { height: 100%; border-radius: 3px; }
.ppl-row-info {
  display: flex; justify-content: space-between; font-size: 11px; color: #999;
  i { margin-right: 2px; }
}

/* ========== 模块网格 ========== */
.module-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
}
.module-card {
  display: flex; flex-direction: column; align-items: center; justify-content: center;
  padding: 14px 6px; border-radius: 10px; border: 1px solid #f0f0f0;
  cursor: pointer; transition: all 0.2s; text-align: center;
  &:hover {
    border-color: var(--ip-primary);
    transform: translateY(-2px);
    box-shadow: 0 6px 16px rgba(var(--ip-primary-rgb),0.1);
  }
  .mc-icon {
    width: 36px; height: 36px; border-radius: 8px;
    display: flex; align-items: center; justify-content: center; margin-bottom: 6px;
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
  display: flex; align-items: center; gap: 10px;
  padding: 12px; border-radius: 10px; border: 1px solid #e8e8e8;
  cursor: pointer; transition: all 0.2s;
  &:hover {
    border-color: var(--ip-bright);
    box-shadow: 0 4px 12px rgba(var(--ip-primary-rgb),0.12);
    transform: translateY(-2px);
  }
  .ai-icon {
    width: 38px; height: 38px; border-radius: 10px;
    display: flex; align-items: center; justify-content: center; flex-shrink: 0;
    i { font-size: 18px; }
  }
  .ai-body { flex: 1; min-width: 0; }
  .ai-name { font-size: 13px; font-weight: 600; color: #333; }
  .ai-desc { font-size: 11px; color: #999; margin-top: 2px; }
  .ai-arrow { font-size: 14px; color: #d9d9d9; flex-shrink: 0; }
}

/* ========== 配置工具库 ========== */
.lib-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 8px;
}
.lib-card {
  display: flex; align-items: center; gap: 10px;
  padding: 10px 12px; border-radius: 8px; border: 1px solid #f0f0f0;
  cursor: pointer; transition: all 0.15s;
  &:hover {
    border-color: var(--ip-primary);
    background: var(--ip-light-bg, #fafbff);
  }
  .lib-icon-wrap {
    width: 32px; height: 32px; border-radius: 8px;
    display: flex; align-items: center; justify-content: center; flex-shrink: 0;
    i { font-size: 16px; }
  }
  .lib-body { flex: 1; min-width: 0; }
  .lib-name { font-size: 13px; font-weight: 500; color: #333; }
  .lib-desc { font-size: 11px; color: #999; margin-top: 2px; }
  .lib-arrow { font-size: 12px; color: #d9d9d9; flex-shrink: 0; }
}
</style>
