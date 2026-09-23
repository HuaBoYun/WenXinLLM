<template>
  <div class="anomaly-detect-wrap">
    <!-- Banner（橙色预警主题） -->
    <div class="page-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-left">
        <div class="banner-title">财务异常检测</div>
        <div class="banner-sub">运用Benford定律、利润/现金流背离等多重数学模型，智能识别财务数据造假疑点</div>
      </div>
      <div class="banner-right">
        <div v-for="q in bannerQuadrants" :key="q.label" class="banner-quad">
          <div class="bq-value">{{ q.value }}</div>
          <div class="bq-label">{{ q.label }}</div>
        </div>
      </div>
    </div>

    <!-- 检测方法标签切换 -->
    <el-card shadow="never" style="margin-bottom:14px">
      <div style="display:flex;gap:10px;align-items:center;flex-wrap:wrap">
        <span style="font-weight:600;color:#333">检测方法：</span>
        <el-button v-for="m in detectMethods" :key="m.key" :type="activeMethod===m.key?'primary':''" size="small" @click="activeMethod=m.key">
          {{ m.label }}
        </el-button>
        <el-select v-model="selectedCompany" style="width:180px;margin-left:auto" size="small" placeholder="选择企业">
          <el-option label="全部企业" value=""/>
          <el-option v-for="c in companyList" :key="c.id" :label="c.name" :value="c.id"/>
        </el-select>
        <el-button type="primary" size="small" @click="runDetect">运行检测</el-button>
      </div>
    </el-card>

    <!-- Benford定律分析区 -->
    <template v-if="activeMethod==='benford'">
      <el-row :gutter="16" style="margin-bottom:16px">
        <el-col :span="14">
          <el-card shadow="never">
            <div slot="header" class="card-header">Benford定律——首位数字分布对比（{{ benfordTarget }}）</div>
            <div ref="benfordChart" style="height:280px"></div>
          </el-card>
        </el-col>
        <el-col :span="10">
          <el-card shadow="never">
            <div slot="header" class="card-header">各企业Benford偏离度评分</div>
            <div v-for="ent in benfordScores" :key="ent.company" class="benford-item">
              <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:4px">
                <span style="font-size:13px;font-weight:600">{{ ent.company }}</span>
                <span style="font-size:12px;color:#666">χ²={{ ent.chiSq }}</span>
                <el-tag :type="ent.level==='HIGH'?'danger':ent.level==='MEDIUM'?'warning':'success'" size="mini">
                  {{ ent.level==='HIGH'?'显著偏离':ent.level==='MEDIUM'?'轻微偏离':'正常' }}
                </el-tag>
              </div>
              <el-progress :percentage="ent.score" :stroke-width="8"
                :color="ent.level==='HIGH'?'#F5222D':ent.level==='MEDIUM'?'FA8C16':'#52C41A'"/>
              <el-divider style="margin:8px 0"/>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-card shadow="never" style="margin-bottom:16px">
        <div slot="header" class="card-header">Benford定律——企业切换</div>
        <div style="display:flex;gap:8px;margin-bottom:10px;flex-wrap:wrap">
          <el-button v-for="b in benfordCompanies" :key="b.company" :type="benfordTarget===b.company?'primary':''"
            size="mini" @click="switchBenford(b)">{{ b.company }}</el-button>
        </div>
        <el-alert type="info" :closable="false" show-icon>
          <span slot="title">Benford定律原理：真实的财务数据中，首位数字为1的概率约为30.1%，为2约为17.6%，...为9约为4.6%。χ²统计量>16.9（p&lt;0.05）则判为显著偏离，可能存在数据造假风险。</span>
        </el-alert>
      </el-card>
    </template>

    <!-- 利润/现金流背离分析区 -->
    <template v-if="activeMethod==='profit_cashflow'">
      <el-row :gutter="16" style="margin-bottom:16px">
        <el-col :span="14">
          <el-card shadow="never">
            <div slot="header" class="card-header">净利润与经营现金流对比（亿元）</div>
            <div ref="pcfChart" style="height:280px"></div>
          </el-card>
        </el-col>
        <el-col :span="10">
          <el-card shadow="never">
            <div slot="header" class="card-header">利润质量系数分析</div>
            <div ref="qualityChart" style="height:280px"></div>
          </el-card>
        </el-col>
      </el-row>
    </template>

    <!-- 异常疑点汇总列表 -->
    <el-card shadow="never">
      <div slot="header" class="card-header">
        <i class="el-icon-warning" style="color:#F5222D;margin-right:6px"></i>
        <span>财务异常疑点汇总（{{ anomalyList.length }} 条）</span>
      </div>
      <el-table :data="anomalyList" size="small" border :row-class-name="anomalyRowClass">
        <el-table-column label="检测编号" prop="detectNo" width="160"/>
        <el-table-column label="企业名称" prop="companyName" min-width="130"/>
        <el-table-column label="异常类型" prop="anomalyType" min-width="180" show-overflow-tooltip/>
        <el-table-column label="检测方法" prop="method" width="130">
          <template slot-scope="{row}">
            <el-tag :type="row.method==='Benford'?'danger':'warning'" size="mini">{{ row.method }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险等级" prop="riskLevel" width="90" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.riskLevel==='HIGH'?'danger':'warning'" size="mini">{{ row.riskLevel==='HIGH'?'高风险':'中风险' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="关键数据" prop="keyData" min-width="160" show-overflow-tooltip/>
        <el-table-column label="状态" prop="status" width="90" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.status==='CONFIRMED'?'danger':row.status==='INVESTIGATING'?'warning':''" size="mini">
              {{ row.status==='CONFIRMED'?'已确认':row.status==='INVESTIGATING'?'调查中':'待核查' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="130" align="center">
          <template slot-scope="{row}">
            <el-button type="text" size="mini" style="color:#F5222D" @click="confirmAnomaly(row)">确认疑点</el-button>
            <el-button type="text" size="mini" @click="ignoreAnomalyItem(row)">忽略</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import { getAnomalyList, ignoreAnomaly, confirmAnomaly, detectBenford, detectProfitCashflow } from '@/api/stateAssets/financialPenetration'

const BENFORD_EXPECTED = [30.1, 17.6, 12.5, 9.7, 7.9, 6.7, 5.8, 5.1, 4.6]

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
  name: 'FinancialAnomalyDetect',
  data() {
    return {
      bannerQuadrants: [],
      detectMethods: [
        { key: 'benford', label: 'Benford定律分析' },
        { key: 'profit_cashflow', label: '利润/现金流背离' },
      ],
      activeMethod: 'benford',
      selectedCompany: '',
      benfordTarget: '',
      benfordCompanies: [],
      benfordScores: [],
      companyList: [],
      anomalyList: [],
      benfordData: {},
      charts: [],
    }
  },
  watch: {
    activeMethod(v) { this.$nextTick(() => { if (v === 'benford') this.initBenfordChart(); else this.initPCFCharts() }) },
  },
  async mounted() {
    await this.loadData()
  },
  beforeDestroy() { this.charts.forEach(c => c && c.dispose()) },
  methods: {
    async loadData(companyId) {
      try {
        const params = { pageNumber: 1, pageSize: 15 }
        if (companyId) params.companyId = companyId
        const res = await getAnomalyList(params)
        if (res.data && res.data.tlist) {
          this.anomalyList = res.data.tlist
        } else {
          this.anomalyList = []
        }
        this.buildBanner()
        // 仅首次加载时构建企业列表（不带筛选时）
        if (!companyId) this.buildCompanyList()
      } catch (e) { console.error('加载异常数据失败', e) }
      // 首次加载时获取Benford数据
      if (!companyId) {
        await this.loadBenfordData('')
      }
      this.$nextTick(() => { if (this.activeMethod === 'benford') this.initBenfordChart(); else this.initPCFCharts() })
    },
    async loadBenfordData(companyId) {
      try {
        const res = await detectBenford(companyId)
        if (res.data) {
          this.benfordData = res.data
          this.benfordTarget = companyId ? (this.companyList.find(c => c.id === companyId) || {}).name || '企业' : '全部企业'
          // digits是对象数组[{digit, expected, actual}]，提取actual值为数字数组
          const digits = res.data.digits || []
          const digitValues = digits.map(d => d.actual != null ? d.actual : d.expected)
          this.benfordCompanies = [{ company: this.benfordTarget, data: digitValues }]
          this.benfordScores = res.data.suspiciousItems || []
        }
      } catch (e) { console.error('加载Benford数据失败', e) }
    },
    buildBanner() {
      const list = this.anomalyList
      const highCount = list.filter(r => r.riskLevel === 'HIGH').length
      const confirmedCount = list.filter(r => r.status === 'CONFIRMED').length
      this.bannerQuadrants = [
        { label: '检测疑点数', value: list.length + '条' },
        { label: '高风险疑点', value: highCount + '条' },
        { label: 'Benford偏离企业', value: list.filter(r => r.method === 'Benford' || r.anomalyType && r.anomalyType.includes('Benford')).length + '家' },
        { label: '已确认疑点', value: confirmedCount + '条' },
      ]
    },
    buildCompanyList() {
      // 使用真实的companyId，去重
      const map = new Map()
      this.anomalyList.forEach(r => {
        if (r.companyId && r.companyName && !map.has(r.companyId)) {
          map.set(r.companyId, r.companyName)
        }
      })
      this.companyList = Array.from(map, ([id, name]) => ({ id, name }))
    },
    async runDetect() {
      // 按选中的公司筛选列表
      await this.loadData(this.selectedCompany)
      if (this.activeMethod === 'benford') {
        await this.loadBenfordData(this.selectedCompany)
        this.$nextTick(() => this.initBenfordChart())
      } else {
        try {
          const res = await detectProfitCashflow(this.selectedCompany)
          if (res.data) this.profitCashflowData = res.data
        } catch {}
        this.$nextTick(() => this.initPCFCharts())
      }
    },
    switchBenford(b) {
      this.benfordTarget = b.company
      this.$nextTick(() => this.initBenfordChart())
    },
    initBenfordChart() {
      if (!this.$refs.benfordChart) return
      const c = echarts.init(this.$refs.benfordChart); this.charts.push(c)
      const target = this.benfordCompanies.find(b => b.company === this.benfordTarget)
      const data = target ? target.data : BENFORD_EXPECTED
      c.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['Benford理论值', `${this.benfordTarget || '企业'}实际值`], top: 0 },
        xAxis: { type: 'category', data: ['1','2','3','4','5','6','7','8','9'] },
        yAxis: { type: 'value', axisLabel: { formatter: '{value}%' } },
        series: [
          { name: 'Benford理论值', type: 'bar', data: BENFORD_EXPECTED, itemStyle: { color: '#1677FF', opacity: 0.7 }, barWidth: 24 },
          { name: `${this.benfordTarget || '企业'}实际值`, type: 'line', data: data, itemStyle: { color: '#F5222D' }, lineStyle: { width: 2 }, symbol: 'circle', symbolSize: 7 },
        ],
      })
    },
    async initPCFCharts() {
      // 尝试获取利润现金流数据
      try {
        const res = await detectProfitCashflow(this.selectedCompany)
        if (res.data) this.profitCashflowData = res.data
      } catch {}
      const pcfData = this.profitCashflowData || {}
      const deviations = pcfData.deviations || []
      if (this.$refs.pcfChart) {
        const c = echarts.init(this.$refs.pcfChart); this.charts.push(c)
        const companies = deviations.map(d => d.companyName)
        c.setOption({
          tooltip: { trigger: 'axis' }, legend: { data: ['净利润', '经营现金流'], top: 0 },
          xAxis: { type: 'category', data: companies.length ? companies : [] },
          yAxis: { type: 'value' },
          series: [
            { name: '净利润', type: 'bar', data: deviations.map(d => d.netProfit || 0), itemStyle: { color: '#52C41A' }, barWidth: 28 },
            { name: '经营现金流', type: 'bar', data: deviations.map(d => d.operatingCashflow || 0), itemStyle: { color: '#1677FF' }, barWidth: 28 },
          ],
        })
      }
      if (this.$refs.qualityChart) {
        const c = echarts.init(this.$refs.qualityChart); this.charts.push(c)
        const companies = deviations.map(d => d.companyName)
        const qualities = deviations.map(d => d.profitQuality || d.profitCashRatio || 0)
        c.setOption({
          tooltip: { trigger: 'axis', formatter: '{b}: {c}' },
          xAxis: { type: 'category', data: companies.length ? companies : [] },
          yAxis: { type: 'value', name: '利润质量系数' },
          series: [{
            type: 'bar', barWidth: 40,
            data: qualities.map(v => ({ value: v, itemStyle: { color: v < 0.5 ? '#F5222D' : v < 0.8 ? '#FA8C16' : '#52C41A' } })),
            label: { show: true, position: 'top' },
            markLine: { data: [{ yAxis: 1, lineStyle: { color: '#1677FF', type: 'dashed' } }] },
          }],
        })
      }
    },
    anomalyRowClass({ row }) {
      if (row.riskLevel === 'HIGH') return 'row-danger'
      return 'row-warning'
    },
    confirmAnomaly(row) {
      this.$confirm(`确认将疑点【${row.detectNo}】标记为已确认？确认后将推送至风险预警模块。`, '确认疑点', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(async () => {
        try {
          await confirmAnomaly(row.detectNo)
          row.status = 'CONFIRMED'
          this.buildBanner()
          this.$message.success('已确认疑点并推送至风险预警模块')
        } catch (e) {
          this.$message.error('确认疑点失败，请重试')
        }
      }).catch(() => {})
    },
    ignoreAnomalyItem(row) {
      this.$confirm(`确认忽略疑点【${row.detectNo}】？忽略后该条记录将不再显示。`, '忽略疑点', {
        confirmButtonText: '确认忽略',
        cancelButtonText: '取消',
        type: 'info',
      }).then(async () => {
        try {
          await ignoreAnomaly(row.detectNo)
          const idx = this.anomalyList.indexOf(row)
          if (idx > -1) this.anomalyList.splice(idx, 1)
          this.buildBanner()
          this.$message.success('已忽略该疑点')
        } catch (e) {
          this.$message.error('忽略操作失败，请重试')
        }
      }).catch(() => {})
    },
  },
}
</script>

<style scoped lang="scss">
.anomaly-detect-wrap { padding: 16px; background: #f5f7fa; min-height: 100vh; }
.page-banner {
  border-radius: 8px; padding: 20px 28px; margin-bottom: 14px;
  display: flex; justify-content: space-between; align-items: center;
  .banner-title { font-size: 22px; font-weight: 700; color: #fff; margin-bottom: 6px; }
  .banner-sub { font-size: 13px; color: rgba(255,255,255,0.78); max-width: 520px; }
  .banner-right { display: flex; gap: 18px; }
  .banner-quad { text-align: center; background: rgba(255,255,255,0.12); border-radius: 8px; padding: 10px 14px; }
  .bq-value { font-size: 20px; font-weight: 700; color: #fff; }
  .bq-label { font-size: 11px; color: rgba(255,255,255,0.7); margin-top: 2px; }
}
.card-header { font-weight: 600; color: #333; display: flex; align-items: center; gap: 8px; }
.benford-item { margin-bottom: 4px; }
::v-deep .row-danger td { background: #FFF1F0 !important; }
::v-deep .row-warning td { background: #FFFBE6 !important; }
</style>
