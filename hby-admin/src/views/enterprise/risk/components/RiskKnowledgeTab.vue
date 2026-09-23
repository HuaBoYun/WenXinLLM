<template>
  <div class="risk-knowledge-tab">
    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="6"><el-card class="stat-card"><div class="stat-item"><div class="stat-icon" style="color: #409EFF"><i class="el-icon-collection"></i></div><div class="stat-info"><div class="stat-value">{{ overview.totalKnowledge || 0 }}</div><div class="stat-title">知识库总数</div></div></div></el-card></el-col>
      <el-col :span="6"><el-card class="stat-card"><div class="stat-item"><div class="stat-icon" style="color: #E6A23C"><i class="el-icon-warning"></i></div><div class="stat-info"><div class="stat-value">{{ overview.riskCases || 0 }}</div><div class="stat-title">风险案例</div></div></div></el-card></el-col>
      <el-col :span="6"><el-card class="stat-card"><div class="stat-item"><div class="stat-icon" style="color: #67C23A"><i class="el-icon-star-on"></i></div><div class="stat-info"><div class="stat-value">{{ overview.bestPractices || 0 }}</div><div class="stat-title">最佳实践</div></div></div></el-card></el-col>
      <el-col :span="6"><el-card class="stat-card"><div class="stat-item"><div class="stat-icon" style="color: #F56C6C"><i class="el-icon-plus"></i></div><div class="stat-info"><div class="stat-value">{{ overview.monthlyNew || 0 }}</div><div class="stat-title">本月新增</div></div></div></el-card></el-col>
    </el-row>
    <el-card style="margin-bottom: 20px;">
      <el-form :model="queryForm" :inline="true">
        <el-form-item label="知识类型">
          <el-select v-model="queryForm.knowledgeType" placeholder="请选择" clearable>
            <el-option label="风险案例" value="case"></el-option><el-option label="最佳实践" value="practice"></el-option>
            <el-option label="政策法规" value="policy"></el-option><el-option label="行业标准" value="standard"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="风险类别">
          <el-select v-model="queryForm.riskCategory" placeholder="请选择" clearable>
            <el-option label="财务风险" value="financial"></el-option><el-option label="经营风险" value="operational"></el-option>
            <el-option label="市场风险" value="market"></el-option><el-option label="技术风险" value="technical"></el-option>
            <el-option label="合规风险" value="compliance"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="关键词"><el-input v-model="queryForm.keyword" placeholder="请输入关键词" clearable></el-input></el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryData">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
          <el-button type="success" @click="showAddDialog">新增知识</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card>
      <div slot="header">
        <span>风险知识库</span>
        <el-button-group style="float: right;">
          <el-button size="small" @click="viewMode = 'list'" :type="viewMode === 'list' ? 'primary' : ''"><i class="el-icon-menu"></i> 列表</el-button>
          <el-button size="small" @click="viewMode = 'card'" :type="viewMode === 'card' ? 'primary' : ''"><i class="el-icon-s-grid"></i> 卡片</el-button>
        </el-button-group>
      </div>
      <!-- 列表视图 -->
      <el-table v-if="viewMode === 'list'" :data="knowledgeList" border v-loading="loading">
        <el-table-column prop="title" label="标题" width="300">
          <template slot-scope="scope"><el-link type="primary">{{ scope.row.title }}</el-link></template>
        </el-table-column>
        <el-table-column prop="knowledgeType" label="类型" width="120">
          <template slot-scope="scope"><el-tag :type="getKnowledgeTypeTag(scope.row.knowledgeType)">{{ getKnowledgeTypeText(scope.row.knowledgeType) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="riskCategory" label="风险类别" width="120">
          <template slot-scope="scope"><el-tag :type="getRiskCategoryTag(scope.row.riskCategory)">{{ getRiskCategoryText(scope.row.riskCategory) }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="author" label="作者" width="100"></el-table-column>
        <el-table-column prop="viewCount" label="浏览量" width="80" align="center"></el-table-column>
        <el-table-column prop="likeCount" label="点赞数" width="80" align="center"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="150"></el-table-column>
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="editKnowledge(scope.row)">编辑</el-button>
            <el-button type="text" size="small" style="color: #F56C6C;" @click="deleteKnowledge(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 卡片视图 -->
      <el-row v-if="viewMode === 'card'" :gutter="20" v-loading="loading">
        <el-col :span="8" v-for="item in knowledgeList" :key="item.riskKnowledgeId" style="margin-bottom: 20px;">
          <el-card :body-style="{ padding: '15px' }" shadow="hover" class="knowledge-card">
            <div style="margin-bottom: 10px;">
              <el-tag :type="getKnowledgeTypeTag(item.knowledgeType)" size="small">{{ getKnowledgeTypeText(item.knowledgeType) }}</el-tag>
              <el-tag :type="getRiskCategoryTag(item.riskCategory)" size="small" style="margin-left: 8px;">{{ getRiskCategoryText(item.riskCategory) }}</el-tag>
            </div>
            <h4 style="margin: 10px 0; color: #409EFF; font-size: 15px;">{{ item.title }}</h4>
            <p style="color: #666; font-size: 13px; line-height: 1.5; overflow: hidden; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical;">{{ item.summary }}</p>
            <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 10px;">
              <div style="color: #999; font-size: 12px;">
                <span><i class="el-icon-view"></i> {{ item.viewCount || 0 }}</span>
                <span style="margin-left: 10px;"><i class="el-icon-star-off"></i> {{ item.likeCount || 0 }}</span>
              </div>
              <div>
                <el-button type="text" size="small" @click="editKnowledge(item)">编辑</el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pagination.current" :page-sizes="[10, 20, 50, 100]" :page-size="pagination.size" layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" style="margin-top: 20px; text-align: right;"></el-pagination>
    </el-card>
    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="650px">
      <el-form :model="formData" label-width="100px">
        <el-form-item label="标题"><el-input v-model="formData.title"></el-input></el-form-item>
        <el-form-item label="知识类型">
          <el-select v-model="formData.knowledgeType" style="width:100%">
            <el-option label="风险案例" value="case"></el-option><el-option label="最佳实践" value="practice"></el-option>
            <el-option label="政策法规" value="policy"></el-option><el-option label="行业标准" value="standard"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="风险类别">
          <el-select v-model="formData.riskCategory" style="width:100%">
            <el-option label="财务风险" value="financial"></el-option><el-option label="经营风险" value="operational"></el-option>
            <el-option label="市场风险" value="market"></el-option><el-option label="技术风险" value="technical"></el-option>
            <el-option label="合规风险" value="compliance"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="作者"><el-input v-model="formData.author"></el-input></el-form-item>
        <el-form-item label="关键词"><el-input v-model="formData.keyword" placeholder="多个关键词用逗号分隔"></el-input></el-form-item>
        <el-form-item label="摘要"><el-input type="textarea" v-model="formData.summary" :rows="3"></el-input></el-form-item>
        <el-form-item label="内容"><el-input type="textarea" v-model="formData.content" :rows="5"></el-input></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="submitForm">确定</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import { getRiskKnowledgeList, addRiskKnowledge, updateRiskKnowledge, deleteRiskKnowledge, searchRiskKnowledge, analyzeRiskKnowledge, extractData } from '@/api/enterprise/risk'

export default {
  name: 'RiskKnowledgeTab',
  props: { enterpriseId: String, enterpriseName: String },
  data() {
    return {
      loading: false,
      viewMode: 'list',
      queryForm: { knowledgeType: '', riskCategory: '', keyword: '' },
      pagination: { current: 1, size: 10, total: 0 },
      overview: { totalKnowledge: 0, riskCases: 0, bestPractices: 0, monthlyNew: 0 },
      knowledgeList: [],
      dialogVisible: false,
      dialogTitle: '新增知识',
      isEdit: false,
      formData: { title: '', knowledgeType: '', riskCategory: '', author: '', keyword: '', summary: '', content: '' }
    }
  },
  watch: { enterpriseId: { handler(val) { if (val) this.loadData() }, immediate: true } },
  methods: {
    async loadData() {
      if (!this.enterpriseId) return
      this.loading = true
      try {
        let res
        if (this.queryForm.keyword) {
          res = await searchRiskKnowledge({ enterpriseId: this.enterpriseId, pageNumber: this.pagination.current, pageSize: this.pagination.size, ...this.queryForm })
        } else {
          const params = { enterpriseId: this.enterpriseId, pageNumber: this.pagination.current, pageSize: this.pagination.size, knowledgeType: this.queryForm.knowledgeType, riskCategory: this.queryForm.riskCategory }
          res = await getRiskKnowledgeList(params)
        }
        const data = extractData(res)
        this.knowledgeList = (data && (data.tlist || data.records)) || []
        this.pagination.total = (data && (data.totalRecord || data.total)) || 0
        this.loadOverview()
      } catch (e) { console.error('加载知识库数据失败:', e) }
      finally { this.loading = false }
    },
    async loadOverview() {
      try {
        const res = await analyzeRiskKnowledge({ enterpriseId: this.enterpriseId })
        this.overview = extractData(res) || {}
      } catch (e) { console.error('加载概览失败:', e) }
    },
    queryData() { this.pagination.current = 1; this.loadData() },
    resetQuery() { this.queryForm = { knowledgeType: '', riskCategory: '', keyword: '' }; this.queryData() },
    handleSizeChange(val) { this.pagination.size = val; this.loadData() },
    handleCurrentChange(val) { this.pagination.current = val; this.loadData() },
    showAddDialog() {
      this.isEdit = false; this.dialogTitle = '新增知识'
      this.formData = { title: '', knowledgeType: '', riskCategory: '', author: '', keyword: '', summary: '', content: '' }
      this.dialogVisible = true
    },
    editKnowledge(row) {
      this.isEdit = true; this.dialogTitle = '编辑知识'
      this.formData = { ...row }; this.dialogVisible = true
    },
    async submitForm() {
      try {
        const data = { ...this.formData, enterpriseId: this.enterpriseId, enterpriseName: this.enterpriseName, status: this.isEdit ? this.formData.status : 'published' }
        if (this.isEdit) { await updateRiskKnowledge(data) } else { await addRiskKnowledge(data) }
        this.$message.success(this.isEdit ? '更新成功' : '新增成功')
        this.dialogVisible = false; this.loadData(); this.$emit('refresh')
      } catch (e) { this.$message.error('操作失败：' + e.message) }
    },
    deleteKnowledge(row) {
      this.$confirm('确认删除该知识？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(async () => {
        await deleteRiskKnowledge(row.riskKnowledgeId); this.$message.success('删除成功'); this.loadData()
      })
    },
    getKnowledgeTypeTag(type) { return { case: 'warning', practice: 'success', policy: 'primary', standard: 'info' }[type] || 'info' },
    getKnowledgeTypeText(type) { return { case: '风险案例', practice: '最佳实践', policy: '政策法规', standard: '行业标准' }[type] || type },
    getRiskCategoryTag(category) { return { financial: 'danger', operational: 'warning', market: 'primary', technical: 'info', compliance: 'success' }[category] || 'info' },
    getRiskCategoryText(category) { return { financial: '财务风险', operational: '经营风险', market: '市场风险', technical: '技术风险', compliance: '合规风险' }[category] || category }
  }
}
</script>
<style scoped>
.risk-knowledge-tab { padding: 20px; }
.knowledge-card { cursor: pointer; transition: all 0.3s; }
.knowledge-card:hover { transform: translateY(-2px); }
.stat-card { height: 100px; }
.stat-item { display: flex; align-items: center; height: 60px; }
.stat-icon { font-size: 28px; margin-right: 12px; }
.stat-info { flex: 1; }
.stat-value { font-size: 22px; font-weight: bold; color: #303133; line-height: 1.2; }
.stat-title { font-size: 13px; color: #909399; margin-top: 4px; }
</style>
