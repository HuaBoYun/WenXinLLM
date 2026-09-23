<template>
  <div class="innovation-team-management">
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="团队编号" prop="teamNo"><el-input v-model="queryForm.teamNo" placeholder="请输入" clearable /></el-form-item>
        <el-form-item label="团队名称" prop="teamName"><el-input v-model="queryForm.teamName" placeholder="请输入" clearable /></el-form-item>
        <el-form-item label="研究方向" prop="researchDirection">
          <el-select v-model="queryForm.researchDirection" placeholder="请选择" clearable>
            <el-option label="人工智能" value="人工智能" /><el-option label="大数据" value="大数据" /><el-option label="云计算" value="云计算" /><el-option label="物联网" value="物联网" /><el-option label="区块链" value="区块链" />
          </el-select>
        </el-form-item>
        <el-form-item label="团队状态" prop="status">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable>
            <el-option label="活跃" value="活跃" /><el-option label="项目中" value="项目中" /><el-option label="休整期" value="休整期" /><el-option label="已解散" value="已解散" />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人" prop="leader"><el-input v-model="queryForm.leader" placeholder="请输入" clearable /></el-form-item>
        <el-form-item><el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button><el-button icon="el-icon-refresh" @click="handleReset">重置</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" class="action-card">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新建团队</el-button>
          <el-button type="danger" icon="el-icon-delete" @click="handleBatchDelete" :disabled="multipleSelection.length === 0">批量删除</el-button>
          <el-button type="warning" icon="el-icon-download" @click="handleExport">导出数据</el-button>
        </el-col>
      </el-row>
    </el-card>
    <el-card shadow="never" class="table-card">
      <el-table :data="teamList" v-loading="loading" @selection-change="handleSelectionChange" stripe border>
        <el-table-column type="selection" width="55" />
        <el-table-column prop="teamNo" label="团队编号" width="100" />
        <el-table-column prop="teamName" label="团队名称" width="140" />
        <el-table-column prop="researchDirection" label="研究方向" width="90"><template slot-scope="scope"><el-tag :type="getDirectionColor(scope.row.researchDirection)">{{ scope.row.researchDirection }}</el-tag></template></el-table-column>
        <el-table-column prop="leader" label="负责人" width="80" />
        <el-table-column prop="memberCount" label="团队规模" width="80"><template slot-scope="scope"><span>{{ scope.row.memberCount || 0 }}人</span></template></el-table-column>
        <el-table-column prop="seniorCount" label="高级人员" width="80"><template slot-scope="scope"><span class="senior-count">{{ scope.row.seniorCount || 0 }}人</span></template></el-table-column>
        <el-table-column prop="averageAge" label="平均年龄" width="80"><template slot-scope="scope"><span>{{ scope.row.averageAge || 0 }}岁</span></template></el-table-column>
        <el-table-column prop="establishDate" label="成立日期" width="110" />
        <el-table-column prop="currentProjects" label="在研项目" width="80"><template slot-scope="scope"><span class="project-count">{{ scope.row.currentProjects || 0 }}个</span></template></el-table-column>
        <el-table-column prop="completedProjects" label="完成项目" width="80"><template slot-scope="scope"><span class="completed-count">{{ scope.row.completedProjects || 0 }}个</span></template></el-table-column>
        <el-table-column prop="teamEfficiency" label="团队效率" width="100"><template slot-scope="scope"><el-progress :percentage="scope.row.teamEfficiency || 0" :color="getEfficiencyColor(scope.row.teamEfficiency || 0)" /></template></el-table-column>
        <el-table-column prop="innovationScore" label="创新指数" width="100"><template slot-scope="scope"><el-rate :value="scope.row.innovationScore || 0" disabled show-score /></template></el-table-column>
        <el-table-column prop="status" label="团队状态" width="80"><template slot-scope="scope"><el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag></template></el-table-column>
        <el-table-column prop="budget" label="年度预算(万)" width="110"><template slot-scope="scope"><span class="budget-amount">{{ scope.row.budget || 0 }}</span></template></el-table-column>
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
          <el-col :span="12"><el-form-item label="团队编号" prop="teamNo"><el-input v-model="editForm.teamNo" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="团队名称" prop="teamName"><el-input v-model="editForm.teamName" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="研究方向" prop="researchDirection"><el-select v-model="editForm.researchDirection" style="width:100%"><el-option label="人工智能" value="人工智能" /><el-option label="大数据" value="大数据" /><el-option label="云计算" value="云计算" /><el-option label="物联网" value="物联网" /><el-option label="区块链" value="区块链" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="负责人"><el-input v-model="editForm.leader" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="团队规模"><el-input-number v-model="editForm.memberCount" :min="1" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="高级人员"><el-input-number v-model="editForm.seniorCount" :min="0" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="平均年龄"><el-input-number v-model="editForm.averageAge" :min="20" :max="60" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="成立日期"><el-date-picker v-model="editForm.establishDate" type="date" value-format="yyyy-MM-dd" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="在研项目"><el-input-number v-model="editForm.currentProjects" :min="0" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="完成项目"><el-input-number v-model="editForm.completedProjects" :min="0" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="团队效率(%)"><el-slider v-model="editForm.teamEfficiency" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="创新指数"><el-rate v-model="editForm.innovationScore" show-score style="padding-top:6px" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="团队状态"><el-select v-model="editForm.status" style="width:100%"><el-option label="活跃" value="活跃" /><el-option label="项目中" value="项目中" /><el-option label="休整期" value="休整期" /><el-option label="已解散" value="已解散" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="年度预算(万)"><el-input-number v-model="editForm.budget" :precision="2" :min="0" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="描述"><el-input v-model="editForm.description" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="editDialogVisible = false">取 消</el-button><el-button type="primary" @click="handleSubmit" :loading="submitLoading">确 定</el-button></div>
    </el-dialog>

    <el-dialog title="团队详情" :visible.sync="detailDialogVisible" width="700px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="团队编号">{{ detailData.teamNo }}</el-descriptions-item>
        <el-descriptions-item label="团队名称">{{ detailData.teamName }}</el-descriptions-item>
        <el-descriptions-item label="研究方向">{{ detailData.researchDirection }}</el-descriptions-item>
        <el-descriptions-item label="负责人">{{ detailData.leader }}</el-descriptions-item>
        <el-descriptions-item label="团队规模">{{ detailData.memberCount }}人</el-descriptions-item>
        <el-descriptions-item label="高级人员">{{ detailData.seniorCount }}人</el-descriptions-item>
        <el-descriptions-item label="平均年龄">{{ detailData.averageAge }}岁</el-descriptions-item>
        <el-descriptions-item label="成立日期">{{ detailData.establishDate }}</el-descriptions-item>
        <el-descriptions-item label="在研项目">{{ detailData.currentProjects }}个</el-descriptions-item>
        <el-descriptions-item label="完成项目">{{ detailData.completedProjects }}个</el-descriptions-item>
        <el-descriptions-item label="团队效率">{{ detailData.teamEfficiency }}%</el-descriptions-item>
        <el-descriptions-item label="创新指数"><el-rate :value="detailData.innovationScore || 0" disabled show-score /></el-descriptions-item>
        <el-descriptions-item label="团队状态">{{ detailData.status }}</el-descriptions-item>
        <el-descriptions-item label="年度预算">{{ detailData.budget }}万元</el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ detailData.description }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { teamApi } from '@/api/enterprise/innovation'
