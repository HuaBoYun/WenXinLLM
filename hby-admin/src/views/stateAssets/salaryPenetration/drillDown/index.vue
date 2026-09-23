<template>
  <div class="sal-drill">
    <div class="sal-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-title">薪酬穿透分析</div>
      <div class="banner-sub">四层穿透 · 集团总览 → 企业概览 → 维度指标 → 人员/科目明细</div>
    </div>

    <!-- 面包屑 + 步骤 -->
    <el-card shadow="never" class="nav-card">
      <div class="nav-row">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item @click.native="goLayer(0)" :class="{ 'bc-active': layer >= 0 }">集团总览</el-breadcrumb-item>
          <el-breadcrumb-item v-if="layer >= 1" @click.native="goLayer(1)" :class="{ 'bc-active': layer >= 1 }">{{ currentCompany ? currentCompany.name : '' }}</el-breadcrumb-item>
          <el-breadcrumb-item v-if="layer >= 2" @click.native="goLayer(2)" :class="{ 'bc-active': layer >= 2 }">{{ currentDim ? currentDim.label : '' }}</el-breadcrumb-item>
          <el-breadcrumb-item v-if="layer >= 3">明细</el-breadcrumb-item>
        </el-breadcrumb>
        <el-steps :active="layer" finish-status="success" class="steps" simple>
          <el-step title="集团总览"></el-step>
          <el-step title="企业概览"></el-step>
          <el-step title="维度指标"></el-step>
          <el-step title="人员明细"></el-step>
        </el-steps>
      </div>
    </el-card>

    <!-- 第0层：集团企业卡片 -->
    <div v-if="layer === 0">
      <el-row :gutter="16">
        <el-col :span="8" v-for="item in drillData" :key="item.id">
          <div class="company-card" @click="selectCompany(item)">
            <div class="cc-header">
              <span class="cc-name">{{ item.name }}</span>
              <el-badge v-if="item.warnCount > 0" :value="item.warnCount" type="danger" class="cc-badge"></el-badge>
              <el-tag :type="item.riskLevel === 'HIGH' ? 'danger' : item.riskLevel === 'MEDIUM' ? 'warning' : 'success'" size="mini" style="margin-left:auto">{{ { HIGH: '高风险', MEDIUM: '中风险', LOW: '低风险' }[item.riskLevel] }}</el-tag>
            </div>
            <el-row :gutter="8" class="cc-kpis">
              <el-col :span="12" v-for="k in item.kpis" :key="k.key">
                <div class="cc-kpi">
                  <div class="cc-kpi-label">{{ k.label }}</div>
                  <div class="cc-kpi-val" :style="{ color: k.color }">{{ k.value }}</div>
                </div>
              </el-col>
            </el-row>
            <div class="cc-bar-row">
              <span style="font-size:11px;color:#8c8c8c">预算执行率</span>
              <el-progress
                :percentage="Math.min(item.budgetExecRate, 110)"
                :color="item.budgetExecRate > 100 ? '#F5222D' : item.budgetExecRate > 95 ? '#FA8C16' : '#52C41A'"
                :stroke-width="8"
                class="cc-bar"
              ></el-progress>
              <span :style="{ color: item.budgetExecRate > 100 ? '#F5222D' : '#8c8c8c', fontSize: '12px' }">{{ item.budgetExecRate }}%</span>
            </div>
            <div class="cc-enter"><i class="el-icon-arrow-right"></i> 点击穿透</div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 第1层：企业概览 + 维度选择 -->
    <div v-if="layer === 1 && currentCompany">
      <el-row :gutter="16" class="company-kpi-row">
        <el-col :span="6" v-for="k in currentCompany.kpis" :key="k.key">
          <div class="kpi-card"><div class="kpi-label">{{ k.label }}</div><div class="kpi-value" :style="{ color: k.color }">{{ k.value }}</div></div>
        </el-col>
      </el-row>
      <el-card shadow="never" class="dim-card">
        <div slot="header"><span class="card-title">选择穿透维度</span></div>
        <div class="dim-list">
          <div v-for="d in dimensions" :key="d.key" class="dim-item" @click="selectDim(d)">
            <i :class="d.icon" :style="{ color: d.color, fontSize: '24px' }"></i>
            <div class="dim-label">{{ d.label }}</div>
            <div class="dim-desc">{{ d.desc }}</div>
            <i class="el-icon-arrow-right dim-arrow"></i>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 第2层：维度指标表格 -->
    <div v-if="layer === 2 && currentDim">
      <el-card shadow="never" class="dim-table-card">
        <div slot="header">
          <span class="card-title">{{ currentCompany.name }} · {{ currentDim.label }}</span>
          <el-button size="small" style="float:right" @click="goLayer(1)">返回</el-button>
        </div>
        <el-table :data="dimTableData" border stripe size="small">
          <el-table-column prop="metric" label="指标名称" width="160"></el-table-column>
          <el-table-column prop="value" label="当前值" align="center">
            <template slot-scope="{ row }">
              <span :style="{ color: row.abnormal ? '#F5222D' : 'inherit', fontWeight: row.abnormal ? 700 : 400 }">{{ row.value }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="benchmark" label="行业基准" align="center"></el-table-column>
          <el-table-column prop="yoy" label="同比变化" align="center">
            <template slot-scope="{ row }">
              <span :style="{ color: row.yoy && row.yoy.startsWith('+') ? '#F5222D' : '#52C41A' }">{{ row.yoy }}</span>
            </template>
          </el-table-column>
          <el-table-column label="状态" align="center" width="90">
            <template slot-scope="{ row }">
              <el-tag :type="row.abnormal ? 'danger' : 'success'" size="mini">{{ row.abnormal ? '异常' : '正常' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="90">
            <template slot-scope="{ row }">
              <el-button type="text" size="mini" @click="drillDetail(row)">穿透明细</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>

    <!-- 第3层：人员/科目明细 -->
    <div v-if="layer === 3">
      <el-card shadow="never">
        <div slot="header">
          <span class="card-title">{{ currentCompany.name }} · {{ currentDim.label }} · {{ currentMetric ? currentMetric.metric : '' }} 明细</span>
          <el-button size="small" style="float:right" @click="goLayer(2)">返回</el-button>
        </div>
        <el-table :data="detailData" border stripe size="small">
          <el-table-column prop="category" label="科目/人员" width="140"></el-table-column>
          <el-table-column prop="amount" label="金额（万元）" align="right">
            <template slot-scope="{ row }">
              <span :style="{ color: row.abnormal ? '#F5222D' : 'inherit' }">{{ row.amount.toLocaleString() }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="proportion" label="占比" align="center">
            <template slot-scope="{ row }"><span>{{ row.proportion }}%</span></template>
          </el-table-column>
          <el-table-column prop="period" label="期间" align="center"></el-table-column>
          <el-table-column prop="remark" label="备注"></el-table-column>
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script>
import { getSalaryDrillData, getSalaryTotalList, getPerformanceLinkList, getExecutivePayList, getLaborCostList, getComplianceIssueList, getSalaryWarningList, getIncentivePlanList } from '@/api/stateAssets/salaryPenetration'
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
  name: 'SalaryDrillDown',
  data() {
    return {
      layer: 0,
      drillData: [],
      currentCompany: null,
      currentDim: null,
      currentMetric: null,
      dimTableData: [],
      detailData: [],
      dimensions: [
        { key: 'wage', label: '工资总额分析', icon: 'el-icon-money', color: '#1677FF', desc: '工资总额预算执行情况' },
        { key: 'link', label: '效益联动分析', icon: 'el-icon-sort', color: '#52C41A', desc: '工资与效益增长联动核查' },
        { key: 'executive', label: '高管薪酬核查', icon: 'el-icon-user-solid', color: '#722ED1', desc: '高管薪酬合规性验证' },
        { key: 'labor', label: '人工成本分析', icon: 'el-icon-s-opportunity', color: '#FA8C16', desc: '人工成本构成与趋势' },
        { key: 'incentive', label: '激励计划追踪', icon: 'el-icon-trophy', color: '#13C2C2', desc: '激励方案合规实施进度' },
        { key: 'compliance', label: '合规问题清单', icon: 'el-icon-circle-check', color: '#F5222D', desc: '三维合规排查结果' },
        { key: 'warning', label: '预警记录', icon: 'el-icon-warning', color: '#F5222D', desc: '本企业预警记录' },
      ],
    }
  },
  mounted() { this.loadData() },
  methods: {
    async loadData() {
      try {
        const res = await getSalaryDrillData({ layer: 0 })
        if (res.data) {
          const data = res.data
          if (data.children && data.children.length) {
            this.drillData = data.children.map(item => ({
              id: item.key,
              name: item.name,
              totalSalary: item.totalSalary || 0,
              budgetExecRate: Number(item.budgetExecRate || 0),
              linkCoeff: Number(item.linkCoefficient || 0),
              riskLevel: item.riskLevel,
              kpis: [
                { key: 'total', label: '工资总额（万元）', value: Number(item.totalSalary || 0).toLocaleString(), color: '#1677FF' },
                { key: 'execRate', label: '预算执行率', value: Number(item.budgetExecRate || 0) + '%', color: Number(item.budgetExecRate || 0) > 100 ? '#F5222D' : '#52C41A' },
                { key: 'link', label: '效益联动系数', value: Number(item.linkCoefficient || 0).toFixed(2), color: '#52C41A' },
                { key: 'cost', label: '风险等级', value: { HIGH: '高风险', MEDIUM: '中风险', LOW: '低风险' }[item.riskLevel], color: item.riskLevel === 'HIGH' ? '#F5222D' : '#52C41A' },
              ],
            }))
          }
        }
      } catch (e) {
        console.error('加载穿透数据失败', e)
      }
    },
    selectCompany(item) {
      this.currentCompany = item
      this.layer = 1
    },
    async selectDim(d) {
      this.currentDim = d
      this.layer = 2
      await this.loadDimData(d)
    },
    async loadDimData(d) {
      try {
        const companyName = this.currentCompany ? this.currentCompany.name : ''
        if (d.key === 'wage') {
          const res = await getSalaryTotalList({ pageSize: 20 })
          const list = (res.data && res.data.tlist) || []
          const filtered = companyName ? list.filter(r => r.companyName === companyName) : list
          this.dimTableData = filtered.map(r => {
            const budgetExecRate = r.budgetTotal && r.budgetTotal > 0
              ? ((r.actualTotal || 0) / r.budgetTotal * 100).toFixed(1)
              : 0
            return {
              metric: '工资总额',
              value: (r.actualTotal || 0).toLocaleString() + '万元',
              benchmark: '<预算',
              yoy: r.wageGrowthRate ? (r.wageGrowthRate > 0 ? '+' : '') + r.wageGrowthRate + '%' : '—',
              abnormal: Number(budgetExecRate) > 100,
            }
          })
        } else if (d.key === 'link') {
          const res = await getPerformanceLinkList({ pageSize: 20 })
          const list = (res.data && res.data.tlist) || []
          const filtered = companyName ? list.filter(r => r.companyName === companyName) : list
          this.dimTableData = filtered.map(r => ({
            metric: '联动系数',
            value: Number(r.linkCoefficient || 0).toFixed(2),
            benchmark: '0.8~1.2',
            yoy: '—',
            abnormal: Number(r.linkCoefficient || 0) > 1.5 || Number(r.linkCoefficient || 0) < 0.6,
          }))
        } else if (d.key === 'executive') {
          const res = await getExecutivePayList({ pageSize: 20 })
          const list = (res.data && res.data.tlist) || []
          const filtered = companyName ? list.filter(r => r.companyName === companyName) : list
          this.dimTableData = filtered.map(r => ({
            metric: r.execName || '高管',
            value: (r.totalComp || 0) + '万元',
            benchmark: '≤8倍',
            yoy: '—',
            abnormal: Number(r.ratioToAvg || 0) > 8,
          }))
        } else if (d.key === 'labor') {
          const res = await getLaborCostList({ pageSize: 20 })
          const list = (res.data && res.data.tlist) || []
          const filtered = companyName ? list.filter(r => r.companyName === companyName) : list
          this.dimTableData = filtered.map(r => ({
            metric: '人工成本',
            value: Number(r.laborCostRate || 0).toFixed(1) + '%',
            benchmark: '<15%',
            yoy: r.yoyChange ? (r.yoyChange > 0 ? '+' : '') + r.yoyChange + '%' : '—',
            abnormal: Number(r.laborCostRate || 0) > 15,
          }))
        } else if (d.key === 'incentive') {
          const res = await getIncentivePlanList({ pageSize: 20 })
          const list = (res.data && res.data.tlist) || []
          this.dimTableData = list.map(r => ({
            metric: r.planName || '激励计划',
            value: r.approvalStatus || r.status || '—',
            benchmark: '—',
            yoy: r.progress != null ? r.progress + '%' : '—',
            abnormal: r.approvalStatus === 'REJECTED' || r.status === 'REJECTED',
          }))
        } else if (d.key === 'compliance') {
          const res = await getComplianceIssueList({ pageSize: 20 })
          const list = (res.data && res.data.tlist) || []
          const filtered = companyName ? list.filter(r => r.companyName === companyName) : list
          this.dimTableData = filtered.map(r => ({
            metric: r.issueDesc || '合规问题',
            value: r.isCompliant === '0' ? '不合规' : r.isCompliant === '2' ? '不合理' : '合规',
            benchmark: '—',
            yoy: '—',
            abnormal: r.isCompliant !== '1',
          }))
        } else if (d.key === 'warning') {
          const res = await getSalaryWarningList({ pageSize: 20 })
          const list = (res.data && res.data.tlist) || []
          const filtered = companyName ? list.filter(r => r.companyName === companyName) : list
          this.dimTableData = filtered.map(r => ({
            metric: r.warnType || r.warningType || '预警',
            value: r.level === 'HIGH' ? '高危' : r.level === 'MEDIUM' ? '中危' : '低危',
            benchmark: '—',
            yoy: r.triggerTime || '—',
            abnormal: r.level === 'HIGH',
          }))
        }
      } catch (e) {
        console.error('加载维度数据失败', e)
        this.dimTableData = []
      }
    },
    async drillDetail(row) {
      this.currentMetric = row
      this.layer = 3
      const companyName = this.currentCompany ? this.currentCompany.name : ''
      try {
        if (this.currentDim.key === 'wage') {
          const res = await getSalaryTotalList({ companyName, pageSize: 20 })
          const list = (res.data && res.data.tlist) || []
          this.detailData = list.map(r => ({
            category: r.companyName || companyName,
            amount: r.actualTotal || 0,
            proportion: r.budgetTotal && r.budgetTotal > 0 ? ((r.actualTotal / r.budgetTotal) * 100).toFixed(1) : 0,
            period: r.reportYear,
            remark: r.actualTotal > r.budgetTotal ? '超预算' : '',
            abnormal: r.actualTotal > r.budgetTotal,
          }))
        } else if (this.currentDim.key === 'link') {
          const res = await getPerformanceLinkList({ companyName, pageSize: 20 })
          const list = (res.data && res.data.tlist) || []
          this.detailData = list.map(r => ({
            category: r.companyName || companyName,
            amount: Number(r.wageGrowthRate || 0),
            proportion: Number(r.linkCoefficient || 0).toFixed(2),
            period: r.reportYear,
            remark: Number(r.linkCoefficient || 0) > 1.5 ? '严重偏高' : Number(r.linkCoefficient || 0) < 0.6 ? '严重脱钩' : '',
            abnormal: Number(r.linkCoefficient || 0) > 1.5 || Number(r.linkCoefficient || 0) < 0.6,
          }))
        } else if (this.currentDim.key === 'executive') {
          const res = await getExecutivePayList({ companyName, pageSize: 20 })
          const list = (res.data && res.data.tlist) || []
          this.detailData = list.map(r => ({
            category: r.execName,
            amount: r.totalComp || 0,
            proportion: r.ratioToAvg ? Number(r.ratioToAvg).toFixed(1) + '倍' : '',
            period: r.reportYear,
            remark: r.isCompliant === '0' ? '超8倍上限' : '',
            abnormal: r.isCompliant === '0',
          }))
        } else if (this.currentDim.key === 'labor') {
          const res = await getLaborCostList({ companyName, pageSize: 20 })
          const list = (res.data && res.data.tlist) || []
          this.detailData = list.map(r => ({
            category: r.companyName || companyName,
            amount: r.totalLaborCost || r.totalCost || 0,
            proportion: r.laborCostRate || 0,
            period: r.reportYear,
            remark: r.riskLevel === 'HIGH' ? '高风险' : '',
            abnormal: r.riskLevel === 'HIGH',
          }))
        } else if (this.currentDim.key === 'incentive') {
          const res = await getIncentivePlanList({ pageSize: 20 })
          const list = (res.data && res.data.tlist) || []
          const filtered = companyName ? list.filter(r => r.companyName === companyName) : list
          this.detailData = filtered.map(r => ({
            category: r.planName || r.incentiveType || '激励计划',
            amount: r.scale || r.totalAmount || 0,
            proportion: r.progress || 0,
            period: (r.startDate || '') + ' ~ ' + (r.endDate || ''),
            remark: r.approvalStatus === 'REJECTED' ? '已驳回' : r.approvalStatus === 'COMPLETED' ? '已兑现' : '',
            abnormal: r.approvalStatus === 'REJECTED',
          }))
        } else if (this.currentDim.key === 'compliance') {
          const res = await getComplianceIssueList({ pageSize: 20 })
          const list = (res.data && res.data.tlist) || []
          const filtered = companyName ? list.filter(r => r.companyName === companyName) : list
          this.detailData = filtered.map(r => ({
            category: r.issueDesc || '合规问题',
            amount: 0,
            proportion: 0,
            period: r.checkYear || '',
            remark: r.dimension || r.checkType || '',
            abnormal: r.riskLevel === 'HIGH',
          }))
        } else if (this.currentDim.key === 'warning') {
          const res = await getSalaryWarningList({ pageSize: 20 })
          const list = (res.data && res.data.tlist) || []
          const filtered = companyName ? list.filter(r => r.companyName === companyName) : list
          this.detailData = filtered.map(r => ({
            category: r.warnType || r.warningType || '预警',
            amount: Number(r.triggerValue || 0),
            proportion: Number(r.thresholdValue || 0),
            period: r.triggerTime || r.createTime || '',
            remark: r.handleResult || '',
            abnormal: r.level === 'HIGH',
          }))
        }
        if (!this.detailData || !this.detailData.length) {
          this.detailData = []
        }
      } catch (e) {
        console.error('加载明细数据失败', e)
        this.detailData = []
      }
    },
    goLayer(n) {
      if (n === 0) { this.layer = 0; this.currentCompany = null; this.currentDim = null }
      else if (n === 1) { this.layer = 1; this.currentDim = null }
      else if (n === 2) { this.layer = 2 }
    },
  },
}
</script>

<style scoped>
.sal-drill { padding: 16px; background: #f0f2f5; min-height: 100vh; }
.sal-banner {
  border-radius: 8px; padding: 20px 24px; color: #fff; margin-bottom: 16px;
}
.banner-title { font-size: 20px; font-weight: 700; margin-bottom: 4px; }
.banner-sub { font-size: 13px; opacity: 0.85; }
.nav-card { margin-bottom: 16px; }
.nav-row { display: flex; justify-content: space-between; align-items: center; }
.steps { flex: 1; max-width: 500px; }
::v-deep .bc-active .el-breadcrumb__inner { color: #1677FF; cursor: pointer; font-weight: 600; }
.company-card {
  background: #fff; border-radius: 8px; padding: 16px; margin-bottom: 16px;
  cursor: pointer; transition: all 0.2s; border: 1px solid #f0f0f0;
}
.company-card:hover { box-shadow: 0 4px 12px rgba(22,119,255,0.15); border-color: #1677FF; }
.cc-header { display: flex; align-items: center; gap: 8px; margin-bottom: 12px; }
.cc-name { font-size: 14px; font-weight: 600; }
.cc-badge { margin-left: 4px; }
.cc-kpis { margin-bottom: 12px; }
.cc-kpi { background: #fafafa; border-radius: 4px; padding: 8px; text-align: center; }
.cc-kpi-label { font-size: 11px; color: #8c8c8c; margin-bottom: 2px; }
.cc-kpi-val { font-size: 14px; font-weight: 700; }
.cc-bar-row { display: flex; align-items: center; gap: 8px; margin-bottom: 8px; }
.cc-bar { flex: 1; }
.cc-enter { font-size: 12px; color: #1677FF; text-align: right; }
.company-kpi-row { margin-bottom: 16px; }
.kpi-card { background: #fff; border-radius: 8px; padding: 14px; text-align: center; box-shadow: 0 1px 4px rgba(0,0,0,0.08); }
.kpi-label { font-size: 12px; color: #8c8c8c; margin-bottom: 4px; }
.kpi-value { font-size: 20px; font-weight: 700; }
.dim-card { margin-bottom: 16px; }
.card-title { font-size: 14px; font-weight: 600; color: #262626; }
.dim-list { display: flex; flex-direction: column; gap: 1px; }
.dim-item {
  display: flex; align-items: center; padding: 14px 16px; border-radius: 6px;
  cursor: pointer; transition: background 0.15s; gap: 14px; border-bottom: 1px solid #f5f5f5;
}
.dim-item:hover { background: #f0f5ff; }
.dim-label { font-size: 14px; font-weight: 600; width: 120px; }
.dim-desc { flex: 1; font-size: 12px; color: #8c8c8c; }
.dim-arrow { color: #1677FF; }
.dim-table-card { margin-bottom: 16px; }
</style>
