<template>
  <div class="competition-analysis-management" :style="themeVars">
    <el-row :gutter="16" class="stats-row">
      <el-col :span="6"><div class="mini-stat-card"><div class="stat-value">{{ statistics.total || 0 }}</div><div class="stat-label">总分析数</div></div></el-col>
      <el-col :span="6"><div class="mini-stat-card"><div class="stat-value" :style="{ color: ipBright }">{{ statistics.inProgressCount || 0 }}</div><div class="stat-label">分析中</div></div></el-col>
      <el-col :span="6"><div class="mini-stat-card"><div class="stat-value" style="color:#67C23A">{{ statistics.completedCount || 0 }}</div><div class="stat-label">已完成</div></div></el-col>
      <el-col :span="6"><div class="mini-stat-card"><div class="stat-value" style="color:#E6A23C">{{ statistics.avgMarketShare || 0 }}%</div><div class="stat-label">平均市场份额</div></div></el-col>
    </el-row>
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="分析编号" prop="analysisNo"><el-input v-model="queryForm.analysisNo" placeholder="请输入" clearable /></el-form-item>
        <el-form-item label="竞争对手" prop="competitorName"><el-input v-model="queryForm.competitorName" placeholder="请输入" clearable /></el-form-item>
        <el-form-item label="分析维度" prop="analysisDimension">
          <el-select v-model="queryForm.analysisDimension" placeholder="请选择" clearable>
            <el-option label="产品竞争力" value="产品竞争力" /><el-option label="市场份额" value="市场份额" /><el-option label="技术实力" value="技术实力" /><el-option label="品牌影响力" value="品牌影响力" />
          </el-select>
        </el-form-item>
        <el-form-item label="分析状态" prop="status">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable>
            <el-option label="待分析" value="待分析" /><el-option label="分析中" value="分析中" /><el-option label="已完成" value="已完成" /><el-option label="需更新" value="需更新" />
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
        <el-table-column prop="competitorName" label="竞争对手" width="150" show-overflow-tooltip />
        <el-table-column prop="analysisDimension" label="分析维度" width="110"><template slot-scope="s"><el-tag :type="getDimensionColor(s.row.analysisDimension)" size="small">{{ s.row.analysisDimension }}</el-tag></template></el-table-column>
        <el-table-column prop="analyst" label="分析师" width="90" />
        <el-table-column prop="analysisDate" label="分析日期" width="110" />
        <el-table-column prop="marketShare" label="市场份额" width="90"><template slot-scope="s"><span class="market-share">{{ s.row.marketShare }}%</span></template></el-table-column>
        <el-table-column prop="productStrength" label="产品实力" width="80"><template slot-scope="s"><el-rate :value="s.row.productStrength" disabled /></template></el-table-column>
        <el-table-column prop="technicalCapability" label="技术能力" width="80"><template slot-scope="s"><el-rate :value="s.row.technicalCapability" disabled /></template></el-table-column>
        <el-table-column prop="brandInfluence" label="品牌影响" width="110"><template slot-scope="s"><el-progress :percentage="s.row.brandInfluence||0" :color="ipBright" /></template></el-table-column>
        <el-table-column prop="threatLevel" label="威胁等级" width="90"><template slot-scope="s"><el-tag :type="getThreatType(s.row.threatLevel)" size="mini">{{ s.row.threatLevel }}</el-tag></template></el-table-column>
        <el-table-column prop="overallScore" label="综合评分" width="90"><template slot-scope="s"><span :class="getScoreClass(s.row.overallScore)">{{ s.row.overallScore }}</span></template></el-table-column>
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
          <el-col :span="12"><el-form-item label="竞争对手" prop="competitorName"><el-input v-model="editForm.competitorName" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="分析维度"><el-select v-model="editForm.analysisDimension" style="width:100%"><el-option label="产品竞争力" value="产品竞争力" /><el-option label="市场份额" value="市场份额" /><el-option label="技术实力" value="技术实力" /><el-option label="品牌影响力" value="品牌影响力" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="分析师"><el-input v-model="editForm.analyst" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="分析日期"><el-date-picker v-model="editForm.analysisDate" type="date" value-format="yyyy-MM-dd" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="市场份额%"><el-input-number v-model="editForm.marketShare" :min="0" :max="100" :precision="2" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="威胁等级"><el-select v-model="editForm.threatLevel" style="width:100%"><el-option label="高" value="高" /><el-option label="中" value="中" /><el-option label="低" value="低" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="产品实力"><el-rate v-model="editForm.productStrength" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="技术能力"><el-rate v-model="editForm.technicalCapability" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="品牌影响"><el-slider v-model="editForm.brandInfluence" :max="100" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="财务实力"><el-rate v-model="editForm.financialStrength" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="综合评分"><el-input-number v-model="editForm.overallScore" :min="0" :max="100" :precision="2" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="状态"><el-select v-model="editForm.status" style="width:100%"><el-option label="待分析" value="待分析" /><el-option label="分析中" value="分析中" /><el-option label="已完成" value="已完成" /><el-option label="需更新" value="需更新" /></el-select></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="竞争优势"><el-input v-model="editForm.competitiveAdvantage" type="textarea" :rows="2" /></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="竞争劣势"><el-input v-model="editForm.competitiveWeakness" type="textarea" :rows="2" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <div slot="footer"><el-button @click="editDialogVisible=false">取消</el-button><el-button type="primary" :loading="submitLoading" @click="submitForm">确定</el-button></div>
    </el-dialog>
    <el-dialog title="竞争分析详情" :visible.sync="detailDialogVisible" width="700px">
      <el-descriptions :column="2" border v-if="selectedItem">
        <el-descriptions-item label="分析编号">{{ selectedItem.analysisNo }}</el-descriptions-item>
        <el-descriptions-item label="竞争对手">{{ selectedItem.competitorName }}</el-descriptions-item>
        <el-descriptions-item label="分析维度">{{ selectedItem.analysisDimension }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ selectedItem.status }}</el-descriptions-item>
        <el-descriptions-item label="市场份额">{{ selectedItem.marketShare }}%</el-descriptions-item>
        <el-descriptions-item label="威胁等级">{{ selectedItem.threatLevel }}</el-descriptions-item>
        <el-descriptions-item label="综合评分">{{ selectedItem.overallScore }}</el-descriptions-item>
        <el-descriptions-item label="分析师">{{ selectedItem.analyst }}</el-descriptions-item>
        <el-descriptions-item label="竞争优势" :span="2">{{ selectedItem.competitiveAdvantage || '-' }}</el-descriptions-item>
        <el-descriptions-item label="竞争劣势" :span="2">{{ selectedItem.competitiveWeakness || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { getCompetitionList, addCompetition, updateCompetition, deleteCompetition, getCompetitionStatistics } from '@/api/enterprise/strategy'
import { investThemeMixin } from '@/views/stateAssets/themeMixin'
export default {
  mixins: [investThemeMixin],
  name: 'CompetitionAnalysisManagement',
  data() {
    return {
      loading: false, submitLoading: false,
      statistics: { total: 0, inProgressCount: 0, completedCount: 0, avgMarketShare: 0 },
      queryForm: { analysisNo: '', competitorName: '', analysisDimension: '', status: '', analyst: '' },
      tableData: [], multipleSelection: [],
      pagination: { currentPage: 1, pageSize: 20, total: 0 },
      editDialogVisible: false, detailDialogVisible: false, dialogTitle: '新增分析',
      editForm: { id: null, competitorName: '', analysisDimension: '', analyst: '', analysisDate: '', marketShare: 0, productStrength: 3, technicalCapability: 3, brandInfluence: 50, financialStrength: 3, competitiveAdvantage: '', competitiveWeakness: '', threatLevel: '中', competitiveStrategy: '', overallScore: 0, status: '待分析' },
      editRules: { competitorName: [{ required: true, message: '请输入', trigger: 'blur' }] },
      selectedItem: null
    }
  },
  mounted() { this.loadList(); this.loadStats() },
  methods: {
    async loadList() {
      this.loading = true
      try {
        const res = await getCompetitionList({ ...this.queryForm, pageNumber: this.pagination.currentPage, pageSize: this.pagination.pageSize })
        if (res && res.result === 200 && res.data) { this.tableData = res.data.tlist || []; this.pagination.total = res.data.totalRecord || 0 }
      } catch (e) { console.error(e); this.tableData = []; this.pagination.total = 0 } finally { this.loading = false }
    },
    async loadStats() { try { const res = await getCompetitionStatistics(); if (res && res.result === 200 && res.data) this.statistics = res.data } catch (e) { console.error(e) } },
    handleSearch() { this.pagination.currentPage = 1; this.loadList() },
    handleReset() { this.$refs.queryForm.resetFields(); this.handleSearch() },
    handleSelectionChange(v) { this.multipleSelection = v },
    handleSizeChange(v) { this.pagination.pageSize = v; this.loadList() },
    handleCurrentChange(v) { this.pagination.currentPage = v; this.loadList() },
    handleAdd() { this.editForm = { id: null, competitorName: '', analysisDimension: '', analyst: '', analysisDate: '', marketShare: 0, productStrength: 3, technicalCapability: 3, brandInfluence: 50, financialStrength: 3, competitiveAdvantage: '', competitiveWeakness: '', threatLevel: '中', competitiveStrategy: '', overallScore: 0, status: '待分析' }; this.dialogTitle = '新增分析'; this.editDialogVisible = true },
    handleEdit(row) { this.editForm = { ...row }; this.dialogTitle = '编辑分析'; this.editDialogVisible = true },
    handleView(row) { this.selectedItem = row; this.detailDialogVisible = true },
    handleDelete(row) { this.$confirm('确认删除?', '提示', { type: 'warning' }).then(async () => { try { await deleteCompetition(row.id); this.$message.success('删除成功'); this.loadList(); this.loadStats() } catch (e) { this.$message.error('删除失败') } }).catch(() => {}) },
    handleBatchDelete() { this.$confirm('确认批量删除?', '提示', { type: 'warning' }).then(async () => { try { for (const item of this.multipleSelection) { await deleteCompetition(item.id) } this.$message.success('删除成功'); this.loadList(); this.loadStats() } catch (e) { this.$message.error('删除失败') } }).catch(() => {}) },
    submitForm() { this.$refs.editFormRef.validate(async (valid) => { if (!valid) return; this.submitLoading = true; try { if (this.editForm.id) { await updateCompetition(this.editForm); this.$message.success('更新成功') } else { await addCompetition(this.editForm); this.$message.success('新增成功') }; this.editDialogVisible = false; this.loadList(); this.loadStats() } catch (e) { this.$message.error('操作失败') } finally { this.submitLoading = false } }) },
    getDimensionColor(d) { return { '产品竞争力': 'primary', '市场份额': 'success', '技术实力': 'warning', '品牌影响力': 'info' }[d] || 'info' },
    getThreatType(l) { return { '低': 'success', '中': 'warning', '高': 'danger' }[l] || 'info' },
    getScoreClass(s) { return s >= 90 ? 'score-excellent' : s >= 80 ? 'score-good' : s >= 70 ? 'score-normal' : 'score-poor' },
    getStatusType(s) { return { '待分析': 'info', '分析中': 'primary', '已完成': 'success', '需更新': 'warning' }[s] || 'info' }
  }
}
</script>

<style lang="scss" scoped>
.competition-analysis-management {
  .stats-row { margin-bottom: 16px; }
  .mini-stat-card { background: white; border-radius: 8px; padding: 16px; box-shadow: 0 2px 8px rgba(0,0,0,0.08); text-align: center;
    .stat-value { font-size: 24px; font-weight: 600; color: #303133; }
    .stat-label { font-size: 13px; color: #909399; margin-top: 4px; }
  }
  .search-card, .action-card, .table-card { margin-bottom: 16px; }
  .market-share { color: var(--ip-bright); font-weight: 500; font-size: 16px; }
  .score-excellent { color: #67C23A; font-weight: bold; font-size: 16px; }
  .score-good { color: var(--ip-bright); font-weight: 500; }
  .score-normal { color: #E6A23C; }
  .score-poor { color: #F56C6C; }
  .pagination-container { margin-top: 20px; text-align: right; }
}
</style>
