<template>
  <div>
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      :disabled="isView"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="体系编码" prop="systemCode">
            <el-input v-model="form.systemCode" placeholder="请输入体系编码" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="体系名称" prop="systemName">
            <el-input v-model="form.systemName" placeholder="请输入体系名称" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="体系类型" prop="systemType">
            <el-select v-model="form.systemType" placeholder="请选择体系类型" style="width: 100%">
              <el-option label="综合预算" value="COMPREHENSIVE" />
              <el-option label="资本预算" value="CAPITAL" />
              <el-option label="现金预算" value="CASH" />
              <el-option label="滚动预算" value="ROLLING" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预算年度" prop="fiscalYear">
            <el-date-picker
              v-model="form.fiscalYear"
              type="year"
              placeholder="选择年度"
              value-format="yyyy"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="组织名称" prop="organizationName">
            <el-input v-model="form.organizationName" placeholder="请输入组织名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预算主体类型" prop="entityType">
            <el-select v-model="form.entityType" placeholder="请选择预算主体类型" style="width: 100%">
              <el-option label="法人实体" value="LEGAL_ENTITY" />
              <el-option label="业务单元" value="BUSINESS_UNIT" />
              <el-option label="成本中心" value="COST_CENTER" />
              <el-option label="利润中心" value="PROFIT_CENTER" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预算主体名称" prop="entityName">
            <el-input v-model="form.entityName" placeholder="请输入预算主体名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预算主体编码" prop="entityCode">
            <el-input v-model="form.entityCode" placeholder="请输入预算主体编码" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="基础币种" prop="baseCurrency">
            <el-select v-model="form.baseCurrency" placeholder="请选择基础币种" style="width: 100%">
              <el-option label="人民币" value="CNY" />
              <el-option label="美元" value="USD" />
              <el-option label="欧元" value="EUR" />
              <el-option label="日元" value="JPY" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="启用多币种">
            <el-switch
              v-model="form.enableMultiCurrency"
              :active-value="1"
              :inactive-value="0"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预算控制模式" prop="controlMode">
            <el-select v-model="form.controlMode" placeholder="请选择控制模式" style="width: 100%">
              <el-option label="严格控制" value="STRICT" />
              <el-option label="预警控制" value="WARNING" />
              <el-option label="无控制" value="NONE" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预警阈值(%)">
            <el-input-number
              v-model="form.warningThreshold"
              :min="0"
              :max="100"
              :precision="2"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="控制阈值(%)">
            <el-input-number
              v-model="form.controlThreshold"
              :min="0"
              :max="100"
              :precision="2"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="启用实时控制">
            <el-switch
              v-model="form.enableRealTimeControl"
              :active-value="1"
              :inactive-value="0"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="需要审批">
            <el-switch
              v-model="form.requireApproval"
              :active-value="1"
              :inactive-value="0"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审批级别" prop="approvalLevel">
            <el-select v-model="form.approvalLevel" placeholder="请选择审批级别" style="width: 100%">
              <el-option label="部门级" value="DEPARTMENT" />
              <el-option label="公司级" value="COMPANY" />
              <el-option label="集团级" value="GROUP" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="是否默认体系">
            <el-switch
              v-model="form.isDefault"
              :active-value="1"
              :inactive-value="0"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
              <el-option label="草稿" value="DRAFT" />
              <el-option label="激活" value="ACTIVE" />
              <el-option label="停用" value="INACTIVE" />
              <el-option label="归档" value="ARCHIVED" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="生效日期">
            <el-date-picker
              v-model="form.effectiveDate"
              type="datetime"
              placeholder="选择生效日期"
              value-format="yyyy-MM-dd HH:mm:ss"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="失效日期">
            <el-date-picker
              v-model="form.expiryDate"
              type="datetime"
              placeholder="选择失效日期"
              value-format="yyyy-MM-dd HH:mm:ss"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="体系描述">
        <el-input
          v-model="form.systemDescription"
          type="textarea"
          :rows="3"
          placeholder="请输入体系描述"
        />
      </el-form-item>
    </el-form>

    <div v-if="!isView" slot="footer" class="dialog-footer" style="text-align: right; margin-top: 20px;">
      <el-button @click="handleCancel">取消</el-button>
      <el-button type="primary" @click="handleSubmit">确定</el-button>
    </div>
  </div>
