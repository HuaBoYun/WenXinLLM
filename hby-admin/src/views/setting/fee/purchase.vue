<template>
  <div class="license-purchase-container">
    <!-- 购买申请 -->
    <el-card shadow="hover" style="margin-bottom: 20px;">
      <div slot="header"><span>密钥购买</span></div>
      <el-form :inline="true">
        <el-form-item label="购买金额">
          <el-input-number v-model="purchaseAmount" :min="1" :precision="2" :step="100" style="width: 200px;" />
          <span style="margin-left: 5px;">元</span>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="submitting" @click="handleSubmit">提交购买申请</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 筛选 -->
    <el-card shadow="hover">
      <div slot="header">
        <span>购买记录</span>
        <el-button style="float: right;" icon="el-icon-download" size="small" @click="handleExport">导出</el-button>
      </div>
      <div style="margin-bottom: 15px; display: flex; align-items: center; flex-wrap: wrap; gap: 10px;">
        <el-select v-model="queryStatus" placeholder="状态" clearable style="width: 130px;">
          <el-option label="待审批" :value="0" />
          <el-option label="已审批" :value="1" />
          <el-option label="已生成密钥" :value="2" />
          <el-option label="已驳回" :value="3" />
        </el-select>
        <el-date-picker v-model="dateRange" type="daterange" range-separator="至" start-placeholder="开始" end-placeholder="结束" value-format="yyyy-MM-dd" />
        <el-button type="primary" icon="el-icon-search" @click="fetchList">查询</el-button>
      </div>

      <!-- 列表 -->
      <el-table v-loading="loading" :data="tableData" border>
        <el-table-column prop="orderNo" label="订单编号" width="200" />
        <el-table-column prop="companyName" label="公司" width="150" />
        <el-table-column prop="purchaseAmount" label="金额(元)" width="100" align="right" />
        <el-table-column label="状态" width="120" align="center">
          <template slot-scope="{ row }">
            <el-tag v-if="row.status === 0" type="warning" size="small">待审批</el-tag>
            <el-tag v-else-if="row.status === 1" type="primary" size="small">已审批</el-tag>
            <el-tag v-else-if="row.status === 2" type="success" size="small">已生成密钥</el-tag>
            <el-tag v-else-if="row.status === 3" type="danger" size="small">已驳回</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="applicantName" label="申请人" width="100" align="center" />
        <el-table-column label="申请时间" width="160" align="center">
          <template slot-scope="{ row }">{{ fmtDate(row.createTime) }}</template>
        </el-table-column>
        <el-table-column prop="approverName" label="审批人" width="100" align="center" />
        <el-table-column label="操作" min-width="200" align="center">
          <template slot-scope="{ row }">
            <el-button type="text" size="small" @click="handleDetail(row)">详情</el-button>
            <el-button v-if="isAdmin && row.status === 0" type="text" size="small" style="color:#67c23a;" @click="handleApprove(row, true)">通过</el-button>
            <el-button v-if="isAdmin && row.status === 0" type="text" size="small" style="color:#f56c6c;" @click="handleApprove(row, false)">驳回</el-button>
            <el-button v-if="isAdmin && row.status === 1" type="text" size="small" style="color:#e6a23c;" @click="handleGenKey(row)">生成密钥</el-button>
            <el-button v-if="row.status === 2 && row.licenseKey" type="text" size="small" @click="handleCopyKey(row)">复制密钥</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top: 15px; text-align: right;" @current-change="p => { pageNum = p; fetchList() }" :current-page="pageNum" :page-size="pageSize" :total="total" layout="total, prev, pager, next" />
    </el-card>

    <!-- 详情弹窗 -->
    <el-dialog title="订单详情" :visible.sync="detailVisible" width="550px">
      <el-descriptions :column="1" border v-if="detailData">
        <el-descriptions-item label="订单编号">{{ detailData.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="公司">{{ detailData.companyName }}</el-descriptions-item>
        <el-descriptions-item label="金额">¥ {{ detailData.purchaseAmount }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ ['待审批','已审批','已生成密钥','已驳回'][detailData.status] }}</el-descriptions-item>
        <el-descriptions-item label="申请人">{{ detailData.applicantName }}</el-descriptions-item>
        <el-descriptions-item label="申请时间">{{ fmtDate(detailData.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="审批人">{{ detailData.approverName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审批时间">{{ fmtDate(detailData.approveTime) || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审批备注">{{ detailData.approveRemark || '-' }}</el-descriptions-item>
        <el-descriptions-item v-if="detailData.licenseKey" label="密钥">
          <el-input :value="detailData.licenseKey" readonly style="width:100%;">
            <el-button slot="append" @click="copyText(detailData.licenseKey)">复制</el-button>
          </el-input>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { submitLicenseOrder, getLicenseOrderList, getLicenseOrderDetail, approveLicenseOrder, generateOrderKey, exportLicenseOrders } from '@/api/setting/fee'

export default {
  name: 'LicensePurchase',
  data() {
    return {
      purchaseAmount: 1000,
      submitting: false,
      loading: false,
      isAdmin: false,
      queryStatus: null,
      dateRange: [],
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 20,
      detailVisible: false,
      detailData: null,
    }
  },
  created() { this.fetchList() },
  methods: {
    async handleSubmit() {
      if (!this.purchaseAmount || this.purchaseAmount <= 0) { this.$message.warning('请输入购买金额'); return }
      this.$confirm(`确认提交购买 ¥${this.purchaseAmount} 的密钥申请？`, '确认').then(async () => {
        this.submitting = true
        try {
          const res = await submitLicenseOrder({ purchaseAmount: this.purchaseAmount })
          if (res.code === 1 || res.code === 200) { this.$message.success('申请已提交'); this.fetchList() }
          else this.$message.error(res.msg || '提交失败')
        } catch (e) { this.$message.error('提交失败') }
        this.submitting = false
      }).catch(() => {})
    },
    async fetchList() {
      this.loading = true
      const params = { status: this.queryStatus, pageNum: this.pageNum, pageSize: this.pageSize }
      if (this.dateRange && this.dateRange.length === 2) { params.startTime = this.dateRange[0]; params.endTime = this.dateRange[1] }
      try {
        const res = await getLicenseOrderList(params)
        if (res.code === 1 || res.code === 200) {
          const d = res.data || {}
          this.tableData = d.list || []; this.total = d.total || 0; this.isAdmin = !!d.isAdmin
        }
      } catch (e) { console.error(e) }
      this.loading = false
    },
    async handleDetail(row) {
      try {
        const res = await getLicenseOrderDetail({ orderId: row.id })
        if (res.code === 1 || res.code === 200) { this.detailData = (res.data && res.data.order) || row; this.detailVisible = true }
      } catch (e) { this.detailData = row; this.detailVisible = true }
    },
    handleApprove(row, approved) {
      const action = approved ? '通过' : '驳回'
      this.$prompt(`请输入${action}备注`, `${action}审批`, { inputPlaceholder: '备注（可选）' }).then(async ({ value }) => {
        const res = await approveLicenseOrder({ orderId: row.id, approved, remark: value || '' })
        if (res.code === 1 || res.code === 200) { this.$message.success(`已${action}`); this.fetchList() }
        else this.$message.error(res.msg || '操作失败')
      }).catch(() => {})
    },
    async handleGenKey(row) {
      this.$confirm('确认为该订单生成密钥？', '生成密钥').then(async () => {
        const res = await generateOrderKey({ orderId: row.id })
        if (res.code === 1 || res.code === 200) {
          const key = res.data && res.data.licenseKey
          this.$alert(`密钥已生成：\n${key}`, '生成成功', { confirmButtonText: '复制密钥', callback: () => { this.copyText(key); this.fetchList() } })
        } else this.$message.error(res.msg || '生成失败')
      }).catch(() => {})
    },
    handleCopyKey(row) { this.copyText(row.licenseKey) },
    copyText(text) {
      if (!text) return
      const el = document.createElement('textarea'); el.value = text; document.body.appendChild(el); el.select(); document.execCommand('copy'); document.body.removeChild(el)
      this.$message.success('已复制到剪贴板')
    },
    async handleExport() {
      const params = { status: this.queryStatus }
      if (this.dateRange && this.dateRange.length === 2) { params.startTime = this.dateRange[0]; params.endTime = this.dateRange[1] }
      try {
        const res = await exportLicenseOrders(params)
        const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob); const a = document.createElement('a'); a.href = url; a.download = '密钥购买记录.xlsx'; a.click(); window.URL.revokeObjectURL(url)
      } catch (e) { this.$message.error('导出失败') }
    },
    fmtDate(v) { if (!v) return ''; const d = new Date(v); const p = n => n < 10 ? '0' + n : n; return d.getFullYear() + '-' + p(d.getMonth()+1) + '-' + p(d.getDate()) + ' ' + p(d.getHours()) + ':' + p(d.getMinutes()) },
  },
}
</script>

<style lang="scss" scoped>
.license-purchase-container { padding: 20px; }
</style>
