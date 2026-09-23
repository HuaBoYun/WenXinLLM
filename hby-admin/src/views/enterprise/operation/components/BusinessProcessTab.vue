<template>
  <div class="business-process-tab">
    <el-card class="overview-card">
      <div slot="header"><span>业务流程概览</span><el-button style="float: right; padding: 3px 0" type="text" @click="refreshData">刷新</el-button></div>
      <el-row :gutter="20">
        <el-col :span="6"><div class="stat-item"><i class="el-icon-share stat-icon" style="color: #409EFF"></i><div class="stat-info"><div class="stat-value">{{ overview.totalProcesses || 0 }}</div><div class="stat-label">流程总数</div></div></div></el-col>
        <el-col :span="6"><div class="stat-item"><i class="el-icon-loading stat-icon" style="color: #67C23A"></i><div class="stat-info"><div class="stat-value">{{ overview.activeProcesses || 0 }}</div><div class="stat-label">运行中流程</div></div></div></el-col>
        <el-col :span="6"><div class="stat-item"><i class="el-icon-data-analysis stat-icon" style="color: #E6A23C"></i><div class="stat-info"><div class="stat-value">{{ overview.efficiency || 0 }}%</div><div class="stat-label">流程效率</div></div></div></el-col>
        <el-col :span="6"><div class="stat-item"><i class="el-icon-warning stat-icon" style="color: #F56C6C"></i><div class="stat-info"><div class="stat-value">{{ overview.abnormalProcesses || 0 }}</div><div class="stat-label">异常流程</div></div></div></el-col>
      </el-row>
    </el-card>
    <el-card class="table-card">
      <div slot="header"><span>业务流程列表</span><div style="float: right;"><el-button type="primary" size="small" @click="handleAdd" icon="el-icon-plus">新建流程</el-button></div></div>
      <el-form :model="queryForm" :inline="true" class="query-form">
        <el-form-item label="流程类型"><el-select v-model="queryForm.processType" placeholder="请选择" clearable><el-option label="审批流程" value="审批流程"></el-option><el-option label="业务流程" value="业务流程"></el-option><el-option label="财务流程" value="财务流程"></el-option><el-option label="人事流程" value="人事流程"></el-option></el-select></el-form-item>
        <el-form-item label="流程状态"><el-select v-model="queryForm.status" placeholder="请选择" clearable><el-option label="设计中" value="设计中"></el-option><el-option label="运行中" value="运行中"></el-option><el-option label="已暂停" value="已暂停"></el-option><el-option label="已停用" value="已停用"></el-option></el-select></el-form-item>
        <el-form-item><el-button type="primary" @click="queryData" icon="el-icon-search">查询</el-button><el-button @click="resetQuery" icon="el-icon-refresh">重置</el-button></el-form-item>
      </el-form>
      <el-table :data="processList" border v-loading="loading">
        <el-table-column prop="processCode" label="流程编号" width="110"></el-table-column>
        <el-table-column prop="processName" label="流程名称" min-width="160" show-overflow-tooltip></el-table-column>
        <el-table-column prop="processType" label="流程类型" width="100" align="center"></el-table-column>
        <el-table-column prop="owner" label="负责人" width="80"></el-table-column>
        <el-table-column prop="stepCount" label="步骤数" width="70" align="center"></el-table-column>
        <el-table-column prop="avgDuration" label="平均耗时(h)" width="100" align="center"></el-table-column>
        <el-table-column prop="efficiency" label="效率" width="80" align="center"><template slot-scope="scope"><span :style="{color: scope.row.efficiency >= 90 ? '#67C23A' : scope.row.efficiency >= 80 ? '#E6A23C' : '#F56C6C', fontWeight: 'bold'}">{{ scope.row.efficiency || 0 }}%</span></template></el-table-column>
        <el-table-column prop="instanceCount" label="实例数" width="70" align="center"></el-table-column>
        <el-table-column prop="lastModified" label="最后修改" width="110"></el-table-column>
        <el-table-column prop="status" label="状态" width="80" align="center"><template slot-scope="scope"><el-tag :type="scope.row.status === '运行中' ? 'success' : scope.row.status === '已暂停' ? 'warning' : scope.row.status === '已停用' ? 'danger' : 'info'" size="small">{{ scope.row.status }}</el-tag></template></el-table-column>
        <el-table-column label="操作" width="150" fixed="right" align="center"><template slot-scope="scope"><el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button><el-button type="text" size="small" @click="handleDelete(scope.row)" style="color: #F56C6C">删除</el-button></template></el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="queryForm.pageNumber" :page-sizes="[10, 20, 50, 100]" :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" style="margin-top: 20px; text-align: right;"></el-pagination>
    </el-card>
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="650px" :close-on-click-modal="false">
      <el-form :model="formData" label-width="100px" ref="formRef">
        <el-form-item label="流程编号" prop="processCode" :rules="[{ required: true, message: '请输入流程编号' }]"><el-input v-model="formData.processCode" placeholder="如 BP-SC-001"></el-input></el-form-item>
        <el-form-item label="流程名称" prop="processName" :rules="[{ required: true, message: '请输入流程名称' }]"><el-input v-model="formData.processName" placeholder="请输入流程名称"></el-input></el-form-item>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="流程类型" prop="processType"><el-select v-model="formData.processType" style="width: 100%;"><el-option label="审批流程" value="审批流程"></el-option><el-option label="业务流程" value="业务流程"></el-option><el-option label="财务流程" value="财务流程"></el-option><el-option label="人事流程" value="人事流程"></el-option></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="负责人" prop="owner"><el-input v-model="formData.owner" placeholder="请输入负责人"></el-input></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8"><el-form-item label="步骤数" prop="stepCount"><el-input-number v-model="formData.stepCount" :min="1" style="width: 100%;"></el-input-number></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="平均耗时(h)" prop="avgDuration"><el-input-number v-model="formData.avgDuration" :min="0" style="width: 100%;"></el-input-number></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="效率(%)" prop="efficiency"><el-input-number v-model="formData.efficiency" :min="0" :max="100" style="width: 100%;"></el-input-number></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8"><el-form-item label="实例数" prop="instanceCount"><el-input-number v-model="formData.instanceCount" :min="0" style="width: 100%;"></el-input-number></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="状态" prop="status"><el-select v-model="formData.status" style="width: 100%;"><el-option label="设计中" value="设计中"></el-option><el-option label="运行中" value="运行中"></el-option><el-option label="已暂停" value="已暂停"></el-option><el-option label="已停用" value="已停用"></el-option></el-select></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="最后修改" prop="lastModified"><el-date-picker v-model="formData.lastModified" type="date" value-format="yyyy-MM-dd" style="width: 100%;"></el-date-picker></el-form-item></el-col>
        </el-row>
        <el-form-item label="备注" prop="remark"><el-input v-model="formData.remark" type="textarea" :rows="2" placeholder="请输入备注"></el-input></el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="dialogVisible = false">取 消</el-button><el-button type="primary" @click="handleSubmit">确 定</el-button></span>
    </el-dialog>
  </div>
