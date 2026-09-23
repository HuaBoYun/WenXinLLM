<template>
  <div class="risk-module-home" :style="themeVars">
    <!-- Banner区域 -->
    <div class="page-banner">
      <div class="banner-left">
        <h1 class="page-title"><i class="el-icon-warning-outline"></i> 风险管控</h1>
        <p class="page-subtitle">风险识别 · 评估 · 应对 · 监控 · 审查 · 报告 全流程闭环管理</p>
      </div>
      <div class="banner-right">
        <el-button type="primary" plain icon="el-icon-full-screen" @click="goTo('home')">管控驾驶舱</el-button>
        <el-button type="warning" plain icon="el-icon-bell" @click="goTo('cbxx')">催办管理</el-button>
      </div>
    </div>

    <!-- KPI统计卡片（懒加载，不可点击跳转） -->
    <div class="stats-overview" v-loading="kpiLoading">
      <el-row :gutter="16" type="flex">
        <el-col :span="4" v-for="(item, idx) in kpiCards" :key="idx">
          <div class="stat-card">
            <div class="stat-icon" :class="'icon-' + (idx + 1)"><i :class="item.icon"></i></div>
            <div class="stat-content">
              <div class="stat-value">{{ item.value }}</div>
              <div class="stat-label">{{ item.label }}</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 业务流转链路 -->
    <div class="flow-chain-section">
      <div class="section-header"><i class="el-icon-sort"></i> 业务流转流程</div>
      <div class="flow-chain">
        <div
          class="flow-node"
          v-for="(node, idx) in flowNodes"
          :key="node.key"
          @click="goTo(node.key)"
        >
          <div class="node-wrap">
            <div class="node-icon" :class="'node-color-' + (idx % 6 + 1)">
              <i :class="node.icon"></i>
            </div>
            <div class="node-label">{{ node.label }}</div>
            <div class="node-sub">{{ node.sub }}</div>
          </div>
          <div class="flow-arrow" v-if="idx < flowNodes.length - 1">
            <i class="el-icon-right"></i>
          </div>
        </div>
      </div>
    </div>

    <!-- 功能模块卡片 -->
    <div class="function-modules">
      <div class="section-header"><i class="el-icon-menu"></i> 功能模块</div>
      <el-row :gutter="16" type="flex" align="stretch" style="flex-wrap:wrap">
        <el-col :span="6" v-for="mod in modules" :key="mod.key" class="module-col">
          <div class="module-card">
            <div class="card-header" @click="goTo(mod.key)">
              <div class="card-icon" :class="'mod-color-' + mod.colorIdx"><i :class="mod.icon"></i></div>
              <div class="card-title">
                <h3>{{ mod.title }}</h3>
                <span class="card-subtitle">{{ mod.desc }}</span>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 消息通知区域（懒加载） -->
    <!-- 暂时注释：未读催办消息 + 风险事件统计 整块隐藏，作用域仅本页面
    <div class="notice-section">
      <el-row :gutter="16">
        <el-col :span="12">
          <div class="notice-card" v-loading="noticeLoading">
            <div class="notice-header">
              <span><i class="el-icon-bell"></i> 未读催办消息</span>
              <el-button type="text" size="mini" @click="openReminderListDialog">查看全部 <i class="el-icon-arrow-right"></i></el-button>
            </div>
            <div v-if="reminderList.length === 0 && !noticeLoading" class="empty-tip">
              <i class="el-icon-check" style="font-size:24px;color:#67C23A;display:block;margin-bottom:6px"></i>
              暂无未读催办
            </div>
            <div
              v-for="item in reminderList"
              :key="item.id"
              class="notice-item"
              @click="showReminderDetail(item)"
            >
              <div class="notice-dot"></div>
              <div class="notice-content">
                <div class="notice-title">{{ item.reminderContent || '-' }}</div>
                <div class="notice-meta">
                  <span class="notice-from">{{ item.creatorName || '' }}</span>
                  <span class="notice-time">{{ item.createdTime || '' }}</span>
                </div>
              </div>
              <el-tag size="mini" type="danger" effect="plain">{{ item.typeName || '待处理' }}</el-tag>
            </div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="notice-card" v-loading="eventLoading">
            <div class="notice-header"><span><i class="el-icon-warning"></i> 风险事件统计</span></div>
            <div class="event-stats">
              <div class="event-stat-item" @click="navigateTo('eventBase')">
                <div class="event-num" style="color:#F56C6C">{{ eventCount.zhongDa || 0 }}</div>
                <div class="event-label">重大风险事件</div>
              </div>
              <div class="event-divider"></div>
              <div class="event-stat-item" @click="navigateTo('eventView')">
                <div class="event-num" style="color:#E6A23C">{{ eventCount.yiBan || 0 }}</div>
                <div class="event-label">一般风险事件</div>
              </div>
              <div class="event-divider"></div>
              <div class="event-stat-item" @click="navigateTo('riskfill')">
                <div class="event-num" style="color:#409EFF">{{ riskCount.zs || 0 }}</div>
                <div class="event-label">风险总数</div>
              </div>
              <div class="event-divider"></div>
              <div class="event-stat-item" @click="navigateTo('treatment')">
                <div class="event-num" style="color:#67C23A">{{ riskCount.ysp || 0 }}</div>
                <div class="event-label">已审批确认</div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
    -->

    <!-- 未读催办列表弹窗（分页） -->
    <el-dialog
      title="未读催办消息"
      :visible.sync="reminderListDialogVisible"
      width="760px"
      append-to-body
      @open="fetchReminderPage(1)"
    >
      <el-table
        :data="reminderPageList"
        v-loading="reminderPageLoading"
        border
        size="small"
        style="width:100%"
      >
        <el-table-column type="index" label="序号" width="55" align="center" />
        <el-table-column prop="reminderContent" label="催办内容" show-overflow-tooltip />
        <el-table-column prop="typeName" label="类型" width="100" align="center" />
        <el-table-column prop="creatorName" label="下发人" width="100" align="center" />
        <el-table-column prop="createdTime" label="下发时间" width="160" align="center" />
        <el-table-column label="操作" width="80" align="center">
          <template slot-scope="{ row }">
            <el-button type="text" size="mini" @click="showReminderDetail(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="text-align:right;margin-top:12px">
        <el-pagination
          background
          layout="total, prev, pager, next"
          :total="reminderPageTotal"
          :page-size="reminderPageSize"
          :current-page="reminderPageNo"
          @current-change="fetchReminderPage"
        />
      </div>
      <span slot="footer">
        <el-button @click="reminderListDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>

    <!-- 催办单条详情弹窗 -->
    <el-dialog
      title="催办消息详情"
      :visible.sync="reminderDialogVisible"
      width="500px"
      append-to-body
    >
      <div v-if="currentReminder" class="reminder-detail">
        <div class="detail-row">
          <span class="detail-label">催办内容：</span>
          <span class="detail-value">{{ currentReminder.reminderContent || '-' }}</span>
        </div>
        <div class="detail-row" v-if="currentReminder.typeName">
          <span class="detail-label">类型：</span>
          <span class="detail-value">{{ currentReminder.typeName }}</span>
        </div>
        <div class="detail-row" v-if="currentReminder.creatorName">
          <span class="detail-label">下发人：</span>
          <span class="detail-value">{{ currentReminder.creatorName }}</span>
        </div>
        <div class="detail-row" v-if="currentReminder.createdTime">
          <span class="detail-label">下发时间：</span>
          <span class="detail-value">{{ currentReminder.createdTime }}</span>
        </div>
        <div class="detail-row" v-if="currentReminder.reminderTime">
          <span class="detail-label">预计完成时间：</span>
          <span class="detail-value" style="color:#F56C6C">{{ currentReminder.reminderTime }}</span>
        </div>
      </div>
      <span slot="footer">
        <el-button @click="reminderDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>

    <!-- 模块子页面弹窗（无路由时展示） -->
    <el-dialog
      :title="subDialogTitle"
      :visible.sync="subDialogVisible"
      width="480px"
      append-to-body
    >
      <div class="sub-dialog-content">
        <div class="sub-dialog-icon"><i :class="subDialogIcon"></i></div>
        <p class="sub-dialog-desc">{{ subDialogDesc }}</p>
        <div class="sub-dialog-links">
          <el-button
            v-for="link in subDialogLinks"
            :key="link.label"
            type="primary"
            plain
            size="small"
            @click="navigateTo(link.key); subDialogVisible = false"
          >{{ link.label }}</el-button>
        </div>
      </div>
      <span slot="footer">
        <el-button @click="subDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getRiskEventCount, riskNumbers } from '@/api/risk/home.js'
