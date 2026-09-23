<template>
  <div class="batch-process-form">
    <div class="selected-warnings">
      <h4>已选择预警 ({{ selectedWarnings.length }})</h4>
      <el-table :data="selectedWarnings" border stripe max-height="200">
        <el-table-column prop="warningCode" label="预警编码" width="140" />
        <el-table-column prop="warningType" label="预警类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getWarningTypeTagType(scope.row.warningType)">
              {{ getWarningTypeText(scope.row.warningType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="warningLevel" label="预警级别" width="100">
          <template slot-scope="scope">
            <el-tag :type="getWarningLevelTagType(scope.row.warningLevel)">
              {{ getWarningLevelText(scope.row.warningLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="companyName" label="企业名称" show-overflow-tooltip />
      </el-table>
    </div>

    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      style="margin-top: 20px;"
    >
      <el-form-item label="处理动作" prop="processAction">
        <el-radio-group v-model="form.processAction">
          <el-radio label="CONFIRM">确认处理</el-radio>
          <el-radio label="IGNORE">忽略预警</el-radio>
          <el-radio label="ESCALATE">升级预警</el-radio>
          <el-radio label="FALSE_POSITIVE">标记误报</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="处理说明" prop="processNote">
        <el-input
          v-model="form.processNote"
          type="textarea"
          :rows="4"
          placeholder="请输入处理说明"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>

      <el-form-item label="处理优先级" prop="processPriority">
        <el-select v-model="form.processPriority" placeholder="请选择处理优先级" style="width: 100%">
          <el-option label="高优先级" value="HIGH" />
          <el-option label="中优先级" value="MEDIUM" />
          <el-option label="低优先级" value="LOW" />
        </el-select>
      </el-form-item>

      <el-form-item label="通知相关人员">
        <el-checkbox-group v-model="form.notifyUsers">
          <el-checkbox label="risk_manager">风险管理员</el-checkbox>
          <el-checkbox label="department_head">部门负责人</el-checkbox>
          <el-checkbox label="company_admin">企业管理员</el-checkbox>
          <el-checkbox label="system_admin">系统管理员</el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item label="预计完成时间" v-if="form.processAction === 'CONFIRM'">
        <el-date-picker
          v-model="form.expectedCompleteTime"
          type="datetime"
          placeholder="选择预计完成时间"
          style="width: 100%"
        />
      </el-form-item>

      <el-form-item label="升级原因" prop="escalateReason" v-if="form.processAction === 'ESCALATE'">
        <el-input
          v-model="form.escalateReason"
          type="textarea"
          :rows="3"
          placeholder="请输入升级原因"
          maxlength="200"
          show-word-limit
        />
      </el-form-item>

      <el-form-item label="误报原因" prop="falsePositiveReason" v-if="form.processAction === 'FALSE_POSITIVE'">
        <el-input
          v-model="form.falsePositiveReason"
          type="textarea"
          :rows="3"
          placeholder="请输入误报原因"
          maxlength="200"
          show-word-limit
        />
      </el-form-item>

      <el-form-item label="后续跟进">
        <el-checkbox v-model="form.needFollowUp">需要后续跟进</el-checkbox>
      </el-form-item>

      <el-form-item label="跟进计划" v-if="form.needFollowUp">
        <el-input
          v-model="form.followUpPlan"
          type="textarea"
          :rows="3"
          placeholder="请输入跟进计划"
          maxlength="300"
          show-word-limit
        />
      </el-form-item>
    </el-form>

    <div class="form-footer">
      <el-button @click="handleCancel">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitting">
        确认处理
      </el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'BatchProcessForm',
  props: {
    selectedWarnings: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      submitting: false,
      form: {
        processAction: 'CONFIRM',
        processNote: '',
        processPriority: 'MEDIUM',
        notifyUsers: [],
        expectedCompleteTime: null,
        escalateReason: '',
        falsePositiveReason: '',
        needFollowUp: false,
        followUpPlan: ''
      },
      rules: {
        processAction: [
          { required: true, message: '请选择处理动作', trigger: 'change' }
        ],
        processNote: [
          { required: true, message: '请输入处理说明', trigger: 'blur' },
          { min: 5, max: 500, message: '长度在 5 到 500 个字符', trigger: 'blur' }
        ],
        processPriority: [
          { required: true, message: '请选择处理优先级', trigger: 'change' }
        ],
        escalateReason: [
          { required: true, message: '请输入升级原因', trigger: 'blur' }
        ],
        falsePositiveReason: [
          { required: true, message: '请输入误报原因', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    // 提交表单
    handleSubmit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.submitting = true
          
          // 构建提交数据
          const submitData = {
            ...this.form,
            warningIds: this.selectedWarnings.map(item => item.warningId)
          }
          
          this.$emit('submit', submitData)
          this.submitting = false
        } else {
          this.$message.error('请完善表单信息')
        }
      })
    },
    // 取消
    handleCancel() {
      this.$emit('cancel')
    },
    // 重置表单
    resetForm() {
      this.$refs.form.resetFields()
      this.form = {
        processAction: 'CONFIRM',
        processNote: '',
        processPriority: 'MEDIUM',
        notifyUsers: [],
        expectedCompleteTime: null,
        escalateReason: '',
        falsePositiveReason: '',
        needFollowUp: false,
        followUpPlan: ''
      }
    },
    // 获取预警类型标签类型
    getWarningTypeTagType(type) {
      const typeMap = {
        'FINANCIAL_RISK': 'success',
        'PROCUREMENT_RISK': 'primary',
        'CREDIT_RISK': 'warning',
        'COMPLIANCE_RISK': 'info'
      }
      return typeMap[type] || 'default'
    },
    // 获取预警类型文本
    getWarningTypeText(type) {
      const typeMap = {
        'FINANCIAL_RISK': '财务风险',
        'PROCUREMENT_RISK': '采购风险',
        'CREDIT_RISK': '信用风险',
        'COMPLIANCE_RISK': '合规风险'
      }
      return typeMap[type] || type
    },
    // 获取预警级别标签类型
    getWarningLevelTagType(level) {
      const levelMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'success'
      }
      return levelMap[level] || 'default'
    },
    // 获取预警级别文本
    getWarningLevelText(level) {
      const levelMap = {
        'HIGH': '高风险',
        'MEDIUM': '中风险',
        'LOW': '低风险'
      }
      return levelMap[level] || level
    }
  }
}
</script>

<style lang="scss" scoped>
.batch-process-form {
  .selected-warnings {
    h4 {
      margin: 0 0 15px 0;
      color: #333;
    }
  }

  .form-footer {
    text-align: right;
    margin-top: 20px;
    padding-top: 20px;
    border-top: 1px solid #ebeef5;
    
    .el-button {
      margin-left: 10px;
    }
  }
}
</style>
