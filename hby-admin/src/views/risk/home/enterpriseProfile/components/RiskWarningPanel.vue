<template>
  <div class="risk-warning-panel">
    <div class="section-title">
      <i class="el-icon-warning-outline"></i>
      风险预警
      <el-button type="text" size="mini" class="title-link" @click="goToRiskHome">
        进入驾驶舱 <i class="el-icon-arrow-right"></i>
      </el-button>
    </div>

    <div class="panel-content" v-loading="kpiLoading">

      <!-- KPI 统计卡 -->
      <div class="kpi-section">
        <div class="kpi-grid">
          <div
            v-for="(card, idx) in kpiCards"
            :key="idx"
            class="kpi-card"
            :class="'kpi-color-' + (idx + 1)"
            @click="navigateTo(card.route)"
          >
            <div class="kpi-icon"><i :class="card.icon"></i></div>
            <div class="kpi-info">
              <div class="kpi-value">{{ card.value }}</div>
              <div class="kpi-label">{{ card.label }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 业务流转链路（精简版，可点击跳转） -->
      <div class="flow-section">
        <div class="subsection-title">业务流转</div>
        <div class="flow-nodes">
          <div
            v-for="(node, idx) in flowNodes"
            :key="node.key"
            class="flow-node"
            @click="navigateTo(node.key)"
          >
            <div class="node-icon" :class="'node-color-' + (idx % 6 + 1)">
              <i :class="node.icon"></i>
            </div>
            <div class="node-label">{{ node.label }}</div>
            <i class="el-icon-arrow-right flow-arrow" v-if="idx < flowNodes.length - 1"></i>
          </div>
        </div>
      </div>

      <!-- 功能模块快捷入口 -->
      <div class="module-section">
        <div class="subsection-title">快捷入口</div>
        <div class="module-grid">
          <div
            v-for="mod in quickModules"
            :key="mod.key"
            class="module-item"
            @click="navigateTo(mod.key)"
          >
            <i :class="[mod.icon, 'mod-icon', 'mod-color-' + mod.colorIdx]"></i>
            <span class="mod-label">{{ mod.label }}</span>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import { getRiskEventCount, riskNumbers } from '@/api/risk/home.js'
import { getRiskListById } from '@/api/risk/riskEvents.js'

export default {
  name: 'RiskWarningPanel',
  props: {
    enterpriseId: { type: String, required: true },
    riskData:     { type: Object, default: null },
    loading:      { type: Boolean, default: false }
  },
  data() {
    return {
      kpiLoading: false,
      kpiCards: [
        { label: '风险总数',       value: '-', icon: 'el-icon-warning-outline',  route: 'standingBook' },
        { label: '已审批确认',     value: '-', icon: 'el-icon-circle-check',     route: 'standingBook' },
        { label: '未审批确认',     value: '-', icon: 'el-icon-time',             route: 'standingBook' },
        { label: '重大风险事件',   value: '-', icon: 'el-icon-s-opportunity',    route: 'riskfill'     },
        { label: '一般风险事件',   value: '-', icon: 'el-icon-info',             route: 'eventBase'    },
      ],
      // 精简版流转链路：6个核心节点
      flowNodes: [
        { key: 'identify',    label: '风险识别', icon: 'el-icon-search' },
        { key: 'assessment',  label: '风险评估', icon: 'el-icon-s-data' },
        { key: 'riskfill',    label: '重大风险', icon: 'el-icon-s-opportunity' },
        { key: 'monitor',     label: '风险监控', icon: 'el-icon-view' },
        { key: 'eventBase',   label: '风险事件', icon: 'el-icon-warning' },
        { key: 'report',      label: '风险报告', icon: 'el-icon-tickets' },
      ],
      // 快捷入口模块
      quickModules: [
        { key: 'riskModel',   label: '风险模型', icon: 'el-icon-cpu',              colorIdx: 3 },
        { key: 'treatment',   label: '风险应对', icon: 'el-icon-s-tools',          colorIdx: 5 },
        { key: 'riskReview',  label: '风险审查', icon: 'el-icon-document-checked', colorIdx: 1 },
        { key: 'cbxx',        label: '催办管理', icon: 'el-icon-bell',             colorIdx: 5 },
      ],
    }
  },
  watch: {
    /**
     * 监听父组件传入的 enterpriseId 变化，公司切换时重新加载 KPI 数据
     */
    enterpriseId(newVal, oldVal) {
      if (newVal !== oldVal) {
        this.loadKpi()
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.loadKpi()
      // 检测是否有切换模块后待跳转的路由 key
      this._handlePendingRoute()
    })
  },
  methods: {
    /**
     * 切换到风险管控模块后，检测 sessionStorage 中的待跳转路径并执行跳转。
     * 只有当前模块确实是 fxgk（风险管控）时才跳转，否则清除 key 不做任何操作，
     * 避免回到其他模块（如监管驾驶舱）时误触发自动跳转。
     * 使用轮询等待动态路由注册完成，最多等待 3 秒。
     */
    _handlePendingRoute() {
      const pendingPath = sessionStorage.getItem('rwp_pending_route')
      if (!pendingPath) return
      // 无论是否跳转，先清除，防止重复触发
      sessionStorage.removeItem('rwp_pending_route')
      // 仅在风险管控模块下才执行跳转
      const currentModel = localStorage.getItem('model')
      if (currentModel !== 'fxgk') return
      // 轮询等待动态路由注册完成后再跳转（最多 3 秒，每 200ms 检测一次）
      this._waitAndNavigatePath(pendingPath, 0, 15)
    },

    /**
     * 轮询等待目标完整路径注册完成后跳转。
     * 存储的是完整路径（如 /identify/creation），直接精确匹配，不走候选表。
     * @param {string} path      目标完整路径
     * @param {number} attempt   当前尝试次数
     * @param {number} maxTries  最大尝试次数（每次间隔 200ms）
     */
    _waitAndNavigatePath(path, attempt, maxTries) {
      const flatPaths = (this.$router.getRoutes ? this.$router.getRoutes() : []).map(r => r.path)
      const cur = this.$route && this.$route.path

      // 精确匹配
      if (flatPaths.includes(path) && path !== cur) {
        this.$router.push(path).catch(() => {})
        return
      }

      // 尾段模糊匹配（容错路由前缀差异，如 /identify/creation → /risk/identify/creation）
      const tail = path.split('/').filter(Boolean).pop()
      if (tail) {
        const hit = flatPaths.find(p => p !== cur && (p === '/' + tail || p.endsWith('/' + tail)))
        if (hit) {
          this.$router.push(hit).catch(() => {})
          return
        }
      }

      // 路由还未注册，继续等待
      if (attempt < maxTries) {
        setTimeout(() => { this._waitAndNavigatePath(path, attempt + 1, maxTries) }, 200)
      }
      // 超时后停留在当前页（风险管控首页）
    },
    /**
     * 跳转至风险管控模块首页（风险驾驶舱 @/views/risk/home）。
     * 若当前已在 fxgk 模块，直接 push；否则利用 localStorage.path 机制切换模块整页跳转。
     */
    goToRiskHome() {
      const currentModel = localStorage.getItem('model')
      const targetPath = '/home/DHOME'

      // 已在风险管控模块，直接跳
      if (currentModel === 'fxgk') {
        const flatPaths = (this.$router.getRoutes ? this.$router.getRoutes() : []).map(r => r.path)
        const cur = this.$route && this.$route.path
        if (flatPaths.includes(targetPath) && targetPath !== cur) {
          this.$router.push(targetPath).catch(() => {})
          return
        }
        const hit = flatPaths.find(p => p !== cur && p.endsWith('/DHOME'))
        if (hit) { this.$router.push(hit).catch(() => {}); return }
        this.$router.push('/').catch(() => {})
        return
      }

      // 切换到风险管控模块，利用 permissions.js 的 localStorage.path 机制整页跳转
      localStorage.setItem('path', targetPath)
      localStorage.setItem('model', 'fxgk')
      localStorage.setItem('modelname', '风险管控')
      location.href = '/'
    },

    loadKpi() {
      this.kpiLoading = true
      // 优先使用父组件传入的 enterpriseId（公司切换时已更新），
      // 兜底从 localStorage userInfo 中取 orgid
      let company = this.enterpriseId || ''
      if (!company) {
        try {
          const info = localStorage.getItem('userInfo')
          if (info) company = (JSON.parse(info).currentOrg || {}).orgid || ''
        } catch (e) { company = '' }
      }

      riskNumbers({ company })
        .then(res => {
          if (res && Number(res.code) === 1 && res.data) {
            this.kpiCards[0].value = Number(res.data.zs)  || 0
            this.kpiCards[1].value = Number(res.data.ysp) || 0
            this.kpiCards[2].value = Number(res.data.wsp) || 0
          } else {
            // 无数据时重置为 0
            this.kpiCards[0].value = 0
            this.kpiCards[1].value = 0
            this.kpiCards[2].value = 0
          }
        })
        .catch(() => {
          this.kpiCards[0].value = 0
          this.kpiCards[1].value = 0
          this.kpiCards[2].value = 0
        })
        .finally(() => { this.kpiLoading = false })

      const base = { pageNo: 1, pageSize: 20, riskcatid: '1005117', orgid: company, orgName: '', startDate: '', endDate: '' }
      const getTotal = r => (r && r.data && r.data.page && r.data.page.total) || 0
      getRiskListById({ ...base, type: '2' }).then(r => { this.kpiCards[3].value = getTotal(r) }).catch(() => { this.kpiCards[3].value = 0 })
      getRiskListById({ ...base, type: '1' }).then(r => { this.kpiCards[4].value = getTotal(r) }).catch(() => { this.kpiCards[4].value = 0 })
    },

    /**
     * 跳转到风险模块对应页面。
     * 一阶：精确匹配已注册路由 → 直接 push。
     * 二阶：尾段模糊匹配 → 直接 push。
     * 三阶：路由未注册 → 切换到风险管控模块(fxgk)，
     *        把目标完整路径（候选表第一项）存入 sessionStorage，
     *        刷新后由 _handlePendingRoute → _waitAndNavigatePath 精确跳转。
     */
    navigateTo(key) {
      if (!key) return

      // 完整路径直接跳
      if (key.startsWith('/')) {
        const paths = (this.$router.getRoutes ? this.$router.getRoutes() : []).map(r => r.path)
        if (paths.includes(key)) { this.$router.push(key).catch(() => {}); return }
        // 路径不存在 → 切换模块后跳（存完整路径）
        this._switchToRiskModule(key)
        return
      }

      // key → 候选路径表（与 riskModuleHome 保持一致，首项为最优先目标）
      const routeCandidates = {
        home:        ['/home/DHOME', '/home/index', '/home'],
        identify:    ['/identify/creation', '/identify/database', '/risk/identify/creation'],
        assessment:  ['/assessment/plan', '/GroupPlan', '/contractTools/GroupPlan', '/workbench/contractTools/GroupPlan', '/assessment/task', '/risk/assessment/plan'],
        standingBook:['/standingBook', '/risk/standingBook'],
        riskfill:    ['/riskfill/riskcreate', '/riskfill/riskfill', '/risk/riskfill/riskcreate'],
        treatment:   ['/treatment', '/risk/treatment'],
        monitor:     ['/monitor/supplier', '/risk/monitor/supplier', '/monitor/company', '/risk/monitor/company'],
        riskReview:  ['/riskReview/riskReviewBook', '/examination/competitors', '/risk/riskReview/riskReviewBook'],
        eventBase:   ['/events/eventBase', '/risk/events/eventBase'],
        eventView:   ['/events/eventView', '/risk/events/eventView'],
        riskModel:   ['/mxgl/fxyjgl', '/RiskSjfx/mxsjmxgl', '/mxgl/sjmxgl', '/risk/mxgl/sjmxgl', '/yjgl/sjmxgl', '/base/sjmxk'],
        report:      ['/report/normal', '/risk/report/normal'],
        cbxx:        ['/cbgl/cbxx', '/setting/cbgl/cbxx', '/risk/cbgl/cbxx'],
      }

      const candidates = routeCandidates[key] || []
      const flatPaths = (this.$router.getRoutes ? this.$router.getRoutes() : []).map(r => r.path)
      const cur = this.$route && this.$route.path

      // 一阶：精确匹配
      for (const c of candidates) {
        if (flatPaths.includes(c) && c !== cur) { this.$router.push(c).catch(() => {}); return }
      }

      // 二阶：尾段模糊匹配
      for (const c of candidates) {
        const segs = c.split('/').filter(Boolean)
        const tail = segs[segs.length - 1]
        if (!tail) continue
        const hit = flatPaths.find(p => p !== cur && (p === '/' + tail || p.endsWith('/' + tail)))
        if (hit) { this.$router.push(hit).catch(() => {}); return }
      }

      // 三阶：路由未注册 → 切换模块，存第一个候选完整路径（最精确的目标）
      const targetPath = candidates[0] || '/'
      this._switchToRiskModule(targetPath)
    },

    /**
     * 切换到风险管控模块(fxgk)，并在刷新后自动跳转到目标完整路径。
     * 与系统模块切换组件 changeModel 逻辑一致：setItem('model') + location.href='/'
     * 注意：已在 fxgk 时不做任何操作（由 _waitAndNavigatePath 轮询处理），避免跳首页。
     */
    _switchToRiskModule(targetPath) {
      const currentModel = localStorage.getItem('model')
      // 已经在风险管控模块，说明是轮询超时后的兜底调用，直接忽略
      if (currentModel === 'fxgk') return
      // 存储待跳转完整路径，刷新后由 _handlePendingRoute → _waitAndNavigatePath 处理
      sessionStorage.setItem('rwp_pending_route', targetPath)
      localStorage.setItem('model', 'fxgk')
      localStorage.setItem('modelname', '风险管控')
      location.href = '/'
    },
  }
}
</script>

<style lang="scss" scoped>
.risk-warning-panel {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  padding: 15px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  height: 100%;
  display: flex;
  flex-direction: column;

  .section-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 16px;
    font-weight: bold;
    color: #1e3c72;
    margin-bottom: 12px;
    i { font-size: 18px; }
    .title-link {
      margin-left: auto;
      font-size: 12px;
      color: #409eff;
      padding: 0;
    }
  }

  .panel-content {
    flex: 1;
    overflow-y: auto;
    display: flex;
    flex-direction: column;
    gap: 14px;

    &::-webkit-scrollbar { width: 4px; }
    &::-webkit-scrollbar-track { background: #f1f1f1; border-radius: 2px; }
    &::-webkit-scrollbar-thumb { background: #c1c1c1; border-radius: 2px; }
  }

  .subsection-title {
    font-size: 13px;
    font-weight: bold;
    color: #555;
    margin-bottom: 8px;
    padding-bottom: 4px;
    border-bottom: 1px solid #f0f0f0;
  }

  /* ===== KPI 统计卡 ===== */
  .kpi-section {
    .kpi-grid {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 8px;
      .kpi-card {
        display: flex;
        align-items: center;
        gap: 8px;
        padding: 8px 10px;
        border-radius: 8px;
        cursor: pointer;
        transition: all 0.2s;
        border: 1px solid transparent;
        &:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(0,0,0,0.1); }
        .kpi-icon { font-size: 18px; flex-shrink: 0; }
        .kpi-info {
          .kpi-value { font-size: 18px; font-weight: 800; line-height: 1.2; }
          .kpi-label { font-size: 10px; color: #888; margin-top: 1px; }
        }
        &.kpi-color-1 { background: #fff7e6; border-color: #ffd591; .kpi-icon { color: #fa8c16; } .kpi-value { color: #d46b08; } }
        &.kpi-color-2 { background: #f6ffed; border-color: #b7eb8f; .kpi-icon { color: #52c41a; } .kpi-value { color: #389e0d; } }
        &.kpi-color-3 { background: #e6f7ff; border-color: #91d5ff; .kpi-icon { color: #1890ff; } .kpi-value { color: #096dd9; } }
        &.kpi-color-4 { background: #fff1f0; border-color: #ffa39e; .kpi-icon { color: #f5222d; } .kpi-value { color: #cf1322; } }
        &.kpi-color-5 { background: #f9f0ff; border-color: #d3adf7; .kpi-icon { color: #722ed1; } .kpi-value { color: #531dab; } }
      }
    }
  }

  /* ===== 业务流转链路 ===== */
  .flow-section {
    .flow-nodes {
      display: flex;
      align-items: center;
      flex-wrap: wrap;
      gap: 4px;
      .flow-node {
        display: flex;
        align-items: center;
        gap: 4px;
        cursor: pointer;
        .node-icon {
          width: 28px; height: 28px;
          border-radius: 6px;
          display: flex; align-items: center; justify-content: center;
          font-size: 13px;
          transition: all 0.2s;
          &:hover { transform: scale(1.1); }
          &.node-color-1 { background: #e6f7ff; color: #1890ff; }
          &.node-color-2 { background: #f6ffed; color: #52c41a; }
          &.node-color-3 { background: #fff7e6; color: #fa8c16; }
          &.node-color-4 { background: #fff1f0; color: #f5222d; }
          &.node-color-5 { background: #f9f0ff; color: #722ed1; }
          &.node-color-6 { background: #e6fffb; color: #13c2c2; }
        }
        .node-label { font-size: 11px; color: #555; white-space: nowrap; }
        .flow-arrow { font-size: 10px; color: #bbb; }
      }
    }
  }

  /* ===== 快捷入口 ===== */
  .module-section {
    .module-grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 8px;
      .module-item {
        display: flex;
        align-items: center;
        gap: 8px;
        padding: 8px 10px;
        background: #f8f9fa;
        border-radius: 8px;
        cursor: pointer;
        transition: all 0.2s;
        border: 1px solid #eee;
        &:hover { background: #e9ecef; border-color: #1890ff; transform: translateY(-1px); }
        .mod-icon { font-size: 16px; flex-shrink: 0; }
        .mod-label { font-size: 12px; color: #333; font-weight: 500; }
        &:nth-child(1) .mod-icon { color: #1890ff; }
        &:nth-child(2) .mod-icon { color: #52c41a; }
        &:nth-child(3) .mod-icon { color: #fa8c16; }
        &:nth-child(4) .mod-icon { color: #722ed1; }
      }
    }
  }
}
</style>
