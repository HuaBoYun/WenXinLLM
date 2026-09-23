<template>
  <div class="audit-legal-panel">
    <div class="section-title">
      <i class="el-icon-document-checked"></i>
      审计管理 &amp; 法律案件
    </div>

    <div class="panel-content">
      <!-- 审计项目计划完成情况一览表 -->
      <div class="audit-section">
        <div class="subsection-title audit-title">
          <i class="el-icon-document-checked subsection-icon audit-icon"></i>
          审计项目计划完成情况一览表
          <el-button
            type="text"
            size="mini"
            class="goto-btn"
            :disabled="auditPlanList.length === 0"
            @click="auditPlanList.length > 0 && goToAuditDashboard()"
          >
            进入审计模块 <i class="el-icon-arrow-right"></i>
          </el-button>
        </div>
        <div v-loading="auditPlanLoading" class="plan-table-wrap">
          <div v-if="auditPlanList.length > 0" class="plan-table">
            <div class="plan-table-header">
              <span class="col-unit">被审计单位</span>
              <span class="col-num">已完成</span>
              <span class="col-num">未完成</span>
              <span class="col-num">总数</span>
            </div>
            <div
              v-for="(item, index) in auditPlanList"
              :key="index"
              class="plan-table-row clickable"
              @click="goToAuditDashboard"
            >
              <span class="col-unit" :title="item.bsjdw">{{ item.bsjdw }}</span>
              <span class="col-num success-text">{{ item.wcs !== '' && item.wcs !== null && item.wcs !== undefined ? item.wcs : 0 }}</span>
              <span class="col-num warning-text">{{ item.wwcs !== '' && item.wwcs !== null && item.wwcs !== undefined ? item.wwcs : 0 }}</span>
              <span class="col-num">{{ item.zs !== '' && item.zs !== null && item.zs !== undefined ? item.zs : 0 }}</span>
            </div>
          </div>
          <div v-else-if="!auditPlanLoading" class="no-data">
            <i class="el-icon-info"></i>
            <span>当前公司暂无审计项目数据</span>
          </div>
        </div>
      </div>

      <!-- 纠纷登记列表 -->
      <div class="legal-section">
        <div class="subsection-title legal-title">
          <i class="el-icon-suitcase subsection-icon legal-icon"></i>
          纠纷登记
          <el-button
            type="text"
            size="mini"
            class="goto-btn"
            @click="goToDisputePage()"
          >
            进入法务模块 <i class="el-icon-arrow-right"></i>
          </el-button>
        </div>
        <div v-loading="disputeLoading" class="dispute-table-wrap">
          <div v-if="disputeList.length > 0" class="dispute-table">
            <div class="dispute-table-header">
              <span class="col-no">登记编号</span>
              <span class="col-name">纠纷名称</span>
              <span class="col-type">纠纷类型</span>
              <span class="col-status">状态</span>
            </div>
            <div
              v-for="(item, index) in disputeList"
              :key="index"
              class="dispute-table-row clickable"
              @click="goToDisputePage"
            >
              <span class="col-no" :title="item.disputeno">{{ item.disputeno || '' }}</span>
              <span class="col-name" :title="item.disputeitem">{{ item.disputeitem || '' }}</span>
              <span class="col-type">{{ item.disputetype || '' }}</span>
              <span class="col-status" :class="getDisputeStatusClass(item.disputestatus)">
                {{ getDisputeStatusText(item.disputestatus) }}
              </span>
            </div>
          </div>
          <div v-else-if="!disputeLoading" class="no-data">
            <i class="el-icon-info"></i>
            <span>当前公司暂无纠纷登记数据</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { getPlanCountData } from '@/api/audit/sjfx'
import { getcaseInformationList } from '@/api/fwgl/legal'
import { getAuthListForUser } from '@/api/setting/auths'

