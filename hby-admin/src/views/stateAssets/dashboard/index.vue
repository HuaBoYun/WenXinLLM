<template>
  <div class="integrated-state-dashboard">

    <!-- ============================================================ -->
    <!-- SECTION 1：企业画像顶部（企业切换栏 + 左中右三列）            -->
    <!-- ============================================================ -->
    <section class="sec-enterprise">
      <!-- 顶部：EnterpriseInfoPanel（企业切换 + 操作按钮） -->
      <div class="enterprise-info-panel-wrap">
        <enterprise-info-panel
          :enterprise-data="enterpriseData"
          :loading="enterpriseLoading"
          :external-enterprise-list="enterpriseList"
          :external-enterprise-id="currentEnterpriseId"
          @enterprise-change="handleEnterpriseChange"
          @enterprise-ready="handleEnterpriseReady"
          @refresh="handleRefresh"
        />
      </div>

      <!-- 三列：企业基本信息 | 360度全息雷达图 | 企业全息画像 -->
      <div class="enterprise-three-cols">
        <!-- 左：企业基本信息 -->
        <div class="col-basic">
          <enterprise-basic-info-card
            :enterprise-data="enterpriseData && enterpriseData.enterpriseInfo"
            :enterprise-detail-data="enterpriseDetailData"
            :loading="enterpriseLoading || enterpriseDetailLoading"
          />
        </div>
        <!-- 中：公司穿透360度雷达图全息画像 -->
        <div class="col-radar">
          <hologram-radar-card
            :hologram-data="hologramData"
            :radar-data="radarData"
            :module-stats-data="moduleStatsData"
            :loading="hologramLoading || radarLoading"
          />
        </div>
        <!-- 右：企业全息画像 -->
        <div class="col-hologram">
          <enterprise-hologram-card
            :hologram-data="hologramData"
            :enterprise-detail-data="enterpriseDetailData"
            :loading="hologramLoading || enterpriseDetailLoading"
          />
        </div>
      </div>
    </section>

    <!-- ============================================================ -->
    <!-- SECTION 2：监管领域纵览（统计卡 + 12领域）              -->
    <!-- ============================================================ -->
    <section class="sec-domain-overview">
      <!-- 顶部统计卡片 -->
      <el-row :gutter="16" class="stats-row">
        <el-col :span="6" v-for="(stat, i) in statsData" :key="i">
          <el-card shadow="hover" class="stat-card">
            <div class="stat-content">
              <div class="stat-icon" :style="{ backgroundColor: stat.color }">
                <i :class="stat.icon"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ stat.value }}</div>
                <div class="stat-label">{{ stat.label }}</div>
                <div class="stat-change" :class="stat.changeType">
                  <i :class="stat.changeIcon"></i>{{ stat.change }}
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 12个穿透领域模块 -->
      <el-card shadow="never" class="domains-card">
        <div slot="header" class="card-header">
          <span><i class="el-icon-share"></i> 穿透监管领域总览</span>
          <div class="header-right">
            <el-tag type="info" size="small" style="margin-right:10px">共 12 个监管领域</el-tag>
            <el-button size="small" @click="refreshAllModules" :loading="loadingModules">
              <i class="el-icon-refresh"></i> 刷新数据
            </el-button>
          </div>
        </div>
        <el-row :gutter="14">
          <el-col :span="6" v-for="domain in domainModules" :key="domain.key" class="domain-col">
            <div class="domain-card" @click="openDomainDetail(domain)">
              <div class="dc-header" :style="{ borderTop: '3px solid ' + domain.color }">
                <div class="dc-icon-wrap" :style="{ background: domain.color + '18' }">
                  <i :class="domain.icon" :style="{ color: domain.color, fontSize: '22px' }"></i>
                </div>
                <div class="dc-title-area">
                  <div class="dc-name">{{ domain.name }}</div>
                  <div class="dc-desc">{{ domain.desc }}</div>
                </div>
                <el-badge
                  v-if="getModuleAlertCount(domain.key) > 0"
                  :value="getModuleAlertCount(domain.key)"
                  type="danger"
                />
              </div>
              <div class="dc-stats" v-loading="loadingModules && !moduleStatsData[domain.key]">
                <div class="ds-item" v-for="stat in getModuleStats(domain.key)" :key="stat.label">
                  <div class="ds-value" :class="stat.cls">{{ stat.value }}</div>
                  <div class="ds-label">{{ stat.label }}</div>
                </div>
              </div>
              <div class="dc-action">
                <el-button type="text" size="mini" :style="{ color: domain.color }" @click.stop="openDomainDetail(domain)">
                  查看详情 <i class="el-icon-zoom-in"></i>
                </el-button>
                <el-button type="text" size="mini" @click.stop="goToModule(domain.dashboardRoute || domain.route)">
                  <i class="el-icon-odometer" style="font-size:12px"></i> 驾驶舱
                </el-button>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-card>
    </section>

    <!-- ============================================================ -->
    <!-- SECTION 3：风险分析区（风险预警 + 关键财务指标 + 审计法律 + 指标预警） -->
    <!-- ============================================================ -->
    <section class="sec-risk-analysis">
      <!-- 三列内容 -->
      <div class="risk-three-cols">
        <div class="risk-col">
          <risk-warning-panel
            :enterprise-id="currentEnterpriseId"
            :risk-data="riskData"
            :loading="riskLoading"
          />
        </div>
        <div class="risk-col center">
          <key-financial-indicators-card
            :indicators-data="radarData && radarData.keyIndicators"
            :loading="radarLoading"
            @refresh="loadFinancialRadarData"
          />
        </div>
        <div class="risk-col">
          <audit-legal-panel
            :enterprise-id="currentEnterpriseId"
            :enterprise-name="currentEnterpriseName"
            :audit-data="auditData"
            :legal-data="legalData"
            :loading="auditLoading"
          />
        </div>
      </div>

      <!-- 实时监控看版（指标预警）独占一行 -->
      <div class="indicator-warning-row">
        <indicator-warning-panel
          :enterprise-id="currentEnterpriseId"
          :enterprise-name="currentEnterpriseName"
          title="实时监控看版"
        >
          <template #action>
            <el-button size="mini" type="primary" plain @click="openMonitoringScreen">
              <i class="el-icon-full-screen"></i> 监控大屏
            </el-button>
          </template>
        </indicator-warning-panel>
      </div>
    </section>

    <!-- ============================================================ -->
    <!-- SECTION 4：决策支持中心                                       -->
    <!-- ============================================================ -->
    <section class="sec-decision">
      <el-card shadow="never" class="decision-card">
        <div slot="header" class="card-header">
          <span>决策支持中心</span>
          <el-button type="text" @click="openDecisionCenter">
            进入决策中心 <i class="el-icon-arrow-right"></i>
          </el-button>
        </div>
        <div class="decision-preview"></div>
      </el-card>
    </section>

    <!-- ============================================================ -->
    <!-- 弹窗                                                          -->
    <!-- ============================================================ -->
    <!-- 领域详情弹窗 -->
    <domain-detail-dialog
      :visible.sync="showDomainDetail"
      :domain="currentDomain"
      :stats="currentDomainStats"
      :alert-count="currentDomainAlertCount"
      @close="handleDomainDialogClose"
    />

    <!-- 投资穿透专属弹窗：项目列表 + 风险列表 -->
    <invest-penetration-dialog
      :visible.sync="showInvestDialog"
      :enterprise-id="currentEnterpriseId"
    />

    <el-dialog
      title="实时监控大屏"
      :visible.sync="showMonitoringScreen"
      width="95%"
      :modal="false"
      :close-on-click-modal="false"
      custom-class="monitoring-dialog"
    >
      <MonitoringScreen v-if="showMonitoringScreen"
              :enterprise-id="currentEnterpriseId"
              :enterprise-name="currentEnterpriseName"
              :enterprise-data="enterpriseData"
              :radar-data="radarData"
              :hologram-data="hologramData"
              :module-stats-data="moduleStatsData"
              :domain-modules="domainModules"
            />
    </el-dialog>

    <el-dialog
      title="决策支持中心"
      :visible.sync="showDecisionCenter"
      width="90%"
      :modal="false"
      :close-on-click-modal="false"
      custom-class="decision-dialog"
    >
      <DecisionSupport v-if="showDecisionCenter" :enterprise-id="currentEnterpriseId" :enterprise-name="currentEnterpriseName" :module-stats-data="moduleStatsData" />
    </el-dialog>
  </div>