export default {
  name: 'InnovationTeamManagement',
  data() {
    return {
      loading: false, submitLoading: false,
      queryForm: { teamNo: '', teamName: '', researchDirection: '', status: '', leader: '' },
      teamList: [], multipleSelection: [],
      pagination: { currentPage: 1, pageSize: 15, total: 0 },
      editDialogVisible: false, detailDialogVisible: false, isEdit: false,
      editForm: { teamNo: '', teamName: '', researchDirection: '', leader: '', memberCount: 5, seniorCount: 2, averageAge: 30, establishDate: '', currentProjects: 0, completedProjects: 0, teamEfficiency: 80, innovationScore: 3, status: '活跃', budget: 0, description: '' },
      editRules: { teamNo: [{ required: true, message: '请输入团队编号', trigger: 'blur' }], teamName: [{ required: true, message: '请输入团队名称', trigger: 'blur' }] },
      detailData: {}
    }
  },
  computed: { dialogTitle() { return this.isEdit ? '编辑团队' : '新建团队' } },
  mounted() { this.loadTeamList() },
  methods: {
    async loadTeamList() {
      this.loading = true; try { const res = await teamApi.getList({ ...this.queryForm, pageNumber: this.pagination.currentPage, pageSize: this.pagination.pageSize }); if (res && res.data) { this.teamList = res.data.tlist || []; this.pagination.total = res.data.totalRecord || 0 } else { this.teamList = []; this.pagination.total = 0 } } catch (e) { this.teamList = []; this.pagination.total = 0 } finally { this.loading = false }
    },
    handleSearch() { this.pagination.currentPage = 1; this.loadTeamList() },
    handleReset() { this.$refs.queryForm.resetFields(); this.loadTeamList() },
    handleAdd() { this.isEdit = false; this.editForm = { teamNo: '', teamName: '', researchDirection: '', leader: '', memberCount: 5, seniorCount: 2, averageAge: 30, establishDate: '', currentProjects: 0, completedProjects: 0, teamEfficiency: 80, innovationScore: 3, status: '活跃', budget: 0, description: '' }; this.editDialogVisible = true; this.$nextTick(() => { if (this.$refs.editForm) this.$refs.editForm.clearValidate() }) },
    handleEdit(row) { this.isEdit = true; this.editForm = { ...row }; this.editDialogVisible = true },
    handleView(row) { this.detailData = row; this.detailDialogVisible = true },
    async handleDelete(row) { try { await this.$confirm('确定删除吗？', '提示', { type: 'warning' }); const res = await teamApi.delete(row.id); if (res && res.result === 200) { this.$message.success('删除成功'); this.loadTeamList() } } catch (e) { if (e !== 'cancel') console.error(e) } },
    async handleBatchDelete() { if (!this.multipleSelection.length) return; try { await this.$confirm(`确定删除选中的${this.multipleSelection.length}条记录吗？`, '提示', { type: 'warning' }); const ids = this.multipleSelection.map(i => i.id); const res = await teamApi.batchDelete({ ids }); if (res && res.result === 200) { this.$message.success('批量删除成功'); this.loadTeamList() } } catch (e) { if (e !== 'cancel') console.error(e) } },
    async handleExport() { try { const res = await teamApi.export(); const blob = new Blob([res.data], { type: 'application/vnd.ms-excel' }); const link = document.createElement('a'); link.href = URL.createObjectURL(blob); link.download = '创新团队数据.xlsx'; link.click(); URL.revokeObjectURL(link.href); this.$message.success('导出成功') } catch (e) { this.$message.error('导出失败') } },
    async handleSubmit() { this.$refs.editForm.validate(async (valid) => { if (!valid) return; this.submitLoading = true; try { const res = this.isEdit ? await teamApi.update(this.editForm.id, this.editForm) : await teamApi.add(this.editForm); if (res && res.result === 200) { this.$message.success(this.isEdit ? '更新成功' : '新增成功'); this.editDialogVisible = false; this.loadTeamList() } } catch (e) { this.$message.error('操作失败') } finally { this.submitLoading = false } }) },
    handleSelectionChange(s) { this.multipleSelection = s },
    handleSizeChange(v) { this.pagination.pageSize = v; this.loadTeamList() },
    handleCurrentChange(v) { this.pagination.currentPage = v; this.loadTeamList() },
    getDirectionColor(d) { return { '人工智能': 'danger', '大数据': 'primary', '云计算': 'success', '物联网': 'warning', '区块链': 'info' }[d] || 'info' },
    getStatusType(s) { return { '活跃': 'success', '项目中': 'primary', '休整期': 'warning', '已解散': 'info' }[s] || 'info' },
    getEfficiencyColor(e) { if (e >= 90) return '#67c23a'; if (e >= 70) return '#e6a23c'; return '#f56c6c' }
  }
}
</script>
<style lang="scss" scoped>
.innovation-team-management { .search-card, .action-card, .table-card { margin-bottom: 16px; } .senior-count { color: #E6A23C; font-weight: 500; } .project-count { color: #409EFF; font-weight: 500; } .completed-count { color: #67C23A; font-weight: 500; } .budget-amount { color: #E6A23C; font-weight: 500; } .pagination-container { margin-top: 20px; text-align: right; } }
</style>
