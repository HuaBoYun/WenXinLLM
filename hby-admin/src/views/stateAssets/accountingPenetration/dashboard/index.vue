<template>
  <div class="app-container accounting-dashboard" :style="themeVars">
    <div class="dash-header">
      <div class="dash-header-left">
        <i class="el-icon-odometer"></i>
        <span>会计穿透监管驾驶舱</span>
        <span class="dash-date">数据截至：{{ currentDate }}</span>
      </div>
      <div class="dash-header-right">
        <el-select v-model="selectedYear" size="small" style="width:100px;margin-right:8px" @change="loadData">
          <el-option v-for="y in yearOptions" :key="y" :label="y + '年'" :value="y" />
        </el-select>
        <el-button size="small" icon="el-icon-refresh" @click="loadData">刷新</el-button>
      </div>
    </div>

    <!-- 核心预警指标条 -->
    <el-row :gutter="16" class="kpi-row">
      <el-col :span="4" v-for="(kpi, idx) in kpiCards" :key="idx">
        <div class="kpi-card" :class="kpi.alertClass">
          <div class="kpi-icon"><i :class="kpi.icon"></i></div>
          <div class="kpi-body">
            <div class="kpi-value">{{ kpi.value }}</div>
            <div class="kpi-label">{{ kpi.label }}</div>
          </div>
          <div v-if="kpi.badge" class="kpi-alert-dot"></div>
        </div>
      </el-col>
    </el-row>

    <!-- 中部：热力图 + 报表质量雷达图 -->
    <el-row :gutter="16" class="mid-row">
      <el-col :span="14">
        <el-card shadow="never" class="section-card">
          <div slot="header" class="card-header">
            <span><i class="el-icon-s-grid"></i> 财务造假风险热力图</span>
            <span class="header-tip">颜色越深 = 风险越高，点击格子查看详情</span>
          </div>
          <div class="heatmap-wrap">
            <div class="heatmap-container">
              <div class="heatmap-col-headers">
                <div class="heatmap-row-label-placeholder"></div>
                <div class="heatmap-col-label" v-for="col in fraudCols" :key="col">{{ col }}</div>
              </div>
              <div class="heatmap-body">
                <div class="heatmap-row" v-for="(row, rIdx) in heatmapData" :key="rIdx">
                  <div class="heatmap-row-label" :title="row.company">{{ row.companyShort }}</div>
                  <div
                    v-for="(val, cIdx) in row.scores"
                    :key="cIdx"
                    class="heatmap-cell"
                    :class="getHeatClass(val)"
                    :title="row.company + ' - ' + fraudCols[cIdx] + ': ' + val + '分'"
                    @click="viewFraudDetail(row, cIdx)"
                  >
                    <span>{{ val }}</span>
                  </div>
                </div>
              </div>
            </div>
            <div class="heatmap-legend">
              <span>风险等级：</span>
              <span class="legend-item heat-low">低 0-39</span>
              <span class="legend-item heat-medium">中 40-69</span>
              <span class="legend-item heat-high">高 70+</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never" class="section-card">
          <div slot="header" class="card-header">
            <span><i class="el-icon-data-analysis"></i> 报表质量五维评分</span>
            <el-select v-model="selectedCompany" size="mini" style="width:140px" @change="updateRadar">
              <el-option v-for="c in companyOptions" :key="c.value" :label="c.label" :value="c.value" />
            </el-select>
          </div>
          <div class="radar-wrap">
            <div class="radar-scores">
              <div class="rs-item" v-for="(dim, idx) in radarDims" :key="idx">
                <div class="rs-label">{{ dim.label }}</div>
                <el-progress
                  :percentage="dim.score"
                  :color="dim.score < 60 ? '#FF4D4F' : dim.score < 80 ? '#FA8C16' : '#52C41A'"
                  :stroke-width="10"
                />
                <div class="rs-value" :class="dim.score < 60 ? 'score-red' : ''">{{ dim.score }}</div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 财务造假风险详情对话框 -->
    <el-dialog
      :title="fraudDialogTitle"
      :visible.sync="fraudDialogVisible"
      width="720px"
      top="6vh"
      custom-class="fraud-detail-dialog"
      :close-on-click-modal="true"
    >
      <div v-if="fraudDetailLoading" style="text-align:center;padding:40px 0">
        <i class="el-icon-loading" style="font-size:24px;color:#909399"></i>
        <div style="margin-top:8px;color:#909399;font-size:13px">加载中...</div>
      </div>
      <div v-else-if="fraudDetail" class="fraud-detail-body">
        <!-- 综合风险概览 -->
        <div class="fd-overview">
          <div class="fd-score-ring" :class="'ring-' + (fraudDetail.alertLevel || 'GREEN').toLowerCase()">
            <div class="fd-score-num">{{ fraudDetail.totalScore || 0 }}</div>
            <div class="fd-score-label">综合风险指数</div>
          </div>
          <div class="fd-meta">
            <div class="fd-company">{{ fraudDetail.companyName }}</div>
            <div class="fd-tags">
              <el-tag size="small" :type="fraudDetail.alertLevel === 'RED' ? 'danger' : fraudDetail.alertLevel === 'ORANGE' ? 'warning' : 'success'">
                {{ fraudDetail.alertLevel === 'RED' ? '高风险' : fraudDetail.alertLevel === 'ORANGE' ? '中风险' : '低风险' }}
              </el-tag>
              <el-tag size="small" type="info">待处置线索：{{ fraudDetail.clueCount || 0 }}条</el-tag>
              <el-tag size="small" :type="fraudDetail.riskLevel === 'HIGH' ? 'danger' : fraudDetail.riskLevel === 'MED' ? 'warning' : 'success'">
                {{ { HIGH: '高风险', MED: '中风险', LOW: '低风险' }[fraudDetail.riskLevel] || '未知' }}
              </el-tag>
            </div>
          </div>
        </div>

        <!-- 五维风险雷达 -->
        <div class="fd-section-title">五维风险分项得分</div>
        <div class="fd-dims">
          <div class="fd-dim-item" v-for="dim in fraudDetailDims" :key="dim.key">
            <div class="fd-dim-header">
              <span class="fd-dim-name">{{ dim.label }}</span>
              <span class="fd-dim-score" :class="dim.score >= 70 ? 'score-high' : dim.score >= 40 ? 'score-med' : 'score-low'">{{ dim.score }}分</span>
            </div>
            <el-progress
              :percentage="dim.score"
              :color="dim.score >= 70 ? '#FF4D4F' : dim.score >= 40 ? '#FA8C16' : '#52C41A'"
              :stroke-width="8"
              :show-text="false"
            />
          </div>
        </div>

        <!-- 关键风险指标 -->
        <div class="fd-section-title">关键风险指标</div>
        <div class="fd-indicators">
          <div class="fd-ind-card" v-for="ind in fraudDetailIndicators" :key="ind.label">
            <div class="fd-ind-val" :style="{ color: ind.color }">{{ ind.value }}</div>
            <div class="fd-ind-label">{{ ind.label }}</div>
          </div>
        </div>

        <!-- 风险线索 -->
        <div v-if="fraudClueList.length" class="fd-section-title" style="margin-top:16px">关联风险线索</div>
        <div v-if="fraudClueList.length" class="fd-clue-list">
          <div class="fd-clue-item" v-for="(clue, ci) in fraudClueList" :key="ci">
            <div class="fd-clue-dot" :class="'dot-' + (clue.riskLevel || '').toLowerCase()"></div>
            <div class="fd-clue-body">
              <div class="fd-clue-type">{{ clue.clueType }}
                <el-tag size="mini" :type="clue.riskLevel === 'RED' ? 'danger' : 'warning'">{{ clue.riskLevel === 'RED' ? '严重' : '警告' }}</el-tag>
              </div>
              <div class="fd-clue-desc">{{ clue.description }}</div>
              <div class="fd-clue-meta">发现时间：{{ clue.foundTime }} · 负责人：{{ clue.assignee }} · 状态：{{ { PENDING: '待处置', PROCESSING: '处理中', DONE: '已完成', CLOSED: '已关闭' }[clue.status] || clue.status }}</div>
            </div>
          </div>
        </div>
      </div>
      <div v-else style="text-align:center;padding:40px 0;color:#909399">暂无数据</div>
      <div slot="footer">
        <el-button size="small" @click="fraudDialogVisible = false">关 闭</el-button>
        <el-button size="small" type="primary" @click="goToFraudPage">查看完整分析</el-button>
      </div>
    </el-dialog>

    <!-- 预警详情对话框 -->
    <el-dialog :visible.sync="warningDialogVisible" :title="warningDialog.title || '预警详情'" width="620px" append-to-body>
      <div v-loading="warningDialog.loading" style="min-height:120px">
        <el-descriptions v-if="warningDialog.data" :column="1" border size="small">
          <el-descriptions-item label="企业">{{ warningDialog.data.companyName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="预警类型">{{ warningDialog.data.warningType || '-' }}</el-descriptions-item>
          <el-descriptions-item label="等级">
            <el-tag size="mini" :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'info' }[warningDialog.data.warningLevel] || 'info'">
              {{ { HIGH: '高', MEDIUM: '中', LOW: '低' }[warningDialog.data.warningLevel] || warningDialog.data.warningLevel || '-' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="标题">{{ warningDialog.data.warningTitle || '-' }}</el-descriptions-item>
          <el-descriptions-item label="内容">{{ warningDialog.data.warningContent || '-' }}</el-descriptions-item>
          <el-descriptions-item label="状态">{{ warningDialog.data.status || '-' }}</el-descriptions-item>
          <el-descriptions-item label="产生时间">{{ warningDialog.data.createTime || '-' }}</el-descriptions-item>
        </el-descriptions>
        <div v-else-if="!warningDialog.loading" style="text-align:center;color:#909399;padding:24px 0">未查询到详情</div>
      </div>
      <div slot="footer">
        <el-button size="small" @click="warningDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 下部：两金趋势 + 预算执行 + 预警列表 -->
    <el-row :gutter="16" class="bottom-row">
      <el-col :span="9">
        <el-card shadow="never" class="section-card">
          <div slot="header" class="card-header">
            <span><i class="el-icon-coin"></i> "两金"余额趋势（近12月）</span>
          </div>
          <div class="trend-chart">
            <div class="trend-bars">
              <div v-for="(item, idx) in twoGoldTrend" :key="idx" class="trend-month-col">
                <div class="bar-group">
                  <div
                    class="trend-bar bar-receivable"
                    :style="{ height: (item.receivable / maxTwoGold * 80) + 'px' }"
                    :title="'应收账款：' + item.receivable + '亿'"
                  ></div>
                  <div
                    class="trend-bar bar-inventory"
                    :style="{ height: (item.inventory / maxTwoGold * 80) + 'px' }"
                    :title="'存货：' + item.inventory + '亿'"
                  ></div>
                </div>
                <div class="trend-month-label">{{ item.month }}</div>
              </div>
            </div>
            <div class="trend-legend">
              <span class="tl-item"><span class="tl-dot dot-receivable"></span>应收账款</span>
              <span class="tl-item"><span class="tl-dot dot-inventory"></span>存货</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="7">
        <el-card shadow="never" class="section-card">
          <div slot="header" class="card-header">
            <span><i class="el-icon-pie-chart"></i> 预算执行综合达标</span>
          </div>
          <div class="budget-summary">
            <div class="bs-donut">
              <div class="donut-center">
                <div class="donut-num">{{ budgetStats.reachRate }}%</div>
                <div class="donut-label">综合达标率</div>
              </div>
            </div>
            <div class="bs-detail">
              <div class="bs-item">
                <span class="bs-dot dot-ok"></span>
                <span class="bs-name">达标企业</span>
                <span class="bs-val green">{{ budgetStats.okCount }}家</span>
              </div>
              <div class="bs-item">
                <span class="bs-dot dot-warn"></span>
                <span class="bs-name">超支企业</span>
                <span class="bs-val orange">{{ budgetStats.overCount }}家</span>
              </div>
              <div class="bs-item">
                <span class="bs-dot dot-danger"></span>
                <span class="bs-name">严重超支</span>
                <span class="bs-val red">{{ budgetStats.dangerCount }}家</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="never" class="section-card">
          <div slot="header" class="card-header">
            <span><i class="el-icon-bell"></i> 会计预警信息</span>
            <el-badge :value="warningList.length" type="danger" />
          </div>
          <div class="warning-scroll">
            <div v-for="(w, idx) in warningList" :key="idx" class="w-item" @click="viewWarning(w)">
              <div class="w-level-dot" :class="'dot-' + w.level.toLowerCase()"></div>
              <div class="w-body">
                <div class="w-text">{{ w.title }}</div>
                <div class="w-meta">{{ w.company }} · {{ w.time }}</div>
              </div>
              <el-tag size="mini" :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'info' }[w.level]">
                {{ { HIGH: '高', MEDIUM: '中', LOW: '低' }[w.level] }}
              </el-tag>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getAccountingDashboard, getAccountingWarningList, getFraudRiskList, getFraudClueList, getReportQualityScore, getWarningDetail } from '@/api/stateAssets/accountingPenetration'
import { mapGetters } from 'vuex'
import { investThemeMixin } from '../../themeMixin'
export default {
  name: 'AccountingDashboard',
  mixins: [investThemeMixin],
  data() {
    const now = new Date()
    return {
      currentDate: now.toLocaleDateString('zh-CN'),
      selectedYear: now.getFullYear(),
      selectedCompany: 'all',
      yearOptions: [now.getFullYear(), now.getFullYear() - 1, now.getFullYear() - 2],
      companyOptions: [
        { value: 'all', label: '全集团综合' },
      ],
      kpiCards: [
        { icon: 'el-icon-office-building', label: '纳管企业数', value: '-', alertClass: '', badge: false },
        { icon: 'el-icon-s-check', label: '凭证异常数', value: '-', alertClass: 'kpi-warn', badge: true },
        { icon: 'el-icon-s-flag', label: '造假风险企业', value: '-', alertClass: 'kpi-danger', badge: true },
        { icon: 'el-icon-pie-chart', label: '预算超支企业', value: '-', alertClass: 'kpi-warn', badge: true },
        { icon: 'el-icon-coin', label: '"两金"未达标', value: '-', alertClass: 'kpi-warn', badge: true },
        { icon: 'el-icon-data-analysis', label: '报表质量均分', value: '-', alertClass: 'kpi-ok', badge: false },
      ],
      fraudCols: ['业绩假', '杠杆假', '出清假', '研发假', '两金假'],
      heatmapData: [],
      radarDims: [
        { label: '准确性', score: 0 },
        { label: '完整性', score: 0 },
        { label: '及时性', score: 0 },
        { label: '规范性', score: 0 },
        { label: '可靠性', score: 0 },
      ],
      twoGoldTrend: [],
      budgetStats: { reachRate: 0, okCount: 0, overCount: 0, dangerCount: 0 },
      warningList: [],
      fraudDialogVisible: false,
      fraudDetailLoading: false,
      fraudDetail: null,
      fraudDialogTitle: '',
      fraudClueList: [],
      warningDialogVisible: false,
      warningDialog: { title: '', loading: false, data: null },
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
    maxTwoGold() {
      if (!this.twoGoldTrend.length) return 1
      return Math.max(...this.twoGoldTrend.map(d => Math.max(d.receivable, d.inventory)))
    },
    fraudDetailDims() {
      if (!this.fraudDetail) return []
      const d = this.fraudDetail
      return [
        { key: 'perf', label: '业绩造假', score: d.perfScore || 0 },
        { key: 'lever', label: '杠杆造假', score: d.leverScore || 0 },
        { key: 'clear', label: '出清造假', score: d.clearScore || 0 },
        { key: 'rd', label: '研发造假', score: d.rdScore || 0 },
        { key: 'twogold', label: '两金造假', score: d.twoGoldScore || 0 },
      ]
    },
    fraudDetailIndicators() {
      if (!this.fraudDetail) return []
      const d = this.fraudDetail
      const fmt = (v, suffix) => v != null ? v + (suffix || '') : '-'
      const fmtPct = (v) => v != null ? v + '%' : '-'
      const colorFn = (v, t, g) => v != null ? (v >= t ? '#FF4D4F' : v >= g ? '#FA8C16' : '#52C41A') : '#909399'
      const indicators = [
        { label: '收入增长率', value: fmtPct(d.revenueGrowth), color: colorFn(d.revenueGrowth, 30, 10) },
        { label: '行业均值', value: fmtPct(d.industryAvg), color: '#606266' },
        { label: 'Z分数', value: fmt(d.zScore), color: colorFn(d.zScore, 0, 2) },
        { label: '毛利率', value: fmtPct(d.grossMargin), color: '#606266' },
        { label: '净利润现金含量', value: fmt(d.cashContent), color: colorFn(d.cashContent, 0, 0.5) },
        { label: '12月收入占比', value: fmtPct(d.dec12Rate), color: colorFn(d.dec12Rate, 30, 15) },
      ]
      if (d.fraudType === 'RD' || d.rdAmount != null) {
        indicators.push(
          { label: '研发投入(万)', value: fmt(d.rdAmount), color: '#606266' },
          { label: '资本化率', value: fmtPct(d.capitalRate), color: colorFn(d.capitalRate, 60, 30) },
          { label: '人均产出', value: fmt(d.outputPerPerson), color: '#606266' },
          { label: '专利数', value: fmt(d.patents), color: '#606266' },
        )
      }
      return indicators
    },
  },
  mounted() { this.loadData() },
  methods: {
    // 根据 selectedCompany 取 label（用于按企业名称查询后端）
    getSelectedCompanyName() {
      if (!this.selectedCompany || this.selectedCompany === 'all') return ''
      const hit = this.companyOptions.find(c => c.value === this.selectedCompany)
      return hit ? hit.label : ''
    },
    async loadData() {
      const year = this.selectedYear
      const companyName = this.getSelectedCompanyName()
      try {
        const res = await getAccountingDashboard({ year, companyName })
        if (res && res.result === 200 && res.data) {
          const d = res.data
          this.kpiCards[0].value = (d.companyCount || 0) + '家'
          this.kpiCards[1].value = (d.voucherAnomalyCount || 0) + '张'
          this.kpiCards[2].value = (d.fraudRiskCount || 0) + '家'
          this.kpiCards[3].value = (d.budgetOverCount || 0) + '家'
          this.kpiCards[4].value = (d.twoGoldWarnCount || 0) + '家'
          this.kpiCards[5].value = d.qualityScore || '-'
          this.heatmapData = d.heatmapData || []
          this.twoGoldTrend = d.twoGoldTrend || []
          if (d.budgetStats) this.budgetStats = d.budgetStats
          if (d.companyOptions && d.companyOptions.length) this.companyOptions = [{ value: 'all', label: '全集团综合' }, ...d.companyOptions]
        }
      } catch (e) { /* keep defaults */ }
      // 雷达评分
      this.updateRadar()
      // 预警列表
      try {
        const wRes = await getAccountingWarningList({ pageSize: 10, year, companyName })
        if (wRes && wRes.result === 200 && wRes.data) {
          this.warningList = (wRes.data.tlist || wRes.data.list || []).map(w => ({
            warningId: w.warningId || w.id || '',
            warningType: w.warningType || '',
            level: w.level || w.warningLevel || 'MEDIUM',
            title: w.title || w.warningTitle || w.text || '',
            text: w.text || w.warningContent || '',
            company: w.company || w.companyName || '',
            time: w.time || '',
            status: w.status || '',
          }))
        }
      } catch (e) { this.warningList = [] }
    },
    updateRadar() {
      const params = { year: this.selectedYear }
      const companyName = this.getSelectedCompanyName()
      if (companyName) params.companyName = companyName
      getReportQualityScore(params).then(res => {
        if (res && res.result === 200 && res.data && res.data.dims) {
          this.radarDims = res.data.dims
        } else {
          // 后端返回空数据时回落到默认 0 分五维
          this.radarDims = this.radarDims.map(d => ({ label: d.label, score: 0 }))
        }
      }).catch(() => {
        this.radarDims = this.radarDims.map(d => ({ label: d.label, score: 0 }))
      })
    },
    getHeatClass(val) {
      if (val >= 70) return 'heat-high'
      if (val >= 40) return 'heat-medium'
      return 'heat-low'
    },
    viewFraudDetail(row, colIdx) {
      const fraudTypeNames = ['业绩造假', '杠杆造假', '出清造假', '研发造假', '两金造假']
      this.fraudDialogTitle = row.company + ' - ' + fraudTypeNames[colIdx] + '风险分析'
      this.fraudDialogVisible = true
      this.fraudDetailLoading = true
      this.fraudDetail = null
      this.fraudClueList = []

      // 查询该公司的造假综合记录（不按 fraudType 精确过滤，避免 DB 里分类不全导致查空）
      getFraudRiskList({ companyName: row.company, pageSize: 5 }).then(res => {
        if (res && res.result === 200 && res.data) {
          const list = res.data.tlist || res.data.list || []
          if (list.length) {
            // 热力图用的分数来自综合记录，直接取第一条即可
            this.fraudDetail = list[0]
          } else {
            // 兜底：从热力图行数据重建一条最小可渲染记录
            this.fraudDetail = {
              companyName: row.company,
              totalScore: Array.isArray(row.scores) ? Math.max(...row.scores) : 0,
              perfScore: row.scores && row.scores[0] != null ? row.scores[0] : 0,
              leverScore: row.scores && row.scores[1] != null ? row.scores[1] : 0,
              clearScore: row.scores && row.scores[2] != null ? row.scores[2] : 0,
              rdScore: row.scores && row.scores[3] != null ? row.scores[3] : 0,
              twoGoldScore: row.scores && row.scores[4] != null ? row.scores[4] : 0,
              alertLevel: (Array.isArray(row.scores) && Math.max(...row.scores) >= 70) ? 'RED'
                          : (Array.isArray(row.scores) && Math.max(...row.scores) >= 40) ? 'ORANGE' : 'GREEN',
              clueCount: 0,
            }
          }
        }
      }).catch(() => {}).finally(() => { this.fraudDetailLoading = false })

      // 线索不按 fraudType 过滤，只按公司名查该公司全部线索
      getFraudClueList({ companyName: row.company, pageSize: 10 }).then(res => {
        if (res && res.result === 200 && res.data) {
          this.fraudClueList = res.data.tlist || res.data.list || []
        }
      }).catch(() => {})
    },
    goToFraudPage() {
      this.fraudDialogVisible = false
      this.$router.push('/compliance/Cwzjsb').catch(() => {})
    },
    viewWarning(w) {
      this.warningDialogVisible = true
      this.warningDialog.title = w.title || '预警详情'
      this.warningDialog.loading = true
      this.warningDialog.data = null
      const id = w.warningId || w.id
      // 先用列表数据渲染，Dialog 立刻可见有内容
      this.warningDialog.data = {
        companyName: w.company || w.companyName || '-',
        warningType: w.warningType || '-',
        warningLevel: w.level || w.warningLevel || '-',
        warningTitle: w.title || w.warningTitle || '-',
        warningContent: w.text || w.warningContent || '-',
        status: w.status || '-',
        createTime: w.time || '',
      }
      if (!id) { this.warningDialog.loading = false; return }
      // 有 warningId 再去查后端详情，覆盖列表字段
      getWarningDetail(id).then(res => {
        if (res && res.result === 200 && res.data) {
          const d = res.data
          this.warningDialog.data = {
            companyName: d.companyName || this.warningDialog.data.companyName,
            warningType: d.warningType || this.warningDialog.data.warningType,
            warningLevel: d.warningLevel || this.warningDialog.data.warningLevel,
            warningTitle: d.warningTitle || this.warningDialog.data.warningTitle,
            warningContent: d.warningContent || this.warningDialog.data.warningContent,
            status: d.status || this.warningDialog.data.status,
            createTime: d.createTime ? String(d.createTime).replace('T', ' ').substring(0, 19) : this.warningDialog.data.createTime,
          }
        }
      }).catch(() => {
        // 后端查询失败不清空 Dialog，用列表 fallback 数据
      }).finally(() => { this.warningDialog.loading = false })
    },
  },
}
</script>

<style lang="scss" scoped>
.accounting-dashboard { padding: 16px; background: #f5f7fa; min-height: calc(100vh - 84px); }

.dash-header {
  display: flex; align-items: center; justify-content: space-between;
  background: linear-gradient(135deg, var(--ip-primary) 0%, var(--ip-secondary) 50%, var(--ip-bright) 100%);
  border-radius: 10px; padding: 14px 20px; margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.07); color: #fff;
  .dash-header-left {
    display: flex; align-items: center; gap: 10px; font-size: 16px; font-weight: 600; color: #fff;
    i { font-size: 20px; color: #fff; }
    .dash-date { font-size: 12px; color: rgba(255,255,255,0.8); font-weight: 400; }
  }
}

/* KPI 条 */
.kpi-row { margin-bottom: 16px; }
.kpi-card {
  background: #fff; border-radius: 10px; padding: 14px 16px;
  display: flex; align-items: center; gap: 10px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.07);
  border-left: 3px solid #d9d9d9;
  position: relative;
  .kpi-icon { width: 36px; height: 36px; border-radius: 8px; background: #f0f2f5; display: flex; align-items: center; justify-content: center; i { font-size: 18px; color: #606266; } }
  .kpi-value { font-size: 20px; font-weight: 700; color: #303133; line-height: 1.2; }
  .kpi-label { font-size: 11px; color: #909399; margin-top: 2px; }
  .kpi-alert-dot { position: absolute; top: 8px; right: 8px; width: 8px; height: 8px; border-radius: 50%; background: #FF4D4F; }
  &.kpi-danger { border-left-color: #FF4D4F; .kpi-icon { background: #fff1f0; i { color: #FF4D4F; } } }
  &.kpi-warn { border-left-color: #FA8C16; .kpi-icon { background: #fff7e6; i { color: #FA8C16; } } }
  &.kpi-ok { border-left-color: #52C41A; .kpi-icon { background: #f6ffed; i { color: #52C41A; } } }
}

/* 中部行 */
.mid-row { margin-bottom: 16px; }
.section-card { height: 100%; }
.card-header {
  display: flex; align-items: center; justify-content: space-between;
  span:first-child { font-size: 14px; font-weight: 600; color: #303133; i { margin-right: 6px; color: var(--ip-primary, #1A3A5C); } }
  .header-tip { font-size: 12px; color: #909399; }
}

/* 热力图 */
.heatmap-wrap { padding: 4px 0; }
.heatmap-container { display: flex; flex-direction: column; gap: 3px; }
.heatmap-col-headers { display: flex; align-items: center; margin-bottom: 4px; }
.heatmap-row-label-placeholder { width: 68px; flex-shrink: 0; }
.heatmap-col-label { flex: 1; text-align: center; font-size: 11px; color: #909399; font-weight: 600; }
.heatmap-body { display: flex; flex-direction: column; gap: 4px; }
.heatmap-row { display: flex; align-items: center; gap: 3px; }
.heatmap-row-label { width: 68px; font-size: 11px; color: #606266; text-align: right; padding-right: 8px; flex-shrink: 0; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.heatmap-cell {
  flex: 1; height: 32px; border-radius: 4px; display: flex; align-items: center; justify-content: center;
  font-size: 12px; font-weight: 600; cursor: pointer; transition: all 0.2s;
  &:hover { transform: scale(1.08); z-index: 1; box-shadow: 0 2px 8px rgba(0,0,0,0.2); }
  &.heat-low { background: #f6ffed; color: #52C41A; }
  &.heat-medium { background: #fff7e6; color: #FA8C16; }
  &.heat-high { background: #fff1f0; color: #FF4D4F; }
}
.heatmap-legend { display: flex; align-items: center; gap: 8px; margin-top: 10px; font-size: 12px; color: #909399; }
.legend-item {
  padding: 2px 8px; border-radius: 4px; font-size: 11px;
  &.heat-low { background: #f6ffed; color: #52C41A; }
  &.heat-medium { background: #fff7e6; color: #FA8C16; }
  &.heat-high { background: #fff1f0; color: #FF4D4F; }
}

/* 雷达评分 */
.radar-wrap { padding: 8px 0; }
.radar-scores { display: flex; flex-direction: column; gap: 12px; }
.rs-item { display: flex; align-items: center; gap: 8px; }
.rs-label { width: 52px; font-size: 12px; color: #606266; flex-shrink: 0; }
::v-deep .el-progress { flex: 1; }
.rs-value { width: 32px; text-align: right; font-size: 13px; font-weight: 700; color: #303133; &.score-red { color: #FF4D4F; } }

/* 趋势图 */
.trend-chart { padding: 4px 0; }
.trend-bars { display: flex; align-items: flex-end; gap: 6px; height: 100px; }
.trend-month-col { display: flex; flex-direction: column; align-items: center; flex: 1; }
.bar-group { display: flex; align-items: flex-end; gap: 2px; }
.trend-bar {
  width: 10px; border-radius: 3px 3px 0 0; min-height: 4px; transition: height 0.4s;
  &.bar-receivable { background: var(--ip-primary, #1A3A5C); }
  &.bar-inventory { background: #FA8C16; }
}
.trend-month-label { font-size: 10px; color: #909399; margin-top: 4px; }
.trend-legend { display: flex; gap: 16px; margin-top: 8px; }
.tl-item { display: flex; align-items: center; gap: 4px; font-size: 12px; color: #606266; }
.tl-dot { width: 10px; height: 10px; border-radius: 2px; &.dot-receivable { background: var(--ip-primary, #1A3A5C); } &.dot-inventory { background: #FA8C16; } }

/* 预算 */
.budget-summary { display: flex; align-items: center; gap: 16px; padding: 8px 0; }
.bs-donut {
  width: 100px; height: 100px; border-radius: 50%; flex-shrink: 0;
  background: conic-gradient(#52C41A 0% 68%, #f0f0f0 68% 100%);
  display: flex; align-items: center; justify-content: center;
  .donut-center { width: 72px; height: 72px; border-radius: 50%; background: #fff; display: flex; flex-direction: column; align-items: center; justify-content: center; }
  .donut-num { font-size: 18px; font-weight: 700; color: #52C41A; }
  .donut-label { font-size: 11px; color: #909399; }
}
.bs-detail { display: flex; flex-direction: column; gap: 10px; flex: 1; }
.bs-item { display: flex; align-items: center; gap: 6px; }
.bs-dot { width: 10px; height: 10px; border-radius: 50%; }
.dot-ok { background: #52C41A; } .dot-warn { background: #FA8C16; } .dot-danger { background: #FF4D4F; }
.bs-name { font-size: 12px; color: #606266; flex: 1; }
.bs-val { font-size: 14px; font-weight: 700; &.green { color: #52C41A; } &.orange { color: #FA8C16; } &.red { color: #FF4D4F; } }

/* 预警列表 */
.warning-scroll { max-height: 220px; overflow-y: auto; }
.w-item {
  display: flex; align-items: center; gap: 8px; padding: 8px 0;
  border-bottom: 1px solid #f5f5f5; cursor: pointer;
  &:last-child { border-bottom: none; }
  &:hover { background: #fafafa; }
}
.w-level-dot { width: 8px; height: 8px; border-radius: 50%; flex-shrink: 0; }
.dot-high { background: #FF4D4F; } .dot-medium { background: #FA8C16; } .dot-low { background: #1890FF; }
.w-body { flex: 1; overflow: hidden; }
.w-text { font-size: 12px; color: #303133; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.w-meta { font-size: 11px; color: #c0c4cc; margin-top: 2px; }

.bottom-row { margin-bottom: 0; }

/* 造假风险详情对话框 */
.fraud-detail-body { padding: 0 4px; }
.fd-overview {
  display: flex; align-items: center; gap: 20px;
  background: #f5f7fa; border-radius: 10px; padding: 16px 20px; margin-bottom: 16px;
}
.fd-score-ring {
  width: 80px; height: 80px; border-radius: 50%; flex-shrink: 0;
  display: flex; flex-direction: column; align-items: center; justify-content: center;
  &.ring-red { background: #fff1f0; border: 3px solid #FF4D4F; .fd-score-num { color: #FF4D4F; } }
  &.ring-orange { background: #fff7e6; border: 3px solid #FA8C16; .fd-score-num { color: #FA8C16; } }
  &.ring-green { background: #f6ffed; border: 3px solid #52C41A; .fd-score-num { color: #52C41A; } }
}
.fd-score-num { font-size: 22px; font-weight: 700; line-height: 1.2; }
.fd-score-label { font-size: 10px; color: #909399; margin-top: 2px; }
.fd-meta { flex: 1; }
.fd-company { font-size: 15px; font-weight: 600; color: #303133; margin-bottom: 8px; }
.fd-tags { display: flex; gap: 6px; flex-wrap: wrap; }

.fd-section-title {
  font-size: 13px; font-weight: 600; color: #303133;
  padding-left: 8px; border-left: 3px solid var(--ip-primary, #1A3A5C); margin-bottom: 10px;
}

.fd-dims { display: flex; flex-direction: column; gap: 8px; margin-bottom: 16px; }
.fd-dim-item { display: flex; align-items: center; gap: 8px; }
.fd-dim-header { display: flex; align-items: center; justify-content: space-between; width: 80px; flex-shrink: 0; }
.fd-dim-name { font-size: 12px; color: #606266; }
.fd-dim-score { font-size: 12px; font-weight: 700; &.score-high { color: #FF4D4F; } &.score-med { color: #FA8C16; } &.score-low { color: #52C41A; } }
::v-deep .fd-dim-item .el-progress { flex: 1; }

.fd-indicators { display: flex; flex-wrap: wrap; gap: 8px; margin-bottom: 4px; }
.fd-ind-card {
  flex: 0 0 calc(25% - 6px); background: #fafafa; border-radius: 6px; padding: 10px 12px; text-align: center;
}
.fd-ind-val { font-size: 16px; font-weight: 700; line-height: 1.3; }
.fd-ind-label { font-size: 11px; color: #909399; margin-top: 2px; }

.fd-clue-list { display: flex; flex-direction: column; gap: 8px; }
.fd-clue-item {
  display: flex; gap: 10px; padding: 10px 12px; background: #fafafa; border-radius: 6px;
}
.fd-clue-dot { width: 8px; height: 8px; border-radius: 50%; flex-shrink: 0; margin-top: 5px;
  &.dot-red { background: #FF4D4F; } &.dot-orange { background: #FA8C16; }
}
.fd-clue-body { flex: 1; overflow: hidden; }
.fd-clue-type { font-size: 12px; font-weight: 600; color: #303133; display: flex; align-items: center; gap: 6px; }
.fd-clue-desc { font-size: 12px; color: #606266; margin-top: 4px; line-height: 1.5; }
.fd-clue-meta { font-size: 11px; color: #c0c4cc; margin-top: 4px; }
</style>
