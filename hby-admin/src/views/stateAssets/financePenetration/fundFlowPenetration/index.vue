<template>
  <div class="app-container finance-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-guide"></i><span>资金流向穿透图</span></div>
      <div class="page-header-desc">跨层级资金流向可视化 · 小金库识别 · 异常资金流高亮预警</div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="14" class="stat-row">
      <el-col :span="6" v-for="c in statCards" :key="c.key">
        <div class="stat-card" :class="c.cls">
          <div class="stat-icon"><i :class="c.icon"></i></div>
          <div class="stat-body"><div class="stat-value">{{ c.value }}</div><div class="stat-label">{{ c.label }}</div></div>
        </div>
      </el-col>
    </el-row>

    <!-- 筛选 -->
    <el-card shadow="never" class="filter-card">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="企业名称"><el-input v-model="queryForm.companyName" placeholder="请输入企业名称" clearable style="width:180px" /></el-form-item>
        <el-form-item label="资金类型">
          <el-select v-model="queryForm.flowType" placeholder="全部" clearable style="width:130px">
            <el-option label="正常资金流" value="NORMAL" /><el-option label="异常资金流" value="ABNORMAL" /><el-option label="疑似小金库" value="HIDDEN" />
          </el-select>
        </el-form-item>
        <el-form-item label="资金性质">
          <el-select v-model="queryForm.fundNature" placeholder="全部" clearable style="width:130px">
            <el-option label="经营性资金" value="经营性资金" /><el-option label="资本性资金" value="资本性资金" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="queryForm.riskLevel" placeholder="全部" clearable style="width:110px">
            <el-option label="低风险" value="LOW" /><el-option label="中风险" value="MEDIUM" /><el-option label="高风险" value="HIGH" />
          </el-select>
        </el-form-item>
        <el-form-item label="金额范围">
          <el-input-number v-model="queryForm.minAmount" :min="0" :precision="0" placeholder="最小" controls-position="right" style="width:100px" />
          <span style="margin:0 4px">-</span>
          <el-input-number v-model="queryForm.maxAmount" :min="0" :precision="0" placeholder="最大" controls-position="right" style="width:100px" />
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker v-model="queryForm.dateRange" type="daterange" range-separator="至" start-placeholder="开始" end-placeholder="结束" value-format="yyyy-MM-dd" style="width:220px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-row :gutter="14">
      <!-- 左：桑基图 -->
      <el-col :span="14">
        <el-card shadow="never" class="chart-card">
          <div slot="header" class="card-header">
            <span>资金流向桑基图</span>
            <el-tag size="mini" type="danger" style="margin-left:8px">红色=异常流向</el-tag>
          </div>
          <div ref="sankeyChart" style="height:420px"></div>
        </el-card>
      </el-col>
      <!-- 右：异常流水列表 -->
      <el-col :span="10">
        <el-card shadow="never" class="chart-card">
          <div slot="header" class="card-header">
            <span>异常资金流水</span>
            <el-badge :value="abnormalList.length" type="danger" style="margin-left:8px" />
          </div>
          <el-table :data="abnormalList" border size="small" style="width:100%" :row-class-name="() => 'row-danger'">
            <el-table-column label="来源企业" prop="fromCompany" min-width="110" show-overflow-tooltip />
            <el-table-column label="目标企业" prop="toCompany" min-width="110" show-overflow-tooltip />
            <el-table-column label="金额(万)" prop="amount" width="80" align="right">
              <template slot-scope="{ row }">{{ row.amount != null ? row.amount.toLocaleString() : '-' }}</template>
            </el-table-column>
            <el-table-column label="资金用途" prop="purpose" width="90" show-overflow-tooltip />
            <el-table-column label="资金类型" width="90" align="center">
              <template slot-scope="{ row }">
                <el-tag :type="row.flowType === 'HIDDEN' ? 'danger' : 'warning'" size="mini">{{ row.flowType === 'HIDDEN' ? '疑似小金库' : '异常' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="60" align="center">
              <template slot-scope="{ row }">
                <el-button size="mini" type="text" style="color:#ff4d4f" @click="handleDispatch(row)">派单</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 全量资金流水台账 -->
    <el-card shadow="never" style="margin-top:14px">
      <div slot="header" class="card-header">全量资金流水台账</div>
      <el-table v-loading="loading" :data="flowList" border size="small" style="width:100%" :row-class-name="flowRowClass">
        <el-table-column label="流水编号" prop="flowNo" width="140" />
        <el-table-column label="来源企业" prop="fromCompany" min-width="140" show-overflow-tooltip />
        <el-table-column label="目标企业" prop="toCompany" min-width="140" show-overflow-tooltip />
        <el-table-column label="金额(万元)" prop="amount" width="110" align="right">
          <template slot-scope="{ row }"><span :style="row.flowType === 'HIDDEN' ? 'color:#ff4d4f;font-weight:bold' : ''">{{ row.amount != null ? Number(row.amount).toLocaleString() : '-' }}</span></template>
        </el-table-column>
        <el-table-column label="资金用途" prop="purpose" min-width="110" show-overflow-tooltip />
        <el-table-column label="资金性质" prop="fundNature" width="100" show-overflow-tooltip />
        <el-table-column label="资金类型" width="110" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="{ NORMAL:'success', ABNORMAL:'warning', HIDDEN:'danger' }[row.flowType]" size="small">
              {{ { NORMAL:'正常', ABNORMAL:'异常', HIDDEN:'疑似小金库' }[row.flowType] || '未知' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="发生时间" prop="occurTime" width="110" align="center" />
        <el-table-column label="操作" width="100" align="center" fixed="right">
          <template slot-scope="{ row }">
            <el-button size="mini" type="text" @click="handleDetail(row)">详情</el-button>
            <el-button v-if="row.flowType !== 'NORMAL'" size="mini" type="text" style="color:#fa8c16" @click="handleDispatch(row)">派单</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 详情抽屉 -->
    <el-drawer title="资金流向详情" :visible.sync="drawerVisible" size="580px" direction="rtl">
      <div v-if="currentRow" style="padding:0 20px 20px">
        <el-descriptions :column="2" border size="small" style="margin-top:16px">
          <el-descriptions-item label="流水编号">{{ currentRow.flowNo }}</el-descriptions-item>
          <el-descriptions-item label="资金类型">
            <el-tag :type="{ NORMAL:'success', ABNORMAL:'warning', HIDDEN:'danger' }[currentRow.flowType]" size="small">
              {{ { NORMAL:'正常', ABNORMAL:'异常', HIDDEN:'疑似小金库' }[currentRow.flowType] || '未知' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="来源企业">{{ currentRow.fromCompany }}</el-descriptions-item>
          <el-descriptions-item label="目标企业">{{ currentRow.toCompany }}</el-descriptions-item>
          <el-descriptions-item label="金额(万元)">{{ currentRow.amount != null ? Number(currentRow.amount).toLocaleString() : '-' }}</el-descriptions-item>
          <el-descriptions-item label="资金用途">{{ currentRow.purpose }}</el-descriptions-item>
          <el-descriptions-item label="资金性质">{{ currentRow.fundNature || '-' }}</el-descriptions-item>
          <el-descriptions-item label="风险等级">
            <el-tag :type="{ LOW:'success', MEDIUM:'warning', HIGH:'danger' }[currentRow.riskLevel]" size="small">
              {{ { LOW:'低风险', MEDIUM:'中风险', HIGH:'高风险' }[currentRow.riskLevel] || '-' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="发生时间">{{ currentRow.occurTime }}</el-descriptions-item>
          <el-descriptions-item label="关联合同">{{ currentRow.contractNo || '无' }}</el-descriptions-item>
        </el-descriptions>
        <el-divider content-position="left">风险分析</el-divider>
        <el-alert :title="currentRow.riskDesc || '暂无风险描述'" :type="currentRow.flowType === 'NORMAL' ? 'success' : 'error'" show-icon :closable="false" />
      </div>
    </el-drawer>

    <!-- 派单对话框 -->
    <el-dialog title="异常资金核查派单" :visible.sync="dispatchVisible" width="460px">
      <el-form :model="dispatchForm" label-width="100px" size="small">
        <el-form-item label="异常类型"><el-input v-model="dispatchForm.anomalyType" disabled /></el-form-item>
        <el-form-item label="核查责任人"><el-input v-model="dispatchForm.owner" placeholder="请输入" /></el-form-item>
        <el-form-item label="核查期限">
          <el-date-picker v-model="dispatchForm.deadline" type="date" value-format="yyyy-MM-dd" style="width:100%" />
        </el-form-item>
        <el-form-item label="核查要求"><el-input v-model="dispatchForm.requirement" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="dispatchVisible = false">取消</el-button>
        <el-button type="primary" @click="submitDispatch">确认派单</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import { getFundFlowPenetrationList, getFundFlowPenetrationStats, getFundFlowPenetrationDetail, submitFundFlowDispatch } from '@/api/stateAssets/financialPenetration'
export default {
  name: 'FundFlowPenetration',
  data() {
    return {
      loading: false, flowList: [], sankeyChart: null,
      queryForm: { companyName: '', flowType: '', fundNature: '', riskLevel: '', minAmount: undefined, maxAmount: undefined, dateRange: null },
      drawerVisible: false, currentRow: null,
      dispatchVisible: false, dispatchForm: { flowId: '', flowNo: '', anomalyType: '', owner: '', deadline: '', requirement: '' },
      statCards: [
        { key: 'total', label: '资金流水总笔数', value: '--', icon: 'el-icon-money', cls: 'card-blue' },
        { key: 'abnormal', label: '异常流水数', value: '--', icon: 'el-icon-warning', cls: 'card-orange' },
        { key: 'hidden', label: '疑似小金库数', value: '--', icon: 'el-icon-lock', cls: 'card-red' },
        { key: 'amount', label: '异常金额(万元)', value: '--', icon: 'el-icon-coin', cls: 'card-red' }
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
    abnormalList() { return this.flowList.filter(r => r.flowType !== 'NORMAL') }
  },
  created() { this.fetchData(); this.fetchStats() },
  mounted() {},
  methods: {
    fetchData() {
      this.loading = true
      const params = {}
      if (this.queryForm.companyName) params.companyName = this.queryForm.companyName
      if (this.queryForm.flowType) params.flowType = this.queryForm.flowType
      if (this.queryForm.fundNature) params.fundNature = this.queryForm.fundNature
      if (this.queryForm.riskLevel) params.riskLevel = this.queryForm.riskLevel
      if (this.queryForm.minAmount != null) params.minAmount = this.queryForm.minAmount
      if (this.queryForm.maxAmount != null) params.maxAmount = this.queryForm.maxAmount
      if (this.queryForm.dateRange && this.queryForm.dateRange.length === 2) {
        params.startDate = this.queryForm.dateRange[0]
        params.endDate = this.queryForm.dateRange[1]
      }
      getFundFlowPenetrationList(params).then(res => {
        const data = res.data || res
        this.flowList = Array.isArray(data) ? data : (data.list || data.tlist || [])
        this.$nextTick(() => { this.initSankey() })
      }).catch(() => {
        this.flowList = []
      }).finally(() => {
        this.loading = false
      })
    },
    fetchStats() {
      getFundFlowPenetrationStats().then(res => {
        const data = res.data || res
        if (data) {
          this.statCards[0].value = data.total != null ? data.total : 0
          this.statCards[1].value = data.abnormal != null ? data.abnormal : 0
          this.statCards[2].value = data.hidden != null ? data.hidden : 0
          this.statCards[3].value = data.abnormalAmount != null ? Number(data.abnormalAmount).toLocaleString() : '0'
        }
      }).catch(() => {})
    },
    handleQuery() { this.fetchData(); this.fetchStats() },
    resetQuery() {
      this.queryForm = { companyName: '', flowType: '', fundNature: '', riskLevel: '', minAmount: undefined, maxAmount: undefined, dateRange: null }
      this.fetchData()
      this.fetchStats()
    },
    flowRowClass({ row }) { return row.flowType === 'HIDDEN' ? 'row-danger' : row.flowType === 'ABNORMAL' ? 'row-warn' : '' },
    handleDetail(row) {
      if (row.flowId) {
        getFundFlowPenetrationDetail(row.flowId).then(res => {
          this.currentRow = res.data || row
          this.drawerVisible = true
        }).catch(() => {
          this.currentRow = row
          this.drawerVisible = true
        })
      } else {
        this.currentRow = row
        this.drawerVisible = true
      }
    },
    handleDispatch(row) {
      this.currentRow = row
      this.dispatchForm = {
        flowId: row.flowId || row.id || '',
        flowNo: row.flowNo || '',
        anomalyType: row.flowType === 'HIDDEN' ? '疑似小金库' : '异常资金流',
        owner: '',
        deadline: '',
        requirement: ''
      }
      this.dispatchVisible = true
    },
    submitDispatch() {
      if (!this.dispatchForm.owner) { this.$message.warning('请填写核查责任人'); return }
      if (!this.dispatchForm.deadline) { this.$message.warning('请选择核查期限'); return }
      submitFundFlowDispatch(this.dispatchForm).then(res => {
        if (res && (res.result === 200 || res.data)) {
          this.$message.success('异常资金核查派单已提交')
          this.dispatchVisible = false
        } else {
          this.$message.error((res && res.msg) || '派单失败')
        }
      }).catch(() => {
        this.$message.error('派单请求失败，请稍后重试')
      })
    },
    initSankey() {
      const el = this.$refs.sankeyChart
      if (!el) return
      if (this.sankeyChart) this.sankeyChart.dispose()
      this.sankeyChart = echarts.init(el)
      // 从 flowList 动态构建桑基图节点和连线
      const nodeSet = new Set()
      // 合并同方向链接，key = source|target
      const linkMap = {}
      this.flowList.forEach(r => {
        if (r.fromCompany) nodeSet.add(r.fromCompany)
        if (r.toCompany) nodeSet.add(r.toCompany)
        if (r.fromCompany && r.toCompany && r.amount) {
          const key = r.fromCompany + '|' + r.toCompany
          const isAbnormal = r.flowType === 'HIDDEN' || r.flowType === 'ABNORMAL'
          if (linkMap[key]) {
            linkMap[key].value += parseFloat(r.amount) || 0
            if (isAbnormal) linkMap[key].isAbnormal = true
            if (r.flowType === 'HIDDEN') linkMap[key].isHidden = true
          } else {
            linkMap[key] = {
              source: r.fromCompany,
              target: r.toCompany,
              value: parseFloat(r.amount) || 1,
              isAbnormal: isAbnormal,
              isHidden: r.flowType === 'HIDDEN'
            }
          }
        }
      })
      // 检测环路：如果存在 A→B 和 B→A，将反向链接的 target 加"(回流)"后缀
      const linkKeys = Object.keys(linkMap)
      const reverseSet = new Set()
      linkKeys.forEach(key => {
        const [s, t] = key.split('|')
        const reverseKey = t + '|' + s
        if (linkMap[reverseKey] && !reverseSet.has(key) && !reverseSet.has(reverseKey)) {
          // 保留金额大的方向不变，金额小的加后缀
          if (linkMap[key].value >= linkMap[reverseKey].value) {
            reverseSet.add(reverseKey)
          } else {
            reverseSet.add(key)
          }
        }
      })
      const links = []
      linkKeys.forEach(key => {
        const item = linkMap[key]
        let target = item.target
        if (reverseSet.has(key)) {
          target = item.target + '(回流)'
          nodeSet.add(target)
        }
        links.push({
          source: item.source,
          target: target,
          value: item.value,
          lineStyle: item.isAbnormal ? { color: item.isHidden ? '#ff4d4f' : '#fa8c16' } : {}
        })
      })
      // 标记异常目标节点（如壳公司/个人账户）
      const abnormalTargets = new Set(
        this.flowList.filter(r => r.flowType === 'HIDDEN').map(r => r.toCompany)
      )
      const nodes = Array.from(nodeSet).map(name => ({
        name,
        itemStyle: abnormalTargets.has(name) ? { color: '#ff4d4f' } : {}
      }))
      if (nodes.length === 0 || links.length === 0) {
        this.sankeyChart.clear()
        return
      }
      this.sankeyChart.setOption({
        tooltip: { trigger: 'item', triggerOn: 'mousemove' },
        series: [{
          type: 'sankey', layout: 'none', emphasis: { focus: 'adjacency' },
          nodeGap: 12,
          data: nodes,
          links: links
        }]
      })
    }
  },
  beforeDestroy() { if (this.sankeyChart) this.sankeyChart.dispose() }
}
</script>
<style lang="scss" scoped>
.finance-page { padding:16px; background:#f0f2f5; min-height:calc(100vh - 84px); }
.page-header { display:flex; align-items:center; justify-content:space-between; margin-bottom:14px; padding:14px 20px; border-radius:6px; color:#fff;
  .page-header-left { display:flex; align-items:center; font-size:16px; font-weight:600; i { font-size:22px; margin-right:10px; } }
  .page-header-desc { font-size:13px; opacity:.85; }
}
.stat-row { margin-bottom:14px; }
.stat-card { display:flex; align-items:center; padding:16px; border-radius:8px; background:#fff; box-shadow:0 1px 4px rgba(0,0,0,.08);
  .stat-icon { font-size:30px; padding:10px; border-radius:8px; margin-right:12px; }
  .stat-value { font-size:26px; font-weight:bold; }
  .stat-label { font-size:12px; color:#909399; margin-top:2px; }
  &.card-blue   { .stat-icon { color:#1890ff; background:#e6f7ff; } .stat-value { color:#1890ff; } }
  &.card-orange { .stat-icon { color:#fa8c16; background:#fff7e6; } .stat-value { color:#fa8c16; } }
  &.card-red    { .stat-icon { color:#ff4d4f; background:#fff1f0; } .stat-value { color:#ff4d4f; } }
}
.filter-card { margin-bottom:12px; }
.chart-card { height:100%; }
.card-header { font-size:14px; font-weight:600; color:#303133; }
::v-deep .row-danger td { background:#fff1f0 !important; }
::v-deep .row-warn td { background:#fff7e6 !important; }
::v-deep .el-table th { background:#e6fffb; }
::v-deep .el-card { border-radius:6px; }
</style>

