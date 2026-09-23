<template>
  <div class="achievement-transformation">
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="成果编号" prop="achievementNo"><el-input v-model="queryForm.achievementNo" placeholder="请输入" clearable /></el-form-item>
        <el-form-item label="成果名称" prop="achievementName"><el-input v-model="queryForm.achievementName" placeholder="请输入" clearable /></el-form-item>
        <el-form-item label="成果类型" prop="achievementType">
          <el-select v-model="queryForm.achievementType" placeholder="请选择" clearable>
            <el-option label="技术成果" value="技术成果" /><el-option label="产品成果" value="产品成果" /><el-option label="工艺成果" value="工艺成果" /><el-option label="软件成果" value="软件成果" />
          </el-select>
        </el-form-item>
        <el-form-item label="转化状态" prop="status">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable>
            <el-option label="待评估" value="待评估" /><el-option label="评估中" value="评估中" /><el-option label="转化中" value="转化中" /><el-option label="已转化" value="已转化" /><el-option label="暂停转化" value="暂停转化" />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人" prop="manager"><el-input v-model="queryForm.manager" placeholder="请输入" clearable /></el-form-item>
        <el-form-item><el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button><el-button icon="el-icon-refresh" @click="handleReset">重置</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" class="action-card">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增成果</el-button>
          <el-button type="danger" icon="el-icon-delete" @click="handleBatchDelete" :disabled="multipleSelection.length === 0">批量删除</el-button>
          <el-button type="warning" icon="el-icon-download" @click="handleExport">导出数据</el-button>
        </el-col>
      </el-row>
    </el-card>
    <el-card shadow="never" class="table-card">
      <el-table :data="achievementList" v-loading="loading" @selection-change="handleSelectionChange" stripe border>
        <el-table-column type="selection" width="55" />
        <el-table-column prop="achievementNo" label="成果编号" width="120" />
        <el-table-column prop="achievementName" label="成果名称" width="200" show-overflow-tooltip />
        <el-table-column prop="achievementType" label="成果类型" width="100"><template slot-scope="scope"><el-tag :type="getTypeColor(scope.row.achievementType)">{{ scope.row.achievementType }}</el-tag></template></el-table-column>
        <el-table-column prop="researchTeam" label="研发团队" width="120" />
        <el-table-column prop="manager" label="转化负责人" width="90" />
        <el-table-column prop="technologyLevel" label="技术水平" width="100"><template slot-scope="scope"><el-rate :value="scope.row.technologyLevel || 0" disabled show-score /></template></el-table-column>
        <el-table-column prop="marketPotential" label="市场潜力" width="80"><template slot-scope="scope"><el-tag :type="getPotentialType(scope.row.marketPotential)" size="mini">{{ scope.row.marketPotential }}</el-tag></template></el-table-column>
        <el-table-column prop="investmentAmount" label="转化投资(万)" width="110"><template slot-scope="scope"><span class="investment-amount">{{ scope.row.investmentAmount || 0 }}</span></template></el-table-column>
        <el-table-column prop="expectedRevenue" label="预期收益(万)" width="110"><template slot-scope="scope"><span class="expected-revenue">{{ scope.row.expectedRevenue || 0 }}</span></template></el-table-column>
        <el-table-column prop="transformationProgress" label="转化进度" width="120"><template slot-scope="scope"><el-progress :percentage="scope.row.transformationProgress || 0" :color="getProgressColor(scope.row.transformationProgress || 0)" /></template></el-table-column>
        <el-table-column prop="status" label="转化状态" width="90"><template slot-scope="scope"><el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag></template></el-table-column>
        <el-table-column prop="roi" label="投资回报率" width="100"><template slot-scope="scope"><span v-if="scope.row.roi" :class="getRoiClass(scope.row.roi)">{{ scope.row.roi }}%</span><span v-else class="text-muted">-</span></template></el-table-column>
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
          <el-col :span="12"><el-form-item label="成果编号" prop="achievementNo"><el-input v-model="editForm.achievementNo" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="成果名称" prop="achievementName"><el-input v-model="editForm.achievementName" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="成果类型" prop="achievementType"><el-select v-model="editForm.achievementType" style="width:100%"><el-option label="技术成果" value="技术成果" /><el-option label="产品成果" value="产品成果" /><el-option label="工艺成果" value="工艺成果" /><el-option label="软件成果" value="软件成果" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="研发团队"><el-input v-model="editForm.researchTeam" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="转化负责人"><el-input v-model="editForm.manager" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="市场潜力"><el-select v-model="editForm.marketPotential" style="width:100%"><el-option label="高" value="高" /><el-option label="中" value="中" /><el-option label="低" value="低" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="技术水平"><el-rate v-model="editForm.technologyLevel" show-score style="padding-top:6px" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="转化状态"><el-select v-model="editForm.status" style="width:100%"><el-option label="待评估" value="待评估" /><el-option label="评估中" value="评估中" /><el-option label="转化中" value="转化中" /><el-option label="已转化" value="已转化" /><el-option label="暂停转化" value="暂停转化" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="转化投资(万)"><el-input-number v-model="editForm.investmentAmount" :precision="2" :min="0" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="预期收益(万)"><el-input-number v-model="editForm.expectedRevenue" :precision="2" :min="0" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="转化进度(%)"><el-slider v-model="editForm.transformationProgress" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="投资回报率(%)"><el-input-number v-model="editForm.roi" :precision="2" :min="0" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="完成日期"><el-date-picker v-model="editForm.completionDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="editForm.description" type="textarea" :rows="3" /></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="editDialogVisible = false">取 消</el-button><el-button type="primary" @click="handleSubmit" :loading="submitLoading">确 定</el-button></div>
    </el-dialog>

    <el-dialog title="成果详情" :visible.sync="detailDialogVisible" width="700px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="成果编号">{{ detailData.achievementNo }}</el-descriptions-item>
        <el-descriptions-item label="成果名称">{{ detailData.achievementName }}</el-descriptions-item>
        <el-descriptions-item label="成果类型">{{ detailData.achievementType }}</el-descriptions-item>
        <el-descriptions-item label="研发团队">{{ detailData.researchTeam }}</el-descriptions-item>
        <el-descriptions-item label="转化负责人">{{ detailData.manager }}</el-descriptions-item>
        <el-descriptions-item label="市场潜力">{{ detailData.marketPotential }}</el-descriptions-item>
        <el-descriptions-item label="技术水平"><el-rate :value="detailData.technologyLevel || 0" disabled show-score /></el-descriptions-item>
        <el-descriptions-item label="转化状态">{{ detailData.status }}</el-descriptions-item>
        <el-descriptions-item label="转化投资">{{ detailData.investmentAmount }}万元</el-descriptions-item>
        <el-descriptions-item label="预期收益">{{ detailData.expectedRevenue }}万元</el-descriptions-item>
        <el-descriptions-item label="转化进度">{{ detailData.transformationProgress }}%</el-descriptions-item>
        <el-descriptions-item label="投资回报率">{{ detailData.roi ? detailData.roi + '%' : '-' }}</el-descriptions-item>
        <el-descriptions-item label="完成日期">{{ detailData.completionDate || '-' }}</el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">{{ detailData.description }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { achievementApi } from '@/api/enterprise/innovation'