export default {
  name: 'AuditLegalPanel',
  props: {
    enterpriseId: {
      type: String,
      default: ''
    },
    // 当前公司名称，用于前端按 bsjdw 字段过滤审计数据
    enterpriseName: {
      type: String,
      default: ''
    },
    // 保留原有 props 兼容性，不影响父组件传参
    auditData: { type: Object, default: null },
    legalData: { type: Object, default: null },
    loading: { type: Boolean, default: false }
  },
  data() {
    return {
      // 审计项目计划完成情况
      auditPlanList: [],
      auditPlanLoading: false,
      // 纠纷登记列表
      disputeList: [],
      disputeLoading: false
    }
  },
  watch: {
    /**
     * 监听 enterpriseId 变化（公司切换时父组件会同步更新 id 和 name）。
     * immediate: true 确保初始化时就能按当前公司过滤。
     */
    enterpriseId: {
      handler(newVal, oldVal) {
        if (newVal !== oldVal) {
          this.loadAuditPlanData()
          this.loadDisputeData()
        }
      },
      immediate: true
    },
    /**
     * 监听 enterpriseName 变化。
     * plancount 接口返回全量数据，需要用公司名称做前端过滤；
     * 名称到位后重新过滤展示（id 变化时名称可能稍晚到达）。
     */
    enterpriseName: {
      handler(newVal, oldVal) {
        if (newVal !== oldVal && newVal) {
          this.loadAuditPlanData()
          this.loadDisputeData()
        }
      },
      immediate: false
    }
  },
  mounted() {
    // watch immediate:true 已在 enterpriseId 有值时触发加载；
    // 若 enterpriseId 初始为空（父组件还未传值），则在此兜底发起一次请求。
    if (!this.enterpriseId) {
      this.loadAuditPlanData()
      this.loadDisputeData()
    }
    // 检测是否有跨模块跳转的待处理路径
    this._handlePendingRoute()
  },
  methods: {
    /**
     * 获取当前公司 orgid。
     * 优先使用父组件传入的 enterpriseId prop。
     * 兜底从 localStorage userInfo 中取，兼容 orgid / id 两种字段名。
     */
    getCurrentOrgId() {
      if (this.enterpriseId !== null && this.enterpriseId !== undefined && this.enterpriseId !== '') {
        return String(this.enterpriseId)
      }
      try {
        const info = JSON.parse(localStorage.getItem('userInfo') || '{}')
        const org = info.currentOrg || {}
        return String(org.orgid || org.id || org.orgId || '')
      } catch (e) { return '' }
    },

    /**
     * 获取当前公司名称。
     * 优先使用父组件传入的 enterpriseName prop（随公司切换实时更新）。
     * 兜底从 localStorage userInfo 中取。
     */
    getCurrentOrgName() {
      // 优先用 prop（父组件已保证随公司切换更新）
      if (this.enterpriseName && this.enterpriseName.trim()) {
        return this.enterpriseName.trim()
      }
      try {
        const info = JSON.parse(localStorage.getItem('userInfo') || '{}')
        const org = info.currentOrg || {}
        return org.orgname || org.orgName || org.name || ''
      } catch (e) { return '' }
    },

    /**
     * 加载审计项目计划完成情况一览表数据（按当前公司过滤）。
     *
     * 后端 plancount 接口只按年份查询，返回所有公司的汇总数据（bsjdw 字段为被审计单位名称）。
     * 前端用当前公司名称对 bsjdw 做精确匹配过滤，实现公司联动。
     */
    async loadAuditPlanData() {
      this.auditPlanLoading = true
      this.auditPlanList = []
      try {
        const params = { year: new Date().getFullYear() }
        const res = await getPlanCountData(params)
        if (res && res.code === 1 && res.data) {
          const list = res.data.list || []
          if (list.length === 0) {
            this.auditPlanList = []
            return
          }

          const orgName = this.getCurrentOrgName()

          let filtered = list
          if (orgName) {
            // 用公司名称精确匹配 bsjdw（被审计单位）
            const exact = list.filter(item =>
              item.bsjdw && item.bsjdw.trim() === orgName
            )
            if (exact.length > 0) {
              // 精确匹配到了，只展示当前公司的数据
              filtered = exact
            } else {
              // 精确匹配不到：尝试包含匹配（公司名称可能有简称/全称差异）
              const fuzzy = list.filter(item =>
                item.bsjdw && (
                  item.bsjdw.includes(orgName) || orgName.includes(item.bsjdw)
                )
              )
              // 模糊匹配到了就用，否则说明当前公司无审计数据，展示空
              filtered = fuzzy.length > 0 ? fuzzy : []
            }
          }
          // 最终映射，最多展示 8 条
          this.auditPlanList = filtered.map(item => ({
            bsjdw: item.bsjdw || '',
            wcs:  item.wcs  !== null && item.wcs  !== undefined ? item.wcs  : 0,
            wwcs: item.wwcs !== null && item.wwcs !== undefined ? item.wwcs : 0,
            zs:   item.zs   !== null && item.zs   !== undefined ? item.zs   : 0
          })).slice(0, 8)
        } else {
          this.auditPlanList = []
        }
      } catch (e) {
        console.error('[AuditLegalPanel] loadAuditPlanData error:', e)
        this.auditPlanList = []
      } finally {
        this.auditPlanLoading = false
      }
    },

    /**
     * 加载纠纷登记列表数据（按当前公司过滤）。
     * 后端 caseInformationList 接口通过 companyId 参数过滤，
     * 传入当前公司 orgid 即可实现公司联动。
     */
    async loadDisputeData() {
      this.disputeLoading = true
      this.disputeList = []
      try {
        const orgId = this.getCurrentOrgId()
        const params = {
          pageNumber: 1,
          pageSize: 8,
          flowid: '698855'
        }
        // 后端接口参数名为 companyId（非 orgid），必须用正确的参数名才能过滤
        if (orgId) params.companyId = orgId
        const res = await getcaseInformationList(params)
        if (res && res.date) {
          const tlist = res.date.tlist || []
          this.disputeList = tlist.map(item => ({
            disputeno:     item.disputeno     || '',
            disputeitem:   item.disputeitem   || '',
            disputetype:   item.disputetype   || '',
            disputestatus: item.disputestatus,
            disputeid:     item.disputeid
          }))
        } else {
          this.disputeList = []
        }
      } catch (e) {
        console.error('[AuditLegalPanel] loadDisputeData error:', e)
        this.disputeList = []
      } finally {
        this.disputeLoading = false
      }
    },

    /**
     * 跳转至审计驾驶舱（智能审计模块 znsj → @/views/audit/home/sjfx）
     *
     * 注意：router/index.js 里的 /znsj 是无 Layout 的裸路由（全屏模式），
     * 不能直接 push，必须走三阶模块切换后使用动态注册的 auditSjfx 路由（有 Layout）。
     * 因此候选路径中去掉 /znsj，只保留动态路由中带 Layout 的路径。
     */
    goToAuditDashboard() {
      const targetPaths = ['/auditSjfx', '/audit/home/sjfx']
      this._navigateToModule('znsj', '智能审计', targetPaths)
    },

    /**
     * 跳转至智慧法务-纠纷管理-纠纷登记（fwgl 模块 → @/views/fwgl/legal/dispute）。
     *
     * 使用 _navigateToModule 的 SPA 内切换（router.replace 而非 location.href），
     * 保留浏览器历史栈，用户点回退可以正常回到监管驾驶舱，不会出现 404。
     */
    goToDisputePage() {
      const targetPaths = ['/legal/dispute', '/fwgl/legal/dispute', '/dispute']
      this._navigateToModule('fwgl', '智慧法务', targetPaths)
    },

    /**
     * 通用模块切换跳转方法。
     * 流程：
     *   1. 若目标路由已在当前路由表中注册 → 直接 push（当前模块已包含该路由）
     *   2. 若当前模块已是目标模块 → 直接 push 第一个候选路径
     *   3. 否则 → 切换模块（权限检查 + store dispatch + router.replace('/')），
     *              并将目标路径存入 sessionStorage，由 _handlePendingRoute 在新模块加载后跳转
     *
     * @param {string} moduleKey   模块标识，如 'znsj'、'fwgl'
     * @param {string} moduleName  模块名称，如 '智能审计'、'智慧法务'
     * @param {string[]} paths     目标路径候选列表（按优先级排列）
     */
    async _navigateToModule(moduleKey, moduleName, paths) {
      const flatPaths = (this.$router.getRoutes ? this.$router.getRoutes() : []).map(r => r.path)
      const cur = this.$route && this.$route.path

      // 一阶：精确匹配已注册路由 → 直接跳（无需切换模块）
      for (const p of paths) {
        if (flatPaths.includes(p) && p !== cur) {
          this.$router.push(p).catch(() => {})
          return
        }
      }

      // 二阶：尾段模糊匹配
      for (const p of paths) {
        const tail = p.split('/').filter(Boolean).pop()
        if (!tail) continue
        const hit = flatPaths.find(fp => fp !== cur && (fp === '/' + tail || fp.endsWith('/' + tail)))
        if (hit) {
          this.$router.push(hit).catch(() => {})
          return
        }
      }

      // 三阶：路由未注册，需要切换模块
      const currentModel = localStorage.getItem('model')
      const targetPath = paths[0] || '/'

      // 若已在目标模块但路由还未注册（极少情况），直接尝试 push
      if (currentModel === moduleKey) {
        this.$router.push(targetPath).catch(() => {})
        return
      }

      // 权限检查
      let authRes
      try {
        authRes = await getAuthListForUser({ moduleType: moduleKey })
      } catch (e) {
        this.$message.error('权限查询失败，请稍后重试')
        return
      }
      const rightList = authRes && authRes.data && authRes.data.rightList
      if (!rightList || rightList.length === 0) {
        this.$message.warning('您暂无该模块的访问权限，请联系管理员')
        return
      }

      // 存储待跳转路径，切换完成后由 _handlePendingRoute 处理
      sessionStorage.setItem('alp_pending_module', moduleKey)
      sessionStorage.setItem('alp_pending_route', targetPath)

      // 切换模块（与 changeModel 逻辑一致）
      localStorage.setItem('model', moduleKey)
      localStorage.setItem('modelname', moduleName)
      try {
        await this.$store.dispatch('tabs/resetVisitedRoutes')
        await this.$store.dispatch('routes/setRoutes')
        await this.$router.replace('/')
      } catch (e) {
        // store dispatch 失败时降级为刷新页面
        location.href = '/'
      }
    },

    /**
     * 检测 sessionStorage 中是否有待跳转路径，有则在路由注册完成后执行跳转。
     * 使用轮询等待动态路由注册完成，最多等待 3 秒。
     */
    _handlePendingRoute() {
      const pendingModule = sessionStorage.getItem('alp_pending_module')
      const pendingPath = sessionStorage.getItem('alp_pending_route')
      if (!pendingPath || !pendingModule) return

      // 仅在目标模块下才执行跳转，避免其他模块误触发
      const currentModel = localStorage.getItem('model')
      if (currentModel !== pendingModule) return

      // 清除，防止重复触发
      sessionStorage.removeItem('alp_pending_module')
      sessionStorage.removeItem('alp_pending_route')

      // 轮询等待路由注册完成（最多 3 秒，每 200ms 检测一次）
      this._waitAndNavigate(pendingPath, 0, 15)
    },

    /**
     * 轮询等待目标路径注册完成后跳转
     */
    _waitAndNavigate(path, attempt, maxTries) {
      const flatPaths = (this.$router.getRoutes ? this.$router.getRoutes() : []).map(r => r.path)
      const cur = this.$route && this.$route.path

      // 精确匹配
      if (flatPaths.includes(path) && path !== cur) {
        this.$router.push(path).catch(() => {})
        return
      }

      // 尾段模糊匹配
      const tail = path.split('/').filter(Boolean).pop()
      if (tail) {
        const hit = flatPaths.find(p => p !== cur && (p === '/' + tail || p.endsWith('/' + tail)))
        if (hit) {
          this.$router.push(hit).catch(() => {})
          return
        }
      }

      if (attempt < maxTries) {
        setTimeout(() => { this._waitAndNavigate(path, attempt + 1, maxTries) }, 200)
      }
    },

    /**
     * 获取纠纷状态文本
     */
    getDisputeStatusText(status) {
      const map = {
        0: '未审批', 1: '审批中', 2: '已退回',
        3: '已撤销', 4: '已终止', 5: '已跟踪', 6: '已完成'
      }
      return map[status] !== undefined ? map[status] : '未审批'
    },

    /**
     * 获取纠纷状态样式类
     */
    getDisputeStatusClass(status) {
      const map = {
        0: 'status-default', 1: 'status-active', 2: 'status-warning',
        3: 'status-closed',  4: 'status-closed',  5: 'status-success', 6: 'status-success'
      }
      return map[status] || 'status-default'
    }
  }
}
</script>

