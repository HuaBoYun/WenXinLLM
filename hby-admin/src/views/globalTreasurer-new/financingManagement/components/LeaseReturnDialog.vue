<template>
  <el-dialog
    title="退租管理"
    :visible.sync="dialogVisible"
    width="900px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div class="return-management">
      <!-- 租赁基本信息 -->
      <el-card class="info-card" shadow="never">
        <div slot="header"><span>租赁基本信息</span></div>
        <el-descriptions :column="3" border size="small">
          <el-descriptions-item label="租赁编号">{{ leaseInfo.leaseNo || '-' }}</el-descriptions-item>
          <el-descriptions-item label="租赁类型">{{ getLeasingTypeText(leaseInfo.leasingType) }}</el-descriptions-item>
          <el-descriptions-item label="租赁公司">{{ leaseInfo.leasingCompany || '-' }}</el-descriptions-item>
          <el-descriptions-item label="租赁金额">{{ formatCurrency(leaseInfo.leasingAmount) }}</el-descriptions-item>
          <el-descriptions-item label="起租日期">{{ leaseInfo.startDate || '-' }}</el-descriptions-item>
          <el-descriptions-item label="到期日期">{{ leaseInfo.endDate || '-' }}</el-descriptions-item>
          <el-descriptions-item label="申请状态">
            <el-tag :type="getStatusTagType(leaseInfo.applicationStatus)" size="small">{{ getStatusText(leaseInfo.applicationStatus) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="剩余期数">{{ remainingPeriods }} 期</el-descriptions-item>
          <el-descriptions-item label="剩余租金">{{ formatCurrency(remainingAmount) }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 退租申请表单 -->
      <el-card class="return-card" shadow="never">
        <div slot="header"><span>退租申请</span></div>
        <el-form ref="returnForm" :model="returnForm" :rules="returnRules" label-width="120px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="退租类型" prop="returnType">
                <el-select v-model="returnForm.returnType" placeholder="请选择退租类型" style="width: 100%">
                  <el-option label="正常到期退租" value="NORMAL" />
                  <el-option label="提前退租" value="EARLY" />
                  <el-option label="违约退租" value="BREACH" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="退租日期" prop="returnDate">
                <el-date-picker v-model="returnForm.returnDate" type="date" placeholder="选择退租日期" style="width: 100%" value-format="yyyy-MM-dd" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="资产处置方式" prop="assetDisposal">
                <el-select v-model="returnForm.assetDisposal" placeholder="请选择处置方式" style="width: 100%">
                  <el-option label="归还出租方" value="RETURN" />
                  <el-option label="承租方购买" value="PURCHASE" />
                  <el-option label="第三方转让" value="TRANSFER" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="资产残值" prop="residualValue">
                <el-input-number v-model="returnForm.residualValue" :min="0" :precision="2" style="width: 100%" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="违约金" prop="penaltyAmount">
                <el-input-number v-model="returnForm.penaltyAmount" :min="0" :precision="2" style="width: 100%" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="结算金额" prop="settlementAmount">
                <el-input-number v-model="returnForm.settlementAmount" :min="0" :precision="2" style="width: 100%" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="退租原因" prop="returnReason">
            <el-input v-model="returnForm.returnReason" type="textarea" :rows="3" placeholder="请输入退租原因" />
          </el-form-item>
          <el-form-item label="附件上传">
            <el-upload action="#" :auto-upload="false" :file-list="fileList" :on-change="handleFileChange" multiple>
              <el-button size="small" type="primary">选择文件</el-button>
              <div slot="tip" class="el-upload__tip">支持上传退租协议、资产交接单等文件</div>
            </el-upload>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- 费用明细 -->
      <el-card class="fee-card" shadow="never">
        <div slot="header"><span>费用明细</span></div>
        <el-table :data="feeList" border stripe size="small">
          <el-table-column prop="feeName" label="费用项目" />
          <el-table-column prop="feeAmount" label="金额" width="150" align="right">
            <template slot-scope="scope">{{ formatCurrency(scope.row.feeAmount) }}</template>
          </el-table-column>
          <el-table-column prop="remark" label="备注" />
        </el-table>
        <div class="fee-total">
          <span>费用合计: <strong>{{ formatCurrency(totalFee) }}</strong></span>
        </div>
      </el-card>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取 消</el-button>
      <el-button type="warning" @click="handleSaveDraft">保存草稿</el-button>
      <el-button type="primary" @click="handleSubmit">提交退租申请</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getFinancialLeaseDetail, getLeaseReturnDetail, saveLeaseReturn, submitLeaseReturn, calculateReturnFees, getLeasePaymentSummary } from '@/api/globalTreasurer/rzgl'

export default {
  name: 'LeaseReturnDialog',
  props: {
    visible: { type: Boolean, default: false },
    leaseId: { type: [String, Number], default: null }
  },
  data() {
    return {
      dialogVisible: false,
      loading: false,
      leaseInfo: {},
      paymentSummary: {},
      returnInfo: null,
      returnForm: {
        returnId: null,
        leaseId: null,
        returnType: '', returnDate: '', assetDisposal: '', residualValue: 0,
        penaltyAmount: 0, settlementAmount: 0, returnReason: '', attachments: ''
      },
      returnRules: {
        returnType: [{ required: true, message: '请选择退租类型', trigger: 'change' }],
        returnDate: [{ required: true, message: '请选择退租日期', trigger: 'change' }],
        assetDisposal: [{ required: true, message: '请选择资产处置方式', trigger: 'change' }],
        returnReason: [{ required: true, message: '请输入退租原因', trigger: 'blur' }]
      },
      fileList: [],
      feeList: []
    }
  },
  computed: {
    remainingPeriods() { return this.paymentSummary.unpaidPeriods || 0 },
    remainingAmount() { return this.paymentSummary.unpaidAmount || 0 },
    totalFee() { return this.feeList.reduce((sum, item) => sum + (item.feeAmount || 0), 0) }
  },
  watch: {
    visible: {
      handler(val) {
        this.dialogVisible = val
        if (val && this.leaseId) {
          console.log('LeaseReturnDialog: visible changed, leaseId =', this.leaseId)
          this.loadData()
        }
      },
      immediate: false
    },
    leaseId: {
      handler(val) {
        console.log('LeaseReturnDialog: leaseId changed to', val)
        if (this.dialogVisible && val) {
          this.loadData()
        }
      },
      immediate: false
    },
    'returnForm.returnType'(val) {
      if (val && this.returnForm.assetDisposal) {
        this.calculateFees()
      }
    },
    'returnForm.assetDisposal'(val) {
      if (val && this.returnForm.returnType) {
        this.calculateFees()
      }
    }
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        // 获取租赁基本信息
        const leaseRes = await getFinancialLeaseDetail(this.leaseId)
        if (leaseRes && (leaseRes.code === 1 || leaseRes.code === 200)) {
          this.leaseInfo = leaseRes.data || {}
        }
        // 获取租金汇总信息（用于计算剩余期数和金额）
        const summaryRes = await getLeasePaymentSummary(this.leaseId)
        if (summaryRes && (summaryRes.code === 1 || summaryRes.code === 200)) {
          this.paymentSummary = summaryRes.data || {}
        }
        // 获取退租申请详情（如果已有）
        const returnRes = await getLeaseReturnDetail(this.leaseId)
        if (returnRes && (returnRes.code === 1 || returnRes.code === 200) && returnRes.data) {
          this.returnInfo = returnRes.data
          this.returnForm = {
            returnId: returnRes.data.returnId,
            leaseId: this.leaseId,
            returnType: returnRes.data.returnType || '',
            returnDate: returnRes.data.returnDate || '',
            assetDisposal: returnRes.data.assetDisposal || '',
            residualValue: returnRes.data.residualValue || 0,
            penaltyAmount: returnRes.data.penaltyAmount || 0,
            settlementAmount: returnRes.data.settlementAmount || 0,
            returnReason: returnRes.data.returnReason || '',
            attachments: returnRes.data.attachments || ''
          }
          // 如果有附件，解析文件列表
          if (returnRes.data.attachments) {
            this.fileList = returnRes.data.attachments.split(',').map((name, index) => ({ name, uid: index }))
          }
        } else {
          this.returnForm.leaseId = this.leaseId
        }
      } catch (e) {
        console.error('获取数据失败:', e)
        this.$message.error('获取数据失败')
      } finally {
        this.loading = false
      }
    },
    async calculateFees() {
      try {
        const res = await calculateReturnFees(this.leaseId, this.returnForm.returnType, this.returnForm.assetDisposal)
        if (res && (res.code === 1 || res.code === 200)) {
          const data = res.data || {}
          this.feeList = data.feeList || []
          this.returnForm.penaltyAmount = data.penaltyAmount || 0
          this.returnForm.settlementAmount = data.settlementAmount || 0
          this.returnForm.residualValue = data.residualValue || 0
        }
      } catch (e) {
        console.error('计算费用失败:', e)
      }
    },
    handleClose() {
      this.dialogVisible = false
      this.$emit('update:visible', false)
      this.resetForm()
    },
    resetForm() {
      this.returnForm = { returnId: null, leaseId: null, returnType: '', returnDate: '', assetDisposal: '', residualValue: 0, penaltyAmount: 0, settlementAmount: 0, returnReason: '', attachments: '' }
      this.fileList = []
      this.feeList = []
      this.returnInfo = null
      this.$refs.returnForm && this.$refs.returnForm.resetFields()
    },
    handleFileChange(file, fileList) { this.fileList = fileList },
    async handleSaveDraft() {
      try {
        // 处理附件
        this.returnForm.attachments = this.fileList.map(f => f.name).join(',')
        const res = await saveLeaseReturn(this.returnForm)
        if (res && (res.code === 1 || res.code === 200)) {
          this.$message.success('草稿保存成功')
          if (res.data && res.data.returnId) {
            this.returnForm.returnId = res.data.returnId
          }
        } else {
          this.$message.error(res.msg || '保存失败')
        }
      } catch (e) {
        console.error('保存失败:', e)
        this.$message.error('保存失败')
      }
    },
    async handleSubmit() {
      this.$refs.returnForm.validate(async valid => {
        if (valid) {
          try {
            await this.$confirm('确认提交退租申请?', '提示', { type: 'warning' })
            // 先保存
            this.returnForm.attachments = this.fileList.map(f => f.name).join(',')
            const saveRes = await saveLeaseReturn(this.returnForm)
            if (saveRes && (saveRes.code === 1 || saveRes.code === 200)) {
              const returnId = saveRes.data?.returnId || this.returnForm.returnId
              if (returnId) {
                // 再提交
                const submitRes = await submitLeaseReturn(returnId)
                if (submitRes && (submitRes.code === 1 || submitRes.code === 200)) {
                  this.$message.success('退租申请提交成功')
                  this.$emit('success')
                  this.handleClose()
                } else {
                  this.$message.error(submitRes.msg || '提交失败')
                }
              } else {
                this.$message.error('保存成功但未获取到退租申请ID')
              }
            } else {
              this.$message.error(saveRes.msg || '保存失败')
            }
          } catch (e) {
            if (e !== 'cancel') {
              console.error('提交失败:', e)
              this.$message.error('提交失败')
            }
          }
        }
      })
    },
    formatCurrency(val) {
      if (!val && val !== 0) return '-'
      return '¥' + Number(val).toLocaleString('zh-CN', { minimumFractionDigits: 2 })
    },
    getLeasingTypeText(type) {
      const map = { DIRECT: '直接租赁', LEASEBACK: '售后回租', LEVERAGED: '杠杆租赁', OPERATING: '经营租赁' }
      return map[type] || type || '-'
    },
    getStatusText(status) {
      const map = { PENDING: '待提交', SUBMITTED: '已提交', APPROVED: '已审批', REJECTED: '已拒绝', ACTIVE: '执行中', COMPLETED: '已完成' }
      return map[status] || status || '-'
    },
    getStatusTagType(status) {
      const map = { PENDING: 'info', SUBMITTED: 'warning', APPROVED: 'success', REJECTED: 'danger', ACTIVE: 'primary', COMPLETED: '' }
      return map[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.return-management {
  .info-card, .return-card, .fee-card { margin-bottom: 15px; }
  .fee-total {
    margin-top: 15px;
    padding: 10px;
    background: #f5f7fa;
    border-radius: 4px;
    text-align: right;
    span { color: #606266; }
    strong { color: #E6A23C; font-size: 18px; }
  }
}
</style>

