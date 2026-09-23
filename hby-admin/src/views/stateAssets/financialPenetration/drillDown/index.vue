<template>
  <div class="fin-drill-wrap" v-loading="loading" element-loading-text="数据加载中...">
    <!-- Banner -->
    <div class="page-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-left">
        <div class="banner-title">财务穿透分析</div>
        <div class="banner-sub">四层穿透：集团合并报表→企业财务概览→财务维度指标→科目明细凭证，层层下钻</div>
      </div>
      <div class="banner-right">
        <el-button size="small" icon="el-icon-refresh" @click="handleRefresh" plain>刷新</el-button>
        <el-button size="small" icon="el-icon-download" type="primary" @click="handleExport" plain>导出</el-button>
      </div>
    </div>

    <!-- 筛选条件 -->
    <el-card shadow="never" class="compact-card" style="margin-bottom:16px">
      <el-form :inline="true" :model="filterForm" size="small" class="filter-form">
        <el-form-item label="报告期">
          <el-select v-model="filterForm.period" placeholder="全部" clearable style="width:140px" @change="handleFilter">
            <el-option v-for="p in periodOptions" :key="p" :label="p" :value="p"/>
          </el-select>
        </el-form-item>
        <el-form-item label="企业名称" v-if="currentLevel===0">
          <el-input v-model="filterForm.companyName" placeholder="搜索企业" clearable style="width:180px" @input="handleFilterEnterprise"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleFilter">查询</el-button>
          <el-button icon="el-icon-refresh-left" @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 面包屑 + 穿透步骤条 -->
    <el-card shadow="never" class="compact-card" style="margin-bottom:16px">
      <div style="display:flex;align-items:center;gap:16px;flex-wrap:wrap">
        <el-button v-if="currentLevel>0" size="mini" icon="el-icon-back" @click="goBack" plain>返回上层</el-button>
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item @click.native="goLevel(0)" style="cursor:pointer">集团总览</el-breadcrumb-item>
          <el-breadcrumb-item v-if="currentLevel>=1" @click.native="goLevel(1)" style="cursor:pointer">{{ activeEnterprise.name }}</el-breadcrumb-item>
          <el-breadcrumb-item v-if="currentLevel>=2" @click.native="goLevel(2)" style="cursor:pointer">{{ activeDimension.label }}</el-breadcrumb-item>
          <el-breadcrumb-item v-if="currentLevel>=3">{{ activeSubject }}</el-breadcrumb-item>
        </el-breadcrumb>
        <el-steps :active="currentLevel" simple style="flex:1;min-width:400px">
          <el-step title="集团总览" icon="el-icon-office-building"/>
          <el-step title="企业财务" icon="el-icon-data-analysis"/>
          <el-step title="维度指标" icon="el-icon-s-finance"/>
          <el-step title="科目明细" icon="el-icon-document"/>
        </el-steps>
      </div>
    </el-card>

    <!-- 第0层：集团企业卡片 -->
    <template v-if="currentLevel===0">
      <el-row :gutter="16">
        <el-col v-for="ent in enterprises" :key="ent.id" :span="8" style="margin-bottom:16px">
          <el-card class="ent-card" shadow="hover" @click.native="drillToEnterprise(ent)">
            <div class="ent-header">
              <span class="ent-name">{{ ent.name }}</span>
              <el-badge :value="ent.warnCount" :hidden="!ent.warnCount" type="danger">
                <el-tag :type="ent.riskLevel==='HIGH'?'danger':ent.riskLevel==='MEDIUM'?'warning':'success'" size="small">
                  {{ ent.riskLevel==='HIGH'?'高风险':ent.riskLevel==='MEDIUM'?'中风险':'低风险' }}
                </el-tag>
              </el-badge>
            </div>
            <el-row :gutter="8" style="margin-top:10px">
              <el-col :span="12">
                <div class="ent-kpi-item">
                  <span class="kpi-lbl">营业收入</span>
                  <span class="kpi-val" style="color:#1677FF">{{ formatWan(ent.revenue) }}</span>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="ent-kpi-item">
                  <span class="kpi-lbl">净利润</span>
                  <span class="kpi-val" style="color:#52C41A">{{ formatWan(ent.netProfit) }}</span>
                </div>
              </el-col>
            </el-row>
            <div style="margin-top:8px">
              <div style="display:flex;justify-content:space-between;font-size:12px;margin-bottom:4px">
                <span>资产负债率</span>
                <span :style="{color:ent.debtRatio>70?'#F5222D':ent.debtRatio>60?'#FA8C16':'#52C41A', fontWeight:'700'}">{{ ent.debtRatio }}%</span>
              </div>
              <el-progress :percentage="Math.min(ent.debtRatio, 100)" :stroke-width="6"
                :color="ent.debtRatio>70?'#F5222D':ent.debtRatio>60?'#FA8C16':'#52C41A'"/>
            </div>
            <div style="margin-top:6px;font-size:12px;color:#999">点击查看企业财务详情 →</div>
          </el-card>
        </el-col>
      </el-row>
    </template>

    <!-- 第1层：企业财务维度选择 -->
    <template v-if="currentLevel===1">
      <el-row :gutter="16" style="margin-bottom:16px">
        <el-col v-for="kpi in activeEnterprise.kpis" :key="kpi.key" :span="6">
          <el-card class="kpi-card" shadow="hover">
            <div class="kpi-inner">
              <div class="kpi-icon" :style="{background:kpi.color+'18'}"><i :class="kpi.icon" :style="{color:kpi.color,fontSize:'24px'}"/></div>
              <div>
                <div class="kpi-value" :style="{color:kpi.color}">{{ kpi.value }}</div>
                <div class="kpi-label">{{ kpi.label }}</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="16">
        <el-col v-for="dim in drillDimensions" :key="dim.key" :span="6" style="margin-bottom:16px">
          <el-card class="dim-card" shadow="hover" @click.native="drillToDimension(dim)">
            <div class="dim-inner">
              <div class="dim-icon" :style="{background:dim.color+'18'}"><i :class="dim.icon" :style="{color:dim.color,fontSize:'28px'}"/></div>
              <div class="dim-info">
                <div class="dim-label">{{ dim.label }}</div>
                <div class="dim-desc">{{ dim.desc }}</div>
              </div>
              <i class="el-icon-arrow-right" style="color:#bbb;margin-left:auto"/>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </template>

    <!-- 第2层：维度指标明细 -->
    <template v-if="currentLevel===2">
      <el-card shadow="never" style="margin-bottom:16px">
        <div slot="header" class="card-header">{{ activeDimension.label }}——{{ activeEnterprise.name }}（{{ currentPeriodLabel }}）</div>
        <el-table :data="dimensionData" size="small" border :row-class-name="dimRowClass">
          <el-table-column label="科目/指标" prop="subject" min-width="200">
            <template slot-scope="{row}">
              <span :style="{fontWeight:row.isTotal?'700':'normal', cursor:row.drillable?'pointer':'default', color:row.drillable?'#1677FF':'inherit'}"
                @click="row.drillable && drillToSubject(row)">{{ row.subject }}</span>
            </template>
          </el-table-column>
          <el-table-column label="金额/数值" prop="value" width="150" align="right">
            <template slot-scope="{row}">
              <span :style="{fontWeight:row.isTotal?'700':'normal'}">{{ formatNum(row.value) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="上期" prop="prevValue" width="130" align="right">
            <template slot-scope="{row}">{{ formatNum(row.prevValue) }}</template>
          </el-table-column>
          <el-table-column label="同比" prop="yoy" width="100" align="center">
            <template slot-scope="{row}">
              <span v-if="row.yoy!=null" :style="{color:row.yoy>0?'#52C41A':'#F5222D',fontWeight:'700'}">
                {{ row.yoy>0?'+':'' }}{{ row.yoy }}%
              </span>
            </template>
          </el-table-column>
          <el-table-column label="异常标注" prop="anomaly" width="180">
            <template slot-scope="{row}">
              <el-tag v-if="row.anomaly" type="danger" size="mini">{{ row.anomaly }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </template>

    <!-- 第3层：科目明细 -->
    <template v-if="currentLevel===3">
      <el-card shadow="never">
        <div slot="header" class="card-header">{{ activeSubject }}——业务凭证明细（{{ activeEnterprise.name }}）</div>
        <el-table :data="subjectDetails" size="small" border>
          <template slot="empty">
            <div style="padding:30px 0;color:#999">
              <i class="el-icon-document" style="font-size:40px;display:block;margin-bottom:8px"></i>
              暂无凭证明细数据
            </div>
          </template>
          <el-table-column label="凭证编号" prop="voucherNo" width="150"/>
          <el-table-column label="业务日期" prop="bizDate" width="120"/>
          <el-table-column label="摘要" prop="summary" min-width="200" show-overflow-tooltip/>
          <el-table-column label="对方科目" prop="counterSubject" min-width="150"/>
          <el-table-column label="借方金额（万元）" prop="debitAmount" width="150" align="right">
            <template slot-scope="{row}">{{ row.debitAmount ? row.debitAmount.toLocaleString() : '-' }}</template>
          </el-table-column>
          <el-table-column label="贷方金额（万元）" prop="creditAmount" width="150" align="right">
            <template slot-scope="{row}">{{ row.creditAmount ? row.creditAmount.toLocaleString() : '-' }}</template>
          </el-table-column>
          <el-table-column label="异常标注" prop="anomaly" width="130">
            <template slot-scope="{row}">
              <el-tag v-if="row.anomaly" type="danger" size="mini">{{ row.anomaly }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
        <div style="margin-top:10px;text-align:right;color:#999;font-size:12px">
          穿透路径：集团总览 → {{ activeEnterprise.name }} → {{ activeDimension.label }} → {{ activeSubject }}
        </div>
      </el-card>
    </template>
  </div>
</template>

<script>
import { getFinancialDrillData, exportFinancialDrillData } from '@/api/stateAssets/financialPenetration'
import { mapGetters } from 'vuex'

export default {
  name: 'FinancialDrillDown',
  data() {
    return {
      currentLevel: 0,
      enterprises: [],
      allEnterprises: [],
      activeEnterprise: {},
      activeDimension: {},
      activeSubject: '',
      filterForm: {
        period: '',
        companyName: '',
      },
      periodOptions: [],
      drillDimensions: [
        { key: 'debt', label: '偿债能力', icon: 'el-icon-bank-card', color: '#F5222D', desc: '资产负债率/流动比率' },
        { key: 'profit', label: '盈利能力', icon: 'el-icon-trend-charts', color: '#52C41A', desc: 'ROE/净利润率/ROA' },
        { key: 'expense', label: '费用管控', icon: 'el-icon-money', color: '#FA8C16', desc: '三项费用率详情' },
        { key: 'cashflow', label: '现金流质量', icon: 'el-icon-s-finance', color: '#722ED1', desc: '利润/现金流背离分析' },
      ],
      dimensionData: [],
      subjectDetails: [],
      loading: false,
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
    currentPeriodLabel() {
      if (this.filterForm.period) {
        return this.filterForm.period.replace('-Y', '年年报').replace('-Q', '季报')
      }
      return '最新报告期'
    },
  },
  async mounted() {
    await this.loadEnterprises()
  },
  methods: {
    formatNum(v) {
      if (v == null || v === '') return '-'
      const n = Number(v)
      if (isNaN(n)) return v
      if (Math.abs(n) >= 10000) return (n / 10000).toFixed(2) + '万'
      return n.toLocaleString('zh-CN', { maximumFractionDigits: 2 })
    },
    formatWan(v) {
      if (v == null || v === '') return '-'
      const n = Number(v)
      if (isNaN(n)) return v
      if (n >= 10000) return (n / 10000).toFixed(2) + '亿'
      return n.toLocaleString('zh-CN', { maximumFractionDigits: 2 }) + '万'
    },
    async loadEnterprises() {
      this.loading = true
      try {
        const params = { level: 0 }
        if (this.filterForm.period) params.period = this.filterForm.period
        const res = await getFinancialDrillData(params)
        if (res.data) {
          const d = res.data
          if (d.children && d.children.length) {
            const list = d.children.map(c => {
              const revenue = Number(c.revenue) || 0
              const netProfit = Number(c.netProfit) || 0
              const debtRatio = Number(c.debtRatio) || 0
              const roe = Number(c.roe) || 0
              return {
                id: c.id || c.companyId,
                name: c.name || c.companyName,
                period: c.period || '',
                revenue, netProfit, debtRatio, roe,
                riskLevel: c.riskLevel || 'LOW',
                warnCount: c.warnCount || 0,
                kpis: [
                  { key: 'revenue', label: '营业收入', value: this.formatWan(revenue), color: '#1677FF', icon: 'el-icon-money' },
                  { key: 'netProfit', label: '净利润', value: this.formatWan(netProfit), color: '#52C41A', icon: 'el-icon-trend-charts' },
                  { key: 'debtRatio', label: '资产负债率', value: debtRatio + '%', color: debtRatio > 70 ? '#F5222D' : debtRatio > 60 ? '#FA8C16' : '#52C41A', icon: 'el-icon-bank-card' },
                  { key: 'roe', label: 'ROE', value: roe + '%', color: roe < 5 ? '#F5222D' : roe < 8 ? '#FA8C16' : '#52C41A', icon: 'el-icon-data-analysis' },
                ],
              }
            })
            this.allEnterprises = list
            this.enterprises = list
            // 提取报告期选项
            const periods = [...new Set(list.map(e => e.period).filter(Boolean))]
            if (periods.length && !this.periodOptions.length) {
              this.periodOptions = periods.sort().reverse()
            }
          }
        }
      } catch (e) { console.error('加载穿透数据失败', e) }
      this.loading = false
    },
    handleFilterEnterprise() {
      const keyword = (this.filterForm.companyName || '').trim()
      if (!keyword) {
        this.enterprises = this.allEnterprises
      } else {
        this.enterprises = this.allEnterprises.filter(e => e.name && e.name.indexOf(keyword) !== -1)
      }
    },
    handleFilter() {
      if (this.currentLevel === 0) {
        this.loadEnterprises()
      } else if (this.currentLevel === 1) {
        this.loadEnterpriseKpis()
      } else if (this.currentLevel === 2) {
        this.drillToDimension(this.activeDimension)
      } else if (this.currentLevel === 3) {
        this.drillToSubject({ subject: this.activeSubject })
      }
    },
    resetFilter() {
      this.filterForm = { period: '', companyName: '' }
      this.handleFilter()
    },
    goBack() {
      if (this.currentLevel > 0) {
        this.currentLevel = this.currentLevel - 1
      }
    },
    goLevel(lv) { this.currentLevel = lv },
    async drillToEnterprise(ent) {
      this.activeEnterprise = ent
      this.currentLevel = 1
      await this.loadEnterpriseKpis()
    },
    /**
     * 调用后端 Level 1 接口获取企业最新 KPI 数据
     */
    async loadEnterpriseKpis() {
      if (!this.activeEnterprise || !this.activeEnterprise.id) return
      this.loading = true
      try {
        const params = { level: 1, companyId: this.activeEnterprise.id }
        if (this.filterForm.period) params.period = this.filterForm.period
        const res = await getFinancialDrillData(params)
        if (res.data) {
          const d = res.data
          const revenue = Number(d.revenue) || 0
          const netProfit = Number(d.netProfit) || 0
          const debtRatio = Number(d.debtRatio) || 0
          const roe = Number(d.roe) || 0
          // 更新 activeEnterprise 的 KPI 数据
          this.activeEnterprise = {
            ...this.activeEnterprise,
            revenue,
            netProfit,
            debtRatio,
            roe,
            kpis: [
              { key: 'revenue', label: '营业收入', value: this.formatWan(revenue), color: '#1677FF', icon: 'el-icon-money' },
              { key: 'netProfit', label: '净利润', value: this.formatWan(netProfit), color: '#52C41A', icon: 'el-icon-trend-charts' },
              { key: 'debtRatio', label: '资产负债率', value: debtRatio + '%', color: debtRatio > 70 ? '#F5222D' : debtRatio > 60 ? '#FA8C16' : '#52C41A', icon: 'el-icon-bank-card' },
              { key: 'roe', label: 'ROE', value: roe + '%', color: roe < 5 ? '#F5222D' : roe < 8 ? '#FA8C16' : '#52C41A', icon: 'el-icon-data-analysis' },
            ],
          }
        }
      } catch (e) { console.error('加载企业KPI数据失败', e) }
      this.loading = false
    },
    async drillToDimension(dim) {
      this.activeDimension = dim
      this.currentLevel = 2
      this.loading = true
      try {
        const params = { level: 2, companyId: this.activeEnterprise.id, dimension: dim.key }
        if (this.filterForm.period) params.period = this.filterForm.period
        const res = await getFinancialDrillData(params)
        if (res.data && res.data.dimensionData && res.data.dimensionData.length) {
          this.dimensionData = res.data.dimensionData
          this.loading = false
          return
        }
      } catch (e) { console.error('加载维度数据失败', e) }
      this.dimensionData = []
      this.loading = false
    },
    async drillToSubject(row) {
      this.activeSubject = row.subject
      this.currentLevel = 3
      this.subjectDetails = []
      this.loading = true
      try {
        const params = {
          level: 3,
          companyId: this.activeEnterprise.id,
          dimension: this.activeDimension.key,
          subject: row.subject,
        }
        if (this.filterForm.period) params.period = this.filterForm.period
        const res = await getFinancialDrillData(params)
        if (res.data && res.data.subjectDetails && res.data.subjectDetails.length) {
          this.subjectDetails = res.data.subjectDetails
        }
      } catch (e) { console.error('加载科目明细失败', e) }
      this.loading = false
    },
    handleRefresh() {
      if (this.currentLevel === 0) {
        this.loadEnterprises()
      } else if (this.currentLevel === 1) {
        this.loadEnterpriseKpis()
      } else if (this.currentLevel === 2) {
        this.drillToDimension(this.activeDimension)
      } else if (this.currentLevel === 3) {
        this.drillToSubject({ subject: this.activeSubject })
      }
      this.$message.success('数据已刷新')
    },
    async handleExport() {
      this.loading = true
      try {
        const params = { level: this.currentLevel }
        if (this.activeEnterprise.id) params.companyId = this.activeEnterprise.id
        if (this.activeDimension.key) params.dimension = this.activeDimension.key
        if (this.activeSubject) params.subject = this.activeSubject
        if (this.filterForm.period) params.period = this.filterForm.period
        const res = await exportFinancialDrillData(params)
        // 响应拦截器对 blob 类型返回 { data, headers, status }
        const blobData = res.data || res
        const blob = new Blob([blobData], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '财务穿透分析_' + new Date().getTime() + '.xlsx'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (e) {
        console.error('导出失败', e)
        this.$message.error('导出失败')
      }
      this.loading = false
    },
    dimRowClass({ row }) {
      if (row.anomaly) return 'row-danger'
      if (row.isTotal) return 'row-total'
      return ''
    },
  },
}
</script>

<style scoped lang="scss">
.fin-drill-wrap {
  padding: 16px;
  background: #f5f7fa;
  min-height: 100vh;
}

/* 紧凑卡片：减少内边距，消除空白感 */
.compact-card {
  ::v-deep .el-card__body {
    padding: 12px 20px;
  }
}

/* 筛选表单：去掉 form-item 默认下边距，避免多余空白 */
.filter-form {
  ::v-deep .el-form-item {
    margin-bottom: 0;
  }
}

.page-banner {
  border-radius: 8px;
  padding: 20px 28px;
  margin-bottom: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;

  .banner-left {
    flex: 1;
  }

  .banner-title {
    font-size: 22px;
    font-weight: 700;
    color: #fff;
    margin-bottom: 6px;
  }

  .banner-sub {
    font-size: 13px;
    color: rgba(255, 255, 255, 0.78);
  }

  .banner-right {
    display: flex;
    gap: 8px;
  }
}

/* 第0层：企业卡片 */
.ent-card {
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 6px 20px rgba(22, 119, 255, 0.15);
  }
}

.ent-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.ent-name {
  font-size: 15px;
  font-weight: 700;
  color: #333;
}

.ent-kpi-item {
  padding: 6px 0;
}

.kpi-lbl {
  font-size: 12px;
  color: #666;
  display: block;
}

.kpi-val {
  font-size: 18px;
  font-weight: 700;
}

/* 第1层：KPI 卡片 */
.kpi-card {
  border-radius: 8px;
  margin-bottom: 14px;
}

.kpi-inner {
  display: flex;
  align-items: center;
  gap: 12px;
}

.kpi-icon {
  width: 46px;
  height: 46px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.kpi-value {
  font-size: 20px;
  font-weight: 700;
}

.kpi-label {
  font-size: 12px;
  color: #666;
  margin-top: 2px;
}

/* 第1层：维度卡片 */
.dim-card {
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 14px rgba(22, 119, 255, 0.12);
  }
}

.dim-inner {
  display: flex;
  align-items: center;
  gap: 12px;
}

.dim-icon {
  width: 50px;
  height: 50px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.dim-info {
  flex: 1;
}

.dim-label {
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.dim-desc {
  font-size: 12px;
  color: #999;
  margin-top: 2px;
}

/* 第2/3层：表格卡片 */
.card-header {
  font-weight: 600;
  color: #333;
  display: flex;
  align-items: center;
  gap: 8px;
}

/* 表格行高亮 */
::v-deep .row-danger td {
  background: #FFF1F0 !important;
}

::v-deep .row-total td {
  background: #E6F4FF !important;
  font-weight: 700;
}
</style>
