<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="ruleForm"
      :model="ruleForm"
      :rules="ruleRules"
      label-width="120px"
      v-loading="loading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="规则名称" prop="ruleName">
            <el-input
              v-model="ruleForm.ruleName"
              placeholder="请输入规则名称"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="规则编码" prop="ruleCode">
            <el-input
              v-model="ruleForm.ruleCode"
              placeholder="请输入规则编码"
              :disabled="dialogType === 'view'"
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
              :disabled="dialogType === 'view'"
            >
              <el-option label="股权监管规则" value="EQUITY"></el-option>
              <el-option label="资产监管规则" value="ASSET"></el-option>
              <el-option label="财务监管规则" value="FINANCIAL"></el-option>
              <el-option label="风险监管规则" value="RISK"></el-option>
              <el-option label="合规监管规则" value="COMPLIANCE"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="规则状态" prop="status">
            <el-select
              v-model="ruleForm.status"
              placeholder="请选择状态"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            >
              <el-option label="启用" value="ACTIVE"></el-option>
              <el-option label="停用" value="INACTIVE"></el-option>
              <el-option label="草稿" value="DRAFT"></el-option>
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
              :disabled="dialogType === 'view'"
            >
              <el-option label="高" value="HIGH"></el-option>
              <el-option label="中" value="MEDIUM"></el-option>
              <el-option label="低" value="LOW"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="生效日期" prop="effectiveDate">
            <el-date-picker
              v-model="ruleForm.effectiveDate"
              type="date"
              placeholder="选择生效日期"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="失效日期" prop="expiryDate">
            <el-date-picker
              v-model="ruleForm.expiryDate"
              type="date"
              placeholder="选择失效日期"
              style="width: 100%"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="创建人" prop="creator">
            <el-input
              v-model="ruleForm.creator"
              placeholder="请输入创建人"
              :disabled="dialogType === 'view'"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="规则描述" prop="description">
        <el-input
          v-model="ruleForm.description"
          type="textarea"
          :rows="3"
          placeholder="请输入规则描述"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="规则条件" prop="ruleCondition">
        <el-input
          v-model="ruleForm.ruleCondition"
          type="textarea"
          :rows="4"
          placeholder="请输入规则条件（支持JSON格式）"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="执行动作" prop="ruleAction">
        <el-input
          v-model="ruleForm.ruleAction"
          type="textarea"
          :rows="3"
          placeholder="请输入执行动作"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>

      <el-form-item label="触发频率" prop="triggerFrequency">
        <el-select
          v-model="ruleForm.triggerFrequency"
          placeholder="请选择触发频率"
          style="width: 100%"
          :disabled="dialogType === 'view'"
        >
          <el-option label="实时" value="REALTIME"></el-option>
          <el-option label="每日" value="DAILY"></el-option>
          <el-option label="每周" value="WEEKLY"></el-option>
          <el-option label="每月" value="MONTHLY"></el-option>
          <el-option label="手动" value="MANUAL"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="适用范围" prop="applicableScope">
        <el-checkbox-group v-model="ruleForm.applicableScope" :disabled="dialogType === 'view'">
          <el-checkbox label="CENTRAL_ENTERPRISE">央企</el-checkbox>
          <el-checkbox label="STATE_ENTERPRISE">国企</el-checkbox>
          <el-checkbox label="LISTED_COMPANY">上市公司</el-checkbox>
          <el-checkbox label="FINANCIAL_INSTITUTION">金融机构</el-checkbox>
          <el-checkbox label="OTHER">其他</el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item label="通知方式" prop="notificationMethod">
        <el-checkbox-group v-model="ruleForm.notificationMethod" :disabled="dialogType === 'view'">
          <el-checkbox label="EMAIL">邮件</el-checkbox>
          <el-checkbox label="SMS">短信</el-checkbox>
          <el-checkbox label="SYSTEM">系统通知</el-checkbox>
          <el-checkbox label="WECHAT">微信</el-checkbox>
        </el-checkbox-group>
      </el-form-item>

      <el-form-item label="备注" prop="remarks">
        <el-input
          v-model="ruleForm.remarks"
          type="textarea"
          :rows="2"
          placeholder="请输入备注信息"
          :disabled="dialogType === 'view'"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button
        v-if="dialogType !== 'view'"
        type="primary"
        @click="handleSubmit"
        :loading="loading"
      >
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveSupervisionRule } from '@/api/stateAssets/supervisionConfig'

