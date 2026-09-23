<template>
  <div class="advanced-features-index">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>高级功能管理</h2>
      <p>提供企业级高级预算功能，包括滚动预算、公式追踪、催报管理、穿透查询、多币种管理等专业功能</p>
    </div>

    <!-- 加载状态 - 使用v-if完全移除DOM，避免遮挡内容 -->
    <div v-if="loading" class="loading-overlay">
      <el-skeleton :rows="6" animated />
    </div>

    <!-- 正常内容 - 始终渲染 -->

    <!-- 功能统计概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card rolling-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ featureStats.rollingBudgets }}</div>
            <div class="stat-label">滚动预算</div>
            <div class="stat-description">活跃的滚动预算计划</div>
            <div class="stat-trend">
              <i class="el-icon-refresh"></i>
              <span>动态更新</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-refresh"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card formula-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ featureStats.formulaTraces }}</div>
            <div class="stat-label">公式追踪</div>
            <div class="stat-description">公式追踪任务数</div>
            <div class="stat-trend">
              <i class="el-icon-share"></i>
              <span>智能分析</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-share"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card reminder-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ featureStats.reminderTasks }}</div>
            <div class="stat-label">催报任务</div>
            <div class="stat-description">待处理催报任务</div>
            <div class="stat-trend">
              <i class="el-icon-bell"></i>
              <span>及时提醒</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-bell"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card currency-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ featureStats.currencies }}</div>
            <div class="stat-label">支持币种</div>
            <div class="stat-description">系统支持的币种数</div>
            <div class="stat-trend">
              <i class="el-icon-money"></i>
              <span>多币种</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-money"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能模块导航 -->
    <el-row :gutter="20" class="feature-modules">
      <el-col :span="8" v-for="module in featureModules" :key="module.id">
        <el-card 
          class="feature-module-card" 
          shadow="hover" 
          @click.native="handleModuleClick(module)"
        >
          <div class="module-header">
            <div class="module-icon">
              <i :class="module.icon"></i>
            </div>
            <div class="module-info">
              <h3>{{ module.name }}</h3>
              <p>{{ module.description }}</p>
            </div>
          </div>
          <div class="module-stats">
            <div class="stat-item">
              <span class="stat-label">任务数</span>
              <span class="stat-value">{{ module.taskCount }}</span>
            </div>
            <div class="stat-item">
              <span class="stat-label">最后更新</span>
              <span class="stat-value">{{ module.lastUpdate }}</span>
            </div>
          </div>
          <div class="module-actions">
            <el-button type="primary" size="mini" @click.stop="handleQuickAccess(module)">
              快速访问
            </el-button>
            <el-button type="text" size="mini" @click.stop="handleViewDetails(module)">
              查看详情
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快速操作面板 -->
    <el-card class="quick-actions-card" shadow="never">
      <div slot="header" class="card-header">
        <span>快速操作</span>
        <el-button icon="el-icon-refresh" size="mini" @click="refreshQuickActions">刷新</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="action in quickActions" :key="action.id">
          <div class="quick-action-item" @click="handleQuickAction(action)">
            <div class="action-icon">
              <i :class="action.icon"></i>
            </div>
            <div class="action-content">
              <div class="action-title">{{ action.title }}</div>
              <div class="action-description">{{ action.description }}</div>
            </div>
            <div class="action-arrow">
              <i class="el-icon-arrow-right"></i>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 最近活动 -->
    <el-card class="recent-activities-card" shadow="never">
      <div slot="header" class="card-header">
        <span>最近活动</span>
        <div class="header-tools">
          <el-button icon="el-icon-view" size="mini" @click="handleViewAllActivities">查看全部</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="getRecentActivities">刷新</el-button>
        </div>
      </div>
      <el-timeline>
        <el-timeline-item
          v-for="activity in recentActivities"
          :key="activity.id"
          :timestamp="activity.timestamp"
          :type="getActivityType(activity.type)"
        >
          <div class="activity-content">
            <div class="activity-title">{{ activity.title }}</div>
            <div class="activity-description">{{ activity.description }}</div>
            <div class="activity-user">操作人：{{ activity.operator }}</div>
          </div>
        </el-timeline-item>
      </el-timeline>
    </el-card>

    <!-- 系统通知 -->
    <el-card class="notifications-card" shadow="never">
      <div slot="header" class="card-header">
        <span>系统通知</span>
        <div class="header-tools">
          <el-badge :value="unreadNotifications" class="notification-badge">
            <el-button icon="el-icon-bell" size="mini" @click="handleViewNotifications">通知</el-button>
          </el-badge>
          <el-button icon="el-icon-setting" size="mini" @click="handleNotificationSettings">设置</el-button>
        </div>
      </div>
      <div class="notifications-list">
        <div 
          v-for="notification in notifications" 
          :key="notification.id"
          class="notification-item"
          :class="{ 'unread': !notification.isRead }"
          @click="handleNotificationClick(notification)"
        >
          <div class="notification-icon">
            <i :class="getNotificationIcon(notification.type)"></i>
          </div>
          <div class="notification-content">
            <div class="notification-title">{{ notification.title }}</div>
            <div class="notification-message">{{ notification.message }}</div>
            <div class="notification-time">{{ notification.time }}</div>
          </div>
          <div class="notification-status" v-if="!notification.isRead">
            <el-badge is-dot />
          </div>
        </div>
      </div>
    </el-card>
 
    <!-- 创建滚动预算对话框 -->
    <el-dialog
      title="创建滚动预算计划"
      :visible.sync="rollingBudgetDialog.visible"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="rollingBudgetForm"
        :model="rollingBudgetDialog.form"
        :rules="rollingBudgetDialog.rules"
        label-width="100px"
      >
        <el-form-item label="计划名称" prop="planName">
          <el-input v-model="rollingBudgetDialog.form.planName" placeholder="请输入计划名称" />
        </el-form-item>
        <el-form-item label="周期类型" prop="periodType">
          <el-select v-model="rollingBudgetDialog.form.periodType" placeholder="请选择周期类型" style="width: 100%">
            <el-option label="月度" value="MONTHLY" />
            <el-option label="季度" value="QUARTERLY" />
            <el-option label="年度" value="YEARLY" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker
            v-model="rollingBudgetDialog.form.startDate"
            type="date"
            placeholder="请选择开始日期"
            value-format="yyyy-MM-dd"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="结束日期" prop="endDate">
          <el-date-picker
            v-model="rollingBudgetDialog.form.endDate"
            type="date"
            placeholder="请选择结束日期"
            value-format="yyyy-MM-dd"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="滚动周期" prop="rollingCycle">
          <el-input-number v-model="rollingBudgetDialog.form.rollingCycle" :min="1" :max="12" style="width: 100%" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="rollingBudgetDialog.visible = false">取 消</el-button>
        <el-button type="primary" :loading="rollingBudgetDialog.loading" @click="submitRollingBudget">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 发送催报通知对话框 -->
    <el-dialog
      title="发送催报通知"
      :visible.sync="reminderDialog.visible"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="reminderForm"
        :model="reminderDialog.form"
        :rules="reminderDialog.rules"
        label-width="100px"
      >
        <el-form-item label="催报内容" prop="message">
          <el-input
            v-model="reminderDialog.form.message"
            type="textarea"
            :rows="4"
            placeholder="请输入催报通知内容"
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="reminderDialog.visible = false">取 消</el-button>
        <el-button type="primary" :loading="reminderDialog.loading" @click="submitReminder">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { advancedFeaturesApi } from '@/api/managementAccountant/ncv65/advancedFeatures'

