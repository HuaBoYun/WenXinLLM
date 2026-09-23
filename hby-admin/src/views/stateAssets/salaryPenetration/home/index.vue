<template>
  <div class="salary-home">
    <!-- Banner -->
    <div class="sal-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-content">
        <div class="banner-left">
          <div class="banner-title">薪酬穿透监管平台</div>
          <div class="banner-sub">全级次 · 全人员 · 全科目 — 薪酬一张表，合规一本账</div>
          <div class="banner-tags">
            <span v-for="node in chainNodes" :key="node.key" class="chain-tag" @click="goTo(node.key)">
              <i :class="node.icon"></i> {{ node.label }}
            </span>
          </div>
        </div>
        <div class="banner-right">
          <el-button type="primary" plain size="small" @click="goTo('dashboard')"><i class="el-icon-monitor"></i> 监控驾驶舱</el-button>
          <el-button type="success" plain size="small" @click="goTo('complianceCheck')"><i class="el-icon-circle-check"></i> 合规检查</el-button>
        </div>
      </div>
    </div>

    <!-- KPI指标卡 -->
    <el-row :gutter="16" class="kpi-row">
      <el-col :span="4" v-for="kpi in kpiCards" :key="kpi.key">
        <div class="kpi-card">
          <div class="kpi-label">{{ kpi.label }}</div>
          <div class="kpi-value" :style="{ color: kpi.color }">{{ kpi.value }}</div>
          <div class="kpi-unit">{{ kpi.unit }}</div>
        </div>
      </el-col>
    </el-row>

    <!-- 预算执行率分布 -->
    <el-card class="budget-card" shadow="never">
      <div slot="header"><span class="card-title">工资总额预算执行率分布</span><span class="card-tip">超100%为超预算，标红预警</span></div>
      <div v-for="item in execRateList" :key="item.company" class="exec-row">
        <span class="exec-company">{{ item.company }}</span>
        <el-progress
          :percentage="Math.min(item.execRate, 110)"
          :color="item.execRate > 100 ? '#F5222D' : item.execRate > 95 ? '#FA8C16' : '#52C41A'"
          :stroke-width="14"
          class="exec-bar"
        ></el-progress>
        <span class="exec-val" :style="{ color: item.execRate > 100 ? '#F5222D' : item.execRate > 95 ? '#FA8C16' : '#52C41A' }">
          {{ item.execRate }}%
          <el-tag v-if="item.execRate > 100" type="danger" size="mini">超预算</el-tag>
        </span>
      </div>
    </el-card>

    <!-- 功能模块导航 -->
    <el-card class="module-card" shadow="never">
      <div slot="header"><span class="card-title">核心功能模块</span></div>
      <el-row :gutter="16">
        <el-col :span="6" v-for="m in moduleCards" :key="m.key" class="module-col">
          <div class="module-item" @click="goTo(m.key)">
            <i :class="m.icon" :style="{ color: m.color, fontSize: '28px' }"></i>
            <div class="module-label">{{ m.label }}</div>
            <div class="module-desc">{{ m.desc }}</div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 底部：预警 + 异常企业 -->
    <el-row :gutter="16" class="bottom-row">
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header"><span class="card-title" style="color:#F5222D"><i class="el-icon-warning"></i> 重点薪酬预警</span></div>
          <el-table :data="warningList" size="small" stripe>
            <el-table-column prop="warnNo" label="预警编号" width="160"></el-table-column>
            <el-table-column prop="companyName" label="企业" width="120"></el-table-column>
            <el-table-column prop="warnType" label="预警内容"></el-table-column>
            <el-table-column prop="level" label="等级" width="70">
              <template slot-scope="{ row }">
                <el-tag :type="row.level === 'HIGH' ? 'danger' : 'warning'" size="mini">{{ row.level === 'HIGH' ? '高危' : '中危' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80">
              <template>
                <el-button type="text" size="mini" @click="goTo('riskWarning')">处置</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header"><span class="card-title"><i class="el-icon-s-flag"></i> 薪酬异常企业榜</span></div>
          <div v-for="item in anomalyList" :key="item.name" class="anomaly-row">
            <div class="anomaly-name">{{ item.name }}</div>
            <el-tag :type="item.riskLevel === 'HIGH' ? 'danger' : item.riskLevel === 'MEDIUM' ? 'warning' : 'success'" size="mini">{{ item.riskLabel }}</el-tag>
            <div class="anomaly-desc">{{ item.desc }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getSalaryDashboard, getSalaryWarningList, getSalaryDrillData } from '@/api/stateAssets/salaryPenetration'
import { mapGetters } from 'vuex'

export default {
  name: 'SalaryPenetrationHome',
  data() {
    return {
      kpi: { companyCount: 0, totalSalary: 0, avgLinkRatio: 0, activeWarnings: 0, budgetExecRate: 0, execComplianceRate: 0 },
      execRateList: [],
      warningList: [],
      anomalyList: [],
      chainNodes: [
        { key: 'totalList', label: '工资总额', icon: 'el-icon-money' },
        { key: 'performanceLink', label: '效益联动', icon: 'el-icon-sort' },
        { key: 'executivePay', label: '高管合规', icon: 'el-icon-user-solid' },
        { key: 'laborCost', label: '人工成本', icon: 'el-icon-s-opportunity' },
        { key: 'riskWarning', label: '风险预警', icon: 'el-icon-warning' },
      ],
      moduleCards: [
        { key: 'dashboard', label: '监控驾驶舱', icon: 'el-icon-monitor', color: '#1677FF', desc: '薪酬综合态势总览' },
        { key: 'totalList', label: '工资总额管理', icon: 'el-icon-money', color: '#0050A0', desc: '预算执行监控' },
        { key: 'performanceLink', label: '效益联动分析', icon: 'el-icon-sort', color: '#52C41A', desc: '工资与效益联动核查' },
        { key: 'executivePay', label: '高管薪酬监控', icon: 'el-icon-user-solid', color: '#722ED1', desc: '高管薪酬合规性验证' },
        { key: 'incentivePlan', label: '中长期激励', icon: 'el-icon-trophy', color: '#FA8C16', desc: '激励计划合规追踪' },
        { key: 'laborCost', label: '人工成本分析', icon: 'el-icon-s-opportunity', color: '#13C2C2', desc: '人工成本结构分析' },
        { key: 'complianceCheck', label: '薪酬合规检查', icon: 'el-icon-circle-check', color: '#52C41A', desc: '三维合规排查' },
        { key: 'riskWarning', label: '风险预警管理', icon: 'el-icon-warning', color: '#F5222D', desc: '多维度薪酬预警' },
        { key: 'drillDown', label: '薪酬穿透分析', icon: 'el-icon-zoom-in', color: '#0050A0', desc: '四层穿透下钻分析' },
      ],
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
    kpiCards() {
      const k = this.kpi
      return [
        { key: 'companyCount', label: '监控企业数', value: k.companyCount, unit: '家', color: '#1677FF' },
        { key: 'totalSalary', label: '集团工资总额', value: k.totalSalary, unit: '亿元', color: '#0050A0' },
        { key: 'avgLinkRatio', label: '平均效益联动率', value: k.avgLinkRatio, unit: '', color: k.avgLinkRatio < 0.8 ? '#FA8C16' : '#52C41A' },
        { key: 'activeWarnings', label: '活跃预警总数', value: k.activeWarnings, unit: '条', color: k.activeWarnings > 0 ? '#F5222D' : '#52C41A' },
        { key: 'budgetExecRate', label: '集团预算执行率', value: k.budgetExecRate + '%', unit: '', color: k.budgetExecRate > 100 ? '#F5222D' : k.budgetExecRate > 95 ? '#FA8C16' : '#52C41A' },
        { key: 'execComplianceRate', label: '高管薪酬合规率', value: k.execComplianceRate + '%', unit: '', color: k.execComplianceRate < 80 ? '#F5222D' : k.execComplianceRate < 90 ? '#FA8C16' : '#52C41A' },
      ]
    },
  },
  mounted() {
    this.loadDashboard()
    this.loadWarnings()
    this.loadDrillData()
  },
  methods: {
    async loadDashboard() {
      try {
        const res = await getSalaryDashboard()
        if (res.data) {
          const d = res.data
          if (d.kpi) {
            this.kpi = {
              companyCount: d.kpi.companyCount || 0,
              totalSalary: d.kpi.totalSalary || 0,
              avgLinkRatio: d.kpi.avgLinkRatio || 0,
              activeWarnings: d.kpi.activeWarnings || 0,
              budgetExecRate: d.kpi.budgetExecRate || 0,
              execComplianceRate: d.kpi.execComplianceRate || 0,
            }
          }
          // 从overview构建执行率列表
          if (d.overview && d.overview.length) {
            this.execRateList = d.overview.map(item => ({
              company: item.companyName,
              execRate: item.budgetExecRate || 0,
            }))
          }
        }
      } catch (e) {
        console.warn('加载驾驶舱数据失败', e)
      }
    },
    async loadWarnings() {
      try {
        const res = await getSalaryWarningList({ pageSize: 4, status: 'PENDING' })
        if (res.data && res.data.tlist && res.data.tlist.length) {
          this.warningList = res.data.tlist.map(w => ({
            warnNo: w.warnNo || w.warningId,
            companyName: w.companyName,
            warnType: w.warnType || w.warningContent,
            level: w.level,
          }))
        }
      } catch (e) {
        console.warn('加载预警数据失败', e)
      }
    },
    async loadDrillData() {
      try {
        const res = await getSalaryDrillData({})
        if (res.data && res.data.children) {
          // Only set execRateList if not already populated by dashboard
          if (!this.execRateList.length) {
            this.execRateList = res.data.children.map(c => ({
              company: c.name,
              execRate: c.budgetExecRate || 0,
            }))
          }
          // Build anomaly list from drill data
          this.anomalyList = res.data.children
            .filter(c => c.riskLevel !== 'LOW')
            .map(c => ({
              name: c.name,
              riskLevel: c.riskLevel,
              riskLabel: c.riskLevel === 'HIGH' ? '高风险' : c.riskLevel === 'MEDIUM' ? '中风险' : '低风险',
              desc: c.budgetExecRate > 100 ? '预算执行率超标' : '效益联动异常',
            }))
        }
      } catch (e) {
        console.warn('加载穿透数据失败', e)
      }
    },
    goTo(key) {
      const routeMap = {
        dashboard: '/formMonitor/Xcjkjsc',
        complianceCheck: '/formMonitor/Xchgswpc',
        totalList: '/formMonitor/salaryPenetration',
        performanceLink: '/formMonitor/Xyldfx',
        executivePay: '/formMonitor/Ggxcfx',
        laborCost: '/formMonitor/Rgcbfx',
        riskWarning: '/formMonitor/salaryPenetrationriskWarning',
        incentivePlan: '/formMonitor/Zcqjljh',
        drillDown: '/formMonitor/salaryPenetrationdrillDown',
      }
      const path = routeMap[key]
      if (path) {
        this.$router.push(path)
      }
    },
  },
}
</script>

<style scoped>
.salary-home { padding: 16px; background: #f0f2f5; min-height: 100vh; }
.sal-banner {
  border-radius: 8px; padding: 24px 28px; color: #fff; margin-bottom: 16px;
}
.banner-content { display: flex; justify-content: space-between; align-items: center; }
.banner-title { font-size: 22px; font-weight: 700; margin-bottom: 6px; }
.banner-sub { font-size: 13px; opacity: 0.85; margin-bottom: 12px; }
.banner-tags { display: flex; gap: 8px; }
.chain-tag {
  background: rgba(255,255,255,0.15); border: 1px solid rgba(255,255,255,0.3);
  padding: 4px 12px; border-radius: 20px; font-size: 12px; cursor: pointer; transition: all 0.2s;
}
.chain-tag:hover { background: rgba(255,255,255,0.3); }
.banner-right { display: flex; flex-direction: column; gap: 8px; }
.kpi-row { margin-bottom: 16px; }
.kpi-card {
  background: #fff; border-radius: 8px; padding: 16px; text-align: center;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);
}
.kpi-label { font-size: 12px; color: #8c8c8c; margin-bottom: 6px; }
.kpi-value { font-size: 24px; font-weight: 700; }
.kpi-unit { font-size: 12px; color: #8c8c8c; margin-top: 2px; }
.budget-card { margin-bottom: 16px; }
.card-title { font-size: 14px; font-weight: 600; color: #262626; }
.card-tip { font-size: 12px; color: #8c8c8c; margin-left: 12px; }
.exec-row { display: flex; align-items: center; margin-bottom: 12px; }
.exec-company { width: 110px; font-size: 13px; flex-shrink: 0; }
.exec-bar { flex: 1; margin: 0 12px; }
.exec-val { width: 110px; font-size: 13px; text-align: right; }
.module-card { margin-bottom: 16px; }
.module-col { margin-bottom: 12px; }
.module-item {
  background: #fafafa; border: 1px solid #f0f0f0; border-radius: 8px;
  padding: 16px; text-align: center; cursor: pointer; transition: all 0.2s;
}
.module-item:hover { box-shadow: 0 4px 12px rgba(22,119,255,0.15); border-color: #1677FF; }
.module-label { font-size: 13px; font-weight: 600; margin-top: 8px; color: #262626; }
.module-desc { font-size: 11px; color: #8c8c8c; margin-top: 4px; }
.bottom-row { margin-bottom: 16px; }
.anomaly-row { display: flex; align-items: center; padding: 10px 0; border-bottom: 1px solid #f0f0f0; gap: 10px; }
.anomaly-row:last-child { border-bottom: none; }
.anomaly-name { width: 110px; font-size: 13px; font-weight: 500; }
.anomaly-desc { flex: 1; font-size: 12px; color: #8c8c8c; }
</style>
