<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="700px"
    :before-close="handleClose"
    :close-on-click-modal="false"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      :disabled="dialogType === 'view'"
      label-width="110px"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="企业名称" prop="enterpriseName">
            <el-input v-model="form.enterpriseName" placeholder="请输入企业名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="股东名称" prop="shareholderName">
            <el-input v-model="form.shareholderName" placeholder="请输入股东名称" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="股东类型" prop="shareholderType">
            <el-select v-model="form.shareholderType" placeholder="请选择" style="width:100%">
              <el-option label="企业" value="ENTERPRISE" />
              <el-option label="个人" value="INDIVIDUAL" />
              <el-option label="政府" value="GOVERNMENT" />
              <el-option label="基金" value="FUND" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="持股比例(%)" prop="shareholdingRatio">
            <el-input-number v-model="form.shareholdingRatio" :min="0" :max="100" :precision="2" style="width:100%" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="持股金额(万)" prop="shareholdingAmount">
            <el-input-number v-model="form.shareholdingAmount" :min="0" :precision="2" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="穿透层级" prop="penetrationLevel">
            <el-input-number v-model="form.penetrationLevel" :min="1" :max="10" style="width:100%" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="最终控制人" prop="ultimateController">
            <el-input v-model="form.ultimateController" placeholder="请输入最终控制人" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="风险等级" prop="riskLevel">
            <el-select v-model="form.riskLevel" placeholder="请选择" style="width:100%">
              <el-option label="低风险" value="LOW" />
              <el-option label="中风险" value="MEDIUM" />
              <el-option label="高风险" value="HIGH" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="是否关联方" prop="isRelatedParty">
            <el-radio-group v-model="form.isRelatedParty">
              <el-radio label="1">是</el-radio>
              <el-radio label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="分析状态" prop="analysisStatus">
            <el-select v-model="form.analysisStatus" placeholder="请选择" style="width:100%">
              <el-option label="待分析" value="PENDING" />
              <el-option label="分析中" value="ANALYZING" />
              <el-option label="已完成" value="COMPLETED" />
              <el-option label="分析失败" value="FAILED" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button v-if="dialogType !== 'view'" type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { addShareholderAnalysis, updateShareholderAnalysis } from '@/api/stateAssets/shareholderAnalysis'

export default {
  name: 'ShareholderAnalysisDialog',
  props: {
    visible: { type: Boolean, default: false },
    formData: { type: Object, default: () => ({}) },
    dialogType: { type: String, default: 'add' },
  },
  data() {
    return {
      submitLoading: false,
      form: {},
      rules: {
        enterpriseName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        shareholderName: [{ required: true, message: '请输入股东名称', trigger: 'blur' }],
        shareholdingRatio: [{ required: true, message: '请输入持股比例', trigger: 'blur' }],
      },
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) },
    },
    dialogTitle() {
      const map = { add: '新增股东分析', edit: '编辑股东分析', view: '查看股东分析' }
      return map[this.dialogType] || '股东分析'
    },
  },
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
      }
    },
  },
  methods: {
    initForm() {
      if (this.dialogType === 'add') {
        this.form = {
          analysisId: null,
          enterpriseName: '',
          shareholderName: '',
          shareholderType: 'ENTERPRISE',
          shareholdingRatio: null,
          shareholdingAmount: null,
          penetrationLevel: 1,
          ultimateController: '',
          riskLevel: 'LOW',
          isRelatedParty: '0',
          analysisStatus: 'PENDING',
          remark: '',
        }
      } else {
        this.form = { ...this.formData }
      }
      this.$nextTick(() => {
        this.$refs.formRef && this.$refs.formRef.clearValidate()
      })
    },
    handleClose() {
      this.dialogVisible = false
    },
    handleSubmit() {
      this.$refs.formRef.validate((valid) => {
        if (!valid) return
        this.submitLoading = true
        const apiFn = this.dialogType === 'add' ? addShareholderAnalysis : updateShareholderAnalysis
        apiFn(this.form)
          .then((response) => {
            if (response && response.result === 200) {
              this.$message.success(this.dialogType === 'add' ? '新增成功' : '更新成功')
              this.$emit('refresh')
              this.handleClose()
            } else {
              this.$message.warning(response?.msg || '操作失败')
            }
          })
          .catch((error) => {
            console.error('提交失败:', error)
            this.$message.error('请求失败')
          })
          .finally(() => {
            this.submitLoading = false
          })
      })
    },
  },
}
</script>

<style scoped>
.dialog-footer { text-align: right; }
</style>