<template>
  <div class="risk-monitoring-tab">
    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="6"><el-card class="stat-card"><div class="stat-item"><div class="stat-icon" style="color: #409EFF"><i class="el-icon-data-line"></i></div><div class="stat-info"><div class="stat-value">{{ overview.totalIndicators || 0 }}</div><div class="stat-title">监控指标数</div></div></div></el-card></el-col>
      <el-col :span="6"><el-card class="stat-card"><div class="stat-item"><div class="stat-icon" style="color: #E6A23C"><i class="el-icon-warning-outline"></i></div><div class="stat-info"><div class="stat-value">{{ overview.warningCount || 0 }}</div><div class="stat-title">预警数量</div></div></div></el-card></el-col>
      <el-col :span="6"><el-card class="stat-card"><div class="stat-item"><div class="stat-icon" style="color: #F56C6C"><i class="el-icon-error"></i></div><div class="stat-info"><div class="stat-value">{{ overview.highRiskCount || 0 }}</div><div class="stat-title">高风险项</div></div></div></el-card></el-col>
      <el-col :span="6"><el-card class="stat-card"><div class="stat-item"><div class="stat-icon" style="color: #67C23A"><i class="el-icon-pie-chart"></i></div><div class="stat-info"><div class="stat-value">{{ overview.coverageRate || 0 }}%</div><div class="stat-title">监控覆盖率</div></div></div></el-card></el-col>
    </el-row>
    <!-- 趋势图+分布图 -->
    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="16">
        <el-card>
          <div slot="header"><span>风险趋势监控</span><el-button style="float:right;padding:3px 0" type="text" @click="loadTrendChart">刷新</el-button></div>
          <div ref="trendChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div slot="header"><span>风险等级分布</span></div>
          <div ref="distributionChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 监控指标列表 -->
    <el-card style="margin-bottom: 20px;">
      <div slot="header">
        <span>监控指标</span>
        <el-button type="primary" size="small" style="float: right;" @click="showAddDialog">新增指标</el-button>
      </div>
      <el-form :model="queryForm" :inline="true" style="margin-bottom: 15px;">
        <el-form-item label="监控类型">
          <el-select v-model="queryForm.monitoringType" placeholder="请选择" clearable>
            <el-option label="财务监控" value="financial"></el-option><el-option label="经营监控" value="operational"></el-option>
            <el-option label="市场监控" value="market"></el-option><el-option label="技术监控" value="technical"></el-option>
            <el-option label="合规监控" value="compliance"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="queryForm.currentRiskLevel" placeholder="请选择" clearable>
            <el-option label="高风险" value="high"></el-option><el-option label="中风险" value="medium"></el-option><el-option label="低风险" value="low"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="监控状态">
          <el-select v-model="queryForm.monitoringStatus" placeholder="请选择" clearable>
            <el-option label="正常" value="normal"></el-option><el-option label="预警" value="warning"></el-option><el-option label="危险" value="danger"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="queryData">查询</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
      <el-table :data="monitoringList" border v-loading="loading">
        <el-table-column prop="indicatorName" label="指标名称" width="200"></el-table-column>
        <el-table-column prop="monitoringType" label="监控类型" width="120">
          <template slot-scope="scope"><el-tag :type="getCategoryTag(scope.row.monitoringType)">{{ getCategoryText(scope.row.monitoringType) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="currentValue" label="当前值" width="100" align="right">
          <template slot-scope="scope"><span :class="getValueClass(scope.row)">{{ scope.row.currentValue || '-' }}</span></template>
        </el-table-column>
        <el-table-column prop="warningThreshold" label="预警阈值" width="100" align="right">
          <template slot-scope="scope">{{ scope.row.warningThreshold || '-' }}</template>
        </el-table-column>
        <el-table-column prop="dangerThreshold" label="危险阈值" width="100" align="right">
          <template slot-scope="scope">{{ scope.row.dangerThreshold || '-' }}</template>
        </el-table-column>
        <el-table-column prop="currentRiskLevel" label="风险等级" width="100">
          <template slot-scope="scope"><el-tag :type="getRiskLevelType(scope.row.currentRiskLevel)">{{ getRiskLevelText(scope.row.currentRiskLevel) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="riskTrend" label="趋势" width="80" align="center">
          <template slot-scope="scope"><i :class="getTrendIcon(scope.row.riskTrend)" :style="getTrendColor(scope.row.riskTrend)"></i></template>
        </el-table-column>
        <el-table-column prop="monitoringStatus" label="状态" width="80">
          <template slot-scope="scope"><el-tag :type="getStatusTag(scope.row.monitoringStatus)">{{ getStatusText(scope.row.monitoringStatus) }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="editIndicator(scope.row)">编辑</el-button>
            <el-button type="text" size="small" style="color: #F56C6C;" @click="deleteIndicator(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pagination.current" :page-sizes="[10, 20, 50, 100]" :page-size="pagination.size" layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" style="margin-top: 20px; text-align: right;"></el-pagination>
    </el-card>
    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form :model="formData" label-width="100px">
        <el-form-item label="指标名称"><el-input v-model="formData.indicatorName"></el-input></el-form-item>
        <el-form-item label="监控类型">
          <el-select v-model="formData.monitoringType" style="width:100%">
            <el-option label="财务监控" value="financial"></el-option><el-option label="经营监控" value="operational"></el-option>
            <el-option label="市场监控" value="market"></el-option><el-option label="技术监控" value="technical"></el-option>
            <el-option label="合规监控" value="compliance"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="监控频率">
          <el-select v-model="formData.monitoringFrequency" style="width:100%">
            <el-option label="每日" value="daily"></el-option><el-option label="每周" value="weekly"></el-option>
            <el-option label="每月" value="monthly"></el-option><el-option label="每季度" value="quarterly"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="当前值"><el-input-number v-model="formData.currentValue" :precision="2"></el-input-number></el-form-item>
        <el-form-item label="预警阈值"><el-input-number v-model="formData.warningThreshold" :precision="2"></el-input-number></el-form-item>
        <el-form-item label="危险阈值"><el-input-number v-model="formData.dangerThreshold" :precision="2"></el-input-number></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="submitForm">确定</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import { getRiskMonitoringList, addRiskMonitoring, updateRiskMonitoring, deleteRiskMonitoring, analyzeRiskMonitoring, getRiskWarningList, extractData } from '@/api/enterprise/risk'

export default {
  name: 'RiskMonitoringTab',
  props: { enterpriseId: String, enterpriseName: String },
  data() {
    return {
      loading: false,
      queryForm: { monitoringType: '', currentRiskLevel: '', monitoringStatus: '' },
      pagination: { current: 1, size: 20, total: 0 },
      overview: { totalIndicators: 0, warningCount: 0, highRiskCount: 0, coverageRate: 0 },
      monitoringList: [],
      dialogVisible: false,
      dialogTitle: '新增监控指标',
      isEdit: false,
      formData: { indicatorName: '', monitoringType: '', monitoringFrequency: 'monthly', currentValue: 0, warningThreshold: 0, dangerThreshold: 0 },
      trendChart: null,
      distributionChart: null
    }
  },
  watch: { enterpriseId: { handler(val) { if (val) this.loadData() }, immediate: true } },
  methods: {
    async loadData() {
      if (!this.enterpriseId) return
      this.loading = true
      try {
        const params = { enterpriseId: this.enterpriseId, pageNumber: this.pagination.current, pageSize: this.pagination.size, ...this.queryForm }
        const res = await getRiskMonitoringList(params)
        const data = extractData(res)
        this.monitoringList = (data && (data.tlist || data.records)) || []
        this.pagination.total = (data && (data.totalRecord || data.total)) || 0
        this.loadOverview()
        this.$nextTick(() => { this.initCharts() })
      } catch (e) { console.error('加载监控数据失败:', e) }
      finally { this.loading = false }
    },
    async loadOverview() {
      try {
        const res = await analyzeRiskMonitoring({ enterpriseId: this.enterpriseId })
        this.overview = extractData(res) || {}
      } catch (e) { console.error('加载概览失败:', e) }
    },
    async initCharts() {
      try {
        const echarts = await import('echarts')
        if (!this.trendChart && this.$refs.trendChart) {
          this.trendChart = echarts.init(this.$refs.trendChart)
        }
        if (!this.distributionChart && this.$refs.distributionChart) {
          this.distributionChart = echarts.init(this.$refs.distributionChart)
        }
        this.loadTrendChart()
        this.loadDistributionChart()
      } catch (e) { console.error('初始化图表失败:', e) }
    },
    loadTrendChart() {
      if (!this.trendChart) return
      // 用监控列表数据生成趋势图
      const list = this.monitoringList
      const names = list.map(i => i.indicatorName || '').slice(0, 10)
      const values = list.map(i => i.currentValue || 0).slice(0, 10)
      const thresholds = list.map(i => i.warningThreshold || 0).slice(0, 10)
      this.trendChart.setOption({
        title: { text: '风险指标趋势', left: 'center', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'axis' },
        legend: { data: ['当前值', '预警阈值'], bottom: 0 },
        xAxis: { type: 'category', data: names, axisLabel: { rotate: 30, fontSize: 10 } },
        yAxis: { type: 'value' },
        series: [
          { name: '当前值', type: 'bar', data: values, itemStyle: { color: '#409EFF' } },
          { name: '预警阈值', type: 'line', data: thresholds, lineStyle: { color: '#E6A23C', type: 'dashed' }, itemStyle: { color: '#E6A23C' } }
        ]
      })
    },
    loadDistributionChart() {
      if (!this.distributionChart) return
      const list = this.monitoringList
      const levelCount = { high: 0, medium: 0, low: 0 }
      list.forEach(i => { if (i.currentRiskLevel) levelCount[i.currentRiskLevel] = (levelCount[i.currentRiskLevel] || 0) + 1 })
      const colorMap = { '高风险': '#F56C6C', '中风险': '#E6A23C', '低风险': '#67C23A' }
      this.distributionChart.setOption({
        title: { text: '风险等级分布', left: 'center', textStyle: { fontSize: 14 } },
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        series: [{
          type: 'pie', radius: ['35%', '60%'], center: ['50%', '55%'],
          data: [
            { name: '高风险', value: levelCount.high || 0 },
            { name: '中风险', value: levelCount.medium || 0 },
            { name: '低风险', value: levelCount.low || 0 }
          ],
          itemStyle: { color: function(p) { return colorMap[p.name] || '#909399' } },
          label: { formatter: '{b}: {c}' }
        }]
      })
    },
    refreshData() { this.loadData(); this.$message.success('数据刷新成功') },
    queryData() { this.pagination.current = 1; this.loadData() },
    resetQuery() { this.queryForm = { monitoringType: '', currentRiskLevel: '', monitoringStatus: '' }; this.queryData() },
    handleSizeChange(val) { this.pagination.size = val; this.loadData() },
    handleCurrentChange(val) { this.pagination.current = val; this.loadData() },
    showAddDialog() {
      this.isEdit = false; this.dialogTitle = '新增监控指标'
      this.formData = { indicatorName: '', monitoringType: '', monitoringFrequency: 'monthly', currentValue: 0, warningThreshold: 0, dangerThreshold: 0 }
      this.dialogVisible = true
    },
    editIndicator(row) {
      this.isEdit = true; this.dialogTitle = '编辑监控指标'
      this.formData = { ...row }; this.dialogVisible = true
    },
    async submitForm() {
      try {
        const data = { ...this.formData, enterpriseId: this.enterpriseId, enterpriseName: this.enterpriseName, monitoringStatus: this.isEdit ? this.formData.monitoringStatus : 'normal' }
        if (this.isEdit) { await updateRiskMonitoring(data) } else { await addRiskMonitoring(data) }
        this.$message.success(this.isEdit ? '更新成功' : '新增成功')
        this.dialogVisible = false; this.loadData(); this.$emit('refresh')
      } catch (e) { this.$message.error('操作失败：' + e.message) }
    },
    deleteIndicator(row) {
      this.$confirm('确认删除该监控指标？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(async () => {
        await deleteRiskMonitoring(row.riskMonitoringId); this.$message.success('删除成功'); this.loadData(); this.$emit('refresh')
      })
    },
    getCategoryTag(type) { return { financial: 'danger', operational: 'warning', market: 'primary', technical: 'info', compliance: 'success' }[type] || 'info' },
    getCategoryText(type) { return { financial: '财务', operational: '经营', market: '市场', technical: '技术', compliance: '合规' }[type] || type },
    getRiskLevelType(level) { return { high: 'danger', medium: 'warning', low: 'success' }[level] || 'info' },
    getRiskLevelText(level) { return { high: '高风险', medium: '中风险', low: '低风险' }[level] || level },
    getStatusTag(status) { return { normal: 'success', warning: 'warning', danger: 'danger' }[status] || 'info' },
    getStatusText(status) { return { normal: '正常', warning: '预警', danger: '危险' }[status] || status },
    getTrendIcon(trend) { return { up: 'el-icon-top', down: 'el-icon-bottom', stable: 'el-icon-minus' }[trend] || 'el-icon-minus' },
    getTrendColor(trend) { return { up: 'color: #F56C6C', down: 'color: #67C23A', stable: 'color: #909399' }[trend] || 'color: #909399' },
    getValueClass(row) {
      if (row.dangerThreshold && row.currentValue >= row.dangerThreshold) return 'value-danger'
      if (row.warningThreshold && row.currentValue >= row.warningThreshold) return 'value-warning'
      return 'value-normal'
    }
  },
  beforeDestroy() {
    if (this.trendChart) this.trendChart.dispose()
    if (this.distributionChart) this.distributionChart.dispose()
  }
}
</script>
<style scoped>
.value-danger { color: #F56C6C; font-weight: bold; }
.value-warning { color: #E6A23C; font-weight: bold; }
.value-normal { color: #67C23A; }
.stat-card { height: 100px; }
.stat-item { display: flex; align-items: center; height: 60px; }
.stat-icon { font-size: 28px; margin-right: 12px; }
.stat-info { flex: 1; }
.stat-value { font-size: 22px; font-weight: bold; color: #303133; line-height: 1.2; }
.stat-title { font-size: 13px; color: #909399; margin-top: 4px; }
</style>
