<template>
  <div class="related-party-wrap">
    <!-- Banner -->
    <div class="page-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-left">
        <div class="banner-title">关联交易专项监控</div>
        <div class="banner-sub">穿透企业间关联交易网络，核查定价公允性，防范关联交易利益输送风险</div>
      </div>
      <div class="banner-right">
        <div v-for="q in bannerQuadrants" :key="q.label" class="banner-quad">
          <div class="bq-value">{{ q.value }}</div>
          <div class="bq-label">{{ q.label }}</div>
        </div>
      </div>
    </div>

    <!-- 图表区：关联交易网络 + 交易类型分布 -->
    <el-row :gutter="16" style="margin-bottom:16px">
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header" class="card-header">关联交易网络图谱（点击节点查看交易明细）</div>
          <div ref="networkChart" style="height:340px"></div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header" class="card-header">关联交易类型分布（金额，万元）</div>
          <div ref="typeChart" style="height:340px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 关联交易台账 -->
    <el-card shadow="never">
      <div slot="header" class="card-header">
        <span>关联交易台账</span>
        <el-tag type="danger" size="small" style="margin-left:10px">{{ abnormalCount }} 笔定价偏离</el-tag>
      </div>
      <el-table :data="tradeList" size="small" border :row-class-name="rowClass">
        <el-table-column label="交易编号" prop="partyId" width="80" />
        <el-table-column label="企业" prop="companyName" min-width="100" show-overflow-tooltip />
        <el-table-column label="关联方" prop="relatedParty" min-width="160" show-overflow-tooltip />
        <el-table-column label="关联关系" prop="relationType" width="80" align="center">
          <template slot-scope="{row}">{{ relationTypeLabel(row.relationType) }}</template>
        </el-table-column>
        <el-table-column label="交易名称" prop="transactionName" min-width="180" show-overflow-tooltip />
        <el-table-column label="交易类型" prop="transactionType" width="100" align="center">
          <template slot-scope="{row}">
            <el-tag :style="tradeTypeStyle(row.transactionType)" size="mini">{{ transactionTypeLabel(row.transactionType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="交易金额(万元)" prop="transactionAmount" width="130" align="right">
          <template slot-scope="{row}"><span style="font-weight:700">{{ formatNum(row.transactionAmount) }}</span></template>
        </el-table-column>
        <el-table-column label="重大交易" prop="isMajor" width="80" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.isMajor==='1'?'danger':'info'" size="mini">{{ row.isMajor==='1'?'是':'否' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险等级" prop="risk" width="80" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.risk==='高'?'danger':row.risk==='中'?'warning':'info'" size="mini">{{ row.risk }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="交易占比(%)" prop="ratio" width="90" align="center" />
        <el-table-column label="报告期" prop="period" width="80" align="center" />
        <el-table-column label="操作" width="90" align="center">
          <template slot-scope="{row}">
            <el-button type="text" size="mini" @click="openDetail(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 详情抽屉 -->
    <el-drawer :title="`交易详情：${activeRow.partyId||''}`" :visible.sync="detailVisible" size="560px" direction="rtl">
      <div v-loading="detailLoading" style="padding:20px">
        <template v-if="detailData.partyId">
          <el-descriptions :column="2" border size="small" title="基本信息">
            <el-descriptions-item label="交易编号">{{ detailData.partyId }}</el-descriptions-item>
            <el-descriptions-item label="报告期">{{ detailData.period }}</el-descriptions-item>
            <el-descriptions-item label="企业">{{ detailData.companyName }}</el-descriptions-item>
            <el-descriptions-item label="关联方">{{ detailData.relatedParty }}</el-descriptions-item>
            <el-descriptions-item label="关联关系">{{ relationTypeLabel(detailData.relationType) }}</el-descriptions-item>
            <el-descriptions-item label="交易类型">{{ transactionTypeLabel(detailData.transactionType) }}</el-descriptions-item>
            <el-descriptions-item label="交易名称" :span="2">{{ detailData.transactionName }}</el-descriptions-item>
            <el-descriptions-item label="交易金额(万元)">{{ formatNum(detailData.transactionAmount) }}</el-descriptions-item>
            <el-descriptions-item label="余额(万元)">{{ formatNum(detailData.balanceAmount) }}</el-descriptions-item>
            <el-descriptions-item label="交易占比">{{ detailData.ratio }}%</el-descriptions-item>
            <el-descriptions-item label="重大交易">
              <el-tag :type="detailData.isMajor==='1'?'danger':'info'" size="mini">{{ detailData.isMajor==='1'?'是':'否' }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="风险等级">
              <el-tag :type="detailData.risk==='高'?'danger':detailData.risk==='中'?'warning':'info'" size="mini">{{ detailData.risk }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
          </el-descriptions>

          <!-- 定价分析 -->
          <el-divider>定价分析</el-divider>
          <el-descriptions v-if="detailData.pricingAnalysis" :column="2" border size="small">
            <el-descriptions-item label="交易价格(万元)">{{ formatNum(detailData.pricingAnalysis.transactionPrice) }}</el-descriptions-item>
            <el-descriptions-item label="市场参考价(万元)">{{ formatNum(detailData.pricingAnalysis.marketReferencePrice) }}</el-descriptions-item>
            <el-descriptions-item label="价格偏离度">
              <span :style="{color: detailData.pricingAnalysis.deviationLevel==='异常'?'#F5222D':detailData.pricingAnalysis.deviationLevel==='关注'?'#FA8C16':'#52C41A', fontWeight:700}">
                {{ detailData.pricingAnalysis.priceDeviation }}%
              </span>
            </el-descriptions-item>
            <el-descriptions-item label="偏离等级">
              <el-tag :type="detailData.pricingAnalysis.deviationLevel==='异常'?'danger':detailData.pricingAnalysis.deviationLevel==='关注'?'warning':'success'" size="mini">
                {{ detailData.pricingAnalysis.deviationLevel }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>

          <!-- 公允性评估 -->
          <el-divider>公允性评估</el-divider>
          <div v-if="detailData.fairnessAssessment" class="fairness-section">
            <el-progress :percentage="detailData.fairnessAssessment.score" :color="fairnessColor" :stroke-width="18" :text-inside="true" />
            <div style="margin-top:10px">
              <el-tag :type="detailData.fairnessAssessment.level==='公允'?'success':detailData.fairnessAssessment.level==='基本公允'?'warning':'danger'" size="small">
                {{ detailData.fairnessAssessment.level }}
              </el-tag>
              <span style="margin-left:10px;color:#666;font-size:13px">{{ detailData.fairnessAssessment.suggestion }}</span>
            </div>
          </div>

          <!-- 风险提示 -->
          <el-divider>风险提示</el-divider>
          <el-alert v-if="detailData.risk==='高'" type="error"
            :title="'高风险关联交易：金额'+formatNum(detailData.transactionAmount)+'万元，需重点关注定价公允性'"
            show-icon :closable="false"/>
          <el-alert v-else-if="detailData.risk==='中'" type="warning"
            title="中等风险关联交易，建议持续关注交易频率和金额变化"
            show-icon :closable="false"/>
          <el-alert v-else type="success" title="低风险关联交易，暂未发现异常" show-icon :closable="false"/>

          <!-- 历史交易记录 -->
          <el-divider>同一关联方历史交易</el-divider>
          <el-table v-if="detailData.historyRecords && detailData.historyRecords.length" :data="detailData.historyRecords" size="mini" border>
            <el-table-column label="交易名称" prop="transactionName" min-width="120" show-overflow-tooltip />
            <el-table-column label="类型" width="80" align="center">
              <template slot-scope="{row}">{{ transactionTypeLabel(row.transactionType) }}</template>
            </el-table-column>
            <el-table-column label="金额(万元)" prop="transactionAmount" width="100" align="right">
              <template slot-scope="{row}"><span>{{ formatNum(row.transactionAmount) }}</span></template>
            </el-table-column>
            <el-table-column label="报告期" prop="period" width="80" align="center" />
          </el-table>
          <el-empty v-else description="暂无历史交易记录" :image-size="60" />
        </template>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import { getRelatedPartyList, getRelatedPartyNetwork, getRelatedPartyDetail } from '@/api/stateAssets/financialPenetration'

export default {
  name: 'FinancialRelatedParty',
  data() {
    return {
      bannerQuadrants: [],
      tradeList: [],
      detailVisible: false,
      detailLoading: false,
      activeRow: {},
      detailData: {},
      charts: [],
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
    abnormalCount() { return this.tradeList.filter(r => r.risk === '高').length },
    fairnessColor() {
      if (!this.detailData.fairnessAssessment) return '#1677FF'
      const score = this.detailData.fairnessAssessment.score
      if (score >= 80) return '#52C41A'
      if (score >= 60) return '#FA8C16'
      return '#F5222D'
    },
  },
  async mounted() {
    await this.loadData()
  },
  beforeDestroy() { this.charts.forEach(c => c && c.dispose()) },
  methods: {
    async loadData() {
      try {
        const [listRes, netRes] = await Promise.all([
          getRelatedPartyList({ pageNumber: 1, pageSize: 15 }).catch(() => ({})),
          getRelatedPartyNetwork().catch(() => ({}))
        ])
        if (listRes.data && listRes.data.tlist && listRes.data.tlist.length) {
          this.tradeList = listRes.data.tlist
        }
        this.buildBanner()
        this.$nextTick(() => { this.initNetworkChart(netRes.data); this.initTypeChart() })
      } catch (e) { console.error('加载关联交易数据失败', e) }
    },
    buildBanner() {
      const list = this.tradeList
      const totalAmount = list.reduce((a, r) => a + (r.transactionAmount || 0), 0)
      const majorCount = list.filter(r => r.isMajor === '1').length
      const highRiskCount = list.filter(r => r.risk === '高').length
      this.bannerQuadrants = [
        { label: '关联交易总笔数', value: list.length + '笔' },
        { label: '交易总金额', value: (totalAmount / 10000).toFixed(2) + '亿' },
        { label: '重大交易笔数', value: majorCount + '笔' },
        { label: '高风险笔数', value: highRiskCount + '笔' },
      ]
    },
    initNetworkChart(netData) {
      if (!this.$refs.networkChart) return
      const c = echarts.init(this.$refs.networkChart); this.charts.push(c)
      const nodes = (netData && netData.nodes && netData.nodes.length) ? netData.nodes.map(n => ({
        id: n.id || n.companyId, name: n.name || n.companyName, symbolSize: n.symbolSize || 50,
        category: n.category || 0, value: n.value || 0
      })) : []
      const edges = (netData && netData.links && netData.links.length) ? netData.links.map(l => ({
        source: l.source, target: l.target,
        lineStyle: { color: Math.abs(l.priceDeviation || 0) > 15 ? '#F5222D' : Math.abs(l.priceDeviation || 0) > 8 ? '#FA8C16' : '#52C41A', width: Math.abs(l.priceDeviation || 0) > 15 ? 3 : 2 },
        label: l.label || { show: false }
      })) : []
      if (!nodes.length) {
        // 从tradeList构建网络图
        const companySet = new Set()
        this.tradeList.forEach(r => { if (r.companyName) companySet.add(r.companyName); if (r.relatedParty) companySet.add(r.relatedParty) })
        const companies = [...companySet]
        nodes.push(...companies.map((name, i) => ({ id: 'N' + i, name, symbolSize: 50, category: 0, value: 0 })))
        this.tradeList.forEach(r => {
          const srcIdx = companies.indexOf(r.companyName)
          const tgtIdx = companies.indexOf(r.relatedParty)
          if (srcIdx >= 0 && tgtIdx >= 0) {
            edges.push({ source: 'N' + srcIdx, target: 'N' + tgtIdx,
              lineStyle: { color: r.risk === '高' ? '#F5222D' : r.risk === '中' ? '#FA8C16' : '#52C41A', width: r.risk === '高' ? 3 : 1.5 },
              label: { show: r.risk === '高', formatter: this.transactionTypeLabel(r.transactionType) }
            })
          }
        })
      }
      c.setOption({
        tooltip: { formatter: d => d.dataType === 'node' ? d.name : `${d.data.source}→${d.data.target}` },
        legend: [{ data: ['正常企业', '高风险企业', '重点关注'], top: 0 }],
        series: [{
          type: 'graph', layout: 'force', data: nodes, links: edges,
          categories: [{ name: '正常企业', itemStyle: { color: '#1677FF' } }, { name: '高风险企业', itemStyle: { color: '#F5222D' } }, { name: '重点关注', itemStyle: { color: '#FA8C16' } }],
          force: { repulsion: 280, edgeLength: 120 },
          roam: true, label: { show: true, position: 'bottom', fontSize: 12 },
          edgeSymbol: ['circle', 'arrow'], edgeSymbolSize: [4, 8],
          lineStyle: { opacity: 0.7 },
        }],
      })
    },
    initTypeChart() {
      if (!this.$refs.typeChart) return
      const c = echarts.init(this.$refs.typeChart); this.charts.push(c)
      // 从tradeList构建交易类型分布
      const typeMap = {}
      this.tradeList.forEach(r => {
        const type = this.transactionTypeLabel(r.transactionType)
        typeMap[type] = (typeMap[type] || 0) + (r.transactionAmount || 0)
      })
      const pieData = Object.keys(typeMap).map(name => ({ name, value: typeMap[name] }))
      c.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c}万元 ({d}%)' },
        legend: { type: 'scroll', bottom: 0 },
        series: [{
          type: 'pie', radius: ['40%', '65%'], center: ['50%', '45%'],
          data: pieData.length ? pieData : [],
          label: { formatter: '{b}\n{d}%' },
        }],
      })
    },
    tradeTypeStyle(type) {
      const map = {
        'INFLOW': { background: '#F6FFED', color: '#52C41A', border: '1px solid #B7EB8F' },
        'OUTFLOW': { background: '#FFF1F0', color: '#F5222D', border: '1px solid #FFA39E' },
        'INTERNAL_TRANSFER': { background: '#FFF7E6', color: '#FA8C16', border: '1px solid #FFD591' },
        'INVESTMENT_RECOVERY': { background: '#E6F4FF', color: '#1677FF', border: '1px solid #91CAFF' },
        '资金流入': { background: '#F6FFED', color: '#52C41A', border: '1px solid #B7EB8F' },
        '资金流出': { background: '#FFF1F0', color: '#F5222D', border: '1px solid #FFA39E' },
        '内部转移': { background: '#FFF7E6', color: '#FA8C16', border: '1px solid #FFD591' },
        '投资收回': { background: '#E6F4FF', color: '#1677FF', border: '1px solid #91CAFF' },
        '资金拆借': { background: '#FFF1F0', color: '#F5222D', border: '1px solid #FFA39E' },
        '担保借款': { background: '#FFF7E6', color: '#FA8C16', border: '1px solid #FFD591' },
        '销售产品': { background: '#F6FFED', color: '#52C41A', border: '1px solid #B7EB8F' },
        '采购货物': { background: '#E6F4FF', color: '#1677FF', border: '1px solid #91CAFF' },
        '采购服务': { background: '#E6F4FF', color: '#1677FF', border: '1px solid #91CAFF' },
        '采购原材料': { background: '#E6F4FF', color: '#1677FF', border: '1px solid #91CAFF' },
        '技术服务': { background: '#F9F0FF', color: '#722ED1', border: '1px solid #D3ADF7' },
        '提供劳务': { background: '#F9F0FF', color: '#722ED1', border: '1px solid #D3ADF7' },
      }
      return map[type] || {}
    },
    transactionTypeLabel(type) {
      const map = {
        'INFLOW': '资金流入',
        'OUTFLOW': '资金流出',
        'INTERNAL_TRANSFER': '内部转移',
        'INVESTMENT_RECOVERY': '投资收回',
      }
      return map[type] || type || '-'
    },
    relationTypeLabel(type) {
      const map = {
        'OPERATING': '经营关联',
        'INVESTING': '投资关联',
        'FINANCING': '融资关联',
      }
      return map[type] || type || '-'
    },
    rowClass({ row }) {
      if (row.risk === '高') return 'row-danger'
      if (row.risk === '中') return 'row-warning'
      return ''
    },
    openDetail(row) {
      this.activeRow = row
      this.detailVisible = true
      this.detailLoading = true
      this.detailData = {}
      getRelatedPartyDetail(row.partyId).then(res => {
        if (res.data) {
          this.detailData = res.data
        } else {
          this.$message.error(res.msg || '获取详情失败')
        }
      }).catch(() => {
        this.$message.error('获取详情失败')
      }).finally(() => {
        this.detailLoading = false
      })
    },
    formatNum(v) {
      if (v == null) return '-'
      return Number(v).toLocaleString('zh-CN', { maximumFractionDigits: 2 })
    },
  },
}
</script>

<style scoped lang="scss">
.related-party-wrap { padding: 16px; background: #f5f7fa; min-height: 100vh; }
.page-banner {
  border-radius: 8px; padding: 20px 28px; margin-bottom: 14px;
  display: flex; justify-content: space-between; align-items: center;
  .banner-title { font-size: 22px; font-weight: 700; color: #fff; margin-bottom: 6px; }
  .banner-sub { font-size: 13px; color: rgba(255,255,255,0.78); }
  .banner-right { display: flex; gap: 18px; }
  .banner-quad { text-align: center; background: rgba(255,255,255,0.12); border-radius: 8px; padding: 10px 14px; }
  .bq-value { font-size: 20px; font-weight: 700; color: #fff; }
  .bq-label { font-size: 11px; color: rgba(255,255,255,0.7); margin-top: 2px; }
}
.card-header { font-weight: 600; color: #333; display: flex; align-items: center; gap: 8px; }
.fairness-section { padding: 8px 0; }
::v-deep .row-danger td { background: #FFF1F0 !important; }
::v-deep .row-warning td { background: #FFFBE6 !important; }
</style>
