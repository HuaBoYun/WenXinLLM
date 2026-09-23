<template>
  <el-dialog
    title="控制措施详情"
    :visible.sync="dialogVisible"
    width="1200px"
    :close-on-click-modal="false"
    top="5vh"
  >
    <div v-loading="loading" class="measure-detail">
      <el-tabs v-model="activeTab" type="border-card">
        <!-- 基本信息 -->
        <el-tab-pane label="基本信息" name="basic">
          <div class="detail-section">
            <el-row :gutter="20">
              <el-col :span="12">
                <div class="detail-item">
                  <label>企业名称：</label>
                  <span>{{ measureData.enterpriseName || '-' }}</span>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="detail-item">
                  <label>措施名称：</label>
                  <span>{{ measureData.measureName || '-' }}</span>
                </div>
              </el-col>
            </el-row>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <div class="detail-item">
                  <label>措施类型：</label>
                  <el-tag :type="getMeasureTypeTagType(measureData.measureType)">
                    {{ getMeasureTypeLabel(measureData.measureType) }}
                  </el-tag>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="detail-item">
                  <label>优先级：</label>
                  <el-tag :type="getPriorityTagType(measureData.priority)">
                    {{ getPriorityLabel(measureData.priority) }}
                  </el-tag>
                </div>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <div class="detail-item">
                  <label>负责人：</label>
                  <span>{{ measureData.responsiblePerson || '-' }}</span>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="detail-item">
                  <label>负责部门：</label>
                  <span>{{ measureData.responsibleDepartment || '-' }}</span>
                </div>
              </el-col>
            </el-row>

            <div class="detail-item">
              <label>措施描述：</label>
              <p>{{ measureData.measureDescription || '-' }}</p>
            </div>

            <div class="detail-item">
              <label>实施目标：</label>
              <p>{{ measureData.implementationGoal || '-' }}</p>
            </div>

            <div class="detail-item">
              <label>关联风险：</label>
              <div class="risk-tags">
                <el-tag
                  v-for="risk in getRiskTags(measureData.relatedRisks)"
                  :key="risk"
                  type="warning"
                  class="risk-tag"
                >
                  {{ risk }}
                </el-tag>
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 实施计划 -->
        <el-tab-pane label="实施计划" name="plan">
          <div class="detail-section">
            <!-- 时间计划 -->
            <div class="plan-timeline">
              <h4>时间计划</h4>
              <el-row :gutter="20">
                <el-col :span="8">
                  <div class="timeline-item">
                    <div class="timeline-label">计划开始</div>
                    <div class="timeline-value">{{ measureData.plannedStartDate || '-' }}</div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="timeline-item">
                    <div class="timeline-label">计划完成</div>
                    <div class="timeline-value">{{ measureData.plannedEndDate || '-' }}</div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="timeline-item">
                    <div class="timeline-label">计划工期</div>
                    <div class="timeline-value">{{ calculateDuration(measureData.plannedStartDate, measureData.plannedEndDate) }}</div>
                  </div>
                </el-col>
              </el-row>
            </div>

            <!-- 预算信息 -->
            <div class="budget-info">
              <h4>预算信息</h4>
              <el-row :gutter="20">
                <el-col :span="8">
                  <div class="budget-item">
                    <div class="budget-label">预算金额</div>
                    <div class="budget-value">
                      <span class="amount">{{ formatAmount(measureData.budgetAmount) }}</span>
                      <span class="currency">元</span>
                    </div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="budget-item">
                    <div class="budget-label">已使用金额</div>
                    <div class="budget-value">
                      <span class="amount">{{ formatAmount(measureData.usedAmount) }}</span>
                      <span class="currency">元</span>
                    </div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="budget-item">
                    <div class="budget-label">剩余预算</div>
                    <div class="budget-value">
                      <span class="amount" :class="getRemainingBudgetClass(measureData)">
                        {{ formatAmount(calculateRemainingBudget(measureData)) }}
                      </span>
                      <span class="currency">元</span>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>

            <!-- 实施步骤 -->
            <div class="implementation-steps">
              <h4>实施步骤</h4>
              <el-steps :active="getCurrentStepIndex()" direction="vertical">
                <el-step
                  v-for="(step, index) in getImplementationSteps(measureData.implementationSteps)"
                  :key="index"
                  :title="`步骤${step.stepNumber}`"
                  :description="step.stepDescription"
                  :status="getStepStatus(index)"
                />
              </el-steps>
            </div>

            <!-- 成功标准 -->
            <div class="success-criteria">
              <h4>成功标准</h4>
              <p>{{ measureData.successCriteria || '-' }}</p>
            </div>

            <!-- 风险评估 -->
            <div class="risk-assessment">
              <h4>风险评估</h4>
              <p>{{ measureData.riskAssessment || '-' }}</p>
            </div>
          </div>
        </el-tab-pane>

        <!-- 实施状态 -->
        <el-tab-pane label="实施状态" name="status">
          <div class="detail-section">
            <!-- 状态概览 -->
            <div class="status-overview">
              <el-row :gutter="20">
                <el-col :span="8">
                  <div class="status-card">
                    <div class="status-header">
                      <i class="el-icon-info"></i>
                      <span>实施状态</span>
                    </div>
                    <div class="status-content">
                      <el-tag :type="getImplementationStatusTagType(measureData.implementationStatus)" size="large">
                        {{ getImplementationStatusLabel(measureData.implementationStatus) }}
                      </el-tag>
                    </div>
                  </div>
                </el-col>
                
                <el-col :span="8">
                  <div class="status-card">
                    <div class="status-header">
                      <i class="el-icon-time"></i>
                      <span>实施进度</span>
                    </div>
                    <div class="status-content">
                      <div class="progress-display">
                        <el-progress
                          :percentage="measureData.implementationProgress || 0"
                          :color="getProgressColor(measureData.implementationProgress)"
                          stroke-width="8"
                        />
                      </div>
                    </div>
                  </div>
                </el-col>

                <el-col :span="8">
                  <div class="status-card">
                    <div class="status-header">
                      <i class="el-icon-star-on"></i>
                      <span>效果评估</span>
                    </div>
                    <div class="status-content">
                      <el-rate
                        v-if="measureData.implementationStatus === 'COMPLETED'"
                        v-model="measureData.effectiveness"
                        :max="5"
                        disabled
                        show-score
                        text-color="#ff9900"
                        score-template="{value}分"
                      />
                      <span v-else class="no-rating">未评估</span>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>

            <!-- 时间信息 -->
            <div class="time-info">
              <h4>时间信息</h4>
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="time-item">
                    <label>实际开始时间：</label>
                    <span>{{ measureData.actualStartDate || '-' }}</span>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="time-item">
                    <label>实际完成时间：</label>
                    <span>{{ measureData.actualEndDate || '-' }}</span>
                  </div>
                </el-col>
              </el-row>
              
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="time-item">
                    <label>实际工期：</label>
                    <span>{{ calculateActualDuration(measureData) }}</span>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="time-item">
                    <label>进度状态：</label>
                    <el-tag :type="getScheduleStatusTagType(measureData)">
                      {{ getScheduleStatusLabel(measureData) }}
                    </el-tag>
                  </div>
                </el-col>
              </el-row>
            </div>

            <!-- 监控配置 -->
            <div class="monitoring-config">
              <h4>监控配置</h4>
              <el-row :gutter="20">
                <el-col :span="12">
                  <div class="config-item">
                    <label>监控频率：</label>
                    <span>{{ getMonitoringFrequencyLabel(measureData.monitoringFrequency) }}</span>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div class="config-item">
                    <label>报告频率：</label>
                    <span>{{ getReportingFrequencyLabel(measureData.reportingFrequency) }}</span>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-tab-pane>

        <!-- 进度记录 -->
        <el-tab-pane label="进度记录" name="progress">
          <div class="detail-section">
            <div class="progress-records">
              <h4>进度更新记录</h4>
              <el-table :data="progressRecords" border stripe>
                <el-table-column prop="updateDate" label="更新日期" width="120" />
                <el-table-column prop="previousProgress" label="之前进度" width="100">
                  <template slot-scope="scope">
                    {{ scope.row.previousProgress }}%
                  </template>
                </el-table-column>
                <el-table-column prop="currentProgress" label="当前进度" width="100">
                  <template slot-scope="scope">
                    <span class="progress-value">{{ scope.row.currentProgress }}%</span>
                  </template>
                </el-table-column>
                <el-table-column prop="progressChange" label="进度变化" width="100">
                  <template slot-scope="scope">
                    <span :class="getProgressChangeClass(scope.row.progressChange)">
                      {{ scope.row.progressChange > 0 ? '+' : '' }}{{ scope.row.progressChange }}%
                    </span>
                  </template>
                </el-table-column>
                <el-table-column prop="updateReason" label="更新说明" show-overflow-tooltip />
                <el-table-column prop="updateBy" label="更新人" width="100" />
              </el-table>
            </div>

            <!-- 里程碑记录 -->
            <div class="milestone-records">
              <h4>里程碑记录</h4>
              <el-timeline>
                <el-timeline-item
                  v-for="milestone in milestoneRecords"
                  :key="milestone.id"
                  :timestamp="milestone.achieveDate"
                  :type="getMilestoneType(milestone.status)"
                >
                  <el-card>
                    <h4>{{ milestone.milestoneName }}</h4>
                    <p>{{ milestone.description }}</p>
                    <div class="milestone-meta">
                      <el-tag :type="getMilestoneStatusTagType(milestone.status)" size="small">
                        {{ getMilestoneStatusLabel(milestone.status) }}
                      </el-tag>
                      <span class="milestone-achiever">完成人：{{ milestone.achieveBy }}</span>
                    </div>
                  </el-card>
                </el-timeline-item>
              </el-timeline>
            </div>
          </div>
        </el-tab-pane>

        <!-- 资源管理 -->
        <el-tab-pane label="资源管理" name="resources">
          <div class="detail-section">
            <!-- 人力资源 -->
            <div class="human-resources">
              <h4>人力资源</h4>
              <el-table :data="humanResources" border stripe>
                <el-table-column prop="personName" label="姓名" width="100" />
                <el-table-column prop="role" label="角色" width="120" />
                <el-table-column prop="department" label="部门" width="150" />
                <el-table-column prop="workload" label="工作量" width="100">
                  <template slot-scope="scope">
                    {{ scope.row.workload }}%
                  </template>
                </el-table-column>
                <el-table-column prop="startDate" label="开始日期" width="120" />
                <el-table-column prop="endDate" label="结束日期" width="120" />
                <el-table-column prop="status" label="状态" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="getResourceStatusTagType(scope.row.status)" size="small">
                      {{ getResourceStatusLabel(scope.row.status) }}
                    </el-tag>
                  </template>
                </el-table-column>
              </el-table>
            </div>

            <!-- 物质资源 -->
            <div class="material-resources">
              <h4>物质资源</h4>
              <el-table :data="materialResources" border stripe>
                <el-table-column prop="resourceName" label="资源名称" width="150" />
                <el-table-column prop="resourceType" label="资源类型" width="120" />
                <el-table-column prop="quantity" label="数量" width="100" />
                <el-table-column prop="unit" label="单位" width="80" />
                <el-table-column prop="unitCost" label="单价" width="100">
                  <template slot-scope="scope">
                    ¥{{ scope.row.unitCost }}
                  </template>
                </el-table-column>
                <el-table-column prop="totalCost" label="总价" width="120">
                  <template slot-scope="scope">
                    <span class="cost-value">¥{{ scope.row.totalCost }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="supplier" label="供应商" show-overflow-tooltip />
                <el-table-column prop="status" label="状态" width="100">
                  <template slot-scope="scope">
                    <el-tag :type="getResourceStatusTagType(scope.row.status)" size="small">
                      {{ getResourceStatusLabel(scope.row.status) }}
                    </el-tag>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </el-tab-pane>

        <!-- 效果评估 -->
        <el-tab-pane label="效果评估" name="evaluation">
          <div class="detail-section">
            <div v-if="measureData.implementationStatus === 'COMPLETED'" class="evaluation-content">
              <!-- 整体评估 -->
              <div class="overall-evaluation">
                <h4>整体评估</h4>
                <el-row :gutter="20">
                  <el-col :span="8">
                    <div class="evaluation-item">
                      <div class="evaluation-label">效果评分</div>
                      <div class="evaluation-value">
                        <el-rate
                          v-model="measureData.effectiveness"
                          :max="5"
                          disabled
                          show-score
                          text-color="#ff9900"
                          score-template="{value}分"
                        />
                      </div>
                    </div>
                  </el-col>
                  <el-col :span="8">
                    <div class="evaluation-item">
                      <div class="evaluation-label">预期效果</div>
                      <div class="evaluation-value">
                        <el-tag :type="getExpectedEffectTagType(measureData.expectedEffect)">
                          {{ getExpectedEffectLabel(measureData.expectedEffect) }}
                        </el-tag>
                      </div>
                    </div>
                  </el-col>
                  <el-col :span="8">
                    <div class="evaluation-item">
                      <div class="evaluation-label">实际效果</div>
                      <div class="evaluation-value">
                        <el-tag :type="getActualEffectTagType(measureData.actualEffect)">
                          {{ getActualEffectLabel(measureData.actualEffect) }}
                        </el-tag>
                      </div>
                    </div>
                  </el-col>
                </el-row>
              </div>

              <!-- 效果分析 -->
              <div class="effect-analysis">
                <h4>效果分析</h4>
                <div class="analysis-content">
                  <div class="analysis-item">
                    <label>目标达成情况：</label>
                    <p>{{ measureData.goalAchievement || '暂无评估' }}</p>
                  </div>
                  <div class="analysis-item">
                    <label>主要成果：</label>
                    <p>{{ measureData.mainAchievements || '暂无记录' }}</p>
                  </div>
                  <div class="analysis-item">
                    <label>存在问题：</label>
                    <p>{{ measureData.existingProblems || '暂无记录' }}</p>
                  </div>
                  <div class="analysis-item">
                    <label>改进建议：</label>
                    <p>{{ measureData.improvementSuggestions || '暂无建议' }}</p>
                  </div>
                </div>
              </div>

              <!-- 经验总结 -->
              <div class="lessons-learned">
                <h4>经验总结</h4>
                <p>{{ measureData.lessonsLearned || '暂无总结' }}</p>
              </div>
            </div>
            <div v-else class="no-evaluation">
              <el-empty description="措施尚未完成，暂无效果评估" />
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleExportReport">导出报告</el-button>
      <el-button
        v-if="measureData.implementationStatus === 'PENDING'"
        type="success"
        @click="handleStartImplementation"
      >
        开始实施
      </el-button>
      <el-button
        v-if="measureData.implementationStatus === 'IN_PROGRESS'"
        type="warning"
        @click="handleUpdateProgress"
      >
        更新进度
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { 
  getRiskControlMeasureById, 
  startImplementation,
  exportMeasureReport 
} from '@/api/stateAssets/riskControlMeasure'

export default {
  name: 'RiskControlMeasureDetail',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    measureId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      activeTab: 'basic',
      measureData: {},
      progressRecords: [],
      milestoneRecords: [],
      humanResources: [],
      materialResources: []
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  watch: {
    visible(val) {
      if (val && this.measureId) {
        this.loadMeasureDetail()
      }
    }
  },
  methods: {
    // 加载措施详情
    async loadMeasureDetail() {
      this.loading = true
      try {
        const response = await getRiskControlMeasureById(this.measureId)
        if (response.code === 200) {
          this.measureData = response.data || {}
          // 生成模拟数据
          this.generateMockData()
        }
      } catch (error) {
        this.$message.error('加载详情失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 生成模拟数据
    generateMockData() {
      // 模拟进度记录
      this.progressRecords = [
        {
          updateDate: '2024-01-15',
          previousProgress: 60,
          currentProgress: 75,
          progressChange: 15,
          updateReason: '完成第三阶段工作',
          updateBy: '张三'
        },
        {
          updateDate: '2024-01-10',
          previousProgress: 40,
          currentProgress: 60,
          progressChange: 20,
          updateReason: '完成第二阶段工作',
          updateBy: '李四'
        }
      ]

      // 模拟里程碑记录
      this.milestoneRecords = [
        {
          id: 1,
          milestoneName: '项目启动',
          description: '项目正式启动，团队组建完成',
          achieveDate: '2024-01-01',
          status: 'COMPLETED',
          achieveBy: '项目经理'
        },
        {
          id: 2,
          milestoneName: '需求分析完成',
          description: '完成详细需求分析和方案设计',
          achieveDate: '2024-01-05',
          status: 'COMPLETED',
          achieveBy: '业务分析师'
        }
      ]

      // 模拟人力资源
      this.humanResources = [
        {
          personName: '张三',
          role: '项目经理',
          department: '风险管理部',
          workload: 100,
          startDate: '2024-01-01',
          endDate: '2024-03-31',
          status: 'ACTIVE'
        },
        {
          personName: '李四',
          role: '业务分析师',
          department: '业务部',
          workload: 80,
          startDate: '2024-01-01',
          endDate: '2024-02-28',
          status: 'ACTIVE'
        }
      ]

      // 模拟物质资源
      this.materialResources = [
        {
          resourceName: '风险管理软件',
          resourceType: '软件',
          quantity: 1,
          unit: '套',
          unitCost: 50000,
          totalCost: 50000,
          supplier: 'ABC软件公司',
          status: 'ALLOCATED'
        },
        {
          resourceName: '服务器设备',
          resourceType: '硬件',
          quantity: 2,
          unit: '台',
          unitCost: 15000,
          totalCost: 30000,
          supplier: 'XYZ硬件公司',
          status: 'ALLOCATED'
        }
      ]
    },

    // 开始实施
    async handleStartImplementation() {
      try {
        const response = await startImplementation({
          riskControlMeasureId: this.measureId,
          startBy: this.$store.getters.userInfo.userName,
          actualStartDate: new Date().toISOString().split('T')[0]
        })
        
        if (response.code === 200) {
          this.$message.success('措施实施已开始')
          this.loadMeasureDetail()
          this.$emit('success')
        }
      } catch (error) {
        this.$message.error('开始实施失败：' + error.message)
      }
    },

    // 更新进度
    handleUpdateProgress() {
      this.$emit('update-progress', this.measureData)
    },

    // 导出报告
    async handleExportReport() {
      try {
        const response = await exportMeasureReport({
          riskControlMeasureId: this.measureId
        })
        
        const blob = new Blob([response], { type: 'application/pdf' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `控制措施报告_${this.measureData.measureName}_${new Date().getTime()}.pdf`
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('报告导出成功')
      } catch (error) {
        this.$message.error('导出报告失败：' + error.message)
      }
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.activeTab = 'basic'
      this.measureData = {}
      this.progressRecords = []
      this.milestoneRecords = []
      this.humanResources = []
      this.materialResources = []
    },

    // 工具方法
    calculateDuration(startDate, endDate) {
      if (!startDate || !endDate) return '-'
      const start = new Date(startDate)
      const end = new Date(endDate)
      const diffTime = Math.abs(end - start)
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
      return `${diffDays}天`
    },

    calculateActualDuration(data) {
      if (!data.actualStartDate) return '-'
      const endDate = data.actualEndDate || new Date().toISOString().split('T')[0]
      return this.calculateDuration(data.actualStartDate, endDate)
    },

    formatAmount(amount) {
      if (!amount) return '0'
      return amount.toLocaleString()
    },

    calculateRemainingBudget(data) {
      const budget = data.budgetAmount || 0
      const used = data.usedAmount || 0
      return budget - used
    },

    getRemainingBudgetClass(data) {
      const remaining = this.calculateRemainingBudget(data)
      const budget = data.budgetAmount || 0
      if (budget === 0) return ''
      
      const ratio = remaining / budget
      if (ratio < 0.1) return 'budget-critical'
      if (ratio < 0.3) return 'budget-warning'
      return 'budget-normal'
    },

    getImplementationSteps(stepsJson) {
      if (!stepsJson) return []
      try {
        return typeof stepsJson === 'string' ? JSON.parse(stepsJson) : stepsJson
      } catch (error) {
        return []
      }
    },

    getCurrentStepIndex() {
      const progress = this.measureData.implementationProgress || 0
      const steps = this.getImplementationSteps(this.measureData.implementationSteps)
      if (steps.length === 0) return 0
      
      const stepProgress = 100 / steps.length
      return Math.floor(progress / stepProgress)
    },

    getStepStatus(index) {
      const currentIndex = this.getCurrentStepIndex()
      if (index < currentIndex) return 'finish'
      if (index === currentIndex) return 'process'
      return 'wait'
    },

    getRiskTags(risksString) {
      if (!risksString) return []
      return risksString.split(';').filter(risk => risk.trim())
    },

    getProgressChangeClass(change) {
      if (change > 0) return 'progress-increase'
      if (change < 0) return 'progress-decrease'
      return 'progress-stable'
    },

    // 标签类型和文本方法
    getMeasureTypeTagType(type) {
      const typeMap = {
        'PREVENTIVE': 'primary',
        'CORRECTIVE': 'warning',
        'IMPROVEMENT': 'success',
        'EMERGENCY': 'danger',
        'MONITORING': 'info'
      }
      return typeMap[type] || ''
    },

    getMeasureTypeLabel(type) {
      const labelMap = {
        'PREVENTIVE': '预防措施',
        'CORRECTIVE': '纠正措施',
        'IMPROVEMENT': '改进措施',
        'EMERGENCY': '应急措施',
        'MONITORING': '监控措施'
      }
      return labelMap[type] || type
    },

    getPriorityTagType(priority) {
      const priorityMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'info'
      }
      return priorityMap[priority] || ''
    },

    getPriorityLabel(priority) {
      const labelMap = {
        'HIGH': '高',
        'MEDIUM': '中',
        'LOW': '低'
      }
      return labelMap[priority] || priority
    },

    getImplementationStatusTagType(status) {
      const statusMap = {
        'PENDING': 'info',
        'IN_PROGRESS': 'warning',
        'COMPLETED': 'success',
        'SUSPENDED': 'warning',
        'CANCELLED': 'danger'
      }
      return statusMap[status] || ''
    },

    getImplementationStatusLabel(status) {
      const labelMap = {
        'PENDING': '待实施',
        'IN_PROGRESS': '实施中',
        'COMPLETED': '已完成',
        'SUSPENDED': '已暂停',
        'CANCELLED': '已取消'
      }
      return labelMap[status] || status
    },

    getProgressColor(progress) {
      if (progress >= 80) return '#67c23a'
      if (progress >= 50) return '#e6a23c'
      return '#f56c6c'
    },

    getScheduleStatusTagType(data) {
      // 根据计划和实际时间判断进度状态
      if (data.implementationStatus === 'COMPLETED') {
        if (data.actualEndDate <= data.plannedEndDate) return 'success'
        return 'warning'
      }
      
      if (data.implementationStatus === 'IN_PROGRESS') {
        const today = new Date().toISOString().split('T')[0]
        if (today > data.plannedEndDate) return 'danger'
        return 'warning'
      }
      
      return 'info'
    },

    getScheduleStatusLabel(data) {
      if (data.implementationStatus === 'COMPLETED') {
        if (data.actualEndDate <= data.plannedEndDate) return '按时完成'
        return '延期完成'
      }
      
      if (data.implementationStatus === 'IN_PROGRESS') {
        const today = new Date().toISOString().split('T')[0]
        if (today > data.plannedEndDate) return '进度延期'
        return '进行中'
      }
      
      return '未开始'
    },

    getMonitoringFrequencyLabel(frequency) {
      const labelMap = {
        'DAILY': '每日',
        'WEEKLY': '每周',
        'MONTHLY': '每月',
        'QUARTERLY': '每季度'
      }
      return labelMap[frequency] || frequency
    },

    getReportingFrequencyLabel(frequency) {
      const labelMap = {
        'WEEKLY': '每周',
        'MONTHLY': '每月',
        'QUARTERLY': '每季度',
        'MILESTONE': '里程碑'
      }
      return labelMap[frequency] || frequency
    },

    getMilestoneType(status) {
      return status === 'COMPLETED' ? 'success' : 'primary'
    },

    getMilestoneStatusTagType(status) {
      const statusMap = {
        'COMPLETED': 'success',
        'IN_PROGRESS': 'warning',
        'PENDING': 'info'
      }
      return statusMap[status] || ''
    },

    getMilestoneStatusLabel(status) {
      const labelMap = {
        'COMPLETED': '已完成',
        'IN_PROGRESS': '进行中',
        'PENDING': '待开始'
      }
      return labelMap[status] || status
    },

    getResourceStatusTagType(status) {
      const statusMap = {
        'ALLOCATED': 'success',
        'PENDING': 'warning',
        'RELEASED': 'info'
      }
      return statusMap[status] || ''
    },

    getResourceStatusLabel(status) {
      const labelMap = {
        'ALLOCATED': '已分配',
        'PENDING': '待分配',
        'RELEASED': '已释放'
      }
      return labelMap[status] || status
    },

    getExpectedEffectTagType(effect) {
      const effectMap = {
        'SIGNIFICANT': 'success',
        'OBVIOUS': 'primary',
        'MODERATE': 'warning',
        'SLIGHT': 'info'
      }
      return effectMap[effect] || ''
    },

    getExpectedEffectLabel(effect) {
      const labelMap = {
        'SIGNIFICANT': '显著改善',
        'OBVIOUS': '明显改善',
        'MODERATE': '一般改善',
        'SLIGHT': '轻微改善'
      }
      return labelMap[effect] || effect
    },

    getActualEffectTagType(effect) {
      return this.getExpectedEffectTagType(effect)
    },

    getActualEffectLabel(effect) {
      return this.getExpectedEffectLabel(effect)
    }
  }
}
</script>

<style lang="scss" scoped>
.measure-detail {
  .detail-section {
    padding: 20px;
  }

  .detail-item {
    margin-bottom: 16px;
    
    label {
      font-weight: 600;
      color: #606266;
      margin-right: 8px;
    }
    
    p {
      margin: 8px 0 0 0;
      color: #303133;
      line-height: 1.6;
    }
  }

  .risk-tags {
    margin-top: 8px;

    .risk-tag {
      margin-right: 8px;
      margin-bottom: 8px;
    }
  }

  // 实施计划样式
  .plan-timeline,
  .budget-info,
  .implementation-steps,
  .success-criteria,
  .risk-assessment {
    margin-bottom: 24px;
    padding: 16px;
    background: #f8f9fa;
    border-radius: 6px;

    h4 {
      margin: 0 0 16px 0;
      color: #303133;
      font-size: 16px;
    }
  }

  .timeline-item,
  .budget-item {
    text-align: center;
    padding: 12px;
    background: white;
    border-radius: 4px;

    .timeline-label,
    .budget-label {
      font-size: 12px;
      color: #909399;
      margin-bottom: 8px;
    }

    .timeline-value,
    .budget-value {
      font-size: 16px;
      font-weight: 600;
      color: #303133;

      .amount {
        font-size: 20px;
      }

      .currency {
        font-size: 14px;
        color: #909399;
        margin-left: 4px;
      }
    }
  }

  .budget-critical {
    color: #f56c6c;
  }

  .budget-warning {
    color: #e6a23c;
  }

  .budget-normal {
    color: #67c23a;
  }

  // 状态概览样式
  .status-overview {
    margin-bottom: 24px;

    .status-card {
      background: white;
      border: 1px solid #ebeef5;
      border-radius: 8px;
      padding: 20px;
      text-align: center;

      .status-header {
        display: flex;
        align-items: center;
        justify-content: center;
        margin-bottom: 12px;
        color: #606266;

        i {
          font-size: 18px;
          margin-right: 8px;
        }
      }

      .status-content {
        .progress-display {
          margin-top: 8px;
        }

        .no-rating {
          color: #909399;
          font-size: 14px;
        }
      }
    }
  }

  .time-info,
  .monitoring-config {
    margin-bottom: 24px;
    padding: 16px;
    background: #f8f9fa;
    border-radius: 6px;

    h4 {
      margin: 0 0 16px 0;
      color: #303133;
      font-size: 16px;
    }

    .time-item,
    .config-item {
      margin-bottom: 12px;
      
      label {
        font-weight: 600;
        color: #606266;
        margin-right: 8px;
      }
    }
  }

  // 进度记录样式
  .progress-records,
  .milestone-records {
    margin-bottom: 24px;

    h4 {
      margin: 0 0 16px 0;
      color: #303133;
      font-size: 16px;
    }

    .progress-value {
      font-weight: 600;
      color: #409eff;
    }

    .progress-increase {
      color: #67c23a;
      font-weight: 600;
    }

    .progress-decrease {
      color: #f56c6c;
      font-weight: 600;
    }

    .progress-stable {
      color: #909399;
    }

    .milestone-meta {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 8px;

      .milestone-achiever {
        font-size: 12px;
        color: #909399;
      }
    }
  }

  // 资源管理样式
  .human-resources,
  .material-resources {
    margin-bottom: 24px;

    h4 {
      margin: 0 0 16px 0;
      color: #303133;
      font-size: 16px;
    }

    .cost-value {
      font-weight: 600;
      color: #e6a23c;
    }
  }

  // 效果评估样式
  .evaluation-content {
    .overall-evaluation,
    .effect-analysis,
    .lessons-learned {
      margin-bottom: 24px;
      padding: 16px;
      background: #f8f9fa;
      border-radius: 6px;

      h4 {
        margin: 0 0 16px 0;
        color: #303133;
        font-size: 16px;
      }
    }

    .evaluation-item {
      text-align: center;
      padding: 12px;
      background: white;
      border-radius: 4px;

      .evaluation-label {
        font-size: 12px;
        color: #909399;
        margin-bottom: 8px;
      }

      .evaluation-value {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
      }
    }

    .analysis-content {
      .analysis-item {
        margin-bottom: 16px;
        
        label {
          font-weight: 600;
          color: #606266;
          margin-right: 8px;
        }
        
        p {
          margin: 8px 0 0 0;
          color: #303133;
          line-height: 1.6;
        }
      }
    }
  }

  .no-evaluation {
    text-align: center;
    padding: 40px;
  }
}

.dialog-footer {
  text-align: right;
}
</style>
