<template>
  <el-dialog
    title="新增业务规则"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="ruleForm"
      :model="ruleForm"
      :rules="rules"
      label-width="120px"
      class="rule-form"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="规则编码" prop="ruleCode">
            <el-input
              v-model="ruleForm.ruleCode"
              placeholder="请输入规则编码"
              :disabled="dialogStatus === 'edit'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="规则名称" prop="ruleName">
            <el-input
              v-model="ruleForm.ruleName"
              placeholder="请输入规则名称"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="规则类型" prop="ruleType">
            <el-select
              v-model="ruleForm.ruleType"
              placeholder="请选择规则类型"
              style="width: 100%"
            >
              <el-option label="审批规则" value="APPROVAL" />
              <el-option label="风控规则" value="RISK_CONTROL" />
              <el-option label="业务规则" value="BUSINESS" />
              <el-option label="计算规则" value="CALCULATION" />
              <el-option label="验证规则" value="VALIDATION" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="适用模块" prop="moduleCode">
            <el-select
              v-model="ruleForm.moduleCode"
              placeholder="请选择适用模块"
              style="width: 100%"
            >
              <el-option label="账户管理" value="ACCOUNT" />
              <el-option label="资金管理" value="FUND" />
              <el-option label="投资理财" value="INVESTMENT" />
              <el-option label="风险管理" value="RISK" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="优先级" prop="priority">
            <el-select
              v-model="ruleForm.priority"
              placeholder="请选择优先级"
              style="width: 100%"
            >
              <el-option label="高" value="HIGH" />
              <el-option label="中" value="MEDIUM" />
              <el-option label="低" value="LOW" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="规则状态" prop="ruleStatus">
            <el-select
              v-model="ruleForm.ruleStatus"
              placeholder="请选择规则状态"
              style="width: 100%"
            >
              <el-option label="启用" value="ENABLED" />
              <el-option label="禁用" value="DISABLED" />
              <el-option label="测试中" value="TESTING" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="生效时间" prop="effectiveTime">
            <el-date-picker
              v-model="ruleForm.effectiveTime"
              type="datetime"
              placeholder="选择生效时间"
              style="width: 100%"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="失效时间" prop="expireTime">
            <el-date-picker
              v-model="ruleForm.expireTime"
              type="datetime"
              placeholder="选择失效时间"
              style="width: 100%"
              format="yyyy-MM-dd HH:mm:ss"
              value-format="yyyy-MM-dd HH:mm:ss"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="规则描述" prop="description">
        <el-input
          v-model="ruleForm.description"
          type="textarea"
          :rows="4"
          placeholder="请输入规则描述"
        />
      </el-form-item>

      <el-form-item label="规则内容" prop="ruleContent">
        <el-input
          v-model="ruleForm.ruleContent"
          type="textarea"
          :rows="6"
          placeholder="请输入规则内容或条件表达式"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="handleSave" :loading="saving">
        {{ dialogStatus === 'create' ? '创 建' : '更 新' }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'BusinessRuleDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    ruleData: {
      type: Object,
      default: () => ({})
    },
    status: {
      type: String,
      default: 'create'
    }
  },
  data() {
    return {
      dialogVisible: this.visible,
      dialogStatus: this.status,
      saving: false,
      ruleForm: {
        ruleId: null,
        ruleCode: '',
        ruleName: '',
        ruleType: '',
        moduleCode: '',
        priority: '',
        ruleStatus: 'ENABLED',
        effectiveTime: '',
        expireTime: '',
        description: '',
        ruleContent: '',
        createTime: ''
      },
      rules: {
        ruleCode: [
          { required: true, message: '请输入规则编码', trigger: 'blur' },
          { pattern: /^[A-Z][A-Z0-9_]*$/, message: '规则编码格式不正确', trigger: 'blur' }
        ],
        ruleName: [
          { required: true, message: '请输入规则名称', trigger: 'blur' },
          { min: 2, max: 100, message: '规则名称长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        ruleType: [
          { required: true, message: '请选择规则类型', trigger: 'change' }
        ],
        moduleCode: [
          { required: true, message: '请选择适用模块', trigger: 'change' }
        ],
        priority: [
          { required: true, message: '请选择优先级', trigger: 'change' }
        ],
        ruleStatus: [
          { required: true, message: '请选择规则状态', trigger: 'change' }
        ],
        effectiveTime: [
          { required: true, message: '请选择生效时间', trigger: 'change' }
        ],
        description: [
          { max: 500, message: '描述长度不能超过500个字符', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        // 用 status prop 直接判断，避免 dialogStatus 异步更新导致判断错误
        this.initForm(this.status)
      }
    },
    ruleData: {
      handler(newVal) {
        // 只在编辑模式下用传入数据填充表单，新增模式由 initForm 负责清空
        if (newVal && Object.keys(newVal).length > 0 && this.status === 'edit') {
          this.ruleForm = { ...newVal }
        }
      },
      deep: true,
      immediate: true
    },
    status(newVal) {
      this.dialogStatus = newVal
    }
  },
  methods: {
    initForm(status) {
      const currentStatus = status || this.dialogStatus
      if (currentStatus === 'create') {
        this.ruleForm = {
          ruleId: null,
          ruleCode: '',
          ruleName: '',
          ruleType: '',
          moduleCode: '',
          priority: '',
          ruleStatus: 'ENABLED',
          effectiveTime: '',
          expireTime: '',
          description: '',
          ruleContent: '',
          createTime: ''
        }
      } else if (currentStatus === 'edit' && this.ruleData && Object.keys(this.ruleData).length > 0) {
        this.ruleForm = { ...this.ruleData }
      }
      this.$nextTick(() => {
        this.$refs.ruleForm && this.$refs.ruleForm.clearValidate()
      })
    },
    handleClose() {
      this.dialogVisible = false
      this.$emit('update:visible', false)
      this.$emit('close')
    },
    handleSave() {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          // 表单校验通过，把数据交给父组件处理（调API）
          this.$emit('success', { ...this.ruleForm })
          this.handleClose()
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.rule-form {
  .el-form-item {
    margin-bottom: 18px;
  }
}

.dialog-footer {
  text-align: right;
}
</style>