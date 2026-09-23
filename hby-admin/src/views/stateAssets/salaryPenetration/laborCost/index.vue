<template>
  <div class="sal-labor">
    <div class="sal-banner" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="banner-title">人工成本分析</div>
      <div class="banner-sub">人工成本总量监控 · 构成结构分析 · 人工成本率警戒线 · 劳动生产率对标</div>
      <div class="rule-tags">
        <span class="rule-tag warning-tag">橙色警戒：人工成本率 &gt; 15%</span>
        <span class="rule-tag danger-tag">红色警戒：人工成本率 &gt; 20%</span>
      </div>
    </div>

    <!-- KPI -->
    <el-row :gutter="16" class="kpi-row">
      <el-col :span="6" v-for="k in kpiCards" :key="k.key">
        <div class="kpi-card"><div class="kpi-label">{{ k.label }}</div><div class="kpi-value" :style="{ color: k.color }">{{ k.value }}</div></div>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card shadow="never" class="query-card">
      <el-form :model="queryForm" inline size="small">
        <el-form-item label="企业">
          <el-select v-model="queryForm.companyName" clearable placeholder="全部企业" style="width:140px">
            <el-option v-for="c in companyOptions" :key="c" :label="c" :value="c"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">查询</el-button>
          <el-button @click="queryForm.companyName = ''; loadData()">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 图表区 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="13">
        <el-card shadow="never">
          <div slot="header"><span class="card-title">人工成本构成对比（堆叠图）</span></div>
          <div ref="stackChart" style="height:260px"></div>
        </el-card>
      </el-col>
      <el-col :span="11">
        <el-card shadow="never">
          <div slot="header"><span class="card-title">人工成本率企业对比</span><span class="chart-tip">橙线=15%，红线=20%警戒</span></div>
          <div ref="rateChart" style="height:260px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 明细表 -->
    <el-card shadow="never" class="table-card">
      <div slot="header">
        <span class="card-title">企业人工成本明细</span>
        <div style="float:right">
          <el-button size="small" type="primary" icon="el-icon-plus" @click="openEdit(null)">新增</el-button>
        </div>
      </div>
      <el-table :data="tableData" border stripe size="small" :row-class-name="rowClass">
        <el-table-column prop="companyName" label="企业名称" width="130"></el-table-column>
        <el-table-column prop="totalCost" label="人工成本总额（万元）" align="right">
          <template slot-scope="{ row }">{{ (row.totalCost || 0).toLocaleString() }}</template>
        </el-table-column>
        <el-table-column prop="wage" label="工资总额（万元）" align="right">
          <template slot-scope="{ row }">{{ (row.wage || 0).toLocaleString() }}</template>
        </el-table-column>
        <el-table-column prop="socialInsurance" label="社保（万元）" align="right"></el-table-column>
        <el-table-column prop="housingFund" label="公积金（万元）" align="right"></el-table-column>
        <el-table-column prop="welfare" label="福利费（万元）" align="right"></el-table-column>
        <el-table-column prop="laborCostRate" label="人工成本率" align="center">
          <template slot-scope="{ row }">
            <span :style="rateStyle(row.laborCostRate)">{{ row.laborCostRate }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="perCapitaCost" label="人均成本（万元）" align="center"></el-table-column>
        <el-table-column prop="yoyChange" label="同比增幅" align="center">
          <template slot-scope="{ row }">
            <span :style="{ color: row.yoyChange > 15 ? '#F5222D' : row.yoyChange > 10 ? '#FA8C16' : '#52C41A' }">
              {{ row.yoyChange > 0 ? '+' : '' }}{{ row.yoyChange }}%
            </span>
          </template>
        </el-table-column>
        <el-table-column label="风险" align="center" width="80">
          <template slot-scope="{ row }">
            <el-tag :type="row.riskLevel === 'HIGH' ? 'danger' : row.riskLevel === 'MEDIUM' ? 'warning' : 'success'" size="mini">
              {{ { HIGH: '高风险', MEDIUM: '中风险', LOW: '低风险' }[row.riskLevel] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="140" fixed="right">
          <template slot-scope="{ row }">
            <el-button type="text" size="mini" @click="openEdit(row)">编辑</el-button>
            <el-button type="text" size="mini" @click="viewDetail(row)">详情</el-button>
            <el-button type="text" size="mini" style="color:#F56C6C" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 详情抽屉 -->
    <el-drawer title="人工成本详情" :visible.sync="drawerVisible" size="480px" direction="rtl">
      <div v-if="currentRow" class="drawer-content">
        <el-descriptions :column="2" border size="small" :title="currentRow.companyName">
          <el-descriptions-item label="人工成本总额">{{ (currentRow.totalCost || 0).toLocaleString() }}万元</el-descriptions-item>
          <el-descriptions-item label="人工成本率">
            <span :style="rateStyle(currentRow.laborCostRate)">{{ currentRow.laborCostRate }}%</span>
          </el-descriptions-item>
          <el-descriptions-item label="工资总额">{{ (currentRow.wage || 0).toLocaleString() }}万元</el-descriptions-item>
          <el-descriptions-item label="社会保险">{{ (currentRow.socialInsurance || 0).toLocaleString() }}万元</el-descriptions-item>
          <el-descriptions-item label="住房公积金">{{ (currentRow.housingFund || 0).toLocaleString() }}万元</el-descriptions-item>
          <el-descriptions-item label="福利费">{{ (currentRow.welfare || 0).toLocaleString() }}万元</el-descriptions-item>
          <el-descriptions-item label="人均人工成本">{{ currentRow.perCapitaCost }}万元</el-descriptions-item>
          <el-descriptions-item label="同比增幅">
            <span :style="{ color: currentRow.yoyChange > 15 ? '#F5222D' : '#52C41A' }">{{ currentRow.yoyChange }}%</span>
          </el-descriptions-item>
        </el-descriptions>
        <div class="cost-ratio">
          <div class="ratio-title">成本构成占比</div>
          <div v-for="item in costItems(currentRow)" :key="item.label" class="ratio-row">
            <span class="ratio-label">{{ item.label }}</span>
            <el-progress :percentage="item.ratio" :color="item.color" :stroke-width="10" class="ratio-bar"></el-progress>
            <span class="ratio-val">{{ item.ratio }}%</span>
          </div>
        </div>
      </div>
    </el-drawer>

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="editForm.costId ? '编辑人工成本' : '新增人工成本'" :visible.sync="editVisible" width="600px">
      <el-form :model="editForm" :rules="editRules" ref="editFormRef" label-width="130px" size="small">
        <el-form-item label="企业名称" prop="companyName">
          <el-input v-model="editForm.companyName" placeholder="请输入企业名称"></el-input>
        </el-form-item>
        <el-form-item label="报告年度" prop="reportYear">
          <el-date-picker v-model="editForm.reportYear" type="year" value-format="yyyy" placeholder="选择年度" style="width:100%"></el-date-picker>
        </el-form-item>
        <el-form-item label="人工成本总额(万元)">
          <el-input-number v-model="editForm.totalLaborCost" :min="0" :precision="2" controls-position="right" style="width:100%"></el-input-number>
        </el-form-item>
        <el-form-item label="工资总额(万元)">
          <el-input-number v-model="editForm.salaryCost" :min="0" :precision="2" controls-position="right" style="width:100%"></el-input-number>
        </el-form-item>
        <el-form-item label="社会保险(万元)">
          <el-input-number v-model="editForm.socialCost" :min="0" :precision="2" controls-position="right" style="width:100%"></el-input-number>
        </el-form-item>
        <el-form-item label="住房公积金(万元)">
          <el-input-number v-model="editForm.housingFund" :min="0" :precision="2" controls-position="right" style="width:100%"></el-input-number>
        </el-form-item>
        <el-form-item label="福利费(万元)">
          <el-input-number v-model="editForm.welfareCost" :min="0" :precision="2" controls-position="right" style="width:100%"></el-input-number>
        </el-form-item>
        <el-form-item label="人工成本率(%)">
          <el-input-number v-model="editForm.laborCostRate" :min="0" :max="100" :precision="1" controls-position="right" style="width:100%"></el-input-number>
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="editVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getLaborCostList, addLaborCost, updateLaborCost, deleteLaborCost } from '@/api/stateAssets/salaryPenetration'
import { mapGetters } from 'vuex'
import * as echarts from 'echarts'



export default {
  name: 'SalaryLaborCost',
  data() {
    return {
      tableData: [],
      drawerVisible: false,
      currentRow: null,
      charts: {},
      queryForm: { companyName: '' },
      companyOptions: [],
      editVisible: false,
      editForm: {},
      editRules: {
        companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
      },
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
      const totalCost = this.tableData.reduce((s, r) => s + (r.totalCost || 0), 0)
      const avgRate = this.tableData.length > 0 ? (this.tableData.reduce((s, r) => s + (r.laborCostRate || 0), 0) / this.tableData.length).toFixed(1) : '0.0'
      const avgPer = this.tableData.length > 0 ? (this.tableData.reduce((s, r) => s + (r.perCapitaCost || 0), 0) / this.tableData.length).toFixed(1) : '0.0'
      const highRisk = this.tableData.filter(r => r.riskLevel === 'HIGH').length
      return [
        { key: 'total', label: '集团人工成本（万元）', value: totalCost.toLocaleString(), color: '#1677FF' },
        { key: 'rate', label: '平均人工成本率', value: avgRate + '%', color: avgRate > 20 ? '#F5222D' : avgRate > 15 ? '#FA8C16' : '#52C41A' },
        { key: 'per', label: '平均人均成本（万元）', value: avgPer, color: '#0050A0' },
        { key: 'high', label: '高风险企业数', value: highRisk + '家', color: highRisk > 0 ? '#F5222D' : '#52C41A' },
      ]
    },
  },
  mounted() {
    this.loadData().then(() => {
      this.$nextTick(() => {
        this.initStackChart()
        this.initRateChart()
      })
    })
  },
  beforeDestroy() {
    Object.values(this.charts).forEach(c => c && c.dispose())
  },
  methods: {
    async loadData() {
      try {
        const params = { pageSize: 20 }
        if (this.queryForm.companyName) {
          params.companyName = this.queryForm.companyName
        }
        const res = await getLaborCostList(params)
        if (res.data && res.data.tlist) {
          this.tableData = res.data.tlist.map(r => ({ ...r, totalCost: r.totalCost || r.totalLaborCost || 0, wage: r.wage || r.salaryCost || 0, socialInsurance: r.socialInsurance || r.socialCost || 0, housingFund: r.housingFund || 0, welfare: r.welfare || r.welfareCost || 0, yoyChange: r.yoyChange || 0, perCapitaCost: r.perCapitaCost || 0, riskLevel: r.riskLevel || 'LOW' }))
          const companies = [...new Set(this.tableData.map(r => r.companyName).filter(Boolean))]
          if (companies.length) this.companyOptions = companies
        }
      } catch (e) { console.error('加载人工成本列表失败', e) }
      this.$nextTick(() => {
        this.initStackChart()
        this.initRateChart()
      })
    },
    rowClass({ row }) {
      return row.riskLevel === 'HIGH' ? 'row-high' : row.riskLevel === 'MEDIUM' ? 'row-medium' : ''
    },
    rateStyle(v) {
      if (v > 20) return { color: '#F5222D', fontWeight: '700' }
      if (v > 15) return { color: '#FA8C16', fontWeight: '700' }
      return { color: '#52C41A' }
    },
    costItems(row) {
      const total = row.totalCost
      return [
        { label: '工资总额', ratio: +((row.wage / total) * 100).toFixed(1), color: '#1677FF' },
        { label: '社会保险', ratio: +((row.socialInsurance / total) * 100).toFixed(1), color: '#52C41A' },
        { label: '住房公积金', ratio: +((row.housingFund / total) * 100).toFixed(1), color: '#FA8C16' },
        { label: '福利费', ratio: +((row.welfare / total) * 100).toFixed(1), color: '#722ED1' },
      ]
    },
    viewDetail(row) { this.currentRow = row; this.drawerVisible = true },
    openEdit(row) {
      if (row) {
        this.editForm = { ...row }
      } else {
        this.editForm = { companyName: '', reportYear: '', totalLaborCost: 0, salaryCost: 0, socialCost: 0, housingFund: 0, welfareCost: 0, laborCostRate: 0 }
      }
      this.editVisible = true
    },
    async submitEdit() {
      this.$refs.editFormRef.validate(async valid => {
        if (!valid) return
        try {
          if (this.editForm.costId) {
            await updateLaborCost(this.editForm)
          } else {
            await addLaborCost(this.editForm)
          }
          this.$message.success('保存成功')
          this.editVisible = false
          this.loadData()
        } catch (e) {
          this.$message.error('保存失败')
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除该记录？', '提示', { type: 'warning' }).then(async () => {
        try {
          await deleteLaborCost(row.costId)
          this.$message.success('删除成功')
          this.loadData()
        } catch (e) {
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },
    initStackChart() {
      const ec = echarts
      if (!ec) return
      const c = ec.init(this.$refs.stackChart)
      this.charts.stack = c
      const names = this.tableData.map(r => (r.companyName || '').replace('集团', '').replace('科技', '').replace('服务', ''))
      c.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        legend: { data: ['工资总额', '社会保险', '公积金', '福利费'], bottom: 0 },
        xAxis: { type: 'category', data: names },
        yAxis: { type: 'value', name: '万元' },
        series: [
          { name: '工资总额', type: 'bar', stack: 'cost', data: this.tableData.map(r => r.wage), itemStyle: { color: '#1677FF' } },
          { name: '社会保险', type: 'bar', stack: 'cost', data: this.tableData.map(r => r.socialInsurance), itemStyle: { color: '#52C41A' } },
          { name: '公积金', type: 'bar', stack: 'cost', data: this.tableData.map(r => r.housingFund), itemStyle: { color: '#FA8C16' } },
          { name: '福利费', type: 'bar', stack: 'cost', data: this.tableData.map(r => r.welfare), itemStyle: { color: '#722ED1' } },
        ],
      })
    },
    initRateChart() {
      const ec = echarts
      if (!ec) return
      const c = ec.init(this.$refs.rateChart)
      this.charts.rate = c
      const names = this.tableData.map(r => (r.companyName || '').replace('集团', '').replace('科技', '').replace('服务', ''))
      const rates = this.tableData.map(r => r.laborCostRate)
      const colors = rates.map(v => v > 20 ? '#F5222D' : v > 15 ? '#FA8C16' : '#52C41A')
      c.setOption({
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'value', name: '%', min: 0, max: 25 },
        yAxis: { type: 'category', data: names },
        series: [
          {
            type: 'bar', data: rates.map((v, i) => ({ value: v, itemStyle: { color: colors[i] } })),
            barWidth: '50%', label: { show: true, position: 'right', formatter: '{c}%' },
          },
          { type: 'line', data: Array(this.tableData.length).fill(15), lineStyle: { color: '#FA8C16', type: 'dashed', width: 2 }, symbol: 'none' },
          { type: 'line', data: Array(this.tableData.length).fill(20), lineStyle: { color: '#F5222D', type: 'dashed', width: 2 }, symbol: 'none' },
        ],
      })
    },
  },
}
</script>

<style scoped>
.sal-labor { padding: 16px; background: #f0f2f5; min-height: 100vh; }
.sal-banner {
  border-radius: 8px; padding: 20px 24px; color: #fff; margin-bottom: 16px;
}
.banner-title { font-size: 20px; font-weight: 700; margin-bottom: 4px; }
.banner-sub { font-size: 13px; opacity: 0.85; margin-bottom: 10px; }
.rule-tags { display: flex; gap: 12px; }
.rule-tag { padding: 3px 12px; border-radius: 4px; font-size: 12px; font-weight: 600; }
.warning-tag { background: #FAAD14; color: #fff; }
.danger-tag { background: #F5222D; color: #fff; }
.kpi-row { margin-bottom: 16px; }
.kpi-card { background: #fff; border-radius: 8px; padding: 14px; text-align: center; box-shadow: 0 1px 4px rgba(0,0,0,0.08); }
.kpi-label { font-size: 12px; color: #8c8c8c; margin-bottom: 4px; }
.kpi-value { font-size: 20px; font-weight: 700; }
.chart-row { margin-bottom: 16px; }
.card-title { font-size: 14px; font-weight: 600; color: #262626; }
.chart-tip { font-size: 11px; color: #8c8c8c; margin-left: 10px; }
.table-card { margin-bottom: 16px; }
::v-deep .row-high td { background: #fff1f0 !important; }
::v-deep .row-medium td { background: #fffbe6 !important; }
.drawer-content { padding: 20px; }
.cost-ratio { margin-top: 20px; }
.ratio-title { font-weight: 600; margin-bottom: 12px; }
.ratio-row { display: flex; align-items: center; margin-bottom: 10px; gap: 8px; }
.ratio-label { width: 70px; font-size: 12px; }
.ratio-bar { flex: 1; }
.ratio-val { width: 45px; font-size: 12px; text-align: right; }
.query-card { margin-bottom: 12px; }
</style>
