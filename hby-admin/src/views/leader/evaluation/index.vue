<template>
  <div class="leader-evaluation" :style="themeVars">
    <!-- 统计概览 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper"><i class="el-icon-medal"></i></div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.totalEvaluations }}</div>
              <div class="label">考核总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper"><i class="el-icon-star-on"></i></div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.avgScore }}</div>
              <div class="label">平均分数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper"><i class="el-icon-trophy"></i></div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.excellentRate }}%</div>
              <div class="label">优秀率</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card gradient-theme">
          <div class="card-content">
            <div class="icon-wrapper"><i class="el-icon-finished"></i></div>
            <div class="data-wrapper">
              <div class="number">{{ overviewData.completedCount }}</div>
              <div class="label">已完成考核</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能模块 -->
    <el-card class="function-card">
      <div slot="header" class="card-header">
        <span class="card-title">负责人考核评价</span>
        <div class="card-actions">
          <el-button type="primary" icon="el-icon-plus" @click="handleStartEvaluation">发起考核</el-button>
          <el-button type="success" icon="el-icon-refresh" @click="refreshAllData">刷新数据</el-button>
        </div>
      </div>
      <el-tabs v-model="activeTab" type="border-card" @tab-click="handleTabClick">
        <!-- Tab1: 绩效考核 -->
        <el-tab-pane label="绩效考核" name="performance">
          <el-table :data="tabData.performance" stripe border size="small" v-loading="tabLoading">
            <el-table-column prop="leaderName" label="负责人" width="100" />
            <el-table-column prop="company" label="所属企业" min-width="150" />
            <el-table-column prop="evaluationType" label="考核类型" width="100" />
            <el-table-column prop="evaluationPeriod" label="考核年度" width="90" />
            <el-table-column prop="totalScore" label="总分" width="70">
              <template slot-scope="scope">
                <el-tag :type="getScoreType(scope.row.totalScore)" size="small">{{ scope.row.totalScore }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="grade" label="等级" width="80">
              <template slot-scope="scope">
                <el-tag :type="getGradeType(scope.row.grade)" size="small">{{ scope.row.grade }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="evaluator" label="考核人" width="90" />
            <el-table-column prop="evaluationDate" label="考核时间" width="110" />
          </el-table>
        </el-tab-pane>
        <!-- Tab2: 能力评估 -->
        <el-tab-pane label="能力评估" name="ability">
          <el-table :data="tabData.ability" stripe border size="small" v-loading="tabLoading">
            <el-table-column prop="leaderName" label="负责人" width="100" />
            <el-table-column prop="company" label="所属企业" min-width="150" />
            <el-table-column prop="evaluationPeriod" label="评估年度" width="90" />
            <el-table-column prop="politicalScore" label="政治素质" width="80" />
            <el-table-column prop="economicScore" label="经营业绩" width="80" />
            <el-table-column prop="managementScore" label="管理能力" width="80" />
            <el-table-column prop="integrityScore" label="廉洁自律" width="80" />
            <el-table-column prop="totalScore" label="总分" width="70">
              <template slot-scope="scope">
                <el-tag :type="getScoreType(scope.row.totalScore)" size="small">{{ scope.row.totalScore }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="grade" label="等级" width="80">
              <template slot-scope="scope">
                <el-tag :type="getGradeType(scope.row.grade)" size="small">{{ scope.row.grade }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <!-- Tab3: 发展规划 -->
        <el-tab-pane label="发展规划" name="development">
          <el-table :data="tabData.development" stripe border size="small" v-loading="tabLoading">
            <el-table-column prop="leaderName" label="负责人" width="100" />
            <el-table-column prop="company" label="所属企业" min-width="140" />
            <el-table-column prop="trainingType" label="发展类型" width="100" />
            <el-table-column prop="programName" label="项目名称" min-width="150" />
            <el-table-column prop="institution" label="培训机构" width="130" />
            <el-table-column prop="startDate" label="开始日期" width="110" />
            <el-table-column prop="endDate" label="结束日期" width="110" />
            <el-table-column prop="status" label="状态" width="80">
              <template slot-scope="scope">
                <el-tag :type="scope.row.status === '已完成' ? 'success' : scope.row.status === '进行中' ? 'warning' : 'info'" size="small">{{ scope.row.status }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <!-- Tab4: 360度评价 -->
        <el-tab-pane label="360度评价" name="feedback">
          <el-table :data="tabData.feedback" stripe border size="small" v-loading="tabLoading">
            <el-table-column prop="leaderName" label="负责人" width="100" />
            <el-table-column prop="company" label="所属企业" min-width="150" />
            <el-table-column prop="evaluationPeriod" label="评价年度" width="90" />
            <el-table-column prop="evaluator" label="评价人" width="90" />
            <el-table-column prop="politicalScore" label="政治素质" width="80" />
            <el-table-column prop="economicScore" label="经营业绩" width="80" />
            <el-table-column prop="managementScore" label="管理能力" width="80" />
            <el-table-column prop="integrityScore" label="廉洁自律" width="80" />
            <el-table-column prop="totalScore" label="总分" width="70">
              <template slot-scope="scope">
                <el-tag :type="getScoreType(scope.row.totalScore)" size="small">{{ scope.row.totalScore }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="grade" label="等级" width="80">
              <template slot-scope="scope">
                <el-tag :type="getGradeType(scope.row.grade)" size="small">{{ scope.row.grade }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <!-- Tab5: 考核指标 -->
        <el-tab-pane label="考核指标" name="indicators">
          <el-table :data="tabData.indicators" stripe border size="small" v-loading="tabLoading">
            <el-table-column prop="indicatorName" label="指标名称" min-width="150" />
            <el-table-column prop="indicatorType" label="指标类型" width="100" />
            <el-table-column prop="weight" label="权重" width="70" />
            <el-table-column prop="maxScore" label="最高分" width="70" />
            <el-table-column prop="minScore" label="最低分" width="70" />
            <el-table-column prop="scoreStandard" label="评分标准" min-width="200" show-overflow-tooltip />
            <el-table-column prop="status" label="状态" width="80">
              <template slot-scope="scope">
                <el-tag :type="scope.row.status === '启用' ? 'success' : 'danger'" size="small">{{ scope.row.status }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <!-- Tab6: 结果分析 -->
        <el-tab-pane label="结果分析" name="analysis">
          <div v-if="analysisData" class="analysis-content">
            <el-row :gutter="16">
              <el-col :span="8">
                <el-card shadow="hover">
                  <div slot="header">等级分布</div>
                  <div class="analysis-item" v-for="(val, key) in analysisData.gradeDistribution" :key="key">
                    <span>{{ key }}</span><el-tag :type="getGradeType(key)" size="small">{{ val }}人</el-tag>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="8">
                <el-card shadow="hover">
                  <div slot="header">各维度平均分</div>
                  <div class="analysis-item"><span>政治素质</span><span>{{ analysisData.dimensionAvg && analysisData.dimensionAvg.politicalAvg ? analysisData.dimensionAvg.politicalAvg.toFixed(1) : '-' }}</span></div>
                  <div class="analysis-item"><span>经营业绩</span><span>{{ analysisData.dimensionAvg && analysisData.dimensionAvg.economicAvg ? analysisData.dimensionAvg.economicAvg.toFixed(1) : '-' }}</span></div>
                  <div class="analysis-item"><span>管理能力</span><span>{{ analysisData.dimensionAvg && analysisData.dimensionAvg.managementAvg ? analysisData.dimensionAvg.managementAvg.toFixed(1) : '-' }}</span></div>
                  <div class="analysis-item"><span>廉洁自律</span><span>{{ analysisData.dimensionAvg && analysisData.dimensionAvg.integrityAvg ? analysisData.dimensionAvg.integrityAvg.toFixed(1) : '-' }}</span></div>
                </el-card>
              </el-col>
              <el-col :span="8">
                <el-card shadow="hover">
                  <div slot="header">分数段分布</div>
                  <div class="analysis-item" v-for="(val, key) in analysisData.scoreDistribution" :key="key">
                    <span>{{ key }}分</span><span>{{ val }}人</span>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>
          <el-empty v-else description="暂无分析数据" />
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="data-card">
      <div slot="header" class="card-header">
        <span class="card-title">考核评价记录</span>
        <div class="search-wrapper">
          <el-input v-model="searchText" placeholder="搜索负责人或企业" prefix-icon="el-icon-search" style="width: 300px;" clearable @input="handleSearch" />
        </div>
      </div>
      <el-table :data="tableData" stripe border style="width: 100%" v-loading="loading">
        <el-table-column prop="leaderName" label="负责人" width="100" />
        <el-table-column prop="company" label="所属企业" min-width="150" />
        <el-table-column prop="evaluationType" label="考核类型" width="100" />
        <el-table-column prop="evaluationPeriod" label="考核周期" width="90" />
        <el-table-column prop="totalScore" label="总分" width="70">
          <template slot-scope="scope">
            <el-tag :type="getScoreType(scope.row.totalScore)" size="small">{{ scope.row.totalScore }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="grade" label="考核等级" width="80">
          <template slot-scope="scope">
            <el-tag :type="getGradeType(scope.row.grade)" size="small">{{ scope.row.grade }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="evaluator" label="考核人" width="90" />
        <el-table-column prop="evaluationDate" label="考核时间" width="110" />
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="240" fixed="right">
          <template slot-scope="scope">
            <div class="action-buttons">
              <el-button size="mini" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
              <el-button size="mini" type="primary" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button size="mini" type="warning" icon="el-icon-download" @click="handleExport(scope.row)">导出</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        style="margin-top: 15px; text-align: right;"
        :current-page="queryForm.pageNumber"
        :page-size="queryForm.pageSize"
        :total="total"
        :page-sizes="[10, 15, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 发起考核/编辑弹窗 -->
    <el-dialog :title="dialogType === 'add' ? '发起考核' : '编辑考核'" :visible.sync="dialogVisible" width="700px" :close-on-click-modal="false">
      <el-form ref="evalForm" :model="evalForm" :rules="evalRules" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="负责人" prop="leaderName">
              <el-input v-model="evalForm.leaderName" placeholder="请输入负责人姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属企业" prop="enterpriseName">
              <el-input v-model="evalForm.enterpriseName" placeholder="请输入企业名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="考核年度" prop="assessmentYear">
              <el-select v-model="evalForm.assessmentYear" placeholder="请选择" style="width:100%">
                <el-option label="2024" value="2024" />
                <el-option label="2023" value="2023" />
                <el-option label="2022" value="2022" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="考核类型" prop="assessmentType">
              <el-select v-model="evalForm.assessmentType" placeholder="请选择" style="width:100%">
                <el-option label="绩效考核" value="绩效考核" />
                <el-option label="年度考核" value="年度考核" />
                <el-option label="季度考核" value="季度考核" />
                <el-option label="能力评估" value="能力评估" />
                <el-option label="360度评价" value="360度评价" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="政治素质" prop="politicalScore">
              <el-input-number v-model="evalForm.politicalScore" :min="0" :max="100" :precision="1" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经营业绩" prop="economicScore">
              <el-input-number v-model="evalForm.economicScore" :min="0" :max="100" :precision="1" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="管理能力" prop="managementScore">
              <el-input-number v-model="evalForm.managementScore" :min="0" :max="100" :precision="1" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="廉洁自律" prop="integrityScore">
              <el-input-number v-model="evalForm.integrityScore" :min="0" :max="100" :precision="1" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="考核等级">
              <el-tag :type="getGradeType(computedGrade)" size="medium">{{ computedGrade }}（自动计算）</el-tag>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="考核人" prop="evaluator">
              <el-input v-model="evalForm.evaluator" placeholder="请输入考核人" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="考核时间" prop="evaluateTime">
              <el-date-picker v-model="evalForm.evaluateTime" type="datetime" placeholder="选择考核时间" style="width:100%" value-format="yyyy-MM-dd HH:mm:ss" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 查看详情弹窗 -->
    <el-dialog title="考核评价详情" :visible.sync="detailVisible" width="650px">
      <el-descriptions :column="2" border v-if="detailData">
        <el-descriptions-item label="负责人">{{ detailData.leaderName }}</el-descriptions-item>
        <el-descriptions-item label="所属企业">{{ detailData.enterpriseName }}</el-descriptions-item>
        <el-descriptions-item label="考核年度">{{ detailData.assessmentYear }}</el-descriptions-item>
        <el-descriptions-item label="考核类型">{{ detailData.assessmentType }}</el-descriptions-item>
        <el-descriptions-item label="政治素质得分">{{ detailData.politicalScore }}</el-descriptions-item>
        <el-descriptions-item label="经营业绩得分">{{ detailData.economicScore }}</el-descriptions-item>
        <el-descriptions-item label="管理能力得分">{{ detailData.managementScore }}</el-descriptions-item>
        <el-descriptions-item label="廉洁自律得分">{{ detailData.integrityScore }}</el-descriptions-item>
        <el-descriptions-item label="总分">
          <el-tag :type="getScoreType(detailData.totalScore)">{{ detailData.totalScore }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="考核等级">
          <el-tag :type="getGradeType(detailData.grade)">{{ detailData.grade }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="考核人">{{ detailData.evaluator }}</el-descriptions-item>
        <el-descriptions-item label="考核时间">{{ detailData.evaluateTime }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer">
        <el-button @click="detailVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getLeaderEvaluationList,
  addLeaderEvaluation,
  updateLeaderEvaluation,
  getLeaderEvaluationDetail,
  getLeaderEvaluationStatistics,
  getLeaderEvaluationTabList,
  getLeaderEvaluationAnalysis,
  exportLeaderEvaluationById,
  getEvaluationIndicatorList,
  getLeaderDevelopmentList
} from '@/api/leader/index'
import { investThemeMixin } from '../../stateAssets/themeMixin'

export default {
  name: 'LeaderEvaluation',
  mixins: [investThemeMixin],
  data() {
    return {
      activeTab: 'performance',
      searchText: '',
      loading: false,
      tabLoading: false,
      submitLoading: false,
      total: 0,
      queryForm: { pageNumber: 1, pageSize: 15 },
      overviewData: { totalEvaluations: 0, avgScore: 0, excellentRate: 0, completedCount: 0 },
      tableData: [],
      tabData: { performance: [], ability: [], development: [], feedback: [], indicators: [], analysis: [] },
      analysisData: null,
      // 弹窗控制
      dialogVisible: false,
      dialogType: 'add',
      detailVisible: false,
      detailData: null,
      // 表单
      evalForm: {
        id: null, leaderName: '', enterpriseName: '', assessmentYear: '2024',
        assessmentType: '绩效考核', politicalScore: 80, economicScore: 80,
        managementScore: 80, integrityScore: 80, evaluator: '', evaluateTime: ''
      },
      evalRules: {
        leaderName: [{ required: true, message: '请输入负责人姓名', trigger: 'blur' }],
        enterpriseName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        assessmentYear: [{ required: true, message: '请选择考核年度', trigger: 'change' }],
        assessmentType: [{ required: true, message: '请选择考核类型', trigger: 'change' }],
        evaluator: [{ required: true, message: '请输入考核人', trigger: 'blur' }]
      }
    }
  },
  computed: {
    // 根据四维度评分自动计算等级
    computedGrade() {
      const { politicalScore, economicScore, managementScore, integrityScore } = this.evalForm
      const avg = ((politicalScore || 0) + (economicScore || 0) + (managementScore || 0) + (integrityScore || 0)) / 4
      if (avg >= 90) return '优秀'
      if (avg >= 80) return '良好'
      if (avg >= 70) return '合格'
      return '待改进'
    }
  },
  mounted() {
    this.loadData()
    this.loadStatistics()
    this.loadTabData('performance')
  },
  methods: {
    // 加载主表格数据
    async loadData() {
      this.loading = true
      try {
        const params = { ...this.queryForm, searchText: this.searchText }
        const res = await getLeaderEvaluationList(params)
        if (res && res.data) {
          this.tableData = res.data.tlist || []
          this.total = res.data.totalRecord || 0
        }
      } catch (e) { console.error('加载考核评价数据失败', e) }
      finally { this.loading = false }
    },
    // 加载统计数据
    async loadStatistics() {
      try {
        const res = await getLeaderEvaluationStatistics()
        if (res && res.data) { this.overviewData = res.data }
      } catch (e) { console.error('加载统计数据失败', e) }
    },
    // 加载Tab数据
    async loadTabData(tabType) {
      this.tabLoading = true
      try {
        if (tabType === 'development') {
          const res = await getLeaderDevelopmentList({ pageNumber: 1, pageSize: 10 })
          if (res && res.data) { this.tabData.development = res.data.tlist || [] }
        } else if (tabType === 'indicators') {
          const res = await getEvaluationIndicatorList({ pageNumber: 1, pageSize: 10 })
          if (res && res.data) { this.tabData.indicators = res.data.tlist || [] }
        } else if (tabType === 'analysis') {
          const res = await getLeaderEvaluationAnalysis()
          if (res && res.data) { this.analysisData = res.data }
        } else {
          const res = await getLeaderEvaluationTabList({ pageNumber: 1, pageSize: 10, tabType })
          if (res && res.data) { this.tabData[tabType] = res.data.tlist || [] }
        }
      } catch (e) { console.error('加载Tab数据失败', e) }
      finally { this.tabLoading = false }
    },
    // Tab切换
    handleTabClick(tab) { this.loadTabData(tab.name) },
    // 刷新所有数据
    refreshAllData() {
      this.loadData()
      this.loadStatistics()
      this.loadTabData(this.activeTab)
      this.$message.success('数据已刷新')
    },
    // 发起考核
    handleStartEvaluation() {
      this.dialogType = 'add'
      this.evalForm = {
        id: null, leaderName: '', enterpriseName: '', assessmentYear: '2024',
        assessmentType: '绩效考核', politicalScore: 80, economicScore: 80,
        managementScore: 80, integrityScore: 80, evaluator: '', evaluateTime: ''
      }
      this.dialogVisible = true
      this.$nextTick(() => { if (this.$refs.evalForm) this.$refs.evalForm.clearValidate() })
    },
    // 查看详情
    async handleView(row) {
      try {
        const res = await getLeaderEvaluationDetail(row.id)
        if (res && res.data) {
          this.detailData = res.data
          this.detailVisible = true
        }
      } catch (e) { this.$message.error('获取详情失败') }
    },
    // 编辑
    async handleEdit(row) {
      try {
        const res = await getLeaderEvaluationDetail(row.id)
        if (res && res.data) {
          const d = res.data
          this.evalForm = {
            id: d.id, leaderName: d.leaderName || '', enterpriseName: d.enterpriseName || '',
            assessmentYear: d.assessmentYear || '2024', assessmentType: d.assessmentType || '绩效考核',
            politicalScore: d.politicalScore || 0, economicScore: d.economicScore || 0,
            managementScore: d.managementScore || 0, integrityScore: d.integrityScore || 0,
            evaluator: d.evaluator || '', evaluateTime: d.evaluateTime || ''
          }
          this.dialogType = 'edit'
          this.dialogVisible = true
        }
      } catch (e) { this.$message.error('获取数据失败') }
    },
    // 提交表单（新增/编辑）
    handleSubmit() {
      this.$refs.evalForm.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          const data = { ...this.evalForm }
          // 计算总分 = 四维度平均分
          data.totalScore = ((data.politicalScore || 0) + (data.economicScore || 0) + (data.managementScore || 0) + (data.integrityScore || 0)) / 4
          data.totalScore = Math.round(data.totalScore * 10) / 10
          // 根据总分自动计算等级
          if (data.totalScore >= 90) data.grade = '优秀'
          else if (data.totalScore >= 80) data.grade = '良好'
          else if (data.totalScore >= 70) data.grade = '合格'
          else data.grade = '待改进'
          // 处理空时间字段：空字符串转null，避免后端反序列化失败
          if (!data.evaluateTime) data.evaluateTime = null
          if (this.dialogType === 'add') {
            await addLeaderEvaluation(data)
            this.$message.success('考核发起成功，已自动归档')
          } else {
            await updateLeaderEvaluation(data)
            this.$message.success('考核更新成功')
          }
          this.dialogVisible = false
          this.loadData()
          this.loadStatistics()
          this.loadTabData(this.activeTab)
        } catch (e) { this.$message.error('操作失败') }
        finally { this.submitLoading = false }
      })
    },
    // 单条导出 - 后端返回JSON，前端生成Excel文件
    async handleExport(row) {
      try {
        const res = await exportLeaderEvaluationById(row.id)
        if (res && res.data) {
          const data = res.data
          // 用HTML表格生成Excel（Excel可直接打开HTML table格式的.xls文件）
          let html = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel"><head><meta charset="UTF-8"></head><body>'
          html += '<table border="1" cellpadding="5" cellspacing="0">'
          html += '<tr style="background:#4472C4;color:#fff;font-weight:bold"><td>项目</td><td>内容</td></tr>'
          Object.keys(data).forEach(key => {
            html += '<tr><td>' + key + '</td><td>' + (data[key] || '') + '</td></tr>'
          })
          html += '</table></body></html>'
          const blob = new Blob(['\ufeff' + html], { type: 'application/vnd.ms-excel' })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = `考核报告_${row.leaderName}_${row.evaluationPeriod || ''}.xls`
          link.click()
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        } else {
          this.$message.error(res && res.msg ? res.msg : '导出失败')
        }
      } catch (e) { this.$message.error('导出失败') }
    },
    // 搜索
    handleSearch() { this.queryForm.pageNumber = 1; this.loadData() },
    // 分页
    handleSizeChange(val) { this.queryForm.pageSize = val; this.loadData() },
    handleCurrentChange(val) { this.queryForm.pageNumber = val; this.loadData() },
    // 工具方法
    getScoreType(score) {
      if (score >= 90) return 'success'
      if (score >= 80) return ''
      if (score >= 70) return 'warning'
      return 'danger'
    },
    getGradeType(grade) {
      const m = { '优秀': 'success', '良好': '', '合格': 'warning', '待改进': 'danger' }
      return m[grade] || 'info'
    },
    getStatusType(status) {
      const m = { '已完成': 'success', '进行中': 'warning', '待审核': 'info', '已发布': 'success' }
      return m[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.leader-evaluation {
  padding: 20px;

  .overview-cards {
    margin-bottom: 20px;
    .overview-card {
      height: 120px;
      &.gradient-theme {
        background: linear-gradient(135deg, var(--ip-secondary) 0%, var(--ip-bright) 100%);
        color: white;
        .card-content {
          display: flex;
          align-items: center;
          height: 100%;
          .icon-wrapper { font-size: 40px; margin-right: 15px; opacity: 0.8; }
          .data-wrapper {
            .number { font-size: 28px; font-weight: bold; line-height: 1; }
            .label { font-size: 14px; margin-top: 5px; opacity: 0.9; }
          }
        }
      }
    }
  }

  .function-card, .data-card { margin-bottom: 20px; }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    .card-title { font-size: 16px; font-weight: bold; }
  }

  .search-wrapper { display: flex; align-items: center; }

  .action-buttons {
    display: flex;
    flex-wrap: nowrap;
    gap: 4px;
  }

  .analysis-content {
    padding: 10px;
    .analysis-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 8px 0;
      border-bottom: 1px solid #f0f0f0;
      &:last-child { border-bottom: none; }
    }
  }

  ::v-deep .el-tabs__content { padding: 10px; }
  ::v-deep .el-descriptions-item__label { width: 120px; }
}
</style>
