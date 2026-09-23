<template>
  <div class="rd-project-management">
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="项目编号" prop="projectNo"><el-input v-model="queryForm.projectNo" placeholder="请输入项目编号" clearable /></el-form-item>
        <el-form-item label="项目名称" prop="projectName"><el-input v-model="queryForm.projectName" placeholder="请输入项目名称" clearable /></el-form-item>
        <el-form-item label="项目类型" prop="projectType">
          <el-select v-model="queryForm.projectType" placeholder="请选择" clearable>
            <el-option label="基础研究" value="基础研究" /><el-option label="应用研究" value="应用研究" /><el-option label="技术开发" value="技术开发" /><el-option label="产品开发" value="产品开发" />
          </el-select>
        </el-form-item>
        <el-form-item label="项目状态" prop="status">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable>
            <el-option label="立项申请" value="立项申请" /><el-option label="进行中" value="进行中" /><el-option label="已完成" value="已完成" /><el-option label="已暂停" value="已暂停" /><el-option label="已取消" value="已取消" />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人" prop="leader"><el-input v-model="queryForm.leader" placeholder="请输入负责人" clearable /></el-form-item>
        <el-form-item><el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button><el-button icon="el-icon-refresh" @click="handleReset">重置</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" class="action-card">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增项目</el-button>
          <el-button type="danger" icon="el-icon-delete" @click="handleBatchDelete" :disabled="multipleSelection.length === 0">批量删除</el-button>
          <el-button type="warning" icon="el-icon-download" @click="handleExport">导出数据</el-button>
        </el-col>
      </el-row>
    </el-card>
    <el-card shadow="never" class="table-card">
      <el-table :data="projectList" v-loading="loading" @selection-change="handleSelectionChange" stripe border>
        <el-table-column type="selection" width="55" />
        <el-table-column prop="projectNo" label="项目编号" width="120" />
        <el-table-column prop="projectName" label="项目名称" width="200" show-overflow-tooltip />
        <el-table-column prop="projectType" label="项目类型" width="100"><template slot-scope="scope"><el-tag :type="getTypeColor(scope.row.projectType)">{{ scope.row.projectType }}</el-tag></template></el-table-column>
        <el-table-column prop="leader" label="负责人" width="100" />
        <el-table-column prop="teamSize" label="团队规模" width="80"><template slot-scope="scope"><span>{{ scope.row.teamSize || 0 }}人</span></template></el-table-column>
        <el-table-column prop="budget" label="项目预算(万)" width="120"><template slot-scope="scope"><span class="budget-amount">{{ scope.row.budget || 0 }}</span></template></el-table-column>
        <el-table-column prop="usedBudget" label="已用预算(万)" width="120"><template slot-scope="scope"><span class="used-budget">{{ scope.row.usedBudget || 0 }}</span></template></el-table-column>
        <el-table-column prop="progress" label="项目进度" width="120"><template slot-scope="scope"><el-progress :percentage="scope.row.progress || 0" :color="getProgressColor(scope.row.progress || 0)" /></template></el-table-column>
        <el-table-column prop="startDate" label="开始日期" width="120" />
        <el-table-column prop="endDate" label="预计结束" width="120" />
        <el-table-column prop="status" label="项目状态" width="100"><template slot-scope="scope"><el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag></template></el-table-column>
        <el-table-column prop="priority" label="优先级" width="80"><template slot-scope="scope"><el-tag :type="getPriorityType(scope.row.priority)" size="mini">{{ scope.row.priority }}</el-tag></template></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="success" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-container">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pagination.currentPage" :page-sizes="[10,20,50,100]" :page-size="pagination.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="editDialogVisible" width="700px" :close-on-click-modal="false">
      <el-form :model="editForm" ref="editForm" label-width="110px" :rules="editRules">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="项目编号" prop="projectNo"><el-input v-model="editForm.projectNo" placeholder="请输入项目编号" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="项目名称" prop="projectName"><el-input v-model="editForm.projectName" placeholder="请输入项目名称" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="项目类型" prop="projectType"><el-select v-model="editForm.projectType" placeholder="请选择" style="width:100%"><el-option label="基础研究" value="基础研究" /><el-option label="应用研究" value="应用研究" /><el-option label="技术开发" value="技术开发" /><el-option label="产品开发" value="产品开发" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="负责人" prop="leader"><el-input v-model="editForm.leader" placeholder="请输入负责人" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="团队规模" prop="teamSize"><el-input-number v-model="editForm.teamSize" :min="1" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="优先级" prop="priority"><el-select v-model="editForm.priority" placeholder="请选择" style="width:100%"><el-option label="高" value="高" /><el-option label="中" value="中" /><el-option label="低" value="低" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="项目预算(万)" prop="budget"><el-input-number v-model="editForm.budget" :precision="2" :min="0" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="已用预算(万)" prop="usedBudget"><el-input-number v-model="editForm.usedBudget" :precision="2" :min="0" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="项目进度(%)"><el-slider v-model="editForm.progress" :min="0" :max="100" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="项目状态" prop="status"><el-select v-model="editForm.status" placeholder="请选择" style="width:100%"><el-option label="立项申请" value="立项申请" /><el-option label="进行中" value="进行中" /><el-option label="已完成" value="已完成" /><el-option label="已暂停" value="已暂停" /><el-option label="已取消" value="已取消" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="开始日期"><el-date-picker v-model="editForm.startDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="预计结束"><el-date-picker v-model="editForm.endDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="项目描述"><el-input v-model="editForm.description" type="textarea" :rows="3" placeholder="请输入项目描述" /></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="editDialogVisible = false">取 消</el-button><el-button type="primary" @click="handleSubmit" :loading="submitLoading">确 定</el-button></div>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog title="项目详情" :visible.sync="detailDialogVisible" width="700px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="项目编号">{{ detailData.projectNo }}</el-descriptions-item>
        <el-descriptions-item label="项目名称">{{ detailData.projectName }}</el-descriptions-item>
        <el-descriptions-item label="项目类型">{{ detailData.projectType }}</el-descriptions-item>
        <el-descriptions-item label="负责人">{{ detailData.leader }}</el-descriptions-item>
        <el-descriptions-item label="团队规模">{{ detailData.teamSize }}人</el-descriptions-item>
        <el-descriptions-item label="优先级">{{ detailData.priority }}</el-descriptions-item>
        <el-descriptions-item label="项目预算">{{ detailData.budget }}万元</el-descriptions-item>
        <el-descriptions-item label="已用预算">{{ detailData.usedBudget }}万元</el-descriptions-item>
        <el-descriptions-item label="项目进度">{{ detailData.progress }}%</el-descriptions-item>
        <el-descriptions-item label="项目状态">{{ detailData.status }}</el-descriptions-item>
        <el-descriptions-item label="开始日期">{{ detailData.startDate }}</el-descriptions-item>
        <el-descriptions-item label="预计结束">{{ detailData.endDate }}</el-descriptions-item>
        <el-descriptions-item label="项目描述" :span="2">{{ detailData.description }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { projectApi } from '@/api/enterprise/innovation'

export default {
  name: 'RDProjectManagement',
  data() {
    return {
      loading: false,
      submitLoading: false,
      queryForm: { projectNo: '', projectName: '', projectType: '', status: '', leader: '' },
      projectList: [],
      multipleSelection: [],
      pagination: { currentPage: 1, pageSize: 15, total: 0 },
      editDialogVisible: false,
      detailDialogVisible: false,
      isEdit: false,
      editForm: { projectNo: '', projectName: '', projectType: '', leader: '', teamSize: 1, budget: 0, usedBudget: 0, progress: 0, startDate: '', endDate: '', status: '立项申请', priority: '中', description: '' },
      editRules: { projectNo: [{ required: true, message: '请输入项目编号', trigger: 'blur' }], projectName: [{ required: true, message: '请输入项目名称', trigger: 'blur' }], projectType: [{ required: true, message: '请选择项目类型', trigger: 'change' }], leader: [{ required: true, message: '请输入负责人', trigger: 'blur' }] },
      detailData: {}
    }
  },
  computed: {
    dialogTitle() { return this.isEdit ? '编辑项目' : '新增项目' }
  },
  mounted() { this.loadProjectList() },
  methods: {
    async loadProjectList() {
      this.loading = true
      try {
        const res = await projectApi.getList({ ...this.queryForm, pageNumber: this.pagination.currentPage, pageSize: this.pagination.pageSize })
        if (res && res.data) { this.projectList = res.data.tlist || []; this.pagination.total = res.data.totalRecord || 0 }
        else { this.projectList = []; this.pagination.total = 0 }
      } catch (e) { console.error(e); this.projectList = []; this.pagination.total = 0 }
      finally { this.loading = false }
    },
    handleSearch() { this.pagination.currentPage = 1; this.loadProjectList() },
    handleReset() { this.$refs.queryForm.resetFields(); this.loadProjectList() },
    handleAdd() { this.isEdit = false; this.editForm = { projectNo: '', projectName: '', projectType: '', leader: '', teamSize: 1, budget: 0, usedBudget: 0, progress: 0, startDate: '', endDate: '', status: '立项申请', priority: '中', description: '' }; this.editDialogVisible = true; this.$nextTick(() => { if (this.$refs.editForm) this.$refs.editForm.clearValidate() }) },
    handleEdit(row) { this.isEdit = true; this.editForm = { ...row }; this.editDialogVisible = true },
    handleView(row) { this.detailData = row; this.detailDialogVisible = true },
    async handleDelete(row) {
      try { await this.$confirm('确定删除该项目吗？', '提示', { type: 'warning' }); const res = await projectApi.delete(row.id); if (res && res.result === 200) { this.$message.success('删除成功'); this.loadProjectList() } else { this.$message.error(res.msg || '删除失败') } } catch (e) { if (e !== 'cancel') console.error(e) }
    },
    async handleBatchDelete() {
      if (this.multipleSelection.length === 0) { this.$message.warning('请选择要删除的项目'); return }
      try { await this.$confirm(`确定删除选中的${this.multipleSelection.length}个项目吗？`, '提示', { type: 'warning' }); const ids = this.multipleSelection.map(i => i.id); const res = await projectApi.batchDelete({ ids }); if (res && res.result === 200) { this.$message.success('批量删除成功'); this.loadProjectList() } else { this.$message.error(res.msg || '批量删除失败') } } catch (e) { if (e !== 'cancel') console.error(e) }
    },
    async handleExport() {
      try { const res = await projectApi.export(); const blob = new Blob([res.data], { type: 'application/vnd.ms-excel' }); const link = document.createElement('a'); link.href = URL.createObjectURL(blob); link.download = '研发项目数据.xlsx'; link.click(); URL.revokeObjectURL(link.href); this.$message.success('导出成功') } catch (e) { this.$message.error('导出失败') }
    },
    async handleSubmit() {
      this.$refs.editForm.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          const res = this.isEdit ? await projectApi.update(this.editForm.id, this.editForm) : await projectApi.add(this.editForm)
          if (res && res.result === 200) { this.$message.success(this.isEdit ? '更新成功' : '新增成功'); this.editDialogVisible = false; this.loadProjectList() }
          else { this.$message.error(res.msg || '操作失败') }
        } catch (e) { this.$message.error('操作失败') }
        finally { this.submitLoading = false }
      })
    },
    handleSelectionChange(selection) { this.multipleSelection = selection },
    handleSizeChange(val) { this.pagination.pageSize = val; this.loadProjectList() },
    handleCurrentChange(val) { this.pagination.currentPage = val; this.loadProjectList() },
    getTypeColor(type) { return { '基础研究': 'primary', '应用研究': 'success', '技术开发': 'warning', '产品开发': 'danger' }[type] || 'info' },
    getStatusType(status) { return { '立项申请': 'info', '进行中': 'primary', '已完成': 'success', '已暂停': 'warning', '已取消': 'danger' }[status] || 'info' },
    getPriorityType(p) { return { '高': 'danger', '中': 'warning', '低': 'success' }[p] || 'info' },
    getProgressColor(p) { if (p < 30) return '#f56c6c'; if (p < 70) return '#e6a23c'; return '#67c23a' }
  }
}
</script>
<style lang="scss" scoped>
.rd-project-management { .search-card, .action-card, .table-card { margin-bottom: 16px; } .budget-amount { color: #409EFF; font-weight: 500; } .used-budget { color: #E6A23C; font-weight: 500; } .pagination-container { margin-top: 20px; text-align: right; } }
</style>
