<template>
  <div style="display:contents">
    <PenetrationScreen
      title="投资穿透大屏" titleIcon="el-icon-bank-card"
      :kpiCards="kpiCards" :pages="pages" :companyList="companyList"
      @goto="goTo" @close="$emit('close')" @view-change="onViewChange" @company-change="onCompanyChange"
      @kpi-click="onKpiClick"
    >
      <template #default>
        <!-- 投前/投中/投后/预警四阶段标题栏 -->
        <div class="phase-bar">
          <div class="phase-item phase-pre" @click="goTo('/investPenetration/compliance')">
            <i class="el-icon-edit-outline"></i> 投前决策
            <span class="phase-sub">合规审批</span>
          </div>
          <div class="phase-arrow"><i class="el-icon-arrow-right"></i></div>
          <div class="phase-item phase-mid" @click="goTo('/investPenetration/progressMonitor')">
            <i class="el-icon-s-order"></i> 投中监控
            <span class="phase-sub">进度追踪</span>
          </div>
          <div class="phase-arrow"><i class="el-icon-arrow-right"></i></div>
          <div class="phase-item phase-post" @click="goTo('/investPenetration/postEval')">
            <i class="el-icon-data-analysis"></i> 投后评价
            <span class="phase-sub">KPI达成</span>
          </div>
          <div class="phase-arrow"><i class="el-icon-arrow-right"></i></div>
          <div class="phase-item phase-warn" @click="goTo('/investPenetration/riskWarning')">
            <i class="el-icon-bell"></i> 全程预警
            <span class="phase-sub">风险管控</span>
            <span v-if="activeWarningCount > 0" class="warn-badge">{{ activeWarningCount }}</span>
          </div>
        </div>

        <!-- 主体：左侧图表区 + 右侧预警列表 -->
        <div class="screen-body">
          <!-- 左侧：3×3 图表网格 -->
          <div class="chart-grid">
            <!-- 第一行：投前决策 -->
            <div class="chart-panel phase-border-pre" @click="openDrawer('compliance')">
              <div class="chart-panel-title">
                <i class="el-icon-document-checked"></i> 合规状态分布
                <span class="phase-tag phase-tag-pre">投前</span>
                <span class="drill-hint">点击明细 →</span>
              </div>
              <div ref="chartCompliance" class="chart-box"></div>
            </div>
            <div class="chart-panel phase-border-pre" @click="openDrawer('typeDist')">
              <div class="chart-panel-title">
                <i class="el-icon-pie-chart"></i> 投资类型分布
                <span class="phase-tag phase-tag-pre">投前</span>
                <span class="drill-hint">点击明细 →</span>
              </div>
              <div ref="chartTypeDist" class="chart-box"></div>
            </div>
            <div class="chart-panel phase-border-mid" @click="openDrawer('progress')">
              <div class="chart-panel-title">
                <i class="el-icon-s-order"></i> 项目进度状态
                <span class="phase-tag phase-tag-mid">投中</span>
                <span class="drill-hint">点击明细 →</span>
              </div>
              <div ref="chartProgress" class="chart-box"></div>
            </div>

            <!-- 第二行：投中监控 + 投后评价 -->
            <div class="chart-panel phase-border-mid">
              <div class="chart-panel-title">
                <i class="el-icon-data-line"></i> 投资金额趋势
                <span class="phase-tag phase-tag-mid">投中</span>
              </div>
              <div ref="chartTrend" class="chart-box"></div>
            </div>
            <div class="chart-panel phase-border-post" @click="openDrawer('postEval')">
              <div class="chart-panel-title">
                <i class="el-icon-data-analysis"></i> KPI达成率分布
                <span class="phase-tag phase-tag-post">投后</span>
                <span class="drill-hint">点击明细 →</span>
              </div>
              <div ref="chartKpi" class="chart-box"></div>
            </div>
            <div class="chart-panel phase-border-post">
              <div class="chart-panel-title">
                <i class="el-icon-money"></i> 预期 vs 实际收益率
                <span class="phase-tag phase-tag-post">投后</span>
              </div>
              <div ref="chartReturn" class="chart-box"></div>
            </div>

            <!-- 第三行：预警 + 非主业 -->
            <div class="chart-panel phase-border-warn" @click="openDrawer('warnings')">
              <div class="chart-panel-title">
                <i class="el-icon-warning"></i> 预警等级分布
                <span class="phase-tag phase-tag-warn">预警</span>
                <span class="drill-hint">点击明细 →</span>
              </div>
              <div ref="chartWarnLevel" class="chart-box"></div>
            </div>
            <div class="chart-panel phase-border-warn" @click="openDrawer('warnType')">
              <div class="chart-panel-title">
                <i class="el-icon-s-flag"></i> 预警类型分布
                <span class="phase-tag phase-tag-warn">预警</span>
                <span class="drill-hint">点击明细 →</span>
              </div>
              <div ref="chartWarnType" class="chart-box"></div>
            </div>
            <div class="chart-panel phase-border-warn" @click="openDrawer('nonMainBiz')">
              <div class="chart-panel-title">
                <i class="el-icon-pie-chart"></i> 非主业投资占比
                <span class="phase-tag phase-tag-warn">预警</span>
                <span class="drill-hint">点击明细 →</span>
              </div>
              <div ref="chartNonMain" class="chart-box"></div>
            </div>
          </div>

          <!-- 右侧：实时预警滚动列表 -->
          <div class="warn-panel">
            <div class="warn-panel-title">
              <i class="el-icon-bell"></i> 实时预警
              <span class="warn-count-badge" v-if="activeWarningCount > 0">{{ activeWarningCount }}</span>
            </div>
            <div class="warn-scroll-list" ref="warnScrollList">
              <div v-for="(w, i) in warningScrollList" :key="i"
                   class="warn-scroll-item"
                   :class="'warn-level-' + w.level.toLowerCase()">
                <div class="wsi-header">
                  <el-tag :type="w.level === 'HIGH' ? 'danger' : w.level === 'MEDIUM' ? 'warning' : 'info'"
                          size="mini" effect="dark">
                    {{ w.level === 'HIGH' ? '高' : w.level === 'MEDIUM' ? '中' : '低' }}
                  </el-tag>
                  <span class="wsi-type">{{ WARNING_TYPE_MAP[w.warningType] || w.warningType }}</span>
                  <span class="wsi-time">{{ w.triggerTime }}</span>
                </div>
                <div class="wsi-body">
                  <span class="wsi-company">{{ w.companyName }}</span>
                  <span class="wsi-project">{{ w.projectName }}</span>
                </div>
                <div class="wsi-status">
                  <el-tag :type="w.status === 'PENDING' ? 'danger' : w.status === 'HANDLING' ? 'warning' : 'success'"
                          size="mini">
                    {{ w.status === 'PENDING' ? '待处理' : w.status === 'HANDLING' ? '处理中' : '已完成' }}
                  </el-tag>
                </div>
              </div>
              <div v-if="warningScrollList.length === 0" class="warn-empty">
                <i class="el-icon-check" style="color:#52C41A;font-size:20px"></i>
                <div>暂无活跃预警</div>
              </div>
            </div>
            <!-- 预警规则快览 -->
            <div class="rule-quick-view">
              <div class="rule-title"><i class="el-icon-info"></i> 预警规则（TOP 5）</div>
              <div v-for="r in topRules" :key="r.code" class="rule-item">
                <span class="rule-code">{{ r.code }}</span>
                <span class="rule-name">{{ r.name }}</span>
                <el-tag :type="r.level === 'HIGH' ? 'danger' : r.level === 'MEDIUM' ? 'warning' : 'info'"
                        size="mini">{{ r.levelLabel }}</el-tag>
              </div>
            </div>
          </div>
        </div>
      </template>
    </PenetrationScreen>

    <!-- 穿透抽屉 -->
    <el-drawer
      :title="drawer.title" :visible.sync="drawer.visible"
      direction="rtl" size="720px" :append-to-body="true"
      custom-class="screen-drawer"
    >
      <div class="drawer-body">
        <div class="drawer-goto" v-if="drawer.route">
          <el-button type="primary" size="small" icon="el-icon-right" @click="goTo(drawer.route)">
            前往{{ drawer.title }}页面查看完整数据
          </el-button>
        </div>

        <!-- 合规审批明细 -->
        <template v-if="drawer.type === 'compliance'">
          <el-table :data="drawer.list" size="small" style="width:100%" stripe>
            <el-table-column prop="projectName" label="项目名称" min-width="150" show-overflow-tooltip />
            <el-table-column prop="companyName" label="公司" width="100" />
            <el-table-column prop="decisionLevel" label="决策层级" width="90" />
            <el-table-column label="合规状态" width="90" align="center">
              <template #default="{ row }">
                <el-tag :type="{ COMPLIANT:'success', NON_COMPLIANT:'danger', PENDING:'warning' }[row.complianceStatus]" size="mini">
                  {{ { COMPLIANT:'合规', NON_COMPLIANT:'不合规', PENDING:'待审查' }[row.complianceStatus] || row.complianceStatus }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="越权标记" width="80" align="center">
              <template #default="{ row }">
                <el-tag v-if="row.overrideFlag === 'Y'" type="danger" size="mini">越权</el-tag>
                <span v-else style="color:#67c23a">正常</span>
              </template>
            </el-table-column>
          </el-table>
        </template>

        <!-- 投资类型明细 -->
        <template v-if="drawer.type === 'typeDist'">
          <el-table :data="drawer.list" size="small" style="width:100%" stripe>
            <el-table-column label="投资类型" width="110">
              <template #default="{ row }">{{ INVEST_TYPE_MAP[row.type] || row.type }}</template>
            </el-table-column>
            <el-table-column prop="count" label="项目数" width="80" />
            <el-table-column label="投资金额(亿)" width="120">
              <template #default="{ row }">{{ row.amount ? (row.amount / 10000).toFixed(1) : '-' }}</template>
            </el-table-column>
          </el-table>
        </template>

        <!-- 进度监控明细 -->
        <template v-if="drawer.type === 'progress'">
          <el-table :data="drawer.list" size="small" style="width:100%" stripe>
            <el-table-column prop="projectName" label="项目名称" min-width="140" show-overflow-tooltip />
            <el-table-column prop="companyName" label="公司" width="90" />
            <el-table-column label="整体进度" width="130">
              <template #default="{ row }">
                <el-progress :percentage="Number(row.actualProgress) || 0"
                  :color="(Number(row.actualProgress)||0) >= 80 ? '#52C41A' : (Number(row.actualProgress)||0) >= 60 ? '#FA8C16' : '#FF4D4F'"
                  :stroke-width="10" />
              </template>
            </el-table-column>
            <el-table-column label="逾期天数" width="80" align="center">
              <template #default="{ row }">
                <span v-if="row.delayDays > 0" style="color:#FF4D4F;font-weight:600">+{{ row.delayDays }}天</span>
                <span v-else style="color:#52C41A">按计划</span>
              </template>
            </el-table-column>
            <el-table-column label="状态" width="80" align="center">
              <template #default="{ row }">
                <el-tag :type="{ NORMAL:'success', DELAYED:'warning', SERIOUSLY_DELAYED:'danger', COMPLETED:'info' }[row.projectStatus]" size="mini">
                  {{ { NORMAL:'正常', DELAYED:'滞后', SERIOUSLY_DELAYED:'严重滞后', COMPLETED:'已完成' }[row.projectStatus] || row.projectStatus }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </template>

        <!-- 投后评价明细 -->
        <template v-if="drawer.type === 'postEval'">
          <el-table :data="drawer.list" size="small" style="width:100%" stripe>
            <el-table-column prop="projectName" label="项目名称" min-width="150" show-overflow-tooltip />
            <el-table-column prop="companyName" label="公司" width="100" />
            <el-table-column label="KPI达成率" width="130">
              <template #default="{ row }">
                <el-progress :percentage="Number(row.evalScore) || 0"
                  :color="(Number(row.evalScore)||0) >= 90 ? '#52C41A' : (Number(row.evalScore)||0) >= 70 ? '#FA8C16' : '#FF4D4F'"
                  :stroke-width="10" />
              </template>
            </el-table-column>
            <el-table-column label="预期收益率" width="100" align="right">
              <template #default="{ row }">{{ row.expectedReturn != null ? row.expectedReturn + '%' : '-' }}</template>
            </el-table-column>
            <el-table-column label="实际收益率" width="100" align="right">
              <template #default="{ row }">
                <span :style="{color: row.actualReturn < row.expectedReturn ? '#FF4D4F' : '#52C41A', fontWeight:600}">
                  {{ row.actualReturn != null ? row.actualReturn + '%' : '-' }}
                </span>
              </template>
            </el-table-column>
            <el-table-column label="评价结论" width="80" align="center">
              <template #default="{ row }">
                <el-tag :type="{ EXCELLENT:'success', GOOD:'', FAIR:'warning', POOR:'danger' }[row.evalConclusion]" size="mini">
                  {{ { EXCELLENT:'优秀', GOOD:'良好', FAIR:'一般', POOR:'较差' }[row.evalConclusion] || '-' }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </template>

        <!-- 预警明细 -->
        <template v-if="drawer.type === 'warnings' || drawer.type === 'warnType'">
          <el-table :data="drawer.list" size="small" style="width:100%" stripe>
            <el-table-column prop="companyName" label="公司" width="90" />
            <el-table-column prop="projectName" label="项目名称" min-width="140" show-overflow-tooltip />
            <el-table-column label="预警类型" width="100">
              <template #default="{ row }">{{ WARNING_TYPE_MAP[row.warningType] || row.warningType }}</template>
            </el-table-column>
            <el-table-column label="等级" width="75" align="center">
              <template #default="{ row }">
                <el-tag :type="levelTagType(row.level)" size="mini">{{ LEVEL_MAP[row.level] || row.level }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="状态" width="80" align="center">
              <template #default="{ row }">
                <el-tag :type="{ PENDING:'danger', HANDLING:'warning', COMPLETED:'success' }[row.status]" size="mini">
                  {{ { PENDING:'待处理', HANDLING:'处理中', COMPLETED:'已完成' }[row.status] || row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="triggerTime" label="触发时间" width="100" />
          </el-table>
        </template>

        <!-- 非主业明细 -->
        <template v-if="drawer.type === 'nonMainBiz'">
          <el-table :data="drawer.list" size="small" style="width:100%" stripe>
            <el-table-column prop="company" label="公司" width="120" />
            <el-table-column label="总投资额(亿)" width="110">
              <template #default="{ row }">{{ row.totalAmount ? (row.totalAmount / 10000).toFixed(1) : '-' }}</template>
            </el-table-column>
            <el-table-column label="非主业金额(亿)" width="120">
              <template #default="{ row }">{{ row.nonMainAmount ? (row.nonMainAmount / 10000).toFixed(1) : '-' }}</template>
            </el-table-column>
            <el-table-column label="非主业占比" width="100" align="center">
              <template #default="{ row }">
                <span :style="{ color: row.ratio > 30 ? '#FF4D4F' : row.ratio > 20 ? '#FA8C16' : '#52C41A', fontWeight:600 }">{{ row.ratio }}%</span>
              </template>
            </el-table-column>
          </el-table>
        </template>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import PenetrationScreen from '@/views/stateAssets/components/PenetrationScreen.vue'
import { getInvestDashboard, getInvestStatistics, getNonMainBizStats, getInvestWarningList } from '@/api/stateAssets/investPenetration'

// 项目状态中文映射
const PROJECT_STATUS_MAP = {
  totalProjects: '项目总数', avgReturnRate: '平均收益率', totalInvestAmount: '投资总额',
  riskProjects: '风险项目', pendingApproval: '待审批', approvedProjects: '已批准',
  IN_PROGRESS: '进行中', COMPLETED: '已完成', PENDING: '待启动', TERMINATED: '已终止',
}

const WARNING_TYPE_MAP = {
  PROGRESS_DELAY: '进度滞后', RETURN_DEVIATION: '收益偏离', COMPLIANCE: '合规问题',
  NON_MAIN_EXCEED: '非主业超标', EXIT_OVERDUE: '退出逾期', UNAUTHORIZED: '越权投资',
}
const LEVEL_MAP = { HIGH: '高风险', MEDIUM: '中风险', LOW: '低风险' }
const WARN_STATUS_MAP = { PENDING: '待处理', HANDLING: '处理中', COMPLETED: '已完成' }
const STATUS_MAP = { IN_PROGRESS: '进行中', COMPLETED: '已完成', PENDING: '待启动', TERMINATED: '已终止' }

export default {
  name: 'InvestPenetrationScreen',
  components: { PenetrationScreen },
  data() {
    return {
      STATUS_MAP, WARNING_TYPE_MAP, LEVEL_MAP, WARN_STATUS_MAP,
      kpiCards: [
        { label: '投资项目总数', value: '-', icon: 'el-icon-document', drawerType: 'projects' },
        { label: '投资总额(亿元)', value: '-', icon: 'el-icon-coin', drawerType: 'projects' },
        { label: '综合收益率(%)', value: '-', icon: 'el-icon-data-line', route: '/investPenetration/drillDown' },
        { label: '非主业占比(%)', value: '-', icon: 'el-icon-pie-chart', drawerType: 'nonMainBiz' },
        { label: '未解决预警', value: '-', icon: 'el-icon-bell', drawerType: 'warnings' },
        { label: '投后评价达标(%)', value: '-', icon: 'el-icon-circle-check', route: '/investPenetration/postEval' },
      ],
      pages: [
        { label: '投资穿透首页', route: '/investPenetration/home', icon: 'el-icon-s-home' },
        { label: '投资监控驾驶舱', route: '/investPenetration/dashboard', icon: 'el-icon-odometer' },
        { label: '投资项目台账', route: '/investPenetration/project', icon: 'el-icon-document' },
        { label: '投资穿透分析', route: '/investPenetration/drillDown', icon: 'el-icon-share' },
        { label: '投资决策合规追踪', route: '/investPenetration/compliance', icon: 'el-icon-s-check' },
        { label: '投后评价管理', route: '/investPenetration/postEval', icon: 'el-icon-data-analysis' },
        { label: '非主业投资分析', route: '/investPenetration/nonMainBiz', icon: 'el-icon-pie-chart' },
        { label: '风险预警管理', route: '/investPenetration/riskWarning', icon: 'el-icon-bell' },
        { label: '投资进度监控', route: '/investPenetration/progressMonitor', icon: 'el-icon-s-order' },
      ],
      companyList: [],
      chartPanels: [
        { ref: 'chart1', title: '投资项目状态分布', icon: 'el-icon-pie-chart', drawerType: 'projects' },
        { ref: 'chart2', title: '非主业投资占比', icon: 'el-icon-bar-chart', drawerType: 'nonMainBiz' },
        { ref: 'chart3', title: '预警等级分布', icon: 'el-icon-warning', drawerType: 'warnings' },
        { ref: 'chart4', title: '投资金额趋势', icon: 'el-icon-data-line', drawerType: null },
      ],
      _charts: [],
      _chartListeners: [],  // 记录 DOM 事件监听器，用于销毁时清理
      // 缓存列表数据供抽屉用
      _projectList: [],
      _warningList: [],
      _nonMainBizList: [],
      drawer: { visible: false, title: '', type: '', route: '', list: [] },
    }
  },
  mounted() { this.$nextTick(() => { setTimeout(() => this.loadData(), 300) }) },
  beforeDestroy() {
    this._charts.forEach(c => c.dispose())
    // 清理 DOM click 监听
    this._chartListeners && this._chartListeners.forEach(({ el, fn }) => el.removeEventListener('click', fn))
  },
  methods: {
    goTo(route) {
      if (!route) return
      this.$emit('close')
      this.$nextTick(() => { this.$router.push(route).catch(() => {}) })
    },
    onViewChange() { this.loadData() },
    onCompanyChange() { this.loadData() },
    async loadData() {
      await Promise.allSettled([this.loadKpi(), this.loadProjectStatus(), this.loadNonMainBiz(), this.loadWarnings()])
      this.loadTrendChart()
    },

    async loadKpi() {
      try {
        const res = await getInvestDashboard({})
        if (res && res.data) {
          const d = res.data
          // 填充 KPI 卡片
          if (d.kpi) {
            const k = d.kpi
            this.kpiCards[0].value = k.totalProjects || 0
            this.kpiCards[1].value = ((k.totalInvestAmount || 0) / 10000).toFixed(1)
            this.kpiCards[2].value = k.avgReturnRate || 0
            this.kpiCards[3].value = k.nonMainBizRatio || 0
            this.kpiCards[4].value = k.activeWarnings || 0
            this.kpiCards[5].value = k.postEvalPassRate || 0
          }
          // 填充公司下拉列表（接口返回字符串数组）
          if (Array.isArray(d.companyList)) {
            this.companyList = d.companyList
          }
          // 缓存项目进度列表供抽屉展示
          if (Array.isArray(d.projectProgress)) {
            this._projectList = d.projectProgress
          }
          // 缓存预警列表
          if (Array.isArray(d.warnings)) {
            this._warningList = d.warnings
          }
        }
      } catch (e) { console.error('[invest screen kpi]', e) }
    },
    async loadProjectStatus() {
      try {
        const res = await getInvestStatistics()
        const raw = (res && res.data) ? res.data : {}
        // 接口返回英文key统计值，用中文映射表转换
        const STAT_CN = {
          totalProjects: '项目总数', approvedProjects: '已批准',
          pendingApproval: '待审批', riskProjects: '风险项目',
        }
        const data = Object.entries(raw)
          .filter(([k]) => STAT_CN[k])
          .map(([k, v]) => ({ name: STAT_CN[k], value: Number(v) || 0 }))
        this.$nextTick(() => {
          this.initChart('chart1', {
            tooltip: { trigger: 'item', formatter: '{b}: {c}个 ({d}%)' },
            legend: { bottom: 0, textStyle: { color: '#aaa' } },
            series: [{ type: 'pie', radius: ['35%', '65%'],
              data: data.length ? data : [{ name: '暂无数据', value: 1 }],
              label: { color: '#ccc' }, itemStyle: { borderRadius: 4 },
              emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(64,158,255,0.5)' } },
            }],
          }, 'projects')
        })
      } catch (e) { console.error('[invest screen chart1]', e) }
    },
    async loadNonMainBiz() {
      try {
        const res = await getNonMainBizStats({})
        const list = (res && res.data && res.data.companyStats) ? res.data.companyStats : []
        this._nonMainBizList = list
        this.$nextTick(() => {
          this.initChart('chart2', {
            tooltip: { trigger: 'axis', formatter: params => `${params[0].name}<br/>非主业占比: ${params[0].value}%` },
            xAxis: { type: 'category', data: list.map(i => i.company), axisLabel: { color: '#aaa', rotate: 30 } },
            yAxis: { type: 'value', axisLabel: { color: '#aaa', formatter: '{value}%' } },
            series: [{ type: 'bar', data: list.map(i => i.ratio),
              itemStyle: { color: (p) => p.value > 30 ? '#FF4D4F' : p.value > 20 ? '#FA8C16' : '#409EFF', borderRadius: [4, 4, 0, 0] },
            }],
            grid: { left: 40, right: 20, bottom: 60, top: 20 },
          }, 'nonMainBiz')
        })
      } catch (e) { console.error('[invest screen chart2]', e) }
    },
    async loadWarnings() {
      try {
        const res = await getInvestWarningList({ pageNumber: 1, pageSize: 100 })
        const list = (res && res.data && res.data.tlist) ? res.data.tlist : []
        if (list.length) this._warningList = list
        const countMap = {}
        list.forEach(w => { countMap[w.level] = (countMap[w.level] || 0) + 1 })
        const data = [
          { name: '高风险', value: countMap.HIGH || 0 },
          { name: '中风险', value: countMap.MEDIUM || 0 },
          { name: '低风险', value: countMap.LOW || 0 },
        ].filter(d => d.value > 0)
        this.$nextTick(() => {
          this.initChart('chart3', {
            tooltip: { trigger: 'item', formatter: '{b}: {c}条 ({d}%)' },
            legend: { bottom: 0, textStyle: { color: '#aaa' } },
            series: [{ type: 'pie', radius: '60%',
              data: data.length ? data : [{ name: '暂无数据', value: 1 }],
              label: { color: '#ccc' }, color: ['#FF4D4F', '#FA8C16', '#52C41A'],
              emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(0,0,0,0.5)' } },
            }],
          }, 'warnings')
        })
      } catch (e) { console.error('[invest screen chart3]', e) }
    },
    loadTrendChart() {
      const years = ['2020', '2021', '2022', '2023', '2024']
      const amounts = [15.5, 25.9, 42.3, 62.7, 76.1]
      this.$nextTick(() => {
        this.initChart('chart4', {
          tooltip: { trigger: 'axis', formatter: params => `${params[0].name}年<br/>投资额: ${params[0].value}亿元` },
          xAxis: { type: 'category', data: years, axisLabel: { color: '#aaa' } },
          yAxis: { type: 'value', axisLabel: { color: '#aaa', formatter: '{value}亿' } },
          series: [{ type: 'line', data: amounts, smooth: true, areaStyle: { opacity: 0.15 },
            lineStyle: { color: '#409EFF', width: 2 }, itemStyle: { color: '#409EFF' },
            symbol: 'circle', symbolSize: 6,
          }],
          grid: { left: 50, right: 20, bottom: 30, top: 20 },
        })
      })
    },
    // KPI 卡片点击
    onKpiClick(card) {
      if (card.drawerType) {
        this.openDrawer(card.drawerType)
      } else if (card.route) {
        this.goTo(card.route)
      }
    },
    // 打开穿透抽屉
    openDrawer(type) {
      const config = {
        projects:   { title: '投资项目明细', route: '/investPenetration/project', list: this._projectList },
        warnings:   { title: '风险预警明细', route: '/investPenetration/riskWarning', list: this._warningList },
        nonMainBiz: { title: '非主业投资明细', route: '/investPenetration/nonMainBiz', list: this._nonMainBizList },
      }
      const c = config[type]
      if (!c) return
      this.drawer = { visible: true, type, title: c.title, route: c.route, list: c.list }
    },
    statusTagType(status) {
      return { IN_PROGRESS: 'primary', COMPLETED: 'success', PENDING: 'info', TERMINATED: 'danger' }[status] || ''
    },
    levelTagType(level) {
      return { HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }[level] || 'info'
    },
    // drawerType 传给 initChart，echarts click 事件触发抽屉
    initChart(refName, option, drawerType) {
      let el = this.$refs[refName]
      if (Array.isArray(el)) el = el[0]
      if (!el) return
      const existing = echarts.getInstanceByDom(el)
      if (existing) existing.dispose()
      const chart = echarts.init(el)
      chart.setOption(option)
      chart.resize()
      if (drawerType) {
        const fn = () => this.openDrawer(drawerType)
        el.addEventListener('click', fn)
        this._chartListeners.push({ el, fn })
      }
      this._charts.push(chart)
    },
  },
}
</script>

<style lang="scss" scoped>
.chart-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
  gap: 12px;
  flex: 1;
  min-height: 0;
}
.chart-panel {
  background: rgba(255,255,255,0.04);
  border: 1px solid rgba(64,158,255,0.15);
  border-radius: 6px;
  padding: 10px;
  display: flex;
  flex-direction: column;
  cursor: pointer;
  transition: border-color 0.2s;
  &:hover { border-color: rgba(64,158,255,0.5); background: rgba(64,158,255,0.06); }
}
.chart-panel-title {
  font-size: 13px;
  color: #409EFF;
  margin-bottom: 6px;
  display: flex;
  align-items: center;
  i { margin-right: 4px; }
  .drill-hint {
    margin-left: auto;
    font-size: 11px;
    color: rgba(64,158,255,0.6);
    font-weight: normal;
  }
}
.chart-box { flex: 1; min-height: 0; height: calc((100vh - 280px) / 2); }

/* 抽屉样式 */
::v-deep .screen-drawer {
  .el-drawer__header { color: #303133; font-weight: 600; font-size: 16px; }
}
.drawer-body { padding: 0 16px 16px; }
.drawer-goto { margin-bottom: 16px; }
</style>
