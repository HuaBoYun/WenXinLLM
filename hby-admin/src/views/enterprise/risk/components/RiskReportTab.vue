<template>
  <div class="risk-report-tab">
    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="6"><el-card class="stat-card"><div class="stat-item"><div class="stat-icon" style="color: #409EFF"><i class="el-icon-document"></i></div><div class="stat-info"><div class="stat-value">{{ overview.totalReports || 0 }}</div><div class="stat-title">报告总数</div></div></div></el-card></el-col>
      <el-col :span="6"><el-card class="stat-card"><div class="stat-item"><div class="stat-icon" style="color: #67C23A"><i class="el-icon-date"></i></div><div class="stat-info"><div class="stat-value">{{ overview.monthlyReports || 0 }}</div><div class="stat-title">本月生成</div></div></div></el-card></el-col>
      <el-col :span="6"><el-card class="stat-card"><div class="stat-item"><div class="stat-icon" style="color: #E6A23C"><i class="el-icon-time"></i></div><div class="stat-info"><div class="stat-value">{{ overview.pendingReports || 0 }}</div><div class="stat-title">待审核</div></div></div></el-card></el-col>
      <el-col :span="6"><el-card class="stat-card"><div class="stat-item"><div class="stat-icon" style="color: #F56C6C"><i class="el-icon-success"></i></div><div class="stat-info"><div class="stat-value">{{ overview.publishedReports || 0 }}</div><div class="stat-title">已发布</div></div></div></el-card></el-col>
    </el-row>
    <!-- 报告生成工具 -->
    <el-card style="margin-bottom: 20px;">
      <div slot="header"><span>报告生成</span></div>
      <el-form :model="reportForm" :inline="true">
        <el-form-item label="报告类型">
          <el-select v-model="reportForm.reportType" placeholder="请选择报告类型">
            <el-option label="风险评估报告" value="assessment"></el-option><el-option label="风险监控报告" value="monitoring"></el-option>
            <el-option label="风险事件报告" value="incident"></el-option><el-option label="综合风险报告" value="comprehensive"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="报告周期">
          <el-select v-model="reportForm.period" placeholder="请选择报告周期">
            <el-option label="日报" value="daily"></el-option><el-option label="周报" value="weekly"></el-option>
            <el-option label="月报" value="monthly"></el-option><el-option label="季报" value="quarterly"></el-option>
            <el-option label="年报" value="yearly"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="generateReport" :loading="generating">{{ generating ? '生成中...' : '生成报告' }}</el-button></el-form-item>
      </el-form>
    </el-card>
    <!-- 报告列表 -->
    <el-card>
      <div slot="header">
        <span>风险报告列表</span>
        <el-button style="float:right;padding:3px 0" type="text" @click="loadData">刷新</el-button>
      </div>
      <el-form :model="queryForm" :inline="true" style="margin-bottom: 15px;">
        <el-form-item label="报告类型">
          <el-select v-model="queryForm.reportType" placeholder="请选择" clearable>
            <el-option label="风险评估报告" value="assessment"></el-option><el-option label="风险监控报告" value="monitoring"></el-option>
            <el-option label="风险事件报告" value="incident"></el-option><el-option label="综合风险报告" value="comprehensive"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable>
            <el-option label="草稿" value="draft"></el-option><el-option label="待审核" value="pending"></el-option>
            <el-option label="已审核" value="approved"></el-option><el-option label="已发布" value="published"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="queryData">查询</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
      <el-table :data="reportList" border v-loading="loading">
        <el-table-column prop="reportTitle" label="报告标题" width="300">
          <template slot-scope="scope"><el-link type="primary">{{ scope.row.reportTitle }}</el-link></template>
        </el-table-column>
        <el-table-column prop="reportType" label="报告类型" width="120">
          <template slot-scope="scope"><el-tag :type="getReportTypeTag(scope.row.reportType)">{{ getReportTypeText(scope.row.reportType) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="period" label="报告周期" width="100">
          <template slot-scope="scope">{{ getPeriodText(scope.row.period) }}</template>
        </el-table-column>
        <el-table-column prop="creator" label="创建人" width="100"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="150"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope"><el-tag :type="getStatusTag(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="downloadCount" label="下载次数" width="80" align="center"></el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="downloadReport(scope.row)">下载</el-button>
            <el-button type="text" size="small" @click="changeStatus(scope.row, 'approved')" v-if="scope.row.status === 'pending'">审核</el-button>
            <el-button type="text" size="small" @click="changeStatus(scope.row, 'published')" v-if="scope.row.status === 'approved'">发布</el-button>
            <el-button type="text" size="small" style="color: #F56C6C;" @click="deleteReport(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pagination.current" :page-sizes="[10, 20, 50, 100]" :page-size="pagination.size" layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" style="margin-top: 20px; text-align: right;"></el-pagination>
    </el-card>
  </div>
</template>

<script>
import { generateRiskReport, getRiskReportList, updateRiskReport, deleteRiskReport, analyzeRiskReport, downloadRiskReport, extractData } from '@/api/enterprise/risk'

export default {
  name: 'RiskReportTab',
  props: { enterpriseId: String, enterpriseName: String },
  data() {
    return {
      loading: false,
      generating: false,
      reportForm: { reportType: 'comprehensive', period: 'monthly' },
      queryForm: { reportType: '', status: '' },
      pagination: { current: 1, size: 10, total: 0 },
      overview: { totalReports: 0, monthlyReports: 0, pendingReports: 0, publishedReports: 0 },
      reportList: []
    }
  },
  watch: { enterpriseId: { handler(val) { if (val) this.loadData() }, immediate: true } },
  methods: {
    async loadData() {
      if (!this.enterpriseId) return
      this.loading = true
      try {
        const params = { enterpriseId: this.enterpriseId, pageNumber: this.pagination.current, pageSize: this.pagination.size, ...this.queryForm }
        const res = await getRiskReportList(params)
        const data = extractData(res)
        this.reportList = (data && (data.tlist || data.records)) || []
        this.pagination.total = (data && (data.totalRecord || data.total)) || 0
        this.loadOverview()
      } catch (e) { console.error('加载报告数据失败:', e) }
      finally { this.loading = false }
    },
    async loadOverview() {
      try {
        const res = await analyzeRiskReport({ enterpriseId: this.enterpriseId })
        this.overview = extractData(res) || {}
      } catch (e) { console.error('加载概览失败:', e) }
    },
    async generateReport() {
      if (!this.reportForm.reportType || !this.reportForm.period) {
        this.$message.error('请选择报告类型和周期'); return
      }
      this.generating = true
      try {
        await generateRiskReport({ ...this.reportForm, enterpriseId: this.enterpriseId, enterpriseName: this.enterpriseName, creator: '系统用户' })
        this.$message.success('报告生成成功'); this.loadData(); this.$emit('refresh')
      } catch (e) { this.$message.error('生成失败：' + e.message) }
      finally { this.generating = false }
    },
    async downloadReport(row) {
      try {
        await downloadRiskReport(row.riskReportId)
        this.$message.success('下载成功'); this.loadData()
      } catch (e) { this.$message.error('下载失败：' + e.message) }
    },
    async changeStatus(row, newStatus) {
      try {
        await updateRiskReport({ ...row, status: newStatus })
        this.$message.success('操作成功'); this.loadData(); this.$emit('refresh')
      } catch (e) { this.$message.error('操作失败：' + e.message) }
    },
    deleteReport(row) {
      this.$confirm('确认删除该报告？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(async () => {
        await deleteRiskReport(row.riskReportId); this.$message.success('删除成功'); this.loadData()
      })
    },
    queryData() { this.pagination.current = 1; this.loadData() },
    resetQuery() { this.queryForm = { reportType: '', status: '' }; this.queryData() },
    handleSizeChange(val) { this.pagination.size = val; this.loadData() },
    handleCurrentChange(val) { this.pagination.current = val; this.loadData() },
    getReportTypeTag(type) { return { assessment: 'primary', monitoring: 'success', incident: 'warning', comprehensive: 'danger' }[type] || 'info' },
    getReportTypeText(type) { return { assessment: '风险评估', monitoring: '风险监控', incident: '风险事件', comprehensive: '综合报告' }[type] || type },
    getPeriodText(period) { return { daily: '日报', weekly: '周报', monthly: '月报', quarterly: '季报', yearly: '年报' }[period] || period },
    getStatusTag(status) { return { draft: 'info', pending: 'warning', approved: 'primary', published: 'success' }[status] || 'info' },
    getStatusText(status) { return { draft: '草稿', pending: '待审核', approved: '已审核', published: '已发布' }[status] || status }
  }
}
</script>
<style scoped>
.risk-report-tab { padding: 20px; }
.stat-card { height: 100px; }
.stat-item { display: flex; align-items: center; height: 60px; }
.stat-icon { font-size: 28px; margin-right: 12px; }
.stat-info { flex: 1; }
.stat-value { font-size: 22px; font-weight: bold; color: #303133; line-height: 1.2; }
.stat-title { font-size: 13px; color: #909399; margin-top: 4px; }
</style>
