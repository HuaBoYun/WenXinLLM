<template>
  <div class="market-analysis-tab">
    <el-card class="overview-card">
      <div slot="header"><span>市场分析概览</span><el-button style="float: right; padding: 3px 0" type="text" @click="refreshData">刷新</el-button></div>
      <el-row :gutter="20">
        <el-col :span="6"><div class="stat-item"><i class="el-icon-pie-chart stat-icon" style="color: #409EFF"></i><div class="stat-info"><div class="stat-value">{{ overview.marketShare || 0 }}%</div><div class="stat-label">市场份额</div></div></div></el-col>
        <el-col :span="6"><div class="stat-item"><i class="el-icon-money stat-icon" style="color: #67C23A"></i><div class="stat-info"><div class="stat-value">{{ overview.salesRevenue || 0 }}</div><div class="stat-label">销售收入(元)</div></div></div></el-col>
        <el-col :span="6"><div class="stat-item"><i class="el-icon-user stat-icon" style="color: #E6A23C"></i><div class="stat-info"><div class="stat-value">{{ overview.customerCount || 0 }}</div><div class="stat-label">竞争对手数</div></div></div></el-col>
        <el-col :span="6"><div class="stat-item"><i class="el-icon-top stat-icon" style="color: #F56C6C"></i><div class="stat-info"><div class="stat-value">{{ overview.growthRate || 0 }}%</div><div class="stat-label">增长率</div></div></div></el-col>
      </el-row>
    </el-card>
    <el-card class="table-card">
      <div slot="header"><span>市场分析报告</span><div style="float: right;"><el-button type="primary" size="small" @click="handleAdd" icon="el-icon-plus">新建分析</el-button></div></div>
      <el-form :model="queryForm" :inline="true" class="query-form">
        <el-form-item label="分析类型"><el-select v-model="queryForm.analysisType" placeholder="请选择" clearable><el-option label="市场规模分析" value="市场规模分析"></el-option><el-option label="竞争分析" value="竞争分析"></el-option><el-option label="客户分析" value="客户分析"></el-option><el-option label="产品分析" value="产品分析"></el-option></el-select></el-form-item>
        <el-form-item label="状态"><el-select v-model="queryForm.status" placeholder="请选择" clearable><el-option label="已完成" value="已完成"></el-option><el-option label="进行中" value="进行中"></el-option><el-option label="待开始" value="待开始"></el-option></el-select></el-form-item>
        <el-form-item><el-button type="primary" @click="queryData" icon="el-icon-search">查询</el-button><el-button @click="resetQuery" icon="el-icon-refresh">重置</el-button></el-form-item>
      </el-form>
      <el-table :data="analysisList" border v-loading="loading">
        <el-table-column prop="analysisName" label="分析名称" min-width="200" show-overflow-tooltip></el-table-column>
        <el-table-column prop="analysisType" label="分析类型" width="110" align="center"></el-table-column>
        <el-table-column prop="marketSize" label="市场规模(万)" width="120" align="right"></el-table-column>
        <el-table-column prop="marketShare" label="市场份额" width="80" align="right"><template slot-scope="scope">{{ scope.row.marketShare || 0 }}%</template></el-table-column>
        <el-table-column prop="growthRate" label="增长率" width="80" align="right"><template slot-scope="scope"><span :style="{color: scope.row.growthRate > 0 ? '#67C23A' : '#F56C6C'}">{{ scope.row.growthRate || 0 }}%</span></template></el-table-column>
        <el-table-column prop="competitorCount" label="竞争对手数" width="100" align="center"></el-table-column>
        <el-table-column prop="analysisDate" label="分析日期" width="110"></el-table-column>
        <el-table-column prop="analyst" label="分析师" width="80"></el-table-column>
        <el-table-column prop="status" label="状态" width="80" align="center"><template slot-scope="scope"><el-tag :type="scope.row.status === '已完成' ? 'success' : scope.row.status === '进行中' ? 'primary' : 'warning'" size="small">{{ scope.row.status }}</el-tag></template></el-table-column>
        <el-table-column label="操作" width="150" fixed="right" align="center"><template slot-scope="scope"><el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button><el-button type="text" size="small" @click="handleDelete(scope.row)" style="color: #F56C6C">删除</el-button></template></el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="queryForm.pageNumber" :page-sizes="[10, 20, 50, 100]" :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" style="margin-top: 20px; text-align: right;"></el-pagination>
    </el-card>
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="650px" :close-on-click-modal="false">
      <el-form :model="formData" label-width="100px" ref="formRef">
        <el-form-item label="分析名称" prop="analysisName" :rules="[{ required: true, message: '请输入分析名称' }]"><el-input v-model="formData.analysisName" placeholder="请输入分析名称"></el-input></el-form-item>
        <el-form-item label="分析类型" prop="analysisType"><el-select v-model="formData.analysisType" style="width: 100%;"><el-option label="市场规模分析" value="市场规模分析"></el-option><el-option label="竞争分析" value="竞争分析"></el-option><el-option label="客户分析" value="客户分析"></el-option><el-option label="产品分析" value="产品分析"></el-option></el-select></el-form-item>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="市场规模" prop="marketSize"><el-input-number v-model="formData.marketSize" :min="0" style="width: 100%;"></el-input-number></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="市场份额(%)" prop="marketShare"><el-input-number v-model="formData.marketShare" :min="0" :max="100" :precision="2" style="width: 100%;"></el-input-number></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8"><el-form-item label="增长率(%)" prop="growthRate"><el-input-number v-model="formData.growthRate" :precision="2" style="width: 100%;"></el-input-number></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="竞争对手数" prop="competitorCount"><el-input-number v-model="formData.competitorCount" :min="0" style="width: 100%;"></el-input-number></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="分析师" prop="analyst"><el-input v-model="formData.analyst" placeholder="请输入"></el-input></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="分析日期" prop="analysisDate"><el-date-picker v-model="formData.analysisDate" type="date" value-format="yyyy-MM-dd" style="width: 100%;"></el-date-picker></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="状态" prop="status"><el-select v-model="formData.status" style="width: 100%;"><el-option label="待开始" value="待开始"></el-option><el-option label="进行中" value="进行中"></el-option><el-option label="已完成" value="已完成"></el-option></el-select></el-form-item></el-col>
        </el-row>
        <el-form-item label="备注" prop="remark"><el-input v-model="formData.remark" type="textarea" :rows="2" placeholder="请输入备注"></el-input></el-form-item>
      </el-form>
      <span slot="footer"><el-button @click="dialogVisible = false">取 消</el-button><el-button type="primary" @click="handleSubmit">确 定</el-button></span>
    </el-dialog>
  </div>
