<template>
  <div class="assessment-360-detail">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <el-button icon="el-icon-arrow-left" @click="goBack">返回</el-button>
        <div class="header-title">
          <h2>{{ isEdit ? '编辑360度评估' : (isCreate ? '新建360度评估' : '360度评估详情') }}</h2>
          <p v-if="!isCreate">{{ formData.assessmentName }}</p>
        </div>
      </div>
      <div class="header-actions">
        <el-button v-if="!isCreate && !isEdit && formData.assessmentStatus === 'DRAFT'" @click="handleStart" type="success">
          启动评估
        </el-button>
        <el-button v-if="!isCreate && !isEdit && formData.assessmentStatus === 'ONGOING'" @click="handleComplete" type="warning">
          完成评估
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
            ref="assessmentForm"
            :model="formData"
            :rules="formRules"
            label-width="120px"
            :disabled="!isEdit && !isCreate"
          >
            <el-card class="form-card">
              <div slot="header">
                <span>评估基本信息</span>
              </div>
              
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="评估编码" prop="assessmentCode">
                    <el-input v-model="formData.assessmentCode" placeholder="系统自动生成" :disabled="!isCreate" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="评估名称" prop="assessmentName">
                    <el-input v-model="formData.assessmentName" placeholder="请输入评估名称" />
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="被评估人" prop="assessedUserId">
                    <el-select v-model="formData.assessedUserId" placeholder="请选择被评估人" style="width: 100%" filterable>
                      <el-option
                        v-for="user in userList"
                        :key="user.userId"
                        :label="user.userName"
                        :value="user.userId"
                        @click="handleUserSelect(user)"
                      />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="评估类型" prop="assessmentType">
                    <el-select v-model="formData.assessmentType" placeholder="请选择评估类型" style="width: 100%">
                      <el-option label="年度评估" value="ANNUAL" />
                      <el-option label="季度评估" value="QUARTERLY" />
                      <el-option label="月度评估" value="MONTHLY" />
                      <el-option label="项目评估" value="PROJECT" />
                    </el-select>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row :gutter="20">
                <el-col :span="8">
                  <el-form-item label="评估年度" prop="assessmentYear">
                    <el-date-picker
                      v-model="formData.assessmentYear"
                      type="year"
                      placeholder="选择评估年度"
                      style="width: 100%"
                    />
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="评估季度" v-if="formData.assessmentType === 'QUARTERLY'">
                    <el-select v-model="formData.assessmentQuarter" placeholder="选择季度" style="width: 100%">
                      <el-option label="第一季度" :value="1" />
                      <el-option label="第二季度" :value="2" />
                      <el-option label="第三季度" :value="3" />
                      <el-option label="第四季度" :value="4" />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="评估月份" v-if="formData.assessmentType === 'MONTHLY'">
                    <el-select v-model="formData.assessmentMonth" placeholder="选择月份" style="width: 100%">
                      <el-option v-for="month in 12" :key="month" :label="`${month}月`" :value="month" />
                    </el-select>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="评估状态">
                    <el-tag :type="getStatusTagType(formData.assessmentStatus)">
                      {{ formatAssessmentStatus(formData.assessmentStatus) }}
                    </el-tag>
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

              <el-form-item label="评估描述">
                <el-input
                  v-model="formData.assessmentDescription"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入评估描述"
                />
              </el-form-item>

              <el-form-item label="评估目标">
                <el-input
                  v-model="formData.assessmentObjectives"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入评估目标"
                />
              </el-form-item>
            </el-card>
          </el-form>
        </el-tab-pane>

        <!-- 权重配置 -->
        <el-tab-pane label="权重配置" name="weight">
          <el-card class="form-card">
            <div slot="header">
              <span>评估维度权重配置</span>
              <el-button style="float: right; margin-top: -5px;" size="small" @click="resetWeights">
                重置权重
              </el-button>
            </div>
            
            <div class="weight-config">
              <div class="weight-item">
                <div class="weight-label">
                  <span>自评权重</span>
                  <span class="weight-value">{{ (formData.selfWeight * 100).toFixed(1) }}%</span>
                </div>
                <el-slider
                  v-model="formData.selfWeight"
                  :min="0"
                  :max="1"
                  :step="0.01"
                  @change="handleWeightChange"
                />
              </div>

              <div class="weight-item">
                <div class="weight-label">
                  <span>上级评价权重</span>
                  <span class="weight-value">{{ (formData.superiorWeight * 100).toFixed(1) }}%</span>
                </div>
                <el-slider
                  v-model="formData.superiorWeight"
                  :min="0"
                  :max="1"
                  :step="0.01"
                  @change="handleWeightChange"
                />
              </div>

              <div class="weight-item">
                <div class="weight-label">
                  <span>同级评价权重</span>
                  <span class="weight-value">{{ (formData.peerWeight * 100).toFixed(1) }}%</span>
                </div>
                <el-slider
                  v-model="formData.peerWeight"
                  :min="0"
                  :max="1"
                  :step="0.01"
                  @change="handleWeightChange"
                />
              </div>

              <div class="weight-item">
                <div class="weight-label">
                  <span>下级评价权重</span>
                  <span class="weight-value">{{ (formData.subordinateWeight * 100).toFixed(1) }}%</span>
                </div>
                <el-slider
                  v-model="formData.subordinateWeight"
                  :min="0"
                  :max="1"
                  :step="0.01"
                  @change="handleWeightChange"
                />
              </div>

              <div class="weight-item">
                <div class="weight-label">
                  <span>客户评价权重</span>
                  <span class="weight-value">{{ (formData.customerWeight * 100).toFixed(1) }}%</span>
                </div>
                <el-slider
                  v-model="formData.customerWeight"
                  :min="0"
                  :max="1"
                  :step="0.01"
                  @change="handleWeightChange"
                />
              </div>

              <div class="weight-summary">
                <el-alert
                  :title="`权重总和: ${totalWeight.toFixed(1)}%`"
                  :type="totalWeight === 100 ? 'success' : 'warning'"
                  :description="totalWeight === 100 ? '权重配置正确' : '权重总和必须等于100%'"
                  show-icon
                  :closable="false"
                />
              </div>

              <div class="weight-templates">
                <h4>快速模板</h4>
                <el-button-group>
                  <el-button size="small" @click="applyWeightTemplate('standard')">标准模式</el-button>
                  <el-button size="small" @click="applyWeightTemplate('superior')">上级主导</el-button>
                  <el-button size="small" @click="applyWeightTemplate('self')">自评为主</el-button>
                  <el-button size="small" @click="applyWeightTemplate('360')">360度均衡</el-button>
                </el-button-group>
              </div>
            </div>
          </el-card>
        </el-tab-pane>

        <!-- 评估进度 -->
        <el-tab-pane label="评估进度" name="progress" v-if="!isCreate">
          <el-card class="form-card">
            <div slot="header">
              <span>评估执行进度</span>
            </div>
            
            <div class="progress-overview">
              <el-row :gutter="20">
                <el-col :span="8">
                  <div class="progress-item">
                    <div class="progress-label">整体进度</div>
                    <el-progress :percentage="formData.completionRate || 0" :stroke-width="8" />
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="progress-item">
                    <div class="progress-label">参与人数</div>
                    <div class="progress-number">
                      {{ formData.completedCount || 0 }} / {{ formData.participantCount || 0 }}
                    </div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="progress-item">
                    <div class="progress-label">综合得分</div>
                    <div class="progress-score" :style="{ color: getScoreColor(formData.weightedAverageScore) }">
                      {{ formData.weightedAverageScore || '--' }}
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>

            <div class="dimension-progress">
              <h4>各维度评估进度</h4>
              <div class="dimension-item">
                <div class="dimension-header">
                  <span>自评</span>
                  <el-tag :type="formData.selfScore ? 'success' : 'info'" size="mini">
                    {{ formData.selfScore ? '已完成' : '待评估' }}
                  </el-tag>
                </div>
                <div class="dimension-score">
                  {{ formData.selfScore || '--' }}
                </div>
              </div>

              <div class="dimension-item">
                <div class="dimension-header">
                  <span>上级评价</span>
                  <el-tag :type="formData.superiorScore ? 'success' : 'info'" size="mini">
                    {{ formData.superiorScore ? '已完成' : '待评估' }}
                  </el-tag>
                </div>
                <div class="dimension-score">
                  {{ formData.superiorScore || '--' }}
                </div>
              </div>

              <div class="dimension-item">
                <div class="dimension-header">
                  <span>同级评价</span>
                  <el-tag :type="formData.peerScore ? 'success' : 'info'" size="mini">
                    {{ formData.peerScore ? '已完成' : '待评估' }}
                  </el-tag>
                </div>
                <div class="dimension-score">
                  {{ formData.peerScore || '--' }}
                </div>
              </div>

              <div class="dimension-item">
                <div class="dimension-header">
                  <span>下级评价</span>
                  <el-tag :type="formData.subordinateScore ? 'success' : 'info'" size="mini">
                    {{ formData.subordinateScore ? '已完成' : '待评估' }}
                  </el-tag>
                </div>
                <div class="dimension-score">
                  {{ formData.subordinateScore || '--' }}
                </div>
              </div>

              <div class="dimension-item">
                <div class="dimension-header">
                  <span>客户评价</span>
                  <el-tag :type="formData.customerScore ? 'success' : 'info'" size="mini">
                    {{ formData.customerScore ? '已完成' : '待评估' }}
                  </el-tag>
                </div>
                <div class="dimension-score">
                  {{ formData.customerScore || '--' }}
                </div>
              </div>
            </div>
          </el-card>
        </el-tab-pane>

        <!-- 评估结果 -->
        <el-tab-pane label="评估结果" name="result" v-if="!isCreate && formData.assessmentStatus === 'COMPLETED'">
          <el-card class="form-card">
            <div slot="header">
              <span>评估结果分析</span>
              <el-button style="float: right; margin-top: -5px;" size="small" type="primary" @click="generateReport">
                生成报告
              </el-button>
            </div>
            
            <div class="result-summary">
              <el-row :gutter="20">
                <el-col :span="6">
                  <div class="result-item">
                    <div class="result-label">综合得分</div>
                    <div class="result-value" :style="{ color: getScoreColor(formData.weightedAverageScore) }">
                      {{ formData.weightedAverageScore || '--' }}
                    </div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="result-item">
                    <div class="result-label">评估等级</div>
                    <el-tag :type="getGradeTagType(formData.assessmentGrade)" size="large">
                      {{ formatAssessmentGrade(formData.assessmentGrade) }}
                    </el-tag>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="result-item">
                    <div class="result-label">总分</div>
                    <div class="result-value">
                      {{ formData.totalScore || '--' }}
                    </div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="result-item">
                    <div class="result-label">排名</div>
                    <div class="result-value">
                      {{ formData.assessmentRank || '--' }}
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>

            <div class="result-chart">
              <h4>维度得分雷达图</h4>
              <div id="radarChart" style="width: 100%; height: 400px;"></div>
            </div>
          </el-card>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import {
  get360AssessmentById,
  create360Assessment,
  update360Assessment,
  startAssessment,
  completeAssessment,
  generateAssessmentReport
} from '@/api/managementAccountant/pm/assessment360'

