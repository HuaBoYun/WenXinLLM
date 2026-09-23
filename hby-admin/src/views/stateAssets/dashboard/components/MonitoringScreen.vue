<template>
  <div class="ms-root" :class="{ fullscreen: isFullscreen }">

    <!-- ===== 顶部标题栏（含公司切换）===== -->
    <div class="ms-topbar">
      <!-- 公司切换（原 tb-center，移至左侧），国资穿透 badge 在公司组件左边 -->
      <div class="tb-left">
        <span class="sys-badge">国资穿透</span>
        <el-popover v-model="treePopVisible" placement="bottom" trigger="click"
          popper-class="ms-company-popper" :width="260">
          <div slot="reference" class="company-trigger">
            <i class="el-icon-office-building"></i>
            <span class="ct-name">{{ localEntName || '选择企业' }}</span>
            <i :class="treePopVisible ? 'el-icon-arrow-up' : 'el-icon-arrow-down'" class="ct-arrow"></i>
          </div>
          <div class="ms-tree-wrap">
            <el-input v-model="treeFilter" placeholder="搜索企业..." size="mini" clearable
              prefix-icon="el-icon-search" style="margin-bottom:8px" />
            <el-tree ref="msOrgTree" :data="treeData" :props="{label:'name',children:'children'}"
              :filter-node-method="filterNode" highlight-current node-key="id"
              :default-expanded-keys="defaultExpKeys" style="max-height:240px;overflow-y:auto"
              @node-click="onTreeNodeClick">
              <span slot-scope="{node,data}" class="ms-tree-node">
                <i :class="data.children&&data.children.length?'el-icon-folder':'el-icon-document'"></i>
                {{ node.label }}
              </span>
            </el-tree>
          </div>
        </el-popover>
      </div>

      <!-- 大屏名称居中 -->
      <div class="tb-center">
        <h1 class="sys-title">国资国企穿透式监管实时监控大屏</h1>
      </div>

      <div class="tb-right">
        <span class="sys-time">{{ currentTime }}</span>
        <el-button size="mini" class="ms-btn" @click="refreshAll" :loading="loading" style="margin-left:8px">
          <i class="el-icon-refresh"></i>
        </el-button>
        <el-button size="mini" class="ms-btn" @click="toggleFullscreen">
          <i :class="isFullscreen?'el-icon-copy-document':'el-icon-full-screen'"></i>
        </el-button>
      </div>
    </div>

    <!-- ===== 主体：四行布局 ===== -->
    <div class="ms-body">

      <!-- 上：企业驾驶舱（左中右）-->
      <div class="ms-row row-enterprise">
        <!-- 左：基本档案 -->
        <div class="ms-card ent-left" v-loading="entLoading">
          <div class="card-title"><i class="el-icon-office-building"></i> 企业基本档案</div>
          <div class="ent-name">{{ entInfo.enterpriseName || localEntName || '--' }}</div>
          <div class="ent-tags">
            <span class="et-tag">{{ entInfo.enterpriseTypeName || '--' }}</span>
            <span class="et-tag blue">{{ entInfo.industryTypeName || '--' }}</span>
            <span class="et-tag" :class="entInfo.riskLevel==='1'?'green':'orange'">
              {{ entInfo.riskLevel==='1'?'低风险':entInfo.riskLevel==='2'?'中等风险':'待评估' }}</span>
          </div>
          <div class="ent-divider"></div>
          <div class="ent-grid">
            <div class="eg-item">
              <span class="eg-l">法定代表人</span>
              <span class="eg-v">{{ entInfo.legalRepresentative||'--' }}</span>
            </div>
            <div class="eg-item">
              <span class="eg-l">注册资本</span>
              <span class="eg-v cyan">{{ entInfo.registeredCapital?fmtMoney(entInfo.registeredCapital):'--' }}</span>
            </div>
            <div class="eg-item">
              <span class="eg-l">成立年份</span>
              <span class="eg-v">{{ entInfo.establishDate||entInfo.establishmentDate||'--' }}</span>
            </div>
            <div class="eg-item">
              <span class="eg-l">员工总数</span>
              <span class="eg-v">{{ entInfo.employeeCount?entInfo.employeeCount+'人':'--' }}</span>
            </div>
            <div class="eg-item">
              <span class="eg-l">子公司数量</span>
              <span class="eg-v cyan">{{ entInfo.subsidiaryCount||'--' }}</span>
            </div>
            <div class="eg-item">
              <span class="eg-l">资产总额</span>
              <span class="eg-v yellow">{{ entInfo.totalAssets?fmtMoney(entInfo.totalAssets):'--' }}</span>
            </div>
          </div>
          <div class="ent-divider"></div>
          <!-- 风险分布 -->
          <div class="risk-label">风险等级分布</div>
          <div class="risk-row" v-for="r in riskBars" :key="r.label">
            <span class="rr-l">{{ r.label }}</span>
            <div class="rr-bar"><div class="rr-fill" :style="{width:r.pct+'%',background:r.color}"></div></div>
            <span class="rr-v" :style="{color:r.color}">{{ r.count }}</span>
          </div>
        </div>

        <!-- 中：360° 雷达图 -->
        <div class="ms-card ent-center">
          <div class="card-title"><i class="el-icon-aim"></i> 360° 全息雷达</div>
          <div ref="radarEl" class="radar-el"></div>
          <div class="radar-legend">
            <span class="rl-item"><i class="rl-dot" style="background:#00d4ff"></i>本企业</span>
            <span class="rl-item"><i class="rl-dot" style="background:#ffd666"></i>行业均值</span>
          </div>
        </div>

        <!-- 右：关键财务指标 -->
        <div class="ms-card ent-right">
          <div class="card-title"><i class="el-icon-data-analysis"></i> 关键财务指标</div>
          <div class="kf-grid">
            <div v-for="kf in keyFinancials" :key="kf.label" class="kf-item">
              <div class="kf-val" :class="kf.cls">{{ kf.value }}</div>
              <div class="kf-info">
                <div class="kf-lbl">{{ kf.label }}</div>
                <div class="kf-chg" :class="kf.chgType">
                  <i :class="kf.chgType==='up'?'el-icon-top':'el-icon-bottom'"></i>{{ kf.change }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 中：12个监管领域 -->
      <div class="ms-row row-domains">
        <div class="row-label"><i class="el-icon-s-grid"></i> 12大穿透监管领域</div>
        <div class="domain-grid" v-loading="loading">
          <div v-for="m in domains" :key="m.key" class="dc"
            :style="{'--ac':m.color}" @click="goModule(m)">
            <div class="dc-head">
              <span class="dc-icon" :style="{background:m.color+'22',color:m.color}"><i :class="m.icon"></i></span>
              <span class="dc-name">{{ m.name }}</span>
              <span class="dc-badge" :class="domainRiskClass(m.key)">{{ domainRiskLabel(m.key) }}</span>
            </div>
            <div class="dc-stats">
              <div v-for="(s,i) in (moduleStats[m.key]||placeholderStats)" :key="i" class="dcs">
                <div class="dcs-v" :class="s.cls">{{ s.value }}</div>
                <div class="dcs-l">{{ s.label }}</div>
              </div>
            </div>
            <div class="dc-prog"><div class="dc-prog-f" :style="{width:domainHealth(m.key)+'%',background:m.color}"></div></div>
          </div>
        </div>
      </div>

      <!-- 下：实时预警 -->
      <div class="ms-row row-warnings">
        <div class="row-label">
          <i class="el-icon-warning"></i> 实时风险预警
          <span class="warn-badge">{{ warningList.length }}</span>
          <span class="row-sub">仅展示待处理</span>
        </div>
        <div class="warn-table" v-loading="warnLoading">
          <div class="wt-head">
            <span class="w-lv">等级</span><span class="w-ti">预警标题</span>
            <span class="w-co">涉及企业</span><span class="w-do">所属领域</span>
            <span class="w-tm">时间</span><span class="w-st">状态</span>
          </div>
          <div class="wt-body">
            <div v-for="w in warningList" :key="w.id"
              class="wt-row" :class="levelRowClass(w.warningLevel||w.level)"
              style="cursor:pointer" @click="showWarningDetail(w)">
              <span class="w-lv">
                <span class="lv-tag" :class="'lv-'+(w.warningLevel||w.level||'LOW').toLowerCase()">
                  {{ levelText(w.warningLevel||w.level) }}
                </span>
              </span>
              <span class="w-ti">{{ extractDescSummary(w.warningDescription||w.warningDesc||'')||w.warningTitle||w.title||'预警信息' }}</span>
              <span class="w-co">{{ w.companyName||localEntName||'--' }}</span>
              <span class="w-do">{{ w.modelName||w.domainName||'综合监管' }}</span>
              <span class="w-tm">{{ w.createTime||'--' }}</span>
              <span class="w-st"><i class="el-icon-time"></i> 待处理</span>
            </div>
            <div v-if="!warnLoading&&warningList.length===0" class="wt-empty">
              <i class="el-icon-circle-check"></i> 无待处理预警，系统运行正常
            </div>
          </div>
        </div>
      </div>

      <!-- 尾：决策支持中心（预览条 + 弹窗）-->
      <div class="ms-row row-decision">
        <div class="decision-bar">
          <div class="db-left">
            <i class="el-icon-s-opportunity db-icon"></i>
            <span class="db-title">决策支持中心</span>
            <span class="db-sub">· 政策影响 / 风险预测 / 投资决策 / 绩效评估 / 智能推荐</span>
          </div>
          <div class="db-items">
            <span v-for="di in decisionItems" :key="di.name" class="db-item" :style="{'--dc':di.color}">
              <i :class="di.icon"></i> {{ di.name }}
            </span>
          </div>
          <el-button size="mini" type="primary" plain @click="showDecision=true" class="db-btn">
            <i class="el-icon-arrow-right"></i> 进入决策中心
          </el-button>
        </div>
      </div>

      <!-- 决策支持弹窗 -->
      <el-dialog
        title="决策支持中心"
        :visible.sync="showDecision"
        width="88%"
        :modal="true"
        :append-to-body="true"
        :close-on-click-modal="false"
        custom-class="ms-decision-dialog"
      >
        <DecisionSupport v-if="showDecision" :enterprise-id="localEntId" :enterprise-name="localEntName" :module-stats-data="moduleStatsData" />
      </el-dialog>

    </div><!-- end ms-body -->

    <!-- 底部状态栏 -->
    <div class="ms-statusbar">
      <span>最后刷新：{{ lastUpdate }}</span>
      <span class="sb-sep">|</span>
      <span>监管企业：<b class="c-cyan">286</b> 家</span>
      <span class="sb-sep">|</span>
      <span>活跃预警：<b class="c-red">{{ warningList.length }}</b> 条</span>
      <span class="sb-sep">|</span>
      <span>系统：<b class="c-green">● 正常</b></span>
    </div>

    <!-- 指标预警详情弹窗（与外层实时监控看版复用同一组件，大屏内不显示遮罩，挂到 body 脱离 overflow:hidden 裁剪） -->
    <indicator-warning-detail ref="msWarningDetail" :modal="false" :append-to-body="true" />
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { findOrganizationDataA } from '@/api/setting/org'
import { getEnterpriseInfo, getFinancialRadarData } from '@/api/risk/enterpriseProfile'
import { getRiskWarningList } from '@/api/mxgl'
import { getInvestStatistics }        from '@/api/stateAssets/investPenetration'
import { getFinancialRiskStatistics } from '@/api/stateAssets/financialRiskPenetration'
import { getProcurementStatistics }   from '@/api/stateAssets/procurementPenetration'
import { getMilitaryStatistics }      from '@/api/stateAssets/militaryPenetration'
import { getOverseasStatistics }      from '@/api/stateAssets/overseasPenetration'
import { getIndustryOverview }        from '@/api/stateAssets/industryPenetration'
import { getContractStatistics }      from '@/api/stateAssets/contractPenetration'
import { getAccountingStatistics }    from '@/api/stateAssets/accountingPenetration'
import { getFinanceStatistics }       from '@/api/stateAssets/financePenetration'
import { getFundFlowStatistics as getFundFlowPenetrationStats } from '@/api/stateAssets/fundFlow'
import { getSalaryDashboard }         from '@/api/stateAssets/salaryPenetration'
import { getPropertyDashboard }       from '@/api/stateAssets/propertyRight'
import DecisionSupport from './DecisionSupport.vue'
import IndicatorWarningDetail from '@/views/risk/home/enterpriseProfile/components/IndicatorWarningDetail.vue'

const SMAP = {
  invest:      [['项目总数','totalProjects,projectCount',''],  ['风险预警','warningCount,riskCount','danger'],  ['合规率','complianceRate','success']],
  financial:   [['融资总额','totalFinancing,financingAmount',''],['对外担保','totalGuarantee,guaranteeAmount',''],['风险数','riskCount,warningCount','danger']],
  procurement: [['采购项目','totalProjects,projectCount',''],  ['供应商数','supplierCount,totalSuppliers',''], ['风险预警','riskWarnings,warningCount','danger']],
  military:    [['任务数','taskCount,totalTasks',''],          ['资质数','qualificationCount,totalQualifications',''],['风险数','riskCount,warningCount','danger']],
  overseas:    [['境外单位','unitCount,overseasCount,totalUnits',''],['覆盖国家','countryCount,countries',''],['风险预警','riskCount,warningCount','danger']],
  industry:    [['行业数','industryCount,totalIndustries',''], ['监管企业','companyCount,enterpriseCount',''],  ['风险预警','riskCount,warningCount','danger']],
  contract:    [['合同总数','totalContracts,contractCount',''],['合同金额','totalAmount,contractAmount',''],   ['风险预警','riskCount,warningCount','danger']],
  accounting:  [['凭证总数','voucherCount,totalVouchers',''],  ['异常凭证','anomalyCount,abnormalCount','warning'],['风险预警','riskCount,warningCount','danger']],
  finance:     [['报表数','statementCount,totalStatements',''],['财务风险','riskCount,financialRisks','danger'],['合规率','complianceRate','success']],
  fund:        [['流向总数','totalFlows,flowCount',''],        ['异常流向','abnormalCount,abnormalFlows','warning'],['风险预警','riskCount,warningCount','danger']],
  salary:      [['工资总额','totalSalary,salaryTotal',''],     ['高管异常','executiveAbnormal,anomalyCount','warning'],['合规率','complianceRate','success']],
  property:    [['产权登记','registryCount,propertyCount,totalCount',''],['产权交易','tradingCount,transactionCount',''],['风险预警','riskCount,warningCount','danger']]
}

export default {
  name: 'MonitoringScreen',
  components: { DecisionSupport, IndicatorWarningDetail },
  props: {
    enterpriseName: { type: String, default: '' },
    enterpriseId:   { type: String, default: '' },
    enterpriseData: { type: Object, default: null },
    radarData:      { type: Object, default: null },
    hologramData:   { type: Object, default: null },
    moduleStatsData:{ type: Object, default: () => ({}) },
    domainModules:  { type: Array, default: () => [] }
  },
  data() {
    return {
      isFullscreen: false,
      currentTime: '', lastUpdate: '',
      loading: false, entLoading: false, warnLoading: false,
      _timer: null, _radar: null, _resizeObs: null,

      // 公司切换
      localEntId: '', localEntName: '',
      treePopVisible: false, treeFilter: '',
      treeData: [], defaultExpKeys: [],

      // 企业信息
      entInfo: {},
      keyFinancials: [
        { label:'总资产',    value:'--', cls:'cyan',  change:'--', chgType:'up' },
        { label:'营业收入',  value:'--', cls:'green', change:'--', chgType:'up' },
        { label:'净利润',    value:'--', cls:'yellow',change:'--', chgType:'up' },
        { label:'负债率',    value:'--', cls:'',      change:'--', chgType:'down' },
        { label:'净资产收益',value:'--', cls:'green', change:'--', chgType:'up' },
        { label:'研发投入',  value:'--', cls:'cyan',  change:'--', chgType:'up' }
      ],
      riskBars: [
        { label:'高风险',count:0, pct:0, color:'#ff4757' },
        { label:'中风险',count:0, pct:0, color:'#ffa500' },
        { label:'低风险',count:0, pct:0, color:'#00ff88' }
      ],

      // 12 领域
      domains: [
        { key:'invest',      name:'投资穿透',  icon:'el-icon-bank-card',       color:'#1890FF', route:'/modelMonitor/Tzjkjsc' },
        { key:'financial',   name:'金融穿透',  icon:'el-icon-bank',            color:'#13C2C2', route:'/equityPenetration/Jrfxjsc' },
        { key:'procurement', name:'采购穿透',  icon:'el-icon-shopping-cart-2', color:'#722ED1', route:'/fxyj/Cgjkjsc' },
        { key:'military',    name:'军品穿透',  icon:'el-icon-s-flag',          color:'#F5222D', route:'/riskPenetration/Jpjkjsc' },
        { key:'overseas',    name:'境外穿透',  icon:'el-icon-place',           color:'#FA8C16', route:'/enterprise/Jwjkjsc' },
        { key:'industry',    name:'行业穿透',  icon:'el-icon-office-building', color:'#52C41A', route:'/industry/HYjkjsc' },
        { key:'contract',    name:'合同穿透',  icon:'el-icon-document',        color:'#EB2F96', route:'/monitorExecute/HtJkjsc' },
        { key:'accounting',  name:'会计穿透',  icon:'el-icon-notebook-2',      color:'#2F54EB', route:'/compliance/Jkctjkjsc' },
        { key:'finance',     name:'财务穿透',  icon:'el-icon-data-analysis',   color:'#AD6800', route:'/financialPenetration/dashboard' },
        { key:'fund',        name:'资金穿透',  icon:'el-icon-money',           color:'#006D75', route:'/ruleMonitor/MXfxyjgl' },
        { key:'salary',      name:'薪酬穿透',  icon:'el-icon-user-solid',      color:'#531DAB', route:'/formMonitor/Xcjkjsc' },
        { key:'property',    name:'产权穿透',  icon:'el-icon-share',           color:'#237804', route:'/assetPenetration/Cqjkjsc' }
      ],
      moduleStats: {},
      placeholderStats: [{ label:'加载中',value:'…',cls:'' },{ label:'',value:'…',cls:'' },{ label:'',value:'…',cls:'' }],

      warningList: [],

      showDecision: false,
      decisionItems: [
        { name:'政策影响', icon:'el-icon-document', color:'#409EFF' },
        { name:'风险预测', icon:'el-icon-warning',  color:'#F56C6C' },
        { name:'投资决策', icon:'el-icon-money',    color:'#67C23A' },
        { name:'绩效评估', icon:'el-icon-trophy',   color:'#E6A23C' },
        { name:'智能推荐', icon:'el-icon-magic-stick', color:'#9C27B0' }
      ]
    }
  },

  watch: {
    treeFilter(val) { this.$refs.msOrgTree && this.$refs.msOrgTree.filter(val) },
    enterpriseId(v) { this.localEntId = v; this.refreshAll() },
    enterpriseName(v) { this.localEntName = v }
  },

  mounted() {
    this.localEntId   = this.enterpriseId
    this.localEntName = this.enterpriseName
    // 使用外面传入的路由配置
    if (this.domainModules && this.domainModules.length > 0) {
      this.domains = this.domainModules.map(m => ({
        key: m.key, name: m.name, icon: m.icon, color: m.color, route: m.dashboardRoute || m.route
      }))
    }
    this.updateTime()
    this._timer = setInterval(this.updateTime, 1000)
    this.loadTree()
    this.$nextTick(() => {
      this.initRadar()
      this.refreshAll()
    })
  },

  beforeDestroy() {
    clearInterval(this._timer)
    if (this._radar) { this._radar.dispose(); this._radar = null }
    if (this._resizeObs) this._resizeObs.disconnect()
  },

  methods: {
    /**
     * 与外层实时监控看版保持一致的标题提取逻辑：
     * 从预警描述中截取"指标组合分析预警"到"风险等级"之间的内容；
     * 若描述中不含这两个关键词则返回空字符串，由调用方回退到 warningTitle。
     */
    extractDescSummary(desc) {
      if (!desc) return ''
      const startKeyword = '指标组合分析预警'
      const endKeyword = '风险等级'
      const startIdx = desc.indexOf(startKeyword)
      if (startIdx === -1) return ''
      const endIdx = desc.indexOf(endKeyword, startIdx)
      if (endIdx === -1) return desc.slice(startIdx).trim()
      return desc.slice(startIdx, endIdx).trim()
    },

    /**
     * 点击预警列表行，弹出指标预警详情（与外层实时监控看版行为一致）
     * 将原始预警数据 w 包装成 indicator 格式传给 IndicatorWarningDetail.showDetail
     */
    showWarningDetail(w) {
      const LEVEL_MAP = { HIGH: 3, MEDIUM: 2, LOW: 1, high: 3, medium: 2, low: 1 }
      const indicator = {
        indicatorCode:  String(w.warningId || w.id || w.warningCode || Math.random()),
        indicatorName:  this.extractDescSummary(w.warningDescription || w.warningDesc || '') ||
                        w.warningTitle || w.title || w.content || '预警信息',
        currentValue:   w.warningValue != null ? String(w.warningValue) : (w.warningType || ''),
        unit:           '',
        warningLevel:   LEVEL_MAP[w.warningLevel || w.level] || 2,
        thresholdValue: w.thresholdValue != null ? String(w.thresholdValue) : '',
        showValue:      false,
        remark:         w.warningDescription || w.warningDesc || w.description || w.remark || '',
        createTime:     w.createTime || '',
        _rawWarning:    w
      }
      this.$refs.msWarningDetail.showDetail(indicator, this.localEntId, w)
    },

    updateTime() {
      const n = new Date()
      this.currentTime = n.toLocaleString('zh-CN')
      this.lastUpdate  = n.toLocaleTimeString('zh-CN')
    },

    /* ---- 公司树 ---- */
    async loadTree() {
      try {
        const res = await findOrganizationDataA({ nodeId: '' })
        if (res && Array.isArray(res) && res.length) {
          this.treeData = res
          if (res[0]) {
            this.defaultExpKeys = [res[0].id]
            if (!this.localEntId) {
              this.localEntId   = String(res[0].id)
              this.localEntName = res[0].name || res[0].label
            }
          }
        }
      } catch(e) {}
    },
    filterNode(val, data) {
      if (!val) return true
      return (data.name || data.label || '').includes(val)
    },
    onTreeNodeClick(data) {
      this.localEntId   = String(data.id)
      this.localEntName = data.name || data.label
      this.treePopVisible = false
      this.treeFilter   = ''
      this.refreshAll()
    },

    /* ---- 刷新 ---- */
    async refreshAll() {
      // 优先使用外面传入的数据
      if (this.moduleStatsData && Object.keys(this.moduleStatsData).length > 0) {
        this.moduleStats = this.moduleStatsData
        this.$nextTick(() => this.updateRadarByStats())
      } else {
        await this.loadAllModuleStats()
      }
      // 企业信息从 props 取
      if (this.enterpriseData) {
        const info = this.enterpriseData.enterpriseInfo || this.enterpriseData || {}
        const km = this.enterpriseData.keyMetrics || {}
        // 合并 keyMetrics 到 entInfo，确保监控大屏字段完整
        this.entInfo = {
          ...info,
          employeeCount: info.employeeCount || km.employeeCount || null,
          totalAssets: info.totalAssets || km.totalAssets || null,
          subsidiaryCount: info.subsidiaryCount || null
        }
      } else {
        await this.loadEntInfo()
      }
      // 关键财务指标从 radarData props 取
      if (this.radarData && this.radarData.keyIndicators) {
        const ki = this.radarData.keyIndicators
        const findKi = (code) => { const item = ki.find(k => k.indicatorCode === code); return item ? item.currentValue : null }
        const updates = [
          [findKi('TOTAL_ASSETS') || findKi('NET_ASSET_RETURN_RATE'), 0],
          [findKi('ANNUAL_REVENUE') || findKi('TOTAL_ASSET_TURNOVER'), 1],
          [findKi('NET_PROFIT') || findKi('NET_PROFIT_MARGIN'), 2],
          [findKi('ASSET_LIABILITY_RATIO'), 3],
          [findKi('CAPITAL_RETURN_RATE') || findKi('NET_ASSET_RETURN_RATE'), 4],
          [findKi('OPERATING_CASH_RATIO'), 5]
        ]
        updates.forEach(([v, i]) => {
          if (v != null) this.keyFinancials[i].value = isNaN(v) ? v : this.fmtMoney(v)
        })
      }
      await this.loadWarnings()
    },

    /* ---- 企业信息 ---- */
    async loadEntInfo() {
      if (!this.localEntId) return
      this.entLoading = true
      try {
        const [ir, rr] = await Promise.allSettled([
          getEnterpriseInfo({ enterpriseId: this.localEntId }),
          getFinancialRadarData({ enterpriseId: this.localEntId })
        ])
        if (ir.status === 'fulfilled' && ir.value?.code === 1) {
          const d = ir.value.data || {}
          const info = d.enterpriseInfo || d
          const km = d.keyMetrics || {}
          // 合并 keyMetrics 到 entInfo，确保监控大屏字段完整
          this.entInfo = {
            ...info,
            employeeCount: info.employeeCount || km.employeeCount || null,
            totalAssets: info.totalAssets || km.totalAssets || null,
            subsidiaryCount: info.subsidiaryCount || null
          }
          if (d.keyMetrics) {
            const m = d.keyMetrics
            const upd = [[m.totalAssets,0],[m.annualRevenue,1],[m.netProfit,2],[null,3],[m.roe?m.roe+'%':null,4],[m.rdExpense,5]]
            upd.forEach(([v,i]) => { if (v!=null) this.keyFinancials[i].value = isNaN(v) ? v : this.fmtMoney(v) })
          }
        }
      } catch(e) {}
      finally { this.entLoading = false }
    },

    /* ---- 预警 ---- */
    async loadWarnings() {
      this.warnLoading = true
      // 国资委作为监管主体，查全部数据；其他公司按公司名过滤（与外层实时监控看版逻辑一致）
      const isGuoZiWei = this.localEntName === '国务院国有资产监督管理委员会'
      const queryCompanyName = isGuoZiWei ? '' : (this.localEntName || '')
      try {
        const res = await getRiskWarningList({ pageNum:1, pageSize:30, warningStatus:'PENDING', companyName:queryCompanyName })
        if (res?.code===1 && res.data?.list?.length) {
          this.warningList = res.data.list
        } else this._mockWarn()
      } catch(e) { this._mockWarn() }
      finally { this.warnLoading = false }
    },
    _mockWarn() {
      this.warningList = [
        { id:1, warningLevel:'HIGH',   warningTitle:'核燃料加工厂大额资金异常流出', companyName:'中核燃料公司', modelName:'资金穿透', createTime:'10分钟前' },
        { id:2, warningLevel:'HIGH',   warningTitle:'对外担保超授权额度预警',       companyName:'中核建工集团', modelName:'金融穿透', createTime:'32分钟前' },
        { id:3, warningLevel:'MEDIUM', warningTitle:'采购合同未经三重一大审批',     companyName:'中国核电工程', modelName:'采购穿透', createTime:'1小时前' },
        { id:4, warningLevel:'MEDIUM', warningTitle:'工资总额超核定基数',           companyName:'核工业勘察院', modelName:'薪酬穿透', createTime:'2小时前' },
        { id:5, warningLevel:'LOW',    warningTitle:'会计凭证摘要不规范',           companyName:'中核华原钛白', modelName:'会计穿透', createTime:'4小时前' }
      ]
    },

    /* ---- 12领域 ---- */
    async loadAllModuleStats() {
      this.loading = true
      const orgId = this.localEntId
      const apiMap = {
        invest:getInvestStatistics, financial:getFinancialRiskStatistics,
        procurement:getProcurementStatistics, military:getMilitaryStatistics,
        overseas:getOverseasStatistics, industry:getIndustryOverview,
        contract:getContractStatistics, accounting:getAccountingStatistics,
        finance:getFinanceStatistics, fund:getFundFlowPenetrationStats,
        salary:getSalaryDashboard, property:getPropertyDashboard
      }
      await Promise.allSettled(Object.keys(apiMap).map(k => this._loadStat(k, apiMap[k], orgId)))
      this.loading = false
      this.$nextTick(() => this.updateRadarByStats())
    },
    async _loadStat(key, fn, orgId) {
      try {
        const res = await fn(orgId)
        const d   = res?.data || {}
        const map = SMAP[key] || []
        this.$set(this.moduleStats, key, map.map(([lbl,f,cls]) => ({ label:lbl, cls, value:this._fmtVal(this._pick(d,f),cls) })))
      } catch(e) {
        const map = SMAP[key]||[]
        this.$set(this.moduleStats, key, map.map(([lbl,,cls]) => ({ label:lbl, cls, value:'--' })))
      }
    },
    _pick(o,f){ for(const k of f.split(',')){ if(o[k]!=null&&o[k]!=='') return o[k] } return null },
    _fmtVal(v,cls){
      if(v==null) return '--'
      const n=Number(v)
      if(!isNaN(n)){
        if(cls==='success') return n.toFixed(1)+'%'
        if(n>=1e8) return (n/1e8).toFixed(1)+'亿'
        if(n>=1e4) return (n/1e4).toFixed(1)+'万'
        return n.toString()
      }
      return String(v)
    },
    fmtMoney(v){
      if(!v) return '--'
      const n=Number(v)
      if(isNaN(n)) return v
      if(n>=1e8) return (n/1e8).toFixed(1)+'亿'
      if(n>=1e4) return (n/1e4).toFixed(0)+'万'
      return n+'元'
    },
    domainRiskClass(k){
      const s=(this.moduleStats[k]||[]).find(x=>x.cls==='danger')
      if(!s||s.value==='--') return 'ok'
      const n=parseInt(s.value)
      return n>=8?'high':n>=3?'med':'ok'
    },
    domainRiskLabel(k){ return {high:'高风险',med:'关注',ok:'正常'}[this.domainRiskClass(k)] },
    domainHealth(k){
      const s=(this.moduleStats[k]||[]).find(x=>x.cls==='success')
      if(s&&s.value!=='--') return Math.min(100,parseFloat(s.value))
      const w=(this.moduleStats[k]||[]).find(x=>x.cls==='danger')
      if(w&&w.value!=='--') return Math.max(20,100-parseInt(w.value)*5)
      return 80
    },
    levelText(l){ return {HIGH:'高',MEDIUM:'中',LOW:'低',high:'高',medium:'中',low:'低'}[l]||'低' },
    levelRowClass(l){ return {HIGH:'rh',MEDIUM:'rm',LOW:'rl',high:'rh',medium:'rm',low:'rl'}[l]||'rl' },
    goModule(m){ this.$router.push(m.route).catch(()=>{}) },
    toggleFullscreen(){
      this.isFullscreen=!this.isFullscreen
      this.isFullscreen ? document.documentElement.requestFullscreen?.() : document.exitFullscreen?.()
      this.$nextTick(()=>{ if(this._radar) this._radar.resize() })
    },

    /* ---- ECharts 雷达 ---- */
    initRadar(){
      const el = this.$refs.radarEl
      if(!el) return
      this._radar = echarts.init(el)
      // 初始空数据，等 loadAllModuleStats 完成后 updateRadarByStats 会更新
      const init12 = [0,0,0,0,0,0,0,0,0,0,0,0]
      this._radar.setOption(this._radarOpt([init12, init12]))
      if(window.ResizeObserver){
        this._resizeObs = new ResizeObserver(()=>{ this._radar && this._radar.resize() })
        this._resizeObs.observe(el)
      }
    },
    updateRadarByStats(){
      if(!this._radar) return
      const keys = ['invest','financial','procurement','military','overseas','industry',
                    'contract','accounting','finance','fund','salary','property']
      const ent = keys.map(k => Math.round(this.domainHealth(k)))
      // 行业均值固定值（不用随机数）
      const avg = [78,75,80,76,72,79,77,81,76,74,82,80]
      this._radar.setOption(this._radarOpt([ent, avg]))
    },
    _radarOpt(vals){
      return {
        backgroundColor:'transparent',
        tooltip:{ trigger:'item' },
        radar:{
          indicator:[
            {name:'投资',max:100},{name:'金融',max:100},
            {name:'采购',max:100},{name:'军品',max:100},
            {name:'境外',max:100},{name:'行业',max:100},
            {name:'合同',max:100},{name:'会计',max:100},
            {name:'财务',max:100},{name:'资金',max:100},
            {name:'薪酬',max:100},{name:'产权',max:100}
          ],
          shape:'polygon', splitNumber:4,
          axisName:{ color:'#7ec8e3', fontSize:11, fontWeight:'bold' },
          splitLine:{ lineStyle:{color:'rgba(0,212,255,.12)'} },
          splitArea:{ areaStyle:{color:['rgba(0,212,255,.02)','rgba(0,212,255,.05)']} },
          axisLine:{ lineStyle:{color:'rgba(0,212,255,.18)'} },
          center:['50%','50%'], radius:'60%'
        },
        series:[{ type:'radar', data:[
          { value:vals[0], name:'本企业',
            lineStyle:{color:'#00d4ff',width:2}, areaStyle:{color:'rgba(0,212,255,.15)'}, itemStyle:{color:'#00d4ff'} },
          { value:vals[1], name:'行业均值',
            lineStyle:{color:'#ffd666',width:1.5,type:'dashed'}, areaStyle:{color:'rgba(255,214,102,.08)'}, itemStyle:{color:'#ffd666'} }
        ]}]
      }
    }
  }
}
</script>

<style lang="scss" scoped>
$bg:   #0a1628;
$card: rgba(255,255,255,.06);
$bd:   rgba(0,212,255,.22);
$cy:   #00d4ff;
$gn:   #00ff88;
$rd:   #ff4757;
$yw:   #ffd666;

/* ===== 根容器：100vh 全屏 ===== */
.ms-root {
  background: radial-gradient(ellipse at 50% 0%, #0d2a4a 0%, $bg 60%);
  color:#d0e8ff;
  display:flex; flex-direction:column;
  height:100%; min-height:0;
  font-family:'Microsoft YaHei','PingFang SC',sans-serif;
  font-size:13px;
  &.fullscreen { position:fixed; inset:0; z-index:9999; }
}

/* ===== 顶部标题栏 ===== */
.ms-topbar {
  display:flex; align-items:center; justify-content:space-between;
  padding:0 16px; height:44px; flex-shrink:0;
  background:linear-gradient(180deg,rgba(6,40,90,.95),rgba(10,22,40,.6));
  border-bottom:1px solid $bd;
  .tb-left  { display:flex; align-items:center; gap:8px; flex:1; }
  .tb-center{ flex:1; display:flex; justify-content:center; }
  .tb-right { flex:1; display:flex; justify-content:flex-end; align-items:center; gap:6px; }
  .sys-badge{
    background:linear-gradient(135deg,#0070f3,#00c6ff);
    padding:2px 8px; border-radius:10px; font-size:10px; color:#fff; font-weight:700;
  }
  .sys-title{
    font-size:16px; font-weight:800; margin:0;
    background:linear-gradient(90deg,$cy,$gn);
    -webkit-background-clip:text; -webkit-text-fill-color:transparent;
  }
  .sys-time { font-size:10px; color:#4a6a88; }
  .ms-btn {
    background: rgba(0,212,255,0.12) !important;
    border: 1px solid rgba(0,212,255,0.4) !important;
    color: #00d4ff !important;
    &:hover {
      background: rgba(0,212,255,0.25) !important;
      border-color: rgba(0,212,255,0.7) !important;
    }
  }
}

/* 公司切换触发器 */
.company-trigger {
  display:flex; align-items:center; gap:5px; cursor:pointer;
  background:rgba(0,212,255,.08); border:1px solid $bd; border-radius:16px;
  padding:4px 12px; transition:all .2s;
  &:hover { background:rgba(0,212,255,.15); }
  .ct-name { font-size:12px; color:$cy; font-weight:600; max-width:160px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap; }
  .ct-arrow { font-size:10px; color:#5a8aaa; }
}

/* ===== 主体：flex 列方向 ===== */
.ms-body {
  flex:1; min-height:0;
  display:flex; flex-direction:column;
  padding:8px 12px; gap:6px; overflow:hidden;
}

/* ===== 行标签 ===== */
.row-label {
  font-size:13px; font-weight:700; color:$cy; letter-spacing:.8px;
  display:flex; align-items:center; gap:5px; margin-bottom:6px;
  .warn-badge { background:$rd; color:#fff; border-radius:10px; padding:0 6px; font-size:11px; }
  .row-sub    { color:#4a6a88; font-weight:400; font-size:11px; }
}

/* ===== 通用 card ===== */
.ms-card {
  background:$card; border:1px solid $bd; border-radius:8px; padding:12px;
  min-height:0; overflow:hidden;
  .card-title { font-size:12px; color:$cy; font-weight:600; margin-bottom:8px;
    display:flex; align-items:center; gap:4px; }
}

/* ===== 上：企业驾驶舱 ===== */
.row-enterprise {
  display:grid; grid-template-columns:1fr 1.1fr 1fr; gap:8px; flex-shrink:0;
  height:320px;
  .ent-name { font-size:15px; font-weight:700; color:#e8f4ff; margin-bottom:8px; }
  .ent-tags { display:flex; flex-wrap:wrap; gap:5px; margin-bottom:0;
    .et-tag { font-size:11px; padding:3px 10px; border-radius:8px;
      background:rgba(255,255,255,.07); color:#8ab4d8;
      &.blue  { background:rgba(0,212,255,.1); color:$cy; }
      &.green { background:rgba(0,255,136,.1); color:$gn; }
      &.orange{ background:rgba(255,165,0,.1); color:#ffa500; }
    }
  }
  .ent-divider { height:1px; background:rgba(0,212,255,.12); margin:10px 0; }
  .ent-grid { display:grid; grid-template-columns:1fr 1fr; gap:8px 14px; margin-bottom:0;
    .eg-item { display:flex; flex-direction:column; gap:3px;
      .eg-l { font-size:11px; color:#3a5878; }
      .eg-v { font-size:13px; color:#c0d8f0; font-weight:500;
        &.cyan  { color:$cy; } &.yellow { color:$yw; }
      }
    }
  }
  .risk-label { font-size:11px; color:#5a8aaa; margin-bottom:6px; }
  .risk-row { display:flex; align-items:center; gap:8px; margin-bottom:5px;
    &:last-child { margin-bottom:0; }
    .rr-l { width:36px; font-size:11px; color:#5a7a98; flex-shrink:0; }
    .rr-bar { flex:1; height:4px; background:rgba(255,255,255,.06); border-radius:2px; overflow:hidden;
      .rr-fill { height:100%; border-radius:2px; transition:width .4s; }
    }
    .rr-v { width:20px; text-align:right; font-size:11px; font-weight:600; }
  }
  .ent-center {
    display:flex; flex-direction:column;
    .radar-el { flex:1; min-height:120px; height:0; }
    .radar-legend { display:flex; justify-content:center; gap:14px; padding-top:6px;
      .rl-item { font-size:12px; color:#8ab4d8; display:flex; align-items:center; gap:4px;
        .rl-dot { width:8px; height:8px; border-radius:50%; display:inline-block; }
      }
    }
  }
  .ent-right {
    .kf-grid { display:grid; grid-template-columns:1fr 1fr; gap:10px;
      .kf-item { background:rgba(0,0,0,.2); border-radius:8px; padding:10px 12px;
        display:flex; align-items:center; gap:10px;
        .kf-val { font-size:18px; font-weight:700; color:$cy; flex-shrink:0;
          &.green { color:$gn; } &.yellow { color:$yw; }
        }
        .kf-info { display:flex; flex-direction:column; gap:2px;
          .kf-lbl { font-size:11px; color:#3a5878; }
          .kf-chg { font-size:11px;
            &.up { color:$gn; } &.down { color:$rd; }
          }
        }
      }
    }
  }
}

/* ===== 中：12领域（flex:1 填充剩余空间）===== */
.row-domains { flex:1; min-height:0; display:flex; flex-direction:column;
  .domain-grid { flex:1; display:grid; grid-template-columns:repeat(6,1fr); gap:8px;
    .dc {
      background:$card; border:1px solid $bd; border-top:3px solid var(--ac);
      border-radius:8px; padding:12px 10px; cursor:pointer; transition:all .2s;
      display:flex; flex-direction:column; justify-content:space-between;
      &:hover { background:rgba(0,212,255,.07); transform:translateY(-2px); box-shadow:0 4px 16px rgba(0,0,0,.4); }
      .dc-head { display:flex; align-items:center; gap:6px; margin-bottom:8px;
        .dc-icon { width:26px; height:26px; border-radius:6px; display:flex; align-items:center; justify-content:center; font-size:14px; flex-shrink:0; }
        .dc-name { font-size:13px; font-weight:600; color:#d0e8ff; flex:1; }
        .dc-badge { font-size:10px; padding:2px 6px; border-radius:8px; flex-shrink:0;
          &.ok   { background:rgba(0,255,136,.1); color:$gn; }
          &.med  { background:rgba(255,165,0,.1);  color:#ffa500; }
          &.high { background:rgba(255,71,87,.1);  color:$rd; }
        }
      }
      .dc-stats { display:flex; gap:4px; margin-bottom:8px;
        .dcs { flex:1; text-align:center; background:rgba(0,0,0,.2); border-radius:4px; padding:5px 2px;
          .dcs-v { font-size:13px; font-weight:700; color:$cy;
            &.danger  { color:$rd; } &.warning { color:$yw; } &.success { color:$gn; }
          }
          .dcs-l { font-size:10px; color:#2a4860; margin-top:2px; }
        }
      }
      .dc-prog { height:3px; background:rgba(255,255,255,.05); border-radius:2px; overflow:hidden;
        .dc-prog-f { height:100%; border-radius:2px; transition:width .4s; }
      }
    }
  }
}

/* ===== 下：预警表格 ===== */
.row-warnings { flex-shrink:0; display:flex; flex-direction:column;
  .warn-table { flex:1; min-height:0; display:flex; flex-direction:column;
    background:$card; border:1px solid $bd; border-radius:6px; overflow:hidden;
    .wt-head, .wt-row {
      display:flex; align-items:center;
      span { padding:6px 10px; font-size:12px; flex-shrink:0; }
      .w-lv { width:60px; text-align:center; }
      .w-ti { flex:1; }
      .w-co { width:140px; }
      .w-do { width:84px; }
      .w-tm { width:90px; color:#3a5878; }
      .w-st { width:68px; text-align:center; }
    }
    .wt-head { background:rgba(0,212,255,.06); border-bottom:1px solid $bd; flex-shrink:0;
      span { color:#3a6080; font-weight:600; font-size:11px; letter-spacing:.5px; }
    }
    .wt-body {
      max-height: 160px;
      overflow-y: auto;
      // 细滚动条，与大屏暗色风格一致
      &::-webkit-scrollbar { width:3px; }
      &::-webkit-scrollbar-track { background:transparent; }
      &::-webkit-scrollbar-thumb { background:rgba(0,212,255,.3); border-radius:2px; }
      &::-webkit-scrollbar-thumb:hover { background:rgba(0,212,255,.55); }
      .wt-row { border-bottom:1px solid rgba(255,255,255,.03);
        &:last-child { border:none; }
        &.rh { background:rgba(255,71,87,.04); }
        &.rm { background:rgba(255,165,0,.04); }
        .lv-tag { display:inline-block; padding:2px 8px; border-radius:8px; font-size:11px; font-weight:700;
          &.lv-high   { background:rgba(255,71,87,.2);  color:$rd; }
          &.lv-medium { background:rgba(255,165,0,.2);  color:#ffa500; }
          &.lv-low    { background:rgba(0,212,255,.12); color:$cy; }
        }
        .w-st { font-size:11px; color:#ffa500; }
      }
      .wt-empty { padding:12px; text-align:center; color:$gn; font-size:12px; }
    }
  }
}

/* ===== 尾：决策支持预览条 ===== */
.row-decision { flex-shrink:0;
  .decision-bar {
    display:flex; align-items:center; gap:12px;
    background:rgba(0,30,80,.4); border:1px solid $bd;
    border-radius:8px; padding:8px 14px;
    .db-icon  { font-size:16px; color:$cy; flex-shrink:0; }
    .db-title { font-size:12px; font-weight:700; color:#d0e8ff; white-space:nowrap; }
    .db-sub   { font-size:10px; color:#3a5878; white-space:nowrap; }
    .db-left  { display:flex; align-items:center; gap:6px; flex-shrink:0; }
    .db-items { flex:1; display:flex; gap:8px; flex-wrap:wrap;
      .db-item {
        font-size:10px; padding:3px 10px; border-radius:12px;
        background:rgba(255,255,255,.05); color:#8ab4d8;
        border:1px solid rgba(255,255,255,.1);
        i { color:var(--dc); margin-right:3px; }
        cursor:pointer; transition:all .2s;
        &:hover { background:rgba(0,212,255,.1); color:$cy; }
      }
    }
    .db-btn { flex-shrink:0; }
  }
}

/* ===== 底部状态栏 ===== */
.ms-statusbar {
  padding:4px 16px; border-top:1px solid $bd; flex-shrink:0;
  font-size:10px; color:#3a5878; display:flex; align-items:center;
  .sb-sep { margin:0 10px; }
  .c-cyan  { color:$cy; }
  .c-green { color:$gn; }
  .c-red   { color:$rd; }
}

/* 滚动条 */
::-webkit-scrollbar { width:3px; }
::-webkit-scrollbar-track { background:transparent; }
::-webkit-scrollbar-thumb { background:rgba(0,212,255,.18); border-radius:2px; }
</style>

<!-- 全局：公司树 Popper 样式 -->
<style>
.ms-company-popper {
  background:#0d1f3c !important;
  border:1px solid rgba(0,212,255,.25) !important;
  padding:10px !important;
}
.ms-company-popper .el-tree {
  background:transparent;
  color:#c0d8f0;
}
.ms-company-popper .el-tree-node__content:hover { background:rgba(0,212,255,.1); }
.ms-company-popper .el-tree-node.is-current > .el-tree-node__content { background:rgba(0,212,255,.15); color:#00d4ff; }
.ms-tree-node { font-size:11px; display:flex; align-items:center; gap:4px; }
.ms-tree-wrap .el-input__inner {
  background:rgba(255,255,255,.06) !important;
  border-color:rgba(0,212,255,.2) !important;
  color:#c0d8f0 !important;
}

/* 决策支持中心弹窗 */
.ms-decision-dialog {
  border-radius:10px;
  overflow:hidden;
}
.ms-decision-dialog .el-dialog__header {
  background:linear-gradient(90deg,#1e3c72,#2a5298);
  padding:12px 20px;
  border-bottom:2px solid #ffd54f;
}
.ms-decision-dialog .el-dialog__title { color:#fff; font-weight:700; letter-spacing:1px; }
.ms-decision-dialog .el-dialog__headerbtn .el-dialog__close { color:rgba(255,255,255,.8); }
.ms-decision-dialog .el-dialog__body { padding:0; max-height:80vh; overflow-y:auto; }
</style>