export default {
  name: 'SupervisionRuleDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    ruleData: {
      type: Object,
      default: () => ({})
    },
    dialogType: {
      type: String,
      default: 'add' // add, edit, view
    }
  },
  data() {
    return {
      loading: false,
      ruleForm: {
        id: '',
        ruleName: '',
        ruleCode: '',
        ruleType: '',
        status: 'ACTIVE',
        priority: 'MEDIUM',
        effectiveDate: '',
        expiryDate: '',
        creator: '',
        description: '',
        ruleCondition: '',
        ruleAction: '',
        triggerFrequency: 'DAILY',
        applicableScope: [],
        notificationMethod: [],
        remarks: ''
      },
      ruleRules: {
        ruleName: [
          { required: true, message: '请输入规则名称', trigger: 'blur' }
        ],
        ruleCode: [
          { required: true, message: '请输入规则编码', trigger: 'blur' }
        ],
        ruleType: [
          { required: true, message: '请选择规则类型', trigger: 'change' }
        ],
        status: [
          { required: true, message: '请选择规则状态', trigger: 'change' }
        ],
        priority: [
          { required: true, message: '请选择优先级', trigger: 'change' }
        ],
        effectiveDate: [
          { required: true, message: '请选择生效日期', trigger: 'change' }
        ],
        creator: [
          { required: true, message: '请输入创建人', trigger: 'blur' }
        ],
        description: [
          { required: true, message: '请输入规则描述', trigger: 'blur' }
        ],
        ruleCondition: [
          { required: true, message: '请输入规则条件', trigger: 'blur' }
        ],
        ruleAction: [
          { required: true, message: '请输入执行动作', trigger: 'blur' }
        ],
        triggerFrequency: [
          { required: true, message: '请选择触发频率', trigger: 'change' }
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
    },
    dialogTitle() {
      const titleMap = {
        add: '新建监管规则',
        edit: '编辑监管规则',
        view: '查看监管规则'
      }
      return titleMap[this.dialogType] || '新建监管规则'
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
      if (this.dialogType === 'add') {
        this.ruleForm = {
          id: '',
          ruleName: '',
          ruleCode: '',
          ruleType: '',
          status: 'ACTIVE',
          priority: 'MEDIUM',
          effectiveDate: '',
          expiryDate: '',
          creator: '',
          description: '',
          ruleCondition: '',
          ruleAction: '',
          triggerFrequency: 'DAILY',
          applicableScope: [],
          notificationMethod: [],
          remarks: ''
        }
      } else {
        this.ruleForm = { ...this.ruleData }
        // 确保数组字段正确初始化
        this.ruleForm.applicableScope = this.ruleData.applicableScope || []
        this.ruleForm.notificationMethod = this.ruleData.notificationMethod || []
      }
      
      this.$nextTick(() => {
        if (this.$refs.ruleForm) {
          this.$refs.ruleForm.clearValidate()
        }
      })
    },

    handleSubmit() {
      this.$refs.ruleForm.validate((valid) => {
        if (valid) {
          this.loading = true
          saveSupervisionRule(this.ruleForm).then(response => {
            if (response.code === 1) {
              this.$message.success(response.msg || '操作成功')
              this.handleClose()
              this.$emit('refresh')
            } else {
              this.$message.error(response.msg || '操作失败')
            }
            this.loading = false
          }).catch(error => {
            console.error('保存监管规则失败:', error)
            this.$message.error('操作失败')
            this.loading = false
          })
        }
      })
    },

    handleClose() {
      this.dialogVisible = false
      this.loading = false
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>
