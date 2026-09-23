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
          <h1 class="welcome-title">敏捷审计监管平台</h1>
          <p class="welcome-desc">计划编制 · 项目实施 · 审计整改 · 质量评议 · 工程审计 · 数据采集</p>
          <div class="welcome-actions">
            <el-button type="primary" icon="el-icon-s-order" round @click="$router.push('/jhlx/jhxq')">计划需求</el-button>
            <el-button icon="el-icon-s-check" round @click="$router.push('/project/index')">实施方案</el-button>
            <el-button icon="el-icon-data-analysis" round @click="$router.push('/rectify/wtda')">审计整改</el-button>
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

    <!-- ========== 敏捷审计全链路流程图（拓扑图） ========== -->
    <div class="lifecycle-section">
      <div class="section-header">
        <div class="section-title"><i class="el-icon-share" /> 敏捷审计全链路流程</div>
        <div class="section-legend">
          <span><i class="dot blue" />主流程</span>
          <span><i class="dot green" />计划支撑</span>
          <span><i class="dot orange" />质量评议</span>
          <span><i class="dot cyan" />AI审计</span>
          <span><i class="dot gray" />基础配置</span>
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
              <marker id="fl-arrow-plan" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="7" markerHeight="7" orient="auto">
                <path d="M0,0 L10,5 L0,10 z" fill="#52C41A" />
              </marker>
              <marker id="fl-arrow-quality" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="7" markerHeight="7" orient="auto">
                <path d="M0,0 L10,5 L0,10 z" fill="#FA8C16" />
              </marker>
              <marker id="fl-arrow-ai" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="7" markerHeight="7" orient="auto">
                <path d="M0,0 L10,5 L0,10 z" fill="#13C2C2" />
              </marker>
              <marker id="fl-arrow-eng" viewBox="0 0 10 10" refX="9" refY="5" markerWidth="7" markerHeight="7" orient="auto">
                <path d="M0,0 L10,5 L0,10 z" fill="#722ED1" />
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

    <!-- ========== 中部：计划编制 + 项目实施 + 审计整改 ========== -->
    <el-row :gutter="14" class="content-row">
      <!-- 计划编制 -->
      <el-col :span="10">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-date" /> 计划编制</span>
            <span class="card-more" @click="jumpTo('/jhlx/jhxq')">计划需求 →</span>
          </div>
          <el-tabs v-model="planActive" class="inner-tabs">
            <el-tab-pane name="demand">
              <span slot="label">计划需求</span>
              <div class="plan-list">
                <div v-for="item in planDemandItems" :key="item.name" class="plan-entry" @click="jumpTo(item.route)">
                  <div class="pe-icon" :style="{background: item.bgColor}">
                    <i :class="item.icon" :style="{color: item.iconColor}" />
                  </div>
                  <div class="pe-info">
                    <span class="pe-name">{{ item.name }}</span>
                    <span class="pe-desc">{{ item.desc }}</span>
                  </div>
                  <i class="el-icon-arrow-right pe-arrow" />
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane name="project">
              <span slot="label">项目启动</span>
              <div class="plan-list">
                <div v-for="item in planProjectItems" :key="item.name" class="plan-entry" @click="jumpTo(item.route)">
                  <div class="pe-icon" :style="{background: item.bgColor}">
                    <i :class="item.icon" :style="{color: item.iconColor}" />
                  </div>
                  <div class="pe-info">
                    <span class="pe-name">{{ item.name }}</span>
                    <span class="pe-desc">{{ item.desc }}</span>
                  </div>
                  <i class="el-icon-arrow-right pe-arrow" />
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-col>

      <!-- 项目实施 -->
      <el-col :span="7">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-s-operation" /> 项目实施</span>
            <span class="card-more" @click="jumpTo('/project/index')">实施方案 →</span>
          </div>
          <div class="impl-list">
            <div v-for="item in implItems" :key="item.name" class="impl-entry" @click="jumpTo(item.route)">
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
        </div>
      </el-col>

      <!-- 审计整改 -->
      <el-col :span="7">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-circle-check" /> 审计整改</span>
            <span class="card-more" @click="jumpTo('/rectify/wtda')">问题清单 →</span>
          </div>
          <div class="rectify-list">
            <div v-for="item in rectifyItems" :key="item.name" class="rect-entry" @click="jumpTo(item.route)">
              <div class="re-dot" :style="{background: item.color}"></div>
              <span class="re-name">{{ item.name }}</span>
              <span class="re-count" v-if="item.count" :style="{color: item.color}">{{ item.count }}</span>
            </div>
          </div>
          <div class="rectify-progress">
            <div class="rp-row" v-for="rp in rectifyProgress" :key="rp.label">
              <span class="rp-label">{{ rp.label }}</span>
              <div class="rp-bar-bg"><div class="rp-bar-fill" :style="{width: rp.percent + '%', background: rp.color}" /></div>
              <span class="rp-val">{{ rp.percent }}%</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- ========== 审计报告 + 审计档案 + 质量评议 ========== -->
    <el-row :gutter="14" class="content-row">
      <el-col :span="8">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-document" /> 审计报告</span>
            <span class="card-more" @click="jumpTo('/report/suggest')">报告定稿 →</span>
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
            <span class="card-more" @click="jumpTo('/implement/package')">项目归档 →</span>
          </div>
          <div class="archive-grid">
            <div v-for="a in archiveItems" :key="a.name" class="archive-card" @click="jumpTo(a.route)">
              <div class="ac-icon" :style="{background: a.bgColor}">
                <i :class="a.icon" :style="{color: a.iconColor}" />
              </div>
              <div class="ac-name">{{ a.name }}</div>
            </div>
          </div>
        </div>
      </el-col>

      <el-col :span="8">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-star-off" /> 质量评议</span>
          </div>
          <div class="quality-grid">
            <div v-for="q in qualityItems" :key="q.name" class="quality-card" @click="jumpTo(q.route)">
              <div class="qc-icon" :style="{background: q.bgColor}">
                <i :class="q.icon" :style="{color: q.iconColor}" />
              </div>
              <div class="qc-body">
                <div class="qc-name">{{ q.name }}</div>
                <div class="qc-desc">{{ q.desc }}</div>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- ========== 功能模块 + 综合管理 ========== -->
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
            <span class="card-title"><i class="el-icon-s-tools" /> 综合管理与配置</span>
          </div>
          <div class="zhgl-grid">
            <div class="zhgl-section">
              <div class="zs-title">综合管理</div>
              <div v-for="z in zhglItems" :key="z.name" class="zs-item" @click="jumpTo(z.route)">
                <i :class="z.icon" /><span>{{ z.name }}</span>
              </div>
            </div>
            <div class="zhgl-section">
              <div class="zs-title">基础配置</div>
              <div v-for="b in baseConfigItems" :key="b.name" class="zs-item" @click="jumpTo(b.route)">
                <i :class="b.icon" /><span>{{ b.name }}</span>
              </div>
            </div>
            <div class="zhgl-section">
              <div class="zs-title">资料收集</div>
              <div v-for="d in dataCollectItems" :key="d.name" class="zs-item" @click="jumpTo(d.route)">
                <i :class="d.icon" /><span>{{ d.name }}</span>
              </div>
            </div>
            <div class="zhgl-section">
              <div class="zs-title">工程项目</div>
              <div v-for="g in engItems" :key="g.name" class="zs-item" @click="jumpTo(g.route)">
                <i :class="g.icon" /><span>{{ g.name }}</span>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- ========== 审计数据 + 消息中心 ========== -->
    <el-row :gutter="14" class="content-row">
      <el-col :span="16">
        <div class="card-panel">
          <div class="card-header">
            <span class="card-title"><i class="el-icon-data-analysis" /> 审计数据与项目评优</span>
          </div>
          <div class="data-grid">
            <div v-for="d in dataItems" :key="d.name" class="data-card" @click="jumpTo(d.route)">
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
            <span class="card-title"><i class="el-icon-bell" /> 快捷入口</span>
          </div>
          <div class="quick-grid">
            <div v-for="q in quickItems" :key="q.name" class="quick-card" @click="jumpTo(q.route)">
              <div class="qk-icon" :style="{background: q.bgColor}">
                <i :class="q.icon" :style="{color: q.iconColor}" />
              </div>
              <div class="qk-name">{{ q.name }}</div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { investThemeMixin } from '@/views/stateAssets/themeMixin'