</template>

<script>
import MonitoringScreen from './components/MonitoringScreen.vue'
import DecisionSupport from './components/DecisionSupport.vue'
import HologramRadarCard from './components/HologramRadarCard.vue'
import DomainDetailDialog from './components/DomainDetailDialog.vue'
import InvestPenetrationDialog from './components/InvestPenetrationDialog.vue'

// enterpriseProfile 子组件
import EnterpriseInfoPanel         from '@/views/risk/home/enterpriseProfile/components/EnterpriseInfoPanel.vue'
import EnterpriseBasicInfoCard     from '@/views/risk/home/enterpriseProfile/components/EnterpriseBasicInfoCard.vue'
import EnterpriseHologramCard      from '@/views/risk/home/enterpriseProfile/components/EnterpriseHologramCard.vue'
import RiskWarningPanel            from '@/views/risk/home/enterpriseProfile/components/RiskWarningPanel.vue'
import AuditLegalPanel             from '@/views/risk/home/enterpriseProfile/components/AuditLegalPanel.vue'
import KeyFinancialIndicatorsCard  from '@/views/risk/home/enterpriseProfile/components/KeyFinancialIndicatorsCard.vue'
import IndicatorWarningPanel       from '@/views/risk/home/enterpriseProfile/components/IndicatorWarningPanel.vue'

// 顶部汇总统计 & 实时预警 & 决策支持
import { getDashboardStats, getRealtimeAlerts, getEnterpriseAuditData, getEnterpriseLegalData, getDecisionSupportData } from '@/api/stateAssets/dashboard'

// 企业画像 API
import * as enterpriseProfileApi from '@/api/risk/enterpriseProfile'

// 风险预警 API
import { getRiskWarningList } from '@/api/mxgl'

// 组织机构树（与切换公司组件使用同一接口）
import { findOrganizationDataA } from '@/api/setting/org'

// 12个领域统计 API
import { getInvestStatistics }          from '@/api/stateAssets/investPenetration'
import { getFinancialRiskStatistics }   from '@/api/stateAssets/financialRiskPenetration'
import { getProcurementStatistics }     from '@/api/stateAssets/procurementPenetration'
import { getMilitaryStatistics }        from '@/api/stateAssets/militaryPenetration'
import { getOverseasStatistics }        from '@/api/stateAssets/overseasPenetration'
import { getIndustryOverview }          from '@/api/stateAssets/industryPenetration'
import { getContractStatistics }        from '@/api/stateAssets/contractPenetration'
import { getAccountingStatistics }      from '@/api/stateAssets/accountingPenetration'
import { getFinanceStatistics }         from '@/api/stateAssets/financePenetration'
import { getFundFlowPenetrationStats }  from '@/api/stateAssets/financialPenetration'
import { getSalaryDashboard }           from '@/api/stateAssets/salaryPenetration'
import { getPropertyDashboard }         from '@/api/stateAssets/propertyRight'

