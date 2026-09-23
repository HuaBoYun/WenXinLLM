<template>
  <el-dialog
    :title="isEdit ? '编辑租赁申请' : '新增租赁申请'"
    :visible.sync="dialogVisible"
    width="900px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      class="lease-form"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="租赁名称" prop="leaseName">
            <el-input v-model="form.leaseName" placeholder="请输入租赁名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="租赁编号" prop="leaseNo">
            <el-input v-model="form.leaseNo" placeholder="系统自动生成" disabled />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="租赁类型" prop="leasingType">
            <el-select v-model="form.leasingType" placeholder="请选择租赁类型" style="width: 100%;">
              <el-option label="直接租赁" value="DIRECT" />
              <el-option label="售后回租" value="LEASEBACK" />
              <el-option label="杠杆租赁" value="LEVERAGED" />
              <el-option label="经营租赁" value="OPERATING" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="租赁公司" prop="leasingCompany">
            <el-input v-model="form.leasingCompany" placeholder="请输入租赁公司名称" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="资产名称" prop="assetName">
            <el-input v-model="form.assetName" placeholder="请输入资产名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="资产价值" prop="assetValue">
            <el-input-number v-model="form.assetValue" :precision="2" :min="0" :controls="false" placeholder="请输入资产价值" style="width: 100%;" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="租赁金额" prop="leaseAmount">
            <el-input-number v-model="form.leaseAmount" :precision="2" :min="0" :controls="false" placeholder="请输入租赁金额" style="width: 100%;" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="币种" prop="currencyCode">
            <el-select v-model="form.currencyCode" placeholder="请选择币种" style="width: 100%;">
              <el-option label="人民币" value="CNY" />
              <el-option label="美元" value="USD" />
              <el-option label="欧元" value="EUR" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="租赁期限" prop="leasePeriod">
            <el-input-number v-model="form.leasePeriod" :min="1" :controls="false" placeholder="请输入租赁期限" style="width: 70%;" />
            <el-select v-model="form.periodUnit" style="width: 28%; margin-left: 2%;">
              <el-option label="月" value="MONTH" />
              <el-option label="年" value="YEAR" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="租赁费率(%)" prop="leaseRate">
            <el-input-number v-model="form.leaseRate" :precision="2" :min="0" :max="100" :controls="false" placeholder="请输入租赁费率" style="width: 100%;" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="开始日期" prop="startDate">
            <el-date-picker v-model="form.startDate" type="date" placeholder="请选择开始日期" value-format="yyyy-MM-dd" style="width: 100%;" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="结束日期" prop="endDate">
            <el-date-picker v-model="form.endDate" type="date" placeholder="请选择结束日期" value-format="yyyy-MM-dd" style="width: 100%;" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="支付方式" prop="paymentMethod">
            <el-select v-model="form.paymentMethod" placeholder="请选择支付方式" style="width: 100%;">
              <el-option label="等额本息" value="EQUAL_PRINCIPAL_INTEREST" />
              <el-option label="等额本金" value="EQUAL_PRINCIPAL" />
              <el-option label="先息后本" value="INTEREST_FIRST" />
              <el-option label="一次性还本付息" value="LUMP_SUM" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属公司" prop="companyName">
            <el-input v-model="form.companyName" placeholder="请输入所属公司" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="申请状态" prop="applicationStatus">
            <el-select v-model="form.applicationStatus" placeholder="请选择申请状态" style="width: 100%;" disabled>
              <el-option label="待提交" value="PENDING" />
              <el-option label="已提交" value="SUBMITTED" />
              <el-option label="已审批" value="APPROVED" />
              <el-option label="已拒绝" value="REJECTED" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注信息" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="primary" :loading="saving" @click="handleConfirm">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { createFinancialLease, updateFinancialLease } from '@/api/globalTreasurer/rzgl'

export default {
  name: 'LeaseFormDialog',
  props: {
    visible: { type: Boolean, default: false },
    formData: { type: Object, default: () => ({}) },
    isEdit: { type: Boolean, default: false }
  },
  data() {
    return {
      dialogVisible: false,
      saving: false,
      form: this.getDefaultForm(),
      rules: {
        leasingType: [{ required: true, message: '请选择租赁类型', trigger: 'change' }],
        assetName: [{ required: true, message: '请输入资产名称', trigger: 'blur' }],
        leaseAmount: [{ required: true, message: '请输入租赁金额', trigger: 'blur' }],
        leasePeriod: [{ required: true, message: '请输入租赁期限', trigger: 'blur' }],
        leaseRate: [{ required: true, message: '请输入租赁费率', trigger: 'blur' }],
        startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
        leasingCompany: [{ required: true, message: '请输入租赁公司', trigger: 'blur' }],
        currencyCode: [{ required: true, message: '请选择币种', trigger: 'change' }]
      }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) this.initForm()
    }
  },
  methods: {
    getDefaultForm() {
      return {
        id: null, leaseNo: '', leaseName: '', leasingType: '', assetName: '', assetValue: null,
        leaseAmount: null, currencyCode: 'CNY', leasePeriod: null, periodUnit: 'MONTH',
        leaseRate: null, startDate: '', endDate: '', leasingCompany: '',
        paymentMethod: '', companyId: '', companyName: '', applicationStatus: 'PENDING', remark: ''
      }
    },
    initForm() {
      this.form = this.isEdit && this.formData ? { ...this.formData } : this.getDefaultForm()
      this.$nextTick(() => this.$refs.form && this.$refs.form.clearValidate())
    },
    handleClose() {
      this.dialogVisible = false
      this.$emit('update:visible', false)
      this.$refs.form && this.$refs.form.resetFields()
    },
    handleConfirm() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        this.saving = true
        try {
          const api = this.isEdit ? updateFinancialLease : createFinancialLease
          const res = await api(this.form)
          if (res && (res.code === 1 || res.code === 200)) {
            this.$message.success(this.isEdit ? '更新成功' : '创建成功')
            this.$emit('success', this.form)
            this.handleClose()
          } else {
            this.$message.error(res?.msg || '操作失败')
          }
        } catch (e) {
          this.$message.error(e.message || '操作失败')
        } finally {
          this.saving = false
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.lease-form { .el-form-item { margin-bottom: 18px; } }
.dialog-footer { text-align: right; }
</style>