import { getHomeCBList1 } from '@/api/setting/report.js'
import { getRiskListById } from '@/api/risk/riskEvents.js'
import { investThemeMixin } from '@/views/stateAssets/themeMixin'

export default {
  name: 'RiskModuleHome',
  mixins: [investThemeMixin],
  data() {
    return {
      kpiLoading: false,
      noticeLoading: false,
      eventLoading: false,
      reminderList: [],
      eventCount: { zhongDa: 0, yiBan: 0 },
      riskCount: { zs: 0, ysp: 0, wsp: 0 },
      reminderDialogVisible: false,
      currentReminder: null,
      reminderListDialogVisible: false,
      reminderPageList: [],
      reminderPageLoading: false,
      reminderPageTotal: 0,
      reminderPageNo: 1,
      reminderPageSize: 10,
      subDialogVisible: false,
      subDialogTitle: '',
      subDialogIcon: '',
      subDialogDesc: '',
      subDialogLinks: [],
      kpiCards: [
        { label: '风险总数', value: '-', icon: 'el-icon-warning-outline' },
        { label: '已审批确认风险数量', value: '-', icon: 'el-icon-circle-check' },
        { label: '未审批确认风险数量', value: '-', icon: 'el-icon-time' },
        { label: '重大风险事件', value: '-', icon: 'el-icon-s-opportunity' },
        { label: '一般风险事件', value: '-', icon: 'el-icon-info' },
        // { label: '未读催办', value: '-', icon: 'el-icon-bell' }, // 暂时注释，与下方未读催办区块同步
      ],
      flowNodes: [
        { key: 'identify', label: '风险识别', sub: '创建风险台账', icon: 'el-icon-search' },
        { key: 'assessment', label: '风险评估', sub: '集团/本级评估', icon: 'el-icon-s-data' },
        { key: 'standingBook', label: '风险数据', sub: '本级/集团数据', icon: 'el-icon-document' },
        { key: 'riskfill', label: '重大风险', sub: '创建/跟进/关闭', icon: 'el-icon-s-opportunity' },
        { key: 'treatment', label: '风险应对', sub: '制定应对措施', icon: 'el-icon-s-tools' },
        { key: 'monitor', label: '风险监控', sub: '实时监控预警', icon: 'el-icon-view' },
        { key: 'riskReview', label: '风险审查', sub: '项目审查意见', icon: 'el-icon-document-checked' },
        { key: 'eventBase', label: '风险事件', sub: '上报/台账管理', icon: 'el-icon-warning' },
        { key: 'riskModel', label: '风险模型', sub: '模型库/预警', icon: 'el-icon-cpu' },
        { key: 'report', label: '风险报告', sub: '管理报告台账', icon: 'el-icon-tickets' },
        { key: 'cbxx', label: '催办管理', sub: '超时催办提醒', icon: 'el-icon-bell' },
      ],
      modules: [
        {
          key: 'identify', title: '风险识别', desc: '风险创建、台账与版本管理', icon: 'el-icon-search', colorIdx: 1,
          pages: [
            { label: '风险创建', route: '/identify/creation' },
            { label: '风险台账', route: '/identify/database' },
            { label: '版本管理', route: '/identify/version' },
          ],
        },
        {
          key: 'assessment', title: '风险评估', desc: '集团与本级单位风险评估', icon: 'el-icon-s-data', colorIdx: 2,
          pages: [
            { label: '集团评估计划', route: '/GroupPlan' },
            { label: '集团跟进结果', route: '/FollowUpResults' },
            { label: '风险评估跟踪', route: '/assessment/assessmentTracking' },
            { label: '评估计划', route: '/assessment/plan' },
            { label: '评估任务', route: '/assessment/task' },
            { label: '评估结果', route: '/assessment/result' },
            { label: '评估标准', route: '/assessmentStandard' },
          ],
        },
        {
          key: 'standingBook', title: '风险数据', desc: '本级及全集团风险数据库', icon: 'el-icon-document', colorIdx: 3,
          pages: [
            { label: '本级风险数据库', route: '/standingBook' },
            { label: '集团风险数据库', route: '/GroupRiskDatabase' },
          ],
        },
        {
          key: 'riskfill', title: '重大风险', desc: '重大风险全生命周期管理', icon: 'el-icon-s-opportunity', colorIdx: 4,
          pages: [
            { label: '重大风险创建', route: '/riskfill/riskcreate' },
            { label: '重大风险填报', route: '/riskfill/riskfill' },
            { label: '重大风险跟踪', route: '/riskfill/riskTracking' },
            { label: '重大风险汇总', route: '/riskall/riskall' },
            { label: '重大风险台账', route: '/riskall/riskLedger' },
            { label: '月度评估', route: '/riskvalue/riskvalue' },
            { label: '月度评估汇总', route: '/riskReportList/riskReportList' },
            { label: '风险监督改进', route: '/riskSupervise/riskSupervise' },
            { label: '风险监测指标创建', route: '/riskfill/riskIndicatorCreation' },
            { label: '风险监测指标填报', route: '/riskfill/riskIndicatorReporting' },
            { label: '风险监测指标汇总', route: '/riskfill/riskIndicatorSummarization' },
          ],
        },
        {
          key: 'treatment', title: '风险应对', desc: '制定并跟踪风险应对措施', icon: 'el-icon-s-tools', colorIdx: 5,
          pages: [
            { label: '风险应对', route: '/treatment' },
          ],
        },
        {
          key: 'monitor', title: '风险监控', desc: '多维度实时监控内外部数据', icon: 'el-icon-view', colorIdx: 6,
          pages: [
            { label: '供应商监控', route: '/monitor/supplier' },
            { label: '投资机构监控', route: '/monitor/organization' },
            { label: '下属公司监控', route: '/monitor/company' },
            { label: '竞争对手监控', route: '/monitor/competitors' },
            { label: '客户监控', route: '/monitor/customer' },
          ],
        },
        {
          key: 'riskReview', title: '风险审查', desc: '项目风险审查与意见管理', icon: 'el-icon-document-checked', colorIdx: 1,
          pages: [
            { label: '项目风险审查', route: '/riskReview/riskReviewBook' },
            { label: '风险审查意见', route: '/riskReview/RiskReviewComments' },
          ],
        },
        {
          key: 'eventBase', title: '风险事件', desc: '风险事件上报与台账管理', icon: 'el-icon-warning', colorIdx: 2,
          pages: [
            { label: '风险事件上报', route: '/events/eventBase' },
            { label: '风险事件台账', route: '/events/eventView' },
          ],
        },
        {
          key: 'riskModel', title: '风险模型', desc: '风险模型库与预警管理', icon: 'el-icon-cpu', colorIdx: 3,
          pages: [
            { label: '风险模型库', route: '/base/sjmxk' },
            { label: '数据源管理', route: '/base/sjygl' },
            { label: 'Excel导入', route: '/base/sjyh' },
            { label: '表达式管理', route: '/yjgl/bdsgl' },
            { label: '评估模型管理', route: '/yjgl/pgmxgl' },
            { label: '数据模型管理', route: '/yjgl/sjmxgl' },
            { label: '风险预警管理', route: '/yjgl/fxyjgl' },
            { label: '数据源配置', route: '/mxgl/sjygl' },
            { label: '风险预警数据', route: '/mxgl/fxyjgl' },
            { label: '评估模型', route: '/mxgl/pgmxgl' },
            { label: '风险预警', route: '/mxgl/fxyj' },
          ],
        },
        {
          key: 'report', title: '风险报告', desc: '风险管理报告与台账', icon: 'el-icon-tickets', colorIdx: 4,
          pages: [
            { label: '风险管理报告', route: '/report/normal' },
            { label: '风险报告台账', route: '/report/normal/report' },
          ],
        },
        {
          key: 'cbxx', title: '催办管理', desc: '超时任务催办与访问记录', icon: 'el-icon-bell', colorIdx: 5,
          pages: [
            { label: '催办信息', route: '/cbgl/cbxx' },
            { label: '访问记录', route: '/cbgl/fwjl' },
          ],
        },
        {
          key: 'home', title: '风险驾驶舱', desc: '风险管控数据全景总览', icon: 'el-icon-odometer', colorIdx: 6,
          pages: [
            { label: '风险统计', route: '/home/index' },
            { label: '图表分析', route: '/home/index' },
            { label: '大屏展示', route: '/home/index' },
          ],
        },
      ],
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.lazyLoadData()
    })
  },
  methods: {
    lazyLoadData() {
      this.loadKpi()
      // this.loadReminder() // 暂时注释：未读催办区块已隐藏，避免向已注释的 kpiCards[5] 写入
      this.loadEventCount()
    },
    loadKpi() {
      this.kpiLoading = true
      // 取当前用户"组织穿透"中选中的组织 id（与 RiskCockpit/index.vue 一致）
      let company = ''
      try {
        const userInfo = localStorage.getItem('userInfo')
        if (userInfo) {
          const parsed = JSON.parse(userInfo)
          company = (parsed && parsed.currentOrg && parsed.currentOrg.orgid) || ''
        }
      } catch (e) { company = '' }

      // 单接口拿全 3 项：data.zs 风险总数 / data.ysp 已审批确认 / data.wsp 未审批确认
      // token 由 utils/requestData.transData 自动注入到 query
      riskNumbers({ company })
        .then((res) => {
          if (res && Number(res.code) === 1 && res.data) {
            const zs = Number(res.data.zs) || 0
            const ysp = Number(res.data.ysp) || 0
            const wsp = Number(res.data.wsp) || 0
            this.riskCount.zs = zs
            this.riskCount.ysp = ysp
            this.riskCount.wsp = wsp
            this.kpiCards[0].value = zs   // 风险总数
            this.kpiCards[1].value = ysp  // 已审批确认风险数量
            this.kpiCards[2].value = wsp  // 未审批确认风险数量
          }
        })
        .catch(() => {})
        .finally(() => { this.kpiLoading = false })
    },
    loadReminder() {
      this.noticeLoading = true
      // 与 cb.vue 保持一致：用 getHomeCBList1，isRead:0 取未读，字段 reminderContent/creatorName/createdTime/typeName
      getHomeCBList1({ pageNumber: 1, pageSize: 5, isRead: 0 })
        .then((res) => {
          if (res && res.data) {
            const list = res.data.tlist || res.data.list || []
            this.reminderList = list.slice(0, 5)
            this.kpiCards[5].value = res.data.totalRecord || list.length
          }
        })
        .catch(() => {})
        .finally(() => { this.noticeLoading = false })
    },
    loadEventCount() {
      this.eventLoading = true
      // 接口：GET /riskEventZh/disposalmanage，riskcatid=1005117 必传
      // losseventcategory: '1'=一般事件，'2'=重大事件，对应 type 参数
      // 返回结构：data.page.total
      const getTotal = (res) => res && res.data && res.data.page && res.data.page.total || 0
      const baseParams = { pageNo: 1, pageSize: 20, riskcatid: '1005117', orgid: '', orgName: '', startDate: '', endDate: '' }

      // 重大风险事件：type='2'
      const p1 = getRiskListById({ ...baseParams, type: '2' })
        .then((res) => {
          this.eventCount.zhongDa = getTotal(res)
          this.kpiCards[3].value = getTotal(res)
        })
        .catch(() => {})
      // 一般风险事件：type='1'
      const p2 = getRiskListById({ ...baseParams, type: '1' })
        .then((res) => {
          this.eventCount.yiBan = getTotal(res)
          this.kpiCards[4].value = getTotal(res)
        })
        .catch(() => {})
      Promise.all([p1, p2]).finally(() => { this.eventLoading = false })
    },
    getCurrentOrgId() {
      try {
        const info = localStorage.getItem('userInfo')
        if (info) return JSON.parse(info).currentOrg.orgid
      } catch (e) {}
      return ''
    },
    goTo(key) {
      this.navigateTo(key)
    },
    // 核心跳转方法：动态查找路由，找不到则弹窗展示模块信息
    navigateTo(key) {
      // 如果传入的是完整路径（页面标签直接传路径），直接在路由表里查找并跳转
      if (key && key.startsWith('/')) {
        const flatPaths = (this.$router.getRoutes ? this.$router.getRoutes() : []).map(r => r.path)
        if (flatPaths.includes(key)) {
          this.$router.push(key).catch(() => {})
          return
        }
        this.$message.warning('该页面暂未加载，请先切换到风险管控模块后访问')
        return
      }

      // 路由候选路径（按优先级排列，动态路由短路径优先）
      // 注：用户指定的目标页面放在候选首位，命中后立即跳转，不会回退到其它路径
      const routeCandidates = {
        // 风险驾驶舱 → @/views/risk/home（本环境实际菜单注册为 /home/DHOME；不要写 /risk/home，那是当前页本身）
        home:        ['/home/DHOME', '/home/index', '/home'],
        identify:    ['/identify/creation', '/identify/database', '/risk/identify/creation'],
        // 风险评估 → @/views/workbench/contractTools/GroupPlan
        assessment:  ['/GroupPlan', '/contractTools/GroupPlan', '/workbench/contractTools/GroupPlan', '/assessment/plan', '/assessment/task', '/risk/assessment/plan'],
        standingBook:['/standingBook', '/risk/standingBook'],
        riskfill:    ['/riskfill/riskcreate', '/riskfill/riskfill', '/risk/riskfill/riskcreate'],
        treatment:   ['/treatment', '/risk/treatment'],
        // 风险监控 → @/views/risk/monitor/supplier
        monitor:     ['/monitor/supplier', '/risk/monitor/supplier', '/monitor/company', '/risk/monitor/company'],
        // 风险审查 → @/views/risk/riskReview/riskReviewBook（本环境实际菜单注册为 /examination/competitors）
        riskReview:  ['/examination/competitors', '/riskReview/riskReviewBook', '/risk/riskReview/riskReviewBook'],
        eventBase:   ['/events/eventBase', '/risk/events/eventBase'],
        eventView:   ['/events/eventView', '/risk/events/eventView'],
        // 风险模型 → @/views/risk/mxgl/sjmxgl（本环境实际菜单注册为 /RiskSjfx/mxsjmxgl）
        riskModel:   ['/RiskSjfx/mxsjmxgl', '/mxgl/sjmxgl', '/risk/mxgl/sjmxgl', '/yjgl/sjmxgl', '/base/sjmxk', '/risk/base/sjmxk'],
        report:      ['/report/normal', '/risk/report/normal'],
        cbxx:        ['/cbgl/cbxx', '/setting/cbgl/cbxx', '/risk/cbgl/cbxx'],
      }

      // 模块弹窗信息（路由找不到时展示）
      const moduleInfo = {
        identify:    { title: '风险识别', icon: 'el-icon-search', desc: '风险创建、台账与版本管理', links: [{ label: '风险创建', key: 'identify' }, { label: '风险台账', key: 'identify' }] },
        assessment:  { title: '风险评估', icon: 'el-icon-s-data', desc: '集团与本级单位风险评估管理', links: [{ label: '评估计划', key: 'assessment' }, { label: '评估任务', key: 'assessment' }] },
        standingBook:{ title: '风险数据', icon: 'el-icon-document', desc: '本级及全集团风险数据库', links: [{ label: '本级风险数据库', key: 'standingBook' }] },
        riskfill:    { title: '重大风险', icon: 'el-icon-s-opportunity', desc: '重大风险全生命周期管理', links: [{ label: '重大风险创建', key: 'riskfill' }, { label: '月度评估', key: 'riskfill' }] },
        treatment:   { title: '风险应对', icon: 'el-icon-s-tools', desc: '制定并跟踪风险应对措施', links: [{ label: '风险应对', key: 'treatment' }] },
        monitor:     { title: '风险监控', icon: 'el-icon-view', desc: '多维度实时监控内外部数据', links: [{ label: '下属公司监控', key: 'monitor' }, { label: '供应商监控', key: 'monitor' }] },
        riskReview:  { title: '风险审查', icon: 'el-icon-document-checked', desc: '项目风险审查与意见管理', links: [{ label: '项目风险审查', key: 'riskReview' }] },
        eventBase:   { title: '风险事件', icon: 'el-icon-warning', desc: '风险事件上报与台账管理', links: [{ label: '事件上报', key: 'eventBase' }, { label: '事件台账', key: 'eventView' }] },
        riskModel:   { title: '风险模型', icon: 'el-icon-cpu', desc: '风险模型库与预警管理', links: [{ label: '风险模型库', key: 'riskModel' }] },
        report:      { title: '风险报告', icon: 'el-icon-tickets', desc: '风险管理报告与台账', links: [{ label: '风险管理报告', key: 'report' }] },
        cbxx:        { title: '催办管理', icon: 'el-icon-bell', desc: '超时任务催办与访问记录', links: [{ label: '催办信息', key: 'cbxx' }] },
      }

      const candidates = routeCandidates[key] || []
      // 从当前已注册路由中查找匹配的路径
      // getRoutes() 返回所有已注册路由（含动态 addRoutes 加入的），options.routes 只有静态路由
      const flatPaths = (this.$router.getRoutes ? this.$router.getRoutes() : []).map(r => r.path)
      const currentPath = this.$route && this.$route.path

      // 一阶：候选 path 精确匹配（排除当前页本身，避免 NavigationDuplicated）
      for (const candidate of candidates) {
        if (flatPaths.includes(candidate) && candidate !== currentPath) {
          this.$router.push(candidate).catch(() => {})
          return
        }
      }

      // 二阶：候选 path 尾段模糊匹配（容错后端动态注册路径前缀差异）
      // 例：候选 '/mxgl/sjmxgl' 没命中，但系统里存在 '/yjgl/sjmxgl' → 也认为匹配
      for (const candidate of candidates) {
        const segs = candidate.split('/').filter(Boolean)
        const tail = segs[segs.length - 1]
        if (!tail) continue
        const hit = flatPaths.find(p =>
          p !== currentPath &&
          (p === '/' + tail || p.endsWith('/' + tail))
        )
        if (hit) {
          this.$router.push(hit).catch(() => {})
          return
        }
      }

      // 路由未找到：弹窗展示模块信息
      const info = moduleInfo[key]
      if (info) {
        this.subDialogTitle = info.title
        this.subDialogIcon = info.icon
        this.subDialogDesc = info.desc + '\n\n（当前模块路由未加载，请先切换到风险管控模块后再访问）'
        this.subDialogLinks = []
        this.subDialogVisible = true
      } else {
        this.$message.warning('该功能模块暂未开放，请切换到风险管控模块后访问')
      }
    },
    // 递归展开所有路由路径
    _flattenRoutes(routes, parentPath = '') {
      const paths = []
      for (const route of routes) {
        const fullPath = route.path.startsWith('/')
          ? route.path
          : (parentPath + '/' + route.path).replace(/\/+/g, '/')
        paths.push(fullPath)
        if (route.children && route.children.length) {
          paths.push(...this._flattenRoutes(route.children, fullPath))
        }
      }
      return paths
    },
    showReminderDetail(item) {
      this.currentReminder = item
      this.reminderDialogVisible = true
    },
    openReminderListDialog() {
      this.reminderListDialogVisible = true
    },
    fetchReminderPage(pageNo) {
      this.reminderPageNo = pageNo || 1
      this.reminderPageLoading = true
      getHomeCBList1({ pageNumber: this.reminderPageNo, pageSize: this.reminderPageSize, isRead: 0 })
        .then((res) => {
          if (res && res.data) {
            this.reminderPageList = res.data.tlist || res.data.list || []
            this.reminderPageTotal = res.data.totalRecord || 0
          }
        })
        .catch(() => {})
        .finally(() => { this.reminderPageLoading = false })
    },
  },
}
</script>

