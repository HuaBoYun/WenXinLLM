<template>
  <div class="risk-warning-management">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="6">
        <el-card class="statistics-card warning-card">
          <div class="statistics-item">
            <div class="statistics-icon">
              <i class="el-icon-warning-outline"></i>
            </div>
            <div class="statistics-content">
              <div class="statistics-value">{{ statistics.totalWarnings || 0 }}</div>
              <div class="statistics-label">总预警数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card danger-card">
          <div class="statistics-item">
            <div class="statistics-icon">
              <i class="el-icon-error"></i>
            </div>
            <div class="statistics-content">
              <div class="statistics-value">{{ statistics.riskLevelStats?.extreme || 0 }}</div>
              <div class="statistics-label">极高风险</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card high-risk-card">
          <div class="statistics-item">
            <div class="statistics-icon">
              <i class="el-icon-warning"></i>
            </div>
            <div class="statistics-content">
              <div class="statistics-value">{{ statistics.riskLevelStats?.high || 0 }}</div>
              <div class="statistics-label">高风险</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card pending-card">
          <div class="statistics-item">
            <div class="statistics-icon">
              <i class="el-icon-time"></i>
            </div>
            <div class="statistics-content">
              <div class="statistics-value">{{ statistics.pendingWarnings || 0 }}</div>
              <div class="statistics-label">待处理</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询表单 -->
    <el-card shadow="never" class="search-card">
      <el-form
        ref="searchForm"
        :model="searchForm"
        :inline="true"
        label-width="80px"
        size="small"
      >
        <el-form-item label="项目名称">
          <el-input
            v-model="searchForm.assessmentName"
            placeholder="请输入项目名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select
            v-model="searchForm.riskLevel"
            placeholder="请选择风险等级"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in riskLevelOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预警状态">
          <el-select
            v-model="searchForm.warningStatus"
            placeholder="请选择预警状态"
            clearable
            style="width: 120px"
          >
            <el-option
              v-for="item in warningStatusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 240px"
            @change="handleDateRangeChange"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">
            查询
          </el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card shadow="never" class="operation-card">
      <el-button type="primary" icon="el-icon-refresh" @click="executeAutoAssessment">
        执行自动评估
      </el-button>
      <el-button
        type="success"
        icon="el-icon-check"
        @click="handleBatchProcess"
        :disabled="selectedRows.length === 0"
      >
        批量处理
      </el-button>
      <el-button type="info" icon="el-icon-download" @click="exportWarningData">
        导出数据
      </el-button>
      <el-button type="warning" icon="el-icon-setting" @click="showConfigDialog">
        预警配置
      </el-button>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never" class="table-card">
      <el-table
        v-loading="tableLoading"
        :data="tableData"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column
          prop="assessmentName"
          label="项目名称"
          min-width="200"
          show-overflow-tooltip
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDetail(row)">
              {{ row.assessmentName }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column
          prop="warningType"
          label="预警类型"
          width="120"
          align="center"
        >
          <template #default="{ row }">
            <el-tag :type="getWarningTypeTagType(row.overallRiskLevel)">
              {{ row.warningType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="overallRiskScore"
          label="风险评分"
          width="100"
          align="center"
        >
          <template #default="{ row }">
            <span :class="getRiskScoreClass(row.overallRiskScore)">
              {{ row.overallRiskScore?.toFixed(1) || '0.0' }}
            </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="riskCount"
          label="风险数量"
          width="100"
          align="center"
        >
          <template #default="{ row }">
            <span>{{ row.riskCount || 0 }}</span>
            <span v-if="row.highRiskCount > 0" class="high-risk-count">
              ({{ row.highRiskCount }}高)
            </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="warningMessage"
          label="预警信息"
          min-width="250"
          show-overflow-tooltip
        />
        <el-table-column
          prop="status"
          label="处理状态"
          width="100"
          align="center"
        >
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">
              {{ getStatusName(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="预警时间"
          width="160"
          align="center"
        >
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template #default="{ row }">
            <el-button
              type="text"
              size="small"
              icon="el-icon-view"
              @click="handleDetail(row)"
            >
              详情
            </el-button>
            <el-button
              v-if="row.status === 1"
              type="text"
              size="small"
              icon="el-icon-check"
              @click="handleProcess(row)"
            >
              处理
            </el-button>
            <el-button
              type="text"
              size="small"
              icon="el-icon-document"
              @click="generateReport(row)"
            >
              报告
            </el-button>
            <el-dropdown @command="handleCommand" trigger="click">
              <el-button type="text" size="small">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'ignore', row}">忽略</el-dropdown-item>
                <el-dropdown-item :command="{action: 'export', row}">导出</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.current"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        />
      </div>
    </el-card>

    <!-- 风险预警详情对话框 -->
    <risk-warning-detail
      ref="warningDetail"
    />

    <!-- 预警处理对话框 -->
    <risk-warning-process
      ref="warningProcess"
      @fetch-data="fetchData"
    />

    <!-- 预警配置对话框 -->
    <risk-warning-config
      ref="warningConfig"
    />

    <!-- 风险趋势图表对话框 -->
    <el-dialog
      title="风险趋势分析"
      :visible.sync="trendDialogVisible"
      width="80%"
    >
      <div id="riskTrendChart" style="height: 400px;"></div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getRiskWarningList,
  getRiskWarningStatistics,
  executeAutoRiskAssessment,
  handleRiskWarning,
  generateRiskAssessmentReport,
  getRiskTrendAnalysis
} from '@/api/contract/riskAssessment'
import RiskWarningDetail from './RiskWarningDetail.vue'
import RiskWarningProcess from './RiskWarningProcess.vue'
import RiskWarningConfig from './RiskWarningConfig.vue'

export default {
  name: 'RiskWarningManagement',
  components: {
    RiskWarningDetail,
    RiskWarningProcess,
    RiskWarningConfig
  },
  data() {
    return {
      tableLoading: false,
      tableData: [],
      selectedRows: [],
      searchForm: {
        assessmentName: '',
        riskLevel: null,
        warningStatus: null,
        startDate: null,
        endDate: null
      },
      dateRange: [],
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      statistics: {
        totalWarnings: 0,
        riskLevelStats: {
          high: 0,
          extreme: 0
        },
        pendingWarnings: 0
      },
      riskLevelOptions: [
        { label: '低风险', value: 1 },
        { label: '中风险', value: 2 },
        { label: '高风险', value: 3 },
        { label: '极高风险', value: 4 }
      ],
      warningStatusOptions: [
        { label: '待处理', value: 1 },
        { label: '处理中', value: 2 },
        { label: '已处理', value: 3 },
        { label: '已忽略', value: 4 }
      ],
      trendDialogVisible: false
    }
  },
  created() {
    this.fetchData()
    this.fetchStatistics()
  },
  methods: {
    async fetchData() {
      this.tableLoading = true
      try {
        const params = {
          ...this.searchForm,
          pageNum: this.pagination.current,
          pageSize: this.pagination.size
        }
        const response = await getRiskWarningList(params)
        // 使用统一的成功状态码判断
        if ([200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          this.tableData = response.data.list || []
          this.pagination.total = response.data.total || 0
        } else {
          this.$message.error(response.msg || response.message || '获取数据失败')
        }
      } catch (error) {
        this.$message.error('获取数据失败：' + error.message)
      } finally {
        this.tableLoading = false
      }
    },

    async fetchStatistics() {
      try {
        const response = await getRiskWarningStatistics()
        console.log('风险预警统计接口返回数据:', response)
        // 使用统一的成功状态码判断
        if ([200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          this.statistics = response.data || {}
          console.log('统计数据处理成功:', this.statistics)
        } else {
          console.error('统计接口返回失败状态码:', response.code)
          this.$message.error(response.msg || response.message || '获取统计数据失败')
        }
      } catch (error) {
        console.error('获取统计数据失败：', error)
        this.$message.error('获取统计数据失败：' + error.message)
      }
    },

    handleSearch() {
      this.pagination.current = 1
      this.fetchData()
    },

    handleReset() {
      this.$refs.searchForm.resetFields()
      this.searchForm = {
        assessmentName: '',
        riskLevel: null,
        warningStatus: null,
        startDate: null,
        endDate: null
      }
      this.dateRange = []
      this.pagination.current = 1
      this.fetchData()
    },

    handleDateRangeChange(dates) {
      if (dates && dates.length === 2) {
        this.searchForm.startDate = dates[0]
        this.searchForm.endDate = dates[1]
      } else {
        this.searchForm.startDate = null
        this.searchForm.endDate = null
      }
    },

    handleDetail(row) {
      this.$refs.warningDetail.showDetail(row)
    },

    handleProcess(row) {
      this.$refs.warningProcess.showProcess(row)
    },

    async executeAutoAssessment() {
      try {
        this.$message.info('正在执行自动风险评估...')
        const response = await executeAutoRiskAssessment()
        // 使用统一的成功状态码判断
        if ([200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          const result = response.data
          this.$message.success(`自动评估完成！评估项目${result.assessedCount}个，发现预警${result.warningCount}个`)
          this.fetchData()
          this.fetchStatistics()
        } else {
          this.$message.error(response.msg || response.message || '自动评估失败')
        }
      } catch (error) {
        this.$message.error('自动评估失败：' + error.message)
      }
    },

    async generateReport(row) {
      try {
        this.$message.info('正在生成风险评估报告...')
        const response = await generateRiskAssessmentReport(row.id)
        // 使用统一的成功状态码判断
        if ([200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          this.$message.success('报告生成成功')
          // 这里可以添加下载逻辑
        } else {
          this.$message.error(response.msg || response.message || '报告生成失败')
        }
      } catch (error) {
        this.$message.error('报告生成失败：' + error.message)
      }
    },

    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    handleBatchProcess() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要处理的预警')
        return
      }
      this.$refs.warningProcess.showBatchProcess(this.selectedRows)
    },

    exportWarningData() {
      this.$message.info('导出功能开发中...')
    },

    showConfigDialog() {
      this.$refs.warningConfig.showConfig()
    },

    async handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'ignore':
          await this.handleIgnore(row)
          break
        case 'export':
          await this.exportSingleReport(row)
          break
      }
    },

    async handleIgnore(row) {
      try {
        await this.$confirm('确定要忽略这个预警吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await handleRiskWarning(row.id, 4, '用户手动忽略')
        // 使用统一的成功状态码判断
        if ([200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          this.$message.success('预警已忽略')
          this.fetchData()
        } else {
          this.$message.error(response.msg || response.message || '操作失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('操作失败：' + error.message)
        }
      }
    },

    async exportSingleReport(row) {
      this.$message.info('导出单个报告功能开发中...')
    },

    handleSizeChange(val) {
      this.pagination.size = val
      this.pagination.current = 1
      this.fetchData()
    },

    handleCurrentChange(val) {
      this.pagination.current = val
      this.fetchData()
    },

    getWarningTypeTagType(riskLevel) {
      const typeMap = {
        1: 'info',
        2: 'warning',
        3: 'danger',
        4: 'danger'
      }
      return typeMap[riskLevel] || 'info'
    },

    getRiskScoreClass(score) {
      if (score >= 85) return 'risk-score-extreme'
      if (score >= 70) return 'risk-score-high'
      if (score >= 50) return 'risk-score-medium'
      return 'risk-score-low'
    },

    getStatusTagType(status) {
      const statusMap = {
        1: 'danger',
        2: 'warning',
        3: 'success',
        4: 'info'
      }
      return statusMap[status] || 'info'
    },

    getStatusName(status) {
      const statusMap = {
        1: '待处理',
        2: '处理中',
        3: '已处理',
        4: '已忽略'
      }
      return statusMap[status] || '未知'
    },

    formatDate(date) {
      if (!date) return ''
      return new Date(date).toLocaleString()
    }
  }
}
</script>

<style scoped>
.risk-warning-management {
  padding: 20px;
}

.statistics-row {
  margin-bottom: 20px;
}

.statistics-card {
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}

.statistics-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.statistics-item {
  display: flex;
  align-items: center;
  padding: 20px;
}

.statistics-icon {
  font-size: 36px;
  margin-right: 15px;
}

.warning-card .statistics-icon {
  color: #e6a23c;
}

.danger-card .statistics-icon {
  color: #f56c6c;
}

.high-risk-card .statistics-icon {
  color: #ff7875;
}

.pending-card .statistics-icon {
  color: #409eff;
}

.statistics-content {
  flex: 1;
  text-align: left;
}

.statistics-value {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 5px;
}

.statistics-label {
  font-size: 14px;
  color: #606266;
}

.search-card,
.operation-card,
.table-card {
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.high-risk-count {
  color: #f56c6c;
  font-size: 12px;
}

.risk-score-extreme {
  color: #f56c6c;
  font-weight: bold;
}

.risk-score-high {
  color: #e6a23c;
  font-weight: bold;
}

.risk-score-medium {
  color: #409eff;
}

.risk-score-low {
  color: #67c23a;
}
</style>
