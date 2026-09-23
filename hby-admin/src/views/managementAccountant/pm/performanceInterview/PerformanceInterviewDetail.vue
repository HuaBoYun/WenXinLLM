<template>
  <div class="performance-interview-detail">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <el-button icon="el-icon-arrow-left" @click="goBack">返回</el-button>
        <div class="header-title">
          <h2>{{ isEdit ? '编辑绩效面谈' : (isCreate ? '新建绩效面谈' : '绩效面谈详情') }}</h2>
          <p v-if="!isCreate">{{ formData.interviewTitle }}</p>
        </div>
      </div>
      <div class="header-actions">
        <el-button v-if="!isCreate && !isEdit && formData.interviewStatus === 'PLANNED'" @click="handleSchedule" type="primary">
          安排面谈
        </el-button>
        <el-button v-if="!isCreate && !isEdit && formData.interviewStatus === 'SCHEDULED'" @click="handleStart" type="success">
          开始面谈
        </el-button>
        <el-button v-if="!isCreate && !isEdit && formData.interviewStatus === 'ONGOING'" @click="handleComplete" type="warning">
          完成面谈
        </el-button>
        <el-button v-if="!isCreate && !isEdit" @click="handleEdit">编辑</el-button>
        <el-button v-if="isEdit || isCreate" @click="handleSave" type="primary" :loading="saving">
          保存
        </el-button>
        <el-button v-if="isEdit" @click="handleCancel">取消</el-button>
      </div>
    </div>

    <!-- 主要内容 -->
    <div class="main-content">
      <el-tabs v-model="activeTab" type="card">
        <!-- 基本信息 -->
        <el-tab-pane label="基本信息" name="basic">
          <el-form
            ref="interviewForm"
            :model="formData"
            :rules="formRules"
            label-width="120px"
            :disabled="!isEdit && !isCreate"
          >
            <el-card class="form-card">
              <div slot="header">
                <span>面谈基本信息</span>
              </div>
              
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="面谈编码" prop="interviewCode">
                    <el-input v-model="formData.interviewCode" placeholder="系统自动生成" :disabled="!isCreate" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="面谈标题" prop="interviewTitle">
                    <el-input v-model="formData.interviewTitle" placeholder="请输入面谈标题" />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="面谈类型" prop="interviewType">
                    <el-select v-model="formData.interviewType" placeholder="请选择面谈类型" style="width: 100%">
                      <el-option label="年度面谈" value="ANNUAL" />
                      <el-option label="季度面谈" value="QUARTERLY" />
                      <el-option label="月度面谈" value="MONTHLY" />
                      <el-option label="项目面谈" value="PROJECT" />
                      <el-option label="专项面谈" value="SPECIAL" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="面谈状态">
                    <el-tag :type="getStatusTagType(formData.interviewStatus)">
                      {{ formatInterviewStatus(formData.interviewStatus) }}
                    </el-tag>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="被面谈人" prop="intervieweeId">
                    <el-select 
                      v-model="formData.intervieweeId" 
                      placeholder="请选择被面谈人" 
                      style="width: 100%" 
                      filterable
                      @change="handleIntervieweeChange"
                    >
                      <el-option
                        v-for="user in userList"
                        :key="user.userId"
                        :label="user.userName"
                        :value="user.userId"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="面谈官" prop="interviewerId">
                    <el-select 
                      v-model="formData.interviewerId" 
                      placeholder="请选择面谈官" 
                      style="width: 100%" 
                      filterable
                      @change="handleInterviewerChange"
                    >
                      <el-option
                        v-for="user in interviewerList"
                        :key="user.userId"
                        :label="user.userName"
                        :value="user.userId"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item label="面谈年度" prop="interviewYear">
                    <el-date-picker
                      v-model="formData.interviewYear"
                      type="year"
                      placeholder="选择面谈年度"
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="面谈季度" v-if="formData.interviewType === 'QUARTERLY'">
                    <el-select v-model="formData.interviewQuarter" placeholder="选择季度" style="width: 100%">
                      <el-option label="第一季度" :value="1" />
                      <el-option label="第二季度" :value="2" />
                      <el-option label="第三季度" :value="3" />
                      <el-option label="第四季度" :value="4" />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="面谈月份" v-if="formData.interviewType === 'MONTHLY'">
                    <el-select v-model="formData.interviewMonth" placeholder="选择月份" style="width: 100%">
                      <el-option v-for="month in 12" :key="month" :label="`${month}月`" :value="month" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="优先级" prop="priorityLevel">
                    <el-select v-model="formData.priorityLevel" placeholder="请选择优先级" style="width: 100%">
                      <el-option label="高" value="HIGH" />
                      <el-option label="中" value="MEDIUM" />
                      <el-option label="低" value="LOW" />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-form-item label="面谈目的">
                <el-input
                  v-model="formData.interviewPurpose"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入面谈目的"
                />
              </el-form-item>

              <el-form-item label="面谈议程">
                <el-input
                  v-model="formData.interviewAgenda"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入面谈议程"
                />
              </el-form-item>

              <el-form-item label="面谈准备">
                <el-input
                  v-model="formData.interviewPreparation"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入面谈准备事项"
                />
              </el-form-item>
            </el-card>
          </el-form>
        </el-tab-pane>

        <!-- 时间安排 -->
        <el-tab-pane label="时间安排" name="schedule">
          <el-card class="form-card">
            <div slot="header">
              <span>面谈时间安排</span>
            </div>
            
            <el-form :model="formData" label-width="120px" :disabled="!isEdit && !isCreate">
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="计划开始时间">
                    <el-date-picker
                      v-model="formData.plannedStartTime"
                      type="datetime"
                      placeholder="选择开始时间"
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="计划结束时间">
                    <el-date-picker
                      v-model="formData.plannedEndTime"
                      type="datetime"
                      placeholder="选择结束时间"
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="实际开始时间">
                    <el-input :value="formatDateTime(formData.actualStartTime)" disabled />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="实际结束时间">
                    <el-input :value="formatDateTime(formData.actualEndTime)" disabled />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="面谈地点">
                    <el-input v-model="formData.interviewLocation" placeholder="请输入面谈地点" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="面谈方式">
                    <el-select v-model="formData.interviewMethod" placeholder="请选择面谈方式" style="width: 100%">
                      <el-option label="面对面" value="FACE_TO_FACE" />
                      <el-option label="视频" value="VIDEO" />
                      <el-option label="电话" value="PHONE" />
                      <el-option label="在线" value="ONLINE" />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>

              <div class="time-conflict-check" v-if="formData.plannedStartTime && formData.plannedEndTime">
                <el-button @click="checkTimeConflict" size="small" type="primary">检查时间冲突</el-button>
                <span v-if="conflictCheckResult !== null" class="conflict-result">
                  <i :class="conflictCheckResult ? 'el-icon-warning' : 'el-icon-success'" 
                     :style="{ color: conflictCheckResult ? '#f56c6c' : '#67c23a' }"></i>
                  {{ conflictCheckResult ? '存在时间冲突' : '无时间冲突' }}
                </span>
              </div>
            </el-form>
          </el-card>
        </el-tab-pane>

        <!-- 面谈记录 -->
        <el-tab-pane label="面谈记录" name="record" v-if="!isCreate">
          <el-card class="form-card">
            <div slot="header">
              <span>面谈记录与反馈</span>
            </div>
            
            <el-form :model="formData" label-width="120px" :disabled="formData.interviewStatus !== 'ONGOING' && formData.interviewStatus !== 'COMPLETED'">
              <el-form-item label="绩效评分">
                <el-input-number
                  v-model="formData.performanceScore"
                  :min="0"
                  :max="100"
                  :precision="1"
                  style="width: 200px"
                />
                <span style="margin-left: 10px">分</span>
              </el-form-item>

              <el-form-item label="绩效等级">
                <el-select v-model="formData.performanceGrade" placeholder="请选择绩效等级" style="width: 200px">
                  <el-option label="优秀" value="EXCELLENT" />
                  <el-option label="良好" value="GOOD" />
                  <el-option label="一般" value="FAIR" />
                  <el-option label="较差" value="POOR" />
                </el-select>
              </el-form-item>

              <el-form-item label="工作表现总结">
                <el-input
                  v-model="formData.performanceSummary"
                  type="textarea"
                  :rows="4"
                  placeholder="请输入工作表现总结"
                />
              </el-form-item>

              <el-form-item label="主要成就">
                <el-input
                  v-model="formData.keyAchievements"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入主要成就"
                />
              </el-form-item>

              <el-form-item label="改进领域">
                <el-input
                  v-model="formData.improvementAreas"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入需要改进的领域"
                />
              </el-form-item>

              <el-form-item label="发展目标">
                <el-input
                  v-model="formData.developmentGoals"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入发展目标"
                />
              </el-form-item>

              <el-form-item label="培训需求">
                <el-input
                  v-model="formData.trainingNeeds"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入培训需求"
                />
              </el-form-item>

              <el-form-item label="职业规划">
                <el-input
                  v-model="formData.careerPlanning"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入职业规划建议"
                />
              </el-form-item>

              <el-form-item label="面谈记录">
                <el-input
                  v-model="formData.interviewNotes"
                  type="textarea"
                  :rows="5"
                  placeholder="请输入面谈记录"
                />
              </el-form-item>

              <el-form-item label="面谈总结">
                <el-input
                  v-model="formData.interviewSummary"
                  type="textarea"
                  :rows="4"
                  placeholder="请输入面谈总结"
                />
              </el-form-item>
            </el-form>
          </el-card>
        </el-tab-pane>

        <!-- 反馈评价 -->
        <el-tab-pane label="反馈评价" name="feedback" v-if="!isCreate">
          <el-card class="form-card">
            <div slot="header">
              <span>双向反馈与评价</span>
            </div>
            
            <el-form :model="formData" label-width="120px" :disabled="formData.interviewStatus !== 'COMPLETED'">
              <el-form-item label="员工反馈">
                <el-input
                  v-model="formData.employeeFeedback"
                  type="textarea"
                  :rows="4"
                  placeholder="员工对面谈的反馈意见"
                />
              </el-form-item>

              <el-form-item label="管理者反馈">
                <el-input
                  v-model="formData.managerFeedback"
                  type="textarea"
                  :rows="4"
                  placeholder="管理者对面谈的反馈意见"
                />
              </el-form-item>

              <el-form-item label="面谈满意度">
                <el-rate
                  v-model="formData.satisfactionRating"
                  :max="5"
                  show-text
                  :texts="['很不满意', '不满意', '一般', '满意', '很满意']"
                />
              </el-form-item>

              <el-form-item label="效果评价">
                <el-input
                  v-model="formData.effectivenessEvaluation"
                  type="textarea"
                  :rows="3"
                  placeholder="请评价面谈效果"
                />
              </el-form-item>

              <el-form-item label="改进计划">
                <el-input
                  v-model="formData.improvementPlan"
                  type="textarea"
                  :rows="4"
                  placeholder="请输入改进计划"
                />
              </el-form-item>

              <el-form-item label="行动计划">
                <el-input
                  v-model="formData.actionPlan"
                  type="textarea"
                  :rows="4"
                  placeholder="请输入行动计划"
                />
              </el-form-item>
            </el-form>
          </el-card>
        </el-tab-pane>

        <!-- 跟进管理 -->
        <el-tab-pane label="跟进管理" name="followup" v-if="!isCreate">
          <el-card class="form-card">
            <div slot="header">
              <span>跟进计划与管理</span>
            </div>
            
            <el-form :model="formData" label-width="120px">
              <el-form-item label="是否需要跟进">
                <el-switch
                  v-model="needFollowUpSwitch"
                  :disabled="formData.interviewStatus !== 'COMPLETED'"
                  @change="handleFollowUpChange"
                />
              </el-form-item>

              <div v-if="needFollowUpSwitch">
                <el-form-item label="跟进状态">
                  <el-tag :type="getFollowUpStatusTagType(formData.followUpStatus)">
                    {{ formatFollowUpStatus(formData.followUpStatus) }}
                  </el-tag>
                </el-form-item>

                <el-form-item label="跟进计划">
                  <el-input
                    v-model="formData.followUpPlan"
                    type="textarea"
                    :rows="4"
                    placeholder="请输入跟进计划"
                    :disabled="formData.interviewStatus !== 'COMPLETED'"
                  />
                </el-form-item>

                <el-form-item label="跟进截止时间">
                  <el-date-picker
                    v-model="formData.followUpDeadline"
                    type="datetime"
                    placeholder="选择跟进截止时间"
                    style="width: 300px"
                    :disabled="formData.interviewStatus !== 'COMPLETED'"
                  />
                </el-form-item>

                <el-form-item label="下次面谈计划">
                  <el-input
                    v-model="formData.nextInterviewPlan"
                    type="textarea"
                    :rows="3"
                    placeholder="请输入下次面谈计划"
                    :disabled="formData.interviewStatus !== 'COMPLETED'"
                  />
                </el-form-item>

                <el-form-item v-if="formData.interviewStatus === 'COMPLETED'">
                  <el-button type="primary" @click="handleSaveFollowUp">保存跟进计划</el-button>
                  <el-button type="success" @click="handleUpdateFollowUpStatus">更新跟进状态</el-button>
                </el-form-item>
              </div>
            </el-form>
          </el-card>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import {
  getInterviewById,
  createInterview,
  updateInterview,
  scheduleInterview,
  startInterview,
  completeInterview,
  saveInterviewRecord,
  submitFeedback,
  createFollowUpPlan,
  updateFollowUpStatus,
  checkTimeConflict,
  formatInterviewStatus,
  formatInterviewType,
  formatPriorityLevel,
  getStatusTagType
} from '@/api/managementAccountant/pm/performanceInterview'

