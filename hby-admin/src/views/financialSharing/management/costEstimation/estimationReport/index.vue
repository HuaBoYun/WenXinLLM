<template>
  <div class="estimation-report-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-document-copy"></i>
          估算报告管理
        </h1>
        <p class="page-description">生成、查看和管理各类成本估算分析报告</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="handleGenerateReport">
          生成报告
        </el-button>
        <el-button type="success" icon="el-icon-view" @click="handleReportTemplate">
          报告模板
        </el-button>
        <el-button type="warning" icon="el-icon-download" @click="handleBatchExport">
          批量导出
        </el-button>
      </div>
    </div>

    <!-- 搜索条件 -->
    <div class="search-container">
      <el-form :model="queryForm" ref="queryForm" :inline="true" class="search-form">
        <el-form-item label="报告名称" prop="reportName">
          <el-input
            v-model="queryForm.reportName"
            placeholder="请输入报告名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="报告类型" prop="reportType">
          <el-select
            v-model="queryForm.reportType"
            placeholder="请选择报告类型"
            clearable
            style="width: 180px"
          >
            <el-option label="成本估算报告" :value="1" />
            <el-option label="差异分析报告" :value="2" />
            <el-option label="预算执行报告" :value="3" />
            <el-option label="成本趋势报告" :value="4" />
            <el-option label="综合分析报告" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="报告期间" prop="reportPeriod">
          <el-date-picker
            v-model="queryForm.reportPeriod"
            type="monthrange"
            range-separator="至"
            start-placeholder="开始月份"
            end-placeholder="结束月份"
            format="yyyy-MM"
            value-format="yyyy-MM"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="生成状态" prop="status">
          <el-select
            v-model="queryForm.status"
            placeholder="请选择生成状态"
            clearable
            style="width: 120px"
          >
            <el-option label="生成中" :value="1" />
            <el-option label="已完成" :value="2" />
            <el-option label="生成失败" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">
            查询
          </el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="reportCode" label="报告编号" width="150" />
        <el-table-column prop="reportName" label="报告名称" min-width="200" />
        <el-table-column prop="reportTypeName" label="报告类型" width="120" />
        <el-table-column prop="reportPeriod" label="报告期间" width="180" />
        <el-table-column prop="dataSource" label="数据来源" width="120" />
        <el-table-column prop="fileSize" label="文件大小" width="100" align="right" />
        <el-table-column prop="statusName" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ scope.row.statusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="generateTime" label="生成时间" width="180" />
        <el-table-column prop="creatorName" label="生成人" width="100" />
        <el-table-column label="操作" width="250" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handlePreview(scope.row)">
              预览
            </el-button>
            <el-button size="mini" type="text" @click="handleDownload(scope.row)">
              下载
            </el-button>
            <el-button size="mini" type="text" @click="handleShare(scope.row)">
              分享
            </el-button>
            <el-button 
              size="mini" 
              type="text" 
              @click="handleRegenerate(scope.row)"
              :disabled="scope.row.status === 1"
            >
              重新生成
            </el-button>
            <el-button size="mini" type="text" class="danger-text" @click="handleDelete(scope.row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页组件 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="queryForm.pageNumber"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="queryForm.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        />
      </div>
    </div>

    <!-- 生成报告对话框 -->
    <el-dialog
      title="生成估算报告"
      :visible.sync="generateDialogVisible"
      width="800px"
      :close-on-click-modal="false"
      @close="handleGenerateDialogClose"
    >
      <el-form
        :model="generateForm"
        :rules="generateRules"
        ref="generateFormRef"
        label-width="120px"
        class="generate-form"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="报告名称" prop="reportName">
              <el-input
                v-model="generateForm.reportName"
                placeholder="请输入报告名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告类型" prop="reportType">
              <el-select
                v-model="generateForm.reportType"
                placeholder="请选择报告类型"
                style="width: 100%"
              >
                <el-option label="成本估算报告" :value="1" />
                <el-option label="差异分析报告" :value="2" />
                <el-option label="预算执行报告" :value="3" />
                <el-option label="成本趋势报告" :value="4" />
                <el-option label="综合分析报告" :value="5" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="报告期间" prop="reportPeriod">
              <el-date-picker
                v-model="generateForm.reportPeriod"
                type="monthrange"
                range-separator="至"
                start-placeholder="开始月份"
                end-placeholder="结束月份"
                format="yyyy-MM"
                value-format="yyyy-MM"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据来源" prop="dataSource">
              <el-select
                v-model="generateForm.dataSource"
                placeholder="请选择数据来源"
                style="width: 100%"
              >
                <el-option label="成本估算系统" value="estimation" />
                <el-option label="预算管理系统" value="budget" />
                <el-option label="财务核算系统" value="accounting" />
                <el-option label="综合数据" value="comprehensive" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="输出格式" prop="outputFormat">
              <el-checkbox-group v-model="generateForm.outputFormat">
                <el-checkbox label="PDF">PDF</el-checkbox>
                <el-checkbox label="Excel">Excel</el-checkbox>
                <el-checkbox label="Word">Word</el-checkbox>
                <el-checkbox label="HTML">HTML</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报告模板" prop="templateId">
              <el-select
                v-model="generateForm.templateId"
                placeholder="请选择报告模板"
                style="width: 100%"
              >
                <el-option label="标准模板" :value="1" />
                <el-option label="详细模板" :value="2" />
                <el-option label="简化模板" :value="3" />
                <el-option label="自定义模板" :value="4" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="包含内容" prop="includeContent">
          <el-checkbox-group v-model="generateForm.includeContent">
            <el-checkbox label="executive_summary">执行摘要</el-checkbox>
            <el-checkbox label="cost_breakdown">成本分解</el-checkbox>
            <el-checkbox label="variance_analysis">差异分析</el-checkbox>
            <el-checkbox label="trend_analysis">趋势分析</el-checkbox>
            <el-checkbox label="recommendations">建议措施</el-checkbox>
            <el-checkbox label="appendix">附录数据</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="筛选条件" prop="filterConditions">
          <el-input
            v-model="generateForm.filterConditions"
            type="textarea"
            :rows="3"
            placeholder="请输入筛选条件，如：成本中心、产品类别、金额范围等"
          />
        </el-form-item>
        <el-form-item label="备注说明" prop="remark">
          <el-input
            v-model="generateForm.remark"
            type="textarea"
            :rows="2"
            placeholder="请输入备注说明"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="generateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitGenerate" :loading="generateLoading">
          开始生成
        </el-button>
      </div>
    </el-dialog>

    <!-- 报告预览对话框 -->
    <el-dialog
      title="报告预览"
      :visible.sync="previewDialogVisible"
      width="90%"
      :close-on-click-modal="false"
      custom-class="preview-dialog"
    >
      <div class="preview-container" v-if="previewData">
        <div class="preview-header">
          <h2>{{ previewData.reportName }}</h2>
          <div class="preview-meta">
            <span>报告期间：{{ previewData.reportPeriod }}</span>
            <span>生成时间：{{ previewData.generateTime }}</span>
            <span>数据来源：{{ previewData.dataSource }}</span>
          </div>
        </div>

        <div class="preview-content">
          <!-- 执行摘要 -->
          <div class="content-section" v-if="previewData.executiveSummary">
            <h3>执行摘要</h3>
            <p>{{ previewData.executiveSummary }}</p>
          </div>

          <!-- 成本概览 -->
          <div class="content-section" v-if="previewData.costOverview">
            <h3>成本概览</h3>
            <el-row :gutter="20">
              <el-col :span="6" v-for="(item, index) in previewData.costOverview" :key="index">
                <div class="overview-card">
                  <div class="card-value">{{ item.value }}</div>
                  <div class="card-label">{{ item.label }}</div>
                </div>
              </el-col>
            </el-row>
          </div>

          <!-- 成本分解表 -->
          <div class="content-section" v-if="previewData.costBreakdown">
            <h3>成本分解</h3>
            <el-table :data="previewData.costBreakdown" border stripe>
              <el-table-column prop="category" label="成本类别" width="150" />
              <el-table-column prop="budgetAmount" label="预算金额" width="120" align="right" />
              <el-table-column prop="actualAmount" label="实际金额" width="120" align="right" />
              <el-table-column prop="variance" label="差异" width="120" align="right" />
              <el-table-column prop="varianceRate" label="差异率" width="100" align="right" />
              <el-table-column prop="remark" label="备注" min-width="200" />
            </el-table>
          </div>

          <!-- 图表分析 -->
          <div class="content-section" v-if="previewData.charts">
            <h3>图表分析</h3>
            <div class="charts-container">
              <div class="chart-item" v-for="(chart, index) in previewData.charts" :key="index">
                <h4>{{ chart.title }}</h4>
                <div :id="'chart-' + index" class="chart-placeholder">
                  [图表占位符 - {{ chart.type }}]
                </div>
              </div>
            </div>
          </div>

          <!-- 建议措施 -->
          <div class="content-section" v-if="previewData.recommendations">
            <h3>建议措施</h3>
            <ol>
              <li v-for="(recommendation, index) in previewData.recommendations" :key="index">
                {{ recommendation }}
              </li>
            </ol>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="previewDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleDownloadFromPreview">
          下载报告
        </el-button>
      </div>
    </el-dialog>

    <!-- 报告模板对话框：从已有报告复制为模板，避免假按钮 -->
    <el-dialog
      title="报告模板库"
      :visible.sync="templateDialogVisible"
      width="900px"
      :close-on-click-modal="false"
      append-to-body
    >
      <div class="template-tip">
        <i class="el-icon-info" />
        从已有报告复制为模板：选定一行后，会以该报告的字段为基础打开"生成报告"对话框（编号留空 + 名称加"（副本）"）。
      </div>
      <el-table
        v-loading="templateLoading"
        :data="templateData"
        border
        stripe
        height="420"
        style="width: 100%"
      >
        <el-table-column prop="reportCode" label="报告编号" width="160" />
        <el-table-column prop="reportName" label="报告名称" min-width="200" />
        <el-table-column prop="reportTypeName" label="报告类型" width="140" />
        <el-table-column prop="reportPeriod" label="报告期间" width="160" />
        <el-table-column prop="statusName" label="状态" width="100" align="center" />
        <el-table-column prop="generateTime" label="生成时间" width="170" />
        <el-table-column label="操作" width="140" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleApplyTemplate(scope.row)">
              复制为模板
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button @click="templateDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getEstimationReportPage,
  generateEstimationReport,
  exportCostEstimateDataBlob,
  deleteEstimationReport,
  getEstimationReportById
} from '@/api/financialSharing/costEstimation'
import { normalizeKeysArray, normalizeKeys } from '@/utils/keyNormalize'

