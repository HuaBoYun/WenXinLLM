<template>
  <div class="counterparty-wrap">
    <!-- Banner -->
    <div class="page-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-left">
        <div class="banner-title">合同对方信用监控</div>
        <div class="banner-sub">持续跟踪集团合同对方信用状态，实时掌握工商异常、失信被执行、信用评级变化，支持合同决策</div>
      </div>
      <div class="banner-right">
        <div v-for="item in bannerStats" :key="item.label" class="banner-stat">
          <span class="stat-num">{{ item.value }}</span>
          <span class="stat-label">{{ item.label }}</span>
        </div>
      </div>
    </div>

    <!-- 统计卡 -->
    <el-row :gutter="16" style="margin-bottom:16px">
      <el-col v-for="card in statCards" :key="card.label" :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-card-inner">
            <i :class="card.icon" :style="{color:card.color,fontSize:'28px'}"></i>
            <div class="stat-info">
              <div class="stat-value" :style="{color:card.color}">{{ card.value }}</div>
              <div class="stat-label">{{ card.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询栏 -->
    <el-card shadow="never" style="margin-bottom:16px">
      <el-row :gutter="12">
        <el-col :span="6">
          <el-input v-model="query.counterpartyName" placeholder="对方单位名称" clearable size="small" prefix-icon="el-icon-search"/>
        </el-col>
        <el-col :span="4">
          <el-select v-model="query.creditStatus" placeholder="信用状态" clearable size="small" style="width:100%">
            <el-option label="信用良好" value="GOOD"/>
            <el-option label="轻微异常" value="MINOR"/>
            <el-option label="信用预警" value="WARNING"/>
            <el-option label="黑名单" value="BLACKLIST"/>
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-select v-model="query.industry" placeholder="行业分类" clearable size="small" style="width:100%">
            <el-option v-for="i in industries" :key="i" :label="i" :value="i"/>
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" size="small" icon="el-icon-search" @click="handleSearch">查询</el-button>
          <el-button size="small" icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 图表区 -->
    <el-row :gutter="16" style="margin-bottom:16px">
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header" class="card-header"><span>信用状态分布</span></div>
          <div ref="creditChart" style="height:220px"></div>
        </el-card>
      </el-col>
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header" class="card-header"><span>失信记录类型分布</span></div>
          <div ref="failChart" style="height:220px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 对方信用列表 -->
    <el-card shadow="never">
      <div slot="header" class="card-header">
        <span>对方信用监控列表</span>
        <el-tag v-if="blacklistCount > 0" type="danger" size="small" style="margin-left:8px">⚠ {{ blacklistCount }} 家黑名单</el-tag>
        <el-button type="primary" size="mini" icon="el-icon-download" style="margin-left:auto" :loading="exportLoading" @click="handleExport">导出</el-button>
      </div>
      <el-table
        :data="filteredList"
        :row-class-name="tableRowClass"
        border size="small" style="width:100%">
        <el-table-column label="对方单位" prop="counterpartyName" min-width="180" fixed show-overflow-tooltip>
          <template slot-scope="{row}">
            <span :style="row.creditStatus==='BLACKLIST'?{fontWeight:'700',color:'#820014'}:{}">{{ row.counterpartyName }}</span>
            <el-tag v-if="row.creditStatus==='BLACKLIST'" type="danger" size="mini" style="margin-left:4px">黑名单</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="行业" prop="industry" width="110"/>
        <el-table-column label="关联合同数" prop="contractCount" width="110" align="center">
          <template slot-scope="{row}"><span style="color:#1677FF;font-weight:600">{{ row.contractCount }}</span> 份</template>
        </el-table-column>
        <el-table-column label="合同金额(万)" prop="totalAmount" width="130" align="right">
          <template slot-scope="{row}"><span style="font-weight:600;color:#0050A0">{{ row.totalAmount.toLocaleString() }}</span></template>
        </el-table-column>
        <el-table-column label="信用状态" prop="creditStatus" width="110" align="center">
          <template slot-scope="{row}">
            <el-tag :type="creditStatusType(row.creditStatus)" size="mini" :effect="row.creditStatus==='BLACKLIST'?'dark':'light'">
              {{ creditStatusLabel(row.creditStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="最新预警" prop="latestWarn" min-width="200" show-overflow-tooltip>
          <template slot-scope="{row}">
            <span v-if="row.latestWarn !== '—'" style="color:#F5222D;font-size:12px">⚠ {{ row.latestWarn }}</span>
            <span v-else style="color:#52C41A">✓ 无预警</span>
          </template>
        </el-table-column>
        <el-table-column label="最近更新" prop="updateTime" width="120"/>
        <el-table-column label="操作" width="130" fixed="right">
          <template slot-scope="{row}">
            <el-button type="text" size="mini" icon="el-icon-document" @click="showDetail(row)">信用详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        style="margin-top:15px;text-align:right"
        :current-page="query.pageNumber"
        :page-sizes="[10, 15, 30, 50]"
        :page-size="query.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
      />
    </el-card>

    <!-- 信用详情抽屉 -->
    <el-drawer title="对方信用详情" :visible.sync="detailVisible" size="480px" append-to-body>
      <div v-if="currentRow" v-loading="detailLoading" style="padding:20px">
        <div class="detail-header" :class="'status-'+currentRow.creditStatus">
          <div class="detail-name">{{ currentRow.counterpartyName }}</div>
          <el-tag :type="creditStatusType(currentRow.creditStatus)" :effect="currentRow.creditStatus==='BLACKLIST'?'dark':'light'">
            {{ creditStatusLabel(currentRow.creditStatus) }}
          </el-tag>
        </div>
        <el-descriptions :column="2" size="small" border style="margin-top:16px;margin-bottom:20px">
          <el-descriptions-item label="行业分类">{{ currentRow.industry }}</el-descriptions-item>
          <el-descriptions-item label="关联合同">{{ currentRow.contractCount }} 份</el-descriptions-item>
          <el-descriptions-item label="合同总额">{{ (currentRow.totalAmount || 0).toLocaleString() }} 万元</el-descriptions-item>
          <el-descriptions-item label="最近更新">{{ currentRow.updateTime }}</el-descriptions-item>
        </el-descriptions>
        <div v-if="currentRow.latestWarn && currentRow.latestWarn !== '—'" class="warn-box">
          <i class="el-icon-warning" style="color:#F5222D;margin-right:6px"></i>
          <span>{{ currentRow.latestWarn }}</span>
        </div>
        <div v-else class="ok-box">
          <i class="el-icon-circle-check" style="color:#52C41A;margin-right:6px"></i>
          <span>信用状态良好，无预警记录</span>
        </div>
        <div style="margin-top:20px;padding:16px;background:#f9f9f9;border-radius:6px">
          <div style="font-weight:600;color:#333;margin-bottom:8px">风险提示</div>
          <div v-if="currentRow.creditStatus==='BLACKLIST'" style="color:#F5222D;font-size:13px">
            ⚠ 该对方单位已被列入严重失信名单，建议暂停新签合同，并对现有合同加强监控。
          </div>
          <div v-else-if="currentRow.creditStatus==='WARNING'" style="color:#FA8C16;font-size:13px">
            ⚠ 该对方单位存在信用预警，建议加强付款条款约束，并监控后续信用变化。
          </div>
          <div v-else style="color:#52C41A;font-size:13px">✓ 信用状况良好，可正常推进合作。</div>
        </div>
        <!-- 弹窗查看按钮 -->
        <div style="margin-top:20px;display:flex;gap:10px">
          <el-button type="primary" size="small" icon="el-icon-document" @click="openContractDialog(currentRow.counterpartyName)">查看关联合同</el-button>
          <el-button type="warning" size="small" icon="el-icon-s-claim" @click="openDisputeDialog(currentRow.counterpartyName)">查看纠纷记录</el-button>
        </div>
      </div>
    </el-drawer>

    <!-- 关联合同弹窗 -->
    <el-dialog :title="'关联合同 - ' + dialogCounterparty" :visible.sync="contractDialogVisible" width="800px" append-to-body>
      <el-table v-loading="contractDialogLoading" :data="contractDialogList" border size="small" max-height="400">
        <el-table-column label="合同编号" prop="contractCode" width="160"/>
        <el-table-column label="合同名称" prop="contractName" min-width="180" show-overflow-tooltip/>
        <el-table-column label="合同类型" prop="contractType" width="90" align="center"/>
        <el-table-column label="金额(万)" prop="contractAmount" width="100" align="right">
          <template slot-scope="{row}"><span style="font-weight:600">{{ Number(row.contractAmount || 0).toLocaleString() }}</span></template>
        </el-table-column>
        <el-table-column label="签订日期" prop="signDate" width="110" align="center"/>
        <el-table-column label="到期日期" prop="expiryDate" width="110" align="center"/>
        <el-table-column label="状态" prop="contractStatus" width="80" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.contractStatus==='ACTIVE'?'success':row.contractStatus==='TERMINATED'?'danger':'info'" size="mini">
              {{ {ACTIVE:'执行中',COMPLETED:'已完成',TERMINATED:'已终止',DRAFT:'草稿'}[row.contractStatus] || row.contractStatus }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      <div v-if="!contractDialogLoading && contractDialogList.length === 0" style="text-align:center;padding:30px;color:#999">暂无关联合同记录</div>
    </el-dialog>

    <!-- 纠纷记录弹窗 -->
    <el-dialog :title="'纠纷记录 - ' + dialogCounterparty" :visible.sync="disputeDialogVisible" width="800px" append-to-body>
      <el-table v-loading="disputeDialogLoading" :data="disputeDialogList" border size="small" max-height="400">
        <el-table-column label="纠纷类型" prop="disputeType" width="110"/>
        <el-table-column label="案件描述" prop="disputeReason" min-width="200" show-overflow-tooltip/>
        <el-table-column label="涉诉金额(万)" prop="disputeAmount" width="110" align="right">
          <template slot-scope="{row}"><span style="color:#F5222D;font-weight:600">{{ Number(row.disputeAmount || 0).toLocaleString() }}</span></template>
        </el-table-column>
        <el-table-column label="案件状态" prop="caseStatus" width="90" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.caseStatus==='CLOSED'?'success':'warning'" size="mini">
              {{ {NEGOTIATION:'协商中',MEDIATION:'调解中',LITIGATION:'诉讼中',TRIAL:'审理中',APPEAL:'上诉中',EXECUTION:'执行中',CLOSED:'已结案'}[row.caseStatus] || row.caseStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="立案日期" prop="filingDate" width="110" align="center"/>
        <el-table-column label="法院" prop="courtName" width="130" show-overflow-tooltip/>
      </el-table>
      <div v-if="!disputeDialogLoading && disputeDialogList.length === 0" style="text-align:center;padding:30px;color:#999">暂无纠纷诉讼记录</div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import { getCounterpartyList, getCounterpartyDetail, getContractList, getDisputeList } from '@/api/stateAssets/contractPenetration'

export default {
  name: 'ContractCounterparty',
  data() {
    return {
      list: [],
      total: 0,
      query: { counterpartyName: '', creditStatus: '', industry: '', pageNumber: 1, pageSize: 15 },
      industries: ['采购服务', '销售贸易', '建筑工程', '技术服务', '综合业务'],
      detailVisible: false,
      detailLoading: false,
      currentRow: null,
      creditChart: null,
      failChart: null,
      exportLoading: false,
      // 关联合同弹窗
      contractDialogVisible: false,
      contractDialogLoading: false,
      contractDialogList: [],
      dialogCounterparty: '',
      // 纠纷记录弹窗
      disputeDialogVisible: false,
      disputeDialogLoading: false,
      disputeDialogList: [],
    }
  },
  computed: {
    ...mapGetters({ theme: 'settings/theme' }),
    themeColor() {
      const map = { red: '#e50113', green: '#41b584', ocean: '#1890ff', white: '#1890ff', default: '#1890ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#1890ff'
    },
    themeColorLight() {
      const map = { red: '#fff1f0', green: '#f6ffed', ocean: '#e6f7ff', white: '#e6f7ff', default: '#e6f7ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#e6f7ff'
    },
    filteredList() {
      return this.list
    },
    blacklistCount() { return this.list.filter(r => r.creditStatus === 'BLACKLIST').length },
    statCards() {
      const d = this.list
      return [
        { label: '监控对方总数', value: this.total, color: '#0050A0', icon: 'el-icon-user' },
        { label: '信用良好', value: d.filter(r => r.creditStatus === 'GOOD').length, color: '#52C41A', icon: 'el-icon-circle-check' },
        { label: '信用预警', value: d.filter(r => r.creditStatus === 'WARNING').length, color: '#FA8C16', icon: 'el-icon-warning' },
        { label: '已列失信', value: this.blacklistCount, color: '#F5222D', icon: 'el-icon-remove' },
      ]
    },
    bannerStats() {
      return [
        { label: '监控对方总数', value: this.total },
        { label: '预警/黑名单', value: this.list.filter(r => ['WARNING', 'BLACKLIST'].includes(r.creditStatus)).length },
        { label: '正常占比', value: this.list.length ? Math.round(this.list.filter(r => r.creditStatus === 'GOOD').length / this.list.length * 100) + '%' : '—' },
      ]
    },
  },
  mounted() {
    this.fetchData()
  },
  beforeDestroy() {
    if (this.creditChart) this.creditChart.dispose()
    if (this.failChart) this.failChart.dispose()
  },
  methods: {
    async fetchData() {
      try {
        const res = await getCounterpartyList(this.query)
        if (res && res.result === 200 && res.data) {
          const rawList = res.data.tlist || []
          this.total = res.data.totalRecord || 0
          this.list = rawList.map(item => {
            const totalAmount = parseFloat(item.totalAmount) || 0
            // latestWarn: 从 creditCode 推导预警信息
            let latestWarn = '—'
            if (item.creditStatus === 'BLACKLIST') {
              latestWarn = item.creditCode || '已被列入失信被执行人名单'
            } else if (item.creditStatus === 'WARNING') {
              latestWarn = item.creditCode || '存在信用预警记录'
            } else if (item.creditStatus === 'MINOR') {
              latestWarn = item.creditCode || '存在轻微工商异常'
            }
            return {
              ...item,
              totalAmount,
              contractCount: item.contractCount || 0,
              industry: item.industry || '未分类',
              latestWarn,
              updateTime: item.updateTime || '—'
            }
          })
        } else {
          this.list = []
          this.total = 0
          this.$message.warning('暂无数据')
        }
      } catch (e) {
        this.list = []
        this.total = 0
        this.$message.error('查询失败，请检查网络连接')
      }
      this.$nextTick(() => { this.initCreditChart(); this.initFailChart() })
    },
    handleSearch() { this.query.pageNumber = 1; this.fetchData() },
    handleReset() { this.query = { counterpartyName: '', creditStatus: '', industry: '', pageNumber: 1, pageSize: 15 }; this.fetchData() },
    handlePageChange(val) { this.query.pageNumber = val; this.fetchData() },
    handleSizeChange(val) { this.query.pageSize = val; this.query.pageNumber = 1; this.fetchData() },
    async showDetail(row) {
      this.currentRow = { ...row }
      this.detailVisible = true
      this.detailLoading = true
      try {
        const res = await getCounterpartyDetail(row.counterpartyName)
        if (res && res.result === 200 && res.data) {
          this.currentRow = { ...this.currentRow, ...res.data }
        }
      } catch (e) {
        // 使用列表行数据作为兜底
      } finally {
        this.detailLoading = false
      }
    },
    async openContractDialog(name) {
      this.dialogCounterparty = name
      this.contractDialogVisible = true
      this.contractDialogLoading = true
      this.contractDialogList = []
      try {
        const res = await getContractList({ counterpartyName: name, pageNumber: 1, pageSize: 50 })
        if (res && res.result === 200 && res.data) {
          this.contractDialogList = (res.data.tlist || []).map(item => ({
            contractCode: item.contractCode || item.contractNo || '',
            contractName: item.contractName || '',
            contractType: item.contractType || '',
            contractAmount: item.contractAmount || item.amount || 0,
            signDate: item.signDate || '',
            expiryDate: item.expiryDate || item.expireDate || '',
            contractStatus: item.contractStatus || 'ACTIVE',
          }))
        }
      } catch (e) {
        this.$message.error('查询关联合同失败')
      } finally {
        this.contractDialogLoading = false
      }
    },
    async openDisputeDialog(name) {
      this.dialogCounterparty = name
      this.disputeDialogVisible = true
      this.disputeDialogLoading = true
      this.disputeDialogList = []
      try {
        const res = await getDisputeList({ companyName: name, pageNumber: 1, pageSize: 50 })
        if (res && res.result === 200 && res.data) {
          this.disputeDialogList = (res.data.tlist || []).map(item => ({
            disputeType: item.disputeType || item.warnType || '',
            disputeReason: item.disputeReason || item.caseName || '',
            disputeAmount: item.disputeAmount || item.amount || 0,
            caseStatus: item.caseStatus || item.stage || '',
            filingDate: item.filingDate || '',
            courtName: item.courtName || '',
          }))
        }
      } catch (e) {
        this.$message.error('查询纠纷记录失败')
      } finally {
        this.disputeDialogLoading = false
      }
    },
    handleExport() {
      this.exportLoading = true
      try {
        // 前端直接导出当前列表数据为CSV
        const headers = ['对方单位', '行业分类', '关联合同数', '合同金额(万)', '信用状态', '最新预警', '最近更新']
        const statusMap = { GOOD: '信用良好', MINOR: '轻微异常', WARNING: '信用预警', BLACKLIST: '黑名单' }
        const rows = this.list.map(r => [
          r.counterpartyName,
          r.industry,
          r.contractCount,
          r.totalAmount,
          statusMap[r.creditStatus] || r.creditStatus,
          r.latestWarn === '—' ? '无预警' : r.latestWarn,
          r.updateTime,
        ])
        const csvContent = '\uFEFF' + [headers, ...rows].map(row => row.map(cell => '"' + String(cell).replace(/"/g, '""') + '"').join(',')).join('\n')
        const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
        const url = window.URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = '对方信用监控_' + new Date().toISOString().slice(0, 10) + '.csv'
        a.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (e) {
        this.$message.error('导出失败')
      } finally {
        this.exportLoading = false
      }
    },
    creditStatusLabel(s) {
      const m = { GOOD: '信用良好', MINOR: '轻微异常', WARNING: '信用预警', BLACKLIST: '黑名单' }
      return m[s] || s
    },
    creditStatusType(s) {
      const m = { GOOD: 'success', MINOR: 'warning', WARNING: 'warning', BLACKLIST: 'danger' }
      return m[s] || ''
    },
    tableRowClass({ row }) {
      if (row.creditStatus === 'BLACKLIST') return 'row-credit-blacklist'
      if (row.creditStatus === 'WARNING') return 'row-credit-warning'
      return ''
    },
    initCreditChart() {
      const el = this.$refs.creditChart
      if (!el) return
      if (this.creditChart) this.creditChart.dispose()
      this.creditChart = echarts.init(el)
      const statusCount = {}
      this.list.forEach(r => { if (r.creditStatus) statusCount[r.creditStatus] = (statusCount[r.creditStatus] || 0) + 1 })
      const colorMap = { GOOD: '#52C41A', MINOR: '#FAAD14', WARNING: '#FA8C16', BLACKLIST: '#F5222D' }
      const labelMap = { GOOD: '信用良好', MINOR: '轻微异常', WARNING: '信用预警', BLACKLIST: '黑名单' }
      const pieData = Object.keys(statusCount).length > 0
        ? Object.entries(statusCount).map(([k, v]) => ({ name: labelMap[k] || k, value: v, itemStyle: { color: colorMap[k] || '#ccc' } }))
        : [{ name: '暂无数据', value: 1, itemStyle: { color: '#f0f0f0' } }]
      this.creditChart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        legend: { bottom: 0 },
        series: [{
          type: 'pie', radius: ['40%', '65%'], center: ['50%', '45%'],
          data: pieData,
          label: { formatter: '{b}\n{d}%', fontSize: 11 },
        }],
      })
    },
    initFailChart() {
      const el = this.$refs.failChart
      if (!el) return
      if (this.failChart) this.failChart.dispose()
      this.failChart = echarts.init(el)
      // 统计失信/预警对方的行业分布，确保所有行业类型都出现
      const warnList = this.list.filter(r => ['WARNING', 'BLACKLIST', 'MINOR'].includes(r.creditStatus))
      // 初始化所有行业计数为0
      const industryCount = {}
      this.industries.forEach(ind => { industryCount[ind] = 0 })
      warnList.forEach(r => {
        const ind = r.industry || '未分类'
        industryCount[ind] = (industryCount[ind] || 0) + 1
      })
      const categories = Object.keys(industryCount)
      const colorArr = ['#F5222D', '#FA8C16', '#FAAD14', '#1677FF', '#722ED1', '#52C41A']
      this.failChart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: 100, right: 30, top: 15, bottom: 20 },
        xAxis: { type: 'value' },
        yAxis: { type: 'category', data: categories },
        series: [{
          type: 'bar', barWidth: 14,
          data: categories.map((cat, i) => ({
            value: industryCount[cat],
            itemStyle: { color: colorArr[i % colorArr.length] }
          })),
          label: { show: true, position: 'right', formatter: '{c} 家' },
        }],
      })
    },
  },
}
</script>

<style scoped lang="scss">
.counterparty-wrap { padding: 16px; background: #f5f7fa; min-height: 100vh; }

.page-banner {
  border-radius: 8px; padding: 24px 28px; margin-bottom: 16px;
  display: flex; justify-content: space-between; align-items: center;
  .banner-title { font-size: 22px; font-weight: 700; color: #fff; margin-bottom: 6px; }
  .banner-sub { font-size: 13px; color: rgba(255,255,255,0.75); }
  .banner-right { display: flex; gap: 32px; }
  .banner-stat { text-align: center; color: #fff;
    .stat-num { display: block; font-size: 26px; font-weight: 700; }
    .stat-label { font-size: 12px; opacity: 0.8; }
  }
}

.stat-card { border-radius: 8px; }
.stat-card-inner { display: flex; align-items: center; gap: 14px;
  .stat-info { .stat-value { font-size: 26px; font-weight: 700; } .stat-label { font-size: 13px; color: #666; margin-top: 2px; } }
}

.card-header { display: flex; align-items: center; font-weight: 600; }

.detail-header { padding: 16px; border-radius: 8px; margin-bottom: 4px; display: flex; justify-content: space-between; align-items: center;
  .detail-name { font-size: 16px; font-weight: 700; }
  &.status-BLACKLIST { background: #FFF1F0; }
  &.status-WARNING { background: #FFF7E6; }
  &.status-GOOD { background: #F6FFED; }
}
.warn-box { padding: 12px 16px; background: #FFF1F0; border-radius: 6px; color: #F5222D; font-size: 13px; border: 1px solid #FFA39E; }
.ok-box { padding: 12px 16px; background: #F6FFED; border-radius: 6px; color: #52C41A; font-size: 13px; border: 1px solid #B7EB8F; }

::v-deep .row-credit-blacklist td { background: #FFF1F0 !important; font-weight: 600; }
::v-deep .row-credit-warning td { background: #FFFBE6 !important; }
</style>