export default {
  name: 'PerformanceInterviewDetail',
  data() {
    return {
      loading: false,
      saving: false,
      isCreate: false,
      isEdit: false,
      interviewId: null,
      activeTab: 'basic',
      conflictCheckResult: null,
      
      formData: {
        interviewId: null,
        interviewCode: '',
        interviewTitle: '',
        interviewType: '',
        interviewStatus: 'PLANNED',
        intervieweeId: null,
        intervieweeName: '',
        interviewerId: null,
        interviewerName: '',
        interviewYear: null,
        interviewQuarter: null,
        interviewMonth: null,
        plannedStartTime: null,
        plannedEndTime: null,
        actualStartTime: null,
        actualEndTime: null,
        interviewLocation: '',
        interviewMethod: 'FACE_TO_FACE',
        interviewPurpose: '',
        interviewAgenda: '',
        interviewPreparation: '',
        priorityLevel: 'MEDIUM',
        performanceScore: null,
        performanceGrade: '',
        performanceSummary: '',
        keyAchievements: '',
        improvementAreas: '',
        developmentGoals: '',
        trainingNeeds: '',
        careerPlanning: '',
        employeeFeedback: '',
        managerFeedback: '',
        interviewNotes: '',
        interviewSummary: '',
        improvementPlan: '',
        actionPlan: '',
        satisfactionRating: 0,
        effectivenessEvaluation: '',
        needFollowUp: 0,
        followUpStatus: '',
        followUpPlan: '',
        followUpDeadline: null,
        nextInterviewPlan: ''
      },

      formRules: {
        interviewTitle: [
          { required: true, message: '请输入面谈标题', trigger: 'blur' }
        ],
        interviewType: [
          { required: true, message: '请选择面谈类型', trigger: 'change' }
        ],
        intervieweeId: [
          { required: true, message: '请选择被面谈人', trigger: 'change' }
        ],
        interviewerId: [
          { required: true, message: '请选择面谈官', trigger: 'change' }
        ],
        interviewYear: [
          { required: true, message: '请选择面谈年度', trigger: 'change' }
        ],
        priorityLevel: [
          { required: true, message: '请选择优先级', trigger: 'change' }
        ]
      },

      userList: [], // 用户列表
      interviewerList: [] // 面谈官列表
    }
  },

  computed: {
    needFollowUpSwitch: {
      get() {
        return this.formData.needFollowUp === 1
      },
      set(value) {
        this.formData.needFollowUp = value ? 1 : 0
      }
    }
  },

  created() {
    this.initPage()
    this.loadUserList()
    this.loadInterviewerList()
  },

  methods: {
    // 初始化页面
    initPage() {
      const routeParams = this.$route.params
      const routePath = this.$route.path

      if (routePath.includes('/create')) {
        this.isCreate = true
        this.isEdit = true
        this.formData.interviewYear = new Date().getFullYear()
      } else if (routePath.includes('/edit/')) {
        this.isEdit = true
        this.interviewId = routeParams.id
        this.loadData()
      } else if (routePath.includes('/detail/')) {
        this.interviewId = routeParams.id
        this.loadData()
      }
    },

    // 加载数据
    async loadData() {
      if (!this.interviewId) return
      
      this.loading = true
      try {
        const response = await getInterviewById(this.interviewId)
        if (response.success) {
          this.formData = { ...this.formData, ...response.data }
          // 转换日期格式
          if (this.formData.interviewYear) {
            this.formData.interviewYear = new Date(this.formData.interviewYear, 0)
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

    // 加载用户列表
    async loadUserList() {
      // TODO: 实际应该调用用户API获取用户列表
      this.userList = [
        { userId: 1, userName: '张三' },
        { userId: 2, userName: '李四' },
        { userId: 3, userName: '王五' }
      ]
    },

    // 加载面谈官列表
    async loadInterviewerList() {
      // TODO: 实际应该调用API获取面谈官列表
      this.interviewerList = [
        { userId: 10, userName: '面谈官A' },
        { userId: 11, userName: '面谈官B' },
        { userId: 12, userName: '面谈官C' }
      ]
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
        await this.$refs.interviewForm.validate()

        this.saving = true

        // 处理年度数据
        if (this.formData.interviewYear) {
          this.formData.interviewYear = this.formData.interviewYear.getFullYear()
        }

        let response
        if (this.isCreate) {
          response = await createInterview(this.formData)
        } else {
          response = await updateInterview(this.formData)
        }

        if (response.success) {
          this.$message.success(this.isCreate ? '创建成功' : '保存成功')
          
          if (this.isCreate) {
            this.$router.replace(`/pm/performance-interview/detail/${response.data.interviewId}`)
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

    // 安排面谈
    handleSchedule() {
      this.activeTab = 'schedule'
      this.isEdit = true
    },

    // 开始面谈
    async handleStart() {
      try {
        await this.$confirm('确认开始该面谈？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await startInterview(this.interviewId)
        
        if (response.success) {
          this.$message.success('开始成功')
          this.loadData()
          this.activeTab = 'record'
        } else {
          this.$message.error(response.message || '开始失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('开始失败：' + error.message)
        }
      }
    },

    // 完成面谈
    async handleComplete() {
      try {
        await this.$confirm('确认完成该面谈？完成后将保存所有记录。', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const completeData = {
          interviewNotes: this.formData.interviewNotes,
          interviewSummary: this.formData.interviewSummary,
          performanceSummary: this.formData.performanceSummary,
          keyAchievements: this.formData.keyAchievements,
          improvementAreas: this.formData.improvementAreas,
          developmentGoals: this.formData.developmentGoals,
          actionPlan: this.formData.actionPlan,
          satisfactionRating: this.formData.satisfactionRating,
          needFollowUp: this.formData.needFollowUp === 1,
          followUpPlan: this.formData.followUpPlan,
          followUpDeadline: this.formData.followUpDeadline
        }
        
        const response = await completeInterview(this.interviewId, completeData)
        
        if (response.success) {
          this.$message.success('完成成功')
          this.loadData()
          this.activeTab = 'feedback'
        } else {
          this.$message.error(response.message || '完成失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('完成失败：' + error.message)
        }
      }
    },

    // 被面谈人变化
    handleIntervieweeChange(userId) {
      const user = this.userList.find(u => u.userId === userId)
      if (user) {
        this.formData.intervieweeName = user.userName
      }
    },

    // 面谈官变化
    handleInterviewerChange(userId) {
      const user = this.interviewerList.find(u => u.userId === userId)
      if (user) {
        this.formData.interviewerName = user.userName
      }
    },

    // 检查时间冲突
    async checkTimeConflict() {
      try {
        const response = await checkTimeConflict(
          this.formData.interviewerId,
          this.formData.plannedStartTime,
          this.formData.plannedEndTime,
          this.formData.interviewId
        )
        
        if (response.success) {
          this.conflictCheckResult = response.data
        } else {
          this.$message.error(response.message || '检查失败')
        }
      } catch (error) {
        this.$message.error('检查失败：' + error.message)
      }
    },

    // 跟进开关变化
    handleFollowUpChange(value) {
      if (!value) {
        this.formData.followUpStatus = ''
        this.formData.followUpPlan = ''
        this.formData.followUpDeadline = null
        this.formData.nextInterviewPlan = ''
      } else {
        this.formData.followUpStatus = 'PENDING'
      }
    },

    // 保存跟进计划
    async handleSaveFollowUp() {
      try {
        const followUpData = {
          followUpPlan: this.formData.followUpPlan,
          followUpDeadline: this.formData.followUpDeadline,
          nextInterviewPlan: this.formData.nextInterviewPlan
        }
        
        const response = await createFollowUpPlan(this.interviewId, followUpData)
        
        if (response.success) {
          this.$message.success('保存成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      }
    },

    // 更新跟进状态
    async handleUpdateFollowUpStatus() {
      try {
        const { value: status } = await this.$prompt('请选择跟进状态', '更新跟进状态', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputType: 'select',
          inputOptions: [
            { value: 'PENDING', label: '待跟进' },
            { value: 'IN_PROGRESS', label: '跟进中' },
            { value: 'COMPLETED', label: '已完成' }
          ]
        })
        
        const response = await updateFollowUpStatus(this.interviewId, status)
        
        if (response.success) {
          this.$message.success('更新成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '更新失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('更新失败：' + error.message)
        }
      }
    },

    // 格式化方法
    formatInterviewStatus,
    formatInterviewType,
    formatPriorityLevel,
    getStatusTagType,

    formatDateTime(dateTime) {
      if (!dateTime) return '--'
      return this.$moment(dateTime).format('YYYY-MM-DD HH:mm')
    },

    formatFollowUpStatus(status) {
      const statusMap = {
        'PENDING': '待跟进',
        'IN_PROGRESS': '跟进中',
        'COMPLETED': '已完成'
      }
      return statusMap[status] || status
    },

    getFollowUpStatusTagType(status) {
      const tagMap = {
        'PENDING': 'warning',
        'IN_PROGRESS': 'primary',
        'COMPLETED': 'success'
      }
      return tagMap[status] || ''
    }
  }
}
</script>

<style scoped>
.performance-interview-detail {
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
}

.form-card {
  margin-bottom: 20px;
}

.time-conflict-check {
  margin-top: 20px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 4px;
}

.conflict-result {
  margin-left: 15px;
  font-size: 14px;
}
</style>
