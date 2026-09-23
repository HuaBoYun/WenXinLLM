<template>
  <div class="enterprise-profile-dashboard">
    <!-- 顶部企业信息面板 -->
    <div class="top-panel">
      <enterprise-info-panel
        :enterprise-data="enterpriseData"
        :loading="enterpriseLoading"
        @enterprise-change="handleEnterpriseChange"
        @enterprise-ready="handleEnterpriseReady"
        @refresh="handleRefresh"
      /> 
    </div>

    <!-- 第二行：企业基本信息、财务雷达图、企业全息画像 -->
    <div class="info-cards-row first-row">
      <div class="info-card large-card">
        <enterprise-basic-info-card
          :enterprise-data="enterpriseData?.enterpriseInfo"
          :enterprise-detail-data="enterpriseDetailData"
          :loading="enterpriseLoading || enterpriseDetailLoading"
        />
      </div>
      <div class="info-card">
        <financial-radar-chart-card
          :radar-data="radarData"
          :loading="radarLoading"
        />
      </div>
      <div class="info-card">
        <enterprise-hologram-card
          :hologram-data="hologramData"
          :enterprise-detail-data="enterpriseDetailData"
          :loading="hologramLoading"
        />
      </div>
    </div>

    <!-- 第三行：组织架构、人员分析 -->
    <div class="info-cards-row second-row">
      <div class="info-card">
        <organization-structure-card
          :organization-data="organizationData"
          :loading="organizationLoading"
        />
      </div>
      <div class="info-card">
        <personnel-analysis-card
          :personnel-data="personnelData"
          :loading="personnelLoading"
        />
      </div>
    </div>

    <!-- 第四行：主要内容区域 -->
    <div class="main-content">
      <!-- 左侧风险预警面板 -->
      <div class="left-panel">
        <risk-warning-panel
          :enterprise-id="currentEnterpriseId"
          :risk-data="riskData"
          :loading="riskLoading"
        />
      </div>

      <!-- 中央关键财务指标面板 -->
      <div class="center-panel">
        <key-financial-indicators-card
          :indicators-data="radarData?.keyIndicators"
          :loading="radarLoading"
          @refresh="loadFinancialRadarData"
        />
      </div>

      <!-- 右侧审计法律面板 -->
      <div class="right-panel">
        <audit-legal-panel
          :enterprise-id="currentEnterpriseId"
          :audit-data="auditData"
          :legal-data="legalData"
          :loading="auditLoading"
        />
      </div>
    </div>

    <!-- 第五行：指标预警独占一行 -->
    <div class="indicator-warning-row">
      <indicator-warning-panel
        :enterprise-id="currentEnterpriseId"
      />
    </div>

    <!-- 全屏模式下的退出按钮 -->
    <div v-if="isFullscreen" class="fullscreen-exit-btn">
      <el-button
        type="danger"
        icon="el-icon-close"
        circle
        size="medium"
        @click="exitFullscreen"
        title="退出全屏 (ESC)"
      />
    </div>
  </div>
</template>

<script>
import EnterpriseInfoPanel from './components/EnterpriseInfoPanel.vue'
import FinancialRadarPanel from './components/FinancialRadarPanel.vue'
import IndicatorWarningPanel from './components/IndicatorWarningPanel.vue'
import RiskWarningPanel from './components/RiskWarningPanel.vue'
import AuditLegalPanel from './components/AuditLegalPanel.vue'
import EnterpriseBasicInfoCard from './components/EnterpriseBasicInfoCard.vue'
import EnterpriseHologramCard from './components/EnterpriseHologramCard.vue'
import EnterpriseInfoCard from './components/EnterpriseInfoCard.vue'
import OrganizationStructureCard from './components/OrganizationStructureCard.vue'
import PersonnelAnalysisCard from './components/PersonnelAnalysisCard.vue'
import FinancialRadarChartCard from './components/FinancialRadarChartCard.vue'
import KeyFinancialIndicatorsCard from './components/KeyFinancialIndicatorsCard.vue'
import dashboardMixin from './mixins/dashboardMixin'
import * as enterpriseProfileApi from '@/api/risk/enterpriseProfile'

