<template>
  <div class="assessment-plan-detail">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <el-button icon="el-icon-arrow-left" @click="goBack">返回</el-button>
        <div class="header-title">
          <h2>{{ isEdit ? '编辑考核方案' : (isCreate ? '新建考核方案' : '考核方案详情') }}</h2>
          <p v-if="!isCreate">{{ formData.planName }}</p>
        </div>
      </div>
      <div class="header-actions">
        <el-button v-if="!isCreate && !isEdit" @click="handleEdit">编辑</el-button>
        <el-button v-if="isEdit || isCreate" @click="handleSave" type="primary" :loading="saving">
          保存
        </el-button>
        <el-button v-if="isEdit" @click="handleCancel">取消</el-button>
        <el-dropdown v-if="!isCreate && !isEdit" @command="handleCommand">
          <el-button>
            更多操作<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="copy">复制方案</el-dropdown-item>
            <el-dropdown-item command="activate" v-if="formData.planStatus === 'DRAFT'">激活方案</el-dropdown-item>
            <el-dropdown-item command="pause" v-if="formData.planStatus === 'ACTIVE'">暂停方案</el-dropdown-item>
            <el-dropdown-item command="complete" v-if="formData.planStatus === 'ACTIVE'">完成方案</el-dropdown-item>
            <el-dropdown-item command="validate">验证配置</el-dropdown-item>
            <el-dropdown-item command="preview">预览方案</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>

    <!-- 主要内容 -->
    <div class="main-content">
      <el-tabs v-model="activeTab" type="border-card">
        <!-- 基本信息 -->
        <el-tab-pane label="基本信息" name="basic">
          <div class="tab-content">
            <el-form
              ref="basicForm"
              :model="formData"
              :rules="basicRules"
              label-width="120px"
              :disabled="!isEdit && !isCreate"
            >
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="方案编码" prop="planCode">
                    <el-input v-model="formData.planCode" placeholder="系统自动生成" :disabled="!isCreate" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="方案名称" prop="planName">
                    <el-input v-model="formData.planName" placeholder="请输入方案名称" />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
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
              </el-row>

              <el-row :gutter="20">
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
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="开始时间" prop="startTime">
                    <el-date-picker
                      v-model="formData.startTime"
                      type="datetime"
                      placeholder="选择开始时间"
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="结束时间" prop="endTime">
                    <el-date-picker
                      v-model="formData.endTime"
                      type="datetime"
                      placeholder="选择结束时间"
                      style="width: 100%"
                    />
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

              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item label="是否启用">
                    <el-switch v-model="formData.isEnabled" :active-value="1" :inactive-value="0" />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="是否默认">
                    <el-switch v-model="formData.isDefault" :active-value="1" :inactive-value="0" />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="允许申诉">
                    <el-switch v-model="formData.allowAppeal" :active-value="1" :inactive-value="0" />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20" v-if="formData.allowAppeal">
                <el-col :span="12">
                  <el-form-item label="申诉期限(天)">
                    <el-input-number v-model="formData.appealDeadline" :min="1" :max="30" style="width: 100%" />
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </div>
        </el-tab-pane>

        <!-- 评分配置 -->
        <el-tab-pane label="评分配置" name="scoring">
          <div class="tab-content">
            <el-form
              ref="scoringForm"
              :model="formData"
              label-width="120px"
              :disabled="!isEdit && !isCreate"
            >
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

              <!-- 等级设置 -->
              <el-form-item label="等级设置">
                <el-table :data="gradeSettings" border style="width: 100%">
                  <el-table-column prop="grade" label="等级" width="100" />
                  <el-table-column prop="label" label="等级名称" width="120" />
                  <el-table-column label="分数范围">
                    <template slot-scope="scope">
                      <el-input-number
                        v-model="scope.row.minScore"
                        :min="0"
                        :max="formData.totalScore"
                        size="small"
                        style="width: 80px"
                        :disabled="!isEdit && !isCreate"
                      />
                      <span style="margin: 0 10px">-</span>
                      <el-input-number
                        v-model="scope.row.maxScore"
                        :min="scope.row.minScore"
                        :max="formData.totalScore"
                        size="small"
                        style="width: 80px"
                        :disabled="!isEdit && !isCreate"
                      />
                    </template>
                  </el-table-column>
                  <el-table-column prop="description" label="等级描述">
                    <template slot-scope="scope">
                      <el-input
                        v-model="scope.row.description"
                        size="small"
                        :disabled="!isEdit && !isCreate"
                      />
                    </template>
                  </el-table-column>
                </el-table>
              </el-form-item>
            </el-form>
          </div>
        </el-tab-pane>

        <!-- 权重配置 -->
        <el-tab-pane label="权重配置" name="weights">
          <div class="tab-content">
            <el-form
              ref="weightsForm"
              :model="weightConfig"
              label-width="150px"
              :disabled="!isEdit && !isCreate"
            >
              <el-form-item label="自评权重(%)">
                <el-slider
                  v-model="weightConfig.selfEvaluationWeight"
                  :min="0"
                  :max="100"
                  show-input
                  :disabled="!isEdit && !isCreate"
                />
              </el-form-item>

              <el-form-item label="上级评价权重(%)">
                <el-slider
                  v-model="weightConfig.superiorEvaluationWeight"
                  :min="0"
                  :max="100"
                  show-input
                  :disabled="!isEdit && !isCreate"
                />
              </el-form-item>

              <el-form-item label="同级评价权重(%)">
                <el-slider
                  v-model="weightConfig.peerEvaluationWeight"
                  :min="0"
                  :max="100"
                  show-input
                  :disabled="!isEdit && !isCreate"
                />
              </el-form-item>

              <el-form-item label="下级评价权重(%)">
                <el-slider
                  v-model="weightConfig.subordinateEvaluationWeight"
                  :min="0"
                  :max="100"
                  show-input
                  :disabled="!isEdit && !isCreate"
                />
              </el-form-item>

              <el-form-item label="客户评价权重(%)">
                <el-slider
                  v-model="weightConfig.customerEvaluationWeight"
                  :min="0"
                  :max="100"
                  show-input
                  :disabled="!isEdit && !isCreate"
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
            </el-form>
          </div>
        </el-tab-pane>

        <!-- 指标配置 -->
        <el-tab-pane label="指标配置" name="indicators">
          <div class="tab-content">
            <div class="indicator-section">
              <div class="section-header">
                <h3>指标体系配置</h3>
                <el-button v-if="isEdit || isCreate" type="primary" size="small" @click="handleSelectIndicators">
                  选择指标体系
                </el-button>
              </div>

              <div v-if="formData.indicatorSystemId" class="indicator-info">
                <el-descriptions :column="2" border>
                  <el-descriptions-item label="指标体系名称">
                    {{ formData.indicatorSystemName }}
                  </el-descriptions-item>
                  <el-descriptions-item label="指标体系ID">
                    {{ formData.indicatorSystemId }}
                  </el-descriptions-item>
                </el-descriptions>
              </div>

              <div v-else class="empty-indicator">
                <el-empty description="暂未配置指标体系" />
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 流程配置 -->
        <el-tab-pane label="流程配置" name="process">
          <div class="tab-content">
            <div class="process-section">
              <div class="section-header">
                <h3>考核流程配置</h3>
                <el-button v-if="isEdit || isCreate" type="primary" size="small" @click="handleSelectProcess">
                  选择考核流程
                </el-button>
              </div>

              <div v-if="formData.processId" class="process-info">
                <el-descriptions :column="2" border>
                  <el-descriptions-item label="流程名称">
                    {{ formData.processName }}
                  </el-descriptions-item>
                  <el-descriptions-item label="流程ID">
                    {{ formData.processId }}
                  </el-descriptions-item>
                </el-descriptions>
              </div>

              <div v-else class="empty-process">
                <el-empty description="暂未配置考核流程" />
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 适用范围 -->
        <el-tab-pane label="适用范围" name="scope">
          <div class="tab-content">
            <el-form
              ref="scopeForm"
              :model="formData"
              label-width="120px"
              :disabled="!isEdit && !isCreate"
            >
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

              <el-form-item label="适用对象">
                <el-input
                  v-model="formData.applicableTargets"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入适用对象描述"
                />
              </el-form-item>

              <el-form-item label="排除对象">
                <el-input
                  v-model="formData.excludeTargets"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入排除对象描述"
                />
              </el-form-item>
            </el-form>
          </div>
        </el-tab-pane>
      </el-tabs>
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
  getAssessmentPlanById,
  createAssessmentPlan,
  updateAssessmentPlan,
  copyAssessmentPlan,
  activateAssessmentPlan,
  pauseAssessmentPlan,
  completeAssessmentPlan,
  validateAssessmentPlan,
  previewAssessmentPlan,
  assessmentPlanUtils,
  assessmentPlanConstants
} from '@/api/managementAccountant/pm/assessmentPlan'

