<template>
  <div class="trade-review">
    <div class="page-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-left">
        <h2 class="banner-title">产权交易合规审查</h2>
        <p class="banner-sub">进场交易监控 · 资产评估覆盖率 · 价格偏离分析 · 违规预警</p>
      </div>
    </div>

    <!-- 合规规则说明 -->
    <el-row :gutter="12" style="margin-bottom:16px;">
      <el-col :span="8">
        <el-alert type="error" :closable="false" show-icon>
          <span slot="title">进场交易要求：国有产权转让须通过产权交易所进行，否则触发 <b>PRO-W02</b> 预警</span>
        </el-alert>
      </el-col>
      <el-col :span="8">
        <el-alert type="warning" :closable="false" show-icon>
          <span slot="title">评估要求：产权交易须有独立机构资产评估报告，否则触发 <b>PRO-W04</b> 预警</span>
        </el-alert>
      </el-col>
      <el-col :span="8">
        <el-alert type="warning" :closable="false" show-icon>
          <span slot="title">价格要求：成交价格不得低于评估价值 <b>90%</b>，低于则触发 <b>PRO-W03</b> 预警</span>
        </el-alert>
      </el-col>
    </el-row>

    <!-- 统计卡 -->
    <el-row :gutter="16" class="kpi-row">
      <el-col :span="6" v-for="kpi in kpiList" :key="kpi.key">
        <el-card class="kpi-card" shadow="hover">
          <div class="kpi-value" :style="{ color: kpi.color }">{{ kpi.value }}{{ kpi.unit }}</div>
          <div class="kpi-label">{{ kpi.label }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" inline size="small">
        <el-form-item label="企业名称">
          <el-input v-model="queryForm.companyName" placeholder="输入企业名称" clearable style="width:180px;"></el-input>
        </el-form-item>
        <el-form-item label="交易方式">
          <el-select v-model="queryForm.transMethod" placeholder="全部" clearable style="width:140px;">
            <el-option label="进场交易" value="EXCHANGE"></el-option>
            <el-option label="协议转让" value="AGREEMENT"></el-option>
            <el-option label="拍卖" value="AUCTION"></el-option>
            <el-option label="无偿划转" value="FREE_TRANSFER"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="合规状态">
          <el-select v-model="queryForm.complianceStatus" placeholder="全部" clearable style="width:120px;">
            <el-option label="合规" value="COMPLIANT"></el-option>
            <el-option label="问题" value="ISSUE"></el-option>
            <el-option label="违规" value="VIOLATION"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="loadData">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
          <el-button type="warning" icon="el-icon-download" @click="handleExport">导出</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-row :gutter="16" style="margin-bottom:16px;">
      <!-- 表格 -->
      <el-col :span="16">
        <el-card shadow="never">
          <div class="toolbar">
            <el-button type="primary" size="small" icon="el-icon-plus" @click="openDialog()">新增交易记录</el-button>
            <span class="total-tip">共 <b>{{ total }}</b> 条</span>
          </div>
          <el-table :data="tableData" size="small" border :row-class-name="rowClass" @row-click="showDetail">
            <el-table-column label="交易编号" prop="transNo" width="150" show-overflow-tooltip></el-table-column>
            <el-table-column label="企业名称" prop="companyName" min-width="150" show-overflow-tooltip></el-table-column>
            <el-table-column label="交易方式" prop="transMethod" width="100" align="center">
              <template slot-scope="{row}">
                <el-tag :type="row.isExchangeTraded === '1' ? 'success' : 'danger'" size="mini">{{ transMethodLabel(row.transMethod) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="交易金额(万)" prop="transAmount" width="110" align="right">
              <template slot-scope="{row}">{{ row.transAmount ? Number(row.transAmount).toLocaleString() : '—' }}</template>
            </el-table-column>
            <el-table-column label="评估价值(万)" prop="appraisalValue" width="110" align="right">
              <template slot-scope="{row}">{{ row.appraisalValue ? Number(row.appraisalValue).toLocaleString() : '未评估' }}</template>
            </el-table-column>
            <el-table-column label="价格比率" prop="priceRatio" width="90" align="center">
              <template slot-scope="{row}">
                <span v-if="row.priceRatio" :style="{ color: Number(row.priceRatio) < 90 ? '#F5222D' : Number(row.priceRatio) < 95 ? '#FA8C16' : '#52C41A', fontWeight:'700' }">
                  {{ row.priceRatio }}%
                </span>
                <span v-else style="color:#aaa;">—</span>
              </template>
            </el-table-column>
            <el-table-column label="合规状态" prop="complianceStatus" width="90" align="center">
              <template slot-scope="{row}">
                <el-tag :type="complianceTagType(row.complianceStatus)" size="mini">{{ complianceLabel(row.complianceStatus) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="交易日期" prop="transDate" width="110"></el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <!-- 价格偏离散点图 -->
      <el-col :span="8">
        <el-card shadow="never" style="height:100%;">
          <div slot="header" class="card-header">
            <span>价格偏离分析</span>
            <el-tag size="mini" type="danger">低于90%=违规</el-tag>
          </div>
          <div ref="scatterChart" style="height:340px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 新增/编辑弹窗 -->
    <el-dialog title="新增产权交易记录" :visible.sync="dialogVisible" width="560px">
      <el-form :model="form" ref="formRef" label-width="110px" size="small">
        <el-form-item label="企业名称" :rules="[{required:true}]" prop="companyName">
          <el-input v-model="form.companyName" placeholder="请输入企业名称"></el-input>
        </el-form-item>
        <el-form-item label="交易方式">
          <el-select v-model="form.transMethod" style="width:100%;">
            <el-option label="进场交易（产权交易所）" value="EXCHANGE"></el-option>
            <el-option label="协议转让" value="AGREEMENT"></el-option>
            <el-option label="拍卖" value="AUCTION"></el-option>
            <el-option label="无偿划转" value="FREE_TRANSFER"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="交易金额(万元)">
          <el-input-number v-model="form.transAmount" :min="0" :precision="2" style="width:100%;"></el-input-number>
        </el-form-item>
        <el-form-item label="评估价值(万元)">
          <el-input-number v-model="form.appraisalValue" :min="0" :precision="2" style="width:100%;"></el-input-number>
        </el-form-item>
        <el-form-item label="是否评估">
          <el-radio-group v-model="form.hasAppraisal">
            <el-radio label="Y">已评估</el-radio>
            <el-radio label="N">未评估</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="交易对手">
          <el-input v-model="form.counterparty" placeholder="请输入交易对手名称"></el-input>
        </el-form-item>
        <el-form-item label="交易日期">
          <el-date-picker v-model="form.transDate" type="date" value-format="yyyy-MM-dd" style="width:100%;"></el-date-picker>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">保存</el-button>
      </span>
    </el-dialog>

    <!-- 合规问题详情抽屉 -->
    <el-drawer title="交易合规详情" :visible.sync="detailDrawer" direction="rtl" size="460px">
      <div class="detail-wrap" v-if="currentRow">
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="交易编号" :span="2">{{ currentRow.transNo || '—' }}</el-descriptions-item>
          <el-descriptions-item label="企业名称" :span="2">{{ currentRow.companyName }}</el-descriptions-item>
          <el-descriptions-item label="交易方式">{{ transMethodLabel(currentRow.transMethod) }}</el-descriptions-item>
          <el-descriptions-item label="交易对手">{{ currentRow.counterparty || '—' }}</el-descriptions-item>
          <el-descriptions-item label="交易金额">{{ currentRow.transAmount ? Number(currentRow.transAmount).toLocaleString() + ' 万元' : '—' }}</el-descriptions-item>
          <el-descriptions-item label="评估价值">{{ currentRow.appraisalValue ? Number(currentRow.appraisalValue).toLocaleString() + ' 万元' : '未评估' }}</el-descriptions-item>
          <el-descriptions-item label="价格比率">
            <span v-if="currentRow.priceRatio" :style="{ color: Number(currentRow.priceRatio) < 90 ? '#F5222D' : '#52C41A', fontWeight:'700' }">{{ currentRow.priceRatio }}%</span>
            <span v-else>—</span>
          </el-descriptions-item>
          <el-descriptions-item label="合规状态">
            <el-tag :type="complianceTagType(currentRow.complianceStatus)" size="mini">{{ complianceLabel(currentRow.complianceStatus) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="交易日期" :span="2">{{ currentRow.transDate || '—' }}</el-descriptions-item>
        </el-descriptions>
        <div v-if="currentRow.complianceIssues" class="issue-box">
          <div class="issue-title"><i class="el-icon-warning" style="color:#F5222D;"></i> 合规问题详情</div>
          <div class="issue-content">{{ currentRow.complianceIssues }}</div>
          <div class="issue-suggest">
            <b>处置建议：</b>{{ getIssueSuggest(currentRow.complianceStatus) }}
          </div>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import { getTradeReviewList, addTradeReview, exportTradeReview } from '@/api/stateAssets/tradeReview'

export default {
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
  },
  name: 'PropertyTradeReview',
  data() {
    return {
      queryForm: { companyName: '', transMethod: '', complianceStatus: '', pageNumber: 1, pageSize: 15 },
      tableData: [],
      total: 0,
      kpiList: [],
      dialogVisible: false,
      detailDrawer: false,
      currentRow: null,
      form: { companyName: '', transMethod: 'EXCHANGE', transAmount: 0, appraisalValue: 0, hasAppraisal: 'N', counterparty: '', transDate: '' },
    }
  },
  mounted() { this.loadData() },
  methods: {
    async loadData() {
      try {
        const res = await getTradeReviewList(this.queryForm)
        if (res.result === 200 && res.data) {
          this.tableData = res.data.tlist || []
          this.total = res.data.totalRecord || 0
        } else {
          this.tableData = []
          this.total = 0
        }
      } catch {
        this.tableData = []
        this.total = 0
      }
      this.buildKpi()
      this.$nextTick(() => this.initScatterChart())
    },
    buildKpi() {
      const d = this.tableData
      const exchTraded = d.filter(r => r.isExchangeTraded === '1').length
      const appraised = d.filter(r => r.hasAppraisal === 'Y').length
      const issues = d.filter(r => r.complianceStatus && r.complianceStatus !== 'COMPLIANT').length
      this.kpiList = [
        { key: 'total', label: '交易总数', value: d.length, unit: '笔', color: '#1677FF' },
        { key: 'exchange', label: '进场交易率', value: d.length ? Math.round(exchTraded / d.length * 1000) / 10 : 0, unit: '%', color: (d.length && exchTraded / d.length < 0.8) ? '#F5222D' : '#52C41A' },
        { key: 'appraisal', label: '评估覆盖率', value: d.length ? Math.round(appraised / d.length * 1000) / 10 : 0, unit: '%', color: (d.length && appraised / d.length < 0.85) ? '#FA8C16' : '#52C41A' },
        { key: 'issues', label: '合规问题数', value: issues, unit: '笔', color: issues > 0 ? '#F5222D' : '#52C41A' },
      ]
    },
    initScatterChart() {
      if (!this.$refs.scatterChart) return
      const chart = echarts.init(this.$refs.scatterChart)
      const compliants = this.tableData.filter(r => r.priceRatio && r.complianceStatus === 'COMPLIANT').map(r => [r.appraisalValue, r.transAmount])
      const issues = this.tableData.filter(r => r.priceRatio && r.complianceStatus !== 'COMPLIANT').map(r => [r.appraisalValue, r.transAmount])
      const maxVal = this.tableData.filter(r => r.appraisalValue).map(r => Number(r.appraisalValue))
      const maxAppraisal = maxVal.length > 0 ? Math.max(...maxVal) * 1.1 : 1000
      chart.setOption({
        tooltip: { trigger: 'item', formatter: (p) => `评估价: ${p.data[0]}万<br/>成交价: ${p.data[1]}万` },
        legend: { bottom: 0 },
        xAxis: { name: '评估价值(万元)', type: 'value' },
        yAxis: { name: '成交价格(万元)', type: 'value' },
        series: [
          { name: '合规', type: 'scatter', data: compliants, itemStyle: { color: '#52C41A' }, symbolSize: 10 },
          { name: '问题/违规', type: 'scatter', data: issues, itemStyle: { color: '#F5222D' }, symbolSize: 12 },
          { name: '90%警戒线', type: 'line', data: [[0, 0], [maxAppraisal, maxAppraisal * 0.9]], lineStyle: { color: '#F5222D', type: 'dashed', width: 1.5 }, symbol: 'none', silent: true },
          { name: '100%基准线', type: 'line', data: [[0, 0], [maxAppraisal, maxAppraisal]], lineStyle: { color: '#8C8C8C', type: 'dashed', width: 1 }, symbol: 'none', silent: true },
        ]
      })
    },
    resetQuery() {
      this.queryForm = { companyName: '', transMethod: '', complianceStatus: '', pageNumber: 1, pageSize: 15 }
      this.loadData()
    },
    openDialog() {
      this.form = { companyName: '', transMethod: 'EXCHANGE', transAmount: 0, appraisalValue: 0, hasAppraisal: 'N', counterparty: '', transDate: '' }
      this.dialogVisible = true
    },
    async submitForm() {
      // 自动计算价格比率和合规状态
      const formData = { ...this.form }
      if (formData.transMethod === 'EXCHANGE') {
        formData.isExchangeTraded = '1'
      } else {
        formData.isExchangeTraded = '0'
      }
      if (formData.appraisalValue && formData.transAmount && Number(formData.appraisalValue) > 0) {
        formData.priceRatio = Math.round(Number(formData.transAmount) / Number(formData.appraisalValue) * 10000) / 100
        formData.hasAppraisal = 'Y'
        if (formData.priceRatio < 90) {
          formData.complianceStatus = 'VIOLATION'
        } else if (formData.priceRatio < 95) {
          formData.complianceStatus = 'ISSUE'
        } else {
          formData.complianceStatus = 'COMPLIANT'
        }
      } else {
        formData.priceRatio = null
        formData.complianceStatus = formData.hasAppraisal === 'N' ? 'ISSUE' : 'COMPLIANT'
      }
      // 自动生成交易编号
      formData.transNo = 'TXN-' + new Date().getFullYear() + '-' + String(Date.now()).slice(-6)
      // 设置审查类型（使用英文编码，避免达梦数据库字段溢出）
      formData.reviewType = formData.transMethod === 'EXCHANGE' ? 'EXCHANGE_REVIEW' : (formData.transMethod === 'AGREEMENT' ? 'AGREEMENT_REVIEW' : (formData.transMethod === 'AUCTION' ? 'AUCTION_REVIEW' : 'TRANSFER_REVIEW'))
      formData.reviewResult = formData.complianceStatus === 'COMPLIANT' ? 'PASS' : (formData.complianceStatus === 'VIOLATION' ? 'FAIL' : 'CONDITIONAL')
      formData.riskLevel = formData.complianceStatus === 'VIOLATION' ? 'HIGH' : (formData.complianceStatus === 'ISSUE' ? 'MEDIUM' : 'LOW')
      // 清理空值字段，避免数据库类型转换异常
      if (!formData.transDate) { delete formData.transDate }
      if (!formData.counterparty) { delete formData.counterparty }
      if (!formData.transAmount) { formData.transAmount = null }
      if (!formData.appraisalValue) { formData.appraisalValue = null }

      try {
        const res = await addTradeReview(formData)
        if (res.result === 200) {
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.loadData()
        } else {
          this.$message.error(res.msg || '保存失败')
        }
      } catch {
        this.$message.error('保存失败')
      }
    },
    showDetail(row) { this.currentRow = row; this.detailDrawer = true },
    rowClass({ row }) {
      if (row.complianceStatus === 'VIOLATION') return 'row-danger'
      if (row.complianceStatus === 'ISSUE') return 'row-warning'
      return ''
    },
    complianceLabel(v) { return { COMPLIANT: '合规', ISSUE: '问题', VIOLATION: '违规' }[v] || v || '—' },
    complianceTagType(v) { return { COMPLIANT: 'success', ISSUE: 'warning', VIOLATION: 'danger' }[v] || '' },
    transMethodLabel(v) { return { EXCHANGE: '进场交易', AGREEMENT: '协议转让', AUCTION: '拍卖', FREE_TRANSFER: '无偿划转' }[v] || v || '—' },
    getIssueSuggest(status) {
      return status === 'VIOLATION' ? '立即暂停交易，核查违规事项，上报监管部门，视情况启动审计程序。' : '请企业补充说明，整改后重新审核确认。'
    },
    async handleExport() {
      try {
        const res = await exportTradeReview()
        const blobData = res.data || res
        const blob = new Blob([blobData], { type: 'text/csv;charset=utf-8' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '产权交易合规审查.csv'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch {
        this.$message.error('导出失败')
      }
    }
  }
}
</script>

<style scoped>
.trade-review { padding: 16px; background: #F5F7FA; min-height: 100vh; }
.page-banner {
  border-radius: 8px; padding: 20px 32px; margin-bottom: 16px; color: #fff;
}
.banner-title { font-size: 22px; font-weight: 700; margin: 0 0 4px; }
.banner-sub { font-size: 13px; opacity: 0.85; margin: 0; }
.kpi-row { margin-bottom: 16px; }
.kpi-card { text-align: center; }
.kpi-value { font-size: 28px; font-weight: 700; }
.kpi-label { font-size: 12px; color: #888; margin-top: 4px; }
.search-card { margin-bottom: 12px; }
.toolbar { display: flex; align-items: center; gap: 8px; margin-bottom: 12px; }
.total-tip { margin-left: auto; font-size: 12px; color: #888; }
.card-header { display: flex; align-items: center; justify-content: space-between; font-size: 14px; font-weight: 600; }
.detail-wrap { padding: 16px; }
.issue-box { margin-top: 16px; padding: 12px; background: #FFF1F0; border-radius: 6px; border: 1px solid #FFCCC7; }
.issue-title { font-size: 14px; font-weight: 700; color: #F5222D; margin-bottom: 8px; }
.issue-content { font-size: 13px; color: #555; margin-bottom: 8px; }
.issue-suggest { font-size: 12px; color: #888; }
::v-deep .row-danger td { background: #FFF1F0 !important; }
::v-deep .row-warning td { background: #FFF7E6 !important; }
</style>
