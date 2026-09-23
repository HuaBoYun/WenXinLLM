<template>
  <div class="military-page" :style="themeVars">
    <div class="page-header"><h2><i class="el-icon-connection"></i> 供应链安全监控</h2><p>穿透识别供应链单点风险，境外依赖比统计与预警</p></div>
    <el-alert v-if="dangerList.length>0" :title="'检测到 '+dangerList.length+' 项高风险供应商，请立即关注！'" type="error" show-icon :closable="false" style="margin-bottom:12px" />
    <el-row :gutter="16" class="stat-row">
      <el-col :span="6"><div class="stat-card" style="border-left:3px solid #0050A0"><div class="stat-val" style="color:#0050A0">{{ stats.totalSuppliers }}</div><div class="stat-label">供应商总数</div></div></el-col>
      <el-col :span="6"><div class="stat-card" style="border-left:3px solid #237804"><div class="stat-val" style="color:#237804">{{ stats.domesticCount }}</div><div class="stat-label">国内供应商</div></div></el-col>
      <el-col :span="6"><div class="stat-card" style="border-left:3px solid #722ED1"><div class="stat-val" style="color:#722ED1">{{ stats.foreignCount }}</div><div class="stat-label">境外供应商</div></div></el-col>
      <el-col :span="6"><div class="stat-card" style="border-left:3px solid #CF1322"><div class="stat-val" style="color:#CF1322">{{ stats.singleSourceCount }}</div><div class="stat-label">单点依赖数</div></div></el-col>
    </el-row>
    <el-row :gutter="16" style="margin-bottom:12px">
      <el-col :span="10"><el-card shadow="never"><div slot="header">境外依赖比（集团平均18.6%）</div><div ref="gaugeChart" class="chart-box-sm"></div></el-card></el-col>
      <el-col :span="14"><el-card shadow="never"><div slot="header">供应商来源分布</div><div ref="sourceChart" class="chart-box-sm"></div></el-card></el-col>
    </el-row>
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" inline size="small">
        <el-form-item label="企业名称"><el-input v-model="queryForm.companyName" placeholder="请输入" clearable style="width:160px" /></el-form-item>
        <el-form-item label="供应商国籍"><el-select v-model="queryForm.isForeign" placeholder="请选择" clearable style="width:130px">
          <el-option label="境外供应商" value="yes" /><el-option label="国内供应商" value="no" />
        </el-select></el-form-item>
        <el-form-item label="物资类型"><el-select v-model="queryForm.materialType" placeholder="请选择" clearable style="width:130px">
          <el-option label="关键元器件" value="KEY_COMPONENT" /><el-option label="原材料" value="RAW_MATERIAL" /><el-option label="专用设备" value="SPECIAL_EQUIP" /><el-option label="软件" value="SOFTWARE" />
        </el-select></el-form-item>
        <el-form-item label="单点依赖"><el-select v-model="queryForm.isSingleSource" placeholder="请选择" clearable style="width:110px">
          <el-option label="是" value="yes" /><el-option label="否" value="no" />
        </el-select></el-form-item>
        <el-form-item><el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button><el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" style="margin-top:10px">
      <div style="margin-bottom:10px">
        <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增</el-button>
      </div>
      <el-table v-loading="loading" :data="list" border style="width:100%" size="small">
        <el-table-column label="采购企业" prop="companyName" min-width="140" show-overflow-tooltip />
        <el-table-column label="供应商名称" prop="supplierName" min-width="160" show-overflow-tooltip />
        <el-table-column label="物资类型" prop="materialType" width="120" align="center">
          <template slot-scope="{row}">{{ {KEY_COMPONENT:'关键元器件',RAW_MATERIAL:'原材料',SPECIAL_EQUIP:'专用设备',SOFTWARE:'软件'}[row.materialType] || row.materialType }}</template>
        </el-table-column>
        <el-table-column label="是否国产" prop="isDomestic" width="80" align="center">
          <template slot-scope="{row}"><el-tag :type="row.isDomestic==='1'?'success':'danger'" size="mini">{{ row.isDomestic==='1'?'国内':'境外' }}</el-tag></template>
        </el-table-column>
        <el-table-column label="原产国" prop="countryOrigin" width="80" align="center" />
        <el-table-column label="依赖程度" prop="dependencyLevel" width="90" align="center">
          <template slot-scope="{row}"><el-tag :type="{HIGH:'danger',MEDIUM:'warning',LOW:'success'}[row.dependencyLevel]" size="mini">{{ {HIGH:'高',MEDIUM:'中',LOW:'低'}[row.dependencyLevel] || row.dependencyLevel }}</el-tag></template>
        </el-table-column>
        <el-table-column label="单一来源" prop="isSingleSource" width="80" align="center">
          <template slot-scope="{row}"><el-tag :type="row.isSingleSource==='1'?'danger':'success'" size="mini">{{ row.isSingleSource==='1'?'是':'否' }}</el-tag></template>
        </el-table-column>
        <el-table-column label="风险等级" prop="riskLevel" width="80" align="center">
          <template slot-scope="{row}"><el-tag :type="{HIGH:'danger',MEDIUM:'warning',LOW:'success'}[row.riskLevel]" size="mini">{{ {HIGH:'高',MEDIUM:'中',LOW:'低'}[row.riskLevel] || row.riskLevel }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" style="color:#F56C6C" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination background style="margin-top:12px;text-align:right" :current-page="queryForm.pageNumber" :page-sizes="[10,20,50]" :page-size="queryForm.pageSize" layout="total,sizes,prev,pager,next,jumper" :total="total" @size-change="val=>{queryForm.pageSize=val;fetchData()}" @current-change="val=>{queryForm.pageNumber=val;fetchData()}" />
    </el-card>
    <el-dialog :title="{add:'新增供应链',edit:'编辑供应链',view:'查看供应链'}[dialogType]" :visible.sync="dialogVisible" width="600px" :close-on-click-modal="false">
      <el-form :model="form" ref="form" label-width="100px" :rules="rules">
        <el-form-item label="企业名称" prop="companyName"><el-input v-model="form.companyName" :disabled="dialogType==='view'" /></el-form-item>
        <el-form-item label="供应商名称" prop="supplierName"><el-input v-model="form.supplierName" :disabled="dialogType==='view'" /></el-form-item>
        <el-form-item label="物资类型"><el-select v-model="form.materialType" :disabled="dialogType==='view'" style="width:100%"><el-option label="关键元器件" value="KEY_COMPONENT" /><el-option label="原材料" value="RAW_MATERIAL" /><el-option label="专用设备" value="SPECIAL_EQUIP" /><el-option label="软件" value="SOFTWARE" /></el-select></el-form-item>
        <el-form-item label="是否国产"><el-select v-model="form.isDomestic" :disabled="dialogType==='view'" style="width:100%"><el-option label="国内" value="1" /><el-option label="境外" value="0" /></el-select></el-form-item>
        <el-form-item label="原产国"><el-input v-model="form.countryOrigin" :disabled="dialogType==='view'" /></el-form-item>
        <el-form-item label="依赖程度"><el-select v-model="form.dependencyLevel" :disabled="dialogType==='view'" style="width:100%"><el-option label="高" value="HIGH" /><el-option label="中" value="MEDIUM" /><el-option label="低" value="LOW" /></el-select></el-form-item>
        <el-form-item label="单一来源"><el-select v-model="form.isSingleSource" :disabled="dialogType==='view'" style="width:100%"><el-option label="是" value="1" /><el-option label="否" value="0" /></el-select></el-form-item>
        <el-form-item label="风险等级"><el-select v-model="form.riskLevel" :disabled="dialogType==='view'" style="width:100%"><el-option label="高" value="HIGH" /><el-option label="中" value="MEDIUM" /><el-option label="低" value="LOW" /></el-select></el-form-item>
      </el-form>
      <div slot="footer" v-if="dialogType!=='view'">
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getSupplyChainSecurityList, addSupplyChain, updateSupplyChain, deleteSupplyChain, getSupplyChainStatistics } from '@/api/stateAssets/militaryPenetration'
import { investThemeMixin } from '../../themeMixin'
export default {
  name: 'SupplyChainSecurity',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false, submitLoading: false, list: [], total: 0,
      stats: { totalSuppliers: 0, domesticCount: 0, foreignCount: 0, singleSourceCount: 0 },
      queryForm: { companyName: '', isDomestic: '', isSingleSource: '', materialType: '', pageNumber: 1, pageSize: 10 },
      dialogVisible: false, dialogType: 'add', form: {},
      rules: { companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }], supplierName: [{ required: true, message: '请输入供应商名称', trigger: 'blur' }] },
      charts: [],
    }
  },
  computed: {
    dangerList() { return this.list.filter(r => r.riskLevel === 'HIGH') },
  },
  created() { this.fetchData(); this.fetchStats() },
  mounted() { window.addEventListener('resize', this.handleResize) },
  beforeDestroy() { window.removeEventListener('resize', this.handleResize); this.charts.forEach(c => c.dispose()) },
  methods: {
    handleResize() { this.charts.forEach(c => c.resize()) },
    async fetchData() {
      this.loading = true
      try {
        const params = { ...this.queryForm }
        if (this.queryForm.isDomestic === 'yes') params.isDomestic = '1'
        else if (this.queryForm.isDomestic === 'no') params.isDomestic = '0'
        if (this.queryForm.isSingleSource === 'yes') params.isSingleSource = '1'
        else if (this.queryForm.isSingleSource === 'no') params.isSingleSource = '0'
        const res = await getSupplyChainSecurityList(params)
        if (res && res.result === 200) { this.list = (res.data && res.data.tlist) || []; this.total = (res.data && res.data.totalRecord) || 0 }
        else { this.list = []; this.total = 0 }
      } catch (e) { this.list = []; this.total = 0 } finally { this.loading = false }
      this.$nextTick(() => this.initCharts())
    },
    async fetchStats() {
      try {
        const res = await getSupplyChainStatistics()
        if (res && res.result === 200) this.stats = res.data || this.stats
      } catch (e) { /* ignore */ }
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() { this.queryForm = { companyName: '', isDomestic: '', isSingleSource: '', materialType: '', pageNumber: 1, pageSize: 10 }; this.fetchData() },
    handleAdd() { this.dialogType = 'add'; this.form = {}; this.dialogVisible = true },
    handleView(row) { this.dialogType = 'view'; this.form = { ...row }; this.dialogVisible = true },
    handleEdit(row) { this.dialogType = 'edit'; this.form = { ...row }; this.dialogVisible = true },
    async handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return; this.submitLoading = true
        try {
          const res = await (this.dialogType === 'add' ? addSupplyChain : updateSupplyChain)(this.form)
          if (res && res.result === 200) { this.$message.success('操作成功'); this.dialogVisible = false; this.fetchData() } else { this.$message.error(res.msg || '操作失败') }
        } catch (e) { this.$message.error('操作失败') } finally { this.submitLoading = false }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除该供应链记录？', '提示', { type: 'warning' }).then(async () => {
        const res = await deleteSupplyChain(row.chainId)
        if (res && res.result === 200) { this.$message.success('删除成功'); this.fetchData() }
      }).catch(() => {})
    },
    initCharts() {
      if (this.$refs.gaugeChart) {
        const foreign = this.stats.foreignCount || 0; const total = this.stats.totalSuppliers || 1
        const ratio = Math.round(foreign * 1000 / total) / 10
        const c1 = echarts.init(this.$refs.gaugeChart); this.charts.push(c1)
        c1.setOption({ series: [{ type: 'gauge', startAngle: 200, endAngle: -20, min: 0, max: 60,
          axisLine: { lineStyle: { width: 12, color: [[0.25, '#52C41A'], [0.5, '#FA8C16'], [1, '#CF1322']] } },
          pointer: { itemStyle: { color: this.ipSecondary } }, axisTick: { show: false }, splitLine: { length: 10, lineStyle: { color: '#fff' } },
          axisLabel: { color: '#8C8C8C', fontSize: 11, formatter: '{value}%' },
          detail: { formatter: '{value}%', fontSize: 18, color: '#722ED1', offsetCenter: [0, '60%'] },
          data: [{ value: ratio, name: '集团境外依赖比' }] }] })
      }
      if (this.$refs.sourceChart) {
        const companies = [...new Set(this.list.map(r => r.companyName))].slice(0, 8)
        const domestic = companies.map(c => this.list.filter(r => r.companyName === c && r.isDomestic === '1').length * 1000)
        const foreign = companies.map(c => this.list.filter(r => r.companyName === c && r.isDomestic === '0').length * 1000)
        const c2 = echarts.init(this.$refs.sourceChart); this.charts.push(c2)
        c2.setOption({ tooltip: { trigger: 'axis' }, legend: { data: ['国内', '境外'] }, grid: { left: 120, right: 30, bottom: 30, top: 40 },
          yAxis: { type: 'category', data: companies }, xAxis: { type: 'value', name: '供应商数' },
          series: [{ name: '国内', type: 'bar', stack: 'total', data: domestic, itemStyle: { color: '#237804' } }, { name: '境外', type: 'bar', stack: 'total', data: foreign, itemStyle: { color: '#722ED1' } }] })
      }
    },
  },
}
</script>
<style lang="scss" scoped>
.military-page { padding:16px; background:#f0f2f5; min-height:calc(100vh - 84px); }
.page-header { display:flex; align-items:center; justify-content:space-between; margin-bottom:16px; padding:18px 24px; background:linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 60%, var(--ip-bright, #1677FF) 100%); border-radius:6px; color:#fff;
  h2{font-size:20px;margin:0 0 4px;i{margin-right:8px;}} p{font-size:13px;opacity:0.85;margin:0;} }
.stat-row { margin-bottom:16px; }
.stat-card { background:#fff; border-radius:6px; padding:16px 14px; box-shadow:0 1px 4px rgba(0,0,0,0.08);
  .stat-val{font-size:22px;font-weight:700;line-height:1.2;} .stat-label{font-size:12px;color:#8C8C8C;margin-top:4px;} }
.search-card { margin-bottom:0; ::v-deep .el-form-item { margin-bottom:0; } }
.chart-box-sm { height:220px; width:100%; }
::v-deep .el-table th { background:var(--ip-light-bg, #EBF1FF) !important; color:var(--ip-secondary, #0050A0); font-weight:600; }
::v-deep .el-table .row-foreign td { background:#F9F0FF !important; }
::v-deep .el-table .row-danger td { background:#FFF1F0 !important; }
::v-deep .el-table .row-warning td { background:#FFF7E6 !important; }
::v-deep .el-card { border-radius:6px; }
</style>
