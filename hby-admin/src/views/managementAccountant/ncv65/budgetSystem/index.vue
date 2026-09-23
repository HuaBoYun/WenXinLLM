<template>
  <div class="budget-system-index">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>预算体系管理</h1>
      <p>构建完整的预算管理体系，包括组织架构、维度管理、指标体系等核心配置</p>
    </div>

    <!-- 体系概览统计 -->
    <el-row :gutter="20" class="overview-stats">
      <el-col :span="6">
        <el-card class="stat-card organization-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon">
              <i class="el-icon-office-building"></i>
            </div>
            <div class="stat-info">
              <h3>{{ systemStats.organizationCount }}</h3>
              <p>组织单元</p>
              <div class="stat-detail">
                <span>{{ systemStats.activeOrgCount }} 个活跃</span>
              </div>
            </div>
          </div>
          <div class="stat-footer">
            <span class="stat-trend up">
              <i class="el-icon-arrow-up"></i>
              {{ systemStats.orgGrowth }}%
            </span>
            <span class="stat-label">较上月</span>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card dimension-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon">
              <i class="el-icon-s-grid"></i>
            </div>
            <div class="stat-info">
              <h3>{{ systemStats.dimensionCount }}</h3>
              <p>预算维度</p>
              <div class="stat-detail">
                <span>{{ systemStats.activeDimCount }} 个启用</span>
              </div>
            </div>
          </div>
          <div class="stat-footer">
            <span class="stat-trend">
              <i class="el-icon-minus"></i>
              {{ systemStats.dimUsageRate }}%
            </span>
            <span class="stat-label">使用率</span>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card indicator-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon">
              <i class="el-icon-data-line"></i>
            </div>
            <div class="stat-info">
              <h3>{{ systemStats.indicatorCount }}</h3>
              <p>预算指标</p>
              <div class="stat-detail">
                <span>{{ systemStats.kpiCount }} 个KPI</span>
              </div>
            </div>
          </div>
          <div class="stat-footer">
            <span class="stat-trend up">
              <i class="el-icon-arrow-up"></i>
              {{ systemStats.indicatorGrowth }}%
            </span>
            <span class="stat-label">较上季</span>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card class="stat-card model-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon">
              <i class="el-icon-s-operation"></i>
            </div>
            <div class="stat-info">
              <h3>{{ systemStats.modelCount }}</h3>
              <p>预算模型</p>
              <div class="stat-detail">
                <span>{{ systemStats.activeModelCount }} 个运行中</span>
              </div>
            </div>
          </div>
          <div class="stat-footer">
            <span class="stat-trend">
              <i class="el-icon-minus"></i>
              {{ systemStats.modelEfficiency }}%
            </span>
            <span class="stat-label">运行效率</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能模块导航 -->
    <el-card class="module-nav-card" shadow="never">
      <div class="module-nav-header">
        <h3>功能模块</h3>
        <p>选择相应的功能模块进行预算体系配置和管理</p>
      </div>
      
      <el-row :gutter="24" class="module-grid">
        <el-col :span="8" v-for="module in modules" :key="module.key">
          <div class="module-item" @click="navigateToModule(module)">
            <div class="module-icon">
              <i :class="module.icon"></i>
            </div>
            <div class="module-content">
              <h4>{{ module.title }}</h4>
              <p>{{ module.description }}</p>
              <div class="module-stats">
                <span class="stat-item">
                  <i class="el-icon-s-data"></i>
                  {{ module.itemCount }} 项配置
                </span>
                <span class="stat-item">
                  <i class="el-icon-time"></i>
                  {{ module.lastUpdate }}
                </span>
              </div>
            </div>
            <div class="module-action">
              <el-button type="primary" size="small">进入管理</el-button>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 体系配置向导 -->
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card class="wizard-card" shadow="never">
          <div class="card-header">
            <h3>体系配置向导</h3>
            <el-button type="text" @click="viewAllSteps">查看全部步骤</el-button>
          </div>
          
          <div class="wizard-steps">
            <el-steps :active="currentStep" align-center>
              <el-step
                v-for="(step, index) in configSteps"
                :key="index"
                :title="step.title"
                :description="step.description"
                :status="getStepStatus(index)"
              />
            </el-steps>
            
            <div class="step-content">
              <div class="current-step-info">
                <h4>{{ configSteps[currentStep].title }}</h4>
                <p>{{ configSteps[currentStep].detail }}</p>
                
                <div class="step-actions">
                  <el-button
                    v-if="currentStep > 0"
                    @click="handlePrevStep"
                  >上一步</el-button>
                  <el-button
                    type="primary"
                    @click="handleNextStep"
                  >{{ currentStep < configSteps.length - 1 ? '下一步' : '完成配置' }}</el-button>
                  <el-button
                    type="success"
                    @click="handleGoToStep"
                  >立即配置</el-button>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="8">
        <el-card class="system-health-card" shadow="never">
          <div class="card-header">
            <h3>体系健康度</h3>
            <el-button type="text" @click="viewHealthDetail">详细报告</el-button>
          </div>
          
          <div class="health-overview">
            <div class="health-score">
              <div class="score-circle">
                <el-progress
                  type="circle"
                  :percentage="systemHealth.overallScore"
                  :width="120"
                  :stroke-width="8"
                  :color="getHealthColor(systemHealth.overallScore)"
                >
                  <span class="score-text">{{ systemHealth.overallScore }}</span>
                </el-progress>
              </div>
              <p class="score-label">综合健康度</p>
            </div>
            
            <div class="health-details">
              <div class="health-item" v-for="item in systemHealth.details" :key="item.key">
                <div class="health-label">{{ item.label }}</div>
                <div class="health-progress">
                  <el-progress
                    :percentage="item.score"
                    :stroke-width="6"
                    :show-text="false"
                    :color="getHealthColor(item.score)"
                  />
                </div>
                <div class="health-score-text">{{ item.score }}%</div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最近活动和快速操作 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="recent-activities-card" shadow="never">
          <div class="card-header">
            <h3>最近活动</h3>
            <el-button type="text" @click="viewAllActivities">查看全部</el-button>
          </div>
          
          <div class="activities-list">
            <div class="activity-item" v-for="activity in recentActivities" :key="activity.id">
              <div class="activity-avatar">
                <el-avatar :size="32" :src="activity.userAvatar">
                  {{ activity.userName.charAt(0) }}
                </el-avatar>
              </div>
              <div class="activity-content">
                <p class="activity-text">
                  <strong>{{ activity.userName }}</strong>
                  {{ activity.action }}
                  <span class="activity-target">{{ activity.target }}</span>
                </p>
                <p class="activity-time">{{ activity.time }}</p>
              </div>
              <div class="activity-status">
                <el-tag :type="getActivityStatusType(activity.status)" size="mini">
                  {{ activity.status }}
                </el-tag>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card class="quick-actions-card" shadow="never">
          <div class="card-header">
            <h3>快速操作</h3>
            <el-button type="text" @click="viewAllActions">更多操作</el-button>
          </div>
          
          <div class="quick-actions">
            <div class="action-item" v-for="action in quickActions" :key="action.key" @click="handleQuickAction(action)">
              <div class="action-icon">
                <i :class="action.icon"></i>
              </div>
              <div class="action-content">
                <h5>{{ action.title }}</h5>
                <p>{{ action.description }}</p>
              </div>
              <div class="action-arrow">
                <i class="el-icon-arrow-right"></i>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 体系架构图 -->
    <el-card class="architecture-card" shadow="never">
      <div class="card-header">
        <h3>预算体系架构</h3>
        <div class="architecture-controls">
          <el-button-group size="small">
            <el-button :type="viewMode === 'tree' ? 'primary' : ''" @click="viewMode = 'tree'">
              <i class="el-icon-share"></i> 树形图
            </el-button>
            <el-button :type="viewMode === 'network' ? 'primary' : ''" @click="viewMode = 'network'">
              <i class="el-icon-connection"></i> 网络图
            </el-button>
            <el-button :type="viewMode === 'matrix' ? 'primary' : ''" @click="viewMode = 'matrix'">
              <i class="el-icon-s-grid"></i> 矩阵图
            </el-button>
          </el-button-group>
        </div>
      </div>
      
      <div class="architecture-content">
        <div v-if="viewMode === 'tree'" class="tree-view">
          <div class="architecture-tree" ref="architectureTree" style="height: 400px;"></div>
        </div>
        <div v-else-if="viewMode === 'network'" class="network-view">
          <div class="architecture-network" ref="architectureNetwork" style="height: 400px;"></div>
        </div>
        <div v-else class="matrix-view">
          <div class="architecture-matrix">
            <el-table :data="matrixData" border size="small">
              <el-table-column prop="dimension" label="维度" width="120" />
              <el-table-column
                v-for="org in organizationList"
                :key="org.id"
                :prop="org.code"
                :label="org.name"
                width="100"
                align="center"
              >
                <template slot-scope="scope">
                  <el-tag
                    v-if="scope.row[org.code]"
                    :type="scope.row[org.code].type"
                    size="mini"
                  >
                    {{ scope.row[org.code].value }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import request from '@/utils/request'

export default {
  name: 'BudgetSystemIndex',
  data() {
    return {
      // 统计数据
      systemStats: {
        organizationCount: 0,
        activeOrgCount: 0,
        orgGrowth: 0,
        dimensionCount: 0,
        activeDimCount: 0,
        dimUsageRate: 0,
        indicatorCount: 0,
        kpiCount: 0,
        indicatorGrowth: 0,
        modelCount: 0,
        activeModelCount: 0,
        modelEfficiency: 0
      },
      
      // 功能模块
      modules: [
        {
          key: 'organizationStructure',
          title: '组织架构管理',
          description: '管理预算组织架构，设置部门层级和责任中心',
          icon: 'el-icon-office-building',
          itemCount: 0,
          lastUpdate: '-',
          path: '/ncv65/budget-system/organization-structure'
        },
        {
          key: 'dimensionConfiguration',
          title: '维度配置',
          description: '配置预算维度，支持多维度预算分析',
          icon: 'el-icon-s-grid',
          itemCount: 0,
          lastUpdate: '-',
          path: '/ncv65/budget-system/dimension-configuration'
        },
        {
          key: 'indicatorManagement',
          title: '指标管理',
          description: '定义预算指标体系，建立KPI考核标准',
          icon: 'el-icon-data-line',
          itemCount: 0,
          lastUpdate: '-',
          path: '/ncv65/budget-system/indicator-management'
        },
        {
          key: 'budgetModel',
          title: '预算模型',
          description: '构建预算计算模型，支持复杂业务逻辑',
          icon: 'el-icon-s-operation',
          itemCount: 0,
          lastUpdate: '-',
          path: '/ncv65/budget-system/budget-model'
        },
        {
          key: 'permissionConfiguration',
          title: '权限配置',
          description: '配置预算管理权限，设置角色与数据访问范围',
          icon: 'el-icon-lock',
          itemCount: 0,
          lastUpdate: '-',
          path: '/ncv65/budget-system/permission-configuration'
        },
        {
          key: 'dataIntegration',
          title: '数据集成',
          description: '配置外部数据源集成，实现数据自动同步',
          icon: 'el-icon-connection',
          itemCount: 0,
          lastUpdate: '-',
          path: '/ncv65/budget-system/data-integration'
        },
        {
          key: 'systemMonitor',
          title: '系统监控',
          description: '监控系统运行状态，查看性能指标',
          icon: 'el-icon-monitor',
          itemCount: 0,
          lastUpdate: '-',
          path: '/ncv65/budget-system/system-monitor'
        },
        {
          key: 'auditTrail',
          title: '审计跟踪',
          description: '记录操作日志，支持合规审计追溯',
          icon: 'el-icon-document-checked',
          itemCount: 0,
          lastUpdate: '-',
          path: '/ncv65/budget-system/audit-trail'
        },
        {
          key: 'backupRestore',
          title: '备份恢复',
          description: '管理数据备份策略，支持快速恢复',
          icon: 'el-icon-upload',
          itemCount: 0,
          lastUpdate: '-',
          path: '/ncv65/budget-system/backup-restore'
        }
      ],
      
      // 配置步骤
      configSteps: [
        {
          title: '组织架构',
          description: '设置组织结构',
          detail: '建立完整的组织架构体系，包括公司、部门、成本中心等层级结构，为预算管理提供组织基础。',
          completed: true
        },
        {
          title: '维度配置',
          description: '配置预算维度',
          detail: '定义预算分析维度，如时间维度、组织维度、产品维度等，支持多维度预算分析和报告。',
          completed: true
        },
        {
          title: '指标体系',
          description: '建立指标体系',
          detail: '构建完整的预算指标体系，包括财务指标、业务指标、KPI指标等，建立量化考核标准。',
          completed: false
        },
        {
          title: '预算模型',
          description: '设计预算模型',
          detail: '根据业务特点设计预算计算模型，包括收入模型、成本模型、投资模型等。',
          completed: false
        },
        {
          title: '权限配置',
          description: '设置权限体系',
          detail: '配置预算管理权限，设置不同角色的操作权限和数据访问范围。',
          completed: false
        }
      ],
      currentStep: 2,
      
      // 体系健康度
      systemHealth: {
        overallScore: 0,
        details: [
          { key: 'completeness', label: '完整性', score: 0 },
          { key: 'consistency', label: '一致性', score: 0 },
          { key: 'accuracy', label: '准确性', score: 0 },
          { key: 'timeliness', label: '及时性', score: 0 },
          { key: 'usability', label: '可用性', score: 0 }
        ]
      },

      // 最近活动
      recentActivities: [],
      
      // 快速操作
      quickActions: [
        {
          key: 'createOrg',
          title: '创建组织单元',
          description: '快速创建新的组织架构单元',
          icon: 'el-icon-plus',
          action: 'create-organization'
        },
        {
          key: 'addDimension',
          title: '添加预算维度',
          description: '新增预算分析维度配置',
          icon: 'el-icon-s-grid',
          action: 'add-dimension'
        },
        {
          key: 'defineIndicator',
          title: '定义预算指标',
          description: '创建新的预算考核指标',
          icon: 'el-icon-data-line',
          action: 'define-indicator'
        },
        {
          key: 'buildModel',
          title: '构建预算模型',
          description: '设计新的预算计算模型',
          icon: 'el-icon-s-operation',
          action: 'build-model'
        }
      ],
      
      // 架构视图
      viewMode: 'tree',
      architectureChart: null,
      architectureTreeData: [],
      matrixData: [],
      organizationList: [],
      networkChart: null,
      cachedMatrixResponse: null
    }
  },

  watch: {
    viewMode(newVal) {
      this.$nextTick(() => {
        if (newVal === 'tree') {
          this.initArchitectureChart()
        } else if (newVal === 'network') {
          this.initNetworkChart()
        }
      })
    }
  },

  mounted() {
    this.loadSystemStats()
    this.loadSystemHealth()
    this.loadRecentActivities()
    this.loadModules()
    this.initArchitectureChart()
    this.loadMatrixData()
  },
  
  beforeDestroy() {
    if (this.architectureChart) {
      this.architectureChart.dispose()
    }
    if (this.networkChart) {
      this.networkChart.dispose()
    }
  },
  
  methods: {
    // 加载系统统计数据
    async loadSystemStats() {
      try {
        const response = await request({ url: '/glkj/accountant/budget/system/stats', method: 'get' })
        if (response.code === 1 && response.data) {
          Object.assign(this.systemStats, response.data)
        }
      } catch (e) {
        console.error('加载统计数据失败', e)
      }
    },
    // 加载系统健康度
    async loadSystemHealth() {
      try {
        const response = await request({ url: '/glkj/accountant/budget/system/health', method: 'get' })
        if (response.code === 1 && response.data) {
          this.systemHealth = response.data
        }
      } catch (e) {
        console.error('加载健康度失败', e)
      }
    },
    // 加载最近活动
    async loadRecentActivities() {
      try {
        const response = await request({ url: '/glkj/accountant/budget/system/activities', method: 'get', params: { limit: 10 } })
        if (response.code === 1 && response.data) {
          this.recentActivities = response.data
        }
      } catch (e) {
        console.error('加载最近活动失败', e)
      }
    },
    // 加载模块信息
    async loadModules() {
      try {
        const response = await request({ url: '/glkj/accountant/budget/system/modules', method: 'get' })
        if (response.code === 1 && response.data) {
          const moduleData = response.data
          this.modules.forEach(m => {
            const found = moduleData.find(d => d.key === m.key)
            if (found) {
              m.itemCount = found.itemCount || 0
              m.lastUpdate = found.lastUpdate || '-'
            }
          })
        }
      } catch (e) {
        console.error('加载模块信息失败', e)
      }
    },
    // 导航到模块
    navigateToModule(module) {
      this.$router.push(module.path)
    },
    
    // 快速操作
    handleQuickAction(action) {
      switch (action.action) {
        case 'create-organization':
          this.$router.push('/managementAccountant/ncv65/budgetSystem/organizationStructure?action=create')
          break
        case 'add-dimension':
          this.$router.push('/managementAccountant/ncv65/budgetSystem/dimensionManagement?action=add')
          break
        case 'define-indicator':
          this.$router.push('/managementAccountant/ncv65/budgetSystem/indicatorManagement?action=define')
          break
        case 'build-model':
          this.$router.push('/managementAccountant/ncv65/budgetSystem/budgetModel?action=build')
          break
      }
    },
    
    // 配置步骤操作
    handlePrevStep() {
      if (this.currentStep > 0) {
        this.currentStep--
      }
    },
    
    handleNextStep() {
      if (this.currentStep < this.configSteps.length - 1) {
        this.currentStep++
      } else {
        this.$message.success('配置向导完成！')
      }
    },
    
    handleGoToStep() {
      const step = this.configSteps[this.currentStep]
      const routeMap = {
        0: '/managementAccountant/ncv65/budgetSystem/organizationStructure',
        1: '/managementAccountant/ncv65/budgetSystem/dimensionManagement',
        2: '/managementAccountant/ncv65/budgetSystem/indicatorManagement',
        3: '/managementAccountant/ncv65/budgetSystem/budgetModel',
        4: '/managementAccountant/ncv65/budgetSystem/budgetHierarchy'
      }
      
      const route = routeMap[this.currentStep]
      if (route) {
        this.$router.push(route)
      }
    },
    
    // 获取步骤状态
    getStepStatus(index) {
      if (this.configSteps[index].completed) return 'success'
      if (index === this.currentStep) return 'process'
      return 'wait'
    },
    
    // 查看全部步骤
    viewAllSteps() {
      this.$router.push('/managementAccountant/ncv65/budgetSystem/configWizard')
    },
    
    // 查看健康度详情
    viewHealthDetail() {
      this.$router.push('/managementAccountant/ncv65/budgetSystem/healthReport')
    },
    
    // 查看全部活动
    viewAllActivities() {
      this.$router.push('/managementAccountant/ncv65/workbench/activities')
    },
    
    // 查看全部操作
    viewAllActions() {
      this.$router.push('/managementAccountant/ncv65/workbench/quickActions')
    },
    
    // 获取健康度颜色
    getHealthColor(score) {
      if (score >= 90) return '#67C23A'
      if (score >= 80) return '#E6A23C'
      if (score >= 70) return '#F56C6C'
      return '#909399'
    },
    
    // 获取活动状态类型
    getActivityStatusType(status) {
      const statusMap = {
        '已完成': 'success',
        '已审核': 'success',
        '运行中': 'primary',
        '待处理': 'warning',
        '已拒绝': 'danger'
      }
      return statusMap[status] || 'info'
    },
    
    // 初始化架构图表
    initArchitectureChart() {
      this.$nextTick(() => {
        if (this.$refs.architectureTree) {
          this.architectureChart = echarts.init(this.$refs.architectureTree)
          this.updateArchitectureChart()
        }
      })
    },
    
    // 更新架构图表
    async updateArchitectureChart() {
      if (!this.architectureChart) return
      // 从API加载组织树数据
      let treeData = this.architectureTreeData
      if (!treeData || treeData.length === 0) {
        try {
          const response = await request({ url: '/glkj/accountant/budget/organization-structure/tree/root', method: 'get' })
          if (response.code === 1 && response.data) {
            treeData = [response.data]
            this.architectureTreeData = treeData
          } else if (response.code === 0) {
            // 处理后端返回的业务错误
            console.warn('组织体系查询失败:', response.msg)
            this.$message.warning(response.msg || '组织体系不存在，请先创建组织架构')
          }
        } catch (e) {
          console.error('加载组织树失败', e)
          this.$message.error('加载组织架构数据失败，请检查网络连接')
        }
      }
      if (!treeData || treeData.length === 0) {
        treeData = [{ name: '暂无数据', children: [] }]
      }

      const option = {
        tooltip: {
          trigger: 'item',
          triggerOn: 'mousemove'
        },
        series: [
          {
            type: 'tree',
            data: treeData,
            top: '1%',
            left: '7%',
            bottom: '1%',
            right: '20%',
            symbolSize: 7,
            label: {
              position: 'left',
              verticalAlign: 'middle',
              align: 'right',
              fontSize: 12
            },
            leaves: {
              label: {
                position: 'right',
                verticalAlign: 'middle',
                align: 'left'
              }
            },
            emphasis: {
              focus: 'descendant'
            },
            expandAndCollapse: true,
            animationDuration: 550,
            animationDurationUpdate: 750
          }
        ]
      }
      
      this.architectureChart.setOption(option)
    },
    
    // 加载矩阵数据
    async loadMatrixData() {
      try {
        const response = await request({ url: '/glkj/accountant/budget/system/matrix', method: 'get' })
        if (response.code === 1 && response.data) {
          this.cachedMatrixResponse = response.data
          // 转换组织列表为表格列头格式
          this.organizationList = (response.data.organizations || []).map(org => ({
            id: org.id,
            code: org.code || org.id,
            name: org.name
          }))
          // 构建矩阵行：维度为行，组织为列
          const dimensions = response.data.dimensions || []
          const relations = response.data.relations || []
          this.matrixData = dimensions.map(dim => {
            const row = { dimension: dim.name }
            this.organizationList.forEach(org => {
              // 检查维度与组织之间是否存在关联（直接或通过模型间接关联）
              const hasRelation = relations.some(r =>
                (r.sourceType === 'dimension' && r.sourceId === dim.id && r.targetType === 'organization' && r.targetId === org.id) ||
                (r.sourceType === 'organization' && r.sourceId === org.id && r.targetType === 'dimension' && r.targetId === dim.id) ||
                relations.some(mr =>
                  mr.sourceType === 'model' && mr.targetType === 'dimension' && mr.targetId === dim.id &&
                  relations.some(mor =>
                    mor.sourceType === 'model' && mor.sourceId === mr.sourceId && mor.targetType === 'organization' && mor.targetId === org.id
                  )
                )
              )
              row[org.code || org.id] = hasRelation ? { type: 'success', label: '✓' } : null
            })
            return row
          })
        }
      } catch (e) {
        console.error('加载矩阵数据失败', e)
        this.matrixData = []
        this.organizationList = []
      }
    },

    // 初始化网络关系图
    initNetworkChart() {
      this.$nextTick(() => {
        if (this.$refs.architectureNetwork) {
          if (this.networkChart) {
            this.networkChart.dispose()
          }
          this.networkChart = echarts.init(this.$refs.architectureNetwork)
          this.updateNetworkChart()
        }
      })
    },

    // 更新网络关系图数据
    updateNetworkChart() {
      if (!this.networkChart) return
      const matrixData = this.cachedMatrixResponse
      if (!matrixData) {
        this.networkChart.setOption({
          graphic: { elements: [{ type: 'text', left: 'center', top: 'center', style: { text: '暂无数据', fontSize: 14, fill: '#999' } }] }
        })
        return
      }

      const nodes = []
      const links = []
      const categories = [
        { name: '组织' },
        { name: '维度' },
        { name: '指标' },
        { name: '模型' }
      ]

      // 组织节点
      ;(matrixData.organizations || []).forEach(org => {
        nodes.push({ id: 'org_' + org.id, name: org.name, category: 0, symbolSize: 30 })
      })
      // 维度节点
      ;(matrixData.dimensions || []).forEach(dim => {
        nodes.push({ id: 'dim_' + dim.id, name: dim.name, category: 1, symbolSize: 25 })
      })
      // 指标节点（限制20个避免过于密集）
      ;(matrixData.indicators || []).slice(0, 20).forEach(ind => {
        nodes.push({ id: 'ind_' + ind.id, name: ind.name, category: 2, symbolSize: 20 })
      })
      // 模型节点
      ;(matrixData.models || []).forEach(model => {
        nodes.push({ id: 'model_' + model.id, name: model.name, category: 3, symbolSize: 35 })
      })

      // 关联关系连线
      ;(matrixData.relations || []).forEach(rel => {
        const prefixMap = { organization: 'org_', dimension: 'dim_', indicator: 'ind_', model: 'model_' }
        const sourceId = (prefixMap[rel.sourceType] || '') + rel.sourceId
        const targetId = (prefixMap[rel.targetType] || '') + rel.targetId
        if (nodes.find(n => n.id === sourceId) && nodes.find(n => n.id === targetId)) {
          links.push({ source: sourceId, target: targetId })
        }
      })

      this.networkChart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}' },
        legend: { data: categories.map(c => c.name), top: 10 },
        series: [{
          type: 'graph',
          layout: 'force',
          data: nodes,
          links: links,
          categories: categories,
          roam: true,
          label: { show: true, position: 'right', fontSize: 10 },
          force: { repulsion: 200, edgeLength: [80, 200] },
          lineStyle: { color: 'source', curveness: 0.3 },
          emphasis: { focus: 'adjacency', lineStyle: { width: 3 } }
        }]
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-system-index {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);
  
  .page-header {
    margin-bottom: 24px;
    
    h1 {
      color: #303133;
      font-size: 28px;
      margin: 0 0 8px 0;
      font-weight: 600;
    }
    
    p {
      color: #606266;
      font-size: 16px;
      margin: 0;
    }
  }
  
  .overview-stats {
    margin-bottom: 24px;
    
    .stat-card {
      border: none;
      border-radius: 8px;
      transition: all 0.3s ease;
      
      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
      }
      
      .stat-content {
        display: flex;
        align-items: center;
        margin-bottom: 12px;
        
        .stat-icon {
          width: 48px;
          height: 48px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          
          i {
            font-size: 24px;
            color: white;
          }
        }
        
        .stat-info {
          flex: 1;
          
          h3 {
            font-size: 24px;
            font-weight: 600;
            color: #303133;
            margin: 0 0 4px 0;
          }
          
          p {
            font-size: 14px;
            color: #909399;
            margin: 0 0 4px 0;
          }
          
          .stat-detail {
            font-size: 12px;
            color: #C0C4CC;
          }
        }
      }
      
      .stat-footer {
        display: flex;
        justify-content: space-between;
        align-items: center;
        
        .stat-trend {
          font-size: 12px;
          font-weight: 500;
          
          &.up {
            color: #67C23A;
          }
          
          &.down {
            color: #F56C6C;
          }
          
          i {
            margin-right: 2px;
          }
        }
        
        .stat-label {
          font-size: 12px;
          color: #C0C4CC;
        }
      }
      
      &.organization-card .stat-icon {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }
      
      &.dimension-card .stat-icon {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }
      
      &.indicator-card .stat-icon {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }
      
      &.model-card .stat-icon {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }
    }
  }
  
  .module-nav-card,
  .wizard-card,
  .system-health-card,
  .recent-activities-card,
  .quick-actions-card,
  .architecture-card {
    margin-bottom: 24px;
    border: none;
    border-radius: 8px;
    
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      
      h3 {
        font-size: 18px;
        font-weight: 600;
        color: #303133;
        margin: 0;
      }
    }
  }
  
  .module-nav-card {
    .module-nav-header {
      margin-bottom: 20px;
      
      h3 {
        font-size: 18px;
        font-weight: 600;
        color: #303133;
        margin: 0 0 8px 0;
      }
      
      p {
        font-size: 14px;
        color: #606266;
        margin: 0;
      }
    }
    
    .module-grid {
      .module-item {
        background: white;
        border: 1px solid #EBEEF5;
        border-radius: 8px;
        padding: 20px;
        cursor: pointer;
        transition: all 0.3s ease;
        margin-bottom: 16px;
        
        &:hover {
          border-color: #409EFF;
          box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
          transform: translateY(-1px);
        }
        
        .module-icon {
          width: 40px;
          height: 40px;
          background: linear-gradient(135deg, #409EFF, #36CFC9);
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-bottom: 16px;
          
          i {
            font-size: 20px;
            color: white;
          }
        }
        
        .module-content {
          margin-bottom: 16px;
          
          h4 {
            font-size: 16px;
            font-weight: 600;
            color: #303133;
            margin: 0 0 8px 0;
          }
          
          p {
            font-size: 14px;
            color: #606266;
            margin: 0 0 12px 0;
            line-height: 1.5;
          }
          
          .module-stats {
            display: flex;
            gap: 16px;
            
            .stat-item {
              font-size: 12px;
              color: #909399;
              display: flex;
              align-items: center;
              
              i {
                margin-right: 4px;
              }
            }
          }
        }
        
        .module-action {
          text-align: right;
        }
      }
    }
  }
  
  .wizard-card {
    .wizard-steps {
      .step-content {
        margin-top: 30px;
        
        .current-step-info {
          text-align: center;
          
          h4 {
            color: #303133;
            margin: 0 0 12px 0;
          }
          
          p {
            color: #606266;
            margin: 0 0 20px 0;
            line-height: 1.6;
          }
          
          .step-actions {
            display: flex;
            justify-content: center;
            gap: 12px;
          }
        }
      }
    }
  }
  
  .system-health-card {
    .health-overview {
      .health-score {
        text-align: center;
        margin-bottom: 20px;
        
        .score-circle {
          margin-bottom: 12px;
          
          .score-text {
            font-size: 24px;
            font-weight: 600;
            color: #303133;
          }
        }
        
        .score-label {
          font-size: 14px;
          color: #606266;
          margin: 0;
        }
      }
      
      .health-details {
        .health-item {
          display: flex;
          align-items: center;
          margin-bottom: 12px;
          
          .health-label {
            width: 60px;
            font-size: 12px;
            color: #606266;
          }
          
          .health-progress {
            flex: 1;
            margin: 0 12px;
          }
          
          .health-score-text {
            width: 40px;
            font-size: 12px;
            color: #303133;
            text-align: right;
          }
        }
      }
    }
  }
  
  .recent-activities-card {
    .activities-list {
      .activity-item {
        display: flex;
        align-items: center;
        padding: 12px 0;
        border-bottom: 1px solid #F5F7FA;
        
        &:last-child {
          border-bottom: none;
        }
        
        .activity-avatar {
          margin-right: 12px;
        }
        
        .activity-content {
          flex: 1;
          
          .activity-text {
            font-size: 14px;
            color: #303133;
            margin: 0 0 4px 0;
            
            .activity-target {
              color: #409EFF;
            }
          }
          
          .activity-time {
            font-size: 12px;
            color: #909399;
            margin: 0;
          }
        }
        
        .activity-status {
          margin-left: 12px;
        }
      }
    }
  }
  
  .quick-actions-card {
    .quick-actions {
      .action-item {
        display: flex;
        align-items: center;
        padding: 16px;
        border: 1px solid #EBEEF5;
        border-radius: 8px;
        margin-bottom: 12px;
        cursor: pointer;
        transition: all 0.3s ease;
        
        &:hover {
          border-color: #409EFF;
          background-color: #F0F9FF;
        }
        
        .action-icon {
          width: 36px;
          height: 36px;
          background: #409EFF;
          border-radius: 6px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 12px;
          
          i {
            font-size: 16px;
            color: white;
          }
        }
        
        .action-content {
          flex: 1;
          
          h5 {
            font-size: 14px;
            font-weight: 500;
            color: #303133;
            margin: 0 0 4px 0;
          }
          
          p {
            font-size: 12px;
            color: #909399;
            margin: 0;
          }
        }
        
        .action-arrow {
          color: #C0C4CC;
        }
      }
    }
  }
  
  .architecture-card {
    .architecture-controls {
      display: flex;
      align-items: center;
    }
    
    .architecture-content {
      margin-top: 20px;
      
      .tree-view,
      .network-view {
        border: 1px solid #EBEEF5;
        border-radius: 4px;
      }
      
      .matrix-view {
        .architecture-matrix {
          max-height: 400px;
          overflow-y: auto;
        }
      }
    }
  }
}
</style>
