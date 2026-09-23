<template>
  <div class="risk-assessment-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-data-analysis"></i>
            风险评估管理
          </h2>
          <p class="page-description">风险评估、风险分析和风险等级管理</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增评估
          </el-button>
          <el-button type="success" icon="el-icon-upload" @click="handleImport">
            批量导入
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 风险评估概览卡片 -->
    <div class="assessment-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">评估总数</div>
                <div class="card-value">{{ totalAssessments }}</div>
                <div class="card-change">项</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon high-risk-icon">
                <i class="el-icon-warning"></i>
              </div>
              <div class="card-info">
                <div class="card-title">高风险项目</div>
                <div class="card-value">{{ highRiskCount }}</div>
                <div class="card-change negative">项</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon pending-icon">
                <i class="el-icon-time"></i>
              </div>
              <div class="card-info">
                <div class="card-title">待评估</div>
                <div class="card-value">{{ pendingAssessments }}</div>
                <div class="card-change warning">项</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon average-score-icon">
                <i class="el-icon-data-line"></i>
              </div>
              <div class="card-info">
                <div class="card-title">平均风险评分</div>
                <div class="card-value">{{ averageRiskScore }}</div>
                <div class="card-change">分</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 风险分析图表 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div class="chart-header">
            <h3>风险等级分布</h3>
          </div>
          <div id="riskLevelChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div class="chart-header">
            <h3>风险类别分布</h3>
          </div>
          <div id="riskCategoryChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="评估编号">
            <el-input
              v-model="listQuery.assessmentNo"
              placeholder="请输入评估编号"
              style="width: 150px;"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="评估名称">
            <el-input
              v-model="listQuery.assessmentName"
              placeholder="请输入评估名称"
              style="width: 150px;"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="风险等级">
            <el-select
              v-model="listQuery.riskLevel"
              placeholder="请选择风险等级"
              clearable
              style="width: 120px;"
            >
              <el-option label="低风险" value="LOW" />
              <el-option label="中风险" value="MEDIUM" />
              <el-option label="高风险" value="HIGH" />
              <el-option label="极高风险" value="CRITICAL" />
            </el-select>
          </el-form-item>
          <el-form-item label="评估状态">
            <el-select
              v-model="listQuery.assessmentStatus"
              placeholder="请选择状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="待评估" value="1" />
              <el-option label="评估中" value="2" />
              <el-option label="已完成" value="3" />
              <el-option label="已审核" value="4" />
            </el-select>
          </el-form-item>
          <el-form-item label="评估日期">
            <el-date-picker
              v-model="listQuery.assessmentDateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 240px;"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button type="default" icon="el-icon-refresh" @click="resetQuery">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 风险评估表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="assessmentList"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        v-loading="listLoading"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="风险ID" prop="riskId" width="120" align="center" show-overflow-tooltip />
        <el-table-column label="风险项" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewDetail(row)">{{ row.riskItem }}</span>
          </template>
        </el-table-column>
        <el-table-column label="风险类别" width="120px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getRiskTypeTagType(row.riskCategory)" size="mini">
              {{ getRiskCategoryText(row.riskCategory) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险描述" width="200px" align="center" show-overflow-tooltip>
          <template slot-scope="{row}">
            <span>{{ row.riskDescription }}</span>
          </template>
        </el-table-column>
        <el-table-column label="影响程度" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getImpactDegreeTagType(row.impactDegree)" size="mini">
              {{ getImpactDegreeText(row.impactDegree) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="发生概率" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getProbabilityTagType(row.occurrenceProbability)" size="mini">
              {{ getProbabilityText(row.occurrenceProbability) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险等级" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getRiskLevelTagType(row.riskLevel)" size="mini">
              {{ getRiskLevelText(row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="应对措施" width="200px" align="center" show-overflow-tooltip>
          <template slot-scope="{row}">
            <span>{{ row.countermeasures }}</span>
          </template>
        </el-table-column>
        <el-table-column label="评估状态" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getAssessmentStatusTagType(row.status)" size="mini">
              {{ getAssessmentStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="评估日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ formatDate(row.assessmentDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <el-button v-if="String(row.status) === '1'" type="primary" size="mini" @click="handleStartAssessment(row)">
              开始评估
            </el-button>
            <el-button v-if="String(row.status) === '3'" type="success" size="mini" @click="handleReview(row)">
              审核
            </el-button>
            <el-button type="info" size="mini" @click="handleViewDetail(row)">
              详情
            </el-button>
            <el-dropdown size="mini" @command="handleCommand">
              <el-button size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'edit', row: row}">编辑</el-dropdown-item>
                <el-dropdown-item :command="{action: 'copy', row: row}">复制</el-dropdown-item>
                <el-dropdown-item :command="{action: 'report', row: row}">生成报告</el-dropdown-item>
                <el-dropdown-item :command="{action: 'history', row: row}">评估历史</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px" @close="resetAssessmentForm">
      <el-form :model="assessmentForm" :rules="assessmentRules" ref="assessmentForm" label-width="110px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="风险类别" prop="riskCategory">
              <el-select v-model="assessmentForm.riskCategory" placeholder="请选择" style="width:100%">
                <el-option label="市场风险" value="MARKET"></el-option>
                <el-option label="信用风险" value="CREDIT"></el-option>
                <el-option label="流动性风险" value="LIQUIDITY"></el-option>
                <el-option label="操作风险" value="OPERATIONAL"></el-option>
                <el-option label="合规风险" value="COMPLIANCE"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险项" prop="riskItem">
              <el-input v-model="assessmentForm.riskItem" placeholder="请输入风险项"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="影响程度" prop="impactDegree">
              <el-select v-model="assessmentForm.impactDegree" placeholder="请选择" style="width:100%">
                <el-option label="低" value="LOW"></el-option>
                <el-option label="中" value="MEDIUM"></el-option>
                <el-option label="高" value="HIGH"></el-option>
                <el-option label="严重" value="CRITICAL"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发生概率" prop="occurrenceProbability">
              <el-select v-model="assessmentForm.occurrenceProbability" placeholder="请选择" style="width:100%">
                <el-option label="低" value="LOW"></el-option>
                <el-option label="中" value="MEDIUM"></el-option>
                <el-option label="高" value="HIGH"></el-option>
                <el-option label="严重" value="CRITICAL"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="风险等级" prop="riskLevel">
              <el-select v-model="assessmentForm.riskLevel" placeholder="请选择" style="width:100%">
                <el-option label="低风险" value="LOW"></el-option>
                <el-option label="中风险" value="MEDIUM"></el-option>
                <el-option label="高风险" value="HIGH"></el-option>
                <el-option label="极高风险" value="CRITICAL"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评估日期" prop="assessmentDate">
              <el-date-picker v-model="assessmentForm.assessmentDate" type="date" placeholder="请选择评估日期" value-format="yyyy-MM-dd" style="width:100%"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="assessmentForm.status" placeholder="请选择" style="width:100%">
                <el-option label="待评估" value="1"></el-option>
                <el-option label="评估中" value="2"></el-option>
                <el-option label="已完成" value="3"></el-option>
                <el-option label="已审核" value="4"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="风险描述" prop="riskDescription">
              <el-input v-model="assessmentForm.riskDescription" type="textarea" :rows="2"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="应对措施" prop="countermeasures">
              <el-input v-model="assessmentForm.countermeasures" type="textarea" :rows="2"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitAssessmentForm">确定</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="评估详情" :visible.sync="detailVisible" width="700px">
      <el-descriptions :column="2" border v-if="detailRow">
        <el-descriptions-item label="风险类别">{{ getRiskCategoryText(detailRow.riskCategory) }}</el-descriptions-item>
        <el-descriptions-item label="风险项">{{ detailRow.riskItem }}</el-descriptions-item>
        <el-descriptions-item label="风险等级">
          <el-tag :type="getRiskLevelTagType(detailRow.riskLevel)" size="small">{{ getRiskLevelText(detailRow.riskLevel) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="评估状态">
          <el-tag :type="getAssessmentStatusTagType(detailRow.status)" size="small">{{ getAssessmentStatusText(detailRow.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="影响程度">{{ getImpactDegreeText(detailRow.impactDegree) }}</el-descriptions-item>
        <el-descriptions-item label="发生概率">{{ getProbabilityText(detailRow.occurrenceProbability) }}</el-descriptions-item>
        <el-descriptions-item label="评估日期">{{ formatDate(detailRow.assessmentDate) }}</el-descriptions-item>
        <el-descriptions-item label="企业ID">{{ detailRow.enterpriseId }}</el-descriptions-item>
        <el-descriptions-item label="风险描述" :span="2">{{ detailRow.riskDescription }}</el-descriptions-item>
        <el-descriptions-item label="应对措施" :span="2">{{ detailRow.countermeasures }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer"><el-button @click="detailVisible = false">关闭</el-button></div>
    </el-dialog>

    <!-- 审核对话框 -->
    <el-dialog title="风险评估审核" :visible.sync="reviewVisible" width="500px">
      <el-form :model="reviewForm" ref="reviewForm" label-width="100px">
        <el-form-item label="审核意见" prop="reviewComment">
          <el-input v-model="reviewForm.reviewComment" type="textarea" :rows="3" placeholder="请输入审核意见"></el-input>
        </el-form-item>
        <el-form-item label="审核结果" prop="reviewResult">
          <el-radio-group v-model="reviewForm.reviewResult">
            <el-radio label="4">通过</el-radio>
            <el-radio label="5">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="reviewVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="submitReview">确定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="批量导入风险评估" :visible.sync="importDialogVisible" width="500px">
      <el-upload
        ref="importUpload"
        :action="''"
        :auto-upload="false"
        :limit="1"
        accept=".xlsx,.xls"
        :on-change="handleImportFileChange"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传xlsx/xls文件，表头顺序：企业ID、风险类别、风险项、风险等级、风险描述、影响程度、发生概率、应对措施</div>
      </el-upload>
      <div slot="footer">
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="importLoading" @click="submitImport">确定导入</el-button>
      </div>
    </el-dialog>

    <el-dialog title="评估历史" :visible.sync="historyDialogVisible" width="800px">
      <el-table :data="historyList" border stripe>
        <el-table-column label="操作类型" prop="operationType" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.operationType === 'CREATE' ? 'success' : 'warning'" size="mini">
              {{ scope.row.operationType === 'CREATE' ? '创建' : '更新' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="风险类别" prop="riskCategory" width="120" />
        <el-table-column label="风险项" prop="riskItem" width="150" />
        <el-table-column label="风险等级" prop="riskLevel" width="100" align="center" />
        <el-table-column label="状态" prop="status" width="100" align="center" />
        <el-table-column label="操作时间" prop="operationTime" width="160" />
        <el-table-column label="备注" prop="remark" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import * as echarts from 'echarts'
import { getRiskAssessmentPage, getRiskDistributionAnalysis, exportRiskAssessment, createRiskAssessment, updateRiskAssessment, getRiskAssessment, importRiskAssessment, generateRiskAssessmentReport, getRiskAssessmentHistory } from '@/api/globalTreasurer/fxgl'

export default {
  name: 'RiskAssessmentManage',
  components: { Pagination },
  data() {
    return {
      listLoading: false,
      total: 0,
      listQuery: {
        page: 1, limit: 20, assessmentNo: undefined, assessmentName: undefined,
        riskLevel: undefined, assessmentStatus: undefined, assessmentDateRange: undefined,
        riskCategory: undefined
      },
      totalAssessments: 0, highRiskCount: 0, pendingAssessments: 0, averageRiskScore: 0,
      assessmentList: [], multipleSelection: [],
      riskLevelChart: null, riskCategoryChart: null, riskLevelData: [], riskCategoryData: [],
      // 新增/编辑
      dialogVisible: false, dialogTitle: '新增评估', isEdit: false, submitLoading: false,
      assessmentForm: {
        riskId: null, riskCategory: '', riskItem: '', riskLevel: '', impactDegree: '', occurrenceProbability: '',
        riskDescription: '', countermeasures: '', assessmentDate: null, status: '1'
      },
      assessmentRules: {
        riskCategory: [{ required: true, message: '请选择风险类别', trigger: 'change' }],
        riskItem: [{ required: true, message: '请输入风险项', trigger: 'blur' }],
        assessmentDate: [{ required: true, message: '请选择评估日期', trigger: 'change' }]
      },
      // 详情
      detailVisible: false, detailRow: null,
      // 审核
      reviewVisible: false, reviewRow: null,
      reviewForm: { reviewComment: '', reviewResult: 'APPROVED' },
      importDialogVisible: false,
      importLoading: false,
      importFile: null,
      historyDialogVisible: false,
      historyList: []
    }
  },
  mounted() {
    this.applyRouteRiskCategoryFilter()
    this.getList()
    this.getStatisticsData()
  },
  watch: {
    '$route.query.riskTypeId'(newVal, oldVal) {
      if (newVal === oldVal) {
        return
      }
      this.applyRouteRiskCategoryFilter()
      this.listQuery.page = 1
      this.getList()
    }
  },
  beforeDestroy() {
    if (this.riskLevelChart) {
      this.riskLevelChart.dispose()
    }
    if (this.riskCategoryChart) {
      this.riskCategoryChart.dispose()
    }
  },
  methods: {
    getRouteRiskCategory() {
      const routeRiskTypeId = this.$route.query.riskTypeId
      const riskTypeCategoryMap = {
        1: 'CREDIT',
        2: 'LIQUIDITY',
        3: 'MARKET',
        4: 'OPERATIONAL',
        5: 'COMPLIANCE'
      }
      return riskTypeCategoryMap[Number(routeRiskTypeId)]
    },

    applyRouteRiskCategoryFilter() {
      this.listQuery.riskCategory = this.getRouteRiskCategory()
    },

    getList() {
      this.listLoading = true
      // 前端 listQuery 参数名 → 后端 DTO 字段名映射
      const params = {
        page: this.listQuery.page,
        limit: this.listQuery.limit,
        riskId: this.listQuery.assessmentNo || undefined,
        riskItem: this.listQuery.assessmentName || undefined,
        riskLevel: this.listQuery.riskLevel || undefined,
        status: this.listQuery.assessmentStatus || undefined,
        riskCategory: this.listQuery.riskCategory || undefined
      }
      // 日期范围拆分为 assessmentDateStart / assessmentDateEnd
      if (this.listQuery.assessmentDateRange && this.listQuery.assessmentDateRange.length === 2) {
        params.assessmentDateStart = this.formatDateToStr(this.listQuery.assessmentDateRange[0])
        params.assessmentDateEnd = this.formatDateToStr(this.listQuery.assessmentDateRange[1])
      }
      getRiskAssessmentPage(params).then(response => {
        // 兼容后端返回 String 类型
        let res = response
        if (typeof response === 'string' || response instanceof String) {
          try {
            const fixed = response.replace(/\{null:/g, '{"null":').replace(/,null:/g, ',"null":')
            res = JSON.parse(fixed)
          } catch (e) { res = {} }
        }
        this.assessmentList = res.data || []
        this.total = res.result ? res.result.total : 0
        this.listLoading = false
      }).catch(() => {
        this.assessmentList = []
        this.total = 0
        this.listLoading = false
      })
    },
    getStatisticsData() {
      getRiskDistributionAnalysis({}).then(response => {
        // 兼容后端返回 String 类型（JsonBean 手动序列化）
        let res = response
        if (typeof response === 'string' || response instanceof String) {
          try {
            // 修复 FastJSON 序列化 null key 产生的非法 JSON：{null:"1"} → {"null":"1"}
            const fixed = response.replace(/\{null:/g, '{"null":').replace(/,null:/g, ',"null":')
            res = JSON.parse(fixed)
          } catch (e) {
            console.error('[fxpg] JSON.parse failed:', e)
            res = {}
          }
        }
        const data = (res && res.data) || {}
        this.totalAssessments = Number(data.totalAssessments || data.total) || 0
        this.highRiskCount = Number(data.highRiskCount) || 0
        this.pendingAssessments = Number(data.pendingAssessments) || 0
        this.averageRiskScore = Number(data.averageRiskScore) || 0
        // 英文 → 中文映射
        const levelNameMap = { 'HIGH': '高', 'MEDIUM': '中', 'LOW': '低', 'CRITICAL': '极高' }
        const categoryNameMap = { 'CREDIT': '信用风险', 'MARKET': '市场风险', 'OPERATIONAL': '操作风险', 'LIQUIDITY': '流动性风险', 'COMPLIANCE': '合规风险', 'REPUTATION': '声誉风险' }
        // 后端返回 levelDistribution / categoryDistribution 是 Map 对象，转为图表需要的数组，合并同义 key
        const levelDist = data.levelDistribution || {}
        const mergedLevel = {}
        Object.keys(levelDist).filter(k => k && k !== 'null').forEach(key => {
          const cnName = levelNameMap[key] || key
          mergedLevel[cnName] = (mergedLevel[cnName] || 0) + (Number(levelDist[key]) || 0)
        })
        this.riskLevelData = Object.keys(mergedLevel).map(name => ({ name, value: mergedLevel[name] }))
        const categoryDist = data.categoryDistribution || {}
        const mergedCategory = {}
        Object.keys(categoryDist).filter(k => k && k !== 'null').forEach(key => {
          const cnName = categoryNameMap[key] || key
          mergedCategory[cnName] = (mergedCategory[cnName] || 0) + (Number(categoryDist[key]) || 0)
        })
        this.riskCategoryData = Object.keys(mergedCategory).map(name => ({ name, value: mergedCategory[name] }))
        this.$nextTick(() => {
          this.initCharts()
        })
      }).catch(() => {
        this.totalAssessments = 0
        this.highRiskCount = 0
        this.pendingAssessments = 0
        this.averageRiskScore = 0
        this.riskLevelData = []
        this.riskCategoryData = []
      })
    },
    initCharts() {
      // 初始化风险等级图表
      this.riskLevelChart = echarts.init(document.getElementById('riskLevelChart'))
      this.updateRiskLevelChart()

      // 初始化风险类别图表
      this.riskCategoryChart = echarts.init(document.getElementById('riskCategoryChart'))
      this.updateRiskCategoryChart()
    },
    updateRiskLevelChart() {
      const data = this.riskLevelData.length > 0 ? this.riskLevelData : []

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c}项 ({d}%)'
        },
        series: [
          {
            name: '风险等级',
            type: 'pie',
            radius: ['40%', '70%'],
            data: data
          }
        ]
      }

      this.riskLevelChart.setOption(option)
    },
    updateRiskCategoryChart() {
      const data = this.riskCategoryData.length > 0 ? this.riskCategoryData : []

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c}项 ({d}%)'
        },
        series: [
          {
            name: '风险类别',
            type: 'pie',
            radius: ['40%', '70%'],
            data: data
          }
        ]
      }

      this.riskCategoryChart.setOption(option)
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        page: 1,
        limit: 20,
        assessmentNo: undefined,
        assessmentName: undefined,
        riskLevel: undefined,
        assessmentStatus: undefined,
        assessmentDateRange: undefined,
        riskCategory: this.getRouteRiskCategory()
      }
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },

    handleCreate() {
      this.isEdit = false
      this.dialogTitle = '新增评估'
      this.assessmentForm = {
        riskId: null, riskCategory: this.getRouteRiskCategory() || '', riskItem: '', riskLevel: '', impactDegree: '', occurrenceProbability: '',
        riskDescription: '', countermeasures: '', assessmentDate: null, status: '1'
      }
      this.dialogVisible = true
    },
    handleViewDetail(row) {
      getRiskAssessment(row.riskId).then(response => {
        this.detailRow = response.data || row
        this.detailVisible = true
      }).catch(() => {
        this.detailRow = row
        this.detailVisible = true
      })
    },
    handleReview(row) {
      this.reviewRow = row
      this.reviewForm = { reviewComment: '', reviewResult: '4' }
      this.reviewVisible = true
    },
    submitReview() {
      if (!this.reviewRow) return
      this.submitLoading = true
      updateRiskAssessment({ riskId: this.reviewRow.riskId, status: this.reviewForm.reviewResult }).then(() => {
        this.$message.success('审核完成')
        this.reviewVisible = false
        this.getList()
        this.getStatisticsData()
      }).finally(() => {
        this.submitLoading = false
      })
    },
    handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'edit':
          this.isEdit = true
          this.dialogTitle = '编辑评估'
          this.assessmentForm = Object.assign({}, row)
          if (this.assessmentForm.assessmentDate) {
            this.assessmentForm.assessmentDate = this.formatDateToStr(this.assessmentForm.assessmentDate)
          }
          this.dialogVisible = true
          break
        case 'copy':
          this.isEdit = false
          this.dialogTitle = '复制评估'
          this.assessmentForm = Object.assign({}, row, { riskId: null, status: '1' })
          if (this.assessmentForm.assessmentDate) {
            this.assessmentForm.assessmentDate = this.formatDateToStr(this.assessmentForm.assessmentDate)
          }
          this.dialogVisible = true
          break
        case 'report':
          generateRiskAssessmentReport(row.riskId).then(res => {
            const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
            const url = window.URL.createObjectURL(blob)
            const a = document.createElement('a')
            a.href = url
            a.download = '风险评估报告_' + row.riskId + '.xlsx'
            a.click()
            window.URL.revokeObjectURL(url)
            this.$message.success('报告已生成并下载')
          })
          break
        case 'history':
          getRiskAssessmentHistory(row.riskId).then(res => {
            this.historyList = res.data || []
            this.historyDialogVisible = true
          })
          break
      }
    },
    handleImport() {
      this.importFile = null
      this.importDialogVisible = true
      this.$nextTick(() => {
        this.$refs.importUpload && this.$refs.importUpload.clearFiles()
      })
    },
    handleImportFileChange(file) {
      this.importFile = file.raw
    },
    submitImport() {
      if (!this.importFile) {
        this.$message.warning('请先选择要导入的文件')
        return
      }
      this.importLoading = true
      importRiskAssessment(this.importFile).then(res => {
        const data = res.data
        this.$message.success('导入完成，成功' + (data.successCount || 0) + '条，失败' + (data.failCount || 0) + '条')
        this.importDialogVisible = false
        this.getList()
        this.getStatisticsData()
      }).finally(() => {
        this.importLoading = false
      })
    },
    submitAssessmentForm() {
      this.$refs.assessmentForm.validate(valid => {
        if (!valid) return
        this.submitLoading = true
        const api = this.isEdit ? updateRiskAssessment : createRiskAssessment
        api(this.assessmentForm).then(() => {
          this.$message.success(this.isEdit ? '更新成功' : '创建成功')
          this.dialogVisible = false
          this.getList()
          this.getStatisticsData()
        }).finally(() => {
          this.submitLoading = false
        })
      })
    },
    resetAssessmentForm() {
      this.$refs.assessmentForm && this.$refs.assessmentForm.resetFields()
    },
    handleStartAssessment(row) {
      this.$confirm('确认开始风险评估?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateRiskAssessment({ riskId: row.riskId, status: '2' }).then(() => {
          this.$message({
            type: 'success',
            message: '风险评估已开始!'
          })
          this.getList()
        })
      })
    },
    handleExport() {
      exportRiskAssessment(this.listQuery).then(response => {
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `risk_assessment_${new Date().getTime()}.xlsx`
        link.style.display = 'none'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        this.$message({
          type: 'success',
          message: '数据导出成功'
        })
      })
    },
    getNextAssessmentClass(date) {
      if (!date) return ''
      const today = new Date()
      const nextDate = new Date(date)
      const diffDays = Math.ceil((nextDate - today) / (1000 * 60 * 60 * 24))

      if (diffDays <= 7) return 'urgent-assessment'
      if (diffDays <= 30) return 'upcoming-assessment'
      return ''
    },
    getRiskScoreClass(score) {
      if (score >= 8) return 'high-risk-score'
      if (score >= 6) return 'medium-risk-score'
      return 'low-risk-score'
    },
    getRiskTypeTagType(category) {
      const typeMap = {
        'MARKET': 'primary',
        'CREDIT': 'success',
        'LIQUIDITY': 'warning',
        'OPERATIONAL': 'info',
        'COMPLIANCE': 'danger'
      }
      return typeMap[category] || 'default'
    },
    getRiskCategoryText(category) {
      const textMap = {
        'MARKET': '市场风险',
        'CREDIT': '信用风险',
        'LIQUIDITY': '流动性风险',
        'OPERATIONAL': '操作风险',
        'COMPLIANCE': '合规风险'
      }
      return textMap[category] || category
    },
    getRiskLevelTagType(level) {
      const typeMap = {
        'LOW': 'success', 1: 'success',
        'MEDIUM': 'warning', 2: 'warning',
        'HIGH': 'danger', 3: 'danger',
        'CRITICAL': 'info', 4: 'info'
      }
      return typeMap[level] || 'default'
    },
    getRiskLevelText(level) {
      const textMap = {
        'LOW': '低风险', 1: '低风险',
        'MEDIUM': '中风险', 2: '中风险',
        'HIGH': '高风险', 3: '高风险',
        'CRITICAL': '极高风险', 4: '极高风险'
      }
      return textMap[level] || level
    },
    getAssessmentStatusTagType(status) {
      const typeMap = {
        'PENDING': 'info', 1: 'info',
        'IN_PROGRESS': 'warning', 2: 'warning',
        'COMPLETED': 'primary', 3: 'primary',
        'REVIEWED': 'success', 4: 'success',
        'APPROVED': 'success',
        'REJECTED': 'danger', 5: 'danger'
      }
      return typeMap[status] || 'default'
    },
    getAssessmentStatusText(status) {
      const textMap = {
        'PENDING': '待评估', 1: '待评估',
        'IN_PROGRESS': '评估中', 2: '评估中',
        'COMPLETED': '已完成', 3: '已完成',
        'REVIEWED': '已审核', 4: '已审核',
        'APPROVED': '已通过',
        'REJECTED': '已驳回', 5: '已驳回'
      }
      return textMap[status] || status
    },
    formatCurrency(amount) {
      if (amount === undefined || amount === null) return '¥0.00'
      const formatter = new Intl.NumberFormat('zh-CN', {
        style: 'currency',
        currency: 'CNY',
        minimumFractionDigits: 2
      })
      return formatter.format(amount)
    },
    // 影响程度标签类型
    getImpactDegreeTagType(degree) {
      const typeMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'info'
      }
      return typeMap[degree] || 'default'
    },
    // 影响程度文本
    getImpactDegreeText(degree) {
      const textMap = {
        'LOW': '低',
        'MEDIUM': '中',
        'HIGH': '高',
        'CRITICAL': '极高'
      }
      return textMap[degree] || degree
    },
    // 发生概率标签类型
    getProbabilityTagType(probability) {
      const typeMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'info'
      }
      return typeMap[probability] || 'default'
    },
    // 发生概率文本
    getProbabilityText(probability) {
      const textMap = {
        'LOW': '低',
        'MEDIUM': '中',
        'HIGH': '高',
        'CRITICAL': '极高'
      }
      return textMap[probability] || probability
    },
    // 将日期转为 yyyy-MM-dd 字符串（用于 el-date-picker 回填）
    formatDateToStr(date) {
      if (!date) return ''
      const d = new Date(date)
      if (Number.isNaN(d.getTime())) return ''
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },
    // 格式化日期（表格展示用）
    formatDate(date) {
      if (!date) return '-'
      const d = new Date(date)
      if (Number.isNaN(d.getTime())) return '-'
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    }
  }
}
</script>

<style lang="scss" scoped>
.risk-assessment-manage {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;
    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .header-left {
        .page-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 600;
          color: #303133;
          i {
            margin-right: 8px;
            color: #409EFF;
          }
        }
        .page-description {
          margin: 0;
          color: #606266;
          font-size: 14px;
        }
      }
    }
  }

  .assessment-overview {
    margin-bottom: 20px;
    .overview-card {
      .card-content {
        display: flex;
        align-items: center;
        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          i {
            font-size: 24px;
            color: white;
          }
          &.total-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }
          &.high-risk-icon {
            background: linear-gradient(135deg, #ff6b6b 0%, #ee5a52 100%);
          }
          &.pending-icon {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
          }
          &.average-score-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
        }
        .card-info {
          flex: 1;
          .card-title {
            font-size: 14px;
            color: #909399;
            margin-bottom: 8px;
          }
          .card-value {
            font-size: 24px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 4px;
          }
          .card-change {
            font-size: 12px;
            color: #909399;
            &.positive {
              color: #67C23A;
            }
            &.negative {
              color: #F56C6C;
            }
            &.warning {
              color: #E6A23C;
            }
          }
        }
      }
    }
  }

  .chart-card, .search-card, .table-card {
    margin-bottom: 20px;
  }

  .chart-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h3 {
      margin: 0;
      color: #303133;
      font-size: 16px;
      font-weight: 600;
    }
  }

  .chart-container {
    height: 300px;
    width: 100%;
  }

  .link-type {
    color: #409EFF;
    cursor: pointer;
    &:hover {
      color: #66b1ff;
    }
  }

  .asset-value, .risk-exposure {
    font-weight: 600;
    color: #409EFF;
  }

  .high-risk-score {
    color: #F56C6C;
    font-weight: 600;
  }

  .medium-risk-score {
    color: #E6A23C;
    font-weight: 600;
  }

  .low-risk-score {
    color: #67C23A;
    font-weight: 600;
  }

  .urgent-assessment {
    color: #F56C6C;
    font-weight: 600;
  }

  .upcoming-assessment {
    color: #E6A23C;
    font-weight: 600;
  }
}
</style>
