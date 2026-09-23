<template>
  <div class="risk-identification-tab">
    <el-card class="overview-card">
      <div slot="header">
        <span>风险识别概览</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="refreshData">刷新</el-button>
      </div>
      <el-row :gutter="20">
        <el-col :span="6"><div class="stat-item"><div class="stat-icon" style="color: #409EFF"><i class="el-icon-search"></i></div><div class="stat-info"><div class="stat-value">{{ overview.totalRisks || 0 }}</div><div class="stat-title">识别风险数</div></div></div></el-col>
        <el-col :span="6"><div class="stat-item"><div class="stat-icon" style="color: #F56C6C"><i class="el-icon-error"></i></div><div class="stat-info"><div class="stat-value">{{ overview.highRisks || 0 }}</div><div class="stat-title">高风险数</div></div></div></el-col>
        <el-col :span="6"><div class="stat-item"><div class="stat-icon" style="color: #67C23A"><i class="el-icon-plus"></i></div><div class="stat-info"><div class="stat-value">{{ overview.monthlyNew || 0 }}</div><div class="stat-title">本月新增</div></div></div></el-col>
        <el-col :span="6"><div class="stat-item"><div class="stat-icon" style="color: #E6A23C"><i class="el-icon-medal"></i></div><div class="stat-info"><div class="stat-value">{{ overview.accuracy || 0 }}%</div><div class="stat-title">识别准确率</div></div></div></el-col>
      </el-row>
    </el-card>
    <el-card class="table-card">
      <div slot="header">
        <span>风险识别列表</span>
        <div style="float: right;">
          <el-button type="primary" size="small" @click="showAddDialog">识别风险</el-button>
          <el-button type="success" size="small" @click="exportData">导出数据</el-button>
        </div>
      </div>
      <el-form :model="queryForm" :inline="true" class="query-form">
        <el-form-item label="风险类型"><el-select v-model="queryForm.riskType" placeholder="请选择风险类型" clearable><el-option label="财务风险" value="financial"></el-option><el-option label="运营风险" value="operational"></el-option><el-option label="市场风险" value="market"></el-option><el-option label="技术风险" value="technical"></el-option><el-option label="合规风险" value="compliance"></el-option></el-select></el-form-item>
        <el-form-item label="风险等级"><el-select v-model="queryForm.riskLevel" placeholder="请选择风险等级" clearable><el-option label="高风险" value="high"></el-option><el-option label="中风险" value="medium"></el-option><el-option label="低风险" value="low"></el-option></el-select></el-form-item>
        <el-form-item label="识别状态"><el-select v-model="queryForm.status" placeholder="请选择识别状态" clearable><el-option label="已识别" value="identified"></el-option><el-option label="待确认" value="pending"></el-option><el-option label="已确认" value="confirmed"></el-option><el-option label="已忽略" value="ignored"></el-option></el-select></el-form-item>
        <el-form-item><el-button type="primary" @click="queryData">查询</el-button><el-button @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
      <el-table :data="riskList" border v-loading="loading">
        <el-table-column prop="riskCode" label="风险编号" width="120"></el-table-column>
        <el-table-column prop="riskName" label="风险名称" width="200"></el-table-column>
        <el-table-column prop="riskType" label="风险类型" width="120"><template slot-scope="scope"><el-tag :type="getRiskTypeTag(scope.row.riskType)">{{ getRiskTypeText(scope.row.riskType) }}</el-tag></template></el-table-column>
        <el-table-column prop="riskLevel" label="风险等级" width="100"><template slot-scope="scope"><el-tag :type="getRiskLevelType(scope.row.riskLevel)">{{ getRiskLevelText(scope.row.riskLevel) }}</el-tag></template></el-table-column>
        <el-table-column prop="identificationMethod" label="识别方法" width="120"></el-table-column>
        <el-table-column prop="identifiedBy" label="识别人" width="100"></el-table-column>
        <el-table-column prop="identificationDate" label="识别日期" width="120"></el-table-column>
        <el-table-column prop="probability" label="发生概率" width="100" align="center"><template slot-scope="scope">{{ scope.row.probability }}%</template></el-table-column>
        <el-table-column prop="impact" label="影响程度" width="100" align="center"><template slot-scope="scope"><span :class="getImpactClass(scope.row.impact)">{{ getImpactText(scope.row.impact) }}</span></template></el-table-column>
        <el-table-column prop="status" label="状态" width="100"><template slot-scope="scope"><el-tag :type="getStatusType(scope.row.status)">{{ getStatusText(scope.row.status) }}</el-tag></template></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="viewDetail(scope.row)">详情</el-button>
            <el-button type="text" size="small" @click="confirmRisk(scope.row)" v-if="scope.row.status === 'pending'">确认</el-button>
            <el-button type="text" size="small" @click="editRisk(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="deleteRisk(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pagination.current" :page-sizes="[10, 20, 50, 100]" :page-size="pagination.size" layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" style="margin-top: 20px; text-align: right;"></el-pagination>
    </el-card>
    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px">
      <el-form :model="formData" label-width="100px">
        <el-form-item label="风险名称"><el-input v-model="formData.riskName"></el-input></el-form-item>
        <el-form-item label="风险类型"><el-select v-model="formData.riskType" style="width:100%"><el-option label="财务风险" value="financial"></el-option><el-option label="运营风险" value="operational"></el-option><el-option label="市场风险" value="market"></el-option><el-option label="技术风险" value="technical"></el-option><el-option label="合规风险" value="compliance"></el-option></el-select></el-form-item>
        <el-form-item label="风险等级"><el-select v-model="formData.riskLevel" style="width:100%"><el-option label="高风险" value="high"></el-option><el-option label="中风险" value="medium"></el-option><el-option label="低风险" value="low"></el-option></el-select></el-form-item>
        <el-form-item label="识别方法"><el-input v-model="formData.identificationMethod"></el-input></el-form-item>
        <el-form-item label="识别人"><el-input v-model="formData.identifiedBy"></el-input></el-form-item>
        <el-form-item label="发生概率(%)"><el-input-number v-model="formData.probability" :min="0" :max="100" :precision="2"></el-input-number></el-form-item>
        <el-form-item label="影响程度"><el-select v-model="formData.impact" style="width:100%"><el-option label="高" value="high"></el-option><el-option label="中" value="medium"></el-option><el-option label="低" value="low"></el-option></el-select></el-form-item>
        <el-form-item label="风险描述"><el-input type="textarea" v-model="formData.riskDesc" :rows="3"></el-input></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="submitForm">确定</el-button></div>
    </el-dialog>
    <!-- 详情弹窗 -->
    <el-dialog title="风险识别详情" :visible.sync="detailDialogVisible" width="700px">
      <el-descriptions :column="2" border v-if="detailData">
        <el-descriptions-item label="风险编号">{{ detailData.riskCode }}</el-descriptions-item>
        <el-descriptions-item label="风险名称">{{ detailData.riskName }}</el-descriptions-item>
        <el-descriptions-item label="风险类型"><el-tag :type="getRiskTypeTag(detailData.riskType)">{{ getRiskTypeText(detailData.riskType) }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="风险等级"><el-tag :type="getRiskLevelType(detailData.riskLevel)">{{ getRiskLevelText(detailData.riskLevel) }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="发生概率">{{ detailData.probability }}%</el-descriptions-item>
        <el-descriptions-item label="影响程度"><span :class="getImpactClass(detailData.impact)">{{ getImpactText(detailData.impact) }}</span></el-descriptions-item>
        <el-descriptions-item label="识别方法">{{ detailData.identificationMethod || '-' }}</el-descriptions-item>
        <el-descriptions-item label="识别人">{{ detailData.identifiedBy }}</el-descriptions-item>
        <el-descriptions-item label="识别日期">{{ detailData.identificationDate }}</el-descriptions-item>
        <el-descriptions-item label="状态"><el-tag :type="getStatusType(detailData.status)">{{ getStatusText(detailData.status) }}</el-tag></el-descriptions-item>
        <el-descriptions-item label="所属企业" :span="2">{{ detailData.enterpriseName }}</el-descriptions-item>
        <el-descriptions-item label="风险描述" :span="2">{{ detailData.riskDesc || '暂无' }}</el-descriptions-item>
      </el-descriptions>
      <!-- 关联的控制措施 -->
      <div style="margin-top: 20px;" v-if="detailData && detailData.relatedControls && detailData.relatedControls.length > 0">
        <h4 style="margin-bottom: 10px;">关联控制措施</h4>
        <el-table :data="detailData.relatedControls" border size="small">
          <el-table-column prop="measureName" label="措施名称" width="180"></el-table-column>
          <el-table-column prop="measureType" label="类型" width="100"><template slot-scope="scope">{{ { preventive: '预防性', detective: '检查性', corrective: '纠正性', compensating: '补偿性' }[scope.row.measureType] || scope.row.measureType }}</template></el-table-column>
          <el-table-column prop="implementationStatus" label="状态" width="80"><template slot-scope="scope"><el-tag size="small">{{ { planned: '计划中', implementing: '实施中', implemented: '已实施' }[scope.row.implementationStatus] || scope.row.implementationStatus }}</el-tag></template></el-table-column>
        </el-table>
      </div>
      <div slot="footer"><el-button @click="detailDialogVisible = false">关闭</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import { getRiskIdentificationList, addRiskIdentification, updateRiskIdentification, deleteRiskIdentification, analyzeRiskIdentification, extractData } from '@/api/enterprise/risk'
import { getRiskControlList } from '@/api/enterprise/risk'

export default {
  name: 'RiskIdentificationTab',
  props: { enterpriseId: String, enterpriseName: String },
  data() {
    return {
      loading: false,
      queryForm: { riskType: '', riskLevel: '', status: '' },
      pagination: { current: 1, size: 20, total: 0 },
      overview: { totalRisks: 0, highRisks: 0, monthlyNew: 0, accuracy: 0 },
      riskList: [],
      dialogVisible: false,
      dialogTitle: '新增风险识别',
      isEdit: false,
      formData: { riskName: '', riskType: '', riskLevel: '', identificationMethod: '', identifiedBy: '', probability: 50, impact: 'medium', riskDesc: '' },
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
        const params = { enterpriseId: this.enterpriseId, pageNumber: this.pagination.current, pageSize: this.pagination.size, ...this.queryForm }
        const res = await getRiskIdentificationList(params)
        const data = extractData(res)
        this.riskList = (data && (data.tlist || data.records)) || []
        this.pagination.total = (data && (data.totalRecord || data.total)) || 0
        this.loadOverview()
      } catch (e) { console.error('加载风险识别数据失败:', e) }
      finally { this.loading = false }
    },
    async loadOverview() {
      try {
        const res = await analyzeRiskIdentification({ enterpriseId: this.enterpriseId })
        this.overview = extractData(res) || {}
      } catch (e) { console.error('加载概览失败:', e) }
    },
    refreshData() { this.loadData(); this.$message.success('数据刷新成功') },
    queryData() { this.pagination.current = 1; this.loadData() },
    resetQuery() { this.queryForm = { riskType: '', riskLevel: '', status: '' }; this.queryData() },
    handleSizeChange(val) { this.pagination.size = val; this.loadData() },
    handleCurrentChange(val) { this.pagination.current = val; this.loadData() },
    showAddDialog() {
      this.isEdit = false; this.dialogTitle = '新增风险识别'
      this.formData = { riskName: '', riskType: '', riskLevel: '', identificationMethod: '', identifiedBy: '', probability: 50, impact: 'medium', riskDesc: '' }
      this.dialogVisible = true
    },
    editRisk(row) {
      this.isEdit = true; this.dialogTitle = '编辑风险识别'
      this.formData = { ...row }; this.dialogVisible = true
    },
    async submitForm() {
      try {
        const data = { ...this.formData, enterpriseId: this.enterpriseId, enterpriseName: this.enterpriseName, status: this.isEdit ? this.formData.status : 'identified', riskCode: this.isEdit ? this.formData.riskCode : 'RISK-' + Date.now() }
        if (this.isEdit) { await updateRiskIdentification(data) } else { await addRiskIdentification(data) }
        this.$message.success(this.isEdit ? '更新成功' : '新增成功')
        this.dialogVisible = false; this.loadData(); this.$emit('refresh')
      } catch (e) { this.$message.error('操作失败：' + e.message) }
    },
    confirmRisk(row) {
      this.$confirm('确认该风险？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(async () => {
        await updateRiskIdentification({ ...row, status: 'confirmed' }); this.$message.success('确认成功'); this.loadData()
      })
    },
    viewDetail(row) {
      this.detailData = { ...row, relatedControls: [] }
      this.detailDialogVisible = true
      // 加载关联的控制措施
      getRiskControlList({ enterpriseId: this.enterpriseId, targetRiskType: row.riskType, pageNumber: 1, pageSize: 5 }).then(res => {
        const data = extractData(res)
        if (data && (data.tlist || data.records)) {
          this.detailData.relatedControls = (data.tlist || data.records).slice(0, 5)
        }
      }).catch(() => {})
    },
    deleteRisk(row) {
      this.$confirm('确认删除该风险？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(async () => {
        await deleteRiskIdentification(row.riskIdentificationId); this.$message.success('删除成功'); this.loadData(); this.$emit('refresh')
      })
    },
    exportData() {
      if (!this.enterpriseId) { this.$message.warning('请先选择企业'); return }
      this.loading = true
      getRiskIdentificationList({ enterpriseId: this.enterpriseId, pageNumber: 1, pageSize: 1000, ...this.queryForm }).then(res => {
        const data = extractData(res)
        const list = (data && (data.tlist || data.records)) || []
        if (list.length === 0) { this.$message.warning('暂无数据可导出'); return }
        const headers = ['风险编号', '风险名称', '风险类型', '风险等级', '发生概率(%)', '影响程度', '识别人', '识别日期', '状态']
        const typeMap = { financial: '财务风险', operational: '运营风险', market: '市场风险', technical: '技术风险', compliance: '合规风险' }
        const levelMap = { high: '高风险', medium: '中风险', low: '低风险' }
        const impactMap = { high: '高', medium: '中', low: '低' }
        const statusMap = { identified: '已识别', pending: '待确认', confirmed: '已确认', ignored: '已忽略' }
        const rows = list.map(item => [
          item.riskCode || '', item.riskName || '', typeMap[item.riskType] || item.riskType || '',
          levelMap[item.riskLevel] || item.riskLevel || '', item.probability || '',
          impactMap[item.impact] || item.impact || '', item.identifiedBy || '',
          item.identificationDate || '', statusMap[item.status] || item.status || ''
        ])
        const csvContent = '\uFEFF' + [headers.join(','), ...rows.map(r => r.join(','))].join('\n')
        const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = '风险识别数据_' + new Date().toISOString().slice(0, 10) + '.csv'
        link.click()
        URL.revokeObjectURL(link.href)
        this.$message.success('导出成功')
      }).catch(e => { this.$message.error('导出失败：' + e.message) }).finally(() => { this.loading = false })
    },
    getRiskTypeTag(type) { return { financial: 'primary', operational: 'success', market: 'warning', technical: 'info', compliance: 'danger' }[type] || 'info' },
    getRiskTypeText(type) { return { financial: '财务风险', operational: '运营风险', market: '市场风险', technical: '技术风险', compliance: '合规风险' }[type] || type },
    getRiskLevelType(level) { return { high: 'danger', medium: 'warning', low: 'success' }[level] || 'info' },
    getRiskLevelText(level) { return { high: '高风险', medium: '中风险', low: '低风险' }[level] || level },
    getImpactClass(impact) { return { high: 'impact-high', medium: 'impact-medium', low: 'impact-low' }[impact] || '' },
    getImpactText(impact) { return { high: '高', medium: '中', low: '低' }[impact] || impact },
    getStatusType(status) { var s = (status || '').toLowerCase(); return { identified: 'info', pending: 'warning', confirmed: 'success', ignored: 'danger' }[s] || 'info' },
    getStatusText(status) { var s = (status || '').toLowerCase(); return { identified: '已识别', pending: '待确认', confirmed: '已确认', ignored: '已忽略' }[s] || status || '-' }
  }
}
</script>
<style scoped>
.risk-identification-tab { padding: 20px; }
.overview-card { margin-bottom: 20px; }
.table-card { margin-bottom: 20px; }
.query-form { margin-bottom: 20px; }
.impact-high { color: #F56C6C; font-weight: bold; }
.impact-medium { color: #E6A23C; font-weight: bold; }
.impact-low { color: #67C23A; }
.stat-item { display: flex; align-items: center; padding: 10px 0; }
.stat-icon { font-size: 28px; margin-right: 12px; }
.stat-info { flex: 1; }
.stat-value { font-size: 22px; font-weight: bold; color: #303133; line-height: 1.2; }
.stat-title { font-size: 13px; color: #909399; margin-top: 4px; }
</style>