export default {
  name: 'AchievementTransformation',
  data() {
    return {
      loading: false, submitLoading: false,
      queryForm: { achievementNo: '', achievementName: '', achievementType: '', status: '', manager: '' },
      achievementList: [], multipleSelection: [],
      pagination: { currentPage: 1, pageSize: 15, total: 0 },
      editDialogVisible: false, detailDialogVisible: false, isEdit: false,
      editForm: { achievementNo: '', achievementName: '', achievementType: '', researchTeam: '', manager: '', technologyLevel: 3, marketPotential: '中', investmentAmount: 0, expectedRevenue: 0, transformationProgress: 0, completionDate: '', status: '待评估', roi: null, description: '' },
      editRules: { achievementNo: [{ required: true, message: '请输入成果编号', trigger: 'blur' }], achievementName: [{ required: true, message: '请输入成果名称', trigger: 'blur' }] },
      detailData: {}
    }
  },
  computed: { dialogTitle() { return this.isEdit ? '编辑成果' : '新增成果' } },
  mounted() { this.loadAchievementList() },
  methods: {
    async loadAchievementList() {
      this.loading = true; try { const res = await achievementApi.getList({ ...this.queryForm, pageNumber: this.pagination.currentPage, pageSize: this.pagination.pageSize }); if (res && res.data) { this.achievementList = res.data.tlist || []; this.pagination.total = res.data.totalRecord || 0 } else { this.achievementList = []; this.pagination.total = 0 } } catch (e) { this.achievementList = []; this.pagination.total = 0 } finally { this.loading = false }
    },
    handleSearch() { this.pagination.currentPage = 1; this.loadAchievementList() },
    handleReset() { this.$refs.queryForm.resetFields(); this.loadAchievementList() },
    handleAdd() { this.isEdit = false; this.editForm = { achievementNo: '', achievementName: '', achievementType: '', researchTeam: '', manager: '', technologyLevel: 3, marketPotential: '中', investmentAmount: 0, expectedRevenue: 0, transformationProgress: 0, completionDate: '', status: '待评估', roi: null, description: '' }; this.editDialogVisible = true; this.$nextTick(() => { if (this.$refs.editForm) this.$refs.editForm.clearValidate() }) },
    handleEdit(row) { this.isEdit = true; this.editForm = { ...row }; this.editDialogVisible = true },
    handleView(row) { this.detailData = row; this.detailDialogVisible = true },
    async handleDelete(row) { try { await this.$confirm('确定删除吗？', '提示', { type: 'warning' }); const res = await achievementApi.delete(row.id); if (res && res.result === 200) { this.$message.success('删除成功'); this.loadAchievementList() } } catch (e) { if (e !== 'cancel') console.error(e) } },
    async handleBatchDelete() { if (!this.multipleSelection.length) return; try { await this.$confirm(`确定删除选中的${this.multipleSelection.length}条记录吗？`, '提示', { type: 'warning' }); const ids = this.multipleSelection.map(i => i.id); const res = await achievementApi.batchDelete({ ids }); if (res && res.result === 200) { this.$message.success('批量删除成功'); this.loadAchievementList() } } catch (e) { if (e !== 'cancel') console.error(e) } },
    async handleExport() { try { const res = await achievementApi.export(); const blob = new Blob([res.data], { type: 'application/vnd.ms-excel' }); const link = document.createElement('a'); link.href = URL.createObjectURL(blob); link.download = '成果转化数据.xlsx'; link.click(); URL.revokeObjectURL(link.href); this.$message.success('导出成功') } catch (e) { this.$message.error('导出失败') } },
    async handleSubmit() { this.$refs.editForm.validate(async (valid) => { if (!valid) return; this.submitLoading = true; try { const res = this.isEdit ? await achievementApi.update(this.editForm.id, this.editForm) : await achievementApi.add(this.editForm); if (res && res.result === 200) { this.$message.success(this.isEdit ? '更新成功' : '新增成功'); this.editDialogVisible = false; this.loadAchievementList() } } catch (e) { this.$message.error('操作失败') } finally { this.submitLoading = false } }) },
    handleSelectionChange(s) { this.multipleSelection = s },
    handleSizeChange(v) { this.pagination.pageSize = v; this.loadAchievementList() },
    handleCurrentChange(v) { this.pagination.currentPage = v; this.loadAchievementList() },
    getTypeColor(t) { return { '技术成果': 'primary', '产品成果': 'success', '工艺成果': 'warning', '软件成果': 'danger' }[t] || 'info' },
    getPotentialType(p) { return { '高': 'danger', '中': 'warning', '低': 'success' }[p] || 'info' },
    getStatusType(s) { return { '待评估': 'info', '评估中': 'warning', '转化中': 'primary', '已转化': 'success', '暂停转化': 'danger' }[s] || 'info' },
    getProgressColor(p) { if (p < 30) return '#f56c6c'; if (p < 70) return '#e6a23c'; return '#67c23a' },
    getRoiClass(r) { if (r >= 150) return 'roi-excellent'; if (r >= 100) return 'roi-good'; if (r >= 50) return 'roi-normal'; return 'roi-poor' }
  }
}
</script>
<style lang="scss" scoped>
.achievement-transformation { .search-card, .action-card, .table-card { margin-bottom: 16px; } .text-muted { color: #999; } .investment-amount { color: #E6A23C; font-weight: 500; } .expected-revenue { color: #67C23A; font-weight: 500; } .roi-excellent { color: #67C23A; font-weight: bold; } .roi-good { color: #409EFF; font-weight: 500; } .roi-normal { color: #E6A23C; } .roi-poor { color: #F56C6C; } .pagination-container { margin-top: 20px; text-align: right; } }
</style>
