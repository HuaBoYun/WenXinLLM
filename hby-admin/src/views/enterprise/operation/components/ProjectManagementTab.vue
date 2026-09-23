<template>
  <div class="project-management-tab">
    <el-card class="overview-card">
      <div slot="header"><span>项目管理概览</span><el-button style="float: right; padding: 3px 0" type="text" @click="refreshData">刷新</el-button></div>
      <el-row :gutter="20">
        <el-col :span="6"><div class="stat-item"><i class="el-icon-folder stat-icon" style="color: #409EFF"></i><div class="stat-info"><div class="stat-value">{{ overview.totalProjects || 0 }}</div><div class="stat-label">项目总数</div></div></div></el-col>
        <el-col :span="6"><div class="stat-item"><i class="el-icon-loading stat-icon" style="color: #67C23A"></i><div class="stat-info"><div class="stat-value">{{ overview.activeProjects || 0 }}</div><div class="stat-label">进行中项目</div></div></div></el-col>
        <el-col :span="6"><div class="stat-item"><i class="el-icon-success stat-icon" style="color: #E6A23C"></i><div class="stat-info"><div class="stat-value">{{ overview.completionRate || 0 }}%</div><div class="stat-label">完成率</div></div></div></el-col>
        <el-col :span="6"><div class="stat-item"><i class="el-icon-time stat-icon" style="color: #F56C6C"></i><div class="stat-info"><div class="stat-value">{{ overview.onTimeRate || 0 }}%</div><div class="stat-label">按时完成率</div></div></div></el-col>
      </el-row>
    </el-card>
    <el-card class="table-card">
      <div slot="header"><span>项目列表</span><div style="float: right;"><el-button type="primary" size="small" @click="handleAdd" icon="el-icon-plus">新建项目</el-button></div></div>
      <el-form :model="queryForm" :inline="true" class="query-form">
        <el-form-item label="项目状态"><el-select v-model="queryForm.status" placeholder="请选择" clearable><el-option label="规划中" value="规划中"></el-option><el-option label="进行中" value="进行中"></el-option><el-option label="已完成" value="已完成"></el-option><el-option label="已暂停" value="已暂停"></el-option></el-select></el-form-item>
        <el-form-item label="项目类型"><el-select v-model="queryForm.projectType" placeholder="请选择" clearable><el-option label="技术项目" value="技术项目"></el-option><el-option label="业务项目" value="业务项目"></el-option><el-option label="基础设施" value="基础设施"></el-option><el-option label="研发项目" value="研发项目"></el-option></el-select></el-form-item>
        <el-form-item label="项目经理"><el-input v-model="queryForm.projectManager" placeholder="请输入" clearable></el-input></el-form-item>
        <el-form-item><el-button type="primary" @click="queryData" icon="el-icon-search">查询</el-button><el-button @click="resetQuery" icon="el-icon-refresh">重置</el-button></el-form-item>
      </el-form>
      <el-table :data="projectList" border v-loading="loading">
        <el-table-column prop="projectCode" label="项目编号" width="120"></el-table-column>
        <el-table-column prop="projectName" label="项目名称" min-width="160" show-overflow-tooltip></el-table-column>
        <el-table-column prop="projectType" label="项目类型" width="90" align="center"></el-table-column>
        <el-table-column prop="projectManager" label="项目经理" width="80"></el-table-column>
        <el-table-column prop="budget" label="预算(万)" width="100" align="right"></el-table-column>
        <el-table-column prop="actualCost" label="实际成本(万)" width="110" align="right"></el-table-column>
        <el-table-column prop="progress" label="进度" width="120" align="center"><template slot-scope="scope"><el-progress :percentage="scope.row.progress || 0" :stroke-width="8"></el-progress></template></el-table-column>
        <el-table-column prop="startDate" label="开始日期" width="110"></el-table-column>
        <el-table-column prop="endDate" label="结束日期" width="110"></el-table-column>
        <el-table-column prop="status" label="状态" width="80" align="center"><template slot-scope="scope"><el-tag :type="scope.row.status === '已完成' ? 'success' : scope.row.status === '进行中' ? 'primary' : scope.row.status === '已暂停' ? 'warning' : 'info'" size="small">{{ scope.row.status }}</el-tag></template></el-table-column>
        <el-table-column label="操作" width="150" fixed="right" align="center"><template slot-scope="scope"><el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button><el-button type="text" size="small" @click="handleDelete(scope.row)" style="color: #F56C6C">删除</el-button></template></el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="queryForm.pageNumber" :page-sizes="[10, 20, 50, 100]" :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" style="margin-top: 20px; text-align: right;"></el-pagination>
    </el-card>
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="650px" :close-on-click-modal="false">
      <el-form :model="formData" label-width="100px" ref="formRef">
        <el-form-item label="项目编号" prop="projectCode" :rules="[{ required: true, message: '请输入项目编号' }]"><el-input v-model="formData.projectCode" placeholder="如 PRJ-2025-001"></el-input></el-form-item>
        <el-form-item label="项目名称" prop="projectName" :rules="[{ required: true, message: '请输入项目名称' }]"><el-input v-model="formData.projectName" placeholder="请输入项目名称"></el-input></el-form-item>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="项目类型" prop="projectType"><el-select v-model="formData.projectType" style="width: 100%;"><el-option label="技术项目" value="技术项目"></el-option><el-option label="业务项目" value="业务项目"></el-option><el-option label="基础设施" value="基础设施"></el-option><el-option label="研发项目" value="研发项目"></el-option></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="项目经理" prop="projectManager"><el-input v-model="formData.projectManager" placeholder="请输入项目经理"></el-input></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="预算(万)" prop="budget"><el-input-number v-model="formData.budget" :min="0" :precision="2" style="width: 100%;"></el-input-number></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="实际成本(万)" prop="actualCost"><el-input-number v-model="formData.actualCost" :min="0" :precision="2" style="width: 100%;"></el-input-number></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8"><el-form-item label="进度(%)" prop="progress"><el-input-number v-model="formData.progress" :min="0" :max="100" style="width: 100%;"></el-input-number></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="开始日期" prop="startDate"><el-date-picker v-model="formData.startDate" type="date" value-format="yyyy-MM-dd" style="width: 100%;"></el-date-picker></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="结束日期" prop="endDate"><el-date-picker v-model="formData.endDate" type="date" value-format="yyyy-MM-dd" style="width: 100%;"></el-date-picker></el-form-item></el-col>
        </el-row>
        <el-form-item label="状态" prop="status"><el-select v-model="formData.status" style="width: 100%;"><el-option label="规划中" value="规划中"></el-option><el-option label="进行中" value="进行中"></el-option><el-option label="已完成" value="已完成"></el-option><el-option label="已暂停" value="已暂停"></el-option></el-select></el-form-item>
        <el-form-item label="备注" prop="remark"><el-input v-model="formData.remark" type="textarea" :rows="2" placeholder="请输入备注"></el-input></el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="dialogVisible = false">取 消</el-button><el-button type="primary" @click="handleSubmit">确 定</el-button></span>
    </el-dialog>
  </div>
