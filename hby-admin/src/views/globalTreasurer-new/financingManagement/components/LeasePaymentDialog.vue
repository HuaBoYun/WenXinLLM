<template>
  <el-dialog
    title="租金管理"
    :visible.sync="dialogVisible"
    width="1100px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div class="payment-management">
      <!-- 租赁基本信息 -->
      <el-card class="info-card" shadow="never">
        <div slot="header"><span>租赁基本信息</span></div>
        <el-descriptions :column="4" border size="small">
          <el-descriptions-item label="租赁编号">{{ leaseInfo.leaseNo || '-' }}</el-descriptions-item>
          <el-descriptions-item label="租赁金额">{{ formatCurrency(leaseInfo.leasingAmount) }}</el-descriptions-item>
          <el-descriptions-item label="月租金">{{ formatCurrency(leaseInfo.monthlyRent) }}</el-descriptions-item>
          <el-descriptions-item label="租赁期限">{{ leaseInfo.leasingTerm || '-' }} {{ getTermUnitText(leaseInfo.termUnit) }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 租金统计 -->
      <el-row :gutter="20" class="payment-stats">
        <el-col :span="6">
          <div class="stat-card total"><div class="stat-label">应付总额</div><div class="stat-value">{{ formatCurrency(totalAmount) }}</div></div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card paid"><div class="stat-label">已付金额</div><div class="stat-value">{{ formatCurrency(paidAmount) }}</div></div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card unpaid"><div class="stat-label">待付金额</div><div class="stat-value">{{ formatCurrency(unpaidAmount) }}</div></div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card progress"><div class="stat-label">付款进度</div><div class="stat-value">{{ paymentProgress }}%</div></div>
        </el-col>
      </el-row>

      <!-- 租金计划表 -->
      <el-card class="payment-card" shadow="never">
        <div slot="header" class="card-header">
          <span>租金计划</span>
          <el-button type="primary" size="mini" icon="el-icon-check" @click="handleBatchPay" :disabled="!selectedPayments.length">批量确认付款</el-button>
        </div>
        <el-table :data="paymentList" border stripe size="small" v-loading="loading" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center" :selectable="row => row.status !== 'PAID'" />
          <el-table-column prop="period" label="期数" width="70" align="center" />
          <el-table-column prop="dueDate" label="应付日期" width="110" />
          <el-table-column prop="principal" label="本金" width="110" align="right">
            <template slot-scope="scope">{{ formatCurrency(scope.row.principal) }}</template>
          </el-table-column>
          <el-table-column prop="interest" label="利息" width="100" align="right">
            <template slot-scope="scope">{{ formatCurrency(scope.row.interest) }}</template>
          </el-table-column>
          <el-table-column prop="amount" label="应付金额" width="120" align="right">
            <template slot-scope="scope">{{ formatCurrency(scope.row.amount) }}</template>
          </el-table-column>
          <el-table-column prop="paidAmount" label="实付金额" width="120" align="right">
            <template slot-scope="scope">{{ formatCurrency(scope.row.paidAmount) }}</template>
          </el-table-column>
          <el-table-column prop="paidDate" label="付款日期" width="110" />
          <el-table-column prop="status" label="状态" width="90" align="center">
            <template slot-scope="scope">
              <el-tag :type="getPaymentStatusType(scope.row.status)" size="mini">{{ getPaymentStatusText(scope.row.status) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" align="center" fixed="right">
            <template slot-scope="scope">
              <el-button v-if="scope.row.status !== 'PAID'" type="text" size="mini" @click="handlePay(scope.row)">付款</el-button>
              <el-button v-else type="text" size="mini" @click="handleViewReceipt(scope.row)">凭证</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>

    <div slot="footer"><el-button @click="handleClose">关 闭</el-button></div>

    <!-- 付款确认弹窗 -->
    <el-dialog title="确认付款" :visible.sync="payFormVisible" width="500px" append-to-body>
      <el-form ref="payForm" :model="payForm" :rules="payRules" label-width="100px">
        <el-form-item label="期数">第 {{ payForm.period }} 期</el-form-item>
        <el-form-item label="应付金额">{{ formatCurrency(payForm.amount) }}</el-form-item>
        <el-form-item label="实付金额" prop="paidAmount">
          <el-input-number v-model="payForm.paidAmount" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="付款日期" prop="paidDate">
          <el-date-picker v-model="payForm.paidDate" type="date" placeholder="选择日期" style="width: 100%" value-format="yyyy-MM-dd" />
        </el-form-item>
        <el-form-item label="付款备注" prop="remark">
          <el-input v-model="payForm.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="payFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitPayForm">确认付款</el-button>
      </div>
    </el-dialog>
  </el-dialog>
</template>

<script>
import { getFinancialLeaseDetail, getLeasePaymentList, getLeasePaymentSummary, confirmLeasePayment, batchConfirmLeasePayments } from '@/api/globalTreasurer/rzgl'

export default {
  name: 'LeasePaymentDialog',
  props: {
    visible: { type: Boolean, default: false },
    leaseId: { type: [String, Number], default: null }
  },
  data() {
    return {
      dialogVisible: false,
      loading: false,
      leaseInfo: {},
      paymentList: [],
      paymentSummary: {},
      selectedPayments: [],
      payFormVisible: false,
      payForm: { paymentId: null, period: '', amount: 0, paidAmount: 0, paidDate: '', remark: '' },
      payRules: {
        paidAmount: [{ required: true, message: '请输入实付金额', trigger: 'blur' }],
        paidDate: [{ required: true, message: '请选择付款日期', trigger: 'change' }]
      }
    }
  },
  computed: {
    totalAmount() { return this.paymentSummary.totalAmount || this.paymentList.reduce((sum, item) => sum + (item.amount || 0), 0) },
    paidAmount() { return this.paymentSummary.paidAmount || this.paymentList.filter(item => item.status === 'PAID').reduce((sum, item) => sum + (item.paidAmount || 0), 0) },
    unpaidAmount() { return this.paymentSummary.unpaidAmount || (this.totalAmount - this.paidAmount) },
    paymentProgress() { return this.paymentSummary.paymentProgress || (this.totalAmount > 0 ? Math.round((this.paidAmount / this.totalAmount) * 100) : 0) }
  },
  watch: {
    visible: {
      handler(val) {
        this.dialogVisible = val
        if (val && this.leaseId) {
          console.log('LeasePaymentDialog: visible changed, leaseId =', this.leaseId)
          this.loadData()
        }
      },
      immediate: false
    },
    leaseId: {
      handler(val) {
        console.log('LeasePaymentDialog: leaseId changed to', val)
        // 如果弹窗已经打开且 leaseId 刚刚设置，则加载数据
        if (this.dialogVisible && val) {
          this.loadData()
        }
      },
      immediate: false
    }
  },
  methods: {
    async loadData() {
      if (!this.leaseId) {
        this.$message.warning('租赁ID不能为空')
        this.loading = false
        return
      }
      this.loading = true
      try {
        // 获取租赁基本信息
        const leaseRes = await getFinancialLeaseDetail(this.leaseId)
        if (leaseRes && (leaseRes.code === 1 || leaseRes.code === 200)) {
          this.leaseInfo = leaseRes.data || {}
        }
        // 获取租金计划列表
        const paymentRes = await getLeasePaymentList(this.leaseId)
        if (paymentRes && (paymentRes.code === 1 || paymentRes.code === 200)) {
          // 后端返回的数据结构可能是 { list: [], summary: {} }
          if (paymentRes.data && paymentRes.data.list) {
            this.paymentList = paymentRes.data.list || []
            this.paymentSummary = paymentRes.data.summary || {}
          } else {
            this.paymentList = paymentRes.data || []
          }
        } else if (paymentRes && paymentRes.code === 0) {
          console.warn('获取租金计划失败:', paymentRes.msg)
          this.paymentList = []
        }
        // 如果上面没有获取到汇总信息，单独获取
        if (!this.paymentSummary || Object.keys(this.paymentSummary).length === 0) {
          try {
            const summaryRes = await getLeasePaymentSummary(this.leaseId)
            if (summaryRes && (summaryRes.code === 1 || summaryRes.code === 200)) {
              this.paymentSummary = summaryRes.data || {}
            }
          } catch (e) {
            console.warn('获取租金汇总信息失败:', e)
          }
        }
      } catch (e) {
        console.error('获取数据失败:', e)
        this.$message.error('获取数据失败: ' + (e.message || '未知错误'))
      } finally {
        this.loading = false
      }
    },
    handleClose() {
      this.dialogVisible = false
      this.$emit('update:visible', false)
    },
    handleSelectionChange(val) { this.selectedPayments = val },
    handlePay(row) {
      this.payForm = { paymentId: row.paymentId, period: row.period, amount: row.amount, paidAmount: row.amount, paidDate: '', remark: '' }
      this.payFormVisible = true
    },
    async handleBatchPay() {
      try {
        await this.$confirm(`确认批量付款 ${this.selectedPayments.length} 期?`, '提示', { type: 'warning' })
        const paymentIds = this.selectedPayments.map(item => item.paymentId)
        const res = await batchConfirmLeasePayments(paymentIds)
        if (res && (res.code === 1 || res.code === 200)) {
          this.$message.success('批量付款成功')
          this.selectedPayments = []
          this.loadData()
        } else {
          this.$message.error(res.msg || '批量付款失败')
        }
      } catch (e) {
        if (e !== 'cancel') {
          console.error('批量付款失败:', e)
          this.$message.error('批量付款失败')
        }
      }
    },
    handleViewReceipt(row) { this.$message.info('查看第 ' + row.period + ' 期付款凭证') },
    async submitPayForm() {
      this.$refs.payForm.validate(async valid => {
        if (valid) {
          try {
            const res = await confirmLeasePayment(this.payForm.paymentId, this.payForm.paidAmount)
            if (res && (res.code === 1 || res.code === 200)) {
              this.payFormVisible = false
              this.$message.success('付款成功')
              this.loadData()
            } else {
              this.$message.error(res.msg || '付款失败')
            }
          } catch (e) {
            console.error('付款失败:', e)
            this.$message.error('付款失败')
          }
        }
      })
    },
    formatCurrency(val) {
      if (!val && val !== 0) return '-'
      return '¥' + Number(val).toLocaleString('zh-CN', { minimumFractionDigits: 2 })
    },
    getTermUnitText(unit) {
      const map = { M: '个月', Y: '年', D: '天' }
      return map[unit] || unit || ''
    },
    getPaymentStatusType(status) {
      const map = { PAID: 'success', PENDING: 'info', OVERDUE: 'danger' }
      return map[status] || 'info'
    },
    getPaymentStatusText(status) {
      const map = { PAID: '已付', PENDING: '待付', OVERDUE: '逾期' }
      return map[status] || status || '-'
    }
  }
}
</script>

<style lang="scss" scoped>
.payment-management {
  .info-card, .payment-card { margin-bottom: 15px; }
  .payment-stats {
    margin-bottom: 15px;
    .stat-card {
      padding: 15px;
      border-radius: 4px;
      text-align: center;
      &.total { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
      &.paid { background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); }
      &.unpaid { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }
      &.progress { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }
      .stat-label { color: rgba(255,255,255,0.9); font-size: 13px; margin-bottom: 8px; }
      .stat-value { color: #fff; font-size: 20px; font-weight: bold; }
    }
  }
  .card-header { display: flex; justify-content: space-between; align-items: center; }
}
</style>

