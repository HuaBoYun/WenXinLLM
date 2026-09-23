<template>
  <el-dialog
    :title="formData.formulaId ? '修改表单公式' : '新增表单公式'"
    :visible.sync="dialogVisible"
    width="900px"
    :before-close="handleClose"
  >
    <el-form ref="form" :model="formData" :rules="rules" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="所属模板" prop="templateId">
            <el-select
              v-model="formData.templateId"
              placeholder="请选择所属模板"
              style="width: 100%"
            >
              <el-option
                v-for="item in templateOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="公式编码" prop="formulaCode">
            <el-input v-model="formData.formulaCode" placeholder="请输入公式编码" maxlength="50" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="公式名称" prop="formulaName">
            <el-input v-model="formData.formulaName" placeholder="请输入公式名称" maxlength="200" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="公式类型" prop="formulaType">
            <el-select v-model="formData.formulaType" placeholder="请选择公式类型" style="width: 100%">
              <el-option label="计算公式" value="CALCULATION" />
              <el-option label="汇总公式" value="SUMMARY" />
              <el-option label="校验公式" value="VALIDATION" />
              <el-option label="取数公式" value="FETCH" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="公式表达式" prop="formulaExpression">
        <el-input
          v-model="formData.formulaExpression"
          type="textarea"
          :rows="4"
          placeholder="请输入公式表达式,如: A1 + A2 * 0.1"
        />
        <span class="form-tip">
          支持基本运算符(+、-、*、/)、单元格引用(如A1、B2)、函数(如SUM、AVG)等
        </span>
      </el-form-item>
      <el-row :gutter="20" v-if="formData.formulaType === 'VALIDATION'">
        <el-col :span="12">
          <el-form-item label="校验类型">
            <el-select v-model="formData.validationType" placeholder="请选择校验类型" style="width: 100%">
              <el-option label="强制" value="MANDATORY" />
              <el-option label="提示" value="PROMPT" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="容差范围">
            <el-input-number
              v-model="formData.toleranceRange"
              :precision="6"
              :step="0.01"
              :min="0"
              controls-position="right"
              style="width: 100%"
              placeholder="允许的误差范围"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="生效条件">
        <el-input
          v-model="formData.effectiveCondition"
          type="textarea"
          :rows="3"
          placeholder="请输入生效条件(JSON格式),如: {&quot;period&quot;: &quot;>=202401&quot;}"
        />
        <span class="form-tip">
          可配置公式的生效条件,如特定期间、特定维度值等,留空表示始终生效
        </span>
      </el-form-item>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="排序号" prop="sortNo">
            <el-input-number
              v-model="formData.sortNo"
              :min="0"
              :max="9999"
              controls-position="right"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="formData.status">
              <el-radio label="ACTIVE">启用</el-radio>
              <el-radio label="INACTIVE">停用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveFormFormula } from '@/api/financialSharing/enterpriseReport/formFormula'

export default {
  name: 'FormFormulaForm',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    formData: {
      type: Object,
      default: () => ({})
    },
    templateOptions: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      dialogVisible: false,
      submitLoading: false,
      rules: {
        templateId: [
          { required: true, message: '所属模板不能为空', trigger: 'change' }
        ],
        formulaCode: [
          { required: true, message: '公式编码不能为空', trigger: 'blur' },
          { min: 1, max: 50, message: '公式编码长度在1到50个字符', trigger: 'blur' }
        ],
        formulaName: [
          { required: true, message: '公式名称不能为空', trigger: 'blur' },
          { min: 1, max: 200, message: '公式名称长度在1到200个字符', trigger: 'blur' }
        ],
        formulaType: [
          { required: true, message: '公式类型不能为空', trigger: 'change' }
        ],
        formulaExpression: [
          { required: true, message: '公式表达式不能为空', trigger: 'blur' }
        ],
        sortNo: [
          { required: true, message: '排序号不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '状态不能为空', trigger: 'change' }
        ]
      }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    /** 提交表单 */
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          // 验证生效条件JSON格式
          if (this.formData.effectiveCondition && this.formData.effectiveCondition.trim()) {
            try {
              JSON.parse(this.formData.effectiveCondition)
            } catch (e) {
              this.$message.error('生效条件格式不正确,请输入有效的JSON')
              return
            }
          }

          this.submitLoading = true
          saveFormFormula(this.formData).then(response => {
            this.submitLoading = false
            if (response.code === 200) {
              this.$message.success(response.msg || '保存成功')
              this.$emit('success')
              this.handleClose()
            } else {
              this.$message.error(response.msg || '保存失败')
            }
          }).catch(() => {
            this.submitLoading = false
          })
        }
      })
    },
    /** 关闭对话框 */
    handleClose() {
      this.$refs.form.resetFields()
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.form-tip {
  font-size: 12px;
  color: #999;
  line-height: 1.5;
  display: block;
  margin-top: 5px;
}
</style>


