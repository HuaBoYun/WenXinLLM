<template>
  <div class="dispute-wrap">
    <!-- Banner -->
    <div class="page-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-left">
        <div class="banner-title">合同纠纷与诉讼管理</div>
        <div class="banner-sub">专项追踪合同纠纷诉讼进展，以时间轴形式呈现立案→审理→判决→执行各阶段状态</div>
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

    <!-- 图表区 -->
    <el-row :gutter="16" style="margin-bottom:16px">
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header" class="card-header"><span>纠纷类型分布</span></div>
          <div ref="typeChart" style="height:220px"></div>
        </el-card>
      </el-col>
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header" class="card-header"><span>涉诉金额趋势（万元/季度）</span></div>
          <div ref="amtChart" style="height:220px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 纠纷诉讼台账 -->
    <el-card shadow="never">
      <div slot="header" class="card-header">
        <span>纠纷诉讼台账</span>
        <el-tag size="small" type="danger" style="margin-left:8px">{{ list.length }} 件</el-tag>
      </div>
      <el-table :data="list" border size="small" style="width:100%">
        <el-table-column label="案件编号" prop="caseNo" width="150" fixed/>
        <el-table-column label="合同名称" prop="contractName" min-width="180" show-overflow-tooltip/>
        <el-table-column label="企业" prop="companyName" width="140" show-overflow-tooltip/>
        <el-table-column label="对方单位" prop="counterparty" width="150" show-overflow-tooltip/>
        <el-table-column label="纠纷类型" prop="disputeType" width="110">
          <template slot-scope="{row}">
            <span class="tag-pill" :style="disputeTagStyle(row.disputeType)">{{ row.disputeType }}</span>
          </template>
        </el-table-column>
        <el-table-column label="涉及金额(万)" prop="amount" width="120" align="right">
          <template slot-scope="{row}"><span style="font-weight:600;color:#873800">{{ row.amount.toLocaleString() }}</span></template>
        </el-table-column>
        <el-table-column label="案件状态" prop="status" width="100" align="center">
          <template slot-scope="{row}">
            <el-tag :type="caseStatusType(row.status)" size="mini">{{ caseStatusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="开案日期" prop="startDate" width="110"/>
        <el-table-column label="案情摘要" prop="description" min-width="180" show-overflow-tooltip/>
        <el-table-column label="操作" width="100" fixed="right">
          <template slot-scope="{row}">
            <el-button type="text" size="mini" icon="el-icon-view" @click="showDetail(row)">诉讼详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 案件详情抽屉 -->
    <el-drawer title="案件诉讼详情" :visible.sync="detailVisible" size="560px" append-to-body>
      <div v-loading="detailLoading" v-if="currentCase" style="padding:20px">
        <el-descriptions :column="2" size="small" border style="margin-bottom:24px">
          <el-descriptions-item label="案件编号">{{ currentCase.caseNo }}</el-descriptions-item>
          <el-descriptions-item label="案件状态">
            <el-tag :type="caseStatusType(currentCase.status)" size="mini">{{ caseStatusLabel(currentCase.status) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="合同名称" :span="2">{{ currentCase.contractName }}</el-descriptions-item>
          <el-descriptions-item label="企业">{{ currentCase.companyName }}</el-descriptions-item>
          <el-descriptions-item label="对方单位">{{ currentCase.counterparty }}</el-descriptions-item>
          <el-descriptions-item label="纠纷类型">
            <span class="tag-pill" :style="disputeTagStyle(currentCase.disputeType)">{{ currentCase.disputeType }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="涉及金额">
            <span style="font-weight:700;color:#873800">{{ currentCase.amount.toLocaleString() }} 万元</span>
          </el-descriptions-item>
          <el-descriptions-item label="开案日期">{{ currentCase.startDate }}</el-descriptions-item>
          <el-descriptions-item label="审理法院" v-if="currentCase.courtName">{{ currentCase.courtName }}</el-descriptions-item>
          <el-descriptions-item label="代理律师" v-if="currentCase.lawyerName">{{ currentCase.lawyerName }}</el-descriptions-item>
          <el-descriptions-item label="和解金额" v-if="currentCase.settlementAmount">
            <span style="color:#52C41A;font-weight:600">{{ currentCase.settlementAmount.toLocaleString() }} 万元</span>
          </el-descriptions-item>
          <el-descriptions-item label="下次开庭" v-if="currentCase.nextHearingDate">
            <span style="color:#F5222D">{{ currentCase.nextHearingDate }}</span>
          </el-descriptions-item>
        </el-descriptions>

        <div style="font-weight:600;color:#333;margin-bottom:8px">案情摘要</div>
        <el-alert :title="currentCase.description || '暂无'" type="warning" :closable="false" style="margin-bottom:20px"/>

        <div v-if="currentCase.judgmentResult" style="margin-bottom:20px">
          <div style="font-weight:600;color:#333;margin-bottom:8px">裁判/和解结果</div>
          <el-alert :title="currentCase.judgmentResult" type="success" :closable="false"/>
        </div>

        <div style="font-weight:600;color:#333;margin-bottom:12px">诉讼进度时间轴</div>
        <el-timeline>
          <el-timeline-item
            v-for="(node, i) in currentCase.timeline"
            :key="i"
            :type="node.type"
            :timestamp="node.time"
            placement="top">
            <el-card shadow="hover" class="timeline-card">
              <div class="timeline-title">{{ node.step }}</div>
              <div class="timeline-content">{{ node.content }}</div>
            </el-card>
          </el-timeline-item>
        </el-timeline>

        <!-- 跳转档案管理 -->
        <div style="margin-top:24px;text-align:center;border-top:1px solid #f0f0f0;padding-top:16px">
          <el-button type="primary" size="small" icon="el-icon-s-order" @click="goToArchiveManagement">
            前往档案管理
          </el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getDisputeList, getDisputeDetail } from '@/api/stateAssets/contractPenetration'
import { mapGetters } from 'vuex'

export default {
  name: 'ContractDispute',
  data() {
    return {
      list: [],
      detailVisible: false,
      detailLoading: false,
      currentCase: null,
      typeChart: null,
      amtChart: null,
      filterCounterparty: '',
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
    statCards() {
      const d = this.list
      const totalAmt = d.reduce((s, r) => s + r.amount, 0)
      const ongoing = d.filter(r => ['LITIGATION', 'TRIAL', 'EXECUTION', 'MEDIATION', 'NEGOTIATION', 'APPEAL'].includes(r.status)).length
      const closed = d.filter(r => r.status === 'CLOSED').length
      return [
        { label: '纠纷案件', value: d.length, color: '#873800', icon: 'el-icon-document-remove' },
        { label: '诉讼中', value: ongoing, color: '#F5222D', icon: 'el-icon-warning' },
        { label: '涉诉金额(万)', value: totalAmt.toLocaleString(), color: '#d46b08', icon: 'el-icon-money' },
        { label: '已结案', value: closed, color: '#52C41A', icon: 'el-icon-circle-check' },
      ]
    },
    bannerStats() {
      const d = this.list
      const winCount = d.filter(r => r.status === 'EXECUTION').length
      const rate = d.length ? Math.round(winCount / d.length * 100) : 0
      return [
        { label: '涉案总数', value: d.length },
        { label: '胜诉/执行', value: winCount },
        { label: '胜诉率', value: rate + '%' },
      ]
    },
  },
  mounted() {
    // 支持从其他页面跳转时带入 counterpartyName 查询参数
    if (this.$route.query.counterpartyName) {
      this.filterCounterparty = this.$route.query.counterpartyName
    }
    this.fetchData()
    this.$nextTick(() => { this.initTypeChart(); this.initAmtChart() })
  },
  beforeDestroy() {
    if (this.typeChart) this.typeChart.dispose()
    if (this.amtChart) this.amtChart.dispose()
  },
  methods: {
    async fetchData() {
      try {
        const params = { pageNumber: 1, pageSize: 200 }
        if (this.filterCounterparty) {
          params.companyName = this.filterCounterparty
        }
        const res = await getDisputeList(params)
        if (res && res.result === 200 && res.data) {
          // 字段映射：接口返回字段 -> 前端使用字段
          this.list = (res.data.tlist || []).map(d => ({
            caseNo: d.disputeId,
            contractName: d.contractId ? `合同-${d.contractId}` : '无关联合同',
            companyName: d.companyId || '',
            counterparty: d.counterpartyName || '',
            disputeType: d.disputeType || '',
            amount: Number(d.disputeAmount) || 0,
            status: d.caseStatus || '',
            startDate: d.filingDate || '',
            description: d.disputeReason || '',
            judgmentResult: d.judgmentResult || '',
            settlementAmount: d.settlementAmount || 0,
            courtName: d.courtName || '',
            lessonsLearned: d.lessonsLearned || '',
            // 构造词诉进度时间轴
            timeline: [
              { step: '立案登记', time: d.filingDate || '', content: `类型：${d.disputeType}，涉商：${d.disputeAmount}万元`, type: 'primary' },
              d.courtName ? { step: '开庭审理', time: '', content: `法院：${d.courtName}`, type: 'warning' } : null,
              d.judgmentResult ? { step: '裁判/和解结果', time: '', content: d.judgmentResult, type: 'success' } : null,
              d.caseStatus === 'CLOSED' && d.lessonsLearned ? { step: '经验总结', time: '', content: d.lessonsLearned, type: 'info' } : null,
            ].filter(Boolean),
          }))
        } else {
          this.list = []
        }
      } catch {
        this.list = []
      }
      this.$nextTick(() => { this.initTypeChart(); this.initAmtChart() })
    },
    async showDetail(row) {
      this.currentCase = row
      this.detailVisible = true
      this.detailLoading = true
      try {
        const res = await getDisputeDetail(row.caseNo)
        if (res && res.result === 200 && res.data) {
          const d = res.data
          // 用接口返回的完整数据更新 currentCase
          this.currentCase = {
            ...row,
            contractName: d.contractName || row.contractName,
            companyName: d.companyName || row.companyName,
            counterparty: d.counterpartyName || row.counterparty,
            disputeType: d.disputeType || row.disputeType,
            amount: Number(d.disputeAmount) || row.amount,
            status: d.caseStatus || row.status,
            startDate: d.filingDate || row.startDate,
            description: d.disputeReason || row.description,
            judgmentResult: d.judgmentResult || row.judgmentResult,
            settlementAmount: d.settlementAmount || row.settlementAmount,
            courtName: d.courtName || row.courtName,
            lessonsLearned: d.lessonsLearned || row.lessonsLearned,
            lawyerName: d.lawyerName || '',
            nextHearingDate: d.nextHearingDate || '',
            timeline: [
              { step: '立案登记', time: d.filingDate || '', content: `类型：${d.disputeType || row.disputeType}，涉诉：${d.disputeAmount || row.amount}万元`, type: 'primary' },
              d.courtName ? { step: '开庭审理', time: d.nextHearingDate || '', content: `法院：${d.courtName}`, type: 'warning' } : null,
              d.judgmentResult ? { step: '裁判/和解结果', time: '', content: d.judgmentResult, type: 'success' } : null,
              d.caseStatus === 'CLOSED' && d.lessonsLearned ? { step: '经验总结', time: '', content: d.lessonsLearned, type: 'info' } : null,
            ].filter(Boolean),
          }
        }
      } catch (e) {
        // 详情接口失败时保持原有数据展示
        console.warn('获取纠纷详情失败，使用列表数据展示', e)
      } finally {
        this.detailLoading = false
      }
    },
    goToArchiveManagement() {
      window.location.href = 'http://localhost:10000/#/monitorExecute/Fljfajgl'
    },
    caseStatusLabel(s) {
      const m = { NEGOTIATION: '协商中', MEDIATION: '调解中', TRIAL: '审理中', LITIGATION: '诉讼中', APPEAL: '二审中', EXECUTION: '执行中', CLOSED: '已结案' }
      return m[s] || s
    },
    caseStatusType(s) {
      const m = { NEGOTIATION: 'warning', MEDIATION: 'warning', TRIAL: 'danger', LITIGATION: 'danger', APPEAL: 'danger', EXECUTION: 'primary', CLOSED: 'success' }
      return m[s] || ''
    },
    disputeTagStyle(v) {
      const map = {
        '合同违约': { background: '#FFF1F0', color: '#F5222D', border: '1px solid #FFA39E' },
        '付款纠纷': { background: '#FFF7E6', color: '#FA8C16', border: '1px solid #FFD591' },
        '质量纠纷': { background: '#FFFBE6', color: '#FAAD14', border: '1px solid #FFE58F' },
        '知识产权': { background: '#F9F0FF', color: '#722ED1', border: '1px solid #D3ADF7' },
        '工程纠纷': { background: '#E8F4FF', color: '#0050A0', border: '1px solid #91CAFF' },
      }
      return map[v] || {}
    },
    initTypeChart() {
      const el = this.$refs.typeChart
      if (!el) return
      if (this.typeChart) this.typeChart.dispose()
      this.typeChart = echarts.init(el)
      const typeCount = {}
      this.list.forEach(r => { if (r.disputeType) typeCount[r.disputeType] = (typeCount[r.disputeType] || 0) + 1 })
      const colorMap = { '工程纠纷': '#0050A0', '知识产权': '#722ED1', '质量纠纷': '#FAAD14', '付款纠纷': '#FA8C16', '合同违约': '#F5222D' }
      const categories = Object.keys(typeCount)
      const values = categories.map(k => ({ value: typeCount[k], itemStyle: { color: colorMap[k] || '#1677FF' } }))
      if (categories.length === 0) {
        this.typeChart.setOption({ title: { text: '暂无数据', left: 'center', top: 'center', textStyle: { color: '#ccc', fontSize: 14 } } })
        return
      }
      this.typeChart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: 80, right: 30, top: 15, bottom: 20 },
        xAxis: { type: 'value' },
        yAxis: { type: 'category', data: categories },
        series: [{
          type: 'bar', barWidth: 16,
          data: values,
          label: { show: true, position: 'right', formatter: '{c} 件' },
        }],
      })
    },
    initAmtChart() {
      const el = this.$refs.amtChart
      if (!el) return
      if (this.amtChart) this.amtChart.dispose()
      this.amtChart = echarts.init(el)
      // 按企业分组显示涉诉金额
      const companyMap = {}
      this.list.forEach(r => {
        const name = r.companyName || '其他'
        companyMap[name] = (companyMap[name] || 0) + (Number(r.amount) || 0)
      })
      const companies = Object.keys(companyMap)
      if (companies.length === 0) {
        this.amtChart.setOption({ title: { text: '暂无数据', left: 'center', top: 'center', textStyle: { color: '#ccc', fontSize: 14 } } })
        return
      }
      this.amtChart.setOption({
        tooltip: { trigger: 'axis' },
        grid: { left: 80, right: 20, top: 20, bottom: 30 },
        xAxis: { type: 'category', data: companies, axisLabel: { rotate: 30, fontSize: 11 } },
        yAxis: { type: 'value', name: '万元' },
        series: [{
          name: '涉诉金额', type: 'bar', barWidth: 20,
          data: companies.map(c => companyMap[c]),
          itemStyle: { color: '#d46b08' },
          areaStyle: { color: { type: 'linear', x: 0, y: 0, x2: 0, y2: 1, colorStops: [{ offset: 0, color: 'rgba(212,107,8,0.35)' }, { offset: 1, color: 'rgba(212,107,8,0)' }] } },
        }],
      })
    },
  },
}
</script>

<style scoped lang="scss">
.dispute-wrap { padding: 16px; background: #f5f7fa; min-height: 100vh; }

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
.tag-pill { display: inline-block; padding: 2px 8px; border-radius: 4px; font-size: 12px; }

.timeline-card { border: none; box-shadow: 0 1px 4px rgba(0,0,0,0.1); }
.timeline-title { font-weight: 600; color: #333; margin-bottom: 4px; }
.timeline-content { font-size: 13px; color: #666; }
</style>
