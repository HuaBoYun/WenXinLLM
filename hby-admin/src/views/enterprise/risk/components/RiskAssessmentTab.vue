<template>
  <div class="risk-assessment-tab">
    <el-card class="overview-card">
      <div slot="header"><span>风险评估概览</span><el-button style="float: right; padding: 3px 0" type="text" @click="refreshData">刷新</el-button></div>
      <el-row :gutter="20">
        <el-col :span="6"><div class="stat-item"><div class="stat-icon" style="color: #409EFF"><i class="el-icon-document"></i></div><div class="stat-info"><div class="stat-value">{{ overview.totalAssessments || 0 }}</div><div class="stat-title">评估总数</div></div></div></el-col>
        <el-col :span="6"><div class="stat-item"><div class="stat-icon" style="color: #F56C6C"><i class="el-icon-error"></i></div><div class="stat-info"><div class="stat-value">{{ overview.highRiskCount || 0 }}</div><div class="stat-title">高风险数</div></div></div></el-col>
        <el-col :span="6"><div class="stat-item"><div class="stat-icon" style="color: #E6A23C"><i class="el-icon-warning"></i></div><div class="stat-info"><div class="stat-value">{{ overview.mediumRiskCount || 0 }}</div><div class="stat-title">中风险数</div></div></div></el-col>
        <el-col :span="6"><div class="stat-item"><div class="stat-icon" style="color: #67C23A"><i class="el-icon-success"></i></div><div class="stat-info"><div class="stat-value">{{ overview.lowRiskCount || 0 }}</div><div class="stat-title">低风险数</div></div></div></el-col>
      </el-row>
    </el-card>
    <el-card class="table-card">
      <div slot="header"><span>风险评估列表</span><div style="float: right;"><el-button type="primary" size="small" @click="showAddDialog">新建评估</el-button><el-button type="success" size="small" @click="exportData">导出数据</el-button></div></div>
      <el-form :model="queryForm" :inline="true" class="query-form">
        <el-form-item label="评估类型"><el-select v-model="queryForm.assessmentType" placeholder="请选择" clearable><el-option label="综合评估" value="COMPREHENSIVE"></el-option><el-option label="财务风险" value="FINANCIAL"></el-option><el-option label="经营风险" value="OPERATIONAL"></el-option><el-option label="合规风险" value="COMPLIANCE"></el-option><el-option label="治理风险" value="GOVERNANCE"></el-option></el-select></el-form-item>
        <el-form-item label="风险等级"><el-select v-model="queryForm.overallRiskLevel" placeholder="请选择" clearable><el-option label="高风险" value="HIGH"></el-option><el-option label="中风险" value="MEDIUM"></el-option><el-option label="低风险" value="LOW"></el-option></el-select></el-form-item>
        <el-form-item label="评估状态"><el-select v-model="queryForm.assessmentStatus" placeholder="请选择" clearable><el-option label="草稿" value="DRAFT"></el-option><el-option label="评估中" value="IN_PROGRESS"></el-option><el-option label="已完成" value="COMPLETED"></el-option><el-option label="待审核" value="PENDING"></el-option></el-select></el-form-item>
        <el-form-item><el-button type="primary" @click="queryData">查询</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
      <el-table :data="assessmentList" border v-loading="loading">
        <el-table-column prop="assessmentName" label="评估名称" width="200"></el-table-column>
        <el-table-column prop="assessmentType" label="风险类型" width="120"><template slot-scope="scope"><el-tag :type="getTypeTag(scope.row.assessmentType)">{{ getTypeText(scope.row.assessmentType) }}</el-tag></template></el-table-column>
        <el-table-column prop="overallRiskLevel" label="风险等级" width="100"><template slot-scope="scope"><el-tag :type="getLevelType(scope.row.overallRiskLevel)">{{ getLevelText(scope.row.overallRiskLevel) }}</el-tag></template></el-table-column>
        <el-table-column prop="overallRiskScore" label="风险评分" width="100" align="center"><template slot-scope="scope"><span :class="getScoreClass(scope.row.overallRiskScore)">{{ scope.row.overallRiskScore }}</span></template></el-table-column>
        <el-table-column prop="assessor" label="评估人" width="100"></el-table-column>
        <el-table-column prop="assessmentDate" label="评估日期" width="120"></el-table-column>
        <el-table-column prop="assessmentStatus" label="状态" width="100"><template slot-scope="scope"><el-tag :type="getStatusType(scope.row.assessmentStatus)">{{ getStatusText(scope.row.assessmentStatus) }}</el-tag></template></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="viewDetail(scope.row)">详情</el-button>
            <el-button type="text" size="small" @click="editAssessment(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="submitAssessment(scope.row)" v-if="scope.row.assessmentStatus === 'DRAFT'">提交</el-button>
            <el-button type="text" size="small" @click="deleteAssessment(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pagination.current" :page-sizes="[10, 20, 50, 100]" :page-size="pagination.size" layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" style="margin-top: 20px; text-align: right;"></el-pagination>
    </el-card>
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form :model="formData" label-width="100px">
        <el-form-item label="评估名称"><el-input v-model="formData.assessmentName"></el-input></el-form-item>
        <el-form-item label="评估类型"><el-select v-model="formData.assessmentType" style="width:100%"><el-option label="综合评估" value="COMPREHENSIVE"></el-option><el-option label="财务风险" value="FINANCIAL"></el-option><el-option label="经营风险" value="OPERATIONAL"></el-option><el-option label="合规风险" value="COMPLIANCE"></el-option></el-select></el-form-item>
        <el-form-item label="评估方法"><el-select v-model="formData.assessmentMethod" style="width:100%"><el-option label="定量评估" value="QUANTITATIVE"></el-option><el-option label="定性评估" value="QUALITATIVE"></el-option><el-option label="混合评估" value="MIXED"></el-option></el-select></el-form-item>
        <el-form-item label="风险等级"><el-select v-model="formData.overallRiskLevel" style="width:100%"><el-option label="高风险" value="HIGH"></el-option><el-option label="中风险" value="MEDIUM"></el-option><el-option label="低风险" value="LOW"></el-option></el-select></el-form-item>
        <el-form-item label="风险评分"><el-input-number v-model="formData.overallRiskScore" :min="0" :max="100" :precision="1" style="width:100%"></el-input-number></el-form-item>
        <el-form-item label="评估日期"><el-date-picker v-model="formData.assessmentDate" type="date" placeholder="选择评估日期" format="yyyy-MM-dd" value-format="yyyy-MM-dd" style="width:100%"></el-date-picker></el-form-item>
        <el-form-item label="评估人"><el-input v-model="formData.assessor"></el-input></el-form-item>
        <el-form-item label="评估结论"><el-input type="textarea" v-model="formData.assessmentConclusion" :rows="3"></el-input></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="submitForm">确定</el-button></div>
    </el-dialog>
    <!-- 详情弹窗 -->
    <el-dialog title="评估详情" :visible.sync="detailDialogVisible" width="700px">
      <el-descriptions :column="2" border v-if="detailData">
        <el-descriptions-item label="评估名称">{{ detailData.assessmentName }}</el-descriptions-item>
        <el-descriptions-item label="评估类型"><el-tag :type="getTypeTag(detailData.assessmentType)">{{ getTypeText(detailData.assessmentType) }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="风险等级"><el-tag :type="getLevelType(detailData.overallRiskLevel)">{{ getLevelText(detailData.overallRiskLevel) }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="风险评分"><span :class="getScoreClass(detailData.overallRiskScore)">{{ detailData.overallRiskScore }}</span></el-descriptions-item>
        <el-descriptions-item label="评估方法">{{ { QUANTITATIVE: '定量评估', QUALITATIVE: '定性评估', MIXED: '混合评估' }[detailData.assessmentMethod] || detailData.assessmentMethod }}</el-descriptions-item>
        <el-descriptions-item label="评估人">{{ detailData.assessor }}</el-descriptions-item>
        <el-descriptions-item label="评估日期">{{ detailData.assessmentDate }}</el-descriptions-item>
        <el-descriptions-item label="状态"><el-tag :type="getStatusType(detailData.assessmentStatus)">{{ getStatusText(detailData.assessmentStatus) }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="所属企业" :span="2">{{ detailData.enterpriseName }}</el-descriptions-item>
        <el-descriptions-item label="评估结论" :span="2">{{ detailData.assessmentConclusion || '暂无' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detailData.updateTime || '-' }}</el-descriptions-item>
      </el-descriptions>
      <!-- 关联的风险识别记录 -->
      <div style="margin-top: 20px;" v-if="detailData && detailData.relatedRisks && detailData.relatedRisks.length > 0">
        <h4 style="margin-bottom: 10px;">关联风险识别</h4>
        <el-table :data="detailData.relatedRisks" border size="small">
          <el-table-column prop="riskName" label="风险名称" width="180"></el-table-column>
          <el-table-column prop="riskType" label="类型" width="100"><template slot-scope="scope">{{ { financial: '财务', operational: '运营', market: '市场', technical: '技术', compliance: '合规' }[scope.row.riskType] || scope.row.riskType }}</template></el-table-column>
          <el-table-column prop="riskLevel" label="等级" width="80"><template slot-scope="scope"><el-tag :type="getLevelType(scope.row.riskLevel)" size="small">{{ getLevelText(scope.row.riskLevel) }}</el-tag></template></el-table-column>
          <el-table-column prop="status" label="状态" width="80"><template slot-scope="scope"><el-tag size="small">{{ { identified: '已识别', pending: '待确认', confirmed: '已确认' }[scope.row.status] || scope.row.status }}</el-tag></template></el-table-column>
        </el-table>
      </div>
      <div slot="footer"><el-button @click="detailDialogVisible = false">关闭</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import { getRiskAssessmentList, getRiskAssessmentById, addRiskAssessment, updateRiskAssessment, deleteRiskAssessment, submitRiskAssessment, analyzeRiskAssessment, extractData } from '@/api/enterprise/risk'
import { getRiskIdentificationList } from '@/api/enterprise/risk'

export default {
  name: 'RiskAssessmentTab',
  props: { enterpriseId: String, enterpriseName: String },
  data() {
    return {
      loading: false, queryForm: { assessmentType: '', overallRiskLevel: '', assessmentStatus: '' },
      pagination: { current: 1, size: 20, total: 0 },
      overview: {}, assessmentList: [],
      dialogVisible: false, dialogTitle: '新建评估', isEdit: false,
      formData: { assessmentName: '', assessmentType: 'COMPREHENSIVE', assessmentMethod: 'MIXED', overallRiskLevel: 'MEDIUM', overallRiskScore: 50, assessmentDate: '', assessor: '', assessmentConclusion: '' },
      detailDialogVisible: false,
      detailData: null
    }
  },
  watch: { enterpriseId: { handler(val) { if (val) this.loadData() }, immediate: true } },
  methods: {
    async loadData() {
      if (!this.enterpriseId) return
      this.loading = true
      try {
        const res = await getRiskAssessmentList({ enterpriseId: this.enterpriseId, pageNumber: this.pagination.current, pageSize: this.pagination.size, ...this.queryForm })
        const data = extractData(res)
        this.assessmentList = (data && (data.tlist || data.records)) || []
        this.pagination.total = (data && (data.totalRecord || data.total)) || 0
        this.loadOverview()
      } catch (e) { console.error(e) } finally { this.loading = false }
    },
    async loadOverview() {
      try { const res = await analyzeRiskAssessment({ enterpriseId: this.enterpriseId }); this.overview = extractData(res) || {} } catch (e) {}
    },
    refreshData() { this.loadData(); this.$message.success('刷新成功') },
    queryData() { this.pagination.current = 1; this.loadData() },
    resetQuery() { this.queryForm = { assessmentType: '', overallRiskLevel: '', assessmentStatus: '' }; this.queryData() },
    handleSizeChange(val) { this.pagination.size = val; this.loadData() },
    handleCurrentChange(val) { this.pagination.current = val; this.loadData() },
    showAddDialog() { this.isEdit = false; this.dialogTitle = '新建评估'; this.formData = { assessmentName: '', assessmentType: 'COMPREHENSIVE', assessmentMethod: 'MIXED', overallRiskLevel: 'MEDIUM', overallRiskScore: 50, assessmentDate: '', assessor: '', assessmentConclusion: '' }; this.dialogVisible = true },
    editAssessment(row) { this.isEdit = true; this.dialogTitle = '编辑评估'; this.formData = { ...row }; this.dialogVisible = true },
    async submitForm() {
      try {
        const data = { ...this.formData, enterpriseId: this.enterpriseId, enterpriseName: this.enterpriseName, assessmentStatus: this.isEdit ? this.formData.assessmentStatus : 'DRAFT' }
        if (this.isEdit) await updateRiskAssessment(data); else await addRiskAssessment(data)
        this.$message.success(this.isEdit ? '更新成功' : '新增成功'); this.dialogVisible = false; this.loadData(); this.$emit('refresh')
      } catch (e) { this.$message.error('操作失败') }
    },
    async submitAssessment(row) { try { await submitRiskAssessment({ riskAssessmentId: row.riskAssessmentId }); this.$message.success('提交成功'); this.loadData() } catch (e) { this.$message.error('提交失败') } },
    viewDetail(row) {
      this.detailData = { ...row, relatedRisks: [] }
      this.detailDialogVisible = true
      // 加载关联的风险识别记录
      getRiskIdentificationList({ enterpriseId: this.enterpriseId, pageNumber: 1, pageSize: 5 }).then(res => {
        const data = extractData(res)
        if (data && (data.tlist || data.records)) {
          this.detailData.relatedRisks = (data.tlist || data.records).slice(0, 5)
        }
      }).catch(() => {})
    },
    deleteAssessment(row) { this.$confirm('确认删除？', '提示', { type: 'warning' }).then(async () => { await deleteRiskAssessment(row.riskAssessmentId); this.$message.success('删除成功'); this.loadData(); this.$emit('refresh') }) },
    exportData() {
      if (!this.enterpriseId) { this.$message.warning('请先选择企业'); return }
      this.loading = true
      getRiskAssessmentList({ enterpriseId: this.enterpriseId, pageNumber: 1, pageSize: 1000, ...this.queryForm }).then(res => {
        const data = extractData(res)
        const list = (data && (data.tlist || data.records)) || []
        if (list.length === 0) { this.$message.warning('暂无数据可导出'); return }
        // 生成CSV
        const headers = ['评估名称', '评估类型', '风险等级', '风险评分', '评估人', '评估日期', '状态']
        const typeMap = { COMPREHENSIVE: '综合评估', FINANCIAL: '财务风险', OPERATIONAL: '经营风险', COMPLIANCE: '合规风险', GOVERNANCE: '治理风险' }
        const levelMap = { HIGH: '高风险', VERY_HIGH: '极高风险', MEDIUM: '中风险', LOW: '低风险', VERY_LOW: '极低风险' }
        const statusMap = { DRAFT: '草稿', IN_PROGRESS: '评估中', COMPLETED: '已完成', PENDING: '待审核' }
        const rows = list.map(item => [
          item.assessmentName || '', typeMap[item.assessmentType] || item.assessmentType || '',
          levelMap[item.overallRiskLevel] || item.overallRiskLevel || '', item.overallRiskScore || '',
          item.assessor || '', item.assessmentDate || '', statusMap[item.assessmentStatus] || item.assessmentStatus || ''
        ])
        const csvContent = '\uFEFF' + [headers.join(','), ...rows.map(r => r.join(','))].join('\n')
        const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = '风险评估数据_' + new Date().toISOString().slice(0, 10) + '.csv'
        link.click()
        URL.revokeObjectURL(link.href)
        this.$message.success('导出成功')
      }).catch(e => { this.$message.error('导出失败：' + e.message) }).finally(() => { this.loading = false })
    },
    getTypeTag(t) { return { COMPREHENSIVE: 'danger', FINANCIAL: 'primary', OPERATIONAL: 'success', COMPLIANCE: 'warning', GOVERNANCE: 'info' }[t] || 'info' },
    getTypeText(t) { return { COMPREHENSIVE: '综合评估', FINANCIAL: '财务风险', OPERATIONAL: '经营风险', COMPLIANCE: '合规风险', GOVERNANCE: '治理风险' }[t] || t },
    getLevelType(l) { var v = (l || '').toUpperCase(); return { HIGH: 'danger', VERY_HIGH: 'danger', MEDIUM: 'warning', LOW: 'success', VERY_LOW: 'success' }[v] || 'info' },
    getLevelText(l) { var v = (l || '').toUpperCase(); return { HIGH: '高风险', VERY_HIGH: '极高风险', MEDIUM: '中风险', LOW: '低风险', VERY_LOW: '极低风险' }[v] || l || '-' },
    getStatusType(s) { return { DRAFT: 'info', IN_PROGRESS: 'primary', COMPLETED: 'success', PENDING: 'warning' }[s] || 'info' },
    getStatusText(s) { return { DRAFT: '草稿', IN_PROGRESS: '评估中', COMPLETED: '已完成', PENDING: '待审核' }[s] || s },
    getScoreClass(s) { if (s >= 80) return 'score-high'; if (s >= 60) return 'score-medium'; return 'score-low' }
  }
}
</script>
<style scoped>
.risk-assessment-tab { padding: 20px; }
.overview-card { margin-bottom: 20px; }
.table-card { margin-bottom: 20px; }
.query-form { margin-bottom: 20px; }
.score-high { color: #F56C6C; font-weight: bold; }
.score-medium { color: #E6A23C; font-weight: bold; }
.score-low { color: #67C23A; }
.stat-item { display: flex; align-items: center; padding: 10px 0; }
.stat-icon { font-size: 28px; margin-right: 12px; }
.stat-info { flex: 1; }
.stat-value { font-size: 22px; font-weight: bold; color: #303133; line-height: 1.2; }
.stat-title { font-size: 13px; color: #909399; margin-top: 4px; }
</style>
