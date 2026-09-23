<template>
  <el-dialog
    title="指令确认"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose">
    
    <div class="instruction-confirm-container">
      <!-- 指令信息 -->
      <el-card class="instruction-card">
        <div slot="header">
          <span>指令信息</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">指令标题：</span>
              <span class="info-value">{{ instructionData.instructionTitle }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">目标企业：</span>
              <span class="info-value">{{ instructionData.targetEnterprise }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">指令类型：</span>
              <span class="info-value">{{ getInstructionTypeText(instructionData.instructionType) }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">优先级：</span>
              <el-tag :type="getPriorityTagType(instructionData.priority)">
                {{ getPriorityText(instructionData.priority) }}
              </el-tag>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">下发日期：</span>
              <span class="info-value">{{ instructionData.issueDate }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="info-item">
              <span class="info-label">截止日期：</span>
              <span class="info-value">{{ instructionData.deadline }}</span>
            </div>
          </el-col>
        </el-row>
        <div class="info-item">
          <span class="info-label">指令内容：</span>
          <div class="info-content">{{ instructionData.instructionContent }}</div>
        </div>
      </el-card>
      
      <!-- 执行结果 -->
      <el-card class="result-card">
        <div slot="header">
          <span>执行结果</span>
        </div>
        <el-form ref="confirmForm" :model="confirmData" :rules="confirmRules" label-width="120px">
          <el-form-item label="执行状态" prop="executionStatus">
            <el-radio-group v-model="confirmData.executionStatus">
              <el-radio label="completed">已完成</el-radio>
              <el-radio label="partially_completed">部分完成</el-radio>
              <el-radio label="not_completed">未完成</el-radio>
            </el-radio-group>
          </el-form-item>
          
          <el-form-item label="完成质量" prop="completionQuality">
            <el-rate
              v-model="confirmData.completionQuality"
              :max="5"
              show-text
              :texts="['很差', '较差', '一般', '良好', '优秀']">
            </el-rate>
          </el-form-item>
          
          <el-form-item label="执行评价" prop="executionEvaluation">
            <el-input
              v-model="confirmData.executionEvaluation"
              type="textarea"
              :rows="4"
              placeholder="请输入对执行情况的评价">
            </el-input>
          </el-form-item>
          
          <el-form-item label="存在问题" prop="existingProblems">
            <el-checkbox-group v-model="confirmData.existingProblems">
              <el-checkbox label="execution_delay">执行延迟</el-checkbox>
              <el-checkbox label="incomplete_content">内容不完整</el-checkbox>
              <el-checkbox label="quality_issue">质量问题</el-checkbox>
              <el-checkbox label="compliance_issue">合规问题</el-checkbox>
              <el-checkbox label="other">其他问题</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          
          <el-form-item v-if="confirmData.existingProblems.includes('other')" label="其他问题描述" prop="otherProblemsDescription">
            <el-input
              v-model="confirmData.otherProblemsDescription"
              type="textarea"
              :rows="3"
              placeholder="请描述其他问题">
            </el-input>
          </el-form-item>
          
          <el-form-item label="改进建议" prop="improvementSuggestions">
            <el-input
              v-model="confirmData.improvementSuggestions"
              type="textarea"
              :rows="3"
              placeholder="请输入改进建议">
            </el-input>
          </el-form-item>
          
          <el-form-item label="后续要求" prop="followUpRequirements">
            <el-checkbox-group v-model="confirmData.followUpRequirements">
              <el-checkbox label="regular_report">定期报告</el-checkbox>
              <el-checkbox label="rectification">整改要求</el-checkbox>
              <el-checkbox label="follow_up_inspection">后续检查</el-checkbox>
              <el-checkbox label="training">培训要求</el-checkbox>
              <el-checkbox label="none">无后续要求</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          
          <el-form-item label="确认结果" prop="confirmResult">
            <el-radio-group v-model="confirmData.confirmResult">
              <el-radio label="approved">通过确认</el-radio>
              <el-radio label="conditional_approval">有条件通过</el-radio>
              <el-radio label="rejected">不予确认</el-radio>
            </el-radio-group>
          </el-form-item>
          
          <el-form-item v-if="confirmData.confirmResult === 'conditional_approval'" label="通过条件" prop="approvalConditions">
            <el-input
              v-model="confirmData.approvalConditions"
              type="textarea"
              :rows="3"
              placeholder="请输入通过条件">
            </el-input>
          </el-form-item>
          
          <el-form-item v-if="confirmData.confirmResult === 'rejected'" label="拒绝原因" prop="rejectionReason">
            <el-input
              v-model="confirmData.rejectionReason"
              type="textarea"
              :rows="3"
              placeholder="请输入拒绝原因">
            </el-input>
          </el-form-item>
          
          <el-form-item label="确认人" prop="confirmer">
            <el-input v-model="confirmData.confirmer" placeholder="请输入确认人"></el-input>
          </el-form-item>
          
          <el-form-item label="确认时间" prop="confirmTime">
            <el-date-picker
              v-model="confirmData.confirmTime"
              type="datetime"
              placeholder="选择确认时间"
              style="width: 100%">
            </el-date-picker>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleConfirm">确认</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'InstructionConfirmDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    instructionData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      confirmData: {
        executionStatus: 'completed',
        completionQuality: 4,
        executionEvaluation: '',
        existingProblems: [],
        otherProblemsDescription: '',
        improvementSuggestions: '',
        followUpRequirements: [],
        confirmResult: 'approved',
        approvalConditions: '',
        rejectionReason: '',
        confirmer: '',
        confirmTime: new Date()
      },
      confirmRules: {
        executionStatus: [
          { required: true, message: '请选择执行状态', trigger: 'change' }
        ],
        completionQuality: [
          { required: true, message: '请评价完成质量', trigger: 'change' }
        ],
        executionEvaluation: [
          { required: true, message: '请输入执行评价', trigger: 'blur' }
        ],
        confirmResult: [
          { required: true, message: '请选择确认结果', trigger: 'change' }
        ],
        confirmer: [
          { required: true, message: '请输入确认人', trigger: 'blur' }
        ],
        confirmTime: [
          { required: true, message: '请选择确认时间', trigger: 'change' }
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
  methods: {
    // 获取指令类型文本
    getInstructionTypeText(type) {
      const typeMap = {
        compliance_check: '合规检查',
        risk_investigation: '风险排查',
        financial_audit: '财务审计',
        information_report: '信息报送',
        rectification_requirement: '整改要求'
      }
      return typeMap[type] || type
    },
    
    // 获取优先级标签类型
    getPriorityTagType(priority) {
      const priorityMap = {
        urgent: 'danger',
        high: 'warning',
        medium: 'primary',
        low: 'info'
      }
      return priorityMap[priority] || 'default'
    },
    
    // 获取优先级文本
    getPriorityText(priority) {
      const priorityMap = {
        urgent: '紧急',
        high: '高',
        medium: '中',
        low: '低'
      }
      return priorityMap[priority] || priority
    },
    
    // 确认
    handleConfirm() {
      this.$refs.confirmForm.validate((valid) => {
        if (valid) {
          const confirmResult = this.confirmData.confirmResult
          let message = ''
          
          switch (confirmResult) {
            case 'approved':
              message = '指令执行确认通过'
              break
            case 'conditional_approval':
              message = '指令执行有条件通过'
              break
            case 'rejected':
              message = '指令执行确认被拒绝'
              break
          }
          
          this.$emit('confirm', { ...this.confirmData })
          this.$message.success(message)
          this.handleClose()
        }
      })
    },
    
    // 关闭
    handleClose() {
      this.$refs.confirmForm.resetFields()
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.instruction-confirm-container {
  max-height: 600px;
  overflow-y: auto;
}

.instruction-card,
.result-card {
  margin-bottom: 20px;
}

.info-item {
  margin-bottom: 15px;
}

.info-label {
  font-weight: bold;
  color: #606266;
  display: inline-block;
  width: 100px;
}

.info-value {
  color: #303133;
}

.info-content {
  margin-top: 5px;
  padding: 10px;
  background-color: #f5f5f5;
  border-radius: 4px;
  color: #303133;
  line-height: 1.5;
}

.dialog-footer {
  text-align: right;
}
</style>
