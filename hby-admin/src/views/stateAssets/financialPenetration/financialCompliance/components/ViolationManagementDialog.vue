<template>
  <el-dialog
    title="违规行为管理"
    :visible.sync="dialogVisible"
    width="1000px"
    :before-close="handleClose"
  >
    <el-steps :active="currentStep" finish-status="success" style="margin-bottom: 20px;">
      <el-step title="违规识别" description="识别违规行为"></el-step>
      <el-step title="违规认定" description="认定违规性质"></el-step>
      <el-step title="处理措施" description="制定处理方案"></el-step>
      <el-step title="跟踪监督" description="跟踪执行情况"></el-step>
    </el-steps>
    
    <!-- 违规识别 -->
    <div v-if="currentStep === 0">
      <el-form :model="violationForm" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="企业名称">
              <el-input v-model="violationForm.enterpriseName" placeholder="请输入企业名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发现时间">
              <el-date-picker
                v-model="violationForm.discoveryDate"
                type="date"
                placeholder="选择发现时间"
                style="width: 100%;"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="违规类型">
          <el-select v-model="violationForm.violationType" placeholder="请选择违规类型" style="width: 100%;">
            <el-option label="财务制度违规" value="FINANCIAL_SYSTEM"></el-option>
            <el-option label="内控制度违规" value="INTERNAL_CONTROL"></el-option>
            <el-option label="预算管理违规" value="BUDGET_MANAGEMENT"></el-option>
            <el-option label="资金使用违规" value="FUND_USAGE"></el-option>
            <el-option label="投资决策违规" value="INVESTMENT_DECISION"></el-option>
            <el-option label="信息披露违规" value="INFORMATION_DISCLOSURE"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="违规描述">
          <el-input
            v-model="violationForm.description"
            type="textarea"
            :rows="4"
            placeholder="请详细描述违规行为"
          />
        </el-form-item>
        
        <el-form-item label="发现方式">
          <el-radio-group v-model="violationForm.discoveryMethod">
            <el-radio label="ROUTINE_CHECK">日常检查</el-radio>
            <el-radio label="SPECIAL_AUDIT">专项审计</el-radio>
            <el-radio label="WHISTLEBLOWING">举报投诉</el-radio>
            <el-radio label="SYSTEM_ALERT">系统预警</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="涉及金额">
          <el-input-number
            v-model="violationForm.involvedAmount"
            :min="0"
            placeholder="请输入涉及金额"
            style="width: 100%;"
          />
        </el-form-item>
      </el-form>
    </div>
    
    <!-- 违规认定 -->
    <div v-if="currentStep === 1">
      <el-form :model="violationForm" label-width="120px">
        <el-form-item label="违规性质">
          <el-select v-model="violationForm.violationNature" placeholder="请选择违规性质" style="width: 100%;">
            <el-option label="轻微违规" value="MINOR"></el-option>
            <el-option label="一般违规" value="GENERAL"></el-option>
            <el-option label="严重违规" value="SERIOUS"></el-option>
            <el-option label="重大违规" value="MAJOR"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="违规等级">
          <el-rate v-model="violationForm.violationLevel" show-text></el-rate>
        </el-form-item>
        
        <el-form-item label="影响范围">
          <el-checkbox-group v-model="violationForm.impactScope">
            <el-checkbox label="FINANCIAL_IMPACT">财务影响</el-checkbox>
            <el-checkbox label="OPERATIONAL_IMPACT">经营影响</el-checkbox>
            <el-checkbox label="REPUTATION_IMPACT">声誉影响</el-checkbox>
            <el-checkbox label="LEGAL_IMPACT">法律影响</el-checkbox>
            <el-checkbox label="REGULATORY_IMPACT">监管影响</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        
        <el-form-item label="责任主体">
          <el-input v-model="violationForm.responsibleParty" placeholder="请输入责任主体" />
        </el-form-item>
        
        <el-form-item label="认定依据">
          <el-input
            v-model="violationForm.determinationBasis"
            type="textarea"
            :rows="3"
            placeholder="请输入认定依据"
          />
        </el-form-item>
        
        <el-form-item label="认定结论">
          <el-input
            v-model="violationForm.conclusion"
            type="textarea"
            :rows="3"
            placeholder="请输入认定结论"
          />
        </el-form-item>
      </el-form>
    </div>
    
    <!-- 处理措施 -->
    <div v-if="currentStep === 2">
      <el-form :model="violationForm" label-width="120px">
        <el-form-item label="处理方式">
          <el-checkbox-group v-model="violationForm.handlingMethods">
            <el-checkbox label="WARNING">警告</el-checkbox>
            <el-checkbox label="RECTIFICATION">限期整改</el-checkbox>
            <el-checkbox label="FINE">罚款</el-checkbox>
            <el-checkbox label="SUSPENSION">暂停相关业务</el-checkbox>
            <el-checkbox label="PERSONNEL_ACTION">人事处理</el-checkbox>
            <el-checkbox label="LEGAL_ACTION">法律追责</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        
        <el-form-item label="处理决定">
          <el-input
            v-model="violationForm.handlingDecision"
            type="textarea"
            :rows="4"
            placeholder="请输入具体的处理决定"
          />
        </el-form-item>
        
        <el-form-item label="整改要求">
          <el-input
            v-model="violationForm.rectificationRequirements"
            type="textarea"
            :rows="3"
            placeholder="请输入整改要求"
          />
        </el-form-item>
        
        <el-form-item label="整改期限">
          <el-date-picker
            v-model="violationForm.rectificationDeadline"
            type="date"
            placeholder="选择整改期限"
            style="width: 100%;"
          />
        </el-form-item>
        
        <el-form-item label="罚款金额">
          <el-input-number
            v-model="violationForm.fineAmount"
            :min="0"
            placeholder="请输入罚款金额"
            style="width: 100%;"
          />
        </el-form-item>
        
        <el-form-item label="责任人处理">
          <el-input
            v-model="violationForm.personnelAction"
            type="textarea"
            :rows="2"
            placeholder="请输入对责任人的处理措施"
          />
        </el-form-item>
      </el-form>
    </div>
    
    <!-- 跟踪监督 -->
    <div v-if="currentStep === 3">
      <el-form :model="violationForm" label-width="120px">
        <el-form-item label="执行状态">
          <el-select v-model="violationForm.executionStatus" placeholder="请选择执行状态" style="width: 100%;">
            <el-option label="未开始" value="NOT_STARTED"></el-option>
            <el-option label="执行中" value="IN_PROGRESS"></el-option>
            <el-option label="已完成" value="COMPLETED"></el-option>
            <el-option label="逾期未完成" value="OVERDUE"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="完成进度">
          <el-slider v-model="violationForm.completionProgress" show-input></el-slider>
        </el-form-item>
        
        <el-form-item label="跟踪记录">
          <el-table :data="trackingRecords" size="small" max-height="200">
            <el-table-column prop="date" label="跟踪日期" width="120"></el-table-column>
            <el-table-column prop="content" label="跟踪内容" min-width="200"></el-table-column>
            <el-table-column prop="result" label="跟踪结果" width="100"></el-table-column>
            <el-table-column prop="nextAction" label="下一步行动" min-width="150"></el-table-column>
          </el-table>
        </el-form-item>
        
        <el-form-item label="效果评估">
          <el-input
            v-model="violationForm.effectivenessAssessment"
            type="textarea"
            :rows="3"
            placeholder="请输入效果评估"
          />
        </el-form-item>
        
        <el-form-item label="经验总结">
          <el-input
            v-model="violationForm.lessonLearned"
            type="textarea"
            :rows="3"
            placeholder="请输入经验总结和改进建议"
          />
        </el-form-item>
      </el-form>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button v-if="currentStep > 0" @click="handlePrevious">上一步</el-button>
      <el-button v-if="currentStep < 3" type="primary" @click="handleNext">下一步</el-button>
      <el-button v-if="currentStep === 3" type="success" :loading="submitLoading" @click="handleComplete">完成</el-button>
      <el-button @click="handleClose">取消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { handleViolation } from '@/api/stateAssets/financialCompliance'

