<template>
  <div class="budget-summary-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card total">
          <div class="stat-content">
            <div class="stat-icon">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.totalCount || 0 }}</div>
              <div class="stat-label">汇总总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card completed">
          <div class="stat-content">
            <div class="stat-icon">
              <i class="el-icon-circle-check"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.completedCount || 0 }}</div>
              <div class="stat-label">已完成</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card processing">
          <div class="stat-content">
            <div class="stat-icon">
              <i class="el-icon-loading"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.processingCount || 0 }}</div>
              <div class="stat-label">处理中</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card failed">
          <div class="stat-content">
            <div class="stat-icon">
              <i class="el-icon-circle-close"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ statistics.failedCount || 0 }}</div>
              <div class="stat-label">失败</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询表单 -->
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="预算模型">
          <el-select v-model="queryForm.modelId" placeholder="请选择预算模型" clearable filterable style="width: 200px">
            <el-option
              v-for="item in modelList"
              :key="item.modelId"
              :label="item.modelName"
              :value="item.modelId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预算期间">
          <el-input v-model="queryForm.period" placeholder="请输入预算期间" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="预算版本">
          <el-input v-model="queryForm.version" placeholder="请输入预算版本" clearable style="width: 150px" />
        </el-form-item>
        <el-form-item label="汇总类型">
          <el-select v-model="queryForm.summaryType" placeholder="请选择" clearable style="width: 120px">
            <el-option label="科目汇总" value="SUBJECT" />
            <el-option label="组织汇总" value="ORGANIZATION" />
            <el-option label="期间汇总" value="PERIOD" />
            <el-option label="自定义汇总" value="CUSTOM" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择" clearable style="width: 120px">
            <el-option label="处理中" value="PROCESSING" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="失败" value="FAILED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <div slot="header" class="card-header">
        <span>汇总列表</span>
        <div>
          <el-button
            type="primary"
            icon="el-icon-plus"
            size="small"
            @click="handleExecute"
          >
            执行汇总
          </el-button>
          <el-button
            type="danger"
            icon="el-icon-delete"
            size="small"
            :disabled="multipleSelection.length === 0"
            @click="handleBatchDelete"
          >
            批量删除
          </el-button>
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="modelName" label="预算模型" min-width="150" show-overflow-tooltip />
        <el-table-column prop="period" label="预算期间" width="120" align="center" />
        <el-table-column prop="version" label="预算版本" width="120" align="center" />
        <el-table-column label="汇总类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.summaryType === 'SUBJECT'" type="primary" size="small">科目汇总</el-tag>
            <el-tag v-else-if="scope.row.summaryType === 'ORGANIZATION'" type="success" size="small">组织汇总</el-tag>
            <el-tag v-else-if="scope.row.summaryType === 'PERIOD'" type="warning" size="small">期间汇总</el-tag>
            <el-tag v-else type="info" size="small">自定义汇总</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="summaryMethod" label="汇总方法" width="100" align="center">
          <template slot-scope="scope">
            {{ summaryMethodMap[scope.row.summaryMethod] || scope.row.summaryMethod || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="dataCount" label="数据条数" width="100" align="center" />
        <el-table-column label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 'COMPLETED'" type="success" size="small">已完成</el-tag>
            <el-tag v-else-if="scope.row.status === 'PROCESSING'" type="warning" size="small">处理中</el-tag>
            <el-tag v-else type="danger" size="small">失败</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" align="center" />
        <el-table-column label="操作" width="250" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" icon="el-icon-view" @click="handleView(scope.row)">
              查看
            </el-button>
            <el-button
              v-if="scope.row.status === 'FAILED'"
              type="text"
              size="small"
              icon="el-icon-refresh"
              @click="handleReExecute(scope.row)"
            >
              重新执行
            </el-button>
            <el-button type="text" size="small" icon="el-icon-download" @click="handleExport(scope.row)">
              导出
            </el-button>
            <el-button type="text" size="small" icon="el-icon-delete" @click="handleDelete(scope.row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryForm.pageNum"
        :limit.sync="queryForm.pageSize"
        @pagination="loadData"
      />
    </el-card>

    <!-- 执行汇总 对话框 -->
    <el-dialog title="执行汇总" :visible.sync="executeDialogVisible" width="600px" @close="resetExecuteForm">
      <el-form ref="executeForm" :model="executeForm" :rules="executeRules" label-width="100px">
        <el-form-item label="预算模型" prop="modelId">
          <el-select v-model="executeForm.modelId" placeholder="请选择预算模型" clearable filterable style="width: 100%">
            <el-option v-for="item in modelList" :key="item.modelId" :label="item.modelName" :value="item.modelId" />
          </el-select>
        </el-form-item>
        <el-form-item label="预算期间" prop="period">
          <el-input v-model="executeForm.period" placeholder="请输入预算期间, 例如 2026" />
        </el-form-item>
        <el-form-item label="预算版本" prop="version">
          <el-input v-model="executeForm.version" placeholder="请输入预算版本, 例如 V1" />
        </el-form-item>
        <el-form-item label="汇总类型" prop="summaryType">
          <el-select v-model="executeForm.summaryType" placeholder="请选择" style="width: 100%">
            <el-option label="科目汇总" value="SUBJECT" />
            <el-option label="组织汇总" value="ORGANIZATION" />
            <el-option label="期间汇总" value="PERIOD" />
            <el-option label="自定义汇总" value="CUSTOM" />
          </el-select>
        </el-form-item>
        <el-form-item label="汇总方法" prop="summaryMethod">
          <el-select v-model="executeForm.summaryMethod" placeholder="请选择" style="width: 100%">
            <el-option v-for="(label, key) in summaryMethodMap" :key="key" :label="label" :value="key" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="executeDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="executeLoading" @click="handleConfirmExecute">确定</el-button>
      </div>
    </el-dialog>

    <!-- 查看汇总明细 对话框 -->
    <el-dialog title="汇总明细" :visible.sync="detailDialogVisible" width="900px" @close="resetDetail">
      <el-descriptions :column="3" border size="small" v-if="detailMeta">
        <el-descriptions-item label="预算模型">{{ detailMeta.modelName }}</el-descriptions-item>
        <el-descriptions-item label="预算期间">{{ detailMeta.period }}</el-descriptions-item>
        <el-descriptions-item label="预算版本">{{ detailMeta.version }}</el-descriptions-item>
        <el-descriptions-item label="汇总类型">{{ summaryTypeMap[detailMeta.summaryType] || detailMeta.summaryType }}</el-descriptions-item>
        <el-descriptions-item label="汇总方法">{{ summaryMethodMap[detailMeta.summaryMethod] || detailMeta.summaryMethod }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="detailMeta.status === 'COMPLETED'" type="success" size="small">已完成</el-tag>
          <el-tag v-else-if="detailMeta.status === 'PROCESSING'" type="warning" size="small">处理中</el-tag>
          <el-tag v-else type="danger" size="small">失败</el-tag>
        </el-descriptions-item>
      </el-descriptions>
      <el-table :data="detailRows" border stripe size="small" style="margin-top: 12px" v-loading="detailLoading">
        <el-table-column prop="dimensionCode" label="维度编码" min-width="140" show-overflow-tooltip />
        <el-table-column prop="dimensionName" label="维度名称" min-width="160" show-overflow-tooltip />
        <el-table-column prop="summaryValue" label="汇总值" width="160" align="right" />
        <el-table-column prop="dataCount" label="数据条数" width="100" align="center" />
      </el-table>
      <div slot="footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getSummaryList,
  deleteSummary,
  batchDeleteSummary,
  reExecuteSummary,
  getSummaryStatistics,
  exportSummary,
  executeSummary,
  getSummaryByCondition
} from '@/api/financialSharing/budgetPlanning/budgetSummary'
import { getModelListNoPage } from '@/api/financialSharing/budgetPlanning/budgetModel'
import Pagination from '@/components/Pagination'

export default {
  name: 'BudgetSummary',
  components: { Pagination },
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      multipleSelection: [],
      modelList: [],
      queryForm: {
        modelId: '',
        period: '',
        version: '',
        summaryType: '',
        status: '',
        pageNum: 1,
        pageSize: 10
      },
      statistics: {
        totalCount: 0,
        completedCount: 0,
        processingCount: 0,
        failedCount: 0
      },
      // 字段中英文映射(模板/弹窗共用)
      summaryMethodMap: {
        SUM: '求和',
        AVG: '平均',
        MAX: '最大',
        MIN: '最小',
        COUNT: '计数'
      },
      summaryTypeMap: {
        SUBJECT: '科目汇总',
        ORGANIZATION: '组织汇总',
        PERIOD: '期间汇总',
        CUSTOM: '自定义汇总'
      },
      // 执行汇总弹窗
      executeDialogVisible: false,
      executeLoading: false,
      executeForm: {
        modelId: '',
        period: '',
        version: '',
        summaryType: 'SUBJECT',
        summaryMethod: 'SUM'
      },
      executeRules: {
        modelId: [{ required: true, message: '请选择预算模型', trigger: 'change' }],
        period: [{ required: true, message: '请输入预算期间', trigger: 'blur' }],
        version: [{ required: true, message: '请输入预算版本', trigger: 'blur' }],
        summaryType: [{ required: true, message: '请选择汇总类型', trigger: 'change' }],
        summaryMethod: [{ required: true, message: '请选择汇总方法', trigger: 'change' }]
      },
      // 查看明细弹窗
      detailDialogVisible: false,
      detailLoading: false,
      detailMeta: null,
      detailRows: []
    }
  },
  created() {
    this.loadModelList()
    this.loadStatistics()
    this.loadData()
  },
  methods: {
    // 加载预算模型列表
    async loadModelList() {
      try {
        const res = await getModelListNoPage({ status: 'ACTIVE' })
        if (res.code === 1) {
          this.modelList = res.data || []
        }
      } catch (error) {
        console.error('加载模型列表失败:', error)
      }
    },
    // 加载统计信息
    async loadStatistics() {
      try {
        const res = await getSummaryStatistics()
        // 调试用: 看接口实际返回, 哥可以在浏览器 DevTools Console 直接看
        console.log('[BudgetSummary] getSummaryStatistics 返回:', res)
        if (res.code === 1 && res.data) {
          this.statistics = {
            totalCount: Number(res.data.totalCount) || 0,
            completedCount: Number(res.data.completedCount) || 0,
            processingCount: Number(res.data.processingCount) || 0,
            failedCount: Number(res.data.failedCount) || 0
          }
        } else {
          this.$message.warning(res.msg || '加载统计信息失败, 请查看控制台')
        }
      } catch (error) {
        console.error('加载统计信息失败:', error)
        this.$message.error('加载统计信息失败: ' + (error.message || error))
      }
    },
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const res = await getSummaryList(this.queryForm)
        if (res.code === 1) {
          this.tableData = res.data.list || []
          this.total = res.data.total || 0
        } else {
          this.$message.error(res.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败: ' + error.message)
      } finally {
        this.loading = false
      }
    },
    handleQuery() {
      this.queryForm.pageNum = 1
      this.loadData()
    },
    handleReset() {
      this.queryForm = {
        modelId: '', period: '', version: '', summaryType: '', status: '',
        pageNum: 1, pageSize: 10
      }
      this.loadData()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    // 执行汇总 - 弹窗(不再跳转路由)
    handleExecute() {
      this.executeForm = {
        modelId: this.queryForm.modelId || '',
        period: this.queryForm.period || '',
        version: this.queryForm.version || '',
        summaryType: 'SUBJECT',
        summaryMethod: 'SUM'
      }
      this.executeDialogVisible = true
    },
    resetExecuteForm() {
      if (this.$refs.executeForm) this.$refs.executeForm.resetFields()
    },
    handleConfirmExecute() {
      this.$refs.executeForm.validate(async valid => {
        if (!valid) return
        this.executeLoading = true
        try {
          const res = await executeSummary(this.executeForm)
          if (res.code === 1) {
            this.$message.success('汇总执行成功')
            this.executeDialogVisible = false
            this.loadStatistics()
            this.loadData()
          } else {
            this.$message.error(res.msg || '汇总执行失败')
          }
        } catch (error) {
          this.$message.error('汇总执行失败: ' + (error.message || error))
        } finally {
          this.executeLoading = false
        }
      })
    },
    // 查看 - 弹窗(同条件下所有维度数据)
    async handleView(row) {
      this.detailMeta = { ...row }
      this.detailRows = []
      this.detailDialogVisible = true
      this.detailLoading = true
      try {
        const res = await getSummaryByCondition({
          modelId: row.modelId,
          period: row.period,
          version: row.version,
          summaryType: row.summaryType
        })
        if (res.code === 1) {
          this.detailRows = res.data || []
        } else {
          this.$message.error(res.msg || '加载明细失败')
        }
      } catch (error) {
        this.$message.error('加载明细失败: ' + (error.message || error))
      } finally {
        this.detailLoading = false
      }
    },
    resetDetail() {
      this.detailMeta = null
      this.detailRows = []
    },
    // 重新执行
    handleReExecute(row) {
      this.$confirm('确认重新执行该汇总任务吗?', '提示', {
        confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
      }).then(async () => {
        try {
          const res = await reExecuteSummary({ summaryId: row.summaryId })
          if (res.code === 1) {
            this.$message.success('重新执行成功')
            this.loadStatistics()
            this.loadData()
          } else {
            this.$message.error(res.msg || '重新执行失败')
          }
        } catch (error) {
          this.$message.error('重新执行失败: ' + error.message)
        }
      }).catch(() => {})
    },
    // 导出 - blob 下载
    async handleExport(row) {
      try {
        const blob = await exportSummary({ summaryId: row.summaryId })
        // 后端异常时, 拦截器仍可能放行 blob; 这里识别一下文本异常
        if (blob && blob.type && blob.type.startsWith('text/')) {
          const text = await blob.text()
          this.$message.error(text || '导出失败')
          return
        }
        const url = window.URL.createObjectURL(new Blob([blob], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        }))
        const link = document.createElement('a')
        link.href = url
        link.download = `预算汇总_${row.period || ''}_${row.version || ''}.xlsx`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
      } catch (error) {
        this.$message.error('导出失败: ' + (error.message || error))
      }
    },
    handleDelete(row) {
      this.$confirm('确认删除该汇总记录吗?', '提示', {
        confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
      }).then(async () => {
        try {
          const res = await deleteSummary({ summaryId: row.summaryId })
          if (res.code === 1) {
            this.$message.success('删除成功')
            this.loadStatistics()
            this.loadData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败: ' + error.message)
        }
      }).catch(() => {})
    },
    handleBatchDelete() {
      this.$confirm('确认批量删除选中的汇总记录吗?', '提示', {
        confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
      }).then(async () => {
        try {
          const summaryIds = this.multipleSelection.map(item => item.summaryId)
          const res = await batchDeleteSummary({ summaryIds })
          if (res.code === 1) {
            this.$message.success('批量删除成功')
            this.loadStatistics()
            this.loadData()
          } else {
            this.$message.error(res.msg || '批量删除失败')
          }
        } catch (error) {
          this.$message.error('批量删除失败: ' + error.message)
        }
      }).catch(() => {})
    }
  }
}
</script>

<style lang="scss" scoped>
.budget-summary-container {
  padding: 20px;

  .statistics-row {
    margin-bottom: 20px;

    .stat-card {
      cursor: pointer;
      transition: all 0.3s;

      &:hover {
        transform: translateY(-5px);
      }

      .stat-content {
        display: flex;
        align-items: center;

        .stat-icon {
          font-size: 48px;
          margin-right: 20px;
        }

        .stat-info {
          flex: 1;

          .stat-value {
            font-size: 32px;
            font-weight: bold;
            margin-bottom: 5px;
          }

          .stat-label {
            font-size: 14px;
            color: #909399;
          }
        }
      }

      &.total {
        .stat-icon {
          color: #409EFF;
        }
        .stat-value {
          color: #409EFF;
        }
      }

      &.completed {
        .stat-icon {
          color: #67C23A;
        }
        .stat-value {
          color: #67C23A;
        }
      }

      &.processing {
        .stat-icon {
          color: #E6A23C;
        }
        .stat-value {
          color: #E6A23C;
        }
      }

      &.failed {
        .stat-icon {
          color: #F56C6C;
        }
        .stat-value {
          color: #F56C6C;
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;
  }

  .table-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .el-pagination {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>

