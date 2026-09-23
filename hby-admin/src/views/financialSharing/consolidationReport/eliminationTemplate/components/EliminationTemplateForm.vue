<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      :disabled="mode === 'view'"
    >
      <el-form-item label="合并模型" prop="modelId">
        <el-select v-model="form.modelId" placeholder="请选择合并模型" style="width: 100%">
          <el-option
            v-for="item in modelOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="模板编码" prop="templateCode">
        <el-input v-model="form.templateCode" placeholder="请输入模板编码" maxlength="50" />
      </el-form-item>
      <el-form-item label="模板名称" prop="templateName">
        <el-input v-model="form.templateName" placeholder="请输入模板名称" maxlength="100" />
      </el-form-item>
      <el-form-item label="抵消类型" prop="eliminationType">
        <el-select v-model="form.eliminationType" placeholder="请选择抵消类型" style="width: 100%">
          <el-option label="内部交易抵消" value="INTERNAL_TRANSACTION" />
          <el-option label="内部债权债务抵消" value="INTERNAL_DEBT" />
          <el-option label="未实现利润抵消" value="UNREALIZED_PROFIT" />
          <el-option label="长期股权投资抵消" value="INVESTMENT_ELIMINATION" />
          <el-option label="所有者权益抵消" value="EQUITY_ELIMINATION" />
          <el-option label="其他" value="OTHER" />
        </el-select>
      </el-form-item>
      <el-form-item label="借方科目" prop="debitAccount">
        <el-input
          v-model="form.debitAccount"
          type="textarea"
          :rows="2"
          placeholder="请输入借方科目,多个科目用逗号分隔"
          maxlength="500"
        />
      </el-form-item>
      <el-form-item label="贷方科目" prop="creditAccount">
        <el-input
          v-model="form.creditAccount"
          type="textarea"
          :rows="2"
          placeholder="请输入贷方科目,多个科目用逗号分隔"
          maxlength="500"
        />
      </el-form-item>
      <el-form-item label="计算规则" prop="calculationRule">
        <el-input
          v-model="form.calculationRule"
          type="textarea"
          :rows="4"
          placeholder="请输入计算规则(JSON格式)"
        />
        <div class="form-tip">
          示例: {"dataSource":"EQUITY_INFO","formula":"HOLDING_RATIO * AMOUNT","conditions":[]}
        </div>
      </el-form-item>
      <el-form-item label="描述">
        <el-input
          v-model="form.description"
          type="textarea"
          :rows="3"
          placeholder="请输入描述"
          maxlength="500"
        />
      </el-form-item>
      <el-form-item label="排序号" prop="sortOrder">
        <el-input-number v-model="form.sortOrder" :min="0" :max="9999" style="width: 100%" />
      </el-form-item>
      <el-form-item label="是否启用" prop="isActive">
        <el-radio-group v-model="form.isActive">
          <el-radio label="Y">启用</el-radio>
          <el-radio label="N">停用</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button v-if="mode !== 'view'" type="primary" :loading="submitting" @click="handleSubmit">
        确定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getTemplateById, saveTemplate, updateTemplate } from '@/api/financialSharing/consolidationReport/eliminationTemplate'
import { getModelList } from '@/api/financialSharing/consolidationReport/consolidationModel'

export default {
  name: 'EliminationTemplateForm',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    mode: {
      type: String,
      default: 'add' // add/edit/view
    },
    templateId: {
      type: String,
      default: ''
    }
  },
  data() {
    // 验证JSON格式
    const validateJson = (rule, value, callback) => {
      if (value && value.trim()) {
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
      dialogVisible: this.visible,
      submitting: false,
      form: {
        templateId: '',
        modelId: '',
        templateCode: '',
        templateName: '',
        eliminationType: '',
        debitAccount: '',
        creditAccount: '',
        calculationRule: '',
        description: '',
        sortOrder: 0,
        isActive: 'Y'
      },
      rules: {
        modelId: [
          { required: true, message: '请选择合并模型', trigger: 'change' }
        ],
        templateCode: [
          { required: true, message: '请输入模板编码', trigger: 'blur' }
        ],
        templateName: [
          { required: true, message: '请输入模板名称', trigger: 'blur' }
        ],
        eliminationType: [
          { required: true, message: '请选择抵消类型', trigger: 'change' }
        ],
        debitAccount: [
          { required: true, message: '请输入借方科目', trigger: 'blur' }
        ],
        creditAccount: [
          { required: true, message: '请输入贷方科目', trigger: 'blur' }
        ],
        calculationRule: [
          { validator: validateJson, trigger: 'blur' }
        ]
      },
      modelOptions: []
    }
  },
  computed: {
    dialogTitle() {
      const titles = {
        add: '新增抵消凭证模板',
        edit: '编辑抵消凭证模板',
        view: '查看抵消凭证模板'
      }
      return titles[this.mode] || '抵消凭证模板'
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
  created() {
    this.loadModelOptions()
    if (this.mode !== 'add' && this.templateId) {
      this.loadTemplateData()
    }
  },
  methods: {
    /** 加载模型选项 */
    loadModelOptions() {
      getModelList({ status: 'ACTIVE', pageNum: 1, pageSize: 1000 }).then(res => {
        if (res.code === 200 && res.data && res.data.list) {
          this.modelOptions = res.data.list.map(item => ({
            value: item.modelId,
            label: item.modelName
          }))
        }
      })
    },
    /** 加载模板数据 */
    loadTemplateData() {
      getTemplateById({ templateId: this.templateId }).then(res => {
        if (res.code === 200 && res.data) {
          this.form = {
            ...res.data
          }
        } else {
          this.$message.error(res.msg || '加载数据失败')
        }
      })
    },
    /** 提交表单 */
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitting = true
          const apiMethod = this.mode === 'add' ? saveTemplate : updateTemplate
          apiMethod(this.form).then(res => {
            this.submitting = false
            if (res.code === 200) {
              this.$message.success(this.mode === 'add' ? '新增成功' : '修改成功')
              this.$emit('success')
              this.handleClose()
            } else {
              this.$message.error(res.msg || '操作失败')
            }
          }).catch(() => {
            this.submitting = false
            this.$message.error('操作失败')
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
  color: #909399;
  margin-top: 5px;
}
</style>


