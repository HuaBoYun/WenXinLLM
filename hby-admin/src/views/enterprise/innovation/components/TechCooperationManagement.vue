<template>
  <div class="tech-cooperation-management">
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="合作编号" prop="cooperationNo"><el-input v-model="queryForm.cooperationNo" placeholder="请输入" clearable /></el-form-item>
        <el-form-item label="合作伙伴" prop="partner"><el-input v-model="queryForm.partner" placeholder="请输入" clearable /></el-form-item>
        <el-form-item label="合作类型" prop="cooperationType">
          <el-select v-model="queryForm.cooperationType" placeholder="请选择" clearable>
            <el-option label="技术研发" value="技术研发" /><el-option label="产学研合作" value="产学研合作" /><el-option label="技术转让" value="技术转让" /><el-option label="联合创新" value="联合创新" />
          </el-select>
        </el-form-item>
        <el-form-item label="合作状态" prop="status">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable>
            <el-option label="洽谈中" value="洽谈中" /><el-option label="已签约" value="已签约" /><el-option label="执行中" value="执行中" /><el-option label="已完成" value="已完成" /><el-option label="已终止" value="已终止" />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人" prop="manager"><el-input v-model="queryForm.manager" placeholder="请输入" clearable /></el-form-item>
        <el-form-item><el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button><el-button icon="el-icon-refresh" @click="handleReset">重置</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" class="action-card">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增合作</el-button>
          <el-button type="danger" icon="el-icon-delete" @click="handleBatchDelete" :disabled="multipleSelection.length === 0">批量删除</el-button>
          <el-button type="warning" icon="el-icon-download" @click="handleExport">导出数据</el-button>
        </el-col>
      </el-row>
    </el-card>
    <el-card shadow="never" class="table-card">
      <el-table :data="cooperationList" v-loading="loading" @selection-change="handleSelectionChange" stripe border>
        <el-table-column type="selection" width="55" />
        <el-table-column prop="cooperationNo" label="合作编号" width="120" />
        <el-table-column prop="cooperationName" label="合作项目" width="200" show-overflow-tooltip />
        <el-table-column prop="partner" label="合作伙伴" width="150" />
        <el-table-column prop="partnerType" label="伙伴类型" width="100"><template slot-scope="scope"><el-tag :type="getPartnerTypeColor(scope.row.partnerType)">{{ scope.row.partnerType }}</el-tag></template></el-table-column>
        <el-table-column prop="cooperationType" label="合作类型" width="100"><template slot-scope="scope"><el-tag :type="getTypeColor(scope.row.cooperationType)">{{ scope.row.cooperationType }}</el-tag></template></el-table-column>
        <el-table-column prop="manager" label="负责人" width="80" />
        <el-table-column prop="investmentAmount" label="投资金额(万)" width="110"><template slot-scope="scope"><span class="investment-amount">{{ scope.row.investmentAmount || 0 }}</span></template></el-table-column>
        <el-table-column prop="expectedReturn" label="预期收益(万)" width="110"><template slot-scope="scope"><span class="expected-return">{{ scope.row.expectedReturn || 0 }}</span></template></el-table-column>
        <el-table-column prop="cooperationPeriod" label="合作期限" width="80"><template slot-scope="scope"><span>{{ scope.row.cooperationPeriod || 0 }}年</span></template></el-table-column>
        <el-table-column prop="startDate" label="开始日期" width="110" />
        <el-table-column prop="endDate" label="结束日期" width="110" />
        <el-table-column prop="progress" label="执行进度" width="120"><template slot-scope="scope"><el-progress :percentage="scope.row.progress || 0" :color="getProgressColor(scope.row.progress || 0)" /></template></el-table-column>
        <el-table-column prop="status" label="合作状态" width="80"><template slot-scope="scope"><el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag></template></el-table-column>
        <el-table-column prop="riskLevel" label="风险等级" width="80"><template slot-scope="scope"><el-tag :type="getRiskLevelType(scope.row.riskLevel)" size="mini">{{ scope.row.riskLevel }}</el-tag></template></el-table-column>
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
          <el-col :span="12"><el-form-item label="合作编号" prop="cooperationNo"><el-input v-model="editForm.cooperationNo" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="合作项目" prop="cooperationName"><el-input v-model="editForm.cooperationName" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="合作伙伴" prop="partner"><el-input v-model="editForm.partner" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="伙伴类型" prop="partnerType"><el-select v-model="editForm.partnerType" style="width:100%"><el-option label="高等院校" value="高等院校" /><el-option label="科研院所" value="科研院所" /><el-option label="企业" value="企业" /><el-option label="政府机构" value="政府机构" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="合作类型" prop="cooperationType"><el-select v-model="editForm.cooperationType" style="width:100%"><el-option label="技术研发" value="技术研发" /><el-option label="产学研合作" value="产学研合作" /><el-option label="技术转让" value="技术转让" /><el-option label="联合创新" value="联合创新" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="负责人"><el-input v-model="editForm.manager" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="投资金额(万)"><el-input-number v-model="editForm.investmentAmount" :precision="2" :min="0" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="预期收益(万)"><el-input-number v-model="editForm.expectedReturn" :precision="2" :min="0" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="合作期限(年)"><el-input-number v-model="editForm.cooperationPeriod" :min="1" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="风险等级"><el-select v-model="editForm.riskLevel" style="width:100%"><el-option label="低" value="低" /><el-option label="中" value="中" /><el-option label="高" value="高" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="开始日期"><el-date-picker v-model="editForm.startDate" type="date" value-format="yyyy-MM-dd" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="结束日期"><el-date-picker v-model="editForm.endDate" type="date" value-format="yyyy-MM-dd" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="执行进度(%)"><el-slider v-model="editForm.progress" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="合作状态"><el-select v-model="editForm.status" style="width:100%"><el-option label="洽谈中" value="洽谈中" /><el-option label="已签约" value="已签约" /><el-option label="执行中" value="执行中" /><el-option label="已完成" value="已完成" /><el-option label="已终止" value="已终止" /></el-select></el-form-item></el-col>
        </el-row>
        <el-form-item label="描述"><el-input v-model="editForm.description" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="editDialogVisible = false">取 消</el-button><el-button type="primary" @click="handleSubmit" :loading="submitLoading">确 定</el-button></div>
    </el-dialog>

    <el-dialog title="合作详情" :visible.sync="detailDialogVisible" width="700px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="合作编号">{{ detailData.cooperationNo }}</el-descriptions-item>
        <el-descriptions-item label="合作项目">{{ detailData.cooperationName }}</el-descriptions-item>
        <el-descriptions-item label="合作伙伴">{{ detailData.partner }}</el-descriptions-item>
        <el-descriptions-item label="伙伴类型">{{ detailData.partnerType }}</el-descriptions-item>
        <el-descriptions-item label="合作类型">{{ detailData.cooperationType }}</el-descriptions-item>
        <el-descriptions-item label="负责人">{{ detailData.manager }}</el-descriptions-item>
        <el-descriptions-item label="投资金额">{{ detailData.investmentAmount }}万元</el-descriptions-item>
        <el-descriptions-item label="预期收益">{{ detailData.expectedReturn }}万元</el-descriptions-item>
        <el-descriptions-item label="合作期限">{{ detailData.cooperationPeriod }}年</el-descriptions-item>
        <el-descriptions-item label="风险等级">{{ detailData.riskLevel }}</el-descriptions-item>
        <el-descriptions-item label="开始日期">{{ detailData.startDate }}</el-descriptions-item>
        <el-descriptions-item label="结束日期">{{ detailData.endDate }}</el-descriptions-item>
        <el-descriptions-item label="执行进度">{{ detailData.progress }}%</el-descriptions-item>
        <el-descriptions-item label="合作状态">{{ detailData.status }}</el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ detailData.description }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { cooperationApi } from '@/api/enterprise/innovation'
