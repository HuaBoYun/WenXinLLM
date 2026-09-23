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
          <el-form-item label="模板编码" prop="templateCode">
            <el-input v-model="form.templateCode" placeholder="请输入模板编码" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模板名称" prop="templateName">
            <el-input v-model="form.templateName" placeholder="请输入模板名称" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="模板类型" prop="templateType">
            <el-select v-model="form.templateType" placeholder="请选择模板类型" style="width: 100%">
              <el-option label="标准模板" value="STANDARD" />
              <el-option label="自定义模板" value="CUSTOM" />
              <el-option label="行业模板" value="INDUSTRY" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模板分类" prop="templateCategory">
            <el-select v-model="form.templateCategory" placeholder="请选择模板分类" style="width: 100%">
              <el-option label="收入预算" value="REVENUE" />
              <el-option label="成本预算" value="COST" />
              <el-option label="费用预算" value="EXPENSE" />
              <el-option label="资本预算" value="CAPEX" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="适用组织类型" prop="applicableOrgType">
            <el-select v-model="form.applicableOrgType" placeholder="请选择适用组织类型" style="width: 100%">
              <el-option label="全部" value="ALL" />
              <el-option label="公司" value="COMPANY" />
              <el-option label="部门" value="DEPARTMENT" />
              <el-option label="项目" value="PROJECT" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="适用行业" prop="applicableIndustry">
            <el-select v-model="form.applicableIndustry" placeholder="请选择适用行业" style="width: 100%">
              <el-option label="全部" value="ALL" />
              <el-option label="制造业" value="MANUFACTURING" />
              <el-option label="服务业" value="SERVICE" />
              <el-option label="零售业" value="RETAIL" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="版本号" prop="versionNumber">
            <el-input v-model="form.versionNumber" placeholder="请输入版本号" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="排序号">
            <el-input-number
              v-model="form.sortOrder"
              :min="0"
              :max="9999"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="是否系统模板">
            <el-switch
              v-model="form.isSystem"
              :active-value="1"
              :inactive-value="0"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="是否可编辑">
            <el-switch
              v-model="form.isEditable"
              :active-value="1"
              :inactive-value="0"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="是否共享">
            <el-switch
              v-model="form.isShared"
              :active-value="1"
              :inactive-value="0"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="是否默认模板">
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

      <el-form-item label="标签">
        <el-input
          v-model="form.tags"
          placeholder="请输入标签，多个标签用逗号分隔"
        />
      </el-form-item>

      <el-form-item label="模板描述">
        <el-input
          v-model="form.templateDescription"
          type="textarea"
          :rows="3"
          placeholder="请输入模板描述"
        />
      </el-form-item>

      <!-- 高级配置 -->
      <el-collapse v-if="!isView">
        <el-collapse-item title="高级配置" name="advanced">
          <el-form-item label="表头配置">
            <el-input
              v-model="form.headerConfig"
              type="textarea"
              :rows="3"
              placeholder="请输入表头配置（JSON格式）"
            />
          </el-form-item>
          
          <el-form-item label="列配置">
            <el-input
              v-model="form.columnConfig"
              type="textarea"
              :rows="3"
              placeholder="请输入列配置（JSON格式）"
            />
          </el-form-item>
          
          <el-form-item label="行配置">
            <el-input
              v-model="form.rowConfig"
              type="textarea"
              :rows="3"
              placeholder="请输入行配置（JSON格式）"
            />
          </el-form-item>
          
          <el-form-item label="样式配置">
            <el-input
              v-model="form.styleConfig"
              type="textarea"
              :rows="3"
              placeholder="请输入样式配置（JSON格式）"
            />
          </el-form-item>
          
          <el-form-item label="验证规则">
            <el-input
              v-model="form.validationRules"
              type="textarea"
              :rows="3"
              placeholder="请输入验证规则（JSON格式）"
            />
          </el-form-item>
          
          <el-form-item label="计算公式">
            <el-input
              v-model="form.calculationFormulas"
              type="textarea"
              :rows="3"
              placeholder="请输入计算公式（JSON格式）"
            />
          </el-form-item>
          
          <el-form-item label="数据源配置">
            <el-input
              v-model="form.dataSourceConfig"
              type="textarea"
              :rows="3"
              placeholder="请输入数据源配置（JSON格式）"
            />
          </el-form-item>
          
          <el-form-item label="权限配置">
            <el-input
              v-model="form.permissionConfig"
              type="textarea"
              :rows="3"
              placeholder="请输入权限配置（JSON格式）"
            />
          </el-form-item>
        </el-collapse-item>
      </el-collapse>
    </el-form>

    <div v-if="!isView" slot="footer" class="dialog-footer" style="text-align: right; margin-top: 20px;">
      <el-button @click="handleCancel">取消</el-button>
      <el-button type="primary" @click="handleSubmit">确定</el-button>
    </div>
  </div>
</template>

<script>
import { createBudgetTemplate, updateBudgetTemplate, checkTemplateCodeExists } from '@/api/managementAccountant/eps/budgetTemplate'

export default {
  name: 'BudgetTemplateForm',
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
    // 模板编码验证
    const validateTemplateCode = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入模板编码'))
      } else {
        const excludeId = this.isEdit ? this.form.templateId : null
        checkTemplateCodeExists({ templateCode: value, excludeId }).then(response => {
          if (response.code === 1 && response.data) {
            callback(new Error('模板编码已存在'))
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
        templateId: null,
        systemId: null,
        templateCode: '',
        templateName: '',
        templateDescription: '',
        templateType: 'CUSTOM',
        templateCategory: '',
        applicableOrgType: 'ALL',
        applicableIndustry: 'ALL',
        versionNumber: '1.0',
        sortOrder: 0,
        isSystem: 0,
        isEditable: 1,
        isShared: 0,
        isDefault: 0,
        status: 'DRAFT',
        effectiveDate: '',
        expiryDate: '',
        tags: '',
        headerConfig: '',
        columnConfig: '',
        rowConfig: '',
        styleConfig: '',
        validationRules: '',
        calculationFormulas: '',
        dataSourceConfig: '',
        permissionConfig: ''
      },
      rules: {
        templateCode: [
          { required: true, message: '请输入模板编码', trigger: 'blur' },
          { validator: validateTemplateCode, trigger: 'blur' }
        ],
        templateName: [
          { required: true, message: '请输入模板名称', trigger: 'blur' }
        ],
        templateType: [
          { required: true, message: '请选择模板类型', trigger: 'change' }
        ],
        templateCategory: [
          { required: true, message: '请选择模板分类', trigger: 'change' }
        ],
        applicableOrgType: [
          { required: true, message: '请选择适用组织类型', trigger: 'change' }
        ],
        applicableIndustry: [
          { required: true, message: '请选择适用行业', trigger: 'change' }
        ],
        versionNumber: [
          { required: true, message: '请输入版本号', trigger: 'blur' }
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
            updateBudgetTemplate(submitData).then(response => {
              if (response.code === 1) {
                this.$message.success('更新成功')
                this.$emit('submit')
              } else {
                this.$message.error(response.msg || '更新失败')
              }
            })
          } else {
            createBudgetTemplate(submitData).then(response => {
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
