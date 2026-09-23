<template>
  <div class="risk-control-tab">
    <el-card class="overview-card">
      <div slot="header">
        <span>风险控制概览</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="refreshData">刷新</el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="6"><div class="stat-item"><div class="stat-icon" style="color: #409EFF"><i class="el-icon-shield"></i></div><div class="stat-info"><div class="stat-value">{{ overview.totalControls || 0 }}</div><div class="stat-title">控制措施数</div></div></div></el-col>
        <el-col :span="6"><div class="stat-item"><div class="stat-icon" style="color: #67C23A"><i class="el-icon-success"></i></div><div class="stat-info"><div class="stat-value">{{ overview.effectiveControls || 0 }}</div><div class="stat-title">有效措施数</div></div></div></el-col>
        <el-col :span="6"><div class="stat-item"><div class="stat-icon" style="color: #E6A23C"><i class="el-icon-data-analysis"></i></div><div class="stat-info"><div class="stat-value">{{ overview.effectivenessRate || 0 }}%</div><div class="stat-title">控制有效率</div></div></div></el-col>
        <el-col :span="6"><div class="stat-item"><div class="stat-icon" style="color: #F56C6C"><i class="el-icon-warning"></i></div><div class="stat-info"><div class="stat-value">{{ overview.improvementNeeded || 0 }}</div><div class="stat-title">待改进措施</div></div></div></el-col>
      </el-row>
    </el-card>
    <el-card class="table-card">
      <div slot="header">
        <span>风险控制措施</span>
        <div style="float: right;">
          <el-button type="primary" size="small" @click="showAddDialog">新建措施</el-button>
        </div>
      </div>
      <el-form :model="queryForm" :inline="true" class="query-form">
        <el-form-item label="措施类型">
          <el-select v-model="queryForm.measureType" placeholder="请选择" clearable>
            <el-option label="预防性控制" value="preventive"></el-option>
            <el-option label="检查性控制" value="detective"></el-option>
            <el-option label="纠正性控制" value="corrective"></el-option>
            <el-option label="补偿性控制" value="compensating"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="实施状态">
          <el-select v-model="queryForm.implementationStatus" placeholder="请选择" clearable>
            <el-option label="计划中" value="planned"></el-option>
            <el-option label="实施中" value="implementing"></el-option>
            <el-option label="已实施" value="implemented"></el-option>
            <el-option label="已暂停" value="suspended"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="有效性">
          <el-select v-model="queryForm.effectivenessLevel" placeholder="请选择" clearable>
            <el-option label="有效" value="effective"></el-option>
            <el-option label="部分有效" value="partially_effective"></el-option>
            <el-option label="无效" value="ineffective"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" @click="queryData">查询</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
      <el-table :data="controlList" border v-loading="loading">
        <el-table-column prop="measureName" label="措施名称" width="200"></el-table-column>
        <el-table-column prop="measureType" label="措施类型" width="120">
          <template slot-scope="scope"><el-tag :type="getTypeTag(scope.row.measureType)">{{ getTypeText(scope.row.measureType) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="targetRiskType" label="目标风险类型" width="120">
          <template slot-scope="scope">{{ getTargetRiskTypeText(scope.row.targetRiskType) }}</template>
        </el-table-column>
        <el-table-column prop="responsiblePerson" label="责任人" width="100"></el-table-column>
        <el-table-column prop="implementationStatus" label="实施状态" width="100">
          <template slot-scope="scope"><el-tag :type="getStatusType(scope.row.implementationStatus)">{{ getStatusText(scope.row.implementationStatus) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="effectivenessLevel" label="有效性" width="100">
          <template slot-scope="scope"><el-tag :type="getEffectivenessType(scope.row.effectivenessLevel)">{{ getEffectivenessText(scope.row.effectivenessLevel) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="implementationProgress" label="实施进度" width="120" align="center">
          <template slot-scope="scope">
            <el-progress :percentage="Number(scope.row.implementationProgress || 0)" :stroke-width="8"></el-progress>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="80" align="center">
          <template slot-scope="scope"><el-tag :type="getPriorityType(scope.row.priority)" size="small">{{ getPriorityText(scope.row.priority) }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="editControl(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="evaluateControl(scope.row)">评估</el-button>
            <el-button type="text" size="small" @click="deleteControl(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pagination.current" :page-sizes="[10, 20, 50, 100]" :page-size="pagination.size" layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" style="margin-top: 20px; text-align: right;"></el-pagination>
    </el-card>
    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="650px">
      <el-form :model="formData" label-width="100px">
        <el-form-item label="措施名称"><el-input v-model="formData.measureName"></el-input></el-form-item>
        <el-form-item label="措施类型">
          <el-select v-model="formData.measureType" style="width:100%">
            <el-option label="预防性控制" value="preventive"></el-option>
            <el-option label="检查性控制" value="detective"></el-option>
            <el-option label="纠正性控制" value="corrective"></el-option>
            <el-option label="补偿性控制" value="compensating"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="关联风险">
          <el-select v-model="formData.riskIdentificationId" style="width:100%" placeholder="请选择关联的风险" clearable filterable>
            <el-option v-for="risk in riskOptions" :key="risk.riskIdentificationId" :label="risk.riskName" :value="risk.riskIdentificationId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="目标风险类型">
          <el-select v-model="formData.targetRiskType" style="width:100%">
            <el-option label="财务风险" value="financial"></el-option><el-option label="运营风险" value="operational"></el-option>
            <el-option label="市场风险" value="market"></el-option><el-option label="技术风险" value="technical"></el-option>
            <el-option label="合规风险" value="compliance"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="formData.priority" style="width:100%">
            <el-option label="高" value="high"></el-option><el-option label="中" value="medium"></el-option><el-option label="低" value="low"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="责任人"><el-input v-model="formData.responsiblePerson"></el-input></el-form-item>
        <el-form-item label="措施描述"><el-input type="textarea" v-model="formData.measureDescription" :rows="3"></el-input></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="submitForm">确定</el-button></div>
    </el-dialog>
    <!-- 评估对话框 -->
    <el-dialog title="控制措施评估" :visible.sync="evalDialogVisible" width="500px">
      <el-form :model="evalForm" label-width="100px">
        <el-form-item label="有效性">
          <el-select v-model="evalForm.effectiveness" style="width:100%">
            <el-option label="有效" value="effective"></el-option>
            <el-option label="部分有效" value="partially_effective"></el-option>
            <el-option label="无效" value="ineffective"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="效果评分"><el-input-number v-model="evalForm.effectivenessScore" :min="0" :max="100" :precision="1"></el-input-number></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="evalDialogVisible = false">取消</el-button><el-button type="primary" @click="submitEval">确定</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import { getRiskControlList, addRiskControl, updateRiskControl, deleteRiskControl, evaluateControlEffectiveness, analyzeRiskControl, extractData } from '@/api/enterprise/risk'
import { getRiskIdentificationList } from '@/api/enterprise/risk'

export default {
  name: 'RiskControlTab',
  props: { enterpriseId: String, enterpriseName: String },
  data() {
    return {
      loading: false,
      queryForm: { measureType: '', implementationStatus: '', effectivenessLevel: '' },
      pagination: { current: 1, size: 20, total: 0 },
      overview: { totalControls: 0, effectiveControls: 0, effectivenessRate: 0, improvementNeeded: 0 },
      controlList: [],
      riskOptions: [],
      dialogVisible: false,
      dialogTitle: '新建控制措施',
      isEdit: false,
      formData: { measureName: '', measureType: '', targetRiskType: '', priority: 'medium', responsiblePerson: '', measureDescription: '', riskIdentificationId: '' },
      evalDialogVisible: false,
      evalForm: { controlMeasureId: '', effectiveness: '', effectivenessScore: 80 }
    }
  },
  watch: { enterpriseId: { handler(val) { if (val) this.loadData() }, immediate: true } },
  methods: {
    async loadData() {
      if (!this.enterpriseId) return
      this.loading = true
      try {
        const params = { enterpriseId: this.enterpriseId, pageNumber: this.pagination.current, pageSize: this.pagination.size, ...this.queryForm }
        const res = await getRiskControlList(params)
        const data = extractData(res)
        this.controlList = (data && (data.tlist || data.records)) || []
        this.pagination.total = (data && (data.totalRecord || data.total)) || 0
        this.loadOverview()
      } catch (e) { console.error('加载控制措施数据失败:', e) }
      finally { this.loading = false }
    },
    async loadOverview() {
      try {
        const res = await analyzeRiskControl({ enterpriseId: this.enterpriseId })
        this.overview = extractData(res) || {}
      } catch (e) { console.error('加载概览失败:', e) }
    },
    refreshData() { this.loadData(); this.$message.success('数据刷新成功') },
    queryData() { this.pagination.current = 1; this.loadData() },
    resetQuery() { this.queryForm = { measureType: '', implementationStatus: '', effectivenessLevel: '' }; this.queryData() },
    handleSizeChange(val) { this.pagination.size = val; this.loadData() },
    handleCurrentChange(val) { this.pagination.current = val; this.loadData() },
    showAddDialog() {
      this.isEdit = false; this.dialogTitle = '新建控制措施'
      this.formData = { measureName: '', measureType: '', targetRiskType: '', priority: 'medium', responsiblePerson: '', measureDescription: '', riskIdentificationId: '' }
      this.loadRiskOptions()
      this.dialogVisible = true
    },
    editControl(row) {
      this.isEdit = true; this.dialogTitle = '编辑控制措施'
      this.formData = { ...row }
      this.loadRiskOptions()
      this.dialogVisible = true
    },
    async loadRiskOptions() {
      try {
        const res = await getRiskIdentificationList({ enterpriseId: this.enterpriseId, pageNumber: 1, pageSize: 100 })
        const data = extractData(res)
        this.riskOptions = (data && (data.tlist || data.records)) || []
      } catch (e) { console.error('加载风险选项失败:', e) }
    },
    async submitForm() {
      try {
        const data = { ...this.formData, enterpriseId: this.enterpriseId, enterpriseName: this.enterpriseName, implementationStatus: this.isEdit ? this.formData.implementationStatus : 'planned' }
        if (this.isEdit) { await updateRiskControl(data) } else { await addRiskControl(data) }
        this.$message.success(this.isEdit ? '更新成功' : '新增成功')
        this.dialogVisible = false; this.loadData(); this.$emit('refresh')
      } catch (e) { this.$message.error('操作失败：' + e.message) }
    },
    evaluateControl(row) {
      this.evalForm = { controlMeasureId: row.controlMeasureId, effectiveness: row.effectivenessLevel || '', effectivenessScore: row.effectivenessScore || 80 }
      this.evalDialogVisible = true
    },
    async submitEval() {
      try {
        await evaluateControlEffectiveness(this.evalForm)
        this.$message.success('评估成功'); this.evalDialogVisible = false; this.loadData()
      } catch (e) { this.$message.error('评估失败：' + e.message) }
    },
    deleteControl(row) {
      this.$confirm('确认删除该控制措施？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(async () => {
        await deleteRiskControl(row.controlMeasureId); this.$message.success('删除成功'); this.loadData(); this.$emit('refresh')
      })
    },
    getTypeTag(type) { return { preventive: 'primary', detective: 'success', corrective: 'warning', compensating: 'info' }[type] || 'info' },
    getTypeText(type) { return { preventive: '预防性', detective: '检查性', corrective: '纠正性', compensating: '补偿性' }[type] || type },
    getStatusType(status) { return { planned: 'info', implementing: 'primary', implemented: 'success', suspended: 'warning' }[status] || 'info' },
    getStatusText(status) { return { planned: '计划中', implementing: '实施中', implemented: '已实施', suspended: '已暂停' }[status] || status },
    getEffectivenessType(eff) { return { effective: 'success', partially_effective: 'warning', ineffective: 'danger' }[eff] || 'info' },
    getEffectivenessText(eff) { return { effective: '有效', partially_effective: '部分有效', ineffective: '无效' }[eff] || eff || '-' },
    getPriorityType(p) { return { high: 'danger', medium: 'warning', low: 'success' }[p] || 'info' },
    getPriorityText(p) { return { high: '高', medium: '中', low: '低' }[p] || p || '-' },
    getTargetRiskTypeText(type) { return { financial: '财务风险', operational: '运营风险', market: '市场风险', technical: '技术风险', compliance: '合规风险' }[type] || type || '-' }
  }
}
</script>
<style scoped>
.risk-control-tab { padding: 20px; }
.overview-card { margin-bottom: 20px; }
.table-card { margin-bottom: 20px; }
.query-form { margin-bottom: 20px; }
.stat-item { display: flex; align-items: center; padding: 10px 0; }
.stat-icon { font-size: 28px; margin-right: 12px; }
.stat-info { flex: 1; }
.stat-value { font-size: 22px; font-weight: bold; color: #303133; line-height: 1.2; }
.stat-title { font-size: 13px; color: #909399; margin-top: 4px; }
</style>