export default {
  name: 'AssessmentPlanDetail',
  data() {
    return {
      loading: false,
      saving: false,
      activeTab: 'basic',
      isCreate: false,
      isEdit: false,
      planId: null,
      assessmentYearDate: null,
      indicatorDialogVisible: false,
      processDialogVisible: false,
      
      formData: {
        planId: null,
        planCode: '',
        planName: '',
        planType: '',
        assessmentMode: '',
        assessmentCycle: '',
        assessmentYear: null,
        startTime: null,
        endTime: null,
        planDescription: '',
        isEnabled: 1,
        isDefault: 0,
        allowAppeal: 1,
        appealDeadline: 7,
        scoringMethod: 'SCORE',
        totalScore: 100,
        passScore: 60,
        excellentScore: 90,
        indicatorSystemId: null,
        indicatorSystemName: '',
        processId: null,
        processName: '',
        applicableScope: '',
        applicableTargets: '',
        excludeTargets: ''
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
    this.initPage()
  },

  methods: {
    // 初始化页面
    initPage() {
      const { mode, id } = this.$route.params
      
      if (mode === 'create') {
        this.isCreate = true
        this.isEdit = true
      } else if (mode === 'edit') {
        this.isEdit = true
        this.planId = id
        this.loadData()
      } else {
        this.planId = id
        this.loadData()
      }
    },

    // 加载数据
    async loadData() {
      if (!this.planId) return
      
      this.loading = true
      try {
        const response = await getAssessmentPlanById(this.planId)
        if (response.success) {
          this.formData = { ...this.formData, ...response.data }
          
          // 处理年度日期
          if (this.formData.assessmentYear) {
            this.assessmentYearDate = new Date(this.formData.assessmentYear, 0, 1)
          }
          
          // 处理权重配置
          if (this.formData.weightConfig) {
            try {
              const weights = JSON.parse(this.formData.weightConfig)
              this.weightConfig = { ...this.weightConfig, ...weights }
            } catch (e) {
              console.warn('权重配置解析失败:', e)
            }
          }
        } else {
          this.$message.error(response.message || '加载失败')
        }
      } catch (error) {
        this.$message.error('加载失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 返回
    goBack() {
      this.$router.go(-1)
    },

    // 编辑
    handleEdit() {
      this.isEdit = true
    },

    // 保存
    async handleSave() {
      try {
        // 验证表单
        await this.$refs.basicForm.validate()
        
        // 验证权重
        if (this.totalWeight !== 100) {
          this.$message.error('权重总和必须为100%')
          this.activeTab = 'weights'
          return
        }

        this.saving = true
        
        // 准备保存数据
        const saveData = {
          ...this.formData,
          weightConfig: JSON.stringify(this.weightConfig),
          gradeSettings: JSON.stringify(this.gradeSettings)
        }

        let response
        if (this.isCreate) {
          response = await createAssessmentPlan(saveData)
        } else {
          response = await updateAssessmentPlan(saveData)
        }

        if (response.success) {
          this.$message.success(this.isCreate ? '创建成功' : '保存成功')
          
          if (this.isCreate) {
            this.$router.replace(`/pm/assessment-plan/detail/${response.data.planId}`)
          } else {
            this.isEdit = false
            this.loadData()
          }
        } else {
          this.$message.error(response.message || '保存失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('保存失败：' + error.message)
        }
      } finally {
        this.saving = false
      }
    },

    // 取消
    handleCancel() {
      this.isEdit = false
      if (this.isCreate) {
        this.goBack()
      } else {
        this.loadData()
      }
    },

    // 下拉菜单操作
    async handleCommand(command) {
      switch (command) {
        case 'copy':
          await this.handleCopy()
          break
        case 'activate':
          await this.handleActivate()
          break
        case 'pause':
          await this.handlePause()
          break
        case 'complete':
          await this.handleComplete()
          break
        case 'validate':
          await this.handleValidate()
          break
        case 'preview':
          await this.handlePreview()
          break
      }
    },

    // 复制
    async handleCopy() {
      try {
        const copyParams = {
          newPlanName: this.formData.planName + '_副本',
          copyIndicators: true,
          copyProcess: true,
          copyWeights: true
        }
        
        const response = await copyAssessmentPlan(this.planId, copyParams)
        if (response.success) {
          this.$message.success('复制成功')
          this.$router.push(`/pm/assessment-plan/detail/${response.data.planId}`)
        } else {
          this.$message.error(response.message || '复制失败')
        }
      } catch (error) {
        this.$message.error('复制失败：' + error.message)
      }
    },

    // 激活
    async handleActivate() {
      try {
        await this.$confirm('确认激活该考核方案？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await activateAssessmentPlan(this.planId)
        if (response.success) {
          this.$message.success('激活成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '激活失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('激活失败：' + error.message)
        }
      }
    },

    // 暂停
    async handlePause() {
      try {
        await this.$confirm('确认暂停该考核方案？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await pauseAssessmentPlan(this.planId)
        if (response.success) {
          this.$message.success('暂停成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '暂停失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('暂停失败：' + error.message)
        }
      }
    },

    // 完成
    async handleComplete() {
      try {
        await this.$confirm('确认完成该考核方案？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await completeAssessmentPlan(this.planId)
        if (response.success) {
          this.$message.success('完成成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '完成失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('完成失败：' + error.message)
        }
      }
    },

    // 验证配置
    async handleValidate() {
      try {
        const response = await validateAssessmentPlan(this.planId)
        if (response.success) {
          const result = response.data
          if (result.isValid) {
            this.$message.success('配置验证通过')
          } else {
            this.$alert(result.errors.join('\n'), '配置验证失败', {
              confirmButtonText: '确定',
              type: 'warning'
            })
          }
        } else {
          this.$message.error(response.message || '验证失败')
        }
      } catch (error) {
        this.$message.error('验证失败：' + error.message)
      }
    },

    // 预览方案
    async handlePreview() {
      try {
        const response = await previewAssessmentPlan(this.planId)
        if (response.success) {
          // 这里可以打开预览对话框或新窗口
          this.$message.success('预览功能开发中')
        } else {
          this.$message.error(response.message || '预览失败')
        }
      } catch (error) {
        this.$message.error('预览失败：' + error.message)
      }
    },

    // 年度变化
    handleYearChange(date) {
      this.formData.assessmentYear = date ? date.getFullYear() : null
    },

    // 选择指标体系
    handleSelectIndicators() {
      this.indicatorDialogVisible = true
    },

    // 确认选择指标
    confirmSelectIndicators() {
      // 这里应该从指标选择组件获取选中的指标体系
      this.formData.indicatorSystemId = 1
      this.formData.indicatorSystemName = '示例指标体系'
      this.indicatorDialogVisible = false
    },

    // 选择考核流程
    handleSelectProcess() {
      this.processDialogVisible = true
    },

    // 确认选择流程
    confirmSelectProcess() {
      // 这里应该从流程选择组件获取选中的流程
      this.formData.processId = 1
      this.formData.processName = '示例考核流程'
      this.processDialogVisible = false
    }
  }
}
</script>

<style scoped>
.assessment-plan-detail {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
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

.main-content {
  background: white;
  border-radius: 4px;
  overflow: hidden;
}

.tab-content {
  padding: 20px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h3 {
  margin: 0;
  color: #303133;
}

.indicator-info,
.process-info {
  margin-top: 20px;
}

.empty-indicator,
.empty-process {
  text-align: center;
  padding: 40px 0;
}
</style>
