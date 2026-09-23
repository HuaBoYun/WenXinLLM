<template>
  <div class="page-container" :style="themeVars">
    <!-- Banner -->
    <div class="page-header">
      <div class="header-left">
        <h2 class="page-title"><i class="el-icon-tickets"></i> 采购台账</h2>
        <p class="page-desc">登记各级企业采购记录，支持采购结构分析和合规预警</p>
      </div>
    </div>

    <!-- 统计卡 -->
    <el-row :gutter="16" class="stat-row">
      <el-col :span="6" v-for="s in statCards" :key="s.label">
        <div class="stat-card" :style="{ borderLeft: '4px solid ' + s.color }">
          <div class="stat-val" :style="{ color: s.color }">{{ s.value }}</div>
          <div class="stat-lb">{{ s.label }}</div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区 -->
    <el-row :gutter="16" class="chart-row">
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header" class="card-hd">采购类型分布</div>
          <div ref="typeChart" class="chart-sm"></div>
        </el-card>
      </el-col>
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header" class="card-hd">月度采购趋势</div>
          <div ref="trendChart" class="chart-sm"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询区 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" inline size="small">
        <el-form-item label="采购企业">
          <el-input v-model="queryForm.companyName" placeholder="请输入" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="采购类型">
          <el-select v-model="queryForm.purchaseType" placeholder="请选择" clearable style="width:120px">
            <el-option label="工程类" value="ENGINEERING" />
            <el-option label="货物类" value="GOODS" />
            <el-option label="服务类" value="SERVICE" />
            <el-option label="IT类" value="IT" />
          </el-select>
        </el-form-item>
        <el-form-item label="采购方式">
          <el-select v-model="queryForm.biddingMethod" placeholder="请选择" clearable style="width:130px">
            <el-option label="公开招标" value="OPEN_BIDDING" />
            <el-option label="竞争性谈判" value="NEGOTIATION" />
            <el-option label="单一来源" value="SINGLE_SOURCE" />
            <el-option label="询价采购" value="INQUIRY" />
          </el-select>
        </el-form-item>
        <el-form-item label="合规状态">
          <el-select v-model="queryForm.complianceStatus" placeholder="请选择" clearable style="width:110px">
            <el-option label="合规" value="COMPLIANT" />
            <el-option label="预警" value="WARNING" />
            <el-option label="违规" value="VIOLATION" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
          <el-button type="success" icon="el-icon-plus" @click="handleAdd">新增</el-button>
          <el-button type="warning" icon="el-icon-download" @click="handleExport">导出</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 预警提示 -->
    <el-alert
      v-if="violationCount > 0"
      :title="`当前有 ${violationCount} 条违规采购记录，需立即核查处理`"
      type="error" show-icon :closable="false"
      style="margin-bottom:12px"
    />

    <!-- 表格 -->
    <el-card shadow="never">
      <el-table
        v-loading="loading"
        :data="list"
        border
        :row-class-name="rowClassName"
      >
        <el-table-column label="采购编号" prop="purchaseNo" width="130" />
        <el-table-column label="采购企业" prop="companyName" min-width="160" show-overflow-tooltip />
        <el-table-column label="供应商名称" prop="supplierName" min-width="160" show-overflow-tooltip />
        <el-table-column label="采购类型" width="90" align="center">
          <template slot-scope="{ row }">
            <el-tag size="mini" type="info">{{ typeMap[row.purchaseType] || row.purchaseType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="采购方式" width="110" align="center">
          <template slot-scope="{ row }">{{ methodMap[row.biddingMethod] || row.biddingMethod }}</template>
        </el-table-column>
        <el-table-column label="合同金额(万元)" prop="contractAmount" width="130" align="right">
          <template slot-scope="{ row }">{{ row.contractAmount ? row.contractAmount.toLocaleString() : '--' }}</template>
        </el-table-column>
        <el-table-column label="采购日期" prop="purchaseDate" width="100" align="center" />
        <el-table-column label="关联交易" width="80" align="center">
          <template slot-scope="{ row }">
            <el-tag v-if="row.isRelated === 'Y'" size="mini" color="#F9F0FF" style="color:#722ED1;border-color:#722ED1">关联</el-tag>
            <span v-else style="color:#8C8C8C;font-size:12px">—</span>
          </template>
        </el-table-column>
        <el-table-column label="合规状态" width="90" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="{ COMPLIANT: 'success', WARNING: 'warning', VIOLATION: 'danger' }[row.complianceStatus]" size="mini">
              {{ { COMPLIANT: '合规', WARNING: '预警', VIOLATION: '违规' }[row.complianceStatus] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险等级" width="85" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }[row.riskLevel]" size="mini">
              {{ { HIGH: '高', MEDIUM: '中', LOW: '低' }[row.riskLevel] }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="130" align="center" fixed="right">
          <template slot-scope="{ row }">
            <el-button type="text" size="mini" icon="el-icon-edit" @click="handleEdit(row)">编辑</el-button>
            <el-button type="text" size="mini" icon="el-icon-delete" style="color:#CF1322" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        style="margin-top:12px;text-align:right"
        :current-page.sync="queryForm.pageNumber"
        :page-size.sync="queryForm.pageSize"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next"
        :total="total"
        @current-change="fetchData"
        @size-change="handleSizeChange"
      />
    </el-card>

    <!-- 图例说明 -->
    <div class="legend-bar">
      <span class="legend-item legend-related">■ 关联交易行</span>
      <span class="legend-item legend-violation">■ 违规行</span>
      <span class="legend-item legend-warning">■ 预警行</span>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="650px" :close-on-click-modal="false">
      <el-form ref="recordForm" :model="recordForm" :rules="formRules" label-width="110px" size="small">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="采购编号" prop="purchaseNo">
              <el-input v-model="recordForm.purchaseNo" placeholder="请输入采购编号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="采购企业" prop="companyName">
              <el-input v-model="recordForm.companyName" placeholder="请输入采购企业" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="供应商名称" prop="supplierName">
              <el-input v-model="recordForm.supplierName" placeholder="请输入供应商名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="采购类型" prop="purchaseType">
              <el-select v-model="recordForm.purchaseType" placeholder="请选择" style="width:100%">
                <el-option label="工程类" value="ENGINEERING" />
                <el-option label="货物类" value="GOODS" />
                <el-option label="服务类" value="SERVICE" />
                <el-option label="IT类" value="IT" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="采购方式" prop="biddingMethod">
              <el-select v-model="recordForm.biddingMethod" placeholder="请选择" style="width:100%">
                <el-option label="公开招标" value="OPEN_BIDDING" />
                <el-option label="竞争性谈判" value="NEGOTIATION" />
                <el-option label="单一来源" value="SINGLE_SOURCE" />
                <el-option label="询价采购" value="INQUIRY" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同金额(万)" prop="contractAmount">
              <el-input-number v-model="recordForm.contractAmount" :min="0" :precision="2" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="采购日期" prop="purchaseDate">
              <el-date-picker v-model="recordForm.purchaseDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否关联交易">
              <el-radio-group v-model="recordForm.isRelated">
                <el-radio label="Y">是</el-radio>
                <el-radio label="N">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="合规状态" prop="complianceStatus">
              <el-select v-model="recordForm.complianceStatus" placeholder="请选择" style="width:100%">
                <el-option label="合规" value="COMPLIANT" />
                <el-option label="预警" value="WARNING" />
                <el-option label="违规" value="VIOLATION" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险等级" prop="riskLevel">
              <el-select v-model="recordForm.riskLevel" placeholder="请选择" style="width:100%">
                <el-option label="高" value="HIGH" />
                <el-option label="中" value="MEDIUM" />
                <el-option label="低" value="LOW" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button size="small" @click="dialogVisible = false">取 消</el-button>
        <el-button size="small" type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getPurchaseRecordList, addPurchaseRecord, updatePurchaseRecord, deletePurchaseRecord, exportPurchaseRecords } from '@/api/stateAssets/procurementPenetration'
import { investThemeMixin } from '../../themeMixin'

export default {
  name: 'PurchaseRecord',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      submitLoading: false,
      list: [],
      total: 0,
      queryForm: { pageNumber: 1, pageSize: 10, companyName: '', purchaseType: '', biddingMethod: '', complianceStatus: '' },
      typeMap: { ENGINEERING: '工程', GOODS: '货物', SERVICE: '服务', IT: 'IT' },
      methodMap: { OPEN_BIDDING: '公开招标', NEGOTIATION: '竞争性谈判', SINGLE_SOURCE: '单一来源', INQUIRY: '询价采购' },
      statCards: [],
      charts: [],
      // Dialog
      dialogVisible: false,
      dialogTitle: '新增采购记录',
      isEdit: false,
      recordForm: {
        id: '',
        purchaseNo: '',
        companyName: '',
        supplierName: '',
        purchaseType: '',
        biddingMethod: '',
        contractAmount: 0,
        purchaseDate: '',
        isRelated: 'N',
        complianceStatus: '',
        riskLevel: '',
      },
      formRules: {
        purchaseNo: [{ required: true, message: '请输入采购编号', trigger: 'blur' }],
        companyName: [{ required: true, message: '请输入采购企业', trigger: 'blur' }],
        supplierName: [{ required: true, message: '请输入供应商名称', trigger: 'blur' }],
        purchaseType: [{ required: true, message: '请选择采购类型', trigger: 'change' }],
        biddingMethod: [{ required: true, message: '请选择采购方式', trigger: 'change' }],
        contractAmount: [{ required: true, message: '请输入合同金额', trigger: 'blur' }],
        purchaseDate: [{ required: true, message: '请选择采购日期', trigger: 'change' }],
        complianceStatus: [{ required: true, message: '请选择合规状态', trigger: 'change' }],
        riskLevel: [{ required: true, message: '请选择风险等级', trigger: 'change' }],
      },
    }
  },
  computed: {
    violationCount() {
      return this.list.filter(r => r.complianceStatus === 'VIOLATION').length
    },
  },
  created() {
    this.statCards = [
      { label: '采购总额(亿元)', value: '--', color: this.ipSecondary },
      { label: '关联采购比', value: '--', color: '#722ED1' },
      { label: '单一来源比', value: '--', color: '#FA8C16' },
      { label: '活跃预警', value: '--', color: '#CF1322' },
    ]
  },
  mounted() {
    this.$nextTick(() => {
      this.initCharts()
      this.fetchData()
    })
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    this.charts.forEach(c => c.dispose())
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getPurchaseRecordList(this.queryForm)
        if (res && res.result === 200 && res.data) {
          this.list = res.data.tlist || []
          this.total = res.data.totalRecord || 0
          this.updateStatCards()
          this.$nextTick(() => this.updateCharts())
        }
      } catch (e) {
        console.error('采购台账数据加载失败', e)
        this.$message.error('数据加载失败')
      } finally {
        this.loading = false
      }
    },
    updateStatCards() {
      const list = this.list
      const totalAmount = list.reduce((sum, r) => sum + (r.contractAmount || 0), 0)
      const relatedCount = list.filter(r => r.isRelated === 'Y').length
      const singleSourceCount = list.filter(r => r.biddingMethod === 'SINGLE_SOURCE').length
      const warningCount = list.filter(r => r.complianceStatus === 'WARNING' || r.complianceStatus === 'VIOLATION').length
      this.statCards = [
        { label: '采购总额(亿元)', value: (totalAmount / 10000).toFixed(1), color: this.ipSecondary },
        { label: '关联采购比', value: list.length ? ((relatedCount / list.length) * 100).toFixed(1) + '%' : '--', color: '#722ED1' },
        { label: '单一来源比', value: list.length ? ((singleSourceCount / list.length) * 100).toFixed(1) + '%' : '--', color: '#FA8C16' },
        { label: '活跃预警', value: warningCount + '条', color: '#CF1322' },
      ]
    },
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.fetchData()
    },
    resetQuery() {
      this.queryForm = { pageNumber: 1, pageSize: 10, companyName: '', purchaseType: '', biddingMethod: '', complianceStatus: '' }
      this.fetchData()
    },
    handleSizeChange(size) {
      this.queryForm.pageSize = size
      this.queryForm.pageNumber = 1
      this.fetchData()
    },
    handleAdd() {
      this.isEdit = false
      this.dialogTitle = '新增采购记录'
      this.recordForm = { id: '', purchaseNo: '', companyName: '', supplierName: '', purchaseType: '', biddingMethod: '', contractAmount: 0, purchaseDate: '', isRelated: 'N', complianceStatus: '', riskLevel: '' }
      this.dialogVisible = true
      this.$nextTick(() => { if (this.$refs.recordForm) this.$refs.recordForm.clearValidate() })
    },
    handleEdit(row) {
      this.isEdit = true
      this.dialogTitle = '编辑采购记录'
      this.recordForm = { ...row }
      this.dialogVisible = true
      this.$nextTick(() => { if (this.$refs.recordForm) this.$refs.recordForm.clearValidate() })
    },
    handleSubmit() {
      this.$refs.recordForm.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          const submitData = { ...this.recordForm }
          // 确保 isRelated 为 "Y"/"N" 格式
          if (submitData.isRelated === true || submitData.isRelated === 'true') submitData.isRelated = 'Y'
          else if (submitData.isRelated === false || submitData.isRelated === 'false' || !submitData.isRelated) submitData.isRelated = 'N'
          const apiFn = this.isEdit ? updatePurchaseRecord : addPurchaseRecord
          const res = await apiFn(submitData)
          if (res && res.result === 200) {
            this.$message.success(this.isEdit ? '修改成功' : '新增成功')
            this.dialogVisible = false
            this.fetchData()
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        } catch (e) {
          console.error('提交失败', e)
          this.$message.error('操作失败，请稍后重试')
        } finally {
          this.submitLoading = false
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除该采购记录？', '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await deletePurchaseRecord(row.id)
          if (res && res.result === 200) {
            this.$message.success('删除成功')
            this.fetchData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        } catch (e) {
          console.error('删除失败', e)
          this.$message.error('删除失败，请稍后重试')
        }
      }).catch(() => {})
    },
    async handleExport() {
      try {
        const res = await exportPurchaseRecords(this.queryForm)
        const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = '采购台账.xlsx'
        a.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (e) {
        console.error('导出失败', e)
        this.$message.error('导出失败，请稍后重试')
      }
    },
    rowClassName({ row }) {
      if (row.complianceStatus === 'VIOLATION') return 'row-violation'
      if (row.isRelated === 'Y') return 'row-related'
      if (row.complianceStatus === 'WARNING') return 'row-warning'
      return ''
    },
    handleResize() { this.charts.forEach(c => c.resize()) },
    initCharts() {
      this.charts.forEach(c => c.dispose())
      this.charts = []
      // type distribution pie
      const typeCountMap = {}
      const typeNameMap = { ENGINEERING: '工程类', GOODS: '货物类', SERVICE: '服务类', IT: 'IT类' }
      const typeColorMap = { ENGINEERING: this.ipSecondary, GOODS: this.ipBright, SERVICE: '#69B1FF', IT: '#FAAD14' }
      this.list.forEach(r => {
        const t = r.purchaseType || 'OTHER'
        typeCountMap[t] = (typeCountMap[t] || 0) + (r.contractAmount || 0)
      })
      const typePieData = Object.keys(typeCountMap).map(k => ({
        value: typeCountMap[k], name: typeNameMap[k] || k, itemStyle: { color: typeColorMap[k] || '#D9D9D9' }
      }))
      const c1 = echarts.init(this.$refs.typeChart)
      this.charts.push(c1)
      c1.setOption({
        tooltip: { trigger: 'item' },
        legend: { bottom: 4, itemWidth: 10 },
        series: [{
          type: 'pie', radius: ['40%', '70%'], center: ['50%', '44%'],
          data: typePieData,
          label: { formatter: '{b}\n{d}%', fontSize: 11 },
        }],
      })
      // monthly trend bar+line
      const monthMap = {}
      const monthRelatedMap = {}
      this.list.forEach(r => {
        if (r.purchaseDate) {
          const m = r.purchaseDate.substring(0, 7)
          monthMap[m] = (monthMap[m] || 0) + (r.contractAmount || 0)
          if (r.isRelated === 'Y') monthRelatedMap[m] = (monthRelatedMap[m] || 0) + (r.contractAmount || 0)
        }
      })
      const months = Object.keys(monthMap).sort()
      const totalVals = months.map(m => monthMap[m] || 0)
      const relatedVals = months.map(m => monthRelatedMap[m] || 0)
      const c2 = echarts.init(this.$refs.trendChart)
      this.charts.push(c2)
      c2.setOption({
        tooltip: { trigger: 'axis' },
        legend: { data: ['采购总额', '关联采购'], top: 4 },
        grid: { left: 45, right: 20, top: 36, bottom: 30 },
        xAxis: { type: 'category', data: months },
        yAxis: { type: 'value', name: '万元' },
        series: [
          { name: '采购总额', type: 'bar', data: totalVals, itemStyle: { color: this.ipSecondary } },
          { name: '关联采购', type: 'line', data: relatedVals, itemStyle: { color: '#722ED1' }, smooth: true },
        ],
      })
    },
    updateCharts() {
      if (!this.list.length) return
      // Type distribution from current data
      const typeCount = {}
      this.list.forEach(r => {
        const name = this.typeMap[r.purchaseType] || r.purchaseType
        typeCount[name] = (typeCount[name] || 0) + (r.contractAmount || 0)
      })
      const colors = { '工程': this.ipSecondary, '货物': this.ipBright, '服务': '#69B1FF', 'IT': '#FAAD14' }
      const pieData = Object.entries(typeCount).map(([name, value]) => ({
        value, name: name + '类', itemStyle: { color: colors[name] || '#D9D9D9' },
      }))
      if (this.charts[0]) this.charts[0].setOption({ series: [{ data: pieData }] })

      // Monthly trend from current data
      const monthMap = {}
      const relatedMonthMap = {}
      this.list.forEach(r => {
        if (!r.purchaseDate) return
        const m = r.purchaseDate.substring(5, 7).replace(/^0/, '') + '月'
        monthMap[m] = (monthMap[m] || 0) + (r.contractAmount || 0)
        if (r.isRelated === 'Y') relatedMonthMap[m] = (relatedMonthMap[m] || 0) + (r.contractAmount || 0)
      })
      const months = Object.keys(monthMap).sort()
      if (this.charts[1] && months.length) {
        this.charts[1].setOption({
          xAxis: { data: months },
          series: [
            { name: '采购总额', data: months.map(m => monthMap[m] || 0) },
            { name: '关联采购', data: months.map(m => relatedMonthMap[m] || 0) },
          ],
        })
      }
    },
  },
}
</script>

