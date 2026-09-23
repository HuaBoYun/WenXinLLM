<template>
  <div class="finance-penetration-container">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="header-left">
        <h1 class="page-title"><i class="el-icon-s-finance"></i>财务穿透</h1>
        <p class="page-description">管理财务报表台账、合并分析、资金流向、关联交易、财务指标对标等财务全链条穿透监管</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-document" @click="goTo('report')">报表台账</el-button>
        <el-button type="success" icon="el-icon-data-analysis" @click="goTo('drillDown')">穿透下钻</el-button>
        <el-button type="warning" icon="el-icon-odometer" @click="goTo('dashboard')">监控驾驶舱</el-button>
      </div>
    </div>
    <div class="stats-overview">
      <el-row :gutter="24">
        <el-col :span="6">
          <div class="stat-card"><div class="stat-icon icon-1"><i class="el-icon-coin"></i></div>
            <div class="stat-content"><div class="stat-value">{{ formatAmount(stats.totalAssets) }}</div><div class="stat-label">资产总额</div></div></div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card"><div class="stat-icon icon-2"><i class="el-icon-data-line"></i></div>
            <div class="stat-content"><div class="stat-value">{{ formatAmount(stats.totalRevenue) }}</div><div class="stat-label">营业收入</div></div></div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card"><div class="stat-icon icon-3"><i class="el-icon-connection"></i></div>
            <div class="stat-content"><div class="stat-value">{{ stats.relatedTxCount }}</div><div class="stat-label">关联交易数</div></div></div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card"><div class="stat-icon icon-4"><i class="el-icon-warning"></i></div>
            <div class="stat-content"><div class="stat-value">{{ stats.anomalyCount }}</div><div class="stat-label">异常检测数</div></div></div>
        </el-col>
      </el-row>
    </div>
    <div class="function-modules">
      <el-row :gutter="24" v-for="(row, rowIdx) in moduleRows" :key="'row-' + rowIdx" :style="rowIdx > 0 ? 'margin-top: 24px' : ''">
        <el-col :span="8" v-for="(mod, idx) in row" :key="mod.key">
          <div class="module-card" :class="'card-color-' + ((rowIdx * 3 + idx) % 9 + 1)">
            <div class="card-header"><div class="card-icon"><i :class="mod.icon"></i></div>
              <div class="card-title"><h3>{{ mod.title }}</h3><span class="card-subtitle">{{ mod.desc }}</span></div></div>
            <div class="card-content"><div class="feature-list">
              <div class="feature-item" v-for="f in mod.features" :key="f"><i class="el-icon-circle-check"></i><span>{{ f }}</span></div>
            </div><div class="card-actions"><el-button type="primary" size="small" @click="goTo(mod.key)">{{ mod.btnText }}</el-button></div></div>
          </div>
        </el-col>
      </el-row>
    </div>
    <div class="quick-actions-panel">
      <div class="panel-header"><h3><i class="el-icon-lightning"></i>快速操作</h3><p>常用的财务穿透管理操作</p></div>
      <el-row :gutter="16">
        <el-col :span="4" v-for="action in quickActions" :key="action.key">
          <div class="quick-action-item" @click="handleQuickAction(action)">
            <div class="action-icon"><i :class="action.icon"></i></div>
            <div class="action-content"><div class="action-title">{{ action.title }}</div><div class="action-desc">{{ action.desc }}</div></div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script>
import { getFinanceStatistics, getRelatedTransactionList } from '@/api/stateAssets/financePenetration'
import { mapGetters } from 'vuex'
import { getAnomalyList } from '@/api/stateAssets/financialPenetration'

