<template>
  <div class="strategy-adjustment-management" :style="themeVars">
    <el-row :gutter="16" class="stats-row">
      <el-col :span="6"><div class="mini-stat-card"><div class="stat-value">{{ statistics.total || 0 }}</div><div class="stat-label">总调整数</div></div></el-col>
      <el-col :span="6"><div class="mini-stat-card"><div class="stat-value" style="color:#E6A23C">{{ statistics.pendingCount || 0 }}</div><div class="stat-label">待审批</div></div></el-col>
      <el-col :span="6"><div class="mini-stat-card"><div class="stat-value" style="color:#67C23A">{{ statistics.approvedCount || 0 }}</div><div class="stat-label">已批准</div></div></el-col>
      <el-col :span="6"><div class="mini-stat-card"><div class="stat-value" style="color:#F56C6C">{{ statistics.rejectedCount || 0 }}</div><div class="stat-label">已拒绝</div></div></el-col>
    </el-row>
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="调整编号" prop="adjustmentNo"><el-input v-model="queryForm.adjustmentNo" placeholder="请输入" clearable /></el-form-item>
        <el-form-item label="战略计划" prop="planName"><el-input v-model="queryForm.planName" placeholder="请输入" clearable /></el-form-item>
        <el-form-item label="调整类型" prop="adjustmentType">
          <el-select v-model="queryForm.adjustmentType" placeholder="请选择" clearable>
            <el-option label="目标调整" value="目标调整" /><el-option label="策略调整" value="策略调整" /><el-option label="资源调整" value="资源调整" /><el-option label="时间调整" value="时间调整" />
          </el-select>
        </el-form-item>
        <el-form-item label="调整状态" prop="status">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable>
            <el-option label="待审批" value="待审批" /><el-option label="审批中" value="审批中" /><el-option label="已批准" value="已批准" /><el-option label="执行中" value="执行中" /><el-option label="已完成" value="已完成" /><el-option label="已拒绝" value="已拒绝" />
          </el-select>
        </el-form-item>
        <el-form-item label="申请人" prop="applicant"><el-input v-model="queryForm.applicant" placeholder="请输入" clearable /></el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" class="action-card">
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增调整</el-button>
      <el-button type="danger" icon="el-icon-delete" :disabled="multipleSelection.length===0" @click="handleBatchDelete">批量删除</el-button>
    </el-card>
    <el-card shadow="never" class="table-card">
      <el-table :data="tableData" v-loading="loading" stripe border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" />
        <el-table-column prop="adjustmentNo" label="调整编号" width="130" />
        <el-table-column prop="planName" label="战略计划" width="180" show-overflow-tooltip />
        <el-table-column prop="adjustmentType" label="调整类型" width="100"><template slot-scope="s"><el-tag :type="getTypeColor(s.row.adjustmentType)" size="small">{{ s.row.adjustmentType }}</el-tag></template></el-table-column>
        <el-table-column prop="applicant" label="申请人" width="90" />
        <el-table-column prop="department" label="部门" width="110" />
        <el-table-column prop="adjustmentReason" label="调整原因" width="150" show-overflow-tooltip />
        <el-table-column prop="impactLevel" label="影响程度" width="90"><template slot-scope="s"><el-tag :type="getLevelType(s.row.impactLevel)" size="mini">{{ s.row.impactLevel }}</el-tag></template></el-table-column>
        <el-table-column prop="urgencyLevel" label="紧急程度" width="90"><template slot-scope="s"><el-tag :type="getLevelType(s.row.urgencyLevel)" size="mini">{{ s.row.urgencyLevel }}</el-tag></template></el-table-column>
        <el-table-column prop="budgetImpact" label="预算影响(万)" width="110"><template slot-scope="s"><span :class="getBudgetClass(s.row.budgetImpact)">{{ s.row.budgetImpact }}</span></template></el-table-column>
        <el-table-column prop="timeImpact" label="时间影响(天)" width="110"><template slot-scope="s"><span :class="getTimeClass(s.row.timeImpact)">{{ s.row.timeImpact }}</span></template></el-table-column>
        <el-table-column prop="approver" label="审批人" width="90" />
        <el-table-column prop="applicationDate" label="申请日期" width="110" />
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
    <el-dialog :title="dialogTitle" :visible.sync="editDialogVisible" width="750px" :close-on-click-modal="false">
      <el-form :model="editForm" :rules="editRules" ref="editFormRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="战略计划" prop="planName"><el-select v-model="editForm.planName" placeholder="请选择战略计划" filterable style="width:100%"><el-option v-for="item in planOptions" :key="item.id" :label="item.planName" :value="item.planName" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="调整类型"><el-select v-model="editForm.adjustmentType" style="width:100%"><el-option label="目标调整" value="目标调整" /><el-option label="策略调整" value="策略调整" /><el-option label="资源调整" value="资源调整" /><el-option label="时间调整" value="时间调整" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="申请人"><el-input v-model="editForm.applicant" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="部门"><el-input v-model="editForm.department" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="影响程度"><el-select v-model="editForm.impactLevel" style="width:100%"><el-option label="高" value="高" /><el-option label="中" value="中" /><el-option label="低" value="低" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="紧急程度"><el-select v-model="editForm.urgencyLevel" style="width:100%"><el-option label="高" value="高" /><el-option label="中" value="中" /><el-option label="低" value="低" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="预算影响"><el-input-number v-model="editForm.budgetImpact" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="时间影响"><el-input-number v-model="editForm.timeImpact" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="审批人"><el-input v-model="editForm.approver" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="申请日期"><el-date-picker v-model="editForm.applicationDate" type="date" value-format="yyyy-MM-dd" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="状态"><el-select v-model="editForm.status" style="width:100%"><el-option label="待审批" value="待审批" /><el-option label="审批中" value="审批中" /><el-option label="已批准" value="已批准" /><el-option label="执行中" value="执行中" /><el-option label="已完成" value="已完成" /><el-option label="已拒绝" value="已拒绝" /></el-select></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="调整原因"><el-input v-model="editForm.adjustmentReason" type="textarea" :rows="2" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <div slot="footer"><el-button @click="editDialogVisible=false">取消</el-button><el-button type="primary" :loading="submitLoading" @click="submitForm">确定</el-button></div>
    </el-dialog>
    <el-dialog title="调整详情" :visible.sync="detailDialogVisible" width="700px">
      <el-descriptions :column="2" border v-if="selectedItem">
        <el-descriptions-item label="调整编号">{{ selectedItem.adjustmentNo }}</el-descriptions-item>
        <el-descriptions-item label="战略计划">{{ selectedItem.planName }}</el-descriptions-item>
        <el-descriptions-item label="调整类型">{{ selectedItem.adjustmentType }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ selectedItem.status }}</el-descriptions-item>
        <el-descriptions-item label="申请人">{{ selectedItem.applicant }}</el-descriptions-item>
        <el-descriptions-item label="部门">{{ selectedItem.department }}</el-descriptions-item>
        <el-descriptions-item label="影响程度">{{ selectedItem.impactLevel }}</el-descriptions-item>
        <el-descriptions-item label="紧急程度">{{ selectedItem.urgencyLevel }}</el-descriptions-item>
        <el-descriptions-item label="预算影响">{{ selectedItem.budgetImpact }}万元</el-descriptions-item>
        <el-descriptions-item label="时间影响">{{ selectedItem.timeImpact }}天</el-descriptions-item>
        <el-descriptions-item label="调整原因" :span="2">{{ selectedItem.adjustmentReason || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { getAdjustmentList, addAdjustment, updateAdjustment, deleteAdjustment, getAdjustmentStatistics, getPlanningAll } from '@/api/enterprise/strategy'
import { investThemeMixin } from '@/views/stateAssets/themeMixin'
export default {
  name: 'StrategyAdjustmentManagement',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false, submitLoading: false,
      statistics: { total: 0, pendingCount: 0, approvedCount: 0, rejectedCount: 0 },
      queryForm: { adjustmentNo: '', planName: '', adjustmentType: '', status: '', applicant: '' },
      tableData: [], multipleSelection: [],
      pagination: { currentPage: 1, pageSize: 20, total: 0 },
      editDialogVisible: false, detailDialogVisible: false, dialogTitle: '\u65b0\u589e\u8c03\u6574',
      editForm: { id: null, planName: '', adjustmentType: '', applicant: '', department: '', adjustmentReason: '', originalTarget: '', adjustedTarget: '', impactLevel: '\u4e2d', urgencyLevel: '\u4e2d', budgetImpact: 0, timeImpact: 0, approver: '', applicationDate: '', status: '\u5f85\u5ba1\u6279' },
      editRules: { planName: [{ required: true, message: '\u8bf7\u8f93\u5165', trigger: 'blur' }] },
      selectedItem: null,
      planOptions: []
    }
  },
  mounted() { this.loadList(); this.loadStats(); this.loadPlanOptions() },
  methods: {
    async loadList() {
      this.loading = true
      try {
        const res = await getAdjustmentList({ ...this.queryForm, pageNumber: this.pagination.currentPage, pageSize: this.pagination.pageSize })
        if (res && res.result === 200 && res.data) { this.tableData = res.data.tlist || []; this.pagination.total = res.data.totalRecord || 0 }
      } catch (e) { console.error(e); this.tableData = []; this.pagination.total = 0 } finally { this.loading = false }
    },
    async loadStats() { try { const res = await getAdjustmentStatistics(); if (res && res.result === 200 && res.data) this.statistics = res.data } catch (e) { console.error(e) } },
    async loadPlanOptions() { try { const res = await getPlanningAll(); if (res && res.result === 200 && res.data) { this.planOptions = res.data } } catch (e) { console.error(e) } },
    handleSearch() { this.pagination.currentPage = 1; this.loadList() },
    handleReset() { this.$refs.queryForm.resetFields(); this.handleSearch() },
    handleSelectionChange(v) { this.multipleSelection = v },
    handleSizeChange(v) { this.pagination.pageSize = v; this.loadList() },
    handleCurrentChange(v) { this.pagination.currentPage = v; this.loadList() },
    handleAdd() { this.editForm = { id: null, planName: '', adjustmentType: '', applicant: '', department: '', adjustmentReason: '', originalTarget: '', adjustedTarget: '', impactLevel: '\u4e2d', urgencyLevel: '\u4e2d', budgetImpact: 0, timeImpact: 0, approver: '', applicationDate: '', status: '\u5f85\u5ba1\u6279' }; this.dialogTitle = '\u65b0\u589e\u8c03\u6574'; this.editDialogVisible = true },
    handleEdit(row) { this.editForm = { ...row }; this.dialogTitle = '\u7f16\u8f91\u8c03\u6574'; this.editDialogVisible = true },
    handleView(row) { this.selectedItem = row; this.detailDialogVisible = true },
    handleDelete(row) { this.$confirm('\u786e\u8ba4\u5220\u9664?', '\u63d0\u793a', { type: 'warning' }).then(async () => { try { await deleteAdjustment(row.id); this.$message.success('\u5220\u9664\u6210\u529f'); this.loadList(); this.loadStats() } catch (e) { this.$message.error('\u5220\u9664\u5931\u8d25') } }).catch(() => {}) },
    handleBatchDelete() { this.$confirm('\u786e\u8ba4\u6279\u91cf\u5220\u9664?', '\u63d0\u793a', { type: 'warning' }).then(async () => { try { for (const item of this.multipleSelection) { await deleteAdjustment(item.id) } this.$message.success('\u5220\u9664\u6210\u529f'); this.loadList(); this.loadStats() } catch (e) { this.$message.error('\u5220\u9664\u5931\u8d25') } }).catch(() => {}) },
    submitForm() { this.$refs.editFormRef.validate(async (valid) => { if (!valid) return; this.submitLoading = true; try { if (this.editForm.id) { await updateAdjustment(this.editForm); this.$message.success('\u66f4\u65b0\u6210\u529f') } else { await addAdjustment(this.editForm); this.$message.success('\u65b0\u589e\u6210\u529f') }; this.editDialogVisible = false; this.loadList(); this.loadStats() } catch (e) { this.$message.error('\u64cd\u4f5c\u5931\u8d25') } finally { this.submitLoading = false } }) },
    getTypeColor(t) { return { '\u76ee\u6807\u8c03\u6574': 'primary', '\u7b56\u7565\u8c03\u6574': 'success', '\u8d44\u6e90\u8c03\u6574': 'warning', '\u65f6\u95f4\u8c03\u6574': 'info' }[t] || 'info' },
    getLevelType(l) { return { '\u4f4e': 'success', '\u4e2d': 'warning', '\u9ad8': 'danger' }[l] || 'info' },
    getBudgetClass(v) { return parseFloat(v) > 0 ? 'budget-increase' : parseFloat(v) < 0 ? 'budget-decrease' : 'budget-neutral' },
    getTimeClass(v) { return v > 0 ? 'time-delay' : v < 0 ? 'time-advance' : 'time-neutral' },
    getStatusType(s) { return { '\u5f85\u5ba1\u6279': 'info', '\u5ba1\u6279\u4e2d': 'warning', '\u5df2\u6279\u51c6': 'primary', '\u6267\u884c\u4e2d': 'primary', '\u5df2\u5b8c\u6210': 'success', '\u5df2\u62d2\u7edd': 'danger' }[s] || 'info' }
  }
}
</script>

<style lang="scss" scoped>
.strategy-adjustment-management {
  .stats-row { margin-bottom: 16px; }
  .mini-stat-card { background: white; border-radius: 8px; padding: 16px; box-shadow: 0 2px 8px rgba(0,0,0,0.08); text-align: center;
    .stat-value { font-size: 24px; font-weight: 600; color: #303133; }
    .stat-label { font-size: 13px; color: #909399; margin-top: 4px; }
  }
  .search-card, .action-card, .table-card { margin-bottom: 16px; }
  .budget-increase { color: #F56C6C; font-weight: 500; }
  .budget-decrease { color: #67C23A; font-weight: 500; }
  .budget-neutral { color: #909399; }
  .time-delay { color: #F56C6C; font-weight: 500; }
  .time-advance { color: #67C23A; font-weight: 500; }
  .time-neutral { color: #909399; }
  .pagination-container { margin-top: 20px; text-align: right; }
}
</style>