export default {
  name: 'AdvancedFeaturesIndex',
  data() {
    return {
      // 加载和错误状态
      loading: false,
      error: false,
      errorMessage: '',

      // 功能统计
      featureStats: {
        rollingBudgets: 0,
        formulaTraces: 0,
        reminderTasks: 0,
        currencies: 0
      },
      
      // 功能模块
      featureModules: [
        {
          id: 1,
          name: '滚动预算',
          description: '动态滚动预算管理，支持多种滚动周期和预测模型',
          icon: 'el-icon-refresh',
          route: '/qmys/RollingBudget',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 2,
          name: '公式追踪',
          description: '预算公式依赖关系追踪和计算路径分析',
          icon: 'el-icon-share',
          route: '/qmys/FormulaTrace',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 3,
          name: '催报管理',
          description: '预算填报催报策略配置和执行管理',
          icon: 'el-icon-bell',
          route: '/qmys/ReminderManagement',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 4,
          name: '穿透查询',
          description: '多维度数据穿透查询和钻取分析',
          icon: 'el-icon-search',
          route: '/qmys/DrillThroughQuery',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 5,
          name: '多币种管理',
          description: '多币种预算管理和汇率转换',
          icon: 'el-icon-money',
          route: '/qmys/CurrencyManagement',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 6,
          name: '批量计算',
          description: '预算数据批量计算和公式执行',
          icon: 'el-icon-setting',
          route: '/qmys/BatchCalculation',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 7,
          name: '智能推荐',
          description: 'AI智能预算推荐和优化建议',
          icon: 'el-icon-star-off',
          route: '/qmys/IntelligentRecommendation',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 8,
          name: '预算模拟',
          description: '预算场景模拟和敏感性分析',
          icon: 'el-icon-data-line',
          route: '/qmys/BudgetSimulation',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 9,
          name: '数据挖掘',
          description: '预算数据挖掘和模式识别',
          icon: 'el-icon-pie-chart',
          route: '/qmys/DataMining',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 10,
          name: '预算优化',
          description: '预算方案优化和资源配置建议',
          icon: 'el-icon-s-flag',
          route: '/qmys/BudgetOptimization',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 11,
          name: '风险评估',
          description: '预算风险识别和评估管理',
          icon: 'el-icon-warning',
          route: '/qmys/RiskAssessment',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 12,
          name: '协同编制',
          description: '多部门协同预算编制和审批',
          icon: 'el-icon-user',
          route: '/qmys/CollaborativeBudgeting',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 13,
          name: '版本对比',
          description: '预算版本对比分析和差异识别',
          icon: 'el-icon-document-copy',
          route: '/qmys/VersionComparison',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 14,
          name: '自动化流程',
          description: '预算流程自动化和智能调度',
          icon: 'el-icon-setting',
          route: '/qmys/AutomationWorkflow',
          taskCount: 0,
          lastUpdate: '-'
        },
        {
          id: 15,
          name: '高级报表',
          description: '高级预算报表设计和生成',
          icon: 'el-icon-document',
          route: '/qmys/AdvancedReports',
          taskCount: 0,
          lastUpdate: '-'
        }
      ],
      
      // 快速操作
      quickActions: [
        {
          id: 1,
          title: '创建滚动预算',
          description: '快速创建新的滚动预算计划',
          icon: 'el-icon-plus',
          action: 'createRollingBudget'
        },
        {
          id: 2,
          title: '执行公式追踪',
          description: '对指定公式执行依赖关系追踪',
          icon: 'el-icon-share',
          action: 'executeFormulaTrace'
        },
        {
          id: 3,
          title: '发送催报通知',
          description: '向相关人员发送预算催报通知',
          icon: 'el-icon-message',
          action: 'sendReminder'
        },
        {
          id: 4,
          title: '汇率更新',
          description: '更新最新的汇率信息',
          icon: 'el-icon-refresh',
          action: 'updateExchangeRate'
        }
      ],
      
      // 最近活动
      recentActivities: [],
      
      // 系统通知
      notifications: [],
      unreadNotifications: 0,

      // 创建滚动预算对话框
      rollingBudgetDialog: {
        visible: false,
        loading: false,
        form: {
          planName: '',
          periodType: '',
          startDate: '',
          endDate: '',
          rollingCycle: 1
        },
        rules: {
          planName: [{ required: true, message: '请输入计划名称', trigger: 'blur' }],
          periodType: [{ required: true, message: '请选择周期类型', trigger: 'change' }],
          startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
          endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }],
          rollingCycle: [{ required: true, message: '请输入滚动周期', trigger: 'blur' }]
        }
      },

      // 发送催报通知对话框
      reminderDialog: {
        visible: false,
        loading: false,
        form: {
          message: ''
        },
        rules: {
          message: [{ required: true, message: '请输入催报通知内容', trigger: 'blur' }]
        }
      }
    }
  },
  
  created() {
    this.getFeatureStats()
    this.getModuleStats()
    this.getRecentActivities()
    this.getNotifications()
  },

  mounted() {
  },

  methods: {

    // 获取功能统计
    async getFeatureStats() {
      this.loading = true
      try {
        const response = await advancedFeaturesApi.getFeatureStats()
        if (response && response.code === 1 && response.data) {
          this.featureStats = response.data
        }
      } catch (error) {
        console.error('获取功能统计失败：', error)
      } finally {
        this.loading = false
      }
    },

    // 获取各模块统计数据
    async getModuleStats() {
      try {
        const response = await advancedFeaturesApi.getModuleStats()
        if (response && response.code === 1 && response.data) {
          const statsMap = {}
          response.data.forEach(item => {
            statsMap[item.moduleId] = item
          })
          this.featureModules.forEach(module => {
            const stat = statsMap[module.id]
            if (stat) {
              module.taskCount = stat.taskCount || 0
              module.lastUpdate = stat.lastUpdate
                ? this.formatDate(stat.lastUpdate)
                : '-'
            }
          })
        }
      } catch (error) {
        console.error('获取模块统计数据失败：', error)
      }
    },

    // 格式化日期
    formatDate(dateStr) {
      if (!dateStr) return '-'
      const date = new Date(dateStr)
      if (isNaN(date.getTime())) return '-'
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}`
    },

    // 获取最近活动
    async getRecentActivities() {
      try {
        const response = await advancedFeaturesApi.getRecentActivities()
        if (response && response.code === 1 && response.data) {
          this.recentActivities = response.data
        }
      } catch (error) {
        console.error('获取最近活动失败：', error)
      }
    },

    // 获取系统通知
    async getNotifications() {
      try {
        const response = await advancedFeaturesApi.getNotifications()
        if (response && response.code === 1 && response.data) {
          this.notifications = response.data
          this.unreadNotifications = this.notifications.filter(n => !n.isRead).length
        }
      } catch (error) {
        console.error('获取系统通知失败：', error)
      }
    },

    // 模块点击
    handleModuleClick(module) {
      this.$router.push(module.route)
    },

    // 快速访问
    handleQuickAccess(module) {
      this.$router.push(module.route)
    },

    // 查看详情
    handleViewDetails(module) {
      this.$router.push(module.route)
    },

    // 快速操作
    handleQuickAction(action) {
      switch (action.action) {
        case 'createRollingBudget':
          this.rollingBudgetDialog.form = { planName: '', periodType: '', startDate: '', endDate: '', rollingCycle: 1 }
          this.rollingBudgetDialog.visible = true
          break
        case 'executeFormulaTrace':
          this.$confirm('确认执行最新的公式追踪任务？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.doExecuteFormulaTrace()
          }).catch(() => {})
          break
        case 'sendReminder':
          this.reminderDialog.form = { message: '' }
          this.reminderDialog.visible = true
          break
        case 'updateExchangeRate':
          this.$confirm('确认更新所有币种的最新汇率？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.doUpdateExchangeRate()
          }).catch(() => {})
          break
        default:
          this.$message.info(`${action.title}功能暂未开放`)
      }
    },

    // 提交创建滚动预算
    submitRollingBudget() {
      this.$refs.rollingBudgetForm.validate(async (valid) => {
        if (!valid) return
        this.rollingBudgetDialog.loading = true
        try {
          const response = await advancedFeaturesApi.createRollingBudgetPlan(this.rollingBudgetDialog.form)
          if (response && response.code === 1) {
            this.$message.success('滚动预算计划创建成功')
            this.rollingBudgetDialog.visible = false
            this.getFeatureStats()
            this.getModuleStats()
          } else {
            this.$message.error((response && response.msg) || '创建失败')
          }
        } catch (error) {
          console.error('创建滚动预算失败：', error)
          this.$message.error('创建滚动预算失败，请稍后重试')
        } finally {
          this.rollingBudgetDialog.loading = false
        }
      })
    },

    // 执行公式追踪
    async doExecuteFormulaTrace() {
      const loading = this.$loading({ lock: true, text: '正在执行公式追踪...', background: 'rgba(0, 0, 0, 0.7)' })
      try {
        const listRes = await advancedFeaturesApi.getFormulaTraceTaskList({})
        const tasks = listRes && listRes.code === 1 && listRes.data
          ? (Array.isArray(listRes.data) ? listRes.data : (listRes.data.list || listRes.data.tlist || []))
          : []
        if (!tasks.length) {
          this.$message.warning('暂无可执行的公式追踪任务')
          return
        }
        const taskId = tasks[0].taskId || tasks[0].id
        const response = await advancedFeaturesApi.executeFormulaTrace(taskId)
        if (response && response.code === 1) {
          this.$message.success('公式追踪任务执行成功')
          this.getFeatureStats()
          this.getModuleStats()
        } else {
          this.$message.error((response && response.msg) || '执行失败')
        }
      } catch (error) {
        console.error('执行公式追踪失败：', error)
        this.$message.error('执行公式追踪失败，请稍后重试')
      } finally {
        loading.close()
      }
    },

    // 提交发送催报通知
    submitReminder() {
      this.$refs.reminderForm.validate(async (valid) => {
        if (!valid) return
        this.reminderDialog.loading = true
        try {
          const strategyRes = await advancedFeaturesApi.getReminderStrategyList({})
          const strategies = strategyRes && strategyRes.code === 1 && strategyRes.data
            ? (Array.isArray(strategyRes.data) ? strategyRes.data : (strategyRes.data.list || strategyRes.data.tlist || []))
            : []
          if (!strategies.length) {
            this.$message.warning('暂无可用的催报策略')
            this.reminderDialog.loading = false
            return
          }
          const response = await advancedFeaturesApi.sendReminder({
            strategyId: strategies[0].strategyId || strategies[0].id,
            targets: strategies.map(s => s.strategyId || s.id),
            message: this.reminderDialog.form.message
          })
          if (response && response.code === 1) {
            this.$message.success('催报通知发送成功')
            this.reminderDialog.visible = false
            this.getFeatureStats()
            this.getModuleStats()
          } else {
            this.$message.error((response && response.msg) || '发送失败')
          }
        } catch (error) {
          console.error('发送催报通知失败：', error)
          this.$message.error('发送催报通知失败，请稍后重试')
        } finally {
          this.reminderDialog.loading = false
        }
      })
    },

    // 更新汇率
    async doUpdateExchangeRate() {
      const loading = this.$loading({ lock: true, text: '正在更新汇率...', background: 'rgba(0, 0, 0, 0.7)' })
      try {
        const response = await advancedFeaturesApi.updateCurrencyRate('ALL')
        if (response && response.code === 1) {
          this.$message.success('汇率更新成功')
          this.getFeatureStats()
          this.getModuleStats()
        } else {
          this.$message.error((response && response.msg) || '更新失败')
        }
      } catch (error) {
        console.error('更新汇率失败：', error)
        this.$message.error('更新汇率失败，请稍后重试')
      } finally {
        loading.close()
      }
    },

    // 刷新快速操作
    refreshQuickActions() {
      this.getFeatureStats()
      this.getModuleStats()
      this.$message.success('已刷新')
    },

    // 查看全部活动
    handleViewAllActivities() {
      this.$message({
        message: '活动列表页面正在开发中',
        type: 'info',
        duration: 2000
      })
      // this.$router.push('/managementAccountant/ncv65/advancedFeatures/activities')
    },

    // 查看通知
    handleViewNotifications() {
      this.$message({
        message: '通知中心页面正在开发中',
        type: 'info',
        duration: 2000
      })
      // this.$router.push('/managementAccountant/ncv65/advancedFeatures/notifications')
    },

    // 通知设置
    handleNotificationSettings() {
      this.$message({
        message: '通知设置页面正在开发中',
        type: 'info',
        duration: 2000
      })
      // this.$router.push('/managementAccountant/ncv65/advancedFeatures/notificationSettings')
    },

    // 通知点击
    handleNotificationClick(notification) {
      if (!notification.isRead) {
        this.markNotificationAsRead(notification.id)
      }
    },
    
    // 标记通知为已读
    async markNotificationAsRead(notificationId) {
      try {
        await advancedFeaturesApi.markNotificationAsRead(notificationId)
        const notification = this.notifications.find(n => n.id === notificationId)
        if (notification) {
          notification.isRead = true
          this.unreadNotifications--
        }
      } catch (error) {
        console.error('标记通知失败：', error)
      }
    },
    
    // 获取活动类型
    getActivityType(type) {
      const typeMap = {
        'create': 'primary',
        'update': 'warning',
        'delete': 'danger',
        'execute': 'success'
      }
      return typeMap[type] || 'primary'
    },
    
    // 获取通知图标
    getNotificationIcon(type) {
      const iconMap = {
        'info': 'el-icon-info',
        'warning': 'el-icon-warning',
        'error': 'el-icon-error',
        'success': 'el-icon-success'
      }
      return iconMap[type] || 'el-icon-info'
    }
  }
}
</script>

<style lang="scss" scoped>
// 主容器样式 - 简化结构，移除双层wrapper
.advanced-features-index {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: calc(100vh - 84px);
  position: relative;

  // 加载覆盖层 - 使用绝对定位，不影响内容渲染
  .loading-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(255, 255, 255, 0.9);
    z-index: 1000;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 20px;
  }

  .page-header {
    margin-bottom: 20px;
    background: white;
    padding: 20px;
    border-radius: 4px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    h2 {
      color: #303133;
      font-size: 24px;
      margin: 0 0 8px 0;
      font-weight: 600;
    }

    p {
      color: #606266;
      font-size: 14px;
      margin: 0;
      line-height: 1.5;
    }
  }

  // 加载和错误状态样式
  .loading-wrapper,
  .error-alert {
    margin-bottom: 20px;
  }

  .loading-wrapper {
    background: white;
    padding: 20px;
    border-radius: 4px;
  }

  .stats-row {
    margin-bottom: 20px;

    .stat-card {
      border: none;
      border-radius: 8px;
      position: relative;
      overflow: hidden;
      min-height: 120px;

      ::v-deep .el-card__body {
        padding: 20px;
      }

      &.rolling-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF) !important;
        color: white !important;
      }

      &.formula-card {
        background: linear-gradient(135deg, #67C23A, #85CE61) !important;
        color: white !important;
      }

      &.reminder-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77) !important;
        color: white !important;
      }

      &.currency-card {
        background: linear-gradient(135deg, #F56C6C, #F78989) !important;
        color: white !important;
      }

      .stat-content {
        position: relative;
        z-index: 2;

        .stat-number {
          font-size: 32px;
          font-weight: 700;
          margin-bottom: 8px;
          color: white;
        }

        .stat-label {
          font-size: 16px;
          opacity: 0.95;
          margin-bottom: 4px;
          font-weight: 500;
          color: white;
        }

        .stat-description {
          font-size: 12px;
          opacity: 0.85;
          margin-bottom: 8px;
          color: white;
        }

        .stat-trend {
          font-size: 12px;
          opacity: 0.9;
          color: white;

          i {
            margin-right: 4px;
          }
        }
      }

      .stat-icon {
        position: absolute;
        top: 20px;
        right: 20px;
        font-size: 48px;
        opacity: 0.3;
        z-index: 1;
        color: white;
      }
    }
  }

  .feature-modules {
    margin-bottom: 20px;

    .feature-module-card {
      margin-bottom: 20px;
      cursor: pointer;
      transition: all 0.3s ease;
      background: white !important;
      border: 1px solid #EBEEF5;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15) !important;
        border-color: #409EFF;
      }

      .module-header {
        display: flex;
        align-items: flex-start;
        margin-bottom: 16px;

        .module-icon {
          width: 48px;
          height: 48px;
          background: linear-gradient(135deg, #409EFF, #66B1FF);
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          flex-shrink: 0;

          i {
            font-size: 24px;
            color: white;
          }
        }

        .module-info {
          flex: 1;

          h3 {
            font-size: 16px;
            color: #303133;
            margin: 0 0 8px 0;
            font-weight: 600;
          }

          p {
            font-size: 13px;
            color: #909399;
            margin: 0;
            line-height: 1.5;
          }
        }
      }

      .module-stats {
        display: flex;
        justify-content: space-between;
        margin-bottom: 16px;
        padding: 12px 0;
        border-top: 1px solid #EBEEF5;
        border-bottom: 1px solid #EBEEF5;

        .stat-item {
          text-align: center;
          flex: 1;

          .stat-label {
            display: block;
            font-size: 12px;
            color: #909399;
            margin-bottom: 4px;
          }

          .stat-value {
            display: block;
            font-size: 18px;
            font-weight: 600;
            color: #409EFF;
          }
        }
      }

      .module-actions {
        display: flex;
        justify-content: space-between;
        align-items: center;
        gap: 8px;
      }
    }
  }

  .quick-actions-card,
  .recent-activities-card,
  .notifications-card {
    margin-bottom: 20px;
    background: white !important;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-weight: 600;
      font-size: 16px;

      .header-tools {
        display: flex;
        gap: 8px;
        align-items: center;

        .notification-badge {
          margin-right: 8px;
        }
      }
    }
  }

  .quick-actions-card {
    .quick-action-item {
      display: flex;
      align-items: center;
      padding: 16px;
      border: 1px solid #EBEEF5;
      border-radius: 6px;
      cursor: pointer;
      transition: all 0.3s ease;

      &:hover {
        border-color: #409EFF;
        background: #F0F8FF;
      }

      .action-icon {
        width: 40px;
        height: 40px;
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 12px;

        i {
          font-size: 18px;
          color: white;
        }
      }

      .action-content {
        flex: 1;

        .action-title {
          font-size: 14px;
          font-weight: 500;
          color: #303133;
          margin-bottom: 4px;
        }

        .action-description {
          font-size: 12px;
          color: #606266;
        }
      }

      .action-arrow {
        color: #C0C4CC;
        font-size: 14px;
      }
    }
  }

  .recent-activities-card {
    .card-header {
      .header-tools {
        display: flex;
        gap: 8px;
      }
    }

    .activity-content {
      .activity-title {
        font-size: 14px;
        font-weight: 500;
        color: #303133;
        margin-bottom: 4px;
      }

      .activity-description {
        font-size: 12px;
        color: #606266;
        margin-bottom: 4px;
      }

      .activity-user {
        font-size: 11px;
        color: #909399;
      }
    }
  }

  .notifications-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .header-tools {
        display: flex;
        gap: 8px;
        align-items: center;

        .notification-badge {
          margin-right: 8px;
        }
      }
    }

    .notifications-list {
      .notification-item {
        display: flex;
        align-items: flex-start;
        padding: 12px;
        border-bottom: 1px solid #F5F7FA;
        cursor: pointer;
        transition: all 0.3s ease;

        &:hover {
          background: #F5F7FA;
        }

        &.unread {
          background: #F0F8FF;
          border-left: 3px solid #409EFF;
        }

        .notification-icon {
          width: 32px;
          height: 32px;
          border-radius: 50%;
          background: #F5F7FA;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 12px;

          i {
            font-size: 16px;
            color: #409EFF;
          }
        }

        .notification-content {
          flex: 1;

          .notification-title {
            font-size: 14px;
            font-weight: 500;
            color: #303133;
            margin-bottom: 4px;
          }

          .notification-message {
            font-size: 12px;
            color: #606266;
            margin-bottom: 4px;
            line-height: 1.4;
          }

          .notification-time {
            font-size: 11px;
            color: #909399;
          }
        }

        .notification-status {
          margin-left: 8px;
        }
      }
    }
  }
}
</style>
