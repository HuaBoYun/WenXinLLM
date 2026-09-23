<template>
  <div class="stmt-query-wrap">
    <!-- Banner -->
    <div class="page-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-left">
        <div class="banner-title">财务报表穿透查询</div>
        <div class="banner-sub">从集团合并报表逐层穿透到各级子企业单体报表，点击蓝色合并数即可查看子企业构成明细</div>
      </div>
    </div>

    <!-- 查询工具栏 -->
    <el-card shadow="never" style="margin-bottom:14px">
      <el-form :inline="true" :model="queryForm" size="small">
        <el-form-item label="报表类型">
          <el-select v-model="queryForm.stmtType" @change="onStmtTypeChange" style="width:160px" clearable placeholder="请选择">
            <el-option v-for="item in stmtTypeOptions" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="报告期">
          <el-select v-model="queryForm.period" style="width:160px" clearable placeholder="请选择报告期">
            <el-option v-for="item in periodOptions" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="企业名称">
          <el-input v-model="queryForm.companyName" placeholder="请输入企业名称" style="width:160px" clearable/>
        </el-form-item>
        <el-form-item label="审计状态">
          <el-select v-model="queryForm.auditStatus" style="width:140px" clearable placeholder="请选择">
            <el-option v-for="item in auditStatusOptions" :key="item.value" :label="item.label" :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="loadData">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 科目穿透表 -->
    <el-card shadow="never">
      <div slot="header" class="card-header">
        <span>{{ stmtTypeLabel }}{{ queryForm.period ? '（' + queryForm.period + '）' : '' }}</span>
        <el-tag size="small" type="info" style="margin-left:10px">合并汇总 - 点击蓝色数字穿透查看子企业构成</el-tag>
      </div>

      <el-table :data="subjectTree" size="small" border row-key="id"
        :tree-props="{children:'children'}" default-expand-all
        :row-class-name="rowClass">
        <el-table-column label="报表科目" prop="subject" min-width="200">
          <template slot-scope="{row}">
            <span :style="{paddingLeft: (row.level||0)*16+'px', fontWeight: row.level===0?'700':'normal'}">{{ row.subject }}</span>
          </template>
        </el-table-column>
        <el-table-column label="合并数（万元）" prop="consolidatedAmount" width="160" align="right">
          <template slot-scope="{row}">
            <span v-if="row.drillable" class="drill-btn" @click="openDrill(row)">{{ formatNum(row.consolidatedAmount) }}</span>
            <span v-else>{{ formatNum(row.consolidatedAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="企业数" prop="companyCount" width="80" align="center" />
        <el-table-column label="占比最高企业" prop="topCompany" width="200" show-overflow-tooltip />
        <el-table-column label="占比" prop="topRatio" width="100" align="center">
          <template slot-scope="{row}">
            <span v-if="row.topRatio" style="color:#1677FF;font-weight:600">{{ row.topRatio }}%</span>
          </template>
        </el-table-column>
        <el-table-column label="异常标注" prop="anomaly" width="120">
          <template slot-scope="{row}">
            <el-tag v-if="row.anomaly" type="danger" size="mini">{{ row.anomaly }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 穿透浮层：子企业构成明细 -->
    <el-dialog :title="`${drillRow.subject || ''}——子企业构成明细`" :visible.sync="drillVisible" width="800px">
      <div v-if="drillRow.subject" style="margin-bottom:12px">
        <el-tag size="small">合并数合计：<b style="color:#F5222D">{{ formatNum(drillRow.consolidatedAmount) }}</b> 万元</el-tag>
        <el-tag size="small" type="info" style="margin-left:8px">共 {{ drillDetails.length }} 家子企业</el-tag>
      </div>
      <el-table :data="drillDetails" size="small" border>
        <el-table-column label="子企业名称" prop="companyName" min-width="180" show-overflow-tooltip />
        <el-table-column label="金额（万元）" prop="amount" width="150" align="right">
          <template slot-scope="{row}"><span style="fontWeight:700">{{ formatNum(row.amount) }}</span></template>
        </el-table-column>
        <el-table-column label="占合并比" prop="proportion" width="100" align="center">
          <template slot-scope="{row}"><span style="color:#1677FF">{{ row.proportion }}%</span></template>
        </el-table-column>
        <el-table-column label="审计状态" prop="auditStatus" width="90" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.auditStatus==='已审计'?'success':'warning'" size="mini">{{ row.auditStatus }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="资产负债率" prop="debtRatio" width="100" align="right">
          <template slot-scope="{row}">
            <span :style="{color: row.debtRatio>70?'#F5222D':'#303133', fontWeight: row.debtRatio>70?'700':'normal'}">{{ row.debtRatio }}%</span>
          </template>
        </el-table-column>
        <el-table-column label="风险标注" prop="risk" width="100">
          <template slot-scope="{row}">
            <el-tag v-if="row.risk" :type="row.risk==='HIGH'?'danger':'warning'" size="mini">
              {{ row.risk==='HIGH'?'高风险':'需关注' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import { getFinanceStatementList, getFinanceStatementPeriods, getFinanceStatementTypes, getFinanceAuditStatuses } from '@/api/stateAssets/financePenetration'
import { mapGetters } from 'vuex'

export default {
  name: 'FinancialStatementQuery',
  data() {
    return {
      queryForm: { stmtType: '', period: '', companyName: '', auditStatus: '', pageNumber: 1, pageSize: 50 },
      stmtTypeOptions: [],
      periodOptions: [],
      auditStatusOptions: [],
      rawList: [],
      subjectTree: [],
      drillVisible: false,
      drillRow: {},
      drillDetails: [],
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
    stmtTypeLabel() {
      if (this.queryForm.stmtType) return this.queryForm.stmtType
      return '全部报表'
    },
  },
  methods: {
    /** 从数据库加载可用报表类型选项 */
    async loadStmtTypes() {
      try {
        const res = await getFinanceStatementTypes()
        if (res.data && Array.isArray(res.data)) {
          this.stmtTypeOptions = res.data
        } else {
          this.stmtTypeOptions = []
        }
      } catch (e) {
        console.error('加载报表类型失败', e)
        this.stmtTypeOptions = []
      }
    },
    /** 从数据库加载可用审计状态选项 */
    async loadAuditStatuses() {
      try {
        const res = await getFinanceAuditStatuses()
        if (res.data && Array.isArray(res.data)) {
          this.auditStatusOptions = res.data
        } else {
          this.auditStatusOptions = []
        }
      } catch (e) {
        console.error('加载审计状态失败', e)
        this.auditStatusOptions = []
      }
    },
    /** 从数据库加载可用报告期选项 */
    async loadPeriods() {
      try {
        const params = {}
        if (this.queryForm.stmtType) params.statementType = this.queryForm.stmtType
        const res = await getFinanceStatementPeriods(params)
        if (res.data && Array.isArray(res.data)) {
          this.periodOptions = res.data
        } else {
          this.periodOptions = []
        }
      } catch (e) {
        console.error('加载报告期失败', e)
        this.periodOptions = []
      }
    },
    async loadData() {
      try {
        const params = { pageNumber: this.queryForm.pageNumber, pageSize: this.queryForm.pageSize }
        if (this.queryForm.stmtType) params.statementType = this.queryForm.stmtType
        if (this.queryForm.period) params.periodKeyword = this.queryForm.period
        if (this.queryForm.companyName) params.companyName = this.queryForm.companyName
        if (this.queryForm.auditStatus) params.auditStatus = this.queryForm.auditStatus
        const res = await getFinanceStatementList(params)
        if (res.data && res.data.tlist) {
          this.rawList = res.data.tlist
          this.buildSubjectTree()
          return
        }
      } catch (e) { console.error(e) }
      this.rawList = []
      this.subjectTree = []
    },
    // 将企业维度数据转为科目维度树形结构
    buildSubjectTree() {
      const list = this.rawList
      if (!list.length) { this.subjectTree = []; return }

      // 指标定义：key=字段名, label=科目名, parent=父科目key
      const indicators = [
        { key: 'totalAssets', label: '资产总额', group: 'asset' },
        { key: 'totalLiabilities', label: '负债总额', group: 'asset', anomalyCalc: v => this.calcGroupDebtRatio() > 70 ? '负债率偏高' : null },
        { key: 'netAssets', label: '净资产', group: 'asset' },
        { key: 'revenue', label: '营业收入', group: 'profit' },
        { key: 'netProfit', label: '净利润', group: 'profit', anomalyCalc: v => this.calcGroupProfitRatio() < 3 ? '利润率偏低' : null },
      ]

      // 分组
      const groups = [
        { key: 'asset', label: '资产负债类', id: 'g-asset' },
        { key: 'profit', label: '损益类', id: 'g-profit' },
      ]

      const totalDebtRatio = this.calcGroupDebtRatio()
      const totalProfitRatio = this.calcGroupProfitRatio()

      const tree = groups.map(g => {
        const children = indicators
          .filter(ind => ind.group === g.key)
          .map(ind => {
            const consolidated = list.reduce((s, r) => s + (Number(r[ind.key]) || 0), 0)
            const topItem = [...list].sort((a, b) => (Number(b[ind.key]) || 0) - (Number(a[ind.key]) || 0))[0]
            const topVal = Number(topItem[ind.key]) || 0
            return {
              id: `ind-${ind.key}`,
              subject: ind.label,
              level: 1,
              consolidatedAmount: consolidated,
              drillable: true,
              fieldKey: ind.key,
              companyCount: list.filter(r => Number(r[ind.key]) > 0).length,
              topCompany: topVal > 0 ? topItem.companyName : '-',
              topRatio: consolidated > 0 && topVal > 0 ? (topVal / consolidated * 100).toFixed(1) : null,
              anomaly: ind.anomalyCalc ? ind.anomalyCalc(consolidated) : null,
            }
          })

        // 组合并数
        const groupAmount = children.reduce((s, c) => s + (c.consolidatedAmount || 0), 0)
        return {
          id: g.id,
          subject: g.label,
          level: 0,
          consolidatedAmount: groupAmount,
          drillable: false,
          children,
          companyCount: list.length,
          anomaly: g.key === 'asset' && totalDebtRatio > 70 ? '集团负债率偏高' : (g.key === 'profit' && totalProfitRatio < 3 ? '集团利润率偏低' : null),
        }
      })

      this.subjectTree = tree
    },
    calcGroupDebtRatio() {
      const totalA = this.rawList.reduce((s, r) => s + (Number(r.totalAssets) || 0), 0)
      const totalL = this.rawList.reduce((s, r) => s + (Number(r.totalLiabilities) || 0), 0)
      return totalA > 0 ? (totalL / totalA * 100) : 0
    },
    calcGroupProfitRatio() {
      const totalR = this.rawList.reduce((s, r) => s + (Number(r.revenue) || 0), 0)
      const totalP = this.rawList.reduce((s, r) => s + (Number(r.netProfit) || 0), 0)
      return totalR > 0 ? (totalP / totalR * 100) : 0
    },
    resetQuery() {
      this.queryForm = { stmtType: '', period: '', companyName: '', auditStatus: '', pageNumber: 1, pageSize: 50 }
      this.loadPeriods()
      this.loadData()
    },
    switchStmt(type) {
      this.queryForm.stmtType = type
      this.queryForm.pageNumber = 1
      this.loadPeriods()
      this.loadData()
    },
    onStmtTypeChange(v) { this.switchStmt(v) },
    // 穿透：点击某科目合并数，展开各企业构成
    openDrill(row) {
      this.drillRow = row
      const total = row.consolidatedAmount || 1
      this.drillDetails = this.rawList
        .filter(r => Number(r[row.fieldKey]) > 0)
        .map(r => {
          const amount = Number(r[row.fieldKey]) || 0
          const debtRatio = (r.totalAssets && r.totalLiabilities) ? (r.totalLiabilities / r.totalAssets * 100).toFixed(1) : 0
          return {
            companyName: r.companyName,
            amount,
            proportion: (amount / total * 100).toFixed(1),
            auditStatus: r.auditStatus || '-',
            debtRatio: Number(debtRatio),
            risk: Number(debtRatio) > 70 ? 'HIGH' : (Number(debtRatio) > 60 ? 'MEDIUM' : null),
          }
        })
        .sort((a, b) => b.amount - a.amount)
      this.drillVisible = true
    },
    rowClass({ row }) {
      if (row.anomaly) return 'row-anomaly'
      return ''
    },
    formatNum(v) {
      if (v == null) return '-'
      return Number(v).toLocaleString('zh-CN', { maximumFractionDigits: 2 })
    },
  },
  mounted() {
    this.loadStmtTypes()
    this.loadPeriods()
    this.loadAuditStatuses()
    this.loadData()
  },
}
</script>

<style scoped lang="scss">
.stmt-query-wrap { padding: 16px; background: #f5f7fa; min-height: 100vh; }
.page-banner {
  border-radius: 8px; padding: 20px 28px; margin-bottom: 14px;
  .banner-title { font-size: 22px; font-weight: 700; color: #fff; margin-bottom: 6px; }
  .banner-sub { font-size: 13px; color: rgba(255,255,255,0.78); }
}
.card-header { font-weight: 600; color: #333; display: flex; align-items: center; flex-wrap: wrap; gap: 8px; }
.drill-btn { color: #1677FF; font-weight: 700; cursor: pointer; text-decoration: underline;
  &:hover { color: #003A6C; }
}
::v-deep .row-anomaly td { background: #FFF1F0 !important; }
</style>
