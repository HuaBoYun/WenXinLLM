<template>
  <div class="contract-home">
    <!-- Banner -->
    <div class="page-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-left">
        <div class="banner-title"><i class="el-icon-document" style="margin-right:8px;"/>合同穿透监管</div>
        <div class="banner-sub">全生命周期穿透 · 签订→审批→履行→变更→终止 全链条监管</div>
        <div class="banner-tags">
          <span class="b-tag">合规监控</span>
          <span class="b-tag">履行追踪</span>
          <span class="b-tag">信用评估</span>
          <span class="b-tag">纠纷预警</span>
        </div>
      </div>
      <div class="banner-right">
        <el-button size="small" style="background:rgba(255,255,255,0.15);color:#fff;border-color:rgba(255,255,255,0.3);" @click="navigateTo('dashboard')">
          <i class="el-icon-odometer"/> 监控驾驶舱
        </el-button>
        <el-button size="small" style="background:rgba(245,34,45,0.2);color:#fff;border-color:rgba(255,120,117,0.4);margin-left:8px;" @click="navigateTo('riskWarning')">
          <i class="el-icon-warning-outline"/> 风险预警
        </el-button>
      </div>
    </div>

    <!-- 穿透链路图 -->
    <el-card shadow="never" class="chain-card">
      <div class="chain-title">合同穿透链路</div>
      <div class="chain-row">
        <template v-for="(node, i) in chainNodes">
          <div class="chain-node" :key="node.key" @click="navigateTo(node.key)">
            <div class="chain-icon" :style="{background: node.color + '22', border: '1.5px solid ' + node.color}">
              <i :class="node.icon" :style="{color: node.color, fontSize:'20px'}"/>
            </div>
            <div class="chain-label">{{ node.label }}</div>
            <div class="chain-desc">{{ node.desc }}</div>
          </div>
          <div class="chain-arrow" v-if="i < chainNodes.length - 1" :key="'arrow-' + i">→</div>
        </template>
      </div>
    </el-card>

    <!-- KPI统计卡 -->
    <el-row :gutter="16" style="margin-bottom:16px;">
      <el-col :span="4" v-for="k in kpiCards" :key="k.label">
        <el-card class="kpi-card" shadow="never" :style="{'border-top': '3px solid ' + k.color}">
          <div class="kpi-val" :style="{color: k.color}">{{ k.value }}</div>
          <div class="kpi-unit">{{ k.unit }}</div>
          <div class="kpi-lbl">{{ k.label }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 合同类型分布 + 功能导航 -->
    <el-row :gutter="16" style="margin-bottom:16px;">
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header" style="font-weight:600;">合同类型分布</div>
          <div ref="typeChart" style="height:220px;"/>
          <div class="type-list">
            <div class="type-item" v-for="t in contractTypes" :key="t.name">
              <span class="type-dot" :style="{background: t.color}"/>
              <span class="type-name">{{ t.name }}</span>
              <span class="type-count">{{ t.count }}份</span>
              <span class="type-amt">{{ t.amount }}亿</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header" style="font-weight:600;">功能模块</div>
          <el-row :gutter="12">
            <el-col :span="8" v-for="m in modules" :key="m.route">
              <div class="module-card" @click="navigateTo(m.route)">
                <div class="mod-icon" :style="{background: m.color + '18'}">
                  <i :class="m.icon" :style="{color: m.color, fontSize:'22px'}"/>
                </div>
                <div class="mod-info">
                  <div class="mod-name">{{ m.name }}</div>
                  <div class="mod-desc">{{ m.desc }}</div>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <!-- 重点预警 + 最新动态 -->
    <el-row :gutter="16">
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header" style="display:flex;align-items:center;justify-content:space-between;">
            <span style="font-weight:600;"><i class="el-icon-warning" style="color:#F5222D;margin-right:4px;"/>重点预警</span>
            <el-button type="text" size="small" @click="navigateTo('riskWarning')">查看全部</el-button>
          </div>
          <el-table :data="topWarnings" size="small" :show-header="true">
            <el-table-column prop="warnNo" label="预警编号" width="150"/>
            <el-table-column prop="companyName" label="企业" width="120"/>
            <el-table-column prop="warnType" label="预警类型" min-width="140"/>
            <el-table-column prop="level" label="级别" width="70" align="center">
              <template slot-scope="{row}">
                <el-tag :type="row.level==='HIGH'?'danger':row.level==='MEDIUM'?'warning':'success'" size="mini" effect="dark">{{ row.level==='HIGH'?'高危':row.level==='MEDIUM'?'中危':'低危' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="80" align="center">
              <template slot-scope="{row}">
                <el-tag :type="row.status==='PENDING'?'danger':row.status==='PROCESSING'?'warning':'info'" size="mini">{{ row.status==='PENDING'?'待处置':row.status==='PROCESSING'?'处置中':'已关闭' }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header" style="font-weight:600;"><i class="el-icon-bell" style="color:#1677FF;margin-right:4px;"/>最新合同动态</div>
          <div class="dynamic-list">
            <div class="dynamic-item" v-for="(d,i) in dynamics" :key="i">
              <div class="dyn-dot" :style="{background: d.color}"/>
              <div class="dyn-content">
                <div class="dyn-text">{{ d.text }}</div>
                <div class="dyn-time">{{ d.time }}</div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import { getContractDashboard, getContractWarningList } from '@/api/stateAssets/contractPenetration'

export default {
  name: 'ContractPenetrationHome',
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
      const d = this.kpiData
      return [
        { label: '合同总量', value: d.totalContracts, unit: '份', color: '#1677FF' },
        { label: '审批合规率', value: d.complianceRate !== '--' ? d.complianceRate + '%' : '--', unit: '', color: '#52C41A' },
        { label: '履行滞后', value: d.delayCount, unit: '份', color: '#FA8C16' },
        { label: '纠纷案件', value: d.totalDisputes, unit: '件', color: '#873800' },
        { label: '活跃预警', value: d.warningCount, unit: '条', color: '#F5222D' },
      ]
    },
  },
  data() {
    return {
      chainNodes: [
        { key: 'contractList', label: '台账管理', icon: 'el-icon-document', color: '#1677FF', desc: '合同全量台账' },
        { key: 'compliance', label: '合规追踪', icon: 'el-icon-finished', color: '#52C41A', desc: '审批链路合规' },
        { key: 'execution', label: '履行监控', icon: 'el-icon-timer', color: '#FA8C16', desc: '履行进度实时' },
        { key: 'counterparty', label: '对方信用', icon: 'el-icon-user', color: '#722ED1', desc: '信用状态跟踪' },
        { key: 'riskWarning', label: '风险预警', icon: 'el-icon-warning', color: '#F5222D', desc: '多维度预警' },
      ],
      contractTypes: [],
      modules: [
        { route: 'dashboard', name: '监控驾驶舱', icon: 'el-icon-odometer', color: '#1677FF', desc: '态势综合总览' },
        { route: 'contractList', name: '合同台账', icon: 'el-icon-document', color: '#0050A0', desc: '全量台账管理' },
        { route: 'lifecycle', name: '生命周期', icon: 'el-icon-time', color: '#52C41A', desc: '全周期追踪' },
        { route: 'compliance', name: '审批合规', icon: 'el-icon-finished', color: '#237804', desc: '合规链路分析' },
        { route: 'execution', name: '履行监控', icon: 'el-icon-timer', color: '#FA8C16', desc: '进度实时监控' },
        { route: 'dispute', name: '纠纷诉讼', icon: 'el-icon-s-claim', color: '#873800', desc: '案件跟踪处置' },
        { route: 'counterparty', name: '对方信用', icon: 'el-icon-user-solid', color: '#722ED1', desc: '信用状态监控' },
        { route: 'riskWarning', name: '风险预警', icon: 'el-icon-warning', color: '#F5222D', desc: '预警分级处置' },
        { route: 'drillDown', name: '穿透分析', icon: 'el-icon-zoom-in', color: '#1677FF', desc: '四层逐级穿透' },
      ],
      topWarnings: [],
      dynamics: [],
      kpiData: {
        totalContracts: '--',
        complianceRate: '--',
        delayCount: '--',
        totalDisputes: '--',
        warningCount: '--',
      },
    }
  },

  mounted() {
    this.fetchDashboard()
    this.fetchWarnings()
  },
  methods: {
    navigateTo(key) {
      const routeMap = {
        dashboard: '/monitorExecute/HtJkjsc',
        contractList: '/monitorExecute/Httzgl',
        lifecycle: '/monitorExecute/Htsmzq',
        compliance: '/monitorExecute/Htsphgzz',
        execution: '/monitorExecute/Htlxjk1',
        dispute: '/monitorExecute/Htjfyssgl',
        counterparty: '/monitorExecute/Dfxyjk',
        riskWarning: '/monitorExecute/Fxyjgl',
        drillDown: '/monitorExecute/Htctfx',
      }
      if (routeMap[key]) {
        this.$router.push(routeMap[key])
      } else {
        this.$router.push('/stateAssets/contractPenetration/' + key)
      }
    },
    async fetchDashboard() {
      try {
        const res = await getContractDashboard()
        if (res && res.result === 200 && res.data) {
          const d = res.data
          this.kpiData = {
            totalContracts: d.totalContracts || 0,
            complianceRate: d.complianceRate || 0,
            delayCount: d.delayCount || 0,
            totalDisputes: d.totalDisputes || 0,
            warningCount: d.warningCount || 0,
          }
          const colorArr = ['#1677FF', '#52C41A', '#FA8C16', '#722ED1', '#F5222D', '#0050A0']
          const typeNameMap = {
            PURCHASE: '采购合同',
            SALES: '销售合同',
            ENGINEERING: '工程合同',
            SERVICE: '服务合同',
            LEASE: '租赁合同',
            LABOR: '劳务合同',
            OTHER: '其他',
          }
          this.contractTypes = (d.typeDistribution || []).map((t, i) => ({
            name: typeNameMap[t.name] || t.name,
            count: t.value,
            amount: 0,
            color: t.color || colorArr[i % colorArr.length],
          }))
          this.$nextTick(() => setTimeout(this.initChart, 50))
        }
      } catch (e) {
        console.error('首页仪表数据加载失败', e)
      }
    },
    async fetchWarnings() {
      try {
        const res = await getContractWarningList({ pageNumber: 1, pageSize: 5 })
        if (res && res.result === 200 && res.data) {
          this.topWarnings = res.data.tlist || []
          // 将预警数据转换为动态信息
          this.dynamics = (res.data.tlist || []).slice(0, 6).map(w => ({
            text: `${w.companyName} ${w.warnType}`,
            time: '最新',
            color: w.level === 'HIGH' ? '#F5222D' : w.level === 'MEDIUM' ? '#FA8C16' : '#52C41A',
          }))
        }
      } catch (e) {
        console.error('预警数据加载失败', e)
      }
    },
    initChart() {
      if (!this.$refs.typeChart) return
      // 容器宽度为0时延迟重试
      if (this.$refs.typeChart.clientWidth === 0) {
        setTimeout(this.initChart, 100)
        return
      }
      // 销毁旧实例
      const existing = echarts.getInstanceByDom(this.$refs.typeChart)
      if (existing) existing.dispose()
      // 无数据时不渲染图表
      if (!this.contractTypes || this.contractTypes.length === 0) return
      const data = this.contractTypes
      const chart = echarts.init(this.$refs.typeChart)
      chart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}：{c}份（占比{d}%）' },
        legend: {
          orient: 'vertical', right: '5%', top: 'center',
          textStyle: { fontSize: 11, color: '#595959' },
          formatter: name => {
            const t = data.find(x => x.name === name)
            return t ? `${name}  ${t.count}份` : name
          },
        },
        series: [{
          name: '合同类型分布',
          type: 'pie', radius: ['45%', '65%'], center: ['35%', '50%'],
          label: {
            show: true,
            formatter: '{b}\n{d}%',
            fontSize: 11,
            color: '#595959',
          },
          labelLine: { show: true, length: 10, length2: 8 },
          emphasis: {
            label: { show: true, fontSize: 13, fontWeight: 'bold' },
          },
          emptyCircleStyle: { color: '#f5f5f5' },
          data: data.map(t => ({ name: t.name, value: t.count, itemStyle: { color: t.color } })),
        }],
      })
    },
  },
}
</script>

