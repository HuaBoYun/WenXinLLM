<template>
  <div class="assessment-plan-wizard">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <el-button icon="el-icon-arrow-left" @click="goBack">返回</el-button>
        <div class="header-title">
          <h2>考核方案配置向导</h2>
          <p>通过向导快速创建和配置考核方案</p>
        </div>
      </div>
    </div>

    <!-- 步骤条 -->
    <div class="steps-section">
      <el-steps :active="currentStep" finish-status="success" align-center>
        <el-step title="基本信息" description="设置方案基本信息"></el-step>
        <el-step title="评分配置" description="配置评分规则"></el-step>
        <el-step title="权重设置" description="设置评估权重"></el-step>
        <el-step title="指标选择" description="选择指标体系"></el-step>
        <el-step title="流程配置" description="配置考核流程"></el-step>
        <el-step title="完成配置" description="确认并保存"></el-step>
      </el-steps>
    </div>

    <!-- 主要内容 -->
    <div class="wizard-content">
      <!-- 步骤1: 基本信息 -->
      <div v-show="currentStep === 0" class="step-content">
        <div class="step-header">
          <h3>基本信息配置</h3>
          <p>请填写考核方案的基本信息</p>
        </div>
        
        <el-form ref="basicForm" :model="formData" :rules="basicRules" label-width="120px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="方案名称" prop="planName">
                <el-input v-model="formData.planName" placeholder="请输入方案名称" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="方案类型" prop="planType">
                <el-select v-model="formData.planType" placeholder="请选择方案类型" style="width: 100%">
                  <el-option
                    v-for="item in planTypeOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="考核模式" prop="assessmentMode">
                <el-select v-model="formData.assessmentMode" placeholder="请选择考核模式" style="width: 100%">
                  <el-option
                    v-for="item in assessmentModeOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="考核周期" prop="assessmentCycle">
                <el-select v-model="formData.assessmentCycle" placeholder="请选择考核周期" style="width: 100%">
                  <el-option
                    v-for="item in assessmentCycleOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="考核年度" prop="assessmentYear">
                <el-date-picker
                  v-model="assessmentYearDate"
                  type="year"
                  placeholder="选择考核年度"
                  style="width: 100%"
                  @change="handleYearChange"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="适用范围">
                <el-select v-model="formData.applicableScope" placeholder="请选择适用范围" style="width: 100%">
                  <el-option
                    v-for="item in applicableScopeOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="方案描述">
            <el-input
              v-model="formData.planDescription"
              type="textarea"
              :rows="3"
              placeholder="请输入方案描述"
            />
          </el-form-item>
        </el-form>
      </div>

      <!-- 步骤2: 评分配置 -->
      <div v-show="currentStep === 1" class="step-content">
        <div class="step-header">
          <h3>评分配置</h3>
          <p>设置考核评分规则和等级标准</p>
        </div>

        <el-form ref="scoringForm" :model="formData" label-width="120px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="评分方式">
                <el-select v-model="formData.scoringMethod" placeholder="请选择评分方式" style="width: 100%">
                  <el-option
                    v-for="item in scoringMethodOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="总分">
                <el-input-number v-model="formData.totalScore" :min="1" :max="1000" style="width: 100%" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="及格分数">
                <el-input-number v-model="formData.passScore" :min="0" :max="formData.totalScore" style="width: 100%" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="优秀分数">
                <el-input-number v-model="formData.excellentScore" :min="formData.passScore" :max="formData.totalScore" style="width: 100%" />
              </el-form-item>
            </el-col>
          </el-row>

          <!-- 快速配置模板 -->
          <el-form-item label="快速配置">
            <el-button-group>
              <el-button @click="applyScoringTemplate('standard')">标准配置</el-button>
              <el-button @click="applyScoringTemplate('strict')">严格配置</el-button>
              <el-button @click="applyScoringTemplate('loose')">宽松配置</el-button>
            </el-button-group>
          </el-form-item>

          <!-- 等级预览 -->
          <el-form-item label="等级预览">
            <el-table :data="gradeSettings" border size="small">
              <el-table-column prop="grade" label="等级" width="80" />
              <el-table-column prop="label" label="等级名称" width="100" />
              <el-table-column label="分数范围" width="150">
                <template slot-scope="scope">
                  {{ scope.row.minScore }} - {{ scope.row.maxScore }}
                </template>
              </el-table-column>
              <el-table-column prop="description" label="等级描述" />
            </el-table>
          </el-form-item>
        </el-form>
      </div>

      <!-- 步骤3: 权重设置 -->
      <div v-show="currentStep === 2" class="step-content">
        <div class="step-header">
          <h3>权重设置</h3>
          <p>设置不同评估维度的权重分配</p>
        </div>

        <el-form ref="weightsForm" :model="weightConfig" label-width="150px">
          <!-- 快速配置模板 -->
          <el-form-item label="快速配置">
            <el-button-group>
              <el-button @click="applyWeightTemplate('360')">360度评估</el-button>
              <el-button @click="applyWeightTemplate('superior')">上级主导</el-button>
              <el-button @click="applyWeightTemplate('self')">自评为主</el-button>
              <el-button @click="applyWeightTemplate('peer')">同级评估</el-button>
            </el-button-group>
          </el-form-item>

          <el-form-item label="自评权重(%)">
            <el-slider
              v-model="weightConfig.selfEvaluationWeight"
              :min="0"
              :max="100"
              show-input
              @change="updateWeightChart"
            />
          </el-form-item>

          <el-form-item label="上级评价权重(%)">
            <el-slider
              v-model="weightConfig.superiorEvaluationWeight"
              :min="0"
              :max="100"
              show-input
              @change="updateWeightChart"
            />
          </el-form-item>

          <el-form-item label="同级评价权重(%)">
            <el-slider
              v-model="weightConfig.peerEvaluationWeight"
              :min="0"
              :max="100"
              show-input
              @change="updateWeightChart"
            />
          </el-form-item>

          <el-form-item label="下级评价权重(%)">
            <el-slider
              v-model="weightConfig.subordinateEvaluationWeight"
              :min="0"
              :max="100"
              show-input
              @change="updateWeightChart"
            />
          </el-form-item>

          <el-form-item label="客户评价权重(%)">
            <el-slider
              v-model="weightConfig.customerEvaluationWeight"
              :min="0"
              :max="100"
              show-input
              @change="updateWeightChart"
            />
          </el-form-item>

          <el-form-item>
            <el-alert
              :title="`权重总和: ${totalWeight}%`"
              :type="totalWeight === 100 ? 'success' : 'warning'"
              :description="totalWeight === 100 ? '权重配置正确' : '权重总和应为100%'"
              show-icon
            />
          </el-form-item>

          <!-- 权重分布图 -->
          <el-form-item label="权重分布">
            <div class="weight-chart">
              <div
                v-for="(weight, key) in weightConfig"
                :key="key"
                class="weight-bar"
                :style="{ width: weight + '%' }"
              >
                <span class="weight-label">{{ getWeightLabel(key) }}: {{ weight }}%</span>
              </div>
            </div>
          </el-form-item>
        </el-form>
      </div>

      <!-- 步骤4: 指标选择 -->
      <div v-show="currentStep === 3" class="step-content">
        <div class="step-header">
          <h3>指标选择</h3>
          <p>选择适合的指标体系</p>
        </div>

        <div class="indicator-selection">
          <div class="selection-actions">
            <el-button type="primary" @click="handleSelectIndicators">选择指标体系</el-button>
            <el-button @click="handleCreateIndicators">创建新指标体系</el-button>
          </div>

          <div v-if="formData.indicatorSystemId" class="selected-indicator">
            <el-card>
              <div slot="header">
                <span>已选择指标体系</span>
                <el-button style="float: right; padding: 3px 0" type="text" @click="clearIndicators">清除</el-button>
              </div>
              <el-descriptions :column="2">
                <el-descriptions-item label="指标体系名称">
                  {{ formData.indicatorSystemName }}
                </el-descriptions-item>
                <el-descriptions-item label="指标体系ID">
                  {{ formData.indicatorSystemId }}
                </el-descriptions-item>
              </el-descriptions>
            </el-card>
          </div>

          <div v-else class="empty-indicator">
            <el-empty description="请选择指标体系">
              <el-button type="primary" @click="handleSelectIndicators">选择指标体系</el-button>
            </el-empty>
          </div>
        </div>
      </div>

      <!-- 步骤5: 流程配置 -->
      <div v-show="currentStep === 4" class="step-content">
        <div class="step-header">
          <h3>流程配置</h3>
          <p>配置考核执行流程</p>
        </div>

        <div class="process-selection">
          <div class="selection-actions">
            <el-button type="primary" @click="handleSelectProcess">选择考核流程</el-button>
            <el-button @click="handleCreateProcess">创建新流程</el-button>
          </div>

          <div v-if="formData.processId" class="selected-process">
            <el-card>
              <div slot="header">
                <span>已选择考核流程</span>
                <el-button style="float: right; padding: 3px 0" type="text" @click="clearProcess">清除</el-button>
              </div>
              <el-descriptions :column="2">
                <el-descriptions-item label="流程名称">
                  {{ formData.processName }}
                </el-descriptions-item>
                <el-descriptions-item label="流程ID">
                  {{ formData.processId }}
                </el-descriptions-item>
              </el-descriptions>
            </el-card>
          </div>

          <div v-else class="empty-process">
            <el-empty description="请选择考核流程">
              <el-button type="primary" @click="handleSelectProcess">选择考核流程</el-button>
            </el-empty>
          </div>
        </div>
      </div>

      <!-- 步骤6: 完成配置 -->
      <div v-show="currentStep === 5" class="step-content">
        <div class="step-header">
          <h3>完成配置</h3>
          <p>确认配置信息并保存方案</p>
        </div>

        <div class="summary-section">
          <el-card class="summary-card">
            <div slot="header">
              <span>配置摘要</span>
            </div>
            
            <el-descriptions :column="2" border>
              <el-descriptions-item label="方案名称">{{ formData.planName }}</el-descriptions-item>
              <el-descriptions-item label="方案类型">{{ formatPlanType(formData.planType) }}</el-descriptions-item>
              <el-descriptions-item label="考核模式">{{ formatAssessmentMode(formData.assessmentMode) }}</el-descriptions-item>
              <el-descriptions-item label="考核周期">{{ formatAssessmentCycle(formData.assessmentCycle) }}</el-descriptions-item>
              <el-descriptions-item label="考核年度">{{ formData.assessmentYear }}</el-descriptions-item>
              <el-descriptions-item label="适用范围">{{ formatApplicableScope(formData.applicableScope) }}</el-descriptions-item>
              <el-descriptions-item label="评分方式">{{ formatScoringMethod(formData.scoringMethod) }}</el-descriptions-item>
              <el-descriptions-item label="总分">{{ formData.totalScore }}</el-descriptions-item>
              <el-descriptions-item label="指标体系">{{ formData.indicatorSystemName || '未配置' }}</el-descriptions-item>
              <el-descriptions-item label="考核流程">{{ formData.processName || '未配置' }}</el-descriptions-item>
            </el-descriptions>
          </el-card>

          <!-- 配置验证 -->
          <el-card class="validation-card">
            <div slot="header">
              <span>配置验证</span>
            </div>
            
            <div class="validation-items">
              <div class="validation-item">
                <i class="el-icon-check" style="color: #67c23a"></i>
                <span>基本信息配置完整</span>
              </div>
              <div class="validation-item">
                <i class="el-icon-check" style="color: #67c23a"></i>
                <span>评分规则配置正确</span>
              </div>
              <div class="validation-item">
                <i :class="totalWeight === 100 ? 'el-icon-check' : 'el-icon-warning'" 
                     :style="{ color: totalWeight === 100 ? '#67c23a' : '#e6a23c' }"></i>
                <span>权重配置{{ totalWeight === 100 ? '正确' : '需要调整' }}</span>
              </div>
              <div class="validation-item">
                <i :class="formData.indicatorSystemId ? 'el-icon-check' : 'el-icon-warning'"
                   :style="{ color: formData.indicatorSystemId ? '#67c23a' : '#e6a23c' }"></i>
                <span>指标体系{{ formData.indicatorSystemId ? '已配置' : '未配置' }}</span>
              </div>
              <div class="validation-item">
                <i :class="formData.processId ? 'el-icon-check' : 'el-icon-warning'"
                   :style="{ color: formData.processId ? '#67c23a' : '#e6a23c' }"></i>
                <span>考核流程{{ formData.processId ? '已配置' : '未配置' }}</span>
              </div>
            </div>
          </el-card>
        </div>
      </div>
    </div>

    <!-- 底部操作栏 -->
    <div class="wizard-footer">
      <el-button v-if="currentStep > 0" @click="prevStep">上一步</el-button>
      <el-button v-if="currentStep < 5" type="primary" @click="nextStep">下一步</el-button>
      <el-button v-if="currentStep === 5" type="primary" @click="handleFinish" :loading="saving">
        完成配置
      </el-button>
    </div>

    <!-- 指标选择对话框 -->
    <el-dialog title="选择指标体系" :visible.sync="indicatorDialogVisible" width="800px">
      <div>指标体系选择组件</div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="indicatorDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmSelectIndicators">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 流程选择对话框 -->
    <el-dialog title="选择考核流程" :visible.sync="processDialogVisible" width="800px">
      <div>考核流程选择组件</div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="processDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmSelectProcess">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  createAssessmentPlan,
  assessmentPlanUtils,
  assessmentPlanConstants
} from '@/api/managementAccountant/pm/assessmentPlan'