export default {
  name: 'ViolationManagementDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    violationData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      currentStep: 0,
      submitLoading: false,
      violationForm: {
        enterpriseName: '',
        discoveryDate: '',
        violationType: '',
        description: '',
        discoveryMethod: '',
        involvedAmount: 0,
        violationNature: '',
        violationLevel: 0,
        impactScope: [],
        responsibleParty: '',
        determinationBasis: '',
        conclusion: '',
        handlingMethods: [],
        handlingDecision: '',
        rectificationRequirements: '',
        rectificationDeadline: '',
        fineAmount: 0,
        personnelAction: '',
        executionStatus: '',
        completionProgress: 0,
        effectivenessAssessment: '',
        lessonLearned: ''
      },
      trackingRecords: []
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
      if (val) {
        this.initForm()
      }
    }
  },
  methods: {
    initForm() {
      this.currentStep = 0
      this.trackingRecords = []
      if (Object.keys(this.violationData).length > 0) {
        const data = { ...this.violationData }
        // 确保 impactScope 是数组
        if (data.impactScope && typeof data.impactScope === 'string') {
          try { data.impactScope = JSON.parse(data.impactScope) } catch (e) { data.impactScope = [] }
        }
        if (!Array.isArray(data.impactScope)) data.impactScope = []
        // 确保 handlingMethods 是数组
        if (data.handlingMethods && typeof data.handlingMethods === 'string') {
          try { data.handlingMethods = JSON.parse(data.handlingMethods) } catch (e) { data.handlingMethods = [] }
        }
        if (!Array.isArray(data.handlingMethods)) data.handlingMethods = []
        this.violationForm = {
          enterpriseName: data.enterpriseName || '',
          discoveryDate: data.discoveryDate || data.checkDate || '',
          violationType: data.violationType || '',
          description: data.violationDescription || data.description || '',
          discoveryMethod: data.discoveryMethod || '',
          involvedAmount: data.involvedAmount || 0,
          violationNature: data.violationNature || '',
          violationLevel: data.violationLevel || 0,
          impactScope: data.impactScope,
          responsibleParty: data.responsibleParty || '',
          determinationBasis: data.determinationBasis || '',
          conclusion: data.conclusion || '',
          handlingMethods: data.handlingMethods,
          handlingDecision: data.handlingDecision || '',
          rectificationRequirements: data.rectificationRequirements || '',
          rectificationDeadline: data.rectificationDeadline || '',
          fineAmount: data.fineAmount || 0,
          personnelAction: data.personnelAction || '',
          executionStatus: data.executionStatus || '',
          completionProgress: data.completionProgress || 0,
          effectivenessAssessment: data.effectivenessAssessment || '',
          lessonLearned: data.lessonLearned || ''
        }
        // 根据数据状态设置步骤
        if (data.executionStatus) {
          this.currentStep = 3
        } else if (data.handlingDecision) {
          this.currentStep = 2
        } else if (data.violationNature) {
          this.currentStep = 1
        }
        // 根据已有数据构建跟踪记录时间线
        this.buildTrackingRecords()
      } else {
        this.violationForm = {
          enterpriseName: '',
          discoveryDate: '',
          violationType: '',
          description: '',
          discoveryMethod: '',
          involvedAmount: 0,
          violationNature: '',
          violationLevel: 0,
          impactScope: [],
          responsibleParty: '',
          determinationBasis: '',
          conclusion: '',
          handlingMethods: [],
          handlingDecision: '',
          rectificationRequirements: '',
          rectificationDeadline: '',
          fineAmount: 0,
          personnelAction: '',
          executionStatus: '',
          completionProgress: 0,
          effectivenessAssessment: '',
          lessonLearned: ''
        }
      }
    },

    /**
     * 根据违规数据已有字段构建跟踪记录时间线
     */
    buildTrackingRecords() {
      const records = []
      const data = this.violationData
      const checkDate = data.checkDate ? String(data.checkDate).substring(0, 10) : '-'
      // 始终添加一条"违规发现"记录
      records.push({
        date: checkDate,
        content: data.discoveryMethod ? '违规行为识别（' + this.getDiscoveryMethodLabel(data.discoveryMethod) + '）' : '违规行为识别',
        result: '已识别',
        nextAction: '进行违规认定'
      })
      if (data.violationNature) {
        records.push({
          date: '-',
          content: '违规性质认定：' + this.getViolationNatureLabel(data.violationNature),
          result: '已认定',
          nextAction: '制定处理措施'
        })
      }
      if (data.handlingDecision) {
        records.push({
          date: '-',
          content: '处理决定：' + (data.handlingDecision.length > 20 ? data.handlingDecision.substring(0, 20) + '...' : data.handlingDecision),
          result: '已下达',
          nextAction: '跟踪执行情况'
        })
      }
      if (data.executionStatus) {
        records.push({
          date: '-',
          content: '执行状态更新',
          result: this.getExecutionStatusLabel(data.executionStatus),
          nextAction: data.completionProgress >= 100 ? '完成结案' : '继续跟踪'
        })
      }
      this.trackingRecords = records
    },

    getDiscoveryMethodLabel(value) {
      const map = {
        'ROUTINE_CHECK': '日常检查',
        'SPECIAL_AUDIT': '专项审计',
        'WHISTLEBLOWING': '举报投诉',
        'SYSTEM_ALERT': '系统预警'
      }
      return map[value] || value
    },

    getViolationNatureLabel(value) {
      const map = {
        'MINOR': '轻微违规',
        'GENERAL': '一般违规',
        'SERIOUS': '严重违规',
        'MAJOR': '重大违规'
      }
      return map[value] || value
    },

    getExecutionStatusLabel(value) {
      const map = {
        'NOT_STARTED': '未开始',
        'IN_PROGRESS': '执行中',
        'COMPLETED': '已完成',
        'OVERDUE': '逾期未完成'
      }
      return map[value] || value
    },

    handleNext() {
      if (this.validateCurrentStep()) {
        this.currentStep++
      }
    },

    handlePrevious() {
      this.currentStep--
    },

    async handleComplete() {
      if (!this.validateCurrentStep()) return
      const violationId = this.violationData.id || this.violationData.complianceId
      if (!violationId) {
        this.$message.error('缺少违规记录ID，无法提交')
        return
      }
      this.submitLoading = true
      try {
        const res = await handleViolation(violationId, this.violationForm)
        // 后端 R 类返回 result:200 表示成功
        if (res && (res.result === 200 || res.code === 1 || res.data === true)) {
          this.$message({ type: 'success', message: '违规行为处理完成' })
          this.$emit('refresh')
          this.handleClose()
        } else {
          this.$message.error((res && res.msg) || '提交失败，请稍后重试')
        }
      } catch (error) {
        console.error('违规处理提交失败:', error)
        this.$message.error(error.message || '提交失败，请稍后重试')
      } finally {
        this.submitLoading = false
      }
    },

    validateCurrentStep() {
      switch (this.currentStep) {
        case 0:
          if (!this.violationForm.enterpriseName || !this.violationForm.violationType) {
            this.$message.warning('请填写必要信息')
            return false
          }
          break
        case 1:
          if (!this.violationForm.violationNature || !this.violationForm.conclusion) {
            this.$message.warning('请完成违规认定')
            return false
          }
          break
        case 2:
          if (!this.violationForm.handlingDecision) {
            this.$message.warning('请制定处理措施')
            return false
          }
          break
        case 3:
          if (!this.violationForm.executionStatus) {
            this.$message.warning('请选择执行状态')
            return false
          }
          break
      }
      return true
    },

    handleClose() {
      this.$emit('update:visible', false)
      this.currentStep = 0
    }
  }
}
</script>

<style scoped>
.el-step__title {
  font-size: 14px;
}
</style>