export default {
  name: 'EstimationReport',
  data() {
    return {
      loading: false,
      generateLoading: false,
      tableData: [],
      total: 0,
      selectedRows: [],
      queryForm: {
        pageNumber: 1,
        pageSize: 20,
        reportName: '',
        reportType: null,
        reportPeriod: [],
        status: null
      },
      generateDialogVisible: false,
      previewDialogVisible: false,
      templateDialogVisible: false,
      templateLoading: false,
      templateData: [],
      generateForm: {
        reportName: '',
        reportType: null,
        reportPeriod: [],
        dataSource: '',
        outputFormat: ['PDF'],
        templateId: 1,
        includeContent: ['executive_summary', 'cost_breakdown', 'variance_analysis'],
        filterConditions: '',
        remark: ''
      },
      previewData: null,
      generateRules: {
        reportName: [
          { required: true, message: '请输入报告名称', trigger: 'blur' }
        ],
        reportType: [
          { required: true, message: '请选择报告类型', trigger: 'change' }
        ],
        reportPeriod: [
          { required: true, message: '请选择报告期间', trigger: 'change' }
        ],
        dataSource: [
          { required: true, message: '请选择数据来源', trigger: 'change' }
        ],
        outputFormat: [
          { required: true, message: '请选择输出格式', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    // 获取数据
    async fetchData() {
      this.loading = true
      try {
        const response = await getEstimationReportPage(this.queryForm)

        // 后端原生返回 code=1，老代码错把 200 当成功，导致表格永远不渲染
        if (response && (response.code === 1 || response.code === 200)) {
          // 后端返回的列表数据在 tlist 中，total 在 totalRecord 中
          const rawList = response.data.tlist || response.data.records || []
          // 防御达梦/Oracle 返回 ALLCAPS 字段名（如 REPORTNO/REPORTSTATUS）
          this.tableData = normalizeKeysArray(rawList)
          this.total = response.data.totalRecord || response.data.total || 0

          // 处理数据格式 + 字段映射兜底
          this.tableData.forEach(item => {
            // 报告类型名称映射（兼容字符串枚举与历史数字编码）
            const typeMap = {
              COST_ESTIMATION: '成本估算报告',
              BUDGET_ANALYSIS: '预算分析报告',
              VARIANCE_ANALYSIS: '差异分析报告',
              COMPREHENSIVE: '综合报告',
              1: '成本估算报告',
              2: '预算分析报告',
              3: '差异分析报告',
              4: '综合报告'
            }
            item.reportTypeName = typeMap[item.reportType] || (item.reportType || '未知')

            // 报告状态名称映射（同时兼容字符串枚举与数字编码）
            const statusMap = {
              GENERATED: '已生成', PUBLISHED: '已发布', ARCHIVED: '已归档',
              1: '已生成', 2: '已发布', 3: '已归档'
            }
            item.reportStatusName = statusMap[item.reportStatus] || (item.reportStatus || '未知')

            // 字段读侧兜底：后端命名 vs 前端表格列 prop 命名分裂
            //   后端 reportNo / generationTime / generatorName / reportStatus
            //   前端 reportCode / generateTime / creatorName / status
            if (!item.reportCode) item.reportCode = item.reportNo || ''
            if (!item.generateTime) item.generateTime = item.generationTime || ''
            if (!item.creatorName) item.creatorName = item.generatorName || ''
            if (item.status == null) item.status = item.reportStatus
            // statusName 给 el-tag 模板用（表格列 prop="statusName"）
            item.statusName = item.reportStatusName

            // 后端没返回这两列——给空字符串避免显示 undefined
            if (item.dataSource == null) item.dataSource = ''
            if (item.fileSize == null) item.fileSize = ''
          })
        } else if (response && response.msg) {
          this.$message.error(response.msg)
        }

      } catch (error) {
        this.$message.error('获取数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 查询
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.fetchData()
    },

    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      // 复位分页避免在小页码上停留导致空数据
      this.queryForm.pageNumber = 1
      this.fetchData()
    },

    // 生成报告
    handleGenerateReport() {
      this.resetGenerateForm()
      this.generateDialogVisible = true
    },

    // 提交生成
    async handleSubmitGenerate() {
      try {
        await this.$refs.generateFormRef.validate()
        this.generateLoading = true

        await generateEstimationReport(this.generateForm)

        this.$message.success('报告生成任务已提交，请稍后查看生成结果')
        this.generateDialogVisible = false
        this.fetchData()
      } catch (error) {
        if (error !== false) {
          this.$message.error('生成失败：' + error.message)
        }
      } finally {
        this.generateLoading = false
      }
    },

    // 预览报告
    async handlePreview(row) {
      try {
        // 改用 /report/getById：直接按 reportId 拿完整报告（含 reportContent / dataSource / fileSize 等）
        const response = await getEstimationReportById(row.reportId)

        if (response && (response.code === 1 || response.code === 200) && response.data) {
          const detail = normalizeKeys(response.data) || {}
          // 兼容 reportContent 存的是 JSON 字符串：尝试解析成结构化数据
          let contentObj = {}
          if (typeof detail.reportContent === 'string' && detail.reportContent.trim().startsWith('{')) {
            try { contentObj = JSON.parse(detail.reportContent) } catch (_) { contentObj = {} }
          } else if (detail.reportContent && typeof detail.reportContent === 'object') {
            contentObj = detail.reportContent
          }
          this.previewData = {
            reportName: detail.reportName || row.reportName || '',
            reportPeriod: detail.reportPeriod || row.reportPeriod || '',
            generateTime: detail.generationTime || row.generateTime || '',
            dataSource: detail.dataSource || row.dataSource || '',
            fileSize: detail.fileSize || row.fileSize || '',
            executiveSummary: contentObj.executiveSummary || detail.reportSummary || '',
            costOverview: Array.isArray(contentObj.costOverview) ? contentObj.costOverview : [],
            costBreakdown: Array.isArray(contentObj.costBreakdown) ? contentObj.costBreakdown : [],
            charts: Array.isArray(contentObj.charts) ? contentObj.charts : [],
            recommendations: Array.isArray(contentObj.recommendations) ? contentObj.recommendations : []
          }
        } else {
          // 后端返回失败：用列表行基本信息兜底
          this.previewData = {
            reportName: row.reportName,
            reportPeriod: row.reportPeriod,
            generateTime: row.generateTime,
            dataSource: row.dataSource,
            fileSize: row.fileSize,
            executiveSummary: row.reportSummary || '',
            costOverview: [],
            costBreakdown: [],
            charts: [],
            recommendations: []
          }
          if (response && response.msg) this.$message.warning(response.msg)
        }

        this.previewDialogVisible = true
      } catch (error) {
        this.$message.error('获取预览数据失败：' + (error.message || error))
      }
    },

    // 下载报告
    async handleDownload(row) {
      try {
        // 调用导出API
        const response = await exportCostEstimateDataBlob({ reportId: row.reportId })

        // 创建下载链接
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `${row.reportName}.xlsx`
        link.click()
        window.URL.revokeObjectURL(url)

        this.$message.success('报告下载已开始')
      } catch (error) {
        this.$message.error('下载失败：' + error.message)
      }
    },

    // 从预览下载
    handleDownloadFromPreview() {
      this.$message.success('报告下载已开始')
      this.previewDialogVisible = false
    },

    // 分享报告
    handleShare(row) {
      const reportLink = `${window.location.origin}/financialSharing/costEstimation/estimationReport?reportId=${row.reportId}`
      navigator.clipboard.writeText(reportLink).then(() => {
        this.$message.success('报告链接已复制到剪贴板')
      }).catch(() => {
        // 降级方案
        const input = document.createElement('input')
        input.value = reportLink
        document.body.appendChild(input)
        input.select()
        document.execCommand('copy')
        document.body.removeChild(input)
        this.$message.success('报告链接已复制到剪贴板')
      })
    },

    // 重新生成
    async handleRegenerate(row) {
      try {
        await this.$confirm('确认重新生成该报告吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        await generateEstimationReport({ reportId: row.reportId, regenerate: true })

        this.$message.success('重新生成任务已提交')
        this.fetchData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('重新生成失败：' + error.message)
        }
      }
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该报告吗？删除后不可恢复！', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        // 改用 /report/delete 物理删除——之前 saveOrUpdate 走 generate 会反向 INSERT 一条新数据
        const resp = await deleteEstimationReport(row.reportId)
        if (resp && (resp.code === 1 || resp.code === 200)) {
          this.$message.success(resp.msg || '删除成功')
          this.fetchData()
        } else {
          this.$message.error((resp && resp.msg) || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + (error.message || error))
        }
      }
    },

    // 报告模板：从已有报告复制为模板（避免假按钮跳 404）
    async handleReportTemplate() {
      this.templateDialogVisible = true
      this.templateLoading = true
      try {
        const response = await getEstimationReportPage({ pageNumber: 1, pageSize: 50 })
        if (response && (response.code === 1 || response.code === 200) && response.data) {
          const rawList = response.data.tlist || response.data.records || []
          const list = normalizeKeysArray(rawList)
          // 派生中文展示字段（与主列表 fetchData 保持一致）
          const typeMap = {
            COST_ESTIMATION: '成本估算报告',
            BUDGET_ANALYSIS: '预算分析报告',
            VARIANCE_ANALYSIS: '差异分析报告',
            COMPREHENSIVE: '综合报告',
            1: '成本估算报告', 2: '预算分析报告',
            3: '差异分析报告', 4: '综合报告'
          }
          const statusMap = {
            GENERATED: '已生成', PUBLISHED: '已发布', ARCHIVED: '已归档',
            1: '已生成', 2: '已发布', 3: '已归档'
          }
          list.forEach(item => {
            item.reportTypeName = typeMap[item.reportType] || (item.reportType || '未知')
            item.statusName = statusMap[item.reportStatus] || (item.reportStatus || '未知')
            if (!item.reportCode) item.reportCode = item.reportNo || ''
            if (!item.generateTime) item.generateTime = item.generationTime || ''
          })
          this.templateData = list
        } else {
          this.templateData = []
          if (response && response.msg) this.$message.error(response.msg)
        }
      } catch (error) {
        this.templateData = []
        this.$message.error('获取模板列表失败：' + (error.message || error))
      } finally {
        this.templateLoading = false
      }
    },

    // 复制为模板：关闭模板弹窗 → 打开生成对话框 → 预填字段（编号留空 + 名称加"（副本）"避免重复）
    handleApplyTemplate(row) {
      this.templateDialogVisible = false
      this.resetGenerateForm()
      this.generateForm.reportName = (row.reportName || '') + '（副本）'
      // reportType 兼容字符串枚举与历史数字编码：保持原值即可，select option 都支持
      this.generateForm.reportType = row.reportType == null ? null : row.reportType
      // reportPeriod 后端单值字符串 vs 前端 monthrange 数组——单值不能塞数组，留空让用户重选
      this.generateForm.reportPeriod = []
      this.generateForm.dataSource = row.dataSource || ''
      this.generateForm.remark = row.remark || ''
      this.generateDialogVisible = true
    },

    // 批量导出
    async handleBatchExport() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要导出的报告')
        return
      }
      try {
        for (const row of this.selectedRows) {
          const response = await exportCostEstimateDataBlob({ reportId: row.reportId })
          const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = `${row.reportName || '报告'}.xlsx`
          link.click()
          window.URL.revokeObjectURL(url)
        }
        this.$message.success('批量导出成功')
      } catch (error) {
        this.$message.error('批量导出失败')
      }
    },

    // 分页相关
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.fetchData()
    },

    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.fetchData()
    },

    // 选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 生成对话框关闭
    handleGenerateDialogClose() {
      this.$refs.generateFormRef?.resetFields()
      this.resetGenerateForm()
    },

    // 重置生成表单
    resetGenerateForm() {
      this.generateForm = {
        reportName: '',
        reportType: null,
        reportPeriod: [],
        dataSource: '',
        outputFormat: ['PDF'],
        templateId: 1,
        includeContent: ['executive_summary', 'cost_breakdown', 'variance_analysis'],
        filterConditions: '',
        remark: ''
      }
    },

    // 获取状态类型
    getStatusType(status) {
      const statusMap = {
        1: 'warning', // 生成中
        2: 'success', // 已完成
        3: 'danger'   // 生成失败
      }
      return statusMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.estimation-report-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 84px);
}

.template-tip {
  margin-bottom: 12px;
  padding: 8px 12px;
  background-color: #ecf5ff;
  border-left: 3px solid #409eff;
  color: #606266;
  font-size: 13px;
  border-radius: 4px;

  i {
    color: #409eff;
    margin-right: 6px;
  }
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      margin: 0 0 8px 0;
      font-size: 24px;
      font-weight: 600;
      color: #303133;

      i {
        margin-right: 8px;
        color: #409eff;
      }
    }

    .page-description {
      margin: 0;
      color: #909399;
      font-size: 14px;
    }
  }

  .header-right {
    .el-button {
      margin-left: 10px;
    }
  }
}

