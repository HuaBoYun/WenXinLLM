<template>
  <div class="risk-incident-tab">
    <el-card class="overview-card">
      <div slot="header">
        <span>风险事件概览</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="refreshData">刷新</el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="6"><div class="stat-item"><div class="stat-icon" style="color: #409EFF"><i class="el-icon-warning"></i></div><div class="stat-info"><div class="stat-value">{{ overview.totalIncidents || 0 }}</div><div class="stat-title">事件总数</div></div></div></el-col>
        <el-col :span="6"><div class="stat-item"><div class="stat-icon" style="color: #F56C6C"><i class="el-icon-error"></i></div><div class="stat-info"><div class="stat-value">{{ overview.severeIncidents || 0 }}</div><div class="stat-title">严重事件</div></div></div></el-col>
        <el-col :span="6"><div class="stat-item"><div class="stat-icon" style="color: #E6A23C"><i class="el-icon-loading"></i></div><div class="stat-info"><div class="stat-value">{{ overview.processingIncidents || 0 }}</div><div class="stat-title">处理中事件</div></div></div></el-col>
        <el-col :span="6"><div class="stat-item"><div class="stat-icon" style="color: #67C23A"><i class="el-icon-success"></i></div><div class="stat-info"><div class="stat-value">{{ overview.completionRate || 0 }}%</div><div class="stat-title">处理完成率</div></div></div></el-col>
      </el-row>
    </el-card>
    <el-card class="table-card">
      <div slot="header">
        <span>风险事件列表</span>
        <div style="float: right;">
          <el-button type="primary" size="small" @click="showAddDialog">上报事件</el-button>
        </div>
      </div>
      <el-form :model="queryForm" :inline="true" class="query-form">
        <el-form-item label="事件类型">
          <el-select v-model="queryForm.incidentType" placeholder="请选择" clearable>
            <el-option label="财务损失" value="financial_loss"></el-option><el-option label="运营中断" value="operational_disruption"></el-option>
            <el-option label="安全事故" value="security_incident"></el-option><el-option label="合规违规" value="compliance_violation"></el-option>
            <el-option label="技术事件" value="technology_incident"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="queryForm.riskLevel" placeholder="请选择" clearable>
            <el-option label="重大" value="critical"></el-option><el-option label="严重" value="high"></el-option>
            <el-option label="一般" value="medium"></el-option><el-option label="轻微" value="low"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="处理状态">
          <el-select v-model="queryForm.handlingStatus" placeholder="请选择" clearable>
            <el-option label="待处理" value="pending"></el-option><el-option label="处理中" value="handling"></el-option>
            <el-option label="已完成" value="completed"></el-option><el-option label="已关闭" value="closed"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="queryData">查询</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
      <el-table :data="incidentList" border v-loading="loading">
        <el-table-column prop="incidentNumber" label="事件编号" width="120"></el-table-column>
        <el-table-column prop="incidentName" label="事件名称" width="200"></el-table-column>
        <el-table-column prop="incidentType" label="事件类型" width="120">
          <template slot-scope="scope"><el-tag :type="getIncidentTypeTag(scope.row.incidentType)">{{ getIncidentTypeText(scope.row.incidentType) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="riskLevel" label="风险等级" width="80">
          <template slot-scope="scope"><el-tag :type="getRiskLevelType(scope.row.riskLevel)">{{ getRiskLevelText(scope.row.riskLevel) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="reporter" label="上报人" width="100"></el-table-column>
        <el-table-column prop="occurrenceTime" label="发生时间" width="150"></el-table-column>
        <el-table-column prop="handlingResponsiblePerson" label="处理人" width="100">
          <template slot-scope="scope">{{ scope.row.handlingResponsiblePerson || '-' }}</template>
        </el-table-column>
        <el-table-column prop="totalEconomicLoss" label="经济损失" width="120" align="right">
          <template slot-scope="scope">{{ formatAmount(scope.row.totalEconomicLoss) }}</template>
        </el-table-column>
        <el-table-column prop="handlingStatus" label="处理状态" width="100">
          <template slot-scope="scope"><el-tag :type="getHandlingStatusType(scope.row.handlingStatus)">{{ getHandlingStatusText(scope.row.handlingStatus) }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="editIncident(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="handleIncident(scope.row)" v-if="scope.row.handlingStatus === 'pending'">处理</el-button>
            <el-button type="text" size="small" @click="closeIncident(scope.row)" v-if="scope.row.handlingStatus === 'completed'">关闭</el-button>
            <el-button type="text" size="small" @click="deleteIncident(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pagination.current" :page-sizes="[10, 20, 50, 100]" :page-size="pagination.size" layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" style="margin-top: 20px; text-align: right;"></el-pagination>
    </el-card>
    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="650px">
      <el-form :model="formData" label-width="100px">
        <el-form-item label="事件名称"><el-input v-model="formData.incidentName"></el-input></el-form-item>
        <el-form-item label="事件类型">
          <el-select v-model="formData.incidentType" style="width:100%">
            <el-option label="财务损失" value="financial_loss"></el-option><el-option label="运营中断" value="operational_disruption"></el-option>
            <el-option label="安全事故" value="security_incident"></el-option><el-option label="合规违规" value="compliance_violation"></el-option>
            <el-option label="技术事件" value="technology_incident"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select v-model="formData.riskLevel" style="width:100%">
            <el-option label="重大" value="critical"></el-option><el-option label="严重" value="high"></el-option>
            <el-option label="一般" value="medium"></el-option><el-option label="轻微" value="low"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="上报人"><el-input v-model="formData.reporter"></el-input></el-form-item>
        <el-form-item label="事件描述"><el-input type="textarea" v-model="formData.incidentDescription" :rows="3"></el-input></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="submitForm">确定</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import { getRiskIncidentList, addRiskIncident, updateRiskIncident, deleteRiskIncident, handleRiskIncident, closeRiskIncident, analyzeRiskIncident, extractData } from '@/api/enterprise/risk'

export default {
  name: 'RiskIncidentTab',
  props: { enterpriseId: String, enterpriseName: String },
  data() {
    return {
      loading: false,
      queryForm: { incidentType: '', riskLevel: '', handlingStatus: '' },
      pagination: { current: 1, size: 20, total: 0 },
      overview: { totalIncidents: 0, severeIncidents: 0, processingIncidents: 0, completionRate: 0 },
      incidentList: [],
      dialogVisible: false,
      dialogTitle: '上报风险事件',
      isEdit: false,
      formData: { incidentName: '', incidentType: '', riskLevel: 'medium', reporter: '', incidentDescription: '' }
    }
  },
  watch: { enterpriseId: { handler(val) { if (val) this.loadData() }, immediate: true } },
  methods: {
    async loadData() {
      if (!this.enterpriseId) return
      this.loading = true
      try {
        const params = { enterpriseId: this.enterpriseId, pageNumber: this.pagination.current, pageSize: this.pagination.size, ...this.queryForm }
        const res = await getRiskIncidentList(params)
        const data = extractData(res)
        this.incidentList = (data && (data.tlist || data.records)) || []
        this.pagination.total = (data && (data.totalRecord || data.total)) || 0
        this.loadOverview()
      } catch (e) { console.error('加载事件数据失败:', e) }
      finally { this.loading = false }
    },
    async loadOverview() {
      try {
        const res = await analyzeRiskIncident({ enterpriseId: this.enterpriseId })
        this.overview = extractData(res) || {}
      } catch (e) { console.error('加载概览失败:', e) }
    },
    refreshData() { this.loadData(); this.$message.success('数据刷新成功') },
    queryData() { this.pagination.current = 1; this.loadData() },
    resetQuery() { this.queryForm = { incidentType: '', riskLevel: '', handlingStatus: '' }; this.queryData() },
    handleSizeChange(val) { this.pagination.size = val; this.loadData() },
    handleCurrentChange(val) { this.pagination.current = val; this.loadData() },
    showAddDialog() {
      this.isEdit = false; this.dialogTitle = '上报风险事件'
      this.formData = { incidentName: '', incidentType: '', riskLevel: 'medium', reporter: '', incidentDescription: '' }
      this.dialogVisible = true
    },
    editIncident(row) {
      this.isEdit = true; this.dialogTitle = '编辑风险事件'
      this.formData = { ...row }; this.dialogVisible = true
    },
    async submitForm() {
      try {
        const data = { ...this.formData, enterpriseId: this.enterpriseId, enterpriseName: this.enterpriseName, handlingStatus: this.isEdit ? this.formData.handlingStatus : 'pending', incidentNumber: this.isEdit ? this.formData.incidentNumber : 'INC-' + Date.now() }
        if (this.isEdit) { await updateRiskIncident(data) } else { await addRiskIncident(data) }
        this.$message.success(this.isEdit ? '更新成功' : '上报成功')
        this.dialogVisible = false; this.loadData(); this.$emit('refresh')
      } catch (e) { this.$message.error('操作失败：' + e.message) }
    },
    handleIncident(row) {
      this.$confirm('确认开始处理该事件？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(async () => {
        await handleRiskIncident({ riskIncidentId: row.riskIncidentId })
        this.$message.success('已开始处理'); this.loadData(); this.$emit('refresh')
      })
    },
    closeIncident(row) {
      this.$prompt('请输入处理结果', '关闭事件', { confirmButtonText: '确定', cancelButtonText: '取消' }).then(async ({ value }) => {
        await closeRiskIncident({ riskIncidentId: row.riskIncidentId, resolution: value })
        this.$message.success('事件已关闭'); this.loadData(); this.$emit('refresh')
      })
    },
    deleteIncident(row) {
      this.$confirm('确认删除该风险事件？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(async () => {
        await deleteRiskIncident(row.riskIncidentId); this.$message.success('删除成功'); this.loadData(); this.$emit('refresh')
      })
    },
    formatAmount(amount) { if (!amount) return '0.00'; return (amount / 10000).toFixed(2) + '万' },
    getIncidentTypeTag(type) { return { financial_loss: 'danger', operational_disruption: 'warning', security_incident: 'primary', compliance_violation: 'info', technology_incident: 'success' }[type] || 'info' },
    getIncidentTypeText(type) { return { financial_loss: '财务损失', operational_disruption: '运营中断', security_incident: '安全事故', compliance_violation: '合规违规', technology_incident: '技术事件' }[type] || type },
    getRiskLevelType(level) { return { critical: 'danger', high: 'danger', medium: 'warning', low: 'success' }[level] || 'info' },
    getRiskLevelText(level) { return { critical: '重大', high: '严重', medium: '一般', low: '轻微' }[level] || level },
    getHandlingStatusType(status) { return { pending: 'info', handling: 'primary', completed: 'success', closed: 'warning' }[status] || 'info' },
    getHandlingStatusText(status) { return { pending: '待处理', handling: '处理中', completed: '已完成', closed: '已关闭' }[status] || status }
  }
}
</script>
<style scoped>
.risk-incident-tab { padding: 20px; }
.overview-card { margin-bottom: 20px; }
.table-card { margin-bottom: 20px; }
.query-form { margin-bottom: 20px; }
.stat-item { display: flex; align-items: center; padding: 10px 0; }
.stat-icon { font-size: 28px; margin-right: 12px; }
.stat-info { flex: 1; }
.stat-value { font-size: 22px; font-weight: bold; color: #303133; line-height: 1.2; }
.stat-title { font-size: 13px; color: #909399; margin-top: 4px; }
</style>
