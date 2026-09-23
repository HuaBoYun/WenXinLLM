<template>
  <el-dialog
    :title="formTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="internalFormData"
      :rules="formRules"
      label-width="120px"
      :disabled="formType === 'view'"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="影响因素编码" prop="factorCode">
            <el-input
              v-model="internalFormData.factorCode"
              placeholder="请输入影响因素编码"
              maxlength="50"
              show-word-limit
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="影响因素名称" prop="factorName">
            <el-input
              v-model="internalFormData.factorName"
              placeholder="请输入影响因素名称"
              maxlength="200"
              show-word-limit
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="影响因素类型" prop="factorType">
            <el-select
              v-model="internalFormData.factorType"
              placeholder="请选择影响因素类型"
              style="width: 100%"
            >
              <el-option
                v-for="(name, value) in FACTOR_TYPE_NAME"
                :key="value"
                :label="name"
                :value="parseInt(value)"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="数据类型" prop="dataType">
            <el-select
              v-model="internalFormData.dataType"
              placeholder="请选择数据类型"
              style="width: 100%"
              @change="handleDataTypeChange"
            >
              <el-option
                v-for="(name, value) in DATA_TYPE_NAME"
                :key="value"
                :label="name"
                :value="parseInt(value)"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="数据长度" prop="dataLength">
            <el-input-number
              v-model="internalFormData.dataLength"
              :min="1"
              :max="4000"
              placeholder="请输入数据长度"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            v-if="internalFormData.dataType === DATA_TYPE.NUMBER"
            label="小数位数"
            prop="decimalPlaces"
          >
            <el-input-number
              v-model="internalFormData.decimalPlaces"
              :min="0"
              :max="10"
              placeholder="请输入小数位数"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="是否必填">
            <el-radio-group v-model="internalFormData.isRequired">
              <el-radio
                v-for="(name, value) in REQUIRED_STATUS_NAME"
                :key="value"
                :label="parseInt(value)"
              >
                {{ name }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否启用">
            <el-radio-group v-model="internalFormData.isEnabled">
              <el-radio
                v-for="(name, value) in ENABLED_STATUS_NAME"
                :key="value"
                :label="parseInt(value)"
              >
                {{ name }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="默认值">
            <el-input
              v-model="internalFormData.defaultValue"
              placeholder="请输入默认值"
              maxlength="500"
              show-word-limit
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="排序号">
            <el-input-number
              v-model="internalFormData.sortOrder"
              :min="1"
              :max="9999"
              placeholder="请输入排序号"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item label="取值范围">
            <el-input
              v-model="internalFormData.valueRange"
              type="textarea"
              :rows="3"
              placeholder="请输入取值范围说明"
              maxlength="1000"
              show-word-limit
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item label="验证规则">
            <el-input
              v-model="internalFormData.validationRule"
              type="textarea"
              :rows="3"
              placeholder="请输入验证规则"
              maxlength="1000"
              show-word-limit
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item label="描述">
            <el-input
              v-model="internalFormData.description"
              type="textarea"
              :rows="4"
              placeholder="请输入描述信息"
              maxlength="1000"
              show-word-limit
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button v-if="formType !== 'view'" type="primary" :loading="loading" @click="handleSubmit">
        保存
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { influenceFactorApi } from '@/api/financialSharing'
import {
  FACTOR_TYPE_NAME,
  DATA_TYPE,
  DATA_TYPE_NAME,
  ENABLED_STATUS_NAME,
  REQUIRED_STATUS_NAME,
  DEFAULT_TENANT_CONFIG
} from '../../consts'

export default {
  name: 'InfluenceFactorForm',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    formType: {
      type: String,
      default: 'add' // add, edit, view
    },
    formData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      // 常量
      FACTOR_TYPE_NAME,
      DATA_TYPE,
      DATA_TYPE_NAME,
      ENABLED_STATUS_NAME,
      REQUIRED_STATUS_NAME,
      
      // 内部表单数据
      internalFormData: {
        factorId: null,
        factorCode: '',
        factorName: '',
        factorType: null,
        dataType: null,
        dataLength: null,
        decimalPlaces: null,
        isRequired: 0,
        defaultValue: '',
        valueRange: '',
        validationRule: '',
        description: '',
        isEnabled: 1,
        sortOrder: null,
        ...DEFAULT_TENANT_CONFIG,
        version: 1
      },
      
      // 表单验证规则
      formRules: {
        factorCode: [
          { required: true, message: '请输入影响因素编码', trigger: 'blur' },
          { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' },
          { validator: this.validateFactorCode, trigger: 'blur' }
        ],
        factorName: [
          { required: true, message: '请输入影响因素名称', trigger: 'blur' },
          { min: 1, max: 200, message: '长度在 1 到 200 个字符', trigger: 'blur' }
        ],
        factorType: [
          { required: true, message: '请选择影响因素类型', trigger: 'change' }
        ],
        dataType: [
          { required: true, message: '请选择数据类型', trigger: 'change' }
        ]
      },
      
      // 加载状态
      loading: false
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
    
    formTitle() {
      const titles = {
        add: '新增影响因素',
        edit: '编辑影响因素',
        view: '查看影响因素'
      }
      return titles[this.formType] || '影响因素'
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
    // 初始化表单
    initForm() {
      this.internalFormData = {
        ...this.internalFormData,
        ...this.formData
      }

      // 清除验证
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
      })
    },

    // 数据类型变化处理
    handleDataTypeChange(value) {
      if (value !== DATA_TYPE.NUMBER) {
        // 非数值类型时清空小数位数
        this.internalFormData.decimalPlaces = null
      }
    },

    // 验证影响因素编码
    validateFactorCode(rule, value, callback) {
      if (!value) {
        callback()
        return
      }

      // 检查编码是否已存在
      influenceFactorApi.checkCodeExists(
        value,
        this.internalFormData.tenantId,
        this.internalFormData.bookId,
        this.internalFormData.factorId
      )
        .then(response => {
          if (response.data.code === 1) {
            if (response.data.data) {
              callback(new Error('影响因素编码已存在'))
            } else {
              callback()
            }
          } else {
            callback()
          }
        })
        .catch(() => {
          callback()
        })
    },

    // 提交表单
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.loading = true
          const isEdit = this.formType === 'edit'
          const apiMethod = isEdit ? influenceFactorApi.update : influenceFactorApi.save

          apiMethod(this.internalFormData)
            .then(response => {
              if (response.data.code === 1) {
                this.$message.success(isEdit ? '更新成功' : '保存成功')
                this.$emit('success')
              } else {
                this.$message.error(response.data.msg || (isEdit ? '更新失败' : '保存失败'))
              }
            })
            .catch(error => {
              console.error('保存影响因素失败:', error)
              this.$message.error(isEdit ? '更新失败' : '保存失败')
            })
            .finally(() => {
              this.loading = false
            })
        }
      })
    },

    // 关闭弹窗
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>
