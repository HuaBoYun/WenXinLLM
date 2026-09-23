<template>
  <div class="app-container">
    <!-- 统计概览卡片 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-document" style="color: #409eff"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.totalReports || 0 }}</div>
              <div class="statistics-label">报告总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-document-checked" style="color: #67c23a"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.publishedReports || 0 }}</div>
              <div class="statistics-label">已发布报告</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-edit-outline" style="color: #e6a23c"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.draftReports || 0 }}</div>
              <div class="statistics-label">草稿报告</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon">
              <i class="el-icon-download" style="color: #f56c6c"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.monthlyDownloads || 0 }}</div>
              <div class="statistics-label">本月下载量</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能标签页 -->
    <el-card>
      <el-tabs v-model="activeTab" type="border-card">
        <!-- 报告管理 -->
        <el-tab-pane label="报告管理" name="reports">
          <div class="tab-content">
            <!-- 查询表单 -->
            <el-form :model="reportsQuery" ref="reportsForm" :inline="true" label-width="100px" class="mb-20">
              <el-form-item label="报告标题">
                <el-input v-model="reportsQuery.reportTitle" placeholder="请输入报告标题" style="width: 200px"></el-input>
              </el-form-item>
              <el-form-item label="报告类型">
                <el-select v-model="reportsQuery.reportType" placeholder="请选择报告类型" clearable style="width: 150px">
                  <el-option label="监管报告" value="SUPERVISION"></el-option>
                  <el-option label="风险报告" value="RISK"></el-option>
                  <el-option label="财务报告" value="FINANCIAL"></el-option>
                  <el-option label="合规报告" value="COMPLIANCE"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="报告状态">
                <el-select v-model="reportsQuery.status" placeholder="请选择状态" clearable style="width: 120px">
                  <el-option label="草稿" value="DRAFT"></el-option>
                  <el-option label="审核中" value="REVIEWING"></el-option>
                  <el-option label="已发布" value="PUBLISHED"></el-option>
                  <el-option label="已归档" value="ARCHIVED"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="创建时间">
                <el-date-picker
                  v-model="reportsQuery.dateRange"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  style="width: 240px"
                ></el-date-picker>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="getReportsList" icon="el-icon-search">查询</el-button>
                <el-button @click="resetReportsQuery" icon="el-icon-refresh">重置</el-button>
                <el-button type="success" @click="handleAddReport" icon="el-icon-plus">新建报告</el-button>
                <el-button type="warning" @click="handleGenerateReport" icon="el-icon-magic-stick">智能生成</el-button>
                <el-button type="info" @click="handleExportReports" icon="el-icon-download">导出</el-button>
              </el-form-item>
            </el-form>

            <!-- 报告列表表格 -->
            <el-table v-loading="reportsLoading" :data="reportsList" stripe border style="width: 100%">
              <el-table-column type="selection" width="55" align="center"></el-table-column>
              <el-table-column prop="reportTitle" label="报告标题" min-width="250" show-overflow-tooltip></el-table-column>
              <el-table-column prop="reportType" label="报告类型" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getReportTypeTag(scope.row.reportType)">
                    {{ getReportTypeText(scope.row.reportType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="reportPeriod" label="报告期间" width="120" align="center"></el-table-column>
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getStatusTag(scope.row.status)">
                    {{ getStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createBy" label="创建人" width="120" align="center"></el-table-column>
              <el-table-column prop="createTime" label="创建时间" width="160" align="center"></el-table-column>
              <el-table-column prop="downloadCount" label="下载次数" width="100" align="center"></el-table-column>
              <el-table-column label="操作" width="320" align="center" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" @click="handleViewReport(scope.row)" icon="el-icon-view">查看</el-button>
                  <el-button size="mini" type="primary" @click="handleEditReport(scope.row)" icon="el-icon-edit">编辑</el-button>
                  <el-button size="mini" type="success" @click="handlePreviewReport(scope.row)" icon="el-icon-document">预览</el-button>
                  <el-button size="mini" type="warning" @click="handlePublishReport(scope.row)" icon="el-icon-upload2">发布</el-button>
                  <el-button size="mini" type="info" @click="handleDownloadReport(scope.row)" icon="el-icon-download">下载</el-button>
                  <el-button size="mini" type="danger" @click="handleDeleteReport(scope.row)" icon="el-icon-delete">删除</el-button>
                </template>
              </el-table-column>
            </el-table>

            <!-- 分页组件 -->
            <pagination
              v-show="reportsTotal > 0"
              :total="reportsTotal"
              :page.sync="reportsQuery.pageNum"
              :limit.sync="reportsQuery.pageSize"
              @pagination="getReportsList"
            />
          </div>
        </el-tab-pane>

        <!-- 报告模板 -->
        <el-tab-pane label="报告模板" name="templates">
          <div class="tab-content">
            <!-- 查询表单 -->
            <el-form :model="templatesQuery" ref="templatesForm" :inline="true" label-width="100px" class="mb-20">
              <el-form-item label="模板名称">
                <el-input v-model="templatesQuery.templateName" placeholder="请输入模板名称" style="width: 200px"></el-input>
              </el-form-item>
              <el-form-item label="模板类型">
                <el-select v-model="templatesQuery.templateType" placeholder="请选择模板类型" clearable style="width: 150px">
                  <el-option label="监管报告模板" value="SUPERVISION"></el-option>
                  <el-option label="风险报告模板" value="RISK"></el-option>
                  <el-option label="财务报告模板" value="FINANCIAL"></el-option>
                  <el-option label="合规报告模板" value="COMPLIANCE"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="getTemplatesList" icon="el-icon-search">查询</el-button>
                <el-button @click="resetTemplatesQuery" icon="el-icon-refresh">重置</el-button>
                <el-button type="success" @click="handleAddTemplate" icon="el-icon-plus">新建模板</el-button>
                <el-button type="info" @click="handleExportTemplates" icon="el-icon-download">导出</el-button>
              </el-form-item>
            </el-form>

            <!-- 模板列表表格 -->
            <el-table v-loading="templatesLoading" :data="templatesList" stripe border style="width: 100%">
              <el-table-column type="selection" width="55" align="center"></el-table-column>
              <el-table-column prop="templateName" label="模板名称" min-width="200" show-overflow-tooltip></el-table-column>
              <el-table-column prop="templateType" label="模板类型" width="150" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getTemplateTypeTag(scope.row.templateType)">
                    {{ getTemplateTypeText(scope.row.templateType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="version" label="版本" width="100" align="center"></el-table-column>
              <el-table-column prop="usageCount" label="使用次数" width="100" align="center"></el-table-column>
              <el-table-column prop="status" label="状态" width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getStatusTag(scope.row.status)">
                    {{ getStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createBy" label="创建人" width="120" align="center"></el-table-column>
              <el-table-column prop="createTime" label="创建时间" width="160" align="center"></el-table-column>
              <el-table-column label="操作" width="280" align="center" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" @click="handleViewTemplate(scope.row)" icon="el-icon-view">查看</el-button>
                  <el-button size="mini" type="primary" @click="handleEditTemplate(scope.row)" icon="el-icon-edit">编辑</el-button>
                  <el-button size="mini" type="success" @click="handleUseTemplate(scope.row)" icon="el-icon-document-add">使用</el-button>
                  <el-button size="mini" type="warning" @click="handleCopyTemplate(scope.row)" icon="el-icon-document-copy">复制</el-button>
                  <el-button size="mini" type="danger" @click="handleDeleteTemplate(scope.row)" icon="el-icon-delete">删除</el-button>
                </template>
              </el-table-column>
            </el-table>

            <!-- 分页组件 -->
            <pagination
              v-show="templatesTotal > 0"
              :total="templatesTotal"
              :page.sync="templatesQuery.pageNum"
              :limit.sync="templatesQuery.pageSize"
              @pagination="getTemplatesList"
            />
          </div>
        </el-tab-pane>

        <!-- 报告分发 -->
        <el-tab-pane label="报告分发" name="distribution">
          <div class="tab-content">
            <!-- 分发配置 -->
            <el-card class="mb-20">
              <div slot="header" class="card-header">
                <span>分发配置</span>
              </div>
              <el-form :model="distributionForm" :inline="true" label-width="120px">
                <el-form-item label="选择报告">
                  <el-select v-model="distributionForm.reportId" placeholder="请选择报告" style="width: 250px">
                    <el-option
                      v-for="report in publishedReports"
                      :key="report.id"
                      :label="report.title"
                      :value="report.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="分发方式">
                  <el-checkbox-group v-model="distributionForm.methods">
                    <el-checkbox label="EMAIL">邮件发送</el-checkbox>
                    <el-checkbox label="SYSTEM">系统通知</el-checkbox>
                    <el-checkbox label="DOWNLOAD">下载链接</el-checkbox>
                    <el-checkbox label="PRINT">打印分发</el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
                <el-form-item label="分发对象">
                  <el-select v-model="distributionForm.recipients" multiple placeholder="请选择分发对象" style="width: 300px">
                    <el-option label="监管部门" value="SUPERVISION_DEPT"></el-option>
                    <el-option label="企业管理层" value="ENTERPRISE_MANAGEMENT"></el-option>
                    <el-option label="财务部门" value="FINANCE_DEPT"></el-option>
                    <el-option label="风险管理部门" value="RISK_DEPT"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleStartDistribution" icon="el-icon-s-promotion">开始分发</el-button>
                  <el-button type="success" @click="handleScheduleDistribution" icon="el-icon-time">定时分发</el-button>
                </el-form-item>
              </el-form>
            </el-card>

            <!-- 分发记录 -->
            <el-card>
              <div slot="header" class="card-header">
                <span>分发记录</span>
                <el-button type="text" @click="refreshDistributionRecords">刷新</el-button>
              </div>
              
              <el-table :data="distributionRecords" stripe border style="width: 100%;">
                <el-table-column prop="reportTitle" label="报告标题" min-width="200" show-overflow-tooltip></el-table-column>
                <el-table-column prop="distributionMethod" label="分发方式" width="120" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getDistributionMethodTag(scope.row.distributionMethod)">
                      {{ getDistributionMethodText(scope.row.distributionMethod) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="recipientCount" label="分发数量" width="100" align="center"></el-table-column>
                <el-table-column prop="successCount" label="成功数量" width="100" align="center"></el-table-column>
                <el-table-column prop="failureCount" label="失败数量" width="100" align="center"></el-table-column>
                <el-table-column prop="distributionStatus" label="分发状态" width="120" align="center">
                  <template slot-scope="scope">
                    <el-tag :type="getDistributionStatusTag(scope.row.distributionStatus)">
                      {{ getDistributionStatusText(scope.row.distributionStatus) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="distributionTime" label="分发时间" width="160" align="center"></el-table-column>
                <el-table-column label="操作" width="200" align="center">
                  <template slot-scope="scope">
                    <el-button size="mini" @click="handleViewDistributionDetail(scope.row)" icon="el-icon-view">查看详情</el-button>
                    <el-button size="mini" type="warning" @click="handleRetryDistribution(scope.row)" icon="el-icon-refresh" v-if="scope.row.failureCount > 0">重试</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </div>
        </el-tab-pane>

        <!-- 报告统计 -->
        <el-tab-pane label="报告统计" name="statistics">
          <div class="tab-content">
            <!-- 统计图表 -->
            <el-row :gutter="20" class="mb-20">
              <el-col :span="12">
                <el-card>
                  <div slot="header" class="card-header">
                    <span>报告类型分布</span>
                  </div>
                  <div ref="reportTypeChart" style="height: 300px;"></div>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card>
                  <div slot="header" class="card-header">
                    <span>报告生成趋势</span>
                  </div>
                  <div ref="reportTrendChart" style="height: 300px;"></div>
                </el-card>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <el-col :span="12">
                <el-card>
                  <div slot="header" class="card-header">
                    <span>报告下载统计</span>
                  </div>
                  <div ref="downloadStatsChart" style="height: 300px;"></div>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card>
                  <div slot="header" class="card-header">
                    <span>报告质量评分</span>
                  </div>
                  <div ref="qualityScoreChart" style="height: 300px;"></div>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 对话框组件 -->
    <SupervisionReportDialog
      :visible.sync="reportDialogVisible"
      :report-data="currentReport"
      :dialog-type="dialogType"
      @refresh="getReportsList"
    />

    <ReportTemplateDialog
      :visible.sync="templateDialogVisible"
      :template-data="currentTemplate"
      :dialog-type="dialogType"
      @refresh="getTemplatesList"
    />

    <ReportPreviewDialog
      :visible.sync="previewDialogVisible"
      :report-data="currentReport"
    />

    <DistributionDetailDialog
      :visible.sync="distributionDetailVisible"
      :distribution-data="currentDistribution"
    />
  </div>
</template>

<script>
import { getSupervisionReportStatistics, getSupervisionReportsList, getReportTemplatesList, getDistributionRecords } from '@/api/stateAssets/supervisionReport'
import Pagination from '@/components/Pagination'
import SupervisionReportDialog from './components/SupervisionReportDialog'
import ReportTemplateDialog from './components/ReportTemplateDialog'
import ReportPreviewDialog from './components/ReportPreviewDialog'
import DistributionDetailDialog from './components/DistributionDetailDialog'
import * as echarts from 'echarts'

export default {
  name: 'SupervisionReport',
  components: {
    Pagination,
    SupervisionReportDialog,
    ReportTemplateDialog,
    ReportPreviewDialog,
    DistributionDetailDialog
  },
  data() {
    return {
      activeTab: 'reports',
      statistics: {},
      
      // 报告管理相关
      reportsLoading: false,
      reportsList: [],
      reportsTotal: 0,
      reportsQuery: {
        pageNum: 1,
        pageSize: 10,
        reportTitle: '',
        reportType: '',
        status: '',
        dateRange: []
      },
      
      // 报告模板相关
      templatesLoading: false,
      templatesList: [],
      templatesTotal: 0,
      templatesQuery: {
        pageNum: 1,
        pageSize: 10,
        templateName: '',
        templateType: ''
      },
      
      // 报告分发相关
      distributionForm: {
        reportId: '',
        methods: [],
        recipients: []
      },
      distributionRecords: [],
      publishedReports: [],
      
      // 对话框相关
      reportDialogVisible: false,
      templateDialogVisible: false,
      previewDialogVisible: false,
      distributionDetailVisible: false,
      dialogType: 'add',
      currentReport: {},
      currentTemplate: {},
      currentDistribution: {}
    }
  },
  created() {
    this.getStatistics()
    this.getReportsList()
    this.initCharts()
  },
  methods: {
    // 获取统计数据
    getStatistics() {
      getSupervisionReportStatistics().then(response => {
        if (response.code === 1) {
          this.statistics = response.data || {}
        }
      }).catch(error => {
        console.error('获取统计数据失败:', error)
      })
    },

    // 报告管理相关方法
    getReportsList() {
      this.reportsLoading = true
      getSupervisionReportsList(this.reportsQuery).then(response => {
        if (response.code === 1) {
          this.reportsList = response.data.list || []
          this.reportsTotal = response.data.total || 0
          this.publishedReports = this.reportsList.filter(report => report.status === 'PUBLISHED')
        } else {
          this.$message.error(response.msg || '获取监管报告列表失败')
        }
        this.reportsLoading = false
      }).catch(error => {
        console.error('获取监管报告列表异常:', error)
        this.$message.warning('获取数据暂未开放')
        this.reportsLoading = false
      })
    },

    resetReportsQuery() {
      this.$refs.reportsForm.resetFields()
      this.reportsQuery = {
        pageNum: 1,
        pageSize: 10,
        reportTitle: '',
        reportType: '',
        status: '',
        dateRange: []
      }
      this.getReportsList()
    },

    handleAddReport() {
      this.currentReport = {}
      this.dialogType = 'add'
      this.reportDialogVisible = true
    },

    handleViewReport(row) {
      this.currentReport = { ...row }
      this.dialogType = 'view'
      this.reportDialogVisible = true
    },

    handleEditReport(row) {
      this.currentReport = { ...row }
      this.dialogType = 'edit'
      this.reportDialogVisible = true
    },

    handlePreviewReport(row) {
      this.currentReport = { ...row }
      this.previewDialogVisible = true
    },

    handlePublishReport(row) {
      this.$confirm(`确定要发布报告"${row.reportTitle}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('报告发布成功')
        this.getReportsList()
      }).catch(() => {
          // 用户取消操作
        })
      },

    handleDownloadReport(row) {
      this.$message.success(`正在下载报告：${row.reportTitle}`)
    },

    handleDeleteReport(row) {
      this.$confirm(`确定要删除报告"${row.reportTitle}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.getReportsList()
      }).catch(() => {
          // 用户取消操作
        })
      },

    handleGenerateReport() {
      this.$message.success('正在智能生成报告...')
    },

    handleExportReports() {
      this.$message.success('正在导出监管报告数据...')
    },

    // 报告模板相关方法
    getTemplatesList() {
      this.templatesLoading = true
      getReportTemplatesList(this.templatesQuery).then(response => {
        if (response.code === 1) {
          this.templatesList = response.data.list || []
          this.templatesTotal = response.data.total || 0
        } else {
          this.$message.error(response.msg || '获取报告模板列表失败')
        }
        this.templatesLoading = false
      }).catch(error => {
        console.error('获取报告模板列表异常:', error)
        this.$message.warning('获取数据暂未开放')
        this.templatesLoading = false
      })
    },

    resetTemplatesQuery() {
      this.$refs.templatesForm.resetFields()
      this.templatesQuery = {
        pageNum: 1,
        pageSize: 10,
        templateName: '',
        templateType: ''
      }
      this.getTemplatesList()
    },

    handleAddTemplate() {
      this.currentTemplate = {}
      this.dialogType = 'add'
      this.templateDialogVisible = true
    },

    handleViewTemplate(row) {
      this.currentTemplate = { ...row }
      this.dialogType = 'view'
      this.templateDialogVisible = true
    },

    handleEditTemplate(row) {
      this.currentTemplate = { ...row }
      this.dialogType = 'edit'
      this.templateDialogVisible = true
    },

    handleUseTemplate(row) {
      this.$message.success(`正在使用模板：${row.templateName}`)
    },

    handleCopyTemplate(row) {
      this.$message.success(`正在复制模板：${row.templateName}`)
    },

    handleDeleteTemplate(row) {
      this.$confirm(`确定要删除模板"${row.templateName}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.getTemplatesList()
      }).catch(() => {
          // 用户取消操作
        })
      },

    handleExportTemplates() {
      this.$message.success('正在导出报告模板数据...')
    },

    // 报告分发相关方法
    handleStartDistribution() {
      if (!this.distributionForm.reportId || !this.distributionForm.methods.length || !this.distributionForm.recipients.length) {
        this.$message.warning('请完善分发配置信息')
        return
      }
      this.$message.success('正在开始分发报告...')
      this.getDistributionRecords()
    },

    handleScheduleDistribution() {
      this.$message.success('正在设置定时分发...')
    },

    getDistributionRecords() {
      getDistributionRecords().then(response => {
        if (response.code === 1) {
          this.distributionRecords = response.data.list || []
        }
      }).catch(error => {
        console.error('获取分发记录失败:', error)
      })
    },

    refreshDistributionRecords() {
      this.getDistributionRecords()
      this.$message.success('分发记录已刷新')
    },

    handleViewDistributionDetail(row) {
      this.currentDistribution = { ...row }
      this.distributionDetailVisible = true
    },

    handleRetryDistribution(row) {
      this.$message.success(`正在重试分发：${row.reportTitle}`)
    },

    // 图表初始化
    initCharts() {
      this.$nextTick(() => {
        this.initReportTypeChart()
        this.initReportTrendChart()
        this.initDownloadStatsChart()
        this.initQualityScoreChart()
      })
    },

    initReportTypeChart() {
      if (!this.$refs.reportTypeChart) return
      const chart = echarts.init(this.$refs.reportTypeChart)
      const option = {
        tooltip: { trigger: 'item' },
        series: [{
          type: 'pie',
          radius: '70%',
          data: [
            { value: 35, name: '监管报告' },
            { value: 25, name: '风险报告' },
            { value: 20, name: '财务报告' },
            { value: 20, name: '合规报告' }
          ]
        }]
      }
      chart.setOption(option)
    },

    initReportTrendChart() {
      if (!this.$refs.reportTrendChart) return
      const chart = echarts.init(this.$refs.reportTrendChart)
      const option = {
        tooltip: { trigger: 'axis' },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月']
        },
        yAxis: { type: 'value' },
        series: [{
          name: '报告数量',
          type: 'line',
          data: [12, 15, 18, 22, 25, 28],
          smooth: true
        }]
      }
      chart.setOption(option)
    },

    initDownloadStatsChart() {
      if (!this.$refs.downloadStatsChart) return
      const chart = echarts.init(this.$refs.downloadStatsChart)
      const option = {
        tooltip: { trigger: 'axis' },
        xAxis: {
          type: 'category',
          data: ['监管报告', '风险报告', '财务报告', '合规报告']
        },
        yAxis: { type: 'value' },
        series: [{
          name: '下载次数',
          type: 'bar',
          data: [120, 85, 95, 75]
        }]
      }
      chart.setOption(option)
    },

    initQualityScoreChart() {
      if (!this.$refs.qualityScoreChart) return
      const chart = echarts.init(this.$refs.qualityScoreChart)
      const option = {
        tooltip: { trigger: 'axis' },
        radar: {
          indicator: [
            { name: '内容完整性', max: 100 },
            { name: '数据准确性', max: 100 },
            { name: '分析深度', max: 100 },
            { name: '可读性', max: 100 },
            { name: '时效性', max: 100 }
          ]
        },
        series: [{
          type: 'radar',
          data: [{
            value: [85, 90, 88, 92, 87],
            name: '报告质量评分'
          }]
        }]
      }
      chart.setOption(option)
    },

    // 工具方法
    getReportTypeTag(type) {
      const tagMap = {
        'SUPERVISION': 'primary',
        'RISK': 'danger',
        'FINANCIAL': 'success',
        'COMPLIANCE': 'warning'
      }
      return tagMap[type] || 'info'
    },

    getReportTypeText(type) {
      const textMap = {
        'SUPERVISION': '监管报告',
        'RISK': '风险报告',
        'FINANCIAL': '财务报告',
        'COMPLIANCE': '合规报告'
      }
      return textMap[type] || type
    },

    getStatusTag(status) {
      const tagMap = {
        'DRAFT': 'info',
        'REVIEWING': 'warning',
        'PUBLISHED': 'success',
        'ARCHIVED': 'info'
      }
      return tagMap[status] || 'info'
    },

    getStatusText(status) {
      const textMap = {
        'DRAFT': '草稿',
        'REVIEWING': '审核中',
        'PUBLISHED': '已发布',
        'ARCHIVED': '已归档'
      }
      return textMap[status] || status
    },

    getTemplateTypeTag(type) {
      const tagMap = {
        'SUPERVISION': 'primary',
        'RISK': 'danger',
        'FINANCIAL': 'success',
        'COMPLIANCE': 'warning'
      }
      return tagMap[type] || 'info'
    },

    getTemplateTypeText(type) {
      const textMap = {
        'SUPERVISION': '监管报告模板',
        'RISK': '风险报告模板',
        'FINANCIAL': '财务报告模板',
        'COMPLIANCE': '合规报告模板'
      }
      return textMap[type] || type
    },

    getDistributionMethodTag(method) {
      const tagMap = {
        'EMAIL': 'primary',
        'SYSTEM': 'success',
        'DOWNLOAD': 'warning',
        'PRINT': 'info'
      }
      return tagMap[method] || 'info'
    },

    getDistributionMethodText(method) {
      const textMap = {
        'EMAIL': '邮件发送',
        'SYSTEM': '系统通知',
        'DOWNLOAD': '下载链接',
        'PRINT': '打印分发'
      }
      return textMap[method] || method
    },

    getDistributionStatusTag(status) {
      const tagMap = {
        'SUCCESS': 'success',
        'FAILED': 'danger',
        'PARTIAL': 'warning',
        'PENDING': 'info'
      }
      return tagMap[status] || 'info'
    },

    getDistributionStatusText(status) {
      const textMap = {
        'SUCCESS': '分发成功',
        'FAILED': '分发失败',
        'PARTIAL': '部分成功',
        'PENDING': '分发中'
      }
      return textMap[status] || status
    }
  }
}
</script>

<style scoped>
.statistics-card {
  margin-bottom: 20px;
}

.statistics-content {
  display: flex;
  align-items: center;
}

.statistics-icon {
  font-size: 40px;
  margin-right: 20px;
}

.statistics-info {
  flex: 1;
}

.statistics-number {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.statistics-label {
  font-size: 14px;
  color: #909399;
  margin-top: 5px;
}

.tab-content {
  padding: 20px 0;
}

.mb-20 {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