.search-container {
  margin-bottom: 20px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

  .search-form {
    .el-form-item {
      margin-bottom: 0;
    }
  }
}

.table-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 20px;

  .pagination-container {
    margin-top: 20px;
    text-align: right;
  }
}

.generate-form {
  .el-form-item {
    margin-bottom: 20px;
  }
}

.preview-container {
  .preview-header {
    text-align: center;
    margin-bottom: 30px;
    padding-bottom: 20px;
    border-bottom: 2px solid #e4e7ed;

    h2 {
      margin: 0 0 10px 0;
      color: #303133;
    }

    .preview-meta {
      display: flex;
      justify-content: center;
      gap: 30px;
      color: #909399;
      font-size: 14px;
    }
  }

  .preview-content {
    .content-section {
      margin-bottom: 30px;

      h3 {
        margin: 0 0 15px 0;
        color: #303133;
        border-left: 4px solid #409eff;
        padding-left: 10px;
      }

      .overview-card {
        text-align: center;
        padding: 20px;
        background: #f8f9fa;
        border-radius: 8px;

        .card-value {
          font-size: 24px;
          font-weight: 600;
          color: #409eff;
          margin-bottom: 8px;
        }

        .card-label {
          font-size: 14px;
          color: #909399;
        }
      }

      .charts-container {
        .chart-item {
          margin-bottom: 20px;

          h4 {
            margin: 0 0 10px 0;
            color: #606266;
          }

          .chart-placeholder {
            height: 200px;
            background: #f8f9fa;
            border: 2px dashed #e4e7ed;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #909399;
            font-size: 14px;
          }
        }
      }

      ol {
        padding-left: 20px;

        li {
          margin-bottom: 8px;
          color: #606266;
          line-height: 1.6;
        }
      }
    }
  }
}

.danger-text {
  color: #f56c6c !important;
}

// 预览对话框样式
:global(.preview-dialog) {
  .el-dialog__body {
    padding: 20px;
    max-height: 70vh;
    overflow-y: auto;
  }
}
</style>
