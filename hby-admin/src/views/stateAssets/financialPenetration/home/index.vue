<template>
  <div class="fin-home-wrap">
    <!-- Banner -->
    <div class="page-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-left">
        <div class="banner-title">财务穿透式监管</div>
        <div class="banner-sub">穿透报表链·穿透指标链·穿透关联链，实现集团财务"一张图、一本账"全方位透明监管</div>
        <div class="banner-btns">
          <el-button size="small" type="primary" icon="el-icon-full-screen" @click="openScreen">大屏展示</el-button>
          <el-button size="small" type="primary" plain @click="$router.push('/financialPenetration/dashboard')">监控驾驶舱</el-button>
          <el-button size="small" type="danger" plain @click="$router.push('/financialPenetration/riskWarning')">风险预警</el-button>
        </div>
      </div>
      <div class="banner-chain">
        <div v-for="(node, i) in chainNodes" :key="node.key" class="chain-item" @click="$router.push('/financialPenetration/'+node.key)">
          <div class="chain-icon" :style="{background: node.color+'22', borderColor: node.color}">
            <i :class="node.icon" :style="{color: node.color, fontSize:'22px'}"></i>
          </div>
          <div class="chain-label">{{ node.label }}</div>
          <div v-if="i < chainNodes.length-1" class="chain-arrow">→</div>
        </div>
      </div>
    </div>

    <!-- 6个KPI卡 -->
    <el-row :gutter="14" style="margin-bottom:16px">
      <el-col v-for="card in kpiCards" :key="card.label" :span="4">
        <el-card class="kpi-card" shadow="hover">
          <div class="kpi-inner">
            <i :class="card.icon" :style="{color:card.color, fontSize:'24px'}"></i>
            <div>
              <div class="kpi-value" :style="{color:card.color}">{{ card.value }}</div>
              <div class="kpi-label">{{ card.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 资产负债率分布条 -->
    <el-card shadow="never" style="margin-bottom:16px">
      <div slot="header" class="card-header"><span>各企业资产负债率健康度分布</span></div>
      <div style="display:flex;gap:16px;flex-wrap:wrap">
        <div v-for="ent in debtRatioList" :key="ent.company" class="debt-item">
          <div style="display:flex;justify-content:space-between;margin-bottom:4px">
            <span style="font-size:12px;color:#333">{{ ent.company }}</span>
            <span :style="{fontSize:'12px',fontWeight:'700',color:ent.ratio>70?'#F5222D':ent.ratio>60?'#FA8C16':'#52C41A'}">{{ ent.ratio }}%</span>
          </div>
          <el-progress :percentage="ent.ratio" :stroke-width="8" style="width:200px"
            :color="ent.ratio>70?'#F5222D':ent.ratio>60?'#FA8C16':'#52C41A'"/>
        </div>
      </div>
    </el-card>

    <!-- 9个功能模块导航卡 -->
    <el-row :gutter="14" style="margin-bottom:16px">
      <el-col v-for="m in moduleCards" :key="m.key" :span="6" style="margin-bottom:14px">
        <el-card class="module-card" shadow="hover" @click.native="$router.push('/financialPenetration/'+m.key)">
          <div class="module-inner">
            <div class="module-icon-wrap" :style="{background: m.color+'15'}">
              <i :class="m.icon" :style="{color: m.color, fontSize:'28px'}"></i>
            </div>
            <div class="module-info">
              <div class="module-label">{{ m.label }}</div>
              <div class="module-desc">{{ m.desc }}</div>
            </div>
            <i class="el-icon-arrow-right" style="color:#bbb;margin-left:auto"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 底部：重点预警 + 异常企业 -->
    <el-row :gutter="16">
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header" class="card-header">
            <i class="el-icon-warning" style="color:#F5222D;margin-right:6px"></i>
            <span>重点财务预警（高危）</span>
            <el-tag type="danger" size="small" style="margin-left:8px">{{ warnList.length }} 条</el-tag>
          </div>
          <el-table :data="warnList" size="small" border>
            <el-table-column label="预警编号" prop="warnNo" width="160"/>
            <el-table-column label="企业" prop="companyName" width="130"/>
            <el-table-column label="预警类型" prop="warnType" min-width="180" show-overflow-tooltip/>
            <el-table-column label="级别" prop="level" width="70" align="center">
              <template slot-scope="{row}">
                <el-tag :type="row.level==='HIGH'?'danger':'warning'" size="mini">{{ row.level==='HIGH'?'高危':'中危' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="状态" prop="status" width="80" align="center">
              <template slot-scope="{row}">
                <el-tag :type="row.status==='PENDING'?'danger':'warning'" size="mini">{{ row.status==='PENDING'?'待处置':'处置中' }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header" class="card-header">
            <i class="el-icon-s-data" style="color:#FA8C16;margin-right:6px"></i>
            <span>财务指标异常企业</span>
          </div>
          <div v-for="ent in anomalyEnts" :key="ent.name" class="anomaly-ent-item">
            <div style="display:flex;justify-content:space-between;align-items:center">
              <span style="font-weight:600;font-size:13px">{{ ent.name }}</span>
              <el-tag :type="ent.riskLevel==='HIGH'?'danger':'warning'" size="mini">{{ ent.riskLevel==='HIGH'?'高风险':'中风险' }}</el-tag>
            </div>
            <div style="font-size:12px;color:#666;margin-top:4px">{{ ent.issues }}</div>
            <el-divider style="margin:8px 0"/>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <div v-if="screenVisible" ref="screenContainer" class="fullscreen-container">
      <FinancialPenetrationScreen @close="closeScreen" />
    </div>
  </div>
</template>

<script>
import { getFinancialDashboard, getFinancialWarningList, getAnomalyList, getDebtRatioMonitor } from '@/api/stateAssets/financialPenetration'
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
  name: 'FinancialPenetrationHome',
  components: { FinancialPenetrationScreen: () => import('../screen/index.vue') },
  data() {
    return {
      screenVisible: false,
      chainNodes: [
        { key: 'statementQuery', label: '报表穿透', icon: 'el-icon-document-copy', color: '#1677FF' },
        { key: 'benchmark', label: '指标对标', icon: 'el-icon-data-analysis', color: '#52C41A' },
        { key: 'relatedParty', label: '关联交易', icon: 'el-icon-share', color: '#722ED1' },
        { key: 'anomalyDetect', label: '异常检测', icon: 'el-icon-aim', color: '#CF1322' },
        { key: 'riskWarning', label: '风险预警', icon: 'el-icon-warning', color: '#F5222D' },
      ],
      kpiCards: [],
      debtRatioList: [],
      moduleCards: [
        { key: 'dashboard', label: '监控驾驶舱', icon: 'el-icon-monitor', color: '#1677FF', desc: '财务综合态势总览' },
        { key: 'statementQuery', label: '报表穿透查询', icon: 'el-icon-document-copy', color: '#0050A0', desc: '合并报表逐层穿透' },
        { key: 'benchmark', label: '指标对标分析', icon: 'el-icon-data-analysis', color: '#52C41A', desc: '关键指标行业对标' },
        { key: 'relatedParty', label: '关联交易监控', icon: 'el-icon-share', color: '#722ED1', desc: '关联交易公允性核查' },
        { key: 'expenseMonitor', label: '费用管控监控', icon: 'el-icon-money', color: '#FA8C16', desc: '费用率异常监控' },
        { key: 'anomalyDetect', label: '财务异常检测', icon: 'el-icon-aim', color: '#CF1322', desc: '智能财务异常识别' },
        { key: 'riskWarning', label: '风险预警管理', icon: 'el-icon-warning', color: '#F5222D', desc: '多维度财务预警' },
        { key: 'drillDown', label: '财务穿透分析', icon: 'el-icon-zoom-in', color: '#0050A0', desc: '四层穿透下钻分析' },
      ],
      warnList: [],
      anomalyEnts: [],
    }
  },
  async mounted() {
    await this.loadData()
  },
  methods: {
    async loadData() {
      try {
        const [dashRes, warnRes, anomalyRes, debtRes] = await Promise.all([
          getFinancialDashboard().catch(() => ({})),
          getFinancialWarningList({ pageNumber: 1, pageSize: 4 }).catch(() => ({})),
          getAnomalyList({ pageNumber: 1, pageSize: 5 }).catch(() => ({})),
          getDebtRatioMonitor().catch(() => ({}))
        ])
        // KPI卡片
        const kpi = dashRes.data && dashRes.data.kpi ? dashRes.data.kpi : {}
        this.kpiCards = [
          { label: '监控企业数', value: (kpi.totalStatements || 0) + '家', color: '#1677FF', icon: 'el-icon-office-building' },
          { label: '平均负债率', value: (kpi.avgDebtRatio || 0) + '%', color: '#FA8C16', icon: 'el-icon-bank-card' },
          { label: '综合ROE', value: (kpi.avgRoe || 0) + '%', color: '#52C41A', icon: 'el-icon-trend-charts' },
          { label: '活跃预警', value: (dashRes.data && dashRes.data.activeAlerts || 0) + '条', color: '#F5222D', icon: 'el-icon-warning' },
          { label: '资金异常占用', value: (kpi.abnormalFund || 0) + '万', color: '#FA8C16', icon: 'el-icon-money' },
          { label: '关联交易占比', value: (kpi.relatedRatio || 0) + '%', color: '#722ED1', icon: 'el-icon-connection' },
        ]
        // 负债率分布
        if (debtRes.data && debtRes.data.trend && debtRes.data.trend.length) {
          this.debtRatioList = debtRes.data.trend.map(t => ({ company: t.companyName, ratio: t.debtRatio }))
        }
        // 预警列表
        if (warnRes.data && warnRes.data.tlist && warnRes.data.tlist.length) {
          this.warnList = warnRes.data.tlist.slice(0, 4).map(w => ({
            warnNo: w.alertNo || w.id, companyName: w.companyName, warnType: w.alertTitle || w.alertType, level: w.riskLevel || 'HIGH', status: w.status
          }))
        }
        // 异常企业
        if (anomalyRes.data && anomalyRes.data.tlist && anomalyRes.data.tlist.length) {
          this.anomalyEnts = anomalyRes.data.tlist.slice(0, 3).map(a => ({
            name: a.companyName, riskLevel: a.riskLevel || 'HIGH', issues: a.anomalyDesc || a.anomalyType
          }))
        }
      } catch (e) { console.error('加载首页数据失败', e) }
    },
    openScreen() {
      this.screenVisible = true
      this.$nextTick(() => {
        const el = this.$refs.screenContainer
        if (el && el.requestFullscreen) el.requestFullscreen()
        else if (el && el.webkitRequestFullscreen) el.webkitRequestFullscreen()
      })
    },
    closeScreen() {
      this.screenVisible = false
      if (document.exitFullscreen) document.exitFullscreen()
      else if (document.webkitExitFullscreen) document.webkitExitFullscreen()
    },
  },
}
</script>

<style scoped lang="scss">
.fullscreen-container { position: fixed; top: 0; left: 0; width: 100vw; height: 100vh; z-index: 9999; }
.fin-home-wrap { padding: 16px; background: #f5f7fa; min-height: 100vh; }

.page-banner {
  border-radius: 8px; padding: 24px 28px; margin-bottom: 16px;
  display: flex; justify-content: space-between; align-items: flex-start; gap: 20px;
  .banner-title { font-size: 24px; font-weight: 700; color: #fff; margin-bottom: 6px; }
  .banner-sub { font-size: 13px; color: rgba(255,255,255,0.78); margin-bottom: 12px; max-width: 480px; }
  .banner-btns { display: flex; gap: 10px; }
  .banner-chain { display: flex; align-items: center; flex-shrink: 0; }
  .chain-item { display: flex; flex-direction: column; align-items: center; cursor: pointer; position: relative; }
  .chain-icon { width: 52px; height: 52px; border-radius: 50%; border: 2px solid; display: flex; align-items: center; justify-content: center; background: rgba(255,255,255,0.1); transition: all 0.2s;
    &:hover { transform: scale(1.1); }
  }
  .chain-label { font-size: 11px; color: rgba(255,255,255,0.85); margin-top: 4px; }
  .chain-arrow { position: absolute; right: -14px; top: 18px; color: rgba(255,255,255,0.5); font-size: 16px; z-index: 1; }
}

.kpi-card { border-radius: 8px; }
.kpi-inner { display: flex; align-items: center; gap: 12px;
  .kpi-value { font-size: 22px; font-weight: 700; }
  .kpi-label { font-size: 12px; color: #666; margin-top: 2px; }
}

.debt-item { min-width: 220px; }

.module-card { border-radius: 8px; cursor: pointer; transition: all 0.2s;
  &:hover { transform: translateY(-2px); box-shadow: 0 4px 16px rgba(22,119,255,0.15); }
}
.module-inner { display: flex; align-items: center; gap: 12px; }
.module-icon-wrap { width: 50px; height: 50px; border-radius: 10px; display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
.module-info { flex: 1;
  .module-label { font-size: 14px; font-weight: 600; color: #333; }
  .module-desc { font-size: 12px; color: #999; margin-top: 2px; }
}

.card-header { display: flex; align-items: center; font-weight: 600; }
.anomaly-ent-item { padding: 4px 0; }
</style>
