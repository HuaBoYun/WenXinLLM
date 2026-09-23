<template>
  <div class="military-page" :style="themeVars">
    <div class="page-header"><h2><i class="el-icon-document-checked"></i> 分包合规分析</h2><p>分包审批合规性、保密协议签署与人员资质核查</p></div>
    <el-row :gutter="16" class="stat-row">
      <el-col :span="6"><div class="stat-card" style="border-left:3px solid #0050A0"><div class="stat-val" style="color:#0050A0">{{ stats.totalCount }}</div><div class="stat-label">分包项目数</div></div></el-col>
      <el-col :span="6"><div class="stat-card" style="border-left:3px solid #237804"><div class="stat-val" style="color:#237804">{{ stats.compliantCount }}</div><div class="stat-label">合规项目数</div></div></el-col>
      <el-col :span="6"><div class="stat-card" style="border-left:3px solid #CF1322"><div class="stat-val" style="color:#CF1322">{{ stats.violationCount }}</div><div class="stat-label">违规项目数</div></div></el-col>
      <el-col :span="6"><div class="stat-card" style="border-left:3px solid #FA8C16"><div class="stat-val" style="color:#FA8C16">{{ stats.pendingCount }}</div><div class="stat-label">待审批数</div></div></el-col>
    </el-row>
    <el-row :gutter="16" style="margin-bottom:12px">
      <el-col :span="12"><el-card shadow="never"><div slot="header">分包方式合规分布</div><div ref="barChart" class="chart-box-sm"></div></el-card></el-col>
      <el-col :span="12"><el-card shadow="never"><div slot="header">企业分包合规率</div><div ref="radarChart" class="chart-box-sm"></div></el-card></el-col>
    </el-row>
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" inline size="small">
        <el-form-item label="主承制企业"><el-input v-model="queryForm.companyName" placeholder="请输入" clearable style="width:160px" /></el-form-item>
        <el-form-item label="分包类型"><el-select v-model="queryForm.subcontractType" placeholder="请选择" clearable style="width:120px">
          <el-option label="科研分包" value="RESEARCH" /><el-option label="生产分包" value="PRODUCTION" /><el-option label="技术服务" value="TECHNICAL_SERVICE" />
        </el-select></el-form-item>
        <el-form-item label="合规状态"><el-select v-model="queryForm.complianceStatus" placeholder="请选择" clearable style="width:120px">
          <el-option label="合规" value="COMPLIANT" /><el-option label="违规" value="VIOLATION" /><el-option label="待核查" value="PENDING" />
        </el-select></el-form-item>
        <el-form-item><el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button><el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" style="margin-top:10px">
      <div style="margin-bottom:10px"><el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增</el-button></div>
      <el-table v-loading="loading" :data="list" border style="width:100%" size="small">
        <el-table-column label="主承制企业" prop="companyName" min-width="140" show-overflow-tooltip />
        <el-table-column label="任务名称" prop="taskName" min-width="160" show-overflow-tooltip />
        <el-table-column label="分包方" prop="subcontractor" min-width="150" show-overflow-tooltip />
        <el-table-column label="分包类型" prop="subcontractType" width="100" align="center">
          <template slot-scope="{row}">{{ subcontractTypeMap[row.subcontractType] || row.subcontractType }}</template>
        </el-table-column>
        <el-table-column label="分包金额(万)" prop="subAmount" width="110" align="right" />
        <el-table-column label="审批状态" prop="approvalStatus" width="90" align="center">
          <template slot-scope="{row}"><el-tag :type="{APPROVED:'success',PENDING:'warning',REJECTED:'danger'}[row.approvalStatus]" size="mini">{{ {APPROVED:'已审批',PENDING:'待审批',REJECTED:'已拒绝'}[row.approvalStatus] || row.approvalStatus }}</el-tag></template>
        </el-table-column>
        <el-table-column label="是否合规" prop="isCompliant" width="80" align="center">
          <template slot-scope="{row}"><el-tag :type="row.isCompliant==='1'?'success':'danger'" size="mini">{{ row.isCompliant==='1'?'合规':'违规' }}</el-tag></template>
        </el-table-column>
        <el-table-column label="审批人" prop="approveUser" width="90" align="center" />
        <el-table-column label="审批日期" prop="approveDate" width="110" align="center">
          <template slot-scope="{row}">{{ row.approveDate || '-' }}</template>
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
    <el-dialog :title="{add:'新增分包',edit:'编辑分包',view:'查看分包'}[dialogType]" :visible.sync="dialogVisible" width="600px" :close-on-click-modal="false">
      <el-form :model="form" ref="form" label-width="100px" :rules="rules">
        <el-form-item label="企业名称" prop="companyName"><el-input v-model="form.companyName" :disabled="dialogType==='view'" /></el-form-item>
        <el-form-item label="任务名称" prop="taskName"><el-input v-model="form.taskName" :disabled="dialogType==='view'" /></el-form-item>
        <el-form-item label="分包方"><el-input v-model="form.subcontractor" :disabled="dialogType==='view'" /></el-form-item>
        <el-form-item label="分包类型"><el-select v-model="form.subcontractType" :disabled="dialogType==='view'" style="width:100%"><el-option label="科研分包" value="RESEARCH" /><el-option label="生产分包" value="PRODUCTION" /><el-option label="技术服务" value="TECHNICAL_SERVICE" /></el-select></el-form-item>
        <el-form-item label="分包金额(万)"><el-input-number v-model="form.subAmount" :min="0" :disabled="dialogType==='view'" style="width:100%" /></el-form-item>
        <el-form-item label="审批状态"><el-select v-model="form.approvalStatus" :disabled="dialogType==='view'" style="width:100%"><el-option label="待审批" value="PENDING" /><el-option label="已审批" value="APPROVED" /><el-option label="已拒绝" value="REJECTED" /></el-select></el-form-item>
        <el-form-item label="是否合规"><el-select v-model="form.isCompliant" :disabled="dialogType==='view'" style="width:100%"><el-option label="合规" value="1" /><el-option label="违规" value="0" /></el-select></el-form-item>
        <el-form-item label="违规描述"><el-input v-model="form.violationDesc" type="textarea" :rows="2" :disabled="dialogType==='view'" /></el-form-item>
        <el-form-item label="审批人"><el-input v-model="form.approveUser" :disabled="dialogType==='view'" /></el-form-item>
        <el-form-item label="审批日期"><el-date-picker v-model="form.approveDate" type="date" value-format="yyyy-MM-dd" :disabled="dialogType==='view'" style="width:100%" /></el-form-item>
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
import { getSubcontractComplianceList, addSubcontract, updateSubcontract, deleteSubcontract, getSubcontractStatistics } from '@/api/stateAssets/militaryPenetration'
import { investThemeMixin } from '../../themeMixin'
export default {
  name: 'SubcontractCompliance',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false, submitLoading: false, list: [], total: 0,
      stats: { totalCount: 0, compliantCount: 0, violationCount: 0, pendingCount: 0 },
      queryForm: { companyName: '', subcontractType: '', complianceStatus: '', pageNumber: 1, pageSize: 10 },
      dialogVisible: false, dialogType: 'add', form: {},
      rules: { companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }], taskName: [{ required: true, message: '请输入任务名称', trigger: 'blur' }] },
      subcontractTypeMap: { RESEARCH: '科研分包', PRODUCTION: '生产分包', TECHNICAL_SERVICE: '技术服务' },
      charts: [],
    }
  },
  created() { this.fetchData(); this.fetchStats() },
  mounted() { window.addEventListener('resize', this.handleResize) },
  beforeDestroy() { window.removeEventListener('resize', this.handleResize); this.charts.forEach(c => c.dispose()) },
  methods: {
    handleResize() { this.charts.forEach(c => c.resize()) },
    async fetchData() {
      this.loading = true
      try {
        const res = await getSubcontractComplianceList(this.queryForm)
        if (res && res.result === 200) { this.list = (res.data && res.data.tlist) || []; this.total = (res.data && res.data.totalRecord) || 0 }
        else { this.list = []; this.total = 0 }
      } catch (e) { this.list = []; this.total = 0 } finally { this.loading = false }
      this.$nextTick(() => this.initCharts())
    },
    async fetchStats() {
      try {
        const res = await getSubcontractStatistics()
        if (res && res.result === 200) this.stats = res.data || this.stats
      } catch (e) { /* ignore */ }
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() { this.queryForm = { companyName: '', subcontractType: '', complianceStatus: '', pageNumber: 1, pageSize: 10 }; this.fetchData() },
    handleAdd() { this.dialogType = 'add'; this.form = {}; this.dialogVisible = true },
    handleView(row) { this.dialogType = 'view'; this.form = { ...row }; this.dialogVisible = true },
    handleEdit(row) { this.dialogType = 'edit'; this.form = { ...row }; this.dialogVisible = true },
    async handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return; this.submitLoading = true
        try {
          const res = await (this.dialogType === 'add' ? addSubcontract : updateSubcontract)(this.form)
          if (res && res.result === 200) { this.$message.success('操作成功'); this.dialogVisible = false; this.fetchData() } else { this.$message.error(res.msg || '操作失败') }
        } catch (e) { this.$message.error('操作失败') } finally { this.submitLoading = false }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除该分包记录？', '提示', { type: 'warning' }).then(async () => {
        const res = await deleteSubcontract(row.subId)
        if (res && res.result === 200) { this.$message.success('删除成功'); this.fetchData() }
      }).catch(() => {})
    },
    initCharts() {
      // 销毁旧图表避免重复
      this.charts.forEach(c => c.dispose())
      this.charts = []
      // 分包方式合规分布（堆叠条形图）
      if (this.$refs.barChart) {
        const companies = [...new Set(this.list.map(r => r.companyName))].slice(0, 6)
        const compliant = companies.map(c => this.list.filter(r => r.companyName === c && r.isCompliant === '1').length)
        const violation = companies.map(c => this.list.filter(r => r.companyName === c && r.isCompliant === '0').length)
        const pending = companies.map(c => this.list.filter(r => r.companyName === c && r.approvalStatus === 'PENDING').length)
        const c1 = echarts.init(this.$refs.barChart); this.charts.push(c1)
        c1.setOption({ tooltip: { trigger: 'axis' }, legend: { data: ['合规', '违规', '待审批'] }, grid: { left: 100, right: 30, bottom: 30, top: 40 },
          yAxis: { type: 'category', data: companies }, xAxis: { type: 'value' },
          series: [{ name: '合规', type: 'bar', stack: 't', data: compliant, itemStyle: { color: '#237804' } }, { name: '违规', type: 'bar', stack: 't', data: violation, itemStyle: { color: '#CF1322' } }, { name: '待审批', type: 'bar', stack: 't', data: pending, itemStyle: { color: '#FA8C16' } }] })
      }
      // 企业分包合规率（雷达图）
      if (this.$refs.radarChart) {
        const companies = [...new Set(this.list.map(r => r.companyName))].slice(0, 6)
        const indicators = companies.map(name => ({ name: name.length > 6 ? name.slice(0, 6) + '…' : name, max: 100 }))
        const rateData = companies.map(c => {
          const total = this.list.filter(r => r.companyName === c).length
          const pass = this.list.filter(r => r.companyName === c && r.isCompliant === '1').length
          return total > 0 ? Math.round(pass / total * 100) : 0
        })
        const c2 = echarts.init(this.$refs.radarChart); this.charts.push(c2)
        c2.setOption({
          tooltip: { trigger: 'item' },
          radar: {
            indicator: indicators.length ? indicators : [{ name: '暂无数据', max: 100 }],
            shape: 'polygon',
            splitNumber: 4,
            name: { textStyle: { color: '#333', fontSize: 11 } },
            splitArea: { areaStyle: { color: ['#fff', '#F0F5FF', '#E6F0FF', '#D6E8FF'] } },
          },
          series: [{
            type: 'radar',
            data: [{
              value: rateData.length ? rateData : [0],
              name: '合规率(%)',
              areaStyle: { color: 'rgba(0,80,160,0.2)' },
              lineStyle: { color: this.ipSecondary, width: 2 },
              itemStyle: { color: this.ipSecondary },
            }],
          }],
        })
      }
    },
  },
}
</script>
<style lang="scss" scoped>
.military-page{padding:16px;background:#f0f2f5;min-height:calc(100vh - 84px);}
.page-header{display:flex;align-items:center;justify-content:space-between;margin-bottom:16px;padding:18px 24px;background:linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 60%, var(--ip-bright, #1677FF) 100%);border-radius:6px;color:#fff;
  h2{font-size:20px;margin:0 0 4px;i{margin-right:8px;}}p{font-size:13px;opacity:0.85;margin:0;}}
.stat-row{margin-bottom:16px;}
.stat-card{background:#fff;border-radius:6px;padding:16px 14px;box-shadow:0 1px 4px rgba(0,0,0,0.08);
  .stat-val{font-size:22px;font-weight:700;line-height:1.2;}.stat-label{font-size:12px;color:#8C8C8C;margin-top:4px;}}
.search-card{margin-bottom:0;::v-deep .el-form-item{margin-bottom:0;}}
.chart-box-sm{height:220px;width:100%;}
::v-deep .el-table th{background:var(--ip-light-bg, #EBF1FF) !important;color:var(--ip-secondary, #0050A0);font-weight:600;}
::v-deep .el-table .row-violation td{background:#FFF1F0 !important;}
::v-deep .el-table .row-pending td{background:#FFF7E6 !important;}
::v-deep .el-card{border-radius:6px;}
</style>