export default {
  name: 'EnterpriseProfileDashboard',
  components: {
    EnterpriseInfoPanel,
    FinancialRadarPanel,
    IndicatorWarningPanel,
    RiskWarningPanel,
    AuditLegalPanel,
    EnterpriseBasicInfoCard,
    EnterpriseHologramCard,
    EnterpriseInfoCard,
    OrganizationStructureCard,
    PersonnelAnalysisCard,
    FinancialRadarChartCard,
    KeyFinancialIndicatorsCard
  },
  mixins: [dashboardMixin],
  data() {
    return {
      // 当前选择的企业ID
      currentEnterpriseId: 'ENT067',

      // API接口
      $api: {
        enterpriseProfile: enterpriseProfileApi
      },

      // 各面板数据
      enterpriseData: null,
      enterpriseDetailData: null,
      radarData: null,
      riskData: null,
      organizationData: null,
      hologramData: null,
      personnelData: null,
      auditData: null,
      legalData: null,
      
      // 加载状态（保留各组件独立的加载状态）
      enterpriseLoading: false,
      enterpriseDetailLoading: false,
      radarLoading: false,
      riskLoading: false,
      organizationLoading: false,
      hologramLoading: false,
      personnelLoading: false,
      auditLoading: false,

      // 全屏状态
      isFullscreen: false,

      // 自动刷新定时器
      refreshTimer: null,
      refreshInterval: 300000, // 5分钟

      // 初始化延迟定时器
      _initTimer: null,
      // 是否已经通过 enterprise-ready 事件初始化过
      _dashboardInitialized: false,
      // 是否为首次加载
      _isFirstLoad: true
    }
  },
  mounted() {
    // 延迟初始化作为兜底，如果 enterprise-ready 事件先触发则取消此定时器
    this._initTimer = setTimeout(() => {
      if (!this._dashboardInitialized) {
        console.log('兜底初始化企业画像数据，当前企业ID:', this.currentEnterpriseId)
        this._dashboardInitialized = true
        this.initDashboard()
      }
    }, 3000)
    this.startAutoRefresh()

    // 监听全屏状态变化
    this.addFullscreenListeners()
  },
  beforeDestroy() {
    // 清理初始化定时器
    if (this._initTimer) {
      clearTimeout(this._initTimer)
      this._initTimer = null
    }
    this.stopAutoRefresh()
    this.removeFullscreenListeners()
  },
  methods: {
    /**
     * 初始化大屏（首次加载）
     */
    async initDashboard() {
      console.log('=== 开始初始化企业画像大屏 ===')
      console.log('当前企业ID:', this.currentEnterpriseId)

      try {
        // 并行加载所有数据，但不因单个失败而中断整体流程
        console.log('开始并行加载9个接口数据...')
        const results = await Promise.allSettled([
          this.loadEnterpriseInfo(),
          this.loadEnterpriseDetailInfo(),
          this.loadFinancialRadarData(),
          this.loadRiskAuditData(),
          this.loadOrganizationData(),
          this.loadHologramData(),
          this.loadPersonnelData(),
          this.loadAuditData(),
          this.loadLegalData()
        ])

        // 统计成功和失败的数量
        const successCount = results.filter(result => result.status === 'fulfilled').length
        const failureCount = results.filter(result => result.status === 'rejected').length

        // 首次加载时显示提示
        if (this._isFirstLoad) {
          if (successCount > 0) {
            this.$message.success(`企业画像数据加载完成 (${successCount}/${results.length})`)
          }
          this._isFirstLoad = false
        }

        if (failureCount > 0) {
          console.warn(`${failureCount} 个数据加载失败:`, results.filter(r => r.status === 'rejected'))
        }
      } catch (error) {
        console.error('初始化大屏失败:', error)
        if (this._isFirstLoad) {
          this.$message.error('数据加载失败，请刷新重试')
        }
      }
    },

    /**
     * 静默刷新数据（定时刷新使用，不影响用户查看）
     */
    async silentRefreshData() {
      console.log('=== 开始静默刷新企业画像数据 ===')
      console.log('当前企业ID:', this.currentEnterpriseId)

      try {
        // 并行加载所有数据，静默更新，不显示任何提示
        await Promise.allSettled([
          this.loadEnterpriseInfo(),
          this.loadEnterpriseDetailInfo(),
          this.loadFinancialRadarData(),
          this.loadRiskAuditData(),
          this.loadOrganizationData(),
          this.loadHologramData(),
          this.loadPersonnelData(),
          this.loadAuditData(),
          this.loadLegalData()
        ])
        
        console.log('静默刷新完成')
      } catch (error) {
        console.error('静默刷新失败:', error)
        // 静默刷新失败不提示用户，只记录日志
      }
    },

    /**
     * 加载企业基本信息
     */
    async loadEnterpriseInfo() {
      console.log('=== 开始加载企业信息 ===')
      console.log('企业ID:', this.currentEnterpriseId)
      console.log('enterpriseProfileApi:', enterpriseProfileApi)

      this.enterpriseLoading = true
      try {
        const response = await enterpriseProfileApi.getEnterpriseInfo({
          enterpriseId: this.currentEnterpriseId,
          includeExtendedInfo: true
        })

        console.log('企业信息响应:', response)

        if (response.code === 1) {
          this.enterpriseData = response.data
          console.log('企业信息设置成功:', this.enterpriseData)
          console.log('企业基本信息部分:', this.enterpriseData?.enterpriseInfo)
          console.log('企业名称:', this.enterpriseData?.enterpriseInfo?.enterpriseName)
        } else {
          throw new Error(response.msg)
        }
      } catch (error) {
        console.error('加载企业信息失败:', error)
        if (this._isFirstLoad) {
          this.$message.error('企业信息加载失败: ' + error.message)
        }
      } finally {
        this.enterpriseLoading = false
      }
    },

    /**
     * 加载企业详细信息
     */
    async loadEnterpriseDetailInfo() {
      console.log('=== 开始加载企业详细信息 ===')
      this.enterpriseDetailLoading = true
      try {
        const response = await enterpriseProfileApi.getEnterpriseDetailInfo({
          enterpriseId: this.currentEnterpriseId
        })

        console.log('企业详细信息响应:', response)

        if (response.code === 1) {
          this.enterpriseDetailData = response.data
          console.log('企业详细信息设置成功:', this.enterpriseDetailData)
        } else {
          throw new Error(response.msg)
        }
      } catch (error) {
        console.error('加载企业详细信息失败:', error)
        if (this._isFirstLoad) {
          this.$message.error('企业详细信息加载失败: ' + error.message)
        }
      } finally {
        this.enterpriseDetailLoading = false
      }
    },

    /**
     * 加载财务雷达图数据
     */
    async loadFinancialRadarData() {
      console.log('=== 开始加载财务雷达图数据 ===')
      console.log('企业ID:', this.currentEnterpriseId)

      this.radarLoading = true
      try {
        const requestData = {
          enterpriseId: this.currentEnterpriseId,
          periodDate: null,
          includeKeyIndicators: true
        }
        console.log('请求参数:', requestData)

        const response = await enterpriseProfileApi.getFinancialRadarData(requestData)
        console.log('财务雷达图响应:', response)
        console.log('响应类型:', typeof response)
        console.log('响应是否为对象:', response && typeof response === 'object')

        if (response && response.code === 1) {
          this.radarData = response.data
          console.log('财务雷达图数据设置成功:', this.radarData)
        } else {
          const errorMsg = response ? response.msg : '未知错误'
          console.error('财务雷达图业务错误:', errorMsg)
          throw new Error(errorMsg)
        }
      } catch (error) {
        console.error('加载财务雷达图数据失败:', error)
        console.error('错误详情:', {
          message: error.message,
          stack: error.stack,
          name: error.name,
          toString: error.toString()
        })
        if (this._isFirstLoad) {
          this.$message.error('财务雷达图数据加载失败: ' + (error.message || '未知错误'))
        }
      } finally {
        this.radarLoading = false
      }
    },



    /**
     * 加载风险审计数据
     */
    async loadRiskAuditData() {
      this.riskLoading = true
      try {
        const response = await enterpriseProfileApi.getRiskAuditPanelData({
          enterpriseId: this.currentEnterpriseId
        })
        
        if (response.code === 1) {
          this.riskData = response.data
        } else {
          // 预留接口，暂时使用模拟数据
          this.riskData = { message: '风险预警和审计面板功能开发中' }
        }
      } catch (error) {
        console.error('加载风险审计数据失败:', error)
        this.riskData = { message: '风险预警和审计面板功能开发中' }
      } finally {
        this.riskLoading = false
      }
    },

    /**
     * 加载组织架构数据
     */
    async loadOrganizationData() {
      console.log('=== 开始加载组织架构数据 ===')
      this.organizationLoading = true
      try {
        // 使用风险模块的组织树API
        const { getFlawTreeData } = await import('@/api/risk/question')
        const response = await getFlawTreeData()

        console.log('组织架构API响应:', response)

        if (response && response.length > 0) {
          // 处理API返回的数据
          const rootOrg = response[0]
          const processedData = {
            rootDepartment: {
              departmentId: rootOrg.id,
              departmentName: rootOrg.name,
              employeeCount: this.calculateTotalEmployees(rootOrg)
            },
            subDepartments: this.processSubDepartments(rootOrg.children || []),
            totalDepartments: this.countTotalDepartments(response),
            totalEmployees: this.calculateTotalEmployees(rootOrg),
            managementLevels: this.calculateManagementLevels(rootOrg)
          }
          this.organizationData = processedData
        } else {
          // 如果API没有返回数据，使用模拟数据
          this.organizationData = this.getDefaultOrganizationData()
        }
      } catch (error) {
        console.error('加载组织架构数据失败:', error)
        // 出错时使用模拟数据
        this.organizationData = this.getDefaultOrganizationData()
      } finally {
        this.organizationLoading = false
      }
    },

    /**
     * 处理企业准备好事件
     */
    handleEnterpriseReady(enterpriseId) {
      console.log('企业列表准备完成，当前企业:', enterpriseId)
      this.currentEnterpriseId = enterpriseId
      // 取消兜底定时器，避免重复初始化
      if (this._initTimer) {
        clearTimeout(this._initTimer)
        this._initTimer = null
      }
      if (!this._dashboardInitialized) {
        this._dashboardInitialized = true
        this.initDashboard()
      }
    },

    /**
     * 处理企业切换
     */
    handleEnterpriseChange(enterpriseId) {
      if (enterpriseId !== this.currentEnterpriseId) {
        this.currentEnterpriseId = enterpriseId
        // 切换企业时重置首次加载标志
        this._isFirstLoad = true
        this.initDashboard()
      }
    },

    /**
     * 处理手动刷新（用户点击刷新按钮）
     */
    async handleRefresh() {
      try {
        const response = await enterpriseProfileApi.refreshEnterpriseData({
          enterpriseId: this.currentEnterpriseId
        })
        
        if (response.code === 1) {
          this.$message.success('数据刷新成功')
          // 重新加载数据
          this._isFirstLoad = true
          this.initDashboard()
        } else {
          this.$message.error(response.msg)
        }
      } catch (error) {
        console.error('数据刷新失败:', error)
        this.$message.error('数据刷新失败')
      }
    },

    /**
     * 开始自动刷新（使用静默刷新）
     */
    startAutoRefresh() {
      this.refreshTimer = setInterval(() => {
        // 定时刷新使用静默方式，不影响用户查看
        this.silentRefreshData()
      }, this.refreshInterval)
    },

    /**
     * 停止自动刷新
     */
    stopAutoRefresh() {
      if (this.refreshTimer) {
        clearInterval(this.refreshTimer)
        this.refreshTimer = null
      }
    },

    /**
     * 添加全屏状态监听器
     */
    addFullscreenListeners() {
      document.addEventListener('fullscreenchange', this.handleFullscreenChange)
      document.addEventListener('webkitfullscreenchange', this.handleFullscreenChange)
      document.addEventListener('mozfullscreenchange', this.handleFullscreenChange)
      document.addEventListener('MSFullscreenChange', this.handleFullscreenChange)
    },

    /**
     * 移除全屏状态监听器
     */
    removeFullscreenListeners() {
      document.removeEventListener('fullscreenchange', this.handleFullscreenChange)
      document.removeEventListener('webkitfullscreenchange', this.handleFullscreenChange)
      document.removeEventListener('mozfullscreenchange', this.handleFullscreenChange)
      document.removeEventListener('MSFullscreenChange', this.handleFullscreenChange)
    },

    /**
     * 处理全屏状态变化
     */
    handleFullscreenChange() {
      this.isFullscreen = !!(
        document.fullscreenElement ||
        document.webkitFullscreenElement ||
        document.mozFullScreenElement ||
        document.msFullscreenElement
      )
    },

    /**
     * 退出全屏
     */
    exitFullscreen() {
      try {
        if (document.exitFullscreen) {
          document.exitFullscreen()
        } else if (document.mozCancelFullScreen) {
          document.mozCancelFullScreen()
        } else if (document.webkitExitFullscreen) {
          document.webkitExitFullscreen()
        } else if (document.msExitFullscreen) {
          document.msExitFullscreen()
        }
      } catch (error) {
        console.error('退出全屏失败:', error)
        this.$message.error('退出全屏失败')
      }
    },

    /**
     * 加载企业全息画像数据
     */
    async loadHologramData() {
      console.log('=== 开始加载企业全息画像数据 ===')
      this.hologramLoading = true
      try {
        const response = await enterpriseProfileApi.getEnterpriseHologramData({
          enterpriseId: this.currentEnterpriseId
        })

        console.log('企业全息画像响应:', response)

        if (response.code === 1) {
          this.hologramData = response.data
          console.log('企业全息画像数据设置成功:', this.hologramData)
        } else {
          throw new Error(response.msg)
        }
      } catch (error) {
        console.error('加载企业全息画像数据失败:', error)
        if (this._isFirstLoad) {
          this.$message.error('企业全息画像数据加载失败: ' + error.message)
        }
        // 出错时使用模拟数据
        this.hologramData = {
          enterpriseTags: [
            { tagId: '1', tagName: '高新技术企业', tagType: '1' },
            { tagId: '2', tagName: '上市公司', tagType: '1' },
            { tagId: '3', tagName: '财务稳健', tagType: '1' },
            { tagId: '4', tagName: '行业领先', tagType: '1' }
          ],
          keyIndicators: [
            { code: 'revenue', name: '营业收入', value: '15.6亿' },
            { code: 'profit', name: '净利润', value: '2.3亿' },
            { code: 'assets', name: '总资产', value: '45.2亿' },
            { code: 'employees', name: '员工数', value: '1250人' }
          ],
          riskLevel: '1'
        }
      } finally {
        this.hologramLoading = false
      }
    },

    /**
     * 加载人员分析数据
     */
    async loadPersonnelData() {
      console.log('=== 开始加载人员分析数据 ===')
      this.personnelLoading = true
      try {
        // 模拟数据，实际应该调用API
        const mockData = {
          totalEmployees: 1250,
          averageAge: 32,
          turnoverRate: 8.5,
          educationDistribution: [
            { education: '1', educationName: '博士', count: 45, percentage: 3.6 },
            { education: '2', educationName: '硕士', count: 320, percentage: 25.6 },
            { education: '3', educationName: '本科', count: 680, percentage: 54.4 },
            { education: '4', educationName: '专科', count: 180, percentage: 14.4 },
            { education: '5', educationName: '其他', count: 25, percentage: 2.0 }
          ],
          ageDistribution: [
            { ageRange: '25岁以下', count: 125, percentage: 10.0 },
            { ageRange: '25-35岁', count: 625, percentage: 50.0 },
            { ageRange: '35-45岁', count: 375, percentage: 30.0 },
            { ageRange: '45岁以上', count: 125, percentage: 10.0 }
          ]
        }
        this.personnelData = mockData
      } catch (error) {
        console.error('加载人员分析数据失败:', error)
      } finally {
        this.personnelLoading = false
      }
    },

    /**
     * 加载审计数据
     */
    async loadAuditData() {
      console.log('=== 开始加载审计数据 ===')
      this.auditLoading = true
      try {
        // 模拟数据，实际应该调用API
        const mockData = {
          auditRecords: [
            {
              auditId: '1',
              auditType: '1',
              auditTypeName: '内部审计',
              auditDate: '2024-12-15',
              auditResult: '1',
              auditResultName: '通过',
              issueCount: 0
            },
            {
              auditId: '2',
              auditType: '2',
              auditTypeName: '外部审计',
              auditDate: '2024-11-20',
              auditResult: '2',
              auditResultName: '有问题',
              issueCount: 3
            }
          ],
          auditStats: {
            totalAudits: 12,
            totalIssues: 8,
            resolvedIssues: 6
          }
        }
        this.auditData = mockData
      } catch (error) {
        console.error('加载审计数据失败:', error)
      } finally {
        this.auditLoading = false
      }
    },

    /**
     * 加载法律案件数据
     */
    async loadLegalData() {
      console.log('=== 开始加载法律案件数据 ===')
      try {
        // 模拟数据，实际应该调用API
        const mockData = {
          legalCases: [
            {
              caseId: '1',
              caseType: '1',
              caseTypeName: '民事案件',
              caseStatus: '2',
              caseStatusName: '已结案',
              caseTitle: '合同纠纷案',
              caseDate: '2024-10-15',
              involvedAmount: 500000
            },
            {
              caseId: '2',
              caseType: '3',
              caseTypeName: '行政案件',
              caseStatus: '1',
              caseStatusName: '进行中',
              caseTitle: '税务争议案',
              caseDate: '2024-12-01',
              involvedAmount: 200000
            }
          ],
          legalStats: {
            totalCases: 5,
            activeCases: 2,
            totalAmount: 1200000
          }
        }
        this.legalData = mockData
      } catch (error) {
        console.error('加载法律案件数据失败:', error)
      }
    },

    /**
     * 获取默认组织架构数据
     */
    getDefaultOrganizationData() {
      return {
        rootDepartment: {
          departmentId: '1',
          departmentName: '示例云科技有限公司',
          employeeCount: 1250
        },
        subDepartments: [
          { departmentId: '2', departmentName: '技术部', employeeCount: 450 },
          { departmentId: '3', departmentName: '产品部', employeeCount: 180 },
          { departmentId: '4', departmentName: '市场部', employeeCount: 120 },
          { departmentId: '5', departmentName: '销售部', employeeCount: 200 },
          { departmentId: '6', departmentName: '人事部', employeeCount: 80 },
          { departmentId: '7', departmentName: '财务部', employeeCount: 60 },
          { departmentId: '8', departmentName: '行政部', employeeCount: 90 },
          { departmentId: '9', departmentName: '法务部', employeeCount: 70 }
        ],
        totalDepartments: 9,
        totalEmployees: 1250,
        managementLevels: 4
      }
    },

    /**
     * 处理子部门数据
     */
    processSubDepartments(children) {
      if (!children || children.length === 0) return []

      return children.map(dept => ({
        departmentId: dept.id,
        departmentName: dept.name,
        employeeCount: Math.floor(Math.random() * 200) + 50 // 模拟员工数量
      }))
    },

    /**
     * 计算总员工数
     */
    calculateTotalEmployees(orgData) {
      if (!orgData) return 0

      let total = Math.floor(Math.random() * 100) + 50 // 根部门基础人数
      if (orgData.children && orgData.children.length > 0) {
        orgData.children.forEach(child => {
          total += Math.floor(Math.random() * 200) + 50
        })
      }
      return total
    },

    /**
     * 计算总部门数
     */
    countTotalDepartments(orgTree) {
      if (!orgTree || orgTree.length === 0) return 0

      let count = 0
      const countNodes = (nodes) => {
        nodes.forEach(node => {
          count++
          if (node.children && node.children.length > 0) {
            countNodes(node.children)
          }
        })
      }

      countNodes(orgTree)
      return count
    },

    /**
     * 计算管理层级
     */
    calculateManagementLevels(orgData) {
      if (!orgData) return 1

      const getMaxDepth = (node, currentDepth = 1) => {
        if (!node.children || node.children.length === 0) {
          return currentDepth
        }

        let maxDepth = currentDepth
        node.children.forEach(child => {
          const depth = getMaxDepth(child, currentDepth + 1)
          maxDepth = Math.max(maxDepth, depth)
        })

        return maxDepth
      }

      return getMaxDepth(orgData)
    },


  }
}
</script>