</template>

<script>
import { createBudgetSystem, updateBudgetSystem, checkSystemCodeExists } from '@/api/managementAccountant/eps/budgetSystem'

export default {
  name: 'BudgetSystemForm',
  props: {
    formData: {
      type: Object,
      default: () => ({})
    },
    isEdit: {
      type: Boolean,
      default: false
    },
    isView: {
      type: Boolean,
      default: false
    }
  },
  data() {
    // 体系编码验证
    const validateSystemCode = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入体系编码'))
      } else {
        const excludeId = this.isEdit ? this.form.systemId : null
        checkSystemCodeExists({ systemCode: value, excludeId }).then(response => {
          if (response.code === 1 && response.data) {
            callback(new Error('体系编码已存在'))
          } else {
            callback()
          }
        }).catch(() => {
          callback()
        })
      }
    }

    return {
      form: {
        systemId: null,
        systemCode: '',
        systemName: '',
        systemDescription: '',
        systemType: '',
        fiscalYear: '',
        organizationId: null,
        organizationName: '',
        entityType: '',
        entityName: '',
        entityCode: '',
        baseCurrency: 'CNY',
        enableMultiCurrency: 0,
        controlMode: 'WARNING',
        warningThreshold: 80,
        controlThreshold: 100,
        enableRealTimeControl: 0,
        requireApproval: 0,
        approvalLevel: 'DEPARTMENT',
        status: 'DRAFT',
        isDefault: 0,
        effectiveDate: '',
        expiryDate: ''
      },
      rules: {
        systemCode: [
          { required: true, message: '请输入体系编码', trigger: 'blur' },
          { validator: validateSystemCode, trigger: 'blur' }
        ],
        systemName: [
          { required: true, message: '请输入体系名称', trigger: 'blur' }
        ],
        systemType: [
          { required: true, message: '请选择体系类型', trigger: 'change' }
        ],
        fiscalYear: [
          { required: true, message: '请选择预算年度', trigger: 'change' }
        ],
        organizationName: [
          { required: true, message: '请输入组织名称', trigger: 'blur' }
        ],
        entityType: [
          { required: true, message: '请选择预算主体类型', trigger: 'change' }
        ],
        entityName: [
          { required: true, message: '请输入预算主体名称', trigger: 'blur' }
        ],
        entityCode: [
          { required: true, message: '请输入预算主体编码', trigger: 'blur' }
        ],
        baseCurrency: [
          { required: true, message: '请选择基础币种', trigger: 'change' }
        ],
        controlMode: [
          { required: true, message: '请选择预算控制模式', trigger: 'change' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' }
        ]
      }
    }
  },
  watch: {
    formData: {
      handler(newVal) {
        if (newVal) {
          this.form = { ...this.form, ...newVal }
        }
      },
      immediate: true,
      deep: true
    }
  },
  methods: {
    // 提交表单
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const submitData = { ...this.form }
          
          if (this.isEdit) {
            updateBudgetSystem(submitData).then(response => {
              if (response.code === 1) {
                this.$message.success('更新成功')
                this.$emit('submit')
              } else {
                this.$message.error(response.msg || '更新失败')
              }
            })
          } else {
            createBudgetSystem(submitData).then(response => {
              if (response.code === 1) {
                this.$message.success('创建成功')
                this.$emit('submit')
              } else {
                this.$message.error(response.msg || '创建失败')
              }
            })
          }
        }
      })
    },

    // 取消
    handleCancel() {
      this.$emit('cancel')
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
  margin-top: 20px;
}
</style>