</template>

<script>
import { getProjectManagementList, addProjectManagement, updateProjectManagement, deleteProjectManagement, getProjectManagementStatistics } from '@/api/enterprise/operation'
export default {
  name: 'ProjectManagementTab',
  props: { enterpriseId: { type: String, default: '' }, enterpriseName: { type: String, default: '' } },
  data() {
    return { loading: false, queryForm: { status: '', projectType: '', projectManager: '', pageNumber: 1, pageSize: 20 }, total: 0, overview: {}, projectList: [], dialogVisible: false, dialogTitle: '新建项目', isEdit: false, formData: { projectCode: '', projectName: '', projectType: '', projectManager: '', budget: 0, actualCost: 0, progress: 0, startDate: '', endDate: '', status: '规划中', remark: '' } }
  },
  watch: { enterpriseId: { handler(val) { if (val) { this.queryForm.enterpriseId = val; this.loadData(); this.loadStatistics(); } }, immediate: true } },
  methods: {
    extractData(response) { if (!response) return null; const d = response.data || response; return d && d.pageInfo ? d.pageInfo : d },
    async loadData() { if (!this.enterpriseId) return; this.loading = true; try { const res = await getProjectManagementList(this.queryForm); const p = this.extractData(res); this.projectList = p && p.tlist ? p.tlist : []; this.total = p && p.totalRecord ? p.totalRecord : 0; } catch (e) { console.error(e) } finally { this.loading = false } },
    async loadStatistics() { if (!this.enterpriseId) return; try { const res = await getProjectManagementStatistics({ enterpriseId: this.enterpriseId }); this.overview = this.extractData(res) || {}; } catch (e) { console.error(e) } },
    refreshData() { this.loadData(); this.loadStatistics(); this.$message.success('刷新成功') },
    queryData() { this.queryForm.pageNumber = 1; this.loadData() },
    resetQuery() { this.queryForm = { status: '', projectType: '', projectManager: '', pageNumber: 1, pageSize: 20, enterpriseId: this.enterpriseId }; this.loadData() },
    handleSizeChange(val) { this.queryForm.pageSize = val; this.queryForm.pageNumber = 1; this.loadData() },
    handleCurrentChange(val) { this.queryForm.pageNumber = val; this.loadData() },
    handleAdd() { this.isEdit = false; this.dialogTitle = '新建项目'; this.formData = { projectCode: '', projectName: '', projectType: '', projectManager: '', budget: 0, actualCost: 0, progress: 0, startDate: '', endDate: '', status: '规划中', remark: '', enterpriseId: this.enterpriseId, enterpriseName: this.enterpriseName }; this.dialogVisible = true },
    handleEdit(row) { this.isEdit = true; this.dialogTitle = '编辑项目'; this.formData = { ...row }; this.dialogVisible = true },
    async handleSubmit() { this.$refs.formRef.validate(async (valid) => { if (!valid) return; try { this.formData.enterpriseId = this.enterpriseId; this.formData.enterpriseName = this.enterpriseName; if (this.isEdit) { await updateProjectManagement(this.formData); this.$message.success('更新成功') } else { await addProjectManagement(this.formData); this.$message.success('新增成功') } this.dialogVisible = false; this.loadData(); this.loadStatistics() } catch (e) { this.$message.error('操作失败') } }) },
    async handleDelete(row) { try { await this.$confirm('确认删除该项目？', '提示', { type: 'warning' }); await deleteProjectManagement(row.id); this.$message.success('删除成功'); this.loadData(); this.loadStatistics() } catch (e) { if (e !== 'cancel') this.$message.error('删除失败') } }
  }
}
</script>
<style scoped>
.project-management-tab { padding: 20px; }
.overview-card { margin-bottom: 20px; }
.table-card { margin-bottom: 20px; }
.query-form { margin-bottom: 20px; }
.stat-item { display: flex; align-items: center; padding: 10px 0; }
.stat-icon { font-size: 32px; margin-right: 12px; }
.stat-info { flex: 1; }
.stat-value { font-size: 22px; font-weight: bold; color: #303133; }
.stat-label { font-size: 13px; color: #909399; margin-top: 4px; }
</style>
