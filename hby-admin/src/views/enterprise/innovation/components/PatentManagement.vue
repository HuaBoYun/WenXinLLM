<template>
  <div class="patent-management">
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="专利号" prop="patentNo"><el-input v-model="queryForm.patentNo" placeholder="请输入专利号" clearable /></el-form-item>
        <el-form-item label="专利名称" prop="patentName"><el-input v-model="queryForm.patentName" placeholder="请输入专利名称" clearable /></el-form-item>
        <el-form-item label="专利类型" prop="patentType">
          <el-select v-model="queryForm.patentType" placeholder="请选择" clearable>
            <el-option label="发明专利" value="发明专利" /><el-option label="实用新型" value="实用新型" /><el-option label="外观设计" value="外观设计" /><el-option label="软件著作权" value="软件著作权" />
          </el-select>
        </el-form-item>
        <el-form-item label="申请状态" prop="status">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable>
            <el-option label="申请中" value="申请中" /><el-option label="实质审查" value="实质审查" /><el-option label="已授权" value="已授权" /><el-option label="已驳回" value="已驳回" /><el-option label="已失效" value="已失效" />
          </el-select>
        </el-form-item>
        <el-form-item label="发明人" prop="inventor"><el-input v-model="queryForm.inventor" placeholder="请输入发明人" clearable /></el-form-item>
        <el-form-item><el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button><el-button icon="el-icon-refresh" @click="handleReset">重置</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" class="action-card">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增专利</el-button>
          <el-button type="danger" icon="el-icon-delete" @click="handleBatchDelete" :disabled="multipleSelection.length === 0">批量删除</el-button>
          <el-button type="warning" icon="el-icon-download" @click="handleExport">导出数据</el-button>
        </el-col>
      </el-row>
    </el-card>
    <el-card shadow="never" class="table-card">
      <el-table :data="patentList" v-loading="loading" @selection-change="handleSelectionChange" stripe border>
        <el-table-column type="selection" width="55" />
        <el-table-column prop="patentNo" label="专利号" width="150" />
        <el-table-column prop="patentName" label="专利名称" width="200" show-overflow-tooltip />
        <el-table-column prop="patentType" label="专利类型" width="100"><template slot-scope="scope"><el-tag :type="getTypeColor(scope.row.patentType)">{{ scope.row.patentType }}</el-tag></template></el-table-column>
        <el-table-column prop="inventor" label="发明人" width="120" />
        <el-table-column prop="applicant" label="申请人" width="150" />
        <el-table-column prop="applicationDate" label="申请日期" width="110" />
        <el-table-column prop="authorizationDate" label="授权日期" width="110"><template slot-scope="scope"><span v-if="scope.row.authorizationDate">{{ scope.row.authorizationDate }}</span><span v-else class="text-muted">-</span></template></el-table-column>
        <el-table-column prop="validityPeriod" label="有效期" width="80"><template slot-scope="scope"><span v-if="scope.row.validityPeriod">{{ scope.row.validityPeriod }}年</span><span v-else>-</span></template></el-table-column>
        <el-table-column prop="status" label="申请状态" width="100"><template slot-scope="scope"><el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag></template></el-table-column>
        <el-table-column prop="maintenanceFee" label="维护费用" width="100"><template slot-scope="scope"><span v-if="scope.row.maintenanceFee">{{ scope.row.maintenanceFee }}元</span><span v-else>-</span></template></el-table-column>
        <el-table-column prop="commercialValue" label="商业价值" width="100"><template slot-scope="scope"><el-rate :value="scope.row.commercialValue || 0" disabled show-score /></template></el-table-column>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="editDialogVisible" width="700px" :close-on-click-modal="false">
      <el-form :model="editForm" ref="editForm" label-width="110px" :rules="editRules">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="专利号" prop="patentNo"><el-input v-model="editForm.patentNo" placeholder="请输入专利号" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="专利名称" prop="patentName"><el-input v-model="editForm.patentName" placeholder="请输入专利名称" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="专利类型" prop="patentType"><el-select v-model="editForm.patentType" placeholder="请选择" style="width:100%"><el-option label="发明专利" value="发明专利" /><el-option label="实用新型" value="实用新型" /><el-option label="外观设计" value="外观设计" /><el-option label="软件著作权" value="软件著作权" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="申请状态" prop="status"><el-select v-model="editForm.status" placeholder="请选择" style="width:100%"><el-option label="申请中" value="申请中" /><el-option label="实质审查" value="实质审查" /><el-option label="已授权" value="已授权" /><el-option label="已驳回" value="已驳回" /><el-option label="已失效" value="已失效" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="发明人"><el-input v-model="editForm.inventor" placeholder="多人用分号分隔" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="申请人"><el-input v-model="editForm.applicant" placeholder="请输入申请人" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="申请日期"><el-date-picker v-model="editForm.applicationDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="公开日期"><el-date-picker v-model="editForm.publicationDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="授权日期"><el-date-picker v-model="editForm.authorizationDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="有效期(年)"><el-input-number v-model="editForm.validityPeriod" :min="1" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="维护费用(元)"><el-input-number v-model="editForm.maintenanceFee" :precision="2" :min="0" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="商业价值"><el-rate v-model="editForm.commercialValue" show-score style="padding-top:6px" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="描述"><el-input v-model="editForm.description" type="textarea" :rows="3" placeholder="请输入描述" /></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="editDialogVisible = false">取 消</el-button><el-button type="primary" @click="handleSubmit" :loading="submitLoading">确 定</el-button></div>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog title="专利详情" :visible.sync="detailDialogVisible" width="700px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="专利号">{{ detailData.patentNo }}</el-descriptions-item>
        <el-descriptions-item label="专利名称">{{ detailData.patentName }}</el-descriptions-item>
        <el-descriptions-item label="专利类型">{{ detailData.patentType }}</el-descriptions-item>
        <el-descriptions-item label="发明人">{{ detailData.inventor }}</el-descriptions-item>
        <el-descriptions-item label="申请人">{{ detailData.applicant }}</el-descriptions-item>
        <el-descriptions-item label="申请状态">{{ detailData.status }}</el-descriptions-item>
        <el-descriptions-item label="申请日期">{{ detailData.applicationDate }}</el-descriptions-item>
        <el-descriptions-item label="公开日期">{{ detailData.publicationDate }}</el-descriptions-item>
        <el-descriptions-item label="授权日期">{{ detailData.authorizationDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="有效期">{{ detailData.validityPeriod ? detailData.validityPeriod + '年' : '-' }}</el-descriptions-item>
        <el-descriptions-item label="维护费用">{{ detailData.maintenanceFee ? detailData.maintenanceFee + '元' : '-' }}</el-descriptions-item>
        <el-descriptions-item label="商业价值"><el-rate :value="detailData.commercialValue || 0" disabled show-score /></el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ detailData.description }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { patentApi } from '@/api/enterprise/innovation'

export default {
  name: 'PatentManagement',
  data() {
    return {
      loading: false, submitLoading: false,
      queryForm: { patentNo: '', patentName: '', patentType: '', status: '', inventor: '' },
      patentList: [], multipleSelection: [],
      pagination: { currentPage: 1, pageSize: 15, total: 0 },
      editDialogVisible: false, detailDialogVisible: false, isEdit: false,
      editForm: { patentNo: '', patentName: '', patentType: '', inventor: '', applicant: '', applicationDate: '', publicationDate: '', authorizationDate: '', validityPeriod: 20, status: '申请中', maintenanceFee: 0, commercialValue: 3, description: '' },
      editRules: { patentNo: [{ required: true, message: '请输入专利号', trigger: 'blur' }], patentName: [{ required: true, message: '请输入专利名称', trigger: 'blur' }], patentType: [{ required: true, message: '请选择专利类型', trigger: 'change' }] },
      detailData: {}
    }
  },
  computed: { dialogTitle() { return this.isEdit ? '编辑专利' : '新增专利' } },
  mounted() { this.loadPatentList() },
  methods: {
    async loadPatentList() {
      this.loading = true
      try { const res = await patentApi.getList({ ...this.queryForm, pageNumber: this.pagination.currentPage, pageSize: this.pagination.pageSize }); if (res && res.data) { this.patentList = res.data.tlist || []; this.pagination.total = res.data.totalRecord || 0 } else { this.patentList = []; this.pagination.total = 0 } } catch (e) { this.patentList = []; this.pagination.total = 0 } finally { this.loading = false }
    },
    handleSearch() { this.pagination.currentPage = 1; this.loadPatentList() },
    handleReset() { this.$refs.queryForm.resetFields(); this.loadPatentList() },
    handleAdd() { this.isEdit = false; this.editForm = { patentNo: '', patentName: '', patentType: '', inventor: '', applicant: '', applicationDate: '', publicationDate: '', authorizationDate: '', validityPeriod: 20, status: '申请中', maintenanceFee: 0, commercialValue: 3, description: '' }; this.editDialogVisible = true; this.$nextTick(() => { if (this.$refs.editForm) this.$refs.editForm.clearValidate() }) },
    handleEdit(row) { this.isEdit = true; this.editForm = { ...row }; this.editDialogVisible = true },
    handleView(row) { this.detailData = row; this.detailDialogVisible = true },
    async handleDelete(row) { try { await this.$confirm('确定删除该专利吗？', '提示', { type: 'warning' }); const res = await patentApi.delete(row.id); if (res && res.result === 200) { this.$message.success('删除成功'); this.loadPatentList() } else { this.$message.error(res.msg || '删除失败') } } catch (e) { if (e !== 'cancel') console.error(e) } },
    async handleBatchDelete() { if (!this.multipleSelection.length) { this.$message.warning('请选择要删除的专利'); return }; try { await this.$confirm(`确定删除选中的${this.multipleSelection.length}条专利吗？`, '提示', { type: 'warning' }); const ids = this.multipleSelection.map(i => i.id); const res = await patentApi.batchDelete({ ids }); if (res && res.result === 200) { this.$message.success('批量删除成功'); this.loadPatentList() } } catch (e) { if (e !== 'cancel') console.error(e) } },
    async handleExport() { try { const res = await patentApi.export(); const blob = new Blob([res.data], { type: 'application/vnd.ms-excel' }); const link = document.createElement('a'); link.href = URL.createObjectURL(blob); link.download = '知识产权数据.xlsx'; link.click(); URL.revokeObjectURL(link.href) } catch (e) { this.$message.error('导出失败') } },
    async handleSubmit() { this.$refs.editForm.validate(async (valid) => { if (!valid) return; this.submitLoading = true; try { const res = this.isEdit ? await patentApi.update(this.editForm.id, this.editForm) : await patentApi.add(this.editForm); if (res && res.result === 200) { this.$message.success(this.isEdit ? '更新成功' : '新增成功'); this.editDialogVisible = false; this.loadPatentList() } else { this.$message.error(res.msg || '操作失败') } } catch (e) { this.$message.error('操作失败') } finally { this.submitLoading = false } }) },
    handleSelectionChange(s) { this.multipleSelection = s },
    handleSizeChange(v) { this.pagination.pageSize = v; this.loadPatentList() },
    handleCurrentChange(v) { this.pagination.currentPage = v; this.loadPatentList() },
    getTypeColor(t) { return { '发明专利': 'danger', '实用新型': 'primary', '外观设计': 'success', '软件著作权': 'warning' }[t] || 'info' },
    getStatusType(s) { return { '申请中': 'info', '实质审查': 'warning', '已授权': 'success', '已驳回': 'danger', '已失效': 'info' }[s] || 'info' }
  }
}
</script>
<style lang="scss" scoped>
.patent-management { .search-card, .action-card, .table-card { margin-bottom: 16px; } .text-muted { color: #999; } .pagination-container { margin-top: 20px; text-align: right; } }
</style>
