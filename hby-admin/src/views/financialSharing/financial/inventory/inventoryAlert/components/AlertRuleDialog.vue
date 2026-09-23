<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="visible"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form ref="ruleForm" :model="formData" :rules="rules" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="规则名称" prop="ruleName">
            <el-input v-model="formData.ruleName" placeholder="请输入规则名称" maxlength="200" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="规则类型" prop="ruleType">
            <el-select v-model="formData.ruleType" placeholder="请选择规则类型" style="width: 100%" @change="handleRuleTypeChange">
              <el-option label="库存下限预警" value="LOWER_LIMIT" />
              <el-option label="库存上限预警" value="UPPER_LIMIT" />
              <el-option label="呆滞库存预警" value="STAGNANT" />
              <el-option label="过期预警" value="EXPIRED" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20" v-if="formData.ruleType === 'LOWER_LIMIT'">
        <el-col :span="12">
          <el-form-item label="库存下限" prop="lowerLimit">
            <el-input-number v-model="formData.lowerLimit" :min="0" :precision="2" style="width: 100%" placeholder="请输入库存下限" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20" v-if="formData.ruleType === 'UPPER_LIMIT'">
        <el-col :span="12">
          <el-form-item label="库存上限" prop="upperLimit">
            <el-input-number v-model="formData.upperLimit" :min="0" :precision="2" style="width: 100%" placeholder="请输入库存上限" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20" v-if="formData.ruleType === 'STAGNANT'">
        <el-col :span="12">
          <el-form-item label="呆滞天数" prop="stagnantDays">
            <el-input-number v-model="formData.stagnantDays" :min="1" :max="9999" style="width: 100%" placeholder="请输入呆滞天数" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="预警级别" prop="alertLevel">
            <el-select v-model="formData.alertLevel" placeholder="请选择预警级别" style="width: 100%">
              <el-option label="低" :value="1" />
              <el-option label="中" :value="2" />
              <el-option label="高" :value="3" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="通知方式" prop="notifyMethod">
            <el-select v-model="formData.notifyMethod" placeholder="请选择通知方式" style="width: 100%" multiple>
              <el-option label="系统消息" value="SYSTEM" />
              <el-option label="邮件" value="EMAIL" />
              <el-option label="短信" value="SMS" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="备注">
        <el-input v-model="formData.remark" type="textarea" :rows="3" placeholder="请输入备注信息" maxlength="500" />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { saveOrUpdateInventoryAlertRule } from '@/api/financialSharing/inventory'

export default {
  name: 'AlertRuleDialog',
  data() {
    return {
      visible: false,
      dialogTitle: '新增预警规则',
      submitLoading: false,
      formData: {
        ruleId: null,
        ruleName: '',
        ruleType: '',
        upperLimit: null,
        lowerLimit: null,
        stagnantDays: null,
        alertLevel: 2,
        notifyMethod: ['SYSTEM'],
        remark: ''
      },
      rules: {
        ruleName: [
          { required: true, message: '请输入规则名称', trigger: 'blur' },
          { min: 2, max: 200, message: '长度在 2 到 200 个字符', trigger: 'blur' }
        ],
        ruleType: [{ required: true, message: '请选择规则类型', trigger: 'change' }],
        lowerLimit: [{ required: true, message: '请输入库存下限', trigger: 'blur' }],
        upperLimit: [{ required: true, message: '请输入库存上限', trigger: 'blur' }],
        stagnantDays: [{ required: true, message: '请输入呆滞天数', trigger: 'blur' }],
        alertLevel: [{ required: true, message: '请选择预警级别', trigger: 'change' }]
      }
    }
  },
  methods: {
    open(row) {
      this.visible = true
      if (row && row.ruleId) {
        this.dialogTitle = '编辑预警规则'
        this.formData = { ...row }
        if (typeof this.formData.notifyMethod === 'string') {
          this.formData.notifyMethod = this.formData.notifyMethod.split(',')
        }
      } else {
        this.dialogTitle = '新增预警规则'
        this.resetForm()
      }
    },
    handleClose() {
      this.visible = false
      this.resetForm()
    },
    resetForm() {
      this.formData = {
        ruleId: null,
        ruleName: '',
        ruleType: '',
        upperLimit: null,
        lowerLimit: null,
        stagnantDays: null,
        alertLevel: 2,
        notifyMethod: ['SYSTEM'],
        remark: ''
      }
      if (this.$refs.ruleForm) {
        this.$refs.ruleForm.clearValidate()
      }
    },
    handleRuleTypeChange(value) {
      this.formData.upperLimit = null
      this.formData.lowerLimit = null
      this.formData.stagnantDays = null
    },
    handleSubmit() {
      this.$refs.ruleForm.validate(async (valid) => {
        if (!valid) return false
        try {
          this.submitLoading = true
          const submitData = { ...this.formData }
          if (Array.isArray(submitData.notifyMethod)) {
            submitData.notifyMethod = submitData.notifyMethod.join(',')
          }
          const response = await saveOrUpdateInventoryAlertRule(submitData)
          if (response.code === 1) {
            this.$message.success(this.formData.ruleId ? '更新成功' : '新增成功')
            this.handleClose()
            this.$emit('success')
          } else {
            this.$message.error(response.msg || '操作失败')
          }
        } catch (error) {
          console.error('保存失败：', error)
          this.$message.error('保存失败：' + (error.message || '未知错误'))
        } finally {
          this.submitLoading = false
        }
      })
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}
</style>