</template>

<script>
import { getMarketAnalysisList, addMarketAnalysis, updateMarketAnalysis, deleteMarketAnalysis, getMarketAnalysisStatistics } from '@/api/enterprise/operation'
export default {
  name: 'MarketAnalysisTab',
  props: { enterpriseId: { type: String, default: '' }, enterpriseName: { type: String, default: '' } },
  data() {
    return { loading: false, queryForm: { analysisType: '', status: '', pageNumber: 1, pageSize: 20 }, total: 0, overview: {}, analysisList: [], dialogVisible: false, dialogTitle: '新建市场分析', isEdit: false, formData: { analysisName: '', analysisType: '', marketSize: 0, marketShare: 0, growthRate: 0, competitorCount: 0, analysisDate: '', analyst: '', status: '待开始', remark: '' } }
  },
  watch: { enterpriseId: { handler(val) { if (val) { this.queryForm.enterpriseId = val; this.loadData(); this.loadStatistics(); } }, immediate: true } },
  methods: {
    extractData(response) { if (!response) return null; const d = response.data || response; return d && d.pageInfo ? d.pageInfo : d },
    async loadData() { if (!this.enterpriseId) return; this.loading = true; try { const res = await getMarketAnalysisList(this.queryForm); const p = this.extractData(res); this.analysisList = p && p.tlist ? p.tlist : []; this.total = p && p.totalRecord ? p.totalRecord : 0; } catch (e) { console.error(e) } finally { this.loading = false } },
    async loadStatistics() { if (!this.enterpriseId) return; try { const res = await getMarketAnalysisStatistics({ enterpriseId: this.enterpriseId }); this.overview = this.extractData(res) || {}; } catch (e) { console.error(e) } },
    refreshData() { this.loadData(); this.loadStatistics(); this.$message.success('刷新成功') },
    queryData() { this.queryForm.pageNumber = 1; this.loadData() },
    resetQuery() { this.queryForm = { analysisType: '', status: '', pageNumber: 1, pageSize: 20, enterpriseId: this.enterpriseId }; this.loadData() },
    handleSizeChange(val) { this.queryForm.pageSize = val; this.queryForm.pageNumber = 1; this.loadData() },
    handleCurrentChange(val) { this.queryForm.pageNumber = val; this.loadData() },
    handleAdd() { this.isEdit = false; this.dialogTitle = '新建市场分析'; this.formData = { analysisName: '', analysisType: '', marketSize: 0, marketShare: 0, growthRate: 0, competitorCount: 0, analysisDate: '', analyst: '', status: '待开始', remark: '', enterpriseId: this.enterpriseId, enterpriseName: this.enterpriseName }; this.dialogVisible = true },
    handleEdit(row) { this.isEdit = true; this.dialogTitle = '编辑市场分析'; this.formData = { ...row }; this.dialogVisible = true },
    async handleSubmit() { this.$refs.formRef.validate(async (valid) => { if (!valid) return; try { this.formData.enterpriseId = this.enterpriseId; this.formData.enterpriseName = this.enterpriseName; if (this.isEdit) { await updateMarketAnalysis(this.formData); this.$message.success('更新成功') } else { await addMarketAnalysis(this.formData); this.$message.success('新增成功') } this.dialogVisible = false; this.loadData(); this.loadStatistics() } catch (e) { this.$message.error('操作失败') } }) },
    async handleDelete(row) { try { await this.$confirm('确认删除该市场分析？', '提示', { type: 'warning' }); await deleteMarketAnalysis(row.id); this.$message.success('删除成功'); this.loadData(); this.loadStatistics() } catch (e) { if (e !== 'cancel') this.$message.error('删除失败') } }
  }
}
</script>
<style scoped>
.market-analysis-tab { padding: 20px; }
.overview-card { margin-bottom: 20px; }
.table-card { margin-bottom: 20px; }
.query-form { margin-bottom: 20px; }
.stat-item { display: flex; align-items: center; padding: 10px 0; }
.stat-icon { font-size: 32px; margin-right: 12px; }
.stat-info { flex: 1; }
.stat-value { font-size: 22px; font-weight: bold; color: #303133; }
.stat-label { font-size: 13px; color: #909399; margin-top: 4px; }
</style>
