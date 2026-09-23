<template>
  <div class="environment-analysis-management" :style="themeVars">
    <el-row :gutter="16" class="stats-row">
      <el-col :span="6"><div class="mini-stat-card"><div class="stat-value">{{ statistics.total || 0 }}</div><div class="stat-label">总分析数</div></div></el-col>
      <el-col :span="6"><div class="mini-stat-card"><div class="stat-value" :style="{ color: ipBright }">{{ statistics.inProgressCount || 0 }}</div><div class="stat-label">进行中</div></div></el-col>
      <el-col :span="6"><div class="mini-stat-card"><div class="stat-value" style="color:#67C23A">{{ statistics.completedCount || 0 }}</div><div class="stat-label">已完成</div></div></el-col>
      <el-col :span="6"><div class="mini-stat-card"><div class="stat-value" style="color:#E6A23C">{{ statistics.needUpdateCount || 0 }}</div><div class="stat-label">需更新</div></div></el-col>
    </el-row>
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="分析编号" prop="analysisNo"><el-input v-model="queryForm.analysisNo" placeholder="请输入" clearable /></el-form-item>
        <el-form-item label="分析主题" prop="analysisTheme"><el-input v-model="queryForm.analysisTheme" placeholder="请输入" clearable /></el-form-item>
        <el-form-item label="分析类型" prop="analysisType">
          <el-select v-model="queryForm.analysisType" placeholder="请选择" clearable>
            <el-option label="PEST分析" value="PEST分析" /><el-option label="SWOT分析" value="SWOT分析" /><el-option label="五力模型" value="五力模型" /><el-option label="价值链分析" value="价值链分析" />
          </el-select>
        </el-form-item>
        <el-form-item label="分析状态" prop="status">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable>
            <el-option label="待开始" value="待开始" /><el-option label="进行中" value="进行中" /><el-option label="已完成" value="已完成" /><el-option label="需更新" value="需更新" />
          </el-select>
        </el-form-item>
        <el-form-item label="分析师" prop="analyst"><el-input v-model="queryForm.analyst" placeholder="请输入" clearable /></el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" class="action-card">
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增分析</el-button>
      <el-button type="danger" icon="el-icon-delete" :disabled="multipleSelection.length===0" @click="handleBatchDelete">批量删除</el-button>
    </el-card>
    <el-card shadow="never" class="table-card">
      <el-table :data="tableData" v-loading="loading" stripe border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" />
        <el-table-column prop="analysisNo" label="分析编号" width="130" />
        <el-table-column prop="analysisTheme" label="分析主题" width="180" show-overflow-tooltip />
        <el-table-column prop="analysisType" label="分析类型" width="110"><template slot-scope="s"><el-tag :type="getTypeColor(s.row.analysisType)" size="small">{{ s.row.analysisType }}</el-tag></template></el-table-column>
        <el-table-column prop="analyst" label="分析师" width="90" />
        <el-table-column prop="department" label="部门" width="110" />
        <el-table-column prop="analysisDate" label="分析日期" width="110" />
        <el-table-column prop="politicalFactor" label="政治" width="60"><template slot-scope="s"><el-rate :value="s.row.politicalFactor" disabled /></template></el-table-column>
        <el-table-column prop="economicFactor" label="经济" width="60"><template slot-scope="s"><el-rate :value="s.row.economicFactor" disabled /></template></el-table-column>
        <el-table-column prop="socialFactor" label="社会" width="60"><template slot-scope="s"><el-rate :value="s.row.socialFactor" disabled /></template></el-table-column>
        <el-table-column prop="technicalFactor" label="技术" width="60"><template slot-scope="s"><el-rate :value="s.row.technicalFactor" disabled /></template></el-table-column>
        <el-table-column prop="overallRisk" label="整体风险" width="90"><template slot-scope="s"><el-tag :type="getRiskType(s.row.overallRisk)" size="mini">{{ s.row.overallRisk }}</el-tag></template></el-table-column>
        <el-table-column prop="opportunityIndex" label="机会指数" width="90"><template slot-scope="s"><span class="opportunity-index">{{ s.row.opportunityIndex }}</span></template></el-table-column>
        <el-table-column prop="threatIndex" label="威胁指数" width="90"><template slot-scope="s"><span class="threat-index">{{ s.row.threatIndex }}</span></template></el-table-column>
        <el-table-column prop="status" label="状态" width="90"><template slot-scope="s"><el-tag :type="getStatusType(s.row.status)" size="small">{{ s.row.status }}</el-tag></template></el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template slot-scope="s">
            <el-button size="mini" type="text" @click="handleView(s.row)">查看</el-button>
            <el-button size="mini" type="text" @click="handleEdit(s.row)">编辑</el-button>
            <el-button size="mini" type="text" style="color:#F56C6C" @click="handleDelete(s.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-container">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pagination.currentPage" :page-sizes="[10,20,50,100]" :page-size="pagination.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" />
      </div>
    </el-card>
    <el-dialog :title="dialogTitle" :visible.sync="editDialogVisible" width="700px" :close-on-click-modal="false">
      <el-form :model="editForm" :rules="editRules" ref="editFormRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="分析主题" prop="analysisTheme"><el-input v-model="editForm.analysisTheme" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="分析类型"><el-select v-model="editForm.analysisType" style="width:100%"><el-option label="PEST分析" value="PEST分析" /><el-option label="SWOT分析" value="SWOT分析" /><el-option label="五力模型" value="五力模型" /><el-option label="价值链分析" value="价值链分析" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="分析师"><el-input v-model="editForm.analyst" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="部门"><el-input v-model="editForm.department" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="分析日期"><el-date-picker v-model="editForm.analysisDate" type="date" value-format="yyyy-MM-dd" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="整体风险"><el-select v-model="editForm.overallRisk" style="width:100%"><el-option label="高" value="高" /><el-option label="中" value="中" /><el-option label="低" value="低" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="政治因素"><el-rate v-model="editForm.politicalFactor" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="经济因素"><el-rate v-model="editForm.economicFactor" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="社会因素"><el-rate v-model="editForm.socialFactor" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="技术因素"><el-rate v-model="editForm.technicalFactor" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="机会指数"><el-input-number v-model="editForm.opportunityIndex" :min="0" :max="100" :precision="2" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="威胁指数"><el-input-number v-model="editForm.threatIndex" :min="0" :max="100" :precision="2" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="有效期(月)"><el-input-number v-model="editForm.validityPeriod" :min="1" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="状态"><el-select v-model="editForm.status" style="width:100%"><el-option label="待开始" value="待开始" /><el-option label="进行中" value="进行中" /><el-option label="已完成" value="已完成" /><el-option label="需更新" value="需更新" /></el-select></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="战略建议"><el-input v-model="editForm.strategicRecommendation" type="textarea" :rows="2" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <div slot="footer"><el-button @click="editDialogVisible=false">取消</el-button><el-button type="primary" :loading="submitLoading" @click="submitForm">确定</el-button></div>
    </el-dialog>
    <el-dialog title="环境分析详情" :visible.sync="detailDialogVisible" width="700px">
      <el-descriptions :column="2" border v-if="selectedItem">
        <el-descriptions-item label="分析编号">{{ selectedItem.analysisNo }}</el-descriptions-item>
        <el-descriptions-item label="分析主题">{{ selectedItem.analysisTheme }}</el-descriptions-item>
        <el-descriptions-item label="分析类型">{{ selectedItem.analysisType }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ selectedItem.status }}</el-descriptions-item>
        <el-descriptions-item label="分析师">{{ selectedItem.analyst }}</el-descriptions-item>
        <el-descriptions-item label="部门">{{ selectedItem.department }}</el-descriptions-item>
        <el-descriptions-item label="整体风险">{{ selectedItem.overallRisk }}</el-descriptions-item>
        <el-descriptions-item label="机会指数">{{ selectedItem.opportunityIndex }}</el-descriptions-item>
        <el-descriptions-item label="威胁指数">{{ selectedItem.threatIndex }}</el-descriptions-item>
        <el-descriptions-item label="有效期">{{ selectedItem.validityPeriod }}个月</el-descriptions-item>
        <el-descriptions-item label="战略建议" :span="2">{{ selectedItem.strategicRecommendation || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { getEnvironmentList, addEnvironment, updateEnvironment, deleteEnvironment, getEnvironmentStatistics } from '@/api/enterprise/strategy'
import { investThemeMixin } from '@/views/stateAssets/themeMixin'
export default {
  mixins: [investThemeMixin],
  name: 'EnvironmentAnalysisManagement',
  data() {
    return {
      loading: false, submitLoading: false,
      statistics: { total: 0, inProgressCount: 0, completedCount: 0, needUpdateCount: 0 },
      queryForm: { analysisNo: '', analysisTheme: '', analysisType: '', status: '', analyst: '' },
      tableData: [], multipleSelection: [],
      pagination: { currentPage: 1, pageSize: 20, total: 0 },
      editDialogVisible: false, detailDialogVisible: false, dialogTitle: '新增分析',
      editForm: { id: null, analysisTheme: '', analysisType: '', analyst: '', department: '', analysisDate: '', politicalFactor: 3, economicFactor: 3, socialFactor: 3, technicalFactor: 3, overallRisk: '中', opportunityIndex: 50, threatIndex: 50, strategicRecommendation: '', validityPeriod: 6, status: '待开始' },
      editRules: { analysisTheme: [{ required: true, message: '请输入', trigger: 'blur' }] },
      selectedItem: null
    }
  },
  mounted() { this.loadList(); this.loadStats() },
  methods: {
    async loadList() {
      this.loading = true
      try {
        const res = await getEnvironmentList({ ...this.queryForm, pageNumber: this.pagination.currentPage, pageSize: this.pagination.pageSize })
        if (res && res.result === 200 && res.data) { this.tableData = res.data.tlist || []; this.pagination.total = res.data.totalRecord || 0 }
      } catch (e) { console.error(e); this.tableData = []; this.pagination.total = 0 } finally { this.loading = false }
    },
    async loadStats() { try { const res = await getEnvironmentStatistics(); if (res && res.result === 200 && res.data) this.statistics = res.data } catch (e) { console.error(e) } },
    handleSearch() { this.pagination.currentPage = 1; this.loadList() },
    handleReset() { this.$refs.queryForm.resetFields(); this.handleSearch() },
    handleSelectionChange(v) { this.multipleSelection = v },
    handleSizeChange(v) { this.pagination.pageSize = v; this.loadList() },
    handleCurrentChange(v) { this.pagination.currentPage = v; this.loadList() },
    handleAdd() { this.editForm = { id: null, analysisTheme: '', analysisType: '', analyst: '', department: '', analysisDate: '', politicalFactor: 3, economicFactor: 3, socialFactor: 3, technicalFactor: 3, overallRisk: '中', opportunityIndex: 50, threatIndex: 50, strategicRecommendation: '', validityPeriod: 6, status: '待开始' }; this.dialogTitle = '新增分析'; this.editDialogVisible = true },
    handleEdit(row) { this.editForm = { ...row }; this.dialogTitle = '编辑分析'; this.editDialogVisible = true },
    handleView(row) { this.selectedItem = row; this.detailDialogVisible = true },
    handleDelete(row) { this.$confirm('确认删除?', '提示', { type: 'warning' }).then(async () => { try { await deleteEnvironment(row.id); this.$message.success('删除成功'); this.loadList(); this.loadStats() } catch (e) { this.$message.error('删除失败') } }).catch(() => {}) },
    handleBatchDelete() { this.$confirm('确认批量删除?', '提示', { type: 'warning' }).then(async () => { try { for (const item of this.multipleSelection) { await deleteEnvironment(item.id) } this.$message.success('删除成功'); this.loadList(); this.loadStats() } catch (e) { this.$message.error('删除失败') } }).catch(() => {}) },
    submitForm() { this.$refs.editFormRef.validate(async (valid) => { if (!valid) return; this.submitLoading = true; try { if (this.editForm.id) { await updateEnvironment(this.editForm); this.$message.success('更新成功') } else { await addEnvironment(this.editForm); this.$message.success('新增成功') }; this.editDialogVisible = false; this.loadList(); this.loadStats() } catch (e) { this.$message.error('操作失败') } finally { this.submitLoading = false } }) },
    getTypeColor(t) { return { 'PEST分析': 'primary', 'SWOT分析': 'success', '五力模型': 'warning', '价值链分析': 'info' }[t] || 'info' },
    getRiskType(l) { return { '低': 'success', '中': 'warning', '高': 'danger' }[l] || 'info' },
    getStatusType(s) { return { '待开始': 'info', '进行中': 'primary', '已完成': 'success', '需更新': 'warning' }[s] || 'info' }
  }
}
</script>

<style lang="scss" scoped>
.environment-analysis-management {
  .stats-row { margin-bottom: 16px; }
  .mini-stat-card { background: white; border-radius: 8px; padding: 16px; box-shadow: 0 2px 8px rgba(0,0,0,0.08); text-align: center;
    .stat-value { font-size: 24px; font-weight: 600; color: #303133; }
    .stat-label { font-size: 13px; color: #909399; margin-top: 4px; }
  }
  .search-card, .action-card, .table-card { margin-bottom: 16px; }
  .opportunity-index { color: #67C23A; font-weight: 500; font-size: 16px; }
  .threat-index { color: #F56C6C; font-weight: 500; font-size: 16px; }
  .pagination-container { margin-top: 20px; text-align: right; }
}
</style>