</template>

<script>
import { getBusinessProcessList, addBusinessProcess, updateBusinessProcess, deleteBusinessProcess, getBusinessProcessStatistics } from '@/api/enterprise/operation'
export default {
  name: 'BusinessProcessTab',
  props: { enterpriseId: { type: String, default: '' }, enterpriseName: { type: String, default: '' } },
  data() {
    return { loading: false, queryForm: { processType: '', status: '', pageNumber: 1, pageSize: 20 }, total: 0, overview: {}, processList: [], dialogVisible: false, dialogTitle: '新建业务流程', isEdit: false, formData: { processCode: '', processName: '', processType: '', owner: '', stepCount: 1, avgDuration: 0, efficiency: 80, instanceCount: 0, lastModified: '', status: '设计中', remark: '' } }
  },
  watch: { enterpriseId: { handler(val) { if (val) { this.queryForm.enterpriseId = val; this.loadData(); this.loadStatistics(); } }, immediate: true } },
  methods: {
    extractData(response) { if (!response) return null; const d = response.data || response; return d && d.pageInfo ? d.pageInfo : d },
    async loadData() { if (!this.enterpriseId) return; this.loading = true; try { const res = await getBusinessProcessList(this.queryForm); const p = this.extractData(res); this.processList = p && p.tlist ? p.tlist : []; this.total = p && p.totalRecord ? p.totalRecord : 0; } catch (e) { console.error(e) } finally { this.loading = false } },
    async loadStatistics() { if (!this.enterpriseId) return; try { const res = await getBusinessProcessStatistics({ enterpriseId: this.enterpriseId }); this.overview = this.extractData(res) || {}; } catch (e) { console.error(e) } },
    refreshData() { this.loadData(); this.loadStatistics(); this.$message.success('刷新成功') },
    queryData() { this.queryForm.pageNumber = 1; this.loadData() },
    resetQuery() { this.queryForm = { processType: '', status: '', pageNumber: 1, pageSize: 20, enterpriseId: this.enterpriseId }; this.loadData() },
    handleSizeChange(val) { this.queryForm.pageSize = val; this.queryForm.pageNumber = 1; this.loadData() },
    handleCurrentChange(val) { this.queryForm.pageNumber = val; this.loadData() },
    handleAdd() { this.isEdit = false; this.dialogTitle = '新建业务流程'; this.formData = { processCode: '', processName: '', processType: '', owner: '', stepCount: 1, avgDuration: 0, efficiency: 80, instanceCount: 0, lastModified: '', status: '设计中', remark: '', enterpriseId: this.enterpriseId, enterpriseName: this.enterpriseName }; this.dialogVisible = true },
    handleEdit(row) { this.isEdit = true; this.dialogTitle = '编辑业务流程'; this.formData = { ...row }; this.dialogVisible = true },
    async handleSubmit() { this.$refs.formRef.validate(async (valid) => { if (!valid) return; try { this.formData.enterpriseId = this.enterpriseId; this.formData.enterpriseName = this.enterpriseName; if (this.isEdit) { await updateBusinessProcess(this.formData); this.$message.success('更新成功') } else { await addBusinessProcess(this.formData); this.$message.success('新增成功') } this.dialogVisible = false; this.loadData(); this.loadStatistics() } catch (e) { this.$message.error('操作失败') } }) },
    async handleDelete(row) { try { await this.$confirm('确认删除该业务流程？', '提示', { type: 'warning' }); await deleteBusinessProcess(row.id); this.$message.success('删除成功'); this.loadData(); this.loadStatistics() } catch (e) { if (e !== 'cancel') this.$message.error('删除失败') } }
  }
}
</script>
<style scoped>
.business-process-tab { padding: 20px; }
.overview-card { margin-bottom: 20px; }
.table-card { margin-bottom: 20px; }
.query-form { margin-bottom: 20px; }
.stat-item { display: flex; align-items: center; padding: 10px 0; }
.stat-icon { font-size: 32px; margin-right: 12px; }
.stat-info { flex: 1; }
.stat-value { font-size: 22px; font-weight: bold; color: #303133; }
.stat-label { font-size: 13px; color: #909399; margin-top: 4px; }
</style>
