<template>
  <div class="cost-report-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-document"></i>
          成本报告
        </h1>
        <p class="page-description">生成和管理各类成本报告，包括核算报告、分析报告、控制报告等</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="handleGenerateReport">
          生成报告
        </el-button>
        <el-button type="success" icon="el-icon-setting" @click="handleReportConfig">
          报告配置
        </el-button>
        <el-button type="warning" icon="el-icon-download" @click="handleBatchExport">
          批量导出
        </el-button>
      </div>
    </div>

    <!-- 报告类型选择 -->
    <div class="report-types">
      <el-card title="报告类型">
        <div slot="header">
          <span>报告类型</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="report-type-card" @click="selectReportType('accounting')">
              <div class="type-icon accounting">
                <i class="el-icon-cpu"></i>
              </div>
              <div class="type-content">
                <div class="type-title">成本核算报告</div>
                <div class="type-desc">产品成本核算结果报告</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="report-type-card" @click="selectReportType('analysis')">
              <div class="type-icon analysis">
                <i class="el-icon-data-analysis"></i>
              </div>
              <div class="type-content">
                <div class="type-title">成本分析报告</div>
                <div class="type-desc">成本构成、趋势分析报告</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="report-type-card" @click="selectReportType('control')">
              <div class="type-icon control">
                <i class="el-icon-warning"></i>
              </div>
              <div class="type-content">
                <div class="type-title">成本控制报告</div>
                <div class="type-desc">预算执行、差异分析报告</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="report-type-card" @click="selectReportType('comprehensive')">
              <div class="type-icon comprehensive">
                <i class="el-icon-document"></i>
              </div>
              <div class="type-content">
                <div class="type-title">综合成本报告</div>
                <div class="type-desc">全面的成本管理报告</div>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-card>
    </div>

    <!-- 报告列表 -->
    <div class="report-list">
      <el-card title="报告列表">
        <div slot="header">
          <span>报告列表</span>
          <div style="float: right">
            <el-button size="small" type="text" @click="refreshReports">刷新</el-button>
          </div>
        </div>

        <!-- 查询条件 -->
        <el-form :model="queryForm" ref="queryForm" :inline="true" class="search-form">
          <el-form-item label="报告名称" prop="reportName">
            <el-input v-model="queryForm.reportName" placeholder="请输入报告名称" clearable />
          </el-form-item>
          <el-form-item label="报告类型" prop="reportType">
            <el-select v-model="queryForm.reportType" placeholder="请选择报告类型" clearable>
              <el-option label="成本核算报告" value="accounting" />
              <el-option label="成本分析报告" value="analysis" />
              <el-option label="成本控制报告" value="control" />
              <el-option label="综合成本报告" value="comprehensive" />
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
              clearable
            />
          </el-form-item>
          <el-form-item label="生成状态" prop="generateStatus">
            <el-select v-model="queryForm.generateStatus" placeholder="请选择生成状态" clearable>
              <el-option label="生成中" value="1" />
              <el-option label="已完成" value="2" />
              <el-option label="生成失败" value="3" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
            <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <!-- 数据表格 -->
        <el-table
          v-loading="loading"
          :data="reportList"
          row-key="reportId"
          border
          stripe
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="报告名称" prop="reportName" min-width="200" />
          <el-table-column label="报告类型" prop="reportTypeName" width="120" />
          <el-table-column label="报告期间" prop="reportPeriod" width="120" />
          <el-table-column label="生成时间" prop="generateTime" width="150" />
          <el-table-column label="生成人" prop="generatorName" width="100" />
          <el-table-column label="文件大小" prop="fileSize" width="100" />
          <el-table-column label="生成状态" prop="generateStatus" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="getGenerateStatusType(scope.row.generateStatus)">
                {{ getGenerateStatusName(scope.row.generateStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="250" align="center" fixed="right">
            <template slot-scope="scope">
              <el-button size="mini" type="text" @click="handlePreview(scope.row)">预览</el-button>
              <el-button size="mini" type="text" @click="handleDownload(scope.row)">下载</el-button>
              <el-button size="mini" type="text" @click="handleShare(scope.row)">分享</el-button>
              <el-button size="mini" type="text" @click="handleRegenerate(scope.row)">重新生成</el-button>
              <el-button size="mini" type="text" style="color: #f56c6c" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页组件 -->
        <el-pagination
          background
          class="pagination"
          :current-page="queryForm.pageNumber"
          layout="total, sizes, prev, pager, next, jumper"
          :page-size="queryForm.pageSize"
          :total="total"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </el-card>
    </div>

    <!-- 生成报告对话框 -->
    <el-dialog
      title="生成报告"
      :visible.sync="generateDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form :model="generateForm" :rules="generateRules" ref="generateForm" label-width="120px">
        <el-form-item label="报告名称" prop="reportName">
          <el-input v-model="generateForm.reportName" placeholder="请输入报告名称" />
        </el-form-item>
        <el-form-item label="报告类型" prop="reportType">
          <el-select v-model="generateForm.reportType" placeholder="请选择报告类型" style="width: 100%">
            <el-option label="成本核算报告" value="accounting" />
            <el-option label="成本分析报告" value="analysis" />
            <el-option label="成本控制报告" value="control" />
            <el-option label="综合成本报告" value="comprehensive" />
          </el-select>
        </el-form-item>
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
        <el-form-item label="产品范围" prop="productScope">
          <el-select v-model="generateForm.productScope" placeholder="请选择产品范围" style="width: 100%">
            <el-option label="全部产品" value="all" />
            <el-option label="指定产品" value="selected" />
            <el-option label="产品分类" value="category" />
          </el-select>
        </el-form-item>
        <el-form-item label="报告格式" prop="reportFormat">
          <el-checkbox-group v-model="generateForm.reportFormat">
            <el-checkbox label="PDF">PDF</el-checkbox>
            <el-checkbox label="Excel">Excel</el-checkbox>
            <el-checkbox label="Word">Word</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="包含图表" prop="includeCharts">
          <el-switch v-model="generateForm.includeCharts" />
        </el-form-item>
        <el-form-item label="报告描述" prop="reportDesc">
          <el-input
            v-model="generateForm.reportDesc"
            type="textarea"
            :rows="3"
            placeholder="请输入报告描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="generateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleGenerateSubmit" :loading="generating">生成</el-button>
      </div>
    </el-dialog>

    <!-- 报告预览对话框 -->
    <el-dialog
      title="报告预览"
      :visible.sync="previewDialogVisible"
      width="1000px"
      :close-on-click-modal="false"
    >
      <div class="report-preview">
        <div class="preview-header">
          <h2>{{ previewReport.reportName }}</h2>
          <div class="report-info">
            <span>报告期间：{{ previewReport.reportPeriod }}</span>
            <span>生成时间：{{ previewReport.generateTime }}</span>
          </div>
        </div>
        <div class="preview-content">
          <!-- 这里可以根据报告类型显示不同的预览内容 -->
          <div v-if="previewReport.reportType === 'accounting'" class="accounting-preview">
            <h3>成本核算汇总</h3>
            <el-table :data="previewData.accountingData" border size="small">
              <el-table-column label="产品名称" prop="productName" />
              <el-table-column label="总成本" prop="totalCost" align="right">
                <template slot-scope="scope">
                  <span>{{ formatAmount(scope.row.totalCost) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="单位成本" prop="unitCost" align="right">
                <template slot-scope="scope">
                  <span>{{ formatAmount(scope.row.unitCost) }}</span>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <div v-else class="other-preview">
            <div class="report-content-preview">
              <el-descriptions :column="2" border size="small">
                <el-descriptions-item label="报告类型">{{ previewReport.reportTypeName || '-' }}</el-descriptions-item>
                <el-descriptions-item label="报告期间">{{ previewReport.reportPeriod || '-' }}</el-descriptions-item>
                <el-descriptions-item label="生成时间">{{ previewReport.generateTime || '-' }}</el-descriptions-item>
                <el-descriptions-item label="生成人">{{ previewReport.generatorName || '-' }}</el-descriptions-item>
                <el-descriptions-item label="文件大小">{{ previewReport.fileSize || '-' }}</el-descriptions-item>
                <el-descriptions-item label="生成状态">
                  <el-tag :type="getGenerateStatusType(previewReport.generateStatus)" size="small">
                    {{ getGenerateStatusName(previewReport.generateStatus) }}
                  </el-tag>
                </el-descriptions-item>
              </el-descriptions>
              <div v-if="previewReport.reportDesc" class="report-summary">
                <h4>报告摘要</h4>
                <p>{{ previewReport.reportDesc }}</p>
              </div>
              <div class="report-download-hint">
                <i class="el-icon-document"></i>
                <p>该类型报告暂不支持在线预览表格数据，请下载后查看完整内容</p>
                <el-button type="primary" size="small" icon="el-icon-download" @click="handleDownloadFromPreview">
                  下载报告
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="previewDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="handleDownloadFromPreview">下载报告</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getCostReportList, generateCostReport, downloadCostReport, deleteCostReport, exportProductCostDataBlob } from '@/api/financialSharing/productCost'


export default {
  name: 'CostReport',
  components: {
  },
  data() {
    return {
      loading: false,
      generating: false,
      reportList: [],
      selectedRows: [],
      total: 0,
      generateDialogVisible: false,
      previewDialogVisible: false,
      selectedReportType: '',
      queryForm: {
        pageNumber: 1,
        pageSize: 15,
        reportName: '',
        reportType: '',
        reportPeriod: [],
        generateStatus: ''
      },
      generateForm: {
        reportName: '',
        reportType: '',
        reportPeriod: [],
        productScope: 'all',
        reportFormat: ['PDF'],
        includeCharts: true,
        reportDesc: ''
      },
      generateRules: {
        reportName: [
          { required: true, message: '请输入报告名称', trigger: 'blur' }
        ],
        reportType: [
          { required: true, message: '请选择报告类型', trigger: 'change' }
        ],
        reportPeriod: [
          { required: true, message: '请选择报告期间', trigger: 'change' }
        ]
      },
      previewReport: {},
      previewData: {
        accountingData: []
      }
    }
  },
  created() {
    this.getReportList()
  },
  methods: {
    // 获取报告列表
    async getReportList() {
      this.loading = true
      try {
        const response = await getCostReportList(this.queryForm)
        if (response.code === 1) {
          this.reportList = response.data.tlist || []
          this.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 选择报告类型
    selectReportType(type) {
      this.selectedReportType = type
      this.generateForm.reportType = type
      this.generateDialogVisible = true
    },

    // 查询
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.getReportList()
    },

    // 重置查询
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.queryForm.pageNumber = 1
      this.getReportList()
    },

    // 刷新报告
    refreshReports() {
      this.getReportList()
    },

    // 生成报告
    handleGenerateReport() {
      this.generateDialogVisible = true
      this.resetGenerateForm()
    },

    // 生成提交
    async handleGenerateSubmit() {
      this.$refs.generateForm.validate(async (valid) => {
        if (valid) {
          this.generating = true
          try {
            const response = await generateCostReport(this.generateForm)
            if (response.code === 1) {
              this.$message.success('报告生成任务已提交，请稍后查看')
              this.generateDialogVisible = false
              this.getReportList()
            } else {
              this.$message.error(response.msg || '生成失败')
            }
          } catch (error) {
            this.$message.error('生成失败：' + error.message)
          } finally {
            this.generating = false
          }
        }
      })
    },

    // 预览报告
    handlePreview(row) {
      this.previewReport = row
      this.previewDialogVisible = true
    },

    // 下载报告
    async handleDownload(row) {
      try {
        const response = await downloadCostReport({ reportId: row.reportId })
        // 这里处理文件下载
        this.$message.success('下载成功')
      } catch (error) {
        this.$message.error('下载失败：' + error.message)
      }
    },

    // 从预览下载
    handleDownloadFromPreview() {
      this.handleDownload(this.previewReport)
    },

    // 分享报告
    handleShare(row) {
      const shareUrl = `${window.location.origin}/financialSharing/management/productCost/costReport?id=${row.reportId}`
      navigator.clipboard.writeText(shareUrl).then(() => {
        this.$message.success('报告链接已复制到剪贴板')
      }).catch(() => {
        this.$message.info('报告链接: ' + shareUrl)
      })
    },

    // 重新生成
    handleRegenerate(row) {
      this.$confirm('确认重新生成该报告吗？重新生成将覆盖当前报告内容。', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await generateCostReport({ reportId: row.reportId, ...row })
          if (res.code === 1) {
            this.$message.success('报告重新生成成功')
            this.fetchReportList()
          } else {
            this.$message.error(res.msg || '重新生成失败')
          }
        } catch (error) {
          this.$message.error('重新生成失败')
        }
      }).catch(() => {})
    },

    // 删除报告
    handleDelete(row) {
      this.$confirm('确认删除该报告吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteCostReport(row.reportId)
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getReportList()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      })
    },

    // 报告配置
    handleReportConfig() {
      this.$message.info('报告配置功能包含：报告模板设置、生成周期配置、数据源选择、审批流程配置，详细配置请联系管理员')
    },

    // 批量导出
    async handleBatchExport() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要导出的报告')
        return
      }
      try {
        for (const row of this.selectedRows) {
          await downloadCostReport(row.reportId)
        }
        this.$message.success(`已成功导出${this.selectedRows.length}份报告`)
      } catch (error) {
        this.$message.error('批量导出失败')
      }
    },

    // 重置生成表单
    resetGenerateForm() {
      this.generateForm = {
        reportName: '',
        reportType: this.selectedReportType || '',
        reportPeriod: [],
        productScope: 'all',
        reportFormat: ['PDF'],
        includeCharts: true,
        reportDesc: ''
      }
      this.$nextTick(() => {
        this.$refs.generateForm && this.$refs.generateForm.clearValidate()
      })
    },

    // 选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 获取生成状态类型
    getGenerateStatusType(status) {
      const types = { 1: 'warning', 2: 'success', 3: 'danger' }
      return types[status] || 'info'
    },

    // 获取生成状态名称
    getGenerateStatusName(status) {
      const names = { 1: '生成中', 2: '已完成', 3: '生成失败' }
      return names[status] || '未知'
    },

    // 格式化金额
    formatAmount(amount) {
      if (amount == null || amount === '') return '0.00'
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.cost-report-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 84px);
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
}

.report-types {
  margin-bottom: 20px;

  .report-type-card {
    display: flex;
    align-items: center;
    padding: 20px;
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    cursor: pointer;
    transition: all 0.3s;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
    }

    .type-icon {
      width: 50px;
      height: 50px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 15px;

      i {
        font-size: 20px;
        color: white;
      }

      &.accounting {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.analysis {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.control {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }

      &.comprehensive {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }
    }

    .type-content {
      .type-title {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }

      .type-desc {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

.report-list {
  .search-form {
    margin-bottom: 20px;
  }
}

.report-preview {
  .preview-header {
    text-align: center;
    margin-bottom: 20px;
    padding-bottom: 15px;
    border-bottom: 1px solid #ebeef5;

    h2 {
      margin: 0 0 10px 0;
      color: #303133;
    }

    .report-info {
      color: #909399;
      font-size: 14px;

      span {
        margin-right: 20px;
      }
    }
  }

  .preview-content {
    h3 {
      margin: 0 0 15px 0;
      color: #303133;
    }

    .report-content-preview {
      .report-summary {
        margin-top: 15px;
        padding: 12px 16px;
        background: #f5f7fa;
        border-radius: 4px;

        h4 {
          margin: 0 0 8px 0;
          font-size: 14px;
          color: #303133;
        }

        p {
          margin: 0;
          font-size: 14px;
          color: #606266;
          line-height: 1.6;
        }
      }

      .report-download-hint {
        margin-top: 20px;
        padding: 30px 20px;
        text-align: center;
        border: 1px dashed #dcdfe6;
        border-radius: 4px;

        i {
          font-size: 36px;
          color: #c0c4cc;
        }

        p {
          margin: 12px 0 16px;
          color: #909399;
          font-size: 14px;
        }
      }
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>
