<template>
  <div class="military-page" :style="themeVars">
    <div class="page-header"><h2><i class="el-icon-s-order"></i> 合同履约追踪</h2><p>军品合同执行状态、交付进度与质量追踪</p></div>
    <el-alert v-if="overdueList.length>0" :title="'当前 '+overdueList.length+' 份军品合同已延期，请关注！'" type="warning" show-icon :closable="false" style="margin-bottom:12px" />
    <el-row :gutter="16" class="stat-row">
      <el-col :span="6"><div class="stat-card" style="border-left:3px solid #0050A0"><div class="stat-val" style="color:#0050A0">{{ stats.totalCount }}</div><div class="stat-label">合同总数</div></div></el-col>
      <el-col :span="6"><div class="stat-card" style="border-left:3px solid #237804"><div class="stat-val" style="color:#237804">{{ stats.completedCount }}</div><div class="stat-label">已完成</div></div></el-col>
      <el-col :span="6"><div class="stat-card" style="border-left:3px solid #CF1322"><div class="stat-val" style="color:#CF1322">{{ stats.overdueCount }}</div><div class="stat-label">已延期</div></div></el-col>
      <el-col :span="6"><div class="stat-card" style="border-left:3px solid #FA8C16"><div class="stat-val" style="color:#FA8C16">{{ total - stats.overdueCount - stats.completedCount }}</div><div class="stat-label">进行中</div></div></el-col>
    </el-row>
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" inline size="small">
        <el-form-item label="承制企业"><el-input v-model="queryForm.companyName" placeholder="请输入" clearable style="width:160px" /></el-form-item>
        <el-form-item label="客户单位"><el-input v-model="queryForm.customerUnit" placeholder="请输入" clearable style="width:140px" /></el-form-item>
        <el-form-item label="合同状态"><el-select v-model="queryForm.contractStatus" placeholder="请选择" clearable style="width:120px">
          <el-option label="进行中" value="IN_PROGRESS" /><el-option label="已延期" value="OVERDUE" /><el-option label="已完成" value="COMPLETED" />
        </el-select></el-form-item>
        <el-form-item label="密级"><el-select v-model="queryForm.secretLevel" placeholder="请选择" clearable style="width:100px">
          <el-option label="绝密" value="TOP_SECRET" /><el-option label="机密" value="SECRET" /><el-option label="秘密" value="CONFIDENTIAL" /><el-option label="内部" value="INTERNAL" />
        </el-select></el-form-item>
        <el-form-item><el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button><el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-row :gutter="16" style="margin-top:12px;margin-bottom:12px">
      <el-col :span="10"><el-card shadow="never"><div slot="header">合同状态分布</div><div ref="statusChart" class="chart-box-sm"></div></el-card></el-col>
      <el-col :span="14"><el-card shadow="never"><div slot="header">任务进度对比</div><div ref="progressChart" class="chart-box-sm"></div></el-card></el-col>
    </el-row>
    <el-card shadow="never" style="margin-top:10px">
      <div style="margin-bottom:10px"><el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增</el-button></div>
      <el-table v-loading="loading" :data="list" border style="width:100%" size="small">
        <el-table-column label="合同编号" prop="contractNo" width="120" />
        <el-table-column label="合同名称" prop="contractName" min-width="180" show-overflow-tooltip />
        <el-table-column label="承制企业" prop="companyName" min-width="140" show-overflow-tooltip />
        <el-table-column label="合同金额(万)" prop="contractAmount" width="110" align="right" />
        <el-table-column label="完成进度(%)" prop="progressRate" width="100" align="center" />
        <el-table-column label="验收状态" prop="acceptanceStatus" width="90" align="center">
          <template slot-scope="{row}"><el-tag :type="{PENDING:'info',REVIEWING:'warning',ACCEPTED:'success',REJECTED:'danger'}[row.acceptanceStatus]" size="mini">{{ {PENDING:'待验收',REVIEWING:'审查中',ACCEPTED:'已验收',REJECTED:'未通过'}[row.acceptanceStatus] || row.acceptanceStatus }}</el-tag></template>
        </el-table-column>
        <el-table-column label="是否逾期" prop="isOverdue" width="80" align="center">
          <template slot-scope="{row}"><el-tag :type="row.isOverdue==='1'?'danger':'success'" size="mini">{{ row.isOverdue==='1'?'已逾期':'正常' }}</el-tag></template>
        </el-table-column>
        <el-table-column label="开始日期" prop="startDate" width="110" align="center">
          <template slot-scope="{row}">{{ row.startDate || '-' }}</template>
        </el-table-column>
        <el-table-column label="截止日期" prop="endDate" width="110" align="center">
          <template slot-scope="{row}">{{ row.endDate || '-' }}</template>
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
    <el-dialog :title="{add:'新增合同',edit:'编辑合同',view:'查看合同'}[dialogType]" :visible.sync="dialogVisible" width="650px" :close-on-click-modal="false">
      <el-form :model="form" ref="form" label-width="110px" :rules="rules">
        <el-form-item label="企业名称" prop="companyName"><el-input v-model="form.companyName" :disabled="dialogType==='view'" /></el-form-item>
        <el-form-item label="合同编号"><el-input v-model="form.contractNo" :disabled="dialogType==='view'" /></el-form-item>
        <el-form-item label="合同名称" prop="contractName"><el-input v-model="form.contractName" :disabled="dialogType==='view'" /></el-form-item>
        <el-form-item label="合同金额(万)"><el-input-number v-model="form.contractAmount" :min="0" :disabled="dialogType==='view'" style="width:100%" /></el-form-item>
        <el-form-item label="完成进度(%)"><el-input-number v-model="form.progressRate" :min="0" :max="100" :disabled="dialogType==='view'" style="width:100%" /></el-form-item>
        <el-form-item label="验收状态"><el-select v-model="form.acceptanceStatus" :disabled="dialogType==='view'" style="width:100%"><el-option label="待验收" value="PENDING" /><el-option label="审查中" value="REVIEWING" /><el-option label="已验收" value="ACCEPTED" /><el-option label="未通过" value="REJECTED" /></el-select></el-form-item>
        <el-form-item label="是否逾期"><el-select v-model="form.isOverdue" :disabled="dialogType==='view'" style="width:100%"><el-option label="正常" value="0" /><el-option label="已逾期" value="1" /></el-select></el-form-item>
        <el-form-item label="开始日期"><el-date-picker v-model="form.startDate" type="date" value-format="yyyy-MM-dd" :disabled="dialogType==='view'" style="width:100%" /></el-form-item>
        <el-form-item label="截止日期"><el-date-picker v-model="form.endDate" type="date" value-format="yyyy-MM-dd" :disabled="dialogType==='view'" style="width:100%" /></el-form-item>
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
import { getMilitaryContractList, addMilitaryContract, updateMilitaryContract, deleteMilitaryContract, getContractStatistics } from '@/api/stateAssets/militaryPenetration'
import { investThemeMixin } from '../../themeMixin'
export default {
  name: 'MilitaryContractExecution',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false, submitLoading: false, list: [], total: 0,
      stats: { totalCount: 0, overdueCount: 0, completedCount: 0 },
      queryForm: { companyName: '', isOverdue: '', acceptanceStatus: '', pageNumber: 1, pageSize: 10 },
      dialogVisible: false, dialogType: 'add', form: {},
      rules: { companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }], contractName: [{ required: true, message: '请输入合同名称', trigger: 'blur' }] },
      charts: [],
    }
  },
  computed: {
    overdueList() { return this.list.filter(r => r.isOverdue === '1') },
  },
  created() { this.fetchData(); this.fetchStats() },
  mounted() { window.addEventListener('resize', this.handleResize) },
  beforeDestroy() { window.removeEventListener('resize', this.handleResize); this.charts.forEach(c => c.dispose()) },
  methods: {
    handleResize() { this.charts.forEach(c => c.resize()) },
    async fetchData() {
      this.loading = true
      try {
        const res = await getMilitaryContractList(this.queryForm)
        if (res && res.result === 200) { this.list = (res.data && res.data.tlist) || []; this.total = (res.data && res.data.totalRecord) || 0 }
        else { this.list = []; this.total = 0 }
      } catch (e) { this.list = []; this.total = 0 } finally { this.loading = false }
      this.$nextTick(() => this.initCharts())
    },
    async fetchStats() {
      try {
        const res = await getContractStatistics()
        if (res && res.result === 200) this.stats = res.data || this.stats
      } catch (e) { /* ignore */ }
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() { this.queryForm = { companyName: '', isOverdue: '', acceptanceStatus: '', pageNumber: 1, pageSize: 10 }; this.fetchData() },
    handleAdd() { this.dialogType = 'add'; this.form = {}; this.dialogVisible = true },
    handleView(row) { this.dialogType = 'view'; this.form = { ...row }; this.dialogVisible = true },
    handleEdit(row) { this.dialogType = 'edit'; this.form = { ...row }; this.dialogVisible = true },
    async handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return; this.submitLoading = true
        try {
          const res = await (this.dialogType === 'add' ? addMilitaryContract : updateMilitaryContract)(this.form)
          if (res && res.result === 200) { this.$message.success('操作成功'); this.dialogVisible = false; this.fetchData() } else { this.$message.error(res.msg || '操作失败') }
        } catch (e) { this.$message.error('操作失败') } finally { this.submitLoading = false }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除该合同？', '提示', { type: 'warning' }).then(async () => {
        const res = await deleteMilitaryContract(row.contractId)
        if (res && res.result === 200) { this.$message.success('删除成功'); this.fetchData() }
      }).catch(() => {})
    },
    initCharts() {
      if (this.$refs.statusChart) {
        const inProgress = this.list.filter(r => r.isOverdue !== '1' && r.acceptanceStatus !== 'ACCEPTED').length
        const overdue = this.list.filter(r => r.isOverdue === '1').length
        const completed = this.list.filter(r => r.acceptanceStatus === 'ACCEPTED').length
        const c1 = echarts.init(this.$refs.statusChart); this.charts.push(c1)
        c1.setOption({ tooltip: { trigger: 'item' }, series: [{ type: 'pie', radius: ['35%', '65%'], label: { formatter: '{b}\n{d}%' },
          data: [{ value: inProgress, name: '进行中', itemStyle: { color: this.ipBright } }, { value: overdue, name: '已延期', itemStyle: { color: '#CF1322' } }, { value: completed, name: '已完成', itemStyle: { color: '#237804' } }] }] })
      }
      if (this.$refs.progressChart) {
        const items = this.list.slice(0, 10)
        const c2 = echarts.init(this.$refs.progressChart); this.charts.push(c2)
        c2.setOption({ tooltip: { trigger: 'axis' }, grid: { left: 120, right: 60, bottom: 30, top: 20 },
          yAxis: { type: 'category', data: items.map(r => (r.contractName || '').slice(0, 8)) },
          xAxis: { type: 'value', name: '进度(%)', max: 100 },
          series: [{ type: 'bar', data: items.map(r => ({ value: r.progressRate || 0, itemStyle: { color: r.isOverdue === '1' ? '#CF1322' : this.ipBright } })), label: { show: true, position: 'right', formatter: '{c}%' } }] })
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
::v-deep .el-table .row-overdue td{background:#FFF1F0 !important;}
::v-deep .el-table .row-warning td{background:#FFF7E6 !important;}
::v-deep .el-card{border-radius:6px;}
</style>