export default {
  name: 'IntegratedStateDashboard',
  components: {
    MonitoringScreen,
    DecisionSupport,
    HologramRadarCard,
    DomainDetailDialog,
    InvestPenetrationDialog,
    EnterpriseInfoPanel,
    EnterpriseBasicInfoCard,
    EnterpriseHologramCard,
    RiskWarningPanel,
    AuditLegalPanel,
    KeyFinancialIndicatorsCard,
    IndicatorWarningPanel
  },
  data() {
    return {
      // ---- 企业相关 ----
      currentEnterpriseId: '',
      currentEnterpriseName: '',
      enterpriseList: [],
      enterpriseData: null,
      enterpriseDetailData: null,
      radarData: null,
      riskData: null,
      hologramData: null,
      auditData: null,
      legalData: null,
      updateTime: '--',
      enterpriseLoading: false,
      enterpriseDetailLoading: false,
      radarLoading: false,
      riskLoading: false,
      hologramLoading: false,
      auditLoading: false,
      refreshTimer: null,
      _dashboardInitialized: false,
      _isFirstLoad: true,

      // ---- 顶部统计 ----
      statsData: [
        { label: '监管企业总数', value: '--', change: '--', changeType: 'positive',
          changeIcon: 'el-icon-arrow-up', icon: 'el-icon-office-building', color: '#409EFF' },
        { label: '总资产规模', value: '--', change: '--', changeType: 'positive',
          changeIcon: 'el-icon-arrow-up', icon: 'el-icon-money', color: '#67C23A' },
        { label: '风险预警数', value: '--', change: '--', changeType: 'negative',
          changeIcon: 'el-icon-arrow-down', icon: 'el-icon-warning', color: '#F56C6C' },
        { label: '合规率', value: '--', change: '--', changeType: 'positive',
          changeIcon: 'el-icon-arrow-up', icon: 'el-icon-circle-check', color: '#E6A23C' }
      ],

      // ---- 12领域模块配置 ----
      domainModules: [
        { key: 'invest',      name: '投资穿透',  icon: 'el-icon-bank-card',       color: '#1890FF', desc: '投资项目全生命周期监管',    route: '/modelMonitor/Tzjkjsc',           dashboardRoute: '/modelMonitor/Tzjkjsc' },
        { key: 'financial',   name: '金融穿透',  icon: 'el-icon-bank',            color: '#13C2C2', desc: '金融风险穿透式监管',        route: '/equityPenetration/Jrfxjsc',      dashboardRoute: '/equityPenetration/Jrfxjsc' },
        { key: 'procurement', name: '采购穿透',  icon: 'el-icon-shopping-cart-2', color: '#722ED1', desc: '采购供应链全链条监管',      route: '/fxyj/Cgjkjsc',                   dashboardRoute: '/fxyj/Cgjkjsc' },
        { key: 'military',    name: '军品穿透',  icon: 'el-icon-s-flag',          color: '#F5222D', desc: '军品业务合规风险监管',      route: '/riskPenetration/Jpjkjsc',        dashboardRoute: '/riskPenetration/Jpjkjsc' },
        { key: 'overseas',    name: '境外穿透',  icon: 'el-icon-place',           color: '#FA8C16', desc: '境外单位经营风险监管',      route: '/enterprise/Jwjkjsc',             dashboardRoute: '/enterprise/Jwjkjsc' },
        { key: 'industry',    name: '行业穿透',  icon: 'el-icon-office-building', color: '#52C41A', desc: '行业布局竞争力分析',        route: '/industry/HYjkjsc',               dashboardRoute: '/industry/HYjkjsc' },
        { key: 'contract',    name: '合同穿透',  icon: 'el-icon-document',        color: '#EB2F96', desc: '合同全生命周期监管',        route: '/monitorExecute/HtJkjsc',         dashboardRoute: '/monitorExecute/HtJkjsc' },
        { key: 'accounting',  name: '会计穿透',  icon: 'el-icon-notebook-2',      color: '#2F54EB', desc: '会计凭证账簿穿透监管',      route: '/compliance/Jkctjkjsc',           dashboardRoute: '/compliance/Jkctjkjsc' },
        { key: 'finance',     name: '财务穿透',  icon: 'el-icon-data-analysis',   color: '#AD6800', desc: '财务报表合规穿透分析',      route: '/financialPenetration/dashboard', dashboardRoute: '/financialPenetration/dashboard' },
        { key: 'fund',        name: '资金穿透',  icon: 'el-icon-money',           color: '#006D75', desc: '资金流向全链路穿透监控',    route: '/ruleMonitor/MXfxyjgl',           dashboardRoute: '/ruleMonitor/MXfxyjgl' },
        { key: 'salary',      name: '薪酬穿透',  icon: 'el-icon-user-solid',      color: '#531DAB', desc: '薪酬分配合规性监管',        route: '/formMonitor/Xcjkjsc',            dashboardRoute: '/formMonitor/Xcjkjsc' },
        { key: 'property',    name: '产权穿透',  icon: 'el-icon-share',           color: '#237804', desc: '产权登记交易全链条监管',    route: '/assetPenetration/Cqjkjsc',       dashboardRoute: '/assetPenetration/Cqjkjsc' }
      ],
      moduleStatsData: {},
      loadingModules: false,

      // ---- 实时预警 ----
      realtimeAlerts: [],

      // ---- 弹窗 ----
      showMonitoringScreen: false,
      showDecisionCenter: false,
      showDomainDetail: false,
      showInvestDialog: false,
      currentDomain: null,

      // ---- 决策支持 ----
      decisionItems: [],
      decisionLoading: false
    }
  },
  mounted() {
    // 从 userInfo 初始化当前企业
    this.initFromUserInfo()
    // 初始化企业列表（完成后会自动调用 initEnterpriseData + loadDashboardStats + loadAllModuleStats）
    this.loadEnterpriseList()
    // 自动刷新
    this.refreshTimer = setInterval(() => this.silentRefresh(), 300000)
  },
  beforeDestroy() {
    if (this.refreshTimer) clearInterval(this.refreshTimer)
  },
  methods: {
    // ===================== 企业选择 =====================
    // 从 localStorage userInfo 初始化当前公司
    initFromUserInfo() {
      try {
        const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
        const org = userInfo.currentOrg || {}
        if (org.id) {
          this.currentEnterpriseId = String(org.id)
        }
        this.currentEnterpriseName = org.orgname || org.orgName || ''
      } catch (e) {
        console.warn('[initFromUserInfo]', e)
      }
    },

    async loadEnterpriseList() {
      try {
        // 与切换公司组件保持一致：调用 findOrganizationData 获取组织树，再递归打平
        const res = await findOrganizationDataA({ nodeId: '' })
        if (res && Array.isArray(res) && res.length > 0) {
          // 保存原始树结构，供统计子公司数量使用
          this._orgTreeRaw = res
          const flatten = (nodes) => {
            const result = []
            nodes.forEach(n => {
              result.push({ enterpriseId: String(n.id), enterpriseName: n.name || n.label })
              if (n.children && n.children.length > 0) {
                result.push(...flatten(n.children))
              }
            })
            return result
          }
          this.enterpriseList = flatten(res)
        }
      } catch (e) { console.error('[loadEnterpriseList]', e) }
      // 首次进入：始终用组织树第一个企业，避免"企业不存在"弹窗
      if (this.enterpriseList.length > 0) {
        const first = this.enterpriseList[0]
        // 如果当前ID不在列表中，或者为空，强制用第一个
        const exists = this.currentEnterpriseId && this.enterpriseList.some(e => String(e.enterpriseId) === String(this.currentEnterpriseId))
        if (!exists) {
          this.currentEnterpriseId = first.enterpriseId
          this.currentEnterpriseName = first.enterpriseName
        }
        if (!this.currentEnterpriseName) {
          this.currentEnterpriseName = first.enterpriseName
        }
      }
      // 加载企业详细信息 + 统计数据
      await this.initEnterpriseData()
      // 以下异步加载不阻塞页面展示
      this.loadDashboardStats()
      this.loadAllModuleStats()
      this.loadRealtimeAlerts()
      this.loadDecisionSupportData()
    },

    async initEnterpriseData() {
      await Promise.allSettled([
        this.loadEnterpriseInfo(),
        this.loadEnterpriseDetailInfo(),
        this.loadFinancialRadarData(),
        this.loadHologramData(),
        this.loadRiskData()
        // this.loadAuditData(),
        // this.loadLegalData()
      ])
      this.updateTime = new Date().toLocaleString('zh-CN')
    },

    async silentRefresh() {
      await this.initEnterpriseData()
      await this.loadAllModuleStats()
    },

    async handleEnterpriseChange(id) {
      this.currentEnterpriseId = id
      const found = this.enterpriseList.find(e => String(e.enterpriseId) === String(id))
      if (found) this.currentEnterpriseName = found.enterpriseName
      this._isFirstLoad = true
      // 清空旧数据，触发loading
      this.moduleStatsData = {}
      // 并行加载，互不阻塞
      this.initEnterpriseData()
      this.loadDashboardStats()
      this.loadAllModuleStats()
    },

    async handleRefresh() {
      try {
        const res = await enterpriseProfileApi.refreshEnterpriseData({ enterpriseId: this.currentEnterpriseId })
        if (res && (res.code === 1 || res.result === 200)) {
          this.$message.success('数据刷新成功')
        }
      } catch (e) { /* ignore */ }
      this._isFirstLoad = true
      await this.initEnterpriseData()
    },

    handleEnterpriseReady(id) {
      if (id && id !== this.currentEnterpriseId) {
        this.currentEnterpriseId = id
        this.initEnterpriseData()
      }
    },

    handleExport() {
      this.$message.info('正在导出企业数据报告…')
    },

    getEnterpriseStatusType() {
      const status = this.enterpriseData?.enterpriseInfo?.enterpriseStatus
      return { 'ACTIVE': 'success', 'INACTIVE': 'danger', 'PENDING': 'warning' }[status] || 'info'
    },
    getEnterpriseStatusText() {
      const info = this.enterpriseData?.enterpriseInfo
      return info?.enterpriseStatusName || info?.status || '正常运营'
    },

    // ===================== 企业数据加载 =====================
    async loadEnterpriseInfo() {
      this.enterpriseLoading = true
      try {
        const res = await enterpriseProfileApi.getEnterpriseInfo({
          enterpriseId: this.currentEnterpriseId, includeExtendedInfo: true
        })
        if (res && (res.code === 1 || res.result === 200)) {
          this.enterpriseData = res.data
        }
      } catch (e) { console.error('[loadEnterpriseInfo]', e) }
      finally { this.enterpriseLoading = false }
    },

    async loadEnterpriseDetailInfo() {
      this.enterpriseDetailLoading = true
      try {
        const res = await enterpriseProfileApi.getEnterpriseDetailInfo({ enterpriseId: this.currentEnterpriseId })
        if (res && (res.code === 1 || res.result === 200)) this.enterpriseDetailData = res.data
      } catch (e) { console.error('[loadEnterpriseDetailInfo]', e) }
      finally { this.enterpriseDetailLoading = false }
    },

    async loadFinancialRadarData() {
      this.radarLoading = true
      try {
        const res = await enterpriseProfileApi.getFinancialRadarData({
          enterpriseId: this.currentEnterpriseId, includeKeyIndicators: true
        })
        if (res && (res.code === 1 || res.result === 200)) this.radarData = res.data
      } catch (e) { console.error('[loadFinancialRadarData]', e) }
      finally { this.radarLoading = false }
    },

    async loadHologramData() {
      this.hologramLoading = true
      try {
        const res = await enterpriseProfileApi.getEnterpriseHologramData({ enterpriseId: this.currentEnterpriseId })
        if (res && (res.code === 1 || res.result === 200)) {
          this.hologramData = res.data
        } else {
          this.hologramData = null
        }
      } catch (e) {
        console.error('[loadHologramData]', e)
        this.hologramData = null
      }
      finally { this.hologramLoading = false }
    },

    async loadRiskData() {
      this.riskLoading = true
      try {
        const [panelRes, listRes] = await Promise.allSettled([
          enterpriseProfileApi.getRiskAuditPanelData({ enterpriseId: this.currentEnterpriseId }),
          getRiskWarningList({ companyName: this.currentEnterpriseName, pageSize: 20, pageNum: 1 })
        ])
        let data = {}
        if (panelRes.status === 'fulfilled' && panelRes.value &&
            (panelRes.value.code === 1 || panelRes.value.result === 200)) {
          data = panelRes.value.data || {}
        }
        // 将风险预警列表合并到 riskData
        if (listRes.status === 'fulfilled' && listRes.value && listRes.value.code === 1 && listRes.value.data) {
          const wList = listRes.value.data.list || []
          data.warningList = wList
          if (!data.trendData) {
            const pending   = wList.filter(w => w.warningStatus === 'PENDING').length
            const processed = wList.filter(w => w.warningStatus === 'PROCESSED').length
            data.trendData = { monthlyIncrease: wList.length, resolved: processed, pending }
          }
          if (!data.riskLevels) {
            data.riskLevels = [
              { level: 'HIGH',   levelName: '高风险', count: wList.filter(w => w.warningLevel === 'HIGH').length },
              { level: 'MEDIUM', levelName: '中风险', count: wList.filter(w => w.warningLevel === 'MEDIUM').length },
              { level: 'LOW',    levelName: '低风险', count: wList.filter(w => w.warningLevel === 'LOW').length }
            ]
          }
        }
        this.riskData = Object.keys(data).length > 0 ? data : null
      } catch (e) {
        console.error('[loadRiskData]', e)
        this.riskData = null
      } finally { this.riskLoading = false }
    },

    async loadAuditData() {
      this.auditLoading = true
      try {
        const res = await getEnterpriseAuditData({ enterpriseId: this.currentEnterpriseId })
        if (res && (res.code === 1 || res.result === 200) && res.data) {
          this.auditData = res.data
        } else {
          // 接口无数据时使用虚拟兜底数据
          this.auditData = {
            auditRecords: [
              {
                auditId: 'mock-audit-001',
                auditType: '1',
                auditTypeName: '内部审计',
                auditDate: '2025-03-15',
                auditResult: '2',
                auditResultName: '有问题',
                issueCount: 3
              },
              {
                auditId: 'mock-audit-002',
                auditType: '3',
                auditTypeName: '政府审计',
                auditDate: '2024-11-20',
                auditResult: '1',
                auditResultName: '通过',
                issueCount: 0
              }
            ],
            auditStats: {
              totalAudits: 8,
              totalIssues: 12,
              resolvedIssues: 9
            }
          }
        }
      } catch (e) {
        console.error('[loadAuditData]', e)
        // 异常时同样展示虚拟数据，保证面板不空白
        this.auditData = {
          auditRecords: [
            {
              auditId: 'mock-audit-001',
              auditType: '1',
              auditTypeName: '内部审计',
              auditDate: '2025-03-15',
              auditResult: '2',
              auditResultName: '有问题',
              issueCount: 3
            }
          ],
          auditStats: { totalAudits: 8, totalIssues: 12, resolvedIssues: 9 }
        }
      }
      finally { this.auditLoading = false }
    },

    async loadLegalData() {
      try {
        const res = await getEnterpriseLegalData({ enterpriseId: this.currentEnterpriseId })
        if (res && (res.code === 1 || res.result === 200) && res.data) {
          this.legalData = res.data
        } else {
          // 接口无数据时使用虚拟兜底数据
          this.legalData = {
            legalCases: [
              {
                caseId: 'mock-case-001',
                caseType: '1',
                caseTypeName: '民事案件',
                caseStatus: '1',
                caseStatusName: '进行中',
                caseTitle: '合同纠纷案（供应商违约索赔）',
                caseDate: '2025-01-08',
                involvedAmount: 3200000
              },
              {
                caseId: 'mock-case-002',
                caseType: '3',
                caseTypeName: '行政案件',
                caseStatus: '2',
                caseStatusName: '已结案',
                caseTitle: '环保行政处罚复议案',
                caseDate: '2024-09-12',
                involvedAmount: 500000
              }
            ],
            legalStats: {
              totalCases: 5,
              activeCases: 2,
              totalAmount: 8750000
            }
          }
        }
      } catch (e) {
        console.error('[loadLegalData]', e)
        // 异常时同样展示虚拟数据
        this.legalData = {
          legalCases: [
            {
              caseId: 'mock-case-001',
              caseType: '1',
              caseTypeName: '民事案件',
              caseStatus: '1',
              caseStatusName: '进行中',
              caseTitle: '合同纠纷案（供应商违约索赔）',
              caseDate: '2025-01-08',
              involvedAmount: 3200000
            }
          ],
          legalStats: { totalCases: 5, activeCases: 2, totalAmount: 8750000 }
        }
      }
    },

    // ===================== 顶部统计 =====================
    async loadDashboardStats() {
      try {
        // 监管企业总数：从组织树统计当前公司的所有子公司数量
        const totalEnterprises = this.getSubEnterpriseCount()
        this.statsData[0].value = totalEnterprises > 0 ? totalEnterprises + '家' : '--'

        // 总资产规模：从关键指标(hologramData/detailInfo已加载)中取TOTAL_ASSETS
        const keyMetrics = this.hologramData?.keyMetrics || this.enterpriseDetailData?.keyMetrics || []
        const totalAssetsMetric = keyMetrics.find(m => m.metricCode === 'TOTAL_ASSETS' || m.code === 'TOTAL_ASSETS')
        if (totalAssetsMetric) {
          const val = Number(totalAssetsMetric.metricValue || totalAssetsMetric.value || 0)
          this.statsData[1].value = val > 0 ? (val / 1e4).toFixed(0) + '亿元' : '--'
        } else {
          // 备选：从 financialRadar 接口的 keyIndicators 中取
          const ki = this.radarData?.keyIndicators || []
          const totalAssetKi = ki.find(k => k.indicatorCode === 'TOTAL_ASSETS')
          if (totalAssetKi && totalAssetKi.currentValue) {
            this.statsData[1].value = (totalAssetKi.currentValue / 1e4).toFixed(0) + '亿元'
          } else {
            this.statsData[1].value = '--'
          }
        }

        // 风险预警数+合规率：从后端dashboard接口获取（10秒超时）
        const dashTimeout = new Promise((_, reject) => setTimeout(() => reject(new Error('timeout')), 10000))
        const res = await Promise.race([getDashboardStats({ companyId: this.currentEnterpriseId }), dashTimeout]).catch(() => null)
        if (res && (res.code === 1 || res.result === 200) && res.data) {
          const d = res.data
          // 风险预警数：汇总各领域的 warningCount/riskCount
          let riskAlerts = 0
          Object.values(d).forEach(stat => {
            if (stat && typeof stat === 'object') {
              riskAlerts += (Number(stat.warningCount) || 0) + (Number(stat.riskCount) || 0)
            }
          })
          // 合规率：从各领域合规率取平均
          const rates = []
          Object.values(d).forEach(stat => {
            if (stat && stat.complianceRate) rates.push(Number(stat.complianceRate))
          })
          const complianceRate = rates.length ? (rates.reduce((a, b) => a + b, 0) / rates.length).toFixed(1) : '--'

          this.statsData[2].value = riskAlerts || '--'
          this.statsData[3].value = complianceRate !== '--' ? complianceRate + '%' : '--'
        }
      } catch (e) {
        console.error('[loadDashboardStats]', e)
      }
    },

    // 从组织树统计当前公司下的子公司数量
    getSubEnterpriseCount() {
      if (!this.enterpriseList || !this.enterpriseList.length) return 0
      // 组织树已经打平存在 enterpriseList 中
      // 需要从原始树结构中找到当前公司节点，统计其所有子节点数
      // 简化方案：如果当前是顶层公司，子公司数 = 列表总数 - 1(自身)
      // 如果是子公司，需要从树中递归查找
      const currentId = String(this.currentEnterpriseId)
      if (!this._orgTreeRaw) return Math.max(0, this.enterpriseList.length - 1)
      const countChildren = (nodes) => {
        for (const node of nodes) {
          if (String(node.id) === currentId) {
            // 找到当前节点，统计其所有子孙数量
            const countAll = (children) => {
              if (!children || !children.length) return 0
              let c = children.length
              children.forEach(child => { c += countAll(child.children) })
              return c
            }
            return countAll(node.children)
          }
          if (node.children && node.children.length > 0) {
            const found = countChildren(node.children)
            if (found >= 0) return found
          }
        }
        return -1 // 未找到
      }
      const result = countChildren(this._orgTreeRaw)
      return result >= 0 ? result : Math.max(0, this.enterpriseList.length - 1)
    },

    async loadRealtimeAlerts() {
      try {
        const res = await getRealtimeAlerts({ enterpriseId: this.currentEnterpriseId })
        if (res && (res.code === 1 || res.result === 200) && res.data) {
          this.realtimeAlerts = res.data
        }
      } catch (e) { console.error('[loadRealtimeAlerts]', e) }
    },

    // ===================== 决策支持 =====================
    async loadDecisionSupportData() {
      this.decisionLoading = true
      try {
        const res = await getDecisionSupportData({ enterpriseId: this.currentEnterpriseId })
        if (res && (res.code === 1 || res.result === 200) && res.data) {
          this.decisionItems = res.data
        }
      } catch (e) { console.error('[loadDecisionSupportData]', e) }
      finally { this.decisionLoading = false }
    },

    // ===================== 12领域统计 =====================
    async loadAllModuleStats() {
      this.loadingModules = true
      const orgId = this.currentEnterpriseId
      // 设置超时，避免后端未启动时无限等待
      const timeout = (ms) => new Promise((_, reject) => setTimeout(() => reject(new Error('timeout')), ms))
      const withTimeout = (fn) => Promise.race([fn(), timeout(10000)])
      await Promise.allSettled([
        this._loadStat('invest',      () => withTimeout(() => getInvestStatistics(orgId))),
        this._loadStat('financial',   () => withTimeout(() => getFinancialRiskStatistics(orgId))),
        this._loadStat('procurement', () => withTimeout(() => getProcurementStatistics(orgId))),
        this._loadStat('military',    () => withTimeout(() => getMilitaryStatistics(orgId))),
        this._loadStat('overseas',    () => withTimeout(() => getOverseasStatistics(orgId))),
        this._loadStat('industry',    () => withTimeout(() => getIndustryOverview(orgId))),
        this._loadStat('contract',    () => withTimeout(() => getContractStatistics(orgId))),
        this._loadStat('accounting',  () => withTimeout(() => getAccountingStatistics(orgId))),
        this._loadStat('finance',     () => withTimeout(() => getFinanceStatistics(orgId))),
        this._loadStat('fund',        () => withTimeout(() => getFundFlowPenetrationStats(orgId))),
        this._loadStat('salary',      () => withTimeout(() => getSalaryDashboard(orgId))),
        this._loadStat('property',    () => withTimeout(() => getPropertyDashboard(orgId)))
      ])
      this.loadingModules = false
    },

    async _loadStat(key, apiFn) {
      const STAT_MAP = {
        invest:      [['项目总数', 'totalProjects,projectCount', ''],   ['风险预警', 'warningCount,riskCount,highRiskCount', 'danger'], ['合规率', 'complianceRate', 'success']],
        financial:   [['融资总额', 'totalFinancing,financingAmount', ''],['对外担保', 'totalGuarantee,guaranteeAmount', ''],            ['风险数量', 'riskCount,warningCount', 'danger']],
        procurement: [['采购项目', 'totalProjects,projectCount', ''],   ['供应商数', 'supplierCount,totalSuppliers', ''],               ['风险预警', 'riskWarnings,warningCount', 'danger']],
        military:    [['任务数量', 'taskCount,totalTasks', ''],         ['资质数量', 'qualificationCount,totalQualifications', ''],     ['风险数量', 'riskCount,warningCount', 'danger']],
        overseas:    [['境外单位', 'unitCount,overseasCount,totalUnits', ''], ['覆盖国家', 'countryCount,countries', ''],              ['风险预警', 'riskCount,warningCount', 'danger']],
        industry:    [['行业数量', 'industryCount,totalIndustries', ''],['监管企业', 'companyCount,enterpriseCount', ''],              ['风险预警', 'riskCount,warningCount', 'danger']],
        contract:    [['合同总数', 'totalContracts,contractCount', ''], ['合同总额', 'totalAmount,contractAmount', ''],               ['风险预警', 'riskCount,warningCount', 'danger']],
        accounting:  [['凭证总数', 'voucherCount,totalVouchers', ''],  ['异常凭证', 'anomalyCount,abnormalCount', 'warning'],          ['风险预警', 'riskCount,warningCount', 'danger']],
        finance:     [['报表数量', 'statementCount,totalStatements', ''], ['财务风险', 'riskCount,financialRisks', 'danger'],          ['合规率', 'complianceRate', 'success']],
        fund:        [['流向总数', 'totalFlows,flowCount', ''],        ['异常流向', 'abnormalCount,abnormalFlows', 'warning'],         ['风险预警', 'riskCount,warningCount', 'danger']],
        salary:      [['工资总额', 'totalSalary,salaryTotal', ''],     ['高管异常', 'executiveAbnormal,anomalyCount', 'warning'],      ['合规率', 'complianceRate', 'success']],
        property:    [['产权登记', 'registryCount,propertyCount,totalCount', ''], ['产权交易', 'tradingCount,transactionCount', ''], ['风险预警', 'riskCount,warningCount', 'danger']]
      }
      try {
        const res = await apiFn()
        const d   = (res && res.data) || {}
        const map = STAT_MAP[key] || []
        const stats = map.map(([label, fields, cls]) => {
          const val = this._pick(d, fields)
          return { label, value: this._fmtVal(val, cls), cls }
        })
        this.$set(this.moduleStatsData, key, stats)
      } catch (e) {
        console.error(`[_loadStat:${key}]`, e)
        // 接口异常时显示占位符
        const map = STAT_MAP[key] || []
        const stats = map.map(([label, , cls]) => {
          return { label, value: '--', cls }
        })
        this.$set(this.moduleStatsData, key, stats)
      }
    },

    _pick(obj, fields) {
      for (const f of fields.split(',')) {
        if (obj[f] !== undefined && obj[f] !== null && obj[f] !== '') return obj[f]
      }
      return null
    },

    _fmtVal(val, cls) {
      if (val === null || val === undefined) return '--'
      const n = Number(val)
      if (!isNaN(n)) {
        if (cls === 'success') return n.toFixed(1) + '%'
        if (n >= 100000000) return (n / 100000000).toFixed(1) + '亿'
        if (n >= 10000)     return (n / 10000).toFixed(1) + '万'
        return String(n)
      }
      return String(val)
    },

    getModuleStats(key) {
      return this.moduleStatsData[key] || [
        { label: '监管对象', value: '--', cls: '' },
        { label: '预警数量', value: '--', cls: 'danger' },
        { label: '合规率',   value: '--', cls: 'success' }
      ]
    },

    getModuleAlertCount(key) {
      const stats = this.moduleStatsData[key]
      if (!stats) return 0
      const s = stats.find(x => x.cls === 'danger')
      const v = s ? parseInt(s.value) : 0
      return isNaN(v) ? 0 : v
    },

    goToModule(route) { if (route) this.$router.push(route) },
    refreshAllModules()  { this.loadAllModuleStats() },
    refreshMonitoring()  { this.loadRealtimeAlerts(); this.$message.success('监控数据已刷新') },
    openMonitoringScreen() { this.showMonitoringScreen = true },
    openDecisionCenter()   { this.showDecisionCenter = true },
    handleAlert(alert)     { this.$message.success(`正在处理预警：${alert.title}`) },

    // ===================== 领域详情弹窗 =====================
    openDomainDetail(domain) {
      // 投资穿透：弹出专属项目+风险列表弹窗
      if (domain.key === 'invest') {
        this.showInvestDialog = true
        return
      }
      this.currentDomain = domain
      this.showDomainDetail = true
    },
    handleDomainDialogClose() {
      this.currentDomain = null
    }
  },
  computed: {
    currentDomainStats() {
      if (!this.currentDomain) return []
      return this.getModuleStats(this.currentDomain.key)
    },
    currentDomainAlertCount() {
      if (!this.currentDomain) return 0
      return this.getModuleAlertCount(this.currentDomain.key)
    }
  }
}
</script>

