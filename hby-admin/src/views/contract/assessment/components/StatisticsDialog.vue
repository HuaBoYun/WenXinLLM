<template>
  <el-dialog
    title="考核统计分析"
    :visible.sync="dialogVisible"
    width="1200px"
    :close-on-click-modal="false"
    @open="handleOpen"
    @close="handleClose"
  >
    <div v-loading="loading" class="statistics-container">
      <!-- 查询条件 -->
      <el-form :model="queryForm" :inline="true" size="small" class="query-form">
        <el-form-item label="统计期间">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            @change="handleDateChange"
          />
        </el-form-item>
        <el-form-item label="部门">
          <el-input
            v-model="queryForm.deptId"
            placeholder="部门ID"
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadStatistics">查询</el-button>
        </el-form-item>
      </el-form>

      <!-- 统计概览 -->
      <el-row :gutter="20" class="overview-row">
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-value">{{ statistics.totalProjects || 0 }}</div>
              <div class="stat-label">项目总数</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-value">{{ statistics.completedAssessments || 0 }}</div>
              <div class="stat-label">已完成考核</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-value">{{ statistics.averageScore || 0 }}</div>
              <div class="stat-label">平均分数</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stat-card">
            <div class="stat-item">
              <div class="stat-value">{{ statistics.excellentCount || 0 }}</div>
              <div class="stat-label">优秀项目</div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 等级分布 -->
      <el-card class="chart-card" shadow="never">
        <div slot="header" class="card-header">
          <span>考核等级分布</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="level-stats">
              <div class="level-item excellent">
                <span class="level-label">优秀</span>
                <span class="level-count">{{ statistics.excellentCount || 0 }}</span>
              </div>
              <div class="level-item good">
                <span class="level-label">良好</span>
                <span class="level-count">{{ statistics.goodCount || 0 }}</span>
              </div>
              <div class="level-item average">
                <span class="level-label">一般</span>
                <span class="level-count">{{ statistics.averageCount || 0 }}</span>
              </div>
              <div class="level-item qualified">
                <span class="level-label">合格</span>
                <span class="level-count">{{ statistics.qualifiedCount || 0 }}</span>
              </div>
              <div class="level-item unqualified">
                <span class="level-label">不合格</span>
                <span class="level-count">{{ statistics.unqualifiedCount || 0 }}</span>
              </div>
            </div>
          </el-col>
          <el-col :span="16">
            <div class="chart-placeholder">
              <i class="el-icon-pie-chart" style="font-size: 48px; color: #ddd;"></i>
              <p>图表功能开发中...</p>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 项目排名 -->
      <el-card class="ranking-card" shadow="never">
        <div slot="header" class="card-header">
          <span>项目考核排名</span>
          <el-form :inline="true" size="mini" style="float: right;">
            <el-form-item label="考核期间">
              <el-input
                v-model="rankingQuery.period"
                placeholder="如：2024-01"
                style="width: 120px"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="mini" @click="loadRanking">查询</el-button>
            </el-form-item>
          </el-form>
        </div>
        <el-table
          :data="rankingData"
          stripe
          size="small"
          style="width: 100%"
        >
          <el-table-column prop="rank" label="排名" width="80" align="center">
            <template slot-scope="scope">
              <el-tag
                :type="getRankTagType(scope.row.rank)"
                size="mini"
              >
                第{{ scope.row.rank }}名
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="projectId" label="项目ID" width="120" />
          <el-table-column prop="projectName" label="项目名称" width="200" show-overflow-tooltip />
          <el-table-column prop="totalScore" label="总分" width="100" align="center">
            <template slot-scope="scope">
              <span :style="{ color: getScoreColor(scope.row.totalScore) }">
                {{ formatScore(scope.row.totalScore) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="assessmentLevel" label="等级" width="100" align="center">
            <template slot-scope="scope">
              <el-tag
                :type="getLevelTagType(scope.row.assessmentLevel)"
                size="mini"
              >
                {{ scope.row.assessmentLevel }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="projectManager" label="项目经理" width="120" />
        </el-table>
      </el-card>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleExport">导出报告</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  getAssessmentStatistics,
  getAssessmentRanking,
  formatAssessmentScore
} from '@/api/contract/assessment'

export default {
  name: 'StatisticsDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      loading: false,
      dateRange: [],
      queryForm: {
        startDate: '',
        endDate: '',
        deptId: ''
      },
      rankingQuery: {
        period: ''
      },
      statistics: {},
      rankingData: []
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  methods: {
    // 对话框打开
    async handleOpen() {
      // 设置默认查询时间为最近30天
      const endDate = new Date()
      const startDate = new Date()
      startDate.setDate(startDate.getDate() - 30)
      
      this.dateRange = [
        startDate.toISOString().split('T')[0],
        endDate.toISOString().split('T')[0]
      ]
      this.handleDateChange(this.dateRange)
      
      // 设置默认排名查询期间
      const currentMonth = new Date().toISOString().slice(0, 7)
      this.rankingQuery.period = currentMonth
      
      await this.loadStatistics()
      await this.loadRanking()
    },

    // 日期范围改变
    handleDateChange(dateRange) {
      if (dateRange && dateRange.length === 2) {
        this.queryForm.startDate = dateRange[0]
        this.queryForm.endDate = dateRange[1]
      } else {
        this.queryForm.startDate = ''
        this.queryForm.endDate = ''
      }
    },

    // 加载统计数据
    async loadStatistics() {
      this.loading = true
      try {
        const response = await getAssessmentStatistics(this.queryForm)
        if (response.code === 1) {
          this.statistics = response.data || {}
        } else {
          this.$message.error(response.msg || '加载统计数据失败')
        }
      } catch (error) {
        console.error('加载统计数据失败:', error)
        this.$message.error('加载统计数据失败，请稍后重试')
      } finally {
        this.loading = false
      }
    },

    // 加载排名数据
    async loadRanking() {
      try {
        const params = {
          period: this.rankingQuery.period,
          limit: 10
        }
        const response = await getAssessmentRanking(params)
        if (response.code === 1) {
          this.rankingData = response.data || []
        } else {
          this.$message.error(response.msg || '加载排名数据失败')
        }
      } catch (error) {
        console.error('加载排名数据失败:', error)
        this.$message.error('加载排名数据失败，请稍后重试')
      }
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.statistics = {}
      this.rankingData = []
    },

    // 导出报告
    handleExport() {
      this.$message.info('导出功能开发中...')
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

    // 获取排名标签类型
    getRankTagType(rank) {
      if (rank === 1) return 'danger'
      if (rank === 2) return 'warning'
      if (rank === 3) return 'success'
      return 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.statistics-container {
  .query-form {
    margin-bottom: 20px;
    padding: 15px;
    background: #f5f7fa;
    border-radius: 4px;
  }

  .overview-row {
    margin-bottom: 20px;
    
    .stat-card {
      text-align: center;
      
      .stat-item {
        .stat-value {
          font-size: 28px;
          font-weight: bold;
          color: #409EFF;
          margin-bottom: 8px;
        }
        
        .stat-label {
          font-size: 14px;
          color: #606266;
        }
      }
    }
  }

  .chart-card,
  .ranking-card {
    margin-bottom: 20px;
    
    .card-header {
      font-weight: 500;
      color: #303133;
    }
    
    .level-stats {
      .level-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 12px 16px;
        margin-bottom: 8px;
        border-radius: 4px;
        
        &.excellent {
          background: #f0f9ff;
          border-left: 4px solid #67C23A;
        }
        
        &.good {
          background: #f0f9ff;
          border-left: 4px solid #409EFF;
        }
        
        &.average {
          background: #fdf6ec;
          border-left: 4px solid #E6A23C;
        }
        
        &.qualified {
          background: #fef0f0;
          border-left: 4px solid #F56C6C;
        }
        
        &.unqualified {
          background: #f4f4f5;
          border-left: 4px solid #909399;
        }
        
        .level-label {
          font-weight: 500;
        }
        
        .level-count {
          font-size: 18px;
          font-weight: bold;
        }
      }
    }
    
    .chart-placeholder {
      height: 300px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      color: #909399;
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>
