<template>
  <div class="performance-management-tab">
    <el-card class="overview-card">
      <div slot="header"><span>绩效管理概览</span><el-button style="float: right; padding: 3px 0" type="text" @click="refreshData">刷新</el-button></div>
      <el-row :gutter="20">
        <el-col :span="6"><div class="stat-item"><i class="el-icon-trophy stat-icon" style="color: #409EFF"></i><div class="stat-info"><div class="stat-value">{{ overview.totalScore || 0 }}分</div><div class="stat-label">总体绩效得分</div></div></div></el-col>
        <el-col :span="6"><div class="stat-item"><i class="el-icon-success stat-icon" style="color: #67C23A"></i><div class="stat-info"><div class="stat-value">{{ overview.completionRate || 0 }}%</div><div class="stat-label">目标完成率</div></div></div></el-col>
        <el-col :span="6"><div class="stat-item"><i class="el-icon-user stat-icon" style="color: #E6A23C"></i><div class="stat-info"><div class="stat-value">{{ overview.evaluatedCount || 0 }}</div><div class="stat-label">考核人数</div></div></div></el-col>
        <el-col :span="6"><div class="stat-item"><i class="el-icon-star-on stat-icon" style="color: #F56C6C"></i><div class="stat-info"><div class="stat-value">{{ overview.excellentRate || 0 }}%</div><div class="stat-label">优秀率</div></div></div></el-col>
      </el-row>
    </el-card>
    <el-card class="table-card">
      <div slot="header"><span>绩效考核记录</span><div style="float: right;"><el-button type="primary" size="small" @click="handleAdd" icon="el-icon-plus">发起考核</el-button></div></div>
      <el-form :model="queryForm" :inline="true" class="query-form">
        <el-form-item label="考核类型"><el-select v-model="queryForm.evaluationType" placeholder="请选择" clearable><el-option label="月度考核" value="月度考核"></el-option><el-option label="季度考核" value="季度考核"></el-option><el-option label="年度考核" value="年度考核"></el-option><el-option label="项目考核" value="项目考核"></el-option></el-select></el-form-item>
        <el-form-item label="部门"><el-input v-model="queryForm.department" placeholder="请输入部门" clearable></el-input></el-form-item>
        <el-form-item label="考核状态"><el-select v-model="queryForm.status" placeholder="请选择" clearable><el-option label="进行中" value="进行中"></el-option><el-option label="已完成" value="已完成"></el-option><el-option label="待审核" value="待审核"></el-option></el-select></el-form-item>
        <el-form-item><el-button type="primary" @click="queryData" icon="el-icon-search">查询</el-button><el-button @click="resetQuery" icon="el-icon-refresh">重置</el-button></el-form-item>
      </el-form>
      <el-table :data="evaluationList" border v-loading="loading">
        <el-table-column prop="evaluationName" label="考核名称" min-width="180" show-overflow-tooltip></el-table-column>
        <el-table-column prop="evaluationType" label="考核类型" width="90" align="center"></el-table-column>
        <el-table-column prop="department" label="部门" width="80"></el-table-column>
        <el-table-column prop="evaluatee" label="被考核人" width="80"></el-table-column>
        <el-table-column prop="score" label="得分" width="70" align="center"><template slot-scope="scope"><span :style="{color: scope.row.score >= 90 ? '#67C23A' : scope.row.score >= 80 ? '#409EFF' : scope.row.score >= 70 ? '#E6A23C' : '#F56C6C', fontWeight: 'bold'}">{{ scope.row.score }}</span></template></el-table-column>
        <el-table-column prop="rating" label="等级" width="70" align="center"><template slot-scope="scope"><el-tag :type="scope.row.rating === '优秀' ? 'success' : scope.row.rating === '良好' ? 'primary' : scope.row.rating === '合格' ? 'warning' : 'danger'" size="small">{{ scope.row.rating }}</el-tag></template></el-table-column>
        <el-table-column prop="completionRate" label="目标完成率" width="120" align="center"><template slot-scope="scope"><el-progress :percentage="scope.row.completionRate || 0" :stroke-width="8"></el-progress></template></el-table-column>
        <el-table-column prop="evaluator" label="考核人" width="80"></el-table-column>
        <el-table-column prop="evaluationDate" label="考核日期" width="110"></el-table-column>
        <el-table-column prop="status" label="状态" width="80" align="center"><template slot-scope="scope"><el-tag :type="scope.row.status === '已完成' ? 'success' : scope.row.status === '进行中' ? 'primary' : 'warning'" size="small">{{ scope.row.status }}</el-tag></template></el-table-column>
        <el-table-column label="操作" width="150" fixed="right" align="center"><template slot-scope="scope"><el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button><el-button type="text" size="small" @click="handleDelete(scope.row)" style="color: #F56C6C">删除</el-button></template></el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="queryForm.pageNumber" :page-sizes="[10, 20, 50, 100]" :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" style="margin-top: 20px; text-align: right;"></el-pagination>
    </el-card>
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="650px" :close-on-click-modal="false">
      <el-form :model="formData" label-width="100px" ref="formRef">
        <el-form-item label="考核名称" prop="evaluationName" :rules="[{ required: true, message: '请输入考核名称' }]"><el-input v-model="formData.evaluationName" placeholder="请输入考核名称"></el-input></el-form-item>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="考核类型" prop="evaluationType"><el-select v-model="formData.evaluationType" style="width: 100%;"><el-option label="月度考核" value="月度考核"></el-option><el-option label="季度考核" value="季度考核"></el-option><el-option label="年度考核" value="年度考核"></el-option><el-option label="项目考核" value="项目考核"></el-option></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="部门" prop="department"><el-input v-model="formData.department" placeholder="请输入部门"></el-input></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="被考核人" prop="evaluatee"><el-input v-model="formData.evaluatee" placeholder="请输入被考核人"></el-input></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="考核人" prop="evaluator"><el-input v-model="formData.evaluator" placeholder="请输入考核人"></el-input></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8"><el-form-item label="得分" prop="score"><el-input-number v-model="formData.score" :min="0" :max="100" style="width: 100%;"></el-input-number></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="等级" prop="rating"><el-select v-model="formData.rating" style="width: 100%;"><el-option label="优秀" value="优秀"></el-option><el-option label="良好" value="良好"></el-option><el-option label="合格" value="合格"></el-option><el-option label="不合格" value="不合格"></el-option></el-select></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="完成率" prop="completionRate"><el-input-number v-model="formData.completionRate" :min="0" :max="100" style="width: 100%;"></el-input-number></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="考核日期" prop="evaluationDate"><el-date-picker v-model="formData.evaluationDate" type="date" value-format="yyyy-MM-dd" style="width: 100%;"></el-date-picker></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="状态" prop="status"><el-select v-model="formData.status" style="width: 100%;"><el-option label="进行中" value="进行中"></el-option><el-option label="已完成" value="已完成"></el-option><el-option label="待审核" value="待审核"></el-option></el-select></el-form-item></el-col>
        </el-row>
        <el-form-item label="备注" prop="remark"><el-input v-model="formData.remark" type="textarea" :rows="2" placeholder="请输入备注"></el-input></el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="dialogVisible = false">取 消</el-button><el-button type="primary" @click="handleSubmit">确 定</el-button></span>
    </el-dialog>
  </div>