<style lang="scss" scoped>
// ============================================================
// 参考 enterpriseProfile 风格
// BG:      linear-gradient(135deg, #e3f2fd → #bbdefb → #90caf9)
// Card:    rgba(255,255,255,0.95)  shadow: 0 4px 20px rgba(0,0,0,0.1)
// Title:   #1e3c72  Sub: #2a5298
// ============================================================
.integrated-state-dashboard {
  padding: 0;
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 50%, #90caf9 100%);
  min-height: 100vh;
  font-family: 'PingFang SC', 'Microsoft YaHei', sans-serif;

  // ============ 通用 card-header ============
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 15px;
    font-weight: bold;
    color: #1e3c72;

    > span {
      display: flex;
      align-items: center;
      gap: 6px;
    }

    .header-right, .header-actions {
      display: flex;
      align-items: center;
      gap: 8px;
    }
  }

  // ============================================================
  // SECTION 1: 监管驾驶舱顶部
  // ============================================================
  .sec-enterprise {
    background: linear-gradient(160deg, #1a237e 0%, #1565c0 55%, #0277bd 100%);
    padding: 0 0 15px;

    .enterprise-info-panel-wrap {
      width: 100%;

      ::v-deep .enterprise-info-panel {
        border-radius: 0;
        background: transparent;
        border-bottom: 1px solid rgba(255,255,255,0.12);

        .panel-header {
          background: transparent;
          padding: 14px 20px;

          .main-title {
            color: #fff;
            font-size: 20px;
            font-weight: 800;
            letter-spacing: 2px;
            text-shadow: 0 2px 6px rgba(0,0,0,0.3);
            display: flex;
            align-items: center;
            gap: 8px;

            i { color: #ffd54f; }
          }

          .enterprise-selector .el-select .el-input__inner {
            background: rgba(255,255,255,0.15);
            border-color: rgba(255,255,255,0.3);
            color: #fff;
            &::placeholder { color: rgba(255,255,255,0.6); }
          }

          .update-time { color: rgba(255,255,255,0.65); }
        }
      }

      ::v-deep .empty-state,
      ::v-deep .loading-state,
      ::v-deep .panel-content {
        display: none !important;
      }
    }

    // 三列布局
    .enterprise-three-cols {
      display: grid;
      grid-template-columns: 1fr 1.5fr 1fr;
      gap: 12px;
      padding: 12px 15px 0;
      min-height: 300px;

      .col-basic,
      .col-radar,
      .col-hologram {
        background: rgba(255, 255, 255, 0.95);
        border-radius: 10px;
        overflow: hidden;
        min-height: 280px;
        box-shadow: 0 4px 20px rgba(0,0,0,0.15);
      }
    }
  }

  // ============================================================
  // SECTION 2: 监管领域纵览
  // ============================================================
  .sec-domain-overview {
    padding: 15px 15px 0;

    // 统计卡
    .stats-row {
      margin-bottom: 15px;

      .stat-card {
        background: rgba(255, 255, 255, 0.95);
        border: none;
        border-radius: 10px;
        box-shadow: 0 4px 20px rgba(0,0,0,0.08);
        transition: box-shadow 0.2s, transform 0.2s;

        &:hover {
          box-shadow: 0 6px 24px rgba(21,101,192,0.18);
          transform: translateY(-2px);
        }

        ::v-deep .el-card__body { padding: 16px 18px; }

        .stat-content {
          display: flex;
          align-items: center;

          .stat-icon {
            width: 52px;
            height: 52px;
            border-radius: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 14px;
            flex-shrink: 0;
            i { font-size: 22px; color: white; }
          }

          .stat-info {
            .stat-value { font-size: 22px; font-weight: 800; color: #1a237e; margin-bottom: 3px; }
            .stat-label { font-size: 12px; color: #7986cb; margin-bottom: 3px; }
            .stat-change {
              font-size: 11px;
              &.positive { color: #2e7d32; }
              &.negative { color: #c62828; }
            }
          }
        }
      }
    }

    // 12领域卡
    .domains-card {
      margin-bottom: 15px;
      background: rgba(255, 255, 255, 0.95);
      border: none;
      border-radius: 10px;
      box-shadow: 0 4px 20px rgba(0,0,0,0.08);

      ::v-deep .el-card__header {
        background: linear-gradient(90deg, #1e3c72 0%, #2a5298 100%);
        padding: 11px 16px;
        border-bottom: none;
        border-radius: 10px 10px 0 0;

        .card-header {
          color: #fff;
          > span { color: #fff; }
        }
      }

      .domain-col { margin-bottom: 12px; }

      .domain-card {
        background: #fff;
        border-radius: 8px;
        border: 1px solid rgba(21,101,192,0.1);
        overflow: hidden;
        cursor: pointer;
        transition: all 0.22s ease;

        &:hover {
          box-shadow: 0 4px 18px rgba(21,101,192,0.18);
          transform: translateY(-2px);
          border-color: rgba(21,101,192,0.3);
        }

        .dc-header {
          padding: 10px 12px 8px;
          display: flex;
          align-items: flex-start;
          gap: 9px;
          background: #f5f7ff;

          .dc-icon-wrap {
            width: 38px; height: 38px;
            border-radius: 8px;
            display: flex; align-items: center; justify-content: center;
            flex-shrink: 0;
          }

          .dc-title-area {
            flex: 1; min-width: 0;
            .dc-name { font-size: 13px; font-weight: 700; color: #1a237e; margin-bottom: 2px; }
            .dc-desc { font-size: 11px; color: #7986cb; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
          }
        }

        .dc-stats {
          display: flex;
          padding: 8px 10px;
          border-top: 1px solid #eef2ff;
          min-height: 54px;
          align-items: center;

          .ds-item {
            flex: 1; text-align: center;
            .ds-value {
              font-size: 15px; font-weight: 700; color: #1a237e; line-height: 1.3;
              &.danger  { color: #c62828; }
              &.warning { color: #e65100; }
              &.success { color: #2e7d32; }
            }
            .ds-label { font-size: 10px; color: #9fa8da; margin-top: 2px; }
          }
        }

        .dc-action {
          padding: 5px 10px;
          border-top: 1px solid #eef2ff;
          background: #f8f9ff;
          display: flex; justify-content: space-between; align-items: center;
        }
      }
    }
  }

  // ============================================================
  // SECTION 3: 风险分析区
  // ============================================================
  .sec-risk-analysis {
    padding: 0 15px 15px;

    .risk-three-cols {
      display: grid;
      grid-template-columns: 1fr 1.4fr 1fr;
      gap: 12px;
      margin-bottom: 12px;
      min-height: 380px;

      .risk-col {
        background: rgba(255, 255, 255, 0.95);
        border-radius: 10px;
        overflow: hidden;
        box-shadow: 0 4px 20px rgba(0,0,0,0.08);

        // 统一子组件内部标题
        ::v-deep .section-title,
        ::v-deep .card-title,
        ::v-deep .panel-title {
          background: linear-gradient(90deg, #1e3c72, #2a5298) !important;
          color: #fff !important;
          padding: 11px 14px !important;
          font-size: 14px !important;
          font-weight: 700 !important;
          margin-bottom: 0 !important;

          i { color: #ffd54f !important; }
        }
      }
    }

    // 实时监控看版区
    .indicator-warning-row {
      background: rgba(255, 255, 255, 0.95);
      border-radius: 10px;
      overflow: hidden;
      box-shadow: 0 4px 20px rgba(0,0,0,0.08);
      min-height: 260px;

      ::v-deep .indicator-warning-panel {
        background: transparent;
        box-shadow: none;
        border-radius: 0;
        padding: 0;
        height: 100%;

        .section-title {
          background: linear-gradient(90deg, #1e3c72 0%, #2a5298 100%);
          color: #fff;
          margin-bottom: 0;
          padding: 11px 14px;
          border-radius: 10px 10px 0 0;

          .title-left {
            color: #fff;
            font-size: 14px;
            font-weight: 700;
            i { color: #ffd54f; font-size: 16px; }
          }
        }

        .panel-content {
          padding: 12px 15px;
        }
      }
    }
  }

  // ============================================================
  // SECTION 4: 决策支持中心
  // ============================================================
  .sec-decision {
    padding: 0 15px 20px;

    .decision-card {
      background: rgba(255, 255, 255, 0.95);
      border: none;
      border-radius: 10px;
      box-shadow: 0 4px 20px rgba(0,0,0,0.08);

      ::v-deep .el-card__header {
        background: linear-gradient(90deg, #1e3c72 0%, #2a5298 100%);
        padding: 11px 16px;
        border-bottom: none;
        border-radius: 10px 10px 0 0;

        .card-header {
          color: #fff;
          > span { color: #fff; }
          .el-button--text { color: #ffd54f; }
        }
      }

    }
  }
}

// ============ 弹窗样式 ============
::v-deep .monitoring-dialog {
  .el-dialog {
    margin-top: 2vh !important;
    height: 96vh;
    border-radius: 10px;
    overflow: hidden;

    .el-dialog__header {
      padding: 12px 20px;
      background: linear-gradient(90deg, #1e3c72 0%, #2a5298 100%);
      border-bottom: 2px solid #ffd54f;
      .el-dialog__title { color: white; font-weight: 700; letter-spacing: 1px; }
      .el-dialog__headerbtn .el-dialog__close { color: rgba(255,255,255,0.8); }
    }
    .el-dialog__body { padding: 0; height: calc(96vh - 60px); overflow: hidden; }
  }
}

::v-deep .decision-dialog {
  .el-dialog {
    margin-top: 3vh !important;
    height: 94vh;
    border-radius: 10px;
    overflow: hidden;

    .el-dialog__header {
      padding: 12px 20px;
      background: linear-gradient(90deg, #1e3c72 0%, #2a5298 100%);
      border-bottom: 2px solid #ffd54f;
      .el-dialog__title { color: white; font-weight: 700; letter-spacing: 1px; }
      .el-dialog__headerbtn .el-dialog__close { color: rgba(255,255,255,0.8); }
    }
    .el-dialog__body { padding: 0; height: calc(94vh - 60px); overflow: hidden; }
  }
}

::v-deep .domain-detail-dialog {
  .el-dialog {
    border-radius: 10px;
    overflow: hidden;

    .el-dialog__header {
      padding: 14px 20px;
      background: linear-gradient(90deg, #1e3c72 0%, #2a5298 100%);
      border-bottom: 2px solid #ffd54f;
      .el-dialog__title { color: white; font-weight: 700; letter-spacing: 1px; }
      .el-dialog__headerbtn .el-dialog__close { color: rgba(255,255,255,0.8); }
    }
    .el-dialog__body { padding: 20px 24px; }
    .el-dialog__footer { padding: 12px 24px 18px; border-top: 1px solid #eef2ff; }
  }
}
</style>