export default {
  name: 'AssessmentPlanWizard',
  data() {
    return {
      currentStep: 0,
      saving: false,
      assessmentYearDate: null,
      indicatorDialogVisible: false,
      processDialogVisible: false,

      formData: {
        planName: '',
        planType: '',
        assessmentMode: '',
        assessmentCycle: '',
        assessmentYear: new Date().getFullYear(),
        planDescription: '',
        applicableScope: '',
        scoringMethod: 'SCORE',
        totalScore: 100,
        passScore: 60,
        excellentScore: 90,
        indicatorSystemId: null,
        indicatorSystemName: '',
        processId: null,
        processName: '',
        isEnabled: 1,
        isDefault: 0,
        allowAppeal: 1,
        appealDeadline: 7
      },

      weightConfig: {
        selfEvaluationWeight: 20,
        superiorEvaluationWeight: 40,
        peerEvaluationWeight: 20,
        subordinateEvaluationWeight: 10,
        customerEvaluationWeight: 10
      },

      gradeSettings: [
        { grade: 'A', label: '优秀', minScore: 90, maxScore: 100, description: '表现优秀，超额完成目标' },
        { grade: 'B', label: '良好', minScore: 80, maxScore: 89, description: '表现良好，较好完成目标' },
        { grade: 'C', label: '合格', minScore: 70, maxScore: 79, description: '表现合格，基本完成目标' },
        { grade: 'D', label: '基本合格', minScore: 60, maxScore: 69, description: '表现一般，勉强完成目标' },
        { grade: 'E', label: '不合格', minScore: 0, maxScore: 59, description: '表现不佳，未完成目标' }
      ],

      basicRules: {
        planName: [
          { required: true, message: '请输入方案名称', trigger: 'blur' }
        ],
        planType: [
          { required: true, message: '请选择方案类型', trigger: 'change' }
        ],
        assessmentMode: [
          { required: true, message: '请选择考核模式', trigger: 'change' }
        ],
        assessmentCycle: [
          { required: true, message: '请选择考核周期', trigger: 'change' }
        ]
      },

      planTypeOptions: assessmentPlanConstants.PLAN_TYPES,
      assessmentModeOptions: assessmentPlanConstants.ASSESSMENT_MODES,
      assessmentCycleOptions: assessmentPlanConstants.ASSESSMENT_CYCLES,
      scoringMethodOptions: assessmentPlanConstants.SCORING_METHODS,
      applicableScopeOptions: assessmentPlanConstants.APPLICABLE_SCOPES
    }
  },

  computed: {
    totalWeight() {
      return Object.values(this.weightConfig).reduce((sum, weight) => sum + (weight || 0), 0)
    }
  },

  created() {
    // 初始化年度
    this.assessmentYearDate = new Date()
  },

  methods: {
    // 返回
    goBack() {
      this.$router.go(-1)
    },

    // 下一步
    async nextStep() {
      // 验证当前步骤
      if (this.currentStep === 0) {
        try {
          await this.$refs.basicForm.validate()
        } catch (error) {
          return
        }
      } else if (this.currentStep === 2) {
        if (this.totalWeight !== 100) {
          this.$message.error('权重总和必须为100%')
          return
        }
      }

      if (this.currentStep < 5) {
        this.currentStep++
      }
    },

    // 上一步
    prevStep() {
      if (this.currentStep > 0) {
        this.currentStep--
      }
    },

    // 完成配置
    async handleFinish() {
      try {
        // 最终验证
        if (this.totalWeight !== 100) {
          this.$message.error('权重总和必须为100%')
          this.currentStep = 2
          return
        }

        this.saving = true

        // 准备保存数据
        const saveData = {
          ...this.formData,
          weightConfig: JSON.stringify(this.weightConfig),
          gradeSettings: JSON.stringify(this.gradeSettings)
        }

        const response = await createAssessmentPlan(saveData)
        if (response.success) {
          this.$message.success('考核方案创建成功')
          this.$router.push(`/pm/assessment-plan/detail/${response.data.planId}`)
        } else {
          this.$message.error(response.message || '创建失败')
        }
      } catch (error) {
        this.$message.error('创建失败：' + error.message)
      } finally {
        this.saving = false
      }
    },

    // 年度变化
    handleYearChange(date) {
      this.formData.assessmentYear = date ? date.getFullYear() : null
    },

    // 应用评分模板
    applyScoringTemplate(template) {
      switch (template) {
        case 'standard':
          this.formData.totalScore = 100
          this.formData.passScore = 60
          this.formData.excellentScore = 90
          break
        case 'strict':
          this.formData.totalScore = 100
          this.formData.passScore = 70
          this.formData.excellentScore = 95
          break
        case 'loose':
          this.formData.totalScore = 100
          this.formData.passScore = 50
          this.formData.excellentScore = 85
          break
      }
      this.updateGradeSettings()
    },

    // 更新等级设置
    updateGradeSettings() {
      const total = this.formData.totalScore
      const pass = this.formData.passScore
      const excellent = this.formData.excellentScore

      this.gradeSettings = [
        { grade: 'A', label: '优秀', minScore: excellent, maxScore: total, description: '表现优秀，超额完成目标' },
        { grade: 'B', label: '良好', minScore: Math.floor((excellent + pass) / 2), maxScore: excellent - 1, description: '表现良好，较好完成目标' },
        { grade: 'C', label: '合格', minScore: pass, maxScore: Math.floor((excellent + pass) / 2) - 1, description: '表现合格，基本完成目标' },
        { grade: 'D', label: '基本合格', minScore: Math.floor(pass * 0.8), maxScore: pass - 1, description: '表现一般，勉强完成目标' },
        { grade: 'E', label: '不合格', minScore: 0, maxScore: Math.floor(pass * 0.8) - 1, description: '表现不佳，未完成目标' }
      ]
    },

    // 应用权重模板
    applyWeightTemplate(template) {
      switch (template) {
        case '360':
          this.weightConfig = {
            selfEvaluationWeight: 20,
            superiorEvaluationWeight: 40,
            peerEvaluationWeight: 20,
            subordinateEvaluationWeight: 10,
            customerEvaluationWeight: 10
          }
          break
        case 'superior':
          this.weightConfig = {
            selfEvaluationWeight: 10,
            superiorEvaluationWeight: 70,
            peerEvaluationWeight: 10,
            subordinateEvaluationWeight: 5,
            customerEvaluationWeight: 5
          }
          break
        case 'self':
          this.weightConfig = {
            selfEvaluationWeight: 60,
            superiorEvaluationWeight: 25,
            peerEvaluationWeight: 10,
            subordinateEvaluationWeight: 3,
            customerEvaluationWeight: 2
          }
          break
        case 'peer':
          this.weightConfig = {
            selfEvaluationWeight: 15,
            superiorEvaluationWeight: 30,
            peerEvaluationWeight: 40,
            subordinateEvaluationWeight: 10,
            customerEvaluationWeight: 5
          }
          break
      }
      this.updateWeightChart()
    },

    // 更新权重图表
    updateWeightChart() {
      // 这里可以添加图表更新逻辑
    },

    // 获取权重标签
    getWeightLabel(key) {
      const labelMap = {
        selfEvaluationWeight: '自评',
        superiorEvaluationWeight: '上级',
        peerEvaluationWeight: '同级',
        subordinateEvaluationWeight: '下级',
        customerEvaluationWeight: '客户'
      }
      return labelMap[key] || key
    },

    // 选择指标体系
    handleSelectIndicators() {
      this.indicatorDialogVisible = true
    },

    // 创建指标体系
    handleCreateIndicators() {
      this.$message.info('创建指标体系功能开发中')
    },

    // 确认选择指标
    confirmSelectIndicators() {
      this.formData.indicatorSystemId = 1
      this.formData.indicatorSystemName = '示例指标体系'
      this.indicatorDialogVisible = false
    },

    // 清除指标
    clearIndicators() {
      this.formData.indicatorSystemId = null
      this.formData.indicatorSystemName = ''
    },

    // 选择考核流程
    handleSelectProcess() {
      this.processDialogVisible = true
    },

    // 创建考核流程
    handleCreateProcess() {
      this.$message.info('创建考核流程功能开发中')
    },

    // 确认选择流程
    confirmSelectProcess() {
      this.formData.processId = 1
      this.formData.processName = '示例考核流程'
      this.processDialogVisible = false
    },

    // 清除流程
    clearProcess() {
      this.formData.processId = null
      this.formData.processName = ''
    },

    // 格式化方法
    formatPlanType(type) {
      return assessmentPlanUtils.formatPlanType(type)
    },

    formatAssessmentMode(mode) {
      return assessmentPlanUtils.formatAssessmentMode(mode)
    },

    formatAssessmentCycle(cycle) {
      return assessmentPlanUtils.formatAssessmentCycle(cycle)
    },

    formatScoringMethod(method) {
      return assessmentPlanUtils.formatScoringMethod(method)
    },

    formatApplicableScope(scope) {
      return assessmentPlanUtils.formatApplicableScope(scope)
    }
  }
}
</script>

