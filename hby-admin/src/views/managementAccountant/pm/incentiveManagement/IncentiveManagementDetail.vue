<template>
  <div class="incentive-management-detail">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <el-button icon="el-icon-arrow-left" @click="handleBack">返回</el-button>
        <span class="page-title">{{ pageTitle }}</span>
        <el-tag v-if="formData.incentiveStatus" :type="getStatusColor(formData.incentiveStatus)" class="status-tag">
          {{ formatIncentiveStatus(formData.incentiveStatus) }}
        </el-tag>
      </div>
      <div class="header-right">
        <el-button v-if="isViewMode" type="primary" @click="handleEdit">编辑</el-button>
        <el-button v-if="isEditMode" type="primary" @click="handleSave" :loading="saveLoading">保存</el-button>
        <el-button v-if="isEditMode" @click="handleCancel">取消</el-button>
        <el-dropdown @command="handleCommand" trigger="click">
          <el-button type="info">
            操作<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="copy">复制方案</el-dropdown-item>
            <el-dropdown-item command="template">保存为模板</el-dropdown-item>
            <el-dropdown-item command="export">导出数据</el-dropdown-item>
            <el-dropdown-item command="history" divided>查看历史</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>

    <!-- 标签页内容 -->
    <el-card shadow="never" class="main-card">
      <el-tabs v-model="activeTab" type="card">
        <!-- 基本信息 -->
        <el-tab-pane label="基本信息" name="basic">
          <el-form
            ref="basicForm"
            :model="formData"
            :rules="formRules"
            label-width="120px"
            class="detail-form"
          >
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="激励编码" prop="incentiveCode">
                  <el-input v-model="formData.incentiveCode" :disabled="!isEditMode" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="激励标题" prop="incentiveTitle">
                  <el-input v-model="formData.incentiveTitle" :disabled="!isEditMode" />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="激励类型" prop="incentiveType">
                  <el-select v-model="formData.incentiveType" :disabled="!isEditMode" style="width: 100%">
                    <el-option label="绩效激励" value="PERFORMANCE" />
                    <el-option label="成就激励" value="ACHIEVEMENT" />
                    <el-option label="创新激励" value="INNOVATION" />
                    <el-option label="团队激励" value="TEAM" />
                    <el-option label="专项激励" value="SPECIAL" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="激励分类" prop="incentiveCategory">
                  <el-select v-model="formData.incentiveCategory" :disabled="!isEditMode" style="width: 100%">
                    <el-option label="货币激励" value="MONETARY" />
                    <el-option label="非货币激励" value="NON_MONETARY" />
                    <el-option label="混合激励" value="MIXED" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="激励周期" prop="incentiveCycle">
                  <el-select v-model="formData.incentiveCycle" :disabled="!isEditMode" style="width: 100%">
                    <el-option label="月度" value="MONTHLY" />
                    <el-option label="季度" value="QUARTERLY" />
                    <el-option label="半年度" value="SEMI_ANNUAL" />
                    <el-option label="年度" value="ANNUAL" />
                    <el-option label="项目制" value="PROJECT" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="激励范围" prop="incentiveScope">
                  <el-select v-model="formData.incentiveScope" :disabled="!isEditMode" style="width: 100%">
                    <el-option label="个人" value="INDIVIDUAL" />
                    <el-option label="部门" value="DEPARTMENT" />
                    <el-option label="公司" value="COMPANY" />
                    <el-option label="项目团队" value="PROJECT_TEAM" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="目标部门" prop="targetDeptId">
                  <el-select v-model="formData.targetDeptId" :disabled="!isEditMode" style="width: 100%">
                    <el-option
                      v-for="dept in deptOptions"
                      :key="dept.id"
                      :label="dept.name"
                      :value="dept.id"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="激励负责人" prop="incentiveOwnerId">
                  <el-select v-model="formData.incentiveOwnerId" :disabled="!isEditMode" style="width: 100%">
                    <el-option
                      v-for="user in userOptions"
                      :key="user.id"
                      :label="user.name"
                      :value="user.id"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="激励年度" prop="incentiveYear">
                  <el-date-picker
                    v-model="formData.incentiveYear"
                    type="year"
                    :disabled="!isEditMode"
                    style="width: 100%"
                    value-format="yyyy"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="优先级" prop="priorityLevel">
                  <el-select v-model="formData.priorityLevel" :disabled="!isEditMode" style="width: 100%">
                    <el-option label="高" value="HIGH" />
                    <el-option label="中" value="MEDIUM" />
                    <el-option label="低" value="LOW" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="计划开始时间" prop="plannedStartTime">
                  <el-date-picker
                    v-model="formData.plannedStartTime"
                    type="datetime"
                    :disabled="!isEditMode"
                    style="width: 100%"
                    value-format="yyyy-MM-dd HH:mm:ss"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="计划结束时间" prop="plannedEndTime">
                  <el-date-picker
                    v-model="formData.plannedEndTime"
                    type="datetime"
                    :disabled="!isEditMode"
                    style="width: 100%"
                    value-format="yyyy-MM-dd HH:mm:ss"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item label="激励目标" prop="incentiveObjective">
              <el-input
                v-model="formData.incentiveObjective"
                type="textarea"
                :rows="3"
                :disabled="!isEditMode"
                placeholder="请输入激励目标"
              />
            </el-form-item>

            <el-form-item label="激励原则" prop="incentivePrinciples">
              <el-input
                v-model="formData.incentivePrinciples"
                type="textarea"
                :rows="3"
                :disabled="!isEditMode"
                placeholder="请输入激励原则"
              />
            </el-form-item>

            <el-form-item label="激励描述" prop="incentiveDescription">
              <el-input
                v-model="formData.incentiveDescription"
                type="textarea"
                :rows="4"
                :disabled="!isEditMode"
                placeholder="请输入激励描述"
              />
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 计算配置 -->
        <el-tab-pane label="计算配置" name="calculation">
          <el-form
            ref="calculationForm"
            :model="formData"
            label-width="120px"
            class="detail-form"
          >
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="计算方法" prop="calculationMethod">
                  <el-select v-model="formData.calculationMethod" :disabled="!isEditMode" style="width: 100%">
                    <el-option label="固定金额" value="FIXED" />
                    <el-option label="百分比" value="PERCENTAGE" />
                    <el-option label="公式计算" value="FORMULA" />
                    <el-option label="分层计算" value="TIERED" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="激励系数" prop="incentiveCoefficient">
                  <el-input-number
                    v-model="formData.incentiveCoefficient"
                    :disabled="!isEditMode"
                    :precision="4"
                    :min="0"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="基础金额" prop="baseAmount">
                  <el-input-number
                    v-model="formData.baseAmount"
                    :disabled="!isEditMode"
                    :precision="2"
                    :min="0"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="最小金额" prop="minAmount">
                  <el-input-number
                    v-model="formData.minAmount"
                    :disabled="!isEditMode"
                    :precision="2"
                    :min="0"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="最大金额" prop="maxAmount">
                  <el-input-number
                    v-model="formData.maxAmount"
                    :disabled="!isEditMode"
                    :precision="2"
                    :min="0"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="预算总额" prop="totalBudget">
                  <el-input-number
                    v-model="formData.totalBudget"
                    :disabled="!isEditMode"
                    :precision="2"
                    :min="0"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="已使用金额" prop="usedAmount">
                  <el-input-number
                    v-model="formData.usedAmount"
                    disabled
                    :precision="2"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="剩余金额" prop="remainingAmount">
                  <el-input-number
                    v-model="formData.remainingAmount"
                    disabled
                    :precision="2"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item label="计算公式" prop="calculationFormula">
              <el-input
                v-model="formData.calculationFormula"
                type="textarea"
                :rows="3"
                :disabled="!isEditMode"
                placeholder="请输入计算公式"
              />
            </el-form-item>

            <el-form-item label="权重配置" prop="weightConfig">
              <el-input
                v-model="formData.weightConfig"
                type="textarea"
                :rows="4"
                :disabled="!isEditMode"
                placeholder="请输入权重配置（JSON格式）"
              />
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 发放管理 -->
        <el-tab-pane label="发放管理" name="distribution">
          <el-form
            ref="distributionForm"
            :model="formData"
            label-width="120px"
            class="detail-form"
          >
            <el-row :gutter="24">
              <el-col :span="12">
                <el-form-item label="发放方式" prop="distributionMethod">
                  <el-select v-model="formData.distributionMethod" :disabled="!isEditMode" style="width: 100%">
                    <el-option label="现金" value="CASH" />
                    <el-option label="转账" value="TRANSFER" />
                    <el-option label="代金券" value="VOUCHER" />
                    <el-option label="实物" value="GIFT" />
                    <el-option label="积分" value="POINTS" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="发放状态" prop="distributionStatus">
                  <el-tag :type="getDistributionStatusColor(formData.distributionStatus)">
                    {{ formatDistributionStatus(formData.distributionStatus) }}
                  </el-tag>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="参与人数" prop="participantCount">
                  <el-input-number
                    v-model="formData.participantCount"
                    :disabled="!isEditMode"
                    :min="0"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="符合条件人数" prop="eligibleCount">
                  <el-input-number
                    v-model="formData.eligibleCount"
                    :disabled="!isEditMode"
                    :min="0"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="实际获得人数" prop="actualRecipients">
                  <el-input-number
                    v-model="formData.actualRecipients"
                    :disabled="!isEditMode"
                    :min="0"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="平均激励金额" prop="averageAmount">
                  <el-input-number
                    v-model="formData.averageAmount"
                    disabled
                    :precision="2"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="最高激励金额" prop="maxIndividualAmount">
                  <el-input-number
                    v-model="formData.maxIndividualAmount"
                    :disabled="!isEditMode"
                    :precision="2"
                    :min="0"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="最低激励金额" prop="minIndividualAmount">
                  <el-input-number
                    v-model="formData.minIndividualAmount"
                    :disabled="!isEditMode"
                    :precision="2"
                    :min="0"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item label="申请条件" prop="applicationConditions">
              <el-input
                v-model="formData.applicationConditions"
                type="textarea"
                :rows="3"
                :disabled="!isEditMode"
                placeholder="请输入申请条件"
              />
            </el-form-item>

            <el-form-item label="评审标准" prop="reviewCriteria">
              <el-input
                v-model="formData.reviewCriteria"
                type="textarea"
                :rows="3"
                :disabled="!isEditMode"
                placeholder="请输入评审标准"
              />
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 效果评估 -->
        <el-tab-pane label="效果评估" name="effectiveness">
          <el-form
            ref="effectivenessForm"
            :model="formData"
            label-width="120px"
            class="detail-form"
          >
            <el-row :gutter="24">
              <el-col :span="8">
                <el-form-item label="完成率" prop="completionRate">
                  <div class="progress-container">
                    <el-progress
                      :percentage="formData.completionRate || 0"
                      :stroke-width="8"
                      :color="getProgressColor(formData.completionRate)"
                    />
                  </div>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="满意度评分" prop="satisfactionScore">
                  <el-rate
                    v-model="formData.satisfactionScore"
                    :disabled="!isEditMode"
                    :max="5"
                    show-score
                    text-color="#ff9900"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="效果评级" prop="effectivenessRating">
                  <el-rate
                    v-model="formData.effectivenessRating"
                    :disabled="!isEditMode"
                    :max="5"
                    show-score
                    text-color="#ff9900"
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <el-form-item label="激励效果" prop="incentiveEffect">
              <el-input
                v-model="formData.incentiveEffect"
                type="textarea"
                :rows="4"
                :disabled="!isEditMode"
                placeholder="请输入激励效果描述"
              />
            </el-form-item>

            <el-form-item label="改进建议" prop="improvementSuggestions">
              <el-input
                v-model="formData.improvementSuggestions"
                type="textarea"
                :rows="4"
                :disabled="!isEditMode"
                placeholder="请输入改进建议"
              />
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import incentiveManagementApi, { utils } from '@/api/managementAccountant/pm/incentiveManagement'