export default {
  name: 'FinancePenetrationHome',
  data() {
    return {
      stats: { totalAssets: 0, totalRevenue: 0, relatedTxCount: 0, anomalyCount: 0 },
      modules: [
        { key: 'report', title: '财务报表台账', desc: '管理合并与单体财务报表', icon: 'el-icon-document', btnText: '报表台账',
          features: ['财务报表登记', '报表数据校验', '报表对比分析', '报表归档管理'] },
        { key: 'consolidatedAnalysis', title: '合并财务分析', desc: '分析合并报表与业绩', icon: 'el-icon-s-data', btnText: '合并分析',
          features: ['合并报表分析', '业绩合并分析', '抵消分录检查', '合并范围管理'] },
        { key: 'fundFlow', title: '资金流向分析', desc: '追踪资金流向与异常', icon: 'el-icon-sort', btnText: '资金流向',
          features: ['资金流向追踪', '资金流向穿透图', '异常资金检测', '资金归集分析'] },
        { key: 'relatedTransaction', title: '关联交易管理', desc: '监控关联交易合规性', icon: 'el-icon-connection', btnText: '关联交易',
          features: ['关联交易台账', '关联方识别', '交易定价分析', '合规性审查'] },
        { key: 'benchmark', title: '财务指标对标', desc: '多维度财务指标对标分析', icon: 'el-icon-data-line', btnText: '指标对标',
          features: ['行业对标分析', '财务分析统计', '同业排名分析', '关键指标预警'] },
        { key: 'financialCompliance', title: '财务合规绩效', desc: '财务合规检查与绩效评价', icon: 'el-icon-document-checked', btnText: '合规绩效',
          features: ['财务合规检查', '财务绩效评价', '合规问题整改', '绩效考核分析'] },
        { key: 'anomaly', title: '财务异常检测', desc: 'AI辅助财务异常识别', icon: 'el-icon-warning-outline', btnText: '异常检测',
          features: ['异常指标检测', '财务风险统计', '数据质量检查', '风险评分模型'] },
        { key: 'dashboard', title: '财务监控驾驶舱', desc: '实时监控财务整体态势', icon: 'el-icon-odometer', btnText: '监控驾驶舱',
          features: ['财务穿透下钻', '风险态势感知', '收益趋势分析', '预警信息汇总'] }
      ],
      quickActions: [
        { key: 'report', title: '报表台账', desc: '查看财务报表', icon: 'el-icon-document' },
        { key: 'consolidatedAnalysis', title: '合并分析', desc: '合并报表分析', icon: 'el-icon-s-data' },
        { key: 'fundFlow', title: '资金流向', desc: '资金流向追踪', icon: 'el-icon-sort' },
        { key: 'relatedTransaction', title: '关联交易', desc: '关联交易监控', icon: 'el-icon-connection' },
        { key: 'anomaly', title: '异常检测', desc: '财务异常识别', icon: 'el-icon-warning-outline' },
        { key: 'dashboard', title: '监控驾驶舱', desc: '财务态势总览', icon: 'el-icon-odometer' }
      ]
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
    moduleRows() {
      const rows = []
      for (let i = 0; i < this.modules.length; i += 3) {
        rows.push(this.modules.slice(i, i + 3))
      }
      return rows
    }
  },
  mounted() { this.loadStats() },
  methods: {
    async loadStats() {
      try {
        const [statsRes, relatedRes, anomalyRes] = await Promise.all([
          getFinanceStatistics().catch(() => ({})),
          getRelatedTransactionList({ pageNumber: 1, pageSize: 1 }).catch(() => ({})),
          getAnomalyList({ pageNumber: 1, pageSize: 1 }).catch(() => ({}))
        ])
        const sd = statsRes.data || {}
        const rd = relatedRes.data || {}
        const ad = anomalyRes.data || {}
        this.stats = {
          totalAssets: sd.totalAssets || 0,
          totalRevenue: sd.totalRevenue || 0,
          relatedTxCount: rd.totalRecord || (rd.tlist && rd.tlist.length) || 0,
          anomalyCount: ad.totalRecord || (ad.tlist && ad.tlist.length) || 0
        }
      } catch (e) {
        console.error('加载统计数据失败', e)
      }
    },
    formatAmount(v) {
      if (!v) return '0'
      return (v / 100000000).toFixed(2) + '亿'
    },
    goTo(key) {
      this.$router.push('/stateAssets/financePenetration/' + key)
    },
    handleQuickAction(action) {
      this.$router.push('/stateAssets/financePenetration/' + action.key)
    }
  }
}
</script>

<style lang="scss" scoped>
.finance-penetration-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}
.page-header {
  display: flex; justify-content: space-between; align-items: center;
  background: white; border-radius: 12px; padding: 24px; margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  .header-left {
    .page-title { font-size: 24px; font-weight: 600; color: #303133; margin: 0 0 8px 0; display: flex; align-items: center;
      i { margin-right: 12px; color: #409eff; } }
    .page-description { color: #606266; font-size: 14px; margin: 0; }
  }
  .header-right { .el-button { margin-left: 12px; } }
}
.stats-overview {
  margin-bottom: 24px;
  .stat-card {
    background: white; border-radius: 12px; padding: 24px; display: flex; align-items: center;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1); transition: all 0.3s ease;
    &:hover { transform: translateY(-2px); box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15); }
    .stat-icon {
      width: 60px; height: 60px; border-radius: 12px; display: flex; align-items: center; justify-content: center; margin-right: 16px;
      i { font-size: 28px; color: white; }
      &.icon-1 { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
      &.icon-2 { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }
      &.icon-3 { background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%); }
      &.icon-4 { background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%); }
    }
    .stat-content {
      .stat-value { font-size: 28px; font-weight: 600; color: #303133; margin-bottom: 4px; }
      .stat-label { font-size: 14px; color: #909399; }
    }
  }
}
.function-modules {
  margin-bottom: 32px;
  .module-card {
    background: white; border-radius: 12px; padding: 24px; height: 280px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1); transition: all 0.3s ease;
    display: flex; flex-direction: column;
    &:hover { transform: translateY(-4px); box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15); }
    &.card-color-1 { border-left: 4px solid #409eff; }
    &.card-color-2 { border-left: 4px solid #67c23a; }
    &.card-color-3 { border-left: 4px solid #e6a23c; }
    &.card-color-4 { border-left: 4px solid #f56c6c; }
    &.card-color-5 { border-left: 4px solid #909399; }
    &.card-color-6 { border-left: 4px solid #9c27b0; }
    &.card-color-7 { border-left: 4px solid #00bcd4; }
    &.card-color-8 { border-left: 4px solid #ff5722; }
    &.card-color-9 { border-left: 4px solid #795548; }
  }
}
.card-header {
  display: flex; align-items: flex-start; margin-bottom: 20px;
  .card-icon {
    width: 48px; height: 48px; border-radius: 12px; display: flex; align-items: center; justify-content: center;
    margin-right: 16px; flex-shrink: 0; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    i { font-size: 24px; color: white; }
  }
  .card-title {
    flex: 1;
    h3 { font-size: 16px; font-weight: 600; color: #303133; margin: 0 0 4px 0; }
    .card-subtitle { font-size: 12px; color: #909399; line-height: 1.4; }
  }
}
.card-content { flex: 1; display: flex; flex-direction: column; justify-content: space-between; }
.feature-list {
  flex: 1;
  .feature-item { display: flex; align-items: center; margin-bottom: 12px; font-size: 14px; color: #606266;
    i { color: #67c23a; margin-right: 8px; font-size: 16px; } }
}
.card-actions { margin-top: 16px; }
.quick-actions-panel {
  background: white; border-radius: 12px; padding: 24px; box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  .panel-header {
    margin-bottom: 24px;
    h3 { font-size: 18px; font-weight: 600; color: #303133; margin: 0 0 8px 0; display: flex; align-items: center;
      i { margin-right: 8px; color: #409eff; } }
    p { color: #606266; font-size: 14px; margin: 0; }
  }
  .quick-action-item {
    background: #f8f9fa; border-radius: 8px; padding: 16px; cursor: pointer; transition: all 0.3s ease; display: flex; align-items: center;
    &:hover { background: #e9ecef; transform: translateY(-2px); }
    .action-icon {
      width: 40px; height: 40px; border-radius: 8px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      display: flex; align-items: center; justify-content: center; margin-right: 12px;
      i { font-size: 20px; color: white; }
    }
    .action-content {
      flex: 1;
      .action-title { font-size: 14px; font-weight: 600; color: #303133; margin-bottom: 4px; }
      .action-desc { font-size: 12px; color: #909399; }
    }
  }
}
</style>