<style lang="scss" scoped>
.audit-legal-panel {
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
    margin-bottom: 15px;
    i { font-size: 18px; }
  }

  .panel-content {
    flex: 1;
    overflow-y: auto;
    display: flex;
    flex-direction: column;
    gap: 14px;

    .subsection-title {
      display: flex;
      align-items: center;
      gap: 6px;
      font-size: 14px;
      font-weight: bold;
      color: #444;
      margin-bottom: 8px;
      padding-bottom: 6px;
      border-bottom: 2px solid #f0f0f0;

      .subsection-icon {
        font-size: 15px;
        border-radius: 4px;
        padding: 3px;
      }

      .goto-btn {
        margin-left: auto;
        font-size: 11px;
        padding: 0;
        color: #1890ff;
      }

      &.audit-title {
        border-bottom-color: #d0e8ff;
        .audit-icon { color: #1890ff; background: #e6f4ff; }
      }

      &.legal-title {
        border-bottom-color: #ffd6d6;
        .legal-icon { color: #f56c6c; background: #fff0f0; }
      }
    }

    // ---- 审计项目计划完成情况一览表 ----
    .plan-table-wrap {
      min-height: 60px;
    }

    .plan-table {
      font-size: 12px;

      .plan-table-header,
      .plan-table-row {
        display: flex;
        align-items: center;
        padding: 5px 6px;
        gap: 4px;
      }

      .plan-table-header {
        background: #f0f5ff;
        border-radius: 4px;
        font-weight: bold;
        color: #1e3c72;
        margin-bottom: 2px;
      }

      .plan-table-row {
        border-bottom: 1px solid #f0f0f0;
        transition: background 0.15s;

        &.clickable {
          cursor: pointer;
          &:hover { background: #e8f4ff; border-radius: 4px; }
        }

        &:last-child { border-bottom: none; }
      }

      .col-unit {
        flex: 1;
        min-width: 0;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        color: #333;
      }

      .col-num {
        width: 44px;
        text-align: center;
        flex-shrink: 0;
        color: #1e3c72;
        font-weight: 600;
      }

      .success-text { color: #2e7d32; }
      .warning-text { color: #e65100; }
    }

    // ---- 纠纷登记列表 ----
    .dispute-table-wrap {
      min-height: 60px;
    }

    .dispute-table {
      font-size: 12px;

      .dispute-table-header,
      .dispute-table-row {
        display: flex;
        align-items: center;
        padding: 5px 6px;
        gap: 4px;
      }

      .dispute-table-header {
        background: #fff0f0;
        border-radius: 4px;
        font-weight: bold;
        color: #c62828;
        margin-bottom: 2px;
      }

      .dispute-table-row {
        border-bottom: 1px solid #f0f0f0;
        transition: background 0.15s;

        &.clickable {
          cursor: pointer;
          &:hover { background: #fff5f5; border-radius: 4px; }
        }

        &:last-child { border-bottom: none; }
      }

      .col-no {
        width: 90px;
        flex-shrink: 0;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        color: #1890ff;
      }

      .col-name {
        flex: 1;
        min-width: 0;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        color: #333;
      }

      .col-type {
        width: 60px;
        flex-shrink: 0;
        text-align: center;
        color: #666;
      }

      .col-status {
        width: 52px;
        flex-shrink: 0;
        text-align: center;
        font-size: 11px;
        padding: 1px 4px;
        border-radius: 3px;

        &.status-default  { color: #666; background: #f5f5f5; }
        &.status-active   { color: #ef6c00; background: #fff3e0; }
        &.status-warning  { color: #c62828; background: #ffebee; }
        &.status-closed   { color: #555; background: #f0f0f0; }
        &.status-success  { color: #2e7d32; background: #e8f5e8; }
      }
    }

    .no-data {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 8px;
      color: #999;
      font-size: 12px;
      padding: 20px;
      i { font-size: 16px; }
    }
  }
}
</style>