export default {
  name: 'IncentiveManagementDetail',
  data() {
    return {
      // 页面模式
      mode: 'view', // view, edit, create
      activeTab: 'basic',
      saveLoading: false,
      
      // 表单数据
      formData: {
        incentiveId: null,
        incentiveCode: '',
        incentiveTitle: '',
        incentiveType: '',
        incentiveCategory: '',
        incentiveStatus: 'DRAFT',
        incentiveYear: new Date().getFullYear(),
        incentiveCycle: '',
        incentiveScope: '',
        targetDeptId: null,
        targetDeptName: '',
        incentiveOwnerId: null,
        incentiveOwnerName: '',
        incentiveObjective: '',
        incentivePrinciples: '',
        incentiveStandards: '',
        incentiveRules: '',
        calculationMethod: 'FIXED',
        calculationFormula: '',
        baseAmount: 0,
        minAmount: 0,
        maxAmount: 0,
        incentiveCoefficient: 1,
        weightConfig: '',
        priorityLevel: 'MEDIUM',
        plannedStartTime: '',
        plannedEndTime: '',
        totalBudget: 0,
        usedAmount: 0,
        remainingAmount: 0,
        participantCount: 0,
        eligibleCount: 0,
        actualRecipients: 0,
        averageAmount: 0,
        maxIndividualAmount: 0,
        minIndividualAmount: 0,
        completionRate: 0,
        satisfactionScore: 0,
        effectivenessRating: 0,
        incentiveEffect: '',
        improvementSuggestions: '',
        incentiveDescription: '',
        applicationConditions: '',
        reviewCriteria: '',
        distributionMethod: 'TRANSFER',
        distributionStatus: 'PENDING'
      },
      
      // 表单验证规则
      formRules: {
        incentiveCode: [
          { required: true, message: '请输入激励编码', trigger: 'blur' }
        ],
        incentiveTitle: [
          { required: true, message: '请输入激励标题', trigger: 'blur' }
        ],
        incentiveType: [
          { required: true, message: '请选择激励类型', trigger: 'change' }
        ],
        targetDeptId: [
          { required: true, message: '请选择目标部门', trigger: 'change' }
        ],
        totalBudget: [
          { required: true, message: '请输入预算总额', trigger: 'blur' },
          { type: 'number', min: 0.01, message: '预算总额必须大于0', trigger: 'blur' }
        ]
      },
      
      // 选项数据
      deptOptions: [],
      userOptions: []
    }
  },
  
  computed: {
    isViewMode() {
      return this.mode === 'view'
    },
    
    isEditMode() {
      return this.mode === 'edit' || this.mode === 'create'
    },
    
    isCreateMode() {
      return this.mode === 'create'
    },
    
    pageTitle() {
      if (this.isCreateMode) {
        return '新建激励方案'
      } else if (this.isEditMode) {
        return '编辑激励方案'
      } else {
        return '激励方案详情'
      }
    }
  },
  
  created() {
    this.initMode()
    this.loadOptions()
    if (!this.isCreateMode) {
      this.loadData()
    } else {
      this.generateIncentiveCode()
    }
  },
  
  methods: {
    // 初始化模式
    initMode() {
      this.mode = this.$route.query.mode || 'view'
    },
    
    // 加载选项数据
    async loadOptions() {
      try {
        // 加载部门选项
        this.deptOptions = [
          { id: 1, name: '技术部' },
          { id: 2, name: '销售部' },
          { id: 3, name: '市场部' },
          { id: 4, name: '人事部' }
        ]
        
        // 加载用户选项
        this.userOptions = [
          { id: 1, name: '张三' },
          { id: 2, name: '李四' },
          { id: 3, name: '王五' },
          { id: 4, name: '赵六' }
        ]
      } catch (error) {
        console.error('加载选项数据失败:', error)
      }
    },
    
    // 加载数据
    async loadData() {
      try {
        const id = this.$route.params.id
        const response = await incentiveManagementApi.getIncentiveManagementById(id)
        if (response.success) {
          this.formData = { ...this.formData, ...response.data }
        } else {
          this.$message.error(response.message || '加载数据失败')
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      }
    },
    
    // 生成激励编码
    generateIncentiveCode() {
      this.formData.incentiveCode = `INC${Date.now()}`
    },
    
    // 格式化方法
    formatIncentiveStatus: utils.formatIncentiveStatus,
    getStatusColor: utils.getStatusColor,
    
    // 格式化发放状态
    formatDistributionStatus(status) {
      const statusMap = {
        'PENDING': '待发放',
        'PROCESSING': '发放中',
        'COMPLETED': '已发放',
        'FAILED': '发放失败'
      }
      return statusMap[status] || status
    },
    
    // 获取发放状态颜色
    getDistributionStatusColor(status) {
      const colorMap = {
        'PENDING': 'warning',
        'PROCESSING': 'primary',
        'COMPLETED': 'success',
        'FAILED': 'danger'
      }
      return colorMap[status] || 'info'
    },
    
    // 获取进度颜色
    getProgressColor(percentage) {
      if (percentage >= 80) return '#67c23a'
      if (percentage >= 60) return '#e6a23c'
      if (percentage >= 40) return '#f56c6c'
      return '#909399'
    },

    // 返回
    handleBack() {
      this.$router.go(-1)
    },

    // 编辑
    handleEdit() {
      this.mode = 'edit'
    },

    // 保存
    async handleSave() {
      try {
        // 验证表单
        await this.$refs.basicForm.validate()

        this.saveLoading = true

        // 验证激励数据
        const errors = utils.validateIncentiveData(this.formData)
        if (errors.length > 0) {
          this.$message.error(errors[0])
          return
        }

        let response
        if (this.isCreateMode) {
          response = await incentiveManagementApi.createIncentiveScheme(this.formData)
        } else {
          response = await incentiveManagementApi.updateIncentiveScheme(this.formData)
        }

        if (response.success) {
          this.$message.success(this.isCreateMode ? '创建成功' : '保存成功')
          if (this.isCreateMode) {
            this.$router.replace({
              name: 'IncentiveManagementDetail',
              params: { id: response.data.incentiveId },
              query: { mode: 'view' }
            })
          } else {
            this.mode = 'view'
            this.loadData()
          }
        } else {
          this.$message.error(response.message || '保存失败')
        }
      } catch (error) {
        console.error('保存失败:', error)
        this.$message.error('保存失败')
      } finally {
        this.saveLoading = false
      }
    },

    // 取消
    handleCancel() {
      if (this.isCreateMode) {
        this.$router.go(-1)
      } else {
        this.mode = 'view'
        this.loadData()
      }
    },

    // 处理命令
    async handleCommand(command) {
      switch (command) {
        case 'copy':
          await this.handleCopy()
          break
        case 'template':
          await this.handleSaveAsTemplate()
          break
        case 'export':
          await this.handleExport()
          break
        case 'history':
          await this.handleViewHistory()
          break
      }
    },

    // 复制方案
    async handleCopy() {
      try {
        const { value: newTitle } = await this.$prompt('请输入新激励方案标题', '复制激励方案', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValue: `${this.formData.incentiveTitle}_副本`,
          inputValidator: (value) => {
            if (!value) {
              return '标题不能为空'
            }
            return true
          }
        })

        const response = await incentiveManagementApi.copyIncentiveScheme(this.formData.incentiveId, newTitle)
        if (response.success) {
          this.$message.success('复制成功')
          this.$router.push({
            name: 'IncentiveManagementDetail',
            params: { id: response.data.incentiveId },
            query: { mode: 'view' }
          })
        } else {
          this.$message.error(response.message || '复制失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('复制激励方案失败:', error)
          this.$message.error('复制失败')
        }
      }
    },

    // 保存为模板
    async handleSaveAsTemplate() {
      try {
        const { value: templateName } = await this.$prompt('请输入模板名称', '保存为模板', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValue: `${this.formData.incentiveTitle}_模板`,
          inputValidator: (value) => {
            if (!value) {
              return '模板名称不能为空'
            }
            return true
          }
        })

        const response = await incentiveManagementApi.saveAsTemplate(this.formData.incentiveId, templateName)
        if (response.success) {
          this.$message.success('保存模板成功')
        } else {
          this.$message.error(response.message || '保存模板失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('保存模板失败:', error)
          this.$message.error('保存模板失败')
        }
      }
    },

    // 导出数据
    async handleExport() {
      try {
        const response = await incentiveManagementApi.exportIncentiveData(
          this.formData.incentiveYear,
          this.formData.incentiveType,
          this.formData.incentiveStatus,
          this.formData.targetDeptId
        )
        if (response.success) {
          this.$message.success('导出成功')
        } else {
          this.$message.error(response.message || '导出失败')
        }
      } catch (error) {
        console.error('导出数据失败:', error)
        this.$message.error('导出失败')
      }
    },

    // 查看历史
    async handleViewHistory() {
      try {
        const response = await incentiveManagementApi.getIncentiveHistory(this.formData.incentiveId)
        if (response.success) {
          // 这里可以打开历史记录对话框
          this.$message.info('历史记录功能开发中')
        } else {
          this.$message.error(response.message || '获取历史记录失败')
        }
      } catch (error) {
        console.error('获取历史记录失败:', error)
        this.$message.error('获取历史记录失败')
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.incentive-management-detail {
  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    padding: 0 20px;
    
    .header-left {
      display: flex;
      align-items: center;
      
      .page-title {
        font-size: 18px;
        font-weight: 500;
        margin: 0 15px;
      }
      
      .status-tag {
        margin-left: 10px;
      }
    }
    
    .header-right {
      .el-button {
        margin-left: 10px;
      }
    }
  }
  
  .main-card {
    .detail-form {
      padding: 20px;
      
      .el-form-item {
        margin-bottom: 20px;
      }
    }
    
    .progress-container {
      padding: 10px 0;
    }
  }
}
</style>
