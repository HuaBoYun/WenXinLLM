<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="70%"
    :before-close="handleClose"
    destroy-on-close
  >
    <!-- 查看模式：使用 el-descriptions 展示 -->
    <el-descriptions v-if="dialogType === 'view'" :column="2" border>
      <el-descriptions-item label="企业名称">{{ formData.companyName }}</el-descriptions-item>
      <el-descriptions-item label="报表类型">{{ formData.statementType }}</el-descriptions-item>
      <el-descriptions-item label="报告期间">{{ formData.period }}</el-descriptions-item>
      <el-descriptions-item label="审计状态">{{ formatAuditStatus(formData.auditStatus) }}</el-descriptions-item>
      <el-descriptions-item label="营业总收入(万元)">{{ formData.totalRevenue }}</el-descriptions-item>
      <el-descriptions-item label="资产总计(万元)">{{ formData.totalAssets }}</el-descriptions-item>
      <el-descriptions-item label="负债总计(万元)">{{ formData.totalLiabilities }}</el-descriptions-item>
      <el-descriptions-item label="净资产(万元)">{{ formData.netAssets }}</el-descriptions-item>
      <el-descriptions-item label="净利润(万元)">{{ formData.netProfit }}</el-descriptions-item>
      <el-descriptions-item label="经营性现金流(万元)">{{ formData.operatingCashflow }}</el-descriptions-item>
    </el-descriptions>

    <!-- 新增/编辑模式：使用 el-form 编辑 -->
    <el-form v-else ref="formRef" :model="form" :rules="rules" label-width="140px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="企业名称" prop="companyName">
            <el-input v-model="form.companyName" placeholder="请输入企业名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报表类型" prop="statementType">
            <el-select v-model="form.statementType" placeholder="请选择报表类型" style="width: 100%;">
              <el-option label="合并" value="合并" />
              <el-option label="单体" value="单体" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="报告期间" prop="period">
            <el-input v-model="form.period" placeholder="如: 2025-Y" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="审计状态" prop="auditStatus">
            <el-select v-model="form.auditStatus" placeholder="请选择审计状态" style="width: 100%;">
              <el-option label="已审计" value="APPROVED" />
              <el-option label="未审计" value="UNAUDITED" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="营业总收入(万元)" prop="totalRevenue">
            <el-input-number v-model="form.totalRevenue" :min="0" :precision="2" style="width: 100%;" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资产总计(万元)" prop="totalAssets">
            <el-input-number v-model="form.totalAssets" :min="0" :precision="2" style="width: 100%;" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="负债总计(万元)" prop="totalLiabilities">
            <el-input-number v-model="form.totalLiabilities" :min="0" :precision="2" style="width: 100%;" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="净资产(万元)" prop="netAssets">
            <el-input-number v-model="form.netAssets" :min="0" :precision="2" style="width: 100%;" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="净利润(万元)" prop="netProfit">
            <el-input-number v-model="form.netProfit" :precision="2" style="width: 100%;" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="经营性现金流(万元)" prop="operatingCashflow">
            <el-input-number v-model="form.operatingCashflow" :precision="2" style="width: 100%;" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button v-if="dialogType !== 'view'" type="primary" :loading="submitLoading" @click="saveForm">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { addConsolidatedAnalysis, updateConsolidatedAnalysis } from '@/api/stateAssets/consolidatedAnalysis'

export default {
  name: 'ConsolidatedAnalysisDialog',
  props: {
    visible: { type: Boolean, default: false },
    formData: { type: Object, default: () => ({}) },
    dialogType: { type: String, default: 'add' }
  },
  data() {
    return {
      submitLoading: false,
      form: {},
      rules: {
        companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        statementType: [{ required: true, message: '请选择报表类型', trigger: 'change' }],
        period: [{ required: true, message: '请输入报告期间', trigger: 'blur' }],
        totalRevenue: [{ required: true, message: '请输入营业总收入', trigger: 'blur' }],
        totalAssets: [{ required: true, message: '请输入资产总计', trigger: 'blur' }]
      }
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    },
    dialogTitle() {
      const map = { add: '新增合并分析', edit: '编辑合并分析', view: '查看合并分析' }
      return map[this.dialogType] || '合并分析'
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.form = this.dialogType === 'add'
          ? { statementType: '合并', auditStatus: 'UNAUDITED' }
          : { ...this.formData }
      }
    }
  },
  methods: {
    handleClose() {
      if (this.$refs.formRef) {
        this.$refs.formRef.resetFields()
      }
      this.dialogVisible = false
    },
    saveForm() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return
        this.submitLoading = true
        const api = this.dialogType === 'edit' ? updateConsolidatedAnalysis : addConsolidatedAnalysis
        api(this.form).then(res => {
          if (this.isSuccess(res)) {
            this.$message.success(this.dialogType === 'add' ? '新增成功' : '编辑成功')
            this.$emit('refresh')
            this.handleClose()
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        }).catch(() => {
          this.$message.error('请求失败')
        }).finally(() => {
          this.submitLoading = false
        })
      })
    },
    isSuccess(res) {
      return !res || res.code === 1 || res.code === 200 || res.result === 200 || res.result === 1
    },
    formatAuditStatus(status) {
      const map = { APPROVED: '已审计', UNAUDITED: '未审计', PENDING: '待审计', REJECTED: '已驳回' }
      return map[status] || status || '-'
    }
  }
}
</script>

<style scoped>
.dialog-footer { text-align: right; }
</style>