<style lang="scss" scoped>
.risk-module-home {
  padding: 16px;
  background: #f0f2f5;
  min-height: calc(100vh - 84px);
}

/* Banner */
.page-banner {
  display: flex; justify-content: space-between; align-items: center;
  padding: 20px 24px; margin-bottom: 16px;
  background: linear-gradient(135deg, var(--ip-primary) 0%, var(--ip-secondary) 50%, var(--ip-bright) 100%);
  border-radius: 6px; color: #fff;
  .page-title {
    font-size: 22px; font-weight: 600; margin: 0 0 6px;
    display: flex; align-items: center;
    i { margin-right: 10px; font-size: 26px; color: #e94560; }
  }
  .page-subtitle { font-size: 13px; opacity: 0.8; margin: 0; }
  .banner-right .el-button { margin-left: 10px; }
}

/* KPI统计 */
.stats-overview {
  margin-bottom: 16px;
  // flex 模式下让 KPI 卡片等分剩余宽度，自动适配未读催办被隐藏后的 5 项布局
  ::v-deep .el-row--flex > .el-col {
    flex: 1 1 0;
    max-width: none;
  }
  .stat-card {
    background: #fff; border-radius: 6px; padding: 16px;
    display: flex; align-items: center; cursor: pointer;
    box-shadow: 0 1px 4px rgba(0,0,0,0.08); transition: all 0.3s;
    &:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(0,0,0,0.12); }
    .stat-icon {
      width: 46px; height: 46px; border-radius: 10px;
      display: flex; align-items: center; justify-content: center; margin-right: 12px;
      i { font-size: 22px; color: #fff; }
      &.icon-1 { background: linear-gradient(135deg, var(--ip-primary), var(--ip-bright)); }
      &.icon-2 { background: linear-gradient(135deg, #52C41A, #73D13D); }
      &.icon-3 { background: linear-gradient(135deg, #FA8C16, #FAAD14); }
      &.icon-4 { background: linear-gradient(135deg, #F5222D, #FF4D4F); }
      &.icon-5 { background: linear-gradient(135deg, #722ED1, #9254DE); }
      &.icon-6 { background: linear-gradient(135deg, #EB2F96, #F759AB); }
    }
    .stat-content {
      .stat-value { font-size: 22px; font-weight: 600; color: #303133; line-height: 1.2; }
      .stat-label { font-size: 12px; color: #909399; margin-top: 2px; }
    }
  }
}

/* 业务流转 */
.flow-chain-section {
  background: #fff; border-radius: 6px; padding: 18px 24px; margin-bottom: 16px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);
  .section-header {
    font-size: 15px; font-weight: 600; color: #303133; margin-bottom: 16px;
    display: flex; align-items: center;
    i { margin-right: 8px; color: var(--ip-primary); }
  }
}
.flow-chain {
  display: flex; align-items: flex-start; flex-wrap: wrap; gap: 4px;
  .flow-node {
    display: flex; align-items: center;
    .node-wrap {
      display: flex; flex-direction: column; align-items: center; cursor: pointer;
      padding: 8px 12px; border-radius: 6px; transition: all 0.3s;
      &:hover { background: var(--ip-light-bg, #f0f4ff); transform: translateY(-2px); }
      .node-icon {
        width: 44px; height: 44px; border-radius: 50%;
        display: flex; align-items: center; justify-content: center;
        color: #fff; font-size: 20px; margin-bottom: 6px; transition: all 0.3s;
        &.node-color-1 { background: linear-gradient(135deg, var(--ip-primary), var(--ip-bright)); }
        &.node-color-2 { background: linear-gradient(135deg, #e94560, #c0392b); }
        &.node-color-3 { background: linear-gradient(135deg, #FA8C16, #FAAD14); }
        &.node-color-4 { background: linear-gradient(135deg, #52C41A, #73D13D); }
        &.node-color-5 { background: linear-gradient(135deg, var(--ip-bright, #1677FF), var(--ip-secondary, #40a9ff)); }
        &.node-color-6 { background: linear-gradient(135deg, #722ED1, #9254DE); }
      }
      .node-label { font-size: 13px; font-weight: 600; color: #303133; }
      .node-sub { font-size: 11px; color: #909399; margin-top: 2px; }
    }
    .flow-arrow {
      font-size: 16px; color: #C0C4CC; margin: 0 2px; padding-bottom: 20px;
    }
  }
}

/* 功能模块 */
.function-modules {
  margin-bottom: 16px;
  .section-header {
    font-size: 15px; font-weight: 600; color: #303133; margin-bottom: 14px;
    display: flex; align-items: center;
    i { margin-right: 8px; color: var(--ip-primary); }
  }
  // el-row flex 模式下每列需要自己控制间距
  ::v-deep .el-row {
    margin-bottom: -16px;
  }
  .module-col {
    margin-bottom: 16px;
    display: flex;
    flex-direction: column;
  }
  .module-card {
    background: #fff; border-radius: 6px; padding: 16px;
    box-shadow: 0 1px 4px rgba(0,0,0,0.08); transition: all 0.3s;
    border-left: 3px solid var(--ip-primary);
    flex: 1; display: flex; flex-direction: column;
    .card-header {
      display: flex; align-items: flex-start; margin-bottom: 10px;
      cursor: pointer;
      &:hover { opacity: 0.85; }
    }
    .card-icon {
      width: 38px; height: 38px; border-radius: 8px;
      display: flex; align-items: center; justify-content: center;
      color: #fff; font-size: 18px; margin-right: 10px; flex-shrink: 0;
      &.mod-color-1 { background: linear-gradient(135deg, var(--ip-primary), var(--ip-bright)); }
      &.mod-color-2 { background: linear-gradient(135deg, #e94560, #c0392b); }
      &.mod-color-3 { background: linear-gradient(135deg, #FA8C16, #FAAD14); }
      &.mod-color-4 { background: linear-gradient(135deg, #52C41A, #73D13D); }
      &.mod-color-5 { background: linear-gradient(135deg, var(--ip-bright, #1677FF), var(--ip-secondary, #40a9ff)); }
      &.mod-color-6 { background: linear-gradient(135deg, #722ED1, #9254DE); }
    }
    .card-title {
      h3 { font-size: 14px; font-weight: 600; color: #303133; margin: 0 0 3px; }
      .card-subtitle { font-size: 12px; color: #909399; }
    }
  }
}

/* 消息通知 */
.notice-section {
  margin-bottom: 16px;
  .notice-card {
    background: #fff; border-radius: 6px; padding: 16px;
    box-shadow: 0 1px 4px rgba(0,0,0,0.08); min-height: 180px;
    .notice-header {
      font-size: 15px; font-weight: 600; color: #303133; margin-bottom: 12px;
      display: flex; align-items: center; justify-content: space-between;
      span { display: flex; align-items: center; }
      i { margin-right: 8px; color: var(--ip-primary); }
    }
    .empty-tip { color: #C0C4CC; font-size: 13px; text-align: center; padding: 30px 0; }
    .notice-item {
      display: flex; align-items: center; padding: 10px 8px;
      border-bottom: 1px solid #f5f5f5; cursor: pointer; transition: all 0.2s;
      border-radius: 4px;
      &:last-child { border-bottom: none; }
      &:hover { background: var(--ip-light-bg, #f0f4ff); }
      .notice-dot {
        width: 8px; height: 8px; border-radius: 50%; background: #F56C6C;
        flex-shrink: 0; margin-right: 10px;
      }
      .notice-content { flex: 1; min-width: 0;
        .notice-title {
          font-size: 13px; color: #303133; line-height: 1.5;
          overflow: hidden; text-overflow: ellipsis; white-space: nowrap;
        }
        .notice-meta {
          display: flex; gap: 12px; margin-top: 3px;
          .notice-from { font-size: 11px; color: var(--ip-bright); }
          .notice-time { font-size: 11px; color: #C0C4CC; }
        }
      }
    }
  }
}

/* 风险事件统计 */
.event-stats {
  display: flex; align-items: center; justify-content: space-around; padding: 20px 0;
  .event-stat-item {
    display: flex; flex-direction: column; align-items: center; cursor: pointer;
    padding: 10px 20px; border-radius: 6px; transition: background 0.2s;
    &:hover { background: var(--ip-light-bg, #f0f4ff); }
    .event-num { font-size: 28px; font-weight: 700; line-height: 1.2; }
    .event-label { font-size: 12px; color: #909399; margin-top: 4px; }
  }
  .event-divider { width: 1px; height: 50px; background: #f0f0f0; }
}

/* 催办详情弹窗 */
.reminder-detail {
  .detail-row {
    display: flex; padding: 10px 0; border-bottom: 1px solid #f5f5f5;
    &:last-child { border-bottom: none; }
    .detail-label { font-size: 13px; color: #909399; width: 80px; flex-shrink: 0; }
    .detail-value { font-size: 13px; color: #303133; flex: 1; line-height: 1.6; }
  }
}

/* 模块弹窗 */
.sub-dialog-content {
  text-align: center; padding: 10px 0;
  .sub-dialog-icon {
    font-size: 48px; color: var(--ip-primary); margin-bottom: 12px;
    i { font-size: 48px; }
  }
  .sub-dialog-desc {
    font-size: 14px; color: #606266; line-height: 1.8; margin-bottom: 16px;
    white-space: pre-line;
  }
  .sub-dialog-links { display: flex; flex-wrap: wrap; gap: 8px; justify-content: center; }
}
</style>