export default {
  name: 'TechCooperationManagement',
  data() {
    return {
      loading: false, submitLoading: false,
      queryForm: { cooperationNo: '', partner: '', cooperationType: '', status: '', manager: '' },
      cooperationList: [], multipleSelection: [],
      pagination: { currentPage: 1, pageSize: 15, total: 0 },
      editDialogVisible: false, detailDialogVisible: false, isEdit: false,
      editForm: { cooperationNo: '', cooperationName: '', partner: '', partnerType: '企业', cooperationType: '', manager: '', investmentAmount: 0, expectedReturn: 0, cooperationPeriod: 1, startDate: '', endDate: '', progress: 0, status: '洽谈中', riskLevel: '低', description: '' },
      editRules: { cooperationNo: [{ required: true, message: '请输入合作编号', trigger: 'blur' }], cooperationName: [{ required: true, message: '请输入合作项目', trigger: 'blur' }] },
      detailData: {}
    }
  },
  computed: { dialogTitle() { return this.isEdit ? '编辑合作' : '新增合作' } },
  mounted() { this.loadCooperationList() },
  methods: {
    async loadCooperationList() {
      this.loading = true; try { const res = await cooperationApi.getList({ ...this.queryForm, pageNumber: this.pagination.currentPage, pageSize: this.pagination.pageSize }); if (res && res.data) { this.cooperationList = res.data.tlist || []; this.pagination.total = res.data.totalRecord || 0 } else { this.cooperationList = []; this.pagination.total = 0 } } catch (e) { this.cooperationList = []; this.pagination.total = 0 } finally { this.loading = false }
    },
    handleSearch() { this.pagination.currentPage = 1; this.loadCooperationList() },
    handleReset() { this.$refs.queryForm.resetFields(); this.loadCooperationList() },
    handleAdd() { this.isEdit = false; this.editForm = { cooperationNo: '', cooperationName: '', partner: '', partnerType: '企业', cooperationType: '', manager: '', investmentAmount: 0, expectedReturn: 0, cooperationPeriod: 1, startDate: '', endDate: '', progress: 0, status: '洽谈中', riskLevel: '低', description: '' }; this.editDialogVisible = true; this.$nextTick(() => { if (this.$refs.editForm) this.$refs.editForm.clearValidate() }) },
    handleEdit(row) { this.isEdit = true; this.editForm = { ...row }; this.editDialogVisible = true },
    handleView(row) { this.detailData = row; this.detailDialogVisible = true },
    async handleDelete(row) { try { await this.$confirm('确定删除吗？', '提示', { type: 'warning' }); const res = await cooperationApi.delete(row.id); if (res && res.result === 200) { this.$message.success('删除成功'); this.loadCooperationList() } } catch (e) { if (e !== 'cancel') console.error(e) } },
    async handleBatchDelete() { if (!this.multipleSelection.length) return; try { await this.$confirm(`确定删除选中的${this.multipleSelection.length}条记录吗？`, '提示', { type: 'warning' }); const ids = this.multipleSelection.map(i => i.id); const res = await cooperationApi.batchDelete({ ids }); if (res && res.result === 200) { this.$message.success('批量删除成功'); this.loadCooperationList() } } catch (e) { if (e !== 'cancel') console.error(e) } },
    async handleExport() { try { const res = await cooperationApi.export(); const blob = new Blob([res.data], { type: 'application/vnd.ms-excel' }); const link = document.createElement('a'); link.href = URL.createObjectURL(blob); link.download = '技术合作数据.xlsx'; link.click(); URL.revokeObjectURL(link.href); this.$message.success('导出成功') } catch (e) { this.$message.error('导出失败') } },
    async handleSubmit() { this.$refs.editForm.validate(async (valid) => { if (!valid) return; this.submitLoading = true; try { const res = this.isEdit ? await cooperationApi.update(this.editForm.id, this.editForm) : await cooperationApi.add(this.editForm); if (res && res.result === 200) { this.$message.success(this.isEdit ? '更新成功' : '新增成功'); this.editDialogVisible = false; this.loadCooperationList() } } catch (e) { this.$message.error('操作失败') } finally { this.submitLoading = false } }) },
    handleSelectionChange(s) { this.multipleSelection = s },
    handleSizeChange(v) { this.pagination.pageSize = v; this.loadCooperationList() },
    handleCurrentChange(v) { this.pagination.currentPage = v; this.loadCooperationList() },
    getPartnerTypeColor(t) { return { '高等院校': 'primary', '科研院所': 'success', '企业': 'warning', '政府机构': 'info' }[t] || 'info' },
    getTypeColor(t) { return { '技术研发': 'primary', '产学研合作': 'success', '技术转让': 'warning', '联合创新': 'danger' }[t] || 'info' },
    getStatusType(s) { return { '洽谈中': 'info', '已签约': 'warning', '执行中': 'primary', '已完成': 'success', '已终止': 'danger' }[s] || 'info' },
    getRiskLevelType(l) { return { '低': 'success', '中': 'warning', '高': 'danger' }[l] || 'info' },
    getProgressColor(p) { if (p < 30) return '#f56c6c'; if (p < 70) return '#e6a23c'; return '#67c23a' }
  }
}
</script>
<style lang="scss" scoped>
.tech-cooperation-management { .search-card, .action-card, .table-card { margin-bottom: 16px; } .investment-amount { color: #E6A23C; font-weight: 500; } .expected-return { color: #67C23A; font-weight: 500; } .pagination-container { margin-top: 20px; text-align: right; } }
</style>
