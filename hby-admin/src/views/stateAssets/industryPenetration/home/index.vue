<template>
  <div class="app-container industry-page" :style="themeVars">
    <!-- 深海蓝 Banner -->
    <div class="page-header">
      <div class="page-header-left">
        <i class="el-icon-s-grid"></i>
        <div>
          <div class="header-title">行业穿透监管</div>
          <div class="header-sub">透过行业布局，看清产业结构合理性、主业集中度与行业竞争力</div>
        </div>
      </div>
      <div class="page-header-right">
        <el-button size="small" style="background:rgba(255,255,255,0.15);color:#fff;border-color:rgba(255,255,255,0.3)"
          icon="el-icon-data-analysis" @click="goTo('dashboard')">监控驾驶舱</el-button>
        <el-button size="small" style="background:rgba(255,255,255,0.15);color:#fff;border-color:rgba(255,255,255,0.3)"
          icon="el-icon-warning" @click="goTo('riskWarning')">风险预警</el-button>
      </div>
    </div>

    <!-- 穿透链路图 -->
    <el-card shadow="never" style="margin-bottom:14px">
      <div class="chain-bar">
        <div v-for="(node, idx) in chainNodes" :key="node.key" class="chain-node-wrap">
          <div class="chain-node" @click="goTo(node.key)">
            <i :class="node.icon"></i>
            <span>{{ node.label }}</span>
          </div>
          <i v-if="idx < chainNodes.length - 1" class="el-icon-arrow-right chain-arrow"></i>
        </div>
      </div>
    </el-card>

    <!-- KPI 统计卡片 -->
    <el-row :gutter="16" style="margin-bottom:14px">
      <el-col :span="4" v-for="item in kpiCards" :key="item.label">
        <el-card shadow="hover" :body-style="{ padding: '16px 18px' }">
          <div class="kpi-card">
            <div class="kpi-icon-wrap" :style="{ background: item.bgColor }">
              <i :class="item.icon" :style="{ color: item.color, fontSize: '22px' }"></i>
            </div>
            <div class="kpi-info">
              <div class="kpi-value" :style="{ color: item.valColor || '#303133' }">{{ item.value }}</div>
              <div class="kpi-label">{{ item.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 五大行业群概览 -->
    <div class="section-title"><i class="el-icon-office-building"></i> 五大行业群布局概览</div>
    <el-row :gutter="16" style="margin-bottom:14px">
      <el-col :span="4" v-for="ig in industryGroups" :key="ig.name">
        <el-card shadow="hover" :body-style="{ padding: '16px' }" class="industry-group-card"
          :style="{ borderTop: '3px solid ' + ig.color }" @click.native="goTo(ig.route)">
          <div class="ig-header">
            <i :class="ig.icon" :style="{ color: ig.color, fontSize: '20px' }"></i>
            <span class="ig-name">{{ ig.name }}</span>
          </div>
          <div class="ig-data">
            <div class="ig-item"><span class="ig-v">{{ ig.enterpriseCount }}</span><span class="ig-u">家</span></div>
            <div class="ig-meta">营收 {{ ig.revenue }}亿</div>
            <div class="ig-meta">净利 {{ ig.profit }}亿</div>
            <el-tag :type="riskTagType(ig.riskLevel)" size="mini" style="margin-top:6px">{{ riskLabel(ig.riskLevel) }}风险</el-tag>
          </div>
        </el-card>
      </el-col>
      <!-- 综合统计 -->
      <el-col :span="4">
        <el-card shadow="hover" :body-style="{ padding: '16px' }" :style="{borderTop: '3px solid ' + ipPrimary}">
          <div class="ig-header"><i class="el-icon-pie-chart" :style="{color: ipPrimary, fontSize: '20px'}"></i><span class="ig-name">合计汇总</span></div>
          <div class="ig-data">
            <div class="ig-item"><span class="ig-v">{{ summaryStats.total }}</span><span class="ig-u">家</span></div>
            <div class="ig-meta">营收 {{ summaryStats.revenue }}亿</div>
            <div class="ig-meta">净利 {{ summaryStats.profit }}亿</div>
            <el-tag type="warning" size="mini" style="margin-top:6px">非主业 {{ summaryStats.nonMainRatio }}%</el-tag>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能模块导航 -->
    <div class="section-title"><i class="el-icon-s-operation"></i> 核心功能模块</div>
    <el-row :gutter="16">
      <el-col :span="6" v-for="mod in funcModules" :key="mod.key" style="margin-bottom:14px">
        <el-card shadow="hover" :body-style="{ padding: '18px' }" class="func-card"
          @click.native="goTo(mod.key)">
          <div class="func-header">
            <div class="func-icon-wrap" :style="{ background: mod.bgColor }">
              <i :class="mod.icon" style="font-size:20px;color:#fff"></i>
            </div>
            <div>
              <div class="func-title">{{ mod.title }}</div>
              <div class="func-desc">{{ mod.desc }}</div>
            </div>
          </div>
          <div class="func-features">
            <span v-for="f in mod.features" :key="f" class="feat-tag">{{ f }}</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 底部：重点预警 + 最新动态 -->
    <el-row :gutter="16" style="margin-top:14px">
      <el-col :span="14">
        <el-card shadow="never" :body-style="{ padding: '16px 20px' }">
          <div slot="header" :style="{fontWeight:'600', color: ipSecondary}"><i class="el-icon-warning"></i> 重点行业预警</div>
          <el-table :data="recentWarnings" size="small" :header-cell-style="{ background: ipLightBg, color: ipSecondary }" border>
            <el-table-column label="预警时间" prop="time" width="145" align="center" />
            <el-table-column label="所属行业" prop="industry" width="100" align="center" />
            <el-table-column label="预警内容" prop="desc" min-width="160" show-overflow-tooltip />
            <el-table-column label="风险等级" prop="level" width="90" align="center">
              <template slot-scope="scope">
                <el-tag :type="riskTagType(scope.row.level)" size="mini">{{ riskLabel(scope.row.level) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="状态" prop="status" width="80" align="center">
              <template slot-scope="scope">
                <el-tag :type="scope.row.status === 'PENDING' ? 'danger' : scope.row.status === 'PROCESSING' ? 'warning' : 'success'" size="mini">
                  {{ { PENDING: '待处理', PROCESSING: '处理中', RESOLVED: '已处置' }[scope.row.status] }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
          <div style="text-align:right;margin-top:10px">
            <el-button type="text" @click="goTo('riskWarning')">查看全部 →</el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never" :body-style="{ padding: '16px 20px' }">
          <div slot="header" :style="{fontWeight:'600', color: ipSecondary}"><i class="el-icon-news"></i> 最新行业动态</div>
          <div v-for="item in industryNews" :key="item.id" class="news-item">
            <el-tag :style="{ background: item.tagBg, color: item.tagColor, border: 'none' }" size="mini" style="margin-right:8px">{{ item.industry }}</el-tag>
            <span class="news-text">{{ item.content }}</span>
            <span class="news-time">{{ item.time }}</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getIndustryKPI, getIndustryOverview, getIndustryWarningList } from '@/api/stateAssets/industryPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'IndustryPenetrationHome',
  mixins: [investThemeMixin],
  data() {
    return {
      chainNodes: [
        { key: 'industryLayout', label: '布局识别', icon: 'el-icon-s-grid' },
        { key: 'dashboard', label: '行业监控', icon: 'el-icon-data-analysis' },
        { key: 'competitiveness', label: '竞争分析', icon: 'el-icon-trophy' },
        { key: 'synergy', label: '协同评估', icon: 'el-icon-connection' },
        { key: 'riskWarning', label: '风险预警', icon: 'el-icon-warning' },
      ],
      kpiCards: [
        { label: '覆盖行业数', value: '-', icon: 'el-icon-s-grid', bgColor: '#EBF1FF', color: '#0050A0' },
        { label: '纳管企业总数', value: '-', icon: 'el-icon-office-building', bgColor: '#EBF1FF', color: '#0050A0' },
        { label: '主业企业数', value: '-', icon: 'el-icon-circle-check', bgColor: '#F6FFED', color: '#52C41A' },
        { label: '非主业占比', value: '-', icon: 'el-icon-pie-chart', bgColor: '#FFF7E6', color: '#FA8C16', valColor: '#FA8C16' },
        { label: '高风险行业', value: '-', icon: 'el-icon-warning', bgColor: '#FFF1F0', color: '#F5222D', valColor: '#F5222D' },
        { label: '行业预警总数', value: '-', icon: 'el-icon-bell', bgColor: '#FFF1F0', color: '#F5222D', valColor: '#F5222D' },
      ],
      industryGroups: [],
      summaryStats: { total: '-', revenue: '-', profit: '-', nonMainRatio: '-' },
      funcModules: [
        { key: 'dashboard', title: '行业监控驾驶舱', desc: '行业全景实时监控', icon: 'el-icon-data-analysis', bgColor: 'linear-gradient(135deg,#003A6C,#1677FF)',
          features: ['五大行业群KPI', '营收趋势分析', '风险热力矩阵'] },
        { key: 'industryLayout', title: '行业布局台账', desc: '企业行业分类管理', icon: 'el-icon-document', bgColor: 'linear-gradient(135deg,#0050A0,#1677FF)',
          features: ['主业/非主业认定', '行业分类归属', '布局结构分析'] },
        { key: 'energy', title: '能源行业监管', desc: '能源安全与碳中和监控', icon: 'el-icon-lightning', bgColor: 'linear-gradient(135deg,#d46b08,#FA8C16)',
          features: ['新旧能源结构', '碳排放强度', '能源转型进度'] },
        { key: 'financial', title: '金融行业监管', desc: '金融风险防控监督', icon: 'el-icon-bank', bgColor: 'linear-gradient(135deg,#0050A0,#40a9ff)',
          features: ['不良资产率监控', '资本充足率', '合规经营状态'] },
        { key: 'manufacturing', title: '制造行业监管', desc: '制造竞争力与创新监控', icon: 'el-icon-s-tools', bgColor: 'linear-gradient(135deg,#389e0d,#73d13d)',
          features: ['研发投入强度', '智能制造等级', '技术自主率'] },
        { key: 'infrastructure', title: '基础设施监管', desc: '基础设施投资效益分析', icon: 'el-icon-house', bgColor: 'linear-gradient(135deg,#003A6C,#0050A0)',
          features: ['资产回报率', '资产负债率', '安全生产评级'] },
        { key: 'publicService', title: '公共服务监管', desc: '公益属性与可持续经营', icon: 'el-icon-service', bgColor: 'linear-gradient(135deg,#531dab,#722ED1)',
          features: ['政策补贴依赖度', '社会责任评分', '连续亏损预警'] },
        { key: 'competitiveness', title: '行业竞争力分析', desc: '五维度竞争力评分', icon: 'el-icon-trophy', bgColor: 'linear-gradient(135deg,#d48806,#faad14)',
          features: ['研发/市场/盈利', '品牌/创新评分', '行业内排名'] },
        { key: 'synergy', title: '产业协同分析', desc: '跨行业协同价值挖掘', icon: 'el-icon-connection', bgColor: 'linear-gradient(135deg,#006d75,#13c2c2)',
          features: ['协同矩阵热力图', '协同效益统计', '内部交易分析'] },
        { key: 'riskWarning', title: '行业风险预警', desc: '12条行业特色预警规则', icon: 'el-icon-warning', bgColor: 'linear-gradient(135deg,#a8071a,#f5222d)',
          features: ['集中度超标预警', '主业偏离预警', '金融不良率预警'] },
        { key: 'drillDown', title: '行业穿透分析', desc: '四层穿透下钻查看', icon: 'el-icon-share', bgColor: 'linear-gradient(135deg,#1d39c4,#2f54eb)',
          features: ['集团→行业→企业', '逐层下钻', '穿透路径面包屑'] },
      ],
      recentWarnings: [],
      industryNews: [],
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    goTo(key) {
      const routeMap = {
        dashboard: '/industry/HYjkjsc',
        riskWarning: '/industry/Fxyjgl',
        industryLayout: '/industry/Hybjtz',
        competitiveness: '/industry/Jzlfx',
        synergy: '/industry/Cyxtfx',
        energy: '/industry/energy',
        financial: '/industry/tsfinancial',
        manufacturing: '/industry/manufacturing',
        infrastructure: '/industry/infrastructure',
        publicService: '/industry/publicService',
        drillDown: '/industry/Hyctfx'
      }
      const path = routeMap[key]
      if (path) { this.$router.push(path).catch(() => {}) }
      else { this.$message.info(`${key}功能模块加载中...`) }
    },
    async loadData() {
      try {
        const res = await getIndustryKPI()
        if (res && res.result === 200 && res.data) {
          const d = res.data
          if (d.kpiCards) this.kpiCards = d.kpiCards
          if (d.industryGroups) this.industryGroups = d.industryGroups
          if (d.summaryStats) this.summaryStats = d.summaryStats
          if (d.industryNews) this.industryNews = d.industryNews
        }
      } catch (e) { /* ignore */ }
      try {
        const wr = await getIndustryWarningList({ pageSize: 5 })
        if (wr && wr.result === 200 && wr.data && (wr.data.tlist || wr.data.list)) {
          const list = (wr.data.tlist || wr.data.list).slice(0, 5)
          // 映射字段名以匹配首页表格 prop
          this.recentWarnings = list.map(item => ({
            ...item,
            time: item.warnTime || item.time || '',
            desc: item.description || item.desc || item.warnType || '',
          }))
        }
      } catch (e) { /* ignore */ }
    },
    riskTagType(v) { return { HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }[v] || 'info' },
    riskLabel(v) { return { HIGH: '高', MEDIUM: '中', LOW: '低' }[v] || v },
  },
}
</script>

<style lang="scss" scoped>
.industry-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header {
  display: flex; align-items: center; justify-content: space-between;
  margin-bottom: 14px; padding: 16px 24px;
  background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 50%, var(--ip-bright, #1677FF) 100%);
  border-radius: 6px; color: #fff;
}
.page-header-left { display: flex; align-items: center; gap: 14px;
  i { font-size: 32px; opacity: 0.9; }
}
.header-title { font-size: 18px; font-weight: 700; }
.header-sub { font-size: 13px; opacity: 0.8; margin-top: 3px; }
.page-header-right { display: flex; gap: 10px; }

.chain-bar { display: flex; align-items: center; justify-content: center; gap: 0; padding: 8px 0; }
.chain-node-wrap { display: flex; align-items: center; }
.chain-node {
  display: flex; align-items: center; gap: 6px; padding: 8px 22px; border-radius: 20px; cursor: pointer;
  background: var(--ip-light-bg, #EBF1FF); color: var(--ip-secondary, #0050A0); font-size: 14px; font-weight: 500; transition: all 0.2s;
  &:hover { background: var(--ip-secondary, #0050A0); color: #fff; }
  i { font-size: 16px; }
}
.chain-arrow { font-size: 20px; color: var(--ip-secondary, #0050A0); margin: 0 8px; }

.kpi-card { display: flex; align-items: center; }
.kpi-icon-wrap { width: 44px; height: 44px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-right: 12px; flex-shrink: 0; }
.kpi-info { flex: 1; }
.kpi-value { font-size: 22px; font-weight: bold; color: #303133; line-height: 1; }
.kpi-label { font-size: 12px; color: #909399; margin-top: 4px; }

.section-title { font-size: 15px; font-weight: 600; color: #303133; margin-bottom: 10px;
  i { color: var(--ip-secondary, #0050A0); margin-right: 6px; } }

.industry-group-card { cursor: pointer; transition: box-shadow 0.2s;
  &:hover { box-shadow: 0 4px 18px rgba(0,80,160,0.15); }
}
.ig-header { display: flex; align-items: center; gap: 8px; margin-bottom: 10px; }
.ig-name { font-size: 14px; font-weight: 600; color: #303133; }
.ig-data { }
.ig-item { display: flex; align-items: baseline; gap: 3px; }
.ig-v { font-size: 24px; font-weight: bold; color: #303133; }
.ig-u { font-size: 12px; color: #909399; }
.ig-meta { font-size: 12px; color: #606266; margin-top: 3px; }

.func-card { cursor: pointer; transition: box-shadow 0.2s;
  &:hover { box-shadow: 0 4px 18px rgba(0,80,160,0.15); }
}
.func-header { display: flex; align-items: flex-start; gap: 12px; margin-bottom: 10px; }
.func-icon-wrap { width: 44px; height: 44px; border-radius: 8px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
.func-title { font-size: 14px; font-weight: 600; color: #303133; }
.func-desc { font-size: 12px; color: #909399; margin-top: 2px; }
.func-features { display: flex; flex-wrap: wrap; gap: 5px; }
.feat-tag { background: var(--ip-light-bg, #EBF1FF); color: var(--ip-secondary, #0050A0); font-size: 11px; padding: 2px 8px; border-radius: 10px; }

.news-item { display: flex; align-items: flex-start; padding: 9px 0; border-bottom: 1px solid #f0f2f5;
  &:last-child { border-bottom: none; }
}
.news-text { flex: 1; font-size: 13px; color: #303133; line-height: 1.5; }
.news-time { font-size: 12px; color: #909399; margin-left: 8px; white-space: nowrap; }

::v-deep .el-card { border-radius: 6px; }
</style>