</template>

<script>
import { getPerformanceAssessmentList, addPerformanceAssessment, updatePerformanceAssessment, deletePerformanceAssessment, getPerformanceAssessmentStatistics } from '@/api/enterprise/operation'
export default {
  name: 'PerformanceManagementTab',
  props: { enterpriseId: { type: String, default: '' }, enterpriseName: { type: String, default: '' } },
  data() {
    return { loading: false, queryForm: { evaluationType: '', department: '', status: '', pageNumber: 1, pageSize: 20 }, total: 0, overview: {}, evaluationList: [], dialogVisible: false, dialogTitle: '发起绩效考核', isEdit: false, formData: { evaluationName: '', evaluationType: '', department: '', evaluatee: '', score: 0, rating: '合格', completionRate: 0, evaluator: '', evaluationDate: '', status: '进行中', remark: '' } }
  },
  watch: { enterpriseId: { handler(val) { if (val) { this.queryForm.enterpriseId = val; this.loadData(); this.loadStatistics(); } }, immediate: true } },
  methods: {
    extractData(response) { if (!response) return null; const d = response.data || response; return d && d.pageInfo ? d.pageInfo : d },
    async loadData() { if (!this.enterpriseId) return; this.loading = true; try { const res = await getPerformanceAssessmentList(this.queryForm); const p = this.extractData(res); this.evaluationList = p && p.tlist ? p.tlist : []; this.total = p && p.totalRecord ? p.totalRecord : 0; } catch (e) { console.error(e) } finally { this.loading = false } },
    async loadStatistics() { if (!this.enterpriseId) return; try { const res = await getPerformanceAssessmentStatistics({ enterpriseId: this.enterpriseId }); this.overview = this.extractData(res) || {}; } catch (e) { console.error(e) } },
    refreshData() { this.loadData(); this.loadStatistics(); this.$message.success('刷新成功') },
    queryData() { this.queryForm.pageNumber = 1; this.loadData() },
    resetQuery() { this.queryForm = { evaluationType: '', department: '', status: '', pageNumber: 1, pageSize: 20, enterpriseId: this.enterpriseId }; this.loadData() },
    handleSizeChange(val) { this.queryForm.pageSize = val; this.queryForm.pageNumber = 1; this.loadData() },
    handleCurrentChange(val) { this.queryForm.pageNumber = val; this.loadData() },
    handleAdd() { this.isEdit = false; this.dialogTitle = '发起绩效考核'; this.formData = { evaluationName: '', evaluationType: '', department: '', evaluatee: '', score: 0, rating: '合格', completionRate: 0, evaluator: '', evaluationDate: '', status: '进行中', remark: '', enterpriseId: this.enterpriseId, enterpriseName: this.enterpriseName }; this.dialogVisible = true },
    handleEdit(row) { this.isEdit = true; this.dialogTitle = '编辑绩效考核'; this.formData = { ...row }; this.dialogVisible = true },
    async handleSubmit() { this.$refs.formRef.validate(async (valid) => { if (!valid) return; try { this.formData.enterpriseId = this.enterpriseId; this.formData.enterpriseName = this.enterpriseName; if (this.isEdit) { await updatePerformanceAssessment(this.formData); this.$message.success('更新成功') } else { await addPerformanceAssessment(this.formData); this.$message.success('新增成功') } this.dialogVisible = false; this.loadData(); this.loadStatistics() } catch (e) { this.$message.error('操作失败') } }) },
    async handleDelete(row) { try { await this.$confirm('确认删除该绩效考核记录？', '提示', { type: 'warning' }); await deletePerformanceAssessment(row.id); this.$message.success('删除成功'); this.loadData(); this.loadStatistics() } catch (e) { if (e !== 'cancel') this.$message.error('删除失败') } }
  }
}
</script>
<style scoped>
.performance-management-tab { padding: 20px; }
.overview-card { margin-bottom: 20px; }
.table-card { margin-bottom: 20px; }
.query-form { margin-bottom: 20px; }
.stat-item { display: flex; align-items: center; padding: 10px 0; }
.stat-icon { font-size: 32px; margin-right: 12px; }
.stat-info { flex: 1; }
.stat-value { font-size: 22px; font-weight: bold; color: #303133; }
.stat-label { font-size: 13px; color: #909399; margin-top: 4px; }
</style>