<style lang="scss" scoped>
.enterprise-profile-dashboard {
  width: 100%;
  min-height: 100vh;
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 50%, #90caf9 100%);
  display: flex;
  flex-direction: column;
  overflow-y: auto;
  position: relative;

  .top-panel {
    height: 120px;
    padding: 10px 10px;
    flex-shrink: 0;
  }

  // 企业信息卡片区域
  .info-cards-row {
    gap: 10px;
    padding: 0 10px 15px 10px;
    min-height: 200px;

    .info-card {
      background: rgba(255, 255, 255, 0.95);
      border-radius: 8px;
      overflow: hidden;

      &.large-card {
        // 企业基本信息卡片占据更多空间，因为包含了更多内容
      }
    }

    // 第二行：企业基本信息、财务雷达图、企业全息画像
    &.first-row {
      display: grid;
      grid-template-columns: 1fr 1.5fr 1fr;
      min-height: 250px; // 增加高度以容纳更多内容
    }

    // 第三行：组织架构、人员分析
    &.second-row {
      display: grid;
      grid-template-columns: 1fr 1fr;
      min-height: 200px;
    }
  }

  // 第四行：主要内容区域
  .main-content {
    display: flex;
    padding: 0 10px 15px 10px;
    gap: 15px;
    min-height: 400px;

    .left-panel {
      width: 350px;
      flex-shrink: 0;
    }

    .center-panel {
      flex: 1;
      min-width: 0;
    }

    .right-panel {
      width: 350px;
      flex-shrink: 0;
    }
  }

  // 第五行：指标预警独占一行
  .indicator-warning-row {
    padding: 0 10px 15px 10px;
    min-height: 300px;
  }

  .fullscreen-exit-btn {
    position: fixed;
    top: 20px;
    right: 20px;
    z-index: 10000;

    .el-button {
      background: rgba(255, 255, 255, 0.9);
      border: 1px solid #f56c6c;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.3);

      &:hover {
        background: #f56c6c;
        color: white;
        transform: scale(1.1);
        transition: all 0.3s ease;
      }
    }
  }
}

