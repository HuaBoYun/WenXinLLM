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
      label-width="120px"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="企业编码" prop="companyId">
            <el-input v-model="form.companyId" placeholder="请输入企业编码" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="关联方名称" prop="partyName">
            <el-input v-model="form.partyName" placeholder="请输入关联方名称" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="交易类型" prop="transactionType">
            <el-select v-model="form.transactionType" placeholder="请选择交易类型" style="width: 100%;">
              <el-option label="资金拆借" value="资金拆借" />
              <el-option label="担保" value="担保" />
              <el-option label="资产转让" value="资产转让" />
              <el-option label="商品购销" value="商品购销" />
              <el-option label="服务交易" value="服务交易" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资金性质" prop="relationType">
            <el-select v-model="form.relationType" placeholder="请选择资金性质" style="width: 100%;">
              <el-option label="经营性" value="OPERATING" />
              <el-option label="投资性" value="INVESTING" />
              <el-option label="筹资性" value="FINANCING" />
              <el-option label="自有资金" value="OWN_FUND" />
              <el-option label="借入资金" value="BORROWED_FUND" />
              <el-option label="投资资金" value="INVESTMENT_FUND" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="交易金额(万)" prop="transactionAmount">
            <el-input-number
              v-model="form.transactionAmount"
              :min="0"
              :precision="2"
              style="width: 100%;"
              placeholder="请输入交易金额"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="余额(万)" prop="balanceAmount">
            <el-input-number
              v-model="form.balanceAmount"
              :min="0"
              :precision="2"
              style="width: 100%;"
              placeholder="请输入余额"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="是否重大" prop="isMajor">
            <el-radio-group v-model="form.isMajor">
              <el-radio label="1">是</el-radio>
              <el-radio label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属期间" prop="period">
            <el-input v-model="form.period" placeholder="如：2025-Q1" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button v-if="dialogType !== 'view'" type="primary" :loading="submitLoading" @click="handleSubmit">
        确 定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { addRelatedTransaction, updateRelatedTransaction } from '@/api/stateAssets/relatedTransaction'

export default {
  name: 'RelatedTransactionDialog',
  props: {
    visible: { type: Boolean, default: false },
    formData: { type: Object, default: () => ({}) },
    dialogType: { type: String, default: 'add' }
  },
  data() {
    return {
      form: {},
      submitLoading: false,
      rules: {
        companyId: [{ required: true, message: '请输入企业编码', trigger: 'blur' }],
        partyName: [{ required: true, message: '请输入关联方名称', trigger: 'blur' }],
        transactionType: [{ required: true, message: '请选择交易类型', trigger: 'change' }],
        relationType: [{ required: true, message: '请选择资金性质', trigger: 'change' }],
        transactionAmount: [{ required: true, message: '请输入交易金额', trigger: 'blur' }],
        isMajor: [{ required: true, message: '请选择是否重大', trigger: 'change' }],
        period: [{ required: true, message: '请输入所属期间', trigger: 'blur' }]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    },
    dialogTitle() {
      const map = { add: '新增关联交易', edit: '编辑关联交易', view: '查看关联交易' }
      return map[this.dialogType] || '关联交易'
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.form = this.dialogType === 'add' ? {} : { ...this.formData }
      }
    }
  },
  methods: {
    handleClose() {
      this.$refs.formRef && this.$refs.formRef.resetFields()
      this.dialogVisible = false
    },
    handleSubmit() {
      this.$refs.formRef.validate(async(valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          const api = this.dialogType === 'edit' ? updateRelatedTransaction : addRelatedTransaction
          await api(this.form)
          this.$message.success('保存成功')
          this.$emit('refresh')
          this.handleClose()
        } catch (e) {
          this.$message.error('保存失败：' + (e.message || '未知错误'))
        } finally {
          this.submitLoading = false
        }
      })
    }
  }
}
</script>

<style scoped>
.dialog-footer { text-align: right; }
</style>