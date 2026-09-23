<template>
  <div class="page-container" :style="themeVars">
    <!-- Banner -->
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title"><i class="el-icon-document-checked"></i> 合同履约追踪</h2>
        <p class="page-desc">追踪各级企业采购合同执行状态，监控付款进度、验收情况和违约记录</p>
      </div>
    </div>

    <!-- 统计卡 -->
    <el-row :gutter="16" class="stat-row">
      <el-col :span="6" v-for="s in statCards" :key="s.label">
        <div class="stat-card" :style="{ borderLeft: '4px solid ' + s.color }">
          <div class="stat-val" :style="{ color: s.color }">{{ s.value }}</div>
          <div class="stat-lb">{{ s.label }}</div>
        </div>
      </el-col>
    </el-row>

    <!-- 逾期预警 -->
    <el-alert
      v-if="overdueCount > 0"
      :title="`当前有 ${overdueCount} 份合同逾期未验收，${defaultCount} 份合同存在违约风险，请立即跟进！`"
      type="error" show-icon :closable="false" style="margin-bottom:12px"
    />

    <!-- 图表区 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header" class="card-hd">合同状态分布</div>
          <div ref="statusChart" class="chart-sm"></div>
        </el-card>
      </el-col>
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header" class="card-hd">TOP合同付款进度</div>
          <div ref="payChart" class="chart-sm"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询区 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" inline size="small">
        <el-form-item label="采购企业">
          <el-input v-model="queryForm.companyName" placeholder="请输入" clearable style="width:150px" />
        </el-form-item>
        <el-form-item label="合同状态">
          <el-select v-model="queryForm.contractStatus" placeholder="请选择" clearable style="width:120px">
            <el-option label="执行中" value="IN_PROGRESS" />
            <el-option label="逾期" value="OVERDUE" />
            <el-option label="完成" value="COMPLETED" />
            <el-option label="违约" value="DEFAULT" />
          </el-select>
        </el-form-item>
        <el-form-item label="验收状态">
          <el-select v-model="queryForm.acceptanceStatus" placeholder="请选择" clearable style="width:120px">
            <el-option label="未验收" value="NONE" />
            <el-option label="部分验收" value="PARTIAL" />
            <el-option label="全部验收" value="FULL" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="never">
      <el-table v-loading="loading" :data="list" border :row-class-name="rowClassName">
        <el-table-column label="合同编号" prop="contractNo" width="130" />
        <el-table-column label="合同名称" prop="contractName" min-width="180" show-overflow-tooltip />
        <el-table-column label="采购企业" prop="companyName" min-width="160" show-overflow-tooltip />
        <el-table-column label="供应商" prop="supplierName" min-width="140" show-overflow-tooltip />
        <el-table-column label="合同金额(万元)" prop="contractAmount" width="130" align="right">
          <template slot-scope="{ row }">{{ row.contractAmount.toLocaleString() }}</template>
        </el-table-column>
        <el-table-column label="付款进度" width="160" align="center">
          <template slot-scope="{ row }">
            <div style="display:flex;align-items:center;gap:6px">
              <el-progress
                :percentage="row.paymentRatio"
                :color="row.paymentRatio === 100 && row.acceptanceStatus === 'NONE' ? '#CF1322' : (row.paymentRatio > 80 ? '#FA8C16' : ipSecondary)"
                :stroke-width="8"
                style="flex:1"
              />
              <span style="font-size:11px;width:32px;flex-shrink:0">{{ row.paymentRatio }}%</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="合同到期日" prop="endDate" width="110" align="center" />
        <el-table-column label="验收状态" width="100" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="{ NONE: 'danger', PARTIAL: 'warning', FULL: 'success' }[row.acceptanceStatus]" size="mini">
              {{ { NONE: '未验收', PARTIAL: '部分验收', FULL: '全部验收' }[row.acceptanceStatus] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="合同状态" width="90" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="{ IN_PROGRESS: '', OVERDUE: 'danger', COMPLETED: 'success', DEFAULT: 'danger' }[row.contractStatus]" size="mini">
              {{ { IN_PROGRESS: '执行中', OVERDUE: '逾期', COMPLETED: '已完成', DEFAULT: '违约' }[row.contractStatus] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险等级" width="80" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }[row.riskLevel]" size="mini">
              {{ { HIGH: '高', MEDIUM: '中', LOW: '低' }[row.riskLevel] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" align="center" fixed="right">
          <template slot-scope="{ row }">
            <el-button type="text" size="mini" @click="handlePayment(row)">更新付款</el-button>
            <el-button type="text" size="mini" @click="handleAcceptance(row)">确认验收</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        style="margin-top:12px;text-align:right"
        :current-page="queryForm.pageNumber"
        :page-size="queryForm.pageSize"
        layout="total, prev, pager, next"
        :total="total"
        @current-change="handlePageChange"
      />
    </el-card>

    <!-- 更新付款弹窗 -->
    <el-dialog title="更新付款信息" :visible.sync="paymentDialogVisible" width="420px" append-to-body>
      <el-form :model="paymentForm" label-width="100px" size="small">
        <el-form-item label="合同编号">
          <span>{{ paymentForm.contractNo }}</span>
        </el-form-item>
        <el-form-item label="合同金额">
          <span>{{ paymentForm.contractAmount }} 万元</span>
        </el-form-item>
        <el-form-item label="已付金额(万)">
          <el-input-number v-model="paymentForm.paidAmount" :min="0" :max="paymentForm.contractAmount" :precision="2" style="width:200px" />
        </el-form-item>
        <el-form-item label="付款比例">
          <span>{{ computedPaymentRatio }}%</span>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="small" @click="paymentDialogVisible = false">取消</el-button>
        <el-button type="primary" size="small" :loading="submitLoading" @click="submitPayment">确定</el-button>
      </div>
    </el-dialog>

    <!-- 确认验收弹窗 -->
    <el-dialog title="确认验收状态" :visible.sync="acceptanceDialogVisible" width="420px" append-to-body>
      <el-form :model="acceptanceForm" label-width="100px" size="small">
        <el-form-item label="合同编号">
          <span>{{ acceptanceForm.contractNo }}</span>
        </el-form-item>
        <el-form-item label="验收状态">
          <el-select v-model="acceptanceForm.acceptanceStatus" placeholder="请选择" style="width:200px">
            <el-option label="未验收" value="NONE" />
            <el-option label="部分验收" value="PARTIAL" />
            <el-option label="全部验收" value="FULL" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button size="small" @click="acceptanceDialogVisible = false">取消</el-button>
        <el-button type="primary" size="small" :loading="submitLoading" @click="submitAcceptance">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getContractExecutionList, updateContractPayment, updateContractAcceptance } from '@/api/stateAssets/procurementPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'ContractExecution',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      submitLoading: false,
      list: [],
      total: 0,
      queryForm: { companyName: '', contractStatus: '', acceptanceStatus: '', pageNumber: 1, pageSize: 10 },
      charts: [],
      // 付款弹窗
      paymentDialogVisible: false,
      paymentForm: { id: '', contractNo: '', contractAmount: 0, paidAmount: 0 },
      // 验收弹窗
      acceptanceDialogVisible: false,
      acceptanceForm: { id: '', contractNo: '', acceptanceStatus: '' },
    }
  },
  computed: {
    statCards() {
      const total = this.list.length
      const inProgress = this.list.filter(r => r.contractStatus === 'IN_PROGRESS').length
      const overdue = this.list.filter(r => r.contractStatus === 'OVERDUE').length
      const defaulted = this.list.filter(r => r.contractStatus === 'DEFAULT').length
      return [
        { label: '合同总数', value: this.total + '份', color: this.ipSecondary },
        { label: '正常履约', value: (total - overdue - defaulted) + '份', color: '#52C41A' },
        { label: '逾期未结', value: overdue + '份', color: '#CF1322' },
        { label: '违约记录', value: defaulted + '份', color: '#CF1322' },
      ]
    },
    overdueCount() { return this.list.filter(r => r.contractStatus === 'OVERDUE').length },
    defaultCount() { return this.list.filter(r => r.contractStatus === 'DEFAULT').length },
    computedPaymentRatio() {
      if (!this.paymentForm.contractAmount) return 0
      return Math.round((this.paymentForm.paidAmount / this.paymentForm.contractAmount) * 100)
    },
  },
  mounted() {
    this.$nextTick(() => {
      this.initCharts()
      this.fetchData()
    })
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    this.charts.forEach(c => c.dispose())
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getContractExecutionList(this.queryForm)
        if (res && res.result === 200) {
          this.list = (res.data && res.data.tlist) || []
          this.total = (res.data && res.data.totalRecord) || 0
          this.$nextTick(() => this.updateCharts())
        } else {
          this.list = []
          this.total = 0
        }
      } catch (e) {
        this.list = []
        this.total = 0
      } finally {
        this.loading = false
      }
    },
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.fetchData()
    },
    resetQuery() {
      this.queryForm = { companyName: '', contractStatus: '', acceptanceStatus: '', pageNumber: 1, pageSize: 10 }
      this.fetchData()
    },
    handlePageChange(page) {
      this.queryForm.pageNumber = page
      this.fetchData()
    },
    rowClassName({ row }) {
      if (row.contractStatus === 'DEFAULT') return 'row-violation'
      if (row.contractStatus === 'OVERDUE') return 'row-overdue'
      return ''
    },
    // 付款操作
    handlePayment(row) {
      this.paymentForm = { id: row.id, contractNo: row.contractNo, contractAmount: row.contractAmount, paidAmount: row.paidAmount || 0 }
      this.paymentDialogVisible = true
    },
    async submitPayment() {
      this.submitLoading = true
      try {
        const res = await updateContractPayment({
          id: this.paymentForm.id,
          paidAmount: this.paymentForm.paidAmount,
          paymentRatio: this.computedPaymentRatio,
        })
        if (res && res.result === 200) {
          this.$message.success('付款信息更新成功')
          this.paymentDialogVisible = false
          this.fetchData()
        } else {
          this.$message.error((res && res.msg) || '操作失败')
        }
      } catch (e) {
        this.$message.error('请求失败')
      } finally {
        this.submitLoading = false
      }
    },
    // 验收操作
    handleAcceptance(row) {
      this.acceptanceForm = { id: row.id, contractNo: row.contractNo, acceptanceStatus: row.acceptanceStatus || '' }
      this.acceptanceDialogVisible = true
    },
    async submitAcceptance() {
      this.submitLoading = true
      try {
        const res = await updateContractAcceptance({
          id: this.acceptanceForm.id,
          acceptanceStatus: this.acceptanceForm.acceptanceStatus,
        })
        if (res && res.result === 200) {
          this.$message.success('验收状态更新成功')
          this.acceptanceDialogVisible = false
          this.fetchData()
        } else {
          this.$message.error((res && res.msg) || '操作失败')
        }
      } catch (e) {
        this.$message.error('请求失败')
      } finally {
        this.submitLoading = false
      }
    },
    // 图表
    handleResize() { this.charts.forEach(c => c.resize()) },
    initCharts() {
      this.charts.forEach(c => c.dispose())
      this.charts = []
      // status pie
      const statusMap = { IN_PROGRESS: '执行中', OVERDUE: '逾期', COMPLETED: '已完成', DEFAULT: '违约' }
      const statusColors = { IN_PROGRESS: this.ipSecondary, OVERDUE: '#CF1322', COMPLETED: '#52C41A', DEFAULT: '#7B0000' }
      const statusCountMap = {}
      this.list.forEach(r => {
        statusCountMap[r.contractStatus] = (statusCountMap[r.contractStatus] || 0) + 1
      })
      const statusPieData = Object.keys(statusCountMap).map(k => ({
        value: statusCountMap[k], name: statusMap[k] || k, itemStyle: { color: statusColors[k] || '#D9D9D9' }
      }))
      const c1 = echarts.init(this.$refs.statusChart)
      this.charts.push(c1)
      const c2 = echarts.init(this.$refs.payChart)
      this.charts.push(c2)
    },
    updateCharts() {
      if (!this.charts.length) return
      // 状态分布饼图
      const statusMap = { IN_PROGRESS: 0, OVERDUE: 0, COMPLETED: 0, DEFAULT: 0 }
      this.list.forEach(r => { if (statusMap[r.contractStatus] !== undefined) statusMap[r.contractStatus]++ })
      this.charts[0].setOption({
        tooltip: { trigger: 'item' },
        legend: { bottom: 4 },
        series: [{
          type: 'pie', radius: ['40%', '70%'], center: ['50%', '44%'],
          data: [
            { value: statusMap.IN_PROGRESS, name: '执行中', itemStyle: { color: this.ipSecondary } },
            { value: statusMap.OVERDUE, name: '逾期', itemStyle: { color: '#CF1322' } },
            { value: statusMap.COMPLETED, name: '已完成', itemStyle: { color: '#52C41A' } },
            { value: statusMap.DEFAULT, name: '违约', itemStyle: { color: '#7B0000' } },
          ],
          label: { formatter: '{b}: {c}份' },
        }],
      })
      // TOP付款进度条形图
      const sorted = [...this.list].sort((a, b) => a.paymentRatio - b.paymentRatio).slice(0, 8)
      this.charts[1].setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: 110, right: 60, top: 8, bottom: 30 },
        xAxis: { type: 'value', max: 100, name: '付款进度(%)' },
        yAxis: {
          type: 'category',
          data: sorted.map(r => r.contractName.length > 10 ? r.contractName.substring(0, 10) : r.contractName),
        },
        series: [{
          type: 'bar',
          data: sorted.map(r => r.paymentRatio),
          itemStyle: { color: (p) => p.value === 100 ? '#FA8C16' : (p.value === 0 ? '#CF1322' : this.ipSecondary) },
          label: { show: true, position: 'right', formatter: (p) => p.value + '%' },
        }],
      })
    },
  },
}
</script>

