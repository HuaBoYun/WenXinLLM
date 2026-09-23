<template>
  <div class="assessment-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>项目考核管理</h2>
      <p>项目考核结果查询、统计分析和报告生成</p>
    </div>

    <!-- 搜索条件 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="项目ID">
          <el-input
            v-model="searchForm.projectId"
            placeholder="请输入项目ID"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="项目名称">
          <el-input
            v-model="searchForm.projectName"
            placeholder="请输入项目名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="考核期间">
          <el-input
            v-model="searchForm.assessmentPeriod"
            placeholder="如：2024-01"
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="考核类型">
          <el-select
            v-model="searchForm.assessmentType"
            placeholder="请选择"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="item in assessmentTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="考核状态">
          <el-select
            v-model="searchForm.assessmentStatus"
            placeholder="请选择"
            clearable
            style="width: 120px"
          >
            <el-option
              v-for="item in assessmentStatusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="考核等级">
          <el-select
            v-model="searchForm.assessmentLevel"
            placeholder="请选择"
            clearable
            style="width: 120px"
          >
            <el-option
              v-for="item in assessmentLevelOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
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
    <div class="toolbar">
      <el-button type="primary" icon="el-icon-plus" @click="handleStartAssessment">
        启动考核
      </el-button>
      <el-button type="success" icon="el-icon-download" @click="handleExport">
        导出数据
      </el-button>
      <el-button type="info" icon="el-icon-s-data" @click="handleStatistics">
        统计分析
      </el-button>
    </div>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        v-loading="loading"
        :data="tableData"
        stripe
        border
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="projectId" label="项目ID" width="120" />
        <el-table-column prop="projectName" label="项目名称" width="200" show-overflow-tooltip />
        <el-table-column prop="assessmentPeriod" label="考核期间" width="100" />
        <el-table-column prop="assessmentTypeName" label="考核类型" width="120" />
        <el-table-column prop="totalScore" label="总分" width="80" align="center">
          <template slot-scope="scope">
            <span :style="{ color: getScoreColor(scope.row.totalScore) }">
              {{ formatScore(scope.row.totalScore) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="assessmentLevel" label="考核等级" width="100" align="center">
          <template slot-scope="scope">
            <el-tag
              :type="getLevelTagType(scope.row.assessmentLevel)"
              size="small"
            >
              {{ scope.row.assessmentLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="assessmentStatusName" label="考核状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag
              :type="getStatusTagType(scope.row.assessmentStatus)"
              size="small"
            >
              {{ scope.row.assessmentStatusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="assessorName" label="考核人" width="100" />
        <el-table-column prop="reviewerName" label="审核人" width="100" />
        <el-table-column prop="assessmentDate" label="考核日期" width="120" />
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="small"
              @click="handleViewDetail(scope.row)"
            >
              查看详情
            </el-button>
            <el-button
              type="text"
              size="small"
              @click="handleGenerateReport(scope.row)"
            >
              生成报告
            </el-button>
            <el-button
              v-if="scope.row.assessmentStatus === 2"
              type="text"
              size="small"
              @click="handleReview(scope.row)"
            >
              审核
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          :current-page="pagination.pageNum"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 启动考核对话框 -->
    <StartAssessmentDialog
      :visible.sync="startDialogVisible"
      @success="handleStartSuccess"
    />

    <!-- 考核详情对话框 -->
    <AssessmentDetailDialog
      :visible.sync="detailDialogVisible"
      :assessment-id="currentAssessmentId"
    />

    <!-- 审核对话框 -->
    <ReviewAssessmentDialog
      :visible.sync="reviewDialogVisible"
      :assessment-id="currentAssessmentId"
      @success="handleReviewSuccess"
    />

    <!-- 统计分析对话框 -->
    <StatisticsDialog
      :visible.sync="statisticsDialogVisible"
    />
  </div>
</template>

<script>
import {
  getAssessmentList,
  getAssessmentTypeOptions,
  getAssessmentStatusOptions,
  getAssessmentLevelOptions,
  getAssessmentLevelColor,
  formatAssessmentScore,
  exportAssessmentResults
} from '@/api/contract/assessment'
import StartAssessmentDialog from './components/StartAssessmentDialog'
import AssessmentDetailDialog from './components/AssessmentDetailDialog'
import ReviewAssessmentDialog from './components/ReviewAssessmentDialog'
import StatisticsDialog from './components/StatisticsDialog'

export default {
  name: 'AssessmentIndex',
  components: {
    StartAssessmentDialog,
    AssessmentDetailDialog,
    ReviewAssessmentDialog,
    StatisticsDialog
  },
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      searchForm: {
        projectId: '',
        projectName: '',
        assessmentPeriod: '',
        assessmentType: null,
        assessmentStatus: null,
        assessmentLevel: ''
      },
      pagination: {
        pageNum: 1,
        pageSize: 20,
        total: 0
      },
      // 选项数据
      assessmentTypeOptions: getAssessmentTypeOptions(),
      assessmentStatusOptions: getAssessmentStatusOptions(),
      assessmentLevelOptions: getAssessmentLevelOptions(),
      // 对话框状态
      startDialogVisible: false,
      detailDialogVisible: false,
      reviewDialogVisible: false,
      statisticsDialogVisible: false,
      currentAssessmentId: null
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const params = {
          ...this.searchForm,
          pageNum: this.pagination.pageNum,
          pageSize: this.pagination.pageSize
        }
        const response = await getAssessmentList(params)
        if (response.code === 1) {
          this.tableData = response.data || []
          this.pagination.total = response.result?.total || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('查询失败，请稍后重试')
      } finally {
        this.loading = false
      }
    },

    // 搜索
    handleSearch() {
      this.pagination.pageNum = 1
      this.loadData()
    },

    // 重置
    handleReset() {
      this.searchForm = {
        projectId: '',
        projectName: '',
        assessmentPeriod: '',
        assessmentType: null,
        assessmentStatus: null,
        assessmentLevel: ''
      }
      this.pagination.pageNum = 1
      this.loadData()
    },

    // 分页大小改变
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.pagination.pageNum = 1
      this.loadData()
    },

    // 当前页改变
    handleCurrentChange(val) {
      this.pagination.pageNum = val
      this.loadData()
    },

    // 选择改变
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 启动考核
    handleStartAssessment() {
      this.startDialogVisible = true
    },

    // 启动成功
    handleStartSuccess() {
      this.loadData()
    },

    // 查看详情
    handleViewDetail(row) {
      this.currentAssessmentId = row.id
      this.detailDialogVisible = true
    },

    // 生成报告
    async handleGenerateReport(row) {
      this.$message.info('报告生成功能开发中...')
    },

    // 审核
    handleReview(row) {
      this.currentAssessmentId = row.id
      this.reviewDialogVisible = true
    },

    // 审核成功
    handleReviewSuccess() {
      this.loadData()
    },

    // 导出数据
    async handleExport() {
      this.$message.info('数据导出功能开发中...')
    },

    // 统计分析
    handleStatistics() {
      this.statisticsDialogVisible = true
    },

    // 格式化分数
    formatScore(score) {
      return formatAssessmentScore(score)
    },

    // 获取分数颜色
    getScoreColor(score) {
      if (score >= 90) return '#67C23A'
      if (score >= 80) return '#409EFF'
      if (score >= 70) return '#E6A23C'
      if (score >= 60) return '#F56C6C'
      return '#909399'
    },

    // 获取等级标签类型
    getLevelTagType(level) {
      const typeMap = {
        '优秀': 'success',
        '良好': 'primary',
        '一般': 'warning',
        '合格': 'danger',
        '不合格': 'info'
      }
      return typeMap[level] || 'info'
    },

    // 获取状态标签类型
    getStatusTagType(status) {
      const typeMap = {
        1: 'warning',  // 进行中
        2: 'success',  // 已完成
        3: 'primary'   // 已审核
      }
      return typeMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.assessment-container {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;
    
    h2 {
      margin: 0 0 8px 0;
      color: #303133;
      font-size: 20px;
      font-weight: 500;
    }
    
    p {
      margin: 0;
      color: #606266;
      font-size: 14px;
    }
  }

  .search-card {
    margin-bottom: 20px;
    
    .el-form {
      margin-bottom: -18px;
    }
  }

  .toolbar {
    margin-bottom: 20px;
  }

  .table-card {
    .pagination-wrapper {
      margin-top: 20px;
      text-align: right;
    }
  }
}
</style>