<style lang="scss" scoped>
.contract-home { padding: 16px; background: #f5f7fa; min-height: 100%; }
.page-banner {
  border-radius: 8px; padding: 24px 32px; display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px;
  .banner-title { font-size: 22px; font-weight: 700; color: #fff; }
  .banner-sub { font-size: 13px; color: rgba(255,255,255,0.8); margin: 6px 0 10px; }
  .banner-tags { display: flex; gap: 8px; }
  .b-tag { padding: 2px 10px; border-radius: 12px; background: rgba(255,255,255,0.15); color: #fff; font-size: 12px; }
}
.chain-card { margin-bottom: 16px; }
.chain-title { font-weight: 600; color: #303133; margin-bottom: 12px; font-size: 13px; }
.chain-row { display: flex; align-items: center; gap: 0; }
.chain-node {
  display: flex; flex-direction: column; align-items: center; cursor: pointer; padding: 8px 16px; border-radius: 8px; transition: all 0.2s; flex: 1;
  &:hover { background: #f0f5ff; }
  .chain-icon { width: 44px; height: 44px; border-radius: 50%; display: flex; align-items: center; justify-content: center; margin-bottom: 6px; }
  .chain-label { font-size: 13px; font-weight: 600; }
  .chain-desc { font-size: 11px; color: #8c8c8c; margin-top: 2px; }
}
.kpi-card { text-align: center; border-radius: 6px; }
.kpi-val { font-size: 26px; font-weight: 700; }
.kpi-unit { font-size: 11px; color: #8c8c8c; }
.kpi-lbl { font-size: 12px; color: #595959; margin-top: 4px; }
.type-list { display: flex; flex-wrap: wrap; gap: 6px 12px; margin-top: 8px; }
.type-item { display: flex; align-items: center; gap: 5px; font-size: 12px; width: calc(50% - 6px); }
.type-dot { width: 8px; height: 8px; border-radius: 50%; flex-shrink: 0; }
.type-name { flex: 1; color: #595959; }
.type-count { color: #1677FF; font-weight: 600; }
.type-amt { color: #8c8c8c; margin-left: 4px; }
.module-card {
  display: flex; align-items: center; gap: 10px; padding: 10px; border-radius: 6px; cursor: pointer; margin-bottom: 8px; border: 1px solid #f0f0f0; transition: all 0.2s;
  &:hover { border-color: #1677FF; box-shadow: 0 2px 8px rgba(22,119,255,0.12); }
  .mod-icon { width: 36px; height: 36px; border-radius: 8px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
  .mod-name { font-size: 13px; font-weight: 600; }
  .mod-desc { font-size: 11px; color: #8c8c8c; margin-top: 2px; }
}
.dynamic-list { }
.dynamic-item { display: flex; align-items: flex-start; gap: 10px; padding: 8px 0; border-bottom: 1px solid #f5f5f5; }
.dyn-dot { width: 8px; height: 8px; border-radius: 50%; margin-top: 5px; flex-shrink: 0; }
.dyn-text { font-size: 13px; color: #303133; }
.dyn-time { font-size: 11px; color: #bfbfbf; margin-top: 2px; }
</style>