<style lang="scss" scoped>
.page-container { padding: 16px; background: #F0F2F5; min-height: calc(100vh - 84px); }
.page-header {
  padding: 18px 24px; margin-bottom: 16px;
  background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 60%, var(--ip-bright, #1677FF) 100%);
  border-radius: 8px; color: #fff;
  .page-title { margin: 0 0 4px 0; font-size: 18px; font-weight: 700; i { margin-right: 8px; } }
  .page-desc { margin: 0; font-size: 13px; opacity: .85; }
}
.stat-row { margin-bottom: 16px; }
.stat-card { background: #fff; border-radius: 6px; padding: 14px 16px; box-shadow: 0 1px 4px rgba(0,0,0,.07);
  .stat-val { font-size: 22px; font-weight: 700; }
  .stat-lb { font-size: 12px; color: #8C8C8C; margin-top: 4px; }
}
.chart-row { margin-bottom: 16px; }
.chart-sm { height: 220px; }
.search-card { margin-bottom: 12px; }
.card-hd { font-size: 14px; font-weight: 600; color: #303133; }
::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); font-weight: 600; }
::v-deep .el-table .row-violation td { background: #FFF1F0 !important; }
::v-deep .el-table .row-overdue td { background: #FFF7E6 !important; }
::v-deep .el-card { border-radius: 6px; }
</style>