<style lang="scss" scoped>
.page-container {
  padding: 16px;
  background: #F0F2F5;
  min-height: calc(100vh - 84px);
}
.page-header {
  padding: 18px 24px;
  margin-bottom: 16px;
  background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 60%, var(--ip-bright, #1677FF) 100%);
  border-radius: 8px;
  color: #fff;
  .page-title { margin: 0 0 4px 0; font-size: 18px; font-weight: 700; i { margin-right: 8px; } }
  .page-desc { margin: 0; font-size: 13px; opacity: .85; }
}
.stat-row { margin-bottom: 16px; }
.stat-card {
  background: #fff;
  border-radius: 6px;
  padding: 14px 16px;
  box-shadow: 0 1px 4px rgba(0,0,0,.07);
  .stat-val { font-size: 22px; font-weight: 700; }
  .stat-lb { font-size: 12px; color: #8C8C8C; margin-top: 4px; }
}
.chart-row { margin-bottom: 16px; }
.chart-sm { height: 210px; }
.search-card { margin-bottom: 12px; }
.card-hd { font-size: 14px; font-weight: 600; color: #303133; }
.legend-bar {
  margin-top: 10px;
  display: flex;
  gap: 16px;
  font-size: 12px;
  .legend-item { display: flex; align-items: center; gap: 4px; }
  .legend-related { color: #722ED1; }
  .legend-violation { color: #CF1322; }
  .legend-warning { color: #FA8C16; }
}
::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); font-weight: 600; }
::v-deep .el-table .row-violation td { background: #FFF1F0 !important; }
::v-deep .el-table .row-related td { background: #F9F0FF !important; }
::v-deep .el-table .row-warning td { background: #FFF7E6 !important; }
::v-deep .el-card { border-radius: 6px; }
</style>