export default {
  name: 'Assessment360Detail',
  data() {
    return {
      loading: false,
      saving: false,
      isCreate: false,
      isEdit: false,
      assessmentId: null,
      activeTab: 'basic',
      
      formData: {
        assessmentId: null,
        assessmentCode: '',
        assessmentName: '',
        assessedUserId: null,
        assessedUserName: '',
        assessmentType: '',
        assessmentStatus: 'DRAFT',
        assessmentYear: null,
        assessmentQuarter: null,
        assessmentMonth: null,
        startTime: null,
        endTime: null,
        assessmentDescription: '',
        assessmentObjectives: '',
        selfWeight: 0.2,
        superiorWeight: 0.4,
        peerWeight: 0.2,
        subordinateWeight: 0.1,
        customerWeight: 0.1,
        selfScore: null,
        superiorScore: null,
        peerScore: null,
        subordinateScore: null,
        customerScore: null,
        totalScore: null,
        weightedAverageScore: null,
        assessmentGrade: null,
        assessmentRank: null,
        completionRate: 0,
        participantCount: 0,
        completedCount: 0
      },

      formRules: {
        assessmentName: [
          { required: true, message: '请输入评估名称', trigger: 'blur' }
        ],
        assessedUserId: [
          { required: true, message: '请选择被评估人', trigger: 'change' }
        ],
        assessmentType: [
          { required: true, message: '请选择评估类型', trigger: 'change' }
        ],
        startTime: [
          { required: true, message: '请选择开始时间', trigger: 'change' }
        ],
        endTime: [
          { required: true, message: '请选择结束时间', trigger: 'change' }
        ]
      },

      userList: [] // 用户列表，实际应该从API获取
    }
  },

  computed: {
    // 权重总和
    totalWeight() {
      return (this.formData.selfWeight + this.formData.superiorWeight + 
              this.formData.peerWeight + this.formData.subordinateWeight + 
              this.formData.customerWeight) * 100
    }
  },

  created() {
    this.initPage()
    this.loadUserList()
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
        this.assessmentId = id
        this.loadData()
      } else {
        this.assessmentId = id
        this.loadData()
      }
    },

    // 加载数据
    async loadData() {
      if (!this.assessmentId) return
      
      this.loading = true
      try {
        const response = await get360AssessmentById(this.assessmentId)
        if (response.success) {
          this.formData = { ...this.formData, ...response.data }
          // 转换日期格式
          if (this.formData.assessmentYear) {
            this.formData.assessmentYear = new Date(this.formData.assessmentYear, 0)
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
        await this.$refs.assessmentForm.validate()

        // 验证权重总和
        if (Math.abs(this.totalWeight - 100) > 0.1) {
          this.$message.error('权重总和必须等于100%')
          this.activeTab = 'weight'
          return
        }

        this.saving = true

        // 处理年度数据
        if (this.formData.assessmentYear) {
          this.formData.assessmentYear = this.formData.assessmentYear.getFullYear()
        }

        let response
        if (this.isCreate) {
          response = await create360Assessment(this.formData)
        } else {
          response = await update360Assessment(this.formData)
        }

        if (response.success) {
          this.$message.success(this.isCreate ? '创建成功' : '保存成功')
          
          if (this.isCreate) {
            this.$router.replace(`/pm/360-assessment/detail/${response.data.assessmentId}`)
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

    // 启动评估
    async handleStart() {
      try {
        await this.$confirm('确认启动该评估？启动后将通知相关评估人员。', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await startAssessment(this.assessmentId, {
          startReason: '评估启动',
          notifyParticipants: true
        })
        
        if (response.success) {
          this.$message.success('启动成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '启动失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('启动失败：' + error.message)
        }
      }
    },

    // 完成评估
    async handleComplete() {
      try {
        await this.$confirm('确认完成该评估？完成后将计算最终结果。', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await completeAssessment(this.assessmentId, {
          completeReason: '评估完成',
          generateReport: true
        })
        
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

    // 用户选择
    handleUserSelect(user) {
      this.formData.assessedUserName = user.userName
    },

    // 权重变化处理
    handleWeightChange() {
      // 可以在这里添加权重变化的逻辑
    },

    // 重置权重
    resetWeights() {
      this.formData.selfWeight = 0.2
      this.formData.superiorWeight = 0.4
      this.formData.peerWeight = 0.2
      this.formData.subordinateWeight = 0.1
      this.formData.customerWeight = 0.1
    },

    // 应用权重模板
    applyWeightTemplate(template) {
      switch (template) {
        case 'standard':
          this.formData.selfWeight = 0.2
          this.formData.superiorWeight = 0.4
          this.formData.peerWeight = 0.2
          this.formData.subordinateWeight = 0.1
          this.formData.customerWeight = 0.1
          break
        case 'superior':
          this.formData.selfWeight = 0.1
          this.formData.superiorWeight = 0.6
          this.formData.peerWeight = 0.15
          this.formData.subordinateWeight = 0.1
          this.formData.customerWeight = 0.05
          break
        case 'self':
          this.formData.selfWeight = 0.5
          this.formData.superiorWeight = 0.3
          this.formData.peerWeight = 0.1
          this.formData.subordinateWeight = 0.05
          this.formData.customerWeight = 0.05
          break
        case '360':
          this.formData.selfWeight = 0.2
          this.formData.superiorWeight = 0.2
          this.formData.peerWeight = 0.2
          this.formData.subordinateWeight = 0.2
          this.formData.customerWeight = 0.2
          break
      }
    },

    // 生成报告
    async handleGenerateReport() {
      try {
        const response = await generateAssessmentReport(this.assessmentId, {
          reportType: 'COMPREHENSIVE',
          includeCharts: true
        })
        
        if (response.success) {
          this.$message.success('报告生成成功')
          // 这里可以打开报告页面或下载报告
        } else {
          this.$message.error(response.message || '报告生成失败')
        }
      } catch (error) {
        this.$message.error('报告生成失败：' + error.message)
      }
    },

    // 格式化方法
    formatAssessmentStatus(status) {
      const statusMap = {
        'DRAFT': '草稿',
        'ONGOING': '进行中',
        'COMPLETED': '已完成',
        'CANCELLED': '已取消'
      }
      return statusMap[status] || status
    },

    formatAssessmentGrade(grade) {
      const gradeMap = {
        'EXCELLENT': '优秀',
        'GOOD': '良好',
        'FAIR': '一般',
        'POOR': '较差'
      }
      return gradeMap[grade] || grade
    },

    // 标签类型
    getStatusTagType(status) {
      const statusMap = {
        'DRAFT': 'info',
        'ONGOING': 'primary',
        'COMPLETED': 'success',
        'CANCELLED': 'danger'
      }
      return statusMap[status] || ''
    },

    getGradeTagType(grade) {
      const gradeMap = {
        'EXCELLENT': 'success',
        'GOOD': 'primary',
        'FAIR': 'warning',
        'POOR': 'danger'
      }
      return gradeMap[grade] || ''
    },

    // 分数颜色
    getScoreColor(score) {
      if (!score) return '#c0c4cc'
      if (score >= 90) return '#67c23a'
      if (score >= 80) return '#409eff'
      if (score >= 70) return '#e6a23c'
      return '#f56c6c'
    }
  }
}
</script>

<style scoped>
.assessment-360-detail {
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

.weight-config {
  padding: 20px 0;
}

.weight-item {
  margin-bottom: 30px;
}

.weight-label {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  font-weight: 500;
}

.weight-value {
  color: #409eff;
  font-weight: bold;
}

.weight-summary {
  margin: 30px 0;
}

.weight-templates h4 {
  margin-bottom: 15px;
  color: #303133;
}

.progress-overview {
  margin-bottom: 30px;
}

.progress-item {
  text-align: center;
}

.progress-label {
  margin-bottom: 10px;
  color: #606266;
  font-size: 14px;
}

.progress-number {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
}

.progress-score {
  font-size: 24px;
  font-weight: bold;
}

.dimension-progress h4 {
  margin-bottom: 20px;
  color: #303133;
}

.dimension-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.dimension-item:last-child {
  border-bottom: none;
}

.dimension-header {
  display: flex;
  align-items: center;
  gap: 10px;
}

.dimension-score {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.result-summary {
  margin-bottom: 30px;
}

.result-item {
  text-align: center;
}

.result-label {
  margin-bottom: 10px;
  color: #606266;
  font-size: 14px;
}

.result-value {
  font-size: 28px;
  font-weight: bold;
}

.result-chart h4 {
  margin-bottom: 20px;
  color: #303133;
}
</style>
