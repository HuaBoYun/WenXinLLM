<template>
  <el-dialog
    title="投资项目审批"
    :visible.sync="dialogVisible"
    width="700px"
    :before-close="handleClose"
    :close-on-click-modal="false"
  >
    <div v-loading="loading">
      <!-- 项目信息 -->
      <el-card shadow="never" class="mb-20">
        <div slot="header">
          <span>项目信息</span>
        </div>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="项目名称">{{ projectData.projectName }}</el-descriptions-item>
          <el-descriptions-item label="项目编号">{{ projectData.projectCode || projectData.projectId || '-' }}</el-descriptions-item>
          <el-descriptions-item label="投资企业">{{ projectData.companyName || projectData.investorName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="被投资企业">{{ projectData.targetCompany || projectData.investeeName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="投资金额">{{ projectData.investAmount || projectData.investmentAmount || '-' }}万元</el-descriptions-item>
          <el-descriptions-item label="投资类型">
            {{ getInvestmentTypeText(projectData.investType || projectData.investmentType) }}
          </el-descriptions-item>
          <el-descriptions-item label="预期收益率">{{ projectData.expectedReturn || '-' }}%</el-descriptions-item>
          <el-descriptions-item label="是否主业">{{ projectData.isMainBiz === 'Y' ? '是' : '否' }}</el-descriptions-item>
          <el-descriptions-item label="项目状态">{{ getStatusText(projectData.projectStatus) }}</el-descriptions-item>
          <el-descriptions-item label="审批日期">{{ projectData.approvalDate || '未审批' }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 审批表单 -->
      <el-form
        ref="approvalForm"
        :model="approvalForm"
        :rules="approvalRules"
        label-width="120px"
      >
        <el-form-item label="审批结果" prop="approvalResult">
          <el-radio-group v-model="approvalForm.approvalResult">
            <el-radio label="APPROVED">通过</el-radio>
            <el-radio label="REJECTED">拒绝</el-radio>
            <el-radio label="CONDITIONAL">有条件通过</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="审批意见" prop="approvalComments">
          <el-input
            v-model="approvalForm.approvalComments"
            type="textarea"
            :rows="4"
            placeholder="请输入审批意见"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>

        <el-form-item 
          label="风险评级" 
          prop="riskRating" 
          v-if="approvalForm.approvalResult === 'APPROVED' || approvalForm.approvalResult === 'CONDITIONAL'"
        >
          <el-select v-model="approvalForm.riskRating" placeholder="请选择风险评级" style="width: 200px;">
            <el-option label="低风险" value="LOW"></el-option>
            <el-option label="中风险" value="MEDIUM"></el-option>
            <el-option label="高风险" value="HIGH"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item 
          label="监管措施" 
          prop="supervisionMeasures" 
          v-if="approvalForm.approvalResult === 'APPROVED' || approvalForm.approvalResult === 'CONDITIONAL'"
        >
          <el-checkbox-group v-model="approvalForm.supervisionMeasures">
            <el-checkbox label="REGULAR_REPORT">定期报告</el-checkbox>
            <el-checkbox label="PROGRESS_TRACKING">进度跟踪</el-checkbox>
            <el-checkbox label="RISK_MONITORING">风险监控</el-checkbox>
            <el-checkbox label="COMPLIANCE_CHECK">合规检查</el-checkbox>
            <el-checkbox label="PERFORMANCE_EVALUATION">绩效评估</el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <el-form-item 
          label="附加条件" 
          prop="additionalConditions"
          v-if="approvalForm.approvalResult === 'CONDITIONAL'"
        >
          <el-input
            v-model="approvalForm.additionalConditions"
            type="textarea"
            :rows="3"
            placeholder="请输入附加条件"
            maxlength="300"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="后续要求" prop="followUpRequirements">
          <el-input
            v-model="approvalForm.followUpRequirements"
            type="textarea"
            :rows="3"
            placeholder="请输入后续监管要求"
            maxlength="300"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="审批期限" prop="approvalDeadline">
          <el-date-picker
            v-model="approvalForm.approvalDeadline"
            type="date"
            placeholder="选择审批期限"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            style="width: 200px;"
          />
        </el-form-item>

        <el-form-item label="投资限额" prop="investmentLimit" v-if="approvalForm.approvalResult === 'APPROVED'">
          <el-input-number
            v-model="approvalForm.investmentLimit"
            :min="0"
            :precision="2"
            placeholder="请输入投资限额"
            style="width: 200px;"
          />
          <span style="margin-left: 10px; color: #909399;">万元</span>
        </el-form-item>
      </el-form>

      <!-- 审批历史 -->
      <el-card shadow="never" v-if="approvalHistory.length > 0">
        <div slot="header">
          <span>审批历史</span>
        </div>
        <el-timeline>
          <el-timeline-item
            v-for="(history, index) in approvalHistory"
            :key="index"
            :timestamp="history.approvalTime"
            placement="top"
          >
            <el-card>
              <div class="history-item">
                <div class="history-header">
                  <span class="history-approver">{{ history.approverName }}</span>
                  <el-tag :type="getApprovalResultTag(history.approvalResult)" size="mini">
                    {{ getApprovalResultText(history.approvalResult) }}
                  </el-tag>
                </div>
                <div class="history-content">{{ history.approvalComments }}</div>
                <div class="history-measures" v-if="history.supervisionMeasures">
                  <span class="measures-label">监管措施：</span>
                  <el-tag
                    v-for="measure in history.supervisionMeasures"
                    :key="measure"
                    size="mini"
                    style="margin-right: 5px;"
                  >
                    {{ getMeasureText(measure) }}
                  </el-tag>
                </div>
              </div>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </el-card>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button
        type="primary"
        @click="handleSubmitApproval"
        :loading="submitLoading"
      >
        提交审批
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  approveInvestmentProject,
  rejectInvestmentProject,
  getApprovalHistory
} from '@/api/stateAssets/investmentDecision'

export default {
  name: 'InvestmentApprovalDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    projectData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      submitLoading: false,
      approvalHistory: [],
      approvalForm: {
        projectId: '',
        approvalResult: 'APPROVED',
        approvalComments: '',
        riskRating: 'LOW',
        supervisionMeasures: [],
        additionalConditions: '',
        followUpRequirements: '',
        approvalDeadline: '',
        investmentLimit: null
      },
      approvalRules: {
        approvalResult: [
          { required: true, message: '请选择审批结果', trigger: 'change' }
        ],
        approvalComments: [
          { required: true, message: '请输入审批意见', trigger: 'blur' },
          { min: 10, message: '审批意见至少10个字符', trigger: 'blur' }
        ],
        riskRating: [
          { required: true, message: '请选择风险评级', trigger: 'change' }
        ]
      }
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
      if (val && this.projectData.projectId) {
        this.initForm()
        this.loadApprovalHistory()
      }
    }
  },
  methods: {
    // 初始化表单
    initForm() {
      this.approvalForm = {
        projectId: this.projectData.projectId,
        approvalResult: 'APPROVED',
        approvalComments: '',
        riskRating: 'LOW',
        supervisionMeasures: [],
        additionalConditions: '',
        followUpRequirements: '',
        approvalDeadline: '',
        investmentLimit: null
      }
      
      this.$nextTick(() => {
        this.$refs.approvalForm && this.$refs.approvalForm.clearValidate()
      })
    },

    // 加载审批历史
    async loadApprovalHistory() {
      this.loading = true
      try {
        const response = await getApprovalHistory({
          projectId: this.projectData.projectId
        })
        this.approvalHistory = response.data || []
      } catch (error) {
        console.error('加载审批历史失败:', error)
      } finally {
        this.loading = false
      }
    },

    // 提交审批
    handleSubmitApproval() {
      this.$refs.approvalForm.validate(async (valid) => {
        if (!valid) return

        this.submitLoading = true
        try {
          if (this.approvalForm.approvalResult === 'APPROVED' || this.approvalForm.approvalResult === 'CONDITIONAL') {
            await approveInvestmentProject(this.approvalForm)
            this.$message.success('审批通过')
          } else {
            await rejectInvestmentProject(this.approvalForm)
            this.$message.success('审批拒绝')
          }
          
          this.handleClose()
          this.$emit('refresh')
        } catch (error) {
          this.$message.error('审批提交失败')
        } finally {
          this.submitLoading = false
        }
      })
    },

    // 获取投资方式文本
    getInvestmentTypeText(type) {
      const textMap = {
        'EQUITY': '股权投资',
        'DEBT': '债权投资',
        'MIXED': '混合投资',
        'FUND': '基金投资'
      }
      return textMap[type] || type
    },

    // 获取风险标签
    getRiskTag(level) {
      const tagMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger'
      }
      return tagMap[level] || 'info'
    },

    // 获取项目状态文本
    getStatusText(status) {
      const textMap = {
        'PLANNING': '规划中',
        'APPROVED': '已审批',
        'EXECUTING': '执行中',
        'COMPLETED': '已完成',
        'SUSPENDED': '已暂停'
      }
      return textMap[status] || status || '-'
    },

    // 获取审批结果标签
    getApprovalResultTag(result) {
      const tagMap = {
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'CONDITIONAL': 'warning'
      }
      return tagMap[result] || 'info'
    },

    // 获取审批结果文本
    getApprovalResultText(result) {
      const textMap = {
        'APPROVED': '通过',
        'REJECTED': '拒绝',
        'CONDITIONAL': '有条件通过'
      }
      return textMap[result] || result
    },

    // 获取监管措施文本
    getMeasureText(measure) {
      const textMap = {
        'REGULAR_REPORT': '定期报告',
        'PROGRESS_TRACKING': '进度跟踪',
        'RISK_MONITORING': '风险监控',
        'COMPLIANCE_CHECK': '合规检查',
        'PERFORMANCE_EVALUATION': '绩效评估'
      }
      return textMap[measure] || measure
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.initForm()
    }
  }
}
</script>

<style scoped>
.mb-20 {
  margin-bottom: 20px;
}

.info-label {
  font-weight: bold;
  color: #606266;
  margin-bottom: 5px;
}

.info-content {
  color: #303133;
  line-height: 1.5;
}

.history-item {
  .history-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
    
    .history-approver {
      font-weight: bold;
      color: #303133;
    }
  }
  
  .history-content {
    color: #606266;
    line-height: 1.5;
    margin-bottom: 10px;
  }
  
  .history-measures {
    .measures-label {
      font-weight: bold;
      color: #606266;
      margin-right: 10px;
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>