import { getProjectStageCount, getRectifyStatistics, getIssueList, getRectifyList, getFollowUpList, getSubsequentList, getQualityList } from '@/api/oilAudit/smartHome'

export default {
  name: 'SmartAuditHome',
  mixins: [investThemeMixin],
  data() {
    return {
      userName: '',
      planActive: 'demand',
      welcomeStats: [
        { label: '实施项目', value: '--', color: '#1890FF' },
        { label: '整改完成', value: '--', color: '#52C41A' },
        { label: '审计报告', value: '--', color: '#722ED1' },
        { label: '质量评议', value: '--', color: '#FA8C16' },
        { label: '问题清单', value: '--', color: '#FF4D4F' },
      ],
      kpiCards: [
        { label: '实施项目数', value: '--', unit: '个', icon: 'el-icon-s-order', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', barBg: 'rgba(24,144,255,0.1)', barColor: '#1890FF', barWidth: 0, trend: 0, theme: '' },
        { label: '整改完成率', value: '--', unit: '%', icon: 'el-icon-circle-check', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', barBg: 'rgba(82,196,26,0.1)', barColor: '#52C41A', barWidth: 0, trend: 0, theme: '' },
        { label: '审计报告', value: '--', unit: '份', icon: 'el-icon-document', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', barBg: 'rgba(114,46,209,0.1)', barColor: '#722ED1', barWidth: 0, trend: 0, theme: '' },
        { label: '质量评议分', value: '--', unit: '分', icon: 'el-icon-star-off', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', barBg: 'rgba(250,140,22,0.1)', barColor: '#FA8C16', barWidth: 0, trend: 0, theme: 'kpi-warning' },
        { label: '问题清单', value: '--', unit: '项', icon: 'el-icon-warning-outline', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', barBg: 'rgba(255,77,79,0.1)', barColor: '#FF4D4F', barWidth: 0, trend: 0, theme: 'kpi-danger' },
        { label: '底稿完成率', value: '--', unit: '%', icon: 'el-icon-notebook-2', iconBg: 'rgba(19,194,194,0.1)', iconColor: '#13C2C2', barBg: 'rgba(19,194,194,0.1)', barColor: '#13C2C2', barWidth: 0, trend: 0, theme: '' },
      ],
      // ===== 全链路流程图拓扑数据 =====
      flowConfig: { colW: 132, rowH: 96, nodeW: 96, nodeH: 60, padX: 20, padY: 16 },
      flowNodes: [
        // 主干流程（row 2）
        { id: 'plan', name: '计划编制', col: 0, row: 2, icon: 'el-icon-date', kind: 'main', route: '' },
        { id: 'launch', name: '项目启动', col: 1, row: 2, icon: 'el-icon-s-flag', kind: 'main', route: '/project/xmqd' },
        { id: 'notice', name: '审计通知', col: 2, row: 2, icon: 'el-icon-message', kind: 'main', route: '/project/notice' },
        { id: 'impl', name: '审计实施', col: 3, row: 2, icon: 'el-icon-s-operation', kind: 'main', route: '/project/index', badge: '' },
        { id: 'draft', name: '底稿管理', col: 4, row: 2, icon: 'el-icon-notebook-2', kind: 'main', route: '/implement/implement/draftManage' },
        { id: 'report', name: '审计报告', col: 5, row: 2, icon: 'el-icon-document', kind: 'main', route: '/zlpy/sjbg' },
        { id: 'rectify', name: '审计整改', col: 6, row: 2, icon: 'el-icon-circle-check', kind: 'warn', route: '/rectify/wtda', alert: true, badge: '' },
        { id: 'archive', name: '项目归档', col: 7, row: 2, icon: 'el-icon-folder-opened', kind: 'main', route: '/implement/package' },

        // 计划支撑层（row 1）
        { id: 'demand', name: '需求建议', col: 0, row: 1, icon: 'el-icon-s-order', kind: 'plan', route: '/jhlx/xqjyb' },
        { id: 'lxjy', name: '立项建议', col: 1, row: 1, icon: 'el-icon-document-add', kind: 'plan', route: '/jhlx/lxjyb' },
        { id: 'jhdg', name: '计划定稿', col: 2, row: 1, icon: 'el-icon-document-checked', kind: 'plan', route: '/jhlx/jhlist' },
        { id: 'jhba', name: '计划备案', col: 3, row: 1, icon: 'el-icon-folder-checked', kind: 'plan', route: '/jhlx/jhba' },
        { id: 'gzfa', name: '工作方案', col: 4, row: 1, icon: 'el-icon-document', kind: 'plan', route: '/project/gzfa' },
        { id: 'sqdcbg', name: '审前调查', col: 5, row: 1, icon: 'el-icon-search', kind: 'plan', route: '/project/sqdcbg' },

        // AI 审计层（row 0）
        { id: 'cockpit', name: '审计驾驶舱', col: 1, row: 0, icon: 'el-icon-data-board', kind: 'ai', route: '/jcdn/zcfzfx' },
        { id: 'agent', name: '审计智能体', col: 2, row: 0, icon: 'el-icon-cpu', kind: 'ai', route: '/jcdn/AIAgent' },
        { id: 'sjzl', name: '审计总览', col: 3, row: 0, icon: 'el-icon-data-analysis', kind: 'ai', route: '/jcdn/cwzl' },

        // 质量评议层（row 3）
        { id: 'zlpy', name: '质量评议', col: 2, row: 3, icon: 'el-icon-star-off', kind: 'quality', route: '/zlpy/zlpy' },
        { id: 'dgpy', name: '底稿评议', col: 3, row: 3, icon: 'el-icon-notebook-2', kind: 'quality', route: '/zlpy/sjdg' },
        { id: 'fapy', name: '方案评议', col: 4, row: 3, icon: 'el-icon-s-operation', kind: 'quality', route: '/zlpy/sjssfa' },
        { id: 'bgpy', name: '报告评议', col: 5, row: 3, icon: 'el-icon-document', kind: 'quality', route: '/zlpy/sjbg' },
        { id: 'jcss', name: '奖惩实施', col: 6, row: 3, icon: 'el-icon-medal', kind: 'quality', route: '/zlpy/jcss' },
        { id: 'jfkp', name: '积分考评', col: 7, row: 3, icon: 'el-icon-trophy', kind: 'quality', route: '/zlpy/jfkp' },

        // 工程/离任/督导层（row 4）
        { id: 'eng', name: '工程审计', col: 0, row: 4, icon: 'el-icon-office-building', kind: 'eng', route: '/jhlx/gcjgysjh' },
        { id: 'resign', name: '离任审计', col: 1, row: 4, icon: 'el-icon-user', kind: 'eng', route: '/jhlx/lrjjzrsq' },
        { id: 'guide', name: '督导任务', col: 2, row: 4, icon: 'el-icon-s-check', kind: 'eng', route: '/implement/sjddjl' },
        { id: 'collect', name: '资料收集', col: 3, row: 4, icon: 'el-icon-download', kind: 'eng', route: '/dataCollection/tjzl' },
        { id: 'zhgl', name: '综合管理', col: 4, row: 4, icon: 'el-icon-s-tools', kind: 'eng', route: '/zhgl/nbwzsqd' },

        // 基础配置层（row 5）
        { id: 'sjlx', name: '审计类型', col: 0, row: 5, icon: 'el-icon-setting', kind: 'config', route: '/configure/category' },
        { id: 'sjzy', name: '审计指引', col: 1, row: 5, icon: 'el-icon-guide', kind: 'config', route: '/configure/audit' },
        { id: 'mbgl', name: '模板管理', col: 2, row: 5, icon: 'el-icon-files', kind: 'config', route: '/configure/mbgl' },
        { id: 'pfgl', name: '评分管理', col: 3, row: 5, icon: 'el-icon-s-data', kind: 'config', route: '/zlpy/pfgl' },
        { id: 'gcsjlx', name: '工程审计类型', col: 4, row: 5, icon: 'el-icon-collection-tag', kind: 'config', route: '/configure/gcsjlx' },
        { id: 'gcsjmb', name: '工程审计模板', col: 5, row: 5, icon: 'el-icon-document-copy', kind: 'config', route: '/configure/gcsjmb' },
      ],
      flowEdges: [
        // 主干流程（实线主流程）
        ['plan', 'launch', 'main'], ['launch', 'notice', 'main'], ['notice', 'impl', 'main'],
        ['impl', 'draft', 'main'], ['draft', 'report', 'main'], ['report', 'rectify', 'main'],
        ['rectify', 'archive', 'main'],
        // 计划支撑 → 主干
        ['demand', 'plan', 'plan'], ['lxjy', 'launch', 'plan'],
        ['jhdg', 'notice', 'plan'], ['jhba', 'impl', 'plan'],
        ['gzfa', 'draft', 'plan'], ['sqdcbg', 'report', 'plan'],
        // 计划支撑横向
        ['demand', 'lxjy', 'plan'], ['lxjy', 'jhdg', 'plan'], ['jhdg', 'jhba', 'plan'],
        // AI 审计 → 主干
        ['cockpit', 'impl', 'ai'], ['agent', 'impl', 'ai'], ['sjzl', 'impl', 'ai'],
        // 主干 → 质量评议
        ['impl', 'zlpy', 'quality'], ['draft', 'dgpy', 'quality'],
        ['impl', 'fapy', 'quality'], ['report', 'bgpy', 'quality'],
        ['rectify', 'jcss', 'quality'],
        // 质量评议横向
        ['zlpy', 'dgpy', 'quality'], ['dgpy', 'fapy', 'quality'],
        ['fapy', 'bgpy', 'quality'], ['bgpy', 'jcss', 'quality'], ['jcss', 'jfkp', 'quality'],
        // 主干 → 工程/督导
        ['impl', 'guide', 'eng'], ['plan', 'eng', 'eng'], ['plan', 'resign', 'eng'],
        ['impl', 'collect', 'eng'],
      ],
      planDemandItems: [
        { name: '需求建议表', desc: '审计需求建议征集', icon: 'el-icon-s-order', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/jhlx/xqjyb' },
        { name: '服务需求表', desc: '审计服务需求申报', icon: 'el-icon-service', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/jhlx/fwxqb' },
        { name: '分管领导汇总', desc: '领导审计需求汇总', icon: 'el-icon-s-custom', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/jhlx/fgldhz' },
        { name: '计划需求', desc: '年度审计计划需求', icon: 'el-icon-date', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/jhlx/jhxq' },
        { name: '工程审计项目安排', desc: '工程项目审计安排', icon: 'el-icon-office-building', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/project/gcsjxmap' },
        { name: '财务审计项目安排', desc: '财务审计项目安排', icon: 'el-icon-coin', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/project/cwsjxmap' },
      ],
      planProjectItems: [
        { name: '项目启动', desc: '审计项目启动审批', icon: 'el-icon-s-flag', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/project/xmqd' },
        { name: '项目延期申请', desc: '审计项目延期申请', icon: 'el-icon-time', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/project/xmyqsq' },
        { name: '工作方案', desc: '审计工作方案编制', icon: 'el-icon-document', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/project/gzfa' },
        { name: '审前调查报告', desc: '审前调查报告编写', icon: 'el-icon-search', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/project/sqdcbg' },
      ],
      implItems: [
        { name: '实施方案', desc: '审计实施方案编制', icon: 'el-icon-s-operation', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/project/index' },
        { name: '任务分配', desc: '审计任务分配管理', icon: 'el-icon-s-order', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/project/task' },
        { name: '我的任务', desc: '我的审计任务列表', icon: 'el-icon-user', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', badge: '', route: '/implement/wdrw' },
        { name: '底稿管理', desc: '审计底稿编制审核', icon: 'el-icon-notebook-2', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/implement/implement/draftManage' },
        { name: '我的底稿', desc: '个人底稿管理', icon: 'el-icon-edit-outline', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/implement/myDraft' },
        { name: '我的工程任务', desc: '工程审计任务', icon: 'el-icon-office-building', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/implement/task' },
      ],
      rectifyItems: [
        { name: '问题清单', color: '#FF4D4F', count: '--', route: '/rectify/wtda' },
        { name: '问题整改', color: '#FA8C16', count: '--', route: '/rectify/wtzg' },
        { name: '跟踪回访', color: '#1890FF', count: '--', route: '/rectify/gzhf' },
        { name: '后续整改', color: '#722ED1', count: '--', route: '/rectify/hxzg' },
      ],
      rectifyProgress: [
        { label: '问题整改率', percent: 0, color: '#52C41A' },
        { label: '跟踪回访率', percent: 0, color: '#1890FF' },
        { label: '后续整改率', percent: 0, color: '#722ED1' },
      ],
      reportItems: [
        { name: '审计报告定稿', desc: '审计报告最终审定', icon: 'el-icon-document', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/report/suggest' },
        { name: '交换意见稿', desc: '审计意见交换稿', icon: 'el-icon-chat-dot-round', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/report/custom' },
        { name: '审计建议', desc: '审计建议书编制', icon: 'el-icon-s-order', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/rectify/sjjy' },
        { name: '审理报告', desc: '审计审理报告', icon: 'el-icon-document-checked', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/report/slbg' },
        { name: '经济责任审计结果报告', desc: '经济责任审计结果', icon: 'el-icon-s-finance', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/report/jjzrsjjgbg' },
        { name: '质量分析报告', desc: '审计质量分析', icon: 'el-icon-data-analysis', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/implement/zlfxbg' },
      ],
      archiveItems: [
        { name: '项目归档', icon: 'el-icon-folder-opened', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/implement/package' },
        { name: '项目查看', icon: 'el-icon-view', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/implement/look' },
      ],
      qualityItems: [
        { name: '质量评议', desc: '审计质量评议评价', icon: 'el-icon-star-off', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/zlpy/zlpy' },
        { name: '审计底稿评议', desc: '底稿质量评议', icon: 'el-icon-notebook-2', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/zlpy/sjdg' },
        { name: '实施方案评议', desc: '方案质量评议', icon: 'el-icon-s-operation', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/zlpy/sjssfa' },
        { name: '审计报告评议', desc: '报告质量评议', icon: 'el-icon-document', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/zlpy/sjbg' },
        { name: '系统上线评议', desc: '管理系统上线评估', icon: 'el-icon-s-platform', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/zlpy/sjglxtsx' },
        { name: '奖惩实施', desc: '审计奖惩实施', icon: 'el-icon-medal', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/zlpy/jcss' },
      ],
      modules: [
        { name: '计划编制', icon: 'el-icon-date', desc: '需求/计划/安排', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', route: '/jhlx/jhxq' },
        { name: '项目管理', icon: 'el-icon-s-order', desc: '方案/任务/分配', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', route: '/project/index' },
        { name: '审计实施', icon: 'el-icon-s-operation', desc: '底稿/任务/记录', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', route: '/implement/wdrw' },
        { name: '审计整改', icon: 'el-icon-circle-check', desc: '问题/整改/回访', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', route: '/rectify/wtda' },
        { name: '审计报告', icon: 'el-icon-document', desc: '定稿/意见/建议', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', route: '/report/suggest' },
        { name: '质量评议', icon: 'el-icon-star-off', desc: '评议/奖惩/评估', iconBg: 'rgba(235,47,150,0.1)', iconColor: '#EB2F96', route: '/zlpy/zlpy' },
        { name: '项目评优', icon: 'el-icon-trophy', desc: '申报/评优/评估', iconBg: 'rgba(255,77,79,0.1)', iconColor: '#FF4D4F', route: '/XMPY/xmpysb' },
        { name: '工程项目', icon: 'el-icon-office-building', desc: '竣工/造价/结算', iconBg: 'rgba(24,144,255,0.1)', iconColor: '#1890FF', route: '/jhlx/gcjgysjh' },
        { name: '离任审计', icon: 'el-icon-user', desc: '离任/任中审计', iconBg: 'rgba(114,46,209,0.1)', iconColor: '#722ED1', route: '/jhlx/lrjjzrsq' },
        { name: '资料收集', icon: 'el-icon-download', desc: '提交/调查/采集', iconBg: 'rgba(82,196,26,0.1)', iconColor: '#52C41A', route: '/dataCollection/tjzl' },
        { name: '消息中心', icon: 'el-icon-bell', desc: '系统消息与通知', iconBg: 'rgba(250,140,22,0.1)', iconColor: '#FA8C16', route: '/home/PMessage' },
      ],
      zhglItems: [
        { name: '内部网站信息发布', icon: 'el-icon-s-order', route: '/zhgl/nbwzsqd' },
        { name: '人员请假单', icon: 'el-icon-date', route: '/zhgl/ryqjd' },
        { name: '销假单', icon: 'el-icon-circle-check', route: '/zhgl/xjd' },
        { name: '办公经费申请', icon: 'el-icon-coin', route: '/zhgl/bgjfzc' },
        { name: '外派任务', icon: 'el-icon-s-flag', route: '/zhgl/wpsqd' },
        { name: '督办通知单', icon: 'el-icon-bell', route: '/zhgl/dbtzd' },
      ],
      baseConfigItems: [
        { name: '审计类型维护', icon: 'el-icon-setting', route: '/configure/category' },
        { name: '拟实施审计指引', icon: 'el-icon-guide', route: '/configure/audit' },
        { name: '审计指引模板库', icon: 'el-icon-collection', route: '/configure/experience' },
        { name: 'VPN账号管理', icon: 'el-icon-key', route: '/zhgl/vpn' },
        { name: '权限申请', icon: 'el-icon-lock', route: '/zhgl/qxsq' },
        { name: '数字证书管理', icon: 'el-icon-certificate', route: '/zhgl/szzsgl' },
      ],
      dataCollectItems: [
        { name: '提交资料', icon: 'el-icon-upload2', route: '/dataCollection/tjzl' },
        { name: '调查表', icon: 'el-icon-s-order', route: '/dataCollection/dcb' },
        { name: '工程结算付款统计', icon: 'el-icon-coin', route: '/dataCollection/fkqktj' },
        { name: '工程结算项目信息', icon: 'el-icon-office-building', route: '/dataCollection/xmxxqk' },
      ],
      engItems: [
        { name: '工程竣工验收计划', icon: 'el-icon-s-order', route: '/jhlx/gcjgysjh' },
        { name: '建设项目投资完成', icon: 'el-icon-coin', route: '/jhlx/jsxmtzwcqk' },
        { name: '建设项目基本情况', icon: 'el-icon-office-building', route: '/jhlx/jsxmjbqk' },
        { name: '工程项目造价', icon: 'el-icon-s-finance', route: '/jhlx/gcxmzj' },
        { name: '工程项目造价中间表', icon: 'el-icon-data-line', route: '/jhlx/gcxmzjzjb' },
      ],
      dataItems: [
        { name: '审计工作记录', desc: '审计工作过程记录', icon: 'el-icon-notebook-2', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/implement/sjgzjl' },
        { name: '审计结果确认单', desc: '审计结果确认签章', icon: 'el-icon-circle-check', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/implement/sjjgqrd' },
        { name: '审计督导任务', desc: '审计督导检查记录', icon: 'el-icon-s-operation', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/implement/sjddjl' },
        { name: '审计项目运行情况表', desc: '项目运行状态汇总', icon: 'el-icon-data-analysis', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/implement/sjxmqkb' },
        { name: '通知变更', desc: '审计通知书变更', icon: 'el-icon-edit', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/project/tzbg' },
        { name: '未委托及预计离任', desc: '离任审计人员跟踪', icon: 'el-icon-user', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/jhlx/wwtjyjlr' },
        { name: '审计承诺书', desc: '审计人员承诺签署', icon: 'el-icon-document-checked', bgColor: 'rgba(235,47,150,0.08)', iconColor: '#EB2F96', route: '/implement/sjcns' },
        { name: '现场审查主要内容', desc: '现场审查要点清单', icon: 'el-icon-search', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/implement/xcsczynr' },
      ],
      quickItems: [
        { name: '审计通知书', icon: 'el-icon-document', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/project/notice' },
        { name: '任务清单', icon: 'el-icon-s-order', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/implement/guide' },
        { name: '审理工作记录', icon: 'el-icon-edit-outline', bgColor: 'rgba(255,77,79,0.08)', iconColor: '#FF4D4F', route: '/report/slgzjl' },
        { name: '审计项目追款', icon: 'el-icon-coin', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/implement/sjxmzk' },
        { name: '印信使用单', icon: 'el-icon-certificate', bgColor: 'rgba(235,47,150,0.08)', iconColor: '#EB2F96', route: '/zhgl/yxsyd' },
        { name: '资产调剂申请', icon: 'el-icon-sort', bgColor: 'rgba(24,144,255,0.08)', iconColor: '#1890FF', route: '/zhgl/zctjsq' },
        { name: 'IP地址申请', icon: 'el-icon-monitor', bgColor: 'rgba(82,196,26,0.08)', iconColor: '#52C41A', route: '/zhgl/ip' },
        { name: '中石油邮箱', icon: 'el-icon-message', bgColor: 'rgba(114,46,209,0.08)', iconColor: '#722ED1', route: '/zhgl/zsyyxgl' },
        { name: '修理费支出', icon: 'el-icon-s-order', bgColor: 'rgba(250,140,22,0.08)', iconColor: '#FA8C16', route: '/zhgl/xlfzc' },
        { name: '外网代理服务', icon: 'el-icon-connection', bgColor: 'rgba(19,194,194,0.08)', iconColor: '#13C2C2', route: '/zhgl/wwdlfwgl' },
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
        plan: 'url(#fl-arrow-plan)',
        quality: 'url(#fl-arrow-quality)',
        ai: 'url(#fl-arrow-ai)',
        eng: 'url(#fl-arrow-eng)',
        sub: 'url(#fl-arrow)',
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
    /** 调用已有后端接口聚合首页数据（全部来源数据库） */
    async fetchHomeData() {
      try {
        const [stageRes, issueRes, rectifyRes, followUpRes, subsequentRes, qualityRes] = await Promise.allSettled([
          getProjectStageCount(),
          getIssueList(),
          getRectifyList(),
          getFollowUpList(),
          getSubsequentList(),
          getQualityList(),
        ])

        // 1. 项目阶段统计
        const stageVal = stageRes.status === 'fulfilled' ? stageRes.value : null
        if (stageVal && stageVal.code === 1 && stageVal.data && stageVal.data.data) {
          const d = stageVal.data.data
          const sqzb = d.sqzbNum || 0
          const xcss = d.xcssNum || 0
          const sjbg = d.sjbgNum || 0
          const sjzg = d.sjzgNum || 0
          const ywc = d.ywcNum || 0
          const totalProjects = sqzb + xcss + sjbg + sjzg + ywc
          const rectifyRate = (sjzg + ywc) > 0 ? Math.round(ywc / (sjzg + ywc) * 1000) / 10 : 0
          const draftRate = totalProjects > 0 ? Math.round((sjbg + sjzg + ywc) / totalProjects * 1000) / 10 : 0

          this.$set(this.welcomeStats, 0, { ...this.welcomeStats[0], value: totalProjects + '个' })
          this.$set(this.welcomeStats, 1, { ...this.welcomeStats[1], value: rectifyRate + '%' })
          this.$set(this.welcomeStats, 2, { ...this.welcomeStats[2], value: sjbg + '份' })
          this.$set(this.kpiCards, 0, { ...this.kpiCards[0], value: String(totalProjects), barWidth: Math.min(totalProjects / 100, 100) })
          this.$set(this.kpiCards, 1, { ...this.kpiCards[1], value: String(rectifyRate), barWidth: rectifyRate })
          this.$set(this.kpiCards, 2, { ...this.kpiCards[2], value: String(sjbg), barWidth: Math.min(sjbg, 100) })
          this.$set(this.kpiCards, 5, { ...this.kpiCards[5], value: String(draftRate), barWidth: draftRate })
          const implNode = this.flowNodes.find(n => n.id === 'impl')
          if (implNode) implNode.badge = sqzb > 0 ? String(sqzb) : ''
        }

        // 2. 问题清单总数
        const issueVal = issueRes.status === 'fulfilled' ? issueRes.value : null
        const issueTotal = this._extractTotal(issueVal)
        this.$set(this.welcomeStats, 4, { ...this.welcomeStats[4], value: issueTotal + '项' })
        this.$set(this.kpiCards, 4, { ...this.kpiCards[4], value: String(issueTotal), barWidth: Math.min(issueTotal, 100) })
        this.$set(this.rectifyItems, 0, { ...this.rectifyItems[0], count: String(issueTotal) })

        // 3. 问题整改总数
        const rectifyVal = rectifyRes.status === 'fulfilled' ? rectifyRes.value : null
        const rectifyTotal = this._extractTotal(rectifyVal)
        this.$set(this.rectifyItems, 1, { ...this.rectifyItems[1], count: String(rectifyTotal) })

        // 4. 跟踪回访总数
        const followUpVal = followUpRes.status === 'fulfilled' ? followUpRes.value : null
        const followUpTotal = this._extractTotal(followUpVal)
        this.$set(this.rectifyItems, 2, { ...this.rectifyItems[2], count: String(followUpTotal) })

        // 5. 后续整改总数
        const subsequentVal = subsequentRes.status === 'fulfilled' ? subsequentRes.value : null
        const subsequentTotal = this._extractTotal(subsequentVal)
        this.$set(this.rectifyItems, 3, { ...this.rectifyItems[3], count: String(subsequentTotal) })

        // 6. 整改进度百分比（基于真实数据计算）
        const rectifyPercent = issueTotal > 0 ? Math.round(rectifyTotal / issueTotal * 100) : 0
        const followUpPercent = rectifyTotal > 0 ? Math.round(followUpTotal / rectifyTotal * 100) : 0
        const subsequentPercent = followUpTotal > 0 ? Math.round(subsequentTotal / followUpTotal * 100) : 0
        this.$set(this.rectifyProgress, 0, { ...this.rectifyProgress[0], percent: rectifyPercent })
        this.$set(this.rectifyProgress, 1, { ...this.rectifyProgress[1], percent: followUpPercent })
        this.$set(this.rectifyProgress, 2, { ...this.rectifyProgress[2], percent: subsequentPercent })

        // 7. 流程图整改节点badge（未整改数 = 问题总数 - 已整改数）
        const rectNode = this.flowNodes.find(n => n.id === 'rectify')
        const pending = issueTotal - rectifyTotal
        if (rectNode) rectNode.badge = pending > 0 ? String(pending) : ''

        // 8. 质量评议
        const qualityVal = qualityRes.status === 'fulfilled' ? qualityRes.value : null
        const qualityTotal = this._extractTotal(qualityVal)
        this.$set(this.welcomeStats, 3, { ...this.welcomeStats[3], value: String(qualityTotal) })
        this.$set(this.kpiCards, 3, { ...this.kpiCards[3], value: String(qualityTotal), barWidth: Math.min(qualityTotal, 100) })
      } catch (e) {
        // 静默降级
      }
    },
    /** 从分页接口响应中提取 totalRecord */
    _extractTotal(res) {
      if (!res || res.code !== 1 || !res.data) return 0
      const d = res.data
      if (d.pageInfo && d.pageInfo.totalRecord != null) return d.pageInfo.totalRecord
      if (d.totalRecord != null) return d.totalRecord
      if (d.total != null) return d.total
      if (Array.isArray(d.list)) return d.list.length
      if (Array.isArray(d)) return d.length
      return 0
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
.greeting { display: flex; align-items: center; gap: 6px; margin-bottom: 8px;
  .greeting-icon { font-size: 20px; color: #FAAD14; }
  .greeting-text { font-size: 14px; color: rgba(255,255,255,0.7); }
}
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
.fl-edge { stroke-width: 1.6; fill: none;
  &.edge-main { stroke: var(--ip-primary, #1890FF); stroke-width: 2.2; }
  &.edge-plan { stroke: #52C41A; stroke-dasharray: 4 3; }
  &.edge-quality { stroke: #FA8C16; stroke-dasharray: 4 3; }
  &.edge-ai { stroke: #13C2C2; stroke-dasharray: 4 3; }
  &.edge-eng { stroke: #722ED1; stroke-dasharray: 4 3; }
  &.edge-sub { stroke: #b8b8b8; stroke-dasharray: 4 3; }
}
.flow-pos-node {
  position: absolute; z-index: 2; display: flex; flex-direction: column; align-items: center; justify-content: center;
  background: #fff; border: 1.5px solid #e8e8e8; border-radius: 10px; cursor: pointer; transition: all 0.2s; box-shadow: 0 1px 4px rgba(0,0,0,.04);
  &:hover { transform: translateY(-2px) scale(1.05); box-shadow: 0 6px 18px rgba(0,0,0,.12); z-index: 3; }
  .fpn-icon-wrap { width: 26px; height: 26px; border-radius: 6px; display: flex; align-items: center; justify-content: center; margin-bottom: 2px; i { font-size: 14px; } }
  .fpn-label { font-size: 11px; font-weight: 600; color: #333; white-space: nowrap; line-height: 1.2; }
  .fpn-dot { position: absolute; top: -3px; right: -3px; width: 9px; height: 9px; border-radius: 50%; background: #FF4D4F; animation: pulse 1.5s infinite; }
  .fpn-badge { position: absolute; top: -7px; right: -7px; min-width: 18px; height: 16px; padding: 0 4px; border-radius: 8px; background: #FF4D4F; color: #fff; font-size: 10px; font-weight: 700; line-height: 16px; text-align: center; box-shadow: 0 1px 3px rgba(255,77,79,.4); }
  &.fpn-main { border-color: var(--ip-primary, #1890FF); border-width: 2px; background: #fff;
    .fpn-icon-wrap { background: rgba(var(--ip-primary-rgb,24,144,255),0.12); }
    .fpn-icon-wrap i { color: var(--ip-primary, #1890FF); } .fpn-label { color: var(--ip-primary, #1890FF); }
  }
  &.fpn-plan { border-color: #52C41A; background: #f6ffed;
    .fpn-icon-wrap { background: rgba(82,196,26,0.12); } .fpn-icon-wrap i { color: #52C41A; } .fpn-label { color: #389e0d; }
  }
  &.fpn-warn { border-color: #FA8C16; border-width: 2px; background: #FFFBE6;
    .fpn-icon-wrap { background: rgba(250,140,22,0.15); } .fpn-icon-wrap i { color: #FA8C16; } .fpn-label { color: #FA8C16; }
  }
  &.fpn-quality { border-color: #FA8C16; background: #fffbe6;
    .fpn-icon-wrap { background: rgba(250,140,22,0.12); } .fpn-icon-wrap i { color: #FA8C16; } .fpn-label { color: #d46b08; }
  }
  &.fpn-ai { border-color: #13C2C2; background: #e6fffb;
    .fpn-icon-wrap { background: rgba(19,194,194,0.15); } .fpn-icon-wrap i { color: #13C2C2; } .fpn-label { color: #08979C; }
  }
  &.fpn-eng { border-color: #722ED1; background: #f9f0ff;
    .fpn-icon-wrap { background: rgba(114,46,209,0.12); } .fpn-icon-wrap i { color: #722ED1; } .fpn-label { color: #531dab; }
  }
  &.fpn-config { border-color: #e8e8e8; background: #fafafa; border-style: dashed;
    .fpn-icon-wrap { background: #f0f0f0; } .fpn-icon-wrap i { color: #888; } .fpn-label { color: #666; font-weight: 500; }
  }
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

/* ========== 计划编制Tab ========== */
.inner-tabs { ::v-deep .el-tabs__header { margin-bottom: 8px; } ::v-deep .el-tabs__item { font-size: 13px; padding: 0 12px; } }
.plan-list { display: flex; flex-direction: column; gap: 6px; }
.plan-entry { display: flex; align-items: center; gap: 10px; padding: 8px 10px; border-radius: 8px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.15s;
  &:hover { border-color: var(--ip-primary); background: var(--ip-light-bg, #fafbff); }
  .pe-icon { width: 30px; height: 30px; border-radius: 6px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 14px; } }
  .pe-info { flex: 1; .pe-name { font-size: 13px; font-weight: 500; color: #333; } .pe-desc { font-size: 11px; color: #999; } }
  .pe-arrow { font-size: 12px; color: #d9d9d9; flex-shrink: 0; }
}

/* ========== 项目实施 ========== */
.impl-list { display: flex; flex-direction: column; gap: 6px; }
.impl-entry { display: flex; align-items: center; gap: 10px; padding: 8px 10px; border-radius: 8px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.15s;
  &:hover { border-color: var(--ip-primary); background: var(--ip-light-bg, #fafbff); }
  .ie-icon { width: 30px; height: 30px; border-radius: 6px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 14px; } }
  .ie-body { flex: 1; .ie-name { font-size: 13px; font-weight: 500; color: #333; } .ie-desc { font-size: 11px; color: #999; } }
  .ie-badge { ::v-deep .el-badge__content { top: 4px; } }
}

/* ========== 审计整改 ========== */
.rectify-list { display: flex; flex-direction: column; gap: 8px; margin-bottom: 12px; }
.rect-entry { display: flex; align-items: center; gap: 8px; padding: 8px 10px; border-radius: 8px; cursor: pointer; transition: background 0.15s;
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
}

/* ========== 质量评议 ========== */
.quality-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 8px; }
.quality-card { display: flex; align-items: center; gap: 8px; padding: 10px; border-radius: 8px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.15s;
  &:hover { border-color: var(--ip-primary); }
  .qc-icon { width: 28px; height: 28px; border-radius: 6px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 13px; } }
  .qc-body { .qc-name { font-size: 12px; font-weight: 500; color: #333; } .qc-desc { font-size: 10px; color: #999; } }
}

/* ========== 功能模块 ========== */
.module-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px; }
.module-card { display: flex; flex-direction: column; align-items: center; justify-content: center; padding: 14px 6px; border-radius: 10px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.2s; text-align: center;
  &:hover { border-color: var(--ip-primary); transform: translateY(-2px); box-shadow: 0 6px 16px rgba(var(--ip-primary-rgb),0.1); }
  .mc-icon { width: 36px; height: 36px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-bottom: 6px; i { font-size: 18px; } }
  .mc-name { font-size: 12px; font-weight: 600; color: #333; }
  .mc-desc { font-size: 10px; color: #bbb; margin-top: 2px; }
}

/* ========== 综合管理 ========== */
.zhgl-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 10px; }
.zhgl-section { padding: 10px; border-radius: 8px; border: 1px solid #f0f0f0; }
.zs-title { font-size: 12px; font-weight: 600; color: var(--ip-primary); margin-bottom: 8px; padding-bottom: 4px; border-bottom: 1px solid #f0f0f0; }
.zs-item { display: flex; align-items: center; gap: 6px; padding: 5px 4px; font-size: 12px; cursor: pointer; transition: color 0.15s;
  &:hover { color: var(--ip-primary); }
  i { font-size: 12px; color: #888; } span { color: #333; }
}

/* ========== 审计数据 ========== */
.data-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 10px; }
.data-card { display: flex; align-items: center; gap: 10px; padding: 12px; border-radius: 10px; border: 1px solid #e8e8e8; cursor: pointer; transition: all 0.2s;
  &:hover { border-color: var(--ip-bright); box-shadow: 0 4px 12px rgba(var(--ip-primary-rgb),0.12); transform: translateY(-2px); }
  .dc-icon { width: 36px; height: 36px; border-radius: 10px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; i { font-size: 16px; } }
  .dc-body { flex: 1; min-width: 0; .dc-name { font-size: 13px; font-weight: 600; color: #333; } .dc-desc { font-size: 11px; color: #999; margin-top: 2px; } }
  .dc-arrow { font-size: 14px; color: #d9d9d9; flex-shrink: 0; }
}

/* ========== 快捷入口 ========== */
.quick-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px; }
.quick-card { display: flex; flex-direction: column; align-items: center; padding: 14px 6px; border-radius: 10px; border: 1px solid #f0f0f0; cursor: pointer; transition: all 0.2s; text-align: center;
  &:hover { border-color: var(--ip-primary); transform: translateY(-2px); box-shadow: 0 6px 16px rgba(var(--ip-primary-rgb),0.1); }
  .qk-icon { width: 36px; height: 36px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-bottom: 6px; i { font-size: 18px; } }
  .qk-name { font-size: 11px; font-weight: 500; color: #333; }
}
</style>