// 全屏模式优化 - 只对大屏内容区域全屏
.enterprise-profile-dashboard:fullscreen,
.enterprise-profile-dashboard:-webkit-full-screen,
.enterprise-profile-dashboard:-moz-full-screen,
.enterprise-profile-dashboard:-ms-fullscreen {
  // 全屏时确保大屏占满整个屏幕
  width: 100vw;
  height: 100vh;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 9999;

  .top-panel {
    height: 100px;
    padding: 8px 15px;
  }

  .main-content {
    padding: 0 15px;
    gap: 15px;

    .left-panel,
    .right-panel {
      width: 320px;
    }
  }

  .bottom-panel {
    height: 280px;
    padding: 15px;
  }
}

// 响应式设计
@media (max-width: 1920px) {
  .enterprise-profile-dashboard {
    .info-cards-row {
      grid-template-columns: 1fr 1fr 1fr 1fr 1.5fr;
      gap: 12px;
    }

    .main-content {
      .left-panel,
      .right-panel {
        width: 320px;
      }
    }
  }
}

@media (max-width: 1600px) {
  .enterprise-profile-dashboard {
    .info-cards-row {
      grid-template-columns: 1fr 1fr 1fr;
      grid-template-rows: 1fr 1fr;
      min-height: 400px;

      .info-card:nth-child(4) {
        grid-column: 1;
        grid-row: 2;
      }

      .info-card:nth-child(5) {
        grid-column: 2 / 4;
        grid-row: 2;
      }
    }

    .main-content {
      .left-panel,
      .right-panel {
        width: 300px;
      }
    }
  }
}

@media (max-width: 1200px) {
  .enterprise-profile-dashboard {
    .info-cards-row {
      grid-template-columns: 1fr 1fr;
      grid-template-rows: 1fr 1fr 1fr;
      min-height: 600px;

      .info-card:nth-child(5) {
        grid-column: 1 / 3;
        grid-row: 3;
      }
    }

    .main-content {
      flex-direction: column;
      gap: 15px;

      .left-panel,
      .center-panel,
      .right-panel {
        width: 100%;
      }
    }
  }
}
</style>
