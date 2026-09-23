<template>
  <div class="app-container industry-page" :style="themeVars">
    <div class="page-header" :style="{ background: `linear-gradient(135deg, ${ipPrimary} 0%, ${ipSecondary} 60%, ${ipBright} 100%)` }">
      <div class="page-header-left"><i class="el-icon-service"></i><span>公共服务监管</span></div>
      <div class="page-header-desc">公益属性与盈利平衡 · 政策补贴依赖度 · 社会责任评分 · 可持续经营监控</div>
    </div>

    <el-row :gutter="16" style="margin-bottom:14px">
      <el-col :span="6" v-for="k in kpiCards" :key="k.label">
        <el-card shadow="hover" :body-style="{ padding: '18px 20px' }">
          <div class="kpi-card">
            <div class="kpi-icon-wrap" :style="{ background: k.bgColor }">
              <i :class="k.icon" :style="{ color: k.color, fontSize: '26px' }"></i>
            </div>
            <div class="kpi-info">
              <div class="kpi-value" :style="{ color: k.valColor || '#303133' }">{{ k.value }}</div>
              <div class="kpi-label">{{ k.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="14" style="margin-bottom:14px">
      <el-col :span="14">
        <el-card shadow="never" :body-style="{ padding: '16px' }">
          <div slot="header" class="card-header-title"><i class="el-icon-pie-chart" :style="{ color: ipSecondary }"></i> 公共服务细分行业营收分布</div>
          <div ref="psDistChart" style="height:260px"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never" :body-style="{ padding: '16px' }">
          <div slot="header" class="card-header-title"><i class="el-icon-data-analysis" :style="{ color: ipSecondary }"></i> 盈亏与补贴结构分析</div>
          <div ref="profitStructChart" style="height:260px"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never">
      <div slot="header" class="card-header-title">公共服务企业明细列表</div>
      <el-table :data="list" border size="small" v-loading="loading"
        :header-cell-style="{ background: ipLightBg, color: ipPrimary }" style="width:100%"
        :row-class-name="tableRowClass">
        <el-table-column label="序号" type="index" width="55" align="center" />
        <el-table-column label="企业名称" prop="companyName" min-width="160" show-overflow-tooltip />
        <el-table-column label="服务类型" prop="serviceType" width="100" align="center">
          <template slot-scope="s"><el-tag type="primary" size="small" :style="{ background: ipLightBg, color: ipSecondary, borderColor: ipBright }">{{ s.row.serviceType }}</el-tag></template>
        </el-table-column>
        <el-table-column label="年营收(亿元)" prop="revenue" width="120" align="right">
          <template slot-scope="s"><span style="font-weight:600">{{ s.row.revenue }}</span></template>
        </el-table-column>
        <el-table-column label="盈利状态" prop="profitStatus" width="100" align="center">
          <template slot-scope="s">
            <el-tag :type="{ PROFIT: 'success', SLIGHT_PROFIT: 'primary', LOSS: 'danger' }[s.row.profitStatus]" size="small">
              {{ { PROFIT: '盈利', SLIGHT_PROFIT: '微利', LOSS: '亏损' }[s.row.profitStatus] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="政策补贴比例%" prop="subsidyRatio" width="130" align="center">
          <template slot-scope="s">
            <span :style="{ color: s.row.subsidyRatio > 30 ? '#FA8C16' : '#303133', fontWeight: 600 }">
              {{ s.row.subsidyRatio }}<i v-if="s.row.subsidyRatio > 30" class="el-icon-warning-outline" style="color:#FA8C16;margin-left:3px"></i>
            </span>
          </template>
        </el-table-column>
        <el-table-column label="连续亏损年数" prop="lossYears" width="120" align="center">
          <template slot-scope="s">
            <span :style="{ color: s.row.lossYears >= 2 ? '#F5222D' : '#303133', fontWeight: 600 }">{{ s.row.lossYears }}年</span>
          </template>
        </el-table-column>
        <el-table-column label="社会责任评分" prop="socialScore" width="120" align="center">
          <template slot-scope="s">
            <el-rate :value="s.row.socialScore / 20" disabled show-score :text-color="ipSecondary" style="display:inline-flex;vertical-align:middle" />
          </template>
        </el-table-column>
        <el-table-column label="风险等级" prop="riskLevel" width="85" align="center">
          <template slot-scope="s">
            <el-tag :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }[s.row.riskLevel]" size="small">{{ { HIGH: '高', MEDIUM: '中', LOW: '低' }[s.row.riskLevel] }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getPublicServiceList, getPublicServiceStats } from '@/api/stateAssets/industryPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'IndustryPublicService',
  mixins: [investThemeMixin],
  computed: {
    kpiCards() {
      const d = this.list
      const avgSubsidy = d.length ? (d.reduce((s, r) => s + r.subsidyRatio, 0) / d.length).toFixed(1) : 0
      return [
        { label: '公共服务企业数', value: d.length + '家', icon: 'el-icon-service', bgColor: this.ipLightBg, color: this.ipSecondary },
        { label: '年总营收', value: d.reduce((s, r) => s + r.revenue, 0).toFixed(1) + '亿', icon: 'el-icon-money', bgColor: this.ipLightBg, color: this.ipSecondary },
        { label: '政策补贴平均依赖度', value: avgSubsidy + '%', icon: 'el-icon-s-check', bgColor: avgSubsidy > 30 ? '#FFF1F0' : '#FFF7E6', color: avgSubsidy > 30 ? '#F5222D' : '#FA8C16', valColor: avgSubsidy > 30 ? '#F5222D' : '#FA8C16' },
        { label: '连续亏损企业', value: d.filter(r => r.lossYears >= 2).length + '家', icon: 'el-icon-warning', bgColor: '#FFF1F0', color: '#F5222D', valColor: '#F5222D' },
      ]
    },
  },
  data() {
    return { loading: false, list: [] }
  },
  created() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getPublicServiceList({})
        this.list = (res && res.result === 200 && res.data && (res.data.tlist || res.data.list)) ? (res.data.tlist || res.data.list) : []
      } catch (e) { this.list = [] } finally {
        this.loading = false
        this.$nextTick(() => { this.initDist(); this.initProfitStruct() })
      }
    },
    tableRowClass({ row }) {
      if (row.profitStatus === 'LOSS' && row.lossYears >= 2) return 'row-loss-risk'
      return ''
    },
    initDist() {
      const c = echarts.init(this.$refs.psDistChart)
      c.setOption({
        tooltip: { trigger: 'axis' },
        grid: { top: 20, bottom: 30, left: 60, right: 20 },
        xAxis: { type: 'category', data: this.list.map(r => r.serviceType) },
        yAxis: { type: 'value', name: '亿元', splitLine: { lineStyle: { color: '#f0f2f5' } } },
        series: [{
          type: 'bar', barMaxWidth: 50, label: { show: true, position: 'top', formatter: '{c}亿' },
          data: this.list.map((r, i) => ({
            value: r.revenue,
            itemStyle: { color: [this.ipSecondary, '#9254de', this.ipBright, '#d3adf7', '#efdbff'][i % 5] },
          })),
        }],
      })
    },
    initProfitStruct() {
      const c = echarts.init(this.$refs.profitStructChart)
      const profit = this.list.filter(r => r.profitStatus !== 'LOSS').reduce((s, r) => s + r.revenue, 0)
      const loss = this.list.filter(r => r.profitStatus === 'LOSS').reduce((s, r) => s + r.revenue, 0)
      const subsidyTotal = this.list.reduce((s, r) => s + r.revenue * r.subsidyRatio / 100, 0)
      c.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c}亿 ({d}%)' },
        legend: { orient: 'vertical', right: 8, top: 'center' },
        series: [{
          type: 'pie', radius: ['35%', '65%'], center: ['40%', '50%'],
          data: [
            { value: profit.toFixed(1), name: '盈利/微利营收', itemStyle: { color: this.ipSecondary } },
            { value: loss.toFixed(1), name: '亏损企业营收', itemStyle: { color: '#F5222D' } },
            { value: subsidyTotal.toFixed(1), name: '政策补贴总额', itemStyle: { color: '#FA8C16' } },
          ],
          label: { show: false },
        }],
      })
    },
  },
}
</script>

<style lang="scss" scoped>
.industry-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header {
  display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px;
  border-radius: 6px; color: #fff;
}
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600;
  i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
.kpi-card { display: flex; align-items: center; }
.kpi-icon-wrap { width: 50px; height: 50px; border-radius: 10px; display: flex; align-items: center; justify-content: center; margin-right: 14px; flex-shrink: 0; }
.kpi-info .kpi-value { font-size: 28px; font-weight: bold; color: #303133; line-height: 1; }
.kpi-info .kpi-label { font-size: 13px; color: #909399; margin-top: 5px; }
.card-header-title { font-size: 14px; font-weight: 600; color: #303133; }
::v-deep .el-card { border-radius: 6px; }
::v-deep .row-loss-risk td { background: #FFF1F0 !important; }
::v-deep .el-rate__icon { font-size: 14px !important; }
</style>
