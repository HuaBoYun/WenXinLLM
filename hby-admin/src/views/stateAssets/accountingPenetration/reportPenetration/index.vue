<template>
  <div class="app-container accounting-page">
    <div class="ap-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="ap-header-left"><i class="el-icon-data-analysis"></i><span>财务报表穿透</span></div>
      <div class="ap-header-desc">勾稽关系自动校验 · 报表质量五维评分 · 向下穿透账簿</div>
    </div>

    <el-card shadow="never" class="search-card">
      <el-form :inline="true" size="small">
        <el-form-item label="企业">
          <el-input v-model="queryForm.companyName" placeholder="企业名称" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="年度">
          <el-date-picker v-model="queryForm.year" type="year" value-format="yyyy" placeholder="选择年度" style="width:110px" />
        </el-form-item>
        <el-form-item label="报表类型">
          <el-select v-model="queryForm.reportType" clearable style="width:120px">
            <el-option label="个别报表" value="INDIVIDUAL" /><el-option label="合并报表" value="CONSOLIDATED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
          <el-button icon="el-icon-download" @click="handleExport">导出</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 顶部统计条 -->
    <el-row :gutter="16" class="stat-bar">
      <el-col :span="6" v-for="(s, i) in statCards" :key="i">
        <div class="stat-card" :class="s.cls">
          <i :class="s.icon"></i>
          <div class="sc-body">
            <div class="sc-num">{{ s.num }}</div>
            <div class="sc-label">{{ s.label }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-card shadow="never" style="margin-top:12px">
      <el-tabs v-model="activeTab" @tab-click="handleTabChange">
        <!-- 报表列表 -->
        <el-tab-pane label="报表台账" name="list">
          <el-table v-loading="loading" :data="reportList" border size="small" @row-click="viewReport" style="width:100%;cursor:pointer">
            <el-table-column label="企业名称" prop="companyName" min-width="140" show-overflow-tooltip />
            <el-table-column label="年度" prop="year" width="70" align="center" />
            <el-table-column label="报表类型" prop="reportType" width="90" align="center">
              <template slot-scope="s">
                <el-tag size="mini" :type="s.row.reportType === 'CONSOLIDATED' ? 'primary' : ''">
                  {{ s.row.reportType === 'CONSOLIDATED' ? '合并' : '个别' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="提交状态" prop="submitStatus" width="90" align="center">
              <template slot-scope="s">
                <el-tag size="mini" :type="{ SUBMITTED: 'success', REVIEWING: 'warning', OVERDUE: 'danger', EXCEPTION: 'danger' }[s.row.submitStatus]">
                  {{ { SUBMITTED: '已提交', REVIEWING: '审核中', OVERDUE: '逾期', EXCEPTION: '有异常' }[s.row.submitStatus] }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="审计意见" prop="auditOpinion" width="100" align="center">
              <template slot-scope="s">
                <el-tag size="mini" :type="s.row.auditOpinion === 'UNQUALIFIED' ? 'success' : 'danger'">
                  {{ { UNQUALIFIED: '标准无保留', QUALIFIED: '保留意见', ADVERSE: '否定意见', DISCLAIMER: '无法表示' }[s.row.auditOpinion] || '-' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="质量评分" prop="qualityScore" width="80" align="center">
              <template slot-scope="s">
                <span :class="s.row.qualityScore < 60 ? 'score-red' : s.row.qualityScore < 80 ? 'score-orange' : 'score-green'">
                  {{ s.row.qualityScore }}
                </span>
              </template>
            </el-table-column>
            <el-table-column label="勾稽问题" prop="hookIssues" width="80" align="center">
              <template slot-scope="s">
                <el-tag v-if="s.row.hookIssues > 0" type="danger" size="mini">{{ s.row.hookIssues }}项</el-tag>
                <span v-else class="score-green">通过</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80" align="center">
              <template slot-scope="s">
                <el-button type="text" size="mini" @click.stop="viewReport(s.row)">查看</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
            background style="margin-top:12px;text-align:right"
            :current-page="page.pageNumber" :page-sizes="[10,20,50]"
            :page-size="page.pageSize" layout="total, sizes, prev, pager, next, jumper"
            :total="page.total"
            @size-change="val => { page.pageSize = val; loadReportList() }"
            @current-change="val => { page.pageNumber = val; loadReportList() }"
          />
        </el-tab-pane>

        <!-- 资产负债表 -->
        <el-tab-pane label="资产负债表" name="balance">
          <div class="current-report-tip" v-if="currentReport.companyName">
            <i class="el-icon-document"></i>
            当前报表：<strong>{{ currentReport.companyName }}</strong>
            <el-tag size="mini" style="margin-left:6px">{{ currentReport.year }}年</el-tag>
            <el-tag size="mini" :type="currentReport.reportType === 'CONSOLIDATED' ? 'primary' : ''" style="margin-left:4px">
              {{ currentReport.reportType === 'CONSOLIDATED' ? '合并' : '个别' }}
            </el-tag>
            <el-button type="text" size="mini" style="margin-left:10px" @click="activeTab = 'list'">返回列表</el-button>
          </div>
          <div class="report-compare-tip">
            <el-radio-group v-model="compareMode" size="mini" @change="loadBalanceSheet">
              <el-radio-button label="single">本期</el-radio-button>
              <el-radio-button label="compare">本期 vs 上期</el-radio-button>
            </el-radio-group>
            <div class="check-result" v-if="hookCheckResults.length">
              <el-tag type="danger" size="mini">{{ hookCheckResults.length }}项勾稽问题</el-tag>
            </div>
          </div>
          <div v-if="!balanceSheetData.length && !loading" class="empty-tip">
            <i class="el-icon-info"></i> 请先在报表台账中点击"查看"选择一份报表，或在顶部筛选后查询
          </div>
          <el-table v-else :data="balanceSheetData" border size="small" :span-method="arraySpanMethod" style="width:100%">
            <el-table-column label="项目" prop="item" min-width="180">
              <template slot-scope="s">
                <span :class="s.row.isCategory ? 'category-row' : ''">{{ s.row.item }}</span>
              </template>
            </el-table-column>
            <el-table-column label="期末金额(万元)" prop="current" width="130" align="right">
              <template slot-scope="s">
                <span v-if="!s.row.isCategory">{{ formatMoney(s.row.current) }}</span>
              </template>
            </el-table-column>
            <el-table-column v-if="compareMode === 'compare'" label="上期金额(万元)" prop="prev" width="130" align="right">
              <template slot-scope="s">
                <span v-if="!s.row.isCategory">{{ formatMoney(s.row.prev) }}</span>
              </template>
            </el-table-column>
            <el-table-column v-if="compareMode === 'compare'" label="变动率" prop="changeRate" width="90" align="center">
              <template slot-scope="s">
                <span v-if="!s.row.isCategory && s.row.changeRate != null" :class="Math.abs(s.row.changeRate) > 30 ? 'score-orange' : ''">
                  {{ s.row.changeRate > 0 ? '+' : '' }}{{ s.row.changeRate }}%
                </span>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 利润表 -->
        <el-tab-pane label="利润表" name="income">
          <div class="current-report-tip" v-if="currentReport.companyName">
            <i class="el-icon-document"></i>
            当前报表：<strong>{{ currentReport.companyName }}</strong>
            <el-tag size="mini" style="margin-left:6px">{{ currentReport.year }}年</el-tag>
            <el-button type="text" size="mini" style="margin-left:10px" @click="activeTab = 'list'">返回列表</el-button>
          </div>
          <div class="anomaly-alert" v-if="incomeAnomalies.length">
            <i class="el-icon-warning-outline"></i>
            <span>检测到 {{ incomeAnomalies.length }} 项异常指标：</span>
            <el-tag v-for="(a, i) in incomeAnomalies" :key="i" size="mini" :type="a.level === 'red' ? 'danger' : 'warning'" style="margin-left:6px">{{ a.name }}</el-tag>
          </div>
          <div v-if="!incomeData.length && !loading" class="empty-tip">
            <i class="el-icon-info"></i> 请先在报表台账中点击"查看"选择一份报表
          </div>
          <el-table v-else :data="incomeData" border size="small" style="width:100%">
            <el-table-column label="项目" prop="item" min-width="180">
              <template slot-scope="s"><span :class="s.row.isKey ? 'key-row' : ''">{{ s.row.item }}</span></template>
            </el-table-column>
            <el-table-column label="本期金额(万元)" prop="current" width="140" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.current) }}</template>
            </el-table-column>
            <el-table-column label="上期金额(万元)" prop="prev" width="140" align="right">
              <template slot-scope="s">{{ formatMoney(s.row.prev) }}</template>
            </el-table-column>
            <el-table-column label="同比增长" prop="growth" width="100" align="center">
              <template slot-scope="s">
                <span v-if="s.row.growth != null" :class="s.row.anomaly ? 'score-red' : s.row.growth > 0 ? 'score-green' : 'score-orange'">
                  {{ s.row.growth > 0 ? '+' : '' }}{{ s.row.growth }}%
                </span>
              </template>
            </el-table-column>
            <el-table-column label="异常" width="80" align="center">
              <template slot-scope="s">
                <el-tag v-if="s.row.anomaly" type="danger" size="mini" effect="plain">预警</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 报表质量评估 -->
        <el-tab-pane label="报表质量评估" name="quality">
          <div class="current-report-tip" v-if="currentReport.companyName">
            <i class="el-icon-document"></i>
            当前报表：<strong>{{ currentReport.companyName }}</strong>
            <el-tag size="mini" style="margin-left:6px">{{ currentReport.year }}年</el-tag>
            <el-button type="text" size="mini" style="margin-left:10px" @click="activeTab = 'list'">返回列表</el-button>
          </div>
          <div v-if="!hookCheckList.length && !loading" class="empty-tip">
            <i class="el-icon-info"></i> 请先在报表台账中点击"查看"选择一份报表
          </div>
          <el-row v-else :gutter="20">
            <el-col :span="12">
              <div class="quality-dims">
                <div class="qd-title">五维度评分</div>
                <div v-for="(dim, i) in qualityDims" :key="i" class="qd-item">
                  <div class="qd-label">{{ dim.label }}</div>
                  <el-progress
                    :percentage="Math.round(dim.score)"
                    :color="dim.score < 60 ? '#FF4D4F' : dim.score < 80 ? '#FA8C16' : '#52C41A'"
                    :stroke-width="12"
                  />
                  <div class="qd-score" :class="dim.score < 60 ? 'score-red' : ''">{{ Math.round(dim.score) }}</div>
                </div>
              </div>
            </el-col>
            <el-col :span="12">
              <div class="hook-check-panel">
                <div class="hcp-title">勾稽关系核查结果</div>
                <div v-for="(r, i) in hookCheckList" :key="i" class="hcp-item">
                  <i :class="r.pass ? 'el-icon-circle-check hcp-ok' : 'el-icon-circle-close hcp-fail'"></i>
                  <span class="hcp-name">{{ r.name }}</span>
                  <span class="hcp-desc" v-if="!r.pass && r.desc">{{ r.desc }}</span>
                  <span class="hcp-desc" v-else-if="!r.pass">存在勾稽差异，请核查</span>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import { getReportList, getReportQualityScore, exportReport } from '@/api/stateAssets/accountingPenetration'
import { mapGetters } from 'vuex'
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
  name: 'AccountingReportPenetration',
  data() {
    return {
      loading: false,
      activeTab: 'list',
      compareMode: 'single',
      queryForm: { companyName: '', year: '', reportType: '' },
      page: { pageNumber: 1, pageSize: 10, total: 0 },
      reportList: [],
      currentReport: { companyName: '', year: '', reportType: '' }, // 当前选中报表上下文
      balanceSheetData: [],
      incomeAnomalies: [],
      incomeData: [],
      qualityDims: [
        { label: '准确性', score: 0 },
        { label: '完整性', score: 0 },
        { label: '及时性', score: 0 },
        { label: '规范性', score: 0 },
        { label: '可靠性', score: 0 },
      ],
      hookCheckList: [],
      hookCheckResults: [],
      statCards: [
        { num: 0, label: '报表总数', icon: 'el-icon-document', cls: 'sc-info' },
        { num: 0, label: '已提交', icon: 'el-icon-circle-check', cls: 'sc-success' },
        { num: 0, label: '有异常/逾期', icon: 'el-icon-warning', cls: 'sc-danger' },
        { num: 0, label: '质量均分', icon: 'el-icon-data-analysis', cls: 'sc-primary' },
      ],
    }
  },
  created() {
    this.loadReportList()
    this.loadStatCards()
  },
  methods: {
    // 剔除空值
    buildParams(extra) {
      const p = { ...this.queryForm, ...(extra || {}) }
      Object.keys(p).forEach(k => {
        if (p[k] === '' || p[k] === null || p[k] === undefined) delete p[k]
      })
      return p
    },
    // 取当前上下文 companyName/year（优先用选中的报表，否则用顶部筛选）
    ctxParams() {
      const p = {}
      const cn = this.currentReport.companyName || this.queryForm.companyName
      const yr = this.currentReport.year || this.queryForm.year
      if (cn) p.companyName = cn
      if (yr) p.year = yr
      return p
    },

    async loadReportList() {
      this.loading = true
      try {
        const params = this.buildParams({ pageNumber: this.page.pageNumber, pageSize: this.page.pageSize })
        const res = await getReportList(params)
        if (res && res.result === 200 && res.data) {
          this.reportList = res.data.tlist || []
          this.page.total = res.data.totalRecord || 0
        } else { this.reportList = []; this.page.total = 0 }
      } catch (e) { this.reportList = []; this.page.total = 0 } finally { this.loading = false }
    },

    // 统计条：独立请求不加任何筛选，显示全局数字
    async loadStatCards() {
      try {
        const res = await getReportList({ pageSize: 500 }) // 拉一批算
        if (res && res.result === 200 && res.data) {
          const list = res.data.tlist || []
          const total = res.data.totalRecord || list.length
          const submitted = list.filter(r => r.submitStatus === 'SUBMITTED').length
          const exception = list.filter(r => ['OVERDUE', 'EXCEPTION'].indexOf(r.submitStatus) >= 0).length
          const scores = list.map(r => r.qualityScore).filter(s => typeof s === 'number')
          const avg = scores.length ? Math.round(scores.reduce((a, b) => a + b, 0) / scores.length) : 0
          this.statCards[0].num = total
          this.statCards[1].num = submitted
          this.statCards[2].num = exception
          this.statCards[3].num = avg
        }
      } catch (e) { /* keep zero */ }
    },

    handleQuery() {
      this.page.pageNumber = 1
      this.loadDataByTab()
    },
    resetQuery() {
      this.queryForm = { companyName: '', year: '', reportType: '' }
      this.currentReport = { companyName: '', year: '', reportType: '' }
      this.page.pageNumber = 1
      this.activeTab = 'list'
      this.loadReportList()
    },
    handleTabChange() { this.loadDataByTab() },
    loadDataByTab() {
      const tab = this.activeTab
      if (tab === 'list') this.loadReportList()
      else if (tab === 'balance') this.loadBalanceSheet()
      else if (tab === 'income') this.loadIncome()
      else if (tab === 'quality') this.loadQuality()
    },

    async loadBalanceSheet() {
      this.loading = true
      try {
        const res = await getReportList(this.buildParams({ ...this.ctxParams(), reportTab: 'balance', compareMode: this.compareMode }))
        if (res && res.result === 200 && res.data) {
          this.balanceSheetData = res.data.balanceSheetData || []
        } else { this.balanceSheetData = [] }
      } catch (e) { this.balanceSheetData = [] } finally { this.loading = false }
    },

    async loadIncome() {
      // 利润表数据由 quality 接口返回 incomeData + incomeAnomalies
      this.loading = true
      try {
        const res = await getReportQualityScore(this.buildParams(this.ctxParams()))
        if (res && res.result === 200 && res.data) {
          this.incomeData = res.data.incomeData || []
          this.incomeAnomalies = res.data.incomeAnomalies || []
        } else { this.incomeData = []; this.incomeAnomalies = [] }
      } catch (e) { this.incomeData = []; this.incomeAnomalies = [] } finally { this.loading = false }
    },

    async loadQuality() {
      this.loading = true
      try {
        const res = await getReportQualityScore(this.buildParams(this.ctxParams()))
        if (res && res.result === 200 && res.data) {
          this.qualityDims = res.data.dims || this.qualityDims
          this.hookCheckList = res.data.hookCheckList || []
          this.hookCheckResults = res.data.hookCheckResults || []
          this.incomeAnomalies = res.data.incomeAnomalies || []
        }
      } catch (e) { /* keep defaults */ } finally { this.loading = false }
    },

    viewReport(row) {
      this.currentReport = { companyName: row.companyName, year: row.year, reportType: row.reportType }
      this.activeTab = 'balance'
      this.loadBalanceSheet()
      this.loadQuality()
    },

    async handleExport() {
      try {
        const resp = await exportReport(this.buildParams())
        downloadBlob(resp, '财务报表穿透.csv')
      } catch (e) { this.$message.error('导出失败') }
    },

    arraySpanMethod({ row, columnIndex }) {
      if (row.isCategory) {
        if (columnIndex === 0) return [1, 4]
        return [0, 0]
      }
    },
    formatMoney(v) {
      if (!v && v !== 0) return '-'
      return Number(v).toLocaleString('zh-CN', { minimumFractionDigits: 2 })
    },
  },
}

// ========== 辅助：下载 Blob ==========
function downloadBlob(resp, defaultName) {
  const blob = new Blob([resp.data], { type: resp.headers['content-type'] || 'application/octet-stream' })
  const disposition = resp.headers['content-disposition'] || ''
  let fileName = defaultName
  const m = disposition.match(/filename\*?=(?:UTF-8''|")?([^;"']+)/i)
  if (m && m[1]) { try { fileName = decodeURIComponent(m[1].replace(/^"|"$/g, '')) } catch (e) {} }
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url; a.download = fileName
  document.body.appendChild(a); a.click(); document.body.removeChild(a)
  URL.revokeObjectURL(url)
}
</script>

<style lang="scss" scoped>
.accounting-page { padding: 16px; background: #f5f7fa; min-height: calc(100vh - 84px); }
.ap-header {
  display: flex; align-items: center; justify-content: space-between;
  padding: 14px 20px; 
  border-radius: 8px; color: #fff; margin-bottom: 14px;
  .ap-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; color: #FA8C16; } }
  .ap-header-desc { font-size: 13px; opacity: 0.8; }
}
.search-card { border-left: 3px solid #1A3A5C; ::v-deep .el-form-item { margin-bottom: 0; } }

/* 顶部统计条 */
.stat-bar { margin-top: 12px; }
.stat-card {
  display: flex; align-items: center; gap: 12px;
  background: #fff; border-radius: 8px; padding: 12px 16px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05); border-left: 3px solid #d9d9d9;
  i { font-size: 28px; color: #909399; }
  .sc-body { flex: 1; }
  .sc-num { font-size: 22px; font-weight: 700; color: #303133; line-height: 1.2; }
  .sc-label { font-size: 12px; color: #909399; margin-top: 2px; }
  &.sc-info { border-left-color: #1A3A5C; i { color: #1A3A5C; } }
  &.sc-success { border-left-color: #52C41A; i { color: #52C41A; } }
  &.sc-danger { border-left-color: #FF4D4F; i { color: #FF4D4F; } }
  &.sc-primary { border-left-color: #FA8C16; i { color: #FA8C16; } }
}

/* 当前报表提示条 */
.current-report-tip {
  display: flex; align-items: center; gap: 6px;
  background: #e6f7ff; border: 1px solid #91d5ff; border-radius: 6px;
  padding: 8px 12px; margin-bottom: 10px; font-size: 13px; color: #1A3A5C;
  i { color: #1A3A5C; margin-right: 4px; }
}

/* 空数据提示 */
.empty-tip {
  text-align: center; padding: 40px 0; color: #909399; font-size: 13px;
  i { margin-right: 6px; color: #1A3A5C; }
}

.score-red { color: #FF4D4F; font-weight: 700; }
.score-orange { color: #FA8C16; font-weight: 600; }
.score-green { color: #52C41A; font-weight: 600; }
.category-row { font-weight: 700; color: #1A3A5C; }
.key-row { font-weight: 700; color: #303133; }
.report-compare-tip { display: flex; align-items: center; gap: 12px; margin-bottom: 10px; }
.anomaly-alert {
  display: flex; align-items: center; background: #fff7e6; border: 1px solid #ffd591;
  border-radius: 6px; padding: 8px 12px; margin-bottom: 10px; font-size: 13px; color: #FA8C16;
  i { margin-right: 6px; }
}
.quality-dims { padding: 8px 0; }
.qd-title { font-size: 14px; font-weight: 600; color: #303133; margin-bottom: 16px; }
.qd-item { display: flex; align-items: center; gap: 10px; margin-bottom: 14px; }
.qd-label { width: 55px; font-size: 13px; color: #606266; flex-shrink: 0; }
::v-deep .el-progress { flex: 1; }
.qd-score { width: 32px; font-size: 14px; font-weight: 700; color: #303133; text-align: right; }
.hook-check-panel { padding: 8px 0; }
.hcp-title { font-size: 14px; font-weight: 600; color: #303133; margin-bottom: 14px; }
.hcp-item { display: flex; align-items: flex-start; gap: 8px; margin-bottom: 10px; font-size: 13px; }
.hcp-ok { color: #52C41A; font-size: 16px; }
.hcp-fail { color: #FF4D4F; font-size: 16px; }
.hcp-name { color: #303133; flex: 1; }
.hcp-desc { color: #FF4D4F; font-size: 12px; }
::v-deep .el-table th { background: #f0f4f8; }
</style>
