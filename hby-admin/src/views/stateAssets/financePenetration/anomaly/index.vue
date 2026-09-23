<template>
  <div class="app-container finance-page">
    <!-- 页头 -->
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-warning-outline"></i><span>财务异常检测</span></div>
      <div class="page-header-desc">智能检测企业财务数据异常指标</div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="12" class="stat-row">
      <el-col :span="6" v-for="s in stats" :key="s.label">
        <div class="stat-card" :style="{ borderColor: s.color }">
          <div class="stat-icon" :style="{ background: s.color }"><i :class="s.icon" /></div>
          <div class="stat-info">
            <div class="stat-value" :style="{ color: s.color }">{{ s.value }}</div>
            <div class="stat-label">{{ s.label }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 筛选栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="企业名称"><el-input v-model="queryForm.companyName" placeholder="请输入" clearable style="width:160px" /></el-form-item>
        <el-form-item label="异常等级">
          <el-select v-model="queryForm.anomalyLevel" placeholder="全部" clearable style="width:100px">
            <el-option label="高" value="HIGH" /><el-option label="中" value="MEDIUM" /><el-option label="低" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item label="异常类型">
          <el-select v-model="queryForm.anomalyType" placeholder="全部" clearable style="width:130px">
            <el-option v-for="t in anomalyTypeOptions" :key="t.value" :label="t.label" :value="t.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="检测时间">
          <el-date-picker v-model="queryForm.dateRange" type="daterange" range-separator="至" start-placeholder="开始" end-placeholder="结束" value-format="yyyy-MM-dd" style="width:220px" size="small" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 主体：左列表 + 右图表 -->
    <el-row :gutter="12" style="margin-top:10px">
      <!-- 左侧列表 -->
      <el-col :span="16">
        <el-card shadow="never">
          <div slot="header" class="card-header">
            <span>异常检测列表</span>
            <el-tag type="danger" size="small" style="margin-left:8px">{{ total }} 条</el-tag>
          </div>
          <el-table v-loading="loading" :data="list" border style="width:100%" size="small"
            :row-class-name="rowClassName" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="40" />
            <el-table-column label="企业名称" prop="companyName" min-width="130" show-overflow-tooltip />
            <el-table-column label="异常类型" prop="anomalyType" width="120" show-overflow-tooltip>
              <template slot-scope="scope">
                {{ anomalyTypeLabel(scope.row.anomalyType) }}
              </template>
            </el-table-column>
            <el-table-column label="异常指标" prop="anomalyIndicator" min-width="120" show-overflow-tooltip />
            <el-table-column label="异常值" prop="anomalyValue" width="90" align="right" />
            <el-table-column label="正常范围" prop="normalRange" width="110" align="center" />
            <el-table-column label="偏差率" prop="deviationRate" width="80" align="center">
              <template slot-scope="scope">
                <span :style="{ color: scope.row.anomalyLevel === 'HIGH' ? '#f5222d' : '#fa8c16' }">{{ scope.row.deviationRate }}</span>
              </template>
            </el-table-column>
            <el-table-column label="等级" prop="anomalyLevel" width="60" align="center">
              <template slot-scope="scope">
                <el-tag :type="levelTagType(scope.row.anomalyLevel)" size="mini">{{ levelLabel(scope.row.anomalyLevel) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="检测时间" prop="detectTime" width="100" align="center" />
            <el-table-column label="操作" width="130" align="center" fixed="right">
              <template slot-scope="scope">
                <el-button size="mini" type="text" @click="handleDetail(scope.row)">分析详情</el-button>
                <el-divider direction="vertical" />
                <el-button size="mini" type="text" style="color:#fa8c16" @click="handleDispatch(scope.row)">派单整改</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination background style="margin-top:12px;text-align:right" :current-page="queryForm.pageNumber"
            :page-sizes="[10,20,50]" :page-size="queryForm.pageSize"
            layout="total, sizes, prev, pager, next, jumper" :total="total"
            @size-change="val => { queryForm.pageSize = val; fetchData() }"
            @current-change="val => { queryForm.pageNumber = val; fetchData() }" />
        </el-card>
      </el-col>

      <!-- 右侧趋势图 -->
      <el-col :span="8">
        <el-card shadow="never" style="height:100%">
          <div slot="header" class="card-header"><span>近6个月异常趋势</span></div>
          <div ref="trendChart" style="height:420px" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 分析详情 Drawer -->
    <el-drawer title="异常分析详情" :visible.sync="drawerVisible" size="600px" direction="rtl">
      <div v-loading="detailLoading" class="drawer-body">
        <template v-if="detailData">
          <el-descriptions :column="2" border size="small" class="desc-block">
            <el-descriptions-item label="企业名称">{{ detailData.companyName }}</el-descriptions-item>
            <el-descriptions-item label="异常类型">{{ anomalyTypeLabel(detailData.anomalyType) }}</el-descriptions-item>
            <el-descriptions-item label="异常指标">{{ detailData.indicatorName }}</el-descriptions-item>
            <el-descriptions-item label="异常等级">
              <el-tag :type="levelTagType(detailData.anomalyLevel)" size="small">{{ levelLabel(detailData.anomalyLevel) }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="实际值">{{ detailData.actualValue }}</el-descriptions-item>
            <el-descriptions-item label="预期值">{{ detailData.expectedValue }}</el-descriptions-item>
            <el-descriptions-item label="偏差率">
              <span style="color:#f5222d;font-weight:600">{{ detailData.deviationRate }}%</span>
            </el-descriptions-item>
            <el-descriptions-item label="检测时间">{{ detailData.createTime }}</el-descriptions-item>
            <el-descriptions-item label="检测方法">{{ detailData.detectionMethod }}</el-descriptions-item>
            <el-descriptions-item label="报告期间">{{ detailData.period }}</el-descriptions-item>
            <el-descriptions-item label="原因分析" :span="2">{{ detailData.reason }}</el-descriptions-item>
          </el-descriptions>
          <!-- 关联报表 -->
          <template v-if="detailData.relatedStatement">
            <div class="drawer-section-title">关联财务数据</div>
            <el-descriptions :column="2" border size="small" style="margin-top:8px">
              <el-descriptions-item label="总资产">{{ detailData.relatedStatement.totalAssets }}</el-descriptions-item>
              <el-descriptions-item label="营业收入">{{ detailData.relatedStatement.revenue }}</el-descriptions-item>
              <el-descriptions-item label="净利润">{{ detailData.relatedStatement.netProfit }}</el-descriptions-item>
              <el-descriptions-item label="经营现金流">{{ detailData.relatedStatement.operatingCashflow }}</el-descriptions-item>
            </el-descriptions>
          </template>
          <div class="drawer-section-title">历史趋势</div>
          <div ref="detailChart" style="height:200px;margin-top:8px" />
          <!-- 整改记录 -->
          <template v-if="detailData.rectifications && detailData.rectifications.length">
            <div class="drawer-section-title">整改记录</div>
            <el-table :data="detailData.rectifications" size="mini" border style="margin-top:8px">
              <el-table-column label="责任人" prop="assignee" width="80" />
              <el-table-column label="期限" prop="deadline" width="100" />
              <el-table-column label="状态" prop="status" width="80">
                <template slot-scope="s">
                  <el-tag :type="s.row.status === 'COMPLETED' ? 'success' : s.row.status === 'OVERDUE' ? 'danger' : 'warning'" size="mini">
                    {{ {PENDING:'待整改',PROCESSING:'整改中',COMPLETED:'已完成',OVERDUE:'已逾期'}[s.row.status] || s.row.status }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="要求" prop="requirement" show-overflow-tooltip />
            </el-table>
          </template>
        </template>
        <el-empty v-else-if="!detailLoading" description="暂无详情数据" />
      </div>
    </el-drawer>

    <!-- 派单整改 Dialog -->
    <el-dialog title="派单整改" :visible.sync="dispatchVisible" width="480px" @close="resetDispatch">
      <el-form :model="dispatchForm" ref="dispatchForm" :rules="dispatchRules" label-width="100px" size="small">
        <el-form-item label="整改责任人" prop="assignee">
          <el-input v-model="dispatchForm.assignee" placeholder="请输入责任人姓名" />
        </el-form-item>
        <el-form-item label="整改期限" prop="deadline">
          <el-date-picker v-model="dispatchForm.deadline" type="date" placeholder="选择日期" value-format="yyyy-MM-dd" style="width:100%" />
        </el-form-item>
        <el-form-item label="整改要求" prop="requirement">
          <el-input v-model="dispatchForm.requirement" type="textarea" :rows="4" placeholder="请描述整改要求" />
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button size="small" @click="dispatchVisible = false">取消</el-button>
        <el-button type="primary" size="small" :loading="dispatchLoading" @click="submitDispatch">提交派单</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import { getAnomalyList, getAnomalyStats, getAnomalyTrend, getAnomalyDetail, submitAnomalyDispatch } from '@/api/stateAssets/financialPenetration'

// 异常类型：DB英文code → 中文标签
const ANOMALY_TYPE_MAP = {
  LIQUIDITY_RISK: '流动性风险',
  SOLVENCY_RISK: '偿债能力风险',
  PROFITABILITY_RISK: '盈利能力风险',
  OPERATIONAL_RISK: '运营效率风险',
  MARKET_RISK: '市场风险',
  CREDIT_RISK: '信用风险',
}
// 筛选下拉选项（value为DB code）
const ANOMALY_TYPE_OPTIONS = Object.entries(ANOMALY_TYPE_MAP).map(([value, label]) => ({ value, label }))

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
  name: 'FinanceAnomaly',
  data() {
    return {
      loading: false,
      list: [],
      total: 0,
      selectedRows: [],
      queryForm: { pageNumber: 1, pageSize: 10, companyName: '', anomalyLevel: '', anomalyType: '', dateRange: [] },
      anomalyTypeOptions: ANOMALY_TYPE_OPTIONS,
      stats: [
        { label: '异常总数', value: 0, icon: 'el-icon-warning', color: '#722ed1' },
        { label: '高风险数', value: 0, icon: 'el-icon-s-opportunity', color: '#f5222d' },
        { label: '待处置数', value: 0, icon: 'el-icon-time', color: '#fa8c16' },
        { label: '本月新增', value: 0, icon: 'el-icon-plus', color: '#1890ff' },
      ],
      // drawer
      drawerVisible: false,
      currentRow: null,
      detailData: null,
      detailLoading: false,
      detailChartInst: null,
      // dispatch dialog
      dispatchVisible: false,
      dispatchLoading: false,
      dispatchForm: { assignee: '', deadline: '', requirement: '' },
      dispatchRules: {
        assignee: [{ required: true, message: '请输入责任人', trigger: 'blur' }],
        deadline: [{ required: true, message: '请选择整改期限', trigger: 'change' }],
        requirement: [{ required: true, message: '请填写整改要求', trigger: 'blur' }],
      },
      trendChartInst: null,
    }
  },
  created() {
    this.fetchData()
    this.fetchStats()
  },
  beforeDestroy() {
    this.trendChartInst && this.trendChartInst.dispose()
    this.detailChartInst && this.detailChartInst.dispose()
  },
  methods: {
    /** 异常类型code转中文 */
    anomalyTypeLabel(code) {
      return ANOMALY_TYPE_MAP[code] || code || '-'
    },
    fetchData() {
      this.loading = true
      const params = {
        pageNumber: this.queryForm.pageNumber,
        pageSize: this.queryForm.pageSize,
        companyName: this.queryForm.companyName || undefined,
        anomalyLevel: this.queryForm.anomalyLevel || undefined,
        anomalyType: this.queryForm.anomalyType || undefined,
        startDate: this.queryForm.dateRange && this.queryForm.dateRange[0] ? this.queryForm.dateRange[0] : undefined,
        endDate: this.queryForm.dateRange && this.queryForm.dateRange[1] ? this.queryForm.dateRange[1] : undefined,
      }
      Object.keys(params).forEach(k => params[k] === undefined && delete params[k])
      getAnomalyList(params).then(res => {
        if (res && res.data) {
          const pageData = res.data
          this.list = pageData.tlist || []
          this.total = pageData.totalRecord || 0
        }
        this.loading = false
      }).catch(err => {
        console.error('获取异常检测数据失败', err)
        this.loading = false
      })
    },
    /** 从后端获取统计数据 */
    fetchStats() {
      getAnomalyStats().then(res => {
        if (res && res.data) {
          this.stats[0].value = res.data.total || 0
          this.stats[1].value = res.data.highRisk || 0
          this.stats[2].value = res.data.pending || 0
          this.stats[3].value = res.data.monthNew || 0
        }
      }).catch(() => {})
      // 获取趋势图数据
      this.fetchTrendData()
    },
    /** 从后端获取趋势图数据 */
    fetchTrendData() {
      getAnomalyTrend().then(res => {
        if (res && res.data) {
          this.$nextTick(() => this.initTrendChart(res.data))
        }
      }).catch(() => {})
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.queryForm.pageNumber = 1; this.fetchData() },
    handleSelectionChange(rows) { this.selectedRows = rows },
    rowClassName({ row }) { return row.anomalyLevel === 'HIGH' ? 'high-risk-row' : '' },
    levelTagType(level) { return { HIGH: 'danger', MEDIUM: 'warning', LOW: 'info' }[level] || '' },
    levelLabel(level) { return { HIGH: '高', MEDIUM: '中', LOW: '低' }[level] || level },

    /** 分析详情 - 调用后端接口获取完整数据 */
    handleDetail(row) {
      this.currentRow = row
      this.detailData = null
      this.drawerVisible = true
      this.detailLoading = true
      const id = row.anomalyId || row.id
      getAnomalyDetail(id).then(res => {
        if (res && res.data) {
          this.detailData = res.data
          this.$nextTick(() => this.initDetailChart(res.data.trendData))
        }
        this.detailLoading = false
      }).catch(() => {
        this.detailLoading = false
        this.$message.error('获取详情失败')
      })
    },
    initDetailChart(trendData) {
      if (!this.$refs.detailChart) return
      this.detailChartInst && this.detailChartInst.dispose()
      const chart = echarts.init(this.$refs.detailChart)
      this.detailChartInst = chart
      if (!trendData || !trendData.length) {
        chart.setOption({ title: { text: '暂无历史趋势数据', left: 'center', top: 'center', textStyle: { color: '#999', fontSize: 14 } } })
        return
      }
      const periods = trendData.map(d => d.period)
      const values = trendData.map(d => d.value)
      const expected = trendData.map(d => d.expected)
      chart.setOption({
        grid: { top: 30, right: 20, bottom: 30, left: 50 },
        tooltip: { trigger: 'axis' },
        legend: { data: ['实际值', '预期值'], bottom: 0 },
        xAxis: { type: 'category', data: periods, axisLine: { lineStyle: { color: '#d9d9d9' } } },
        yAxis: { type: 'value', splitLine: { lineStyle: { type: 'dashed' } } },
        series: [
          { name: '实际值', data: values, type: 'line', smooth: true, lineStyle: { color: '#722ed1' }, itemStyle: { color: '#722ed1' } },
          { name: '预期值', data: expected, type: 'line', smooth: true, lineStyle: { color: '#52c41a', type: 'dashed' }, itemStyle: { color: '#52c41a' } },
        ],
      })
    },

    /** 派单整改 - 调用后端接口 */
    handleDispatch(row) { this.currentRow = row; this.dispatchVisible = true },
    resetDispatch() { this.$refs.dispatchForm && this.$refs.dispatchForm.resetFields() },
    submitDispatch() {
      this.$refs.dispatchForm.validate(valid => {
        if (!valid) return
        this.dispatchLoading = true
        const data = {
          anomalyId: this.currentRow.anomalyId || this.currentRow.id,
          assignee: this.dispatchForm.assignee,
          deadline: this.dispatchForm.deadline,
          requirement: this.dispatchForm.requirement,
        }
        submitAnomalyDispatch(data).then(res => {
          this.dispatchLoading = false
          if (res && res.data) {
            this.dispatchVisible = false
            this.$message.success(`已向 ${this.dispatchForm.assignee} 派单整改，期限：${this.dispatchForm.deadline}`)
            this.fetchData()
            this.fetchStats()
          } else {
            this.$message.error(res.msg || '派单失败')
          }
        }).catch(err => {
          this.dispatchLoading = false
          this.$message.error('派单请求失败')
        })
      })
    },

    /** 趋势图 - 使用后端真实数据 */
    initTrendChart(data) {
      if (!this.$refs.trendChart) return
      this.trendChartInst && this.trendChartInst.dispose()
      const chart = echarts.init(this.$refs.trendChart)
      this.trendChartInst = chart
      if (!data || !data.months || !data.months.length) {
        chart.setOption({ title: { text: '暂无趋势数据', left: 'center', top: 'center', textStyle: { color: '#999', fontSize: 14 } } })
        return
      }
      chart.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['高风险', '中风险', '低风险'], bottom: 0 },
        grid: { top: 30, right: 20, bottom: 50, left: 45 },
        xAxis: { type: 'category', data: data.months, axisLine: { lineStyle: { color: '#d9d9d9' } } },
        yAxis: { type: 'value', splitLine: { lineStyle: { type: 'dashed' } } },
        series: [
          { name: '高风险', type: 'line', smooth: true, data: data.high, lineStyle: { color: '#f5222d' }, itemStyle: { color: '#f5222d' } },
          { name: '中风险', type: 'line', smooth: true, data: data.medium, lineStyle: { color: '#fa8c16' }, itemStyle: { color: '#fa8c16' } },
          { name: '低风险', type: 'line', smooth: true, data: data.low, lineStyle: { color: '#52c41a' }, itemStyle: { color: '#52c41a' } },
        ],
      })
    },
  },
}
</script>

<style lang="scss" scoped>
.finance-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
.search-card { border-left: 3px solid #722ed1; }
.stat-row { margin-bottom: 12px; }
.stat-card {
  display: flex; align-items: center; background: #fff; border-radius: 6px; padding: 14px 16px;
  border-left: 4px solid #722ed1; box-shadow: 0 1px 4px rgba(0,0,0,.08);
  .stat-icon { width: 44px; height: 44px; border-radius: 50%; display: flex; align-items: center; justify-content: center; margin-right: 14px; i { font-size: 22px; color: #fff; } }
  .stat-value { font-size: 26px; font-weight: 700; line-height: 1.2; }
  .stat-label { font-size: 12px; color: #8c8c8c; margin-top: 2px; }
}
.card-header { display: flex; align-items: center; font-weight: 600; }
.drawer-body { padding: 16px 20px; }
.desc-block { margin-bottom: 16px; }
.drawer-section-title { font-size: 13px; font-weight: 600; color: #722ed1; border-left: 3px solid #722ed1; padding-left: 8px; margin-top: 16px; }
::v-deep .el-table th { background: #f9f0ff; }
::v-deep .el-card { border-radius: 6px; }
::v-deep .high-risk-row td { background: #fff1f0 !important; }
::v-deep .el-drawer__body { overflow-y: auto; }
</style>
