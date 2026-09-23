<template>
  <div class="tech-evaluation-management">
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="评估编号" prop="evaluationNo"><el-input v-model="queryForm.evaluationNo" placeholder="请输入" clearable /></el-form-item>
        <el-form-item label="技术名称" prop="techName"><el-input v-model="queryForm.techName" placeholder="请输入" clearable /></el-form-item>
        <el-form-item label="评估类型" prop="evaluationType">
          <el-select v-model="queryForm.evaluationType" placeholder="请选择" clearable>
            <el-option label="技术可行性" value="技术可行性" /><el-option label="市场前景" value="市场前景" /><el-option label="商业价值" value="商业价值" /><el-option label="风险评估" value="风险评估" />
          </el-select>
        </el-form-item>
        <el-form-item label="评估状态" prop="status">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable>
            <el-option label="待评估" value="待评估" /><el-option label="评估中" value="评估中" /><el-option label="已完成" value="已完成" /><el-option label="需复评" value="需复评" />
          </el-select>
        </el-form-item>
        <el-form-item label="评估专家" prop="evaluator"><el-input v-model="queryForm.evaluator" placeholder="请输入" clearable /></el-form-item>
        <el-form-item><el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button><el-button icon="el-icon-refresh" @click="handleReset">重置</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" class="action-card">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增评估</el-button>
          <el-button type="danger" icon="el-icon-delete" @click="handleBatchDelete" :disabled="multipleSelection.length === 0">批量删除</el-button>
          <el-button type="warning" icon="el-icon-download" @click="handleExport">导出数据</el-button>
        </el-col>
      </el-row>
    </el-card>
    <el-card shadow="never" class="table-card">
      <el-table :data="evaluationList" v-loading="loading" @selection-change="handleSelectionChange" stripe border>
        <el-table-column type="selection" width="55" />
        <el-table-column prop="evaluationNo" label="评估编号" width="120" />
        <el-table-column prop="techName" label="技术名称" width="180" show-overflow-tooltip />
        <el-table-column prop="evaluationType" label="评估类型" width="100"><template slot-scope="scope"><el-tag :type="getTypeColor(scope.row.evaluationType)">{{ scope.row.evaluationType }}</el-tag></template></el-table-column>
        <el-table-column prop="applicant" label="申请人" width="80" />
        <el-table-column prop="evaluator" label="评估专家" width="80" />
        <el-table-column prop="evaluationDate" label="评估日期" width="110" />
        <el-table-column prop="techMaturity" label="技术成熟度" width="110"><template slot-scope="scope"><el-progress :percentage="scope.row.techMaturity || 0" :color="getMaturityColor(scope.row.techMaturity || 0)" /></template></el-table-column>
        <el-table-column prop="marketPotential" label="市场潜力" width="100"><template slot-scope="scope"><el-rate :value="scope.row.marketPotential || 0" disabled show-score /></template></el-table-column>
        <el-table-column prop="commercialValue" label="商业价值(万)" width="110"><template slot-scope="scope"><span class="commercial-value">{{ scope.row.commercialValue || 0 }}</span></template></el-table-column>
        <el-table-column prop="riskLevel" label="风险等级" width="80"><template slot-scope="scope"><el-tag :type="getRiskLevelType(scope.row.riskLevel)" size="mini">{{ scope.row.riskLevel }}</el-tag></template></el-table-column>
        <el-table-column prop="overallScore" label="综合评分" width="80"><template slot-scope="scope"><span :class="getScoreClass(scope.row.overallScore)">{{ scope.row.overallScore }}</span></template></el-table-column>
        <el-table-column prop="recommendation" label="评估建议" width="100"><template slot-scope="scope"><el-tag :type="getRecommendationType(scope.row.recommendation)">{{ scope.row.recommendation }}</el-tag></template></el-table-column>
        <el-table-column prop="status" label="评估状态" width="80"><template slot-scope="scope"><el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag></template></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="success" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-container"><el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pagination.currentPage" :page-sizes="[10,20,50,100]" :page-size="pagination.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" /></div>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="editDialogVisible" width="700px" :close-on-click-modal="false">
      <el-form :model="editForm" ref="editForm" label-width="110px" :rules="editRules">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="评估编号" prop="evaluationNo"><el-input v-model="editForm.evaluationNo" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="技术名称" prop="techName"><el-input v-model="editForm.techName" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="评估类型" prop="evaluationType"><el-select v-model="editForm.evaluationType" style="width:100%"><el-option label="技术可行性" value="技术可行性" /><el-option label="市场前景" value="市场前景" /><el-option label="商业价值" value="商业价值" /><el-option label="风险评估" value="风险评估" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="评估状态"><el-select v-model="editForm.status" style="width:100%"><el-option label="待评估" value="待评估" /><el-option label="评估中" value="评估中" /><el-option label="已完成" value="已完成" /><el-option label="需复评" value="需复评" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="申请人"><el-input v-model="editForm.applicant" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="评估专家"><el-input v-model="editForm.evaluator" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="评估日期"><el-date-picker v-model="editForm.evaluationDate" type="date" value-format="yyyy-MM-dd" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="完成日期"><el-date-picker v-model="editForm.completionDate" type="date" value-format="yyyy-MM-dd" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="技术成熟度(%)"><el-slider v-model="editForm.techMaturity" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="市场潜力"><el-rate v-model="editForm.marketPotential" show-score style="padding-top:6px" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="商业价值(万)"><el-input-number v-model="editForm.commercialValue" :precision="2" :min="0" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="综合评分"><el-input-number v-model="editForm.overallScore" :precision="1" :min="0" :max="100" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="风险等级"><el-select v-model="editForm.riskLevel" style="width:100%"><el-option label="低" value="低" /><el-option label="中" value="中" /><el-option label="高" value="高" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="评估建议"><el-select v-model="editForm.recommendation" style="width:100%"><el-option label="推荐" value="推荐" /><el-option label="有条件推荐" value="有条件推荐" /><el-option label="不推荐" value="不推荐" /><el-option label="需进一步研究" value="需进一步研究" /></el-select></el-form-item></el-col>
        </el-row>
        <el-form-item label="描述"><el-input v-model="editForm.description" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="editDialogVisible = false">取 消</el-button><el-button type="primary" @click="handleSubmit" :loading="submitLoading">确 定</el-button></div>
    </el-dialog>

    <el-dialog title="评估详情" :visible.sync="detailDialogVisible" width="700px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="评估编号">{{ detailData.evaluationNo }}</el-descriptions-item>
        <el-descriptions-item label="技术名称">{{ detailData.techName }}</el-descriptions-item>
        <el-descriptions-item label="评估类型">{{ detailData.evaluationType }}</el-descriptions-item>
        <el-descriptions-item label="评估状态">{{ detailData.status }}</el-descriptions-item>
        <el-descriptions-item label="申请人">{{ detailData.applicant }}</el-descriptions-item>
        <el-descriptions-item label="评估专家">{{ detailData.evaluator }}</el-descriptions-item>
        <el-descriptions-item label="评估日期">{{ detailData.evaluationDate }}</el-descriptions-item>
        <el-descriptions-item label="完成日期">{{ detailData.completionDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="技术成熟度">{{ detailData.techMaturity }}%</el-descriptions-item>
        <el-descriptions-item label="市场潜力"><el-rate :value="detailData.marketPotential || 0" disabled show-score /></el-descriptions-item>
        <el-descriptions-item label="商业价值">{{ detailData.commercialValue }}万元</el-descriptions-item>
        <el-descriptions-item label="综合评分">{{ detailData.overallScore }}</el-descriptions-item>
        <el-descriptions-item label="风险等级">{{ detailData.riskLevel }}</el-descriptions-item>
        <el-descriptions-item label="评估建议">{{ detailData.recommendation }}</el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ detailData.description }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { evaluationApi } from '@/api/enterprise/innovation'
export default {
  name: 'TechEvaluationManagement',
  data() {
    return {
      loading: false, submitLoading: false,
      queryForm: { evaluationNo: '', techName: '', evaluationType: '', status: '', evaluator: '' },
      evaluationList: [], multipleSelection: [],
      pagination: { currentPage: 1, pageSize: 15, total: 0 },
      editDialogVisible: false, detailDialogVisible: false, isEdit: false,
      editForm: { evaluationNo: '', techName: '', evaluationType: '', applicant: '', evaluator: '', evaluationDate: '', techMaturity: 50, marketPotential: 3, commercialValue: 0, riskLevel: '低', overallScore: 70, recommendation: '推荐', status: '待评估', completionDate: '', description: '' },
      editRules: { evaluationNo: [{ required: true, message: '请输入评估编号', trigger: 'blur' }], techName: [{ required: true, message: '请输入技术名称', trigger: 'blur' }], evaluationType: [{ required: true, message: '请选择评估类型', trigger: 'change' }] },
      detailData: {}
    }
  },
  computed: { dialogTitle() { return this.isEdit ? '编辑评估' : '新增评估' } },
  mounted() { this.loadEvaluationList() },
  methods: {
    async loadEvaluationList() {
      this.loading = true; try { const res = await evaluationApi.getList({ ...this.queryForm, pageNumber: this.pagination.currentPage, pageSize: this.pagination.pageSize }); if (res && res.data) { this.evaluationList = res.data.tlist || []; this.pagination.total = res.data.totalRecord || 0 } else { this.evaluationList = []; this.pagination.total = 0 } } catch (e) { this.evaluationList = []; this.pagination.total = 0 } finally { this.loading = false }
    },
    handleSearch() { this.pagination.currentPage = 1; this.loadEvaluationList() },
    handleReset() { this.$refs.queryForm.resetFields(); this.loadEvaluationList() },
    handleAdd() { this.isEdit = false; this.editForm = { evaluationNo: '', techName: '', evaluationType: '', applicant: '', evaluator: '', evaluationDate: '', techMaturity: 50, marketPotential: 3, commercialValue: 0, riskLevel: '低', overallScore: 70, recommendation: '推荐', status: '待评估', completionDate: '', description: '' }; this.editDialogVisible = true; this.$nextTick(() => { if (this.$refs.editForm) this.$refs.editForm.clearValidate() }) },
    handleEdit(row) { this.isEdit = true; this.editForm = { ...row }; this.editDialogVisible = true },
    handleView(row) { this.detailData = row; this.detailDialogVisible = true },
    async handleDelete(row) { try { await this.$confirm('确定删除吗？', '提示', { type: 'warning' }); const res = await evaluationApi.delete(row.id); if (res && res.result === 200) { this.$message.success('删除成功'); this.loadEvaluationList() } } catch (e) { if (e !== 'cancel') console.error(e) } },
    async handleBatchDelete() { if (!this.multipleSelection.length) return; try { await this.$confirm(`确定删除选中的${this.multipleSelection.length}条记录吗？`, '提示', { type: 'warning' }); const ids = this.multipleSelection.map(i => i.id); const res = await evaluationApi.batchDelete({ ids }); if (res && res.result === 200) { this.$message.success('批量删除成功'); this.loadEvaluationList() } } catch (e) { if (e !== 'cancel') console.error(e) } },
    async handleExport() { try { const res = await evaluationApi.export(); const blob = new Blob([res.data], { type: 'application/vnd.ms-excel' }); const link = document.createElement('a'); link.href = URL.createObjectURL(blob); link.download = '技术评估数据.xlsx'; link.click(); URL.revokeObjectURL(link.href); this.$message.success('导出成功') } catch (e) { this.$message.error('导出失败') } },
    async handleSubmit() { this.$refs.editForm.validate(async (valid) => { if (!valid) return; this.submitLoading = true; try { const res = this.isEdit ? await evaluationApi.update(this.editForm.id, this.editForm) : await evaluationApi.add(this.editForm); if (res && res.result === 200) { this.$message.success(this.isEdit ? '更新成功' : '新增成功'); this.editDialogVisible = false; this.loadEvaluationList() } } catch (e) { this.$message.error('操作失败') } finally { this.submitLoading = false } }) },
    handleSelectionChange(s) { this.multipleSelection = s },
    handleSizeChange(v) { this.pagination.pageSize = v; this.loadEvaluationList() },
    handleCurrentChange(v) { this.pagination.currentPage = v; this.loadEvaluationList() },
    getTypeColor(t) { return { '技术可行性': 'primary', '市场前景': 'success', '商业价值': 'warning', '风险评估': 'danger' }[t] || 'info' },
    getMaturityColor(m) { if (m >= 90) return '#67c23a'; if (m >= 70) return '#e6a23c'; return '#f56c6c' },
    getRiskLevelType(l) { return { '低': 'success', '中': 'warning', '高': 'danger' }[l] || 'info' },
    getScoreClass(s) { if (s >= 90) return 'score-excellent'; if (s >= 80) return 'score-good'; if (s >= 70) return 'score-normal'; return 'score-poor' },
    getRecommendationType(r) { return { '推荐': 'success', '有条件推荐': 'warning', '不推荐': 'danger', '需进一步研究': 'info' }[r] || 'info' },
    getStatusType(s) { return { '待评估': 'info', '评估中': 'primary', '已完成': 'success', '需复评': 'warning' }[s] || 'info' }
  }
}
</script>
<style lang="scss" scoped>
.tech-evaluation-management { .search-card, .action-card, .table-card { margin-bottom: 16px; } .commercial-value { color: #67C23A; font-weight: 500; } .score-excellent { color: #67C23A; font-weight: bold; font-size: 16px; } .score-good { color: #409EFF; font-weight: 500; } .score-normal { color: #E6A23C; } .score-poor { color: #F56C6C; } .pagination-container { margin-top: 20px; text-align: right; } }
</style>