<style scoped>
.assessment-plan-wizard {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e4e7ed;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 15px;
}

.header-title h2 {
  margin: 0 0 5px 0;
  color: #303133;
}

.header-title p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.steps-section {
  margin-bottom: 30px;
  padding: 20px;
  background: white;
  border-radius: 4px;
}

.wizard-content {
  background: white;
  border-radius: 4px;
  min-height: 500px;
}

.step-content {
  padding: 30px;
}

.step-header {
  margin-bottom: 30px;
  text-align: center;
}

.step-header h3 {
  margin: 0 0 10px 0;
  color: #303133;
  font-size: 20px;
}

.step-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.wizard-footer {
  padding: 20px;
  text-align: center;
  background: white;
  border-radius: 4px;
  margin-top: 20px;
}

.weight-chart {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.weight-bar {
  height: 30px;
  background: linear-gradient(90deg, #409eff, #67c23a);
  border-radius: 4px;
  display: flex;
  align-items: center;
  padding: 0 10px;
  min-width: 100px;
}

.weight-label {
  color: white;
  font-size: 12px;
  font-weight: bold;
}

.indicator-selection,
.process-selection {
  text-align: center;
}

.selection-actions {
  margin-bottom: 20px;
}

.selected-indicator,
.selected-process {
  margin-top: 20px;
}

.empty-indicator,
.empty-process {
  padding: 40px 0;
}

.summary-section {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.summary-card,
.validation-card {
  margin-bottom: 20px;
}

.validation-items {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.validation-item {
  display: flex;
  align-items: center;
  gap: 10px;
}
</style>
