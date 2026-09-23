<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="900px"
    :before-close="handleClose"
    :close-on-click-modal="false"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      :disabled="formType === 'view'"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="模型编码" prop="modelCode">
            <el-input v-model="form.modelCode" placeholder="请输入模型编码" maxlength="50" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模型名称" prop="modelName">
            <el-input v-model="form.modelName" placeholder="请输入模型名称" maxlength="200" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="合并类型" prop="consolidationType">
            <el-select v-model="form.consolidationType" placeholder="请选择合并类型">
              <el-option label="完全合并" value="FULL" />
              <el-option label="比例合并" value="PROPORTIONAL" />
              <el-option label="权益法" value="EQUITY" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="母公司ID" prop="parentOrgId">
            <el-input v-model="form.parentOrgId" placeholder="请输入母公司ID" maxlength="32" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="周期类型" prop="periodType">
            <el-select v-model="form.periodType" placeholder="请选择周期类型">
              <el-option label="年" value="YEAR" />
              <el-option label="半年" value="HALF_YEAR" />
              <el-option label="季度" value="QUARTER" />
              <el-option label="月" value="MONTH" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="记账本位币" prop="currencyCode">
            <el-input v-model="form.currencyCode" placeholder="如:CNY" maxlength="10" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="起始期间" prop="startPeriod">
            <el-input v-model="form.startPeriod" placeholder="如:202401" maxlength="20" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="终止期间" prop="endPeriod">
            <el-input v-model="form.endPeriod" placeholder="如:202412" maxlength="20" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="汇率类型" prop="exchangeRateType">
            <el-select v-model="form.exchangeRateType" placeholder="请选择汇率类型">
              <el-option label="即期" value="SPOT" />
              <el-option label="平均" value="AVERAGE" />
              <el-option label="固定" value="FIXED" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否自动抵消" prop="isAutoElimination">
            <el-radio-group v-model="form.isAutoElimination">
              <el-radio label="Y">是</el-radio>
              <el-radio label="N">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="合并范围" prop="consolidationScope">
        <el-input
          v-model="form.consolidationScope"
          type="textarea"
          :rows="3"
          placeholder="请输入合并范围(JSON数组格式),如:[&quot;org001&quot;,&quot;org002&quot;]"
        />
      </el-form-item>

      <el-form-item label="抵消规则" prop="eliminationRules">
        <el-input
          v-model="form.eliminationRules"
          type="textarea"
          :rows="3"
          placeholder="请输入抵消规则(JSON格式)"
        />
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="form.status">
              <el-radio label="ACTIVE">启用</el-radio>
              <el-radio label="INACTIVE">停用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button v-if="formType !== 'view'" type="primary" :loading="submitLoading" @click="handleSubmit">
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getModelById, saveModel, updateModel } from '@/api/financialSharing/consolidationReport/consolidationModel'

export default {
  name: 'ConsolidationModelForm',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    modelId: {
      type: String,
      default: null
    },
    formType: {
      type: String,
      default: 'add' // add/edit/view
    }
  },
  data() {
    // JSON格式验证
    const validateJson = (rule, value, callback) => {
      if (value && value.trim() !== '') {
        try {
          JSON.parse(value)
          callback()
        } catch (e) {
          callback(new Error('请输入正确的JSON格式'))
        }
      } else {
        callback()
      }
    }

    return {
      dialogVisible: false,
      submitLoading: false,
      form: {
        modelId: null,
        modelCode: '',
        modelName: '',
        consolidationType: '',
        parentOrgId: '',
        consolidationScope: '',
        periodType: '',
        startPeriod: '',
        endPeriod: '',
        currencyCode: '',
        exchangeRateType: '',
        isAutoElimination: 'Y',
        eliminationRules: '',
        status: 'ACTIVE'
      },
      rules: {
        modelCode: [
          { required: true, message: '请输入模型编码', trigger: 'blur' }
        ],
        modelName: [
          { required: true, message: '请输入模型名称', trigger: 'blur' }
        ],
        consolidationType: [
          { required: true, message: '请选择合并类型', trigger: 'change' }
        ],
        parentOrgId: [
          { required: true, message: '请输入母公司ID', trigger: 'blur' }
        ],
        consolidationScope: [
          { validator: validateJson, trigger: 'blur' }
        ],
        eliminationRules: [
          { validator: validateJson, trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    dialogTitle() {
      const titleMap = {
        add: '新增合并模型',
        edit: '编辑合并模型',
        view: '查看合并模型'
      }
      return titleMap[this.formType] || '合并模型'
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val && this.modelId) {
        this.loadModelData()
      }
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
      if (!val) {
        this.resetForm()
      }
    }
  },
  methods: {
    /** 加载模型数据 */
    loadModelData() {
      getModelById({ modelId: this.modelId }).then(res => {
        if (res.code === 200 && res.data) {
          this.form = { ...res.data }
        } else {
          this.$message.error(res.msg || '加载数据失败')
        }
      })
    },
    /** 提交表单 */
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          const apiMethod = this.formType === 'add' ? saveModel : updateModel
          apiMethod(this.form).then(res => {
            this.submitLoading = false
            if (res.code === 200) {
              this.$message.success(this.formType === 'add' ? '新增成功' : '修改成功')
              this.$emit('success')
              this.handleClose()
            } else {
              this.$message.error(res.msg || '操作失败')
            }
          }).catch(() => {
            this.submitLoading = false
            this.$message.error('操作失败')
          })
        }
      })
    },
    /** 关闭对话框 */
    handleClose() {
      this.dialogVisible = false
    },
    /** 重置表单 */
    resetForm() {
      this.form = {
        modelId: null,
        modelCode: '',
        modelName: '',
        consolidationType: '',
        parentOrgId: '',
        consolidationScope: '',
        periodType: '',
        startPeriod: '',
        endPeriod: '',
        currencyCode: '',
        exchangeRateType: '',
        isAutoElimination: 'Y',
        eliminationRules: '',
        status: 'ACTIVE'
      }
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
      })
    }
  }
}
</script>

<style scoped>
</style>


